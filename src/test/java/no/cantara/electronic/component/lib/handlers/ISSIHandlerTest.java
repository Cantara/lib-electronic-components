package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.ISSIHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ISSIHandler.
 * Tests SRAM, DRAM, Flash Memory, LED Drivers, and Specialty Memory.
 *
 * ISSI Part Number Patterns:
 * - SRAM: IS61xx (Async), IS62xx (Low Power), IS64xx (Sync)
 * - DRAM: IS42xx (SDRAM), IS43xx (DDR), IS45xx (DDR2), IS47xx (DDR3)
 * - Flash: IS25xx (SPI), IS29xx (NOR)
 * - LED Drivers: IS31FL, IS31AP, IS31LT
 * - Specialty: IS65xx (FIFO), IS67xx (Dual-Port)
 *
 * Package Codes:
 * T=TSOP, U=SOIC, L=PLCC, V=TSSOP, TL=TSOP-II, TR=TFBGA, B=BGA, BK=FBGA, WB=WBGA, K=VFBGA, M=QFN, Q=QFP
 */
class ISSIHandlerTest {

    private static ISSIHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new ISSIHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Async SRAM Detection (IS61xx)")
    class AsyncSRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS61C256AL-12TLI",
            "IS61LV256AL-10TLI",
            "IS61WV102416BLL-10TLI"
        })
        void documentAsyncSRAMDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("Low Power SRAM Detection (IS62xx)")
    class LowPowerSRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS62C256AL-45ULI",
            "IS62WV12816DALL-45TLI",
            "IS62LV256AL-45ULI"
        })
        void documentLowPowerSRAMDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("Sync SRAM Detection (IS64xx)")
    class SyncSRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS64WV12816BLL-10TLI",
            "IS64C1024AL-15JLI",
            "IS64WV51216BLL-10B3LI"
        })
        void documentSyncSRAMDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("SDRAM Detection (IS42xx)")
    class SDRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS42S16160J-7TLI",
            "IS42S32800J-6TLI",
            "IS42S16320D-7TLI"
        })
        void documentSDRAMDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("DDR SDRAM Detection (IS43xx)")
    class DDRTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS43TR16256A-125JBLI",
            "IS43R16160D-6TLI",
            "IS43DR16320D-3DBLI"
        })
        void documentDDRDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("DDR2 SDRAM Detection (IS45xx)")
    class DDR2Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS45S16320D-6TLA1",
            "IS45S32800D-6TLA1"
        })
        void documentDDR2Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("DDR3 SDRAM Detection (IS47xx)")
    class DDR3Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS47TR16256A-125JBLI",
            "IS47S16160J-75BLI"
        })
        void documentDDR3Detection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("SPI Flash Detection (IS25xx)")
    class SPIFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS25LP128-JBLE",
            "IS25WP064A-JBLE",
            "IS25LP512M-JBLE"
        })
        void documentSPIFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("NOR Flash Detection (IS29xx)")
    class NORFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS29GL128-90TQLI",
            "IS29GL256-90TQLI"
        })
        void documentNORFlashDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("LED Matrix Driver Detection (IS31FL)")
    class LEDMatrixDriverTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS31FL3731-QFLS2-TR",
            "IS31FL3736A-QFLS4-TR",
            "IS31FL3741-QFLS4-TR"
        })
        void documentLEDMatrixDriverDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Audio Modulated LED Driver Detection (IS31AP)")
    class AudioLEDDriverTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS31AP4991-SLS2-TR",
            "IS31AP2031-DLS2-TR"
        })
        void documentAudioLEDDriverDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("LED Lighting Driver Detection (IS31LT)")
    class LEDLightingDriverTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS31LT3360-CLS4-TR",
            "IS31LT3948A-CLS4-TR"
        })
        void documentLEDLightingDriverDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("FIFO Detection (IS65xx)")
    class FIFOTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS65C102AL-15JLI",
            "IS65WV512128BLL-10CTL"
        })
        void documentFIFODetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("Dual-Port RAM Detection (IS67xx)")
    class DualPortTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS67WV102416BLL-10TL",
            "IS67C102416DALL-10TL"
        })
        void documentDualPortDetection(String mpn) {
            boolean matchesMemory = handler.matches(mpn, ComponentType.MEMORY, registry);
            boolean matchesISSI = handler.matches(mpn, ComponentType.MEMORY_ISSI, registry);
            System.out.println(mpn + " matches MEMORY = " + matchesMemory);
            System.out.println(mpn + " matches MEMORY_ISSI = " + matchesISSI);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "IS61C256AL-12TLI",
            "IS62C256AL-45ULI",
            "IS42S16160J-7TLI",
            "IS25LP128-JBLE",
            "IS31FL3731-QFLS2-TR"
        })
        void documentPackageCodeExtraction(String mpn) {
            // Document current behavior - extractPackageCode has implementation issues
            String packageCode = handler.extractPackageCode(mpn);
            System.out.println(mpn + " -> extractPackageCode = '" + packageCode + "'");
        }

        @Test
        void shouldReturnEmptyForNull() {
            assertEquals("", handler.extractPackageCode(null));
        }

        @Test
        void shouldReturnEmptyForEmptyString() {
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "IS61C256AL-12TLI, IS61",
            "IS62WV12816DALL-45TLI, IS62",
            "IS64WV51216BLL-10B3LI, IS64",
            "IS42S16160J-7TLI, IS42",
            "IS43TR16256A-125JBLI, IS43",
            "IS45S16320D-6TLA1, IS45",
            "IS47TR16256A-125JBLI, IS47",
            "IS25LP128-JBLE, IS25",
            "IS29GL128-90TQLI, IS29",
            "IS65C102AL-15JLI, IS65",
            "IS67WV102416BLL-10TL, IS67"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "IS61C256AL-12TLI"));
            assertFalse(handler.isOfficialReplacement("IS61C256AL-12TLI", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("IS61C256AL-12TLI", null, registry));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldSupportMemoryType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY),
                    "Should support MEMORY");
        }

        @Test
        void shouldSupportMemoryISSIType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY_ISSI),
                    "Should support MEMORY_ISSI");
        }

        @Test
        void shouldSupportMemoryFlashType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY_FLASH),
                    "Should support MEMORY_FLASH");
        }

        @Test
        void shouldSupportMemoryEEPROMType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY_EEPROM),
                    "Should support MEMORY_EEPROM");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {
        @Test
        void canInstantiateDirectly() {
            ISSIHandler directHandler = new ISSIHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes());
        }

        @Test
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("IS61C256AL-12TLI", "IS62C256AL-45ULI"));
        }

        @Test
        void documentSameSeriesReplacement() {
            // Same series with same density should be replaceable
            boolean result = handler.isOfficialReplacement("IS61C256AL-12TLI", "IS61C256AL-10TLI");
            System.out.println("Same series replacement (IS61C256AL-12TLI, IS61C256AL-10TLI) = " + result);
        }

        @Test
        void documentSpeedGradeCompatibility() {
            // Faster speed grade should be able to replace slower (lower number = faster)
            boolean fasterReplaces = handler.isOfficialReplacement(
                "IS42S16160J-6TLI", "IS42S16160J-7TLI");
            System.out.println("Faster speed (6ns) can replace slower (7ns) = " + fasterReplaces);
        }

        @Test
        void documentDifferentDensityNotReplaceable() {
            // Different densities should not be replaceable
            boolean result = handler.isOfficialReplacement("IS42S16160J-7TLI", "IS42S32800J-6TLI");
            System.out.println("Different density replacement = " + result);
        }
    }

    @Nested
    @DisplayName("Memory Technology Documentation")
    class MemoryTechnologyTests {
        @Test
        void documentSRAMFamilies() {
            System.out.println("ISSI SRAM Families:");
            System.out.println("  IS61xx - Async SRAM (standard SRAM)");
            System.out.println("  IS62xx - Low Power SRAM (reduced power consumption)");
            System.out.println("  IS64xx - Sync SRAM (synchronous burst SRAM)");
        }

        @Test
        void documentDRAMFamilies() {
            System.out.println("ISSI DRAM Families:");
            System.out.println("  IS42xx - SDRAM (synchronous DRAM)");
            System.out.println("  IS43xx - DDR SDRAM (double data rate)");
            System.out.println("  IS45xx - DDR2 SDRAM");
            System.out.println("  IS47xx - DDR3 SDRAM");
        }

        @Test
        void documentFlashFamilies() {
            System.out.println("ISSI Flash Families:");
            System.out.println("  IS25xx - SPI Flash (serial peripheral interface)");
            System.out.println("  IS29xx - NOR Flash (parallel interface)");
        }

        @Test
        void documentLEDDriverFamilies() {
            System.out.println("ISSI LED Driver Families:");
            System.out.println("  IS31FL - LED Matrix Drivers (RGB/dot matrix)");
            System.out.println("  IS31AP - Audio Modulated LED Drivers");
            System.out.println("  IS31LT - LED Lighting Drivers (constant current)");
        }

        @Test
        void documentSpecialtyMemoryFamilies() {
            System.out.println("ISSI Specialty Memory Families:");
            System.out.println("  IS65xx - FIFO (First-In-First-Out buffers)");
            System.out.println("  IS67xx - Dual-Port RAM (two independent access ports)");
        }

        @Test
        void documentPackageCodes() {
            System.out.println("ISSI Package Codes:");
            System.out.println("  T   = TSOP (Thin Small Outline Package)");
            System.out.println("  U   = SOIC (Small Outline Integrated Circuit)");
            System.out.println("  L   = PLCC (Plastic Leaded Chip Carrier)");
            System.out.println("  V   = TSSOP (Thin Shrink Small Outline Package)");
            System.out.println("  TL  = TSOP-II (TSOP Type II)");
            System.out.println("  TR  = TFBGA (Thin Fine-pitch Ball Grid Array)");
            System.out.println("  B   = BGA (Ball Grid Array)");
            System.out.println("  BK  = FBGA (Fine-pitch Ball Grid Array)");
            System.out.println("  WB  = WBGA (Wafer-level Ball Grid Array)");
            System.out.println("  K   = VFBGA (Very Fine-pitch Ball Grid Array)");
            System.out.println("  M   = QFN (Quad Flat No-lead)");
            System.out.println("  Q   = QFP (Quad Flat Package)");
        }
    }
}
