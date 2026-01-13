package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MeanWellHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for MeanWellHandler.
 * Tests pattern matching, power/voltage extraction, series extraction, and replacement detection.
 */
class MeanWellHandlerTest {

    private static MeanWellHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation - avoids MPNUtils.getManufacturerHandler issues
        handler = new MeanWellHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("AC-DC Enclosed Power Supply Detection")
    class ACDCEnclosedTests {

        @ParameterizedTest
        @DisplayName("Should detect RS series power supplies")
        @ValueSource(strings = {"RS-25-5", "RS-50-12", "RS-100-24", "RS-150-48"})
        void shouldDetectRSSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_AC_DC_MEANWELL");
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY, registry),
                    mpn + " should match POWER_SUPPLY (base type)");
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_MEANWELL");
        }

        @ParameterizedTest
        @DisplayName("Should detect LRS series slim power supplies")
        @ValueSource(strings = {"LRS-50-5", "LRS-100-12", "LRS-200-24", "LRS-350-24", "LRS-600-48"})
        void shouldDetectLRSSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_AC_DC_MEANWELL");
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY, registry),
                    mpn + " should match POWER_SUPPLY (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect SE series high power supplies")
        @ValueSource(strings = {"SE-200-24", "SE-450-24", "SE-600-24", "SE-1000-24"})
        void shouldDetectSESeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_AC_DC_MEANWELL");
        }

        @ParameterizedTest
        @DisplayName("Should detect NES series power supplies")
        @ValueSource(strings = {"NES-25-5", "NES-50-12", "NES-100-24", "NES-350-48"})
        void shouldDetectNESSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_AC_DC_MEANWELL");
        }
    }

    @Nested
    @DisplayName("DC-DC Converter Detection")
    class DCDCConverterTests {

        @ParameterizedTest
        @DisplayName("Should detect SD series DC-DC converters")
        @ValueSource(strings = {"SD-25C-12", "SD-50A-24", "SD-100B-12", "SD-200C-24"})
        void shouldDetectSDSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_DC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_DC_DC_MEANWELL");
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_DC_DC, registry),
                    mpn + " should match POWER_SUPPLY_DC_DC (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect DDR series DIN rail DC-DC converters")
        @ValueSource(strings = {"DDR-15G-12", "DDR-30L-24", "DDR-60G-24", "DDR-120D-24"})
        void shouldDetectDDRSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_DC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_DC_DC_MEANWELL");
        }

        @Test
        @DisplayName("DC-DC converters should be identified correctly")
        void shouldIdentifyDCDCConverters() {
            assertTrue(handler.isDCDC("SD-25C-12"), "SD series should be DC-DC");
            assertTrue(handler.isDCDC("DDR-30L-24"), "DDR series should be DC-DC");
            assertFalse(handler.isDCDC("RS-25-5"), "RS series should NOT be DC-DC");
            assertFalse(handler.isDCDC("HLG-150H-24A"), "HLG series should NOT be DC-DC");
        }
    }

    @Nested
    @DisplayName("LED Driver Detection")
    class LEDDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect LPV series LED drivers")
        @ValueSource(strings = {"LPV-20-12", "LPV-35-24", "LPV-60-12", "LPV-100-24"})
        void shouldDetectLPVSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER_MEANWELL, registry),
                    mpn + " should match LED_DRIVER_MEANWELL");
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect HLG series LED drivers with dimming")
        @ValueSource(strings = {"HLG-40H-12", "HLG-80H-24", "HLG-150H-24A", "HLG-240H-48B", "HLG-320H-24"})
        void shouldDetectHLGSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER_MEANWELL, registry),
                    mpn + " should match LED_DRIVER_MEANWELL");
        }

        @ParameterizedTest
        @DisplayName("Should detect ELG series LED drivers")
        @ValueSource(strings = {"ELG-75-24", "ELG-100-48", "ELG-150-C500", "ELG-200-12"})
        void shouldDetectELGSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER_MEANWELL, registry),
                    mpn + " should match LED_DRIVER_MEANWELL");
        }

        @Test
        @DisplayName("LED drivers should be identified correctly")
        void shouldIdentifyLEDDrivers() {
            assertTrue(handler.isLEDDriver("LPV-60-24"), "LPV series should be LED driver");
            assertTrue(handler.isLEDDriver("HLG-150H-24A"), "HLG series should be LED driver");
            assertTrue(handler.isLEDDriver("ELG-100-48"), "ELG series should be LED driver");
            assertFalse(handler.isLEDDriver("RS-25-5"), "RS series should NOT be LED driver");
            assertFalse(handler.isLEDDriver("SD-25C-12"), "SD series should NOT be LED driver");
        }
    }

    @Nested
    @DisplayName("DIN Rail Power Supply Detection")
    class DINRailTests {

        @ParameterizedTest
        @DisplayName("Should detect HDR series ultra slim DIN rail supplies")
        @ValueSource(strings = {"HDR-15-5", "HDR-30-12", "HDR-60-24", "HDR-100-24"})
        void shouldDetectHDRSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_AC_DC_MEANWELL");
        }

        @ParameterizedTest
        @DisplayName("Should detect EDR series economy DIN rail supplies")
        @ValueSource(strings = {"EDR-75-12", "EDR-120-24", "EDR-150-48"})
        void shouldDetectEDRSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_AC_DC_MEANWELL");
        }

        @ParameterizedTest
        @DisplayName("Should detect MDR series miniature DIN rail supplies")
        @ValueSource(strings = {"MDR-10-5", "MDR-20-12", "MDR-40-24", "MDR-60-24"})
        void shouldDetectMDRSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    mpn + " should match POWER_SUPPLY_AC_DC_MEANWELL");
        }

        @Test
        @DisplayName("DIN rail supplies should be identified correctly")
        void shouldIdentifyDINRailSupplies() {
            assertTrue(handler.isDINRail("HDR-30-24"), "HDR series should be DIN rail");
            assertTrue(handler.isDINRail("EDR-120-24"), "EDR series should be DIN rail");
            assertTrue(handler.isDINRail("MDR-40-24"), "MDR series should be DIN rail");
            assertTrue(handler.isDINRail("DDR-30L-24"), "DDR series should be DIN rail");
            assertFalse(handler.isDINRail("RS-25-5"), "RS series should NOT be DIN rail");
        }
    }

    @Nested
    @DisplayName("Power Rating Extraction")
    class PowerRatingTests {

        @ParameterizedTest
        @DisplayName("Should extract power ratings from MPNs")
        @CsvSource({
                "RS-25-5, 25W",
                "LRS-350-24, 350W",
                "SE-600-24, 600W",
                "HLG-150H-24A, 150W",
                "SD-25C-12, 25W",
                "HDR-30-24, 30W"
        })
        void shouldExtractPowerRatings(String mpn, String expectedPower) {
            assertEquals(expectedPower, handler.extractPowerRating(mpn),
                    "Power rating for " + mpn);
        }

        @Test
        @DisplayName("Package code returns wattage")
        void packageCodeReturnsWattage() {
            assertEquals("25W", handler.extractPackageCode("RS-25-5"));
            assertEquals("350W", handler.extractPackageCode("LRS-350-24"));
        }
    }

    @Nested
    @DisplayName("Output Voltage Extraction")
    class OutputVoltageTests {

        @ParameterizedTest
        @DisplayName("Should extract output voltages from MPNs")
        @CsvSource({
                "RS-25-5, 5V",
                "RS-25-12, 12V",
                "LRS-350-24, 24V",
                "SE-600-48, 48V",
                "HLG-150H-24A, 24V",
                "SD-25C-12, 12V"
        })
        void shouldExtractOutputVoltages(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractOutputVoltage(mpn),
                    "Output voltage for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from MPNs")
        @CsvSource({
                "RS-25-5, RS",
                "LRS-350-24, LRS",
                "SE-600-24, SE",
                "HLG-150H-24A, HLG",
                "SD-25C-12, SD",
                "HDR-30-24, HDR",
                "DDR-60G-24, DDR",
                "LPV-60-24, LPV",
                "ELG-100-48, ELG"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Product Type Description")
    class ProductTypeTests {

        @Test
        @DisplayName("Should return correct product descriptions")
        void shouldReturnProductDescriptions() {
            assertEquals("Economy Enclosed Switching Power Supply",
                    handler.getProductType("RS-25-5"));
            assertEquals("Slim Enclosed Switching Power Supply",
                    handler.getProductType("LRS-350-24"));
            assertEquals("LED Driver with Dimming",
                    handler.getProductType("HLG-150H-24A"));
            assertEquals("DC-DC Enclosed Converter",
                    handler.getProductType("SD-25C-12"));
            assertEquals("Ultra Slim DIN Rail Power Supply",
                    handler.getProductType("HDR-30-24"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and voltage with higher wattage should be replacement")
        void higherWattageSameVoltageShouldBeReplacement() {
            assertTrue(handler.isOfficialReplacement("RS-25-5", "RS-50-5"),
                    "RS-50-5 should be a replacement for RS-25-5 (same voltage, higher wattage)");
            assertTrue(handler.isOfficialReplacement("RS-25-12", "RS-50-12"),
                    "RS-50-12 should be a replacement for RS-25-12");
        }

        @Test
        @DisplayName("Same series and voltage with lower wattage should NOT be replacement")
        void lowerWattageShouldNotBeReplacement() {
            assertFalse(handler.isOfficialReplacement("RS-50-5", "RS-25-5"),
                    "RS-25-5 should NOT be a replacement for RS-50-5 (lower wattage)");
        }

        @Test
        @DisplayName("Different voltage should NOT be replacement")
        void differentVoltageShouldNotBeReplacement() {
            assertFalse(handler.isOfficialReplacement("RS-25-5", "RS-25-12"),
                    "Different voltage should NOT be replacement");
        }

        @Test
        @DisplayName("Different series should NOT be replacement")
        void differentSeriesShouldNotBeReplacement() {
            assertFalse(handler.isOfficialReplacement("RS-25-5", "LRS-25-5"),
                    "Different series should NOT be replacement");
        }

        @Test
        @DisplayName("Same part should be replacement")
        void samePartShouldBeReplacement() {
            assertTrue(handler.isOfficialReplacement("RS-25-5", "RS-25-5"),
                    "Same part should be replacement");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.POWER_SUPPLY),
                    "Should support POWER_SUPPLY");
            assertTrue(types.contains(ComponentType.POWER_SUPPLY_AC_DC),
                    "Should support POWER_SUPPLY_AC_DC");
            assertTrue(types.contains(ComponentType.POWER_SUPPLY_DC_DC),
                    "Should support POWER_SUPPLY_DC_DC");
            assertTrue(types.contains(ComponentType.POWER_SUPPLY_MEANWELL),
                    "Should support POWER_SUPPLY_MEANWELL");
            assertTrue(types.contains(ComponentType.POWER_SUPPLY_AC_DC_MEANWELL),
                    "Should support POWER_SUPPLY_AC_DC_MEANWELL");
            assertTrue(types.contains(ComponentType.POWER_SUPPLY_DC_DC_MEANWELL),
                    "Should support POWER_SUPPLY_DC_DC_MEANWELL");
            assertTrue(types.contains(ComponentType.LED_DRIVER),
                    "Should support LED_DRIVER");
            assertTrue(types.contains(ComponentType.LED_DRIVER_MEANWELL),
                    "Should support LED_DRIVER_MEANWELL");
        }

        @Test
        @DisplayName("Should use Set.of() (immutable) for getSupportedTypes()")
        void shouldUseSetOf() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes() should return immutable Set");
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
            assertFalse(handler.matches(null, ComponentType.POWER_SUPPLY_MEANWELL, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractOutputVoltage(null));
            assertEquals("", handler.extractPowerRating(null));
            assertFalse(handler.isOfficialReplacement(null, "RS-25-5"));
            assertFalse(handler.isOfficialReplacement("RS-25-5", null));
            assertFalse(handler.isLEDDriver(null));
            assertFalse(handler.isDINRail(null));
            assertFalse(handler.isDCDC(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.POWER_SUPPLY_MEANWELL, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractOutputVoltage(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("RS-25-5", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("rs-25-5", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    "lowercase rs-25-5 should match");
            assertTrue(handler.matches("RS-25-5", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    "uppercase RS-25-5 should match");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            MeanWellHandler directHandler = new MeanWellHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify patterns work
            assertTrue(directHandler.matches("RS-25-5", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, directRegistry));
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
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @Test
        @DisplayName("Common enclosed power supplies")
        void commonEnclosedPowerSupplies() {
            // RS series - economy
            assertTrue(handler.matches("RS-25-5", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    "RS-25-5: 25W 5V enclosed supply");

            // LRS series - slim
            assertTrue(handler.matches("LRS-350-24", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    "LRS-350-24: 350W 24V slim supply");

            // SE series - high power
            assertTrue(handler.matches("SE-600-24", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    "SE-600-24: 600W 24V high power supply");
        }

        @Test
        @DisplayName("Common LED drivers")
        void commonLEDDrivers() {
            assertTrue(handler.matches("LPV-60-24", ComponentType.LED_DRIVER_MEANWELL, registry),
                    "LPV-60-24: 60W 24V LED driver");
            assertTrue(handler.matches("HLG-150H-24A", ComponentType.LED_DRIVER_MEANWELL, registry),
                    "HLG-150H-24A: 150W 24V LED driver with dimming");
        }

        @Test
        @DisplayName("Common DIN rail power supplies")
        void commonDINRailPowerSupplies() {
            assertTrue(handler.matches("HDR-30-24", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    "HDR-30-24: 30W 24V ultra slim DIN rail");
            assertTrue(handler.matches("MDR-60-24", ComponentType.POWER_SUPPLY_AC_DC_MEANWELL, registry),
                    "MDR-60-24: 60W 24V miniature DIN rail");
        }

        @Test
        @DisplayName("DC-DC converter examples")
        void dcDcConverterExamples() {
            assertTrue(handler.matches("SD-25C-12", ComponentType.POWER_SUPPLY_DC_DC_MEANWELL, registry),
                    "SD-25C-12: 25W DC-DC converter, 36-72V input to 12V output");
        }
    }
}
