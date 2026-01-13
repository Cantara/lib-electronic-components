package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.WIMAHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for WIMAHandler.
 * Tests WIMA premium film capacitors including MKS, MKP, FKS, and FKP series.
 *
 * WIMA Series:
 * - MKS: Metallized Polyester (PET) - general purpose, compact
 * - MKP: Metallized Polypropylene (PP) - audio, snubber, pulse
 * - FKS: Foil Polyester (PET) - precision, low loss
 * - FKP: Foil Polypropylene (PP) - highest quality, audio
 * - FKC: Foil Polycarbonate (legacy)
 */
class WIMAHandlerTest {

    private static WIMAHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new WIMAHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MKS Series - Metallized Polyester")
    class MKSSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "MKS2-100/63/10",      // 100nF, 63V, 10%, 5mm pitch
            "MKS4-100/63/10",      // 100nF, 63V, 10%, 7.5mm pitch
            "MKS4-220/63/5",       // 220nF, 63V, 5%
            "MKS4-470n/63/10",     // 470nF, 63V, 10%
            "MKS4-1u/63/10",       // 1uF, 63V, 10%
            "MKS4C024703C00KSSD"   // Alternative format
        })
        void shouldDetectMKSCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    mpn + " should match CAPACITOR");
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_FILM_WIMA, registry),
                    mpn + " should match CAPACITOR_FILM_WIMA");
        }

        @Test
        void shouldExtractMKSSeries() {
            assertEquals("MKS2", handler.extractSeries("MKS2-100/63/10"));
            assertEquals("MKS4", handler.extractSeries("MKS4-100/63/10"));
            assertEquals("MKS4", handler.extractSeries("MKS4C024703C00KSSD"));
        }

        @Test
        void shouldExtractMKSPackageCode() {
            assertEquals("5mm", handler.extractPackageCode("MKS2-100/63/10"));
            assertEquals("7.5mm", handler.extractPackageCode("MKS4-100/63/10"));
        }

        @Test
        void shouldGetMKSDielectric() {
            assertEquals("PET", handler.getDielectricType("MKS4-100/63/10"));
        }

        @Test
        void shouldGetMKSConstructionType() {
            assertEquals("Metallized", handler.getConstructionType("MKS4-100/63/10"));
        }
    }

    @Nested
    @DisplayName("MKP Series - Metallized Polypropylene")
    class MKPSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "MKP10-100/1000/10",   // 100nF, 1000V, 10%, pulse cap
            "MKP10-0.01/1000/10",  // 10nF, 1000V, 10%
            "MKP4-1000/630/5",     // 1uF, 630V, 5%
            "MKP10-0.047/630",     // 47nF, 630V
            "MKP4-680n/400/10"     // 680nF, 400V, 10%
        })
        void shouldDetectMKPCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    mpn + " should match CAPACITOR");
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_FILM_WIMA, registry),
                    mpn + " should match CAPACITOR_FILM_WIMA");
        }

        @Test
        void shouldExtractMKPSeries() {
            assertEquals("MKP10", handler.extractSeries("MKP10-100/1000/10"));
            assertEquals("MKP4", handler.extractSeries("MKP4-1000/630/5"));
        }

        @Test
        void shouldExtractMKPPackageCode() {
            assertEquals("15mm", handler.extractPackageCode("MKP10-100/1000/10"));
            assertEquals("7.5mm", handler.extractPackageCode("MKP4-1000/630/5"));
        }

        @Test
        void shouldGetMKPDielectric() {
            assertEquals("PP", handler.getDielectricType("MKP10-100/1000/10"));
        }

        @Test
        void shouldGetMKPConstructionType() {
            assertEquals("Metallized", handler.getConstructionType("MKP10-100/1000/10"));
        }
    }

    @Nested
    @DisplayName("FKP Series - Foil Polypropylene")
    class FKPSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "FKP2-330/100/5",      // 330pF, 100V, 5%
            "FKP2-330P/100/5",     // 330pF, 100V, 5%
            "FKP2-1000/63/5",      // 1000pF, 63V, 5%
            "FKP3-100/250/10"      // 100pF, 250V, 10%
        })
        void shouldDetectFKPCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    mpn + " should match CAPACITOR");
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_FILM_WIMA, registry),
                    mpn + " should match CAPACITOR_FILM_WIMA");
        }

        @Test
        void shouldExtractFKPSeries() {
            assertEquals("FKP2", handler.extractSeries("FKP2-330/100/5"));
            assertEquals("FKP3", handler.extractSeries("FKP3-100/250/10"));
        }

        @Test
        void shouldExtractFKPPackageCode() {
            assertEquals("5mm", handler.extractPackageCode("FKP2-330/100/5"));
            assertEquals("7.5mm", handler.extractPackageCode("FKP3-100/250/10"));
        }

        @Test
        void shouldGetFKPDielectric() {
            assertEquals("PP", handler.getDielectricType("FKP2-330/100/5"));
        }

        @Test
        void shouldGetFKPConstructionType() {
            assertEquals("Foil", handler.getConstructionType("FKP2-330/100/5"));
        }
    }

    @Nested
    @DisplayName("FKS Series - Foil Polyester")
    class FKSSeriesTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "FKS2-100/63/10",      // 100nF, 63V, 10%
            "FKS3-220/100/5",      // 220nF, 100V, 5%
            "FKS2-470n/63/10"      // 470nF, 63V, 10%
        })
        void shouldDetectFKSCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    mpn + " should match CAPACITOR");
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_FILM_WIMA, registry),
                    mpn + " should match CAPACITOR_FILM_WIMA");
        }

        @Test
        void shouldExtractFKSSeries() {
            assertEquals("FKS2", handler.extractSeries("FKS2-100/63/10"));
            assertEquals("FKS3", handler.extractSeries("FKS3-220/100/5"));
        }

        @Test
        void shouldGetFKSDielectric() {
            assertEquals("PET", handler.getDielectricType("FKS2-100/63/10"));
        }

        @Test
        void shouldGetFKSConstructionType() {
            assertEquals("Foil", handler.getConstructionType("FKS2-100/63/10"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @CsvSource({
            "MKS2-100/63/10, MKS2",
            "MKS4-100/63/10, MKS4",
            "MKP10-100/1000/10, MKP10",
            "MKP4-1000/630/5, MKP4",
            "FKP2-330/100/5, FKP2",
            "FKS3-220/100/5, FKS3",
            "FKC2-100/63/10, FKC2"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeExtractionTests {

        @ParameterizedTest
        @CsvSource({
            "MKS2-100/63/10, 5mm",
            "MKS4-100/63/10, 7.5mm",
            "MKP10-100/1000/10, 15mm",
            "FKP2-330/100/5, 5mm",
            "FKP3-100/250/10, 7.5mm"
        })
        void shouldExtractPackageCode(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Dielectric Type Extraction")
    class DielectricTypeTests {

        @ParameterizedTest
        @CsvSource({
            "MKS4-100/63/10, PET",
            "MKP10-100/1000/10, PP",
            "FKS2-100/63/10, PET",
            "FKP2-330/100/5, PP",
            "FKC2-100/63/10, PC"
        })
        void shouldExtractDielectricType(String mpn, String expectedDielectric) {
            assertEquals(expectedDielectric, handler.getDielectricType(mpn));
        }
    }

    @Nested
    @DisplayName("Construction Type Extraction")
    class ConstructionTypeTests {

        @ParameterizedTest
        @CsvSource({
            "MKS4-100/63/10, Metallized",
            "MKP10-100/1000/10, Metallized",
            "FKS2-100/63/10, Foil",
            "FKP2-330/100/5, Foil"
        })
        void shouldExtractConstructionType(String mpn, String expectedConstruction) {
            assertEquals(expectedConstruction, handler.getConstructionType(mpn));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {

        @Test
        void shouldAcceptSameSeriesAndPackageAsReplacement() {
            assertTrue(handler.isOfficialReplacement("MKS4-100/63/10", "MKS4-220/63/5"),
                    "Same series and package should be replacements");
        }

        @Test
        void shouldRejectDifferentSeriesAsReplacement() {
            assertFalse(handler.isOfficialReplacement("MKS4-100/63/10", "MKP4-100/63/10"),
                    "Different series should not be replacements");
        }

        @Test
        void shouldRejectDifferentSizeAsReplacement() {
            assertFalse(handler.isOfficialReplacement("MKS2-100/63/10", "MKS4-100/63/10"),
                    "Different size/pitch should not be replacements");
        }

        @Test
        void shouldAcceptDifferentValueSameSeriesAsReplacement() {
            assertTrue(handler.isOfficialReplacement("FKP2-330/100/5", "FKP2-470/100/5"),
                    "Different values but same series should be replacements");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.getDielectricType(null));
            assertEquals("", handler.getConstructionType(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getDielectricType(""));
            assertEquals("", handler.getConstructionType(""));
        }

        @Test
        void shouldHandleInvalidMPN() {
            assertFalse(handler.matches("INVALID123", ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches("GRM155R71C104KA88D", ComponentType.CAPACITOR, registry),
                    "Should not match Murata ceramic capacitor");
        }

        @Test
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("mks4-100/63/10", ComponentType.CAPACITOR, registry),
                    "Should match lowercase");
            assertTrue(handler.matches("MKS4-100/63/10", ComponentType.CAPACITOR, registry),
                    "Should match uppercase");
            assertTrue(handler.matches("Mks4-100/63/10", ComponentType.CAPACITOR, registry),
                    "Should match mixed case");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        void shouldReturnCorrectSupportedTypes() {
            var supportedTypes = handler.getSupportedTypes();
            assertTrue(supportedTypes.contains(ComponentType.CAPACITOR));
            assertTrue(supportedTypes.contains(ComponentType.CAPACITOR_FILM_WIMA));
            assertEquals(2, supportedTypes.size());
        }

        @Test
        void shouldUseImmutableSet() {
            var supportedTypes = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> supportedTypes.add(ComponentType.RESISTOR),
                    "getSupportedTypes should return immutable Set");
        }
    }

    @Nested
    @DisplayName("Integration with ComponentType")
    class IntegrationTests {

        @Test
        void shouldHaveCorrectBaseType() {
            assertEquals(ComponentType.CAPACITOR,
                    ComponentType.CAPACITOR_FILM_WIMA.getBaseType());
        }

        @Test
        void shouldBePassiveComponent() {
            assertTrue(ComponentType.CAPACITOR_FILM_WIMA.isPassive());
        }

        @Test
        void shouldNotBeSemiconductor() {
            assertFalse(ComponentType.CAPACITOR_FILM_WIMA.isSemiconductor());
        }
    }

    @Nested
    @DisplayName("Real World Part Numbers")
    class RealWorldTests {

        @ParameterizedTest
        @ValueSource(strings = {
            // Common audio coupling capacitors
            "MKP4-0.22/250/5",     // 220nF, 250V - audio coupling
            "MKP10-0.01/630",      // 10nF, 630V - snubber
            "FKP2-100P/63/2",      // 100pF, 63V - precision
            // WIMA Red Box series (MKS)
            "MKS4-1u/63/10",       // 1uF for general coupling
            "MKS2-100n/50/10",     // 100nF compact
            // High voltage pulse capacitors (MKP10)
            "MKP10-0.1/1000/10",   // 100nF, 1kV pulse
            "MKP10-0.047/1600/10"  // 47nF, 1.6kV high voltage
        })
        void shouldDetectRealWorldParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR_FILM_WIMA, registry),
                    mpn + " should be detected as WIMA film capacitor");
        }
    }
}
