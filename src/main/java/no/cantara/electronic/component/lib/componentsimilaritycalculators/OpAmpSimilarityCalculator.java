package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpAmpSimilarityCalculator implements ComponentSimilarityCalculator {

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
    private static final Map<String, OpAmpCharacteristics> CHARACTERISTICS = new HashMap<>();
    static {
        // Dual op-amps
        CHARACTERISTICS.put("LM358", new OpAmpCharacteristics(
                OpAmpCharacteristics.Function.DUAL,
                OpAmpCharacteristics.InputType.BIPOLAR,
                false,  // railToRail
                true,   // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("TL072", new OpAmpCharacteristics(
                OpAmpCharacteristics.Function.DUAL,
                OpAmpCharacteristics.InputType.JFET,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("MC1458", new OpAmpCharacteristics(
                OpAmpCharacteristics.Function.DUAL,
                OpAmpCharacteristics.InputType.BIPOLAR,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));

        // Quad op-amps
        CHARACTERISTICS.put("LM324", new OpAmpCharacteristics(
                OpAmpCharacteristics.Function.QUAD,
                OpAmpCharacteristics.InputType.BIPOLAR,
                false,  // railToRail
                true,   // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("TL074", new OpAmpCharacteristics(
                OpAmpCharacteristics.Function.QUAD,
                OpAmpCharacteristics.InputType.JFET,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));

        // Single op-amps
        CHARACTERISTICS.put("LM741", new OpAmpCharacteristics(
                OpAmpCharacteristics.Function.SINGLE,
                OpAmpCharacteristics.InputType.BIPOLAR,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));
        CHARACTERISTICS.put("TL071", new OpAmpCharacteristics(
                OpAmpCharacteristics.Function.SINGLE,
                OpAmpCharacteristics.InputType.JFET,
                false,  // railToRail
                false,  // lowPower
                false   // highSpeed
        ));
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        // Handle null type
        if (type == null) {
            return false;
        }// If it's a base IC type, we'll check if it's an op-amp in calculateSimilarity
        if (type == ComponentType.IC || type == ComponentType.ANALOG_IC) {
            return true;
        }
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

        System.out.println("Comparing op-amps: " + mpn1 + " vs " + mpn2);

        // Extract base part numbers (without package codes)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);
        System.out.println("Base part numbers: " + base1 + " and " + base2);

        // Same exact part
        if (base1.equals(base2)) {
            String pkg1 = extractPackageCode(mpn1);
            String pkg2 = extractPackageCode(mpn2);
            if (areCompatiblePackages(pkg1, pkg2)) {
                System.out.println("Same part, compatible packages - similarity: " + HIGH_SIMILARITY);
                return HIGH_SIMILARITY;
            }
        }

        // Extract families (LM358, MC1458, etc.)
        String family1 = extractFamily(base1);
        String family2 = extractFamily(base2);
        System.out.println("Families: " + family1 + " and " + family2);

        // Check for equivalent families
        if (areEquivalentFamilies(family1, family2)) {
            System.out.println("Equivalent families with characteristics - similarity: " + HIGH_SIMILARITY);
            return HIGH_SIMILARITY;
        }

        // Same function (dual, quad, etc.)
        if (haveSameFunction(family1, family2)) {
            System.out.println("Same function type - similarity: " + MEDIUM_SIMILARITY);
            return MEDIUM_SIMILARITY;
        }

        System.out.println("Both are op-amps - similarity: " + LOW_SIMILARITY);
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

}