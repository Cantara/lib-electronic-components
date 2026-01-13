package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.CMediaHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CMediaHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * C-Media Electronics Product Categories:
 * - CM108 series: USB Audio Codec (CM108, CM108AH, CM108B)
 * - CM109 series: USB Audio with HID keyboard support
 * - CM119 series: USB 7.1 Channel Audio (CM119, CM119A, CM119B)
 * - CM6xxx series: Professional USB Audio (CM6206, CM6631, CM6632)
 * - CMI87xx series: HD Audio Codec (CMI8738, CMI8768, CMI8788)
 * - CMI83xx series: Legacy PCI Audio (CMI8330, CMI8338)
 *
 * Package Codes:
 * - QFP: Quad Flat Package
 * - LQFP: Low-profile Quad Flat Package
 * - QFN: Quad Flat No-lead
 * - SSOP: Shrink Small Outline Package
 */
class CMediaHandlerTest {

    private static CMediaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new CMediaHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("CM108 Series Detection (USB Audio Codec)")
    class CM108SeriesTests {

        @ParameterizedTest
        @DisplayName("CM108 USB audio codecs should match IC type")
        @ValueSource(strings = {"CM108", "CM108AH", "CM108B", "CM108AH-QFP48", "CM108B-LQFP"})
        void cm108ShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("CM108 should be detected via registry")
        @ValueSource(strings = {"CM108", "CM108AH", "CM108B"})
        void cm108ShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry");
        }

        @Test
        @DisplayName("CM108AH variants should be detected")
        void cm108AHVariantsShouldBeDetected() {
            assertTrue(handler.matches("CM108AH", ComponentType.IC, registry));
            assertTrue(handler.matches("CM108AH-QFP48", ComponentType.IC, registry));
            assertEquals("CM108", handler.extractSeries("CM108AH"));
        }

        @Test
        @DisplayName("CM108B is a common variant")
        void cm108BShouldBeDetected() {
            assertTrue(handler.matches("CM108B", ComponentType.IC, registry));
            assertEquals("CM108", handler.extractSeries("CM108B"));
        }
    }

    @Nested
    @DisplayName("CM109 Series Detection (USB Audio with HID)")
    class CM109SeriesTests {

        @ParameterizedTest
        @DisplayName("CM109 USB audio with HID should match IC type")
        @ValueSource(strings = {"CM109", "CM109A", "CM109B", "CM109-QFP"})
        void cm109ShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("CM109 series should be detected")
        void cm109SeriesShouldBeDetected() {
            assertTrue(handler.matches("CM109", ComponentType.IC, registry));
            assertEquals("CM109", handler.extractSeries("CM109"));
        }
    }

    @Nested
    @DisplayName("CM119 Series Detection (USB 7.1 Audio)")
    class CM119SeriesTests {

        @ParameterizedTest
        @DisplayName("CM119 USB 7.1 audio should match IC type")
        @ValueSource(strings = {"CM119", "CM119A", "CM119B", "CM119-LQFP48"})
        void cm119ShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("CM119 should be detected via registry")
        @ValueSource(strings = {"CM119", "CM119A", "CM119B"})
        void cm119ShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry");
        }

        @Test
        @DisplayName("CM119A 7.1 channel audio should be detected")
        void cm119AShouldBeDetected() {
            assertTrue(handler.matches("CM119A", ComponentType.IC, registry));
            assertEquals("CM119", handler.extractSeries("CM119A"));
        }
    }

    @Nested
    @DisplayName("CM6xxx Series Detection (Professional USB Audio)")
    class CM6xxxSeriesTests {

        @ParameterizedTest
        @DisplayName("CM6xxx professional audio should match IC type")
        @ValueSource(strings = {"CM6206", "CM6206-LQ", "CM6400", "CM6502", "CM6631", "CM6631A", "CM6632"})
        void cm6xxxShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("CM6xxx should be detected via registry")
        @ValueSource(strings = {"CM6206", "CM6400", "CM6631", "CM6632"})
        void cm6xxxShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry");
        }

        @Test
        @DisplayName("CM6206 is a popular USB audio IC")
        void cm6206ShouldBeDetected() {
            assertTrue(handler.matches("CM6206", ComponentType.IC, registry));
            assertTrue(handler.matches("CM6206-LQ", ComponentType.IC, registry));
            assertEquals("CM6206", handler.extractSeries("CM6206"));
        }

        @Test
        @DisplayName("CM6631 is a high-end USB audio processor")
        void cm6631ShouldBeDetected() {
            assertTrue(handler.matches("CM6631", ComponentType.IC, registry));
            assertTrue(handler.matches("CM6631A", ComponentType.IC, registry));
            assertEquals("CM6631", handler.extractSeries("CM6631A"));
        }

        @Test
        @DisplayName("CM6632 is a USB audio DAC")
        void cm6632ShouldBeDetected() {
            assertTrue(handler.matches("CM6632", ComponentType.IC, registry));
            assertEquals("CM6632", handler.extractSeries("CM6632"));
        }
    }

    @Nested
    @DisplayName("CMI87xx Series Detection (HD Audio Codec)")
    class CMI87xxSeriesTests {

        @ParameterizedTest
        @DisplayName("CMI87xx HD audio codecs should match IC type")
        @ValueSource(strings = {"CMI8738", "CMI8738-MX", "CMI8768", "CMI8768-8", "CMI8788", "CMI8788-LX"})
        void cmi87xxShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("CMI87xx should be detected via registry")
        @ValueSource(strings = {"CMI8738", "CMI8768", "CMI8788"})
        void cmi87xxShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry");
        }

        @Test
        @DisplayName("CMI8738 is a popular PCI audio chip")
        void cmi8738ShouldBeDetected() {
            assertTrue(handler.matches("CMI8738", ComponentType.IC, registry));
            assertEquals("CMI8738", handler.extractSeries("CMI8738"));
        }

        @Test
        @DisplayName("CMI8768 is 8-channel HD audio")
        void cmi8768ShouldBeDetected() {
            assertTrue(handler.matches("CMI8768", ComponentType.IC, registry));
            assertEquals("CMI8768", handler.extractSeries("CMI8768-8"));
        }

        @Test
        @DisplayName("CMI8788 (Oxygen HD Audio) should be detected")
        void cmi8788ShouldBeDetected() {
            assertTrue(handler.matches("CMI8788", ComponentType.IC, registry));
            assertTrue(handler.matches("CMI8788-LX", ComponentType.IC, registry));
            assertEquals("CMI8788", handler.extractSeries("CMI8788"));
        }
    }

    @Nested
    @DisplayName("CMI83xx Series Detection (Legacy PCI Audio)")
    class CMI83xxSeriesTests {

        @ParameterizedTest
        @DisplayName("CMI83xx legacy audio should match IC type")
        @ValueSource(strings = {"CMI8330", "CMI8338"})
        void cmi83xxShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("CMI8330 legacy audio should be detected")
        void cmi8330ShouldBeDetected() {
            assertTrue(handler.matches("CMI8330", ComponentType.IC, registry));
            assertEquals("CMI8330", handler.extractSeries("CMI8330"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract hyphenated package codes")
        @CsvSource({
                "CM108AH-QFP48, QFP",
                "CM108B-LQFP, LQFP",
                "CM6206-LQ, LQFP",
                "CM119-QFN, QFN",
                "CM6631-SSOP, SSOP"
        })
        void shouldExtractHyphenatedPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle parts without package suffix")
        void shouldHandlePartsWithoutPackageSuffix() {
            String result = handler.extractPackageCode("CM108AH");
            assertNotNull(result);
            // No package code expected for simple part numbers
        }

        @Test
        @DisplayName("Should handle hyphenated suffixes")
        void shouldHandleHyphenatedSuffixes() {
            assertEquals("QFP", handler.extractPackageCode("CM108-QFP48"));
            assertEquals("LQFP", handler.extractPackageCode("CM6206-LQFP48"));
        }

        @Test
        @DisplayName("Should handle lowercase input")
        void shouldHandleLowercaseInput() {
            assertEquals("QFP", handler.extractPackageCode("cm108ah-qfp48"));
            assertEquals("LQFP", handler.extractPackageCode("cm6206-lq"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract CM10x series correctly")
        @CsvSource({
                "CM108, CM108",
                "CM108AH, CM108",
                "CM108B-QFP, CM108",
                "CM109, CM109",
                "CM109A, CM109"
        })
        void shouldExtractCM10xSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CM119 series correctly")
        @CsvSource({
                "CM119, CM119",
                "CM119A, CM119",
                "CM119B-LQFP, CM119"
        })
        void shouldExtractCM119Series(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CM6xxx series correctly")
        @CsvSource({
                "CM6206, CM6206",
                "CM6206-LQ, CM6206",
                "CM6400, CM6400",
                "CM6631, CM6631",
                "CM6631A, CM6631",
                "CM6632, CM6632"
        })
        void shouldExtractCM6xxxSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CMI87xx series correctly")
        @CsvSource({
                "CMI8738, CMI8738",
                "CMI8738-MX, CMI8738",
                "CMI8768, CMI8768",
                "CMI8788, CMI8788",
                "CMI8788-LX, CMI8788"
        })
        void shouldExtractCMI87xxSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CMI83xx series correctly")
        @CsvSource({
                "CMI8330, CMI8330",
                "CMI8338, CMI8338"
        })
        void shouldExtractCMI83xxSeries(String mpn, String expectedSeries) {
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
            assertTrue(handler.isOfficialReplacement("CM108AH-QFP48", "CM108AH-LQFP"),
                    "Same part with different packages should be replacements");
        }

        @Test
        @DisplayName("Same base part should be replacement")
        void sameBasePartAreReplacements() {
            assertTrue(handler.isOfficialReplacement("CM6206", "CM6206-LQ"),
                    "Part with and without package suffix should be replacements");
        }

        @Test
        @DisplayName("Different variants in same series should NOT be replacements")
        void differentVariantsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CM108AH", "CM108B"),
                    "Different variants (AH vs B) should not be automatic replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CM108AH", "CM119A"),
                    "Different series should not be replacements");
            assertFalse(handler.isOfficialReplacement("CM6206", "CM6631"),
                    "Different CM6xxx parts should not be replacements");
            assertFalse(handler.isOfficialReplacement("CMI8738", "CMI8788"),
                    "Different CMI87xx parts should not be replacements");
        }

        @Test
        @DisplayName("USB vs HD Audio should NOT be replacements")
        void usbVsHdAudioNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CM108AH", "CMI8788"),
                    "USB audio and HD audio should not be replacements");
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

        @Test
        @DisplayName("Should have exactly one type (IC)")
        void shouldHaveExactlyOneType() {
            var types = handler.getSupportedTypes();
            assertEquals(1, types.size(), "Should have exactly 1 type");
            assertTrue(types.contains(ComponentType.IC));
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
            assertFalse(handler.isOfficialReplacement(null, "CM108AH"));
            assertFalse(handler.isOfficialReplacement("CM108AH", null));
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
            assertFalse(handler.matches("CM108AH", null, registry));
        }

        @Test
        @DisplayName("Case insensitive matching via handler")
        void caseInsensitiveViaHandler() {
            assertTrue(handler.matches("cm108ah", ComponentType.IC, registry));
            assertTrue(handler.matches("CM108AH", ComponentType.IC, registry));
            assertTrue(handler.matches("cmi8788", ComponentType.IC, registry));
            assertTrue(handler.matches("CMI8788", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Case insensitive matching via registry")
        void caseInsensitiveViaRegistry() {
            assertTrue(registry.matches("cm108", ComponentType.IC));
            assertTrue(registry.matches("CM108", ComponentType.IC));
            assertTrue(registry.matches("cm6206", ComponentType.IC));
            assertTrue(registry.matches("CM6206", ComponentType.IC));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            CMediaHandler directHandler = new CMediaHandler();
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
        @DisplayName("Common USB Audio ICs")
        @ValueSource(strings = {
                "CM108AH",        // Basic USB audio codec
                "CM108B",         // Popular USB audio codec variant
                "CM119A",         // USB 7.1 channel audio
                "CM6206",         // USB 2.0 audio controller
                "CM6631A"         // Professional USB audio processor
        })
        void commonUSBAudioParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
        }

        @ParameterizedTest
        @DisplayName("Common HD Audio ICs")
        @ValueSource(strings = {
                "CMI8738",        // PCI audio controller
                "CMI8768",        // 8-channel PCI audio
                "CMI8788"         // Oxygen HD Audio chip
        })
        void commonHDAudioParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
        }

        @Test
        @DisplayName("Document popular USB audio parts")
        void documentPopularUSBAudioParts() {
            // CM108AH - Very popular in USB sound cards
            assertTrue(handler.matches("CM108AH", ComponentType.IC, registry));
            assertEquals("CM108", handler.extractSeries("CM108AH"));

            // CM6206 - Common in USB audio devices
            assertTrue(handler.matches("CM6206", ComponentType.IC, registry));
            assertEquals("CM6206", handler.extractSeries("CM6206"));

            // CMI8788 - Used in enthusiast sound cards (ASUS Xonar, etc.)
            assertTrue(handler.matches("CMI8788", ComponentType.IC, registry));
            assertEquals("CMI8788", handler.extractSeries("CMI8788"));
        }
    }

    @Nested
    @DisplayName("Pattern Coverage")
    class PatternCoverageTests {

        @Test
        @DisplayName("All CM108/CM109 patterns should be registered")
        void allCM10xPatternsRegistered() {
            assertTrue(registry.matches("CM108", ComponentType.IC));
            assertTrue(registry.matches("CM108AH", ComponentType.IC));
            assertTrue(registry.matches("CM108B", ComponentType.IC));
            assertTrue(registry.matches("CM109", ComponentType.IC));
            assertTrue(registry.matches("CM109A", ComponentType.IC));
        }

        @Test
        @DisplayName("All CM119 patterns should be registered")
        void allCM119PatternsRegistered() {
            assertTrue(registry.matches("CM119", ComponentType.IC));
            assertTrue(registry.matches("CM119A", ComponentType.IC));
            assertTrue(registry.matches("CM119B", ComponentType.IC));
        }

        @Test
        @DisplayName("All CM6xxx patterns should be registered")
        void allCM6xxxPatternsRegistered() {
            assertTrue(registry.matches("CM6206", ComponentType.IC));
            assertTrue(registry.matches("CM6400", ComponentType.IC));
            assertTrue(registry.matches("CM6502", ComponentType.IC));
            assertTrue(registry.matches("CM6631", ComponentType.IC));
            assertTrue(registry.matches("CM6632", ComponentType.IC));
        }

        @Test
        @DisplayName("All CMI87xx patterns should be registered")
        void allCMI87xxPatternsRegistered() {
            assertTrue(registry.matches("CMI8738", ComponentType.IC));
            assertTrue(registry.matches("CMI8768", ComponentType.IC));
            assertTrue(registry.matches("CMI8788", ComponentType.IC));
        }

        @Test
        @DisplayName("All CMI83xx patterns should be registered")
        void allCMI83xxPatternsRegistered() {
            assertTrue(registry.matches("CMI8330", ComponentType.IC));
            assertTrue(registry.matches("CMI8338", ComponentType.IC));
        }
    }

    @Nested
    @DisplayName("Non-Matching Parts")
    class NonMatchingTests {

        @ParameterizedTest
        @DisplayName("Similar but non-C-Media parts should NOT match")
        @ValueSource(strings = {
                "CM100",          // Not a valid C-Media part
                "CM200",          // Not a valid series
                "CMI9000",        // Invalid CMI series
                "CS4344",         // Cirrus Logic part
                "WM8731",         // Wolfson part
                "PCM2704"         // TI USB audio
        })
        void nonCMediaPartsShouldNotMatch(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should NOT match C-Media handler");
        }
    }
}
