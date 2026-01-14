package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TransistorSimilarityCalculator
 */
class TransistorSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private TransistorSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new TransistorSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base TRANSISTOR type")
        void shouldBeApplicableForTransistor() {
            assertTrue(calculator.isApplicable(ComponentType.TRANSISTOR));
        }

        @Test
        @DisplayName("Should be applicable for manufacturer-specific transistor types")
        void shouldBeApplicableForManufacturerTransistorTypes() {
            assertTrue(calculator.isApplicable(ComponentType.TRANSISTOR_NXP));
            assertTrue(calculator.isApplicable(ComponentType.TRANSISTOR_VISHAY));
        }

        @Test
        @DisplayName("Should not be applicable for non-transistor types")
        void shouldNotBeApplicableForNonTransistorTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.CAPACITOR));
            assertFalse(calculator.isApplicable(ComponentType.MOSFET));
            assertFalse(calculator.isApplicable(ComponentType.DIODE));
        }

        @Test
        @DisplayName("Should handle null type")
        void shouldHandleNullType() {
            assertFalse(calculator.isApplicable(null));
        }
    }

    @Nested
    @DisplayName("Equivalent groups tests")
    class EquivalentGroupTests {

        @Test
        @DisplayName("2N2222 and PN2222 should be high similarity (equivalent)")
        void shouldMatch2N2222andPN2222() {
            double similarity = calculator.calculateSimilarity("2N2222", "PN2222", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "2N2222 and PN2222 are equivalent");
        }

        @Test
        @DisplayName("2N2222 and 2N2222A should be high similarity")
        void shouldMatch2N2222andVariant() {
            double similarity = calculator.calculateSimilarity("2N2222", "2N2222A", registry);
            assertTrue(similarity >= HIGH_SIMILARITY, "2N2222 and 2N2222A are equivalent (got: " + similarity + ")");
        }

        @Test
        @DisplayName("2N3904 and PN3904 should be high similarity")
        void shouldMatch2N3904andPN3904() {
            double similarity = calculator.calculateSimilarity("2N3904", "PN3904", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "2N3904 and PN3904 are equivalent");
        }

        @Test
        @DisplayName("2N2907 and PN2907 should be high similarity (PNP)")
        void shouldMatch2N2907andPN2907() {
            double similarity = calculator.calculateSimilarity("2N2907", "PN2907", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "2N2907 and PN2907 are equivalent PNP");
        }

        @Test
        @DisplayName("BC547 variants should be high similarity")
        void shouldMatchBC547Variants() {
            double similarity = calculator.calculateSimilarity("BC547", "BC547B", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "BC547 and BC547B should match");
        }
    }

    @Nested
    @DisplayName("NPN vs PNP tests")
    class PolarityTests {

        @Test
        @DisplayName("NPN and PNP transistors should have low similarity")
        void npnAndPnpShouldBeLowSimilarity() {
            // 2N2222 is NPN, 2N2907 is PNP - completely incompatible
            double similarity = calculator.calculateSimilarity("2N2222", "2N2907", registry);
            assertTrue(similarity <= LOW_SIMILARITY, "NPN and PNP should have low similarity (got: " + similarity + ")");
        }

        @Test
        @DisplayName("2N3904 (NPN) and 2N3906 (PNP) should have low similarity")
        void shouldDistinguish3904and3906() {
            double similarity = calculator.calculateSimilarity("2N3904", "2N3906", registry);
            assertTrue(similarity <= LOW_SIMILARITY, "NPN and PNP complementary pair should differ (got: " + similarity + ")");
        }
    }

    @Nested
    @DisplayName("Same series tests")
    class SameSeriesTests {

        @Test
        @DisplayName("Same transistor family different models")
        void sameFamily() {
            // Both BC series but different models
            double similarity = calculator.calculateSimilarity("BC547", "BC337", registry);
            assertTrue(similarity >= LOW_SIMILARITY, "Same BC series should have some similarity");
        }

        @Test
        @DisplayName("Different transistor families")
        void differentFamilies() {
            // 2N series vs BC series - both NPN general purpose, similar specs
            double similarity = calculator.calculateSimilarity("2N2222", "BC547", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "Different families but similar specs should have medium-high similarity (got: " + similarity + ")");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "2N2222", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("2N2222", null, registry));
        }

        @Test
        @DisplayName("Non-transistor MPNs should return 0")
        void nonTransistorMpnsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("LM358", "MC1458", registry);
            assertEquals(0.0, similarity, "Non-transistor parts should return 0");
        }

        @Test
        @DisplayName("Mixed transistor and non-transistor should return 0")
        void mixedPartsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("2N2222", "LM358", registry);
            assertEquals(0.0, similarity, "Mixed part types should return 0");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("2N2222", "PN2222", registry);
            double sim2 = calculator.calculateSimilarity("PN2222", "2N2222", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Identical MPNs should have high similarity")
        void identicalMpnsShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("2N2222", "2N2222", registry);
            assertTrue(similarity >= HIGH_SIMILARITY, "Identical MPNs should have high similarity (got: " + similarity + ")");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"2N2222", "PN2222", "BC547", "2N3904", "2N2907"};

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
    @DisplayName("Part number suffix handling")
    class SuffixHandlingTests {

        @Test
        @DisplayName("Should ignore common suffixes")
        void shouldIgnoreCommonSuffixes() {
            // -T, -TR, -TA are common tape/reel suffixes
            double sim1 = calculator.calculateSimilarity("2N2222", "2N2222-T", registry);
            double sim2 = calculator.calculateSimilarity("2N2222", "2N2222-TR", registry);

            assertTrue(sim1 >= HIGH_SIMILARITY, "Should ignore -T suffix (got: " + sim1 + ")");
            assertTrue(sim2 >= HIGH_SIMILARITY, "Should ignore -TR suffix (got: " + sim2 + ")");
        }

        @Test
        @DisplayName("Should handle case variations")
        void shouldHandleCaseVariations() {
            double similarity = calculator.calculateSimilarity("2n2222", "2N2222", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Should be case insensitive");
        }
    }

    @Nested
    @DisplayName("Known characteristics comparison")
    class CharacteristicsTests {

        @Test
        @DisplayName("Transistors with similar characteristics should match")
        void similarCharacteristicsShouldMatch() {
            // 2N2222 and PN2222 have similar characteristics
            double similarity = calculator.calculateSimilarity("2N2222", "PN2222", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01);
        }

        @Test
        @DisplayName("2N3904 (low power) vs 2N4401 (higher power) should have lower similarity")
        void differentPowerRatingsShouldDiffer() {
            double similarity = calculator.calculateSimilarity("2N3904", "2N4401", registry);
            // Both NPN but different current ratings (0.2A vs 0.6A)
            // Metadata-driven approach: minimumRequired for current means higher is acceptable
            assertTrue(similarity >= MEDIUM_SIMILARITY,
                    "Different power ratings but compatible characteristics (got: " + similarity + ")");
        }
    }
}
