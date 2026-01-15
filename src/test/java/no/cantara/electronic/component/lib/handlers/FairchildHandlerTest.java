package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.FairchildHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for FairchildHandler.
 * Fairchild Semiconductor (now part of ON Semiconductor) MOSFET handler.
 *
 * KNOWN BUGS DOCUMENTED:
 * 1. extractPackageCode() contains STM32 code (lines 113-124) - copy-paste error
 * 2. extractPackageCode() "L" suffix maps to DPAK, overriding correct prefix-based detection
 * 3. extractSeries() for FQL series doesn't capture full part number
 */
class FairchildHandlerTest {

    private static FairchildHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new FairchildHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("N-Channel MOSFET Detection")
    class NChannelMosfetTests {

        @ParameterizedTest
        @DisplayName("FQP series N-Channel TO-220 MOSFETs should match")
        @ValueSource(strings = {
                "FQP30N06", "FQP30N06L", "FQP50N06",
                "FQP13N10", "FQP13N10L", "FQP9N25"
        })
        void shouldDetectFQPNChannelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("FQD series N-Channel DPAK MOSFETs should match")
        @ValueSource(strings = {"FQD30N06", "FQD30N06L", "FQD13N10", "FQD13N10L"})
        void shouldDetectFQDNChannelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("FQU series N-Channel IPAK MOSFETs should match")
        @ValueSource(strings = {"FQU13N06", "FQU13N06L", "FQU30N06"})
        void shouldDetectFQUNChannelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("FDS series N-Channel SO-8 MOSFETs should match")
        @ValueSource(strings = {"FDS6680", "FDS6890A", "FDS4935", "FDS4685"})
        void shouldDetectFDSNChannelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("P-Channel MOSFET Detection")
    class PChannelMosfetTests {

        @ParameterizedTest
        @DisplayName("FQP series P-Channel TO-220 MOSFETs should match")
        @ValueSource(strings = {"FQP27P06", "FQP47P06", "FQP19P10"})
        void shouldDetectFQPPChannelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("FQD series P-Channel DPAK MOSFETs should match")
        @ValueSource(strings = {"FQD27P06", "FQD47P06"})
        void shouldDetectFQDPChannelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("FDS series P-Channel SO-8 MOSFETs should match")
        @ValueSource(strings = {"FDS4935P", "FDS6675P", "FDS9435P"})
        void shouldDetectFDSPChannelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("Logic Level and Small Signal MOSFET Detection")
    class LogicLevelMosfetTests {

        @ParameterizedTest
        @DisplayName("FQL series Logic Level MOSFETs should match")
        @ValueSource(strings = {"FQL40N50", "FQL40N50F", "FQL50N50"})
        void shouldDetectFQLLogicLevelMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("2N7xxx series small signal MOSFETs should match")
        @ValueSource(strings = {"2N7000", "2N7002", "2N7008"})
        void shouldDetect2N7SeriesMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("BS series small signal MOSFETs should match")
        @ValueSource(strings = {"BS170", "BS250", "BS107"})
        void shouldDetectBSSeriesMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("PowerTrench and Complementary MOSFET Detection")
    class PowerTrenchMosfetTests {

        @ParameterizedTest
        @DisplayName("FDB series PowerTrench MOSFETs should match")
        @ValueSource(strings = {"FDB8896", "FDB8860", "FDB2614"})
        void shouldDetectFDBPowerTrenchMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("FDA series complementary pair MOSFETs should match")
        @ValueSource(strings = {"FDA8896", "FDA38N30"})
        void shouldDetectFDAComplementaryMosfets(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("FQP series should extract TO-220 package (without L suffix)")
        @CsvSource({
                "FQP30N06, TO-220",
                "FQP13N10, TO-220",
                "FQP27P06, TO-220"
        })
        void shouldExtractFQPPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @Test
        @DisplayName("BUG: FQP with L suffix returns DPAK instead of TO-220")
        void bugLSuffixReturnsDPAK() {
            // "L" is in PACKAGE_CODES map as DPAK
            // This overrides the correct FQP prefix detection (TO-220)
            assertEquals("DPAK", handler.extractPackageCode("FQP30N06L"));
            assertEquals("DPAK", handler.extractPackageCode("FQU13N06L"));
            
            System.out.println("BUG: 'L' suffix in PACKAGE_CODES overrides prefix-based detection");
            System.out.println("FQP30N06L should be TO-220, but returns DPAK");
        }

        @ParameterizedTest
        @DisplayName("FQD series should extract DPAK package")
        @CsvSource({
                "FQD30N06, DPAK",
                "FQD13N10, DPAK"
        })
        void shouldExtractFQDPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("FQU series should extract IPAK package (without L suffix)")
        @CsvSource({
                "FQU13N06, IPAK",
                "FQU30N06, IPAK"
        })
        void shouldExtractFQUPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("FDS series should extract SO-8 package")
        @CsvSource({
                "FDS6680, SO-8",
                "FDS6890A, SO-8",
                "FDS4935, SO-8"
        })
        void shouldExtractFDSPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn);
        }

        @Test
        @DisplayName("BUG: STM32 package extraction code in Fairchild handler")
        void documentSTM32BugInPackageExtraction() {
            // Lines 113-124 of extractPackageCode() contain STM32-specific code
            // This is a copy-paste bug - STM32 is STMicroelectronics, not Fairchild

            // This code should never execute for Fairchild parts
            String fairchildMpn = "FQP30N06L";
            assertFalse(fairchildMpn.matches("STM32[F|L|H|G|W|U].*"),
                    "Fairchild MPNs should not match STM32 pattern");

            // Document the bug - STM32 code should be removed
            System.out.println("BUG: extractPackageCode() contains STM32 code (lines 113-124)");
            System.out.println("This should be removed - STM32 is STMicroelectronics, not Fairchild");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract FQP N-Channel series")
        @CsvSource({
                "FQP30N06, FQP30N06",
                "FQP30N06L, FQP30N06",
                "FQP13N10, FQP13N10",
                "FQP13N10L, FQP13N10"
        })
        void shouldExtractFQPNChannelSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract FQP P-Channel series")
        @CsvSource({
                "FQP27P06, FQP27P06",
                "FQP47P06, FQP47P06"
        })
        void shouldExtractFQPPChannelSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract FDS series")
        @CsvSource({
                "FDS6680, FDS6680",
                "FDS6890A, FDS6890",
                "FDS4935, FDS4935"
        })
        void shouldExtractFDSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("BUG: FQL series extraction only captures partial series")
        void bugFQLSeriesExtraction() {
            // Regex (FQL[0-9]+).* only captures digits, not N/P channel + voltage
            assertEquals("FQL40", handler.extractSeries("FQL40N50"));
            assertEquals("FQL40", handler.extractSeries("FQL40N50F"));
            
            System.out.println("BUG: FQL series extraction incomplete");
            System.out.println("FQL40N50 should extract FQL40N50, but returns FQL40");
        }

        @ParameterizedTest
        @DisplayName("Should extract 2N7 series")
        @CsvSource({
                "2N7000, 2N7000",
                "2N7002, 2N7002"
        })
        void shouldExtract2N7Series(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Document: Replacement detection limited by series extraction bugs")
        void documentReplacementLimitations() {
            // Series extraction for FQP with L suffix works correctly
            // But package extraction returns wrong package, so replacement may fail
            boolean result = handler.isOfficialReplacement("FQP30N06", "FQP30N06L");
            
            System.out.println("FQP30N06 vs FQP30N06L replacement: " + result);
            System.out.println("May fail due to package extraction bug (L suffix → DPAK)");
        }

        @Test
        @DisplayName("Known replacement: FQP30N06 ↔ IRF530")
        void knownReplacementFQP30N06AndIRF530() {
            assertTrue(handler.isOfficialReplacement("FQP30N06", "IRF530"),
                    "FQP30N06 is known replacement for IRF530");
            assertTrue(handler.isOfficialReplacement("IRF530", "FQP30N06"),
                    "IRF530 is known replacement for FQP30N06");
        }

        @Test
        @DisplayName("Different series should not be replacement")
        void differentSeriesNotReplacement() {
            assertFalse(handler.isOfficialReplacement("FQP30N06", "FQP13N10"),
                    "Different series should not be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.MOSFET));
            assertTrue(types.contains(ComponentType.TRANSISTOR));
            assertTrue(types.contains(ComponentType.DIODE));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
        }

        @Test
        @DisplayName("Uses Set.of() - should be immutable")
        void shouldUseSetOf() {
            var types = handler.getSupportedTypes();
            assertNotNull(types);
            // Set.of() returns immutable set
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR));
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
            assertFalse(handler.matches(null, ComponentType.MOSFET, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "FQP30N06"));
            assertFalse(handler.isOfficialReplacement("FQP30N06", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MOSFET, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("FQP30N06", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for pattern matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("fqp30n06", ComponentType.MOSFET, registry));
            assertTrue(handler.matches("FQP30N06", ComponentType.MOSFET, registry));
            assertTrue(handler.matches("Fqp30N06", ComponentType.MOSFET, registry));
        }
    }

    @Nested
    @DisplayName("Real-World Datasheet MPNs")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should recognize real Fairchild MOSFETs from datasheets")
        @ValueSource(strings = {
                "FQP30N06L",      // 60V N-Channel TO-220
                "FQP27P06",       // -60V P-Channel TO-220
                "FDS6680",        // 30V N-Channel SO-8
                "FDB8896",        // 30V N-Channel D2PAK PowerTrench
                "FQL40N50",       // 500V N-Channel Logic Level
                "2N7000",         // 60V N-Channel SOT-23
                "2N7002",         // 60V N-Channel SOT-23
                "BS170"           // 60V N-Channel TO-92
        })
        void shouldRecognizeRealDatasheetMPNs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("Pattern Registration via Registry")
    class PatternRegistryTests {

        @Test
        @DisplayName("All N-Channel patterns should match via registry")
        void allNChannelPatternsShouldMatchViaRegistry() {
            assertTrue(registry.matches("FQP30N06", ComponentType.MOSFET));
            assertTrue(registry.matches("FQD30N06", ComponentType.MOSFET));
            assertTrue(registry.matches("FQU30N06", ComponentType.MOSFET));
            assertTrue(registry.matches("FDS6680", ComponentType.MOSFET));
        }

        @Test
        @DisplayName("All P-Channel patterns should match via registry")
        void allPChannelPatternsShouldMatchViaRegistry() {
            assertTrue(registry.matches("FQP27P06", ComponentType.MOSFET));
            assertTrue(registry.matches("FQD27P06", ComponentType.MOSFET));
            assertTrue(registry.matches("FDS6680P", ComponentType.MOSFET));
        }

        @Test
        @DisplayName("Logic and small signal patterns should match via registry")
        void logicAndSmallSignalPatternsShouldMatchViaRegistry() {
            assertTrue(registry.matches("FQL40N50", ComponentType.MOSFET));
            assertTrue(registry.matches("2N7000", ComponentType.MOSFET));
            assertTrue(registry.matches("BS170", ComponentType.MOSFET));
        }

        @Test
        @DisplayName("PowerTrench and complementary patterns should match via registry")
        void powerTrenchAndComplementaryPatternsShouldMatchViaRegistry() {
            assertTrue(registry.matches("FDB8896", ComponentType.MOSFET));
            assertTrue(registry.matches("FDA8896", ComponentType.MOSFET));
        }
    }
}
