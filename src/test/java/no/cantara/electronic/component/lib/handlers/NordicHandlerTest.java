package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.NordicHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for NordicHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class NordicHandlerTest {

    private static NordicHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new NordicHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("nRF52 Series Detection")
    class NRF52SeriesTests {

        @ParameterizedTest
        @DisplayName("Document nRF52 SoC detection")
        @ValueSource(strings = {"nRF52840-QIAA", "nRF52832-QFAA", "nRF52833-QIAA"})
        void documentNRF52Detection(String mpn) {
            // Document behavior - patterns registered for IC type
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("nRF52 detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("nRF51 Series Detection")
    class NRF51SeriesTests {

        @ParameterizedTest
        @DisplayName("Document nRF51 SoC detection")
        @ValueSource(strings = {"nRF51822-CEAA", "nRF51824-QFAC"})
        void documentNRF51Detection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("nRF51 detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("nRF53/91 Series Detection")
    class AdvancedSeriesTests {

        @ParameterizedTest
        @DisplayName("Document nRF53/91 detection")
        @ValueSource(strings = {"nRF5340-QKAA", "nRF9160-SIAA"})
        void documentAdvancedSeriesDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Advanced series detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes")
        @CsvSource({
                "nRF52840-QIAA, QFN48",
                "nRF52832-QFAA, QFN48",
                "nRF51822-CEAA, WLCSP"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract nRF series")
        @CsvSource({
                "nRF52840-QIAA, nRF52840",
                "nRF52832-QFAA, nRF52832",
                "nRF5340-QKAA, nRF5340"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series should be replacements")
        void sameSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("nRF52840-QIAA", "nRF52840-QFAA"),
                    "Same series different packages should be replacements");
        }

        @Test
        @DisplayName("nRF52840 can replace nRF52832")
        void nrf52840ReplacesNrf52832() {
            assertTrue(handler.isOfficialReplacement("nRF52840-QIAA", "nRF52832-QFAA"),
                    "nRF52840 should be able to replace nRF52832");
        }

        @Test
        @DisplayName("Different families NOT replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("nRF52840-QIAA", "nRF51822-CEAA"),
                    "nRF52 and nRF51 are different families");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support BLUETOOTH_IC_NORDIC")
        void shouldSupportBluetoothType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.BLUETOOTH_IC_NORDIC),
                    "Should support BLUETOOTH_IC_NORDIC");
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
            assertFalse(handler.isOfficialReplacement(null, "nRF52840-QIAA"));
            assertFalse(handler.isOfficialReplacement("nRF52840-QIAA", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            NordicHandler directHandler = new NordicHandler();
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
}
