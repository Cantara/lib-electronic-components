package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.LelonHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test cases for LelonHandler.
 *
 * Lelon Electronics specializes in aluminum electrolytic capacitors:
 * - VE series: Low impedance
 * - VR series: Standard
 * - VZ series: High temperature
 * - REA/REB/REC series: Radial lead
 * - OVZ/OVR series: Conductive polymer
 *
 * MPN Structure example: VE-101M1CTR-0605
 * - VE = Series (Low Impedance)
 * - 101 = Capacitance (100uF - first two digits significant, third is multiplier)
 * - M = Tolerance (20%)
 * - 1C = Voltage code (16V)
 * - TR = Tape and Reel
 * - 0605 = Package (6.3x5mm diameter x height)
 */
class LelonHandlerTest {

    private static LelonHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new LelonHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldReturnCorrectSupportedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.CAPACITOR), "Should support CAPACITOR");
            assertTrue(types.contains(ComponentType.IC), "Should support IC");
            assertEquals(2, types.size(), "Should have exactly 2 supported types");
        }

        @Test
        void supportedTypesShouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes() should return immutable Set");
        }
    }

    @Nested
    @DisplayName("VE Series - Low Impedance Capacitors")
    class VESeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "VE-101M1CTR-0605",   // 100uF, 16V, 6.3x5mm
            "VE-470M1ETR-0810",   // 47uF, 25V, 8x10mm
            "VE-102M1HTR-1012",   // 1000uF, 50V, 10x12mm
            "VE-221M1VTR-0811",   // 220uF, 35V, 8x11mm
            "VE-100M1ATR-0605"    // 10uF, 10V, 6.3x5mm
        })
        void shouldMatchVESeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                mpn + " should match as CAPACITOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "ve-101m1ctr-0605",   // lowercase
            "Ve-470M1Etr-0810"    // mixed case
        })
        void shouldMatchVESeriesCaseInsensitive(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                mpn + " should match regardless of case");
        }

        @Test
        void shouldExtractVESeriesName() {
            assertEquals("VE Low Impedance", handler.extractSeries("VE-101M1CTR-0605"));
        }
    }

    @Nested
    @DisplayName("VR Series - Standard Capacitors")
    class VRSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "VR-100M1ETR-0811",   // 10uF, 25V, 8x11mm
            "VR-471M1CTR-0605",   // 470uF, 16V, 6.3x5mm
            "VR-102M1HTR-1012",   // 1000uF, 50V, 10x12mm
            "VR-220M2ATR-1020"    // 22uF, 100V, 10x20mm
        })
        void shouldMatchVRSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                mpn + " should match as CAPACITOR");
        }

        @Test
        void shouldExtractVRSeriesName() {
            assertEquals("VR Standard", handler.extractSeries("VR-100M1ETR-0811"));
        }
    }

    @Nested
    @DisplayName("VZ Series - High Temperature Capacitors")
    class VZSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "VZ-470M1HTR-0810",   // 47uF, 50V, 8x10mm
            "VZ-101M1ETR-0605",   // 100uF, 25V, 6.3x5mm
            "VZ-102M1CTR-0811"    // 1000uF, 16V, 8x11mm
        })
        void shouldMatchVZSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                mpn + " should match as CAPACITOR");
        }

        @Test
        void shouldExtractVZSeriesName() {
            assertEquals("VZ High Temperature", handler.extractSeries("VZ-470M1HTR-0810"));
        }
    }

    @Nested
    @DisplayName("REA/REB/REC Series - Radial Lead Capacitors")
    class RESeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "REA221M1CTR-0810",   // REA series, 220uF, 16V
            "REA101M1ETR-0605",   // REA series, 100uF, 25V
            "REB471M1HTR-1012",   // REB series, 470uF, 50V
            "REB102M1CTR-0811",   // REB series, 1000uF, 16V
            "REC103M1ETR-1620",   // REC series, 10000uF, 25V
            "REC222M2ATR-1825"    // REC series, 2200uF, 100V
        })
        void shouldMatchRESeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                mpn + " should match as CAPACITOR");
        }

        @ParameterizedTest
        @CsvSource({
            "REA221M1CTR-0810, REA Radial Lead",
            "REB471M1HTR-1012, REB Radial Lead",
            "REC103M1ETR-1620, REC Radial Lead"
        })
        void shouldExtractRESeriesName(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("OVZ/OVR Series - Conductive Polymer Capacitors")
    class PolymerSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "OVZ-100M1ETR-0605",  // OVZ series, 10uF, 25V
            "OVZ-221M1CTR-0605",  // OVZ series, 220uF, 16V
            "OVR-470M1VTR-0810",  // OVR series, 47uF, 35V
            "OVR-101M1ETR-0605"   // OVR series, 100uF, 25V
        })
        void shouldMatchPolymerSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                mpn + " should match as CAPACITOR");
        }

        @ParameterizedTest
        @CsvSource({
            "OVZ-100M1ETR-0605, OVZ Conductive Polymer",
            "OVR-470M1VTR-0810, OVR Conductive Polymer"
        })
        void shouldExtractPolymerSeriesName(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "VE-101M1CTR-0605, 6.3x5mm",
            "VR-100M1ETR-0811, 8x11mm",
            "VZ-470M1HTR-0810, 8x10mm",
            "VE-102M1HTR-1012, 10x12mm",
            "REC103M1ETR-1620, 16x20mm",
            "REC222M2ATR-1825, 18x25mm"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "VE-101M1CTR-0405, 4x5mm",
            "VE-101M1CTR-0507, 5x7mm",
            "VE-101M1CTR-0607, 6.3x7mm",
            "VE-101M1CTR-1215, 12.5x15mm",
            "VE-101M1CTR-2530, 25x30mm",
            "VE-101M1CTR-3040, 30x40mm",
            "VE-101M1CTR-3545, 35x45mm"
        })
        void shouldDecodeVariousPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "VE-101M1CTR-0605, VE Low Impedance",
            "VR-100M1ETR-0811, VR Standard",
            "VZ-470M1HTR-0810, VZ High Temperature",
            "REA221M1CTR-0810, REA Radial Lead",
            "REB471M1HTR-1012, REB Radial Lead",
            "REC103M1ETR-1620, REC Radial Lead",
            "OVZ-100M1ETR-0605, OVZ Conductive Polymer",
            "OVR-470M1VTR-0810, OVR Conductive Polymer"
        })
        void shouldExtractSeriesName(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.extractSeries("XX-100M1ETR-0811"));
            assertEquals("", handler.extractSeries("UNKNOWN123"));
        }
    }

    @Nested
    @DisplayName("Voltage Rating Extraction")
    class VoltageExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "VE-101M1CTR-0605, 16V",
            "VR-100M1ETR-0811, 25V",
            "VZ-470M1HTR-0810, 50V",
            "VE-102M1VTR-1012, 35V",
            "VR-220M2ATR-1020, 100V",
            "VE-100M1ATR-0605, 10V"
        })
        void shouldExtractVoltageRating(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltageRating(mpn));
        }
    }

    @Nested
    @DisplayName("Capacitance Extraction")
    class CapacitanceExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "VE-101M1CTR-0605, 100uF",    // 10 x 10^1 = 100
            "VR-470M1ETR-0811, 47uF",     // 47 x 10^0 = 47
            "VZ-102M1HTR-0810, 1000uF",   // 10 x 10^2 = 1000
            "VE-221M1VTR-0811, 220uF",    // 22 x 10^1 = 220
            "VE-100M1ATR-0605, 10uF",     // 10 x 10^0 = 10
            "REC103M1ETR-1620, 10000uF",  // 10 x 10^3 = 10000
            "VE-222M1CTR-1012, 2200uF"    // 22 x 10^2 = 2200
        })
        void shouldExtractCapacitance(String mpn, String expectedCapacitance) {
            assertEquals(expectedCapacitance, handler.extractCapacitance(mpn));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void sameSeriesSamePackageShouldNotBeAutoReplacement() {
            // Same series and package but potentially different specs
            // isOfficialReplacement returns false for same series unless upgrade path
            assertFalse(handler.isOfficialReplacement("VE-101M1CTR-0605", "VE-102M1CTR-0605"));
        }

        @Test
        void differentPackageShouldNotBeReplacement() {
            // Different packages cannot be replacements
            assertFalse(handler.isOfficialReplacement("VE-101M1CTR-0605", "VE-101M1CTR-0810"));
        }

        @Test
        void veCanReplaceVR() {
            // VE (low impedance) can replace VR (standard) - better performance
            assertTrue(handler.isOfficialReplacement("VE-101M1CTR-0605", "VR-101M1CTR-0605"));
        }

        @Test
        void vzCanReplaceVRandVE() {
            // VZ (high temp) can replace standard series - wider temp range
            assertTrue(handler.isOfficialReplacement("VZ-101M1CTR-0605", "VR-101M1CTR-0605"));
            assertTrue(handler.isOfficialReplacement("VZ-101M1CTR-0605", "VE-101M1CTR-0605"));
        }

        @Test
        void polymerCanReplaceStandard() {
            // OVZ/OVR polymer can replace VR standard when package matches
            assertTrue(handler.isOfficialReplacement("OVZ-101M1CTR-0605", "VR-101M1CTR-0605"));
            assertTrue(handler.isOfficialReplacement("OVR-101M1CTR-0605", "VR-101M1CTR-0605"));
        }

        @Test
        void shouldNotReplaceDifferentSeriesWithDifferentPackage() {
            assertFalse(handler.isOfficialReplacement("VE-101M1CTR-0605", "VR-101M1CTR-0810"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {
        @Test
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractVoltageRating(null));
            assertEquals("", handler.extractCapacitance(null));
            assertFalse(handler.isOfficialReplacement(null, "VE-101M1CTR-0605"));
            assertFalse(handler.isOfficialReplacement("VE-101M1CTR-0605", null));
        }

        @Test
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractVoltageRating(""));
            assertEquals("", handler.extractCapacitance(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("VE-101M1CTR-0605", null, registry));
        }

        @Test
        void shouldNotMatchNonLelonParts() {
            assertFalse(handler.matches("UUD1C100MCL1GS", ComponentType.CAPACITOR, registry),
                "Should not match Nichicon parts");
            assertFalse(handler.matches("EEE-1EA100SP", ComponentType.CAPACITOR, registry),
                "Should not match Panasonic parts");
            assertFalse(handler.matches("GRM188R71C104KA01D", ComponentType.CAPACITOR, registry),
                "Should not match Murata MLCC");
        }

        @Test
        void shouldHandleMalformedMPN() {
            // Missing package code
            assertEquals("", handler.extractPackageCode("VE-101M1CTR"));

            // Very short MPN
            assertEquals("", handler.extractPackageCode("VE"));

            // Invalid format
            assertEquals("", handler.extractSeries("123-ABC"));
        }
    }

    @Nested
    @DisplayName("Pattern Matching Details")
    class PatternMatchingTests {
        @Test
        void shouldNotMatchWithoutHyphenForVSeries() {
            // V-series requires hyphen
            assertFalse(handler.matches("VE101M1CTR0605", ComponentType.CAPACITOR, registry),
                "VE series should require hyphen");
            assertFalse(handler.matches("VR100M1ETR0811", ComponentType.CAPACITOR, registry),
                "VR series should require hyphen");
        }

        @Test
        void shouldMatchRESeriesWithoutHyphen() {
            // RE-series does not use hyphen
            assertTrue(handler.matches("REA221M1CTR0810", ComponentType.CAPACITOR, registry),
                "REA series should match without hyphen");
        }

        @Test
        void shouldNotMatchAsWrongType() {
            assertFalse(handler.matches("VE-101M1CTR-0605", ComponentType.RESISTOR, registry),
                "Lelon capacitor should not match as RESISTOR");
            assertFalse(handler.matches("VE-101M1CTR-0605", ComponentType.INDUCTOR, registry),
                "Lelon capacitor should not match as INDUCTOR");
        }
    }

    @Nested
    @DisplayName("Real-World Part Numbers")
    class RealWorldTests {
        @ParameterizedTest
        @CsvSource({
            "VE-101M1CTR-0605, VE Low Impedance, 6.3x5mm",
            "VR-100M1ETR-0811, VR Standard, 8x11mm",
            "VZ-470M1HTR-0810, VZ High Temperature, 8x10mm",
            "REA221M1CTR-0810, REA Radial Lead, 8x10mm",
            "OVZ-100M1ETR-0605, OVZ Conductive Polymer, 6.3x5mm"
        })
        void shouldParseRealWorldPartNumbers(String mpn, String expectedSeries, String expectedPackage) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                mpn + " should match as CAPACITOR");
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                "Series extraction failed for " + mpn);
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                "Package extraction failed for " + mpn);
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {
        @Test
        void shouldReturnEmptyManufacturerTypes() {
            assertTrue(handler.getManufacturerTypes().isEmpty(),
                "getManufacturerTypes() should return empty set");
        }
    }
}
