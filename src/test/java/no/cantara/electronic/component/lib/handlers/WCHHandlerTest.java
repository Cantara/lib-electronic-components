package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.WCHHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for WCHHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction,
 * and replacement detection for WCH (Nanjing Qinheng) products.
 * <p>
 * WCH product families covered:
 * <ul>
 *   <li>CH32V003 - Entry-level RISC-V MCU (QingKe V2A, 48MHz)</li>
 *   <li>CH32V103 - RISC-V MCU (QingKe V3A, 80MHz)</li>
 *   <li>CH32V203 - RISC-V MCU (QingKe V4B, 144MHz)</li>
 *   <li>CH32V208 - RISC-V MCU with BLE</li>
 *   <li>CH32V303 - High-performance RISC-V MCU</li>
 *   <li>CH32V305/307 - RISC-V MCU with USB HS and Ethernet</li>
 *   <li>CH32F103 - ARM Cortex-M3 MCU</li>
 *   <li>CH32F203 - ARM Cortex-M3 MCU with enhanced peripherals</li>
 *   <li>CH340 - USB to serial UART (most popular)</li>
 *   <li>CH341 - USB to serial/parallel/I2C/SPI</li>
 *   <li>CH9340 - USB to serial (enhanced CH340)</li>
 *   <li>CH395 - Ethernet controller</li>
 *   <li>CH9121 - Ethernet to serial converter</li>
 *   <li>CH9141/CH9143 - Bluetooth modules</li>
 * </ul>
 */
class WCHHandlerTest {

    private static WCHHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new WCHHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("CH32V003 Entry-Level RISC-V MCU Detection")
    class CH32V003Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH32V003 series MCUs")
        @ValueSource(strings = {
                "CH32V003F4U6",
                "CH32V003F4P6",
                "CH32V003A4M6",
                "CH32V003J4M6"
        })
        void shouldDetectCH32V003(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("CH32V003 should extract correct series")
        void shouldExtractCH32V003Series() {
            assertEquals("CH32V003", handler.extractSeries("CH32V003F4U6"));
            assertEquals("CH32V003", handler.extractSeries("CH32V003J4M6"));
        }

        @Test
        @DisplayName("CH32V003 should NOT match as IC")
        void shouldNotMatchAsIC() {
            assertFalse(handler.matches("CH32V003F4U6", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("CH32V003 should be identified as RISC-V")
        void shouldBeIdentifiedAsRiscV() {
            assertTrue(handler.isRiscV("CH32V003F4U6"));
            assertFalse(handler.isArm("CH32V003F4U6"));
        }

        @Test
        @DisplayName("CH32V003 should have QingKe V2A core")
        void shouldHaveCorrectCoreType() {
            assertEquals("RISC-V QingKe V2A", handler.getCoreType("CH32V003F4U6"));
        }
    }

    @Nested
    @DisplayName("CH32V103 RISC-V MCU Detection")
    class CH32V103Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH32V103 series MCUs")
        @ValueSource(strings = {
                "CH32V103C8T6",
                "CH32V103C6T6",
                "CH32V103R8T6"
        })
        void shouldDetectCH32V103(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("CH32V103 should extract correct series")
        void shouldExtractCH32V103Series() {
            assertEquals("CH32V103", handler.extractSeries("CH32V103C8T6"));
            assertEquals("CH32V103", handler.extractSeries("CH32V103R8T6"));
        }

        @Test
        @DisplayName("CH32V103 should have QingKe V3A core")
        void shouldHaveCorrectCoreType() {
            assertEquals("RISC-V QingKe V3A", handler.getCoreType("CH32V103C8T6"));
        }
    }

    @Nested
    @DisplayName("CH32V203 RISC-V MCU Detection")
    class CH32V203Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH32V203 series MCUs")
        @ValueSource(strings = {
                "CH32V203C8T6",
                "CH32V203CBT6",
                "CH32V203RBT6",
                "CH32V203K8T6"
        })
        void shouldDetectCH32V203(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("CH32V203 should extract correct series")
        void shouldExtractCH32V203Series() {
            assertEquals("CH32V203", handler.extractSeries("CH32V203C8T6"));
            assertEquals("CH32V203", handler.extractSeries("CH32V203RBT6"));
        }

        @Test
        @DisplayName("CH32V203 should have QingKe V4B core")
        void shouldHaveCorrectCoreType() {
            assertEquals("RISC-V QingKe V4B", handler.getCoreType("CH32V203C8T6"));
        }
    }

    @Nested
    @DisplayName("CH32V307 High-Performance RISC-V MCU Detection")
    class CH32V307Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH32V307 series MCUs")
        @ValueSource(strings = {
                "CH32V307VCT6",
                "CH32V307RCT6",
                "CH32V307WCU6"
        })
        void shouldDetectCH32V307(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("CH32V307 should extract correct series")
        void shouldExtractCH32V307Series() {
            assertEquals("CH32V307", handler.extractSeries("CH32V307VCT6"));
            assertEquals("CH32V307", handler.extractSeries("CH32V307RCT6"));
        }

        @Test
        @DisplayName("CH32V307 should have QingKe V4F core")
        void shouldHaveCorrectCoreType() {
            assertEquals("RISC-V QingKe V4F", handler.getCoreType("CH32V307VCT6"));
        }
    }

    @Nested
    @DisplayName("CH32F103 ARM Cortex-M3 MCU Detection")
    class CH32F103Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH32F103 series MCUs")
        @ValueSource(strings = {
                "CH32F103C8T6",
                "CH32F103CBT6",
                "CH32F103R8T6",
                "CH32F103RBT6"
        })
        void shouldDetectCH32F103(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("CH32F103 should extract correct series")
        void shouldExtractCH32F103Series() {
            assertEquals("CH32F103", handler.extractSeries("CH32F103C8T6"));
            assertEquals("CH32F103", handler.extractSeries("CH32F103RBT6"));
        }

        @Test
        @DisplayName("CH32F103 should be identified as ARM")
        void shouldBeIdentifiedAsArm() {
            assertTrue(handler.isArm("CH32F103C8T6"));
            assertFalse(handler.isRiscV("CH32F103C8T6"));
        }

        @Test
        @DisplayName("CH32F103 should have ARM Cortex-M3 core")
        void shouldHaveCorrectCoreType() {
            assertEquals("ARM Cortex-M3", handler.getCoreType("CH32F103C8T6"));
        }

        @Test
        @DisplayName("CH32F103 should NOT match as IC")
        void shouldNotMatchAsIC() {
            assertFalse(handler.matches("CH32F103C8T6", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("CH340 USB to Serial Detection")
    class CH340Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH340 series USB chips")
        @ValueSource(strings = {
                "CH340G",
                "CH340C",
                "CH340K",
                "CH340N",
                "CH340E",
                "CH340X",
                "CH340B"
        })
        void shouldDetectCH340(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("CH340 should extract correct series")
        void shouldExtractCH340Series() {
            assertEquals("CH340", handler.extractSeries("CH340G"));
            assertEquals("CH340", handler.extractSeries("CH340C"));
            assertEquals("CH340", handler.extractSeries("CH340K"));
        }

        @Test
        @DisplayName("CH340 should have USB to UART interface")
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB to UART", handler.getInterfaceType("CH340G"));
            assertEquals("USB to UART", handler.getInterfaceType("CH340C"));
        }

        @Test
        @DisplayName("CH340 should NOT match as MICROCONTROLLER")
        void shouldNotMatchAsMCU() {
            assertFalse(handler.matches("CH340G", ComponentType.MICROCONTROLLER, registry));
        }
    }

    @Nested
    @DisplayName("CH341 USB to Serial/Parallel/I2C Detection")
    class CH341Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH341 series USB chips")
        @ValueSource(strings = {
                "CH341A",
                "CH341B",
                "CH341T",
                "CH341H"
        })
        void shouldDetectCH341(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("CH341 should extract correct series")
        void shouldExtractCH341Series() {
            assertEquals("CH341", handler.extractSeries("CH341A"));
            assertEquals("CH341", handler.extractSeries("CH341T"));
        }

        @Test
        @DisplayName("CH341 should have USB to UART/I2C/SPI/Parallel interface")
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB to UART/I2C/SPI/Parallel", handler.getInterfaceType("CH341A"));
        }
    }

    @Nested
    @DisplayName("CH9340 Enhanced USB to Serial Detection")
    class CH9340Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH9340 series USB chips")
        @ValueSource(strings = {
                "CH9340C",
                "CH9340K"
        })
        void shouldDetectCH9340(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("CH9340 should extract correct series")
        void shouldExtractCH9340Series() {
            assertEquals("CH9340", handler.extractSeries("CH9340C"));
            assertEquals("CH9340", handler.extractSeries("CH9340K"));
        }

        @Test
        @DisplayName("CH9340 should have USB to UART interface")
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB to UART", handler.getInterfaceType("CH9340C"));
        }
    }

    @Nested
    @DisplayName("CH395 Ethernet Controller Detection")
    class CH395Tests {

        @ParameterizedTest
        @DisplayName("Should detect CH395 ethernet chips")
        @ValueSource(strings = {
                "CH395L",
                "CH395Q"
        })
        void shouldDetectCH395(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("CH395 should extract correct series")
        void shouldExtractCH395Series() {
            assertEquals("CH395", handler.extractSeries("CH395L"));
        }

        @Test
        @DisplayName("CH395 should have Ethernet Controller interface")
        void shouldHaveCorrectInterfaceType() {
            assertEquals("Ethernet Controller", handler.getInterfaceType("CH395L"));
        }
    }

    @Nested
    @DisplayName("CH9121 Ethernet to Serial Detection")
    class CH9121Tests {

        @Test
        @DisplayName("Should detect CH9121 ethernet chips")
        void shouldDetectCH9121() {
            assertTrue(handler.matches("CH9121", ComponentType.IC, registry),
                    "CH9121 should match IC");
        }

        @Test
        @DisplayName("CH9121 should extract correct series")
        void shouldExtractCH9121Series() {
            assertEquals("CH9121", handler.extractSeries("CH9121"));
        }

        @Test
        @DisplayName("CH9121 should have Ethernet to UART interface")
        void shouldHaveCorrectInterfaceType() {
            assertEquals("Ethernet to UART", handler.getInterfaceType("CH9121"));
        }
    }

    @Nested
    @DisplayName("Bluetooth Module Detection")
    class BluetoothTests {

        @ParameterizedTest
        @DisplayName("Should detect CH914x Bluetooth modules")
        @ValueSource(strings = {
                "CH9141",
                "CH9143"
        })
        void shouldDetectBluetoothModules(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("CH9141 should have BLE to UART interface")
        void shouldHaveCorrectInterfaceType() {
            assertEquals("BLE to UART", handler.getInterfaceType("CH9141"));
            assertEquals("BLE to UART", handler.getInterfaceType("CH9143"));
        }
    }

    @Nested
    @DisplayName("CH224K USB PD Controller Detection")
    class CH224Tests {

        @Test
        @DisplayName("Should detect CH224K USB PD chip")
        void shouldDetectCH224K() {
            assertTrue(handler.matches("CH224K", ComponentType.IC, registry),
                    "CH224K should match IC");
        }

        @Test
        @DisplayName("CH224 should extract correct series")
        void shouldExtractCH224Series() {
            assertEquals("CH224", handler.extractSeries("CH224K"));
        }

        @Test
        @DisplayName("CH224 should have USB PD Controller interface")
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB PD Controller", handler.getInterfaceType("CH224K"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction - MCU")
    class MCUPackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for MCUs")
        @CsvSource({
                "CH32V103C8T6, LQFP-48",
                "CH32V203RBT6, LQFP-64",
                "CH32V307VCT6, LQFP-100",
                "CH32F103C8T6, LQFP-48",
                "CH32V003F4U6, TSSOP-20"
        })
        void shouldExtractMCUPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction - USB Chips")
    class USBPackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for USB chips")
        @CsvSource({
                "CH340G, SOP-16",
                "CH340C, SOP-16",
                "CH340K, ESSOP-10",
                "CH340N, SOP-8",
                "CH340E, MSOP-10",
                "CH341A, SOP-28",
                "CH341T, SSOP-20"
        })
        void shouldExtractUSBPackageCode(String mpn, String expected) {
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
                "CH32V003F4U6, CH32V003",
                "CH32V103C8T6, CH32V103",
                "CH32V203RBT6, CH32V203",
                "CH32V307VCT6, CH32V307",
                "CH32F103C8T6, CH32F103",
                "CH340G, CH340",
                "CH341A, CH341",
                "CH9340C, CH9340",
                "CH395L, CH395",
                "CH9121, CH9121",
                "CH224K, CH224"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-WCH parts")
        void shouldReturnEmptyForNonWCHParts() {
            assertEquals("", handler.extractSeries("FT232RL"));       // FTDI
            assertEquals("", handler.extractSeries("STM32F103C8"));   // ST
            assertEquals("", handler.extractSeries("CP2102"));        // Silicon Labs
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
            assertEquals("CH340", handler.extractSeries("ch340g"));
            assertEquals("CH32V103", handler.extractSeries("ch32v103c8t6"));
        }
    }

    @Nested
    @DisplayName("MCU Flash Size Extraction")
    class MCUFlashSizeTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU flash size in KB")
        @CsvSource({
                "CH32V003F4U6, 16",
                "CH32V103C6T6, 32",
                "CH32V103C8T6, 64",
                "CH32V203CBT6, 128",
                "CH32V307VCT6, 256",
                "CH32F103C8T6, 64",
                "CH32F103RBT6, 128"
        })
        void shouldExtractMCUFlashSize(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUFlashSizeKB(mpn),
                    "Flash size for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-MCU parts")
        void shouldReturnNegativeForNonMCU() {
            assertEquals(-1, handler.extractMCUFlashSizeKB("CH340G"));
            assertEquals(-1, handler.extractMCUFlashSizeKB("CH341A"));
        }
    }

    @Nested
    @DisplayName("MCU Pin Count Extraction")
    class MCUPinCountTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU pin count")
        @CsvSource({
                "CH32V103C8T6, 48",
                "CH32V203RBT6, 64",
                "CH32V307VCT6, 100",
                "CH32V003F4U6, 20",
                "CH32F103C8T6, 48"
        })
        void shouldExtractMCUPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUPinCount(mpn),
                    "Pin count for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-MCU parts")
        void shouldReturnNegativeForNonMCU() {
            assertEquals(-1, handler.extractMCUPinCount("CH340G"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection - USB Chips")
    class USBReplacementTests {

        @Test
        @DisplayName("Same series USB chips should be replacements")
        void sameSeriesShouldBeReplacements() {
            // CH340G and CH340C are both USB to serial - different packages
            assertTrue(handler.isOfficialReplacement("CH340G", "CH340C"),
                    "Same series different package should be replacement");

            // CH340K and CH340E are both compact versions
            assertTrue(handler.isOfficialReplacement("CH340K", "CH340E"),
                    "Same series compact packages should be replacement");
        }

        @Test
        @DisplayName("Different USB series should NOT be replacements")
        void differentSeriesNotReplacements() {
            // CH340 vs CH341 (different functionality)
            assertFalse(handler.isOfficialReplacement("CH340G", "CH341A"),
                    "CH340 and CH341 should NOT be replacements");

            // CH340 vs CH9340 (different series even though similar function)
            assertFalse(handler.isOfficialReplacement("CH340G", "CH9340C"),
                    "CH340 and CH9340 should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Replacement Detection - MCU")
    class MCUReplacementTests {

        @Test
        @DisplayName("Same MCU series same pin count should be replacements")
        void sameMCUSeriesSamePinsShouldBeReplacements() {
            // Same CH32V103 with same pin count (C=48), different flash
            assertTrue(handler.isOfficialReplacement("CH32V103C8T6", "CH32V103C6T6"),
                    "Same MCU series same pin count should be replacement");
        }

        @Test
        @DisplayName("Same MCU series different pin count should NOT be replacements")
        void sameMCUSeriesDifferentPinsNotReplacements() {
            // CH32V103 C (48-pin) vs R (64-pin)
            assertFalse(handler.isOfficialReplacement("CH32V103C8T6", "CH32V103R8T6"),
                    "Different pin count should NOT be replacements");
        }

        @Test
        @DisplayName("Different MCU series should NOT be replacements")
        void differentMCUSeriesNotReplacements() {
            // CH32V103 vs CH32V203
            assertFalse(handler.isOfficialReplacement("CH32V103C8T6", "CH32V203C8T6"),
                    "CH32V103 and CH32V203 should NOT be replacements");

            // RISC-V vs ARM
            assertFalse(handler.isOfficialReplacement("CH32V103C8T6", "CH32F103C8T6"),
                    "RISC-V and ARM should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Cross-Product Replacement Detection")
    class CrossProductReplacementTests {

        @Test
        @DisplayName("USB chip and MCU should NOT be replacements")
        void usbAndMCUNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CH340G", "CH32V103C8T6"),
                    "USB chip and MCU should NOT be replacements");
        }

        @Test
        @DisplayName("Cross-manufacturer parts should NOT be replacements")
        void crossManufacturerNotReplacements() {
            // WCH vs FTDI
            assertFalse(handler.isOfficialReplacement("CH340G", "FT232RL"),
                    "WCH and FTDI should NOT be replacements");

            // WCH MCU vs ST MCU
            assertFalse(handler.isOfficialReplacement("CH32F103C8T6", "STM32F103C8T6"),
                    "WCH and ST MCUs should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "CH340G"));
            assertFalse(handler.isOfficialReplacement("CH340G", null));
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
            assertEquals("", handler.getCoreType(null));
            assertEquals("", handler.getInterfaceType(null));
            assertFalse(handler.isRiscV(null));
            assertFalse(handler.isArm(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MICROCONTROLLER, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getCoreType(""));
            assertEquals("", handler.getInterfaceType(""));
            assertFalse(handler.isRiscV(""));
            assertFalse(handler.isArm(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("CH340G", null, registry));
        }

        @Test
        @DisplayName("Should NOT match non-WCH patterns")
        void shouldNotMatchNonWCHPatterns() {
            // Other USB-to-serial manufacturers
            assertFalse(handler.matches("FT232RL", ComponentType.IC, registry));     // FTDI
            assertFalse(handler.matches("CP2102", ComponentType.IC, registry));      // Silicon Labs
            assertFalse(handler.matches("PL2303", ComponentType.IC, registry));      // Prolific

            // Other MCU manufacturers
            assertFalse(handler.matches("STM32F103", ComponentType.MICROCONTROLLER, registry)); // ST
            assertFalse(handler.matches("GD32F103", ComponentType.MICROCONTROLLER, registry)); // GigaDevice
            assertFalse(handler.matches("ATMEGA328", ComponentType.MICROCONTROLLER, registry)); // Atmel
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("ch340g", ComponentType.IC, registry));
            assertTrue(handler.matches("ch32v103c8t6", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Ch340G", ComponentType.IC, registry));
            assertTrue(handler.matches("cH32V103c8T6", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("Should handle very short MPNs")
        void shouldHandleShortMpns() {
            assertFalse(handler.matches("CH", ComponentType.IC, registry));
            assertFalse(handler.matches("CH3", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode("CH"));
        }
    }

    @Nested
    @DisplayName("Core Type Detection")
    class CoreTypeTests {

        @ParameterizedTest
        @DisplayName("Should detect correct core types for MCUs")
        @CsvSource({
                "CH32V003F4U6, RISC-V QingKe V2A",
                "CH32V103C8T6, RISC-V QingKe V3A",
                "CH32V203C8T6, RISC-V QingKe V4B",
                "CH32V208WBU6, RISC-V QingKe V4B",
                "CH32V303RBT6, RISC-V QingKe V4F",
                "CH32V307VCT6, RISC-V QingKe V4F",
                "CH32F103C8T6, ARM Cortex-M3",
                "CH32F203C8T6, ARM Cortex-M3"
        })
        void shouldDetectCorrectCoreType(String mpn, String expected) {
            assertEquals(expected, handler.getCoreType(mpn),
                    "Core type for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-MCU parts")
        void shouldReturnEmptyForNonMCU() {
            assertEquals("", handler.getCoreType("CH340G"));
            assertEquals("", handler.getCoreType("CH341A"));
        }
    }

    @Nested
    @DisplayName("Interface Type Detection")
    class InterfaceTypeTests {

        @ParameterizedTest
        @DisplayName("Should detect correct interface types for USB chips")
        @CsvSource({
                "CH340G, USB to UART",
                "CH9340C, USB to UART",
                "CH341A, USB to UART/I2C/SPI/Parallel",
                "CH395L, Ethernet Controller",
                "CH9121, Ethernet to UART",
                "CH9141, BLE to UART",
                "CH334F, USB Hub Controller",
                "CH224K, USB PD Controller"
        })
        void shouldDetectCorrectInterfaceType(String mpn, String expected) {
            assertEquals(expected, handler.getInterfaceType(mpn),
                    "Interface type for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MCU parts")
        void shouldReturnEmptyForMCU() {
            assertEquals("", handler.getInterfaceType("CH32V103C8T6"));
            assertEquals("", handler.getInterfaceType("CH32F103C8T6"));
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {

        @Test
        @DisplayName("Document WCH RISC-V MCU product families")
        void documentRiscVProductFamilies() {
            System.out.println("WCH RISC-V MCU Product Families:");
            System.out.println("CH32V003 = Entry-level RISC-V (QingKe V2A, 48MHz, 2KB SRAM, <$0.10)");
            System.out.println("CH32V103 = General Purpose RISC-V (QingKe V3A, 80MHz, 20KB SRAM)");
            System.out.println("CH32V203 = Enhanced RISC-V (QingKe V4B, 144MHz, 64KB SRAM, USB)");
            System.out.println("CH32V208 = RISC-V with BLE (QingKe V4C, BLE 5.3)");
            System.out.println("CH32V303 = High-Performance RISC-V (QingKe V4F, 144MHz)");
            System.out.println("CH32V305 = RISC-V with USB HS (High-Speed USB 2.0)");
            System.out.println("CH32V307 = RISC-V with Ethernet (10M/100M Ethernet MAC)");
        }

        @Test
        @DisplayName("Document WCH ARM MCU product families")
        void documentArmProductFamilies() {
            System.out.println("WCH ARM MCU Product Families:");
            System.out.println("CH32F103 = ARM Cortex-M3 (Compatible with STM32F103)");
            System.out.println("CH32F203 = ARM Cortex-M3 Enhanced (More peripherals)");
        }

        @Test
        @DisplayName("Document WCH USB interface families")
        void documentUSBFamilies() {
            System.out.println("WCH USB Interface Product Families:");
            System.out.println("CH340 = USB to Serial (Most popular, <$0.30)");
            System.out.println("  CH340G = SOP-16, requires external crystal");
            System.out.println("  CH340C = SOP-16, internal crystal");
            System.out.println("  CH340K = ESSOP-10, compact");
            System.out.println("  CH340N = SOP-8, ultra-compact");
            System.out.println("  CH340E = MSOP-10, tiny");
            System.out.println("CH341 = USB to Serial/I2C/SPI/Parallel (Multi-interface)");
            System.out.println("CH9340 = Enhanced CH340 (Better compatibility)");
            System.out.println("CH342-CH348 = Various USB UART bridges");
            System.out.println("CH395 = Ethernet Controller (TCP/IP stack onboard)");
            System.out.println("CH9121 = Ethernet to Serial Converter");
            System.out.println("CH9141/CH9143 = BLE to UART Modules");
            System.out.println("CH334 = USB Hub Controller");
            System.out.println("CH224 = USB PD Controller (Power Delivery)");
        }

        @Test
        @DisplayName("Document MCU package encoding")
        void documentMCUEncoding() {
            System.out.println("WCH MCU MPN Encoding:");
            System.out.println("Format: CH32V103C8T6");
            System.out.println("         CH32 = WCH 32-bit");
            System.out.println("            V = RISC-V (F = ARM)");
            System.out.println("             1 = Series (1xx, 2xx, 3xx)");
            System.out.println("              03 = Product line");
            System.out.println("                C = 48-pin (F/U=20, R=64, V=100)");
            System.out.println("                 8 = 64KB Flash (4=16K, 6=32K, B=128K, C=256K)");
            System.out.println("                  T = Package type (T=LQFP, U=QFN)");
            System.out.println("                   6 = Temperature grade");
        }
    }

    @Nested
    @DisplayName("Comparison with Competitors")
    class CompetitorComparisonTests {

        @Test
        @DisplayName("Document CH340 vs FT232 comparison")
        void documentCH340vsFT232() {
            System.out.println("CH340 vs FT232 Comparison:");
            System.out.println("CH340G: ~$0.30, SOP-16, basic USB-UART, external crystal");
            System.out.println("CH340C: ~$0.35, SOP-16, basic USB-UART, internal crystal");
            System.out.println("FT232RL: ~$4.00, SSOP-28, full modem signals, ESD protection");
            System.out.println("");
            System.out.println("CH340 Pros: Very cheap, widely available, good enough for most uses");
            System.out.println("CH340 Cons: No modem signals (DTR/RTS only), driver issues on some OS");
            System.out.println("FT232 Pros: Full signals, better drivers, better ESD protection");
            System.out.println("FT232 Cons: Much more expensive");
        }

        @Test
        @DisplayName("Document CH32V vs STM32 comparison")
        void documentCH32VvsSTM32() {
            System.out.println("CH32V/F vs STM32 Comparison:");
            System.out.println("CH32F103C8T6: ~$0.50, ARM Cortex-M3, pin-compatible with STM32F103");
            System.out.println("STM32F103C8T6: ~$2.50, ARM Cortex-M3, original design");
            System.out.println("CH32V103C8T6: ~$0.80, RISC-V, similar pinout to STM32F103");
            System.out.println("");
            System.out.println("CH32 Pros: Much cheaper, good documentation, active community");
            System.out.println("CH32 Cons: Less ecosystem support, fewer libraries");
            System.out.println("STM32 Pros: Mature ecosystem, better tools, wider support");
            System.out.println("STM32 Cons: More expensive, supply chain issues");
        }
    }
}
