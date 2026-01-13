package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.FTDIHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for FTDIHandler.
 * Tests USB to serial, I2C, SPI, FIFO, and other FTDI interface ICs.
 */
class FTDIHandlerTest {

    private static FTDIHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new FTDIHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("USB Serial Detection - FT232 Series (Single Channel)")
    class FT232SeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT232R",
            "FT232RL",
            "FT232RQ",
            "FT232RL-REEL",
            "FT232RQ-REEL"
        })
        void shouldDetectFT232Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForFT232() {
            assertEquals("FT232", handler.extractSeries("FT232R"));
            assertEquals("FT232", handler.extractSeries("FT232RL"));
            assertEquals("FT232", handler.extractSeries("FT232RQ"));
            assertEquals("FT232", handler.extractSeries("FT232RL-REEL"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("UART", handler.getInterfaceType("FT232RL"));
        }

        @Test
        void shouldHaveCorrectChannelCount() {
            assertEquals(1, handler.getChannelCount("FT232RL"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0 Full-Speed", handler.getUsbVersion("FT232RL"));
        }
    }

    @Nested
    @DisplayName("USB Serial Detection - FT2232 Series (Dual Channel)")
    class FT2232SeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT2232D",
            "FT2232H",
            "FT2232H-REEL",
            "FT2232HL"
        })
        void shouldDetectFT2232Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForFT2232() {
            assertEquals("FT2232", handler.extractSeries("FT2232D"));
            assertEquals("FT2232", handler.extractSeries("FT2232H"));
            assertEquals("FT2232", handler.extractSeries("FT2232H-REEL"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("UART", handler.getInterfaceType("FT2232H"));
        }

        @Test
        void shouldHaveCorrectChannelCount() {
            assertEquals(2, handler.getChannelCount("FT2232H"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0 High-Speed", handler.getUsbVersion("FT2232H"));
        }
    }

    @Nested
    @DisplayName("USB Serial Detection - FT4232 Series (Quad Channel)")
    class FT4232SeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT4232H",
            "FT4232H-REEL",
            "FT4232HL"
        })
        void shouldDetectFT4232Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForFT4232() {
            assertEquals("FT4232", handler.extractSeries("FT4232H"));
            assertEquals("FT4232", handler.extractSeries("FT4232H-REEL"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("UART", handler.getInterfaceType("FT4232H"));
        }

        @Test
        void shouldHaveCorrectChannelCount() {
            assertEquals(4, handler.getChannelCount("FT4232H"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0 High-Speed", handler.getUsbVersion("FT4232H"));
        }
    }

    @Nested
    @DisplayName("USB Serial Detection - X Series (FT230X, FT231X, FT234X)")
    class XSeriesUartTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT230XS",
            "FT230XS-R",
            "FT230XQ",
            "FT231XS",
            "FT231XS-R",
            "FT231XQ",
            "FT234XD",
            "FT234XD-R"
        })
        void shouldDetectXSeriesUart(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForXSeries() {
            assertEquals("FT230X", handler.extractSeries("FT230XS"));
            assertEquals("FT230X", handler.extractSeries("FT230XS-R"));
            assertEquals("FT231X", handler.extractSeries("FT231XS"));
            assertEquals("FT234X", handler.extractSeries("FT234XD"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("UART", handler.getInterfaceType("FT230XS"));
            assertEquals("UART", handler.getInterfaceType("FT231XS"));
            assertEquals("UART", handler.getInterfaceType("FT234XD"));
        }

        @Test
        void shouldHaveCorrectChannelCount() {
            assertEquals(1, handler.getChannelCount("FT230XS"));
            assertEquals(1, handler.getChannelCount("FT231XS"));
            assertEquals(1, handler.getChannelCount("FT234XD"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0 Full-Speed", handler.getUsbVersion("FT230XS"));
        }
    }

    @Nested
    @DisplayName("USB I2C Detection - FT200X, FT201X, FT260")
    class UsbI2CTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT200XD",
            "FT200XD-R",
            "FT201XS",
            "FT201XQ"
        })
        void shouldDetectUSBI2CSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "FT260S",
            "FT260Q",
            "FT260S-R"
        })
        void shouldDetectFT260Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForI2C() {
            assertEquals("FT200X", handler.extractSeries("FT200XD"));
            assertEquals("FT201X", handler.extractSeries("FT201XS"));
            assertEquals("FT260", handler.extractSeries("FT260S"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("I2C", handler.getInterfaceType("FT200XD"));
            assertEquals("I2C", handler.getInterfaceType("FT201XS"));
            assertEquals("I2C/UART", handler.getInterfaceType("FT260S"));
        }
    }

    @Nested
    @DisplayName("USB SPI Detection - FT220X, FT221X")
    class UsbSPITests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT220XS",
            "FT220XQ",
            "FT221XS",
            "FT221XQ"
        })
        void shouldDetectUSBSPISeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForSPI() {
            assertEquals("FT220X", handler.extractSeries("FT220XS"));
            assertEquals("FT221X", handler.extractSeries("FT221XS"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("SPI", handler.getInterfaceType("FT220XS"));
            assertEquals("SPI", handler.getInterfaceType("FT221XS"));
        }
    }

    @Nested
    @DisplayName("USB FIFO Detection - FT240X, FT600, FT601")
    class UsbFIFOTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT240XS",
            "FT240XQ"
        })
        void shouldDetectFT240XSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "FT600",
            "FT600BL",
            "FT600BQ",
            "FT601",
            "FT601BL",
            "FT601BQ"
        })
        void shouldDetectUSB3FIFOSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForFIFO() {
            assertEquals("FT240X", handler.extractSeries("FT240XS"));
            assertEquals("FT600", handler.extractSeries("FT600BL"));
            assertEquals("FT601", handler.extractSeries("FT601BQ"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("FIFO", handler.getInterfaceType("FT240XS"));
            assertEquals("FIFO", handler.getInterfaceType("FT600"));
            assertEquals("FIFO", handler.getInterfaceType("FT601"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0 Full-Speed", handler.getUsbVersion("FT240XS"));
            assertEquals("USB 3.0 Super-Speed", handler.getUsbVersion("FT600"));
            assertEquals("USB 3.0 Super-Speed", handler.getUsbVersion("FT601"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "FT232RL, SSOP",
            "FT232RQ, QFN",
            "FT2232H, LQFP",
            "FT2232D, LQFP",
            "FT4232H, LQFP",
            "FT230XS, SSOP",
            "FT230XQ, QFN",
            "FT260S, SSOP",
            "FT260Q, QFN",
            "FT600BL, LQFP",
            "FT600BQ, QFN",
            "FT601BL, LQFP"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        void shouldHandleReelSuffix() {
            assertEquals("SSOP", handler.extractPackageCode("FT232RL-REEL"));
            assertEquals("LQFP", handler.extractPackageCode("FT2232H-REEL"));
            assertEquals("LQFP", handler.extractPackageCode("FT4232H-REEL"));
        }

        @Test
        void shouldReturnEmptyForUnknownPackage() {
            assertEquals("", handler.extractPackageCode("FT232"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "FT232R, FT232",
            "FT232RL, FT232",
            "FT232RQ, FT232",
            "FT232RL-REEL, FT232",
            "FT2232D, FT2232",
            "FT2232H, FT2232",
            "FT4232H, FT4232",
            "FT230XS, FT230X",
            "FT231XS, FT231X",
            "FT234XD, FT234X",
            "FT200XD, FT200X",
            "FT201XS, FT201X",
            "FT220XS, FT220X",
            "FT221XS, FT221X",
            "FT240XS, FT240X",
            "FT260S, FT260",
            "FT600BL, FT600",
            "FT601BQ, FT601"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonFTDIParts() {
            assertEquals("", handler.extractSeries("CP2102"));
            assertEquals("", handler.extractSeries("CH340G"));
            assertEquals("", handler.extractSeries("INVALID"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {
        @Test
        void shouldRecognizeSameSeriesAsReplacement() {
            // Same series, different package
            assertTrue(handler.isOfficialReplacement("FT232RL", "FT232RQ"));
            assertTrue(handler.isOfficialReplacement("FT2232D", "FT2232H"));
            assertTrue(handler.isOfficialReplacement("FT230XS", "FT230XQ"));
        }

        @Test
        void shouldNotReplaceAcrossSeriesWithDifferentChannelCount() {
            // Different channel counts are not interchangeable
            assertFalse(handler.isOfficialReplacement("FT232RL", "FT2232H"));
            assertFalse(handler.isOfficialReplacement("FT2232H", "FT4232H"));
        }

        @Test
        void shouldNotReplaceAcrossDifferentInterfaces() {
            // Different interface types are not interchangeable
            assertFalse(handler.isOfficialReplacement("FT232RL", "FT220XS"));  // UART vs SPI
            assertFalse(handler.isOfficialReplacement("FT232RL", "FT200XD"));  // UART vs I2C
        }

        @Test
        void shouldHandleXSeriesUpgrades() {
            // FT234X (more pins) can replace FT231X or FT230X
            assertTrue(handler.isOfficialReplacement("FT234XD", "FT231XS"));
            assertTrue(handler.isOfficialReplacement("FT234XD", "FT230XS"));
            assertTrue(handler.isOfficialReplacement("FT231XS", "FT230XS"));
        }

        @Test
        void shouldNotAllowXSeriesDowngrade() {
            // FT230X cannot replace FT231X (fewer control lines)
            assertFalse(handler.isOfficialReplacement("FT230XS", "FT231XS"));
            assertFalse(handler.isOfficialReplacement("FT230XS", "FT234XD"));
        }

        @Test
        void shouldNotReplaceUSB2WithUSB3() {
            // FT600/FT601 are not interchangeable with USB 2.0 parts
            assertFalse(handler.isOfficialReplacement("FT232RL", "FT600BL"));
            assertFalse(handler.isOfficialReplacement("FT240XS", "FT600BL"));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.getInterfaceType(null));
            assertEquals(0, handler.getChannelCount(null));
            assertEquals("", handler.getUsbVersion(null));
            assertFalse(handler.isOfficialReplacement(null, "FT232RL"));
            assertFalse(handler.isOfficialReplacement("FT232RL", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getInterfaceType(""));
            assertEquals(0, handler.getChannelCount(""));
            assertEquals("", handler.getUsbVersion(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("FT232RL", null, registry));
        }

        @Test
        void shouldOnlyMatchICType() {
            // FTDI parts should only match IC type, not other types
            assertTrue(handler.matches("FT232RL", ComponentType.IC, registry));
            assertFalse(handler.matches("FT232RL", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("FT232RL", ComponentType.MEMORY, registry));
        }

        @Test
        void shouldHandleCaseInsensitivity() {
            assertTrue(handler.matches("ft232rl", ComponentType.IC, registry));
            assertTrue(handler.matches("FT232RL", ComponentType.IC, registry));
            assertTrue(handler.matches("Ft232Rl", ComponentType.IC, registry));
        }

        @Test
        void shouldReturnEmptyForInvalidMpn() {
            assertEquals("", handler.extractPackageCode("INVALID-MPN"));
            assertEquals("", handler.extractSeries("INVALID-MPN"));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetInterfaceType() {
            assertEquals("UART", handler.getInterfaceType("FT232RL"));
            assertEquals("UART", handler.getInterfaceType("FT2232H"));
            assertEquals("UART", handler.getInterfaceType("FT4232H"));
            assertEquals("UART", handler.getInterfaceType("FT230XS"));
            assertEquals("I2C", handler.getInterfaceType("FT200XD"));
            assertEquals("I2C", handler.getInterfaceType("FT201XS"));
            assertEquals("I2C/UART", handler.getInterfaceType("FT260S"));
            assertEquals("SPI", handler.getInterfaceType("FT220XS"));
            assertEquals("SPI", handler.getInterfaceType("FT221XS"));
            assertEquals("FIFO", handler.getInterfaceType("FT240XS"));
            assertEquals("FIFO", handler.getInterfaceType("FT600BL"));
            assertEquals("FIFO", handler.getInterfaceType("FT601BQ"));
        }

        @Test
        void shouldGetChannelCount() {
            assertEquals(1, handler.getChannelCount("FT232RL"));
            assertEquals(2, handler.getChannelCount("FT2232H"));
            assertEquals(4, handler.getChannelCount("FT4232H"));
            assertEquals(1, handler.getChannelCount("FT230XS"));
            assertEquals(1, handler.getChannelCount("FT260S"));
            assertEquals(2, handler.getChannelCount("FT600BL"));
            assertEquals(1, handler.getChannelCount("FT601BQ"));
        }

        @Test
        void shouldGetUsbVersion() {
            assertEquals("USB 2.0 Full-Speed", handler.getUsbVersion("FT232RL"));
            assertEquals("USB 2.0 High-Speed", handler.getUsbVersion("FT2232H"));
            assertEquals("USB 2.0 High-Speed", handler.getUsbVersion("FT4232H"));
            assertEquals("USB 2.0 Full-Speed", handler.getUsbVersion("FT230XS"));
            assertEquals("USB 3.0 Super-Speed", handler.getUsbVersion("FT600BL"));
            assertEquals("USB 3.0 Super-Speed", handler.getUsbVersion("FT601BQ"));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldReturnCorrectSupportedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC));
            assertEquals(1, types.size());
        }

        @Test
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.MEMORY));
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {
        @Test
        void documentProductFamilies() {
            System.out.println("FTDI Product Families:");
            System.out.println("FT232R/RL/RQ = Single channel USB to UART (Full-Speed)");
            System.out.println("FT2232D/H = Dual channel USB to UART/FIFO/SPI/JTAG (High-Speed)");
            System.out.println("FT4232H = Quad channel USB to UART/SPI (High-Speed)");
            System.out.println("FT230X/FT231X/FT234X = X-Series USB to UART (Full-Speed)");
            System.out.println("FT200XD/FT201X = USB to I2C (Full-Speed)");
            System.out.println("FT220X/FT221X = USB to SPI/FT1248 (Full-Speed)");
            System.out.println("FT240X = USB to 8-bit FIFO (Full-Speed)");
            System.out.println("FT260 = USB to I2C/UART HID-class (Full-Speed)");
            System.out.println("FT600/FT601 = USB 3.0 to 16/32-bit FIFO (Super-Speed)");
            System.out.println("FT51A = Programmable USB MCU");
        }

        @Test
        void documentPackageCodes() {
            System.out.println("FTDI Package Codes:");
            System.out.println("R = SSOP-28 (FT232R)");
            System.out.println("RL = SSOP-28 (FT232RL)");
            System.out.println("RQ = QFN-32 (FT232RQ)");
            System.out.println("S = SSOP (X-series)");
            System.out.println("Q = QFN (X-series)");
            System.out.println("H = LQFP-48 (FT2232H/FT4232H)");
            System.out.println("D = LQFP-48 (FT2232D)");
            System.out.println("BL = LQFP-76 (FT600/FT601)");
            System.out.println("BQ = QFN-56 (FT600/FT601)");
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldPartNumberTests {
        @ParameterizedTest
        @ValueSource(strings = {
            // Common USB to serial converters
            "FT232RL",
            "FT232RQ",
            "FT232RL-REEL",
            "FT2232H",
            "FT2232H-REEL",
            "FT4232H-REEL",
            // X-series
            "FT230XS-R",
            "FT231XQ-R",
            "FT234XD-R",
            // USB 3.0
            "FT600BL",
            "FT600BQ",
            "FT601BL-REEL"
        })
        void shouldRecognizeRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should recognize real-world part number: " + mpn);
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series from: " + mpn);
        }
    }
}
