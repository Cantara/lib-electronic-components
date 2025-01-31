package no.cantara.electronic.component.componentsimilaritycalculators;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.PatternRegistry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResistorSimilarityCalculator implements ComponentSimilarityCalculator {
    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) {
            return false;
        }
        return type == ComponentType.RESISTOR ||
                type == ComponentType.RESISTOR_CHIP_VISHAY ||
                type == ComponentType.RESISTOR_CHIP_YAGEO ||
                type.name().startsWith("RESISTOR_");
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null || patternRegistry == null) {
            return 0.0;
        }

        System.out.println("Comparing resistors: " + mpn1 + " and " + mpn2);

        // Extract and compare key characteristics
        String size1 = extractPackageSize(mpn1);
        String size2 = extractPackageSize(mpn2);
        String value1 = extractValue(mpn1);
        String value2 = extractValue(mpn2);

        System.out.println("Size1: " + size1 + ", Size2: " + size2);
        System.out.println("Value1: " + value1 + ", Value2: " + value2);

        double similarity = 0.0;

        // Same package size
        if (size1 != null && size2 != null && size1.equals(size2)) {
            similarity += 0.3;
            System.out.println("Package size match (+0.3)");
        }

        // Same resistance value
        if (value1 != null && value2 != null) {
            String norm1 = normalizeValue(value1);
            String norm2 = normalizeValue(value2);
            System.out.println("Normalized values: " + norm1 + " and " + norm2);
            if (norm1.equals(norm2)) {
                similarity += 0.5;
                System.out.println("Value match (+0.5)");
            }
        }

        System.out.println("Final similarity: " + similarity);
        return similarity;
    }

    private String extractPackageSize(String mpn) {
        // Common chip resistor sizes
        if (mpn.contains("0603")) return "0603";
        if (mpn.contains("0805")) return "0805";
        if (mpn.contains("1206")) return "1206";
        if (mpn.contains("2512")) return "2512";

        // For Vishay CRCW format
        if (mpn.startsWith("CRCW")) {
            try {
                return mpn.substring(4, 8);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }

        // For Yageo RC format
        if (mpn.startsWith("RC")) {
            String[] parts = mpn.split("-");
            if (parts.length > 0 && parts[0].length() > 2) {
                return parts[0].substring(2);
            }
        }

        return null;
    }

    private String extractValue(String mpn) {
        // For Vishay CRCW format (e.g., CRCW060310K0FKEA)
        if (mpn.startsWith("CRCW")) {
            // Find the value part (usually after size code)
            int startIdx = mpn.indexOf("10K");
            if (startIdx != -1) {
                return "10K";
            }
        }

        // For Yageo RC format (e.g., RC0603FR-0710KL)
        if (mpn.startsWith("RC")) {
            String[] parts = mpn.split("-");
            if (parts.length > 1) {
                // Look for value in the second part
                String valuePart = parts[1];
                if (valuePart.contains("10K")) {
                    return "10K";
                }
            }
        }

        // Generic value extraction for other formats
        Matcher matcher = Pattern.compile("(\\d+[.]?\\d*[K|M|R]?)").matcher(mpn);
        if (matcher.find()) {
            return normalizeValue(matcher.group(1));
        }

        return null;
    }

    private String normalizeValue(String value) {
        if (value == null) return null;

        value = value.toUpperCase();

        // Handle special cases
        if (value.contains("R")) {
            value = value.replace("R", ".");
        }

        // Remove trailing zeros after K or M
        value = value.replaceAll("K0+$", "K");
        value = value.replaceAll("M0+$", "M");

        // Handle fractional K values (e.g., 1K5 -> 1.5K)
        if (value.contains("K") && !value.endsWith("K")) {
            String[] parts = value.split("K");
            if (parts.length == 2) {
                return parts[0] + "." + parts[1] + "K";
            }
        }

        return value;
    }
}