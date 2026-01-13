package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.JAEHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for JAEHandler.
 * Tests FI (FPC/FFC), DX (board-to-board, USB), MX (automotive), and IL (circular) connector series.
 *
 * JAE Electronics (Japan Aviation Electronics Industry) product lines:
 * - FI-RE: 0.5mm pitch, low profile FPC connectors
 * - FI-X: 0.5mm pitch, standard FPC connectors
 * - FI-W: 0.5mm pitch, high-current FPC connectors
 * - FI-E: 0.5mm pitch, slim FPC connectors
 * - FI-S: 1.0mm pitch, FPC connectors
 * - FI-J: 1.0mm pitch, wire-to-board connectors
 * - DX07: USB Type-C connectors
 * - DX40: High-speed board-to-board connectors
 * - MX34: Automotive waterproof connectors
 * - MX44: Automotive miniature connectors
 * - MX77: Automotive connectors
 * - IL: Circular connectors
 *
 * MPN format examples:
 * - FI-RE51S-HF-R1500: FI-RE series, 51 pins, S type, HF option, R1500 (reel packaging)
 * - FI-X30HL-T: FI-X series, 30 pins, HL type, T (tape packaging)
 * - DX07S024JA1R1500: DX07 USB-C, S (SMT), 024 (24 pins), JA (variant), 1 (option), R1500 (reel)
 * - MX34036NF1: MX34 series, 036 (36 pins), NF (variant), 1 (option)
 */
class JAEHandlerTest {

    private static JAEHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new JAEHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("FI-RE Series Detection (0.5mm FPC)")
    class FIRETests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FI-RE51S-HF-R1500",
            "FI-RE31S-HF",
            "FI-RE41S-HF-R1500",
            "FI-RE21S-VF",
            "FI-RE61S-HF-R1500"
        })
        void shouldMatchFIREConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractPinCount() {
            assertEquals(51, handler.extractPinCount("FI-RE51S-HF-R1500"));
            assertEquals(31, handler.extractPinCount("FI-RE31S-HF"));
            assertEquals(41, handler.extractPinCount("FI-RE41S-HF-R1500"));
            assertEquals(21, handler.extractPinCount("FI-RE21S-VF"));
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("FI-RE", handler.extractSeries("FI-RE51S-HF-R1500"));
            assertEquals("FI-RE", handler.extractSeries("FI-RE31S-HF"));
        }
    }

    @Nested
    @DisplayName("FI-X Series Detection (0.5mm FPC)")
    class FIXTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FI-X30HL-T",
            "FI-X40HL-T",
            "FI-X50HLS-T",
            "FI-X20HL-T",
            "FI-X60HL"
        })
        void shouldMatchFIXConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractPinCount() {
            assertEquals(30, handler.extractPinCount("FI-X30HL-T"));
            assertEquals(40, handler.extractPinCount("FI-X40HL-T"));
            assertEquals(50, handler.extractPinCount("FI-X50HLS-T"));
            assertEquals(20, handler.extractPinCount("FI-X20HL-T"));
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("FI-X", handler.extractSeries("FI-X30HL-T"));
            assertEquals("FI-X", handler.extractSeries("FI-X40HL-T"));
        }
    }

    @Nested
    @DisplayName("FI-W Series Detection (0.5mm high-current FPC)")
    class FIWTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FI-W11P-HFE",
            "FI-W17P-HFE",
            "FI-W31P-HFE",
            "FI-W9P-HFE-R1500"
        })
        void shouldMatchFIWConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("FI-W", handler.extractSeries("FI-W11P-HFE"));
            assertEquals("FI-W", handler.extractSeries("FI-W17P-HFE"));
        }
    }

    @Nested
    @DisplayName("DX07 Series Detection (USB Type-C)")
    class DX07Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "DX07S024JA1R1500",
            "DX07S024JA1",
            "DX07B024JA1R1500",
            "DX07S016JA1",
            "DX07P024AJ1R1500"
        })
        void shouldMatchDX07Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractPinCount() {
            assertEquals(24, handler.extractPinCount("DX07S024JA1R1500"));
            assertEquals(24, handler.extractPinCount("DX07B024JA1R1500"));
            assertEquals(16, handler.extractPinCount("DX07S016JA1"));
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("DX07", handler.extractSeries("DX07S024JA1R1500"));
            assertEquals("DX07", handler.extractSeries("DX07B024JA1R1500"));
        }

        @Test
        void shouldIdentifyUSBTypeC() {
            assertTrue(handler.isUSBTypeC("DX07S024JA1R1500"));
            assertTrue(handler.isUSBTypeC("DX07B024JA1R1500"));
            assertFalse(handler.isUSBTypeC("FI-X30HL-T"));
        }
    }

    @Nested
    @DisplayName("DX40 Series Detection (Board-to-Board)")
    class DX40Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "DX40-36P",
            "DX40-20P",
            "DX40-50P-S",
            "DX40-100P-S"
        })
        void shouldMatchDX40Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("DX40", handler.extractSeries("DX40-36P"));
            assertEquals("DX40", handler.extractSeries("DX40-20P"));
        }
    }

    @Nested
    @DisplayName("MX34 Series Detection (Automotive Waterproof)")
    class MX34Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MX34036NF1",
            "MX34016NF1",
            "MX34024SF2",
            "MX34012SF1",
            "MX34040NF1"
        })
        void shouldMatchMX34Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractPinCount() {
            assertEquals(36, handler.extractPinCount("MX34036NF1"));
            assertEquals(16, handler.extractPinCount("MX34016NF1"));
            assertEquals(24, handler.extractPinCount("MX34024SF2"));
            assertEquals(12, handler.extractPinCount("MX34012SF1"));
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("MX34", handler.extractSeries("MX34036NF1"));
            assertEquals("MX34", handler.extractSeries("MX34016NF1"));
        }

        @Test
        void shouldIdentifyAutomotiveGrade() {
            assertTrue(handler.isAutomotiveGrade("MX34036NF1"));
            assertTrue(handler.isWaterproof("MX34036NF1"));
        }
    }

    @Nested
    @DisplayName("MX44 Series Detection (Automotive Miniature)")
    class MX44Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MX44016NF1",
            "MX44024NF1",
            "MX44012SF1"
        })
        void shouldMatchMX44Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractPinCount() {
            assertEquals(16, handler.extractPinCount("MX44016NF1"));
            assertEquals(24, handler.extractPinCount("MX44024NF1"));
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("MX44", handler.extractSeries("MX44016NF1"));
        }

        @Test
        void shouldIdentifyAutomotiveGrade() {
            assertTrue(handler.isAutomotiveGrade("MX44016NF1"));
        }
    }

    @Nested
    @DisplayName("MX77 Series Detection (Automotive)")
    class MX77Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MX7724SF1",
            "MX7712NF1",
            "MX7736SF2"
        })
        void shouldMatchMX77Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("MX77", handler.extractSeries("MX7724SF1"));
            assertEquals("MX77", handler.extractSeries("MX7712NF1"));
        }

        @Test
        void shouldIdentifyAutomotiveGrade() {
            assertTrue(handler.isAutomotiveGrade("MX7724SF1"));
            assertTrue(handler.isWaterproof("MX7724SF1"));
        }
    }

    @Nested
    @DisplayName("IL Series Detection (Circular)")
    class ILTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IL-312-16P",
            "IL-312-24P",
            "IL-S12-16PF",
            "IL-G12-8P"
        })
        void shouldMatchILConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                    mpn + " should match CONNECTOR_JAE");
        }

        @Test
        void shouldExtractSeries() {
            assertEquals("IL", handler.extractSeries("IL-312-16P"));
            assertEquals("IL", handler.extractSeries("IL-S12-16PF"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @Test
        void shouldExtractPackageCodeFromFIRE() {
            assertEquals("HF", handler.extractPackageCode("FI-RE51S-HF-R1500"));
            assertEquals("HF", handler.extractPackageCode("FI-RE31S-HF"));
            assertEquals("VF", handler.extractPackageCode("FI-RE21S-VF"));
        }

        @Test
        void shouldExtractPackageCodeFromFIX() {
            assertEquals("HL", handler.extractPackageCode("FI-X30HL-T"));
            assertEquals("HL", handler.extractPackageCode("FI-X40HL-T"));
            assertEquals("HLS", handler.extractPackageCode("FI-X50HLS-T"));
        }

        @Test
        void shouldExtractPackageCodeFromMX34() {
            assertEquals("NF1", handler.extractPackageCode("MX34036NF1"));
            assertEquals("SF2", handler.extractPackageCode("MX34024SF2"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "FI-RE51S-HF-R1500, FI-RE",
            "FI-X30HL-T, FI-X",
            "FI-W11P-HFE, FI-W",
            "DX07S024JA1R1500, DX07",
            "DX40-36P, DX40",
            "MX34036NF1, MX34",
            "MX44016NF1, MX44",
            "MX7724SF1, MX77",
            "IL-312-16P, IL"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetPitch() {
            assertEquals("0.50", handler.getPitch("FI-RE51S-HF-R1500"));
            assertEquals("0.50", handler.getPitch("FI-X30HL-T"));
            assertEquals("0.50", handler.getPitch("DX07S024JA1R1500"));
            assertEquals("0.40", handler.getPitch("DX40-36P"));
            assertEquals("2.20", handler.getPitch("MX34036NF1"));
            assertEquals("1.00", handler.getPitch("MX44016NF1"));
        }

        @Test
        void shouldGetRatedCurrent() {
            assertEquals(0.5, handler.getRatedCurrent("FI-RE51S-HF-R1500"));
            assertEquals(0.5, handler.getRatedCurrent("FI-X30HL-T"));
            assertEquals(1.0, handler.getRatedCurrent("FI-W11P-HFE"));  // High-current
            assertEquals(5.0, handler.getRatedCurrent("DX07S024JA1R1500"));  // USB-C
            assertEquals(5.0, handler.getRatedCurrent("MX34036NF1"));  // Automotive
        }

        @Test
        void shouldGetApplicationType() {
            assertEquals("FPC/FFC", handler.getApplicationType("FI-RE51S-HF-R1500"));
            assertEquals("FPC/FFC", handler.getApplicationType("FI-X30HL-T"));
            assertEquals("USB Type-C", handler.getApplicationType("DX07S024JA1R1500"));
            assertEquals("Board-to-Board", handler.getApplicationType("DX40-36P"));
            assertEquals("Automotive", handler.getApplicationType("MX34036NF1"));
            assertEquals("Circular", handler.getApplicationType("IL-312-16P"));
        }

        @Test
        void shouldDetermineMountingType() {
            // Most JAE connectors are SMT
            assertEquals("SMT", handler.getMountingType("FI-RE51S-HF-R1500"));
            assertEquals("SMT", handler.getMountingType("DX07S024JA1R1500"));
        }

        @Test
        void shouldDetermineWaterproofStatus() {
            assertTrue(handler.isWaterproof("MX34036NF1"));
            assertTrue(handler.isWaterproof("MX7724SF1"));
            assertFalse(handler.isWaterproof("FI-X30HL-T"));
            assertFalse(handler.isWaterproof("DX07S024JA1R1500"));
        }

        @Test
        void shouldGetFamily() {
            assertEquals("FI-RE Series", handler.getFamily("FI-RE51S-HF-R1500"));
            assertEquals("FI-X Series", handler.getFamily("FI-X30HL-T"));
            assertEquals("DX07 Series", handler.getFamily("DX07S024JA1R1500"));
            assertEquals("MX34 Series", handler.getFamily("MX34036NF1"));
        }

        @Test
        void shouldIdentifyFPCConnectors() {
            assertTrue(handler.isFPCConnector("FI-RE51S-HF-R1500"));
            assertTrue(handler.isFPCConnector("FI-X30HL-T"));
            assertTrue(handler.isFPCConnector("FI-W11P-HFE"));
            assertFalse(handler.isFPCConnector("DX07S024JA1R1500"));
            assertFalse(handler.isFPCConnector("MX34036NF1"));
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
            assertFalse(handler.isOfficialReplacement(null, "FI-RE51S-HF"));
            assertFalse(handler.isOfficialReplacement("FI-RE51S-HF", null));
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
            assertFalse(handler.matches("FI-RE51S-HF-R1500", null, registry));
        }

        @Test
        void shouldHandleUnrecognizedMPN() {
            assertEquals("", handler.extractSeries("UNKNOWN-12345"));
            assertEquals(0, handler.extractPinCount("UNKNOWN-12345"));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("FI-RE51S-HF", "FI-X51HL-T"));
            assertFalse(handler.isOfficialReplacement("MX34036NF1", "MX44036NF1"));
        }

        @Test
        void shouldNotReplaceWithDifferentPinCount() {
            assertFalse(handler.isOfficialReplacement("FI-RE51S-HF", "FI-RE31S-HF"));
            assertFalse(handler.isOfficialReplacement("MX34036NF1", "MX34024NF1"));
        }

        @Test
        void shouldAllowCompatibleVariants() {
            // Same series, same pin count, compatible mounting types
            assertTrue(handler.isOfficialReplacement("FI-RE51S-HF", "FI-RE51S-VF"));
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
            assertTrue(types.contains(ComponentType.CONNECTOR_JAE),
                    "Should support CONNECTOR_JAE");
        }

        @Test
        void shouldUseSetOfNotHashSet() {
            var types = handler.getSupportedTypes();
            // Set.of() returns an unmodifiable set
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.IC),
                    "getSupportedTypes should return immutable Set");
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
            JAEHandler directHandler = new JAEHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify patterns work
            assertTrue(directHandler.matches("FI-RE51S-HF-R1500", ComponentType.CONNECTOR_JAE, directRegistry));
            assertTrue(directHandler.matches("DX07S024JA1R1500", ComponentType.CONNECTOR_JAE, directRegistry));
            assertTrue(directHandler.matches("MX34036NF1", ComponentType.CONNECTOR_JAE, directRegistry));
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
    @DisplayName("Real-World Part Numbers")
    class RealWorldTests {
        @Test
        void shouldMatchRealFPCConnectors() {
            // Real JAE FPC connector part numbers
            String[] realMpns = {
                "FI-RE51S-HF-R1500",   // 51-pin FPC, 0.5mm pitch
                "FI-X30HL-T",          // 30-pin FPC, 0.5mm pitch
                "FI-X40HL-T",          // 40-pin FPC
                "FI-RE31S-HF",         // 31-pin FPC
                "FI-W17P-HFE"          // 17-pin high-current FPC
            };

            for (String mpn : realMpns) {
                assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                        mpn + " should be recognized as CONNECTOR");
                assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JAE, registry),
                        mpn + " should be recognized as CONNECTOR_JAE");
            }
        }

        @Test
        void shouldMatchRealUSBCConnectors() {
            // Real JAE USB Type-C connector part numbers
            String[] realMpns = {
                "DX07S024JA1R1500",   // USB-C receptacle, SMT, 24-pin
                "DX07B024JA1R1500",   // USB-C plug
                "DX07S016JA1"         // USB-C 16-pin variant
            };

            for (String mpn : realMpns) {
                assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                        mpn + " should be recognized as CONNECTOR");
                assertTrue(handler.isUSBTypeC(mpn),
                        mpn + " should be recognized as USB Type-C");
            }
        }

        @Test
        void shouldMatchRealAutomotiveConnectors() {
            // Real JAE automotive connector part numbers
            String[] realMpns = {
                "MX34036NF1",         // 36-pin automotive waterproof
                "MX34016NF1",         // 16-pin automotive waterproof
                "MX34024SF2"          // 24-pin automotive
            };

            for (String mpn : realMpns) {
                assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                        mpn + " should be recognized as CONNECTOR");
                assertTrue(handler.isAutomotiveGrade(mpn),
                        mpn + " should be recognized as automotive grade");
            }
        }
    }
}
