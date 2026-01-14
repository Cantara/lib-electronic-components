package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Requires candidate value to be >= original value.
 * Used for ratings like voltage, current, power where higher is acceptable/better.
 *
 * Scoring:
 * - candidate == original: 1.0 (perfect match)
 * - candidate > original: 0.95 (acceptable upgrade, slight penalty for over-spec)
 * - candidate >= 0.9 * original: 0.8 (marginal, within 10% lower)
 * - candidate < 0.9 * original: 0.0 (unacceptable)
 */
class MinimumRequiredRule implements ToleranceRule {

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

        if (cand >= orig) {
            // Candidate meets or exceeds requirement
            if (cand == orig) {
                return 1.0; // Perfect match
            } else {
                // Higher rating is acceptable but penalize over-spec slightly
                // to prefer closer matches when multiple options available
                return 0.95;
            }
        } else if (cand >= orig * 0.9) {
            // Marginal - within 10% lower than required
            // May be acceptable for non-critical applications
            return 0.8;
        } else {
            // Below minimum requirement - unacceptable
            return 0.0;
        }
    }

    @Override
    public boolean isAcceptable(SpecValue<?> original, SpecValue<?> candidate) {
        // For minimum required, we're more lenient - accept >= 0.8
        return compare(original, candidate) >= 0.8;
    }

    @Override
    public String toString() {
        return "MinimumRequiredRule{candidate >= original}";
    }
}
