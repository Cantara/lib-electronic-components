package no.cantara.electronic.component.lib.similarity.config;

import java.util.Map;

/**
 * Defines context-aware similarity profiles for different use cases.
 *
 * Each profile adjusts the importance weights based on the scenario:
 * - DESIGN_PHASE: Strict matching, exact specs required
 * - REPLACEMENT: Drop-in replacement, form/fit/function compatible
 * - COST_OPTIMIZATION: Accept downgrades if cheaper, maintain safety specs
 * - PERFORMANCE_UPGRADE: Prefer better specs, accept over-spec
 * - EMERGENCY_SOURCING: Any functional equivalent acceptable
 *
 * Profiles multiply the base importance weights from ComponentTypeMetadata
 * to produce the final effective weights for similarity calculation.
 */
public enum SimilarityProfile {

    /**
     * Design phase - exact specification match required.
     * Used when designing a new circuit/system where exact component specs matter.
     *
     * Characteristics:
     * - Very strict matching (high weights for all importance levels)
     * - High minimum similarity threshold (0.85)
     * - Critical specs must match very closely
     */
    DESIGN_PHASE(
        "Exact specification match required",
        Map.of(
            SpecImportance.CRITICAL, 1.0,   // Must match
            SpecImportance.HIGH, 0.9,       // Very strict
            SpecImportance.MEDIUM, 0.7,     // Still important
            SpecImportance.LOW, 0.4,        // Consider
            SpecImportance.OPTIONAL, 0.0    // Ignore
        ),
        0.85  // Minimum acceptable similarity score
    ),

    /**
     * Replacement - drop-in replacement for existing component.
     * Used when finding alternatives for out-of-stock or obsolete parts.
     *
     * Characteristics:
     * - Moderate strictness (critical specs must match, others flexible)
     * - Medium minimum similarity threshold (0.75)
     * - Form/fit/function compatibility prioritized
     */
    REPLACEMENT(
        "Drop-in replacement - form/fit/function compatible",
        Map.of(
            SpecImportance.CRITICAL, 1.0,   // Must match
            SpecImportance.HIGH, 0.7,       // Some flexibility
            SpecImportance.MEDIUM, 0.4,     // Relaxed
            SpecImportance.LOW, 0.2,        // Minimal importance
            SpecImportance.OPTIONAL, 0.0    // Ignore
        ),
        0.75  // Accept looser matches
    ),

    /**
     * Cost optimization - accept downgrades if cheaper while maintaining safety.
     * Used for value engineering and cost reduction initiatives.
     *
     * Characteristics:
     * - Relaxed for non-safety specs
     * - Low minimum similarity threshold (0.60)
     * - Safety/critical specs still enforced
     */
    COST_OPTIMIZATION(
        "Accept downgrade if cheaper, maintain critical specs",
        Map.of(
            SpecImportance.CRITICAL, 1.0,   // Safety specs still critical
            SpecImportance.HIGH, 0.4,       // Significantly relaxed
            SpecImportance.MEDIUM, 0.2,     // Minimal importance
            SpecImportance.LOW, 0.0,        // Ignore
            SpecImportance.OPTIONAL, 0.0    // Ignore
        ),
        0.60  // Much looser matching
    ),

    /**
     * Performance upgrade - prioritize better specs, accept over-spec.
     * Used when looking for improved alternatives (lower Rds, higher frequency, etc.).
     *
     * Characteristics:
     * - Higher weights on performance specs
     * - Medium-high minimum similarity threshold (0.70)
     * - Prefers upgrades over exact matches
     */
    PERFORMANCE_UPGRADE(
        "Accept better specs, prioritize performance",
        Map.of(
            SpecImportance.CRITICAL, 1.0,   // Must be compatible
            SpecImportance.HIGH, 0.8,       // Higher weight on performance specs
            SpecImportance.MEDIUM, 0.5,     // Moderately important
            SpecImportance.LOW, 0.2,        // Consider
            SpecImportance.OPTIONAL, 0.0    // Ignore
        ),
        0.70
    ),

    /**
     * Emergency sourcing - any functional equivalent acceptable.
     * Used when component is urgently needed and exact match not available.
     *
     * Characteristics:
     * - Very relaxed matching (even critical specs can flex)
     * - Low minimum similarity threshold (0.50)
     * - Functional equivalence is sufficient
     */
    EMERGENCY_SOURCING(
        "Any functional equivalent acceptable",
        Map.of(
            SpecImportance.CRITICAL, 0.8,   // Even critical specs can flex
            SpecImportance.HIGH, 0.4,       // Relaxed
            SpecImportance.MEDIUM, 0.2,     // Minimal
            SpecImportance.LOW, 0.0,        // Ignore
            SpecImportance.OPTIONAL, 0.0    // Ignore
        ),
        0.50  // Very loose matching
    );

    private final String description;
    private final Map<SpecImportance, Double> weightMultipliers;
    private final double minimumScore;

    SimilarityProfile(String description, Map<SpecImportance, Double> weightMultipliers, double minimumScore) {
        this.description = description;
        this.weightMultipliers = Map.copyOf(weightMultipliers);  // Immutable copy
        this.minimumScore = minimumScore;
    }

    /**
     * Get the human-readable description of this profile.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the weight multiplier for a given importance level in this profile.
     *
     * @param importance The spec importance level
     * @return Multiplier to apply to base weight (0.0 to 1.0)
     */
    public double getMultiplier(SpecImportance importance) {
        return weightMultipliers.getOrDefault(importance, 0.0);
    }

    /**
     * Get the minimum acceptable similarity score for this profile.
     * Matches below this threshold should be rejected.
     *
     * @return Minimum score from 0.0 to 1.0
     */
    public double getMinimumScore() {
        return minimumScore;
    }

    /**
     * Calculate the effective weight for a spec given its importance and this profile.
     *
     * @param baseImportance The spec's base importance level
     * @return Effective weight = baseWeight * profileMultiplier
     */
    public double getEffectiveWeight(SpecImportance baseImportance) {
        return baseImportance.getBaseWeight() * getMultiplier(baseImportance);
    }

    /**
     * Check if a similarity score meets the minimum threshold for this profile.
     *
     * @param score The calculated similarity score
     * @return true if score >= minimum threshold
     */
    public boolean meetsThreshold(double score) {
        return score >= minimumScore;
    }
}
