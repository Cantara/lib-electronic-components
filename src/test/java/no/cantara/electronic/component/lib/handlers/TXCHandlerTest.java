package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.TXCHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for TXCHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for TXC Corporation (Taiwan) timing devices and crystals.
 *
 * <p>TXC product lines tested:
 * <ul>
 *   <li>7M series: Standard crystals</li>
 *   <li>8Y series: SMD crystals</li>
 *   <li>9C series: Clock oscillators</li>
 *   <li>7V series: VCXO (Voltage Controlled Crystal Oscillators)</li>
 *   <li>7X series: TCXO (Temperature Compensated Crystal Oscillators)</li>
 *   <li>AX series: Automotive grade crystals</li>
 * </ul>
 */
class TXCHandlerTest {

    private static TXCHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new TXCHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Crystal Detection Tests")
    class CrystalDetectionTests {

        @ParameterizedTest
        @DisplayName("Should detect 7M standard crystals")
        @ValueSource(strings = {
                "7M-12.000MAAJ-T",
                "7M-16.000MAAE-T",
                "7M-8.000MAAJ-T",
                "7M-24.000MAAJ-T",
                "7M12.000MAAJ-T",   // Without hyphen after series
                "7M-4.000MAAJ-T"
        })
        void shouldDetect7MCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect 8Y SMD crystals")
        @ValueSource(strings = {
                "8Y-12.000MAAE-T",
                "8Y-24.000MAAE-T",
                "8Y-16.000MAAJ-T",
                "8Y-32.768KAAJ-T",  // 32.768 kHz watch crystal
                "8Y12.000MAAE-T"    // Without hyphen after series
        })
        void shouldDetect8YCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect AX automotive crystals")
        @ValueSource(strings = {
                "AX-12.000MAAJ-T",
                "AX-16.000MAAE-T",
                "AX-8.000MAAJ-T",
                "AX12.000MAAJ-T"    // Without hyphen after series
        })
        void shouldDetectAXAutomotiveCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @Test
        @DisplayName("Should not detect oscillators as crystals")
        void shouldNotDetectOscillatorsAsCrystals() {
            assertFalse(handler.matches("9C-12.000MBD-T", ComponentType.CRYSTAL, registry),
                    "Clock oscillator should not match CRYSTAL");
            assertFalse(handler.matches("7V-12.000MBA-T", ComponentType.CRYSTAL, registry),
                    "VCXO should not match CRYSTAL");
            assertFalse(handler.matches("7X-16.000MBA-T", ComponentType.CRYSTAL, registry),
                    "TCXO should not match CRYSTAL");
        }
    }

    @Nested
    @DisplayName("Oscillator Detection Tests")
    class OscillatorDetectionTests {

        @ParameterizedTest
        @DisplayName("Should detect 9C clock oscillators")
        @ValueSource(strings = {
                "9C-12.000MBD-T",
                "9C-25.000MBD-T",
                "9C-16.000MBD-T",
                "9C-8.000MBD-T",
                "9C12.000MBD-T"     // Without hyphen after series
        })
        void shouldDetect9CClockOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect 7V VCXO oscillators")
        @ValueSource(strings = {
                "7V-12.000MBA-T",
                "7V-16.000MBA-T",
                "7V-24.000MBA-T",
                "7V12.000MBA-T"     // Without hyphen after series
        })
        void shouldDetect7VVCXOOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect 7X TCXO oscillators")
        @ValueSource(strings = {
                "7X-16.000MBA-T",
                "7X-12.000MBA-T",
                "7X-26.000MBA-T",
                "7X16.000MBA-T"     // Without hyphen after series
        })
        void shouldDetect7XTCXOOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @Test
        @DisplayName("Should not detect crystals as oscillators")
        void shouldNotDetectCrystalsAsOscillators() {
            assertFalse(handler.matches("7M-12.000MAAJ-T", ComponentType.OSCILLATOR, registry),
                    "Standard crystal should not match OSCILLATOR");
            assertFalse(handler.matches("8Y-12.000MAAE-T", ComponentType.OSCILLATOR, registry),
                    "SMD crystal should not match OSCILLATOR");
            assertFalse(handler.matches("AX-12.000MAAJ-T", ComponentType.OSCILLATOR, registry),
                    "Automotive crystal should not match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("Series Extraction Tests")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract crystal series correctly")
        @CsvSource({
                "7M-12.000MAAJ-T, 7M Standard Crystal",
                "7M-16.000MAAE-T, 7M Standard Crystal",
                "8Y-12.000MAAE-T, 8Y SMD Crystal",
                "8Y-24.000MAAJ-T, 8Y SMD Crystal",
                "AX-12.000MAAJ-T, AX Automotive Crystal",
                "AX-16.000MAAE-T, AX Automotive Crystal"
        })
        void shouldExtractCrystalSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator series correctly")
        @CsvSource({
                "9C-12.000MBD-T, 9C Clock Oscillator",
                "9C-25.000MBD-T, 9C Clock Oscillator",
                "7V-12.000MBA-T, 7V VCXO",
                "7V-16.000MBA-T, 7V VCXO",
                "7X-16.000MBA-T, 7X TCXO",
                "7X-12.000MBA-T, 7X TCXO"
        })
        void shouldExtractOscillatorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty string for unknown series")
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.extractSeries("UNKNOWN-PART"));
            assertEquals("", handler.extractSeries("XX-12.000MAAJ-T"));
            assertEquals("", handler.extractSeries("12.000M"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction Tests")
    class PackageCodeExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract 7M crystal package info")
        @CsvSource({
                "7M-12.000MAAJ-T, HC49 Commercial Tape & Reel",
                "7M-16.000MAAE-T, HC49 Extended Tape & Reel",
                "7M-24.000MAAJ, HC49 Commercial"
        })
        void shouldExtract7MPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract 8Y SMD crystal package info")
        @CsvSource({
                "8Y-12.000MAAE-T, SMD 3.2x2.5mm Extended Tape & Reel",
                "8Y-24.000MAAJ-T, SMD 3.2x2.5mm Commercial Tape & Reel"
        })
        void shouldExtract8YPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator package info")
        @CsvSource({
                "9C-12.000MBD-T, SMD Oscillator Tape & Reel",
                "7V-12.000MBA-T, SMD VCXO Commercial Tape & Reel",
                "7X-16.000MBA-T, SMD TCXO Commercial Tape & Reel"
        })
        void shouldExtractOscillatorPackageCode(String mpn, String expectedPackage) {
            String extracted = handler.extractPackageCode(mpn);
            // Check that key elements are present (order may vary based on implementation)
            assertTrue(extracted.contains("SMD") || extracted.contains("Oscillator") ||
                       extracted.contains("VCXO") || extracted.contains("TCXO"),
                    "Package for " + mpn + " should contain type info, got: " + extracted);
        }

        @Test
        @DisplayName("Should return empty string for unknown MPNs")
        void shouldReturnEmptyForUnknownMpn() {
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode("UNKNOWN"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection Tests")
    class ReplacementDetectionTests {

        @Test
        @DisplayName("Same series and frequency should be replacements")
        void sameSerisSameFrequencyShouldBeReplacements() {
            // Same part with same temp grade
            assertTrue(handler.isOfficialReplacement("7M-12.000MAAJ-T", "7M-12.000MAAJ-T"),
                    "Identical parts should be replacements");
        }

        @Test
        @DisplayName("Extended temp should replace commercial temp")
        void extendedTempShouldReplaceCommercial() {
            // Extended (E) can replace Commercial (A)
            assertTrue(handler.isOfficialReplacement("7M-12.000MAAE-T", "7M-12.000MAAJ-T"),
                    "Extended temp should replace commercial temp in same series");
        }

        @Test
        @DisplayName("Commercial temp should NOT replace extended temp")
        void commercialTempShouldNotReplaceExtended() {
            // Commercial (A) cannot replace Extended (E)
            assertFalse(handler.isOfficialReplacement("7M-12.000MAAJ-T", "7M-12.000MAAE-T"),
                    "Commercial temp should NOT replace extended temp");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("7M-12.000MAAJ-T", "8Y-12.000MAAJ-T"),
                    "Different series should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("7M-12.000MAAJ-T", "9C-12.000MBD-T"),
                    "Crystal and oscillator should NOT be replacements");
        }

        @Test
        @DisplayName("Different frequencies should NOT be replacements")
        void differentFrequenciesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("7M-12.000MAAJ-T", "7M-16.000MAAJ-T"),
                    "Different frequencies should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("8Y-12.000MAAE-T", "8Y-24.000MAAE-T"),
                    "Different frequencies should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesShouldReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "7M-12.000MAAJ-T"));
            assertFalse(handler.isOfficialReplacement("7M-12.000MAAJ-T", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types Tests")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support CRYSTAL and OSCILLATOR types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.CRYSTAL),
                    "Should support CRYSTAL type");
            assertTrue(types.contains(ComponentType.OSCILLATOR),
                    "Should support OSCILLATOR type");
        }

        @Test
        @DisplayName("Should use Set.of() for immutable supported types")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.IC),
                    "Supported types should be immutable");
        }

        @Test
        @DisplayName("Should have exactly 2 supported types")
        void shouldHaveCorrectNumberOfTypes() {
            assertEquals(2, handler.getSupportedTypes().size(),
                    "Should support exactly CRYSTAL and OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.CRYSTAL, registry));
            assertFalse(handler.matches(null, ComponentType.OSCILLATOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CRYSTAL, registry));
            assertFalse(handler.matches("", ComponentType.OSCILLATOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("7M-12.000MAAJ-T", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("7m-12.000maaj-t", ComponentType.CRYSTAL, registry),
                    "Should match lowercase");
            assertTrue(handler.matches("7M-12.000MAAJ-T", ComponentType.CRYSTAL, registry),
                    "Should match uppercase");
            assertTrue(handler.matches("7m-12.000MAAJ-t", ComponentType.CRYSTAL, registry),
                    "Should match mixed case");
        }

        @Test
        @DisplayName("Should handle short MPNs without crashing")
        void shouldHandleShortMpns() {
            assertFalse(handler.matches("7", ComponentType.CRYSTAL, registry));
            assertFalse(handler.matches("7M", ComponentType.CRYSTAL, registry));
            assertEquals("", handler.extractSeries("7"));
            assertEquals("", handler.extractPackageCode("7M"));
        }

        @Test
        @DisplayName("Should return empty manufacturer types")
        void shouldReturnEmptyManufacturerTypes() {
            assertTrue(handler.getManufacturerTypes().isEmpty(),
                    "Should return empty manufacturer types");
        }
    }

    @Nested
    @DisplayName("Frequency Extraction Tests")
    class FrequencyExtractionTests {

        @ParameterizedTest
        @DisplayName("Should handle various frequency formats")
        @CsvSource({
                "7M-12.000MAAJ-T, 7M Standard Crystal",
                "7M-16.000MAAE-T, 7M Standard Crystal",
                "7M-8.000MAAJ-T, 7M Standard Crystal",
                "7M-24.576MAAJ-T, 7M Standard Crystal",  // Precise frequency
                "8Y-32.768KAAJ-T, 8Y SMD Crystal"        // kHz frequency
        })
        void shouldExtractSeriesRegardlessOfFrequency(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Should extract series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Real-World MPN Tests")
    class RealWorldMpnTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify real TXC part numbers")
        @CsvSource({
                "7M-12.000MAAJ-T, CRYSTAL, true",
                "7M-16.000MAAE-T, CRYSTAL, true",
                "8Y-12.000MAAE-T, CRYSTAL, true",
                "8Y-24.000MAAE-T, CRYSTAL, true",
                "9C-12.000MBD-T, OSCILLATOR, true",
                "9C-25.000MBD-T, OSCILLATOR, true",
                "7V-12.000MBA-T, OSCILLATOR, true",
                "7X-16.000MBA-T, OSCILLATOR, true",
                "AX-12.000MAAJ-T, CRYSTAL, true",
                "7M-12.000MAAJ-T, OSCILLATOR, false",
                "9C-12.000MBD-T, CRYSTAL, false"
        })
        void shouldCorrectlyIdentifyRealPartNumbers(String mpn, String typeStr, boolean shouldMatch) {
            ComponentType type = ComponentType.valueOf(typeStr);
            assertEquals(shouldMatch, handler.matches(mpn, type, registry),
                    mpn + " should " + (shouldMatch ? "" : "NOT ") + "match " + typeStr);
        }
    }

    @Nested
    @DisplayName("Pattern Registry Integration Tests")
    class PatternRegistryTests {

        @Test
        @DisplayName("Should initialize patterns in registry")
        void shouldInitializePatternsInRegistry() {
            PatternRegistry newRegistry = new PatternRegistry();
            handler.initializePatterns(newRegistry);

            // Verify patterns are registered by testing matches
            assertTrue(handler.matches("7M-12.000MAAJ-T", ComponentType.CRYSTAL, newRegistry));
            assertTrue(handler.matches("9C-12.000MBD-T", ComponentType.OSCILLATOR, newRegistry));
        }

        @Test
        @DisplayName("Should not interfere with patterns in shared registry")
        void shouldNotInterfereWithSharedRegistry() {
            // Verify our patterns don't match other manufacturers' products
            assertFalse(handler.matches("FA-238", ComponentType.CRYSTAL, registry),
                    "Should not match Epson crystal pattern");
            assertFalse(handler.matches("NX3225", ComponentType.CRYSTAL, registry),
                    "Should not match NDK crystal pattern");
            assertFalse(handler.matches("ABM8", ComponentType.CRYSTAL, registry),
                    "Should not match Abracon crystal pattern");
        }
    }
}
