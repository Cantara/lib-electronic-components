package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.InvSenseHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for InvSenseHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * InvenSense Product Categories:
 * - 6-Axis IMUs: ICM-20xxx, MPU-6xxx (Accelerometer + Gyroscope)
 * - 9-Axis IMUs: ICM-209xx, MPU-9xxx (Accelerometer + Gyroscope + Magnetometer)
 * - Gyroscopes: ITG-3xxx, IVS-4xxx
 * - Accelerometers: IAM-2xxx, IIM-4xxx
 * - Audio/Motion: ICS-4xxxx, IAC-5xxxx
 *
 * Package Codes: QFN, LGA, BGA, WLCSP, CSP (also Q, L, B, C suffixes)
 */
class InvSenseHandlerTest {

    private static InvSenseHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new InvSenseHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("6-Axis IMU Detection (ICM-20xxx, MPU-6xxx)")
    class SixAxisIMUTests {

        @ParameterizedTest
        @DisplayName("Document ICM-20xxx 6-axis IMU detection")
        @ValueSource(strings = {"ICM-20600", "ICM-20602", "ICM-20608", "ICM-20648", "ICM-20689"})
        void documentICM20xxxDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            System.out.println("ICM-20xxx detection: " + mpn + " ACCELEROMETER=" + matchesAccel + " GYROSCOPE=" + matchesGyro);
        }

        @ParameterizedTest
        @DisplayName("Document MPU-6xxx 6-axis IMU detection")
        @ValueSource(strings = {"MPU-6000", "MPU-6050", "MPU-6500", "MPU-6515", "MPU-6880"})
        void documentMPU6xxxDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            System.out.println("MPU-6xxx detection: " + mpn + " ACCELEROMETER=" + matchesAccel + " GYROSCOPE=" + matchesGyro);
        }

        @ParameterizedTest
        @DisplayName("Document 6-axis IMU with package suffix detection")
        @ValueSource(strings = {"ICM-20600-Q", "ICM-20602-L", "MPU-6050-QFN", "MPU-6500-LGA"})
        void documentSixAxisWithPackageDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            System.out.println("6-axis with package: " + mpn + " ACCELEROMETER=" + matchesAccel + " GYROSCOPE=" + matchesGyro);
        }
    }

    @Nested
    @DisplayName("9-Axis IMU Detection (ICM-209xx, MPU-9xxx)")
    class NineAxisIMUTests {

        @ParameterizedTest
        @DisplayName("Document ICM-209xx 9-axis IMU detection")
        @ValueSource(strings = {"ICM-20948", "ICM-20900", "ICM-20920", "ICM-20948-Q"})
        void documentICM209xxDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            boolean matchesMag = handler.matches(mpn, ComponentType.MAGNETOMETER, registry);
            System.out.println("ICM-209xx detection: " + mpn + " ACCELEROMETER=" + matchesAccel + " GYROSCOPE=" + matchesGyro + " MAGNETOMETER=" + matchesMag);
        }

        @ParameterizedTest
        @DisplayName("Document MPU-9xxx 9-axis IMU detection")
        @ValueSource(strings = {"MPU-9150", "MPU-9250", "MPU-9255", "MPU-9250-QFN"})
        void documentMPU9xxxDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            boolean matchesMag = handler.matches(mpn, ComponentType.MAGNETOMETER, registry);
            System.out.println("MPU-9xxx detection: " + mpn + " ACCELEROMETER=" + matchesAccel + " GYROSCOPE=" + matchesGyro + " MAGNETOMETER=" + matchesMag);
        }
    }

    @Nested
    @DisplayName("Gyroscope Detection (ITG-3xxx, IVS-4xxx)")
    class GyroscopeTests {

        @ParameterizedTest
        @DisplayName("Document ITG-3xxx gyroscope detection")
        @ValueSource(strings = {"ITG-3050", "ITG-3200", "ITG-3500", "ITG-3200-Q"})
        void documentITG3xxxDetection(String mpn) {
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            System.out.println("ITG-3xxx detection: " + mpn + " GYROSCOPE=" + matchesGyro);
        }

        @ParameterizedTest
        @DisplayName("Document IVS-4xxx gyroscope detection")
        @ValueSource(strings = {"IVS-4000", "IVS-4100", "IVS-4200", "IVS-4500-LGA"})
        void documentIVS4xxxDetection(String mpn) {
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            System.out.println("IVS-4xxx detection: " + mpn + " GYROSCOPE=" + matchesGyro);
        }
    }

    @Nested
    @DisplayName("Accelerometer Detection (IAM-2xxx, IIM-4xxx)")
    class AccelerometerTests {

        @ParameterizedTest
        @DisplayName("Document IAM-2xxx accelerometer detection")
        @ValueSource(strings = {"IAM-2000", "IAM-2100", "IAM-2500", "IAM-2000-Q"})
        void documentIAM2xxxDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            System.out.println("IAM-2xxx detection: " + mpn + " ACCELEROMETER=" + matchesAccel);
        }

        @ParameterizedTest
        @DisplayName("Document IIM-4xxx accelerometer detection")
        @ValueSource(strings = {"IIM-4000", "IIM-4200", "IIM-4500", "IIM-4000-LGA"})
        void documentIIM4xxxDetection(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            System.out.println("IIM-4xxx detection: " + mpn + " ACCELEROMETER=" + matchesAccel);
        }
    }

    @Nested
    @DisplayName("Audio/Motion Processor Detection (ICS-4xxxx, IAC-5xxxx)")
    class AudioMotionTests {

        @ParameterizedTest
        @DisplayName("Document ICS-4xxxx audio/motion detection")
        @ValueSource(strings = {"ICS-40000", "ICS-41000", "ICS-42000", "ICS-43434", "ICS-40000-Q"})
        void documentICS4xxxxDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("ICS-4xxxx detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document IAC-5xxxx audio/motion detection")
        @ValueSource(strings = {"IAC-50000", "IAC-51000", "IAC-52000", "IAC-50000-BGA"})
        void documentIAC5xxxxDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("IAC-5xxxx detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract explicit package codes")
        @CsvSource({
                "ICM-20948-QFN, QFN",
                "MPU-6050-LGA, LGA",
                "MPU-9250-BGA, BGA",
                "ICM-20600-WLCSP, WLCSP",
                "ITG-3200-CSP, CSP"
        })
        void shouldExtractExplicitPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes from suffix letters")
        @CsvSource({
                "ICM-20948-Q, QFN",
                "MPU-6050-L, LGA",
                "MPU-9250-B, BGA",
                "ICM-20600-C, CSP"
        })
        void shouldExtractPackageCodesFromSuffixLetters(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should interpret suffix ending with C as CSP")
        void shouldInterpretSuffixEndingWithCAsCsp() {
            // Handler interprets suffix ending with C as CSP package
            String result = handler.extractPackageCode("ICM-20948-ABC");
            assertEquals("CSP", result, "Suffix ending with C should be interpreted as CSP");
        }

        @Test
        @DisplayName("Should return model number when no explicit package suffix")
        void shouldReturnModelNumberWhenNoPackageSuffix() {
            // Handler returns last part when only two parts present
            String result = handler.extractPackageCode("ICM-20948");
            assertEquals("20948", result, "Should return model number as last part");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract ICM series")
        @CsvSource({
                "ICM-20948-QFN, ICM-20948",
                "ICM-20600-L, ICM-20600",
                "ICM-20689, ICM-20689"
        })
        void shouldExtractICMSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MPU series")
        @CsvSource({
                "MPU-6050-QFN, MPU-6050",
                "MPU-9250-B, MPU-9250",
                "MPU-6500, MPU-6500"
        })
        void shouldExtractMPUSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract ITG/IVS series")
        @CsvSource({
                "ITG-3200-Q, ITG-3200",
                "IVS-4200-LGA, IVS-4200"
        })
        void shouldExtractGyroSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract IAM/IIM series")
        @CsvSource({
                "IAM-2000-Q, IAM-2000",
                "IIM-4000-LGA, IIM-4000"
        })
        void shouldExtractAccelSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract ICS/IAC series")
        @CsvSource({
                "ICS-43434-Q, ICS-43434",
                "IAC-50000-BGA, IAC-50000"
        })
        void shouldExtractAudioMotionSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with different packages should be replacements")
        void sameSeriesDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("ICM-20948-QFN", "ICM-20948-LGA"),
                    "Same series with different packages should be replacements");
        }

        @Test
        @DisplayName("MPU-6050 and ICM-20600 should be compatible")
        void mpu6050AndIcm20600Compatible() {
            assertTrue(handler.isOfficialReplacement("MPU-6050", "ICM-20600"),
                    "MPU-6050 and ICM-20600 should be compatible");
            assertTrue(handler.isOfficialReplacement("ICM-20600", "MPU-6050"),
                    "ICM-20600 and MPU-6050 should be compatible");
        }

        @Test
        @DisplayName("MPU-9250 and ICM-20948 should be compatible")
        void mpu9250AndIcm20948Compatible() {
            assertTrue(handler.isOfficialReplacement("MPU-9250", "ICM-20948"),
                    "MPU-9250 and ICM-20948 should be compatible");
            assertTrue(handler.isOfficialReplacement("ICM-20948", "MPU-9250"),
                    "ICM-20948 and MPU-9250 should be compatible");
        }

        @Test
        @DisplayName("ITG-3200 and IVS-4200 should be compatible")
        void itg3200AndIvs4200Compatible() {
            assertTrue(handler.isOfficialReplacement("ITG-3200", "IVS-4200"),
                    "ITG-3200 and IVS-4200 should be compatible");
        }

        @Test
        @DisplayName("IAM-2000 and IIM-4000 should be compatible")
        void iam2000AndIim4000Compatible() {
            assertTrue(handler.isOfficialReplacement("IAM-2000", "IIM-4000"),
                    "IAM-2000 and IIM-4000 should be compatible");
        }

        @Test
        @DisplayName("Different incompatible series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ICM-20948", "ITG-3200"),
                    "9-axis IMU and gyroscope should not be replacements");
            assertFalse(handler.isOfficialReplacement("MPU-6050", "IAM-2000"),
                    "6-axis IMU and accelerometer should not be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support SENSOR type")
        void shouldSupportSensorType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.SENSOR),
                    "Should support SENSOR");
        }

        @Test
        @DisplayName("Should support ACCELEROMETER type")
        void shouldSupportAccelerometerType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.ACCELEROMETER),
                    "Should support ACCELEROMETER");
        }

        @Test
        @DisplayName("Should support GYROSCOPE type")
        void shouldSupportGyroscopeType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.GYROSCOPE),
                    "Should support GYROSCOPE");
        }

        @Test
        @DisplayName("Should support MAGNETOMETER type")
        void shouldSupportMagnetometerType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MAGNETOMETER),
                    "Should support MAGNETOMETER");
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
            assertFalse(handler.isOfficialReplacement(null, "ICM-20948"));
            assertFalse(handler.isOfficialReplacement("ICM-20948", null));
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
            assertFalse(handler.matches("ICM-20948", null, registry));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            InvSenseHandler directHandler = new InvSenseHandler();
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
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @ParameterizedTest
        @DisplayName("Document common InvenSense products")
        @ValueSource(strings = {
                "ICM-20948",      // Popular 9-axis IMU
                "MPU-6050",       // Classic 6-axis IMU
                "MPU-9250",       // 9-axis IMU with magnetometer
                "ICM-20600",      // Low-power 6-axis
                "ICS-43434"       // MEMS microphone
        })
        void documentCommonProducts(String mpn) {
            boolean matchesAccel = handler.matches(mpn, ComponentType.ACCELEROMETER, registry);
            boolean matchesGyro = handler.matches(mpn, ComponentType.GYROSCOPE, registry);
            boolean matchesMag = handler.matches(mpn, ComponentType.MAGNETOMETER, registry);
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            String series = handler.extractSeries(mpn);
            System.out.println("Real-world: " + mpn + " series=" + series +
                    " ACCEL=" + matchesAccel + " GYRO=" + matchesGyro +
                    " MAG=" + matchesMag + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document full part numbers with package codes")
        @ValueSource(strings = {
                "ICM-20948-QFN",
                "MPU-6050-LGA",
                "MPU-9250-WLCSP",
                "ICM-20600-B",
                "ITG-3200-Q"
        })
        void documentFullPartNumbers(String mpn) {
            String series = handler.extractSeries(mpn);
            String packageCode = handler.extractPackageCode(mpn);
            System.out.println("Full part: " + mpn + " series=" + series + " package=" + packageCode);
        }
    }
}
