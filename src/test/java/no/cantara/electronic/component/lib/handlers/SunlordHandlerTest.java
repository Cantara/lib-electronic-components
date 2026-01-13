package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SunlordHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SunlordHandler.
 *
 * Tests pattern matching, package code extraction, series extraction,
 * inductance/impedance value extraction, and replacement detection for
 * Sunlord Electronics inductors and ferrite beads.
 */
class SunlordHandlerTest {

    private static SunlordHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SunlordHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SDCL Power Inductor Detection")
    class SDCLTests {

        @ParameterizedTest
        @DisplayName("Should detect SDCL power inductors")
        @ValueSource(strings = {
                "SDCL1005C1R0MTDF",
                "SDCL1608C2R2MT",
                "SDCL2012C100MT",
                "SDCL3216C101KT",
                "SDCL1005C4R7MT"
        })
        void shouldDetectSDCLInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should extract SDCL series with size code")
        @CsvSource({
                "SDCL1005C1R0MTDF, SDCL1005",
                "SDCL1608C2R2MT, SDCL1608",
                "SDCL2012C100MT, SDCL2012",
                "SDCL3216C101KT, SDCL3216"
        })
        void shouldExtractSDCLSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from SDCL")
        @CsvSource({
                "SDCL1005C1R0MTDF, 0402",
                "SDCL1608C2R2MT, 0603",
                "SDCL2012C100MT, 0805",
                "SDCL3216C101KT, 1206"
        })
        void shouldExtractSDCLPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("SWPA Power Inductor Detection")
    class SWPATests {

        @ParameterizedTest
        @DisplayName("Should detect SWPA shielded power inductors")
        @ValueSource(strings = {
                "SWPA4020S100MT",
                "SWPA5030S101MT",
                "SWPA6030S1R0MT",
                "SWPA4030S2R2KT",
                "SWPA3015S4R7MT"
        })
        void shouldDetectSWPAInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should extract SWPA series with size code")
        @CsvSource({
                "SWPA4020S100MT, SWPA4020",
                "SWPA5030S101MT, SWPA5030",
                "SWPA6030S1R0MT, SWPA6030",
                "SWPA3015S4R7MT, SWPA3015"
        })
        void shouldExtractSWPASeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from SWPA")
        @CsvSource({
                "SWPA4020S100MT, 4020",
                "SWPA5030S101MT, 5030",
                "SWPA6030S1R0MT, 6030",
                "SWPA3015S4R7MT, 3015"
        })
        void shouldExtractSWPAPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("SDFL Ferrite Chip Inductor Detection")
    class SDFLTests {

        @ParameterizedTest
        @DisplayName("Should detect SDFL ferrite chip inductors")
        @ValueSource(strings = {
                "SDFL2012T1R0M3B",
                "SDFL1608T2R2K2A",
                "SDFL3216T100M3B",
                "SDFL1005T4R7M2A"
        })
        void shouldDetectSDFLInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should extract SDFL series with size code")
        @CsvSource({
                "SDFL2012T1R0M3B, SDFL2012",
                "SDFL1608T2R2K2A, SDFL1608",
                "SDFL3216T100M3B, SDFL3216"
        })
        void shouldExtractSDFLSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from SDFL")
        @CsvSource({
                "SDFL2012T1R0M3B, 0805",
                "SDFL1608T2R2K2A, 0603",
                "SDFL3216T100M3B, 1206"
        })
        void shouldExtractSDFLPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("GZ Ferrite Bead Detection")
    class GZTests {

        @ParameterizedTest
        @DisplayName("Should detect GZ ferrite beads")
        @ValueSource(strings = {
                "GZ2012D601TF",
                "GZ1608D121TF",
                "GZ3216D102TF",
                "GZ1005D301TF",
                "GZ2012D471TF"
        })
        void shouldDetectGZFerriteBead(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @ParameterizedTest
        @DisplayName("Should extract GZ series with size code")
        @CsvSource({
                "GZ2012D601TF, GZ2012",
                "GZ1608D121TF, GZ1608",
                "GZ3216D102TF, GZ3216",
                "GZ1005D301TF, GZ1005"
        })
        void shouldExtractGZSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package code from GZ")
        @CsvSource({
                "GZ2012D601TF, 0805",
                "GZ1608D121TF, 0603",
                "GZ3216D102TF, 1206",
                "GZ1005D301TF, 0402"
        })
        void shouldExtractGZPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Inductance Value Extraction")
    class InductanceValueTests {

        @ParameterizedTest
        @DisplayName("Should extract inductance from R-notation (middle)")
        @CsvSource({
                "SDCL1005C1R0MTDF, 1.0uH",
                "SDCL1608C2R2MT, 2.2uH",
                "SWPA4020S4R7MT, 4.7uH",
                "SDFL2012T1R0M3B, 1.0uH"
        })
        void shouldExtractInductanceRNotationMiddle(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inductance from R-notation (start)")
        @CsvSource({
                "SDCL1005CR47MT, 470nH",
                "SWPA4020SR22MT, 220nH",
                "SDFL2012TR10M3B, 100nH"
        })
        void shouldExtractInductanceRNotationStart(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inductance from 3-digit code")
        @CsvSource({
                "SWPA4020S100MT, 10.0uH",
                "SWPA5030S101MT, 100.0uH",
                "SWPA6030S102MT, 1.0mH",
                "SDFL3216T100M3B, 10.0uH"
        })
        void shouldExtractInductanceThreeDigitCode(String mpn, String expectedValue) {
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
    @DisplayName("Impedance Value Extraction (Ferrite Beads)")
    class ImpedanceValueTests {

        @ParameterizedTest
        @DisplayName("Should extract impedance from GZ ferrite beads")
        @CsvSource({
                "GZ2012D601TF, 600 ohm",
                "GZ1608D121TF, 120 ohm",
                "GZ3216D102TF, 1000 ohm",
                "GZ1005D301TF, 300 ohm",
                "GZ2012D471TF, 470 ohm"
        })
        void shouldExtractImpedanceValue(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractImpedanceValue(mpn),
                    "Impedance value for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-ferrite bead MPNs")
        void shouldReturnEmptyForNonFerriteBead() {
            assertEquals("", handler.extractImpedanceValue("SDCL1005C1R0MTDF"),
                    "Should not extract impedance from inductor");
            assertEquals("", handler.extractImpedanceValue("SWPA4020S100MT"),
                    "Should not extract impedance from power inductor");
        }
    }

    @Nested
    @DisplayName("Series Type Identification")
    class SeriesTypeTests {

        @ParameterizedTest
        @DisplayName("Should identify series type")
        @CsvSource({
                "SDCL1005C1R0MTDF, Power Inductor",
                "SWPA4020S100MT, Power Inductor (Shielded)",
                "SDFL2012T1R0M3B, Ferrite Chip Inductor",
                "GZ2012D601TF, Ferrite Bead"
        })
        void shouldIdentifySeriesType(String mpn, String expectedType) {
            assertEquals(expectedType, handler.getSeriesType(mpn),
                    "Series type for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.getSeriesType("UNKNOWN1234"));
            assertEquals("", handler.getSeriesType(null));
            assertEquals("", handler.getSeriesType(""));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series and inductance should be replacements")
        void sameSeriesAndValueShouldBeReplacements() {
            // Same inductor, different packaging options
            assertTrue(handler.isOfficialReplacement("SDCL1005C1R0MTDF", "SDCL1005C1R0KTDF"),
                    "Same value with different tolerance should be replaceable");
            assertTrue(handler.isOfficialReplacement("SWPA4020S100MT", "SWPA4020S100KT"),
                    "Same value with different tolerance should be replaceable");
        }

        @Test
        @DisplayName("Same series but different inductance should NOT be replacements")
        void differentValueShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("SDCL1005C1R0MTDF", "SDCL1005C2R2MTDF"),
                    "Different inductance values should not be replaceable");
            assertFalse(handler.isOfficialReplacement("SWPA4020S100MT", "SWPA4020S101MT"),
                    "Different inductance values should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different series should NOT be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("SDCL1005C1R0MTDF", "SDFL1005C1R0MTDF"),
                    "Different series should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different size should NOT be replacements")
        void differentSizeShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("SDCL1005C1R0MTDF", "SDCL2012C1R0MTDF"),
                    "Different sizes should not be replaceable");
        }

        @Test
        @DisplayName("Same ferrite bead impedance should be replacements")
        void sameImpedanceShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("GZ2012D601TF", "GZ2012D601KF"),
                    "Same impedance with different options should be replaceable");
        }

        @Test
        @DisplayName("Different ferrite bead impedance should NOT be replacements")
        void differentImpedanceShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("GZ2012D601TF", "GZ2012D102TF"),
                    "Different impedance values should not be replaceable");
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
            assertEquals("", handler.extractImpedanceValue(null));
            assertFalse(handler.isOfficialReplacement(null, "SDCL1005C1R0MTDF"));
            assertFalse(handler.isOfficialReplacement("SDCL1005C1R0MTDF", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.INDUCTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractInductanceValue(""));
            assertEquals("", handler.extractImpedanceValue(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("SDCL1005C1R0MTDF", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMPNs() {
            assertTrue(handler.matches("sdcl1005c1r0mtdf", ComponentType.INDUCTOR, registry),
                    "Should handle lowercase MPN");
            assertTrue(handler.matches("gz2012d601tf", ComponentType.INDUCTOR, registry),
                    "Should handle lowercase ferrite bead MPN");
        }

        @Test
        @DisplayName("Should not match non-Sunlord parts")
        void shouldNotMatchNonSunlordParts() {
            assertFalse(handler.matches("GRM155R61A104KA01D", ComponentType.INDUCTOR, registry),
                    "Should not match Murata capacitor");
            assertFalse(handler.matches("XAL4020-222ME", ComponentType.INDUCTOR, registry),
                    "Should not match Coilcraft inductor");
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
            SunlordHandler directHandler = new SunlordHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("SDCL1005C1R0MTDF", ComponentType.INDUCTOR, directRegistry),
                    "Direct handler should match SDCL inductor");
            assertTrue(directHandler.matches("GZ2012D601TF", ComponentType.INDUCTOR, directRegistry),
                    "Direct handler should match GZ ferrite bead");
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
    @DisplayName("Package Size Mapping")
    class PackageSizeTests {

        @ParameterizedTest
        @DisplayName("Should map metric sizes to imperial codes")
        @CsvSource({
                "SDCL1005C1R0MTDF, 0402",
                "SDCL1608C2R2MT, 0603",
                "SDCL2012C100MT, 0805",
                "SDCL3216C101KT, 1206",
                "SDCL3225C100MT, 1210",
                "SDCL4532C100MT, 1812",
                "SDCL5025C100MT, 2010",
                "SDCL6332C100MT, 2512"
        })
        void shouldMapMetricToImperial(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code mapping for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should return metric size for power inductor sizes")
        @CsvSource({
                "SWPA2520S100MT, 2520",
                "SWPA3015S100MT, 3015",
                "SWPA4020S100MT, 4020",
                "SWPA4030S100MT, 4030",
                "SWPA5020S100MT, 5020",
                "SWPA5030S100MT, 5030",
                "SWPA6020S100MT, 6020",
                "SWPA6030S100MT, 6030"
        })
        void shouldReturnMetricForPowerInductors(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for power inductor " + mpn);
        }
    }

    @Nested
    @DisplayName("MPN Structure Documentation")
    class MPNStructureTests {

        @Test
        @DisplayName("Document SDCL MPN structure")
        void documentSDCLMPNStructure() {
            // MPN: SDCL1005C1R0MTDF
            // Structure:
            // - SDCL = Series (power inductor)
            // - 1005 = Package size (1.0mm x 0.5mm = 0402 imperial)
            // - C = Type/characteristics code
            // - 1R0 = Inductance (1.0uH - R indicates decimal point)
            // - M = Tolerance (M=+/-20%)
            // - T = Taping specification
            // - DF = Additional options
            String mpn = "SDCL1005C1R0MTDF";
            assertEquals("SDCL1005", handler.extractSeries(mpn));
            assertEquals("0402", handler.extractPackageCode(mpn));
            assertEquals("1.0uH", handler.extractInductanceValue(mpn));
            assertEquals("Power Inductor", handler.getSeriesType(mpn));
        }

        @Test
        @DisplayName("Document SWPA MPN structure")
        void documentSWPAMPNStructure() {
            // MPN: SWPA4020S100MT
            // Structure:
            // - SWPA = Series (shielded power inductor)
            // - 4020 = Package size (4.0mm x 2.0mm)
            // - S = Shielded
            // - 100 = Inductance (10uH)
            // - M = Tolerance
            // - T = Taping
            String mpn = "SWPA4020S100MT";
            assertEquals("SWPA4020", handler.extractSeries(mpn));
            assertEquals("4020", handler.extractPackageCode(mpn));
            assertEquals("10.0uH", handler.extractInductanceValue(mpn));
            assertEquals("Power Inductor (Shielded)", handler.getSeriesType(mpn));
        }

        @Test
        @DisplayName("Document GZ MPN structure")
        void documentGZMPNStructure() {
            // MPN: GZ2012D601TF
            // Structure:
            // - GZ = Series (ferrite bead)
            // - 2012 = Package size (2.0mm x 1.2mm = 0805 imperial)
            // - D = Type code
            // - 601 = Impedance at 100MHz (600 ohm)
            // - T = Tolerance
            // - F = Packaging
            String mpn = "GZ2012D601TF";
            assertEquals("GZ2012", handler.extractSeries(mpn));
            assertEquals("0805", handler.extractPackageCode(mpn));
            assertEquals("600 ohm", handler.extractImpedanceValue(mpn));
            assertEquals("Ferrite Bead", handler.getSeriesType(mpn));
        }
    }

    @Nested
    @DisplayName("Cross-Handler Pattern Matching Prevention")
    class CrossHandlerTests {

        @Test
        @DisplayName("Should not match other manufacturer patterns")
        void shouldNotMatchOtherManufacturers() {
            // Coilcraft patterns
            assertFalse(handler.matches("XAL4020-222ME", ComponentType.INDUCTOR, registry),
                    "Should not match Coilcraft XAL");
            assertFalse(handler.matches("XFL3012-222ME", ComponentType.INDUCTOR, registry),
                    "Should not match Coilcraft XFL");

            // TDK patterns
            assertFalse(handler.matches("MLZ2012M100WT", ComponentType.INDUCTOR, registry),
                    "Should not match TDK MLZ");

            // Murata patterns
            assertFalse(handler.matches("LQM2MPN1R0MC0L", ComponentType.INDUCTOR, registry),
                    "Should not match Murata LQM");

            // Bourns patterns
            assertFalse(handler.matches("SRN4018-100M", ComponentType.INDUCTOR, registry),
                    "Should not match Bourns SRN");
        }
    }
}
