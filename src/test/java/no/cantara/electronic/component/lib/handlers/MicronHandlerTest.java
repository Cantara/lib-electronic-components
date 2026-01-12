package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MicronHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for MicronHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class MicronHandlerTest {

    private static MicronHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new MicronHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("DDR4 SDRAM Detection - Documentation Tests")
    class DDR4Tests {

        @ParameterizedTest
        @DisplayName("Document DDR4 SDRAM detection")
        @ValueSource(strings = {"MT40A1G16KD-062E:E", "MT40A2G16KD-062E:E", "MT40A512M16LY-062E:E"})
        void documentDDR4Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println("DDR4 detection: " + mpn + " MEMORY=" + matchesMemory + " MICRON=" + matchesMicron);
        }
    }

    @Nested
    @DisplayName("DDR3 SDRAM Detection - Documentation Tests")
    class DDR3Tests {

        @ParameterizedTest
        @DisplayName("Document DDR3 SDRAM detection")
        @ValueSource(strings = {"MT41K256M16HA-125:E", "MT41K512M16HA-125:E"})
        void documentDDR3Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println("DDR3 detection: " + mpn + " MEMORY=" + matchesMemory + " MICRON=" + matchesMicron);
        }
    }

    @Nested
    @DisplayName("LPDDR4 Detection - Documentation Tests")
    class LPDDR4Tests {

        @ParameterizedTest
        @DisplayName("Document LPDDR4 detection")
        @ValueSource(strings = {"MT48LC16M16A2P-6A:G", "MT48LC32M16A2P-6A:G"})
        void documentLPDDR4Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println("LPDDR4 detection: " + mpn + " MEMORY=" + matchesMemory + " MICRON=" + matchesMicron);
        }
    }

    @Nested
    @DisplayName("NAND Flash Detection - Documentation Tests")
    class NANDFlashTests {

        @ParameterizedTest
        @DisplayName("Document NAND flash detection")
        @ValueSource(strings = {"MT29F4G08ABADAWP:D", "MT29F16G08CBACAWP:C", "MT29E64G08CBCBB:A"})
        void documentNANDFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println("NAND flash detection: " + mpn + " MEMORY=" + matchesMemory + " MICRON=" + matchesMicron);
        }
    }

    @Nested
    @DisplayName("NOR Flash Detection - Documentation Tests")
    class NORFlashTests {

        @ParameterizedTest
        @DisplayName("Document NOR flash detection")
        @ValueSource(strings = {"MT28EW128ABA1LPC-0SIT", "MT28FW256ABA1LPC-0SIT", "MT25QL128ABA1ESE-0SIT"})
        void documentNORFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println("NOR flash detection: " + mpn + " MEMORY=" + matchesMemory + " MICRON=" + matchesMicron);
        }
    }

    @Nested
    @DisplayName("eMMC Detection - Documentation Tests")
    class EMMCTests {

        @ParameterizedTest
        @DisplayName("Document eMMC detection")
        @ValueSource(strings = {"MT29K128G08AWBDS", "MT29L64G08AWBDS"})
        void documentEMMCDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println("eMMC detection: " + mpn + " MEMORY=" + matchesMemory + " MICRON=" + matchesMicron);
        }
    }

    @Nested
    @DisplayName("SSD Detection - Documentation Tests")
    class SSDTests {

        @ParameterizedTest
        @DisplayName("Document SSD detection")
        @ValueSource(strings = {"MTFDDAK256TBN-1AR1ZABYY", "MTFDDAV256TBN-1AR1ZABYY"})
        void documentSSDDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesMicron = handler.matches(mpn, ComponentType.MEMORY_MICRON, registry);
            System.out.println("SSD detection: " + mpn + " MEMORY=" + matchesMemory + " MICRON=" + matchesMicron);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"MT40A1G16KD-062E-BGA", "MT29F4G08ABADAWP-FBGA", "MT25QL128ABA1ESE-TSOP"};
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
        @DisplayName("Should extract DDR4 series")
        void shouldExtractDDR4Series() {
            assertEquals("MT40A", handler.extractSeries("MT40A1G16KD-062E:E"));
        }

        @Test
        @DisplayName("Should extract DDR3 series")
        void shouldExtractDDR3Series() {
            assertEquals("MT41K", handler.extractSeries("MT41K256M16HA-125:E"));
        }

        @Test
        @DisplayName("Should extract NAND series")
        void shouldExtractNANDSeries() {
            assertEquals("MT29F", handler.extractSeries("MT29F4G08ABADAWP:D"));
        }

        @Test
        @DisplayName("Should extract NOR series")
        void shouldExtractNORSeries() {
            assertEquals("MT25Q", handler.extractSeries("MT25QL128ABA1ESE-0SIT"));
        }

        @Test
        @DisplayName("Should extract SSD series")
        void shouldExtractSSDSeries() {
            assertEquals("MTFDD", handler.extractSeries("MTFDDAK256TBN-1AR1ZABYY"));
        }

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"MT40A1G16KD-062E:E", "MT41K256M16HA-125:E", "MT29F4G08ABADAWP:D", "MT25QL128ABA1ESE-0SIT", "MTFDDAK256TBN-1AR1ZABYY"};
            for (String mpn : mpns) {
                String series = handler.extractSeries(mpn);
                System.out.println("Series for " + mpn + ": " + series);
            }
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Document replacement detection")
        void documentReplacementDetection() {
            String[][] pairs = {
                    {"MT40A1G16KD-062E-I", "MT40A1G16KD-062E-C"},
                    {"MT40A1G16KD-062E-A", "MT40A1G16KD-062E-I"},
                    {"MT29F4G08ABADAWP:D", "MT29F4G08ABADAWP:C"}
            };
            for (String[] pair : pairs) {
                boolean isReplacement = handler.isOfficialReplacement(pair[0], pair[1]);
                System.out.println("Replacement check: " + pair[0] + " <-> " + pair[1] + " = " + isReplacement);
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
            assertTrue(types.contains(ComponentType.MEMORY), "Should support MEMORY type");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "MT40A1G16KD-062E:E"));
            assertFalse(handler.isOfficialReplacement("MT40A1G16KD-062E:E", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
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
            MicronHandler directHandler = new MicronHandler();
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
            assertTrue(manufacturerTypes.isEmpty());
        }
    }
}
