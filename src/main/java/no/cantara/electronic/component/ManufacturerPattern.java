package no.cantara.electronic.component;

/**
 * Represents a manufacturer-specific pattern for component identification
 */
public class ManufacturerPattern {
    private final String regex;
    private final ComponentType componentType;
    private final String description;

    public ManufacturerPattern(String regex, ComponentType componentType, String description) {
        this.regex = regex;
        this.componentType = componentType;
        this.description = description;
    }

    public String getRegex() {
        return regex;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public String getDescription() {
        return description;
    }
}