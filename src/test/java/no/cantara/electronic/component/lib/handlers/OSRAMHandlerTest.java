package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.OSRAMHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for OSRAMHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for OSRAM LED components.
 */
class OSRAMHandlerTest {

    private static OSRAMHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new OSRAMHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Standard LED Detection")
    class StandardLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LS series standard LEDs")
        @ValueSource(strings = {"LSA1234", "LSB5678", "LSC9012"})
        void shouldDetectLSSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_STANDARD_OSRAM, registry),
                    mpn + " should match LED_STANDARD_OSRAM");
        }
    }

    @Nested
    @DisplayName("SMD LED Detection")
    class SMDLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LW white SMD LEDs")
        @ValueSource(strings = {"LWA1234", "LWB5678", "LWC9012"})
        void shouldDetectLWWhiteLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_SMD_OSRAM, registry),
                    mpn + " should match LED_SMD_OSRAM");
        }

        @ParameterizedTest
        @DisplayName("Should detect LA amber SMD LEDs")
        @ValueSource(strings = {"LAA1234", "LAB5678", "LAC9012"})
        void shouldDetectLAAmberLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LY yellow SMD LEDs")
        @ValueSource(strings = {"LYA1234", "LYB5678", "LYC9012"})
        void shouldDetectLYYellowLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LB blue SMD LEDs")
        @ValueSource(strings = {"LBA1234", "LBB5678", "LBC9012"})
        void shouldDetectLBBlueLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LR red SMD LEDs")
        @ValueSource(strings = {"LRA1234", "LRB5678", "LRC9012"})
        void shouldDetectLRRedLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LG green SMD LEDs")
        @ValueSource(strings = {"LGA1234", "LGB5678", "LGC9012"})
        void shouldDetectLGGreenLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }
    }

    @Nested
    @DisplayName("High Power LED Detection")
    class HighPowerLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect OSLON series LEDs")
        @ValueSource(strings = {"OSLON SSL 80", "OSLON Square", "OSLON SSL 150"})
        void shouldDetectOSLONSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_OSRAM, registry),
                    mpn + " should match LED_HIGHPOWER_OSRAM");
        }

        @ParameterizedTest
        @DisplayName("Should detect DURIS series LEDs")
        @ValueSource(strings = {"DURIS S5", "DURIS P8", "DURIS E5"})
        void shouldDetectDURISSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }
    }

    @Nested
    @DisplayName("RGB LED Detection")
    class RGBLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LRTB RGB LEDs")
        @ValueSource(strings = {"LRTB GVSG", "LRTB GWTG", "LRTB TEST"})
        void shouldDetectLRTBRGBLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_RGB_OSRAM, registry),
                    mpn + " should match LED_RGB_OSRAM");
        }

        @ParameterizedTest
        @DisplayName("Should detect LBCW RGBW LEDs")
        @ValueSource(strings = {"LBCW GVSG", "LBCW GWTG", "LBCW TEST"})
        void shouldDetectLBCWRGBWLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should return THT for LS series")
        void shouldReturnTHTForLSSeries() {
            assertEquals("THT", handler.extractPackageCode("LSA1234"));
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from SMD LEDs")
        @CsvSource({
                "LWA1234, LWA1",
                "LRA5678, LRA5",
                "LGA9012, LGA9"
        })
        void shouldExtractSMDLEDSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from high power LEDs")
        @CsvSource({
                "OSLON SSL 80, OSLON",
                "DURIS S5, DURIS"
        })
        void shouldExtractHighPowerSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from RGB LEDs")
        @CsvSource({
                "LRTB GVSG, LRTB",
                "LBCW GWTG, LBCW"
        })
        void shouldExtractRGBSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same color LEDs in same series should be replacements")
        void sameColorSameSeriesShouldBeReplacements() {
            // extractSeries returns first 4 alphanumeric chars, so series must match exactly
            // LWA1234 -> series = "LWA1", LWA1567 -> series = "LWA1"
            assertTrue(handler.isOfficialReplacement("LWA1234", "LWA1567"),
                    "Same series white LEDs should be replacements");
        }

        @Test
        @DisplayName("Different color LEDs should NOT be replacements")
        void differentColorsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("LWA1234", "LRA1234"),
                    "Different color LEDs should NOT be replacements");
        }

        @Test
        @DisplayName("OSLON series parts should be replacements")
        void oslonSeriesShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("OSLON SSL 80", "OSLON SSL 150"),
                    "Same OSLON series should be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "LWA1234"));
            assertFalse(handler.isOfficialReplacement("LWA1234", null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.LED));
            assertTrue(types.contains(ComponentType.LED_STANDARD_OSRAM));
            assertTrue(types.contains(ComponentType.LED_SMD_OSRAM));
            assertTrue(types.contains(ComponentType.LED_HIGHPOWER_OSRAM));
            assertTrue(types.contains(ComponentType.LED_RGB_OSRAM));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }
}
