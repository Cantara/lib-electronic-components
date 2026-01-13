package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.EpsonHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for EpsonHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Epson timing devices and crystals.
 */
class EpsonHandlerTest {

    private static EpsonHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new EpsonHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Crystal Detection")
    class CrystalTests {

        @ParameterizedTest
        @DisplayName("Should detect FA AT-cut crystals")
        @ValueSource(strings = {"FA128", "FA238", "FA328", "FA-128", "FA-238"})
        void shouldDetectFACrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect FC tuning fork crystals")
        @ValueSource(strings = {"FC135", "FC238", "FC-135", "FC-238"})
        void shouldDetectFCCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect MA high frequency crystals")
        @ValueSource(strings = {"MA405", "MA506", "MA-405", "MA-506"})
        void shouldDetectMACrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect MC ceramic package crystals")
        @ValueSource(strings = {"MC146", "MC306", "MC-146", "MC-306"})
        void shouldDetectMCCrystals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }
    }

    @Nested
    @DisplayName("Oscillator Detection")
    class OscillatorTests {

        @ParameterizedTest
        @DisplayName("Should detect SG standard oscillators")
        @ValueSource(strings = {"SG210", "SG310", "SG-210", "SG-310"})
        void shouldDetectSGOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect TG TCXO oscillators")
        @ValueSource(strings = {"TG3541", "TG5032", "TG-3541", "TG-5032"})
        void shouldDetectTGTCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect VG VCXO oscillators")
        @ValueSource(strings = {"VG4513", "VG7050", "VG-4513", "VG-7050"})
        void shouldDetectVGVCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect HG OCXO oscillators")
        @ValueSource(strings = {"HG8002", "HG9001", "HG-8002"})
        void shouldDetectHGOCXO(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("RTC Module Detection")
    class RTCTests {

        @ParameterizedTest
        @DisplayName("Should detect RX RTC modules")
        @ValueSource(strings = {"RX4571", "RX8900", "RX-4571", "RX-8900"})
        void shouldDetectRXRTCModules(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract crystal series")
        @CsvSource({
                "FA-238SA, AT-Cut Crystal",
                "FC-135, Tuning Fork Crystal",
                "MA-506, High Frequency Crystal",
                "MC-306, Ceramic Package Crystal"
        })
        void shouldExtractCrystalSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator series")
        @CsvSource({
                "SG-310STF, Standard Oscillator",
                "SG-210STF, Programmable Oscillator",
                "SG-2100CBA, Programmable Oscillator",
                "TG-5032CJN, TCXO",
                "TG-3541CE, High Stability TCXO",
                "VG-7050, VCXO",
                "VG-4513, High Stability VCXO",
                "HG-8002, OCXO"
        })
        void shouldExtractOscillatorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract RTC series")
        @CsvSource({
                "RX-8010SJ, RTC Module",
                "RX-4571SA, Low Power RTC",
                "RX-8900SA, High Accuracy RTC"
        })
        void shouldExtractRTCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("FA-238", "FC-238"),
                    "Different crystal series should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("SG-210", "TG-210"),
                    "Different oscillator series should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "FA-238"));
            assertFalse(handler.isOfficialReplacement("FA-238", null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.CRYSTAL));
            assertTrue(types.contains(ComponentType.CRYSTAL_EPSON));
            assertTrue(types.contains(ComponentType.OSCILLATOR));
            assertTrue(types.contains(ComponentType.OSCILLATOR_EPSON));
            assertTrue(types.contains(ComponentType.OSCILLATOR_TCXO_EPSON));
            assertTrue(types.contains(ComponentType.OSCILLATOR_VCXO_EPSON));
            assertTrue(types.contains(ComponentType.OSCILLATOR_OCXO_EPSON));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.CRYSTAL, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CRYSTAL, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }
}
