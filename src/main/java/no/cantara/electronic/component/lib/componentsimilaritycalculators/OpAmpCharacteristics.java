package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OpAmpCharacteristics {
    // Function types
    public enum Function {
        SINGLE,
        DUAL,
        QUAD,
        TRIPLE
    }

    public static Function fromMPN(String mpn) {
        if (mpn == null) return null;
        String base = mpn.toUpperCase();
        if (base.matches(".*(?:358|072|082|5532).*")) return Function.DUAL;
        if (base.matches(".*(?:324|3403|4136).*")) return Function.QUAD;
        if (base.matches(".*(?:741|748).*")) return Function.SINGLE;
        return null;
    }
    // Input types
    public enum InputType {
        BIPOLAR,
        JFET,
        CMOS
    }

    // Specs categories
    private final Function function;
    private final InputType inputType;
    private final boolean railToRail;
    private final boolean lowPower;
    private final boolean highSpeed;

    public OpAmpCharacteristics(Function function, InputType inputType, boolean railToRail,
                                 boolean lowPower, boolean highSpeed) {
        this.function = function;
        this.inputType = inputType;
        this.railToRail = railToRail;
        this.lowPower = lowPower;
        this.highSpeed = highSpeed;
    }

    // Map of known op-amps and their characteristics
    private static final Map<String, OpAmpCharacteristics> KNOWN_OPAMPS = new HashMap<>();
    static {
        // Standard dual op-amps
        KNOWN_OPAMPS.put("LM358", new OpAmpCharacteristics(
                Function.DUAL, InputType.BIPOLAR, false, true, false));
        KNOWN_OPAMPS.put("MC1458", new OpAmpCharacteristics(
                Function.DUAL, InputType.BIPOLAR, false, false, false));
        KNOWN_OPAMPS.put("LM1458", new OpAmpCharacteristics(
                Function.DUAL, InputType.BIPOLAR, false, false, false));
        KNOWN_OPAMPS.put("RC4558", new OpAmpCharacteristics(
                Function.DUAL, InputType.BIPOLAR, false, false, false));

        // JFET input op-amps
        KNOWN_OPAMPS.put("TL072", new OpAmpCharacteristics(
                Function.DUAL, InputType.JFET, false, false, false));
        KNOWN_OPAMPS.put("TL082", new OpAmpCharacteristics(
                Function.DUAL, InputType.JFET, false, false, false));

        // Quad op-amps
        KNOWN_OPAMPS.put("LM324", new OpAmpCharacteristics(
                Function.QUAD, InputType.BIPOLAR, false, true, false));
        KNOWN_OPAMPS.put("MC3403", new OpAmpCharacteristics(
                Function.QUAD, InputType.BIPOLAR, false, false, false));
        KNOWN_OPAMPS.put("RC4136", new OpAmpCharacteristics(
                Function.QUAD, InputType.BIPOLAR, false, false, false));

        // High performance op-amps
        KNOWN_OPAMPS.put("NE5532", new OpAmpCharacteristics(
                Function.DUAL, InputType.BIPOLAR, false, false, true));
    }

    // Known equivalent families
    private static final Map<String, Set<String>> EQUIVALENT_FAMILIES = new HashMap<>();
    static {
        // Standard dual op-amps (similar specs)
        EQUIVALENT_FAMILIES.put("LM358", Set.of("MC1458", "LM1458", "RC4558"));
        EQUIVALENT_FAMILIES.put("MC1458", Set.of("LM358", "LM1458", "RC4558"));
        EQUIVALENT_FAMILIES.put("LM1458", Set.of("LM358", "MC1458", "RC4558"));
        EQUIVALENT_FAMILIES.put("RC4558", Set.of("LM358", "MC1458", "LM1458"));

        // JFET input op-amps
        EQUIVALENT_FAMILIES.put("TL072", Set.of("TL082"));
        EQUIVALENT_FAMILIES.put("TL082", Set.of("TL072"));

        // Quad op-amps
        EQUIVALENT_FAMILIES.put("LM324", Set.of("MC3403", "RC4136"));
        EQUIVALENT_FAMILIES.put("MC3403", Set.of("LM324", "RC4136"));
        EQUIVALENT_FAMILIES.put("RC4136", Set.of("LM324", "MC3403"));
    }

    public static String extractBaseFamily(String mpn) {
        if (mpn == null) return "";
        // Remove package codes and variations
        String base = mpn.replaceAll("(?:N|D|P|DG|PW|DR|DGK|DBV|DRG4)$", "");
        // Remove suffixes like A, B, C
        return base.replaceAll("[A-Z]*$", "");
    }

    public static boolean areEquivalent(String mpn1, String mpn2) {
        String family1 = extractBaseFamily(mpn1);
        String family2 = extractBaseFamily(mpn2);

        // Check direct equivalence
        if (family1.equals(family2)) return true;

        // Check known equivalent families
        Set<String> equivalents = EQUIVALENT_FAMILIES.get(family1);
        return equivalents != null && equivalents.contains(family2);
    }

    public static double calculateSimilarity(String mpn1, String mpn2) {
        String family1 = extractBaseFamily(mpn1);
        String family2 = extractBaseFamily(mpn2);

        OpAmpCharacteristics char1 = KNOWN_OPAMPS.get(family1);
        OpAmpCharacteristics char2 = KNOWN_OPAMPS.get(family2);

        if (char1 == null || char2 == null) return 0.0;

        double similarity = 0.0;

        // Same exact family
        if (family1.equals(family2)) {
            return 0.9;
        }

        // Known equivalent families
        if (areEquivalent(mpn1, mpn2)) {
            return 0.9;
        }

        // Same function type (dual, quad, etc.)
        if (char1.function == char2.function) {
            similarity += 0.3;
        }

        // Same input type
        if (char1.inputType == char2.inputType) {
            similarity += 0.2;
        }

        // Similar performance characteristics
        if (char1.lowPower == char2.lowPower) similarity += 0.1;
        if (char1.highSpeed == char2.highSpeed) similarity += 0.1;
        if (char1.railToRail == char2.railToRail) similarity += 0.1;

        return Math.min(similarity, 0.9);
    }

    public static boolean isSameFunction(String mpn1, String mpn2) {
        String family1 = extractBaseFamily(mpn1);
        String family2 = extractBaseFamily(mpn2);

        OpAmpCharacteristics char1 = KNOWN_OPAMPS.get(family1);
        OpAmpCharacteristics char2 = KNOWN_OPAMPS.get(family2);

        return char1 != null && char2 != null && char1.function == char2.function;
    }
}