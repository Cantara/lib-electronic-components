package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AmphenolHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AmphenolHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
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
    @DisplayName("Mini-PV Series Detection - Documentation Tests")
    class MiniPVTests {

        @ParameterizedTest
        @DisplayName("Document Mini-PV series detection")
        @ValueSource(strings = {"504182-0210", "504182-0410", "505478-0210", "505478-0410"})
        void documentMiniPVDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println("Mini-PV detection: " + mpn + " CONNECTOR=" + matchesConnector + " AMPHENOL=" + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("HD20 Series Detection - Documentation Tests")
    class HD20Tests {

        @ParameterizedTest
        @DisplayName("Document HD20 series detection")
        @ValueSource(strings = {"10120843-0210", "10120843-0410", "10120855-0210", "10120855-0410"})
        void documentHD20Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println("HD20 detection: " + mpn + " CONNECTOR=" + matchesConnector + " AMPHENOL=" + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("SFP+ Cage Detection - Documentation Tests")
    class SFPTests {

        @ParameterizedTest
        @DisplayName("Document SFP+ cage detection")
        @ValueSource(strings = {"RJHSE-5380-02", "RJHSE-5381-04", "RJHSE-5382-01"})
        void documentSFPDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println("SFP+ detection: " + mpn + " CONNECTOR=" + matchesConnector + " AMPHENOL=" + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("USB 3.0 Connector Detection - Documentation Tests")
    class USB3Tests {

        @ParameterizedTest
        @DisplayName("Document USB 3.0 connector detection")
        @ValueSource(strings = {"USB3-A-01-A001", "USB3-A-02-B002"})
        void documentUSB3Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println("USB 3.0 detection: " + mpn + " CONNECTOR=" + matchesConnector + " AMPHENOL=" + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("Additional Series Detection - Documentation Tests")
    class AdditionalSeriesTests {

        @ParameterizedTest
        @DisplayName("Document additional series detection")
        @ValueSource(strings = {"10051922-0410", "10151980-0210", "10129378-0410", "RJMG2310-01"})
        void documentAdditionalSeriesDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesAmphenol = handler.matches(mpn, ComponentType.CONNECTOR_AMPHENOL, registry);
            System.out.println("Additional series detection: " + mpn + " CONNECTOR=" + matchesConnector + " AMPHENOL=" + matchesAmphenol);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should extract package code from Mini-PV")
        void shouldExtractPackageCodeFromMiniPV() {
            assertEquals("0210", handler.extractPackageCode("504182-0210"));
            assertEquals("0410", handler.extractPackageCode("504182-0410"));
        }

        @Test
        @DisplayName("Should extract package code from HD20")
        void shouldExtractPackageCodeFromHD20() {
            assertEquals("0210", handler.extractPackageCode("10120843-0210"));
            assertEquals("0410", handler.extractPackageCode("10120843-0410"));
        }

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"504182-0210", "10120843-0410", "RJHSE-5380-02", "USB3-A-01-A001"};
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
        @DisplayName("Should extract Mini-PV series")
        void shouldExtractMiniPVSeries() {
            assertEquals("504182", handler.extractSeries("504182-0210"));
            assertEquals("505478", handler.extractSeries("505478-0210"));
        }

        @Test
        @DisplayName("Should extract HD20 series")
        void shouldExtractHD20Series() {
            assertEquals("10120843", handler.extractSeries("10120843-0210"));
            assertEquals("10120855", handler.extractSeries("10120855-0210"));
        }

        @Test
        @DisplayName("Should extract SFP series")
        void shouldExtractSFPSeries() {
            assertEquals("RJHSE", handler.extractSeries("RJHSE-5380-02"));
        }

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"504182-0210", "10120843-0210", "RJHSE-5380-02", "USB3-A-01-A001", "10051922-0410"};
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
        @DisplayName("Should return correct pitch")
        void shouldReturnCorrectPitch() {
            assertEquals("2.00", handler.getPitch("504182-0210"));
            assertEquals("2.00", handler.getPitch("10120843-0210"));
            assertEquals("3.00", handler.getPitch("10051922-0410"));
        }

        @Test
        @DisplayName("Should return correct mounting type")
        void shouldReturnCorrectMountingType() {
            assertEquals("THT", handler.getMountingType("504182-0210"));
            assertEquals("SMT", handler.getMountingType("505478-0210"));
        }

        @Test
        @DisplayName("Should return correct rated current")
        void shouldReturnCorrectRatedCurrent() {
            assertEquals(3.0, handler.getRatedCurrent("504182-0210"));
            assertEquals(5.0, handler.getRatedCurrent("10051922-0410"));
        }

        @Test
        @DisplayName("Should detect shielded connectors")
        void shouldDetectShieldedConnectors() {
            assertTrue(handler.isShielded("RJHSE-5380-02"));
            assertTrue(handler.isShielded("USB3-A-01-A001"));
            assertFalse(handler.isShielded("504182-0210"));
        }

        @Test
        @DisplayName("Should return correct application type")
        void shouldReturnCorrectApplicationType() {
            assertEquals("SFP/SFP+", handler.getApplicationType("RJHSE-5380-02"));
            assertEquals("USB 3.0", handler.getApplicationType("USB3-A-01-A001"));
            assertEquals("Power", handler.getApplicationType("10051922-0410"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Document replacement detection")
        void documentReplacementDetection() {
            String[][] pairs = {
                    {"504182-0210", "504182-0410"},
                    {"10120843-0210", "10120855-0210"},
                    {"RJHSE-5380-02", "RJHSE-5381-02"}
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
            assertFalse(handler.isOfficialReplacement(null, "504182-0210"));
            assertFalse(handler.isOfficialReplacement("504182-0210", null));
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
            AmphenolHandler directHandler = new AmphenolHandler();
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
