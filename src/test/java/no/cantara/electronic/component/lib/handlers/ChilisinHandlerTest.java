package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ChilisinHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ChilisinHandler.
 *
 * Tests pattern matching, package code extraction, series extraction,
 * inductance value extraction, and replacement detection for Chilisin
 * Electronics inductors.
 */
class ChilisinHandlerTest {

    private static ChilisinHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ChilisinHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SQC Chip Inductor Detection")
    class SQCTests {

        @ParameterizedTest
        @DisplayName("Should detect SQC chip inductors")
        @ValueSource(strings = {
                "SQC453226T-100M-N",
                "SQC322516T-1R0M-N",
                "SQC201612T-R10K-N",
                "SQC453226T-101M-N",
                "SQC252016T-2R2K-N"
        })
        void shouldDetectSQCInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect SQC inductors with IC type")
        @ValueSource(strings = {
                "SQC453226T-100M-N",
                "SQC322516T-1R0M-N"
        })
        void shouldDetectSQCAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract SQC series with size code")
        void shouldExtractSQCSeries() {
            assertEquals("SQC4532", handler.extractSeries("SQC453226T-100M-N"));
            assertEquals("SQC3225", handler.extractSeries("SQC322516T-1R0M-N"));
            assertEquals("SQC2016", handler.extractSeries("SQC201612T-R10K-N"));
        }

        @Test
        @DisplayName("Should extract SQC inductance values")
        void shouldExtractSQCInductance() {
            assertEquals("10.0uH", handler.extractInductanceValue("SQC453226T-100M-N"));
            assertEquals("1.0uH", handler.extractInductanceValue("SQC322516T-1R0M-N"));
            assertEquals("100.0uH", handler.extractInductanceValue("SQC453226T-101M-N"));
        }
    }

    @Nested
    @DisplayName("MHCI Power Inductor Detection")
    class MHCITests {

        @ParameterizedTest
        @DisplayName("Should detect MHCI power inductors")
        @ValueSource(strings = {
                "MHCI0504-1R0M-R8",
                "MHCI0403-2R2K-R8",
                "MHCI0302-100M-R8",
                "MHCI0504-4R7M-R8",
                "MHCI0605-101K-R8"
        })
        void shouldDetectMHCIInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract MHCI series with size code")
        void shouldExtractMHCISeries() {
            assertEquals("MHCI0504", handler.extractSeries("MHCI0504-1R0M-R8"));
            assertEquals("MHCI0403", handler.extractSeries("MHCI0403-2R2K-R8"));
            assertEquals("MHCI0302", handler.extractSeries("MHCI0302-100M-R8"));
        }

        @Test
        @DisplayName("Should extract MHCI inductance values")
        void shouldExtractMHCIInductance() {
            assertEquals("1.0uH", handler.extractInductanceValue("MHCI0504-1R0M-R8"));
            assertEquals("2.2uH", handler.extractInductanceValue("MHCI0403-2R2K-R8"));
            assertEquals("10.0uH", handler.extractInductanceValue("MHCI0302-100M-R8"));
            assertEquals("4.7uH", handler.extractInductanceValue("MHCI0504-4R7M-R8"));
            assertEquals("100.0uH", handler.extractInductanceValue("MHCI0605-101K-R8"));
        }

        @Test
        @DisplayName("Should extract MHCI package code")
        void shouldExtractMHCIPackageCode() {
            assertEquals("5.0x4.0mm", handler.extractPackageCode("MHCI0504-1R0M-R8"));
            assertEquals("4.0x3.0mm", handler.extractPackageCode("MHCI0403-2R2K-R8"));
            assertEquals("3.0x2.0mm", handler.extractPackageCode("MHCI0302-100M-R8"));
        }
    }

    @Nested
    @DisplayName("MHCC Coupled Inductor Detection")
    class MHCCTests {

        @ParameterizedTest
        @DisplayName("Should detect MHCC coupled inductors")
        @ValueSource(strings = {
                "MHCC0504-1R0M-R8",
                "MHCC0403-2R2K-R8",
                "MHCC0302-100M-R8"
        })
        void shouldDetectMHCCInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract MHCC series with size code")
        void shouldExtractMHCCSeries() {
            assertEquals("MHCC0504", handler.extractSeries("MHCC0504-1R0M-R8"));
            assertEquals("MHCC0403", handler.extractSeries("MHCC0403-2R2K-R8"));
        }
    }

    @Nested
    @DisplayName("CS Ferrite Chip Inductor Detection")
    class CSTests {

        @ParameterizedTest
        @DisplayName("Should detect CS ferrite chip inductors")
        @ValueSource(strings = {
                "CS0402-1R0M-N",
                "CS0603-2R2K-N",
                "CS0805-100M-N",
                "CS1005-4R7K-N"
        })
        void shouldDetectCSInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract CS series with size code")
        void shouldExtractCSSeries() {
            assertEquals("CS0402", handler.extractSeries("CS0402-1R0M-N"));
            assertEquals("CS0603", handler.extractSeries("CS0603-2R2K-N"));
            assertEquals("CS0805", handler.extractSeries("CS0805-100M-N"));
        }

        @Test
        @DisplayName("Should extract CS package code")
        void shouldExtractCSPackageCode() {
            assertEquals("0402", handler.extractPackageCode("CS0402-1R0M-N"));
            assertEquals("0603", handler.extractPackageCode("CS0603-2R2K-N"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package dimensions from size code")
        @CsvSource({
                "MHCI0504-1R0M-R8, 5.0x4.0mm",
                "MHCI0403-2R2K-R8, 4.0x3.0mm",
                "MHCI0302-100M-R8, 3.0x2.0mm",
                "SQC453226T-100M-N, 4.5x3.2mm",
                "SQC322516T-1R0M-N, 3.2x2.5mm",
                "CS0402-1R0M-N, 0402",
                "CS0603-2R2K-N, 0603"
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
        @DisplayName("Should extract series with size code")
        @CsvSource({
                "SQC453226T-100M-N, SQC4532",
                "SQC322516T-1R0M-N, SQC3225",
                "MHCI0504-1R0M-R8, MHCI0504",
                "MHCI0403-2R2K-R8, MHCI0403",
                "MHCC0504-1R0M-R8, MHCC0504",
                "CS0402-1R0M-N, CS0402",
                "CS0603-2R2K-N, CS0603"
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
        @DisplayName("Should extract inductance from 3-digit code")
        @CsvSource({
                "MHCI0504-100M-R8, 10.0uH",
                "MHCI0504-101M-R8, 100.0uH",
                "MHCI0504-102M-R8, 1.0mH",
                "SQC453226T-100M-N, 10.0uH",
                "SQC453226T-101M-N, 100.0uH",
                "SQC453226T-220M-N, 22.0uH",
                "SQC453226T-470M-N, 47.0uH"
        })
        void shouldExtractInductanceFrom3DigitCode(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inductance from R-notation")
        @CsvSource({
                "MHCI0504-1R0M-R8, 1.0uH",
                "MHCI0504-2R2K-R8, 2.2uH",
                "MHCI0504-4R7M-R8, 4.7uH",
                "MHCI0504-R10M-R8, 100nH",
                "MHCI0504-R22M-R8, 220nH",
                "MHCI0504-R47K-R8, 470nH",
                "SQC322516T-1R0M-N, 1.0uH",
                "SQC322516T-2R2K-N, 2.2uH"
        })
        void shouldExtractInductanceFromRNotation(String mpn, String expectedValue) {
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
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and value should be replacements")
        void sameSeriesAndValueShouldBeReplacements() {
            // Same inductor, different packaging
            assertTrue(handler.isOfficialReplacement("MHCI0504-1R0M-R8", "MHCI0504-1R0K-R8"),
                    "Same value with different tolerance should be replaceable");
        }

        @Test
        @DisplayName("Same series but different value should NOT be replacements")
        void differentValueShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("MHCI0504-1R0M-R8", "MHCI0504-2R2M-R8"),
                    "Different inductance values should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different series should NOT be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("MHCI0504-1R0M-R8", "MHCC0504-1R0M-R8"),
                    "Different series should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different size should NOT be replacements")
        void differentSizeShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("MHCI0504-1R0M-R8", "MHCI0403-1R0M-R8"),
                    "Different sizes should not be replaceable");
        }

        @Test
        @DisplayName("Different tolerance codes should be replacements if same series and value")
        void differentToleranceShouldBeReplacements() {
            // Same inductor, different tolerance (M=20% vs K=10%)
            assertTrue(handler.isOfficialReplacement("MHCI0504-1R0M-R8", "MHCI0504-1R0K-R8"),
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
            assertFalse(handler.isOfficialReplacement(null, "MHCI0504-1R0M-R8"));
            assertFalse(handler.isOfficialReplacement("MHCI0504-1R0M-R8", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.INDUCTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractInductanceValue(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("MHCI0504-1R0M-R8", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMPNs() {
            assertTrue(handler.matches("mhci0504-1r0m-r8", ComponentType.INDUCTOR, registry),
                    "Should handle lowercase MPN");
            assertTrue(handler.matches("sqc453226t-100m-n", ComponentType.INDUCTOR, registry),
                    "Should handle lowercase SQC MPN");
        }

        @Test
        @DisplayName("Should not match non-Chilisin parts")
        void shouldNotMatchNonChilisinParts() {
            assertFalse(handler.matches("GRM155R61A104KA01D", ComponentType.INDUCTOR, registry),
                    "Should not match Murata capacitor");
            assertFalse(handler.matches("XAL4020-222ME", ComponentType.INDUCTOR, registry),
                    "Should not match Coilcraft inductor");
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
            ChilisinHandler directHandler = new ChilisinHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("MHCI0504-1R0M-R8", ComponentType.INDUCTOR, directRegistry),
                    "Direct handler should match MHCI inductor");
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
    @DisplayName("MPN Structure Documentation")
    class MPNStructureTests {

        @Test
        @DisplayName("Document MHCI MPN structure")
        void documentMHCIMPNStructure() {
            // MPN: MHCI0504-1R0M-R8
            // Structure:
            // - MHCI = Series (power inductor)
            // - 0504 = Size code (5.0mm x 4.0mm)
            // - 1R0 = Inductance (1.0 uH, R indicates decimal point)
            // - M = Tolerance (M=+/-20%)
            // - R8 = Packaging code
            String mpn = "MHCI0504-1R0M-R8";
            assertEquals("MHCI0504", handler.extractSeries(mpn));
            assertEquals("5.0x4.0mm", handler.extractPackageCode(mpn));
            assertEquals("1.0uH", handler.extractInductanceValue(mpn));
        }

        @Test
        @DisplayName("Document SQC MPN structure")
        void documentSQCMPNStructure() {
            // MPN: SQC453226T-100M-N
            // Structure:
            // - SQC = Series (chip inductor)
            // - 4532 = Size code (4.5mm x 3.2mm)
            // - 26 = Height code
            // - T = Type/Shield code
            // - 100 = Inductance (10 x 10^0 uH = 10uH)
            // - M = Tolerance (M=+/-20%)
            // - N = Packaging code
            String mpn = "SQC453226T-100M-N";
            assertEquals("SQC4532", handler.extractSeries(mpn));
            assertEquals("4.5x3.2mm", handler.extractPackageCode(mpn));
            assertEquals("10.0uH", handler.extractInductanceValue(mpn));
        }

        @Test
        @DisplayName("Document CS MPN structure")
        void documentCSMPNStructure() {
            // MPN: CS0402-1R0M-N
            // Structure:
            // - CS = Series (ferrite chip inductor)
            // - 0402 = Size code (0402 package)
            // - 1R0 = Inductance (1.0 uH)
            // - M = Tolerance (M=+/-20%)
            // - N = Packaging code
            String mpn = "CS0402-1R0M-N";
            assertEquals("CS0402", handler.extractSeries(mpn));
            assertEquals("0402", handler.extractPackageCode(mpn));
            assertEquals("1.0uH", handler.extractInductanceValue(mpn));
        }
    }

    @Nested
    @DisplayName("Inductance Code Documentation")
    class InductanceCodeDocumentationTests {

        @Test
        @DisplayName("Document R-notation: 1R0 = 1.0uH")
        void documentRNotation1R0() {
            assertEquals("1.0uH", handler.extractInductanceValue("MHCI0504-1R0M-R8"));
        }

        @Test
        @DisplayName("Document R-notation: 2R2 = 2.2uH")
        void documentRNotation2R2() {
            assertEquals("2.2uH", handler.extractInductanceValue("MHCI0504-2R2K-R8"));
        }

        @Test
        @DisplayName("Document R-notation: 4R7 = 4.7uH")
        void documentRNotation4R7() {
            assertEquals("4.7uH", handler.extractInductanceValue("MHCI0504-4R7M-R8"));
        }

        @Test
        @DisplayName("Document R-notation at start: R10 = 0.10uH = 100nH")
        void documentRNotationR10() {
            assertEquals("100nH", handler.extractInductanceValue("MHCI0504-R10M-R8"));
        }

        @Test
        @DisplayName("Document R-notation at start: R22 = 0.22uH = 220nH")
        void documentRNotationR22() {
            assertEquals("220nH", handler.extractInductanceValue("MHCI0504-R22M-R8"));
        }

        @Test
        @DisplayName("Document 3-digit code: 100 = 10uH")
        void document3DigitCode100() {
            assertEquals("10.0uH", handler.extractInductanceValue("SQC453226T-100M-N"));
        }

        @Test
        @DisplayName("Document 3-digit code: 101 = 100uH")
        void document3DigitCode101() {
            assertEquals("100.0uH", handler.extractInductanceValue("SQC453226T-101M-N"));
        }

        @Test
        @DisplayName("Document 3-digit code: 102 = 1000uH = 1mH")
        void document3DigitCode102() {
            assertEquals("1.0mH", handler.extractInductanceValue("MHCI0504-102M-R8"));
        }

        @Test
        @DisplayName("Document 3-digit code: 220 = 22uH")
        void document3DigitCode220() {
            assertEquals("22.0uH", handler.extractInductanceValue("SQC453226T-220M-N"));
        }

        @Test
        @DisplayName("Document 3-digit code: 470 = 47uH")
        void document3DigitCode470() {
            assertEquals("47.0uH", handler.extractInductanceValue("SQC453226T-470M-N"));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Tests")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly parse real-world SQC inductors")
        @CsvSource({
                "SQC453226T-100M-N, SQC4532, 4.5x3.2mm, 10.0uH",
                "SQC322516T-1R0M-N, SQC3225, 3.2x2.5mm, 1.0uH",
                "SQC252016T-2R2K-N, SQC2520, 2.5x2.0mm, 2.2uH"
        })
        void shouldParseRealWorldSQCInductors(String mpn, String expectedSeries, String expectedPackage, String expectedValue) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
            assertEquals(expectedValue, handler.extractInductanceValue(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should correctly parse real-world MHCI inductors")
        @CsvSource({
                "MHCI0504-1R0M-R8, MHCI0504, 5.0x4.0mm, 1.0uH",
                "MHCI0403-2R2K-R8, MHCI0403, 4.0x3.0mm, 2.2uH",
                "MHCI0302-100M-R8, MHCI0302, 3.0x2.0mm, 10.0uH"
        })
        void shouldParseRealWorldMHCIInductors(String mpn, String expectedSeries, String expectedPackage, String expectedValue) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
            assertEquals(expectedValue, handler.extractInductanceValue(mpn));
        }
    }
}
