package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ThreePeakHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ThreePeakHandler (3PEAK - Suzhou 3PEAK Electronic Inc.).
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * 3PEAK manufactures:
 * - Op-amps (TP1541, TP2111, TP2304)
 * - Comparators (TP1561, TP2345)
 * - LDO regulators (TP7140, TP7150)
 * - ADCs (TP5551, TP5854)
 * - Current sense amplifiers (TP181, TP182)
 */
class ThreePeakHandlerTest {

    private static ThreePeakHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ThreePeakHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Op-Amp Detection (TP1541, TP2111, TP2304)")
    class OpAmpTests {

        @ParameterizedTest
        @DisplayName("Should detect TP1541 op-amp variants as OPAMP")
        @ValueSource(strings = {
            "TP1541", "TP1541-TR", "TP1541TR", "TP1541-QR",
            "TP1541-DR", "TP1541-MR"
        })
        void shouldDetectTP1541AsOpAmp(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect TP2111 precision op-amp variants as OPAMP")
        @ValueSource(strings = {
            "TP2111", "TP2111-TR", "TP2111TR", "TP2111-QR",
            "TP2111-DR", "TP2111-MR"
        })
        void shouldDetectTP2111AsOpAmp(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect TP2304 rail-to-rail op-amp variants as OPAMP")
        @ValueSource(strings = {
            "TP2304", "TP2304-TR", "TP2304TR", "TP2304-QR"
        })
        void shouldDetectTP2304AsOpAmp(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
        }

        @ParameterizedTest
        @DisplayName("Should detect additional op-amp part numbers")
        @ValueSource(strings = {
            "TP2071", "TP2072", "TP2082", "TP2092",
            "TP2231", "TP2232"
        })
        void shouldDetectAdditionalOpAmps(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
        }

        @Test
        @DisplayName("Op-amps should NOT match VOLTAGE_REGULATOR")
        void opAmpsShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("TP1541", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP1541 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("TP2111", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP2111 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("TP2304", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP2304 should NOT match VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("Comparator Detection (TP1561, TP2345)")
    class ComparatorTests {

        @ParameterizedTest
        @DisplayName("Should detect TP1561 comparator variants as IC")
        @ValueSource(strings = {
            "TP1561", "TP1561-TR", "TP1561TR", "TP1561-QR",
            "TP1561-DR"
        })
        void shouldDetectTP1561AsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect TP2345 comparator variants as IC")
        @ValueSource(strings = {
            "TP2345", "TP2345-TR", "TP2345TR", "TP2345-QR"
        })
        void shouldDetectTP2345AsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect additional comparator part numbers")
        @ValueSource(strings = {
            "TP1393", "TP2393"
        })
        void shouldDetectAdditionalComparators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Comparators should identify correctly via getComponentCategory")
        void comparatorsShouldIdentifyCorrectly() {
            assertEquals("Comparator", handler.getComponentCategory("TP1561"));
            assertEquals("Comparator", handler.getComponentCategory("TP2345"));
            assertEquals("Comparator", handler.getComponentCategory("TP1393"));
            assertEquals("Comparator", handler.getComponentCategory("TP2393"));
        }
    }

    @Nested
    @DisplayName("LDO Regulator Detection (TP7xxx)")
    class LDOTests {

        @ParameterizedTest
        @DisplayName("Should detect TP7140 LDO variants as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "TP7140", "TP7140-TR", "TP7140TR", "TP7140-QR",
            "TP7140-DR", "TP7140-MR"
        })
        void shouldDetectTP7140AsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect TP7150 LDO variants as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
            "TP7150", "TP7150-TR", "TP7150TR", "TP7150-QR",
            "TP7150-MR"
        })
        void shouldDetectTP7150AsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect various TP7xxx LDOs")
        @ValueSource(strings = {
            "TP7101", "TP7102", "TP7130", "TP7133",
            "TP7160", "TP7180", "TP7200"
        })
        void shouldDetectVariousTP7xxxLDOs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("TP7xxx should NOT match OPAMP")
        void tp7xxxShouldNotMatchOpAmp() {
            assertFalse(handler.matches("TP7140", ComponentType.OPAMP, registry),
                    "TP7140 should NOT match OPAMP");
            assertFalse(handler.matches("TP7150", ComponentType.OPAMP, registry),
                    "TP7150 should NOT match OPAMP");
        }

        @Test
        @DisplayName("LDOs should identify correctly via getComponentCategory")
        void ldosShouldIdentifyCorrectly() {
            assertEquals("LDO", handler.getComponentCategory("TP7140"));
            assertEquals("LDO", handler.getComponentCategory("TP7150"));
            assertEquals("LDO", handler.getComponentCategory("TP7133"));
        }
    }

    @Nested
    @DisplayName("ADC Detection (TP5xxx)")
    class ADCTests {

        @ParameterizedTest
        @DisplayName("Should detect TP5551 ADC variants as IC")
        @ValueSource(strings = {
            "TP5551", "TP5551-TR", "TP5551TR", "TP5551-QR"
        })
        void shouldDetectTP5551AsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect TP5854 ADC variants as IC")
        @ValueSource(strings = {
            "TP5854", "TP5854-TR", "TP5854TR", "TP5854-QR"
        })
        void shouldDetectTP5854AsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect various TP5xxx ADCs")
        @ValueSource(strings = {
            "TP5100", "TP5200", "TP5300", "TP5400",
            "TP5600", "TP5700", "TP5800", "TP5900"
        })
        void shouldDetectVariousTP5xxxADCs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("TP5xxx should NOT match VOLTAGE_REGULATOR")
        void tp5xxxShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("TP5551", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP5551 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("TP5854", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP5854 should NOT match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("ADCs should identify correctly via getComponentCategory")
        void adcsShouldIdentifyCorrectly() {
            assertEquals("ADC", handler.getComponentCategory("TP5551"));
            assertEquals("ADC", handler.getComponentCategory("TP5854"));
            assertEquals("ADC", handler.getComponentCategory("TP5100"));
        }
    }

    @Nested
    @DisplayName("Current Sense Amplifier Detection (TP1xx)")
    class CurrentSenseTests {

        @ParameterizedTest
        @DisplayName("Should detect TP181 current sense amp variants as OPAMP")
        @ValueSource(strings = {
            "TP181", "TP181-TR", "TP181TR", "TP181-QR",
            "TP181-DR"
        })
        void shouldDetectTP181AsOpAmp(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect TP182 current sense amp variants as OPAMP")
        @ValueSource(strings = {
            "TP182", "TP182-TR", "TP182TR", "TP182-QR"
        })
        void shouldDetectTP182AsOpAmp(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
        }

        @ParameterizedTest
        @DisplayName("Should detect various TP1xx current sense amps")
        @ValueSource(strings = {
            "TP183", "TP184", "TP185", "TP186",
            "TP190", "TP191", "TP199"
        })
        void shouldDetectVariousTP1xxCurrentSense(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
        }

        @Test
        @DisplayName("Current sense amps should NOT match VOLTAGE_REGULATOR")
        void currentSenseShouldNotMatchVoltageRegulator() {
            assertFalse(handler.matches("TP181", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP181 should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("TP182", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP182 should NOT match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Current sense amps should identify correctly via getComponentCategory")
        void currentSenseShouldIdentifyCorrectly() {
            assertEquals("Current Sense", handler.getComponentCategory("TP181"));
            assertEquals("Current Sense", handler.getComponentCategory("TP182"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract standard package codes")
        @CsvSource({
            "TP1541-TR, SOT-23",
            "TP2111-QR, QFN",
            "TP7150-MR, MSOP",
            "TP181-DR, SOIC"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes without hyphen")
        @CsvSource({
            "TP1541TR, SOT-23",
            "TP2111QR, QFN",
            "TP7150MR, MSOP",
            "TP181DR, SOIC"
        })
        void shouldExtractPackageCodesNoHyphen(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should handle additional package codes")
        @CsvSource({
            "TP1541-SR, SOP",
            "TP2111-PR, TSSOP"
        })
        void shouldHandleAdditionalPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @Test
        @DisplayName("Should return raw code for unknown package codes")
        void shouldReturnRawCodeForUnknown() {
            String result = handler.extractPackageCode("TP1541-XY");
            // Should return XY if not in lookup table
            assertEquals("XY", result);
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
        @DisplayName("Should extract TP series codes for 4-digit parts")
        @CsvSource({
            "TP1541-TR, TP1",
            "TP2111-QR, TP2",
            "TP5551-TR, TP5",
            "TP7140-MR, TP7"
        })
        void shouldExtractTPSeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TP18 series for current sense amps")
        @ValueSource(strings = {"TP181-TR", "TP182-QR", "TP183DR"})
        void shouldExtractTP18Series(String mpn) {
            assertEquals("TP18", handler.extractSeries(mpn),
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
            assertTrue(handler.isOfficialReplacement("TP1541-TR", "TP1541-QR"),
                    "TP1541-TR and TP1541-QR should be replacements (same part, different package)");
            assertTrue(handler.isOfficialReplacement("TP2111-DR", "TP2111-MR"),
                    "TP2111-DR and TP2111-MR should be replacements");
            assertTrue(handler.isOfficialReplacement("TP7150TR", "TP7150QR"),
                    "TP7150TR and TP7150QR should be replacements");
        }

        @Test
        @DisplayName("Same part with and without suffix should be replacements")
        void samePartWithSuffix() {
            assertTrue(handler.isOfficialReplacement("TP1541", "TP1541-TR"),
                    "TP1541 and TP1541-TR should be replacements");
            assertTrue(handler.isOfficialReplacement("TP7140", "TP7140-MR"),
                    "TP7140 and TP7140-MR should be replacements");
        }

        @Test
        @DisplayName("Different parts should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("TP1541", "TP2111"),
                    "TP1541 and TP2111 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("TP7140", "TP7150"),
                    "TP7140 and TP7150 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("TP1541", "TP7140"),
                    "TP1541 (op-amp) and TP7140 (LDO) should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("TP1541", "TP5551"),
                    "TP1541 and TP5551 should NOT be replacements (different series)");
            assertFalse(handler.isOfficialReplacement("TP181", "TP2111"),
                    "TP181 and TP2111 should NOT be replacements (different series)");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "TP1541-TR"));
            assertFalse(handler.isOfficialReplacement("TP1541-TR", null));
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
            assertTrue(types.contains(ComponentType.OPAMP));
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
    }

    @Nested
    @DisplayName("Series Description")
    class SeriesDescriptionTests {

        @ParameterizedTest
        @DisplayName("Should return correct series descriptions")
        @CsvSource({
            "TP1, Op-Amps/Comparators",
            "TP2, Precision Op-Amps/Comparators",
            "TP5, ADCs",
            "TP7, LDO Regulators",
            "TP18, Current Sense Amplifiers"
        })
        void shouldReturnSeriesDescription(String series, String expectedDesc) {
            assertEquals(expectedDesc, handler.getSeriesDescription(series));
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getSeriesDescription("UNKNOWN"));
            assertEquals("", handler.getSeriesDescription(""));
        }
    }

    @Nested
    @DisplayName("Component Category Detection")
    class ComponentCategoryTests {

        @ParameterizedTest
        @DisplayName("Should categorize components correctly")
        @CsvSource({
            "TP1541, Op-Amp",
            "TP2111, Op-Amp",
            "TP2304, Op-Amp",
            "TP1561, Comparator",
            "TP2345, Comparator",
            "TP7140, LDO",
            "TP7150, LDO",
            "TP5551, ADC",
            "TP5854, ADC",
            "TP181, Current Sense",
            "TP182, Current Sense"
        })
        void shouldCategorizeCorrectly(String mpn, String expectedCategory) {
            assertEquals(expectedCategory, handler.getComponentCategory(mpn),
                    "Category for " + mpn);
        }

        @Test
        @DisplayName("Should return Unknown for invalid MPN")
        void shouldReturnUnknownForInvalid() {
            assertEquals("Unknown", handler.getComponentCategory(null));
            assertEquals("Unknown", handler.getComponentCategory("INVALID"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.OPAMP, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "TP1541"));
            assertFalse(handler.isOfficialReplacement("TP1541", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.OPAMP, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("TP1541", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("tp1541-tr", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("TP1541-TR", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("Tp1541-Tr", ComponentType.OPAMP, registry));
        }

        @Test
        @DisplayName("Should not match non-3PEAK parts")
        void shouldNotMatchNon3PeakParts() {
            assertFalse(handler.matches("LM358", ComponentType.OPAMP, registry),
                    "LM358 is not a 3PEAK part");
            assertFalse(handler.matches("TL082", ComponentType.OPAMP, registry),
                    "TL082 is not a 3PEAK part");
            assertFalse(handler.matches("LM7805", ComponentType.VOLTAGE_REGULATOR, registry),
                    "LM7805 is not a 3PEAK part");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("TP", ComponentType.OPAMP, registry));
            assertFalse(handler.matches("TP1", ComponentType.OPAMP, registry));
            assertFalse(handler.matches("TP15", ComponentType.OPAMP, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular 3PEAK parts")
        @CsvSource({
            "TP1541-TR, OPAMP",
            "TP2111-QR, OPAMP",
            "TP2304-TR, OPAMP",
            "TP1561-DR, IC",
            "TP2345-TR, IC",
            "TP7140-MR, VOLTAGE_REGULATOR",
            "TP7150-TR, VOLTAGE_REGULATOR",
            "TP5551-QR, IC",
            "TP5854-TR, IC",
            "TP181-DR, OPAMP",
            "TP182-TR, OPAMP"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
        }

        @Test
        @DisplayName("Should handle full MPN with all components")
        void shouldHandleFullMPN() {
            String fullMpn = "TP1541-TR";
            assertTrue(handler.matches(fullMpn, ComponentType.OPAMP, registry));
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertEquals("SOT-23", handler.extractPackageCode(fullMpn));
            assertEquals("TP1", handler.extractSeries(fullMpn));
            assertEquals("Op-Amp", handler.getComponentCategory(fullMpn));
        }

        @Test
        @DisplayName("Should handle LDO with full MPN")
        void shouldHandleLDOFullMPN() {
            String fullMpn = "TP7150-MR";
            assertTrue(handler.matches(fullMpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertEquals("MSOP", handler.extractPackageCode(fullMpn));
            assertEquals("TP7", handler.extractSeries(fullMpn));
            assertEquals("LDO", handler.getComponentCategory(fullMpn));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All 3PEAK parts should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "TP1541-TR", "TP2111-QR", "TP2304-TR",
                "TP1561-DR", "TP2345-TR",
                "TP7140-MR", "TP7150-TR",
                "TP5551-QR", "TP5854-TR",
                "TP181-DR", "TP182-TR"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("Only op-amps and current sense amps should match OPAMP")
        void onlyOpAmpsShouldMatchOpAmp() {
            // Should match OPAMP
            assertTrue(handler.matches("TP1541", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("TP2111", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("TP181", ComponentType.OPAMP, registry));

            // Should NOT match OPAMP
            assertFalse(handler.matches("TP1561", ComponentType.OPAMP, registry),
                    "TP1561 (comparator) should NOT match OPAMP");
            assertFalse(handler.matches("TP7140", ComponentType.OPAMP, registry),
                    "TP7140 (LDO) should NOT match OPAMP");
            assertFalse(handler.matches("TP5551", ComponentType.OPAMP, registry),
                    "TP5551 (ADC) should NOT match OPAMP");
        }

        @Test
        @DisplayName("Only LDOs should match VOLTAGE_REGULATOR")
        void onlyLDOsShouldMatchVoltageRegulator() {
            // Should match VOLTAGE_REGULATOR
            assertTrue(handler.matches("TP7140", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("TP7150", ComponentType.VOLTAGE_REGULATOR, registry));

            // Should NOT match VOLTAGE_REGULATOR
            assertFalse(handler.matches("TP1541", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP1541 (op-amp) should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("TP5551", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP5551 (ADC) should NOT match VOLTAGE_REGULATOR");
            assertFalse(handler.matches("TP181", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TP181 (current sense) should NOT match VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("Pattern Boundary Tests")
    class PatternBoundaryTests {

        @Test
        @DisplayName("Should handle 3-digit part numbers correctly")
        void shouldHandle3DigitPartNumbers() {
            // Current sense amps are 3 digits
            assertTrue(handler.matches("TP181", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("TP182", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("TP199", ComponentType.OPAMP, registry));
        }

        @Test
        @DisplayName("Should handle 4-digit part numbers correctly")
        void shouldHandle4DigitPartNumbers() {
            assertTrue(handler.matches("TP1541", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("TP2111", ComponentType.OPAMP, registry));
            assertTrue(handler.matches("TP7140", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Should reject invalid digit counts")
        void shouldRejectInvalidDigitCounts() {
            // Too few digits
            assertFalse(handler.matches("TP1", ComponentType.IC, registry));
            assertFalse(handler.matches("TP15", ComponentType.IC, registry));

            // Part numbers must be TP + 3 or 4 digits
            // TP + 5 digits would be unusual for 3PEAK
        }
    }
}
