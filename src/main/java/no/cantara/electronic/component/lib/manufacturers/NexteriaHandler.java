package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for Nexperia components (formerly NXP Standard Products/Philips Semiconductors)
 */
public class NexteriaHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PSMN[0-9].*");         // N-channel power MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PSMP[0-9].*");         // P-channel power MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PMV[0-9].*");          // Small signal MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PJD[0-9].*");          // JFET series
        registry.addPattern(ComponentType.MOSFET, "^BUK[0-9].*");          // Legacy MOSFETs
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^PSMN[0-9].*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^PSMP[0-9].*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^PMV[0-9].*");

        // Transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PMBT[0-9].*");      // Small signal transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PBSS[0-9].*");      // Small signal transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PMP[0-9].*");       // Medium power transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PXN[0-9].*");       // High power transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^BC[0-9].*");        // Legacy transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^BF[0-9].*");        // Legacy transistors

        // Diodes
        registry.addPattern(ComponentType.DIODE, "^PMEG[0-9].*");          // Schottky rectifiers
        registry.addPattern(ComponentType.DIODE, "^BAS[0-9].*");           // Signal diodes
        registry.addPattern(ComponentType.DIODE, "^BAT[0-9].*");           // Schottky diodes
        registry.addPattern(ComponentType.DIODE, "^BZX[0-9].*");           // Zener diodes
        registry.addPattern(ComponentType.DIODE, "^PZU[0-9].*");           // Zener diodes

        // Logic ICs
        registry.addPattern(ComponentType.IC, "^74AHC.*");                 // Advanced HC logic
        registry.addPattern(ComponentType.IC, "^74AUC.*");                 // Advanced ultra-low-voltage CMOS
        registry.addPattern(ComponentType.IC, "^74HC.*");                  // High-speed CMOS
        registry.addPattern(ComponentType.IC, "^74HCT.*");                 // High-speed CMOS (TTL compatible)
        registry.addPattern(ComponentType.IC, "^74LVC.*");                 // Low-voltage CMOS

        // ESD Protection
        registry.addPattern(ComponentType.IC, "^PESD[0-9].*");            // ESD protection devices
        registry.addPattern(ComponentType.IC, "^PRTR[0-9].*");            // ESD protection arrays
        registry.addPattern(ComponentType.IC, "^IP4[0-9].*");             // ESD protection for interfaces

        // Interface ICs
        registry.addPattern(ComponentType.IC, "^PCA[0-9].*");             // I²C devices
        registry.addPattern(ComponentType.IC, "^PCF[0-9].*");             // Interface and control
        registry.addPattern(ComponentType.IC, "^PTN[0-9].*");             // Level translators
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MOSFET);
        types.add(ComponentType.MOSFET_NEXPERIA);
     //   types.add(ComponentType.LOGIC_IC);
        types.add(ComponentType.LOGIC_IC_NEXPERIA);
        types.add(ComponentType.ESD_PROTECTION_NEXPERIA);
        types.add(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA);
        // Add other Nexperia-specific types if they exist in ComponentType enum
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Power MOSFET packages
        if (upperMpn.startsWith("PSMN") || upperMpn.startsWith("PSMP")) {
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                String suffix = upperMpn.substring(lastDigitIndex + 1);
                return switch (suffix) {
                    case "T" -> "LFPAK56";
                    case "U" -> "LFPAK88";
                    case "V" -> "LFPAK33";
                    case "B" -> "SOT754";
                    case "L" -> "TO-220";
                    case "F" -> "TO-220F";
                    default -> suffix;
                };
            }
        }

        // Small signal packages
        if (upperMpn.startsWith("PMV") || upperMpn.startsWith("PMBT")) {
            // Extract package code from end of part number
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                String suffix = upperMpn.substring(lastDigitIndex + 1);
                return switch (suffix) {
                    case "T" -> "SOT23";
                    case "S" -> "SOT363";
                    case "U" -> "SOT323";
                    case "W" -> "SC70";
                    default -> suffix;
                };
            }
        }

        // Logic IC packages
        if (upperMpn.startsWith("74")) {
            String[] parts = upperMpn.split("_");
            if (parts.length > 1) {
                return switch (parts[1]) {
                    case "D" -> "SO14/SO16";
                    case "DB" -> "SSOP14/SSOP16";
                    case "PW" -> "TSSOP14/TSSOP16";
                    case "BQ" -> "DHVQFN14/DHVQFN16";
                    default -> parts[1];
                };
            }
        }

        return "";
    }

    private int findLastDigit(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // MOSFETs
        if (upperMpn.startsWith("PSMN")) return "PSMN Series";
        if (upperMpn.startsWith("PSMP")) return "PSMP Series";
        if (upperMpn.startsWith("PMV")) return "PMV Series";
        if (upperMpn.startsWith("PJD")) return "PJD Series";
        if (upperMpn.startsWith("BUK")) return "BUK Series";

        // Transistors
        if (upperMpn.startsWith("PMBT")) return "PMBT Series";
        if (upperMpn.startsWith("PBSS")) return "PBSS Series";
        if (upperMpn.startsWith("PMP")) return "PMP Series";
        if (upperMpn.startsWith("PXN")) return "PXN Series";

        // Diodes
        if (upperMpn.startsWith("PMEG")) return "PMEG Series";
        if (upperMpn.startsWith("BAS")) return "BAS Series";
        if (upperMpn.startsWith("BAT")) return "BAT Series";
        if (upperMpn.startsWith("BZX")) return "BZX Series";
        if (upperMpn.startsWith("PZU")) return "PZU Series";

        // Logic ICs
        if (upperMpn.startsWith("74")) {
            // Extract logic family (AHC, AUC, HC, HCT, LVC)
            for (String family : new String[]{"AHC", "AUC", "HCT", "HC", "LVC"}) {
                if (upperMpn.startsWith("74" + family)) {
                    return "74" + family;
                }
            }
            return "74 Series";
        }

        // ESD Protection
        if (upperMpn.startsWith("PESD")) return "PESD Series";
        if (upperMpn.startsWith("PRTR")) return "PRTR Series";
        if (upperMpn.startsWith("IP4")) return "IP4 Series";

        // Interface ICs
        if (upperMpn.startsWith("PCA")) return "PCA Series";
        if (upperMpn.startsWith("PCF")) return "PCF Series";
        if (upperMpn.startsWith("PTN")) return "PTN Series";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // Logic IC replacements
        if (series1.startsWith("74")) {
            // Different package variants of same logic function are usually compatible
            String base1 = mpn1.substring(0, mpn1.indexOf('_'));
            String base2 = mpn2.substring(0, mpn2.indexOf('_'));
            if (base1.equals(base2)) return true;

            // Cross-family compatibility (e.g., HC to HCT)
            String func1 = extractLogicFunction(mpn1);
            String func2 = extractLogicFunction(mpn2);
            if (func1.equals(func2)) {
                // HCT is backward compatible with HC
                if ((mpn1.contains("HC") && mpn2.contains("HCT")) ||
                        (mpn1.contains("HCT") && mpn2.contains("HC"))) {
                    return true;
                }
            }
        }

        // MOSFET replacements within same voltage/current class
        if (series1.equals("PSMN Series") || series1.equals("PSMP Series")) {
            // Compare voltage and current ratings if needed
            String base1 = mpn1.substring(0, findLastDigit(mpn1) + 1);
            String base2 = mpn2.substring(0, findLastDigit(mpn2) + 1);
            return base1.equals(base2);
        }

        return false;
    }

    private String extractLogicFunction(String mpn) {
        // Extract the actual logic function number from 74xxx parts
        // Example: 74HC00 -> "00", 74HCT04 -> "04"
        int startIndex = mpn.indexOf("74") + 2;
        while (startIndex < mpn.length() && !Character.isDigit(mpn.charAt(startIndex))) {
            startIndex++;
        }
        int endIndex = startIndex;
        while (endIndex < mpn.length() && Character.isDigit(mpn.charAt(endIndex))) {
            endIndex++;
        }
        return startIndex < endIndex ? mpn.substring(startIndex, endIndex) : "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}