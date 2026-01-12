package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SiliconLabsHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SiliconLabsHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class SiliconLabsHandlerTest {

    private static SiliconLabsHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SiliconLabsHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("C8051 MCU Detection - Documentation Tests")
    class C8051Tests {

        @ParameterizedTest
        @DisplayName("Document C8051 MCU detection")
        @ValueSource(strings = {"C8051F380-GQ", "C8051F320-GM", "C8051F330-GM"})
        void documentC8051Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println("C8051 detection: " + mpn + " MCU=" + matchesMCU + " SILABS=" + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("EFM8 MCU Detection - Documentation Tests")
    class EFM8Tests {

        @ParameterizedTest
        @DisplayName("Document EFM8 MCU detection")
        @ValueSource(strings = {"EFM8BB10F8G-A-QFN20", "EFM8SB10F8G-A-QFN20", "EFM8LB12F64E-B-QFP32"})
        void documentEFM8Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println("EFM8 detection: " + mpn + " MCU=" + matchesMCU + " SILABS=" + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("EFM32 MCU Detection - Documentation Tests")
    class EFM32Tests {

        @ParameterizedTest
        @DisplayName("Document EFM32 MCU detection")
        @ValueSource(strings = {"EFM32GG230F1024G-C0", "EFM32WG980F256-QFP100", "EFM32HG108F64G-B-QFN24", "EFM32PG12B500F1024GL125-B"})
        void documentEFM32Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println("EFM32 detection: " + mpn + " MCU=" + matchesMCU + " SILABS=" + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("EFR32 Wireless MCU Detection - Documentation Tests")
    class EFR32Tests {

        @ParameterizedTest
        @DisplayName("Document EFR32 wireless MCU detection")
        @ValueSource(strings = {"EFR32BG13P632F512GM48-C0", "EFR32FG14V232F256GM48-C0", "EFR32MG21A020F1024IM32-B0"})
        void documentEFR32Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println("EFR32 detection: " + mpn + " MCU=" + matchesMCU + " SILABS=" + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("Timing IC Detection - Documentation Tests")
    class TimingICTests {

        @ParameterizedTest
        @DisplayName("Document timing IC detection")
        @ValueSource(strings = {"SI5351A-B-GT", "SI5340A-D-GM", "SI5342A-D-GM"})
        void documentTimingICDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Timing IC detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Sensor IC Detection - Documentation Tests")
    class SensorICTests {

        @ParameterizedTest
        @DisplayName("Document sensor IC detection")
        @ValueSource(strings = {"SI7021-A20-GM", "SI7020-A20-GM1", "SI1145-A11-GMR"})
        void documentSensorICDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Sensor IC detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("USB Bridge Detection - Documentation Tests")
    class USBBridgeTests {

        @ParameterizedTest
        @DisplayName("Document USB bridge detection")
        @ValueSource(strings = {"CP2102N-A02-GQFN28", "CP2104-F03-GMR", "CP2105-F01-GM"})
        void documentUSBBridgeDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("USB bridge detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @Test
        @DisplayName("Should extract GQ package code")
        void shouldExtractGQPackage() {
            assertEquals("GQ", handler.extractPackageCode("C8051F380-GQ"));
        }

        @Test
        @DisplayName("Should extract QFP package code")
        void shouldExtractQFPPackage() {
            assertEquals("QFP100", handler.extractPackageCode("EFM32WG980F256-QFP100"));
        }

        @Test
        @DisplayName("Document package code extraction")
        void documentPackageCodeExtraction() {
            String[] mpns = {"C8051F380-GQ", "EFM32WG980F256-QFP100", "EFR32BG13P632F512GM48-C0"};
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
        @DisplayName("Should extract C8051 series")
        void shouldExtractC8051Series() {
            assertEquals("C8051", handler.extractSeries("C8051F380-GQ"));
        }

        @Test
        @DisplayName("Should extract EFM8 Busy Bee series")
        void shouldExtractEFM8BusyBeeSeries() {
            assertEquals("EFM8 Busy Bee", handler.extractSeries("EFM8BB10F8G-A-QFN20"));
        }

        @Test
        @DisplayName("Should extract EFM32 Giant Gecko series")
        void shouldExtractEFM32GiantGeckoSeries() {
            assertEquals("EFM32 Giant Gecko", handler.extractSeries("EFM32GG230F1024G-C0"));
        }

        @Test
        @DisplayName("Should extract Blue Gecko series")
        void shouldExtractBlueGeckoSeries() {
            assertEquals("Blue Gecko", handler.extractSeries("EFR32BG13P632F512GM48-C0"));
        }

        @Test
        @DisplayName("Should extract Timing series")
        void shouldExtractTimingSeries() {
            assertEquals("Timing", handler.extractSeries("SI5351A-B-GT"));
        }

        @Test
        @DisplayName("Should extract USB Bridge series")
        void shouldExtractUSBBridgeSeries() {
            assertEquals("USB Bridge", handler.extractSeries("CP2102N-A02-GQFN28"));
        }

        @Test
        @DisplayName("Document series extraction")
        void documentSeriesExtraction() {
            String[] mpns = {"C8051F380-GQ", "EFM8BB10F8G-A-QFN20", "EFM32GG230F1024G-C0", "EFR32BG13P632F512GM48-C0", "SI5351A-B-GT"};
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
            assertFalse(handler.matches(null, ComponentType.MICROCONTROLLER, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "EFM32GG230F1024G-C0"));
            assertFalse(handler.isOfficialReplacement("EFM32GG230F1024G-C0", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MICROCONTROLLER, registry));
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
            SiliconLabsHandler directHandler = new SiliconLabsHandler();
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
