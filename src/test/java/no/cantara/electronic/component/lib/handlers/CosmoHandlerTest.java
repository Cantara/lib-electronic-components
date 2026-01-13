package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.CosmoHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for CosmoHandler.
 *
 * Cosmo Electronics (Taiwan) is a manufacturer specializing in optocouplers and photo interrupters.
 *
 * Key product lines:
 * - KP series: Phototransistor optocouplers (KP1010, KP1020, KP2010, KP4010)
 * - KPC series: High isolation optocouplers (KPC817, KPC357)
 * - KPH series: High speed optocouplers (KPH121, KPH141)
 * - KPS series: SMD optocouplers (KPS1010, KPS2010)
 * - KPTR series: Reflective sensors (KPTR1200, KPTR1201)
 *
 * Key MPN patterns:
 * - KP1010-1: Phototransistor optocoupler, DIP package
 * - KPC817C: High isolation optocoupler, CTR grade C
 * - KPH121-1: High speed optocoupler
 * - KPS1010S: SMD optocoupler
 * - KPTR1200: Reflective sensor
 */
class CosmoHandlerTest {

    private static CosmoHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new CosmoHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // KP Series Tests (Phototransistor Optocouplers)
    // ========================================================================

    @Nested
    @DisplayName("KP Series (Phototransistor Optocouplers)")
    class KPSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect KP1010 series (single channel)")
        @ValueSource(strings = {
                "KP1010",
                "KP1010-1",
                "KP1010C",
                "KP1010S",
                "KP1010-1-F",
                "KP1010-TR"
        })
        void shouldDetectKP1010Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA, registry),
                    mpn + " should match OPTOCOUPLER_TOSHIBA");
        }

        @ParameterizedTest
        @DisplayName("Should detect KP1020 series")
        @ValueSource(strings = {
                "KP1020",
                "KP1020-1",
                "KP1020C"
        })
        void shouldDetectKP1020Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect KP2010 series (dual channel)")
        @ValueSource(strings = {
                "KP2010",
                "KP2010-1",
                "KP2010C",
                "KP2010S"
        })
        void shouldDetectKP2010Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect KP4010 series (quad channel)")
        @ValueSource(strings = {
                "KP4010",
                "KP4010-1",
                "KP4010C"
        })
        void shouldDetectKP4010Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("KP series should return correct product category")
        void kpSeriesShouldReturnCorrectCategory() {
            assertEquals("Phototransistor Optocoupler", handler.getProductCategory("KP1010-1"));
            assertEquals("Phototransistor Optocoupler", handler.getProductCategory("KP2010C"));
            assertEquals("Phototransistor Optocoupler", handler.getProductCategory("KP4010"));
        }

        @Test
        @DisplayName("KP series should extract correct series")
        void kpSeriesShouldExtractCorrectSeries() {
            assertEquals("KP1010", handler.extractSeries("KP1010-1"));
            assertEquals("KP2010", handler.extractSeries("KP2010C"));
            assertEquals("KP4010", handler.extractSeries("KP4010-1-F"));
        }
    }

    // ========================================================================
    // KPC Series Tests (High Isolation Optocouplers)
    // ========================================================================

    @Nested
    @DisplayName("KPC Series (High Isolation Optocouplers)")
    class KPCSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect KPC817 series with CTR grades")
        @ValueSource(strings = {
                "KPC817",
                "KPC817A",
                "KPC817B",
                "KPC817C",
                "KPC817D",
                "KPC817C-1",
                "KPC817NT",
                "KPC817CS",
                "KPC817-1"
        })
        void shouldDetectKPC817Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA, registry),
                    mpn + " should match OPTOCOUPLER_TOSHIBA");
        }

        @ParameterizedTest
        @DisplayName("Should detect KPC357 series (Darlington)")
        @ValueSource(strings = {
                "KPC357",
                "KPC357N",
                "KPC357NT",
                "KPC357NT-1"
        })
        void shouldDetectKPC357Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("KPC series should return correct product category")
        void kpcSeriesShouldReturnCorrectCategory() {
            assertEquals("High Isolation Optocoupler", handler.getProductCategory("KPC817C"));
            assertEquals("High Isolation Optocoupler", handler.getProductCategory("KPC357NT"));
        }

        @Test
        @DisplayName("KPC series should extract correct series")
        void kpcSeriesShouldExtractCorrectSeries() {
            assertEquals("KPC817", handler.extractSeries("KPC817C"));
            assertEquals("KPC817", handler.extractSeries("KPC817C-1"));
            assertEquals("KPC357", handler.extractSeries("KPC357NT"));
        }

        @Test
        @DisplayName("Should extract CTR grade from KPC817")
        void shouldExtractCTRGrade() {
            assertEquals("A", handler.extractCTRGrade("KPC817A"));
            assertEquals("B", handler.extractCTRGrade("KPC817B"));
            assertEquals("C", handler.extractCTRGrade("KPC817C"));
            assertEquals("D", handler.extractCTRGrade("KPC817D"));
            assertEquals("", handler.extractCTRGrade("KPC817"));  // No grade specified
        }
    }

    // ========================================================================
    // KPH Series Tests (High Speed Optocouplers)
    // ========================================================================

    @Nested
    @DisplayName("KPH Series (High Speed Optocouplers)")
    class KPHSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect KPH121 series (single channel)")
        @ValueSource(strings = {
                "KPH121",
                "KPH121-1",
                "KPH121S"
        })
        void shouldDetectKPH121Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA, registry),
                    mpn + " should match OPTOCOUPLER_TOSHIBA");
        }

        @ParameterizedTest
        @DisplayName("Should detect KPH141 series (dual channel)")
        @ValueSource(strings = {
                "KPH141",
                "KPH141-1",
                "KPH141S"
        })
        void shouldDetectKPH141Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("KPH series should return correct product category")
        void kphSeriesShouldReturnCorrectCategory() {
            assertEquals("High Speed Optocoupler", handler.getProductCategory("KPH121-1"));
            assertEquals("High Speed Optocoupler", handler.getProductCategory("KPH141S"));
        }

        @Test
        @DisplayName("KPH series should extract correct series")
        void kphSeriesShouldExtractCorrectSeries() {
            assertEquals("KPH121", handler.extractSeries("KPH121-1"));
            assertEquals("KPH141", handler.extractSeries("KPH141S"));
        }
    }

    // ========================================================================
    // KPS Series Tests (SMD Optocouplers)
    // ========================================================================

    @Nested
    @DisplayName("KPS Series (SMD Optocouplers)")
    class KPSSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect KPS1010 series")
        @ValueSource(strings = {
                "KPS1010",
                "KPS1010S",
                "KPS1010C",
                "KPS1010-TR"
        })
        void shouldDetectKPS1010Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA, registry),
                    mpn + " should match OPTOCOUPLER_TOSHIBA");
        }

        @ParameterizedTest
        @DisplayName("Should detect KPS2010 series")
        @ValueSource(strings = {
                "KPS2010",
                "KPS2010S",
                "KPS2010C"
        })
        void shouldDetectKPS2010Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("KPS series should return correct product category")
        void kpsSeriesShouldReturnCorrectCategory() {
            assertEquals("SMD Optocoupler", handler.getProductCategory("KPS1010S"));
            assertEquals("SMD Optocoupler", handler.getProductCategory("KPS2010C"));
        }

        @Test
        @DisplayName("KPS series should extract correct series")
        void kpsSeriesShouldExtractCorrectSeries() {
            assertEquals("KPS1010", handler.extractSeries("KPS1010S"));
            assertEquals("KPS2010", handler.extractSeries("KPS2010C"));
        }

        @Test
        @DisplayName("KPS series should always return SMD package")
        void kpsSeriesShouldAlwaysReturnSMDPackage() {
            assertEquals("SMD", handler.extractPackageCode("KPS1010"));
            assertEquals("SMD", handler.extractPackageCode("KPS1010S"));
            assertEquals("SMD", handler.extractPackageCode("KPS2010C"));
        }
    }

    // ========================================================================
    // KPTR Series Tests (Reflective Sensors)
    // ========================================================================

    @Nested
    @DisplayName("KPTR Series (Reflective Sensors)")
    class KPTRSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect KPTR1200 series")
        @ValueSource(strings = {
                "KPTR1200",
                "KPTR1201",
                "KPTR1200-TR"
        })
        void shouldDetectKPTRSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
        }

        @Test
        @DisplayName("KPTR series should NOT match optocoupler type")
        void kptrSeriesShouldNotMatchOptocoupler() {
            assertFalse(handler.matches("KPTR1200", ComponentType.OPTOCOUPLER_TOSHIBA, registry),
                    "KPTR is a sensor, not optocoupler");
        }

        @Test
        @DisplayName("KPTR series should return correct product category")
        void kptrSeriesShouldReturnCorrectCategory() {
            assertEquals("Reflective Sensor", handler.getProductCategory("KPTR1200"));
            assertEquals("Reflective Sensor", handler.getProductCategory("KPTR1201"));
        }

        @Test
        @DisplayName("KPTR series should extract correct series")
        void kptrSeriesShouldExtractCorrectSeries() {
            assertEquals("KPTR1200", handler.extractSeries("KPTR1200"));
            assertEquals("KPTR1201", handler.extractSeries("KPTR1201"));
        }
    }

    // ========================================================================
    // Package Code Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract DIP-4 package for -1 suffix")
        @CsvSource({
                "KP1010-1, DIP-4",
                "KPC817-1, DIP-4",
                "KPC817C-1, DIP-4",
                "KPH121-1, DIP-4"
        })
        void shouldExtractDIP4Package(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SMD package for S/C suffix")
        @CsvSource({
                "KP1010S, SMD",
                "KP1010C, SMD",
                "KPS1010, SMD",
                "KPS2010S, SMD",
                "KPTR1200, SMD"
        })
        void shouldExtractSMDPackage(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract default DIP-4 for KP/KPC without package suffix")
        @CsvSource({
                "KP1010, DIP-4",
                "KP2010, DIP-4",
                "KPC817, DIP-4",
                "KPC817C, DIP-4"
        })
        void shouldExtractDefaultDIPPackage(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle null and empty inputs")
        void shouldHandleNullAndEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    // ========================================================================
    // Series Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from various MPNs")
        @CsvSource({
                "KP1010-1, KP1010",
                "KP2010C, KP2010",
                "KP4010-1-F, KP4010",
                "KPC817C, KPC817",
                "KPC817C-1, KPC817",
                "KPC357NT, KPC357",
                "KPH121-1, KPH121",
                "KPH141S, KPH141",
                "KPS1010S, KPS1010",
                "KPS2010C, KPS2010",
                "KPTR1200, KPTR1200",
                "KPTR1201, KPTR1201"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should handle null and empty inputs")
        void shouldHandleNullAndEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    // ========================================================================
    // Mounting Type Tests
    // ========================================================================

    @Nested
    @DisplayName("Mounting Type Detection")
    class MountingTypeTests {

        @ParameterizedTest
        @DisplayName("Should detect through-hole mounting")
        @CsvSource({
                "KP1010-1, Through-hole",
                "KPC817-1, Through-hole",
                "KPC817C-1, Through-hole"
        })
        void shouldDetectThroughHoleMounting(String mpn, String expected) {
            assertEquals(expected, handler.getMountingType(mpn),
                    "Mounting type for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should detect SMD mounting")
        @CsvSource({
                "KP1010S, SMD",
                "KP1010C, SMD",
                "KPS1010, SMD",
                "KPS2010S, SMD",
                "KPTR1200, SMD"
        })
        void shouldDetectSMDMounting(String mpn, String expected) {
            assertEquals(expected, handler.getMountingType(mpn),
                    "Mounting type for " + mpn);
        }
    }

    // ========================================================================
    // Channel Count Tests
    // ========================================================================

    @Nested
    @DisplayName("Channel Count Detection")
    class ChannelCountTests {

        @ParameterizedTest
        @DisplayName("Should detect single channel optocouplers")
        @CsvSource({
                "KP1010-1, 1",
                "KPC817C, 1",
                "KPH121-1, 1",
                "KPS1010S, 1"
        })
        void shouldDetectSingleChannel(String mpn, int expected) {
            assertEquals(expected, handler.getChannelCount(mpn),
                    "Channel count for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should detect dual channel optocouplers")
        @CsvSource({
                "KP2010-1, 2",
                "KP2010C, 2",
                "KPS2010S, 2",
                "KPH141-1, 2"
        })
        void shouldDetectDualChannel(String mpn, int expected) {
            assertEquals(expected, handler.getChannelCount(mpn),
                    "Channel count for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should detect quad channel optocouplers")
        @CsvSource({
                "KP4010-1, 4",
                "KP4010C, 4"
        })
        void shouldDetectQuadChannel(String mpn, int expected) {
            assertEquals(expected, handler.getChannelCount(mpn),
                    "Channel count for " + mpn);
        }
    }

    // ========================================================================
    // Official Replacement Tests
    // ========================================================================

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with different variants should be replacements")
        void sameSeriesToDifferentVariantsShouldBeReplacements() {
            // Same KP1010 series, DIP variants
            assertTrue(handler.isOfficialReplacement("KP1010-1", "KP1010-1-F"),
                    "Same series DIP variants should be replacements");
        }

        @Test
        @DisplayName("Same series but different packages should NOT be replacements")
        void sameSeriesToDifferentPackagesShouldNotBeReplacements() {
            // KP1010 DIP vs SMD
            assertFalse(handler.isOfficialReplacement("KP1010-1", "KP1010S"),
                    "DIP and SMD packages are not compatible");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("KP1010-1", "KP2010-1"),
                    "Different series should not be replacements");
            assertFalse(handler.isOfficialReplacement("KPC817C", "KPC357N"),
                    "KPC817 and KPC357 are different series");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsShouldReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "KP1010-1"));
            assertFalse(handler.isOfficialReplacement("KP1010-1", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }

        @Test
        @DisplayName("Same KPC817 with different CTR grades should be replacements (same package)")
        void sameSeriesToDifferentCTRShouldBeReplacements() {
            // Same KPC817 series with different CTR grades
            assertTrue(handler.isOfficialReplacement("KPC817A", "KPC817B"),
                    "Same series with different CTR grades should be replacements");
            assertTrue(handler.isOfficialReplacement("KPC817C", "KPC817D"),
                    "Same series with different CTR grades should be replacements");
        }
    }

    // ========================================================================
    // Edge Cases and Null Handling
    // ========================================================================

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractCTRGrade(null));
            assertEquals("", handler.getMountingType(null));
            assertEquals("", handler.getProductCategory(null));
            assertEquals(0, handler.getChannelCount(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractCTRGrade(""));
            assertEquals("", handler.getMountingType(""));
            assertEquals("", handler.getProductCategory(""));
            assertEquals(0, handler.getChannelCount(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("KPC817C", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("kpc817c", ComponentType.IC, registry));
            assertTrue(handler.matches("KPC817C", ComponentType.IC, registry));
            assertTrue(handler.matches("Kpc817C", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // getSupportedTypes() Tests
    // ========================================================================

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should include all expected component types")
        void shouldIncludeAllExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC), "Should support IC");
            assertTrue(types.contains(ComponentType.OPTOCOUPLER_TOSHIBA), "Should support OPTOCOUPLER_TOSHIBA");
            assertTrue(types.contains(ComponentType.SENSOR), "Should support SENSOR");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes should return an immutable set");
        }
    }

    // ========================================================================
    // Non-Matching Tests
    // ========================================================================

    @Nested
    @DisplayName("Non-Matching MPNs")
    class NonMatchingTests {

        @ParameterizedTest
        @DisplayName("Should not match non-Cosmo components")
        @ValueSource(strings = {
                "EL817",              // Everlight optocoupler
                "TLP621",             // Toshiba optocoupler
                "PC817",              // Sharp optocoupler
                "4N35",               // Generic optocoupler
                "6N137",              // Generic high-speed optocoupler
                "LM7805",             // TI regulator
                "STM32F103",          // ST microcontroller
                "GRM188R71H104KA93",  // Murata capacitor
                "RC0603FR-0710KL"     // Yageo resistor
        })
        void shouldNotMatchNonCosmoParts(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should NOT match IC for Cosmo handler");
            assertFalse(handler.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA, registry),
                    mpn + " should NOT match OPTOCOUPLER for Cosmo handler");
        }

        @Test
        @DisplayName("Should not match unrelated component types")
        void shouldNotMatchUnrelatedTypes() {
            assertFalse(handler.matches("KPC817C", ComponentType.RESISTOR, registry));
            assertFalse(handler.matches("KPC817C", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("KPC817C", ComponentType.MICROCONTROLLER, registry));
            assertFalse(handler.matches("KPC817C", ComponentType.LED, registry));
        }
    }

    // ========================================================================
    // Product Category Tests
    // ========================================================================

    @Nested
    @DisplayName("Product Category Detection")
    class ProductCategoryTests {

        @ParameterizedTest
        @DisplayName("Should return correct product categories")
        @CsvSource({
                "KP1010-1, Phototransistor Optocoupler",
                "KP2010C, Phototransistor Optocoupler",
                "KPC817C, High Isolation Optocoupler",
                "KPC357NT, High Isolation Optocoupler",
                "KPH121-1, High Speed Optocoupler",
                "KPH141S, High Speed Optocoupler",
                "KPS1010S, SMD Optocoupler",
                "KPS2010C, SMD Optocoupler",
                "KPTR1200, Reflective Sensor",
                "KPTR1201, Reflective Sensor"
        })
        void shouldReturnCorrectProductCategory(String mpn, String expected) {
            assertEquals(expected, handler.getProductCategory(mpn),
                    "Product category for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown MPN")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getProductCategory("UNKNOWN123"));
        }
    }

    // ========================================================================
    // Cross-manufacturer Equivalents Tests
    // ========================================================================

    @Nested
    @DisplayName("Cross-Manufacturer Equivalents")
    class CrossManufacturerTests {

        @Test
        @DisplayName("KPC817 is equivalent to PC817/EL817/TLP621")
        void kpc817HasCrossManufacturerEquivalents() {
            // Note: KPC817 is a clone/equivalent of Sharp PC817
            // These are NOT official replacements, but functionally equivalent
            // This test documents the relationship for reference

            String cosmoMpn = "KPC817C";
            assertEquals("KPC817", handler.extractSeries(cosmoMpn));
            assertEquals("High Isolation Optocoupler", handler.getProductCategory(cosmoMpn));
            assertEquals("C", handler.extractCTRGrade(cosmoMpn));
        }
    }
}
