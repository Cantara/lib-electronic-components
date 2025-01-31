package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

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
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MICROCONTROLLER);
        types.add(ComponentType.MICROCONTROLLER_ST);
        types.add(ComponentType.MCU_ST);
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_ST);
        types.add(ComponentType.OPAMP);
        types.add(ComponentType.OPAMP_ST);
        types.add(ComponentType.MOSFET);
        types.add(ComponentType.MOSFET_ST);
        types.add(ComponentType.VOLTAGE_REGULATOR);
        types.add(ComponentType.VOLTAGE_REGULATOR_LINEAR_ST);
        types.add(ComponentType.VOLTAGE_REGULATOR_SWITCHING_ST);
        types.add(ComponentType.TEMPERATURE_SENSOR);
        types.add(ComponentType.TEMPERATURE_SENSOR_ST);
        return types;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Special case for ST MOSFETs
        if (type == ComponentType.MOSFET_ST &&
                (upperMpn.startsWith("STF") || upperMpn.startsWith("STP") ||
                        upperMpn.startsWith("STD") || upperMpn.startsWith("STB") ||
                        upperMpn.startsWith("VN") || upperMpn.startsWith("VP"))) {
            return true;
        }

        // Special case for STM32 microcontrollers
        if (type == ComponentType.MICROCONTROLLER_ST && upperMpn.startsWith("STM32")) {
            return true;
        }

        // Special case for STM8 microcontrollers
        if (type == ComponentType.MICROCONTROLLER_ST && upperMpn.startsWith("STM8")) {
            return true;
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Op-amps and voltage regulators
        String suffix = mpn.replaceAll("^[A-Z0-9]+", "");
        return switch (suffix) {
            case "N" -> "DIP";
            case "D" -> "SOIC";
            case "DT" -> "TSSOP";
            case "PT" -> "TSSOP";
            case "PW" -> "TSSOP";
            case "T" -> "TO-220";
            case "M" -> "SO-8";
            case "MW" -> "SO-8 Wide";
            case "R" -> "SO-8";
            default -> suffix;
        };
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