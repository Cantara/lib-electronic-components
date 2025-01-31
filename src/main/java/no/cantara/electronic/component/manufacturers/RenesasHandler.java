package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;
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
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MICROCONTROLLER);
        types.add(ComponentType.MICROCONTROLLER_RENESAS);
        types.add(ComponentType.MCU_RENESAS);
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_RENESAS);
        // Add RX, RA, RE, RH, RZ series if they exist in ComponentType enum
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Example: R5F51303ADFN -> DFN package
        // Example: R7FA2A1AB3CFM -> FM package
        int lastDigitIndex = getLastDigitIndex(upperMpn);
        if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
            return upperMpn.substring(lastDigitIndex + 1);
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
        // Renesas typically doesn't have direct replacements between series
        // but might have pin-compatible parts within same series
        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}