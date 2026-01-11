package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

public class STHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^STF[0-9].*");        // N-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_ST, "^STF[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^STP[0-9].*");        // P-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_ST, "^STP[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^STD[0-9].*");        // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET_ST, "^STD[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^STB[0-9].*");        // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET_ST, "^STB[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^VN[0-9].*");         // N-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_ST, "^VN[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^VP[0-9].*");         // P-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_ST, "^VP[0-9].*");

        // Voltage Regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MC78.*");     // Fixed positive
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, "^MC78.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MC79.*");     // Fixed negative
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, "^MC79.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MC317.*");    // Adjustable positive
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, "^MC317.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MC337.*");    // Adjustable negative
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, "^MC337.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^L78.*");      // Fixed positive
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, "^L78.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^L79.*");      // Fixed negative
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST, "^L79.*");

        // Microcontrollers
        registry.addPattern(ComponentType.MICROCONTROLLER, "^STM32[F|L|H|G|W|U].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ST, "^STM32[F|L|H|G|W|U].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^STM8[S|L|A].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ST, "^STM8[S|L|A].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MICROCONTROLLER,
                ComponentType.MICROCONTROLLER_ST,
                ComponentType.MCU_ST,
                ComponentType.MEMORY,
                ComponentType.MEMORY_ST,
                ComponentType.OPAMP,
                ComponentType.OPAMP_ST,
                ComponentType.MOSFET,
                ComponentType.MOSFET_ST,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.VOLTAGE_REGULATOR_LINEAR_ST,
                ComponentType.VOLTAGE_REGULATOR_SWITCHING_ST,
                ComponentType.TEMPERATURE_SENSOR,
                ComponentType.TEMPERATURE_SENSOR_ST
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Quick prefix checks for common types
        boolean isMOSFET = upperMpn.startsWith("STF") || upperMpn.startsWith("STP") ||
                upperMpn.startsWith("STD") || upperMpn.startsWith("STB") ||
                upperMpn.startsWith("VN") || upperMpn.startsWith("VP");
        boolean isSTM32 = upperMpn.startsWith("STM32");
        boolean isSTM8 = upperMpn.startsWith("STM8");
        boolean isRegulator = upperMpn.startsWith("L78") || upperMpn.startsWith("L79") ||
                upperMpn.startsWith("MC78") || upperMpn.startsWith("MC79") ||
                upperMpn.startsWith("MC317") || upperMpn.startsWith("MC337");

        // Check base types
        if (type == ComponentType.MOSFET && isMOSFET) {
            return true;
        }
        if (type == ComponentType.MICROCONTROLLER && (isSTM32 || isSTM8)) {
            return true;
        }
        if (type == ComponentType.VOLTAGE_REGULATOR && isRegulator) {
            return true;
        }

        // Check ST-specific types
        if (type == ComponentType.MOSFET_ST && isMOSFET) {
            return true;
        }
        if (type == ComponentType.MICROCONTROLLER_ST && (isSTM32 || isSTM8)) {
            return true;
        }
        if (type == ComponentType.VOLTAGE_REGULATOR_LINEAR_ST && isRegulator) {
            return true;
        }

        // Fallback to registry pattern matching for all patterns
        return patterns.matches(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // STM32/STM8: Package is second-to-last character, temp is last
        // e.g., STM32F103C8T6 → T = LQFP, 6 = temperature range
        if (upperMpn.startsWith("STM32") || upperMpn.startsWith("STM8")) {
            if (upperMpn.length() >= 2) {
                char packageChar = upperMpn.charAt(upperMpn.length() - 2);
                return switch (packageChar) {
                    case 'T' -> "LQFP";
                    case 'H' -> "BGA";
                    case 'U' -> "VFQFPN";
                    case 'Y' -> "WLCSP";
                    case 'P' -> "TSSOP";
                    default -> String.valueOf(packageChar);
                };
            }
        }

        // MOSFETs: Package is in prefix
        if (upperMpn.startsWith("STF")) return "TO-220FP";
        if (upperMpn.startsWith("STP")) return "TO-220";
        if (upperMpn.startsWith("STD")) return "DPAK";
        if (upperMpn.startsWith("STB")) return "D2PAK";
        if (upperMpn.startsWith("STW")) return "TO-247";

        // Voltage regulators: L78xxCV, L78xxABD2T, MC78xxCT, etc.
        // Pattern: L78[voltage][grade][package] or MC78[voltage][package]
        if (upperMpn.startsWith("L78") || upperMpn.startsWith("L79") ||
                upperMpn.startsWith("MC78") || upperMpn.startsWith("MC79")) {
            // Extract suffix after voltage digits
            String suffix = upperMpn.replaceFirst("^[LM]C?7[89]\\d{2}", "");
            // Strip grade letters (A, AB, AC) from the beginning
            suffix = suffix.replaceFirst("^[A-C]{1,2}", "");
            return switch (suffix) {
                case "CV", "V" -> "TO-220";
                case "CP", "P" -> "TO-220FP";
                case "CT", "T" -> "TO-220";
                case "CD2T", "D2T" -> "D2PAK";
                case "CDT", "DT" -> "DPAK";
                default -> suffix.isEmpty() ? "" : suffix;
            };
        }

        // Op-amps and other linear ICs
        if (upperMpn.contains("-")) {
            String suffix = upperMpn.substring(upperMpn.lastIndexOf('-') + 1);
            return switch (suffix) {
                case "N" -> "DIP";
                case "D" -> "SOIC";
                case "DT" -> "TSSOP";
                case "PT" -> "TSSOP";
                case "PW" -> "TSSOP";
                default -> suffix;
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // STM32 microcontrollers
        if (mpn.startsWith("STM32")) {
            int end = "STM32".length();
            while (end < mpn.length() &&
                    (Character.isLetterOrDigit(mpn.charAt(end)) || mpn.charAt(end) == '_')) {
                end++;
            }
            return mpn.substring(0, end);
        }

        // STM8 microcontrollers
        if (mpn.startsWith("STM8")) {
            int end = "STM8".length() + 1;  // Include the family letter (S/L/A)
            return mpn.substring(0, Math.min(end, mpn.length()));
        }

        // MOSFETs
        if (mpn.startsWith("STF") || mpn.startsWith("STP") ||
                mpn.startsWith("STD") || mpn.startsWith("STB")) {
            return mpn.substring(0, 3);
        }
        if (mpn.startsWith("VN") || mpn.startsWith("VP")) {
            return mpn.substring(0, 2);
        }

        // Op-amps and voltage regulators
        if (mpn.matches(".*(?:78|79)\\d{2}.*")) {
            return mpn.substring(0, mpn.indexOf("78") + 2);
        }
        if (mpn.startsWith("MC317") || mpn.startsWith("MC337")) {
            return mpn.substring(0, 5);
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

        // Check package size
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If packages are specified and different, they're not compatible
        if (!pkg1.isEmpty() && !pkg2.isEmpty() && !pkg1.equals(pkg2)) {
            return false;
        }

        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}