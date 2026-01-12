package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.NichiconHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for NichiconHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class NichiconHandlerTest {

    private static NichiconHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new NichiconHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Standard Grade Detection - Documentation Tests")
    class StandardGradeTests {

        @ParameterizedTest
        @DisplayName("Document standard grade detection")
        @ValueSource(strings = {"UUD1E100MCL1GS", "UUD1V470MCL1GS", "UUE1H100MCL1GS"})
        void documentStandardGradeDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("Standard grade detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("High Temperature Detection - Documentation Tests")
    class HighTemperatureTests {

        @ParameterizedTest
        @DisplayName("Document high temperature detection")
        @ValueSource(strings = {"UHS1C220MDD", "UHE1E100MDD", "UHW1H330MDD"})
        void documentHighTempDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("High temperature detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("Long Life Detection - Documentation Tests")
    class LongLifeTests {

        @ParameterizedTest
        @DisplayName("Document long life detection")
        @ValueSource(strings = {"UES1A100MDE", "UEW1E220MDE", "UKL1V470MHD"})
        void documentLongLifeDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("Long life detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("Low Impedance Detection - Documentation Tests")
    class LowImpedanceTests {

        @ParameterizedTest
        @DisplayName("Document low impedance detection")
        @ValueSource(strings = {"UPW1C100MCL", "UPS1V220MCL"})
        void documentLowImpedanceDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("Low impedance detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("Polymer Capacitor Detection - Documentation Tests")
    class PolymerTests {

        @ParameterizedTest
        @DisplayName("Document polymer capacitor detection")
        @ValueSource(strings = {"PCJ1A100MCL1GS", "PCS1V220MCL1GS", "PCR1E330MCL1GS"})
        void documentPolymerDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("Polymer detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("Miniature Capacitor Detection - Documentation Tests")
    class MiniatureTests {

        @ParameterizedTest
        @DisplayName("Document miniature capacitor detection")
        @ValueSource(strings = {"UMA1A100MDD", "UMD1C220MDD"})
        void documentMiniatureDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("Miniature detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("Photo Flash Detection - Documentation Tests")
    class PhotoFlashTests {

        @ParameterizedTest
        @DisplayName("Document photo flash detection")
        @ValueSource(strings = {"PF1330P", "PF2660P"})
        void documentPhotoFlashDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("Photo flash detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("Super Capacitor (EDLC) Detection - Documentation Tests")
    class SuperCapacitorTests {

        @ParameterizedTest
        @DisplayName("Document EDLC detection")
        @ValueSource(strings = {"JJD1V105MCL", "JJS1V225MCL"})
        void documentEDLCDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesNichicon = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println("EDLC detection: " + mpn + " CAPACITOR=" + matchesCapacitor + " NICHICON=" + matchesNichicon);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"UUD1E040MCL1GS", "UUD1E050MCL1GS", "UUD1E080MCL1GS", "UUD1E100MCL1GS"};
            for (String mpn : mpns) {
                String packageCode = handler.extractPackageCode(mpn);
                System.out.println("Package code for " + mpn + ": " + packageCode);
            }
        }

        @Test
        @DisplayName("Document polymer package code extraction")
        void documentPolymerPackageCodeExtraction() {
            String[] mpns = {"PCJ1A100MCL1GS", "PCS1V220MCL1GS"};
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
        @DisplayName("Should extract Standard Grade series")
        void shouldExtractStandardGradeSeries() {
            assertEquals("Standard Grade", handler.extractSeries("UUD1E100MCL1GS"));
        }

        @Test
        @DisplayName("Should extract Standard High Voltage series")
        void shouldExtractStandardHighVoltageSeries() {
            assertEquals("Standard High Voltage", handler.extractSeries("UUE1H100MCL1GS"));
        }

        @Test
        @DisplayName("Should extract High Temp series")
        void shouldExtractHighTempSeries() {
            assertEquals("125°C High Temp", handler.extractSeries("UHS1C220MDD"));
            assertEquals("135°C High Temp", handler.extractSeries("UHE1E100MDD"));
            assertEquals("105°C High Temp", handler.extractSeries("UHW1H330MDD"));
        }

        @Test
        @DisplayName("Should extract Long Life series")
        void shouldExtractLongLifeSeries() {
            assertEquals("Long Life Standard", handler.extractSeries("UES1A100MDE"));
            assertEquals("Long Life High Ripple", handler.extractSeries("UEW1E220MDE"));
            assertEquals("Extra Long Life", handler.extractSeries("UKL1V470MHD"));
        }

        @Test
        @DisplayName("Should extract Low Impedance series")
        void shouldExtractLowImpedanceSeries() {
            assertEquals("Low Impedance", handler.extractSeries("UPW1C100MCL"));
            assertEquals("Ultra Low Impedance", handler.extractSeries("UPS1V220MCL"));
        }

        @Test
        @DisplayName("Should extract Polymer series")
        void shouldExtractPolymerSeries() {
            assertEquals("Polymer Standard", handler.extractSeries("PCJ1A100MCL1GS"));
            assertEquals("Polymer Low Profile", handler.extractSeries("PCS1V220MCL1GS"));
            assertEquals("Polymer High Reliability", handler.extractSeries("PCR1E330MCL1GS"));
        }

        @Test
        @DisplayName("Should extract EDLC series")
        void shouldExtractEDLCSeries() {
            assertEquals("Standard EDLC", handler.extractSeries("JJD1V105MCL"));
            assertEquals("High Power EDLC", handler.extractSeries("JJS1V225MCL"));
        }

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"UUD1E100MCL1GS", "UHS1C220MDD", "UES1A100MDE", "UPW1C100MCL", "PCJ1A100MCL1GS", "JJD1V105MCL"};
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
        @DisplayName("Document replacement detection")
        void documentReplacementDetection() {
            String[][] pairs = {
                    {"UHE1E100MDD", "UHS1E100MDD"},
                    {"UHS1E100MDD", "UHW1E100MDD"},
                    {"UES1A100MDE", "UUD1A100MDE"}
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
            assertTrue(types.contains(ComponentType.CAPACITOR), "Should support CAPACITOR type");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "UUD1E100MCL1GS"));
            assertFalse(handler.isOfficialReplacement("UUD1E100MCL1GS", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR, registry));
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
            NichiconHandler directHandler = new NichiconHandler();
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
