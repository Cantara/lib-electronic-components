package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.WurthHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for WurthHandler.
 * Tests pin headers, socket headers, and LEDs.
 */
class WurthHandlerTest {

    private static WurthHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new WurthHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Pin Header Detection")
    class PinHeaderTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "61300211121",
            "61300411121",
            "61300611121"
        })
        void documentPinHeaderDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesWurth = handler.matches(mpn, ComponentType.CONNECTOR_WURTH, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_WURTH = " + matchesWurth);
        }
    }

    @Nested
    @DisplayName("Socket Header Detection")
    class SocketHeaderTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "62200211121",
            "62200411121",
            "62200611121"
        })
        void documentSocketHeaderDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesWurth = handler.matches(mpn, ComponentType.CONNECTOR_WURTH, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_WURTH = " + matchesWurth);
        }
    }

    @Nested
    @DisplayName("WR-PHD Series Detection")
    class WRPHDSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "61301011121",
            "61302011121"
        })
        void documentWRPHDDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
        }
    }

    @Nested
    @DisplayName("WR-BHD Series Detection")
    class WRBHDSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "61401011121",
            "61402011121"
        })
        void documentWRBHDDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
        }
    }

    @Nested
    @DisplayName("WR-TBL Series Detection")
    class WRTBLSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "61501011121",
            "61502011121"
        })
        void documentWRTBLDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "61300211121, 1",
            "61300411122, 2",
            "61300611123, 3"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @Test
        void shouldReturnEmptyForNonMatchingMPN() {
            assertEquals("", handler.extractPackageCode("INVALID123"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "61300211121, 61300",
            "61300411121, 61300",
            "61301011121, 61301",
            "62200211121, 62200",
            "62200411121, 62200"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldAllowSameSeriesPinCountPitchVariants() {
            // Same series, pin count, and pitch but different variant should be replaceable
            assertTrue(handler.isOfficialReplacement("61300211121", "61300211122"));
        }

        @Test
        void shouldNotReplaceAcrossDifferentPinCounts() {
            // Same series but different pin count should not be replaceable
            assertFalse(handler.isOfficialReplacement("61300211121", "61300411121"));
        }

        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("61300211121", "62200211121"));
        }
    }

    @Nested
    @DisplayName("MPN Structure Parsing")
    class MPNStructureTests {
        @Test
        void documentMPNStructure() {
            // MPN: 61300211121
            // Structure: 6XXXX YY ZZZ V
            // - 61300 = Series code (5 digits)
            // - 02 = Pin count (2 digits)
            // - 112 = Pitch code (3 digits)
            // - 1 = Variant code (1 digit)
            String mpn = "61300211121";
            System.out.println("MPN: " + mpn);
            System.out.println("Series: " + handler.extractSeries(mpn));
            System.out.println("Package/Variant: " + handler.extractPackageCode(mpn));
        }
    }
}
