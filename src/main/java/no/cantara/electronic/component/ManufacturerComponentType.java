package no.cantara.electronic.component;

/**
 * Interface representing a manufacturer-specific component type
 */
public interface ManufacturerComponentType {
    String name();  // Enum-like name
    boolean isPassive();
    boolean isSemiconductor();
    ComponentType getBaseType();
}