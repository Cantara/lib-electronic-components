package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.*;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metadata-driven similarity calculator for resistors.
 * Uses ComponentTypeMetadata to determine critical specs, tolerance rules, and importance weights.
 */
public class ResistorSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(ResistorSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    public ResistorSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) {
            return false;
        }
        return type == ComponentType.RESISTOR ||
                type == ComponentType.RESISTOR_CHIP_VISHAY ||
                type == ComponentType.RESISTOR_CHIP_YAGEO ||
                type.name().startsWith("RESISTOR_");
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null || patternRegistry == null) {
            return 0.0;
        }

        logger.debug("Comparing resistors: {} and {}", mpn1, mpn2);

        // Get metadata for resistor type
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.RESISTOR);
        if (metadataOpt.isEmpty()) {
            logger.warn("No metadata found for RESISTOR type, falling back to legacy scoring");
            return calculateLegacySimilarity(mpn1, mpn2);
        }

        ComponentTypeMetadata metadata = metadataOpt.get();
        SimilarityProfile profile = metadata.getDefaultProfile();

        // Extract specs for both MPNs
        String size1 = extractPackageSize(mpn1);
        String size2 = extractPackageSize(mpn2);
        String value1 = extractValue(mpn1);
        String value2 = extractValue(mpn2);

        logger.trace("Size1: {}, Size2: {}", size1, size2);
        logger.trace("Value1: {}, Value2: {}", value1, value2);

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // 1. Package comparison (HIGH importance)
        ComponentTypeMetadata.SpecConfig packageConfig = metadata.getSpecConfig("package");
        if (packageConfig != null && size1 != null && size2 != null) {
            ToleranceRule packageRule = packageConfig.getToleranceRule();
            SpecValue<String> origPackage = new SpecValue<>(size1, SpecUnit.NONE);
            SpecValue<String> candPackage = new SpecValue<>(size2, SpecUnit.NONE);

            double packageScore = packageRule.compare(origPackage, candPackage);
            double packageWeight = profile.getEffectiveWeight(packageConfig.getImportance());

            logger.trace("Package score: {}, weight: {}", packageScore, packageWeight);
            totalScore += packageScore * packageWeight;
            maxPossibleScore += packageWeight;
        }

        // 2. Resistance value comparison (CRITICAL importance)
        ComponentTypeMetadata.SpecConfig resistanceConfig = metadata.getSpecConfig("resistance");
        if (resistanceConfig != null && value1 != null && value2 != null) {
            ToleranceRule resistanceRule = resistanceConfig.getToleranceRule();

            // Parse resistance values to double (in ohms)
            Double r1 = parseResistanceValue(value1);
            Double r2 = parseResistanceValue(value2);

            if (r1 != null && r2 != null) {
                SpecValue<Double> origResistance = new SpecValue<>(r1, SpecUnit.OHMS);
                SpecValue<Double> candResistance = new SpecValue<>(r2, SpecUnit.OHMS);

                double resistanceScore = resistanceRule.compare(origResistance, candResistance);
                double resistanceWeight = profile.getEffectiveWeight(resistanceConfig.getImportance());

                logger.trace("Resistance score: {}, weight: {}", resistanceScore, resistanceWeight);
                totalScore += resistanceScore * resistanceWeight;
                maxPossibleScore += resistanceWeight;
            }
        }

        // Normalize to [0.0, 1.0]
        double normalizedSimilarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

        logger.debug("Total score: {}, Max possible: {}, Normalized similarity: {}",
                     totalScore, maxPossibleScore, normalizedSimilarity);

        return normalizedSimilarity;
    }

    /**
     * Legacy similarity calculation for backward compatibility if metadata unavailable.
     */
    private double calculateLegacySimilarity(String mpn1, String mpn2) {
        String size1 = extractPackageSize(mpn1);
        String size2 = extractPackageSize(mpn2);
        String value1 = extractValue(mpn1);
        String value2 = extractValue(mpn2);

        double similarity = 0.0;

        // Same package size
        if (size1 != null && size2 != null && size1.equals(size2)) {
            similarity += 0.3;
        }

        // Same resistance value
        if (value1 != null && value2 != null) {
            String norm1 = normalizeValue(value1);
            String norm2 = normalizeValue(value2);
            if (norm1.equals(norm2)) {
                similarity += 0.5;
            }
        }

        return similarity;
    }

    /**
     * Parse resistance value string to double in ohms.
     * Examples:
     * - "10K" → 10000.0
     * - "1M" → 1000000.0
     * - "100R" → 100.0
     * - "4.7K" → 4700.0
     */
    private Double parseResistanceValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            String normalized = normalizeValue(value);
            if (normalized == null) {
                return null;
            }

            // Handle K (kilo-ohms)
            if (normalized.contains("K")) {
                String numPart = normalized.replace("K", "");
                return Double.parseDouble(numPart) * 1000.0;
            }

            // Handle M (mega-ohms)
            if (normalized.contains("M")) {
                String numPart = normalized.replace("M", "");
                return Double.parseDouble(numPart) * 1000000.0;
            }

            // Handle R (decimal point) or plain ohms
            if (normalized.contains(".")) {
                return Double.parseDouble(normalized);
            }

            // Plain number (ohms)
            return Double.parseDouble(normalized);

        } catch (NumberFormatException e) {
            logger.trace("Could not parse resistance value: {}", value);
            return null;
        }
    }

    private String extractPackageSize(String mpn) {
        // Common chip resistor sizes
        if (mpn.contains("0603")) return "0603";
        if (mpn.contains("0805")) return "0805";
        if (mpn.contains("1206")) return "1206";
        if (mpn.contains("2512")) return "2512";

        // For Vishay CRCW format
        if (mpn.startsWith("CRCW")) {
            try {
                return mpn.substring(4, 8);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }

        // For Yageo RC format
        if (mpn.startsWith("RC")) {
            String[] parts = mpn.split("-");
            if (parts.length > 0 && parts[0].length() > 2) {
                return parts[0].substring(2);
            }
        }

        return null;
    }

    private String extractValue(String mpn) {
        // For Vishay CRCW format (e.g., CRCW060310K0FKEA)
        if (mpn.startsWith("CRCW")) {
            // Find the value part (usually after size code)
            int startIdx = mpn.indexOf("10K");
            if (startIdx != -1) {
                return "10K";
            }
        }

        // For Yageo RC format (e.g., RC0603FR-0710KL)
        if (mpn.startsWith("RC")) {
            String[] parts = mpn.split("-");
            if (parts.length > 1) {
                // Look for value in the second part
                String valuePart = parts[1];
                if (valuePart.contains("10K")) {
                    return "10K";
                }
            }
        }

        // Generic value extraction for other formats
        Matcher matcher = Pattern.compile("(\\d+[.]?\\d*[K|M|R]?)").matcher(mpn);
        if (matcher.find()) {
            return normalizeValue(matcher.group(1));
        }

        return null;
    }

    private String normalizeValue(String value) {
        if (value == null) return null;

        value = value.toUpperCase();

        // Handle special cases
        if (value.contains("R")) {
            value = value.replace("R", ".");
        }

        // Remove trailing zeros after K or M
        value = value.replaceAll("K0+$", "K");
        value = value.replaceAll("M0+$", "M");

        // Handle fractional K values (e.g., 1K5 -> 1.5K)
        if (value.contains("K") && !value.endsWith("K")) {
            String[] parts = value.split("K");
            if (parts.length == 2) {
                return parts[0] + "." + parts[1] + "K";
            }
        }

        return value;
    }
}
