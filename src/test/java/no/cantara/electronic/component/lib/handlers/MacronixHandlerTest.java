package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MacronixHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for MacronixHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction, density extraction,
 * and replacement detection for Macronix Flash Memory ICs.
 * <p>
 * Macronix product families covered:
 * <ul>
 *   <li>MX25Lxxxx - Serial NOR Flash (standard)</li>
 *   <li>MX25Uxxxx - Ultra Low Power Serial Flash</li>
 *   <li>MX25Rxxxx - Wide Voltage Range Serial Flash</li>
 *   <li>MX25Vxxxx - Very Low Voltage Serial Flash</li>
 *   <li>MX66Lxxxx - High Performance Serial Flash</li>
 *   <li>MX29GLxxx - Parallel NOR Flash (high density)</li>
 *   <li>MX29LVxxx - Low Voltage Parallel Flash</li>
 *   <li>MX30LFxxxx - SLC NAND Flash</li>
 * </ul>
 */
class MacronixHandlerTest {

    private static MacronixHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new MacronixHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MX25L Serial NOR Flash Detection")
    class MX25LSerialNORFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect MX25L series standard serial NOR flash")
        @ValueSource(strings = {
                "MX25L6433F",
                "MX25L6433FMI-08G",
                "MX25L12833F",
                "MX25L12835FMI-10G",
                "MX25L25635F",
                "MX25L25635FMI-10G",
                "MX25L51245G",
                "MX25L51245GMI-08G"
        })
        void shouldDetectMX25LFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX25L series should be detected with various suffixes")
        void shouldDetectMX25LWithSuffixes() {
            String[] suffixes = {"", "F", "E", "G", "FMI-08G", "FMI-10G", "EMI-10G", "GMI-08G"};
            for (String suffix : suffixes) {
                String mpn = "MX25L6433" + suffix;
                assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                        mpn + " should match MEMORY");
            }
        }
    }

    @Nested
    @DisplayName("MX25U Ultra Low Power Serial Flash Detection")
    class MX25UUltraLowPowerTests {

        @ParameterizedTest
        @DisplayName("Should detect MX25U series ultra low power flash")
        @ValueSource(strings = {
                "MX25U6435F",
                "MX25U6435FMI-25G",
                "MX25U12835F",
                "MX25U12835FMI-10G",
                "MX25U25635F",
                "MX25U51245G"
        })
        void shouldDetectMX25UFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX25U series should extract correct series")
        void shouldExtractMX25USeries() {
            assertEquals("MX25U", handler.extractSeries("MX25U6435F"));
            assertEquals("MX25U", handler.extractSeries("MX25U12835FMI-10G"));
        }
    }

    @Nested
    @DisplayName("MX25R Wide Voltage Serial Flash Detection")
    class MX25RWideVoltageTests {

        @ParameterizedTest
        @DisplayName("Should detect MX25R series wide voltage flash")
        @ValueSource(strings = {
                "MX25R6435F",
                "MX25R6435FMI-1H",
                "MX25R3235F",
                "MX25R3235FMI-1H",
                "MX25R1635F",
                "MX25R8035F"
        })
        void shouldDetectMX25RFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX25R series should extract correct series")
        void shouldExtractMX25RSeries() {
            assertEquals("MX25R", handler.extractSeries("MX25R6435F"));
            assertEquals("MX25R", handler.extractSeries("MX25R3235FMI-1H"));
        }
    }

    @Nested
    @DisplayName("MX25V Very Low Voltage Serial Flash Detection")
    class MX25VVeryLowVoltageTests {

        @ParameterizedTest
        @DisplayName("Should detect MX25V series very low voltage flash")
        @ValueSource(strings = {
                "MX25V512C",
                "MX25V512CMI",
                "MX25V1006E",
                "MX25V2006E",
                "MX25V4006E",
                "MX25V8006E"
        })
        void shouldDetectMX25VFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX25V series should extract correct series")
        void shouldExtractMX25VSeries() {
            assertEquals("MX25V", handler.extractSeries("MX25V512C"));
            assertEquals("MX25V", handler.extractSeries("MX25V8006E"));
        }
    }

    @Nested
    @DisplayName("MX66L High Performance Serial Flash Detection")
    class MX66LHighPerformanceTests {

        @ParameterizedTest
        @DisplayName("Should detect MX66L series high performance flash")
        @ValueSource(strings = {
                "MX66L51235F",
                "MX66L51235FMI-08G",
                "MX66L1G45G",
                "MX66L1G45GMI-08G",
                "MX66L2G45G"
        })
        void shouldDetectMX66LFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX66L series should extract correct series")
        void shouldExtractMX66LSeries() {
            assertEquals("MX66L", handler.extractSeries("MX66L51235F"));
            assertEquals("MX66L", handler.extractSeries("MX66L1G45GMI-08G"));
        }
    }

    @Nested
    @DisplayName("MX29GL Parallel NOR Flash Detection")
    class MX29GLParallelNORTests {

        @ParameterizedTest
        @DisplayName("Should detect MX29GL series parallel NOR flash")
        @ValueSource(strings = {
                "MX29GL128",
                "MX29GL128FHT2I-90Q",
                "MX29GL256",
                "MX29GL256FHT2I-90Q",
                "MX29GL512",
                "MX29GL512FLT2I-10Q",
                "MX29GL1G"
        })
        void shouldDetectMX29GLFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX29GL series should extract correct series")
        void shouldExtractMX29GLSeries() {
            assertEquals("MX29GL", handler.extractSeries("MX29GL128"));
            assertEquals("MX29GL", handler.extractSeries("MX29GL256FHT2I-90Q"));
        }
    }

    @Nested
    @DisplayName("MX29LV Low Voltage Parallel Flash Detection")
    class MX29LVLowVoltageParallelTests {

        @ParameterizedTest
        @DisplayName("Should detect MX29LV series low voltage parallel flash")
        @ValueSource(strings = {
                "MX29LV640",
                "MX29LV640EBTI-70G",
                "MX29LV160",
                "MX29LV160DBTI-70G",
                "MX29LV320",
                "MX29LV800"
        })
        void shouldDetectMX29LVFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX29LV series should extract correct series")
        void shouldExtractMX29LVSeries() {
            assertEquals("MX29LV", handler.extractSeries("MX29LV640"));
            assertEquals("MX29LV", handler.extractSeries("MX29LV160DBTI-70G"));
        }
    }

    @Nested
    @DisplayName("MX30LF SLC NAND Flash Detection")
    class MX30LFNANDFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect MX30LF series SLC NAND flash")
        @ValueSource(strings = {
                "MX30LF1G18AC",
                "MX30LF1G18AC-TI",
                "MX30LF2G18AC",
                "MX30LF2G18AC-XKI",
                "MX30LF4G18AC",
                "MX30LF1G08AA"
        })
        void shouldDetectMX30LFFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
        }

        @Test
        @DisplayName("MX30LF series should extract correct series")
        void shouldExtractMX30LFSeries() {
            assertEquals("MX30LF", handler.extractSeries("MX30LF1G18AC"));
            assertEquals("MX30LF", handler.extractSeries("MX30LF2G18AC-XKI"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for serial NOR flash")
        @CsvSource({
                "MX25L6433FMI-08G, SOP-8",
                "MX25L12835FMN-10G, DFN-8",
                "MX25L25635FZI-10G, WSON-8",
                "MX25L6433FSI-08G, SOIC-8"
        })
        void shouldExtractSerialNORPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes for parallel NOR flash")
        @CsvSource({
                "MX29GL256FHT2I-90Q, TSOP-48"
        })
        void shouldExtractParallelNORPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes for NAND flash")
        @CsvSource({
                "MX30LF1G18AC, TSOP-48",
                "MX30LF2G18AC, TSOP-48"
        })
        void shouldExtractNANDPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MPN without recognized package code")
        void shouldReturnEmptyForUnrecognizedPackage() {
            assertEquals("", handler.extractPackageCode("MX25L6433F"));
            assertEquals("", handler.extractPackageCode("MX29GL128"));
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series for all product families")
        @CsvSource({
                "MX25L6433F, MX25L",
                "MX25L12835FMI-10G, MX25L",
                "MX25U6435F, MX25U",
                "MX25R3235F, MX25R",
                "MX25V512C, MX25V",
                "MX66L51235F, MX66L",
                "MX29GL128, MX29GL",
                "MX29GL256FHT2I-90Q, MX29GL",
                "MX29LV640, MX29LV",
                "MX30LF1G18AC, MX30LF"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-Macronix parts")
        void shouldReturnEmptyForNonMacronixParts() {
            assertEquals("", handler.extractSeries("W25Q64"));       // Winbond
            assertEquals("", handler.extractSeries("S25FL128"));     // Spansion
            assertEquals("", handler.extractSeries("MT29F4G08"));    // Micron
            assertEquals("", handler.extractSeries("AT25SF128"));    // Adesto/Dialog
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
            assertEquals("MX25L", handler.extractSeries("mx25l6433f"));
            assertEquals("MX29GL", handler.extractSeries("mx29gl128"));
            assertEquals("MX30LF", handler.extractSeries("mx30lf1g18ac"));
        }
    }

    @Nested
    @DisplayName("Density Extraction")
    class DensityExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract density for serial NOR flash")
        @CsvSource({
                "MX25L6433F, 64",
                "MX25L12833F, 128",
                "MX25L12835FMI-10G, 128",
                "MX25L25635F, 256",
                "MX25L51245G, 512"
        })
        void shouldExtractSerialNORDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract density for parallel NOR flash")
        @CsvSource({
                "MX29GL128, 128",
                "MX29GL256, 256",
                "MX29GL256FHT2I-90Q, 256",
                "MX29GL512, 512"
        })
        void shouldExtractParallelNORDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract density for low voltage parallel flash")
        @CsvSource({
                "MX29LV640, 64",
                "MX29LV160, 16",
                "MX29LV320, 32"
        })
        void shouldExtractLVParallelDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract density for NAND flash")
        @CsvSource({
                "MX30LF1G18AC, 1G",
                "MX30LF2G18AC, 2G",
                "MX30LF4G18AC, 4G"
        })
        void shouldExtractNANDDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractDensity(null));
            assertEquals("", handler.extractDensity(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series same density should be replacements")
        void sameSerieSameDensityShouldBeReplacements() {
            // Same MX25L with same density (64Mb), different packages
            assertTrue(handler.isOfficialReplacement("MX25L6433FMI-08G", "MX25L6433FMN-08G"),
                    "Same series same density different package should be replacement");

            // Same MX29GL with same density (256Mb), different suffixes
            assertTrue(handler.isOfficialReplacement("MX29GL256FHT2I-90Q", "MX29GL256FLT2I-90Q"),
                    "Same series same density should be replacement");

            // Same MX30LF with same density (1Gbit), different configurations
            assertTrue(handler.isOfficialReplacement("MX30LF1G18AC", "MX30LF1G08AA"),
                    "Same series same density should be replacement");
        }

        @Test
        @DisplayName("Same series different density should NOT be replacements")
        void sameSerieDifferentDensityNotReplacements() {
            assertFalse(handler.isOfficialReplacement("MX25L6433F", "MX25L12833F"),
                    "64Mb and 128Mb should NOT be replacements");

            assertFalse(handler.isOfficialReplacement("MX29GL128", "MX29GL256"),
                    "128Mb and 256Mb should NOT be replacements");

            assertFalse(handler.isOfficialReplacement("MX30LF1G18AC", "MX30LF2G18AC"),
                    "1Gb and 2Gb should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            // Serial vs Parallel
            assertFalse(handler.isOfficialReplacement("MX25L12833F", "MX29GL128"),
                    "Serial and Parallel should NOT be replacements");

            // Different serial families
            assertFalse(handler.isOfficialReplacement("MX25L6433F", "MX25U6435F"),
                    "MX25L and MX25U should NOT be replacements");

            // Serial vs NAND
            assertFalse(handler.isOfficialReplacement("MX25L6433F", "MX30LF1G18AC"),
                    "Serial NOR and NAND should NOT be replacements");
        }

        @Test
        @DisplayName("Cross-manufacturer parts should NOT be replacements")
        void crossManufacturerNotReplacements() {
            // Macronix vs Winbond
            assertFalse(handler.isOfficialReplacement("MX25L6433F", "W25Q64"),
                    "Macronix and Winbond should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "MX25L6433F"));
            assertFalse(handler.isOfficialReplacement("MX25L6433F", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support MEMORY and MEMORY_FLASH types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY),
                    "Should support MEMORY type");
            assertTrue(types.contains(ComponentType.MEMORY_FLASH),
                    "Should support MEMORY_FLASH type");
        }

        @Test
        @DisplayName("getSupportedTypes() should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.IC);
            }, "Set should be immutable");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractDensity(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractDensity(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("MX25L6433F", null, registry));
        }

        @Test
        @DisplayName("Should NOT match non-Macronix patterns")
        void shouldNotMatchNonMacronixPatterns() {
            // Other flash manufacturers
            assertFalse(handler.matches("W25Q128", ComponentType.MEMORY, registry));      // Winbond
            assertFalse(handler.matches("S25FL256", ComponentType.MEMORY, registry));     // Spansion
            assertFalse(handler.matches("MT25Q512", ComponentType.MEMORY, registry));     // Micron
            assertFalse(handler.matches("IS25LP128", ComponentType.MEMORY, registry));    // ISSI
            assertFalse(handler.matches("AT25SF128", ComponentType.MEMORY, registry));    // Adesto

            // Other component types
            assertFalse(handler.matches("STM32F103", ComponentType.MICROCONTROLLER, registry)); // ST MCU
            assertFalse(handler.matches("LM358", ComponentType.IC, registry));            // TI Op-Amp
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("mx25l6433f", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("mx29gl128", ComponentType.MEMORY_FLASH, registry));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Mx25L6433F", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("mX29gL256", ComponentType.MEMORY_FLASH, registry));
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {

        @Test
        @DisplayName("Document Macronix product families")
        void documentProductFamilies() {
            System.out.println("Macronix Product Families:");
            System.out.println("MX25Lxxxx = Standard Serial NOR Flash (1.8V/3V)");
            System.out.println("MX25Uxxxx = Ultra Low Power Serial Flash (1.65-2V)");
            System.out.println("MX25Rxxxx = Wide Voltage Serial Flash (1.65-3.6V)");
            System.out.println("MX25Vxxxx = Very Low Voltage Serial Flash (1.65-1.95V)");
            System.out.println("MX66Lxxxx = High Performance Serial Flash (DTR/QPI)");
            System.out.println("MX29GLxxx = Parallel NOR Flash (high density, 3V)");
            System.out.println("MX29LVxxx = Low Voltage Parallel Flash (3V/1.8V)");
            System.out.println("MX30LFxxxx = SLC NAND Flash");
        }

        @Test
        @DisplayName("Document density encoding")
        void documentDensityEncoding() {
            System.out.println("Macronix Serial NOR Density Encoding:");
            System.out.println("6433/6435 = 64 Mbit");
            System.out.println("12833/12835 = 128 Mbit");
            System.out.println("25635/25645 = 256 Mbit");
            System.out.println("51235/51245 = 512 Mbit");
            System.out.println("1G = 1 Gbit");
            System.out.println("2G = 2 Gbit");
            System.out.println("");
            System.out.println("Macronix Parallel NOR Density (direct):");
            System.out.println("128 = 128 Mbit");
            System.out.println("256 = 256 Mbit");
            System.out.println("512 = 512 Mbit");
            System.out.println("");
            System.out.println("Macronix Low Voltage Parallel (scaled):");
            System.out.println("640 = 64 Mbit");
            System.out.println("160 = 16 Mbit");
            System.out.println("320 = 32 Mbit");
        }

        @Test
        @DisplayName("Document package codes")
        void documentPackageCodes() {
            System.out.println("Macronix Package Codes:");
            System.out.println("MI = SOP-8");
            System.out.println("MN = DFN-8");
            System.out.println("ZI = WSON-8");
            System.out.println("SI = SOIC-8");
            System.out.println("SS = SOIC-8");
            System.out.println("DI = SOIC-16");
            System.out.println("TI = TSOP-8");
            System.out.println("HT = TSOP-48");
            System.out.println("AC = TSOP-48 (NAND)");
            System.out.println("BH = BGA");
            System.out.println("FH = FBGA");
            System.out.println("LI = WLCSP");
        }
    }

    @Nested
    @DisplayName("Temperature Grade Detection")
    class TemperatureGradeTests {

        @Test
        @DisplayName("Document temperature grade suffixes")
        void documentTemperatureGrades() {
            System.out.println("Macronix Temperature Grade Suffixes:");
            System.out.println("-08 or no suffix = Commercial (0 to +70C)");
            System.out.println("-10 = Industrial (-40 to +85C)");
            System.out.println("-1H = Industrial (-40 to +85C, alternate)");
            System.out.println("-25 = Industrial extended");
            System.out.println("-90 = Automotive (-40 to +105C)");
            System.out.println("Q suffix (e.g., -90Q) = Automotive qualified");
            System.out.println("G suffix (e.g., -08G) = Green/RoHS compliant");
        }
    }
}
