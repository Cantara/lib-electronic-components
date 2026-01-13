package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ABLICHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ABLICHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * ABLIC (formerly Seiko Instruments) specializes in low-power analog semiconductors:
 * - LDO regulators (S-1xxx series)
 * - Voltage detectors (S-80xxx series)
 * - RTCs (S-35xxx series)
 * - Battery management ICs (S-82xx series)
 * - EEPROMs (S-24C, S-93C series)
 */
class ABLICHandlerTest {

    private static ABLICHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation - simpler and avoids MPN lookup dependency
        handler = new ABLICHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("LDO Voltage Regulator Detection")
    class LDORegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect S-1167 LDO variants")
        @ValueSource(strings = {"S-1167B33A-I6T1U", "S-1167B30A", "S-1167B50A-M6T1U"})
        void shouldDetectS1167Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect S-1206 LDO variants")
        @ValueSource(strings = {"S-1206B33A", "S-1206B50A-I6T1U", "S-1206B18A"})
        void shouldDetectS1206Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect S-1312 LDO variants")
        @ValueSource(strings = {"S-1312B33A", "S-1312B50B", "S-1312B30A-I6T1G"})
        void shouldDetectS1312Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect other S-1xxx LDO series")
        @ValueSource(strings = {"S-1313B33A", "S-1318B50A", "S-1000B33A"})
        void shouldDetectOtherS1xxxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("isLDORegulator helper method should work")
        void isLDORegulatorHelperShouldWork() {
            assertTrue(handler.isLDORegulator("S-1167B33A-I6T1U"));
            assertTrue(handler.isLDORegulator("S-1206B50A"));
            assertTrue(handler.isLDORegulator("S-1312B33A"));
            assertFalse(handler.isLDORegulator("S-80740"));
            assertFalse(handler.isLDORegulator("S-35390A"));
        }
    }

    @Nested
    @DisplayName("Voltage Detector Detection")
    class VoltageDetectorTests {

        @ParameterizedTest
        @DisplayName("Should detect S-80740 voltage detector variants")
        @ValueSource(strings = {"S-80740CNNB-G6T1U", "S-80740ANNB", "S-80740BNNB-G6T1G"})
        void shouldDetectS80740Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect S-80945 voltage detector variants")
        @ValueSource(strings = {"S-80945CNNB", "S-80945ANNB-G6T1U", "S-80945BNNB"})
        void shouldDetectS80945Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect other S-80xxx voltage detectors")
        @ValueSource(strings = {"S-80835ANNB", "S-80810CNNB", "S-80900ANNB"})
        void shouldDetectOtherS80xxxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("isVoltageDetector helper method should work")
        void isVoltageDetectorHelperShouldWork() {
            assertTrue(handler.isVoltageDetector("S-80740CNNB-G6T1U"));
            assertTrue(handler.isVoltageDetector("S-80945ANNB"));
            assertFalse(handler.isVoltageDetector("S-1167B33A"));
            assertFalse(handler.isVoltageDetector("S-35390A"));
        }
    }

    @Nested
    @DisplayName("RTC Detection")
    class RTCTests {

        @ParameterizedTest
        @DisplayName("Should detect S-35390A RTC variants")
        @ValueSource(strings = {"S-35390A-T8T1G", "S-35390A", "S-35390A-J8T1U"})
        void shouldDetectS35390AVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.isRTC(mpn), mpn + " should be detected as RTC");
        }

        @ParameterizedTest
        @DisplayName("Should detect S-35198 RTC variants")
        @ValueSource(strings = {"S-35198A", "S-35198A-T8T1G", "S-35198B"})
        void shouldDetectS35198Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.isRTC(mpn), mpn + " should be detected as RTC");
        }

        @Test
        @DisplayName("isRTC helper method should work")
        void isRTCHelperShouldWork() {
            assertTrue(handler.isRTC("S-35390A-T8T1G"));
            assertTrue(handler.isRTC("S-35198A"));
            assertFalse(handler.isRTC("S-1167B33A"));
            assertFalse(handler.isRTC("S-80740CNNB"));
        }
    }

    @Nested
    @DisplayName("Battery Management IC Detection")
    class BatteryManagementTests {

        @ParameterizedTest
        @DisplayName("Should detect S-8261 battery protection IC variants")
        @ValueSource(strings = {"S-8261ABJMD-G3JT2G", "S-8261ABJMD", "S-8261ABNMD-G3JT2U"})
        void shouldDetectS8261Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.isBatteryManagement(mpn), mpn + " should be detected as battery management");
        }

        @ParameterizedTest
        @DisplayName("Should detect S-8254 battery fuel gauge variants")
        @ValueSource(strings = {"S-8254A", "S-8254A-M8T1U", "S-8254B"})
        void shouldDetectS8254Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.isBatteryManagement(mpn), mpn + " should be detected as battery management");
        }

        @Test
        @DisplayName("isBatteryManagement helper method should work")
        void isBatteryManagementHelperShouldWork() {
            assertTrue(handler.isBatteryManagement("S-8261ABJMD-G3JT2G"));
            assertTrue(handler.isBatteryManagement("S-8254A"));
            assertFalse(handler.isBatteryManagement("S-1167B33A"));
            assertFalse(handler.isBatteryManagement("S-35390A"));
        }
    }

    @Nested
    @DisplayName("EEPROM Detection")
    class EEPROMTests {

        @ParameterizedTest
        @DisplayName("Should detect S-24C I2C EEPROM variants")
        @ValueSource(strings = {"S-24C02A", "S-24C04A-J8T1U", "S-24C08B", "S-24C16A", "S-24C32A"})
        void shouldDetectS24CEEPROMVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_EEPROM, registry),
                    mpn + " should match MEMORY_EEPROM");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect S-93C Microwire EEPROM variants")
        @ValueSource(strings = {"S-93C46A", "S-93C56B-J8T1G", "S-93C66A", "S-93C76A", "S-93C86A"})
        void shouldDetectS93CEEPROMVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should match MEMORY");
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_EEPROM, registry),
                    mpn + " should match MEMORY_EEPROM");
        }

        @Test
        @DisplayName("isEEPROM helper method should work")
        void isEEPROMHelperShouldWork() {
            assertTrue(handler.isEEPROM("S-24C02A"));
            assertTrue(handler.isEEPROM("S-93C46A"));
            assertFalse(handler.isEEPROM("S-1167B33A"));
            assertFalse(handler.isEEPROM("S-35390A"));
        }

        @Test
        @DisplayName("isI2CEEPROM helper method should work")
        void isI2CEEPROMHelperShouldWork() {
            assertTrue(handler.isI2CEEPROM("S-24C02A"));
            assertTrue(handler.isI2CEEPROM("S-24C16A-J8T1U"));
            assertFalse(handler.isI2CEEPROM("S-93C46A"));
        }

        @Test
        @DisplayName("isMicrowireEEPROM helper method should work")
        void isMicrowireEEPROMHelperShouldWork() {
            assertTrue(handler.isMicrowireEEPROM("S-93C46A"));
            assertTrue(handler.isMicrowireEEPROM("S-93C56B-J8T1G"));
            assertFalse(handler.isMicrowireEEPROM("S-24C02A"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from LDO parts")
        @CsvSource({
                "S-1167B33A-I6T1U, SOT-23",
                "S-1206B50B-M6T1U, SOT-89"
        })
        void shouldExtractLDOPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes from EEPROM parts")
        @CsvSource({
                "S-24C02A, SOT-23",
                "S-24C08B, SOT-89",
                "S-93C46A, SOT-23"
        })
        void shouldExtractEEPROMPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract LDO series")
        @CsvSource({
                "S-1167B33A-I6T1U, S-1167",
                "S-1206B50A, S-1206",
                "S-1312B33A, S-1312"
        })
        void shouldExtractLDOSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract voltage detector series")
        @CsvSource({
                "S-80740CNNB-G6T1U, S-80740",
                "S-80945ANNB, S-80945",
                "S-80835BNNB, S-80835"
        })
        void shouldExtractVoltageDetectorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract RTC series")
        @CsvSource({
                "S-35390A-T8T1G, S-35390",
                "S-35198A, S-35198"
        })
        void shouldExtractRTCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract battery management series")
        @CsvSource({
                "S-8261ABJMD-G3JT2G, S-8261",
                "S-8254A, S-8254"
        })
        void shouldExtractBatteryManagementSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract EEPROM series")
        @CsvSource({
                "S-24C02A, S-24C",
                "S-24C16A-J8T1U, S-24C",
                "S-93C46A, S-93C",
                "S-93C56B-J8T1G, S-93C"
        })
        void shouldExtractEEPROMSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same LDO series with different packages should be replacements")
        void sameLDOSeriesDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("S-1167B33A-I6T1U", "S-1167B33A-M6T1U"),
                    "Same LDO with different suffix should be replacements");
            assertTrue(handler.isOfficialReplacement("S-1167B33A", "S-1167B33B"),
                    "Same LDO with different package should be replacements");
        }

        @Test
        @DisplayName("Same EEPROM series with different packages should be replacements")
        void sameEEPROMSeriesDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("S-24C02A", "S-24C02B"),
                    "Same EEPROM with different package should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("S-1167B33A", "S-1206B33A"),
                    "S-1167 and S-1206 should NOT be replacements (different series)");
            assertFalse(handler.isOfficialReplacement("S-24C02A", "S-93C46A"),
                    "I2C and Microwire EEPROM should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("S-1167B33A", "S-35390A"),
                    "LDO and RTC should NOT be replacements");
        }

        @Test
        @DisplayName("Different product families should NOT be replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("S-1167B33A", "S-80740CNNB"),
                    "LDO and voltage detector should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR),
                    "Should support VOLTAGE_REGULATOR");
            assertTrue(types.contains(ComponentType.MEMORY),
                    "Should support MEMORY");
            assertTrue(types.contains(ComponentType.MEMORY_EEPROM),
                    "Should support MEMORY_EEPROM");
        }

        @Test
        @DisplayName("Should have exactly 4 supported types")
        void shouldHaveCorrectTypeCount() {
            var types = handler.getSupportedTypes();
            assertEquals(4, types.size(), "Should have exactly 4 supported types");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of() (immutable)")
        void getSupportedTypesShouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes() should return immutable Set");
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
            assertFalse(handler.isOfficialReplacement(null, "S-1167B33A"));
            assertFalse(handler.isOfficialReplacement("S-1167B33A", null));
            assertFalse(handler.isLDORegulator(null));
            assertFalse(handler.isVoltageDetector(null));
            assertFalse(handler.isRTC(null));
            assertFalse(handler.isBatteryManagement(null));
            assertFalse(handler.isEEPROM(null));
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
            assertFalse(handler.matches("S-1167B33A", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("s-1167b33a", ComponentType.VOLTAGE_REGULATOR, registry),
                    "lowercase should match");
            assertTrue(handler.matches("S-1167B33A", ComponentType.VOLTAGE_REGULATOR, registry),
                    "uppercase should match");
            assertTrue(handler.matches("s-1167B33A", ComponentType.VOLTAGE_REGULATOR, registry),
                    "mixed case should match");
        }

        @Test
        @DisplayName("Should not match non-ABLIC parts")
        void shouldNotMatchNonABLICParts() {
            assertFalse(handler.matches("LM317", ComponentType.VOLTAGE_REGULATOR, registry),
                    "Should not match TI part");
            assertFalse(handler.matches("24C02", ComponentType.MEMORY, registry),
                    "Should not match generic EEPROM (no S- prefix)");
            assertFalse(handler.matches("S1167B33A", ComponentType.VOLTAGE_REGULATOR, registry),
                    "Should not match without hyphen");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            ABLICHandler directHandler = new ABLICHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify patterns work
            assertTrue(directHandler.matches("S-1167B33A", ComponentType.VOLTAGE_REGULATOR, directRegistry));
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }

    @Nested
    @DisplayName("Series Description")
    class SeriesDescriptionTests {

        @ParameterizedTest
        @DisplayName("Should return descriptions for known series")
        @CsvSource({
                "S-1167, LDO Voltage Regulator",
                "S-1206, LDO Voltage Regulator",
                "S-80740, Voltage Detector",
                "S-35390, Real-Time Clock with I2C",
                "S-8261, Battery Protection IC",
                "S-24C, I2C EEPROM",
                "S-93C, Microwire EEPROM"
        })
        void shouldReturnSeriesDescriptions(String series, String expectedDescription) {
            assertEquals(expectedDescription, handler.getSeriesDescription(series),
                    "Description for series " + series);
        }

        @Test
        @DisplayName("Should return empty string for unknown series")
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.getSeriesDescription("S-9999"),
                    "Unknown series should return empty string");
            assertEquals("", handler.getSeriesDescription("UNKNOWN"),
                    "Non-ABLIC series should return empty string");
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @Test
        @DisplayName("Common LDO regulators from datasheets")
        void commonLDORegulators() {
            // S-1167 series - ultra-low quiescent current LDO
            assertTrue(handler.matches("S-1167B33A-I6T1U", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("S-1167B50A-M6T1U", ComponentType.VOLTAGE_REGULATOR, registry));

            // S-1206 series - low noise LDO
            assertTrue(handler.matches("S-1206B33A-I6T1U", ComponentType.VOLTAGE_REGULATOR, registry));

            // S-1312 series - high output current LDO
            assertTrue(handler.matches("S-1312B33A-M6T1G", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Common voltage detectors from datasheets")
        void commonVoltageDetectors() {
            // S-80740 series - voltage detector with delay
            assertTrue(handler.matches("S-80740CNNB-G6T1U", ComponentType.VOLTAGE_REGULATOR, registry));

            // S-80945 series - high accuracy voltage detector
            assertTrue(handler.matches("S-80945ANNB-G6T1G", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Common RTCs from datasheets")
        void commonRTCs() {
            // S-35390A - I2C Real-Time Clock
            assertTrue(handler.isRTC("S-35390A-T8T1G"));
            assertTrue(handler.matches("S-35390A-T8T1G", ComponentType.IC, registry));

            // S-35198 - Calendar RTC
            assertTrue(handler.isRTC("S-35198A-T8T1U"));
        }

        @Test
        @DisplayName("Common battery management ICs from datasheets")
        void commonBatteryManagementICs() {
            // S-8261 series - lithium-ion battery protection
            assertTrue(handler.isBatteryManagement("S-8261ABJMD-G3JT2G"));
            assertTrue(handler.matches("S-8261ABJMD-G3JT2G", ComponentType.IC, registry));

            // S-8254 series - fuel gauge
            assertTrue(handler.isBatteryManagement("S-8254A-M8T1U"));
        }

        @Test
        @DisplayName("Common EEPROMs from datasheets")
        void commonEEPROMs() {
            // I2C EEPROM
            assertTrue(handler.isI2CEEPROM("S-24C02A-J8T1U"));
            assertTrue(handler.isI2CEEPROM("S-24C16A-K8T1G"));
            assertTrue(handler.matches("S-24C02A", ComponentType.MEMORY_EEPROM, registry));

            // Microwire EEPROM
            assertTrue(handler.isMicrowireEEPROM("S-93C46A-T8T1G"));
            assertTrue(handler.isMicrowireEEPROM("S-93C56B-J8T1U"));
            assertTrue(handler.matches("S-93C46A", ComponentType.MEMORY_EEPROM, registry));
        }
    }

    @Nested
    @DisplayName("Pattern Non-Match Tests")
    class PatternNonMatchTests {

        @ParameterizedTest
        @DisplayName("Should not match invalid S- prefixed parts")
        @ValueSource(strings = {
                "S-ABC123",      // Invalid format
                "S-1",           // Too short
                "S-12",          // Too short
                "S-123",         // Too short
                "S-ABCD",        // No digits
                "SA-1167B33A",   // Wrong prefix format
                "S1167B33A"      // Missing hyphen
        })
        void shouldNotMatchInvalidSParts(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should NOT match IC");
            assertFalse(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should NOT match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should not match other manufacturers' parts")
        @ValueSource(strings = {
                "LM317",
                "TPS62200",
                "MAX6675",
                "DS18B20",
                "AT24C02",
                "93C46"
        })
        void shouldNotMatchOtherManufacturers(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should NOT match (not ABLIC)");
            assertFalse(handler.matches(mpn, ComponentType.MEMORY, registry),
                    mpn + " should NOT match (not ABLIC)");
        }
    }
}
