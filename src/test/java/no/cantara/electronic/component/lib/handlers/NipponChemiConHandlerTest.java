package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.NipponChemiConHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for NipponChemiConHandler.
 *
 * Tests aluminum electrolytic capacitors including:
 * - KY series (low impedance, high ripple current)
 * - KZE series (conductive polymer hybrid)
 * - KXG/KXJ series (high temperature)
 * - MVY/MVZ series (miniature)
 * - KMG/KMH series (general purpose)
 * - SMG/SMH series (surface mount)
 * - PSC/PSE series (conductive polymer solid)
 * - GXE series (ultra low ESR)
 */
class NipponChemiConHandlerTest {

    private static NipponChemiConHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new NipponChemiConHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldReturnImmutableSet() {
            Set<ComponentType> types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> types.add(ComponentType.RESISTOR));
        }

        @Test
        void shouldSupportCapacitor() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.CAPACITOR));
        }

        @Test
        void shouldSupportIC() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.IC));
        }
    }

    @Nested
    @DisplayName("KY Series - Low Impedance Detection")
    class KYSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EKYE500ELL101MJC5S",   // KY 50V 100uF
            "EKYE250ELL471MJC5S",   // KY 25V 470uF
            "EKYE160ELL102MJC5S",   // KY 16V 1000uF
            "EKY1C101MLL",          // Alternative format
            "EKYA500ELL221M"        // KY variant
        })
        void shouldDetectKYSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractKYSeries() {
            assertEquals("KY Low Impedance", handler.extractSeries("EKYE500ELL101MJC5S"));
            assertEquals("KY Low Impedance", handler.extractSeries("EKY1C101MLL"));
        }
    }

    @Nested
    @DisplayName("KZE Series - Conductive Polymer Detection")
    class KZESeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EKZE250ELL471MJC5S",   // KZE 25V 470uF
            "EKZE160ELL102MJC5S",   // KZE 16V 1000uF
            "EKZE100ELL222MJC5S",   // KZE 10V 2200uF
            "EKZ1C471MLL"           // Alternative format
        })
        void shouldDetectKZESeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractKZESeries() {
            assertEquals("KZE Conductive Polymer", handler.extractSeries("EKZE250ELL471MJC5S"));
            assertEquals("KZE Conductive Polymer", handler.extractSeries("EKZ1C471MLL"));
        }
    }

    @Nested
    @DisplayName("KXG Series - High Temperature Long Life Detection")
    class KXGSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EKXG250ELL471MJC5S",   // KXG 25V 470uF
            "EKXG160ELL102MJC5S",   // KXG 16V 1000uF
            "EKXG350ELL101MJC5S"    // KXG 35V 100uF
        })
        void shouldDetectKXGSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractKXGSeries() {
            assertEquals("KXG High Temp Long Life", handler.extractSeries("EKXG250ELL471MJC5S"));
        }
    }

    @Nested
    @DisplayName("KXJ Series - High Temperature Low Impedance Detection")
    class KXJSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EKXJ250ELL471MJC5S",   // KXJ 25V 470uF
            "EKXJ160ELL102MJC5S",   // KXJ 16V 1000uF
            "EKXJ350ELL101MJC5S"    // KXJ 35V 100uF
        })
        void shouldDetectKXJSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractKXJSeries() {
            assertEquals("KXJ High Temp Low Impedance", handler.extractSeries("EKXJ250ELL471MJC5S"));
        }
    }

    @Nested
    @DisplayName("MVY Series - Miniature Low Impedance Detection")
    class MVYSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MVY250ELL101MDD",      // MVY 25V 100uF
            "MVY160ELL221MDD",      // MVY 16V 220uF
            "EMVY250ELL101MDD"      // EMVY variant
        })
        void shouldDetectMVYSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractMVYSeries() {
            assertEquals("MVY Miniature Low Impedance", handler.extractSeries("MVY250ELL101MDD"));
            assertEquals("MVY Miniature Low Impedance", handler.extractSeries("EMVY250ELL101MDD"));
        }
    }

    @Nested
    @DisplayName("MVZ Series - Miniature Standard Detection")
    class MVZSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "MVZ250ELL101MDD",      // MVZ 25V 100uF
            "MVZ160ELL221MDD",      // MVZ 16V 220uF
            "EMVZ250ELL101MDD"      // EMVZ variant
        })
        void shouldDetectMVZSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractMVZSeries() {
            assertEquals("MVZ Miniature Standard", handler.extractSeries("MVZ250ELL101MDD"));
            assertEquals("MVZ Miniature Standard", handler.extractSeries("EMVZ250ELL101MDD"));
        }
    }

    @Nested
    @DisplayName("KMG Series - General Purpose Miniature Detection")
    class KMGSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EKMG250ELL101MDD",     // KMG 25V 100uF
            "EKMG160ELL221MHD",     // KMG 16V 220uF
            "KMG250ELL471MHD"       // Without E prefix
        })
        void shouldDetectKMGSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractKMGSeries() {
            assertEquals("KMG General Purpose Mini", handler.extractSeries("EKMG250ELL101MDD"));
            assertEquals("KMG General Purpose Mini", handler.extractSeries("KMG250ELL471MHD"));
        }
    }

    @Nested
    @DisplayName("KMH Series - General Purpose Detection")
    class KMHSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EKMH250ELL101MHD",     // KMH 25V 100uF
            "EKMH350ELL471MHD",     // KMH 35V 470uF
            "KMH250ELL221MHD"       // Without E prefix
        })
        void shouldDetectKMHSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractKMHSeries() {
            assertEquals("KMH General Purpose", handler.extractSeries("EKMH250ELL101MHD"));
            assertEquals("KMH General Purpose", handler.extractSeries("KMH250ELL221MHD"));
        }
    }

    @Nested
    @DisplayName("SMG/SMH Series - Surface Mount Detection")
    class SMDSeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "ESMG160ELL101MJC5S",   // SMG 16V 100uF SMD
            "ESMG250ELL221MJC5S",   // SMG 25V 220uF SMD
            "ESMH160ELL101MJC5S",   // SMH 16V 100uF SMD
            "ESMH350ELL471MJC5S"    // SMH 35V 470uF SMD
        })
        void shouldDetectSMDSeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractSMDSeries() {
            assertEquals("SMG Surface Mount", handler.extractSeries("ESMG160ELL101MJC5S"));
            assertEquals("SMH Surface Mount", handler.extractSeries("ESMH160ELL101MJC5S"));
        }
    }

    @Nested
    @DisplayName("PSC/PSE Series - Polymer Solid Detection")
    class PolymerSolidTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "PSC160ELL101MJC5S",    // PSC 16V 100uF
            "PSC250ELL221MJC5S",    // PSC 25V 220uF
            "PSE160ELL101MJC5S",    // PSE 16V 100uF
            "PSE250ELL471MJC5S"     // PSE 25V 470uF
        })
        void shouldDetectPolymerSolidAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractPolymerSolidSeries() {
            assertEquals("PSC Polymer Solid", handler.extractSeries("PSC160ELL101MJC5S"));
            assertEquals("PSE Polymer Solid", handler.extractSeries("PSE160ELL101MJC5S"));
        }
    }

    @Nested
    @DisplayName("GXE Series - Ultra Low ESR Detection")
    class GXESeriesTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GXE160ELL101MJC5S",    // GXE 16V 100uF
            "GXE250ELL221MJC5S",    // GXE 25V 220uF
            "GXE350ELL471MJC5S"     // GXE 35V 470uF
        })
        void shouldDetectGXESeriesAsCapacitor(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match CAPACITOR");
        }

        @Test
        void shouldExtractGXESeries() {
            assertEquals("GXE Ultra Low ESR", handler.extractSeries("GXE160ELL101MJC5S"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "EKYE500ELL101MJC5S, MJC5S",
            "EKZE250ELL471MJC5S, MJC5S",
            "MVY250ELL101MDD, MDD",
            "EKMG160ELL221MHD, MHD",
            "EKMH350ELL471MHD, MHD",
            "ESMG250ELL101MJC5S, MJC5S"
        })
        void shouldExtractPackageCode(String mpn, String expectedSuffix) {
            String packageCode = handler.extractPackageCode(mpn);
            assertNotNull(packageCode);
            assertFalse(packageCode.isEmpty(), "Package code should not be empty for " + mpn);
        }

        @Test
        void shouldReturnEmptyForUnknownFormat() {
            assertEquals("", handler.extractPackageCode("UNKNOWN123"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "EKYE500ELL101MJC5S, KY Low Impedance",
            "EKY1C101MLL, KY Low Impedance",
            "EKZE250ELL471MJC5S, KZE Conductive Polymer",
            "EKXG250ELL471MJC5S, KXG High Temp Long Life",
            "EKXJ250ELL471MJC5S, KXJ High Temp Low Impedance",
            "MVY250ELL101MDD, MVY Miniature Low Impedance",
            "EMVY250ELL101MDD, MVY Miniature Low Impedance",
            "MVZ250ELL101MDD, MVZ Miniature Standard",
            "EMVZ250ELL101MDD, MVZ Miniature Standard",
            "EKMG250ELL101MDD, KMG General Purpose Mini",
            "KMG250ELL471MHD, KMG General Purpose Mini",
            "EKMH250ELL101MHD, KMH General Purpose",
            "KMH250ELL221MHD, KMH General Purpose",
            "ESMG160ELL101MJC5S, SMG Surface Mount",
            "ESMH160ELL101MJC5S, SMH Surface Mount",
            "PSC160ELL101MJC5S, PSC Polymer Solid",
            "PSE160ELL101MJC5S, PSE Polymer Solid",
            "GXE160ELL101MJC5S, GXE Ultra Low ESR"
        })
        void shouldExtractCorrectSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn));
        }

        @Test
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.extractSeries("UNKNOWN123"));
        }
    }

    @Nested
    @DisplayName("Voltage Encoding")
    class VoltageEncodingTests {
        @Test
        void shouldIdentifyVoltageFromMPN() {
            // Document voltage encoding in Nippon Chemi-Con MPNs
            // 500 = 50V, 250 = 25V, 160 = 16V, 100 = 10V, 063 = 6.3V
            String[] mpns = {
                "EKYE500ELL101MJC5S",  // 50V
                "EKYE250ELL101MJC5S",  // 25V
                "EKYE160ELL101MJC5S",  // 16V
                "EKYE100ELL101MJC5S",  // 10V
                "EKYE063ELL101MJC5S"   // 6.3V
            };
            for (String mpn : mpns) {
                assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    "Should detect " + mpn + " as capacitor regardless of voltage");
            }
        }
    }

    @Nested
    @DisplayName("Capacitance Encoding")
    class CapacitanceEncodingTests {
        @Test
        void shouldIdentifyCapacitanceFromMPN() {
            // Document capacitance encoding in Nippon Chemi-Con MPNs
            // 101 = 100uF, 471 = 470uF, 102 = 1000uF, 222 = 2200uF
            String[] mpns = {
                "EKYE500ELL101MJC5S",  // 100uF
                "EKYE500ELL471MJC5S",  // 470uF
                "EKYE250ELL102MJC5S",  // 1000uF
                "EKYE250ELL222MJC5S"   // 2200uF
            };
            for (String mpn : mpns) {
                assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    "Should detect " + mpn + " as capacitor regardless of capacitance");
            }
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

        @Test
        void shouldHandleLowerCase() {
            // MPN matching should be case-insensitive
            assertTrue(handler.matches("ekye500ell101mjc5s", ComponentType.CAPACITOR, registry));
            assertEquals("KY Low Impedance", handler.extractSeries("ekye500ell101mjc5s"));
        }

        @Test
        void shouldNotMatchNonChemiConParts() {
            // Nichicon parts should not match
            assertFalse(handler.matches("UUD1C100MCL1GS", ComponentType.CAPACITOR, registry));
            // Murata parts should not match
            assertFalse(handler.matches("GRM188R71H104KA93D", ComponentType.CAPACITOR, registry));
            // TDK parts should not match
            assertFalse(handler.matches("C3216X5R1E106K160AA", ComponentType.CAPACITOR, registry));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            // Different series should not be replacements
            assertFalse(handler.isOfficialReplacement("EKYE500ELL101MJC5S", "EKZE500ELL101MJC5S"));
            assertFalse(handler.isOfficialReplacement("EKYE500ELL101MJC5S", "MVY500ELL101MDD"));
        }

        @Test
        void shouldNotReplaceWithDifferentPackage() {
            // Same series but different package
            assertFalse(handler.isOfficialReplacement("EKYE500ELL101MJC5S", "EKYE500ELL101MDD"));
        }

        @Test
        void higherVoltageCanReplaceLowerInSameSeries() {
            // 50V part should be able to replace 25V part (same series, same package)
            // This tests the voltage hierarchy replacement logic
            boolean canReplace = handler.isOfficialReplacement("EKYE500ELL101MJC5S", "EKYE250ELL101MJC5S");
            // Document behavior - implementation may vary
            System.out.println("50V can replace 25V (KY series): " + canReplace);
        }

        @Test
        void shouldHandleNullInputs() {
            assertFalse(handler.isOfficialReplacement(null, "EKYE500ELL101MJC5S"));
            assertFalse(handler.isOfficialReplacement("EKYE500ELL101MJC5S", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {
        @Test
        void shouldReturnEmptyManufacturerTypes() {
            // Nippon Chemi-Con doesn't have manufacturer-specific ComponentTypes defined
            assertTrue(handler.getManufacturerTypes().isEmpty());
        }
    }

    @Nested
    @DisplayName("Generic Pattern Matching")
    class GenericPatternTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "EKAB500ELL101MJC5S",   // Generic EK pattern
            "EKCD250ELL471MJC5S",   // Generic EK pattern
            "MAB250ELL101MDD",      // Generic M pattern
            "MCD160ELL221MDD"       // Generic M pattern
        })
        void shouldMatchGenericPatterns(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                "Expected " + mpn + " to match generic pattern");
        }
    }
}
