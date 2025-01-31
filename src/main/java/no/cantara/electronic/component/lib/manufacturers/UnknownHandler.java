package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for unknown or unrecognized components.
 * Implements a fallback detection mechanism using generic patterns.
 */
public class UnknownHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Basic passive components
        registry.addPattern(ComponentType.RESISTOR, "^R[0-9].*");      // Generic resistor
        registry.addPattern(ComponentType.CAPACITOR, "^C[0-9].*");     // Generic capacitor
        registry.addPattern(ComponentType.INDUCTOR, "^L[0-9].*");      // Generic inductor

        // Basic semiconductor patterns
        registry.addPattern(ComponentType.DIODE, "^D[0-9].*");         // Generic diode
        registry.addPattern(ComponentType.DIODE, "^1N[0-9].*");        // Standard diode
        registry.addPattern(ComponentType.TRANSISTOR, "^Q[0-9].*");    // Generic transistor
        registry.addPattern(ComponentType.TRANSISTOR, "^2N[0-9].*");   // Standard transistor
        registry.addPattern(ComponentType.TRANSISTOR, "^BC[0-9].*");   // European transistor
        registry.addPattern(ComponentType.TRANSISTOR, "^BD[0-9].*");   // European power transistor

        // Generic IC patterns
        registry.addPattern(ComponentType.IC, "^IC[0-9].*");          // Generic IC
        registry.addPattern(ComponentType.IC, "^U[0-9].*");           // Generic IC (alternative)
        registry.addPattern(ComponentType.IC, "^74[0-9].*");          // 74 series logic
        registry.addPattern(ComponentType.IC, "^CD[0-9].*");          // CD4000 series

        // Crystal/oscillator patterns
        registry.addPattern(ComponentType.CRYSTAL, "^X[0-9].*");      // Generic crystal
        registry.addPattern(ComponentType.CRYSTAL, "^Y[0-9].*");      // Generic crystal (alternative)
        registry.addPattern(ComponentType.OSCILLATOR, "^OSC.*");      // Generic oscillator

        // LED patterns
        registry.addPattern(ComponentType.LED, "^LED.*");             // Generic LED
        registry.addPattern(ComponentType.LED, "^LD[0-9].*");        // Generic LED (alternative)
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.GENERIC);
        // For unknown manufacturers, we only support the generic component type
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Try to extract common package codes
        if (upperMpn.contains("-")) {
            String[] parts = upperMpn.split("-");
            String lastPart = parts[parts.length - 1];

            // Common SMD packages
            if (lastPart.matches("(0402|0603|0805|1206|1210|2010|2512)")) {
                return lastPart;
            }
            // Common through-hole packages
            if (lastPart.matches("(TO92|TO220|TO247|DIP[0-9]+|SOP[0-9]+|SOIC[0-9]+)")) {
                return lastPart;
            }
        }

        // Try to extract package code from the end of the MPN
        String[] commonPackages = {
                "SMD", "TH", "DIP", "SOIC", "QFP", "QFN", "BGA", "SOT23", "TO220", "TO247"
        };

        for (String pkg : commonPackages) {
            if (upperMpn.endsWith(pkg)) {
                return pkg;
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Try to identify common series
        if (upperMpn.startsWith("74")) return "74-SERIES";
        if (upperMpn.startsWith("CD4")) return "CD4000";
        if (upperMpn.startsWith("1N")) return "1N-DIODE";
        if (upperMpn.startsWith("2N")) return "2N-TRANSISTOR";
        if (upperMpn.startsWith("BC")) return "BC-TRANSISTOR";
        if (upperMpn.startsWith("BD")) return "BD-TRANSISTOR";

        // Extract prefix letters as series
        StringBuilder series = new StringBuilder();
        for (char c : upperMpn.toCharArray()) {
            if (Character.isLetter(c)) {
                series.append(c);
            } else {
                break;
            }
        }

        return series.length() > 0 ? series.toString() : "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        // For unknown components, check some common industry-standard replacements
        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Common transistor replacements
        if (upper1.equals("2N2222") && upper2.equals("PN2222")) return true;
        if (upper1.equals("PN2222") && upper2.equals("2N2222")) return true;
        if (upper1.equals("2N3904") && upper2.equals("BC547")) return true;
        if (upper1.equals("BC547") && upper2.equals("2N3904")) return true;

        // Common diode replacements
        if (upper1.equals("1N4148") && upper2.equals("1N914")) return true;
        if (upper1.equals("1N914") && upper2.equals("1N4148")) return true;
        if (upper1.equals("1N4001") && upper2.equals("1N4002")) return true;
        if (upper1.equals("1N4002") && upper2.equals("1N4001")) return true;

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as this handler deals with unknown/generic components
        return Collections.emptySet();
    }

    /**
     * Additional helper method to try to determine component type from generic patterns
     */
    public ComponentType guessComponentType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return ComponentType.GENERIC;
        String upperMpn = mpn.toUpperCase();

        // Check first character(s) for common component indicators
        if (upperMpn.startsWith("R") && Character.isDigit(getCharAt(upperMpn, 1)))
            return ComponentType.RESISTOR;
        if (upperMpn.startsWith("C") && Character.isDigit(getCharAt(upperMpn, 1)))
            return ComponentType.CAPACITOR;
        if (upperMpn.startsWith("L") && Character.isDigit(getCharAt(upperMpn, 1)))
            return ComponentType.INDUCTOR;
        if (upperMpn.startsWith("D") && Character.isDigit(getCharAt(upperMpn, 1)))
            return ComponentType.DIODE;
        if (upperMpn.startsWith("Q") && Character.isDigit(getCharAt(upperMpn, 1)))
            return ComponentType.TRANSISTOR;
        if (upperMpn.startsWith("LED"))
            return ComponentType.LED;
        if (upperMpn.startsWith("X") || upperMpn.startsWith("Y"))
            return ComponentType.CRYSTAL;
        if (upperMpn.startsWith("U") || upperMpn.startsWith("IC"))
            return ComponentType.IC;

        // Check common series
        if (upperMpn.startsWith("74") || upperMpn.startsWith("CD4"))
            return ComponentType.IC;
        if (upperMpn.startsWith("1N"))
            return ComponentType.DIODE;
        if (upperMpn.startsWith("2N") || upperMpn.startsWith("BC") || upperMpn.startsWith("BD"))
            return ComponentType.TRANSISTOR;

        return ComponentType.GENERIC;
    }

    private char getCharAt(String str, int index) {
        return index < str.length() ? str.charAt(index) : '\0';
    }

    /**
     * Additional helper method to get a description for unknown components
     */
    public String getGenericDescription(String mpn) {
        ComponentType guessedType = guessComponentType(mpn);
        String series = extractSeries(mpn);
        String package_ = extractPackageCode(mpn);

        StringBuilder description = new StringBuilder();
        description.append("Unknown ");
        description.append(guessedType.name().toLowerCase());

        if (!series.isEmpty()) {
            description.append(" (").append(series).append(" series)");
        }

        if (!package_.isEmpty()) {
            description.append(" in ").append(package_).append(" package");
        }

        return description.toString();
    }
}