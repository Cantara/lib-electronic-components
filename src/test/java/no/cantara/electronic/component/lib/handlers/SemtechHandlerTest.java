package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SemtechHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for SemtechHandler.
 *
 * Tests coverage for:
 * - LoRa Transceivers (SX127x, SX126x, LR11xx, LLCC68)
 * - LoRa Gateway Chips (SX130x)
 * - ESD Protection (RClamp, SLVU, SM series)
 * - Signal Integrity (ClearEdge GN series)
 * - Power Management (SC, SY, TC series)
 */
class SemtechHandlerTest {

    private static SemtechHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SemtechHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("LoRa Transceiver Detection - SX127x Series (Legacy)")
    class LoRaSX127xTests {

        @ParameterizedTest
        @DisplayName("Should detect SX1276 LoRa transceivers")
        @ValueSource(strings = {
            "SX1276",
            "SX1276IMLTRT",
            "SX1276-IMLTR",
            "SX1276IMLT"
        })
        void shouldDetectSX1276(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect SX1278 LoRa transceivers")
        @ValueSource(strings = {
            "SX1278",
            "SX1278IMLTRT",
            "SX1278-QFN"
        })
        void shouldDetectSX1278(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect SX1279 LoRa transceivers")
        @ValueSource(strings = {
            "SX1279",
            "SX1279IMLTRT"
        })
        void shouldDetectSX1279(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("LoRa Transceiver Detection - SX126x Series (Newer)")
    class LoRaSX126xTests {

        @ParameterizedTest
        @DisplayName("Should detect SX1261 low power transceiver")
        @ValueSource(strings = {
            "SX1261",
            "SX1261IMLTRT",
            "SX1261-QFN24"
        })
        void shouldDetectSX1261(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect SX1262 high power transceiver")
        @ValueSource(strings = {
            "SX1262",
            "SX1262IMLTRT",
            "SX1262-IMLTR"
        })
        void shouldDetectSX1262(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect SX1268 LoRa transceiver")
        @ValueSource(strings = {
            "SX1268",
            "SX1268IMLTRT"
        })
        void shouldDetectSX1268(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("LoRa Gateway Chips - SX130x Series")
    class LoRaSX130xTests {

        @ParameterizedTest
        @DisplayName("Should detect SX1301 gateway baseband processor")
        @ValueSource(strings = {
            "SX1301",
            "SX1301IMLTRT"
        })
        void shouldDetectSX1301(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect SX1302 gateway baseband processor")
        @ValueSource(strings = {
            "SX1302",
            "SX1302IMLTRT",
            "SX1302CSS"
        })
        void shouldDetectSX1302(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect SX1303 gateway baseband processor")
        @ValueSource(strings = {
            "SX1303",
            "SX1303IMLTRT"
        })
        void shouldDetectSX1303(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("LoRa Edge - LR11xx Series")
    class LoRaLR11xxTests {

        @ParameterizedTest
        @DisplayName("Should detect LR1110 multi-protocol transceiver")
        @ValueSource(strings = {
            "LR1110",
            "LR1110IMLTRT",
            "LR1110-QFN"
        })
        void shouldDetectLR1110(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect LR1120 transceiver")
        @ValueSource(strings = {
            "LR1120",
            "LR1120IMLTRT"
        })
        void shouldDetectLR1120(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should detect LR1121 low power variant")
        @ValueSource(strings = {
            "LR1121",
            "LR1121IMLTRT"
        })
        void shouldDetectLR1121(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("LLCC68 LoRa Transceiver")
    class LLCC68Tests {

        @ParameterizedTest
        @DisplayName("Should detect LLCC68 transceiver")
        @ValueSource(strings = {
            "LLCC68",
            "LLCC68IMLTRT",
            "LLCC68-QFN"
        })
        void shouldDetectLLCC68(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("ESD Protection - RClamp Series")
    class ESDRClampTests {

        @ParameterizedTest
        @DisplayName("Should detect RClamp ESD protection devices")
        @ValueSource(strings = {
            "RCLAMP0524P",
            "RCLAMP0504F",
            "RCLAMP0502A",
            "RCLAMP0524P-TR",
            "RCLAMP0544P"
        })
        void shouldDetectRClamp(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("RClamp should also match ESD_PROTECTION type")
        void shouldMatchESDType() {
            assertTrue(handler.matches("RCLAMP0524P", ComponentType.ESD_PROTECTION_NEXPERIA, registry),
                    "RCLAMP0524P should match ESD protection type");
        }
    }

    @Nested
    @DisplayName("ESD Protection - SLVU Series")
    class ESDSLVUTests {

        @ParameterizedTest
        @DisplayName("Should detect SLVU TVS diodes")
        @ValueSource(strings = {
            "SLVU2.8-4",
            "SLVU2.8-8",
            "SLVU5.0-4",
            "SLVU3.3-4TBT"
        })
        void shouldDetectSLVU(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("SLVU should also match ESD_PROTECTION type")
        void shouldMatchESDType() {
            assertTrue(handler.matches("SLVU2.8-4", ComponentType.ESD_PROTECTION_NEXPERIA, registry),
                    "SLVU2.8-4 should match ESD protection type");
        }
    }

    @Nested
    @DisplayName("ESD Protection - SM Series")
    class ESDSMSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect SM series TVS diodes")
        @ValueSource(strings = {
            "SM712",
            "SM712-02HTG",
            "SM15T33A",
            "SM200"
        })
        void shouldDetectSMSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("ESD Protection - SMCJ Series")
    class ESDSMCJTests {

        @ParameterizedTest
        @DisplayName("Should detect SMCJ TVS diodes")
        @ValueSource(strings = {
            "SMCJ5.0A",
            "SMCJ12A",
            "SMCJ33A",
            "SMCJ5.0CA"
        })
        void shouldDetectSMCJ(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("Signal Integrity - ClearEdge GN Series")
    class SignalIntegrityGNTests {

        @ParameterizedTest
        @DisplayName("Should detect GN series signal integrity ICs")
        @ValueSource(strings = {
            "GN2033",
            "GN2033-INE3",
            "GN2005",
            "GN1054"
        })
        void shouldDetectGNSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("Signal Integrity - GS Series Retimers")
    class SignalIntegrityGSTests {

        @ParameterizedTest
        @DisplayName("Should detect GS series retimers")
        @ValueSource(strings = {
            "GS12090",
            "GS12190",
            "GS12070"
        })
        void shouldDetectGSSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("Power Management - SC Series")
    class PowerManagementSCTests {

        @ParameterizedTest
        @DisplayName("Should detect SC series power management ICs")
        @ValueSource(strings = {
            "SC4238",
            "SC5501",
            "SC189",
            "SC4524"
        })
        void shouldDetectSCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("Power Management - SY Series")
    class PowerManagementSYTests {

        @ParameterizedTest
        @DisplayName("Should detect SY series regulators (formerly Micrel)")
        @ValueSource(strings = {
            "SY8089",
            "SY8113",
            "SY8088",
            "SY8205"
        })
        void shouldDetectSYSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("Power Management - TC Series")
    class PowerManagementTCTests {

        @ParameterizedTest
        @DisplayName("Should detect TC series charge pumps")
        @ValueSource(strings = {
            "TC7660",
            "TC7662",
            "TC1240"
        })
        void shouldDetectTCSeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract QFN package codes")
        @CsvSource({
            "SX1276-QFN, QFN",
            "SX1262-QFN24, QFN-24",
            "SX1302-QFN32, QFN-32",
            "LR1110-QFN48, QFN-48"
        })
        void shouldExtractQFNPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SOT package codes")
        @CsvSource({
            "SM712-SOT23, SOT-23",
            "RCLAMP-SOT89, SOT-89"
        })
        void shouldExtractSOTPackages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SC70 package codes")
        @CsvSource({
            "SLVU2.8-SC70, SC70"
        })
        void shouldExtractSC70Packages(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract tape and reel indicators")
        @CsvSource({
            "SX1276-TR, Tape & Reel",
            "SX1262-TRG, Tape & Reel (Green)"
        })
        void shouldExtractTapeReelIndicators(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("RClamp suffix P indicates SOT-23")
        void shouldExtractRClampPackage() {
            assertEquals("SOT-23", handler.extractPackageCode("RCLAMP0524P"),
                    "RCLAMP0524P should have SOT-23 package");
        }

        @Test
        @DisplayName("Should return empty for MPN without package info")
        void shouldReturnEmptyForNoPackage() {
            assertEquals("", handler.extractPackageCode("SX1276"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract LoRa transceiver series")
        @CsvSource({
            "SX1276IMLTRT, SX1276",
            "SX1278-QFN, SX1278",
            "SX1262IMLTRT, SX1262",
            "SX1268-TR, SX1268",
            "SX1302CSS, SX1302",
            "SX1303IMLTRT, SX1303"
        })
        void shouldExtractLoRaSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract LR11xx series")
        @CsvSource({
            "LR1110IMLTRT, LR1110",
            "LR1120-QFN, LR1120",
            "LR1121, LR1121"
        })
        void shouldExtractLR11Series(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract LLCC68 series")
        @CsvSource({
            "LLCC68, LLCC68",
            "LLCC68IMLTRT, LLCC68"
        })
        void shouldExtractLLCC68Series(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract ESD protection series")
        @CsvSource({
            "RCLAMP0524P, RCLAMP",
            "RCLAMP0504F, RCLAMP",
            "SLVU2.8-4, SLVU",
            "SLVU5.0-8, SLVU",
            "SM712, SM712",
            "SM712-02HTG, SM712",
            "SMCJ5.0A, SMCJ"
        })
        void shouldExtractESDSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract signal integrity series")
        @CsvSource({
            "GN2033-INE3, GN2033",
            "GN2005, GN2005",
            "GS12090, GS12090"
        })
        void shouldExtractSignalIntegritySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract power management series")
        @CsvSource({
            "SC4238-TR, SC4238",
            "SC5501, SC5501",
            "SY8089, SY8089",
            "SY8113BADC, SY8113",
            "TC7660, TC7660"
        })
        void shouldExtractPowerManagementSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with different packages should be replacements")
        void sameSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("SX1276IMLTRT", "SX1276-QFN"),
                    "Same series with different packages should be replacements");
        }

        @Test
        @DisplayName("SX1261 and SX1262 should be compatible")
        void sx1261And1262Compatible() {
            assertTrue(handler.isOfficialReplacement("SX1261IMLTRT", "SX1262IMLTRT"),
                    "SX1261 and SX1262 should be compatible");
        }

        @Test
        @DisplayName("SX1262 and SX1268 should be compatible")
        void sx1262And1268Compatible() {
            assertTrue(handler.isOfficialReplacement("SX1262IMLTRT", "SX1268IMLTRT"),
                    "SX1262 and SX1268 should be compatible");
        }

        @Test
        @DisplayName("SX1302 and SX1303 gateway chips should be compatible")
        void sx1302And1303Compatible() {
            assertTrue(handler.isOfficialReplacement("SX1302CSS", "SX1303CSS"),
                    "SX1302 and SX1303 should be compatible");
        }

        @Test
        @DisplayName("LR11xx series should be internally compatible")
        void lr11xxCompatible() {
            assertTrue(handler.isOfficialReplacement("LR1110IMLTRT", "LR1120IMLTRT"),
                    "LR1110 and LR1120 should be compatible");
        }

        @Test
        @DisplayName("SX127x and SX126x should NOT be direct replacements")
        void sx127xAndSx126xNotDirectReplacements() {
            assertFalse(handler.isOfficialReplacement("SX1276IMLTRT", "SX1262IMLTRT"),
                    "SX127x and SX126x series should not be direct replacements");
        }

        @Test
        @DisplayName("Same RClamp series should be replacements")
        void rclampReplacements() {
            assertTrue(handler.isOfficialReplacement("RCLAMP0524P", "RCLAMP0524P-TR"),
                    "Same RClamp with different suffixes should be replacements");
        }

        @Test
        @DisplayName("Different product types should NOT be replacements")
        void differentTypesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("SX1276IMLTRT", "RCLAMP0524P"),
                    "LoRa transceiver and ESD protection should not be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC type")
        void shouldSupportIC() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("Should support RF_IC type")
        void shouldSupportRFIC() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.RF_IC_SKYWORKS),
                    "Should support RF_IC type for LoRa products");
        }

        @Test
        @DisplayName("Should support ESD_PROTECTION type")
        void shouldSupportESD() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.ESD_PROTECTION_NEXPERIA),
                    "Should support ESD_PROTECTION type");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of() (immutable)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.GENERIC);
            }, "getSupportedTypes should return immutable set");
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
            assertFalse(handler.isOfficialReplacement(null, "SX1276"));
            assertFalse(handler.isOfficialReplacement("SX1276", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("SX1276", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("sx1276imltrt", ComponentType.IC, registry),
                    "Should match lowercase MPN");
            assertTrue(handler.matches("Sx1276IMLTRT", ComponentType.IC, registry),
                    "Should match mixed case MPN");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            SemtechHandler directHandler = new SemtechHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes());
            assertFalse(directHandler.getSupportedTypes().isEmpty());
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }

    @Nested
    @DisplayName("Real-World Part Numbers")
    class RealWorldPartNumberTests {

        @ParameterizedTest
        @DisplayName("Should detect common LoRa module part numbers")
        @ValueSource(strings = {
            "SX1276IMLTRT",      // Popular LoRa transceiver in IoT
            "SX1262IMLTRT",      // Low power LoRa transceiver
            "SX1302CSSIMLTRT",   // Gateway baseband processor
            "LR1110IMLTRT",      // LoRa Edge with GNSS
            "LLCC68IMLTRT"       // Low-cost LoRa transceiver
        })
        void shouldDetectRealWorldLoRaParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect common ESD protection part numbers")
        @ValueSource(strings = {
            "RCLAMP0524P",       // Popular USB ESD protection
            "SLVU2.8-4TBT",      // USB 2.0 ESD protection
            "SM712-02HTG",       // RS-485/CAN ESD protection
            "SMCJ5.0A"           // Automotive TVS
        })
        void shouldDetectRealWorldESDParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect common signal integrity part numbers")
        @ValueSource(strings = {
            "GN2033-INE3",       // USB3.0/SATA redriver
            "GS12090-INTE3"      // PCIe/SAS retimer
        })
        void shouldDetectRealWorldSignalIntegrityParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be detected as IC");
        }
    }
}
