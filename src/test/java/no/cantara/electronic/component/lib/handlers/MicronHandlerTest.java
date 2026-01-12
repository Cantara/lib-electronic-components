package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MicronHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for MicronHandler.
 * Tests DRAM, NAND Flash, NOR Flash, and SSDs.
 */
class MicronHandlerTest {

    private static MicronHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new MicronHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("DDR4 SDRAM Detection")
    class DDR4Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MT40A1G16KD-062E:E",
            "MT40A512M16LY-062E:E",
            "MT40A2G4WE-062E:E"
        })
        void documentDDR4Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_MICRON = " + matchesMicron);
        }
    }

    @Nested
    @DisplayName("DDR3 SDRAM Detection")
    class DDR3Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MT41K256M16TW-107:P",
            "MT41K512M8RH-125:E",
            "MT41K128M16JT-125:K"
        })
        void documentDDR3Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_MICRON = " + matchesMicron);
        }
    }

    @Nested
    @DisplayName("LPDDR4 Detection")
    class LPDDR4Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MT48LC4M16A2P-6A:G",
            "MT48LC8M16A2P-75:C"
        })
        void documentLPDDR4Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_MICRON = " + matchesMicron);
        }
    }

    @Nested
    @DisplayName("NAND Flash Detection")
    class NANDFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MT29F4G08ABADAH4-IT:D",
            "MT29F8G08ADBDAH4:D",
            "MT29E512G08CKBBA-C"
        })
        void documentNANDFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_MICRON = " + matchesMicron);
        }
    }

    @Nested
    @DisplayName("NOR Flash Detection")
    class NORFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MT28EW128ABA1LPC-0SIT",
            "MT25QL512ABB8ESF-0SIT",
            "MT25TL512ABB8ESF-0SIT"
        })
        void documentNORFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_MICRON = " + matchesMicron);
        }
    }

    @Nested
    @DisplayName("eMMC Detection")
    class eMMCTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MT29K4G16WDBAH4-AATC:C",
            "MT29L2G8PABDAWP-IT:D"
        })
        void documenteMMCDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_MICRON = " + matchesMicron);
        }
    }

    @Nested
    @DisplayName("SSD Detection")
    class SSDTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MTFDDAK480TDC-1AT1ZABYY",
            "MTFDDAV256TDL-1AW1ZABYY"
        })
        void documentSSDDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_MICRON = " + matchesMicron);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "MT40A1G16KD-062E:E-BGA, BGA",
            "MT29F4G08ABADAH4-FBGA, FBGA",
            "MT25QL512ABB8ESF-TSOP, TSOP"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "MT40A1G16KD-062E:E, MT40A",
            "MT41K256M16TW-107:P, MT41K",
            "MT48LC4M16A2P-6A:G, MT48L",
            "MT29F4G08ABADAH4-IT:D, MT29F",
            "MT28EW128ABA1LPC-0SIT, MT28E",
            "MT25QL512ABB8ESF-0SIT, MT25Q",
            "MTFDDAK480TDC-1AT1ZABYY, MTFDD"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("MT40A1G16KD-062E:E", "MT41K256M16TW-107:P"));
        }

        @Test
        void documentSpeedGradeCompatibility() {
            // Faster speed grade should be able to replace slower (lower number = faster)
            boolean fasterReplaces = handler.isOfficialReplacement(
                "MT40A1G16KD-062E-I", "MT40A1G16KD-075E-I");
            System.out.println("Faster speed (062) can replace slower (075) = " + fasterReplaces);
        }

        @Test
        void documentTemperatureRangeCompatibility() {
            // Automotive (-A) can replace Industrial (-I)
            // Industrial (-I) can replace Commercial (-C)
            boolean automotiveReplacesIndustrial = handler.isOfficialReplacement(
                "MT40A1G16KD-062E-A", "MT40A1G16KD-062E-I");
            boolean industrialReplacesCommercial = handler.isOfficialReplacement(
                "MT40A1G16KD-062E-I", "MT40A1G16KD-062E-C");
            System.out.println("Automotive can replace Industrial = " + automotiveReplacesIndustrial);
            System.out.println("Industrial can replace Commercial = " + industrialReplacesCommercial);
        }
    }

    @Nested
    @DisplayName("Temperature Range Codes")
    class TemperatureRangeTests {
        @Test
        void documentTemperatureRangeCodes() {
            // Document the temperature range code conventions
            System.out.println("Temperature Range Codes:");
            System.out.println("-I = Industrial (-40 to +85C)");
            System.out.println("-C = Commercial (0 to +70C)");
            System.out.println("-A = Automotive (-40 to +105C)");
            System.out.println("-E = Extended (-40 to +95C)");
        }
    }
}
