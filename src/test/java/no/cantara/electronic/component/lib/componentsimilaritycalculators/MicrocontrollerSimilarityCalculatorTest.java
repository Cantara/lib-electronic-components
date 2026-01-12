package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MicrocontrollerSimilarityCalculator
 */
class MicrocontrollerSimilarityCalculatorTest {

    private MicrocontrollerSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new MicrocontrollerSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base MICROCONTROLLER type")
        void shouldBeApplicableForMicrocontroller() {
            assertTrue(calculator.isApplicable(ComponentType.MICROCONTROLLER));
        }

        @Test
        @DisplayName("Should be applicable for Atmel microcontrollers")
        void shouldBeApplicableForAtmelMicrocontrollers() {
            assertTrue(calculator.isApplicable(ComponentType.MICROCONTROLLER_ATMEL));
            assertTrue(calculator.isApplicable(ComponentType.MCU_ATMEL));
        }

        @Test
        @DisplayName("Should be applicable for ST microcontrollers")
        void shouldBeApplicableForSTMicrocontrollers() {
            assertTrue(calculator.isApplicable(ComponentType.MICROCONTROLLER_ST));
        }

        @Test
        @DisplayName("Should be applicable for Infineon microcontrollers")
        void shouldBeApplicableForInfineonMicrocontrollers() {
            assertTrue(calculator.isApplicable(ComponentType.MICROCONTROLLER_INFINEON));
        }

        @Test
        @DisplayName("Should not be applicable for non-microcontroller types")
        void shouldNotBeApplicableForNonMicrocontrollerTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.CAPACITOR));
            assertFalse(calculator.isApplicable(ComponentType.TRANSISTOR));
        }

        @Test
        @DisplayName("Should handle null type")
        void shouldHandleNullType() {
            assertFalse(calculator.isApplicable(null));
        }
    }

    @Nested
    @DisplayName("Same manufacturer and series tests")
    class SameManufacturerSeriesTests {

        @Test
        @DisplayName("Same part different package should have very high similarity")
        void samePartDifferentPackageShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "ATMEGA328P-AU", "ATMEGA328P-PU", registry);
            assertTrue(similarity >= 0.5, "Same part different package should have high similarity");
        }

        @Test
        @DisplayName("Identical parts should have similarity 1.0")
        void identicalPartsShouldHaveSimilarityOne() {
            double similarity = calculator.calculateSimilarity(
                    "ATMEGA328P-AU", "ATMEGA328P-AU", registry);
            assertTrue(similarity >= 0.5, "Identical parts should have high similarity");
        }
    }

    @Nested
    @DisplayName("Cross-manufacturer tests")
    class CrossManufacturerTests {

        @Test
        @DisplayName("Different microcontrollers should have base similarity")
        void differentMicrocontrollersShouldHaveBaseSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "ATMEGA328P", "STM32F103C8T6", registry);
            assertTrue(similarity >= 0.5, "Different MCUs should have at least base similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "ATMEGA328P", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("ATMEGA328P", null, registry));
        }

        @Test
        @DisplayName("Null registry should return 0")
        void nullRegistryShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("ATMEGA328P", "ATMEGA328P", null));
        }

        @Test
        @DisplayName("Both null should return 0")
        void bothNullShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, null, registry));
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("ATMEGA328P", "STM32F103C8T6", registry);
            double sim2 = calculator.calculateSimilarity("STM32F103C8T6", "ATMEGA328P", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"ATMEGA328P", "STM32F103C8T6", "PIC16F877A"};
            for (String mpn1 : testMpns) {
                for (String mpn2 : testMpns) {
                    double sim = calculator.calculateSimilarity(mpn1, mpn2, registry);
                    assertTrue(sim >= 0.0 && sim <= 1.0,
                            String.format("Similarity for %s vs %s should be in [0,1], was %f", mpn1, mpn2, sim));
                }
            }
        }
    }

    @Nested
    @DisplayName("Real-world MCU tests")
    class RealWorldTests {

        @Test
        @DisplayName("Arduino MCU variants")
        void arduinoMcuVariants() {
            double similarity = calculator.calculateSimilarity(
                    "ATMEGA328P-AU", "ATMEGA328P-PU", registry);
            assertTrue(similarity >= 0.5, "Arduino MCU variants should have high similarity");
        }

        @Test
        @DisplayName("STM32 variants")
        void stm32Variants() {
            double similarity = calculator.calculateSimilarity(
                    "STM32F103C8T6", "STM32F103CBT6", registry);
            assertTrue(similarity >= 0.5, "STM32 variants should have high similarity");
        }
    }
}
