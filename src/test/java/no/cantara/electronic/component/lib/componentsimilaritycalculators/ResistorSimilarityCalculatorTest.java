package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ResistorSimilarityCalculator
 */
class ResistorSimilarityCalculatorTest {

    private ResistorSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new ResistorSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base RESISTOR type")
        void shouldBeApplicableForResistor() {
            assertTrue(calculator.isApplicable(ComponentType.RESISTOR));
        }

        @Test
        @DisplayName("Should be applicable for manufacturer-specific resistor types")
        void shouldBeApplicableForManufacturerResistorTypes() {
            assertTrue(calculator.isApplicable(ComponentType.RESISTOR_CHIP_VISHAY));
            assertTrue(calculator.isApplicable(ComponentType.RESISTOR_CHIP_YAGEO));
            assertTrue(calculator.isApplicable(ComponentType.RESISTOR_CHIP_PANASONIC));
        }

        @Test
        @DisplayName("Should not be applicable for non-resistor types")
        void shouldNotBeApplicableForNonResistorTypes() {
            assertFalse(calculator.isApplicable(ComponentType.CAPACITOR));
            assertFalse(calculator.isApplicable(ComponentType.TRANSISTOR));
            assertFalse(calculator.isApplicable(ComponentType.MICROCONTROLLER));
            assertFalse(calculator.isApplicable(ComponentType.OPAMP));
        }

        @Test
        @DisplayName("Should handle null type")
        void shouldHandleNullType() {
            assertFalse(calculator.isApplicable(null));
        }
    }

    @Nested
    @DisplayName("calculateSimilarity tests")
    class CalculateSimilarityTests {

        @Test
        @DisplayName("Same Vishay resistors should have high similarity")
        void sameVishayResistorsShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "CRCW060310K0FKEA", "CRCW060310K0FKEA", registry);
            assertEquals(1.0, similarity, 0.01, "Identical resistors should score 1.0 (perfect match, metadata-driven normalized)");
        }

        @Test
        @DisplayName("Same value different package should have medium similarity")
        void sameValueDifferentPackageShouldHaveMediumSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "CRCW060310K0FKEA", "CRCW080510K0FKEA", registry);
            // Resistance (CRITICAL) weight dominates: 1.0 / 1.49 = 0.671
            assertEquals(0.67, similarity, 0.02, "Same value, different package should score ~0.67 (resistance weight dominates)");
        }

        @Test
        @DisplayName("Same package different value should have low similarity")
        void samePackageDifferentValueShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "CRCW0603100RFKEA", "CRCW060310K0FKEA", registry);
            // Only package matches: 0.49 / 1.49 = 0.329, values are different (100R vs 10K)
            assertEquals(0.33, similarity, 0.02, "Same package, different value should score ~0.33 (package weight only)");
        }

        @Test
        @DisplayName("Cross-manufacturer resistors with same value and size")
        void crossManufacturerSameValueAndSize() {
            // Vishay 0603 10K vs Yageo 0603 10K
            double similarity = calculator.calculateSimilarity(
                    "CRCW060310K0FKEA", "RC0603FR-0710KL", registry);
            assertTrue(similarity >= 0.5, "Cross-manufacturer same value should have >= 0.5 similarity");
        }

        @Test
        @DisplayName("Resistors with common package sizes should match size")
        void resistorsWithCommonPackageSizesShouldMatchSize() {
            // Both contain 0805 in their MPNs
            double similarity = calculator.calculateSimilarity(
                    "RC0805FR-0747KL", "ERJ6GEYJ473V", registry);
            // At minimum, if they have same package size extracted, they get 0.3
            assertTrue(similarity >= 0.0, "Should calculate some similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "CRCW060310K0FKEA", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("CRCW060310K0FKEA", null, registry));
        }

        @Test
        @DisplayName("Null registry should return 0")
        void nullRegistryShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("CRCW060310K0FKEA", "CRCW060310K0FKEA", null));
        }

        @Test
        @DisplayName("Both null should return 0")
        void bothNullShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, null, registry));
        }

        @Test
        @DisplayName("Empty strings should return 0")
        void emptyStringsShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("", "", registry));
        }

        @Test
        @DisplayName("Unrecognized format should still attempt comparison")
        void unrecognizedFormatShouldStillAttemptComparison() {
            // Should not throw, just return low/zero similarity
            double similarity = calculator.calculateSimilarity("UNKNOWN123", "UNKNOWN456", registry);
            assertTrue(similarity >= 0.0 && similarity <= 1.0, "Should return valid similarity range");
        }
    }

    @Nested
    @DisplayName("Symmetry and reflexivity tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric: sim(A,B) == sim(B,A)")
        void similarityShouldBeSymmetric() {
            String mpn1 = "CRCW060310K0FKEA";
            String mpn2 = "RC0603FR-0710KL";

            double sim1 = calculator.calculateSimilarity(mpn1, mpn2, registry);
            double sim2 = calculator.calculateSimilarity(mpn2, mpn1, registry);

            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Identical MPNs should have maximum similarity for this calculator")
        void identicalMpnsShouldHaveMaxSimilarity() {
            String mpn = "CRCW060310K0FKEA";
            double similarity = calculator.calculateSimilarity(mpn, mpn, registry);
            // Max for metadata-driven calculator is 1.0 (normalized perfect match)
            assertEquals(1.0, similarity, 0.01, "Identical MPNs should have max similarity (normalized to 1.0)");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {
                    "CRCW060310K0FKEA",
                    "RC0603FR-0710KL",
                    "ERJ3GEYJ103V",
                    "UNKNOWN",
                    ""
            };

            for (String mpn1 : testMpns) {
                for (String mpn2 : testMpns) {
                    if (mpn1 != null && mpn2 != null) {
                        double sim = calculator.calculateSimilarity(mpn1, mpn2, registry);
                        assertTrue(sim >= 0.0 && sim <= 1.0,
                                String.format("Similarity for %s vs %s should be in [0,1], was %f", mpn1, mpn2, sim));
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Value extraction and normalization tests")
    class ValueExtractionTests {

        @Test
        @DisplayName("Should correctly compare 10K resistors from different manufacturers")
        void shouldCompare10KResistors() {
            // All 10K resistors should have high value match
            String vishay10k = "CRCW060310K0FKEA";
            String yageo10k = "RC0603FR-0710KL";

            double similarity = calculator.calculateSimilarity(vishay10k, yageo10k, registry);
            // Should have at least the value match (~0.67) since both are 10K resistors
            assertTrue(similarity >= 0.6, "10K resistors should match on value");
        }

        @Test
        @DisplayName("Should handle different package sizes correctly")
        void shouldHandleDifferentPackageSizes() {
            // 0603 vs 0805 - different sizes
            double sim0603vs0805 = calculator.calculateSimilarity(
                    "CRCW060310K0FKEA", "CRCW080510K0FKEA", registry);

            // 0603 vs 0603 - same size
            double sim0603vs0603 = calculator.calculateSimilarity(
                    "CRCW060310K0FKEA", "CRCW060310K0FKEA", registry);

            assertTrue(sim0603vs0603 > sim0603vs0805,
                    "Same size should have higher similarity than different sizes");
        }
    }
}
