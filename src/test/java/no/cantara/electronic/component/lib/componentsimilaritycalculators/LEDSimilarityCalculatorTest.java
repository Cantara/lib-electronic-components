package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LEDSimilarityCalculator
 */
class LEDSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private LEDSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new LEDSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base LED type")
        void shouldBeApplicableForLed() {
            assertTrue(calculator.isApplicable(ComponentType.LED));
        }

        @Test
        @DisplayName("Should not be applicable for non-LED types")
        void shouldNotBeApplicableForNonLedTypes() {
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
    @DisplayName("Same LED tests")
    class SameLedTests {

        @Test
        @DisplayName("Exact same LED should have high similarity")
        void exactSameLedShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("TLHR5400", "TLHR5400", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same LED should have high similarity");
        }

        @Test
        @DisplayName("Same LED different bins should have high similarity")
        void sameLedDifferentBinsShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("TLHR5400", "TLHR5401", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same LED family should have high similarity");
        }
    }

    @Nested
    @DisplayName("LED family tests")
    class LedFamilyTests {

        @Test
        @DisplayName("TI TLHR series should match")
        void tiTlhrSeriesShouldMatch() {
            double similarity = calculator.calculateSimilarity("TLHR5400", "TLHR5402", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same TI series should match");
        }

        @Test
        @DisplayName("LG R series variants should match")
        void lgRSeriesVariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("LG R971", "LG R971-KN", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "LG R series variants should match");
        }

        @Test
        @DisplayName("Samsung LM series variants should match")
        void samsungLmSeriesVariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("LM301B", "LM301B-K", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "Samsung LM series variants should match");
        }
    }

    @Nested
    @DisplayName("Color temperature tests")
    class ColorTemperatureTests {

        @Test
        @DisplayName("Different color temperatures should have low similarity")
        void differentColorTemperaturesShouldHaveLowSimilarity() {
            // Assuming Cree color bin codes where K and C indicate different color temps
            double similarity = calculator.calculateSimilarity("XPERED-L1-FKA", "XPERED-L1-FCA", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different color temperatures should have low similarity");
        }

        @Test
        @DisplayName("Same color temperature should have high similarity")
        void sameColorTemperatureShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("XPERED-L1-FKA", "XPERED-L1-FKB", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same color temperature should have high similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "TLHR5400", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("TLHR5400", null, registry));
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
            double sim1 = calculator.calculateSimilarity("TLHR5400", "TLHR5401", registry);
            double sim2 = calculator.calculateSimilarity("TLHR5401", "TLHR5400", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"TLHR5400", "TLHR5401", "LG R971", "LM301B"};
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
    @DisplayName("Real-world LED tests")
    class RealWorldTests {

        @Test
        @DisplayName("Osram LED variants")
        void osramLedVariants() {
            double similarity = calculator.calculateSimilarity("LW E67C", "LCW E6SF", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "Osram LED variants should match");
        }

        @Test
        @DisplayName("Nichia LED variants")
        void nichiaLedVariants() {
            double similarity = calculator.calculateSimilarity("NCSW170", "NCSW170T", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "Nichia LED variants should match");
        }

        @Test
        @DisplayName("Lumileds LUXEON variants")
        void lumiledLuxeonVariants() {
            double similarity = calculator.calculateSimilarity("L130-5580", "L130-5580CT", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "Lumileds LUXEON variants should match");
        }
    }
}
