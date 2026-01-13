package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Handler for Cypress Semiconductor components
 */
public class CypressHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // PSoC MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CY8C[0-9]+.*");        // PSoC general
        registry.addPattern(ComponentType.MICROCONTROLLER_CYPRESS, "^CY8C[0-9]+.*");
        registry.addPattern(ComponentType.MCU_CYPRESS, "^CY8C[0-9]+.*");

        // PSoC specific families
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CY8C4[0-9]+.*");       // PSoC 4
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CY8C5[0-9]+.*");       // PSoC 5
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CY8C6[0-9]+.*");       // PSoC 6

        // Memory products
        registry.addPattern(ComponentType.MEMORY, "^CY14B[0-9]+.*");               // nvSRAM
        registry.addPattern(ComponentType.MEMORY, "^CY62[0-9]+.*");                // SRAM
        registry.addPattern(ComponentType.MEMORY_CYPRESS, "^CY14B[0-9]+.*");
        registry.addPattern(ComponentType.MEMORY_CYPRESS, "^CY62[0-9]+.*");

        // USB Controllers
        registry.addPattern(ComponentType.IC, "^CY7C[0-9]+.*");                   // USB Controllers
        registry.addPattern(ComponentType.IC, "^CYUSB[0-9]+.*");                  // USB 3.0 Controllers

        // Bluetooth/Wireless
        registry.addPattern(ComponentType.IC, "^CYW[0-9]+.*");                    // Wireless combo chips
        registry.addPattern(ComponentType.IC, "^CYBL[0-9]+.*");                   // Bluetooth Low Energy

        // Touch sensing
        registry.addPattern(ComponentType.IC, "^CY8CMBR[0-9]+.*");               // CapSense Controllers
        registry.addPattern(ComponentType.IC, "^CY8CTECH[0-9]+.*");              // TrueTouch Controllers

        // Power Management
        registry.addPattern(ComponentType.IC, "^CCG[0-9]+.*");                   // USB-C Controllers
        registry.addPattern(ComponentType.IC, "^CYPD[0-9]+.*");                  // USB-PD Controllers
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MICROCONTROLLER,
            ComponentType.MICROCONTROLLER_CYPRESS,
            ComponentType.MCU_CYPRESS,
            ComponentType.MEMORY,
            ComponentType.MEMORY_CYPRESS,
            ComponentType.PSOC_MCU,
            ComponentType.FM_SERIES_MCU,
            ComponentType.TRAVEO_MCU
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // PSoC MCU packages
        if (upperMpn.startsWith("CY8C")) {
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                String suffix = upperMpn.substring(lastDigitIndex + 1);
                return switch (suffix) {
                    case "LP" -> "LQFP";
                    case "TM" -> "TQFP";
                    case "BX" -> "BGA";
                    case "QF" -> "QFN";
                    default -> suffix;
                };
            }
        }

        // Memory packages
        if (upperMpn.startsWith("CY14B") || upperMpn.startsWith("CY62")) {
            int dashIndex = upperMpn.lastIndexOf('-');
            if (dashIndex >= 0 && dashIndex < upperMpn.length() - 1) {
                return upperMpn.substring(dashIndex + 1);
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

        // PSoC series
        if (upperMpn.startsWith("CY8C")) {
            if (upperMpn.startsWith("CY8C4")) return "PSoC 4";
            if (upperMpn.startsWith("CY8C5")) return "PSoC 5";
            if (upperMpn.startsWith("CY8C6")) return "PSoC 6";
            return "PSoC";
        }

        // Memory series
        if (upperMpn.startsWith("CY14B")) return "nvSRAM";
        if (upperMpn.startsWith("CY62")) return "SRAM";

        // USB series
        if (upperMpn.startsWith("CY7C")) return "USB Controller";
        if (upperMpn.startsWith("CYUSB")) return "USB 3.0 Controller";

        // Wireless series
        if (upperMpn.startsWith("CYW")) return "Wireless Combo";
        if (upperMpn.startsWith("CYBL")) return "Bluetooth LE";

        // Touch sensing series
        if (upperMpn.startsWith("CY8CMBR")) return "CapSense";
        if (upperMpn.startsWith("CY8CTECH")) return "TrueTouch";

        // Power Management series
        if (upperMpn.startsWith("CCG")) return "USB-C Controller";
        if (upperMpn.startsWith("CYPD")) return "USB-PD Controller";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // PSoC replacements
        if (series1.startsWith("PSoC")) {
            // Same PSoC family and package might be pin-compatible
            String pkg1 = extractPackageCode(mpn1);
            String pkg2 = extractPackageCode(mpn2);
            if (!pkg1.equals(pkg2)) return false;

            // Extract base part number
            String base1 = mpn1.substring(0, mpn1.indexOf('-'));
            String base2 = mpn2.substring(0, mpn2.indexOf('-'));
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