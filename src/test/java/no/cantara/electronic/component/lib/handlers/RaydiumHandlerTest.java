package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.RaydiumHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for RaydiumHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Raydium Semiconductor product families:
 * - RM68xxx: TFT LCD drivers (RM68120, RM68140, RM68172)
 * - RM69xxx: AMOLED display drivers (RM69032, RM69080, RM69091)
 * - RM31xxx: Touch controllers (RM31100, RM31080)
 * - RM35xxx: Touch + display integrated controllers
 *
 * Package codes: COG=Chip-On-Glass, COF=Chip-On-Film, BGA=Ball Grid Array, QFN=Quad Flat No-leads
 */
class RaydiumHandlerTest {

    private static RaydiumHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new RaydiumHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("TFT LCD Driver Detection (RM68xxx Series)")
    class TFTDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect RM68xxx TFT drivers as IC")
        @ValueSource(strings = {"RM68120", "RM68140", "RM68172", "RM68191", "RM68200"})
        void shouldDetectRM68SeriesAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect RM68xxx with package suffixes")
        @ValueSource(strings = {"RM68120-COG", "RM68140-COF", "RM68172-BGA", "RM68191QFN"})
        void shouldDetectRM68WithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @Test
        @DisplayName("Should identify RM68xxx as TFT LCD Driver")
        void shouldIdentifyAsTFTDriver() {
            assertEquals("TFT LCD Driver", handler.getProductType("RM68120"));
            assertEquals("TFT LCD Driver", handler.getProductType("RM68172"));
        }

        @Test
        @DisplayName("RM68xxx should be display drivers")
        void shouldBeDisplayDrivers() {
            assertTrue(handler.isDisplayDriver("RM68120"));
            assertTrue(handler.isDisplayDriver("RM68172-COG"));
        }

        @Test
        @DisplayName("RM68xxx should not be touch controllers")
        void shouldNotBeTouchControllers() {
            assertFalse(handler.isTouchController("RM68120"));
            assertFalse(handler.isTouchController("RM68172"));
        }
    }

    @Nested
    @DisplayName("AMOLED Driver Detection (RM69xxx Series)")
    class AMOLEDDriverTests {

        @ParameterizedTest
        @DisplayName("Should detect RM69xxx AMOLED drivers as IC")
        @ValueSource(strings = {"RM69032", "RM69080", "RM69091", "RM69100", "RM69299"})
        void shouldDetectRM69SeriesAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect RM69xxx with package suffixes")
        @ValueSource(strings = {"RM69091-COG", "RM69080-COF", "RM69032BGA"})
        void shouldDetectRM69WithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @Test
        @DisplayName("Should identify RM69xxx as AMOLED Display Driver")
        void shouldIdentifyAsAMOLEDDriver() {
            assertEquals("AMOLED Display Driver", handler.getProductType("RM69091"));
            assertEquals("AMOLED Display Driver", handler.getProductType("RM69080"));
        }

        @Test
        @DisplayName("RM69xxx should be display drivers")
        void shouldBeDisplayDrivers() {
            assertTrue(handler.isDisplayDriver("RM69091"));
            assertTrue(handler.isDisplayDriver("RM69080-COG"));
        }
    }

    @Nested
    @DisplayName("Touch Controller Detection (RM31xxx Series)")
    class TouchControllerTests {

        @ParameterizedTest
        @DisplayName("Should detect RM31xxx touch controllers as IC")
        @ValueSource(strings = {"RM31100", "RM31080", "RM31200", "RM31300"})
        void shouldDetectRM31SeriesAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect RM31xxx with package suffixes")
        @ValueSource(strings = {"RM31100-QFN", "RM31080BGA", "RM31200-WLCSP"})
        void shouldDetectRM31WithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @Test
        @DisplayName("Should identify RM31xxx as Touch Controller")
        void shouldIdentifyAsTouchController() {
            assertEquals("Touch Controller", handler.getProductType("RM31100"));
            assertEquals("Touch Controller", handler.getProductType("RM31080"));
        }

        @Test
        @DisplayName("RM31xxx should be touch controllers")
        void shouldBeTouchControllers() {
            assertTrue(handler.isTouchController("RM31100"));
            assertTrue(handler.isTouchController("RM31080-QFN"));
        }

        @Test
        @DisplayName("RM31xxx should not be display drivers")
        void shouldNotBeDisplayDrivers() {
            assertFalse(handler.isDisplayDriver("RM31100"));
            assertFalse(handler.isDisplayDriver("RM31080"));
        }
    }

    @Nested
    @DisplayName("Touch + Display Controller Detection (RM35xxx Series)")
    class TouchDisplayControllerTests {

        @ParameterizedTest
        @DisplayName("Should detect RM35xxx integrated controllers as IC")
        @ValueSource(strings = {"RM35100", "RM35200", "RM35300"})
        void shouldDetectRM35SeriesAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect RM35xxx with package suffixes")
        @ValueSource(strings = {"RM35100-COG", "RM35200BGA"})
        void shouldDetectRM35WithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @Test
        @DisplayName("Should identify RM35xxx as Touch + Display Controller")
        void shouldIdentifyAsTouchDisplayController() {
            assertEquals("Touch + Display Controller", handler.getProductType("RM35100"));
            assertEquals("Touch + Display Controller", handler.getProductType("RM35200"));
        }

        @Test
        @DisplayName("RM35xxx should be both touch and display controllers")
        void shouldBeBothTouchAndDisplay() {
            assertTrue(handler.isTouchController("RM35100"));
            assertTrue(handler.isDisplayDriver("RM35100"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract hyphenated package codes")
        @CsvSource({
                "RM69091-COG, COG",
                "RM68140-COF, COF",
                "RM31100-BGA, BGA",
                "RM35200-QFN, QFN",
                "RM69080-WLCSP, WLCSP"
        })
        void shouldExtractHyphenatedPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract concatenated package codes")
        @CsvSource({
                "RM69091COG, COG",
                "RM68140COF, COF",
                "RM31100BGA, BGA",
                "RM35200QFN, QFN"
        })
        void shouldExtractConcatenatedPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for base MPN without package code")
        void shouldReturnEmptyForBaseMPN() {
            assertEquals("", handler.extractPackageCode("RM68120"),
                    "Base MPN should return empty package code");
            assertEquals("", handler.extractPackageCode("RM69091"),
                    "Base MPN should return empty package code");
        }

        @Test
        @DisplayName("Should return empty for unknown package codes")
        void shouldReturnEmptyForUnknownPackage() {
            assertEquals("", handler.extractPackageCode("RM68120-XYZ"),
                    "Unknown package code should return empty");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract TFT driver series")
        @CsvSource({
                "RM68120, RM68",
                "RM68140, RM68",
                "RM68172, RM68",
                "RM68120-COG, RM68"
        })
        void shouldExtractTFTSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract AMOLED driver series")
        @CsvSource({
                "RM69032, RM69",
                "RM69080, RM69",
                "RM69091, RM69",
                "RM69091-COG, RM69"
        })
        void shouldExtractAMOLEDSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract touch controller series")
        @CsvSource({
                "RM31100, RM31",
                "RM31080, RM31",
                "RM31100-QFN, RM31"
        })
        void shouldExtractTouchSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract touch+display controller series")
        @CsvSource({
                "RM35100, RM35",
                "RM35200, RM35",
                "RM35100-COG, RM35"
        })
        void shouldExtractTouchDisplaySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.extractSeries("RM12345"),
                    "Invalid series should return empty");
            assertEquals("", handler.extractSeries("RM00000"),
                    "Invalid series should return empty");
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part with different packages should be replacements")
        void samePartDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("RM69091-COG", "RM69091-COF"),
                    "Same part with different packages should be replacements");
            assertTrue(handler.isOfficialReplacement("RM68120-COG", "RM68120BGA"),
                    "Same part with different package formats should be replacements");
        }

        @Test
        @DisplayName("Same base part should be replacement")
        void sameBasePartShouldBeReplacement() {
            assertTrue(handler.isOfficialReplacement("RM69091", "RM69091"),
                    "Same MPN should be replacement");
            assertTrue(handler.isOfficialReplacement("RM31100", "RM31100-QFN"),
                    "Base and packaged version should be replacements");
        }

        @Test
        @DisplayName("Different parts in same series should NOT be replacements")
        void differentPartsInSameSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("RM69091", "RM69080"),
                    "Different AMOLED drivers support different resolutions");
            assertFalse(handler.isOfficialReplacement("RM68120", "RM68172"),
                    "Different TFT drivers support different panels");
        }

        @Test
        @DisplayName("Parts from different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("RM68120", "RM69091"),
                    "TFT and AMOLED drivers are not interchangeable");
            assertFalse(handler.isOfficialReplacement("RM31100", "RM68120"),
                    "Touch controller and display driver are not interchangeable");
            assertFalse(handler.isOfficialReplacement("RM31100", "RM35100"),
                    "Touch and touch+display controllers are different product types");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC type")
        void shouldSupportICType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of()")
        void shouldUseSetOf() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes should return immutable Set.of()");
        }
    }

    @Nested
    @DisplayName("Series Description")
    class SeriesDescriptionTests {

        @Test
        @DisplayName("Should return correct series descriptions")
        void shouldReturnCorrectDescriptions() {
            assertEquals("TFT LCD Drivers", handler.getSeriesDescription("RM68"));
            assertEquals("AMOLED Display Drivers", handler.getSeriesDescription("RM69"));
            assertEquals("Touch Controllers", handler.getSeriesDescription("RM31"));
            assertEquals("Touch + Display Controllers", handler.getSeriesDescription("RM35"));
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.getSeriesDescription("RM00"));
            assertEquals("", handler.getSeriesDescription("XX99"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "RM69091"));
            assertFalse(handler.isOfficialReplacement("RM69091", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("RM69091", null, registry));
        }

        @Test
        @DisplayName("Should handle lowercase MPN")
        void shouldHandleLowercaseMPN() {
            assertTrue(handler.matches("rm69091", ComponentType.IC, registry),
                    "Should handle lowercase MPN");
            assertEquals("RM69", handler.extractSeries("rm69091"),
                    "Should extract series from lowercase MPN");
        }

        @Test
        @DisplayName("Should reject non-Raydium MPNs")
        void shouldRejectNonRaydiumMPNs() {
            assertFalse(handler.matches("STM32F103", ComponentType.IC, registry),
                    "Should reject ST MPN");
            assertFalse(handler.matches("ATMEGA328P", ComponentType.IC, registry),
                    "Should reject Atmel MPN");
            assertFalse(handler.matches("LM358", ComponentType.IC, registry),
                    "Should reject TI MPN");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            RaydiumHandler directHandler = new RaydiumHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes());
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
    @DisplayName("Product Type Detection")
    class ProductTypeTests {

        @Test
        @DisplayName("Should return correct product types")
        void shouldReturnCorrectProductTypes() {
            assertEquals("TFT LCD Driver", handler.getProductType("RM68120"));
            assertEquals("AMOLED Display Driver", handler.getProductType("RM69091"));
            assertEquals("Touch Controller", handler.getProductType("RM31100"));
            assertEquals("Touch + Display Controller", handler.getProductType("RM35100"));
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalidMPN() {
            assertEquals("", handler.getProductType("RM00000"));
            assertEquals("", handler.getProductType("INVALID"));
            assertEquals("", handler.getProductType(null));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @Test
        @DisplayName("RM69091 (common AMOLED driver for smartwatches)")
        void rm69091CommonAMOLED() {
            String mpn = "RM69091";
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
            assertEquals("RM69", handler.extractSeries(mpn));
            assertEquals("AMOLED Display Driver", handler.getProductType(mpn));
            assertTrue(handler.isDisplayDriver(mpn));
            assertFalse(handler.isTouchController(mpn));
        }

        @Test
        @DisplayName("RM68120 (TFT driver for small displays)")
        void rm68120TFTDriver() {
            String mpn = "RM68120";
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
            assertEquals("RM68", handler.extractSeries(mpn));
            assertEquals("TFT LCD Driver", handler.getProductType(mpn));
            assertTrue(handler.isDisplayDriver(mpn));
            assertFalse(handler.isTouchController(mpn));
        }

        @Test
        @DisplayName("RM31100 (capacitive touch controller)")
        void rm31100TouchController() {
            String mpn = "RM31100";
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
            assertEquals("RM31", handler.extractSeries(mpn));
            assertEquals("Touch Controller", handler.getProductType(mpn));
            assertFalse(handler.isDisplayDriver(mpn));
            assertTrue(handler.isTouchController(mpn));
        }

        @Test
        @DisplayName("RM69080 with COG package")
        void rm69080WithCOGPackage() {
            String mpn = "RM69080-COG";
            assertTrue(handler.matches(mpn, ComponentType.IC, registry));
            assertEquals("RM69", handler.extractSeries(mpn));
            assertEquals("COG", handler.extractPackageCode(mpn));
            assertEquals("AMOLED Display Driver", handler.getProductType(mpn));
        }
    }
}
