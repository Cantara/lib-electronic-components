package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.TorexHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for TorexHandler (Torex Semiconductor).
 * Tests pattern matching, package code extraction, series extraction, and helper methods.
 *
 * Torex specializes in:
 * - LDOs (XC6206, XC6210, XC6220)
 * - DC-DC converters (XC9265, XC9142, XC9235)
 * - Voltage detectors (XC6119, XC61CN)
 * - Battery chargers (XC6802, XC6808)
 * - Load switches (XC8107, XC8109)
 */
class TorexHandlerTest {

    private static TorexHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new TorexHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("XC62xx LDO Regulator Detection")
    class LDOTests {

        @ParameterizedTest
        @DisplayName("Should detect XC6206 series as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "XC6206", "XC6206P", "XC6206P332MR",
            "XC6206P182MR", "XC6206P502MR",
            "XC6206A33AMR", "XC6206B332TR"
        })
        void shouldDetectXC6206AsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect XC6210 series LDOs")
        @ValueSource(strings = {
            "XC6210", "XC6210B332MR", "XC6210A18AMR",
            "XC6210P302MR", "XC6210C502TR"
        })
        void shouldDetectXC6210AsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect XC6220 series LDOs")
        @ValueSource(strings = {
            "XC6220", "XC6220A331MR", "XC6220B181MR",
            "XC6220D331DR", "XC6220C251TR"
        })
        void shouldDetectXC6220AsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("XC6206P332MR is a popular 3.3V 200mA LDO")
        void shouldDetectPopularLDO() {
            String mpn = "XC6206P332MR";
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.isLDO(mpn));
            assertFalse(handler.isDCDCConverter(mpn));
        }
    }

    @Nested
    @DisplayName("XC9xxx DC-DC Converter Detection")
    class DCDCConverterTests {

        @ParameterizedTest
        @DisplayName("Should detect XC9265 series DC-DC converters")
        @ValueSource(strings = {
            "XC9265", "XC9265A10CMR", "XC9265A12CMR",
            "XC9265B33CMR", "XC9265C50CMR"
        })
        void shouldDetectXC9265AsDCDC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect XC9142 series DC-DC converters")
        @ValueSource(strings = {
            "XC9142", "XC9142A50CMR", "XC9142B33DMR",
            "XC9142C18CMR"
        })
        void shouldDetectXC9142AsDCDC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect XC9235 series step-up converters")
        @ValueSource(strings = {
            "XC9235", "XC9235A33CMR", "XC9235B50CMR",
            "XC9235C18DMR"
        })
        void shouldDetectXC9235AsStepUp(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should identify DC-DC converters correctly")
        void shouldIdentifyDCDCConverters() {
            assertTrue(handler.isDCDCConverter("XC9265A10CMR"));
            assertTrue(handler.isDCDCConverter("XC9235B33CMR"));
            assertTrue(handler.isDCDCConverter("XC9142A50CMR"));
            assertFalse(handler.isDCDCConverter("XC6206P332MR"));
            assertFalse(handler.isDCDCConverter("XC6119N22AMR"));
        }
    }

    @Nested
    @DisplayName("XC61xx Voltage Detector Detection")
    class VoltageDetectorTests {

        @ParameterizedTest
        @DisplayName("Should detect XC6119 series voltage detectors")
        @ValueSource(strings = {
            "XC6119", "XC6119N22AMR", "XC6119N27AMR",
            "XC6119N30AMR", "XC6119N33BMR"
        })
        void shouldDetectXC6119AsVoltageDetector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR (power management IC)");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect XC61CN series voltage detectors")
        @ValueSource(strings = {
            "XC61CN", "XC61CN2702MR", "XC61CN3002MR",
            "XC61CN3302MR", "XC61CN4002NR"
        })
        void shouldDetectXC61CNAsVoltageDetector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should identify voltage detectors correctly")
        void shouldIdentifyVoltageDetectors() {
            assertTrue(handler.isVoltageDetector("XC6119N22AMR"));
            assertTrue(handler.isVoltageDetector("XC61CN3302MR"));
            assertFalse(handler.isVoltageDetector("XC6206P332MR"));
            assertFalse(handler.isVoltageDetector("XC9265A10CMR"));
        }
    }

    @Nested
    @DisplayName("XC68xx Battery Charger Detection")
    class BatteryChargerTests {

        @ParameterizedTest
        @DisplayName("Should detect XC6802 series battery chargers")
        @ValueSource(strings = {
            "XC6802", "XC6802A42AMR", "XC6802B42BMR",
            "XC6802C42CMR"
        })
        void shouldDetectXC6802AsBatteryCharger(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR (power management IC)");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect XC6808 series battery chargers")
        @ValueSource(strings = {
            "XC6808", "XC6808A4AMR", "XC6808B4BMR"
        })
        void shouldDetectXC6808AsBatteryCharger(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should identify battery chargers correctly")
        void shouldIdentifyBatteryChargers() {
            assertTrue(handler.isBatteryCharger("XC6802A42AMR"));
            assertTrue(handler.isBatteryCharger("XC6808B4BMR"));
            assertFalse(handler.isBatteryCharger("XC6206P332MR"));
            assertFalse(handler.isBatteryCharger("XC8107A05CMR"));
        }
    }

    @Nested
    @DisplayName("XC81xx Load Switch Detection")
    class LoadSwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect XC8107 series load switches")
        @ValueSource(strings = {
            "XC8107", "XC8107A05CMR", "XC8107B10CMR",
            "XC8107C05DMR"
        })
        void shouldDetectXC8107AsLoadSwitch(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR (power management IC)");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect XC8109 series load switches")
        @ValueSource(strings = {
            "XC8109", "XC8109A05CMR", "XC8109B10CMR"
        })
        void shouldDetectXC8109AsLoadSwitch(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should identify load switches correctly")
        void shouldIdentifyLoadSwitches() {
            assertTrue(handler.isLoadSwitch("XC8107A05CMR"));
            assertTrue(handler.isLoadSwitch("XC8109B10CMR"));
            assertFalse(handler.isLoadSwitch("XC6206P332MR"));
            assertFalse(handler.isLoadSwitch("XC6802A42AMR"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract standard package codes")
        @CsvSource({
            "XC6206A33AMR, SOT-23",
            "XC6206B332TR, SOT-89",
            "XC6206C332MR, SOT-25",
            "XC6206D332MR, DFN",
            "XC6206P332MR, SOT-23-5"
        })
        void shouldExtractStandardPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should handle various package suffixes")
        @ValueSource(strings = {"XC6206P332MR", "XC6210B332MR"})
        void shouldHandlePackageSuffixes(String mpn) {
            String pkg = handler.extractPackageCode(mpn);
            assertNotNull(pkg);
            assertFalse(pkg.isEmpty());
        }

        @Test
        @DisplayName("Should handle empty and null MPN")
        void shouldHandleEmptyAndNull() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series codes")
        @CsvSource({
            "XC6206P332MR, XC62",
            "XC6210A33AMR, XC62",
            "XC6220B181MR, XC62",
            "XC9265A10CMR, XC92",
            "XC9142B33CMR, XC91",
            "XC9235C50CMR, XC92",
            "XC6119N22AMR, XC61",
            "XC6802A42AMR, XC68",
            "XC8107A05CMR, XC81",
            "XC9142A50CMR, XC91"
        })
        void shouldExtractSeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from short format MPNs")
        @CsvSource({
            "XC61CN, XC61",
            "XC61CN2702MR, XC61",
            "XC61CN3302MR, XC61"
        })
        void shouldExtractSeriesFromShortFormat(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
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
    @DisplayName("Series Description")
    class SeriesDescriptionTests {

        @ParameterizedTest
        @DisplayName("Should return correct series descriptions")
        @CsvSource({
            "XC62, LDO Regulators",
            "XC91, DC-DC Converters",
            "XC92, DC-DC Converters",
            "XC61, Voltage Detectors",
            "XC68, Battery Chargers",
            "XC81, Load Switches"
        })
        void shouldReturnSeriesDescription(String series, String expectedDesc) {
            assertEquals(expectedDesc, handler.getSeriesDescription(series));
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getSeriesDescription("UNKNOWN"));
            assertEquals("", handler.getSeriesDescription(""));
            assertEquals("", handler.getSeriesDescription("XC99"));
        }
    }

    @Nested
    @DisplayName("Voltage Code Extraction")
    class VoltageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract voltage codes from LDO MPNs")
        @CsvSource({
            "XC6206P332MR, 332",
            "XC6206P182MR, 182",
            "XC6206P502MR, 502",
            "XC6206A33AMR, 33"
        })
        void shouldExtractVoltageCodes(String mpn, String expectedCode) {
            assertEquals(expectedCode, handler.extractVoltageCode(mpn),
                    "Voltage code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("XC6206P332MR", "XC6206A332MR"),
                    "XC6206 with P and A packages should be replacements");
            assertTrue(handler.isOfficialReplacement("XC6206B332TR", "XC6206C332MR"),
                    "XC6206 with B and C packages should be replacements");
        }

        @Test
        @DisplayName("Same part with different voltage should still be replacements (pin-compatible)")
        void samePartDifferentVoltage() {
            // Same base parts (XC6206) are pin-compatible regardless of voltage code
            assertTrue(handler.isOfficialReplacement("XC6206P332MR", "XC6206P182MR"),
                    "Same base part, different voltage, are still replacements (pin-compatible)");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("XC6206P332MR", "XC9265A10CMR"),
                    "XC6206 and XC9265 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("XC6206P332MR", "XC6119N22AMR"),
                    "XC6206 and XC6119 should NOT be replacements");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "XC6206P332MR"));
            assertFalse(handler.isOfficialReplacement("XC6206P332MR", null));
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

        @Test
        @DisplayName("Should have exactly 2 types")
        void shouldHaveExactTypes() {
            var types = handler.getSupportedTypes();
            assertEquals(2, types.size(), "Should have exactly 2 types: IC and VOLTAGE_REGULATOR");
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
            assertEquals("", handler.extractVoltageCode(null));
            assertFalse(handler.isOfficialReplacement(null, "XC6206"));
            assertFalse(handler.isOfficialReplacement("XC6206", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractVoltageCode(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("XC6206P332MR", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("xc6206p332mr", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("XC6206P332MR", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("Xc6206p332Mr", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Should not match non-Torex parts")
        void shouldNotMatchNonTorexParts() {
            assertFalse(handler.matches("LM7805", ComponentType.VOLTAGE_REGULATOR, registry),
                    "LM7805 is not a Torex part");
            assertFalse(handler.matches("TPS54331", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TPS54331 is not a Torex part");
            assertFalse(handler.matches("MP1584EN", ComponentType.VOLTAGE_REGULATOR, registry),
                    "MP1584EN is not a Torex part");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("XC", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("XC6", ComponentType.VOLTAGE_REGULATOR, registry));
            assertFalse(handler.matches("XC62", ComponentType.VOLTAGE_REGULATOR, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular Torex parts")
        @CsvSource({
            "XC6206P332MR, VOLTAGE_REGULATOR",
            "XC6206P182MR, VOLTAGE_REGULATOR",
            "XC6210B332MR, VOLTAGE_REGULATOR",
            "XC6220A331MR, VOLTAGE_REGULATOR",
            "XC9265A10CMR, VOLTAGE_REGULATOR",
            "XC9142B33CMR, VOLTAGE_REGULATOR",
            "XC6119N22AMR, VOLTAGE_REGULATOR",
            "XC61CN3302MR, VOLTAGE_REGULATOR",
            "XC6802A42AMR, VOLTAGE_REGULATOR",
            "XC8107A05CMR, VOLTAGE_REGULATOR"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @Test
        @DisplayName("Should handle full MPN with all details")
        void shouldHandleFullMPN() {
            String fullMpn = "XC6206P332MR";
            assertTrue(handler.matches(fullMpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertEquals("XC62", handler.extractSeries(fullMpn));
            assertEquals("332", handler.extractVoltageCode(fullMpn));
            assertTrue(handler.isLDO(fullMpn));
            assertFalse(handler.isDCDCConverter(fullMpn));
            assertFalse(handler.isVoltageDetector(fullMpn));
            assertFalse(handler.isBatteryCharger(fullMpn));
            assertFalse(handler.isLoadSwitch(fullMpn));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All Torex parts should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "XC6206P332MR", "XC9265A10CMR", "XC6119N22AMR",
                "XC6802A42AMR", "XC8107A05CMR", "XC61CN3302MR"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("All Torex parts should match VOLTAGE_REGULATOR")
        void allPartsShouldMatchVoltageRegulator() {
            String[] testMpns = {
                "XC6206P332MR", "XC9265A10CMR", "XC6119N22AMR",
                "XC6802A42AMR", "XC8107A05CMR"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                        mpn + " should match VOLTAGE_REGULATOR");
            }
        }
    }

    @Nested
    @DisplayName("Component Type Identification Helper Methods")
    class HelperMethodTests {

        @Test
        @DisplayName("isLDO should correctly identify LDO regulators")
        void testIsLDO() {
            // Positive cases
            assertTrue(handler.isLDO("XC6206P332MR"));
            assertTrue(handler.isLDO("XC6210B332MR"));
            assertTrue(handler.isLDO("XC6220A331MR"));
            assertTrue(handler.isLDO("XC6301A33AMR"));
            assertTrue(handler.isLDO("XC6401B18AMR"));

            // Negative cases
            assertFalse(handler.isLDO("XC9265A10CMR"));
            assertFalse(handler.isLDO("XC6119N22AMR"));
            assertFalse(handler.isLDO("XC6802A42AMR"));
            assertFalse(handler.isLDO("XC8107A05CMR"));
            assertFalse(handler.isLDO(null));
        }

        @Test
        @DisplayName("isDCDCConverter should correctly identify DC-DC converters")
        void testIsDCDCConverter() {
            // Positive cases
            assertTrue(handler.isDCDCConverter("XC9265A10CMR"));
            assertTrue(handler.isDCDCConverter("XC9235B33CMR"));
            assertTrue(handler.isDCDCConverter("XC9142A50CMR"));
            assertTrue(handler.isDCDCConverter("XC9301A50CMR"));

            // Negative cases
            assertFalse(handler.isDCDCConverter("XC6206P332MR"));
            assertFalse(handler.isDCDCConverter("XC6119N22AMR"));
            assertFalse(handler.isDCDCConverter("XC6802A42AMR"));
            assertFalse(handler.isDCDCConverter(null));
        }

        @Test
        @DisplayName("isVoltageDetector should correctly identify voltage detectors")
        void testIsVoltageDetector() {
            // Positive cases
            assertTrue(handler.isVoltageDetector("XC6119N22AMR"));
            assertTrue(handler.isVoltageDetector("XC61CN3302MR"));
            assertTrue(handler.isVoltageDetector("XC6115N27AMR"));

            // Negative cases
            assertFalse(handler.isVoltageDetector("XC6206P332MR"));
            assertFalse(handler.isVoltageDetector("XC9265A10CMR"));
            assertFalse(handler.isVoltageDetector("XC6802A42AMR"));
            assertFalse(handler.isVoltageDetector(null));
        }

        @Test
        @DisplayName("isBatteryCharger should correctly identify battery chargers")
        void testIsBatteryCharger() {
            // Positive cases
            assertTrue(handler.isBatteryCharger("XC6802A42AMR"));
            assertTrue(handler.isBatteryCharger("XC6808B4BMR"));

            // Negative cases
            assertFalse(handler.isBatteryCharger("XC6206P332MR"));
            assertFalse(handler.isBatteryCharger("XC9265A10CMR"));
            assertFalse(handler.isBatteryCharger("XC8107A05CMR"));
            assertFalse(handler.isBatteryCharger(null));
        }

        @Test
        @DisplayName("isLoadSwitch should correctly identify load switches")
        void testIsLoadSwitch() {
            // Positive cases
            assertTrue(handler.isLoadSwitch("XC8107A05CMR"));
            assertTrue(handler.isLoadSwitch("XC8109B10CMR"));

            // Negative cases
            assertFalse(handler.isLoadSwitch("XC6206P332MR"));
            assertFalse(handler.isLoadSwitch("XC9265A10CMR"));
            assertFalse(handler.isLoadSwitch("XC6802A42AMR"));
            assertFalse(handler.isLoadSwitch(null));
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {

        @Test
        @DisplayName("Should return empty manufacturer types")
        void shouldReturnEmptyManufacturerTypes() {
            var types = handler.getManufacturerTypes();
            assertNotNull(types);
            assertTrue(types.isEmpty(), "Should return empty set of manufacturer types");
        }
    }
}
