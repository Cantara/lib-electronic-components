package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.TEHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for TEHandler (TE Connectivity).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
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
    @DisplayName("Terminal Block Detection - Documentation Tests")
    class TerminalBlockTests {

        @ParameterizedTest
        @DisplayName("Document terminal block detection")
        @ValueSource(strings = {"282837-2", "282836-4", "282837-8"})
        void documentTerminalBlockDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesTE = handler.matches(mpn, ComponentType.CONNECTOR_TE, registry);
            System.out.println("Terminal block detection: " + mpn + " CONNECTOR=" + matchesConnector + " TE=" + matchesTE);
        }
    }

    @Nested
    @DisplayName("PCB Header Detection - Documentation Tests")
    class PCBHeaderTests {

        @ParameterizedTest
        @DisplayName("Document PCB header detection")
        @ValueSource(strings = {"5-826950-2", "5-103635-8"})
        void documentPCBHeaderDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesTE = handler.matches(mpn, ComponentType.CONNECTOR_TE, registry);
            System.out.println("PCB header detection: " + mpn + " CONNECTOR=" + matchesConnector + " TE=" + matchesTE);
        }
    }

    @Nested
    @DisplayName("IDC Connector Detection - Documentation Tests")
    class IDCConnectorTests {

        @ParameterizedTest
        @DisplayName("Document IDC connector detection")
        @ValueSource(strings = {"640456-10", "640457-20"})
        void documentIDCConnectorDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesTE = handler.matches(mpn, ComponentType.CONNECTOR_TE, registry);
            System.out.println("IDC connector detection: " + mpn + " CONNECTOR=" + matchesConnector + " TE=" + matchesTE);
        }
    }

    @Nested
    @DisplayName("MATE-N-LOK Detection - Documentation Tests")
    class MateNLokTests {

        @ParameterizedTest
        @DisplayName("Document MATE-N-LOK detection")
        @ValueSource(strings = {"350211-1", "350211-4"})
        void documentMateNLokDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesTE = handler.matches(mpn, ComponentType.CONNECTOR_TE, registry);
            System.out.println("MATE-N-LOK detection: " + mpn + " CONNECTOR=" + matchesConnector + " TE=" + matchesTE);
        }
    }

    @Nested
    @DisplayName("Headers with Prefix Detection - Documentation Tests")
    class HeadersWithPrefixTests {

        @ParameterizedTest
        @DisplayName("Document headers with prefix detection")
        @ValueSource(strings = {"1-770966-0", "2-770967-0", "1-284392-0"})
        void documentHeadersWithPrefixDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesTE = handler.matches(mpn, ComponentType.CONNECTOR_TE, registry);
            System.out.println("Header with prefix detection: " + mpn + " CONNECTOR=" + matchesConnector + " TE=" + matchesTE);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should extract variant code from terminal block")
        void shouldExtractVariantFromTerminalBlock() {
            assertEquals("2", handler.extractPackageCode("282837-2"));
            assertEquals("4", handler.extractPackageCode("282836-4"));
        }

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"282837-2", "5-826950-2", "350211-4", "1-770966-0"};
            for (String mpn : mpns) {
                String packageCode = handler.extractPackageCode(mpn);
                System.out.println("Package code for " + mpn + ": " + packageCode);
            }
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @Test
        @DisplayName("Should extract terminal block series")
        void shouldExtractTerminalBlockSeries() {
            assertEquals("282837", handler.extractSeries("282837-2"));
            assertEquals("282836", handler.extractSeries("282836-4"));
        }

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"282837-2", "5-826950-2", "640456-10", "350211-1"};
            for (String mpn : mpns) {
                String series = handler.extractSeries(mpn);
                System.out.println("Series for " + mpn + ": " + series);
            }
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {

        @Test
        @DisplayName("Should return correct family")
        void shouldReturnCorrectFamily() {
            assertEquals("Terminal Block", handler.getFamily("282837-2"));
        }

        @Test
        @DisplayName("Should return correct pitch")
        void shouldReturnCorrectPitch() {
            assertEquals("5.08", handler.getPitch("282837-2"));
            assertEquals("5.00", handler.getPitch("282836-4"));
        }

        @Test
        @DisplayName("Should return correct mounting type")
        void shouldReturnCorrectMountingType() {
            assertEquals("THT", handler.getMountingType("282837-1"));
            assertEquals("SMD", handler.getMountingType("282837-2"));
        }

        @Test
        @DisplayName("Should return correct gender")
        void shouldReturnCorrectGender() {
            assertEquals("Male", handler.getGender("282837-1"));
            assertEquals("Female", handler.getGender("282837-2"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Document replacement detection")
        void documentReplacementDetection() {
            String[][] pairs = {
                    {"282837-2", "282837-4"},
                    {"282836-2", "282836-4"},
                    {"5-826950-2", "5-826950-4"}
            };
            for (String[] pair : pairs) {
                boolean isReplacement = handler.isOfficialReplacement(pair[0], pair[1]);
                System.out.println("Replacement check: " + pair[0] + " <-> " + pair[1] + " = " + isReplacement);
            }
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should have supported types")
        void shouldHaveSupportedTypes() {
            var types = handler.getSupportedTypes();
            assertNotNull(types, "Should return non-null set");
            assertFalse(types.isEmpty(), "Should have at least one supported type");
            assertTrue(types.contains(ComponentType.CONNECTOR), "Should support CONNECTOR type");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            TEHandler directHandler = new TEHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes());
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty());
        }
    }
}
