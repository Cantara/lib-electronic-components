package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.BekenHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for BekenHandler.
 * <p>
 * Tests pattern matching, package code extraction, series extraction,
 * wireless type detection, and replacement detection for Beken WiFi/BLE SoCs.
 * <p>
 * Beken product families:
 * <ul>
 *   <li>BK7231 series - WiFi + BLE combo</li>
 *   <li>BK7251 - Audio + WiFi</li>
 *   <li>BK3431 - BLE only</li>
 *   <li>BK3432 - BLE 5.0</li>
 *   <li>BK7256 - WiFi6 + BLE5.2</li>
 * </ul>
 */
class BekenHandlerTest {

    private static BekenHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new BekenHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("WiFi + BLE Combo Chips (BK7xxx) Detection")
    class Bk7xxxSeriesTests {

        @ParameterizedTest
        @DisplayName("Should match BK7231 series variants")
        @ValueSource(strings = {"BK7231", "BK7231N", "BK7231T", "BK7231U", "BK7231Q", "BK7231-QFN32"})
        void shouldMatchBk7231Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should match BK7251 series (Audio + WiFi)")
        @ValueSource(strings = {"BK7251", "BK7251N", "BK7251-QFN40"})
        void shouldMatchBk7251Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should match BK7256 series (WiFi6 + BLE5.2)")
        @ValueSource(strings = {"BK7256", "BK7256N", "BK7256-QFN48"})
        void shouldMatchBk7256Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("BK7xxx should be WiFi capable")
        void shouldBeWifiCapable() {
            assertTrue(handler.isWifiCapable("BK7231"));
            assertTrue(handler.isWifiCapable("BK7251"));
            assertTrue(handler.isWifiCapable("BK7256"));
        }

        @Test
        @DisplayName("BK7xxx should be BLE capable")
        void shouldBeBleCapable() {
            assertTrue(handler.isBleCapable("BK7231"));
            assertTrue(handler.isBleCapable("BK7251"));
            assertTrue(handler.isBleCapable("BK7256"));
        }
    }

    @Nested
    @DisplayName("BLE Only Chips (BK3xxx) Detection")
    class Bk3xxxSeriesTests {

        @ParameterizedTest
        @DisplayName("Should match BK3431 series (BLE)")
        @ValueSource(strings = {"BK3431", "BK3431N", "BK3431Q", "BK3431-QFN32"})
        void shouldMatchBk3431Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @ParameterizedTest
        @DisplayName("Should match BK3432 series (BLE 5.0)")
        @ValueSource(strings = {"BK3432", "BK3432N", "BK3432-QFN32"})
        void shouldMatchBk3432Series(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("BK3xxx should NOT be WiFi capable")
        void shouldNotBeWifiCapable() {
            assertFalse(handler.isWifiCapable("BK3431"));
            assertFalse(handler.isWifiCapable("BK3432"));
        }

        @Test
        @DisplayName("BK3xxx should be BLE capable")
        void shouldBeBleCapable() {
            assertTrue(handler.isBleCapable("BK3431"));
            assertTrue(handler.isBleCapable("BK3432"));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract WiFi+BLE series correctly")
        @CsvSource({
            "BK7231, BK7231",
            "BK7231N, BK7231",
            "BK7231-QFN32, BK7231",
            "BK7251, BK7251",
            "BK7251N, BK7251",
            "BK7256, BK7256",
            "BK7256-QFN48, BK7256"
        })
        void shouldExtractWifiBleSeriesCorrectly(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn + " should be " + expectedSeries);
        }

        @ParameterizedTest
        @DisplayName("Should extract BLE only series correctly")
        @CsvSource({
            "BK3431, BK3431",
            "BK3431N, BK3431",
            "BK3431-QFN32, BK3431",
            "BK3432, BK3432",
            "BK3432N, BK3432"
        })
        void shouldExtractBleSeriesCorrectly(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn + " should be " + expectedSeries);
        }

        @Test
        @DisplayName("Should return empty for non-Beken parts")
        void shouldReturnEmptyForNonBekenParts() {
            assertEquals("", handler.extractSeries("ESP32"));
            assertEquals("", handler.extractSeries("nRF52832"));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries(null));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract QFN package codes from suffix")
        @CsvSource({
            "BK7231-QFN32, QFN32",
            "BK7231-QFN-32, QFN-32",
            "BK7251-QFN40, QFN40",
            "BK7256-QFN48, QFN48"
        })
        void shouldExtractQfnPackageFromSuffix(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn + " should be " + expectedPackage);
        }

        @ParameterizedTest
        @DisplayName("Should extract QFN from embedded designation")
        @CsvSource({
            "BK7231QFN32, QFN32",
            "BK7231QFN40, QFN40",
            "BK7256QFN48, QFN48"
        })
        void shouldExtractQfnFromEmbedded(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn + " should be " + expectedPackage);
        }

        @ParameterizedTest
        @DisplayName("Should extract QFN from suffix letter")
        @CsvSource({
            "BK7231N, QFN",
            "BK7231Q, QFN"
        })
        void shouldExtractQfnFromSuffixLetter(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package for " + mpn + " should be " + expectedPackage);
        }

        @Test
        @DisplayName("Should return empty for bare part numbers")
        void shouldReturnEmptyForBareParts() {
            assertEquals("", handler.extractPackageCode("BK7231"));
            assertEquals("", handler.extractPackageCode("BK3431"));
        }

        @Test
        @DisplayName("Should handle null and empty inputs")
        void shouldHandleNullAndEmpty() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Wireless Type Detection")
    class WirelessTypeTests {

        @ParameterizedTest
        @DisplayName("Should detect WiFi+BLE type for BK7xxx")
        @CsvSource({
            "BK7231, WIFI_BLE",
            "BK7231N, WIFI_BLE",
            "BK7251, WIFI_BLE"
        })
        void shouldDetectWifiBleType(String mpn, String expectedType) {
            assertEquals(expectedType, handler.getWirelessType(mpn),
                    "Wireless type for " + mpn + " should be " + expectedType);
        }

        @Test
        @DisplayName("Should detect WiFi6+BLE5 for BK7256")
        void shouldDetectWifi6Ble5() {
            assertEquals("WIFI6_BLE5", handler.getWirelessType("BK7256"));
            assertEquals("WIFI6_BLE5", handler.getWirelessType("BK7256N"));
        }

        @ParameterizedTest
        @DisplayName("Should detect BLE type for BK3xxx")
        @CsvSource({
            "BK3431, BLE",
            "BK3431N, BLE"
        })
        void shouldDetectBleType(String mpn, String expectedType) {
            assertEquals(expectedType, handler.getWirelessType(mpn),
                    "Wireless type for " + mpn + " should be " + expectedType);
        }

        @Test
        @DisplayName("Should detect BLE5 for BK3432")
        void shouldDetectBle5() {
            assertEquals("BLE5", handler.getWirelessType("BK3432"));
            assertEquals("BLE5", handler.getWirelessType("BK3432N"));
        }

        @Test
        @DisplayName("Should return empty for non-Beken parts")
        void shouldReturnEmptyForNonBeken() {
            assertEquals("", handler.getWirelessType("ESP32"));
            assertEquals("", handler.getWirelessType(null));
            assertEquals("", handler.getWirelessType(""));
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series with different suffixes should be replacements")
        void sameSerisDifferentSuffixes() {
            assertTrue(handler.isOfficialReplacement("BK7231", "BK7231N"),
                    "BK7231 and BK7231N should be replacements");
            assertTrue(handler.isOfficialReplacement("BK7231N", "BK7231Q"),
                    "BK7231N and BK7231Q should be replacements");
            assertTrue(handler.isOfficialReplacement("BK3432", "BK3432N"),
                    "BK3432 and BK3432N should be replacements");
        }

        @Test
        @DisplayName("Same series with same package should be replacements")
        void sameSeriesSamePackage() {
            assertTrue(handler.isOfficialReplacement("BK7231-QFN32", "BK7231N-QFN32"),
                    "Same series and package should be replacements");
        }

        @Test
        @DisplayName("Same series with different pin count should NOT be replacements")
        void sameSeriesDifferentPinCount() {
            assertFalse(handler.isOfficialReplacement("BK7231-QFN32", "BK7231-QFN40"),
                    "Different pin count packages should NOT be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("BK7231", "BK7251"),
                    "BK7231 and BK7251 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("BK7231", "BK3431"),
                    "WiFi+BLE and BLE-only chips should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("BK3431", "BK3432"),
                    "BK3431 and BK3432 should NOT be replacements");
        }

        @Test
        @DisplayName("Non-Beken parts should not be replacements")
        void nonBekenNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ESP32", "BK7231"));
            assertFalse(handler.isOfficialReplacement("BK7231", "nRF52832"));
        }

        @Test
        @DisplayName("Null handling for replacements")
        void nullHandling() {
            assertFalse(handler.isOfficialReplacement(null, "BK7231"));
            assertFalse(handler.isOfficialReplacement("BK7231", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC type")
        void shouldSupportIcType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("Should use Set.of() (immutable)")
        void shouldUseSetOf() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.MEMORY);
            }, "getSupportedTypes() should return immutable set");
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
            assertFalse(handler.matches(null, ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.getWirelessType(null));
            assertFalse(handler.isWifiCapable(null));
            assertFalse(handler.isBleCapable(null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.getWirelessType(""));
            assertFalse(handler.isWifiCapable(""));
            assertFalse(handler.isBleCapable(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("BK7231", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("bk7231", ComponentType.IC, registry));
            assertTrue(handler.matches("BK7231", ComponentType.IC, registry));
            assertTrue(handler.matches("Bk7231", ComponentType.IC, registry));

            assertEquals("BK7231", handler.extractSeries("bk7231"));
            assertEquals("BK7231", handler.extractSeries("BK7231"));
        }

        @Test
        @DisplayName("Should not match non-Beken parts")
        void shouldNotMatchNonBekenParts() {
            assertFalse(handler.matches("ESP32", ComponentType.IC, registry));
            assertFalse(handler.matches("nRF52832", ComponentType.IC, registry));
            assertFalse(handler.matches("CC2541", ComponentType.IC, registry));
            assertFalse(handler.matches("RTL8720", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Should not match invalid BK patterns")
        void shouldNotMatchInvalidPatterns() {
            // BK1xxx and BK2xxx are not valid Beken patterns (need BK3xxx or BK7xxx)
            assertFalse(handler.matches("BK1234", ComponentType.IC, registry));
            assertFalse(handler.matches("BK2345", ComponentType.IC, registry));
            // BK5xxx and BK6xxx are not valid
            assertFalse(handler.matches("BK5678", ComponentType.IC, registry));
            assertFalse(handler.matches("BK6789", ComponentType.IC, registry));
        }
    }

    @Nested
    @DisplayName("Real-World Part Numbers")
    class RealWorldPartTests {

        @Test
        @DisplayName("Should handle real BK7231 variants")
        void shouldHandleRealBk7231() {
            // Common BK7231 variants found in IoT modules
            String[] realParts = {
                "BK7231N",
                "BK7231T",
                "BK7231U",
                "BK7231S"
            };

            for (String mpn : realParts) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match");
                assertEquals("BK7231", handler.extractSeries(mpn),
                        mpn + " should have series BK7231");
                assertTrue(handler.isWifiCapable(mpn),
                        mpn + " should be WiFi capable");
                assertTrue(handler.isBleCapable(mpn),
                        mpn + " should be BLE capable");
            }
        }

        @Test
        @DisplayName("Should handle Tuya-branded BK7231 parts")
        void shouldHandleTuyaParts() {
            // Tuya uses BK7231 in their modules
            // While the module names are different, the underlying chips follow BK7231 pattern
            assertTrue(handler.matches("BK7231N", ComponentType.IC, registry));
            assertEquals("WIFI_BLE", handler.getWirelessType("BK7231N"));
        }
    }

    @Nested
    @DisplayName("Manufacturer Types")
    class ManufacturerTypesTests {

        @Test
        @DisplayName("Should return empty manufacturer types")
        void shouldReturnEmptyManufacturerTypes() {
            var types = handler.getManufacturerTypes();
            assertTrue(types.isEmpty(),
                    "Manufacturer types should be empty");
        }
    }
}
