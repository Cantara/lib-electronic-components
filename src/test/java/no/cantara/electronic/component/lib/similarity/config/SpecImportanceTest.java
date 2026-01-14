package no.cantara.electronic.component.lib.similarity.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SpecImportance enum behavior and weight values.
 */
class SpecImportanceTest {

    @Nested
    @DisplayName("Base Weight Tests")
    class BaseWeightTests {

        @Test
        void criticalShouldHaveMaximumWeight() {
            assertEquals(1.0, SpecImportance.CRITICAL.getBaseWeight());
        }

        @Test
        void highShouldHaveHighWeight() {
            assertEquals(0.7, SpecImportance.HIGH.getBaseWeight());
        }

        @Test
        void mediumShouldHaveMediumWeight() {
            assertEquals(0.4, SpecImportance.MEDIUM.getBaseWeight());
        }

        @Test
        void lowShouldHaveLowWeight() {
            assertEquals(0.2, SpecImportance.LOW.getBaseWeight());
        }

        @Test
        void optionalShouldHaveZeroWeight() {
            assertEquals(0.0, SpecImportance.OPTIONAL.getBaseWeight());
        }

        @Test
        void weightsShouldDecreaseMonotonically() {
            assertTrue(SpecImportance.CRITICAL.getBaseWeight() >
                    SpecImportance.HIGH.getBaseWeight());
            assertTrue(SpecImportance.HIGH.getBaseWeight() >
                    SpecImportance.MEDIUM.getBaseWeight());
            assertTrue(SpecImportance.MEDIUM.getBaseWeight() >
                    SpecImportance.LOW.getBaseWeight());
            assertTrue(SpecImportance.LOW.getBaseWeight() >
                    SpecImportance.OPTIONAL.getBaseWeight());
        }

        @Test
        void allWeightsShouldBeInValidRange() {
            for (SpecImportance importance : SpecImportance.values()) {
                double weight = importance.getBaseWeight();
                assertTrue(weight >= 0.0 && weight <= 1.0,
                        importance + " has invalid weight: " + weight);
            }
        }
    }

    @Nested
    @DisplayName("Mandatory Flag Tests")
    class MandatoryFlagTests {

        @Test
        void criticalShouldBeMandatory() {
            assertTrue(SpecImportance.CRITICAL.isMandatory());
        }

        @Test
        void highShouldNotBeMandatory() {
            assertFalse(SpecImportance.HIGH.isMandatory());
        }

        @Test
        void mediumShouldNotBeMandatory() {
            assertFalse(SpecImportance.MEDIUM.isMandatory());
        }

        @Test
        void lowShouldNotBeMandatory() {
            assertFalse(SpecImportance.LOW.isMandatory());
        }

        @Test
        void optionalShouldNotBeMandatory() {
            assertFalse(SpecImportance.OPTIONAL.isMandatory());
        }

        @Test
        void onlyCriticalShouldBeMandatory() {
            int mandatoryCount = 0;
            for (SpecImportance importance : SpecImportance.values()) {
                if (importance.isMandatory()) {
                    mandatoryCount++;
                    assertEquals(SpecImportance.CRITICAL, importance);
                }
            }
            assertEquals(1, mandatoryCount, "Only CRITICAL should be mandatory");
        }
    }

    @Nested
    @DisplayName("Enum Completeness Tests")
    class EnumCompletenessTests {

        @Test
        void shouldHaveFiveLevels() {
            assertEquals(5, SpecImportance.values().length);
        }

        @Test
        void shouldContainAllExpectedLevels() {
            SpecImportance[] values = SpecImportance.values();

            boolean hasCritical = false;
            boolean hasHigh = false;
            boolean hasMedium = false;
            boolean hasLow = false;
            boolean hasOptional = false;

            for (SpecImportance importance : values) {
                switch (importance) {
                    case CRITICAL -> hasCritical = true;
                    case HIGH -> hasHigh = true;
                    case MEDIUM -> hasMedium = true;
                    case LOW -> hasLow = true;
                    case OPTIONAL -> hasOptional = true;
                }
            }

            assertTrue(hasCritical, "Missing CRITICAL");
            assertTrue(hasHigh, "Missing HIGH");
            assertTrue(hasMedium, "Missing MEDIUM");
            assertTrue(hasLow, "Missing LOW");
            assertTrue(hasOptional, "Missing OPTIONAL");
        }

        @Test
        void shouldSupportValueOfLookup() {
            assertEquals(SpecImportance.CRITICAL, SpecImportance.valueOf("CRITICAL"));
            assertEquals(SpecImportance.HIGH, SpecImportance.valueOf("HIGH"));
            assertEquals(SpecImportance.MEDIUM, SpecImportance.valueOf("MEDIUM"));
            assertEquals(SpecImportance.LOW, SpecImportance.valueOf("LOW"));
            assertEquals(SpecImportance.OPTIONAL, SpecImportance.valueOf("OPTIONAL"));
        }
    }

    @Nested
    @DisplayName("Semantic Meaning Tests")
    class SemanticMeaningTests {

        @Test
        void criticalSpecsAffectFunctionality() {
            // CRITICAL specs must match or the part won't function correctly
            // Examples: resistance value, capacitance, voltage rating, polarity
            assertTrue(SpecImportance.CRITICAL.isMandatory());
            assertEquals(1.0, SpecImportance.CRITICAL.getBaseWeight());
        }

        @Test
        void highSpecsAffectReliability() {
            // HIGH specs should match for reliable replacement
            // Examples: package type, tolerance, dielectric
            assertFalse(SpecImportance.HIGH.isMandatory());
            assertTrue(SpecImportance.HIGH.getBaseWeight() >= 0.7);
        }

        @Test
        void mediumSpecsAreImportantButNotCritical() {
            // MEDIUM specs affect performance but not core functionality
            // Examples: power rating, ESR, temperature coefficient
            assertFalse(SpecImportance.MEDIUM.isMandatory());
            assertTrue(SpecImportance.MEDIUM.getBaseWeight() >= 0.3);
        }

        @Test
        void lowSpecsAreNiceToHave() {
            // LOW specs are secondary considerations
            // Examples: gate charge, viewing angle, specific certifications
            assertFalse(SpecImportance.LOW.isMandatory());
            assertTrue(SpecImportance.LOW.getBaseWeight() > 0.0);
        }

        @Test
        void optionalSpecsAreInformational() {
            // OPTIONAL specs don't affect similarity scoring
            // Examples: lifecycle status, manufacturer notes, internal codes
            assertFalse(SpecImportance.OPTIONAL.isMandatory());
            assertEquals(0.0, SpecImportance.OPTIONAL.getBaseWeight());
        }
    }

    @Nested
    @DisplayName("Weight Distribution Tests")
    class WeightDistributionTests {

        @Test
        void criticalAndHighShouldCover70PercentOfWeight() {
            // CRITICAL (1.0) + HIGH (0.7) = 1.7
            // This is 70% of total importance space (1.0 + 0.7 + 0.4 + 0.2 = 2.3)
            double criticalAndHigh = SpecImportance.CRITICAL.getBaseWeight() +
                    SpecImportance.HIGH.getBaseWeight();

            double totalWeight = 0.0;
            for (SpecImportance importance : SpecImportance.values()) {
                totalWeight += importance.getBaseWeight();
            }

            double percentage = criticalAndHigh / totalWeight;
            assertTrue(percentage >= 0.70, "CRITICAL+HIGH should be ~70% of total weight");
        }

        @Test
        void weightsShouldSumToReasonableTotal() {
            double sum = 0.0;
            for (SpecImportance importance : SpecImportance.values()) {
                sum += importance.getBaseWeight();
            }

            // Should sum to 2.3 (1.0 + 0.7 + 0.4 + 0.2 + 0.0)
            assertEquals(2.3, sum, 0.01);
        }
    }

    @Nested
    @DisplayName("Real-World Usage Tests")
    class RealWorldUsageTests {

        @Test
        void resistorResistanceIsCritical() {
            // For a resistor, the resistance value is CRITICAL
            // Using wrong value = circuit doesn't work
            SpecImportance importance = SpecImportance.CRITICAL;

            assertTrue(importance.isMandatory());
            assertEquals(1.0, importance.getBaseWeight());
        }

        @Test
        void resistorPackageIsHigh() {
            // Package affects form/fit/function but not electrical behavior
            // Can sometimes substitute (0603 → 0805) if space allows
            SpecImportance importance = SpecImportance.HIGH;

            assertFalse(importance.isMandatory());
            assertEquals(0.7, importance.getBaseWeight());
        }

        @Test
        void mosfetChannelIsCritical() {
            // N-channel vs P-channel is absolutely critical
            // Wrong channel = circuit failure
            SpecImportance importance = SpecImportance.CRITICAL;

            assertTrue(importance.isMandatory());
        }

        @Test
        void mosfetGateChargeIsLow() {
            // Gate charge affects switching speed but not core function
            // Higher gate charge = slightly slower switching
            SpecImportance importance = SpecImportance.LOW;

            assertFalse(importance.isMandatory());
            assertEquals(0.2, importance.getBaseWeight());
        }

        @Test
        void capacitorDielectricIsCritical() {
            // X7R vs X5R vs C0G affects temperature stability
            // Wrong dielectric = unreliable circuit
            SpecImportance importance = SpecImportance.CRITICAL;

            assertTrue(importance.isMandatory());
        }

        @Test
        void connectorPinCountIsCritical() {
            // 10-pin connector cannot replace 8-pin connector
            // Physical mismatch
            SpecImportance importance = SpecImportance.CRITICAL;

            assertTrue(importance.isMandatory());
        }

        @Test
        void lifecycleStatusIsOptional() {
            // Lifecycle status doesn't affect similarity
            // It's for sourcing decisions, not technical equivalence
            SpecImportance importance = SpecImportance.OPTIONAL;

            assertFalse(importance.isMandatory());
            assertEquals(0.0, importance.getBaseWeight());
        }
    }
}
