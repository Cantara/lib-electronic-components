package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Defines how to compare two specification values with configurable tolerance.
 *
 * Tolerance rules determine whether two spec values are "similar enough" based on:
 * - Percentage tolerance (e.g., resistance within ±5%)
 * - Minimum required (e.g., voltage rating must be >= original)
 * - Maximum allowed (e.g., Rds(on) must be <= 120% of original)
 * - Exact match (e.g., dielectric type must match exactly)
 *
 * Returns a similarity score from 0.0 (completely different) to 1.0 (perfect match).
 */
@FunctionalInterface
public interface ToleranceRule {

    /**
     * Compare two specification values and return a similarity score.
     *
     * @param original The original/reference specification value
     * @param candidate The candidate/comparison specification value
     * @return Similarity score: 1.0 = perfect match, 0.0 = no match, values in between indicate partial match
     */
    double compare(SpecValue<?> original, SpecValue<?> candidate);

    /**
     * Check if the candidate value is acceptable given the tolerance rule.
     * Default implementation considers scores >= 0.7 as acceptable.
     */
    default boolean isAcceptable(SpecValue<?> original, SpecValue<?> candidate) {
        return compare(original, candidate) >= 0.7;
    }

    /**
     * Exact match required - values must be identical.
     */
    static ToleranceRule exactMatch() {
        return new ExactMatchRule();
    }

    /**
     * Percentage-based tolerance - candidate must be within ±X% of original.
     *
     * @param percentTolerance Allowed percentage deviation (e.g., 5.0 for ±5%)
     */
    static ToleranceRule percentageTolerance(double percentTolerance) {
        return new PercentageToleranceRule(percentTolerance);
    }

    /**
     * Minimum required - candidate must be >= original (for ratings like voltage, current).
     * Accepts higher values as better alternatives.
     */
    static ToleranceRule minimumRequired() {
        return new MinimumRequiredRule();
    }

    /**
     * Maximum allowed - candidate must be <= original * maxMultiplier.
     * Used for specs where lower is better (e.g., Rds(on), ESR).
     *
     * @param maxMultiplier Maximum allowed multiplier (e.g., 1.2 for 120% of original)
     */
    static ToleranceRule maximumAllowed(double maxMultiplier) {
        return new MaximumAllowedRule(maxMultiplier);
    }

    /**
     * Range-based tolerance - candidate must be within min/max bounds.
     *
     * @param minMultiplier Minimum allowed as fraction of original (e.g., 0.8 for 80%)
     * @param maxMultiplier Maximum allowed as fraction of original (e.g., 1.2 for 120%)
     */
    static ToleranceRule rangeTolerance(double minMultiplier, double maxMultiplier) {
        return new RangeToleranceRule(minMultiplier, maxMultiplier);
    }
}
