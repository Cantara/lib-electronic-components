package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.PuyaHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for PuyaHandler.
 * Tests SPI NOR Flash detection for P25Q, P25D, and PY25Q series.
 */
class PuyaHandlerTest {

    private static PuyaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new PuyaHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        void shouldReturnCorrectSupportedTypes() {
            Set<ComponentType> types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY), "Should support MEMORY");
            assertTrue(types.contains(ComponentType.MEMORY_FLASH), "Should support MEMORY_FLASH");
            assertTrue(types.contains(ComponentType.IC), "Should support IC");
            assertEquals(3, types.size(), "Should have exactly 3 supported types");
        }

        @Test
        void shouldUseImmutableSet() {
            Set<ComponentType> types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.CAPACITOR),
                    "getSupportedTypes() should return immutable Set");
        }
    }

    @Nested
    @DisplayName("P25Q Series - Standard SPI NOR Flash")
    class P25QSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "P25Q80H",
                "P25Q16H",
                "P25Q32H",
                "P25Q64H",
                "P25Q128H"
        })
        void shouldMatchMemoryType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "P25Q80H",
                "P25Q16H",
                "P25Q32H",
                "P25Q64H",
                "P25Q128H"
        })
        void shouldMatchMemoryFlashType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "P25Q80H",
                "P25Q16H",
                "P25Q32H",
                "P25Q64H",
                "P25Q128H"
        })
        void shouldMatchICType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "P25Q80H",
                "P25Q16H",
                "P25Q32H",
                "P25Q64H",
                "P25Q128H",
                "P25Q80H-SSH",
                "P25Q32H-SUH"
        })
        void shouldExtractP25QSeries(String mpn) {
            assertEquals("P25Q", handler.extractSeries(mpn),
                    "Should extract P25Q series from " + mpn);
        }
    }

    @Nested
    @DisplayName("P25D Series - Low Power SPI NOR Flash")
    class P25DSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "P25D80H",
                "P25D16H",
                "P25D32H",
                "P25D64H"
        })
        void shouldMatchMemoryType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "P25D80H",
                "P25D16H",
                "P25D32H",
                "P25D64H"
        })
        void shouldMatchMemoryFlashType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "P25D80H",
                "P25D16H",
                "P25D32H",
                "P25D64H"
        })
        void shouldExtractP25DSeries(String mpn) {
            assertEquals("P25D", handler.extractSeries(mpn),
                    "Should extract P25D series from " + mpn);
        }

        @Test
        void shouldExtractLowPowerGrade() {
            assertEquals("Low Power", handler.extractGrade("P25D80H"),
                    "P25D series should be Low Power grade");
        }
    }

    @Nested
    @DisplayName("PY25Q Series - Automotive Grade SPI NOR Flash")
    class PY25QSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "PY25Q80HA",
                "PY25Q16HA",
                "PY25Q32HA",
                "PY25Q64HA",
                "PY25Q128HA"
        })
        void shouldMatchMemoryType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "PY25Q80HA",
                "PY25Q16HA",
                "PY25Q32HA",
                "PY25Q64HA",
                "PY25Q128HA"
        })
        void shouldMatchMemoryFlashType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should match MEMORY_FLASH type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "PY25Q80HA",
                "PY25Q16HA",
                "PY25Q32HA",
                "PY25Q64HA",
                "PY25Q128HA"
        })
        void shouldExtractPY25QSeries(String mpn) {
            assertEquals("PY25Q", handler.extractSeries(mpn),
                    "Should extract PY25Q series from " + mpn);
        }

        @Test
        void shouldExtractAutomotiveGrade() {
            assertEquals("Automotive", handler.extractGrade("PY25Q128HA"),
                    "PY25Q series should be Automotive grade");
        }
    }

    @Nested
    @DisplayName("Density Extraction")
    class DensityExtractionTests {

        @ParameterizedTest
        @CsvSource({
                "P25Q80H, 8",
                "P25Q16H, 16",
                "P25Q32H, 32",
                "P25Q64H, 64",
                "P25Q128H, 128",
                "P25D80H, 8",
                "P25D16H, 16",
                "PY25Q80HA, 8",
                "PY25Q128HA, 128"
        })
        void shouldExtractDensity(String mpn, String expectedDensity) {
            assertEquals(expectedDensity, handler.extractDensity(mpn),
                    "Density extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonPuyaParts() {
            assertEquals("", handler.extractDensity("W25Q32JVSSIQ"));
            assertEquals("", handler.extractDensity("MX25L12835F"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeExtractionTests {

        @ParameterizedTest
        @CsvSource({
                "P25Q80H, SOIC-8",
                "P25Q16H, SOIC-8",
                "P25Q32U, USON-8",
                "P25Q64SH, SOIC-8-WIDE",
                "P25Q128SU, WSON-8"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package extraction for " + mpn);
        }

        @ParameterizedTest
        @CsvSource({
                "P25Q16H-SSH, SOIC-8",
                "P25Q32H-SUH, WSON-8"
        })
        void shouldExtractPackageCodeFromHyphenatedSuffix(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package extraction from hyphenated suffix for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonPuyaParts() {
            assertEquals("", handler.extractPackageCode("W25Q32JVSSIQ"));
            assertEquals("", handler.extractPackageCode("MX25L12835F"));
        }

        @Test
        void shouldHandleUnknownPackageCode() {
            // Unknown suffix should return the raw suffix
            String result = handler.extractPackageCode("P25Q80X");
            assertEquals("X", result, "Unknown package code should return raw suffix");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @CsvSource({
                "P25Q80H, P25Q",
                "P25Q16H-SSH, P25Q",
                "P25Q128H, P25Q",
                "P25D80H, P25D",
                "P25D32H, P25D",
                "PY25Q80HA, PY25Q",
                "PY25Q128HA, PY25Q"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonPuyaParts() {
            assertEquals("", handler.extractSeries("W25Q32JVSSIQ"));
            assertEquals("", handler.extractSeries("MX25L12835F"));
            assertEquals("", handler.extractSeries("GD25Q32C"));
        }
    }

    @Nested
    @DisplayName("Grade Extraction")
    class GradeExtractionTests {

        @ParameterizedTest
        @CsvSource({
                "P25Q80H, Standard",
                "P25Q128H, Standard",
                "P25D80H, Low Power",
                "P25D32H, Low Power",
                "PY25Q80HA, Automotive",
                "PY25Q128HA, Automotive"
        })
        void shouldExtractGrade(String mpn, String expectedGrade) {
            assertEquals(expectedGrade, handler.extractGrade(mpn),
                    "Grade extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNonPuyaParts() {
            assertEquals("", handler.extractGrade("W25Q32JVSSIQ"));
            assertEquals("", handler.extractGrade("MX25L12835F"));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {

        @Test
        void sameDensitySameSeriesShouldBeReplacement() {
            assertTrue(handler.isOfficialReplacement("P25Q80H", "P25Q80U"),
                    "Same series and density with different package should be replacement");
        }

        @Test
        void sameDensityDifferentSeriesShouldNotBeReplacement() {
            assertFalse(handler.isOfficialReplacement("P25Q80H", "P25D80H"),
                    "Different series should not be replacement");
        }

        @Test
        void differentDensityShouldNotBeReplacement() {
            assertFalse(handler.isOfficialReplacement("P25Q80H", "P25Q16H"),
                    "Different density should not be replacement");
            assertFalse(handler.isOfficialReplacement("P25Q32H", "P25Q64H"),
                    "Different density should not be replacement");
        }

        @Test
        void standardShouldNotReplaceAutomotive() {
            assertFalse(handler.isOfficialReplacement("P25Q128H", "PY25Q128HA"),
                    "Standard series should not replace automotive series");
        }

        @Test
        void automotiveShouldNotReplaceStandard() {
            assertFalse(handler.isOfficialReplacement("PY25Q128HA", "P25Q128H"),
                    "Automotive series should not replace standard series");
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "P25Q80H"));
            assertFalse(handler.isOfficialReplacement("P25Q80H", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractDensity(null));
            assertEquals("", handler.extractGrade(null));
        }

        @Test
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractDensity(""));
            assertEquals("", handler.extractGrade(""));
        }

        @Test
        void shouldHandleNullType() {
            assertFalse(handler.matches("P25Q80H", null, registry));
        }

        @Test
        void shouldHandleLowerCaseMpn() {
            assertTrue(handler.matches("p25q80h", ComponentType.MEMORY, registry),
                    "Should handle lowercase MPN");
            assertTrue(handler.matches("p25d80h", ComponentType.MEMORY_FLASH, registry),
                    "Should handle lowercase MPN");
            assertTrue(handler.matches("py25q128ha", ComponentType.IC, registry),
                    "Should handle lowercase MPN");
        }

        @Test
        void shouldHandleMixedCaseMpn() {
            assertTrue(handler.matches("P25q80H", ComponentType.MEMORY, registry),
                    "Should handle mixed case MPN");
            assertTrue(handler.matches("Py25Q128Ha", ComponentType.MEMORY_FLASH, registry),
                    "Should handle mixed case MPN");
        }
    }

    @Nested
    @DisplayName("Non-Puya Parts Should Not Match")
    class NonPuyaPartsTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "W25Q32JVSSIQ",     // Winbond
                "MX25L12835F",      // Macronix
                "GD25Q32C",         // GigaDevice
                "AT25SF128A",       // Atmel/Microchip
                "IS25LP128F",       // ISSI
                "S25FL128L"         // Cypress/Infineon
        })
        void shouldNotMatchOtherManufacturersParts(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should not match MEMORY type for Puya handler");
            assertFalse(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                    mpn + " should not match MEMORY_FLASH type for Puya handler");
        }
    }

    @Nested
    @DisplayName("Product Family Reference")
    class ProductFamilyReferenceTests {

        @Test
        void documentProductFamilies() {
            System.out.println("Puya Semiconductor Product Families:");
            System.out.println("P25Q series - Standard SPI NOR Flash (1.65V-3.6V)");
            System.out.println("P25D series - Low Power SPI NOR Flash");
            System.out.println("PY25Q series - Automotive Grade SPI NOR Flash (AEC-Q100)");
        }

        @Test
        void documentDensityCodes() {
            System.out.println("Puya Density Codes:");
            System.out.println("80 = 8 Mbit (1 MByte)");
            System.out.println("16 = 16 Mbit (2 MByte)");
            System.out.println("32 = 32 Mbit (4 MByte)");
            System.out.println("64 = 64 Mbit (8 MByte)");
            System.out.println("128 = 128 Mbit (16 MByte)");
        }

        @Test
        void documentPackageCodes() {
            System.out.println("Puya Package Codes:");
            System.out.println("H = SOIC-8 (standard 150mil width)");
            System.out.println("U = USON-8 (2x3mm)");
            System.out.println("SH = SOIC-8 wide (208mil width)");
            System.out.println("SU = WSON-8 (5x6mm)");
        }
    }

    @Nested
    @DisplayName("Real World MPN Variations")
    class RealWorldMPNTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "P25Q80H",
                "P25Q80H-SSH",
                "P25Q80H-SUH",
                "P25Q80U",
                "P25Q80SH",
                "P25Q80SU"
        })
        void shouldMatchAllP25Q80Variations(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match");
            assertEquals("P25Q", handler.extractSeries(mpn),
                    "Should extract P25Q series from " + mpn);
            assertEquals("8", handler.extractDensity(mpn),
                    "Should extract 8Mbit density from " + mpn);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "P25Q128H",
                "P25Q128H-SSH",
                "P25Q128U",
                "P25Q128SH",
                "P25Q128SU"
        })
        void shouldMatchAllP25Q128Variations(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match");
            assertEquals("P25Q", handler.extractSeries(mpn),
                    "Should extract P25Q series from " + mpn);
            assertEquals("128", handler.extractDensity(mpn),
                    "Should extract 128Mbit density from " + mpn);
        }
    }

    @Nested
    @DisplayName("Type Detection Completeness")
    class TypeDetectionTests {

        @Test
        void p25qShouldMatchAllSupportedTypes() {
            String mpn = "P25Q80H";
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry));
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry));
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
        }

        @Test
        void p25dShouldMatchAllSupportedTypes() {
            String mpn = "P25D80H";
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry));
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry));
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
        }

        @Test
        void py25qShouldMatchAllSupportedTypes() {
            String mpn = "PY25Q128HA";
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry));
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry));
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
        }

        @Test
        void shouldNotMatchUnsupportedTypes() {
            String mpn = "P25Q80H";
            assertFalse(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    "Flash memory should not match CAPACITOR");
            assertFalse(handler.matches(mpn, ComponentType.RESISTOR, registry),
                    "Flash memory should not match RESISTOR");
            assertFalse(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    "Flash memory should not match MICROCONTROLLER");
        }
    }
}
