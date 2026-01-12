package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.ElectronicPart;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility for parametric searching of electronic components.
 *
 * Supports filtering components by their specifications using:
 * - Range queries: ">= 10nF", "< 100V", "> 1k", "<= 5%"
 * - Equality: "= 100nF", "= X7R"
 * - Inequality: "!= X5R"
 * - Set membership: "IN(X7R, X5R, C0G)", "IN(0402, 0603, 0805)"
 * - Range between: "10nF..100nF", "1k..10k"
 *
 * Values are parsed with unit awareness using {@link ComponentValueStandardizer}.
 *
 * Example usage:
 * <pre>
 * List&lt;ElectronicPart&gt; capacitors = ...;
 *
 * Map&lt;String, String&gt; requirements = Map.of(
 *     "capacitance", "&gt;= 10nF",
 *     "voltage", "&gt;= 25V",
 *     "dielectric", "IN(X7R, X5R)"
 * );
 *
 * List&lt;ElectronicPart&gt; matches = ParametricSearch.filter(capacitors, requirements);
 * </pre>
 */
public class ParametricSearch {

    private static final ComponentValueStandardizer VALUE_STANDARDIZER = new ComponentValueStandardizer();

    // Pattern for numeric comparison: >= 10nF, < 100V, etc.
    private static final Pattern COMPARISON_PATTERN =
            Pattern.compile("^\\s*([<>]=?|=|!=)\\s*(.+)$");

    // Pattern for range: 10nF..100nF
    private static final Pattern RANGE_PATTERN =
            Pattern.compile("^\\s*(.+?)\\s*\\.\\.\\s*(.+)\\s*$");

    // Pattern for set membership: IN(X7R, X5R, C0G)
    private static final Pattern IN_PATTERN =
            Pattern.compile("^\\s*IN\\s*\\((.+)\\)\\s*$", Pattern.CASE_INSENSITIVE);

    // Unit multipliers for parsing
    private static final Map<String, BigDecimal> MULTIPLIERS = new LinkedHashMap<>();
    static {
        MULTIPLIERS.put("p", new BigDecimal("1e-12"));   // pico
        MULTIPLIERS.put("n", new BigDecimal("1e-9"));    // nano
        MULTIPLIERS.put("u", new BigDecimal("1e-6"));    // micro
        MULTIPLIERS.put("µ", new BigDecimal("1e-6"));    // micro (symbol)
        MULTIPLIERS.put("m", new BigDecimal("1e-3"));    // milli
        MULTIPLIERS.put("k", new BigDecimal("1e3"));     // kilo
        MULTIPLIERS.put("K", new BigDecimal("1e3"));     // kilo (alt)
        MULTIPLIERS.put("M", new BigDecimal("1e6"));     // mega
        MULTIPLIERS.put("G", new BigDecimal("1e9"));     // giga
    }

    /**
     * Filters a collection of parts by parametric requirements.
     *
     * @param parts The parts to filter
     * @param requirements Map of spec key to requirement string
     * @return List of parts matching all requirements
     */
    public static List<ElectronicPart> filter(
            Collection<? extends ElectronicPart> parts,
            Map<String, String> requirements) {

        if (parts == null || parts.isEmpty()) {
            return Collections.emptyList();
        }
        if (requirements == null || requirements.isEmpty()) {
            return new ArrayList<>(parts);
        }

        // Build predicates for each requirement
        List<Predicate<ElectronicPart>> predicates = requirements.entrySet().stream()
                .map(e -> buildPredicate(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        // Filter parts that match ALL predicates
        return parts.stream()
                .filter(part -> predicates.stream().allMatch(p -> p.test(part)))
                .collect(Collectors.toList());
    }

    /**
     * Filters parts using a fluent builder pattern.
     *
     * @param parts The parts to search
     * @return A SearchBuilder for chaining requirements
     */
    public static SearchBuilder search(Collection<? extends ElectronicPart> parts) {
        return new SearchBuilder(parts);
    }

    /**
     * Checks if a single part meets a requirement.
     *
     * @param part The part to check
     * @param specKey The specification key
     * @param requirement The requirement string
     * @return true if the part meets the requirement
     */
    public static boolean meets(ElectronicPart part, String specKey, String requirement) {
        return buildPredicate(specKey, requirement).test(part);
    }

    /**
     * Builds a predicate for a single requirement.
     */
    private static Predicate<ElectronicPart> buildPredicate(String specKey, String requirement) {
        if (requirement == null || requirement.trim().isEmpty()) {
            return part -> true; // No requirement = always matches
        }

        String req = requirement.trim();

        // Try IN pattern first
        Matcher inMatcher = IN_PATTERN.matcher(req);
        if (inMatcher.matches()) {
            Set<String> allowedValues = parseInValues(inMatcher.group(1));
            return part -> {
                String value = getSpecValue(part, specKey);
                return value != null && allowedValues.contains(value.toUpperCase());
            };
        }

        // Try range pattern (10nF..100nF)
        Matcher rangeMatcher = RANGE_PATTERN.matcher(req);
        if (rangeMatcher.matches()) {
            BigDecimal minValue = parseNumericValue(rangeMatcher.group(1));
            BigDecimal maxValue = parseNumericValue(rangeMatcher.group(2));
            return part -> {
                BigDecimal value = parsePartSpecValue(part, specKey);
                return value != null &&
                       value.compareTo(minValue) >= 0 &&
                       value.compareTo(maxValue) <= 0;
            };
        }

        // Try comparison pattern (>=, <=, >, <, =, !=)
        Matcher compMatcher = COMPARISON_PATTERN.matcher(req);
        if (compMatcher.matches()) {
            String operator = compMatcher.group(1);
            String operand = compMatcher.group(2).trim();

            // Check if operand is numeric
            BigDecimal numericOperand = parseNumericValue(operand);
            if (numericOperand != null) {
                return buildNumericPredicate(specKey, operator, numericOperand);
            } else {
                // String comparison
                return buildStringPredicate(specKey, operator, operand);
            }
        }

        // Default: exact string match
        return part -> {
            String value = getSpecValue(part, specKey);
            return req.equalsIgnoreCase(value);
        };
    }

    /**
     * Builds a predicate for numeric comparison.
     */
    private static Predicate<ElectronicPart> buildNumericPredicate(
            String specKey, String operator, BigDecimal operand) {

        return part -> {
            BigDecimal value = parsePartSpecValue(part, specKey);
            if (value == null) {
                return false;
            }

            return switch (operator) {
                case ">" -> value.compareTo(operand) > 0;
                case ">=" -> value.compareTo(operand) >= 0;
                case "<" -> value.compareTo(operand) < 0;
                case "<=" -> value.compareTo(operand) <= 0;
                case "=" -> value.compareTo(operand) == 0;
                case "!=" -> value.compareTo(operand) != 0;
                default -> false;
            };
        };
    }

    /**
     * Builds a predicate for string comparison.
     */
    private static Predicate<ElectronicPart> buildStringPredicate(
            String specKey, String operator, String operand) {

        return part -> {
            String value = getSpecValue(part, specKey);
            if (value == null) {
                return "!=".equals(operator); // null != anything is true
            }

            return switch (operator) {
                case "=" -> operand.equalsIgnoreCase(value);
                case "!=" -> !operand.equalsIgnoreCase(value);
                default -> false; // <, >, <=, >= don't make sense for strings
            };
        };
    }

    /**
     * Gets a spec value from a part, checking both specs map and common fields.
     */
    private static String getSpecValue(ElectronicPart part, String specKey) {
        if (part == null) {
            return null;
        }

        // Check specs map first
        Map<String, String> specs = part.getSpecs();
        if (specs != null && specs.containsKey(specKey)) {
            return specs.get(specKey);
        }

        // Check common fields
        return switch (specKey.toLowerCase()) {
            case "value" -> part.getValue();
            case "package", "pkg" -> part.getPkg();
            case "manufacturer", "mfr" -> part.getManufacturer();
            case "description", "desc" -> part.getDescription();
            case "mpn" -> part.getMpn();
            default -> null;
        };
    }

    /**
     * Parses a part's spec value to a numeric BigDecimal.
     */
    private static BigDecimal parsePartSpecValue(ElectronicPart part, String specKey) {
        String stringValue = getSpecValue(part, specKey);
        if (stringValue == null) {
            return null;
        }
        return parseNumericValue(stringValue);
    }

    /**
     * Parses a value string like "100nF", "4.7k", "25V" to a normalized BigDecimal.
     */
    static BigDecimal parseNumericValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String cleaned = value.trim()
                .replace(" ", "")
                .replace("Ω", "")
                .replace("F", "")
                .replace("H", "")
                .replace("V", "")
                .replace("A", "")
                .replace("W", "")
                .replace("Hz", "")
                .replace("%", "");

        // Handle percentage separately
        if (value.contains("%")) {
            try {
                String numPart = value.replace("%", "").trim();
                return new BigDecimal(numPart);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        // Try direct parse first
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            // Continue to prefix parsing
        }

        // Try to extract number and prefix
        Pattern valuePattern = Pattern.compile("^([\\d.]+)([pnuµmkKMG])?.*$");
        Matcher matcher = valuePattern.matcher(cleaned);

        if (matcher.matches()) {
            try {
                BigDecimal number = new BigDecimal(matcher.group(1));
                String prefix = matcher.group(2);

                if (prefix != null && MULTIPLIERS.containsKey(prefix)) {
                    number = number.multiply(MULTIPLIERS.get(prefix));
                }

                return number;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        // Try ComponentValueStandardizer as fallback
        try {
            ComponentValueStandardizer.StandardizedValue standardized =
                    VALUE_STANDARDIZER.standardize(value, null);
            if (standardized.isValid()) {
                return standardized.value();
            }
        } catch (Exception e) {
            // Ignore and return null
        }

        return null;
    }

    /**
     * Parses IN clause values.
     */
    private static Set<String> parseInValues(String valuesStr) {
        return Arrays.stream(valuesStr.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Fluent builder for parametric search.
     */
    public static class SearchBuilder {
        private final Collection<? extends ElectronicPart> parts;
        private final Map<String, String> requirements = new LinkedHashMap<>();

        SearchBuilder(Collection<? extends ElectronicPart> parts) {
            this.parts = parts;
        }

        /**
         * Adds a requirement.
         */
        public SearchBuilder where(String specKey, String requirement) {
            requirements.put(specKey, requirement);
            return this;
        }

        /**
         * Adds a minimum value requirement (>=).
         */
        public SearchBuilder min(String specKey, String value) {
            requirements.put(specKey, ">= " + value);
            return this;
        }

        /**
         * Adds a maximum value requirement (<=).
         */
        public SearchBuilder max(String specKey, String value) {
            requirements.put(specKey, "<= " + value);
            return this;
        }

        /**
         * Adds a range requirement (min..max).
         */
        public SearchBuilder range(String specKey, String min, String max) {
            requirements.put(specKey, min + ".." + max);
            return this;
        }

        /**
         * Adds an exact match requirement.
         */
        public SearchBuilder equals(String specKey, String value) {
            requirements.put(specKey, "= " + value);
            return this;
        }

        /**
         * Adds a set membership requirement.
         */
        public SearchBuilder in(String specKey, String... values) {
            requirements.put(specKey, "IN(" + String.join(", ", values) + ")");
            return this;
        }

        /**
         * Adds an exclusion requirement.
         */
        public SearchBuilder notEquals(String specKey, String value) {
            requirements.put(specKey, "!= " + value);
            return this;
        }

        /**
         * Executes the search and returns matching parts.
         */
        public List<ElectronicPart> find() {
            return filter(parts, requirements);
        }

        /**
         * Returns the first matching part, or empty if none.
         */
        public Optional<ElectronicPart> findFirst() {
            return find().stream().findFirst();
        }

        /**
         * Returns the count of matching parts.
         */
        public long count() {
            return find().size();
        }

        /**
         * Checks if any parts match.
         */
        public boolean anyMatch() {
            return !find().isEmpty();
        }
    }
}
