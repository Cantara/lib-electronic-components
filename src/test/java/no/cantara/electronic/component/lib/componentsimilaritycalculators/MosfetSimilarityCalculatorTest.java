package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MosfetSimilarityCalculator
 */
class MosfetSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private MosfetSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new MosfetSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base MOSFET type")
        void shouldBeApplicableForMosfet() {
            assertTrue(calculator.isApplicable(ComponentType.MOSFET));
        }

        @Test
        @DisplayName("Should be applicable for manufacturer-specific MOSFET types")
        void shouldBeApplicableForManufacturerMosfetTypes() {
            assertTrue(calculator.isApplicable(ComponentType.MOSFET_INFINEON));
            assertTrue(calculator.isApplicable(ComponentType.MOSFET_ST));
            assertTrue(calculator.isApplicable(ComponentType.MOSFET_NXP));
        }

        @Test
        @DisplayName("Should not be applicable for non-MOSFET types")
        void shouldNotBeApplicableForNonMosfetTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.TRANSISTOR));
            assertFalse(calculator.isApplicable(ComponentType.DIODE));
        }

        @Test
        @DisplayName("Should handle null type - returns true for MOSFET detection")
        void shouldHandleNullType() {
            // MosfetSimilarityCalculator returns true for null to handle unrecognized MOSFETs
            assertTrue(calculator.isApplicable(null));
        }
    }

    @Nested
    @DisplayName("Equivalent groups tests")
    class EquivalentGroupTests {

        @Test
        @DisplayName("IRF530 and STF530 should be high similarity (equivalent)")
        void shouldMatchIRF530andSTF530() {
            double similarity = calculator.calculateSimilarity("IRF530", "STF530", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "IRF530 and STF530 are equivalent");
        }

        @Test
        @DisplayName("IRF530 and IRF530N should be high similarity")
        void shouldMatchIRF530andVariant() {
            double similarity = calculator.calculateSimilarity("IRF530", "IRF530N", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "IRF530 and IRF530N are equivalent");
        }

        @Test
        @DisplayName("IRF540 equivalents should match")
        void shouldMatchIRF540Equivalents() {
            double similarity = calculator.calculateSimilarity("IRF540", "STF540", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }
    }

    @Nested
    @DisplayName("N-Channel vs P-Channel tests")
    class PolarityTests {

        @Test
        @DisplayName("N-Channel and P-Channel MOSFETs should have low similarity")
        void nChannelAndPChannelShouldBeLowSimilarity() {
            // IRF530 is N-channel, IRF9530 is P-channel
            double similarity = calculator.calculateSimilarity("IRF530", "IRF9530", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "N and P channel should have low similarity");
        }

        @Test
        @DisplayName("Same N-Channel MOSFETs should have high similarity")
        void sameNChannelShouldMatch() {
            double similarity = calculator.calculateSimilarity("IRF530", "IRF530", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }
    }

    @Nested
    @DisplayName("Characteristics comparison tests")
    class CharacteristicsTests {

        @Test
        @DisplayName("MOSFETs with similar characteristics should match")
        void similarCharacteristicsShouldMatch() {
            // IRF530N and STF530N have similar specs
            double similarity = calculator.calculateSimilarity("IRF530N", "STF530N", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("Different power rating MOSFETs should have lower similarity")
        void differentPowerRatingsShouldDiffer() {
            // IRF530 (14A) vs IRF540 (28A)
            double similarity = calculator.calculateSimilarity("IRF530", "IRF540", registry);
            assertTrue(similarity < HIGH_SIMILARITY,
                    "Different current ratings should have lower similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "IRF530", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("IRF530", null, registry));
        }

        @Test
        @DisplayName("Non-MOSFET MPNs should return 0")
        void nonMosfetMpnsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("LM358", "LM324", registry);
            assertEquals(0.0, similarity, "Non-MOSFET parts should return 0");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("IRF530", "STF530", registry);
            double sim2 = calculator.calculateSimilarity("STF530", "IRF530", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Identical MPNs should have high similarity")
        void identicalMpnsShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("IRF530", "IRF530", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"IRF530", "IRF530N", "STF530", "IRF540", "IRF9530"};

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
    @DisplayName("Package suffix handling")
    class SuffixHandlingTests {

        @Test
        @DisplayName("Should handle N suffix correctly")
        void shouldHandleNSuffix() {
            double similarity = calculator.calculateSimilarity("IRF530", "IRF530N", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "N suffix variants should match");
        }

        @Test
        @DisplayName("Should handle different manufacturer prefixes")
        void shouldHandleDifferentPrefixes() {
            double similarity = calculator.calculateSimilarity("IRF530", "STF530", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Different manufacturer prefixes should match");
        }
    }
}
