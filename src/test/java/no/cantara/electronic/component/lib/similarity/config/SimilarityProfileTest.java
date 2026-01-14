package no.cantara.electronic.component.lib.similarity.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SimilarityProfile enum behavior.
 * Validates multipliers, effective weights, and threshold logic for each profile.
 */
class SimilarityProfileTest {

    @Nested
    @DisplayName("DESIGN_PHASE Profile Tests")
    class DesignPhaseTests {

        private final SimilarityProfile profile = SimilarityProfile.DESIGN_PHASE;

        @Test
        void shouldHaveHighThreshold() {
            assertEquals(0.85, profile.getMinimumScore());
        }

        @Test
        void shouldApplyStrictMultipliers() {
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL));
            assertEquals(0.9, profile.getMultiplier(SpecImportance.HIGH));
            assertEquals(0.7, profile.getMultiplier(SpecImportance.MEDIUM));
            assertEquals(0.4, profile.getMultiplier(SpecImportance.LOW));
            assertEquals(0.0, profile.getMultiplier(SpecImportance.OPTIONAL));
        }

        @Test
        void shouldCalculateEffectiveWeights() {
            // CRITICAL: 1.0 * 1.0 = 1.0
            assertEquals(1.0, profile.getEffectiveWeight(SpecImportance.CRITICAL));

            // HIGH: 0.7 * 0.9 = 0.63
            assertEquals(0.63, profile.getEffectiveWeight(SpecImportance.HIGH), 0.01);

            // MEDIUM: 0.4 * 0.7 = 0.28
            assertEquals(0.28, profile.getEffectiveWeight(SpecImportance.MEDIUM), 0.01);

            // LOW: 0.2 * 0.4 = 0.08
            assertEquals(0.08, profile.getEffectiveWeight(SpecImportance.LOW), 0.01);

            // OPTIONAL: 0.0 * 0.0 = 0.0
            assertEquals(0.0, profile.getEffectiveWeight(SpecImportance.OPTIONAL));
        }

        @Test
        void shouldAcceptScoresAboveThreshold() {
            assertTrue(profile.meetsThreshold(0.90));
            assertTrue(profile.meetsThreshold(0.85));
        }

        @Test
        void shouldRejectScoresBelowThreshold() {
            assertFalse(profile.meetsThreshold(0.84));
            assertFalse(profile.meetsThreshold(0.70));
        }

        @Test
        void shouldHaveDescriptiveLabel() {
            assertNotNull(profile.getDescription());
            assertTrue(profile.getDescription().toLowerCase().contains("exact"));
        }
    }

    @Nested
    @DisplayName("REPLACEMENT Profile Tests")
    class ReplacementTests {

        private final SimilarityProfile profile = SimilarityProfile.REPLACEMENT;

        @Test
        void shouldHaveModerateThreshold() {
            assertEquals(0.75, profile.getMinimumScore());
        }

        @Test
        void shouldApplyBalancedMultipliers() {
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL));
            assertEquals(0.7, profile.getMultiplier(SpecImportance.HIGH));
            assertEquals(0.4, profile.getMultiplier(SpecImportance.MEDIUM));
            assertEquals(0.2, profile.getMultiplier(SpecImportance.LOW));
            assertEquals(0.0, profile.getMultiplier(SpecImportance.OPTIONAL));
        }

        @Test
        void shouldAcceptScoresAboveThreshold() {
            assertTrue(profile.meetsThreshold(0.80));
            assertTrue(profile.meetsThreshold(0.75));
        }

        @Test
        void shouldRejectScoresBelowThreshold() {
            assertFalse(profile.meetsThreshold(0.74));
            assertFalse(profile.meetsThreshold(0.60));
        }
    }

    @Nested
    @DisplayName("COST_OPTIMIZATION Profile Tests")
    class CostOptimizationTests {

        private final SimilarityProfile profile = SimilarityProfile.COST_OPTIMIZATION;

        @Test
        void shouldHaveLowThreshold() {
            assertEquals(0.60, profile.getMinimumScore());
        }

        @Test
        void shouldApplyRelaxedMultipliers() {
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL));
            assertEquals(0.4, profile.getMultiplier(SpecImportance.HIGH));
            assertEquals(0.2, profile.getMultiplier(SpecImportance.MEDIUM));
            assertEquals(0.0, profile.getMultiplier(SpecImportance.LOW));
            assertEquals(0.0, profile.getMultiplier(SpecImportance.OPTIONAL));
        }

        @Test
        void shouldMaintainCriticalSpecs() {
            // Cost optimization still requires CRITICAL specs to match
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL));
        }

        @Test
        void shouldAcceptWiderRangeOfScores() {
            assertTrue(profile.meetsThreshold(0.65));
            assertTrue(profile.meetsThreshold(0.60));
        }

        @Test
        void shouldRejectVeryLowScores() {
            assertFalse(profile.meetsThreshold(0.59));
            assertFalse(profile.meetsThreshold(0.50));
        }
    }

    @Nested
    @DisplayName("PERFORMANCE_UPGRADE Profile Tests")
    class PerformanceUpgradeTests {

        private final SimilarityProfile profile = SimilarityProfile.PERFORMANCE_UPGRADE;

        @Test
        void shouldHaveModerateThreshold() {
            assertEquals(0.70, profile.getMinimumScore());
        }

        @Test
        void shouldPrioritizePerformanceSpecs() {
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL));
            assertEquals(0.8, profile.getMultiplier(SpecImportance.HIGH));
            assertEquals(0.5, profile.getMultiplier(SpecImportance.MEDIUM));
            assertEquals(0.2, profile.getMultiplier(SpecImportance.LOW));
            assertEquals(0.0, profile.getMultiplier(SpecImportance.OPTIONAL));
        }

        @Test
        void shouldMaintainCriticalSpecMatch() {
            // CRITICAL specs still must match (1.0)
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL));
        }

        @Test
        void shouldAcceptScoresAboveThreshold() {
            assertTrue(profile.meetsThreshold(0.75));
            assertTrue(profile.meetsThreshold(0.70));
        }

        @Test
        void shouldRejectScoresBelowThreshold() {
            assertFalse(profile.meetsThreshold(0.69));
            assertFalse(profile.meetsThreshold(0.60));
        }
    }

    @Nested
    @DisplayName("EMERGENCY_SOURCING Profile Tests")
    class EmergencySourcingTests {

        private final SimilarityProfile profile = SimilarityProfile.EMERGENCY_SOURCING;

        @Test
        void shouldHaveVeryLowThreshold() {
            assertEquals(0.50, profile.getMinimumScore());
        }

        @Test
        void shouldApplyVeryRelaxedMultipliers() {
            assertEquals(0.8, profile.getMultiplier(SpecImportance.CRITICAL));
            assertEquals(0.4, profile.getMultiplier(SpecImportance.HIGH));
            assertEquals(0.2, profile.getMultiplier(SpecImportance.MEDIUM));
            assertEquals(0.0, profile.getMultiplier(SpecImportance.LOW));
            assertEquals(0.0, profile.getMultiplier(SpecImportance.OPTIONAL));
        }

        @Test
        void shouldSignificantlyRelaxCriticalSpecs() {
            // CRITICAL is only 0.8 - accepting significant variation
            assertEquals(0.8, profile.getMultiplier(SpecImportance.CRITICAL));
        }

        @Test
        void shouldAcceptWideRangeOfScores() {
            assertTrue(profile.meetsThreshold(0.55));
            assertTrue(profile.meetsThreshold(0.50));
        }

        @Test
        void shouldOnlyRejectVeryPoorMatches() {
            assertFalse(profile.meetsThreshold(0.49));
            assertFalse(profile.meetsThreshold(0.30));
        }
    }

    @Nested
    @DisplayName("Profile Ordering Tests")
    class ProfileOrderingTests {

        @Test
        void shouldOrderProfilesByStrictness() {
            // DESIGN_PHASE most strict, EMERGENCY_SOURCING most lenient
            assertTrue(SimilarityProfile.DESIGN_PHASE.getMinimumScore() >
                    SimilarityProfile.REPLACEMENT.getMinimumScore());

            assertTrue(SimilarityProfile.REPLACEMENT.getMinimumScore() >
                    SimilarityProfile.PERFORMANCE_UPGRADE.getMinimumScore());

            assertTrue(SimilarityProfile.PERFORMANCE_UPGRADE.getMinimumScore() >
                    SimilarityProfile.COST_OPTIMIZATION.getMinimumScore());

            assertTrue(SimilarityProfile.COST_OPTIMIZATION.getMinimumScore() >
                    SimilarityProfile.EMERGENCY_SOURCING.getMinimumScore());
        }

        @Test
        void shouldAllProvideMeaningfulDescriptions() {
            for (SimilarityProfile profile : SimilarityProfile.values()) {
                assertNotNull(profile.getDescription());
                assertFalse(profile.getDescription().isEmpty());
            }
        }

        @Test
        void shouldAllHandleAllImportanceLevels() {
            for (SimilarityProfile profile : SimilarityProfile.values()) {
                for (SpecImportance importance : SpecImportance.values()) {
                    // Should not throw
                    double multiplier = profile.getMultiplier(importance);
                    assertTrue(multiplier >= 0.0 && multiplier <= 1.0,
                            profile + " returned invalid multiplier " + multiplier + " for " + importance);
                }
            }
        }
    }

    @Nested
    @DisplayName("Effective Weight Calculation Tests")
    class EffectiveWeightTests {

        @Test
        void shouldMultiplyBaseWeightByProfileMultiplier() {
            SimilarityProfile profile = SimilarityProfile.REPLACEMENT;

            // CRITICAL: base=1.0, multiplier=1.0 → 1.0
            assertEquals(1.0, profile.getEffectiveWeight(SpecImportance.CRITICAL));

            // HIGH: base=0.7, multiplier=0.7 → 0.49
            assertEquals(0.49, profile.getEffectiveWeight(SpecImportance.HIGH), 0.01);

            // MEDIUM: base=0.4, multiplier=0.4 → 0.16
            assertEquals(0.16, profile.getEffectiveWeight(SpecImportance.MEDIUM), 0.01);
        }

        @Test
        void shouldReturnZeroForOptionalSpecs() {
            for (SimilarityProfile profile : SimilarityProfile.values()) {
                assertEquals(0.0, profile.getEffectiveWeight(SpecImportance.OPTIONAL));
            }
        }

        @Test
        void shouldProduceHigherWeightsForStricterProfiles() {
            SpecImportance importance = SpecImportance.HIGH;

            double designWeight = SimilarityProfile.DESIGN_PHASE.getEffectiveWeight(importance);
            double emergencyWeight = SimilarityProfile.EMERGENCY_SOURCING.getEffectiveWeight(importance);

            assertTrue(designWeight > emergencyWeight,
                    "DESIGN_PHASE should have higher effective weight than EMERGENCY_SOURCING");
        }
    }

    @Nested
    @DisplayName("Real-World Scenario Tests")
    class RealWorldScenarioTests {

        @Test
        void designPhaseRejectsMarginallySimilarParts() {
            // In design phase, a 0.80 similarity (good but not great) should be rejected
            SimilarityProfile profile = SimilarityProfile.DESIGN_PHASE;
            assertFalse(profile.meetsThreshold(0.80),
                    "Design phase should reject 0.80 similarity (requires 0.85+)");
        }

        @Test
        void emergencySourcingAcceptsMarginallySimilarParts() {
            // In emergency, a 0.55 similarity (marginal) should be accepted
            SimilarityProfile profile = SimilarityProfile.EMERGENCY_SOURCING;
            assertTrue(profile.meetsThreshold(0.55),
                    "Emergency sourcing should accept 0.55 similarity (requires 0.50+)");
        }

        @Test
        void performanceUpgradeWeightsPerformanceSpecs() {
            SimilarityProfile profile = SimilarityProfile.PERFORMANCE_UPGRADE;

            // HIGH specs have strong weight (0.8) for performance matching
            double highMultiplier = profile.getMultiplier(SpecImportance.HIGH);
            assertEquals(0.8, highMultiplier, "Performance upgrade should give high weight to HIGH specs");
        }

        @Test
        void costOptimizationMaintainsCriticalSpecs() {
            SimilarityProfile profile = SimilarityProfile.COST_OPTIMIZATION;

            // CRITICAL multiplier = 1.0, safety specs are maintained
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL),
                    "Cost optimization should maintain CRITICAL spec requirements");
        }

        @Test
        void replacementBalancesCriticalAndPractical() {
            SimilarityProfile profile = SimilarityProfile.REPLACEMENT;

            // CRITICAL must match exactly
            assertEquals(1.0, profile.getMultiplier(SpecImportance.CRITICAL));

            // But threshold is moderate (0.75, not 0.85)
            assertEquals(0.75, profile.getMinimumScore());
        }
    }
}
