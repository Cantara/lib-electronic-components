package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LevenshteinCalculator
 */
class LevenshteinCalculatorTest {

    private LevenshteinCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new LevenshteinCalculator();
    }

    @Nested
    @DisplayName("calculateDistance tests")
    class DistanceTests {

        @Test
        @DisplayName("Identical strings should have distance 0")
        void identicalStringsShouldHaveZeroDistance() {
            assertEquals(0, calculator.calculateDistance("hello", "hello"));
        }

        @Test
        @DisplayName("Empty string vs non-empty should equal length")
        void emptyVsNonEmptyShouldEqualLength() {
            assertEquals(5, calculator.calculateDistance("", "hello"));
            assertEquals(5, calculator.calculateDistance("hello", ""));
        }

        @Test
        @DisplayName("Single character difference")
        void singleCharacterDifference() {
            assertEquals(1, calculator.calculateDistance("cat", "bat"));
            assertEquals(1, calculator.calculateDistance("cat", "car"));
            assertEquals(1, calculator.calculateDistance("cat", "cats"));
        }

        @Test
        @DisplayName("Multiple character differences")
        void multipleCharacterDifferences() {
            assertEquals(3, calculator.calculateDistance("kitten", "sitting"));
            // "hello" -> "hallo" is only 1 edit (e->a)
            assertEquals(1, calculator.calculateDistance("hello", "hallo"));
            // "cat" -> "dog" is 3 edits
            assertEquals(3, calculator.calculateDistance("cat", "dog"));
        }

        @Test
        @DisplayName("Null strings should return -1")
        void nullStringsShouldReturnNegativeOne() {
            assertEquals(-1, calculator.calculateDistance(null, "hello"));
            assertEquals(-1, calculator.calculateDistance("hello", null));
        }
    }

    @Nested
    @DisplayName("calculateSimilarity tests")
    class SimilarityTests {

        @Test
        @DisplayName("Identical strings should have similarity 1.0")
        void identicalStringsShouldHaveSimilarityOne() {
            assertEquals(1.0, calculator.calculateSimilarity("hello", "hello"), 0.001);
        }

        @Test
        @DisplayName("Completely different strings should have low similarity")
        void differentStringsShouldHaveLowSimilarity() {
            double similarity = calculator.calculateSimilarity("abc", "xyz");
            assertTrue(similarity < 0.5, "Completely different strings should have low similarity");
        }

        @Test
        @DisplayName("Similar strings should have high similarity")
        void similarStringsShouldHaveHighSimilarity() {
            double similarity = calculator.calculateSimilarity("LM358", "LM358N");
            assertTrue(similarity > 0.8, "LM358 and LM358N should be very similar");
        }

        @Test
        @DisplayName("Null strings should return 0.0")
        void nullStringsShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "hello"));
            assertEquals(0.0, calculator.calculateSimilarity("hello", null));
        }

        @Test
        @DisplayName("Empty strings should return 0.0")
        void emptyStringsShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("", "hello"));
            assertEquals(0.0, calculator.calculateSimilarity("hello", ""));
        }
    }

    @Nested
    @DisplayName("calculateCaseInsensitiveSimilarity tests")
    class CaseInsensitiveTests {

        @Test
        @DisplayName("Case differences should be ignored")
        void caseDifferencesShouldBeIgnored() {
            assertEquals(1.0, calculator.calculateCaseInsensitiveSimilarity("Hello", "hello"), 0.001);
            assertEquals(1.0, calculator.calculateCaseInsensitiveSimilarity("HELLO", "hello"), 0.001);
        }

        @Test
        @DisplayName("MPN case variations should match")
        void mpnCaseVariationsShouldMatch() {
            double similarity = calculator.calculateCaseInsensitiveSimilarity("LM358", "lm358");
            assertEquals(1.0, similarity, 0.001);
        }
    }

    @Nested
    @DisplayName("calculateNumericSimilarity tests")
    class NumericSimilarityTests {

        @Test
        @DisplayName("Same numbers should have similarity 1.0")
        void sameNumbersShouldHaveSimilarityOne() {
            assertEquals(1.0, calculator.calculateNumericSimilarity("100", "100"), 0.001);
        }

        @Test
        @DisplayName("Similar numbers should have high similarity")
        void similarNumbersShouldHaveHighSimilarity() {
            double similarity = calculator.calculateNumericSimilarity("100", "101");
            assertTrue(similarity > 0.9, "100 and 101 should be very similar");
        }

        @Test
        @DisplayName("Very different numbers should have low similarity")
        void veryDifferentNumbersShouldHaveLowSimilarity() {
            double similarity = calculator.calculateNumericSimilarity("100", "10000");
            assertTrue(similarity < 0.5, "100 and 10000 should have low similarity");
        }

        @Test
        @DisplayName("Invalid numbers should fall back to string similarity")
        void invalidNumbersShouldFallbackToStringSimilarity() {
            double similarity = calculator.calculateNumericSimilarity("abc", "abd");
            assertTrue(similarity > 0.0, "Should fall back to string similarity");
        }
    }

    @Nested
    @DisplayName("calculateWeightedSimilarity tests")
    class WeightedSimilarityTests {

        @Test
        @DisplayName("Same strings should have similarity 1.0 regardless of weight")
        void sameStringsShouldHaveSimilarityOne() {
            assertEquals(1.0, calculator.calculateWeightedSimilarity("hello", "hello", 0.5), 0.001);
        }

        @Test
        @DisplayName("Length weight should penalize different length strings")
        void lengthWeightShouldPenalize() {
            double noWeight = calculator.calculateWeightedSimilarity("hello", "helloworld", 0.0);
            double withWeight = calculator.calculateWeightedSimilarity("hello", "helloworld", 0.5);
            assertTrue(withWeight < noWeight, "Weight should penalize length differences");
        }
    }

    @Nested
    @DisplayName("calculatePrefixSimilarity tests")
    class PrefixSimilarityTests {

        @Test
        @DisplayName("Same prefix should boost similarity")
        void samePrefixShouldBoostSimilarity() {
            double similarity = calculator.calculatePrefixSimilarity("LM358N", "LM358D", 4);
            assertTrue(similarity > 0.8, "Same prefix 'LM35' should give high similarity");
        }

        @Test
        @DisplayName("Different prefix should reduce similarity")
        void differentPrefixShouldReduceSimilarity() {
            double samePrefixSim = calculator.calculatePrefixSimilarity("LM358", "LM324", 2);
            double diffPrefixSim = calculator.calculatePrefixSimilarity("LM358", "MC1458", 2);
            assertTrue(samePrefixSim > diffPrefixSim, "Same prefix should have higher similarity");
        }
    }

    @Nested
    @DisplayName("calculateSimilarityWithSubstitutions tests")
    class SubstitutionTests {

        @Test
        @DisplayName("Known substitutions should increase similarity")
        void knownSubstitutionsShouldMatch() {
            Map<String, String> substitutions = Map.of("2N", "PN");
            double similarity = calculator.calculateSimilarityWithSubstitutions(
                    "2N2222", "PN2222", substitutions);
            assertEquals(0.9, similarity, 0.01, "Known substitution should give high similarity");
        }

        @Test
        @DisplayName("No matching substitution should use basic similarity")
        void noMatchingShouldUseBasicSimilarity() {
            Map<String, String> substitutions = Map.of("XX", "YY");
            double withSub = calculator.calculateSimilarityWithSubstitutions(
                    "hello", "world", substitutions);
            double without = calculator.calculateSimilarity("hello", "world");
            assertEquals(without, withSub, 0.001);
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("hello", "hallo");
            double sim2 = calculator.calculateSimilarity("hallo", "hello");
            assertEquals(sim1, sim2, 0.001);
        }

        @Test
        @DisplayName("Distance should be symmetric")
        void distanceShouldBeSymmetric() {
            int dist1 = calculator.calculateDistance("hello", "hallo");
            int dist2 = calculator.calculateDistance("hallo", "hello");
            assertEquals(dist1, dist2);
        }

        @Test
        @DisplayName("Similarity should be in range [0.0, 1.0]")
        void similarityShouldBeInRange() {
            String[] testStrings = {"hello", "world", "test", "", "abcdefghijklmnop"};
            for (String s1 : testStrings) {
                for (String s2 : testStrings) {
                    double sim = calculator.calculateSimilarity(s1, s2);
                    assertTrue(sim >= 0.0 && sim <= 1.0,
                            String.format("Similarity for '%s' vs '%s' should be in [0,1], was %f", s1, s2, sim));
                }
            }
        }
    }
}
