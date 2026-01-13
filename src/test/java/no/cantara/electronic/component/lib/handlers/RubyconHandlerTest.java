package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.RubyconHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for RubyconHandler.
 * Tests aluminum electrolytic capacitors including ZLH, YXF/YXG, MCZ, and PK/PL series.
 *
 * Rubycon Corporation is a Japanese manufacturer specializing in
 * aluminum electrolytic capacitors for various applications.
 */
class RubyconHandlerTest {

    private static RubyconHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new RubyconHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Handler Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should return immutable Set for getSupportedTypes")
        void shouldReturnImmutableSet() {
            Set<ComponentType> types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                () -> types.add(ComponentType.RESISTOR));
        }

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            Set<ComponentType> types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.CAPACITOR),
                "Should support CAPACITOR type");
            assertTrue(types.contains(ComponentType.IC),
                "Should support IC type");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of()")
        void shouldUseSetOf() {
            Set<ComponentType> types = handler.getSupportedTypes();
            // Set.of() returns an immutable set that throws on modification
            assertThrows(UnsupportedOperationException.class,
                () -> types.add(ComponentType.RESISTOR),
                "Set should be immutable (using Set.of())");
        }
    }

    @Nested
    @DisplayName("ZLH Series - Low Impedance Capacitors")
    class ZLHSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "ZLH35VB221M08X12",  // 35V, 220uF, 8x12mm
            "ZLH50VB101M10X16", // 50V, 100uF, 10x16mm
            "ZLH25VB471M10X12", // 25V, 470uF, 10x12mm
            "ZLH16VB102M12X20", // 16V, 1000uF, 12x20mm
            "ZLH63VB470M08X11"  // 63V, 47uF, 8x11mm
        })
        @DisplayName("Should detect ZLH series as CAPACITOR")
        void shouldDetectZLHSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should match ZLH series MPN: " + mpn);
        }

        @Test
        @DisplayName("Should extract correct series for ZLH")
        void shouldExtractZLHSeries() {
            assertEquals("ZLH (Low Impedance)", handler.extractSeries("ZLH35VB221M08X12"));
        }

        @Test
        @DisplayName("Should extract package dimensions for ZLH")
        void shouldExtractZLHPackage() {
            assertEquals("8x12mm", handler.extractPackageCode("ZLH35VB221M08X12"));
            assertEquals("10x16mm", handler.extractPackageCode("ZLH50VB101M10X16"));
        }

        @Test
        @DisplayName("Should extract voltage for ZLH series")
        void shouldExtractZLHVoltage() {
            assertEquals("35V", handler.extractVoltage("ZLH35VB221M08X12"));
            assertEquals("50V", handler.extractVoltage("ZLH50VB101M10X16"));
            assertEquals("25V", handler.extractVoltage("ZLH25VB471M10X12"));
        }

        @Test
        @DisplayName("Should extract capacitance for ZLH series")
        void shouldExtractZLHCapacitance() {
            assertEquals("220uF", handler.extractCapacitance("ZLH35VB221M08X12"));
            assertEquals("100uF", handler.extractCapacitance("ZLH50VB101M10X16"));
            assertEquals("470uF", handler.extractCapacitance("ZLH25VB471M10X12"));
        }
    }

    @Nested
    @DisplayName("YXF Series - Miniature Capacitors")
    class YXFSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "YXF25VB100M05X11", // 25V, 10uF, 5x11mm
            "YXF35VB220M06X11", // 35V, 22uF, 6x11mm
            "YXF50VB470M08X11", // 50V, 47uF, 8x11mm
            "YXF16VB101M05X11"  // 16V, 100uF, 5x11mm
        })
        @DisplayName("Should detect YXF series as CAPACITOR")
        void shouldDetectYXFSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should match YXF series MPN: " + mpn);
        }

        @Test
        @DisplayName("Should extract correct series for YXF")
        void shouldExtractYXFSeries() {
            assertEquals("YXF (Miniature)", handler.extractSeries("YXF25VB100M05X11"));
        }

        @Test
        @DisplayName("Should extract package dimensions for YXF")
        void shouldExtractYXFPackage() {
            assertEquals("5x11mm", handler.extractPackageCode("YXF25VB100M05X11"));
            assertEquals("6x11mm", handler.extractPackageCode("YXF35VB220M06X11"));
        }
    }

    @Nested
    @DisplayName("YXG Series - Miniature Wide Temperature Capacitors")
    class YXGSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "YXG35VB100M05X11",  // 35V, 10uF, 5x11mm
            "YXG50VB220M06X12",  // 50V, 22uF, 6x12mm
            "YXG25VB470M08X11"   // 25V, 47uF, 8x11mm
        })
        @DisplayName("Should detect YXG series as CAPACITOR")
        void shouldDetectYXGSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should match YXG series MPN: " + mpn);
        }

        @Test
        @DisplayName("Should extract correct series for YXG")
        void shouldExtractYXGSeries() {
            assertEquals("YXG (Miniature Wide Temp)", handler.extractSeries("YXG35VB100M05X11"));
        }
    }

    @Nested
    @DisplayName("MCZ Series - Conductive Polymer Hybrid Capacitors")
    class MCZSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "MCZ1V471MNN08F12",  // 35V (1V code), 470uF, 8x12mm
            "MCZ1E101MNN06F08",  // 25V (1E code), 100uF, 6x8mm
            "MCZ1C221MNN08F10",  // 16V (1C code), 220uF, 8x10mm
            "MCZ1A102MNN10F12"   // 10V (1A code), 1000uF, 10x12mm
        })
        @DisplayName("Should detect MCZ series as CAPACITOR")
        void shouldDetectMCZSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should match MCZ series MPN: " + mpn);
        }

        @Test
        @DisplayName("Should extract correct series for MCZ")
        void shouldExtractMCZSeries() {
            assertEquals("MCZ (Polymer Hybrid)", handler.extractSeries("MCZ1V471MNN08F12"));
        }

        @Test
        @DisplayName("Should extract package with F separator for MCZ")
        void shouldExtractMCZPackage() {
            assertEquals("8x12mm", handler.extractPackageCode("MCZ1V471MNN08F12"));
            assertEquals("6x8mm", handler.extractPackageCode("MCZ1E101MNN06F08"));
        }

        @Test
        @DisplayName("Should decode MCZ voltage codes")
        void shouldDecodeMCZVoltage() {
            assertEquals("35V", handler.extractVoltage("MCZ1V471MNN08F12"));
            assertEquals("25V", handler.extractVoltage("MCZ1E101MNN06F08"));
            assertEquals("16V", handler.extractVoltage("MCZ1C221MNN08F10"));
            assertEquals("10V", handler.extractVoltage("MCZ1A102MNN10F12"));
        }
    }

    @Nested
    @DisplayName("PK Series - Small Size Capacitors")
    class PKSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "16PK1000MEFC10X20", // 16V, 1000uF, 10x20mm
            "25PK470MEFC08X15",  // 25V, 470uF, 8x15mm
            "35PK220MEFC06X11",  // 35V, 220uF, 6x11mm
            "50PK100MEFC08X11"   // 50V, 100uF, 8x11mm
        })
        @DisplayName("Should detect PK series as CAPACITOR")
        void shouldDetectPKSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should match PK series MPN: " + mpn);
        }

        @Test
        @DisplayName("Should extract correct series for PK")
        void shouldExtractPKSeries() {
            assertEquals("PK (Small Size)", handler.extractSeries("16PK1000MEFC10X20"));
        }

        @Test
        @DisplayName("Should extract voltage from PK series prefix")
        void shouldExtractPKVoltage() {
            assertEquals("16V", handler.extractVoltage("16PK1000MEFC10X20"));
            assertEquals("25V", handler.extractVoltage("25PK470MEFC08X15"));
            assertEquals("35V", handler.extractVoltage("35PK220MEFC06X11"));
            assertEquals("50V", handler.extractVoltage("50PK100MEFC08X11"));
        }

        @Test
        @DisplayName("Should extract package dimensions for PK")
        void shouldExtractPKPackage() {
            assertEquals("10x20mm", handler.extractPackageCode("16PK1000MEFC10X20"));
            assertEquals("8x15mm", handler.extractPackageCode("25PK470MEFC08X15"));
        }
    }

    @Nested
    @DisplayName("PL Series - Small Size Long Life Capacitors")
    class PLSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "16PL1000MEFC10X20", // 16V, 1000uF, 10x20mm
            "25PL470MEFC08X15",  // 25V, 470uF, 8x15mm
            "35PL220MEFC06X11"   // 35V, 220uF, 6x11mm
        })
        @DisplayName("Should detect PL series as CAPACITOR")
        void shouldDetectPLSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should match PL series MPN: " + mpn);
        }

        @Test
        @DisplayName("Should extract correct series for PL")
        void shouldExtractPLSeries() {
            assertEquals("PL (Small Size Long Life)", handler.extractSeries("16PL1000MEFC10X20"));
        }
    }

    @Nested
    @DisplayName("Other Series Tests")
    class OtherSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "ZL35VB221M08X12",   // ZL standard series
            "YXA35VB100M05X11",  // YXA high temperature
            "YXH25VB100M05X11",  // YXH low ESR miniature
            "MBZ1V471MNN08F12"   // MBZ polymer solid
        })
        @DisplayName("Should detect other series as CAPACITOR")
        void shouldDetectOtherSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should match series MPN: " + mpn);
        }

        @ParameterizedTest
        @CsvSource({
            "ZL35VB221M08X12, ZL (Standard)",
            "YXA35VB100M05X11, YXA (High Temperature)",
            "YXH25VB100M05X11, YXH (Low ESR Miniature)",
            "YXJ35VB100M05X11, YXJ (Standard Miniature)",
            "YXL25VB100M05X11, YXL (Long Life Miniature)",
            "MBZ1V471MNN08F12, MBZ (Polymer Solid)"
        })
        @DisplayName("Should extract correct series names")
        void shouldExtractCorrectSeriesNames(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Value Encoding Tests")
    class ValueEncodingTests {

        @ParameterizedTest
        @CsvSource({
            "ZLH35VB221M08X12, 220uF",   // 221M = 22 * 10^1 = 220uF
            "ZLH50VB101M10X16, 100uF",   // 101M = 10 * 10^1 = 100uF
            "ZLH25VB471M10X12, 470uF",   // 471M = 47 * 10^1 = 470uF
            "ZLH16VB102M12X20, 1000uF",  // 102M = 10 * 10^2 = 1000uF
            "ZLH63VB470M08X11, 47uF",    // 470M = 47 * 10^0 = 47uF
            "YXF25VB100M05X11, 10uF"     // 100M = 10 * 10^0 = 10uF
        })
        @DisplayName("Should decode EIA capacitance codes correctly")
        void shouldDecodeCapacitanceCodes(String mpn, String expectedCapacitance) {
            assertEquals(expectedCapacitance, handler.extractCapacitance(mpn));
        }
    }

    @Nested
    @DisplayName("Voltage Encoding Tests")
    class VoltageEncodingTests {

        @ParameterizedTest
        @CsvSource({
            "ZLH35VB221M08X12, 35V",
            "ZLH50VB101M10X16, 50V",
            "ZLH25VB471M10X12, 25V",
            "YXF16VB101M05X11, 16V",
            "16PK1000MEFC10X20, 16V",
            "25PK470MEFC08X15, 25V",
            "MCZ1V471MNN08F12, 35V",
            "MCZ1E101MNN06F08, 25V"
        })
        @DisplayName("Should extract voltage correctly from different formats")
        void shouldExtractVoltageCorrectly(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltage(mpn));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction Tests")
    class PackageCodeTests {

        @ParameterizedTest
        @CsvSource({
            "ZLH35VB221M08X12, 8x12mm",
            "ZLH50VB101M10X16, 10x16mm",
            "YXF25VB100M05X11, 5x11mm",
            "MCZ1V471MNN08F12, 8x12mm",
            "16PK1000MEFC10X20, 10x20mm"
        })
        @DisplayName("Should extract package dimensions correctly")
        void shouldExtractPackageDimensions(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Replacement Logic Tests")
    class ReplacementTests {

        @Test
        @DisplayName("Should not replace across different series")
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("ZLH35VB221M08X12", "YXF35VB221M08X12"),
                "ZLH and YXF are different series and not direct replacements");
        }

        @Test
        @DisplayName("YXG (wide temp) can replace YXF with same specs")
        void shouldAllowYXGToReplaceYXF() {
            // YXG has wider temperature range, so it can replace YXF
            assertTrue(handler.isOfficialReplacement("YXG25VB100M05X11", "YXF25VB100M05X11"),
                "YXG should be able to replace YXF with same specifications");
        }

        @Test
        @DisplayName("PL (long life) can replace PK with same specs")
        void shouldAllowPLToReplacePK() {
            // PL has longer life, so it can replace PK
            assertTrue(handler.isOfficialReplacement("16PL1000MEFC10X20", "16PK1000MEFC10X20"),
                "PL should be able to replace PK with same specifications");
        }

        @Test
        @DisplayName("ZLH can replace ZL with same specs")
        void shouldAllowZLHToReplaceZL() {
            // ZLH has lower impedance, compatible with ZL
            assertTrue(handler.isOfficialReplacement("ZLH35VB221M08X12", "ZL35VB221M08X12"),
                "ZLH should be able to replace ZL with same specifications");
        }

        @Test
        @DisplayName("Should not replace when specs differ")
        void shouldNotReplaceWithDifferentSpecs() {
            // Different voltage
            assertFalse(handler.isOfficialReplacement("YXG35VB100M05X11", "YXF25VB100M05X11"),
                "Should not replace when voltage differs");

            // Different capacitance
            assertFalse(handler.isOfficialReplacement("YXG25VB220M05X11", "YXF25VB100M05X11"),
                "Should not replace when capacitance differs");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractVoltage(null));
            assertEquals("", handler.extractCapacitance(null));
        }

        @Test
        @DisplayName("Should handle empty string gracefully")
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractVoltage(""));
            assertEquals("", handler.extractCapacitance(""));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMPNs() {
            assertTrue(handler.matches("zlh35vb221m08x12", ComponentType.CAPACITOR, registry),
                "Should match lowercase MPN");
            assertEquals("ZLH (Low Impedance)", handler.extractSeries("zlh35vb221m08x12"));
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMPNs() {
            assertTrue(handler.matches("ZlH35Vb221M08x12", ComponentType.CAPACITOR, registry),
                "Should match mixed case MPN");
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.extractSeries("UNKNOWN123"));
        }

        @Test
        @DisplayName("Should return empty package for unrecognized format")
        void shouldReturnEmptyPackageForUnrecognizedFormat() {
            assertEquals("", handler.extractPackageCode("UNKNOWN123"));
        }
    }

    @Nested
    @DisplayName("Non-Matching MPN Tests")
    class NonMatchingTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "GRM21BR61C106KE15L",  // Murata ceramic capacitor
            "UUD1C100MCL1GS",       // Nichicon capacitor
            "EEE-FK1V101P",         // Panasonic capacitor
            "T491B106K016AT",       // Kemet tantalum
            "RC0603FR-0710KL",      // Yageo resistor
            "LM7805CT",             // TI voltage regulator
            "STM32F103C8T6"         // ST microcontroller
        })
        @DisplayName("Should not match non-Rubycon MPNs")
        void shouldNotMatchNonRubyconMPNs(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Should not match non-Rubycon MPN: " + mpn);
        }
    }

    @Nested
    @DisplayName("Pattern Matching Documentation Tests")
    class PatternDocumentationTests {

        @Test
        @DisplayName("Document ZLH pattern matching")
        void documentZLHPattern() {
            String[] zlhExamples = {
                "ZLH35VB221M08X12",  // 35V, 220uF, 8x12mm
                "ZLH50VB101M10X16", // 50V, 100uF, 10x16mm
                "ZLH25VB471M10X12"  // 25V, 470uF, 10x12mm
            };

            System.out.println("=== ZLH Series Pattern Documentation ===");
            System.out.println("Format: ZLH<voltage>VB<value><tolerance><dimensions>");
            for (String mpn : zlhExamples) {
                System.out.println(String.format("  %s -> Series: %s, Voltage: %s, Cap: %s, Pkg: %s",
                    mpn,
                    handler.extractSeries(mpn),
                    handler.extractVoltage(mpn),
                    handler.extractCapacitance(mpn),
                    handler.extractPackageCode(mpn)));
            }
        }

        @Test
        @DisplayName("Document MCZ voltage codes")
        void documentMCZVoltageCodes() {
            String[][] mczExamples = {
                {"MCZ0J471MNN08F12", "6.3V"},
                {"MCZ1A101MNN06F08", "10V"},
                {"MCZ1C221MNN08F10", "16V"},
                {"MCZ1E101MNN06F08", "25V"},
                {"MCZ1V471MNN08F12", "35V"},
                {"MCZ1H100MNN08F08", "50V"}
            };

            System.out.println("=== MCZ Series Voltage Code Documentation ===");
            System.out.println("Format: MCZ<voltage_code><value><suffix>");
            System.out.println("Voltage codes: 0J=6.3V, 1A=10V, 1C=16V, 1E=25V, 1V=35V, 1H=50V");
            for (String[] example : mczExamples) {
                String mpn = example[0];
                String expectedVoltage = example[1];
                String actualVoltage = handler.extractVoltage(mpn);
                System.out.println(String.format("  %s -> Expected: %s, Actual: %s",
                    mpn, expectedVoltage, actualVoltage));
            }
        }

        @Test
        @DisplayName("Document capacitance value encoding")
        void documentCapacitanceEncoding() {
            System.out.println("=== Capacitance Value Encoding (EIA 3-digit) ===");
            System.out.println("Format: <2 significant digits><multiplier><tolerance>");
            System.out.println("Examples:");
            System.out.println("  100M = 10 * 10^0 = 10uF (M = +/-20%)");
            System.out.println("  221M = 22 * 10^1 = 220uF");
            System.out.println("  471M = 47 * 10^1 = 470uF");
            System.out.println("  102M = 10 * 10^2 = 1000uF");
            System.out.println("  103M = 10 * 10^3 = 10000uF");
        }
    }

    @Nested
    @DisplayName("Series Comparison Tests")
    class SeriesComparisonTests {

        @Test
        @DisplayName("All supported series should be detected")
        void allSeriesShouldBeDetected() {
            String[] allSeries = {
                "ZLH35VB221M08X12",  // Low impedance
                "ZL35VB221M08X12",   // Standard
                "YXF25VB100M05X11",  // Miniature
                "YXG35VB100M05X11",  // Miniature wide temp
                "YXA35VB100M05X11",  // High temperature
                "YXH25VB100M05X11",  // Low ESR miniature
                "YXJ35VB100M05X11",  // Standard miniature
                "YXL25VB100M05X11",  // Long life miniature
                "MCZ1V471MNN08F12",  // Polymer hybrid
                "MBZ1V471MNN08F12",  // Polymer solid
                "16PK1000MEFC10X20", // Small size
                "16PL1000MEFC10X20"  // Small size long life
            };

            for (String mpn : allSeries) {
                assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    "Should detect series: " + mpn);
                assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series name for: " + mpn);
            }
        }
    }
}
