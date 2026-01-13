package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.IQDHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for IQDHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for IQD (International Quartz Devices) frequency products.
 */
class IQDHandlerTest {

    private static IQDHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new IQDHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Crystal Detection")
    class CrystalTests {

        @ParameterizedTest
        @DisplayName("Should detect LFXTAL low frequency crystals")
        @ValueSource(strings = {"LFXTAL003054", "LFXTAL003096", "LFXTAL016000"})
        void shouldDetectLFXTALCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL_IQD, registry),
                    mpn + " should match CRYSTAL_IQD");
        }

        @ParameterizedTest
        @DisplayName("Should detect CFPX standard crystals")
        @ValueSource(strings = {"CFPX104", "CFPX217", "CFPX236"})
        void shouldDetectCFPXCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL_IQD, registry),
                    mpn + " should match CRYSTAL_IQD");
        }

        @ParameterizedTest
        @DisplayName("Should detect XTALS SMD crystals")
        @ValueSource(strings = {"XTALS016", "XTALS020", "XTALS032"})
        void shouldDetectXTALSSMDCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL_IQD, registry),
                    mpn + " should match CRYSTAL_IQD");
        }

        @ParameterizedTest
        @DisplayName("Should detect XTAL through-hole crystals")
        @ValueSource(strings = {"XTAL032", "XTAL049", "XTAL100"})
        void shouldDetectXTALThroughHoleCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL_IQD, registry),
                    mpn + " should match CRYSTAL_IQD");
        }
    }

    @Nested
    @DisplayName("Standard Oscillator Detection")
    class OscillatorTests {

        @ParameterizedTest
        @DisplayName("Should detect IQXO standard oscillators")
        @ValueSource(strings = {"IQXO-100", "IQXO-350", "IQXO-500"})
        void shouldDetectIQXOOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR_IQD, registry),
                    mpn + " should match OSCILLATOR_IQD");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQXO-H high frequency oscillators")
        @ValueSource(strings = {"IQXO-H100", "IQXO-H350", "IQXO-H500"})
        void shouldDetectIQXOHighFrequencyOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQXO-L low power oscillators")
        @ValueSource(strings = {"IQXO-L100", "IQXO-L350", "IQXO-L500"})
        void shouldDetectIQXOLowPowerOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("TCXO Detection")
    class TCXOTests {

        @ParameterizedTest
        @DisplayName("Should detect IQTX TCXO oscillators")
        @ValueSource(strings = {"IQTX-100", "IQTX-350", "IQTX-500"})
        void shouldDetectIQTXOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR_TCXO_IQD, registry),
                    mpn + " should match OSCILLATOR_TCXO_IQD");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQTX-H high stability TCXO")
        @ValueSource(strings = {"IQTX-H100", "IQTX-H350", "IQTX-H500"})
        void shouldDetectHighStabilityTCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQTX-L low power TCXO")
        @ValueSource(strings = {"IQTX-L100", "IQTX-L350", "IQTX-L500"})
        void shouldDetectLowPowerTCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("VCXO Detection")
    class VCXOTests {

        @ParameterizedTest
        @DisplayName("Should detect IQVCXO oscillators")
        @ValueSource(strings = {"IQVCXO-100", "IQVCXO-350", "IQVCXO-500"})
        void shouldDetectIQVCXOOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR_VCXO_IQD, registry),
                    mpn + " should match OSCILLATOR_VCXO_IQD");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQVCXO-H high stability VCXO")
        @ValueSource(strings = {"IQVCXO-H100", "IQVCXO-H350", "IQVCXO-H500"})
        void shouldDetectHighStabilityVCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("OCXO Detection")
    class OCXOTests {

        @ParameterizedTest
        @DisplayName("Should detect IQOCXO oscillators")
        @ValueSource(strings = {"IQOCXO-100", "IQOCXO-350", "IQOCXO-500"})
        void shouldDetectIQOCXOOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR_OCXO_IQD, registry),
                    mpn + " should match OSCILLATOR_OCXO_IQD");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQOCXO-H high stability OCXO")
        @ValueSource(strings = {"IQOCXO-H100", "IQOCXO-H350", "IQOCXO-H500"})
        void shouldDetectHighStabilityOCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("Clock and RTC Modules Detection")
    class ModuleTests {

        @ParameterizedTest
        @DisplayName("Should detect IQCM clock modules")
        @ValueSource(strings = {"IQCM-100", "IQCM-200", "IQCM-300"})
        void shouldDetectClockModules(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQRTC RTC modules")
        @ValueSource(strings = {"IQRTC-100", "IQRTC-200", "IQRTC-300"})
        void shouldDetectRTCModules(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Filter Detection")
    class FilterTests {

        @ParameterizedTest
        @DisplayName("Should detect IQXF crystal filters")
        @ValueSource(strings = {"IQXF-100", "IQXF-200", "IQXF-300"})
        void shouldDetectCrystalFilters(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQSPF SAW filters")
        @ValueSource(strings = {"IQSPF-100", "IQSPF-200", "IQSPF-300"})
        void shouldDetectSAWFilters(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect IQRB resonator bandpass")
        @ValueSource(strings = {"IQRB-100", "IQRB-200", "IQRB-300"})
        void shouldDetectResonatorBandpass(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract crystal package codes")
        @CsvSource({
                "CFPX-016, 1.6 x 1.2mm",
                "CFPX-020, 2.0 x 1.6mm",
                "CFPX-025, 2.5 x 2.0mm",
                "CFPX-032, 3.2 x 2.5mm",
                "CFPX-050, 5.0 x 3.2mm",
                "CFPX-070, 7.0 x 5.0mm"
        })
        void shouldExtractCrystalPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator package codes")
        @CsvSource({
                "IQXO-21, 2.0 x 1.6mm",
                "IQXO-25, 2.5 x 2.0mm",
                "IQXO-32, 3.2 x 2.5mm",
                "IQXO-50, 5.0 x 3.2mm",
                "IQXO-70, 7.0 x 5.0mm",
                "IQXO-90, 9.0 x 7.0mm"
        })
        void shouldExtractOscillatorPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TCXO package codes")
        @CsvSource({
                "IQTX-32, 3.2 x 2.5mm",
                "IQTX-50, 5.0 x 3.2mm"
        })
        void shouldExtractTCXOPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract crystal series")
        @CsvSource({
                "LFXTAL003054, Low Frequency Crystal",
                "CFPX-104, Standard Crystal",
                "XTALS016, SMD Crystal",
                "XTAL032, Through-hole Crystal"
        })
        void shouldExtractCrystalSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator series")
        @CsvSource({
                "IQXO-100, Standard Oscillator",
                "IQXO-H100, High Frequency Oscillator",
                "IQXO-L100, Low Power Oscillator"
        })
        void shouldExtractOscillatorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TCXO series")
        @CsvSource({
                "IQTX-100, Standard TCXO",
                "IQTX-H100, High Stability TCXO",
                "IQTX-L100, Low Power TCXO"
        })
        void shouldExtractTCXOSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract VCXO series")
        @CsvSource({
                "IQVCXO-100, Standard VCXO",
                "IQVCXO-H100, High Stability VCXO"
        })
        void shouldExtractVCXOSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract OCXO series")
        @CsvSource({
                "IQOCXO-100, Standard OCXO",
                "IQOCXO-H100, High Stability OCXO"
        })
        void shouldExtractOCXOSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract module and filter series")
        @CsvSource({
                "IQCM-100, Clock Module",
                "IQRTC-100, RTC Module",
                "IQXF-100, Crystal Filter",
                "IQSPF-100, SAW Filter",
                "IQRB-100, Resonator Bandpass"
        })
        void shouldExtractModuleAndFilterSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series same package should be replacements")
        void sameSeriesSamePackage() {
            // Same oscillator series with same package - should be replacements
            assertTrue(handler.isOfficialReplacement("IQXO-32-10MHz", "IQXO-32-10MHz"),
                    "Identical parts should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("IQXO-100", "IQTX-100"),
                    "IQXO and IQTX should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("CFPX-104", "XTALS016"),
                    "Different crystal series should NOT be replacements");
        }

        @Test
        @DisplayName("Different packages should NOT be replacements")
        void differentPackagesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("IQXO-32", "IQXO-50"),
                    "Different packages should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.CRYSTAL));
            assertTrue(types.contains(ComponentType.CRYSTAL_IQD));
            assertTrue(types.contains(ComponentType.OSCILLATOR));
            assertTrue(types.contains(ComponentType.OSCILLATOR_IQD));
            assertTrue(types.contains(ComponentType.OSCILLATOR_TCXO_IQD));
            assertTrue(types.contains(ComponentType.OSCILLATOR_VCXO_IQD));
            assertTrue(types.contains(ComponentType.OSCILLATOR_OCXO_IQD));
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
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.CRYSTAL, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "IQXO-100"));
            assertFalse(handler.isOfficialReplacement("IQXO-100", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CRYSTAL, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("IQXO-100", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("iqxo-100", ComponentType.OSCILLATOR, registry));
            assertTrue(handler.matches("IQXO-100", ComponentType.OSCILLATOR, registry));
            assertTrue(handler.matches("IqXo-100", ComponentType.OSCILLATOR, registry));
        }
    }
}
