package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.MPNUtils;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.TIHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for TIHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class TIHandlerTest {

    private static TIHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Get handler through MPNUtils to ensure proper initialization
        ManufacturerHandler h = MPNUtils.getManufacturerHandler("LM358");
        assertNotNull(h, "Should find TI handler for LM358");
        assertTrue(h instanceof TIHandler, "Handler should be TIHandler");
        handler = (TIHandler) h;

        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Op-Amp Detection")
    class OpAmpTests {

        @ParameterizedTest
        @DisplayName("Should detect LM358 variants as OPAMP_TI")
        @ValueSource(strings = {"LM358", "LM358N", "LM358D", "LM358AN", "LM358AD", "LM358PW", "LM358DGK"})
        void shouldDetectLM358Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP_TI, registry),
                    mpn + " should match OPAMP_TI");
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect LM324 variants as OPAMP_TI")
        @ValueSource(strings = {"LM324", "LM324N", "LM324D", "LM324AN", "LM324PW"})
        void shouldDetectLM324Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP_TI, registry),
                    mpn + " should match OPAMP_TI");
        }

        @ParameterizedTest
        @DisplayName("Should detect TL07x JFET op-amps")
        @ValueSource(strings = {"TL072", "TL072N", "TL072D", "TL074", "TL074N", "TL074D"})
        void shouldDetectTL07xVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP_TI, registry),
                    mpn + " should match OPAMP_TI");
        }

        @ParameterizedTest
        @DisplayName("Should detect NE5532 low-noise op-amp")
        @ValueSource(strings = {"NE5532", "NE5532N", "NE5532D", "NE5532PW"})
        void shouldDetectNE5532Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP_TI, registry),
                    mpn + " should match OPAMP_TI");
        }

        @Test
        @DisplayName("Should detect LM311 comparator as OPAMP_TI")
        void shouldDetectLM311() {
            assertTrue(handler.matches("LM311", ComponentType.OPAMP_TI, registry));
            assertTrue(handler.matches("LM311N", ComponentType.OPAMP_TI, registry));
            assertTrue(handler.matches("LM311D", ComponentType.OPAMP_TI, registry));
        }
    }

    @Nested
    @DisplayName("Voltage Regulator Detection")
    class VoltageRegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect 78xx positive regulators")
        @CsvSource({
                "LM7805, VOLTAGE_REGULATOR_LINEAR_TI",
                "LM7805CT, VOLTAGE_REGULATOR_LINEAR_TI",
                "LM7812, VOLTAGE_REGULATOR_LINEAR_TI",
                "LM7812CT, VOLTAGE_REGULATOR_LINEAR_TI",
                "UA7805, VOLTAGE_REGULATOR_LINEAR_TI",
                "LM7815CT, VOLTAGE_REGULATOR_LINEAR_TI",
                "LM7824CT, VOLTAGE_REGULATOR_LINEAR_TI"
        })
        void shouldDetect78xxRegulators(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect 79xx negative regulators")
        @ValueSource(strings = {"LM7905", "LM7905CT", "LM7912", "LM7912CT", "UA7905"})
        void shouldDetect79xxRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_TI, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_TI");
        }

        @ParameterizedTest
        @DisplayName("Should detect adjustable regulators (LM317, LM350, LM338)")
        @ValueSource(strings = {"LM317", "LM317T", "LM317K", "LM350", "LM350T", "LM338", "LM338T"})
        void shouldDetectAdjustableRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_TI, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_TI");
        }

        @Test
        @DisplayName("Should detect TL431 voltage reference")
        void shouldDetectTL431() {
            assertTrue(handler.matches("TL431", ComponentType.VOLTAGE_REGULATOR_LINEAR_TI, registry));
            assertTrue(handler.matches("TL431A", ComponentType.VOLTAGE_REGULATOR_LINEAR_TI, registry));
            assertTrue(handler.matches("TL431ALP", ComponentType.VOLTAGE_REGULATOR_LINEAR_TI, registry));
        }

        @Test
        @DisplayName("Should detect TPS LDO regulators")
        void shouldDetectTPSRegulators() {
            assertTrue(handler.matches("TPS7350", ComponentType.VOLTAGE_REGULATOR_LINEAR_TI, registry));
        }
    }

    @Nested
    @DisplayName("Temperature Sensor Detection")
    class TemperatureSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect LM35 temperature sensors")
        @ValueSource(strings = {"LM35DZ", "LM35CZ", "LM35AZ", "LM35BZ", "LM35DM"})
        void shouldDetectLM35Sensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR_TI, registry),
                    mpn + " should match TEMPERATURE_SENSOR_TI");
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry),
                    mpn + " should match TEMPERATURE_SENSOR (base type)");
        }

        @Test
        @DisplayName("LM35 should NOT match as OPAMP (critical distinction from LM358)")
        void lm35ShouldNotMatchOpAmp() {
            assertFalse(handler.matches("LM35DZ", ComponentType.OPAMP_TI, registry),
                    "LM35DZ should NOT match OPAMP_TI");
            assertFalse(handler.matches("LM35CZ", ComponentType.OPAMP_TI, registry),
                    "LM35CZ should NOT match OPAMP_TI");
        }

        @Test
        @DisplayName("LM358 should NOT match as temperature sensor")
        void lm358ShouldNotMatchTempSensor() {
            assertFalse(handler.matches("LM358", ComponentType.TEMPERATURE_SENSOR_TI, registry),
                    "LM358 should NOT match TEMPERATURE_SENSOR_TI");
            assertFalse(handler.matches("LM358N", ComponentType.TEMPERATURE_SENSOR_TI, registry),
                    "LM358N should NOT match TEMPERATURE_SENSOR_TI");
        }
    }

    @Nested
    @DisplayName("LM35 vs LM358 Conflict Resolution")
    class LM35vsLM358Tests {

        @Test
        @DisplayName("LM35D should be temperature sensor, not op-amp")
        void lm35dIsTemperatureSensor() {
            assertTrue(handler.matches("LM35DZ", ComponentType.TEMPERATURE_SENSOR_TI, registry));
            assertFalse(handler.matches("LM35DZ", ComponentType.OPAMP_TI, registry));
        }

        @Test
        @DisplayName("LM358 should be op-amp, not temperature sensor")
        void lm358IsOpAmp() {
            assertTrue(handler.matches("LM358", ComponentType.OPAMP_TI, registry));
            assertFalse(handler.matches("LM358", ComponentType.TEMPERATURE_SENSOR_TI, registry));
        }

        @Test
        @DisplayName("Pattern distinguishes LM35+letter from LM35+digit")
        void patternDistinguishesCorrectly() {
            // LM35 followed by letter A-D = temperature sensor
            assertTrue(handler.matches("LM35A", ComponentType.TEMPERATURE_SENSOR_TI, registry));
            assertTrue(handler.matches("LM35B", ComponentType.TEMPERATURE_SENSOR_TI, registry));
            assertTrue(handler.matches("LM35C", ComponentType.TEMPERATURE_SENSOR_TI, registry));
            assertTrue(handler.matches("LM35D", ComponentType.TEMPERATURE_SENSOR_TI, registry));

            // LM35 followed by digit 8 = LM358 op-amp
            assertTrue(handler.matches("LM358", ComponentType.OPAMP_TI, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract op-amp package codes")
        @CsvSource({
                "LM358N, DIP",
                "LM358D, SOIC",
                "LM358PW, TSSOP",
                "LM358DGK, MSOP"
        })
        void shouldExtractOpAmpPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract voltage regulator package codes")
        @CsvSource({
                "LM7805CT, TO-220",
                "LM7805T, TO-220",
                "LM7805KC, TO-252",
                "LM7805MP, SOT-223",
                "LM7805DT, SOT-223"
        })
        void shouldExtractRegulatorPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract temperature sensor package codes")
        @CsvSource({
                "LM35DZ, TO-92",
                "LM35CZ, TO-92"
        })
        void shouldExtractTempSensorPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract op-amp series")
        @CsvSource({
                "LM358, LM358",
                "LM358N, LM358",
                "LM358AN, LM358",
                "LM324, LM324",
                "LM324N, LM324",
                "TL072, TL072",
                "TL072N, TL072"
        })
        void shouldExtractOpAmpSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract voltage regulator series")
        @CsvSource({
                "LM7805, LM7805",
                "LM7805CT, LM7805",
                "LM7812, LM7812",
                "LM7812CT, LM7812",
                "LM317, LM317",
                "LM317T, LM317"
        })
        void shouldExtractRegulatorSeries(String mpn, String expectedSeries) {
            String series = handler.extractSeries(mpn);
            assertTrue(series.startsWith(expectedSeries),
                    "Series for " + mpn + " should start with " + expectedSeries + " but was " + series);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series different packages should be replacements")
        void sameSeriesDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("LM358N", "LM358D"),
                    "LM358N and LM358D should be replacements");
            assertTrue(handler.isOfficialReplacement("LM7805CT", "LM7805KC"),
                    "LM7805CT and LM7805KC should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("LM358", "LM324"),
                    "LM358 and LM324 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("LM7805", "LM7812"),
                    "LM7805 and LM7812 should NOT be replacements (different voltage)");
        }

        @Test
        @DisplayName("Equivalent series should be replacements")
        void equivalentSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("LM358N", "LM358D"),
                    "Same series with different package should be replacement");
        }
    }

    @Nested
    @DisplayName("LED Detection")
    class LEDTests {

        @ParameterizedTest
        @DisplayName("Should detect TI LED series")
        @ValueSource(strings = {"TLHR5400", "TLHG5800", "TLHB5800"})
        void shouldDetectLEDSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_TI, registry),
                    mpn + " should match LED_TI");
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED (base type)");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.OPAMP_TI));
            assertTrue(types.contains(ComponentType.OPAMP));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR_LINEAR_TI));
            assertTrue(types.contains(ComponentType.TEMPERATURE_SENSOR));
            assertTrue(types.contains(ComponentType.TEMPERATURE_SENSOR_TI));
            assertTrue(types.contains(ComponentType.LED));
            assertTrue(types.contains(ComponentType.LED_TI));
            assertTrue(types.contains(ComponentType.MICROCONTROLLER_TI));
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
            assertFalse(handler.matches(null, ComponentType.OPAMP_TI, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "LM358"));
            assertFalse(handler.isOfficialReplacement("LM358", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.OPAMP_TI, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("LM358", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("lm358", ComponentType.OPAMP_TI, registry));
            assertTrue(handler.matches("LM358", ComponentType.OPAMP_TI, registry));
            assertTrue(handler.matches("Lm358", ComponentType.OPAMP_TI, registry));
        }
    }
}
