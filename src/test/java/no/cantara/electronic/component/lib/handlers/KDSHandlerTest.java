package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.KDSHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for KDSHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for KDS (Daishinku Corporation) crystals, oscillators, and SAW devices.
 *
 * KDS Product Lines:
 * - DSX series: SMD crystals (DSX321G, DSX530GA, DSX840GA)
 * - DST series: Tuning fork crystals (DST310S, DST410S)
 * - DSO series: Clock oscillators (DSO321SR, DSO531SDH)
 * - DSB series: SAW filters/resonators (DSB321SDA)
 * - 1N series: Crystal units (1N-26.000)
 */
class KDSHandlerTest {

    private static KDSHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new KDSHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("DSX Series - SMD Crystals")
    class DSXCrystalTests {

        @ParameterizedTest
        @DisplayName("Should detect DSX SMD crystals")
        @ValueSource(strings = {
                "DSX321G",
                "DSX321GA",
                "DSX530GA",
                "DSX840GA",
                "DSX211G",
                "DSX221G",
                "DSX320G",
                "DSX750G"
        })
        void shouldDetectDSXCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect DSX crystals with frequency suffix")
        @ValueSource(strings = {
                "DSX321G-16.000M",
                "DSX321GA-24.000M",
                "DSX530GA-8.000M",
                "DSX840GA-25.000M"
        })
        void shouldDetectDSXCrystalsWithFrequency(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should extract DSX package codes")
        @CsvSource({
                "DSX321G, 3.2 x 1.3mm (SMD ceramic)",
                "DSX321GA, 3.2 x 1.3mm (SMD ceramic AEC-Q200)",
                "DSX530GA, 5.0 x 3.2mm (SMD ceramic AEC-Q200)",
                "DSX211G, 2.0 x 1.2mm (SMD ceramic)"
        })
        void shouldExtractDSXPackageCodes(String mpn, String expectedPackage) {
            String result = handler.extractPackageCode(mpn);
            assertNotNull(result, "Package code for " + mpn + " should not be null");
            assertTrue(result.contains("x"), "Package code should contain dimension indicator");
        }

        @ParameterizedTest
        @DisplayName("Should extract DSX series")
        @CsvSource({
                "DSX321G, DSX (SMD Crystal)",
                "DSX321GA, DSX (SMD Crystal AEC-Q200)",
                "DSX530GA, DSX (SMD Crystal AEC-Q200)",
                "DSX840G, DSX (SMD Crystal)"
        })
        void shouldExtractDSXSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("DST Series - Tuning Fork Crystals")
    class DSTCrystalTests {

        @ParameterizedTest
        @DisplayName("Should detect DST tuning fork crystals")
        @ValueSource(strings = {
                "DST310S",
                "DST410S",
                "DST210S",
                "DST520S",
                "DST310SR"
        })
        void shouldDetectDSTCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should extract DST package codes")
        @CsvSource({
                "DST310S, 3.1 x 1.5mm",
                "DST410S, 4.1 x 1.5mm",
                "DST210S, 2.0 x 1.2mm",
                "DST520S, 5.0 x 2.0mm"
        })
        void shouldExtractDSTPackageCodes(String mpn, String expectedPackage) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expectedPackage, result, "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should extract DST series")
        void shouldExtractDSTSeries() {
            assertEquals("DST (Tuning Fork Crystal)", handler.extractSeries("DST310S"));
            assertEquals("DST (Tuning Fork Crystal)", handler.extractSeries("DST410S"));
        }
    }

    @Nested
    @DisplayName("DSO Series - Clock Oscillators")
    class DSOOscillatorTests {

        @ParameterizedTest
        @DisplayName("Should detect DSO clock oscillators")
        @ValueSource(strings = {
                "DSO321SR",
                "DSO531SDH",
                "DSO211S",
                "DSO321S",
                "DSO750SR"
        })
        void shouldDetectDSOOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should NOT match DSO as CRYSTAL")
        @ValueSource(strings = {"DSO321SR", "DSO531SDH"})
        void shouldNotMatchDSOAsCrystal(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should NOT match CRYSTAL (it's an oscillator)");
        }

        @ParameterizedTest
        @DisplayName("Should extract DSO package codes")
        @CsvSource({
                "DSO321SR, 3.2 x 2.5mm",
                "DSO531SDH, 5.0 x 3.2mm",
                "DSO211S, 2.0 x 1.6mm"
        })
        void shouldExtractDSOPackageCodes(String mpn, String expectedPackage) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expectedPackage, result, "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract DSO series")
        @CsvSource({
                "DSO321SR, DSO (Clock Oscillator)",
                "DSO531SDH, DSO (High Stability Oscillator)",
                "DSO211S, DSO (Clock Oscillator)"
        })
        void shouldExtractDSOSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("DSB Series - SAW Filters/Resonators")
    class DSBSAWTests {

        @ParameterizedTest
        @DisplayName("Should detect DSB SAW devices")
        @ValueSource(strings = {
                "DSB321SDA",
                "DSB211S",
                "DSB531S",
                "DSB321S"
        })
        void shouldDetectDSBSAWDevices(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC (SAW devices)");
        }

        @ParameterizedTest
        @DisplayName("Should NOT match DSB as CRYSTAL or OSCILLATOR")
        @ValueSource(strings = {"DSB321SDA", "DSB211S"})
        void shouldNotMatchDSBAsCrystalOrOscillator(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should NOT match CRYSTAL");
            assertFalse(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should NOT match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should extract DSB series")
        @CsvSource({
                "DSB321S, DSB (SAW Filter/Resonator)",
                "DSB321SDA, DSB (SAW Filter/Resonator AEC-Q200)"
        })
        void shouldExtractDSBSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("1N Series - Crystal Units")
    class CrystalUnitTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N crystal units")
        @ValueSource(strings = {
                "1N-26.000",
                "1N-16.000",
                "1N-8.000",
                "1N26.000",
                "1N16"
        })
        void shouldDetect1NCrystalUnits(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @Test
        @DisplayName("Should extract 1N package code")
        void shouldExtract1NPackageCode() {
            assertEquals("HC-49U", handler.extractPackageCode("1N-26.000"),
                    "1N series should return HC-49U package");
        }

        @Test
        @DisplayName("Should extract 1N series")
        void shouldExtract1NSeries() {
            assertEquals("1N (Crystal Unit)", handler.extractSeries("1N-26.000"));
            assertEquals("1N (Crystal Unit)", handler.extractSeries("1N16"));
        }
    }

    @Nested
    @DisplayName("Other Series - DX and SM")
    class OtherSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect DX standard crystals")
        @ValueSource(strings = {"DX26", "DX32", "DX8"})
        void shouldDetectDXCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect SM surface mount crystals")
        @ValueSource(strings = {"SM26", "SM32", "SM8"})
        void shouldDetectSMCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @Test
        @DisplayName("Should extract DX series")
        void shouldExtractDXSeries() {
            assertEquals("DX (Standard Crystal)", handler.extractSeries("DX26"));
        }

        @Test
        @DisplayName("Should extract SM series")
        void shouldExtractSMSeries() {
            assertEquals("SM (Surface Mount Crystal)", handler.extractSeries("SM26"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should return non-empty package code for valid KDS MPNs")
        void shouldReturnNonEmptyForValidMpns() {
            assertFalse(handler.extractPackageCode("DSX321G").isEmpty());
            assertFalse(handler.extractPackageCode("DST310S").isEmpty());
            assertFalse(handler.extractPackageCode("DSO321SR").isEmpty());
            assertFalse(handler.extractPackageCode("1N-26.000").isEmpty());
        }

        @Test
        @DisplayName("Should handle package codes with different suffixes")
        void shouldHandleDifferentSuffixes() {
            String pkgG = handler.extractPackageCode("DSX321G");
            String pkgGA = handler.extractPackageCode("DSX321GA");
            String pkgS = handler.extractPackageCode("DSO321S");
            String pkgSR = handler.extractPackageCode("DSO321SR");

            assertNotNull(pkgG);
            assertNotNull(pkgGA);
            assertNotNull(pkgS);
            assertNotNull(pkgSR);

            // GA (AEC-Q200) suffix should be present in package code
            assertTrue(pkgGA.contains("AEC-Q200"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same DSX crystal should be replacement")
        void sameDSXCrystalShouldBeReplacement() {
            assertTrue(handler.isOfficialReplacement("DSX321G", "DSX321G"),
                    "Identical parts should be replacements");
        }

        @Test
        @DisplayName("AEC-Q200 grade can replace standard")
        void aecQ200CanReplaceStandard() {
            assertTrue(handler.isOfficialReplacement("DSX321G", "DSX321GA"),
                    "AEC-Q200 grade should be able to replace standard grade");
        }

        @Test
        @DisplayName("Standard cannot replace AEC-Q200")
        void standardCannotReplaceAecQ200() {
            // Standard replacing automotive grade is usually not acceptable
            // The handler allows AEC-Q200 to replace standard, not the other way
            assertFalse(handler.isOfficialReplacement("DSX321GA", "DSX321G"),
                    "Standard grade should not replace AEC-Q200 grade");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("DSX321G", "DST310S"),
                    "Different series should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("DSX321G", "DSO321SR"),
                    "Crystal should NOT replace oscillator");
        }

        @Test
        @DisplayName("Different sizes should NOT be replacements")
        void differentSizesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("DSX321G", "DSX530G"),
                    "Different sizes should NOT be replacements");
        }

        @Test
        @DisplayName("Same 1N crystal with same frequency should be replacement")
        void same1NCrystalShouldBeReplacement() {
            assertTrue(handler.isOfficialReplacement("1N-26.000", "1N-26.000"),
                    "Identical 1N crystals should be replacements");
        }

        @Test
        @DisplayName("1N crystals with different frequencies should NOT be replacements")
        void different1NFrequenciesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("1N-26.000", "1N-16.000"),
                    "Different frequencies should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "DSX321G"));
            assertFalse(handler.isOfficialReplacement("DSX321G", null));
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
            assertTrue(types.contains(ComponentType.CRYSTAL),
                    "Should support CRYSTAL");
            assertTrue(types.contains(ComponentType.OSCILLATOR),
                    "Should support OSCILLATOR");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC (for SAW devices)");
        }

        @Test
        @DisplayName("Supported types should use Set.of() (immutable)")
        void supportedTypesShouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "Supported types should be immutable");
        }

        @Test
        @DisplayName("Supported types should have exactly 3 types")
        void supportedTypesShouldHaveCorrectCount() {
            assertEquals(3, handler.getSupportedTypes().size(),
                    "Should support exactly 3 types: CRYSTAL, OSCILLATOR, IC");
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

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("DSX321G", null, registry));
        }

        @Test
        @DisplayName("Should handle case insensitivity")
        void shouldHandleCaseInsensitivity() {
            // Upper case
            assertTrue(handler.matches("DSX321G", ComponentType.CRYSTAL, registry));
            // Lower case
            assertTrue(handler.matches("dsx321g", ComponentType.CRYSTAL, registry));
            // Mixed case
            assertTrue(handler.matches("Dsx321G", ComponentType.CRYSTAL, registry));
        }

        @Test
        @DisplayName("Should handle short MPNs gracefully")
        void shouldHandleShortMpns() {
            // These might not match patterns but should not throw exceptions
            assertDoesNotThrow(() -> handler.extractPackageCode("DS"));
            assertDoesNotThrow(() -> handler.extractSeries("DS"));
            assertDoesNotThrow(() -> handler.matches("DS", ComponentType.CRYSTAL, registry));
        }

        @Test
        @DisplayName("Should handle MPNs with special characters")
        void shouldHandleSpecialCharacters() {
            // 1N series uses dash
            assertTrue(handler.matches("1N-26.000", ComponentType.CRYSTAL, registry));
            assertEquals("1N (Crystal Unit)", handler.extractSeries("1N-26.000"));
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {

        @Test
        @DisplayName("Should return empty manufacturer types set")
        void shouldReturnEmptyManufacturerTypes() {
            var types = handler.getManufacturerTypes();
            assertNotNull(types, "Manufacturer types should not be null");
            assertTrue(types.isEmpty(), "Manufacturer types should be empty");
        }
    }

    @Nested
    @DisplayName("Frequency Extraction")
    class FrequencyExtractionTests {

        @Test
        @DisplayName("Should identify same frequency as compatible for replacement")
        void shouldIdentifySameFrequency() {
            assertTrue(handler.isOfficialReplacement(
                            "DSX321G-16.000M", "DSX321G-16.000M"),
                    "Same frequency should be compatible");
        }

        @Test
        @DisplayName("Should identify different frequencies as incompatible")
        void shouldIdentifyDifferentFrequencies() {
            assertFalse(handler.isOfficialReplacement(
                            "DSX321G-16.000M", "DSX321G-24.000M"),
                    "Different frequencies should not be compatible");
        }
    }

    @Nested
    @DisplayName("Pattern Registry Integration")
    class PatternRegistryTests {

        @Test
        @DisplayName("Should initialize patterns correctly")
        void shouldInitializePatternsCorrectly() {
            PatternRegistry testRegistry = new PatternRegistry();
            handler.initializePatterns(testRegistry);

            // Verify patterns were added by testing matches
            assertTrue(handler.matches("DSX321G", ComponentType.CRYSTAL, testRegistry));
            assertTrue(handler.matches("DSO321SR", ComponentType.OSCILLATOR, testRegistry));
            assertTrue(handler.matches("DSB321SDA", ComponentType.IC, testRegistry));
        }
    }
}
