package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.JinlingHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for JinlingHandler.
 *
 * Company: Shenzhen Jinling Electronics Co., Ltd. (JILN)
 * Founded: 2004
 * Website: szjiln.en.made-in-china.com, jilnconnector.com
 *
 * Tests cover two MPN encoding systems:
 * 1. Elprint position-based (273102NSNSUXT)
 * 2. JILN distributor format (321010MG0CBK00A02)
 *
 * Test organization:
 * - Family detection tests (13, 16, 17, 26, 27)
 * - Core extraction tests (series, package, pin count)
 * - Connector-specific helpers
 * - Edge cases
 * - Replacement logic
 *
 * IMPORTANT: Direct instantiation used instead of MPNUtils.getManufacturerHandler()
 * to avoid circular initialization and handler ordering bugs.
 */
class JinlingHandlerTest {

    private static JinlingHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation avoids MPNUtils ordering bugs
        handler = new JinlingHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Family 13 Tests - 1.27mm Pin Headers")
    class Family13Tests {

        @ParameterizedTest
        @ValueSource(strings = {
            "13100140ANSNSUT",  // 1 row, 40 pins
            "13100220ANSNSUT",  // 2 rows, 20 pins = 40 total
            "13100110ANSNSUT",  // 1 row, 10 pins
            "13100240ANSNSUT"   // 2 rows, 40 pins = 80 total
        })
        @DisplayName("Should match family 13 (1.27mm pin headers)")
        void shouldMatchFamily13(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JINLING, registry));
        }

        @Test
        @DisplayName("Should extract series as '13' for family 13")
        void shouldExtractSeries13() {
            assertEquals("13", handler.extractSeries("13100140ANSNSUT"));
            assertEquals("13", handler.extractSeries("13100220ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify correct pitch for family 13")
        void shouldGetPitchFamily13() {
            assertEquals("1.27", handler.getPitch("13100140ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify as male connector")
        void shouldGetGenderMale13() {
            assertEquals("Male", handler.getGender("13100140ANSNSUT"));
        }
    }

    @Nested
    @DisplayName("Family 16 Tests - 2.00mm Pin Headers")
    class Family16Tests {

        @ParameterizedTest
        @ValueSource(strings = {
            "16310140ANSNSUT",  // 1 row, 40 pins
            "16310220ANSNSUT",  // 2 rows, 20 pins = 40 total
            "16310110ANG3SUT"   // 1 row, 10 pins, 90°
        })
        @DisplayName("Should match family 16 (2.00mm pin headers)")
        void shouldMatchFamily16(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JINLING, registry));
        }

        @Test
        @DisplayName("Should extract series as '16' for family 16")
        void shouldExtractSeries16() {
            assertEquals("16", handler.extractSeries("16310140ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify correct pitch for family 16")
        void shouldGetPitchFamily16() {
            assertEquals("2.00", handler.getPitch("16310140ANSNSUT"));
        }
    }

    @Nested
    @DisplayName("Family 17 Tests - 2.54mm Pin Headers")
    class Family17Tests {

        @ParameterizedTest
        @ValueSource(strings = {
            "17310140ANSNSUT",  // 1 row, 40 pins
            "17310220ANSNSUT",  // 2 rows, 20 pins = 40 total
            "17310110ANG3SUT",  // 1 row, 10 pins, 90°
            "17310202ANSNSUT",  // 2 rows, 2 pins = 4 total
            "17310120ANSNSUT"   // 1 row, 20 pins
        })
        @DisplayName("Should match family 17 (2.54mm pin headers)")
        void shouldMatchFamily17(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JINLING, registry));
        }

        @Test
        @DisplayName("Should extract series as '17' for family 17")
        void shouldExtractSeries17() {
            assertEquals("17", handler.extractSeries("17310140ANSNSUT"));
            assertEquals("17", handler.extractSeries("17310220ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify correct pitch for family 17")
        void shouldGetPitchFamily17() {
            assertEquals("2.54", handler.getPitch("17310140ANSNSUT"));
            assertEquals("2.54", handler.getPitch("17310202ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify as male connector")
        void shouldGetGenderMale17() {
            assertEquals("Male", handler.getGender("17310140ANSNSUT"));
            assertEquals("Male", handler.getGender("17310202ANSNSUT"));
        }
    }

    @Nested
    @DisplayName("Family 26 Tests - 2.00mm Female Headers")
    class Family26Tests {

        @ParameterizedTest
        @ValueSource(strings = {
            "26310140ANSNSUT",  // 1 row, 40 pins
            "26310220ANSNSUT",  // 2 rows, 20 pins = 40 total
            "26310110ANG3SUT"   // 1 row, 10 pins, 90°
        })
        @DisplayName("Should match family 26 (2.00mm female headers)")
        void shouldMatchFamily26(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JINLING, registry));
        }

        @Test
        @DisplayName("Should extract series as '26' for family 26")
        void shouldExtractSeries26() {
            assertEquals("26", handler.extractSeries("26310140ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify correct pitch for family 26")
        void shouldGetPitchFamily26() {
            assertEquals("2.00", handler.getPitch("26310140ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify as female connector")
        void shouldGetGenderFemale26() {
            assertEquals("Female", handler.getGender("26310140ANSNSUT"));
        }
    }

    @Nested
    @DisplayName("Family 27 Tests - 2.54mm Female Headers")
    class Family27Tests {

        @ParameterizedTest
        @ValueSource(strings = {
            "27310140ANSNSUT",  // 1 row, 40 pins
            "27310220ANSNSUT",  // 2 rows, 20 pins = 40 total
            "27310110ANG3SUT",  // 1 row, 10 pins, 90°
            "27310202ANSNSUT",  // 2 rows, 2 pins = 4 total
            "27310240ANSNSUT",  // 2 rows, 40 pins = 80 total
            "27310102ANG3SUT"   // 1 row, 2 pins, 90°
        })
        @DisplayName("Should match family 27 (2.54mm female headers)")
        void shouldMatchFamily27(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JINLING, registry));
        }

        @Test
        @DisplayName("Should extract series as '27' for family 27")
        void shouldExtractSeries27() {
            assertEquals("27", handler.extractSeries("27310140ANSNSUT"));
            assertEquals("27", handler.extractSeries("27310202ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify correct pitch for family 27")
        void shouldGetPitchFamily27() {
            assertEquals("2.54", handler.getPitch("27310140ANSNSUT"));
            assertEquals("2.54", handler.getPitch("27310202ANSNSUT"));
        }

        @Test
        @DisplayName("Should identify as female connector")
        void shouldGetGenderFemale27() {
            assertEquals("Female", handler.getGender("27310140ANSNSUT"));
            assertEquals("Female", handler.getGender("27310202ANSNSUT"));
        }
    }

    @Nested
    @DisplayName("JILN Distributor Format Tests")
    class DistributorFormatTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "321010MG0CBK00A02",  // IDC 2x5P 2.54mm SMT Gold (from LCSC C601966)
            "12251140CNG0S115001", // Pin Header 1x40P 2.54mm THT (from LCSC C429959)
            "12251220ANG0S115001", // Pin Header 2x20P 2.54mm THT (from LCSC C429965)
            "22850120ANG1SYA01",   // Female Header 2.54mm (from LCSC C429947)
            "22850102ANG1SYA02",   // Female Header 2.54mm (from LCSC C429966)
            "13201140CNG0S087004"  // Pin Header 1.27mm (from LCSC C429955)
        })
        @DisplayName("Should match JILN distributor format MPNs")
        void shouldMatchDistributorFormat(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR_JINLING, registry));
        }

        @ParameterizedTest
        @CsvSource({
            "321010MG0CBK00A02, 32",
            "12251140CNG0S115001, 12",
            "22850120ANG1SYA01, 22",
            "13201140CNG0S087004, 13"
        })
        @DisplayName("Should extract series from distributor format")
        void shouldExtractSeriesDistributor(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        @DisplayName("Should detect SMT from distributor format")
        void shouldDetectSMT() {
            String mpn = "321010MG0CBK00A02"; // IDC SMT
            assertEquals("SMT", handler.extractPackageCode(mpn));
            assertEquals("SMT", handler.getMountingType(mpn));
        }

        @Test
        @DisplayName("Should detect THT from distributor format")
        void shouldDetectTHT() {
            String mpn = "12251140CNG0S115001"; // Pin Header THT
            assertEquals("Through-Hole", handler.extractPackageCode(mpn));
            assertEquals("THT", handler.getMountingType(mpn));
        }
    }

    @Nested
    @DisplayName("Series Extraction Tests")
    class SeriesExtractionTests {

        @ParameterizedTest
        @CsvSource({
            "13100140ANSNSUT, 13",
            "16310140ANSNSUT, 16",
            "17310140ANSNSUT, 17",
            "26310140ANSNSUT, 26",
            "27310140ANSNSUT, 27",
            "27310202ANSNSUT, 27",
            "321010MG0CBK00A02, 32"
        })
        @DisplayName("Should extract correct series/family code")
        void shouldExtractSeriesCorrectly(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        @DisplayName("Should return empty string for null MPN")
        void shouldReturnEmptyForNullSeries() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction Tests")
    class PackageCodeTests {

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNMUT, SMT",
            "27310202ANSNSUT, Straight THT",
            "27310202ANSNRUT, Right Angle THT",
            "27310202ANSNWUT, Straddle THT",
            "27310202ANSNZUT, Right Angle SMT"
        })
        @DisplayName("Should decode connector type from position 12")
        void shouldDecodeConnectorType(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn));
        }

        @Test
        @DisplayName("Should handle distributor format package codes")
        void shouldHandleDistributorPackage() {
            assertEquals("SMT", handler.extractPackageCode("321010MG0CBK00A02"));
            assertEquals("Through-Hole", handler.extractPackageCode("12251140CNG0S115001"));
        }
    }

    @Nested
    @DisplayName("Pin Count Extraction Tests")
    class PinCountTests {

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNSUT, 4",    // 2 rows × 2 pins = 4
            "27310240ANSNSUT, 80",   // 2 rows × 40 pins = 80
            "27310140ANSNSUT, 40",   // 1 row × 40 pins = 40
            "17310220ANSNSUT, 40",   // 2 rows × 20 pins = 40
            "17310110ANG3SUT, 10",   // 1 row × 10 pins = 10
            "13100240ANSNSUT, 80"    // 2 rows × 40 pins = 80
        })
        @DisplayName("Should calculate pin count as rows × pins/row")
        void shouldCalculatePinCount(String mpn, int expectedPins) {
            assertEquals(expectedPins, handler.extractPinCount(mpn));
        }

        @Test
        @DisplayName("Should extract pin count from distributor format")
        void shouldExtractPinCountDistributor() {
            assertEquals(10, handler.extractPinCount("321010MG0CBK00A02")); // 2x5=10
            assertEquals(40, handler.extractPinCount("12251140CNG0S115001")); // 40 pins
            assertEquals(20, handler.extractPinCount("12251220ANG0S115001")); // 2x20=20? or 20?
        }

        @Test
        @DisplayName("Should return 0 for invalid pin count")
        void shouldReturn0ForInvalidPinCount() {
            assertEquals(0, handler.extractPinCount(null));
            assertEquals(0, handler.extractPinCount(""));
            assertEquals(0, handler.extractPinCount("INVALID"));
        }
    }

    @Nested
    @DisplayName("Helper Methods Tests")
    class HelperMethodsTests {

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNSUT, 2",
            "27310140ANSNSUT, 1",
            "17310202ANSNSUT, 2",
            "27310110ANG3SUT, 1"
        })
        @DisplayName("Should get row count")
        void shouldGetRowCount(String mpn, int expectedRows) {
            assertEquals(expectedRows, handler.getRowCount(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "13100140ANSNSUT, 1.27",
            "16310140ANSNSUT, 2.00",
            "17310140ANSNSUT, 2.54",
            "26310140ANSNSUT, 2.00",
            "27310140ANSNSUT, 2.54",
            "321010MG0CBK00A02, 2.54"
        })
        @DisplayName("Should get correct pitch for each family")
        void shouldGetPitch(String mpn, String expectedPitch) {
            assertEquals(expectedPitch, handler.getPitch(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNSUT, Straight",
            "27310202ANSNRUT, Right Angle",
            "27310202ANSNZUT, Right Angle"
        })
        @DisplayName("Should get orientation")
        void shouldGetOrientation(String mpn, String expectedOrientation) {
            assertEquals(expectedOrientation, handler.getOrientation(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "13100140ANSNSUT, Male",
            "16310140ANSNSUT, Male",
            "17310140ANSNSUT, Male",
            "26310140ANSNSUT, Female",
            "27310140ANSNSUT, Female",
            "22850102ANG1SYA02, Female"
        })
        @DisplayName("Should get gender")
        void shouldGetGender(String mpn, String expectedGender) {
            assertEquals(expectedGender, handler.getGender(mpn));
        }

        @Test
        @DisplayName("Should get plastics height")
        void shouldGetPlasticsHeight() {
            assertEquals(3.1, handler.getPlasticsHeight("27310202ANSNSUT"), 0.01);
            assertEquals(3.5, handler.getPlasticsHeight("27350202ANSNSUT"), 0.01);
            assertEquals(8.5, handler.getPlasticsHeight("27850202ANSNSUT"), 0.01);
        }

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNSUT, PBT",
            "27310202BNSNSUT, PA66",
            "27310202CNSNSUT, PA6T",
            "27310202DNSNSUT, PA46",
            "27310202ENSNSUT, PA9T",
            "27310202FNSNSUT, LCP"
        })
        @DisplayName("Should get insulator material")
        void shouldGetInsulatorMaterial(String mpn, String expectedMaterial) {
            assertEquals(expectedMaterial, handler.getInsulatorMaterial(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNSUT, Tin",
            "27310202ANG0SUT, Gold Flash",
            "27310202ANG1SUT, 3µin Gold",
            "27310202ANG3SUT, 10µin Gold",
            "27310202ANG5SUT, 30µin Gold",
            "27310202ANS0SUT, Gold Flash/Tin"
        })
        @DisplayName("Should get contact plating")
        void shouldGetContactPlating(String mpn, String expectedPlating) {
            assertEquals(expectedPlating, handler.getContactPlating(mpn));
        }

        @Test
        @DisplayName("Should detect post presence")
        void shouldDetectPost() {
            assertTrue(handler.hasPost("27310202AWSNSUT"));  // Position 9: W = with post
            assertFalse(handler.hasPost("27310202ANSNSUT")); // Position 9: N = no post
        }

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNSUT, Tube",
            "27310202ANSNSUP, Tube + Cap",
            "27310202ANSNSUR, Reel",
            "27310202ANSNSUO, PE Bag",
            "27310202ANSNSUA, Tray"
        })
        @DisplayName("Should get packing type")
        void shouldGetPacking(String mpn, String expectedPacking) {
            assertEquals(expectedPacking, handler.getPacking(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "27310202ANSNSUT, U-Type",
            "27310202ANSNSST, Straight",
            "27310202ANSNSRT, Right Angle"
        })
        @DisplayName("Should get contact type")
        void shouldGetContactType(String mpn, String expectedContactType) {
            assertEquals(expectedContactType, handler.getContactType(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "13100140ANSNSUT, Pin Header",
            "16310140ANSNSUT, Pin Header",
            "17310140ANSNSUT, Pin Header",
            "26310140ANSNSUT, Female Header",
            "27310140ANSNSUT, Female Header",
            "321010MG0CBK00A02, IDC Connector"
        })
        @DisplayName("Should get category")
        void shouldGetCategory(String mpn, String expectedCategory) {
            assertEquals(expectedCategory, handler.getCategory(mpn));
        }
    }

    @Nested
    @DisplayName("Replacement Logic Tests")
    class ReplacementTests {

        @Test
        @DisplayName("Should accept replacement with same family and pins")
        void shouldAcceptSameFamilySamePins() {
            // Same family (27), same pins (4), different plating
            assertTrue(handler.isOfficialReplacement("27310202ANSNSUT", "27310202ANG3SUT"));
        }

        @Test
        @DisplayName("Should reject replacement with different family")
        void shouldRejectDifferentFamily() {
            // Different families (17 vs 27)
            assertFalse(handler.isOfficialReplacement("17310202ANSNSUT", "27310202ANSNSUT"));
        }

        @Test
        @DisplayName("Should reject replacement with different pin count")
        void shouldRejectDifferentPinCount() {
            // Same family (27), different pins (4 vs 80)
            assertFalse(handler.isOfficialReplacement("27310202ANSNSUT", "27310240ANSNSUT"));
        }

        @Test
        @DisplayName("Should accept replacement with compatible mounting")
        void shouldAcceptCompatibleMounting() {
            // Same family, same pins, both THT
            assertTrue(handler.isOfficialReplacement("27310202ANSNSUT", "27310202ANSNRUT"));
        }

        @Test
        @DisplayName("Should reject replacement with incompatible mounting")
        void shouldRejectIncompatibleMounting() {
            // Same family, same pins, but SMT vs THT
            assertFalse(handler.isOfficialReplacement("27310202ANSNMUT", "27310202ANSNSUT"));
        }

        @Test
        @DisplayName("Should handle null MPNs")
        void shouldHandleNullMPNs() {
            assertFalse(handler.isOfficialReplacement(null, "27310202ANSNSUT"));
            assertFalse(handler.isOfficialReplacement("27310202ANSNSUT", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPNs gracefully")
        void shouldHandleNullMPNs() {
            assertFalse(handler.matches(null, ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals(0, handler.extractPinCount(null));
        }

        @Test
        @DisplayName("Should handle empty MPNs gracefully")
        void shouldHandleEmptyMPNs() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals(0, handler.extractPinCount(""));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("27310202ansnsut", ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches("27310202ANSNSUT", ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches("27310202AnSnSuT", ComponentType.CONNECTOR, registry));
        }

        @Test
        @DisplayName("Should not match other manufacturers")
        void shouldNotMatchOtherManufacturers() {
            // Molex
            assertFalse(handler.matches("43045-0200", ComponentType.CONNECTOR, registry));
            // JST
            assertFalse(handler.matches("PHR-2", ComponentType.CONNECTOR, registry));
            // Sullins
            assertFalse(handler.matches("PPPC081LFBN-RC", ComponentType.CONNECTOR, registry));
        }
    }

    @Nested
    @DisplayName("Supported Types Tests")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support CONNECTOR type")
        void shouldSupportConnector() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.CONNECTOR));
        }

        @Test
        @DisplayName("Should support CONNECTOR_JINLING type")
        void shouldSupportConnectorJinling() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.CONNECTOR_JINLING));
        }

        @Test
        @DisplayName("Should have exactly 2 supported types")
        void shouldHaveCorrectNumberOfTypes() {
            assertEquals(2, handler.getSupportedTypes().size());
        }
    }

    @Nested
    @DisplayName("Handler Initialization Tests")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Should initialize without errors")
        void shouldInitializeWithoutErrors() {
            assertNotNull(handler);
            assertNotNull(registry);
        }

        @Test
        @DisplayName("Should have patterns registered")
        void shouldHavePatternsRegistered() {
            // Verify that patterns are registered by testing matches
            assertTrue(handler.matches("27310202ANSNSUT", ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches("17310140ANSNSUT", ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches("321010MG0CBK00A02", ComponentType.CONNECTOR, registry));
        }
    }
}
