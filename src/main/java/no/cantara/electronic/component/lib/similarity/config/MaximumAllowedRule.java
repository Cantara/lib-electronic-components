package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Requires candidate value to be <= original * maxMultiplier.
 * Used for specs where lower is better (Rds(on), ESR, leakage current).
 *
 * Scoring:
 * - candidate == original: 1.0 (perfect match)
 * - candidate < original: 0.98 (better spec, slight penalty for over-spec)
 * - candidate <= original * maxMultiplier: linear decay
 * - candidate > original * maxMultiplier: 0.0 (unacceptable)
 */
class MaximumAllowedRule implements ToleranceRule {

    private final double maxMultiplier;

    public MaximumAllowedRule(double maxMultiplier) {
        if (maxMultiplier < 1.0) {
            throw new IllegalArgumentException("Max multiplier must be >= 1.0");
        }
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
        } else if (cand < orig) {
            // Better spec (lower is better) - slight penalty for over-spec
            return 0.98;
        } else if (cand <= orig * maxMultiplier) {
            // Within acceptable range - linear decay from 1.0 to 0.7
            double ratio = cand / orig;
            double allowedRange = maxMultiplier - 1.0; // e.g., 1.2 - 1.0 = 0.2
            double actualExcess = ratio - 1.0;

            // Score decreases linearly: 1.0 at orig, 0.7 at orig * maxMultiplier
            return 1.0 - (actualExcess / allowedRange) * 0.3;
        } else {
            // Beyond maximum allowed - unacceptable
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("MaximumAllowedRule{candidate <= original × %.2f}", maxMultiplier);
    }
}
