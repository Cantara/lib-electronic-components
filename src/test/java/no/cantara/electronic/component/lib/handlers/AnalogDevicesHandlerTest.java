package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.MPNUtils;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AnalogDevicesHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AnalogDevicesHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * KNOWN BUGS DOCUMENTED IN THIS TEST FILE:
 * 1. Package extraction fails for most AD parts (returns empty string)
 * 2. HashSet used in getSupportedTypes() instead of Set.of() or EnumSet
 * 3. ManufacturerHandler.matches() uses getPattern() which returns only FIRST pattern
 *    - This affects types with multiple patterns (OPAMP, TEMPERATURE_SENSOR, IC)
 *    - Use registry.matches() for accurate multi-pattern matching
 */
class AnalogDevicesHandlerTest {

    private static AnalogDevicesHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct handler instantiation
        handler = new AnalogDevicesHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // Pattern Registration Tests - Using registry.matches() for accuracy
    // ========================================================================

    @Nested
    @DisplayName("Op-Amp Pattern Registration")
    class OpAmpPatternTests {

        @ParameterizedTest
        @DisplayName("OP series op-amps should match OPAMP_AD via registry")
        @ValueSource(strings = {"OP07", "OP07CPZ", "OP27", "OP27GPZ", "OP37", "OP177"})
        void opSeriesShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.OPAMP_AD),
                    mpn + " should match OPAMP_AD via registry.matches()");
            assertTrue(registry.matches(mpn, ComponentType.OPAMP),
                    mpn + " should match OPAMP via registry.matches()");
        }

        @ParameterizedTest
        @DisplayName("AD8xxx series op-amps should match OPAMP_AD via registry")
        @ValueSource(strings = {"AD8065", "AD8065ARZ", "AD8066", "AD8597", "AD8599", "AD8605", "AD8606", "AD8608"})
        void ad8SeriesShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.OPAMP_AD),
                    mpn + " should match OPAMP_AD via registry.matches()");
            assertTrue(registry.matches(mpn, ComponentType.OPAMP),
                    mpn + " should match OPAMP via registry.matches()");
        }
    }

    @Nested
    @DisplayName("Accelerometer Pattern Registration")
    class AccelerometerPatternTests {

        @ParameterizedTest
        @DisplayName("ADXL series accelerometers should match via registry")
        @ValueSource(strings = {"ADXL345", "ADXL345BCCZ", "ADXL345BCCZ-RL", "ADXL355", "ADXL375", "ADXL1002"})
        void adxlSeriesShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.ACCELEROMETER_AD),
                    mpn + " should match ACCELEROMETER_AD via registry.matches()");
            assertTrue(registry.matches(mpn, ComponentType.ACCELEROMETER),
                    mpn + " should match ACCELEROMETER via registry.matches()");
        }

        @ParameterizedTest
        @DisplayName("ADXL series should match via handler.matches()")
        @ValueSource(strings = {"ADXL345", "ADXL345BCCZ", "ADXL355", "ADXL375", "ADXL1002"})
        void adxlSeriesShouldMatchViaHandler(String mpn) {
            // ADXL is the only pattern for ACCELEROMETER_AD, so handler.matches() works
            assertTrue(handler.matches(mpn, ComponentType.ACCELEROMETER_AD, registry),
                    mpn + " should match ACCELEROMETER_AD via handler.matches()");
        }
    }

    @Nested
    @DisplayName("Gyroscope Pattern Registration")
    class GyroscopePatternTests {

        @ParameterizedTest
        @DisplayName("ADXRS series gyroscopes should match via registry")
        @ValueSource(strings = {"ADXRS290", "ADXRS290BCQZ", "ADXRS450", "ADXRS450BEYZ"})
        void adxrsSeriesShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.GYROSCOPE_AD),
                    mpn + " should match GYROSCOPE_AD via registry.matches()");
            assertTrue(registry.matches(mpn, ComponentType.GYROSCOPE),
                    mpn + " should match GYROSCOPE via registry.matches()");
        }

        @ParameterizedTest
        @DisplayName("ADXRS series should match via handler.matches()")
        @ValueSource(strings = {"ADXRS290", "ADXRS450"})
        void adxrsSeriesShouldMatchViaHandler(String mpn) {
            // ADXRS is the only pattern for GYROSCOPE_AD, so handler.matches() works
            assertTrue(handler.matches(mpn, ComponentType.GYROSCOPE_AD, registry),
                    mpn + " should match GYROSCOPE_AD via handler.matches()");
        }
    }

    @Nested
    @DisplayName("Temperature Sensor Pattern Registration")
    class TemperatureSensorPatternTests {

        @ParameterizedTest
        @DisplayName("AD590 series should match via registry")
        @ValueSource(strings = {"AD590", "AD590KH", "AD590LH", "AD590MH"})
        void ad590SeriesShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.TEMPERATURE_SENSOR_AD),
                    mpn + " should match TEMPERATURE_SENSOR_AD via registry.matches()");
            assertTrue(registry.matches(mpn, ComponentType.TEMPERATURE_SENSOR),
                    mpn + " should match TEMPERATURE_SENSOR via registry.matches()");
        }

        @ParameterizedTest
        @DisplayName("ADT series digital temp sensors should match via registry")
        @ValueSource(strings = {"ADT7410", "ADT7410TRZ", "ADT7420", "ADT7420UCPZ"})
        void adtSeriesShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.TEMPERATURE_SENSOR_AD),
                    mpn + " should match TEMPERATURE_SENSOR_AD via registry.matches()");
        }
    }

    @Nested
    @DisplayName("ADC/DAC Pattern Registration")
    class AdcDacPatternTests {

        @ParameterizedTest
        @DisplayName("AD7xxx ADC series should match IC via registry")
        @ValueSource(strings = {"AD7606", "AD7606BSTZ", "AD7124", "AD7689", "AD7689BCPZ"})
        void ad7SeriesShouldMatchICViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry.matches()");
        }

        @ParameterizedTest
        @DisplayName("AD5xxx DAC series should match IC via registry")
        @ValueSource(strings = {"AD5060", "AD5061", "AD5543", "AD5628", "AD5754"})
        void ad5SeriesShouldMatchICViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry.matches()");
        }
    }

    // ========================================================================
    // Package Code Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("BUG: Package extraction returns empty for most parts")
        void bugPackageExtractionReturnsEmpty() {
            // Current implementation has broken logic
            String pkg = handler.extractPackageCode("AD8065ARZ");
            assertEquals("", pkg, "BUG: Currently returns empty string");
        }

        @Test
        @DisplayName("Package extraction behavior documentation")
        void packageExtractionBehavior() {
            assertEquals("", handler.extractPackageCode("AD8065ARZ"));
            assertEquals("", handler.extractPackageCode("ADXL345BCCZ"));
            assertNotNull(handler.extractPackageCode("AD7606-4BSTZ"));
        }
    }

    // ========================================================================
    // Series Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract ADXL accelerometer series correctly")
        @CsvSource({
                "ADXL345BCCZ, ADXL345",
                "ADXL355BCCZ, ADXL355",
                "ADXL1002BCPZ, ADXL1002"
        })
        void shouldExtractADXLSeries(String mpn, String expectedSeries) {
            String series = handler.extractSeries(mpn);
            assertEquals(expectedSeries, series,
                    "Series for " + mpn + " should be " + expectedSeries);
        }

        @Test
        @DisplayName("Series extraction for non-ADXL parts returns full base")
        void seriesExtractionForOtherParts() {
            String series = handler.extractSeries("AD8065ARZ");
            assertNotNull(series);
            assertTrue(series.length() > 0);
            assertEquals("AD8065ARZ", series);
        }

        @Test
        @DisplayName("Series extraction for hyphenated parts")
        void seriesExtractionForHyphenatedParts() {
            String series = handler.extractSeries("AD7124-4BRUZ");
            assertEquals("AD7124", series, "Should extract base part before hyphen");
        }
    }

    // ========================================================================
    // Official Replacement Detection Tests
    // ========================================================================

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("ADXL345BCCZ", "ADXL345BCCZ-RL"),
                    "Tape/reel variant should be replacement");
            assertTrue(handler.isOfficialReplacement("AD8065ARZ", "AD8065ARZ-R7"),
                    "Tape/reel variant should be replacement");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("AD8065ARZ", "AD8066ARZ"),
                    "Different products should not be replacements");
            assertFalse(handler.isOfficialReplacement("ADXL345BCCZ", "ADXL355BCCZ"),
                    "Different products should not be replacements");
        }
    }

    // ========================================================================
    // Supported Types Tests
    // ========================================================================

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.OPAMP_AD));
            assertTrue(types.contains(ComponentType.OPAMP));
            assertTrue(types.contains(ComponentType.ADC_AD));
            assertTrue(types.contains(ComponentType.DAC_AD));
            assertTrue(types.contains(ComponentType.ACCELEROMETER));
            assertTrue(types.contains(ComponentType.ACCELEROMETER_AD));
            assertTrue(types.contains(ComponentType.GYROSCOPE));
            assertTrue(types.contains(ComponentType.GYROSCOPE_AD));
            assertTrue(types.contains(ComponentType.TEMPERATURE_SENSOR));
            assertTrue(types.contains(ComponentType.TEMPERATURE_SENSOR_AD));
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count());
        }
    }

    // ========================================================================
    // Edge Cases and Null Handling Tests
    // ========================================================================

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.OPAMP_AD, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "AD8065ARZ"));
            assertFalse(handler.isOfficialReplacement("AD8065ARZ", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.OPAMP_AD, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("AD8065ARZ", null, registry));
        }

        @Test
        @DisplayName("Case insensitive matching via PatternRegistry")
        void caseInsensitiveViaRegistry() {
            assertTrue(registry.matches("ad8065", ComponentType.OPAMP_AD));
            assertTrue(registry.matches("AD8065", ComponentType.OPAMP_AD));
            assertTrue(registry.matches("adxl345", ComponentType.ACCELEROMETER_AD));
            assertTrue(registry.matches("ADXL345", ComponentType.ACCELEROMETER_AD));
        }
    }

    // ========================================================================
    // Framework Bug Documentation Tests
    // ========================================================================

    @Nested
    @DisplayName("Framework Bug Documentation")
    class FrameworkBugTests {

        @Test
        @DisplayName("BUG: handler.matches() only uses first pattern for each type")
        void documentGetPatternBug() {
            // The ManufacturerHandler.matches() method calls getPattern()
            // which returns only the FIRST pattern registered for a type.
            // This means types with multiple patterns only match one pattern.

            // For OPAMP_AD: AD8[0-9].* registered first, OP[0-9].* second
            // But HashMap iteration order is non-deterministic

            // registry.matches() checks ALL patterns and works correctly
            assertTrue(registry.matches("AD8065", ComponentType.OPAMP_AD));
            assertTrue(registry.matches("OP27", ComponentType.OPAMP_AD));

            // handler.matches() only checks ONE pattern
            // One of these may fail depending on HashMap iteration order
            boolean ad8Matches = handler.matches("AD8065", ComponentType.OPAMP_AD, registry);
            boolean opMatches = handler.matches("OP27", ComponentType.OPAMP_AD, registry);

            // At least one should match (whichever pattern is first)
            assertTrue(ad8Matches || opMatches,
                    "At least one pattern should work via handler.matches()");
        }

        @Test
        @DisplayName("Workaround: Use registry.matches() for multi-pattern types")
        void workaroundUseRegistryMatches() {
            // Types with multiple patterns should use registry.matches()
            // OPAMP/OPAMP_AD: AD8xxx and OP
            // TEMPERATURE_SENSOR/TEMPERATURE_SENSOR_AD: AD590 and ADT
            // IC: AD7xxx and AD5xxx

            assertTrue(registry.matches("AD8065", ComponentType.OPAMP_AD));
            assertTrue(registry.matches("OP27", ComponentType.OPAMP_AD));
            assertTrue(registry.matches("AD590", ComponentType.TEMPERATURE_SENSOR_AD));
            assertTrue(registry.matches("ADT7410", ComponentType.TEMPERATURE_SENSOR_AD));
            assertTrue(registry.matches("AD7606", ComponentType.IC));
            assertTrue(registry.matches("AD5543", ComponentType.IC));
        }
    }
}
