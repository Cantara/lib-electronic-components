package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.OmronHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for OmronHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Omron product families:
 * - Relays: G2R (general purpose), G5V (signal), G6K (ultra-miniature), G5RL (slim power)
 * - SSR: G3MC, G3NA, G3NE (solid state relays)
 * - Switches: B3F (tact), B3FS (tact SMD), B3U (ultra compact), D2F/D2FC (micro), SS (slide)
 * - Sensors: EE-S (optical), D6F (flow), E2E/E2K (proximity)
 *
 * MPN Examples:
 * - G5V-1-DC5: Signal relay, SPDT, 5V DC coil
 * - G3MC-201P-DC5: SSR, 201P model, 5V DC control
 * - B3F-1000: Tact switch, 1.47N force
 * - D2FC-F-7N: Microswitch compact, low force
 * - EE-SX1041: Optical sensor, slot type
 */
class OmronHandlerTest {

    private static OmronHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new OmronHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Signal Relay Detection (G5V/G6K Series)")
    class SignalRelayTests {

        @ParameterizedTest
        @DisplayName("Should detect G5V signal relays")
        @ValueSource(strings = {"G5V-1-DC5", "G5V-2-DC12", "G5V-1-DC24"})
        void shouldDetectG5VRelays(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RELAY, registry),
                    mpn + " should match RELAY");
            assertTrue(handler.matches(mpn, ComponentType.RELAY_OMRON, registry),
                    mpn + " should match RELAY_OMRON");
            assertTrue(handler.matches(mpn, ComponentType.RELAY_SIGNAL, registry),
                    mpn + " should match RELAY_SIGNAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect G6K ultra-miniature signal relays")
        @ValueSource(strings = {"G6K-2-DC5", "G6K-2F-DC5", "G6KU-2-DC3"})
        void shouldDetectG6KRelays(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RELAY, registry),
                    mpn + " should match RELAY");
            assertTrue(handler.matches(mpn, ComponentType.RELAY_SIGNAL, registry),
                    mpn + " should match RELAY_SIGNAL");
        }

        @Test
        @DisplayName("Should extract series from G5V relay")
        void shouldExtractG5VSeries() {
            assertEquals("G5V", handler.extractSeries("G5V-1-DC5"));
            assertEquals("G6K", handler.extractSeries("G6K-2-DC5"));
        }

        @Test
        @DisplayName("Should extract contact configuration from signal relays")
        void shouldExtractContactConfig() {
            assertEquals("SPDT", handler.extractPackageCode("G5V-1-DC5"));
            assertEquals("DPDT", handler.extractPackageCode("G5V-2-DC12"));
        }
    }

    @Nested
    @DisplayName("Power Relay Detection (G2R/G2RL/G5RL Series)")
    class PowerRelayTests {

        @ParameterizedTest
        @DisplayName("Should detect G2R general purpose relays")
        @ValueSource(strings = {"G2R-1-DC12", "G2R-2-DC24", "G2RL-1-DC12"})
        void shouldDetectG2RRelays(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RELAY, registry),
                    mpn + " should match RELAY");
            assertTrue(handler.matches(mpn, ComponentType.RELAY_POWER, registry),
                    mpn + " should match RELAY_POWER");
        }

        @ParameterizedTest
        @DisplayName("Should detect G5RL slim power relays")
        @ValueSource(strings = {"G5RL-1-E-DC12", "G5RL-1A-DC24"})
        void shouldDetectG5RLRelays(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RELAY, registry),
                    mpn + " should match RELAY");
            assertTrue(handler.matches(mpn, ComponentType.RELAY_POWER, registry),
                    mpn + " should match RELAY_POWER");
        }
    }

    @Nested
    @DisplayName("SSR Detection (G3MC/G3NA/G3NE Series)")
    class SSRTests {

        @ParameterizedTest
        @DisplayName("Should detect G3MC solid state relays")
        @ValueSource(strings = {"G3MC-201P-DC5", "G3MC-202P-DC24", "G3MC-101P-DC12"})
        void shouldDetectG3MCRelays(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RELAY, registry),
                    mpn + " should match RELAY");
            assertTrue(handler.matches(mpn, ComponentType.RELAY_SSR, registry),
                    mpn + " should match RELAY_SSR");
            assertFalse(handler.matches(mpn, ComponentType.RELAY_SIGNAL, registry),
                    mpn + " should NOT match RELAY_SIGNAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect G3NA panel mount SSRs")
        @ValueSource(strings = {"G3NA-210B-DC5-24", "G3NA-220B-DC5-24"})
        void shouldDetectG3NARelays(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.RELAY_SSR, registry),
                    mpn + " should match RELAY_SSR");
        }

        @Test
        @DisplayName("Should extract series from SSR")
        void shouldExtractSSRSeries() {
            assertEquals("G3MC", handler.extractSeries("G3MC-201P-DC5"));
            assertEquals("G3NA", handler.extractSeries("G3NA-210B-DC5-24"));
        }
    }

    @Nested
    @DisplayName("Tactile Switch Detection (B3F/B3FS/B3U Series)")
    class TactileSwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect B3F tactile switches")
        @ValueSource(strings = {"B3F-1000", "B3F-1002", "B3F-1050", "B3F-3100"})
        void shouldDetectB3FSwitches(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SWITCH, registry),
                    mpn + " should match SWITCH");
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_OMRON, registry),
                    mpn + " should match SWITCH_OMRON");
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_TACT, registry),
                    mpn + " should match SWITCH_TACT");
        }

        @ParameterizedTest
        @DisplayName("Should detect B3FS SMD tactile switches")
        @ValueSource(strings = {"B3FS-1000", "B3FS-1002P", "B3FS-1010"})
        void shouldDetectB3FSSwitches(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_TACT, registry),
                    mpn + " should match SWITCH_TACT");
        }

        @ParameterizedTest
        @DisplayName("Should detect B3U ultra-compact tactile switches")
        @ValueSource(strings = {"B3U-1000P", "B3U-1000PM", "B3U-3000P"})
        void shouldDetectB3USwitches(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_TACT, registry),
                    mpn + " should match SWITCH_TACT");
        }

        @Test
        @DisplayName("Should extract package type from tactile switches")
        void shouldExtractPackageFromTactSwitch() {
            assertEquals("THT", handler.extractPackageCode("B3F-1000"));
            assertEquals("SMD", handler.extractPackageCode("B3FS-1000"));
            assertEquals("SMD", handler.extractPackageCode("B3U-1000P"));
        }

        @Test
        @DisplayName("Should extract actuation force")
        void shouldExtractActuationForce() {
            assertEquals("1.47N", handler.getActuationForce("B3F-1000"));
            assertEquals("2.55N", handler.getActuationForce("B3F-2000"));
        }
    }

    @Nested
    @DisplayName("Microswitch Detection (D2F/D2FC Series)")
    class MicroswitchTests {

        @ParameterizedTest
        @DisplayName("Should detect D2F microswitches")
        @ValueSource(strings = {"D2F-01", "D2F-01F", "D2F-01L", "D2F-5"})
        void shouldDetectD2FSwitches(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SWITCH, registry),
                    mpn + " should match SWITCH");
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_MICRO, registry),
                    mpn + " should match SWITCH_MICRO");
        }

        @ParameterizedTest
        @DisplayName("Should detect D2FC compact microswitches (mouse switches)")
        @ValueSource(strings = {"D2FC-F-7N", "D2FC-F-7N-10M", "D2FC-F-7N-20M"})
        void shouldDetectD2FCSwitches(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_MICRO, registry),
                    mpn + " should match SWITCH_MICRO");
        }

        @Test
        @DisplayName("Should extract series from microswitches")
        void shouldExtractMicroswitchSeries() {
            assertEquals("D2F", handler.extractSeries("D2F-01"));
            assertEquals("D2FC", handler.extractSeries("D2FC-F-7N"));
        }

        @Test
        @DisplayName("Should identify low force variant")
        void shouldIdentifyLowForce() {
            assertEquals("Low force", handler.extractPackageCode("D2F-01F"));
            assertEquals("0.74N", handler.getActuationForce("D2FC-F-7N"));
        }
    }

    @Nested
    @DisplayName("Slide Switch Detection (SS Series)")
    class SlideSwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect SS slide switches")
        @ValueSource(strings = {"SS-10GL13", "SS-10GL2", "SS-12SDP2"})
        void shouldDetectSSSwitches(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SWITCH, registry),
                    mpn + " should match SWITCH");
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_SLIDE, registry),
                    mpn + " should match SWITCH_SLIDE");
        }
    }

    @Nested
    @DisplayName("Optical Sensor Detection (EE-S/EE-SX Series)")
    class OpticalSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect EE-S optical sensors")
        @ValueSource(strings = {"EE-SX1041", "EE-SX1070", "EE-SX4009-P1", "EE-SY310"})
        void shouldDetectEESSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_OPTICAL, registry),
                    mpn + " should match SENSOR_OPTICAL");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_OPTICAL_OMRON, registry),
                    mpn + " should match SENSOR_OPTICAL_OMRON");
        }

        @Test
        @DisplayName("Should extract series from optical sensors")
        void shouldExtractOpticalSensorSeries() {
            assertEquals("EE", handler.extractSeries("EE-SX1041"));
            assertEquals("EE", handler.extractSeries("EE-SY310"));
        }
    }

    @Nested
    @DisplayName("Flow Sensor Detection (D6F Series)")
    class FlowSensorTests {

        @ParameterizedTest
        @DisplayName("Should detect D6F MEMS flow sensors")
        @ValueSource(strings = {"D6F-01N1-110", "D6F-10A6-000", "D6F-20A6-000", "D6F-50A6-000"})
        void shouldDetectD6FSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_FLOW, registry),
                    mpn + " should match SENSOR_FLOW");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_FLOW_OMRON, registry),
                    mpn + " should match SENSOR_FLOW_OMRON");
        }

        @Test
        @DisplayName("Should extract series from flow sensors")
        void shouldExtractFlowSensorSeries() {
            assertEquals("D6F", handler.extractSeries("D6F-01N1-110"));
        }
    }

    @Nested
    @DisplayName("Proximity Sensor Detection (E2E/E2K Series)")
    class ProximitySensorTests {

        @ParameterizedTest
        @DisplayName("Should detect E2E inductive proximity sensors")
        @ValueSource(strings = {"E2E-X1R5E1", "E2E-X2D1-N", "E2E-X5F1", "E2E-X10D1-M1"})
        void shouldDetectE2ESensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR, registry),
                    mpn + " should match SENSOR");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_PROXIMITY, registry),
                    mpn + " should match SENSOR_PROXIMITY");
        }

        @ParameterizedTest
        @DisplayName("Should detect E2K capacitive proximity sensors")
        @ValueSource(strings = {"E2K-C25ME1", "E2K-X4ME1", "E2K-X8ME1"})
        void shouldDetectE2KSensors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_PROXIMITY, registry),
                    mpn + " should match SENSOR_PROXIMITY");
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_PROXIMITY_OMRON, registry),
                    mpn + " should match SENSOR_PROXIMITY_OMRON");
        }

        @Test
        @DisplayName("Should extract connector type from E2E sensors")
        void shouldExtractConnectorType() {
            assertEquals("M12 Connector", handler.extractPackageCode("E2E-X1R5E1"));
            assertEquals("2m Cable", handler.extractPackageCode("E2E-X2D1-E2"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from various MPNs")
        @CsvSource({
                "G5V-1-DC5, G5V",
                "G6K-2-DC5, G6K",
                "G2R-1-DC12, G2R",
                "G3MC-201P-DC5, G3MC",
                "B3F-1000, B3F",
                "D2FC-F-7N, D2FC",
                "EE-SX1041, EE",
                "D6F-01N1-110, D6F",
                "E2E-X1R5E1, E2E"
        })
        void shouldExtractSeriesCorrectly(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with same voltage are replacements")
        void sameSeriesSameVoltage() {
            assertTrue(handler.isOfficialReplacement("G5V-1-DC5", "G5V-1-DC5"),
                    "Identical MPNs should be replacements");
        }

        @Test
        @DisplayName("Same series with different voltage are NOT replacements")
        void sameSeriesDifferentVoltage() {
            assertFalse(handler.isOfficialReplacement("G5V-1-DC5", "G5V-1-DC12"),
                    "Different coil voltages should not be replacements");
        }

        @Test
        @DisplayName("Same series with different contact config are NOT replacements")
        void sameSeriesDifferentContact() {
            assertFalse(handler.isOfficialReplacement("G5V-1-DC5", "G5V-2-DC5"),
                    "Different contact configurations should not be replacements");
        }

        @Test
        @DisplayName("G5V and G6K are compatible signal relays")
        void g5vAndG6kCompatible() {
            assertTrue(handler.isOfficialReplacement("G5V-1-DC5", "G6K-2-DC5"),
                    "G5V and G6K are both signal relays and can be compatible");
        }

        @Test
        @DisplayName("G3MC and G3NA are compatible SSRs")
        void g3mcAndG3naCompatible() {
            assertTrue(handler.isOfficialReplacement("G3MC-201P-DC5", "G3NA-210B-DC5-24"),
                    "G3MC and G3NA are both SSRs with different mounting");
        }

        @Test
        @DisplayName("B3F and B3FS are compatible (THT vs SMD)")
        void b3fAndB3fsCompatible() {
            assertTrue(handler.isOfficialReplacement("B3F-1000", "B3FS-1000"),
                    "B3F and B3FS have same function, different packages");
        }

        @Test
        @DisplayName("Relay and switch are NOT replacements")
        void relayAndSwitchNotCompatible() {
            assertFalse(handler.isOfficialReplacement("G5V-1-DC5", "B3F-1000"),
                    "Relays and switches are different component types");
        }
    }

    @Nested
    @DisplayName("Family Information")
    class FamilyTests {

        @ParameterizedTest
        @DisplayName("Should identify relay families")
        @CsvSource({
                "G5V-1-DC5, Signal Relay",
                "G6K-2-DC5, Ultra Miniature Signal Relay",
                "G2R-1-DC12, General Purpose Relay",
                "G3MC-201P-DC5, Solid State Relay"
        })
        void shouldIdentifyRelayFamily(String mpn, String expectedFamily) {
            assertEquals(expectedFamily, handler.getFamily(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should identify switch families")
        @CsvSource({
                "B3F-1000, Tactile Switch",
                "B3FS-1000, Tactile Switch SMD",
                "D2F-01, Microswitch",
                "SS-10GL13, Slide Switch"
        })
        void shouldIdentifySwitchFamily(String mpn, String expectedFamily) {
            assertEquals(expectedFamily, handler.getFamily(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should identify sensor families")
        @CsvSource({
                "EE-SX1041, Optical Sensor SMD",
                "EE-S301, Optical Sensor Slot",
                "D6F-01N1-110, MEMS Flow Sensor",
                "E2E-X1R5E1, Proximity Sensor Inductive"
        })
        void shouldIdentifySensorFamily(String mpn, String expectedFamily) {
            assertEquals(expectedFamily, handler.getFamily(mpn));
        }
    }

    @Nested
    @DisplayName("Coil Voltage Extraction")
    class CoilVoltageTests {

        @ParameterizedTest
        @DisplayName("Should extract DC coil voltage")
        @CsvSource({
                "G5V-1-DC5, 5V DC",
                "G5V-1-DC12, 12V DC",
                "G2R-1-DC24, 24V DC"
        })
        void shouldExtractDCVoltage(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.getCoilVoltage(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should extract AC control voltage")
        @CsvSource({
                "G3NA-220B-AC100-240, 100V AC"
        })
        void shouldExtractACVoltage(String mpn, String expectedVoltage) {
            String voltage = handler.getCoilVoltage(mpn);
            assertTrue(voltage.contains("AC"), "Should extract AC voltage");
        }
    }

    @Nested
    @DisplayName("Contact Rating")
    class ContactRatingTests {

        @ParameterizedTest
        @DisplayName("Should return contact ratings by series")
        @CsvSource({
                "G5V-1-DC5, 1A 24VDC",
                "G6K-2-DC5, 1A 30VDC",
                "G2R-1-DC12, 5A 250VAC"
        })
        void shouldGetContactRating(String mpn, String expectedRating) {
            assertEquals(expectedRating, handler.getContactRating(mpn));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support relay types")
        void shouldSupportRelayTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.RELAY), "Should support RELAY");
            assertTrue(types.contains(ComponentType.RELAY_OMRON), "Should support RELAY_OMRON");
            assertTrue(types.contains(ComponentType.RELAY_SIGNAL), "Should support RELAY_SIGNAL");
            assertTrue(types.contains(ComponentType.RELAY_POWER), "Should support RELAY_POWER");
            assertTrue(types.contains(ComponentType.RELAY_SSR), "Should support RELAY_SSR");
        }

        @Test
        @DisplayName("Should support switch types")
        void shouldSupportSwitchTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.SWITCH), "Should support SWITCH");
            assertTrue(types.contains(ComponentType.SWITCH_OMRON), "Should support SWITCH_OMRON");
            assertTrue(types.contains(ComponentType.SWITCH_TACT), "Should support SWITCH_TACT");
            assertTrue(types.contains(ComponentType.SWITCH_MICRO), "Should support SWITCH_MICRO");
            assertTrue(types.contains(ComponentType.SWITCH_SLIDE), "Should support SWITCH_SLIDE");
        }

        @Test
        @DisplayName("Should support sensor types")
        void shouldSupportSensorTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.SENSOR), "Should support SENSOR");
            assertTrue(types.contains(ComponentType.SENSOR_OMRON), "Should support SENSOR_OMRON");
            assertTrue(types.contains(ComponentType.SENSOR_OPTICAL), "Should support SENSOR_OPTICAL");
            assertTrue(types.contains(ComponentType.SENSOR_OPTICAL_OMRON), "Should support SENSOR_OPTICAL_OMRON");
            assertTrue(types.contains(ComponentType.SENSOR_FLOW), "Should support SENSOR_FLOW");
            assertTrue(types.contains(ComponentType.SENSOR_FLOW_OMRON), "Should support SENSOR_FLOW_OMRON");
            assertTrue(types.contains(ComponentType.SENSOR_PROXIMITY), "Should support SENSOR_PROXIMITY");
            assertTrue(types.contains(ComponentType.SENSOR_PROXIMITY_OMRON), "Should support SENSOR_PROXIMITY_OMRON");
        }

        @Test
        @DisplayName("Should use Set.of() - no duplicates")
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
            assertFalse(handler.matches(null, ComponentType.RELAY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "G5V-1-DC5"));
            assertFalse(handler.isOfficialReplacement("G5V-1-DC5", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.RELAY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("G5V-1-DC5", null, registry));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            OmronHandler directHandler = new OmronHandler();
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
    }

    @Nested
    @DisplayName("Real-World Component Tests")
    class RealWorldTests {

        @Test
        @DisplayName("G5V-1-DC5 - Popular signal relay")
        void g5v1dc5() {
            String mpn = "G5V-1-DC5";
            assertTrue(handler.matches(mpn, ComponentType.RELAY_SIGNAL, registry));
            assertEquals("G5V", handler.extractSeries(mpn));
            assertEquals("SPDT", handler.extractPackageCode(mpn));
            assertEquals("5V DC", handler.getCoilVoltage(mpn));
            assertEquals("Signal Relay", handler.getFamily(mpn));
        }

        @Test
        @DisplayName("G3MC-201P-DC5 - SSR for automation")
        void g3mc201pdc5() {
            String mpn = "G3MC-201P-DC5";
            assertTrue(handler.matches(mpn, ComponentType.RELAY_SSR, registry));
            assertEquals("G3MC", handler.extractSeries(mpn));
            assertEquals("5V DC", handler.getCoilVoltage(mpn));
            assertEquals("Solid State Relay", handler.getFamily(mpn));
        }

        @Test
        @DisplayName("B3F-1000 - Classic tact switch")
        void b3f1000() {
            String mpn = "B3F-1000";
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_TACT, registry));
            assertEquals("B3F", handler.extractSeries(mpn));
            assertEquals("THT", handler.extractPackageCode(mpn));
            assertEquals("1.47N", handler.getActuationForce(mpn));
            assertEquals("Tactile Switch", handler.getFamily(mpn));
        }

        @Test
        @DisplayName("D2FC-F-7N - Mouse microswitch")
        void d2fcf7n() {
            String mpn = "D2FC-F-7N";
            assertTrue(handler.matches(mpn, ComponentType.SWITCH_MICRO, registry));
            assertEquals("D2FC", handler.extractSeries(mpn));
            assertEquals("0.74N", handler.getActuationForce(mpn));
        }

        @Test
        @DisplayName("EE-SX1041 - Optical sensor for position detection")
        void eesx1041() {
            String mpn = "EE-SX1041";
            assertTrue(handler.matches(mpn, ComponentType.SENSOR_OPTICAL, registry));
            assertEquals("EE", handler.extractSeries(mpn));
        }
    }
}
