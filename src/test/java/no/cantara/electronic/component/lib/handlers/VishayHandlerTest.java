package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.VishayHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for VishayHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class VishayHandlerTest {

    private static VishayHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new VishayHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("CRCW Resistor Detection")
    class CRCWResistorTests {

        @ParameterizedTest
        @DisplayName("Should detect CRCW chip resistors")
        @ValueSource(strings = {"CRCW0603100RFKEA", "CRCW0805100KJNEA", "CRCW12061K00FKEA"})
        void shouldDetectCRCWResistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RESISTOR_CHIP_VISHAY, registry),
                    mpn + " should match RESISTOR_CHIP_VISHAY");
            assertTrue(handler.matches(mpn, ComponentType.RESISTOR, registry),
                    mpn + " should match RESISTOR (base type)");
        }
    }

    @Nested
    @DisplayName("MOSFET Detection")
    class MOSFETTests {

        @ParameterizedTest
        @DisplayName("Document SI MOSFET detection")
        @ValueSource(strings = {"SI2302CDS", "SI7336ADP", "SIHF16N50D"})
        void documentSIMOSFETDetection(String mpn) {
            // Document actual behavior - may vary
            boolean matchesMosfet = handler.matches(mpn, ComponentType.MOSFET, registry);
            boolean matchesMosfetVishay = handler.matches(mpn, ComponentType.MOSFET_VISHAY, registry);
            System.out.println("MOSFET detection: " + mpn + " MOSFET=" + matchesMosfet + " MOSFET_VISHAY=" + matchesMosfetVishay);
        }
    }

    @Nested
    @DisplayName("Diode Detection")
    class DiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N4xxx rectifier diodes")
        @ValueSource(strings = {"1N4007", "1N4001", "1N4004"})
        void shouldDetect1N4xxxDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect signal diodes")
        @ValueSource(strings = {"1N4148", "1N914"})
        void shouldDetectSignalDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("Transistor Detection")
    class TransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect 2N transistors")
        @ValueSource(strings = {"2N2222", "2N3904", "2N3906"})
        void shouldDetect2NTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract CRCW size codes")
        @CsvSource({
                "CRCW0603100RFKEA, 0603",
                "CRCW0805100KJNEA, 0805",
                "CRCW12061K00FKEA, 1206"
        })
        void shouldExtractCRCWSizeCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from resistors")
        @CsvSource({
                "CRCW0603100RFKEA, CRCW",
                "CRMA0603100RFKEA, CRMA"
        })
        void shouldExtractResistorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Document diode series extraction")
        void documentDiodeSeriesExtraction() {
            // Document actual series extraction behavior
            String[] mpns = {"1N4007", "1N4148"};
            for (String mpn : mpns) {
                String series = handler.extractSeries(mpn);
                System.out.println("Series for " + mpn + ": " + series);
            }
            // Verify 1N4007 returns 1N4000 series
            assertEquals("1N4000", handler.extractSeries("1N4007"),
                    "1N4007 should return 1N4000 series");
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Higher voltage 1N400x should replace lower")
        void higherVoltageReplacesLower() {
            assertTrue(handler.isOfficialReplacement("1N4007", "1N4001"),
                    "1N4007 (1000V) should replace 1N4001 (50V)");
        }

        @Test
        @DisplayName("Lower voltage should NOT replace higher")
        void lowerVoltageNotReplacesHigher() {
            assertFalse(handler.isOfficialReplacement("1N4001", "1N4007"),
                    "1N4001 (50V) should NOT replace 1N4007 (1000V)");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.RESISTOR),
                    "Should support RESISTOR");
            assertTrue(types.contains(ComponentType.RESISTOR_CHIP_VISHAY),
                    "Should support RESISTOR_CHIP_VISHAY");
            assertTrue(types.contains(ComponentType.DIODE),
                    "Should support DIODE");
            assertTrue(types.contains(ComponentType.MOSFET),
                    "Should support MOSFET");
            assertTrue(types.contains(ComponentType.TRANSISTOR),
                    "Should support TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.RESISTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "1N4007"));
            assertFalse(handler.isOfficialReplacement("1N4007", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.RESISTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("CRCW0603100RFKEA", null, registry));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            VishayHandler directHandler = new VishayHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("CRCW0603100RFKEA", ComponentType.RESISTOR_CHIP_VISHAY, directRegistry));
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }
}
