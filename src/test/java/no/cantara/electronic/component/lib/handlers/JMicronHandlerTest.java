package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.JMicronHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for JMicronHandler.
 * Tests storage controller ICs: USB-SATA bridges, PCIe SATA/NVMe controllers,
 * SATA port multipliers, and Flash controllers.
 */
class JMicronHandlerTest {

    private static JMicronHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new JMicronHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("USB-SATA Bridge Detection - JMS5xx Series")
    class JMS5xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "JMS539",
            "JMS567",
            "JMS578",
            "JMS583",
            "JMS539-QFN",
            "JMS567-LQFP",
            "JMS578-BGA",
            "JMS583-QFN"
        })
        void shouldDetectJMS5xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForJMS5xx() {
            assertEquals("JMS539", handler.extractSeries("JMS539"));
            assertEquals("JMS567", handler.extractSeries("JMS567"));
            assertEquals("JMS578", handler.extractSeries("JMS578"));
            assertEquals("JMS583", handler.extractSeries("JMS583"));
            assertEquals("JMS583", handler.extractSeries("JMS583-QFN"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB-SATA", handler.getInterfaceType("JMS539"));
            assertEquals("USB-SATA", handler.getInterfaceType("JMS567"));
            assertEquals("USB-SATA", handler.getInterfaceType("JMS578"));
            assertEquals("USB-SATA/NVMe", handler.getInterfaceType("JMS583"));
        }

        @Test
        void shouldHaveCorrectUsbGeneration() {
            assertEquals("USB 3.0", handler.getUsbGeneration("JMS539"));
            assertEquals("USB 3.0", handler.getUsbGeneration("JMS567"));
            assertEquals("USB 3.1 Gen 1", handler.getUsbGeneration("JMS578"));
            assertEquals("USB 3.1 Gen 2", handler.getUsbGeneration("JMS583"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(1, handler.getPortCount("JMS539"));
            assertEquals(1, handler.getPortCount("JMS567"));
            assertEquals(1, handler.getPortCount("JMS578"));
            assertEquals(1, handler.getPortCount("JMS583"));
        }

        @Test
        void shouldDetectUASPSupport() {
            assertFalse(handler.supportsUASP("JMS539"));
            assertTrue(handler.supportsUASP("JMS567"));
            assertTrue(handler.supportsUASP("JMS578"));
            assertTrue(handler.supportsUASP("JMS583"));
        }

        @Test
        void shouldDetectNVMeSupport() {
            assertFalse(handler.supportsNVMe("JMS539"));
            assertFalse(handler.supportsNVMe("JMS567"));
            assertFalse(handler.supportsNVMe("JMS578"));
            assertTrue(handler.supportsNVMe("JMS583"));
        }
    }

    @Nested
    @DisplayName("PCIe SATA Controller Detection - JMB5xx Series")
    class JMB5xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "JMB575",
            "JMB585",
            "JMB575-QFN",
            "JMB585-LQFP",
            "JMB585-BGA"
        })
        void shouldDetectJMB5xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForJMB5xx() {
            assertEquals("JMB575", handler.extractSeries("JMB575"));
            assertEquals("JMB585", handler.extractSeries("JMB585"));
            assertEquals("JMB585", handler.extractSeries("JMB585-QFN"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("PCIe-SATA", handler.getInterfaceType("JMB575"));
            assertEquals("PCIe-SATA", handler.getInterfaceType("JMB585"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(5, handler.getPortCount("JMB575"));
            assertEquals(5, handler.getPortCount("JMB585"));
        }
    }

    @Nested
    @DisplayName("SATA Controller Detection - JMB3xx Series")
    class JMB3xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "JMB363",
            "JMB368",
            "JMB363-QFN",
            "JMB368-LQFP"
        })
        void shouldDetectJMB3xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForJMB3xx() {
            assertEquals("JMB363", handler.extractSeries("JMB363"));
            assertEquals("JMB368", handler.extractSeries("JMB368"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("SATA/PATA", handler.getInterfaceType("JMB363"));
            assertEquals("PATA", handler.getInterfaceType("JMB368"));
        }

        @Test
        void shouldHaveCorrectPortCount() {
            assertEquals(2, handler.getPortCount("JMB363"));
            assertEquals(2, handler.getPortCount("JMB368"));
        }
    }

    @Nested
    @DisplayName("Flash Controller Detection - JMF Series")
    class JMFSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "JMF602",
            "JMF612",
            "JMF667",
            "JMF670H",
            "JMF602-QFN"
        })
        void shouldDetectJMFSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForJMF() {
            assertEquals("JMF602", handler.extractSeries("JMF602"));
            assertEquals("JMF612", handler.extractSeries("JMF612"));
            assertEquals("JMF667", handler.extractSeries("JMF667"));
            assertEquals("JMF670", handler.extractSeries("JMF670H"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("Flash", handler.getInterfaceType("JMF602"));
            assertEquals("Flash", handler.getInterfaceType("JMF667"));
        }
    }

    @Nested
    @DisplayName("Legacy Controller Detection - JM20xxx Series")
    class JM20xxxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "JM20329",
            "JM20330",
            "JM20337"
        })
        void shouldDetectJM20xxxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForJM20xxx() {
            assertEquals("JM20329", handler.extractSeries("JM20329"));
            assertEquals("JM20330", handler.extractSeries("JM20330"));
            assertEquals("JM20337", handler.extractSeries("JM20337"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("IDE/SATA", handler.getInterfaceType("JM20329"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "JMS583-QFN, QFN",
            "JMS567-LQFP, LQFP",
            "JMB585-BGA, BGA",
            "JMS578Q, QFN",
            "JMB575L, LQFP",
            "JMF667B, BGA"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        void shouldHandleReelSuffix() {
            assertEquals("QFN", handler.extractPackageCode("JMS583-QFN-REEL"));
            assertEquals("LQFP", handler.extractPackageCode("JMB585-LQFP-TR"));
        }

        @Test
        void shouldReturnEmptyForUnknownPackage() {
            assertEquals("", handler.extractPackageCode("JMS583"));
            assertEquals("", handler.extractPackageCode("JMB585"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "JMS539, JMS539",
            "JMS567, JMS567",
            "JMS578, JMS578",
            "JMS583, JMS583",
            "JMS583-QFN, JMS583",
            "JMB575, JMB575",
            "JMB585, JMB585",
            "JMB585-BGA, JMB585",
            "JMB363, JMB363",
            "JMB368, JMB368",
            "JMF602, JMF602",
            "JMF667, JMF667",
            "JMF670H, JMF670",
            "JM20329, JM20329",
            "JM20337, JM20337"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonJMicronParts() {
            assertEquals("", handler.extractSeries("ASM1153E"));
            assertEquals("", handler.extractSeries("VL716"));
            assertEquals("", handler.extractSeries("INVALID"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {
        @Test
        void shouldRecognizeSameSeriesAsReplacement() {
            // Same series, different package
            assertTrue(handler.isOfficialReplacement("JMS583-QFN", "JMS583-BGA"));
            assertTrue(handler.isOfficialReplacement("JMB585-LQFP", "JMB585-QFN"));
        }

        @Test
        void shouldAllowUSBSataUpgrades() {
            // Newer USB-SATA bridges can replace older ones
            assertTrue(handler.isOfficialReplacement("JMS567", "JMS539"));
            assertTrue(handler.isOfficialReplacement("JMS578", "JMS567"));
            assertTrue(handler.isOfficialReplacement("JMS583", "JMS578"));
            assertTrue(handler.isOfficialReplacement("JMS583", "JMS539"));
        }

        @Test
        void shouldNotAllowUSBSataDowngrades() {
            // Older USB-SATA bridges cannot replace newer ones
            assertFalse(handler.isOfficialReplacement("JMS539", "JMS567"));
            assertFalse(handler.isOfficialReplacement("JMS567", "JMS578"));
            assertFalse(handler.isOfficialReplacement("JMS578", "JMS583"));
        }

        @Test
        void shouldAllowPCIeSataUpgrade() {
            // JMB585 can replace JMB575
            assertTrue(handler.isOfficialReplacement("JMB585", "JMB575"));
        }

        @Test
        void shouldNotAllowPCIeSataDowngrade() {
            assertFalse(handler.isOfficialReplacement("JMB575", "JMB585"));
        }

        @Test
        void shouldNotReplaceDifferentControllerTypes() {
            // USB-SATA and PCIe-SATA are not interchangeable
            assertFalse(handler.isOfficialReplacement("JMS583", "JMB585"));
            assertFalse(handler.isOfficialReplacement("JMB575", "JMS567"));
        }

        @Test
        void shouldNotReplaceFlashControllers() {
            // Flash controllers are not interchangeable with other types
            assertFalse(handler.isOfficialReplacement("JMF667", "JMS583"));
            assertFalse(handler.isOfficialReplacement("JMS583", "JMF667"));
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
            assertEquals("", handler.getUsbGeneration(null));
            assertEquals(0, handler.getPortCount(null));
            assertFalse(handler.supportsUASP(null));
            assertFalse(handler.supportsNVMe(null));
            assertFalse(handler.isOfficialReplacement(null, "JMS583"));
            assertFalse(handler.isOfficialReplacement("JMS583", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getInterfaceType(""));
            assertEquals("", handler.getUsbGeneration(""));
            assertEquals(0, handler.getPortCount(""));
            assertFalse(handler.supportsUASP(""));
            assertFalse(handler.supportsNVMe(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("JMS583", null, registry));
        }

        @Test
        void shouldOnlyMatchICType() {
            // JMicron parts should only match IC type, not other types
            assertTrue(handler.matches("JMS583", ComponentType.IC, registry));
            assertFalse(handler.matches("JMS583", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("JMS583", ComponentType.MEMORY, registry));
        }

        @Test
        void shouldHandleCaseInsensitivity() {
            assertTrue(handler.matches("jms583", ComponentType.IC, registry));
            assertTrue(handler.matches("JMS583", ComponentType.IC, registry));
            assertTrue(handler.matches("Jms583", ComponentType.IC, registry));
        }

        @Test
        void shouldReturnEmptyForInvalidMpn() {
            assertEquals("", handler.extractPackageCode("INVALID-MPN"));
            assertEquals("", handler.extractSeries("INVALID-MPN"));
            assertEquals("", handler.getInterfaceType("INVALID-MPN"));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetInterfaceType() {
            assertEquals("USB-SATA", handler.getInterfaceType("JMS539"));
            assertEquals("USB-SATA", handler.getInterfaceType("JMS567"));
            assertEquals("USB-SATA", handler.getInterfaceType("JMS578"));
            assertEquals("USB-SATA/NVMe", handler.getInterfaceType("JMS583"));
            assertEquals("PCIe-SATA", handler.getInterfaceType("JMB575"));
            assertEquals("PCIe-SATA", handler.getInterfaceType("JMB585"));
            assertEquals("SATA/PATA", handler.getInterfaceType("JMB363"));
            assertEquals("PATA", handler.getInterfaceType("JMB368"));
            assertEquals("Flash", handler.getInterfaceType("JMF667"));
            assertEquals("IDE/SATA", handler.getInterfaceType("JM20329"));
        }

        @Test
        void shouldGetUsbGeneration() {
            assertEquals("USB 3.0", handler.getUsbGeneration("JMS539"));
            assertEquals("USB 3.0", handler.getUsbGeneration("JMS567"));
            assertEquals("USB 3.1 Gen 1", handler.getUsbGeneration("JMS578"));
            assertEquals("USB 3.1 Gen 2", handler.getUsbGeneration("JMS583"));
            assertEquals("", handler.getUsbGeneration("JMB585"));  // Not a USB controller
        }

        @Test
        void shouldGetPortCount() {
            assertEquals(1, handler.getPortCount("JMS539"));
            assertEquals(1, handler.getPortCount("JMS567"));
            assertEquals(1, handler.getPortCount("JMS578"));
            assertEquals(1, handler.getPortCount("JMS583"));
            assertEquals(5, handler.getPortCount("JMB575"));
            assertEquals(5, handler.getPortCount("JMB585"));
            assertEquals(2, handler.getPortCount("JMB363"));
        }

        @Test
        void shouldDetectUASPSupport() {
            assertFalse(handler.supportsUASP("JMS539"));
            assertTrue(handler.supportsUASP("JMS567"));
            assertTrue(handler.supportsUASP("JMS578"));
            assertTrue(handler.supportsUASP("JMS583"));
            assertFalse(handler.supportsUASP("JMB585"));  // Not a USB bridge
        }

        @Test
        void shouldDetectNVMeSupport() {
            assertFalse(handler.supportsNVMe("JMS539"));
            assertFalse(handler.supportsNVMe("JMS567"));
            assertFalse(handler.supportsNVMe("JMS578"));
            assertTrue(handler.supportsNVMe("JMS583"));
            assertFalse(handler.supportsNVMe("JMB585"));
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
            System.out.println("JMicron Product Families:");
            System.out.println("JMS539 = USB 3.0 to SATA III bridge");
            System.out.println("JMS567 = USB 3.0 to SATA III bridge with UASP");
            System.out.println("JMS578 = USB 3.1 Gen 1 to SATA III bridge with UASP");
            System.out.println("JMS583 = USB 3.1 Gen 2 to SATA III/NVMe bridge with UASP");
            System.out.println("JMB575 = 5-port SATA 6Gb/s PCIe controller");
            System.out.println("JMB585 = 5-port SATA 6Gb/s PCIe Gen 3 controller");
            System.out.println("JMB363 = SATA/PATA combo controller");
            System.out.println("JMB368 = PATA controller");
            System.out.println("JMF6xx = Flash memory controllers");
            System.out.println("JM20xxx = Legacy IDE/SATA controllers");
        }

        @Test
        void documentPackageCodes() {
            System.out.println("JMicron Package Codes:");
            System.out.println("QFN = Quad Flat No-lead package");
            System.out.println("LQFP = Low-profile Quad Flat Package");
            System.out.println("BGA = Ball Grid Array");
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldPartNumberTests {
        @ParameterizedTest
        @ValueSource(strings = {
            // Common USB-SATA bridges
            "JMS539",
            "JMS567",
            "JMS578",
            "JMS583",
            // PCIe SATA controllers
            "JMB575",
            "JMB585",
            // SATA/PATA controllers
            "JMB363",
            "JMB368",
            // Flash controllers
            "JMF602",
            "JMF612",
            "JMF667"
        })
        void shouldRecognizeRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should recognize real-world part number: " + mpn);
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series from: " + mpn);
        }
    }

    @Nested
    @DisplayName("USB-SATA Bridge Feature Matrix")
    class FeatureMatrixTests {
        @Test
        void shouldDocumentJMS539Features() {
            String mpn = "JMS539";
            assertEquals("JMS539", handler.extractSeries(mpn));
            assertEquals("USB-SATA", handler.getInterfaceType(mpn));
            assertEquals("USB 3.0", handler.getUsbGeneration(mpn));
            assertEquals(1, handler.getPortCount(mpn));
            assertFalse(handler.supportsUASP(mpn));
            assertFalse(handler.supportsNVMe(mpn));
        }

        @Test
        void shouldDocumentJMS567Features() {
            String mpn = "JMS567";
            assertEquals("JMS567", handler.extractSeries(mpn));
            assertEquals("USB-SATA", handler.getInterfaceType(mpn));
            assertEquals("USB 3.0", handler.getUsbGeneration(mpn));
            assertEquals(1, handler.getPortCount(mpn));
            assertTrue(handler.supportsUASP(mpn));
            assertFalse(handler.supportsNVMe(mpn));
        }

        @Test
        void shouldDocumentJMS578Features() {
            String mpn = "JMS578";
            assertEquals("JMS578", handler.extractSeries(mpn));
            assertEquals("USB-SATA", handler.getInterfaceType(mpn));
            assertEquals("USB 3.1 Gen 1", handler.getUsbGeneration(mpn));
            assertEquals(1, handler.getPortCount(mpn));
            assertTrue(handler.supportsUASP(mpn));
            assertFalse(handler.supportsNVMe(mpn));
        }

        @Test
        void shouldDocumentJMS583Features() {
            String mpn = "JMS583";
            assertEquals("JMS583", handler.extractSeries(mpn));
            assertEquals("USB-SATA/NVMe", handler.getInterfaceType(mpn));
            assertEquals("USB 3.1 Gen 2", handler.getUsbGeneration(mpn));
            assertEquals(1, handler.getPortCount(mpn));
            assertTrue(handler.supportsUASP(mpn));
            assertTrue(handler.supportsNVMe(mpn));
        }
    }

    @Nested
    @DisplayName("PCIe Controller Feature Tests")
    class PCIeControllerTests {
        @Test
        void shouldDocumentJMB575Features() {
            String mpn = "JMB575";
            assertEquals("JMB575", handler.extractSeries(mpn));
            assertEquals("PCIe-SATA", handler.getInterfaceType(mpn));
            assertEquals(5, handler.getPortCount(mpn));
        }

        @Test
        void shouldDocumentJMB585Features() {
            String mpn = "JMB585";
            assertEquals("JMB585", handler.extractSeries(mpn));
            assertEquals("PCIe-SATA", handler.getInterfaceType(mpn));
            assertEquals(5, handler.getPortCount(mpn));
        }
    }
}
