package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.TrinamicHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for TrinamicHandler.
 *
 * Trinamic Motion Control (now part of Analog Devices) specializes in:
 * - Stepper motor drivers (TMC21xx, TMC22xx, TMC26xx series)
 * - Motion controllers (TMC5xxx series)
 * - Gate drivers (TMC4xxx series)
 * - 3-phase drivers (TMC6xxx series)
 *
 * Package codes:
 * - LA = QFN (Leadless Array)
 * - TA = TQFP (Thin Quad Flat Package)
 * - WA = WQFN (Very thin QFN)
 * - BOB = Breakout Board
 */
class TrinamicHandlerTest {

    private static TrinamicHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new TrinamicHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // TMC21xx Stepper Driver Tests
    // ========================================================================

    @Nested
    @DisplayName("TMC21xx Stepper Driver Detection")
    class TMC21xxTests {

        @ParameterizedTest
        @DisplayName("Should detect TMC2100 stepper drivers")
        @ValueSource(strings = {
            "TMC2100",
            "TMC2100-LA",
            "TMC2100-LA-T",
            "TMC2100-TA",
            "TMC2100LA"
        })
        void shouldDetectTMC2100(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC2130 stepper drivers")
        @ValueSource(strings = {
            "TMC2130",
            "TMC2130-LA",
            "TMC2130-LA-T",
            "TMC2130-TA",
            "TMC2130LA"
        })
        void shouldDetectTMC2130(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC2160 stepper drivers")
        @ValueSource(strings = {
            "TMC2160",
            "TMC2160-LA",
            "TMC2160-TA",
            "TMC2160-WA"
        })
        void shouldDetectTMC2160(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @Test
        @DisplayName("TMC21xx should also match IC type")
        void tmc21xxShouldMatchIC() {
            assertTrue(handler.matches("TMC2130-LA", ComponentType.IC, registry));
            assertTrue(handler.matches("TMC2160-TA", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // TMC22xx Advanced Stepper Driver Tests
    // ========================================================================

    @Nested
    @DisplayName("TMC22xx Advanced Stepper Driver Detection")
    class TMC22xxTests {

        @ParameterizedTest
        @DisplayName("Should detect TMC2208 UART stepper drivers")
        @ValueSource(strings = {
            "TMC2208",
            "TMC2208-LA",
            "TMC2208-LA-T",
            "TMC2208-TA",
            "TMC2208LA"
        })
        void shouldDetectTMC2208(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC2209 UART stepper drivers")
        @ValueSource(strings = {
            "TMC2209",
            "TMC2209-LA",
            "TMC2209-LA-T",
            "TMC2209-TA",
            "TMC2209LA"
        })
        void shouldDetectTMC2209(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC2225 stepper drivers")
        @ValueSource(strings = {
            "TMC2225",
            "TMC2225-LA",
            "TMC2225-TA"
        })
        void shouldDetectTMC2225(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC2226 stepper drivers")
        @ValueSource(strings = {
            "TMC2226",
            "TMC2226-LA",
            "TMC2226-TA"
        })
        void shouldDetectTMC2226(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @Test
        @DisplayName("TMC22xx should also match IC type")
        void tmc22xxShouldMatchIC() {
            assertTrue(handler.matches("TMC2208-LA", ComponentType.IC, registry));
            assertTrue(handler.matches("TMC2209-LA", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // TMC26xx High Power Driver Tests
    // ========================================================================

    @Nested
    @DisplayName("TMC26xx High Power Driver Detection")
    class TMC26xxTests {

        @ParameterizedTest
        @DisplayName("Should detect TMC2660 high power stepper drivers")
        @ValueSource(strings = {
            "TMC2660",
            "TMC2660-LA",
            "TMC2660-TA",
            "TMC2660-PA"
        })
        void shouldDetectTMC2660(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC2690 high power drivers")
        @ValueSource(strings = {
            "TMC2690",
            "TMC2690-LA",
            "TMC2690-TA"
        })
        void shouldDetectTMC2690(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }
    }

    // ========================================================================
    // TMC5xxx Motion Controller Tests
    // ========================================================================

    @Nested
    @DisplayName("TMC5xxx Motion Controller Detection")
    class TMC5xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect TMC5041 dual motion controller")
        @ValueSource(strings = {
            "TMC5041",
            "TMC5041-LA",
            "TMC5041-TA"
        })
        void shouldDetectTMC5041(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC5072 motion controller")
        @ValueSource(strings = {
            "TMC5072",
            "TMC5072-LA",
            "TMC5072-TA"
        })
        void shouldDetectTMC5072(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC5130 single axis controller")
        @ValueSource(strings = {
            "TMC5130",
            "TMC5130-LA",
            "TMC5130-TA",
            "TMC5130A-LA"
        })
        void shouldDetectTMC5130(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC5160 high voltage controller")
        @ValueSource(strings = {
            "TMC5160",
            "TMC5160-LA",
            "TMC5160-TA",
            "TMC5160-WA"
        })
        void shouldDetectTMC5160(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @Test
        @DisplayName("TMC5xxx should also match IC type")
        void tmc5xxxShouldMatchIC() {
            assertTrue(handler.matches("TMC5130-LA", ComponentType.IC, registry));
            assertTrue(handler.matches("TMC5160-TA", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // TMC4xxx Gate Driver / Servo Controller Tests
    // ========================================================================

    @Nested
    @DisplayName("TMC4xxx Gate Driver / Servo Controller Detection")
    class TMC4xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect TMC4361 motion controller")
        @ValueSource(strings = {
            "TMC4361",
            "TMC4361-LA",
            "TMC4361A-LA"
        })
        void shouldDetectTMC4361(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC4671 servo controller")
        @ValueSource(strings = {
            "TMC4671",
            "TMC4671-LA",
            "TMC4671-TA"
        })
        void shouldDetectTMC4671(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @Test
        @DisplayName("TMC4xxx should also match IC type")
        void tmc4xxxShouldMatchIC() {
            assertTrue(handler.matches("TMC4361-LA", ComponentType.IC, registry));
            assertTrue(handler.matches("TMC4671-LA", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // TMC6xxx 3-Phase Driver Tests
    // ========================================================================

    @Nested
    @DisplayName("TMC6xxx 3-Phase Driver Detection")
    class TMC6xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect TMC6100 3-phase driver")
        @ValueSource(strings = {
            "TMC6100",
            "TMC6100-LA",
            "TMC6100-TA"
        })
        void shouldDetectTMC6100(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC6140 3-phase driver")
        @ValueSource(strings = {
            "TMC6140",
            "TMC6140-LA",
            "TMC6140-TA"
        })
        void shouldDetectTMC6140(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @ParameterizedTest
        @DisplayName("Should detect TMC6200 gate driver")
        @ValueSource(strings = {
            "TMC6200",
            "TMC6200-LA",
            "TMC6200-TA"
        })
        void shouldDetectTMC6200(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
        }

        @Test
        @DisplayName("TMC6xxx should also match IC type")
        void tmc6xxxShouldMatchIC() {
            assertTrue(handler.matches("TMC6100-LA", ComponentType.IC, registry));
            assertTrue(handler.matches("TMC6200-TA", ComponentType.IC, registry));
        }
    }

    // ========================================================================
    // Package Code Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract QFN (LA) package codes")
        @CsvSource({
            "TMC2209-LA, QFN",
            "TMC2209-LA-T, QFN",
            "TMC5160-LA, QFN",
            "TMC2130LA, QFN"
        })
        void shouldExtractQFNPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TQFP (TA) package codes")
        @CsvSource({
            "TMC2209-TA, TQFP",
            "TMC5160-TA, TQFP",
            "TMC2130-TA-T, TQFP",
            "TMC6100-TA, TQFP"
        })
        void shouldExtractTQFPPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract WQFN (WA) package codes")
        @CsvSource({
            "TMC2209-WA, WQFN",
            "TMC5160-WA, WQFN"
        })
        void shouldExtractWQFNPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Breakout Board package codes")
        @CsvSource({
            "TMC2209-BOB, Breakout Board",
            "TMC5160-BOB, Breakout Board"
        })
        void shouldExtractBreakoutBoardPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown package code")
        void shouldReturnEmptyForUnknownPackage() {
            assertEquals("", handler.extractPackageCode("TMC2209"));
            assertEquals("", handler.extractPackageCode("TMC2209-XX"));
        }
    }

    // ========================================================================
    // Series Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract TMC21xx series")
        @CsvSource({
            "TMC2100-LA, TMC2100",
            "TMC2130-LA-T, TMC2130",
            "TMC2160-TA, TMC2160"
        })
        void shouldExtractTMC21xxSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TMC22xx series")
        @CsvSource({
            "TMC2208-LA, TMC2208",
            "TMC2209-LA-T, TMC2209",
            "TMC2225-TA, TMC2225",
            "TMC2226-LA, TMC2226"
        })
        void shouldExtractTMC22xxSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TMC26xx series")
        @CsvSource({
            "TMC2660-LA, TMC2660",
            "TMC2690-TA, TMC2690"
        })
        void shouldExtractTMC26xxSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TMC5xxx series")
        @CsvSource({
            "TMC5041-LA, TMC5041",
            "TMC5072-TA, TMC5072",
            "TMC5130-LA, TMC5130",
            "TMC5160-TA, TMC5160"
        })
        void shouldExtractTMC5xxxSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TMC4xxx series")
        @CsvSource({
            "TMC4361-LA, TMC4361",
            "TMC4671-TA, TMC4671"
        })
        void shouldExtractTMC4xxxSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TMC6xxx series")
        @CsvSource({
            "TMC6100-LA, TMC6100",
            "TMC6140-TA, TMC6140",
            "TMC6200-LA, TMC6200"
        })
        void shouldExtractTMC6xxxSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-Trinamic parts")
        void shouldReturnEmptyForNonTrinamic() {
            assertEquals("", handler.extractSeries("A4988SETTR"));
            assertEquals("", handler.extractSeries("LM358"));
            assertEquals("", handler.extractSeries(""));
        }
    }

    // ========================================================================
    // Official Replacement Tests
    // ========================================================================

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and package should be official replacement")
        void sameSeriesToAreReplacement() {
            assertTrue(handler.isOfficialReplacement("TMC2209-LA", "TMC2209-LA-T"),
                    "Same series with tape/reel variant should be replacement");
        }

        @Test
        @DisplayName("Same series different packages should NOT be replacement")
        void differentPackagesNotReplacement() {
            assertFalse(handler.isOfficialReplacement("TMC2209-LA", "TMC2209-TA"),
                    "Different packages (QFN vs TQFP) should not be direct replacement");
        }

        @Test
        @DisplayName("Different series should NOT be official replacement")
        void differentSeriesNotReplacement() {
            assertFalse(handler.isOfficialReplacement("TMC2208-LA", "TMC2209-LA"),
                    "TMC2208 and TMC2209 are different series");
            assertFalse(handler.isOfficialReplacement("TMC2130-LA", "TMC5130-LA"),
                    "TMC2130 and TMC5130 are different series");
        }

        @Test
        @DisplayName("Same series with unspecified package should be replacement")
        void sameSeriesUnspecifiedPackageIsReplacement() {
            assertTrue(handler.isOfficialReplacement("TMC2209", "TMC2209-LA"),
                    "Same series with one unspecified package should be replacement");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsNotReplacement() {
            assertFalse(handler.isOfficialReplacement(null, "TMC2209-LA"));
            assertFalse(handler.isOfficialReplacement("TMC2209-LA", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    // ========================================================================
    // Edge Cases and Null Handling
    // ========================================================================

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.MOTOR_DRIVER, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.MOTOR_DRIVER, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("TMC2209-LA", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("tmc2209-la", ComponentType.MOTOR_DRIVER, registry));
            assertTrue(handler.matches("TMC2209-LA", ComponentType.MOTOR_DRIVER, registry));
            assertTrue(handler.matches("Tmc2209-La", ComponentType.MOTOR_DRIVER, registry));
        }

        @Test
        @DisplayName("Should not match non-Trinamic parts")
        void shouldNotMatchNonTrinamic() {
            assertFalse(handler.matches("A4988SETTR", ComponentType.MOTOR_DRIVER, registry),
                    "Allegro A4988 should not match");
            assertFalse(handler.matches("LM358", ComponentType.MOTOR_DRIVER, registry),
                    "TI LM358 should not match");
            assertFalse(handler.matches("STM32F103", ComponentType.MOTOR_DRIVER, registry),
                    "STM32 should not match");
        }

        @Test
        @DisplayName("Should not match unsupported component types")
        void shouldNotMatchUnsupportedTypes() {
            assertFalse(handler.matches("TMC2209-LA", ComponentType.RESISTOR, registry));
            assertFalse(handler.matches("TMC2209-LA", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("TMC2209-LA", ComponentType.OPAMP, registry));
        }
    }

    // ========================================================================
    // getSupportedTypes() Tests
    // ========================================================================

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should include all expected component types")
        void shouldIncludeAllExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MOTOR_DRIVER), "Should support MOTOR_DRIVER");
            assertTrue(types.contains(ComponentType.IC), "Should support IC");
        }

        @Test
        @DisplayName("Should use Set.of (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes should return an immutable set");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicateTypes() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    // ========================================================================
    // Real-World MPN Tests (Common 3D Printer Parts)
    // ========================================================================

    @Nested
    @DisplayName("Real-World MPN Tests")
    class RealWorldTests {

        @ParameterizedTest
        @DisplayName("Should detect common 3D printer stepper drivers")
        @ValueSource(strings = {
            "TMC2208-LA",     // Common in 3D printers
            "TMC2209-LA",     // Popular upgrade
            "TMC2130-LA",     // Used in Prusa printers
            "TMC5160-TA",     // High power option
            "TMC2225-LA"      // Budget option
        })
        void shouldDetectCommon3DPrinterDrivers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOTOR_DRIVER, registry),
                    mpn + " should match MOTOR_DRIVER");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should also match IC");
        }

        @ParameterizedTest
        @DisplayName("Should correctly extract series from real MPNs")
        @CsvSource({
            "TMC2209-LA-T, TMC2209",
            "TMC5160-TA, TMC5160",
            "TMC2130-LA, TMC2130"
        })
        void shouldExtractSeriesFromRealMPNs(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should correctly extract package from real MPNs")
        @CsvSource({
            "TMC2209-LA-T, QFN",
            "TMC5160-TA, TQFP",
            "TMC2130-BOB, Breakout Board"
        })
        void shouldExtractPackageFromRealMPNs(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }
    }
}
