package no.cantara.electronic.component.lib.specs.base;

import java.util.Map;

/**
 * Base interface for all component specifications.
 * Defines the core functionality that all component specifications must implement.
 */
public interface ComponentSpecification {
    /**
     * Gets all specifications as a map of key-value pairs.
     *
     * @return Map of specification keys to their values
     */
    Map<String, SpecValue<?>> getSpecs();

    /**
     * Checks if the component meets the given requirements.
     *
     * @param requirements Map of requirement keys to their required values/ranges
     * @return true if all requirements are met, false otherwise
     */
    boolean meetsRequirements(Map<String, String> requirements);

    /**
     * Gets a specific specification value by key.
     *
     * @param key The specification key
     * @return The specification value, or null if not present
     */
    SpecValue<?> getSpec(String key);

    /**
     * Sets a specification value.
     *
     * @param key The specification key
     * @param value The specification value
     */
    void setSpec(String key, SpecValue<?> value);

    /**
     * Validates the specifications according to component-specific rules.
     *
     * @return Map of validation errors (empty if valid)
     */
    Map<String, String> validate();
}