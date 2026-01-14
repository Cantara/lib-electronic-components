package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.ComponentType;

import java.util.*;

/**
 * Metadata for a component type defining which specifications matter and how much.
 *
 * Each component type (especially granular types like RESISTOR_CHIP_YAGEO, MOSFET_INFINEON)
 * has different specifications that are important for similarity matching.
 *
 * This class defines:
 * - Which specs are important (and how important)
 * - Tolerance rules for each spec (how to compare values)
 * - Critical specs that must be present and match
 * - Default similarity profile for this component type
 *
 * Example usage:
 * <pre>
 * ComponentTypeMetadata metadata = ComponentTypeMetadata.builder()
 *     .componentType(ComponentType.RESISTOR_CHIP_YAGEO)
 *     .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
 *     .addSpec("tolerance", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
 *     .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
 *     .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
 *     .defaultProfile(SimilarityProfile.REPLACEMENT)
 *     .build();
 * </pre>
 */
public class ComponentTypeMetadata {

    private final ComponentType componentType;
    private final Map<String, SpecConfig> specConfigs;
    private final Set<String> criticalSpecs;
    private final SimilarityProfile defaultProfile;

    private ComponentTypeMetadata(Builder builder) {
        this.componentType = builder.componentType;
        this.specConfigs = Map.copyOf(builder.specConfigs);  // Immutable
        this.criticalSpecs = Set.copyOf(builder.criticalSpecs);  // Immutable
        this.defaultProfile = builder.defaultProfile;
    }

    /**
     * Get the component type this metadata applies to.
     */
    public ComponentType getComponentType() {
        return componentType;
    }

    /**
     * Get all spec names defined in this metadata.
     */
    public Set<String> getAllSpecs() {
        return specConfigs.keySet();
    }

    /**
     * Get the importance level for a spec.
     *
     * @param specName The spec name (e.g., "resistance", "voltage")
     * @return Importance level, or OPTIONAL if spec not defined
     */
    public SpecImportance getImportance(String specName) {
        SpecConfig config = specConfigs.get(specName);
        return config != null ? config.importance : SpecImportance.OPTIONAL;
    }

    /**
     * Get the tolerance rule for a spec.
     *
     * @param specName The spec name
     * @return Tolerance rule, or exactMatch if spec not defined
     */
    public ToleranceRule getToleranceRule(String specName) {
        SpecConfig config = specConfigs.get(specName);
        return config != null ? config.toleranceRule : ToleranceRule.exactMatch();
    }

    /**
     * Check if a spec is critical (must be present and match closely).
     *
     * @param specName The spec name
     * @return true if spec is marked as critical
     */
    public boolean isCritical(String specName) {
        return criticalSpecs.contains(specName);
    }

    /**
     * Get the default similarity profile for this component type.
     */
    public SimilarityProfile getDefaultProfile() {
        return defaultProfile;
    }

    /**
     * Get the spec configuration for a given spec name.
     *
     * @param specName The spec name
     * @return SpecConfig or null if not defined
     */
    public SpecConfig getSpecConfig(String specName) {
        return specConfigs.get(specName);
    }

    /**
     * Create a new builder for ComponentTypeMetadata.
     *
     * @param componentType The component type this metadata applies to
     * @return New builder instance
     */
    public static Builder builder(ComponentType componentType) {
        return new Builder(componentType);
    }

    /**
     * Configuration for a single specification.
     */
    public static class SpecConfig {
        private final SpecImportance importance;
        private final ToleranceRule toleranceRule;

        public SpecConfig(SpecImportance importance, ToleranceRule toleranceRule) {
            this.importance = importance;
            this.toleranceRule = toleranceRule;
        }

        public SpecImportance getImportance() {
            return importance;
        }

        public ToleranceRule getToleranceRule() {
            return toleranceRule;
        }
    }

    /**
     * Builder for ComponentTypeMetadata.
     */
    public static class Builder {
        private final ComponentType componentType;
        private final Map<String, SpecConfig> specConfigs = new HashMap<>();
        private final Set<String> criticalSpecs = new HashSet<>();
        private SimilarityProfile defaultProfile = SimilarityProfile.REPLACEMENT;

        private Builder(ComponentType componentType) {
            if (componentType == null) {
                throw new IllegalArgumentException("Component type cannot be null");
            }
            this.componentType = componentType;
        }

        /**
         * Add a specification with importance and tolerance rule.
         *
         * @param specName Name of the spec (e.g., "resistance", "voltage")
         * @param importance How important this spec is for matching
         * @param toleranceRule How to compare values for this spec
         * @return this builder for chaining
         */
        public Builder addSpec(String specName, SpecImportance importance, ToleranceRule toleranceRule) {
            if (specName == null || specName.isEmpty()) {
                throw new IllegalArgumentException("Spec name cannot be null or empty");
            }
            if (importance == null) {
                throw new IllegalArgumentException("Importance cannot be null");
            }
            if (toleranceRule == null) {
                throw new IllegalArgumentException("Tolerance rule cannot be null");
            }

            specConfigs.put(specName, new SpecConfig(importance, toleranceRule));

            // Auto-mark CRITICAL specs as critical
            if (importance == SpecImportance.CRITICAL) {
                criticalSpecs.add(specName);
            }

            return this;
        }

        /**
         * Mark a spec as critical (must be present and match).
         * Note: CRITICAL importance specs are automatically marked as critical.
         *
         * @param specName The spec name
         * @return this builder for chaining
         */
        public Builder markCritical(String specName) {
            criticalSpecs.add(specName);
            return this;
        }

        /**
         * Set the default similarity profile for this component type.
         *
         * @param profile The default profile
         * @return this builder for chaining
         */
        public Builder defaultProfile(SimilarityProfile profile) {
            this.defaultProfile = profile;
            return this;
        }

        /**
         * Build the ComponentTypeMetadata instance.
         *
         * @return New immutable ComponentTypeMetadata
         */
        public ComponentTypeMetadata build() {
            if (specConfigs.isEmpty()) {
                throw new IllegalStateException("Cannot build metadata without any specs");
            }
            return new ComponentTypeMetadata(this);
        }
    }

    @Override
    public String toString() {
        return String.format("ComponentTypeMetadata{type=%s, specs=%d, critical=%d, profile=%s}",
                componentType, specConfigs.size(), criticalSpecs.size(), defaultProfile);
    }
}
