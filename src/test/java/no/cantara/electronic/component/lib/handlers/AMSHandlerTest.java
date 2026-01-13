package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AMSHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AMSHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * ams-OSRAM Product Families:
 * - AS72xx - Spectral Sensors (AS7261, AS7262, AS7263, AS7265)
 * - AS73xx - Color Sensors (AS7341, AS7343)
 * - TSL2xxx - Light-to-Digital Sensors (TSL2561, TSL2591)
 * - TMD2xxx - Proximity/ALS Sensors (TMD2671, TMD2772, TMD26721)
 * - TCS3xxx - Color Sensors (TCS3400, TCS3472, TCS34725)
 * - APDS-9xxx - Proximity/Gesture Sensors (APDS-9960, APDS-9930)
 * - AS3xxx - Driver ICs
 * - AS5xxx - Position Sensors (AS5600, AS5047, AS5048)
 * - AS6xxx - Spectral Sensors (AS6500)
 * - ENSxxx - Environmental Sensors (ENS160, ENS210, ENS220)
 *
 * Package Codes: BLGT=BGA, FN=QFN, TSL=DFN, ASIL=Automotive, ASOM=Module
 */
class AMSHandlerTest {

    private static AMSHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AMSHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("AS72xx Spectral Sensor Detection")
    class AS72xxSpectralSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect AS72xx spectral sensors")
        @ValueSource(strings = {"AS7261", "AS7262", "AS7263", "AS7265X"})
        void shouldDetectAS72xxSpectralSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AS72xx with package suffixes")
        @ValueSource(strings = {"AS7262-BLGT", "AS7263-BLGT", "AS7265X-BLGT"})
        void shouldDetectAS72xxWithPackageSuffix(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @Test
        @DisplayName("Document AS72xx detection behavior")
        void documentAS72xxDetection() {
            String[] mpns = {"AS7261", "AS7262", "AS7262-BLGT", "AS7263", "AS7265X"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("AS72xx detection: " + mpn +
                        " SENSOR=" + matchesSensor + " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("AS73xx Color Sensor Detection")
    class AS73xxColorSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect AS73xx color sensors")
        @ValueSource(strings = {"AS7341", "AS7343", "AS7343-DLGM"})
        void shouldDetectAS73xxColorSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Document AS73xx detection behavior")
        void documentAS73xxDetection() {
            String[] mpns = {"AS7341", "AS7343", "AS7343-DLGM"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("AS73xx detection: " + mpn +
                        " SENSOR=" + matchesSensor + " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("TSL2xxx Light Sensor Detection")
    class TSL2xxxLightSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect TSL2xxx light sensors")
        @ValueSource(strings = {"TSL2561", "TSL2591", "TSL2540", "TSL2561-TSL"})
        void shouldDetectTSL2xxxLightSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Document TSL2xxx detection behavior")
        void documentTSL2xxxDetection() {
            String[] mpns = {"TSL2561", "TSL2561-TSL", "TSL2591", "TSL2540"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("TSL2xxx detection: " + mpn +
                        " SENSOR=" + matchesSensor + " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("TMD2xxx Proximity Sensor Detection")
    class TMD2xxxProximitySensorTests {

        @ParameterizedTest
        @DisplayName("Should detect TMD2xxx proximity sensors")
        @ValueSource(strings = {"TMD2671", "TMD2772", "TMD26721", "TMD26721FN"})
        void shouldDetectTMD2xxxProximitySensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_PROXIMITY, registry),
                    mpn + " should match SENSOR_PROXIMITY");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMD2xxx as IC")
        @ValueSource(strings = {"TMD2671", "TMD2772", "TMD26721"})
        void shouldDetectTMD2xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Document TMD2xxx detection behavior")
        void documentTMD2xxxDetection() {
            String[] mpns = {"TMD2671", "TMD2772", "TMD26721", "TMD26721FN"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesProximity = handler.matches(mpn, ComponentType.SENSOR_PROXIMITY, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("TMD2xxx detection: " + mpn +
                        " SENSOR=" + matchesSensor +
                        " SENSOR_PROXIMITY=" + matchesProximity +
                        " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("TCS3xxx Color Sensor Detection")
    class TCS3xxxColorSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect TCS3xxx color sensors")
        @ValueSource(strings = {"TCS3400", "TCS3472", "TCS34725", "TCS34725FN"})
        void shouldDetectTCS3xxxColorSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Document TCS3xxx detection behavior")
        void documentTCS3xxxDetection() {
            String[] mpns = {"TCS3400", "TCS3472", "TCS34725", "TCS34725FN"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("TCS3xxx detection: " + mpn +
                        " SENSOR=" + matchesSensor + " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("APDS-9xxx Gesture Sensor Detection")
    class APDS9xxxGestureSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect APDS-9xxx gesture sensors (with hyphen)")
        @ValueSource(strings = {"APDS-9960", "APDS-9930", "APDS-9960-ASIL"})
        void shouldDetectAPDS9xxxWithHyphen(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_PROXIMITY, registry),
                    mpn + " should match SENSOR_PROXIMITY");
        }

        @ParameterizedTest
        @DisplayName("Should detect APDS9xxx gesture sensors (without hyphen)")
        @ValueSource(strings = {"APDS9960", "APDS9930"})
        void shouldDetectAPDS9xxxWithoutHyphen(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_PROXIMITY, registry),
                    mpn + " should match SENSOR_PROXIMITY");
        }

        @Test
        @DisplayName("Document APDS-9xxx detection behavior")
        void documentAPDS9xxxDetection() {
            String[] mpns = {"APDS-9960", "APDS9960", "APDS-9930", "APDS-9960-ASIL"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesProximity = handler.matches(mpn, ComponentType.SENSOR_PROXIMITY, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("APDS-9xxx detection: " + mpn +
                        " SENSOR=" + matchesSensor +
                        " SENSOR_PROXIMITY=" + matchesProximity +
                        " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("AS5xxx Position Sensor Detection")
    class AS5xxxPositionSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect AS5xxx position sensors")
        @ValueSource(strings = {"AS5600", "AS5047", "AS5048", "AS5600-ASOM"})
        void shouldDetectAS5xxxPositionSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Document AS5xxx detection behavior")
        void documentAS5xxxDetection() {
            String[] mpns = {"AS5600", "AS5047", "AS5048", "AS5600-ASOM", "AS5048A"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                String series = handler.extractSeries(mpn);
                System.out.println("AS5xxx detection: " + mpn +
                        " series=" + series +
                        " SENSOR=" + matchesSensor + " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("AS3xxx Driver IC Detection")
    class AS3xxxDriverICTests {

        @ParameterizedTest
        @DisplayName("Should detect AS3xxx driver ICs")
        @ValueSource(strings = {"AS3935", "AS3935-BQFT", "AS3410"})
        void shouldDetectAS3xxxDriverICs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AS3xxx as LED_DRIVER")
        @ValueSource(strings = {"AS3935", "AS3410"})
        void shouldDetectAS3xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
        }

        @Test
        @DisplayName("Document AS3xxx detection behavior")
        void documentAS3xxxDetection() {
            String[] mpns = {"AS3935", "AS3935-BQFT", "AS3410"};
            for (String mpn : mpns) {
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                boolean matchesLEDDriver = handler.matches(mpn, ComponentType.LED_DRIVER, registry);
                System.out.println("AS3xxx detection: " + mpn +
                        " IC=" + matchesIC + " LED_DRIVER=" + matchesLEDDriver);
            }
        }
    }

    @Nested
    @DisplayName("AS6xxx Spectral Sensor Detection")
    class AS6xxxSpectralSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect AS6xxx spectral sensors")
        @ValueSource(strings = {"AS6500", "AS6501"})
        void shouldDetectAS6xxxSpectralSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Document AS6xxx detection behavior")
        void documentAS6xxxDetection() {
            String[] mpns = {"AS6500", "AS6501"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("AS6xxx detection: " + mpn +
                        " SENSOR=" + matchesSensor + " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("ENS2xx Environmental Sensor Detection")
    class ENS2xxEnvironmentalSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect ENS2xx environmental sensors")
        @ValueSource(strings = {"ENS160", "ENS210", "ENS220"})
        void shouldDetectENS2xxEnvironmentalSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect ENS2xx as humidity sensor")
        @ValueSource(strings = {"ENS210", "ENS220"})
        void shouldDetectENS2xxAsHumiditySensor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry),
                    mpn + " should match HUMIDITY_SENSOR");
        }

        @Test
        @DisplayName("Document ENS2xx detection behavior")
        void documentENS2xxDetection() {
            String[] mpns = {"ENS160", "ENS210", "ENS220"};
            for (String mpn : mpns) {
                boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
                boolean matchesHumidity = handler.matches(mpn, ComponentType.HUMIDITY_SENSOR, registry);
                boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
                System.out.println("ENS2xx detection: " + mpn +
                        " SENSOR=" + matchesSensor +
                        " HUMIDITY_SENSOR=" + matchesHumidity +
                        " IC=" + matchesIC);
            }
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from hyphenated suffixes")
        @CsvSource({
                "AS7262-BLGT, BGA",
                "AS7262-BGA, BGA",
                "TSL2561-TSL, DFN",
                "AS5600-ASOM, MODULE"
        })
        void shouldExtractPackageCodesFromHyphenatedSuffixes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract FN suffix as QFN")
        @CsvSource({
                "TMD26721FN, QFN",
                "TCS34725FN, QFN"
        })
        void shouldExtractFNSuffixAsQFN(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for base part numbers")
        void shouldReturnEmptyForBaseParts() {
            assertEquals("", handler.extractPackageCode("AS7262"),
                    "Base part should return empty package code");
            assertEquals("", handler.extractPackageCode("TSL2591"),
                    "Base part should return empty package code");
        }

        @Test
        @DisplayName("Document package code extraction behavior")
        void documentPackageCodeExtraction() {
            String[] mpns = {"AS7262", "AS7262-BLGT", "TMD26721FN", "APDS-9960", "AS5600-ASOM"};
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
        @DisplayName("Should extract AS series correctly")
        @CsvSource({
                "AS7262, AS72",
                "AS7262-BLGT, AS72",
                "AS7343, AS73",
                "AS5600, AS56",
                "AS5600-ASOM, AS56",
                "AS3935, AS39",
                "AS6500, AS65"
        })
        void shouldExtractASSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TSL series correctly")
        @CsvSource({
                "TSL2561, TSL",
                "TSL2591, TSL",
                "TSL2561-TSL, TSL"
        })
        void shouldExtractTSLSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TMD series correctly")
        @CsvSource({
                "TMD2671, TMD",
                "TMD26721, TMD",
                "TMD26721FN, TMD"
        })
        void shouldExtractTMDSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TCS series correctly")
        @CsvSource({
                "TCS3400, TCS",
                "TCS3472, TCS",
                "TCS34725, TCS"
        })
        void shouldExtractTCSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract APDS series correctly")
        @CsvSource({
                "APDS-9960, APDS",
                "APDS9960, APDS",
                "APDS-9930, APDS",
                "APDS-9960-ASIL, APDS"
        })
        void shouldExtractAPDSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract ENS series correctly")
        @CsvSource({
                "ENS160, ENS",
                "ENS210, ENS",
                "ENS220, ENS"
        })
        void shouldExtractENSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same model with different packages should be replacements")
        void sameModelDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("AS7262-BLGT", "AS7262-BGA"),
                    "Same model with different packages should be replacements");
        }

        @Test
        @DisplayName("AS72xx spectral sensors in same family should be compatible")
        void as72xxFamilyCompatible() {
            assertTrue(handler.isOfficialReplacement("AS7261", "AS7262"),
                    "AS7261 and AS7262 should be compatible (same spectral sensor family)");
            assertTrue(handler.isOfficialReplacement("AS7262", "AS7263"),
                    "AS7262 and AS7263 should be compatible");
        }

        @Test
        @DisplayName("TSL light sensors in same family should be compatible")
        void tslFamilyCompatible() {
            assertTrue(handler.isOfficialReplacement("TSL2561", "TSL2591"),
                    "TSL2561 and TSL2591 should be compatible");
        }

        @Test
        @DisplayName("TCS color sensors in same family should be compatible")
        void tcsFamilyCompatible() {
            assertTrue(handler.isOfficialReplacement("TCS3472", "TCS34725"),
                    "TCS3472 and TCS34725 should be compatible");
        }

        @Test
        @DisplayName("APDS gesture sensors in same family should be compatible")
        void apdsFamilyCompatible() {
            assertTrue(handler.isOfficialReplacement("APDS-9960", "APDS-9930"),
                    "APDS-9960 and APDS-9930 should be compatible");
        }

        @Test
        @DisplayName("AS5xxx position sensors in same family should be compatible")
        void as5xxxFamilyCompatible() {
            assertTrue(handler.isOfficialReplacement("AS5047", "AS5048"),
                    "AS5047 and AS5048 should be compatible");
        }

        @Test
        @DisplayName("Different families should NOT be replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("AS7262", "TSL2561"),
                    "AS72xx and TSL are different families");
            assertFalse(handler.isOfficialReplacement("TCS3472", "TMD2671"),
                    "TCS and TMD are different families");
            assertFalse(handler.isOfficialReplacement("APDS-9960", "AS5600"),
                    "APDS and AS5xxx are different families");
        }

        @Test
        @DisplayName("ENS sensors with different functions should NOT be replacements")
        void ensDifferentFunctionsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ENS160", "ENS210"),
                    "ENS160 (air quality) and ENS210 (humidity) are different sensors");
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
        @DisplayName("Should support SENSOR_PROXIMITY type")
        void shouldSupportSensorProximityType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.SENSOR_PROXIMITY),
                    "Should support SENSOR_PROXIMITY");
        }

        @Test
        @DisplayName("Should support HUMIDITY_SENSOR type")
        void shouldSupportHumiditySensorType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.HUMIDITY_SENSOR),
                    "Should support HUMIDITY_SENSOR");
        }

        @Test
        @DisplayName("Should support LED_DRIVER type")
        void shouldSupportLEDDriverType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.LED_DRIVER),
                    "Should support LED_DRIVER");
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
        @DisplayName("Should use immutable Set.of()")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes() should return immutable set");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.SENSOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "AS7262"));
            assertFalse(handler.isOfficialReplacement("AS7262", null));
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
            assertFalse(handler.matches("AS7262", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("as7262", ComponentType.SENSOR, registry),
                    "Should match lowercase MPN");
            assertTrue(handler.matches("tsl2561", ComponentType.SENSOR, registry),
                    "Should match lowercase MPN");
            assertTrue(handler.matches("apds-9960", ComponentType.SENSOR, registry),
                    "Should match lowercase MPN");
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("As7262", ComponentType.SENSOR, registry),
                    "Should match mixed case MPN");
            assertTrue(handler.matches("Tsl2561", ComponentType.SENSOR, registry),
                    "Should match mixed case MPN");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            AMSHandler directHandler = new AMSHandler();
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
        @DisplayName("Document common ams-OSRAM products")
        @ValueSource(strings = {
                "AS7262",       // 6-channel spectral sensor
                "AS7262-BLGT",  // With package code
                "TSL2591",      // High dynamic range light sensor
                "TSL2561",      // Light-to-digital sensor
                "TMD26721FN",   // Proximity/ALS sensor
                "TCS34725FN",   // RGB color sensor
                "APDS-9960",    // Gesture/proximity sensor
                "AS5600",       // Magnetic rotary encoder
                "AS5600-ASOM",  // Module variant
                "ENS160",       // Air quality sensor
                "ENS210"        // Humidity/temperature sensor
        })
        void documentCommonProducts(String mpn) {
            boolean matchesSensor = handler.matches(mpn, ComponentType.SENSOR, registry);
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            String series = handler.extractSeries(mpn);
            String packageCode = handler.extractPackageCode(mpn);
            System.out.println("Real-world: " + mpn + " series=" + series +
                    " package=" + packageCode +
                    " SENSOR=" + matchesSensor + " IC=" + matchesIC);
        }

        @Test
        @DisplayName("Popular breakout board sensors should be detected")
        void popularBreakoutBoardSensors() {
            // Popular sensors used in hobby electronics
            String[] popularSensors = {
                    "APDS-9960",    // SparkFun, Adafruit gesture sensor
                    "TSL2561",      // Adafruit light sensor
                    "TCS34725",     // Adafruit color sensor
                    "AS7262",       // SparkFun spectral sensor
                    "AS5600",       // Magnetic encoder modules
                    "ENS160"        // Air quality sensor
            };

            for (String mpn : popularSensors) {
                assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                        mpn + " should be detected as sensor");
            }
        }
    }

    @Nested
    @DisplayName("Non-Matching MPNs")
    class NonMatchingMPNTests {

        @ParameterizedTest
        @DisplayName("Should NOT match non-AMS parts")
        @ValueSource(strings = {
                "BME280",       // Bosch sensor
                "BH1750",       // Rohm light sensor
                "VEML6070",     // Vishay UV sensor
                "SHT31",        // Sensirion humidity sensor
                "MLX90614",     // Melexis temperature sensor
                "ICM-20948",    // InvenSense IMU
                "LM35"          // TI temperature sensor
        })
        void shouldNotMatchNonAMSParts(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should NOT match (not an ams-OSRAM part)");
        }

        @ParameterizedTest
        @DisplayName("Should NOT match OSRAM LEDs (handled by OSRAMHandler)")
        @ValueSource(strings = {
                "LRTB GVSG",    // OSRAM LED
                "LE UW S2W",    // OSRAM LED
                "SFH 7060"      // OSRAM photodiode (but starts with S, not our patterns)
        })
        void shouldNotMatchOSRAMLEDs(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should NOT match (OSRAM LEDs handled separately)");
        }
    }
}
