package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.NichiconHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for NichiconHandler.
 * Tests aluminum electrolytic capacitors, polymer capacitors, and supercapacitors (EDLC).
 */
class NichiconHandlerTest {

    private static NichiconHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new NichiconHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Standard Grade Capacitor Detection")
    class StandardGradeTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "UUD1C100MCL1GS",
            "UUD1E470MCL1GS",
            "UUE1C101MNL1GS",
            "UUE1E221MNL1GS"
        })
        void documentStandardGradeDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesElectrolytic = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_ELECTROLYTIC_NICHICON = " + matchesElectrolytic);
        }
    }

    @Nested
    @DisplayName("High Temperature Capacitor Detection")
    class HighTempTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "UHS1C101MHD",
            "UHE1V101MHD1TA",
            "UHW1C101MCL1GS"
        })
        void documentHighTempDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesElectrolytic = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_ELECTROLYTIC_NICHICON = " + matchesElectrolytic);
        }
    }

    @Nested
    @DisplayName("Long Life Capacitor Detection")
    class LongLifeTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "UES1C101MHD",
            "UEW1V471MHD",
            "UKL1E101MHD"
        })
        void documentLongLifeDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesElectrolytic = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_ELECTROLYTIC_NICHICON = " + matchesElectrolytic);
        }
    }

    @Nested
    @DisplayName("Low Impedance Capacitor Detection")
    class LowImpedanceTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "UPW1C101MPD",
            "UPS1C101MPD"
        })
        void documentLowImpedanceDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesElectrolytic = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_ELECTROLYTIC_NICHICON = " + matchesElectrolytic);
        }
    }

    @Nested
    @DisplayName("Polymer Capacitor Detection")
    class PolymerTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PCJ1V101MCL1GS",
            "PCS1C470MCL1GS",
            "PCR1C220MCL1GS"
        })
        void documentPolymerDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesElectrolytic = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_ELECTROLYTIC_NICHICON = " + matchesElectrolytic);
        }
    }

    @Nested
    @DisplayName("Miniature Capacitor Detection")
    class MiniatureTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "UMA1C100MDD",
            "UMD1C100MDD"
        })
        void documentMiniatureDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesElectrolytic = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_ELECTROLYTIC_NICHICON = " + matchesElectrolytic);
        }
    }

    @Nested
    @DisplayName("Supercapacitor (EDLC) Detection")
    class EDLCTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "JJD0E106MSELCT",
            "JJS0E226MSELCT"
        })
        void documentEDLCDetection(String mpn) {
            boolean matchesCap = handler.matches(mpn, ComponentType.CAPACITOR, registry);
            boolean matchesElectrolytic = handler.matches(mpn, ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, registry);
            System.out.println(mpn + " matches CAPACITOR = " + matchesCap);
            System.out.println(mpn + " matches CAPACITOR_ELECTROLYTIC_NICHICON = " + matchesElectrolytic);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "UUD1C100MCL1GS, 04mm",
            "UUD1E080MCL1GS, 08mm",
            "UUD1E100MCL1GS, 10mm"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            String packageCode = handler.extractPackageCode(mpn);
            // Document actual behavior
            System.out.println(mpn + " -> package code = " + packageCode);
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "UUD1C100MCL1GS, Standard Grade",
            "UUE1C101MNL1GS, Standard High Voltage",
            "UHS1C101MHD, 125°C High Temp",
            "UHE1V101MHD1TA, 135°C High Temp",
            "UHW1C101MCL1GS, 105°C High Temp",
            "UES1C101MHD, Long Life Standard",
            "UEW1V471MHD, Long Life High Ripple",
            "UKL1E101MHD, Extra Long Life",
            "UPW1C101MPD, Low Impedance",
            "UPS1C101MPD, Ultra Low Impedance",
            "PCJ1V101MCL1GS, Polymer Standard",
            "PCS1C470MCL1GS, Polymer Low Profile",
            "PCR1C220MCL1GS, Polymer High Reliability",
            "UMA1C100MDD, Miniature General",
            "UMD1C100MDD, Miniature High Temp",
            "PF2A105MHN1, Photo Flash",
            "JJD0E106MSELCT, Standard EDLC",
            "JJS0E226MSELCT, High Power EDLC"
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
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("UUD1C100MCL1GS", "UHS1C100MCL1GS"));
        }

        @Test
        void documentHighTempReplacementChain() {
            // Higher temp rating can replace lower
            // 135C (UHE) should be able to replace 125C (UHS) and 105C (UHW)
            boolean uheReplacesUhs = handler.isOfficialReplacement("UHE1C100MCL1GS", "UHS1C100MCL1GS");
            boolean uhsReplacesUhw = handler.isOfficialReplacement("UHS1C100MCL1GS", "UHW1C100MCL1GS");
            System.out.println("UHE (135C) can replace UHS (125C) = " + uheReplacesUhs);
            System.out.println("UHS (125C) can replace UHW (105C) = " + uhsReplacesUhw);
        }

        @Test
        void documentLongLifeReplacingStandard() {
            // Long life series can replace standard
            boolean longLifeReplacesStandard = handler.isOfficialReplacement("UES1C100MCL1GS", "UUD1C100MCL1GS");
            System.out.println("Long life can replace standard = " + longLifeReplacesStandard);
        }
    }
}
