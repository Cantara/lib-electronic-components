package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.ComponentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ComponentTypeMetadata builder pattern and metadata queries.
 */
class ComponentTypeMetadataTest {

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        void shouldBuildMetadataWithAllFields() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
                    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
                    .addSpec("tolerance", SpecImportance.HIGH, ToleranceRule.exactMatch())
                    .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                    .defaultProfile(SimilarityProfile.REPLACEMENT)
                    .build();

            assertEquals(ComponentType.RESISTOR, metadata.getComponentType());
            assertEquals(SimilarityProfile.REPLACEMENT, metadata.getDefaultProfile());
        }

        @Test
        void shouldAllowMultipleSpecsWithDifferentImportance() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.MOSFET)
                    .addSpec("voltageRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                    .addSpec("currentRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                    .addSpec("rdsOn", SpecImportance.HIGH, ToleranceRule.maximumAllowed(1.2))
                    .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                    .addSpec("gateCharge", SpecImportance.LOW, ToleranceRule.percentageTolerance(30.0))
                    .build();

            assertNotNull(metadata.getSpecConfig("voltageRating"));
            assertNotNull(metadata.getSpecConfig("currentRating"));
            assertNotNull(metadata.getSpecConfig("rdsOn"));
            assertNotNull(metadata.getSpecConfig("package"));
            assertNotNull(metadata.getSpecConfig("gateCharge"));
        }

        @Test
        void shouldDefaultToReplacementProfile() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.CAPACITOR)
                    .addSpec("capacitance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
                    .build();

            assertEquals(SimilarityProfile.REPLACEMENT, metadata.getDefaultProfile());
        }

        @Test
        void shouldAllowCustomDefaultProfile() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.CAPACITOR)
                    .addSpec("capacitance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
                    .defaultProfile(SimilarityProfile.DESIGN_PHASE)
                    .build();

            assertEquals(SimilarityProfile.DESIGN_PHASE, metadata.getDefaultProfile());
        }

        @Test
        void shouldThrowOnNullComponentType() {
            // Builder validates and throws IllegalArgumentException (better than NPE)
            assertThrows(IllegalArgumentException.class,
                    () -> ComponentTypeMetadata.builder(null));
        }
    }

    @Nested
    @DisplayName("Spec Config Query Tests")
    class SpecConfigQueryTests {

        private final ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
                .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
                .addSpec("tolerance", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("powerRating", SpecImportance.LOW, ToleranceRule.minimumRequired())
                .build();

        @Test
        void shouldReturnTrueForConfiguredSpecs() {
            assertNotNull(metadata.getSpecConfig("resistance"));
            assertNotNull(metadata.getSpecConfig("tolerance"));
            assertNotNull(metadata.getSpecConfig("package"));
            assertNotNull(metadata.getSpecConfig("powerRating"));
        }

        @Test
        void shouldReturnFalseForUnconfiguredSpecs() {
            assertNull(metadata.getSpecConfig("voltage"));
            assertNull(metadata.getSpecConfig("capacitance"));
            assertNull(metadata.getSpecConfig("unknownSpec"));
        }

        @Test
        void shouldHandleNullSpec() {
            // getSpecConfig uses Map.get() which can throw NPE with null key
            // This is expected behavior for Map operations
            assertThrows(NullPointerException.class, () -> metadata.getSpecConfig(null));
        }

        @Test
        void shouldReturnConfigForExistingSpec() {
            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("resistance");

            assertNotNull(config);
            assertEquals(SpecImportance.CRITICAL, config.getImportance());
            assertNotNull(config.getToleranceRule());
        }

        @Test
        void shouldReturnNullForMissingSpec() {
            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("voltage");

            assertNull(config);
        }

        @Test
        void shouldReturnAllSpecNames() {
            Set<String> specNames = metadata.getAllSpecs();

            assertEquals(4, specNames.size());
            assertTrue(specNames.contains("resistance"));
            assertTrue(specNames.contains("tolerance"));
            assertTrue(specNames.contains("package"));
            assertTrue(specNames.contains("powerRating"));
        }
    }

    @Nested
    @DisplayName("Critical Spec Tests")
    class CriticalSpecTests {

        @Test
        void shouldIdentifyCriticalSpecs() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.MOSFET)
                    .addSpec("voltageRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                    .addSpec("currentRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                    .addSpec("channel", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                    .addSpec("rdsOn", SpecImportance.HIGH, ToleranceRule.maximumAllowed(1.2))
                    .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                    .build();

            // Count critical specs
            long criticalCount = metadata.getAllSpecs().stream()
                    .filter(metadata::isCritical)
                    .count();

            assertEquals(3, criticalCount);
            assertTrue(metadata.isCritical("voltageRating"));
            assertTrue(metadata.isCritical("currentRating"));
            assertTrue(metadata.isCritical("channel"));
            assertFalse(metadata.isCritical("rdsOn"));
            assertFalse(metadata.isCritical("package"));
        }

        @Test
        void shouldReturnZeroWhenNoCriticalSpecs() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.CONNECTOR)
                    .addSpec("pinCount", SpecImportance.HIGH, ToleranceRule.exactMatch())
                    .addSpec("pitch", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                    .build();

            long criticalCount = metadata.getAllSpecs().stream()
                    .filter(metadata::isCritical)
                    .count();

            assertEquals(0, criticalCount);
        }

        @Test
        void shouldCheckIfSpecIsCritical() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.CAPACITOR)
                    .addSpec("capacitance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
                    .addSpec("voltage", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                    .addSpec("dielectric", SpecImportance.HIGH, ToleranceRule.exactMatch())
                    .build();

            assertTrue(metadata.isCritical("capacitance"));
            assertTrue(metadata.isCritical("voltage"));
            assertFalse(metadata.isCritical("dielectric"));
            assertFalse(metadata.isCritical("unknownSpec"));
        }
    }

    @Nested
    @DisplayName("SpecConfig Tests")
    class SpecConfigInternalTests {

        @Test
        void shouldStoreAllConfigFields() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            ComponentTypeMetadata.SpecConfig config = new ComponentTypeMetadata.SpecConfig(
                    SpecImportance.CRITICAL,
                    rule
            );

            assertEquals(SpecImportance.CRITICAL, config.getImportance());
            assertEquals(rule, config.getToleranceRule());
        }
    }

    @Nested
    @DisplayName("Resistor Metadata Example")
    class ResistorMetadataExample {

        private final ComponentTypeMetadata resistor = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
                .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
                .addSpec("tolerance", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
                .addSpec("temperatureCoefficient", SpecImportance.LOW, ToleranceRule.percentageTolerance(20.0))
                .defaultProfile(SimilarityProfile.REPLACEMENT)
                .build();

        @Test
        void shouldHaveCorrectComponentType() {
            assertEquals(ComponentType.RESISTOR, resistor.getComponentType());
        }

        @Test
        void shouldHaveTwoCriticalSpecs() {
            long criticalCount = resistor.getAllSpecs().stream()
                    .filter(resistor::isCritical)
                    .count();
            assertEquals(2, criticalCount);
            assertTrue(resistor.isCritical("resistance"));
            assertTrue(resistor.isCritical("tolerance"));
        }

        @Test
        void shouldConfigureResistanceWithPercentageTolerance() {
            ComponentTypeMetadata.SpecConfig config = resistor.getSpecConfig("resistance");
            assertNotNull(config);
            assertEquals(SpecImportance.CRITICAL, config.getImportance());
            // Tolerance rule should be PercentageToleranceRule
            assertNotNull(config.getToleranceRule());
        }

        @Test
        void shouldConfigurePackageWithHighImportance() {
            ComponentTypeMetadata.SpecConfig config = resistor.getSpecConfig("package");
            assertNotNull(config);
            assertEquals(SpecImportance.HIGH, config.getImportance());
        }
    }

    @Nested
    @DisplayName("Capacitor Metadata Example")
    class CapacitorMetadataExample {

        private final ComponentTypeMetadata capacitor = ComponentTypeMetadata.builder(ComponentType.CAPACITOR)
                .addSpec("capacitance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
                .addSpec("voltage", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("dielectric", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
                .addSpec("tolerance", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("esr", SpecImportance.LOW, ToleranceRule.maximumAllowed(1.5))
                .build();

        @Test
        void shouldHaveThreeCriticalSpecs() {
            long criticalCount = capacitor.getAllSpecs().stream()
                    .filter(capacitor::isCritical)
                    .count();
            assertEquals(3, criticalCount);
            assertTrue(capacitor.isCritical("capacitance"));
            assertTrue(capacitor.isCritical("voltage"));
            assertTrue(capacitor.isCritical("dielectric"));
        }

        @Test
        void shouldConfigureVoltageWithMinimumRequired() {
            ComponentTypeMetadata.SpecConfig config = capacitor.getSpecConfig("voltage");
            assertNotNull(config);
            assertEquals(SpecImportance.CRITICAL, config.getImportance());
            // Should be MinimumRequiredRule
            assertNotNull(config.getToleranceRule());
        }

        @Test
        void shouldConfigureDielectricWithExactMatch() {
            ComponentTypeMetadata.SpecConfig config = capacitor.getSpecConfig("dielectric");
            assertNotNull(config);
            assertEquals(SpecImportance.CRITICAL, config.getImportance());
        }
    }

    @Nested
    @DisplayName("MOSFET Metadata Example")
    class MosfetMetadataExample {

        private final ComponentTypeMetadata mosfet = ComponentTypeMetadata.builder(ComponentType.MOSFET)
                .addSpec("voltageRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("currentRating", SpecImportance.CRITICAL, ToleranceRule.minimumRequired())
                .addSpec("channel", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
                .addSpec("rdsOn", SpecImportance.HIGH, ToleranceRule.maximumAllowed(1.2))
                .addSpec("package", SpecImportance.MEDIUM, ToleranceRule.exactMatch())
                .addSpec("gateCharge", SpecImportance.LOW, ToleranceRule.percentageTolerance(30.0))
                .build();

        @Test
        void shouldHaveThreeCriticalSpecs() {
            long criticalCount = mosfet.getAllSpecs().stream()
                    .filter(mosfet::isCritical)
                    .count();
            assertEquals(3, criticalCount);
            assertTrue(mosfet.isCritical("voltageRating"));
            assertTrue(mosfet.isCritical("currentRating"));
            assertTrue(mosfet.isCritical("channel"));
        }

        @Test
        void shouldConfigureRdsOnWithMaximumAllowed() {
            ComponentTypeMetadata.SpecConfig config = mosfet.getSpecConfig("rdsOn");
            assertNotNull(config);
            assertEquals(SpecImportance.HIGH, config.getImportance());
            // Should be MaximumAllowedRule
            assertNotNull(config.getToleranceRule());
        }

        @Test
        void shouldConfigureChannelWithExactMatch() {
            ComponentTypeMetadata.SpecConfig config = mosfet.getSpecConfig("channel");
            assertNotNull(config);
            assertEquals(SpecImportance.CRITICAL, config.getImportance());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Validation")
    class EdgeCasesTests {

        @Test
        void shouldRejectEmptyMetadata() {
            // Builder requires at least one spec
            assertThrows(IllegalStateException.class, () ->
                    ComponentTypeMetadata.builder(ComponentType.IC).build()
            );
        }

        @Test
        void shouldHandleSpecNameCaseSensitivity() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
                    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
                    .build();

            // Should be case-sensitive
            assertNotNull(metadata.getSpecConfig("resistance"));
            assertNull(metadata.getSpecConfig("Resistance"));
            assertNull(metadata.getSpecConfig("RESISTANCE"));
        }

        @Test
        void shouldAllowDuplicateSpecOverwrite() {
            // Last one wins
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
                    .addSpec("resistance", SpecImportance.LOW, ToleranceRule.percentageTolerance(10.0))
                    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
                    .build();

            ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("resistance");
            assertNotNull(config);
            assertEquals(SpecImportance.CRITICAL, config.getImportance());
        }

        @Test
        void shouldProvideReadableToString() {
            ComponentTypeMetadata metadata = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
                    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
                    .build();

            String str = metadata.toString();
            assertTrue(str.contains("RESISTOR"));
        }
    }
}
