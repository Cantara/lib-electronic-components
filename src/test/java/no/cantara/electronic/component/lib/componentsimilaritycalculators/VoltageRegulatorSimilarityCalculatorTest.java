package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VoltageRegulatorSimilarityCalculator
 */
class VoltageRegulatorSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private VoltageRegulatorSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new VoltageRegulatorSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base VOLTAGE_REGULATOR type")
        void shouldBeApplicableForVoltageRegulator() {
            assertTrue(calculator.isApplicable(ComponentType.VOLTAGE_REGULATOR));
        }

        @Test
        @DisplayName("Should not be applicable for non-voltage-regulator types")
        void shouldNotBeApplicableForNonVoltageRegulatorTypes() {
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
    @DisplayName("Fixed voltage regulator tests (78xx/79xx)")
    class FixedVoltageRegulatorTests {

        @Test
        @DisplayName("Same 7805 with different packages should have high similarity")
        void same7805DifferentPackagesShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("LM7805CT", "LM7805T", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same regulator different package should have high similarity");
        }

        @Test
        @DisplayName("Same voltage 7805 from different manufacturers should match")
        void sameVoltage7805FromDifferentManufacturersShouldMatch() {
            double similarity = calculator.calculateSimilarity("LM7805", "MC7805", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same voltage regulators should match");
        }

        @Test
        @DisplayName("Different voltage 7805 vs 7812 should have low similarity")
        void differentVoltage7805vs7812ShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("LM7805", "LM7812", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different voltage should have low similarity");
        }

        @Test
        @DisplayName("Positive vs negative regulators should have low similarity")
        void positiveVsNegativeRegulatorsShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("LM7805", "LM7905", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Positive vs negative should have low similarity");
        }

        @Test
        @DisplayName("7815 variants should match")
        void voltage7815VariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("LM7815CT", "MC7815CT", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same voltage regulators should match");
        }
    }

    @Nested
    @DisplayName("Adjustable voltage regulator tests")
    class AdjustableVoltageRegulatorTests {

        @Test
        @DisplayName("Same LM317 with different packages should have high similarity")
        void sameLm317DifferentPackagesShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("LM317T", "LM317K", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same adjustable regulator should have high similarity");
        }

        @Test
        @DisplayName("LM317 and LM350 should be compatible (positive adjustable)")
        void lm317AndLm350ShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("LM317", "LM350", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Positive adjustable regulators should be compatible");
        }

        @Test
        @DisplayName("LM317 and LM338 should be compatible (positive adjustable)")
        void lm317AndLm338ShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("LM317", "LM338", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Positive adjustable regulators should be compatible");
        }

        @Test
        @DisplayName("LM317 (positive) and LM337 (negative) should have low similarity")
        void lm317AndLm337ShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("LM317", "LM337", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Positive and negative adjustable should have low similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "LM7805", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("LM7805", null, registry));
        }

        @Test
        @DisplayName("Non-voltage-regulator MPNs should return 0")
        void nonVoltageRegulatorMpnsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("LM358", "LM324", registry);
            assertEquals(0.0, similarity, "Non-voltage-regulator parts should return 0");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("LM7805", "MC7805", registry);
            double sim2 = calculator.calculateSimilarity("MC7805", "LM7805", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"LM7805", "LM7812", "LM317", "LM337"};
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
    @DisplayName("Package code tests")
    class PackageCodeTests {

        @Test
        @DisplayName("TO-220 package variants should be compatible")
        void to220PackageVariantsShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("LM7805CT", "LM7805T", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "TO-220 variants should be compatible");
        }

        @Test
        @DisplayName("Different packages same regulator should have high similarity")
        void differentPackagesSameRegulatorShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("LM7805T", "LM7805MP", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Different packages should still have high similarity");
        }
    }
}
