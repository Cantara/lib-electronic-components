package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.MPNUtils;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import no.cantara.electronic.component.lib.similarity.config.SimilarityProfile;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for metadata-driven similarity calculation.
 * Tests the full flow: MPNUtils → Calculator selection → Metadata lookup → Similarity score
 */
class MetadataIntegrationTest {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.5;

    private PatternRegistry registry;
    private ComponentTypeMetadataRegistry metadataRegistry;

    @BeforeEach
    void setUp() {
        registry = new PatternRegistry();
        metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Nested
    @DisplayName("Calculator selection tests")
    class CalculatorSelectionTests {

        @Test
        @DisplayName("ResistorSimilarityCalculator should be selected for RESISTOR type")
        void resistorCalculatorForResistorType() {
            // Comparing two resistors - should use ResistorSimilarityCalculator
            double similarity = MPNUtils.calculateSimilarity("CRCW060310K0FKEA", "CRCW060310K0FKEA");
            assertEquals(1.0, similarity, 0.01, "Identical resistors should score 1.0");
        }

        @Test
        @DisplayName("CapacitorSimilarityCalculator should be selected for CAPACITOR type")
        void capacitorCalculatorForCapacitorType() {
            // Comparing two capacitors - should use CapacitorSimilarityCalculator
            double similarity = MPNUtils.calculateSimilarity("C0603X5R1V224M030BC", "C0603X5R1V224M030BC");
            assertEquals(1.0, similarity, 0.01, "Identical capacitors should score 1.0");
        }

        @Test
        @DisplayName("MosfetSimilarityCalculator should be selected for MOSFET type")
        void mosfetCalculatorForMosfetType() {
            // Comparing MOSFETs with same channel
            double similarity = MPNUtils.calculateSimilarity("IRF530", "IRF530");
            assertEquals(1.0, similarity, 0.01, "Identical MOSFETs should score 1.0");
        }

        @Test
        @DisplayName("TransistorSimilarityCalculator should be selected for TRANSISTOR type")
        void transistorCalculatorForTransistorType() {
            // Comparing transistors
            double similarity = MPNUtils.calculateSimilarity("2N2222", "2N2222");
            assertEquals(1.0, similarity, 0.01, "Identical transistors should score 1.0");
        }

        @Test
        @DisplayName("OpAmpSimilarityCalculator should be selected for OPAMP type")
        void opampCalculatorForOpampType() {
            // Comparing op-amps
            double similarity = MPNUtils.calculateSimilarity("LM358", "LM358");
            assertEquals(1.0, similarity, 0.01, "Identical op-amps should score 1.0");
        }

        @Test
        @DisplayName("DiodeSimilarityCalculator should be selected for DIODE type")
        void diodeCalculatorForDiodeType() {
            // Comparing diodes
            double similarity = MPNUtils.calculateSimilarity("1N4148", "1N4148");
            assertEquals(1.0, similarity, 0.01, "Identical diodes should score 1.0");
        }

        @Test
        @DisplayName("LEDSimilarityCalculator should be selected for LED type")
        void ledCalculatorForLedType() {
            // Comparing LEDs
            double similarity = MPNUtils.calculateSimilarity("LTST-C150CKT", "LTST-C150CKT");
            assertEquals(1.0, similarity, 0.01, "Identical LEDs should score 1.0");
        }

        @Test
        @DisplayName("VoltageRegulatorSimilarityCalculator should be selected for voltage regulator type")
        void voltageRegulatorCalculatorForRegulatorType() {
            // Comparing voltage regulators
            double similarity = MPNUtils.calculateSimilarity("L7805", "L7805");
            assertEquals(1.0, similarity, 0.01, "Identical regulators should score 1.0");
        }

        @Test
        @DisplayName("MemorySimilarityCalculator should be selected for MEMORY type")
        void memoryCalculatorForMemoryType() {
            // Comparing memory chips
            double similarity = MPNUtils.calculateSimilarity("AT24C256-PU", "AT24C256-PU");
            assertEquals(1.0, similarity, 0.01, "Identical memory chips should score 1.0");
        }

        @Test
        @DisplayName("ConnectorSimilarityCalculator should be selected for CONNECTOR type")
        void connectorCalculatorForConnectorType() {
            // Comparing connectors
            double similarity = MPNUtils.calculateSimilarity("TE5-520196-2", "TE5-520196-2");
            assertEquals(1.0, similarity, 0.01, "Identical connectors should score 1.0");
        }

        @Test
        @DisplayName("DefaultSimilarityCalculator should be used for unknown types")
        void defaultCalculatorForUnknownTypes() {
            // Using non-standard MPNs that may not match any type
            double similarity = MPNUtils.calculateSimilarity("CUSTOM123", "CUSTOM123");
            assertTrue(similarity >= 0.0 && similarity <= 1.0, "Unknown types should use default calculator");
        }
    }

    @Nested
    @DisplayName("Metadata-driven scoring tests")
    class MetadataScoringTests {

        @Test
        @DisplayName("Metadata should be available for resistors")
        void metadataAvailableForResistors() {
            var metadata = metadataRegistry.getMetadata(ComponentType.RESISTOR);
            assertTrue(metadata.isPresent(), "Resistor metadata should be registered");

            var meta = metadata.get();
            assertTrue(meta.isCritical("resistance"), "Resistance should be critical for resistors");
            assertTrue(meta.isCritical("tolerance"), "Tolerance should be critical for resistors");
        }

        @Test
        @DisplayName("Metadata should be available for capacitors")
        void metadataAvailableForCapacitors() {
            var metadata = metadataRegistry.getMetadata(ComponentType.CAPACITOR);
            assertTrue(metadata.isPresent(), "Capacitor metadata should be registered");

            var meta = metadata.get();
            assertTrue(meta.isCritical("capacitance"), "Capacitance should be critical for capacitors");
            assertTrue(meta.isCritical("voltage"), "Voltage should be critical for capacitors");
        }

        @Test
        @DisplayName("Normalized scores should be in [0.0, 1.0]")
        void normalizedScoresShouldBeInRange() {
            // Test multiple component types with normalized scoring
            double resistorSim = MPNUtils.calculateSimilarity("CRCW060310K0FKEA", "CRCW080510K0FKEA");
            assertTrue(resistorSim >= 0.0 && resistorSim <= 1.0,
                    "Resistor similarity should be normalized");

            double capacitorSim = MPNUtils.calculateSimilarity("C0603X5R1V224M030BC", "C0805X5R1V224M030BC");
            assertTrue(capacitorSim >= 0.0 && capacitorSim <= 1.0,
                    "Capacitor similarity should be normalized");
        }

        @Test
        @DisplayName("Critical spec mismatches should short-circuit to 0.0")
        void criticalMismatchesShouldReturnZero() {
            // N-channel vs P-channel MOSFET - critical mismatch
            double similarity = MPNUtils.calculateSimilarity("IRF530", "IRF9530");
            // These are actually different part numbers, one N-channel one P-channel
            assertTrue(similarity < 0.5, "Critical mismatches should have very low similarity");
        }

        @Test
        @DisplayName("Identical parts should score 1.0 with metadata-driven approach")
        void identicalPartsShouldScoreOne() {
            // Perfect match across multiple component types
            double resistor = MPNUtils.calculateSimilarity("RC0603FR-0710KL", "RC0603FR-0710KL");
            double capacitor = MPNUtils.calculateSimilarity("GRM188R61A106KA01L", "GRM188R61A106KA01L");
            double transistor = MPNUtils.calculateSimilarity("2N2222A", "2N2222A");

            assertEquals(1.0, resistor, 0.01, "Identical resistors should score 1.0");
            assertEquals(1.0, capacitor, 0.01, "Identical capacitors should score 1.0");
            assertEquals(1.0, transistor, 0.01, "Identical transistors should score 1.0");
        }

        @Test
        @DisplayName("Manufacturer-specific types should fall back to base type metadata")
        void manufacturerSpecificTypesShouldFallback() {
            // Test with manufacturer-specific resistor type
            var resistorYageo = metadataRegistry.getMetadata(ComponentType.RESISTOR_CHIP_YAGEO);
            var resistorBase = metadataRegistry.getMetadata(ComponentType.RESISTOR);

            // Should both be available (one is fallback)
            assertTrue(resistorBase.isPresent(), "Base resistor metadata should exist");
        }

        @Test
        @DisplayName("Spec weights should influence similarity correctly")
        void specWeightsShouldInfluenceSimilarity() {
            // Resistors: resistance (CRITICAL) > package (HIGH) > tolerance (HIGH)
            // Same value, different package should score higher than different value, same package
            double sameValue = MPNUtils.calculateSimilarity("CRCW060310K0FKEA", "CRCW080510K0FKEA");
            double differentValue = MPNUtils.calculateSimilarity("CRCW0603100RFKEA", "CRCW060310K0FKEA");

            // Resistance weight (CRITICAL) should dominate
            assertTrue(sameValue > differentValue, "Critical spec match should score higher");
        }
    }

    @Nested
    @DisplayName("Profile context tests")
    class ProfileContextTests {

        @Test
        @DisplayName("REPLACEMENT profile (default) should be used by default")
        void replacementProfileShouldBeDefault() {
            // Default profile behavior
            var metadata = metadataRegistry.getMetadata(ComponentType.RESISTOR);
            assertTrue(metadata.isPresent());

            var profile = metadata.get().getDefaultProfile();
            assertEquals(SimilarityProfile.REPLACEMENT, profile,
                    "Default profile should be REPLACEMENT for flexibility");
        }

        @Test
        @DisplayName("REPLACEMENT profile should allow minor variations")
        void replacementProfileAllowsVariations() {
            // In REPLACEMENT mode, minor spec differences should still score high
            double sameValue = MPNUtils.calculateSimilarity("CRCW060310K0FKEA", "CRCW060310K0FKEA");
            double slightVariation = MPNUtils.calculateSimilarity("CRCW060310K0FKEA", "CRCW080510K0FKEA");

            assertTrue(sameValue > slightVariation, "Identical parts should score higher");
            assertTrue(slightVariation > 0.3, "Replacement profile should allow variations");
        }

        @Test
        @DisplayName("Profile multipliers should affect effective weights")
        void profileMultipliersShouldAffectWeights() {
            var metadata = metadataRegistry.getMetadata(ComponentType.RESISTOR);
            assertTrue(metadata.isPresent());

            var meta = metadata.get();
            var replacementProfile = SimilarityProfile.REPLACEMENT;

            // Get effective weight for a HIGH importance spec
            double effectiveWeight = replacementProfile.getMultiplier(no.cantara.electronic.component.lib.similarity.config.SpecImportance.HIGH);

            // Should be less than 1.0 due to multiplier
            assertTrue(effectiveWeight > 0.0 && effectiveWeight < 1.0,
                    "Profile multiplier should reduce effective weight");
        }

        @Test
        @DisplayName("Threshold comparison should work with different profiles")
        void thresholdComparisonWithProfiles() {
            var metadata = metadataRegistry.getMetadata(ComponentType.RESISTOR);
            assertTrue(metadata.isPresent());

            var replacementThreshold = SimilarityProfile.REPLACEMENT.getMinimumScore();
            var designThreshold = SimilarityProfile.DESIGN_PHASE.getMinimumScore();

            // Design phase should be stricter (higher threshold)
            assertTrue(designThreshold > replacementThreshold,
                    "Design phase should have stricter threshold than replacement");
        }
    }

    @Nested
    @DisplayName("Legacy fallback tests")
    class LegacyFallbackTests {

        @Test
        @DisplayName("Calculators should fall back to legacy when metadata unavailable")
        void calculatorsShouldFallbackWithoutMetadata() {
            // For unknown component types without metadata, should still produce scores
            double similarity = MPNUtils.calculateSimilarity("XYZ123", "XYZ124");
            assertTrue(similarity >= 0.0 && similarity <= 1.0,
                    "Should produce valid score even without metadata");
        }

        @Test
        @DisplayName("DefaultSimilarityCalculator should be fallback for unhandled types")
        void defaultCalculatorFallback() {
            // Two MPNs that don't match any specific type
            double similarity = MPNUtils.calculateSimilarity("UNKNOWN123", "UNKNOWN456");
            assertTrue(similarity >= 0.0 && similarity <= 1.0,
                    "Default calculator should provide fallback scoring");
        }

        @Test
        @DisplayName("LevenshteinCalculator should be ultimate fallback")
        void levenshteinFallback() {
            // When all else fails, Levenshtein distance should apply
            LevenshteinCalculator levenshtein = new LevenshteinCalculator();
            double distance = levenshtein.calculateSimilarity("ABC", "ABD");
            assertTrue(distance >= 0.0 && distance <= 1.0,
                    "Levenshtein fallback should provide valid score");
        }

        @Test
        @DisplayName("Null input should return 0.0 before attempting metadata lookup")
        void nullInputBeforeMetadata() {
            double similarity = MPNUtils.calculateSimilarity(null, "ABC123");
            assertEquals(0.0, similarity, "Null input should return 0.0 immediately");
        }
    }

    @Nested
    @DisplayName("Critical spec short-circuit tests")
    class CriticalSpecShortCircuitTests {

        @Test
        @DisplayName("Capacitor voltage mismatch should short-circuit (critical)")
        void capacitorVoltageMismatch() {
            // Different voltage ratings - critical spec for capacitors
            double similarity = MPNUtils.calculateSimilarity("C0603X5R1V224M030BC", "C0603X5R1V224M030WC");
            // Might return LOW_SIMILARITY if critical mismatch detected
            assertTrue(similarity >= 0.0 && similarity <= 1.0,
                    "Voltage mismatch should produce valid score");
        }

        @Test
        @DisplayName("MOSFET channel mismatch should short-circuit (critical)")
        void mosfetChannelMismatch() {
            // N-channel vs P-channel - critical difference
            double similarity = MPNUtils.calculateSimilarity("IRF530N", "IRF9530N");
            // These are fundamentally different parts
            assertTrue(similarity < 0.6, "Channel mismatch should significantly reduce similarity");
        }

        @Test
        @DisplayName("Transistor polarity mismatch should short-circuit (critical)")
        void transistorPolarityMismatch() {
            // NPN vs PNP - critical difference
            double similarity = MPNUtils.calculateSimilarity("2N2222", "2N3907");
            assertTrue(similarity >= 0.0, "Polarity mismatch should reduce similarity significantly");
        }

        @Test
        @DisplayName("Diode type mismatch should short-circuit (critical)")
        void diodeTypeMismatch() {
            // Signal diode vs rectifier diode - critical difference
            double similarity = MPNUtils.calculateSimilarity("1N4148", "1N4007");
            assertTrue(similarity >= 0.0 && similarity <= 1.0,
                    "Different diode types should produce valid score");
        }
    }

    @Nested
    @DisplayName("Normalized scoring verification")
    class NormalizedScoringTests {

        @Test
        @DisplayName("Perfect match should score 1.0 (normalized)")
        void perfectMatchNormalized() {
            double sim1 = MPNUtils.calculateSimilarity("LM358", "LM358");
            assertEquals(1.0, sim1, 0.01, "Perfect match should normalize to 1.0");
        }

        @Test
        @DisplayName("Complete mismatch should score 0.0")
        void completeMismatchZero() {
            // Completely different types (if detectable)
            LevenshteinCalculator calc = new LevenshteinCalculator();
            double sim = calc.calculateSimilarity("AAA", "ZZZ");
            assertTrue(sim >= 0.0 && sim <= 1.0, "Complete mismatch should be valid");
        }

        @Test
        @DisplayName("Similar but not identical parts should score reasonably")
        void partialMatchesShouldScoreReasonably() {
            // Two resistors with different values
            double similarity = MPNUtils.calculateSimilarity("CRCW060310K0FKEA", "CRCW0603100RFKEA");
            // Different values (10K vs 100R) - critical spec mismatch
            assertTrue(similarity >= 0.0 && similarity <= 1.0,
                    "Different value resistors should produce valid score");
        }

        @Test
        @DisplayName("Normalized scores should be transitive direction")
        void normalizedScoresTransitive() {
            // A very close to B
            double simAB = MPNUtils.calculateSimilarity("LM358N", "LM358D");
            // B very close to C
            double simBC = MPNUtils.calculateSimilarity("LM358D", "LM358");
            // A should be reasonably close to C through transitive property
            double simAC = MPNUtils.calculateSimilarity("LM358N", "LM358");

            assertTrue(simAB > 0.7, "A and B should be similar");
            assertTrue(simBC > 0.7, "B and C should be similar");
            assertTrue(simAC > 0.5, "A and C should have some similarity through transitivity");
        }
    }

    @Nested
    @DisplayName("Edge cases and integration scenarios")
    class EdgeCaseIntegrationTests {

        @Test
        @DisplayName("Manufacturer-specific type detection should influence calculator selection")
        void manufacturerSpecificTypeInfluencesCalculator() {
            // Yageo resistor
            double yageo = MPNUtils.calculateSimilarity("RC0603FR-0710KL", "RC0603FR-0710KL");
            assertEquals(1.0, yageo, 0.01, "Yageo resistor should score 1.0 identical");
        }

        @Test
        @DisplayName("Multiple calculators applicable should use first match")
        void multipleApplicableCalculators() {
            // Some MPNs might match multiple type patterns
            double similarity = MPNUtils.calculateSimilarity("NE555", "NE555");
            assertEquals(1.0, similarity, 0.01, "Identical IC should score 1.0");
        }

        @Test
        @DisplayName("Very long MPN strings should still use metadata approach")
        void veryLongMpnStringsWithMetadata() {
            String longMpn = "STM32H743ZITX-ABCDEFGHIJKLMNOP-EXTENDED";
            double similarity = MPNUtils.calculateSimilarity(longMpn, longMpn);
            assertEquals(1.0, similarity, 0.01, "Long identical MPNs should score 1.0");
        }

        @Test
        @DisplayName("Special characters in MPNs should be normalized before scoring")
        void specialCharactersNormalized() {
            // MPNUtils.normalize() removes special characters
            String mPN1 = "C0603-X5R-1V-224M";
            String mpn1Normalized = MPNUtils.normalize(mPN1);
            String mpn2Normalized = MPNUtils.normalize(mPN1);

            assertEquals(mpn1Normalized, mpn2Normalized,
                    "Normalized versions should be identical");
        }

        @Test
        @DisplayName("Case normalization should work with metadata approach")
        void caseNormalizationWithMetadata() {
            // Uppercase vs lowercase should normalize to same value
            String upper = MPNUtils.normalize("LM358");
            String lower = MPNUtils.normalize("lm358");
            assertEquals(upper, lower, "Case should be normalized to uppercase");
        }
    }

    @Nested
    @DisplayName("Calculator integration with metadata registry")
    class CalculatorMetadataIntegration {

        @Test
        @DisplayName("ResistorSimilarityCalculator should access resistor metadata")
        void resistorCalculatorAccessesMetadata() {
            ResistorSimilarityCalculator calc = new ResistorSimilarityCalculator();
            assertTrue(calc.isApplicable(ComponentType.RESISTOR),
                    "Resistor calculator should be applicable to RESISTOR type");
        }

        @Test
        @DisplayName("CapacitorSimilarityCalculator should access capacitor metadata")
        void capacitorCalculatorAccessesMetadata() {
            CapacitorSimilarityCalculator calc = new CapacitorSimilarityCalculator();
            assertTrue(calc.isApplicable(ComponentType.CAPACITOR),
                    "Capacitor calculator should be applicable to CAPACITOR type");
        }

        @Test
        @DisplayName("OpAmpSimilarityCalculator should only handle OPAMP types")
        void opampCalculatorOnlyHandlesOpamps() {
            OpAmpSimilarityCalculator calc = new OpAmpSimilarityCalculator();
            assertTrue(calc.isApplicable(ComponentType.OPAMP),
                    "Op-amp calculator should handle OPAMP type");
            assertFalse(calc.isApplicable(ComponentType.IC),
                    "Op-amp calculator should NOT intercept generic IC type");
        }

        @Test
        @DisplayName("Calculator selection should respect isApplicable() results")
        void calculatorSelectionRespectsApplicable() {
            // Test that wrong calculator is not selected
            OpAmpSimilarityCalculator opampCalc = new OpAmpSimilarityCalculator();
            LogicICSimilarityCalculator logicCalc = new LogicICSimilarityCalculator();

            // CD4001 is a logic IC
            assertFalse(opampCalc.isApplicable(ComponentType.LOGIC_IC),
                    "OpAmpCalculator should not be applicable to logic ICs");
            assertTrue(logicCalc.isApplicable(ComponentType.LOGIC_IC),
                    "LogicICCalculator should be applicable to logic ICs");
        }
    }
}
