package no.cantara.electronic.component.lib.similarity.config;

/**
 * Defines the importance level of a specification in similarity calculations.
 *
 * Importance levels determine:
 * 1. Base weight in similarity score calculation
 * 2. Impact on overall match quality
 * 3. Whether missing specs cause rejection
 *
 * Higher importance specs have more impact on the final similarity score.
 */
public enum SpecImportance {

    /**
     * CRITICAL specs must match very closely (within tight tolerance).
     * Missing critical specs result in zero similarity.
     * Examples: resistance value, voltage rating, polarity, dielectric type
     */
    CRITICAL(1.0, true),

    /**
     * HIGH importance specs should match well but some deviation acceptable.
     * Missing high-importance specs significantly reduces similarity.
     * Examples: package size, tolerance band, temperature coefficient
     */
    HIGH(0.7, false),

    /**
     * MEDIUM importance specs are moderately important.
     * Missing medium-importance specs reduces similarity moderately.
     * Examples: power rating (when over-specced), manufacturer series
     */
    MEDIUM(0.4, false),

    /**
     * LOW importance specs are nice to have but not critical.
     * Missing low-importance specs has minimal impact.
     * Examples: tape/reel indicator, lead-free status
     */
    LOW(0.2, false),

    /**
     * OPTIONAL specs are informational only, not used in similarity calculation.
     * Examples: datasheet URL, part lifecycle status
     */
    OPTIONAL(0.0, false);

    private final double baseWeight;
    private final boolean mandatory;

    SpecImportance(double baseWeight, boolean mandatory) {
        this.baseWeight = baseWeight;
        this.mandatory = mandatory;
    }

    /**
     * Get the base weight for this importance level.
     * This weight is multiplied by the profile multiplier to get the effective weight.
     *
     * @return Base weight from 0.0 (optional) to 1.0 (critical)
     */
    public double getBaseWeight() {
        return baseWeight;
    }

    /**
     * Check if this spec is mandatory (missing spec causes zero similarity).
     *
     * @return true if mandatory (only CRITICAL specs are mandatory)
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Get the importance level for a given base weight.
     * Useful for parsing configuration files.
     *
     * @param weight Base weight value
     * @return Closest matching importance level
     */
    public static SpecImportance fromWeight(double weight) {
        if (weight >= 0.85) return CRITICAL;
        if (weight >= 0.55) return HIGH;
        if (weight >= 0.30) return MEDIUM;
        if (weight >= 0.10) return LOW;
        return OPTIONAL;
    }
}
