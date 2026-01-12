package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DefaultSimilarityCalculator
 */
class DefaultSimilarityCalculatorTest {

    private DefaultSimilarityCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DefaultSimilarityCalculator();
    }

    @Nested
    @DisplayName("Basic similarity tests")
    class BasicSimilarityTests {

        @Test
        @DisplayName("Identical strings should have similarity 1.0")
        void identicalStringsShouldHaveSimilarityOne() {
            assertEquals(1.0, calculator.calculateSimilarity("LM358N", "LM358N"), 0.001);
        }

        @Test
        @DisplayName("Null strings should return 0.0")
        void nullStringsShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity(null, "LM358"));
            assertEquals(0.0, calculator.calculateSimilarity("LM358", null));
            assertEquals(0.0, calculator.calculateSimilarity(null, null));
        }

        @Test
        @DisplayName("Empty strings should return 0.0")
        void emptyStringsShouldReturnZero() {
            assertEquals(0.0, calculator.calculateSimilarity("", "LM358"));
            assertEquals(0.0, calculator.calculateSimilarity("LM358", ""));
        }
    }

    @Nested
    @DisplayName("MPN comparison tests")
    class MpnComparisonTests {

        @Test
        @DisplayName("Same part with different suffix should have high similarity")
        void samePartDifferentSuffixShouldMatch() {
            double similarity = calculator.calculateSimilarity("LM358N", "LM358D");
            assertTrue(similarity > 0.7, "Same part with different suffix should be similar");
        }

        @Test
        @DisplayName("Same prefix same numbers should have higher similarity than same prefix different numbers")
        void samePrefixSameNumbersShouldBeHigher() {
            double sameNumbers = calculator.calculateSimilarity("LM358", "LM358N");
            double diffNumbers = calculator.calculateSimilarity("LM358", "LM100");
            // LM358 vs LM358N: same prefix (LM), same numbers (358), slight suffix diff
            // LM358 vs LM100: same prefix (LM), different numbers (358 vs 100)
            assertTrue(sameNumbers > diffNumbers, "Same numbers should have higher similarity");
        }

        @Test
        @DisplayName("Different prefix should have lower similarity")
        void differentPrefixShouldHaveLowerSimilarity() {
            double samePrefix = calculator.calculateSimilarity("LM358", "LM324");
            double diffPrefix = calculator.calculateSimilarity("LM358", "MC1458");
            // This tests prefix weight - both have different numeric parts
            // samePrefix: LM vs LM = 1.0 prefix match
            // diffPrefix: LM vs MC = lower prefix match
            assertTrue(samePrefix >= diffPrefix, "Same prefix should have >= similarity");
        }

        @Test
        @DisplayName("Standard MPNs similarity")
        void standardMpnsSimilarity() {
            // The DefaultSimilarityCalculator is optimized for standard MPNs starting with letters
            double similarity = calculator.calculateSimilarity("IRF530", "IRF540");
            // Same prefix (IRF), similar numbers (530 vs 540), no suffix
            assertTrue(similarity > 0.5, "Standard MPNs with similar patterns should match");
        }
    }

    @Nested
    @DisplayName("Weighted component tests")
    class WeightedComponentTests {

        @Test
        @DisplayName("Similar numbers should have higher similarity")
        void similarNumbersShouldMatch() {
            // Same prefix, close numbers
            double closeSim = calculator.calculateSimilarity("LM358", "LM359");
            // Same prefix, farther numbers
            double farSim = calculator.calculateSimilarity("LM358", "LM100");

            // Closer numbers should have higher similarity
            assertTrue(closeSim > farSim,
                    "Closer numeric parts should give higher similarity");
        }

        @Test
        @DisplayName("Prefix similarity contributes to overall score")
        void prefixContributesToScore() {
            double samePrefix = calculator.calculateSimilarity("ABC100", "ABC200");
            double diffPrefix = calculator.calculateSimilarity("ABC100", "XYZ100");
            // Both have different numbers, but prefix weight (0.3) should make samePrefix higher
            // Actually they have same numbers "100" so numeric is same...
            // Let's use truly different numbers
            double samePref2 = calculator.calculateSimilarity("ABC100", "ABC999");
            double diffPref2 = calculator.calculateSimilarity("ABC100", "XYZ999");
            assertTrue(samePref2 >= diffPref2, "Same prefix should contribute to >= similarity");
        }
    }

    @Nested
    @DisplayName("Symmetry and property tests")
    class PropertyTests {

        @Test
        @DisplayName("Similarity should be symmetric")
        void similarityShouldBeSymmetric() {
            double sim1 = calculator.calculateSimilarity("LM358", "MC1458");
            double sim2 = calculator.calculateSimilarity("MC1458", "LM358");
            assertEquals(sim1, sim2, 0.001, "Similarity should be symmetric");
        }

        @Test
        @DisplayName("Similarity should be in range [0.0, 1.0]")
        void similarityShouldBeInRange() {
            String[] testMpns = {"LM358", "MC1458", "2N2222", "IRF530", "ABC123", ""};
            for (String mpn1 : testMpns) {
                for (String mpn2 : testMpns) {
                    if (mpn1 != null && mpn2 != null) {
                        double sim = calculator.calculateSimilarity(mpn1, mpn2);
                        assertTrue(sim >= 0.0 && sim <= 1.0,
                                String.format("Similarity for '%s' vs '%s' should be in [0,1], was %f",
                                        mpn1, mpn2, sim));
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Numeric similarity tests")
    class NumericSimilarityTests {

        @Test
        @DisplayName("Close numbers should have high numeric similarity")
        void closeNumbersShouldMatch() {
            // LM358 vs LM359 - only differs by 1 in numeric part
            double similarity = calculator.calculateSimilarity("LM358", "LM359");
            assertTrue(similarity > 0.8, "Close numbers should give high similarity");
        }

        @Test
        @DisplayName("Large numeric differences should reduce similarity")
        void largeNumericDifferencesShouldReduceSimilarity() {
            double close = calculator.calculateSimilarity("LM358", "LM359");
            double far = calculator.calculateSimilarity("LM358", "LM7805");
            assertTrue(close > far, "Larger numeric difference should have lower similarity");
        }
    }

    @Nested
    @DisplayName("Real-world MPN tests")
    class RealWorldTests {

        @Test
        @DisplayName("Op-amp package variants")
        void opAmpPackageVariants() {
            // LM358 in different packages
            assertTrue(calculator.calculateSimilarity("LM358N", "LM358D") > 0.7);
            assertTrue(calculator.calculateSimilarity("LM358N", "LM358P") > 0.7);
        }

        @Test
        @DisplayName("Voltage regulator variants")
        void voltageRegulatorVariants() {
            // 7805 variants
            double similarity = calculator.calculateSimilarity("LM7805", "L7805");
            assertTrue(similarity > 0.6, "Voltage regulator variants should match");
        }

        @Test
        @DisplayName("Microcontroller variants")
        void microcontrollerVariants() {
            // ATmega variants - ATMEGA328P vs ATMEGA328
            // prefix: ATMEGA, numeric: 328, suffix: P vs nothing
            double similarity = calculator.calculateSimilarity("ATMEGA328P", "ATMEGA328");
            // With suffix weight 0.2, missing suffix gets 0.5 score, so not > 0.9
            assertTrue(similarity > 0.7, "Same MCU different suffix should have good similarity");
        }
    }
}
