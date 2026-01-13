package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.YangjieHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for YangjieHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Yangjie Technology (Chinese semiconductor manufacturer) components.
 *
 * Yangjie product lines tested:
 * - YJ series: Rectifier diodes (YJ1N4007, YJ1N5408)
 * - MBR series: Schottky rectifiers (MBR1045, MBR2045, MBR3045)
 * - SMBJ/SMAJ series: TVS diodes (SMBJ5.0A, SMBJ15A, SMAJ5.0A)
 * - SS/SK series: Schottky diodes (SS14, SS24, SS34, SK34, SK54)
 * - 2N/MMBT series: Transistors (2N7002, MMBT2222, MMBT3904)
 * - YJB/YJD series: Bridge rectifiers (YJB1010, YJDB107)
 */
class YangjieHandlerTest {

    private static YangjieHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new YangjieHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("YJ Rectifier Diode Detection")
    class YJRectifierDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect YJ1N rectifier diodes")
        @ValueSource(strings = {"YJ1N4001", "YJ1N4007", "YJ1N5408", "YJ1N5404"})
        void shouldDetectYJ1NDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect standard 1N series diodes")
        @ValueSource(strings = {"1N4001", "1N4007", "1N5408", "1N4148"})
        void shouldDetect1NDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("MBR Schottky Rectifier Detection")
    class MBRSchottkyTests {

        @ParameterizedTest
        @DisplayName("Should detect MBR Schottky rectifiers")
        @ValueSource(strings = {"MBR1045", "MBR2045", "MBR3045", "MBR1060", "MBR2060"})
        void shouldDetectMBRSchottky(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect MBR with package suffixes")
        @ValueSource(strings = {"MBR1045CT", "MBR2045PT", "MBR3045FCT"})
        void shouldDetectMBRWithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("TVS Diode Detection")
    class TVSDiodeTests {

        @ParameterizedTest
        @DisplayName("Should detect SMBJ TVS diodes")
        @ValueSource(strings = {"SMBJ5.0A", "SMBJ15A", "SMBJ24A", "SMBJ33CA", "SMBJ58A"})
        void shouldDetectSMBJDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SMAJ TVS diodes")
        @ValueSource(strings = {"SMAJ5.0A", "SMAJ15A", "SMAJ24CA", "SMAJ33A", "SMAJ58CA"})
        void shouldDetectSMAJDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect TVS diodes with decimal voltage ratings")
        @ValueSource(strings = {"SMBJ5.0A", "SMAJ5.0CA", "SMBJ6.5A", "SMAJ6.5CA"})
        void shouldDetectTVSWithDecimalVoltage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("SS/SK Schottky Diode Detection")
    class SSSchottkyTests {

        @ParameterizedTest
        @DisplayName("Should detect SS Schottky diodes")
        @ValueSource(strings = {"SS12", "SS14", "SS24", "SS34", "SS54"})
        void shouldDetectSSDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SK Schottky diodes")
        @ValueSource(strings = {"SK34", "SK54", "SK56", "SK36"})
        void shouldDetectSKDiodes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect SS/SK with package suffixes")
        @ValueSource(strings = {"SS14L", "SS24FL", "SK34L", "SK54FL"})
        void shouldDetectSSWithSuffix(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("Transistor Detection")
    class TransistorTests {

        @ParameterizedTest
        @DisplayName("Should detect 2N series transistors")
        @ValueSource(strings = {"2N2222", "2N3904", "2N3906", "2N4401", "2N4403"})
        void shouldDetect2NTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect MMBT series transistors")
        @ValueSource(strings = {"MMBT2222", "MMBT3904", "MMBT3906", "MMBT5551"})
        void shouldDetectMMBTTransistors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should match TRANSISTOR");
        }

        @Test
        @DisplayName("Should detect 2N7002 as MOSFET")
        void shouldDetect2N7002AsMOSFET() {
            assertTrue(handler.matches("2N7002", ComponentType.MOSFET, registry),
                    "2N7002 should match MOSFET");
            assertTrue(handler.matches("2N7002K", ComponentType.MOSFET, registry),
                    "2N7002K should match MOSFET");
        }
    }

    @Nested
    @DisplayName("Bridge Rectifier Detection")
    class BridgeRectifierTests {

        @ParameterizedTest
        @DisplayName("Should detect YJB bridge rectifiers")
        @ValueSource(strings = {"YJB1010", "YJB1008", "YJB2010", "YJB108"})
        void shouldDetectYJBBridge(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should detect YJDB bridge rectifiers")
        @ValueSource(strings = {"YJDB107", "YJDB157", "YJDB207"})
        void shouldDetectYJDBBridge(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract MBR package codes")
        @CsvSource({
                "MBR1045CT, TO-220",
                "MBR2045PT, TO-247",
                "MBR3045FCT, TO-220F"
        })
        void shouldExtractMBRPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract TVS package codes")
        @CsvSource({
                "SMBJ5.0A, SMB",
                "SMBJ15A, SMB",
                "SMAJ5.0A, SMA",
                "SMAJ15A, SMA"
        })
        void shouldExtractTVSPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SS/SK package codes")
        @CsvSource({
                "SS14L, SOD-123",
                "SS24FL, SOD-123FL",
                "SS34, DO-214AC"
        })
        void shouldExtractSSPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract transistor package codes")
        @CsvSource({
                "MMBT2222, SOT-23",
                "MMBT3904, SOT-23",
                "2N3904, TO-92",
                "2N7002, TO-92"
        })
        void shouldExtractTransistorPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract 1N diode package codes")
        @CsvSource({
                "1N4007, DO-41",
                "1N4007A, DO-41",
                "1N4148, DO-41"
        })
        void shouldExtract1NPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract diode series")
        @CsvSource({
                "YJ1N4007, YJ",
                "MBR1045CT, MBR",
                "SMBJ15A, SMBJ",
                "SMAJ15A, SMAJ",
                "SS14, SS",
                "SK34, SK"
        })
        void shouldExtractDiodeSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract transistor series")
        @CsvSource({
                "MMBT2222, MMBT",
                "MMBT3904, MMBT",
                "2N3904, 2N",
                "2N3906, 2N"
        })
        void shouldExtractTransistorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should extract 2N7002 as specific series")
        void shouldExtract2N7002AsSeries() {
            assertEquals("2N7002", handler.extractSeries("2N7002"),
                    "2N7002 should return its specific series");
            assertEquals("2N7002", handler.extractSeries("2N7002K"),
                    "2N7002K should return 2N7002 series");
        }

        @ParameterizedTest
        @DisplayName("Should extract bridge rectifier series")
        @CsvSource({
                "YJB1010, YJB",
                "YJDB107, YJDB"
        })
        void shouldExtractBridgeSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract 1N specific series")
        @CsvSource({
                "1N4148, 1N4148",
                "1N914, 1N914",
                "1N4007, 1N4xxx",
                "1N5408, 1N5xxx",
                "1N4742, 1N47xx"
        })
        void shouldExtract1NSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Nested
        @DisplayName("Rectifier Diode Replacements")
        class RectifierReplacementTests {

            @Test
            @DisplayName("Higher voltage 1N400x should replace lower")
            void higherVoltageReplacesLower() {
                assertTrue(handler.isOfficialReplacement("1N4007", "1N4001"),
                        "1N4007 (1000V) should replace 1N4001 (50V)");
                assertTrue(handler.isOfficialReplacement("1N4004", "1N4001"),
                        "1N4004 (400V) should replace 1N4001 (50V)");
            }

            @Test
            @DisplayName("Lower voltage should NOT replace higher")
            void lowerVoltageNotReplacesHigher() {
                assertFalse(handler.isOfficialReplacement("1N4001", "1N4007"),
                        "1N4001 (50V) should NOT replace 1N4007 (1000V)");
            }

            @Test
            @DisplayName("Same 1N voltage is a valid replacement")
            void sameVoltageIsReplacement() {
                assertTrue(handler.isOfficialReplacement("1N4007", "1N4007"),
                        "Same part should be valid replacement");
            }
        }

        @Nested
        @DisplayName("MBR Schottky Replacements")
        class MBRReplacementTests {

            @Test
            @DisplayName("Same MBR electrical specs with different package are replacements")
            void sameMBRSpecsAreReplacements() {
                assertTrue(handler.isOfficialReplacement("MBR1045CT", "MBR1045PT"),
                        "MBR1045CT and MBR1045PT should be replacements");
            }

            @Test
            @DisplayName("Different MBR electrical specs are NOT replacements")
            void differentMBRSpecsNotReplacements() {
                assertFalse(handler.isOfficialReplacement("MBR1045CT", "MBR2045CT"),
                        "MBR1045 and MBR2045 should NOT be replacements (different current)");
            }
        }

        @Nested
        @DisplayName("TVS Diode Replacements")
        class TVSReplacementTests {

            @Test
            @DisplayName("Same voltage TVS diodes are replacements")
            void sameVoltageTVSAreReplacements() {
                assertTrue(handler.isOfficialReplacement("SMBJ15A", "SMBJ15A"),
                        "Same TVS part should be replacement");
            }

            @Test
            @DisplayName("Bidirectional TVS can replace unidirectional")
            void bidirectionalCanReplaceUnidirectional() {
                assertTrue(handler.isOfficialReplacement("SMBJ15CA", "SMBJ15A"),
                        "SMBJ15CA (bidirectional) can replace SMBJ15A (unidirectional)");
            }

            @Test
            @DisplayName("Different voltage TVS are NOT replacements")
            void differentVoltageTVSNotReplacements() {
                assertFalse(handler.isOfficialReplacement("SMBJ15A", "SMBJ24A"),
                        "SMBJ15A and SMBJ24A should NOT be replacements");
            }
        }

        @Nested
        @DisplayName("Schottky SS/SK Replacements")
        class SchottkySSReplacementTests {

            @Test
            @DisplayName("Same current rating SS/SK are replacements")
            void sameSsRatingAreReplacements() {
                assertTrue(handler.isOfficialReplacement("SS14", "SS14L"),
                        "SS14 and SS14L should be replacements");
            }

            @Test
            @DisplayName("Different current rating SS are NOT replacements")
            void differentSsRatingNotReplacements() {
                assertFalse(handler.isOfficialReplacement("SS14", "SS24"),
                        "SS14 and SS24 should NOT be replacements (different current)");
            }
        }

        @Nested
        @DisplayName("Transistor Replacements")
        class TransistorReplacementTests {

            @Test
            @DisplayName("Same MMBT transistor with suffix variations are replacements")
            void sameMMBTAreReplacements() {
                assertTrue(handler.isOfficialReplacement("MMBT2222", "MMBT2222A"),
                        "MMBT2222 and MMBT2222A should be replacements");
            }

            @Test
            @DisplayName("Different MMBT transistors are NOT replacements")
            void differentMMBTNotReplacements() {
                assertFalse(handler.isOfficialReplacement("MMBT2222", "MMBT3904"),
                        "MMBT2222 and MMBT3904 should NOT be replacements");
            }

            @Test
            @DisplayName("Same 2N transistor are replacements")
            void same2NAreReplacements() {
                assertTrue(handler.isOfficialReplacement("2N3904", "2N3904"),
                        "Same 2N3904 should be replacement");
            }
        }

        @Nested
        @DisplayName("Cross-Series Replacements")
        class CrossSeriesReplacementTests {

            @Test
            @DisplayName("Different series should NOT be replacements")
            void differentSeriesNotReplacements() {
                assertFalse(handler.isOfficialReplacement("SS14", "SK34"),
                        "SS14 and SK34 should NOT be replacements");
                assertFalse(handler.isOfficialReplacement("SMBJ15A", "SMAJ15A"),
                        "SMBJ and SMAJ should NOT be replacements");
                assertFalse(handler.isOfficialReplacement("MBR1045", "SS14"),
                        "MBR and SS should NOT be replacements");
            }
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "1N4007"));
            assertFalse(handler.isOfficialReplacement("1N4007", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.DIODE),
                    "Should support DIODE");
            assertTrue(types.contains(ComponentType.TRANSISTOR),
                    "Should support TRANSISTOR");
            assertTrue(types.contains(ComponentType.MOSFET),
                    "Should support MOSFET");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of() (immutable)")
        void getSupportedTypesShouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes should return immutable set");
        }

        @Test
        @DisplayName("Should have exactly 3 supported types")
        void shouldHaveExactlyThreeTypes() {
            var types = handler.getSupportedTypes();
            assertEquals(3, types.size(),
                    "Should have exactly 3 supported types: DIODE, TRANSISTOR, MOSFET");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.DIODE, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "MBR1045"));
            assertFalse(handler.isOfficialReplacement("MBR1045", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.DIODE, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("MBR1045", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMpns() {
            assertTrue(handler.matches("mbr1045", ComponentType.DIODE, registry),
                    "Should match lowercase MPN");
            assertTrue(handler.matches("smbj15a", ComponentType.DIODE, registry),
                    "Should match lowercase TVS MPN");
            assertTrue(handler.matches("mmbt2222", ComponentType.TRANSISTOR, registry),
                    "Should match lowercase transistor MPN");
        }

        @Test
        @DisplayName("Should handle mixed case MPNs")
        void shouldHandleMixedCaseMpns() {
            assertTrue(handler.matches("MBr1045Ct", ComponentType.DIODE, registry),
                    "Should match mixed case MPN");
            assertTrue(handler.matches("SmBj15A", ComponentType.DIODE, registry),
                    "Should match mixed case TVS MPN");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            YangjieHandler directHandler = new YangjieHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("MBR1045", ComponentType.DIODE, directRegistry));
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
    @DisplayName("Non-Matching MPNs")
    class NonMatchingTests {

        @ParameterizedTest
        @DisplayName("Should NOT match unrelated MPNs as DIODE")
        @ValueSource(strings = {"LM7805", "STM32F103", "ATMEGA328P", "74HC00"})
        void shouldNotMatchUnrelatedAsDiode(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should NOT match DIODE");
        }

        @ParameterizedTest
        @DisplayName("Should NOT match unrelated MPNs as TRANSISTOR")
        @ValueSource(strings = {"BC547", "TIP31", "IRF540"})
        void shouldNotMatchUnrelatedAsTransistor(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.TRANSISTOR, registry),
                    mpn + " should NOT match TRANSISTOR");
        }

        @ParameterizedTest
        @DisplayName("Should NOT match unrelated MPNs as MOSFET")
        @ValueSource(strings = {"IRF540", "IRLZ44", "FQP30N06"})
        void shouldNotMatchUnrelatedAsMosfet(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should NOT match MOSFET");
        }
    }

    @Nested
    @DisplayName("Real-World Part Number Tests")
    class RealWorldTests {

        @Test
        @DisplayName("Should correctly identify common Yangjie parts")
        void shouldIdentifyCommonParts() {
            // Common rectifier diodes
            assertTrue(handler.matches("YJ1N4007", ComponentType.DIODE, registry));
            assertTrue(handler.matches("1N4007", ComponentType.DIODE, registry));

            // Common Schottky rectifiers
            assertTrue(handler.matches("MBR1045CT", ComponentType.DIODE, registry));
            assertTrue(handler.matches("MBR2045CT", ComponentType.DIODE, registry));

            // Common TVS diodes
            assertTrue(handler.matches("SMBJ5.0A", ComponentType.DIODE, registry));
            assertTrue(handler.matches("SMBJ15CA", ComponentType.DIODE, registry));

            // Common small signal Schottky
            assertTrue(handler.matches("SS14", ComponentType.DIODE, registry));
            assertTrue(handler.matches("SK34", ComponentType.DIODE, registry));

            // Common transistors
            assertTrue(handler.matches("MMBT2222", ComponentType.TRANSISTOR, registry));
            assertTrue(handler.matches("2N3904", ComponentType.TRANSISTOR, registry));

            // Common MOSFET
            assertTrue(handler.matches("2N7002", ComponentType.MOSFET, registry));
        }

        @Test
        @DisplayName("Should extract correct package codes for common parts")
        void shouldExtractCorrectPackagesForCommonParts() {
            assertEquals("TO-220", handler.extractPackageCode("MBR1045CT"));
            assertEquals("SMB", handler.extractPackageCode("SMBJ15A"));
            assertEquals("SMA", handler.extractPackageCode("SMAJ15A"));
            assertEquals("SOT-23", handler.extractPackageCode("MMBT2222"));
            assertEquals("DO-41", handler.extractPackageCode("1N4007"));
        }

        @Test
        @DisplayName("Should extract correct series for common parts")
        void shouldExtractCorrectSeriesForCommonParts() {
            assertEquals("MBR", handler.extractSeries("MBR1045CT"));
            assertEquals("SMBJ", handler.extractSeries("SMBJ15A"));
            assertEquals("SS", handler.extractSeries("SS14"));
            assertEquals("MMBT", handler.extractSeries("MMBT2222"));
            assertEquals("1N4xxx", handler.extractSeries("1N4007"));
        }
    }
}
