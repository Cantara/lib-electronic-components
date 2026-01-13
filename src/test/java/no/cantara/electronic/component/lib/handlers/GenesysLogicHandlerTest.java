package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.GenesysLogicHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for GenesysLogicHandler.
 * Tests USB hubs, card readers, and USB PD controllers from Genesys Logic.
 */
class GenesysLogicHandlerTest {

    private static GenesysLogicHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new GenesysLogicHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("USB 2.0 Hub Detection - GL85xx Series")
    class GL85xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GL850G",
            "GL850G-HHY48",
            "GL850G-REEL",
            "GL852G",
            "GL852G-HHY48",
            "GL852G-REEL"
        })
        void shouldDetectGL85xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForGL85xx() {
            assertEquals("GL850", handler.extractSeries("GL850G"));
            assertEquals("GL850", handler.extractSeries("GL850G-HHY48"));
            assertEquals("GL852", handler.extractSeries("GL852G"));
            assertEquals("GL852", handler.extractSeries("GL852G-REEL"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("USB 2.0 Hub", handler.getProductType("GL850G"));
            assertEquals("USB 2.0 Hub", handler.getProductType("GL852G"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("GL850G"));
            assertEquals("USB 2.0", handler.getUsbVersion("GL852G"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(4, handler.getPortCount("GL850G"));
            assertEquals(4, handler.getPortCount("GL852G"));
        }
    }

    @Nested
    @DisplayName("USB 3.0 Hub Detection - GL35xx Series")
    class GL35xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GL3510",
            "GL3510-QFN48",
            "GL3510-REEL",
            "GL3520",
            "GL3520-QFN64",
            "GL3520-REEL",
            "GL3523",
            "GL3523-QFN88",
            "GL3523-REEL",
            "GL3590",
            "GL3590-QFN"
        })
        void shouldDetectGL35xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForGL35xx() {
            assertEquals("GL3510", handler.extractSeries("GL3510"));
            assertEquals("GL3510", handler.extractSeries("GL3510-QFN48"));
            assertEquals("GL3520", handler.extractSeries("GL3520"));
            assertEquals("GL3520", handler.extractSeries("GL3520-QFN64"));
            assertEquals("GL3523", handler.extractSeries("GL3523"));
            assertEquals("GL3523", handler.extractSeries("GL3523-QFN88"));
            assertEquals("GL3590", handler.extractSeries("GL3590"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("USB 3.x Hub", handler.getProductType("GL3510"));
            assertEquals("USB 3.x Hub", handler.getProductType("GL3520"));
            assertEquals("USB 3.x Hub", handler.getProductType("GL3523"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.0", handler.getUsbVersion("GL3510"));
            assertEquals("USB 3.0", handler.getUsbVersion("GL3520"));
            assertEquals("USB 3.1 Gen 1", handler.getUsbVersion("GL3523"));
            assertEquals("USB 3.1 Gen 1", handler.getUsbVersion("GL3590"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(4, handler.getPortCount("GL3510"));
            assertEquals(4, handler.getPortCount("GL3520"));
            assertEquals(7, handler.getPortCount("GL3523"));
            assertEquals(4, handler.getPortCount("GL3590"));
        }

        @Test
        void shouldDetectMTTSupport() {
            assertFalse(handler.supportsMTT("GL3510"));
            assertTrue(handler.supportsMTT("GL3520"));
            assertTrue(handler.supportsMTT("GL3523"));
        }
    }

    @Nested
    @DisplayName("Card Reader Detection - GL32xx Series")
    class GL32xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GL3220",
            "GL3220-QFN",
            "GL3220-REEL",
            "GL3224",
            "GL3224-QFN48",
            "GL3227",
            "GL3227-QFN"
        })
        void shouldDetectGL32xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForGL32xx() {
            assertEquals("GL3220", handler.extractSeries("GL3220"));
            assertEquals("GL3224", handler.extractSeries("GL3224"));
            assertEquals("GL3227", handler.extractSeries("GL3227"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("Card Reader", handler.getProductType("GL3220"));
            assertEquals("Card Reader", handler.getProductType("GL3224"));
            assertEquals("Card Reader", handler.getProductType("GL3227"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("GL3220"));
            assertEquals("USB 2.0", handler.getUsbVersion("GL3224"));
            assertEquals("USB 2.0", handler.getUsbVersion("GL3227"));
        }
    }

    @Nested
    @DisplayName("USB 3.0 Card Reader Detection - GL33xx Series")
    class GL33xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GL3310",
            "GL3310-QFN",
            "GL3321",
            "GL3321-QFN48"
        })
        void shouldDetectGL33xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForGL33xx() {
            assertEquals("GL3310", handler.extractSeries("GL3310"));
            assertEquals("GL3321", handler.extractSeries("GL3321"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("Card Reader", handler.getProductType("GL3310"));
            assertEquals("Card Reader", handler.getProductType("GL3321"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.0", handler.getUsbVersion("GL3310"));
            assertEquals("USB 3.0", handler.getUsbVersion("GL3321"));
        }
    }

    @Nested
    @DisplayName("USB PD Controller Detection - GL98xx Series")
    class GL98xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GL9801",
            "GL9801-QFN",
            "GL9802",
            "GL9802-REEL"
        })
        void shouldDetectGL98xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForGL98xx() {
            assertEquals("GL9801", handler.extractSeries("GL9801"));
            assertEquals("GL9802", handler.extractSeries("GL9802"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("USB PD Controller", handler.getProductType("GL9801"));
            assertEquals("USB PD Controller", handler.getProductType("GL9802"));
        }
    }

    @Nested
    @DisplayName("USB 3.1 Gen 2 Hub Detection - GL36xx Series")
    class GL36xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GL3610",
            "GL3610-QFN",
            "GL3620",
            "GL3620-QFN64"
        })
        void shouldDetectGL36xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForGL36xx() {
            assertEquals("GL3610", handler.extractSeries("GL3610"));
            assertEquals("GL3620", handler.extractSeries("GL3620"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("USB 3.x Hub", handler.getProductType("GL3610"));
            assertEquals("USB 3.x Hub", handler.getProductType("GL3620"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.1 Gen 2", handler.getUsbVersion("GL3610"));
            assertEquals("USB 3.1 Gen 2", handler.getUsbVersion("GL3620"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "GL850G, LQFP",
            "GL852G, LQFP",
            "GL3523-QFN88, QFN",
            "GL3520-QFN64, QFN",
            "GL3510-LQFP, LQFP"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        void shouldHandleReelSuffix() {
            assertEquals("LQFP", handler.extractPackageCode("GL850G-REEL"));
        }

        @Test
        void shouldReturnEmptyForUnknownPackage() {
            assertEquals("", handler.extractPackageCode("GL3510"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "GL850G, GL850",
            "GL850G-HHY48, GL850",
            "GL852G, GL852",
            "GL852G-REEL, GL852",
            "GL3510, GL3510",
            "GL3510-QFN48, GL3510",
            "GL3520, GL3520",
            "GL3523-QFN88, GL3523",
            "GL3220, GL3220",
            "GL3224, GL3224",
            "GL9801, GL9801",
            "GL9802-QFN, GL9802"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonGenesysLogicParts() {
            assertEquals("", handler.extractSeries("FT232RL"));
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
            assertTrue(handler.isOfficialReplacement("GL850G", "GL850G-HHY48"));
            assertTrue(handler.isOfficialReplacement("GL3520", "GL3520-QFN64"));
        }

        @Test
        void shouldRecognizeGL852AsReplacementForGL850() {
            // GL852 is an improved version of GL850
            assertTrue(handler.isOfficialReplacement("GL852G", "GL850G"));
            assertTrue(handler.isOfficialReplacement("GL850G", "GL852G"));
        }

        @Test
        void shouldRecognizeGL3520AsReplacementForGL3510() {
            // GL3520 has MTT, GL3510 doesn't, but they're compatible
            assertTrue(handler.isOfficialReplacement("GL3520", "GL3510"));
            assertTrue(handler.isOfficialReplacement("GL3510", "GL3520"));
        }

        @Test
        void shouldNotReplaceAcrossDifferentProductTypes() {
            // Hubs and card readers are not interchangeable
            assertFalse(handler.isOfficialReplacement("GL3520", "GL3220"));
            assertFalse(handler.isOfficialReplacement("GL850G", "GL3220"));
        }

        @Test
        void shouldNotReplaceAcrossUsbVersions() {
            // USB 2.0 and USB 3.0 hubs are not interchangeable
            assertFalse(handler.isOfficialReplacement("GL850G", "GL3510"));
            assertFalse(handler.isOfficialReplacement("GL852G", "GL3520"));
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
            assertEquals("", handler.getProductType(null));
            assertEquals("", handler.getUsbVersion(null));
            assertEquals(0, handler.getPortCount(null));
            assertFalse(handler.supportsMTT(null));
            assertFalse(handler.isOfficialReplacement(null, "GL850G"));
            assertFalse(handler.isOfficialReplacement("GL850G", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getProductType(""));
            assertEquals("", handler.getUsbVersion(""));
            assertEquals(0, handler.getPortCount(""));
            assertFalse(handler.supportsMTT(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("GL850G", null, registry));
        }

        @Test
        void shouldOnlyMatchICType() {
            // Genesys Logic parts should only match IC type
            assertTrue(handler.matches("GL850G", ComponentType.IC, registry));
            assertFalse(handler.matches("GL850G", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("GL850G", ComponentType.MEMORY, registry));
            assertFalse(handler.matches("GL850G", ComponentType.CONNECTOR, registry));
        }

        @Test
        void shouldHandleCaseInsensitivity() {
            assertTrue(handler.matches("gl850g", ComponentType.IC, registry));
            assertTrue(handler.matches("GL850G", ComponentType.IC, registry));
            assertTrue(handler.matches("Gl850g", ComponentType.IC, registry));
        }

        @Test
        void shouldReturnEmptyForInvalidMpn() {
            assertEquals("", handler.extractPackageCode("INVALID-MPN"));
            assertEquals("", handler.extractSeries("INVALID-MPN"));
            assertEquals("", handler.getProductType("INVALID-MPN"));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetProductType() {
            assertEquals("USB 2.0 Hub", handler.getProductType("GL850G"));
            assertEquals("USB 2.0 Hub", handler.getProductType("GL852G"));
            assertEquals("USB 3.x Hub", handler.getProductType("GL3510"));
            assertEquals("USB 3.x Hub", handler.getProductType("GL3520"));
            assertEquals("USB 3.x Hub", handler.getProductType("GL3523"));
            assertEquals("Card Reader", handler.getProductType("GL3220"));
            assertEquals("Card Reader", handler.getProductType("GL3224"));
            assertEquals("Card Reader", handler.getProductType("GL3310"));
            assertEquals("USB PD Controller", handler.getProductType("GL9801"));
        }

        @Test
        void shouldGetUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("GL850G"));
            assertEquals("USB 2.0", handler.getUsbVersion("GL852G"));
            assertEquals("USB 3.0", handler.getUsbVersion("GL3510"));
            assertEquals("USB 3.0", handler.getUsbVersion("GL3520"));
            assertEquals("USB 3.1 Gen 1", handler.getUsbVersion("GL3523"));
            assertEquals("USB 3.1 Gen 2", handler.getUsbVersion("GL3610"));
        }

        @Test
        void shouldGetPortCount() {
            assertEquals(4, handler.getPortCount("GL850G"));
            assertEquals(4, handler.getPortCount("GL852G"));
            assertEquals(4, handler.getPortCount("GL3510"));
            assertEquals(4, handler.getPortCount("GL3520"));
            assertEquals(7, handler.getPortCount("GL3523"));
            assertEquals(0, handler.getPortCount("GL3220")); // Card readers don't have ports
        }

        @Test
        void shouldDetectMTTSupport() {
            assertFalse(handler.supportsMTT("GL850G"));
            assertFalse(handler.supportsMTT("GL3510"));
            assertTrue(handler.supportsMTT("GL3520"));
            assertTrue(handler.supportsMTT("GL3523"));
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
            System.out.println("Genesys Logic Product Families:");
            System.out.println("GL850/GL852 = USB 2.0 4-port hub");
            System.out.println("GL3510 = USB 3.0 4-port hub");
            System.out.println("GL3520 = USB 3.0 4-port hub with MTT");
            System.out.println("GL3523 = USB 3.1 Gen 1 7-port hub with MTT");
            System.out.println("GL3590 = USB 3.1 Gen 1 4-port hub");
            System.out.println("GL36xx = USB 3.1 Gen 2 hubs");
            System.out.println("GL3220/GL3224/GL3227 = USB 2.0 card readers");
            System.out.println("GL33xx = USB 3.0 card readers");
            System.out.println("GL98xx = USB PD controllers");
        }

        @Test
        void documentPackageCodes() {
            System.out.println("Genesys Logic Package Codes:");
            System.out.println("G = LQFP (e.g., GL850G)");
            System.out.println("Q = QFN");
            System.out.println("S = SSOP");
            System.out.println("-QFNxx = QFN with pin count (e.g., GL3523-QFN88)");
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldPartNumberTests {
        @ParameterizedTest
        @ValueSource(strings = {
            // USB 2.0 hubs
            "GL850G",
            "GL850G-HHY48",
            "GL852G",
            // USB 3.0 hubs
            "GL3510",
            "GL3520",
            "GL3523",
            // Card readers
            "GL3220",
            "GL3224",
            // USB PD controllers
            "GL9801",
            "GL9802"
        })
        void shouldRecognizeRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should recognize real-world part number: " + mpn);
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series from: " + mpn);
        }
    }

    @Nested
    @DisplayName("Non-Matching Part Numbers")
    class NonMatchingTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FT232RL",      // FTDI
            "CH340G",       // WCH
            "CP2102",       // Silicon Labs
            "TUSB8041",     // TI USB hub
            "USB2514B",     // Microchip USB hub
            "INVALID",
            "GL1234"        // Invalid Genesys Logic prefix
        })
        void shouldNotMatchNonGenesysLogicParts(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    "Should not match non-Genesys Logic part: " + mpn);
        }
    }
}
