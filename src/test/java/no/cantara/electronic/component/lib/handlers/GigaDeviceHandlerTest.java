package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.GigaDeviceHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for GigaDeviceHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction, density extraction,
 * and replacement detection for GigaDevice Flash Memory and MCU products.
 * <p>
 * GigaDevice product families covered:
 * <ul>
 *   <li>GD25Qxx - Serial NOR Flash (standard SPI)</li>
 *   <li>GD25Bxx - Wide Voltage Serial Flash</li>
 *   <li>GD25LQxx - Low Power Serial Flash</li>
 *   <li>GD25WQxx - Wide Voltage Low Power Flash</li>
 *   <li>GD25Txx - High Performance Flash</li>
 *   <li>GD5Fxxxx - SLC NAND Flash</li>
 *   <li>GD32F1xx - ARM Cortex-M3 MCU</li>
 *   <li>GD32F3xx - ARM Cortex-M4 MCU</li>
 *   <li>GD32F4xx - High Performance MCU</li>
 *   <li>GD32E1xx/GD32E2xx - Enhanced MCUs</li>
 *   <li>GD32VF1xx - RISC-V MCU</li>
 * </ul>
 */
class GigaDeviceHandlerTest {

    private static GigaDeviceHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new GigaDeviceHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("GD25Q Serial NOR Flash Detection")
    class GD25QSerialNORFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect GD25Q series standard serial NOR flash")
        @ValueSource(strings = {
                "GD25Q16CSIG",
                "GD25Q32CSIG",
                "GD25Q64CSIG",
                "GD25Q128CSIG",
                "GD25Q256CSIG",
                "GD25Q16ESIG",
                "GD25Q64EWIGR",
                "GD25Q128EWIG"
        })
        void shouldDetectGD25QFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("GD25Q series should extract correct series")
        void shouldExtractGD25QSeries() {
            assertEquals("GD25Q", handler.extractSeries("GD25Q16CSIG"));
            assertEquals("GD25Q", handler.extractSeries("GD25Q128CSIG"));
            assertEquals("GD25Q", handler.extractSeries("GD25Q64EWIGR"));
        }

        @Test
        @DisplayName("GD25Q series should extract correct density")
        void shouldExtractGD25QDensity() {
            assertEquals("16", handler.extractDensity("GD25Q16CSIG"));
            assertEquals("32", handler.extractDensity("GD25Q32CSIG"));
            assertEquals("64", handler.extractDensity("GD25Q64CSIG"));
            assertEquals("128", handler.extractDensity("GD25Q128CSIG"));
            assertEquals("256", handler.extractDensity("GD25Q256CSIG"));
        }
    }

    @Nested
    @DisplayName("GD25B Wide Voltage Flash Detection")
    class GD25BWideVoltageFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect GD25B series wide voltage flash")
        @ValueSource(strings = {
                "GD25B64CSIG",
                "GD25B128CSIG",
                "GD25B256CSIG",
                "GD25B64EWIG",
                "GD25B128EWIG"
        })
        void shouldDetectGD25BFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("GD25B series should extract correct series")
        void shouldExtractGD25BSeries() {
            assertEquals("GD25B", handler.extractSeries("GD25B64CSIG"));
            assertEquals("GD25B", handler.extractSeries("GD25B128CSIG"));
        }

        @Test
        @DisplayName("GD25B series should extract correct density")
        void shouldExtractGD25BDensity() {
            assertEquals("64", handler.extractDensity("GD25B64CSIG"));
            assertEquals("128", handler.extractDensity("GD25B128CSIG"));
            assertEquals("256", handler.extractDensity("GD25B256CSIG"));
        }
    }

    @Nested
    @DisplayName("GD25LQ Low Power Flash Detection")
    class GD25LQLowPowerFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect GD25LQ series low power flash")
        @ValueSource(strings = {
                "GD25LQ16CSIG",
                "GD25LQ32CSIG",
                "GD25LQ64CSIG",
                "GD25LQ128CSIG",
                "GD25LQ16EWIG"
        })
        void shouldDetectGD25LQFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("GD25LQ series should extract correct series")
        void shouldExtractGD25LQSeries() {
            assertEquals("GD25LQ", handler.extractSeries("GD25LQ16CSIG"));
            assertEquals("GD25LQ", handler.extractSeries("GD25LQ128CSIG"));
        }

        @Test
        @DisplayName("GD25LQ series should extract correct density")
        void shouldExtractGD25LQDensity() {
            assertEquals("16", handler.extractDensity("GD25LQ16CSIG"));
            assertEquals("32", handler.extractDensity("GD25LQ32CSIG"));
            assertEquals("64", handler.extractDensity("GD25LQ64CSIG"));
        }
    }

    @Nested
    @DisplayName("GD25WQ Wide Voltage Low Power Flash Detection")
    class GD25WQWideVoltageLowPowerTests {

        @ParameterizedTest
        @DisplayName("Should detect GD25WQ series wide voltage low power flash")
        @ValueSource(strings = {
                "GD25WQ64EWIG",
                "GD25WQ80EWIG",
                "GD25WQ16ESIG",
                "GD25WQ32ESIG"
        })
        void shouldDetectGD25WQFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("GD25WQ series should extract correct series")
        void shouldExtractGD25WQSeries() {
            assertEquals("GD25WQ", handler.extractSeries("GD25WQ64EWIG"));
            assertEquals("GD25WQ", handler.extractSeries("GD25WQ80EWIG"));
        }
    }

    @Nested
    @DisplayName("GD5F NAND Flash Detection")
    class GD5FNANDFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect GD5F series SLC NAND flash")
        @ValueSource(strings = {
                "GD5F1GM7UEYIG",
                "GD5F2GM7UEYIG",
                "GD5F1GQ5UEYIG",
                "GD5F2GQ5UEYIG",
                "GD5F4GM5UEYIG"
        })
        void shouldDetectGD5FFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("GD5F series should extract correct series")
        void shouldExtractGD5FSeries() {
            assertEquals("GD5F", handler.extractSeries("GD5F1GM7UEYIG"));
            assertEquals("GD5F", handler.extractSeries("GD5F2GM7UEYIG"));
        }

        @Test
        @DisplayName("GD5F series should extract correct density")
        void shouldExtractGD5FDensity() {
            assertEquals("1G", handler.extractDensity("GD5F1GM7UEYIG"));
            assertEquals("2G", handler.extractDensity("GD5F2GM7UEYIG"));
            assertEquals("4G", handler.extractDensity("GD5F4GM5UEYIG"));
        }
    }

    @Nested
    @DisplayName("GD32F1xx Cortex-M3 MCU Detection")
    class GD32F1xxCortexM3Tests {

        @ParameterizedTest
        @DisplayName("Should detect GD32F1xx series Cortex-M3 MCUs")
        @ValueSource(strings = {
                "GD32F103C8T6",
                "GD32F103CBT6",
                "GD32F103RCT6",
                "GD32F103VET6",
                "GD32F130C8T6",
                "GD32F150C8T6"
        })
        void shouldDetectGD32F1xxMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("GD32F1 series should extract correct series")
        void shouldExtractGD32F1Series() {
            assertEquals("GD32F1", handler.extractSeries("GD32F103C8T6"));
            assertEquals("GD32F1", handler.extractSeries("GD32F130C8T6"));
            assertEquals("GD32F1", handler.extractSeries("GD32F150C8T6"));
        }

        @Test
        @DisplayName("GD32F1xx should not match as MEMORY")
        void shouldNotMatchAsMemory() {
            assertFalse(handler.matches("GD32F103C8T6", ComponentType.MEMORY, registry));
            assertFalse(handler.matches("GD32F103C8T6", ComponentType.MEMORY_FLASH, registry));
        }
    }

    @Nested
    @DisplayName("GD32F3xx Cortex-M4 MCU Detection")
    class GD32F3xxCortexM4Tests {

        @ParameterizedTest
        @DisplayName("Should detect GD32F3xx series Cortex-M4 MCUs")
        @ValueSource(strings = {
                "GD32F303CCT6",
                "GD32F303RCT6",
                "GD32F303VET6",
                "GD32F330C8T6",
                "GD32F350C8T6"
        })
        void shouldDetectGD32F3xxMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("GD32F3 series should extract correct series")
        void shouldExtractGD32F3Series() {
            assertEquals("GD32F3", handler.extractSeries("GD32F303CCT6"));
            assertEquals("GD32F3", handler.extractSeries("GD32F330C8T6"));
        }
    }

    @Nested
    @DisplayName("GD32F4xx High Performance MCU Detection")
    class GD32F4xxHighPerformanceTests {

        @ParameterizedTest
        @DisplayName("Should detect GD32F4xx series high performance MCUs")
        @ValueSource(strings = {
                "GD32F403RGT6",
                "GD32F403VET6",
                "GD32F450VIT6",
                "GD32F450ZIT6",
                "GD32F470ZIT6"
        })
        void shouldDetectGD32F4xxMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("GD32F4 series should extract correct series")
        void shouldExtractGD32F4Series() {
            assertEquals("GD32F4", handler.extractSeries("GD32F403RGT6"));
            assertEquals("GD32F4", handler.extractSeries("GD32F450VIT6"));
        }
    }

    @Nested
    @DisplayName("GD32E Enhanced MCU Detection")
    class GD32EEnhancedMCUTests {

        @ParameterizedTest
        @DisplayName("Should detect GD32E series enhanced MCUs")
        @ValueSource(strings = {
                "GD32E103C8T6",
                "GD32E103RBT6",
                "GD32E230C8T6",
                "GD32E230K8T6",
                "GD32E503RCT6"
        })
        void shouldDetectGD32EMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("GD32E series should extract correct series")
        void shouldExtractGD32ESeries() {
            assertEquals("GD32E1", handler.extractSeries("GD32E103C8T6"));
            assertEquals("GD32E2", handler.extractSeries("GD32E230C8T6"));
            assertEquals("GD32E5", handler.extractSeries("GD32E503RCT6"));
        }
    }

    @Nested
    @DisplayName("GD32VF RISC-V MCU Detection")
    class GD32VFRiscVTests {

        @ParameterizedTest
        @DisplayName("Should detect GD32VF series RISC-V MCUs")
        @ValueSource(strings = {
                "GD32VF103C8T6",
                "GD32VF103CBT6",
                "GD32VF103RBT6",
                "GD32VF103VBT6"
        })
        void shouldDetectGD32VFMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("GD32VF series should extract correct series")
        void shouldExtractGD32VFSeries() {
            assertEquals("GD32VF1", handler.extractSeries("GD32VF103C8T6"));
            assertEquals("GD32VF1", handler.extractSeries("GD32VF103CBT6"));
        }

        @Test
        @DisplayName("GD32VF should not match as MEMORY")
        void shouldNotMatchAsMemory() {
            assertFalse(handler.matches("GD32VF103C8T6", ComponentType.MEMORY, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction - Flash Memory")
    class FlashPackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for serial NOR flash")
        @CsvSource({
                "GD25Q128CSIG, SOP-8",
                "GD25Q64ESIG, SOP-8",
                "GD25Q64EWIG, WSON-8",
                "GD25Q64EWIGR, WSON-8",
                "GD25Q128EWIG, WSON-8"
        })
        void shouldExtractFlashPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle tape and reel suffix")
        void shouldHandleTapeAndReelSuffix() {
            // R suffix indicates tape and reel - should still extract package
            assertEquals("WSON-8", handler.extractPackageCode("GD25Q64EWIGR"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction - MCU")
    class MCUPackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for MCUs")
        @CsvSource({
                "GD32F103C8T6, LQFP48",
                "GD32F103CBT6, LQFP48",
                "GD32F103RCT6, LQFP64",
                "GD32F103VET6, LQFP100",
                "GD32VF103C8T6, LQFP48",
                "GD32VF103CBT6, LQFP48"
        })
        void shouldExtractMCUPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series for all product families")
        @CsvSource({
                "GD25Q128CSIG, GD25Q",
                "GD25B64CSIG, GD25B",
                "GD25LQ32CSIG, GD25LQ",
                "GD25WQ64EWIG, GD25WQ",
                "GD25T64SIG, GD25T",
                "GD5F1GM7UEYIG, GD5F",
                "GD32F103C8T6, GD32F1",
                "GD32F303CCT6, GD32F3",
                "GD32F403RGT6, GD32F4",
                "GD32E230C8T6, GD32E2",
                "GD32VF103CBT6, GD32VF1"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-GigaDevice parts")
        void shouldReturnEmptyForNonGigaDeviceParts() {
            assertEquals("", handler.extractSeries("W25Q64"));       // Winbond
            assertEquals("", handler.extractSeries("MX25L128"));     // Macronix
            assertEquals("", handler.extractSeries("STM32F103"));    // ST
            assertEquals("", handler.extractSeries("AT25SF128"));    // Adesto
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle case insensitivity")
        void shouldHandleCaseInsensitivity() {
            assertEquals("GD25Q", handler.extractSeries("gd25q128csig"));
            assertEquals("GD32F1", handler.extractSeries("gd32f103c8t6"));
        }
    }

    @Nested
    @DisplayName("Density Extraction - Flash")
    class DensityExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract density for serial NOR flash")
        @CsvSource({
                "GD25Q16CSIG, 16",
                "GD25Q32CSIG, 32",
                "GD25Q64CSIG, 64",
                "GD25Q128CSIG, 128",
                "GD25Q256CSIG, 256",
                "GD25B64CSIG, 64",
                "GD25LQ32CSIG, 32"
        })
        void shouldExtractSerialNORDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract density for NAND flash")
        @CsvSource({
                "GD5F1GM7UEYIG, 1G",
                "GD5F2GM7UEYIG, 2G",
                "GD5F4GM5UEYIG, 4G"
        })
        void shouldExtractNANDDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MCU (not applicable)")
        void shouldReturnEmptyForMCU() {
            assertEquals("", handler.extractDensity("GD32F103C8T6"));
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractDensity(null));
            assertEquals("", handler.extractDensity(""));
        }
    }

    @Nested
    @DisplayName("MCU Flash Size Extraction")
    class MCUFlashSizeTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU flash size in KB")
        @CsvSource({
                "GD32F103C8T6, 64",
                "GD32F103CBT6, 128",
                "GD32F103RCT6, 256",
                "GD32F103VET6, 512",
                "GD32VF103C8T6, 64",
                "GD32VF103CBT6, 128"
        })
        void shouldExtractMCUFlashSize(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUFlashSizeKB(mpn),
                    "Flash size for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-MCU parts")
        void shouldReturnNegativeForNonMCU() {
            assertEquals(-1, handler.extractMCUFlashSizeKB("GD25Q128CSIG"));
            assertEquals(-1, handler.extractMCUFlashSizeKB("W25Q64"));
        }
    }

    @Nested
    @DisplayName("MCU Pin Count Extraction")
    class MCUPinCountTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU pin count")
        @CsvSource({
                "GD32F103C8T6, 48",
                "GD32F103RCT6, 64",
                "GD32F103VET6, 100",
                "GD32F103ZET6, 144",
                "GD32VF103C8T6, 48"
        })
        void shouldExtractMCUPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUPinCount(mpn),
                    "Pin count for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-MCU parts")
        void shouldReturnNegativeForNonMCU() {
            assertEquals(-1, handler.extractMCUPinCount("GD25Q128CSIG"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection - Flash")
    class FlashReplacementTests {

        @Test
        @DisplayName("Same series same density should be replacements")
        void sameSerieSameDensityShouldBeReplacements() {
            // Same GD25Q with same density (128Mb), different packages
            assertTrue(handler.isOfficialReplacement("GD25Q128CSIG", "GD25Q128EWIG"),
                    "Same series same density different package should be replacement");

            // Same density with tape and reel variant
            assertTrue(handler.isOfficialReplacement("GD25Q64EWIG", "GD25Q64EWIGR"),
                    "Same density with tape and reel should be replacement");
        }

        @Test
        @DisplayName("Same series different density should NOT be replacements")
        void sameSerieDifferentDensityNotReplacements() {
            assertFalse(handler.isOfficialReplacement("GD25Q64CSIG", "GD25Q128CSIG"),
                    "64Mb and 128Mb should NOT be replacements");

            assertFalse(handler.isOfficialReplacement("GD5F1GM7UEYIG", "GD5F2GM7UEYIG"),
                    "1Gb and 2Gb should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            // GD25Q vs GD25B
            assertFalse(handler.isOfficialReplacement("GD25Q64CSIG", "GD25B64CSIG"),
                    "GD25Q and GD25B should NOT be replacements");

            // GD25Q vs GD25LQ
            assertFalse(handler.isOfficialReplacement("GD25Q64CSIG", "GD25LQ64CSIG"),
                    "GD25Q and GD25LQ should NOT be replacements");

            // NOR vs NAND
            assertFalse(handler.isOfficialReplacement("GD25Q128CSIG", "GD5F1GM7UEYIG"),
                    "NOR and NAND should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Replacement Detection - MCU")
    class MCUReplacementTests {

        @Test
        @DisplayName("Same MCU line same pin count should be replacements")
        void sameMCULineSamePinsShouldBeReplacements() {
            // Same GD32F103 with same pin count (C=48), different flash
            assertTrue(handler.isOfficialReplacement("GD32F103C8T6", "GD32F103CBT6"),
                    "Same MCU line same pin count should be replacement");
        }

        @Test
        @DisplayName("Same MCU line different pin count should NOT be replacements")
        void sameMCULineDifferentPinsNotReplacements() {
            // GD32F103 C (48-pin) vs R (64-pin)
            assertFalse(handler.isOfficialReplacement("GD32F103C8T6", "GD32F103R8T6"),
                    "Different pin count should NOT be replacements");
        }

        @Test
        @DisplayName("Different MCU families should NOT be replacements")
        void differentMCUFamiliesNotReplacements() {
            // GD32F1 vs GD32F3
            assertFalse(handler.isOfficialReplacement("GD32F103C8T6", "GD32F303C8T6"),
                    "GD32F1 and GD32F3 should NOT be replacements");

            // ARM vs RISC-V
            assertFalse(handler.isOfficialReplacement("GD32F103C8T6", "GD32VF103C8T6"),
                    "ARM and RISC-V should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Cross-Product Replacement Detection")
    class CrossProductReplacementTests {

        @Test
        @DisplayName("Flash and MCU should NOT be replacements")
        void flashAndMCUNotReplacements() {
            assertFalse(handler.isOfficialReplacement("GD25Q128CSIG", "GD32F103C8T6"),
                    "Flash and MCU should NOT be replacements");
        }

        @Test
        @DisplayName("Cross-manufacturer parts should NOT be replacements")
        void crossManufacturerNotReplacements() {
            // GigaDevice vs Winbond
            assertFalse(handler.isOfficialReplacement("GD25Q64CSIG", "W25Q64"),
                    "GigaDevice and Winbond should NOT be replacements");

            // GigaDevice MCU vs ST MCU
            assertFalse(handler.isOfficialReplacement("GD32F103C8T6", "STM32F103C8T6"),
                    "GigaDevice and ST MCUs should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "GD25Q128CSIG"));
            assertFalse(handler.isOfficialReplacement("GD25Q128CSIG", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support MEMORY, MEMORY_FLASH, and MICROCONTROLLER types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY),
                    "Should support MEMORY type");
            assertTrue(types.contains(ComponentType.MEMORY_FLASH),
                    "Should support MEMORY_FLASH type");
            assertTrue(types.contains(ComponentType.MICROCONTROLLER),
                    "Should support MICROCONTROLLER type");
        }

        @Test
        @DisplayName("getSupportedTypes() should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.IC);
            }, "Set should be immutable");
        }

        @Test
        @DisplayName("Should have exactly 3 supported types")
        void shouldHaveExactlyThreeTypes() {
            assertEquals(3, handler.getSupportedTypes().size(),
                    "Should support exactly 3 types");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractDensity(null));
            assertEquals(-1, handler.extractMCUFlashSizeKB(null));
            assertEquals(-1, handler.extractMCUPinCount(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractDensity(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("GD25Q128CSIG", null, registry));
        }

        @Test
        @DisplayName("Should NOT match non-GigaDevice patterns")
        void shouldNotMatchNonGigaDevicePatterns() {
            // Other flash manufacturers
            assertFalse(handler.matches("W25Q128", ComponentType.MEMORY, registry));      // Winbond
            assertFalse(handler.matches("MX25L128", ComponentType.MEMORY, registry));     // Macronix
            assertFalse(handler.matches("S25FL256", ComponentType.MEMORY, registry));     // Spansion
            assertFalse(handler.matches("IS25LP128", ComponentType.MEMORY, registry));    // ISSI

            // Other MCU manufacturers
            assertFalse(handler.matches("STM32F103", ComponentType.MICROCONTROLLER, registry)); // ST
            assertFalse(handler.matches("ATMEGA328", ComponentType.MICROCONTROLLER, registry)); // Atmel
            assertFalse(handler.matches("PIC16F877", ComponentType.MICROCONTROLLER, registry)); // Microchip
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("gd25q128csig", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("gd32f103c8t6", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Gd25Q128Csig", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("gD32F103c8T6", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("Should handle very short MPNs")
        void shouldHandleShortMpns() {
            assertFalse(handler.matches("GD", ComponentType.MEMORY, registry));
            assertFalse(handler.matches("GD25", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode("GD"));
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {

        @Test
        @DisplayName("Document GigaDevice Flash product families")
        void documentFlashProductFamilies() {
            System.out.println("GigaDevice Flash Product Families:");
            System.out.println("GD25Qxx = Standard Serial NOR Flash (2.7-3.6V, SPI/Dual/Quad)");
            System.out.println("GD25Bxx = Wide Voltage Serial Flash (1.65-3.6V)");
            System.out.println("GD25LQxx = Low Power Serial Flash (1.65-2.0V)");
            System.out.println("GD25WQxx = Wide Voltage Low Power Flash (1.65-3.6V, low power)");
            System.out.println("GD25Txx = High Performance Flash (high speed)");
            System.out.println("GD5Fxxxx = SLC NAND Flash (1-4Gbit)");
        }

        @Test
        @DisplayName("Document GigaDevice MCU product families")
        void documentMCUProductFamilies() {
            System.out.println("GigaDevice MCU Product Families:");
            System.out.println("GD32F1xx = ARM Cortex-M3 MCU (STM32F1 compatible)");
            System.out.println("GD32F3xx = ARM Cortex-M4 MCU (STM32F3 compatible)");
            System.out.println("GD32F4xx = High Performance Cortex-M4 MCU");
            System.out.println("GD32E1xx = Enhanced Cortex-M3 MCU");
            System.out.println("GD32E2xx = Enhanced Entry-level Cortex-M23 MCU");
            System.out.println("GD32E5xx = Enhanced High-performance Cortex-M33 MCU");
            System.out.println("GD32VF1xx = RISC-V 32-bit MCU");
            System.out.println("GD32W5xx = Wi-Fi + Cortex-M33 MCU");
            System.out.println("GD32L2xx = Low Power Cortex-M23 MCU");
        }

        @Test
        @DisplayName("Document Flash package codes")
        void documentFlashPackageCodes() {
            System.out.println("GigaDevice Flash Package Codes:");
            System.out.println("SIG/CSIG/ESIG = SOP-8");
            System.out.println("WIG/EWIG/EWIGR = WSON-8 (8-pad)");
            System.out.println("ZIG/EZIQ = USON-8");
            System.out.println("FIG = WLCSP");
            System.out.println("LIG = SOIC-16");
            System.out.println("NIG = DFN-8");
            System.out.println("TIG/TIGR = TFBGA");
            System.out.println("BIG = BGA");
        }

        @Test
        @DisplayName("Document MCU package and pin encoding")
        void documentMCUEncoding() {
            System.out.println("GigaDevice MCU MPN Encoding:");
            System.out.println("Format: GD32F103C8T6");
            System.out.println("         GD32 = GigaDevice 32-bit");
            System.out.println("            F1 = Cortex-M3 family");
            System.out.println("              03 = Product line");
            System.out.println("                C = 48-pin (K=32, R=64, V=100, Z=144)");
            System.out.println("                 8 = 64KB Flash (4=16K, 6=32K, B=128K, C=256K, E=512K)");
            System.out.println("                  T = LQFP package");
            System.out.println("                   6 = Temperature grade (-40 to +85C)");
        }
    }

    @Nested
    @DisplayName("Compatibility with STM32 Reference")
    class STM32CompatibilityTests {

        @Test
        @DisplayName("GD32F1xx follows STM32F1 naming convention")
        void gd32F1FollowsSTM32F1Naming() {
            // GD32F103C8T6 is pin-compatible with STM32F103C8T6
            // Verify the naming convention matches
            String gd32 = "GD32F103C8T6";
            String stm32 = "STM32F103C8T6";

            // Both should have same suffix encoding
            assertEquals(gd32.substring(4), stm32.substring(5),
                    "GD32F and STM32F should have matching suffix encoding");
        }

        @Test
        @DisplayName("Document STM32-compatible GigaDevice parts")
        void documentSTM32CompatibleParts() {
            System.out.println("STM32-Compatible GigaDevice MCUs:");
            System.out.println("GD32F103 <-> STM32F103 (Cortex-M3, general purpose)");
            System.out.println("GD32F303 <-> STM32F303 (Cortex-M4, analog-rich)");
            System.out.println("GD32F407 <-> STM32F407 (Cortex-M4, high performance)");
            System.out.println("");
            System.out.println("Note: Pin-compatible but check errata and peripherals");
        }
    }
}
