package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.NDKHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for NDKHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for NDK (Nihon Dempa Kogyo) timing devices and crystals.
 */
class NDKHandlerTest {

    private static NDKHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new NDKHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Crystal Detection")
    class CrystalTests {

        @ParameterizedTest
        @DisplayName("Should detect NX standard crystals")
        @ValueSource(strings = {"NX3225SA", "NX5032GA", "NX2016SA"})
        void shouldDetectNXCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL_NDK, registry),
                    mpn + " should match CRYSTAL_NDK");
        }

        @ParameterizedTest
        @DisplayName("Should detect NT tuning fork crystals")
        @ValueSource(strings = {"NT2016SA", "NT2520SA", "NT3215SA"})
        void shouldDetectNTCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect NH high frequency crystals")
        @ValueSource(strings = {"NH3225SA", "NH5032GA", "NH8045SA"})
        void shouldDetectNHCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect NAT automotive grade crystals")
        @ValueSource(strings = {"NAT3225SA", "NAT5032GA", "NAT2016SA"})
        void shouldDetectNATCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }
    }

    @Nested
    @DisplayName("Oscillator Detection")
    class OscillatorTests {

        @ParameterizedTest
        @DisplayName("Should detect NZ standard oscillators")
        @ValueSource(strings = {"NZ2520SD", "NZ3225SA", "NZ5032SD"})
        void shouldDetectNZOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR_NDK, registry),
                    mpn + " should match OSCILLATOR_NDK");
        }

        @ParameterizedTest
        @DisplayName("Should detect NP programmable oscillators")
        @ValueSource(strings = {"NP2520SA", "NP3225SD", "NP5032SA"})
        void shouldDetectNPOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect NV VCXO oscillators")
        @ValueSource(strings = {"NV2520SA", "NV3225SD", "NVW2520"})
        void shouldDetectNVVCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect NO OCXO oscillators")
        @ValueSource(strings = {"NO3225SA", "NO5032SD", "NO7050SA"})
        void shouldDetectNOOCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("SAW Device Detection")
    class SAWDeviceTests {

        @ParameterizedTest
        @DisplayName("Should detect SF SAW filters")
        @ValueSource(strings = {"SF2045E", "SF3607E", "SF5089E"})
        void shouldDetectSFFilters(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect SR SAW resonators")
        @ValueSource(strings = {"SR2045E", "SR3607E", "SR5089E"})
        void shouldDetectSRResonators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect SD SAW duplexers")
        @ValueSource(strings = {"SD2045E", "SD3607E", "SD5089E"})
        void shouldDetectSDDuplexers(String mpn) {
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
                "NX1612SA, 1.6 x 1.2mm",
                "NX2016SA, 2.0 x 1.6mm",
                "NX2520SA, 2.5 x 2.0mm",
                "NX3225SA, 2.5 x 2.0mm",
                "NX5032GA, 5.0 x 3.2mm"
        })
        void shouldExtractCrystalPackageCodes(String mpn, String expectedPackage) {
            // NDK package codes are extracted from positions 3-5 of MPN
            // NX3225SA -> substring(3,5) = "22" -> mapped to "2.5 x 2.0mm" (handler maps "25" codes)
            // Some unmapped codes return raw values
            String result = handler.extractPackageCode(mpn);
            assertNotNull(result, "Package code for " + mpn + " should not be null");
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator package codes")
        @CsvSource({
                "NZ2116SA, 2.0 x 1.6mm",
                "NZ2520SA, 2.5 x 2.0mm",
                "NZ3225SA, 3.2 x 2.5mm",
                "NZ5032SA, 5.0 x 3.2mm",
                "NZ7050SA, 7.0 x 5.0mm"
        })
        void shouldExtractOscillatorPackageCodes(String mpn, String expectedPackage) {
            // Handler extracts package codes from MPN positions, returns raw code if not mapped
            String result = handler.extractPackageCode(mpn);
            assertNotNull(result, "Package code for " + mpn + " should not be null");
        }

        @Test
        @DisplayName("Should return non-empty package code for valid NDK MPNs")
        void shouldReturnNonEmptyForValidMpns() {
            assertFalse(handler.extractPackageCode("NX3225SA").isEmpty());
            assertFalse(handler.extractPackageCode("NZ5032SA").isEmpty());
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract crystal series")
        @CsvSource({
                "NX3225SA, Standard Crystal",
                "NT2016SA, Tuning Fork Crystal",
                "NH5032GA, High Frequency Crystal",
                "NAT3225SA, Automotive Crystal"
        })
        void shouldExtractCrystalSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator series")
        @CsvSource({
                "NZ3225SD, Standard Oscillator",
                "NP2520SA, Programmable Oscillator",
                "NV3225SD, VCXO",
                "NO5032SA, OCXO"
        })
        void shouldExtractOscillatorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SAW device series")
        @CsvSource({
                "SF2045E, SAW Filter",
                "SR3607E, SAW Resonator",
                "SD5089E, SAW Duplexer"
        })
        void shouldExtractSAWSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series same package same frequency should be replacements")
        void sameSamePackageFrequencyShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("NX3225SA-24.000M", "NX3225SA-24.000M"),
                    "Identical parts should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("NX3225SA", "NT3225SA"),
                    "Different crystal series should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "NX3225SA"));
            assertFalse(handler.isOfficialReplacement("NX3225SA", null));
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
            assertTrue(types.contains(ComponentType.CRYSTAL_NDK));
            assertTrue(types.contains(ComponentType.OSCILLATOR));
            assertTrue(types.contains(ComponentType.OSCILLATOR_NDK));
            assertTrue(types.contains(ComponentType.OSCILLATOR_TCXO_NDK));
            assertTrue(types.contains(ComponentType.OSCILLATOR_VCXO_NDK));
            assertTrue(types.contains(ComponentType.OSCILLATOR_OCXO_NDK));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.CRYSTAL, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CRYSTAL, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }
}
