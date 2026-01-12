package no.cantara.electronic.component.lib;

/**
 * Quality tier classification for electronic components based on data completeness.
 *
 * Tiers are assigned based on spec coverage, MPN validation, and datasheet availability.
 */
public enum QualityTier {

    /**
     * High quality: ≥80% spec coverage + datasheet + validated MPN
     */
    HIGH(0.8, "Complete specifications with validated data"),

    /**
     * Medium quality: 50-79% spec coverage OR missing datasheet
     */
    MEDIUM(0.5, "Partial specifications or missing documentation"),

    /**
     * Low quality: 10-49% spec coverage OR unvalidated MPN
     */
    LOW(0.1, "Minimal specifications or unvalidated part number"),

    /**
     * Incomplete: <10% spec coverage OR missing critical specs
     */
    INCOMPLETE(0.0, "Insufficient data for meaningful analysis");

    private final double minScore;
    private final String description;

    QualityTier(double minScore, String description) {
        this.minScore = minScore;
        this.description = description;
    }

    public double getMinScore() {
        return minScore;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Determines the quality tier based on score and additional factors.
     *
     * @param score The overall quality score (0.0 - 1.0)
     * @param hasDatasheet Whether a datasheet URL is available
     * @param validMpn Whether the MPN matches known manufacturer patterns
     * @param hasCriticalSpecs Whether critical specs for the component type are present
     * @return The appropriate quality tier
     */
    public static QualityTier fromScore(double score, boolean hasDatasheet,
                                        boolean validMpn, boolean hasCriticalSpecs) {
        if (!hasCriticalSpecs || score < 0.1) {
            return INCOMPLETE;
        }
        if (score >= 0.8 && hasDatasheet && validMpn) {
            return HIGH;
        }
        if (score >= 0.5 && (hasDatasheet || validMpn)) {
            return MEDIUM;
        }
        if (score >= 0.1) {
            return LOW;
        }
        return INCOMPLETE;
    }
}