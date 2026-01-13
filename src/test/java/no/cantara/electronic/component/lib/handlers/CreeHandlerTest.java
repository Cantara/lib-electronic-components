package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.CreeHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CreeHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Cree LED components.
 */
class CreeHandlerTest {

    private static CreeHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new CreeHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("LED Detection")
    class LEDTests {

        @ParameterizedTest
        @DisplayName("Should detect CLV series LEDs")
        @ValueSource(strings = {"CLV1A-FKA", "CLV1B-FKB", "CLV2A-FKA", "CLV6A-FKA"})
        void shouldDetectCLVSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.LED_HIGHPOWER_CREE, registry),
                    mpn + " should match LED_HIGHPOWER_CREE");
        }

        @ParameterizedTest
        @DisplayName("Should detect CLVBA series LEDs with bin codes")
        @ValueSource(strings = {"CLVBA-FKA", "CLVBA-FKB", "CLVBA-FKC", "CLVBA-WKA"})
        void shouldDetectCLVBALEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Should NOT match non-Cree patterns")
        void shouldNotMatchNonCreePatterns() {
            assertFalse(handler.matches("OSRAM LA E65B", ComponentType.LED, registry));
            assertFalse(handler.matches("LUMILEDS LUXEON", ComponentType.LED, registry));
            assertFalse(handler.matches("LG R971", ComponentType.LED, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should return SMD for CLVBA series")
        void shouldReturnSMDForCLVBA() {
            assertEquals("SMD", handler.extractPackageCode("CLVBA-FKA"));
            assertEquals("SMD", handler.extractPackageCode("CLVBA-WKB"));
        }

        @Test
        @DisplayName("Should return empty for non-CLVBA series")
        void shouldReturnEmptyForNonCLVBA() {
            assertEquals("", handler.extractPackageCode("CLV1A-FKA"));
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
        @DisplayName("Should extract series from CLVBA LEDs")
        @CsvSource({
                "CLVBA-FKA, CLVBA-FK",
                "CLVBA-FKB, CLVBA-FK",
                "CLVBA-WKA, CLVBA-WK"
        })
        void shouldExtractCLVBASeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from CLV LEDs")
        @CsvSource({
                "CLV1A-FKA, CLV1A",
                "CLV2B-WKA, CLV2B"
        })
        void shouldExtractCLVSeries(String mpn, String expectedSeries) {
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
        @DisplayName("Same series with different bin codes should be replacements")
        void sameSeriesDifferentBinCodes() {
            // CLVBA-FKA and CLVBA-FKB are same series with different bin codes
            assertTrue(handler.isOfficialReplacement("CLVBA-FKA", "CLVBA-FKB"),
                    "Same series different bin should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CLVBA-FKA", "CLVBA-WKA"),
                    "Different color bins (FK vs WK) should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "CLVBA-FKA"));
            assertFalse(handler.isOfficialReplacement("CLVBA-FKA", null));
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
            assertTrue(types.contains(ComponentType.LED_HIGHPOWER_CREE));
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

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("CLVBA-FKA", null, registry));
        }
    }
}
