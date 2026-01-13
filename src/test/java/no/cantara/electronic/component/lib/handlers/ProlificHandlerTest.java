package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ProlificHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ProlificHandler.
 * Tests USB to serial, bridge, hub, and parallel interface ICs.
 */
class ProlificHandlerTest {

    private static ProlificHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ProlificHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("USB Serial Detection - PL23xx Series")
    class PL23xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PL2303",
            "PL2303HX",
            "PL2303HXA",
            "PL2303HXD",
            "PL2303TA",
            "PL2303GT",
            "PL2303GL",
            "PL2303GC",
            "PL2303RA",
            "PL2303SA",
            "PL2303-SSOP",
            "PL2303HXA-REEL",
            "PL2312"
        })
        void shouldDetectPL23xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForPL23xx() {
            assertEquals("PL2303", handler.extractSeries("PL2303"));
            assertEquals("PL2303", handler.extractSeries("PL2303HX"));
            assertEquals("PL2303", handler.extractSeries("PL2303HXA"));
            assertEquals("PL2303", handler.extractSeries("PL2303HXD"));
            assertEquals("PL2303", handler.extractSeries("PL2303TA"));
            assertEquals("PL2303", handler.extractSeries("PL2303GT"));
            assertEquals("PL2312", handler.extractSeries("PL2312"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("Serial", handler.getInterfaceType("PL2303"));
            assertEquals("Serial", handler.getInterfaceType("PL2303HX"));
            assertEquals("Serial", handler.getInterfaceType("PL2312"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("PL2303"));
            assertEquals("USB 2.0", handler.getUsbVersion("PL2303HX"));
            assertEquals("USB 2.0", handler.getUsbVersion("PL2312"));
        }
    }

    @Nested
    @DisplayName("USB Bridge Detection - PL25xx Series")
    class PL25xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PL2501",
            "PL2501A",
            "PL2571",
            "PL2571B"
        })
        void shouldDetectPL25xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForPL25xx() {
            assertEquals("PL2501", handler.extractSeries("PL2501"));
            assertEquals("PL2501", handler.extractSeries("PL2501A"));
            assertEquals("PL2571", handler.extractSeries("PL2571"));
            assertEquals("PL2571", handler.extractSeries("PL2571B"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("Bridge", handler.getInterfaceType("PL2501"));
            assertEquals("IDE Bridge", handler.getInterfaceType("PL2571"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("PL2501"));
            assertEquals("USB 2.0", handler.getUsbVersion("PL2571"));
        }
    }

    @Nested
    @DisplayName("USB Hub Detection - PL27xx Series")
    class PL27xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PL2734",
            "PL2734A",
            "PL2773",
            "PL2773B"
        })
        void shouldDetectPL27xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForPL27xx() {
            assertEquals("PL2734", handler.extractSeries("PL2734"));
            assertEquals("PL2734", handler.extractSeries("PL2734A"));
            assertEquals("PL2773", handler.extractSeries("PL2773"));
            assertEquals("PL2773", handler.extractSeries("PL2773B"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("Hub", handler.getInterfaceType("PL2734"));
            assertEquals("Hub", handler.getInterfaceType("PL2773"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("PL2734"));
            assertEquals("USB 3.0", handler.getUsbVersion("PL2773"));
        }

        @Test
        void shouldReturnCorrectPortCount() {
            assertEquals(4, handler.getHubPortCount("PL2734"));
            assertEquals(4, handler.getHubPortCount("PL2773"));
            assertEquals(0, handler.getHubPortCount("PL2303"));  // Not a hub
        }
    }

    @Nested
    @DisplayName("USB to Parallel Detection - PL38xx Series")
    class PL38xxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PL3805",
            "PL3805A"
        })
        void shouldDetectPL38xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForPL38xx() {
            assertEquals("PL3805", handler.extractSeries("PL3805"));
            assertEquals("PL3805", handler.extractSeries("PL3805A"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("Parallel", handler.getInterfaceType("PL3805"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("PL3805"));
        }
    }

    @Nested
    @DisplayName("PL2303 Variant Detection")
    class PL2303VariantTests {
        @ParameterizedTest
        @CsvSource({
            "PL2303, Standard",
            "PL2303HX, HX",
            "PL2303HXA, HXA",
            "PL2303HXD, HXD",
            "PL2303TA, TA",
            "PL2303GT, GT",
            "PL2303GL, GL",
            "PL2303GC, GC",
            "PL2303RA, RA",
            "PL2303SA, SA"
        })
        void shouldDetectPL2303Variant(String mpn, String expectedVariant) {
            assertEquals(expectedVariant, handler.getPL2303Variant(mpn),
                    "Variant detection for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonPL2303() {
            assertEquals("", handler.getPL2303Variant("PL2501"));
            assertEquals("", handler.getPL2303Variant("PL2734"));
            assertEquals("", handler.getPL2303Variant("INVALID"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "PL2303HX, SSOP",
            "PL2303HXA, SSOP",
            "PL2303TA, SSOP",
            "PL2303GT, QFN",
            "PL2303GL, QFN",
            "PL2303GC, QFN",
            "PL2303RA, SOP",
            "PL2303SA, SOP",
            "PL2303-SSOP, SSOP",
            "PL2303-QFN, QFN"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        void shouldHandleReelSuffix() {
            assertEquals("SSOP", handler.extractPackageCode("PL2303HXA-REEL"));
            assertEquals("SSOP", handler.extractPackageCode("PL2303TA-TR"));
        }

        @Test
        void shouldReturnEmptyForBasicMPN() {
            assertEquals("", handler.extractPackageCode("PL2303"));
        }

        @Test
        void shouldReturnEmptyForNull() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "PL2303, PL2303",
            "PL2303HX, PL2303",
            "PL2303HXA, PL2303",
            "PL2303HXA-REEL, PL2303",
            "PL2312, PL2312",
            "PL2501, PL2501",
            "PL2571, PL2571",
            "PL2734, PL2734",
            "PL2773, PL2773",
            "PL3805, PL3805"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonProlificParts() {
            assertEquals("", handler.extractSeries("FT232RL"));
            assertEquals("", handler.extractSeries("CP2102"));
            assertEquals("", handler.extractSeries("CH340G"));
            assertEquals("", handler.extractSeries("INVALID"));
        }

        @Test
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {
        @Test
        void shouldRecognizeSameSeriesAsReplacement() {
            // Same series, different variant
            assertTrue(handler.isOfficialReplacement("PL2303HX", "PL2303HXA"));
            assertTrue(handler.isOfficialReplacement("PL2303TA", "PL2303GT"));
        }

        @Test
        void shouldRecognizePL2303FamilyAsCompatible() {
            // PL2303 variants are generally compatible
            assertTrue(handler.isOfficialReplacement("PL2303", "PL2303HX"));
            assertTrue(handler.isOfficialReplacement("PL2303HX", "PL2303HXD"));
        }

        @Test
        void shouldNotReplaceAcrossDifferentSeries() {
            // Different series are not interchangeable
            assertFalse(handler.isOfficialReplacement("PL2303", "PL2501"));
            assertFalse(handler.isOfficialReplacement("PL2303", "PL2734"));
            assertFalse(handler.isOfficialReplacement("PL2501", "PL2571"));
        }

        @Test
        void shouldNotReplaceSerialWithHub() {
            assertFalse(handler.isOfficialReplacement("PL2303", "PL2773"));
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "PL2303"));
            assertFalse(handler.isOfficialReplacement("PL2303", null));
            assertFalse(handler.isOfficialReplacement(null, null));
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
            assertEquals("", handler.getUsbVersion(null));
            assertEquals("", handler.getPL2303Variant(null));
            assertEquals(0, handler.getHubPortCount(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getInterfaceType(""));
            assertEquals("", handler.getUsbVersion(""));
            assertEquals("", handler.getPL2303Variant(""));
            assertEquals(0, handler.getHubPortCount(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("PL2303", null, registry));
        }

        @Test
        void shouldOnlyMatchICType() {
            // Prolific parts should only match IC type, not other types
            assertTrue(handler.matches("PL2303", ComponentType.IC, registry));
            assertFalse(handler.matches("PL2303", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("PL2303", ComponentType.MEMORY, registry));
        }

        @Test
        void shouldHandleCaseInsensitivity() {
            assertTrue(handler.matches("pl2303", ComponentType.IC, registry));
            assertTrue(handler.matches("PL2303", ComponentType.IC, registry));
            assertTrue(handler.matches("Pl2303", ComponentType.IC, registry));
        }

        @Test
        void shouldReturnEmptyForInvalidMpn() {
            assertEquals("", handler.extractPackageCode("INVALID-MPN"));
            assertEquals("", handler.extractSeries("INVALID-MPN"));
            assertEquals("", handler.getInterfaceType("INVALID-MPN"));
            assertEquals("", handler.getUsbVersion("INVALID-MPN"));
        }

        @Test
        void shouldNotMatchOtherUSBSerialChips() {
            // Should not match competitor parts
            assertFalse(handler.matches("FT232RL", ComponentType.IC, registry));
            assertFalse(handler.matches("CP2102", ComponentType.IC, registry));
            assertFalse(handler.matches("CH340G", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetInterfaceType() {
            assertEquals("Serial", handler.getInterfaceType("PL2303"));
            assertEquals("Serial", handler.getInterfaceType("PL2303HX"));
            assertEquals("Serial", handler.getInterfaceType("PL2312"));
            assertEquals("Bridge", handler.getInterfaceType("PL2501"));
            assertEquals("IDE Bridge", handler.getInterfaceType("PL2571"));
            assertEquals("Hub", handler.getInterfaceType("PL2734"));
            assertEquals("Hub", handler.getInterfaceType("PL2773"));
            assertEquals("Parallel", handler.getInterfaceType("PL3805"));
        }

        @Test
        void shouldGetUsbVersion() {
            assertEquals("USB 2.0", handler.getUsbVersion("PL2303"));
            assertEquals("USB 2.0", handler.getUsbVersion("PL2501"));
            assertEquals("USB 2.0", handler.getUsbVersion("PL2734"));
            assertEquals("USB 3.0", handler.getUsbVersion("PL2773"));
            assertEquals("USB 2.0", handler.getUsbVersion("PL3805"));
        }

        @Test
        void shouldGetHubPortCount() {
            assertEquals(4, handler.getHubPortCount("PL2734"));
            assertEquals(4, handler.getHubPortCount("PL2773"));
            assertEquals(0, handler.getHubPortCount("PL2303"));  // Not a hub
            assertEquals(0, handler.getHubPortCount("PL2501"));  // Not a hub
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
            System.out.println("Prolific Product Families:");
            System.out.println("PL2303 = USB to Serial UART (most popular)");
            System.out.println("  - PL2303HX = High performance version");
            System.out.println("  - PL2303HXA = HX with improved compatibility");
            System.out.println("  - PL2303HXD = Enhanced HX variant");
            System.out.println("  - PL2303TA = Alternative package variant");
            System.out.println("  - PL2303GT/GL/GC = G-series variants");
            System.out.println("PL2312 = USB to Serial (enhanced)");
            System.out.println("PL2501 = USB Bridge IC");
            System.out.println("PL2571 = USB to IDE Bridge");
            System.out.println("PL2734 = USB 2.0 Hub Controller (4-port)");
            System.out.println("PL2773 = USB 3.0 Hub Controller (4-port)");
            System.out.println("PL3805 = USB to Parallel Port");
        }

        @Test
        void documentPackageCodes() {
            System.out.println("Prolific Package Codes:");
            System.out.println("HX = SSOP package (standard)");
            System.out.println("HXA = SSOP package (enhanced)");
            System.out.println("HXD = SSOP package (enhanced v2)");
            System.out.println("TA = SSOP-28 package");
            System.out.println("GT/GL/GC = QFN package variants");
            System.out.println("RA/SA = SOP package variants");
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldPartNumberTests {
        @ParameterizedTest
        @ValueSource(strings = {
            // Common USB to serial converters
            "PL2303",
            "PL2303HX",
            "PL2303HXA",
            "PL2303HXD",
            "PL2303TA",
            "PL2303GT",
            "PL2303GL",
            "PL2303GC",
            "PL2303HXA-REEL",
            // USB Bridge
            "PL2501",
            "PL2571",
            // USB Hub
            "PL2734",
            "PL2773",
            // USB to Parallel
            "PL3805"
        })
        void shouldRecognizeRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should recognize real-world part number: " + mpn);
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series from: " + mpn);
        }
    }

    @Nested
    @DisplayName("MPN Pattern Validation")
    class MPNPatternTests {
        @Test
        void shouldMatchSupportedSeriesPatterns() {
            // PL23xx series (USB to serial)
            assertTrue(handler.matches("PL2300", ComponentType.IC, registry));
            assertTrue(handler.matches("PL2399", ComponentType.IC, registry));
            assertTrue(handler.matches("PL2303ABC", ComponentType.IC, registry));

            // PL25xx series (USB bridge)
            assertTrue(handler.matches("PL2500", ComponentType.IC, registry));
            assertTrue(handler.matches("PL2599", ComponentType.IC, registry));

            // PL27xx series (USB hub)
            assertTrue(handler.matches("PL2700", ComponentType.IC, registry));
            assertTrue(handler.matches("PL2799", ComponentType.IC, registry));

            // PL38xx series (USB to parallel)
            assertTrue(handler.matches("PL3800", ComponentType.IC, registry));
            assertTrue(handler.matches("PL3899", ComponentType.IC, registry));
        }

        @Test
        void shouldNotMatchInvalidPatterns() {
            // Invalid prefixes
            assertFalse(handler.matches("PL1303", ComponentType.IC, registry));  // PL1xxx not supported
            assertFalse(handler.matches("PL2003", ComponentType.IC, registry));  // PL20xx not supported
            assertFalse(handler.matches("PL2103", ComponentType.IC, registry));  // PL21xx not supported
            assertFalse(handler.matches("PL2203", ComponentType.IC, registry));  // PL22xx not supported
            assertFalse(handler.matches("PL2403", ComponentType.IC, registry));  // PL24xx not supported
            assertFalse(handler.matches("PL2603", ComponentType.IC, registry));  // PL26xx not supported
            assertFalse(handler.matches("PL2803", ComponentType.IC, registry));  // PL28xx not supported
            assertFalse(handler.matches("PL3303", ComponentType.IC, registry));  // PL33xx not supported
            assertFalse(handler.matches("XL2303", ComponentType.IC, registry));  // Wrong prefix
        }
    }
}
