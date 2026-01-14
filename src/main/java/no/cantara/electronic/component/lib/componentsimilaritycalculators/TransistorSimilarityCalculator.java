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

/**
 * Similarity calculator for transistor components (BJTs).
 *
 * Uses metadata-driven comparison based on extracted specs (polarity, voltage, current, package).
 * NPN vs PNP transistors (2N2222≈PN2222, 2N3904, BC547 series) are compared using
 * their electrical specifications with known equivalent groups as boost.
 *
 * Fallback to equivalent-group matching for unknown transistor types.
 */
public class TransistorSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(TransistorSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    // Known equivalent groups
    private static final Map<String, Set<String>> EQUIVALENT_GROUPS = new HashMap<>();
    static {
        // NPN general purpose transistors - 2N series
        EQUIVALENT_GROUPS.put("2N2222", Set.of("2N2222", "2N2222A", "PN2222"));
        EQUIVALENT_GROUPS.put("2N3904", Set.of("2N3904", "PN3904"));
        EQUIVALENT_GROUPS.put("2N4401", Set.of("2N4401", "PN4401"));

        // NPN general purpose transistors - BC series
        EQUIVALENT_GROUPS.put("BC547", Set.of("BC547", "BC547B", "BC548", "BC337"));
        EQUIVALENT_GROUPS.put("BC337", Set.of("BC337", "BC337-40"));

        // PNP general purpose transistors - 2N series
        EQUIVALENT_GROUPS.put("2N2907", Set.of("2N2907", "2N2907A", "PN2907"));
        EQUIVALENT_GROUPS.put("2N3906", Set.of("2N3906", "PN3906"));
        EQUIVALENT_GROUPS.put("2N4403", Set.of("2N4403", "PN4403"));

        // PNP general purpose transistors - BC series
        EQUIVALENT_GROUPS.put("BC557", Set.of("BC557", "BC557B", "BC558", "BC327"));
        EQUIVALENT_GROUPS.put("BC327", Set.of("BC327", "BC327-40"));
    }

    // Known transistor characteristics
    private static final Map<String, TransistorCharacteristics> KNOWN_CHARACTERISTICS = new HashMap<>();
    static {
        // NPN transistors
        KNOWN_CHARACTERISTICS.put("2N2222", new TransistorCharacteristics(true, 40, 0.8, 30, "TO-18"));
        KNOWN_CHARACTERISTICS.put("PN2222", new TransistorCharacteristics(true, 40, 0.8, 30, "TO-92"));
        KNOWN_CHARACTERISTICS.put("2N3904", new TransistorCharacteristics(true, 40, 0.2, 20, "TO-92"));
        KNOWN_CHARACTERISTICS.put("2N4401", new TransistorCharacteristics(true, 40, 0.6, 25, "TO-92"));
        KNOWN_CHARACTERISTICS.put("BC547", new TransistorCharacteristics(true, 45, 0.1, 20, "TO-92"));

        // PNP transistors
        KNOWN_CHARACTERISTICS.put("2N2907", new TransistorCharacteristics(false, -40, -0.8, -30, "TO-18"));
        KNOWN_CHARACTERISTICS.put("PN2907", new TransistorCharacteristics(false, -40, -0.8, -30, "TO-92"));
        KNOWN_CHARACTERISTICS.put("2N3906", new TransistorCharacteristics(false, -40, -0.2, -20, "TO-92"));
        KNOWN_CHARACTERISTICS.put("BC557", new TransistorCharacteristics(false, -45, -0.1, -20, "TO-92"));
    }

    public TransistorSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) return false;
        return type == ComponentType.TRANSISTOR ||
                type.name().startsWith("TRANSISTOR_") ||
                type.getBaseType() == ComponentType.TRANSISTOR;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        logger.debug("Comparing transistors: {} vs {}", mpn1, mpn2);

        // Check if they're even transistors
        if (!isTransistor(mpn1) || !isTransistor(mpn2)) {
            logger.debug("One or both parts are not transistors");
            return 0.0;
        }

        // Get metadata for spec-based comparison
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.TRANSISTOR);
        if (metadataOpt.isPresent()) {
            logger.trace("Using metadata-driven comparison for transistors");
            return calculateMetadataDrivenSimilarity(mpn1, mpn2, metadataOpt.get());
        }

        // Fallback to equivalent-group matching if metadata not available
        logger.trace("No metadata found for TRANSISTOR, using equivalent group fallback");
        return calculateEquivalentGroupBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity using metadata-driven weighted comparison.
     * Extracts specs from MPNs and uses tolerance rules for comparison.
     */
    private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
        SimilarityProfile profile = metadata.getDefaultProfile();

        logger.trace("Using profile: {}", profile);

        // Extract specs from both MPNs
        String polarity1 = extractPolarity(mpn1);
        String polarity2 = extractPolarity(mpn2);
        Integer voltage1 = extractVoltageRating(mpn1);
        Integer voltage2 = extractVoltageRating(mpn2);
        Integer current1 = extractCurrentRating(mpn1);
        Integer current2 = extractCurrentRating(mpn2);
        String package1 = extractPackage(mpn1);
        String package2 = extractPackage(mpn2);
        Double hfe1 = extractHfe(mpn1);
        Double hfe2 = extractHfe(mpn2);

        logger.trace("MPN1 specs: polarity={}, voltage={}V, current={}mA, package={}, hfe={}",
                polarity1, voltage1, current1, package1, hfe1);
        logger.trace("MPN2 specs: polarity={}, voltage={}V, current={}mA, package={}, hfe={}",
                polarity2, voltage2, current2, package2, hfe2);

        // Short-circuit: NPN and PNP are not interchangeable
        if (polarity1 != null && polarity2 != null && !polarity1.equals(polarity2)) {
            logger.debug("Different polarity: {} vs {} -> 0.0", polarity1, polarity2);
            return 0.0;
        }

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

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
            logger.trace("Polarity comparison: score={}, weight={}, contribution={}", specScore, specWeight, specScore * specWeight);
        }

        // Compare voltageRating (CRITICAL)
        ComponentTypeMetadata.SpecConfig voltageConfig = metadata.getSpecConfig("voltageRating");
        if (voltageConfig != null && voltage1 != null && voltage2 != null) {
            ToleranceRule rule = voltageConfig.getToleranceRule();
            SpecValue<Integer> orig = new SpecValue<>(voltage1, SpecUnit.VOLTS);
            SpecValue<Integer> cand = new SpecValue<>(voltage2, SpecUnit.VOLTS);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(voltageConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Voltage comparison: score={}, weight={}, contribution={}", specScore, specWeight, specScore * specWeight);
        }

        // Compare currentRating (CRITICAL)
        ComponentTypeMetadata.SpecConfig currentConfig = metadata.getSpecConfig("currentRating");
        if (currentConfig != null && current1 != null && current2 != null) {
            ToleranceRule rule = currentConfig.getToleranceRule();
            SpecValue<Integer> orig = new SpecValue<>(current1, SpecUnit.MILLIAMPS);
            SpecValue<Integer> cand = new SpecValue<>(current2, SpecUnit.MILLIAMPS);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(currentConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Current comparison: score={}, weight={}, contribution={}", specScore, specWeight, specScore * specWeight);
        }

        // Compare package (HIGH)
        ComponentTypeMetadata.SpecConfig packageConfig = metadata.getSpecConfig("package");
        if (packageConfig != null && package1 != null && package2 != null) {
            ToleranceRule rule = packageConfig.getToleranceRule();
            SpecValue<String> orig = new SpecValue<>(package1, SpecUnit.NONE);
            SpecValue<String> cand = new SpecValue<>(package2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(packageConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Package comparison: score={}, weight={}, contribution={}", specScore, specWeight, specScore * specWeight);
        }

        // Compare hfe/gain (MEDIUM) - optional
        ComponentTypeMetadata.SpecConfig hfeConfig = metadata.getSpecConfig("hfe");
        if (hfeConfig != null && hfe1 != null && hfe2 != null) {
            ToleranceRule rule = hfeConfig.getToleranceRule();
            SpecValue<Double> orig = new SpecValue<>(hfe1, SpecUnit.NONE);
            SpecValue<Double> cand = new SpecValue<>(hfe2, SpecUnit.NONE);
            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(hfeConfig.getImportance());
            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("hfe comparison: score={}, weight={}, contribution={}", specScore, specWeight, specScore * specWeight);
        }

        // Normalize to [0.0, 1.0]
        double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

        // Check for known equivalent groups and boost score if applicable
        String normalizedMpn1 = normalizePartNumber(mpn1);
        String normalizedMpn2 = normalizePartNumber(mpn2);
        if (isInKnownEquivalentGroup(normalizedMpn1, normalizedMpn2)) {
            logger.trace("Known equivalent group detected, boosting similarity");
            similarity = Math.max(similarity, HIGH_SIMILARITY);
        }

        logger.debug("Final similarity: {}", similarity);
        return similarity;
    }

    /**
     * Extract polarity from MPN (NPN or PNP).
     */
    private String extractPolarity(String mpn) {
        if (mpn == null) return null;
        String basePart = getBasePart(mpn);
        TransistorCharacteristics chars = KNOWN_CHARACTERISTICS.get(basePart);
        if (chars != null) {
            return chars.isNPN ? "NPN" : "PNP";
        }
        // Default heuristic: most common 2N/PN transistors are NPN, BC5xx are NPN, BC3xx are PNP
        String upperMpn = mpn.toUpperCase();
        if (upperMpn.matches("^(2N|PN)(2222|3904|4401).*")) return "NPN";
        if (upperMpn.matches("^(2N|PN)(2907|3906|4403).*")) return "PNP";
        if (upperMpn.matches("^BC(54[0-9]|33[0-9]).*")) return "NPN";
        if (upperMpn.matches("^BC(55[0-9]|32[0-9]).*")) return "PNP";
        return null;
    }

    /**
     * Extract voltage rating from MPN in volts.
     */
    private Integer extractVoltageRating(String mpn) {
        if (mpn == null) return null;
        String basePart = getBasePart(mpn);
        TransistorCharacteristics chars = KNOWN_CHARACTERISTICS.get(basePart);
        if (chars != null) {
            return (int) Math.abs(chars.vceo);
        }
        // Default ratings for unknown parts
        return 40; // Common rating for general purpose transistors
    }

    /**
     * Extract current rating from MPN in milliamps.
     */
    private Integer extractCurrentRating(String mpn) {
        if (mpn == null) return null;
        String basePart = getBasePart(mpn);
        TransistorCharacteristics chars = KNOWN_CHARACTERISTICS.get(basePart);
        if (chars != null) {
            return (int) (Math.abs(chars.ic) * 1000); // Convert A to mA
        }
        // Default ratings for unknown parts
        return 600; // Common rating for general purpose transistors (600mA)
    }

    /**
     * Extract package type from MPN.
     */
    private String extractPackage(String mpn) {
        if (mpn == null) return null;
        String basePart = getBasePart(mpn);
        TransistorCharacteristics chars = KNOWN_CHARACTERISTICS.get(basePart);
        if (chars != null) {
            return chars.packageType;
        }
        // Default package detection from suffixes
        String upperMpn = mpn.toUpperCase();
        if (upperMpn.endsWith("-TA") || upperMpn.endsWith("TA")) return "TO-92";
        if (upperMpn.endsWith("-TF") || upperMpn.endsWith("TF")) return "TO-92";
        if (upperMpn.endsWith("-T") || upperMpn.endsWith("T")) return "TO-92";
        if (upperMpn.matches("^2N.*")) return "TO-18"; // Default for 2N series
        return "TO-92"; // Default for BC series
    }

    /**
     * Extract hFE (gain) from MPN.
     */
    private Double extractHfe(String mpn) {
        if (mpn == null) return null;
        String basePart = getBasePart(mpn);
        TransistorCharacteristics chars = KNOWN_CHARACTERISTICS.get(basePart);
        if (chars != null) {
            return chars.hfe;
        }
        return null; // Unknown gain
    }

    /**
     * Check if two MPNs are in a known equivalent group.
     */
    private boolean isInKnownEquivalentGroup(String mpn1, String mpn2) {
        for (Set<String> group : EQUIVALENT_GROUPS.values()) {
            if (isInEquivalentGroup(mpn1, group) && isInEquivalentGroup(mpn2, group)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate similarity based on equivalent groups and known characteristics.
     * This is the fallback comparison method when metadata is not available.
     */
    private double calculateEquivalentGroupBasedSimilarity(String mpn1, String mpn2) {
        // Extract base numbers (without prefix and suffix)
        String base1 = extractBaseNumber(mpn1);
        String base2 = extractBaseNumber(mpn2);

        logger.debug("Base numbers: {} and {}", base1, base2);

        // Check transistor family
        String family1 = getTransistorFamily(mpn1);
        String family2 = getTransistorFamily(mpn2);
        if (!family1.isEmpty() && !family2.isEmpty() && !family1.equals(family2)) {
            logger.debug("Different transistor families: {} vs {}", family1, family2);
            return LOW_SIMILARITY;  // Different families should have low similarity
        }

        // Check for known equivalent groups first
        String normalizedMpn1 = normalizePartNumber(mpn1);
        String normalizedMpn2 = normalizePartNumber(mpn2);

        for (Set<String> group : EQUIVALENT_GROUPS.values()) {
            if (isInEquivalentGroup(normalizedMpn1, group) &&
                    isInEquivalentGroup(normalizedMpn2, group)) {
                logger.debug("Found in equivalent group");
                return HIGH_SIMILARITY;
            }
        }

        // Check for exact same base part with optional A suffix
        if (isSameBasePart(mpn1, mpn2)) {
            logger.debug("Same transistor with/without suffix");
            return HIGH_SIMILARITY;
        }

        // Get transistor characteristics for comparison
        TransistorCharacteristics char1 = KNOWN_CHARACTERISTICS.get(getBasePart(mpn1));
        TransistorCharacteristics char2 = KNOWN_CHARACTERISTICS.get(getBasePart(mpn2));

        // If we have characteristics for both, compare them
        if (char1 != null && char2 != null) {
            // Must be same type (NPN/PNP)
            if (char1.isNPN != char2.isNPN) {
                logger.debug("Different transistor types (NPN vs PNP)");
                return LOW_SIMILARITY;
            }

            // Compare electrical characteristics
            if (areCompatibleCharacteristics(char1, char2)) {
                return HIGH_SIMILARITY;
            }
        }

        // Different transistors in same series
        if (areInSameSeries(base1, base2)) {
            logger.debug("Same series but different transistors");
            return MEDIUM_SIMILARITY;
        }

        logger.debug("Different transistor types");
        return LOW_SIMILARITY;
    }

    private boolean isSameBasePart(String mpn1, String mpn2) {
        // Extract the complete base number including prefix
        String base1 = getCompleteBasePart(mpn1);
        String base2 = getCompleteBasePart(mpn2);

        // Clean up any 'A' suffix
        base1 = base1.replaceAll("A$", "");
        base2 = base2.replaceAll("A$", "");

        return base1.equals(base2);
    }

    private String getCompleteBasePart(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();
        // Keep the prefix (2N, PN, etc.) and the base number
        return upperMpn.replaceAll("([2P]N[0-9]+).*$", "$1");
    }

    // Add this method to check transistor family
    private String getTransistorFamily(String mpn) {
        if (mpn == null) return "";
        if (mpn.toUpperCase().startsWith("2N") || mpn.toUpperCase().startsWith("PN")) {
            return "2N";
        } else if (mpn.toUpperCase().startsWith("BC")) {
            return "BC";
        }
        return "";
    }
    private String normalizePartNumber(String mpn) {
        if (mpn == null) return "";
        // Remove suffixes and convert to uppercase
        return mpn.toUpperCase()
                .replaceAll("[-](T|TR|TA|TF|G|L|R|Q|X)$", "")
                .replaceAll("(T|TR|TA|TF|G|L|R|Q|X)$", "")
                .replaceAll("A$", "");  // Remove trailing A
    }

    private boolean isInEquivalentGroup(String mpn, Set<String> group) {
        String normalizedMpn = normalizePartNumber(mpn);
        return group.stream()
                .map(this::normalizePartNumber)
                .anyMatch(g -> g.equals(normalizedMpn));
    }

    private boolean areInSameSeries(String base1, String base2) {
        if (base1 == null || base2 == null) return false;

        try {
            int num1 = Integer.parseInt(base1);
            int num2 = Integer.parseInt(base2);

            // Define series ranges
            return (isInRange(num1, 2218, 2222) && isInRange(num2, 2218, 2222)) ||  // 2N22xx series
                    (isInRange(num1, 2904, 2907) && isInRange(num2, 2904, 2907)) ||  // 2N29xx series
                    (isInRange(num1, 3903, 3904) && isInRange(num2, 3903, 3904)) ||  // 2N390x series
                    (isInRange(num1, 3905, 3906) && isInRange(num2, 3905, 3906));    // 2N390x series
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isInRange(int num, int min, int max) {
        return num >= min && num <= max;
    }

    private String extractBaseNumber(String mpn) {
        if (mpn == null) return null;
        String withoutPrefix = mpn.replaceAll("^(2N|PN|BC|BD|TIP|2SC|2SA|MPS|MPSA|MPSH)", "");
        StringBuilder number = new StringBuilder();
        for (char c : withoutPrefix.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                break;
            }
        }
        return number.length() > 0 ? number.toString() : null;
    }

    private String getBasePart(String mpn) {
        if (mpn == null) return "";
        return mpn.replaceAll("([A-Z0-9]+)[A-Z].*$", "$1").toUpperCase();
    }

    private boolean isTransistor(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.matches("^(2N|PN|BC|BD|TIP|2SC|2SA|MPS|MPSA|MPSH)[0-9].*");
    }

    private boolean areCompatibleCharacteristics(TransistorCharacteristics char1,
                                                 TransistorCharacteristics char2) {
        // Must be same type (NPN/PNP)
        if (char1.isNPN != char2.isNPN) return false;

        // Compare voltage, current and gain with some tolerance
        double voltageTolerance = 0.2;  // 20%
        double currentTolerance = 0.3;  // 30%
        double gainTolerance = 0.3;     // 30%

        boolean voltageMatch = Math.abs(char1.vceo - char2.vceo) /
                Math.max(char1.vceo, char2.vceo) <= voltageTolerance;
        boolean currentMatch = Math.abs(char1.ic - char2.ic) /
                Math.max(char1.ic, char2.ic) <= currentTolerance;
        boolean gainMatch = Math.abs(char1.hfe - char2.hfe) /
                Math.max(char1.hfe, char2.hfe) <= gainTolerance;

        return voltageMatch && currentMatch && gainMatch;
    }

    private static class TransistorCharacteristics {
        final boolean isNPN;           // true for NPN, false for PNP
        final double vceo;             // Collector-Emitter Voltage (V)
        final double ic;               // Collector Current (A)
        final double hfe;              // DC Current Gain
        final String packageType;      // Package type (TO-92, TO-18, etc.)

        TransistorCharacteristics(boolean isNPN, double vceo, double ic, double hfe, String packageType) {
            this.isNPN = isNPN;
            this.vceo = vceo;
            this.ic = ic;
            this.hfe = hfe;
            this.packageType = packageType;
        }

        @Override
        public String toString() {
            return String.format("%s: Vceo=%.1fV, Ic=%.1fA, hfe=%.1f, Pkg=%s",
                    isNPN ? "NPN" : "PNP", vceo, ic, hfe, packageType);
        }
    }
}