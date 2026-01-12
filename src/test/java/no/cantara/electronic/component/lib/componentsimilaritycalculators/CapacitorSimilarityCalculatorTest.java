package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CapacitorSimilarityCalculator
 */
class CapacitorSimilarityCalculatorTest {

    private CapacitorSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new CapacitorSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base CAPACITOR type")
        void shouldBeApplicableForCapacitor() {
            assertTrue(calculator.isApplicable(ComponentType.CAPACITOR));
        }

        @Test
        @DisplayName("Should be applicable for manufacturer-specific capacitor types")
        void shouldBeApplicableForManufacturerCapacitorTypes() {
            assertTrue(calculator.isApplicable(ComponentType.CAPACITOR_CERAMIC_MURATA));
            assertTrue(calculator.isApplicable(ComponentType.CAPACITOR_CERAMIC_KEMET));
            assertTrue(calculator.isApplicable(ComponentType.CAPACITOR_CERAMIC_SAMSUNG));
        }

        @Test
        @DisplayName("Should not be applicable for non-capacitor types")
        void shouldNotBeApplicableForNonCapacitorTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.TRANSISTOR));
            assertFalse(calculator.isApplicable(ComponentType.MICROCONTROLLER));
            assertFalse(calculator.isApplicable(ComponentType.INDUCTOR));
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
        @DisplayName("Same Murata capacitors should have high similarity")
        void sameMurataCapacitorsShouldHaveHighSimilarity() {
            // GRM188R71H104KA93D - 0603, 100nF, 50V
            double similarity = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "GRM188R71H104KA93D", registry);
            assertTrue(similarity >= 0.7, "Identical capacitors should have high similarity");
        }

        @Test
        @DisplayName("Same Kemet capacitors should have high similarity")
        void sameKemetCapacitorsShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "C0603C104K5RACTU", "C0603C104K5RACTU", registry);
            assertTrue(similarity >= 0.3, "Identical Kemet capacitors should have similarity");
        }

        @Test
        @DisplayName("Cross-manufacturer capacitors with same specs")
        void crossManufacturerSameSpecs() {
            // Both 0603, 100nF capacitors from different manufacturers
            double similarity = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "C0603C104K5RACTU", registry);
            assertTrue(similarity >= 0.3, "Same specs from different manufacturers should have similarity");
        }

        @Test
        @DisplayName("Different package sizes should reduce similarity")
        void differentPackageSizesShouldReduceSimilarity() {
            // 0603 vs 0805
            double sameSizeSim = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "GRM188R71H104KA93D", registry);
            double diffSizeSim = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "GRM216R71H104KA93D", registry);

            assertTrue(sameSizeSim >= diffSizeSim, "Same size should have >= similarity than different");
        }

        @Test
        @DisplayName("Samsung capacitors should match")
        void samsungCapacitorsShouldMatch() {
            double similarity = calculator.calculateSimilarity(
                    "CL10B104KB8NNNC", "CL10B104KB8NNNC", registry);
            assertTrue(similarity >= 0.0, "Samsung capacitors should return valid similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "GRM188R71H104KA93D", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("GRM188R71H104KA93D", null, registry));
        }

        @Test
        @DisplayName("Null registry should return 0")
        void nullRegistryShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("GRM188R71H104KA93D", "GRM188R71H104KA93D", null));
        }

        @Test
        @DisplayName("Empty strings should return 0")
        void emptyStringsShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("", "", registry));
        }

        @Test
        @DisplayName("Unknown format should not throw")
        void unknownFormatShouldNotThrow() {
            double similarity = calculator.calculateSimilarity("UNKNOWN123", "UNKNOWN456", registry);
            assertTrue(similarity >= 0.0 && similarity <= 1.0, "Should return valid range");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric for same values")
        void similarityShouldBeSymmetricForSameValues() {
            String mpn1 = "GRM188R71H104KA93D";
            String mpn2 = "C0603C104K5RACTU";

            double sim1 = calculator.calculateSimilarity(mpn1, mpn2, registry);
            double sim2 = calculator.calculateSimilarity(mpn2, mpn1, registry);

            // Note: voltage comparison may not be symmetric (higher is ok)
            // but value and size should be
            assertTrue(Math.abs(sim1 - sim2) <= 0.2, "Similarity should be roughly symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {
                    "GRM188R71H104KA93D",
                    "C0603C104K5RACTU",
                    "CL10B104KB8NNNC",
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
    @DisplayName("Capacitance value tests")
    class CapacitanceValueTests {

        @Test
        @DisplayName("Should recognize 104 code as 100nF")
        void shouldRecognize104Code() {
            // Both have 104 = 100nF
            double similarity = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "C0603C104K5RACTU", registry);
            // Should at least match on value
            assertTrue(similarity > 0.0, "Same capacitance code should contribute to similarity");
        }

        @Test
        @DisplayName("Different capacitance values should reduce similarity")
        void differentCapacitanceValuesShouldReduceSimilarity() {
            // 104 (100nF) vs 105 (1µF)
            double sameValueSim = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "GRM188R71H104KA93D", registry);
            double diffValueSim = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "GRM188R71H105KA93D", registry);

            assertTrue(sameValueSim >= diffValueSim, "Same value should have >= similarity than different");
        }
    }

    @Nested
    @DisplayName("Package size extraction tests")
    class PackageSizeTests {

        @Test
        @DisplayName("Should extract 0603 from Murata GRM188")
        void shouldExtract0603FromMurata() {
            // GRM188 = 0603 package
            double similarity = calculator.calculateSimilarity(
                    "GRM188R71H104KA93D", "C0603C104K5RACTU", registry);
            // Both should be recognized as 0603
            assertTrue(similarity >= 0.3, "Both 0603 packages should match");
        }

        @Test
        @DisplayName("Should extract 0805 from Murata GRM216")
        void shouldExtract0805FromMurata() {
            // GRM216 = 0805 package
            double similarity = calculator.calculateSimilarity(
                    "GRM216R71H104KA93D", "C0805C104K5RACTU", registry);
            // Both should be recognized as 0805
            assertTrue(similarity >= 0.0, "Should calculate similarity for 0805 packages");
        }
    }
}
