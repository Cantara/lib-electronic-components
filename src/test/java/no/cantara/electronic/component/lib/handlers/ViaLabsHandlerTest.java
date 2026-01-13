package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ViaLabsHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ViaLabsHandler.
 * Tests USB hub controllers and USB Power Delivery controllers.
 */
class ViaLabsHandlerTest {

    private static ViaLabsHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ViaLabsHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("USB 2.0 Hub Detection - VL7xx Series")
    class VL7xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "VL750",
            "VL750Q",
            "VL751",
            "VL751Q",
            "VL752",
            "VL752Q",
            "VL750-Q7",
            "VL751-Q7"
        })
        void shouldDetectVL7xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForVL7xx() {
            assertEquals("VL750", handler.extractSeries("VL750"));
            assertEquals("VL750", handler.extractSeries("VL750Q"));
            assertEquals("VL751", handler.extractSeries("VL751"));
            assertEquals("VL751", handler.extractSeries("VL751Q"));
            assertEquals("VL752", handler.extractSeries("VL752"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("VL750"));
            assertEquals("USB 2.0", handler.getUsbVersion("VL751"));
            assertEquals("USB 2.0", handler.getUsbVersion("VL752"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("Hub Controller", handler.getProductType("VL750"));
            assertEquals("Hub Controller", handler.getProductType("VL751"));
            assertEquals("Hub Controller", handler.getProductType("VL752"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(4, handler.getPortCount("VL750"));
            assertEquals(4, handler.getPortCount("VL751"));
            assertEquals(7, handler.getPortCount("VL752"));
        }
    }

    @Nested
    @DisplayName("USB 3.0 Hub Detection - VL8xx Series")
    class VL8xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "VL812",
            "VL812-Q7",
            "VL812Q",
            "VL813",
            "VL813-Q7",
            "VL815",
            "VL815-Q7",
            "VL817",
            "VL817-Q7",
            "VL817Q"
        })
        void shouldDetectVL8xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForVL8xx() {
            assertEquals("VL812", handler.extractSeries("VL812"));
            assertEquals("VL812", handler.extractSeries("VL812-Q7"));
            assertEquals("VL813", handler.extractSeries("VL813"));
            assertEquals("VL815", handler.extractSeries("VL815"));
            assertEquals("VL817", handler.extractSeries("VL817"));
            assertEquals("VL817", handler.extractSeries("VL817Q"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.0", handler.getUsbVersion("VL812"));
            assertEquals("USB 3.0", handler.getUsbVersion("VL813"));
            assertEquals("USB 3.0", handler.getUsbVersion("VL815"));
            assertEquals("USB 3.0", handler.getUsbVersion("VL817"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("Hub Controller", handler.getProductType("VL812"));
            assertEquals("Hub Controller", handler.getProductType("VL817"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(4, handler.getPortCount("VL812"));
            assertEquals(4, handler.getPortCount("VL813"));
            assertEquals(4, handler.getPortCount("VL815"));
            assertEquals(4, handler.getPortCount("VL817"));
        }
    }

    @Nested
    @DisplayName("USB 3.2 Hub Detection - VL82x Series")
    class VL82xSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "VL820",
            "VL820-Q7",
            "VL822",
            "VL822-Q7",
            "VL822Q",
            "VL823",
            "VL823-Q7"
        })
        void shouldDetectVL82xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForVL82x() {
            assertEquals("VL820", handler.extractSeries("VL820"));
            assertEquals("VL822", handler.extractSeries("VL822"));
            assertEquals("VL822", handler.extractSeries("VL822-Q7"));
            assertEquals("VL823", handler.extractSeries("VL823"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.2", handler.getUsbVersion("VL820"));
            assertEquals("USB 3.2", handler.getUsbVersion("VL822"));
            assertEquals("USB 3.2", handler.getUsbVersion("VL823"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("Hub Controller", handler.getProductType("VL820"));
            assertEquals("Hub Controller", handler.getProductType("VL822"));
            assertEquals("Hub Controller", handler.getProductType("VL823"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(4, handler.getPortCount("VL820"));
            assertEquals(4, handler.getPortCount("VL822"));
            assertEquals(4, handler.getPortCount("VL823"));
        }
    }

    @Nested
    @DisplayName("USB Power Delivery Detection - VL10x Series")
    class VL10xSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "VL100",
            "VL100Q",
            "VL100-Q7",
            "VL102",
            "VL102Q",
            "VL103",
            "VL103Q"
        })
        void shouldDetectVL10xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForVL10x() {
            assertEquals("VL100", handler.extractSeries("VL100"));
            assertEquals("VL100", handler.extractSeries("VL100Q"));
            assertEquals("VL102", handler.extractSeries("VL102"));
            assertEquals("VL103", handler.extractSeries("VL103"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB PD", handler.getUsbVersion("VL100"));
            assertEquals("USB PD", handler.getUsbVersion("VL102"));
            assertEquals("USB PD", handler.getUsbVersion("VL103"));
        }

        @Test
        void shouldHaveCorrectProductType() {
            assertEquals("PD Controller", handler.getProductType("VL100"));
            assertEquals("PD Controller", handler.getProductType("VL102"));
            assertEquals("PD Controller", handler.getProductType("VL103"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "VL817-Q7, QFN",
            "VL817Q, QFN",
            "VL822-Q7, QFN",
            "VL822Q, QFN",
            "VL817-L, LQFP",
            "VL817L, LQFP"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        void shouldHandleReelSuffix() {
            assertEquals("QFN", handler.extractPackageCode("VL817-Q7-REEL"));
            assertEquals("QFN", handler.extractPackageCode("VL822Q-TR"));
        }

        @Test
        void shouldReturnEmptyForUnknownPackage() {
            assertEquals("", handler.extractPackageCode("VL817"));
            assertEquals("", handler.extractPackageCode("VL822"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "VL750, VL750",
            "VL750Q, VL750",
            "VL751, VL751",
            "VL752, VL752",
            "VL812, VL812",
            "VL812-Q7, VL812",
            "VL813, VL813",
            "VL815, VL815",
            "VL817, VL817",
            "VL817Q, VL817",
            "VL820, VL820",
            "VL822, VL822",
            "VL822-Q7, VL822",
            "VL823, VL823",
            "VL100, VL100",
            "VL102, VL102",
            "VL103, VL103"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonViaLabsParts() {
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
            assertTrue(handler.isOfficialReplacement("VL817Q", "VL817-Q7"));
            assertTrue(handler.isOfficialReplacement("VL822Q", "VL822-Q7"));
            assertTrue(handler.isOfficialReplacement("VL750Q", "VL750"));
        }

        @Test
        void shouldAllowUsb30HubUpgrades() {
            // VL817 can replace older VL8xx USB 3.0 hubs
            assertTrue(handler.isOfficialReplacement("VL817", "VL812"));
            assertTrue(handler.isOfficialReplacement("VL817", "VL813"));
            assertTrue(handler.isOfficialReplacement("VL815", "VL812"));
        }

        @Test
        void shouldNotAllowUsb30HubDowngrades() {
            // Older hubs cannot replace newer ones
            assertFalse(handler.isOfficialReplacement("VL812", "VL817"));
            assertFalse(handler.isOfficialReplacement("VL813", "VL815"));
        }

        @Test
        void shouldAllowUsb32HubUpgrades() {
            // VL823 can replace older VL82x USB 3.2 hubs
            assertTrue(handler.isOfficialReplacement("VL823", "VL822"));
            assertTrue(handler.isOfficialReplacement("VL823", "VL820"));
            assertTrue(handler.isOfficialReplacement("VL822", "VL820"));
        }

        @Test
        void shouldNotAllowUsb32HubDowngrades() {
            // Older hubs cannot replace newer ones
            assertFalse(handler.isOfficialReplacement("VL820", "VL822"));
            assertFalse(handler.isOfficialReplacement("VL820", "VL823"));
        }

        @Test
        void shouldAllowUsb20HubInterchange() {
            // USB 2.0 hubs are generally interchangeable
            assertTrue(handler.isOfficialReplacement("VL750", "VL751"));
            assertTrue(handler.isOfficialReplacement("VL751", "VL750"));
        }

        @Test
        void shouldNotReplaceAcrossUsbVersions() {
            // Different USB versions are not interchangeable
            assertFalse(handler.isOfficialReplacement("VL750", "VL812"));  // USB 2.0 vs 3.0
            assertFalse(handler.isOfficialReplacement("VL817", "VL822"));  // USB 3.0 vs 3.2
            assertFalse(handler.isOfficialReplacement("VL817", "VL100"));  // Hub vs PD
        }

        @Test
        void shouldNotReplacePdControllersWithHubs() {
            // PD controllers are not interchangeable with hub controllers
            assertFalse(handler.isOfficialReplacement("VL817", "VL100"));
            assertFalse(handler.isOfficialReplacement("VL100", "VL817"));
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
            assertEquals("", handler.getUsbVersion(null));
            assertEquals("", handler.getProductType(null));
            assertEquals(0, handler.getPortCount(null));
            assertFalse(handler.isOfficialReplacement(null, "VL817"));
            assertFalse(handler.isOfficialReplacement("VL817", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getUsbVersion(""));
            assertEquals("", handler.getProductType(""));
            assertEquals(0, handler.getPortCount(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("VL817", null, registry));
        }

        @Test
        void shouldOnlyMatchICType() {
            // VIA Labs parts should only match IC type, not other types
            assertTrue(handler.matches("VL817", ComponentType.IC, registry));
            assertFalse(handler.matches("VL817", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("VL817", ComponentType.MEMORY, registry));
        }

        @Test
        void shouldHandleCaseInsensitivity() {
            assertTrue(handler.matches("vl817", ComponentType.IC, registry));
            assertTrue(handler.matches("VL817", ComponentType.IC, registry));
            assertTrue(handler.matches("Vl817", ComponentType.IC, registry));
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
        void shouldGetUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("VL750"));
            assertEquals("USB 2.0", handler.getUsbVersion("VL751"));
            assertEquals("USB 2.0", handler.getUsbVersion("VL752"));
            assertEquals("USB 3.0", handler.getUsbVersion("VL812"));
            assertEquals("USB 3.0", handler.getUsbVersion("VL813"));
            assertEquals("USB 3.0", handler.getUsbVersion("VL815"));
            assertEquals("USB 3.0", handler.getUsbVersion("VL817"));
            assertEquals("USB 3.2", handler.getUsbVersion("VL820"));
            assertEquals("USB 3.2", handler.getUsbVersion("VL822"));
            assertEquals("USB 3.2", handler.getUsbVersion("VL823"));
            assertEquals("USB PD", handler.getUsbVersion("VL100"));
            assertEquals("USB PD", handler.getUsbVersion("VL102"));
            assertEquals("USB PD", handler.getUsbVersion("VL103"));
        }

        @Test
        void shouldGetProductType() {
            assertEquals("Hub Controller", handler.getProductType("VL750"));
            assertEquals("Hub Controller", handler.getProductType("VL817"));
            assertEquals("Hub Controller", handler.getProductType("VL822"));
            assertEquals("PD Controller", handler.getProductType("VL100"));
            assertEquals("PD Controller", handler.getProductType("VL103"));
        }

        @Test
        void shouldGetPortCount() {
            assertEquals(4, handler.getPortCount("VL750"));
            assertEquals(4, handler.getPortCount("VL751"));
            assertEquals(7, handler.getPortCount("VL752"));
            assertEquals(4, handler.getPortCount("VL812"));
            assertEquals(4, handler.getPortCount("VL817"));
            assertEquals(4, handler.getPortCount("VL822"));
            assertEquals(4, handler.getPortCount("VL823"));
            assertEquals(0, handler.getPortCount("VL100"));  // PD controllers don't have ports
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
            System.out.println("VIA Labs Product Families:");
            System.out.println("VL7xx = USB 2.0 Hub Controllers");
            System.out.println("  VL750 = 4-port USB 2.0 hub");
            System.out.println("  VL751 = 4-port USB 2.0 hub (enhanced)");
            System.out.println("  VL752 = 7-port USB 2.0 hub");
            System.out.println("VL8xx = USB 3.0 Hub Controllers");
            System.out.println("  VL812 = First gen USB 3.0 hub");
            System.out.println("  VL813 = Second gen USB 3.0 hub");
            System.out.println("  VL815 = Third gen USB 3.0 hub");
            System.out.println("  VL817 = Latest gen USB 3.0 hub (most common)");
            System.out.println("VL82x = USB 3.2 Hub Controllers");
            System.out.println("  VL820 = First gen USB 3.2 hub");
            System.out.println("  VL822 = Second gen USB 3.2 hub");
            System.out.println("  VL823 = Latest gen USB 3.2 hub");
            System.out.println("VL10x = USB Power Delivery Controllers");
            System.out.println("  VL100 = USB PD controller");
            System.out.println("  VL102 = USB PD controller (enhanced)");
            System.out.println("  VL103 = USB PD controller (latest)");
        }

        @Test
        void documentPackageCodes() {
            System.out.println("VIA Labs Package Codes:");
            System.out.println("Q = QFN package");
            System.out.println("L = LQFP package");
            System.out.println("B = BGA package");
            System.out.println("T = TQFP package");
            System.out.println("-Q7 = QFN-7x7 package suffix");
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldPartNumberTests {
        @ParameterizedTest
        @ValueSource(strings = {
            // USB 3.0 Hub Controllers (most common VIA Labs parts)
            "VL812",
            "VL812-Q7",
            "VL813",
            "VL817",
            "VL817-Q7",
            // USB 3.2 Hub Controllers
            "VL822",
            "VL822-Q7",
            "VL823",
            // USB 2.0 Hub Controllers
            "VL750",
            "VL751",
            // USB Power Delivery
            "VL100",
            "VL103"
        })
        void shouldRecognizeRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should recognize real-world part number: " + mpn);
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series from: " + mpn);
        }

        @Test
        void shouldNotMatchNonViaLabsParts() {
            // Common USB controller chips from other manufacturers
            assertFalse(handler.matches("FT232RL", ComponentType.IC, registry));   // FTDI
            assertFalse(handler.matches("CP2102", ComponentType.IC, registry));     // Silicon Labs
            assertFalse(handler.matches("CH340G", ComponentType.IC, registry));     // WCH
            assertFalse(handler.matches("USB2514B", ComponentType.IC, registry));   // Microchip
            assertFalse(handler.matches("TUSB8041", ComponentType.IC, registry));   // TI
        }
    }
}
