package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ElnaHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ElnaHandler.
 * Tests Elna audio-grade aluminum electrolytic capacitors and Dynacap (EDLC) series.
 *
 * Elna is renowned for premium audio capacitors, including:
 * - Silmic II (RFS series): Premium audio-grade
 * - TONEREX (ROA/ROB series): Audio-grade
 * - Standard series (RE3, RJ3, RJH): General purpose electrolytic
 * - Dynacap (DB/DX/DZ): Electric double layer capacitors (supercaps)
 */
class ElnaHandlerTest {

    private static ElnaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ElnaHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldSupportCapacitorType() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.CAPACITOR));
        }

        @Test
        void shouldOnlySupportCapacitors() {
            // Elna only makes capacitors - no IC support
            assertEquals(1, handler.getSupportedTypes().size());
            assertFalse(handler.getSupportedTypes().contains(ComponentType.IC));
        }

        @Test
        void shouldUseImmutableSet() {
            assertThrows(UnsupportedOperationException.class, () ->
                handler.getSupportedTypes().add(ComponentType.RESISTOR)
            );
        }
    }

    @Nested
    @DisplayName("Silmic II Series Detection (RFS)")
    class SilmicIITests {
        @ParameterizedTest
        @ValueSource(strings = {
            "RFS-25V101MH3#P",
            "RFS-50V470MH5#",
            "RFS-16V222MF3#P",
            "RFS-35V102ML5#",
            "RFS-63V100MH7#P"
        })
        void shouldMatchSilmicIISeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "RFS-25V101MH3#P",
            "RFS-50V470MH5#"
        })
        void shouldExtractSilmicIISeries(String mpn) {
            assertEquals("Silmic II", handler.extractSeries(mpn));
        }

        @Test
        void shouldExtractSilmicIIPackageCode() {
            String packageCode = handler.extractPackageCode("RFS-25V101MH3#P");
            assertNotNull(packageCode);
            assertFalse(packageCode.isEmpty());
        }
    }

    @Nested
    @DisplayName("TONEREX Series Detection (ROA/ROB)")
    class TonerexTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ROA-50V4R7MF3#",
            "ROA-25V100MH5#",
            "ROA-35V220MH3#P",
            "ROB-16V470MF5#",
            "ROB-50V100MH7#"
        })
        void shouldMatchTonerexSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @Test
        void shouldExtractTonerexTypeASeries() {
            assertEquals("TONEREX Type A", handler.extractSeries("ROA-50V4R7MF3#"));
        }

        @Test
        void shouldExtractTonerexTypeBSeries() {
            assertEquals("TONEREX Type B", handler.extractSeries("ROB-16V470MF5#"));
        }
    }

    @Nested
    @DisplayName("Standard Aluminum Electrolytic Series Detection")
    class StandardSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "RE3-25V102M",
            "RE3-50V471M",
            "RJ3-16V100M",
            "RJ3-35V220M",
            "RJH-35V470M",
            "RJH-50V101M"
        })
        void shouldMatchStandardSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @ParameterizedTest
        @CsvSource({
            "RE3-25V102M, RE3 Standard",
            "RJ3-16V100M, RJ3 Standard",
            "RJH-35V470M, RJH High Temp"
        })
        void shouldExtractStandardSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Bi-Polar Electrolytic Series Detection (RBD/RBI)")
    class BiPolarTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "RBD-25V100M",
            "RBD-50V470M",
            "RBI-35V220M"
        })
        void shouldMatchBiPolarSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @ParameterizedTest
        @CsvSource({
            "RBD-25V100M, RBD Bi-Polar",
            "RBI-35V220M, RBI Bi-Polar"
        })
        void shouldExtractBiPolarSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Low ESR Series Detection (RSE)")
    class LowESRTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "RSE-25V101M",
            "RSE-35V220M",
            "RSE-50V470M"
        })
        void shouldMatchLowESRSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @Test
        void shouldExtractLowESRSeries() {
            assertEquals("RSE Super Low ESR", handler.extractSeries("RSE-25V101M"));
        }
    }

    @Nested
    @DisplayName("Low Leakage Series Detection (RVD/RVE)")
    class LowLeakageTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "RVD-16V100M",
            "RVD-25V470M",
            "RVE-35V220M"
        })
        void shouldMatchLowLeakageSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @ParameterizedTest
        @CsvSource({
            "RVD-16V100M, RVD Low Leakage",
            "RVE-35V220M, RVE Low Leakage"
        })
        void shouldExtractLowLeakageSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Dynacap (EDLC/Supercap) Series Detection")
    class DynacapTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "DB-5R5D105T",
            "DB-5R5D474V",
            "DX-5R5H105T",
            "DZ-3R3C225H"
        })
        void shouldMatchDynacapSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @ParameterizedTest
        @CsvSource({
            "DB-5R5D105T, Dynacap Standard",
            "DX-5R5H105T, Dynacap Low Profile",
            "DZ-3R3C225H, Dynacap Ultra-Low Profile"
        })
        void shouldExtractDynacapSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "DB-5R5D105T, Radial THT",
            "DB-5R5D474V, Vertical SMD",
            "DX-5R5H105H, Horizontal SMD"
        })
        void shouldExtractDynacapPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Legacy/STARGET Series Detection")
    class LegacySeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "LAO1234",
            "LAS5678"
        })
        void shouldMatchLegacySeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @ParameterizedTest
        @CsvSource({
            "LAO1234, STARGET Audio",
            "LAS5678, STARGET Standard"
        })
        void shouldExtractLegacySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("CE-BP Audio Crossover Series Detection")
    class CEBPTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "CE-BP100",
            "CE-BP220",
            "CE-BP470"
        })
        void shouldMatchCEBPSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR type");
        }

        @Test
        void shouldExtractCEBPSeries() {
            assertEquals("CE-BP Audio Crossover", handler.extractSeries("CE-BP100"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "RFS-25V101MH3#P, 5x11mm",
            "RFS-50V470MH5#, 6.3x11mm",
            "ROA-35V100MF3#, 5x7mm",
            "ROA-50V220MF5#, 6.3x7mm"
        })
        void shouldExtractAndMapPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }

        @Test
        void shouldReturnEmptyForUnknownPackage() {
            // MPN without clear package code
            String result = handler.extractPackageCode("UNKNOWN123");
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("Series Extraction Comprehensive")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "RFS-25V101MH3#P, Silmic II",
            "ROA-50V4R7MF3#, TONEREX Type A",
            "ROB-16V470MF5#, TONEREX Type B",
            "RE3-25V102M, RE3 Standard",
            "RJ3-16V100M, RJ3 Standard",
            "RJH-35V470M, RJH High Temp",
            "RBD-25V100M, RBD Bi-Polar",
            "RBI-35V220M, RBI Bi-Polar",
            "RSE-25V101M, RSE Super Low ESR",
            "RVD-16V100M, RVD Low Leakage",
            "RVE-35V220M, RVE Low Leakage",
            "DB-5R5D105T, Dynacap Standard",
            "DX-5R5H105T, Dynacap Low Profile",
            "DZ-3R3C225H, Dynacap Ultra-Low Profile",
            "LAO1234, STARGET Audio",
            "LAS5678, STARGET Standard",
            "CE-BP100, CE-BP Audio Crossover"
        })
        void shouldExtractCorrectSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "RFS-25V101MH3#P"));
            assertFalse(handler.isOfficialReplacement("RFS-25V101MH3#P", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("RFS-25V101MH3#P", null, registry));
        }

        @Test
        void shouldNotMatchNonElnaParts() {
            assertFalse(handler.matches("UUD1C100MCL1GS", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("GRM155R71H104KA88D", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("ATMEGA328P", ComponentType.CAPACITOR, registry));
        }

        @Test
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("rfs-25v101mh3#p", ComponentType.CAPACITOR, registry));
            assertTrue(handler.matches("RFS-25V101MH3#P", ComponentType.CAPACITOR, registry));
            assertTrue(handler.matches("Rfs-25V101Mh3#P", ComponentType.CAPACITOR, registry));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossDifferentSeries() {
            // Different series (Silmic II vs RE3) should not be replacements
            assertFalse(handler.isOfficialReplacement("RFS-25V101MH3#P", "RE3-25V101M"));
        }

        @Test
        void silmicIICanReplaceTonerex() {
            // Silmic II (premium) can replace TONEREX (standard audio)
            // when voltage and capacitance match
            assertTrue(handler.isOfficialReplacement("RFS-25V100MH5#", "ROA-25V100MH5#"));
        }

        @Test
        void tonerexTypesAreInterchangeable() {
            // TONEREX Type A and Type B are interchangeable with same specs
            assertTrue(handler.isOfficialReplacement("ROA-25V100MH5#", "ROB-25V100MH5#"));
        }

        @Test
        void sameSeriesSameSpecsAreReplacements() {
            // Same series with same voltage and capacitance
            assertTrue(handler.isOfficialReplacement("RFS-25V101MH3#P", "RFS-25V101MH5#"));
        }

        @Test
        void differentVoltagesAreNotReplacements() {
            // Same series but different voltage
            assertFalse(handler.isOfficialReplacement("RFS-25V101MH3#P", "RFS-50V101MH3#P"));
        }

        @Test
        void differentCapacitancesAreNotReplacements() {
            // Same series but different capacitance
            assertFalse(handler.isOfficialReplacement("RFS-25V101MH3#P", "RFS-25V102MH3#P"));
        }
    }

    @Nested
    @DisplayName("Voltage and Capacitance Parsing")
    class ParsingTests {
        @Test
        void shouldParseStandardVoltageNotation() {
            // Voltage is embedded in the MPN after the dash
            // The replacement logic uses voltage parsing internally
            assertTrue(handler.isOfficialReplacement("RFS-25V101MH3#P", "RFS-25V101MH5#"));
            assertFalse(handler.isOfficialReplacement("RFS-25V101MH3#P", "RFS-35V101MH3#P"));
        }

        @Test
        void shouldParseRNotationCapacitance() {
            // R notation like 4R7 = 4.7uF
            assertTrue(handler.isOfficialReplacement("ROA-50V4R7MF3#", "ROB-50V4R7MF5#"));
        }

        @Test
        void shouldParseEIACapacitanceCode() {
            // EIA code like 101 = 100uF (10 * 10^1)
            assertTrue(handler.isOfficialReplacement("RFS-25V101MH3#P", "RFS-25V101ML5#"));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldTests {
        @Test
        void shouldMatchRealSilmicIIPart() {
            // Real Elna Silmic II 100uF 25V audio capacitor
            assertTrue(handler.matches("RFS-25V101MH3#P", ComponentType.CAPACITOR, registry));
            assertEquals("Silmic II", handler.extractSeries("RFS-25V101MH3#P"));
        }

        @Test
        void shouldMatchRealTonerexPart() {
            // Real Elna TONEREX 4.7uF 50V audio capacitor
            assertTrue(handler.matches("ROA-50V4R7MF3#", ComponentType.CAPACITOR, registry));
            assertEquals("TONEREX Type A", handler.extractSeries("ROA-50V4R7MF3#"));
        }

        @Test
        void shouldMatchRealDynacapPart() {
            // Real Elna Dynacap 1F 5.5V supercapacitor
            assertTrue(handler.matches("DB-5R5D105T", ComponentType.CAPACITOR, registry));
            assertEquals("Dynacap Standard", handler.extractSeries("DB-5R5D105T"));
            assertEquals("Radial THT", handler.extractPackageCode("DB-5R5D105T"));
        }
    }

    @Nested
    @DisplayName("Pattern Registry Integration")
    class PatternRegistryTests {
        @Test
        void shouldRegisterPatternsForCapacitorType() {
            // Verify patterns are registered and work through registry
            assertTrue(registry.matches("RFS-25V101MH3#P", ComponentType.CAPACITOR));
        }

        @Test
        void shouldNotMatchOtherComponentTypes() {
            // Elna capacitor MPNs should not match non-CAPACITOR types
            assertFalse(handler.matches("RFS-25V101MH3#P", ComponentType.RESISTOR, registry));
            assertFalse(handler.matches("RFS-25V101MH3#P", ComponentType.INDUCTOR, registry));
            assertFalse(handler.matches("RFS-25V101MH3#P", ComponentType.LED, registry));
        }
    }

    @Nested
    @DisplayName("Manufacturer Component Types")
    class ManufacturerTypesTests {
        @Test
        void shouldReturnEmptyManufacturerTypes() {
            // Elna uses generic ComponentTypes, not manufacturer-specific
            assertTrue(handler.getManufacturerTypes().isEmpty());
        }
    }
}
