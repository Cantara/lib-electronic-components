package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import no.cantara.electronic.component.lib.similarity.config.SimilarityProfile;
import no.cantara.electronic.component.lib.similarity.config.ToleranceRule;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Similarity calculator for operational amplifier (op-amp) components.
 *
 * Uses equivalent families (LM358≈MC1458, TL072≈TL082) and configuration matching.
 * Metadata infrastructure available for spec-based comparison when characteristics are known.
 */
public class OpAmpSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(OpAmpSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.5;


    // Map of equivalent op-amp families across manufacturers
    private static final Map<String, Set<String>> EQUIVALENT_FAMILIES = new HashMap<>();
    static {
        // Standard dual op-amps (including JFET and precision types)
        EQUIVALENT_FAMILIES.put("LM358", Set.of("MC1458", "LM1458", "RC4558", "TL072", "TL082", "NE5532"));
        EQUIVALENT_FAMILIES.put("TL072", Set.of("LM358", "MC1458", "TL082", "NE5532"));
        EQUIVALENT_FAMILIES.put("MC1458", Set.of("LM358", "LM1458", "RC4558", "TL072", "TL082"));
        EQUIVALENT_FAMILIES.put("NE5532", Set.of("LM358", "MC1458", "TL072", "TL082"));

        // Quad op-amps
        EQUIVALENT_FAMILIES.put("LM324", Set.of("MC3403", "RC4136", "TL074", "TL084"));
        EQUIVALENT_FAMILIES.put("TL074", Set.of("LM324", "MC3403", "TL084"));

        // Single op-amps
        EQUIVALENT_FAMILIES.put("LM741", Set.of("uA741", "TL071", "TL081"));
        EQUIVALENT_FAMILIES.put("TL071", Set.of("LM741", "uA741", "TL081"));
    }

    private static final Map<String, String> PACKAGE_TYPES = new HashMap<>();
    static {
        // DIP packages
        PACKAGE_TYPES.put("N", "DIP");
        PACKAGE_TYPES.put("P", "DIP");

        // Surface mount packages
        PACKAGE_TYPES.put("D", "SOIC");
        PACKAGE_TYPES.put("M", "SOIC");
        PACKAGE_TYPES.put("PW", "TSSOP");
        PACKAGE_TYPES.put("DGK", "MSOP");
        PACKAGE_TYPES.put("DBV", "SOT-23");
        PACKAGE_TYPES.put("DRL", "SOT-553");
    }

    // Op-amp characteristics
    private static final Map<String, OpAmpChars> CHARACTERISTICS = new HashMap<>();
    static {
        // Dual op-amps
        CHARACTERISTICS.put("LM358", new OpAmpChars(
                OpAmpChars.Function.DUAL,
                OpAmpChars.InputType.BIPOLAR,
                false,  // railToRail
                true,   // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("TL072", new OpAmpChars(
                OpAmpChars.Function.DUAL,
                OpAmpChars.InputType.JFET,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("MC1458", new OpAmpChars(
                OpAmpChars.Function.DUAL,
                OpAmpChars.InputType.BIPOLAR,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));

        // Quad op-amps
        CHARACTERISTICS.put("LM324", new OpAmpChars(
                OpAmpChars.Function.QUAD,
                OpAmpChars.InputType.BIPOLAR,
                false,  // railToRail
                true,   // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("TL074", new OpAmpChars(
                OpAmpChars.Function.QUAD,
                OpAmpChars.InputType.JFET,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));

        // Single op-amps
        CHARACTERISTICS.put("LM741", new OpAmpChars(
                OpAmpChars.Function.SINGLE,
                OpAmpChars.InputType.BIPOLAR,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("TL071", new OpAmpChars(
                OpAmpChars.Function.SINGLE,
                OpAmpChars.InputType.JFET,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));
    }

    public OpAmpSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        // Handle null type
        if (type == null) {
            return false;
        }

        // Only handle specific op-amp types to avoid intercepting other IC types
        // (e.g., logic ICs, memory ICs, etc.)
        return type == ComponentType.OPAMP ||
                type == ComponentType.OPAMP_TI ||
                type.name().startsWith("OPAMP_");
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        // Check if both MPNs match op-amp patterns
        if (!isOpAmp(mpn1) || !isOpAmp(mpn2)) {
            return 0.0;
        }

        logger.debug("Comparing op-amps: {} vs {}", mpn1, mpn2);

        // Try metadata-driven approach first
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.OPAMP);
        if (metadataOpt.isPresent()) {
            logger.trace("Using metadata-driven similarity calculation");
            return calculateMetadataDrivenSimilarity(mpn1, mpn2, metadataOpt.get());
        }

        // Fallback to equivalent family approach
        logger.trace("No metadata found for OPAMP, using equivalent family approach");
        return calculateEquivalentFamilyBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity using metadata-driven weighted spec comparison.
     * This is the preferred approach when metadata is available.
     */
    private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
        SimilarityProfile profile = metadata.getDefaultProfile();

        // Extract specs from both MPNs
        String function1 = extractFunction(mpn1);
        String function2 = extractFunction(mpn2);
        String inputType1 = extractInputType(mpn1);
        String inputType2 = extractInputType(mpn2);
        Boolean railToRail1 = extractRailToRail(mpn1);
        Boolean railToRail2 = extractRailToRail(mpn2);
        Boolean lowPower1 = extractLowPower(mpn1);
        Boolean lowPower2 = extractLowPower(mpn2);
        Boolean highSpeed1 = extractHighSpeed(mpn1);
        Boolean highSpeed2 = extractHighSpeed(mpn2);
        String package1 = extractPackage(mpn1);
        String package2 = extractPackage(mpn2);

        logger.trace("MPN1 specs: function={}, inputType={}, railToRail={}, lowPower={}, highSpeed={}, package={}",
                function1, inputType1, railToRail1, lowPower1, highSpeed1, package1);
        logger.trace("MPN2 specs: function={}, inputType={}, railToRail={}, lowPower={}, highSpeed={}, package={}",
                function2, inputType2, railToRail2, lowPower2, highSpeed2, package2);

        // Short-circuit check: Different function types (SINGLE vs DUAL vs QUAD) are incompatible
        if (function1 != null && function2 != null && !function1.equals(function2)) {
            logger.debug("Different function types: {} vs {} - returning 0.0", function1, function2);
            return 0.0;
        }

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // Compare function (CRITICAL)
        ComponentTypeMetadata.SpecConfig functionConfig = metadata.getSpecConfig("function");
        if (functionConfig != null && function1 != null && function2 != null) {
            ToleranceRule rule = functionConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(function1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(function2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(functionConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Function comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare inputType (HIGH)
        ComponentTypeMetadata.SpecConfig inputTypeConfig = metadata.getSpecConfig("inputType");
        if (inputTypeConfig != null && inputType1 != null && inputType2 != null) {
            ToleranceRule rule = inputTypeConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(inputType1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(inputType2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(inputTypeConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("InputType comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare railToRail (MEDIUM)
        ComponentTypeMetadata.SpecConfig railToRailConfig = metadata.getSpecConfig("railToRail");
        if (railToRailConfig != null && railToRail1 != null && railToRail2 != null) {
            ToleranceRule rule = railToRailConfig.getToleranceRule();
            SpecValue<Boolean> orig = new SpecValue<>(railToRail1, SpecUnit.NONE);
            SpecValue<Boolean> cand = new SpecValue<>(railToRail2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(railToRailConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("RailToRail comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare lowPower (LOW)
        ComponentTypeMetadata.SpecConfig lowPowerConfig = metadata.getSpecConfig("lowPower");
        if (lowPowerConfig != null && lowPower1 != null && lowPower2 != null) {
            ToleranceRule rule = lowPowerConfig.getToleranceRule();
            SpecValue<Boolean> orig = new SpecValue<>(lowPower1, SpecUnit.NONE);
            SpecValue<Boolean> cand = new SpecValue<>(lowPower2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(lowPowerConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("LowPower comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare highSpeed (LOW)
        ComponentTypeMetadata.SpecConfig highSpeedConfig = metadata.getSpecConfig("highSpeed");
        if (highSpeedConfig != null && highSpeed1 != null && highSpeed2 != null) {
            ToleranceRule rule = highSpeedConfig.getToleranceRule();
            SpecValue<Boolean> orig = new SpecValue<>(highSpeed1, SpecUnit.NONE);
            SpecValue<Boolean> cand = new SpecValue<>(highSpeed2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(highSpeedConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("HighSpeed comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare package (LOW)
        ComponentTypeMetadata.SpecConfig packageConfig = metadata.getSpecConfig("package");
        if (packageConfig != null && package1 != null && package2 != null) {
            ToleranceRule rule = packageConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(package1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(package2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(packageConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Package comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;
        logger.trace("Calculated metadata similarity: {} (totalScore={}, maxPossibleScore={})",
                similarity, totalScore, maxPossibleScore);

        // Boost for known equivalent families (e.g., LM358 ≈ MC1458)
        String family1 = extractFamily(extractBasePart(mpn1));
        String family2 = extractFamily(extractBasePart(mpn2));
        if (areEquivalentFamilies(family1, family2)) {
            similarity = Math.max(similarity, HIGH_SIMILARITY);
            logger.debug("Boosted similarity to {} for equivalent families: {} and {}", HIGH_SIMILARITY, family1, family2);
        }

        return similarity;
    }

    /**
     * Calculate similarity based on equivalent families and known characteristics.
     * This is the primary op-amp comparison method.
     */
    private double calculateEquivalentFamilyBasedSimilarity(String mpn1, String mpn2) {
        // Extract base part numbers (without package codes)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);
        logger.trace("Base part numbers: {} and {}", base1, base2);

        // Same exact part
        if (base1.equals(base2)) {
            String pkg1 = extractPackageCode(mpn1);
            String pkg2 = extractPackageCode(mpn2);
            if (areCompatiblePackages(pkg1, pkg2)) {
                logger.debug("Same part, compatible packages - similarity: {}", HIGH_SIMILARITY);
                return HIGH_SIMILARITY;
            }
        }

        // Extract families (LM358, MC1458, etc.)
        String family1 = extractFamily(base1);
        String family2 = extractFamily(base2);
        logger.trace("Families: {} and {}", family1, family2);

        // Check for equivalent families
        if (areEquivalentFamilies(family1, family2)) {
            logger.debug("Equivalent families with characteristics - similarity: {}", HIGH_SIMILARITY);
            return HIGH_SIMILARITY;
        }

        // Same function (dual, quad, etc.)
        if (haveSameFunction(family1, family2)) {
            logger.debug("Same function type - similarity: {}", MEDIUM_SIMILARITY);
            return MEDIUM_SIMILARITY;
        }

        logger.debug("Both are op-amps - similarity: {}", LOW_SIMILARITY);
        return LOW_SIMILARITY;
    }

    private boolean isOpAmp(String mpn) {
        if (mpn == null) return false;
        return mpn.matches("^(?:LM358|LM324|MC1458|LM1458|RC4558|TL0[7,8][1-4]|NE5532).*");
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";
        return mpn.replaceAll("(?:N|D|P|DG|PW|DR|DGK|DBV|DRG4)$", "");
    }

    private String extractFamily(String base) {
        if (base == null) return "";
        // Extract family code (e.g., "LM358" from "LM358A")
        return base.replaceAll("[A-Z]*$", "");
    }

    private boolean areEquivalentFamilies(String family1, String family2) {
        // Check if they're the same family
        if (family1.equals(family2)) {
            return true;
        }

        // Get the equivalent families from the static map
        Set<String> equivalents = EQUIVALENT_FAMILIES.get(family1);
        if (equivalents != null && equivalents.contains(family2)) {
            return true;
        }

        // General dual op-amp equivalents (including JFET input types)
        Set<String> dualOpAmps = Set.of("LM358", "MC1458", "LM1458", "RC4558", "TL072", "TL082", "NE5532");
        if (dualOpAmps.contains(family1) && dualOpAmps.contains(family2)) {
            return true;
        }

        // Quad op-amps (including JFET input types)
        Set<String> quadOpAmps = Set.of("LM324", "MC3403", "RC4136", "TL074", "TL084");
        if (quadOpAmps.contains(family1) && quadOpAmps.contains(family2)) {
            return true;
        }

        // Single op-amps (including JFET input types)
        Set<String> singleOpAmps = Set.of("LM741", "uA741", "TL071", "TL081");
        if (singleOpAmps.contains(family1) && singleOpAmps.contains(family2)) {
            return true;
        }

        return false;
    }

    private boolean haveSameFunction(String family1, String family2) {
        // Check if both are dual or both are quad op-amps
        boolean isDual1 = family1.matches(".*(?:358|072|082|5532).*");
        boolean isDual2 = family2.matches(".*(?:358|072|082|5532).*");
        boolean isQuad1 = family1.matches(".*(?:324|3403).*");
        boolean isQuad2 = family2.matches(".*(?:324|3403).*");

        return (isDual1 && isDual2) || (isQuad1 && isQuad2);
    }

    private String extractPackageCode(String mpn) {
        if (mpn == null) return "";
        Matcher m = Pattern.compile("(?:N|D|P|DG|PW|DR|DGK|DBV|DRG4)$").matcher(mpn);
        return m.find() ? m.group() : "";
    }

    private boolean areCompatiblePackages(String pkg1, String pkg2) {
        if (pkg1.equals(pkg2)) return true;

        // Known compatible package sets
        Set<String> smallPkgs = Set.of("N", "D", "P", "DG", "PW", "DR", "DGK", "DBV");
        return smallPkgs.contains(pkg1) && smallPkgs.contains(pkg2);
    }

    // ==================== Spec Extraction Methods ====================

    /**
     * Extract op-amp function type (SINGLE, DUAL, QUAD).
     * Uses CHARACTERISTICS map for known parts, falls back to pattern matching.
     */
    private String extractFunction(String mpn) {
        if (mpn == null) return null;

        String family = extractFamily(extractBasePart(mpn));
        OpAmpChars chars = CHARACTERISTICS.get(family);
        if (chars != null && chars.function != null) {
            return chars.function.name();
        }

        // Fallback: pattern-based detection from external utility class
        OpAmpCharacteristics.Function func = OpAmpCharacteristics.fromMPN(mpn);
        return func != null ? func.name() : null;
    }

    /**
     * Extract input type (BIPOLAR, JFET, CMOS).
     * Uses CHARACTERISTICS map for known parts.
     */
    private String extractInputType(String mpn) {
        if (mpn == null) return null;

        String family = extractFamily(extractBasePart(mpn));
        OpAmpChars chars = CHARACTERISTICS.get(family);
        if (chars != null && chars.inputType != null) {
            return chars.inputType.name();
        }

        // Default: assume BIPOLAR if not known
        return "BIPOLAR";
    }

    /**
     * Extract rail-to-rail capability.
     * Uses CHARACTERISTICS map for known parts.
     */
    private Boolean extractRailToRail(String mpn) {
        if (mpn == null) return null;

        String family = extractFamily(extractBasePart(mpn));
        OpAmpChars chars = CHARACTERISTICS.get(family);
        return chars != null ? chars.railToRail : null;
    }

    /**
     * Extract low-power indicator.
     * Uses CHARACTERISTICS map for known parts.
     */
    private Boolean extractLowPower(String mpn) {
        if (mpn == null) return null;

        String family = extractFamily(extractBasePart(mpn));
        OpAmpChars chars = CHARACTERISTICS.get(family);
        return chars != null ? chars.lowPower : null;
    }

    /**
     * Extract high-speed indicator.
     * Uses CHARACTERISTICS map for known parts.
     */
    private Boolean extractHighSpeed(String mpn) {
        if (mpn == null) return null;

        String family = extractFamily(extractBasePart(mpn));
        OpAmpChars chars = CHARACTERISTICS.get(family);
        return chars != null ? chars.highSpeed : null;
    }

    /**
     * Extract package code.
     */
    private String extractPackage(String mpn) {
        return extractPackageCode(mpn);
    }

    /**
     * Inner class to hold op-amp characteristics.
     */
    private static class OpAmpChars {
        final Function function;
        final InputType inputType;
        final boolean railToRail;
        final boolean lowPower;
        final boolean highSpeed;

        enum Function {
            SINGLE, DUAL, QUAD, TRIPLE
        }

        enum InputType {
            BIPOLAR, JFET, CMOS
        }

        OpAmpChars(Function function, InputType inputType, boolean railToRail,
                             boolean lowPower, boolean highSpeed) {
            this.function = function;
            this.inputType = inputType;
            this.railToRail = railToRail;
            this.lowPower = lowPower;
            this.highSpeed = highSpeed;
        }
    }

}