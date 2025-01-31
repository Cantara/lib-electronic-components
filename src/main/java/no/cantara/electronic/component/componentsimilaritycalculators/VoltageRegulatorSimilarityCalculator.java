package no.cantara.electronic.component.componentsimilaritycalculators;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoltageRegulatorSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

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

        System.out.println("Comparing voltage regulators: " + mpn1 + " vs " + mpn2);

        // Handle adjustable voltage regulators first
        if (isAdjustableRegulator(mpn1) && isAdjustableRegulator(mpn2)) {
            // Extract base parts without package codes
            String base1 = extractBasePart(mpn1);
            String base2 = extractBasePart(mpn2);

            if (base1.equals(base2)) {
                System.out.println("Same adjustable regulator with different packages");
                return HIGH_SIMILARITY;
            }

            // Check compatible families (LM317/LM350/LM338)
            if (areCompatibleAdjustableRegulators(base1, base2)) {
                System.out.println("Compatible adjustable regulators");
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
                    System.out.println("Same fixed regulator with different package codes");
                    return HIGH_SIMILARITY;
                }
                System.out.println("Compatible fixed regulators. Voltages: " + voltage1 + " vs " + voltage2);
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
}