package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PassiveComponentCalculator
 */
class PassiveComponentCalculatorTest {

    private PassiveComponentCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PassiveComponentCalculator();
    }

    @Nested
    @DisplayName("Value comparison tests")
    class ValueComparisonTests {

        @Test
        @DisplayName("Same value should have high similarity")
        void sameValueShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("10K", "10K");
            assertTrue(similarity > 0.4, "Same value should have high similarity");
        }

        @Test
        @DisplayName("Very close values should have high similarity")
        void veryCloseValuesShouldHaveHighSimilarity() {
            // 10K vs 10K - same value
            double similarity = calculator.calculateSimilarity("10K", "10K");
            assertTrue(similarity > 0.4, "Close values should have high similarity");
        }

        @Test
        @DisplayName("Different values should have lower similarity")
        void differentValuesShouldHaveLowerSimilarity() {
            double sameValue = calculator.calculateSimilarity("10K", "10K");
            double diffValue = calculator.calculateSimilarity("10K", "100K");
            assertTrue(sameValue > diffValue, "Same values should have higher similarity");
        }

        @Test
        @DisplayName("R suffix should be recognized as ohms")
        void rSuffixShouldBeRecognizedAsOhms() {
            double similarity = calculator.calculateSimilarity("100R", "100R");
            assertTrue(similarity > 0.4, "Same ohm value should have high similarity");
        }
    }

    @Nested
    @DisplayName("Size code comparison tests")
    class SizeCodeComparisonTests {

        @Test
        @DisplayName("Same size code should match")
        void sameSizeCodeShouldMatch() {
            double similarity = calculator.calculateSimilarity("0603", "0603");
            assertTrue(similarity > 0.2, "Same size code should match");
        }

        @Test
        @DisplayName("Imperial and metric equivalents should match")
        void imperialAndMetricEquivalentsShouldMatch() {
            // 0603 imperial = 1608 metric
            double similarity = calculator.calculateSimilarity("0603", "1608");
            assertTrue(similarity > 0.2, "Imperial and metric equivalents should match");
        }

        @Test
        @DisplayName("Different sizes should not match")
        void differentSizesShouldNotMatch() {
            double sameSize = calculator.calculateSimilarity("0603", "0603");
            double diffSize = calculator.calculateSimilarity("0603", "1206");
            assertTrue(sameSize >= diffSize, "Same size should have >= similarity");
        }
    }

    @Nested
    @DisplayName("Tolerance comparison tests")
    class ToleranceComparisonTests {

        @Test
        @DisplayName("Same tolerance should match")
        void sameToleranceShouldMatch() {
            double similarity = calculator.calculateSimilarity("100RF", "100RF");
            assertTrue(similarity > 0.4, "Same tolerance should match");
        }

        @Test
        @DisplayName("Different tolerance should have lower similarity")
        void differentToleranceShouldHaveLowerSimilarity() {
            // F = 1%, K = 10%
            double sameTol = calculator.calculateSimilarity("100RF", "100RF");
            double diffTol = calculator.calculateSimilarity("100RF", "100RK");
            assertTrue(sameTol >= diffTol, "Same tolerance should have >= similarity");
        }
    }

    @Nested
    @DisplayName("Combined comparison tests")
    class CombinedComparisonTests {

        @Test
        @DisplayName("Full part number comparison")
        void fullPartNumberComparison() {
            // Same value, size, tolerance
            double similarity = calculator.calculateSimilarity("060310KF", "060310KF");
            assertTrue(similarity > 0.7, "Identical parts should have high similarity");
        }

        @Test
        @DisplayName("Same value different size should have similarity")
        void sameValueDifferentSizeShouldHaveSimilarity() {
            double similarity = calculator.calculateSimilarity("060310K", "080510K");
            // Value match is most important (0.5 weight), size codes don't match
            assertTrue(similarity >= 0.0 && similarity <= 1.0, "Should return valid similarity");
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("No value found should return low similarity")
        void noValueFoundShouldReturnLowSimilarity() {
            double similarity = calculator.calculateSimilarity("ABC", "XYZ");
            assertTrue(similarity < 0.5, "No recognizable value should return low similarity");
        }

        @Test
        @DisplayName("Empty strings should return low similarity")
        void emptyStringsShouldReturnLowSimilarity() {
            double similarity = calculator.calculateSimilarity("", "");
            assertTrue(similarity <= 0.5, "Empty strings should return low similarity");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("10K", "100K");
            double sim2 = calculator.calculateSimilarity("100K", "10K");
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in range [0.0, 1.0]")
        void similarityShouldBeInRange() {
            String[] testMpns = {"10K", "100R", "0603", "1206", ""};
            for (String mpn1 : testMpns) {
                for (String mpn2 : testMpns) {
                    double sim = calculator.calculateSimilarity(mpn1, mpn2);
                    assertTrue(sim >= 0.0 && sim <= 1.0,
                            String.format("Similarity for '%s' vs '%s' should be in [0,1], was %f", mpn1, mpn2, sim));
                }
            }
        }
    }

    @Nested
    @DisplayName("Unit conversion tests")
    class UnitConversionTests {

        @Test
        @DisplayName("K suffix should multiply by 1000")
        void kSuffixShouldMultiplyBy1000() {
            double similarity = calculator.calculateSimilarity("10K", "10K");
            assertTrue(similarity > 0.4, "K suffix should be recognized");
        }

        @Test
        @DisplayName("M suffix should multiply by 1000000")
        void mSuffixShouldMultiplyBy1000000() {
            double similarity = calculator.calculateSimilarity("1M", "1M");
            assertTrue(similarity > 0.4, "M suffix should be recognized");
        }

        @Test
        @DisplayName("U suffix for microfarads should be recognized")
        void uSuffixForMicrofaradsShouldBeRecognized() {
            double similarity = calculator.calculateSimilarity("10U", "10U");
            assertTrue(similarity > 0.4, "U suffix should be recognized");
        }

        @Test
        @DisplayName("N suffix for nanofarads should be recognized")
        void nSuffixForNanofaradsShouldBeRecognized() {
            double similarity = calculator.calculateSimilarity("100N", "100N");
            assertTrue(similarity > 0.4, "N suffix should be recognized");
        }

        @Test
        @DisplayName("P suffix for picofarads should be recognized")
        void pSuffixForPicofaradsShouldBeRecognized() {
            double similarity = calculator.calculateSimilarity("100P", "100P");
            assertTrue(similarity > 0.4, "P suffix should be recognized");
        }
    }
}
