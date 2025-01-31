package no.cantara.electronic.component.componentsimilaritycalculators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCUSimilarityCalculator implements SimilarityCalculator {
    private static final Pattern FAMILY_PATTERN = Pattern.compile("^([A-Z]+)\\d+");
    private static final Pattern SERIES_PATTERN = Pattern.compile("\\d+");
    private static final Pattern FEATURE_PATTERN = Pattern.compile("[A-Z]+$");

    // MCU family relationships (e.g., similar architectures or capabilities)
    private static final Map<String, String[]> FAMILY_GROUPS = new HashMap<String, String[]>() {{
        put("PIC", new String[]{"PIC16", "PIC18", "PIC24", "PIC32"});
        put("STM", new String[]{"STM8", "STM32"});
        put("ATM", new String[]{"ATMEGA", "ATTINY", "ATXMEGA"});
        put("MSP", new String[]{"MSP430", "MSP432"});
    }};

    // Feature code meanings across manufacturers
    private static final Map<String, String[]> FEATURE_GROUPS = new HashMap<String, String[]>() {{
        put("F", new String[]{"Flash"});           // Flash memory
        put("L", new String[]{"Low Power"});       // Low power variant
        put("U", new String[]{"USB"});             // USB capable
        put("W", new String[]{"Wireless"});        // Wireless capabilities
        put("R", new String[]{"ROM"});             // ROM version
        put("T", new String[]{"Temperature"});     // Extended temperature
    }};

    @Override
    public double calculateSimilarity(String normalizedMpn1, String normalizedMpn2) {
        // Extract components
        String family1 = extractFamily(normalizedMpn1);
        String family2 = extractFamily(normalizedMpn2);
        String series1 = extractSeries(normalizedMpn1);
        String series2 = extractSeries(normalizedMpn2);
        String features1 = extractFeatures(normalizedMpn1);
        String features2 = extractFeatures(normalizedMpn2);

        // Calculate component similarities
        double familySimilarity = calculateFamilySimilarity(family1, family2);
        double seriesSimilarity = calculateSeriesSimilarity(series1, series2);
        double featureSimilarity = calculateFeatureSimilarity(features1, features2);

        // Weight the components
        // Family similarity is most important, followed by series, then features
        return (familySimilarity * 0.5) + (seriesSimilarity * 0.3) + (featureSimilarity * 0.2);
    }

    private String extractFamily(String mpn) {
        Matcher matcher = FAMILY_PATTERN.matcher(mpn);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extractSeries(String mpn) {
        Matcher matcher = SERIES_PATTERN.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractFeatures(String mpn) {
        Matcher matcher = FEATURE_PATTERN.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    private double calculateFamilySimilarity(String family1, String family2) {
        if (family1.isEmpty() || family2.isEmpty()) return 0.0;
        if (family1.equals(family2)) return 1.0;

        // Check if families are in the same group
        for (Map.Entry<String, String[]> entry : FAMILY_GROUPS.entrySet()) {
            boolean family1InGroup = Arrays.asList(entry.getValue()).contains(family1);
            boolean family2InGroup = Arrays.asList(entry.getValue()).contains(family2);
            if (family1InGroup && family2InGroup) {
                return 0.8;  // Families are related
            }
        }

        // Calculate basic string similarity for unknown relationships
        return calculateLevenshteinSimilarity(family1, family2);
    }

    private double calculateSeriesSimilarity(String series1, String series2) {
        if (series1.isEmpty() || series2.isEmpty()) return 0.0;
        if (series1.equals(series2)) return 1.0;

        // Convert to numbers for numeric comparison
        try {
            int num1 = Integer.parseInt(series1);
            int num2 = Integer.parseInt(series2);

            // Calculate similarity based on numeric difference
            int diff = Math.abs(num1 - num2);
            int maxDiff = 100; // Maximum difference to consider for similarity

            return Math.max(0.0, 1.0 - (diff / (double)maxDiff));
        } catch (NumberFormatException e) {
            // Fall back to string similarity if not numeric
            return calculateLevenshteinSimilarity(series1, series2);
        }
    }

    private double calculateFeatureSimilarity(String features1, String features2) {
        if (features1.isEmpty() && features2.isEmpty()) return 1.0;
        if (features1.isEmpty() || features2.isEmpty()) return 0.0;

        // Count matching feature codes
        int matchingFeatures = 0;
        int totalFeatures = Math.max(features1.length(), features2.length());

        for (char feature : features1.toCharArray()) {
            if (features2.indexOf(feature) != -1) {
                matchingFeatures++;
            }
        }

        return (double) matchingFeatures / totalFeatures;
    }

    private double calculateLevenshteinSimilarity(String s1, String s2) {
        int distance = calculateLevenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());
        return 1.0 - ((double) distance / maxLength);
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }
}