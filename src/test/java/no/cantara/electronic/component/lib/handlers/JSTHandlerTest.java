package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.JSTHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for JSTHandler.
 * Tests PH, XH, SH, GH, ZH, and EH series JST connectors.
 */
class JSTHandlerTest {

    private static JSTHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new JSTHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("PH Series Detection (2.0mm pitch)")
    class PHSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PHR-2",
            "PHR-3",
            "PHR-4",
            "PHR-6",
            "PHR-8",
            "PH-2",
            "PHS-3",
            "PHD-4",
            "PHL-6"
        })
        void documentPHSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesJST = handler.matches(mpn, ComponentType.CONNECTOR_JST, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_JST = " + matchesJST);
        }
    }

    @Nested
    @DisplayName("XH Series Detection (2.5mm pitch)")
    class XHSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "XHR-2",
            "XHR-3",
            "XHR-4",
            "XHR-6",
            "XH-2",
            "XHS-3",
            "XHD-4",
            "XHL-6"
        })
        void documentXHSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesJST = handler.matches(mpn, ComponentType.CONNECTOR_JST, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_JST = " + matchesJST);
        }
    }

    @Nested
    @DisplayName("SH Series Detection (1.0mm pitch)")
    class SHSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "SHR-2",
            "SHR-3",
            "SHR-4",
            "SH-2",
            "SHS-3",
            "SHD-4"
        })
        void documentSHSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesJST = handler.matches(mpn, ComponentType.CONNECTOR_JST, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_JST = " + matchesJST);
        }
    }

    @Nested
    @DisplayName("GH Series Detection (1.25mm pitch)")
    class GHSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GHR-2",
            "GHR-3",
            "GHR-4",
            "GH-2",
            "GHS-3",
            "GHD-4"
        })
        void documentGHSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesJST = handler.matches(mpn, ComponentType.CONNECTOR_JST, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_JST = " + matchesJST);
        }
    }

    @Nested
    @DisplayName("ZH Series Detection (1.5mm pitch)")
    class ZHSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ZHR-2",
            "ZHR-3",
            "ZHR-4",
            "ZH-2",
            "ZH-3"
        })
        void documentZHSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesJST = handler.matches(mpn, ComponentType.CONNECTOR_JST, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_JST = " + matchesJST);
        }
    }

    @Nested
    @DisplayName("EH Series Detection (2.5mm pitch)")
    class EHSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EHR-2",
            "EHR-3",
            "EHR-4",
            "EH-2",
            "EH-3"
        })
        void documentEHSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesJST = handler.matches(mpn, ComponentType.CONNECTOR_JST, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_JST = " + matchesJST);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "PHR-2-V, V",
            "PHR-3-R, R",
            "XHR-4-VS, VS",
            "XHR-6-RS, RS",
            "SHR-2-S, S",
            "GHR-3-W, W"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @Test
        void shouldReturnEmptyForNoSuffix() {
            assertEquals("", handler.extractPackageCode("PHR-2"));
            assertEquals("", handler.extractPackageCode("XHR-4"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "PH-3, PH",
            "XH-6, XH",
            "SH-3, SH",
            "GH-2, GH",
            "ZH-4, ZH",
            "EH-3, EH"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "PHR-2",
            "XHR-4",
            "SHR-2",
            "GHR-4",
            "ZHR-3",
            "EHR-2"
        })
        void documentSeriesExtractionForRVariants(String mpn) {
            String series = handler.extractSeries(mpn);
            System.out.println(mpn + " extracted series = " + series);
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetPitch() {
            assertEquals("2.00", handler.getPitch("PHR-2"));
            assertEquals("2.00", handler.getPitch("PH-3"));
            assertEquals("2.50", handler.getPitch("XHR-4"));
            assertEquals("2.50", handler.getPitch("XH-6"));
            assertEquals("1.00", handler.getPitch("SHR-2"));
            assertEquals("1.00", handler.getPitch("SH-3"));
            assertEquals("1.25", handler.getPitch("GHR-4"));
            assertEquals("1.25", handler.getPitch("GH-2"));
            assertEquals("1.50", handler.getPitch("ZHR-3"));
            assertEquals("1.50", handler.getPitch("ZH-4"));
            assertEquals("2.50", handler.getPitch("EHR-2"));
            assertEquals("2.50", handler.getPitch("EH-3"));
        }

        @Test
        void shouldGetMountingType() {
            assertEquals("THT", handler.getMountingType("PHR-2"));
            assertEquals("THT", handler.getMountingType("PHR-2-V"));
            assertEquals("SMT", handler.getMountingType("PHR-2-S"));
            assertEquals("SMT", handler.getMountingType("PHR-2-VS"));
        }

        @Test
        void shouldGetOrientation() {
            assertEquals("Vertical", handler.getOrientation("PHR-2"));
            assertEquals("Vertical", handler.getOrientation("PHR-2-V"));
            assertEquals("Right Angle", handler.getOrientation("PHR-2-R"));
            assertEquals("Right Angle", handler.getOrientation("PHR-2-RS"));
        }

        @Test
        void shouldGetRatedCurrent() {
            assertEquals(3.0, handler.getRatedCurrent("PHR-2"));
            assertEquals(3.0, handler.getRatedCurrent("XHR-4"));
            assertEquals(1.0, handler.getRatedCurrent("SHR-2"));
            assertEquals(1.0, handler.getRatedCurrent("GHR-3"));
            assertEquals(1.0, handler.getRatedCurrent("ZHR-4"));
            assertEquals(3.0, handler.getRatedCurrent("EHR-2"));
        }

        @Test
        void shouldDetermineGenderForMaleConnectors() {
            assertEquals("Male", handler.getGender("PH-2"));
            assertEquals("Male", handler.getGender("XH-4"));
            assertEquals("Male", handler.getGender("SH-2"));
            assertEquals("Male", handler.getGender("GH-3"));
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "PHR-2",
            "XHR-4",
            "SHR-2",
            "GHR-3"
        })
        void documentGenderForRVariants(String mpn) {
            String gender = handler.getGender(mpn);
            System.out.println(mpn + " gender = " + gender);
        }

        @Test
        void shouldAlwaysBeKeyed() {
            assertTrue(handler.isKeyed("PHR-2"));
            assertTrue(handler.isKeyed("XHR-4"));
            assertTrue(handler.isKeyed("SHR-2"));
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

        @Test
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.extractSeries("ABC-123"));
            assertEquals("", handler.getPitch("ABC-123"));
            assertEquals(0.0, handler.getRatedCurrent("ABC-123"));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("PHR-2", "XHR-2"));
            assertFalse(handler.isOfficialReplacement("SHR-4", "GHR-4"));
        }

        @Test
        void shouldNotReplaceWithDifferentPinCount() {
            assertFalse(handler.isOfficialReplacement("PHR-2", "PHR-4"));
            assertFalse(handler.isOfficialReplacement("XHR-3", "XHR-6"));
        }

        @Test
        void documentCompatibleMountingTypes() {
            // Same series, same pin count, different mounting variants may be compatible
            boolean verticalTHTReplacement = handler.isOfficialReplacement("PHR-2-V", "PHR-2");
            System.out.println("PHR-2-V can replace PHR-2 = " + verticalTHTReplacement);

            boolean samePackageReplacement = handler.isOfficialReplacement("PHR-2-V", "PHR-2-V");
            System.out.println("PHR-2-V can replace PHR-2-V = " + samePackageReplacement);
        }
    }
}
