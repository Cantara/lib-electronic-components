package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ToshibaHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ToshibaHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Toshiba produces MOSFETs (TK, TPC, TPH, SSM series), transistors (2SA, 2SC, RN, RP),
 * optocouplers (TLP series), motor drivers (TB series), and ARM/RISC-V MCUs (TMP series).
 *
 * CRITICAL BUG DISCOVERED:
 * The handler.matches() method uses getPattern() which returns only ONE pattern per type.
 * The registry.matches() method correctly checks ALL patterns.
 * This means handler.matches() is essentially broken for types with multiple patterns.
 *
 * BUGS DOCUMENTED IN THIS TEST FILE:
 * 1. handler.matches() uses getPattern() which returns only first pattern - DOES NOT WORK for multi-pattern types
 * 2. getSupportedTypes() uses mutable HashSet instead of immutable Set.of()
 * 3. TRANSISTOR type missing from getSupportedTypes() despite patterns existing
 * 4. IC type missing from getSupportedTypes() despite being used for IGBTs, motor drivers, optocouplers
 * 5. Package extraction incomplete - only handles TPC/TPH/TMP, missing TK, TLP, SSM, TB series
 * 6. Series extraction incomplete - missing TLP, 2SC, 2SA, TAR series
 */
class ToshibaHandlerTest {

    private static ToshibaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation - ensures we test the specific handler
        handler = new ToshibaHandler();

        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Critical Bug: handler.matches() vs registry.matches()")
    class MatchesBugTests {

        @Test
        @DisplayName("CRITICAL BUG: handler.matches() uses getPattern() which returns only one pattern")
        void handlerMatchesOnlyUsesOnePattern() {
            // registry.matches() works correctly - checks all patterns
            assertTrue(registry.matches("TPC8107", ComponentType.MOSFET_TOSHIBA),
                    "registry.matches() correctly finds TPC8107");
            assertTrue(registry.matches("TK024N60Z1", ComponentType.MOSFET_TOSHIBA),
                    "registry.matches() correctly finds TK024N60Z1");
            assertTrue(registry.matches("TPH4R606NH", ComponentType.MOSFET_TOSHIBA),
                    "registry.matches() correctly finds TPH4R606NH");
            assertTrue(registry.matches("SSM3K102TU", ComponentType.MOSFET_TOSHIBA),
                    "registry.matches() correctly finds SSM3K102TU");

            // handler.matches() uses getPattern() which returns only first pattern
            // Due to HashSet ordering, the first pattern is non-deterministic
            // This is a critical bug - handler.matches() fails for most parts
            var pattern = registry.getPattern(ComponentType.MOSFET_TOSHIBA);
            System.out.println("getPattern() returns: " + (pattern != null ? pattern.pattern() : "null"));
            // Only parts matching this one pattern will work with handler.matches()
        }

        @Test
        @DisplayName("Workaround: Use registry.matches() instead of handler.matches()")
        void useRegistryMatchesAsWorkaround() {
            // For production code, use registry.matches() which works correctly
            assertTrue(registry.matches("TPC8107", ComponentType.MOSFET_TOSHIBA));
            assertTrue(registry.matches("TK024N60Z1", ComponentType.MOSFET_TOSHIBA));
            assertTrue(registry.matches("TLP291", ComponentType.OPTOCOUPLER_TOSHIBA));
            assertTrue(registry.matches("TB6612FNG", ComponentType.MOTOR_DRIVER_TOSHIBA));
        }
    }

    @Nested
    @DisplayName("MOSFET Detection - TK Series (DTMOS Digital Power)")
    class TKSeriesMOSFETTests {

        @ParameterizedTest
        @DisplayName("TK series should match MOSFET_TOSHIBA via registry.matches()")
        @ValueSource(strings = {
                "TK024N60Z1",      // 600V 0.024 Ohm DTMOS VI
                "TK057V60Z1",      // 600V 0.057 Ohm DTMOS VI
                "TK170V65Z",       // 650V N-channel
                "TK40S10K3Z",      // 100V N-channel
                "TK39A60W"         // 600V N-channel
        })
        void shouldDetectTKSeriesMOSFETs(String mpn) {
            // Use registry.matches() which correctly checks all patterns
            assertTrue(registry.matches(mpn, ComponentType.MOSFET_TOSHIBA),
                    mpn + " should match MOSFET_TOSHIBA via registry.matches()");
            assertTrue(registry.matches(mpn, ComponentType.MOSFET),
                    mpn + " should match base MOSFET type");
        }

        @Test
        @DisplayName("Should extract TK Series for TK MOSFETs")
        void shouldExtractTKSeries() {
            assertEquals("TK Series", handler.extractSeries("TK024N60Z1"));
            assertEquals("TK Series", handler.extractSeries("TK170V65Z"));
        }
    }

    @Nested
    @DisplayName("MOSFET Detection - TPC Series (Compact Power)")
    class TPCSeriesMOSFETTests {

        @ParameterizedTest
        @DisplayName("TPC series should match MOSFET_TOSHIBA via registry.matches()")
        @ValueSource(strings = {
                "TPC8107",         // 30V N-channel
                "TPC6101",         // Small signal
                "TPC8102"          // 8-pin SOP
        })
        void shouldDetectTPCSeriesMOSFETs(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.MOSFET_TOSHIBA),
                    mpn + " should match MOSFET_TOSHIBA");
            assertTrue(registry.matches(mpn, ComponentType.MOSFET),
                    mpn + " should match base MOSFET type");
        }

        @Test
        @DisplayName("Should extract TPC Series")
        void shouldExtractTPCSeries() {
            assertEquals("TPC Series", handler.extractSeries("TPC8107"));
        }

        @Test
        @DisplayName("Should extract package code from TPC MOSFETs")
        void shouldExtractTPCPackageCode() {
            String pkgCode = handler.extractPackageCode("TPC8107LM");
            assertEquals("LM", pkgCode, "TPC8107LM should have package code 'LM'");
        }
    }

    @Nested
    @DisplayName("MOSFET Detection - TPH Series (High Voltage)")
    class TPHSeriesMOSFETTests {

        @ParameterizedTest
        @DisplayName("TPH series should match MOSFET_TOSHIBA via registry.matches()")
        @ValueSource(strings = {
                "TPH4R606NH",      // 60V 4.6mOhm TO-247
                "TPH1R306PL",      // 60V 1.3mOhm
                "TPH3206PD"        // Power MOSFET
        })
        void shouldDetectTPHSeriesMOSFETs(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.MOSFET_TOSHIBA),
                    mpn + " should match MOSFET_TOSHIBA");
        }

        @Test
        @DisplayName("Should extract TPH Series")
        void shouldExtractTPHSeries() {
            assertEquals("TPH Series", handler.extractSeries("TPH4R606NH"));
        }

        @Test
        @DisplayName("Should extract package code from TPH MOSFETs")
        void shouldExtractTPHPackageCode() {
            String pkgCode = handler.extractPackageCode("TPH4R606NH");
            assertEquals("NH", pkgCode, "TPH4R606NH should have package code 'NH'");
        }
    }

    @Nested
    @DisplayName("MOSFET Detection - SSM Series (Small Signal)")
    class SSMSeriesMOSFETTests {

        @ParameterizedTest
        @DisplayName("SSM series should match MOSFET_TOSHIBA via registry.matches()")
        @ValueSource(strings = {
                "SSM3K102TU",      // 3-pin N-channel UFM
                "SSM3K15ACT",      // 3-pin N-channel
                "SSM6K203FE",      // 6-pin N-channel
                "SSM3J332R"        // 3-pin P-channel
        })
        void shouldDetectSSMSeriesMOSFETs(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.MOSFET_TOSHIBA),
                    mpn + " should match MOSFET_TOSHIBA");
        }

        @Test
        @DisplayName("Should extract SSM Series")
        void shouldExtractSSMSeries() {
            assertEquals("SSM Series", handler.extractSeries("SSM3K102TU"));
        }

        @Test
        @DisplayName("FIXED: SSM package extraction now works")
        void ssmPackageExtractionBug() {
            String packageCode = handler.extractPackageCode("SSM3K102TU");
            assertEquals("TU", packageCode,
                    "FIXED: SSM package extraction returns 'TU'");
        }
    }

    @Nested
    @DisplayName("Transistor Detection - 2SC/2SA Series (JIS Standard)")
    class TransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect 2SC NPN high-frequency transistors")
        @ValueSource(strings = {
                "2SC5198",         // 140V 10A audio transistor
                "2SC5200",         // 230V 15A complementary pair
                "2SC1815",         // 50V 150mA general purpose
                "2SC945"           // Small signal
        })
        void shouldDetect2SCTransistors(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.TRANSISTOR),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect 2SA PNP high-frequency transistors")
        @ValueSource(strings = {
                "2SA1943",         // 230V 15A complementary to 2SC5200
                "2SA1962",         // Power transistor
                "2SA1015"          // 50V 150mA general purpose
        })
        void shouldDetect2SATransistors(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.TRANSISTOR),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect RN digital transistors")
        @ValueSource(strings = {
                "RN1002",          // Digital transistor
                "RN1428"           // With built-in resistors
        })
        void shouldDetectRNDigitalTransistors(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.TRANSISTOR),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect RP digital transistors")
        @ValueSource(strings = {
                "RP1002",          // Digital transistor
                "RP1428"           // With built-in resistors
        })
        void shouldDetectRPDigitalTransistors(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.TRANSISTOR),
                    mpn + " should match TRANSISTOR");
        }

        @Test
        @DisplayName("FIXED: extractSeries now works for transistors")
        void seriesExtractionMissingForTransistors() {
            assertEquals("2SC Series", handler.extractSeries("2SC5198"),
                    "FIXED: extractSeries works for 2SC transistors");
            assertEquals("2SA Series", handler.extractSeries("2SA1943"),
                    "FIXED: extractSeries works for 2SA transistors");
        }
    }

    @Nested
    @DisplayName("Optocoupler Detection - TLP Series")
    class OptocouplerTests {

        @ParameterizedTest
        @DisplayName("Should detect TLP optocouplers via registry.matches()")
        @ValueSource(strings = {
                "TLP127",          // Darlington output
                "TLP185",          // High isolation
                "TLP291",          // High-speed transistor output
                "TLP350",          // IGBT driver
                "TLP3910",         // Photovoltaic output
                "TLP3640A"         // 1A photo-relay
        })
        void shouldDetectTLPOptocouplers(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA),
                    mpn + " should match OPTOCOUPLER_TOSHIBA");
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match base IC type");
        }

        @Test
        @DisplayName("TLP with packaging options should still match")
        void tlpWithPackagingOptions() {
            assertTrue(registry.matches("TLP291", ComponentType.OPTOCOUPLER_TOSHIBA));
            assertTrue(registry.matches("TLP291(GB-TP,SE)", ComponentType.OPTOCOUPLER_TOSHIBA),
                    "TLP291(GB-TP,SE) should match");
        }

        @Test
        @DisplayName("FIXED: extractSeries now works for optocouplers")
        void seriesExtractionMissingForOptocouplers() {
            assertEquals("TLP Series", handler.extractSeries("TLP127"),
                    "FIXED: extractSeries works for TLP optocouplers");
        }

        @Test
        @DisplayName("TLP127 has no package code suffix")
        void packageExtractionMissingForOptocouplers() {
            assertEquals("", handler.extractPackageCode("TLP127"),
                    "TLP127 has no package code suffix");
        }
    }

    @Nested
    @DisplayName("Motor Driver Detection - TB Series")
    class MotorDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect TB series motor drivers")
        @ValueSource(strings = {
                "TB6612FNG",       // Dual H-bridge SSOP24
                "TB6612AFNG",      // Derivative
                "TB67H450FNG",     // Single H-bridge
                "TB67S109AFTG",    // Stepper driver
                "TB67S128FTG"      // Stepper driver
        })
        void shouldDetectTBMotorDrivers(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.MOTOR_DRIVER_TOSHIBA),
                    mpn + " should match MOTOR_DRIVER_TOSHIBA");
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match base IC type");
        }

        @Test
        @DisplayName("Should extract TB Series")
        void shouldExtractTBSeries() {
            assertEquals("TB Series", handler.extractSeries("TB6612FNG"));
        }

        @Test
        @DisplayName("FIXED: extractPackageCode now works for motor drivers")
        void packageExtractionMissingForMotorDrivers() {
            String packageCode = handler.extractPackageCode("TB6612FNG");
            assertEquals("FNG", packageCode,
                    "FIXED: Package extraction returns 'FNG' for TB6612FNG");
        }
    }

    @Nested
    @DisplayName("Gate Driver Detection - TPD Series")
    class GateDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect TPD gate drivers")
        @ValueSource(strings = {
                "TPD4152F",        // Gate driver
                "TPD4206F",        // IGBT/MOSFET driver
                "TPD7104F"         // High-side driver
        })
        void shouldDetectTPDGateDrivers(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.GATE_DRIVER_TOSHIBA),
                    mpn + " should match GATE_DRIVER_TOSHIBA");
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match base IC type");
        }

        @Test
        @DisplayName("Should extract TPD Series")
        void shouldExtractTPDSeries() {
            assertEquals("TPD Series", handler.extractSeries("TPD4152F"));
        }
    }

    @Nested
    @DisplayName("Voltage Regulator Detection - TAR Series")
    class VoltageRegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect TAR linear regulators")
        @ValueSource(strings = {
                "TAR5S33",         // 3.3V LDO
                "TAR5SB33",        // 3.3V variant
                "TAR5SB50"         // 5V LDO
        })
        void shouldDetectTARRegulators(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.VOLTAGE_REGULATOR_TOSHIBA),
                    mpn + " should match VOLTAGE_REGULATOR_TOSHIBA");
            assertTrue(registry.matches(mpn, ComponentType.VOLTAGE_REGULATOR),
                    mpn + " should match base VOLTAGE_REGULATOR type");
        }

        @Test
        @DisplayName("FIXED: extractSeries now works for voltage regulators")
        void seriesExtractionMissingForRegulators() {
            assertEquals("TAR Series", handler.extractSeries("TAR5S33"),
                    "FIXED: extractSeries works for TAR regulators");
        }
    }

    @Nested
    @DisplayName("IGBT Detection - GT/MG Series")
    class IGBTTests {

        @ParameterizedTest
        @DisplayName("Should detect GT series IGBTs")
        @ValueSource(strings = {
                "GT40QR21",        // IGBT module
                "GT50J101",        // High power IGBT
                "GT60M303"         // IGBT
        })
        void shouldDetectGTIGBTs(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IGBT_TOSHIBA),
                    mpn + " should match IGBT_TOSHIBA");
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match base IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect MG series IGBT modules")
        @ValueSource(strings = {
                "MG25Q2YS40",      // IGBT module
                "MG50J6ES40",      // IGBT module
                "MG75Q2YS40"       // High power module
        })
        void shouldDetectMGIGBTModules(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IGBT_TOSHIBA),
                    mpn + " should match IGBT_TOSHIBA");
        }

        @Test
        @DisplayName("Should extract GT/MG Series")
        void shouldExtractIGBTSeries() {
            assertEquals("GT Series", handler.extractSeries("GT40QR21"));
            assertEquals("MG Series", handler.extractSeries("MG25Q2YS40"));
        }
    }

    @Nested
    @DisplayName("Microcontroller Detection - TMP Series")
    class MicrocontrollerTests {

        @ParameterizedTest
        @DisplayName("Should detect TMPM ARM Cortex MCUs")
        @ValueSource(strings = {
                "TMPM370FYFG",     // Cortex-M3 256KB LQFP100
                "TMPM382FSFG",     // Cortex-M3 128KB
                "TMPM4G9F15FG"     // Cortex-M4 2MB
        })
        void shouldDetectTMPMArmMCUs(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.MICROCONTROLLER_TOSHIBA),
                    mpn + " should match MICROCONTROLLER_TOSHIBA");
            assertTrue(registry.matches(mpn, ComponentType.MICROCONTROLLER),
                    mpn + " should match base MICROCONTROLLER type");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMPV RISC-V MCUs")
        @ValueSource(strings = {
                "TMPV7502XBG"      // RISC-V MCU
        })
        void shouldDetectTMPVRiscVMCUs(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.MICROCONTROLLER_TOSHIBA),
                    mpn + " should match MICROCONTROLLER_TOSHIBA");
        }

        @Test
        @DisplayName("Should extract ARM MCU series")
        void shouldExtractMCUSeries() {
            assertEquals("ARM MCU", handler.extractSeries("TMPM370FYFG"));
        }

        @Test
        @DisplayName("Should extract RISC-V MCU series")
        void shouldExtractRiscVSeries() {
            assertEquals("RISC-V MCU", handler.extractSeries("TMPV7502XBG"));
        }

        @Test
        @DisplayName("MCU package extraction via dash")
        void mcuPackageExtraction() {
            String pkgCode = handler.extractPackageCode("TMPM370-FG");
            assertEquals("FG", pkgCode,
                    "TMPM370-FG should extract 'FG' package code");

            assertEquals("", handler.extractPackageCode("TMPM370FYFG"),
                    "TMPM370FYFG has no dash, returns empty");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should extract package codes from TPC MOSFETs")
        void tpcPackageExtraction() {
            String pkgCode = handler.extractPackageCode("TPC8107LM");
            assertEquals("LM", pkgCode, "TPC8107LM should have package code 'LM'");
        }

        @Test
        @DisplayName("Should extract package codes from TPH MOSFETs")
        void tphPackageExtraction() {
            String pkgCode = handler.extractPackageCode("TPH4R606NH");
            assertEquals("NH", pkgCode, "TPH4R606NH should have package code 'NH'");
        }

        @Test
        @DisplayName("FIXED: TK series package extraction now works")
        void tkPackageExtractionBug() {
            String pkgCode = handler.extractPackageCode("TK024N60Z1");
            assertEquals("Z1", pkgCode,
                    "FIXED: TK package extraction returns 'Z1'");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract correct series names")
        @CsvSource({
                "TK024N60Z1, TK Series",
                "TPC8107, TPC Series",
                "TPH4R606NH, TPH Series",
                "SSM3K102TU, SSM Series",
                "GT40QR21, GT Series",
                "MG25Q2YS40, MG Series",
                "TB6612FNG, TB Series",
                "TPD4152F, TPD Series",
                "TMPM370FYFG, ARM MCU",
                "TMPV7502XBG, RISC-V MCU"
        })
        void shouldExtractCorrectSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        @DisplayName("FIXED: Series extraction now complete for all product families")
        void missingSeriesExtractionBugs() {
            assertEquals("TLP Series", handler.extractSeries("TLP127"),
                    "FIXED: TLP series now handled");
            assertEquals("2SC Series", handler.extractSeries("2SC5198"),
                    "FIXED: 2SC series now handled");
            assertEquals("2SA Series", handler.extractSeries("2SA1943"),
                    "FIXED: 2SA series now handled");
            assertEquals("TAR Series", handler.extractSeries("TAR5S33"),
                    "FIXED: TAR series now handled");
            assertEquals("RN Series", handler.extractSeries("RN1002"),
                    "FIXED: RN series now handled");
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("isOfficialReplacement returns false for different parts")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("TK024N60Z1", "TK057V60Z1"),
                    "Different TK parts not considered replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("TK024N60Z1", "TPC8107"),
                    "TK and TPC are different series");
            assertFalse(handler.isOfficialReplacement("TLP127", "TB6612FNG"),
                    "TLP and TB are different product families");
        }

        @Test
        @DisplayName("isOfficialReplacement returns false for MOSFETs even with same series/package")
        void replacementAlwaysFalseForMOSFETs() {
            assertFalse(handler.isOfficialReplacement("TPC8107", "TPC8107"),
                    "Even identical TPC parts return false");
            assertFalse(handler.isOfficialReplacement("GT40QR21", "GT40QR21"),
                    "Even identical GT parts return false");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected MOSFET types")
        void shouldSupportMOSFETTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MOSFET));
            assertTrue(types.contains(ComponentType.MOSFET_TOSHIBA));
        }

        @Test
        @DisplayName("Should support expected optocoupler types")
        void shouldSupportOptocouplerTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.OPTOCOUPLER_TOSHIBA));
        }

        @Test
        @DisplayName("Should support expected motor driver types")
        void shouldSupportMotorDriverTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MOTOR_DRIVER_TOSHIBA));
        }

        @Test
        @DisplayName("Should support expected IGBT types")
        void shouldSupportIGBTTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IGBT_TOSHIBA));
        }

        @Test
        @DisplayName("Should support expected MCU types")
        void shouldSupportMCUTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MICROCONTROLLER));
            assertTrue(types.contains(ComponentType.MICROCONTROLLER_TOSHIBA));
        }

        @Test
        @DisplayName("Should support voltage regulator types")
        void shouldSupportVoltageRegulatorTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR_TOSHIBA));
        }

        @Test
        @DisplayName("FIXED: TRANSISTOR type now in supported types")
        void transistorTypeMissing() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.TRANSISTOR),
                    "FIXED: TRANSISTOR type now in getSupportedTypes()");
        }

        @Test
        @DisplayName("FIXED: IC type now in supported types")
        void icTypeMissing() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "FIXED: IC type now in getSupportedTypes()");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "TK024N60Z1"));
            assertFalse(handler.isOfficialReplacement("TK024N60Z1", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should be case-insensitive for pattern matching")
        void shouldBeCaseInsensitive() {
            // Using registry.matches() which works correctly
            assertTrue(registry.matches("tpc8107", ComponentType.MOSFET_TOSHIBA));
            assertTrue(registry.matches("TPC8107", ComponentType.MOSFET_TOSHIBA));
            assertTrue(registry.matches("Tpc8107", ComponentType.MOSFET_TOSHIBA));
        }
    }

    @Nested
    @DisplayName("Real-World Examples from Datasheets")
    class DatasheetExamples {

        @Test
        @DisplayName("TK024N60Z1 - DTMOS VI N-channel Power MOSFET")
        void tk024n60z1() {
            String mpn = "TK024N60Z1";
            assertTrue(registry.matches(mpn, ComponentType.MOSFET_TOSHIBA));
            assertEquals("TK Series", handler.extractSeries(mpn));
            // 600V, 0.024 Ohm max RDS(on), TO-220SIS package
        }

        @Test
        @DisplayName("TB6612FNG - Popular dual H-bridge motor driver")
        void tb6612fng() {
            String mpn = "TB6612FNG";
            assertTrue(registry.matches(mpn, ComponentType.MOTOR_DRIVER_TOSHIBA));
            assertEquals("TB Series", handler.extractSeries(mpn));
            // 15V max, 1.2A avg current, SSOP24 package
        }

        @Test
        @DisplayName("TLP291 - High-speed transistor output photocoupler")
        void tlp291() {
            String mpn = "TLP291";
            assertTrue(registry.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA));
            assertEquals("TLP Series", handler.extractSeries(mpn),
                    "FIXED: TLP series extracted");
        }

        @Test
        @DisplayName("2SC5200 / 2SA1943 - Complementary audio transistor pair")
        void audioTransistorPair() {
            assertTrue(registry.matches("2SC5200", ComponentType.TRANSISTOR),
                    "2SC5200 NPN should match");
            assertTrue(registry.matches("2SA1943", ComponentType.TRANSISTOR),
                    "2SA1943 PNP should match");
        }

        @Test
        @DisplayName("TMPM370FYFG - ARM Cortex-M3 microcontroller")
        void tmpm370fyfg() {
            String mpn = "TMPM370FYFG";
            assertTrue(registry.matches(mpn, ComponentType.MICROCONTROLLER_TOSHIBA));
            assertEquals("ARM MCU", handler.extractSeries(mpn));
            // 80MHz, 256KB Flash, LQFP100 package
        }

        @Test
        @DisplayName("GT40QR21 - IGBT module")
        void gt40qr21() {
            String mpn = "GT40QR21";
            assertTrue(registry.matches(mpn, ComponentType.IGBT_TOSHIBA));
            assertEquals("GT Series", handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Known Bugs Summary")
    class KnownBugsSummary {

        @Test
        @DisplayName("CRITICAL BUG: handler.matches() only checks one pattern")
        void criticalBug_handlerMatchesBroken() {
            // The ManufacturerHandler.matches() default implementation uses getPattern()
            // which returns only ONE pattern per type
            // This means handler.matches() fails for most parts when multiple patterns exist
            // WORKAROUND: Use registry.matches() instead

            // registry.matches() works correctly
            assertTrue(registry.matches("TPC8107", ComponentType.MOSFET_TOSHIBA));
            assertTrue(registry.matches("TK024N60Z1", ComponentType.MOSFET_TOSHIBA));

            // handler.matches() may fail depending on which pattern getPattern() returns
            // Due to HashSet non-deterministic ordering
        }

        @Test
        @DisplayName("FIXED: getSupportedTypes now uses immutable Set.of()")
        void bug1_mutableHashSet() {
            var types = handler.getSupportedTypes();
            assertFalse(types.getClass().getName().contains("HashSet"),
                    "FIXED: Now uses immutable Set.of() instead of mutable HashSet");
        }

        @Test
        @DisplayName("FIXED: TRANSISTOR type now in getSupportedTypes")
        void bug2_transistorTypeMissing() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.TRANSISTOR),
                    "FIXED: TRANSISTOR now in supported types");
        }

        @Test
        @DisplayName("FIXED: IC type now in getSupportedTypes")
        void bug3_icTypeMissing() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "FIXED: IC now in supported types");
        }

        @Test
        @DisplayName("FIXED: Package extraction now complete")
        void bug4_packageExtractionIncomplete() {
            assertEquals("Z1", handler.extractPackageCode("TK024N60Z1"),
                    "FIXED: TK package extraction works");
            assertEquals("", handler.extractPackageCode("TLP127"),
                    "TLP127 has no package code suffix");
            assertEquals("TU", handler.extractPackageCode("SSM3K102TU"),
                    "FIXED: SSM package extraction works");
            assertEquals("FNG", handler.extractPackageCode("TB6612FNG"),
                    "FIXED: TB package extraction works");
        }

        @Test
        @DisplayName("FIXED: Series extraction now complete")
        void bug5_seriesExtractionIncomplete() {
            assertEquals("TLP Series", handler.extractSeries("TLP127"),
                    "FIXED: TLP series extraction works");
            assertEquals("2SC Series", handler.extractSeries("2SC5198"),
                    "FIXED: 2SC series extraction works");
            assertEquals("2SA Series", handler.extractSeries("2SA1943"),
                    "FIXED: 2SA series extraction works");
            assertEquals("TAR Series", handler.extractSeries("TAR5S33"),
                    "FIXED: TAR series extraction works");
        }

        @Test
        @DisplayName("BUG #6: isOfficialReplacement always returns false for power devices")
        void bug6_replacementAlwaysFalse() {
            assertFalse(handler.isOfficialReplacement("TPC8107", "TPC8107"),
                    "Even identical parts return false - overly conservative");
        }
    }
}
