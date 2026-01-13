package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.RealtekHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for RealtekHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 *
 * Realtek Product Categories:
 * - ALC2xx/ALC6xx/ALC8xx: Audio Codecs (ALC269, ALC272, ALC662, ALC892, ALC898)
 * - ALC1xxx: High Definition Audio (ALC1150, ALC1200, ALC1220)
 * - ALC5xxx: Mobile Audio Codecs (ALC5640, ALC5682)
 * - RTL81xx: Fast Ethernet Controllers (RTL8101, RTL8102)
 * - RTL81xxE: PCIe Ethernet Controllers (RTL8111E, RTL8168E)
 * - RTL821x: Gigabit PHY (RTL8211, RTL8212)
 * - RTL88xx: WiFi Controllers (RTL8188, RTL8192, RTL8812, RTL8814)
 * - RTL8xxxU: USB WiFi (RTL8188EU, RTL8192EU)
 * - RTD2xxx: Display Controllers
 *
 * Package Codes:
 * - GR: QFP (Quad Flat Package)
 * - VB: QFN (Quad Flat No-lead)
 * - CG: QFN (Quad Flat No-lead)
 * - VL: QFN-VL variant
 */
class RealtekHandlerTest {

    private static RealtekHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new RealtekHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("ALC Audio Codec Detection")
    class ALCAudioCodecTests {

        @Nested
        @DisplayName("ALC2xx Entry-Level Audio Codecs")
        class ALC2xxTests {

            @ParameterizedTest
            @DisplayName("ALC2xx Audio Codecs should match IC type")
            @ValueSource(strings = {"ALC269", "ALC269-GR", "ALC269-VB", "ALC272", "ALC272-GR", "ALC282", "ALC283"})
            void alc2xxShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("ALC269 is a popular notebook audio codec")
            void alc269ShouldBeDetected() {
                assertTrue(handler.matches("ALC269-GR", ComponentType.IC, registry));
                assertEquals("ALC2", handler.extractSeries("ALC269-GR"));
                assertEquals("QFP", handler.extractPackageCode("ALC269-GR"));
            }
        }

        @Nested
        @DisplayName("ALC6xx Mid-Range Audio Codecs")
        class ALC6xxTests {

            @ParameterizedTest
            @DisplayName("ALC6xx Audio Codecs should match IC type")
            @ValueSource(strings = {"ALC662", "ALC662-GR", "ALC662-VB", "ALC663", "ALC668", "ALC670"})
            void alc6xxShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("ALC662 is a popular desktop audio codec")
            void alc662ShouldBeDetected() {
                assertTrue(handler.matches("ALC662-GR", ComponentType.IC, registry));
                assertEquals("ALC6", handler.extractSeries("ALC662-GR"));
                assertEquals("QFP", handler.extractPackageCode("ALC662-GR"));
            }
        }

        @Nested
        @DisplayName("ALC8xx High-End Audio Codecs")
        class ALC8xxTests {

            @ParameterizedTest
            @DisplayName("ALC8xx Audio Codecs should match IC type")
            @ValueSource(strings = {"ALC882", "ALC883", "ALC888", "ALC892", "ALC892-GR", "ALC898", "ALC898-GR"})
            void alc8xxShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("ALC892 is a popular high-end audio codec")
            void alc892ShouldBeDetected() {
                assertTrue(handler.matches("ALC892-GR", ComponentType.IC, registry));
                assertEquals("ALC8", handler.extractSeries("ALC892-GR"));
                assertEquals("QFP", handler.extractPackageCode("ALC892-GR"));
            }

            @Test
            @DisplayName("ALC898 is an enhanced version of ALC892")
            void alc898ShouldBeDetected() {
                assertTrue(handler.matches("ALC898-GR", ComponentType.IC, registry));
                assertEquals("ALC8", handler.extractSeries("ALC898-GR"));
            }
        }

        @Nested
        @DisplayName("ALC1xxx High Definition Audio Codecs")
        class ALC1xxxTests {

            @ParameterizedTest
            @DisplayName("ALC1xxx HD Audio Codecs should match IC type")
            @ValueSource(strings = {"ALC1150", "ALC1150-VB", "ALC1200", "ALC1200-VB", "ALC1220", "ALC1220-VB"})
            void alc1xxxShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("ALC1220 is a flagship HD audio codec")
            void alc1220ShouldBeDetected() {
                assertTrue(handler.matches("ALC1220-VB", ComponentType.IC, registry));
                assertEquals("ALC1", handler.extractSeries("ALC1220-VB"));
                assertEquals("QFN", handler.extractPackageCode("ALC1220-VB"));
            }

            @Test
            @DisplayName("ALC1150 is an earlier HD audio codec")
            void alc1150ShouldBeDetected() {
                assertTrue(handler.matches("ALC1150-VB", ComponentType.IC, registry));
                assertEquals("ALC1", handler.extractSeries("ALC1150-VB"));
            }
        }

        @Nested
        @DisplayName("ALC5xxx Mobile Audio Codecs")
        class ALC5xxxTests {

            @ParameterizedTest
            @DisplayName("ALC5xxx Mobile Audio Codecs should match IC type")
            @ValueSource(strings = {"ALC5621", "ALC5631", "ALC5640", "ALC5640-VB", "ALC5682", "ALC5682-VB"})
            void alc5xxxShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("ALC5640 is a popular mobile audio codec")
            void alc5640ShouldBeDetected() {
                assertTrue(handler.matches("ALC5640-VB", ComponentType.IC, registry));
                assertEquals("ALC5", handler.extractSeries("ALC5640-VB"));
                assertEquals("QFN", handler.extractPackageCode("ALC5640-VB"));
            }

            @Test
            @DisplayName("ALC5682 is a newer mobile audio codec")
            void alc5682ShouldBeDetected() {
                assertTrue(handler.matches("ALC5682-VB", ComponentType.IC, registry));
                assertEquals("ALC5", handler.extractSeries("ALC5682-VB"));
            }
        }
    }

    @Nested
    @DisplayName("RTL81xx Ethernet Controller Detection")
    class RTL81xxEthernetTests {

        @Nested
        @DisplayName("RTL810x Fast Ethernet Controllers")
        class RTL810xTests {

            @ParameterizedTest
            @DisplayName("RTL810x Fast Ethernet should match IC type")
            @ValueSource(strings = {"RTL8100", "RTL8101", "RTL8101E", "RTL8102", "RTL8102E", "RTL8102E-CG"})
            void rtl810xShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("RTL8101E is a common Fast Ethernet controller")
            void rtl8101EShouldBeDetected() {
                assertTrue(handler.matches("RTL8101E-CG", ComponentType.IC, registry));
                assertEquals("RTL81", handler.extractSeries("RTL8101E-CG"));
            }
        }

        @Nested
        @DisplayName("RTL8111/RTL8168 Gigabit Ethernet Controllers")
        class RTL8111Tests {

            @ParameterizedTest
            @DisplayName("RTL8111 Gigabit Ethernet should match IC type")
            @ValueSource(strings = {
                    "RTL8111", "RTL8111B", "RTL8111C", "RTL8111D", "RTL8111E",
                    "RTL8111F", "RTL8111G", "RTL8111H", "RTL8111H-CG"
            })
            void rtl8111ShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @ParameterizedTest
            @DisplayName("RTL8168 Gigabit Ethernet should match IC type")
            @ValueSource(strings = {"RTL8168", "RTL8168B", "RTL8168E", "RTL8168E-CG"})
            void rtl8168ShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("RTL8111H is the latest generation")
            void rtl8111HShouldBeDetected() {
                assertTrue(handler.matches("RTL8111H-CG", ComponentType.IC, registry));
                assertEquals("RTL81", handler.extractSeries("RTL8111H-CG"));
                assertEquals("QFN", handler.extractPackageCode("RTL8111H-CG"));
            }
        }
    }

    @Nested
    @DisplayName("RTL821x Gigabit PHY Detection")
    class RTL821xGigabitPHYTests {

        @ParameterizedTest
        @DisplayName("RTL821x Gigabit PHY should match IC type")
        @ValueSource(strings = {
                "RTL8211", "RTL8211B", "RTL8211CL", "RTL8211DN", "RTL8211E",
                "RTL8211E-VL", "RTL8211E-VL-CG", "RTL8211F", "RTL8211F-CG",
                "RTL8212", "RTL8212B"
        })
        void rtl821xShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("RTL8211E is a popular Gigabit Ethernet PHY")
        void rtl8211EShouldBeDetected() {
            assertTrue(handler.matches("RTL8211E-VL-CG", ComponentType.IC, registry));
            assertEquals("RTL82", handler.extractSeries("RTL8211E-VL-CG"));
            assertEquals("QFN", handler.extractPackageCode("RTL8211E-VL-CG"));
        }

        @Test
        @DisplayName("RTL8211F is the newer generation PHY")
        void rtl8211FShouldBeDetected() {
            assertTrue(handler.matches("RTL8211F-CG", ComponentType.IC, registry));
            assertEquals("RTL82", handler.extractSeries("RTL8211F-CG"));
        }
    }

    @Nested
    @DisplayName("RTL88xx WiFi Controller Detection")
    class RTL88xxWiFiTests {

        @Nested
        @DisplayName("RTL8188 WiFi (802.11n)")
        class RTL8188Tests {

            @ParameterizedTest
            @DisplayName("RTL8188 WiFi should match IC type")
            @ValueSource(strings = {"RTL8188", "RTL8188CE", "RTL8188CUS", "RTL8188EU", "RTL8188EE"})
            void rtl8188ShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("RTL8188EU is a popular USB WiFi chip")
            void rtl8188EUShouldBeDetected() {
                assertTrue(handler.matches("RTL8188EU", ComponentType.IC, registry));
                assertEquals("RTL88", handler.extractSeries("RTL8188EU"));
            }
        }

        @Nested
        @DisplayName("RTL8192 WiFi (802.11n Dual-Stream)")
        class RTL8192Tests {

            @ParameterizedTest
            @DisplayName("RTL8192 WiFi should match IC type")
            @ValueSource(strings = {"RTL8192", "RTL8192CE", "RTL8192CU", "RTL8192EU", "RTL8192EE"})
            void rtl8192ShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("RTL8192EU is a popular dual-stream WiFi chip")
            void rtl8192EUShouldBeDetected() {
                assertTrue(handler.matches("RTL8192EU", ComponentType.IC, registry));
                assertEquals("RTL88", handler.extractSeries("RTL8192EU"));
            }
        }

        @Nested
        @DisplayName("RTL8812/RTL8814 WiFi (802.11ac)")
        class RTL8812Tests {

            @ParameterizedTest
            @DisplayName("RTL8812/8814 WiFi AC should match IC type")
            @ValueSource(strings = {"RTL8812", "RTL8812AU", "RTL8812AE", "RTL8814", "RTL8814AU"})
            void rtl8812ShouldMatchIC(String mpn) {
                assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                        mpn + " should match IC type");
            }

            @Test
            @DisplayName("RTL8812AU is a popular 802.11ac WiFi chip")
            void rtl8812AUShouldBeDetected() {
                assertTrue(handler.matches("RTL8812AU", ComponentType.IC, registry));
                assertEquals("RTL88", handler.extractSeries("RTL8812AU"));
            }
        }
    }

    @Nested
    @DisplayName("RTD2xxx Display Controller Detection")
    class RTD2xxxDisplayTests {

        @ParameterizedTest
        @DisplayName("RTD2xxx Display Controllers should match IC type")
        @ValueSource(strings = {"RTD2120", "RTD2132", "RTD2281", "RTD2660", "RTD2662"})
        void rtd2xxxShouldMatchIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC type");
        }

        @Test
        @DisplayName("RTD2660 is a popular LCD controller")
        void rtd2660ShouldBeDetected() {
            assertTrue(handler.matches("RTD2660", ComponentType.IC, registry));
            assertEquals("RTD2", handler.extractSeries("RTD2660"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract QFP package codes")
        @CsvSource({
                "ALC892-GR, QFP",
                "ALC662-GR, QFP",
                "ALC269-GR, QFP"
        })
        void shouldExtractQFPPackageCodes(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract QFN package codes")
        @CsvSource({
                "ALC1220-VB, QFN",
                "RTL8111H-CG, QFN",
                "RTL8211E-VL-CG, QFN",
                "ALC5640-VB, QFN"
        })
        void shouldExtractQFNPackageCodes(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should handle multi-segment package codes")
        void shouldHandleMultiSegmentPackageCodes() {
            // RTL8211E-VL-CG has VL (variant) and CG (package)
            // Should extract the last segment as package
            assertEquals("QFN", handler.extractPackageCode("RTL8211E-VL-CG"));
        }

        @Test
        @DisplayName("Should handle parts without package suffix")
        void shouldHandlePartsWithoutPackageSuffix() {
            String result = handler.extractPackageCode("ALC892");
            assertNotNull(result);
        }

        @Test
        @DisplayName("Should return empty for null/empty input")
        void shouldReturnEmptyForNullOrEmptyInput() {
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractPackageCode(""));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract ALC series correctly")
        @CsvSource({
                "ALC269-GR, ALC2",
                "ALC662-GR, ALC6",
                "ALC892-GR, ALC8",
                "ALC1220-VB, ALC1",
                "ALC5640-VB, ALC5"
        })
        void shouldExtractALCSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract RTL series correctly")
        @CsvSource({
                "RTL8101E-CG, RTL81",
                "RTL8111H-CG, RTL81",
                "RTL8168E-CG, RTL81",
                "RTL8211E-VL-CG, RTL82",
                "RTL8211F-CG, RTL82",
                "RTL8188EU, RTL88",
                "RTL8192EU, RTL88",
                "RTL8812AU, RTL88"
        })
        void shouldExtractRTLSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract RTD series correctly")
        @CsvSource({
                "RTD2660, RTD2",
                "RTD2281, RTD2",
                "RTD2132, RTD2"
        })
        void shouldExtractRTDSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @Test
        @DisplayName("Should return empty for invalid input")
        void shouldReturnEmptyForInvalidInput() {
            assertEquals("", handler.extractSeries(null));
            assertEquals("", handler.extractSeries(""));
            assertEquals("", handler.extractSeries("INVALID123"));
        }
    }

    @Nested
    @DisplayName("Replacement Detection")
    class ReplacementTests {

        @Nested
        @DisplayName("Same Part Different Package Replacements")
        class SamePartDifferentPackageTests {

            @Test
            @DisplayName("ALC892 with different packages should be replacements")
            void alc892DifferentPackagesShouldBeReplacements() {
                assertTrue(handler.isOfficialReplacement("ALC892-GR", "ALC892-VB"),
                        "Same part with QFP and QFN packages should be replacements");
            }

            @Test
            @DisplayName("RTL8111H with different packages should be replacements")
            void rtl8111DifferentPackagesShouldBeReplacements() {
                assertTrue(handler.isOfficialReplacement("RTL8111H-CG", "RTL8111H-VB"),
                        "Same part with different QFN variants should be replacements");
            }

            @Test
            @DisplayName("ALC1220 variants should be replacements")
            void alc1220VariantsShouldBeReplacements() {
                assertTrue(handler.isOfficialReplacement("ALC1220-VB", "ALC1220-VD"),
                        "Same part with different QFN variants should be replacements");
            }
        }

        @Nested
        @DisplayName("Compatible Series Replacements")
        class CompatibleSeriesTests {

            @Test
            @DisplayName("ALC892 and ALC898 should be compatible")
            void alc892And898ShouldBeCompatible() {
                assertTrue(handler.isOfficialReplacement("ALC892-GR", "ALC898-GR"),
                        "ALC892 and ALC898 should be compatible");
                assertTrue(handler.isOfficialReplacement("ALC898-GR", "ALC892-GR"),
                        "Compatibility should be bidirectional");
            }

            @Test
            @DisplayName("ALC1200 and ALC1220 should be compatible")
            void alc1200And1220ShouldBeCompatible() {
                assertTrue(handler.isOfficialReplacement("ALC1200-VB", "ALC1220-VB"),
                        "ALC1200 and ALC1220 should be compatible");
            }

            @Test
            @DisplayName("RTL8111 variants should be compatible")
            void rtl8111VariantsShouldBeCompatible() {
                assertTrue(handler.isOfficialReplacement("RTL8111E-CG", "RTL8111H-CG"),
                        "RTL8111E and RTL8111H should be compatible");
                assertTrue(handler.isOfficialReplacement("RTL8111F-CG", "RTL8111G-CG"),
                        "RTL8111F and RTL8111G should be compatible");
            }

            @Test
            @DisplayName("RTL8211 variants should be compatible")
            void rtl8211VariantsShouldBeCompatible() {
                assertTrue(handler.isOfficialReplacement("RTL8211E-VL-CG", "RTL8211F-CG"),
                        "RTL8211E and RTL8211F should be compatible");
            }

            @Test
            @DisplayName("RTL8188 variants should be compatible")
            void rtl8188VariantsShouldBeCompatible() {
                assertTrue(handler.isOfficialReplacement("RTL8188CE", "RTL8188EU"),
                        "RTL8188CE and RTL8188EU should be compatible");
            }
        }

        @Nested
        @DisplayName("Incompatible Parts")
        class IncompatiblePartsTests {

            @Test
            @DisplayName("Different ALC series should NOT be replacements")
            void differentALCSeriesShouldNotBeReplacements() {
                assertFalse(handler.isOfficialReplacement("ALC892-GR", "ALC662-GR"),
                        "ALC8xx and ALC6xx should not be replacements");
                assertFalse(handler.isOfficialReplacement("ALC269-GR", "ALC1220-VB"),
                        "ALC2xx and ALC1xxx should not be replacements");
            }

            @Test
            @DisplayName("Audio and Network ICs should NOT be replacements")
            void audioAndNetworkShouldNotBeReplacements() {
                assertFalse(handler.isOfficialReplacement("ALC892-GR", "RTL8111H-CG"),
                        "Audio codec and Ethernet IC should not be replacements");
            }

            @Test
            @DisplayName("Ethernet and WiFi should NOT be replacements")
            void ethernetAndWiFiShouldNotBeReplacements() {
                assertFalse(handler.isOfficialReplacement("RTL8111H-CG", "RTL8812AU"),
                        "Ethernet and WiFi ICs should not be replacements");
            }

            @Test
            @DisplayName("Different RTL series should NOT be replacements")
            void differentRTLSeriesShouldNotBeReplacements() {
                assertFalse(handler.isOfficialReplacement("RTL8111H-CG", "RTL8211E-CG"),
                        "RTL81xx and RTL82xx should not be replacements");
            }
        }

        @Test
        @DisplayName("Null inputs should return false")
        void nullInputsShouldReturnFalse() {
            assertFalse(handler.isOfficialReplacement(null, "ALC892-GR"));
            assertFalse(handler.isOfficialReplacement("ALC892-GR", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support IC type")
        void shouldSupportICType() {
            var types = handler.getSupportedTypes();
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC type");
        }

        @Test
        @DisplayName("Should use Set.of() not HashSet")
        void shouldUseSetOf() {
            var types = handler.getSupportedTypes();
            // Set.of() returns an ImmutableCollections class
            assertThrows(UnsupportedOperationException.class, () ->
                            types.add(ComponentType.SENSOR),
                    "getSupportedTypes() should return immutable Set from Set.of()");
        }

        @Test
        @DisplayName("Should only contain IC type")
        void shouldOnlyContainICType() {
            var types = handler.getSupportedTypes();
            assertEquals(1, types.size(), "Should only contain one type");
            assertTrue(types.contains(ComponentType.IC), "Should contain IC type");
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
            assertFalse(handler.isOfficialReplacement(null, "ALC892-GR"));
            assertFalse(handler.isOfficialReplacement("ALC892-GR", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.IC, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("ALC892-GR", null, registry));
        }

        @Test
        @DisplayName("Case insensitive matching")
        void caseInsensitiveMatching() {
            assertTrue(handler.matches("alc892-gr", ComponentType.IC, registry));
            assertTrue(handler.matches("ALC892-GR", ComponentType.IC, registry));
            assertTrue(handler.matches("rtl8111h-cg", ComponentType.IC, registry));
            assertTrue(handler.matches("RTL8111H-CG", ComponentType.IC, registry));
        }

        @Test
        @DisplayName("Case insensitive via registry")
        void caseInsensitiveViaRegistry() {
            assertTrue(registry.matches("alc892", ComponentType.IC));
            assertTrue(registry.matches("ALC892", ComponentType.IC));
            assertTrue(registry.matches("rtl8111", ComponentType.IC));
            assertTrue(registry.matches("RTL8111", ComponentType.IC));
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {

        @Test
        @DisplayName("Handler can be instantiated directly")
        void canInstantiateDirectly() {
            RealtekHandler directHandler = new RealtekHandler();
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

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {

        @Test
        @DisplayName("isAudioCodec should identify ALC parts")
        void isAudioCodecShouldIdentifyALCParts() {
            assertTrue(handler.isAudioCodec("ALC892-GR"));
            assertTrue(handler.isAudioCodec("ALC1220-VB"));
            assertTrue(handler.isAudioCodec("ALC5640-VB"));
            assertFalse(handler.isAudioCodec("RTL8111H-CG"));
            assertFalse(handler.isAudioCodec(null));
        }

        @Test
        @DisplayName("isNetworkController should identify RTL81/82 parts")
        void isNetworkControllerShouldIdentifyRTL8xParts() {
            assertTrue(handler.isNetworkController("RTL8111H-CG"));
            assertTrue(handler.isNetworkController("RTL8211E-VL-CG"));
            assertFalse(handler.isNetworkController("RTL8812AU"));
            assertFalse(handler.isNetworkController("ALC892-GR"));
            assertFalse(handler.isNetworkController(null));
        }

        @Test
        @DisplayName("isWiFiController should identify RTL88 parts")
        void isWiFiControllerShouldIdentifyRTL88Parts() {
            assertTrue(handler.isWiFiController("RTL8812AU"));
            assertTrue(handler.isWiFiController("RTL8188EU"));
            assertFalse(handler.isWiFiController("RTL8111H-CG"));
            assertFalse(handler.isWiFiController("ALC892-GR"));
            assertFalse(handler.isWiFiController(null));
        }

        @Test
        @DisplayName("isDisplayController should identify RTD parts")
        void isDisplayControllerShouldIdentifyRTDParts() {
            assertTrue(handler.isDisplayController("RTD2660"));
            assertTrue(handler.isDisplayController("RTD2281"));
            assertFalse(handler.isDisplayController("RTL8111H-CG"));
            assertFalse(handler.isDisplayController("ALC892-GR"));
            assertFalse(handler.isDisplayController(null));
        }

        @Test
        @DisplayName("getAudioCodecGeneration should return correct generation")
        void getAudioCodecGenerationShouldReturnCorrectGeneration() {
            assertEquals("HD", handler.getAudioCodecGeneration("ALC1220-VB"));
            assertEquals("Mobile", handler.getAudioCodecGeneration("ALC5640-VB"));
            assertEquals("Standard", handler.getAudioCodecGeneration("ALC892-GR"));
            assertEquals("Standard", handler.getAudioCodecGeneration("ALC662-GR"));
            assertEquals("Standard", handler.getAudioCodecGeneration("ALC269-GR"));
            assertEquals("", handler.getAudioCodecGeneration("RTL8111H-CG"));
            assertEquals("", handler.getAudioCodecGeneration(null));
        }

        @Test
        @DisplayName("getNetworkInterfaceType should return correct interface")
        void getNetworkInterfaceTypeShouldReturnCorrectInterface() {
            assertEquals("Fast Ethernet", handler.getNetworkInterfaceType("RTL8101E-CG"));
            assertEquals("Gigabit Ethernet", handler.getNetworkInterfaceType("RTL8111H-CG"));
            assertEquals("Gigabit PHY", handler.getNetworkInterfaceType("RTL8211E-VL-CG"));
            assertEquals("WiFi", handler.getNetworkInterfaceType("RTL8812AU"));
            assertEquals("", handler.getNetworkInterfaceType("ALC892-GR"));
            assertEquals("", handler.getNetworkInterfaceType(null));
        }
    }

    @Nested
    @DisplayName("Real-World MPN Examples")
    class RealWorldExamples {

        @ParameterizedTest
        @DisplayName("Common Realtek Audio Codecs")
        @ValueSource(strings = {
                "ALC269-GR",      // Popular notebook audio codec
                "ALC662-GR",      // Popular desktop audio codec
                "ALC892-GR",      // High-end motherboard codec
                "ALC898-GR",      // Enhanced version of ALC892
                "ALC1150-VB",     // HD Audio codec
                "ALC1220-VB",     // Flagship HD Audio codec
                "ALC5640-VB"      // Mobile audio codec
        })
        void commonRealtekAudioCodecs(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            assertTrue(handler.isAudioCodec(mpn), mpn + " should be an audio codec");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
        }

        @ParameterizedTest
        @DisplayName("Common Realtek Ethernet Controllers")
        @ValueSource(strings = {
                "RTL8101E-CG",    // Fast Ethernet PCIe
                "RTL8111H-CG",    // Gigabit Ethernet PCIe
                "RTL8168E-CG",    // Gigabit Ethernet PCIe (alternative)
                "RTL8211E-VL-CG", // Gigabit PHY
                "RTL8211F-CG"     // Newer Gigabit PHY
        })
        void commonRealtekEthernetControllers(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            assertTrue(handler.isNetworkController(mpn), mpn + " should be a network controller");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
        }

        @ParameterizedTest
        @DisplayName("Common Realtek WiFi Chips")
        @ValueSource(strings = {
                "RTL8188EU",      // USB WiFi 802.11n
                "RTL8188CE",      // PCIe WiFi 802.11n
                "RTL8192EU",      // USB WiFi 802.11n dual-stream
                "RTL8812AU",      // USB WiFi 802.11ac
                "RTL8814AU"       // USB WiFi 802.11ac 4x4
        })
        void commonRealtekWiFiChips(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should be recognized as IC");
            assertTrue(handler.isWiFiController(mpn), mpn + " should be a WiFi controller");
            String series = handler.extractSeries(mpn);
            assertFalse(series.isEmpty(), mpn + " should have extractable series");
        }

        @Test
        @DisplayName("Document popular motherboard audio codecs")
        void documentPopularMotherboardAudioCodecs() {
            // ALC892 - Very common on mid-range motherboards
            assertTrue(handler.matches("ALC892-GR", ComponentType.IC, registry));
            assertEquals("ALC8", handler.extractSeries("ALC892-GR"));
            assertEquals("QFP", handler.extractPackageCode("ALC892-GR"));
            assertEquals("Standard", handler.getAudioCodecGeneration("ALC892-GR"));

            // ALC1220 - High-end motherboard audio codec
            assertTrue(handler.matches("ALC1220-VB", ComponentType.IC, registry));
            assertEquals("ALC1", handler.extractSeries("ALC1220-VB"));
            assertEquals("QFN", handler.extractPackageCode("ALC1220-VB"));
            assertEquals("HD", handler.getAudioCodecGeneration("ALC1220-VB"));
        }

        @Test
        @DisplayName("Document popular Ethernet controllers")
        void documentPopularEthernetControllers() {
            // RTL8111H - Latest generation Gigabit Ethernet
            assertTrue(handler.matches("RTL8111H-CG", ComponentType.IC, registry));
            assertEquals("RTL81", handler.extractSeries("RTL8111H-CG"));
            assertEquals("QFN", handler.extractPackageCode("RTL8111H-CG"));
            assertEquals("Gigabit Ethernet", handler.getNetworkInterfaceType("RTL8111H-CG"));

            // RTL8211E - Popular Gigabit PHY
            assertTrue(handler.matches("RTL8211E-VL-CG", ComponentType.IC, registry));
            assertEquals("RTL82", handler.extractSeries("RTL8211E-VL-CG"));
            assertEquals("QFN", handler.extractPackageCode("RTL8211E-VL-CG"));
            assertEquals("Gigabit PHY", handler.getNetworkInterfaceType("RTL8211E-VL-CG"));
        }
    }

    @Nested
    @DisplayName("Pattern Coverage")
    class PatternCoverageTests {

        @Test
        @DisplayName("All ALC series patterns should be registered")
        void allALCSeriesPatternsRegistered() {
            // ALC2xx - Entry-level
            assertTrue(registry.matches("ALC269", ComponentType.IC));
            assertTrue(registry.matches("ALC272", ComponentType.IC));
            assertTrue(registry.matches("ALC282", ComponentType.IC));

            // ALC6xx - Mid-range
            assertTrue(registry.matches("ALC662", ComponentType.IC));
            assertTrue(registry.matches("ALC668", ComponentType.IC));

            // ALC8xx - High-end
            assertTrue(registry.matches("ALC882", ComponentType.IC));
            assertTrue(registry.matches("ALC892", ComponentType.IC));
            assertTrue(registry.matches("ALC898", ComponentType.IC));

            // ALC1xxx - HD Audio
            assertTrue(registry.matches("ALC1150", ComponentType.IC));
            assertTrue(registry.matches("ALC1200", ComponentType.IC));
            assertTrue(registry.matches("ALC1220", ComponentType.IC));

            // ALC5xxx - Mobile
            assertTrue(registry.matches("ALC5621", ComponentType.IC));
            assertTrue(registry.matches("ALC5640", ComponentType.IC));
            assertTrue(registry.matches("ALC5682", ComponentType.IC));
        }

        @Test
        @DisplayName("All RTL series patterns should be registered")
        void allRTLSeriesPatternsRegistered() {
            // RTL810x - Fast Ethernet
            assertTrue(registry.matches("RTL8100", ComponentType.IC));
            assertTrue(registry.matches("RTL8101", ComponentType.IC));
            assertTrue(registry.matches("RTL8102", ComponentType.IC));

            // RTL811x - Gigabit Ethernet
            assertTrue(registry.matches("RTL8111", ComponentType.IC));
            assertTrue(registry.matches("RTL8111H", ComponentType.IC));

            // RTL816x - Gigabit Ethernet (alternative)
            assertTrue(registry.matches("RTL8168", ComponentType.IC));
            assertTrue(registry.matches("RTL8168E", ComponentType.IC));

            // RTL821x - Gigabit PHY
            assertTrue(registry.matches("RTL8211", ComponentType.IC));
            assertTrue(registry.matches("RTL8211E", ComponentType.IC));
            assertTrue(registry.matches("RTL8211F", ComponentType.IC));
            assertTrue(registry.matches("RTL8212", ComponentType.IC));

            // RTL88xx - WiFi
            assertTrue(registry.matches("RTL8188", ComponentType.IC));
            assertTrue(registry.matches("RTL8188EU", ComponentType.IC));
            assertTrue(registry.matches("RTL8192", ComponentType.IC));
            assertTrue(registry.matches("RTL8812", ComponentType.IC));
            assertTrue(registry.matches("RTL8814", ComponentType.IC));
        }

        @Test
        @DisplayName("RTD series patterns should be registered")
        void rtdSeriesPatternsRegistered() {
            assertTrue(registry.matches("RTD2120", ComponentType.IC));
            assertTrue(registry.matches("RTD2281", ComponentType.IC));
            assertTrue(registry.matches("RTD2660", ComponentType.IC));
        }

        @Test
        @DisplayName("Should not match non-Realtek parts")
        void shouldNotMatchNonRealtekParts() {
            assertFalse(handler.matches("CS4344-CZZ", ComponentType.IC, registry));
            assertFalse(handler.matches("WM8731SEDS", ComponentType.IC, registry));
            assertFalse(handler.matches("BCM4375-KF", ComponentType.IC, registry));
            assertFalse(handler.matches("LM358N", ComponentType.IC, registry));
        }
    }
}
