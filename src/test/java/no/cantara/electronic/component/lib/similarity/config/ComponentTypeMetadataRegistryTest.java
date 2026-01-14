package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.ComponentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ComponentTypeMetadataRegistry singleton and metadata lookup.
 */
class ComponentTypeMetadataRegistryTest {

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonTests {

        @Test
        void shouldReturnSameInstanceOnMultipleCalls() {
            ComponentTypeMetadataRegistry instance1 = ComponentTypeMetadataRegistry.getInstance();
            ComponentTypeMetadataRegistry instance2 = ComponentTypeMetadataRegistry.getInstance();

            assertSame(instance1, instance2);
        }

        @Test
        void shouldNotBeNull() {
            ComponentTypeMetadataRegistry instance = ComponentTypeMetadataRegistry.getInstance();
            assertNotNull(instance);
        }
    }

    @Nested
    @DisplayName("Pre-registered Component Type Tests")
    class PreRegisteredTypesTests {

        private final ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

        @Test
        void shouldHaveResistorMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.RESISTOR);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.RESISTOR, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("resistance"));
            assertNotNull(metadata.get().getSpecConfig("tolerance"));
        }

        @Test
        void shouldHaveCapacitorMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.CAPACITOR);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.CAPACITOR, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("capacitance"));
            assertNotNull(metadata.get().getSpecConfig("voltage"));
            assertNotNull(metadata.get().getSpecConfig("dielectric"));
        }

        @Test
        void shouldHaveMosfetMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.MOSFET);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.MOSFET, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("voltageRating"));
            assertNotNull(metadata.get().getSpecConfig("currentRating"));
            assertNotNull(metadata.get().getSpecConfig("channel"));
        }

        @Test
        void shouldHaveTransistorMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.TRANSISTOR);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.TRANSISTOR, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("polarity"));
            assertNotNull(metadata.get().getSpecConfig("voltageRating"));
        }

        @Test
        void shouldHaveDiodeMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.DIODE);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.DIODE, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("type"));
            assertNotNull(metadata.get().getSpecConfig("voltageRating"));
        }

        @Test
        void shouldHaveOpAmpMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.OPAMP);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.OPAMP, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("configuration"));
            assertNotNull(metadata.get().getSpecConfig("inputType"));
        }

        @Test
        void shouldHaveMicrocontrollerMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.MICROCONTROLLER);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.MICROCONTROLLER, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("family"));
            assertNotNull(metadata.get().getSpecConfig("flashSize"));
        }

        @Test
        void shouldHaveMemoryMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.MEMORY);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.MEMORY, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("type"));
            assertNotNull(metadata.get().getSpecConfig("capacity"));
            assertNotNull(metadata.get().getSpecConfig("interface"));
        }

        @Test
        void shouldHaveLedMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.LED);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.LED, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("color"));
            assertNotNull(metadata.get().getSpecConfig("brightness"));
        }

        @Test
        void shouldHaveConnectorMetadata() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.CONNECTOR);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.CONNECTOR, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("pinCount"));
            assertNotNull(metadata.get().getSpecConfig("pitch"));
            assertNotNull(metadata.get().getSpecConfig("gender"));
        }
    }

    @Nested
    @DisplayName("Metadata Lookup Tests")
    class MetadataLookupTests {

        private final ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

        @Test
        void shouldReturnEmptyForUnregisteredType() {
            // CRYSTAL is not in the pre-registered list and not registered by other tests
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.CRYSTAL);

            assertFalse(metadata.isPresent());
        }

        @Test
        void shouldReturnEmptyForNull() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(null);

            assertFalse(metadata.isPresent());
        }

        @Test
        void shouldFindExactMatch() {
            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.RESISTOR);

            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.RESISTOR, metadata.get().getComponentType());
        }
    }

    @Nested
    @DisplayName("Base Type Fallback Tests")
    class BaseTypeFallbackTests {

        private final ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

        @Test
        void shouldFallbackToBaseTypeForManufacturerSpecificType() {
            // Assuming RESISTOR_CHIP_YAGEO exists and its base is RESISTOR
            ComponentType yageoResistor = ComponentType.RESISTOR_CHIP_YAGEO;

            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(yageoResistor);

            // Should fallback to RESISTOR metadata
            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.RESISTOR, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("resistance"));
        }

        @Test
        void shouldFallbackToBaseTypeForInfineonMosfet() {
            ComponentType infineonMosfet = ComponentType.MOSFET_INFINEON;

            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(infineonMosfet);

            // Should fallback to MOSFET metadata
            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.MOSFET, metadata.get().getComponentType());
            assertNotNull(metadata.get().getSpecConfig("voltageRating"));
        }

        @Test
        void shouldReturnEmptyWhenBaseTypeAlsoUnregistered() {
            // IC is not registered, and manufacturer-specific IC types should also fail
            ComponentType tiIC = ComponentType.OPAMP_TI; // Base is OPAMP which IS registered

            Optional<ComponentTypeMetadata> metadata = registry.getMetadata(tiIC);

            // Should fallback to OPAMP
            assertTrue(metadata.isPresent());
            assertEquals(ComponentType.OPAMP, metadata.get().getComponentType());
        }
    }

    @Nested
    @DisplayName("Critical Spec Configuration Tests")
    class CriticalSpecTests {

        private final ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

        @Test
        void resistorShouldHaveTwoCriticalSpecs() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.RESISTOR).orElseThrow();

            assertEquals(2, metadata.getAllSpecs().stream().filter(metadata::isCritical).collect(Collectors.toSet()).size());
            assertTrue(metadata.isCritical("resistance"));
            assertTrue(metadata.isCritical("tolerance"));
        }

        @Test
        void capacitorShouldHaveThreeCriticalSpecs() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.CAPACITOR).orElseThrow();

            assertEquals(3, metadata.getAllSpecs().stream().filter(metadata::isCritical).collect(Collectors.toSet()).size());
            assertTrue(metadata.isCritical("capacitance"));
            assertTrue(metadata.isCritical("voltage"));
            assertTrue(metadata.isCritical("dielectric"));
        }

        @Test
        void mosfetShouldHaveThreeCriticalSpecs() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.MOSFET).orElseThrow();

            assertEquals(3, metadata.getAllSpecs().stream().filter(metadata::isCritical).collect(Collectors.toSet()).size());
            assertTrue(metadata.isCritical("voltageRating"));
            assertTrue(metadata.isCritical("currentRating"));
            assertTrue(metadata.isCritical("channel"));
        }

        @Test
        void connectorShouldHaveThreeCriticalSpecs() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.CONNECTOR).orElseThrow();

            assertEquals(3, metadata.getAllSpecs().stream().filter(metadata::isCritical).collect(Collectors.toSet()).size());
            assertTrue(metadata.isCritical("pinCount"));
            assertTrue(metadata.isCritical("pitch"));
            assertTrue(metadata.isCritical("gender"));
        }
    }

    @Nested
    @DisplayName("Tolerance Rule Configuration Tests")
    class ToleranceRuleTests {

        private final ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

        @Test
        void resistanceShouldUsePercentageTolerance() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.RESISTOR).orElseThrow();
            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("resistance");

            assertNotNull(config.getToleranceRule());
            assertTrue(config.getToleranceRule().toString().contains("Percentage"));
        }

        @Test
        void voltageRatingShouldUseMinimumRequired() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.MOSFET).orElseThrow();
            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("voltageRating");

            assertNotNull(config.getToleranceRule());
            assertTrue(config.getToleranceRule().toString().contains("Minimum"));
        }

        @Test
        void rdsOnShouldUseMaximumAllowed() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.MOSFET).orElseThrow();
            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("rdsOn");

            assertNotNull(config.getToleranceRule());
            assertTrue(config.getToleranceRule().toString().contains("Maximum"));
        }

        @Test
        void channelShouldUseExactMatch() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.MOSFET).orElseThrow();
            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("channel");

            assertNotNull(config.getToleranceRule());
            assertTrue(config.getToleranceRule().toString().contains("Exact"));
        }

        @Test
        void hfeShouldUseRangeTolerance() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.TRANSISTOR).orElseThrow();
            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("hfe");

            assertNotNull(config.getToleranceRule());
            assertTrue(config.getToleranceRule().toString().contains("Range"));
        }
    }

    @Nested
    @DisplayName("Default Profile Configuration Tests")
    class DefaultProfileTests {

        private final ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

        @Test
        void allRegisteredTypesShouldHaveReplacementProfile() {
            ComponentType[] types = {
                    ComponentType.RESISTOR,
                    ComponentType.CAPACITOR,
                    ComponentType.MOSFET,
                    ComponentType.TRANSISTOR,
                    ComponentType.DIODE,
                    ComponentType.OPAMP,
                    ComponentType.MICROCONTROLLER,
                    ComponentType.MEMORY,
                    ComponentType.LED,
                    ComponentType.CONNECTOR
            };

            for (ComponentType type : types) {
                ComponentTypeMetadata metadata = registry.getMetadata(type).orElseThrow();
                assertEquals(SimilarityProfile.REPLACEMENT, metadata.getDefaultProfile(),
                        type + " should have REPLACEMENT profile");
            }
        }
    }

    @Nested
    @DisplayName("Custom Registration Tests")
    class CustomRegistrationTests {

        @Test
        void shouldAllowCustomRegistration() {
            ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

            // Register custom metadata for IC
            ComponentTypeMetadata customMetadata = ComponentTypeMetadata.builder(ComponentType.IC)
                    .addSpec("family", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                    .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                    .build();

            registry.register(customMetadata);

            // Should now be available
            Optional<ComponentTypeMetadata> retrieved = registry.getMetadata(ComponentType.IC);
            assertTrue(retrieved.isPresent());
            assertEquals(ComponentType.IC, retrieved.get().getComponentType());
            assertNotNull(retrieved.get().getSpecConfig("family"));
        }

        @Test
        void shouldOverwriteExistingRegistration() {
            ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

            // Register new metadata for INDUCTOR (overwrites if exists, but INDUCTOR not pre-registered)
            // First register some initial metadata
            ComponentTypeMetadata initialMetadata = ComponentTypeMetadata.builder(ComponentType.INDUCTOR)
                    .addSpec("inductance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
                    .build();
            registry.register(initialMetadata);

            // Now overwrite with different metadata
            ComponentTypeMetadata customMetadata = ComponentTypeMetadata.builder(ComponentType.INDUCTOR)
                    .addSpec("customSpec", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                    .build();

            registry.register(customMetadata);

            // Should now have the custom spec (not inductance)
            Optional<ComponentTypeMetadata> retrieved = registry.getMetadata(ComponentType.INDUCTOR);
            assertTrue(retrieved.isPresent());
            assertNotNull(retrieved.get().getSpecConfig("customSpec"));
            // Verify old spec is gone
            assertNull(retrieved.get().getSpecConfig("inductance"));
        }

        @Test
        void shouldIgnoreNullRegistration() {
            ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

            // Should not throw
            assertDoesNotThrow(() -> registry.register(null));
        }
    }

    @Nested
    @DisplayName("Real-World Scenario Tests")
    class RealWorldScenarioTests {

        private final ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

        @Test
        void shouldProvideCompleteResistorMetadata() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.RESISTOR).orElseThrow();

            // Verify all expected specs are present
            assertNotNull(metadata.getSpecConfig("resistance"));
            assertNotNull(metadata.getSpecConfig("tolerance"));
            assertNotNull(metadata.getSpecConfig("package"));
            assertNotNull(metadata.getSpecConfig("powerRating"));
            assertNotNull(metadata.getSpecConfig("temperatureCoefficient"));

            // Verify importance levels
            assertEquals(SpecImportance.CRITICAL, metadata.getSpecConfig("resistance").getImportance());
            assertEquals(SpecImportance.CRITICAL, metadata.getSpecConfig("tolerance").getImportance());
            assertEquals(SpecImportance.HIGH, metadata.getSpecConfig("package").getImportance());
            assertEquals(SpecImportance.MEDIUM, metadata.getSpecConfig("powerRating").getImportance());
            assertEquals(SpecImportance.LOW, metadata.getSpecConfig("temperatureCoefficient").getImportance());
        }

        @Test
        void shouldProvideCompleteCapacitorMetadata() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.CAPACITOR).orElseThrow();

            // Verify ceramic capacitor specs
            assertNotNull(metadata.getSpecConfig("capacitance"));
            assertNotNull(metadata.getSpecConfig("voltage"));
            assertNotNull(metadata.getSpecConfig("dielectric"));
            assertNotNull(metadata.getSpecConfig("package"));
            assertNotNull(metadata.getSpecConfig("tolerance"));
            assertNotNull(metadata.getSpecConfig("temperatureCharacteristic"));
            assertNotNull(metadata.getSpecConfig("esr"));
        }

        @Test
        void shouldProvideMosfetMetadataForPowerApplications() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.MOSFET).orElseThrow();

            // Key specs for MOSFET selection
            assertNotNull(metadata.getSpecConfig("voltageRating"));
            assertNotNull(metadata.getSpecConfig("currentRating"));
            assertNotNull(metadata.getSpecConfig("rdsOn"));
            assertNotNull(metadata.getSpecConfig("channel"));

            // Voltage and current must be >= (MinimumRequired)
            // Rds(on) must be <= (MaximumAllowed)
            // Channel must match exactly
            assertTrue(metadata.isCritical("voltageRating"));
            assertTrue(metadata.isCritical("currentRating"));
            assertTrue(metadata.isCritical("channel"));
        }

        @Test
        void shouldProvideConnectorMetadataForPCBDesign() {
            ComponentTypeMetadata metadata = registry.getMetadata(ComponentType.CONNECTOR).orElseThrow();

            // Critical specs that must match exactly
            assertTrue(metadata.isCritical("pinCount"));
            assertTrue(metadata.isCritical("pitch"));
            assertTrue(metadata.isCritical("gender"));

            // Additional important specs
            assertNotNull(metadata.getSpecConfig("mountingType"));
            assertNotNull(metadata.getSpecConfig("currentRating"));
            assertNotNull(metadata.getSpecConfig("voltageRating"));
        }
    }
}
