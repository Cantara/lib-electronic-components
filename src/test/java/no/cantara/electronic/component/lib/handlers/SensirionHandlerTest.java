package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SensirionHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SensirionHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Sensirion Product Families:
 * - SHTxx - Humidity & Temperature sensors (SHT30, SHT31, SHT35, SHT40, SHT41, SHT45, SHT85)
 * - SGPxx - Gas sensors (SGP30, SGP40, SGP41)
 * - SCDxx - CO2 sensors (SCD30, SCD40, SCD41)
 * - SFAxx - Formaldehyde sensors (SFA30)
 * - SPSxx - Particulate matter sensors (SPS30)
 * - STSxx - Temperature only sensors (STS30, STS31, STS40)
 * - SLFxxxx - Liquid flow sensors (SLF3S-1300F)
 * - SDPxxx - Differential pressure sensors (SDP31, SDP810)
 *
 * Package Codes: DIS=DFN, AD1B=DFN, D=DFN, P=PIN
 */
class SensirionHandlerTest {

    private static SensirionHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SensirionHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Humidity & Temperature Sensor Detection (SHTxx)")
    class HumidityTemperatureTests {

        @ParameterizedTest
        @DisplayName("Should detect SHT3x series humidity/temperature sensors")
        @ValueSource(strings = {"SHT30", "SHT31", "SHT35", "SHT31-DIS-B", "SHT35-DIS-F"})
        void shouldDetectSHT3xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry),
                    mpn + " should match HUMIDITY_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry),
                    mpn + " should match TEMPERATURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SHT4x series humidity/temperature sensors")
        @ValueSource(strings = {"SHT40", "SHT41", "SHT45", "SHT40-AD1B-R2", "SHT45-AD1F-R3"})
        void shouldDetectSHT4xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry),
                    mpn + " should match HUMIDITY_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry),
                    mpn + " should match TEMPERATURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SHT8x series humidity/temperature sensors")
        @ValueSource(strings = {"SHT85", "SHT85-DIS-B"})
        void shouldDetectSHT8xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry),
                    mpn + " should match HUMIDITY_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry),
                    mpn + " should match TEMPERATURE_SENSOR");
        }
    }

    @Nested
    @DisplayName("Temperature Only Sensor Detection (STSxx)")
    class TemperatureOnlyTests {

        @ParameterizedTest
        @DisplayName("Should detect STS3x series temperature sensors")
        @ValueSource(strings = {"STS30", "STS31", "STS30-DIS", "STS31-DIS-B"})
        void shouldDetectSTS3xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry),
                    mpn + " should match TEMPERATURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            // Temperature-only sensors should NOT match humidity
            assertFalse(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry),
                    mpn + " should NOT match HUMIDITY_SENSOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect STS4x series temperature sensors")
        @ValueSource(strings = {"STS40", "STS40-AD1B", "STS40-AD1B-R2"})
        void shouldDetectSTS4xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry),
                    mpn + " should match TEMPERATURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("Gas Sensor Detection (SGPxx)")
    class GasSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect SGP3x series VOC sensors")
        @ValueSource(strings = {"SGP30", "SGP30-D", "SGP30-D-R4"})
        void shouldDetectSGP3xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect SGP4x series VOC sensors")
        @ValueSource(strings = {"SGP40", "SGP41", "SGP40-D-R4", "SGP41-D-R4"})
        void shouldDetectSGP4xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("CO2 Sensor Detection (SCDxx)")
    class CO2SensorTests {

        @ParameterizedTest
        @DisplayName("Should detect SCD3x series CO2 sensors (photoacoustic)")
        @ValueSource(strings = {"SCD30", "SCD30-D", "SCD30-D-R2"})
        void shouldDetectSCD3xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect SCD4x series CO2 sensors (miniaturized)")
        @ValueSource(strings = {"SCD40", "SCD41", "SCD40-D-R2", "SCD41-D-R2"})
        void shouldDetectSCD4xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Formaldehyde Sensor Detection (SFAxx)")
    class FormaldehydeSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect SFA3x series formaldehyde sensors")
        @ValueSource(strings = {"SFA30", "SFA30-D", "SFA30-D-R2"})
        void shouldDetectSFA3xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Particulate Matter Sensor Detection (SPSxx)")
    class ParticulateSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect SPS3x series particulate matter sensors")
        @ValueSource(strings = {"SPS30", "SPS30-D", "SPS30-D-R2"})
        void shouldDetectSPS3xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Liquid Flow Sensor Detection (SLFxxxx)")
    class LiquidFlowSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect SLF series liquid flow sensors")
        @ValueSource(strings = {"SLF3S", "SLF3S-1300F", "SLF3S-0600F", "SLF3S-4000B"})
        void shouldDetectSLFSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_FLOW, registry),
                    mpn + " should match SENSOR_FLOW");
        }
    }

    @Nested
    @DisplayName("Differential Pressure Sensor Detection (SDPxxx)")
    class DifferentialPressureSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect SDP3x series differential pressure sensors")
        @ValueSource(strings = {"SDP31", "SDP32", "SDP31-D", "SDP32-D-R4"})
        void shouldDetectSDP3xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry),
                    mpn + " should match PRESSURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SDP8xx series differential pressure sensors")
        @ValueSource(strings = {"SDP810", "SDP816", "SDP810-D", "SDP816-D-R4"})
        void shouldDetectSDP8xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry),
                    mpn + " should match PRESSURE_SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract DFN package from DIS suffix")
        @CsvSource({
                "SHT31-DIS, DFN",
                "SHT31-DIS-B, DFN",
                "SHT35-DIS-F, DFN"
        })
        void shouldExtractDISPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract DFN package from AD1x suffix")
        @CsvSource({
                "SHT40-AD1B, DFN",
                "SHT40-AD1B-R2, DFN",
                "SHT45-AD1F-R3, DFN"
        })
        void shouldExtractAD1xPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract DFN package from D suffix")
        @CsvSource({
                "SGP40-D, DFN",
                "SGP40-D-R4, DFN",
                "SCD40-D-R2, DFN"
        })
        void shouldExtractDPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for base MPN without suffix")
        void shouldReturnEmptyForBaseMpn() {
            assertEquals("", handler.extractPackageCode("SHT31"));
            assertEquals("", handler.extractPackageCode("SGP40"));
        }

        @Test
        @DisplayName("Should handle flow sensor format")
        void shouldHandleFlowSensorFormat() {
            String result = handler.extractPackageCode("SLF3S-1300F");
            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract SHT3x series")
        @CsvSource({
                "SHT30, SHT3x",
                "SHT31, SHT3x",
                "SHT35, SHT3x",
                "SHT31-DIS-B, SHT3x"
        })
        void shouldExtractSHT3xSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SHT4x series")
        @CsvSource({
                "SHT40, SHT4x",
                "SHT41, SHT4x",
                "SHT45, SHT4x",
                "SHT40-AD1B-R2, SHT4x"
        })
        void shouldExtractSHT4xSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract STS series")
        @CsvSource({
                "STS30, STS3x",
                "STS31, STS3x",
                "STS40, STS4x"
        })
        void shouldExtractSTSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SGP series")
        @CsvSource({
                "SGP30, SGP3x",
                "SGP40, SGP4x",
                "SGP41, SGP4x",
                "SGP40-D-R4, SGP4x"
        })
        void shouldExtractSGPSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SCD series")
        @CsvSource({
                "SCD30, SCD3x",
                "SCD40, SCD4x",
                "SCD41, SCD4x",
                "SCD40-D-R2, SCD4x"
        })
        void shouldExtractSCDSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SFA series")
        @CsvSource({
                "SFA30, SFA3x",
                "SFA30-D-R2, SFA3x"
        })
        void shouldExtractSFASeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SPS series")
        @CsvSource({
                "SPS30, SPS3x",
                "SPS30-D-R2, SPS3x"
        })
        void shouldExtractSPSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SDP series")
        @CsvSource({
                "SDP31, SDP3x",
                "SDP32, SDP3x",
                "SDP810, SDP8x",
                "SDP816, SDP8x"
        })
        void shouldExtractSDPSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should extract SLF series")
        void shouldExtractSLFSeries() {
            assertEquals("SLFxx", handler.extractSeries("SLF3S"));
            assertEquals("SLFxx", handler.extractSeries("SLF3S-1300F"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with different packages should be replacements")
        void sameSeriesDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("SHT31-DIS-B", "SHT31-DIS-F"),
                    "Same series different grades should be replacements");
            assertTrue(handler.isOfficialReplacement("SGP40-D", "SGP40-D-R4"),
                    "Same series with reel option should be replacements");
        }

        @Test
        @DisplayName("SHT4x can replace SHT3x (next generation)")
        void sht4xCanReplaceSht3x() {
            assertTrue(handler.isOfficialReplacement("SHT40", "SHT31"),
                    "SHT40 should be able to replace SHT31");
            assertTrue(handler.isOfficialReplacement("SHT45", "SHT35"),
                    "SHT45 should be able to replace SHT35");
        }

        @Test
        @DisplayName("SGP4x can replace SGP3x (improved gas sensor)")
        void sgp4xCanReplaceSgp3x() {
            assertTrue(handler.isOfficialReplacement("SGP40", "SGP30"),
                    "SGP40 should be able to replace SGP30");
            assertTrue(handler.isOfficialReplacement("SGP41", "SGP30"),
                    "SGP41 should be able to replace SGP30");
        }

        @Test
        @DisplayName("SCD4x can replace SCD3x (miniaturized CO2)")
        void scd4xCanReplaceScd3x() {
            assertTrue(handler.isOfficialReplacement("SCD40", "SCD30"),
                    "SCD40 should be able to replace SCD30");
            assertTrue(handler.isOfficialReplacement("SCD41", "SCD30"),
                    "SCD41 should be able to replace SCD30");
        }

        @Test
        @DisplayName("STS4x can replace STS3x")
        void sts4xCanReplaceSts3x() {
            assertTrue(handler.isOfficialReplacement("STS40", "STS30"),
                    "STS40 should be able to replace STS30");
        }

        @Test
        @DisplayName("Different product families should NOT be replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("SHT31", "SGP40"),
                    "Humidity sensor and gas sensor should not be replacements");
            assertFalse(handler.isOfficialReplacement("SCD40", "SPS30"),
                    "CO2 sensor and particulate sensor should not be replacements");
            assertFalse(handler.isOfficialReplacement("SDP31", "SLF3S"),
                    "Pressure sensor and flow sensor should not be replacements");
        }

        @Test
        @DisplayName("Higher accuracy grade can replace lower grade")
        void higherAccuracyCanReplaceLower() {
            // Grade A can replace B
            assertTrue(handler.isOfficialReplacement("SHT31-DIS-A", "SHT31-DIS-B"),
                    "Grade A should replace grade B");
            // Grade B can replace F
            assertTrue(handler.isOfficialReplacement("SHT31-DIS-B", "SHT31-DIS-F"),
                    "Grade B should replace grade F");
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
        @DisplayName("Should support HUMIDITY_SENSOR type")
        void shouldSupportHumiditySensorType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.HUMIDITY_SENSOR),
                    "Should support HUMIDITY_SENSOR");
        }

        @Test
        @DisplayName("Should support TEMPERATURE_SENSOR type")
        void shouldSupportTemperatureSensorType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.TEMPERATURE_SENSOR),
                    "Should support TEMPERATURE_SENSOR");
        }

        @Test
        @DisplayName("Should support PRESSURE_SENSOR type")
        void shouldSupportPressureSensorType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.PRESSURE_SENSOR),
                    "Should support PRESSURE_SENSOR");
        }

        @Test
        @DisplayName("Should support SENSOR_FLOW type")
        void shouldSupportSensorFlowType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.SENSOR_FLOW),
                    "Should support SENSOR_FLOW");
        }

        @Test
        @DisplayName("Should support IC type")
        void shouldSupportICType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        @DisplayName("Should use Set.of() for immutability")
        void shouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes() should return immutable set");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.HUMIDITY_SENSOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "SHT31"));
            assertFalse(handler.isOfficialReplacement("SHT31", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.HUMIDITY_SENSOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("SHT31", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPN")
        void shouldHandleLowercaseMpn() {
            assertTrue(handler.matches("sht31", ComponentType.HUMIDITY_SENSOR, registry),
                    "Should match lowercase MPN");
            assertEquals("SHT3x", handler.extractSeries("sht31"),
                    "Should extract series from lowercase MPN");
        }

        @Test
        @DisplayName("Should handle mixed case MPN")
        void shouldHandleMixedCaseMpn() {
            assertTrue(handler.matches("Sht31-Dis-B", ComponentType.HUMIDITY_SENSOR, registry),
                    "Should match mixed case MPN");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            SensirionHandler directHandler = new SensirionHandler();
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
        @DisplayName("Document popular Sensirion products")
        @ValueSource(strings = {
                "SHT31-DIS-B",      // Popular humidity/temp sensor
                "SHT40-AD1B-R2",    // Next-gen humidity/temp
                "SGP40-D-R4",       // Popular VOC sensor
                "SCD40-D-R2",       // Miniaturized CO2 sensor
                "SPS30",            // Particulate matter sensor
                "SDP31-D",          // Differential pressure sensor
                "SLF3S-1300F"       // Liquid flow sensor
        })
        void documentPopularProducts(String mpn) {
            String series = handler.extractSeries(mpn);
            String packageCode = handler.extractPackageCode(mpn);
            boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);

            System.out.println("Product: " + mpn + " series=" + series +
                    " package=" + packageCode + " SENSOR=" + matchesSensor);
        }

        @ParameterizedTest
        @DisplayName("Document humidity/temperature sensor variants")
        @ValueSource(strings = {
                "SHT30-DIS-B",
                "SHT31-DIS-B",
                "SHT35-DIS-F",
                "SHT40-AD1B-R2",
                "SHT41-AD1B-R2",
                "SHT45-AD1F-R3",
                "SHT85-DIS"
        })
        void documentHumidityTempVariants(String mpn) {
            boolean humidity = handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry);
            boolean temp = handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry);
            String series = handler.extractSeries(mpn);

            System.out.println("Humidity/Temp: " + mpn + " series=" + series +
                    " HUMIDITY=" + humidity + " TEMP=" + temp);
        }

        @ParameterizedTest
        @DisplayName("Document environmental sensor variants")
        @ValueSource(strings = {
                "SGP30-D",
                "SGP40-D-R4",
                "SGP41-D-R4",
                "SCD30-D",
                "SCD40-D-R2",
                "SCD41-D-R2",
                "SFA30-D-R2",
                "SPS30"
        })
        void documentEnvironmentalSensorVariants(String mpn) {
            boolean sensor = handler.matches(mpn, ComponentType.SENSOR, registry);
            boolean ic = handler.matches(mpn, ComponentType.IC, registry);
            String series = handler.extractSeries(mpn);

            System.out.println("Environmental: " + mpn + " series=" + series +
                    " SENSOR=" + sensor + " IC=" + ic);
        }
    }

    @Nested
    @DisplayName("Component Type Coverage")
    class ComponentTypeCoverageTests {

        @Test
        @DisplayName("SHTxx should match multiple component types")
        void shtxxMatchesMultipleTypes() {
            String mpn = "SHT31-DIS-B";
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry));
        }

        @Test
        @DisplayName("STSxx should only match temperature types")
        void stsxxOnlyMatchesTemperature() {
            String mpn = "STS30-DIS";
            assertTrue(handler.matches(mpn, ComponentType.TEMPERATURE_SENSOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry));
            assertFalse(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry));
        }

        @Test
        @DisplayName("SDPxx should match pressure sensor types")
        void sdpxxMatchesPressureTypes() {
            String mpn = "SDP31-D";
            assertTrue(handler.matches(mpn, ComponentType.PRESSURE_SENSOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry));
        }

        @Test
        @DisplayName("SLFxxxx should match flow sensor types")
        void slfxxxxMatchesFlowTypes() {
            String mpn = "SLF3S-1300F";
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_FLOW, registry));
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry));
        }
    }
}
