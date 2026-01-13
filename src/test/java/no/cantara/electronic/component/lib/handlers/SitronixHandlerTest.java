package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SitronixHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SitronixHandler (Sitronix Technology Corporation).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Sitronix is known for display controllers and LED drivers:
 * - ST75xx: TFT LCD controllers
 * - ST77xx: TFT display drivers
 * - ST7920: Graphic LCD controller
 * - ST16xx: LED drivers
 */
class SitronixHandlerTest {

    private static SitronixHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SitronixHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("ST75xx TFT LCD Controller Detection")
    class ST75xxTests {

        @ParameterizedTest
        @DisplayName("Should detect ST75xx TFT LCD controllers as IC")
        @ValueSource(strings = {
            "ST7565", "ST7565R", "ST7565P",
            "ST7567", "ST7567A", "ST7567S",
            "ST7571", "ST7580"
        })
        void shouldDetectST75xxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("ST7565 is a popular 65x132 LCD controller")
        @ValueSource(strings = {"ST7565R", "ST7565P", "ST7565S"})
        void shouldDetectST7565Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
            assertTrue(handler.isTFTLCDController(mpn),
                    mpn + " should be identified as TFT LCD controller");
        }

        @Test
        @DisplayName("ST7567 is a common segment LCD driver")
        void shouldDetectST7567() {
            assertTrue(handler.matches("ST7567A", ComponentType.IC, registry));
            assertTrue(handler.isTFTLCDController("ST7567A"));
        }

        @Test
        @DisplayName("ST75xx should NOT match LED_DRIVER")
        void st75xxShouldNotMatchLEDDriver() {
            assertFalse(handler.matches("ST7565", ComponentType.LED_DRIVER, registry),
                    "ST7565 should NOT match LED_DRIVER");
            assertFalse(handler.matches("ST7567", ComponentType.LED_DRIVER, registry),
                    "ST7567 should NOT match LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("ST77xx TFT Display Driver Detection")
    class ST77xxTests {

        @ParameterizedTest
        @DisplayName("Should detect ST77xx TFT display drivers as IC")
        @ValueSource(strings = {
            "ST7735", "ST7735S", "ST7735R",
            "ST7789", "ST7789V", "ST7789VW",
            "ST7796", "ST7796S"
        })
        void shouldDetectST77xxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("ST7735 is a popular 1.8in TFT driver")
        void shouldDetectST7735() {
            assertTrue(handler.matches("ST7735S", ComponentType.IC, registry));
            assertTrue(handler.isTFTDisplayDriver("ST7735S"));
            assertFalse(handler.isTFTLCDController("ST7735S"),
                    "ST7735 is in ST77xx series, not ST75xx");
        }

        @Test
        @DisplayName("ST7789 is a popular 240x320 TFT driver")
        void shouldDetectST7789() {
            assertTrue(handler.matches("ST7789V", ComponentType.IC, registry));
            assertTrue(handler.matches("ST7789VW", ComponentType.IC, registry));
            assertTrue(handler.isTFTDisplayDriver("ST7789V"));
        }

        @Test
        @DisplayName("ST7796 is a 320x480 TFT driver")
        void shouldDetectST7796() {
            assertTrue(handler.matches("ST7796S", ComponentType.IC, registry));
            assertTrue(handler.isTFTDisplayDriver("ST7796S"));
        }

        @Test
        @DisplayName("ST77xx should NOT match LED_DRIVER")
        void st77xxShouldNotMatchLEDDriver() {
            assertFalse(handler.matches("ST7735", ComponentType.LED_DRIVER, registry),
                    "ST7735 should NOT match LED_DRIVER");
            assertFalse(handler.matches("ST7789", ComponentType.LED_DRIVER, registry),
                    "ST7789 should NOT match LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("ST7920 Graphic LCD Controller Detection")
    class ST7920Tests {

        @ParameterizedTest
        @DisplayName("Should detect ST7920 graphic LCD controllers as IC")
        @ValueSource(strings = {
            "ST7920", "ST7920-0B", "ST7920-0S"
        })
        void shouldDetectST7920AsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("ST7920 is a 128x64 graphic LCD controller")
        void shouldDetectST7920() {
            assertTrue(handler.matches("ST7920", ComponentType.IC, registry));
            assertTrue(handler.isGraphicLCDController("ST7920"));
        }

        @Test
        @DisplayName("ST7920 should NOT match LED_DRIVER")
        void st7920ShouldNotMatchLEDDriver() {
            assertFalse(handler.matches("ST7920", ComponentType.LED_DRIVER, registry),
                    "ST7920 should NOT match LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("ST16xx LED Driver Detection")
    class ST16xxTests {

        @ParameterizedTest
        @DisplayName("Should detect ST16xx as LED_DRIVER")
        @ValueSource(strings = {
            "ST1628", "ST1633", "ST1622",
            "ST1629", "ST1632"
        })
        void shouldDetectST16xxAsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("ST1628 is a common LED driver with keyboard scan")
        void shouldDetectST1628() {
            assertTrue(handler.matches("ST1628", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("ST1628", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("ST1633 is an LED driver with serial interface")
        void shouldDetectST1633() {
            assertTrue(handler.matches("ST1633", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("ST1633", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from letter suffixes")
        @CsvSource({
            "ST7735S, SOP",
            "ST7789V, LQFP",
            "ST7789R, QFN"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle hyphenated MPNs")
        void shouldHandleHyphenatedMpn() {
            // The base part before hyphen should be used
            String pkg = handler.extractPackageCode("ST7920-0B");
            // ST7920 doesn't have a package suffix, so returns empty
            assertEquals("", pkg);
        }

        @Test
        @DisplayName("Should handle empty and null MPN")
        void shouldHandleEmptyAndNull() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }

        @Test
        @DisplayName("Should return raw code for unknown package codes")
        void shouldReturnRawCodeForUnknown() {
            String result = handler.extractPackageCode("ST7789X");
            assertEquals("X", result);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series codes")
        @CsvSource({
            "ST7565R, ST75",
            "ST7567A, ST75",
            "ST7735S, ST77",
            "ST7789V, ST77",
            "ST7920, ST79",
            "ST1628, ST16",
            "ST1633, ST16"
        })
        void shouldExtractSeriesCodes(String mpn, String expectedSeries) {
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

        @Test
        @DisplayName("Should not match STMicroelectronics parts")
        void shouldNotMatchSTMicroParts() {
            // STM32 is STMicroelectronics, not Sitronix
            assertEquals("", handler.extractSeries("STM32F103"));
            assertFalse(handler.matches("STM32F103", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("ST7735S", "ST7735R"),
                    "ST7735S and ST7735R should be replacements (same part, different package)");
            assertTrue(handler.isOfficialReplacement("ST7789V", "ST7789R"),
                    "ST7789V and ST7789R should be replacements");
        }

        @Test
        @DisplayName("Same part with and without suffix should be replacements")
        void samePartWithSuffix() {
            assertTrue(handler.isOfficialReplacement("ST7735", "ST7735S"),
                    "ST7735 and ST7735S should be replacements");
            assertTrue(handler.isOfficialReplacement("ST7789", "ST7789V"),
                    "ST7789 and ST7789V should be replacements");
        }

        @Test
        @DisplayName("Different parts should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ST7735S", "ST7789V"),
                    "ST7735 and ST7789 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("ST7565", "ST7735"),
                    "ST7565 and ST7735 should NOT be replacements (different series)");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ST7565", "ST1628"),
                    "ST7565 (LCD) and ST1628 (LED driver) should NOT be replacements");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "ST7735S"));
            assertFalse(handler.isOfficialReplacement("ST7735S", null));
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
            "ST75, TFT LCD Controllers",
            "ST77, TFT Display Drivers",
            "ST79, Graphic LCD Controllers",
            "ST16, LED Drivers"
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
    @DisplayName("Component Type Detection Methods")
    class ComponentTypeDetectionTests {

        @Test
        @DisplayName("isTFTLCDController should identify ST75xx")
        void shouldIdentifyTFTLCDController() {
            assertTrue(handler.isTFTLCDController("ST7565"));
            assertTrue(handler.isTFTLCDController("ST7567A"));
            assertTrue(handler.isTFTLCDController("ST7571"));
            assertFalse(handler.isTFTLCDController("ST7735"));
            assertFalse(handler.isTFTLCDController("ST7789"));
            assertFalse(handler.isTFTLCDController(null));
        }

        @Test
        @DisplayName("isTFTDisplayDriver should identify ST77xx")
        void shouldIdentifyTFTDisplayDriver() {
            assertTrue(handler.isTFTDisplayDriver("ST7735"));
            assertTrue(handler.isTFTDisplayDriver("ST7789V"));
            assertTrue(handler.isTFTDisplayDriver("ST7796S"));
            assertFalse(handler.isTFTDisplayDriver("ST7565"));
            assertFalse(handler.isTFTDisplayDriver("ST7920"));
            assertFalse(handler.isTFTDisplayDriver(null));
        }

        @Test
        @DisplayName("isGraphicLCDController should identify ST79xx")
        void shouldIdentifyGraphicLCDController() {
            assertTrue(handler.isGraphicLCDController("ST7920"));
            assertTrue(handler.isGraphicLCDController("ST7920-0B"));
            assertFalse(handler.isGraphicLCDController("ST7735"));
            assertFalse(handler.isGraphicLCDController("ST7565"));
            assertFalse(handler.isGraphicLCDController(null));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "ST7735"));
            assertFalse(handler.isOfficialReplacement("ST7735", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("ST7735S", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("st7735s", ComponentType.IC, registry));
            assertTrue(handler.matches("ST7735S", ComponentType.IC, registry));
            assertTrue(handler.matches("St7735s", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should not match non-Sitronix parts")
        void shouldNotMatchNonSitronixParts() {
            // STMicroelectronics parts
            assertFalse(handler.matches("STM32F103", ComponentType.IC, registry),
                    "STM32F103 is STMicroelectronics, not Sitronix");
            assertFalse(handler.matches("STM8S003", ComponentType.IC, registry),
                    "STM8S003 is STMicroelectronics, not Sitronix");

            // Other parts starting with ST
            assertFalse(handler.matches("ST62T01", ComponentType.IC, registry),
                    "ST62T01 is STMicroelectronics");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("ST", ComponentType.IC, registry));
            assertFalse(handler.matches("ST7", ComponentType.IC, registry));
            assertFalse(handler.matches("ST77", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular Sitronix display controllers")
        @CsvSource({
            "ST7735S, IC",
            "ST7789V, IC",
            "ST7789VW, IC",
            "ST7920, IC",
            "ST7920-0B, IC",
            "ST7565R, IC",
            "ST7567A, IC",
            "ST1628, LED_DRIVER",
            "ST1633, LED_DRIVER"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @Test
        @DisplayName("ST7789VW is a popular display driver for Arduino projects")
        void shouldHandleST7789VW() {
            assertTrue(handler.matches("ST7789VW", ComponentType.IC, registry));
            assertEquals("ST77", handler.extractSeries("ST7789VW"));
            assertTrue(handler.isTFTDisplayDriver("ST7789VW"));
        }

        @Test
        @DisplayName("ST7920 is commonly used in 12864 LCD modules")
        void shouldHandleST7920() {
            assertTrue(handler.matches("ST7920", ComponentType.IC, registry));
            assertEquals("ST79", handler.extractSeries("ST7920"));
            assertTrue(handler.isGraphicLCDController("ST7920"));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All Sitronix display controllers should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "ST7565", "ST7567", "ST7735S",
                "ST7789V", "ST7920", "ST1628", "ST1633"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("Only ST16xx should match LED_DRIVER")
        void onlyST16xxShouldMatchLEDDriver() {
            // ST16xx are LED drivers
            assertTrue(handler.matches("ST1628", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("ST1633", ComponentType.LED_DRIVER, registry));

            // Other series should not match LED_DRIVER
            assertFalse(handler.matches("ST7565", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("ST7735", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("ST7789", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("ST7920", ComponentType.LED_DRIVER, registry));
        }
    }

    @Nested
    @DisplayName("Cross-Handler Pattern Isolation")
    class CrossHandlerTests {

        @Test
        @DisplayName("Should not match STMicroelectronics MCU patterns")
        void shouldNotMatchSTMicroMCU() {
            // These are STMicroelectronics, not Sitronix
            assertFalse(handler.matches("STM32F103C8T6", ComponentType.IC, registry));
            assertFalse(handler.matches("STM32F407VGT6", ComponentType.IC, registry));
            assertFalse(handler.matches("STM8S103F3P6", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should not match STMicroelectronics MOSFET patterns")
        void shouldNotMatchSTMicroMOSFET() {
            assertFalse(handler.matches("STF10N60", ComponentType.IC, registry));
            assertFalse(handler.matches("STP55NF06", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should only match Sitronix ST[75|77|79|16]xx patterns")
        void shouldOnlyMatchSitronixPatterns() {
            // Valid Sitronix patterns
            assertTrue(handler.matches("ST7565", ComponentType.IC, registry));
            assertTrue(handler.matches("ST7735", ComponentType.IC, registry));
            assertTrue(handler.matches("ST7920", ComponentType.IC, registry));
            assertTrue(handler.matches("ST1628", ComponentType.IC, registry));

            // Invalid patterns (not Sitronix)
            assertFalse(handler.matches("ST1234", ComponentType.IC, registry));
            assertFalse(handler.matches("ST8000", ComponentType.IC, registry));
            assertFalse(handler.matches("ST6000", ComponentType.IC, registry));
        }
    }
}
