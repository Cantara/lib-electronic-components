package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for ROHM components
 */
public class RohmHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^RN[0-9].*");        // NPN transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^RP[0-9].*");        // PNP transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^RZT[0-9].*");       // Digital transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^RQ[0-9].*");        // Power transistors

        // MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^RQ[0-9].*");           // N-channel MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^RSQ[0-9].*");          // P-channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_ROHM, "^RQ[0-9].*");
        registry.addPattern(ComponentType.MOSFET_ROHM, "^RSQ[0-9].*");

        // Diodes
        registry.addPattern(ComponentType.DIODE, "^RB[0-9].*");            // General purpose diodes
        registry.addPattern(ComponentType.DIODE, "^RF[0-9].*");            // Fast recovery diodes
        registry.addPattern(ComponentType.DIODE, "^RB[0-9].*Z");           // Zener diodes
        registry.addPattern(ComponentType.DIODE, "^RBR[0-9].*");           // Schottky diodes
        registry.addPattern(ComponentType.DIODE_ROHM, "^RB[0-9].*");
        registry.addPattern(ComponentType.DIODE_ROHM, "^RF[0-9].*");

        // Power Management
        registry.addPattern(ComponentType.IC, "^BD[0-9].*");               // DC-DC converters
        registry.addPattern(ComponentType.IC, "^BA[0-9].*");               // Linear regulators
        registry.addPattern(ComponentType.IC, "^BM[0-9].*");               // Motor drivers
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_ROHM, "^BD[0-9].*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_ROHM, "^BA[0-9].*");
        registry.addPattern(ComponentType.MOTOR_DRIVER_ROHM, "^BM[0-9].*");

        // Op-Amps and Comparators
        registry.addPattern(ComponentType.OPAMP, "^BA[0-9].*");            // Op-amps
        registry.addPattern(ComponentType.OPAMP, "^BU[0-9].*");            // Comparators
        registry.addPattern(ComponentType.OPAMP_ROHM, "^BA[0-9].*");
        registry.addPattern(ComponentType.OPAMP_ROHM, "^BU[0-9].*");

        // LED Drivers
        registry.addPattern(ComponentType.IC, "^BD[0-9].*L");              // LED drivers
        registry.addPattern(ComponentType.IC, "^BU[0-9].*L");              // LED driver controllers
        registry.addPattern(ComponentType.LED_DRIVER_ROHM, "^BD[0-9].*L");
        registry.addPattern(ComponentType.LED_DRIVER_ROHM, "^BU[0-9].*L");

        // Sensors
        registry.addPattern(ComponentType.IC, "^BH[0-9].*");               // Hall effect sensors
        registry.addPattern(ComponentType.IC, "^BP[0-9].*");               // Pressure sensors
        registry.addPattern(ComponentType.IC, "^BM[0-9].*S");              // Motion sensors

        // Interface ICs
        registry.addPattern(ComponentType.IC, "^BD[0-9].*U");              // USB controllers
        registry.addPattern(ComponentType.IC, "^BU[0-9].*I");              // I2C devices
        registry.addPattern(ComponentType.IC, "^BM[0-9].*C");              // CAN transceivers
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MOSFET,
            ComponentType.MOSFET_ROHM,
            ComponentType.DIODE,
            ComponentType.DIODE_ROHM,
            ComponentType.LED,
            ComponentType.LED_ROHM,
            ComponentType.LED_DRIVER_ROHM,
            ComponentType.OPAMP,
            ComponentType.OPAMP_ROHM,
            ComponentType.VOLTAGE_REGULATOR,
            ComponentType.VOLTAGE_REGULATOR_ROHM,
            ComponentType.MOTOR_DRIVER_ROHM
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Transistor and MOSFET packages
        if (upperMpn.startsWith("R") && upperMpn.length() >= 6) {
            String suffix = upperMpn.substring(upperMpn.length() - 2);
            return switch (suffix) {
                case "T1" -> "SOT-23";
                case "T2" -> "SOT-223";
                case "T3" -> "TO-251";
                case "T4" -> "TO-252";
                case "T5" -> "TO-263";
                case "T6" -> "TO-220";
                case "S1" -> "SC-59";
                case "S2" -> "SC-70";
                case "S3" -> "SC-88A";
                default -> "";
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("RN")) return "NPN Transistor";
        if (upperMpn.startsWith("RP")) return "PNP Transistor";
        if (upperMpn.startsWith("RZT")) return "Digital Transistor";
        if (upperMpn.startsWith("RQ")) return "MOSFET";
        if (upperMpn.startsWith("RSQ")) return "P-Channel MOSFET";
        if (upperMpn.startsWith("RB")) {
            if (upperMpn.endsWith("Z")) return "Zener Diode";
            if (upperMpn.startsWith("RBR")) return "Schottky Diode";
            return "General Purpose Diode";
        }
        if (upperMpn.startsWith("RF")) return "Fast Recovery Diode";
        if (upperMpn.startsWith("BA")) return "Op-amp/Linear Regulator";
        if (upperMpn.startsWith("BU")) return "Comparator";
        if (upperMpn.startsWith("BD")) return "DC-DC Converter";
        if (upperMpn.startsWith("BM")) return "Motor Driver";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // Check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        return pkg1.equals(pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}