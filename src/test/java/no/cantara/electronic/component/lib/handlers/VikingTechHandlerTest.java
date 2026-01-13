package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.VikingTechHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for VikingTechHandler.
 * Tests pattern matching, package code extraction, series extraction,
 * value extraction, and replacement detection for Viking Tech resistors.
 *
 * Viking Tech resistor series:
 * - CR series: Standard chip resistors (CR0603-FX-1001ELF)
 * - AR series: Anti-sulfur resistors (AR0603-FX-1002GLF)
 * - PA series: Power resistors
 * - CSR series: Current sense resistors (CSR0805-0R010F)
 */
class VikingTechHandlerTest {

    private static VikingTechHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new VikingTechHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("CR Series - Standard Chip Resistors")
    class CRSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect CR series resistors")
        @ValueSource(strings = {
                "CR0603-FX-1001ELF",
                "CR0402-FX-1002GLF",
                "CR0805-JX-1003JLF",
                "CR1206-FX-1000ELF",
                "CR0603FR1001"
        })
        void shouldDetectCRResistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RESISTOR, registry),
                    "Should match CR series resistor: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from CR series")
        @CsvSource({
                "CR0603-FX-1001ELF, 0603",
                "CR0402-FX-1002GLF, 0402",
                "CR0805-JX-1003JLF, 0805",
                "CR1206-FX-1000ELF, 1206",
                "CR2512-FX-1001ELF, 2512"
        })
        void shouldExtractCRPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code mismatch for: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from CR resistors")
        @CsvSource({
                "CR0603-FX-1001ELF, CR0603",
                "CR0402-FX-1002GLF, CR0402",
                "CR0805-JX-1003JLF, CR0805",
                "CR1206-FX-1000ELF, CR1206"
        })
        void shouldExtractCRSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series mismatch for: " + mpn);
        }

        @Test
        @DisplayName("Should extract value from CR series")
        void shouldExtractCRValue() {
            assertEquals("1k", handler.extractValue("CR0603-FX-1001ELF"));
            assertEquals("10k", handler.extractValue("CR0603-FX-1002ELF"));
            assertEquals("100k", handler.extractValue("CR0603-FX-1003ELF"));
        }
    }

    @Nested
    @DisplayName("AR Series - Anti-Sulfur Resistors")
    class ARSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AR series resistors")
        @ValueSource(strings = {
                "AR0603-FX-1002GLF",
                "AR0402-FX-1001ELF",
                "AR0805-GX-4702GLF",
                "AR1206-JX-1003JLF"
        })
        void shouldDetectARResistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RESISTOR, registry),
                    "Should match AR series resistor: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from AR series")
        @CsvSource({
                "AR0603-FX-1002GLF, 0603",
                "AR0402-FX-1001ELF, 0402",
                "AR0805-GX-4702GLF, 0805",
                "AR1206-JX-1003JLF, 1206"
        })
        void shouldExtractARPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code mismatch for: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from AR resistors")
        @CsvSource({
                "AR0603-FX-1002GLF, AR0603",
                "AR0402-FX-1001ELF, AR0402",
                "AR0805-GX-4702GLF, AR0805"
        })
        void shouldExtractARSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series mismatch for: " + mpn);
        }
    }

    @Nested
    @DisplayName("PA Series - Power Resistors")
    class PASeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect PA series resistors")
        @ValueSource(strings = {
                "PA0805-10R0F",
                "PA1206-1R00J",
                "PA2010-100RF",
                "PA2512-0R100F"
        })
        void shouldDetectPAResistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RESISTOR, registry),
                    "Should match PA series resistor: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from PA series")
        @CsvSource({
                "PA0805-10R0F, 0805",
                "PA1206-1R00J, 1206",
                "PA2010-100RF, 2010",
                "PA2512-0R100F, 2512"
        })
        void shouldExtractPAPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code mismatch for: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from PA resistors")
        @CsvSource({
                "PA0805-10R0F, PA0805",
                "PA1206-1R00J, PA1206",
                "PA2010-100RF, PA2010"
        })
        void shouldExtractPASeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series mismatch for: " + mpn);
        }
    }

    @Nested
    @DisplayName("CSR Series - Current Sense Resistors")
    class CSRSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect CSR series resistors")
        @ValueSource(strings = {
                "CSR0805-0R010F",
                "CSR1206-0R050G",
                "CSR2512-0R100J",
                "CSR0603-0R005F"
        })
        void shouldDetectCSRResistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RESISTOR, registry),
                    "Should match CSR series resistor: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from CSR series")
        @CsvSource({
                "CSR0805-0R010F, 0805",
                "CSR1206-0R050G, 1206",
                "CSR2512-0R100J, 2512",
                "CSR0603-0R005F, 0603"
        })
        void shouldExtractCSRPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code mismatch for: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from CSR resistors")
        @CsvSource({
                "CSR0805-0R010F, CSR0805",
                "CSR1206-0R050G, CSR1206",
                "CSR2512-0R100J, CSR2512"
        })
        void shouldExtractCSRSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series mismatch for: " + mpn);
        }

        @Test
        @DisplayName("Should extract value from CSR series")
        void shouldExtractCSRValue() {
            assertEquals("0.010", handler.extractValue("CSR0805-0R010F"));
            assertEquals("0.050", handler.extractValue("CSR1206-0R050G"));
            assertEquals("0.100", handler.extractValue("CSR2512-0R100J"));
        }
    }

    @Nested
    @DisplayName("Tolerance Code Extraction")
    class ToleranceTests {

        @Test
        @DisplayName("Should recognize F=1% tolerance")
        void shouldRecognizeFTolerance() {
            // Same series, same size, same tolerance - should be replacement
            assertTrue(handler.isOfficialReplacement(
                    "CR0603-FX-1001ELF",
                    "CR0603-FX-1002ELF"));
        }

        @Test
        @DisplayName("Should recognize G=2% tolerance")
        void shouldRecognizeGTolerance() {
            assertTrue(handler.isOfficialReplacement(
                    "AR0603-GX-1001GLF",
                    "AR0603-GX-1002GLF"));
        }

        @Test
        @DisplayName("Should recognize J=5% tolerance")
        void shouldRecognizeJTolerance() {
            assertTrue(handler.isOfficialReplacement(
                    "CR0805-JX-1001JLF",
                    "CR0805-JX-1002JLF"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and tolerance should be replacements")
        void sameSeriesToleranceShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement(
                    "CR0603-FX-1001ELF",
                    "CR0603-FX-1002ELF"),
                    "Same series, same tolerance should be replacements");
        }

        @Test
        @DisplayName("Different series should not be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement(
                    "CR0603-FX-1001ELF",
                    "AR0603-FX-1001ELF"),
                    "Different series should not be replacements");
        }

        @Test
        @DisplayName("Different size should not be replacements")
        void differentSizeShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement(
                    "CR0603-FX-1001ELF",
                    "CR0805-FX-1001ELF"),
                    "Different size should not be replacements");
        }

        @Test
        @DisplayName("Different tolerance should not be replacements")
        void differentToleranceShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement(
                    "CR0603-FX-1001ELF",
                    "CR0603-JX-1001JLF"),
                    "Different tolerance should not be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support RESISTOR type")
        void shouldSupportResistor() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.RESISTOR),
                    "Should support RESISTOR type");
        }

        @Test
        @DisplayName("Should support IC type")
        void shouldSupportIC() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("Should use immutable Set.of()")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertNotNull(types, "Should return non-null set");
            assertFalse(types.isEmpty(), "Should have at least one supported type");
            assertEquals(2, types.size(), "Should have exactly 2 supported types");

            // Verify immutability
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.CAPACITOR);
            }, "Set should be immutable");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.RESISTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractValue(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.RESISTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractValue(""));
        }

        @Test
        @DisplayName("Should handle null type gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("CR0603-FX-1001ELF", null, registry));
        }

        @Test
        @DisplayName("Should handle null in replacement check")
        void shouldHandleNullInReplacementCheck() {
            assertFalse(handler.isOfficialReplacement(null, "CR0603-FX-1001ELF"));
            assertFalse(handler.isOfficialReplacement("CR0603-FX-1001ELF", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("cr0603-fx-1001elf", ComponentType.RESISTOR, registry),
                    "Should match lowercase MPN");
            assertEquals("0603", handler.extractPackageCode("cr0603-fx-1001elf"));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("Cr0603-Fx-1001Elf", ComponentType.RESISTOR, registry),
                    "Should match mixed case MPN");
        }

        @Test
        @DisplayName("Should not match invalid MPNs")
        void shouldNotMatchInvalidMpns() {
            assertFalse(handler.matches("RC0603FR-07100KL", ComponentType.RESISTOR, registry),
                    "Should not match Yageo RC series");
            assertFalse(handler.matches("CRCW0603100KFKEA", ComponentType.RESISTOR, registry),
                    "Should not match Vishay CRCW series");
            assertFalse(handler.matches("ERJ3GEYJ103V", ComponentType.RESISTOR, registry),
                    "Should not match Panasonic ERJ series");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            VikingTechHandler directHandler = new VikingTechHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes());
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "Manufacturer types should be empty");
        }
    }

    @Nested
    @DisplayName("Value Decoding")
    class ValueDecodingTests {

        @ParameterizedTest
        @DisplayName("Should decode standard 4-digit value codes")
        @CsvSource({
                "CR0603-FX-1000ELF, 100",
                "CR0603-FX-1001ELF, 1k",
                "CR0603-FX-1002ELF, 10k",
                "CR0603-FX-1003ELF, 100k",
                "CR0603-FX-1004ELF, 1M"
        })
        void shouldDecodeStandardValueCodes(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractValue(mpn),
                    "Value decoding mismatch for: " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should decode current sense R-notation values")
        @CsvSource({
                "CSR0805-0R010F, 0.010",
                "CSR0805-0R050F, 0.050",
                "CSR0805-0R100F, 0.100"
        })
        void shouldDecodeCurrentSenseValues(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractValue(mpn),
                    "Current sense value decoding mismatch for: " + mpn);
        }
    }

    @Nested
    @DisplayName("Package Size Coverage")
    class PackageSizeTests {

        @ParameterizedTest
        @DisplayName("Should handle all standard imperial sizes")
        @CsvSource({
                "CR0201-FX-1001ELF, 0201",
                "CR0402-FX-1001ELF, 0402",
                "CR0603-FX-1001ELF, 0603",
                "CR0805-FX-1001ELF, 0805",
                "CR1206-FX-1001ELF, 1206",
                "CR1210-FX-1001ELF, 1210",
                "CR2010-FX-1001ELF, 2010",
                "CR2512-FX-1001ELF, 2512"
        })
        void shouldHandleAllStandardSizes(String mpn, String expectedSize) {
            assertEquals(expectedSize, handler.extractPackageCode(mpn),
                    "Package size mismatch for: " + mpn);
            assertTrue(handler.matches(mpn, ComponentType.RESISTOR, registry),
                    "Should match resistor with size: " + expectedSize);
        }
    }
}
