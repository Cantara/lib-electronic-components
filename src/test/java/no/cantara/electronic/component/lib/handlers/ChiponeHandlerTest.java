package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ChiponeHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ChiponeHandler (Chipone Technology LED Driver ICs).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class ChiponeHandlerTest {

    private static ChiponeHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ChiponeHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("ICN2xxx Constant Current LED Driver Detection")
    class ICN2xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect ICN2xxx LED drivers")
        @ValueSource(strings = {
            "ICN2024", "ICN2024SS", "ICN2024-SS",
            "ICN2038", "ICN2038S", "ICN2038-SOP",
            "ICN2053", "ICN2053SS", "ICN2053-TR",
            "ICN2065", "ICN2065B", "ICN2065SS"
        })
        void shouldDetectICN2xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("ICN2024 is a popular 24-channel constant current LED driver")
        @ValueSource(strings = {"ICN2024", "ICN2024SS", "ICN2024-SS"})
        void shouldDetectICN2024Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should be detected as LED_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("ICN2053 is a 16-channel LED driver with S-PWM")
        @ValueSource(strings = {"ICN2053", "ICN2053SS", "ICN2053-TR"})
        void shouldDetectICN2053Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should be detected as LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("ICN20xx LED Display Driver Detection")
    class ICN20xxTests {

        @ParameterizedTest
        @DisplayName("Should detect ICN20xx LED display drivers")
        @ValueSource(strings = {
            "ICN2012", "ICN2012S", "ICN2012-SOP",
            "ICN2018", "ICN2018SS",
            "ICN2026", "ICN2026-Q"
        })
        void shouldDetectICN20xxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("ICN2012 is a 12-channel driver")
        void shouldDetectICN2012() {
            assertTrue(handler.matches("ICN2012", ComponentType.LED_DRIVER, registry));
            assertEquals(12, handler.getChannelCount("ICN2012"));
        }

        @Test
        @DisplayName("ICN2018 is an 18-channel driver")
        void shouldDetectICN2018() {
            assertTrue(handler.matches("ICN2018", ComponentType.LED_DRIVER, registry));
            assertEquals(18, handler.getChannelCount("ICN2018"));
        }
    }

    @Nested
    @DisplayName("ICND2xxx Improved Series LED Driver Detection")
    class ICND2xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect ICND2xxx improved LED drivers")
        @ValueSource(strings = {
            "ICND2025", "ICND2025SS", "ICND2025-S",
            "ICND2053", "ICND2053SS", "ICND2053-TR",
            "ICND2110", "ICND2110S", "ICND2110-SOP"
        })
        void shouldDetectICND2xxxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("ICND2053 is the improved version of ICN2053")
        void shouldDetectICND2053() {
            assertTrue(handler.matches("ICND2053", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("ICND2053SS", ComponentType.IC, registry));
            assertEquals(16, handler.getChannelCount("ICND2053"));
        }

        @Test
        @DisplayName("ICND2110 is a 16-channel improved driver")
        void shouldDetectICND2110() {
            assertTrue(handler.matches("ICND2110", ComponentType.LED_DRIVER, registry));
            assertEquals(16, handler.getChannelCount("ICND2110"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes with hyphen separator")
        @CsvSource({
            "ICN2024-SS, SSOP",
            "ICND2110-S, SOP",
            "ICN2053-SOP, SOP",
            "ICND2025-QFN, QFN"
        })
        void shouldExtractPackageCodesWithHyphen(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inline package codes")
        @CsvSource({
            "ICN2024SS, SSOP",
            "ICN2038SS, SSOP",
            "ICND2053SS, SSOP"
        })
        void shouldExtractInlinePackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @Test
        @DisplayName("Should handle tape and reel suffix")
        void shouldHandleTapeReelSuffix() {
            // -TR is tape/reel, not a package code
            String code = handler.extractPackageCode("ICN2053SS-TR");
            assertEquals("SSOP", code);
        }

        @Test
        @DisplayName("Should return empty for variant letters only")
        void shouldReturnEmptyForVariantLetters() {
            // Single letter like 'B' indicates version, not package
            String code = handler.extractPackageCode("ICN2065B");
            assertEquals("", code);
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
        @DisplayName("Should extract ICN2 series")
        @CsvSource({
            "ICN2024, ICN2",
            "ICN2024SS, ICN2",
            "ICN2053-TR, ICN2",
            "ICN2065B, ICN2"
        })
        void shouldExtractICN2Series(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract ICND2 series")
        @CsvSource({
            "ICND2025, ICND2",
            "ICND2053SS, ICND2",
            "ICND2110-S, ICND2"
        })
        void shouldExtractICND2Series(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
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
            assertTrue(handler.isOfficialReplacement("ICN2024", "ICN2024SS"),
                    "ICN2024 and ICN2024SS should be replacements (same part, different package)");
            assertTrue(handler.isOfficialReplacement("ICND2053", "ICND2053SS"),
                    "ICND2053 and ICND2053SS should be replacements");
        }

        @Test
        @DisplayName("Same part with and without suffix should be replacements")
        void samePartWithSuffix() {
            assertTrue(handler.isOfficialReplacement("ICN2053", "ICN2053-TR"),
                    "ICN2053 and ICN2053-TR should be replacements");
            assertTrue(handler.isOfficialReplacement("ICND2110", "ICND2110-SOP"),
                    "ICND2110 and ICND2110-SOP should be replacements");
        }

        @Test
        @DisplayName("ICND2xxx should be replacement for ICN2xxx with same number")
        void icndShouldReplaceIcn() {
            assertTrue(handler.isOfficialReplacement("ICN2053", "ICND2053"),
                    "ICND2053 should be a replacement for ICN2053");
            assertTrue(handler.isOfficialReplacement("ICND2053", "ICN2053"),
                    "ICN2053 and ICND2053 should be mutual replacements");
        }

        @Test
        @DisplayName("Different parts should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ICN2024", "ICN2053"),
                    "ICN2024 and ICN2053 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("ICN2012", "ICN2038"),
                    "ICN2012 and ICN2038 should NOT be replacements");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "ICN2024"));
            assertFalse(handler.isOfficialReplacement("ICN2024", null));
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

        @Test
        @DisplayName("Should NOT support non-LED driver types")
        void shouldNotSupportNonLEDDriverTypes() {
            var types = handler.getSupportedTypes();

            assertFalse(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertFalse(types.contains(ComponentType.MOTOR_DRIVER));
            assertFalse(types.contains(ComponentType.MICROCONTROLLER));
        }
    }

    @Nested
    @DisplayName("Series Description")
    class SeriesDescriptionTests {

        @ParameterizedTest
        @DisplayName("Should return correct series descriptions")
        @CsvSource({
            "ICN2, Constant Current LED Drivers",
            "ICND2, Improved LED Drivers"
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
    @DisplayName("Channel Count")
    class ChannelCountTests {

        @ParameterizedTest
        @DisplayName("Should return correct channel counts")
        @CsvSource({
            "ICN2012, 12",
            "ICN2018, 18",
            "ICN2024, 24",
            "ICN2024SS, 24",
            "ICN2026, 26",
            "ICN2038, 38",
            "ICN2053, 16",
            "ICND2053, 16",
            "ICN2065, 16",
            "ICND2110, 16"
        })
        void shouldReturnChannelCount(String mpn, int expectedChannels) {
            assertEquals(expectedChannels, handler.getChannelCount(mpn),
                    "Channel count for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for unknown parts")
        void shouldReturnNegativeForUnknown() {
            assertEquals(-1, handler.getChannelCount("ICN9999"));
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
            assertFalse(handler.isOfficialReplacement(null, "ICN2024"));
            assertFalse(handler.isOfficialReplacement("ICN2024", null));
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
            assertFalse(handler.matches("ICN2024", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("icn2024", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("ICN2024", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("Icn2024", ComponentType.LED_DRIVER, registry));
        }

        @Test
        @DisplayName("Should not match non-Chipone parts")
        void shouldNotMatchNonChiponeParts() {
            assertFalse(handler.matches("TLC5940", ComponentType.LED_DRIVER, registry),
                    "TLC5940 is a TI part, not Chipone");
            assertFalse(handler.matches("IS31FL3741", ComponentType.LED_DRIVER, registry),
                    "IS31FL3741 is an ISSI part, not Chipone");
            assertFalse(handler.matches("MP3302", ComponentType.LED_DRIVER, registry),
                    "MP3302 is an MPS part, not Chipone");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("ICN", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("ICN2", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("ICND", ComponentType.LED_DRIVER, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular Chipone LED drivers")
        @CsvSource({
            "ICN2024, LED_DRIVER",
            "ICN2024SS, LED_DRIVER",
            "ICN2053, LED_DRIVER",
            "ICN2053SS, LED_DRIVER",
            "ICN2065B, LED_DRIVER",
            "ICND2053, LED_DRIVER",
            "ICND2110, LED_DRIVER"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @Test
        @DisplayName("Should handle full MPN with all suffixes")
        void shouldHandleFullMPN() {
            String fullMpn = "ICN2053SS-TR";
            assertTrue(handler.matches(fullMpn, ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertEquals("SSOP", handler.extractPackageCode(fullMpn));
            assertEquals("ICN2", handler.extractSeries(fullMpn));
            assertEquals(16, handler.getChannelCount(fullMpn));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All Chipone LED drivers should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "ICN2024", "ICN2053", "ICN2065",
                "ICN2012", "ICN2018", "ICN2026",
                "ICND2025", "ICND2053", "ICND2110"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("LED drivers should not match VOLTAGE_REGULATOR")
        void ledDriversShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("ICN2024", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("ICND2053", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("LED drivers should not match MOTOR_DRIVER")
        void ledDriversShouldNotMatchMotorDriver() {
            assertFalse(handler.matches("ICN2024", ComponentType.MOTOR_DRIVER, registry));
            assertFalse(handler.matches("ICND2110", ComponentType.MOTOR_DRIVER, registry));
        }

        @Test
        @DisplayName("LED drivers should not match MICROCONTROLLER")
        void ledDriversShouldNotMatchMicrocontroller() {
            assertFalse(handler.matches("ICN2024", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("ICND2053", ComponentType.MICROCONTROLLER, registry));
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {

        @Test
        @DisplayName("Should return empty manufacturer types set")
        void shouldReturnEmptyManufacturerTypes() {
            assertTrue(handler.getManufacturerTypes().isEmpty(),
                    "Chipone handler should return empty manufacturer types");
        }
    }
}
