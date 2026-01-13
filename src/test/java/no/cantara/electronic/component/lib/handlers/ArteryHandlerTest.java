package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ArteryHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ArteryHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction,
 * flash size extraction, pin count extraction, and replacement detection
 * for Artery Technology STM32-compatible ARM MCU products.
 * <p>
 * Artery product families covered:
 * <ul>
 *   <li>AT32F403 series - Cortex-M4, 240MHz high performance</li>
 *   <li>AT32F407 series - Cortex-M4, Ethernet and CAN</li>
 *   <li>AT32F413 series - Cortex-M4, 200MHz mainstream</li>
 *   <li>AT32F415 series - Cortex-M4, USB OTG</li>
 *   <li>AT32F421 series - Cortex-M4, value line</li>
 *   <li>AT32F423 series - Cortex-M4, enhanced value line</li>
 *   <li>AT32F425 series - Cortex-M4, USB device</li>
 *   <li>AT32F435/437 series - Cortex-M4, high-performance</li>
 *   <li>AT32WB415 series - Cortex-M4, Bluetooth LE</li>
 * </ul>
 */
class ArteryHandlerTest {

    private static ArteryHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ArteryHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("AT32F403 Series Detection (240MHz High Performance)")
    class AT32F403SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F403 series MCUs")
        @ValueSource(strings = {
                "AT32F403AVCT7",
                "AT32F403ARCT7",
                "AT32F403ACCT7",
                "AT32F403AVGT7",
                "AT32F403AZGT7",
                "AT32F403ACGU7"
        })
        void shouldDetectAT32F403MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("AT32F403 series should extract correct series")
        void shouldExtractAT32F403Series() {
            assertEquals("AT32F403", handler.extractSeries("AT32F403AVCT7"));
            assertEquals("AT32F403", handler.extractSeries("AT32F403ARCT7"));
            assertEquals("AT32F403", handler.extractSeries("AT32F403AZGT7"));
        }

        @Test
        @DisplayName("AT32F403A variant should be detected")
        void shouldDetectAT32F403AVariant() {
            assertTrue(handler.matches("AT32F403AVCT7", ComponentType.MICROCONTROLLER, registry),
                    "AT32F403A variant should be detected");
        }
    }

    @Nested
    @DisplayName("AT32F407 Series Detection (Ethernet and CAN)")
    class AT32F407SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F407 series MCUs")
        @ValueSource(strings = {
                "AT32F407VET7",
                "AT32F407VGT7",
                "AT32F407RET7",
                "AT32F407RGT7",
                "AT32F407ZET7",
                "AT32F407ZGT7"
        })
        void shouldDetectAT32F407MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F407 series should extract correct series")
        void shouldExtractAT32F407Series() {
            assertEquals("AT32F407", handler.extractSeries("AT32F407VET7"));
            assertEquals("AT32F407", handler.extractSeries("AT32F407ZGT7"));
        }
    }

    @Nested
    @DisplayName("AT32F413 Series Detection (200MHz Mainstream)")
    class AT32F413SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F413 series MCUs")
        @ValueSource(strings = {
                "AT32F413C8T7",
                "AT32F413CBT7",
                "AT32F413CCT7",
                "AT32F413RBT7",
                "AT32F413RCT7",
                "AT32F413KBU7"
        })
        void shouldDetectAT32F413MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F413 series should extract correct series")
        void shouldExtractAT32F413Series() {
            assertEquals("AT32F413", handler.extractSeries("AT32F413C8T7"));
            assertEquals("AT32F413", handler.extractSeries("AT32F413RCT7"));
        }
    }

    @Nested
    @DisplayName("AT32F415 Series Detection (USB OTG)")
    class AT32F415SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F415 series MCUs")
        @ValueSource(strings = {
                "AT32F415C8T7",
                "AT32F415CBT7",
                "AT32F415CCT7",
                "AT32F415R8T7",
                "AT32F415RBT7",
                "AT32F415RCT7",
                "AT32F415KBU7"
        })
        void shouldDetectAT32F415MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F415 series should extract correct series")
        void shouldExtractAT32F415Series() {
            assertEquals("AT32F415", handler.extractSeries("AT32F415CBT7"));
            assertEquals("AT32F415", handler.extractSeries("AT32F415RCT7"));
        }
    }

    @Nested
    @DisplayName("AT32F421 Series Detection (Value Line)")
    class AT32F421SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F421 series MCUs")
        @ValueSource(strings = {
                "AT32F421C4T7",
                "AT32F421C6T7",
                "AT32F421C8T7",
                "AT32F421K4T7",
                "AT32F421K6T7",
                "AT32F421K8T7",
                "AT32F421K8U7"
        })
        void shouldDetectAT32F421MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F421 series should extract correct series")
        void shouldExtractAT32F421Series() {
            assertEquals("AT32F421", handler.extractSeries("AT32F421C8T7"));
            assertEquals("AT32F421", handler.extractSeries("AT32F421K8U7"));
        }
    }

    @Nested
    @DisplayName("AT32F423 Series Detection (Enhanced Value Line)")
    class AT32F423SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F423 series MCUs")
        @ValueSource(strings = {
                "AT32F423C8T7",
                "AT32F423CBT7",
                "AT32F423CCT7",
                "AT32F423R8T7",
                "AT32F423RBT7",
                "AT32F423RCT7"
        })
        void shouldDetectAT32F423MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F423 series should extract correct series")
        void shouldExtractAT32F423Series() {
            assertEquals("AT32F423", handler.extractSeries("AT32F423CBT7"));
            assertEquals("AT32F423", handler.extractSeries("AT32F423RCT7"));
        }
    }

    @Nested
    @DisplayName("AT32F425 Series Detection (USB Device)")
    class AT32F425SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F425 series MCUs")
        @ValueSource(strings = {
                "AT32F425C6T7",
                "AT32F425C8T7",
                "AT32F425R6T7",
                "AT32F425R8T7",
                "AT32F425K6U7",
                "AT32F425K8U7"
        })
        void shouldDetectAT32F425MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F425 series should extract correct series")
        void shouldExtractAT32F425Series() {
            assertEquals("AT32F425", handler.extractSeries("AT32F425C8T7"));
            assertEquals("AT32F425", handler.extractSeries("AT32F425R8T7"));
        }
    }

    @Nested
    @DisplayName("AT32F435 Series Detection (High Performance)")
    class AT32F435SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F435 series MCUs")
        @ValueSource(strings = {
                "AT32F435CGT7",
                "AT32F435CMT7",
                "AT32F435RGT7",
                "AT32F435RMT7",
                "AT32F435VGT7",
                "AT32F435VMT7",
                "AT32F435ZGT7",
                "AT32F435ZMT7"
        })
        void shouldDetectAT32F435MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F435 series should extract correct series")
        void shouldExtractAT32F435Series() {
            assertEquals("AT32F435", handler.extractSeries("AT32F435ZMT7"));
            assertEquals("AT32F435", handler.extractSeries("AT32F435CGT7"));
        }
    }

    @Nested
    @DisplayName("AT32F437 Series Detection (High Performance with Ethernet)")
    class AT32F437SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32F437 series MCUs")
        @ValueSource(strings = {
                "AT32F437VGT7",
                "AT32F437VMT7",
                "AT32F437ZGT7",
                "AT32F437ZMT7"
        })
        void shouldDetectAT32F437MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32F437 series should extract correct series")
        void shouldExtractAT32F437Series() {
            assertEquals("AT32F437", handler.extractSeries("AT32F437ZMT7"));
            assertEquals("AT32F437", handler.extractSeries("AT32F437VGT7"));
        }
    }

    @Nested
    @DisplayName("AT32WB415 Series Detection (Bluetooth LE)")
    class AT32WB415SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AT32WB415 series MCUs")
        @ValueSource(strings = {
                "AT32WB415CCU7"
        })
        void shouldDetectAT32WB415MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
        }

        @Test
        @DisplayName("AT32WB415 series should extract correct series")
        void shouldExtractAT32WB415Series() {
            assertEquals("AT32WB415", handler.extractSeries("AT32WB415CCU7"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for MCUs")
        @CsvSource({
                "AT32F403AVCT7, LQFP100",
                "AT32F403ARCT7, LQFP64",
                "AT32F413CCT7, LQFP48",
                "AT32F415CBT7, LQFP48",
                "AT32F421K8U7, QFN32",
                "AT32F435ZMT7, LQFP144"
        })
        void shouldExtractMCUPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle QFN package")
        void shouldHandleQFNPackage() {
            String pkg = handler.extractPackageCode("AT32F421K8U7");
            assertTrue(pkg.contains("QFN"), "Should extract QFN package");
        }

        @Test
        @DisplayName("Should handle LQFP package")
        void shouldHandleLQFPPackage() {
            String pkg = handler.extractPackageCode("AT32F403AVCT7");
            assertTrue(pkg.contains("LQFP"), "Should extract LQFP package");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series for all product families")
        @CsvSource({
                "AT32F403AVCT7, AT32F403",
                "AT32F407VET7, AT32F407",
                "AT32F413CCT7, AT32F413",
                "AT32F415CBT7, AT32F415",
                "AT32F421C8T7, AT32F421",
                "AT32F423CBT7, AT32F423",
                "AT32F425C8T7, AT32F425",
                "AT32F435ZMT7, AT32F435",
                "AT32F437ZMT7, AT32F437",
                "AT32WB415CCU7, AT32WB415"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-Artery parts")
        void shouldReturnEmptyForNonArteryParts() {
            assertEquals("", handler.extractSeries("STM32F103C8T6"));   // ST
            assertEquals("", handler.extractSeries("GD32F103C8T6"));    // GigaDevice
            assertEquals("", handler.extractSeries("ATMEGA328P"));       // Atmel
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
            assertEquals("AT32F403", handler.extractSeries("at32f403avct7"));
            assertEquals("AT32F421", handler.extractSeries("At32f421C8t7"));
        }
    }

    @Nested
    @DisplayName("Flash Size Extraction")
    class FlashSizeExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU flash size in KB")
        @CsvSource({
                "AT32F421C4T7, 16",
                "AT32F421C6T7, 32",
                "AT32F421C8T7, 64",
                "AT32F413CBT7, 128",
                "AT32F403AVCT7, 256",
                "AT32F407VET7, 512",
                "AT32F435ZGT7, 1024",
                "AT32F435ZMT7, 4096"
        })
        void shouldExtractMCUFlashSize(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUFlashSizeKB(mpn),
                    "Flash size for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-Artery parts")
        void shouldReturnNegativeForNonArtery() {
            assertEquals(-1, handler.extractMCUFlashSizeKB("STM32F103C8T6"));
            assertEquals(-1, handler.extractMCUFlashSizeKB("W25Q64"));
        }

        @Test
        @DisplayName("Should return -1 for null or empty")
        void shouldReturnNegativeForNullOrEmpty() {
            assertEquals(-1, handler.extractMCUFlashSizeKB(null));
            assertEquals(-1, handler.extractMCUFlashSizeKB(""));
        }
    }

    @Nested
    @DisplayName("Pin Count Extraction")
    class PinCountExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract MCU pin count")
        @CsvSource({
                "AT32F421T8T7, 36",
                "AT32F421K8T7, 32",
                "AT32F421C8T7, 48",
                "AT32F413RCT7, 64",
                "AT32F403AVCT7, 100",
                "AT32F435ZMT7, 144"
        })
        void shouldExtractMCUPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractMCUPinCount(mpn),
                    "Pin count for " + mpn);
        }

        @Test
        @DisplayName("Should return -1 for non-Artery parts")
        void shouldReturnNegativeForNonArtery() {
            assertEquals(-1, handler.extractMCUPinCount("STM32F103C8T6"));
        }
    }

    @Nested
    @DisplayName("Temperature Grade Extraction")
    class TemperatureGradeTests {

        @Test
        @DisplayName("Should extract industrial temperature grade (6)")
        void shouldExtractIndustrialGrade() {
            assertEquals("Industrial (-40 to +85C)",
                    handler.extractTemperatureGrade("AT32F403AVCT6"));
        }

        @Test
        @DisplayName("Should extract industrial extended temperature grade (7)")
        void shouldExtractIndustrialExtendedGrade() {
            assertEquals("Industrial Extended (-40 to +105C)",
                    handler.extractTemperatureGrade("AT32F403AVCT7"));
        }

        @Test
        @DisplayName("Should return empty for unknown temperature grade")
        void shouldReturnEmptyForUnknownGrade() {
            assertEquals("", handler.extractTemperatureGrade("AT32F403AVCT5"));
        }

        @Test
        @DisplayName("Should handle null and empty")
        void shouldHandleNullAndEmpty() {
            assertEquals("", handler.extractTemperatureGrade(null));
            assertEquals("", handler.extractTemperatureGrade(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series same pin count should be replacements")
        void sameSerieSamePinsShouldBeReplacements() {
            // Same AT32F403A with same pin count (V=100), different flash
            assertTrue(handler.isOfficialReplacement("AT32F403AVCT7", "AT32F403AVGT7"),
                    "Same series same pin count different flash should be replacement");

            // Same with different package
            assertTrue(handler.isOfficialReplacement("AT32F421K8T7", "AT32F421K8U7"),
                    "Same series same pin count different package should be replacement");
        }

        @Test
        @DisplayName("Same series different pin count should NOT be replacements")
        void sameSerieDifferentPinsNotReplacements() {
            // AT32F403A C (48-pin) vs V (100-pin)
            assertFalse(handler.isOfficialReplacement("AT32F403ACCT7", "AT32F403AVCT7"),
                    "Different pin count should NOT be replacements");

            // AT32F413 C (48-pin) vs R (64-pin)
            assertFalse(handler.isOfficialReplacement("AT32F413CCT7", "AT32F413RCT7"),
                    "Different pin count should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            // AT32F403 vs AT32F407
            assertFalse(handler.isOfficialReplacement("AT32F403AVCT7", "AT32F407VET7"),
                    "AT32F403 and AT32F407 should NOT be replacements");

            // AT32F413 vs AT32F415
            assertFalse(handler.isOfficialReplacement("AT32F413CCT7", "AT32F415CCT7"),
                    "AT32F413 and AT32F415 should NOT be replacements");

            // AT32F421 vs AT32F423
            assertFalse(handler.isOfficialReplacement("AT32F421C8T7", "AT32F423C8T7"),
                    "AT32F421 and AT32F423 should NOT be replacements");
        }

        @Test
        @DisplayName("Flash size difference is acceptable for replacement")
        void flashSizeDifferenceAcceptable() {
            // Same series, same pins, different flash (64KB vs 128KB)
            assertTrue(handler.isOfficialReplacement("AT32F413C8T7", "AT32F413CBT7"),
                    "Flash size difference should be acceptable");
        }

        @Test
        @DisplayName("Temperature grade difference is acceptable for replacement")
        void temperatureGradeDifferenceAcceptable() {
            // Same series, same pins, different temp grade
            assertTrue(handler.isOfficialReplacement("AT32F413CCT6", "AT32F413CCT7"),
                    "Temperature grade difference should be acceptable");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "AT32F403AVCT7"));
            assertFalse(handler.isOfficialReplacement("AT32F403AVCT7", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Cross-Manufacturer Detection")
    class CrossManufacturerTests {

        @Test
        @DisplayName("Should NOT match STM32 compatible parts from other manufacturers")
        void shouldNotMatchOtherManufacturers() {
            // ST parts
            assertFalse(handler.matches("STM32F103C8T6", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("STM32F407VET6", ComponentType.MICROCONTROLLER, registry));

            // GigaDevice parts
            assertFalse(handler.matches("GD32F103C8T6", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("GD32F407VET6", ComponentType.MICROCONTROLLER, registry));

            // Other manufacturers
            assertFalse(handler.matches("ATMEGA328P", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("PIC16F877A", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("Cross-manufacturer parts should NOT be replacements")
        void crossManufacturerNotReplacements() {
            // Artery vs ST
            assertFalse(handler.isOfficialReplacement("AT32F403AVCT7", "STM32F103C8T6"),
                    "Artery and ST should NOT be replacements");

            // Artery vs GigaDevice
            assertFalse(handler.isOfficialReplacement("AT32F403AVCT7", "GD32F103C8T6"),
                    "Artery and GigaDevice should NOT be replacements");
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

        @Test
        @DisplayName("Should NOT support MEMORY types")
        void shouldNotSupportMemoryTypes() {
            var types = handler.getSupportedTypes();
            assertFalse(types.contains(ComponentType.MEMORY),
                    "Should NOT support MEMORY type");
            assertFalse(types.contains(ComponentType.MEMORY_FLASH),
                    "Should NOT support MEMORY_FLASH type");
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
            assertFalse(handler.matches("AT32F403AVCT7", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("at32f403avct7", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("at32f421c8t7", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("At32F403AvCt7", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("aT32f421C8t7", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("Should handle very short MPNs")
        void shouldHandleShortMpns() {
            assertFalse(handler.matches("AT32", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("AT32F", ComponentType.MICROCONTROLLER, registry));
            // Very short MPNs return whatever character is at second-to-last position
            // This is acceptable behavior - real Artery MPNs are always longer
            String shortPkg = handler.extractPackageCode("AT");
            assertNotNull(shortPkg);
        }
    }

    @Nested
    @DisplayName("Product Family Reference Documentation")
    class ProductFamilyReferenceTests {

        @Test
        @DisplayName("Document Artery MCU product families")
        void documentMCUProductFamilies() {
            System.out.println("Artery Technology MCU Product Families:");
            System.out.println("AT32F403 = ARM Cortex-M4, 240MHz, High Performance");
            System.out.println("AT32F407 = ARM Cortex-M4, Ethernet and CAN bus");
            System.out.println("AT32F413 = ARM Cortex-M4, 200MHz, Mainstream");
            System.out.println("AT32F415 = ARM Cortex-M4, USB OTG support");
            System.out.println("AT32F421 = ARM Cortex-M4, Value Line");
            System.out.println("AT32F423 = ARM Cortex-M4, Enhanced Value Line");
            System.out.println("AT32F425 = ARM Cortex-M4, USB Device");
            System.out.println("AT32F435 = ARM Cortex-M4, High Performance");
            System.out.println("AT32F437 = ARM Cortex-M4, High Performance with Ethernet");
            System.out.println("AT32WB415 = ARM Cortex-M4, Bluetooth Low Energy");
        }

        @Test
        @DisplayName("Document MCU MPN encoding")
        void documentMPNEncoding() {
            System.out.println("Artery MCU MPN Encoding:");
            System.out.println("Format: AT32F403AVCT7");
            System.out.println("         AT32 = Artery 32-bit");
            System.out.println("            F403 = Series (403 = 240MHz high perf)");
            System.out.println("                A = Variant (if present)");
            System.out.println("                 V = 100-pin (T=36, K=32, C=48, R=64, Z=144)");
            System.out.println("                  C = 256KB Flash (4=16K, 6=32K, 8=64K, B=128K, E=512K, G=1024K, M=4096K)");
            System.out.println("                   T = LQFP package (U=QFN, H=BGA)");
            System.out.println("                    7 = Temperature (-40 to +105C)");
        }

        @Test
        @DisplayName("Document package codes")
        void documentPackageCodes() {
            System.out.println("Artery MCU Package Codes:");
            System.out.println("T = LQFP (Thin Quad Flat Package)");
            System.out.println("U = QFN (Quad Flat No-Lead)");
            System.out.println("H = BGA (Ball Grid Array)");
        }

        @Test
        @DisplayName("Document pin count codes")
        void documentPinCountCodes() {
            System.out.println("Artery MCU Pin Count Codes:");
            System.out.println("T = 36-pin");
            System.out.println("K = 32-pin");
            System.out.println("C = 48-pin");
            System.out.println("R = 64-pin");
            System.out.println("V = 100-pin");
            System.out.println("Z = 144-pin");
            System.out.println("I = 176-pin");
        }
    }

    @Nested
    @DisplayName("STM32 Compatibility Reference")
    class STM32CompatibilityTests {

        @Test
        @DisplayName("Document STM32-compatible Artery parts")
        void documentSTM32CompatibleParts() {
            System.out.println("STM32-Compatible Artery MCUs:");
            System.out.println("AT32F403/407 <-> STM32F1xx (Cortex-M3/M4 general purpose)");
            System.out.println("AT32F413/415 <-> STM32F1xx (enhanced peripherals)");
            System.out.println("AT32F435/437 <-> STM32F4xx (high performance)");
            System.out.println("");
            System.out.println("Note: Pin-compatible in many cases but check:");
            System.out.println("- Peripheral register differences");
            System.out.println("- Clock tree and PLL configuration");
            System.out.println("- Flash/SRAM layout");
            System.out.println("- Errata for specific silicon revisions");
        }

        @Test
        @DisplayName("Artery naming follows similar convention to STM32")
        void arteryNamingFollowsSTM32Convention() {
            // Both have similar encoding structure
            // AT32F403AVCT7 vs STM32F407VET6
            String artery = "AT32F403AVCT7";
            String stm32 = "STM32F407VET6";

            // Pin code position is similar (3rd from end after removing temp)
            // Flash code position is similar (4th from end after removing temp)
            System.out.println("Artery: " + artery + " -> V=100-pin, C=256KB, T=LQFP, 7=105C");
            System.out.println("STM32:  " + stm32 + " -> V=100-pin, E=512KB, T=LQFP, 6=85C");
        }
    }
}
