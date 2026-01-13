package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.QualcommHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for QualcommHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Qualcomm mobile platforms, RF front-end, and wireless connectivity ICs.
 */
class QualcommHandlerTest {

    private static QualcommHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new QualcommHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Mobile Platform Detection")
    class MobilePlatformTests {

        @ParameterizedTest
        @DisplayName("Should detect SM series Mobile SoCs")
        @ValueSource(strings = {"SM8350", "SM8450", "SM8550", "SM8650"})
        void shouldDetectSMSeriesSoCs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect SD series Snapdragon SoCs")
        @ValueSource(strings = {"SD888", "SD865", "SD855", "SD845"})
        void shouldDetectSDSeriesSoCs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect MSM series Mobile Station Modems")
        @ValueSource(strings = {"MSM8996", "MSM8998", "MSM8974"})
        void shouldDetectMSMSeriesModems(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect APQ series Application Processors")
        @ValueSource(strings = {"APQ8064", "APQ8096", "APQ8098"})
        void shouldDetectAPQSeriesProcessors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("IoT Platform Detection")
    class IoTPlatformTests {

        @ParameterizedTest
        @DisplayName("Should detect QCS series IoT platforms")
        @ValueSource(strings = {"QCS403", "QCS404", "QCS610", "QCS8250"})
        void shouldDetectQCSSeriesIoT(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect QCM series IoT modules")
        @ValueSource(strings = {"QCM2150", "QCM2290", "QCM4290", "QCM6490"})
        void shouldDetectQCMSeriesModules(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect IPQ series Network processors")
        @ValueSource(strings = {"IPQ4019", "IPQ6018", "IPQ8074", "IPQ9574"})
        void shouldDetectIPQSeriesProcessors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("RF Front-End Detection")
    class RFFrontEndTests {

        @ParameterizedTest
        @DisplayName("Should detect QM series RF modules")
        @ValueSource(strings = {"QM78207", "QM77031", "QM56020"})
        void shouldDetectQMSeriesRFModules(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect QPM series Power modules")
        @ValueSource(strings = {"QPM5621", "QPM5670", "QPM6585"})
        void shouldDetectQPMSeriesPowerModules(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect QAT series Antenna tuners")
        @ValueSource(strings = {"QAT3514", "QAT3516", "QAT3550"})
        void shouldDetectQATSeriesAntennaTuners(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect QET series Envelope trackers")
        @ValueSource(strings = {"QET4101", "QET5100", "QET6100"})
        void shouldDetectQETSeriesEnvelopeTrackers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Wireless Connectivity Detection")
    class WirelessConnectivityTests {

        @ParameterizedTest
        @DisplayName("Should detect QCA series Wi-Fi/Bluetooth")
        @ValueSource(strings = {"QCA6174", "QCA6390", "QCA6696", "QCA9377"})
        void shouldDetectQCASeriesWiFi(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect WCN series Wireless connectivity")
        @ValueSource(strings = {"WCN3680", "WCN3990", "WCN6855", "WCN7850"})
        void shouldDetectWCNSeriesWireless(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect MDM series Modems")
        @ValueSource(strings = {"MDM9205", "MDM9206", "MDM9607", "MDM9650"})
        void shouldDetectMDMSeriesModems(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Power Management Detection")
    class PowerManagementTests {

        @ParameterizedTest
        @DisplayName("Should detect PM series PMICs")
        @ValueSource(strings = {"PM8998", "PM8150", "PM8350", "PM8550"})
        void shouldDetectPMSeriesPMICs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect PMI series Power management")
        @ValueSource(strings = {"PMI8998", "PMI8994", "PMI8952"})
        void shouldDetectPMISeriesPowerMgmt(String mpn) {
            // Note: Pattern requires 4 digits (PMI[0-9]{4})
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect SMB series Battery charging")
        @ValueSource(strings = {"SMB1350", "SMB1355", "SMB1390", "SMB1398"})
        void shouldDetectSMBSeriesCharging(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Audio Detection")
    class AudioTests {

        @ParameterizedTest
        @DisplayName("Should detect WCD series Audio codecs")
        @ValueSource(strings = {"WCD9340", "WCD9341", "WCD9380", "WCD9385"})
        void shouldDetectWCDSeriesAudioCodecs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @ParameterizedTest
        @DisplayName("Should detect WSA series Smart amplifiers")
        @ValueSource(strings = {"WSA8810", "WSA8815", "WSA8830", "WSA8835"})
        void shouldDetectWSASeriesAmplifiers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from suffix")
        @CsvSource({
                "SM8350-BGA, BGA",
                "QCA6390-CSP, CSP",
                "IPQ8074-QFN, QFN",
                "PM8998-WLCSP, Wafer Level CSP"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MPN without package suffix")
        void shouldReturnEmptyWithoutSuffix() {
            assertEquals("", handler.extractPackageCode("SM8350"));
            assertEquals("", handler.extractPackageCode("QCA6390"));
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract series from mobile SoCs")
        @CsvSource({
                "SM8350, SM8350",
                "SM8450-BGA, SM8450",
                "SD888, SD888",
                "MSM8996, MSM8996"
        })
        void shouldExtractMobileSoCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract series from wireless ICs")
        @CsvSource({
                "QCA6390, QCA6390",
                "WCN6855, WCN6855",
                "MDM9650, MDM9650"
        })
        void shouldExtractWirelessSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for null or empty MPN")
        void shouldReturnEmptyForNullOrEmpty() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same Snapdragon family should be compatible")
        void sameSnapdragonFamily() {
            // Same SM8xxx family
            assertTrue(handler.isOfficialReplacement("SM8350", "SM8450"),
                    "Same SM8xxx family should be compatible");
        }

        @Test
        @DisplayName("Same QCA Wi-Fi family should be compatible")
        void sameQCAFamily() {
            assertTrue(handler.isOfficialReplacement("QCA6390", "QCA6696"),
                    "Same QCA6xxx family should be compatible");
        }

        @Test
        @DisplayName("Different product families should NOT be replacements")
        void differentFamiliesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("SM8350", "QCA6390"),
                    "Different product families should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("PM8998", "WCD9380"),
                    "PMIC and audio codec should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "SM8350"));
            assertFalse(handler.isOfficialReplacement("SM8350", null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.WIFI_IC_QUALCOMM));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMpn() {
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }
}
