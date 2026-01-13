package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AbraconHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AbraconHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Abracon timing devices, crystals, and RF components.
 */
class AbraconHandlerTest {

    private static AbraconHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AbraconHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Crystal Detection")
    class CrystalTests {

        @ParameterizedTest
        @DisplayName("Should detect ABM standard crystals")
        @ValueSource(strings = {"ABM3-12.000MHZ", "ABM03-32.768KHZ", "ABM7-16.000MHZ"})
        void shouldDetectABMCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL_ABRACON, registry),
                    mpn + " should match CRYSTAL_ABRACON");
        }

        @ParameterizedTest
        @DisplayName("Should detect ABL low profile crystals")
        @ValueSource(strings = {"ABL2-12.000MHZ", "ABL03-24.000MHZ", "ABL7-8.000MHZ"})
        void shouldDetectABLCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect ABT tuning fork crystals")
        @ValueSource(strings = {"ABT2-32.768KHZ", "ABT03-32.768KHZ", "ABT7-32.768KHZ"})
        void shouldDetectABTCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect ABS automotive crystals")
        @ValueSource(strings = {"ABS2-16.000MHZ", "ABS06-40.000MHZ", "ABS25-12.000MHZ"})
        void shouldDetectABSCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }
    }

    @Nested
    @DisplayName("Oscillator Detection")
    class OscillatorTests {

        @ParameterizedTest
        @DisplayName("Should detect ASCO standard oscillators")
        @ValueSource(strings = {"ASCO1-12.000MHZ", "ASCO2-24.000MHZ", "ASCO7-48.000MHZ"})
        void shouldDetectASCOOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR_ABRACON, registry),
                    mpn + " should match OSCILLATOR_ABRACON");
        }

        @ParameterizedTest
        @DisplayName("Should detect ASFL low power oscillators")
        @ValueSource(strings = {"ASFL1-12.000MHZ", "ASFL2-24.000MHZ", "ASFL3-32.768KHZ"})
        void shouldDetectASFLOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect ASE EMI reduced oscillators")
        @ValueSource(strings = {"ASE1-12.000MHZ", "ASE2-24.000MHZ", "ASE3-48.000MHZ"})
        void shouldDetectASEOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect ASTX TCXO oscillators")
        @ValueSource(strings = {"ASTX1-12.000MHZ", "ASTX2-26.000MHZ", "ASTX3-19.200MHZ"})
        void shouldDetectASTXTCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR_TCXO_ABRACON, registry),
                    mpn + " should match OSCILLATOR_TCXO_ABRACON");
        }

        @ParameterizedTest
        @DisplayName("Should detect ASV VCXO oscillators")
        @ValueSource(strings = {"ASV1-12.000MHZ", "ASV2-24.000MHZ", "ASV3-19.200MHZ"})
        void shouldDetectASVVCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("Inductor Detection")
    class InductorTests {

        @ParameterizedTest
        @DisplayName("Should detect AIAL air core inductors")
        @ValueSource(strings = {"AIAL0510-1R0K", "AIAL1210-2R2M", "AIAL1812-4R7K"})
        void shouldDetectAIALInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect AIML multilayer inductors")
        @ValueSource(strings = {"AIML0402-1N0S", "AIML0603-2N2S", "AIML0805-4N7S"})
        void shouldDetectAIMLInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect AIRP power inductors")
        @ValueSource(strings = {"AIRP1212-100M", "AIRP1515-4R7M", "AIRP2020-10R0M"})
        void shouldDetectAIRPInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract crystal package codes")
        @CsvSource({
                "ABM02-12.000MHZ, 2.0 x 1.6mm",
                "ABM03-16.000MHZ, 3.2 x 2.5mm",
                "ABM07-24.000MHZ, 7.0 x 5.0mm",
                "ABM08-12.000MHZ, 8.0 x 4.5mm"
        })
        void shouldExtractCrystalPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract crystal series")
        @CsvSource({
                "ABM3-12.000MHZ, Standard Crystal",
                "ABL2-24.000MHZ, Low Profile Crystal",
                "ABT3-32.768KHZ, Tuning Fork Crystal",
                "ABS07-16.000MHZ, Automotive Crystal"
        })
        void shouldExtractCrystalSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator series")
        @CsvSource({
                "ASCO1-12.000MHZ, Standard Oscillator",
                "ASFL2-24.000MHZ, Low Power Oscillator",
                "ASE1-16.000MHZ, EMI Reduced Oscillator",
                "ASTX2-26.000MHZ, TCXO",
                "ASVTX1-19.200MHZ, VCTCXO",
                "ASV3-24.000MHZ, VCXO"
        })
        void shouldExtractOscillatorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract RF series")
        @CsvSource({
                "ABA1-433MHZ, RF Antenna",
                "ABF1-915MHZ, RF Filter",
                "ABB1-2400MHZ, RF Balun",
                "ABUN1-GPS, RF Diplexer"
        })
        void shouldExtractRFSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inductor series")
        @CsvSource({
                "AIAL0510-1R0K, Air Core Inductor",
                "AIML0402-1N0S, Multilayer Inductor",
                "AIRP1212-100M, Power Inductor"
        })
        void shouldExtractInductorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same crystal series same package same frequency should be replacements")
        void sameCrystalSeriesPackageFrequency() {
            // Note: isOfficialReplacement requires matching frequency and stability
            // Identical parts are always replacements
            assertTrue(handler.isOfficialReplacement("ABM3-12.000MHZ", "ABM3-12.000MHZ"),
                    "Identical parts should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ABM3-12.000MHZ", "ABL3-12.000MHZ"),
                    "Different crystal series should NOT be replacements");
        }

        @Test
        @DisplayName("Different frequencies should NOT be replacements")
        void differentFrequenciesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ABM3-12.000MHZ", "ABM3-16.000MHZ"),
                    "Different frequencies should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "ABM3-12.000MHZ"));
            assertFalse(handler.isOfficialReplacement("ABM3-12.000MHZ", null));
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
            assertTrue(types.contains(ComponentType.CRYSTAL_ABRACON));
            assertTrue(types.contains(ComponentType.OSCILLATOR));
            assertTrue(types.contains(ComponentType.OSCILLATOR_ABRACON));
            assertTrue(types.contains(ComponentType.OSCILLATOR_TCXO_ABRACON));
            assertTrue(types.contains(ComponentType.OSCILLATOR_VCXO_ABRACON));
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
