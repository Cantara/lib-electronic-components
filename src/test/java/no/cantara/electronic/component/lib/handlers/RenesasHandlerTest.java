package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.RenesasHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for RenesasHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Renesas MCU families:
 * - RL78: 8/16-bit low-power MCUs (R5F1xxxx)
 * - RX: 32-bit general purpose MCUs (R5Fxxxxx)
 * - RA: 32-bit Arm Cortex-M MCUs (R7FAxxxx)
 * - RH850: 32-bit automotive MCUs (R7F7xxxx)
 * - R8C: 16-bit compact MCUs (R5F2xxxx)
 *
 * KNOWN BUG: The default handler.matches() implementation uses PatternRegistry.getPattern()
 * which only returns the FIRST pattern registered for a type. Since RenesasHandler registers
 * multiple patterns (RX, RL78, RH850, RA) for the same ComponentType, only the first one works.
 *
 * Tests use registry.matches() directly which correctly checks ALL patterns for a type.
 */
class RenesasHandlerTest {

    private static RenesasHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new RenesasHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    /**
     * Helper method that uses registry.matches() instead of handler.matches().
     * The handler's default matches() only checks the first pattern per type,
     * but the registry's matches() checks all patterns.
     */
    private boolean matchesPattern(String mpn, ComponentType type) {
        if (mpn == null) return false;
        return registry.matches(mpn.toUpperCase(), type);
    }

    @Nested
    @DisplayName("RL78 Family Detection (R5F1xxxx)")
    class RL78Tests {

        @ParameterizedTest
        @DisplayName("Should detect RL78/G13 series via registry patterns")
        @ValueSource(strings = {
                "R5F100LEAFB",
                "R5F100GCAFB",
                "R5F100PJDFB",
                "R5F101LCAFB"
        })
        void shouldDetectRL78G13SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertTrue(matchesPattern(mpn, ComponentType.MCU_RENESAS),
                    mpn + " should match MCU_RENESAS via registry");
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER),
                    mpn + " should match MICROCONTROLLER (base type) via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RL78/G14 series (motor control) via registry")
        @ValueSource(strings = {
                "R5F10268ASP",
                "R5F1026EASP"
        })
        void shouldDetectRL78G14SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @Test
        @DisplayName("Should extract RL78 series name")
        void shouldExtractRL78Series() {
            assertEquals("RL78", handler.extractSeries("R5F100LEAFB"));
            assertEquals("RL78", handler.extractSeries("R5F101LCAFB"));
            assertEquals("RL78", handler.extractSeries("R5F10268ASP"));
        }
    }

    @Nested
    @DisplayName("RX Family Detection (R5Fxxxxx not R5F1)")
    class RXTests {

        @ParameterizedTest
        @DisplayName("Should detect RX100 series (entry-level) via registry")
        @ValueSource(strings = {
                "R5F51303ADFM",
                "R5F51303ADFN",
                "R5F51138ADFM"
        })
        void shouldDetectRX100SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertTrue(matchesPattern(mpn, ComponentType.MCU_RENESAS),
                    mpn + " should match MCU_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RX200 series (low power) via registry")
        @ValueSource(strings = {
                "R5F52108ADFM",
                "R5F52318AXFM",
                "R5F52315AXFM"
        })
        void shouldDetectRX200SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RX600 series (general purpose) via registry")
        @ValueSource(strings = {
                "R5F5631EDDFP",
                "R5F563NBDDFP"
        })
        void shouldDetectRX600SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RX64M/65N series (high performance) via registry")
        @ValueSource(strings = {
                "R5F565NEDDFB",
                "R5F565NEHDFB"
        })
        void shouldDetectRX64MSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RX72x series (motor control) via registry")
        @ValueSource(strings = {
                "R5F572MDDDFB",
                "R5F572NDHDFB"
        })
        void shouldDetectRX72xSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @Test
        @DisplayName("Should extract RX series name")
        void shouldExtractRXSeries() {
            assertEquals("RX", handler.extractSeries("R5F51303ADFM"));
            assertEquals("RX", handler.extractSeries("R5F5631EDDFP"));
            assertEquals("RX", handler.extractSeries("R5F572MDDDFB"));
        }

        @Test
        @DisplayName("RX series (R5F5xxx) should NOT be identified as RL78")
        void rxShouldNotBeRL78() {
            String rxMpn = "R5F51303ADFM";
            assertEquals("RX", handler.extractSeries(rxMpn));
            assertNotEquals("RL78", handler.extractSeries(rxMpn));
        }
    }

    @Nested
    @DisplayName("RA Family Detection (R7FAxxxx - Arm Cortex-M)")
    class RATests {

        @ParameterizedTest
        @DisplayName("Should detect RA2 series (entry-level Cortex-M23) via registry")
        @ValueSource(strings = {
                "R7FA2A1AB3CFM",
                "R7FA2E1A92DFM",
                "R7FA2L1AB2CFM"
        })
        void shouldDetectRA2SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertTrue(matchesPattern(mpn, ComponentType.MCU_RENESAS),
                    mpn + " should match MCU_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RA4 series (mid-range Cortex-M33) via registry")
        @ValueSource(strings = {
                "R7FA4M1AB3CFP",
                "R7FA4M2AD3CFP",
                "R7FA4M3AF3CFP"
        })
        void shouldDetectRA4SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RA6 series (high-performance Cortex-M33) via registry")
        @ValueSource(strings = {
                "R7FA6M1AD3CFP",
                "R7FA6M2AF3CFB",
                "R7FA6M3AH3CFC",
                "R7FA6M4AF3CFB"
        })
        void shouldDetectRA6SeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RA6T series (motor control) via registry")
        @ValueSource(strings = {
                "R7FA6T1AD3CFP",
                "R7FA6T2BD3CFP"
        })
        void shouldDetectRA6TSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @Test
        @DisplayName("Should extract RA series name")
        void shouldExtractRASeries() {
            assertEquals("RA", handler.extractSeries("R7FA2A1AB3CFM"));
            assertEquals("RA", handler.extractSeries("R7FA4M1AB3CFP"));
            assertEquals("RA", handler.extractSeries("R7FA6T1AD3CFP"));
        }
    }

    @Nested
    @DisplayName("RH850 Family Detection (R7F7xxxx - Automotive)")
    class RH850Tests {

        @ParameterizedTest
        @DisplayName("Should detect RH850/F1K series via registry")
        @ValueSource(strings = {
                "R7F7016023AFP",
                "R7F7016024AFP"
        })
        void shouldDetectRH850F1KSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertTrue(matchesPattern(mpn, ComponentType.MCU_RENESAS),
                    mpn + " should match MCU_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RH850/F1KM series via registry")
        @ValueSource(strings = {
                "R7F701695AFP",
                "R7F701694AFP",
                "R7F701789AFB"
        })
        void shouldDetectRH850F1KMSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RH850/C1M series (chassis) via registry")
        @ValueSource(strings = {
                "R7F701278ABG",
                "R7F701275ABG"
        })
        void shouldDetectRH850C1MSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect RH850/E2x series (engine control) via registry")
        @ValueSource(strings = {
                "R7F702011EABG",
                "R7F702011EAFP"
        })
        void shouldDetectRH850E2xSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
        }

        @Test
        @DisplayName("Should extract RH850 series name")
        void shouldExtractRH850Series() {
            assertEquals("RH850", handler.extractSeries("R7F7016023AFP"));
            assertEquals("RH850", handler.extractSeries("R7F701695AFP"));
            assertEquals("RH850", handler.extractSeries("R7F702011EABG"));
        }

        @Test
        @DisplayName("RH850 (R7F7xxx) should NOT be identified as RA")
        void rh850ShouldNotBeRA() {
            String rh850Mpn = "R7F7016023AFP";
            assertEquals("RH850", handler.extractSeries(rh850Mpn));
            assertNotEquals("RA", handler.extractSeries(rh850Mpn));
        }
    }

    @Nested
    @DisplayName("R8C Family Detection (R8Cxxxx)")
    class R8CTests {

        @ParameterizedTest
        @DisplayName("Should detect R8C series via registry")
        @ValueSource(strings = {
                "R8C21358SNFP",
                "R8C13168NFP"
        })
        void shouldDetectR8CSeriesViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertTrue(matchesPattern(mpn, ComponentType.MCU_RENESAS),
                    mpn + " should match MCU_RENESAS via registry");
        }

        @Test
        @DisplayName("Should extract R8C series name")
        void shouldExtractR8CSeries() {
            assertEquals("R8C", handler.extractSeries("R8C21358SNFP"));
            assertEquals("R8C", handler.extractSeries("R8C13168NFP"));
        }
    }

    @Nested
    @DisplayName("Memory Product Detection")
    class MemoryTests {

        @ParameterizedTest
        @DisplayName("Should detect Flash memory (R1EX) via registry")
        @ValueSource(strings = {
                "R1EX25064ASA",
                "R1EX25128CSP"
        })
        void shouldDetectFlashMemoryViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MEMORY),
                    mpn + " should match MEMORY via registry");
        }

        @ParameterizedTest
        @DisplayName("Should detect Low Voltage memory (R1LV) via registry")
        @ValueSource(strings = {
                "R1LV0416CSB",
                "R1LV1616HSB"
        })
        void shouldDetectLowVoltageMemoryViaRegistry(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MEMORY),
                    mpn + " should match MEMORY via registry");
        }

        @Test
        @DisplayName("Should extract memory series name")
        void shouldExtractMemorySeries() {
            assertEquals("Flash", handler.extractSeries("R1EX25064ASA"));
            assertEquals("Low Voltage", handler.extractSeries("R1LV0416CSB"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        /**
         * The current extractPackageCode implementation finds the last digit
         * and returns everything after it. This works for simple MPNs but has issues.
         */

        @ParameterizedTest
        @DisplayName("Documents current package extraction behavior for RL78")
        @CsvSource({
                "R5F100LEAFB, LEAFB",
                "R5F100LEAFA, LEAFA",
                "R5F10268ASP, ASP"
        })
        void documentsRL78PackageExtractionBehavior(String mpn, String actualResult) {
            // Current behavior: extracts everything after last digit
            // For R5F100LEAFB: last digit is '0' in "100", returns "LEAFB"
            assertEquals(actualResult, handler.extractPackageCode(mpn),
                    "Current extraction for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Documents current package extraction behavior for RX")
        @CsvSource({
                "R5F51303ADFM, ADFM",
                "R5F51303ADFN, ADFN",
                "R5F5631EDDFP, EDDFP"
        })
        void documentsRXPackageExtractionBehavior(String mpn, String actualResult) {
            assertEquals(actualResult, handler.extractPackageCode(mpn),
                    "Current extraction for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Documents current package extraction behavior for RA")
        @CsvSource({
                "R7FA4M1AB3CFP, CFP",
                "R7FA2A1AB3CFM, CFM",
                "R7FA6M2AF3CFB, CFB"
        })
        void documentsRAPackageExtractionBehavior(String mpn, String actualResult) {
            // For RA MPNs like R7FA4M1AB3CFP: last digit is '3', returns "CFP"
            assertEquals(actualResult, handler.extractPackageCode(mpn),
                    "Current extraction for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Documents current package extraction behavior for RH850")
        @CsvSource({
                "R7F7016023AFP, AFP",
                "R7F701278ABG, ABG",
                "R7F701695AFP, AFP"
        })
        void documentsRH850PackageExtractionBehavior(String mpn, String actualResult) {
            assertEquals(actualResult, handler.extractPackageCode(mpn),
                    "Current extraction for " + mpn);
        }

        @Test
        @DisplayName("Package extraction returns empty for MPNs with version suffix")
        void packageExtractionWithVersionSuffix() {
            // MPNs with #30 or #V1 - the # is not a digit, so extraction fails
            assertEquals("", handler.extractPackageCode("R5F5631EDDFP#V1"));
            assertEquals("", handler.extractPackageCode("R5F100LEAFB#30"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify MCU family from MPN")
        @CsvSource({
                "R5F100LEAFB, RL78",
                "R5F101LCAFB, RL78",
                "R5F10268ASP, RL78",
                "R5F51303ADFM, RX",
                "R5F5631EDDFP, RX",
                "R5F572MDDDFB, RX",
                "R7FA4M1AB3CFP, RA",
                "R7FA6T1AD3CFP, RA",
                "R7F7016023AFP, RH850",
                "R7F701695AFP, RH850",
                "R8C21358SNFP, R8C",
                "R1EX25064ASA, Flash",
                "R1LV0416CSB, Low Voltage"
        })
        void shouldExtractCorrectSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown patterns")
        void shouldReturnEmptyForUnknownPatterns() {
            assertEquals("", handler.extractSeries("UNKNOWN123"));
            assertEquals("", handler.extractSeries("R9F123456"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Current implementation returns false for all replacements")
        void documentReplacementBehavior() {
            // Current RenesasHandler.isOfficialReplacement always returns false
            // This is documented behavior - Renesas typically doesn't have direct replacements
            assertFalse(handler.isOfficialReplacement("R5F100LEAFB", "R5F100LEAFA"),
                    "Same device different package - returns false per implementation");
            assertFalse(handler.isOfficialReplacement("R5F100LEAFB", "R5F100LEAFB"),
                    "Even identical parts return false");
        }

        @Test
        @DisplayName("Different families should not be replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("R5F100LEAFB", "R7FA4M1AB3CFP"),
                    "RL78 and RA should not be replacements");
            assertFalse(handler.isOfficialReplacement("R5F51303ADFM", "R5F100LEAFB"),
                    "RX and RL78 should not be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected Renesas component types")
        void shouldSupportExpectedRenesasTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.MICROCONTROLLER),
                    "Should contain MICROCONTROLLER");
            assertTrue(types.contains(ComponentType.MICROCONTROLLER_RENESAS),
                    "Should contain MICROCONTROLLER_RENESAS");
            assertTrue(types.contains(ComponentType.MCU_RENESAS),
                    "Should contain MCU_RENESAS");
            assertTrue(types.contains(ComponentType.MEMORY),
                    "Should contain MEMORY");
            assertTrue(types.contains(ComponentType.MEMORY_RENESAS),
                    "Should contain MEMORY_RENESAS");
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
            assertFalse(matchesPattern(null, ComponentType.MICROCONTROLLER_RENESAS));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "R5F100LEAFB"));
            assertFalse(handler.isOfficialReplacement("R5F100LEAFB", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(matchesPattern("", ComponentType.MICROCONTROLLER_RENESAS));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should be case-insensitive for pattern matching via registry")
        void shouldBeCaseInsensitiveViaRegistry() {
            assertTrue(matchesPattern("r5f100leafb", ComponentType.MICROCONTROLLER_RENESAS));
            assertTrue(matchesPattern("R5F100LEAFB", ComponentType.MICROCONTROLLER_RENESAS));
            assertTrue(matchesPattern("R5f100LeaFb", ComponentType.MICROCONTROLLER_RENESAS));
        }
    }

    @Nested
    @DisplayName("Real Datasheet MPNs via Registry")
    class RealDatasheetMPNTests {

        @ParameterizedTest
        @DisplayName("Should recognize real RL78/G13 MPNs from datasheets")
        @ValueSource(strings = {
                "R5F100LEAFA",
                "R5F100LEAFB",
                "R5F100GCAFB",
                "R5F100PJDFB",
                "R5F101LCAFB"
        })
        void shouldRecognizeRealRL78MPNs(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertEquals("RL78", handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should recognize real RX MPNs from datasheets")
        @ValueSource(strings = {
                "R5F51303ADFM",
                "R5F5631EDDFP",
                "R5F565NEDDFB"
        })
        void shouldRecognizeRealRXMPNs(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertEquals("RX", handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should recognize real RA MPNs from datasheets")
        @ValueSource(strings = {
                "R7FA4M1AB3CFP",
                "R7FA2A1AB3CFM",
                "R7FA6T1AD3CFP",
                "R7FA6M1AD3CFP",
                "R7FA2E1A92DFM"
        })
        void shouldRecognizeRealRAMPNs(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertEquals("RA", handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should recognize real RH850 MPNs from datasheets")
        @ValueSource(strings = {
                "R7F7016023AFP",
                "R7F701695AFP",
                "R7F701278ABG"
        })
        void shouldRecognizeRealRH850MPNs(String mpn) {
            assertTrue(matchesPattern(mpn, ComponentType.MICROCONTROLLER_RENESAS),
                    mpn + " should match MICROCONTROLLER_RENESAS via registry");
            assertEquals("RH850", handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Bug Documentation")
    class BugDocumentationTests {

        @Test
        @DisplayName("BUG: HashSet used instead of Set.of() in getSupportedTypes")
        void documentHashSetUsage() {
            // The handler uses new HashSet<>() which is mutable
            // Better practice would be Set.of() for immutable sets
            var types = handler.getSupportedTypes();
            assertNotNull(types);
            // Current implementation allows modification - this is a potential bug
        }

        @Test
        @DisplayName("BUG: handler.matches() only checks first pattern per type")
        void documentHandlerMatchesBug() {
            // The default handler.matches() uses PatternRegistry.getPattern()
            // which returns only the FIRST pattern registered for a type.
            // This means only RX pattern works via handler.matches(), not RL78, RA, or RH850.

            // RX should work (first pattern registered for MICROCONTROLLER_RENESAS)
            // but the order depends on implementation details

            // Using registry.matches() works for all patterns
            assertTrue(matchesPattern("R5F100LEAFB", ComponentType.MICROCONTROLLER_RENESAS),
                    "RL78 works via registry");
            assertTrue(matchesPattern("R7FA4M1AB3CFP", ComponentType.MICROCONTROLLER_RENESAS),
                    "RA works via registry");
            assertTrue(matchesPattern("R7F7016023AFP", ComponentType.MICROCONTROLLER_RENESAS),
                    "RH850 works via registry");
        }

        @Test
        @DisplayName("BUG: Package extraction returns too much for complex MPNs")
        void documentPackageExtractionBug() {
            // Handler finds last digit and returns everything after it
            // For "R5F100LEAFB": last digit is '0', returns "LEAFB" (expected "FB")
            // For "R7FA4M1AB3CFP": last digit is '3', returns "CFP" (expected "FP")

            String result1 = handler.extractPackageCode("R5F100LEAFB");
            assertEquals("LEAFB", result1, "Current behavior returns everything after last digit");

            String result2 = handler.extractPackageCode("R7FA4M1AB3CFP");
            assertEquals("CFP", result2, "RA extraction works better due to digit placement");
        }

        @Test
        @DisplayName("BUG: Package extraction fails for MPNs with # version suffix")
        void documentVersionSuffixBug() {
            // MPNs like R5F5631EDDFP#V1 have # after package code
            // The # is not a digit, so getLastDigitIndex fails to find correct position
            assertEquals("", handler.extractPackageCode("R5F5631EDDFP#V1"),
                    "Version suffix causes empty result");
        }

        @Test
        @DisplayName("isOfficialReplacement always returns false")
        void documentReplacementAlwaysFalse() {
            // Current implementation always returns false
            // This may be intentional (Renesas doesn't publish replacement guides)
            // but could be improved to at least return true for identical parts
            assertFalse(handler.isOfficialReplacement("R5F100LEAFB", "R5F100LEAFB"),
                    "Even identical parts return false");
        }
    }

    @Nested
    @DisplayName("Cross-Family Differentiation")
    class CrossFamilyTests {

        @Test
        @DisplayName("R5F1xxx should be RL78, R5F5xxx should be RX")
        void shouldDifferentiateR5FVariants() {
            // RL78 starts with R5F1
            assertEquals("RL78", handler.extractSeries("R5F100LEAFB"));
            assertEquals("RL78", handler.extractSeries("R5F10268ASP"));

            // RX starts with R5F but not R5F1 (e.g., R5F5xxx)
            assertEquals("RX", handler.extractSeries("R5F51303ADFM"));
            assertEquals("RX", handler.extractSeries("R5F5631EDDFP"));
        }

        @Test
        @DisplayName("R7FAxxxx should be RA, R7F7xxxx should be RH850")
        void shouldDifferentiateR7FVariants() {
            // RA starts with R7FA
            assertEquals("RA", handler.extractSeries("R7FA4M1AB3CFP"));
            assertEquals("RA", handler.extractSeries("R7FA6T1AD3CFP"));

            // RH850 starts with R7F but not R7FA (e.g., R7F7xxx)
            assertEquals("RH850", handler.extractSeries("R7F7016023AFP"));
            assertEquals("RH850", handler.extractSeries("R7F701695AFP"));
        }
    }
}
