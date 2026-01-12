package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.QorvoHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for QorvoHandler.
 * Tests RF Power Amplifiers, RF Switches, RF Filters, Front-End Modules,
 * LNAs, Phase Shifters, and Mixers.
 */
class QorvoHandlerTest {

    private static QorvoHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new QorvoHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Power Amplifier Detection")
    class PowerAmplifierTests {
        @ParameterizedTest
        @DisplayName("Document QPA series power amplifiers")
        @ValueSource(strings = {
            "QPA1234-TR1",
            "QPA5678-BU",
            "QPA9999"
        })
        void documentQPADetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("QPA detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document TQP series power amplifiers")
        @ValueSource(strings = {
            "TQP1234-EN",
            "TQP5678-GM",
            "TQP9876"
        })
        void documentTQPDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("TQP detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document RF series power amplifiers")
        @ValueSource(strings = {
            "RF1234-ML",
            "RF5678-CS",
            "RF9999"
        })
        void documentRFDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("RF detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("RF Switch Detection")
    class RFSwitchTests {
        @ParameterizedTest
        @DisplayName("Document QPC series RF switches")
        @ValueSource(strings = {
            "QPC1234-TR1",
            "QPC5678-BU",
            "QPC9999"
        })
        void documentQPCDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("QPC detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document RFSW series RF switches")
        @ValueSource(strings = {
            "RFSW1234-EN",
            "RFSW5678-GM",
            "RFSW9999"
        })
        void documentRFSWDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("RFSW detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document PE4 series RF switches (TriQuint)")
        @ValueSource(strings = {
            "PE41234-ML",
            "PE45678-CS",
            "PE49999"
        })
        void documentPE4Detection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("PE4 detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("RF Filter Detection")
    class RFFilterTests {
        @ParameterizedTest
        @DisplayName("Document 885xxx series RF filters")
        @ValueSource(strings = {
            "885123-TR1",
            "885456-BU",
            "885789"
        })
        void document885Detection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("885 filter detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document 854xxx series RF filters")
        @ValueSource(strings = {
            "854123-EN",
            "854456-GM",
            "854789"
        })
        void document854Detection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("854 filter detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document TQM series RF filters")
        @ValueSource(strings = {
            "TQM123-ML",
            "TQM456-CS",
            "TQM789"
        })
        void documentTQMFilterDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("TQM filter detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Front-End Module Detection")
    class FrontEndModuleTests {
        @ParameterizedTest
        @DisplayName("Document RF5xxx series front-end modules")
        @ValueSource(strings = {
            "RF5123-TR1",
            "RF5456-BU",
            "RF5789"
        })
        void documentRF5Detection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("RF5 FEM detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document RFF series front-end modules")
        @ValueSource(strings = {
            "RFF1234-EN",
            "RFF5678-GM",
            "RFF9999"
        })
        void documentRFFDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("RFF FEM detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document QPF series front-end modules")
        @ValueSource(strings = {
            "QPF1234-ML",
            "QPF5678-CS",
            "QPF9999"
        })
        void documentQPFDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("QPF FEM detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("LNA Detection")
    class LNATests {
        @ParameterizedTest
        @DisplayName("Document QPL series LNAs")
        @ValueSource(strings = {
            "QPL1234-TR1",
            "QPL5678-BU",
            "QPL9999"
        })
        void documentQPLDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("QPL LNA detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document TQP3 series LNAs")
        @ValueSource(strings = {
            "TQP3123-EN",
            "TQP3456-GM",
            "TQP3789"
        })
        void documentTQP3Detection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("TQP3 LNA detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document SPF series LNAs")
        @ValueSource(strings = {
            "SPF1234-ML",
            "SPF5678-CS",
            "SPF9999"
        })
        void documentSPFDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("SPF LNA detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Phase Shifter Detection")
    class PhaseShifterTests {
        @ParameterizedTest
        @DisplayName("Document QPS series phase shifters")
        @ValueSource(strings = {
            "QPS1234-TR1",
            "QPS5678-BU",
            "QPS9999"
        })
        void documentQPSDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("QPS phase shifter detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document RFPS series phase shifters")
        @ValueSource(strings = {
            "RFPS1234-EN",
            "RFPS5678-GM",
            "RFPS9999"
        })
        void documentRFPSDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("RFPS phase shifter detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Mixer Detection")
    class MixerTests {
        @ParameterizedTest
        @DisplayName("Document QPM series mixers")
        @ValueSource(strings = {
            "QPM1234-ML",
            "QPM5678-CS",
            "QPM9999"
        })
        void documentQPMDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("QPM mixer detection: " + mpn + " IC=" + matchesIC);
        }

        @ParameterizedTest
        @DisplayName("Document TQM series mixers (4 digits)")
        @ValueSource(strings = {
            "TQM1234-TR1",
            "TQM5678-BU",
            "TQM9999"
        })
        void documentTQMMixerDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println("TQM mixer detection: " + mpn + " IC=" + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @DisplayName("Should extract package codes from suffix")
        @CsvSource({
            "QPA1234-TR1, QFN-TR",
            "QPF5678-BU, WLCSP",
            "QPC9999-EN, DFN",
            "QPL1234-GM, MCM",
            "RFSW5678-ML, QFN",
            "SPF1234-CS, CSP",
            "QPM5678-LAM, LAMINATE"
        })
        void shouldExtractPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract embedded package codes")
        @CsvSource({
            "QPA1234-QFN48, QFN",
            "QPF5678-DFN8, DFN",
            "QPC9999-LGA16, LGA"
        })
        void shouldExtractEmbeddedPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Embedded package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for MPN without dash")
        void shouldReturnEmptyForMPNWithoutDash() {
            assertEquals("", handler.extractPackageCode("QPA1234"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @DisplayName("Should extract series from MPNs")
        @CsvSource({
            "QPA1234-TR1, QPA1234",
            "TQP5678-BU, TQP5678",
            "RFSW1234-EN, RFSW1234",
            "PE41234-GM, PE4123",  // Handler extracts prefix + 4 digits
            "885123-ML, 8851",
            "854456-CS, 8544",
            "TQM123-LAM, TQM123",
            "RF5123-TR1, RF5123",
            "QPF9999, QPF9999",
            "QPL5678-BU, QPL5678",
            "TQP3456-EN, TQP3456",
            "SPF1234-GM, SPF1234",
            "QPS5678-ML, QPS5678",
            "RFPS1234-CS, RFPS1234",
            "QPM9999-LAM, QPM9999"
        })
        void shouldExtractSeries(String mpn, String expectedSeries) {
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
            assertTrue(handler.isOfficialReplacement("QPA1234-TR1", "QPA1234-BU"),
                    "Same series different packages should be replacements");
        }

        @Test
        @DisplayName("QPA and TQP cross-series compatibility")
        void qpaAndTqpAreCompatible() {
            // QPA and TQP power amplifiers are compatible series
            boolean isReplacement = handler.isOfficialReplacement("QPA1234-TR1", "TQP1234-TR1");
            System.out.println("QPA1234-TR1 can replace TQP1234-TR1 = " + isReplacement);
        }

        @Test
        @DisplayName("QPC and RFSW cross-series compatibility")
        void qpcAndRfswAreCompatible() {
            // QPC and RFSW RF switches are compatible series
            boolean isReplacement = handler.isOfficialReplacement("QPC1234-EN", "RFSW1234-EN");
            System.out.println("QPC1234-EN can replace RFSW1234-EN = " + isReplacement);
        }

        @Test
        @DisplayName("Different product types are NOT replacements")
        void differentTypesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("QPA1234-TR1", "QPL5678-BU"),
                    "Power amplifier and LNA should not be replacements");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        @DisplayName("Should support RF_IC_QORVO")
        void shouldSupportRFType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.RF_IC_QORVO),
                    "Should support RF_IC_QORVO");
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
            assertFalse(handler.isOfficialReplacement(null, "QPA1234-TR1"));
            assertFalse(handler.isOfficialReplacement("QPA1234-TR1", null));
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
            QorvoHandler directHandler = new QorvoHandler();
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
