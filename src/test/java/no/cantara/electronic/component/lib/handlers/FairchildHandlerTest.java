package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.FairchildHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for FairchildHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Fairchild Semiconductor (now ON Semiconductor) MOSFETs and transistors.
 */
class FairchildHandlerTest {

    private static FairchildHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new FairchildHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("N-Channel MOSFET Detection")
    class NChannelMOSFETTests {

        @ParameterizedTest
        @DisplayName("Should detect FQP N-Channel TO-220 MOSFETs")
        @ValueSource(strings = {"FQP30N06", "FQP50N06", "FQP13N10", "FQP27P06"})
        void shouldDetectFQPMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect FQD N-Channel DPAK MOSFETs")
        @ValueSource(strings = {"FQD30N06", "FQD13N10", "FQD2N60"})
        void shouldDetectFQDMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect FDS SO-8 MOSFETs")
        @ValueSource(strings = {"FDS6679", "FDS6680", "FDS4435", "FDS6690"})
        void shouldDetectFDSMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("P-Channel MOSFET Detection")
    class PChannelMOSFETTests {

        @ParameterizedTest
        @DisplayName("Should detect FQP P-Channel TO-220 MOSFETs")
        @ValueSource(strings = {"FQP27P06", "FQP47P06", "FQP12P20"})
        void shouldDetectFQPPChannelMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("Other MOSFET Families")
    class OtherMOSFETTests {

        @ParameterizedTest
        @DisplayName("Should detect FQL Logic Level MOSFETs")
        @ValueSource(strings = {"FQL40N50", "FQL30N06"})
        void shouldDetectFQLMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect 2N7xxx Small Signal MOSFETs")
        @ValueSource(strings = {"2N7000", "2N7002", "2N7002K"})
        void shouldDetect2N7xxxMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect BS series Small Signal MOSFETs")
        @ValueSource(strings = {"BS170", "BS250", "BS108"})
        void shouldDetectBSMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect FDB PowerTrench MOSFETs")
        @ValueSource(strings = {"FDB045AN08", "FDB050AN08", "FDB8030L"})
        void shouldDetectFDBMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }

        @ParameterizedTest
        @DisplayName("Should detect FDA Complementary Pair MOSFETs")
        @ValueSource(strings = {"FDA24N40", "FDA38N30", "FDA59N30"})
        void shouldDetectFDAMOSFETs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MOSFET, registry),
                    mpn + " should match MOSFET");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from prefix")
        @CsvSource({
                "FQP30N06, TO-220",
                "FQD30N06, DPAK",
                "FQU13N10, IPAK",
                "FDS6679, SO-8"
        })
        void shouldExtractPackageFromPrefix(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
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
        @DisplayName("Should extract FQP series")
        @CsvSource({
                "FQP30N06, FQP30N06",
                "FQP30N06L, FQP30N06",
                "FQP27P06, FQP27P06"
        })
        void shouldExtractFQPSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract FDS series")
        @CsvSource({
                "FDS6679, FDS6679",
                "FDS6679AZ, FDS6679",
                "FDS4435, FDS4435"
        })
        void shouldExtractFDSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract 2N7xxx series")
        @CsvSource({
                "2N7000, 2N7000",
                "2N7002, 2N7002",
                "2N7002K, 2N7002"
        })
        void shouldExtract2N7xxxSeries(String mpn, String expectedSeries) {
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
        @DisplayName("Identical parts should be replacements")
        void identicalPartsShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("FQP30N06", "FQP30N06"),
                    "Identical parts should be replacements");
        }

        @Test
        @DisplayName("Known cross-reference replacements")
        void knownCrossReference() {
            assertTrue(handler.isOfficialReplacement("FQP30N06", "IRF530"),
                    "FQP30N06 and IRF530 are known compatible");
            assertTrue(handler.isOfficialReplacement("IRF530", "FQP30N06"),
                    "IRF530 and FQP30N06 are known compatible");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("FQP30N06", "FDS6679"),
                    "Different series should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "FQP30N06"));
            assertFalse(handler.isOfficialReplacement("FQP30N06", null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MOSFET));
            assertTrue(types.contains(ComponentType.TRANSISTOR));
            assertTrue(types.contains(ComponentType.DIODE));
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.MOSFET, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MOSFET, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("FQP30N06", null, registry));
        }
    }
}
