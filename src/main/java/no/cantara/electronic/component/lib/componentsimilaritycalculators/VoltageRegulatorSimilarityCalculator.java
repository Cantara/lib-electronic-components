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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Similarity calculator for voltage regulator components.
 *
 * Uses metadata-driven comparison based on extracted specs (regulatorType, outputVoltage, polarity, currentRating).
 * Fixed regulators (78xx/79xx) and adjustable regulators (LM317/LM337) are compared using their electrical
 * specifications with known equivalent families as boost.
 *
 * Fallback to pattern-based matching for unknown regulator types.
 */
public class VoltageRegulatorSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(VoltageRegulatorSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    public VoltageRegulatorSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        // Handle null type
        if (type == null) {
            return false;
        }

        return type == ComponentType.VOLTAGE_REGULATOR ||
                //type == ComponentType.VOLTAGE_REGULATOR_LINEAR_TI ||
                //type == ComponentType.VOLTAGE_REGULATOR_LINEAR_ON ||
                type.name().startsWith("VOLTAGE_REGULATOR");
    }


    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        // First check if both MPNs match voltage regulator patterns
        if (!isVoltageRegulator(mpn1) || !isVoltageRegulator(mpn2)) {
            return 0.0;
        }

        logger.debug("Comparing voltage regulators: {} vs {}", mpn1, mpn2);

        // Get metadata for spec-based comparison
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.VOLTAGE_REGULATOR);
        if (metadataOpt.isPresent()) {
            logger.trace("Using metadata-driven comparison for voltage regulators");
            return calculateMetadataDrivenSimilarity(mpn1, mpn2, metadataOpt.get());
        }

        // Fallback to pattern-based matching if metadata not available
        logger.trace("No metadata found for VOLTAGE_REGULATOR, using pattern-based fallback");
        return calculateRegulatorTypeBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity using metadata-driven weighted comparison.
     * Extracts specs from MPNs and uses tolerance rules for comparison.
     */
    private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
        SimilarityProfile profile = metadata.getDefaultProfile();

        logger.trace("Using profile: {}", profile);

        // Extract specs from both MPNs
        String regulatorType1 = extractRegulatorType(mpn1);
        String regulatorType2 = extractRegulatorType(mpn2);
        String outputVoltage1 = extractOutputVoltage(mpn1);
        String outputVoltage2 = extractOutputVoltage(mpn2);
        String polarity1 = extractPolarity(mpn1);
        String polarity2 = extractPolarity(mpn2);
        Integer currentRating1 = extractCurrentRating(mpn1);
        Integer currentRating2 = extractCurrentRating(mpn2);
        String package1 = extractPackage(mpn1);
        String package2 = extractPackage(mpn2);

        logger.trace("MPN1 specs: type={}, voltage={}, polarity={}, current={}A, package={}",
                regulatorType1, outputVoltage1, polarity1, currentRating1, package1);
        logger.trace("MPN2 specs: type={}, voltage={}, polarity={}, current={}A, package={}",
                regulatorType2, outputVoltage2, polarity2, currentRating2, package2);

        // Short-circuit: Different regulator types (fixed vs adjustable) are not interchangeable
        if (regulatorType1 != null && regulatorType2 != null && !regulatorType1.equals(regulatorType2)) {
            logger.debug("Different regulator types: {} vs {} -> LOW_SIMILARITY", regulatorType1, regulatorType2);
            return LOW_SIMILARITY;
        }

        // Short-circuit: Different polarity regulators are not interchangeable
        if (polarity1 != null && polarity2 != null && !polarity1.equals(polarity2)) {
            logger.debug("Different polarity: {} vs {} -> LOW_SIMILARITY", polarity1, polarity2);
            return LOW_SIMILARITY;
        }

        // Short-circuit: For fixed regulators, different output voltages are not interchangeable
        if ("fixed".equals(regulatorType1) && outputVoltage1 != null && outputVoltage2 != null &&
                !outputVoltage1.equals(outputVoltage2)) {
            logger.debug("Fixed regulators with different voltages: {} vs {} -> LOW_SIMILARITY",
                    outputVoltage1, outputVoltage2);
            return LOW_SIMILARITY;
        }

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // Compare regulatorType (CRITICAL)
        ComponentTypeMetadata.SpecConfig regulatorTypeConfig = metadata.getSpecConfig("regulatorType");
        if (regulatorTypeConfig != null && regulatorType1 != null && regulatorType2 != null) {
            ToleranceRule rule = regulatorTypeConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(regulatorType1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(regulatorType2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(regulatorTypeConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("RegulatorType comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare outputVoltage (CRITICAL)
        ComponentTypeMetadata.SpecConfig outputVoltageConfig = metadata.getSpecConfig("outputVoltage");
        if (outputVoltageConfig != null && outputVoltage1 != null && outputVoltage2 != null) {
            ToleranceRule rule = outputVoltageConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(outputVoltage1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(outputVoltage2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(outputVoltageConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("OutputVoltage comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare polarity (CRITICAL)
        ComponentTypeMetadata.SpecConfig polarityConfig = metadata.getSpecConfig("polarity");
        if (polarityConfig != null && polarity1 != null && polarity2 != null) {
            ToleranceRule rule = polarityConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(polarity1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(polarity2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(polarityConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Polarity comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare currentRating (HIGH)
        ComponentTypeMetadata.SpecConfig currentConfig = metadata.getSpecConfig("currentRating");
        if (currentConfig != null && currentRating1 != null && currentRating2 != null) {
            ToleranceRule rule = currentConfig.getToleranceRule();
            SpecValue<Integer> orig = new SpecValue<>(currentRating1, SpecUnit.MILLIAMPS);
            SpecValue<Integer> cand = new SpecValue<>(currentRating2, SpecUnit.MILLIAMPS);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(currentConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("CurrentRating comparison: score={}, weight={}, contribution={}",
                    specScore, specWeight, specScore * specWeight);
        }

        // Compare package (MEDIUM)
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

        // Normalize to [0.0, 1.0]
        double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

        // Check for known equivalent/compatible regulators and boost score if applicable
        if (areCompatibleRegulators(mpn1, mpn2)) {
            logger.trace("Known compatible regulators detected, boosting similarity");
            similarity = Math.max(similarity, HIGH_SIMILARITY);
        }

        logger.debug("Final similarity: {}", similarity);
        return similarity;
    }

    /**
     * Check if two regulators are known to be compatible (e.g., LM317/LM350/LM338 family).
     */
    private boolean areCompatibleRegulators(String mpn1, String mpn2) {
        // Same regulator with different packages
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);
        if (base1.equals(base2)) {
            return true;
        }

        // Compatible adjustable regulator families
        if (isAdjustableRegulator(mpn1) && isAdjustableRegulator(mpn2)) {
            return areCompatibleAdjustableRegulators(base1, base2);
        }

        // Compatible fixed regulators (same voltage, same polarity, different manufacturer)
        if (isFixedRegulator(mpn1) && isFixedRegulator(mpn2)) {
            String voltage1 = extractVoltage(mpn1);
            String voltage2 = extractVoltage(mpn2);
            boolean samePolarity = (mpn1.contains("78") && mpn2.contains("78")) ||
                    (mpn1.contains("79") && mpn2.contains("79"));
            return voltage1.equals(voltage2) && samePolarity;
        }

        return false;
    }

    /**
     * Calculate similarity based on regulator type (fixed/adjustable), voltage, and polarity.
     * This is the fallback method when metadata is not available.
     */
    private double calculateRegulatorTypeBasedSimilarity(String mpn1, String mpn2) {

        // Handle adjustable voltage regulators first
        if (isAdjustableRegulator(mpn1) && isAdjustableRegulator(mpn2)) {
            // Extract base parts without package codes
            String base1 = extractBasePart(mpn1);
            String base2 = extractBasePart(mpn2);

            if (base1.equals(base2)) {
                logger.debug("Same adjustable regulator with different packages");
                return HIGH_SIMILARITY;
            }

            // Check compatible families (LM317/LM350/LM338)
            if (areCompatibleAdjustableRegulators(base1, base2)) {
                logger.debug("Compatible adjustable regulators");
                return HIGH_SIMILARITY;
            }
            return LOW_SIMILARITY;
        }

        // Handle fixed voltage regulators (78xx/79xx series)
        if (isFixedRegulator(mpn1) && isFixedRegulator(mpn2)) {
            String voltage1 = extractVoltage(mpn1);
            String voltage2 = extractVoltage(mpn2);
            boolean samePolarity = (mpn1.contains("78") && mpn2.contains("78")) ||
                    (mpn1.contains("79") && mpn2.contains("79"));

            if (voltage1.equals(voltage2) && samePolarity) {
                // Check if they're the same part with different packages
                String base1 = extractBasePart(mpn1);
                String base2 = extractBasePart(mpn2);
                if (base1.equals(base2)) {
                    logger.debug("Same fixed regulator with different package codes");
                    return HIGH_SIMILARITY;
                }
                logger.debug("Compatible fixed regulators. Voltages: {} vs {}", voltage1, voltage2);
                return HIGH_SIMILARITY;
            }
            return LOW_SIMILARITY;
        }

        return LOW_SIMILARITY;
    }



    private boolean areCompatibleAdjustableRegulators(String base1, String base2) {
        // Positive adjustable regulators
        Set<String> positiveAdj = Set.of("LM317", "LM338", "LM350");
        // Negative adjustable regulators
        Set<String> negativeAdj = Set.of("LM337");

        return (positiveAdj.contains(base1) && positiveAdj.contains(base2)) ||
                (negativeAdj.contains(base1) && negativeAdj.contains(base2));
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";
        // For adjustable regulators
        if (isAdjustableRegulator(mpn)) {
            return mpn.replaceAll("(?:[A-Z]*(?:T|K|H|MP|S))?$", "");
        }
        // For fixed regulators
        return mpn.replaceAll("(?:CT|T|KC|KV|MP|DT)$", "");
    }

    private boolean isVoltageRegulator(String mpn) {
        if (mpn == null) return false;
        return mpn.matches("^(?:LM|MC|UA)7[89][0-9]{2}.*") ||  // Fixed voltage regulators
                mpn.matches("^(?:LM|MC)3(?:17|37|38|50).*");    // Adjustable regulators
    }

    private boolean isFixedRegulator(String mpn) {
        return mpn != null && mpn.matches(".*7[89][0-9]{2}.*");
    }

    private String extractVoltage(String mpn) {
        if (mpn == null) return "";
        Matcher m = Pattern.compile("7[89](\\d{2})").matcher(mpn);
        return m.find() ? m.group(1) : "";
    }




    private String extractPackageCode(String mpn) {
        if (mpn == null) return "";
        Matcher m = Pattern.compile("(?:CT|T|KC|KV|MP|DT|K|H|S)$").matcher(mpn);
        return m.find() ? m.group() : "";
    }

    private boolean areCompatiblePackages(String pkg1, String pkg2) {
        if (pkg1.equals(pkg2)) return true;

        // Known compatible package sets
        Set<String> powerPkgs = Set.of("T", "CT", "K", "KC", "KV", "MP", "DT");
        return powerPkgs.contains(pkg1) && powerPkgs.contains(pkg2);
    }



    private boolean isAdjustableRegulator(String mpn) {
        return mpn != null &&
                (mpn.startsWith("LM317") || mpn.startsWith("LM337") ||
                        mpn.startsWith("LM338") || mpn.startsWith("LM350"));
    }



    private String extractFamily(String mpn) {
        if (mpn == null) return "";
        Matcher m = Pattern.compile("(LM3[0-9]{2})").matcher(mpn);
        return m.find() ? m.group(1) : "";
    }

    private boolean areCompatibleFamilies(String family1, String family2) {
        if (family1.equals(family2)) return true;

        // Known compatible families
        Set<String> positiveAdj = Set.of("LM317", "LM338", "LM350");
        Set<String> negativeAdj = Set.of("LM337", "LM333");

        return (positiveAdj.contains(family1) && positiveAdj.contains(family2)) ||
                (negativeAdj.contains(family1) && negativeAdj.contains(family2));
    }

    // ========== Spec Extraction Methods for Metadata-Driven Comparison ==========

    /**
     * Extract regulator type from MPN (fixed or adjustable).
     */
    private String extractRegulatorType(String mpn) {
        if (mpn == null) return null;
        if (isAdjustableRegulator(mpn)) {
            return "adjustable";
        } else if (isFixedRegulator(mpn)) {
            return "fixed";
        }
        return null;
    }

    /**
     * Extract output voltage from MPN.
     * For fixed regulators: 05, 12, 15, 24, etc. (from 78xx/79xx pattern)
     * For adjustable regulators: null (voltage is variable)
     */
    private String extractOutputVoltage(String mpn) {
        if (mpn == null) return null;
        if (isFixedRegulator(mpn)) {
            return extractVoltage(mpn);  // Returns "05", "12", "15", etc.
        }
        // Adjustable regulators have variable output voltage
        return null;
    }

    /**
     * Extract polarity from MPN (positive or negative).
     */
    private String extractPolarity(String mpn) {
        if (mpn == null) return null;

        // For fixed regulators
        if (mpn.contains("78")) {
            return "positive";
        } else if (mpn.contains("79")) {
            return "negative";
        }

        // For adjustable regulators
        if (mpn.startsWith("LM317") || mpn.startsWith("LM338") || mpn.startsWith("LM350")) {
            return "positive";
        } else if (mpn.startsWith("LM337")) {
            return "negative";
        }

        return null;
    }

    /**
     * Extract current rating from MPN in milliamps.
     * Returns null if current rating cannot be determined from MPN alone.
     *
     * Note: Most common regulators are 1A (1000mA), but there are variations:
     * - 78Lxx/79Lxx: 100mA (low power)
     * - 78Mxx/79Mxx: 500mA (medium power)
     * - 78xx/79xx: 1000mA (standard)
     * - 78Hxx/79Hxx: 5000mA (high power)
     * - LM317: 1500mA
     * - LM338: 5000mA
     * - LM350: 3000mA
     * - LM337: 1500mA
     */
    private Integer extractCurrentRating(String mpn) {
        if (mpn == null) return null;

        // Low power: 78L/79L series
        if (mpn.matches(".*7[89]L[0-9]{2}.*")) {
            return 100;
        }

        // Medium power: 78M/79M series
        if (mpn.matches(".*7[89]M[0-9]{2}.*")) {
            return 500;
        }

        // High power: 78H/79H series
        if (mpn.matches(".*7[89]H[0-9]{2}.*")) {
            return 5000;
        }

        // Standard fixed regulators (78xx/79xx): 1A
        if (isFixedRegulator(mpn) && !mpn.matches(".*7[89][LMH][0-9]{2}.*")) {
            return 1000;
        }

        // Adjustable regulators - known types
        if (mpn.startsWith("LM317")) {
            return 1500;
        } else if (mpn.startsWith("LM338")) {
            return 5000;
        } else if (mpn.startsWith("LM350")) {
            return 3000;
        } else if (mpn.startsWith("LM337")) {
            return 1500;
        }

        return null;
    }

    /**
     * Extract package type from MPN.
     * Returns decoded package name (e.g., "TO-220", "TO-3") instead of package code.
     */
    private String extractPackage(String mpn) {
        if (mpn == null) return null;

        String packageCode = extractPackageCode(mpn);
        if (packageCode.isEmpty()) {
            return null;
        }

        // Decode package codes to standard package names
        switch (packageCode) {
            case "T":
            case "CT":
                return "TO-220";
            case "K":
            case "KC":
            case "KV":
                return "TO-3";
            case "H":
                return "TO-39";
            case "S":
            case "MP":
                return "SOT-223";
            case "DT":
                return "TO-252";
            default:
                return packageCode;  // Return as-is if unknown
        }
    }
}