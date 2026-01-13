package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.DiodesIncHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for DiodesIncHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Diodes Incorporated components.
 */
class DiodesIncHandlerTest {

    private static DiodesIncHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new DiodesIncHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Diode Detection")
    class DiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N series general purpose diodes")
        @ValueSource(strings = {"1N4001", "1N4007", "1N4148", "1N5819"})
        void shouldDetect1NSeriesDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect BAV signal diodes")
        @ValueSource(strings = {"BAV19", "BAV20", "BAV21", "BAV99"})
        void shouldDetectBAVDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect BAS small signal diodes")
        @ValueSource(strings = {"BAS16", "BAS19", "BAS21", "BAS32"})
        void shouldDetectBASDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SBR Schottky rectifiers")
        @ValueSource(strings = {"SBR1040", "SBR1560", "SBR2060", "SBR30100"})
        void shouldDetectSBRDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect BZX Zener diodes")
        @ValueSource(strings = {"BZX79", "BZX84", "BZX55", "BZX85"})
        void shouldDetectBZXDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("MOSFET Detection")
    class MOSFETTests {

        @ParameterizedTest
        @DisplayName("Should detect DMN N-Channel MOSFETs")
        @ValueSource(strings = {"DMN2075U", "DMN3404L", "DMN6075S", "DMN10H220L"})
        void shouldDetectDMNMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect DMP P-Channel MOSFETs")
        @ValueSource(strings = {"DMP2035U", "DMP3099L", "DMP6023S", "DMP3085LK3"})
        void shouldDetectDMPMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect ZXMN N-Channel MOSFETs (ZETEX)")
        @ValueSource(strings = {"ZXMN2A01F", "ZXMN3A01F", "ZXMN6A07F", "ZXMN10A07F"})
        void shouldDetectZXMNMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect ZXMP P-Channel MOSFETs (ZETEX)")
        @ValueSource(strings = {"ZXMP2A17F", "ZXMP3A13F", "ZXMP6A17F", "ZXMP10A17F"})
        void shouldDetectZXMPMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("Transistor Detection")
    class TransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect MMBT small signal transistors")
        @ValueSource(strings = {"MMBT2222", "MMBT3904", "MMBT3906", "MMBT5551"})
        void shouldDetectMMBTTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect FMMT RF transistors")
        @ValueSource(strings = {"FMMT458", "FMMT489", "FMMT491", "FMMT617"})
        void shouldDetectFMMTTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect DTA digital transistors")
        @ValueSource(strings = {"DTA114E", "DTA123J", "DTA143E", "DTA144E"})
        void shouldDetectDTATransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("Voltage Regulator Detection")
    class VoltageRegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect AP series linear regulators")
        @ValueSource(strings = {"AP2112K", "AP2114H", "AP7333", "AP7361"})
        void shouldDetectAPRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect AZ series LDO regulators")
        @ValueSource(strings = {"AZ1117", "AZ1084", "AZ1085", "AZ1086"})
        void shouldDetectAZRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect PAM series DC-DC converters")
        @ValueSource(strings = {"PAM2301", "PAM2305", "PAM8302", "PAM8406"})
        void shouldDetectPAMDCDC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract MOSFET package codes")
        @CsvSource({
                "DMN2075U, SOT23",
                "DMN3404L, TO252/DPAK",
                "DMN6075T, SOT223"
        })
        void shouldExtractMOSFETPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should extract ZETEX style package codes")
        void shouldExtractZETEXPackageCodes() {
            String result = handler.extractPackageCode("ZXMN2A01F");
            assertFalse(result.isEmpty(), "Should extract package code for ZETEX MOSFET");
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
        @DisplayName("Should extract MOSFET series")
        @CsvSource({
                "DMN2075U, DMN Series",
                "DMP3085LK3, DMP Series",
                "ZXMN2A01F, ZXMN Series",
                "ZXMP3A13F, ZXMP Series"
        })
        void shouldExtractMOSFETSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract transistor series")
        @CsvSource({
                "MMBT2222, MMBT Series",
                "FMMT458, FMMT Series",
                "DTA114E, DTA Series"
        })
        void shouldExtractTransistorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract diode series")
        @CsvSource({
                "BAV19, BAV Series",
                "BAS16, BAS Series",
                "SBR1040, SBR Series",
                "BZX79, BZX Series"
        })
        void shouldExtractDiodeSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract regulator series")
        @CsvSource({
                "AP2112K, AP Series",
                "AZ1117, AZ Series",
                "PAM2301, PAM Series"
        })
        void shouldExtractRegulatorSeries(String mpn, String expectedSeries) {
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
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("BAV19 and BAV20 should be compatible replacements")
        void bavCompatibleReplacements() {
            assertTrue(handler.isOfficialReplacement("BAV19", "BAV20"),
                    "BAV19 and BAV20 should be compatible");
            assertTrue(handler.isOfficialReplacement("BAV20", "BAV19"),
                    "BAV20 and BAV19 should be compatible");
        }

        @Test
        @DisplayName("Handler only supports specific known compatible pairs")
        void handlerSupportsSpecificPairs() {
            // The handler only returns true for specific known compatible pairs
            // BAV19 and BAV20 are explicitly defined as compatible
            // General same-series parts are NOT automatically considered replacements
            assertFalse(handler.isOfficialReplacement("DMN2075U", "DMN3404L"),
                    "Generic same series parts are not automatically replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("DMN2075U", "DMP2035U"),
                    "N-channel and P-channel should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("BAV19", "BAS16"),
                    "Different diode series should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "DMN2075U"));
            assertFalse(handler.isOfficialReplacement("DMN2075U", null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MOSFET));
            assertTrue(types.contains(ComponentType.MOSFET_DIODES));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR_DIODES));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.DIODE, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.DIODE, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("DMN2075U", null, registry));
        }
    }
}
