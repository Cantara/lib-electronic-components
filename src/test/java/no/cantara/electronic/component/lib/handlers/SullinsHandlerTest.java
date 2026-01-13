package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SullinsHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for SullinsHandler.
 * Tests SBH, PPPC, PRPC, NPPN, and SFH connector series.
 *
 * Handler patterns:
 * - SBH: SBHxx-PBPC-Dxx-ST/RA-BK (box headers, dual row)
 * - PPPC: PPPCxxxLFxx-RC (female headers)
 * - PRPC: PRPCxxxSAAx-RC (male headers)
 * - NPPN: NPPNxxxBFCx-RC (pin headers)
 * - SFH: SFHxx-PBPC-Dxx-RA-BK (FFC/FPC connectors)
 *
 * Sullins MPN Structure Examples:
 * - SBH11-PBPC-D10-ST-BK: SBH series, 11mm height, D10=20 pins dual row, straight, black
 * - PPPC081LFBN-RC: PPPC series, 08 pins, 1 row, lead-free, through-hole
 * - PRPC040SAAN-RC: PRPC series, 04 pins, 0 = single row, through-hole
 */
class SullinsHandlerTest {

    private static SullinsHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SullinsHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SBH Series Detection (Box Headers)")
    class SBHTests {
        @ParameterizedTest
        @ValueSource(strings = {
                "SBH11-PBPC-D10-ST-BK",
                "SBH11-PBPC-D05-ST-BK",
                "SBH11-PBPC-D13-RA-BK",
                "SBH11-PBPC-D20-ST-BK",
                "SBH11-NBPC-D17-RA-BK"
        })
        void shouldMatchSBHSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @CsvSource({
                "SBH11-PBPC-D10-ST-BK, SBH",
                "SBH11-PBPC-D05-RA-BK, SBH"
        })
        void shouldExtractSeriesFromSBH(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldExtractPinCountFromSBH() {
            // D10 = 10 positions * 2 rows = 20 pins
            assertEquals(20, handler.extractPinCount("SBH11-PBPC-D10-ST-BK"));
            // D05 = 5 positions * 2 rows = 10 pins
            assertEquals(10, handler.extractPinCount("SBH11-PBPC-D05-ST-BK"));
            // D17 = 17 positions * 2 rows = 34 pins
            assertEquals(34, handler.extractPinCount("SBH11-PBPC-D17-RA-BK"));
        }

        @Test
        void shouldIdentifyBoxHeaderAsShrouded() {
            assertTrue(handler.isShrouded("SBH11-PBPC-D10-ST-BK"));
        }

        @Test
        void shouldDetectOrientation() {
            assertEquals("Straight", handler.getOrientation("SBH11-PBPC-D10-ST-BK"));
            assertEquals("Right Angle", handler.getOrientation("SBH11-PBPC-D13-RA-BK"));
        }
    }

    @Nested
    @DisplayName("PPPC Series Detection (Female Headers)")
    class PPPCTests {
        @ParameterizedTest
        @ValueSource(strings = {
                "PPPC081LFBN-RC",
                "PPPC101LFBN-RC",
                "PPPC201LFBN-RC",
                "PPPC401LFBN-RC",
                "PPPC082LFBN-RC"
        })
        void shouldMatchPPPCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @CsvSource({
                "PPPC081LFBN-RC, PPPC",
                "PPPC201LFBN-RC, PPPC"
        })
        void shouldExtractSeriesFromPPPC(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldExtractPinCountFromPPPC() {
            assertEquals(8, handler.extractPinCount("PPPC081LFBN-RC"));
            assertEquals(10, handler.extractPinCount("PPPC101LFBN-RC"));
            assertEquals(20, handler.extractPinCount("PPPC201LFBN-RC"));
            assertEquals(40, handler.extractPinCount("PPPC401LFBN-RC"));
        }

        @Test
        void shouldExtractRowCountFromPPPC() {
            assertEquals(1, handler.getRowCount("PPPC081LFBN-RC")); // Single row
            assertEquals(2, handler.getRowCount("PPPC082LFBN-RC")); // Dual row
        }

        @Test
        void shouldIdentifyFemaleGender() {
            assertEquals("Female", handler.getGender("PPPC081LFBN-RC"));
        }

        @Test
        void shouldNotBeShrouded() {
            assertFalse(handler.isShrouded("PPPC081LFBN-RC"));
        }
    }

    @Nested
    @DisplayName("PRPC Series Detection (Male Headers)")
    class PRPCTests {
        @ParameterizedTest
        @ValueSource(strings = {
                "PRPC040SAAN-RC",
                "PRPC100SAAN-RC",
                "PRPC200SAAN-RC",
                "PRPC400SAAN-RC",
                "PRPC041SAAN-RC"
        })
        void shouldMatchPRPCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @CsvSource({
                "PRPC040SAAN-RC, PRPC",
                "PRPC200SAAN-RC, PRPC"
        })
        void shouldExtractSeriesFromPRPC(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldExtractPinCountFromPRPC() {
            assertEquals(4, handler.extractPinCount("PRPC040SAAN-RC"));
            assertEquals(10, handler.extractPinCount("PRPC100SAAN-RC"));
            assertEquals(40, handler.extractPinCount("PRPC400SAAN-RC"));
        }

        @Test
        void shouldIdentifyMaleGender() {
            assertEquals("Male", handler.getGender("PRPC040SAAN-RC"));
        }
    }

    @Nested
    @DisplayName("NPPN Series Detection (Pin Headers)")
    class NPPNTests {
        @ParameterizedTest
        @ValueSource(strings = {
                "NPPN101BFCN-RC",
                "NPPN201BFCN-RC",
                "NPPN361BFCN-RC",
                "NPPN401BFCN-RC"
        })
        void shouldMatchNPPNSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @CsvSource({
                "NPPN101BFCN-RC, NPPN",
                "NPPN361BFCN-RC, NPPN"
        })
        void shouldExtractSeriesFromNPPN(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldExtractPinCountFromNPPN() {
            assertEquals(10, handler.extractPinCount("NPPN101BFCN-RC"));
            assertEquals(20, handler.extractPinCount("NPPN201BFCN-RC"));
            assertEquals(36, handler.extractPinCount("NPPN361BFCN-RC"));
        }

        @Test
        void shouldIdentifyMaleGender() {
            assertEquals("Male", handler.getGender("NPPN101BFCN-RC"));
        }
    }

    @Nested
    @DisplayName("SFH Series Detection (FFC/FPC Connectors)")
    class SFHTests {
        @ParameterizedTest
        @ValueSource(strings = {
                "SFH11-PBPC-D17-RA-BK",
                "SFH11-PBPC-D10-ST-BK",
                "SFH11-NBPC-D25-RA-BK"
        })
        void shouldMatchSFHSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @CsvSource({
                "SFH11-PBPC-D17-RA-BK, SFH",
                "SFH11-PBPC-D10-ST-BK, SFH"
        })
        void shouldExtractSeriesFromSFH(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldExtractPinCountFromSFH() {
            assertEquals(34, handler.extractPinCount("SFH11-PBPC-D17-RA-BK"));
            assertEquals(20, handler.extractPinCount("SFH11-PBPC-D10-ST-BK"));
            assertEquals(50, handler.extractPinCount("SFH11-NBPC-D25-RA-BK"));
        }

        @Test
        void shouldIdentifyFFCConnector() {
            assertEquals("FFC/FPC Connector", handler.getApplicationType("SFH11-PBPC-D17-RA-BK"));
        }

        @Test
        void shouldIdentifyAsShrouded() {
            assertTrue(handler.isShrouded("SFH11-PBPC-D17-RA-BK"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @Test
        void shouldExtractPackageCodeFromSBH() {
            assertEquals("Through-Hole Straight", handler.extractPackageCode("SBH11-PBPC-D10-ST-BK"));
            assertEquals("Through-Hole Right Angle", handler.extractPackageCode("SBH11-PBPC-D13-RA-BK"));
        }

        @Test
        void shouldExtractPackageCodeFromSFH() {
            assertEquals("Through-Hole Right Angle", handler.extractPackageCode("SFH11-PBPC-D17-RA-BK"));
            assertEquals("Through-Hole Straight", handler.extractPackageCode("SFH11-PBPC-D10-ST-BK"));
        }

        @Test
        void shouldExtractPackageCodeFromPPPC() {
            assertEquals("Through-Hole", handler.extractPackageCode("PPPC081LFBN-RC"));
        }

        @Test
        void shouldExtractPackageCodeFromPRPC() {
            assertEquals("Through-Hole", handler.extractPackageCode("PRPC040SAAN-RC"));
        }

        @Test
        void shouldExtractPackageCodeFromNPPN() {
            assertEquals("Through-Hole", handler.extractPackageCode("NPPN101BFCN-RC"));
        }

        @Test
        void shouldHandleSMTSuffix() {
            // Simulate an SMT variant (if available)
            assertTrue(handler.extractPackageCode("PPPC081LFBN-SC").contains("SMT") ||
                    handler.extractPackageCode("PPPC081LFBN-SC").equals("SC"));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetPitch() {
            assertEquals("2.54", handler.getPitch("SBH11-PBPC-D10-ST-BK"));
            assertEquals("2.54", handler.getPitch("PPPC081LFBN-RC"));
            assertEquals("2.54", handler.getPitch("PRPC040SAAN-RC"));
            assertEquals("2.54", handler.getPitch("NPPN101BFCN-RC"));
            assertEquals("1.27", handler.getPitch("SFH11-PBPC-D17-RA-BK"));
        }

        @Test
        void shouldGetMountingType() {
            assertEquals("THT", handler.getMountingType("SBH11-PBPC-D10-ST-BK"));
            assertEquals("THT", handler.getMountingType("PPPC081LFBN-RC"));
        }

        @Test
        void shouldGetRatedCurrent() {
            assertEquals(3.0, handler.getRatedCurrent("SBH11-PBPC-D10-ST-BK"));
            assertEquals(3.0, handler.getRatedCurrent("PPPC081LFBN-RC"));
            assertEquals(3.0, handler.getRatedCurrent("PRPC040SAAN-RC"));
            assertEquals(3.0, handler.getRatedCurrent("NPPN101BFCN-RC"));
            assertEquals(1.0, handler.getRatedCurrent("SFH11-PBPC-D17-RA-BK"));
        }

        @Test
        void shouldGetGender() {
            assertEquals("Male (Shrouded)", handler.getGender("SBH11-PBPC-D10-ST-BK"));
            assertEquals("Female", handler.getGender("PPPC081LFBN-RC"));
            assertEquals("Male", handler.getGender("PRPC040SAAN-RC"));
            assertEquals("Male", handler.getGender("NPPN101BFCN-RC"));
            assertEquals("Male (Shrouded)", handler.getGender("SFH11-PBPC-D17-RA-BK"));
        }

        @Test
        void shouldGetApplicationType() {
            assertEquals("Box Header", handler.getApplicationType("SBH11-PBPC-D10-ST-BK"));
            assertEquals("Female Header/Socket", handler.getApplicationType("PPPC081LFBN-RC"));
            assertEquals("Male Header", handler.getApplicationType("PRPC040SAAN-RC"));
            assertEquals("Pin Header", handler.getApplicationType("NPPN101BFCN-RC"));
            assertEquals("FFC/FPC Connector", handler.getApplicationType("SFH11-PBPC-D17-RA-BK"));
        }

        @Test
        void shouldGetSeriesDescription() {
            assertEquals("Box Header", handler.getSeriesDescription("SBH11-PBPC-D10-ST-BK"));
            assertEquals("Female Header", handler.getSeriesDescription("PPPC081LFBN-RC"));
            assertEquals("Male Header", handler.getSeriesDescription("PRPC040SAAN-RC"));
            assertEquals("Pin Header", handler.getSeriesDescription("NPPN101BFCN-RC"));
            assertEquals("FFC/FPC Connector", handler.getSeriesDescription("SFH11-PBPC-D17-RA-BK"));
        }

        @Test
        void shouldDetectContactPlating() {
            // Test lead-free tin plating (LF in part number)
            assertEquals("Lead-Free Tin", handler.getContactPlating("PPPC081LFBN-RC"));
        }

        @Test
        void shouldGetRowCount() {
            assertEquals(2, handler.getRowCount("SBH11-PBPC-D10-ST-BK")); // Box headers are dual row
            assertEquals(2, handler.getRowCount("SFH11-PBPC-D17-RA-BK")); // SFH also dual row
            assertEquals(1, handler.getRowCount("PPPC081LFBN-RC")); // 1 in position = single row
            assertEquals(2, handler.getRowCount("PPPC082LFBN-RC")); // 2 in position = dual row
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
            assertFalse(handler.isOfficialReplacement(null, "SBH11-PBPC-D10-ST-BK"));
            assertFalse(handler.isOfficialReplacement("SBH11-PBPC-D10-ST-BK", null));
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
            assertFalse(handler.matches("SBH11-PBPC-D10-ST-BK", null, registry));
        }

        @Test
        void shouldHandleCaseInsensitivity() {
            // Test lowercase
            assertEquals("SBH", handler.extractSeries("sbh11-pbpc-d10-st-bk"));
            assertEquals("PPPC", handler.extractSeries("pppc081lfbn-rc"));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("SBH11-PBPC-D10-ST-BK", "PPPC101LFBN-RC"));
            assertFalse(handler.isOfficialReplacement("PRPC040SAAN-RC", "NPPN041BFCN-RC"));
        }

        @Test
        void shouldNotReplaceWithDifferentPinCount() {
            assertFalse(handler.isOfficialReplacement("PPPC081LFBN-RC", "PPPC101LFBN-RC"));
            assertFalse(handler.isOfficialReplacement("PRPC040SAAN-RC", "PRPC100SAAN-RC"));
        }

        @Test
        void shouldReplaceCompatibleMountingTypes() {
            // Same series, same pin count, compatible mounting types should be replaceable
            assertTrue(handler.isOfficialReplacement("SBH11-PBPC-D10-ST-BK", "SBH11-NBPC-D10-ST-BK"));
        }

        @Test
        void shouldAllowReplacementWithinSameSeries() {
            // Same pin count, same series - should be compatible
            assertTrue(handler.isOfficialReplacement("PPPC081LFBN-RC", "PPPC081LFBN-RC"));
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
        void shouldReturnImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () ->
                    types.add(ComponentType.RESISTOR));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {
        @Test
        void canInstantiateDirectly() {
            SullinsHandler directHandler = new SullinsHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("SBH11-PBPC-D10-ST-BK",
                    ComponentType.CONNECTOR, directRegistry));
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
    @DisplayName("Pattern Matching Accuracy")
    class PatternMatchingTests {
        @Test
        void shouldNotMatchUnrelatedMPNs() {
            // These should NOT match - they're from other manufacturers
            assertFalse(handler.matches("DF13-2P-DSA", ComponentType.CONNECTOR, registry),
                    "Should not match Hirose connector");
            assertFalse(handler.matches("43045-0802", ComponentType.CONNECTOR, registry),
                    "Should not match Molex connector");
            assertFalse(handler.matches("LM7805", ComponentType.CONNECTOR, registry),
                    "Should not match voltage regulator");
        }

        @Test
        void shouldMatchGenericSullinsPatterns() {
            // Test generic S-prefix patterns
            assertTrue(handler.matches("SAB11-TEST-01", ComponentType.CONNECTOR, registry));
            // Test generic P-prefix patterns
            assertTrue(handler.matches("PXXX123ABC-RC", ComponentType.CONNECTOR, registry));
            // Test generic N-prefix patterns
            assertTrue(handler.matches("NXYZ456DEF-RC", ComponentType.CONNECTOR, registry));
        }
    }

    @Nested
    @DisplayName("Real-World Part Numbers")
    class RealWorldTests {
        @ParameterizedTest
        @ValueSource(strings = {
                // Common Sullins headers from distributor catalogs
                "SBH11-PBPC-D10-ST-BK",
                "SBH11-PBPC-D13-RA-BK",
                "PPPC081LFBN-RC",
                "PPPC101LFBN-RC",
                "PRPC040SAAN-RC",
                "PRPC100SAAN-RC",
                "NPPN101BFCN-RC",
                "NPPN361BFCN-RC"
        })
        void shouldMatchRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    "Should match real-world Sullins part number: " + mpn);
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    "Should extract series from: " + mpn);
        }

        @Test
        void documentRealWorldPartDetails() {
            String[] mpns = {
                    "SBH11-PBPC-D10-ST-BK",
                    "PPPC081LFBN-RC",
                    "PRPC040SAAN-RC",
                    "NPPN101BFCN-RC",
                    "SFH11-PBPC-D17-RA-BK"
            };

            for (String mpn : mpns) {
                System.out.println("\n=== " + mpn + " ===");
                System.out.println("  Series: " + handler.extractSeries(mpn));
                System.out.println("  Description: " + handler.getSeriesDescription(mpn));
                System.out.println("  Pin Count: " + handler.extractPinCount(mpn));
                System.out.println("  Row Count: " + handler.getRowCount(mpn));
                System.out.println("  Gender: " + handler.getGender(mpn));
                System.out.println("  Mounting: " + handler.getMountingType(mpn));
                System.out.println("  Orientation: " + handler.getOrientation(mpn));
                System.out.println("  Pitch: " + handler.getPitch(mpn) + "mm");
                System.out.println("  Current Rating: " + handler.getRatedCurrent(mpn) + "A");
                System.out.println("  Shrouded: " + handler.isShrouded(mpn));
                System.out.println("  Application: " + handler.getApplicationType(mpn));
            }
        }
    }
}
