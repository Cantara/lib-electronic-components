package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AllegroHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for AllegroHandler.
 *
 * Allegro MicroSystems specializes in:
 * - Current sensors (ACS7xx series)
 * - Motor drivers (A39xx, A49xx series)
 * - Hall effect sensors (A13xx series, AH prefix)
 * - LED drivers (A6xxx, A8xxx series)
 */
class AllegroHandlerTest {

    private static AllegroHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AllegroHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // Current Sensor Tests (ACS7xx series)
    // ========================================================================

    @Nested
    @DisplayName("Current Sensor Detection")
    class CurrentSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect ACS712 current sensors")
        @ValueSource(strings = {
            "ACS712ELCTR-05B-T",
            "ACS712ELCTR-20A-T",
            "ACS712ELCTR-30A-T",
            "ACS712"
        })
        void shouldDetectACS712(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_CURRENT, registry),
                    mpn + " should match SENSOR_CURRENT");
        }

        @ParameterizedTest
        @DisplayName("Should detect ACS723 current sensors")
        @ValueSource(strings = {
            "ACS723LLCTR-05AB-T",
            "ACS723LLCTR-10AB-T",
            "ACS723LLCTR-20AB-T",
            "ACS723LLCTR-40AB-T"
        })
        void shouldDetectACS723(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_CURRENT, registry),
                    mpn + " should match SENSOR_CURRENT");
        }

        @ParameterizedTest
        @DisplayName("Should detect ACS37612 coreless current sensors")
        @ValueSource(strings = {
            "ACS37612LLCATR-030B5-T",
            "ACS37612LLCATR-015B3-T"
        })
        void shouldDetectACS37612(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_CURRENT, registry),
                    mpn + " should match SENSOR_CURRENT");
        }

        @Test
        @DisplayName("Current sensors should also match IC type")
        void currentSensorsShouldMatchIC() {
            assertTrue(handler.matches("ACS712ELCTR-05B-T", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // Motor Driver Tests (A39xx, A49xx series)
    // ========================================================================

    @Nested
    @DisplayName("Motor Driver Detection")
    class MotorDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect A4988 stepper motor drivers")
        @ValueSource(strings = {
            "A4988SETTR-T",
            "A4988SETTR",
            "A4988"
        })
        void shouldDetectA4988(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect A3967 easy driver")
        @ValueSource(strings = {
            "A3967SLBT",
            "A3967SLB",
            "A3967"
        })
        void shouldDetectA3967(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect other motor drivers")
        @ValueSource(strings = {
            "A4950KLJTR-T",
            "A4953KLPTR-T",
            "A3977SEDTR-T",
            "A5984GLPTR-T"
        })
        void shouldDetectOtherMotorDrivers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @Test
        @DisplayName("Motor drivers should also match IC type")
        void motorDriversShouldMatchIC() {
            assertTrue(handler.matches("A4988SETTR-T", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // Hall Sensor Tests (A13xx series, AH prefix)
    // ========================================================================

    @Nested
    @DisplayName("Hall Sensor Detection")
    class HallSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect A13xx linear Hall sensors")
        @ValueSource(strings = {
            "A1324LLHLT-T",
            "A1324LUA-T",
            "A1325LLHLT-T",
            "A1326LLHLT-T",
            "A1301EUA-T",
            "A1302EUA-T"
        })
        void shouldDetectA13xxHallSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HALL_SENSOR, registry),
                    mpn + " should match HALL_SENSOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect AH series Hall switches")
        @ValueSource(strings = {
            "AH3362Q",
            "AH3366Q",
            "AH3369Q"
        })
        void shouldDetectAHSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HALL_SENSOR, registry),
                    mpn + " should match HALL_SENSOR");
        }

        @Test
        @DisplayName("Hall sensors should also match SENSOR type")
        void hallSensorsShouldMatchSensor() {
            assertTrue(handler.matches("A1324LLHLT-T", ComponentType.SENSOR, registry));
        }
    }

    // ========================================================================
    // LED Driver Tests (A6xxx, A8xxx series)
    // ========================================================================

    @Nested
    @DisplayName("LED Driver Detection")
    class LEDDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect A6xxx LED drivers")
        @ValueSource(strings = {
            "A6261KLJTR-T",
            "A6262KLJTR-T",
            "A6281EETTR"
        })
        void shouldDetectA6xxxLEDDrivers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect A80xxx LED drivers")
        @ValueSource(strings = {
            "A80601SETTR-T",
            "A80604SETTR-T",
            "A8061KLJTR-T"
        })
        void shouldDetectA8xxxLEDDrivers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
        }
    }

    // ========================================================================
    // Package Code Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract current sensor package codes")
        @CsvSource({
            "ACS712ELCTR-05B-T, SOIC-8",
            "ACS723LLCTR-05AB-T, SOIC-8"
        })
        void shouldExtractCurrentSensorPackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should extract enhanced package code (K suffix)")
        void shouldExtractEnhancedPackageCode() {
            // KLC = SOIC-8 with enhanced thermal pad
            assertEquals("SOIC-8-EP", handler.extractPackageCode("ACS723KLCTR-05AB-T"));
        }

        @ParameterizedTest
        @DisplayName("Should extract motor driver package codes")
        @CsvSource({
            "A4988SETTR-T, QFN",
            "A3967SLBT, SOIC-24"
        })
        void shouldExtractMotorDriverPackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Hall sensor package codes")
        @CsvSource({
            "A1324LUA-T, SIP-3",
            "A1324LLHLT-T, SOT-23"
        })
        void shouldExtractHallSensorPackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }
    }

    // ========================================================================
    // Series Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract current sensor series")
        @CsvSource({
            "ACS712ELCTR-05B-T, ACS712",
            "ACS723LLCTR-05AB-T, ACS723",
            "ACS37612LLCATR-030B5-T, ACS37612"
        })
        void shouldExtractCurrentSensorSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract motor driver series")
        @CsvSource({
            "A4988SETTR-T, A4988",
            "A3967SLBT, A3967",
            "A4950KLJTR-T, A4950"
        })
        void shouldExtractMotorDriverSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Hall sensor series")
        @CsvSource({
            "A1324LUA-T, A1324",
            "A1302EUA-T, A1302",
            "AH3362Q, AH3362"
        })
        void shouldExtractHallSensorSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract LED driver series")
        @CsvSource({
            "A6261KLJTR-T, A6261",
            "A80601SETTR-T, A80601"
        })
        void shouldExtractLEDDriverSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
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
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("ACS712ELCTR-05B-T", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("acs712elctr-05b-t", ComponentType.SENSOR_CURRENT, registry));
            assertTrue(handler.matches("a4988settr-t", ComponentType.MOTOR_DRIVER, registry));
        }
    }

    // ========================================================================
    // getSupportedTypes() Tests
    // ========================================================================

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should include all expected component types")
        void shouldIncludeAllExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.SENSOR_CURRENT), "Should support SENSOR_CURRENT");
            assertTrue(types.contains(ComponentType.MOTOR_DRIVER), "Should support MOTOR_DRIVER");
            assertTrue(types.contains(ComponentType.HALL_SENSOR), "Should support HALL_SENSOR");
            assertTrue(types.contains(ComponentType.LED_DRIVER), "Should support LED_DRIVER");
            assertTrue(types.contains(ComponentType.SENSOR), "Should support SENSOR");
            assertTrue(types.contains(ComponentType.IC), "Should support IC");
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
            // Same series, same package = replacement
            assertTrue(handler.isOfficialReplacement(
                    "ACS712ELCTR-05B-T",
                    "ACS712ELCTR-20A-T"),
                    "Same series ACS712 with same package should be replacements");
        }

        @Test
        @DisplayName("Different series should not be official replacement")
        void differentSeriesNotReplacement() {
            // Different series = not replacement
            assertFalse(handler.isOfficialReplacement(
                    "ACS712ELCTR-05B-T",
                    "ACS723LLCTR-05AB-T"),
                    "ACS712 and ACS723 are different series");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsNotReplacement() {
            assertFalse(handler.isOfficialReplacement(null, "ACS712ELCTR-05B-T"));
            assertFalse(handler.isOfficialReplacement("ACS712ELCTR-05B-T", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }
}
