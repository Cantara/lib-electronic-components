package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;

/**
 * Handler for Silicon Labs components
 */
public class SiliconLabsHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // 8-bit MCUs (8051-based)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^C8051F[0-9]+.*");      // C8051 MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER_SILICON_LABS, "^C8051F[0-9]+.*");
        registry.addPattern(ComponentType.MCU_SILICON_LABS, "^C8051F[0-9]+.*");

        // EFM8 Series (8-bit MCUs)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^EFM8[A-Z][A-Z][0-9]+.*");  // EFM8 Busy Bee, Sleepy Bee, etc.
        registry.addPattern(ComponentType.MICROCONTROLLER_SILICON_LABS, "^EFM8[A-Z][A-Z][0-9]+.*");
        registry.addPattern(ComponentType.MCU_SILICON_LABS, "^EFM8[A-Z][A-Z][0-9]+.*");

        // EFM32 Series (32-bit ARM MCUs)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^EFM32[A-Z][A-Z][0-9]+.*");  // Giant Gecko, Happy Gecko, etc.
        registry.addPattern(ComponentType.MICROCONTROLLER_SILICON_LABS, "^EFM32[A-Z][A-Z][0-9]+.*");
        registry.addPattern(ComponentType.MCU_SILICON_LABS, "^EFM32[A-Z][A-Z][0-9]+.*");

        // EFR32 Series (Wireless MCUs)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^EFR32[A-Z][A-Z][0-9]+.*");  // Blue Gecko, Flex Gecko, etc.
        registry.addPattern(ComponentType.MICROCONTROLLER_SILICON_LABS, "^EFR32[A-Z][A-Z][0-9]+.*");
        registry.addPattern(ComponentType.MCU_SILICON_LABS, "^EFR32[A-Z][A-Z][0-9]+.*");

        // Wireless ICs
        registry.addPattern(ComponentType.IC, "^SI[0-9]+.*");                   // General wireless ICs
        registry.addPattern(ComponentType.IC, "^BGM[0-9]+.*");                  // Blue Gecko Modules
        registry.addPattern(ComponentType.IC, "^EZR32[A-Z][A-Z][0-9]+.*");     // EZR32 Wireless MCUs

        // Timing Solutions
        registry.addPattern(ComponentType.IC, "^SI5[0-9]+.*");                  // Clock ICs
        registry.addPattern(ComponentType.CRYSTAL, "^501[0-9]+.*");             // Crystal products
        registry.addPattern(ComponentType.OSCILLATOR, "^598[0-9]+.*");          // Oscillator products

        // Sensors
        registry.addPattern(ComponentType.IC, "^SI7[0-9]+.*");                  // Temperature/Humidity sensors
        registry.addPattern(ComponentType.IC, "^SI1[0-9]+.*");                  // Proximity/Ambient light sensors

        // Interface ICs
        registry.addPattern(ComponentType.IC, "^CP2[0-9]+.*");                  // USB-UART bridges
        registry.addPattern(ComponentType.IC, "^SI321[0-9]+.*");               // USB controllers

        // Isolators
        registry.addPattern(ComponentType.IC, "^SI84[0-9]+.*");                 // Digital isolators
        registry.addPattern(ComponentType.IC, "^SI86[0-9]+.*");                 // Isolated gate drivers
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MICROCONTROLLER,
            ComponentType.MICROCONTROLLER_SILABS,
            ComponentType.MCU_SILABS,
            ComponentType.MICROCONTROLLER_SILICON_LABS,
            ComponentType.MCU_SILICON_LABS,
            ComponentType.EFM8_MCU,
            ComponentType.EFM32_MCU,
            ComponentType.EFR32_MCU,
            ComponentType.IC,
            ComponentType.CRYSTAL,
            ComponentType.OSCILLATOR
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // EFM32/EFR32 package codes
        if (upperMpn.startsWith("EFM32") || upperMpn.startsWith("EFR32")) {
            // Example: EFM32GG230F1024G-C0 -> G (BGA)
            // Example: EFM32WG980F256-QFP100 -> QFP100
            int dashIndex = upperMpn.indexOf('-');
            if (dashIndex > 0) {
                // If after dash contains common package names, use that
                String afterDash = upperMpn.substring(dashIndex + 1);
                if (afterDash.startsWith("QFP") || afterDash.startsWith("BGA") ||
                        afterDash.startsWith("QFN") || afterDash.startsWith("CSP")) {
                    return afterDash;
                }
                // Otherwise look for single letter package code before dash
                int lastLetterIndex = findLastLetterBeforeDash(upperMpn, dashIndex);
                if (lastLetterIndex >= 0) {
                    return String.valueOf(upperMpn.charAt(lastLetterIndex));
                }
            }
        }

        // 8-bit MCU package codes
        if (upperMpn.startsWith("C8051F")) {
            // Example: C8051F380-GQ -> GQ
            int dashIndex = upperMpn.lastIndexOf('-');
            if (dashIndex >= 0 && dashIndex < upperMpn.length() - 1) {
                return upperMpn.substring(dashIndex + 1);
            }
        }

        // Wireless IC package codes
        if (upperMpn.startsWith("SI")) {
            // Look for package code after last dash
            int dashIndex = upperMpn.lastIndexOf('-');
            if (dashIndex >= 0 && dashIndex < upperMpn.length() - 1) {
                return upperMpn.substring(dashIndex + 1);
            }
        }

        return "";
    }

    private int findLastLetterBeforeDash(String str, int dashIndex) {
        for (int i = dashIndex - 1; i >= 0; i--) {
            if (Character.isLetter(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // MCU series
        if (upperMpn.startsWith("C8051F")) return "C8051";
        if (upperMpn.startsWith("EFM8")) {
            if (upperMpn.startsWith("EFM8BB")) return "EFM8 Busy Bee";
            if (upperMpn.startsWith("EFM8SB")) return "EFM8 Sleepy Bee";
            if (upperMpn.startsWith("EFM8LB")) return "EFM8 Laser Bee";
            return "EFM8";
        }
        if (upperMpn.startsWith("EFM32")) {
            if (upperMpn.startsWith("EFM32GG")) return "EFM32 Giant Gecko";
            if (upperMpn.startsWith("EFM32WG")) return "EFM32 Wonder Gecko";
            if (upperMpn.startsWith("EFM32HG")) return "EFM32 Happy Gecko";
            if (upperMpn.startsWith("EFM32PG")) return "EFM32 Pearl Gecko";
            return "EFM32";
        }
        if (upperMpn.startsWith("EFR32")) {
            if (upperMpn.startsWith("EFR32BG")) return "Blue Gecko";
            if (upperMpn.startsWith("EFR32FG")) return "Flex Gecko";
            if (upperMpn.startsWith("EFR32MG")) return "Mighty Gecko";
            return "EFR32";
        }

        // Other product lines
        if (upperMpn.startsWith("SI5")) return "Timing";
        if (upperMpn.startsWith("SI7")) return "Environmental Sensor";
        if (upperMpn.startsWith("SI1")) return "Proximity Sensor";
        if (upperMpn.startsWith("CP2")) return "USB Bridge";
        if (upperMpn.startsWith("SI84")) return "Digital Isolator";
        if (upperMpn.startsWith("SI86")) return "Isolated Gate Driver";
        if (upperMpn.startsWith("BGM")) return "Bluetooth Module";
        if (upperMpn.startsWith("EZR32")) return "EZR32 Wireless MCU";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not typically replacements
        if (!series1.equals(series2)) return false;

        // Check for pin-compatible parts within same subfamily
        if (series1.startsWith("EFM32") || series1.startsWith("EFR32")) {
            // Extract base part number before package code
            String base1 = mpn1.substring(0, mpn1.indexOf('-'));
            String base2 = mpn2.substring(0, mpn2.indexOf('-'));

            // Same base part number with different package or temperature grade
            // might be pin-compatible
            if (base1.equals(base2)) return true;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}