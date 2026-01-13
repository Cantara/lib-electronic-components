package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.CUIHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for CUIHandler.
 * Tests audio jacks (SJ, PJ series), buzzers/speakers (CMI, CMS, CPE, CPT series),
 * and encoders (AMT, ACZ series).
 *
 * CUI Devices product lines:
 * - Audio Jacks: SJ series (3.5mm audio), PJ series (power jacks)
 * - Buzzers: CMI (magnetic), CPE (piezo), CPT (transducer)
 * - Speakers: CMS series
 * - Encoders: AMT (modular), ACZ (rotary)
 *
 * Handler patterns:
 * - SJ: SJx-xxxx (e.g., SJ1-3523N)
 * - PJ: PJ-xxxX (e.g., PJ-002A)
 * - CMI: CMI-xxxx-xxX (e.g., CMI-1295-85T)
 * - CMS: CMS-xxxxx-xxX (e.g., CMS-15118-78P)
 * - CPE: CPE-xxxxX (e.g., CPE-825P)
 * - CPT: CPT-xxxx-xxX (e.g., CPT-2014-65)
 * - AMT: AMTxxx-X (e.g., AMT102-V)
 * - ACZ: ACZxxXxxx-xxx (e.g., ACZ11BR1E-20FA1-24C)
 */
class CUIHandlerTest {

    private static CUIHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new CUIHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("SJ Audio Jack Series Detection")
    class SJAudioJackTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "SJ1-3523N",
            "SJ1-3525N",
            "SJ1-3535N",
            "SJ2-3523N",
            "SJ1-3553N",
            "SJ1-3593N"
        })
        void shouldDetectSJAudioJacks(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
            assertTrue(handler.matches(mpn, ComponentType.AUDIO_JACK, registry),
                    mpn + " should match AUDIO_JACK");
        }

        @Test
        void shouldIdentifyAsAudioConnector() {
            assertTrue(handler.isAudioConnector("SJ1-3523N"));
            assertTrue(handler.isAudioConnector("SJ2-3525N"));
            assertFalse(handler.isAudioConnector("PJ-002A"));
            assertFalse(handler.isAudioConnector("CMI-1295-85T"));
        }
    }

    @Nested
    @DisplayName("PJ Power Jack Series Detection")
    class PJPowerJackTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PJ-002A",
            "PJ-002AH",
            "PJ-002B",
            "PJ-005A",
            "PJ-102A",
            "PJ-202A"
        })
        void shouldDetectPJPowerJacks(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldIdentifyAsPowerConnector() {
            assertTrue(handler.isPowerConnector("PJ-002A"));
            assertTrue(handler.isPowerConnector("PJ-102A"));
            assertFalse(handler.isPowerConnector("SJ1-3523N"));
            assertFalse(handler.isPowerConnector("CMI-1295-85T"));
        }
    }

    @Nested
    @DisplayName("CMI Magnetic Buzzer Series Detection")
    class CMIBuzzerTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "CMI-1295-85T",
            "CMI-0905-45T",
            "CMI-1210-65T",
            "CMI-1614-75T"
        })
        void shouldDetectCMIBuzzers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.BUZZER, registry),
                    mpn + " should match BUZZER");
        }

        @Test
        void shouldIdentifyAsBuzzer() {
            assertTrue(handler.isBuzzer("CMI-1295-85T"));
            assertTrue(handler.isBuzzer("CPE-825P"));
            assertTrue(handler.isBuzzer("CPT-2014-65"));
            assertFalse(handler.isBuzzer("CMS-15118-78P"));
            assertFalse(handler.isBuzzer("SJ1-3523N"));
        }
    }

    @Nested
    @DisplayName("CMS Speaker Series Detection")
    class CMSSpeakerTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "CMS-15118-78P",
            "CMS-2011-45P",
            "CMS-2715-68T",
            "CMS-4015-88P"
        })
        void shouldDetectCMSSpeakers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.SPEAKER, registry),
                    mpn + " should match SPEAKER");
        }

        @Test
        void shouldIdentifyAsSpeaker() {
            assertTrue(handler.isSpeaker("CMS-15118-78P"));
            assertTrue(handler.isSpeaker("CMS-2011-45P"));
            assertFalse(handler.isSpeaker("CMI-1295-85T"));
            assertFalse(handler.isSpeaker("CPE-825P"));
        }
    }

    @Nested
    @DisplayName("CPE/CPT Buzzer Series Detection")
    class CPEBuzzerTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "CPE-825P",
            "CPE-735T",
            "CPE-527P"
        })
        void shouldDetectCPEBuzzers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.BUZZER, registry),
                    mpn + " should match BUZZER");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "CPT-2014-65",
            "CPT-2320-85T"
        })
        void shouldDetectCPTTransducers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.BUZZER, registry),
                    mpn + " should match BUZZER");
        }
    }

    @Nested
    @DisplayName("AMT Encoder Series Detection")
    class AMTEncoderTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "AMT102-V",
            "AMT103-V",
            "AMT10E-V",
            "AMT112Q-V",
            "AMT21"
        })
        void shouldDetectAMTEncoders(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.ENCODER, registry),
                    mpn + " should match ENCODER");
        }

        @Test
        void shouldIdentifyAsEncoder() {
            assertTrue(handler.isEncoder("AMT102-V"));
            assertTrue(handler.isEncoder("ACZ11BR1E-20FA1-24C"));
            assertFalse(handler.isEncoder("SJ1-3523N"));
            assertFalse(handler.isEncoder("CMI-1295-85T"));
        }
    }

    @Nested
    @DisplayName("ACZ Rotary Encoder Series Detection")
    class ACZEncoderTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ACZ11BR1E-20FA1-24C",
            "ACZ16NBRD-15ETG",
            "ACZ15"
        })
        void shouldDetectACZEncoders(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.ENCODER, registry),
                    mpn + " should match ENCODER");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "SJ1-3523N, N",
            "SJ1-3525NR, NR",
            "PJ-002A, A",
            "PJ-002AH, AH",
            "CMI-1295-85T, T",
            "CMS-15118-78P, P",
            "AMT102-V, V"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code extraction for " + mpn);
        }

        @Test
        void shouldHandleEmptyPackageCode() {
            assertEquals("", handler.extractPackageCode("PJ-002"));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "SJ1-3523N, SJ1",
            "SJ2-3525N, SJ2",
            "PJ-002A, PJ",
            "CMI-1295-85T, CMI",
            "CMS-15118-78P, CMS",
            "CPE-825P, CPE",
            "CPT-2014-65, CPT",
            "AMT102-V, AMT102",
            "AMT103-V, AMT103",
            "ACZ11BR1E-20FA1-24C, ACZ11"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn),
                    "Series extraction for " + mpn);
        }

        @Test
        void shouldHandleEmptySeries() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("UNKNOWN-123"));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetProductFamily() {
            assertEquals("SJ1 Audio Jack Series", handler.getProductFamily("SJ1-3523N"));
            assertEquals("PJ Power Jack Series", handler.getProductFamily("PJ-002A"));
            assertEquals("CMI Magnetic Buzzer Series", handler.getProductFamily("CMI-1295-85T"));
            assertEquals("CMS Speaker Series", handler.getProductFamily("CMS-15118-78P"));
            assertEquals("AMT Modular Encoder Series", handler.getProductFamily("AMT102-V"));
            assertEquals("ACZ Rotary Encoder Series", handler.getProductFamily("ACZ11BR1E"));
        }

        @Test
        void shouldGetMountingType() {
            assertEquals("Vertical/PCB Mount", handler.getMountingType("SJ1-3523N"));
            assertEquals("Surface Mount", handler.getMountingType("PJ-002A"));
            assertEquals("Terminal Type", handler.getMountingType("CMI-1295-85T"));
            assertEquals("Vertical", handler.getMountingType("AMT102-V"));
        }

        @Test
        void shouldGetTransducerSize() {
            assertEquals(12, handler.getTransducerSizeMm("CMI-1295-85T"));
            assertEquals(15, handler.getTransducerSizeMm("CMS-15118-78P"));
            assertEquals(-1, handler.getTransducerSizeMm("SJ1-3523N"));
            assertEquals(-1, handler.getTransducerSizeMm(null));
        }

        @Test
        void shouldGetImpedance() {
            assertEquals(85, handler.getImpedanceOhms("CMI-1295-85T"));
            assertEquals(78, handler.getImpedanceOhms("CMS-15118-78P"));
            assertEquals(-1, handler.getImpedanceOhms("SJ1-3523N"));
            assertEquals(-1, handler.getImpedanceOhms(null));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldMatchSameAudioJackVariants() {
            assertTrue(handler.isOfficialReplacement("SJ1-3523N", "SJ1-3523NR"),
                    "Same audio jack number should be replacement");
        }

        @Test
        void shouldNotMatchDifferentAudioJackTypes() {
            assertFalse(handler.isOfficialReplacement("SJ1-3523N", "SJ1-3525N"),
                    "Different audio jack types should not be replacement");
        }

        @Test
        void shouldNotMatchAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("SJ1-3523N", "PJ-002A"),
                    "Different series should not be replacement");
            assertFalse(handler.isOfficialReplacement("CMI-1295-85T", "CMS-15118-78P"),
                    "Buzzer and speaker should not be replacement");
        }

        @Test
        void shouldMatchSameEncoderSeries() {
            assertTrue(handler.isOfficialReplacement("AMT102-V", "AMT102-V"),
                    "Same encoder should be replacement");
        }

        @Test
        void shouldNotMatchDifferentEncoderResolutions() {
            assertFalse(handler.isOfficialReplacement("AMT102-V", "AMT103-V"),
                    "Different encoder resolutions should not be replacement");
        }

        @Test
        void shouldHandleNullValues() {
            assertFalse(handler.isOfficialReplacement(null, "SJ1-3523N"));
            assertFalse(handler.isOfficialReplacement("SJ1-3523N", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }

        @Test
        void shouldMatchCompatibleTransducers() {
            // Same size and impedance should be compatible
            assertTrue(handler.isOfficialReplacement("CMI-1295-85T", "CMI-1295-85P"),
                    "Same size and impedance buzzer should be replacement");
        }

        @Test
        void shouldNotMatchIncompatibleTransducers() {
            // Different sizes should not be compatible
            assertFalse(handler.isOfficialReplacement("CMI-1295-85T", "CMI-0905-85T"),
                    "Different size buzzers should not be replacement");
            // Different impedances should not be compatible
            assertFalse(handler.isOfficialReplacement("CMI-1295-85T", "CMI-1295-65T"),
                    "Different impedance buzzers should not be replacement");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.CONNECTOR, registry));
            assertFalse(handler.matches(null, ComponentType.BUZZER, registry));
            assertFalse(handler.matches(null, ComponentType.ENCODER, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertFalse(handler.matches("", ComponentType.BUZZER, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("SJ1-3523N", null, registry));
        }

        @Test
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("sj1-3523n", ComponentType.AUDIO_JACK, registry));
            assertTrue(handler.matches("pj-002a", ComponentType.CONNECTOR, registry));
            assertTrue(handler.matches("cmi-1295-85t", ComponentType.BUZZER, registry));
            assertTrue(handler.matches("amt102-v", ComponentType.ENCODER, registry));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.CONNECTOR),
                    "Should support CONNECTOR");
            assertTrue(types.contains(ComponentType.AUDIO_JACK),
                    "Should support AUDIO_JACK");
            assertTrue(types.contains(ComponentType.SPEAKER),
                    "Should support SPEAKER");
            assertTrue(types.contains(ComponentType.BUZZER),
                    "Should support BUZZER");
            assertTrue(types.contains(ComponentType.ENCODER),
                    "Should support ENCODER");
        }

        @Test
        void shouldHaveExactlyFiveTypes() {
            assertEquals(5, handler.getSupportedTypes().size(),
                    "Should support exactly 5 component types");
        }

        @Test
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {
        @Test
        void canInstantiateDirectly() {
            CUIHandler directHandler = new CUIHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify patterns work
            assertTrue(directHandler.matches("SJ1-3523N", ComponentType.AUDIO_JACK, directRegistry));
            assertTrue(directHandler.matches("CMI-1295-85T", ComponentType.BUZZER, directRegistry));
            assertTrue(directHandler.matches("AMT102-V", ComponentType.ENCODER, directRegistry));
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
    @DisplayName("Real-World MPN Testing")
    class RealWorldMPNTests {
        @Test
        void shouldMatchRealAudioJackMPNs() {
            // Real CUI audio jack part numbers
            assertTrue(handler.matches("SJ1-3523N", ComponentType.AUDIO_JACK, registry),
                    "SJ1-3523N is a real CUI 3.5mm audio jack");
            assertTrue(handler.matches("SJ1-3525N", ComponentType.AUDIO_JACK, registry),
                    "SJ1-3525N is a real CUI 3.5mm audio jack with switch");
        }

        @Test
        void shouldMatchRealPowerJackMPNs() {
            // Real CUI DC power jack part numbers
            assertTrue(handler.matches("PJ-002A", ComponentType.CONNECTOR, registry),
                    "PJ-002A is a real CUI DC power jack");
        }

        @Test
        void shouldMatchRealSpeakerMPNs() {
            // Real CUI speaker part numbers
            assertTrue(handler.matches("CMS-15118-78P", ComponentType.SPEAKER, registry),
                    "CMS-15118-78P is a real CUI speaker");
        }

        @Test
        void shouldMatchRealBuzzerMPNs() {
            // Real CUI buzzer part numbers
            assertTrue(handler.matches("CMI-1295-85T", ComponentType.BUZZER, registry),
                    "CMI-1295-85T is a real CUI magnetic buzzer");
        }

        @Test
        void shouldMatchRealEncoderMPNs() {
            // Real CUI encoder part numbers
            assertTrue(handler.matches("AMT102-V", ComponentType.ENCODER, registry),
                    "AMT102-V is a real CUI incremental encoder");
        }
    }
}
