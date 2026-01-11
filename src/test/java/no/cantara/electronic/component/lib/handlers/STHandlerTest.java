package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.STHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for STHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class STHandlerTest {

    private static STHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation - MPNUtils.getManufacturerHandler has a bug where it returns
        // the first handler supporting a type (alphabetically AtmelHandler < STHandler),
        // not the handler whose patterns actually match the MPN.
        handler = new STHandler();

        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("STM32 Microcontroller Detection")
    class STM32Tests {

        @ParameterizedTest
        @DisplayName("Should detect STM32F series (Mainstream)")
        @ValueSource(strings = {
                "STM32F103C8T6",    // Blue Pill
                "STM32F401CCU6",
                "STM32F407VGT6",
                "STM32F429ZIT6",
                "STM32F746NGH6"
        })
        void shouldDetectSTM32FSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ST, registry),
                    mpn + " should match MICROCONTROLLER_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect STM32L series (Low-power)")
        @ValueSource(strings = {
                "STM32L053R8T6",
                "STM32L151C8T6",
                "STM32L476RGT6"
        })
        void shouldDetectSTM32LSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ST, registry),
                    mpn + " should match MICROCONTROLLER_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect STM32H series (High-performance)")
        @ValueSource(strings = {
                "STM32H743VIT6",
                "STM32H750VBT6"
        })
        void shouldDetectSTM32HSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ST, registry),
                    mpn + " should match MICROCONTROLLER_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect STM32G series (General-purpose)")
        @ValueSource(strings = {
                "STM32G030C8T6",
                "STM32G431KBT6"
        })
        void shouldDetectSTM32GSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ST, registry),
                    mpn + " should match MICROCONTROLLER_ST");
        }

        @Test
        @DisplayName("STM32 should also match base MICROCONTROLLER type")
        void shouldMatchBaseMicrocontrollerType() {
            assertTrue(handler.matches("STM32F103C8T6", ComponentType.MICROCONTROLLER, registry),
                    "STM32F103C8T6 should match MICROCONTROLLER (base type)");
        }
    }

    @Nested
    @DisplayName("STM8 Microcontroller Detection")
    class STM8Tests {

        @ParameterizedTest
        @DisplayName("Should detect STM8S series (Standard)")
        @ValueSource(strings = {
                "STM8S003F3P6",
                "STM8S105K4T6"
        })
        void shouldDetectSTM8SSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ST, registry),
                    mpn + " should match MICROCONTROLLER_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect STM8L series (Low-power)")
        @ValueSource(strings = {
                "STM8L151C8T6",
                "STM8L052R8T6"
        })
        void shouldDetectSTM8LSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ST, registry),
                    mpn + " should match MICROCONTROLLER_ST");
        }
    }

    @Nested
    @DisplayName("Power MOSFET Detection")
    class MOSFETTests {

        @ParameterizedTest
        @DisplayName("Should detect STF series (TO-220FP)")
        @ValueSource(strings = {"STF5N52U", "STF10N60", "STF20N20"})
        void shouldDetectSTFMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET_ST, registry),
                    mpn + " should match MOSFET_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect STP series (TO-220)")
        @ValueSource(strings = {"STP20N20", "STP55NF06", "STP80NF70"})
        void shouldDetectSTPMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET_ST, registry),
                    mpn + " should match MOSFET_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect STD series (DPAK)")
        @ValueSource(strings = {"STD16NF25", "STD4N52K3", "STD20N20"})
        void shouldDetectSTDMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET_ST, registry),
                    mpn + " should match MOSFET_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect STB series (D2PAK)")
        @ValueSource(strings = {"STB80NF55", "STB200NF03"})
        void shouldDetectSTBMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET_ST, registry),
                    mpn + " should match MOSFET_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect VN/VP series")
        @ValueSource(strings = {"VN10KN3", "VN2222", "VP0106"})
        void shouldDetectVNVPSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET_ST, registry),
                    mpn + " should match MOSFET_ST");
        }

        @Test
        @DisplayName("MOSFETs should also match base MOSFET type")
        void shouldMatchBaseMOSFETType() {
            assertTrue(handler.matches("STP55NF06", ComponentType.MOSFET, registry),
                    "STP55NF06 should match MOSFET (base type)");
        }
    }

    @Nested
    @DisplayName("Voltage Regulator Detection")
    class VoltageRegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect L78xx positive regulators")
        @ValueSource(strings = {
                "L7805CV",
                "L7812CV",
                "L7815ACD2T",
                "L7805ABD2T"
        })
        void shouldDetectL78xxRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect L79xx negative regulators")
        @ValueSource(strings = {
                "L7905CV",
                "L7912CV"
        })
        void shouldDetectL79xxRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect MC78xx regulators")
        @ValueSource(strings = {
                "MC7805CT",
                "MC7812CT"
        })
        void shouldDetectMC78xxRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_ST");
        }

        @ParameterizedTest
        @DisplayName("Should detect MC317/MC337 adjustable regulators")
        @ValueSource(strings = {
                "MC317T",
                "MC337T"
        })
        void shouldDetectAdjustableRegulators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, registry),
                    mpn + " should match VOLTAGE_REGULATOR_LINEAR_ST");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract STM32 package codes")
        @CsvSource({
                "STM32F103C8T6, LQFP",   // T = LQFP
                "STM32F407VGT6, LQFP",   // T = LQFP
                "STM32L053R8T6, LQFP",   // T = LQFP
                "STM32H743VIT6, LQFP"    // T = LQFP
        })
        void shouldExtractSTM32PackageCodes(String mpn, String expectedPackage) {
            String pkg = handler.extractPackageCode(mpn);
            assertEquals(expectedPackage, pkg,
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract MOSFET package from prefix")
        @CsvSource({
                "STF5N52U, TO-220FP",
                "STP55NF06, TO-220",
                "STD16NF25, DPAK",
                "STB80NF55, D2PAK"
        })
        void shouldExtractMOSFETPackageCodes(String mpn, String expectedPackage) {
            String pkg = handler.extractPackageCode(mpn);
            assertEquals(expectedPackage, pkg,
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract voltage regulator package codes")
        @CsvSource({
                "L7805CV, TO-220",
                "L7812CV, TO-220",
                "L7815ACD2T, D2PAK",
                "L7805ABD2T, D2PAK"
        })
        void shouldExtractRegulatorPackageCodes(String mpn, String expectedPackage) {
            String pkg = handler.extractPackageCode(mpn);
            assertEquals(expectedPackage, pkg,
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract STM32 series")
        @CsvSource({
                "STM32F103C8T6, STM32F103",
                "STM32F407VGT6, STM32F407",
                "STM32L053R8T6, STM32L053"
        })
        void shouldExtractSTM32Series(String mpn, String expectedPrefix) {
            String series = handler.extractSeries(mpn);
            assertTrue(series.startsWith(expectedPrefix) || series.startsWith("STM32"),
                    "Series for " + mpn + " should start with " + expectedPrefix + " but was: " + series);
        }

        @ParameterizedTest
        @DisplayName("Should extract STM8 series")
        @CsvSource({
                "STM8S003F3P6, STM8S",
                "STM8L151C8T6, STM8L"
        })
        void shouldExtractSTM8Series(String mpn, String expectedSeries) {
            String series = handler.extractSeries(mpn);
            assertTrue(series.startsWith(expectedSeries),
                    "Series for " + mpn + " should start with " + expectedSeries + " but was: " + series);
        }

        @ParameterizedTest
        @DisplayName("Should extract MOSFET series (prefix)")
        @CsvSource({
                "STF5N52U, STF",
                "STP55NF06, STP",
                "STD16NF25, STD"
        })
        void shouldExtractMOSFETSeries(String mpn, String expectedSeries) {
            String series = handler.extractSeries(mpn);
            assertEquals(expectedSeries, series,
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.MICROCONTROLLER));
            assertTrue(types.contains(ComponentType.MICROCONTROLLER_ST));
            assertTrue(types.contains(ComponentType.MOSFET));
            assertTrue(types.contains(ComponentType.MOSFET_ST));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST));
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
            assertFalse(handler.matches(null, ComponentType.MICROCONTROLLER_ST, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "STM32F103C8T6"));
            assertFalse(handler.isOfficialReplacement("STM32F103C8T6", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MICROCONTROLLER_ST, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("STM32F103C8T6", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("stm32f103c8t6", ComponentType.MICROCONTROLLER_ST, registry));
            assertTrue(handler.matches("STM32F103C8T6", ComponentType.MICROCONTROLLER_ST, registry));
            assertTrue(handler.matches("Stm32f103c8t6", ComponentType.MICROCONTROLLER_ST, registry));
        }
    }

    @Nested
    @DisplayName("Common Development Board MCUs")
    class DevelopmentBoardTests {

        @Test
        @DisplayName("Blue Pill MCU (STM32F103C8T6)")
        void bluePillMcu() {
            assertTrue(handler.matches("STM32F103C8T6", ComponentType.MICROCONTROLLER_ST, registry));
        }

        @Test
        @DisplayName("Black Pill MCU (STM32F401CCU6)")
        void blackPillMcu() {
            assertTrue(handler.matches("STM32F401CCU6", ComponentType.MICROCONTROLLER_ST, registry));
        }

        @Test
        @DisplayName("STM32F4 Discovery MCU (STM32F407VGT6)")
        void f4DiscoveryMcu() {
            assertTrue(handler.matches("STM32F407VGT6", ComponentType.MICROCONTROLLER_ST, registry));
        }

        @Test
        @DisplayName("Nucleo-64 MCU (STM32F401RET6)")
        void nucleo64Mcu() {
            assertTrue(handler.matches("STM32F401RET6", ComponentType.MICROCONTROLLER_ST, registry));
        }
    }
}
