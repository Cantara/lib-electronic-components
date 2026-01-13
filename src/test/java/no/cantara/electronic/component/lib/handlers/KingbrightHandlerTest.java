package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.KingbrightHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for KingbrightHandler.
 *
 * Kingbright is a major LED manufacturer producing:
 * - Through-hole LEDs (WP prefix - lead-free, replaced old "L" prefix)
 * - SMD top-emitting LEDs (KP prefix)
 * - SMD right-angle LEDs (AP, APT, APTD prefix)
 * - SMD PLCC LEDs (AA prefix)
 * - LED displays (various)
 *
 * Key MPN patterns:
 * - WP7113ID: 5mm through-hole red LED
 * - WP7113SGC: 5mm through-hole green LED
 * - APTD3216SRCPRV: 1206 SMD red LED
 * - AA3528SURSK: 3528 PLCC SMD LED
 * - KP-2012CGCK: 0805 SMD green LED
 * - APT2012CGCK: 0805 right-angle green LED
 */
class KingbrightHandlerTest {

    private static KingbrightHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new KingbrightHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // Through-Hole LED Tests (WP series)
    // ========================================================================

    @Nested
    @DisplayName("Through-Hole LED Detection (WP series)")
    class ThroughHoleLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect WP 5mm through-hole LEDs")
        @ValueSource(strings = {
            "WP7113ID",           // 5mm red
            "WP7113SGC",          // 5mm green
            "WP7113SBC",          // 5mm blue
            "WP7113SEC",          // 5mm yellow
            "WP7113SWC",          // 5mm white
            "WP7113ID/D",         // 5mm red diffused
            "WP7113QBC/D"         // 5mm blue clear
        })
        void shouldDetect5mmThroughHoleLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_STANDARD_KINGBRIGHT, registry),
                    mpn + " should match LED_STANDARD_KINGBRIGHT");
        }

        @ParameterizedTest
        @DisplayName("Should detect WP 3mm through-hole LEDs")
        @ValueSource(strings = {
            "WP3114ID",           // 3mm red
            "WP3114SGC",          // 3mm green
            "WP3114SBC",          // 3mm blue
            "WP34ID"              // 3mm flat top red
        })
        void shouldDetect3mmThroughHoleLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Through-hole LEDs should return correct mounting type")
        void throughHoleShouldReturnCorrectMountingType() {
            assertEquals("Through-hole", handler.getMountingType("WP7113ID"));
            assertEquals("Through-hole", handler.getMountingType("WP3114SGC"));
        }
    }

    // ========================================================================
    // SMD Top-Emitting LED Tests (KP series)
    // ========================================================================

    @Nested
    @DisplayName("SMD Top-Emitting LED Detection (KP series)")
    class SMDTopEmittingTests {

        @ParameterizedTest
        @DisplayName("Should detect KP SMD LEDs")
        @ValueSource(strings = {
            "KP-2012CGCK",        // 0805 green
            "KP-1608SRCPRV",      // 0603 red
            "KP-3216SRCPRV",      // 1206 red
            "KP2012CGCK",         // Without dash
            "KP-2012F3C",         // Alternative naming
            "KP-1608MGC"          // 0603 green
        })
        void shouldDetectKPSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_SMD_KINGBRIGHT, registry),
                    mpn + " should match LED_SMD_KINGBRIGHT");
        }

        @Test
        @DisplayName("KP series should return SMD mounting type")
        void kpSeriesShouldReturnSMDMountingType() {
            assertEquals("SMD", handler.getMountingType("KP-2012CGCK"));
            assertEquals("SMD", handler.getMountingType("KP2012SRCPRV"));
        }
    }

    // ========================================================================
    // SMD Right-Angle LED Tests (APT/APTD/AP series)
    // ========================================================================

    @Nested
    @DisplayName("SMD Right-Angle LED Detection (AP series)")
    class SMDRightAngleTests {

        @ParameterizedTest
        @DisplayName("Should detect APTD series LEDs")
        @ValueSource(strings = {
            "APTD3216SRCPRV",     // 1206 red
            "APTD3216CGCK",       // 1206 green
            "APTD2012SRCPRV",     // 0805 red
            "APTD1608SRCPRV"      // 0603 red
        })
        void shouldDetectAPTDSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_SMD_KINGBRIGHT, registry),
                    mpn + " should match LED_SMD_KINGBRIGHT");
        }

        @ParameterizedTest
        @DisplayName("Should detect APT series LEDs")
        @ValueSource(strings = {
            "APT2012CGCK",        // 0805 green
            "APT2012SRCPRV",      // 0805 red
            "APT3216CGCK"         // 1206 green
        })
        void shouldDetectAPTSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("AP series should return SMD mounting type")
        void apSeriesShouldReturnSMDMountingType() {
            assertEquals("SMD", handler.getMountingType("APTD3216SRCPRV"));
            assertEquals("SMD", handler.getMountingType("APT2012CGCK"));
        }
    }

    // ========================================================================
    // SMD PLCC LED Tests (AA series)
    // ========================================================================

    @Nested
    @DisplayName("SMD PLCC LED Detection (AA series)")
    class SMDPLCCTests {

        @ParameterizedTest
        @DisplayName("Should detect AA series PLCC LEDs")
        @ValueSource(strings = {
            "AA3528SURSK",        // 3528 PLCC red
            "AA3528SRCPRV",       // 3528 PLCC red variant
            "AA3528CGSK",         // 3528 PLCC green
            "AA5060SURSK",        // 5060 PLCC red
            "AA3528ESGW"          // 3528 PLCC green/white
        })
        void shouldDetectAASeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_SMD_KINGBRIGHT, registry),
                    mpn + " should match LED_SMD_KINGBRIGHT");
        }

        @Test
        @DisplayName("AA series should return SMD mounting type")
        void aaSeriesShouldReturnSMDMountingType() {
            assertEquals("SMD", handler.getMountingType("AA3528SURSK"));
        }
    }

    // ========================================================================
    // Package Code Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract through-hole package codes")
        @CsvSource({
            "WP7113ID, T-1-3/4",       // 5mm
            "WP7113SGC, T-1-3/4",      // 5mm
            "WP3114ID, T-1"            // 3mm
        })
        void shouldExtractThroughHolePackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SMD package codes with metric to imperial conversion")
        @CsvSource({
            "KP-2012CGCK, 0805",       // 2012 metric = 0805 imperial
            "KP-1608SRCPRV, 0603",     // 1608 metric = 0603 imperial
            "KP-3216SRCPRV, 1206",     // 3216 metric = 1206 imperial
            "APTD3216SRCPRV, 1206",    // 3216 metric = 1206 imperial
            "APT2012CGCK, 0805",       // 2012 metric = 0805 imperial
            "AA3528SURSK, 1411"        // 3528 metric = 1411 imperial (PLCC)
        })
        void shouldExtractSMDPackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle null and empty inputs")
        void shouldHandleNullAndEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    // ========================================================================
    // Series Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract through-hole series")
        @CsvSource({
            "WP7113ID, WP7113",
            "WP7113SGC, WP7113",
            "WP3114ID, WP3114",
            "WP34ID, WP34"
        })
        void shouldExtractThroughHoleSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SMD KP series")
        @CsvSource({
            "KP-2012CGCK, KP-2012",
            "KP-1608SRCPRV, KP-1608",
            "KP2012CGCK, KP-2012"
        })
        void shouldExtractKPSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SMD APT/APTD series")
        @CsvSource({
            "APTD3216SRCPRV, APTD3216",
            "APT2012CGCK, APT2012",
            "APTD2012SRCPRV, APTD2012"
        })
        void shouldExtractAPTSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SMD AA series")
        @CsvSource({
            "AA3528SURSK, AA3528",
            "AA5060SURSK, AA5060"
        })
        void shouldExtractAASeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }
    }

    // ========================================================================
    // Color Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Color Extraction")
    class ColorExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract LED colors")
        @CsvSource({
            "WP7113ID, Red",
            "WP7113SGC, Green",
            "WP7113SBC, Blue",
            "APTD3216SRCPRV, Red",
            "KP-2012CGCK, Green",
            "AA3528SURSK, Red"
        })
        void shouldExtractColors(String mpn, String expected) {
            String result = handler.extractColor(mpn);
            assertEquals(expected, result, "Color for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown color codes")
        void shouldReturnEmptyForUnknownColors() {
            assertEquals("", handler.extractColor("WP7113XYZ"));
        }
    }

    // ========================================================================
    // Edge Cases and Null Handling
    // ========================================================================

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractColor(null));
            assertEquals("", handler.getMountingType(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractColor(""));
            assertEquals("", handler.getMountingType(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("WP7113ID", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("wp7113id", ComponentType.LED, registry));
            assertTrue(handler.matches("kp-2012cgck", ComponentType.LED_SMD_KINGBRIGHT, registry));
            assertTrue(handler.matches("aptd3216srcprv", ComponentType.LED, registry));
        }
    }

    // ========================================================================
    // getSupportedTypes() Tests
    // ========================================================================

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should include all expected LED component types")
        void shouldIncludeAllExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.LED), "Should support LED");
            assertTrue(types.contains(ComponentType.LED_STANDARD_KINGBRIGHT), "Should support LED_STANDARD_KINGBRIGHT");
            assertTrue(types.contains(ComponentType.LED_SMD_KINGBRIGHT), "Should support LED_SMD_KINGBRIGHT");
            assertTrue(types.contains(ComponentType.LED_RGB_KINGBRIGHT), "Should support LED_RGB_KINGBRIGHT");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes should return an immutable set");
        }
    }

    // ========================================================================
    // Official Replacement Tests
    // ========================================================================

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and package should be official replacement")
        void sameSeriesToAreReplacement() {
            // Same series (WP7113), same 5mm package, different colors
            assertTrue(handler.isOfficialReplacement(
                    "WP7113ID",
                    "WP7113SGC"),
                    "Same series WP7113 with same package should be replacements");
        }

        @Test
        @DisplayName("Different series should not be official replacement")
        void differentSeriesNotReplacement() {
            // Different series (WP7113 vs WP3114) = not replacement
            assertFalse(handler.isOfficialReplacement(
                    "WP7113ID",
                    "WP3114ID"),
                    "WP7113 and WP3114 are different series");
        }

        @Test
        @DisplayName("Same SMD series different color should be replacement")
        void sameSMDSeriesReplacement() {
            assertTrue(handler.isOfficialReplacement(
                    "APTD3216SRCPRV",
                    "APTD3216CGCK"),
                    "Same APTD3216 series should be replacements");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsNotReplacement() {
            assertFalse(handler.isOfficialReplacement(null, "WP7113ID"));
            assertFalse(handler.isOfficialReplacement("WP7113ID", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    // ========================================================================
    // Non-Matching Tests
    // ========================================================================

    @Nested
    @DisplayName("Non-Matching MPNs")
    class NonMatchingTests {

        @ParameterizedTest
        @DisplayName("Should not match non-Kingbright LEDs")
        @ValueSource(strings = {
            "CREE-XPE",           // Cree LED
            "OSRAM-LW",           // OSRAM LED
            "LM7805",             // TI regulator
            "STM32F103",          // ST microcontroller
            "GRM188R71H104KA93",  // Murata capacitor
            "RC0603FR-0710KL"     // Yageo resistor
        })
        void shouldNotMatchNonKingbrightParts(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should NOT match LED for Kingbright handler");
        }

        @Test
        @DisplayName("Should not match unrelated component types")
        void shouldNotMatchUnrelatedTypes() {
            assertFalse(handler.matches("WP7113ID", ComponentType.RESISTOR, registry));
            assertFalse(handler.matches("WP7113ID", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("WP7113ID", ComponentType.MICROCONTROLLER, registry));
        }
    }
}
