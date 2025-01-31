package no.cantara.electronic.component.lib;

/**
 * Similarity dimensions for component comparison
 */
public enum SimilarityDimension {
    ELECTRICAL,      // Electrically equivalent (same specs)
    PHYSICAL,        // Same physical package and pinout
    FUNCTIONAL,      // Same functional characteristics
    MANUFACTURER,    // Same manufacturer
    FAMILY,
    SERIES,
    PACKAGE,
    REPLACEMENT     // Listed as official replacement/alternative
}