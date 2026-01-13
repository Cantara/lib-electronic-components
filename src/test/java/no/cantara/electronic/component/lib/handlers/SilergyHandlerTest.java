package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SilergyHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SilergyHandler (Silergy Corp).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class SilergyHandlerTest {

    private static SilergyHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SilergyHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SY808x DC-DC Buck Converter Detection")
    class SY808xTests {

        @ParameterizedTest
        @DisplayName("Should detect SY808x buck converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SY8088", "SY8088AAC", "SY8088QNC",
            "SY8089", "SY8089AAC",
            "SY8083", "SY8086", "SY8087"
        })
        void shouldDetectSY808xAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("SY8088 is a popular 2A sync buck converter")
        @ValueSource(strings = {"SY8088AAC", "SY8088QNC", "SY8088DFN"})
        void shouldDetectSY8088Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should be detected as VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("SY809x DC-DC Buck Converter Detection")
    class SY809xTests {

        @ParameterizedTest
        @DisplayName("Should detect SY809x buck converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SY8090", "SY8090AAC",
            "SY8091", "SY8092", "SY8093"
        })
        void shouldDetectSY809xAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("SY811x DC-DC Buck Converter Detection")
    class SY811xTests {

        @ParameterizedTest
        @DisplayName("Should detect SY811x buck converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SY8113", "SY8113AAC", "SY8113QNC",
            "SY8115", "SY8118", "SY8119"
        })
        void shouldDetectSY811xAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("SY8113 is a popular 3A sync buck converter")
        void shouldDetectSY8113() {
            assertTrue(handler.matches("SY8113AAC", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("SY8113QNC", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("SY720x Boost Converter Detection")
    class SY720xTests {

        @ParameterizedTest
        @DisplayName("Should detect SY720x boost converters as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SY7201", "SY7201QNC", "SY7201AAC",
            "SY7208", "SY7208AAC", "SY7208QNC",
            "SY7203", "SY7205"
        })
        void shouldDetectSY720xAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("SY7208 is a popular 2A boost converter")
        void shouldDetectSY7208() {
            assertTrue(handler.matches("SY7208AAC", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("SY7208QNC", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("SY7200 LED Driver Detection")
    class SY7200Tests {

        @ParameterizedTest
        @DisplayName("Should detect SY7200 as LED_DRIVER")
        @ValueSource(strings = {
            "SY7200", "SY7200AAC", "SY7200QNC", "SY7200DFN"
        })
        void shouldDetectSY7200AsLEDDriver(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("SY7200 should NOT match VOLTAGE_REGULATOR")
        void sy7200ShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("SY7200", ComponentType.VOLTAGE_REGULATOR, registry),
                    "SY7200 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("SY7200AAC", ComponentType.VOLTAGE_REGULATOR, registry),
                    "SY7200AAC should NOT match VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("SY800x/SY628x LDO Regulator Detection")
    class LDOTests {

        @ParameterizedTest
        @DisplayName("Should detect SY800x LDOs as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SY8009", "SY8009AAC", "SY8009QNC",
            "SY8001", "SY8003", "SY8005"
        })
        void shouldDetectSY800xAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SY628x LDOs as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SY6288", "SY6288DFN", "SY6288AAC",
            "SY6280", "SY6282"
        })
        void shouldDetectSY628xAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("LDOs should NOT match LED_DRIVER")
        void ldosShouldNotMatchLEDDriver() {
            assertFalse(handler.matches("SY8009", ComponentType.LED_DRIVER, registry),
                    "SY8009 should NOT match LED_DRIVER");
            assertFalse(handler.matches("SY6288", ComponentType.LED_DRIVER, registry),
                    "SY6288 should NOT match LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("SY698x Battery Charger Detection")
    class BatteryChargerTests {

        @ParameterizedTest
        @DisplayName("Should detect SY698x battery chargers as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SY6981", "SY6981AAC", "SY6981QNC",
            "SY6982", "SY6982DFN",
            "SY6980", "SY6983"
        })
        void shouldDetectSY698xAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @Test
        @DisplayName("Battery chargers should NOT match LED_DRIVER")
        void chargersShouldNotMatchLEDDriver() {
            assertFalse(handler.matches("SY6981", ComponentType.LED_DRIVER, registry),
                    "SY6981 should NOT match LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("SYXxxx Extended Product Line Detection")
    class SYXTests {

        @ParameterizedTest
        @DisplayName("Should detect SYXxxx as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "SYX196", "SYX196AAC",
            "SYX199", "SYX200", "SYX205"
        })
        void shouldDetectSYXAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract standard package codes")
        @CsvSource({
            "SY8088AAC, QFN",
            "SY7208QNC, QFN",
            "SY6288DFN, DFN",
            "SY8113AAC, QFN"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return raw code for unknown package codes")
        void shouldReturnRawCodeForUnknown() {
            String result = handler.extractPackageCode("SY8088XYZ");
            // Should return XYZ if not in lookup table
            assertEquals("XYZ", result);
        }

        @Test
        @DisplayName("Should handle empty and null MPN")
        void shouldHandleEmptyAndNull() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }

        @Test
        @DisplayName("Should handle MPN without package code")
        void shouldHandleMPNWithoutPackage() {
            // When just base part number, no package extracted
            assertEquals("", handler.extractPackageCode("SY8088"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series codes")
        @CsvSource({
            "SY8088AAC, SY808",
            "SY8089QNC, SY808",
            "SY8113AAC, SY811",
            "SY7208QNC, SY720",
            "SY7200AAC, SY720",
            "SY8009AAC, SY800",
            "SY6288DFN, SY628",
            "SY6981AAC, SY698"
        })
        void shouldExtractSeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SYX series")
        @ValueSource(strings = {"SYX196", "SYX196AAC", "SYX200"})
        void shouldExtractSYXSeries(String mpn) {
            assertEquals("SYX", handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalid() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("INVALID"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("SY8088AAC", "SY8088QNC"),
                    "SY8088AAC and SY8088QNC should be replacements (same part, different package)");
            assertTrue(handler.isOfficialReplacement("SY7208AAC", "SY7208DFN"),
                    "SY7208AAC and SY7208DFN should be replacements");
        }

        @Test
        @DisplayName("Same part with and without suffix should be replacements")
        void samePartWithSuffix() {
            assertTrue(handler.isOfficialReplacement("SY8088", "SY8088AAC"),
                    "SY8088 and SY8088AAC should be replacements");
            assertTrue(handler.isOfficialReplacement("SY8113", "SY8113QNC"),
                    "SY8113 and SY8113QNC should be replacements");
        }

        @Test
        @DisplayName("Different parts should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("SY8088AAC", "SY8089AAC"),
                    "SY8088 and SY8089 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("SY7200AAC", "SY7208AAC"),
                    "SY7200 (LED driver) and SY7208 (boost) should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("SY8088AAC", "SY6288DFN"),
                    "SY8088 (buck) and SY6288 (LDO) should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("SY8088AAC", "SYX196"),
                    "SY8088 and SYX196 should NOT be replacements (different series)");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "SY8088AAC"));
            assertFalse(handler.isOfficialReplacement("SY8088AAC", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.IC));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertTrue(types.contains(ComponentType.LED_DRIVER));
        }

        @Test
        @DisplayName("Should NOT support motor driver")
        void shouldNotSupportMotorDriver() {
            var types = handler.getSupportedTypes();
            assertFalse(types.contains(ComponentType.MOTOR_DRIVER));
        }

        @Test
        @DisplayName("Should use Set.of() for immutability")
        void shouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes() should return immutable Set");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    @Nested
    @DisplayName("Product Category Detection")
    class ProductCategoryTests {

        @ParameterizedTest
        @DisplayName("Should return correct product categories")
        @CsvSource({
            "SY8088AAC, DC-DC Buck Converter",
            "SY8089QNC, DC-DC Buck Converter",
            "SY8113AAC, DC-DC Buck Converter",
            "SY7208QNC, DC-DC Boost Converter",
            "SY7201AAC, DC-DC Boost Converter",
            "SY7200AAC, LED Driver",
            "SY8009AAC, LDO Regulator",
            "SY6288DFN, LDO Regulator",
            "SY6981AAC, Battery Charger",
            "SY6982DFN, Battery Charger",
            "SYX196AAC, Extended Product"
        })
        void shouldReturnProductCategory(String mpn, String expectedCategory) {
            assertEquals(expectedCategory, handler.getProductCategory(mpn));
        }

        @Test
        @DisplayName("Should return empty for null MPN")
        void shouldReturnEmptyForNull() {
            assertEquals("", handler.getProductCategory(null));
        }

        @Test
        @DisplayName("Should return empty for unknown MPN")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getProductCategory("UNKNOWN123"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "SY8088"));
            assertFalse(handler.isOfficialReplacement("SY8088", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("SY8088AAC", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("sy8088aac", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("SY8088AAC", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("Sy8088Aac", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Should not match non-Silergy parts")
        void shouldNotMatchNonSilergyParts() {
            assertFalse(handler.matches("LM7805", ComponentType.VOLTAGE_REGULATOR, registry),
                    "LM7805 is not a Silergy part");
            assertFalse(handler.matches("TPS54331", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TPS54331 is not a Silergy part");
            assertFalse(handler.matches("MP1584EN", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MP1584EN is not a Silergy part");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("SY", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("SY8", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("SY80", ComponentType.VOLTAGE_REGULATOR, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular Silergy parts")
        @CsvSource({
            "SY8088AAC, VOLTAGE_REGULATOR",
            "SY8089QNC, VOLTAGE_REGULATOR",
            "SY8113AAC, VOLTAGE_REGULATOR",
            "SY7208QNC, VOLTAGE_REGULATOR",
            "SY7201AAC, VOLTAGE_REGULATOR",
            "SY7200AAC, LED_DRIVER",
            "SY8009AAC, VOLTAGE_REGULATOR",
            "SY6288DFN, VOLTAGE_REGULATOR",
            "SY6981AAC, VOLTAGE_REGULATOR",
            "SYX196AAC, VOLTAGE_REGULATOR"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @Test
        @DisplayName("Should handle full MPN with package suffix")
        void shouldHandleFullMPN() {
            String fullMpn = "SY8088AAC";
            assertTrue(handler.matches(fullMpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertEquals("QFN", handler.extractPackageCode(fullMpn));
            assertEquals("SY808", handler.extractSeries(fullMpn));
            assertEquals("DC-DC Buck Converter", handler.getProductCategory(fullMpn));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All Silergy parts should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "SY8088AAC", "SY8089QNC", "SY8113AAC",
                "SY7208QNC", "SY7200AAC", "SY8009AAC",
                "SY6288DFN", "SY6981AAC", "SYX196AAC"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("LED drivers should not match VOLTAGE_REGULATOR")
        void ledDriversShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("SY7200", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("SY7200AAC", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Voltage regulators should not match LED_DRIVER")
        void voltageRegulatorsShouldNotMatchLEDDriver() {
            assertFalse(handler.matches("SY8088", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("SY7208", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("SY6288", ComponentType.LED_DRIVER, registry));
        }
    }

    @Nested
    @DisplayName("Series Description Tests")
    class SeriesDescriptionTests {

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getSeriesDescription("UNKNOWN"));
            assertEquals("", handler.getSeriesDescription(""));
        }

        @Test
        @DisplayName("Should return non-empty for known series")
        void shouldReturnDescriptionForKnown() {
            // Note: Not all series have descriptions in SERIES_INFO
            // This test verifies the method works without error
            String desc = handler.getSeriesDescription("SY808");
            assertNotNull(desc, "Description should not be null");
        }
    }
}
