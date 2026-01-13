package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.LGHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for LGHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for LG LED components.
 */
class LGHandlerTest {

    private static LGHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new LGHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("LED Detection")
    class LEDTests {

        @ParameterizedTest
        @DisplayName("Should detect basic LG LED patterns")
        @ValueSource(strings = {"LG R971", "LG R972", "LG R123"})
        void shouldDetectBasicLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LG LEDs with suffix")
        @ValueSource(strings = {"LG R971-KN", "LG R972-LP", "LG R123-ABC"})
        void shouldDetectLEDsWithSuffix(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect LG LEDs with longer numbers")
        @ValueSource(strings = {"LG R9710", "LG R97201", "LG R123456"})
        void shouldDetectLEDsWithLongerNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Should NOT match non-LG patterns")
        void shouldNotMatchNonLGPatterns() {
            assertFalse(handler.matches("CREE XP-G3", ComponentType.LED, registry));
            assertFalse(handler.matches("OSRAM LA E65B", ComponentType.LED, registry));
            assertFalse(handler.matches("LUMILEDS LUXEON", ComponentType.LED, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from suffix")
        @CsvSource({
                "LG R971-KN, KN",
                "LG R972-LP, LP",
                "LG R123-ABC, ABC",
                "LG R9710-XY, XY"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MPNs without suffix")
        void shouldReturnEmptyForNoSuffix() {
            assertEquals("", handler.extractPackageCode("LG R971"));
            assertEquals("", handler.extractPackageCode("LG R972"));
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
        @DisplayName("Should extract series from LG LEDs")
        @CsvSource({
                "LG R971, LG R971",
                "LG R971-KN, LG R971",
                "LG R972, LG R972",
                "LG R972-LP, LG R972"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid formats")
        void shouldReturnEmptyForInvalidFormats() {
            assertEquals("", handler.extractSeries("R971")); // No LG prefix
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series different suffix should be replacements")
        void sameSeriesDifferentSuffix() {
            assertTrue(handler.isOfficialReplacement("LG R971", "LG R971-KN"),
                    "Same series with/without suffix should be replacements");
            assertTrue(handler.isOfficialReplacement("LG R971-KN", "LG R971-LP"),
                    "Same series different suffix should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("LG R971", "LG R972"),
                    "Different series should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("LG R971-KN", "LG R972-KN"),
                    "Different series even with same suffix should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "LG R971"));
            assertFalse(handler.isOfficialReplacement("LG R971", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support LED type")
        void shouldSupportLEDType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.LED));
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
            assertFalse(handler.matches("LG R971", null, registry));
        }
    }
}
