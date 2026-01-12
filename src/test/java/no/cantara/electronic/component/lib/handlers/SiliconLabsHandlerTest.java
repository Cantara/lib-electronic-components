package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SiliconLabsHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for SiliconLabsHandler.
 * Tests EFM8, EFM32, EFR32 MCUs, timing ICs, and sensors.
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
    @DisplayName("8-bit MCU Detection (C8051)")
    class C8051Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "C8051F380-GQ",
            "C8051F320-GQ",
            "C8051F120-GQ"
        })
        void documentC8051Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println(mpn + " matches MICROCONTROLLER = " + matchesMCU);
            System.out.println(mpn + " matches MICROCONTROLLER_SILICON_LABS = " + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("EFM8 Series Detection")
    class EFM8Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EFM8BB10F8G-A-QFN20",
            "EFM8SB10F8G-A-QFN20",
            "EFM8LB12F64E-A-QFN32"
        })
        void documentEFM8Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println(mpn + " matches MICROCONTROLLER = " + matchesMCU);
            System.out.println(mpn + " matches MICROCONTROLLER_SILICON_LABS = " + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("EFM32 Series Detection")
    class EFM32Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EFM32GG230F1024G-C0",
            "EFM32WG980F256-QFP100",
            "EFM32HG322F64G-B-QFN48",
            "EFM32PG12B500F1024GL125"
        })
        void documentEFM32Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println(mpn + " matches MICROCONTROLLER = " + matchesMCU);
            System.out.println(mpn + " matches MICROCONTROLLER_SILICON_LABS = " + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("EFR32 Series Detection (Wireless MCUs)")
    class EFR32Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EFR32BG21A010F1024IM32-B",
            "EFR32FG14P233F256GM48-C0",
            "EFR32MG12P332F1024GL125"
        })
        void documentEFR32Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            boolean matchesSilabs = handler.matches(mpn, ComponentType.MICROCONTROLLER_SILICON_LABS, registry);
            System.out.println(mpn + " matches MICROCONTROLLER = " + matchesMCU);
            System.out.println(mpn + " matches MICROCONTROLLER_SILICON_LABS = " + matchesSilabs);
        }
    }

    @Nested
    @DisplayName("Timing ICs Detection")
    class TimingTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "SI5351A-B-GT",
            "SI5341B-D-GM"
        })
        void documentTimingICDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Sensor Detection")
    class SensorTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "SI7021-A20-GM1R",
            "SI1145-A10-GMR"
        })
        void documentSensorDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("USB Bridge Detection")
    class USBBridgeTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "CP2102N-A01-GQFN28",
            "CP2104-F03-GM"
        })
        void documentUSBBridgeDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "C8051F380-GQ, GQ",
            "EFM32GG230F1024G-C0, G",
            "EFM32WG980F256-QFP100, QFP100",
            "SI5351A-B-GT, GT"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "C8051F380-GQ, C8051",
            "EFM8BB10F8G-A-QFN20, EFM8 Busy Bee",
            "EFM8SB10F8G-A-QFN20, EFM8 Sleepy Bee",
            "EFM8LB12F64E-A-QFN32, EFM8 Laser Bee",
            "EFM32GG230F1024G-C0, EFM32 Giant Gecko",
            "EFM32WG980F256-QFP100, EFM32 Wonder Gecko",
            "EFM32HG322F64G-B-QFN48, EFM32 Happy Gecko",
            "EFM32PG12B500F1024GL125, EFM32 Pearl Gecko",
            "EFR32BG21A010F1024IM32-B, Blue Gecko",
            "EFR32FG14P233F256GM48-C0, Flex Gecko",
            "EFR32MG12P332F1024GL125, Mighty Gecko",
            "SI5351A-B-GT, Timing",
            "SI7021-A20-GM1R, Environmental Sensor",
            "CP2102N-A01-GQFN28, USB Bridge"
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
            assertFalse(handler.matches(null, ComponentType.MICROCONTROLLER, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.MICROCONTROLLER, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        void shouldBeCaseInsensitive() {
            String series1 = handler.extractSeries("efm32gg230f1024g-c0");
            String series2 = handler.extractSeries("EFM32GG230F1024G-C0");
            assertEquals(series1, series2);
        }
    }
}
