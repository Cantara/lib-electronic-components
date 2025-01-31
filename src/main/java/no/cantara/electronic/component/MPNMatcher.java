package no.cantara.electronic.component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class MPNMatcher {
    private static final Pattern LETTER_PREFIX = Pattern.compile("^[A-Za-z]+");
    private static final Pattern NUMBER_SEQUENCE = Pattern.compile("\\d+");
    private static final Pattern LETTER_SUFFIX = Pattern.compile("[A-Za-z]+$");
    private static final Pattern VALUE_PATTERN = Pattern.compile("(\\d+)(R|K|M|F|H|U|N|P)?");
    private static final Pattern SIZE_CODE_PATTERN = Pattern.compile("\\d{4}");
    private static final Pattern TOLERANCE_CODE = Pattern.compile("[FGJKM]");

    // Common package size mappings (imperial to metric)
    private static final Map<String, String> PACKAGE_SIZE_MAPPINGS = new HashMap<String, String>() {{
        put("0201", "0603");
        put("0402", "1005");
        put("0603", "1608");
        put("0805", "2012");
        put("1206", "3216");
        put("1210", "3225");
        put("1812", "4532");
        put("2010", "5025");
        put("2512", "6332");
    }};

    /**
     * Extract the prefix part of an MPN
     */
    public String extractPrefix(String mpn) {
        Matcher matcher = LETTER_PREFIX.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    /**
     * Extract the numeric part of an MPN
     */
    public String extractNumericPart(String mpn) {
        Matcher matcher = NUMBER_SEQUENCE.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    /**
     * Extract the suffix part of an MPN
     */
    public String extractSuffix(String mpn) {
        Matcher matcher = LETTER_SUFFIX.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    /**
     * Extract component value (for passive components)
     */
    public ComponentValue extractValue(String mpn) {
        Matcher matcher = VALUE_PATTERN.matcher(mpn);
        if (!matcher.find()) {
            return new ComponentValue(0, "");
        }

        String numericPart = matcher.group(1);
        String unit = matcher.groupCount() > 1 && matcher.group(2) != null ? matcher.group(2) : "";

        return new ComponentValue(
                Double.parseDouble(numericPart),
                unit
        );
    }

    /**
     * Extract package size code
     */
    public String extractPackageSize(String mpn) {
        Matcher matcher = SIZE_CODE_PATTERN.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    /**
     * Extract tolerance code
     */
    public String extractToleranceCode(String mpn) {
        Matcher matcher = TOLERANCE_CODE.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    /**
     * Convert package size between imperial and metric
     */
    public String convertPackageSize(String sizeCode, boolean toMetric) {
        if (toMetric) {
            return PACKAGE_SIZE_MAPPINGS.getOrDefault(sizeCode, sizeCode);
        } else {
            for (Map.Entry<String, String> entry : PACKAGE_SIZE_MAPPINGS.entrySet()) {
                if (entry.getValue().equals(sizeCode)) {
                    return entry.getKey();
                }
            }
            return sizeCode;
        }
    }

    /**
     * Check if size codes are equivalent (considering imperial/metric conversion)
     */
    public boolean arePackageSizesEquivalent(String size1, String size2) {
        // Convert both to metric for comparison
        String metric1 = convertPackageSize(size1, true);
        String metric2 = convertPackageSize(size2, true);
        return metric1.equals(metric2);
    }

    /**
     * Get tolerance value from code
     */
    public double getToleranceValue(String toleranceCode) {
        return switch (toleranceCode) {
            case "F" -> 0.01;  // 1%
            case "G" -> 0.02;  // 2%
            case "J" -> 0.05;  // 5%
            case "K" -> 0.10;  // 10%
            case "M" -> 0.20;  // 20%
            default -> 0.0;
        };
    }

    /**
     * Inner class to represent component values with units
     */
    public static class ComponentValue {
        private final double value;
        private final String unit;

        public ComponentValue(double value, String unit) {
            this.value = value;
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public String getUnit() {
            return unit;
        }

        /**
         * Convert value to base unit
         */
        public double getBaseValue() {
            return switch (unit) {
                case "R" -> value;              // Ohms
                case "K" -> value * 1000;       // Kilo-ohms
                case "M" -> value * 1000000;    // Mega-ohms
                case "F" -> value;              // Farads
                case "U" -> value * 0.000001;   // Micro-farads
                case "N" -> value * 0.000000001; // Nano-farads
                case "P" -> value * 0.000000000001; // Pico-farads
                case "H" -> value;              // Henries
                default -> value;
            };
        }

        /**
         * Check if values are equivalent within tolerance
         */
        public boolean isEquivalentTo(ComponentValue other, double tolerance) {
            if (other == null) return false;

            double base1 = this.getBaseValue();
            double base2 = other.getBaseValue();

            double ratio = Math.min(base1, base2) / Math.max(base1, base2);
            return ratio >= (1 - tolerance);
        }

        @Override
        public String toString() {
            return value + unit;
        }
    }
}