package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentManufacturer;
import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AlphaOmegaHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for AlphaOmegaHandler.
 *
 * Alpha and Omega Semiconductor (AOS) specializes in power MOSFETs.
 * MPN Structure: AO[Package Code][Series Number][Variant]
 *
 * Package Prefixes:
 * - AO3xxx = SOT-23 packages
 * - AO4xxx = SO-8 packages
 * - AOD = TO-252 (DPAK)
 * - AON = DFN packages
 * - AOI = TO-251 (IPAK)
 * - AOT = TO-220/TO-247
 * - AOB = D2PAK (TO-263)
 * - AOC = Common drain dual MOSFETs
 * - AOP = PDFN packages
 * - AOTL = TOLL packages
 * - AOGT = GTPAK (topside cooling)
 * - AOGL = GLPAK (gull-wing)
 */
class AlphaOmegaHandlerTest {

    private static AlphaOmegaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AlphaOmegaHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // SOT-23 Package Tests (AO3xxx series)
    // ========================================================================

    @Nested
    @DisplayName("SOT-23 Package MOSFETs (AO3xxx)")
    class SOT23Tests {

        @ParameterizedTest
        @DisplayName("Should detect P-channel SOT-23 MOSFETs")
        @ValueSource(strings = {
            "AO3401",
            "AO3401A",
            "AO3415",
            "AO3415A",
            "AO3435"
        })
        void shouldDetectPChannelSOT23(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect N-channel SOT-23 MOSFETs")
        @ValueSource(strings = {
            "AO3400",
            "AO3400A",
            "AO3402",
            "AO3416",
            "AO3420",
            "AO3422"
        })
        void shouldDetectNChannelSOT23(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract SOT-23 package code")
        void shouldExtractSOT23Package() {
            assertEquals("SOT-23", handler.extractPackageCode("AO3401A"));
            assertEquals("SOT-23", handler.extractPackageCode("AO3400"));
        }

        @Test
        @DisplayName("Should extract AO3xxx series")
        void shouldExtractAO3Series() {
            assertEquals("AO3401", handler.extractSeries("AO3401A"));
            assertEquals("AO3400", handler.extractSeries("AO3400A"));
        }
    }

    // ========================================================================
    // SO-8 Package Tests (AO4xxx series)
    // ========================================================================

    @Nested
    @DisplayName("SO-8 Package MOSFETs (AO4xxx)")
    class SO8Tests {

        @ParameterizedTest
        @DisplayName("Should detect SO-8 package MOSFETs")
        @ValueSource(strings = {
            "AO4407",
            "AO4407A",
            "AO4421",
            "AO4433",
            "AO4435",
            "AO4468",
            "AO4988"
        })
        void shouldDetectSO8MOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract SO-8 package code")
        void shouldExtractSO8Package() {
            assertEquals("SO-8", handler.extractPackageCode("AO4407A"));
            assertEquals("SO-8", handler.extractPackageCode("AO4988"));
        }

        @Test
        @DisplayName("Should extract AO4xxx series")
        void shouldExtractAO4Series() {
            assertEquals("AO4407", handler.extractSeries("AO4407A"));
            assertEquals("AO4988", handler.extractSeries("AO4988"));
        }
    }

    // ========================================================================
    // TO-252/DPAK Package Tests (AOD series)
    // ========================================================================

    @Nested
    @DisplayName("TO-252/DPAK Package MOSFETs (AOD)")
    class AODTests {

        @ParameterizedTest
        @DisplayName("Should detect AOD series MOSFETs")
        @ValueSource(strings = {
            "AOD4184",
            "AOD4184A",
            "AOD409",
            "AOD413A",
            "AOD472",
            "AOD482",
            "AOD4126"
        })
        void shouldDetectAODMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract TO-252 package code for AOD")
        void shouldExtractTO252Package() {
            assertEquals("TO-252", handler.extractPackageCode("AOD4184"));
            assertEquals("TO-252", handler.extractPackageCode("AOD409"));
        }

        @Test
        @DisplayName("Should extract AOD series")
        void shouldExtractAODSeries() {
            assertEquals("AOD4184", handler.extractSeries("AOD4184A"));
            assertEquals("AOD409", handler.extractSeries("AOD409"));
        }
    }

    // ========================================================================
    // DFN Package Tests (AON series)
    // ========================================================================

    @Nested
    @DisplayName("DFN Package MOSFETs (AON)")
    class AONTests {

        @ParameterizedTest
        @DisplayName("Should detect AON series MOSFETs")
        @ValueSource(strings = {
            "AON6758",
            "AON6144",
            "AON2403",
            "AON2405",
            "AON7200"
        })
        void shouldDetectAONMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract DFN package code for AON")
        void shouldExtractDFNPackage() {
            assertEquals("DFN", handler.extractPackageCode("AON6758"));
            assertEquals("DFN", handler.extractPackageCode("AON6144"));
        }

        @Test
        @DisplayName("Should extract AON series")
        void shouldExtractAONSeries() {
            assertEquals("AON6758", handler.extractSeries("AON6758"));
            assertEquals("AON6144", handler.extractSeries("AON6144"));
        }
    }

    // ========================================================================
    // TO-251/IPAK Package Tests (AOI series)
    // ========================================================================

    @Nested
    @DisplayName("TO-251/IPAK Package MOSFETs (AOI)")
    class AOITests {

        @ParameterizedTest
        @DisplayName("Should detect AOI series MOSFETs")
        @ValueSource(strings = {
            "AOI403",
            "AOI409",
            "AOI4185",
            "AOI423"
        })
        void shouldDetectAOIMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract TO-251 package code for AOI")
        void shouldExtractTO251Package() {
            assertEquals("TO-251", handler.extractPackageCode("AOI403"));
            assertEquals("TO-251", handler.extractPackageCode("AOI4185"));
        }
    }

    // ========================================================================
    // TO-220 Package Tests (AOT series)
    // ========================================================================

    @Nested
    @DisplayName("TO-220 Package MOSFETs (AOT)")
    class AOTTests {

        @ParameterizedTest
        @DisplayName("Should detect AOT series MOSFETs")
        @ValueSource(strings = {
            "AOT240L",
            "AOT240",
            "AOT10N60",
            "AOT460",
            "AOT260L"
        })
        void shouldDetectAOTMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract TO-220 package code for AOT")
        void shouldExtractTO220Package() {
            assertEquals("TO-220", handler.extractPackageCode("AOT240L"));
            assertEquals("TO-220", handler.extractPackageCode("AOT10N60"));
        }
    }

    // ========================================================================
    // TO-263/D2PAK Package Tests (AOB series)
    // ========================================================================

    @Nested
    @DisplayName("TO-263/D2PAK Package MOSFETs (AOB)")
    class AOBTests {

        @ParameterizedTest
        @DisplayName("Should detect AOB series MOSFETs")
        @ValueSource(strings = {
            "AOB409L",
            "AOB411L",
            "AOB10N60",
            "AOB240L"
        })
        void shouldDetectAOBMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract TO-263 package code for AOB")
        void shouldExtractTO263Package() {
            assertEquals("TO-263", handler.extractPackageCode("AOB409L"));
            assertEquals("TO-263", handler.extractPackageCode("AOB10N60"));
        }
    }

    // ========================================================================
    // Advanced Package Tests (AOTL, AOGT, AOGL, AONS, AONR, AONK)
    // ========================================================================

    @Nested
    @DisplayName("Advanced Package MOSFETs")
    class AdvancedPackageTests {

        @ParameterizedTest
        @DisplayName("Should detect TOLL package MOSFETs (AOTL)")
        @ValueSource(strings = {
            "AOTL66912",
            "AOTL66908"
        })
        void shouldDetectAOTLMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
            assertEquals("TOLL", handler.extractPackageCode(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should detect GTPAK MOSFETs (AOGT)")
        @ValueSource(strings = {
            "AOGT66909"
        })
        void shouldDetectAOGTMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
            assertEquals("GTPAK", handler.extractPackageCode(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should detect GLPAK MOSFETs (AOGL)")
        @ValueSource(strings = {
            "AOGL66901"
        })
        void shouldDetectAOGLMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
            assertEquals("GLPAK", handler.extractPackageCode(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should detect DFN source-down MOSFETs (AONS/AONR)")
        @ValueSource(strings = {
            "AONS66811",
            "AONR66820"
        })
        void shouldDetectSourceDownDFNMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
            assertEquals("DFN", handler.extractPackageCode(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should detect DFN3.3x3.3 MOSFETs (AONK)")
        @ValueSource(strings = {
            "AONK66914"
        })
        void shouldDetectAONKMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
            assertEquals("DFN3.3x3.3", handler.extractPackageCode(mpn));
        }
    }

    // ========================================================================
    // Common Drain Dual MOSFETs (AOC series)
    // ========================================================================

    @Nested
    @DisplayName("Common Drain Dual MOSFETs (AOC)")
    class AOCTests {

        @ParameterizedTest
        @DisplayName("Should detect AOC series MOSFETs")
        @ValueSource(strings = {
            "AOC2421",
            "AOC2409"
        })
        void shouldDetectAOCMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract SO-8 package code for AOC")
        void shouldExtractAOCPackage() {
            assertEquals("SO-8", handler.extractPackageCode("AOC2421"));
        }
    }

    // ========================================================================
    // PDFN Package Tests (AOP series)
    // ========================================================================

    @Nested
    @DisplayName("PDFN Package MOSFETs (AOP)")
    class AOPTests {

        @ParameterizedTest
        @DisplayName("Should detect AOP series MOSFETs")
        @ValueSource(strings = {
            "AOP604",
            "AOP602"
        })
        void shouldDetectAOPMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract PDFN package code for AOP")
        void shouldExtractPDFNPackage() {
            assertEquals("PDFN", handler.extractPackageCode("AOP604"));
        }
    }

    // ========================================================================
    // Package Code Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract correct package codes for all prefixes")
        @CsvSource({
            "AO3401A, SOT-23",
            "AO4407A, SO-8",
            "AOD4184, TO-252",
            "AON6758, DFN",
            "AOI403, TO-251",
            "AOT240L, TO-220",
            "AOB409L, TO-263",
            "AOC2421, SO-8",
            "AOP604, PDFN",
            "AOTL66912, TOLL",
            "AOGT66909, GTPAK",
            "AOGL66901, GLPAK",
            "AONS66811, DFN",
            "AONR66820, DFN",
            "AONK66914, DFN3.3x3.3"
        })
        void shouldExtractCorrectPackageCodes(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    // ========================================================================
    // Series Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract correct series for all prefixes")
        @CsvSource({
            "AO3401A, AO3401",
            "AO4407A, AO4407",
            "AOD4184A, AOD4184",
            "AON6758, AON6758",
            "AOI403, AOI403",
            "AOT240L, AOT240",
            "AOB409L, AOB409",
            "AOC2421, AOC2421",
            "AOP604, AOP604",
            "AOTL66912, AOTL66912",
            "AOGT66909, AOGT66909",
            "AOGL66901, AOGL66901",
            "AONS66811, AONS66811",
            "AONR66820, AONR66820",
            "AONK66914, AONK66914"
        })
        void shouldExtractCorrectSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
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
            assertFalse(handler.matches(null, ComponentType.MOSFET, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.MOSFET, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("AO3401A", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("ao3401a", ComponentType.MOSFET, registry));
            assertTrue(handler.matches("AOD4184", ComponentType.MOSFET, registry));
            assertTrue(handler.matches("aon6758", ComponentType.MOSFET, registry));
        }

        @Test
        @DisplayName("Should not match non-AOS MPNs")
        void shouldNotMatchNonAOSMPNs() {
            assertFalse(handler.matches("IRF540", ComponentType.MOSFET, registry));
            assertFalse(handler.matches("BSC0902NS", ComponentType.MOSFET, registry));
            assertFalse(handler.matches("STF20NF06", ComponentType.MOSFET, registry));
        }
    }

    // ========================================================================
    // getSupportedTypes() Tests
    // ========================================================================

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should include MOSFET type")
        void shouldIncludeMOSFETType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MOSFET), "Should support MOSFET");
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
    // Official Replacement Tests
    // ========================================================================

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and package should be official replacement")
        void sameSeriesToAreReplacement() {
            assertTrue(handler.isOfficialReplacement("AO3401", "AO3401A"),
                    "Same series AO3401 with variant should be replacements");
        }

        @Test
        @DisplayName("Different series should not be official replacement")
        void differentSeriesNotReplacement() {
            assertFalse(handler.isOfficialReplacement("AO3401A", "AO3400A"),
                    "AO3401 and AO3400 are different series");
        }

        @Test
        @DisplayName("Different packages should not be official replacement")
        void differentPackagesNotReplacement() {
            assertFalse(handler.isOfficialReplacement("AO3401A", "AO4401A"),
                    "AO3 (SOT-23) and AO4 (SO-8) are different packages");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsNotReplacement() {
            assertFalse(handler.isOfficialReplacement(null, "AO3401A"));
            assertFalse(handler.isOfficialReplacement("AO3401A", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    // ========================================================================
    // Manufacturer Detection Tests
    // ========================================================================

    @Nested
    @DisplayName("Manufacturer Detection via ComponentManufacturer")
    class ManufacturerDetectionTests {

        @ParameterizedTest
        @DisplayName("Should detect AOS as manufacturer from MPN")
        @ValueSource(strings = {
            "AO3401A",
            "AO4407A",
            "AOD4184",
            "AON6758",
            "AOTL66912"
        })
        void shouldDetectAOSManufacturer(String mpn) {
            ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
            assertEquals(ComponentManufacturer.ALPHA_OMEGA, manufacturer,
                    mpn + " should be detected as Alpha and Omega Semiconductor");
        }
    }
}
