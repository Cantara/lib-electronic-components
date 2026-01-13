package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MacroblockHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for MacroblockHandler (Macroblock Inc.).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class MacroblockHandlerTest {

    private static MacroblockHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new MacroblockHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MBI5xxx Constant Current LED Driver Detection")
    class MBI5xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MBI5xxx constant current LED drivers as LED_DRIVER")
        @ValueSource(strings = {
            "MBI5024", "MBI5024GP", "MBI5024GF",
            "MBI5039", "MBI5039GP", "MBI5039GH",
            "MBI5040", "MBI5041", "MBI5042",
            "MBI5050", "MBI5050GP",
            "MBI5124", "MBI5124GP",
            "MBI5153", "MBI5153GP", "MBI5153GF",
            "MBI5252", "MBI5353"
        })
        void shouldDetectMBI5xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("MBI5024 is a popular 16-channel constant current LED driver")
        @ValueSource(strings = {"MBI5024", "MBI5024GP", "MBI5024GF", "MBI5024GH"})
        void shouldDetectMBI5024Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should be detected as LED_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("MBI5039 is a 16-channel driver with error detection")
        @ValueSource(strings = {"MBI5039", "MBI5039GP", "MBI5039GH"})
        void shouldDetectMBI5039Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should be detected as LED_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("MBI5153 is a 48-channel driver for high-refresh displays")
        @ValueSource(strings = {"MBI5153", "MBI5153GP", "MBI5153GF", "MBI5153GH"})
        void shouldDetectMBI5153Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should be detected as LED_DRIVER");
        }

        @Test
        @DisplayName("MBI5xxx should be identified as constant current drivers")
        void shouldIdentifyAsConstantCurrentDriver() {
            assertTrue(handler.isConstantCurrentDriver("MBI5024"));
            assertTrue(handler.isConstantCurrentDriver("MBI5039GP"));
            assertTrue(handler.isConstantCurrentDriver("MBI5153"));
            assertFalse(handler.isConstantCurrentDriver("MBI6651"));
            assertFalse(handler.isConstantCurrentDriver("MBI1801"));
        }
    }

    @Nested
    @DisplayName("MBI6xxx DC-DC LED Driver Detection")
    class MBI6xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MBI6xxx DC-DC LED drivers as LED_DRIVER")
        @ValueSource(strings = {
            "MBI6651", "MBI6651GS", "MBI6651GT",
            "MBI6652", "MBI6653", "MBI6654",
            "MBI6655", "MBI6656", "MBI6657",
            "MBI6658", "MBI6659", "MBI6660"
        })
        void shouldDetectMBI6xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("MBI6xxx should be identified as DC-DC drivers")
        void shouldIdentifyAsDCDCDriver() {
            assertTrue(handler.isDCDCDriver("MBI6651"));
            assertTrue(handler.isDCDCDriver("MBI6652GS"));
            assertTrue(handler.isDCDCDriver("MBI6660"));
            assertFalse(handler.isDCDCDriver("MBI5024"));
            assertFalse(handler.isDCDCDriver("MBI1801"));
        }
    }

    @Nested
    @DisplayName("MBI1xxx Scan Driver Detection")
    class MBI1xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect MBI1xxx scan drivers as LED_DRIVER")
        @ValueSource(strings = {
            "MBI1801", "MBI1801GT", "MBI1801TE",
            "MBI1802", "MBI1803", "MBI1804",
            "MBI1810", "MBI1820", "MBI1830"
        })
        void shouldDetectMBI1xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("MBI1xxx should be identified as scan drivers")
        void shouldIdentifyAsScanDriver() {
            assertTrue(handler.isScanDriver("MBI1801"));
            assertTrue(handler.isScanDriver("MBI1802GT"));
            assertTrue(handler.isScanDriver("MBI1830"));
            assertFalse(handler.isScanDriver("MBI5024"));
            assertFalse(handler.isScanDriver("MBI6651"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract standard package codes")
        @CsvSource({
            "MBI5024GP, SOP-24",
            "MBI5024GF, SSOP-24",
            "MBI5024GH, TSSOP-24",
            "MBI5039GS, SSOP-24",
            "MBI5039GT, TSSOP-24",
            "MBI5153GN, QFN",
            "MBI5153GQ, QFN"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should handle single-letter package codes")
        @CsvSource({
            "MBI5024S, SOP",
            "MBI5024T, TSSOP",
            "MBI5024N, QFN",
            "MBI5024P, PDIP"
        })
        void shouldHandleSingleLetterCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @Test
        @DisplayName("Should return raw code for unknown package codes")
        void shouldReturnRawCodeForUnknown() {
            String result = handler.extractPackageCode("MBI5024XY");
            // Should return XY if not in lookup table
            assertEquals("XY", result);
        }

        @Test
        @DisplayName("Should handle tape and reel suffix")
        void shouldHandleTapeAndReelSuffix() {
            assertEquals("SOP-24", handler.extractPackageCode("MBI5024GP-TR"));
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
        @DisplayName("Should extract MBI5 series codes")
        @CsvSource({
            "MBI5024GP, MBI5",
            "MBI5039GH, MBI5",
            "MBI5153, MBI5",
            "MBI5252GP, MBI5",
            "MBI5353, MBI5"
        })
        void shouldExtractMBI5SeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MBI6 series codes")
        @CsvSource({
            "MBI6651GS, MBI6",
            "MBI6652, MBI6",
            "MBI6660GT, MBI6"
        })
        void shouldExtractMBI6SeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MBI1 series codes")
        @CsvSource({
            "MBI1801GT, MBI1",
            "MBI1802, MBI1",
            "MBI1830TE, MBI1"
        })
        void shouldExtractMBI1SeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalid() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("INVALID"));
            assertEquals("", handler.extractSeries("LM7805"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("MBI5024GP", "MBI5024GF"),
                    "MBI5024GP and MBI5024GF should be replacements (same part, different package)");
            assertTrue(handler.isOfficialReplacement("MBI5039GH", "MBI5039GP"),
                    "MBI5039GH and MBI5039GP should be replacements");
        }

        @Test
        @DisplayName("Same part with and without suffix should be replacements")
        void samePartWithSuffix() {
            assertTrue(handler.isOfficialReplacement("MBI5024", "MBI5024GP"),
                    "MBI5024 and MBI5024GP should be replacements");
            assertTrue(handler.isOfficialReplacement("MBI5153GP", "MBI5153GP-TR"),
                    "MBI5153GP and MBI5153GP-TR should be replacements");
        }

        @Test
        @DisplayName("Different parts should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("MBI5024GP", "MBI5039GP"),
                    "MBI5024 and MBI5039 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("MBI5024", "MBI5153"),
                    "MBI5024 (16ch) and MBI5153 (48ch) should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("MBI5024", "MBI6651"),
                    "MBI5024 and MBI6651 should NOT be replacements (different series)");
            assertFalse(handler.isOfficialReplacement("MBI5024", "MBI1801"),
                    "MBI5024 and MBI1801 should NOT be replacements (different series)");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "MBI5024GP"));
            assertFalse(handler.isOfficialReplacement("MBI5024GP", null));
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
            assertTrue(types.contains(ComponentType.LED_DRIVER));
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
            "MBI5, Constant Current LED Drivers",
            "MBI6, DC-DC LED Drivers",
            "MBI1, Scan Drivers"
        })
        void shouldReturnSeriesDescription(String series, String expectedDesc) {
            assertEquals(expectedDesc, handler.getSeriesDescription(series));
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getSeriesDescription("UNKNOWN"));
            assertEquals("", handler.getSeriesDescription(""));
            assertEquals("", handler.getSeriesDescription("MBI9"));
        }
    }

    @Nested
    @DisplayName("Channel Count Detection")
    class ChannelCountTests {

        @ParameterizedTest
        @DisplayName("Should detect 16-channel LED drivers")
        @CsvSource({
            "MBI5024, 16",
            "MBI5024GP, 16",
            "MBI5039, 16",
            "MBI5039GH, 16",
            "MBI5040, 16",
            "MBI5041, 16",
            "MBI5042, 16",
            "MBI5050, 16",
            "MBI5124, 16"
        })
        void shouldDetect16ChannelDrivers(String mpn, int expectedChannels) {
            assertEquals(expectedChannels, handler.getChannelCount(mpn),
                    mpn + " should have " + expectedChannels + " channels");
        }

        @ParameterizedTest
        @DisplayName("Should detect 48-channel LED drivers")
        @CsvSource({
            "MBI5153, 48",
            "MBI5153GP, 48",
            "MBI5252, 48",
            "MBI5353, 48"
        })
        void shouldDetect48ChannelDrivers(String mpn, int expectedChannels) {
            assertEquals(expectedChannels, handler.getChannelCount(mpn),
                    mpn + " should have " + expectedChannels + " channels");
        }

        @Test
        @DisplayName("Should return -1 for unknown parts")
        void shouldReturnMinusOneForUnknown() {
            assertEquals(-1, handler.getChannelCount("MBI5999"));
            assertEquals(-1, handler.getChannelCount("MBI6651"));
            assertEquals(-1, handler.getChannelCount(null));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.LED_DRIVER, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "MBI5024"));
            assertFalse(handler.isOfficialReplacement("MBI5024", null));
            assertFalse(handler.isConstantCurrentDriver(null));
            assertFalse(handler.isDCDCDriver(null));
            assertFalse(handler.isScanDriver(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.LED_DRIVER, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("MBI5024GP", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("mbi5024gp", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("MBI5024GP", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("Mbi5024Gp", ComponentType.LED_DRIVER, registry));
        }

        @Test
        @DisplayName("Should not match non-Macroblock parts")
        void shouldNotMatchNonMacroblockParts() {
            assertFalse(handler.matches("LM7805", ComponentType.LED_DRIVER, registry),
                    "LM7805 is not a Macroblock part");
            assertFalse(handler.matches("TPS54331", ComponentType.LED_DRIVER, registry),
                    "TPS54331 is not a Macroblock part");
            assertFalse(handler.matches("MP3302", ComponentType.LED_DRIVER, registry),
                    "MP3302 is MPS, not Macroblock");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("MBI", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("MBI5", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("MBI50", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("MBI502", ComponentType.LED_DRIVER, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular Macroblock parts")
        @CsvSource({
            "MBI5024GP, LED_DRIVER",
            "MBI5039GH, LED_DRIVER",
            "MBI5153GP, LED_DRIVER",
            "MBI6651GS, LED_DRIVER",
            "MBI1801GT, LED_DRIVER"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @Test
        @DisplayName("Should handle full MPN with all suffixes")
        void shouldHandleFullMPN() {
            String fullMpn = "MBI5024GP-TR";
            assertTrue(handler.matches(fullMpn, ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertEquals("SOP-24", handler.extractPackageCode(fullMpn));
            assertEquals("MBI5", handler.extractSeries(fullMpn));
            assertTrue(handler.isConstantCurrentDriver(fullMpn));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All Macroblock parts should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "MBI5024", "MBI5039GP", "MBI5153",
                "MBI6651", "MBI6652GS",
                "MBI1801", "MBI1802GT"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("All Macroblock parts should match LED_DRIVER")
        void allPartsShouldMatchLEDDriver() {
            String[] testMpns = {
                "MBI5024", "MBI5039GP", "MBI5153",
                "MBI6651", "MBI6652GS",
                "MBI1801", "MBI1802GT"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                        mpn + " should match LED_DRIVER");
            }
        }

        @Test
        @DisplayName("Macroblock parts should NOT match VOLTAGE_REGULATOR")
        void shouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("MBI5024", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MBI5024 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("MBI6651", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MBI6651 should NOT match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Macroblock parts should NOT match MOTOR_DRIVER")
        void shouldNotMatchMotorDriver() {
            assertFalse(handler.matches("MBI5024", ComponentType.MOTOR_DRIVER, registry),
                    "MBI5024 should NOT match MOTOR_DRIVER");
        }
    }

    @Nested
    @DisplayName("Driver Type Classification")
    class DriverTypeTests {

        @Test
        @DisplayName("Should correctly classify driver types")
        void shouldClassifyDriverTypes() {
            // MBI5xxx - Constant Current
            assertTrue(handler.isConstantCurrentDriver("MBI5024"));
            assertFalse(handler.isDCDCDriver("MBI5024"));
            assertFalse(handler.isScanDriver("MBI5024"));

            // MBI6xxx - DC-DC
            assertFalse(handler.isConstantCurrentDriver("MBI6651"));
            assertTrue(handler.isDCDCDriver("MBI6651"));
            assertFalse(handler.isScanDriver("MBI6651"));

            // MBI1xxx - Scan
            assertFalse(handler.isConstantCurrentDriver("MBI1801"));
            assertFalse(handler.isDCDCDriver("MBI1801"));
            assertTrue(handler.isScanDriver("MBI1801"));
        }
    }
}
