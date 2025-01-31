package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultSimilarityCalculator implements SimilarityCalculator {
    private static final Pattern LETTER_PREFIX = Pattern.compile("^[A-Za-z]+");
    private static final Pattern NUMBER_SEQUENCE = Pattern.compile("\\d+");
    private static final Pattern LETTER_SUFFIX = Pattern.compile("[A-Za-z]+$");

    // Weights for different parts of the MPN
    private static final double PREFIX_WEIGHT = 0.3;
    private static final double NUMERIC_WEIGHT = 0.5;
    private static final double SUFFIX_WEIGHT = 0.2;

    @Override
    public double calculateSimilarity(String normalizedMpn1, String normalizedMpn2) {
        if (normalizedMpn1 == null || normalizedMpn2 == null) return 0.0;
        if (normalizedMpn1.equals(normalizedMpn2)) return 1.0;
        if (normalizedMpn1.isEmpty() || normalizedMpn2.isEmpty()) return 0.0;

        // Calculate similarities for different parts
        double prefixSimilarity = calculatePrefixSimilarity(normalizedMpn1, normalizedMpn2);
        double numericSimilarity = calculateNumericSimilarity(normalizedMpn1, normalizedMpn2);
        double suffixSimilarity = calculateSuffixSimilarity(normalizedMpn1, normalizedMpn2);

        // Calculate weighted average
        return (prefixSimilarity * PREFIX_WEIGHT) +
                (numericSimilarity * NUMERIC_WEIGHT) +
                (suffixSimilarity * SUFFIX_WEIGHT);
    }

    private double calculatePrefixSimilarity(String mpn1, String mpn2) {
        String prefix1 = extractPrefix(mpn1);
        String prefix2 = extractPrefix(mpn2);

        if (prefix1.isEmpty() && prefix2.isEmpty()) return 1.0;
        if (prefix1.isEmpty() || prefix2.isEmpty()) return 0.0;
        if (prefix1.equals(prefix2)) return 1.0;

        return calculateLevenshteinSimilarity(prefix1, prefix2);
    }

    private double calculateNumericSimilarity(String mpn1, String mpn2) {
        String num1 = extractNumericPart(mpn1);
        String num2 = extractNumericPart(mpn2);

        if (num1.isEmpty() && num2.isEmpty()) return 1.0;
        if (num1.isEmpty() || num2.isEmpty()) return 0.0;
        if (num1.equals(num2)) return 1.0;

        try {
            // Try numeric comparison if both are valid numbers
            long value1 = Long.parseLong(num1);
            long value2 = Long.parseLong(num2);

            // Calculate similarity based on numeric difference
            double diff = Math.abs(value1 - value2);
            double maxValue = Math.max(value1, value2);

            // Use logarithmic scale for large numbers
            if (maxValue > 1000) {
                return Math.max(0.0, 1.0 - (Math.log10(diff + 1) / Math.log10(maxValue + 1)));
            } else {
                return Math.max(0.0, 1.0 - (diff / maxValue));
            }
        } catch (NumberFormatException e) {
            // Fall back to string similarity if not valid numbers
            return calculateLevenshteinSimilarity(num1, num2);
        }
    }

    private double calculateSuffixSimilarity(String mpn1, String mpn2) {
        String suffix1 = extractSuffix(mpn1);
        String suffix2 = extractSuffix(mpn2);

        if (suffix1.isEmpty() && suffix2.isEmpty()) return 1.0;
        if (suffix1.isEmpty() || suffix2.isEmpty()) return 0.5; // Neutral score for missing suffix
        if (suffix1.equals(suffix2)) return 1.0;

        return calculateLevenshteinSimilarity(suffix1, suffix2);
    }

    private String extractPrefix(String mpn) {
        Matcher matcher = LETTER_PREFIX.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractNumericPart(String mpn) {
        Matcher matcher = NUMBER_SEQUENCE.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractSuffix(String mpn) {
        Matcher matcher = LETTER_SUFFIX.matcher(mpn);
        return matcher.find() ? matcher.group() : "";
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