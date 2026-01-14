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

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * Similarity calculator for logic IC components (74xx, CD4000 series).
 *
 * Uses function matching, technology compatibility, and series identification.
 * Metadata infrastructure available for spec-based comparison when characteristics are known.
 */
public class LogicICSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(LogicICSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;
    private static final Pattern TTL_PATTERN = Pattern.compile("([0-9]+)([A-Z]+)([0-9]+).*");
    private static final Pattern CMOS_PATTERN = Pattern.compile("CD([0-9]+)([A-Z]*).*");

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.5;
    private static final double LOW_SIMILARITY = 0.3;

    public LogicICSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    // Common 74xx series function groups
    private static final Map<String, Set<String>> FUNCTION_GROUPS = new HashMap<>();
    static {
        // NAND Gates
        FUNCTION_GROUPS.put("NAND", Set.of("00", "10", "20", "30", "40"));
        // NOR Gates
        FUNCTION_GROUPS.put("NOR", Set.of("02", "12", "22", "32", "42"));
        // NOT/Inverters
        FUNCTION_GROUPS.put("NOT", Set.of("04", "14", "24", "34", "44"));
        // AND Gates
        FUNCTION_GROUPS.put("AND", Set.of("08", "18", "28", "38", "48"));
        // OR Gates
        FUNCTION_GROUPS.put("OR", Set.of("32", "42"));
        // Flip-Flops
        FUNCTION_GROUPS.put("FF", Set.of("73", "74", "75", "76", "77", "78"));
        // Multiplexers
        FUNCTION_GROUPS.put("MUX", Set.of("151", "153", "157", "158"));
        // Decoders
        FUNCTION_GROUPS.put("DECODER", Set.of("138", "139", "154", "155"));
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) {
            logger.trace("LogicICSimilarityCalculator: type is null");
            return false;
        }

        logger.trace("LogicICSimilarityCalculator checking applicability for type: {}", type);

        // Check if it's an IC and if the component being checked is a logic IC
        if (type == ComponentType.IC) {
            logger.trace("LogicICSimilarityCalculator: Handling generic IC type");
            return true;
        }

        boolean result = type == ComponentType.LOGIC_IC ||
                type == ComponentType.LOGIC_IC_NEXPERIA ||
                type == ComponentType.LOGIC_IC_DIODES;

        logger.trace("LogicICSimilarityCalculator: Is applicable for {}? {}", type, result);
        return result;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        logger.debug("Comparing logic ICs: {} vs {}", mpn1, mpn2);

        // Try metadata-driven approach first
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.LOGIC_IC);
        if (metadataOpt.isPresent()) {
            logger.trace("Using metadata-driven similarity calculation for logic ICs");
            return calculateMetadataDrivenSimilarity(mpn1, mpn2, metadataOpt.get());
        }

        // Fallback to function/technology matching approach
        logger.trace("No metadata found for LOGIC_IC, using function/technology matching approach");
        return calculateFunctionBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity using metadata-driven approach with weighted spec comparison.
     */
    private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
        SimilarityProfile profile = metadata.getDefaultProfile();

        // Extract specs from both MPNs
        String function1 = extractFunction(mpn1);
        String function2 = extractFunction(mpn2);
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        String technology1 = extractTechnology(mpn1);
        String technology2 = extractTechnology(mpn2);
        String package1 = extractPackageCode(mpn1);
        String package2 = extractPackageCode(mpn2);

        // Short-circuit check for CRITICAL incompatibility - different functions
        if (!function1.isEmpty() && !function2.isEmpty() && !areSameFunction(mpn1, mpn2)) {
            logger.debug("Different logic IC functions: {} vs {} - returning LOW_SIMILARITY", function1, function2);
            return LOW_SIMILARITY;
        }

        // Short-circuit check for series incompatibility (74xx vs CD4000)
        if (!series1.isEmpty() && !series2.isEmpty() && !series1.equals(series2)) {
            logger.debug("Different logic IC series: {} vs {} - returning 0.0", series1, series2);
            return 0.0;
        }

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // Compare function (CRITICAL)
        ComponentTypeMetadata.SpecConfig functionConfig = metadata.getSpecConfig("function");
        if (functionConfig != null && !function1.isEmpty() && !function2.isEmpty()) {
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

        // Compare series (HIGH)
        ComponentTypeMetadata.SpecConfig seriesConfig = metadata.getSpecConfig("series");
        if (seriesConfig != null && !series1.isEmpty() && !series2.isEmpty()) {
            ToleranceRule rule = seriesConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(series1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(series2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(seriesConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Series comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare technology (MEDIUM)
        ComponentTypeMetadata.SpecConfig technologyConfig = metadata.getSpecConfig("technology");
        if (technologyConfig != null && !technology1.isEmpty() && !technology2.isEmpty()) {
            ToleranceRule rule = technologyConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(technology1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(technology2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(technologyConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Technology comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare package (LOW)
        ComponentTypeMetadata.SpecConfig packageConfig = metadata.getSpecConfig("package");
        if (packageConfig != null && !package1.isEmpty() && !package2.isEmpty()) {
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

        // voltageRange is not extracted currently, so skip it

        double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

        // Boost for same base part
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);
        if (base1.equals(base2)) {
            similarity = Math.max(similarity, HIGH_SIMILARITY);
            logger.debug("Boosted similarity to {} for same base part", HIGH_SIMILARITY);
        }

        return similarity;
    }

    /**
     * Extract series (74xx or CD4000) from MPN.
     */
    private String extractSeries(String mpn) {
        if (mpn == null) return "";
        if (mpn.matches("^CD4.*")) return "CD4000";
        if (mpn.matches("^74.*") || mpn.matches("^[0-9]{2,3}[A-Z]+[0-9]+.*")) return "74xx";
        return "";
    }

    /**
     * Extract package code from MPN.
     */
    private String extractPackageCode(String mpn) {
        if (mpn == null) return "";

        // For CD4000 series, extract suffix (BE, BM, UBE, etc.)
        if (mpn.matches("^CD4.*")) {
            Matcher m = Pattern.compile("CD4[0-9]+([A-Z]+)$").matcher(mpn);
            if (m.matches()) {
                return m.group(1);
            }
        }

        // For 74xx series, extract trailing letters
        Matcher m = Pattern.compile(".*([A-Z]{1,2})$").matcher(mpn);
        if (m.matches()) {
            return m.group(1);
        }

        return "";
    }

    /**
     * Calculate similarity based on function matching, series, and technology compatibility.
     * This is the fallback logic IC comparison method.
     */
    private double calculateFunctionBasedSimilarity(String mpn1, String mpn2) {

        // CD4000 series comparison
        if (mpn1.matches("^CD4.*") || mpn2.matches("^CD4.*")) {
            Matcher m1 = CMOS_PATTERN.matcher(mpn1);
            Matcher m2 = CMOS_PATTERN.matcher(mpn2);

            if (m1.matches() && m2.matches()) {
                String base1 = m1.group(1);  // The numbers after CD4
                String base2 = m2.group(1);

                logger.trace("CD4000 series bases: {} vs {}", base1, base2);

                // Same base number means same function
                if (base1.equals(base2)) {
                    return HIGH_SIMILARITY;  // High similarity for same function
                }
            }
            return LOW_SIMILARITY;
        }

        // Existing 74xx series handling
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        logger.trace("Base parts: {} and {}", base1, base2);

        // Different functions should have low similarity
        if (!areSameFunction(base1, base2)) {
            return LOW_SIMILARITY;
        }

        // Same base part with different package
        if (base1.equals(base2)) {
            return HIGH_SIMILARITY;
        }

        // Same series with compatible technology
        if (areCompatibleTechnologies(mpn1, mpn2)) {
            return HIGH_SIMILARITY;
        }

        return LOW_SIMILARITY;
    }

    private double compareCD4000Series(String mpn1, String mpn2) {
        logger.trace("Comparing CD4000 series: {} vs {}", mpn1, mpn2);

        // Extract base part (e.g., CD4001 from CD4001BE)
        String base1 = mpn1.replaceAll("(?:BE|BM|UBE|N|P|DG|PW|DR|DGK|DBV|DRG4)$", "");
        String base2 = mpn2.replaceAll("(?:BE|BM|UBE|N|P|DG|PW|DR|DGK|DBV|DRG4)$", "");

        logger.trace("Base parts: {} vs {}", base1, base2);

        if (base1.equals(base2)) {
            logger.debug("Same CD4000 series IC with different package codes");
            return 0.9;
        }

        return 0.3;
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";
        // Remove package codes
        return mpn.replaceAll("([A-Z]{1,2})$", "");
    }

    private boolean areSameFunction(String base1, String base2) {
        String func1 = extractFunction(base1);
        String func2 = extractFunction(base2);

        if (func1.equals(func2)) return true;

        // Check function groups
        for (Set<String> group : FUNCTION_GROUPS.values()) {
            if (group.contains(func1) && group.contains(func2)) {
                return true;
            }
        }

        return false;
    }

    private String extractFunction(String mpn) {
        if (mpn == null) return "";

        // Handle CD4000 series (e.g., CD4001 → "001", CD4011 → "011")
        if (mpn.matches("^CD4.*")) {
            Matcher m = CMOS_PATTERN.matcher(mpn);
            if (m.matches()) {
                return m.group(1);  // The digits after CD4
            }
        }

        // Handle 74xx series (e.g., 74LS00 → "00", 74HC138 → "138")
        Matcher matcher = TTL_PATTERN.matcher(mpn);
        if (matcher.matches()) {
            return matcher.group(3);  // The function number after technology code
        }
        return "";
    }

    private boolean areCompatibleTechnologies(String mpn1, String mpn2) {
        String tech1 = extractTechnology(mpn1);
        String tech2 = extractTechnology(mpn2);

        if (tech1.equals(tech2)) return true;

        // Compatible 74-series technologies
        Set<String> compatibleTTL = Set.of("LS", "ALS", "F", "HC", "HCT");
        return compatibleTTL.contains(tech1) && compatibleTTL.contains(tech2);
    }

    private String extractTechnology(String mpn) {
        Matcher matcher = TTL_PATTERN.matcher(mpn);
        if (matcher.matches()) {
            return matcher.group(2);  // The letters between numbers (LS, HC, etc.)
        }
        return "";
    }
}