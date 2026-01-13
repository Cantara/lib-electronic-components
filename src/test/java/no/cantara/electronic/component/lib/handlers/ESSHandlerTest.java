package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ESSHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ESSHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * ESS Technology Product Categories:
 * - ES9038 Series: Flagship Sabre DACs (8-channel and 2-channel)
 *   - ES9038PRO: Flagship 8-channel DAC
 *   - ES9038Q2M: 2-channel mobile version
 * - ES9028 Series: High-end Sabre DACs
 *   - ES9028PRO: 8-channel high-performance
 *   - ES9028Q2M: 2-channel version
 * - ES9018 Series: Classic Sabre DACs
 *   - ES9018S: Reference stereo DAC
 *   - ES9018K2M: Mobile 2-channel
 * - ES92xx Series: Portable DACs with headphone amps
 *   - ES9218P: Portable with HiFi headphone amp
 *   - ES9219: Newest generation portable
 *   - ES9281: USB DAC
 *
 * Package Codes:
 * - QFN: Quad Flat No-Lead
 * - TQFP: Thin Quad Flat Pack
 * - WLCSP: Wafer Level Chip Scale Package
 */
class ESSHandlerTest {

    private static ESSHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ESSHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("ES9038 Series Detection (Flagship)")
    class ES9038SeriesTests {

        @ParameterizedTest
        @DisplayName("ES9038 flagship DACs should match IC type")
        @ValueSource(strings = {
                "ES9038PRO",
                "ES9038Q2M",
                "ES9038PRO-QFN",
                "ES9038Q2M-QFN",
                "ES9038S"
        })
        void es9038ShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("ES9038PRO is the flagship 8-channel DAC")
        void es9038ProShouldBeDetected() {
            assertTrue(handler.matches("ES9038PRO", ComponentType.IC, registry));
            assertEquals("ES9038", handler.extractSeries("ES9038PRO"));
        }

        @Test
        @DisplayName("ES9038Q2M is the 2-channel mobile variant")
        void es9038Q2MShouldBeDetected() {
            assertTrue(handler.matches("ES9038Q2M", ComponentType.IC, registry));
            assertEquals("ES9038", handler.extractSeries("ES9038Q2M"));
        }

        @ParameterizedTest
        @DisplayName("ES9038 variants detected via registry")
        @ValueSource(strings = {"ES9038PRO", "ES9038Q2M", "ES9038S"})
        void es9038ShouldMatchViaRegistry(String mpn) {
            assertTrue(registry.matches(mpn, ComponentType.IC),
                    mpn + " should match IC via registry");
        }
    }

    @Nested
    @DisplayName("ES9028 Series Detection (High-End)")
    class ES9028SeriesTests {

        @ParameterizedTest
        @DisplayName("ES9028 high-end DACs should match IC type")
        @ValueSource(strings = {
                "ES9028PRO",
                "ES9028Q2M",
                "ES9028PRO-QFN",
                "ES9028Q2M-QFN"
        })
        void es9028ShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("ES9028PRO is a high-performance 8-channel DAC")
        void es9028ProShouldBeDetected() {
            assertTrue(handler.matches("ES9028PRO", ComponentType.IC, registry));
            assertEquals("ES9028", handler.extractSeries("ES9028PRO"));
        }

        @Test
        @DisplayName("ES9028Q2M is the 2-channel version")
        void es9028Q2MShouldBeDetected() {
            assertTrue(handler.matches("ES9028Q2M", ComponentType.IC, registry));
            assertEquals("ES9028", handler.extractSeries("ES9028Q2M"));
        }
    }

    @Nested
    @DisplayName("ES9018 Series Detection (Classic Sabre)")
    class ES9018SeriesTests {

        @ParameterizedTest
        @DisplayName("ES9018 classic DACs should match IC type")
        @ValueSource(strings = {
                "ES9018",
                "ES9018S",
                "ES9018K2M",
                "ES9018-QFN",
                "ES9018S2M"
        })
        void es9018ShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("ES9018S is the reference stereo DAC")
        void es9018SShouldBeDetected() {
            assertTrue(handler.matches("ES9018S", ComponentType.IC, registry));
            assertEquals("ES9018", handler.extractSeries("ES9018S"));
        }

        @Test
        @DisplayName("ES9018K2M is the mobile 2-channel DAC")
        void es9018K2MShouldBeDetected() {
            assertTrue(handler.matches("ES9018K2M", ComponentType.IC, registry));
            assertEquals("ES9018", handler.extractSeries("ES9018K2M"));
        }
    }

    @Nested
    @DisplayName("ES92xx Series Detection (Portable)")
    class ES92xxSeriesTests {

        @ParameterizedTest
        @DisplayName("ES92xx portable DACs should match IC type")
        @ValueSource(strings = {
                "ES9218P",
                "ES9218",
                "ES9219",
                "ES9219C",
                "ES9281",
                "ES9280",
                "ES9218P-QFNR-48"
        })
        void es92xxShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("ES9218P is the popular portable DAC with headphone amp")
        void es9218PShouldBeDetected() {
            assertTrue(handler.matches("ES9218P", ComponentType.IC, registry));
            assertEquals("ES9218", handler.extractSeries("ES9218P"));
        }

        @Test
        @DisplayName("ES9219 is the newest portable DAC")
        void es9219ShouldBeDetected() {
            assertTrue(handler.matches("ES9219", ComponentType.IC, registry));
            assertTrue(handler.matches("ES9219C", ComponentType.IC, registry));
            assertEquals("ES9219", handler.extractSeries("ES9219"));
            assertEquals("ES9219", handler.extractSeries("ES9219C"));
        }

        @Test
        @DisplayName("ES9281 USB DAC should be detected")
        void es9281ShouldBeDetected() {
            assertTrue(handler.matches("ES9281", ComponentType.IC, registry));
            assertEquals("ES9281", handler.extractSeries("ES9281"));
        }
    }

    @Nested
    @DisplayName("Mid-Range DAC Series")
    class MidRangeSeriesTests {

        @ParameterizedTest
        @DisplayName("ES901x mid-range DACs should match")
        @ValueSource(strings = {"ES9010", "ES9011", "ES9012", "ES9016"})
        void es901xShouldMatch(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("ES902x mid-range DACs should match")
        @ValueSource(strings = {"ES9020", "ES9021", "ES9023"})
        void es902xShouldMatch(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("ES9023 is a popular budget DAC")
        void es9023ShouldBeDetected() {
            assertTrue(handler.matches("ES9023", ComponentType.IC, registry));
            assertEquals("ES9023", handler.extractSeries("ES9023"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from hyphenated MPNs")
        @CsvSource({
                "ES9038PRO-QFN, QFN",
                "ES9038Q2M-TQFP, TQFP",
                "ES9218P-WLCSP, WLCSP",
                "ES9028PRO-QFN-64, QFN",
                "ES9218P-QFNR-48, QFN"
        })
        void shouldExtractHyphenatedPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle parts without package suffix")
        void shouldHandlePartsWithoutPackageSuffix() {
            String result = handler.extractPackageCode("ES9038PRO");
            assertNotNull(result);
            // ES9038PRO without package suffix returns empty string
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should return empty for null input")
        void shouldReturnEmptyForNull() {
            assertEquals("", handler.extractPackageCode(null));
        }

        @Test
        @DisplayName("Should return empty for empty input")
        void shouldReturnEmptyForEmpty() {
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series correctly for flagship DACs")
        @CsvSource({
                "ES9038PRO, ES9038",
                "ES9038Q2M, ES9038",
                "ES9038PRO-QFN, ES9038",
                "ES9028PRO, ES9028",
                "ES9028Q2M, ES9028",
                "ES9018S, ES9018",
                "ES9018K2M, ES9018"
        })
        void shouldExtractFlagshipSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series correctly for portable DACs")
        @CsvSource({
                "ES9218P, ES9218",
                "ES9218P-QFNR-48, ES9218",
                "ES9219, ES9219",
                "ES9219C, ES9219",
                "ES9281, ES9281"
        })
        void shouldExtractPortableSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid input")
        void shouldReturnEmptyForInvalidInput() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("XYZ123"));
        }

        @Test
        @DisplayName("Should return empty for non-ESS parts")
        void shouldReturnEmptyForNonESSParts() {
            assertEquals("", handler.extractSeries("AK4490"));
            assertEquals("", handler.extractSeries("PCM5122"));
            assertEquals("", handler.extractSeries("CS4344"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same part different packages should be replacements")
        void samePartDifferentPackagesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("ES9038PRO", "ES9038PRO-QFN"),
                    "Same part with and without package suffix should be replacements");
            assertTrue(handler.isOfficialReplacement("ES9218P", "ES9218P-QFNR-48"),
                    "Same part with different package variants should be replacements");
        }

        @Test
        @DisplayName("Different channel count variants should NOT be replacements")
        void differentChannelCountsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ES9038PRO", "ES9038Q2M"),
                    "8-channel and 2-channel variants should not be replacements");
            assertFalse(handler.isOfficialReplacement("ES9028PRO", "ES9028Q2M"),
                    "Different channel counts are not interchangeable");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ES9038PRO", "ES9028PRO"),
                    "Different series should not be replacements");
            assertFalse(handler.isOfficialReplacement("ES9038PRO", "ES9018S"),
                    "Different generations should not be replacements");
            assertFalse(handler.isOfficialReplacement("ES9218P", "ES9219"),
                    "Different portable DACs should not be replacements");
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "ES9038PRO"));
            assertFalse(handler.isOfficialReplacement("ES9038PRO", null));
            assertFalse(handler.isOfficialReplacement(null, null));
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
            assertEquals(1, types.size(), "Should have exactly one type");
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
            assertFalse(handler.isOfficialReplacement(null, "ES9038PRO"));
            assertFalse(handler.isOfficialReplacement("ES9038PRO", null));
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
            assertFalse(handler.matches("ES9038PRO", null, registry));
        }

        @Test
        @DisplayName("Case insensitive matching via handler")
        void caseInsensitiveViaHandler() {
            assertTrue(handler.matches("es9038pro", ComponentType.IC, registry));
            assertTrue(handler.matches("ES9038PRO", ComponentType.IC, registry));
            assertTrue(handler.matches("Es9038Pro", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Case insensitive matching via registry")
        void caseInsensitiveViaRegistry() {
            assertTrue(registry.matches("es9038pro", ComponentType.IC));
            assertTrue(registry.matches("ES9038PRO", ComponentType.IC));
            assertTrue(registry.matches("es9218p", ComponentType.IC));
            assertTrue(registry.matches("ES9218P", ComponentType.IC));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            ESSHandler directHandler = new ESSHandler();
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
        @DisplayName("Popular audiophile DACs")
        @ValueSource(strings = {
                "ES9038PRO",      // Flagship 8-channel, -140dB THD+N
                "ES9038Q2M",      // Mobile flagship, 2-channel
                "ES9028PRO",      // High-end 8-channel
                "ES9018S",        // Classic reference DAC
                "ES9018K2M",      // Classic mobile DAC
                "ES9218P",        // Portable with headphone amp
                "ES9219",         // Latest portable DAC
                "ES9023"          // Budget stereo DAC
        })
        void popularAudiophileDACs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
            assertTrue(series.startsWith("ES"),
                    mpn + " series should start with ES");
        }

        @Test
        @DisplayName("Document flagship ES9038PRO specifications")
        void documentFlagshipDAC() {
            // ES9038PRO - The flagship Sabre DAC
            // 8 channels, 32-bit, -140dB THD+N
            assertTrue(handler.matches("ES9038PRO", ComponentType.IC, registry));
            assertEquals("ES9038", handler.extractSeries("ES9038PRO"));
        }

        @Test
        @DisplayName("Document popular portable ES9218P specifications")
        void documentPortableDAC() {
            // ES9218P - Popular portable DAC with integrated headphone amp
            // Used in many high-end smartphones and portable audio players
            assertTrue(handler.matches("ES9218P", ComponentType.IC, registry));
            assertEquals("ES9218", handler.extractSeries("ES9218P"));
        }

        @Test
        @DisplayName("ES9038 variants should all be detected")
        void es9038VariantsShouldBeDetected() {
            // All variants of the flagship ES9038 series
            assertTrue(handler.matches("ES9038PRO", ComponentType.IC, registry));
            assertTrue(handler.matches("ES9038Q2M", ComponentType.IC, registry));
            assertTrue(handler.matches("ES9038S", ComponentType.IC, registry));

            // All should have the same series
            assertEquals("ES9038", handler.extractSeries("ES9038PRO"));
            assertEquals("ES9038", handler.extractSeries("ES9038Q2M"));
            assertEquals("ES9038", handler.extractSeries("ES9038S"));
        }
    }

    @Nested
    @DisplayName("Pattern Coverage")
    class PatternCoverageTests {

        @Test
        @DisplayName("All ES9038 patterns should be registered")
        void allES9038PatternsRegistered() {
            assertTrue(registry.matches("ES9038PRO", ComponentType.IC));
            assertTrue(registry.matches("ES9038Q2M", ComponentType.IC));
            assertTrue(registry.matches("ES9038S", ComponentType.IC));
        }

        @Test
        @DisplayName("All ES9028 patterns should be registered")
        void allES9028PatternsRegistered() {
            assertTrue(registry.matches("ES9028PRO", ComponentType.IC));
            assertTrue(registry.matches("ES9028Q2M", ComponentType.IC));
        }

        @Test
        @DisplayName("All ES9018 patterns should be registered")
        void allES9018PatternsRegistered() {
            assertTrue(registry.matches("ES9018S", ComponentType.IC));
            assertTrue(registry.matches("ES9018K2M", ComponentType.IC));
            assertTrue(registry.matches("ES9018", ComponentType.IC));
        }

        @Test
        @DisplayName("All ES92xx portable patterns should be registered")
        void allES92xxPatternsRegistered() {
            assertTrue(registry.matches("ES9218P", ComponentType.IC));
            assertTrue(registry.matches("ES9219", ComponentType.IC));
            assertTrue(registry.matches("ES9219C", ComponentType.IC));
            assertTrue(registry.matches("ES9280", ComponentType.IC));
            assertTrue(registry.matches("ES9281", ComponentType.IC));
        }

        @Test
        @DisplayName("Mid-range DAC patterns should be registered")
        void midRangePatternsRegistered() {
            assertTrue(registry.matches("ES9010", ComponentType.IC));
            assertTrue(registry.matches("ES9016", ComponentType.IC));
            assertTrue(registry.matches("ES9023", ComponentType.IC));
        }
    }

    @Nested
    @DisplayName("Non-ESS Parts Should Not Match")
    class NonESSPartsTests {

        @ParameterizedTest
        @DisplayName("Competing DAC brands should not match")
        @ValueSource(strings = {
                "AK4490",         // AKM DAC
                "AK4499",         // AKM flagship
                "PCM5122",        // TI DAC
                "PCM1794",        // TI flagship
                "CS4344",         // Cirrus Logic DAC
                "CS43L22",        // Cirrus Logic CODEC
                "WM8731"          // Wolfson/Cirrus CODEC
        })
        void competingBrandsShouldNotMatch(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " (non-ESS) should not match ESS handler");
        }

        @Test
        @DisplayName("Random strings should not match")
        void randomStringsShouldNotMatch() {
            assertFalse(handler.matches("ABCD1234", ComponentType.IC, registry));
            assertFalse(handler.matches("12345", ComponentType.IC, registry));
            assertFalse(handler.matches("ES", ComponentType.IC, registry));
        }
    }
}
