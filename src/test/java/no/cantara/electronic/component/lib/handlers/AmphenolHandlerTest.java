package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AmphenolHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for AmphenolHandler.
 * Tests Mini-PV, HD20, SFP+, USB 3.0, and various industrial connectors.
 */
class AmphenolHandlerTest {

    private static AmphenolHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AmphenolHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Mini-PV Series Detection")
    class MiniPVTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "504182-0210",
            "504182-0410",
            "505478-0210",
            "505478-0410"
        })
        void documentMiniPVDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_AMPHENOL = " + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("HD20 Series Detection")
    class HD20Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "10120843-0210",
            "10120843-0410",
            "10120855-0210",
            "10120855-0410"
        })
        void documentHD20Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_AMPHENOL = " + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("SFP+ Cage Detection")
    class SFPTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "RJHSE-5381-01",
            "RJHSE-5382-02",
            "RJHSE-5384-04"
        })
        void documentSFPDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_AMPHENOL = " + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("USB 3.0 Connector Detection")
    class USB30Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "USB3-A-01-S101",
            "USB3-A-02-S201"
        })
        void documentUSB30Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_AMPHENOL = " + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("Additional Series Detection")
    class AdditionalSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "10051922-0210",
            "10151980-0410",
            "10129378-0310",
            "RJMG2310-01"
        })
        void documentAdditionalSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_AMPHENOL = " + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "504182-0210, 0210",
            "504182-0410, 0410",
            "10120843-0210, 0210",
            "RJHSE-5381-01, 01"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "504182-0210, 504182",
            "505478-0410, 505478",
            "10120843-0210, 10120843",
            "10120855-0410, 10120855",
            "RJHSE-5381-01, RJHSE"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetPitch() {
            assertEquals("2.00", handler.getPitch("504182-0210"));
            assertEquals("2.00", handler.getPitch("10120843-0210"));
            assertEquals("3.00", handler.getPitch("10051922-0210"));
            assertEquals("2.54", handler.getPitch("10151980-0410"));
        }

        @Test
        void shouldGetMountingType() {
            assertEquals("THT", handler.getMountingType("504182-0210"));
            assertEquals("SMT", handler.getMountingType("505478-0210"));
            assertEquals("THT", handler.getMountingType("10120843-0210"));
            assertEquals("SMT", handler.getMountingType("10120855-0210"));
        }

        @Test
        void shouldGetRatedCurrent() {
            assertEquals(3.0, handler.getRatedCurrent("504182-0210"));
            assertEquals(5.0, handler.getRatedCurrent("10051922-0210"));
        }

        @Test
        void shouldDetermineShielded() {
            assertTrue(handler.isShielded("RJHSE-5381-01"));
            assertTrue(handler.isShielded("USB3-A-01-S101"));
            assertFalse(handler.isShielded("504182-0210"));
        }

        @Test
        void shouldGetApplicationType() {
            assertEquals("SFP/SFP+", handler.getApplicationType("RJHSE-5381-01"));
            assertEquals("USB 3.0", handler.getApplicationType("USB3-A-01-S101"));
            assertEquals("RJ45", handler.getApplicationType("RJMG2310-01"));
            assertEquals("Power", handler.getApplicationType("10051922-0210"));
            assertEquals("Signal", handler.getApplicationType("504182-0210"));
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
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("504182-0210", "505478-0210"));
        }

        @Test
        void documentCompatibleMountingTypes() {
            // Same series, different mounting variants may be compatible
            boolean isReplacement = handler.isOfficialReplacement("504182-0210", "504182-0410");
            System.out.println("504182-0210 can replace 504182-0410 = " + isReplacement);
        }
    }
}
