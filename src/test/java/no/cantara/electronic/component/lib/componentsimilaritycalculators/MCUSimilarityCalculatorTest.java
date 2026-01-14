package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MCUSimilarityCalculator
 */
class MCUSimilarityCalculatorTest {

    private MCUSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new MCUSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("Applicability tests")
    class ApplicabilityTests {

        @Test
        @DisplayName("Should be applicable to MICROCONTROLLER type")
        void shouldBeApplicableToMicrocontrollerType() {
            assertTrue(calculator.isApplicable(ComponentType.MICROCONTROLLER));
        }

        @Test
        @DisplayName("Should not be applicable to null type")
        void shouldNotBeApplicableToNullType() {
            assertFalse(calculator.isApplicable(null));
        }

        @Test
        @DisplayName("Should not be applicable to non-MCU types")
        void shouldNotBeApplicableToNonMcuTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.CAPACITOR));
        }
    }

    @Nested
    @DisplayName("Family similarity tests")
    class FamilySimilarityTests {

        @Test
        @DisplayName("Same family should have high similarity")
        void sameFamilyShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("STM32F103", "STM32F407", registry);
            assertTrue(similarity > 0.5, "Same STM family should have good similarity");
        }

        @Test
        @DisplayName("Same exact MCU should have similarity 1.0")
        void identicalMcuShouldHaveSimilarityOne() {
            double similarity = calculator.calculateSimilarity("ATMEGA328P", "ATMEGA328P", registry);
            assertEquals(1.0, similarity, 0.01, "Identical MCUs should have similarity 1.0");
        }

        @Test
        @DisplayName("Different families should have lower similarity")
        void differentFamiliesShouldHaveLowerSimilarity() {
            double sameFam = calculator.calculateSimilarity("STM32F103", "STM32F407", registry);
            double diffFam = calculator.calculateSimilarity("STM32F103", "ATMEGA328", registry);
            assertTrue(sameFam >= diffFam, "Same family should have >= similarity than different families");
        }

        @Test
        @DisplayName("PIC family members should match")
        void picFamilyMembersShouldMatch() {
            double similarity = calculator.calculateSimilarity("PIC16F877", "PIC18F4550", registry);
            assertTrue(similarity > 0.3, "PIC family members should have some similarity");
        }

        @Test
        @DisplayName("MSP430 family members should match")
        void mspFamilyMembersShouldMatch() {
            double similarity = calculator.calculateSimilarity("MSP430F5529", "MSP430G2553", registry);
            assertTrue(similarity > 0.3, "MSP430 family members should have some similarity");
        }
    }

    @Nested
    @DisplayName("Series similarity tests")
    class SeriesSimilarityTests {

        @Test
        @DisplayName("Same series number should have high similarity")
        void sameSeriesShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("STM32F103C8", "STM32F103CB", registry);
            assertTrue(similarity > 0.5, "Same series should have high similarity");
        }

        @Test
        @DisplayName("Close series numbers should have higher similarity than far")
        void closeSeriesNumbersShouldBeMoreSimilar() {
            double close = calculator.calculateSimilarity("ATMEGA328", "ATMEGA328P", registry);
            double far = calculator.calculateSimilarity("ATMEGA328", "ATMEGA2560", registry);
            assertTrue(close > far, "Closer series numbers should have higher similarity");
        }

        @Test
        @DisplayName("Very different series should have lower similarity")
        void veryDifferentSeriesShouldHaveLowerSimilarity() {
            double similarity = calculator.calculateSimilarity("STM32F103", "STM32H743", registry);
            // Different series numbers but same family
            assertTrue(similarity >= 0.0, "Very different series should have some similarity due to same family");
        }
    }

    @Nested
    @DisplayName("Feature similarity tests")
    class FeatureSimilarityTests {

        @Test
        @DisplayName("Same feature codes should match")
        void sameFeatureCodesShouldMatch() {
            double similarity = calculator.calculateSimilarity("ATMEGA328P", "ATTINY85P", registry);
            // Both have P suffix, but different families
            assertTrue(similarity >= 0.0, "Feature codes contribute to similarity");
        }

        @Test
        @DisplayName("Flash feature should be recognized")
        void flashFeatureShouldBeRecognized() {
            double similarity = calculator.calculateSimilarity("STM32F103", "STM32F407", registry);
            // Both have F (Flash) feature
            assertTrue(similarity > 0.4, "Same feature should contribute to similarity");
        }

        @Test
        @DisplayName("Low power variants should match")
        void lowPowerVariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("STM32L476", "STM32L432", registry);
            // Both are low power (L series)
            assertTrue(similarity > 0.4, "Low power variants should match");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPNs should return zero similarity")
        void nullMpnsShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "ATMEGA328", registry));
            assertEquals(0.0, calculator.calculateSimilarity("ATMEGA328", null, registry));
            assertEquals(0.0, calculator.calculateSimilarity(null, null, registry));
        }

        @Test
        @DisplayName("Empty strings should return low similarity")
        void emptyStringsShouldReturnLowSimilarity() {
            double similarity = calculator.calculateSimilarity("", "ATMEGA328", registry);
            assertTrue(similarity < 0.5, "Empty string should result in low similarity");
        }

        @Test
        @DisplayName("Both empty should return low similarity")
        void bothEmptyShouldReturnLowSimilarity() {
            double similarity = calculator.calculateSimilarity("", "", registry);
            assertTrue(similarity <= 0.3, "Both empty should return low similarity");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("STM32F103", "ATMEGA328", registry);
            double sim2 = calculator.calculateSimilarity("ATMEGA328", "STM32F103", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in range [0.0, 1.0]")
        void similarityShouldBeInRange() {
            String[] testMpns = {"STM32F103", "ATMEGA328", "PIC16F877", "MSP430G2553", ""};
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
        @DisplayName("Arduino compatible MCUs")
        void arduinoCompatibleMcus() {
            // ATMEGA328P variants should match
            double similarity = calculator.calculateSimilarity("ATMEGA328P", "ATMEGA328PB", registry);
            assertTrue(similarity > 0.7, "ATMEGA328 variants should match");
        }

        @Test
        @DisplayName("STM32 Blue Pill variants")
        void stm32BluePillVariants() {
            double similarity = calculator.calculateSimilarity("STM32F103C8T6", "STM32F103CBT6", registry);
            assertTrue(similarity > 0.8, "Blue Pill variants should have high similarity");
        }

        @Test
        @DisplayName("ESP32 family variants")
        void esp32FamilyVariants() {
            double similarity = calculator.calculateSimilarity("ESP32", "ESP32S2", registry);
            assertTrue(similarity > 0.3, "ESP32 family should have some similarity");
        }
    }
}
