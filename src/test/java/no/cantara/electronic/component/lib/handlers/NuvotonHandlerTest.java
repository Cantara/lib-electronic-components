package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.NuvotonHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for NuvotonHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction,
 * and replacement detection for Nuvoton Technology products.
 * <p>
 * Nuvoton product families covered:
 * <ul>
 *   <li>NUC1xx - ARM Cortex-M0 MCU</li>
 *   <li>NUC2xx - ARM Cortex-M0 MCU</li>
 *   <li>M031 - ARM Cortex-M0 MCU (entry-level)</li>
 *   <li>M451 - ARM Cortex-M4 MCU (motor control)</li>
 *   <li>M480 - ARM Cortex-M4 MCU (high performance)</li>
 *   <li>N76E003 - 8051-based MCU</li>
 *   <li>MS51 - Enhanced 8051 MCU</li>
 *   <li>NAU8810 - Audio Codec (mono)</li>
 *   <li>NAU8822 - Audio Codec (stereo)</li>
 *   <li>NAU88L25 - Audio Codec (low power)</li>
 *   <li>NPCT6xx - TPM chips</li>
 * </ul>
 */
class NuvotonHandlerTest {

    private static NuvotonHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new NuvotonHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("NUC1xx ARM Cortex-M0 MCU Detection")
    class NUC1xxTests {

        @ParameterizedTest
        @DisplayName("Should detect NUC1xx series MCUs")
        @ValueSource(strings = {
                "NUC123LD4AN0",
                "NUC123SD4AN0",
                "NUC120LE3AN",
                "NUC120VD2AN",
                "NUC140VE3AN",
                "NUC100LD3BN"
        })
        void shouldDetectNUC1xxMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("NUC1xx series should extract correct series")
        void shouldExtractNUC1xxSeries() {
            assertEquals("NUC1xx", handler.extractSeries("NUC123LD4AN0"));
            assertEquals("NUC1xx", handler.extractSeries("NUC120LE3AN"));
            assertEquals("NUC1xx", handler.extractSeries("NUC140VE3AN"));
        }

        @Test
        @DisplayName("NUC1xx should not match as IC")
        void shouldNotMatchAsIC() {
            assertFalse(handler.matches("NUC123LD4AN0", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("NUC2xx ARM Cortex-M0 MCU Detection")
    class NUC2xxTests {

        @ParameterizedTest
        @DisplayName("Should detect NUC2xx series MCUs")
        @ValueSource(strings = {
                "NUC240LD2AE",
                "NUC240VE3CN",
                "NUC200LE3AN",
                "NUC230SD2AE"
        })
        void shouldDetectNUC2xxMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("NUC2xx series should extract correct series")
        void shouldExtractNUC2xxSeries() {
            assertEquals("NUC2xx", handler.extractSeries("NUC240LD2AE"));
            assertEquals("NUC2xx", handler.extractSeries("NUC200LE3AN"));
        }
    }

    @Nested
    @DisplayName("M031 ARM Cortex-M0 MCU Detection")
    class M031Tests {

        @ParameterizedTest
        @DisplayName("Should detect M031 series MCUs")
        @ValueSource(strings = {
                "M031LD2AE",
                "M031SD2AE",
                "M031LC2AE",
                "M032LD2AE"
        })
        void shouldDetectM031MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("M031 series should extract correct series")
        void shouldExtractM031Series() {
            assertEquals("M031", handler.extractSeries("M031LD2AE"));
            assertEquals("M032", handler.extractSeries("M032LD2AE"));
        }
    }

    @Nested
    @DisplayName("M451 ARM Cortex-M4 MCU Detection")
    class M451Tests {

        @ParameterizedTest
        @DisplayName("Should detect M451 series MCUs")
        @ValueSource(strings = {
                "M451LG6AE",
                "M451LD3AE",
                "M452LG6AE",
                "M453VG6AE"
        })
        void shouldDetectM451MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("M451 series should extract correct series")
        void shouldExtractM451Series() {
            assertEquals("M451", handler.extractSeries("M451LG6AE"));
            assertEquals("M452", handler.extractSeries("M452LG6AE"));
        }
    }

    @Nested
    @DisplayName("M480 ARM Cortex-M4 MCU Detection")
    class M480Tests {

        @ParameterizedTest
        @DisplayName("Should detect M480 series MCUs")
        @ValueSource(strings = {
                "M480KIDAE",
                "M480LGAAE",
                "M482ZGCAE",
                "M483ZIDAE"
        })
        void shouldDetectM480MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("M480 series should extract correct series")
        void shouldExtractM480Series() {
            assertEquals("M480", handler.extractSeries("M480KIDAE"));
            assertEquals("M482", handler.extractSeries("M482ZGCAE"));
        }
    }

    @Nested
    @DisplayName("N76E003 8051 MCU Detection")
    class N76E003Tests {

        @ParameterizedTest
        @DisplayName("Should detect N76E003 series MCUs")
        @ValueSource(strings = {
                "N76E003AT20",
                "N76E003AQ20",
                "N76E003AS20",
                "N76E616AT28"
        })
        void shouldDetectN76EMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("N76E003 series should extract correct series")
        void shouldExtractN76E003Series() {
            assertEquals("N76E003", handler.extractSeries("N76E003AT20"));
            assertEquals("N76E", handler.extractSeries("N76E616AT28"));
        }

        @Test
        @DisplayName("N76E003 should not match as IC")
        void shouldNotMatchAsIC() {
            assertFalse(handler.matches("N76E003AT20", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("MS51 Enhanced 8051 MCU Detection")
    class MS51Tests {

        @ParameterizedTest
        @DisplayName("Should detect MS51 series MCUs")
        @ValueSource(strings = {
                "MS51FB9AE",
                "MS51DA9AE",
                "MS51BA9AE",
                "MS51EC0AE"
        })
        void shouldDetectMS51MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("MS51 series should extract correct series")
        void shouldExtractMS51Series() {
            assertEquals("MS51", handler.extractSeries("MS51FB9AE"));
            assertEquals("MS51", handler.extractSeries("MS51BA9AE"));
        }
    }

    @Nested
    @DisplayName("NAU8810 Audio Codec Detection")
    class NAU8810Tests {

        @ParameterizedTest
        @DisplayName("Should detect NAU8810 audio codecs")
        @ValueSource(strings = {
                "NAU8810YG",
                "NAU8810LG",
                "NAU8810G"
        })
        void shouldDetectNAU8810Codec(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("NAU8810 should extract correct series")
        void shouldExtractNAU8810Series() {
            assertEquals("NAU8810", handler.extractSeries("NAU8810YG"));
        }

        @Test
        @DisplayName("NAU8810 should not match as MICROCONTROLLER")
        void shouldNotMatchAsMCU() {
            assertFalse(handler.matches("NAU8810YG", ComponentType.MICROCONTROLLER, registry));
        }
    }

    @Nested
    @DisplayName("NAU8822 Audio Codec Detection")
    class NAU8822Tests {

        @ParameterizedTest
        @DisplayName("Should detect NAU8822 audio codecs")
        @ValueSource(strings = {
                "NAU8822YG",
                "NAU8822LG",
                "NAU8822G"
        })
        void shouldDetectNAU8822Codec(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("NAU8822 should extract correct series")
        void shouldExtractNAU8822Series() {
            assertEquals("NAU8822", handler.extractSeries("NAU8822YG"));
        }
    }

    @Nested
    @DisplayName("NAU88L25 Low Power Audio Codec Detection")
    class NAU88L25Tests {

        @ParameterizedTest
        @DisplayName("Should detect NAU88L25 audio codecs")
        @ValueSource(strings = {
                "NAU88L25YGB",
                "NAU88L25YG"
        })
        void shouldDetectNAU88L25Codec(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("NAU88L25 should extract correct series")
        void shouldExtractNAU88L25Series() {
            assertEquals("NAU88L", handler.extractSeries("NAU88L25YGB"));
        }
    }

    @Nested
    @DisplayName("NPCT6xx TPM Chip Detection")
    class NPCT6xxTests {

        @ParameterizedTest
        @DisplayName("Should detect NPCT6xx TPM chips")
        @ValueSource(strings = {
                "NPCT650JAAYX",
                "NPCT652JAAYX",
                "NPCT660JAAYX",
                "NPCT750JAAYX"
        })
        void shouldDetectNPCTChips(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("NPCT6xx should extract correct series")
        void shouldExtractNPCT6xxSeries() {
            assertEquals("NPCT6xx", handler.extractSeries("NPCT650JAAYX"));
            assertEquals("NPCT6xx", handler.extractSeries("NPCT660JAAYX"));
        }

        @Test
        @DisplayName("NPCT should not match as MICROCONTROLLER")
        void shouldNotMatchAsMCU() {
            assertFalse(handler.matches("NPCT650JAAYX", ComponentType.MICROCONTROLLER, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction - MCUs")
    class MCUPackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for NUC series MCUs")
        @CsvSource({
                "NUC123LD4AN0, LQFP-48",
                "NUC123SD4AN0, LQFP-64",
                "NUC123AN4AN0, QFN-33"
        })
        void shouldExtractNUCPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes for M-series MCUs")
        @CsvSource({
                "M451LG6AE, LQFP-100",
                "M451LD3AE, LQFP-48"
        })
        void shouldExtractMSeriesPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes for N76E series MCUs")
        @CsvSource({
                "N76E003AT20, TSSOP-20"
        })
        void shouldExtractN76EPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes for MS51 series MCUs")
        @CsvSource({
                "MS51FB9AE, LQFP-48"
        })
        void shouldExtractMS51PackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction - Audio Codecs")
    class CodecPackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for NAU audio codecs")
        @CsvSource({
                "NAU8810YG, QFN-32",
                "NAU8822YG, QFN-32",
                "NAU88L25YGB, QFN-32"
        })
        void shouldExtractNAUPackageCode(String mpn, String expected) {
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
                "NUC123LD4AN0, NUC1xx",
                "NUC240LD2AE, NUC2xx",
                "M031LD2AE, M031",
                "M451LG6AE, M451",
                "M480KIDAE, M480",
                "N76E003AT20, N76E003",
                "MS51FB9AE, MS51",
                "NAU8810YG, NAU8810",
                "NAU8822YG, NAU8822",
                "NAU88L25YGB, NAU88L",
                "NPCT650JAAYX, NPCT6xx"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-Nuvoton parts")
        void shouldReturnEmptyForNonNuvotonParts() {
            assertEquals("", handler.extractSeries("STM32F103"));     // ST
            assertEquals("", handler.extractSeries("ATMEGA328"));     // Atmel
            assertEquals("", handler.extractSeries("WM8731"));        // Wolfson codec
            assertEquals("", handler.extractSeries("GD32F103"));      // GigaDevice
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
            assertEquals("NUC1xx", handler.extractSeries("nuc123ld4an0"));
            assertEquals("NAU8810", handler.extractSeries("nau8810yg"));
        }
    }

    @Nested
    @DisplayName("MCU Flash Size Extraction")
    class MCUFlashSizeTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU flash size for NUC series")
        @CsvSource({
                "NUC123LD4AN0, 64",
                "NUC123LD3AN0, 32",
                "NUC123LD5AN0, 128"
        })
        void shouldExtractNUCFlashSize(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUFlashSizeKB(mpn),
                    "Flash size for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MCU flash size for M-series")
        @CsvSource({
                "M451LG6AE, 512",
                "M451LG5AE, 256",
                "M451LG4AE, 128"
        })
        void shouldExtractMSeriesFlashSize(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUFlashSizeKB(mpn),
                    "Flash size for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-MCU parts")
        void shouldReturnNegativeForNonMCU() {
            assertEquals(-1, handler.extractMCUFlashSizeKB("NAU8810YG"));
            assertEquals(-1, handler.extractMCUFlashSizeKB("NPCT650JAAYX"));
        }
    }

    @Nested
    @DisplayName("MCU Pin Count Extraction")
    class MCUPinCountTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU pin count for NUC series")
        @CsvSource({
                "NUC123LD4AN0, 48",
                "NUC123SD4AN0, 64",
                "NUC123VD4AN0, 100"
        })
        void shouldExtractNUCPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUPinCount(mpn),
                    "Pin count for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MCU pin count for M-series")
        @CsvSource({
                "M451LD3AE, 48",
                "M451LG6AE, 100",
                "M480KIDAE, 128"
        })
        void shouldExtractMSeriesPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUPinCount(mpn),
                    "Pin count for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MCU pin count for N76E series")
        @CsvSource({
                "N76E003AT20, 20"
        })
        void shouldExtractN76EPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUPinCount(mpn),
                    "Pin count for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-MCU parts")
        void shouldReturnNegativeForNonMCU() {
            assertEquals(-1, handler.extractMCUPinCount("NAU8810YG"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection - MCUs")
    class MCUReplacementTests {

        @Test
        @DisplayName("Same MCU series same pin count should be replacements")
        void sameMCUSeriesSamePinsShouldBeReplacements() {
            // Same NUC123 with same pin count (LD=48), different flash
            assertTrue(handler.isOfficialReplacement("NUC123LD3AN0", "NUC123LD4AN0"),
                    "Same MCU series same pin count should be replacement");
        }

        @Test
        @DisplayName("Same MCU series different pin count should NOT be replacements")
        void sameMCUSeriesDifferentPinsNotReplacements() {
            // NUC123 LD (48-pin) vs SD (64-pin)
            assertFalse(handler.isOfficialReplacement("NUC123LD4AN0", "NUC123SD4AN0"),
                    "Different pin count should NOT be replacements");
        }

        @Test
        @DisplayName("Different MCU series should NOT be replacements")
        void differentMCUSeriesNotReplacements() {
            // NUC1xx vs NUC2xx
            assertFalse(handler.isOfficialReplacement("NUC123LD4AN0", "NUC240LD2AE"),
                    "NUC1xx and NUC2xx should NOT be replacements");

            // M031 vs M451
            assertFalse(handler.isOfficialReplacement("M031LD2AE", "M451LD3AE"),
                    "M031 and M451 should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Replacement Detection - Audio Codecs")
    class CodecReplacementTests {

        @Test
        @DisplayName("Same codec series different package should be replacements")
        void sameCodecSeriesDifferentPackageShouldBeReplacements() {
            // Same NAU8810, different packages
            assertTrue(handler.isOfficialReplacement("NAU8810YG", "NAU8810LG"),
                    "Same codec series different package should be replacement");
        }

        @Test
        @DisplayName("Different codec series should NOT be replacements")
        void differentCodecSeriesNotReplacements() {
            // NAU8810 vs NAU8822
            assertFalse(handler.isOfficialReplacement("NAU8810YG", "NAU8822YG"),
                    "NAU8810 and NAU8822 should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Cross-Product Replacement Detection")
    class CrossProductReplacementTests {

        @Test
        @DisplayName("MCU and Audio Codec should NOT be replacements")
        void mcuAndCodecNotReplacements() {
            assertFalse(handler.isOfficialReplacement("NUC123LD4AN0", "NAU8810YG"),
                    "MCU and Audio Codec should NOT be replacements");
        }

        @Test
        @DisplayName("Cross-manufacturer parts should NOT be replacements")
        void crossManufacturerNotReplacements() {
            // Nuvoton vs ST
            assertFalse(handler.isOfficialReplacement("NUC123LD4AN0", "STM32F103"),
                    "Nuvoton and ST MCUs should NOT be replacements");

            // Nuvoton vs Wolfson (audio codec)
            assertFalse(handler.isOfficialReplacement("NAU8810YG", "WM8731"),
                    "Nuvoton and Wolfson codecs should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "NUC123LD4AN0"));
            assertFalse(handler.isOfficialReplacement("NUC123LD4AN0", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support MICROCONTROLLER and IC types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MICROCONTROLLER),
                    "Should support MICROCONTROLLER type");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("getSupportedTypes() should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.MEMORY);
            }, "Set should be immutable");
        }

        @Test
        @DisplayName("Should have exactly 2 supported types")
        void shouldHaveExactlyTwoTypes() {
            assertEquals(2, handler.getSupportedTypes().size(),
                    "Should support exactly 2 types");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.MICROCONTROLLER, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals(-1, handler.extractMCUFlashSizeKB(null));
            assertEquals(-1, handler.extractMCUPinCount(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MICROCONTROLLER, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("NUC123LD4AN0", null, registry));
        }

        @Test
        @DisplayName("Should NOT match non-Nuvoton patterns")
        void shouldNotMatchNonNuvotonPatterns() {
            // Other MCU manufacturers
            assertFalse(handler.matches("STM32F103", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("ATMEGA328", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("GD32F103", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("PIC16F877", ComponentType.MICROCONTROLLER, registry));

            // Other audio codec manufacturers
            assertFalse(handler.matches("WM8731", ComponentType.IC, registry));
            assertFalse(handler.matches("CS4334", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("nuc123ld4an0", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("nau8810yg", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Nuc123Ld4An0", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("Nau8810Yg", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle very short MPNs")
        void shouldHandleShortMpns() {
            assertFalse(handler.matches("NUC", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("NAU", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode("NUC"));
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {

        @Test
        @DisplayName("Document Nuvoton MCU product families")
        void documentMCUProductFamilies() {
            System.out.println("Nuvoton MCU Product Families:");
            System.out.println("NUC1xx = ARM Cortex-M0, NuMicro M051/M0516 compatible");
            System.out.println("NUC2xx = ARM Cortex-M0, enhanced features");
            System.out.println("M031 = ARM Cortex-M0, entry-level, low power");
            System.out.println("M451 = ARM Cortex-M4, motor control focused");
            System.out.println("M480 = ARM Cortex-M4, high performance, crypto");
            System.out.println("N76E003 = 8051-based, ultra-low cost, small package");
            System.out.println("MS51 = Enhanced 8051, better peripherals than N76E");
        }

        @Test
        @DisplayName("Document Nuvoton Audio Codec product families")
        void documentAudioCodecProductFamilies() {
            System.out.println("Nuvoton Audio Codec Product Families:");
            System.out.println("NAU8810 = Mono audio codec, single DAC/ADC");
            System.out.println("NAU8822 = Stereo audio codec, dual DAC/ADC");
            System.out.println("NAU88L25 = Low power stereo codec, mobile focused");
        }

        @Test
        @DisplayName("Document Nuvoton TPM product families")
        void documentTPMProductFamilies() {
            System.out.println("Nuvoton TPM Product Families:");
            System.out.println("NPCT6xx = TPM 2.0 chips (NPCT650, NPCT660, etc.)");
            System.out.println("NPCT7xx = Next-gen TPM with additional security");
        }

        @Test
        @DisplayName("Document MCU MPN encoding")
        void documentMCUEncoding() {
            System.out.println("Nuvoton NUC MCU MPN Encoding:");
            System.out.println("Format: NUC123LD4AN0");
            System.out.println("        NUC = NuMicro family");
            System.out.println("           1 = Series (1xx = Cortex-M0)");
            System.out.println("            23 = Line number");
            System.out.println("              LD = Pin count (LD=48, SD=64, VD=100)");
            System.out.println("                4 = Flash size code");
            System.out.println("                 A = Package type");
            System.out.println("                  N = Temperature grade");
            System.out.println("                   0 = Version");
            System.out.println("");
            System.out.println("Format: M451LG6AE");
            System.out.println("        M = M-series");
            System.out.println("         451 = Product line");
            System.out.println("            LG = Pin count (LD=48, LG=100, KI=128)");
            System.out.println("              6 = Flash size code");
            System.out.println("               AE = Package suffix");
        }

        @Test
        @DisplayName("Document Audio Codec package codes")
        void documentAudioCodecPackageCodes() {
            System.out.println("Nuvoton Audio Codec Package Codes:");
            System.out.println("YG = QFN-32");
            System.out.println("YGB = QFN-32 (with exposed pad)");
            System.out.println("LG = WLCSP");
            System.out.println("G = QFN-48");
        }
    }

    @Nested
    @DisplayName("Compatibility Notes")
    class CompatibilityNotes {

        @Test
        @DisplayName("Document NuMicro compatibility")
        void documentNuMicroCompatibility() {
            System.out.println("NuMicro Compatibility Notes:");
            System.out.println("NUC120/NUC140/NUC123 share similar pinouts");
            System.out.println("M031 is cost-optimized, fewer features than NUC series");
            System.out.println("M451/M452/M453 are motor control variants of M4 series");
            System.out.println("M480 series has highest performance in Nuvoton lineup");
        }

        @Test
        @DisplayName("Document 8051 compatibility")
        void document8051Compatibility() {
            System.out.println("Nuvoton 8051 Compatibility Notes:");
            System.out.println("N76E003 is 1T 8051 (single clock cycle per instruction)");
            System.out.println("Pin-compatible with AT89S52 in TSSOP-20");
            System.out.println("MS51 is enhanced version with more RAM and Flash");
        }
    }
}
