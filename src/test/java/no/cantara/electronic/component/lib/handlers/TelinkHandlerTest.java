package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.TelinkHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for TelinkHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Telink Semiconductor specializes in BLE and Zigbee SoCs:
 * - TLSR82xx series (BLE 5.0)
 * - TLSR92xx series (BLE 5.2)
 * - TLSR825x (Zigbee 3.0)
 * - B85/B87/B91 series (newer naming)
 */
class TelinkHandlerTest {

    private static TelinkHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new TelinkHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("TLSR82xx Series Detection (BLE 5.0)")
    class TLSR82xxSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect TLSR82xx BLE 5.0 SoCs")
        @ValueSource(strings = {
                "TLSR8251",
                "TLSR8251F512ET32",
                "TLSR8253",
                "TLSR8258",
                "TLSR8258F512ET48",
                "TLSR8261",
                "TLSR8269"
        })
        void shouldDetectTLSR82xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    "Should match MICROCONTROLLER type for " + mpn);
        }

        @Test
        @DisplayName("TLSR8251 is entry-level BLE 5.0")
        void tlsr8251Detection() {
            String mpn = "TLSR8251F512ET32";
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
            assertEquals("TLSR8251", handler.extractSeries(mpn));
        }

        @Test
        @DisplayName("TLSR8258 is feature-rich BLE 5.0")
        void tlsr8258Detection() {
            String mpn = "TLSR8258F512ET48";
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
            assertEquals("TLSR8258", handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("TLSR92xx Series Detection (BLE 5.2)")
    class TLSR92xxSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect TLSR92xx BLE 5.2 SoCs")
        @ValueSource(strings = {
                "TLSR9218",
                "TLSR9218A",
                "TLSR9218F512"
        })
        void shouldDetectTLSR92xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER, registry),
                    "Should match MICROCONTROLLER type for " + mpn);
        }

        @Test
        @DisplayName("TLSR9218 series extraction")
        void tlsr9218SeriesExtraction() {
            assertEquals("TLSR9218", handler.extractSeries("TLSR9218"));
            assertEquals("TLSR9218", handler.extractSeries("TLSR9218A"));
        }
    }

    @Nested
    @DisplayName("TLSR825x Series Detection (Zigbee 3.0)")
    class TLSR825xZigbeeTests {

        @ParameterizedTest
        @DisplayName("Should detect TLSR825x Zigbee 3.0 SoCs")
        @ValueSource(strings = {
                "TLSR8258",
                "TLSR8258F1KET48",
                "TLSR8253"
        })
        void shouldDetectTLSR825xZigbeeSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for Zigbee SoC " + mpn);
        }
    }

    @Nested
    @DisplayName("B-Series Detection (Newer Naming)")
    class BSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect B85 series")
        @ValueSource(strings = {
                "B85",
                "B85M",
                "B85F512"
        })
        void shouldDetectB85Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
            assertEquals("B85", handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should detect B87 series")
        @ValueSource(strings = {
                "B87",
                "B87M",
                "B87F512"
        })
        void shouldDetectB87Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
            assertEquals("B87", handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should detect B91 series (RISC-V)")
        @ValueSource(strings = {
                "B91",
                "B91M",
                "B91F512"
        })
        void shouldDetectB91Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for RISC-V SoC " + mpn);
            assertEquals("B91", handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should detect B92 series")
        @ValueSource(strings = {
                "B92",
                "B92M"
        })
        void shouldDetectB92Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match IC type for " + mpn);
            assertEquals("B92", handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract QFN-32 package code")
        @ValueSource(strings = {
                "TLSR8251F512ET32",
                "TLSR8253-Q32"
        })
        void shouldExtractQFN32(String mpn) {
            assertEquals("QFN-32", handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract QFN-48 package code")
        @ValueSource(strings = {
                "TLSR8258F512ET48",
                "TLSR9218-Q48"
        })
        void shouldExtractQFN48(String mpn) {
            assertEquals("QFN-48", handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract QFN-24 package code")
        @ValueSource(strings = {
                "TLSR8251-Q24",
                "TLSR8253ET24"
        })
        void shouldExtractQFN24(String mpn) {
            assertEquals("QFN-24", handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MPNs without package info")
        void shouldReturnEmptyForUnknownPackage() {
            assertEquals("", handler.extractPackageCode("TLSR8251"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract TLSR series")
        @CsvSource({
                "TLSR8251, TLSR8251",
                "TLSR8251F512ET32, TLSR8251",
                "TLSR8258, TLSR8258",
                "TLSR8258F512ET48, TLSR8258",
                "TLSR9218, TLSR9218",
                "TLSR9218A, TLSR9218"
        })
        void shouldExtractTLSRSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract B-series")
        @CsvSource({
                "B85, B85",
                "B85M, B85",
                "B87, B87",
                "B87F512, B87",
                "B91, B91",
                "B91M, B91",
                "B92, B92"
        })
        void shouldExtractBSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series should be replacements")
        void sameSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("TLSR8251F512ET32", "TLSR8251"),
                    "Same series different variants should be replacements");
        }

        @Test
        @DisplayName("TLSR8258 can replace TLSR8251")
        void tlsr8258ReplaceTlsr8251() {
            assertTrue(handler.isOfficialReplacement("TLSR8258", "TLSR8251"),
                    "TLSR8258 (more features) should replace TLSR8251");
        }

        @Test
        @DisplayName("TLSR8269 can replace TLSR8258")
        void tlsr8269ReplaceTlsr8258() {
            assertTrue(handler.isOfficialReplacement("TLSR8269", "TLSR8258"),
                    "TLSR8269 should replace TLSR8258");
        }

        @Test
        @DisplayName("TLSR92xx can replace TLSR82xx (BLE 5.2 vs BLE 5.0)")
        void tlsr92xxReplaceTlsr82xx() {
            assertTrue(handler.isOfficialReplacement("TLSR9218", "TLSR8258"),
                    "TLSR92xx (BLE 5.2) should replace TLSR82xx (BLE 5.0)");
        }

        @Test
        @DisplayName("B87 can replace B85")
        void b87ReplaceB85() {
            assertTrue(handler.isOfficialReplacement("B87", "B85"),
                    "B87 should replace B85");
        }

        @Test
        @DisplayName("Different families NOT replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("TLSR8251", "B91"),
                    "TLSR and B-series are different architectures");
        }

        @Test
        @DisplayName("Lower-tier cannot replace higher-tier")
        void lowerTierCannotReplaceHigher() {
            assertFalse(handler.isOfficialReplacement("TLSR8251", "TLSR8258"),
                    "TLSR8251 cannot replace TLSR8258");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC type")
        void shouldSupportIC() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("Should support MICROCONTROLLER type")
        void shouldSupportMicrocontroller() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MICROCONTROLLER),
                    "Should support MICROCONTROLLER type");
        }

        @Test
        @DisplayName("Should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.CAPACITOR),
                    "getSupportedTypes() should return immutable set");
        }

        @Test
        @DisplayName("Should have exactly 2 supported types")
        void shouldHaveExpectedTypeCount() {
            var types = handler.getSupportedTypes();
            assertEquals(2, types.size(),
                    "Should support exactly IC and MICROCONTROLLER");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "TLSR8251"));
            assertFalse(handler.isOfficialReplacement("TLSR8251", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null type gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("TLSR8251", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("tlsr8251", ComponentType.IC, registry),
                    "Should match lowercase MPN");
            assertTrue(handler.matches("Tlsr8251", ComponentType.IC, registry),
                    "Should match mixed case MPN");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            TelinkHandler directHandler = new TelinkHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes());
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }

        @Test
        @DisplayName("initializePatterns can be called multiple times")
        void canReinitializePatterns() {
            TelinkHandler testHandler = new TelinkHandler();
            PatternRegistry testRegistry = new PatternRegistry();

            testHandler.initializePatterns(testRegistry);
            testHandler.initializePatterns(testRegistry);

            assertTrue(testHandler.matches("TLSR8251", ComponentType.IC, testRegistry));
        }
    }

    @Nested
    @DisplayName("Non-Matching MPNs")
    class NonMatchingTests {

        @ParameterizedTest
        @DisplayName("Should not match non-Telink MPNs")
        @ValueSource(strings = {
                "nRF52840",           // Nordic
                "ESP32",              // Espressif
                "STM32F103",          // ST
                "ATMEGA328P",         // Atmel
                "CC2530",             // TI
                "EFR32BG22"           // Silicon Labs
        })
        void shouldNotMatchNonTelinkMpns(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    "Should not match non-Telink MPN: " + mpn);
        }

        @Test
        @DisplayName("Should not match unsupported component types")
        void shouldNotMatchUnsupportedTypes() {
            assertFalse(handler.matches("TLSR8251", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("TLSR8251", ComponentType.RESISTOR, registry));
            assertFalse(handler.matches("TLSR8251", ComponentType.DIODE, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMpnTests {

        @ParameterizedTest
        @DisplayName("Should handle real Telink part numbers")
        @CsvSource({
                "TLSR8251F512ET32, TLSR8251, QFN-32",
                "TLSR8258F512ET48, TLSR8258, QFN-48",
                "TLSR8253F256ET24, TLSR8253, QFN-24"
        })
        void shouldHandleRealPartNumbers(String mpn, String expectedSeries, String expectedPackage) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Should match " + mpn);
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }
    }
}
