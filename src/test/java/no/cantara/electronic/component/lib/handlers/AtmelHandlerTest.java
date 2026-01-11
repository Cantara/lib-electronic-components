package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.MPNUtils;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.AtmelHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AtmelHandler.
 * Tests pattern matching, package code extraction, series extraction, and replacement detection.
 */
class AtmelHandlerTest {

    private static AtmelHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // Get handler through MPNUtils to ensure proper initialization
        // (avoids circular initialization issues)
        ManufacturerHandler h = MPNUtils.getManufacturerHandler("ATMEGA328P");
        assertNotNull(h, "Should find Atmel handler for ATMEGA328P");
        assertTrue(h instanceof AtmelHandler, "Handler should be AtmelHandler");
        handler = (AtmelHandler) h;

        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("ATmega Series Detection")
    class ATmegaTests {

        @ParameterizedTest
        @DisplayName("Should detect ATmega328 variants")
        @ValueSource(strings = {"ATMEGA328", "ATMEGA328P", "ATMEGA328P-PU", "ATMEGA328P-AU", "ATMEGA328PB"})
        void shouldDetectATmega328Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect ATmega2560 variants (Arduino Mega)")
        @ValueSource(strings = {"ATMEGA2560", "ATMEGA2560-16AU", "ATMEGA2560-16CU"})
        void shouldDetectATmega2560Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect other ATmega variants")
        @ValueSource(strings = {"ATMEGA8", "ATMEGA88", "ATMEGA168", "ATMEGA32U4", "ATMEGA1284P"})
        void shouldDetectOtherATmegaVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }

        @Test
        @DisplayName("ATmega should also match base MICROCONTROLLER type")
        void shouldMatchBaseMicrocontrollerType() {
            assertTrue(handler.matches("ATMEGA328P", ComponentType.MICROCONTROLLER, registry),
                    "ATMEGA328P should match MICROCONTROLLER (base type)");
        }
    }

    @Nested
    @DisplayName("ATtiny Series Detection")
    class ATtinyTests {

        @ParameterizedTest
        @DisplayName("Should detect ATtiny85 variants (popular small MCU)")
        @ValueSource(strings = {"ATTINY85", "ATTINY85-20PU", "ATTINY85-20SU", "ATTINY85V-10PU"})
        void shouldDetectATtiny85Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect other ATtiny variants")
        @ValueSource(strings = {"ATTINY13", "ATTINY13A", "ATTINY25", "ATTINY45", "ATTINY84", "ATTINY2313"})
        void shouldDetectOtherATtinyVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }
    }

    @Nested
    @DisplayName("AT90 Series Detection")
    class AT90Tests {

        @ParameterizedTest
        @DisplayName("Should detect AT90USB variants")
        @ValueSource(strings = {"AT90USB162", "AT90USB1286", "AT90USB1287"})
        void shouldDetectAT90USBVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect AT90CAN variants")
        @ValueSource(strings = {"AT90CAN128", "AT90CAN32", "AT90CAN64"})
        void shouldDetectAT90CANVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }
    }

    @Nested
    @DisplayName("SAM Series Detection (ARM-based)")
    class SAMTests {

        @ParameterizedTest
        @DisplayName("Should detect ATSAM3 variants (Arduino Due)")
        @ValueSource(strings = {"ATSAM3X8E", "ATSAM3X8E-AU"})
        void shouldDetectATSAM3Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect ATSAMD variants (Arduino Zero)")
        @ValueSource(strings = {"ATSAMD21G18A", "ATSAMD21G18A-AU", "ATSAMD21E18A"})
        void shouldDetectATSAMDVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MICROCONTROLLER_ATMEL, registry),
                    mpn + " should match MICROCONTROLLER_ATMEL");
        }
    }

    @Nested
    @DisplayName("Memory Products Detection")
    class MemoryTests {

        @ParameterizedTest
        @DisplayName("Should detect AT24C I2C EEPROM variants")
        @ValueSource(strings = {"AT24C256", "AT24C256-PU", "AT24C32", "AT24C512"})
        void shouldDetectAT24CEEPROMVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_ATMEL, registry),
                    mpn + " should match MEMORY_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect AT25 SPI EEPROM/Flash variants")
        @ValueSource(strings = {"AT25SF041", "AT25DF321", "AT25080"})
        void shouldDetectAT25Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.MEMORY_ATMEL, registry),
                    mpn + " should match MEMORY_ATMEL");
        }

        @Test
        @DisplayName("Memory should also match base MEMORY type")
        void shouldMatchBaseMemoryType() {
            assertTrue(handler.matches("AT24C256", ComponentType.MEMORY, registry),
                    "AT24C256 should match MEMORY (base type)");
        }
    }

    @Nested
    @DisplayName("Touch Controller Detection")
    class TouchTests {

        @ParameterizedTest
        @DisplayName("Should detect AT42QT QTouch variants")
        @ValueSource(strings = {"AT42QT1010", "AT42QT1011", "AT42QT2120"})
        void shouldDetectQTouchVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TOUCH_ATMEL, registry),
                    mpn + " should match TOUCH_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect ATMXT maXTouch variants")
        @ValueSource(strings = {"ATMXT336S", "ATMXT224"})
        void shouldDetectMaxTouchVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.TOUCH_ATMEL, registry),
                    mpn + " should match TOUCH_ATMEL");
        }
    }

    @Nested
    @DisplayName("Crypto Products Detection")
    class CryptoTests {

        @ParameterizedTest
        @DisplayName("Should detect ATECC CryptoAuthentication variants")
        @ValueSource(strings = {"ATECC608A", "ATECC508A", "ATECC108A"})
        void shouldDetectATECCVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYPTO_ATMEL, registry),
                    mpn + " should match CRYPTO_ATMEL");
        }

        @ParameterizedTest
        @DisplayName("Should detect ATSHA SHA Authentication variants")
        @ValueSource(strings = {"ATSHA204A", "ATSHA206A"})
        void shouldDetectATSHAVariants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CRYPTO_ATMEL, registry),
                    mpn + " should match CRYPTO_ATMEL");
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {

        @ParameterizedTest
        @DisplayName("Should extract package codes from ATmega MPNs")
        @CsvSource({
                "ATMEGA328P-PU, PDIP",
                "ATMEGA328P-AU, TQFP",
                "ATMEGA328P-MU, QFN/MLF",
                "ATMEGA32U4-AU, TQFP"
        })
        void shouldExtractATmegaPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes from ATtiny MPNs")
        @CsvSource({
                "ATTINY85-20PU, PDIP",
                "ATTINY85-20SU, SOIC"
        })
        void shouldExtractATtinyPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract package codes from memory MPNs")
        @CsvSource({
                "AT24C256-PU, PDIP",
                "AT24C256-SU, SOIC"
        })
        void shouldExtractMemoryPackageCodes(String mpn, String expectedPackage) {
            assertEquals(expectedPackage, handler.extractPackageCode(mpn),
                    "Package code for " + mpn);
        }

        @Test
        @DisplayName("Should return empty string for MPN without package code")
        void shouldReturnEmptyForNoPackageCode() {
            assertEquals("", handler.extractPackageCode("ATMEGA328P"),
                    "Should return empty for MPN without hyphen");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {

        @ParameterizedTest
        @DisplayName("Should extract ATmega series")
        @CsvSource({
                "ATMEGA328P, ATMEGA328",
                "ATMEGA328P-PU, ATMEGA328",
                "ATMEGA2560-16AU, ATMEGA2560",
                "ATMEGA32U4-AU, ATMEGA32"
        })
        void shouldExtractATmegaSeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract ATtiny series")
        @CsvSource({
                "ATTINY85, ATTINY85",
                "ATTINY85-20PU, ATTINY85",
                "ATTINY13A, ATTINY13",
                "ATTINY2313, ATTINY2313"
        })
        void shouldExtractATtinySeries(String mpn, String expectedSeries) {
            assertEquals(expectedSeries, handler.extractSeries(mpn),
                    "Series for " + mpn);
        }

        @ParameterizedTest
        @DisplayName("Should extract SAM series")
        @CsvSource({
                "ATSAM3X8E, ATSAM3X",
                "ATSAM3X8E-AU, ATSAM3X"
        })
        void shouldExtractSAMSeries(String mpn, String expectedSeries) {
            String series = handler.extractSeries(mpn);
            assertTrue(series.startsWith(expectedSeries),
                    "Series for " + mpn + " should start with " + expectedSeries + " but was " + series);
        }
    }

    @Nested
    @DisplayName("Official Replacement Detection")
    class ReplacementTests {

        @Test
        @DisplayName("Same series different packages should be replacements")
        void sameSeriesDifferentPackages() {
            assertTrue(handler.isOfficialReplacement("ATMEGA328P-PU", "ATMEGA328P-AU"),
                    "ATMEGA328P-PU and ATMEGA328P-AU should be replacements");
            assertTrue(handler.isOfficialReplacement("ATTINY85-20PU", "ATTINY85-20SU"),
                    "ATTINY85-20PU and ATTINY85-20SU should be replacements");
        }

        @Test
        @DisplayName("Different series should NOT be replacements")
        void differentSeriesNotReplacements() {
            assertFalse(handler.isOfficialReplacement("ATMEGA328P", "ATMEGA2560"),
                    "ATMEGA328P and ATMEGA2560 should NOT be replacements");
            assertFalse(handler.isOfficialReplacement("ATTINY85", "ATTINY13"),
                    "ATTINY85 and ATTINY13 should NOT be replacements");
        }

        @Test
        @DisplayName("Same MPN is a replacement for itself")
        void sameMpnIsReplacement() {
            assertTrue(handler.isOfficialReplacement("ATMEGA328P-PU", "ATMEGA328P-PU"),
                    "Same MPN should be replacement for itself");
        }
    }

    @Nested
    @DisplayName("Pin-Compatible Groups")
    class PinCompatibleTests {

        @Test
        @DisplayName("ATmega88/168/328 are pin-compatible (28-pin)")
        void atmega88168328PinCompatible() {
            // These are pin-compatible but different series
            // This tests the series extraction, not replacement
            assertEquals("ATMEGA88", handler.extractSeries("ATMEGA88P-PU"));
            assertEquals("ATMEGA168", handler.extractSeries("ATMEGA168P-PU"));
            assertEquals("ATMEGA328", handler.extractSeries("ATMEGA328P-PU"));
        }

        @Test
        @DisplayName("ATtiny25/45/85 are pin-compatible (8-pin)")
        void attiny254585PinCompatible() {
            assertEquals("ATTINY25", handler.extractSeries("ATTINY25-20PU"));
            assertEquals("ATTINY45", handler.extractSeries("ATTINY45-20PU"));
            assertEquals("ATTINY85", handler.extractSeries("ATTINY85-20PU"));
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {

        @Test
        @DisplayName("Should support expected component types")
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.MICROCONTROLLER));
            assertTrue(types.contains(ComponentType.MICROCONTROLLER_ATMEL));
            assertTrue(types.contains(ComponentType.MCU_ATMEL));
            assertTrue(types.contains(ComponentType.MEMORY));
            assertTrue(types.contains(ComponentType.MEMORY_ATMEL));
            assertTrue(types.contains(ComponentType.TOUCH_ATMEL));
            assertTrue(types.contains(ComponentType.CRYPTO_ATMEL));
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
            assertFalse(handler.matches(null, ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "ATMEGA328P"));
            assertFalse(handler.isOfficialReplacement("ATMEGA328P", null));
        }

        @Test
        @DisplayName("Should handle empty MPN gracefully")
        void shouldHandleEmptyMpn() {
            assertFalse(handler.matches("", ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        @DisplayName("Should handle null component type")
        void shouldHandleNullType() {
            assertFalse(handler.matches("ATMEGA328P", null, registry));
        }

        @Test
        @DisplayName("Should be case-insensitive for MPN matching")
        void shouldBeCaseInsensitive() {
            assertTrue(handler.matches("atmega328p", ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertTrue(handler.matches("ATMEGA328P", ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertTrue(handler.matches("Atmega328p", ComponentType.MICROCONTROLLER_ATMEL, registry));
        }
    }

    @Nested
    @DisplayName("Arduino Board MCU Mappings")
    class ArduinoBoardTests {

        @Test
        @DisplayName("Arduino Uno MCU (ATMEGA328P)")
        void arduinoUnoMcu() {
            assertTrue(handler.matches("ATMEGA328P-PU", ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertEquals("PDIP", handler.extractPackageCode("ATMEGA328P-PU"));
        }

        @Test
        @DisplayName("Arduino Mega MCU (ATMEGA2560)")
        void arduinoMegaMcu() {
            assertTrue(handler.matches("ATMEGA2560-16AU", ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertEquals("TQFP", handler.extractPackageCode("ATMEGA2560-16AU"));
        }

        @Test
        @DisplayName("Arduino Leonardo MCU (ATMEGA32U4)")
        void arduinoLeonardoMcu() {
            assertTrue(handler.matches("ATMEGA32U4-AU", ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertEquals("TQFP", handler.extractPackageCode("ATMEGA32U4-AU"));
        }

        @Test
        @DisplayName("Arduino Due MCU (ATSAM3X8E)")
        void arduinoDueMcu() {
            assertTrue(handler.matches("ATSAM3X8E-AU", ComponentType.MICROCONTROLLER_ATMEL, registry));
            assertEquals("TQFP", handler.extractPackageCode("ATSAM3X8E-AU"));
        }
    }
}
