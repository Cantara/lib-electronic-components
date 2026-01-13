package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.PanjitHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for PanjitHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Panjit International is a Taiwan semiconductor manufacturer producing:
 * - Standard rectifier diodes (1N4xxx, 1N5xxx)
 * - Fast recovery diodes (ES/RS series)
 * - Schottky diodes (SS/SK series)
 * - Signal diodes (BAV/BAS/BAT series)
 * - SMD transistors (MMBT series)
 * - Through-hole transistors (2N series)
 * - MOSFETs (PJ series)
 */
class PanjitHandlerTest {

    private static PanjitHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new PanjitHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Standard Rectifier Diodes (1N4xxx series)")
    class StandardRectifierDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N400x rectifier diodes")
        @ValueSource(strings = {"1N4001G", "1N4004G", "1N4007G", "1N4007W", "1N4001"})
        void shouldDetect1N400xDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from 1N400x series")
        @CsvSource({
                "1N4001G, DO-41",
                "1N4007G, DO-41",
                "1N4007W, SOD-123",
                "1N4007GP, DO-41"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should extract 1N4000 series name")
        void shouldExtractSeries() {
            assertEquals("1N4000", handler.extractSeries("1N4007G"),
                    "Series for 1N4007G");
            assertEquals("1N4000", handler.extractSeries("1N4001W"),
                    "Series for 1N4001W");
        }
    }

    @Nested
    @DisplayName("Signal Diodes (1N4148/1N914)")
    class SignalDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N4148 signal diodes")
        @ValueSource(strings = {"1N4148W", "1N4148WS", "1N4148"})
        void shouldDetect1N4148Diodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 1N914 signal diodes")
        @ValueSource(strings = {"1N914", "1N914W"})
        void shouldDetect1N914Diodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @Test
        @DisplayName("Should extract series for signal diodes")
        void shouldExtractSignalDiodeSeries() {
            assertEquals("1N4148", handler.extractSeries("1N4148W"),
                    "Series for 1N4148W");
            assertEquals("1N914", handler.extractSeries("1N914"),
                    "Series for 1N914");
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from signal diodes")
        @CsvSource({
                "1N4148W, SOD-123",
                "1N4148WS, SOD-323"
        })
        void shouldExtractSignalDiodePackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Power Rectifier Diodes (1N5xxx series)")
    class PowerRectifierDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N54xx power rectifiers")
        @ValueSource(strings = {"1N5400G", "1N5401G", "1N5408G", "1N5408"})
        void shouldDetect1N54xxDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 1N58xx Schottky diodes")
        @ValueSource(strings = {"1N5819", "1N5820", "1N5822"})
        void shouldDetect1N58xxDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @Test
        @DisplayName("Should extract series for power rectifiers")
        void shouldExtractPowerRectifierSeries() {
            assertEquals("1N5400", handler.extractSeries("1N5408G"),
                    "Series for 1N5408G");
            assertEquals("1N5800", handler.extractSeries("1N5819"),
                    "Series for 1N5819");
        }
    }

    @Nested
    @DisplayName("Fast Recovery Diodes (ES/RS series)")
    class FastRecoveryDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect ES series fast recovery diodes")
        @ValueSource(strings = {"ES1J", "ES2J", "ES1D", "ES1M"})
        void shouldDetectESDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect RS series fast recovery diodes")
        @ValueSource(strings = {"RS1M", "RS2M", "RS1G", "RS2G"})
        void shouldDetectRSDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @Test
        @DisplayName("Should extract series for fast recovery diodes")
        void shouldExtractFastRecoverySeries() {
            assertEquals("ES", handler.extractSeries("ES1J"),
                    "Series for ES1J");
            assertEquals("RS", handler.extractSeries("RS2M"),
                    "Series for RS2M");
        }

        @Test
        @DisplayName("Should extract package code for fast recovery diodes")
        void shouldExtractFastRecoveryPackage() {
            assertEquals("SMA", handler.extractPackageCode("ES1J"),
                    "Package code for ES1J");
            assertEquals("SMA", handler.extractPackageCode("RS2M"),
                    "Package code for RS2M");
        }
    }

    @Nested
    @DisplayName("Schottky Diodes (SS/SK series)")
    class SchottkyDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect SS series Schottky diodes")
        @ValueSource(strings = {"SS12", "SS14", "SS24", "SS34", "SS16"})
        void shouldDetectSSDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SK series Schottky diodes")
        @ValueSource(strings = {"SK52", "SK54", "SK56", "SK34"})
        void shouldDetectSKDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @Test
        @DisplayName("Should extract series for Schottky diodes")
        void shouldExtractSchottkySeries() {
            assertEquals("SS", handler.extractSeries("SS14"),
                    "Series for SS14");
            assertEquals("SK", handler.extractSeries("SK54"),
                    "Series for SK54");
        }

        @Test
        @DisplayName("Should extract package code for Schottky diodes")
        void shouldExtractSchottkyPackage() {
            assertEquals("DO-214AC", handler.extractPackageCode("SS14"),
                    "Package code for SS14");
        }
    }

    @Nested
    @DisplayName("Signal Diodes (BAV/BAS/BAT series)")
    class BAVSignalDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect BAV signal diodes")
        @ValueSource(strings = {"BAV99", "BAV70", "BAV21"})
        void shouldDetectBAVDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect BAS signal diodes")
        @ValueSource(strings = {"BAS16", "BAS21"})
        void shouldDetectBASDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect BAT Schottky diodes")
        @ValueSource(strings = {"BAT54", "BAT54S", "BAT54C", "BAT46"})
        void shouldDetectBATDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @Test
        @DisplayName("Should extract series for BAV/BAS/BAT diodes")
        void shouldExtractBAVSeries() {
            assertEquals("BAV", handler.extractSeries("BAV99"),
                    "Series for BAV99");
            assertEquals("BAS", handler.extractSeries("BAS16"),
                    "Series for BAS16");
            assertEquals("BAT", handler.extractSeries("BAT54"),
                    "Series for BAT54");
        }
    }

    @Nested
    @DisplayName("SMD Transistors (MMBT series)")
    class MMBTTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect MMBT transistors")
        @ValueSource(strings = {"MMBT2222A", "MMBT3904", "MMBT3906", "MMBT2907A"})
        void shouldDetectMMBTTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @Test
        @DisplayName("Should extract MMBT series")
        void shouldExtractMMBTSeries() {
            assertEquals("MMBT", handler.extractSeries("MMBT2222A"),
                    "Series for MMBT2222A");
            assertEquals("MMBT", handler.extractSeries("MMBT3904"),
                    "Series for MMBT3904");
        }

        @Test
        @DisplayName("Should extract SOT-23 package for MMBT")
        void shouldExtractMMBTPackage() {
            assertEquals("SOT-23", handler.extractPackageCode("MMBT2222A"),
                    "Package code for MMBT2222A");
        }
    }

    @Nested
    @DisplayName("2N Series Transistors")
    class TwoNTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect 2N transistors (not 2N7002)")
        @ValueSource(strings = {"2N2222A", "2N3904", "2N3906", "2N4401"})
        void shouldDetect2NTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @Test
        @DisplayName("2N7002 should NOT match as transistor (it is a MOSFET)")
        void shouldNot2N7002MatchTransistor() {
            assertFalse(handler.matches("2N7002", ComponentType.TRANSISTOR, registry),
                    "2N7002 should NOT match TRANSISTOR (it is a MOSFET)");
        }

        @Test
        @DisplayName("Should extract 2N series")
        void shouldExtract2NSeries() {
            assertEquals("2N", handler.extractSeries("2N2222A"),
                    "Series for 2N2222A");
            assertEquals("2N", handler.extractSeries("2N3904"),
                    "Series for 2N3904");
        }
    }

    @Nested
    @DisplayName("MOSFETs")
    class MOSFETTests {

        @Test
        @DisplayName("2N7002 should match as MOSFET")
        void shouldDetect2N7002AsMosfet() {
            assertTrue(handler.matches("2N7002", ComponentType.MOSFET, registry),
                    "2N7002 should match MOSFET");
            assertTrue(handler.matches("2N7002K", ComponentType.MOSFET, registry),
                    "2N7002K should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect BSS MOSFETs")
        @ValueSource(strings = {"BSS138", "BSS84", "BSS123"})
        void shouldDetectBSSMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect PJ series MOSFETs")
        @ValueSource(strings = {"PJ2308", "PJ3415", "PJ4435"})
        void shouldDetectPJMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @Test
        @DisplayName("Should extract MOSFET series")
        void shouldExtractMosfetSeries() {
            assertEquals("BSS", handler.extractSeries("BSS138"),
                    "Series for BSS138");
            assertEquals("PJ", handler.extractSeries("PJ2308"),
                    "Series for PJ2308");
        }

        @Test
        @DisplayName("Should extract package for BSS MOSFETs")
        void shouldExtractBSSPackage() {
            assertEquals("SOT-23", handler.extractPackageCode("BSS138"),
                    "Package code for BSS138");
        }

        @Test
        @DisplayName("Should extract package for 2N7002")
        void shouldExtract2N7002Package() {
            assertEquals("SOT-23", handler.extractPackageCode("2N7002"),
                    "Package code for 2N7002");
        }
    }

    @Nested
    @DisplayName("Other Transistor Series")
    class OtherTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect MPSA transistors")
        @ValueSource(strings = {"MPSA42", "MPSA92", "MPSA06"})
        void shouldDetectMPSATransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect BC series transistors")
        @ValueSource(strings = {"BC847", "BC857", "BC817"})
        void shouldDetectBCTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @Test
        @DisplayName("Should extract series for other transistors")
        void shouldExtractOtherTransistorSeries() {
            assertEquals("MPSA", handler.extractSeries("MPSA42"),
                    "Series for MPSA42");
            assertEquals("BC", handler.extractSeries("BC847"),
                    "Series for BC847");
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Higher voltage 1N400x should replace lower")
        void higherVoltageReplacesLower() {
            assertTrue(handler.isOfficialReplacement("1N4007G", "1N4001G"),
                    "1N4007 (1000V) should replace 1N4001 (50V)");
            assertTrue(handler.isOfficialReplacement("1N4004G", "1N4002G"),
                    "1N4004 (400V) should replace 1N4002 (100V)");
        }

        @Test
        @DisplayName("Lower voltage 1N400x should NOT replace higher")
        void lowerVoltageNotReplacesHigher() {
            assertFalse(handler.isOfficialReplacement("1N4001G", "1N4007G"),
                    "1N4001 (50V) should NOT replace 1N4007 (1000V)");
        }

        @Test
        @DisplayName("1N4148 and 1N914 should be replacements for each other")
        void signalDiodesAreEquivalent() {
            assertTrue(handler.isOfficialReplacement("1N4148W", "1N914"),
                    "1N4148 should replace 1N914");
            assertTrue(handler.isOfficialReplacement("1N914", "1N4148W"),
                    "1N914 should replace 1N4148");
        }

        @Test
        @DisplayName("Higher current Schottky should replace lower")
        void higherCurrentSchottkyReplacesLower() {
            assertTrue(handler.isOfficialReplacement("SS34", "SS14"),
                    "SS34 (3A) should replace SS14 (1A)");
        }

        @Test
        @DisplayName("MMBT2222A should be replacement for itself")
        void sameTransistorIsReplacement() {
            assertTrue(handler.isOfficialReplacement("MMBT2222A", "MMBT2222A"),
                    "MMBT2222A should replace MMBT2222A");
        }

        @Test
        @DisplayName("Different transistor series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("MMBT3904", "MMBT3906"),
                    "MMBT3904 (NPN) should NOT replace MMBT3906 (PNP)");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.DIODE),
                    "Should support DIODE");
            assertTrue(types.contains(ComponentType.TRANSISTOR),
                    "Should support TRANSISTOR");
            assertTrue(types.contains(ComponentType.MOSFET),
                    "Should support MOSFET");

            assertEquals(3, types.size(),
                    "Should support exactly 3 types");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of (immutable)")
        void supportedTypesIsImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes should return immutable set");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.DIODE, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "1N4007"));
            assertFalse(handler.isOfficialReplacement("1N4007", null));
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
            assertFalse(handler.matches("1N4007G", null, registry));
        }

        @Test
        @DisplayName("Should handle case-insensitive matching")
        void shouldHandleCaseInsensitive() {
            assertTrue(handler.matches("1n4007g", ComponentType.DIODE, registry),
                    "Should match lowercase MPN");
            assertTrue(handler.matches("Mmbt2222a", ComponentType.TRANSISTOR, registry),
                    "Should match mixed case MPN");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            PanjitHandler directHandler = new PanjitHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("1N4007G", ComponentType.DIODE, directRegistry));
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }

    @Nested
    @DisplayName("Comprehensive Series Extraction")
    class ComprehensiveSeriesTests {

        @ParameterizedTest
        @DisplayName("Should extract correct series from various MPNs")
        @CsvSource({
                // Rectifiers
                "1N4001G, 1N4000",
                "1N4007W, 1N4000",
                "1N5408G, 1N5400",
                "1N5819, 1N5800",
                // Signal
                "1N4148W, 1N4148",
                "1N914, 1N914",
                // Fast recovery
                "ES1J, ES",
                "RS2M, RS",
                // Schottky
                "SS14, SS",
                "SK54, SK",
                "SB140, SB",
                "MBR340, MBR",
                // BAV/BAS/BAT
                "BAV99, BAV",
                "BAS16, BAS",
                "BAT54, BAT",
                // Zener
                "BZX84C5V1, BZX",
                // Transistors
                "MMBT2222A, MMBT",
                "MPSA42, MPSA",
                "2N3904, 2N",
                "BC847, BC",
                // MOSFETs
                "BSS138, BSS",
                "PJ2308, PJ"
        })
        void shouldExtractCorrectSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Comprehensive Package Extraction")
    class ComprehensivePackageTests {

        @ParameterizedTest
        @DisplayName("Should extract correct package from various MPNs")
        @CsvSource({
                "1N4007G, DO-41",
                "1N4007W, SOD-123",
                "1N4148W, SOD-123",
                "1N4148WS, SOD-323",
                "MMBT2222A, SOT-23",
                "BSS138, SOT-23",
                "2N7002, SOT-23",
                "BC847, SOT-23"
        })
        void shouldExtractCorrectPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Zener and TVS Diodes")
    class ZenerTVSDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect BZX Zener diodes")
        @ValueSource(strings = {"BZX84C5V1", "BZX55C3V3", "BZX79C6V2"})
        void shouldDetectBZXZenerDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @Test
        @DisplayName("Should extract BZX series")
        void shouldExtractBZXSeries() {
            assertEquals("BZX", handler.extractSeries("BZX84C5V1"),
                    "Series for BZX84C5V1");
        }
    }
}
