package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OpAmpSimilarityCalculator
 */
class OpAmpSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.5;

    private OpAmpSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new OpAmpSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base OPAMP type")
        void shouldBeApplicableForOpAmp() {
            assertTrue(calculator.isApplicable(ComponentType.OPAMP));
        }

        @Test
        @DisplayName("Should be applicable for manufacturer-specific op-amp types")
        void shouldBeApplicableForManufacturerOpAmpTypes() {
            assertTrue(calculator.isApplicable(ComponentType.OPAMP_TI));
        }

        @Test
        @DisplayName("Should be applicable for generic IC types")
        void shouldBeApplicableForICTypes() {
            assertTrue(calculator.isApplicable(ComponentType.IC));
            assertTrue(calculator.isApplicable(ComponentType.ANALOG_IC));
        }

        @Test
        @DisplayName("Should not be applicable for non-op-amp types")
        void shouldNotBeApplicableForNonOpAmpTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.TRANSISTOR));
            assertFalse(calculator.isApplicable(ComponentType.MOSFET));
        }

        @Test
        @DisplayName("Should handle null type")
        void shouldHandleNullType() {
            assertFalse(calculator.isApplicable(null));
        }
    }

    @Nested
    @DisplayName("Equivalent families tests")
    class EquivalentFamiliesTests {

        @Test
        @DisplayName("LM358 and MC1458 should be high similarity (dual op-amps)")
        void shouldMatchLM358andMC1458() {
            double similarity = calculator.calculateSimilarity("LM358", "MC1458", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "LM358 and MC1458 are equivalent duals");
        }

        @Test
        @DisplayName("LM358 variants should match")
        void shouldMatchLM358Variants() {
            double similarity = calculator.calculateSimilarity("LM358", "LM358N", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("TL072 and LM358 should match (both dual)")
        void shouldMatchTL072andLM358() {
            double similarity = calculator.calculateSimilarity("TL072", "LM358", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Both are dual op-amps");
        }

        @Test
        @DisplayName("LM324 and TL074 should match (both quad)")
        void shouldMatchLM324andTL074() {
            double similarity = calculator.calculateSimilarity("LM324", "TL074", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Both are quad op-amps");
        }
    }

    @Nested
    @DisplayName("Function matching tests")
    class FunctionTests {

        @Test
        @DisplayName("Dual op-amps should match other dual op-amps")
        void dualOpAmpsShouldMatch() {
            // All dual op-amps
            double similarity = calculator.calculateSimilarity("LM358", "NE5532", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "Dual op-amps should have good similarity");
        }

        @Test
        @DisplayName("Quad op-amps should match other quad op-amps")
        void quadOpAmpsShouldMatch() {
            double similarity = calculator.calculateSimilarity("LM324", "TL074", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("Dual vs Quad op-amps should have lower similarity")
        void dualVsQuadShouldDiffer() {
            double dualDualSim = calculator.calculateSimilarity("LM358", "MC1458", registry);
            double dualQuadSim = calculator.calculateSimilarity("LM358", "LM324", registry);

            assertTrue(dualDualSim > dualQuadSim,
                    "Dual-Dual should have higher similarity than Dual-Quad");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "LM358", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("LM358", null, registry));
        }

        @Test
        @DisplayName("Non-op-amp MPNs should return 0")
        void nonOpAmpMpnsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("2N2222", "BC547", registry);
            assertEquals(0.0, similarity, "Non-op-amp parts should return 0");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("LM358", "MC1458", registry);
            double sim2 = calculator.calculateSimilarity("MC1458", "LM358", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Identical MPNs should have high similarity")
        void identicalMpnsShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("LM358", "LM358", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"LM358", "MC1458", "TL072", "LM324", "TL074"};

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
    @DisplayName("Package code handling")
    class PackageCodeTests {

        @Test
        @DisplayName("Same op-amp different package codes should have high similarity")
        void sameOpAmpDifferentPackagesShouldMatch() {
            // LM358N (DIP) vs LM358D (SOIC)
            double similarity = calculator.calculateSimilarity("LM358N", "LM358D", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same part different packages should match");
        }

        @Test
        @DisplayName("Should extract and compare package codes")
        void shouldExtractPackageCodes() {
            // Both DIP packages (N suffix)
            double similarity = calculator.calculateSimilarity("LM358N", "MC1458N", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }
    }
}
