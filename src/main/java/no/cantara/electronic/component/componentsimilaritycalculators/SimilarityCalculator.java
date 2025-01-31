package no.cantara.electronic.component.componentsimilaritycalculators;

public interface SimilarityCalculator {
    double calculateSimilarity(String normalizedMpn1, String normalizedMpn2);
}