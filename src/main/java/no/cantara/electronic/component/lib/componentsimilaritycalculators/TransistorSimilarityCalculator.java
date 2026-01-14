package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Similarity calculator for transistor components (BJTs).
 *
 * Uses equivalent groups (2N2222≈PN2222, BC547 series) and characteristics comparison.
 * Metadata infrastructure available for spec-based comparison when characteristics are known.
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

        // Check if metadata is available (for future spec-based enhancement)
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.TRANSISTOR);
        if (metadataOpt.isEmpty()) {
            logger.trace("No metadata found for TRANSISTOR, using equivalent group approach");
        }

        // Transistor comparison primarily uses equivalent groups and characteristics
        return calculateEquivalentGroupBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity based on equivalent groups and known characteristics.
     * This is the primary transistor comparison method.
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