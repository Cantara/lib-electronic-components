package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DiodeSimilarityCalculator
 */
class DiodeSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private DiodeSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new DiodeSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base DIODE type")
        void shouldBeApplicableForDiode() {
            assertTrue(calculator.isApplicable(ComponentType.DIODE));
        }

        @Test
        @DisplayName("Should not be applicable for non-diode types")
        void shouldNotBeApplicableForNonDiodeTypes() {
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
    @DisplayName("Signal diode tests")
    class SignalDiodeTests {

        @Test
        @DisplayName("1N4148 and 1N914 should be high similarity (equivalent)")
        void shouldMatch1N4148and1N914() {
            double similarity = calculator.calculateSimilarity("1N4148", "1N914", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "1N4148 and 1N914 are equivalent");
        }

        @Test
        @DisplayName("Same signal diode should have high similarity")
        void sameSignalDiodeShouldMatch() {
            double similarity = calculator.calculateSimilarity("1N4148", "1N4148", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }
    }

    @Nested
    @DisplayName("Rectifier diode tests")
    class RectifierDiodeTests {

        @Test
        @DisplayName("1N4007 and RL207 should be high similarity (equivalent)")
        void shouldMatch1N4007andRL207() {
            double similarity = calculator.calculateSimilarity("1N4007", "RL207", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "1N4007 and RL207 are 1000V equivalents");
        }

        @Test
        @DisplayName("1N4001 and RL201 should be high similarity")
        void shouldMatch1N4001andRL201() {
            double similarity = calculator.calculateSimilarity("1N4001", "RL201", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "1N4001 and RL201 are 50V equivalents");
        }

        @Test
        @DisplayName("1N400x series diodes in same family")
        void shouldMatch1N400xFamily() {
            // All 1N400x are in the same family
            double similarity = calculator.calculateSimilarity("1N4001", "1N4007", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same family should be high similarity");
        }

        @Test
        @DisplayName("Different voltage ratings should still be in same family")
        void differentVoltageRatingsSameFamily() {
            // 1N4001 is 50V, 1N4004 is 400V
            double similarity = calculator.calculateSimilarity("1N4001", "1N4004", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "Same family different voltage should match");
        }
    }

    @Nested
    @DisplayName("Zener diode tests")
    class ZenerDiodeTests {

        @Test
        @DisplayName("Same voltage Zeners should have high similarity")
        void sameVoltageZenersShouldMatch() {
            // Both are 5.1V Zeners
            double similarity = calculator.calculateSimilarity("1N4733", "1N4733", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("Different voltage Zeners should have low similarity")
        void differentVoltageZenersShouldDiffer() {
            // 1N4733 is 5.1V, 1N4742 is 12V
            double similarity = calculator.calculateSimilarity("1N4733", "1N4742", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different voltage Zeners should have low similarity");
        }
    }

    @Nested
    @DisplayName("Schottky diode tests")
    class SchottkyDiodeTests {

        @Test
        @DisplayName("Same Schottky diode should have high similarity")
        void sameSchottkyDiodeShouldMatch() {
            double similarity = calculator.calculateSimilarity("BAT54", "BAT54", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("BAT54 variants should match")
        void bat54VariantsShouldMatch() {
            // BAT54S is a dual series version of BAT54
            double similarity = calculator.calculateSimilarity("BAT54", "BAT54S", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "BAT54 variants should match");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "1N4148", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("1N4148", null, registry));
        }

        @Test
        @DisplayName("Non-diode MPNs should return 0")
        void nonDiodeMpnsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("LM358", "LM324", registry);
            assertEquals(0.0, similarity, "Non-diode parts should return 0");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("1N4148", "1N914", registry);
            double sim2 = calculator.calculateSimilarity("1N914", "1N4148", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"1N4148", "1N914", "1N4007", "BAT54", "1N4733"};

            for (String mpn1 : testMpns) {
                for (String mpn2 : testMpns) {
                    double sim = calculator.calculateSimilarity(mpn1, mpn2, registry);
                    assertTrue(sim >= 0.0 && sim <= 1.0,
                            String.format("Similarity for %s vs %s should be in [0,1], was %f", mpn1, mpn2, sim));
                }
            }
        }
    }
}
