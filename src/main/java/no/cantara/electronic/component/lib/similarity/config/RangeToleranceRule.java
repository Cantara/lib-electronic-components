package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Requires candidate value to be within [original * minMultiplier, original * maxMultiplier].
 * Used for general range-based matching.
 *
 * Scoring:
 * - candidate == original: 1.0 (perfect match)
 * - Within range: 0.9 (acceptable)
 * - Outside range: 0.0 (unacceptable)
 */
class RangeToleranceRule implements ToleranceRule {

    private final double minMultiplier;
    private final double maxMultiplier;

    public RangeToleranceRule(double minMultiplier, double maxMultiplier) {
        if (minMultiplier <= 0 || maxMultiplier <= 0) {
            throw new IllegalArgumentException("Multipliers must be > 0");
        }
        if (minMultiplier > maxMultiplier) {
            throw new IllegalArgumentException("Min multiplier must be <= max multiplier");
        }
        this.minMultiplier = minMultiplier;
        this.maxMultiplier = maxMultiplier;
    }

    @Override
    public double compare(SpecValue<?> original, SpecValue<?> candidate) {
        if (original == null || candidate == null) return 0.0;

        Object origValue = original.getValue();
        Object candValue = candidate.getValue();

        if (!(origValue instanceof Number) || !(candValue instanceof Number)) {
            // Fall back to exact match for non-numeric values
            return origValue.equals(candValue) ? 1.0 : 0.0;
        }

        double orig = ((Number) origValue).doubleValue();
        double cand = ((Number) candValue).doubleValue();

        if (orig == 0.0) {
            // Avoid division by zero
            return cand == 0.0 ? 1.0 : 0.0;
        }

        if (cand == orig) {
            // Perfect match
            return 1.0;
        }

        double minAllowed = orig * minMultiplier;
        double maxAllowed = orig * maxMultiplier;

        if (cand >= minAllowed && cand <= maxAllowed) {
            // Within acceptable range
            return 0.9;
        } else {
            // Outside range - unacceptable
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("RangeToleranceRule{original × %.2f to %.2f}", minMultiplier, maxMultiplier);
    }
}
