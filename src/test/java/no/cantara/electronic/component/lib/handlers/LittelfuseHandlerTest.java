package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.MPNUtils;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.LittelfuseHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for LittelfuseHandler.
 * Tests pattern matching, package code extraction, series extraction, and component specifications.
 *
 * Littelfuse Products Covered:
 * - TVS Diodes: SMAJ, SMBJ, SMCJ, SMDJ, P4KE, P6KE, 1.5KE series
 * - Fuses: 0451, 0452, 0453, 0454, 0448 series (NANO2)
 * - Varistors: V series MOVs, MLE multilayer
 */
class LittelfuseHandlerTest {

    private static LittelfuseHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Direct instantiation to avoid circular initialization issues
        handler = new LittelfuseHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("TVS Diode Detection - SMAJ Series (400W, SMA Package)")
    class SMAJSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect SMAJ unidirectional TVS diodes")
        @ValueSource(strings = {"SMAJ5.0A", "SMAJ6.0A", "SMAJ10A", "SMAJ15A", "SMAJ24A", "SMAJ33A", "SMAJ58A"})
        void shouldDetectSMAJUnidirectional(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE (base type)");
            assertFalse(handler.isBidirectional(mpn),
                    mpn + " should be unidirectional (A suffix)");
        }

        @ParameterizedTest
        @DisplayName("Should detect SMAJ bidirectional TVS diodes")
        @ValueSource(strings = {"SMAJ5.0CA", "SMAJ10CA", "SMAJ15CA", "SMAJ24CA", "SMAJ33CA", "SMAJ58CA"})
        void shouldDetectSMAJBidirectional(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
            assertTrue(handler.isBidirectional(mpn),
                    mpn + " should be bidirectional (CA suffix)");
        }

        @Test
        @DisplayName("SMAJ series should extract correct package and series")
        void shouldExtractSMAJInfo() {
            assertEquals("SMA", handler.extractPackageCode("SMAJ5.0A"));
            assertEquals("SMA", handler.extractPackageCode("SMAJ33CA"));
            assertEquals("SMAJ", handler.extractSeries("SMAJ15A"));
            assertEquals("5.0", handler.extractVoltage("SMAJ5.0A"));
            assertEquals("33", handler.extractVoltage("SMAJ33CA"));
        }

        @Test
        @DisplayName("SMAJ series should have 400W power rating")
        void shouldHave400WPowerRating() {
            assertEquals(400, handler.getPowerRating("SMAJ5.0A"));
            assertEquals(400, handler.getPowerRating("SMAJ33CA"));
        }
    }

    @Nested
    @DisplayName("TVS Diode Detection - SMBJ Series (600W, SMB Package)")
    class SMBJSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect SMBJ TVS diodes")
        @ValueSource(strings = {"SMBJ5.0A", "SMBJ10A", "SMBJ15A", "SMBJ33A", "SMBJ5.0CA", "SMBJ33CA"})
        void shouldDetectSMBJVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
        }

        @Test
        @DisplayName("SMBJ series should extract correct package")
        void shouldExtractSMBJPackage() {
            assertEquals("SMB", handler.extractPackageCode("SMBJ5.0A"));
            assertEquals("SMB", handler.extractPackageCode("SMBJ33CA"));
            assertEquals("SMBJ", handler.extractSeries("SMBJ15A"));
        }

        @Test
        @DisplayName("SMBJ series should have 600W power rating")
        void shouldHave600WPowerRating() {
            assertEquals(600, handler.getPowerRating("SMBJ5.0A"));
            assertEquals(600, handler.getPowerRating("SMBJ33CA"));
        }
    }

    @Nested
    @DisplayName("TVS Diode Detection - SMCJ Series (1500W, SMC Package)")
    class SMCJSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect SMCJ TVS diodes")
        @ValueSource(strings = {"SMCJ5.0A", "SMCJ10A", "SMCJ33A", "SMCJ58A", "SMCJ5.0CA", "SMCJ33CA"})
        void shouldDetectSMCJVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
        }

        @Test
        @DisplayName("SMCJ series should extract correct package")
        void shouldExtractSMCJPackage() {
            assertEquals("SMC", handler.extractPackageCode("SMCJ5.0A"));
            assertEquals("SMCJ", handler.extractSeries("SMCJ15A"));
        }

        @Test
        @DisplayName("SMCJ series should have 1500W power rating")
        void shouldHave1500WPowerRating() {
            assertEquals(1500, handler.getPowerRating("SMCJ5.0A"));
        }
    }

    @Nested
    @DisplayName("TVS Diode Detection - P6KE Series (600W, Axial DO-15)")
    class P6KESeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect P6KE TVS diodes (VBR-based naming)")
        @ValueSource(strings = {"P6KE6.8A", "P6KE10A", "P6KE15A", "P6KE33A", "P6KE6.8CA", "P6KE15CA", "P6KE33CA"})
        void shouldDetectP6KEVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
            assertTrue(handler.matches(mpn, ComponentType.DIODE, registry),
                    mpn + " should match DIODE (base type)");
        }

        @Test
        @DisplayName("P6KE series should extract correct package (axial)")
        void shouldExtractP6KEPackage() {
            assertEquals("DO-15", handler.extractPackageCode("P6KE6.8A"));
            assertEquals("DO-15", handler.extractPackageCode("P6KE15CA"));
            assertEquals("P6KE", handler.extractSeries("P6KE10A"));
        }

        @Test
        @DisplayName("P6KE should extract voltage from VBR-based naming")
        void shouldExtractP6KEVoltage() {
            assertEquals("6.8", handler.extractVoltage("P6KE6.8A"));
            assertEquals("15", handler.extractVoltage("P6KE15CA"));
            assertEquals("33", handler.extractVoltage("P6KE33A"));
        }

        @Test
        @DisplayName("P6KE series should have 600W power rating")
        void shouldHave600WPowerRating() {
            assertEquals(600, handler.getPowerRating("P6KE6.8A"));
        }
    }

    @Nested
    @DisplayName("TVS Diode Detection - P4KE Series (400W, Axial DO-41)")
    class P4KESeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect P4KE TVS diodes")
        @ValueSource(strings = {"P4KE6.8A", "P4KE10A", "P4KE15A", "P4KE33CA"})
        void shouldDetectP4KEVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
        }

        @Test
        @DisplayName("P4KE series should extract DO-41 package")
        void shouldExtractP4KEPackage() {
            assertEquals("DO-41", handler.extractPackageCode("P4KE6.8A"));
            assertEquals("P4KE", handler.extractSeries("P4KE10A"));
        }

        @Test
        @DisplayName("P4KE series should have 400W power rating")
        void shouldHave400WPowerRating() {
            assertEquals(400, handler.getPowerRating("P4KE6.8A"));
        }
    }

    @Nested
    @DisplayName("TVS Diode Detection - 1.5KE Series (1500W, Axial DO-15)")
    class KE15SeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect 1.5KE TVS diodes")
        @ValueSource(strings = {"1.5KE6.8A", "1.5KE15A", "1.5KE33A", "1.5KE6.8CA", "1.5KE15CA"})
        void shouldDetect15KEVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
        }

        @Test
        @DisplayName("1.5KE series should extract correct info")
        void shouldExtract15KEInfo() {
            assertEquals("DO-15", handler.extractPackageCode("1.5KE15A"));
            assertEquals("1.5KE", handler.extractSeries("1.5KE15A"));
            assertEquals("15", handler.extractVoltage("1.5KE15A"));
        }

        @Test
        @DisplayName("1.5KE series should have 1500W power rating")
        void shouldHave1500WPowerRating() {
            assertEquals(1500, handler.getPowerRating("1.5KE15A"));
        }
    }

    @Nested
    @DisplayName("Fuse Detection - 0451/0452/0453/0454 NANO2 Series")
    class FuseSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect 0451 Very Fast Acting fuses")
        @ValueSource(strings = {"0451001.MRL", "0451002.MRL", "0451003.MRL", "0451005.MRL", "0451010.MRL"})
        void shouldDetect0451Fuses(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.FUSE_LITTELFUSE, registry),
                    mpn + " should match FUSE_LITTELFUSE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 0452 Slow Blow fuses")
        @ValueSource(strings = {"0452001.MRL", "0452002.MRL", "0452003.MRL", "0452005.MRL", "0452010.MRL"})
        void shouldDetect0452Fuses(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.FUSE_LITTELFUSE, registry),
                    mpn + " should match FUSE_LITTELFUSE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 0448 Nano2 SMF fuses")
        @ValueSource(strings = {"0448.062", "0448.125", "0448.250", "0448.500", "0448001"})
        void shouldDetect0448Fuses(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.FUSE_LITTELFUSE, registry),
                    mpn + " should match FUSE_LITTELFUSE");
        }

        @Test
        @DisplayName("Fuse series should extract correct package and series")
        void shouldExtractFuseInfo() {
            assertEquals("NANO2", handler.extractPackageCode("0452005.MRL"));
            assertEquals("NANO2", handler.extractPackageCode("0451002.MRL"));
            assertEquals("NANO2-SMF", handler.extractPackageCode("0448.125"));
            assertEquals("0452", handler.extractSeries("0452005.MRL"));
            assertEquals("0451", handler.extractSeries("0451002.MRL"));
            assertEquals("0448", handler.extractSeries("0448.125"));
        }

        @Test
        @DisplayName("Should extract current rating from fuse MPN")
        void shouldExtractCurrentRating() {
            assertEquals("5", handler.extractCurrentRating("0452005.MRL"));
            assertEquals("2", handler.extractCurrentRating("0451002.MRL"));
            assertEquals("10", handler.extractCurrentRating("0452010.MRL"));
        }

        @ParameterizedTest
        @DisplayName("Should detect 154/155 series 5x20mm fuses")
        @ValueSource(strings = {"154001", "154002", "154003.1", "155001", "155002"})
        void shouldDetect154SeriesFuses(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.FUSE_LITTELFUSE, registry),
                    mpn + " should match FUSE_LITTELFUSE");
        }

        @ParameterizedTest
        @DisplayName("Should detect 21x series ceramic fuses")
        @ValueSource(strings = {"215001", "215002", "216001", "217001", "218001"})
        void shouldDetect21xSeriesFuses(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.FUSE_LITTELFUSE, registry),
                    mpn + " should match FUSE_LITTELFUSE");
        }
    }

    @Nested
    @DisplayName("Varistor Detection - V Series MOVs")
    class VaristorSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect V series radial MOVs")
        @ValueSource(strings = {"V07E130P", "V10E130P", "V14E130P", "V07E230P", "V20E230P"})
        void shouldDetectRadialMOVs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VARISTOR_LITTELFUSE, registry),
                    mpn + " should match VARISTOR_LITTELFUSE");
        }

        @ParameterizedTest
        @DisplayName("Should detect MLE series multilayer varistors")
        @ValueSource(strings = {"V18MLE0402N", "V18MLE0603N", "V26MLE0805N", "V33MLE1206N"})
        void shouldDetectMLEVaristors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.VARISTOR_LITTELFUSE, registry),
                    mpn + " should match VARISTOR_LITTELFUSE");
        }

        @Test
        @DisplayName("MLE varistors should extract correct package codes")
        void shouldExtractMLEPackage() {
            assertEquals("0402", handler.extractPackageCode("V18MLE0402N"));
            assertEquals("0603", handler.extractPackageCode("V18MLE0603N"));
            assertEquals("0805", handler.extractPackageCode("V26MLE0805N"));
            assertEquals("1206", handler.extractPackageCode("V33MLE1206N"));
        }

        @Test
        @DisplayName("Radial MOVs should extract disc diameter as package")
        void shouldExtractRadialMOVPackage() {
            assertEquals("07mm", handler.extractPackageCode("V07E130P"));
            assertEquals("10mm", handler.extractPackageCode("V10E130P"));
            assertEquals("14mm", handler.extractPackageCode("V14E130P"));
            assertEquals("20mm", handler.extractPackageCode("V20E230P"));
        }

        @Test
        @DisplayName("Varistors should extract series correctly")
        void shouldExtractVaristorSeries() {
            assertEquals("MLE", handler.extractSeries("V18MLE0603N"));
            assertEquals("V", handler.extractSeries("V07E130P"));
        }

        @Test
        @DisplayName("Varistors should extract voltage rating")
        void shouldExtractVaristorVoltage() {
            assertEquals("18", handler.extractVoltage("V18MLE0603N"));
            assertEquals("07", handler.extractVoltage("V07E130P"));
            assertEquals("130", handler.extractVoltage("V130E230P"));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same voltage and polarity TVS diodes should be replacements")
        void shouldDetectSameVoltageReplacements() {
            // Same series, same voltage, same directionality
            assertTrue(handler.isOfficialReplacement("SMAJ15A", "SMAJ15A"),
                    "Identical parts should be replacements");

            // SMAJ15A vs SMAJ15CA - different directionality should NOT be replacement
            assertFalse(handler.isOfficialReplacement("SMAJ15A", "SMAJ15CA"),
                    "Different directionality should not be replacement");

            // SMAJ15A vs SMAJ33A - different voltage should NOT be replacement
            assertFalse(handler.isOfficialReplacement("SMAJ15A", "SMAJ33A"),
                    "Different voltage should not be replacement");

            // SMAJ15A vs SMBJ15A - different series should NOT be replacement
            assertFalse(handler.isOfficialReplacement("SMAJ15A", "SMBJ15A"),
                    "Different series should not be replacement");
        }

        @Test
        @DisplayName("Bidirectional TVS diodes should be interchangeable within series")
        void shouldDetectBidirectionalReplacements() {
            assertTrue(handler.isOfficialReplacement("SMAJ15CA", "SMAJ15CA"),
                    "Same bidirectional parts should be replacements");
            assertFalse(handler.isOfficialReplacement("SMAJ15CA", "SMAJ15A"),
                    "Bidirectional should not replace unidirectional");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support TVS_DIODE_LITTELFUSE")
        void shouldSupportTVSDiode() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.TVS_DIODE_LITTELFUSE));
        }

        @Test
        @DisplayName("Should support FUSE_LITTELFUSE")
        void shouldSupportFuse() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.FUSE_LITTELFUSE));
        }

        @Test
        @DisplayName("Should support VARISTOR_LITTELFUSE")
        void shouldSupportVaristor() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.VARISTOR_LITTELFUSE));
        }

        @Test
        @DisplayName("Should support base DIODE type")
        void shouldSupportDiode() {
            assertTrue(handler.getSupportedTypes().contains(ComponentType.DIODE));
        }

        @Test
        @DisplayName("getSupportedTypes() should return immutable Set")
        void shouldReturnImmutableSet() {
            assertThrows(UnsupportedOperationException.class, () -> {
                handler.getSupportedTypes().add(ComponentType.IC);
            });
        }
    }

    @Nested
    @DisplayName("Additional TVS Series Coverage")
    class AdditionalTVSSeriesTests {

        @ParameterizedTest
        @DisplayName("Should detect P4SMA and P6SMB SMD P-series")
        @ValueSource(strings = {"P4SMA10A", "P4SMA33CA", "P6SMB10A", "P6SMB33CA"})
        void shouldDetectPSeriesSMD(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
        }

        @Test
        @DisplayName("P4SMA should return SMA package")
        void shouldExtractP4SMAPackage() {
            assertEquals("SMA", handler.extractPackageCode("P4SMA10A"));
            assertEquals("P4SMA", handler.extractSeries("P4SMA10A"));
            assertEquals(400, handler.getPowerRating("P4SMA10A"));
        }

        @Test
        @DisplayName("P6SMB should return SMB package")
        void shouldExtractP6SMBPackage() {
            assertEquals("SMB", handler.extractPackageCode("P6SMB10A"));
            assertEquals("P6SMB", handler.extractSeries("P6SMB10A"));
            assertEquals(600, handler.getPowerRating("P6SMB10A"));
        }

        @ParameterizedTest
        @DisplayName("Should detect SA/SAC series (500W axial)")
        @ValueSource(strings = {"SA5.0A", "SA10A", "SA15CA", "SAC5.0A", "SAC10A"})
        void shouldDetectSASeries(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TVS_DIODE_LITTELFUSE, registry),
                    mpn + " should match TVS_DIODE_LITTELFUSE");
        }

        @Test
        @DisplayName("SA series should have 500W power rating")
        void shouldHave500WPowerRating() {
            assertEquals(500, handler.getPowerRating("SA10A"));
            assertEquals(500, handler.getPowerRating("SAC10A"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully")
        void shouldHandleNullMPN() {
            assertFalse(handler.matches(null, ComponentType.TVS_DIODE_LITTELFUSE, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractVoltage(null));
            assertFalse(handler.isBidirectional(null));
            assertEquals(0, handler.getPowerRating(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMPN() {
            assertFalse(handler.matches("", ComponentType.TVS_DIODE_LITTELFUSE, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractVoltage(""));
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully")
        void shouldHandleNullType() {
            assertFalse(handler.matches("SMAJ15A", null, registry));
        }

        @Test
        @DisplayName("Should not match unrelated part numbers")
        void shouldNotMatchUnrelatedParts() {
            assertFalse(handler.matches("LM7805", ComponentType.TVS_DIODE_LITTELFUSE, registry));
            assertFalse(handler.matches("STM32F103", ComponentType.FUSE_LITTELFUSE, registry));
            assertFalse(handler.matches("GRM155R71H104", ComponentType.VARISTOR_LITTELFUSE, registry));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("smaj15a", ComponentType.TVS_DIODE_LITTELFUSE, registry));
            assertTrue(handler.matches("SMAJ15A", ComponentType.TVS_DIODE_LITTELFUSE, registry));
            assertTrue(handler.matches("Smaj15A", ComponentType.TVS_DIODE_LITTELFUSE, registry));
        }
    }

    @Nested
    @DisplayName("Integration with MPNUtils")
    class IntegrationTests {

        @Test
        @DisplayName("MPNUtils should detect SMAJ as Littelfuse")
        void shouldDetectLittelfuseViaUtils() {
            ManufacturerHandler h = MPNUtils.getManufacturerHandler("SMAJ15A");
            assertNotNull(h, "Should find handler for SMAJ15A");
            assertTrue(h instanceof LittelfuseHandler, "Handler should be LittelfuseHandler");
        }

        @Test
        @DisplayName("MPNUtils should detect P6KE as Littelfuse")
        void shouldDetectP6KEViaUtils() {
            ManufacturerHandler h = MPNUtils.getManufacturerHandler("P6KE15CA");
            assertNotNull(h, "Should find handler for P6KE15CA");
            assertTrue(h instanceof LittelfuseHandler, "Handler should be LittelfuseHandler");
        }

        @Test
        @DisplayName("MPNUtils should detect fuses as Littelfuse")
        void shouldDetectFusesViaUtils() {
            ManufacturerHandler h = MPNUtils.getManufacturerHandler("0452005.MRL");
            assertNotNull(h, "Should find handler for 0452005.MRL");
            assertTrue(h instanceof LittelfuseHandler, "Handler should be LittelfuseHandler");
        }
    }
}
