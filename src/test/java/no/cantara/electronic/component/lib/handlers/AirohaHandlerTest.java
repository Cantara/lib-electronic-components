package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AirohaHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AirohaHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Airoha Technology (MediaTek subsidiary) Bluetooth audio SoCs.
 *
 * Product Series:
 * - AB155x: TWS earbuds (AB1552, AB1558)
 * - AB156x: ANC earbuds (AB1562, AB1568)
 * - AB157x: Premium audio
 * - AB158x: Ultra-low power
 */
class AirohaHandlerTest {

    private static AirohaHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AirohaHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("AB155x Series Detection (TWS Earbuds)")
    class AB155xSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AB155x series TWS earbuds SoCs")
        @ValueSource(strings = {"AB1552", "AB1553", "AB1555", "AB1558"})
        void shouldDetectAB155xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AB155x with package suffix")
        @ValueSource(strings = {"AB1552-QFN", "AB1558-BGA", "AB1552QFN"})
        void shouldDetectAB155xWithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AB155x with various suffixes")
        @ValueSource(strings = {"AB1552A", "AB1552B", "AB1558M", "AB1558E"})
        void shouldDetectAB155xWithVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("AB156x Series Detection (ANC Earbuds)")
    class AB156xSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AB156x series ANC earbuds SoCs")
        @ValueSource(strings = {"AB1562", "AB1563", "AB1565", "AB1568"})
        void shouldDetectAB156xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AB156x with package suffix")
        @ValueSource(strings = {"AB1562-QFN", "AB1568-BGA", "AB1563-CSP"})
        void shouldDetectAB156xWithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AB156x ANC variants")
        @ValueSource(strings = {"AB1562A", "AB1562M", "AB1568ANC", "AB1568E"})
        void shouldDetectAB156xANCVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("AB157x Series Detection (Premium Audio)")
    class AB157xSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AB157x series premium audio SoCs")
        @ValueSource(strings = {"AB1570", "AB1572", "AB1575", "AB1578"})
        void shouldDetectAB157xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AB157x with package suffix")
        @ValueSource(strings = {"AB1570-QFN", "AB1575-BGA", "AB1578-WLCSP"})
        void shouldDetectAB157xWithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("AB158x Series Detection (Ultra-low Power)")
    class AB158xSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect AB158x series ultra-low power SoCs")
        @ValueSource(strings = {"AB1580", "AB1582", "AB1585", "AB1588"})
        void shouldDetectAB158xSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect AB158x with package suffix")
        @ValueSource(strings = {"AB1580-QFN", "AB1585-CSP", "AB1588-BGA"})
        void shouldDetectAB158xWithPackage(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Non-Matching Patterns")
    class NonMatchingTests {

        @ParameterizedTest
        @DisplayName("Should NOT match non-Airoha patterns")
        @ValueSource(strings = {"AB1540", "AB1599", "AB1600", "AB1490", "QCA6390", "nRF52840"})
        void shouldNotMatchNonAirohaPatterns(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should NOT match IC");
        }

        @ParameterizedTest
        @DisplayName("Should NOT match other AB prefixes outside 155x-158x range")
        @ValueSource(strings = {"AB1000", "AB1400", "AB1500", "AB1540", "AB1590", "AB1600"})
        void shouldNotMatchOutOfRangeSeries(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should NOT match IC (outside AB155x-AB158x range)");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from hyphenated suffix")
        @CsvSource({
                "AB1552-QFN, QFN",
                "AB1562-BGA, BGA",
                "AB1575-CSP, CSP",
                "AB1580-WLCSP, Wafer Level CSP",
                "AB1585-FCBGA, Flip-Chip BGA"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes from compound suffix")
        @CsvSource({
                "AB1552-48QFN, QFN",
                "AB1562-64BGA, BGA",
                "AB1575-32CSP, CSP"
        })
        void shouldExtractPackageFromCompoundSuffix(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract inline package codes")
        @CsvSource({
                "AB1552QFN, QFN",
                "AB1562BGA, BGA",
                "AB1575CSP, CSP"
        })
        void shouldExtractInlinePackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MPN without package suffix")
        void shouldReturnEmptyWithoutSuffix() {
            assertEquals("", handler.extractPackageCode("AB1552"));
            assertEquals("", handler.extractPackageCode("AB1562A"));
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from AB155x")
        @CsvSource({
                "AB1552, AB1552",
                "AB1558, AB1558",
                "AB1552-QFN, AB1552",
                "AB1558A, AB1558"
        })
        void shouldExtractAB155xSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from AB156x")
        @CsvSource({
                "AB1562, AB1562",
                "AB1568, AB1568",
                "AB1562-BGA, AB1562",
                "AB1568ANC, AB1568"
        })
        void shouldExtractAB156xSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from AB157x and AB158x")
        @CsvSource({
                "AB1570, AB1570",
                "AB1575, AB1575",
                "AB1580, AB1580",
                "AB1585-WLCSP, AB1585"
        })
        void shouldExtractPremiumAndUltraLowPowerSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series should be replacements")
        void sameSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("AB1552-QFN", "AB1552-BGA"),
                    "Same series with different packages should be replacements");
        }

        @Test
        @DisplayName("Higher model in same family can replace lower")
        void higherModelCanReplaceLower() {
            // AB1558 (higher) can replace AB1552 (lower) in TWS family
            assertTrue(handler.isOfficialReplacement("AB1558", "AB1552"),
                    "AB1558 should be able to replace AB1552 (higher model)");
        }

        @Test
        @DisplayName("Lower model in same family cannot replace higher")
        void lowerModelCannotReplaceHigher() {
            assertFalse(handler.isOfficialReplacement("AB1552", "AB1558"),
                    "AB1552 should NOT be able to replace AB1558 (lower model)");
        }

        @Test
        @DisplayName("Different families should NOT be replacements")
        void differentFamiliesNotReplacements() {
            // TWS (AB155x) cannot replace ANC (AB156x) - different product categories
            assertFalse(handler.isOfficialReplacement("AB1552", "AB1562"),
                    "AB155x and AB156x are different product families");
            assertFalse(handler.isOfficialReplacement("AB1575", "AB1580"),
                    "AB157x and AB158x are different product families");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "AB1552"));
            assertFalse(handler.isOfficialReplacement("AB1552", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC component type")
        void shouldSupportICType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.MICROCONTROLLER);
            }, "Set should be immutable (using Set.of())");
        }

        @Test
        @DisplayName("getSupportedTypes should not have duplicates")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(1, types.size(), "Should have exactly 1 type");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully in matches")
        void shouldHandleNullMpnInMatches() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully in matches")
        void shouldHandleEmptyMpnInMatches() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should handle null MPN gracefully in extractPackageCode")
        void shouldHandleNullMpnInExtractPackageCode() {
            assertEquals("", handler.extractPackageCode(null));
        }

        @Test
        @DisplayName("Should handle null MPN gracefully in extractSeries")
        void shouldHandleNullMpnInExtractSeries() {
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle case insensitivity")
        void shouldHandleCaseInsensitivity() {
            // PatternRegistry uses Pattern.CASE_INSENSITIVE by design
            // Both uppercase and lowercase should match
            assertTrue(handler.matches("AB1552", ComponentType.IC, registry),
                    "Uppercase AB1552 should match");
            assertTrue(handler.matches("ab1552", ComponentType.IC, registry),
                    "Lowercase ab1552 should match (case-insensitive patterns)");
            assertTrue(handler.matches("Ab1552", ComponentType.IC, registry),
                    "Mixed case Ab1552 should match");
        }

        @ParameterizedTest
        @DisplayName("Should handle various MPN formats")
        @ValueSource(strings = {
                "AB1552",
                "AB1552A",
                "AB1552-QFN",
                "AB1552-48-QFN",
                "AB1552QFN48"
        })
        void shouldHandleVariousMPNFormats(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            AirohaHandler directHandler = new AirohaHandler();
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

        @Test
        @DisplayName("Multiple pattern registrations should not cause issues")
        void multiplePatternRegistrationsShouldWork() {
            AirohaHandler newHandler = new AirohaHandler();
            PatternRegistry newRegistry = new PatternRegistry();

            // Register patterns multiple times (shouldn't cause issues)
            newHandler.initializePatterns(newRegistry);
            newHandler.initializePatterns(newRegistry);

            // Should still work correctly
            assertTrue(newHandler.matches("AB1552", ComponentType.IC, newRegistry));
        }
    }

    @Nested
    @DisplayName("Real World MPN Examples")
    class RealWorldExamples {

        @ParameterizedTest
        @DisplayName("Should match typical Airoha TWS part numbers")
        @ValueSource(strings = {
                "AB1552",       // Basic TWS
                "AB1552A",      // Variant A
                "AB1558",       // Enhanced TWS
                "AB1558M"       // Module variant
        })
        void shouldMatchTypicalTWSParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Real-world TWS part " + mpn + " should match");
        }

        @ParameterizedTest
        @DisplayName("Should match typical Airoha ANC part numbers")
        @ValueSource(strings = {
                "AB1562",       // Basic ANC
                "AB1562A",      // Variant A
                "AB1568",       // Enhanced ANC
                "AB1568E"       // Enhanced variant
        })
        void shouldMatchTypicalANCParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    "Real-world ANC part " + mpn + " should match");
        }
    }

    @Nested
    @DisplayName("Bluetooth Version Inference")
    class BluetoothVersionTests {

        @Test
        @DisplayName("AB155x series should infer BT 5.0")
        void ab155xShouldInferBT50() {
            // This tests the internal Bluetooth version inference used in replacement detection
            // AB155x (basic TWS) should be BT 5.0
            // AB156x+ should be BT 5.2+
            // Therefore AB1562 should NOT be replaceable by AB1552 (BT version downgrade)
            assertFalse(handler.isOfficialReplacement("AB1552", "AB1562"),
                    "Lower BT version cannot replace higher");
        }

        @Test
        @DisplayName("Higher series should have higher BT version")
        void higherSeriesShouldHaveHigherBTVersion() {
            // AB158x (ultra-low power, latest) has highest BT version
            // Cannot be replaced by AB155x
            assertFalse(handler.isOfficialReplacement("AB1552", "AB1580"),
                    "AB155x cannot replace AB158x (BT version mismatch)");
        }
    }
}
