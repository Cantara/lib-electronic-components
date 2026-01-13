package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.HoltekHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for HoltekHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction,
 * and replacement detection for Holtek Semiconductor products.
 * <p>
 * Holtek product families covered:
 * <ul>
 *   <li>HT66Fxxxx - Flash MCU (general purpose)</li>
 *   <li>HT67Fxxxx - Flash MCU (with LCD driver)</li>
 *   <li>HT68Fxxxx - Flash MCU (enhanced I/O)</li>
 *   <li>HT45Fxxxx - Touch Key MCU</li>
 *   <li>BS83xxxx - Capacitive Touch MCU (Body Sensor)</li>
 *   <li>HT82V739 - Voice/Audio IC</li>
 *   <li>HT42B534 - USB to UART Bridge IC</li>
 *   <li>HT1621/HT1628 - LCD Controller Drivers</li>
 * </ul>
 */
class HoltekHandlerTest {

    private static HoltekHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new HoltekHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("HT66F Flash MCU Detection")
    class HT66FFlashMCUTests {

        @ParameterizedTest
        @DisplayName("Should detect HT66F series flash MCUs")
        @ValueSource(strings = {
                "HT66F0185",
                "HT66F0172",
                "HT66F0175",
                "HT66F0176",
                "HT66F0185-1",
                "HT66F0185-3",
                "HT66F002",
                "HT66F004",
                "HT66F70A",
                "HT66F2350",
                "HT66F3185"
        })
        void shouldDetectHT66FMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("HT66F series should extract correct series")
        void shouldExtractHT66FSeries() {
            assertEquals("HT66F", handler.extractSeries("HT66F0185"));
            assertEquals("HT66F", handler.extractSeries("HT66F0172"));
            assertEquals("HT66F", handler.extractSeries("HT66F3185"));
        }

        @Test
        @DisplayName("HT66F should be identified as Flash MCU")
        void shouldBeIdentifiedAsFlashMCU() {
            assertTrue(handler.isFlashMCU("HT66F0185"));
            assertTrue(handler.isFlashMCU("HT66F002"));
        }
    }

    @Nested
    @DisplayName("HT67F Flash MCU with LCD Detection")
    class HT67FFlashMCUTests {

        @ParameterizedTest
        @DisplayName("Should detect HT67F series flash MCUs with LCD")
        @ValueSource(strings = {
                "HT67F5640",
                "HT67F5650",
                "HT67F5660",
                "HT67F2350",
                "HT67F2360",
                "HT67F489"
        })
        void shouldDetectHT67FMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("HT67F series should extract correct series")
        void shouldExtractHT67FSeries() {
            assertEquals("HT67F", handler.extractSeries("HT67F5640"));
            assertEquals("HT67F", handler.extractSeries("HT67F2350"));
        }

        @Test
        @DisplayName("HT67F should be identified as Flash MCU")
        void shouldBeIdentifiedAsFlashMCU() {
            assertTrue(handler.isFlashMCU("HT67F5640"));
            assertTrue(handler.isFlashMCU("HT67F2350"));
        }
    }

    @Nested
    @DisplayName("HT68F Enhanced I/O MCU Detection")
    class HT68FEnhancedIOMCUTests {

        @ParameterizedTest
        @DisplayName("Should detect HT68F series enhanced I/O MCUs")
        @ValueSource(strings = {
                "HT68F002",
                "HT68F003",
                "HT68F001",
                "HT68F30",
                "HT68F40"
        })
        void shouldDetectHT68FMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("HT68F series should extract correct series")
        void shouldExtractHT68FSeries() {
            assertEquals("HT68F", handler.extractSeries("HT68F002"));
            assertEquals("HT68F", handler.extractSeries("HT68F30"));
        }

        @Test
        @DisplayName("HT68F should be identified as Flash MCU")
        void shouldBeIdentifiedAsFlashMCU() {
            assertTrue(handler.isFlashMCU("HT68F002"));
            assertTrue(handler.isFlashMCU("HT68F30"));
        }
    }

    @Nested
    @DisplayName("HT45F Touch Key MCU Detection")
    class HT45FTouchMCUTests {

        @ParameterizedTest
        @DisplayName("Should detect HT45F series touch key MCUs")
        @ValueSource(strings = {
                "HT45F0072",
                "HT45F0062",
                "HT45F0057",
                "HT45F0058",
                "HT45F0059",
                "HT45F3820",
                "HT45F75"
        })
        void shouldDetectHT45FMCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("HT45F series should extract correct series")
        void shouldExtractHT45FSeries() {
            assertEquals("HT45F", handler.extractSeries("HT45F0072"));
            assertEquals("HT45F", handler.extractSeries("HT45F3820"));
        }

        @Test
        @DisplayName("HT45F should be identified as Touch MCU")
        void shouldBeIdentifiedAsTouchMCU() {
            assertTrue(handler.isTouchMCU("HT45F0072"));
            assertTrue(handler.isTouchMCU("HT45F3820"));
        }

        @Test
        @DisplayName("HT45F should NOT be identified as Flash MCU")
        void shouldNotBeFlashMCU() {
            assertFalse(handler.isFlashMCU("HT45F0072"));
        }
    }

    @Nested
    @DisplayName("BS83 Capacitive Touch MCU Detection")
    class BS83TouchMCUTests {

        @ParameterizedTest
        @DisplayName("Should detect BS83 series capacitive touch MCUs")
        @ValueSource(strings = {
                "BS83B16A-3",
                "BS83B12A-3",
                "BS83B08A-3",
                "BS83B04A-3",
                "BS83A04A-3",
                "BS83A02A-3",
                "BS83B16A-1",
                "BS83B16A"
        })
        void shouldDetectBS83MCU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    mpn + " should match MICROCONTROLLER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("BS83 series should extract correct series")
        void shouldExtractBS83Series() {
            assertEquals("BS83B", handler.extractSeries("BS83B16A-3"));
            assertEquals("BS83A", handler.extractSeries("BS83A04A-3"));
            assertEquals("BS83", handler.extractSeries("BS8316A"));
        }

        @Test
        @DisplayName("BS83 should be identified as Touch MCU")
        void shouldBeIdentifiedAsTouchMCU() {
            assertTrue(handler.isTouchMCU("BS83B16A-3"));
            assertTrue(handler.isTouchMCU("BS83A04A-3"));
        }

        @Test
        @DisplayName("BS83 should NOT be identified as Flash MCU")
        void shouldNotBeFlashMCU() {
            assertFalse(handler.isFlashMCU("BS83B16A-3"));
        }
    }

    @Nested
    @DisplayName("HT82V Voice/Audio IC Detection")
    class HT82VVoiceICTests {

        @ParameterizedTest
        @DisplayName("Should detect HT82V series voice/audio ICs")
        @ValueSource(strings = {
                "HT82V739",
                "HT82V733",
                "HT82V735",
                "HT82V742"
        })
        void shouldDetectHT82VIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("HT82V should NOT match MICROCONTROLLER")
        void shouldNotMatchMicrocontroller() {
            assertFalse(handler.matches("HT82V739", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("HT82V series should extract correct series")
        void shouldExtractHT82VSeries() {
            assertEquals("HT82V", handler.extractSeries("HT82V739"));
            assertEquals("HT82V", handler.extractSeries("HT82V733"));
        }

        @Test
        @DisplayName("HT82V should be identified as Voice IC")
        void shouldBeIdentifiedAsVoiceIC() {
            assertTrue(handler.isVoiceIC("HT82V739"));
            assertTrue(handler.isVoiceIC("HT82V733"));
        }

        @Test
        @DisplayName("HT82V should NOT be other types")
        void shouldNotBeOtherTypes() {
            assertFalse(handler.isFlashMCU("HT82V739"));
            assertFalse(handler.isTouchMCU("HT82V739"));
            assertFalse(handler.isUSBIC("HT82V739"));
            assertFalse(handler.isLCDDriver("HT82V739"));
        }
    }

    @Nested
    @DisplayName("HT42B USB IC Detection")
    class HT42BUSBICTests {

        @ParameterizedTest
        @DisplayName("Should detect HT42B series USB ICs")
        @ValueSource(strings = {
                "HT42B534-1",
                "HT42B534-2",
                "HT42B532",
                "HT42B564"
        })
        void shouldDetectHT42BIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("HT42B should NOT match MICROCONTROLLER")
        void shouldNotMatchMicrocontroller() {
            assertFalse(handler.matches("HT42B534-1", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("HT42B series should extract correct series")
        void shouldExtractHT42BSeries() {
            assertEquals("HT42B", handler.extractSeries("HT42B534-1"));
            assertEquals("HT42B", handler.extractSeries("HT42B532"));
        }

        @Test
        @DisplayName("HT42B should be identified as USB IC")
        void shouldBeIdentifiedAsUSBIC() {
            assertTrue(handler.isUSBIC("HT42B534-1"));
            assertTrue(handler.isUSBIC("HT42B532"));
        }

        @Test
        @DisplayName("HT42B should NOT be other types")
        void shouldNotBeOtherTypes() {
            assertFalse(handler.isFlashMCU("HT42B534-1"));
            assertFalse(handler.isTouchMCU("HT42B534-1"));
            assertFalse(handler.isVoiceIC("HT42B534-1"));
            assertFalse(handler.isLCDDriver("HT42B534-1"));
        }
    }

    @Nested
    @DisplayName("HT16xx LCD Driver Detection")
    class HT16xxLCDDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect HT16xx series LCD drivers")
        @ValueSource(strings = {
                "HT1621",
                "HT1621B",
                "HT1621D",
                "HT1628",
                "HT1628A",
                "HT1632",
                "HT1632C",
                "HT1650"
        })
        void shouldDetectHT16xxIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("HT16xx should NOT match MICROCONTROLLER")
        void shouldNotMatchMicrocontroller() {
            assertFalse(handler.matches("HT1621", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("HT1628", ComponentType.MICROCONTROLLER, registry));
        }

        @Test
        @DisplayName("HT16xx series should extract correct series")
        void shouldExtractHT16xxSeries() {
            assertEquals("HT1621", handler.extractSeries("HT1621"));
            assertEquals("HT1621", handler.extractSeries("HT1621B"));
            assertEquals("HT1628", handler.extractSeries("HT1628"));
            assertEquals("HT1632", handler.extractSeries("HT1632C"));
        }

        @Test
        @DisplayName("HT16xx should be identified as LCD Driver")
        void shouldBeIdentifiedAsLCDDriver() {
            assertTrue(handler.isLCDDriver("HT1621"));
            assertTrue(handler.isLCDDriver("HT1621B"));
            assertTrue(handler.isLCDDriver("HT1628"));
            assertTrue(handler.isLCDDriver("HT1632C"));
        }

        @Test
        @DisplayName("HT16xx should NOT be other types")
        void shouldNotBeOtherTypes() {
            assertFalse(handler.isFlashMCU("HT1621"));
            assertFalse(handler.isTouchMCU("HT1621"));
            assertFalse(handler.isVoiceIC("HT1621"));
            assertFalse(handler.isUSBIC("HT1621"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from suffix")
        @CsvSource({
                "BS83B16A-3, SSOP",
                "BS83B16A-1, SOP",
                "BS83B16A-2, SOP",
                "HT66F0185-3, SSOP",
                "HT66F0185-1, SOP"
        })
        void shouldExtractPackageCodeFromSuffix(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for parts without package suffix")
        void shouldReturnEmptyWithoutSuffix() {
            assertEquals("", handler.extractPackageCode("HT1621"));
            assertEquals("", handler.extractPackageCode("HT82V739"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series for all product families")
        @CsvSource({
                "HT66F0185, HT66F",
                "HT67F5640, HT67F",
                "HT68F002, HT68F",
                "HT45F0072, HT45F",
                "BS83B16A-3, BS83B",
                "BS83A04A-3, BS83A",
                "HT82V739, HT82V",
                "HT42B534-1, HT42B",
                "HT1621, HT1621",
                "HT1621B, HT1621",
                "HT1628, HT1628",
                "HT1632C, HT1632"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-Holtek parts")
        void shouldReturnEmptyForNonHoltekParts() {
            assertEquals("", handler.extractSeries("STM32F103"));
            assertEquals("", handler.extractSeries("ATMEGA328P"));
            assertEquals("", handler.extractSeries("PIC16F877A"));
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
            assertEquals("HT66F", handler.extractSeries("ht66f0185"));
            assertEquals("BS83B", handler.extractSeries("bs83b16a-3"));
        }
    }

    @Nested
    @DisplayName("Product Code Extraction")
    class ProductCodeExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract product code")
        @CsvSource({
                "HT66F0185, HT66F0185",
                "HT66F0185-1, HT66F0185",
                "HT66F0185-3, HT66F0185",
                "BS83B16A-3, BS83B16A",
                "BS83B16A, BS83B16A",
                "HT1621B, HT1621B",
                "HT82V739, HT82V739"
        })
        void shouldExtractProductCode(String mpn, String expected) {
            assertEquals(expected, handler.extractProductCode(mpn),
                    "Product code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractProductCode(null));
            assertEquals("", handler.extractProductCode(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementDetectionTests {

        @Test
        @DisplayName("Same product different package should be replacements")
        void sameProductDifferentPackageShouldBeReplacements() {
            // Same BS83B16A in different packages
            assertTrue(handler.isOfficialReplacement("BS83B16A-1", "BS83B16A-3"),
                    "Same product different package should be replacement");

            // Same HT66F0185 in different packages
            assertTrue(handler.isOfficialReplacement("HT66F0185-1", "HT66F0185-3"),
                    "Same product different package should be replacement");

            // Same product with and without suffix
            assertTrue(handler.isOfficialReplacement("BS83B16A", "BS83B16A-3"),
                    "Same product with/without package suffix should be replacement");
        }

        @Test
        @DisplayName("Different products should NOT be replacements")
        void differentProductsNotReplacements() {
            // Different product numbers
            assertFalse(handler.isOfficialReplacement("BS83B16A-3", "BS83B12A-3"),
                    "Different touch key counts should NOT be replacements");

            // Different series
            assertFalse(handler.isOfficialReplacement("HT66F0185", "HT67F5640"),
                    "Different series should NOT be replacements");

            // Different product families
            assertFalse(handler.isOfficialReplacement("HT66F0185", "BS83B16A-3"),
                    "Flash MCU and Touch MCU should NOT be replacements");
        }

        @Test
        @DisplayName("LCD driver variants (B, D) are NOT direct replacements")
        void lcdDriverVariantsNotReplacements() {
            // HT1621 and HT1621B are different products (B is enhanced version)
            // They are NOT direct drop-in replacements
            assertFalse(handler.isOfficialReplacement("HT1621", "HT1621B"),
                    "HT1621 and HT1621B are different products");
            assertFalse(handler.isOfficialReplacement("HT1621", "HT1621D"),
                    "HT1621 and HT1621D are different products");
        }

        @Test
        @DisplayName("Same LCD driver product should be replacement")
        void sameLcdDriverShouldBeReplacement() {
            // Same product in different packages would be replacements
            // Note: HT1621-specific package variants follow same pattern
            assertTrue(handler.isOfficialReplacement("HT1621", "HT1621"),
                    "Same product should be replacement");
            assertTrue(handler.isOfficialReplacement("HT1628", "HT1628"),
                    "Same product should be replacement");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "HT66F0185"));
            assertFalse(handler.isOfficialReplacement("HT66F0185", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Type Classification")
    class TypeClassificationTests {

        @Test
        @DisplayName("Flash MCUs should only be Flash MCU type")
        void flashMcuClassification() {
            String[] flashMcus = {"HT66F0185", "HT67F5640", "HT68F002"};
            for (String mpn : flashMcus) {
                assertTrue(handler.isFlashMCU(mpn), mpn + " should be Flash MCU");
                assertFalse(handler.isTouchMCU(mpn), mpn + " should NOT be Touch MCU");
                assertFalse(handler.isVoiceIC(mpn), mpn + " should NOT be Voice IC");
                assertFalse(handler.isUSBIC(mpn), mpn + " should NOT be USB IC");
                assertFalse(handler.isLCDDriver(mpn), mpn + " should NOT be LCD Driver");
            }
        }

        @Test
        @DisplayName("Touch MCUs should only be Touch MCU type")
        void touchMcuClassification() {
            String[] touchMcus = {"HT45F0072", "BS83B16A-3"};
            for (String mpn : touchMcus) {
                assertTrue(handler.isTouchMCU(mpn), mpn + " should be Touch MCU");
                assertFalse(handler.isFlashMCU(mpn), mpn + " should NOT be Flash MCU");
                assertFalse(handler.isVoiceIC(mpn), mpn + " should NOT be Voice IC");
                assertFalse(handler.isUSBIC(mpn), mpn + " should NOT be USB IC");
                assertFalse(handler.isLCDDriver(mpn), mpn + " should NOT be LCD Driver");
            }
        }

        @Test
        @DisplayName("Voice ICs should only be Voice IC type")
        void voiceIcClassification() {
            String mpn = "HT82V739";
            assertTrue(handler.isVoiceIC(mpn), mpn + " should be Voice IC");
            assertFalse(handler.isFlashMCU(mpn), mpn + " should NOT be Flash MCU");
            assertFalse(handler.isTouchMCU(mpn), mpn + " should NOT be Touch MCU");
            assertFalse(handler.isUSBIC(mpn), mpn + " should NOT be USB IC");
            assertFalse(handler.isLCDDriver(mpn), mpn + " should NOT be LCD Driver");
        }

        @Test
        @DisplayName("USB ICs should only be USB IC type")
        void usbIcClassification() {
            String mpn = "HT42B534-1";
            assertTrue(handler.isUSBIC(mpn), mpn + " should be USB IC");
            assertFalse(handler.isFlashMCU(mpn), mpn + " should NOT be Flash MCU");
            assertFalse(handler.isTouchMCU(mpn), mpn + " should NOT be Touch MCU");
            assertFalse(handler.isVoiceIC(mpn), mpn + " should NOT be Voice IC");
            assertFalse(handler.isLCDDriver(mpn), mpn + " should NOT be LCD Driver");
        }

        @Test
        @DisplayName("LCD Drivers should only be LCD Driver type")
        void lcdDriverClassification() {
            String[] lcdDrivers = {"HT1621", "HT1621B", "HT1628"};
            for (String mpn : lcdDrivers) {
                assertTrue(handler.isLCDDriver(mpn), mpn + " should be LCD Driver");
                assertFalse(handler.isFlashMCU(mpn), mpn + " should NOT be Flash MCU");
                assertFalse(handler.isTouchMCU(mpn), mpn + " should NOT be Touch MCU");
                assertFalse(handler.isVoiceIC(mpn), mpn + " should NOT be Voice IC");
                assertFalse(handler.isUSBIC(mpn), mpn + " should NOT be USB IC");
            }
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
                    "Should support exactly 2 types (MICROCONTROLLER, IC)");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractProductCode(null));
            assertFalse(handler.isFlashMCU(null));
            assertFalse(handler.isTouchMCU(null));
            assertFalse(handler.isVoiceIC(null));
            assertFalse(handler.isUSBIC(null));
            assertFalse(handler.isLCDDriver(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractProductCode(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("HT66F0185", null, registry));
        }

        @Test
        @DisplayName("Should NOT match non-Holtek patterns")
        void shouldNotMatchNonHoltekPatterns() {
            // Other MCU manufacturers
            assertFalse(handler.matches("STM32F103", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("ATMEGA328P", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("PIC16F877A", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("ESP32", ComponentType.MICROCONTROLLER, registry));

            // Other IC manufacturers
            assertFalse(handler.matches("LM358", ComponentType.IC, registry));
            assertFalse(handler.matches("NE555", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("ht66f0185", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("bs83b16a-3", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("ht1621", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Ht66F0185", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("Bs83B16A-3", ComponentType.MICROCONTROLLER, registry));
            assertTrue(handler.matches("hT1621b", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle very short MPNs")
        void shouldHandleShortMpns() {
            assertFalse(handler.matches("HT", ComponentType.IC, registry));
            assertFalse(handler.matches("BS", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode("HT"));
            assertEquals("", handler.extractSeries("HT"));
        }
    }

    @Nested
    @DisplayName("Cross-Product Type Tests")
    class CrossProductTests {

        @Test
        @DisplayName("MCUs should match both MICROCONTROLLER and IC types")
        void mcusShouldMatchBothTypes() {
            String[] mcus = {"HT66F0185", "HT45F0072", "BS83B16A-3"};
            for (String mpn : mcus) {
                assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                        mpn + " should match MICROCONTROLLER");
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("Non-MCU ICs should NOT match MICROCONTROLLER")
        void nonMcuIcsShouldNotMatchMicrocontroller() {
            String[] nonMcuIcs = {"HT82V739", "HT42B534-1", "HT1621", "HT1628"};
            for (String mpn : nonMcuIcs) {
                assertFalse(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                        mpn + " should NOT match MICROCONTROLLER");
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }
    }
}
