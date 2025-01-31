package no.cantara.electronic.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class PrefixRegistry {
    private final Map<String, Set<String>> mosfetPrefixGroups;
    private final Map<String, Set<String>> opAmpPrefixGroups;
    private final Map<String, Set<String>> diodePrefixGroups;
    private final Map<String, Set<String>> passivePrefixGroups;

    public PrefixRegistry() {
        mosfetPrefixGroups = initializeMosfetPrefixGroups();
        opAmpPrefixGroups = initializeOpAmpPrefixGroups();
        diodePrefixGroups = initializeDiodePrefixGroups();
        passivePrefixGroups = initializePassivePrefixGroups();
    }

    private Map<String, Set<String>> initializeMosfetPrefixGroups() {
        Map<String, Set<String>> groups = new HashMap<>();

        // N-Channel MOSFETs
        groups.put("IRF", new HashSet<>(Arrays.asList("STF", "FQP", "SIHF", "FDP")));
        groups.put("IRL", new HashSet<>(Arrays.asList("FDN", "FDS", "BSS")));

        // P-Channel MOSFETs
        groups.put("IRP", new HashSet<>(Arrays.asList("FQD", "STP", "BSS")));

        // Dual MOSFETs
        groups.put("IRF7", new HashSet<>(Arrays.asList("FDS7", "SI7", "BSS7")));

        return groups;
    }

    private Map<String, Set<String>> initializeOpAmpPrefixGroups() {
        Map<String, Set<String>> groups = new HashMap<>();

        // General Purpose Op-Amps
        groups.put("LM", new HashSet<>(Arrays.asList("MC", "CA", "UA")));
        groups.put("TL", new HashSet<>(Arrays.asList("UA", "RC", "LF")));

        // Precision Op-Amps
        groups.put("OP", new HashSet<>(Arrays.asList("AD", "OPA", "MCP")));
        groups.put("AD", new HashSet<>(Arrays.asList("OP", "OPA", "MCP")));

        // High-Speed Op-Amps
        groups.put("LT", new HashSet<>(Arrays.asList("AD8", "OPA8", "THS")));

        return groups;
    }

    private Map<String, Set<String>> initializeDiodePrefixGroups() {
        Map<String, Set<String>> groups = new HashMap<>();

        // Rectifier Diodes
        groups.put("1N", new HashSet<>(Arrays.asList("US", "ES", "RS")));
        groups.put("MBR", new HashSet<>(Arrays.asList("SB", "SS", "VS")));

        // Signal Diodes
        groups.put("1N4148", new HashSet<>(Arrays.asList("LL4148", "1SS", "BAV")));
        groups.put("BAT", new HashSet<>(Arrays.asList("BAT41", "BAT42", "BAT43")));

        // Schottky Diodes
        groups.put("BAT", new HashSet<>(Arrays.asList("MBR", "SB", "SS")));

        // Zener Diodes
        groups.put("BZX", new HashSet<>(Arrays.asList("1N47", "1N48", "1N49")));

        return groups;
    }

    private Map<String, Set<String>> initializePassivePrefixGroups() {
        Map<String, Set<String>> groups = new HashMap<>();

        // Resistors
        groups.put("RC", new HashSet<>(Arrays.asList("ERJ", "CRCW", "WCR")));
        groups.put("MFR", new HashSet<>(Arrays.asList("CFR", "PR", "MRS")));

        // Ceramic Capacitors
        groups.put("GRM", new HashSet<>(Arrays.asList("C0G", "X7R", "CGA")));
        groups.put("C0G", new HashSet<>(Arrays.asList("NP0", "GRM", "CGA")));

        // Electrolytic Capacitors
        groups.put("EEE", new HashSet<>(Arrays.asList("UUD", "ECE", "RVT")));

        // Inductors
        groups.put("SRN", new HashSet<>(Arrays.asList("SRP", "SRR", "SDR")));
        groups.put("MLF", new HashSet<>(Arrays.asList("LQH", "SLF", "DFE")));

        return groups;
    }

    public boolean arePrefixesRelated(String prefix1, String prefix2, ComponentType type) {
        Map<String, Set<String>> relevantGroups = switch (type.getBaseType()) {
            case MOSFET -> mosfetPrefixGroups;
            case OPAMP -> opAmpPrefixGroups;
            case DIODE -> diodePrefixGroups;
            case RESISTOR, CAPACITOR, INDUCTOR -> passivePrefixGroups;
            default -> new HashMap<>();
        };

        return arePrefixesRelated(prefix1, prefix2, relevantGroups);
    }

    public boolean areMosfetPrefixesRelated(String prefix1, String prefix2) {
        return arePrefixesRelated(prefix1, prefix2, mosfetPrefixGroups);
    }

    public boolean areOpAmpPrefixesRelated(String prefix1, String prefix2) {
        return arePrefixesRelated(prefix1, prefix2, opAmpPrefixGroups);
    }

    public boolean areDiodePrefixesRelated(String prefix1, String prefix2) {
        return arePrefixesRelated(prefix1, prefix2, diodePrefixGroups);
    }

    public boolean arePassivePrefixesRelated(String prefix1, String prefix2) {
        return arePrefixesRelated(prefix1, prefix2, passivePrefixGroups);
    }

    private boolean arePrefixesRelated(String prefix1, String prefix2,
                                       Map<String, Set<String>> prefixGroups) {
        if (prefix1 == null || prefix2 == null) return false;
        if (prefix1.equals(prefix2)) return true;

        // Check direct relationships
        Set<String> related1 = prefixGroups.get(prefix1);
        if (related1 != null && related1.contains(prefix2)) return true;

        // Check reverse relationships
        Set<String> related2 = prefixGroups.get(prefix2);
        if (related2 != null && related2.contains(prefix1)) return true;

        // Check transitive relationships
        for (Map.Entry<String, Set<String>> entry : prefixGroups.entrySet()) {
            Set<String> group = entry.getValue();
            if ((group.contains(prefix1) && group.contains(prefix2)) ||
                    (prefix1.equals(entry.getKey()) && group.contains(prefix2)) ||
                    (prefix2.equals(entry.getKey()) && group.contains(prefix1))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all related prefixes for a given prefix
     */
    public Set<String> getAllRelatedPrefixes(String prefix, ComponentType type) {
        Set<String> relatedPrefixes = new HashSet<>();
        Map<String, Set<String>> relevantGroups = switch (type.getBaseType()) {
            case MOSFET -> mosfetPrefixGroups;
            case OPAMP -> opAmpPrefixGroups;
            case DIODE -> diodePrefixGroups;
            case RESISTOR, CAPACITOR, INDUCTOR -> passivePrefixGroups;
            default -> new HashMap<>();
        };

        // Add the prefix itself
        relatedPrefixes.add(prefix);

        // Add directly related prefixes
        Set<String> directlyRelated = relevantGroups.get(prefix);
        if (directlyRelated != null) {
            relatedPrefixes.addAll(directlyRelated);
        }

        // Add reverse relationships
        for (Map.Entry<String, Set<String>> entry : relevantGroups.entrySet()) {
            if (entry.getValue().contains(prefix)) {
                relatedPrefixes.add(entry.getKey());
                relatedPrefixes.addAll(entry.getValue());
            }
        }

        return relatedPrefixes;
    }
}