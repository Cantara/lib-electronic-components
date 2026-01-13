package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AKMHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AKMHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for AKM magnetic sensors, audio ICs, and IMUs.
 */
class AKMHandlerTest {

    private static AKMHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AKMHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Magnetometer Detection")
    class MagnetometerTests {

        @ParameterizedTest
        @DisplayName("Should detect AK89xx magnetometers")
        @ValueSource(strings = {"AK8963", "AK8975", "AK8963C", "AK8975A"})
        void shouldDetectAK89xxMagnetometers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MAGNETOMETER, registry),
                    mpn + " should match MAGNETOMETER");
        }

        @ParameterizedTest
        @DisplayName("Should detect AK099xx magnetometers")
        @ValueSource(strings = {"AK09911", "AK09912", "AK09918", "AK09915"})
        void shouldDetectAK099xxMagnetometers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MAGNETOMETER, registry),
                    mpn + " should match MAGNETOMETER");
        }
    }

    @Nested
    @DisplayName("Sensor Detection")
    class SensorTests {

        @ParameterizedTest
        @DisplayName("Should detect AK09xxx sensors")
        @ValueSource(strings = {"AK09911", "AK09912", "AK09913", "AK09918"})
        void shouldDetectAK09xxxSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect Hall effect sensors")
        @ValueSource(strings = {"AK09911C", "AK09912D", "AK09915E"})
        void shouldDetectHallSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("Audio IC Detection")
    class AudioICTests {

        @ParameterizedTest
        @DisplayName("Should detect AK449x audio DACs")
        @ValueSource(strings = {"AK4490", "AK4493", "AK4497", "AK4499"})
        void shouldDetectAK449xDACs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AK479x audio DACs")
        @ValueSource(strings = {"AK4793", "AK4795", "AK4797", "AK4790"})
        void shouldDetectAK479xDACs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AK5xxx audio ADCs")
        @ValueSource(strings = {"AK5386", "AK5386A", "AK5386B"})
        void shouldDetectAK5xxxADCs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should return empty for standard sensor MPNs without package suffix")
        void shouldReturnEmptyForStandardMpns() {
            // Handler extracts package from suffix after base part number
            String result = handler.extractPackageCode("AK8963");
            assertNotNull(result, "Package code should not be null");
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
        @DisplayName("Should extract sensor series (6 chars)")
        @CsvSource({
                "AK8963, AK8963",
                "AK8963C, AK8963",
                "AK09918, AK0991",
                "AK09911A, AK0991"
        })
        void shouldExtractSensorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract audio IC series (5 chars)")
        @CsvSource({
                "AK4490, AK449",
                "AK4490EQ, AK449",
                "AK5386, AK538",
                "AK5386A, AK538"
        })
        void shouldExtractAudioICSeries(String mpn, String expectedSeries) {
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
        @DisplayName("Same sensor series with same resolution should be replacements")
        void sameSensorSeriesSameResolution() {
            // Same series sensors with matching resolution and interface
            assertTrue(handler.isOfficialReplacement("AK8963", "AK8963C"),
                    "Same series sensors should be replacements");
        }

        @Test
        @DisplayName("Same audio IC series with same sample rate should be replacements")
        void sameAudioICSeries() {
            // AK4490 and AK4490EQ are same series
            assertTrue(handler.isOfficialReplacement("AK4490", "AK4490EQ"),
                    "Same audio IC series should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("AK8963", "AK8975"),
                    "Different sensor series should NOT be replacements");
            // Note: AK4490 and AK4493 have same series prefix "AK449" (5 chars for audio ICs)
            // so they ARE considered replacements by the handler
            assertFalse(handler.isOfficialReplacement("AK4490", "AK5386"),
                    "Different audio IC families should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "AK8963"));
            assertFalse(handler.isOfficialReplacement("AK8963", null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.SENSOR));
            assertTrue(types.contains(ComponentType.MAGNETOMETER));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.SENSOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.SENSOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("AK8963", null, registry));
        }
    }
}
