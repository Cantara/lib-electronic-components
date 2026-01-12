package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConnectorSimilarityCalculator
 */
class ConnectorSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.5;
    private static final double LOW_SIMILARITY = 0.3;

    private ConnectorSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new ConnectorSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base CONNECTOR type")
        void shouldBeApplicableForConnector() {
            assertTrue(calculator.isApplicable(ComponentType.CONNECTOR));
        }

        @Test
        @DisplayName("Should be applicable for manufacturer-specific connector types")
        void shouldBeApplicableForManufacturerConnectorTypes() {
            assertTrue(calculator.isApplicable(ComponentType.CONNECTOR_MOLEX));
            assertTrue(calculator.isApplicable(ComponentType.CONNECTOR_TE));
            assertTrue(calculator.isApplicable(ComponentType.CONNECTOR_JST));
            assertTrue(calculator.isApplicable(ComponentType.CONNECTOR_HIROSE));
            assertTrue(calculator.isApplicable(ComponentType.CONNECTOR_AMPHENOL));
            assertTrue(calculator.isApplicable(ComponentType.CONNECTOR_HARWIN));
        }

        @Test
        @DisplayName("Should not be applicable for non-connector types")
        void shouldNotBeApplicableForNonConnectorTypes() {
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
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "1-292161-0", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("1-292161-0", null, registry));
        }

        @Test
        @DisplayName("Null registry should return 0")
        void nullRegistryShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("1-292161-0", "1-292161-0", null));
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
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"1-292161-0", "1-292161-2", "0039012020"};
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
