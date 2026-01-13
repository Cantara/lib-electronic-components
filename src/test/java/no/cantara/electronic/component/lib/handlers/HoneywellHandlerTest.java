package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.HoneywellHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for HoneywellHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Honeywell Sensing product families:
 * - HIHxxxx - Humidity Sensors (HIH4000, HIH5030, HIH6130, HIH8120)
 * - HSCxxxx - TruStability Pressure Sensors (HSCDANN001PG2A3, HSCDRRN060MD)
 * - SSCxxxx - Standard Pressure Sensors (SSCMANV060PGAA5)
 * - ABPxxxx - Basic Pressure Sensors (ABPMANN060PG2A3)
 * - MPRxxxx - MicroPressure Sensors (MPRLS0025PA00001A)
 * - SS49x/SS59x - Hall Effect Linear Sensors (SS49E, SS495A, SS59ET)
 * - SS4xx/SS5xx - Hall Effect Switch Sensors (SS441A, SS451A, SS461A)
 * - HMCxxxx - Magnetometers (HMC5883L, HMC5883)
 * - HRS/HPX - Rotary Sensors
 * - HOA/HLC - Optical Sensors (HOA1180, HLC2705)
 */
class HoneywellHandlerTest {

    private static HoneywellHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new HoneywellHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("HIH Humidity Sensor Detection")
    class HIHHumiditySensorTests {

        @ParameterizedTest
        @DisplayName("Should detect HIH humidity sensors")
        @ValueSource(strings = {"HIH4000", "HIH5030", "HIH6130", "HIH6130-021-001", "HIH8120", "HIH8121"})
        void shouldDetectHIHHumiditySensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry),
                    mpn + " should match HUMIDITY_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @Test
        @DisplayName("HIH4000 series humidity sensor")
        void hih4000Series() {
            assertTrue(handler.matches("HIH4000", ComponentType.HUMIDITY_SENSOR, registry));
            assertTrue(handler.matches("HIH4010", ComponentType.HUMIDITY_SENSOR, registry));
        }

        @Test
        @DisplayName("HIH6130 with full part number")
        void hih6130FullPartNumber() {
            String mpn = "HIH6130-021-001";
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry));
        }

        @Test
        @DisplayName("HIH8120 digital humidity sensor")
        void hih8120Digital() {
            assertTrue(handler.matches("HIH8120", ComponentType.HUMIDITY_SENSOR, registry));
            assertTrue(handler.matches("HIH8121", ComponentType.HUMIDITY_SENSOR, registry));
        }
    }

    @Nested
    @DisplayName("HSC TruStability Pressure Sensor Detection")
    class HSCPressureSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect HSC TruStability pressure sensors")
        @ValueSource(strings = {"HSCDANN001PG2A3", "HSCDRRN060MD", "HSCDRRN030PGSA3", "HSCMAND060MDSA3"})
        void shouldDetectHSCPressureSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry),
                    mpn + " should match PRESSURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @Test
        @DisplayName("HSC differential pressure sensor")
        void hscDifferentialSensor() {
            assertTrue(handler.matches("HSCDRRN060MD", ComponentType.PRESSURE_SENSOR, registry));
        }

        @Test
        @DisplayName("HSC gauge pressure sensor")
        void hscGaugeSensor() {
            assertTrue(handler.matches("HSCDANN001PG2A3", ComponentType.PRESSURE_SENSOR, registry));
        }
    }

    @Nested
    @DisplayName("SSC Standard Pressure Sensor Detection")
    class SSCPressureSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect SSC standard pressure sensors")
        @ValueSource(strings = {"SSCMANV060PGAA5", "SSCMRRN060MDSA5", "SSCMAND015PGSA3"})
        void shouldDetectSSCPressureSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry),
                    mpn + " should match PRESSURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("ABP Basic Pressure Sensor Detection")
    class ABPPressureSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect ABP basic pressure sensors")
        @ValueSource(strings = {"ABPMANN060PG2A3", "ABPDRRN100MGAA5", "ABPMRRV060MG2A3"})
        void shouldDetectABPPressureSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry),
                    mpn + " should match PRESSURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("MPR MicroPressure Sensor Detection")
    class MPRPressureSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect MPR MicroPressure sensors")
        @ValueSource(strings = {"MPRLS0025PA00001A", "MPRLS0015PA0000SA", "MPRLS0300YG00001BB"})
        void shouldDetectMPRPressureSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry),
                    mpn + " should match PRESSURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("SS49x Hall Effect Linear Sensor Detection")
    class SS49xHallEffectLinearTests {

        @ParameterizedTest
        @DisplayName("Should detect SS49x Hall effect linear sensors")
        @ValueSource(strings = {"SS49E", "SS495A", "SS495A1", "SS496A", "SS496A1"})
        void shouldDetectSS49xHallSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HALL_SENSOR, registry),
                    mpn + " should match HALL_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @Test
        @DisplayName("SS49E basic linear Hall sensor")
        void ss49eBasic() {
            assertTrue(handler.matches("SS49E", ComponentType.HALL_SENSOR, registry));
        }

        @Test
        @DisplayName("SS495A precision linear Hall sensor")
        void ss495aPrecision() {
            assertTrue(handler.matches("SS495A", ComponentType.HALL_SENSOR, registry));
            assertTrue(handler.matches("SS495A1", ComponentType.HALL_SENSOR, registry));
        }
    }

    @Nested
    @DisplayName("SS59x Hall Effect Linear Sensor Detection")
    class SS59xHallEffectLinearTests {

        @ParameterizedTest
        @DisplayName("Should detect SS59x Hall effect linear sensors")
        @ValueSource(strings = {"SS59ET", "SS59E"})
        void shouldDetectSS59xHallSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HALL_SENSOR, registry),
                    mpn + " should match HALL_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("SS4xx Hall Effect Switch Sensor Detection")
    class SS4xxHallEffectSwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect SS4xx Hall effect switch sensors")
        @ValueSource(strings = {"SS441A", "SS451A", "SS461A", "SS411A", "SS413A"})
        void shouldDetectSS4xxHallSwitchSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HALL_SENSOR, registry),
                    mpn + " should match HALL_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @Test
        @DisplayName("SS441A bipolar latch")
        void ss441aBipolarLatch() {
            assertTrue(handler.matches("SS441A", ComponentType.HALL_SENSOR, registry));
        }

        @Test
        @DisplayName("SS451A unipolar switch")
        void ss451aUnipolarSwitch() {
            assertTrue(handler.matches("SS451A", ComponentType.HALL_SENSOR, registry));
        }
    }

    @Nested
    @DisplayName("HMC Magnetometer Detection")
    class HMCMagnetometerTests {

        @ParameterizedTest
        @DisplayName("Should detect HMC magnetometers")
        @ValueSource(strings = {"HMC5883L", "HMC5883", "HMC5883L-TR", "HMC5843"})
        void shouldDetectHMCMagnetometers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MAGNETOMETER, registry),
                    mpn + " should match MAGNETOMETER");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @Test
        @DisplayName("HMC5883L 3-axis digital compass")
        void hmc5883lDigitalCompass() {
            assertTrue(handler.matches("HMC5883L", ComponentType.MAGNETOMETER, registry));
            assertTrue(handler.matches("HMC5883L-TR", ComponentType.MAGNETOMETER, registry));
        }

        @Test
        @DisplayName("HMC5883 without L suffix")
        void hmc5883WithoutL() {
            assertTrue(handler.matches("HMC5883", ComponentType.MAGNETOMETER, registry));
        }
    }

    @Nested
    @DisplayName("Rotary Sensor Detection")
    class RotarySensorTests {

        @ParameterizedTest
        @DisplayName("Should detect HRS/HPX rotary sensors")
        @ValueSource(strings = {"HRS100", "HRS200", "HPX100"})
        void shouldDetectRotarySensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("Optical Sensor Detection")
    class OpticalSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect HOA optical sensors")
        @ValueSource(strings = {"HOA1180", "HOA1870", "HOA1404"})
        void shouldDetectHOAOpticalSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_OPTICAL, registry),
                    mpn + " should match SENSOR_OPTICAL");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect HLC optical sensors")
        @ValueSource(strings = {"HLC2705", "HLC2701"})
        void shouldDetectHLCOpticalSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_OPTICAL, registry),
                    mpn + " should match SENSOR_OPTICAL");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract pressure sensor package codes")
        @CsvSource({
            "HSCDANN001PG2A3, DIP",
            "SSCMANV060PGAA5, SMT",
            "HSCDRRN060MD, SMD"
        })
        void shouldExtractPressureSensorPackageCodes(String mpn, String expectedPkg) {
            assertEquals(expectedPkg, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract humidity sensor package codes from segment")
        @CsvSource({
            "HIH6130-021-001, SIP",
            "HIH6130-022-001, SMD",
            "HIH6130-031-001, SIP",
            "HIH6130-032-001, SMD"
        })
        void shouldExtractHumiditySensorPackageCodes(String mpn, String expectedPkg) {
            assertEquals(expectedPkg, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Hall sensor package codes")
        @CsvSource({
            "SS495A, TO-92",
            "SS495A1, TO-92",
            "SS49E, SOT-89"
        })
        void shouldExtractHallSensorPackageCodes(String mpn, String expectedPkg) {
            assertEquals(expectedPkg, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should extract HMC5883L package code")
        void shouldExtractMagnetometerPackageCode() {
            assertEquals("LCC", handler.extractPackageCode("HMC5883L"),
                    "HMC5883L should have LCC package");
        }

        @Test
        @DisplayName("Should return empty for unknown formats")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.extractPackageCode("HMC5883"),
                    "HMC5883 without L should return empty");
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract humidity sensor series")
        @CsvSource({
            "HIH4000, HIH4000",
            "HIH5030, HIH5030",
            "HIH6130, HIH6130",
            "HIH6130-021-001, HIH6130",
            "HIH8120, HIH8120"
        })
        void shouldExtractHumiditySensorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract pressure sensor series")
        @CsvSource({
            "HSCDANN001PG2A3, HSC",
            "SSCMANV060PGAA5, SSC",
            "ABPMANN060PG2A3, ABP",
            "MPRLS0025PA00001A, MPR"
        })
        void shouldExtractPressureSensorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Hall effect linear sensor series")
        @CsvSource({
            "SS49E, SS49",
            "SS495A, SS49",
            "SS496A1, SS49",
            "SS59ET, SS59"
        })
        void shouldExtractHallLinearSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Hall effect switch sensor series")
        @CsvSource({
            "SS441A, SS4",
            "SS451A, SS4",
            "SS461A, SS4"
        })
        void shouldExtractHallSwitchSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract magnetometer series")
        @CsvSource({
            "HMC5883L, HMC5883",
            "HMC5883, HMC5883",
            "HMC5843, HMC5843"
        })
        void shouldExtractMagnetometerSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract optical sensor series")
        @CsvSource({
            "HOA1180, HOA1180",
            "HLC2705, HLC2705"
        })
        void shouldExtractOpticalSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract rotary sensor series")
        @CsvSource({
            "HRS100, HRS",
            "HPX100, HPX"
        })
        void shouldExtractRotarySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same HIH series different packages are replacements")
        void sameHIHSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("HIH6130-021-001", "HIH6130-022-001"),
                    "Same HIH series different packages should be replacements");
        }

        @Test
        @DisplayName("HIH6130 and HIH6131 are compatible")
        void hih6130And6131Compatible() {
            assertTrue(handler.isOfficialReplacement("HIH6130", "HIH6131"),
                    "HIH6130 and HIH6131 should be compatible");
        }

        @Test
        @DisplayName("HIH8120 and HIH8121 are compatible")
        void hih8120And8121Compatible() {
            assertTrue(handler.isOfficialReplacement("HIH8120", "HIH8121"),
                    "HIH8120 and HIH8121 should be compatible");
        }

        @Test
        @DisplayName("HIH4000 and HIH4010 are compatible")
        void hih4000And4010Compatible() {
            assertTrue(handler.isOfficialReplacement("HIH4000", "HIH4010"),
                    "HIH4000 and HIH4010 should be compatible");
        }

        @Test
        @DisplayName("Same pressure sensor series same range are replacements")
        void samePressureSensorSeriesSameRangeAreReplacements() {
            assertTrue(handler.isOfficialReplacement("HSCDRRN060MDSA3", "HSCDRRN060MDSA5"),
                    "Same HSC series same range different packages should be replacements");
        }

        @Test
        @DisplayName("Same pressure sensor series different range are NOT replacements")
        void differentPressureRangeNotReplacements() {
            assertFalse(handler.isOfficialReplacement("HSCDANN001PG2A3", "HSCDANN060PG2A3"),
                    "Same HSC series different ranges should NOT be replacements");
        }

        @Test
        @DisplayName("Same SS49 Hall sensor family are replacements")
        void sameSS49FamilyAreReplacements() {
            assertTrue(handler.isOfficialReplacement("SS49E", "SS49E"),
                    "Same SS49 part should be replacement");
            assertTrue(handler.isOfficialReplacement("SS495A", "SS495A1"),
                    "SS495A variants should be replacements");
        }

        @Test
        @DisplayName("Same HMC magnetometer series are replacements")
        void sameHMCSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("HMC5883", "HMC5883L"),
                    "HMC5883 variants should be replacements");
            assertTrue(handler.isOfficialReplacement("HMC5883L", "HMC5883L-TR"),
                    "HMC5883L with tape-reel should be replacement");
        }

        @Test
        @DisplayName("Different families are NOT replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("HIH6130", "HSCDRRN060MD"),
                    "Humidity and pressure sensors should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("SS495A", "HMC5883L"),
                    "Hall sensor and magnetometer should NOT be replacements");
        }

        @Test
        @DisplayName("Null MPNs return false")
        void nullMpnsReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "HIH6130"));
            assertFalse(handler.isOfficialReplacement("HIH6130", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support core sensor types")
        void shouldSupportCoreSensorTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.SENSOR),
                    "Should support SENSOR");
            assertTrue(types.contains(ComponentType.HUMIDITY_SENSOR),
                    "Should support HUMIDITY_SENSOR");
            assertTrue(types.contains(ComponentType.PRESSURE_SENSOR),
                    "Should support PRESSURE_SENSOR");
            assertTrue(types.contains(ComponentType.HALL_SENSOR),
                    "Should support HALL_SENSOR");
            assertTrue(types.contains(ComponentType.MAGNETOMETER),
                    "Should support MAGNETOMETER");
            assertTrue(types.contains(ComponentType.SENSOR_OPTICAL),
                    "Should support SENSOR_OPTICAL");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        @DisplayName("getSupportedTypes returns immutable Set.of()")
        void supportedTypesIsImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.IC);
            }, "Set.of() should be immutable");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.HUMIDITY_SENSOR, registry));
            assertFalse(handler.matches(null, ComponentType.PRESSURE_SENSOR, registry));
            assertFalse(handler.matches(null, ComponentType.HALL_SENSOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "HIH6130"));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.HUMIDITY_SENSOR, registry));
            assertFalse(handler.matches("", ComponentType.PRESSURE_SENSOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("HIH6130", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("hih6130", ComponentType.HUMIDITY_SENSOR, registry),
                    "Should match lowercase HIH MPN");
            assertTrue(handler.matches("hmc5883l", ComponentType.MAGNETOMETER, registry),
                    "Should match lowercase HMC MPN");
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Hih6130", ComponentType.HUMIDITY_SENSOR, registry));
            assertTrue(handler.matches("Hsc" + "DANN001PG2A3".toLowerCase(), ComponentType.PRESSURE_SENSOR, registry));
        }

        @Test
        @DisplayName("Should not match non-Honeywell parts")
        void shouldNotMatchNonHoneywellParts() {
            assertFalse(handler.matches("BME280", ComponentType.HUMIDITY_SENSOR, registry),
                    "Bosch sensor should not match");
            assertFalse(handler.matches("SHT31", ComponentType.HUMIDITY_SENSOR, registry),
                    "Sensirion sensor should not match");
            assertFalse(handler.matches("LM35", ComponentType.SENSOR, registry),
                    "TI sensor should not match");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            HoneywellHandler directHandler = new HoneywellHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes());
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }

        @Test
        @DisplayName("Multiple handler instances are independent")
        void multipleInstancesAreIndependent() {
            HoneywellHandler handler1 = new HoneywellHandler();
            HoneywellHandler handler2 = new HoneywellHandler();

            PatternRegistry registry1 = new PatternRegistry();
            PatternRegistry registry2 = new PatternRegistry();

            handler1.initializePatterns(registry1);
            handler2.initializePatterns(registry2);

            // Both should work independently
            assertTrue(handler1.matches("HIH6130", ComponentType.HUMIDITY_SENSOR, registry1));
            assertTrue(handler2.matches("HIH6130", ComponentType.HUMIDITY_SENSOR, registry2));
        }
    }

    @Nested
    @DisplayName("Popular Development Board Sensors")
    class DevelopmentBoardTests {

        @Test
        @DisplayName("HMC5883L (popular magnetometer for drones/robots)")
        void hmc5883lMagnetometer() {
            assertTrue(handler.matches("HMC5883L", ComponentType.MAGNETOMETER, registry));
            assertTrue(handler.matches("HMC5883L-TR", ComponentType.MAGNETOMETER, registry));
            assertEquals("HMC5883", handler.extractSeries("HMC5883L"));
        }

        @Test
        @DisplayName("SS49E (popular linear Hall sensor)")
        void ss49eHallSensor() {
            assertTrue(handler.matches("SS49E", ComponentType.HALL_SENSOR, registry));
            assertEquals("SS49", handler.extractSeries("SS49E"));
        }

        @Test
        @DisplayName("HIH6130 (I2C humidity/temp sensor)")
        void hih6130HumiditySensor() {
            assertTrue(handler.matches("HIH6130-021-001", ComponentType.HUMIDITY_SENSOR, registry));
            assertEquals("HIH6130", handler.extractSeries("HIH6130-021-001"));
            assertEquals("SIP", handler.extractPackageCode("HIH6130-021-001"));
        }

        @Test
        @DisplayName("ABP pressure sensor for pneumatics projects")
        void abpPressureSensor() {
            assertTrue(handler.matches("ABPMANN060PG2A3", ComponentType.PRESSURE_SENSOR, registry));
            assertEquals("ABP", handler.extractSeries("ABPMANN060PG2A3"));
        }
    }

    @Nested
    @DisplayName("Pressure Range Extraction Tests")
    class PressureRangeTests {

        @Test
        @DisplayName("Same series same range should be compatible")
        void sameSameSeriesSameRange() {
            // Both are 060 range
            assertTrue(handler.isOfficialReplacement("HSCDRRN060MDSA3", "HSCDRRN060MDSA5"));
        }

        @Test
        @DisplayName("Same series different range should NOT be compatible")
        void sameSeriesDifferentRange() {
            // 001 vs 060 range
            assertFalse(handler.isOfficialReplacement("HSCDANN001PG2A3", "HSCDANN060PG2A3"));
        }

        @Test
        @DisplayName("MPR sensors same range should be compatible")
        void mprSamRange() {
            // Both 0025 range
            assertTrue(handler.isOfficialReplacement("MPRLS0025PA00001A", "MPRLS0025PA00001B"));
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldPartNumberTests {

        @ParameterizedTest
        @DisplayName("Should detect real Honeywell humidity sensor part numbers")
        @ValueSource(strings = {
            "HIH4030-003", "HIH4021-003", "HIH4000-001",
            "HIH5030-001", "HIH5031-001",
            "HIH6130-021-001", "HIH6130-021-301", "HIH6131-021-001",
            "HIH8120-021-001", "HIH8121-021-001"
        })
        void realHumiditySensorParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry),
                    "Real humidity sensor " + mpn + " should be detected");
        }

        @ParameterizedTest
        @DisplayName("Should detect real Honeywell pressure sensor part numbers")
        @ValueSource(strings = {
            "HSCDANN001PG2A3", "HSCDRRN030PGSA3", "HSCDRRN060MDSA3",
            "SSCMANV060PGAA5", "SSCMRRN100PGSA5",
            "ABPMANN060PG2A3", "ABPDRRN100MGAA5"
        })
        void realPressureSensorParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry),
                    "Real pressure sensor " + mpn + " should be detected");
        }

        @ParameterizedTest
        @DisplayName("Should detect real Honeywell Hall sensor part numbers")
        @ValueSource(strings = {
            "SS49E", "SS495A", "SS495A1", "SS496A", "SS496A1",
            "SS441A", "SS451A", "SS461A", "SS411A", "SS413A"
        })
        void realHallSensorParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HALL_SENSOR, registry),
                    "Real Hall sensor " + mpn + " should be detected");
        }
    }
}
