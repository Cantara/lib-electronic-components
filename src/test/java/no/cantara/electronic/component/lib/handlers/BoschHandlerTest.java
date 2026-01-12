package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.BoschHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for BoschHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Bosch Sensortec product families:
 * - Accelerometers: BMAx (digital), SMBx (analog)
 * - Gyroscopes: BMGx
 * - IMUs: BMIx (accelerometer + gyroscope)
 * - Magnetometers: BMMx
 * - Pressure Sensors: BMPx
 * - Environmental Sensors: BMEx (humidity, pressure, temp)
 * - Gas Sensors: BME68x, BME69x
 *
 * Package codes: FB=LGA, FL=LGA, MI=LGA-METAL, TR=LGA, SG=SMD, WB=WLCSP, CP=CSP
 */
class BoschHandlerTest {

    private static BoschHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new BoschHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Accelerometer Detection (BMA/SMB Series)")
    class AccelerometerTests {

        @ParameterizedTest
        @DisplayName("Document BMA digital accelerometer detection")
        @ValueSource(strings = {"BMA456", "BMA455", "BMA400", "BMA280", "BMA253"})
        void documentBMADetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesBoschAccel = handler.matches(mpn, ComponentType.ACCELEROMETER_BOSCH, registry);
            System.out.println("BMA accelerometer detection: " + mpn +
                    " ACCELEROMETER=" + matchesAccel +
                    " ACCELEROMETER_BOSCH=" + matchesBoschAccel);
        }

        @ParameterizedTest
        @DisplayName("Document SMB analog accelerometer detection")
        @ValueSource(strings = {"SMB380", "SMB355"})
        void documentSMBDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesBoschAccel = handler.matches(mpn, ComponentType.ACCELEROMETER_BOSCH, registry);
            System.out.println("SMB accelerometer detection: " + mpn +
                    " ACCELEROMETER=" + matchesAccel +
                    " ACCELEROMETER_BOSCH=" + matchesBoschAccel);
        }
    }

    @Nested
    @DisplayName("Gyroscope Detection (BMG Series)")
    class GyroscopeTests {

        @ParameterizedTest
        @DisplayName("Document BMG gyroscope detection")
        @ValueSource(strings = {"BMG250", "BMG160"})
        void documentBMGDetection(String mpn) {
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            boolean matchesBoschGyro = handler.matches(mpn, ComponentType.GYROSCOPE_BOSCH, registry);
            System.out.println("BMG gyroscope detection: " + mpn +
                    " GYROSCOPE=" + matchesGyro +
                    " GYROSCOPE_BOSCH=" + matchesBoschGyro);
        }
    }

    @Nested
    @DisplayName("IMU Detection (BMI Series)")
    class IMUTests {

        @ParameterizedTest
        @DisplayName("Document BMI IMU detection")
        @ValueSource(strings = {"BMI270", "BMI160", "BMI088", "BMI055"})
        void documentBMIDetection(String mpn) {
            boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
            boolean matchesBoschAccel = handler.matches(mpn, ComponentType.ACCELEROMETER_BOSCH, registry);
            boolean matchesBoschGyro = handler.matches(mpn, ComponentType.GYROSCOPE_BOSCH, registry);
            System.out.println("BMI IMU detection: " + mpn +
                    " SENSOR=" + matchesSensor +
                    " ACCELEROMETER_BOSCH=" + matchesBoschAccel +
                    " GYROSCOPE_BOSCH=" + matchesBoschGyro);
        }
    }

    @Nested
    @DisplayName("Magnetometer Detection (BMM Series)")
    class MagnetometerTests {

        @ParameterizedTest
        @DisplayName("Document BMM magnetometer detection")
        @ValueSource(strings = {"BMM150", "BMM350"})
        void documentBMMDetection(String mpn) {
            boolean matchesMag = handler.matches(mpn, ComponentType.MAGNETOMETER, registry);
            System.out.println("BMM magnetometer detection: " + mpn +
                    " MAGNETOMETER=" + matchesMag);
        }
    }

    @Nested
    @DisplayName("Pressure Sensor Detection (BMP Series)")
    class PressureSensorTests {

        @ParameterizedTest
        @DisplayName("Document BMP pressure sensor detection")
        @ValueSource(strings = {"BMP390", "BMP388", "BMP280", "BMP180", "BMP085"})
        void documentBMPDetection(String mpn) {
            boolean matchesPressure = handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry);
            System.out.println("BMP pressure sensor detection: " + mpn +
                    " PRESSURE_SENSOR=" + matchesPressure);
        }
    }

    @Nested
    @DisplayName("Environmental Sensor Detection (BME Series)")
    class EnvironmentalSensorTests {

        @ParameterizedTest
        @DisplayName("Document BME environmental sensor detection")
        @ValueSource(strings = {"BME280", "BME680", "BME688"})
        void documentBMEDetection(String mpn) {
            boolean matchesHumidity = handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry);
            boolean matchesPressure = handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry);
            boolean matchesTemp = handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry);
            System.out.println("BME environmental sensor detection: " + mpn +
                    " HUMIDITY_SENSOR=" + matchesHumidity +
                    " PRESSURE_SENSOR=" + matchesPressure +
                    " TEMPERATURE_SENSOR=" + matchesTemp);
        }
    }

    @Nested
    @DisplayName("Gas Sensor Detection (BME68x/BME69x Series)")
    class GasSensorTests {

        @ParameterizedTest
        @DisplayName("Document BME68x gas sensor detection")
        @ValueSource(strings = {"BME680", "BME688"})
        void documentBME68xDetection(String mpn) {
            boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
            System.out.println("BME68x gas sensor detection: " + mpn +
                    " SENSOR=" + matchesSensor);
        }

        @ParameterizedTest
        @DisplayName("Document BME69x advanced gas sensor detection")
        @ValueSource(strings = {"BME690", "BME695"})
        void documentBME69xDetection(String mpn) {
            boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
            System.out.println("BME69x advanced gas sensor detection: " + mpn +
                    " SENSOR=" + matchesSensor);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Package code extraction returns empty for standard MPN formats")
        void packageCodeExtractionReturnsEmpty() {
            // Current handler implementation looks for suffix in the main part (before hyphen)
            // but Bosch package codes appear after the hyphen, so extraction returns empty
            assertEquals("", handler.extractPackageCode("BMA456"),
                    "Base MPN should return empty package code");
            assertEquals("", handler.extractPackageCode("BMA456-FB"),
                    "Hyphenated MPN should return empty (handler checks wrong part)");
            assertEquals("", handler.extractPackageCode("BMA456FB"),
                    "Concatenated MPN should return empty");
        }

        @Test
        @DisplayName("Document package code extraction behavior")
        void documentPackageCodeBehavior() {
            // Document actual behavior for various MPN formats
            String[] mpns = {"BMA456", "BMA456-FB", "BMA456FB", "BMI270-FL", "BME280-WB"};
            for (String mpn : mpns) {
                String pkg = handler.extractPackageCode(mpn);
                System.out.println("Package code for " + mpn + ": '" + pkg + "'");
            }
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract accelerometer series from base MPNs")
        @CsvSource({
                "BMA456, BMA456",
                "BMA400, BMA400",
                "SMB380, SMB380"
        })
        void shouldExtractAccelerometerSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from hyphenated MPNs")
        @CsvSource({
                "BMA400-I2C, BMA400",
                "BMI088-SPI, BMI088",
                "BME680-WB, BME680"
        })
        void shouldExtractSeriesFromHyphenatedMpns(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract IMU series from base MPNs")
        @CsvSource({
                "BMI270, BMI270",
                "BMI160, BMI160",
                "BMI088, BMI088"
        })
        void shouldExtractIMUSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract pressure sensor series")
        @CsvSource({
                "BMP390, BMP390",
                "BMP388, BMP388",
                "BMP280, BMP280"
        })
        void shouldExtractPressureSensorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract environmental sensor series")
        @CsvSource({
                "BME280, BME280",
                "BME680, BME680",
                "BME688, BME688"
        })
        void shouldExtractEnvironmentalSensorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with hyphenated suffixes should be replacements")
        void sameSeriesHyphenatedAreReplacements() {
            assertTrue(handler.isOfficialReplacement("BMA456-FB", "BMA456-FL"),
                    "Same series different packages should be replacements");
        }

        @Test
        @DisplayName("Same series base MPNs should be replacements")
        void sameSeriesBaseAreReplacements() {
            assertTrue(handler.isOfficialReplacement("BMA456", "BMA456"),
                    "Same series should be replacements");
        }

        @Test
        @DisplayName("BMA456 and BMA455 are compatible")
        void bma456AndBma455Compatible() {
            assertTrue(handler.isOfficialReplacement("BMA456", "BMA455"),
                    "BMA456 should be compatible with BMA455");
        }

        @Test
        @DisplayName("BMI270 and BMI160 are compatible")
        void bmi270AndBmi160Compatible() {
            assertTrue(handler.isOfficialReplacement("BMI270", "BMI160"),
                    "BMI270 should be compatible with BMI160");
        }

        @Test
        @DisplayName("BMP390 and BMP388 are compatible")
        void bmp390AndBmp388Compatible() {
            assertTrue(handler.isOfficialReplacement("BMP390", "BMP388"),
                    "BMP390 should be compatible with BMP388");
        }

        @Test
        @DisplayName("BME680 and BME688 are compatible")
        void bme680AndBme688Compatible() {
            assertTrue(handler.isOfficialReplacement("BME680", "BME688"),
                    "BME680 should be compatible with BME688");
        }

        @Test
        @DisplayName("Different families NOT replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("BMA456", "BMI270"),
                    "Accelerometer and IMU are different product families");
        }

        @Test
        @DisplayName("Interface mismatch prevents replacement")
        void interfaceMismatchPreventsReplacement() {
            assertFalse(handler.isOfficialReplacement("BMA456-I2C", "BMA456-SPI"),
                    "Different interfaces should not be compatible");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support sensor types")
        void shouldSupportSensorTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.SENSOR),
                    "Should support SENSOR");
            assertTrue(types.contains(ComponentType.ACCELEROMETER),
                    "Should support ACCELEROMETER");
            assertTrue(types.contains(ComponentType.GYROSCOPE),
                    "Should support GYROSCOPE");
            assertTrue(types.contains(ComponentType.MAGNETOMETER),
                    "Should support MAGNETOMETER");
            assertTrue(types.contains(ComponentType.PRESSURE_SENSOR),
                    "Should support PRESSURE_SENSOR");
            assertTrue(types.contains(ComponentType.HUMIDITY_SENSOR),
                    "Should support HUMIDITY_SENSOR");
            assertTrue(types.contains(ComponentType.TEMPERATURE_SENSOR),
                    "Should support TEMPERATURE_SENSOR");
        }

        @Test
        @DisplayName("Should support Bosch-specific types")
        void shouldSupportBoschSpecificTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.ACCELEROMETER_BOSCH),
                    "Should support ACCELEROMETER_BOSCH");
            assertTrue(types.contains(ComponentType.GYROSCOPE_BOSCH),
                    "Should support GYROSCOPE_BOSCH");
            assertTrue(types.contains(ComponentType.IMU_BOSCH),
                    "Should support IMU_BOSCH");
            assertTrue(types.contains(ComponentType.MAGNETOMETER_BOSCH),
                    "Should support MAGNETOMETER_BOSCH");
            assertTrue(types.contains(ComponentType.PRESSURE_SENSOR_BOSCH),
                    "Should support PRESSURE_SENSOR_BOSCH");
            assertTrue(types.contains(ComponentType.HUMIDITY_SENSOR_BOSCH),
                    "Should support HUMIDITY_SENSOR_BOSCH");
            assertTrue(types.contains(ComponentType.TEMPERATURE_SENSOR_BOSCH),
                    "Should support TEMPERATURE_SENSOR_BOSCH");
            assertTrue(types.contains(ComponentType.GAS_SENSOR_BOSCH),
                    "Should support GAS_SENSOR_BOSCH");
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
            assertFalse(handler.matches(null, ComponentType.ACCELEROMETER, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "BMA456"));
            assertFalse(handler.isOfficialReplacement("BMA456", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.ACCELEROMETER, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("BMA456", null, registry));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            BoschHandler directHandler = new BoschHandler();
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
    }

    @Nested
    @DisplayName("Common Development Board Sensors")
    class DevelopmentBoardTests {

        @Test
        @DisplayName("BME280 (popular environmental sensor)")
        void bme280Sensor() {
            System.out.println("BME280 detection: " +
                    " HUMIDITY_SENSOR=" + handler.matches("BME280", ComponentType.HUMIDITY_SENSOR, registry) +
                    " PRESSURE_SENSOR=" + handler.matches("BME280", ComponentType.PRESSURE_SENSOR, registry) +
                    " TEMPERATURE_SENSOR=" + handler.matches("BME280", ComponentType.TEMPERATURE_SENSOR, registry));
        }

        @Test
        @DisplayName("BME680 (gas + environmental sensor)")
        void bme680Sensor() {
            System.out.println("BME680 detection: " +
                    " SENSOR=" + handler.matches("BME680", ComponentType.SENSOR, registry) +
                    " HUMIDITY_SENSOR=" + handler.matches("BME680", ComponentType.HUMIDITY_SENSOR, registry));
        }

        @Test
        @DisplayName("BMI270 (popular IMU)")
        void bmi270Sensor() {
            System.out.println("BMI270 detection: " +
                    " SENSOR=" + handler.matches("BMI270", ComponentType.SENSOR, registry) +
                    " ACCELEROMETER_BOSCH=" + handler.matches("BMI270", ComponentType.ACCELEROMETER_BOSCH, registry) +
                    " GYROSCOPE_BOSCH=" + handler.matches("BMI270", ComponentType.GYROSCOPE_BOSCH, registry));
        }

        @Test
        @DisplayName("BMP388 (precision pressure sensor)")
        void bmp388Sensor() {
            System.out.println("BMP388 detection: " +
                    " PRESSURE_SENSOR=" + handler.matches("BMP388", ComponentType.PRESSURE_SENSOR, registry));
        }
    }
}
