package no.cantara.electronic.component.componentsimilaritycalculators;

public class LevenshteinCalculator {
    /**
     * Calculate similarity based on Levenshtein distance (0-1 scale)
     */
    public double calculateSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;

        int distance = calculateDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());
        return 1.0 - ((double) distance / maxLength);
    }

    /**
     * Calculate the Levenshtein distance between two strings
     */
    public int calculateDistance(String s1, String s2) {
        if (s1 == null || s2 == null) return -1;
        if (s1.equals(s2)) return 0;
        if (s1.isEmpty()) return s2.length();
        if (s2.isEmpty()) return s1.length();

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

    /**
     * Calculate weighted similarity for different length strings
     */
    public double calculateWeightedSimilarity(String s1, String s2, double lengthWeight) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;

        double basicSimilarity = calculateSimilarity(s1, s2);

        // Apply length penalty if strings have very different lengths
        int lengthDiff = Math.abs(s1.length() - s2.length());
        int maxLength = Math.max(s1.length(), s2.length());
        double lengthPenalty = 1.0 - (lengthDiff / (double) maxLength) * lengthWeight;

        return basicSimilarity * lengthPenalty;
    }

    /**
     * Calculate similarity ignoring case
     */
    public double calculateCaseInsensitiveSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        return calculateSimilarity(s1.toLowerCase(), s2.toLowerCase());
    }

    /**
     * Calculate similarity for numeric strings with value consideration
     */
    public double calculateNumericSimilarity(String num1, String num2) {
        try {
            double val1 = Double.parseDouble(num1);
            double val2 = Double.parseDouble(num2);

            // Handle exact match
            if (val1 == val2) return 1.0;

            // Calculate relative difference
            double maxVal = Math.max(Math.abs(val1), Math.abs(val2));
            double diff = Math.abs(val1 - val2);

            // Use logarithmic scale for large numbers
            if (maxVal > 1000) {
                return Math.max(0.0, 1.0 - (Math.log10(diff + 1) / Math.log10(maxVal + 1)));
            } else {
                return Math.max(0.0, 1.0 - (diff / maxVal));
            }
        } catch (NumberFormatException e) {
            // Fall back to string similarity if not valid numbers
            return calculateSimilarity(num1, num2);
        }
    }

    /**
     * Calculate similarity for prefix matching with bonus for exact prefix match
     */
    public double calculatePrefixSimilarity(String s1, String s2, int prefixLength) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;

        // Get prefixes
        String prefix1 = s1.length() >= prefixLength ? s1.substring(0, prefixLength) : s1;
        String prefix2 = s2.length() >= prefixLength ? s2.substring(0, prefixLength) : s2;

        // Calculate prefix similarity
        double prefixSimilarity = calculateSimilarity(prefix1, prefix2);

        // Add bonus for exact prefix match
        if (prefix1.equals(prefix2)) {
            prefixSimilarity = (prefixSimilarity + 1.0) / 2.0;
        }

        // Calculate remaining string similarity
        String remainder1 = s1.length() > prefixLength ? s1.substring(prefixLength) : "";
        String remainder2 = s2.length() > prefixLength ? s2.substring(prefixLength) : "";
        double remainderSimilarity = calculateSimilarity(remainder1, remainder2);

        // Weight prefix more heavily
        return (prefixSimilarity * 0.7) + (remainderSimilarity * 0.3);
    }

    /**
     * Calculate similarity with support for common substitutions
     */
    public double calculateSimilarityWithSubstitutions(String s1, String s2,
                                                       java.util.Map<String, String> substitutions) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;

        double basicSimilarity = calculateSimilarity(s1, s2);

        // Check for common substitutions
        for (java.util.Map.Entry<String, String> entry : substitutions.entrySet()) {
            String normalized1 = s1.replace(entry.getKey(), entry.getValue());
            String normalized2 = s2.replace(entry.getKey(), entry.getValue());

            if (normalized1.equals(normalized2)) {
                return 0.9; // High similarity for matching substitutions
            }

            double substitutionSimilarity = calculateSimilarity(normalized1, normalized2);
            basicSimilarity = Math.max(basicSimilarity, substitutionSimilarity);
        }

        return basicSimilarity;
    }
}