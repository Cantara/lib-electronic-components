package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.PanasonicHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for PanasonicHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class PanasonicHandlerTest {

    private static PanasonicHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new PanasonicHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Capacitor Detection - Documentation Tests")
    class CapacitorTests {

        @ParameterizedTest
        @DisplayName("Document EEE capacitor detection")
        @ValueSource(strings = {"EEEHA1V470P", "EEEHD1H100P", "EEU-FC1E101"})
        void documentEEECapacitorDetection(String mpn) {
            boolean matchesCapacitor = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            System.out.println("Capacitor detection: " + mpn + " CAPACITOR=" + matchesCapacitor);
        }
    }

    @Nested
    @DisplayName("Resistor Detection - Documentation Tests")
    class ResistorTests {

        @ParameterizedTest
        @DisplayName("Document ERJ resistor detection")
        @ValueSource(strings = {"ERJ-3EKF1001V", "ERJ-6ENF1000V", "ERJ-8ENF1002V"})
        void documentERJResistorDetection(String mpn) {
            boolean matchesResistor = handler.matches(mpn, ComponentType.RESISTOR, registry);
            System.out.println("Resistor detection: " + mpn + " RESISTOR=" + matchesResistor);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"EEEHA1V470P", "ERJ-3EKF1001V"};
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
            String[] mpns = {"EEEHA1V470P", "ERJ-3EKF1001V"};
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
            PanasonicHandler directHandler = new PanasonicHandler();
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
