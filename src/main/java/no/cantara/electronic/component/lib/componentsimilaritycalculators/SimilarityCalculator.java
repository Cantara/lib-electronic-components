package no.cantara.electronic.component.lib.componentsimilaritycalculators;

public interface SimilarityCalculator {
    double calculateSimilarity(String normalizedMpn1, String normalizedMpn2);
}