package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Requires exact match between specification values.
 * Used for categorical specs like dielectric type, polarity, package type.
 */
class ExactMatchRule implements ToleranceRule {

    @Override
    public double compare(SpecValue<?> original, SpecValue<?> candidate) {
        if (original == null || candidate == null) return 0.0;

        // For exact match, values must be identical
        Object origValue = original.getValue();
        Object candValue = candidate.getValue();

        if (origValue == null && candValue == null) return 1.0;
        if (origValue == null || candValue == null) return 0.0;

        // Case-insensitive string comparison for text values
        if (origValue instanceof String && candValue instanceof String) {
            return ((String) origValue).equalsIgnoreCase((String) candValue) ? 1.0 : 0.0;
        }

        // Exact object equality for other types
        return origValue.equals(candValue) ? 1.0 : 0.0;
    }

    @Override
    public String toString() {
        return "ExactMatchRule{}";
    }
}
