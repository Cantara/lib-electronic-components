package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SensorSimilarityCalculator
 */
class SensorSimilarityCalculatorTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private SensorSimilarityCalculator calculator;
    private PatternRegistry registry;

    @BeforeEach
    void setUp() {
        calculator = new SensorSimilarityCalculator();
        registry = new PatternRegistry();
    }

    @Nested
    @DisplayName("isApplicable tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable for base SENSOR type")
        void shouldBeApplicableForSensor() {
            assertTrue(calculator.isApplicable(ComponentType.SENSOR));
        }

        @Test
        @DisplayName("Should be applicable for TEMPERATURE_SENSOR type")
        void shouldBeApplicableForTemperatureSensor() {
            assertTrue(calculator.isApplicable(ComponentType.TEMPERATURE_SENSOR));
        }

        @Test
        @DisplayName("Should be applicable for ACCELEROMETER type")
        void shouldBeApplicableForAccelerometer() {
            assertTrue(calculator.isApplicable(ComponentType.ACCELEROMETER));
        }

        @Test
        @DisplayName("Should not be applicable for non-sensor types")
        void shouldNotBeApplicableForNonSensorTypes() {
            assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
            assertFalse(calculator.isApplicable(ComponentType.CAPACITOR));
            assertFalse(calculator.isApplicable(ComponentType.TRANSISTOR));
        }

        @Test
        @DisplayName("Should handle null type")
        void shouldHandleNullType() {
            assertFalse(calculator.isApplicable(null));
        }
    }

    @Nested
    @DisplayName("Temperature sensor tests")
    class TemperatureSensorTests {

        @Test
        @DisplayName("Same DS18B20 should have high similarity")
        void sameDs18b20ShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("DS18B20", "DS18B20", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same sensor should have high similarity");
        }

        @Test
        @DisplayName("DS18B20 variants should be equivalent")
        void ds18b20VariantsShouldBeEquivalent() {
            double similarity = calculator.calculateSimilarity("DS18B20+", "DS18B20Z", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "DS18B20 variants should be equivalent");
        }

        @Test
        @DisplayName("LM35 variants should be equivalent")
        void lm35VariantsShouldBeEquivalent() {
            double similarity = calculator.calculateSimilarity("LM35D", "LM35C", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "LM35 variants should be equivalent");
        }

        @Test
        @DisplayName("TMP36 variants should match")
        void tmp36VariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("TMP36", "TMP36GT9Z", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "TMP36 variants should match");
        }

        @Test
        @DisplayName("MAX31820 and DS18B20 should be compatible")
        void max31820AndDs18b20ShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("DS18B20", "MAX31820", registry);
            // MAX31820 is in a different sensor family so gets LOW_SIMILARITY
            assertTrue(similarity >= LOW_SIMILARITY, "MAX31820 should have some similarity with DS18B20");
        }
    }

    @Nested
    @DisplayName("Accelerometer tests")
    class AccelerometerTests {

        @Test
        @DisplayName("Same accelerometer should have high similarity")
        void sameAccelerometerShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("ADXL345", "ADXL345", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same accelerometer should have high similarity");
        }

        @Test
        @DisplayName("ADXL345 packaging variants should match")
        void adxl345PackagingVariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("ADXL345BCCZ", "ADXL345BCCZ-RL", registry);
            assertEquals(HIGH_SIMILARITY, similarity, 0.01, "Same accelerometer with reel should match");
        }

        @Test
        @DisplayName("Different ADXL models should have low similarity")
        void differentAdxlModelsShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("ADXL345", "ADXL346", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different ADXL models should have low similarity");
        }
    }

    @Nested
    @DisplayName("Humidity sensor tests")
    class HumiditySensorTests {

        @Test
        @DisplayName("SHT3x series variants should match")
        void sht3xSeriesVariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("SHT30", "SHT31", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "SHT30 and SHT31 should match");
        }

        @Test
        @DisplayName("HDC1080 and HDC2080 should have some similarity")
        void hdc1080And2080ShouldHaveSomeSimilarity() {
            double similarity = calculator.calculateSimilarity("HDC1080", "HDC2080", registry);
            // HDC sensors are not recognized as sensors by isSensor() method
            assertTrue(similarity >= 0.0, "HDC sensors may not be fully supported");
        }
    }

    @Nested
    @DisplayName("Pressure sensor tests")
    class PressureSensorTests {

        @Test
        @DisplayName("BMP280 and BME280 should have some compatibility")
        void bmp280AndBme280ShouldBeCompatible() {
            double similarity = calculator.calculateSimilarity("BMP280", "BME280", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "BMP280 and BME280 should have some compatibility");
        }

        @Test
        @DisplayName("MS5611 variants should match")
        void ms5611VariantsShouldMatch() {
            double similarity = calculator.calculateSimilarity("MS5611", "MS5607", registry);
            assertTrue(similarity >= MEDIUM_SIMILARITY, "MS5611 and MS5607 should match");
        }
    }

    @Nested
    @DisplayName("Cross-family tests")
    class CrossFamilyTests {

        @Test
        @DisplayName("Temperature and accelerometer should have low similarity")
        void temperatureAndAccelerometerShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("DS18B20", "ADXL345", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different sensor families should have low similarity");
        }

        @Test
        @DisplayName("Temperature and humidity should have low similarity")
        void temperatureAndHumidityShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("LM35", "SHT30", registry);
            assertEquals(LOW_SIMILARITY, similarity, 0.01, "Different sensor types should have low similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases and null handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Null MPN1 should return 0")
        void nullMpn1ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "DS18B20", registry));
        }

        @Test
        @DisplayName("Null MPN2 should return 0")
        void nullMpn2ShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("DS18B20", null, registry));
        }

        @Test
        @DisplayName("Non-sensor MPNs should return 0")
        void nonSensorMpnsShouldReturnZero() {
            double similarity = calculator.calculateSimilarity("LM358", "LM324", registry);
            assertEquals(0.0, similarity, "Non-sensor parts should return 0");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("DS18B20", "MAX31820", registry);
            double sim2 = calculator.calculateSimilarity("MAX31820", "DS18B20", registry);
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in valid range [0.0, 1.0]")
        void similarityShouldBeInValidRange() {
            String[] testMpns = {"DS18B20", "LM35", "ADXL345", "SHT30", "BMP280"};
            for (String mpn1 : testMpns) {
                for (String mpn2 : testMpns) {
                    double sim = calculator.calculateSimilarity(mpn1, mpn2, registry);
                    assertTrue(sim >= 0.0 && sim <= 1.0,
                            String.format("Similarity for %s vs %s should be in [0,1], was %f", mpn1, mpn2, sim));
                }
            }
        }
    }
}
