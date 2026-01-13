package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.GoodArkHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for GoodArkHandler.
 * Good-Ark Semiconductor is a Chinese manufacturer specializing in diodes and transistors.
 *
 * Tests cover:
 * - Pattern matching for diodes and transistors
 * - Package code extraction
 * - Series extraction
 * - Official replacement detection
 * - Edge cases and null handling
 */
class GoodArkHandlerTest {

    private static GoodArkHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new GoodArkHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // DIODE DETECTION TESTS
    // ========================================================================

    @Nested
    @DisplayName("1N400x Rectifier Diode Detection")
    class Rectifier1N400xTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N400x standard rectifiers")
        @ValueSource(strings = {
                "1N4001", "1N4002", "1N4003", "1N4004",
                "1N4005", "1N4006", "1N4007",
                "1N4007G", "1N4007-TP", "1N4007RL"
        })
        void shouldDetect1N400xRectifiers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @Test
        @DisplayName("Should NOT match invalid 1N400x numbers")
        void shouldNotMatchInvalid1N400x() {
            assertFalse(handler.matches("1N4000", ComponentType.DIODE, registry),
                    "1N4000 is not a valid part");
            assertFalse(handler.matches("1N4008", ComponentType.DIODE, registry),
                    "1N4008 is not a valid part");
            assertFalse(handler.matches("1N4009", ComponentType.DIODE, registry),
                    "1N4009 is not a valid part");
        }
    }

    @Nested
    @DisplayName("Signal Diode Detection")
    class SignalDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N4148 signal diodes")
        @ValueSource(strings = {
                "1N4148", "1N4148W", "1N4148WT",
                "1N4148-TR", "1N4148W-7-F"
        })
        void shouldDetect1N4148(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 1N4448 high-speed signal diodes")
        @ValueSource(strings = {"1N4448", "1N4448W", "1N4448-TR"})
        void shouldDetect1N4448(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 1N914 signal diodes")
        @ValueSource(strings = {"1N914", "1N914A", "1N914B", "1N914-TR"})
        void shouldDetect1N914(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("Zener Diode Detection")
    class ZenerDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N47xx Zener diodes")
        @ValueSource(strings = {
                "1N4728", "1N4733", "1N4742", "1N4748",
                "1N4728A", "1N4733-TP", "1N4742-TR"
        })
        void shouldDetect1N47xxZeners(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("1N54xx Power Rectifier Detection")
    class PowerRectifierTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N540x power rectifiers (3A)")
        @ValueSource(strings = {
                "1N5400", "1N5401", "1N5402", "1N5404",
                "1N5406", "1N5408", "1N5408-TP"
        })
        void shouldDetect1N540x(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 1N58xx Schottky diodes")
        @ValueSource(strings = {
                "1N5817", "1N5818", "1N5819",
                "1N5820", "1N5821", "1N5822"
        })
        void shouldDetect1N58xx(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("Fast Recovery Diode Detection")
    class FastRecoveryTests {

        @ParameterizedTest
        @DisplayName("Should detect ES series fast recovery diodes")
        @ValueSource(strings = {"ES1J", "ES1D", "ES1G", "ES2D", "ES2J", "ES2G"})
        void shouldDetectESSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect US series ultra-fast diodes")
        @ValueSource(strings = {"US1M", "US1G", "US1J", "US2M", "US2G", "US2J"})
        void shouldDetectUSSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect UF series ultra-fast diodes")
        @ValueSource(strings = {"UF4001", "UF4002", "UF4004", "UF4007"})
        void shouldDetectUFSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("Schottky Diode Detection")
    class SchottkyDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect SS series Schottky diodes")
        @ValueSource(strings = {
                "SS12", "SS14", "SS16", "SS18",
                "SS22", "SS24", "SS26",
                "SS110", "SS120"
        })
        void shouldDetectSSSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SK series Schottky diodes")
        @ValueSource(strings = {
                "SK34", "SK36", "SK38",
                "SK54", "SK56", "SK58",
                "SK34A", "SK56-TP"
        })
        void shouldDetectSKSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SB series Schottky barrier diodes")
        @ValueSource(strings = {
                "SB140", "SB150", "SB160",
                "SB260", "SB360", "SB540"
        })
        void shouldDetectSBSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("BAV/BAT Series Diode Detection")
    class BAVBATSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect BAV series signal diodes")
        @ValueSource(strings = {
                "BAV21", "BAV70", "BAV99",
                "BAV21W", "BAV70-7-F"
        })
        void shouldDetectBAVSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect BAT series Schottky diodes")
        @ValueSource(strings = {
                "BAT54", "BAT54S", "BAT54C",
                "BAT85", "BAT46", "BAT42"
        })
        void shouldDetectBATSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("TVS Diode Detection")
    class TVSDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect SMBJ TVS diodes")
        @ValueSource(strings = {"SMBJ5.0", "SMBJ6.5", "SMBJ12", "SMBJ24"})
        void shouldDetectSMBJ(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SMAJ TVS diodes")
        @ValueSource(strings = {"SMAJ5.0", "SMAJ12", "SMAJ24", "SMAJ33"})
        void shouldDetectSMAJ(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect P4KE/P6KE TVS diodes")
        @ValueSource(strings = {"P4KE6.8", "P4KE15", "P6KE6.8", "P6KE15"})
        void shouldDetectPxKE(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("Bridge Rectifier Detection")
    class BridgeRectifierTests {

        @ParameterizedTest
        @DisplayName("Should detect MB series bridges")
        @ValueSource(strings = {"MB1S", "MB2S", "MB4S", "MB6S"})
        void shouldDetectMBSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect DB series dual bridges")
        @ValueSource(strings = {"DB1S", "DB2S", "DB4S", "DB6S"})
        void shouldDetectDBSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    // ========================================================================
    // TRANSISTOR DETECTION TESTS
    // ========================================================================

    @Nested
    @DisplayName("2N Series Transistor Detection")
    class Transistor2NTests {

        @ParameterizedTest
        @DisplayName("Should detect 2N NPN transistors")
        @ValueSource(strings = {
                "2N2222", "2N2222A",
                "2N3904", "2N3904-TP",
                "2N4401", "2N5551"
        })
        void shouldDetect2NNPN(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect 2N PNP transistors")
        @ValueSource(strings = {
                "2N2907", "2N2907A",
                "2N3906", "2N3906-TP",
                "2N4403", "2N5401"
        })
        void shouldDetect2NPNP(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect 2N706 switching transistor")
        @ValueSource(strings = {"2N706", "2N706A", "2N706B"})
        void shouldDetect2N706(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("MMBT Series SMD Transistor Detection")
    class MMBTTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect MMBT NPN transistors")
        @ValueSource(strings = {
                "MMBT2222", "MMBT2222A", "MMBT2222ALT1",
                "MMBT3904", "MMBT3904LT1",
                "MMBT4401", "MMBT5551"
        })
        void shouldDetectMMBTNPN(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect MMBT PNP transistors")
        @ValueSource(strings = {
                "MMBT2907", "MMBT2907A",
                "MMBT3906", "MMBT3906LT1",
                "MMBT4403", "MMBT5401"
        })
        void shouldDetectMMBTPNP(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("MMBTA Series Transistor Detection")
    class MMBTATransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect MMBTA transistors")
        @ValueSource(strings = {
                "MMBTA42", "MMBTA42LT1",
                "MMBTA92", "MMBTA13", "MMBTA14"
        })
        void shouldDetectMMBTA(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("PN Series Transistor Detection")
    class PNTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect PN series transistors")
        @ValueSource(strings = {
                "PN2222", "PN2222A",
                "PN2907", "PN2907A"
        })
        void shouldDetectPNSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("MPSA Series Transistor Detection")
    class MPSATransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect MPSA transistors")
        @ValueSource(strings = {
                "MPSA42", "MPSA42-TP",
                "MPSA92", "MPSA13", "MPSA14"
        })
        void shouldDetectMPSA(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("BC Series European Transistor Detection")
    class BCTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect BC series NPN transistors")
        @ValueSource(strings = {
                "BC547", "BC547B", "BC547C",
                "BC548", "BC549", "BC337"
        })
        void shouldDetectBCNPN(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect BC series PNP transistors")
        @ValueSource(strings = {
                "BC557", "BC557B", "BC557C",
                "BC558", "BC559", "BC327"
        })
        void shouldDetectBCPNP(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("BF Series High Frequency Transistor Detection")
    class BFTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect BF series transistors")
        @ValueSource(strings = {"BF245", "BF199", "BF494"})
        void shouldDetectBFSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    @Nested
    @DisplayName("S80xx/S90xx Asia Series Transistor Detection")
    class AsiaSeriesTransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect S8050/S8550 transistors")
        @ValueSource(strings = {"S8050", "S8050D", "S8550", "S8550D"})
        void shouldDetectS80xx(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect S90xx transistors")
        @ValueSource(strings = {
                "S9012", "S9012H", "S9013", "S9013H",
                "S9014", "S9015", "S9018"
        })
        void shouldDetectS90xx(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }
    }

    // ========================================================================
    // PACKAGE CODE EXTRACTION TESTS
    // ========================================================================

    @Nested
    @DisplayName("Diode Package Code Extraction")
    class DiodePackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract 1N400x package codes")
        @CsvSource({
                "1N4007, DO-41",
                "1N4007G, DO-41",
                "1N4007RL, DO-201AD"
        })
        void shouldExtract1N400xPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract signal diode package codes")
        @CsvSource({
                "1N4148, DO-35",
                "1N4148W, SOD-123",
                "1N4148WT, SOD-523",
                "1N914, DO-35"
        })
        void shouldExtractSignalDiodePackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract power rectifier package codes")
        @CsvSource({
                "1N5400, DO-201AD",
                "1N5408, DO-201AD",
                "1N5819, DO-41"
        })
        void shouldExtractPowerRectifierPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract fast recovery diode packages")
        @CsvSource({
                "ES1J, SMA",
                "US1M, SMA",
                "ES2D, SMA"
        })
        void shouldExtractFastRecoveryPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Schottky diode packages")
        @CsvSource({
                "SS14, SMA",
                "SK34, SMB",
                "SB140, DO-41"
        })
        void shouldExtractSchottkyPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract BAV/BAT package codes")
        @CsvSource({
                "BAV70, SOT-23",
                "BAV99, SOT-23",
                "BAV21, SOD-323",
                "BAT54, SOT-23"
        })
        void shouldExtractBAVBATPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TVS diode packages")
        @CsvSource({
                "SMBJ5.0, SMB",
                "SMAJ12, SMA",
                "P4KE15, DO-41",
                "P6KE6.8, DO-41"
        })
        void shouldExtractTVSPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }
    }

    @Nested
    @DisplayName("Transistor Package Code Extraction")
    class TransistorPackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract 2N series package codes")
        @CsvSource({
                "2N2222, TO-92",
                "2N2222A, TO-92",
                "2N3904, TO-92",
                "2N3906, TO-92"
        })
        void shouldExtract2NPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MMBT series package codes")
        @CsvSource({
                "MMBT2222, SOT-23",
                "MMBT2222ALT1, SOT-23",
                "MMBT3904, SOT-23",
                "MMBT3904G3, SOT-323",
                "MMBT3904F5, SOT-23-5"
        })
        void shouldExtractMMBTPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract PN series package codes")
        @CsvSource({
                "PN2222, TO-92",
                "PN2907, TO-92"
        })
        void shouldExtractPNPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract BC series package codes")
        @CsvSource({
                "BC547, TO-92",
                "BC547B, TO-92",
                "BC547SMD, SOT-23"
        })
        void shouldExtractBCPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract S80xx/S90xx package codes")
        @CsvSource({
                "S8050, TO-92",
                "S8050SMD, SOT-23",
                "S9012, TO-92"
        })
        void shouldExtractAsiaSeriesPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }
    }

    // ========================================================================
    // SERIES EXTRACTION TESTS
    // ========================================================================

    @Nested
    @DisplayName("Diode Series Extraction")
    class DiodeSeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract 1N400x rectifier series")
        @CsvSource({
                "1N4001, 1N400x",
                "1N4007, 1N400x",
                "1N4007G, 1N400x"
        })
        void shouldExtract1N400xSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract signal diode series")
        @CsvSource({
                "1N4148, 1N4148",
                "1N4148W, 1N4148",
                "1N4448, 1N4448",
                "1N914, 1N914"
        })
        void shouldExtractSignalDiodeSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Zener series")
        @CsvSource({
                "1N4728, 1N47xx",
                "1N4733, 1N47xx",
                "1N4748A, 1N47xx"
        })
        void shouldExtractZenerSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract power rectifier series")
        @CsvSource({
                "1N5400, 1N540x",
                "1N5408, 1N540x",
                "1N5817, 1N5817",
                "1N5822, 1N5820"
        })
        void shouldExtractPowerRectifierSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract fast recovery series")
        @CsvSource({
                "ES1J, ES1",
                "ES2D, ES2",
                "US1M, US1",
                "US2G, US2"
        })
        void shouldExtractFastRecoverySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Schottky series")
        @CsvSource({
                "SS14, SS14",
                "SS34, SS34",
                "SK34, SK34",
                "SK56, SK56",
                "SB140, SB140",
                "SB360, SB360"
        })
        void shouldExtractSchottkySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract BAV/BAT series")
        @CsvSource({
                "BAV21, BAV21",
                "BAV70, BAV70",
                "BAV99, BAV99",
                "BAT54, BAT54",
                "BAT85, BAT85"
        })
        void shouldExtractBAVBATSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TVS series")
        @CsvSource({
                "SMBJ5.0, SMBJ",
                "SMAJ12, SMAJ",
                "P4KE15, P4KE",
                "P6KE6.8, P6KE"
        })
        void shouldExtractTVSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Transistor Series Extraction")
    class TransistorSeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract 2N series")
        @CsvSource({
                "2N2222, 2N2222",
                "2N2222A, 2N2222",
                "2N3904, 2N3904",
                "2N3906, 2N3906",
                "2N706, 2N706"
        })
        void shouldExtract2NSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MMBT series")
        @CsvSource({
                "MMBT2222, MMBT2222",
                "MMBT2222A, MMBT2222",
                "MMBT3904, MMBT3904",
                "MMBT3906LT1, MMBT3906"
        })
        void shouldExtractMMBTSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MMBTA series")
        @CsvSource({
                "MMBTA42, MMBTA42",
                "MMBTA92, MMBTA92"
        })
        void shouldExtractMMBTASeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract PN series")
        @CsvSource({
                "PN2222, PN2222",
                "PN2907A, PN2907"
        })
        void shouldExtractPNSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MPSA series")
        @CsvSource({
                "MPSA42, MPSA42",
                "MPSA92, MPSA92"
        })
        void shouldExtractMPSASeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract BC series")
        @CsvSource({
                "BC547, BC547",
                "BC547B, BC547",
                "BC557, BC557"
        })
        void shouldExtractBCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Asia series")
        @CsvSource({
                "S8050, S8050",
                "S8550, S8550",
                "S9012, S9012",
                "S9013, S9013"
        })
        void shouldExtractAsiaSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    // ========================================================================
    // OFFICIAL REPLACEMENT TESTS
    // ========================================================================

    @Nested
    @DisplayName("1N400x Voltage Replacement Rules")
    class Voltage1N400xReplacementTests {

        @Test
        @DisplayName("Higher voltage 1N400x should replace lower")
        void higherVoltageReplacesLower() {
            assertTrue(handler.isOfficialReplacement("1N4007", "1N4001"),
                    "1N4007 (1000V) should replace 1N4001 (50V)");
            assertTrue(handler.isOfficialReplacement("1N4007", "1N4004"),
                    "1N4007 (1000V) should replace 1N4004 (400V)");
            assertTrue(handler.isOfficialReplacement("1N4004", "1N4001"),
                    "1N4004 (400V) should replace 1N4001 (50V)");
        }

        @Test
        @DisplayName("Lower voltage should NOT replace higher")
        void lowerVoltageNotReplacesHigher() {
            assertFalse(handler.isOfficialReplacement("1N4001", "1N4007"),
                    "1N4001 (50V) should NOT replace 1N4007 (1000V)");
            assertFalse(handler.isOfficialReplacement("1N4004", "1N4007"),
                    "1N4004 (400V) should NOT replace 1N4007 (1000V)");
        }

        @Test
        @DisplayName("Same voltage is compatible")
        void sameVoltageIsCompatible() {
            assertTrue(handler.isOfficialReplacement("1N4007", "1N4007"),
                    "Same part should be compatible");
            assertTrue(handler.isOfficialReplacement("1N4007G", "1N4007"),
                    "Package variant should be compatible");
        }
    }

    @Nested
    @DisplayName("1N540x Voltage Replacement Rules")
    class Voltage1N540xReplacementTests {

        @Test
        @DisplayName("Higher voltage 1N540x should replace lower")
        void higherVoltageReplacesLower() {
            assertTrue(handler.isOfficialReplacement("1N5408", "1N5400"),
                    "1N5408 (800V) should replace 1N5400 (50V)");
            assertTrue(handler.isOfficialReplacement("1N5408", "1N5404"),
                    "1N5408 (800V) should replace 1N5404 (400V)");
        }

        @Test
        @DisplayName("Lower voltage should NOT replace higher")
        void lowerVoltageNotReplacesHigher() {
            assertFalse(handler.isOfficialReplacement("1N5400", "1N5408"),
                    "1N5400 (50V) should NOT replace 1N5408 (800V)");
        }
    }

    @Nested
    @DisplayName("Transistor Equivalent Replacement Rules")
    class TransistorEquivalentTests {

        @Test
        @DisplayName("MMBT should replace 2N equivalent")
        void mmbtReplaces2N() {
            assertTrue(handler.isOfficialReplacement("MMBT2222", "2N2222"),
                    "MMBT2222 (SMD) should replace 2N2222 (THT)");
            assertTrue(handler.isOfficialReplacement("MMBT3904", "2N3904"),
                    "MMBT3904 should replace 2N3904");
            assertTrue(handler.isOfficialReplacement("MMBT3906", "2N3906"),
                    "MMBT3906 should replace 2N3906");
        }

        @Test
        @DisplayName("2N should replace MMBT equivalent")
        void twoNReplacesMMBT() {
            assertTrue(handler.isOfficialReplacement("2N2222", "MMBT2222"),
                    "2N2222 should replace MMBT2222");
            assertTrue(handler.isOfficialReplacement("2N3904", "MMBT3904"),
                    "2N3904 should replace MMBT3904");
        }

        @Test
        @DisplayName("PN should replace 2N equivalent")
        void pnReplaces2N() {
            assertTrue(handler.isOfficialReplacement("PN2222", "2N2222"),
                    "PN2222 should replace 2N2222");
            assertTrue(handler.isOfficialReplacement("PN2907", "2N2907"),
                    "PN2907 should replace 2N2907");
        }

        @Test
        @DisplayName("Different transistor numbers should NOT be compatible")
        void differentNumbersNotCompatible() {
            assertFalse(handler.isOfficialReplacement("2N2222", "2N3904"),
                    "2N2222 and 2N3904 are different transistors");
            assertFalse(handler.isOfficialReplacement("MMBT2222", "MMBT3904"),
                    "MMBT2222 and MMBT3904 are different transistors");
        }
    }

    @Nested
    @DisplayName("Signal Diode Equivalent Rules")
    class SignalDiodeEquivalentTests {

        @Test
        @DisplayName("1N4148 and 1N914 are interchangeable")
        void signalDiodesInterchangeable() {
            assertTrue(handler.isOfficialReplacement("1N4148", "1N914"),
                    "1N4148 should replace 1N914");
            assertTrue(handler.isOfficialReplacement("1N914", "1N4148"),
                    "1N914 should replace 1N4148");
        }
    }

    @Nested
    @DisplayName("SS Series Current Replacement Rules")
    class SSSeriesReplacementTests {

        @Test
        @DisplayName("Higher current SS should replace lower")
        void higherCurrentReplacesLower() {
            assertTrue(handler.isOfficialReplacement("SS34", "SS14"),
                    "SS34 (3A) should replace SS14 (1A)");
            assertTrue(handler.isOfficialReplacement("SS54", "SS34"),
                    "SS54 (5A) should replace SS34 (3A)");
        }

        @Test
        @DisplayName("Lower current should NOT replace higher")
        void lowerCurrentNotReplacesHigher() {
            assertFalse(handler.isOfficialReplacement("SS14", "SS34"),
                    "SS14 (1A) should NOT replace SS34 (3A)");
        }
    }

    @Nested
    @DisplayName("Same Series Package Variants")
    class SameSeriesVariantTests {

        @Test
        @DisplayName("Same series different packages should be compatible")
        void sameSeriesDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("BAT54", "BAT54S"),
                    "BAT54 variants should be compatible");
            assertTrue(handler.isOfficialReplacement("ES1J", "ES1D"),
                    "ES1x variants should be compatible");
        }
    }

    // ========================================================================
    // SUPPORTED TYPES TESTS
    // ========================================================================

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support DIODE type")
        void shouldSupportDiode() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.DIODE),
                    "Should support DIODE");
        }

        @Test
        @DisplayName("Should support TRANSISTOR type")
        void shouldSupportTransistor() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.TRANSISTOR),
                    "Should support TRANSISTOR");
        }

        @Test
        @DisplayName("Should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes() should return immutable set");
        }

        @Test
        @DisplayName("Should have exactly 2 supported types")
        void shouldHaveTwoTypes() {
            assertEquals(2, handler.getSupportedTypes().size(),
                    "Should support exactly 2 types (DIODE, TRANSISTOR)");
        }
    }

    // ========================================================================
    // EDGE CASE AND NULL HANDLING TESTS
    // ========================================================================

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN in matches")
        void shouldHandleNullMpnInMatches() {
            assertFalse(handler.matches(null, ComponentType.DIODE, registry),
                    "matches() should return false for null MPN");
        }

        @Test
        @DisplayName("Should handle null type in matches")
        void shouldHandleNullTypeInMatches() {
            assertFalse(handler.matches("1N4007", null, registry),
                    "matches() should return false for null type");
        }

        @Test
        @DisplayName("Should handle null MPN in extractPackageCode")
        void shouldHandleNullMpnInExtractPackageCode() {
            assertEquals("", handler.extractPackageCode(null),
                    "extractPackageCode() should return empty string for null");
        }

        @Test
        @DisplayName("Should handle empty MPN in extractPackageCode")
        void shouldHandleEmptyMpnInExtractPackageCode() {
            assertEquals("", handler.extractPackageCode(""),
                    "extractPackageCode() should return empty string for empty");
        }

        @Test
        @DisplayName("Should handle null MPN in extractSeries")
        void shouldHandleNullMpnInExtractSeries() {
            assertEquals("", handler.extractSeries(null),
                    "extractSeries() should return empty string for null");
        }

        @Test
        @DisplayName("Should handle empty MPN in extractSeries")
        void shouldHandleEmptyMpnInExtractSeries() {
            assertEquals("", handler.extractSeries(""),
                    "extractSeries() should return empty string for empty");
        }

        @Test
        @DisplayName("Should handle null MPN in isOfficialReplacement")
        void shouldHandleNullMpnInIsOfficialReplacement() {
            assertFalse(handler.isOfficialReplacement(null, "1N4007"),
                    "isOfficialReplacement() should return false for null mpn1");
            assertFalse(handler.isOfficialReplacement("1N4007", null),
                    "isOfficialReplacement() should return false for null mpn2");
            assertFalse(handler.isOfficialReplacement(null, null),
                    "isOfficialReplacement() should return false for both null");
        }

        @Test
        @DisplayName("Should handle case insensitivity")
        void shouldHandleCaseInsensitivity() {
            assertTrue(handler.matches("1n4007", ComponentType.DIODE, registry),
                    "Should match lowercase MPN");
            assertTrue(handler.matches("1N4007", ComponentType.DIODE, registry),
                    "Should match uppercase MPN");
            assertTrue(handler.matches("mmbt3904", ComponentType.TRANSISTOR, registry),
                    "Should match lowercase transistor MPN");
        }

        @Test
        @DisplayName("Should NOT match non-Good-Ark patterns")
        void shouldNotMatchNonGoodArkPatterns() {
            assertFalse(handler.matches("LM317", ComponentType.DIODE, registry),
                    "Should not match voltage regulator as diode");
            assertFalse(handler.matches("ATMEGA328P", ComponentType.TRANSISTOR, registry),
                    "Should not match MCU as transistor");
            assertFalse(handler.matches("IRF540", ComponentType.DIODE, registry),
                    "Should not match MOSFET as diode");
        }

        @Test
        @DisplayName("Should handle MPN with suffix variations")
        void shouldHandleSuffixVariations() {
            assertTrue(handler.matches("1N4007-TP", ComponentType.DIODE, registry),
                    "Should match with -TP suffix");
            assertTrue(handler.matches("1N4007G", ComponentType.DIODE, registry),
                    "Should match with G suffix");
            assertTrue(handler.matches("MMBT3904LT1", ComponentType.TRANSISTOR, registry),
                    "Should match with LT1 suffix");
        }
    }

    // ========================================================================
    // HANDLER INITIALIZATION TESTS
    // ========================================================================

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            GoodArkHandler directHandler = new GoodArkHandler();
            assertNotNull(directHandler, "Should create handler instance");

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("1N4007", ComponentType.DIODE, directRegistry),
                    "Freshly initialized handler should work");
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes, "Should not return null");
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }

    // ========================================================================
    // CROSS-TYPE MATCHING TESTS (Negative Tests)
    // ========================================================================

    @Nested
    @DisplayName("Cross-Type Matching (Negative)")
    class CrossTypeMatchingTests {

        @Test
        @DisplayName("Diodes should NOT match as transistors")
        void diodesShouldNotMatchAsTransistors() {
            assertFalse(handler.matches("1N4007", ComponentType.TRANSISTOR, registry),
                    "1N4007 diode should not match TRANSISTOR");
            assertFalse(handler.matches("SS14", ComponentType.TRANSISTOR, registry),
                    "SS14 Schottky should not match TRANSISTOR");
            assertFalse(handler.matches("BAT54", ComponentType.TRANSISTOR, registry),
                    "BAT54 should not match TRANSISTOR");
        }

        @Test
        @DisplayName("Transistors should NOT match as diodes")
        void transistorsShouldNotMatchAsDiodes() {
            assertFalse(handler.matches("2N2222", ComponentType.DIODE, registry),
                    "2N2222 transistor should not match DIODE");
            assertFalse(handler.matches("MMBT3904", ComponentType.DIODE, registry),
                    "MMBT3904 should not match DIODE");
            assertFalse(handler.matches("BC547", ComponentType.DIODE, registry),
                    "BC547 should not match DIODE");
        }
    }
}
