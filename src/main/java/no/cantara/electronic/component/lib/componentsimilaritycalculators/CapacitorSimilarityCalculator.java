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
 * Metadata-driven similarity calculator for capacitors.
 * Uses ComponentTypeMetadata to determine critical specs, tolerance rules, and importance weights.
 */
public class CapacitorSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(CapacitorSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    public CapacitorSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) {
            return false;
        }
        return type == ComponentType.CAPACITOR ||
                type == ComponentType.CAPACITOR_CERAMIC_MURATA ||
                type == ComponentType.CAPACITOR_CERAMIC_KEMET ||
                type.name().startsWith("CAPACITOR_");
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null || patternRegistry == null) {
            return 0.0;
        }

        logger.debug("Comparing capacitors: {} and {}", mpn1, mpn2);

        // Get metadata for capacitor type
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.CAPACITOR);
        if (metadataOpt.isEmpty()) {
            logger.warn("No metadata found for CAPACITOR type, falling back to legacy scoring");
            return calculateLegacySimilarity(mpn1, mpn2);
        }

        ComponentTypeMetadata metadata = metadataOpt.get();
        SimilarityProfile profile = metadata.getDefaultProfile();

        // Extract specs for both MPNs
        String size1 = extractPackageSize(mpn1);
        String size2 = extractPackageSize(mpn2);
        String value1 = extractValue(mpn1);
        String value2 = extractValue(mpn2);
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);

        logger.trace("Size1: {}, Size2: {}", size1, size2);
        logger.trace("Value1: {}, Value2: {}", value1, value2);
        logger.trace("Voltage1: {}, Voltage2: {}", voltage1, voltage2);

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

        // 2. Capacitance value comparison (CRITICAL importance)
        ComponentTypeMetadata.SpecConfig capacitanceConfig = metadata.getSpecConfig("capacitance");
        if (capacitanceConfig != null && value1 != null && value2 != null) {
            ToleranceRule capacitanceRule = capacitanceConfig.getToleranceRule();

            // Parse capacitance values to double (in farads)
            Double c1 = parseCapacitanceValue(value1);
            Double c2 = parseCapacitanceValue(value2);

            if (c1 != null && c2 != null) {
                SpecValue<Double> origCapacitance = new SpecValue<>(c1, SpecUnit.FARADS);
                SpecValue<Double> candCapacitance = new SpecValue<>(c2, SpecUnit.FARADS);

                double capacitanceScore = capacitanceRule.compare(origCapacitance, candCapacitance);
                double capacitanceWeight = profile.getEffectiveWeight(capacitanceConfig.getImportance());

                logger.trace("Capacitance score: {}, weight: {}", capacitanceScore, capacitanceWeight);
                totalScore += capacitanceScore * capacitanceWeight;
                maxPossibleScore += capacitanceWeight;
            }
        }

        // 3. Voltage comparison (CRITICAL importance)
        ComponentTypeMetadata.SpecConfig voltageConfig = metadata.getSpecConfig("voltage");
        if (voltageConfig != null && voltage1 != null && voltage2 != null) {
            ToleranceRule voltageRule = voltageConfig.getToleranceRule();

            double v1 = parseVoltage(voltage1);
            double v2 = parseVoltage(voltage2);

            if (v1 > 0 && v2 > 0) {
                SpecValue<Double> origVoltage = new SpecValue<>(v1, SpecUnit.VOLTS);
                SpecValue<Double> candVoltage = new SpecValue<>(v2, SpecUnit.VOLTS);

                double voltageScore = voltageRule.compare(origVoltage, candVoltage);
                double voltageWeight = profile.getEffectiveWeight(voltageConfig.getImportance());

                logger.trace("Voltage score: {}, weight: {}", voltageScore, voltageWeight);
                totalScore += voltageScore * voltageWeight;
                maxPossibleScore += voltageWeight;
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
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);

        double similarity = 0.0;

        // Same package size
        if (size1 != null && size2 != null && size1.equals(size2)) {
            similarity += 0.3;
        }

        // Same capacitance value
        if (value1 != null && value2 != null) {
            String norm1 = normalizeValue(value1);
            String norm2 = normalizeValue(value2);
            if (norm1.equals(norm2)) {
                similarity += 0.4;
            }
        }

        // Compatible voltage rating
        if (voltage1 != null && voltage2 != null) {
            double v1 = parseVoltage(voltage1);
            double v2 = parseVoltage(voltage2);
            if (v1 >= v2) {  // Higher voltage rating is acceptable
                similarity += 0.2;
            }
        }

        return similarity;
    }

    /**
     * Parse capacitance value string to double in farads.
     * Examples:
     * - "0.1µF" → 1.0e-7 (100nF)
     * - "104" (via convertMurataCode) → "100000.0pF" → 1.0e-7 F
     */
    private Double parseCapacitanceValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            // Normalize: handle both µ (micro sign U+00B5) and Μ (Greek MU U+039C from toUpperCase)
            // Replace micro variants with 'u' before normalizing
            String normalized = value.replace("µ", "u").replace("Μ", "u");
            normalized = normalizeValue(normalized);
            if (normalized == null) {
                return null;
            }

            // Handle uF/UF (microfarads) - handles both µF and plain UF
            if (normalized.contains("UF")) {
                String numPart = normalized.replace("UF", "").trim();
                return Double.parseDouble(numPart) * 1.0e-6;
            }

            // Handle nF (nanofarads)
            if (normalized.contains("NF")) {
                String numPart = normalized.replace("NF", "").trim();
                return Double.parseDouble(numPart) * 1.0e-9;
            }

            // Handle pF (picofarads)
            if (normalized.contains("PF")) {
                String numPart = normalized.replace("PF", "").trim();
                return Double.parseDouble(numPart) * 1.0e-12;
            }

            // Plain number (assume microfarads for compatibility)
            return Double.parseDouble(normalized) * 1.0e-6;

        } catch (NumberFormatException e) {
            logger.trace("Could not parse capacitance value: {}", value);
            return null;
        }
    }

    private String extractPackageSize(String mpn) {
        // Murata GRM format (e.g., GRM188)
        if (mpn.startsWith("GRM")) {
            // 188 -> 0603
            if (mpn.contains("188")) return "0603";
            if (mpn.contains("216")) return "0805";
            if (mpn.contains("316")) return "1206";
        }

        // KEMET format (e.g., C0603)
        if (mpn.contains("0603")) return "0603";
        if (mpn.contains("0805")) return "0805";
        if (mpn.contains("1206")) return "1206";

        return null;
    }

    private String extractValue(String mpn) {
        // Murata format (e.g., 104 = 100nF = 0.1µF)
        Matcher murataMatch = Pattern.compile("[0-9]{3}[A-Z]").matcher(mpn);
        if (murataMatch.find()) {
            return convertMurataCode(murataMatch.group());
        }

        // KEMET format (e.g., 104K = 100nF = 0.1µF)
        Matcher kemetMatch = Pattern.compile("[0-9]{3}[A-Z]").matcher(mpn);
        if (kemetMatch.find()) {
            return convertKemetCode(kemetMatch.group());
        }

        return null;
    }

    private String extractVoltage(String mpn) {
        // Murata format (e.g., 1H = 50V)
        if (mpn.startsWith("GRM")) {
            Pattern voltPattern = Pattern.compile("[1-3][A-Z]");
            Matcher m = voltPattern.matcher(mpn);
            if (m.find()) {
                return convertMurataVoltage(m.group());
            }
        }

        // KEMET format (e.g., 5R = 50V)
        Pattern voltPattern = Pattern.compile("[0-9]+[R]");
        Matcher m = voltPattern.matcher(mpn);
        if (m.find()) {
            return m.group().replace("R", "");
        }

        return null;
    }

    private String convertMurataCode(String code) {
        // First two digits are significant digits
        // Third digit is multiplier
        // e.g., 104 = 10 * 10^4 pF = 100nF = 0.1µF
        try {
            int sig = Integer.parseInt(code.substring(0, 2));
            int mult = Integer.parseInt(code.substring(2, 3));
            double value = sig * Math.pow(10, mult - 12);  // Convert to µF
            return String.format("%.1f", value) + "µF";
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String convertKemetCode(String code) {
        // Similar to Murata code
        return convertMurataCode(code);
    }

    private String normalizeValue(String value) {
        if (value == null) return null;
        // Convert all to µF
        return value.replaceAll("\\s+", "").toUpperCase();
    }

    private double parseVoltage(String voltage) {
        try {
            return Double.parseDouble(voltage);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String convertMurataVoltage(String code) {
        return switch (code) {
            case "1H" -> "50";
            case "1E" -> "25";
            case "1C" -> "16";
            case "1A" -> "10";
            default -> "0";
        };
    }
}