package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.KyoceraHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for KyoceraHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction, and replacement detection
 * for Kyocera electronic components including:
 * <ul>
 *   <li>CX/CXO/CSTS/PBRC series - Ceramic resonators</li>
 *   <li>KC/KT series - Crystal oscillators</li>
 *   <li>CT/CM series - Ceramic capacitors</li>
 *   <li>5600/5800 series - Connectors</li>
 * </ul>
 */
class KyoceraHandlerTest {

    private static KyoceraHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new KyoceraHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Ceramic Resonator Detection")
    class CeramicResonatorTests {

        @ParameterizedTest
        @DisplayName("Should detect CX series ceramic resonators")
        @ValueSource(strings = {
            "CX-3215GA",
            "CX-49FA",
            "CX3215GA",
            "CX49FA",
            "CX-3215GB",
            "CX-2012A"
        })
        void shouldDetectCXResonators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect CXO series ceramic resonators")
        @ValueSource(strings = {
            "CXO-3215",
            "CXO3215",
            "CXO-4000",
            "CXO-16000"
        })
        void shouldDetectCXOResonators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect CSTS series ceramic resonators")
        @ValueSource(strings = {
            "CSTS0400MA",
            "CSTS-0800MB",
            "CSTS1600MC",
            "CSTSV0400MA"
        })
        void shouldDetectCSTSResonators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @ParameterizedTest
        @DisplayName("Should detect PBRC series ceramic resonators")
        @ValueSource(strings = {
            "PBRC-4000",
            "PBRC4000",
            "PBRC-8000",
            "PBRC-16000"
        })
        void shouldDetectPBRCResonators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYSTAL, registry),
                    mpn + " should match CRYSTAL");
        }

        @Test
        @DisplayName("Resonators should NOT match OSCILLATOR type")
        void resonatorsShouldNotMatchOscillator() {
            assertFalse(handler.matches("CX-3215GA", ComponentType.OSCILLATOR, registry),
                    "CX series should NOT match OSCILLATOR");
            assertFalse(handler.matches("CSTS0400MA", ComponentType.OSCILLATOR, registry),
                    "CSTS series should NOT match OSCILLATOR");
        }
    }

    @Nested
    @DisplayName("Crystal Oscillator Detection")
    class CrystalOscillatorTests {

        @ParameterizedTest
        @DisplayName("Should detect KC series crystal oscillators")
        @ValueSource(strings = {
            "KC1612D-C3",
            "KC1612B-P3",
            "KC2520D-C3",
            "KC3225D",
            "KC5032A-C3"
        })
        void shouldDetectKCOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect KT series crystal oscillators")
        @ValueSource(strings = {
            "KT1612A",
            "KT2520B",
            "KT3225C",
            "KT5032D"
        })
        void shouldDetectKTOscillators(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OSCILLATOR, registry),
                    mpn + " should match OSCILLATOR");
        }

        @Test
        @DisplayName("Oscillators should NOT match CRYSTAL type")
        void oscillatorsShouldNotMatchCrystal() {
            assertFalse(handler.matches("KC1612D-C3", ComponentType.CRYSTAL, registry),
                    "KC series should NOT match CRYSTAL");
            assertFalse(handler.matches("KT2520B", ComponentType.CRYSTAL, registry),
                    "KT series should NOT match CRYSTAL");
        }
    }

    @Nested
    @DisplayName("Ceramic Capacitor Detection")
    class CeramicCapacitorTests {

        @ParameterizedTest
        @DisplayName("Should detect CT series ceramic capacitors")
        @ValueSource(strings = {
            "CT31B104K",
            "CT41A105K",
            "CT42A106M",
            "CT43B106M",
            "CT45B107M"
        })
        void shouldDetectCTCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    mpn + " should match CAPACITOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect CM series ceramic capacitors")
        @ValueSource(strings = {
            "CM03B104K",
            "CM05A105K",
            "CM06A106M",
            "CM21A107M"
        })
        void shouldDetectCMCapacitors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CAPACITOR, registry),
                    mpn + " should match CAPACITOR");
        }

        @Test
        @DisplayName("Capacitors should NOT match other types")
        void capacitorsShouldNotMatchOtherTypes() {
            assertFalse(handler.matches("CT31B104K", ComponentType.CRYSTAL, registry),
                    "CT series should NOT match CRYSTAL");
            assertFalse(handler.matches("CT31B104K", ComponentType.OSCILLATOR, registry),
                    "CT series should NOT match OSCILLATOR");
            assertFalse(handler.matches("CT31B104K", ComponentType.CONNECTOR, registry),
                    "CT series should NOT match CONNECTOR");
        }
    }

    @Nested
    @DisplayName("Connector Detection")
    class ConnectorTests {

        @ParameterizedTest
        @DisplayName("Should detect 5600 series connectors")
        @ValueSource(strings = {
            "5600-050-141",
            "5600-100-242",
            "5602-030-101",
            "5699-080-181"
        })
        void shouldDetect5600Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect 5800 series connectors")
        @ValueSource(strings = {
            "5800-050-141",
            "5800-100-242",
            "5802-030-101"
        })
        void shouldDetect5800Connectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @DisplayName("Should detect other numeric series connectors")
        @ValueSource(strings = {
            "24200-050-100",
            "12500-080-200",
            "34567-060-150"
        })
        void shouldDetectNumericSeriesConnectors(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        @DisplayName("Connectors should NOT match other types")
        void connectorsShouldNotMatchOtherTypes() {
            assertFalse(handler.matches("5600-050-141", ComponentType.CRYSTAL, registry),
                    "5600 series should NOT match CRYSTAL");
            assertFalse(handler.matches("5600-050-141", ComponentType.CAPACITOR, registry),
                    "5600 series should NOT match CAPACITOR");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract resonator package codes")
        @CsvSource({
            "CX-3215GA, 3.2x1.5mm",
            "CX-49FA, 4.5x2.0mm",
            "CX-2012A, 2.0x1.2mm",
            "CX-2520A, 2.5x2.0mm"
        })
        void shouldExtractResonatorPackageCodes(String mpn, String expectedPackage) {
            String packageCode = handler.extractPackageCode(mpn);
            assertNotNull(packageCode, "Package code for " + mpn + " should not be null");
            assertFalse(packageCode.isEmpty(), "Package code for " + mpn + " should not be empty");
        }

        @ParameterizedTest
        @DisplayName("Should extract oscillator package codes")
        @CsvSource({
            "KC1612D-C3, 1.6x1.2mm",
            "KC2016D-C3, 2.0x1.6mm",
            "KC2520D-C3, 2.5x2.0mm",
            "KC3225D-C3, 3.2x2.5mm",
            "KC5032D-C3, 5.0x3.2mm"
        })
        void shouldExtractOscillatorPackageCodes(String mpn, String expectedPackage) {
            String packageCode = handler.extractPackageCode(mpn);
            assertEquals(expectedPackage, packageCode, "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract capacitor package codes")
        @CsvSource({
            "CT31B104K, 0603/1608M",
            "CT41A105K, 0805/2012M",
            "CT42A106M, 1206/3216M",
            "CT43B106M, 1210/3225M"
        })
        void shouldExtractCapacitorPackageCodes(String mpn, String expectedPackage) {
            String packageCode = handler.extractPackageCode(mpn);
            assertEquals(expectedPackage, packageCode, "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract connector pin counts")
        @CsvSource({
            "5600-050-141, 50-pin",
            "5600-100-242, 100-pin",
            "5602-030-101, 30-pin",
            "5699-080-181, 80-pin"
        })
        void shouldExtractConnectorPinCounts(String mpn, String expectedPins) {
            String packageCode = handler.extractPackageCode(mpn);
            assertEquals(expectedPins, packageCode, "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid MPNs")
        void shouldReturnEmptyForInvalidMpns() {
            assertEquals("", handler.extractPackageCode("INVALID"));
            assertEquals("", handler.extractPackageCode("XYZ123"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract CX resonator series")
        @CsvSource({
            "CX-3215GA, CX Ceramic Resonator",
            "CX3215GB, CX Ceramic Resonator",
            "CX-49FA, CX Ceramic Resonator"
        })
        void shouldExtractCXSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CXO resonator series")
        @CsvSource({
            "CXO-3215, CXO Ceramic Resonator",
            "CXO4000, CXO Ceramic Resonator"
        })
        void shouldExtractCXOSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CSTS resonator series")
        @CsvSource({
            "CSTS0400MA, CSTS Ceramic Resonator",
            "CSTSV0400MA, CSTS Ceramic Resonator"
        })
        void shouldExtractCSTSSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract PBRC resonator series")
        @CsvSource({
            "PBRC-4000, PBRC Ceramic Resonator",
            "PBRC8000, PBRC Ceramic Resonator"
        })
        void shouldExtractPBRCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract KC oscillator series")
        @CsvSource({
            "KC1612D-C3, KC-D Crystal Oscillator",
            "KC1612B-P3, KC-B Crystal Oscillator",
            "KC2520A, KC Crystal Oscillator"
        })
        void shouldExtractKCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract KT oscillator series")
        @CsvSource({
            "KT1612A, KT Crystal Oscillator",
            "KT2520B, KT Crystal Oscillator"
        })
        void shouldExtractKTSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CT capacitor series")
        @CsvSource({
            "CT31B104K, CT31 Ceramic Capacitor",
            "CT41A105K, CT41 Ceramic Capacitor",
            "CT42A106M, CT42 Ceramic Capacitor"
        })
        void shouldExtractCTSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract CM capacitor series")
        @CsvSource({
            "CM03B104K, CM03 Ceramic Capacitor",
            "CM05A105K, CM05 Ceramic Capacitor"
        })
        void shouldExtractCMSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract connector series")
        @CsvSource({
            "5600-050-141, 5600 Connector",
            "5602-030-101, 5602 Connector",
            "5800-050-141, 5800 Connector"
        })
        void shouldExtractConnectorSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for unknown series")
        void shouldReturnEmptyForUnknownSeries() {
            assertEquals("", handler.extractSeries("UNKNOWN123"));
            assertEquals("", handler.extractSeries("XYZ456"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementDetectionTests {

        @Test
        @DisplayName("Same CX resonator series and package should be replacements")
        void sameCXSeriesPackageShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("CX-3215GA", "CX-3215GB"),
                    "Same series and package should be replacements");
        }

        @Test
        @DisplayName("Same KC oscillator series and package should be replacements")
        void sameKCSeriesPackageShouldBeReplacements() {
            assertTrue(handler.isOfficialReplacement("KC1612D-C3", "KC1612D-P3"),
                    "Same series and package should be replacements");
        }

        @Test
        @DisplayName("Different resonator series should NOT be replacements")
        void differentResonatorSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CX-3215GA", "CSTS0400MA"),
                    "Different series should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("CX-3215GA", "PBRC-4000"),
                    "Different series should NOT be replacements");
        }

        @Test
        @DisplayName("Different oscillator series should NOT be replacements")
        void differentOscillatorSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("KC1612D-C3", "KT1612A"),
                    "KC and KT series should NOT be replacements");
        }

        @Test
        @DisplayName("Different capacitor series should NOT be replacements")
        void differentCapacitorSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("CT31B104K", "CT41A104K"),
                    "Different CT sizes should NOT be replacements");
        }

        @Test
        @DisplayName("Null values should return false")
        void nullValuesShouldReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "CX-3215GA"),
                    "Null first argument should return false");
            assertFalse(handler.isOfficialReplacement("CX-3215GA", null),
                    "Null second argument should return false");
            assertFalse(handler.isOfficialReplacement(null, null),
                    "Both null should return false");
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.CRYSTAL),
                    "Should support CRYSTAL type");
            assertTrue(types.contains(ComponentType.OSCILLATOR),
                    "Should support OSCILLATOR type");
            assertTrue(types.contains(ComponentType.CAPACITOR),
                    "Should support CAPACITOR type");
            assertTrue(types.contains(ComponentType.CONNECTOR),
                    "Should support CONNECTOR type");
        }

        @Test
        @DisplayName("getSupportedTypes should use Set.of() (immutable)")
        void supportedTypesShouldBeImmutable() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            }, "getSupportedTypes() should return immutable set");
        }

        @Test
        @DisplayName("Should have exactly 4 supported types")
        void shouldHaveExpectedNumberOfTypes() {
            assertEquals(4, handler.getSupportedTypes().size(),
                    "Should support exactly 4 component types");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null MPN gracefully for matches")
        void shouldHandleNullMpnForMatches() {
            assertFalse(handler.matches(null, ComponentType.CRYSTAL, registry),
                    "Null MPN should return false");
        }

        @Test
        @DisplayName("Should handle null ComponentType gracefully for matches")
        void shouldHandleNullTypeForMatches() {
            assertFalse(handler.matches("CX-3215GA", null, registry),
                    "Null type should return false");
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully for matches")
        void shouldHandleEmptyMpnForMatches() {
            assertFalse(handler.matches("", ComponentType.CRYSTAL, registry),
                    "Empty MPN should return false");
        }

        @Test
        @DisplayName("Should handle null MPN for extractPackageCode")
        void shouldHandleNullMpnForPackageCode() {
            assertEquals("", handler.extractPackageCode(null),
                    "Null MPN should return empty string");
        }

        @Test
        @DisplayName("Should handle empty MPN for extractPackageCode")
        void shouldHandleEmptyMpnForPackageCode() {
            assertEquals("", handler.extractPackageCode(""),
                    "Empty MPN should return empty string");
        }

        @Test
        @DisplayName("Should handle null MPN for extractSeries")
        void shouldHandleNullMpnForSeries() {
            assertEquals("", handler.extractSeries(null),
                    "Null MPN should return empty string");
        }

        @Test
        @DisplayName("Should handle empty MPN for extractSeries")
        void shouldHandleEmptyMpnForSeries() {
            assertEquals("", handler.extractSeries(""),
                    "Empty MPN should return empty string");
        }

        @Test
        @DisplayName("Should be case insensitive for pattern matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("cx-3215ga", ComponentType.CRYSTAL, registry),
                    "Lowercase MPN should match");
            assertTrue(handler.matches("CX-3215GA", ComponentType.CRYSTAL, registry),
                    "Uppercase MPN should match");
            assertTrue(handler.matches("Cx-3215Ga", ComponentType.CRYSTAL, registry),
                    "Mixed case MPN should match");
        }

        @Test
        @DisplayName("Should handle MPNs without hyphens")
        void shouldHandleMpnsWithoutHyphens() {
            assertTrue(handler.matches("CX3215GA", ComponentType.CRYSTAL, registry),
                    "MPN without hyphen should match");
            assertTrue(handler.matches("KC1612DC3", ComponentType.OSCILLATOR, registry),
                    "Oscillator MPN without hyphen should match");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            KyoceraHandler directHandler = new KyoceraHandler();
            assertNotNull(directHandler, "Handler should be instantiated");

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            assertNotNull(directHandler.getSupportedTypes(),
                    "Supported types should not be null");
            assertFalse(directHandler.getSupportedTypes().isEmpty(),
                    "Supported types should not be empty");
        }

        @Test
        @DisplayName("getManufacturerTypes returns empty set")
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes,
                    "Manufacturer types should not be null");
            assertTrue(manufacturerTypes.isEmpty(),
                    "Manufacturer types should be empty");
        }

        @Test
        @DisplayName("Multiple pattern registrations should work")
        void multiplePatternRegistrationsShouldWork() {
            // Re-initialize patterns on fresh registry
            PatternRegistry freshRegistry = new PatternRegistry();
            handler.initializePatterns(freshRegistry);

            // Verify patterns are registered
            assertTrue(handler.matches("CX-3215GA", ComponentType.CRYSTAL, freshRegistry),
                    "Should match after re-initialization");
        }
    }

    @Nested
    @DisplayName("Cross-Type Verification")
    class CrossTypeVerificationTests {

        @Test
        @DisplayName("Each component type should only match its patterns")
        void componentTypesShouldNotCrossMatch() {
            // Resonator should only match CRYSTAL
            String resonator = "CX-3215GA";
            assertTrue(handler.matches(resonator, ComponentType.CRYSTAL, registry));
            assertFalse(handler.matches(resonator, ComponentType.OSCILLATOR, registry));
            assertFalse(handler.matches(resonator, ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches(resonator, ComponentType.CONNECTOR, registry));

            // Oscillator should only match OSCILLATOR
            String oscillator = "KC1612D-C3";
            assertFalse(handler.matches(oscillator, ComponentType.CRYSTAL, registry));
            assertTrue(handler.matches(oscillator, ComponentType.OSCILLATOR, registry));
            assertFalse(handler.matches(oscillator, ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches(oscillator, ComponentType.CONNECTOR, registry));

            // Capacitor should only match CAPACITOR
            String capacitor = "CT31B104K";
            assertFalse(handler.matches(capacitor, ComponentType.CRYSTAL, registry));
            assertFalse(handler.matches(capacitor, ComponentType.OSCILLATOR, registry));
            assertTrue(handler.matches(capacitor, ComponentType.CAPACITOR, registry));
            assertFalse(handler.matches(capacitor, ComponentType.CONNECTOR, registry));

            // Connector should only match CONNECTOR
            String connector = "5600-050-141";
            assertFalse(handler.matches(connector, ComponentType.CRYSTAL, registry));
            assertFalse(handler.matches(connector, ComponentType.OSCILLATOR, registry));
            assertFalse(handler.matches(connector, ComponentType.CAPACITOR, registry));
            assertTrue(handler.matches(connector, ComponentType.CONNECTOR, registry));
        }
    }

    @Nested
    @DisplayName("Real-World Part Numbers")
    class RealWorldPartNumberTests {

        @ParameterizedTest
        @DisplayName("Should handle real-world Kyocera resonator part numbers")
        @ValueSource(strings = {
            "CX-3215GA",
            "CX-49FAFT",
            "CSTS0400MG04",
            "PBRC-4.00HR"
        })
        void shouldHandleRealResonatorParts(String mpn) {
            boolean matches = handler.matches(mpn, ComponentType.CRYSTAL, registry);
            String series = handler.extractSeries(mpn);

            assertTrue(matches || !series.isEmpty(),
                    mpn + " should either match CRYSTAL or have extractable series");
        }

        @ParameterizedTest
        @DisplayName("Should handle real-world Kyocera oscillator part numbers")
        @ValueSource(strings = {
            "KC1612D32.768MR",
            "KC2520B4.000MR"
        })
        void shouldHandleRealOscillatorParts(String mpn) {
            boolean matches = handler.matches(mpn, ComponentType.OSCILLATOR, registry);
            String series = handler.extractSeries(mpn);

            assertTrue(matches || !series.isEmpty(),
                    mpn + " should either match OSCILLATOR or have extractable series");
        }
    }
}
