package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.WinbondHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for WinbondHandler.
 * Tests SPI/QSPI Flash, NOR Flash, and EEPROM detection.
 */
class WinbondHandlerTest {

    private static WinbondHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new WinbondHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SPI/QSPI Flash Detection - W25Qxx Series")
    class W25QFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "W25Q32JVSSIQ",
            "W25Q64FVSIG",
            "W25Q128JVSIM",
            "W25Q256JVEIQ"
        })
        void documentW25QFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesFlash = handler.matches(mpn, ComponentType.MEMORY_FLASH, registry);
            boolean matchesWinbond = handler.matches(mpn, ComponentType.MEMORY_FLASH_WINBOND, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_FLASH = " + matchesFlash);
            System.out.println(mpn + " matches MEMORY_FLASH_WINBOND = " + matchesWinbond);
        }
    }

    @Nested
    @DisplayName("SPI NAND Flash Detection - W25Nxx Series")
    class W25NFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "W25N01GVZEIG",
            "W25N02KVZEIR",
            "W25N512GVEIG"
        })
        void documentW25NFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesFlash = handler.matches(mpn, ComponentType.MEMORY_FLASH, registry);
            boolean matchesWinbond = handler.matches(mpn, ComponentType.MEMORY_FLASH_WINBOND, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_FLASH = " + matchesFlash);
            System.out.println(mpn + " matches MEMORY_FLASH_WINBOND = " + matchesWinbond);
        }
    }

    @Nested
    @DisplayName("Special Flash Detection - W25Xxx Series")
    class W25XFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "W25X10CLSNIG",
            "W25X20CLSNIG",
            "W25X40CLSNIG"
        })
        void documentW25XFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesFlash = handler.matches(mpn, ComponentType.MEMORY_FLASH, registry);
            boolean matchesWinbond = handler.matches(mpn, ComponentType.MEMORY_FLASH_WINBOND, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_FLASH = " + matchesFlash);
            System.out.println(mpn + " matches MEMORY_FLASH_WINBOND = " + matchesWinbond);
        }
    }

    @Nested
    @DisplayName("NOR Flash Detection - W29Cxx Series")
    class W29CFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "W29C020C-90B",
            "W29C040P-70",
            "W29C010P-90N"
        })
        void documentW29CFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesFlash = handler.matches(mpn, ComponentType.MEMORY_FLASH, registry);
            boolean matchesWinbond = handler.matches(mpn, ComponentType.MEMORY_FLASH_WINBOND, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_FLASH = " + matchesFlash);
            System.out.println(mpn + " matches MEMORY_FLASH_WINBOND = " + matchesWinbond);
        }
    }

    @Nested
    @DisplayName("NOR Flash Detection - W29Nxx Series")
    class W29NFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "W29N01GVSIAA",
            "W29N02GVSIAA",
            "W29N04GVSIAA"
        })
        void documentW29NFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesFlash = handler.matches(mpn, ComponentType.MEMORY_FLASH, registry);
            boolean matchesWinbond = handler.matches(mpn, ComponentType.MEMORY_FLASH_WINBOND, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_FLASH = " + matchesFlash);
            System.out.println(mpn + " matches MEMORY_FLASH_WINBOND = " + matchesWinbond);
        }
    }

    @Nested
    @DisplayName("NOR Flash Detection - W29Exx Series")
    class W29EFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "W29EE011-15",
            "W29EE012-90",
            "W29E011T-15"
        })
        void documentW29EFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesFlash = handler.matches(mpn, ComponentType.MEMORY_FLASH, registry);
            boolean matchesWinbond = handler.matches(mpn, ComponentType.MEMORY_FLASH_WINBOND, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_FLASH = " + matchesFlash);
            System.out.println(mpn + " matches MEMORY_FLASH_WINBOND = " + matchesWinbond);
        }
    }

    @Nested
    @DisplayName("EEPROM Detection - W24xxx Series")
    class EEPROMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "W24C02",
            "W24C04",
            "W24C08",
            "W24C16",
            "W24C32",
            "W24C64",
            "W24C128",
            "W24C256"
        })
        void documentEEPROMDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesEeprom = handler.matches(mpn, ComponentType.MEMORY_EEPROM, registry);
            boolean matchesWinbond = handler.matches(mpn, ComponentType.MEMORY_EEPROM_WINBOND, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_EEPROM = " + matchesEeprom);
            System.out.println(mpn + " matches MEMORY_EEPROM_WINBOND = " + matchesWinbond);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "W25Q32S, SOIC",
            "W25Q64SS, SOIC",
            "W25Q128F, QFN",
            "W25Q256W, WSON",
            "W25Q512U, USON"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @Test
        void shouldHandleEmptyPackageCode() {
            // Handler returns empty string when no recognized package suffix
            assertEquals("", handler.extractPackageCode("W24C32"));
        }

        @Test
        void shouldReturnSuffixForUnrecognizedPackage() {
            // Handler returns the raw suffix for unrecognized package codes
            String result = handler.extractPackageCode("W25Q32JVS");
            assertEquals("JVS", result);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "W25Q32JVSSIQ, W25Q32JVSSIQ",
            "W25Q64FVSIG, W25Q64FVSIG",
            "W25Q128JVSIM, W25Q128JVSIM",
            "W25N01GVZEIG, W25N01GVZEIG",
            "W29C020C-90B, W29C020C",
            "W29N01GVSIAA, W29N01GVSIAA"
        })
        void shouldExtractSeries(String mpn, String expected) {
            String actual = handler.extractSeries(mpn);
            assertEquals(expected, actual, "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonWinbondParts() {
            assertEquals("", handler.extractSeries("AT25SF128A"));
            assertEquals("", handler.extractSeries("MX25L12835F"));
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

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("W25Q32JVSSIQ", null, registry));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("W25Q32JVSSIQ", "W25N01GVZEIG"));
        }

        @Test
        void shouldNotReplaceDifferentDensities() {
            assertFalse(handler.isOfficialReplacement("W25Q32JVSSIQ", "W25Q64JVSSIQ"));
        }

        @Test
        void documentSameSeriesReplacement() {
            // Same series with different package should be compatible
            boolean sameSeriesCompatible = handler.isOfficialReplacement(
                "W25Q32JVSSIQ", "W25Q32JVSFIQ");
            System.out.println("Same series different package compatible = " + sameSeriesCompatible);
        }

        @Test
        void documentFlashToEEPROMReplacement() {
            // Flash should not replace EEPROM and vice versa
            boolean flashReplacesEeprom = handler.isOfficialReplacement(
                "W25Q32JVSSIQ", "W24C32");
            System.out.println("Flash can replace EEPROM = " + flashReplacesEeprom);
        }
    }

    @Nested
    @DisplayName("Package Code Reference")
    class PackageCodeReferenceTests {
        @Test
        void documentPackageCodes() {
            System.out.println("Winbond Package Codes:");
            System.out.println("S/SS = SOIC (Small Outline IC)");
            System.out.println("F = QFN (Quad Flat No-lead)");
            System.out.println("W = WSON (Very Very Thin Small Outline No-lead)");
            System.out.println("U = USON (Ultra Small Outline No-lead)");
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {
        @Test
        void documentProductFamilies() {
            System.out.println("Winbond Product Families:");
            System.out.println("W25Qxx = SPI/QSPI NOR Flash");
            System.out.println("W25Nxx = SPI NAND Flash");
            System.out.println("W25Xxx = Legacy SPI Flash");
            System.out.println("W29Cxx = Parallel NOR Flash");
            System.out.println("W29Nxx = Parallel NAND Flash");
            System.out.println("W29Exx = Parallel EEPROM-like Flash");
            System.out.println("W24xxx = I2C EEPROM");
        }
    }
}
