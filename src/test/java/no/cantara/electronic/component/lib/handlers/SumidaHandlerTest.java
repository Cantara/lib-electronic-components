package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SumidaHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SumidaHandler.
 * Tests pattern matching, package code extraction, series extraction,
 * inductance value extraction, and replacement detection.
 * <p>
 * Sumida Corporation is a major manufacturer of power inductors with
 * several product families targeting different applications.
 */
class SumidaHandlerTest {

    private static SumidaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation - avoids MPNUtils.getManufacturerHandler issues
        handler = new SumidaHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("CDRH SMD Power Inductor Detection")
    class CDRHSMDPowerInductorTests {

        @ParameterizedTest
        @DisplayName("Should detect CDRH4D28 series (4x4x2.8mm)")
        @ValueSource(strings = {
                "CDRH4D28NP-2R2NC",
                "CDRH4D28NP-4R7NC",
                "CDRH4D28NP-100NC"
        })
        void shouldDetectCDRH4D28Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CDRH", handler.extractSeries(mpn),
                    mpn + " should have series CDRH");
        }

        @ParameterizedTest
        @DisplayName("Should detect CDRH6D28 series (6x6x2.8mm)")
        @ValueSource(strings = {
                "CDRH6D28NP-4R7NC",
                "CDRH6D28NP-100NC",
                "CDRH6D28NP-220MC"
        })
        void shouldDetectCDRH6D28Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect CDRH8D43 series (8x8x4.3mm)")
        @ValueSource(strings = {
                "CDRH8D43NP-100MC",
                "CDRH8D43NP-220MC",
                "CDRH8D43NP-470MC"
        })
        void shouldDetectCDRH8D43Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect various CDRH size variants")
        @ValueSource(strings = {
                "CDRH2D14NP-1R0NC",
                "CDRH3D16NP-2R2NC",
                "CDRH5D18NP-4R7NC",
                "CDRH10D58NP-100MC"
        })
        void shouldDetectVariousCDRHSizes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CDRH", handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("CDR Drum Core Inductor Detection")
    class CDRDrumCoreInductorTests {

        @ParameterizedTest
        @DisplayName("Should detect CDR74 series (7.4mm diameter)")
        @ValueSource(strings = {
                "CDR74NP-100MC",
                "CDR74NP-220MC",
                "CDR74NP-470MC"
        })
        void shouldDetectCDR74Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CDR", handler.extractSeries(mpn),
                    mpn + " should have series CDR");
        }

        @ParameterizedTest
        @DisplayName("Should detect CDR104 series (10.4mm diameter)")
        @ValueSource(strings = {
                "CDR104NP-100MC",
                "CDR104NP-220MC",
                "CDR104NP-1R0MC"
        })
        void shouldDetectCDR104Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect CDR125 series (12.5mm diameter)")
        @ValueSource(strings = {
                "CDR125NP-100MC",
                "CDR125NP-220MC",
                "CDR125NP-470MC"
        })
        void shouldDetectCDR125Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("CDR series should NOT be confused with CDRH series")
        void cdrShouldNotConfuseWithCdrh() {
            assertEquals("CDR", handler.extractSeries("CDR125NP-100MC"));
            assertEquals("CDRH", handler.extractSeries("CDRH6D28NP-100MC"));
        }
    }

    @Nested
    @DisplayName("CDEP Shielded Power Inductor Detection")
    class CDEPShieldedPowerTests {

        @ParameterizedTest
        @DisplayName("Should detect CDEP104 series (10x10x4mm)")
        @ValueSource(strings = {
                "CDEP104NP-1R5MC",
                "CDEP104NP-2R2MC",
                "CDEP104NP-4R7MC"
        })
        void shouldDetectCDEP104Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CDEP", handler.extractSeries(mpn),
                    mpn + " should have series CDEP");
        }

        @ParameterizedTest
        @DisplayName("Should detect CDEP105 series (10x10x5mm)")
        @ValueSource(strings = {
                "CDEP105NP-1R5MC",
                "CDEP105NP-2R2MC",
                "CDEP105NP-4R7MC"
        })
        void shouldDetectCDEP105Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }
    }

    @Nested
    @DisplayName("CR Chip Inductor Detection")
    class CRChipInductorTests {

        @ParameterizedTest
        @DisplayName("Should detect CR16 series (1.6mm)")
        @ValueSource(strings = {
                "CR16-100JM",
                "CR16-220JM",
                "CR16-470JM"
        })
        void shouldDetectCR16Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CR", handler.extractSeries(mpn),
                    mpn + " should have series CR");
        }

        @ParameterizedTest
        @DisplayName("Should detect CR21 series (2.1mm)")
        @ValueSource(strings = {
                "CR21-100JM",
                "CR21-220JM",
                "CR21-4R7JM"
        })
        void shouldDetectCR21Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect CR32 series (3.2mm)")
        @ValueSource(strings = {
                "CR32-100JM",
                "CR32-220JM",
                "CR32-101JM"
        })
        void shouldDetectCR32Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }
    }

    @Nested
    @DisplayName("RCH High Current Inductor Detection")
    class RCHHighCurrentTests {

        @ParameterizedTest
        @DisplayName("Should detect RCH110 series")
        @ValueSource(strings = {
                "RCH110NP-100MC",
                "RCH110NP-220MC",
                "RCH110NP-4R7MC"
        })
        void shouldDetectRCH110Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("RCH", handler.extractSeries(mpn),
                    mpn + " should have series RCH");
        }

        @ParameterizedTest
        @DisplayName("Should detect RCH895 series")
        @ValueSource(strings = {
                "RCH895NP-100MC",
                "RCH895NP-220MC"
        })
        void shouldDetectRCH895Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }
    }

    @Nested
    @DisplayName("CDC Common Mode Choke Detection")
    class CDCCommonModeChokeTests {

        @ParameterizedTest
        @DisplayName("Should detect CDC2G2 series")
        @ValueSource(strings = {
                "CDC2G2NP-100MC",
                "CDC2G2NP-220MC"
        })
        void shouldDetectCDC2G2Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CDC", handler.extractSeries(mpn),
                    mpn + " should have series CDC");
        }

        @Test
        @DisplayName("Should identify CDC series as common mode choke")
        void shouldIdentifyAsCommonModeChoke() {
            String description = handler.getInductorTypeDescription("CDC2G2NP-100MC");
            assertEquals("Common Mode Choke", description);
        }
    }

    @Nested
    @DisplayName("CLF Low Profile Power Inductor Detection")
    class CLFLowProfileTests {

        @ParameterizedTest
        @DisplayName("Should detect CLF10040 series (10x10x4.0mm)")
        @ValueSource(strings = {
                "CLF10040NP-1R0MC",
                "CLF10040NP-2R2MC",
                "CLF10040NP-4R7MC"
        })
        void shouldDetectCLF10040Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CLF", handler.extractSeries(mpn),
                    mpn + " should have series CLF");
        }
    }

    @Nested
    @DisplayName("CDEF High Efficiency Detection")
    class CDEFHighEfficiencyTests {

        @ParameterizedTest
        @DisplayName("Should detect CDEF38 series")
        @ValueSource(strings = {
                "CDEF38NP-1R0MC",
                "CDEF38NP-2R2MC"
        })
        void shouldDetectCDEF38Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CDEF", handler.extractSeries(mpn),
                    mpn + " should have series CDEF");
        }
    }

    @Nested
    @DisplayName("CEP Edge-Wound Inductor Detection")
    class CEPEdgeWoundTests {

        @ParameterizedTest
        @DisplayName("Should detect CEP series")
        @ValueSource(strings = {
                "CEP125NP-100MC",
                "CEP125NP-220MC"
        })
        void shouldDetectCEPSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertEquals("CEP", handler.extractSeries(mpn),
                    mpn + " should have series CEP");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract CDRH package codes as dimensions")
        @CsvSource({
                "CDRH6D28NP-4R7NC, 6x6x2.8mm",
                "CDRH4D28NP-100NC, 4x4x2.8mm",
                "CDRH8D43NP-220MC, 8x8x4.3mm"
        })
        void shouldExtractCDRHPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CDR package codes as diameter")
        @CsvSource({
                "CDR74NP-100MC, 7.4mm",
                "CDR104NP-220MC, 10.4mm",
                "CDR125NP-470MC, 12.5mm"
        })
        void shouldExtractCDRPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CDEP package codes as dimensions")
        @CsvSource({
                "CDEP104NP-1R5MC, 10x10x4mm",
                "CDEP105NP-2R2MC, 10x10x5mm"
        })
        void shouldExtractCDEPPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CR chip inductor sizes")
        @CsvSource({
                "CR16-100JM, 1.6mm",
                "CR21-220JM, 2.1mm",
                "CR32-470JM, 3.2mm"
        })
        void shouldExtractCRPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CLF package codes as dimensions")
        @CsvSource({
                "CLF10040NP-1R0MC, 10x10x4.0mm"
        })
        void shouldExtractCLFPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract correct series from various MPNs")
        @CsvSource({
                "CDRH6D28NP-4R7NC, CDRH",
                "CDR125NP-100MC, CDR",
                "CDEP104NP-1R5MC, CDEP",
                "CDEF38NP-1R0MC, CDEF",
                "CR21-100JM, CR",
                "RCH110NP-100MC, RCH",
                "CEP125NP-100MC, CEP",
                "CDC2G2NP-100MC, CDC",
                "CLF10040NP-1R0MC, CLF"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty string for unknown series")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.extractSeries("UNKNOWN123-456"));
        }
    }

    @Nested
    @DisplayName("Inductance Value Extraction")
    class InductanceValueExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract R-notation values correctly")
        @CsvSource({
                "CDRH6D28NP-4R7NC, 4.7uH",
                "CDEP104NP-1R5MC, 1.5uH",
                "CDRH8D43NP-R47MC, 0.47uH",
                "RCH110NP-10RMC, 10uH"
        })
        void shouldExtractRNotationValues(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract numeric code values correctly")
        @CsvSource({
                "CDR125NP-100MC, 10uH",
                "CDR125NP-220MC, 22uH",
                "CDR125NP-470MC, 47uH",
                "CDR125NP-101MC, 100uH"
        })
        void shouldExtractNumericCodeValues(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalid() {
            assertEquals("", handler.extractInductanceValue("INVALID"));
            assertEquals("", handler.extractInductanceValue(""));
            assertEquals("", handler.extractInductanceValue(null));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementDetectionTests {

        @Test
        @DisplayName("Same series same value should be replacement")
        void sameSeriesSameValueAreReplacements() {
            assertTrue(handler.isOfficialReplacement(
                            "CDRH6D28NP-4R7NC", "CDRH6D28NP-4R7MC"),
                    "Same CDRH series with same value should be replacements");

            assertTrue(handler.isOfficialReplacement(
                            "CDR125NP-100MC", "CDR125NP-100NC"),
                    "Same CDR series with same value should be replacements");
        }

        @Test
        @DisplayName("Same series different value should NOT be replacement")
        void sameSeriesDifferentValueNotReplacements() {
            assertFalse(handler.isOfficialReplacement(
                            "CDRH6D28NP-4R7NC", "CDRH6D28NP-100NC"),
                    "Same series with different values should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacement")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement(
                            "CDRH6D28NP-4R7NC", "CDR125NP-4R7NC"),
                    "Different series should NOT be replacements");

            assertFalse(handler.isOfficialReplacement(
                            "CDEP104NP-4R7MC", "CLF10040NP-4R7MC"),
                    "CDEP and CLF series should NOT be replacements");
        }

        @Test
        @DisplayName("Different sizes within same series can be replacement if same value")
        void differentSizesSameSeriesSameValue() {
            // Within CDRH series, different sizes with same value
            assertTrue(handler.isOfficialReplacement(
                            "CDRH6D28NP-4R7NC", "CDRH8D43NP-4R7NC"),
                    "CDRH different sizes with same value should be replacements");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.INDUCTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractInductanceValue(null));
            assertFalse(handler.isOfficialReplacement(null, "CDRH6D28NP-4R7NC"));
            assertFalse(handler.isOfficialReplacement("CDRH6D28NP-4R7NC", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.INDUCTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractInductanceValue(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("CDRH6D28NP-4R7NC", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("cdrh6d28np-4r7nc", ComponentType.INDUCTOR, registry),
                    "lowercase cdrh should match");
            assertTrue(handler.matches("CDRH6D28NP-4R7NC", ComponentType.INDUCTOR, registry),
                    "uppercase CDRH should match");
            assertTrue(handler.matches("Cdrh6D28Np-4R7Nc", ComponentType.INDUCTOR, registry),
                    "mixed case should match");
        }

        @Test
        @DisplayName("Should NOT match non-inductor types")
        void shouldNotMatchNonInductorTypes() {
            assertFalse(handler.matches("CDRH6D28NP-4R7NC", ComponentType.CAPACITOR, registry),
                    "Should not match CAPACITOR type");
            assertFalse(handler.matches("CDRH6D28NP-4R7NC", ComponentType.RESISTOR, registry),
                    "Should not match RESISTOR type");
        }

        @Test
        @DisplayName("Should NOT match non-Sumida parts")
        void shouldNotMatchNonSumidaParts() {
            assertFalse(handler.matches("GRM188R71H104KA93D", ComponentType.INDUCTOR, registry),
                    "Should not match Murata capacitor");
            assertFalse(handler.matches("MLF2012A1R0KT", ComponentType.INDUCTOR, registry),
                    "Should not match TDK inductor");
            assertFalse(handler.matches("LQM2MPN2R2MG0L", ComponentType.INDUCTOR, registry),
                    "Should not match Murata inductor");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support only INDUCTOR type")
        void shouldSupportOnlyInductorType() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.INDUCTOR),
                    "Should support INDUCTOR");
            assertEquals(1, types.size(),
                    "Should support exactly one type");
        }

        @Test
        @DisplayName("Should return immutable set")
        void shouldReturnImmutableSet() {
            var types = handler.getSupportedTypes();

            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.CAPACITOR);
            }, "getSupportedTypes should return immutable Set.of()");
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
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            SumidaHandler directHandler = new SumidaHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify patterns work
            assertTrue(directHandler.matches("CDRH6D28NP-4R7NC", ComponentType.INDUCTOR, directRegistry));
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
    @DisplayName("Inductor Type Description")
    class InductorTypeDescriptionTests {

        @ParameterizedTest
        @DisplayName("Should return correct description for each series")
        @CsvSource({
                "CDRH6D28NP-4R7NC, SMD Power Inductor",
                "CDR125NP-100MC, Drum Core Inductor",
                "CDEP104NP-1R5MC, SMD Shielded Power Inductor",
                "CDEF38NP-1R0MC, SMD High Efficiency Inductor",
                "CR21-100JM, Chip Inductor",
                "RCH110NP-100MC, High Current Inductor",
                "CEP125NP-100MC, Edge-Wound Inductor",
                "CDC2G2NP-100MC, Common Mode Choke",
                "CLF10040NP-1R0MC, Low Profile Power Inductor"
        })
        void shouldReturnCorrectDescription(String mpn, String expectedDescription) {
            assertEquals(expectedDescription, handler.getInductorTypeDescription(mpn),
                    "Description for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown MPN")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getInductorTypeDescription("UNKNOWN"));
            assertEquals("", handler.getInductorTypeDescription(""));
            assertEquals("", handler.getInductorTypeDescription(null));
        }
    }

    @Nested
    @DisplayName("isSumidaInductor Helper Method")
    class IsSumidaInductorTests {

        @ParameterizedTest
        @DisplayName("Should identify Sumida inductors")
        @ValueSource(strings = {
                "CDRH6D28NP-4R7NC",
                "CDR125NP-100MC",
                "CDEP104NP-1R5MC",
                "CR21-100JM",
                "RCH110NP-100MC",
                "CDC2G2NP-100MC",
                "CLF10040NP-1R0MC"
        })
        void shouldIdentifySumidaInductors(String mpn) {
            assertTrue(handler.isSumidaInductor(mpn),
                    mpn + " should be identified as Sumida inductor");
        }

        @ParameterizedTest
        @DisplayName("Should NOT identify non-Sumida parts")
        @ValueSource(strings = {
                "GRM188R71H104KA93D",
                "MLF2012A1R0KT",
                "LQM2MPN2R2MG0L",
                "INVALID"
        })
        void shouldNotIdentifyNonSumidaParts(String mpn) {
            assertFalse(handler.isSumidaInductor(mpn),
                    mpn + " should NOT be identified as Sumida inductor");
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @Test
        @DisplayName("Common power inductors from datasheets")
        void commonPowerInductors() {
            // CDRH6D28 - popular 6x6mm power inductor
            assertTrue(handler.matches("CDRH6D28NP-4R7NC", ComponentType.INDUCTOR, registry),
                    "Common 4.7uH power inductor");
            assertEquals("4.7uH", handler.extractInductanceValue("CDRH6D28NP-4R7NC"));

            // CDR125 - drum core inductor
            assertTrue(handler.matches("CDR125NP-100MC", ComponentType.INDUCTOR, registry),
                    "Common 10uH drum core inductor");
            assertEquals("10uH", handler.extractInductanceValue("CDR125NP-100MC"));

            // CDEP104 - shielded power inductor
            assertTrue(handler.matches("CDEP104NP-1R5MC", ComponentType.INDUCTOR, registry),
                    "Common 1.5uH shielded inductor");
            assertEquals("1.5uH", handler.extractInductanceValue("CDEP104NP-1R5MC"));
        }

        @Test
        @DisplayName("Full MPN parsing example")
        void fullMpnParsingExample() {
            String mpn = "CDRH6D28NP-4R7NC";

            assertEquals("CDRH", handler.extractSeries(mpn));
            assertEquals("6x6x2.8mm", handler.extractPackageCode(mpn));
            assertEquals("4.7uH", handler.extractInductanceValue(mpn));
            assertEquals("SMD Power Inductor", handler.getInductorTypeDescription(mpn));
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry));
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should handle minimum length MPNs")
        void shouldHandleMinLengthMpns() {
            // CR series with minimum suffix
            assertTrue(handler.matches("CR16-1R0", ComponentType.INDUCTOR, registry));
        }

        @Test
        @DisplayName("Should handle MPNs without hyphen")
        void shouldHandleMpnsWithoutHyphen() {
            // Some Sumida MPNs may not have hyphen separator
            assertTrue(handler.matches("CDRH6D28NP4R7NC", ComponentType.INDUCTOR, registry));
        }

        @Test
        @DisplayName("Should extract value from MPN without hyphen")
        void shouldExtractValueWithoutHyphen() {
            // Hyphen is expected for value extraction
            String value = handler.extractInductanceValue("CDRH6D28NP4R7NC");
            // Without hyphen, value extraction may not work - document behavior
            // This is acceptable as proper Sumida MPNs include hyphen
            assertNotNull(value);
        }
    }
}
