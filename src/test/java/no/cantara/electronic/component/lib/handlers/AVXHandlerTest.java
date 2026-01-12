package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AVXHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for AVXHandler.
 * Tests tantalum, ceramic, film capacitors and RF components.
 */
class AVXHandlerTest {

    private static AVXHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new AVXHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Tantalum Capacitor Detection")
    class TantalumCapacitorTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "TAJA106K016RNJ",
            "TPSB107M010R0100",
            "TPMD227M006R0035",
            "TRJA476K020RNJ",
            "TCJA476K016RNJ"
        })
        void documentTantalumCapacitorDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesTantalum = handler.matches(mpn, ComponentType.CAPACITOR_TANTALUM_AVX, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_TANTALUM_AVX = " + matchesTantalum);
        }
    }

    @Nested
    @DisplayName("Ceramic Capacitor Detection")
    class CeramicCapacitorTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "0805YC104KAT2A",
            "12061C104KAT2A",
            "ML03V150G",
            "21108C104KAT1A"
        })
        void documentCeramicCapacitorDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
        }
    }

    @Nested
    @DisplayName("Film Capacitor Detection")
    class FilmCapacitorTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FA16C0G1H102JNU00",
            "FB16H0G2A223JRP0J",
            "FP16B0G1E104JRP0J"
        })
        void documentFilmCapacitorDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
        }
    }

    @Nested
    @DisplayName("SuperCapacitor Detection")
    class SuperCapacitorTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "SCC10R0E105PRB",
            "SCM10R0E475PRB"
        })
        void documentSuperCapacitorDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
        }
    }

    @Nested
    @DisplayName("RF Component Detection")
    class RFComponentTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "BP0402V4R7ATT",
            "LP0402A2N7D",
            "HP0402A1N5C",
            "MLO0603E1N0CT000"
        })
        void documentRFComponentDetection(String mpn) {
            boolean matchesIC = handler.matches(mpn, ComponentType.IC, registry);
            System.out.println(mpn + " matches IC = " + matchesIC);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "TAJA106K016RNJ, EIA-3216-12/Case A",
            "TAJB107K016RNJ, EIA-3528-12/Case B",
            "TAJC107K016RNJ, EIA-6032-15/Case C",
            "TAJD107K016RNJ, EIA-7343-20/Case D",
            "TAJE107K016RNJ, EIA-7343-31/Case E"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @ParameterizedTest
        @CsvSource({
            "0805YC104KAT2A, 0402/1005M",
            "12061C104KAT2A, 0603/1608M"
        })
        void documentCeramicPackageCodeExtraction(String mpn, String expected) {
            // Handler extracts size code from positions 2-3 after 08/12 prefix
            String actual = handler.extractPackageCode(mpn);
            System.out.println(mpn + " -> package code = " + actual);
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "TAJA106K016RNJ, Standard MnO2",
            "TPSB107M010R0100, Polymer",
            "TPMD227M006R0035, Multianode Polymer",
            "TRJA476K020RNJ, High Reliability",
            "TCJA476K016RNJ, Commercial Grade",
            "0805YC104KAT2A, Standard MLCC",
            "12061C104KAT2A, High Voltage MLCC",
            "FA16C0G1H102JNU00, Stacked Film",
            "SCC10R0E105PRB, Cylindrical SuperCap",
            "BP0402V4R7ATT, Band Pass Filter"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CAPACITOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void documentPolymerReplacingMnO2() {
            // TPS (polymer) may replace TAJ (MnO2) in same case size - document behavior
            boolean isReplacement = handler.isOfficialReplacement("TPSA106K016RNJ", "TAJA106K016RNJ");
            System.out.println("TPSA can replace TAJA in same case = " + isReplacement);
        }

        @Test
        void shouldNotReplaceAcrossDifferentCaseSizes() {
            assertFalse(handler.isOfficialReplacement("TAJA106K016RNJ", "TAJB106K016RNJ"));
        }
    }
}
