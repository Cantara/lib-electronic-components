package no.cantara.electronic.component.componentsimilaritycalculators;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.PatternRegistry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapacitorSimilarityCalculator implements ComponentSimilarityCalculator {
    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) {
            return false;
        }
        return type == ComponentType.CAPACITOR ||
                type == ComponentType.CAPACITOR_CERAMIC_MURATA ||
                type == ComponentType.CAPACITOR_CERAMIC_KEMET ||
                type.name().startsWith("CAPACITOR_");
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null || patternRegistry == null) {
            return 0.0;
        }

        System.out.println("Comparing capacitors: " + mpn1 + " and " + mpn2);

        // Extract and compare key characteristics
        String size1 = extractPackageSize(mpn1);
        String size2 = extractPackageSize(mpn2);
        String value1 = extractValue(mpn1);
        String value2 = extractValue(mpn2);
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);

        System.out.println("Size1: " + size1 + ", Size2: " + size2);
        System.out.println("Value1: " + value1 + ", Value2: " + value2);
        System.out.println("Voltage1: " + voltage1 + ", Voltage2: " + voltage2);

        double similarity = 0.0;

        // Same package size
        if (size1 != null && size2 != null && size1.equals(size2)) {
            similarity += 0.3;
            System.out.println("Package size match (+0.3)");
        }

        // Same capacitance value
        if (value1 != null && value2 != null) {
            String norm1 = normalizeValue(value1);
            String norm2 = normalizeValue(value2);
            System.out.println("Normalized values: " + norm1 + " and " + norm2);
            if (norm1.equals(norm2)) {
                similarity += 0.4;
                System.out.println("Value match (+0.4)");
            }
        }

        // Compatible voltage rating
        if (voltage1 != null && voltage2 != null) {
            double v1 = parseVoltage(voltage1);
            double v2 = parseVoltage(voltage2);
            if (v1 >= v2) {  // Higher voltage rating is acceptable
                similarity += 0.2;
                System.out.println("Voltage compatible (+0.2)");
            }
        }

        System.out.println("Final similarity: " + similarity);
        return similarity;
    }

    private String extractPackageSize(String mpn) {
        // Murata GRM format (e.g., GRM188)
        if (mpn.startsWith("GRM")) {
            // 188 -> 0603
            if (mpn.contains("188")) return "0603";
            if (mpn.contains("216")) return "0805";
            if (mpn.contains("316")) return "1206";
        }

        // KEMET format (e.g., C0603)
        if (mpn.contains("0603")) return "0603";
        if (mpn.contains("0805")) return "0805";
        if (mpn.contains("1206")) return "1206";

        return null;
    }

    private String extractValue(String mpn) {
        // Murata format (e.g., 104 = 100nF = 0.1µF)
        Matcher murataMatch = Pattern.compile("[0-9]{3}[A-Z]").matcher(mpn);
        if (murataMatch.find()) {
            return convertMurataCode(murataMatch.group());
        }

        // KEMET format (e.g., 104K = 100nF = 0.1µF)
        Matcher kemetMatch = Pattern.compile("[0-9]{3}[A-Z]").matcher(mpn);
        if (kemetMatch.find()) {
            return convertKemetCode(kemetMatch.group());
        }

        return null;
    }

    private String extractVoltage(String mpn) {
        // Murata format (e.g., 1H = 50V)
        if (mpn.startsWith("GRM")) {
            Pattern voltPattern = Pattern.compile("[1-3][A-Z]");
            Matcher m = voltPattern.matcher(mpn);
            if (m.find()) {
                return convertMurataVoltage(m.group());
            }
        }

        // KEMET format (e.g., 5R = 50V)
        Pattern voltPattern = Pattern.compile("[0-9]+[R]");
        Matcher m = voltPattern.matcher(mpn);
        if (m.find()) {
            return m.group().replace("R", "");
        }

        return null;
    }

    private String convertMurataCode(String code) {
        // First two digits are significant digits
        // Third digit is multiplier
        // e.g., 104 = 10 * 10^4 pF = 100nF = 0.1µF
        try {
            int sig = Integer.parseInt(code.substring(0, 2));
            int mult = Integer.parseInt(code.substring(2, 3));
            double value = sig * Math.pow(10, mult - 12);  // Convert to µF
            return String.format("%.1f", value) + "µF";
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String convertKemetCode(String code) {
        // Similar to Murata code
        return convertMurataCode(code);
    }

    private String normalizeValue(String value) {
        if (value == null) return null;
        // Convert all to µF
        return value.replaceAll("\\s+", "").toUpperCase();
    }

    private double parseVoltage(String voltage) {
        try {
            return Double.parseDouble(voltage);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String convertMurataVoltage(String code) {
        return switch (code) {
            case "1H" -> "50";
            case "1E" -> "25";
            case "1C" -> "16";
            case "1A" -> "10";
            default -> "0";
        };
    }
}