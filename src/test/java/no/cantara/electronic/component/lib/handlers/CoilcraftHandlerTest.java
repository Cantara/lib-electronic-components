package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.CoilcraftHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CoilcraftHandler.
 *
 * Tests pattern matching, package code extraction, series extraction,
 * inductance value extraction, and replacement detection for Coilcraft
 * power inductors.
 */
class CoilcraftHandlerTest {

    private static CoilcraftHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new CoilcraftHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("XAL/XEL Power Inductor Detection")
    class XALXELTests {

        @ParameterizedTest
        @DisplayName("Should detect XAL high current power inductors")
        @ValueSource(strings = {
                "XAL4020-222ME",
                "XAL5030-102ME",
                "XAL6030-472ME",
                "XAL7070-103ME",
                "XAL4030-331ME"
        })
        void shouldDetectXALInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @ParameterizedTest
        @DisplayName("Should detect XEL low DCR power inductors")
        @ValueSource(strings = {
                "XEL4030-102ME",
                "XEL5020-222ME",
                "XEL6060-472ME",
                "XEL4020-331ME"
        })
        void shouldDetectXELInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @ParameterizedTest
        @DisplayName("Should detect XAT automotive power inductors")
        @ValueSource(strings = {
                "XAT4030-222ME",
                "XAT5020-102ME"
        })
        void shouldDetectXATInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }
    }

    @Nested
    @DisplayName("XFL Low Profile Inductor Detection")
    class XFLTests {

        @ParameterizedTest
        @DisplayName("Should detect XFL low profile power inductors")
        @ValueSource(strings = {
                "XFL2006-102ME",
                "XFL3012-222ME",
                "XFL4020-472MEB",
                "XFL5015-103ME",
                "XFL6060-332ME"
        })
        void shouldDetectXFLInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @Test
        @DisplayName("Should extract XFL series with size code")
        void shouldExtractXFLSeries() {
            assertEquals("XFL4020", handler.extractSeries("XFL4020-472MEB"));
            assertEquals("XFL3012", handler.extractSeries("XFL3012-222ME"));
        }
    }

    @Nested
    @DisplayName("SER High Efficiency Inductor Detection")
    class SERTests {

        @ParameterizedTest
        @DisplayName("Should detect SER high efficiency power inductors")
        @ValueSource(strings = {
                "SER1360-202ML",
                "SER2010-102ME",
                "SER2915-222ME",
                "SER1412-102ME"
        })
        void shouldDetectSERInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @Test
        @DisplayName("Should extract SER series with size code")
        void shouldExtractSERSeries() {
            assertEquals("SER2010", handler.extractSeries("SER2010-202ML"));
            assertEquals("SER1360", handler.extractSeries("SER1360-102ME"));
        }
    }

    @Nested
    @DisplayName("LPS Low Profile Shielded Detection")
    class LPSTests {

        @ParameterizedTest
        @DisplayName("Should detect LPS low profile shielded inductors")
        @ValueSource(strings = {
                "LPS3015-223MRB",
                "LPS4018-102MEB",
                "LPS4020-222MRB",
                "LPS6225-472MEB"
        })
        void shouldDetectLPSInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @Test
        @DisplayName("Should extract LPS series with size code")
        void shouldExtractLPSSeries() {
            assertEquals("LPS4018", handler.extractSeries("LPS4018-223MRB"));
            assertEquals("LPS3015", handler.extractSeries("LPS3015-102MEB"));
        }
    }

    @Nested
    @DisplayName("MSS Magnetically Shielded Detection")
    class MSSTests {

        @ParameterizedTest
        @DisplayName("Should detect MSS magnetically shielded inductors")
        @ValueSource(strings = {
                "MSS1038-102MEB",
                "MSS1048-222MEB",
                "MSS1038-472MEB",
                "MSS1260-103MEB"
        })
        void shouldDetectMSSInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @Test
        @DisplayName("Should extract MSS series with size code")
        void shouldExtractMSSSeries() {
            assertEquals("MSS1038", handler.extractSeries("MSS1038-102MEB"));
            assertEquals("MSS1048", handler.extractSeries("MSS1048-222MEB"));
        }
    }

    @Nested
    @DisplayName("DO Drum Core Detection")
    class DOTests {

        @ParameterizedTest
        @DisplayName("Should detect DO drum core inductors")
        @ValueSource(strings = {
                "DO1608C-222ME",
                "DO3316P-102ME",
                "DO3340P-472ME",
                "DO5022P-103ME"
        })
        void shouldDetectDOInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @Test
        @DisplayName("Should extract DO series with type suffix")
        void shouldExtractDOSeries() {
            assertEquals("DO3316P", handler.extractSeries("DO3316P-102ME"));
            assertEquals("DO1608C", handler.extractSeries("DO1608C-222ME"));
        }
    }

    @Nested
    @DisplayName("MSD Mid-Size Drum Detection")
    class MSDTests {

        @ParameterizedTest
        @DisplayName("Should detect MSD mid-size drum inductors")
        @ValueSource(strings = {
                "MSD1260-102ME",
                "MSD1278-222ME",
                "MSD1583-472ME"
        })
        void shouldDetectMSDInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @Test
        @DisplayName("Should extract MSD series with size code")
        void shouldExtractMSDSeries() {
            assertEquals("MSD1260", handler.extractSeries("MSD1260-102ME"));
        }
    }

    @Nested
    @DisplayName("SLC/SLR High Q Chip Inductor Detection")
    class SLCTests {

        @ParameterizedTest
        @DisplayName("Should detect SLC high Q chip inductors")
        @ValueSource(strings = {
                "SLC0806-102ME",
                "SLC1008-222ME",
                "SLC1609-472ME"
        })
        void shouldDetectSLCInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @ParameterizedTest
        @DisplayName("Should detect SLR high Q RF chip inductors")
        @ValueSource(strings = {
                "SLR0806-102ME",
                "SLR1008-222ME"
        })
        void shouldDetectSLRInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
        }

        @Test
        @DisplayName("Should extract SLC/SLR series with size code")
        void shouldExtractSLCSeries() {
            assertEquals("SLC0806", handler.extractSeries("SLC0806-102ME"));
            assertEquals("SLR1008", handler.extractSeries("SLR1008-222ME"));
        }
    }

    @Nested
    @DisplayName("HP High Performance Chip Inductor Detection")
    class HPTests {

        @ParameterizedTest
        @DisplayName("Should detect 0402HP/0603HP high performance inductors")
        @ValueSource(strings = {
                "0402HP-102ME",
                "0402HP-222ME",
                "0603HP-472ME",
                "0603HP-103ME"
        })
        void shouldDetectHPInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_COILCRAFT, registry),
                    mpn + " should match INDUCTOR_CHIP_COILCRAFT");
        }

        @Test
        @DisplayName("Should extract HP series")
        void shouldExtractHPSeries() {
            assertEquals("0402HP", handler.extractSeries("0402HP-102ME"));
            assertEquals("0603HP", handler.extractSeries("0603HP-222ME"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package type from series prefix")
        @CsvSource({
                "XAL4020-222ME, Shielded Power",
                "XEL4030-102ME, Shielded Low DCR",
                "XFL3012-472ME, Low Profile",
                "SER2010-202ML, High Efficiency",
                "LPS4018-223MRB, Low Profile Shielded",
                "MSS1048-222MEB, Magnetically Shielded",
                "DO3316P-102ME, Drum Core",
                "MSD1260-102ME, Mid-Size Drum",
                "SLC0806-102ME, High Q Chip",
                "SLR1008-222ME, High Q Chip RF",
                "0402HP-102ME, 0402 High Performance",
                "0603HP-222ME, 0603 High Performance"
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
                "XAL4020-222ME, XAL4020",
                "XEL5030-102ME, XEL5030",
                "XFL4020-472MEB, XFL4020",
                "SER2010-202ML, SER2010",
                "LPS4018-223MRB, LPS4018",
                "MSS1048-222MEB, MSS1048",
                "MSD1260-102ME, MSD1260",
                "SLC0806-102ME, SLC0806"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract DO series with type suffix")
        @CsvSource({
                "DO3316P-102ME, DO3316P",
                "DO1608C-222ME, DO1608C",
                "DO5022P-103ME, DO5022P"
        })
        void shouldExtractDOSeriesWithSuffix(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract HP series")
        @CsvSource({
                "0402HP-102ME, 0402HP",
                "0603HP-222ME, 0603HP"
        })
        void shouldExtractHPSeries(String mpn, String expectedSeries) {
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
                "XAL4020-222ME, 2.2uH",
                "XAL4020-472ME, 4.7uH",
                "XAL4020-102ME, 1.0uH",
                "XAL4020-103ME, 10.0uH",
                "XAL4020-104ME, 100.0uH",
                "XAL4020-331ME, 330nH",
                "XAL4020-100ME, 10nH"
        })
        void shouldExtractInductanceValue(String mpn, String expectedValue) {
            assertEquals(expectedValue, handler.extractInductanceValue(mpn),
                    "Inductance value for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inductance from R-notation")
        @CsvSource({
                "XAL4020-R47ME, 470nH",
                "XAL4020-R22ME, 220nH",
                "XAL4020-R10ME, 100nH"
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
            // Same inductor, different packaging (E vs B)
            assertTrue(handler.isOfficialReplacement("XAL4020-222ME", "XAL4020-222MB"),
                    "Same value with different packaging should be replaceable");
        }

        @Test
        @DisplayName("Same series but different value should NOT be replacements")
        void differentValueShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("XAL4020-222ME", "XAL4020-472ME"),
                    "Different inductance values should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different series should NOT be replacements")
        void differentSeriesShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("XAL4020-222ME", "XFL4020-222ME"),
                    "Different series should not be replaceable");
        }

        @Test
        @DisplayName("Same value but different size should NOT be replacements")
        void differentSizeShouldNotBeReplacements() {
            assertFalse(handler.isOfficialReplacement("XAL4020-222ME", "XAL5030-222ME"),
                    "Different sizes should not be replaceable");
        }

        @Test
        @DisplayName("Different tolerance codes should be replacements if same series and value")
        void differentToleranceShouldBeReplacements() {
            // Same inductor, different tolerance (M=20% vs L=15%)
            assertTrue(handler.isOfficialReplacement("XAL4020-222ME", "XAL4020-222LE"),
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
            assertFalse(handler.isOfficialReplacement(null, "XAL4020-222ME"));
            assertFalse(handler.isOfficialReplacement("XAL4020-222ME", null));
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
            assertFalse(handler.matches("XAL4020-222ME", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPNs")
        void shouldHandleLowercaseMPNs() {
            assertTrue(handler.matches("xal4020-222me", ComponentType.INDUCTOR, registry),
                    "Should handle lowercase MPN");
        }

        @Test
        @DisplayName("Should not match non-Coilcraft parts")
        void shouldNotMatchNonCoilcraftParts() {
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
            assertTrue(types.contains(ComponentType.INDUCTOR_CHIP_COILCRAFT),
                    "Should support INDUCTOR_CHIP_COILCRAFT");

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
            CoilcraftHandler directHandler = new CoilcraftHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("XAL4020-222ME", ComponentType.INDUCTOR, directRegistry),
                    "Direct handler should match XAL inductor");
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
        @DisplayName("Document XAL MPN structure")
        void documentXALMPNStructure() {
            // MPN: XAL4020-222ME
            // Structure:
            // - XAL = Series (shielded power inductor)
            // - 4020 = Size code (4.0mm x 2.0mm)
            // - 222 = Inductance (22 x 10^2 nH = 2.2uH)
            // - M = Tolerance (M=+/-20%)
            // - E = Packaging (E=embossed tape)
            String mpn = "XAL4020-222ME";
            System.out.println("MPN: " + mpn);
            System.out.println("Series: " + handler.extractSeries(mpn));
            System.out.println("Package: " + handler.extractPackageCode(mpn));
            System.out.println("Inductance: " + handler.extractInductanceValue(mpn));
        }

        @Test
        @DisplayName("Document LPS MPN structure")
        void documentLPSMPNStructure() {
            // MPN: LPS4018-223MRB
            // Structure:
            // - LPS = Series (low profile shielded)
            // - 4018 = Size code (4.0mm x 1.8mm)
            // - 223 = Inductance (22 x 10^3 nH = 22uH)
            // - M = Tolerance (M=+/-20%)
            // - R = Resistance option
            // - B = Packaging (B=bulk)
            String mpn = "LPS4018-223MRB";
            System.out.println("MPN: " + mpn);
            System.out.println("Series: " + handler.extractSeries(mpn));
            System.out.println("Package: " + handler.extractPackageCode(mpn));
            System.out.println("Inductance: " + handler.extractInductanceValue(mpn));
        }
    }
}
