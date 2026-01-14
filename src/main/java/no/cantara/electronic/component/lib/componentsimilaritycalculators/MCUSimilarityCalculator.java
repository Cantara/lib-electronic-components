package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import no.cantara.electronic.component.lib.similarity.config.SimilarityProfile;
import no.cantara.electronic.component.lib.similarity.config.ToleranceRule;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Similarity calculator for MCU (microcontroller) components.
 *
 * Uses metadata-driven comparison based on extracted specs (family, series, features).
 * MCUs are compared using family relationships (50%), series numbers (30%), and
 * feature codes (20%) with configurable weights.
 *
 * Fallback to legacy weighted approach for unknown MCU types.
 */
public class MCUSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MCUSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

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

    public MCUSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) return false;
        return type == ComponentType.MICROCONTROLLER ||
                type == ComponentType.MCU_ATMEL ||
                type.name().startsWith("MICROCONTROLLER_") ||
                type.getBaseType() == ComponentType.MICROCONTROLLER;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        logger.debug("Comparing MCUs: {} vs {}", mpn1, mpn2);

        // Normalize MPNs (uppercase, trim)
        String normalizedMpn1 = mpn1.toUpperCase().trim();
        String normalizedMpn2 = mpn2.toUpperCase().trim();

        // Get metadata for spec-based comparison
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.MICROCONTROLLER);
        if (metadataOpt.isPresent()) {
            logger.trace("Using metadata-driven comparison for MCUs");
            return calculateMetadataDrivenSimilarity(normalizedMpn1, normalizedMpn2, metadataOpt.get());
        }

        // Fallback to legacy weighted approach if metadata not available
        logger.trace("No metadata found for MICROCONTROLLER, using legacy approach");
        return calculateLegacySimilarity(normalizedMpn1, normalizedMpn2);
    }

    /**
     * Calculate similarity using metadata-driven weighted comparison.
     * Extracts specs from MPNs and uses tolerance rules for comparison.
     */
    private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
        SimilarityProfile profile = metadata.getDefaultProfile();

        logger.trace("Using profile: {}", profile);

        // Extract specs from both MPNs
        String family1 = extractFamily(mpn1);
        String family2 = extractFamily(mpn2);
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        String features1 = extractFeatures(mpn1);
        String features2 = extractFeatures(mpn2);

        logger.trace("MPN1 specs: family={}, series={}, features={}", family1, series1, features1);
        logger.trace("MPN2 specs: family={}, series={}, features={}", family2, series2, features2);

        // Short-circuit: Different families (not in same group) = incompatible
        if (!family1.isEmpty() && !family2.isEmpty() &&
            !family1.equals(family2) && !areRelatedFamilies(family1, family2)) {
            logger.debug("Unrelated families: {} vs {} -> LOW_SIMILARITY", family1, family2);
            return LOW_SIMILARITY;
        }

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // Compare family (CRITICAL)
        ComponentTypeMetadata.SpecConfig familyConfig = metadata.getSpecConfig("family");
        if (familyConfig != null && !family1.isEmpty() && !family2.isEmpty()) {
            ToleranceRule rule = familyConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(family1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(family2, SpecUnit.NONE);

            // Use custom family similarity logic
            double familySim = calculateFamilySimilarity(family1, family2);
            double specWeight = profile.getEffectiveWeight(familyConfig.getImportance());

            totalScore += familySim * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Family comparison: score={}, weight={}, contribution={}", familySim, specWeight, familySim * specWeight);
        }

        // Compare series (HIGH)
        ComponentTypeMetadata.SpecConfig seriesConfig = metadata.getSpecConfig("series");
        if (seriesConfig != null && !series1.isEmpty() && !series2.isEmpty()) {
            // Use custom series similarity logic
            double seriesSim = calculateSeriesSimilarity(series1, series2);
            double specWeight = profile.getEffectiveWeight(seriesConfig.getImportance());

            totalScore += seriesSim * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Series comparison: score={}, weight={}, contribution={}", seriesSim, specWeight, seriesSim * specWeight);
        }

        // Compare features (MEDIUM)
        ComponentTypeMetadata.SpecConfig featuresConfig = metadata.getSpecConfig("features");
        if (featuresConfig != null && (!features1.isEmpty() || !features2.isEmpty())) {
            // Use custom feature similarity logic
            double featureSim = calculateFeatureSimilarity(features1, features2);
            double specWeight = profile.getEffectiveWeight(featuresConfig.getImportance());

            totalScore += featureSim * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Features comparison: score={}, weight={}, contribution={}", featureSim, specWeight, featureSim * specWeight);
        }

        // Normalize to [0.0, 1.0]
        double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

        logger.debug("Final similarity: {}", similarity);
        return similarity;
    }

    /**
     * Check if two families are related (in same family group).
     */
    private boolean areRelatedFamilies(String family1, String family2) {
        for (Map.Entry<String, String[]> entry : FAMILY_GROUPS.entrySet()) {
            boolean family1InGroup = Arrays.asList(entry.getValue()).contains(family1);
            boolean family2InGroup = Arrays.asList(entry.getValue()).contains(family2);
            if (family1InGroup && family2InGroup) {
                return true;
            }
        }
        return false;
    }

    /**
     * Legacy similarity calculation for backward compatibility if metadata unavailable.
     */
    private double calculateLegacySimilarity(String normalizedMpn1, String normalizedMpn2) {
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
