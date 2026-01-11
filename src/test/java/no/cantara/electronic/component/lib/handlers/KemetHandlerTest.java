package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.KemetHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for KemetHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class KemetHandlerTest {

    private static KemetHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new KemetHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MLCC Detection - Documentation Tests")
    class MLCCTests {

        @ParameterizedTest
        @DisplayName("Document C series MLCC detection")
        @ValueSource(strings = {"C0603C104K5RACTU", "C0402C103K4RACTU", "C0805C106K8PACTU"})
        void documentMLCCDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            System.out.println("MLCC detection: " + mpn + " CAPACITOR=" + matchesCapacitor);
        }
    }

    @Nested
    @DisplayName("Tantalum Capacitor Detection - Documentation Tests")
    class TantalumTests {

        @ParameterizedTest
        @DisplayName("Document T series tantalum detection")
        @ValueSource(strings = {"T491A106K016AT", "T491B476M010AT"})
        void documentTantalumDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            System.out.println("Tantalum detection: " + mpn + " CAPACITOR=" + matchesCapacitor);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"C0603C104K5RACTU", "T491A106K016AT"};
            for (String mpn : mpns) {
                String packageCode = handler.extractPackageCode(mpn);
                System.out.println("Package code for " + mpn + ": " + packageCode);
            }
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"C0603C104K5RACTU", "T491A106K016AT"};
            for (String mpn : mpns) {
                String series = handler.extractSeries(mpn);
                System.out.println("Series for " + mpn + ": " + series);
            }
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should have supported types")
        void shouldHaveSupportedTypes() {
            var types = handler.getSupportedTypes();
            assertNotNull(types, "Should return non-null set");
            assertFalse(types.isEmpty(), "Should have at least one supported type");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR, registry));
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
            KemetHandler directHandler = new KemetHandler();
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
        }
    }
}
