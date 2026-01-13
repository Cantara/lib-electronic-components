package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.LumiledsHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for LumiledsHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Lumileds LUXEON LED components.
 */
class LumiledsHandlerTest {

    private static LumiledsHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new LumiledsHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("LUXEON High Power LED Detection")
    class HighPowerLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LUXEON TX series (L1T2)")
        @ValueSource(strings = {"L1T2-4070000000000", "L1T2-4090000000000", "L1T2-CRXX"})
        void shouldDetectLUXEONTX(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_LUMILEDS, registry),
                    mpn + " should match LED_HIGHPOWER_LUMILEDS");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON M series (L1M1)")
        @ValueSource(strings = {"L1M1-MXC1000000000", "L1M1-MXC2000000000", "L1M1-TEST"})
        void shouldDetectLUXEONM(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_LUMILEDS, registry),
                    mpn + " should match LED_HIGHPOWER_LUMILEDS");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON V series (L1V0)")
        @ValueSource(strings = {"L1V0-4080000000000", "L1V0-CR65000000000", "L1V0-TEST"})
        void shouldDetectLUXEONV(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_LUMILEDS, registry),
                    mpn + " should match LED_HIGHPOWER_LUMILEDS");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON Rebel series (LXML)")
        @ValueSource(strings = {"LXML-PWC1-0100", "LXML-PM01-0080", "LXML-TEST"})
        void shouldDetectLUXEONRebel(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_LUMILEDS, registry),
                    mpn + " should match LED_HIGHPOWER_LUMILEDS");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON III series (LXHL)")
        @ValueSource(strings = {"LXHL-PW09", "LXHL-MW1D", "LXHL-TEST"})
        void shouldDetectLUXEONIII(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_LUMILEDS, registry),
                    mpn + " should match LED_HIGHPOWER_LUMILEDS");
        }
    }

    @Nested
    @DisplayName("LUXEON Color LED Detection")
    class ColorLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LUXEON Z series (LXZ1)")
        @ValueSource(strings = {"LXZ1-PA01", "LXZ1-PM01", "LXZ1-TEST"})
        void shouldDetectLUXEONZ(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_LUMILEDS, registry),
                    mpn + " should match LED_HIGHPOWER_LUMILEDS");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON C series (LXCL)")
        @ValueSource(strings = {"LXCL-PWC1", "LXCL-PM01", "LXCL-TEST"})
        void shouldDetectLUXEONC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_LUMILEDS, registry),
                    mpn + " should match LED_HIGHPOWER_LUMILEDS");
        }
    }

    @Nested
    @DisplayName("RGB and Multi-die LED Detection")
    class RGBLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LUXEON Altilon series (LXI8)")
        @ValueSource(strings = {"LXI8-PW40", "LXI8-RED1", "LXI8-TEST"})
        void shouldDetectLUXEONAltilon(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON RGB series (LXA7)")
        @ValueSource(strings = {"LXA7-PW50", "LXA7-RGB1", "LXA7-TEST"})
        void shouldDetectLUXEONRGB(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }
    }

    @Nested
    @DisplayName("Mid-Power LED Detection")
    class MidPowerLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LUXEON 3030 series (L130)")
        @ValueSource(strings = {"L130-4080002800000", "L130-CW80002000000", "L130-TEST"})
        void shouldDetectLUXEON3030(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON 3535 series (L135)")
        @ValueSource(strings = {"L135-4080002800000", "L135-NW65002000000", "L135-TEST"})
        void shouldDetectLUXEON3535(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON 5050 series (L150)")
        @ValueSource(strings = {"L150-4080002800000", "L150-WW27002000000", "L150-TEST"})
        void shouldDetectLUXEON5050(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }
    }

    @Nested
    @DisplayName("Automotive LED Detection")
    class AutomotiveLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect LUXEON Altilon automotive (LXA2)")
        @ValueSource(strings = {"LXA2-PW40-0000", "LXA2-AM50-0000", "LXA2-TEST"})
        void shouldDetectAutomotiveAltilon(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LUXEON F automotive (LXA3)")
        @ValueSource(strings = {"LXA3-PW50-0000", "LXA3-RD40-0000", "LXA3-TEST"})
        void shouldDetectAutomotiveF(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from series")
        @CsvSource({
                "LXZ1-PA01, Z2",
                "LXML-PWC1, Rebel",
                "L130-4080002800000, 3030",
                "L135-4080002800000, 3535",
                "L150-4080002800000, 5050"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
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
        @DisplayName("Should extract series from LUXEON LEDs")
        @CsvSource({
                "LXML-PWC1-0100, LXML",
                "LXHL-PW09, LXHL",
                "LXZ1-PA01, LXZ1",
                "L1T2-4070000000000, L1T2",
                "L1M1-MXC1000000000, L1M1",
                "L130-4080002800000, L130",
                "L135-4080002800000, L135"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with compatible colors should be replacements")
        void sameSeriesShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("LXML-PWC1-0100", "LXML-PM01-0080"),
                    "Same LXML series should be replacements");
            // L130 parts with same color code (40) should be replacements
            assertTrue(handler.isOfficialReplacement("L130-4080002800000", "L130-4090002000000"),
                    "Same L130 series with same color should be replacements");
        }

        @Test
        @DisplayName("Same series with different colors should NOT be replacements")
        void differentColorsShouldNotBeReplacements() {
            // L130 parts with different color codes should NOT be replacements
            assertFalse(handler.isOfficialReplacement("L130-4080002800000", "L130-CW80002000000"),
                    "Same series but different colors should NOT be replacements");
        }

        @Test
        @DisplayName("Compatible series should be replacements")
        void compatibleSeriesShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("LXML-PWC1", "LXZ1-PA01"),
                    "LXML and LXZ1 should be compatible");
            assertTrue(handler.isOfficialReplacement("L130-TEST", "L135-TEST"),
                    "L130 and L135 should be compatible");
        }

        @Test
        @DisplayName("Different incompatible series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("LXML-PWC1", "LXHL-PW09"),
                    "LXML and LXHL should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("L1T2-TEST", "L150-TEST"),
                    "L1T2 and L150 should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "LXML-PWC1"));
            assertFalse(handler.isOfficialReplacement("LXML-PWC1", null));
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
            assertTrue(types.contains(ComponentType.LED_HIGHPOWER_LUMILEDS));
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
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

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("LXML-PWC1", null, registry));
        }
    }
}
