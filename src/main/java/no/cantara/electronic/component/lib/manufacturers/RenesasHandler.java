package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for Renesas components
 */
public class RenesasHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // RX Series MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^R5F[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_RENESAS, "^R5F[0-9]+.*");
        registry.addPattern(ComponentType.MCU_RENESAS, "^R5F[0-9]+.*");

        // RL78 Series MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^R5F1[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_RENESAS, "^R5F1[0-9]+.*");
        registry.addPattern(ComponentType.MCU_RENESAS, "^R5F1[0-9]+.*");

        // RH850 Series MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^R7F[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_RENESAS, "^R7F[0-9]+.*");
        registry.addPattern(ComponentType.MCU_RENESAS, "^R7F[0-9]+.*");

        // RA Series MCUs (newer ARM-based)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^R7FA[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_RENESAS, "^R7FA[0-9]+.*");
        registry.addPattern(ComponentType.MCU_RENESAS, "^R7FA[0-9]+.*");

        // RE Series MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^R8C[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_RENESAS, "^R8C[0-9]+.*");
        registry.addPattern(ComponentType.MCU_RENESAS, "^R8C[0-9]+.*");

        // Memory products
        registry.addPattern(ComponentType.MEMORY, "^R1EX[0-9]+.*");    // Flash Memory
        registry.addPattern(ComponentType.MEMORY, "^R1LV[0-9]+.*");    // Low Voltage Memory
        registry.addPattern(ComponentType.MEMORY_RENESAS, "^R1[A-Z][0-9]+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MICROCONTROLLER,
            ComponentType.MICROCONTROLLER_RENESAS,
            ComponentType.MCU_RENESAS,
            ComponentType.MEMORY,
            ComponentType.MEMORY_RENESAS
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Strip version suffix if present (e.g., #V1, #30, #AA0)
        int hashIndex = upperMpn.indexOf('#');
        if (hashIndex >= 0) {
            upperMpn = upperMpn.substring(0, hashIndex);
        }

        // Example: R5F51303ADFN -> DFN package
        // Example: R7FA2A1AB3CFM -> FM package
        // Example: R7FA4M2AD3CFP -> CFP package (not LEAFB)
        int lastDigitIndex = getLastDigitIndex(upperMpn);
        if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(lastDigitIndex + 1);

            // Extract package code (2-3 letters, not the full suffix)
            // For MPNs like R7FA4M2AD3CFP, extract "CFP" (last 2-3 chars)
            // For MPNs like R5F51303ADFN, extract "DFN" (last 3 chars)
            if (suffix.length() >= 2) {
                // Common package codes are 2-3 characters
                int pkgLength = Math.min(3, suffix.length());
                // Try to extract the last 2-3 uppercase letters
                if (suffix.length() <= 4) {
                    return suffix;  // Short suffix, return as-is
                } else {
                    // Longer suffix, extract last 2-3 chars as package
                    return suffix.substring(suffix.length() - pkgLength);
                }
            }
            return suffix;
        }

        return "";
    }

    private int getLastDigitIndex(String str) {
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

        if (upperMpn.startsWith("R5F")) {
            if (upperMpn.startsWith("R5F1")) return "RL78";
            return "RX";
        }
        if (upperMpn.startsWith("R7F")) {
            if (upperMpn.startsWith("R7FA")) return "RA";
            return "RH850";
        }
        if (upperMpn.startsWith("R8C")) return "R8C";
        if (upperMpn.startsWith("R1EX")) return "Flash";
        if (upperMpn.startsWith("R1LV")) return "Low Voltage";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Strip version suffixes for comparison
        String clean1 = mpn1.toUpperCase().replaceAll("#.*$", "");
        String clean2 = mpn2.toUpperCase().replaceAll("#.*$", "");

        String series1 = extractSeries(clean1);
        String series2 = extractSeries(clean2);

        // Must be same series
        if (series1.isEmpty() || !series1.equals(series2)) return false;

        // For microcontrollers, check if same base part with different package
        // Example: R7FA4M2AD3CFP vs R7FA4M2AD3CFM (same part, different package)
        // Extract base part (everything before last 2-3 chars which is package)
        int lastDigit1 = getLastDigitIndex(clean1);
        int lastDigit2 = getLastDigitIndex(clean2);

        if (lastDigit1 >= 0 && lastDigit2 >= 0) {
            String base1 = clean1.substring(0, lastDigit1 + 1);
            String base2 = clean2.substring(0, lastDigit2 + 1);

            // Same base part number = package variant = official replacement
            return base1.equals(base2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}