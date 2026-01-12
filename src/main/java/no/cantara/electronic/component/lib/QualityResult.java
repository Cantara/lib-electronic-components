package no.cantara.electronic.component.lib;

import java.util.List;

/**
 * Result of a component quality analysis.
 *
 * Contains the overall quality score, tier assignment, and detailed breakdown
 * of spec completeness and missing specifications.
 *
 * @param overallScore Quality score from 0.0 (worst) to 1.0 (best)
 * @param tier The assigned quality tier (HIGH, MEDIUM, LOW, INCOMPLETE)
 * @param specCompleteness Percentage of expected specs that are present (0.0 - 1.0)
 * @param presentSpecs Number of expected specs that have values
 * @param expectedSpecs Total number of expected specs for this component type
 * @param missingSpecs List of spec names that are expected but missing
 * @param missingCriticalSpecs List of critical spec names that are missing
 * @param validMpn Whether the MPN matches known manufacturer patterns
 * @param componentType The detected component type name
 */
public record QualityResult(
        double overallScore,
        QualityTier tier,
        double specCompleteness,
        int presentSpecs,
        int expectedSpecs,
        List<String> missingSpecs,
        List<String> missingCriticalSpecs,
        boolean validMpn,
        String componentType
) {
    /**
     * Creates a result for an unknown or unsupported component type.
     */
    public static QualityResult unknown(String mpn) {
        return new QualityResult(
                0.0,
                QualityTier.INCOMPLETE,
                0.0,
                0,
                0,
                List.of(),
                List.of(),
                false,
                "UNKNOWN"
        );
    }

    /**
     * Checks if this component has high quality data.
     */
    public boolean isHighQuality() {
        return tier == QualityTier.HIGH;
    }

    /**
     * Checks if this component needs enrichment (not high quality).
     */
    public boolean needsEnrichment() {
        return tier != QualityTier.HIGH;
    }

    /**
     * Checks if critical specs are missing.
     */
    public boolean hasMissingCriticalSpecs() {
        return !missingCriticalSpecs.isEmpty();
    }
}
