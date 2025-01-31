package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class LogicICSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Pattern TTL_PATTERN = Pattern.compile("([0-9]+)([A-Z]+)([0-9]+).*");
    private static final Pattern CMOS_PATTERN = Pattern.compile("CD([0-9]+)([A-Z]*).*");

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.5;
    private static final double LOW_SIMILARITY = 0.3;

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
            System.out.println("LogicICSimilarityCalculator: type is null");
            return false;
        }

        System.out.println("LogicICSimilarityCalculator checking applicability for type: " + type);

        // Check if it's an IC and if the component being checked is a logic IC
        if (type == ComponentType.IC) {
            System.out.println("LogicICSimilarityCalculator: Handling generic IC type");
            return true;
        }

        boolean result = type == ComponentType.LOGIC_IC ||
                type == ComponentType.LOGIC_IC_NEXPERIA ||
                type == ComponentType.LOGIC_IC_DIODES;

        System.out.println("LogicICSimilarityCalculator: Is applicable for " + type + "? " + result);
        return result;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        System.out.println("Comparing logic ICs: " + mpn1 + " vs " + mpn2);

        // CD4000 series comparison
        if (mpn1.matches("^CD4.*") || mpn2.matches("^CD4.*")) {
            Matcher m1 = CMOS_PATTERN.matcher(mpn1);
            Matcher m2 = CMOS_PATTERN.matcher(mpn2);

            if (m1.matches() && m2.matches()) {
                String base1 = m1.group(1);  // The numbers after CD4
                String base2 = m2.group(1);

                System.out.println("CD4000 series bases: " + base1 + " vs " + base2);

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

        System.out.println("Base parts: " + base1 + " and " + base2);

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
        System.out.println("Comparing CD4000 series: " + mpn1 + " vs " + mpn2);

        // Extract base part (e.g., CD4001 from CD4001BE)
        String base1 = mpn1.replaceAll("(?:BE|BM|UBE|N|P|DG|PW|DR|DGK|DBV|DRG4)$", "");
        String base2 = mpn2.replaceAll("(?:BE|BM|UBE|N|P|DG|PW|DR|DGK|DBV|DRG4)$", "");

        System.out.println("Base parts: " + base1 + " vs " + base2);

        if (base1.equals(base2)) {
            System.out.println("Same CD4000 series IC with different package codes");
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