package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MurataHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for MurataHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class MurataHandlerTest {

    private static MurataHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation - avoids MPNUtils.getManufacturerHandler issues
        handler = new MurataHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MLCC Capacitor Detection")
    class MLCCCapacitorTests {

        @ParameterizedTest
        @DisplayName("Should detect GRM series capacitors")
        @ValueSource(strings = {"GRM188R71H104KA93D", "GRM155R71C104KA88D", "GRM21BR71A105KA73L"})
        void shouldDetectGRMCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_CERAMIC_MURATA, registry),
                    mpn + " should match CAPACITOR_CERAMIC_MURATA");
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    mpn + " should match CAPACITOR (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect GCM series automotive capacitors")
        @ValueSource(strings = {"GCM188R71H104KA93D", "GCM155R71C104KA88D"})
        void shouldDetectGCMCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_CERAMIC_MURATA, registry),
                    mpn + " should match CAPACITOR_CERAMIC_MURATA");
        }

        @ParameterizedTest
        @DisplayName("Should detect KC series high voltage capacitors")
        @ValueSource(strings = {"KCA55R71H104KA88D", "KCM55R71H104KA88D"})
        void shouldDetectKCCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_CERAMIC_MURATA, registry),
                    mpn + " should match CAPACITOR_CERAMIC_MURATA");
        }
    }

    @Nested
    @DisplayName("Inductor Detection")
    class InductorTests {

        @ParameterizedTest
        @DisplayName("Should detect LQM power inductors")
        @ValueSource(strings = {"LQM2MPN2R2MG0L", "LQM18PN2R2MC0D"})
        void shouldDetectLQMInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_MURATA, registry),
                    mpn + " should match INDUCTOR_CHIP_MURATA");
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR, registry),
                    mpn + " should match INDUCTOR (base type)");
        }

        @ParameterizedTest
        @DisplayName("Should detect LQW wire wound inductors")
        @ValueSource(strings = {"LQW18AN2N2D00D", "LQW15AN2N2B00D"})
        void shouldDetectLQWInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_MURATA, registry),
                    mpn + " should match INDUCTOR_CHIP_MURATA");
        }

        @ParameterizedTest
        @DisplayName("Should detect LQG RF inductors")
        @ValueSource(strings = {"LQG15HS2N2S02D", "LQG18HN2N2S00D"})
        void shouldDetectLQGInductors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.INDUCTOR_CHIP_MURATA, registry),
                    mpn + " should match INDUCTOR_CHIP_MURATA");
        }

        @ParameterizedTest
        @DisplayName("Document DFE power inductor detection")
        @ValueSource(strings = {"DFE201612P1R0M", "DFE252012P2R2M"})
        void documentDFEDetection(String mpn) {
            // Document actual behavior - DFE patterns may vary
            boolean matches = handler.matches(mpn, ComponentType.INDUCTOR_POWER_MURATA, registry);
            System.out.println("DFE detection: " + mpn + " matches INDUCTOR_POWER_MURATA = " + matches);
        }
    }

    @Nested
    @DisplayName("EMI Component Detection")
    class EMIComponentTests {

        @ParameterizedTest
        @DisplayName("Document BLM ferrite bead detection")
        @ValueSource(strings = {"BLM15AG121SN1D", "BLM18PG121SN1D"})
        void documentBLMDetection(String mpn) {
            // Document actual behavior
            boolean matches = handler.matches(mpn, ComponentType.INDUCTOR_CHIP_MURATA, registry);
            System.out.println("BLM detection: " + mpn + " matches INDUCTOR_CHIP_MURATA = " + matches);
        }

        @ParameterizedTest
        @DisplayName("Document NFM filter detection")
        @ValueSource(strings = {"NFM41PC104R1E3L", "NFM18PC104R1E3D"})
        void documentNFMDetection(String mpn) {
            // Document actual behavior
            boolean matches = handler.matches(mpn, ComponentType.EMI_FILTER_MURATA, registry);
            System.out.println("NFM detection: " + mpn + " matches EMI_FILTER_MURATA = " + matches);
        }

        @ParameterizedTest
        @DisplayName("Document DLW common mode choke detection")
        @ValueSource(strings = {"DLW5BTM102SQ2L", "DLW21SN102SQ2L"})
        void documentDLWDetection(String mpn) {
            // Document actual behavior
            boolean matches = handler.matches(mpn, ComponentType.COMMON_MODE_CHOKE_MURATA, registry);
            System.out.println("DLW detection: " + mpn + " matches COMMON_MODE_CHOKE_MURATA = " + matches);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract GRM package codes")
        @CsvSource({
                "GRM188R71H104KA93D, 188",
                "GRM155R71C104KA88D, 155",
                "GRM21BR71A105KA73L, 21B"
        })
        void shouldExtractGRMPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract GCM package codes")
        @CsvSource({
                "GCM188R71H104KA93D, 188",
                "GCM155R71C104KA88D, 155"
        })
        void shouldExtractGCMPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract LQM package codes")
        @CsvSource({
                "LQM2MPN2R2MG0L, 2MPN",
                "LQM18PN2R2MC0D, 18PN"
        })
        void shouldExtractLQMPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract GRM series")
        @CsvSource({
                "GRM188R71H104KA93D, GRM188",
                "GRM155R71C104KA88D, GRM155"
        })
        void shouldExtractGRMSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract LQM series")
        @CsvSource({
                "LQM2MPN2R2MG0L, LQM2MPN",
                "LQM18PN2R2MC0D, LQM18PN"
        })
        void shouldExtractLQMSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same GRM series should be replacements")
        void sameGRMSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("GRM188R71H104KA93D", "GRM188R71H104KA93J"),
                    "Same GRM series should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("GRM188R71H104KA93D", "GRM155R71C104KA88D"),
                    "Different size codes should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.CAPACITOR),
                    "Should support CAPACITOR");
            assertTrue(types.contains(ComponentType.CAPACITOR_CERAMIC_MURATA),
                    "Should support CAPACITOR_CERAMIC_MURATA");
            assertTrue(types.contains(ComponentType.INDUCTOR),
                    "Should support INDUCTOR");
            assertTrue(types.contains(ComponentType.INDUCTOR_CHIP_MURATA),
                    "Should support INDUCTOR_CHIP_MURATA");
            assertTrue(types.contains(ComponentType.INDUCTOR_POWER_MURATA),
                    "Should support INDUCTOR_POWER_MURATA");
            assertTrue(types.contains(ComponentType.EMI_FILTER_MURATA),
                    "Should support EMI_FILTER_MURATA");
            assertTrue(types.contains(ComponentType.COMMON_MODE_CHOKE_MURATA),
                    "Should support COMMON_MODE_CHOKE_MURATA");
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
            assertFalse(handler.matches(null, ComponentType.CAPACITOR_CERAMIC_MURATA, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "GRM188R71H104KA93D"));
            assertFalse(handler.isOfficialReplacement("GRM188R71H104KA93D", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR_CERAMIC_MURATA, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("GRM188R71H104KA93D", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("grm188r71h104ka93d", ComponentType.CAPACITOR_CERAMIC_MURATA, registry),
                    "lowercase grm should match");
            assertTrue(handler.matches("GRM188R71H104KA93D", ComponentType.CAPACITOR_CERAMIC_MURATA, registry),
                    "uppercase GRM should match");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            MurataHandler directHandler = new MurataHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify patterns work
            assertTrue(directHandler.matches("GRM188R71H104KA93D", ComponentType.CAPACITOR_CERAMIC_MURATA, directRegistry));
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
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @Test
        @DisplayName("Common MLCC capacitors from datasheets")
        void commonMLCCCapacitors() {
            // Common 0.1uF decoupling caps
            assertTrue(handler.matches("GRM188R71H104KA93D", ComponentType.CAPACITOR_CERAMIC_MURATA, registry),
                    "Common 100nF 0603 cap");
            assertTrue(handler.matches("GRM155R71C104KA88D", ComponentType.CAPACITOR_CERAMIC_MURATA, registry),
                    "Common 100nF 0402 cap");
        }

        @Test
        @DisplayName("Document detection of various component types")
        void documentVariousComponents() {
            String[] mpns = {"GRM188R71H104KA93D", "LQM2MPN2R2MG0L", "BLM15AG121SN1D", "NFM41PC104R1E3L"};
            ComponentType[] types = {ComponentType.CAPACITOR, ComponentType.INDUCTOR, ComponentType.INDUCTOR, ComponentType.EMI_FILTER_MURATA};

            for (int i = 0; i < mpns.length; i++) {
                boolean matches = handler.matches(mpns[i], types[i], registry);
                System.out.println("Real-world: " + mpns[i] + " matches " + types[i] + " = " + matches);
            }
        }
    }
}
