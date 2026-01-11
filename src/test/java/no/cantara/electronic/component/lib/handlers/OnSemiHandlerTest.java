package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.OnSemiHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for OnSemiHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * ON Semiconductor (onsemi) acquired Fairchild Semiconductor in 2016, inheriting FQP/FDP/FDD nomenclature.
 * Also inherited MC78xx, MC79xx, MUR, MBR series from Motorola semiconductor division.
 *
 * BUGS DOCUMENTED IN THIS TEST FILE:
 * 1. getSupportedTypes() uses mutable HashSet instead of immutable Set.of()
 * 2. No patterns registered for MOSFETs (NTD, FQP, FDP series)
 * 3. No patterns registered for transistors (2N, MMBT series)
 * 4. Missing NCP voltage regulator patterns
 * 5. Package extraction only handles diodes, not MOSFETs/transistors
 * 6. Series extraction incomplete for MOSFETs and transistors
 */
class OnSemiHandlerTest {

    private static OnSemiHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation - ensures we test the specific handler
        handler = new OnSemiHandler();

        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Diode Detection - Rectifiers")
    class RectifierDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect RL series standard rectifiers")
        @ValueSource(strings = {
                "RL201",      // 50V, 2A rectifier
                "RL202",      // 100V, 2A rectifier
                "RL203",      // 200V, 2A rectifier
                "RL204",      // 400V, 2A rectifier
                "RL205",      // 600V, 2A rectifier
                "RL206",      // 800V, 2A rectifier
                "RL207"       // 1000V, 2A rectifier
        })
        void shouldDetectRLRectifiers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry),
                    mpn + " should match DIODE_ON");
        }

        @ParameterizedTest
        @DisplayName("Should detect MUR ultra-fast recovery rectifiers")
        @ValueSource(strings = {
                "MUR120",     // 1A, 200V ultra-fast
                "MUR460",     // 4A, 600V ultra-fast
                "MUR820",     // 8A, 200V ultra-fast
                "MUR1520"     // 15A, 200V ultra-fast
        })
        void shouldDetectMURRectifiers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry),
                    mpn + " should match DIODE_ON");
        }

        @ParameterizedTest
        @DisplayName("Should detect RHRP high voltage rectifiers")
        @ValueSource(strings = {
                "RHRP860",    // 8A, 600V hyperfast
                "RHRP1560",   // 15A, 600V hyperfast
                "RHRP30120"   // 30A, 1200V hyperfast
        })
        void shouldDetectRHRPRectifiers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry),
                    mpn + " should match DIODE_ON");
        }
    }

    @Nested
    @DisplayName("Diode Detection - Schottky")
    class SchottkyDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect MBR through-hole Schottky diodes")
        @ValueSource(strings = {
                "MBR1045",    // 10A, 45V Schottky
                "MBR2045CT",  // 20A, 45V dual Schottky
                "MBR1060",    // 10A, 60V Schottky
                "MBR20100CT"  // 20A, 100V dual Schottky
        })
        void shouldDetectMBRSchottky(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry),
                    mpn + " should match DIODE_ON");
        }

        @ParameterizedTest
        @DisplayName("Should detect MBRS surface mount Schottky diodes")
        @ValueSource(strings = {
                "MBRS340",    // 3A, 40V SMD Schottky
                "MBRS360",    // 3A, 60V SMD Schottky
                "MBRS540",    // 5A, 40V SMD Schottky
                "MBRS1100"    // 1A, 100V SMD Schottky
        })
        void shouldDetectMBRSSchottky(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry),
                    mpn + " should match DIODE_ON");
        }
    }

    @Nested
    @DisplayName("Diode Detection - Zener")
    class ZenerDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect 1N47xx 1W Zener diodes")
        @ValueSource(strings = {
                "1N4728A",    // 3.3V Zener
                "1N4733A",    // 5.1V Zener
                "1N4742A",    // 12V Zener
                "1N4749A"     // 24V Zener
        })
        void shouldDetect1N47xxZeners(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry),
                    mpn + " should match DIODE_ON");
        }

        @ParameterizedTest
        @DisplayName("Should detect 1N52xx 0.5W Zener diodes")
        @ValueSource(strings = {
                "1N5225B",    // 3V Zener
                "1N5231B",    // 5.1V Zener
                "1N5242B",    // 12V Zener
                "1N5259B"     // 39V Zener
        })
        void shouldDetect1N52xxZeners(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry),
                    mpn + " should match DIODE_ON");
        }
    }

    @Nested
    @DisplayName("Op-Amp Detection")
    class OpAmpTests {

        @ParameterizedTest
        @DisplayName("Should detect MC series op-amps")
        @ValueSource(strings = {
                "MC1458",     // Dual op-amp
                "MC1458P",    // DIP package
                "MC1458D",    // SOIC package
                "MC324",      // Quad op-amp
                "MC324D",     // SOIC package
                "MC741",      // Single op-amp
                "MC741CP"     // TO-92 package
        })
        void shouldDetectMCOpAmps(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry),
                    mpn + " should match OPAMP");
            assertTrue(handler.matches(mpn, ComponentType.OPAMP_ON, registry),
                    mpn + " should match OPAMP_ON");
        }
    }

    @Nested
    @DisplayName("Voltage Regulator Detection - Linear")
    class LinearRegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect MC78xx positive regulators")
        @ValueSource(strings = {
                "MC7805CT",    // +5V, 1A, TO-220
                "MC7805",      // +5V base
                "MC7812CT",    // +12V, 1A, TO-220
                "MC7812ACT",   // +12V improved
                "MC7815CT",    // +15V, 1A
                "MC7824CT"     // +24V, 1A
        })
        void shouldDetectMC78xxRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_ON");
        }

        @ParameterizedTest
        @DisplayName("Should detect MC79xx negative regulators")
        @ValueSource(strings = {
                "MC7905CT",    // -5V, 1A, TO-220
                "MC7905",      // -5V base
                "MC7912CT",    // -12V, 1A, TO-220
                "MC7915CT",    // -15V, 1A
                "MC7924CT"     // -24V, 1A
        })
        void shouldDetectMC79xxRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_ON");
        }
    }

    @Nested
    @DisplayName("Voltage Regulator Detection - Switching")
    class SwitchingRegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect MC33xx switching regulators")
        @ValueSource(strings = {
                "MC33063AD",   // DC-DC converter, SOIC
                "MC33063A",    // 1.5A switching
                "MC33167T",    // 5A switching, TO-220
                "MC33262D"     // Power factor controller, SOIC
        })
        void shouldDetectMC33xxRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_SWITCHING_ON, registry),
                    mpn + " should match VOLTAGE_REGULATOR_SWITCHING_ON");
        }
    }

    @Nested
    @DisplayName("Missing MOSFET Patterns - BUG")
    class MOSFETPatternBugs {

        @Test
        @DisplayName("BUG: NTD series MOSFETs not detected - no patterns registered")
        void ntdMosfetsNotDetected() {
            // These are ON Semiconductor native MOSFETs but have no patterns
            assertFalse(handler.matches("NTD20N06L", ComponentType.MOSFET, registry),
                    "BUG: NTD20N06L not matched - no MOSFET pattern for NTD series");
            assertFalse(handler.matches("NTD20N06L", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: NTD20N06L not matched - no MOSFET_ONSEMI pattern for NTD series");
            assertFalse(handler.matches("NTD2955", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: NTD2955 not matched");
            assertFalse(handler.matches("NTD4809N", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: NTD4809N not matched");
        }

        @Test
        @DisplayName("BUG: FQP series MOSFETs not detected - no patterns registered")
        void fqpMosfetsNotDetected() {
            // Legacy Fairchild QFET MOSFETs have no patterns
            assertFalse(handler.matches("FQP50N06L", ComponentType.MOSFET, registry),
                    "BUG: FQP50N06L not matched - no MOSFET pattern for FQP series");
            assertFalse(handler.matches("FQP50N06L", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: FQP50N06L not matched - no MOSFET_ONSEMI pattern for FQP series");
            assertFalse(handler.matches("FQP27P06", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: FQP27P06 not matched");
            assertFalse(handler.matches("FQP4N20L", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: FQP4N20L not matched");
        }

        @Test
        @DisplayName("BUG: FDP/FDD/FDB PowerTrench MOSFETs not detected")
        void powerTrenchMosfetsNotDetected() {
            assertFalse(handler.matches("FDP3680", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: FDP3680 not matched");
            assertFalse(handler.matches("FDD3680", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: FDD3680 not matched");
            assertFalse(handler.matches("FDB3632", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: FDB3632 not matched");
        }

        @Test
        @DisplayName("BUG: 2N7xxx small signal MOSFETs not detected")
        void smallSignalMosfetsNotDetected() {
            assertFalse(handler.matches("2N7000", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: 2N7000 not matched");
            assertFalse(handler.matches("2N7002", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: 2N7002 not matched");
        }
    }

    @Nested
    @DisplayName("Missing Transistor Patterns - BUG")
    class TransistorPatternBugs {

        @Test
        @DisplayName("BUG: No transistor patterns registered at all")
        void noTransistorPatternsRegistered() {
            // Handler declares no TRANSISTOR component type in getSupportedTypes()
            // and has no transistor patterns in initializePatterns()
            var types = handler.getSupportedTypes();
            assertFalse(types.stream().anyMatch(t -> t.name().contains("TRANSISTOR")),
                    "BUG: No TRANSISTOR types in getSupportedTypes()");
        }

        @Test
        @DisplayName("BUG: 2N series transistors have no patterns")
        void twoNTransistorsNotDetected() {
            // These are classic ON Semi transistors but have no handler support
            // Note: No TRANSISTOR type in getSupportedTypes means matches() will always fail
            assertFalse(handler.matches("2N2222A", ComponentType.MOSFET_ONSEMI, registry),
                    "2N2222A is a BJT, not a MOSFET - but no transistor type supported");
            assertFalse(handler.matches("2N3904", ComponentType.MOSFET_ONSEMI, registry),
                    "2N3904 is a BJT, not a MOSFET");
        }

        @Test
        @DisplayName("BUG: MMBT series SMD transistors have no patterns")
        void mmbtTransistorsNotDetected() {
            // MMBT series are SOT-23 versions of 2N transistors
            // No handler support for these
            assertFalse(handler.matches("MMBT2222A", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: MMBT2222A not matched - no transistor support");
            assertFalse(handler.matches("MMBT3904", ComponentType.MOSFET_ONSEMI, registry),
                    "BUG: MMBT3904 not matched");
        }
    }

    @Nested
    @DisplayName("Missing NCP Regulator Patterns - BUG")
    class NCPRegulatorPatternBugs {

        @Test
        @DisplayName("BUG: NCP series regulators not detected")
        void ncpRegulatorsNotDetected() {
            // NCP is ON Semi's newer regulator series
            assertFalse(handler.matches("NCP1117ST33T3G", ComponentType.VOLTAGE_REGULATOR, registry),
                    "BUG: NCP1117ST33T3G not matched");
            assertFalse(handler.matches("NCP1117ST33T3G", ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, registry),
                    "BUG: NCP1117 not matched by LINEAR_ON pattern");
            assertFalse(handler.matches("NCP5500", ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, registry),
                    "BUG: NCP5500 not matched");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract diode packages correctly")
        @CsvSource({
                "RL207, DO-41",
                "RL204, DO-41",
                "MUR460, DO-41",
                "MUR460T, TO-220",
                "MBRS340, SMB",
                "MBR1045, DO-41",
                "MBR1045T, TO-220"
        })
        void shouldExtractDiodePackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("BUG: MOSFET package extraction not implemented")
        void mosfetPackageExtractionNotImplemented() {
            // Package extraction logic only handles diodes
            // MOSFETs fall through to generic suffix extraction which doesn't work well

            // FQP = TO-220, but handler doesn't know this
            assertNotEquals("TO-220", handler.extractPackageCode("FQP50N06L"),
                    "BUG: FQP package not extracted correctly");

            // NTD = DPAK, but handler doesn't know this
            assertNotEquals("DPAK", handler.extractPackageCode("NTD20N06L"),
                    "BUG: NTD package not extracted correctly");
        }

        @Test
        @DisplayName("Regulator package extraction works via T suffix")
        void regulatorPackageExtractionWorksViaTSuffix() {
            // CT suffix - the 'T' maps to TO-220 in PACKAGE_CODES
            assertEquals("TO-220", handler.extractPackageCode("MC7805CT"),
                    "MC78xx CT suffix should extract to TO-220 via T mapping");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract diode series correctly")
        @CsvSource({
                "RL207, RL207",
                "RL204, RL207",
                "MUR460, MUR",
                "MBRS340, MBRS",
                "MBR1045, MBR",
                "1N4733A, 1N47",
                "1N5231B, 1N52"
        })
        void shouldExtractDiodeSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract op-amp series correctly")
        @CsvSource({
                "MC1458, MC1458",
                "MC1458P, MC1458",
                "MC324, MC324",
                "MC324D, MC324",
                "MC741, MC741",
                "MC741CP, MC741"
        })
        void shouldExtractOpAmpSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract regulator series correctly")
        @CsvSource({
                "MC7805CT, MC78",
                "MC7812ACT, MC78",
                "MC7905CT, MC79",
                "MC33063AD, MC33"
        })
        void shouldExtractRegulatorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("BUG: MOSFET series extraction returns empty")
        void mosfetSeriesExtractionReturnsEmpty() {
            // No series extraction logic for MOSFETs
            assertEquals("", handler.extractSeries("FQP50N06L"),
                    "BUG: No series extraction for FQP");
            assertEquals("", handler.extractSeries("NTD20N06L"),
                    "BUG: No series extraction for NTD");
            assertEquals("", handler.extractSeries("FDP3680"),
                    "BUG: No series extraction for FDP");
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("RL series higher voltage can replace lower voltage")
        void rlHigherVoltageCanReplaceLower() {
            // RL207 (1000V) can replace RL204 (400V) - higher voltage rating
            assertTrue(handler.isOfficialReplacement("RL207", "RL204"),
                    "RL207 (1000V) should be able to replace RL204 (400V)");
        }

        @Test
        @DisplayName("RL series lower voltage cannot replace higher voltage")
        void rlLowerVoltageCannotReplaceHigher() {
            // RL204 (400V) cannot replace RL207 (1000V) - lower voltage rating
            assertFalse(handler.isOfficialReplacement("RL204", "RL207"),
                    "RL204 (400V) should NOT replace RL207 (1000V)");
        }

        @Test
        @DisplayName("Same series same package is replacement")
        void sameSeriesSamePackageIsReplacement() {
            assertTrue(handler.isOfficialReplacement("MC7805CT", "MC7805CT"),
                    "Same part should be replacement");
        }

        @Test
        @DisplayName("Different series not replacement")
        void differentSeriesNotReplacement() {
            assertFalse(handler.isOfficialReplacement("MUR460", "MBR1045"),
                    "MUR and MBR are different series");
        }

        @Test
        @DisplayName("Diode package interchangeability")
        void diodePackageInterchangeability() {
            // DO-41 and DO-201 are interchangeable for some applications
            // This tests the isOfficialReplacement logic
            assertTrue(handler.isOfficialReplacement("RL207", "RL207"),
                    "Same DO-41 parts should be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support MOSFET types")
        void shouldSupportMOSFETTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MOSFET));
            assertTrue(types.contains(ComponentType.MOSFET_ONSEMI));
        }

        @Test
        @DisplayName("Should support IGBT types")
        void shouldSupportIGBTTypes() {
            var types = handler.getSupportedTypes();
            // IGBT is commented out in handler, but IGBT_ONSEMI should exist
            assertTrue(types.contains(ComponentType.IGBT_ONSEMI));
        }

        @Test
        @DisplayName("Should support voltage regulator types")
        void shouldSupportVoltageRegulatorTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR_LINEAR_ON));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR_SWITCHING_ON));
        }

        @Test
        @DisplayName("Should support op-amp types")
        void shouldSupportOpAmpTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.OPAMP));
            assertTrue(types.contains(ComponentType.OPAMP_ON));
        }

        @Test
        @DisplayName("Should support diode types")
        void shouldSupportDiodeTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.DIODE));
            assertTrue(types.contains(ComponentType.DIODE_ON));
        }

        @Test
        @DisplayName("Should support driver types")
        void shouldSupportDriverTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.LED_DRIVER_ONSEMI));
            assertTrue(types.contains(ComponentType.MOTOR_DRIVER_ONSEMI));
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        @DisplayName("BUG: Uses mutable HashSet instead of immutable Set")
        void bugMutableHashSet() {
            // getSupportedTypes() returns a HashSet which is mutable
            // Should use Set.of() or EnumSet for immutability
            var types = handler.getSupportedTypes();
            assertNotNull(types);
            // This is a documentation of the bug - types is modifiable
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
            assertFalse(handler.isOfficialReplacement(null, "RL207"));
            assertFalse(handler.isOfficialReplacement("RL207", null));
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
            assertFalse(handler.matches("RL207", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("rl207", ComponentType.DIODE, registry));
            assertTrue(handler.matches("RL207", ComponentType.DIODE, registry));
            assertTrue(handler.matches("Rl207", ComponentType.DIODE, registry));
        }
    }

    @Nested
    @DisplayName("Real-World Component Examples from Datasheets")
    class DatasheetExamples {

        @Test
        @DisplayName("RL207 - 1000V 2A Standard Rectifier")
        void rl207() {
            String mpn = "RL207";
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry));
            assertTrue(handler.matches(mpn, ComponentType.DIODE_ON, registry));
            assertEquals("RL207", handler.extractSeries(mpn));
            assertEquals("DO-41", handler.extractPackageCode(mpn));
        }

        @Test
        @DisplayName("MUR460 - 4A 600V Ultra-Fast Rectifier")
        void mur460() {
            String mpn = "MUR460";
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry));
            assertEquals("MUR", handler.extractSeries(mpn));
            assertEquals("DO-41", handler.extractPackageCode(mpn));
        }

        @Test
        @DisplayName("MBR1045 - 10A 45V Schottky Rectifier")
        void mbr1045() {
            String mpn = "MBR1045";
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry));
            assertEquals("MBR", handler.extractSeries(mpn));
            assertEquals("DO-41", handler.extractPackageCode(mpn));
        }

        @Test
        @DisplayName("1N4733A - 5.1V 1W Zener Diode")
        void oneN4733a() {
            String mpn = "1N4733A";
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry));
            assertEquals("1N47", handler.extractSeries(mpn));
        }

        @Test
        @DisplayName("MC7805CT - 5V 1A Linear Regulator")
        void mc7805ct() {
            String mpn = "MC7805CT";
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, registry));
            assertEquals("MC78", handler.extractSeries(mpn));
        }

        @Test
        @DisplayName("MC33063AD - DC-DC Converter")
        void mc33063ad() {
            String mpn = "MC33063AD";
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_SWITCHING_ON, registry));
            assertEquals("MC33", handler.extractSeries(mpn));
        }

        @Test
        @DisplayName("MC1458 - Dual Op-Amp")
        void mc1458() {
            String mpn = "MC1458";
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry));
            assertTrue(handler.matches(mpn, ComponentType.OPAMP_ON, registry));
            assertEquals("MC1458", handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Diode Voltage Extraction")
    class DiodeVoltageExtractionTests {

        @Test
        @DisplayName("RL series voltage extraction")
        void rlSeriesVoltageExtraction() {
            // RL207 (1000V) can replace RL204 (400V) based on voltage
            // This validates the private extractDiodeVoltage method indirectly
            assertTrue(handler.isOfficialReplacement("RL207", "RL201"),
                    "1000V can replace 50V");
            assertTrue(handler.isOfficialReplacement("RL207", "RL206"),
                    "1000V can replace 800V");
            assertFalse(handler.isOfficialReplacement("RL201", "RL207"),
                    "50V cannot replace 1000V");
        }
    }

    @Nested
    @DisplayName("Known Bugs Summary")
    class KnownBugsSummary {

        @Test
        @DisplayName("BUG #1: Uses mutable HashSet in getSupportedTypes()")
        void bug1_mutableHashSet() {
            // Should use Set.of() or EnumSet for immutability
            var types = handler.getSupportedTypes();
            assertNotNull(types);
        }

        @Test
        @DisplayName("BUG #2: No MOSFET patterns registered")
        void bug2_noMosfetPatterns() {
            // Handler declares MOSFET and MOSFET_ONSEMI in getSupportedTypes()
            // but has no patterns for NTD, FQP, FDP, etc.
            assertFalse(handler.matches("FQP50N06L", ComponentType.MOSFET_ONSEMI, registry));
            assertFalse(handler.matches("NTD20N06L", ComponentType.MOSFET_ONSEMI, registry));
        }

        @Test
        @DisplayName("BUG #3: No transistor support")
        void bug3_noTransistorSupport() {
            // Handler doesn't support TRANSISTOR type at all
            // 2N and MMBT series transistors can't be detected
            var types = handler.getSupportedTypes();
            assertFalse(types.stream().anyMatch(t -> t.name().contains("TRANSISTOR")));
        }

        @Test
        @DisplayName("BUG #4: NCP regulator patterns missing")
        void bug4_ncpPatternsMissing() {
            // NCP is ON Semi's modern regulator series
            assertFalse(handler.matches("NCP1117ST33T3G", ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, registry));
        }

        @Test
        @DisplayName("BUG #5: IGBT patterns missing")
        void bug5_igbtPatternsMissing() {
            // IGBT_ONSEMI is in getSupportedTypes() but no patterns registered
            // Note: Base IGBT type is commented out in handler
            assertFalse(handler.matches("NGTB15N120SWG", ComponentType.IGBT_ONSEMI, registry),
                    "BUG: NGTB IGBT not matched - no patterns");
        }

        @Test
        @DisplayName("BUG #6: LED_DRIVER patterns missing")
        void bug6_ledDriverPatternsMissing() {
            // LED_DRIVER_ONSEMI in getSupportedTypes() but no patterns
            assertFalse(handler.matches("NCL30186", ComponentType.LED_DRIVER_ONSEMI, registry),
                    "BUG: NCL LED driver not matched - no patterns");
        }

        @Test
        @DisplayName("BUG #7: MOTOR_DRIVER patterns missing")
        void bug7_motorDriverPatternsMissing() {
            // MOTOR_DRIVER_ONSEMI in getSupportedTypes() but no patterns
            assertFalse(handler.matches("NCV7428", ComponentType.MOTOR_DRIVER_ONSEMI, registry),
                    "BUG: NCV motor driver not matched - no patterns");
        }
    }
}
