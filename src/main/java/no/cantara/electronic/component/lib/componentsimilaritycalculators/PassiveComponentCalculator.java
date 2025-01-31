package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PassiveComponentCalculator implements SimilarityCalculator {
    private static final Pattern VALUE_PATTERN = Pattern.compile("(\\d+)(R|K|M|F|H|U|N|P)?");
    private static final Pattern SIZE_CODE_PATTERN = Pattern.compile("\\d{4}");

    @Override
    public double calculateSimilarity(String normalizedMpn1, String normalizedMpn2) {
        double valueSimilarity = compareValues(normalizedMpn1, normalizedMpn2);
        double sizeSimilarity = compareSizeCodes(normalizedMpn1, normalizedMpn2);
        double toleranceSimilarity = compareTolerances(normalizedMpn1, normalizedMpn2);

        // Weight the similarities
        return (valueSimilarity * 0.5) + (sizeSimilarity * 0.3) + (toleranceSimilarity * 0.2);
    }

    private double compareValues(String mpn1, String mpn2) {
        String value1 = extractValue(mpn1);
        String value2 = extractValue(mpn2);

        if (value1.isEmpty() || value2.isEmpty()) return 0.0;
        if (value1.equals(value2)) return 1.0;

        // Convert values to comparable format (e.g., ohms, farads, henries)
        double numericValue1 = convertToBaseUnit(value1);
        double numericValue2 = convertToBaseUnit(value2);

        // Compare numeric values with tolerance for small differences
        double ratio = Math.min(numericValue1, numericValue2) / Math.max(numericValue1, numericValue2);
        return ratio > 0.9 ? ratio : 0.0;
    }

    private String extractValue(String mpn) {
        Matcher matcher = VALUE_PATTERN.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    private double convertToBaseUnit(String value) {
        // Convert value with suffix (R, K, M, F, H, etc.) to base unit
        Matcher matcher = VALUE_PATTERN.matcher(value);
        if (!matcher.find()) return 0.0;

        double numericValue = Double.parseDouble(matcher.group(1));
        String suffix = matcher.groupCount() > 1 && matcher.group(2) != null ? matcher.group(2) : "";

        return switch (suffix) {
            case "R" -> numericValue;
            case "K" -> numericValue * 1000;
            case "M" -> numericValue * 1000000;
            case "U" -> numericValue * 0.000001;
            case "N" -> numericValue * 0.000000001;
            case "P" -> numericValue * 0.000000000001;
            default -> numericValue;
        };
    }

    private double compareSizeCodes(String mpn1, String mpn2) {
        String size1 = extractSizeCode(mpn1);
        String size2 = extractSizeCode(mpn2);

        if (size1.isEmpty() || size2.isEmpty()) return 0.0;
        if (size1.equals(size2)) return 1.0;

        // Compare imperial vs metric size codes
        return convertAndCompareSizeCodes(size1, size2);
    }

    private String extractSizeCode(String mpn) {
        Matcher matcher = SIZE_CODE_PATTERN.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    private double convertAndCompareSizeCodes(String code1, String code2) {
        // Add size code conversion logic (e.g., 0603 vs 1608)
        // Return similarity based on physical size comparison
        Map<String, String> imperialToMetric = new HashMap<>();
        imperialToMetric.put("0201", "0603");
        imperialToMetric.put("0402", "1005");
        imperialToMetric.put("0603", "1608");
        imperialToMetric.put("0805", "2012");
        imperialToMetric.put("1206", "3216");

        String metric1 = imperialToMetric.getOrDefault(code1, code1);
        String metric2 = imperialToMetric.getOrDefault(code2, code2);

        return metric1.equals(metric2) ? 1.0 : 0.0;
    }

    private double compareTolerances(String mpn1, String mpn2) {
        // Extract and compare tolerance codes
        // Common tolerance codes: F(1%), G(2%), J(5%), K(10%), M(20%)
        Pattern tolerancePattern = Pattern.compile("[FGJKM]");

        Matcher matcher1 = tolerancePattern.matcher(mpn1);
        Matcher matcher2 = tolerancePattern.matcher(mpn2);

        if (!matcher1.find() || !matcher2.find()) return 0.5; // Neutral if no tolerance found

        return matcher1.group().equals(matcher2.group()) ? 1.0 : 0.0;
    }
}