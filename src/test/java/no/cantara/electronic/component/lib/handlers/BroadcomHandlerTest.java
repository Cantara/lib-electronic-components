package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.BroadcomHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for BroadcomHandler.
 * Tests Wi-Fi/Bluetooth combos, network switches, storage controllers, and PHYs.
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
    @DisplayName("WiFi/Bluetooth Combo Detection")
    class WiFiBluetoothTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "BCM4375",
            "BCM4389",
            "BCM43752",
            "BCM89359"
        })
        void documentWiFiBluetoothDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Network Switch Detection")
    class NetworkSwitchTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "BCM53128",
            "BCM56870",
            "BCM58700",
            "BCM88690"
        })
        void documentNetworkSwitchDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Network Controller Detection")
    class NetworkControllerTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "BCM57810",
            "BCM5719",
            "BCM5720",
            "BCM5762"
        })
        void documentNetworkControllerDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Storage Controller Detection")
    class StorageControllerTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "BCM16001",
            "BCM22001",
            "BCM84501"
        })
        void documentStorageControllerDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("PHY Detection")
    class PHYTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "BCM54210",
            "BCM54618",
            "BCM54820"
        })
        void documentPHYDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("PCIe Switch Detection")
    class PCIeSwitchTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PEX8724",
            "PLX8748"
        })
        void documentPCIeSwitchDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "BCM4375-KF, FCBGA",
            "BCM4389-KU, UBGA",
            "BCM43752-B0, BGA",
            "BCM57810-LP, LFBGA",
            "BCM5719-WH, WLCSP"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @Test
        void shouldReturnEmptyForMPNWithoutDash() {
            assertEquals("", handler.extractPackageCode("BCM4375"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "BCM4375-KF, BCM4375",
            "BCM43752-B0, BCM4375",
            "BCM57810-LP, BCM5781",
            "PEX8724, PEX8724"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("BCM4375-KF", "BCM5719-B0"));
        }

        @Test
        void documentCompatibleSeries() {
            // BCM4375 and BCM4373 are documented as compatible
            boolean isReplacement = handler.isOfficialReplacement("BCM4375-KF", "BCM4373-KF");
            System.out.println("BCM4375-KF can replace BCM4373-KF = " + isReplacement);
        }
    }
}
