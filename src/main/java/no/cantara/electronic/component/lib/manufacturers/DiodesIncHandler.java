package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Handler for Diodes Incorporated components
 */
public class DiodesIncHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Discrete Diodes
        registry.addPattern(ComponentType.DIODE, "^1N[0-9].*");          // General purpose diodes
        registry.addPattern(ComponentType.DIODE, "^BAV[0-9].*");         // Signal diodes
        registry.addPattern(ComponentType.DIODE, "^BAS[0-9].*");         // Small signal diodes
        registry.addPattern(ComponentType.DIODE, "^B[0-9].*");           // Bridge rectifiers
        registry.addPattern(ComponentType.DIODE, "^DMXX.*");             // Surface mount diodes
        registry.addPattern(ComponentType.DIODE, "^MMBD.*");             // Dual diodes

        // Schottky Diodes
        registry.addPattern(ComponentType.DIODE, "^SBR[0-9].*");         // Schottky rectifiers
        registry.addPattern(ComponentType.DIODE, "^SK[0-9].*");          // Surface mount Schottkys
        registry.addPattern(ComponentType.DIODE, "^B[0-9]M.*");          // Power Schottkys

        // Zener Diodes
        registry.addPattern(ComponentType.DIODE, "^BZX[0-9].*");         // Zener diodes
        registry.addPattern(ComponentType.DIODE, "^MMSZ.*");             // Surface mount Zeners
        registry.addPattern(ComponentType.DIODE, "^DDZ.*");              // Digital Zeners

        // MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^DMN[0-9].*");        // N-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^DMP[0-9].*");        // P-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^ZXMN.*");            // N-Channel MOSFETs (ZETEX)
        registry.addPattern(ComponentType.MOSFET, "^ZXMP.*");            // P-Channel MOSFETs (ZETEX)

        // Transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT.*");        // Small signal transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^FMMT.*");        // RF transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^ZXT.*");         // Digital transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^DTA.*");         // Digital transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^DTB.*");         // Digital transistors

        // Voltage Regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^AP[0-9].*");    // Linear regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^AZ[0-9].*");    // LDO regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^PAM[0-9].*");   // DC-DC converters

        // LED Drivers
        registry.addPattern(ComponentType.IC, "^AL[0-9].*");             // LED drivers
        registry.addPattern(ComponentType.IC, "^AP[0-9]L.*");            // LED driver controllers

        // Interface ICs
        registry.addPattern(ComponentType.IC, "^PI[0-9].*");             // Interface ICs
        registry.addPattern(ComponentType.IC, "^DIODES_[0-9].*");        // USB switches
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MOSFET,
            ComponentType.MOSFET_DIODES,
            ComponentType.VOLTAGE_REGULATOR,
            ComponentType.VOLTAGE_REGULATOR_DIODES,
            ComponentType.LED_DRIVER_DIODES,
            ComponentType.LOGIC_IC_DIODES,
            ComponentType.HALL_SENSOR_DIODES
        );
    }


    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Extract package code based on prefix
        if (upperMpn.startsWith("DMN") || upperMpn.startsWith("DMP")) {
            // Example: DMN2075U -> U (SOT23)
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                String suffix = upperMpn.substring(lastDigitIndex + 1);
                return switch (suffix) {
                    case "U" -> "SOT23";
                    case "T" -> "SOT223";
                    case "L" -> "TO252/DPAK";
                    case "F" -> "TO220";
                    default -> suffix;
                };
            }
        }

        // ZETEX style package codes
        if (upperMpn.startsWith("ZX")) {
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                return upperMpn.substring(lastDigitIndex + 1);
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
        if (upperMpn.startsWith("DMN")) return "DMN Series";
        if (upperMpn.startsWith("DMP")) return "DMP Series";
        if (upperMpn.startsWith("ZXMN")) return "ZXMN Series";
        if (upperMpn.startsWith("ZXMP")) return "ZXMP Series";

        // Transistors
        if (upperMpn.startsWith("MMBT")) return "MMBT Series";
        if (upperMpn.startsWith("FMMT")) return "FMMT Series";
        if (upperMpn.startsWith("ZXT")) return "ZXT Series";
        if (upperMpn.startsWith("DTA")) return "DTA Series";
        if (upperMpn.startsWith("DTB")) return "DTB Series";

        // Diodes
        if (upperMpn.startsWith("BAV")) return "BAV Series";
        if (upperMpn.startsWith("BAS")) return "BAS Series";
        if (upperMpn.startsWith("SBR")) return "SBR Series";
        if (upperMpn.startsWith("BZX")) return "BZX Series";

        // ICs
        if (upperMpn.startsWith("AP")) return "AP Series";
        if (upperMpn.startsWith("AZ")) return "AZ Series";
        if (upperMpn.startsWith("PAM")) return "PAM Series";
        if (upperMpn.startsWith("AL")) return "AL Series";
        if (upperMpn.startsWith("PI")) return "PI Series";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // Some known compatible parts
        if (mpn1.startsWith("BAV19") && mpn2.startsWith("BAV20")) return true;
        if (mpn1.startsWith("BAV20") && mpn2.startsWith("BAV19")) return true;

        // ZETEX to DMx series compatibility
        if (mpn1.startsWith("ZXMN") && mpn2.startsWith("DMN")) {
            // Compare electrical characteristics if needed
            return true;
        }
        if (mpn1.startsWith("ZXMP") && mpn2.startsWith("DMP")) {
            // Compare electrical characteristics if needed
            return true;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}