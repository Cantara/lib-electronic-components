package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ProTekDevicesHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ProTekDevicesHandler.
 * Tests pattern matching, package code extraction, series extraction, and component specifications.
 *
 * ProTek Devices Products Covered:
 * - TVS Diodes: TVS series (TVS03500, TVS05500, TVS15000)
 * - ESD Protection: GBLC series (GBLC03C, GBLC05C, GBLC15C)
 * - Automotive TVS: PSM series (PSM712, PSM712-LF)
 * - Ultra-Low Capacitance: ULC series (ULC0512, ULC0524)
 * - Surface Mount TVS: SMD series (SMD0512, SMD1512)
 */
class ProTekDevicesHandlerTest {

    private static ProTekDevicesHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation to avoid circular initialization issues
        handler = new ProTekDevicesHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("TVS Series Detection - Standard TVS Diodes")
    class TVSSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect TVS series parts")
        @ValueSource(strings = {"TVS03500", "TVS05500", "TVS12000", "TVS15000", "TVS24000", "TVS33000"})
        void shouldDetectTVSSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE type");
        }

        @Test
        @DisplayName("TVS series should extract correct series")
        void shouldExtractTVSSeries() {
            assertEquals("TVS", handler.extractSeries("TVS03500"));
            assertEquals("TVS", handler.extractSeries("TVS15000"));
        }

        @Test
        @DisplayName("TVS series should extract correct package")
        void shouldExtractTVSPackage() {
            assertEquals("SMB", handler.extractPackageCode("TVS03500"));
            assertEquals("SMB", handler.extractPackageCode("TVS15000"));
        }

        @ParameterizedTest
        @DisplayName("TVS series should extract correct voltage")
        @CsvSource({
            "TVS03500, 3.5",
            "TVS05500, 5.5",
            "TVS12000, 12",
            "TVS15000, 15",
            "TVS24000, 24",
            "TVS33000, 33"
        })
        void shouldExtractTVSVoltage(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltage(mpn),
                    mpn + " should have voltage " + expectedVoltage);
        }

        @Test
        @DisplayName("TVS series should not be bidirectional by default")
        void shouldNotBeBidirectional() {
            assertFalse(handler.isBidirectional("TVS05500"));
            assertFalse(handler.isBidirectional("TVS15000"));
        }
    }

    @Nested
    @DisplayName("GBLC Series Detection - ESD Protection")
    class GBLCSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect GBLC series parts")
        @ValueSource(strings = {"GBLC03C", "GBLC05C", "GBLC12C", "GBLC15C", "GBLC24C", "GBLC33C"})
        void shouldDetectGBLCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE type");
        }

        @Test
        @DisplayName("GBLC series should extract correct series")
        void shouldExtractGBLCSeries() {
            assertEquals("GBLC", handler.extractSeries("GBLC03C"));
            assertEquals("GBLC", handler.extractSeries("GBLC15C"));
        }

        @Test
        @DisplayName("GBLC series should extract SOT-23 package")
        void shouldExtractGBLCPackage() {
            assertEquals("SOT-23", handler.extractPackageCode("GBLC03C"));
            assertEquals("SOT-23", handler.extractPackageCode("GBLC15C"));
        }

        @ParameterizedTest
        @DisplayName("GBLC series should extract correct voltage")
        @CsvSource({
            "GBLC03C, 3.3",
            "GBLC05C, 5",
            "GBLC12C, 12",
            "GBLC15C, 15",
            "GBLC24C, 24"
        })
        void shouldExtractGBLCVoltage(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltage(mpn),
                    mpn + " should have voltage " + expectedVoltage);
        }

        @Test
        @DisplayName("GBLC series with C suffix should be bidirectional")
        void shouldBeBidirectional() {
            assertTrue(handler.isBidirectional("GBLC03C"));
            assertTrue(handler.isBidirectional("GBLC15C"));
        }

        @Test
        @DisplayName("GBLC series should have typical ESD power rating")
        void shouldHaveCorrectPowerRating() {
            assertEquals(200, handler.getPowerRating("GBLC03C"));
            assertEquals(200, handler.getPowerRating("GBLC15C"));
        }
    }

    @Nested
    @DisplayName("PSM Series Detection - Automotive/RS-232 Protection")
    class PSMSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect PSM series parts")
        @ValueSource(strings = {"PSM712", "PSM712-LF", "PSM05", "PSM12", "PSM24"})
        void shouldDetectPSMSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE type");
        }

        @Test
        @DisplayName("PSM series should extract correct series")
        void shouldExtractPSMSeries() {
            assertEquals("PSM", handler.extractSeries("PSM712"));
            assertEquals("PSM", handler.extractSeries("PSM712-LF"));
        }

        @Test
        @DisplayName("PSM series should extract SOIC-8 package")
        void shouldExtractPSMPackage() {
            assertEquals("SOIC-8", handler.extractPackageCode("PSM712"));
            assertEquals("SOIC-8", handler.extractPackageCode("PSM712-LF"));
        }

        @Test
        @DisplayName("PSM712 should extract 7V voltage")
        void shouldExtractPSM712Voltage() {
            assertEquals("7", handler.extractVoltage("PSM712"));
            assertEquals("7", handler.extractVoltage("PSM712-LF"));
        }

        @Test
        @DisplayName("PSM series should be bidirectional")
        void shouldBeBidirectional() {
            assertTrue(handler.isBidirectional("PSM712"));
            assertTrue(handler.isBidirectional("PSM05"));
        }

        @Test
        @DisplayName("PSM series with -LF suffix should be lead-free")
        void shouldDetectLeadFree() {
            assertFalse(handler.isLeadFree("PSM712"));
            assertTrue(handler.isLeadFree("PSM712-LF"));
            assertTrue(handler.isLeadFree("psm712-lf")); // Case insensitive
        }

        @Test
        @DisplayName("PSM series should have dual channel")
        void shouldHaveDualChannel() {
            assertEquals(2, handler.getLineCount("PSM712"));
        }

        @Test
        @DisplayName("PSM series should have 400W power rating")
        void shouldHaveCorrectPowerRating() {
            assertEquals(400, handler.getPowerRating("PSM712"));
        }
    }

    @Nested
    @DisplayName("ULC Series Detection - Ultra-Low Capacitance ESD")
    class ULCSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect ULC series parts")
        @ValueSource(strings = {"ULC0512", "ULC0524", "ULC1212", "ULC1224", "ULC0508"})
        void shouldDetectULCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE type");
        }

        @Test
        @DisplayName("ULC series should extract correct series")
        void shouldExtractULCSeries() {
            assertEquals("ULC", handler.extractSeries("ULC0512"));
            assertEquals("ULC", handler.extractSeries("ULC0524"));
        }

        @ParameterizedTest
        @DisplayName("ULC series should extract correct package based on line count")
        @CsvSource({
            "ULC0512, SSOP-16",
            "ULC0524, SSOP-28",
            "ULC0508, SOT-23"
        })
        void shouldExtractULCPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    mpn + " should have package " + expectedPackage);
        }

        @ParameterizedTest
        @DisplayName("ULC series should extract correct voltage")
        @CsvSource({
            "ULC0512, 5",
            "ULC0524, 5",
            "ULC1212, 12",
            "ULC3312, 33"
        })
        void shouldExtractULCVoltage(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltage(mpn),
                    mpn + " should have voltage " + expectedVoltage);
        }

        @Test
        @DisplayName("ULC series should be bidirectional")
        void shouldBeBidirectional() {
            assertTrue(handler.isBidirectional("ULC0512"));
            assertTrue(handler.isBidirectional("ULC0524"));
        }

        @ParameterizedTest
        @DisplayName("ULC series should extract correct line count")
        @CsvSource({
            "ULC0512, 12",
            "ULC0524, 24",
            "ULC0508, 8"
        })
        void shouldExtractLineCount(String mpn, int expectedLines) {
            assertEquals(expectedLines, handler.getLineCount(mpn),
                    mpn + " should have " + expectedLines + " lines");
        }

        @Test
        @DisplayName("ULC series should have 150W power rating")
        void shouldHaveCorrectPowerRating() {
            assertEquals(150, handler.getPowerRating("ULC0512"));
        }
    }

    @Nested
    @DisplayName("SMD Series Detection - Surface Mount TVS")
    class SMDSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect SMD series parts")
        @ValueSource(strings = {"SMD0512", "SMD0524", "SMD1012", "SMD1512", "SMD3012"})
        void shouldDetectSMDSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE type");
        }

        @Test
        @DisplayName("SMD series should extract correct series")
        void shouldExtractSMDSeries() {
            assertEquals("SMD", handler.extractSeries("SMD0512"));
            assertEquals("SMD", handler.extractSeries("SMD1512"));
        }

        @ParameterizedTest
        @DisplayName("SMD series should extract correct package based on power")
        @CsvSource({
            "SMD0512, SMA",
            "SMD1012, SMB",
            "SMD1512, SMC",
            "SMD3012, SMC"
        })
        void shouldExtractSMDPackage(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    mpn + " should have package " + expectedPackage);
        }

        @ParameterizedTest
        @DisplayName("SMD series should extract correct voltage")
        @CsvSource({
            "SMD0512, 12",
            "SMD0524, 24",
            "SMD1515, 15",
            "SMD1533, 33"
        })
        void shouldExtractSMDVoltage(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltage(mpn),
                    mpn + " should have voltage " + expectedVoltage);
        }

        @ParameterizedTest
        @DisplayName("SMD series should extract correct power rating")
        @CsvSource({
            "SMD0512, 500",
            "SMD1012, 1000",
            "SMD1512, 1500",
            "SMD3012, 3000"
        })
        void shouldExtractPowerRating(String mpn, int expectedPower) {
            assertEquals(expectedPower, handler.getPowerRating(mpn),
                    mpn + " should have power rating " + expectedPower + "W");
        }
    }

    @Nested
    @DisplayName("SP Series Detection - Surge Protection")
    class SPSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect SP series parts")
        @ValueSource(strings = {"SP05", "SP12", "SP24", "SP33", "SP100"})
        void shouldDetectSPSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE type");
        }

        @Test
        @DisplayName("SP series should extract correct series")
        void shouldExtractSPSeries() {
            assertEquals("SP", handler.extractSeries("SP05"));
            assertEquals("SP", handler.extractSeries("SP24"));
        }

        @Test
        @DisplayName("SP series should extract SOT-23 package")
        void shouldExtractSPPackage() {
            assertEquals("SOT-23", handler.extractPackageCode("SP05"));
            assertEquals("SOT-23", handler.extractPackageCode("SP24"));
        }

        @ParameterizedTest
        @DisplayName("SP series should extract correct voltage")
        @CsvSource({
            "SP05, 05",
            "SP12, 12",
            "SP24, 24",
            "SP100, 100"
        })
        void shouldExtractSPVoltage(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltage(mpn),
                    mpn + " should have voltage " + expectedVoltage);
        }
    }

    @Nested
    @DisplayName("LC Series Detection - Low Capacitance TVS")
    class LCSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect LC series parts")
        @ValueSource(strings = {"LC03", "LC05", "LC12", "LC15", "LC24"})
        void shouldDetectLCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE type");
        }

        @Test
        @DisplayName("LC series should extract correct series")
        void shouldExtractLCSeries() {
            assertEquals("LC", handler.extractSeries("LC03"));
            assertEquals("LC", handler.extractSeries("LC15"));
        }

        @Test
        @DisplayName("LC series should extract SOT-23 package")
        void shouldExtractLCPackage() {
            assertEquals("SOT-23", handler.extractPackageCode("LC03"));
            assertEquals("SOT-23", handler.extractPackageCode("LC15"));
        }

        @ParameterizedTest
        @DisplayName("LC series should extract correct voltage")
        @CsvSource({
            "LC03, 3.3",
            "LC05, 5",
            "LC12, 12",
            "LC15, 15"
        })
        void shouldExtractLCVoltage(String mpn, String expectedVoltage) {
            assertEquals(expectedVoltage, handler.extractVoltage(mpn),
                    mpn + " should have voltage " + expectedVoltage);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and voltage should be replacements")
        void shouldDetectSameSeriesReplacements() {
            assertTrue(handler.isOfficialReplacement("GBLC05C", "GBLC05C"),
                    "Identical parts should be replacements");
            assertTrue(handler.isOfficialReplacement("PSM712", "PSM712-LF"),
                    "Lead-free variant should be replacement");
        }

        @Test
        @DisplayName("Different voltage should not be replacements")
        void shouldNotReplaceDifferentVoltage() {
            assertFalse(handler.isOfficialReplacement("GBLC05C", "GBLC12C"),
                    "Different voltage should not be replacement");
            assertFalse(handler.isOfficialReplacement("TVS05500", "TVS15000"),
                    "Different voltage should not be replacement");
        }

        @Test
        @DisplayName("Different series should not be replacements")
        void shouldNotReplaceDifferentSeries() {
            assertFalse(handler.isOfficialReplacement("TVS05500", "SMD0505"),
                    "Different series should not be replacement");
            assertFalse(handler.isOfficialReplacement("GBLC05C", "ULC0512"),
                    "Different series should not be replacement");
        }

        @Test
        @DisplayName("Bidirectional and unidirectional should not be replacements")
        void shouldNotReplaceDifferentPolarity() {
            // TVS is unidirectional, GBLC is bidirectional
            assertFalse(handler.isOfficialReplacement("TVS05500", "GBLC05C"),
                    "Unidirectional should not replace bidirectional");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support DIODE type")
        void shouldSupportDiode() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.DIODE));
        }

        @Test
        @DisplayName("getSupportedTypes() should return immutable Set")
        void shouldReturnImmutableSet() {
            assertThrows(UnsupportedOperationException.class, () -> {
                handler.getSupportedTypes().add(ComponentType.IC);
            });
        }

        @Test
        @DisplayName("getSupportedTypes() should use Set.of()")
        void shouldUseSetOf() {
            // Set.of() returns an immutable set, verify size is correct
            assertEquals(1, handler.getSupportedTypes().size());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.DIODE, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractVoltage(null));
            assertFalse(handler.isBidirectional(null));
            assertFalse(handler.isLeadFree(null));
            assertEquals(0, handler.getPowerRating(null));
            assertEquals(1, handler.getLineCount(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.DIODE, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractVoltage(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("GBLC05C", null, registry));
        }

        @Test
        @DisplayName("Should not match unrelated part numbers")
        void shouldNotMatchUnrelatedParts() {
            assertFalse(handler.matches("LM7805", ComponentType.DIODE, registry));
            assertFalse(handler.matches("STM32F103", ComponentType.DIODE, registry));
            assertFalse(handler.matches("GRM155R71H104", ComponentType.DIODE, registry));
            assertFalse(handler.matches("SMAJ15A", ComponentType.DIODE, registry));  // Littelfuse
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("gblc05c", ComponentType.DIODE, registry));
            assertTrue(handler.matches("GBLC05C", ComponentType.DIODE, registry));
            assertTrue(handler.matches("Gblc05c", ComponentType.DIODE, registry));
            assertTrue(handler.matches("psm712", ComponentType.DIODE, registry));
            assertTrue(handler.matches("PSM712", ComponentType.DIODE, registry));
        }

        @Test
        @DisplayName("Should handle MPN with trailing characters")
        void shouldHandleTrailingCharacters() {
            assertTrue(handler.matches("GBLC05C-TR", ComponentType.DIODE, registry));
            assertTrue(handler.matches("PSM712-LF-TR", ComponentType.DIODE, registry));
            assertTrue(handler.matches("TVS05500A", ComponentType.DIODE, registry));
        }
    }

    @Nested
    @DisplayName("Pattern Matching Edge Cases")
    class PatternMatchingTests {

        @Test
        @DisplayName("Should not match SMD prefix for non-ProTek parts")
        void shouldNotMatchWrongSMDParts() {
            // SMD followed by non-digit should not match
            assertFalse(handler.matches("SMDJ15A", ComponentType.DIODE, registry));
            // SMD with only 3 digits should not match
            assertFalse(handler.matches("SMD051", ComponentType.DIODE, registry));
        }

        @Test
        @DisplayName("Should not match LC prefix for non-ProTek parts")
        void shouldNotMatchWrongLCParts() {
            // LC followed by non-digit should not match
            assertFalse(handler.matches("LCA123", ComponentType.DIODE, registry));
            // LC with only 1 digit should not match
            assertFalse(handler.matches("LC5", ComponentType.DIODE, registry));
        }

        @Test
        @DisplayName("Should not match SP prefix for non-ProTek parts")
        void shouldNotMatchWrongSPParts() {
            // SP followed by non-digit should not match
            assertFalse(handler.matches("SPICE", ComponentType.DIODE, registry));
            // SP with only 1 digit should not match
            assertFalse(handler.matches("SP5", ComponentType.DIODE, registry));
        }

        @Test
        @DisplayName("TVS series requires 5 digits after prefix")
        void shouldRequireFiveDigitsForTVS() {
            assertTrue(handler.matches("TVS03500", ComponentType.DIODE, registry));
            assertTrue(handler.matches("TVS15000", ComponentType.DIODE, registry));
            assertFalse(handler.matches("TVS035", ComponentType.DIODE, registry));
            assertFalse(handler.matches("TVS0350", ComponentType.DIODE, registry));
        }

        @Test
        @DisplayName("GBLC series requires 2 digits and C suffix")
        void shouldRequireTwoDigitsAndCForGBLC() {
            assertTrue(handler.matches("GBLC05C", ComponentType.DIODE, registry));
            assertTrue(handler.matches("GBLC15C", ComponentType.DIODE, registry));
            assertFalse(handler.matches("GBLC5C", ComponentType.DIODE, registry));  // Only 1 digit
            assertFalse(handler.matches("GBLC05", ComponentType.DIODE, registry));  // No C suffix
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {

        @Test
        @DisplayName("getManufacturerTypes() should return empty set")
        void shouldReturnEmptyManufacturerTypes() {
            assertTrue(handler.getManufacturerTypes().isEmpty());
        }
    }

    @Nested
    @DisplayName("Series-Specific Functionality")
    class SeriesSpecificTests {

        @Test
        @DisplayName("PSM series line count should be 2 (dual channel)")
        void psmShouldHaveDualChannel() {
            assertEquals(2, handler.getLineCount("PSM712"));
            assertEquals(2, handler.getLineCount("PSM05"));
        }

        @Test
        @DisplayName("GBLC series line count should be 1 (single channel)")
        void gblcShouldHaveSingleChannel() {
            assertEquals(1, handler.getLineCount("GBLC05C"));
            assertEquals(1, handler.getLineCount("GBLC15C"));
        }

        @Test
        @DisplayName("ULC series line count should vary by part number")
        void ulcShouldHaveVariableChannels() {
            assertEquals(12, handler.getLineCount("ULC0512"));
            assertEquals(24, handler.getLineCount("ULC0524"));
            assertEquals(8, handler.getLineCount("ULC0508"));
        }

        @Test
        @DisplayName("Single channel devices should return line count 1")
        void singleChannelShouldReturn1() {
            assertEquals(1, handler.getLineCount("TVS05500"));
            assertEquals(1, handler.getLineCount("SMD0512"));
        }
    }
}
