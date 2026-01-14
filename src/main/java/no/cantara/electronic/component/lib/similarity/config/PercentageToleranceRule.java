package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Allows percentage-based deviation from original value.
 * Used for specs like resistance, capacitance where ±X% tolerance is acceptable.
 *
 * Scoring:
 * - Within tolerance: 1.0
 * - Outside tolerance: score decreases linearly with distance
 * - Beyond 2x tolerance: 0.0
 */
class PercentageToleranceRule implements ToleranceRule {

    private final double percentTolerance;

    public PercentageToleranceRule(double percentTolerance) {
        if (percentTolerance < 0) {
            throw new IllegalArgumentException("Percent tolerance must be >= 0");
        }
        this.percentTolerance = percentTolerance;
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
            // Avoid division by zero - require exact match
            return cand == 0.0 ? 1.0 : 0.0;
        }

        // Calculate percentage deviation
        double deviation = Math.abs((cand - orig) / orig) * 100.0;

        if (deviation <= percentTolerance) {
            // Within tolerance - perfect match
            return 1.0;
        } else if (deviation <= percentTolerance * 2.0) {
            // Outside tolerance but within 2x - linear decay
            double excessDeviation = deviation - percentTolerance;
            double maxExcessAllowed = percentTolerance; // 2x - 1x
            return 1.0 - (excessDeviation / maxExcessAllowed);
        } else {
            // Beyond 2x tolerance - no match
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("PercentageToleranceRule{±%.1f%%}", percentTolerance);
    }
}
