package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LogicICSimilarityCalculator
 */
class LogicICSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.5;
    private static final double LOW_SIMILARITY = 0.3;

    private LogicICSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new LogicICSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base LOGIC_IC type")
        void shouldBeApplicableForLogicIc() {
            assertTrue(calculator.isApplicable(ComponentType.LOGIC_IC));
        }

        @Test
        @DisplayName("Should be applicable for IC type")
        void shouldBeApplicableForIcType() {
            assertTrue(calculator.isApplicable(ComponentType.IC));
        }

        @Test
        @DisplayName("Should be applicable for manufacturer-specific logic IC types")
        void shouldBeApplicableForManufacturerLogicIcTypes() {
            assertTrue(calculator.isApplicable(ComponentType.LOGIC_IC_NEXPERIA));
            assertTrue(calculator.isApplicable(ComponentType.LOGIC_IC_DIODES));
        }

        @Test
        @DisplayName("Should not be applicable for non-logic-IC types")
        void shouldNotBeApplicableForNonLogicIcTypes() {
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
    @DisplayName("CD4000 series tests")
    class Cd4000SeriesTests {

        @Test
        @DisplayName("Same CD4001 should have high similarity")
        void sameCd4001ShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("CD4001BE", "CD4001BE", registry);
            assertTrue(similarity >= HIGH_SIMILARITY,
                    "Same IC - Expected >= " + HIGH_SIMILARITY + " but was: " + similarity);
        }

        @Test
        @DisplayName("CD4001 with different package codes should have high similarity")
        void cd4001DifferentPackagesShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("CD4001BE", "CD4001BM", registry);
            assertTrue(similarity >= HIGH_SIMILARITY,
                    "Same IC different package - Expected >= " + HIGH_SIMILARITY + " but was: " + similarity);
        }

        @Test
        @DisplayName("Different CD4000 ICs should have low similarity")
        void differentCd4000IcsShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("CD4001BE", "CD4011BE", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different CD4000 ICs should have low similarity");
        }
    }

    @Nested
    @DisplayName("74xx series tests")
    class Series74xxTests {

        @Test
        @DisplayName("Same 74 series with compatible technologies should match")
        void same74SeriesCompatibleTechnologiesShouldMatch() {
            double similarity = calculator.calculateSimilarity("74LS00", "74HC00", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Compatible technologies should match");
        }

        @Test
        @DisplayName("Same function different technology should match")
        void sameFunctionDifferentTechnologyShouldMatch() {
            double similarity = calculator.calculateSimilarity("74LS04", "74HC04", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same function different technology should match");
        }

        @Test
        @DisplayName("Different functions should have low similarity")
        void differentFunctionsShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("74LS00", "74LS74", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different functions should have low similarity");
        }
    }

    @Nested
    @DisplayName("Function group tests")
    class FunctionGroupTests {

        @Test
        @DisplayName("NAND gates in same group should match")
        void nandGatesInSameGroupShouldMatch() {
            double similarity = calculator.calculateSimilarity("74LS00", "74HC00", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "NAND gates should match");
        }

        @Test
        @DisplayName("Inverters in same group should match")
        void invertersInSameGroupShouldMatch() {
            double similarity = calculator.calculateSimilarity("74LS04", "74ALS04", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Inverters should match");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "74LS00", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("74LS00", null, registry));
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
            double sim1 = calculator.calculateSimilarity("74LS00", "74HC00", registry);
            double sim2 = calculator.calculateSimilarity("74HC00", "74LS00", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"74LS00", "74HC04", "CD4001BE", "CD4011BE"};
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
    @DisplayName("Technology compatibility tests")
    class TechnologyCompatibilityTests {

        @Test
        @DisplayName("LS and ALS should be compatible")
        void lsAndAlsShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("74LS04", "74ALS04", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "LS and ALS should be compatible");
        }

        @Test
        @DisplayName("HC and HCT should be compatible")
        void hcAndHctShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("74HC04", "74HCT04", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "HC and HCT should be compatible");
        }

        @Test
        @DisplayName("F and LS should be compatible")
        void fAndLsShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("74F04", "74LS04", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "F and LS should be compatible");
        }
    }
}
