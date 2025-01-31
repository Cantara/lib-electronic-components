package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InfineonHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^IRF[0-9].*");        // Standard MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRF[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IRL[0-9].*");        // Logic level
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRL[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IRFP[0-9].*");       // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRFP[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IRFB[0-9].*");       // Bridge MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRFB[0-9].*");

        // IGBTs
        registry.addPattern(ComponentType.IGBT_INFINEON, "^IKP[0-9].*");  // Standard IGBTs
        registry.addPattern(ComponentType.IGBT_INFINEON, "^IKW[0-9].*");  // High-power IGBTs

        // Voltage Regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^IFX[0-9].*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_INFINEON, "^IFX[0-9].*");

        // LED Drivers
        registry.addPattern(ComponentType.LED_DRIVER_INFINEON, "^ILD[0-9].*");

        // Gate Drivers
        registry.addPattern(ComponentType.GATE_DRIVER_INFINEON, "^IRS[0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MOSFET);
        types.add(ComponentType.MOSFET_INFINEON);
       // types.add(ComponentType.IGBT);
        types.add(ComponentType.IGBT_INFINEON);
        types.add(ComponentType.VOLTAGE_REGULATOR);
        types.add(ComponentType.VOLTAGE_REGULATOR_LINEAR_INFINEON);
        types.add(ComponentType.VOLTAGE_REGULATOR_SWITCHING_INFINEON);
     //   types.add(ComponentType.LED_DRIVER);
        types.add(ComponentType.LED_DRIVER_INFINEON);
     //   types.add(ComponentType.GATE_DRIVER);
        types.add(ComponentType.GATE_DRIVER_INFINEON);
        types.add(ComponentType.OPAMP);
        types.add(ComponentType.OPAMP_INFINEON);
        types.add(ComponentType.MICROCONTROLLER);
        types.add(ComponentType.MICROCONTROLLER_INFINEON);
        types.add(ComponentType.MCU_INFINEON);
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_INFINEON);
        return types;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Special case for IRF and IRL MOSFETs
        if (type == ComponentType.MOSFET_INFINEON &&
                (upperMpn.startsWith("IRF") || upperMpn.startsWith("IRL"))) {
            return true;
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // MOSFETs
        if (mpn.startsWith("IRF") || mpn.startsWith("IRL")) {
            String suffix = mpn.replaceAll("^[A-Z0-9]+", "");
            return switch (suffix) {
                case "N" -> "TO-220";
                case "L" -> "TO-262";
                case "S" -> "D2PAK";
                case "U" -> "IPAK";
                case "P" -> "TO-247";
                default -> suffix;
            };
        }

        // IGBTs
        if (mpn.startsWith("IKP") || mpn.startsWith("IKW")) {
            String suffix = mpn.replaceAll("^[A-Z0-9]+", "");
            return switch (suffix) {
                case "N" -> "TO-220";
                case "P" -> "TO-247";
                case "H" -> "TO-247HV";
                default -> suffix;
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // MOSFETs
        if (mpn.startsWith("IRF")) return "IRF";
        if (mpn.startsWith("IRL")) return "IRL";
        if (mpn.startsWith("IRFP")) return "IRFP";
        if (mpn.startsWith("IRFB")) return "IRFB";

        // IGBTs
        if (mpn.startsWith("IKP")) return "IKP";
        if (mpn.startsWith("IKW")) return "IKW";

        // Voltage Regulators
        if (mpn.startsWith("IFX")) return "IFX";

        // LED Drivers
        if (mpn.startsWith("ILD")) return "ILD";

        // Gate Drivers
        if (mpn.startsWith("IRS")) return "IRS";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (!series1.equals(series2)) return false;

        // For MOSFETs and IGBTs, compare base part number
        if ((mpn1.startsWith("IRF") || mpn1.startsWith("IRL") ||
                mpn1.startsWith("IKP") || mpn1.startsWith("IKW")) &&
                (mpn2.startsWith("IRF") || mpn2.startsWith("IRL") ||
                        mpn2.startsWith("IKP") || mpn2.startsWith("IKW"))) {

            // Extract numeric part
            String base1 = mpn1.replaceAll("[A-Z]+", "").replaceAll("[A-Z]+$", "");
            String base2 = mpn2.replaceAll("[A-Z]+", "").replaceAll("[A-Z]+$", "");
            return base1.equals(base2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}