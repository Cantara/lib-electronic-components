package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SiTimeHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SiTimeHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for SiTime MEMS oscillators and timing devices.
 */
class SiTimeHandlerTest {

    private static SiTimeHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SiTimeHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SiT15xx LVCMOS Oscillator Detection")
    class SiT15xxTests {

        @ParameterizedTest
        @DisplayName("Should detect SiT1533 oscillators")
        @ValueSource(strings = {
                "SiT1533AI-H4-DCC-32.768",
                "SIT1533AI-H4-DCC-32.768",
                "SiT1533BI-H4-18E-32.768X"
        })
        void shouldDetectSiT1533(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SiT1534 oscillators")
        @ValueSource(strings = {
                "SiT1534AI-H4-DCC-32.768",
                "SIT1534BI-H4-18E-32.768"
        })
        void shouldDetectSiT1534(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SiT1552 oscillators")
        @ValueSource(strings = {
                "SiT1552AI-J4-DCC-32.768",
                "SIT1552BI-H4-18E-32.768"
        })
        void shouldDetectSiT1552(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @Test
        @DisplayName("32.768kHz parts should also match CRYSTAL type")
        void shouldMatch32kHzAsCrystal() {
            assertTrue(handler.matches("SiT1533AI-H4-DCC-32.768", ComponentType.CRYSTAL, registry),
                    "32.768kHz oscillator should match CRYSTAL for crystal replacement use");
        }
    }

    @Nested
    @DisplayName("SiT16xx Low Power Oscillator Detection")
    class SiT16xxTests {

        @ParameterizedTest
        @DisplayName("Should detect SiT1602 oscillators")
        @ValueSource(strings = {
                "SiT1602AI-H4-DCC-32.768",
                "SIT1602BI-H4-18E-32.768"
        })
        void shouldDetectSiT1602(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SiT1618 oscillators")
        @ValueSource(strings = {
                "SiT1618AI-H4-DCC-32.768",
                "SIT1618BI-H4-18E-32.768"
        })
        void shouldDetectSiT1618(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("SiT80xx Precision MHz Oscillator Detection")
    class SiT80xxTests {

        @ParameterizedTest
        @DisplayName("Should detect SiT8008 precision oscillators")
        @ValueSource(strings = {
                "SiT8008BI-71-18E-24.000000X",
                "SIT8008BI-71-18E-24.000000",
                "SiT8008AI-71-25E-25.000000X"
        })
        void shouldDetectSiT8008(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SiT8021 precision oscillators")
        @ValueSource(strings = {
                "SiT8021BI-71-25E-48.000000",
                "SIT8021AI-71-32E-50.000000"
        })
        void shouldDetectSiT8021(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @Test
        @DisplayName("Should also match as IC type")
        void shouldMatchAsIC() {
            assertTrue(handler.matches("SiT8008BI-71-18E-24.000000X", ComponentType.IC, registry),
                    "SiT8008 should match IC");
        }
    }

    @Nested
    @DisplayName("SiT81xx Differential Oscillator Detection")
    class SiT81xxTests {

        @ParameterizedTest
        @DisplayName("Should detect SiT8148 differential oscillators")
        @ValueSource(strings = {
                "SiT8148AI-G-33N-156.250000",
                "SIT8148BI-G-33N-100.000000"
        })
        void shouldDetectSiT8148(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SiT8152 differential oscillators")
        @ValueSource(strings = {
                "SiT8152AI-G-33N-156.250000",
                "SIT8152BI-G-33N-125.000000"
        })
        void shouldDetectSiT8152(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("SiT85xx/SiT5xxx Super-TCXO Detection")
    class SiT85xxTests {

        @ParameterizedTest
        @DisplayName("Should detect SiT5155 Super-TCXO")
        @ValueSource(strings = {
                "SiT5155AI-J3-33N-26.000000",
                "SIT5155BI-J3-33N-26.000000"
        })
        void shouldDetectSiT5155(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SiT5156 Super-TCXO")
        @ValueSource(strings = {
                "SiT5156AI-J3-33N-38.400000",
                "SIT5156BI-J3-33N-38.400000"
        })
        void shouldDetectSiT5156(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("SiT91xx AEC-Q100 Automotive Detection")
    class SiT91xxTests {

        @ParameterizedTest
        @DisplayName("Should detect SiT9120 automotive oscillators")
        @ValueSource(strings = {
                "SiT9120AI-1B1-33E-24.000000T",
                "SIT9120BI-1B1-33E-25.000000"
        })
        void shouldDetectSiT9120(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SiT9121 automotive oscillators")
        @ValueSource(strings = {
                "SiT9121AI-1B1-33E-26.000000",
                "SIT9121BI-1B1-33E-40.000000"
        })
        void shouldDetectSiT9121(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("Other SiTime Series Detection")
    class OtherSeriesTests {

        @Test
        @DisplayName("Should detect SiT1701 ultra low power")
        void shouldDetectSiT1701() {
            assertTrue(handler.matches("SiT1701AI-H4-DCC-32.768", ComponentType.OSCILLATOR, registry));
        }

        @Test
        @DisplayName("Should detect SiT8621 high performance")
        void shouldDetectSiT8621() {
            assertTrue(handler.matches("SiT8621AI-82-33N-100.000000", ComponentType.OSCILLATOR, registry));
        }

        @Test
        @DisplayName("Should detect SiT8814 network sync")
        void shouldDetectSiT8814() {
            assertTrue(handler.matches("SiT8814AI-83-33N-156.250000", ComponentType.OSCILLATOR, registry));
        }

        @Test
        @DisplayName("Should detect SiT9002 high temp")
        void shouldDetectSiT9002() {
            assertTrue(handler.matches("SiT9002AI-1B1-33E-25.000000", ComponentType.OSCILLATOR, registry));
        }

        @Test
        @DisplayName("Should detect SiT9367 clock generator")
        void shouldDetectSiT9367() {
            assertTrue(handler.matches("SiT9367AI-1D1-33E-156.250000", ComponentType.OSCILLATOR, registry));
        }

        @Test
        @DisplayName("Should detect SiT9501 high performance clock")
        void shouldDetectSiT9501() {
            assertTrue(handler.matches("SiT9501AI-2C1-33N-125.000000", ComponentType.OSCILLATOR, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes")
        @CsvSource({
                "SiT8008BI-71-18E-24.000000X, 1.6x1.2mm",
                "SiT8008AI-71-25E-25.000000X, 2.5x2.0mm",
                "SiT1533AI-H4-DCC-32.768, 1.5x0.8mm CSP",
                "SiT8021BI-71-32E-48.000000, 3.2x2.5mm",
                "SiT8008BI-71-50E-24.000000, 5.0x3.2mm",
                "SiT8008BI-71-70E-24.000000, 7.0x5.0mm"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown package codes")
        void shouldReturnEmptyForUnknown() {
            // MPN without recognizable package code
            String result = handler.extractPackageCode("SiT8008");
            assertTrue(result.isEmpty() || result.length() <= 3,
                    "Should return empty or short code for incomplete MPN");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from MPNs")
        @CsvSource({
                "SiT1533AI-H4-DCC-32.768, SiT15",
                "SiT1602BI-H4-18E-32.768, SiT16",
                "SiT1701AI-H4-DCC-32.768, SiT17",
                "SiT8008BI-71-18E-24.000000X, SiT80",
                "SiT8148AI-G-33N-156.250000, SiT81",
                "SiT5155AI-J3-33N-26.000000, SiT5",
                "SiT8621AI-82-33N-100.000000, SiT86",
                "SiT8814AI-83-33N-156.250000, SiT88",
                "SiT9002AI-1B1-33E-25.000000, SiT90",
                "SiT9120AI-1B1-33E-24.000000T, SiT91",
                "SiT9367AI-1D1-33E-156.250000, SiT93",
                "SiT9501AI-2C1-33N-125.000000, SiT95"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should return series description")
        @CsvSource({
                "SiT1533AI-H4-DCC-32.768, MHz LVCMOS Oscillator",
                "SiT1602BI-H4-18E-32.768, Low Power Oscillator",
                "SiT1701AI-H4-DCC-32.768, Ultra Low Power Oscillator",
                "SiT8008BI-71-18E-24.000000X, Precision MHz Oscillator",
                "SiT8148AI-G-33N-156.250000, Differential Oscillator",
                "SiT5155AI-J3-33N-26.000000, Super-TCXO",
                "SiT9120AI-1B1-33E-24.000000T, AEC-Q100 Automotive Oscillator"
        })
        void shouldReturnSeriesDescription(String mpn, String expectedDescription) {
            assertEquals(expectedDescription, handler.getSeriesDescription(mpn),
                    "Description for " + mpn);
        }
    }

    @Nested
    @DisplayName("Frequency Extraction")
    class FrequencyExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract frequency from MPNs")
        @CsvSource({
                "SiT8008BI-71-18E-24.000000X, 24.000000",
                "SiT1533AI-H4-DCC-32.768, 32.768",
                "SiT8148AI-G-33N-156.250000, 156.250000",
                "SiT8021BI-71-25E-48.000000, 48.000000",
                "SiT8621AI-82-33N-100.000000, 100.000000"
        })
        void shouldExtractFrequency(String mpn, String expectedFrequency) {
            assertEquals(expectedFrequency, handler.extractFrequency(mpn),
                    "Frequency for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementDetectionTests {

        @Test
        @DisplayName("Same series and frequency should be replacements")
        void sameSSeriesAndFrequencyAreReplacements() {
            // Same series (SiT80), same frequency (24MHz), different temp grade
            assertTrue(handler.isOfficialReplacement(
                    "SiT8008AI-71-18E-24.000000X",
                    "SiT8008BI-71-18E-24.000000X"),
                    "Same series and frequency should be replacements");

            // Same series (SiT80), same frequency, different package
            assertTrue(handler.isOfficialReplacement(
                    "SiT8008BI-71-18E-24.000000",
                    "SiT8008BI-71-25E-24.000000"),
                    "Same series and frequency with different package should be replacements");
        }

        @Test
        @DisplayName("Different frequencies should NOT be replacements")
        void differentFrequenciesNotReplacements() {
            assertFalse(handler.isOfficialReplacement(
                    "SiT8008BI-71-18E-24.000000X",
                    "SiT8008BI-71-18E-25.000000X"),
                    "Different frequencies should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            // SiT80xx (Precision) vs SiT81xx (Differential) - truly different series
            assertFalse(handler.isOfficialReplacement(
                    "SiT8008BI-71-18E-24.000000X",
                    "SiT8148BI-71-18E-24.000000X"),
                    "SiT80 vs SiT81 should NOT be replacements");

            // SiT15xx vs SiT16xx - different series
            assertFalse(handler.isOfficialReplacement(
                    "SiT1533AI-H4-DCC-32.768",
                    "SiT1602AI-H4-DCC-32.768"),
                    "SiT15 vs SiT16 should NOT be replacements");

            // SiT80xx vs SiT91xx (Automotive) - different series
            assertFalse(handler.isOfficialReplacement(
                    "SiT8008BI-71-18E-24.000000X",
                    "SiT9120BI-71-18E-24.000000X"),
                    "SiT80 vs SiT91 should NOT be replacements");
        }

        @Test
        @DisplayName("Same series different model should be replacements if same frequency")
        void sameSeriesDifferentModelAreReplacements() {
            // SiT8008 and SiT8021 are both SiT80xx series with same frequency
            assertTrue(handler.isOfficialReplacement(
                    "SiT8008BI-71-18E-24.000000X",
                    "SiT8021BI-71-18E-24.000000X"),
                    "Same series with same frequency should be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "SiT8008BI-71-18E-24.000000X"));
            assertFalse(handler.isOfficialReplacement("SiT8008BI-71-18E-24.000000X", null));
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
            assertTrue(types.contains(ComponentType.OSCILLATOR),
                    "Should support OSCILLATOR");
            assertTrue(types.contains(ComponentType.CRYSTAL),
                    "Should support CRYSTAL");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");
        }

        @Test
        @DisplayName("Should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () ->
                            types.add(ComponentType.RESISTOR),
                    "getSupportedTypes() should return immutable Set");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.OSCILLATOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractFrequency(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.OSCILLATOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractFrequency(""));
        }

        @Test
        @DisplayName("Should be case-insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("SIT8008BI-71-18E-24.000000X", ComponentType.OSCILLATOR, registry),
                    "Should match uppercase");
            assertTrue(handler.matches("sit8008bi-71-18e-24.000000x", ComponentType.OSCILLATOR, registry),
                    "Should match lowercase");
            assertTrue(handler.matches("SiT8008BI-71-18E-24.000000X", ComponentType.OSCILLATOR, registry),
                    "Should match mixed case");
        }

        @Test
        @DisplayName("Should not match non-SiTime MPNs")
        void shouldNotMatchNonSiTimeMpns() {
            assertFalse(handler.matches("FA-238SA", ComponentType.OSCILLATOR, registry),
                    "Should not match Epson crystal");
            assertFalse(handler.matches("SG-210STF", ComponentType.OSCILLATOR, registry),
                    "Should not match Epson oscillator");
            assertFalse(handler.matches("NX3215SA", ComponentType.OSCILLATOR, registry),
                    "Should not match NDK oscillator");
            assertFalse(handler.matches("TXC7M-24.000MHz", ComponentType.OSCILLATOR, registry),
                    "Should not match TXC oscillator");
        }

        @Test
        @DisplayName("Should handle partial MPNs")
        void shouldHandlePartialMpns() {
            // Just the model number without full part number
            assertTrue(handler.matches("SiT8008", ComponentType.OSCILLATOR, registry),
                    "Should match partial MPN");
            assertEquals("SiT80", handler.extractSeries("SiT8008"));
        }

        @Test
        @DisplayName("Should handle MPNs with trailing suffixes")
        void shouldHandleMpnsWithTrailingSuffixes() {
            assertTrue(handler.matches("SiT8008BI-71-18E-24.000000X", ComponentType.OSCILLATOR, registry));
            assertTrue(handler.matches("SiT9120AI-1B1-33E-24.000000T", ComponentType.OSCILLATOR, registry));
            assertEquals("24.000000", handler.extractFrequency("SiT8008BI-71-18E-24.000000X"));
        }
    }

    @Nested
    @DisplayName("Pattern Registration")
    class PatternRegistrationTests {

        @Test
        @DisplayName("Registry should have patterns for OSCILLATOR type")
        void registryShouldHaveOscillatorPatterns() {
            assertTrue(registry.hasPattern(ComponentType.OSCILLATOR),
                    "Registry should have OSCILLATOR patterns");
        }

        @Test
        @DisplayName("Registry should have patterns for IC type")
        void registryShouldHaveICPatterns() {
            assertTrue(registry.hasPattern(ComponentType.IC),
                    "Registry should have IC patterns");
        }

        @Test
        @DisplayName("Registry should have patterns for CRYSTAL type")
        void registryShouldHaveCrystalPatterns() {
            assertTrue(registry.hasPattern(ComponentType.CRYSTAL),
                    "Registry should have CRYSTAL patterns");
        }
    }
}
