package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.XMCHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for XMCHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction, density extraction,
 * and replacement detection for XMC (Wuhan Xinxin Semiconductor) SPI NOR Flash Memory ICs.
 * <p>
 * XMC product families covered:
 * <ul>
 *   <li>XM25QH - Standard 3.3V SPI NOR Flash</li>
 *   <li>XM25QU - 1.8V Low Voltage SPI NOR Flash</li>
 *   <li>XM25LU - Ultra Low Voltage SPI NOR Flash</li>
 * </ul>
 */
class XMCHandlerTest {

    private static XMCHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new XMCHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("XM25QH Standard 3.3V SPI NOR Flash Detection")
    class XM25QHFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect XM25QH series standard SPI NOR flash")
        @ValueSource(strings = {
                "XM25QH64A",
                "XM25QH64B",
                "XM25QH128A",
                "XM25QH128B",
                "XM25QH256A",
                "XM25QH256B",
                "XM25QH512A",
                "XM25QH32C",
                "XM25QH16D"
        })
        void shouldDetectXM25QHFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("XM25QH series should be detected with various suffixes")
        void shouldDetectXM25QHWithSuffixes() {
            String[] suffixes = {"", "A", "B", "C", "D"};
            for (String suffix : suffixes) {
                String mpn = "XM25QH64" + suffix;
                assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                        mpn + " should match MEMORY");
            }
        }

        @Test
        @DisplayName("XM25QH series should extract correct series")
        void shouldExtractXM25QHSeries() {
            assertEquals("XM25QH", handler.extractSeries("XM25QH64A"));
            assertEquals("XM25QH", handler.extractSeries("XM25QH128B"));
            assertEquals("XM25QH", handler.extractSeries("XM25QH256A"));
        }
    }

    @Nested
    @DisplayName("XM25QU 1.8V Low Voltage SPI NOR Flash Detection")
    class XM25QUFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect XM25QU series low voltage SPI NOR flash")
        @ValueSource(strings = {
                "XM25QU64A",
                "XM25QU64B",
                "XM25QU128A",
                "XM25QU128C",
                "XM25QU256A",
                "XM25QU256D",
                "XM25QU32A"
        })
        void shouldDetectXM25QUFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("XM25QU series should extract correct series")
        void shouldExtractXM25QUSeries() {
            assertEquals("XM25QU", handler.extractSeries("XM25QU64A"));
            assertEquals("XM25QU", handler.extractSeries("XM25QU128B"));
            assertEquals("XM25QU", handler.extractSeries("XM25QU256C"));
        }

        @Test
        @DisplayName("XM25QU series should have correct voltage range")
        void shouldHaveCorrectVoltageRange() {
            assertEquals("1.65V-2.0V", handler.getVoltageRange("XM25QU64A"));
            assertEquals("1.65V-2.0V", handler.getVoltageRange("XM25QU128B"));
        }
    }

    @Nested
    @DisplayName("XM25LU Ultra Low Voltage SPI NOR Flash Detection")
    class XM25LUFlashTests {

        @ParameterizedTest
        @DisplayName("Should detect XM25LU series ultra low voltage SPI NOR flash")
        @ValueSource(strings = {
                "XM25LU64",
                "XM25LU64A",
                "XM25LU128",
                "XM25LU128A",
                "XM25LU256",
                "XM25LU256B",
                "XM25LU32"
        })
        void shouldDetectXM25LUFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("XM25LU series should extract correct series")
        void shouldExtractXM25LUSeries() {
            assertEquals("XM25LU", handler.extractSeries("XM25LU64A"));
            assertEquals("XM25LU", handler.extractSeries("XM25LU128"));
            assertEquals("XM25LU", handler.extractSeries("XM25LU256B"));
        }

        @Test
        @DisplayName("XM25LU series should have correct voltage range")
        void shouldHaveCorrectVoltageRange() {
            assertEquals("1.65V-1.95V", handler.getVoltageRange("XM25LU64A"));
            assertEquals("1.65V-1.95V", handler.getVoltageRange("XM25LU128"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes for SPI NOR flash")
        @CsvSource({
                "XM25QH64A, SOIC-8",
                "XM25QH128B, SOIC-16",
                "XM25QU256C, WSON-8",
                "XM25LU64D, USON-8"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MPN without package suffix")
        void shouldReturnEmptyForNoSuffix() {
            assertEquals("", handler.extractPackageCode("XM25QH64"));
            assertEquals("", handler.extractPackageCode("XM25LU128"));
        }

        @Test
        @DisplayName("Should return raw suffix for unrecognized package code")
        void shouldReturnRawSuffixForUnrecognized() {
            // If X is not a known package code, return X as raw suffix
            String result = handler.extractPackageCode("XM25QH64X");
            assertEquals("X", result);
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
                "XM25QH64A, XM25QH",
                "XM25QH128B, XM25QH",
                "XM25QH256A, XM25QH",
                "XM25QU64A, XM25QU",
                "XM25QU128C, XM25QU",
                "XM25LU64A, XM25LU",
                "XM25LU256, XM25LU"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-XMC parts")
        void shouldReturnEmptyForNonXMCParts() {
            assertEquals("", handler.extractSeries("W25Q64"));       // Winbond
            assertEquals("", handler.extractSeries("MX25L12835"));   // Macronix
            assertEquals("", handler.extractSeries("GD25Q128"));     // GigaDevice
            assertEquals("", handler.extractSeries("IS25LP128"));    // ISSI
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
            assertEquals("XM25QH", handler.extractSeries("xm25qh64a"));
            assertEquals("XM25QU", handler.extractSeries("Xm25Qu128B"));
            assertEquals("XM25LU", handler.extractSeries("XM25lu256"));
        }
    }

    @Nested
    @DisplayName("Density Extraction")
    class DensityExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract density for XM25QH series")
        @CsvSource({
                "XM25QH16A, 16",
                "XM25QH32A, 32",
                "XM25QH64A, 64",
                "XM25QH128B, 128",
                "XM25QH256A, 256",
                "XM25QH512A, 512"
        })
        void shouldExtractXM25QHDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract density for XM25QU series")
        @CsvSource({
                "XM25QU32A, 32",
                "XM25QU64A, 64",
                "XM25QU128C, 128",
                "XM25QU256D, 256"
        })
        void shouldExtractXM25QUDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                    "Density for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract density for XM25LU series")
        @CsvSource({
                "XM25LU64, 64",
                "XM25LU64A, 64",
                "XM25LU128, 128",
                "XM25LU256B, 256"
        })
        void shouldExtractXM25LUDensity(String mpn, String expected) {
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
    @DisplayName("Voltage Range Detection")
    class VoltageRangeTests {

        @ParameterizedTest
        @DisplayName("Should return correct voltage range for each series")
        @CsvSource({
                "XM25QH64A, 2.7V-3.6V",
                "XM25QH128B, 2.7V-3.6V",
                "XM25QU64A, 1.65V-2.0V",
                "XM25QU128C, 1.65V-2.0V",
                "XM25LU64A, 1.65V-1.95V",
                "XM25LU256, 1.65V-1.95V"
        })
        void shouldReturnCorrectVoltageRange(String mpn, String expected) {
            assertEquals(expected, handler.getVoltageRange(mpn),
                    "Voltage range for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.getVoltageRange("UNKNOWN123"));
            assertEquals("", handler.getVoltageRange("W25Q64"));
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.getVoltageRange(null));
            assertEquals("", handler.getVoltageRange(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series same density should be replacements")
        void sameSerieSameDensityShouldBeReplacements() {
            // Same XM25QH with same density (64Mb), different packages
            assertTrue(handler.isOfficialReplacement("XM25QH64A", "XM25QH64B"),
                    "Same series same density different package should be replacement");
            assertTrue(handler.isOfficialReplacement("XM25QH64A", "XM25QH64C"),
                    "Same series same density different package should be replacement");

            // Same XM25QU with same density (128Mb)
            assertTrue(handler.isOfficialReplacement("XM25QU128A", "XM25QU128B"),
                    "Same series same density should be replacement");

            // Same XM25LU with same density (256Mb)
            assertTrue(handler.isOfficialReplacement("XM25LU256", "XM25LU256A"),
                    "Same series same density should be replacement");
        }

        @Test
        @DisplayName("Same series different density should NOT be replacements")
        void sameSerieDifferentDensityNotReplacements() {
            assertFalse(handler.isOfficialReplacement("XM25QH64A", "XM25QH128A"),
                    "64Mb and 128Mb should NOT be replacements");

            assertFalse(handler.isOfficialReplacement("XM25QU128A", "XM25QU256A"),
                    "128Mb and 256Mb should NOT be replacements");

            assertFalse(handler.isOfficialReplacement("XM25LU64A", "XM25LU128A"),
                    "64Mb and 128Mb should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            // XM25QH vs XM25QU (different voltage)
            assertFalse(handler.isOfficialReplacement("XM25QH64A", "XM25QU64A"),
                    "XM25QH and XM25QU should NOT be replacements (different voltage)");

            // XM25QH vs XM25LU
            assertFalse(handler.isOfficialReplacement("XM25QH128A", "XM25LU128A"),
                    "XM25QH and XM25LU should NOT be replacements");

            // XM25QU vs XM25LU
            assertFalse(handler.isOfficialReplacement("XM25QU64A", "XM25LU64A"),
                    "XM25QU and XM25LU should NOT be replacements");
        }

        @Test
        @DisplayName("Cross-manufacturer parts should NOT be replacements")
        void crossManufacturerNotReplacements() {
            // XMC vs Winbond
            assertFalse(handler.isOfficialReplacement("XM25QH64A", "W25Q64"),
                    "XMC and Winbond should NOT be replacements");

            // XMC vs Macronix
            assertFalse(handler.isOfficialReplacement("XM25QH128A", "MX25L12835"),
                    "XMC and Macronix should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "XM25QH64A"));
            assertFalse(handler.isOfficialReplacement("XM25QH64A", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support MEMORY, MEMORY_FLASH, and IC types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY),
                    "Should support MEMORY type");
            assertTrue(types.contains(ComponentType.MEMORY_FLASH),
                    "Should support MEMORY_FLASH type");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("getSupportedTypes() should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.MICROCONTROLLER);
            }, "Set should be immutable");
        }

        @Test
        @DisplayName("getSupportedTypes() should have exactly 3 types")
        void shouldHaveExactlyThreeTypes() {
            var types = handler.getSupportedTypes();
            assertEquals(3, types.size(), "Should have exactly 3 supported types");
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
            assertEquals("", handler.getVoltageRange(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractDensity(""));
            assertEquals("", handler.getVoltageRange(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("XM25QH64A", null, registry));
        }

        @Test
        @DisplayName("Should NOT match non-XMC patterns")
        void shouldNotMatchNonXMCPatterns() {
            // Other flash manufacturers
            assertFalse(handler.matches("W25Q128", ComponentType.MEMORY, registry));      // Winbond
            assertFalse(handler.matches("MX25L12835", ComponentType.MEMORY, registry));   // Macronix
            assertFalse(handler.matches("GD25Q64", ComponentType.MEMORY, registry));      // GigaDevice
            assertFalse(handler.matches("IS25LP128", ComponentType.MEMORY, registry));    // ISSI
            assertFalse(handler.matches("S25FL256", ComponentType.MEMORY, registry));     // Spansion

            // Other component types
            assertFalse(handler.matches("STM32F103", ComponentType.MICROCONTROLLER, registry)); // ST MCU
            assertFalse(handler.matches("LM358", ComponentType.IC, registry));            // TI Op-Amp
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("xm25qh64a", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("xm25qu128b", ComponentType.MEMORY_FLASH, registry));
            assertTrue(handler.matches("xm25lu256", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Xm25Qh64A", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("XM25qu128B", ComponentType.MEMORY_FLASH, registry));
            assertTrue(handler.matches("xM25Lu256a", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {

        @Test
        @DisplayName("Document XMC product families")
        void documentProductFamilies() {
            System.out.println("XMC (Wuhan Xinxin Semiconductor) Product Families:");
            System.out.println("XM25QH = Standard 3.3V SPI NOR Flash (2.7V-3.6V)");
            System.out.println("XM25QU = 1.8V Low Voltage SPI NOR Flash (1.65V-2.0V)");
            System.out.println("XM25LU = Ultra Low Voltage SPI NOR Flash (1.65V-1.95V)");
        }

        @Test
        @DisplayName("Document density encoding")
        void documentDensityEncoding() {
            System.out.println("XMC Density Encoding (direct):");
            System.out.println("16 = 16 Mbit (2 MB)");
            System.out.println("32 = 32 Mbit (4 MB)");
            System.out.println("64 = 64 Mbit (8 MB)");
            System.out.println("128 = 128 Mbit (16 MB)");
            System.out.println("256 = 256 Mbit (32 MB)");
            System.out.println("512 = 512 Mbit (64 MB)");
        }

        @Test
        @DisplayName("Document package codes")
        void documentPackageCodes() {
            System.out.println("XMC Package Codes:");
            System.out.println("A = SOIC-8");
            System.out.println("B = SOIC-16");
            System.out.println("C = WSON-8");
            System.out.println("D = USON-8");
        }
    }

    @Nested
    @DisplayName("Comparison with Other Flash Manufacturers")
    class ComparisonTests {

        @Test
        @DisplayName("Document cross-reference with equivalent parts")
        void documentCrossReference() {
            System.out.println("XMC Cross-Reference (equivalent densities):");
            System.out.println("XM25QH64A  ~ W25Q64 (Winbond) ~ MX25L6433F (Macronix) ~ GD25Q64 (GigaDevice)");
            System.out.println("XM25QH128A ~ W25Q128 (Winbond) ~ MX25L12835F (Macronix) ~ GD25Q128 (GigaDevice)");
            System.out.println("XM25QH256A ~ W25Q256 (Winbond) ~ MX25L25635F (Macronix) ~ GD25Q256 (GigaDevice)");
            System.out.println("");
            System.out.println("Note: While these have equivalent densities, they may have");
            System.out.println("differences in command sets, timing, and features.");
        }
    }
}
