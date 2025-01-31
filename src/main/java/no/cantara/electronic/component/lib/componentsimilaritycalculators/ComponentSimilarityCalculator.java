package no.cantara.electronic.component.lib.componentsimilaritycalculators;


import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

public interface ComponentSimilarityCalculator {
    /**
     * Calculate similarity between two components
     * @param mpn1 First component MPN
     * @param mpn2 Second component MPN
     * @param registry Pattern registry to use for matching
     * @return Similarity score between 0.0 and 1.0
     */
    double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry);

    /**
     * Check if this calculator is applicable for the given component type
     * @param type Component type to check
     * @return true if this calculator can handle the type
     */
    boolean isApplicable(ComponentType type);
}