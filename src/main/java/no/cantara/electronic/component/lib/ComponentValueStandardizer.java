package no.cantara.electronic.component.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.HashMap;

public class ComponentValueStandardizer {
    private static final Logger logger = LoggerFactory.getLogger(ComponentValueStandardizer.class);
    // Value format patterns
    private static final Pattern STANDARD_NOTATION = Pattern.compile("^(\\d*\\.?\\d+)([pnuμmkKMG])?([FΩH])?$");
    private static final Pattern ENGINEERING_NOTATION = Pattern.compile("^(\\d*\\.?\\d+)[Ee]([+-]?\\d+)([FΩH])?$");
    private static final Pattern SHORTENED_NOTATION = Pattern.compile("^(\\d+)([RKMG])(\\d+)?$");
    private static final Pattern VALUE_WITH_TOLERANCE = Pattern.compile("^(\\d*\\.?\\d+)([pnuμmkKMG])?([FΩH])?\\s*(±?\\d+%)?$");
    private static final Pattern VALUE_PATTERN = Pattern.compile("^(\\d*\\.?\\d+)([pnuµmkKMG])?([FΩV])?$");

    // Unit multipliers
    private static final Map<String, BigDecimal> MULTIPLIERS = new HashMap<>();

    static {
        MULTIPLIERS.put("p", new BigDecimal("0.000000000001"));  // pico
        MULTIPLIERS.put("n", new BigDecimal("0.000000001"));     // nano
        MULTIPLIERS.put("u", new BigDecimal("0.000001"));        // micro
        MULTIPLIERS.put("μ", new BigDecimal("0.000001"));        // micro (alternative)
        MULTIPLIERS.put("m", new BigDecimal("0.001"));           // milli
        MULTIPLIERS.put("k", new BigDecimal("1000"));            // kilo
        MULTIPLIERS.put("K", new BigDecimal("1000"));            // kilo (alternative)
        MULTIPLIERS.put("M", new BigDecimal("1000000"));         // mega
        MULTIPLIERS.put("G", new BigDecimal("1000000000"));      // giga
    }

    // E-series values (E24 series)
    private static final double[] E24_VALUES = {
            1.0, 1.1, 1.2, 1.3, 1.5, 1.6, 1.8, 2.0, 2.2, 2.4, 2.7, 3.0,
            3.3, 3.6, 3.9, 4.3, 4.7, 5.1, 5.6, 6.2, 6.8, 7.5, 8.2, 9.1
    };

    /**
     * Standardize a component value
     */
    public StandardizedValue standardize(String value, ComponentType type) {
        if (value == null || value.trim().isEmpty()) {
            return new StandardizedValue(BigDecimal.ZERO, "", 0, false);
        }

        value = value.trim().replace(" ", "");

        // Try different notation patterns
        StandardizedValue result = tryStandardNotation(value);
        if (result == null) result = tryEngineeringNotation(value);
        if (result == null) result = tryShortenedNotation(value);

        if (result == null) {
            return new StandardizedValue(BigDecimal.ZERO, "", 0, false);
        }

        // Add appropriate unit based on component type
        String unit = determineUnit(type);
        return new StandardizedValue(result.value(), unit, result.tolerance(), true);
    }

    private StandardizedValue tryStandardNotation(String value) {
        Matcher matcher = STANDARD_NOTATION.matcher(value);
        if (!matcher.matches()) return null;

        try {
            BigDecimal numericValue = new BigDecimal(matcher.group(1));
            String prefix = matcher.group(2);

            if (prefix != null) {
                numericValue = numericValue.multiply(MULTIPLIERS.getOrDefault(prefix, BigDecimal.ONE));
            }

            // Extract tolerance if present
            double tolerance = extractTolerance(value);

            return new StandardizedValue(numericValue, matcher.group(3), tolerance, true);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private StandardizedValue tryEngineeringNotation(String value) {
        Matcher matcher = ENGINEERING_NOTATION.matcher(value);
        if (!matcher.matches()) return null;

        try {
            BigDecimal numericValue = new BigDecimal(matcher.group(1));
            int exponent = Integer.parseInt(matcher.group(2));

            BigDecimal multiplier = BigDecimal.TEN.pow(exponent);
            numericValue = numericValue.multiply(multiplier);

            // Extract tolerance if present
            double tolerance = extractTolerance(value);

            return new StandardizedValue(numericValue, matcher.group(3), tolerance, true);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private StandardizedValue tryShortenedNotation(String value) {
        Matcher matcher = SHORTENED_NOTATION.matcher(value);
        if (!matcher.matches()) return null;

        try {
            String baseValue = matcher.group(1);
            String multiplier = matcher.group(2);
            String decimal = matcher.group(3);

            BigDecimal numericValue = new BigDecimal(baseValue);

            if (multiplier.equals("R")) {
                // Handle decimal point notation (e.g., 4R7 = 4.7)
                if (decimal != null) {
                    numericValue = new BigDecimal(baseValue + "." + decimal);
                }
            } else {
                // Handle multiplier with optional decimal (e.g., 4K7 = 4.7k)
                numericValue = numericValue.multiply(MULTIPLIERS.get(multiplier));
                if (decimal != null) {
                    BigDecimal decimalPart = new BigDecimal("0." + decimal);
                    decimalPart = decimalPart.multiply(MULTIPLIERS.get(multiplier));
                    numericValue = numericValue.add(decimalPart);
                }
            }

            // Extract tolerance if present
            double tolerance = extractTolerance(value);

            return new StandardizedValue(numericValue, "", tolerance, true);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String determineUnit(ComponentType type) {
        ComponentType baseType = type.getBaseType();
        return switch (baseType) {
            case RESISTOR -> "Ω";
            case CAPACITOR -> "F";
            case INDUCTOR -> "H";
            default -> "";
        };
    }

    private double extractTolerance(String value) {
        Matcher matcher = VALUE_WITH_TOLERANCE.matcher(value);
        if (matcher.matches() && matcher.group(4) != null) {
            String toleranceStr = matcher.group(4).replace("±", "").replace("%", "");
            try {
                return Double.parseDouble(toleranceStr) / 100.0;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    /**
     * Format a value to a human-readable string
     */
    public String format(BigDecimal value, String unit) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "0" + unit;
        }

        BigDecimal absValue = value.abs();
        String prefix = "";
        BigDecimal scaledValue = value;

        // Define scale ranges
        ScaleRange[] ranges = {
                new ScaleRange("p", new BigDecimal("0.000000000001"), new BigDecimal("0.000000001")),
                new ScaleRange("n", new BigDecimal("0.000000001"), new BigDecimal("0.000001")),
                new ScaleRange("μ", new BigDecimal("0.000001"), new BigDecimal("0.001")),
                new ScaleRange("m", new BigDecimal("0.001"), new BigDecimal("1")),
                new ScaleRange("k", new BigDecimal("1000"), new BigDecimal("1000000")),
                new ScaleRange("M", new BigDecimal("1000000"), new BigDecimal("1000000000")),
                new ScaleRange("G", new BigDecimal("1000000000"), new BigDecimal("1000000000000"))
        };

        for (ScaleRange range : ranges) {
            if (range.contains(absValue)) {
                prefix = range.prefix;
                scaledValue = value.divide(MULTIPLIERS.get(prefix), 3, RoundingMode.HALF_UP);
                break;
            }
        }

        return scaledValue.stripTrailingZeros().toPlainString() + prefix + unit;
    }

    /**
     * Find the nearest E24 series value
     */
    public BigDecimal findNearestE24Value(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // Find the decade
        double doubleValue = value.doubleValue();
        int decade = (int) Math.floor(Math.log10(doubleValue));
        double normalizedValue = doubleValue / Math.pow(10, decade);

        // Find nearest E24 value
        double nearest = E24_VALUES[0];
        double minDiff = Math.abs(normalizedValue - E24_VALUES[0]);

        for (double e24Value : E24_VALUES) {
            double diff = Math.abs(normalizedValue - e24Value);
            if (diff < minDiff) {
                minDiff = diff;
                nearest = e24Value;
            }
        }

        // Scale back to original decade
        return BigDecimal.valueOf(nearest * Math.pow(10, decade));
    }

    private static class ScaleRange {
        final String prefix;
        final BigDecimal min;
        final BigDecimal max;

        ScaleRange(String prefix, BigDecimal min, BigDecimal max) {
            this.prefix = prefix;
            this.min = min;
            this.max = max;
        }

        boolean contains(BigDecimal value) {
            return value.compareTo(min) >= 0 && value.compareTo(max) < 0;
        }
    }

    /**
     * Record to hold standardized value information
     */
    public record StandardizedValue(BigDecimal value, String unit, double tolerance, boolean isValid) {
        public String format() {
            String formattedValue = new ComponentValueStandardizer().format(value, unit);
            if (tolerance > 0) {
                return formattedValue + " ±" + (tolerance * 100) + "%";
            }
            return formattedValue;
        }

        public StandardizedValue findNearestE24() {
            if (!isValid) return this;
            BigDecimal nearestE24 = new ComponentValueStandardizer().findNearestE24Value(value);
            return new StandardizedValue(nearestE24, unit, tolerance, true);
        }

        @Override
        public String toString() {
            return format();
        }
    }

    /**
     * Check if a value is valid for the given component type
     */
    public boolean isValidValue(String value, ComponentType type) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        String normalizedValue = value.trim().toUpperCase();
        ComponentType baseType = type.getBaseType();

        try {
            return switch (baseType) {
                case RESISTOR -> isValidResistorValue(normalizedValue);
                case CAPACITOR -> isValidCapacitorValue(normalizedValue);
                case INDUCTOR -> isValidInductorValue(normalizedValue);
                case CRYSTAL, OSCILLATOR -> isValidFrequencyValue(normalizedValue);
                case VOLTAGE_REGULATOR -> isValidVoltageValue(normalizedValue);
                default -> true; // For non-value components, any non-empty string is valid
            };
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if string matches resistor value format
     */
    private boolean isValidResistorValue(String value) {
        // Zero ohm resistor
        if (value.equals("0") || value.equalsIgnoreCase("0R")) {
            return true;
        }

        // Common resistor value formats:
        // 100R, 4.7K, 1M, 4K7, etc.
        return value.matches("^\\d+(\\.\\d+)?[RKMΩkM]\\d*$") ||    // With unit
                value.matches("^\\d+(\\.\\d+)?$") ||                 // Just number
                value.matches("^\\d+[RKM]\\d+$") ||                 // Format like 4K7
                value.matches("^R\\d+$");                           // Format like R47
    }

    /**
     * Check if string matches capacitor value format
     */
    private boolean isValidCapacitorValue(String value) {
        // Common capacitor value formats:
        // 100n, 10u, 4.7u, 100p, etc.
        return value.matches("^\\d+(\\.\\d+)?[PNUΜMK]?[F]?$") ||    // With or without unit/prefix
                value.matches("^\\d+(\\.\\d+)?[pnuµmk][fF]?$") ||    // With prefix, optional F
                value.matches("^\\d+[PNUΜMK]\\d+[F]?$");
    }

    /**
     * Check if string matches inductor value format
     */
    private boolean isValidInductorValue(String value) {
        // Common inductor value formats:
        // 10uH, 100nH, 1mH, 4u7H, etc.
        boolean isValid = value.matches("^\\d+(\\.\\d+)?[pnuµmkM]H$") ||     // Standard format with H
                value.matches("^\\d+(\\.\\d+)?[pnuµmkM]h$") ||     // Lowercase h
                value.matches("^\\d+(\\.\\d+)?[PNUΜMK]H$") ||     // Uppercase prefix
                value.matches("^\\d+[pnuµmkM]\\d+H$");            // Format like 4u7H

        logger.trace("Inductor value validation for '{}': {}", value, isValid);
        return isValid;
    }

    /**
     * Check if string matches frequency value format
     */
    private boolean isValidFrequencyValue(String value) {
        // Common frequency value formats:
        // 16M, 32.768K, etc.
        return value.matches("^\\d+(\\.\\d+)?[kKmM][hH][zZ]?$") ||  // With unit
                value.matches("^\\d+(\\.\\d+)?[hH][zZ]?$") ||        // With Hz
                value.matches("^\\d+(\\.\\d+)?$");                    // Just number
    }

    /**
     * Check if string matches voltage value format
     */
    private boolean isValidVoltageValue(String value) {
        // Common voltage value formats:
        // 3.3V, 5V, 3V3, etc.
        return value.matches("^\\d+(\\.\\d+)?[vV]?$") ||            // With or without V
                value.matches("^\\d+[vV]\\d+$");                      // Format like 3V3
    }

    /**
     * Standardize resistor value (e.g., "100OHM" -> "100Ω", "4K7" -> "4.7kΩ")
     */
    public String standardizeResistorValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        // Remove any spaces and convert to uppercase
        value = value.replaceAll("\\s+", "").toUpperCase();

        // Handle special case for zero ohm resistors
        if (value.equals("0") || value.equalsIgnoreCase("0R")) {
            return "0Ω";
        }

        // Remove "OHM" text and replace with Ω
        value = value.replace("OHM", "")
                .replace("Ω", "");  // Remove any existing Ω to avoid duplication

        // Convert values like "4K7" to "4.7K"
        if (value.matches("\\d+[RKM]\\d+")) {
            char unit = value.charAt(value.indexOf(String.valueOf(value).replaceAll("[\\d.]", "").charAt(0)));
            String[] parts = value.split(String.valueOf(unit));
            value = parts[0] + "." + parts[1] + unit;
        }

        // Standardize unit notation
        value = value.replace("R", "Ω")
                .replace("K", "kΩ")
                .replace("M", "MΩ");

        // Add Ω if only number is provided
        if (value.matches("^\\d+(\\.\\d+)?$")) {
            value += "Ω";
        }

        return value;
    }

    /**
     * Standardize capacitor value (e.g., "100n" -> "100nF")
     */
    public String standardizeCapacitorValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        value = value.replaceAll("\\s+", "").toUpperCase();

        // Handle special notations
        value = value.replace("UF", "µF")
                .replace("U", "µ")
                .replace("MF", "mF");

        // Handle format like 4µ7 -> 4.7µF
        if (value.matches("^\\d+[pnµumkMG]\\d+$")) {
            String prefix = value.replaceAll("^\\d+|\\d+$", "");
            String[] numbers = value.split("[pnµumkMG]");
            return numbers[0] + "." + numbers[1] + "µF";
        }

        // Add F if only number and prefix are provided
        if (value.matches("^\\d+(\\.\\d+)?[pnµmu]$")) {
            value += "F";
        }

        // Add F if only number is provided (assumed to be in µF)
        if (value.matches("^\\d+(\\.\\d+)?$")) {
            value += "µF";
        }

        // Handle case where prefix is present but no unit
        if (value.matches("^\\d+(\\.\\d+)?[PNUMKG]$")) {
            String number = value.replaceAll("[PNUMKG]$", "");
            String prefix = value.substring(value.length() - 1);
            return number + prefix.toLowerCase() + "F";
        }

        return value;
    }

    /**
     * Standardize inductor value (e.g., "100n" -> "100nH", "4u7" -> "4.7µH")
     */
    public String standardizeInductorValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        value = value.replaceAll("\\s+", "").toUpperCase();

        // Handle special notations
        value = value.replace("UH", "µH")
                .replace("U", "µ");

        // Handle format like 4µ7 -> 4.7µH
        if (value.matches("^\\d+[pnµumkMG]\\d+$")) {
            String prefix = value.replaceAll("^\\d+|\\d+$", "");
            String[] numbers = value.split("[pnµumkMG]");
            return numbers[0] + "." + numbers[1] + "µH";
        }

        // Add H if only number and prefix are provided
        if (value.matches("^\\d+(\\.\\d+)?[pnµmu]$")) {
            value += "H";
        }

        // Add H if only number is provided (assumed to be in µH)
        if (value.matches("^\\d+(\\.\\d+)?$")) {
            value += "µH";
        }

        // Handle case where prefix is present but no unit
        if (value.matches("^\\d+(\\.\\d+)?[PNUMKG]$")) {
            String number = value.replaceAll("[PNUMKG]$", "");
            String prefix = value.substring(value.length() - 1);
            return number + prefix.toLowerCase() + "H";
        }

        return value;
    }

    /**
     * Standardize any component value based on type
     */
    public String standardizeValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        // Remove any whitespace
        String cleanValue = value.trim().replace(" ", "");

        // Try to match the pattern
        Matcher matcher = VALUE_PATTERN.matcher(cleanValue);
        if (matcher.matches()) {
            String number = matcher.group(1);
            String prefix = matcher.group(2);
            String unit = matcher.group(3);

            // Standardize the prefix if present
            if (prefix != null) {
                prefix = standardizePrefix(prefix);
            }

            // Keep the unit if present
            if (unit != null) {
                // Handle special cases for units
                unit = standardizeUnit(unit);
                return number + prefix + unit;
            }

            return number + (prefix != null ? prefix : "");
        }

        // If no match, return original value
        return cleanValue;
    }

    private String standardizeUnit(String unit) {
        return switch (unit) {
            case "F" -> "F";  // Farads for capacitors
            case "Ω", "R" -> "Ω";  // Ohms for resistors
            case "V" -> "V";  // Volts
            case "H" -> "H";  // Henries for inductors
            default -> unit;
        };
    }

    public String standardizeValue(String value, ComponentType type) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        // Remove any whitespace
        String cleanValue = value.trim().replace(" ", "");

        // Special handling for inductors
        if (type != null && type.getBaseType() == ComponentType.INDUCTOR) {
            // Try to match the pattern
            Matcher matcher = VALUE_PATTERN.matcher(cleanValue);
            if (matcher.matches()) {
                String number = matcher.group(1);
                String prefix = matcher.group(2);

                // Convert 'u' to 'µ' for inductors and always add 'H'
                if (prefix != null && prefix.toLowerCase().equals("u")) {
                    return number + "µH";
                }
            }
        }

        // Regular pattern matching
        Matcher matcher = VALUE_PATTERN.matcher(cleanValue);
        if (matcher.matches()) {
            String number = matcher.group(1);
            String prefix = matcher.group(2);
            String unit = matcher.group(3);

            // Standardize the prefix if present
            if (prefix != null) {
                prefix = standardizePrefix(prefix);
            }

            // Handle units based on component type
            if (unit != null) {
                unit = standardizeUnit(unit, type);
            } else {
                unit = getDefaultUnit(type);
            }

            return number + (prefix != null ? prefix : "") + unit;
        }

        // If no match, return original value
        return cleanValue;
    }

    private String getDefaultUnit(ComponentType type) {
        if (type == null) {
            return "";
        }

        return switch (type.getBaseType()) {
            case CAPACITOR -> "F";
            case RESISTOR -> "Ω";
            case VOLTAGE_REGULATOR,
                 DIODE,
                 MOSFET -> "V";
            default -> "";
        };
    }

    private String standardizeUnit(String unit, ComponentType type) {
        if (type == null) {
            return unit;
        }

        return switch (type.getBaseType()) {
            case CAPACITOR -> "F";
            case RESISTOR -> "Ω";
            case VOLTAGE_REGULATOR,
                 DIODE,
                 MOSFET -> "V";
            default -> unit;
        };
    }

    private String standardizePrefix(String prefix) {
        return switch (prefix.toLowerCase()) {
            case "p" -> "p";
            case "n" -> "n";
            case "u", "µ" -> "u";
            case "m" -> "m";
            case "k", "K" -> "k";
            case "M" -> "M";
            case "G" -> "G";
            default -> prefix;
        };
    }

}