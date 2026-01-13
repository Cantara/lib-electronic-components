package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.VicorHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for VicorHandler.
 *
 * Vicor Corporation manufactures high-performance power modules including:
 * - DCM: DC-DC Converter Modules
 * - BCM: Bus Converter Modules
 * - PRM: PRM Regulator Modules
 * - VTM: Voltage Transformation Modules
 * - NBM: NBM Converter Modules
 * - PI33xx/PI35xx: ZVS Buck and Cool-Power Regulators
 */
class VicorHandlerTest {

    private static VicorHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new VicorHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("DCM DC-DC Converter Detection")
    class DCMConverterTests {

        @ParameterizedTest
        @DisplayName("Should detect DCM converter modules as VOLTAGE_REGULATOR")
        @ValueSource(strings = {
                "DCM3623T50D40A4T",
                "DCM2322T50D40A0T",
                "DCM5614T36D53G4T",
                "DCM3714T50D40A4T",
                "DCM3623T36D40A0T"
        })
        void shouldDetectDCMAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect DCM converter modules as IC")
        @ValueSource(strings = {
                "DCM3623T50D40A4T",
                "DCM2322T50D40A0T",
                "DCM5614T36D53G4T"
        })
        void shouldDetectDCMAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract DCM series")
        void shouldExtractDCMSeries() {
            assertEquals("DCM", handler.extractSeries("DCM3623T50D40A4T"));
            assertEquals("DCM", handler.extractSeries("DCM2322T50D40A0T"));
            assertEquals("DCM", handler.extractSeries("DCM5614T36D53G4T"));
        }
    }

    @Nested
    @DisplayName("BCM Bus Converter Detection")
    class BCMConverterTests {

        @ParameterizedTest
        @DisplayName("Should detect BCM bus converter modules")
        @ValueSource(strings = {
                "BCM48BT120T300A00",
                "BCM48BF480T300A00",
                "BCM48BH480T250A00",
                "BCM384BT033T300A00"
        })
        void shouldDetectBCMAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect BCM modules as IC")
        @ValueSource(strings = {
                "BCM48BT120T300A00",
                "BCM48BF480T300A00"
        })
        void shouldDetectBCMAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract BCM series")
        void shouldExtractBCMSeries() {
            assertEquals("BCM", handler.extractSeries("BCM48BT120T300A00"));
            assertEquals("BCM", handler.extractSeries("BCM48BF480T300A00"));
        }
    }

    @Nested
    @DisplayName("PRM Regulator Detection")
    class PRMRegulatorTests {

        @ParameterizedTest
        @DisplayName("Should detect PRM regulator modules")
        @ValueSource(strings = {
                "PRM48BH480T200A00",
                "PRM48BT480T200A00",
                "PRM48AF480T400A00"
        })
        void shouldDetectPRMAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect PRM modules as IC")
        @ValueSource(strings = {
                "PRM48BH480T200A00",
                "PRM48BT480T200A00"
        })
        void shouldDetectPRMAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract PRM series")
        void shouldExtractPRMSeries() {
            assertEquals("PRM", handler.extractSeries("PRM48BH480T200A00"));
            assertEquals("PRM", handler.extractSeries("PRM48BT480T200A00"));
        }
    }

    @Nested
    @DisplayName("VTM Voltage Transformation Module Detection")
    class VTMTransformerTests {

        @ParameterizedTest
        @DisplayName("Should detect VTM transformer modules")
        @ValueSource(strings = {
                "VTM48EH040T025A00",
                "VTM48EF040T115A00",
                "VTM48ET040T015A00"
        })
        void shouldDetectVTMAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect VTM modules as IC")
        @ValueSource(strings = {
                "VTM48EH040T025A00",
                "VTM48EF040T115A00"
        })
        void shouldDetectVTMAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract VTM series")
        void shouldExtractVTMSeries() {
            assertEquals("VTM", handler.extractSeries("VTM48EH040T025A00"));
            assertEquals("VTM", handler.extractSeries("VTM48EF040T115A00"));
        }
    }

    @Nested
    @DisplayName("NBM Converter Detection")
    class NBMConverterTests {

        @ParameterizedTest
        @DisplayName("Should detect NBM converter modules")
        @ValueSource(strings = {
                "NBM2317S54E1560T00",
                "NBM6123S60E1560T0G",
                "NBM2317S48E1300T00"
        })
        void shouldDetectNBMAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect NBM modules as IC")
        @ValueSource(strings = {
                "NBM2317S54E1560T00",
                "NBM6123S60E1560T0G"
        })
        void shouldDetectNBMAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract NBM series")
        void shouldExtractNBMSeries() {
            assertEquals("NBM", handler.extractSeries("NBM2317S54E1560T00"));
            assertEquals("NBM", handler.extractSeries("NBM6123S60E1560T0G"));
        }
    }

    @Nested
    @DisplayName("PI Series Regulator Detection")
    class PISeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect PI33xx ZVS Buck Regulators")
        @ValueSource(strings = {
                "PI3301",
                "PI3302",
                "PI3301-00-LGIZ",
                "PI3302-00-QGIZ",
                "PI3303-20-LGIZ"
        })
        void shouldDetectPI33xxAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect PI35xx Cool-Power ZVS Regulators")
        @ValueSource(strings = {
                "PI3523",
                "PI3542",
                "PI3523-00-LGIZ",
                "PI3542-00-QGIZ",
                "PI3526-00-LGIZ"
        })
        void shouldDetectPI35xxAsVoltageRegulator(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry),
                    mpn + " should match VOLTAGE_REGULATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect PI series as IC")
        @ValueSource(strings = {
                "PI3301-00-LGIZ",
                "PI3523-00-LGIZ"
        })
        void shouldDetectPISeriesAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        @DisplayName("Should extract PI33 series")
        void shouldExtractPI33Series() {
            assertEquals("PI33", handler.extractSeries("PI3301"));
            assertEquals("PI33", handler.extractSeries("PI3302-00-LGIZ"));
            assertEquals("PI33", handler.extractSeries("PI3303-20-LGIZ"));
        }

        @Test
        @DisplayName("Should extract PI35 series")
        void shouldExtractPI35Series() {
            assertEquals("PI35", handler.extractSeries("PI3523"));
            assertEquals("PI35", handler.extractSeries("PI3542-00-QGIZ"));
            assertEquals("PI35", handler.extractSeries("PI3526-00-LGIZ"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract PI series package codes")
        @CsvSource({
                "PI3301-00-LGIZ, LGA",
                "PI3302-00-QGIZ, QFN",
                "PI3523-00-LGIZ, LGA",
                "PI3542-00-QGIZ, QFN"
        })
        void shouldExtractPIPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract power module package codes")
        @CsvSource({
                "DCM3623T50D40A4T, ChiP",
                "BCM48BT120T300A00, ChiP",
                "PRM48BH480T200A00, ChiP",
                "VTM48EH040T025A00, ChiP"
        })
        void shouldExtractPowerModulePackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for PI series without package suffix")
        void shouldReturnEmptyForPIWithoutSuffix() {
            assertEquals("", handler.extractPackageCode("PI3301"));
            assertEquals("", handler.extractPackageCode("PI3523"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract correct series from various MPNs")
        @CsvSource({
                "DCM3623T50D40A4T, DCM",
                "BCM48BT120T300A00, BCM",
                "PRM48BH480T200A00, PRM",
                "VTM48EH040T025A00, VTM",
                "NBM2317S54E1560T00, NBM",
                "PI3301-00-LGIZ, PI33",
                "PI3523-00-LGIZ, PI35"
        })
        void shouldExtractCorrectSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty series for unknown MPN")
        void shouldReturnEmptyForUnknownMPN() {
            assertEquals("", handler.extractSeries("UNKNOWN123"));
            assertEquals("", handler.extractSeries("LM7805"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same DCM spec with different options should be replacements")
        void sameDCMSpecAreReplacements() {
            assertTrue(handler.isOfficialReplacement("DCM3623T50D40A4T", "DCM3623T36D40A0T"),
                    "Same DCM spec should be replacements");
        }

        @Test
        @DisplayName("Different DCM specs should NOT be replacements")
        void differentDCMSpecsNotReplacements() {
            assertFalse(handler.isOfficialReplacement("DCM3623T50D40A4T", "DCM2322T50D40A0T"),
                    "Different DCM specs should NOT be replacements");
        }

        @Test
        @DisplayName("Same PI series part with different packages should be replacements")
        void samePIPartAreReplacements() {
            assertTrue(handler.isOfficialReplacement("PI3301-00-LGIZ", "PI3301-00-QGIZ"),
                    "Same PI part with different package should be replacement");
        }

        @Test
        @DisplayName("Different PI series should NOT be replacements")
        void differentPISeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("PI3301-00-LGIZ", "PI3523-00-LGIZ"),
                    "Different PI series should NOT be replacements");
        }

        @Test
        @DisplayName("Same BCM spec should be replacements")
        void sameBCMSpecAreReplacements() {
            assertTrue(handler.isOfficialReplacement("BCM48BT120T300A00", "BCM48BT120T250A00"),
                    "Same BCM spec should be replacements");
        }

        @Test
        @DisplayName("Different series families should NOT be replacements")
        void differentSeriesFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("DCM3623T50D40A4T", "BCM48BT120T300A00"),
                    "DCM and BCM should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("PRM48BH480T200A00", "VTM48EH040T025A00"),
                    "PRM and VTM should NOT be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support VOLTAGE_REGULATOR and IC types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.VOLTAGE_REGULATOR),
                    "Should support VOLTAGE_REGULATOR");
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");
        }

        @Test
        @DisplayName("Should use Set.of() (immutable set)")
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();

            // Verify it's immutable by checking class or attempting modification
            assertThrows(UnsupportedOperationException.class,
                    () -> types.add(ComponentType.RESISTOR),
                    "Set should be immutable");
        }

        @Test
        @DisplayName("Should not have duplicate types")
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "DCM3623T50D40A4T"));
            assertFalse(handler.isOfficialReplacement("DCM3623T50D40A4T", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("DCM3623T50D40A4T", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("dcm3623t50d40a4t", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("DCM3623T50D40A4T", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("Dcm3623T50D40A4T", ComponentType.VOLTAGE_REGULATOR, registry));

            assertTrue(handler.matches("pi3301-00-lgiz", ComponentType.VOLTAGE_REGULATOR, registry));
            assertTrue(handler.matches("PI3301-00-LGIZ", ComponentType.VOLTAGE_REGULATOR, registry));
        }

        @Test
        @DisplayName("Should NOT match non-Vicor MPNs")
        void shouldNotMatchNonVicorMPNs() {
            assertFalse(handler.matches("LM7805", ComponentType.VOLTAGE_REGULATOR, registry),
                    "TI regulator should not match");
            assertFalse(handler.matches("STM32F103", ComponentType.IC, registry),
                    "ST MCU should not match");
            assertFalse(handler.matches("ATMEGA328P", ComponentType.IC, registry),
                    "Atmel MCU should not match");
        }

        @Test
        @DisplayName("Should NOT match unsupported component types")
        void shouldNotMatchUnsupportedTypes() {
            assertFalse(handler.matches("DCM3623T50D40A4T", ComponentType.RESISTOR, registry),
                    "Should not match RESISTOR");
            assertFalse(handler.matches("DCM3623T50D40A4T", ComponentType.CAPACITOR, registry),
                    "Should not match CAPACITOR");
            assertFalse(handler.matches("DCM3623T50D40A4T", ComponentType.MOSFET, registry),
                    "Should not match MOSFET");
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldTests {

        @ParameterizedTest
        @DisplayName("Should handle real Vicor DCM part numbers")
        @ValueSource(strings = {
                "DCM3623T50D40A4T",      // 260-420V input, 36V output
                "DCM3714T50D40A4T",      // Different current rating
                "DCM2322T50D40A0T",      // 160-420V input, 24V output
                "DCM5614T36D53G4T"       // High current variant
        })
        void shouldHandleRealDCMParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("DCM", handler.extractSeries(mpn));
            assertNotNull(handler.extractPackageCode(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should handle real Vicor BCM part numbers")
        @ValueSource(strings = {
                "BCM48BT120T300A00",     // 48V bus converter, 120A
                "BCM48BF480T300A00"      // 48V bus converter, variant
        })
        void shouldHandleRealBCMParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry));
            assertEquals("BCM", handler.extractSeries(mpn));
        }

        @ParameterizedTest
        @DisplayName("Should handle real Vicor PI series part numbers")
        @ValueSource(strings = {
                "PI3301-00-LGIZ",        // 3.3V ZVS Buck, LGA
                "PI3302-00-QGIZ",        // 3.3V ZVS Buck, QFN
                "PI3523-00-LGIZ",        // Cool-Power ZVS, LGA
                "PI3542-00-QGIZ"         // Cool-Power ZVS, QFN
        })
        void shouldHandleRealPIParts(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VOLTAGE_REGULATOR, registry));
            String series = handler.extractSeries(mpn);
            assertTrue(series.startsWith("PI3"), "Series should start with PI3");
        }
    }

    @Nested
    @DisplayName("Cross-Family Comparison")
    class CrossFamilyTests {

        @Test
        @DisplayName("DCM should not be replacement for BCM")
        void dcmNotReplacementForBcm() {
            assertFalse(handler.isOfficialReplacement("DCM3623T50D40A4T", "BCM48BT120T300A00"));
        }

        @Test
        @DisplayName("PRM should not be replacement for VTM")
        void prmNotReplacementForVtm() {
            assertFalse(handler.isOfficialReplacement("PRM48BH480T200A00", "VTM48EH040T025A00"));
        }

        @Test
        @DisplayName("PI33xx should not be replacement for PI35xx")
        void pi33NotReplacementForPi35() {
            assertFalse(handler.isOfficialReplacement("PI3301-00-LGIZ", "PI3523-00-LGIZ"));
        }

        @Test
        @DisplayName("NBM should not be replacement for any other family")
        void nbmNotReplacementForOthers() {
            assertFalse(handler.isOfficialReplacement("NBM2317S54E1560T00", "DCM3623T50D40A4T"));
            assertFalse(handler.isOfficialReplacement("NBM2317S54E1560T00", "BCM48BT120T300A00"));
            assertFalse(handler.isOfficialReplacement("NBM2317S54E1560T00", "PI3301-00-LGIZ"));
        }
    }
}
