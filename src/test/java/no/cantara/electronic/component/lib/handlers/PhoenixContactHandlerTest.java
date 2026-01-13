package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.PhoenixContactHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for PhoenixContactHandler.
 *
 * Tests Phoenix Contact industrial connector series:
 * - MC/MCV - COMBICON pluggable connectors
 * - MSTB/MSTBA - Standard pitch pluggable connectors
 * - PT - Push-in terminal blocks
 * - PTSM - SMD terminal blocks
 * - SPT - Spring-cage PCB connectors
 * - UK - Screw terminal blocks
 * - UT - Through-wall terminal blocks
 * - FK-MCP - High current connectors
 * - FRONT-MC - Front connectors
 * - PC - PCB terminal blocks
 *
 * MPN Format Examples:
 * - MC 1,5/3-ST-3,81 (series current/pins-type-pitch)
 * - MSTB 2,5/4-ST-5,08
 * - PT 1,5/2-PH-3,5
 * - PTSM 0,5/3-HH-2,5-SMD
 * - UK 5-TWIN
 */
class PhoenixContactHandlerTest {

    private static PhoenixContactHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new PhoenixContactHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MC/MCV COMBICON Pluggable Connector Detection")
    class MCMCVTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "MC 1,5/3-ST-3,81",
                "MC 1,5/4-ST-3,81",
                "MC 1,5/6-ST-3,81",
                "MC 1,5/8-ST-3,81",
                "MC 1,5/10-ST-3,81",
                "MC 1,5/12-ST-3,81"
        })
        void shouldDetectMCPluggableConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "MCV 1,5/2-G-3,81",
                "MCV 1,5/3-G-3,81",
                "MCV 1,5/4-G-3,81",
                "MCV 1,5/6-G-3,81",
                "MCV 1,5/8-GF-3,81"
        })
        void shouldDetectMCVHeaderConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromMC() {
            assertEquals("MC", handler.extractSeries("MC 1,5/3-ST-3,81"));
            assertEquals("MCV", handler.extractSeries("MCV 1,5/4-G-3,81"));
        }

        @Test
        void shouldExtractPinCountFromMC() {
            assertEquals(3, handler.extractPinCount("MC 1,5/3-ST-3,81"));
            assertEquals(4, handler.extractPinCount("MCV 1,5/4-G-3,81"));
            assertEquals(10, handler.extractPinCount("MC 1,5/10-ST-3,81"));
        }

        @Test
        void shouldExtractPitchFromMC() {
            assertEquals("3.81", handler.extractPitch("MC 1,5/3-ST-3,81"));
            assertEquals("3.81", handler.extractPitch("MCV 1,5/4-G-3,81"));
        }

        @Test
        void shouldIdentifyAsPluggable() {
            assertTrue(handler.isPluggable("MC 1,5/3-ST-3,81"));
            assertTrue(handler.isPluggable("MCV 1,5/4-G-3,81"));
        }
    }

    @Nested
    @DisplayName("MSTB/MSTBA Standard Pitch Connector Detection")
    class MSTBTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "MSTB 2,5/2-ST-5,08",
                "MSTB 2,5/3-ST-5,08",
                "MSTB 2,5/4-ST-5,08",
                "MSTB 2,5/6-ST-5,08",
                "MSTB 2,5/8-STF-5,08"
        })
        void shouldDetectMSTBPluggableConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "MSTBA 2,5/2-G-5,08",
                "MSTBA 2,5/4-G-5,08",
                "MSTBA 2,5/6-GF-5,08"
        })
        void shouldDetectMSTBAHeaderConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromMSTB() {
            assertEquals("MSTB", handler.extractSeries("MSTB 2,5/4-ST-5,08"));
            assertEquals("MSTBA", handler.extractSeries("MSTBA 2,5/4-G-5,08"));
        }

        @Test
        void shouldExtractPinCountFromMSTB() {
            assertEquals(4, handler.extractPinCount("MSTB 2,5/4-ST-5,08"));
            assertEquals(6, handler.extractPinCount("MSTBA 2,5/6-G-5,08"));
        }

        @Test
        void shouldExtractPitchFromMSTB() {
            assertEquals("5.08", handler.extractPitch("MSTB 2,5/4-ST-5,08"));
            assertEquals("5.08", handler.extractPitch("MSTBA 2,5/6-G-5,08"));
        }
    }

    @Nested
    @DisplayName("PT Push-In Terminal Block Detection")
    class PTTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "PT 1,5/2-PH-3,5",
                "PT 1,5/3-PH-3,5",
                "PT 1,5/4-PH-3,5",
                "PT 1,5/6-PH-3,5",
                "PT 2,5/2-PH-5,0",
                "PT 2,5/4-PH-5,0"
        })
        void shouldDetectPTPushInTerminals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromPT() {
            assertEquals("PT", handler.extractSeries("PT 1,5/2-PH-3,5"));
        }

        @Test
        void shouldExtractPinCountFromPT() {
            assertEquals(2, handler.extractPinCount("PT 1,5/2-PH-3,5"));
            assertEquals(4, handler.extractPinCount("PT 1,5/4-PH-3,5"));
        }

        @Test
        void shouldExtractPitchFromPT() {
            assertEquals("3.5", handler.extractPitch("PT 1,5/2-PH-3,5"));
            assertEquals("5.0", handler.extractPitch("PT 2,5/4-PH-5,0"));
        }

        @Test
        void shouldIdentifyAsTerminalBlock() {
            assertTrue(handler.isTerminalBlock("PT 1,5/2-PH-3,5"));
            assertFalse(handler.isPluggable("PT 1,5/2-PH-3,5"));
        }
    }

    @Nested
    @DisplayName("UK Screw Terminal Block Detection")
    class UKTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "UK 5-TWIN",
                "UK 5 N",
                "UK 2,5 N",
                "UK 3-TWIN",
                "UK 6 N",
                "UK 10 N"
        })
        void shouldDetectUKScrewTerminals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromUK() {
            assertEquals("UK", handler.extractSeries("UK 5-TWIN"));
            assertEquals("UK", handler.extractSeries("UK 2,5 N"));
        }

        @Test
        void shouldIdentifyAsTerminalBlock() {
            assertTrue(handler.isTerminalBlock("UK 5-TWIN"));
        }

        @Test
        void shouldGetFamilyForUK() {
            assertEquals("Screw Terminal Block", handler.getFamily("UK 5-TWIN"));
        }
    }

    @Nested
    @DisplayName("PTSM SMD Terminal Block Detection")
    class PTSMTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "PTSM 0,5/2-HH-2,5-SMD",
                "PTSM 0,5/3-HH-2,5-SMD",
                "PTSM 0,5/4-HH-2,5-SMD",
                "PTSM 0,5/6-HH-2,5-SMD",
                "PTSM 0,5/2-H-2,5-SMD"
        })
        void shouldDetectPTSMSMDTerminals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromPTSM() {
            assertEquals("PTSM", handler.extractSeries("PTSM 0,5/3-HH-2,5-SMD"));
        }

        @Test
        void shouldExtractPinCountFromPTSM() {
            assertEquals(3, handler.extractPinCount("PTSM 0,5/3-HH-2,5-SMD"));
            assertEquals(4, handler.extractPinCount("PTSM 0,5/4-HH-2,5-SMD"));
        }

        @Test
        void shouldExtractPitchFromPTSM() {
            assertEquals("2.5", handler.extractPitch("PTSM 0,5/3-HH-2,5-SMD"));
        }

        @Test
        void shouldIdentifyAsSMD() {
            assertTrue(handler.isSMD("PTSM 0,5/3-HH-2,5-SMD"));
            assertEquals("SMD", handler.getMountingType("PTSM 0,5/3-HH-2,5-SMD"));
        }

        @Test
        void shouldIdentifyAsTerminalBlock() {
            assertTrue(handler.isTerminalBlock("PTSM 0,5/3-HH-2,5-SMD"));
        }
    }

    @Nested
    @DisplayName("SPT Spring-Cage Connector Detection")
    class SPTTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "SPT 2,5/2-V-5,0",
                "SPT 2,5/3-V-5,0",
                "SPT 2,5/4-V-5,0",
                "SPT 2,5/6-H-5,0",
                "SPT 5/3-V-7,5"
        })
        void shouldDetectSPTSpringCageConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromSPT() {
            assertEquals("SPT", handler.extractSeries("SPT 2,5/3-V-5,0"));
        }

        @Test
        void shouldExtractPinCountFromSPT() {
            assertEquals(3, handler.extractPinCount("SPT 2,5/3-V-5,0"));
            assertEquals(4, handler.extractPinCount("SPT 2,5/4-V-5,0"));
        }

        @Test
        void shouldExtractPitchFromSPT() {
            assertEquals("5.0", handler.extractPitch("SPT 2,5/3-V-5,0"));
            assertEquals("7.5", handler.extractPitch("SPT 5/3-V-7,5"));
        }
    }

    @Nested
    @DisplayName("UT Through-Wall Terminal Detection")
    class UTTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "UT 2,5-MTD",
                "UT 4-TWIN",
                "UT 6 N",
                "UT 2,5"
        })
        void shouldDetectUTThroughWallTerminals(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromUT() {
            assertEquals("UT", handler.extractSeries("UT 2,5-MTD"));
            assertEquals("UT", handler.extractSeries("UT 4-TWIN"));
        }

        @Test
        void shouldGetFamilyForUT() {
            assertEquals("Through-Wall Terminal", handler.getFamily("UT 2,5-MTD"));
        }
    }

    @Nested
    @DisplayName("FK-MCP High Current Connector Detection")
    class FKMCPTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "FK-MCP 1,5/2-ST-3,5",
                "FK-MCP 1,5/3-ST-3,5",
                "FK-MCP 1,5/4-ST-3,5",
                "FK-MCP 1,5/6-STF-3,5"
        })
        void shouldDetectFKMCPHighCurrentConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromFKMCP() {
            assertEquals("FK-MCP", handler.extractSeries("FK-MCP 1,5/3-ST-3,5"));
        }

        @Test
        void shouldExtractPinCountFromFKMCP() {
            assertEquals(3, handler.extractPinCount("FK-MCP 1,5/3-ST-3,5"));
            assertEquals(4, handler.extractPinCount("FK-MCP 1,5/4-ST-3,5"));
        }

        @Test
        void shouldExtractPitchFromFKMCP() {
            assertEquals("3.5", handler.extractPitch("FK-MCP 1,5/3-ST-3,5"));
        }

        @Test
        void shouldIdentifyAsPluggable() {
            assertTrue(handler.isPluggable("FK-MCP 1,5/3-ST-3,5"));
        }
    }

    @Nested
    @DisplayName("FRONT-MC Front Connector Detection")
    class FRONTMCTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "FRONT-MC 1,5/4-ST-3,81",
                "FRONT-MC 1,5/6-ST-3,81",
                "FRONT-MC 1,5/8-STF-3,81"
        })
        void shouldDetectFRONTMCFrontConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromFRONTMC() {
            assertEquals("FRONT-MC", handler.extractSeries("FRONT-MC 1,5/4-ST-3,81"));
        }

        @Test
        void shouldExtractPinCountFromFRONTMC() {
            assertEquals(4, handler.extractPinCount("FRONT-MC 1,5/4-ST-3,81"));
            assertEquals(6, handler.extractPinCount("FRONT-MC 1,5/6-ST-3,81"));
        }

        @Test
        void shouldIdentifyAsPluggable() {
            assertTrue(handler.isPluggable("FRONT-MC 1,5/4-ST-3,81"));
        }
    }

    @Nested
    @DisplayName("PC PCB Terminal Block Detection")
    class PCTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "PC 5/2-STF-7,62",
                "PC 5/3-STF-7,62",
                "PC 5/4-STF-7,62",
                "PC 16/2-STF-10,16"
        })
        void shouldDetectPCPCBTerminalBlocks(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldExtractSeriesFromPC() {
            assertEquals("PC", handler.extractSeries("PC 5/3-STF-7,62"));
        }

        @Test
        void shouldExtractPinCountFromPC() {
            assertEquals(3, handler.extractPinCount("PC 5/3-STF-7,62"));
            assertEquals(2, handler.extractPinCount("PC 16/2-STF-10,16"));
        }

        @Test
        void shouldExtractPitchFromPC() {
            assertEquals("7.62", handler.extractPitch("PC 5/3-STF-7,62"));
            assertEquals("10.16", handler.extractPitch("PC 16/2-STF-10,16"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        void shouldExtractPackageCodeForMCSeries() {
            assertEquals("3P-3.81mm", handler.extractPackageCode("MC 1,5/3-ST-3,81"));
            assertEquals("4P-3.81mm", handler.extractPackageCode("MC 1,5/4-ST-3,81"));
            assertEquals("6P-3.81mm", handler.extractPackageCode("MCV 1,5/6-G-3,81"));
        }

        @Test
        void shouldExtractPackageCodeForMSTBSeries() {
            assertEquals("4P-5.08mm", handler.extractPackageCode("MSTB 2,5/4-ST-5,08"));
        }

        @Test
        void shouldExtractPackageCodeForPTSeries() {
            assertEquals("2P-3.5mm", handler.extractPackageCode("PT 1,5/2-PH-3,5"));
        }

        @Test
        void shouldExtractPackageCodeForPTSMSeries() {
            assertEquals("3P-2.5mm-SMD", handler.extractPackageCode("PTSM 0,5/3-HH-2,5-SMD"));
        }

        @Test
        void shouldExtractPackageCodeForSPTSeries() {
            assertEquals("4P-5.0mm", handler.extractPackageCode("SPT 2,5/4-V-5,0"));
        }

        @Test
        void shouldExtractPackageCodeForUKSeries() {
            String packageCode = handler.extractPackageCode("UK 5-TWIN");
            assertEquals("5mm2", packageCode);
        }

        @Test
        void shouldExtractPackageCodeForUTSeries() {
            String packageCode = handler.extractPackageCode("UT 2,5-MTD");
            assertEquals("2.5mm2", packageCode);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @Test
        void shouldExtractSeriesFromMC() {
            assertEquals("MC", handler.extractSeries("MC 1,5/3-ST-3,81"));
        }

        @Test
        void shouldExtractSeriesFromMCV() {
            assertEquals("MCV", handler.extractSeries("MCV 1,5/4-G-3,81"));
        }

        @Test
        void shouldExtractSeriesFromMSTB() {
            assertEquals("MSTB", handler.extractSeries("MSTB 2,5/4-ST-5,08"));
        }

        @Test
        void shouldExtractSeriesFromMSTBA() {
            assertEquals("MSTBA", handler.extractSeries("MSTBA 2,5/4-G-5,08"));
        }

        @Test
        void shouldExtractSeriesFromPT() {
            assertEquals("PT", handler.extractSeries("PT 1,5/2-PH-3,5"));
        }

        @Test
        void shouldExtractSeriesFromPTSM() {
            assertEquals("PTSM", handler.extractSeries("PTSM 0,5/3-HH-2,5-SMD"));
        }

        @Test
        void shouldExtractSeriesFromSPT() {
            assertEquals("SPT", handler.extractSeries("SPT 2,5/3-V-5,0"));
        }

        @Test
        void shouldExtractSeriesFromUK() {
            assertEquals("UK", handler.extractSeries("UK 5-TWIN"));
        }

        @Test
        void shouldExtractSeriesFromUT() {
            assertEquals("UT", handler.extractSeries("UT 2,5-MTD"));
        }

        @Test
        void shouldExtractSeriesFromFKMCP() {
            assertEquals("FK-MCP", handler.extractSeries("FK-MCP 1,5/3-ST-3,5"));
        }

        @Test
        void shouldExtractSeriesFromFRONTMC() {
            assertEquals("FRONT-MC", handler.extractSeries("FRONT-MC 1,5/4-ST-3,81"));
        }

        @Test
        void shouldExtractSeriesFromPC() {
            assertEquals("PC", handler.extractSeries("PC 5/3-STF-7,62"));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {

        @Test
        void shouldGetFamily() {
            assertEquals("COMBICON Pluggable", handler.getFamily("MC 1,5/3-ST-3,81"));
            assertEquals("COMBICON Header", handler.getFamily("MCV 1,5/4-G-3,81"));
            assertEquals("COMBICON Standard Pluggable", handler.getFamily("MSTB 2,5/4-ST-5,08"));
            assertEquals("Push-In Terminal Block", handler.getFamily("PT 1,5/2-PH-3,5"));
            assertEquals("SMD Terminal Block", handler.getFamily("PTSM 0,5/3-HH-2,5-SMD"));
            assertEquals("Spring-Cage PCB Connector", handler.getFamily("SPT 2,5/3-V-5,0"));
            assertEquals("Screw Terminal Block", handler.getFamily("UK 5-TWIN"));
            assertEquals("Through-Wall Terminal", handler.getFamily("UT 2,5-MTD"));
        }

        @Test
        void shouldGetRatedCurrent() {
            assertEquals(8.0, handler.getRatedCurrent("MC 1,5/3-ST-3,81"));
            assertEquals(12.0, handler.getRatedCurrent("MSTB 2,5/4-ST-5,08"));
            assertEquals(17.5, handler.getRatedCurrent("PT 1,5/2-PH-3,5"));
            assertEquals(6.0, handler.getRatedCurrent("PTSM 0,5/3-HH-2,5-SMD"));
            assertEquals(24.0, handler.getRatedCurrent("SPT 2,5/3-V-5,0"));
            assertEquals(32.0, handler.getRatedCurrent("UK 5-TWIN"));
        }

        @Test
        void shouldGetMountingType() {
            assertEquals("THT", handler.getMountingType("MC 1,5/3-ST-3,81"));
            assertEquals("THT", handler.getMountingType("MSTB 2,5/4-ST-5,08"));
            assertEquals("SMD", handler.getMountingType("PTSM 0,5/3-HH-2,5-SMD"));
        }

        @Test
        void shouldGetConnectorType() {
            assertEquals("ST", handler.getConnectorType("MC 1,5/3-ST-3,81"));
            assertEquals("G", handler.getConnectorType("MCV 1,5/4-G-3,81"));
            assertEquals("PH", handler.getConnectorType("PT 1,5/2-PH-3,5"));
            assertEquals("HH", handler.getConnectorType("PTSM 0,5/3-HH-2,5-SMD"));
            assertEquals("V", handler.getConnectorType("SPT 2,5/3-V-5,0"));
        }

        @Test
        void shouldGetConnectorTypeDescription() {
            assertEquals("Plug (female)", handler.getConnectorTypeDescription("MC 1,5/3-ST-3,81"));
            assertEquals("Header (male)", handler.getConnectorTypeDescription("MCV 1,5/4-G-3,81"));
            assertEquals("Push-in header", handler.getConnectorTypeDescription("PT 1,5/2-PH-3,5"));
            assertEquals("Double-row horizontal", handler.getConnectorTypeDescription("PTSM 0,5/3-HH-2,5-SMD"));
            assertEquals("Vertical", handler.getConnectorTypeDescription("SPT 2,5/3-V-5,0"));
        }

        @Test
        void shouldGetWireGauge() {
            assertEquals("1.5", handler.getWireGauge("MC 1,5/3-ST-3,81"));
            assertEquals("2.5", handler.getWireGauge("MSTB 2,5/4-ST-5,08"));
            assertEquals("1.5", handler.getWireGauge("PT 1,5/2-PH-3,5"));
            assertEquals("0.5", handler.getWireGauge("PTSM 0,5/3-HH-2,5-SMD"));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {

        @Test
        void shouldIdentifyReplacementsWithinSameSeries() {
            // Same series, same pin count, same pitch - should be compatible
            assertTrue(handler.isOfficialReplacement("MC 1,5/3-ST-3,81", "MC 1,5/3-G-3,81"));
            assertTrue(handler.isOfficialReplacement("MSTB 2,5/4-ST-5,08", "MSTB 2,5/4-STF-5,08"));
        }

        @Test
        void shouldNotReplaceAcrossSeries() {
            // Different series should not be compatible
            assertFalse(handler.isOfficialReplacement("MC 1,5/3-ST-3,81", "MCV 1,5/3-G-3,81"));
            assertFalse(handler.isOfficialReplacement("MSTB 2,5/4-ST-5,08", "MSTBA 2,5/4-G-5,08"));
            assertFalse(handler.isOfficialReplacement("PT 1,5/2-PH-3,5", "PTSM 0,5/2-HH-2,5-SMD"));
        }

        @Test
        void shouldNotReplaceWithDifferentPinCount() {
            // Same series but different pin count should not be compatible
            assertFalse(handler.isOfficialReplacement("MC 1,5/3-ST-3,81", "MC 1,5/4-ST-3,81"));
            assertFalse(handler.isOfficialReplacement("MSTB 2,5/4-ST-5,08", "MSTB 2,5/6-ST-5,08"));
        }

        @Test
        void shouldNotReplaceWithDifferentPitch() {
            // Same series, same pins, but different pitch should not be compatible
            assertFalse(handler.isOfficialReplacement("PT 1,5/2-PH-3,5", "PT 1,5/2-PH-5,0"));
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "MC 1,5/3-ST-3,81"));
            assertFalse(handler.isOfficialReplacement("MC 1,5/3-ST-3,81", null));
            assertFalse(handler.isOfficialReplacement(null, null));
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
            assertEquals("", handler.extractPitch(null));
            assertEquals(0, handler.extractPinCount(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractPitch(""));
            assertEquals(0, handler.extractPinCount(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("MC 1,5/3-ST-3,81", null, registry));
        }

        @Test
        void shouldHandleUnknownMPN() {
            assertFalse(handler.matches("UNKNOWN-123", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractSeries("UNKNOWN-123"));
        }

        @Test
        void shouldHandleMPNWithPeriodInsteadOfComma() {
            // Some systems may use period instead of comma
            assertTrue(handler.matches("MC 1.5/3-ST-3.81", ComponentType.CONNECTOR, registry));
        }

        @Test
        void shouldHandleMPNWithHyphenInsteadOfSpace() {
            // Some systems may use hyphen instead of space
            assertTrue(handler.matches("MC-1,5/3-ST-3,81", ComponentType.CONNECTOR, registry));
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
            }, "getSupportedTypes should return immutable set");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        void canInstantiateDirectly() {
            PhoenixContactHandler directHandler = new PhoenixContactHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("MC 1,5/3-ST-3,81", ComponentType.CONNECTOR, directRegistry));
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
    @DisplayName("Pluggable vs Terminal Block Classification")
    class ClassificationTests {

        @Test
        void shouldClassifyPluggableConnectors() {
            assertTrue(handler.isPluggable("MC 1,5/3-ST-3,81"));
            assertTrue(handler.isPluggable("MCV 1,5/4-G-3,81"));
            assertTrue(handler.isPluggable("MSTB 2,5/4-ST-5,08"));
            assertTrue(handler.isPluggable("MSTBA 2,5/4-G-5,08"));
            assertTrue(handler.isPluggable("FK-MCP 1,5/3-ST-3,5"));
            assertTrue(handler.isPluggable("FRONT-MC 1,5/4-ST-3,81"));
        }

        @Test
        void shouldClassifyTerminalBlocks() {
            assertTrue(handler.isTerminalBlock("PT 1,5/2-PH-3,5"));
            assertTrue(handler.isTerminalBlock("PTSM 0,5/3-HH-2,5-SMD"));
            assertTrue(handler.isTerminalBlock("SPT 2,5/3-V-5,0"));
            assertTrue(handler.isTerminalBlock("UK 5-TWIN"));
            assertTrue(handler.isTerminalBlock("UT 2,5-MTD"));
            assertTrue(handler.isTerminalBlock("PC 5/3-STF-7,62"));
        }

        @Test
        void pluggableShouldNotBeTerminalBlock() {
            assertFalse(handler.isTerminalBlock("MC 1,5/3-ST-3,81"));
            assertFalse(handler.isTerminalBlock("MSTB 2,5/4-ST-5,08"));
        }

        @Test
        void terminalBlockShouldNotBePluggable() {
            assertFalse(handler.isPluggable("PT 1,5/2-PH-3,5"));
            assertFalse(handler.isPluggable("UK 5-TWIN"));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldTests {

        @ParameterizedTest
        @ValueSource(strings = {
                // Real Phoenix Contact MPNs from datasheets
                "MC 1,5/2-ST-3,81",
                "MC 1,5/3-ST-3,81",
                "MC 1,5/4-ST-3,81",
                "MC 1,5/5-ST-3,81",
                "MC 1,5/6-ST-3,81",
                "MC 1,5/7-ST-3,81",
                "MC 1,5/8-ST-3,81",
                "MC 1,5/9-ST-3,81",
                "MC 1,5/10-ST-3,81",
                "MC 1,5/11-ST-3,81",
                "MC 1,5/12-ST-3,81",
                "MCV 1,5/2-G-3,81",
                "MCV 1,5/3-G-3,81",
                "MCV 1,5/4-G-3,81",
                "MSTB 2,5/2-ST-5,08",
                "MSTB 2,5/3-ST-5,08",
                "MSTB 2,5/4-ST-5,08",
                "MSTBA 2,5/2-G-5,08",
                "PT 1,5/2-PH-3,5",
                "PT 1,5/3-PH-3,5",
                "PTSM 0,5/2-HH-2,5-SMD",
                "PTSM 0,5/3-HH-2,5-SMD",
                "SPT 2,5/2-V-5,0",
                "SPT 2,5/3-V-5,0",
                "UK 5-TWIN",
                "UK 3 N",
                "UT 2,5",
                "UT 4-TWIN"
        })
        void shouldMatchRealWorldMPNs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }
    }
}
