package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.CirrusLogicHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CirrusLogicHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Cirrus Logic Product Categories:
 * - CS42xx: Audio ADCs (CS4270, CS4272)
 * - CS43xx: Audio DACs/CODECs (CS4334, CS4344, CS43L22)
 * - CS47xx: DSP Audio (CS47L24, CS47L35)
 * - CS48xx: SoundClear DSP (CS4860x)
 * - CS53xx: LED Drivers (CS5361)
 * - CS84xx: Digital Audio Interface (CS8416, CS8422)
 * - WM8xxx: Wolfson Audio (acquired by Cirrus) (WM8731, WM8960, WM8994)
 *
 * Package Codes:
 * - CZZ: TSSOP
 * - CNZ: QFN
 * - DZZ: SSOP
 * - SEDS: SOIC (Wolfson)
 * - CGEFL: QFN with exposed pad (Wolfson)
 */
class CirrusLogicHandlerTest {

    private static CirrusLogicHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new CirrusLogicHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Audio ADC Detection (CS42xx)")
    class AudioADCTests {

        @ParameterizedTest
        @DisplayName("CS42xx Audio ADCs should match IC type")
        @ValueSource(strings = {"CS4270", "CS4270-CZZ", "CS4270-CNZ", "CS4271", "CS4272", "CS4272-DZZ"})
        void cs42xxShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("CS42xx should be detected via registry")
        @ValueSource(strings = {"CS4270", "CS4271", "CS4272", "CS4270-CZZ"})
        void cs42xxShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry");
        }

        @Test
        @DisplayName("CS4270 variants should be detected")
        void cs4270VariantsShouldBeDetected() {
            assertTrue(handler.matches("CS4270-CZZ", ComponentType.IC, registry));
            assertTrue(handler.matches("CS4270-CNZ", ComponentType.IC, registry));
            assertTrue(handler.matches("CS4270-CZZR", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Audio DAC/CODEC Detection (CS43xx)")
    class AudioDACCodecTests {

        @ParameterizedTest
        @DisplayName("CS43xx Audio DACs should match IC type")
        @ValueSource(strings = {"CS4334", "CS4334-CZZ", "CS4340", "CS4344", "CS4344-CZZ", "CS4344-CNZ"})
        void cs43xxDacShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("CS43Lxx Audio CODECs should match IC type")
        @ValueSource(strings = {"CS43L22", "CS43L22-CNZ", "CS43L21", "CS43L36", "CS43L41"})
        void cs43LxxCodecShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("CS4344 is a common stereo DAC")
        void cs4344ShouldBeDetected() {
            assertTrue(handler.matches("CS4344-CZZ", ComponentType.IC, registry));
            assertTrue(handler.matches("CS4344-CZZR", ComponentType.IC, registry));
            assertEquals("CS43", handler.extractSeries("CS4344-CZZ"));
        }

        @Test
        @DisplayName("CS43L22 is a popular CODEC")
        void cs43L22ShouldBeDetected() {
            assertTrue(handler.matches("CS43L22-CNZ", ComponentType.IC, registry));
            assertEquals("CS43", handler.extractSeries("CS43L22-CNZ"));
        }
    }

    @Nested
    @DisplayName("Audio DSP Detection (CS47xx)")
    class AudioDSPTests {

        @ParameterizedTest
        @DisplayName("CS47xx DSP Audio should match IC type")
        @ValueSource(strings = {"CS47L24", "CS47L24-CNZ", "CS47L35", "CS47L63", "CS47L85"})
        void cs47xxDspShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("CS47L24 smart codec should be detected")
        void cs47L24ShouldBeDetected() {
            assertTrue(handler.matches("CS47L24", ComponentType.IC, registry));
            assertTrue(handler.matches("CS47L24-CWZR", ComponentType.IC, registry));
            assertEquals("CS47", handler.extractSeries("CS47L24"));
        }
    }

    @Nested
    @DisplayName("SoundClear DSP Detection (CS48xx)")
    class SoundClearDSPTests {

        @ParameterizedTest
        @DisplayName("CS48xx SoundClear DSP should match IC type")
        @ValueSource(strings = {"CS4860", "CS48600", "CS48601", "CS4862"})
        void cs48xxSoundClearShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("CS4860x series should be detected")
        void cs4860xSeriesShouldBeDetected() {
            assertTrue(handler.matches("CS4860", ComponentType.IC, registry));
            assertEquals("CS48", handler.extractSeries("CS4860"));
        }
    }

    @Nested
    @DisplayName("LED Driver Detection (CS53xx)")
    class LEDDriverTests {

        @ParameterizedTest
        @DisplayName("CS53xx LED Drivers should match IC type")
        @ValueSource(strings = {"CS5361", "CS5361-CNZ", "CS5368", "CS5381"})
        void cs53xxLedDriversShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("CS5361 should be detected")
        void cs5361ShouldBeDetected() {
            assertTrue(handler.matches("CS5361", ComponentType.IC, registry));
            assertEquals("CS53", handler.extractSeries("CS5361"));
        }
    }

    @Nested
    @DisplayName("Digital Audio Interface Detection (CS84xx)")
    class DigitalAudioInterfaceTests {

        @ParameterizedTest
        @DisplayName("CS84xx Digital Audio Interface should match IC type")
        @ValueSource(strings = {"CS8416", "CS8416-CZZ", "CS8422", "CS8427", "CS8406"})
        void cs84xxDigitalAudioShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("CS8416 S/PDIF receiver should be detected")
        void cs8416ShouldBeDetected() {
            assertTrue(handler.matches("CS8416-CZZ", ComponentType.IC, registry));
            assertEquals("CS84", handler.extractSeries("CS8416-CZZ"));
        }

        @Test
        @DisplayName("CS8422 digital audio receiver should be detected")
        void cs8422ShouldBeDetected() {
            assertTrue(handler.matches("CS8422-CNZ", ComponentType.IC, registry));
            assertEquals("CS84", handler.extractSeries("CS8422-CNZ"));
        }
    }

    @Nested
    @DisplayName("Wolfson Audio Detection (WM8xxx)")
    class WolfsonAudioTests {

        @ParameterizedTest
        @DisplayName("WM8xxx Wolfson CODECs should match IC type")
        @ValueSource(strings = {"WM8731", "WM8731SEDS", "WM8731CLQVP", "WM8750", "WM8750L"})
        void wm8xxxCodecsShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("WM89xx Wolfson CODECs should match IC type")
        @ValueSource(strings = {"WM8960", "WM8960CGEFL", "WM8962", "WM8994", "WM8994ECS"})
        void wm89xxCodecsShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("WM8731 is a popular low-power CODEC")
        void wm8731ShouldBeDetected() {
            assertTrue(handler.matches("WM8731SEDS", ComponentType.IC, registry));
            assertEquals("WM87", handler.extractSeries("WM8731SEDS"));
        }

        @Test
        @DisplayName("WM8960 is a popular stereo CODEC")
        void wm8960ShouldBeDetected() {
            assertTrue(handler.matches("WM8960CGEFL", ComponentType.IC, registry));
            assertEquals("WM89", handler.extractSeries("WM8960CGEFL"));
        }

        @Test
        @DisplayName("WM8994 is a high-performance CODEC")
        void wm8994ShouldBeDetected() {
            assertTrue(handler.matches("WM8994", ComponentType.IC, registry));
            assertEquals("WM89", handler.extractSeries("WM8994"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract CS series package codes")
        @CsvSource({
                "CS4344-CZZ, TSSOP",
                "CS4344-CNZ, QFN",
                "CS4270-DZZ, SSOP",
                "CS4344-CZZR, TSSOP",
                "CS4344-CNZR, QFN"
        })
        void shouldExtractCSPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Wolfson package codes")
        @CsvSource({
                "WM8731SEDS, SOIC",
                "WM8960CGEFL, QFN",
                "WM8994EFL, QFN"
        })
        void shouldExtractWolfsonPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle parts without package suffix")
        void shouldHandlePartsWithoutPackageSuffix() {
            String result = handler.extractPackageCode("CS4344");
            assertNotNull(result);
        }

        @Test
        @DisplayName("Should handle hyphenated suffixes")
        void shouldHandleHyphenatedSuffixes() {
            assertEquals("TSSOP", handler.extractPackageCode("CS4344-CZZ"));
            assertEquals("QFN", handler.extractPackageCode("CS43L22-CNZ"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract CS series correctly")
        @CsvSource({
                "CS4270-CZZ, CS42",
                "CS4344-CNZ, CS43",
                "CS43L22-CNZ, CS43",
                "CS47L24-CWZR, CS47",
                "CS4860, CS48",
                "CS5361-CNZ, CS53",
                "CS8416-CZZ, CS84"
        })
        void shouldExtractCSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Wolfson series correctly")
        @CsvSource({
                "WM8731SEDS, WM87",
                "WM8750L, WM87",
                "WM8960CGEFL, WM89",
                "WM8994ECS, WM89"
        })
        void shouldExtractWolfsonSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid input")
        void shouldReturnEmptyForInvalidInput() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("CS4344-CZZ", "CS4344-CNZ"),
                    "Same part with TSSOP and QFN packages should be replacements");
            assertTrue(handler.isOfficialReplacement("WM8731SEDS", "WM8731CLQVP"),
                    "Same Wolfson part with different packages should be replacements");
        }

        @Test
        @DisplayName("Same part with reel suffix should be replacement")
        void samePartWithReelSuffixAreReplacements() {
            assertTrue(handler.isOfficialReplacement("CS4344-CZZ", "CS4344-CZZR"),
                    "Part with reel suffix should be replacement");
        }

        @Test
        @DisplayName("Different parts should NOT be replacements")
        void differentPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CS4344-CZZ", "CS4334-CZZ"),
                    "Different part numbers should not be replacements");
            assertFalse(handler.isOfficialReplacement("CS4270-CZZ", "CS8416-CZZ"),
                    "Different series should not be replacements");
        }

        @Test
        @DisplayName("CS vs Wolfson parts should NOT be replacements")
        void csVsWolfsonNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CS4344-CZZ", "WM8731SEDS"),
                    "CS and Wolfson parts should not be replacements");
        }

        @Test
        @DisplayName("Different Wolfson parts should NOT be replacements")
        void differentWolfsonPartsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("WM8731SEDS", "WM8960CGEFL"),
                    "Different Wolfson parts should not be replacements");
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
                    "Should support IC type");
        }

        @Test
        @DisplayName("Should use Set.of() not HashSet")
        void shouldUseSetOf() {
            var types = handler.getSupportedTypes();
            // Set.of() returns an ImmutableCollections class
            assertThrows(UnsupportedOperationException.class, () ->
                    types.add(ComponentType.SENSOR),
                    "getSupportedTypes() should return immutable Set from Set.of()");
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
            assertFalse(handler.isOfficialReplacement(null, "CS4344-CZZ"));
            assertFalse(handler.isOfficialReplacement("CS4344-CZZ", null));
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
            assertFalse(handler.matches("CS4344-CZZ", null, registry));
        }

        @Test
        @DisplayName("Case insensitive matching via handler")
        void caseInsensitiveViaHandler() {
            assertTrue(handler.matches("cs4344-czz", ComponentType.IC, registry));
            assertTrue(handler.matches("CS4344-CZZ", ComponentType.IC, registry));
            assertTrue(handler.matches("wm8731seds", ComponentType.IC, registry));
            assertTrue(handler.matches("WM8731SEDS", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Case insensitive matching via registry")
        void caseInsensitiveViaRegistry() {
            assertTrue(registry.matches("cs4344", ComponentType.IC));
            assertTrue(registry.matches("CS4344", ComponentType.IC));
            assertTrue(registry.matches("wm8731", ComponentType.IC));
            assertTrue(registry.matches("WM8731", ComponentType.IC));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            CirrusLogicHandler directHandler = new CirrusLogicHandler();
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
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @ParameterizedTest
        @DisplayName("Common Cirrus Logic Audio ICs")
        @ValueSource(strings = {
                "CS4344-CZZ",     // Popular stereo DAC
                "CS43L22-CNZ",    // Popular audio CODEC
                "CS8416-CZZ",     // S/PDIF receiver
                "CS4270-DZZ",     // Audio ADC/DAC
                "CS47L24"         // Smart CODEC
        })
        void commonCirrusLogicParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
        }

        @ParameterizedTest
        @DisplayName("Common Wolfson Audio ICs")
        @ValueSource(strings = {
                "WM8731SEDS",     // Low-power stereo CODEC
                "WM8960CGEFL",    // Portable audio CODEC
                "WM8994ECS",      // High-performance CODEC
                "WM8750L",        // Stereo CODEC
                "WM8962"          // Ultra-low power CODEC
        })
        void commonWolfsonParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
        }

        @Test
        @DisplayName("Document popular audio CODEC parts")
        void documentPopularAudioCodecs() {
            // CS43L22 - Popular in STM32 discovery boards
            assertTrue(handler.matches("CS43L22-CNZ", ComponentType.IC, registry));
            assertEquals("CS43", handler.extractSeries("CS43L22-CNZ"));
            assertEquals("QFN", handler.extractPackageCode("CS43L22-CNZ"));

            // WM8731 - Classic low-power CODEC
            assertTrue(handler.matches("WM8731SEDS", ComponentType.IC, registry));
            assertEquals("WM87", handler.extractSeries("WM8731SEDS"));
            assertEquals("SOIC", handler.extractPackageCode("WM8731SEDS"));

            // WM8960 - Popular in embedded audio
            assertTrue(handler.matches("WM8960CGEFL", ComponentType.IC, registry));
            assertEquals("WM89", handler.extractSeries("WM8960CGEFL"));
            assertEquals("QFN", handler.extractPackageCode("WM8960CGEFL"));
        }
    }

    @Nested
    @DisplayName("Pattern Coverage")
    class PatternCoverageTests {

        @Test
        @DisplayName("All CS series patterns should be registered")
        void allCSSeriesPatternsRegistered() {
            // CS42xx - Audio ADCs
            assertTrue(registry.matches("CS4270", ComponentType.IC));
            assertTrue(registry.matches("CS4271", ComponentType.IC));
            assertTrue(registry.matches("CS4272", ComponentType.IC));

            // CS43xx - Audio DACs/CODECs
            assertTrue(registry.matches("CS4334", ComponentType.IC));
            assertTrue(registry.matches("CS4344", ComponentType.IC));
            assertTrue(registry.matches("CS43L22", ComponentType.IC));

            // CS47xx - DSP Audio
            assertTrue(registry.matches("CS47L24", ComponentType.IC));
            assertTrue(registry.matches("CS47L35", ComponentType.IC));

            // CS48xx - SoundClear DSP
            assertTrue(registry.matches("CS4860", ComponentType.IC));
            assertTrue(registry.matches("CS48601", ComponentType.IC));

            // CS53xx - LED Drivers
            assertTrue(registry.matches("CS5361", ComponentType.IC));
            assertTrue(registry.matches("CS5368", ComponentType.IC));

            // CS84xx - Digital Audio Interface
            assertTrue(registry.matches("CS8416", ComponentType.IC));
            assertTrue(registry.matches("CS8422", ComponentType.IC));
        }

        @Test
        @DisplayName("All Wolfson patterns should be registered")
        void allWolfsonPatternsRegistered() {
            // WM87xx series
            assertTrue(registry.matches("WM8731", ComponentType.IC));
            assertTrue(registry.matches("WM8750", ComponentType.IC));

            // WM89xx series
            assertTrue(registry.matches("WM8960", ComponentType.IC));
            assertTrue(registry.matches("WM8962", ComponentType.IC));
            assertTrue(registry.matches("WM8994", ComponentType.IC));
        }
    }
}
