package no.cantara.electronic.component.lib.similarity.config;

import no.cantara.electronic.component.lib.specs.base.SpecValue;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for all ToleranceRule implementations.
 * Tests scoring behavior, edge cases, and acceptance thresholds.
 */
class ToleranceRuleTest {

    @Nested
    @DisplayName("ExactMatchRule Tests")
    class ExactMatchRuleTests {

        private final ToleranceRule rule = ToleranceRule.exactMatch();

        @Test
        void shouldReturnPerfectScoreForIdenticalStrings() {
            SpecValue<String> original = new SpecValue<>("X7R", SpecUnit.NONE);
            SpecValue<String> candidate = new SpecValue<>("X7R", SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldBeCaseInsensitiveForStrings() {
            SpecValue<String> original = new SpecValue<>("X7R", SpecUnit.NONE);
            SpecValue<String> candidate = new SpecValue<>("x7r", SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnZeroForDifferentStrings() {
            SpecValue<String> original = new SpecValue<>("X7R", SpecUnit.NONE);
            SpecValue<String> candidate = new SpecValue<>("X5R", SpecUnit.NONE);

            assertEquals(0.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnPerfectScoreForIdenticalNumbers() {
            SpecValue<Integer> original = new SpecValue<>(100, SpecUnit.NONE);
            SpecValue<Integer> candidate = new SpecValue<>(100, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnZeroForDifferentNumbers() {
            SpecValue<Integer> original = new SpecValue<>(100, SpecUnit.NONE);
            SpecValue<Integer> candidate = new SpecValue<>(101, SpecUnit.NONE);

            assertEquals(0.0, rule.compare(original, candidate));
        }

        @Test
        void shouldHandleNullValues() {
            SpecValue<String> original = new SpecValue<>("X7R", SpecUnit.NONE);

            assertEquals(0.0, rule.compare(null, original));
            assertEquals(0.0, rule.compare(original, null));
            assertEquals(0.0, rule.compare(null, null));
        }
    }

    @Nested
    @DisplayName("PercentageToleranceRule Tests")
    class PercentageToleranceRuleTests {

        @Test
        void shouldReturnPerfectScoreForExactMatch() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(100.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnPerfectScoreWithinTolerance() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(104.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnPerfectScoreAtToleranceBoundary() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(105.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldDecayLinearlyBeyondTolerance() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);

            // At 7.5% deviation (midpoint between 5% and 10%), expect ~0.5
            SpecValue<Double> candidate = new SpecValue<>(107.5, SpecUnit.NONE);
            double score = rule.compare(original, candidate);
            assertTrue(score > 0.4 && score < 0.6, "Score at 7.5% should be ~0.5, was: " + score);
        }

        @Test
        void shouldReturnZeroBeyondTwiceTheTolerance() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(111.0, SpecUnit.NONE); // 11% deviation (> 2×5%)

            assertEquals(0.0, rule.compare(original, candidate));
        }

        @Test
        void shouldHandleNegativeDeviation() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(96.0, SpecUnit.NONE); // -4% deviation

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldHandleZeroOriginalValue() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(0.0, SpecUnit.NONE);
            SpecValue<Double> candidateZero = new SpecValue<>(0.0, SpecUnit.NONE);
            SpecValue<Double> candidateNonZero = new SpecValue<>(1.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidateZero));
            assertEquals(0.0, rule.compare(original, candidateNonZero));
        }

        @Test
        void shouldHandleNonNumericValuesWithExactMatch() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<String> original = new SpecValue<>("X7R", SpecUnit.NONE);
            SpecValue<String> candidateMatch = new SpecValue<>("X7R", SpecUnit.NONE);
            SpecValue<String> candidateDiff = new SpecValue<>("X5R", SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidateMatch));
            assertEquals(0.0, rule.compare(original, candidateDiff));
        }
    }

    @Nested
    @DisplayName("MinimumRequiredRule Tests")
    class MinimumRequiredRuleTests {

        private final ToleranceRule rule = ToleranceRule.minimumRequired();

        @Test
        void shouldReturnPerfectScoreForExactMatch() {
            SpecValue<Double> original = new SpecValue<>(50.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(50.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnHighScoreForHigherValue() {
            SpecValue<Double> original = new SpecValue<>(50.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(60.0, SpecUnit.NONE);

            assertEquals(0.95, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnMarginalScoreForSlightlyLower() {
            SpecValue<Double> original = new SpecValue<>(50.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(46.0, SpecUnit.NONE); // 92% of original

            assertEquals(0.8, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnZeroForBelowMinimum() {
            SpecValue<Double> original = new SpecValue<>(50.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(44.0, SpecUnit.NONE); // 88% of original

            assertEquals(0.0, rule.compare(original, candidate));
        }

        @Test
        void shouldUseLenientAcceptanceThreshold() {
            SpecValue<Double> original = new SpecValue<>(50.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(46.0, SpecUnit.NONE); // Score = 0.8

            assertTrue(rule.isAcceptable(original, candidate));
        }

        @Test
        void shouldHandleNonNumericValuesWithExactMatch() {
            SpecValue<String> original = new SpecValue<>("50V", SpecUnit.NONE);
            SpecValue<String> candidateMatch = new SpecValue<>("50V", SpecUnit.NONE);
            SpecValue<String> candidateDiff = new SpecValue<>("25V", SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidateMatch));
            assertEquals(0.0, rule.compare(original, candidateDiff));
        }
    }

    @Nested
    @DisplayName("MaximumAllowedRule Tests")
    class MaximumAllowedRuleTests {

        @Test
        void shouldReturnPerfectScoreForExactMatch() {
            ToleranceRule rule = ToleranceRule.maximumAllowed(1.2);
            SpecValue<Double> original = new SpecValue<>(10.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(10.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnHighScoreForBetterSpec() {
            ToleranceRule rule = ToleranceRule.maximumAllowed(1.2);
            SpecValue<Double> original = new SpecValue<>(10.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(8.0, SpecUnit.NONE); // Lower is better for ESR/Rds(on)

            assertEquals(0.98, rule.compare(original, candidate));
        }

        @Test
        void shouldDecayLinearlyWithinLimit() {
            ToleranceRule rule = ToleranceRule.maximumAllowed(1.2);
            SpecValue<Double> original = new SpecValue<>(10.0, SpecUnit.NONE);

            // At 10% worse (11.0), expect high score
            SpecValue<Double> candidate1 = new SpecValue<>(11.0, SpecUnit.NONE);
            double score1 = rule.compare(original, candidate1);
            assertTrue(score1 > 0.8 && score1 < 1.0, "Score at 110% should be >0.8, was: " + score1);

            // At 20% worse (12.0), expect lower score
            SpecValue<Double> candidate2 = new SpecValue<>(12.0, SpecUnit.NONE);
            double score2 = rule.compare(original, candidate2);
            assertTrue(score2 >= 0.7 && score2 < 0.8, "Score at 120% should be ~0.7, was: " + score2);
        }

        @Test
        void shouldReturnZeroBeyondMaximum() {
            ToleranceRule rule = ToleranceRule.maximumAllowed(1.2);
            SpecValue<Double> original = new SpecValue<>(10.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(12.5, SpecUnit.NONE); // 125% > 120%

            assertEquals(0.0, rule.compare(original, candidate));
        }

        @Test
        void shouldHandleZeroOriginalValue() {
            ToleranceRule rule = ToleranceRule.maximumAllowed(1.2);
            SpecValue<Double> original = new SpecValue<>(0.0, SpecUnit.NONE);
            SpecValue<Double> candidateZero = new SpecValue<>(0.0, SpecUnit.NONE);
            SpecValue<Double> candidateNonZero = new SpecValue<>(1.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidateZero));
            assertEquals(0.0, rule.compare(original, candidateNonZero));
        }

        @Test
        void shouldHandleNonNumericValuesWithExactMatch() {
            ToleranceRule rule = ToleranceRule.maximumAllowed(1.2);
            SpecValue<String> original = new SpecValue<>("10mΩ", SpecUnit.NONE);
            SpecValue<String> candidateMatch = new SpecValue<>("10mΩ", SpecUnit.NONE);
            SpecValue<String> candidateDiff = new SpecValue<>("12mΩ", SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidateMatch));
            assertEquals(0.0, rule.compare(original, candidateDiff));
        }
    }

    @Nested
    @DisplayName("RangeToleranceRule Tests")
    class RangeToleranceRuleTests {

        @Test
        void shouldReturnPerfectScoreForExactMatch() {
            ToleranceRule rule = ToleranceRule.rangeTolerance(0.8, 1.2);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(100.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidate));
        }

        @Test
        void shouldReturnHighScoreWithinRange() {
            ToleranceRule rule = ToleranceRule.rangeTolerance(0.8, 1.2);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);

            SpecValue<Double> candidate1 = new SpecValue<>(85.0, SpecUnit.NONE);
            assertEquals(0.9, rule.compare(original, candidate1));

            SpecValue<Double> candidate2 = new SpecValue<>(115.0, SpecUnit.NONE);
            assertEquals(0.9, rule.compare(original, candidate2));
        }

        @Test
        void shouldReturnHighScoreAtRangeBoundaries() {
            ToleranceRule rule = ToleranceRule.rangeTolerance(0.8, 1.2);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);

            SpecValue<Double> candidateMin = new SpecValue<>(80.0, SpecUnit.NONE);
            assertEquals(0.9, rule.compare(original, candidateMin));

            SpecValue<Double> candidateMax = new SpecValue<>(120.0, SpecUnit.NONE);
            assertEquals(0.9, rule.compare(original, candidateMax));
        }

        @Test
        void shouldReturnZeroOutsideRange() {
            ToleranceRule rule = ToleranceRule.rangeTolerance(0.8, 1.2);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);

            SpecValue<Double> candidateLow = new SpecValue<>(75.0, SpecUnit.NONE);
            assertEquals(0.0, rule.compare(original, candidateLow));

            SpecValue<Double> candidateHigh = new SpecValue<>(125.0, SpecUnit.NONE);
            assertEquals(0.0, rule.compare(original, candidateHigh));
        }

        @Test
        void shouldHandleZeroOriginalValue() {
            ToleranceRule rule = ToleranceRule.rangeTolerance(0.8, 1.2);
            SpecValue<Double> original = new SpecValue<>(0.0, SpecUnit.NONE);
            SpecValue<Double> candidateZero = new SpecValue<>(0.0, SpecUnit.NONE);
            SpecValue<Double> candidateNonZero = new SpecValue<>(1.0, SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidateZero));
            assertEquals(0.0, rule.compare(original, candidateNonZero));
        }

        @Test
        void shouldRejectInvalidMultipliers() {
            assertThrows(IllegalArgumentException.class,
                    () -> ToleranceRule.rangeTolerance(-0.1, 1.2));
            assertThrows(IllegalArgumentException.class,
                    () -> ToleranceRule.rangeTolerance(0.8, -0.1));
            assertThrows(IllegalArgumentException.class,
                    () -> ToleranceRule.rangeTolerance(1.5, 1.2)); // min > max
        }

        @Test
        void shouldHandleNonNumericValuesWithExactMatch() {
            ToleranceRule rule = ToleranceRule.rangeTolerance(0.8, 1.2);
            SpecValue<String> original = new SpecValue<>("100V", SpecUnit.NONE);
            SpecValue<String> candidateMatch = new SpecValue<>("100V", SpecUnit.NONE);
            SpecValue<String> candidateDiff = new SpecValue<>("50V", SpecUnit.NONE);

            assertEquals(1.0, rule.compare(original, candidateMatch));
            assertEquals(0.0, rule.compare(original, candidateDiff));
        }
    }

    @Nested
    @DisplayName("isAcceptable() Default Behavior")
    class IsAcceptableTests {

        @Test
        void shouldAcceptScoresAboveSeventyPercent() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);

            // Score = 1.0 (within tolerance)
            SpecValue<Double> candidate1 = new SpecValue<>(104.0, SpecUnit.NONE);
            assertTrue(rule.isAcceptable(original, candidate1));

            // Score should be ~0.5 (at 7.5% deviation)
            SpecValue<Double> candidate2 = new SpecValue<>(107.5, SpecUnit.NONE);
            assertFalse(rule.isAcceptable(original, candidate2));
        }

        @Test
        void shouldRejectScoresBelowSeventyPercent() {
            ToleranceRule rule = ToleranceRule.percentageTolerance(5.0);
            SpecValue<Double> original = new SpecValue<>(100.0, SpecUnit.NONE);
            SpecValue<Double> candidate = new SpecValue<>(111.0, SpecUnit.NONE); // Score = 0.0

            assertFalse(rule.isAcceptable(original, candidate));
        }
    }
}
