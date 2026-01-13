package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MPSHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for MPSHandler (Monolithic Power Systems).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class MPSHandlerTest {

    private static MPSHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new MPSHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MP1xxx Step-Down Converter Detection")
    class MP1xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MP1xxx step-down converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "MP1584", "MP1584EN", "MP1584EN-LF-Z",
            "MP1593", "MP1593DN", "MP1593DN-LF-Z",
            "MP1517", "MP1517DR", "MP1591",
            "MP1494", "MP1470"
        })
        void shouldDetectMP1xxxAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("MP1584 is a popular 3A step-down converter")
        @ValueSource(strings = {"MP1584EN", "MP1584EN-LF-Z", "MP1584DN"})
        void shouldDetectMP1584Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should be detected as VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("MP2xxx Step-Down/LDO Converter Detection")
    class MP2xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MP2xxx converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "MP2307", "MP2307DN", "MP2307DN-LF-Z",
            "MP2359", "MP2359DJ-LF-Z",
            "MP2161", "MP2161GJ-LF-Z",
            "MP2315", "MP2451", "MP2456",
            "MP2331", "MP2336"
        })
        void shouldDetectMP2xxxAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("MP2307 is a popular 3A buck converter")
        void shouldDetectMP2307() {
            assertTrue(handler.matches("MP2307DN", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("MP2307DN-LF-Z", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("MP3xxx LED Driver Detection")
    class MP3xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MP3xxx as LED_DRIVER")
        @ValueSource(strings = {
            "MP3302", "MP3302DJ", "MP3302DJ-LF-Z",
            "MP3394", "MP3394GJ", "MP3394GJ-LF-Z",
            "MP3378", "MP3388", "MP3340"
        })
        void shouldDetectMP3xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("MP3xxx should NOT match VOLTAGE_REGULATOR")
        void mp3xxxShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("MP3302", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MP3302 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("MP3394", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MP3394 should NOT match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("MP3xxx should NOT match MOTOR_DRIVER")
        void mp3xxxShouldNotMatchMotorDriver() {
            assertFalse(handler.matches("MP3302", ComponentType.MOTOR_DRIVER, registry),
                    "MP3302 should NOT match MOTOR_DRIVER");
        }
    }

    @Nested
    @DisplayName("MP4xxx High-Current Step-Down Detection")
    class MP4xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MP4xxx high-current converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "MP4560", "MP4560DN", "MP4560GQ-AEC1-LF-Z",
            "MP4569", "MP4569GQ", "MP4569GQ-LF-Z",
            "MP4420", "MP4462", "MP4689"
        })
        void shouldDetectMP4xxxAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("MP4560 is a popular 2A sync buck with AEC1 automotive option")
        void shouldDetectMP4560Variants() {
            assertTrue(handler.matches("MP4560DN", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("MP4560GQ-AEC1-LF-Z", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("MP5xxx Step-Up/SEPIC Converter Detection")
    class MP5xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MP5xxx boost converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "MP5010", "MP5010DS", "MP5010DS-LF-Z",
            "MP5032", "MP5032GF", "MP5032GF-LF-Z",
            "MP5075", "MP5078", "MP5080"
        })
        void shouldDetectMP5xxxAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("MP6xxx Motor Driver Detection")
    class MP6xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MP6xxx as MOTOR_DRIVER")
        @ValueSource(strings = {
            "MP6500", "MP6500GF", "MP6500GF-LF-Z",
            "MP6513", "MP6513GF-Z",
            "MP6543", "MP6543GF", "MP6543GR",
            "MP6540", "MP6550", "MP6530"
        })
        void shouldDetectMP6xxxAsMotorDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("MP6xxx should NOT match VOLTAGE_REGULATOR")
        void mp6xxxShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("MP6500", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MP6500 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("MP6543", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MP6543 should NOT match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("MP6xxx should NOT match LED_DRIVER")
        void mp6xxxShouldNotMatchLEDDriver() {
            assertFalse(handler.matches("MP6500", ComponentType.LED_DRIVER, registry),
                    "MP6500 should NOT match LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("MP8xxx Multi-Channel PMIC Detection")
    class MP8xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MP8xxx PMICs as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "MP8756", "MP8756GQ", "MP8756GQ-LF-Z",
            "MP8859", "MP8859GL", "MP8859GL-LF-Z",
            "MP8867", "MP8865", "MP8860"
        })
        void shouldDetectMP8xxxAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("MPQ Automotive Grade Detection")
    class MPQAutomotiveTests {

        @ParameterizedTest
        @DisplayName("Should detect MPQ voltage regulators")
        @CsvSource({
            "MPQ4560GQ-AEC1-LF-Z, VOLTAGE_REGULATOR",
            "MPQ4569, VOLTAGE_REGULATOR",
            "MPQ8875, VOLTAGE_REGULATOR",
            "MPQ2307, VOLTAGE_REGULATOR",
            "MPQ1584, VOLTAGE_REGULATOR"
        })
        void shouldDetectMPQVoltageRegulators(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @ParameterizedTest
        @DisplayName("Should detect MPQ6xxx motor drivers")
        @ValueSource(strings = {"MPQ6500", "MPQ6543", "MPQ6550"})
        void shouldDetectMPQ6xxxAsMotorDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect MPQ3xxx LED drivers")
        @ValueSource(strings = {"MPQ3302", "MPQ3394", "MPQ3378"})
        void shouldDetectMPQ3xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
        }

        @Test
        @DisplayName("Should identify automotive grade parts")
        void shouldIdentifyAutomotiveGrade() {
            assertTrue(handler.isAutomotiveGrade("MPQ4560GQ-AEC1-LF-Z"));
            assertTrue(handler.isAutomotiveGrade("MPQ8875"));
            assertTrue(handler.isAutomotiveGrade("MP4560GQ-AEC1-LF-Z"));  // Has -AEC suffix
            assertFalse(handler.isAutomotiveGrade("MP4560DN"));
            assertFalse(handler.isAutomotiveGrade("MP1584EN"));
        }
    }

    @Nested
    @DisplayName("MPM Power Module Detection")
    class MPMModuleTests {

        @ParameterizedTest
        @DisplayName("Should detect MPM power modules as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "MPM3610", "MPM3610GQ", "MPM3610GQV-P",
            "MPM3833", "MPM3833GQ", "MPM3833C",
            "MPM3530", "MPM3520", "MPM3650"
        })
        void shouldDetectMPMAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract standard package codes")
        @CsvSource({
            "MP1584EN-LF-Z, SOIC-8E",
            "MP2307DN-LF-Z, SO-8",
            "MP4560GQ-AEC1-LF-Z, QFN",
            "MP8756GL, QFN",
            "MP6500GF-LF-Z, WLCSP"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should handle various package suffixes")
        @CsvSource({
            "MP1584EN, SOIC-8E",
            "MP2307DN, SO-8",
            "MP3302DJ, DFN",
            "MP6500GR, QFN",
            "MP8859GS, QFN-EP"
        })
        void shouldHandleVariousPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @Test
        @DisplayName("Should return raw code for unknown package codes")
        void shouldReturnRawCodeForUnknown() {
            String result = handler.extractPackageCode("MP1234XY");
            // Should return XY if not in lookup table
            assertEquals("XY", result);
        }

        @Test
        @DisplayName("Should handle empty and null MPN")
        void shouldHandleEmptyAndNull() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract MP series codes")
        @CsvSource({
            "MP1584EN-LF-Z, MP1",
            "MP2307DN-LF-Z, MP2",
            "MP3302DJ, MP3",
            "MP4560GQ, MP4",
            "MP5010DS, MP5",
            "MP6500GF, MP6",
            "MP8756GQ, MP8",
            "MP9101GQ, MP9"
        })
        void shouldExtractMPSeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MPQ automotive series")
        @ValueSource(strings = {"MPQ4560GQ", "MPQ8875", "MPQ2307"})
        void shouldExtractMPQSeries(String mpn) {
            assertEquals("MPQ", handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MPM module series")
        @ValueSource(strings = {"MPM3610GQ", "MPM3833C", "MPM3520"})
        void shouldExtractMPMSeries(String mpn) {
            assertEquals("MPM", handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalid() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("INVALID"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("MP1584EN", "MP1584DN"),
                    "MP1584EN and MP1584DN should be replacements (same part, different package)");
            assertTrue(handler.isOfficialReplacement("MP2307DN", "MP2307GQ"),
                    "MP2307DN and MP2307GQ should be replacements");
        }

        @Test
        @DisplayName("Same part with and without suffix should be replacements")
        void samePartWithSuffix() {
            assertTrue(handler.isOfficialReplacement("MP1584EN", "MP1584EN-LF-Z"),
                    "MP1584EN and MP1584EN-LF-Z should be replacements");
            assertTrue(handler.isOfficialReplacement("MP4560GQ", "MP4560GQ-AEC1-LF-Z"),
                    "MP4560GQ and MP4560GQ-AEC1-LF-Z should be replacements");
        }

        @Test
        @DisplayName("Automotive and standard versions should be replacements")
        void automotiveAndStandardReplacements() {
            assertTrue(handler.isOfficialReplacement("MP4560DN", "MPQ4560GQ"),
                    "MP4560 and MPQ4560 should be replacements (automotive equiv)");
            assertTrue(handler.isOfficialReplacement("MPQ8875", "MP8875"),
                    "MPQ8875 and MP8875 should be replacements");
        }

        @Test
        @DisplayName("Different parts should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("MP1584EN", "MP2307DN"),
                    "MP1584 and MP2307 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("MP3302", "MP6500"),
                    "MP3302 (LED driver) and MP6500 (motor driver) should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("MP1584", "MPM3610"),
                    "MP1584 and MPM3610 should NOT be replacements (different series)");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "MP1584EN"));
            assertFalse(handler.isOfficialReplacement("MP1584EN", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.IC));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertTrue(types.contains(ComponentType.LED_DRIVER));
            assertTrue(types.contains(ComponentType.MOTOR_DRIVER));
        }

        @Test
        @DisplayName("Should use Set.of() for immutability")
        void shouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes() should return immutable Set");
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
    @DisplayName("Series Description")
    class SeriesDescriptionTests {

        @ParameterizedTest
        @DisplayName("Should return correct series descriptions")
        @CsvSource({
            "MP1, Step-Down Converters",
            "MP2, Step-Down/LDO Converters",
            "MP3, LED Drivers",
            "MP4, High-Current Step-Down",
            "MP5, Step-Up/SEPIC Converters",
            "MP6, Motor Drivers",
            "MP8, Multi-Channel PMIC",
            "MPQ, Automotive Grade",
            "MPM, Power Modules"
        })
        void shouldReturnSeriesDescription(String series, String expectedDesc) {
            assertEquals(expectedDesc, handler.getSeriesDescription(series));
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getSeriesDescription("UNKNOWN"));
            assertEquals("", handler.getSeriesDescription(""));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "MP1584"));
            assertFalse(handler.isOfficialReplacement("MP1584", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("MP1584EN", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("mp1584en", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("MP1584EN", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("Mp1584En", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Should not match non-MPS parts")
        void shouldNotMatchNonMPSParts() {
            assertFalse(handler.matches("LM7805", ComponentType.VOLTAGE_REGULATOR, registry),
                    "LM7805 is not an MPS part");
            assertFalse(handler.matches("TPS54331", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TPS54331 is not an MPS part");
            assertFalse(handler.matches("ADP3330", ComponentType.VOLTAGE_REGULATOR, registry),
                    "ADP3330 is not an MPS part");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("MP", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("MP1", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("MP15", ComponentType.VOLTAGE_REGULATOR, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular MPS parts")
        @CsvSource({
            "MP1584EN-LF-Z, VOLTAGE_REGULATOR",
            "MP2307DN-LF-Z, VOLTAGE_REGULATOR",
            "MP3302DJ-LF-Z, LED_DRIVER",
            "MP4560GQ-AEC1-LF-Z, VOLTAGE_REGULATOR",
            "MP6500GF-LF-Z, MOTOR_DRIVER",
            "MPM3610GQV-P, VOLTAGE_REGULATOR",
            "MPQ4560GQ-AEC1-LF-Z, VOLTAGE_REGULATOR"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @Test
        @DisplayName("Should handle full MPN with all suffixes")
        void shouldHandleFullMPN() {
            String fullMpn = "MPQ4560GQ-AEC1-LF-Z";
            assertTrue(handler.matches(fullMpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertEquals("QFN", handler.extractPackageCode(fullMpn));
            assertEquals("MPQ", handler.extractSeries(fullMpn));
            assertTrue(handler.isAutomotiveGrade(fullMpn));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All MPS parts should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "MP1584EN", "MP2307DN", "MP3302DJ",
                "MP4560GQ", "MP6500GF", "MP8756GL",
                "MPQ4560", "MPM3610"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("LED drivers should not match MOTOR_DRIVER")
        void ledDriversShouldNotMatchMotor() {
            assertFalse(handler.matches("MP3302", ComponentType.MOTOR_DRIVER, registry));
            assertFalse(handler.matches("MPQ3394", ComponentType.MOTOR_DRIVER, registry));
        }

        @Test
        @DisplayName("Motor drivers should not match LED_DRIVER")
        void motorDriversShouldNotMatchLED() {
            assertFalse(handler.matches("MP6500", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("MPQ6543", ComponentType.LED_DRIVER, registry));
        }
    }
}
