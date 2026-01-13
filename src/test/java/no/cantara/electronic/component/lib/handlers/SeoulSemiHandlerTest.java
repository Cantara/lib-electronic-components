package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SeoulSemiHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SeoulSemiHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Seoul Semiconductor LED components.
 *
 * Seoul Semiconductor product lines covered:
 * - Acrich - AC-driven LED modules (SMJHA series)
 * - Z-Power - High-power LEDs (Z5-M, P4, P7 series)
 * - Z5-Mx - Mid-power LEDs (Z5-M0, Z5-M1, Z5-M2)
 * - WICOP - Wafer-Level Integrated Chip on PCB
 * - SunLike - Human-centric lighting
 * - STW/STN - Standard white/neutral LEDs
 * - CUD - UV LEDs
 * - SFH - Infrared LEDs
 */
class SeoulSemiHandlerTest {

    private static SeoulSemiHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SeoulSemiHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Z-Power LED Detection")
    class ZPowerLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect Z5-M series LEDs")
        @ValueSource(strings = {
                "Z5-M0-W0-00",
                "Z5-M1-W0-00",
                "Z5-M2-W0-00",
                "Z5-M0-R0-00",
                "Z5M0-W0",
                "Z5M1-B0"
        })
        void shouldDetectZ5MSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect P4/P7 high-power series")
        @ValueSource(strings = {
                "P4-W0-00",
                "P7-W0-00",
                "P4SC-W0",
                "P7-R0"
        })
        void shouldDetectP4P7Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect X-series high-power LEDs")
        @ValueSource(strings = {
                "X10-W0-00",
                "X20-B0",
                "X15SC-W"
        })
        void shouldDetectXSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }
    }

    @Nested
    @DisplayName("Z5-M Mid-Power LED Detection")
    class Z5MMidPowerTests {

        @ParameterizedTest
        @DisplayName("Should extract series from Z5-M variants")
        @CsvSource({
                "Z5-M0-W0-00, Z5-M0",
                "Z5-M1-W0-00, Z5-M1",
                "Z5-M2-R0-00, Z5-M2",
                "Z5M0-W0, Z5-M0",
                "Z5M1-B0, Z5-M1"
        })
        void shouldExtractZ5MSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Z5-M0 parts should match as high-power LEDs")
        void z5M0ShouldMatchHighPower() {
            assertTrue(handler.matches("Z5-M0-W0-00", ComponentType.LED, registry));
            assertEquals("Ceramic", handler.extractPackageCode("Z5-M0-W0-00"));
        }
    }

    @Nested
    @DisplayName("Acrich AC LED Detection")
    class AcrichLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect Acrich SMJHA series")
        @ValueSource(strings = {
                "SMJHA3V1W1P0S0",
                "SMJHA-3V1W1P0S0",
                "SMJHA5V2W1P0",
                "SMJHA-6V"
        })
        void shouldDetectAcrichSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Should extract Acrich series")
        void shouldExtractAcrichSeries() {
            assertEquals("Acrich", handler.extractSeries("SMJHA3V1W1P0S0"));
            assertEquals("Acrich", handler.extractSeries("SMJHA-3V1W1P0S0"));
        }

        @Test
        @DisplayName("Acrich should have Module package code")
        void acrichShouldHaveModulePackage() {
            assertEquals("Module", handler.extractPackageCode("SMJHA3V1W1P0S0"));
            assertEquals("Module", handler.extractPackageCode("SMJHA-5V2W1"));
        }

        @Test
        @DisplayName("Acrich should have AC-Driven technology")
        void acrichShouldHaveACDrivenTechnology() {
            assertEquals("AC-Driven", handler.getTechnology("SMJHA3V1W1P0S0"));
        }
    }

    @Nested
    @DisplayName("WICOP LED Detection")
    class WICOPLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect WICOP series")
        @ValueSource(strings = {
                "W3030-W0",
                "W2835-CW",
                "W5050RGB",
                "W100-W"
        })
        void shouldDetectWICOPSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Should extract WICOP series")
        void shouldExtractWICOPSeries() {
            assertEquals("WICOP", handler.extractSeries("W3030-W0"));
            assertEquals("WICOP", handler.extractSeries("W2835-CW"));
        }

        @Test
        @DisplayName("WICOP should have CSP package code")
        void wiicopShouldHaveCSPPackage() {
            assertEquals("CSP", handler.extractPackageCode("W3030-W0"));
        }

        @Test
        @DisplayName("WICOP should have WICOP technology")
        void wiicopShouldHaveWICOPTechnology() {
            assertEquals("WICOP (Wafer-Level CSP)", handler.getTechnology("W3030-W0"));
        }
    }

    @Nested
    @DisplayName("SunLike LED Detection")
    class SunLikeLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect SunLike series")
        @ValueSource(strings = {
                "SPHWHTL3D50YE3KPH",
                "SPHWWT2D50YE3",
                "SPHWHTAL50M5",
                "SPHWWT3535"
        })
        void shouldDetectSunLikeSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Should extract SunLike series")
        void shouldExtractSunLikeSeries() {
            assertEquals("SunLike", handler.extractSeries("SPHWHTL3D50YE3KPH"));
            assertEquals("SunLike", handler.extractSeries("SPHWWT2D50YE3"));
        }

        @Test
        @DisplayName("SunLike should have SMD package code")
        void sunLikeShouldHaveSMDPackage() {
            assertEquals("3535", handler.extractPackageCode("SPHWWT3535"));
            assertEquals("SMD", handler.extractPackageCode("SPHWHTL3D50YE3KPH"));
        }

        @Test
        @DisplayName("SunLike should have Human-Centric technology")
        void sunLikeShouldHaveHumanCentricTechnology() {
            assertEquals("SunLike (Human-Centric)", handler.getTechnology("SPHWHTL3D50YE3KPH"));
        }
    }

    @Nested
    @DisplayName("Standard LED (STW/STN) Detection")
    class StandardLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect STW white LED series")
        @ValueSource(strings = {
                "STW9Q14C-W9",
                "STW8Q14BE",
                "STW8C3B-W",
                "STW9Q14-W5"
        })
        void shouldDetectSTWSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect STN neutral LED series")
        @ValueSource(strings = {
                "STN9Q14C-N9",
                "STN8Q14BE",
                "STN8C3B-N"
        })
        void shouldDetectSTNSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should extract STW/STN series")
        @CsvSource({
                "STW9Q14C-W9, STW9Q14C",
                "STW8Q14BE, STW8Q14BE",
                "STN9Q14C-N9, STN9Q14C"
        })
        void shouldExtractSTWSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("STW should have Standard White technology")
        void stwShouldHaveStandardWhiteTechnology() {
            assertEquals("Standard White", handler.getTechnology("STW9Q14C-W9"));
        }

        @Test
        @DisplayName("STN should have Standard Neutral technology")
        void stnShouldHaveStandardNeutralTechnology() {
            assertEquals("Standard Neutral", handler.getTechnology("STN9Q14C-N9"));
        }
    }

    @Nested
    @DisplayName("UV LED (CUD) Detection")
    class UVLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect CUD UV LED series")
        @ValueSource(strings = {
                "CUD6GF1B",
                "CUD8AF1B",
                "CUD1AF1",
                "CUD5GF1A"
        })
        void shouldDetectCUDSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should extract CUD series code")
        @CsvSource({
                "CUD6GF1B, CUD6",
                "CUD8AF1B, CUD8",
                "CUD1AF1, CUD1"
        })
        void shouldExtractCUDSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("CUD should have UV package code")
        void cudShouldHaveUVPackage() {
            assertEquals("UV", handler.extractPackageCode("CUD6GF1B"));
        }

        @Test
        @DisplayName("CUD should have UV LED technology")
        void cudShouldHaveUVTechnology() {
            assertEquals("UV LED", handler.getTechnology("CUD6GF1B"));
        }

        @Test
        @DisplayName("CUD should extract UV color code")
        void cudShouldExtractUVColor() {
            assertEquals("UV", handler.extractColorCode("CUD6GF1B"));
        }
    }

    @Nested
    @DisplayName("Infrared LED (SFH) Detection")
    class InfraredLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect SFH IR LED series")
        @ValueSource(strings = {
                "SFH4045N",
                "SFH4050",
                "SFH4547",
                "SFH4725S"
        })
        void shouldDetectSFHSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should extract SFH series code")
        @CsvSource({
                "SFH4045N, SFH4045",
                "SFH4050, SFH4050",
                "SFH4547, SFH4547"
        })
        void shouldExtractSFHSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("SFH should have IR package code")
        void sfhShouldHaveIRPackage() {
            assertEquals("IR", handler.extractPackageCode("SFH4045N"));
        }

        @Test
        @DisplayName("SFH should have Infrared LED technology")
        void sfhShouldHaveIRTechnology() {
            assertEquals("Infrared LED", handler.getTechnology("SFH4045N"));
        }

        @Test
        @DisplayName("SFH should extract IR color code")
        void sfhShouldExtractIRColor() {
            assertEquals("IR", handler.extractColorCode("SFH4045N"));
        }
    }

    @Nested
    @DisplayName("MJT LED Detection")
    class MJTLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect MJT Multi-Junction series")
        @ValueSource(strings = {
                "MJT-3030-W",
                "MJT-5050-CW",
                "MJT3535RGB",
                "MJT-W0"
        })
        void shouldDetectMJTSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Should extract MJT series")
        void shouldExtractMJTSeries() {
            assertEquals("MJT", handler.extractSeries("MJT-3030-W"));
            assertEquals("MJT", handler.extractSeries("MJT3535RGB"));
        }

        @Test
        @DisplayName("MJT should have MJT package code")
        void mjtShouldHaveMJTPackage() {
            assertEquals("MJT", handler.extractPackageCode("MJT-3030-W"));
        }

        @Test
        @DisplayName("MJT should have Multi-Junction technology")
        void mjtShouldHaveMJTechnology() {
            assertEquals("MJT (Multi-Junction)", handler.getTechnology("MJT-3030-W"));
        }
    }

    @Nested
    @DisplayName("N-Series LED Detection")
    class NSeriesLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect N-series niche LEDs")
        @ValueSource(strings = {
                "N100-W0",
                "N200-CW",
                "N50RGB",
                "N75-R"
        })
        void shouldDetectNSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should extract N-series code")
        @CsvSource({
                "N100-W0, N100",
                "N200-CW, N200",
                "N50RGB, N50"
        })
        void shouldExtractNSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from various series")
        @CsvSource({
                "Z5-M0-W0-00, Ceramic",
                "Z5-M1-W0-00, Ceramic",
                "SMJHA3V1W1P0S0, Module",
                "W3030-W0, CSP",
                "CUD6GF1B, UV",
                "SFH4045N, IR",
                "MJT-3030-W, MJT",
                "P4-W0-00, High-Power",
                "X10-W0-00, High-Power"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract embedded size codes")
        @CsvSource({
                "SPHWWT3535, 3535",
                "SPHWWT5050, 5050",
                "SPHWWT2835, 2835"
        })
        void shouldExtractEmbeddedSizeCodes(String mpn, String expectedSize) {
            assertEquals(expectedSize, handler.extractPackageCode(mpn),
                    "Package size for " + mpn);
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
        @DisplayName("Should extract series from Seoul Semi LEDs")
        @CsvSource({
                "Z5-M0-W0-00, Z5-M0",
                "Z5-M1-W0-00, Z5-M1",
                "P4-W0-00, P4",
                "P7-W0-00, P7",
                "X10-W0-00, X10",
                "SMJHA3V1W1P0S0, Acrich",
                "SPHWHTL3D50YE3KPH, SunLike",
                "STW9Q14C-W9, STW9Q14C",
                "CUD6GF1B, CUD6",
                "SFH4045N, SFH4045",
                "W3030-W0, WICOP",
                "MJT-3030-W, MJT",
                "N100-W0, N100"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Color Code Extraction")
    class ColorCodeExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract color codes")
        @CsvSource({
                "Z5-M0-W0-00, W",
                "Z5-M0-CW-00, CW",
                "Z5-M0-WW-00, WW",
                "Z5-M0-NW-00, NW",
                "CUD6GF1B, UV",
                "SFH4045N, IR"
        })
        void shouldExtractColorCodes(String mpn, String expectedColor) {
            assertEquals(expectedColor, handler.extractColorCode(mpn),
                    "Color code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractColorCode(null));
            assertEquals("", handler.extractColorCode(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series same color should be replacements")
        void sameSeriesSameColorShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("Z5-M0-W0-00", "Z5-M0-W0-01"),
                    "Same Z5-M0 series with same color should be replacements");
            assertTrue(handler.isOfficialReplacement("CUD6GF1A", "CUD6GF1B"),
                    "Same CUD6 series should be replacements");
        }

        @Test
        @DisplayName("Same series different color should NOT be replacements")
        void sameSeriesDifferentColorShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("Z5-M0-W0-00", "Z5-M0-CW-00"),
                    "Different colors (W vs CW) should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("Z5-M0-W0-00", "Z5-M1-W0-00"),
                    "Different series (M0 vs M1) should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("P4-W0-00", "P7-W0-00"),
                    "Different P series should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("CUD6GF1B", "CUD8AF1B"),
                    "Different CUD series should NOT be replacements");
        }

        @Test
        @DisplayName("Different product lines should NOT be replacements")
        void differentProductLinesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("Z5-M0-W0-00", "SMJHA3V1W1P0S0"),
                    "Z-Power and Acrich should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("SPHWHTL3D50YE3", "STW9Q14C-W9"),
                    "SunLike and STW should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "Z5-M0-W0-00"));
            assertFalse(handler.isOfficialReplacement("Z5-M0-W0-00", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }

        @Test
        @DisplayName("Empty values should return false")
        void emptyValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement("", "Z5-M0-W0-00"));
            assertFalse(handler.isOfficialReplacement("Z5-M0-W0-00", ""));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.LED));
            assertTrue(types.contains(ComponentType.IC));
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of()")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "Set should be immutable (Set.of())");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractColorCode(null));
            assertEquals("", handler.getTechnology(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractColorCode(""));
            assertEquals("", handler.getTechnology(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("Z5-M0-W0-00", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("z5-m0-w0-00", ComponentType.LED, registry));
            assertTrue(handler.matches("Z5-M0-W0-00", ComponentType.LED, registry));
            assertTrue(handler.matches("smjha3v1w1p0s0", ComponentType.LED, registry));
            assertTrue(handler.matches("SMJHA3V1W1P0S0", ComponentType.LED, registry));
        }

        @Test
        @DisplayName("Should NOT match non-Seoul Semiconductor patterns")
        void shouldNotMatchNonSeoulPatterns() {
            assertFalse(handler.matches("OSRAM LA E65B", ComponentType.LED, registry));
            assertFalse(handler.matches("LUMILEDS LUXEON", ComponentType.LED, registry));
            assertFalse(handler.matches("CLVBA-FKA", ComponentType.LED, registry));
            assertFalse(handler.matches("LG R971", ComponentType.LED, registry));
        }

        @Test
        @DisplayName("Should handle MPNs with and without dashes")
        void shouldHandleDashVariants() {
            // With dash
            assertTrue(handler.matches("Z5-M0-W0-00", ComponentType.LED, registry));
            assertTrue(handler.matches("SMJHA-3V1W1P0S0", ComponentType.LED, registry));

            // Without dash
            assertTrue(handler.matches("Z5M0W000", ComponentType.LED, registry));
            assertTrue(handler.matches("SMJHA3V1W1P0S0", ComponentType.LED, registry));
        }
    }

    @Nested
    @DisplayName("Technology Extraction")
    class TechnologyExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract technology type")
        @CsvSource({
                "SMJHA3V1W1P0S0, AC-Driven",
                "W3030-W0, WICOP (Wafer-Level CSP)",
                "MJT-3030-W, MJT (Multi-Junction)",
                "SPHWHTL3D50YE3KPH, SunLike (Human-Centric)",
                "CUD6GF1B, UV LED",
                "SFH4045N, Infrared LED",
                "Z5-M0-W0-00, High-Power LED",
                "P4-W0-00, High-Power LED",
                "X10-W0-00, High-Power LED",
                "STW9Q14C-W9, Standard White",
                "STN9Q14C-N9, Standard Neutral"
        })
        void shouldExtractTechnology(String mpn, String expectedTechnology) {
            assertEquals(expectedTechnology, handler.getTechnology(mpn),
                    "Technology for " + mpn);
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {

        @Test
        @DisplayName("Should return empty manufacturer types")
        void shouldReturnEmptyManufacturerTypes() {
            var types = handler.getManufacturerTypes();
            assertTrue(types.isEmpty(), "Manufacturer types should be empty");
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @ParameterizedTest
        @DisplayName("Should handle documented Seoul Semi MPNs")
        @ValueSource(strings = {
                "STW9Q14C-W9",        // Standard white LED
                "Z5-M0-W0-00",        // Z-Power mid-power
                "SPHWHTL3D50YE3KPH",  // SunLike
                "CUD6GF1B"            // UV LED
        })
        void shouldHandleDocumentedMPNs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should be recognized");
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    mpn + " should have extractable series");
        }
    }
}
