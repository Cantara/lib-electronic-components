package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.SkyworksHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SkyworksHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class SkyworksHandlerTest {

    private static SkyworksHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new SkyworksHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("RF Amplifiers Detection")
    class RFAmplifiersTests {

        @ParameterizedTest
        @DisplayName("Document SKY RF Amplifier detection")
        @ValueSource(strings = {"SKY65017-70LF", "SKY65111-348LF", "SKY67151-396LF"})
        void documentSkyAmplifierDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SKY RF Amplifier detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document SE RF Power Amplifier detection")
        @ValueSource(strings = {"SE2576L", "SE5004L", "SE2435L"})
        void documentSeAmplifierDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SE RF Amplifier detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("RF Switches Detection")
    class RFSwitchesTests {

        @ParameterizedTest
        @DisplayName("Document AS RF Switch detection")
        @ValueSource(strings = {"AS169-73LF", "AS179-92LF", "AS213-334LF"})
        void documentAsSwitchDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("AS RF Switch detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document SKY13 RF Switch detection")
        @ValueSource(strings = {"SKY13350-385LF", "SKY13317-373LF", "SKY13351-378LF"})
        void documentSky13SwitchDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SKY13 RF Switch detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document PE RF Switch detection")
        @ValueSource(strings = {"PE4259", "PE42520", "PE42723"})
        void documentPeSwitchDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("PE RF Switch detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Front-End Modules Detection")
    class FrontEndModulesTests {

        @ParameterizedTest
        @DisplayName("Document SKY7 Front-End Module detection")
        @ValueSource(strings = {"SKY77643", "SKY77765", "SKY77541"})
        void documentSky7FemDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SKY7 FEM detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document SE47 FEM detection")
        @ValueSource(strings = {"SE4702L", "SE4753L", "SE4790L"})
        void documentSe47FemDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SE47 FEM detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("RF Filters Detection")
    class RFFiltersTests {

        @ParameterizedTest
        @DisplayName("Document SKY6 RF Filter detection")
        @ValueSource(strings = {"SKY66112", "SKY65313", "SKY66014"})
        void documentSky6FilterDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SKY6 RF Filter detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document SF SAW Filter detection")
        @ValueSource(strings = {"SF2098E", "SF1186A", "SF2049E"})
        void documentSfFilterDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SF SAW Filter detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Attenuators Detection")
    class AttenuatorsTests {

        @ParameterizedTest
        @DisplayName("Document AAT Attenuator detection")
        @ValueSource(strings = {"AAT4250", "AAT4620", "AAT3215"})
        void documentAatAttenuatorDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("AAT Attenuator detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Power Management Detection")
    class PowerManagementTests {

        @ParameterizedTest
        @DisplayName("Document SC Voltage Regulator detection")
        @ValueSource(strings = {"SC4524", "SC189", "SC4501"})
        void documentScRegulatorDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SC Regulator detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Silicon Labs Legacy MCUs Detection")
    class SiliconLabsMCUTests {

        @ParameterizedTest
        @DisplayName("Document EFM8 8-bit MCU detection")
        @ValueSource(strings = {"EFM8BB10F8G-A-QFN20", "EFM8SB20F16G-A-QFN24", "EFM8UB20F32G-C-QFN32"})
        void documentEfm8Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            System.out.println("EFM8 MCU detection: " + mpn + " MCU=" + matchesMCU);
        }

        @ParameterizedTest
        @DisplayName("Document EFM32 32-bit MCU detection")
        @ValueSource(strings = {"EFM32GG11B820F2048GL192", "EFM32PG12B500F1024GL125", "EFM32TG11B520F128GQ64"})
        void documentEfm32Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            System.out.println("EFM32 MCU detection: " + mpn + " MCU=" + matchesMCU);
        }

        @ParameterizedTest
        @DisplayName("Document EFR32 Wireless MCU detection")
        @ValueSource(strings = {"EFR32MG12P432F1024GL125", "EFR32BG21A020F1024IM32", "EFR32FG23A020F512GM48"})
        void documentEfr32Detection(String mpn) {
            boolean matchesMCU = handler.matches(mpn, ComponentType.MICROCONTROLLER, registry);
            System.out.println("EFR32 MCU detection: " + mpn + " MCU=" + matchesMCU);
        }
    }

    @Nested
    @DisplayName("Wireless ICs Detection")
    class WirelessICsTests {

        @ParameterizedTest
        @DisplayName("Document BGM Bluetooth Module detection")
        @ValueSource(strings = {"BGM121A256V2", "BGM220PC22HNA", "BGM13P22F512GA"})
        void documentBgmModuleDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("BGM Bluetooth Module detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document EFR32BG Blue Gecko detection")
        @ValueSource(strings = {"EFR32BG12P332F1024GL125", "EFR32BG21A010F1024IM32", "EFR32BG22C224F512GN32"})
        void documentBlueGeckoDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Blue Gecko detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document EFR32FG Flex Gecko detection")
        @ValueSource(strings = {"EFR32FG12P431F1024GL125", "EFR32FG23A020F512GM48", "EFR32FG14P231F256GM32"})
        void documentFlexGeckoDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Flex Gecko detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document EFR32MG Mighty Gecko detection")
        @ValueSource(strings = {"EFR32MG12P432F1024GL125", "EFR32MG21A010F1024IM32", "EFR32MG24B210F1536IM48"})
        void documentMightyGeckoDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("Mighty Gecko detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes")
        @CsvSource({
                "SKY65017-70LF-QFN, QFN",
                "SKY13350-385LF-REEL, REEL",
                "SKY77643-TR, REEL",
                "EFM32GG11B820F2048GL192-WLCSP, WLCSP",
                "BGM220PC22HNA-CSP, CSP",
                "EFR32MG12P432F1024GL125-BGA, BGA"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should handle special package codes")
        @CsvSource({
                "SKY65017-89, QFN",
                "SKY13350-86, CSP",
                "SKY77643-481, QFN48",
                "SKY66112-321, QFN32"
        })
        void shouldHandleSpecialPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract SKY series")
        @CsvSource({
                "SKY65017-70LF, SKY65017",
                "SKY13350-385LF, SKY13350",
                "SKY77643-11, SKY77643"
        })
        void shouldExtractSkySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract other RF series")
        @CsvSource({
                "AS169-73LF, AS169",
                "PE4259, PE425",
                "SE2576L, SE257",
                "SF2098E, SF209"
        })
        void shouldExtractOtherRfSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract Silicon Labs MCU series")
        @CsvSource({
                "EFM8BB10F8G-A-QFN20, EFM8BB1",
                "EFM32GG11B820F2048GL192, EFM32GG",
                "EFR32MG12P432F1024GL125, EFR32MG"
        })
        void shouldExtractSiliconLabsSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series should be replacements")
        void sameSeriesAreReplacements() {
            assertTrue(handler.isOfficialReplacement("SKY65017-70LF", "SKY65017-89LF"),
                    "Same series different suffixes should be replacements");
        }

        @Test
        @DisplayName("AS and PE switches can be compatible")
        void asPeSwitchesCompatible() {
            assertTrue(handler.isOfficialReplacement("AS169-73LF", "PE4259"),
                    "AS and PE switches can be compatible");
        }

        @Test
        @DisplayName("EFM32GG and EFR32MG can be compatible")
        void mcuSeriesCompatible() {
            assertTrue(handler.isOfficialReplacement("EFM32GG11B820F2048GL192", "EFR32MG12P432F1024GL125"),
                    "EFM32GG and EFR32MG can be compatible");
        }

        @Test
        @DisplayName("Different incompatible series NOT replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("SKY65017-70LF", "EFM8BB10F8G-A-QFN20"),
                    "RF amplifier and MCU are not replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support RF_IC_SKYWORKS")
        void shouldSupportRfIcType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.RF_IC_SKYWORKS),
                    "Should support RF_IC_SKYWORKS");
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
            assertFalse(handler.isOfficialReplacement(null, "SKY65017-70LF"));
            assertFalse(handler.isOfficialReplacement("SKY65017-70LF", null));
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
            SkyworksHandler directHandler = new SkyworksHandler();
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
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }
}
