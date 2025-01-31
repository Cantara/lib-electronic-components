package no.cantara.electronic.component.componentsimilaritycalculators;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.PatternRegistry;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class MosfetSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    // Known equivalent groups
    private static final Map<String, Set<String>> EQUIVALENT_GROUPS = new HashMap<>();
    static {
        // N-Channel MOSFETs - Include both base parts and variants
        EQUIVALENT_GROUPS.put("IRF530", Set.of("IRF530", "IRF530N", "STF530", "STF530N", "FQP30N06"));
        EQUIVALENT_GROUPS.put("STF530", Set.of("IRF530", "IRF530N", "STF530", "STF530N", "FQP30N06"));
        EQUIVALENT_GROUPS.put("IRF540", Set.of("IRF540", "IRF540N", "STF540", "STF540N", "FQP50N06"));
        EQUIVALENT_GROUPS.put("IRF640", Set.of("IRF640", "IRF640N", "STF640", "STF640N", "FQP44N10"));
    }

    private static final Map<String, MosfetCharacteristics> KNOWN_CHARACTERISTICS = new HashMap<>();
    static {
        // IRF530 series - include base parts and variants
        KNOWN_CHARACTERISTICS.put("IRF530", new MosfetCharacteristics(100, 14, 0.16, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("STF530", new MosfetCharacteristics(100, 14, 0.16, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("IRF530N", new MosfetCharacteristics(100, 17, 0.11, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("STF530N", new MosfetCharacteristics(100, 17, 0.11, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("FQP30N06", new MosfetCharacteristics(60, 30, 0.095, true, "TO-220"));

        // IRF540 series
        KNOWN_CHARACTERISTICS.put("IRF540", new MosfetCharacteristics(100, 28, 0.077, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("STF540", new MosfetCharacteristics(100, 28, 0.077, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("IRF540N", new MosfetCharacteristics(100, 33, 0.052, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("STF540N", new MosfetCharacteristics(100, 33, 0.052, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("FQP50N06", new MosfetCharacteristics(60, 50, 0.040, true, "TO-220"));

        // IRF640 series
        KNOWN_CHARACTERISTICS.put("IRF640", new MosfetCharacteristics(200, 18, 0.150, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("STF640", new MosfetCharacteristics(200, 18, 0.150, true, "TO-220"));
        KNOWN_CHARACTERISTICS.put("FQP44N10", new MosfetCharacteristics(100, 44, 0.085, true, "TO-220"));
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        // Check for null type to handle unrecognized MOSFETs
        if (type == null) {
            return true;
        }
        return type == ComponentType.MOSFET ||
                type.name().startsWith("MOSFET_") ||
                type.getBaseType() == ComponentType.MOSFET;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null) {
            return 0.0;
        }

        System.out.println("Comparing MOSFETs: " + mpn1 + " vs " + mpn2);

        // First check if these are MOSFETs
        if (!isMosfet(mpn1) || !isMosfet(mpn2)) {
            System.out.println("One or both parts are not MOSFETs");
            return 0.0;
        }

        // Check polarity FIRST
        boolean isN1 = isNChannel(mpn1);
        boolean isN2 = isNChannel(mpn2);

        System.out.println("N-Channel: " + isN1 + " and " + isN2);

        // Different polarities are not compatible
        if (isN1 != isN2) {
            System.out.println("Different polarities - incompatible");
            return LOW_SIMILARITY;
        }

        // Extract base part numbers (without suffixes)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        System.out.println("Base parts: " + base1 + " and " + base2);

        // Check known equivalent groups
        if (areKnownEquivalents(base1, base2)) {
            System.out.println("Known equivalents - high similarity");
            return 0.9;
        }

        // Get characteristics
        MosfetCharacteristics char1 = getCharacteristics(mpn1);
        MosfetCharacteristics char2 = getCharacteristics(mpn2);

        if (char1 != null && char2 != null) {
            return calculateCharacteristicsSimilarity(char1, char2);
        }

        return MEDIUM_SIMILARITY;
        // If characteristics not found, use simpler comparison
        //return calculateBasicSimilarity(base1, base2, isN1);
    }

    private boolean isMosfet(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        // Extended MOSFET pattern matching
        return upperMpn.matches("^(IRF|IRL|FQ[PNS]|FDS|STF|STP|SI|BS|FD|NTD|NTB|NDS|NDF)[0-9].*");
    }

    private boolean isNChannel(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();

        // P-channel identifiers first
        if (upperMpn.startsWith("IRF9")) return false;  // IRF9xxx series are P-channel
        if (upperMpn.startsWith("FQP")) return true;    // FQP are N-channel
        if (upperMpn.startsWith("FQN")) return true;    // FQN are N-channel
        if (upperMpn.contains("P") && !upperMpn.startsWith("FQP")) return false;  // P suffix/infix (except FQP prefix)

        // N-channel identifiers
        if (upperMpn.startsWith("IRF") || upperMpn.startsWith("IRL")) return true;  // IRF/IRL series (except IRF9xxx)
        if (upperMpn.startsWith("FDS") || upperMpn.startsWith("NTD")) return true;  // These series are N-channel
        if (upperMpn.startsWith("STF") && !upperMpn.contains("P")) return true;     // STF series (unless marked P)

        return true;  // Default to N-channel if unclear
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return null;

        // Remove suffixes but keep core part number
        String upperMpn = mpn.toUpperCase();

        // Handle different manufacturer prefixes
        for (String prefix : new String[]{"IRF", "STF", "FQP", "FQN", "FDS", "NTD"}) {
            if (upperMpn.startsWith(prefix)) {
                // Find where the numeric part ends
                int i = prefix.length();
                StringBuilder base = new StringBuilder(prefix);
                while (i < upperMpn.length() &&
                        (Character.isDigit(upperMpn.charAt(i)) || upperMpn.charAt(i) == 'N')) {
                    base.append(upperMpn.charAt(i));
                    i++;
                }
                return base.toString();
            }
        }
        return mpn;
    }

    private String stripSuffix(String mpn) {
        if (mpn == null) return null;
        // Remove N suffix but keep base part number
        return mpn.replaceAll("N$", "");
    }

    private boolean areKnownEquivalents(String mpn1, String mpn2) {
        // First try exact match
        for (Set<String> group : EQUIVALENT_GROUPS.values()) {
            if (matchesGroup(mpn1, group) && matchesGroup(mpn2, group)) {
                return true;
            }
        }

        // Try base part numbers (without N suffix)
        String base1 = stripSuffix(mpn1);
        String base2 = stripSuffix(mpn2);

        for (Set<String> group : EQUIVALENT_GROUPS.values()) {
            Set<String> baseParts = group.stream()
                    .map(this::stripSuffix)
                    .collect(Collectors.toSet());
            if (baseParts.contains(base1) && baseParts.contains(base2)) {
                return true;
            }
        }

        return false;
    }

    private boolean matchesGroup(String mpn, Set<String> group) {
        if (mpn == null) return false;
        String baseMpn = extractBasePart(mpn);
        // Try exact match first
        if (group.contains(baseMpn)) return true;
        // Try without N suffix
        String strippedMpn = stripSuffix(baseMpn);
        return group.stream()
                .map(this::stripSuffix)
                .anyMatch(strippedMpn::equals);
    }

    private MosfetCharacteristics getCharacteristics(String mpn) {
        // Try exact match first
        MosfetCharacteristics exact = KNOWN_CHARACTERISTICS.get(mpn);
        if (exact != null) return exact;

        // Try base part match
        String base = extractBasePart(mpn);
        MosfetCharacteristics baseMatch = KNOWN_CHARACTERISTICS.get(base);
        if (baseMatch != null) return baseMatch;

        // Try stripped version (without N suffix)
        return KNOWN_CHARACTERISTICS.get(stripSuffix(base));
    }

    private double calculateCharacteristicsSimilarity(MosfetCharacteristics char1,
                                                      MosfetCharacteristics char2) {
        double similarity = 0.3;  // Base similarity for matching MOSFET type

        // Voltage rating compatibility (within 20%)
        if (areVoltageRatingsCompatible(char1.voltageRating, char2.voltageRating)) {
            similarity += 0.25;  // Increased weight for voltage match
        }

        // Current rating compatibility (within 20%)
        if (areCurrentRatingsCompatible(char1.currentRating, char2.currentRating)) {
            similarity += 0.25;  // Increased weight for current match
        }

        // Rds(on) compatibility (within 50%)
        if (areRdsOnCompatible(char1.rdsOn, char2.rdsOn)) {
            similarity += 0.1;
        }

        // Package compatibility
        if (arePackagesCompatible(char1.packageType, char2.packageType)) {
            similarity += 0.1;
        }

        return similarity;
    }

    private double calculateBasicSimilarity(String base1, String base2, boolean isNChannel) {
        if (base1 == null || base2 == null) return 0.0;

        double similarity = 0.3;  // Base similarity for matching MOSFET type and polarity

        // Same base part (ignoring N suffix)
        if (stripSuffix(base1).equals(stripSuffix(base2))) {
            similarity += 0.3;
            return similarity;
        }

        // Same manufacturer family
        if (base1.substring(0, 3).equals(base2.substring(0, 3))) {
            similarity += 0.2;
        }

        // Similar current rating (based on part number)
        try {
            int rating1 = extractCurrentRating(base1);
            int rating2 = extractCurrentRating(base2);
            if (Math.min(rating1, rating2) > 0 &&
                    Math.abs(rating1 - rating2) <= Math.max(rating1, rating2) * 0.2) {
                similarity += 0.2;
            }
        } catch (NumberFormatException e) {
            // Ignore if cannot parse ratings
        }

        return similarity;
    }

    private int extractCurrentRating(String basePart) {
        // Extract numeric part that typically indicates current rating
        String numbers = basePart.replaceAll("[^0-9]", "");
        if (numbers.length() >= 2) {
            return Integer.parseInt(numbers.substring(0, 2));
        }
        return 0;
    }

    private boolean areVoltageRatingsCompatible(double v1, double v2) {
        if (v1 == 0 || v2 == 0) return false;
        return Math.min(v1, v2) >= 0.8 * Math.max(v1, v2);
    }

    private boolean areCurrentRatingsCompatible(double i1, double i2) {
        if (i1 == 0 || i2 == 0) return false;
        return Math.min(i1, i2) >= 0.8 * Math.max(i1, i2);
    }

    private boolean areRdsOnCompatible(double r1, double r2) {
        if (r1 == 0 || r2 == 0) return false;
        return Math.max(r1, r2) <= 1.5 * Math.min(r1, r2);
    }

    private boolean arePackagesCompatible(String pkg1, String pkg2) {
        if (pkg1 == null || pkg2 == null) return false;
        if (pkg1.equals(pkg2)) return true;

        // TO-220 packages are often compatible
        if (pkg1.startsWith("TO-220") && pkg2.startsWith("TO-220")) return true;

        // SOT-223 and DPAK are often compatible
        Set<String> smallPowerPackages = new HashSet<>(Set.of("SOT-223", "DPAK", "TO-252"));
        return smallPowerPackages.contains(pkg1) && smallPowerPackages.contains(pkg2);
    }

    private static class MosfetCharacteristics {
        final double voltageRating;  // Vds in volts
        final double currentRating;  // Id in amps
        final double rdsOn;         // Rds(on) in ohms
        final boolean isNChannel;    // true for N-channel, false for P-channel
        final String packageType;    // Package type

        MosfetCharacteristics(double voltageRating, double currentRating,
                              double rdsOn, boolean isNChannel, String packageType) {
            this.voltageRating = voltageRating;
            this.currentRating = currentRating;
            this.rdsOn = rdsOn;
            this.isNChannel = isNChannel;
            this.packageType = packageType;
        }

        @Override
        public String toString() {
            return String.format("%s-channel: V=%.0fV, I=%.1fA, Rds=%.3fΩ, Pkg=%s",
                    isNChannel ? "N" : "P", voltageRating, currentRating, rdsOn, packageType);
        }
    }
}