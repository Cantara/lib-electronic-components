package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.PowerIntegrationsHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for PowerIntegrationsHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Power Integrations SMPS controllers.
 */
class PowerIntegrationsHandlerTest {

    private static PowerIntegrationsHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new PowerIntegrationsHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("TOPSwitch Detection")
    class TOPSwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect TOP2xx variants")
        @ValueSource(strings = {"TOP223Y", "TOP224Y", "TOP225Y", "TOP226Y", "TOP227Y",
                "TOP244Y", "TOP245Y", "TOP246Y", "TOP247Y", "TOP248Y", "TOP249Y",
                "TOP249YN", "TOP250Y", "TOP250YN"})
        void shouldDetectTOP2xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect TOP3xx variants")
        @ValueSource(strings = {"TOP300Y", "TOP310Y", "TOP320Y", "TOP321Y",
                "TOP369KG", "TOP269KG", "TOP269Y"})
        void shouldDetectTOP3xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as TOP")
        void shouldExtractTOPSeries() {
            assertEquals("TOP", handler.extractSeries("TOP249Y"));
            assertEquals("TOP", handler.extractSeries("TOP249YN"));
            assertEquals("TOP", handler.extractSeries("TOP223Y"));
        }

        @Test
        @DisplayName("Should identify TOPSwitch family")
        void shouldIdentifyTOPSwitchFamily() {
            assertEquals("TOPSwitch", handler.getProductFamily("TOP249Y"));
            assertEquals("TOPSwitch", handler.getProductFamily("TOP223Y"));
        }
    }

    @Nested
    @DisplayName("TinySwitch Detection")
    class TinySwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect TNY2xx variants")
        @ValueSource(strings = {"TNY263", "TNY264", "TNY265", "TNY266", "TNY267", "TNY268",
                "TNY263GN", "TNY264GN", "TNY268GN", "TNY268DN", "TNY268PN"})
        void shouldDetectTNY2xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect TNY3xx variants")
        @ValueSource(strings = {"TNY363", "TNY364", "TNY365", "TNY366",
                "TNY374", "TNY375", "TNY376", "TNY377", "TNY378", "TNY379",
                "TNY376GN", "TNY377GN"})
        void shouldDetectTNY3xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as TNY")
        void shouldExtractTNYSeries() {
            assertEquals("TNY", handler.extractSeries("TNY268GN"));
            assertEquals("TNY", handler.extractSeries("TNY263"));
            assertEquals("TNY", handler.extractSeries("TNY376GN"));
        }

        @Test
        @DisplayName("Should identify TinySwitch family")
        void shouldIdentifyTinySwitchFamily() {
            assertEquals("TinySwitch", handler.getProductFamily("TNY268GN"));
            assertEquals("TinySwitch", handler.getProductFamily("TNY376GN"));
        }
    }

    @Nested
    @DisplayName("LinkSwitch Detection")
    class LinkSwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect LNK3xx variants")
        @ValueSource(strings = {"LNK302", "LNK304", "LNK305", "LNK306",
                "LNK302DN", "LNK304DN", "LNK304GN", "LNK306GN", "LNK306DN"})
        void shouldDetectLNK3xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect LNK4xx variants")
        @ValueSource(strings = {"LNK403", "LNK404", "LNK405", "LNK406", "LNK407",
                "LNK403EG", "LNK406EG", "LNK407EG"})
        void shouldDetectLNK4xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect LNK6xx variants")
        @ValueSource(strings = {"LNK362", "LNK363", "LNK364", "LNK365", "LNK366",
                "LNK362DN", "LNK364DN", "LNK364GN", "LNK364PN"})
        void shouldDetectLNK6xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as LNK")
        void shouldExtractLNKSeries() {
            assertEquals("LNK", handler.extractSeries("LNK302DN"));
            assertEquals("LNK", handler.extractSeries("LNK364DN"));
            assertEquals("LNK", handler.extractSeries("LNK406EG"));
        }

        @Test
        @DisplayName("Should identify LinkSwitch family")
        void shouldIdentifyLinkSwitchFamily() {
            assertEquals("LinkSwitch", handler.getProductFamily("LNK302DN"));
            assertEquals("LinkSwitch", handler.getProductFamily("LNK364DN"));
        }
    }

    @Nested
    @DisplayName("InnoSwitch Detection")
    class InnoSwitchTests {

        @ParameterizedTest
        @DisplayName("Should detect INN20xx variants")
        @ValueSource(strings = {"INN2023K", "INN2024K", "INN2025K",
                "INN2023KG", "INN2024KG"})
        void shouldDetectINN20xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect INN30xx variants")
        @ValueSource(strings = {"INN3023C", "INN3024C", "INN3025C",
                "INN3166C", "INN3167C", "INN3168C",
                "INN3166CJ", "INN3167CJ"})
        void shouldDetectINN30xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect INN40xx variants")
        @ValueSource(strings = {"INN4023C", "INN4024C", "INN4025C"})
        void shouldDetectINN40xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect InnoSwitch3 (INN3xxx) variants")
        @ValueSource(strings = {"INN3673C", "INN3674C", "INN3675C",
                "INN3673CJ", "INN3370C", "INN3371C"})
        void shouldDetectInnoSwitch3Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as INN")
        void shouldExtractINNSeries() {
            assertEquals("INN", handler.extractSeries("INN2023K"));
            assertEquals("INN", handler.extractSeries("INN3166C"));
            assertEquals("INN", handler.extractSeries("INN3673C"));
        }

        @Test
        @DisplayName("Should identify InnoSwitch family")
        void shouldIdentifyInnoSwitchFamily() {
            assertEquals("InnoSwitch", handler.getProductFamily("INN2023K"));
            assertEquals("InnoSwitch", handler.getProductFamily("INN3166C"));
            assertEquals("InnoSwitch", handler.getProductFamily("INN3673C"));
        }
    }

    @Nested
    @DisplayName("HiperPFS Detection")
    class HiperPFSTests {

        @ParameterizedTest
        @DisplayName("Should detect PFS7xx variants")
        @ValueSource(strings = {"PFS714", "PFS716", "PFS718",
                "PFS714EG", "PFS716EG", "PFS718EG",
                "PFS7624", "PFS7624H", "PFS7628H"})
        void shouldDetectPFS7xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as PFS")
        void shouldExtractPFSSeries() {
            assertEquals("PFS", handler.extractSeries("PFS714EG"));
            assertEquals("PFS", handler.extractSeries("PFS7624H"));
        }

        @Test
        @DisplayName("Should identify HiperPFS family")
        void shouldIdentifyHiperPFSFamily() {
            assertEquals("HiperPFS", handler.getProductFamily("PFS714EG"));
            assertEquals("HiperPFS", handler.getProductFamily("PFS7624H"));
        }
    }

    @Nested
    @DisplayName("HiperLCS Detection")
    class HiperLCSTests {

        @ParameterizedTest
        @DisplayName("Should detect LCS7xx variants")
        @ValueSource(strings = {"LCS708", "LCS712", "LCS716",
                "LCS708HG", "LCS712HG", "LCS716HG"})
        void shouldDetectLCS7xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as LCS")
        void shouldExtractLCSSeries() {
            assertEquals("LCS", handler.extractSeries("LCS708HG"));
            assertEquals("LCS", handler.extractSeries("LCS712HG"));
        }

        @Test
        @DisplayName("Should identify HiperLCS family")
        void shouldIdentifyHiperLCSFamily() {
            assertEquals("HiperLCS", handler.getProductFamily("LCS708HG"));
            assertEquals("HiperLCS", handler.getProductFamily("LCS712HG"));
        }
    }

    @Nested
    @DisplayName("CAPZero Detection")
    class CAPZeroTests {

        @ParameterizedTest
        @DisplayName("Should detect CAP0xx variants")
        @ValueSource(strings = {"CAP002", "CAP002DG", "CAP003", "CAP003DG",
                "CAP004", "CAP004DG"})
        void shouldDetectCAP0xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as CAP")
        void shouldExtractCAPSeries() {
            assertEquals("CAP", handler.extractSeries("CAP002DG"));
        }

        @Test
        @DisplayName("Should identify CAPZero family")
        void shouldIdentifyCAPZeroFamily() {
            assertEquals("CAPZero", handler.getProductFamily("CAP002DG"));
        }
    }

    @Nested
    @DisplayName("SENZero Detection")
    class SENZeroTests {

        @ParameterizedTest
        @DisplayName("Should detect SEN0xx variants")
        @ValueSource(strings = {"SEN013", "SEN013DG", "SEN012", "SEN012DG"})
        void shouldDetectSEN0xxVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @Test
        @DisplayName("Should extract series as SEN")
        void shouldExtractSENSeries() {
            assertEquals("SEN", handler.extractSeries("SEN013DG"));
        }

        @Test
        @DisplayName("Should identify SENZero family")
        void shouldIdentifySENZeroFamily() {
            assertEquals("SENZero", handler.getProductFamily("SEN013DG"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes correctly")
        @CsvSource({
                "TOP249Y, TO-220",
                "TOP249YN, TO-220",
                "TNY268GN, SMD-8",
                "TNY268G, SMD-8",
                "TNY268DN, DIP-8",
                "TNY268D, DIP-8",
                "TNY268PN, PDIP-8",
                "LNK364DN, DIP-8",
                "INN2023K, eSOP-12",
                "INN2023KG, eSOP-12",
                "INN3166C, InSOP-24",
                "INN3166CJ, InSOP-24",
                "PFS714EG, eSIP-7",
                "PFS7624H, eSIP-7",
                "LCS708HG, eSIP-7",
                "CAP002DG, SMD-8",
                "SEN013DG, SMD-8"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle MPNs without package suffix")
        void shouldHandleMPNsWithoutPackageSuffix() {
            String packageCode = handler.extractPackageCode("TOP249");
            assertTrue(packageCode.isEmpty() || packageCode.equals("TO-220") || packageCode.length() <= 2,
                    "Package code for bare MPN should be empty or minimal");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series correctly")
        @CsvSource({
                "TOP249YN, TOP",
                "TNY268GN, TNY",
                "LNK364DN, LNK",
                "INN3166C, INN",
                "PFS714EG, PFS",
                "LCS708HG, LCS",
                "CAP002DG, CAP",
                "SEN013DG, SEN"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }
    }

    @Nested
    @DisplayName("Base Part Number Extraction")
    class BasePartNumberTests {

        @ParameterizedTest
        @DisplayName("Should extract base part numbers correctly")
        @CsvSource({
                "TOP249YN, TOP249",
                "TOP249Y, TOP249",
                "TNY268GN, TNY268",
                "LNK364DN, LNK364",
                "INN3166C, INN3166",
                "INN3166CJ, INN3166",
                "PFS714EG, PFS714",
                "LCS708HG, LCS708"
        })
        void shouldExtractBasePartNumber(String mpn, String expectedBase) {
            assertEquals(expectedBase, handler.extractBasePartNumber(mpn),
                    "Base part number for " + mpn);
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same base part with different packages should be replacements")
        void sameBasePartDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("TOP249Y", "TOP249YN"),
                    "TOP249Y and TOP249YN should be replacements");
            assertTrue(handler.isOfficialReplacement("TNY268GN", "TNY268DN"),
                    "TNY268GN and TNY268DN should be replacements");
            assertTrue(handler.isOfficialReplacement("LNK364GN", "LNK364DN"),
                    "LNK364GN and LNK364DN should be replacements");
            assertTrue(handler.isOfficialReplacement("INN3166C", "INN3166CJ"),
                    "INN3166C and INN3166CJ should be replacements");
        }

        @Test
        @DisplayName("Similar power ratings in same family should be replacements")
        void similarPowerRatingsAreReplacements() {
            assertTrue(handler.isOfficialReplacement("TOP247Y", "TOP248Y"),
                    "TOP247Y and TOP248Y should be replacements (close power ratings)");
            assertTrue(handler.isOfficialReplacement("TNY267GN", "TNY268GN"),
                    "TNY267GN and TNY268GN should be replacements");
            assertTrue(handler.isOfficialReplacement("LNK363DN", "LNK364DN"),
                    "LNK363DN and LNK364DN should be replacements");
        }

        @Test
        @DisplayName("Different families should NOT be replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("TOP249Y", "TNY268GN"),
                    "TOP and TNY should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("LNK364DN", "TNY268GN"),
                    "LNK and TNY should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("INN3166C", "TOP249Y"),
                    "INN and TOP should NOT be replacements");
        }

        @Test
        @DisplayName("Very different power ratings should NOT be replacements")
        void veryDifferentPowerRatingsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("TOP223Y", "TOP249Y"),
                    "TOP223 and TOP249 should NOT be replacements (very different power ratings)");
            assertFalse(handler.isOfficialReplacement("TNY263GN", "TNY290GN"),
                    "TNY263 and TNY290 should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC and VOLTAGE_REGULATOR types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR),
                    "Should support VOLTAGE_REGULATOR type");
            assertEquals(2, types.size(),
                    "Should only support 2 types");
        }

        @Test
        @DisplayName("Should use Set.of() for immutable set")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();

            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.MOSFET);
            }, "getSupportedTypes() should return immutable set");
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
            assertEquals("", handler.extractBasePartNumber(null));
            assertEquals("", handler.getProductFamily(null));
            assertFalse(handler.isOfficialReplacement(null, "TOP249Y"));
            assertFalse(handler.isOfficialReplacement("TOP249Y", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractBasePartNumber(""));
            assertEquals("", handler.getProductFamily(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("TOP249Y", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("top249y", ComponentType.IC, registry));
            assertTrue(handler.matches("TOP249Y", ComponentType.IC, registry));
            assertTrue(handler.matches("Top249Y", ComponentType.IC, registry));
            assertTrue(handler.matches("tny268gn", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should not match unrelated MPNs")
        void shouldNotMatchUnrelatedMPNs() {
            assertFalse(handler.matches("LM7805", ComponentType.IC, registry),
                    "Should not match TI voltage regulators");
            assertFalse(handler.matches("MC34063", ComponentType.IC, registry),
                    "Should not match ON Semi regulators");
            assertFalse(handler.matches("STM32F103", ComponentType.IC, registry),
                    "Should not match ST microcontrollers");
            assertFalse(handler.matches("NE555", ComponentType.IC, registry),
                    "Should not match generic timer ICs");
        }

        @Test
        @DisplayName("Should not match partial matches")
        void shouldNotMatchPartialMatches() {
            assertFalse(handler.matches("TOP", ComponentType.IC, registry),
                    "Should not match just prefix");
            assertFalse(handler.matches("TNY", ComponentType.IC, registry),
                    "Should not match just prefix");
            assertFalse(handler.matches("TOPSWITCH", ComponentType.IC, registry),
                    "Should not match family name");
        }
    }

    @Nested
    @DisplayName("Product Family Tests")
    class ProductFamilyTests {

        @ParameterizedTest
        @DisplayName("Should return correct product family")
        @CsvSource({
                "TOP249Y, TOPSwitch",
                "TNY268GN, TinySwitch",
                "LNK364DN, LinkSwitch",
                "INN3166C, InnoSwitch",
                "PFS714EG, HiperPFS",
                "LCS708HG, HiperLCS",
                "CAP002DG, CAPZero",
                "SEN013DG, SENZero"
        })
        void shouldReturnCorrectProductFamily(String mpn, String expectedFamily) {
            assertEquals(expectedFamily, handler.getProductFamily(mpn),
                    "Product family for " + mpn);
        }

        @Test
        @DisplayName("Should return empty string for unknown MPN")
        void shouldReturnEmptyForUnknownMPN() {
            assertEquals("", handler.getProductFamily("LM7805"));
            assertEquals("", handler.getProductFamily("UNKNOWN"));
        }
    }
}
