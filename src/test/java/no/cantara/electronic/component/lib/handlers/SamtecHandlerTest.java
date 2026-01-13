package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SamtecHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for SamtecHandler.
 * Tests high-speed connector series from Samtec:
 * - LSHM series (high-speed micro headers, 0.50mm pitch)
 * - SEAM series (card edge connectors, 1.27mm pitch)
 * - HSEC8 series (high-speed edge card, 0.80mm pitch)
 * - QSH/QTH series (high-speed terminal strips, 0.635mm pitch)
 * - TFM/TSM series (terminal strips, 1.27mm pitch)
 * - SSW/TSW series (through-hole headers, 2.54mm pitch)
 *
 * MPN format: SERIES-PINS-PITCH-MOUNT-OPTIONS
 * Examples:
 * - LSHM-110-02.5-L-DV-A-S-K-TR
 * - SEAM-50-03.0-L-10-2-A-K
 * - HSEC8-120-01-L-DV-A
 * - QSH-030-01-L-D-A
 * - TSW-110-01-L-S
 */
class SamtecHandlerTest {

    private static SamtecHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SamtecHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("LSHM Series Detection (High-Speed Micro Headers)")
    class LSHMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "LSHM-110-02.5-L-DV-A-S-K-TR",
            "LSHM-120-02.5-L-DV-A-K",
            "LSHM-150-02.5-L-DV-A",
            "LSHM-140-02.5-L-DV",
            "LSHM-160-02.5-L-S",
            "LSHM-180-02.5-L-D-A-K-TR"
        })
        void shouldMatchLSHMConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "LSHM-110-02.5-L-DV-A-S-K-TR",
            "LSHM-120-02.5-L-DV-A-K"
        })
        void shouldMatchLSHMAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        void shouldExtractLSHMPinCount() {
            assertEquals(110, handler.extractPinCount("LSHM-110-02.5-L-DV-A-S-K-TR"));
            assertEquals(120, handler.extractPinCount("LSHM-120-02.5-L-DV-A-K"));
            assertEquals(150, handler.extractPinCount("LSHM-150-02.5-L-DV-A"));
        }

        @Test
        void shouldExtractLSHMPitch() {
            assertEquals("02.5", handler.getPitch("LSHM-110-02.5-L-DV-A"));
        }
    }

    @Nested
    @DisplayName("SEAM Series Detection (Card Edge)")
    class SEAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "SEAM-50-03.0-L-10-2-A-K",
            "SEAM-40-03.0-L-08-2-A",
            "SEAM-60-03.0-L-12-2-A-K-TR",
            "SEAM-80-03.0-L-16-2-A"
        })
        void shouldMatchSEAMConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSEAMPinCount() {
            assertEquals(50, handler.extractPinCount("SEAM-50-03.0-L-10-2-A-K"));
            assertEquals(40, handler.extractPinCount("SEAM-40-03.0-L-08-2-A"));
        }

        @Test
        void shouldIdentifyCardEdgeMounting() {
            assertEquals("Card Edge", handler.getMountingType("SEAM-50-03.0-L-10-2-A-K"));
        }
    }

    @Nested
    @DisplayName("HSEC8 Series Detection (High-Speed Edge Card)")
    class HSEC8Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "HSEC8-120-01-L-DV-A",
            "HSEC8-140-01-L-DV-A-K",
            "HSEC8-160-01-L-DV-A-K-TR",
            "HSEC8-180-01-L-S-A"
        })
        void shouldMatchHSEC8Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractHSEC8PinCount() {
            assertEquals(120, handler.extractPinCount("HSEC8-120-01-L-DV-A"));
            assertEquals(140, handler.extractPinCount("HSEC8-140-01-L-DV-A-K"));
        }

        @Test
        void shouldBeHighSpeed() {
            assertTrue(handler.isHighSpeed("HSEC8-120-01-L-DV-A"));
        }
    }

    @Nested
    @DisplayName("QSH/QTH Series Detection (High-Speed Terminal)")
    class QSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "QSH-030-01-L-D-A",
            "QSH-060-01-L-D-A-K",
            "QSH-090-01-L-D-A-K-TR",
            "QSH-120-01-L-S-A"
        })
        void shouldMatchQSHConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "QTH-030-01-L-D-A",
            "QTH-060-01-L-D-A-K",
            "QTH-090-01-L-D-A-K-TR",
            "QTH-120-01-L-S-A"
        })
        void shouldMatchQTHConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractQSeriesPinCount() {
            assertEquals(30, handler.extractPinCount("QSH-030-01-L-D-A"));
            assertEquals(60, handler.extractPinCount("QTH-060-01-L-D-A-K"));
        }

        @Test
        void shouldIdentifyAsHighSpeed() {
            assertTrue(handler.isHighSpeed("QSH-030-01-L-D-A"));
            assertTrue(handler.isHighSpeed("QTH-030-01-L-D-A"));
        }

        @Test
        void shouldDefaultToDualRow() {
            assertEquals("Dual Row", handler.getRowConfiguration("QSH-030-01-L-D-A"));
            assertEquals("Dual Row", handler.getRowConfiguration("QTH-030-01-L-D-A"));
        }
    }

    @Nested
    @DisplayName("TFM/TSM Series Detection (Terminal Strips)")
    class TerminalStripTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "TFM-110-01-L-D",
            "TFM-120-01-L-S",
            "TFM-140-01-L-D-A",
            "TFM-150-02-L-D-K"
        })
        void shouldMatchTFMConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "TSM-110-01-L-DV",
            "TSM-120-01-L-SV",
            "TSM-140-01-L-DV-A",
            "TSM-150-02-L-DV-K"
        })
        void shouldMatchTSMConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractTerminalStripPinCount() {
            assertEquals(110, handler.extractPinCount("TFM-110-01-L-D"));
            assertEquals(120, handler.extractPinCount("TSM-120-01-L-DV"));
        }

        @Test
        void shouldNotBeHighSpeed() {
            assertFalse(handler.isHighSpeed("TFM-110-01-L-D"));
            assertFalse(handler.isHighSpeed("TSM-110-01-L-DV"));
        }
    }

    @Nested
    @DisplayName("SSW/TSW Series Detection (Through-Hole Headers)")
    class ThroughHoleTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "SSW-110-01-L-S",
            "SSW-120-01-L-D",
            "SSW-140-02-L-S-K",
            "SSW-150-01-L-S-TR"
        })
        void shouldMatchSSWConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "TSW-110-01-L-S",
            "TSW-120-01-L-D",
            "TSW-140-02-L-S-K",
            "TSW-150-01-L-S-TR"
        })
        void shouldMatchTSWConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractThroughHolePinCount() {
            assertEquals(110, handler.extractPinCount("SSW-110-01-L-S"));
            assertEquals(120, handler.extractPinCount("TSW-120-01-L-D"));
        }

        @Test
        void shouldIdentifyAsThroughHole() {
            assertEquals("THT", handler.getMountingType("SSW-110-01-L-S"));
            assertEquals("THT", handler.getMountingType("TSW-110-01-L-S"));
        }

        @Test
        void shouldNotBeHighSpeed() {
            assertFalse(handler.isHighSpeed("SSW-110-01-L-S"));
            assertFalse(handler.isHighSpeed("TSW-110-01-L-S"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "LSHM-110-02.5-L-DV-A, LSHM",
            "SEAM-50-03.0-L-10-2-A-K, SEAM",
            "HSEC8-120-01-L-DV-A, HSEC8",
            "QSH-030-01-L-D-A, QSH",
            "QTH-030-01-L-D-A, QTH",
            "TFM-110-01-L-D, TFM",
            "TSM-110-01-L-DV, TSM",
            "SSW-110-01-L-S, SSW",
            "TSW-110-01-L-S, TSW"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }

        @Test
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.extractSeries("UNKNOWN-123"));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries(null));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "LSHM-110-02.5-L-DV-A, L-DV-A",
            "SEAM-50-03.0-L-10-2-A-K, L-10-2-A-K",
            "SSW-110-01-L-S, L-S",
            "TSW-110-01-L-D, L-D"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @Test
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractPackageCode("INVALID"));
        }
    }

    @Nested
    @DisplayName("Pin Count Extraction")
    class PinCountTests {
        @ParameterizedTest
        @CsvSource({
            "LSHM-110-02.5-L-DV-A, 110",
            "SEAM-50-03.0-L-10-2-A-K, 50",
            "HSEC8-120-01-L-DV-A, 120",
            "QSH-030-01-L-D-A, 30",
            "QTH-060-01-L-D-A, 60",
            "TFM-110-01-L-D, 110",
            "TSM-120-01-L-DV, 120",
            "SSW-140-01-L-S, 140",
            "TSW-150-01-L-D, 150"
        })
        void shouldExtractPinCount(String mpn, int expected) {
            assertEquals(expected, handler.extractPinCount(mpn));
        }

        @Test
        void shouldReturnZeroForInvalidMPN() {
            assertEquals(0, handler.extractPinCount(null));
            assertEquals(0, handler.extractPinCount(""));
            assertEquals(0, handler.extractPinCount("INVALID"));
        }
    }

    @Nested
    @DisplayName("Pitch Extraction")
    class PitchTests {
        @Test
        void shouldExtractPitchFromMPN() {
            assertEquals("02.5", handler.getPitch("LSHM-110-02.5-L-DV-A"));
            assertEquals("03.0", handler.getPitch("SEAM-50-03.0-L-10-2-A-K"));
            assertEquals("01", handler.getPitch("HSEC8-120-01-L-DV-A"));
        }
    }

    @Nested
    @DisplayName("Mounting Type Detection")
    class MountingTypeTests {
        @Test
        void shouldIdentifySMTConnectors() {
            assertEquals("SMT", handler.getMountingType("LSHM-110-02.5-L-DV-A"));
            assertEquals("SMT", handler.getMountingType("HSEC8-120-01-L-DV-A"));
            assertEquals("SMT", handler.getMountingType("QSH-030-01-L-D-A"));
            assertEquals("SMT", handler.getMountingType("QTH-030-01-L-D-A"));
        }

        @Test
        void shouldIdentifyTHTConnectors() {
            assertEquals("THT", handler.getMountingType("SSW-110-01-L-S"));
            assertEquals("THT", handler.getMountingType("TSW-110-01-L-S"));
        }

        @Test
        void shouldIdentifyCardEdge() {
            assertEquals("Card Edge", handler.getMountingType("SEAM-50-03.0-L-10-2-A-K"));
        }
    }

    @Nested
    @DisplayName("Row Configuration")
    class RowConfigurationTests {
        @Test
        void shouldIdentifyDualRow() {
            assertEquals("Dual Row", handler.getRowConfiguration("LSHM-110-02.5-L-DV-A"));
            assertEquals("Dual Row", handler.getRowConfiguration("QSH-030-01-L-D-A"));
        }

        @Test
        void shouldIdentifySingleRow() {
            assertEquals("Single Row", handler.getRowConfiguration("SSW-110-01-L-S"));
            assertEquals("Single Row", handler.getRowConfiguration("TSW-110-01-L-S"));
        }
    }

    @Nested
    @DisplayName("High-Speed Detection")
    class HighSpeedTests {
        @Test
        void shouldIdentifyHighSpeedSeries() {
            assertTrue(handler.isHighSpeed("LSHM-110-02.5-L-DV-A"));
            assertTrue(handler.isHighSpeed("HSEC8-120-01-L-DV-A"));
            assertTrue(handler.isHighSpeed("QSH-030-01-L-D-A"));
            assertTrue(handler.isHighSpeed("QTH-030-01-L-D-A"));
        }

        @Test
        void shouldIdentifyNonHighSpeedSeries() {
            assertFalse(handler.isHighSpeed("SEAM-50-03.0-L-10-2-A-K"));
            assertFalse(handler.isHighSpeed("TFM-110-01-L-D"));
            assertFalse(handler.isHighSpeed("TSM-110-01-L-DV"));
            assertFalse(handler.isHighSpeed("SSW-110-01-L-S"));
            assertFalse(handler.isHighSpeed("TSW-110-01-L-S"));
        }
    }

    @Nested
    @DisplayName("Rated Current")
    class RatedCurrentTests {
        @Test
        void shouldGetRatedCurrent() {
            assertEquals(1.7, handler.getRatedCurrent("LSHM-110-02.5-L-DV-A"));
            assertEquals(1.5, handler.getRatedCurrent("SEAM-50-03.0-L-10-2-A-K"));
            assertEquals(1.8, handler.getRatedCurrent("HSEC8-120-01-L-DV-A"));
            assertEquals(2.3, handler.getRatedCurrent("QSH-030-01-L-D-A"));
            assertEquals(2.3, handler.getRatedCurrent("QTH-030-01-L-D-A"));
            assertEquals(2.1, handler.getRatedCurrent("TFM-110-01-L-D"));
            assertEquals(2.3, handler.getRatedCurrent("TSM-110-01-L-DV"));
            assertEquals(3.0, handler.getRatedCurrent("SSW-110-01-L-S"));
            assertEquals(3.0, handler.getRatedCurrent("TSW-110-01-L-S"));
        }

        @Test
        void shouldReturnZeroForUnknown() {
            assertEquals(0.0, handler.getRatedCurrent("UNKNOWN-123"));
        }
    }

    @Nested
    @DisplayName("Series Description")
    class SeriesDescriptionTests {
        @ParameterizedTest
        @CsvSource({
            "LSHM-110-02.5-L-DV-A, High-Speed Micro Headers",
            "SEAM-50-03.0-L-10-2-A-K, Card Edge Connectors",
            "HSEC8-120-01-L-DV-A, High-Speed Edge Card",
            "QSH-030-01-L-D-A, High-Speed Terminal Strip (Socket)",
            "QTH-030-01-L-D-A, High-Speed Terminal Strip (Header)",
            "TFM-110-01-L-D, Terminal Strip (Female)",
            "TSM-110-01-L-DV, Tiger Eye Terminal Strip",
            "SSW-110-01-L-S, Through-Hole Socket Strip",
            "TSW-110-01-L-S, Through-Hole Terminal Strip"
        })
        void shouldGetSeriesDescription(String mpn, String expected) {
            assertEquals(expected, handler.getSeriesDescription(mpn));
        }
    }

    @Nested
    @DisplayName("Application Type")
    class ApplicationTypeTests {
        @ParameterizedTest
        @CsvSource({
            "LSHM-110-02.5-L-DV-A, High-Speed Board-to-Board",
            "SEAM-50-03.0-L-10-2-A-K, Card Edge",
            "HSEC8-120-01-L-DV-A, High-Speed Edge Card",
            "QSH-030-01-L-D-A, High-Speed Terminal",
            "QTH-030-01-L-D-A, High-Speed Terminal",
            "TFM-110-01-L-D, General Purpose Terminal Strip",
            "TSM-110-01-L-DV, General Purpose Terminal Strip",
            "SSW-110-01-L-S, Through-Hole Header/Socket",
            "TSW-110-01-L-S, Through-Hole Header/Socket"
        })
        void shouldGetApplicationType(String mpn, String expected) {
            assertEquals(expected, handler.getApplicationType(mpn));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("LSHM-110-02.5-L-DV-A", "HSEC8-110-01-L-DV-A"));
            assertFalse(handler.isOfficialReplacement("QSH-030-01-L-D-A", "QTH-030-01-L-D-A"));
            assertFalse(handler.isOfficialReplacement("SSW-110-01-L-S", "TSW-110-01-L-S"));
        }

        @Test
        void shouldNotReplaceWithDifferentPinCount() {
            assertFalse(handler.isOfficialReplacement("LSHM-110-02.5-L-DV-A", "LSHM-120-02.5-L-DV-A"));
            assertFalse(handler.isOfficialReplacement("SSW-110-01-L-S", "SSW-120-01-L-S"));
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "LSHM-110-02.5-L-DV-A"));
            assertFalse(handler.isOfficialReplacement("LSHM-110-02.5-L-DV-A", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }

        @Test
        void shouldAllowSameMountingTypeReplacement() {
            // Same series, same pin count, same mounting type
            assertTrue(handler.isOfficialReplacement("SSW-110-01-L-S", "SSW-110-02-L-S"));
            assertTrue(handler.isOfficialReplacement("LSHM-110-02.5-L-DV-A", "LSHM-110-02.5-L-DV-K"));
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
            assertEquals("", handler.getPitch(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals(0, handler.extractPinCount(""));
            assertEquals("", handler.getPitch(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("LSHM-110-02.5-L-DV-A", null, registry));
        }

        @Test
        void shouldNotMatchUnknownMPN() {
            assertFalse(handler.matches("UNKNOWN-123-ABC", ComponentType.CONNECTOR, registry));
            assertFalse(handler.matches("XYZ-110-01", ComponentType.CONNECTOR, registry));
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
        void shouldHaveExactlyTwoTypes() {
            var types = handler.getSupportedTypes();
            assertEquals(2, types.size(), "Should support exactly 2 types");
        }

        @Test
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {
        @Test
        void canInstantiateDirectly() {
            SamtecHandler directHandler = new SamtecHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("LSHM-110-02.5-L-DV-A", ComponentType.CONNECTOR, directRegistry));
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
    @DisplayName("Orientation Detection")
    class OrientationTests {
        @Test
        void shouldDefaultToVertical() {
            assertEquals("Vertical", handler.getOrientation("LSHM-110-02.5-L-DV-A"));
            assertEquals("Vertical", handler.getOrientation("SSW-110-01-L-S"));
        }

        @Test
        void documentOrientationDetection() {
            // Document how orientation is detected
            String[] mpns = {"LSHM-110-02.5-L-DV-A", "SSW-110-01-L-S-RA"};
            for (String mpn : mpns) {
                System.out.println(mpn + " orientation = " + handler.getOrientation(mpn));
            }
        }
    }

    @Nested
    @DisplayName("Case Sensitivity")
    class CaseSensitivityTests {
        @Test
        void shouldMatchLowercaseMPN() {
            assertTrue(handler.matches("lshm-110-02.5-l-dv-a", ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches("ssw-110-01-l-s", ComponentType.CONNECTOR, registry));
        }

        @Test
        void shouldMatchMixedCaseMPN() {
            assertTrue(handler.matches("Lshm-110-02.5-L-Dv-A", ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches("Ssw-110-01-L-S", ComponentType.CONNECTOR, registry));
        }

        @Test
        void shouldExtractSeriesFromLowercase() {
            assertEquals("LSHM", handler.extractSeries("lshm-110-02.5-l-dv-a"));
            assertEquals("SSW", handler.extractSeries("ssw-110-01-l-s"));
        }
    }
}
