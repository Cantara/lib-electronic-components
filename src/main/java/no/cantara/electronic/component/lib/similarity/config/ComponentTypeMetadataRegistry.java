package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.ComponentType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry of component type metadata definitions.
 *
 * Provides centralized access to metadata for component types, defining:
 * - Which specifications are important for similarity matching
 * - How to compare specification values (tolerance rules)
 * - Default similarity profiles
 *
 * Metadata is defined for the most common component types. For types without
 * specific metadata, a generic fallback is used.
 *
 * Thread-safe singleton implementation.
 */
public class ComponentTypeMetadataRegistry {

    private static final ComponentTypeMetadataRegistry INSTANCE = new ComponentTypeMetadataRegistry();

    private final Map<ComponentType, ComponentTypeMetadata> metadataMap = new HashMap<>();

    private ComponentTypeMetadataRegistry() {
        // Initialize metadata for top component types
        registerResistorMetadata();
        registerCapacitorMetadata();
        registerMosfetMetadata();
        registerTransistorMetadata();
        registerDiodeMetadata();
        registerOpAmpMetadata();
        registerMicrocontrollerMetadata();
        registerMemoryMetadata();
        registerLedMetadata();
        registerConnectorMetadata();
    }

    /**
     * Get the singleton instance.
     */
    public static ComponentTypeMetadataRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Get metadata for a component type.
     * Returns empty if no specific metadata is registered.
     *
     * @param type The component type
     * @return Optional containing metadata if registered
     */
    public Optional<ComponentTypeMetadata> getMetadata(ComponentType type) {
        if (type == null) return Optional.empty();

        // Try exact match first
        ComponentTypeMetadata metadata = metadataMap.get(type);
        if (metadata != null) {
            return Optional.of(metadata);
        }

        // Try base type if this is a manufacturer-specific type
        ComponentType baseType = type.getBaseType();
        if (baseType != type) {
            metadata = metadataMap.get(baseType);
            if (metadata != null) {
                return Optional.of(metadata);
            }
        }

        return Optional.empty();
    }

    /**
     * Register metadata for a component type.
     * Used internally and for testing/extension.
     *
     * @param metadata The metadata to register
     */
    public void register(ComponentTypeMetadata metadata) {
        if (metadata != null) {
            metadataMap.put(metadata.getComponentType(), metadata);
        }
    }

    // ========================================================================
    // Metadata Definitions for Top 10 Component Types
    // ========================================================================

    private void registerResistorMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
                .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
                .addSpec("tolerance", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .addSpec("temperatureCoefficient", SpecImportance.LOW, ToleranceRule.percentageTolerance(20.0))
                .addSpec("composition", SpecImportance.LOW, ToleranceRule.exactMatch())
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerCapacitorMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.CAPACITOR)
                .addSpec("capacitance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
                .addSpec("voltage", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("dielectric", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("tolerance", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("temperatureCharacteristic", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("esr", SpecImportance.LOW, ToleranceRule.maximumAllowed(1.5))
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerMosfetMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.MOSFET)
                .addSpec("voltageRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("currentRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("channel", SpecImportance.CRITICAL, ToleranceRule.exactMatch()) // N vs P
                .addSpec("rdsOn", SpecImportance.HIGH, ToleranceRule.maximumAllowed(1.2))
                .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("gateCharge", SpecImportance.LOW, ToleranceRule.percentageTolerance(30.0))
                .addSpec("threshold", SpecImportance.LOW, ToleranceRule.rangeTolerance(0.8, 1.2))
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerTransistorMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.TRANSISTOR)
                .addSpec("polarity", SpecImportance.CRITICAL, ToleranceRule.exactMatch()) // NPN vs PNP
                .addSpec("voltageRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("currentRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("hfe", SpecImportance.MEDIUM, ToleranceRule.rangeTolerance(0.7, 1.5))
                .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerDiodeMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.DIODE)
                .addSpec("type", SpecImportance.CRITICAL, ToleranceRule.exactMatch()) // signal, rectifier, zener
                .addSpec("voltageRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("currentRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("forwardVoltage", SpecImportance.MEDIUM, ToleranceRule.maximumAllowed(1.2))
                .addSpec("reverseRecovery", SpecImportance.LOW, ToleranceRule.maximumAllowed(1.5))
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerOpAmpMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.OPAMP)
                .addSpec("configuration", SpecImportance.CRITICAL, ToleranceRule.exactMatch()) // single, dual, quad
                .addSpec("inputType", SpecImportance.HIGH, ToleranceRule.exactMatch()) // JFET, bipolar, CMOS
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("gbw", SpecImportance.MEDIUM, ToleranceRule.minimumRequired()) // Gain-bandwidth product
                .addSpec("slewRate", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .addSpec("inputOffset", SpecImportance.LOW, ToleranceRule.maximumAllowed(1.5))
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerMicrocontrollerMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.MICROCONTROLLER)
                .addSpec("family", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("series", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("flashSize", SpecImportance.HIGH, ToleranceRule.minimumRequired())
                .addSpec("ramSize", SpecImportance.HIGH, ToleranceRule.minimumRequired())
                .addSpec("ioCount", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("frequency", SpecImportance.LOW, ToleranceRule.minimumRequired())
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerMemoryMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.MEMORY)
                .addSpec("type", SpecImportance.CRITICAL, ToleranceRule.exactMatch()) // EEPROM, Flash, SRAM
                .addSpec("capacity", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("interface", SpecImportance.CRITICAL, ToleranceRule.exactMatch()) // I2C, SPI, parallel
                .addSpec("voltage", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("speed", SpecImportance.LOW, ToleranceRule.minimumRequired())
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerLedMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.LED)
                .addSpec("color", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("brightness", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .addSpec("forwardVoltage", SpecImportance.MEDIUM, ToleranceRule.rangeTolerance(0.9, 1.1))
                .addSpec("viewingAngle", SpecImportance.LOW, ToleranceRule.minimumRequired())
                .addSpec("wavelength", SpecImportance.LOW, ToleranceRule.percentageTolerance(5.0))
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }

    private void registerConnectorMetadata() {
        ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.CONNECTOR)
                .addSpec("pinCount", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("pitch", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("gender", SpecImportance.CRITICAL, ToleranceRule.exactMatch()) // male, female
                .addSpec("mountingType", SpecImportance.HIGH, ToleranceRule.exactMatch()) // SMD, THT
                .addSpec("currentRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .addSpec("voltageRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        register(metadata);
    }
}
