package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Similarity calculator for diode components.
 *
 * Note: Diode similarity is primarily based on equivalent families and voltage ratings.
 * Signal diodes (1N4148≈1N914), rectifiers (1N400x≈RL20x), Zeners (voltage-specific),
 * and Schottkys have manufacturer-specific equivalent groups that are preserved
 * in the family-matching logic.
 *
 * Metadata infrastructure is available for future spec-based enhancements.
 */
public class DiodeSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(DiodeSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    public DiodeSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) return false;
        // Check both the type itself and its base type
        return type == ComponentType.DIODE ||
                type.name().startsWith("DIODE_") ||  // Handle manufacturer-specific diode types
                type.getBaseType() == ComponentType.DIODE;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        logger.debug("Comparing diodes: {} vs {}", mpn1, mpn2);

        // First check if these are even diodes
        if (!isDiode(mpn1) || !isDiode(mpn2)) {
            logger.debug("One or both parts are not diodes");
            return 0.0;
        }

        // Check if metadata is available (for future spec-based enhancement)
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.DIODE);
        if (metadataOpt.isEmpty()) {
            logger.trace("No metadata found for DIODE, using family-matching approach");
        }

        // Diode comparison uses family-matching approach based on equivalent groups
        // and voltage ratings rather than extracting individual electrical specs
        return calculateFamilyBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity based on diode families, equivalent groups, and voltage ratings.
     * This is the primary diode comparison method.
     */
    private double calculateFamilyBasedSimilarity(String mpn1, String mpn2) {

        // Get the diode families
        String family1 = getDiodeFamily(mpn1);
        String family2 = getDiodeFamily(mpn2);

        logger.debug("Diode families: {} vs {}", family1, family2);

        // Signal diode equivalents - specifically handle 1N4148 and 1N914
        if (isSignalDiode(mpn1) && isSignalDiode(mpn2)) {
            if (areEquivalentSignalDiodes(mpn1, mpn2)) {
                return HIGH_SIMILARITY;
            }
        }

        // Special case for 1N400x and RL20x series equivalence
        if ((mpn1.matches("1N400[1-7]") && mpn2.matches("RL20[1-7]")) ||
                (mpn2.matches("1N400[1-7]") && mpn1.matches("RL20[1-7]"))) {
            // Extract the numeric suffix and compare
            int suffix1 = extractSuffix(mpn1);
            int suffix2 = extractSuffix(mpn2);
            if (suffix1 == suffix2) {
                return HIGH_SIMILARITY;  // Exact equivalent (e.g., 1N4007 = RL207)
            }
        }
        if (family1.equals("SIGNAL") && family2.equals("SIGNAL")) {
            // Check for known equivalent pairs
            if ((mpn1.startsWith("1N4148") && mpn2.startsWith("1N914")) ||
                    (mpn1.startsWith("1N914") && mpn2.startsWith("1N4148"))) {
                return HIGH_SIMILARITY;  // Should be > 0.9 for known equivalents
            }
        }

        // Special handling for Zener diodes
        if (family1.equals("ZENER") && family2.equals("ZENER")) {
            double voltage1 = getZenerVoltage(mpn1);
            double voltage2 = getZenerVoltage(mpn2);
            logger.trace("Comparing Zener voltages: {}V vs {}V", voltage1, voltage2);

            if (voltage1 == voltage2) {
                return HIGH_SIMILARITY;
            }
            return LOW_SIMILARITY;
        }

        // Special handling for Schottky diodes
        if (family1.equals("SCHOTTKY") && family2.equals("SCHOTTKY")) {
            // First check if they're the same base part with different package
            String base1 = getBasePart(mpn1);
            String base2 = getBasePart(mpn2);
            logger.trace("Comparing Schottky base parts: {} vs {}", base1, base2);

            if (base1.equals(base2)) {
                return HIGH_SIMILARITY;
            }

            // If different base parts, check voltage compatibility
            int voltage1 = getSchottkyVoltage(mpn1);
            int voltage2 = getSchottkyVoltage(mpn2);
            logger.trace("Comparing Schottky voltages: {}V vs {}V", voltage1, voltage2);

            if (voltage1 == voltage2) {
                return HIGH_SIMILARITY;
            }
            if (voltage1 > 0 && voltage2 > 0) {
                return MEDIUM_SIMILARITY;
            }
        }

        // Check for equivalent families
        if (areEquivalentFamilies(family1, family2)) {
            logger.debug("Equivalent families -> HIGH_SIMILARITY");
            return HIGH_SIMILARITY;
        }

        // If they're in the same family
        if (family1.equals(family2)) {
            // All diodes in these families are typically interchangeable
            if (family1.equals("1N400x") || family1.equals("SIGNAL")) {
                logger.debug("Same interchangeable family -> HIGH_SIMILARITY");
                return HIGH_SIMILARITY;
            }

            // For rectifiers, check voltage ratings
            int voltage1 = getRectifierVoltage(mpn1);
            int voltage2 = getRectifierVoltage(mpn2);
            logger.trace("Comparing rectifier voltages: {}V vs {}V", voltage1, voltage2);

            if (voltage1 == voltage2) {
                return HIGH_SIMILARITY;
            }
            if (voltage1 > 0 && voltage2 > 0 && voltage1 >= voltage2) {
                return MEDIUM_SIMILARITY;
            }
        }

        return LOW_SIMILARITY;
    }


    private int extractSuffix(String mpn) {
        if (mpn.matches("1N400[1-7]")) {
            return Integer.parseInt(mpn.substring(5));
        }
        if (mpn.matches("RL20[1-7]")) {
            return Integer.parseInt(mpn.substring(4));
        }
        return 0;
    }

    private boolean isSignalDiode(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.startsWith("1N4148") ||
                upperMpn.startsWith("1N914");
    }

    private boolean areEquivalentSignalDiodes(String mpn1, String mpn2) {
        String base1 = getBasePartNumber(mpn1);
        String base2 = getBasePartNumber(mpn2);

        // Known equivalent pairs
        return (base1.equals("1N4148") && base2.equals("1N914")) ||
                (base1.equals("1N914") && base2.equals("1N4148"));
    }

    private String getBasePartNumber(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();
        // Extract base part number without suffixes
        return upperMpn.replaceAll("([1-9]N[0-9]+).*", "$1");
    }


    private boolean isDiode(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        // Add comprehensive diode pattern matching
        return upperMpn.matches("^(1N|BAV|BAS|BAT|FR|MUR|UF|BAW|BYV|BYW)[0-9].*") ||
                upperMpn.matches("^(LL|RR|RL|SR|SB|SD|SM|SS)[0-9].*");  // Added RL series
    }

    private String getDiodeFamily(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // 1N400x series rectifiers and equivalents
        if (upperMpn.matches("^1N400[1-7].*")) return "1N400x";
        if (upperMpn.matches("^RL20[1-7].*")) return "1N400x";  // RL207 is equivalent to 1N4007
        // 1N4148 signal diodes
        if (upperMpn.startsWith("1N4148")) return "SIGNAL";
        if (upperMpn.startsWith("1N914")) return "SIGNAL";
        // Schottky diodes
        if (upperMpn.matches("^(BAT|SB|SD)[0-9].*")) return "SCHOTTKY";
        // Zener diodes
        if (upperMpn.matches("^(BZX|1N47|1N52)[0-9].*")) return "ZENER";
        // Fast recovery rectifiers
        if (upperMpn.matches("^(FR|UF|MUR)[0-9].*")) return "FAST_RECTIFIER";
        // Other rectifiers
        if (upperMpn.matches("^1N[0-9].*")) return "RECTIFIER";
        if (upperMpn.matches("^RL[0-9].*")) return "RECTIFIER";  // Add RL series rectifiers

        return "OTHER";
    }

    private String getBasePart(String mpn) {
        if (mpn == null) return "";
        return mpn.replaceAll("([A-Z0-9]+)[A-Z].*$", "$1");
    }

    private boolean areEquivalentFamilies(String family1, String family2) {
        // Handle signal diode equivalents
        if ((family1.equals("SIGNAL") && family2.equals("SIGNAL")) ||
                (family1.equals("1N4148") && family2.equals("1N914")) ||
                (family1.equals("1N914") && family2.equals("1N4148"))) {
            return true;
        }

        // Handle rectifier equivalents
        if ((family1.equals("1N400x") && family2.equals("RECTIFIER")) ||
                (family1.equals("RECTIFIER") && family2.equals("1N400x"))) {
            return true;
        }

        return false;
    }

    private int getRectifierVoltage(String mpn) {
        String upperMpn = mpn.toUpperCase();

        // 1N400x series
        return switch (upperMpn) {
            case "1N4001", "RL201" -> 50;    // 50V
            case "1N4002", "RL202" -> 100;   // 100V
            case "1N4003", "RL203" -> 200;   // 200V
            case "1N4004", "RL204" -> 400;   // 400V
            case "1N4005", "RL205" -> 600;   // 600V
            case "1N4006", "RL206" -> 800;   // 800V
            case "1N4007", "RL207" -> 1000;  // 1000V
            default -> 0;
        };
    }

    private double getZenerVoltage(String mpn) {
        // Implementation for Zener voltage extraction
        if (mpn.startsWith("1N47")) {
            try {
                int code = Integer.parseInt(mpn.substring(4, 6));
                return switch (code) {
                    case 28 -> 3.3;    // 1N4728
                    case 29 -> 3.6;    // 1N4729
                    case 30 -> 3.9;    // 1N4730
                    case 31 -> 4.3;    // 1N4731
                    case 32 -> 4.7;    // 1N4732
                    case 33 -> 5.1;    // 1N4733
                    case 34 -> 5.6;    // 1N4734
                    case 35 -> 6.2;    // 1N4735
                    case 36 -> 6.8;    // 1N4736
                    case 37 -> 7.5;    // 1N4737
                    case 38 -> 8.2;    // 1N4738
                    case 39 -> 9.1;    // 1N4739
                    case 40 -> 10.0;   // 1N4740
                    case 41 -> 11.0;   // 1N4741
                    case 42 -> 12.0;   // 1N4742
                    case 43 -> 13.0;   // 1N4743
                    case 44 -> 15.0;   // 1N4744
                    case 45 -> 16.0;   // 1N4745
                    case 46 -> 18.0;   // 1N4746
                    case 47 -> 20.0;   // 1N4747
                    case 48 -> 22.0;   // 1N4748
                    case 49 -> 24.0;   // 1N4749
                    case 50 -> 27.0;   // 1N4750
                    case 51 -> 30.0;   // 1N4751
                    case 52 -> 33.0;   // 1N4752
                    case 53 -> 36.0;   // 1N4753
                    case 54 -> 39.0;   // 1N4754
                    case 55 -> 43.0;   // 1N4755
                    case 56 -> 47.0;   // 1N4756
                    case 57 -> 51.0;   // 1N4757
                    case 58 -> 56.0;   // 1N4758
                    case 59 -> 62.0;   // 1N4759
                    case 60 -> 68.0;   // 1N4760
                    case 61 -> 75.0;   // 1N4761
                    default -> 0.0;
                };
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private int getSchottkyVoltage(String mpn) {
        String base = getBasePart(mpn);
        return switch (base.toUpperCase()) {
            case "BAT54" -> 30;     // 30V (all variants: BAT54, BAT54S, BAT54C, BAT54A)
            case "BAT42" -> 30;     // 30V
            case "BAT43" -> 30;     // 30V
            case "BAT46" -> 100;    // 100V
            case "BAT48" -> 40;     // 40V
            case "BAT85" -> 30;     // 30V
            case "1N5817" -> 20;    // 20V
            case "1N5818" -> 30;    // 30V
            case "1N5819" -> 40;    // 40V
            default -> 0;
        };
    }
}