package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.BroadcomHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for BroadcomHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class BroadcomHandlerTest {

    private static BroadcomHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new BroadcomHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Wi-Fi/Bluetooth Combo Chips - Documentation Tests")
    class WiFiBluetoothTests {

        @ParameterizedTest
        @DisplayName("Document Wi-Fi/BT combo detection")
        @ValueSource(strings = {"BCM4375", "BCM4389", "BCM43752", "BCM89560"})
        void documentWiFiBTDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Wi-Fi/BT detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Network Switch Detection - Documentation Tests")
    class NetworkSwitchTests {

        @ParameterizedTest
        @DisplayName("Document network switch detection")
        @ValueSource(strings = {"BCM53125", "BCM56870", "BCM58525", "BCM88650"})
        void documentNetworkSwitchDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Network switch detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Network Controller Detection - Documentation Tests")
    class NetworkControllerTests {

        @ParameterizedTest
        @DisplayName("Document network controller detection")
        @ValueSource(strings = {"BCM5719", "BCM5720", "BCM5762", "BCM57810"})
        void documentNetworkControllerDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Network controller detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Storage Controller Detection - Documentation Tests")
    class StorageControllerTests {

        @ParameterizedTest
        @DisplayName("Document storage controller detection")
        @ValueSource(strings = {"BCM1600", "BCM2200", "BCM8450"})
        void documentStorageControllerDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Storage controller detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("PHY Detection - Documentation Tests")
    class PHYTests {

        @ParameterizedTest
        @DisplayName("Document PHY detection")
        @ValueSource(strings = {"BCM52411", "BCM54610", "BCM54820"})
        void documentPHYDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("PHY detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("PCIe Switch Detection - Documentation Tests")
    class PCIeSwitchTests {

        @ParameterizedTest
        @DisplayName("Document PCIe switch detection")
        @ValueSource(strings = {"PEX8732", "PEX8748", "PLX8732"})
        void documentPCIeSwitchDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("PCIe switch detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should extract FCBGA package code")
        void shouldExtractFCBGAPackage() {
            assertEquals("FCBGA", handler.extractPackageCode("BCM4375-KF"));
        }

        @Test
        @DisplayName("Should extract BGA package code")
        void shouldExtractBGAPackage() {
            assertEquals("BGA", handler.extractPackageCode("BCM53125-B0"));
        }

        @Test
        @DisplayName("Should extract WLCSP package code")
        void shouldExtractWLCSPPackage() {
            assertEquals("WLCSP", handler.extractPackageCode("BCM4389-WH"));
        }

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"BCM4375-KF", "BCM53125-B0", "BCM4389-WH", "BCM5719-LP"};
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
        @DisplayName("Should extract BCM series")
        void shouldExtractBCMSeries() {
            assertEquals("BCM4375", handler.extractSeries("BCM4375-KF"));
            assertEquals("BCM5312", handler.extractSeries("BCM53125-B0"));
        }

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"BCM4375-KF", "BCM53125-B0", "BCM89560-AX", "PEX8732"};
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
                    {"BCM4375-KF", "BCM4373-KF"},
                    {"BCM53125-B0", "BCM53125-LP"},
                    {"BCM5719", "BCM5720"}
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
            assertFalse(handler.isOfficialReplacement(null, "BCM4375"));
            assertFalse(handler.isOfficialReplacement("BCM4375", null));
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
            BroadcomHandler directHandler = new BroadcomHandler();
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
