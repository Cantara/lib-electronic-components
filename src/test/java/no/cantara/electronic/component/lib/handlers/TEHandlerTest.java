package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.TEHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for TEHandler.
 * Tests terminal blocks, PCB headers, IDC connectors, and MATE-N-LOK.
 */
class TEHandlerTest {

    private static TEHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new TEHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Terminal Block Detection")
    class TerminalBlockTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "282836-2",
            "282836-3",
            "282837-4",
            "282837-6"
        })
        void shouldDetectTerminalBlocks(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "282836-2",
            "282837-4"
        })
        void shouldDetectTESpecificTerminalBlocks(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_TE, registry),
                    mpn + " should match CONNECTOR_TE");
        }
    }

    @Nested
    @DisplayName("PCB Header Detection")
    class PCBHeaderTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "1-284392-0",
            "2-284392-0"
        })
        void shouldDetectPCBHeaders(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }
    }

    @Nested
    @DisplayName("IDC Connector Detection")
    class IDCConnectorTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "640456-10",
            "640457-16"
        })
        void documentIDCConnectorDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
        }
    }

    @Nested
    @DisplayName("MATE-N-LOK Detection")
    class MateNLokTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "350211-1",
            "350211-2"
        })
        void documentMateNLokDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "282836-2, 2",
            "282836-3, 3",
            "282837-4, 4",
            "1-284392-0, 0"
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
            "282836-2, 282836",
            "282837-4, 282837",
            "1-284392-0, 1-284392"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetFamily() {
            assertEquals("Terminal Block", handler.getFamily("282836-2"));
            assertEquals("Terminal Block", handler.getFamily("282837-4"));
        }

        @Test
        void shouldGetPitch() {
            assertEquals("5.00", handler.getPitch("282836-2"));
            assertEquals("5.08", handler.getPitch("282837-4"));
        }

        @Test
        void shouldGetMountingType() {
            assertEquals("THT", handler.getMountingType("282836-1"));
            assertEquals("SMD", handler.getMountingType("282836-2"));
        }

        @Test
        void shouldDetermineGender() {
            assertEquals("Male", handler.getGender("282836-1"));
            assertEquals("Female", handler.getGender("282836-2"));
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
            assertFalse(handler.isOfficialReplacement("282836-2", "282837-2"));
        }

        @Test
        void documentSameSeriesVariants() {
            // Same series, different package variants may be compatible
            boolean isReplacement = handler.isOfficialReplacement("282836-2", "282836-3");
            System.out.println("282836-2 can replace 282836-3 = " + isReplacement);
        }
    }
}
