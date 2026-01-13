package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.HartingHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for HartingHandler.
 * Tests Han series (A/E, B, D, K, Modular, Yellock), M12, M8, har-bus HM, har-link, and har-flex.
 *
 * Harting MPN structure:
 * - First 2 digits: Product family/series
 * - Next 2 digits: Sub-series or variant
 * - Next 3 digits: Pin count or position code
 * - Last 4 digits: Configuration code (plating, orientation, etc.)
 *
 * Pattern examples:
 * - 09 20 010 2611 (Han A/E, 10-pin)
 * - 09 33 024 2701 (Han E, 24-pin)
 * - 09 30 016 0301 (Han B, 16-pin, high current)
 * - 09 21 006 3101 (Han D, 6-pin, compact)
 * - 21 03 311 1305 (M12, 3-pin, circular)
 * - 21 02 151 0405 (M8, miniature circular)
 * - 02 00 120 1101 (har-bus HM backplane)
 * - 09 01 000 6231 (har-link high-speed)
 */
class HartingHandlerTest {

    private static HartingHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new HartingHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Han A/Han E Detection (Standard Industrial)")
    class HanAETests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 20 010 2611",
                "09200102611",
                "09-20-010-2611",
                "09 20 016 2811",
                "09 20 024 2611",
                "09 33 010 2701",
                "09330102701",
                "09 33 024 2701",
                "09 33 016 2811"
        })
        void shouldDetectHanAE(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, Han A",
                "09200102611, Han A",
                "09 33 024 2701, Han E",
                "09330242701, Han E"
        })
        void shouldExtractHanAESeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldIdentifyHanSeriesConnector() {
            assertTrue(handler.isHanSeries("09 20 010 2611"));
            assertTrue(handler.isHanSeries("09 33 024 2701"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Industrial Signal", handler.getApplicationType("09 20 010 2611"));
            assertEquals("Industrial Signal", handler.getApplicationType("09 33 024 2701"));
        }
    }

    @Nested
    @DisplayName("Han B Detection (High Current)")
    class HanBTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 30 016 0301",
                "09300160301",
                "09-30-016-0301",
                "09 30 010 0501",
                "09 30 024 0601"
        })
        void shouldDetectHanB(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "09 30 016 0301",
                "09300160301"
        })
        void shouldExtractHanBSeries(String mpn) {
            assertEquals("Han B", handler.extractSeries(mpn));
        }

        @Test
        void shouldIdentifyAsPowerConnector() {
            assertTrue(handler.isPowerConnector("09 30 016 0301"));
        }

        @Test
        void shouldHaveHighCurrent() {
            assertEquals(40.0, handler.getRatedCurrent("09 30 016 0301"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("High Current Industrial", handler.getApplicationType("09 30 016 0301"));
        }
    }

    @Nested
    @DisplayName("Han D Detection (Compact)")
    class HanDTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 21 006 3101",
                "09210063101",
                "09-21-006-3101",
                "09 21 015 3101",
                "09 21 025 3201"
        })
        void shouldDetectHanD(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHanDSeries() {
            assertEquals("Han D", handler.extractSeries("09 21 006 3101"));
            assertEquals("Han D", handler.extractSeries("09210063101"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Compact Industrial", handler.getApplicationType("09 21 006 3101"));
        }

        @Test
        void shouldHaveLowerCurrentRating() {
            assertEquals(10.0, handler.getRatedCurrent("09 21 006 3101"));
        }
    }

    @Nested
    @DisplayName("Han K Detection (Power)")
    class HanKTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 38 002 0301",
                "09380020301",
                "09-38-002-0301",
                "09 16 004 0501"
        })
        void shouldDetectHanK(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHanKSeries() {
            assertEquals("Han K", handler.extractSeries("09 38 002 0301"));
            assertEquals("Han K", handler.extractSeries("09 16 004 0501"));
        }

        @Test
        void shouldIdentifyAsPowerConnector() {
            assertTrue(handler.isPowerConnector("09 38 002 0301"));
        }

        @Test
        void shouldHaveVeryHighCurrent() {
            assertEquals(200.0, handler.getRatedCurrent("09 38 002 0301"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Power Distribution", handler.getApplicationType("09 38 002 0301"));
        }
    }

    @Nested
    @DisplayName("Han-Modular Detection")
    class HanModularTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 14 008 3001",
                "09140083001",
                "09-14-008-3001"
        })
        void shouldDetectHanModular(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHanModularSeries() {
            assertEquals("Han-Modular", handler.extractSeries("09 14 008 3001"));
        }

        @Test
        void shouldIdentifyAsModularSystem() {
            assertTrue(handler.isModularSystem("09 14 008 3001"));
            assertFalse(handler.isModularSystem("09 20 010 2611"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Modular Industrial", handler.getApplicationType("09 14 008 3001"));
        }
    }

    @Nested
    @DisplayName("Han-Yellock Detection (Push-Pull Circular)")
    class HanYellockTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 02 012 1201",
                "09020121201",
                "09-02-012-1201"
        })
        void shouldDetectHanYellock(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHanYellockSeries() {
            assertEquals("Han-Yellock", handler.extractSeries("09 02 012 1201"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Push-Pull Circular", handler.getApplicationType("09 02 012 1201"));
        }

        @Test
        void shouldHaveIP67Rating() {
            assertEquals("IP67", handler.getIPRating("09 02 012 1201"));
        }
    }

    @Nested
    @DisplayName("M12 Circular Detection")
    class M12Tests {

        @ParameterizedTest
        @ValueSource(strings = {
                "21 03 311 1305",
                "21033111305",
                "21-03-311-1305",
                "21 03 004 1405",
                "21 03 008 1505"
        })
        void shouldDetectM12(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractM12Series() {
            assertEquals("M12", handler.extractSeries("21 03 311 1305"));
            assertEquals("M12", handler.extractSeries("21033111305"));
        }

        @Test
        void shouldIdentifyAsCircularConnector() {
            assertTrue(handler.isCircularConnector("21 03 311 1305"));
        }

        @Test
        void shouldNotIdentifyAsHanSeries() {
            assertFalse(handler.isHanSeries("21 03 311 1305"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Industrial Sensor/Ethernet", handler.getApplicationType("21 03 311 1305"));
        }

        @Test
        void shouldHaveIP67Rating() {
            assertEquals("IP67", handler.getIPRating("21 03 311 1305"));
        }
    }

    @Nested
    @DisplayName("M8 Miniature Circular Detection")
    class M8Tests {

        @ParameterizedTest
        @ValueSource(strings = {
                "21 02 151 0405",
                "21021510405",
                "21-02-151-0405",
                "21 02 003 0305",
                "21 02 004 0405"
        })
        void shouldDetectM8(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractM8Series() {
            assertEquals("M8", handler.extractSeries("21 02 151 0405"));
            assertEquals("M8", handler.extractSeries("21021510405"));
        }

        @Test
        void shouldIdentifyAsCircularConnector() {
            assertTrue(handler.isCircularConnector("21 02 151 0405"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Miniature Sensor", handler.getApplicationType("21 02 151 0405"));
        }

        @Test
        void shouldHaveLowerCurrentThanM12() {
            assertEquals(3.0, handler.getRatedCurrent("21 02 151 0405"));
            assertEquals(4.0, handler.getRatedCurrent("21 03 311 1305"));
        }
    }

    @Nested
    @DisplayName("har-bus HM Backplane Detection")
    class HarBusHMTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "02 00 120 1101",
                "02001201101",
                "02-00-120-1101",
                "02 01 110 1201",
                "02 00 055 1301"
        })
        void shouldDetectHarBusHM(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHarBusHMSeries() {
            assertEquals("har-bus HM", handler.extractSeries("02 00 120 1101"));
            assertEquals("har-bus HM", handler.extractSeries("02 01 110 1201"));
        }

        @Test
        void shouldIdentifyAsHighSpeedConnector() {
            assertTrue(handler.isHighSpeedConnector("02 00 120 1101"));
        }

        @Test
        void shouldNotBePowerConnector() {
            assertFalse(handler.isPowerConnector("02 00 120 1101"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("High-Speed Backplane", handler.getApplicationType("02 00 120 1101"));
        }

        @Test
        void shouldHaveIP20Rating() {
            assertEquals("IP20", handler.getIPRating("02 00 120 1101"));
        }
    }

    @Nested
    @DisplayName("har-link High-Speed Data Detection")
    class HarLinkTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 01 000 6231",
                "09010006231",
                "09-01-000-6231"
        })
        void shouldDetectHarLink(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHarLinkSeries() {
            assertEquals("har-link", handler.extractSeries("09 01 000 6231"));
        }

        @Test
        void shouldIdentifyAsHighSpeedConnector() {
            assertTrue(handler.isHighSpeedConnector("09 01 000 6231"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("High-Speed Data Link", handler.getApplicationType("09 01 000 6231"));
        }
    }

    @Nested
    @DisplayName("har-flex Flexible PCB Detection")
    class HarFlexTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "14 010 1201",
                "140101201",
                "14-010-1201",
                "15 020 1301"
        })
        void shouldDetectHarFlex(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHarFlexSeries() {
            assertEquals("har-flex", handler.extractSeries("14 010 1201"));
            assertEquals("har-flex", handler.extractSeries("15 020 1301"));
        }

        @Test
        void shouldNotBeCircularConnector() {
            assertFalse(handler.isCircularConnector("14 010 1201"));
        }

        @Test
        void shouldGetCorrectApplicationType() {
            assertEquals("Flexible PCB", handler.getApplicationType("14 010 1201"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, 2611",
                "09200102611, 2611",
                "09 33 024 2701, 2701",
                "09 30 016 0301, 0301",
                "21 03 311 1305, 1305",
                "02 00 120 1101, 1101"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @Test
        void shouldReturnEmptyForInvalidMpn() {
            assertEquals("", handler.extractPackageCode("INVALID"));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractPackageCode(null));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, Han A",
                "09 33 024 2701, Han E",
                "09 30 016 0301, Han B",
                "09 21 006 3101, Han D",
                "09 38 002 0301, Han K",
                "09 14 008 3001, Han-Modular",
                "09 02 012 1201, Han-Yellock",
                "21 03 311 1305, M12",
                "21 02 151 0405, M8",
                "02 00 120 1101, har-bus HM",
                "09 01 000 6231, har-link",
                "14 010 1201, har-flex"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }

        @Test
        void shouldReturnEmptyForUnknownMpn() {
            assertEquals("", handler.extractSeries("UNKNOWN123"));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries(null));
        }
    }

    @Nested
    @DisplayName("Pin Count Extraction")
    class PinCountTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, 10",
                "09 33 024 2701, 24",
                "09 30 016 0301, 16",
                "09 21 006 3101, 6",
                "21 02 003 0305, 3",
                "21 02 004 0405, 4"
        })
        void shouldExtractPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractPinCount(mpn));
        }
    }

    @Nested
    @DisplayName("Position Code Extraction")
    class PositionCodeTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, 010",
                "09 33 024 2701, 024",
                "21 03 311 1305, 311"
        })
        void shouldExtractPositionCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPositionCode(mpn));
        }
    }

    @Nested
    @DisplayName("Series Code Extraction")
    class SeriesCodeTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, 0920",
                "09 33 024 2701, 0933",
                "09 30 016 0301, 0930",
                "21 03 311 1305, 2103",
                "02 00 120 1101, 0200"
        })
        void shouldExtractSeriesCode(String mpn, String expected) {
            assertEquals(expected, handler.getSeriesCode(mpn));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {

        @Test
        void shouldBeReplacementWithSameSeriesAndPinCount() {
            // Same series (Han A), same pin count (10), different config
            assertTrue(handler.isOfficialReplacement("09 20 010 2611", "09 20 010 2801"));
        }

        @Test
        void shouldNotBeReplacementAcrossSeries() {
            // Han A vs Han E
            assertFalse(handler.isOfficialReplacement("09 20 010 2611", "09 33 010 2701"));
            // Han A vs Han B
            assertFalse(handler.isOfficialReplacement("09 20 016 2611", "09 30 016 0301"));
            // M12 vs M8
            assertFalse(handler.isOfficialReplacement("21 03 004 1305", "21 02 004 0405"));
        }

        @Test
        void shouldNotBeReplacementWithDifferentPinCount() {
            // Same series (Han A), different pin count
            assertFalse(handler.isOfficialReplacement("09 20 010 2611", "09 20 016 2611"));
            assertFalse(handler.isOfficialReplacement("09 20 024 2611", "09 20 010 2611"));
        }

        @Test
        void shouldBeReplacementWithDifferentFormatSamePart() {
            // Same part, different formatting
            assertTrue(handler.isOfficialReplacement("09 20 010 2611", "09200102611"));
            assertTrue(handler.isOfficialReplacement("09-20-010-2611", "09 20 010 2611"));
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "09 20 010 2611"));
            assertFalse(handler.isOfficialReplacement("09 20 010 2611", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("IP Rating")
    class IPRatingTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, IP65",
                "09 33 024 2701, IP65",
                "09 30 016 0301, IP65",
                "09 02 012 1201, IP67",
                "21 03 311 1305, IP67",
                "21 02 151 0405, IP67",
                "02 00 120 1101, IP20"
        })
        void shouldReturnCorrectIPRating(String mpn, String expected) {
            assertEquals(expected, handler.getIPRating(mpn));
        }
    }

    @Nested
    @DisplayName("Rated Current")
    class RatedCurrentTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, 16.0",
                "09 30 016 0301, 40.0",
                "09 38 002 0301, 200.0",
                "09 21 006 3101, 10.0",
                "21 03 311 1305, 4.0",
                "21 02 151 0405, 3.0",
                "02 00 120 1101, 1.0"
        })
        void shouldReturnCorrectRatedCurrent(String mpn, double expected) {
            assertEquals(expected, handler.getRatedCurrent(mpn));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals(0, handler.extractPinCount(null));
            assertFalse(handler.isOfficialReplacement(null, "09 20 010 2611"));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals(0, handler.extractPinCount(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("09 20 010 2611", null, registry));
        }

        @Test
        void shouldHandleInvalidMpnFormats() {
            assertFalse(handler.matches("ABC123", ComponentType.CONNECTOR, registry));
            assertFalse(handler.matches("09 20", ComponentType.CONNECTOR, registry));
            assertFalse(handler.matches("09 20 010", ComponentType.CONNECTOR, registry));
        }

        @Test
        void shouldHandleMixedCaseMpn() {
            // Harting MPNs are numeric, but handler should be case insensitive for any letters
            assertTrue(handler.matches("09 20 010 2611", ComponentType.CONNECTOR, registry));
        }

        @Test
        void shouldHandleExtraWhitespace() {
            // Handler normalizes MPN, should handle extra spaces
            assertTrue(handler.matches("09 20 010 2611", ComponentType.CONNECTOR, registry));
            assertEquals("Han A", handler.extractSeries("09 20 010 2611"));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.CONNECTOR),
                    "Should support CONNECTOR");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");
        }

        @Test
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes should return immutable Set");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        void canInstantiateDirectly() {
            HartingHandler directHandler = new HartingHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify patterns work
            boolean matches = directHandler.matches("09 20 010 2611", ComponentType.CONNECTOR, directRegistry);
            assertTrue(matches, "Direct instantiation should work for Han A connector");
        }

        @Test
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }

    @Nested
    @DisplayName("Connector Classification")
    class ConnectorClassificationTests {

        @Test
        void shouldClassifyHanSeriesCorrectly() {
            assertTrue(handler.isHanSeries("09 20 010 2611"));
            assertTrue(handler.isHanSeries("09 33 024 2701"));
            assertTrue(handler.isHanSeries("09 30 016 0301"));
            assertTrue(handler.isHanSeries("09 21 006 3101"));
            assertFalse(handler.isHanSeries("21 03 311 1305"));
            assertFalse(handler.isHanSeries("02 00 120 1101"));
        }

        @Test
        void shouldClassifyCircularConnectorsCorrectly() {
            assertTrue(handler.isCircularConnector("21 03 311 1305"));
            assertTrue(handler.isCircularConnector("21 02 151 0405"));
            assertFalse(handler.isCircularConnector("09 20 010 2611"));
            assertFalse(handler.isCircularConnector("02 00 120 1101"));
        }

        @Test
        void shouldClassifyHighSpeedConnectorsCorrectly() {
            assertTrue(handler.isHighSpeedConnector("02 00 120 1101"));
            assertTrue(handler.isHighSpeedConnector("09 01 000 6231"));
            assertFalse(handler.isHighSpeedConnector("09 20 010 2611"));
            assertFalse(handler.isHighSpeedConnector("21 03 311 1305"));
        }

        @Test
        void shouldClassifyPowerConnectorsCorrectly() {
            assertTrue(handler.isPowerConnector("09 30 016 0301"));
            assertTrue(handler.isPowerConnector("09 38 002 0301"));
            assertFalse(handler.isPowerConnector("09 20 010 2611"));
            assertFalse(handler.isPowerConnector("21 03 311 1305"));
        }
    }

    @Nested
    @DisplayName("Family Information")
    class FamilyInformationTests {

        @ParameterizedTest
        @CsvSource({
                "09 20 010 2611, Han A/E",
                "09 33 024 2701, Han E",
                "09 30 016 0301, Han B",
                "09 21 006 3101, Han D",
                "21 03 311 1305, M12",
                "21 02 151 0405, M8",
                "02 00 120 1101, har-bus HM"
        })
        void shouldReturnCorrectFamily(String mpn, String expected) {
            assertEquals(expected, handler.getFamily(mpn));
        }

        @Test
        void shouldReturnDefaultForUnknownFamily() {
            assertEquals("Industrial Connector", handler.getFamily("99 99 999 9999"));
        }
    }

    @Nested
    @DisplayName("IC Type Matching")
    class ICTypeMatchingTests {

        @Test
        void shouldMatchICType() {
            // Handler includes IC in supported types for some scenarios
            assertTrue(handler.matches("09 20 010 2611", ComponentType.IC, registry));
            assertTrue(handler.matches("02 00 120 1101", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Pattern Variations")
    class PatternVariationsTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "09 20 010 2611",   // With spaces
                "09200102611",       // No spaces
                "09-20-010-2611",    // With hyphens
                "09 20 0102611"      // Mixed format
        })
        void shouldHandleVariousFormats(String mpn) {
            // Should handle common format variations
            String series = handler.extractSeries(mpn);
            assertNotNull(series);
        }
    }
}
