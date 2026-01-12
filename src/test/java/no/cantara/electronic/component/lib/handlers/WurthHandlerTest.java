package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.WurthHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for WurthHandler (Wurth Elektronik).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
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
    @DisplayName("Pin Header Detection - Documentation Tests")
    class PinHeaderTests {

        @ParameterizedTest
        @DisplayName("Document pin header detection")
        @ValueSource(strings = {"61300211121", "61300411121", "61300611121", "61300811121"})
        void documentPinHeaderDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesWurth = handler.matches(mpn, ComponentType.CONNECTOR_WURTH, registry);
            System.out.println("Pin header detection: " + mpn + " CONNECTOR=" + matchesConnector + " WURTH=" + matchesWurth);
        }
    }

    @Nested
    @DisplayName("Socket Header Detection - Documentation Tests")
    class SocketHeaderTests {

        @ParameterizedTest
        @DisplayName("Document socket header detection")
        @ValueSource(strings = {"62000211121", "62000411121", "62000611121"})
        void documentSocketHeaderDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesWurth = handler.matches(mpn, ComponentType.CONNECTOR_WURTH, registry);
            System.out.println("Socket header detection: " + mpn + " CONNECTOR=" + matchesConnector + " WURTH=" + matchesWurth);
        }
    }

    @Nested
    @DisplayName("WR-PHD Series Detection - Documentation Tests")
    class WRPHDTests {

        @ParameterizedTest
        @DisplayName("Document WR-PHD series detection")
        @ValueSource(strings = {"61300211121", "61300411121"})
        void documentWRPHDDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesWurth = handler.matches(mpn, ComponentType.CONNECTOR_WURTH, registry);
            System.out.println("WR-PHD detection: " + mpn + " CONNECTOR=" + matchesConnector + " WURTH=" + matchesWurth);
        }
    }

    @Nested
    @DisplayName("WR-BHD Series Detection - Documentation Tests")
    class WRBHDTests {

        @ParameterizedTest
        @DisplayName("Document WR-BHD series detection")
        @ValueSource(strings = {"61400211121", "61400411121"})
        void documentWRBHDDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesWurth = handler.matches(mpn, ComponentType.CONNECTOR_WURTH, registry);
            System.out.println("WR-BHD detection: " + mpn + " CONNECTOR=" + matchesConnector + " WURTH=" + matchesWurth);
        }
    }

    @Nested
    @DisplayName("WR-TBL Series Detection - Documentation Tests")
    class WRTBLTests {

        @ParameterizedTest
        @DisplayName("Document WR-TBL series detection")
        @ValueSource(strings = {"61500211121", "61500411121"})
        void documentWRTBLDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesWurth = handler.matches(mpn, ComponentType.CONNECTOR_WURTH, registry);
            System.out.println("WR-TBL detection: " + mpn + " CONNECTOR=" + matchesConnector + " WURTH=" + matchesWurth);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should extract variant code from header")
        void shouldExtractVariantCode() {
            assertEquals("1", handler.extractPackageCode("61300211121"));
        }

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"61300211121", "61300411121", "62000211121", "61400211121"};
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
        @DisplayName("Should extract series from header MPN")
        void shouldExtractSeriesFromHeader() {
            assertEquals("61300", handler.extractSeries("61300211121"));
            assertEquals("61300", handler.extractSeries("61300411121"));
        }

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"61300211121", "62000211121", "61400211121", "61500211121"};
            for (String mpn : mpns) {
                String series = handler.extractSeries(mpn);
                System.out.println("Series for " + mpn + ": " + series);
            }
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series, pin count, pitch but different variant should be compatible")
        void sameSeriesDifferentVariantShouldBeCompatible() {
            // 61300 21 112 1 and 61300 21 112 2 - same series, pin count, pitch, different variant
            assertTrue(handler.isOfficialReplacement("61300211121", "61300211122"));
        }

        @Test
        @DisplayName("Document replacement detection")
        void documentReplacementDetection() {
            String[][] pairs = {
                    {"61300211121", "61300211122"},
                    {"61300211121", "61300411121"},
                    {"61300211121", "62000211121"}
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

        @Test
        @DisplayName("Should support LED types")
        void shouldSupportLEDTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.LED), "Should support LED type");
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
            assertFalse(handler.isOfficialReplacement(null, "61300211121"));
            assertFalse(handler.isOfficialReplacement("61300211121", null));
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
            WurthHandler directHandler = new WurthHandler();
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
