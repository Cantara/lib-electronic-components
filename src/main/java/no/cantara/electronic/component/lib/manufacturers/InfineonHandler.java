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
        // Legacy International Rectifier MOSFETs (now Infineon)
        registry.addPattern(ComponentType.MOSFET, "^IRF[0-9].*");        // Standard MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRF[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IRL[0-9].*");        // Logic level
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRL[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IRFP[0-9].*");       // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRFP[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IRFB[0-9].*");       // Bridge MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRFB[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IRFZ[0-9].*");       // HEXFET MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRFZ[0-9].*");

        // OptiMOS MOSFETs (Modern Infineon naming)
        registry.addPattern(ComponentType.MOSFET, "^IPP[0-9].*");        // OptiMOS Power
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IPP[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^BSC[0-9].*");        // OptiMOS Small Signal
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^BSC[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^IFX[A-Z0-9]+N.*");   // IFX MOSFETs
        registry.addPattern(ComponentType.MOSFET_INFINEON, "^IFX[A-Z0-9]+N.*");

        // IGBTs
        registry.addPattern(ComponentType.IGBT_INFINEON, "^IKP[0-9].*");  // Standard IGBTs
        registry.addPattern(ComponentType.IGBT_INFINEON, "^IKW[0-9].*");  // High-power IGBTs

        // Voltage Regulators (Automotive ICs)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^IFX[0-9].*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_INFINEON, "^IFX[0-9].*");

        // LED Drivers
        registry.addPattern(ComponentType.LED_DRIVER_INFINEON, "^ILD[0-9].*");

        // Gate Drivers
        registry.addPattern(ComponentType.GATE_DRIVER_INFINEON, "^IRS[0-9].*");

        // Microcontrollers (Cypress PSoC acquired by Infineon)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^XMC[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_INFINEON, "^XMC[0-9].*");
        registry.addPattern(ComponentType.MCU_INFINEON, "^XMC[0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MOSFET,
                ComponentType.MOSFET_INFINEON,
                ComponentType.IGBT_INFINEON,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.VOLTAGE_REGULATOR_LINEAR_INFINEON,
                ComponentType.VOLTAGE_REGULATOR_SWITCHING_INFINEON,
                ComponentType.LED_DRIVER_INFINEON,
                ComponentType.GATE_DRIVER_INFINEON,
                ComponentType.OPAMP,
                ComponentType.OPAMP_INFINEON,
                ComponentType.MICROCONTROLLER,
                ComponentType.MICROCONTROLLER_INFINEON,
                ComponentType.MCU_INFINEON,
                ComponentType.MEMORY,
                ComponentType.MEMORY_INFINEON,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Special case for MOSFETs (all series)
        if (type == ComponentType.MOSFET_INFINEON || type == ComponentType.MOSFET) {
            if (upperMpn.startsWith("IRF") || upperMpn.startsWith("IRL") ||
                upperMpn.startsWith("IPP") || upperMpn.startsWith("BSC")) {
                return true;
            }
            // IFX MOSFETs have 'N' in the name (N-channel indicator)
            if (upperMpn.startsWith("IFX") && upperMpn.contains("N")) {
                return true;
            }
        }

        // Special case for microcontrollers
        if ((type == ComponentType.MICROCONTROLLER_INFINEON || type == ComponentType.MCU_INFINEON ||
             type == ComponentType.MICROCONTROLLER) && upperMpn.startsWith("XMC")) {
            return true;
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Legacy IRF/IRL MOSFETs - package code is last letter
        if (upperMpn.startsWith("IRF") || upperMpn.startsWith("IRL")) {
            // Extract last character if it's a letter
            if (upperMpn.length() > 0) {
                char lastChar = upperMpn.charAt(upperMpn.length() - 1);
                if (Character.isLetter(lastChar)) {
                    String suffix = String.valueOf(lastChar);
                    return switch (suffix) {
                        case "N" -> "TO-220";
                        case "L" -> "TO-262";
                        case "S" -> "D2PAK";
                        case "U" -> "IPAK";
                        case "P" -> "TO-247";
                        case "B" -> "TO-263";
                        case "E" -> "TO-220AB";
                        default -> suffix;
                    };
                }
            }
            return "";
        }

        // IGBTs - package code is last letter
        if (upperMpn.startsWith("IKP") || upperMpn.startsWith("IKW")) {
            if (upperMpn.length() > 0) {
                char lastChar = upperMpn.charAt(upperMpn.length() - 1);
                if (Character.isLetter(lastChar)) {
                    String suffix = String.valueOf(lastChar);
                    return switch (suffix) {
                        case "N" -> "TO-220";
                        case "P" -> "TO-247";
                        case "H" -> "TO-247HV";
                        case "S" -> "D2PAK";
                        default -> suffix;
                    };
                }
            }
            return "";
        }

        // OptiMOS IPP series - package in middle
        if (upperMpn.startsWith("IPP")) {
            // IPP060N06N3 G -> extract package letter before final digit
            if (upperMpn.matches("IPP[0-9]+[A-Z][0-9]+[A-Z][0-9].*")) {
                // Complex format, return empty for now
                return "";
            }
            return "";
        }

        // BSC series small signal MOSFETs
        if (upperMpn.startsWith("BSC")) {
            // BSC093N03LS - typically SOT-223 or similar
            return "";
        }

        // XMC microcontrollers - suffix after series
        if (upperMpn.startsWith("XMC")) {
            // XMC1202-T028X0064 AB -> extract package between hyphens
            if (upperMpn.contains("-")) {
                String[] parts = upperMpn.split("-");
                if (parts.length > 1) {
                    // T028 format: T=TSSOP, first digit=temperature, 28=pins
                    String suffix = parts[1];
                    if (suffix.length() > 0) {
                        char pkgChar = suffix.charAt(0);
                        return switch (pkgChar) {
                            case 'T' -> "TSSOP";
                            case 'Q' -> "TQFP";
                            case 'F' -> "LQFP";
                            case 'V' -> "VQFN";
                            default -> "";
                        };
                    }
                }
            }
            return "";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // MOSFETs - check longer prefixes first to avoid matching "IRF" on "IRFP"
        if (upperMpn.startsWith("IRFZ")) return "IRFZ";
        if (upperMpn.startsWith("IRFP")) return "IRFP";
        if (upperMpn.startsWith("IRFB")) return "IRFB";
        if (upperMpn.startsWith("IRF")) return "IRF";
        if (upperMpn.startsWith("IRL")) return "IRL";

        // OptiMOS
        if (upperMpn.startsWith("IPP")) return "IPP";
        if (upperMpn.startsWith("BSC")) return "BSC";

        // IGBTs
        if (upperMpn.startsWith("IKP")) return "IKP";
        if (upperMpn.startsWith("IKW")) return "IKW";

        // Voltage Regulators / Automotive ICs
        if (upperMpn.startsWith("IFX")) return "IFX";

        // LED Drivers
        if (upperMpn.startsWith("ILD")) return "ILD";

        // Gate Drivers
        if (upperMpn.startsWith("IRS")) return "IRS";

        // Microcontrollers (Cypress PSoC)
        if (upperMpn.startsWith("XMC")) {
            // XMC1202 -> return XMC1202 (series includes 4 digits)
            if (upperMpn.length() >= 7) {
                return upperMpn.substring(0, 7);
            }
            return "XMC";
        }

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