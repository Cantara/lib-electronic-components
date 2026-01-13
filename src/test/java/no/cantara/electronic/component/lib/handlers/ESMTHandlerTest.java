package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ESMTHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ESMTHandler.
 * Tests SDRAM, DDR SDRAM, SPI Flash, and Parallel Flash detection.
 * <p>
 * ESMT (Elite Semiconductor Memory Technology) is a Taiwanese semiconductor
 * company specializing in DRAM and Flash memory products.
 */
class ESMTHandlerTest {

    private static ESMTHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ESMTHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SDRAM Detection - M12L Series")
    class SDRAMTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "M12L128168A-6TG",
            "M12L128168A-7TG",
            "M12L64164A-5TG",
            "M12L64164A-6TG",
            "M12L32164A-5TG",
            "M12L16164A-7TG"
        })
        void shouldMatchSDRAMAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match as MEMORY");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "M12L128168A-6TG",
            "M12L64164A-5TG"
        })
        void shouldMatchSDRAMAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match as IC");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "M12L128168A-6TG",
            "M12L64164A-5TG"
        })
        void shouldNotMatchSDRAMAsFlash(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should NOT match as MEMORY_FLASH (SDRAM is not flash)");
        }

        @Test
        void documentSDRAMProductFamily() {
            System.out.println("ESMT SDRAM Product Family (M12L Series):");
            System.out.println("M12L128168A = 128Mb SDRAM (8M x 16 bits x 8 banks)");
            System.out.println("M12L64164A = 64Mb SDRAM (4M x 16 bits x 4 banks)");
            System.out.println("M12L32164A = 32Mb SDRAM (2M x 16 bits x 4 banks)");
            System.out.println("M12L16164A = 16Mb SDRAM (1M x 16 bits x 4 banks)");
        }
    }

    @Nested
    @DisplayName("DDR SDRAM Detection - M14D Series")
    class DDRSDRAMTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "M14D5121632A-2.5BG",
            "M14D5121632A-25BG",
            "M14D2561616A-25BG",
            "M14D1281616A-25BG"
        })
        void shouldMatchDDRAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match as MEMORY");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "M14D5121632A-2.5BG",
            "M14D2561616A-25BG"
        })
        void shouldMatchDDRAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match as IC");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "M14D5121632A-2.5BG",
            "M14D2561616A-25BG"
        })
        void shouldNotMatchDDRAsFlash(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should NOT match as MEMORY_FLASH (DDR SDRAM is not flash)");
        }

        @Test
        void documentDDRSDRAMProductFamily() {
            System.out.println("ESMT DDR SDRAM Product Family (M14D Series):");
            System.out.println("M14D5121632A = 512Mb DDR SDRAM (32M x 16 bits x 2 banks)");
            System.out.println("M14D2561616A = 256Mb DDR SDRAM (16M x 16 bits x 1 bank)");
            System.out.println("M14D1281616A = 128Mb DDR SDRAM (8M x 16 bits x 1 bank)");
        }
    }

    @Nested
    @DisplayName("SPI Flash Detection - F25L Series")
    class SPIFlashTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "F25L004A-100PAIG",
            "F25L008A-100PAIG",
            "F25L016A-100PAIG",
            "F25L032A-100PAIG",
            "F25L064A",
            "F25L004A"
        })
        void shouldMatchSPIFlashAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match as MEMORY");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "F25L004A-100PAIG",
            "F25L008A-100PAIG",
            "F25L016A-100PAIG"
        })
        void shouldMatchSPIFlashAsFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match as MEMORY_FLASH");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "F25L004A-100PAIG",
            "F25L008A-100PAIG"
        })
        void shouldMatchSPIFlashAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match as IC");
        }

        @Test
        void documentSPIFlashProductFamily() {
            System.out.println("ESMT SPI Flash Product Family (F25L Series):");
            System.out.println("F25L004A = 4Mb (512KB) SPI NOR Flash");
            System.out.println("F25L008A = 8Mb (1MB) SPI NOR Flash");
            System.out.println("F25L016A = 16Mb (2MB) SPI NOR Flash");
            System.out.println("F25L032A = 32Mb (4MB) SPI NOR Flash");
            System.out.println("F25L064A = 64Mb (8MB) SPI NOR Flash");
        }
    }

    @Nested
    @DisplayName("Parallel Flash Detection - F49L Series")
    class ParallelFlashTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "F49L160UA-70TG",
            "F49L160UA-55TG",
            "F49L320UA-70TG",
            "F49L640UA-70TG"
        })
        void shouldMatchParallelFlashAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match as MEMORY");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "F49L160UA-70TG",
            "F49L320UA-70TG"
        })
        void shouldMatchParallelFlashAsFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match as MEMORY_FLASH");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "F49L160UA-70TG",
            "F49L320UA-70TG"
        })
        void shouldMatchParallelFlashAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match as IC");
        }

        @Test
        void documentParallelFlashProductFamily() {
            System.out.println("ESMT Parallel Flash Product Family (F49L Series):");
            System.out.println("F49L160UA = 16Mb Parallel NOR Flash");
            System.out.println("F49L320UA = 32Mb Parallel NOR Flash");
            System.out.println("F49L640UA = 64Mb Parallel NOR Flash");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @CsvSource({
            "M12L128168A-6TG, M12L",
            "M12L64164A-5TG, M12L",
            "M14D5121632A-2.5BG, M14D",
            "M14D2561616A-25BG, M14D",
            "F25L004A-100PAIG, F25L",
            "F25L008A, F25L",
            "F25L016A-100PAIG, F25L",
            "F49L160UA-70TG, F49L",
            "F49L320UA-70TG, F49L"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonESMTParts() {
            assertEquals("", handler.extractSeries("W25Q32JVSSIQ"));
            assertEquals("", handler.extractSeries("MX25L12835F"));
            assertEquals("", handler.extractSeries("MT41K256M16TW"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @CsvSource({
            "M12L128168A-6TG, TSOP-II",
            "M12L64164A-5TG, TSOP-II",
            "M14D5121632A-2.5BG, BGA",
            "M14D5121632A-25BG, BGA",
            "F49L160UA-70TG, TSOP-II"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package extraction for " + mpn);
        }

        @Test
        void shouldHandleEmptyAndNull() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }

        @Test
        void documentPackageCodes() {
            System.out.println("ESMT Package Codes:");
            System.out.println("A = TSOP (Thin Small Outline Package)");
            System.out.println("B = BGA (Ball Grid Array)");
            System.out.println("TG = TSOP-II");
            System.out.println("WG = WBGA (Window BGA)");
            System.out.println("BG = BGA");
            System.out.println("PA/PAIG = SOP (Small Outline Package)");
        }
    }

    @Nested
    @DisplayName("Density Extraction")
    class DensityExtractionTests {

        @ParameterizedTest
        @CsvSource({
            "M12L128168A-6TG, 128",
            "M12L64164A-5TG, 64",
            "M12L32164A-5TG, 32",
            "M14D5121632A-2.5BG, 512",
            "M14D2561616A-25BG, 256",
            "M14D1281616A-25BG, 128",
            "F25L004A-100PAIG, 4",
            "F25L008A-100PAIG, 8",
            "F25L016A-100PAIG, 16",
            "F49L160UA-70TG, 16",
            "F49L320UA-70TG, 32"
        })
        void shouldExtractDensity(String mpn, String expectedDensity) {
            assertEquals(expectedDensity, handler.extractDensity(mpn),
                    "Density extraction for " + mpn);
        }

        @Test
        void shouldHandleEmptyAndNull() {
            assertEquals("", handler.extractDensity(null));
            assertEquals("", handler.extractDensity(""));
        }
    }

    @Nested
    @DisplayName("Configuration Extraction")
    class ConfigurationExtractionTests {

        @ParameterizedTest
        @CsvSource({
            "M12L128168A-6TG, 8Mx16x8",
            "M12L64164A-5TG, 4Mx16x4"
        })
        void shouldExtractConfiguration(String mpn, String expectedConfig) {
            assertEquals(expectedConfig, handler.extractConfiguration(mpn),
                    "Configuration extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForFlash() {
            // Flash memory doesn't have bank configuration in the same way
            assertEquals("", handler.extractConfiguration("F25L004A-100PAIG"));
            assertEquals("", handler.extractConfiguration("F49L160UA-70TG"));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {

        @Test
        void shouldAllowSameSeriesSameDensityReplacement() {
            // Same series, same density, different speed = valid replacement
            assertTrue(handler.isOfficialReplacement(
                "M12L128168A-6TG", "M12L128168A-7TG"),
                "Same SDRAM with different speed should be replacement");

            assertTrue(handler.isOfficialReplacement(
                "F25L016A-100PAIG", "F25L016A-50PAIG"),
                "Same SPI Flash with different speed should be replacement");
        }

        @Test
        void shouldNotAllowDifferentSeriesReplacement() {
            // Different series = not a replacement
            assertFalse(handler.isOfficialReplacement(
                "M12L128168A-6TG", "M14D5121632A-2.5BG"),
                "SDRAM should not replace DDR SDRAM");

            assertFalse(handler.isOfficialReplacement(
                "F25L016A-100PAIG", "F49L160UA-70TG"),
                "SPI Flash should not replace Parallel Flash");
        }

        @Test
        void shouldNotAllowDifferentDensityReplacement() {
            // Different density = not a replacement
            assertFalse(handler.isOfficialReplacement(
                "M12L128168A-6TG", "M12L64164A-6TG"),
                "Different density SDRAM should not be replacement");

            assertFalse(handler.isOfficialReplacement(
                "F25L004A-100PAIG", "F25L016A-100PAIG"),
                "Different density SPI Flash should not be replacement");
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "M12L128168A-6TG"));
            assertFalse(handler.isOfficialReplacement("M12L128168A-6TG", null));
            assertFalse(handler.isOfficialReplacement(null, null));
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
            assertEquals("", handler.extractDensity(null));
            assertEquals("", handler.extractConfiguration(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractDensity(""));
            assertEquals("", handler.extractConfiguration(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("M12L128168A-6TG", null, registry));
        }

        @Test
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("m12l128168a-6tg", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("f25l004a-100paig", ComponentType.MEMORY_FLASH, registry));
        }

        @Test
        void shouldNotMatchOtherManufacturerParts() {
            assertFalse(handler.matches("W25Q32JVSSIQ", ComponentType.MEMORY, registry));
            assertFalse(handler.matches("MX25L12835F", ComponentType.MEMORY_FLASH, registry));
            assertFalse(handler.matches("MT41K256M16TW", ComponentType.MEMORY, registry));
            assertFalse(handler.matches("AT25SF128A", ComponentType.MEMORY_FLASH, registry));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        void shouldUseSupportedTypes() {
            var supportedTypes = handler.getSupportedTypes();
            assertTrue(supportedTypes.contains(ComponentType.MEMORY));
            assertTrue(supportedTypes.contains(ComponentType.MEMORY_FLASH));
            assertTrue(supportedTypes.contains(ComponentType.IC));
        }

        @Test
        void shouldUseImmutableSet() {
            var supportedTypes = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                supportedTypes.add(ComponentType.CAPACITOR);
            }, "getSupportedTypes() should return immutable Set.of()");
        }

        @Test
        void shouldHaveExpectedTypeCount() {
            assertEquals(3, handler.getSupportedTypes().size(),
                    "Should support exactly 3 types: MEMORY, MEMORY_FLASH, IC");
        }
    }

    @Nested
    @DisplayName("Speed Grade Codes")
    class SpeedGradeTests {

        @Test
        void documentSpeedGrades() {
            System.out.println("ESMT Speed Grade Codes:");
            System.out.println("SDRAM (M12L series):");
            System.out.println("  -5 = 5ns access time (200MHz)");
            System.out.println("  -6 = 6ns access time (166MHz)");
            System.out.println("  -7 = 7ns access time (143MHz)");
            System.out.println("");
            System.out.println("DDR SDRAM (M14D series):");
            System.out.println("  -2.5 = 2.5ns (400MHz DDR-400)");
            System.out.println("  -25 = 2.5ns (400MHz DDR-400)");
            System.out.println("");
            System.out.println("SPI Flash (F25L series):");
            System.out.println("  -100 = 100MHz SPI clock");
            System.out.println("  -50 = 50MHz SPI clock");
            System.out.println("");
            System.out.println("Parallel Flash (F49L series):");
            System.out.println("  -55 = 55ns access time");
            System.out.println("  -70 = 70ns access time");
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {

        @Test
        void documentProductFamilies() {
            System.out.println("ESMT Product Families:");
            System.out.println("M12Lxxxx = Synchronous DRAM (SDRAM)");
            System.out.println("M14Dxxxx = DDR SDRAM");
            System.out.println("F25Lxxx = SPI NOR Flash");
            System.out.println("F49Lxxx = Parallel NOR Flash");
        }

        @Test
        void documentTypicalApplications() {
            System.out.println("Typical Applications:");
            System.out.println("SDRAM (M12L): Embedded systems, consumer electronics, networking");
            System.out.println("DDR SDRAM (M14D): Higher-performance embedded, industrial");
            System.out.println("SPI Flash (F25L): Code storage, configuration data, boot memory");
            System.out.println("Parallel Flash (F49L): Legacy systems, high-speed code execution");
        }
    }

    @Nested
    @DisplayName("Cross-Manufacturer Comparison")
    class CrossManufacturerTests {

        @Test
        void documentEquivalentParts() {
            System.out.println("Cross-Manufacturer Equivalent Parts:");
            System.out.println("ESMT M12L128168A ~ Samsung K4S561632N ~ Micron MT48LC");
            System.out.println("ESMT F25L016A ~ Winbond W25Q16 ~ Macronix MX25L16");
            System.out.println("Note: Verify pin compatibility and timing specs before substitution");
        }
    }
}
