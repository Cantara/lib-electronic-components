package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.NovatekHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for NovatekHandler (Novatek Microelectronics).
 * Tests pattern matching, package code extraction, series extraction, and display driver detection.
 */
class NovatekHandlerTest {

    private static NovatekHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new NovatekHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("NT35xxx TFT LCD Driver Detection")
    class NT35xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect NT35xxx TFT LCD drivers as IC")
        @ValueSource(strings = {
            "NT35510", "NT35510H", "NT35510-COG",
            "NT35516", "NT35516A",
            "NT35596", "NT35596H",
            "NT35521", "NT35521S",
            "NT35532", "NT35560"
        })
        void shouldDetectNT35xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("NT35xxx should be identified as TFT LCD drivers")
        @ValueSource(strings = {"NT35510", "NT35516", "NT35596", "NT35521"})
        void shouldIdentifyAsTFTLCDDriver(String mpn) {
            assertTrue(handler.isTFTLCDDriver(mpn),
                    mpn + " should be identified as TFT LCD driver");
        }

        @Test
        @DisplayName("NT35510 is a popular smartphone LCD driver")
        void shouldDetectNT35510() {
            assertTrue(handler.matches("NT35510", ComponentType.IC, registry));
            assertTrue(handler.matches("NT35510H", ComponentType.IC, registry));
            assertTrue(handler.isTFTLCDDriver("NT35510"));
            assertEquals("TFT LCD", handler.getDisplayTechnology("NT35510"));
        }

        @Test
        @DisplayName("NT35xxx should NOT be LED_DRIVER")
        void nt35xxxShouldNotBeLEDDriver() {
            assertFalse(handler.matches("NT35510", ComponentType.LED_DRIVER, registry),
                    "NT35510 should NOT match LED_DRIVER");
        }
    }

    @Nested
    @DisplayName("NT36xxx AMOLED Driver Detection")
    class NT36xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect NT36xxx AMOLED drivers as IC")
        @ValueSource(strings = {
            "NT36672", "NT36672A", "NT36672A-DP",
            "NT36675", "NT36675A",
            "NT36670", "NT36670A",
            "NT36676", "NT36680"
        })
        void shouldDetectNT36xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("NT36xxx should be identified as AMOLED drivers")
        @ValueSource(strings = {"NT36672", "NT36675", "NT36670", "NT36676"})
        void shouldIdentifyAsAMOLEDDriver(String mpn) {
            assertTrue(handler.isAMOLEDDriver(mpn),
                    mpn + " should be identified as AMOLED driver");
        }

        @Test
        @DisplayName("NT36672 is a popular AMOLED driver for smartphones")
        void shouldDetectNT36672() {
            assertTrue(handler.matches("NT36672", ComponentType.IC, registry));
            assertTrue(handler.matches("NT36672A", ComponentType.IC, registry));
            assertTrue(handler.isAMOLEDDriver("NT36672"));
            assertEquals("AMOLED", handler.getDisplayTechnology("NT36672"));
        }

        @Test
        @DisplayName("NT36xxx should NOT be TFT LCD driver")
        void nt36xxxShouldNotBeTFTDriver() {
            assertFalse(handler.isTFTLCDDriver("NT36672"),
                    "NT36672 should NOT be TFT LCD driver");
        }
    }

    @Nested
    @DisplayName("NT39xxx Advanced Display Controller Detection")
    class NT39xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect NT39xxx advanced display controllers as IC")
        @ValueSource(strings = {
            "NT39016", "NT39016D", "NT39016D-C02",
            "NT39125", "NT39125A",
            "NT39410", "NT39411",
            "NT39523", "NT39550"
        })
        void shouldDetectNT39xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("NT39xxx should have Advanced Display technology")
        void shouldDetectAdvancedDisplayTechnology() {
            assertEquals("Advanced Display", handler.getDisplayTechnology("NT39016"));
            assertEquals("Advanced Display", handler.getDisplayTechnology("NT39125"));
        }
    }

    @Nested
    @DisplayName("NT51xxx LED Backlight Driver Detection")
    class NT51xxxTests {

        @ParameterizedTest
        @DisplayName("Should detect NT51xxx LED backlight drivers as IC and LED_DRIVER")
        @ValueSource(strings = {
            "NT51021", "NT51021B",
            "NT51035", "NT51035A",
            "NT51198", "NT51199"
        })
        void shouldDetectNT51xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.LED_DRIVER, registry),
                    mpn + " should match LED_DRIVER");
        }

        @Test
        @DisplayName("NT51xxx should have LED Backlight technology")
        void shouldDetectLEDBacklightTechnology() {
            assertEquals("LED Backlight", handler.getDisplayTechnology("NT51021"));
            assertEquals("LED Backlight", handler.getDisplayTechnology("NT51035"));
        }
    }

    @Nested
    @DisplayName("Touch Controller Detection (NT66xxx, NT67xxx)")
    class TouchControllerTests {

        @ParameterizedTest
        @DisplayName("Should detect NT66xxx touch controllers as IC")
        @ValueSource(strings = {
            "NT66525", "NT66525A",
            "NT66526", "NT66527",
            "NT66801", "NT66802"
        })
        void shouldDetectNT66xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.isTouchController(mpn),
                    mpn + " should be touch controller");
        }

        @ParameterizedTest
        @DisplayName("Should detect NT67xxx touch controllers as IC")
        @ValueSource(strings = {
            "NT67001", "NT67001A",
            "NT67121", "NT67122",
            "NT67501", "NT67502"
        })
        void shouldDetectNT67xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.isTouchController(mpn),
                    mpn + " should be touch controller");
        }

        @Test
        @DisplayName("Touch controllers should have correct technology type")
        void shouldDetectTouchControllerTechnology() {
            assertEquals("Touch Controller", handler.getDisplayTechnology("NT66525"));
            assertEquals("Touch Controller", handler.getDisplayTechnology("NT67001"));
        }

        @Test
        @DisplayName("Touch controllers should NOT be display drivers")
        void touchControllersShouldNotBeDisplayDrivers() {
            assertFalse(handler.isTFTLCDDriver("NT66525"),
                    "NT66525 should NOT be TFT LCD driver");
            assertFalse(handler.isAMOLEDDriver("NT67001"),
                    "NT67001 should NOT be AMOLED driver");
        }
    }

    @Nested
    @DisplayName("NT50xxx Timing Controller Detection")
    class TimingControllerTests {

        @ParameterizedTest
        @DisplayName("Should detect NT50xxx timing controllers as IC")
        @ValueSource(strings = {
            "NT50198", "NT50198A",
            "NT50358", "NT50366",
            "NT50168", "NT50197"
        })
        void shouldDetectNT50xxxAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("NT50xxx should have Timing Controller technology")
        void shouldDetectTimingControllerTechnology() {
            assertEquals("Timing Controller", handler.getDisplayTechnology("NT50198"));
            assertEquals("Timing Controller", handler.getDisplayTechnology("NT50358"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from suffix")
        @CsvSource({
            "NT35510H, COG",
            "NT35510C, COF",
            "NT35510A, AMOLED",
            "NT35510D, Display Driver"
        })
        void shouldExtractPackageCodesFromSuffix(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes from hyphenated format")
        @CsvSource({
            "NT35510-COG, Chip-on-Glass",
            "NT35510-COF, Chip-on-Film",
            "NT35510-BGA, BGA",
            "NT35510-QFP, QFP"
        })
        void shouldExtractPackageCodesFromHyphen(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle empty and null MPN")
        void shouldHandleEmptyAndNull() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series codes")
        @CsvSource({
            "NT35510, NT35",
            "NT35510H, NT35",
            "NT36672A, NT36",
            "NT39016D, NT39",
            "NT50198, NT50",
            "NT51021, NT51",
            "NT66525, NT66",
            "NT67001, NT67"
        })
        void shouldExtractSeriesCodes(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPN")
        void shouldReturnEmptyForInvalid() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("INVALID"));
            assertEquals("", handler.extractSeries("ABC123"));
        }
    }

    @Nested
    @DisplayName("Series Description")
    class SeriesDescriptionTests {

        @ParameterizedTest
        @DisplayName("Should return correct series descriptions")
        @CsvSource({
            "NT35, TFT LCD Drivers",
            "NT36, AMOLED Drivers",
            "NT39, Advanced Display Controllers",
            "NT50, Timing Controllers",
            "NT51, LED Backlight Drivers",
            "NT66, Touch Controllers",
            "NT67, Touch Controllers"
        })
        void shouldReturnSeriesDescription(String series, String expectedDesc) {
            assertEquals(expectedDesc, handler.getSeriesDescription(series));
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknown() {
            assertEquals("", handler.getSeriesDescription("UNKNOWN"));
            assertEquals("", handler.getSeriesDescription("NT99"));
            assertEquals("", handler.getSeriesDescription(""));
        }
    }

    @Nested
    @DisplayName("Display Technology Detection")
    class DisplayTechnologyTests {

        @ParameterizedTest
        @DisplayName("Should detect correct display technology")
        @CsvSource({
            "NT35510, TFT LCD",
            "NT36672, AMOLED",
            "NT37123, OLED",
            "NT38456, OLED",
            "NT39016, Advanced Display",
            "NT50198, Timing Controller",
            "NT51021, LED Backlight",
            "NT66525, Touch Controller",
            "NT67001, Touch Controller"
        })
        void shouldDetectDisplayTechnology(String mpn, String expectedTech) {
            assertEquals(expectedTech, handler.getDisplayTechnology(mpn),
                    "Technology for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for non-Novatek parts")
        void shouldReturnEmptyForNonNovatek() {
            assertEquals("", handler.getDisplayTechnology("LM7805"));
            assertEquals("", handler.getDisplayTechnology("STM32F103"));
            assertEquals("", handler.getDisplayTechnology(null));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different revisions should be replacements")
        void samePartDifferentRevisions() {
            assertTrue(handler.isOfficialReplacement("NT35510", "NT35510H"),
                    "NT35510 and NT35510H should be replacements");
            assertTrue(handler.isOfficialReplacement("NT36672A", "NT36672B"),
                    "NT36672A and NT36672B should be replacements");
        }

        @Test
        @DisplayName("Same part with and without package suffix should be replacements")
        void samePartWithPackageSuffix() {
            assertTrue(handler.isOfficialReplacement("NT35510", "NT35510-COG"),
                    "NT35510 and NT35510-COG should be replacements");
            assertTrue(handler.isOfficialReplacement("NT36672", "NT36672A-DP"),
                    "NT36672 and NT36672A-DP should be replacements");
        }

        @Test
        @DisplayName("Parts in same family (sequential numbers) should be replacements")
        void sameFamily() {
            assertTrue(handler.isOfficialReplacement("NT35510", "NT35511"),
                    "NT35510 and NT35511 should be replacements (same family)");
            assertTrue(handler.isOfficialReplacement("NT36672", "NT36673"),
                    "NT36672 and NT36673 should be replacements (same family)");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("NT35510", "NT36672"),
                    "NT35510 and NT36672 should NOT be replacements (different series)");
            assertFalse(handler.isOfficialReplacement("NT35510", "NT66525"),
                    "NT35510 and NT66525 should NOT be replacements");
        }

        @Test
        @DisplayName("Very different parts in same series should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("NT35100", "NT35999"),
                    "NT35100 and NT35999 should NOT be replacements");
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "NT35510"));
            assertFalse(handler.isOfficialReplacement("NT35510", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.IC));
            assertTrue(types.contains(ComponentType.LED_DRIVER));
        }

        @Test
        @DisplayName("Should use Set.of() for immutability")
        void shouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes() should return immutable Set");
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
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "NT35510"));
            assertFalse(handler.isOfficialReplacement("NT35510", null));
            assertFalse(handler.isTFTLCDDriver(null));
            assertFalse(handler.isAMOLEDDriver(null));
            assertFalse(handler.isTouchController(null));
            assertEquals("", handler.getDisplayTechnology(null));
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
            assertFalse(handler.matches("NT35510", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("nt35510", ComponentType.IC, registry));
            assertTrue(handler.matches("NT35510", ComponentType.IC, registry));
            assertTrue(handler.matches("Nt35510", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should not match non-Novatek parts")
        void shouldNotMatchNonNovatekParts() {
            assertFalse(handler.matches("LM7805", ComponentType.IC, registry),
                    "LM7805 is not a Novatek part");
            assertFalse(handler.matches("STM32F103", ComponentType.IC, registry),
                    "STM32F103 is not a Novatek part");
            assertFalse(handler.matches("NT-UNRELATED", ComponentType.IC, registry),
                    "NT-UNRELATED is not a valid Novatek MPN");
        }

        @Test
        @DisplayName("Should handle MPN with only prefix")
        void shouldHandleShortMpn() {
            assertFalse(handler.matches("NT", ComponentType.IC, registry));
            assertFalse(handler.matches("NT3", ComponentType.IC, registry));
            assertFalse(handler.matches("NT35", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldMPNTests {

        @ParameterizedTest
        @DisplayName("Should correctly identify popular Novatek parts")
        @CsvSource({
            "NT35510H, IC, true, TFT LCD",
            "NT35516A, IC, true, TFT LCD",
            "NT35596, IC, true, TFT LCD",
            "NT36672A, IC, false, AMOLED",
            "NT36675, IC, false, AMOLED",
            "NT39016D, IC, false, Advanced Display",
            "NT51021B, LED_DRIVER, false, LED Backlight",
            "NT66525A, IC, false, Touch Controller"
        })
        void shouldIdentifyPopularParts(String mpn, String expectedType, boolean isTFT, String tech) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry),
                    mpn + " should match " + expectedType);
            assertEquals(isTFT, handler.isTFTLCDDriver(mpn));
            assertEquals(tech, handler.getDisplayTechnology(mpn));
        }

        @Test
        @DisplayName("Should handle full MPN with all suffixes")
        void shouldHandleFullMPN() {
            String fullMpn = "NT36672A-DP";
            assertTrue(handler.matches(fullMpn, ComponentType.IC, registry));
            assertTrue(handler.isAMOLEDDriver(fullMpn));
            assertEquals("NT36", handler.extractSeries(fullMpn));
            assertEquals("AMOLED", handler.getDisplayTechnology(fullMpn));
        }
    }

    @Nested
    @DisplayName("Type Hierarchy Tests")
    class TypeHierarchyTests {

        @Test
        @DisplayName("All Novatek display drivers should match IC")
        void allPartsShouldMatchIC() {
            String[] testMpns = {
                "NT35510", "NT36672", "NT39016",
                "NT50198", "NT51021", "NT66525", "NT67001"
            };

            for (String mpn : testMpns) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC");
            }
        }

        @Test
        @DisplayName("Only NT51xxx should match LED_DRIVER")
        void onlyNT51xxxShouldMatchLEDDriver() {
            assertTrue(handler.matches("NT51021", ComponentType.LED_DRIVER, registry));
            assertTrue(handler.matches("NT51035", ComponentType.LED_DRIVER, registry));

            assertFalse(handler.matches("NT35510", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("NT36672", ComponentType.LED_DRIVER, registry));
            assertFalse(handler.matches("NT66525", ComponentType.LED_DRIVER, registry));
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {

        @Test
        @DisplayName("Should return empty manufacturer types")
        void shouldReturnEmptyManufacturerTypes() {
            assertTrue(handler.getManufacturerTypes().isEmpty(),
                    "getManufacturerTypes() should return empty set");
        }
    }
}
