package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.CyntecHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CyntecHandler.
 *
 * Tests pattern matching, package code extraction, series extraction,
 * inductance value extraction, and replacement detection for Cyntec
 * power inductors and modules.
 *
 * Cyntec products covered:
 * - PCMC series: Power inductors
 * - VCMD series: Molded power inductors
 * - MCPA series: Automotive power inductors
 * - CMC series: Common mode chokes
 */
class CyntecHandlerTest {

    private static CyntecHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new CyntecHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("PCMC Power Inductor Detection")
    class PCMCTests {

        @ParameterizedTest
        @DisplayName("Should detect PCMC power inductors with type variant")
        @ValueSource(strings = {
                "PCMC063T-1R0MN",
                "PCMC063T-2R2MN",
                "PCMC063T-4R7MN",
                "PCMC063T-100MN",
                "PCMC063T-101MN"
        })
        void shouldDetectPCMCInductorsWithType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect PCMC power inductors with 4-digit size code")
        @ValueSource(strings = {
                "PCMC0504T-1R0MN",
                "PCMC0403T-2R2MN",
                "PCMC0605T-4R7MN"
        })
        void shouldDetectPCMCInductorsWithFourDigitSize(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect PCMC power inductors without type variant")
        @ValueSource(strings = {
                "PCMC063-1R0MN",
                "PCMC063-2R2MN",
                "PCMC0504-4R7MN"
        })
        void shouldDetectPCMCInductorsWithoutType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract PCMC series with size and type")
        void shouldExtractPCMCSeries() {
            assertEquals("PCMC063T", handler.extractSeries("PCMC063T-1R0MN"));
            assertEquals("PCMC0504T", handler.extractSeries("PCMC0504T-2R2MN"));
            assertEquals("PCMC063", handler.extractSeries("PCMC063-1R0MN"));
        }
    }

    @Nested
    @DisplayName("VCMD Molded Power Inductor Detection")
    class VCMDTests {

        @ParameterizedTest
        @DisplayName("Should detect VCMD molded power inductors")
        @ValueSource(strings = {
                "VCMD063T-1R0MN",
                "VCMD063T-2R2MN",
                "VCMD063T-4R7MN",
                "VCMD0504T-100MN",
                "VCMD0403T-101MN"
        })
        void shouldDetectVCMDInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect VCMD molded inductors without type variant")
        @ValueSource(strings = {
                "VCMD063-1R0MN",
                "VCMD0504-2R2MN"
        })
        void shouldDetectVCMDInductorsWithoutType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract VCMD series with size and type")
        void shouldExtractVCMDSeries() {
            assertEquals("VCMD063T", handler.extractSeries("VCMD063T-1R0MN"));
            assertEquals("VCMD0504T", handler.extractSeries("VCMD0504T-2R2MN"));
            assertEquals("VCMD063", handler.extractSeries("VCMD063-1R0MN"));
        }
    }

    @Nested
    @DisplayName("MCPA Automotive Inductor Detection")
    class MCPATests {

        @ParameterizedTest
        @DisplayName("Should detect MCPA automotive power inductors")
        @ValueSource(strings = {
                "MCPA0504-1R0MN",
                "MCPA0504-2R2MN",
                "MCPA0605-4R7MN",
                "MCPA0807-100MN",
                "MCPA1008-101MN"
        })
        void shouldDetectMCPAInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract MCPA series with size code")
        void shouldExtractMCPASeries() {
            assertEquals("MCPA0504", handler.extractSeries("MCPA0504-1R0MN"));
            assertEquals("MCPA0605", handler.extractSeries("MCPA0605-2R2MN"));
            assertEquals("MCPA0807", handler.extractSeries("MCPA0807-4R7MN"));
        }
    }

    @Nested
    @DisplayName("CMC Common Mode Choke Detection")
    class CMCTests {

        @ParameterizedTest
        @DisplayName("Should detect CMC common mode chokes")
        @ValueSource(strings = {
                "CMC0503-471M",
                "CMC0503-102M",
                "CMC0604-222M",
                "CMC0805-103M"
        })
        void shouldDetectCMCChokes(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract CMC series with size code")
        void shouldExtractCMCSeries() {
            assertEquals("CMC0503", handler.extractSeries("CMC0503-471M"));
            assertEquals("CMC0604", handler.extractSeries("CMC0604-222M"));
            assertEquals("CMC0805", handler.extractSeries("CMC0805-103M"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package type from series prefix")
        @CsvSource({
                "PCMC063T-1R0MN, Power Inductor",
                "VCMD063T-2R2MN, Molded Power Inductor",
                "MCPA0504-1R0MN, Automotive Power Inductor",
                "CMC0503-471M, Common Mode Choke"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty string for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractPackageCode("INVALID123"));
            assertEquals("", handler.extractPackageCode("ABC1234"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series with size code and type")
        @CsvSource({
                "PCMC063T-1R0MN, PCMC063T",
                "PCMC0504T-2R2MN, PCMC0504T",
                "VCMD063T-4R7MN, VCMD063T",
                "VCMD0403T-100MN, VCMD0403T",
                "MCPA0504-1R0MN, MCPA0504",
                "MCPA0605-2R2MN, MCPA0605",
                "CMC0503-471M, CMC0503",
                "CMC0604-222M, CMC0604"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty string for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("INVALID"));
        }
    }

    @Nested
    @DisplayName("Inductance Value Extraction")
    class InductanceValueTests {

        @ParameterizedTest
        @DisplayName("Should extract inductance from R-notation")
        @CsvSource({
                "PCMC063T-1R0MN, 1.0uH",
                "PCMC063T-2R2MN, 2.2uH",
                "PCMC063T-4R7MN, 4.7uH",
                "VCMD063T-1R0MN, 1.0uH",
                "VCMD063T-R47MN, 470nH",
                "MCPA0504-1R0MN, 1.0uH",
                "MCPA0504-2R2MN, 2.2uH"
        })
        void shouldExtractInductanceFromRNotation(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inductance from 3-digit code")
        @CsvSource({
                "PCMC063T-100MN, 10.0uH",
                "PCMC063T-101MN, 100.0uH",
                "CMC0503-471M, 470.0uH",
                "CMC0503-102M, 1.0mH",
                "CMC0604-222M, 2.2mH"
        })
        void shouldExtractInductanceFrom3DigitCode(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractInductanceValue(null));
            assertEquals("", handler.extractInductanceValue(""));
            assertEquals("", handler.extractInductanceValue("INVALID"));
        }
    }

    @Nested
    @DisplayName("Size Code Extraction")
    class SizeCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract 3-digit size code")
        @CsvSource({
                "PCMC063T-1R0MN, 063",
                "VCMD063T-2R2MN, 063"
        })
        void shouldExtract3DigitSizeCode(String mpn, String expectedSize) {
            assertEquals(expectedSize, handler.extractSizeCode(mpn),
                    "Size code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract 4-digit size code")
        @CsvSource({
                "PCMC0504T-1R0MN, 0504",
                "VCMD0403T-2R2MN, 0403",
                "MCPA0504-1R0MN, 0504",
                "CMC0503-471M, 0503"
        })
        void shouldExtract4DigitSizeCode(String mpn, String expectedSize) {
            assertEquals(expectedSize, handler.extractSizeCode(mpn),
                    "Size code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractSizeCode(null));
            assertEquals("", handler.extractSizeCode(""));
            assertEquals("", handler.extractSizeCode("INVALID"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and value should be replacements")
        void sameSeriesAndValueShouldBeReplacements() {
            // Same inductor, different packaging
            assertTrue(handler.isOfficialReplacement("PCMC063T-1R0MN", "PCMC063T-1R0MB"),
                    "Same value with different packaging should be replaceable");
        }

        @Test
        @DisplayName("Same series but different value should NOT be replacements")
        void differentValueShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("PCMC063T-1R0MN", "PCMC063T-2R2MN"),
                    "Different inductance values should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different series should NOT be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("PCMC063T-1R0MN", "VCMD063T-1R0MN"),
                    "Different series should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different size should NOT be replacements")
        void differentSizeShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("PCMC063T-1R0MN", "PCMC0504T-1R0MN"),
                    "Different sizes should not be replaceable");
        }

        @Test
        @DisplayName("Different tolerance codes should be replacements if same series and value")
        void differentToleranceShouldBeReplacements() {
            // Same inductor, different tolerance (M=20% vs K=10%)
            assertTrue(handler.isOfficialReplacement("PCMC063T-1R0MN", "PCMC063T-1R0KN"),
                    "Same value with different tolerance should be replaceable");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.INDUCTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractInductanceValue(null));
            assertEquals("", handler.extractSizeCode(null));
            assertFalse(handler.isOfficialReplacement(null, "PCMC063T-1R0MN"));
            assertFalse(handler.isOfficialReplacement("PCMC063T-1R0MN", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.INDUCTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractInductanceValue(""));
            assertEquals("", handler.extractSizeCode(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("PCMC063T-1R0MN", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMPNs() {
            assertTrue(handler.matches("pcmc063t-1r0mn", ComponentType.INDUCTOR, registry),
                    "Should handle lowercase MPN");
        }

        @Test
        @DisplayName("Should not match non-Cyntec parts")
        void shouldNotMatchNonCyntecParts() {
            assertFalse(handler.matches("XAL4020-222ME", ComponentType.INDUCTOR, registry),
                    "Should not match Coilcraft inductor");
            assertFalse(handler.matches("GRM155R61A104KA01D", ComponentType.INDUCTOR, registry),
                    "Should not match Murata capacitor");
            assertFalse(handler.matches("SRN4018-100M", ComponentType.INDUCTOR, registry),
                    "Should not match Bourns inductor");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.INDUCTOR),
                    "Should support INDUCTOR");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");

            // Verify Set.of() is used (immutable set)
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.CAPACITOR);
            }, "getSupportedTypes() should return immutable set");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of() not HashSet")
        void shouldUseSetOfNotHashSet() {
            var types = handler.getSupportedTypes();
            // Set.of() returns an immutable set, HashSet does not
            assertTrue(types.getClass().getName().contains("Immutable") ||
                       types.getClass().getName().contains("Set12") ||
                       types.getClass().getName().contains("SetN"),
                    "Should use immutable Set (Set.of()), not HashSet");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            CyntecHandler directHandler = new CyntecHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("PCMC063T-1R0MN", ComponentType.INDUCTOR, directRegistry),
                    "Direct handler should match PCMC inductor");
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
    @DisplayName("IC Type Matching")
    class ICTypeMatchingTests {

        @ParameterizedTest
        @DisplayName("Should match IC type for all series")
        @ValueSource(strings = {
                "PCMC063T-1R0MN",
                "VCMD063T-2R2MN",
                "MCPA0504-1R0MN",
                "CMC0503-471M"
        })
        void shouldMatchICType(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should also match IC type");
        }
    }

    @Nested
    @DisplayName("MPN Structure Documentation")
    class MPNStructureTests {

        @Test
        @DisplayName("Document PCMC MPN structure")
        void documentPCMCMPNStructure() {
            // MPN: PCMC063T-1R0MN
            // Structure:
            // - PCMC = Series (power inductor)
            // - 063 = Size code (6.3mm)
            // - T = Type/variant
            // - 1R0 = Inductance (1.0uH, R=decimal point)
            // - M = Tolerance (M=+/-20%)
            // - N = Packaging (N=tape & reel)
            String mpn = "PCMC063T-1R0MN";
            System.out.println("MPN: " + mpn);
            System.out.println("Series: " + handler.extractSeries(mpn));
            System.out.println("Package: " + handler.extractPackageCode(mpn));
            System.out.println("Inductance: " + handler.extractInductanceValue(mpn));
            System.out.println("Size Code: " + handler.extractSizeCode(mpn));
        }

        @Test
        @DisplayName("Document MCPA MPN structure")
        void documentMCPAMPNStructure() {
            // MPN: MCPA0504-1R0MN
            // Structure:
            // - MCPA = Series (automotive power inductor)
            // - 0504 = Size code (5.0mm x 4.0mm)
            // - 1R0 = Inductance (1.0uH)
            // - M = Tolerance (M=+/-20%)
            // - N = Packaging (N=tape & reel)
            String mpn = "MCPA0504-1R0MN";
            System.out.println("MPN: " + mpn);
            System.out.println("Series: " + handler.extractSeries(mpn));
            System.out.println("Package: " + handler.extractPackageCode(mpn));
            System.out.println("Inductance: " + handler.extractInductanceValue(mpn));
            System.out.println("Size Code: " + handler.extractSizeCode(mpn));
        }

        @Test
        @DisplayName("Document CMC MPN structure")
        void documentCMCMPNStructure() {
            // MPN: CMC0503-471M
            // Structure:
            // - CMC = Series (common mode choke)
            // - 0503 = Size code (5.0mm x 3.0mm)
            // - 471 = Inductance (47 x 10^1 uH = 470uH)
            // - M = Tolerance (M=+/-20%)
            String mpn = "CMC0503-471M";
            System.out.println("MPN: " + mpn);
            System.out.println("Series: " + handler.extractSeries(mpn));
            System.out.println("Package: " + handler.extractPackageCode(mpn));
            System.out.println("Inductance: " + handler.extractInductanceValue(mpn));
            System.out.println("Size Code: " + handler.extractSizeCode(mpn));
        }
    }
}
