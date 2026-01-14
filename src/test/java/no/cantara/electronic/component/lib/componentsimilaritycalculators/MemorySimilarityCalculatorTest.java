package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MemorySimilarityCalculator
 */
class MemorySimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private MemorySimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new MemorySimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base MEMORY type")
        void shouldBeApplicableForMemory() {
            assertTrue(calculator.isApplicable(ComponentType.MEMORY));
        }

        @Test
        @DisplayName("Should be applicable for null type (handle unrecognized memory)")
        void shouldBeApplicableForNullType() {
            assertTrue(calculator.isApplicable(null));
        }

        @Test
        @DisplayName("Should not be applicable for non-memory types")
        void shouldNotBeApplicableForNonMemoryTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.CAPACITOR));
            assertFalse(calculator.isApplicable(ComponentType.TRANSISTOR));
        }
    }

    @Nested
    @DisplayName("I2C EEPROM tests")
    class I2cEepromTests {

        @Test
        @DisplayName("Same I2C EEPROM should have high similarity")
        void sameI2cEepromShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "24LC256", "24LC256", registry);
            assertEquals(1.0, similarity, 0.01, "Same EEPROM should have similarity 1.0");
        }

        @Test
        @DisplayName("I2C EEPROM variants should be equivalent")
        void i2cEepromVariantsShouldBeEquivalent() {
            double similarity = calculator.calculateSimilarity(
                    "24LC256", "AT24C256", registry);
            assertTrue(similarity >= 0.7, "24LC256 and AT24C256 should be equivalent");
        }

        @Test
        @DisplayName("Microchip and ST I2C EEPROMs should be equivalent")
        void microchipAndStI2cEepromsShouldBeEquivalent() {
            double similarity = calculator.calculateSimilarity(
                    "24LC256", "M24C256", registry);
            assertTrue(similarity >= 0.7, "24LC256 and M24C256 should be equivalent");
        }

        @Test
        @DisplayName("Same EEPROM different revision should have high similarity")
        void sameEepromDifferentRevisionShouldMatch() {
            double similarity = calculator.calculateSimilarity(
                    "24LC256", "24LC256A", registry);
            assertTrue(similarity >= 0.7, "Same EEPROM different revision should match");
        }
    }

    @Nested
    @DisplayName("SPI EEPROM tests")
    class SpiEepromTests {

        @Test
        @DisplayName("SPI EEPROM equivalents should match")
        void spiEepromEquivalentsShouldMatch() {
            double similarity = calculator.calculateSimilarity(
                    "25LC256", "AT25256", registry);
            assertTrue(similarity >= 0.3, "SPI EEPROM equivalents should match");
        }

        @Test
        @DisplayName("Different capacity SPI EEPROMs (larger can replace smaller)")
        void differentCapacitySpiEepromsShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity(
                    "25LC256", "25LC512", registry);
            // Metadata-driven: larger capacity can replace smaller (minimumRequired tolerance)
            // Same type (EEPROM) + same interface (SPI) = high compatibility
            assertTrue(similarity >= MEDIUM_SIMILARITY,
                    "Larger capacity EEPROM can replace smaller - Expected >= " + MEDIUM_SIMILARITY + " but was: " + similarity);
        }
    }

    @Nested
    @DisplayName("SPI Flash tests")
    class SpiFlashTests {

        @Test
        @DisplayName("W25Q32 variants should be equivalent")
        void w25q32VariantsShouldBeEquivalent() {
            double similarity = calculator.calculateSimilarity(
                    "W25Q32JV", "W25Q32FW", registry);
            assertTrue(similarity >= 0.7, "W25Q32 variants should be equivalent");
        }

        @Test
        @DisplayName("Winbond and Macronix Flash should be equivalent")
        void winbondAndMacronixFlashShouldBeEquivalent() {
            double similarity = calculator.calculateSimilarity(
                    "W25Q32JV", "MX25L3233F", registry);
            assertTrue(similarity >= 0.3, "W25Q32 and MX25L should have some similarity");
        }

        @Test
        @DisplayName("Different size Flash (larger can replace smaller)")
        void differentSizeFlashShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity(
                    "W25Q32JV", "W25Q128JV", registry);
            // Metadata-driven: larger capacity can replace smaller (minimumRequired tolerance)
            // Same type (Flash) + same interface (SPI) = high compatibility
            assertTrue(similarity >= MEDIUM_SIMILARITY,
                    "Larger capacity Flash can replace smaller - Expected >= " + MEDIUM_SIMILARITY + " but was: " + similarity);
        }
    }

    @Nested
    @DisplayName("Package variation tests")
    class PackageVariationTests {

        @Test
        @DisplayName("Same memory different package should have high similarity")
        void sameMemoryDifferentPackageShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "W25Q32JVSSIQ", "W25Q32JVSFIQ", registry);
            assertTrue(similarity >= 0.7, "Same memory different package should have high similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "24LC256", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("24LC256", null, registry));
        }

        @Test
        @DisplayName("Non-memory MPNs should return 0")
        void nonMemoryMpnsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("LM358", "LM324", registry);
            assertEquals(0.0, similarity, "Non-memory parts should return 0");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("24LC256", "AT24C256", registry);
            double sim2 = calculator.calculateSimilarity("AT24C256", "24LC256", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"24LC256", "AT24C256", "W25Q32JV", "MX25L3233F"};
            for (String mpn1 : testMpns) {
                for (String mpn2 : testMpns) {
                    double sim = calculator.calculateSimilarity(mpn1, mpn2, registry);
                    assertTrue(sim >= 0.0 && sim <= 1.0,
                            String.format("Similarity for %s vs %s should be in [0,1], was %f", mpn1, mpn2, sim));
                }
            }
        }
    }

    @Nested
    @DisplayName("Interface compatibility tests")
    class InterfaceCompatibilityTests {

        @Test
        @DisplayName("I2C and SPI memory should have low similarity")
        void i2cAndSpiMemoryShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity(
                    "24LC256", "25LC256", registry);
            assertTrue(similarity <= MEDIUM_SIMILARITY, "I2C and SPI memory should have lower similarity");
        }
    }
}
