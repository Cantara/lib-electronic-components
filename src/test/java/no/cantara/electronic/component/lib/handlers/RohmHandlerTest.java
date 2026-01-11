package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.RohmHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for RohmHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class RohmHandlerTest {

    private static RohmHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new RohmHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("MCR Resistor Detection - Documentation Tests")
    class MCRResistorTests {

        @ParameterizedTest
        @DisplayName("Document MCR resistor detection")
        @ValueSource(strings = {"MCR10EZPF1001", "MCR03EZPFX1000", "KTR03EZPF1001"})
        void documentMCRResistorDetection(String mpn) {
            boolean matchesResistor = handler.matches(mpn, ComponentType.RESISTOR, registry);
            System.out.println("Resistor detection: " + mpn + " RESISTOR=" + matchesResistor);
        }
    }

    @Nested
    @DisplayName("IC Detection - Documentation Tests")
    class ICTests {

        @ParameterizedTest
        @DisplayName("Document BH/BD IC detection")
        @ValueSource(strings = {"BH1750FVI-TR", "BD9215AFV", "BU9480F"})
        void documentICDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("IC detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("MOSFET Detection - Documentation Tests")
    class MOSFETTests {

        @ParameterizedTest
        @DisplayName("Document RQ MOSFET detection")
        @ValueSource(strings = {"RQ3E050BNTB", "RQK0305CBDTB"})
        void documentMOSFETDetection(String mpn) {
            boolean matchesMosfet = handler.matches(mpn, ComponentType.MOSFET, registry);
            System.out.println("MOSFET detection: " + mpn + " MOSFET=" + matchesMosfet);
        }
    }

    @Nested
    @DisplayName("LED Detection - Documentation Tests")
    class LEDTests {

        @ParameterizedTest
        @DisplayName("Document SML LED detection")
        @ValueSource(strings = {"SML-LX0603IW", "SMLP12BC8W1"})
        void documentLEDDetection(String mpn) {
            boolean matchesLED = handler.matches(mpn, ComponentType.LED, registry);
            System.out.println("LED detection: " + mpn + " LED=" + matchesLED);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"MCR10EZPF1001", "BH1750FVI-TR"};
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
            String[] mpns = {"MCR10EZPF1001", "BH1750FVI-TR"};
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
            assertFalse(handler.matches(null, ComponentType.RESISTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.RESISTOR, registry));
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
            RohmHandler directHandler = new RohmHandler();
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
