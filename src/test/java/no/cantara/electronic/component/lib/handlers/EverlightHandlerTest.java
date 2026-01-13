package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.EverlightHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for EverlightHandler.
 *
 * Everlight Electronics is a major LED and optocoupler manufacturer producing:
 * - Standard SMD LEDs (17-21, 19-21, 26-21 series based on package size)
 * - High-power SMD LEDs (ELSH series)
 * - SMD LEDs (EL-SSD series)
 * - Through-hole LEDs (333-2 series)
 * - RGB LEDs (19-337 series)
 * - High-power LEDs (Shwo series, EASP series)
 * - Infrared LEDs (IR333, IR908)
 * - Phototransistors (PT333, PT908, PT19-21)
 * - Optocouplers (EL817, EL357, EL3063)
 * - Light Sensors (ALS-PT19-315)
 * - Seven-segment displays
 *
 * Key MPN patterns:
 * - 17-21SURC/S530-A3/TR8: 0603 SMD red LED
 * - 19-21SUGC/S530-A2/TR8: 0805 SMD green LED
 * - EL817S1-C-TU: Optocoupler, SMD variant
 * - IR333-A: 3mm infrared LED
 * - PT908-7C-F: 5mm phototransistor
 * - ALS-PT19-315C/L177/TR8: Light sensor
 */
class EverlightHandlerTest {

    private static EverlightHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new EverlightHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    // ========================================================================
    // Standard SMD LED Tests (17-21, 19-21, 26-21 series)
    // ========================================================================

    @Nested
    @DisplayName("Standard SMD LED Detection")
    class StandardSMDLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect 17-21 series (0603) SMD LEDs")
        @ValueSource(strings = {
                "17-21SURC/S530-A3/TR8",
                "17-21SUGC/S530-A2/TR8",
                "17-21SUBC/S530-A3/TR8",
                "17-21SUYC/S530-A4/TR8",
                "17-21SUOC/S530-A3/TR8",
                "17-21SUWC/S530-W2/TR8"
        })
        void shouldDetect17_21SeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect 19-21 series (0805) SMD LEDs")
        @ValueSource(strings = {
                "19-21SURC/S530-A3/TR8",
                "19-21SUGC/Y530-A2/TR8",
                "19-21SUBC/C530-A3/TR8",
                "19-21SUYC/S530-A4/TR8"
        })
        void shouldDetect19_21SeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect 26-21 series (1206) SMD LEDs")
        @ValueSource(strings = {
                "26-21SURC/S530-A3/TR8",
                "26-21SUGC/Y530-A2/TR8",
                "26-21SUBC/C530-A3/TR8"
        })
        void shouldDetect26_21SeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Standard SMD LEDs should return correct mounting type")
        void standardSMDShouldReturnSMDMountingType() {
            assertEquals("SMD", handler.getMountingType("17-21SURC/S530-A3/TR8"));
            assertEquals("SMD", handler.getMountingType("19-21SUGC/S530-A2/TR8"));
            assertEquals("SMD", handler.getMountingType("26-21SUBC/C530-A3/TR8"));
        }
    }

    // ========================================================================
    // Through-Hole LED Tests (333 series)
    // ========================================================================

    @Nested
    @DisplayName("Through-Hole LED Detection (333 series)")
    class ThroughHoleLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect 333 series through-hole LEDs")
        @ValueSource(strings = {
                "333-2SURC/S400-A3",
                "333-2SUGC/Y530-A2",
                "333-2SUBC/C530-A3",
                "333SURC/S400-A3",
                "333-2SUYC/S530-A4"
        })
        void shouldDetect333SeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Through-hole LEDs should return correct mounting type")
        void throughHoleShouldReturnCorrectMountingType() {
            assertEquals("Through-hole", handler.getMountingType("333-2SURC/S400-A3"));
            assertEquals("Through-hole", handler.getMountingType("333SURC/S400-A3"));
        }
    }

    // ========================================================================
    // RGB LED Tests (19-337 series)
    // ========================================================================

    @Nested
    @DisplayName("RGB LED Detection")
    class RGBLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect RGB LEDs")
        @ValueSource(strings = {
                "19-337/RGB/KHC-A01/2T",
                "19-337RGB/KHC-A01/2T",
                "19-337-RGB-CATHODE"
        })
        void shouldDetectRGBLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("RGB LEDs should extract correct color")
        void rgbShouldExtractRGBColor() {
            assertEquals("RGB", handler.extractColor("19-337/RGB/KHC-A01/2T"));
            assertEquals("RGB", handler.extractColor("19-337RGB/KHC-A01/2T"));
        }
    }

    // ========================================================================
    // Infrared LED Tests (IR series)
    // ========================================================================

    @Nested
    @DisplayName("Infrared LED Detection")
    class InfraredLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect IR333 series (3mm) infrared LEDs")
        @ValueSource(strings = {
                "IR333-A",
                "IR333-A/L10",
                "IR333/H0/L10",
                "IR333C-A"
        })
        void shouldDetectIR333SeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect IR908 series (5mm) infrared LEDs")
        @ValueSource(strings = {
                "IR908-7C",
                "IR908-7C-F",
                "IR908-7C-TP/L10"
        })
        void shouldDetectIR908SeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Infrared LEDs should return correct mounting type")
        void irLEDsShouldReturnCorrectMountingType() {
            assertEquals("Through-hole", handler.getMountingType("IR333-A"));
            assertEquals("Through-hole", handler.getMountingType("IR908-7C"));
        }

        @Test
        @DisplayName("Infrared LEDs should be categorized correctly")
        void irLEDsShouldBeCategorizedCorrectly() {
            assertEquals("Infrared LED", handler.getProductCategory("IR333-A"));
            assertEquals("Infrared LED", handler.getProductCategory("IR908-7C-F"));
        }
    }

    // ========================================================================
    // Phototransistor Tests (PT series)
    // ========================================================================

    @Nested
    @DisplayName("Phototransistor Detection")
    class PhototransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect PT333 series phototransistors")
        @ValueSource(strings = {
                "PT333-3C",
                "PT333-3C/L10",
                "PT333/D10",
                "PT333-3B/L10"
        })
        void shouldDetectPT333SeriesPhototransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should also match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect PT908 series phototransistors")
        @ValueSource(strings = {
                "PT908-7C",
                "PT908-7C-F",
                "PT908-7C-TP/L10"
        })
        void shouldDetectPT908SeriesPhototransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect PT19-21 series SMD phototransistors")
        @ValueSource(strings = {
                "PT19-21B/TR8",
                "PT19-21C/TR8",
                "PT19-21B/L41/TR8"
        })
        void shouldDetectPT19_21SeriesPhototransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("Phototransistors should return correct mounting type")
        void phototransistorsShouldReturnCorrectMountingType() {
            assertEquals("Through-hole", handler.getMountingType("PT333-3C"));
            assertEquals("Through-hole", handler.getMountingType("PT908-7C"));
            assertEquals("SMD", handler.getMountingType("PT19-21B/TR8"));
        }

        @Test
        @DisplayName("Phototransistors should be categorized correctly")
        void phototransistorsShouldBeCategorizedCorrectly() {
            assertEquals("Phototransistor", handler.getProductCategory("PT333-3C"));
            assertEquals("Phototransistor", handler.getProductCategory("PT908-7C"));
            assertEquals("Phototransistor", handler.getProductCategory("PT19-21B/TR8"));
        }
    }

    // ========================================================================
    // Optocoupler Tests (EL817, EL357, EL3063)
    // ========================================================================

    @Nested
    @DisplayName("Optocoupler Detection")
    class OptocouplerTests {

        @ParameterizedTest
        @DisplayName("Should detect EL817 optocouplers")
        @ValueSource(strings = {
                "EL817",
                "EL817S1-C-TU",
                "EL817S2-C",
                "EL817(A)(TU)",
                "EL817(B)(TA)-G",
                "EL817S(A)(TU)-G"
        })
        void shouldDetectEL817Optocouplers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.OPTOCOUPLER_TOSHIBA, registry),
                    mpn + " should match OPTOCOUPLER_TOSHIBA");
        }

        @ParameterizedTest
        @DisplayName("Should detect EL357 optocouplers")
        @ValueSource(strings = {
                "EL357N-C",
                "EL357N(A)(TA)-G",
                "EL357NC-G",
                "EL357N(B)(TA)"
        })
        void shouldDetectEL357Optocouplers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect EL3063 and other optocouplers")
        @ValueSource(strings = {
                "EL3063",
                "EL3063S(TA)",
                "EL3061",
                "EL3062"
        })
        void shouldDetectOtherOptocouplers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Optocouplers should return correct mounting type")
        void optocouplersShouldReturnCorrectMountingType() {
            assertEquals("DIP", handler.getMountingType("EL817"));
            assertEquals("SMD", handler.getMountingType("EL817S1-C-TU"));
            assertEquals("SMD", handler.getMountingType("EL817S2-C"));
        }

        @Test
        @DisplayName("Optocouplers should be categorized correctly")
        void optocouplersShouldBeCategorizedCorrectly() {
            assertEquals("Optocoupler", handler.getProductCategory("EL817"));
            assertEquals("Optocoupler", handler.getProductCategory("EL357N-C"));
            assertEquals("Optocoupler", handler.getProductCategory("EL3063"));
        }
    }

    // ========================================================================
    // Light Sensor Tests (ALS-PT series)
    // ========================================================================

    @Nested
    @DisplayName("Light Sensor Detection")
    class LightSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect ALS-PT light sensors")
        @ValueSource(strings = {
                "ALS-PT19-315C/L177/TR8",
                "ALS-PT19-315B/L177/TR8",
                "ALS-PT19-315C/TR8"
        })
        void shouldDetectLightSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Light sensors should return correct mounting type")
        void lightSensorsShouldReturnSMDMountingType() {
            assertEquals("SMD", handler.getMountingType("ALS-PT19-315C/L177/TR8"));
        }

        @Test
        @DisplayName("Light sensors should be categorized correctly")
        void lightSensorsShouldBeCategorizedCorrectly() {
            assertEquals("Light Sensor", handler.getProductCategory("ALS-PT19-315C/L177/TR8"));
        }
    }

    // ========================================================================
    // High-Power LED Tests (ELSH, Shwo, EASP series)
    // ========================================================================

    @Nested
    @DisplayName("High-Power LED Detection")
    class HighPowerLEDTests {

        @ParameterizedTest
        @DisplayName("Should detect ELSH series high-power LEDs")
        @ValueSource(strings = {
                "ELSH-Q61K1-0LPGS",
                "ELSH-Q91A1-0LPGS",
                "ELSH-R61D1-0LPRS"
        })
        void shouldDetectELSHSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect Shwo series high-power LEDs")
        @ValueSource(strings = {
                "SHWO-1CZR-30",
                "SHWO-2CZW-41",
                "SHWO3CZW-51"
        })
        void shouldDetectShwoSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @ParameterizedTest
        @DisplayName("Should detect EASP series high-power LEDs")
        @ValueSource(strings = {
                "EASP-KW15-50",
                "EASP-KW25-50",
                "EASP-3535WW"
        })
        void shouldDetectEASPSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("High-power LEDs should return correct mounting type")
        void highPowerLEDsShouldReturnSMDMountingType() {
            assertEquals("SMD", handler.getMountingType("ELSH-Q61K1-0LPGS"));
            assertEquals("SMD", handler.getMountingType("SHWO-1CZR-30"));
            assertEquals("SMD", handler.getMountingType("EASP-KW15-50"));
        }

        @Test
        @DisplayName("High-power LEDs should be categorized correctly")
        void highPowerLEDsShouldBeCategorizedCorrectly() {
            assertEquals("High-power SMD LED", handler.getProductCategory("ELSH-Q61K1-0LPGS"));
            assertEquals("High-power LED", handler.getProductCategory("SHWO-1CZR-30"));
            assertEquals("High-power LED", handler.getProductCategory("EASP-KW15-50"));
        }
    }

    // ========================================================================
    // EL-SSD Series LED Tests
    // ========================================================================

    @Nested
    @DisplayName("EL-SSD Series LED Detection")
    class ELSSDSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect EL-SSD series SMD LEDs")
        @ValueSource(strings = {
                "EL-SSD1206R",
                "EL-SSD0805G",
                "EL-SSD0603B"
        })
        void shouldDetectELSSDSeriesLEDs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
        }

        @Test
        @DisplayName("EL-SSD LEDs should return SMD mounting type")
        void elSSDShouldReturnSMDMountingType() {
            assertEquals("SMD", handler.getMountingType("EL-SSD1206R"));
        }
    }

    // ========================================================================
    // Package Code Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract SMD package codes")
        @CsvSource({
                "17-21SURC/S530-A3/TR8, 0603",
                "19-21SUGC/S530-A2/TR8, 0805",
                "26-21SUBC/C530-A3/TR8, 1206"
        })
        void shouldExtractSMDPackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract through-hole package codes")
        @CsvSource({
                "333-2SURC/S400-A3, T-1",
                "IR333-A, T-1",
                "IR908-7C, T-1-3/4",
                "PT333-3C, T-1",
                "PT908-7C, T-1-3/4"
        })
        void shouldExtractThroughHolePackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract optocoupler package codes")
        @CsvSource({
                "EL817, DIP-4",
                "EL817S1-C-TU, SMD",
                "EL817S2-C, SMD"
        })
        void shouldExtractOptocouplerPackageCodes(String mpn, String expected) {
            String result = handler.extractPackageCode(mpn);
            assertEquals(expected, result, "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should extract SMD phototransistor package codes")
        void shouldExtractSMDPhototransistorPackageCodes() {
            assertEquals("0805", handler.extractPackageCode("PT19-21B/TR8"));
        }

        @Test
        @DisplayName("Should handle null and empty inputs")
        void shouldHandleNullAndEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    // ========================================================================
    // Series Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract SMD LED series")
        @CsvSource({
                "17-21SURC/S530-A3/TR8, 17-21",
                "19-21SUGC/S530-A2/TR8, 19-21",
                "26-21SUBC/C530-A3/TR8, 26-21"
        })
        void shouldExtractSMDLEDSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract through-hole LED series")
        @CsvSource({
                "333-2SURC/S400-A3, 333-2",
                "333SURC/S400-A3, 333"
        })
        void shouldExtractThroughHoleLEDSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract infrared LED series")
        @CsvSource({
                "IR333-A, IR333",
                "IR908-7C, IR908"
        })
        void shouldExtractIRLEDSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract phototransistor series")
        @CsvSource({
                "PT333-3C, PT333",
                "PT908-7C, PT908",
                "PT19-21B/TR8, PT1921"
        })
        void shouldExtractPhototransistorSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract optocoupler series")
        @CsvSource({
                "EL817, EL817",
                "EL817S1-C-TU, EL817",
                "EL357N-C, EL357",
                "EL3063, EL3063"
        })
        void shouldExtractOptocouplerSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract high-power LED series")
        @CsvSource({
                "ELSH-Q61K1-0LPGS, ELSH",
                "SHWO-1CZR-30, SHWO",
                "EASP-KW15-50, EASP"
        })
        void shouldExtractHighPowerLEDSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract light sensor series")
        @CsvSource({
                "ALS-PT19-315C/L177/TR8, ALS-PT19-315"
        })
        void shouldExtractLightSensorSeries(String mpn, String expected) {
            String result = handler.extractSeries(mpn);
            assertEquals(expected, result, "Series for " + mpn);
        }
    }

    // ========================================================================
    // Color Extraction Tests
    // ========================================================================

    @Nested
    @DisplayName("Color Extraction")
    class ColorExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract LED colors")
        @CsvSource({
                "17-21SURC/S530-A3/TR8, Red",
                "17-21SUGC/S530-A2/TR8, Green",
                "17-21SUBC/S530-A3/TR8, Blue",
                "17-21SUYC/S530-A4/TR8, Yellow",
                "17-21SUOC/S530-A3/TR8, Orange",
                "17-21SUWC/S530-W2/TR8, White",
                "19-337/RGB/KHC-A01/2T, RGB"
        })
        void shouldExtractColors(String mpn, String expected) {
            String result = handler.extractColor(mpn);
            assertEquals(expected, result, "Color for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown color codes")
        void shouldReturnEmptyForUnknownColors() {
            assertEquals("", handler.extractColor("IR333-A"));  // IR LEDs don't have visible color
        }
    }

    // ========================================================================
    // Official Replacement Tests
    // ========================================================================

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and color should be official replacement")
        void sameSeriesToSameColorAreReplacement() {
            // Same 17-21 series, same red color, different bins
            assertTrue(handler.isOfficialReplacement(
                            "17-21SURC/S530-A3/TR8",
                            "17-21SURC/S530-A4/TR8"),
                    "Same series and color should be replacements");
        }

        @Test
        @DisplayName("Same series but different color should not be replacement")
        void sameSeriesToDifferentColorNotReplacement() {
            // Same 17-21 series, different colors (red vs green)
            assertFalse(handler.isOfficialReplacement(
                            "17-21SURC/S530-A3/TR8",
                            "17-21SUGC/S530-A2/TR8"),
                    "Different colors should not be replacements");
        }

        @Test
        @DisplayName("Different series should not be official replacement")
        void differentSeriesNotReplacement() {
            // Different series (17-21 vs 19-21)
            assertFalse(handler.isOfficialReplacement(
                            "17-21SURC/S530-A3/TR8",
                            "19-21SURC/S530-A3/TR8"),
                    "Different series should not be replacements");
        }

        @Test
        @DisplayName("Same optocoupler series with different variants should be replacement")
        void sameOptocouplerSeriesReplacement() {
            assertTrue(handler.isOfficialReplacement(
                            "EL817S1-C-TU",
                            "EL817S2-C"),
                    "Same EL817 series optocouplers should be replacements");
        }

        @Test
        @DisplayName("Different optocoupler series should not be replacement")
        void differentOptocouplerSeriesNotReplacement() {
            assertFalse(handler.isOfficialReplacement(
                            "EL817S1-C-TU",
                            "EL357N-C"),
                    "EL817 and EL357 are different series");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsNotReplacement() {
            assertFalse(handler.isOfficialReplacement(null, "17-21SURC/S530-A3/TR8"));
            assertFalse(handler.isOfficialReplacement("17-21SURC/S530-A3/TR8", null));
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
            assertFalse(handler.matches(null, ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractColor(null));
            assertEquals("", handler.getMountingType(null));
            assertEquals("", handler.getProductCategory(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.LED, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractColor(""));
            assertEquals("", handler.getMountingType(""));
            assertEquals("", handler.getProductCategory(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("17-21SURC/S530-A3/TR8", null, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("17-21surc/s530-a3/tr8", ComponentType.LED, registry));
            assertTrue(handler.matches("el817s1-c-tu", ComponentType.IC, registry));
            assertTrue(handler.matches("ir333-a", ComponentType.LED, registry));
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
            assertTrue(types.contains(ComponentType.LED), "Should support LED");
            assertTrue(types.contains(ComponentType.IC), "Should support IC");
            assertTrue(types.contains(ComponentType.OPTOCOUPLER_TOSHIBA), "Should support OPTOCOUPLER_TOSHIBA");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes should return an immutable set");
        }
    }

    // ========================================================================
    // Non-Matching Tests
    // ========================================================================

    @Nested
    @DisplayName("Non-Matching MPNs")
    class NonMatchingTests {

        @ParameterizedTest
        @DisplayName("Should not match non-Everlight components")
        @ValueSource(strings = {
                "WP7113ID",               // Kingbright LED
                "OSRAM-LW",               // OSRAM LED
                "LM7805",                 // TI regulator
                "STM32F103",              // ST microcontroller
                "GRM188R71H104KA93",      // Murata capacitor
                "RC0603FR-0710KL",        // Yageo resistor
                "TLP621",                 // Toshiba optocoupler
                "6N137"                   // Generic optocoupler
        })
        void shouldNotMatchNonEverlightParts(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should NOT match LED for Everlight handler");
        }

        @Test
        @DisplayName("Should not match unrelated component types")
        void shouldNotMatchUnrelatedTypes() {
            assertFalse(handler.matches("17-21SURC/S530-A3/TR8", ComponentType.RESISTOR, registry));
            assertFalse(handler.matches("17-21SURC/S530-A3/TR8", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("17-21SURC/S530-A3/TR8", ComponentType.MICROCONTROLLER, registry));
        }
    }

    // ========================================================================
    // Seven-Segment Display Tests
    // ========================================================================

    @Nested
    @DisplayName("Seven-Segment Display Detection")
    class SevenSegmentDisplayTests {

        @ParameterizedTest
        @DisplayName("Should detect ELS series displays")
        @ValueSource(strings = {
                "ELS-321SURWA",
                "ELS-431SYGWA",
                "ELS-521SURWA"
        })
        void shouldDetectSevenSegmentDisplays(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.LED, registry),
                    mpn + " should match LED");
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Seven-segment displays should be categorized correctly")
        void sevenSegmentDisplaysShouldBeCategorizedCorrectly() {
            assertEquals("Seven-segment Display", handler.getProductCategory("ELS-321SURWA"));
        }
    }
}
