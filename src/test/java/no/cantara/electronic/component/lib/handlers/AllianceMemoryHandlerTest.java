package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AllianceMemoryHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for AllianceMemoryHandler.
 *
 * Alliance Memory Part Number Structure:
 * - AS6C series: Async SRAM (AS6C1008, AS6C4008, AS6C62256)
 * - AS7C series: High-speed SRAM (AS7C1024, AS7C256)
 * - AS4C series: SDRAM and DDR SDRAM (AS4C4M16SA, AS4C16M16SA, AS4C256M16D3)
 * - AS29LV series: NOR Flash
 *
 * Example formats:
 * - AS6C4008-55PCN (Async SRAM, 55ns, PDIP package, Commercial, Lead-free)
 * - AS7C1024A-12JCN (High-speed SRAM, 12ns, PLCC package, Commercial, Lead-free)
 * - AS4C4M16SA-6TIN (SDRAM, 6ns, TSOP package, Industrial, Lead-free)
 * - AS4C256M16D3A-12BCN (DDR3, 12ns, BGA package, Commercial, Lead-free)
 *
 * Package codes: P=PDIP, T=TSOP, B=BGA, J=PLCC, S=SOIC, V=TSSOP
 * Temperature grades: C=Commercial (0-70), I=Industrial (-40 to +85), A=Automotive (-40 to +125)
 */
class AllianceMemoryHandlerTest {

    private static AllianceMemoryHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AllianceMemoryHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("AS6C Async SRAM Detection")
    class AS6CAsyncSRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "AS6C1008-55PCN",
            "AS6C4008-55PCN",
            "AS6C4008-55TIN",
            "AS6C62256-70PCN",
            "AS6C1008-55SIN",
            "AS6C2016-55ZIN",
            "AS6C6264-55TIN"
        })
        void shouldDetectAS6CAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                mpn + " should match MEMORY type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "AS6C1008-55PCN",
            "AS6C4008-55TIN"
        })
        void shouldNotDetectAS6CAsFlash(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                mpn + " should NOT match MEMORY_FLASH type (SRAM is not Flash)");
        }

        @Test
        void documentAS6CVariants() {
            System.out.println("Alliance Memory AS6C (Async SRAM) Variants:");
            System.out.println("  AS6C1008 - 1Mbit (128K x 8) async SRAM");
            System.out.println("  AS6C4008 - 4Mbit (512K x 8) async SRAM");
            System.out.println("  AS6C62256 - 256Kbit (32K x 8) async SRAM");
            System.out.println("  AS6C6264 - 64Kbit (8K x 8) async SRAM");
            System.out.println("  AS6C2016 - 16Kbit async SRAM");
        }
    }

    @Nested
    @DisplayName("AS7C High-Speed SRAM Detection")
    class AS7CHighSpeedSRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "AS7C1024A-12JCN",
            "AS7C256-15JCN",
            "AS7C1024B-12TCN",
            "AS7C256A-10TCN",
            "AS7C4096A-12TCN"
        })
        void shouldDetectAS7CAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                mpn + " should match MEMORY type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "AS7C1024A-12JCN",
            "AS7C256-15JCN"
        })
        void shouldNotDetectAS7CAsFlash(String mpn) {
            assertFalse(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                mpn + " should NOT match MEMORY_FLASH type (SRAM is not Flash)");
        }

        @Test
        void documentAS7CVariants() {
            System.out.println("Alliance Memory AS7C (High-Speed SRAM) Variants:");
            System.out.println("  AS7C1024 - 1Mbit high-speed SRAM");
            System.out.println("  AS7C256 - 256Kbit high-speed SRAM");
            System.out.println("  AS7C4096 - 4Mbit high-speed SRAM");
        }
    }

    @Nested
    @DisplayName("AS4C SDRAM Detection")
    class AS4CSDRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "AS4C4M16SA-6TIN",
            "AS4C16M16SA-6TCN",
            "AS4C8M16SA-7TCN",
            "AS4C32M16SA-6TIN"
        })
        void shouldDetectAS4CSDRAMAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                mpn + " should match MEMORY type");
        }

        @Test
        void documentAS4CSDRAMVariants() {
            System.out.println("Alliance Memory AS4C SDRAM Variants:");
            System.out.println("  AS4C4M16SA - 64Mbit (4M x 16) SDRAM");
            System.out.println("  AS4C8M16SA - 128Mbit (8M x 16) SDRAM");
            System.out.println("  AS4C16M16SA - 256Mbit (16M x 16) SDRAM");
            System.out.println("  AS4C32M16SA - 512Mbit (32M x 16) SDRAM");
        }
    }

    @Nested
    @DisplayName("AS4C DDR SDRAM Detection")
    class AS4CDDRSDRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "AS4C256M16D3A-12BCN",
            "AS4C128M16D3A-12BCN",
            "AS4C512M16D3A-12BIN",
            "AS4C64M16D2A-25TCN",
            "AS4C32M16D1A-5TCN"
        })
        void shouldDetectAS4CDDRAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                mpn + " should match MEMORY type");
        }

        @Test
        void documentAS4CDDRVariants() {
            System.out.println("Alliance Memory AS4C DDR SDRAM Variants:");
            System.out.println("  AS4CxxxD1 - DDR1 SDRAM");
            System.out.println("  AS4CxxxD2 - DDR2 SDRAM");
            System.out.println("  AS4CxxxD3 - DDR3 SDRAM");
            System.out.println("  AS4C256M16D3A - 4Gbit (256M x 16) DDR3");
            System.out.println("  AS4C512M16D3A - 8Gbit (512M x 16) DDR3");
        }
    }

    @Nested
    @DisplayName("AS29LV Flash Memory Detection")
    class AS29LVFlashTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "AS29LV160B-90TC",
            "AS29LV800B-70TC",
            "AS29LV400B-90TC",
            "AS29LV320B-90TC",
            "AS29LV640B-90TC"
        })
        void shouldDetectAS29LVAsMemory(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                mpn + " should match MEMORY type");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "AS29LV160B-90TC",
            "AS29LV800B-70TC",
            "AS29LV400B-90TC"
        })
        void shouldDetectAS29LVAsFlash(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_FLASH, registry),
                mpn + " should match MEMORY_FLASH type");
        }

        @Test
        void documentAS29LVVariants() {
            System.out.println("Alliance Memory AS29LV (NOR Flash) Variants:");
            System.out.println("  AS29LV160B - 16Mbit NOR Flash");
            System.out.println("  AS29LV320B - 32Mbit NOR Flash");
            System.out.println("  AS29LV640B - 64Mbit NOR Flash");
            System.out.println("  AS29LV800B - 8Mbit NOR Flash");
            System.out.println("  AS29LV400B - 4Mbit NOR Flash");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "AS6C4008-55PCN, PDIP",
            "AS6C4008-55TIN, TSOP",
            "AS7C1024A-12JCN, PLCC",
            "AS4C256M16D3A-12BCN, BGA",
            "AS6C1008-55SIN, SOIC",
            "AS6C2016-55VCN, TSSOP"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                "Package extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNullMPN() {
            assertEquals("", handler.extractPackageCode(null));
        }

        @Test
        void shouldReturnEmptyForEmptyMPN() {
            assertEquals("", handler.extractPackageCode(""));
        }

        @Test
        void shouldReturnEmptyForMPNWithoutHyphen() {
            assertEquals("", handler.extractPackageCode("AS6C4008"));
        }

        @Test
        void documentPackageCodes() {
            System.out.println("Alliance Memory Package Codes:");
            System.out.println("  P = PDIP (Plastic Dual In-line Package)");
            System.out.println("  T = TSOP (Thin Small Outline Package)");
            System.out.println("  B = BGA (Ball Grid Array)");
            System.out.println("  J = PLCC (Plastic Leaded Chip Carrier)");
            System.out.println("  S = SOIC (Small Outline IC)");
            System.out.println("  V = TSSOP (Thin Shrink Small Outline Package)");
            System.out.println("  Z = FBGA (Fine-pitch Ball Grid Array)");
            System.out.println("  W = WSON (Very Very Thin Small Outline No-lead)");
            System.out.println("  Q = QFN (Quad Flat No-lead)");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "AS6C4008-55PCN, AS6C",
            "AS6C1008-55TIN, AS6C",
            "AS6C62256-70PCN, AS6C",
            "AS7C1024A-12JCN, AS7C",
            "AS7C256-15JCN, AS7C",
            "AS4C4M16SA-6TIN, AS4C",
            "AS4C256M16D3A-12BCN, AS4C",
            "AS29LV160B-90TC, AS29LV",
            "AS29LV800B-70TC, AS29LV"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                "Series extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNullMPN() {
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        void shouldReturnEmptyForEmptyMPN() {
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        void shouldReturnEmptyForNonAllianceMPN() {
            assertEquals("", handler.extractSeries("MT41K256M16TW-107"));
            assertEquals("", handler.extractSeries("IS61WV25616BLL"));
        }
    }

    @Nested
    @DisplayName("Density Extraction")
    class DensityExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "AS6C4008-55PCN, 4008",
            "AS6C1008-55TIN, 1008",
            "AS6C62256-70PCN, 62256",
            "AS6C6264-55TIN, 6264",
            "AS7C1024A-12JCN, 1024",
            "AS7C256-15JCN, 256",
            "AS4C4M16SA-6TIN, 4M16",
            "AS4C16M16SA-6TCN, 16M16",
            "AS4C256M16D3A-12BCN, 256M16",
            "AS29LV160B-90TC, 160",
            "AS29LV800B-70TC, 800"
        })
        void shouldExtractDensity(String mpn, String expected) {
            assertEquals(expected, handler.extractDensity(mpn),
                "Density extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForNullMPN() {
            assertEquals("", handler.extractDensity(null));
        }

        @Test
        void shouldReturnEmptyForEmptyMPN() {
            assertEquals("", handler.extractDensity(""));
        }

        @Test
        void documentDensityFormat() {
            System.out.println("Alliance Memory Density Format:");
            System.out.println("  AS6C/AS7C: Numeric density (4008 = 4Mbit, 62256 = 256Kbit)");
            System.out.println("  AS4C: [depth]M[width] format (4M16 = 4M x 16 bits = 64Mbit)");
            System.out.println("  AS29LV: Numeric density in Mbit (160 = 16Mbit)");
        }
    }

    @Nested
    @DisplayName("Speed Grade Extraction")
    class SpeedGradeExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "AS6C4008-55PCN, 55",
            "AS6C4008-45TIN, 45",
            "AS7C1024A-12JCN, 12",
            "AS7C256-15JCN, 15",
            "AS4C4M16SA-6TIN, 6",
            "AS4C256M16D3A-12BCN, 12",
            "AS29LV160B-90TC, 90",
            "AS29LV800B-70TC, 70"
        })
        void shouldExtractSpeedGrade(String mpn, String expected) {
            assertEquals(expected, handler.extractSpeedGrade(mpn),
                "Speed grade extraction for " + mpn);
        }

        @Test
        void shouldReturnEmptyForMPNWithoutHyphen() {
            assertEquals("", handler.extractSpeedGrade("AS6C4008"));
        }

        @Test
        void shouldReturnEmptyForNullMPN() {
            assertEquals("", handler.extractSpeedGrade(null));
        }

        @Test
        void documentSpeedGrades() {
            System.out.println("Alliance Memory Speed Grades (access time in nanoseconds):");
            System.out.println("  SRAM: 10ns, 12ns, 15ns, 20ns, 25ns, 45ns, 55ns, 70ns");
            System.out.println("  SDRAM: 6ns, 7ns, 8ns, 10ns");
            System.out.println("  DDR3: 12ns (CL=11), 15ns (CL=11)");
            System.out.println("  Flash: 70ns, 90ns, 120ns");
        }
    }

    @Nested
    @DisplayName("Temperature Grade Extraction")
    class TemperatureGradeExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "AS6C4008-55PCN, C",
            "AS6C4008-55TIN, I",
            "AS4C256M16D3A-12BCN, C",
            "AS4C256M16D3A-12BIN, I"
        })
        void shouldExtractTemperatureGrade(String mpn, String expected) {
            assertEquals(expected, handler.extractTemperatureGrade(mpn),
                "Temperature grade extraction for " + mpn);
        }

        @Test
        void documentTemperatureGrades() {
            System.out.println("Alliance Memory Temperature Grades:");
            System.out.println("  C = Commercial (0C to +70C)");
            System.out.println("  I = Industrial (-40C to +85C)");
            System.out.println("  A = Automotive (-40C to +125C)");
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {
        @Test
        void shouldAllowSamePartReplacement() {
            assertTrue(handler.isOfficialReplacement(
                "AS6C4008-55PCN", "AS6C4008-55PCN"),
                "Same part should be replacement");
        }

        @Test
        void shouldAllowFasterSpeedReplacement() {
            // 45ns can replace 55ns (faster can replace slower)
            assertTrue(handler.isOfficialReplacement(
                "AS6C4008-45TIN", "AS6C4008-55TIN"),
                "Faster speed (45ns) should be able to replace slower (55ns)");
        }

        @Test
        void shouldNotAllowSlowerSpeedReplacement() {
            // 55ns cannot replace 45ns (slower cannot replace faster)
            assertFalse(handler.isOfficialReplacement(
                "AS6C4008-55TIN", "AS6C4008-45TIN"),
                "Slower speed (55ns) should NOT be able to replace faster (45ns)");
        }

        @Test
        void shouldAllowWiderTempRangeReplacement() {
            // Industrial (-40 to +85) can replace Commercial (0 to +70)
            assertTrue(handler.isOfficialReplacement(
                "AS6C4008-55TIN", "AS6C4008-55TCN"),
                "Industrial grade should be able to replace Commercial grade");
        }

        @Test
        void shouldNotAllowNarrowerTempRangeReplacement() {
            // Commercial (0 to +70) cannot replace Industrial (-40 to +85)
            assertFalse(handler.isOfficialReplacement(
                "AS6C4008-55TCN", "AS6C4008-55TIN"),
                "Commercial grade should NOT be able to replace Industrial grade");
        }

        @Test
        void shouldNotAllowDifferentSeriesReplacement() {
            assertFalse(handler.isOfficialReplacement(
                "AS6C4008-55PCN", "AS7C4008-55PCN"),
                "Different series (AS6C vs AS7C) should not be replaceable");
        }

        @Test
        void shouldNotAllowDifferentDensityReplacement() {
            assertFalse(handler.isOfficialReplacement(
                "AS6C4008-55PCN", "AS6C1008-55PCN"),
                "Different density (4008 vs 1008) should not be replaceable");
        }

        @Test
        void shouldNotAllowDifferentMemoryTypeReplacement() {
            assertFalse(handler.isOfficialReplacement(
                "AS6C4008-55PCN", "AS29LV160B-90TC"),
                "Different memory types (SRAM vs Flash) should not be replaceable");
        }

        @Test
        void shouldAllowDifferentPackageReplacement() {
            // Same density, same speed, different package should be replacement
            // Package is a design choice, not electrical compatibility
            assertTrue(handler.isOfficialReplacement(
                "AS6C4008-55TCN", "AS6C4008-55PCN"),
                "Same part in different package should be replacement (with design consideration)");
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "AS6C4008-55PCN"));
            assertFalse(handler.isOfficialReplacement("AS6C4008-55PCN", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractDensity(null));
            assertEquals("", handler.extractSpeedGrade(null));
            assertEquals("", handler.extractTemperatureGrade(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.MEMORY, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractDensity(""));
            assertEquals("", handler.extractSpeedGrade(""));
            assertEquals("", handler.extractTemperatureGrade(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("AS6C4008-55PCN", null, registry));
        }

        @Test
        void shouldNotMatchOtherManufacturerMPNs() {
            // Should not match Micron parts
            assertFalse(handler.matches("MT41K256M16TW-107", ComponentType.MEMORY, registry));
            // Should not match ISSI parts
            assertFalse(handler.matches("IS61WV25616BLL-10TLI", ComponentType.MEMORY, registry));
            // Should not match Winbond parts
            assertFalse(handler.matches("W25Q32JVSSIQ", ComponentType.MEMORY, registry));
        }

        @Test
        void shouldHandleCaseInsensitively() {
            assertTrue(handler.matches("as6c4008-55pcn", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("AS6C4008-55PCN", ComponentType.MEMORY, registry));
            assertTrue(handler.matches("As6c4008-55Pcn", ComponentType.MEMORY, registry));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldSupportMemoryType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY),
                "Should support MEMORY type");
        }

        @Test
        void shouldSupportMemoryFlashType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.MEMORY_FLASH),
                "Should support MEMORY_FLASH type");
        }

        @Test
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () ->
                types.add(ComponentType.IC),
                "getSupportedTypes() should return immutable Set.of()");
        }

        @Test
        void shouldContainExpectedTypeCount() {
            var types = handler.getSupportedTypes();
            assertEquals(2, types.size(), "Should support exactly 2 types");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {
        @Test
        void canInstantiateDirectly() {
            AllianceMemoryHandler directHandler = new AllianceMemoryHandler();
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
    @DisplayName("Product Family Documentation")
    class ProductFamilyDocumentationTests {
        @Test
        void documentSRAMFamilies() {
            System.out.println("Alliance Memory SRAM Families:");
            System.out.println("  AS6C - Async SRAM");
            System.out.println("    AS6C62256 - 256Kbit (32K x 8)");
            System.out.println("    AS6C6264 - 64Kbit (8K x 8)");
            System.out.println("    AS6C1008 - 1Mbit (128K x 8)");
            System.out.println("    AS6C2016 - 16Kbit");
            System.out.println("    AS6C4008 - 4Mbit (512K x 8)");
            System.out.println("  AS7C - High-Speed SRAM");
            System.out.println("    AS7C256 - 256Kbit");
            System.out.println("    AS7C1024 - 1Mbit");
            System.out.println("    AS7C4096 - 4Mbit");
        }

        @Test
        void documentDRAMFamilies() {
            System.out.println("Alliance Memory DRAM Families:");
            System.out.println("  AS4CxxxSA - SDRAM");
            System.out.println("    AS4C4M16SA - 64Mbit (4M x 16)");
            System.out.println("    AS4C8M16SA - 128Mbit (8M x 16)");
            System.out.println("    AS4C16M16SA - 256Mbit (16M x 16)");
            System.out.println("    AS4C32M16SA - 512Mbit (32M x 16)");
            System.out.println("  AS4CxxxD1 - DDR1 SDRAM");
            System.out.println("  AS4CxxxD2 - DDR2 SDRAM");
            System.out.println("  AS4CxxxD3 - DDR3 SDRAM");
            System.out.println("    AS4C128M16D3 - 2Gbit (128M x 16)");
            System.out.println("    AS4C256M16D3 - 4Gbit (256M x 16)");
            System.out.println("    AS4C512M16D3 - 8Gbit (512M x 16)");
        }

        @Test
        void documentFlashFamilies() {
            System.out.println("Alliance Memory Flash Families:");
            System.out.println("  AS29LV - NOR Flash");
            System.out.println("    AS29LV160B - 16Mbit");
            System.out.println("    AS29LV320B - 32Mbit");
            System.out.println("    AS29LV640B - 64Mbit");
            System.out.println("    AS29LV800B - 8Mbit");
            System.out.println("    AS29LV400B - 4Mbit");
        }

        @Test
        void documentPartNumberStructure() {
            System.out.println("Alliance Memory Part Number Structure:");
            System.out.println("  AS6C4008-55PCN");
            System.out.println("  |  |   |  |||");
            System.out.println("  |  |   |  ||+-- N = Lead-free (RoHS)");
            System.out.println("  |  |   |  |+--- C/I/A = Temp Grade (Commercial/Industrial/Automotive)");
            System.out.println("  |  |   |  +---- P = Package (P=PDIP, T=TSOP, B=BGA, J=PLCC)");
            System.out.println("  |  |   +------- 55 = Speed Grade (access time in ns)");
            System.out.println("  |  +----------- 4008 = Density (4Mbit, organized as 512K x 8)");
            System.out.println("  +-------------- AS6C = Series (Async SRAM)");
        }
    }

    @Nested
    @DisplayName("Low Power SRAM Variants")
    class LowPowerSRAMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "AS6C1008-55PCN",
            "AS6C4008L-55PCN",
            "AS6C62256-70LPI"
        })
        void shouldDetectLowPowerVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry),
                mpn + " should match MEMORY type");
        }

        @Test
        void documentLowPowerFeatures() {
            System.out.println("Alliance Memory Low Power SRAM Features:");
            System.out.println("  - Ultra-low standby current");
            System.out.println("  - Battery backup capability");
            System.out.println("  - Often indicated by 'L' suffix before density");
            System.out.println("  - Or by specific series variants");
        }
    }

    @Nested
    @DisplayName("DDR Generation Detection")
    class DDRGenerationTests {
        @Test
        void shouldDetectDDR1() {
            String mpn = "AS4C32M16D1A-5TCN";
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry));
            assertEquals("AS4C", handler.extractSeries(mpn));
            assertEquals("32M16", handler.extractDensity(mpn));
        }

        @Test
        void shouldDetectDDR2() {
            String mpn = "AS4C64M16D2A-25TCN";
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry));
            assertEquals("AS4C", handler.extractSeries(mpn));
            assertEquals("64M16", handler.extractDensity(mpn));
        }

        @Test
        void shouldDetectDDR3() {
            String mpn = "AS4C256M16D3A-12BCN";
            assertTrue(handler.matches(mpn, ComponentType.MEMORY, registry));
            assertEquals("AS4C", handler.extractSeries(mpn));
            assertEquals("256M16", handler.extractDensity(mpn));
        }

        @Test
        void documentDDRGenerationIndicators() {
            System.out.println("DDR Generation Detection:");
            System.out.println("  D1 suffix = DDR1 (e.g., AS4C32M16D1A)");
            System.out.println("  D2 suffix = DDR2 (e.g., AS4C64M16D2A)");
            System.out.println("  D3 suffix = DDR3 (e.g., AS4C256M16D3A)");
            System.out.println("  SA suffix = SDRAM (e.g., AS4C4M16SA)");
        }
    }
}
