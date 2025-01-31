package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for Samsung Electro-Mechanics components
 */
public class SamsungHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Ceramic Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^CL[0-9].*");           // General purpose MLCC
        registry.addPattern(ComponentType.CAPACITOR, "^CL[0-9][0-9]B.*");     // X5R/X7R dielectric
        registry.addPattern(ComponentType.CAPACITOR, "^CL[0-9][0-9]C.*");     // X7S/X7T dielectric
        registry.addPattern(ComponentType.CAPACITOR, "^CL[0-9][0-9]A.*");     // C0G/NP0 dielectric
        registry.addPattern(ComponentType.CAPACITOR, "^CL[0-9][0-9]X.*");     // Y5V dielectric
        registry.addPattern(ComponentType.CAPACITOR_CERAMIC_SAMSUNG, "^CL[0-9].*");

        // Tantalum Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^TP[0-9].*");           // Polymer tantalum
        registry.addPattern(ComponentType.CAPACITOR, "^TC[0-9].*");           // Standard tantalum

        // Chip Inductors
        registry.addPattern(ComponentType.INDUCTOR, "^CIS[0-9].*");          // Standard inductors
        registry.addPattern(ComponentType.INDUCTOR, "^CIG[0-9].*");          // High-frequency inductors
        registry.addPattern(ComponentType.INDUCTOR, "^CIH[0-9].*");          // High-current inductors

        // EMI Components
        registry.addPattern(ComponentType.INDUCTOR, "^CBM[0-9].*");          // Ferrite beads
        registry.addPattern(ComponentType.INDUCTOR, "^CIM[0-9].*");          // Common mode filters

        // RF Components
        registry.addPattern(ComponentType.IC, "^CBP[0-9].*");               // Band pass filters
        registry.addPattern(ComponentType.IC, "^CBD[0-9].*");               // Diplexers
        registry.addPattern(ComponentType.IC, "^CBA[0-9].*");               // RF antenna components

        // LED Components
        registry.addPattern(ComponentType.LED, "^STW[0-9].*");              // White LEDs
        registry.addPattern(ComponentType.LED, "^STP[0-9].*");              // Power LEDs
        registry.addPattern(ComponentType.LED, "^STR[0-9].*");              // RGB LEDs
        registry.addPattern(ComponentType.LED_HIGHPOWER_SAMSUNG, "^STW[0-9].*");
        registry.addPattern(ComponentType.LED_HIGHPOWER_SAMSUNG, "^STP[0-9].*");

        // Power Modules
        registry.addPattern(ComponentType.IC, "^SPM[0-9].*");               // Power modules
        registry.addPattern(ComponentType.IC, "^SPC[0-9].*");               // Power controllers

        // Sensors
        registry.addPattern(ComponentType.IC, "^STH[0-9].*");               // Hall sensors
        registry.addPattern(ComponentType.IC, "^STS[0-9].*");               // Temperature sensors
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CAPACITOR);
        types.add(ComponentType.CAPACITOR_CERAMIC_SAMSUNG);
        types.add(ComponentType.LED);
        types.add(ComponentType.LED_HIGHPOWER_SAMSUNG);
        // Add other Samsung component types if they exist in ComponentType enum
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // MLCC size codes
        if (upperMpn.startsWith("CL")) {
            try {
                String sizeCode = upperMpn.substring(2, 4);
                return switch (sizeCode) {
                    case "03" -> "0201/0603M";
                    case "05" -> "0402/1005M";
                    case "10" -> "0603/1608M";
                    case "21" -> "0805/2012M";
                    case "31" -> "1206/3216M";
                    case "32" -> "1210/3225M";
                    case "43" -> "1812/4532M";
                    case "44" -> "1825/4564M";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Tantalum capacitor case codes
        if (upperMpn.startsWith("TP") || upperMpn.startsWith("TC")) {
            // Look for standard case codes (A, B, C, D, etc.)
            for (String caseCode : new String[]{"A", "B", "C", "D", "E", "U", "V", "W"}) {
                int idx = upperMpn.indexOf(caseCode);
                if (idx > 2 && idx < upperMpn.length() - 1) {
                    return "Case " + caseCode;
                }
            }
        }

        // Inductor size codes
        if (upperMpn.startsWith("CIS") || upperMpn.startsWith("CIG") || upperMpn.startsWith("CIH")) {
            try {
                String sizeCode = upperMpn.substring(3, 5);
                return switch (sizeCode) {
                    case "10" -> "0603";
                    case "15" -> "0805";
                    case "20" -> "1206";
                    case "25" -> "1210";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Ceramic capacitors
        if (upperMpn.startsWith("CL")) {
            if (upperMpn.length() > 4) {
                char dielectric = upperMpn.charAt(4);
                return switch (dielectric) {
                    case 'B' -> "X5R/X7R MLCC";
                    case 'C' -> "X7S/X7T MLCC";
                    case 'A' -> "C0G/NP0 MLCC";
                    case 'X' -> "Y5V MLCC";
                    default -> "MLCC Series";
                };
            }
            return "MLCC Series";
        }

        // Tantalum capacitors
        if (upperMpn.startsWith("TP")) return "Polymer Tantalum";
        if (upperMpn.startsWith("TC")) return "Standard Tantalum";

        // Inductors
        if (upperMpn.startsWith("CIS")) return "Standard Inductor";
        if (upperMpn.startsWith("CIG")) return "High-Frequency Inductor";
        if (upperMpn.startsWith("CIH")) return "High-Current Inductor";

        // EMI components
        if (upperMpn.startsWith("CBM")) return "Ferrite Bead";
        if (upperMpn.startsWith("CIM")) return "Common Mode Filter";

        // RF components
        if (upperMpn.startsWith("CBP")) return "Band Pass Filter";
        if (upperMpn.startsWith("CBD")) return "Diplexer";
        if (upperMpn.startsWith("CBA")) return "RF Antenna";

        // LED components
        if (upperMpn.startsWith("STW")) return "White LED";
        if (upperMpn.startsWith("STP")) return "Power LED";
        if (upperMpn.startsWith("STR")) return "RGB LED";

        // Power modules
        if (upperMpn.startsWith("SPM")) return "Power Module";
        if (upperMpn.startsWith("SPC")) return "Power Controller";

        // Sensors
        if (upperMpn.startsWith("STH")) return "Hall Sensor";
        if (upperMpn.startsWith("STS")) return "Temperature Sensor";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // For MLCCs, check size and dielectric type
        if (mpn1.startsWith("CL") && mpn2.startsWith("CL")) {
            // Compare case size
            String size1 = extractPackageCode(mpn1);
            String size2 = extractPackageCode(mpn2);
            if (!size1.equals(size2)) return false;

            // Compare dielectric type (if available)
            try {
                char dielectric1 = mpn1.charAt(4);
                char dielectric2 = mpn2.charAt(4);
                return dielectric1 == dielectric2;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }

        // For tantalum capacitors, same case size might be compatible
        if ((mpn1.startsWith("TP") || mpn1.startsWith("TC")) &&
                (mpn2.startsWith("TP") || mpn2.startsWith("TC"))) {
            String case1 = extractPackageCode(mpn1);
            String case2 = extractPackageCode(mpn2);
            return case1.equals(case2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}