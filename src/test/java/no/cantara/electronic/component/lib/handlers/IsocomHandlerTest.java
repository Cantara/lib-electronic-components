package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.IsocomHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for IsocomHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 * <p>
 * Isocom is a manufacturer of optocouplers and solid state relays with these key product lines:
 * - ISP series: Phototransistor output (ISP817, ISP827, ISP847)
 * - ISQ series: High CTR optocouplers (ISQ817, ISQ827)
 * - ISD series: Darlington output (ISD817, ISD827)
 * - 4N series: Standard optocouplers (4N25, 4N26, 4N35, 4N36)
 * - 6N series: Logic output (6N135, 6N136, 6N137, 6N138)
 * - MOC series: Triac/SCR drivers (MOC3020, MOC3021, MOC3041, MOC3042)
 * - TLP series: Toshiba-compatible (TLP521, TLP621)
 */
class IsocomHandlerTest {

    private static IsocomHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new IsocomHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("ISP Series (Phototransistor Output)")
    class ISPSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect ISP series optocouplers")
        @ValueSource(strings = {
            "ISP817",
            "ISP817A",
            "ISP817B",
            "ISP817C",
            "ISP817D",
            "ISP817-1",
            "ISP817-2",
            "ISP817-4",
            "ISP827",
            "ISP827-1",
            "ISP847",
            "ISP847-4"
        })
        void shouldDetectISPSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should extract ISP series correctly")
        @ValueSource(strings = {"ISP817", "ISP817A", "ISP817-1", "ISP827", "ISP847"})
        void shouldExtractISPSeries(String mpn) {
            assertEquals("ISP", handler.extractSeries(mpn),
                    "Series for " + mpn + " should be ISP");
        }
    }

    @Nested
    @DisplayName("ISQ Series (High CTR)")
    class ISQSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect ISQ series optocouplers")
        @ValueSource(strings = {
            "ISQ817",
            "ISQ817A",
            "ISQ817-1",
            "ISQ827",
            "ISQ827-2"
        })
        void shouldDetectISQSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("Should extract ISQ series correctly")
        void shouldExtractISQSeries() {
            assertEquals("ISQ", handler.extractSeries("ISQ817"));
            assertEquals("ISQ", handler.extractSeries("ISQ817A"));
            assertEquals("ISQ", handler.extractSeries("ISQ827-1"));
        }
    }

    @Nested
    @DisplayName("ISD Series (Darlington Output)")
    class ISDSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect ISD series optocouplers")
        @ValueSource(strings = {
            "ISD817",
            "ISD817A",
            "ISD817-1",
            "ISD827",
            "ISD827-4"
        })
        void shouldDetectISDSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("Should extract ISD series correctly")
        void shouldExtractISDSeries() {
            assertEquals("ISD", handler.extractSeries("ISD817"));
            assertEquals("ISD", handler.extractSeries("ISD827-1"));
        }
    }

    @Nested
    @DisplayName("4N Series (Standard Optocouplers)")
    class FourNSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect 4N series optocouplers")
        @ValueSource(strings = {
            "4N25",
            "4N26",
            "4N27",
            "4N28",
            "4N35",
            "4N36",
            "4N37"
        })
        void shouldDetect4NSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should extract 4N series correctly")
        @CsvSource({
            "4N25, 4N25",
            "4N26, 4N26",
            "4N27, 4N27",
            "4N28, 4N28",
            "4N35, 4N35",
            "4N36, 4N36",
            "4N37, 4N37"
        })
        void shouldExtract4NSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("6N Series (Logic Output)")
    class SixNSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect 6N series optocouplers")
        @ValueSource(strings = {
            "6N135",
            "6N136",
            "6N137",
            "6N138",
            "6N139"
        })
        void shouldDetect6NSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should extract 6N series correctly")
        @CsvSource({
            "6N135, 6N135",
            "6N136, 6N136",
            "6N137, 6N137",
            "6N138, 6N138",
            "6N139, 6N139"
        })
        void shouldExtract6NSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("MOC Series (Triac/SCR Drivers)")
    class MOCSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect MOC30xx zero-crossing triac drivers")
        @ValueSource(strings = {
            "MOC3020",
            "MOC3021",
            "MOC3022",
            "MOC3023",
            "MOC3041",
            "MOC3042",
            "MOC3043",
            "MOC3051",
            "MOC3052",
            "MOC3062",
            "MOC3063"
        })
        void shouldDetectMOC30xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect MOC31xx random phase triac drivers")
        @ValueSource(strings = {
            "MOC3120",
            "MOC3121",
            "MOC3140",
            "MOC3141",
            "MOC3142",
            "MOC3160",
            "MOC3161",
            "MOC3162",
            "MOC3163"
        })
        void shouldDetectMOC31xxSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should extract MOC series correctly")
        @CsvSource({
            "MOC3020, MOC30xx",
            "MOC3021, MOC30xx",
            "MOC3041, MOC30xx",
            "MOC3120, MOC31xx",
            "MOC3162, MOC31xx"
        })
        void shouldExtractMOCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("TLP Series (Toshiba-Compatible)")
    class TLPSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect TLP series optocouplers")
        @ValueSource(strings = {
            "TLP521",
            "TLP521-1",
            "TLP521-2",
            "TLP521-4",
            "TLP621",
            "TLP621-1",
            "TLP627",
            "TLP627-4"
        })
        void shouldDetectTLPSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should extract TLP series correctly")
        @CsvSource({
            "TLP521, TLP521",
            "TLP521-1, TLP521",
            "TLP621, TLP621",
            "TLP627-4, TLP627"
        })
        void shouldExtractTLPSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Other Compatible Series")
    class OtherSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect H11 series")
        @ValueSource(strings = {"H11A1", "H11A2", "H11B1", "H11B2", "H11C1", "H11D1"})
        void shouldDetectH11Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect CNY series")
        @ValueSource(strings = {"CNY17", "CNY17-1", "CNY17-2", "CNY17-3", "CNY17-4"})
        void shouldDetectCNYSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect PC series (Sharp-compatible)")
        @ValueSource(strings = {"PC817", "PC817A", "PC817B", "PC847"})
        void shouldDetectPCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect IL series (Vishay-compatible)")
        @ValueSource(strings = {"IL300", "IL410", "IL420"})
        void shouldDetectILSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect SFH series")
        @ValueSource(strings = {"SFH6156", "SFH6186", "SFH6206", "SFH6286"})
        void shouldDetectSFHSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package code from hyphenated MPNs")
        @CsvSource({
            "ISP817-1, DIP-4",
            "ISP817-2, DIP-6",
            "ISP817-4, DIP-8",
            "ISP827-1, DIP-4",
            "TLP521-1, DIP-4",
            "TLP521-2, DIP-6",
            "TLP521-4, DIP-8"
        })
        void shouldExtractPackageFromHyphenatedMPN(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should return default package for series without explicit code")
        @CsvSource({
            "4N25, DIP-6",
            "4N35, DIP-6",
            "6N137, DIP-8",
            "MOC3020, DIP-6",
            "MOC3041, DIP-6",
            "H11A1, DIP-6"
        })
        void shouldReturnDefaultPackageForSeries(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from various MPNs")
        @CsvSource({
            "ISP817, ISP",
            "ISP817A, ISP",
            "ISP817-1, ISP",
            "ISQ817, ISQ",
            "ISD817, ISD",
            "4N25, 4N25",
            "4N35, 4N35",
            "6N137, 6N137",
            "MOC3020, MOC30xx",
            "MOC3120, MOC31xx",
            "TLP521, TLP521",
            "TLP621, TLP621",
            "H11A1, H11A",
            "CNY17, CNY",
            "PC817, PC",
            "IL300, IL",
            "SFH6156, SFH"
        })
        void shouldExtractSeriesCorrectly(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same ISP part with different CTR grades")
        void samePISPartDifferentCTRGrades() {
            // Higher CTR grade (A) can replace lower (B, C, D)
            assertTrue(handler.isOfficialReplacement("ISP817A", "ISP817B"),
                    "ISP817A should replace ISP817B (higher CTR)");
            assertTrue(handler.isOfficialReplacement("ISP817A", "ISP817C"),
                    "ISP817A should replace ISP817C");
            assertTrue(handler.isOfficialReplacement("ISP817A", "ISP817D"),
                    "ISP817A should replace ISP817D");

            // Lower CTR grade should NOT replace higher
            assertFalse(handler.isOfficialReplacement("ISP817D", "ISP817A"),
                    "ISP817D should NOT replace ISP817A (lower CTR)");
        }

        @Test
        @DisplayName("Same part different packages")
        void samePartDifferentPackages() {
            // Different packages are not replaceable
            assertFalse(handler.isOfficialReplacement("ISP817-1", "ISP817-4"),
                    "Different packages should not be replaceable");
        }

        @Test
        @DisplayName("Compatible 4N series parts")
        void compatible4NSeriesParts() {
            // Within same group should be compatible
            assertTrue(handler.isOfficialReplacement("4N25", "4N26"),
                    "4N25 and 4N26 should be compatible (same group)");
            assertTrue(handler.isOfficialReplacement("4N35", "4N36"),
                    "4N35 and 4N36 should be compatible (same group)");

            // Different groups should not be compatible
            assertFalse(handler.isOfficialReplacement("4N25", "4N35"),
                    "4N25 and 4N35 should not be compatible (different groups)");
        }

        @Test
        @DisplayName("MOC series driver type compatibility")
        void mocSeriesDriverTypeCompatibility() {
            // Same driver type should be compatible
            assertTrue(handler.isOfficialReplacement("MOC3020", "MOC3021"),
                    "MOC3020 and MOC3021 should be compatible (same type)");

            // Different driver types should not be compatible
            assertFalse(handler.isOfficialReplacement("MOC3020", "MOC3120"),
                    "Zero-crossing (30xx) and random phase (31xx) should not be compatible");
        }

        @Test
        @DisplayName("Different series are not replaceable")
        void differentSeriesNotReplaceable() {
            assertFalse(handler.isOfficialReplacement("ISP817", "ISQ817"),
                    "Different series should not be replaceable");
            assertFalse(handler.isOfficialReplacement("4N25", "6N137"),
                    "Different series should not be replaceable");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC component type")
        void shouldSupportICType() {
            var types = handler.getSupportedTypes();

            assertNotNull(types, "Supported types should not be null");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type for optocouplers");
        }

        @Test
        @DisplayName("Should use immutable Set.of()")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();

            // Set.of() returns an immutable set, which throws UnsupportedOperationException on add
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "getSupportedTypes() should return immutable Set");
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
            assertFalse(handler.isOfficialReplacement(null, "ISP817"));
            assertFalse(handler.isOfficialReplacement("ISP817", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertFalse(handler.isOfficialReplacement("", "ISP817"));
            assertFalse(handler.isOfficialReplacement("ISP817", ""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("ISP817", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("isp817", ComponentType.IC, registry));
            assertTrue(handler.matches("ISP817", ComponentType.IC, registry));
            assertTrue(handler.matches("Isp817", ComponentType.IC, registry));
            assertTrue(handler.matches("moc3020", ComponentType.IC, registry));
            assertTrue(handler.matches("MOC3020", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            IsocomHandler directHandler = new IsocomHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertTrue(directHandler.matches("ISP817", ComponentType.IC, directRegistry));
            assertTrue(directHandler.matches("4N25", ComponentType.IC, directRegistry));
            assertTrue(directHandler.matches("MOC3020", ComponentType.IC, directRegistry));
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
    @DisplayName("Lead-Free (RoHS) Suffix Handling")
    class LeadFreeSuffixTests {

        @ParameterizedTest
        @DisplayName("Should handle lead-free X suffix")
        @ValueSource(strings = {
            "ISP817-1X",
            "ISP817-2X",
            "ISP817-4X",
            "TLP521-1X",
            "4N25X"
        })
        void shouldHandleLeadFreeSuffix(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("Lead-free suffix should not affect package extraction")
        void leadFreeSuffixShouldNotAffectPackage() {
            // -1X should still return DIP-4
            assertEquals("DIP-4", handler.extractPackageCode("ISP817-1X"),
                    "ISP817-1X should have DIP-4 package");
        }
    }

    @Nested
    @DisplayName("Real-World Part Numbers")
    class RealWorldPartNumberTests {

        @ParameterizedTest
        @DisplayName("Should handle common real-world Isocom part numbers")
        @ValueSource(strings = {
            "ISP817A",          // Common phototransistor optocoupler
            "ISP817-1",         // DIP-4 package
            "ISP827-4",         // Dual, DIP-8 package
            "ISP847-4",         // Quad, DIP-8 package
            "4N25",             // Industry standard
            "4N35",             // Higher isolation
            "6N137",            // High-speed logic output
            "MOC3020",          // Zero-crossing triac driver
            "MOC3041",          // Zero-crossing, 400V
            "MOC3162",          // Random phase, 600V
            "TLP521-1",         // Toshiba-compatible single
            "TLP621-4"          // Toshiba-compatible quad
        })
        void shouldHandleRealWorldPartNumbers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized");
            assertFalse(handler.extractSeries(mpn).isEmpty(),
                    mpn + " should have extractable series");
        }
    }
}
