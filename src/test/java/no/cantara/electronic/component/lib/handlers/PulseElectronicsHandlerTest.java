package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.PulseElectronicsHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for PulseElectronicsHandler.
 *
 * Tests pattern matching, package code extraction, series extraction,
 * and replacement detection for Pulse Electronics magnetics and power
 * components including transformers, inductors, and LAN magnetics.
 */
class PulseElectronicsHandlerTest {

    private static PulseElectronicsHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new PulseElectronicsHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("H Series LAN Transformer Detection")
    class HSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect H series LAN transformers")
        @ValueSource(strings = {
                "H1012NL",
                "H1102NL",
                "H1188NL",
                "H1102G",
                "H1012NLT",
                "H5001NL"
        })
        void shouldDetectHSeriesTransformers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSFORMER_BOURNS, registry),
                    mpn + " should match TRANSFORMER_BOURNS");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract H series correctly")
        void shouldExtractHSeries() {
            assertEquals("H", handler.extractSeries("H1012NL"));
            assertEquals("H", handler.extractSeries("H1102G"));
            assertEquals("H", handler.extractSeries("H1188NLT"));
        }

        @Test
        @DisplayName("Should identify H series as transformers")
        void shouldIdentifyAsTransformer() {
            assertTrue(handler.isTransformer("H1012NL"));
            assertTrue(handler.isTransformer("H1102G"));
            assertFalse(handler.isInductor("H1012NL"));
            assertFalse(handler.isLanMagnetics("H1012NL"));
        }

        @Test
        @DisplayName("Should get product type for H series")
        void shouldGetProductType() {
            assertEquals("LAN Transformer", handler.getProductType("H1012NL"));
        }
    }

    @Nested
    @DisplayName("T Series Telecom Transformer Detection")
    class TSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect T series telecom transformers")
        @ValueSource(strings = {
                "T1029NL",
                "T3012NL",
                "T1102G",
                "T3024NLT",
                "T5012NL"
        })
        void shouldDetectTSeriesTransformers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSFORMER_BOURNS, registry),
                    mpn + " should match TRANSFORMER_BOURNS");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract T series correctly")
        void shouldExtractTSeries() {
            assertEquals("T", handler.extractSeries("T1029NL"));
            assertEquals("T", handler.extractSeries("T3012G"));
        }

        @Test
        @DisplayName("Should identify T series as transformers")
        void shouldIdentifyAsTransformer() {
            assertTrue(handler.isTransformer("T1029NL"));
            assertTrue(handler.isTransformer("T3012G"));
            assertFalse(handler.isInductor("T1029NL"));
        }

        @Test
        @DisplayName("Should get product type for T series")
        void shouldGetProductType() {
            assertEquals("Telecom Transformer", handler.getProductType("T1029NL"));
        }
    }

    @Nested
    @DisplayName("P Series Power Inductor Detection")
    class PSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect P series power inductors")
        @ValueSource(strings = {
                "P0751SNL",
                "P1166NL",
                "P0751NLT",
                "P1020S",
                "P2010NL"
        })
        void shouldDetectPSeriesInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract P series correctly")
        void shouldExtractPSeries() {
            assertEquals("P", handler.extractSeries("P0751SNL"));
            assertEquals("P", handler.extractSeries("P1166NL"));
        }

        @Test
        @DisplayName("Should identify P series as inductors")
        void shouldIdentifyAsInductor() {
            assertTrue(handler.isInductor("P0751SNL"));
            assertTrue(handler.isInductor("P1166NL"));
            assertFalse(handler.isTransformer("P0751SNL"));
            assertFalse(handler.isLanMagnetics("P0751SNL"));
        }

        @Test
        @DisplayName("Should get product type for P series")
        void shouldGetProductType() {
            assertEquals("Power Inductor", handler.getProductType("P0751SNL"));
        }
    }

    @Nested
    @DisplayName("PA Series Detection")
    class PASeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect PA series parts")
        @ValueSource(strings = {
                "PA-1000NL",
                "PA-2512NL",
                "PA1000NL",
                "PA2512G"
        })
        void shouldDetectPASeriesParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry) ||
                       handler.matches(mpn, ComponentType.TRANSFORMER_BOURNS, registry),
                    mpn + " should match INDUCTOR or TRANSFORMER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract PA series correctly")
        void shouldExtractPASeries() {
            assertEquals("PA", handler.extractSeries("PA-1000NL"));
            assertEquals("PA", handler.extractSeries("PA2512G"));
        }

        @Test
        @DisplayName("Should identify PA series as both inductor and transformer capable")
        void shouldIdentifyPASeriesType() {
            // PA series can be either automotive inductors or power adapters
            assertTrue(handler.isInductor("PA-1000NL"));
            assertTrue(handler.isTransformer("PA-1000NL"));
        }

        @Test
        @DisplayName("Should get product type for PA series")
        void shouldGetProductType() {
            assertEquals("Power Adapter", handler.getProductType("PA-1000NL"));
        }
    }

    @Nested
    @DisplayName("PE Series Detection")
    class PESeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect PE series power electronics")
        @ValueSource(strings = {
                "PE-53680NL",
                "PE-63851NL",
                "PE-53680NLT",
                "PE-63851G"
        })
        void shouldDetectPESeriesParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry) ||
                       handler.matches(mpn, ComponentType.TRANSFORMER_BOURNS, registry),
                    mpn + " should match INDUCTOR or TRANSFORMER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract PE series correctly")
        void shouldExtractPESeries() {
            assertEquals("PE", handler.extractSeries("PE-53680NL"));
            assertEquals("PE", handler.extractSeries("PE-63851G"));
        }

        @Test
        @DisplayName("Should identify PE series as both inductor and transformer capable")
        void shouldIdentifyPESeriesType() {
            assertTrue(handler.isInductor("PE-53680NL"));
            assertTrue(handler.isTransformer("PE-53680NL"));
        }

        @Test
        @DisplayName("Should get product type for PE series")
        void shouldGetProductType() {
            assertEquals("Power Electronics", handler.getProductType("PE-53680NL"));
        }
    }

    @Nested
    @DisplayName("J Series LAN Magnetics Detection")
    class JSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect J series RJ45 with magnetics")
        @ValueSource(strings = {
                "JD0-0001NL",
                "JD0-0005NL",
                "JD1-0001NL",
                "JD0-0001NLT",
                "JD2-0003G"
        })
        void shouldDetectJSeriesLanMagnetics(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract J series correctly")
        void shouldExtractJSeries() {
            assertEquals("J", handler.extractSeries("JD0-0001NL"));
            assertEquals("J", handler.extractSeries("JD1-0005NL"));
        }

        @Test
        @DisplayName("Should identify J series as LAN magnetics")
        void shouldIdentifyAsLanMagnetics() {
            assertTrue(handler.isLanMagnetics("JD0-0001NL"));
            assertFalse(handler.isTransformer("JD0-0001NL"));
            assertFalse(handler.isInductor("JD0-0001NL"));
        }

        @Test
        @DisplayName("Should get product type for J series")
        void shouldGetProductType() {
            assertEquals("LAN Magnetics RJ45", handler.getProductType("JD0-0001NL"));
        }
    }

    @Nested
    @DisplayName("JK Series RJ45 Jack Detection")
    class JKSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect JK series RJ45 jacks")
        @ValueSource(strings = {
                "JK-0001NL",
                "JK0-1000NL",
                "JK1-2000G",
                "JK-0001NLT"
        })
        void shouldDetectJKSeriesConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract JK series correctly")
        void shouldExtractJKSeries() {
            assertEquals("JK", handler.extractSeries("JK-0001NL"));
            assertEquals("JK", handler.extractSeries("JK0-1000NL"));
        }

        @Test
        @DisplayName("Should identify JK series as LAN magnetics")
        void shouldIdentifyAsLanMagnetics() {
            assertTrue(handler.isLanMagnetics("JK-0001NL"));
        }

        @Test
        @DisplayName("Should get product type for JK series")
        void shouldGetProductType() {
            assertEquals("RJ45 Jack", handler.getProductType("JK-0001NL"));
        }
    }

    @Nested
    @DisplayName("JXD Series 10G LAN Detection")
    class JXDSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect JXD series 10G LAN connectors")
        @ValueSource(strings = {
                "JXD-0001NL",
                "JXD0-1000NL",
                "JXD1-2000G",
                "JXD-0001NLT"
        })
        void shouldDetectJXDSeriesConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract JXD series correctly")
        void shouldExtractJXDSeries() {
            assertEquals("JXD", handler.extractSeries("JXD-0001NL"));
            assertEquals("JXD", handler.extractSeries("JXD0-1000NL"));
        }

        @Test
        @DisplayName("Should identify JXD series as LAN magnetics")
        void shouldIdentifyAsLanMagnetics() {
            assertTrue(handler.isLanMagnetics("JXD-0001NL"));
        }

        @Test
        @DisplayName("Should get product type for JXD series")
        void shouldGetProductType() {
            assertEquals("10G LAN Connector", handler.getProductType("JXD-0001NL"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package code from suffix")
        @CsvSource({
                "H1012NL, SMD Lead-Free",
                "H1012NLT, SMD Lead-Free T&R",
                "P0751SNL, Standard Lead-Free",
                "T1029G, Green/RoHS",
                "JD0-0001NL, SMD Lead-Free"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return raw suffix for unknown package codes")
        void shouldReturnRawSuffixForUnknown() {
            String packageCode = handler.extractPackageCode("H1012XYZ");
            assertEquals("XYZ", packageCode);
        }

        @Test
        @DisplayName("Should return empty string for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractPackageCode("INVALID123"));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractPackageCode(null));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series correctly")
        @CsvSource({
                "H1012NL, H",
                "T1029NL, T",
                "P0751SNL, P",
                "PA-1000NL, PA",
                "PE-53680NL, PE",
                "JD0-0001NL, J",
                "JK-0001NL, JK",
                "JXD-0001NL, JXD"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty string for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("INVALID"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part with different suffix should be replacement")
        void samePart_DifferentSuffix_ShouldBeReplacement() {
            // NL vs NLT (different packaging)
            assertTrue(handler.isOfficialReplacement("H1012NL", "H1012NLT"),
                    "Same part with NL vs NLT should be replaceable");

            // NL vs G (lead-free variants)
            assertTrue(handler.isOfficialReplacement("T1029NL", "T1029G"),
                    "Same part with NL vs G should be replaceable");

            // SNL vs S
            assertTrue(handler.isOfficialReplacement("P0751SNL", "P0751S"),
                    "Same part with SNL vs S should be replaceable");
        }

        @Test
        @DisplayName("Different parts in same series should NOT be replacements")
        void differentParts_SameSeries_ShouldNotBeReplacement() {
            assertFalse(handler.isOfficialReplacement("H1012NL", "H1102NL"),
                    "Different H series parts should not be replaceable");

            assertFalse(handler.isOfficialReplacement("P0751SNL", "P1166NL"),
                    "Different P series parts should not be replaceable");
        }

        @Test
        @DisplayName("Parts from different series should NOT be replacements")
        void differentSeries_ShouldNotBeReplacement() {
            assertFalse(handler.isOfficialReplacement("H1012NL", "T1012NL"),
                    "H vs T series should not be replaceable");

            assertFalse(handler.isOfficialReplacement("JD0-0001NL", "JK-0001NL"),
                    "JD vs JK series should not be replaceable");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputs_ShouldReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "H1012NL"));
            assertFalse(handler.isOfficialReplacement("H1012NL", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Lead-Free Detection")
    class LeadFreeTests {

        @ParameterizedTest
        @DisplayName("Should detect lead-free parts")
        @CsvSource({
                "H1012NL, true",
                "H1012NLT, true",
                "H1012G, true",
                "H1012GNL, true",
                "P0751S, false",
                "T1029, false"
        })
        void shouldDetectLeadFree(String mpn, boolean expectedLeadFree) {
            assertEquals(expectedLeadFree, handler.isLeadFree(mpn),
                    "Lead-free detection for " + mpn);
        }
    }

    @Nested
    @DisplayName("Tape and Reel Detection")
    class TapeAndReelTests {

        @ParameterizedTest
        @DisplayName("Should detect tape and reel variants")
        @CsvSource({
                "H1012NLT, true",
                "H1012T, true",
                "H1012TL, true",
                "H1012NL, false",
                "H1012G, false"
        })
        void shouldDetectTapeAndReel(String mpn, boolean expectedTapeAndReel) {
            assertEquals(expectedTapeAndReel, handler.isTapeAndReel(mpn),
                    "Tape and reel detection for " + mpn);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.INDUCTOR, registry));
            assertFalse(handler.matches(null, ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "H1012NL"));
            assertFalse(handler.isOfficialReplacement("H1012NL", null));
            assertFalse(handler.isLeadFree(null));
            assertFalse(handler.isTapeAndReel(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.INDUCTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertFalse(handler.isLeadFree(""));
            assertFalse(handler.isTapeAndReel(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("H1012NL", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMPNs() {
            assertTrue(handler.matches("h1012nl", ComponentType.TRANSFORMER_BOURNS, registry),
                    "Should handle lowercase MPN");
            assertTrue(handler.matches("jd0-0001nl", ComponentType.CONNECTOR, registry),
                    "Should handle lowercase connector MPN");
            assertEquals("H", handler.extractSeries("h1012nl"));
        }

        @Test
        @DisplayName("Should not match non-Pulse parts")
        void shouldNotMatchNonPulseParts() {
            assertFalse(handler.matches("GRM155R61A104KA01D", ComponentType.INDUCTOR, registry),
                    "Should not match Murata capacitor");
            assertFalse(handler.matches("XAL4020-222ME", ComponentType.INDUCTOR, registry),
                    "Should not match Coilcraft inductor");
            assertFalse(handler.matches("43045-0802", ComponentType.CONNECTOR, registry),
                    "Should not match Molex connector");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.INDUCTOR),
                    "Should support INDUCTOR");
            assertTrue(types.contains(ComponentType.TRANSFORMER_BOURNS),
                    "Should support TRANSFORMER_BOURNS");
            assertTrue(types.contains(ComponentType.CONNECTOR),
                    "Should support CONNECTOR");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");

            assertEquals(4, types.size(), "Should have exactly 4 supported types");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of() not HashSet")
        void shouldUseSetOfNotHashSet() {
            var types = handler.getSupportedTypes();

            // Verify Set.of() is used by checking immutability
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.CAPACITOR);
            }, "getSupportedTypes() should return immutable set");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            PulseElectronicsHandler directHandler = new PulseElectronicsHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("H1012NL", ComponentType.TRANSFORMER_BOURNS, directRegistry),
                    "Direct handler should match H series transformer");
            assertTrue(directHandler.matches("P0751SNL", ComponentType.INDUCTOR, directRegistry),
                    "Direct handler should match P series inductor");
            assertTrue(directHandler.matches("JD0-0001NL", ComponentType.CONNECTOR, directRegistry),
                    "Direct handler should match J series connector");
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
    @DisplayName("Product Type Classification")
    class ProductTypeClassificationTests {

        @ParameterizedTest
        @DisplayName("Should correctly classify product types")
        @CsvSource({
                "H1012NL, true, false, false",
                "T1029NL, true, false, false",
                "P0751SNL, false, true, false",
                "PA-1000NL, true, true, false",
                "PE-53680NL, true, true, false",
                "JD0-0001NL, false, false, true",
                "JK-0001NL, false, false, true",
                "JXD-0001NL, false, false, true"
        })
        void shouldClassifyProductTypes(String mpn, boolean isTransformer,
                                         boolean isInductor, boolean isLanMagnetics) {
            assertEquals(isTransformer, handler.isTransformer(mpn),
                    mpn + " transformer classification");
            assertEquals(isInductor, handler.isInductor(mpn),
                    mpn + " inductor classification");
            assertEquals(isLanMagnetics, handler.isLanMagnetics(mpn),
                    mpn + " LAN magnetics classification");
        }
    }

    @Nested
    @DisplayName("MPN Structure Documentation")
    class MPNStructureTests {

        @Test
        @DisplayName("Document H series MPN structure")
        void documentHSeriesMPNStructure() {
            // MPN: H1012NL
            // Structure:
            // - H = Series (LAN transformer)
            // - 1012 = Part number
            // - NL = Lead-free / RoHS compliant
            String mpn = "H1012NL";
            assertEquals("H", handler.extractSeries(mpn));
            assertEquals("SMD Lead-Free", handler.extractPackageCode(mpn));
            assertTrue(handler.isLeadFree(mpn));
            assertFalse(handler.isTapeAndReel(mpn));
        }

        @Test
        @DisplayName("Document JD series MPN structure")
        void documentJDSeriesMPNStructure() {
            // MPN: JD0-0001NL
            // Structure:
            // - JD0 = Series (J series, variant 0)
            // - 0001 = Part number
            // - NL = Lead-free / RoHS compliant
            String mpn = "JD0-0001NL";
            assertEquals("J", handler.extractSeries(mpn));
            assertEquals("SMD Lead-Free", handler.extractPackageCode(mpn));
            assertTrue(handler.isLanMagnetics(mpn));
            assertTrue(handler.isLeadFree(mpn));
        }

        @Test
        @DisplayName("Document PE series MPN structure")
        void documentPESeriesMPNStructure() {
            // MPN: PE-53680NL
            // Structure:
            // - PE = Series (Power Electronics)
            // - 53680 = Part number
            // - NL = Lead-free / RoHS compliant
            String mpn = "PE-53680NL";
            assertEquals("PE", handler.extractSeries(mpn));
            assertEquals("SMD Lead-Free", handler.extractPackageCode(mpn));
            assertEquals("Power Electronics", handler.getProductType(mpn));
            assertTrue(handler.isTransformer(mpn));
            assertTrue(handler.isInductor(mpn));
        }
    }
}
