package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ASMediaHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ASMediaHandler.
 * Tests USB host controllers, SATA controllers, USB bridges, and USB4 controllers.
 */
class ASMediaHandlerTest {

    private static ASMediaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ASMediaHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("USB 3.0 Host Controller Detection - ASM104x Series")
    class ASM104xSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ASM1042",
            "ASM1042A",
            "ASM1042AE",
            "ASM1042A-QFN",
            "ASM1042-BGA"
        })
        void shouldDetectASM1042Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForASM1042() {
            assertEquals("ASM1042", handler.extractSeries("ASM1042"));
            assertEquals("ASM1042", handler.extractSeries("ASM1042A"));
            assertEquals("ASM1042", handler.extractSeries("ASM1042AE"));
            assertEquals("ASM1042", handler.extractSeries("ASM1042A-QFN"));
        }

        @Test
        void shouldHaveCorrectControllerType() {
            assertEquals("USB 3.0 Host Controller", handler.getControllerType("ASM1042"));
            assertEquals("USB 3.0 Host Controller", handler.getControllerType("ASM1042A"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.0", handler.getUsbVersion("ASM1042"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("PCIe to USB", handler.getInterfaceType("ASM1042"));
        }
    }

    @Nested
    @DisplayName("USB 3.0 Hub Controller Detection - ASM107x Series")
    class ASM107xSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ASM1074",
            "ASM1074A",
            "ASM1074-QFN"
        })
        void shouldDetectASM1074Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForASM1074() {
            assertEquals("ASM1074", handler.extractSeries("ASM1074"));
            assertEquals("ASM1074", handler.extractSeries("ASM1074A"));
        }

        @Test
        void shouldHaveCorrectControllerType() {
            assertEquals("USB 3.0 Hub Controller", handler.getControllerType("ASM1074"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB Hub", handler.getInterfaceType("ASM1074"));
        }
    }

    @Nested
    @DisplayName("USB 3.1 Gen2 Host Controller Detection - ASM114x Series")
    class ASM114xSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ASM1142",
            "ASM1142A",
            "ASM1142-BGA",
            "ASM1143"
        })
        void shouldDetectASM114xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForASM1142() {
            assertEquals("ASM1142", handler.extractSeries("ASM1142"));
            assertEquals("ASM1142", handler.extractSeries("ASM1142A"));
            assertEquals("ASM1143", handler.extractSeries("ASM1143"));
        }

        @Test
        void shouldHaveCorrectControllerType() {
            assertEquals("USB 3.1 Gen2 Host Controller", handler.getControllerType("ASM1142"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.1 Gen2", handler.getUsbVersion("ASM1142"));
            assertEquals("USB 3.1 Gen2", handler.getUsbVersion("ASM1143"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("PCIe to USB", handler.getInterfaceType("ASM1142"));
        }
    }

    @Nested
    @DisplayName("USB to SATA Bridge Detection - ASM115x Series")
    class ASM115xSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ASM1153",
            "ASM1153E",
            "ASM1153-QFN",
            "ASM1156",
            "ASM1156E"
        })
        void shouldDetectASM115xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForASM115x() {
            assertEquals("ASM1153", handler.extractSeries("ASM1153"));
            assertEquals("ASM1153", handler.extractSeries("ASM1153E"));
            assertEquals("ASM1156", handler.extractSeries("ASM1156"));
        }

        @Test
        void shouldHaveCorrectControllerType() {
            assertEquals("USB 3.0 to SATA Bridge", handler.getControllerType("ASM1153"));
            assertEquals("USB 3.0 to SATA Bridge", handler.getControllerType("ASM1156"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB 3.0", handler.getUsbVersion("ASM1153"));
            assertEquals("USB 3.0", handler.getUsbVersion("ASM1156"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB to SATA", handler.getInterfaceType("ASM1153"));
            assertEquals("USB to SATA", handler.getInterfaceType("ASM1156"));
        }
    }

    @Nested
    @DisplayName("SATA/NVMe Controller Detection - ASM2xxx Series")
    class ASM2xxxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ASM2362",
            "ASM2362-BGA",
            "ASM2364",
            "ASM2364A"
        })
        void shouldDetectASM2xxxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForASM2xxx() {
            assertEquals("ASM2362", handler.extractSeries("ASM2362"));
            assertEquals("ASM2364", handler.extractSeries("ASM2364"));
            assertEquals("ASM2364", handler.extractSeries("ASM2364A"));
        }

        @Test
        void shouldHaveCorrectControllerType() {
            assertEquals("PCIe to NVMe/SATA Bridge", handler.getControllerType("ASM2362"));
            assertEquals("USB 3.2 Gen2x2 to NVMe Bridge", handler.getControllerType("ASM2364"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("N/A (PCIe)", handler.getUsbVersion("ASM2362"));
            assertEquals("USB 3.2 Gen2x2", handler.getUsbVersion("ASM2364"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("PCIe to NVMe/SATA", handler.getInterfaceType("ASM2362"));
            assertEquals("USB to NVMe", handler.getInterfaceType("ASM2364"));
        }
    }

    @Nested
    @DisplayName("USB4/Thunderbolt Controller Detection - ASM3xxx Series")
    class ASM3xxxSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ASM3242",
            "ASM3242A",
            "ASM3242-BGA"
        })
        void shouldDetectASM3xxxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
        }

        @Test
        void shouldExtractCorrectSeriesForASM3xxx() {
            assertEquals("ASM3242", handler.extractSeries("ASM3242"));
            assertEquals("ASM3242", handler.extractSeries("ASM3242A"));
        }

        @Test
        void shouldHaveCorrectControllerType() {
            assertEquals("USB4 Controller", handler.getControllerType("ASM3242"));
        }

        @Test
        void shouldHaveCorrectUsbVersion() {
            assertEquals("USB4", handler.getUsbVersion("ASM3242"));
        }

        @Test
        void shouldHaveCorrectInterfaceType() {
            assertEquals("USB4/Thunderbolt", handler.getInterfaceType("ASM3242"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "ASM1042-QFN, QFN",
            "ASM1042-BGA, BGA",
            "ASM1142-LQFP, LQFP"
        })
        void shouldExtractPackageCodeFromSuffix(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        void shouldHandleReelSuffix() {
            // Package should still be extracted even with reel suffix
            assertEquals("QFN", handler.extractPackageCode("ASM1042-QFN-REEL"));
        }

        @Test
        void shouldReturnEmptyForNoPackageCode() {
            assertEquals("", handler.extractPackageCode("ASM1042"));
            assertEquals("", handler.extractPackageCode("ASM1042A"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "ASM1042, ASM1042",
            "ASM1042A, ASM1042",
            "ASM1042AE, ASM1042",
            "ASM1042A-QFN, ASM1042",
            "ASM1074, ASM1074",
            "ASM1142, ASM1142",
            "ASM1153, ASM1153",
            "ASM1156E, ASM1156",
            "ASM2362, ASM2362",
            "ASM2364, ASM2364",
            "ASM3242, ASM3242"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonASMediaParts() {
            assertEquals("", handler.extractSeries("FT232RL"));
            assertEquals("", handler.extractSeries("CP2102"));
            assertEquals("", handler.extractSeries("INVALID"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {
        @Test
        void shouldRecognizeSameSeriesAsReplacement() {
            // Same series, different revision
            assertTrue(handler.isOfficialReplacement("ASM1042", "ASM1042A"));
            assertTrue(handler.isOfficialReplacement("ASM1042A", "ASM1042AE"));
            assertTrue(handler.isOfficialReplacement("ASM1153", "ASM1153E"));
        }

        @Test
        void shouldRecognizeUpgradePath() {
            // USB 3.1 Gen2 can replace USB 3.0
            assertTrue(handler.isOfficialReplacement("ASM1142", "ASM1042"));
        }

        @Test
        void shouldRecognizeSameBridgeFamily() {
            // USB to SATA bridges are compatible within family
            assertTrue(handler.isOfficialReplacement("ASM1153", "ASM1156"));
            assertTrue(handler.isOfficialReplacement("ASM1156", "ASM1153"));
        }

        @Test
        void shouldNotReplaceDifferentControllerTypes() {
            // Host controller cannot replace hub controller
            assertFalse(handler.isOfficialReplacement("ASM1042", "ASM1074"));
            // USB controller cannot replace SATA controller
            assertFalse(handler.isOfficialReplacement("ASM1042", "ASM2362"));
        }

        @Test
        void shouldNotReplaceAcrossGenerations() {
            // USB4 is not backward compatible replacement for USB 3.0
            assertFalse(handler.isOfficialReplacement("ASM3242", "ASM1042"));
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
            assertEquals("", handler.getControllerType(null));
            assertEquals("", handler.getUsbVersion(null));
            assertEquals("", handler.getInterfaceType(null));
            assertFalse(handler.isOfficialReplacement(null, "ASM1042"));
            assertFalse(handler.isOfficialReplacement("ASM1042", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getControllerType(""));
            assertEquals("", handler.getUsbVersion(""));
            assertEquals("", handler.getInterfaceType(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("ASM1042", null, registry));
        }

        @Test
        void shouldOnlyMatchICType() {
            // ASMedia parts should only match IC type, not other types
            assertTrue(handler.matches("ASM1042", ComponentType.IC, registry));
            assertFalse(handler.matches("ASM1042", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("ASM1042", ComponentType.MEMORY, registry));
        }

        @Test
        void shouldHandleCaseInsensitivity() {
            assertTrue(handler.matches("asm1042", ComponentType.IC, registry));
            assertTrue(handler.matches("ASM1042", ComponentType.IC, registry));
            assertTrue(handler.matches("Asm1042", ComponentType.IC, registry));
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
        void shouldGetControllerType() {
            assertEquals("USB 3.0 Host Controller", handler.getControllerType("ASM1042"));
            assertEquals("USB 3.0 Hub Controller", handler.getControllerType("ASM1074"));
            assertEquals("USB 3.1 Gen2 Host Controller", handler.getControllerType("ASM1142"));
            assertEquals("USB 3.0 to SATA Bridge", handler.getControllerType("ASM1153"));
            assertEquals("USB 3.0 to SATA Bridge", handler.getControllerType("ASM1156"));
            assertEquals("PCIe to NVMe/SATA Bridge", handler.getControllerType("ASM2362"));
            assertEquals("USB 3.2 Gen2x2 to NVMe Bridge", handler.getControllerType("ASM2364"));
            assertEquals("USB4 Controller", handler.getControllerType("ASM3242"));
        }

        @Test
        void shouldGetUsbVersion() {
            assertEquals("USB 3.0", handler.getUsbVersion("ASM1042"));
            assertEquals("USB 3.0", handler.getUsbVersion("ASM1074"));
            assertEquals("USB 3.1 Gen2", handler.getUsbVersion("ASM1142"));
            assertEquals("USB 3.0", handler.getUsbVersion("ASM1153"));
            assertEquals("N/A (PCIe)", handler.getUsbVersion("ASM2362"));
            assertEquals("USB 3.2 Gen2x2", handler.getUsbVersion("ASM2364"));
            assertEquals("USB4", handler.getUsbVersion("ASM3242"));
        }

        @Test
        void shouldGetInterfaceType() {
            assertEquals("PCIe to USB", handler.getInterfaceType("ASM1042"));
            assertEquals("USB Hub", handler.getInterfaceType("ASM1074"));
            assertEquals("PCIe to USB", handler.getInterfaceType("ASM1142"));
            assertEquals("USB to SATA", handler.getInterfaceType("ASM1153"));
            assertEquals("PCIe to NVMe/SATA", handler.getInterfaceType("ASM2362"));
            assertEquals("USB to NVMe", handler.getInterfaceType("ASM2364"));
            assertEquals("USB4/Thunderbolt", handler.getInterfaceType("ASM3242"));
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
            System.out.println("ASMedia Product Families:");
            System.out.println("ASM104x = PCIe to USB 3.0 Host Controllers (ASM1042)");
            System.out.println("ASM107x = USB 3.0 Hub Controllers (ASM1074)");
            System.out.println("ASM114x = PCIe to USB 3.1 Gen2 Host Controllers (ASM1142, ASM1143)");
            System.out.println("ASM115x = USB 3.0 to SATA Bridge Controllers (ASM1153, ASM1156)");
            System.out.println("ASM236x = PCIe to NVMe/SATA Bridge Controllers (ASM2362)");
            System.out.println("ASM2364 = USB 3.2 Gen2x2 to NVMe Bridge Controller");
            System.out.println("ASM324x = USB4/Thunderbolt Controllers (ASM3242)");
        }

        @Test
        void documentCommonUseCases() {
            System.out.println("Common ASMedia Use Cases:");
            System.out.println("ASM1042 = Add-in PCIe USB 3.0 cards, motherboard USB 3.0 ports");
            System.out.println("ASM1074 = USB 3.0 hub chips for multi-port hubs");
            System.out.println("ASM1142 = USB 3.1 Gen2 (10Gbps) add-in cards");
            System.out.println("ASM1153/1156 = External USB 3.0 hard drive enclosures");
            System.out.println("ASM2362 = M.2 NVMe to PCIe adapters");
            System.out.println("ASM2364 = USB to NVMe enclosures (20Gbps)");
            System.out.println("ASM3242 = USB4/Thunderbolt docking stations");
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldPartNumberTests {
        @ParameterizedTest
        @ValueSource(strings = {
            // USB 3.0 host controllers
            "ASM1042",
            "ASM1042A",
            "ASM1042AE",
            // USB 3.0 hub
            "ASM1074",
            // USB 3.1 Gen2 host controllers
            "ASM1142",
            "ASM1142A",
            // USB to SATA bridges
            "ASM1153",
            "ASM1153E",
            "ASM1156",
            // NVMe bridges
            "ASM2362",
            "ASM2364",
            // USB4
            "ASM3242"
        })
        void shouldRecognizeRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should recognize real-world part number: " + mpn);
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series from: " + mpn);
        }
    }

    @Nested
    @DisplayName("Generic Fallback Matching")
    class GenericFallbackTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ASM1234",
            "ASM5678",
            "ASM9999"
        })
        void shouldMatchGenericASMPattern(String mpn) {
            // Any ASM + 4 digits should match as IC
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match generic ASM pattern: " + mpn);
        }

        @Test
        void shouldExtractSeriesForGenericParts() {
            assertEquals("ASM1234", handler.extractSeries("ASM1234"));
            assertEquals("ASM5678", handler.extractSeries("ASM5678A"));
        }

        @Test
        void shouldReturnGenericControllerType() {
            assertEquals("USB Controller", handler.getControllerType("ASM1234"));
            assertEquals("Storage Controller", handler.getControllerType("ASM2999"));
            assertEquals("USB4/TB Controller", handler.getControllerType("ASM3999"));
        }
    }
}
