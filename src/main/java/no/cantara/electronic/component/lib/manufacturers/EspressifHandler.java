package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Espressif components
 */
public class EspressifHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // WiFi SoCs - ESP8266
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP8266.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ESPRESSIF, "^ESP8266.*");
        registry.addPattern(ComponentType.MCU_ESPRESSIF, "^ESP8266.*");
        registry.addPattern(ComponentType.ESP8266_SOC, "^ESP8266.*");

        // WiFi + Bluetooth SoCs - ESP32 base
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP32[^-].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ESPRESSIF, "^ESP32[^-].*");
        registry.addPattern(ComponentType.MCU_ESPRESSIF, "^ESP32[^-].*");
        registry.addPattern(ComponentType.ESP32_SOC, "^ESP32[^-].*");

        // ESP32-S2 variant
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP32-S2.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ESPRESSIF, "^ESP32-S2.*");
        registry.addPattern(ComponentType.MCU_ESPRESSIF, "^ESP32-S2.*");
        registry.addPattern(ComponentType.ESP32_S2_SOC, "^ESP32-S2.*");

        // ESP32-S3 variant
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP32-S3.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ESPRESSIF, "^ESP32-S3.*");
        registry.addPattern(ComponentType.MCU_ESPRESSIF, "^ESP32-S3.*");
        registry.addPattern(ComponentType.ESP32_S3_SOC, "^ESP32-S3.*");

        // ESP32-C3 variant
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP32-C3.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ESPRESSIF, "^ESP32-C3.*");
        registry.addPattern(ComponentType.MCU_ESPRESSIF, "^ESP32-C3.*");
        registry.addPattern(ComponentType.ESP32_C3_SOC, "^ESP32-C3.*");

        // ESP32-C6 variant
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP32-C6.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ESPRESSIF, "^ESP32-C6.*");
        registry.addPattern(ComponentType.MCU_ESPRESSIF, "^ESP32-C6.*");

        // ESP32-H variant
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP32-H.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_ESPRESSIF, "^ESP32-H.*");
        registry.addPattern(ComponentType.MCU_ESPRESSIF, "^ESP32-H.*");

        // WROOM Modules
        registry.addPattern(ComponentType.IC, "^ESP-WROOM-.*");
        registry.addPattern(ComponentType.IC, "^ESP32-WROOM-.*");
        registry.addPattern(ComponentType.ESP32_WROOM_MODULE, "^ESP-WROOM-.*");
        registry.addPattern(ComponentType.ESP32_WROOM_MODULE, "^ESP32-WROOM-.*");

        // WROVER Modules
        registry.addPattern(ComponentType.IC, "^ESP-WROVER-.*");
        registry.addPattern(ComponentType.IC, "^ESP32-WROVER-.*");
        registry.addPattern(ComponentType.ESP32_WROVER_MODULE, "^ESP-WROVER-.*");
        registry.addPattern(ComponentType.ESP32_WROVER_MODULE, "^ESP32-WROVER-.*");

        // Other Modules
        registry.addPattern(ComponentType.IC, "^ESP-MODULE-.*");
        registry.addPattern(ComponentType.IC, "^ESP32-MINI-.*");
        registry.addPattern(ComponentType.IC, "^ESP32-PICO-.*");

        // Development Boards
        registry.addPattern(ComponentType.IC, "^ESP32-DevKit.*");
        registry.addPattern(ComponentType.IC, "^ESP32-WROOM-DevKit.*");
        registry.addPattern(ComponentType.IC, "^ESP-LAUNCHER.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MICROCONTROLLER,
            ComponentType.MICROCONTROLLER_ESPRESSIF,
            ComponentType.MCU_ESPRESSIF,
            ComponentType.IC,
            ComponentType.ESP8266_SOC,
            ComponentType.ESP32_SOC,
            ComponentType.ESP32_S2_SOC,
            ComponentType.ESP32_S3_SOC,
            ComponentType.ESP32_C3_SOC,
            ComponentType.ESP32_WROOM_MODULE,
            ComponentType.ESP32_WROVER_MODULE
        );
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Module package types
        if (upperMpn.contains("WROOM")) return "WROOM";
        if (upperMpn.contains("WROVER")) return "WROVER";
        if (upperMpn.contains("MINI")) return "MINI";
        if (upperMpn.contains("PICO")) return "PICO";

        // SoC package types
        if (upperMpn.startsWith("ESP32") || upperMpn.startsWith("ESP8266")) {
            // Look for package code after last dash
            int dashIndex = upperMpn.lastIndexOf('-');
            if (dashIndex >= 0 && dashIndex < upperMpn.length() - 1) {
                String suffix = upperMpn.substring(dashIndex + 1);
                // Common package codes
                if (suffix.startsWith("QFN") ||
                        suffix.startsWith("LGA") ||
                        suffix.startsWith("BGA")) {
                    return suffix;
                }
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Base series
        if (upperMpn.startsWith("ESP8266")) return "ESP8266";

        // ESP32 series and variants
        if (upperMpn.startsWith("ESP32")) {
            if (upperMpn.startsWith("ESP32-S2")) return "ESP32-S2";
            if (upperMpn.startsWith("ESP32-S3")) return "ESP32-S3";
            if (upperMpn.startsWith("ESP32-C3")) return "ESP32-C3";
            if (upperMpn.startsWith("ESP32-C6")) return "ESP32-C6";
            if (upperMpn.startsWith("ESP32-H")) return "ESP32-H";
            return "ESP32";
        }

        // Modules
        if (upperMpn.contains("WROOM")) {
            if (upperMpn.startsWith("ESP32")) return "ESP32-WROOM";
            return "ESP-WROOM";
        }
        if (upperMpn.contains("WROVER")) {
            if (upperMpn.startsWith("ESP32")) return "ESP32-WROVER";
            return "ESP-WROVER";
        }
        if (upperMpn.contains("MINI")) return "ESP32-MINI";
        if (upperMpn.contains("PICO")) return "ESP32-PICO";

        // Development boards
        if (upperMpn.contains("DEVKIT")) return "ESP32-DevKit";
        if (upperMpn.contains("LAUNCHER")) return "ESP-LAUNCHER";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // Some known compatible replacements
        if (series1.equals("ESP32")) {
            // Within same series, different memory configurations might be compatible
            // Example: ESP32-D0WDQ6 and ESP32-D0WD are compatible
            int dash1 = mpn1.indexOf('-');
            int dash2 = mpn2.indexOf('-');
            // If no dash found, use entire MPN as base
            String base1 = dash1 >= 0 ? mpn1.substring(0, dash1) : mpn1;
            String base2 = dash2 >= 0 ? mpn2.substring(0, dash2) : mpn2;
            if (base1.equals(base2)) return true;
        }

        // Module compatibility
        if (series1.contains("WROOM") || series1.contains("WROVER")) {
            // Different revisions of same module family might be compatible
            // Example: ESP32-WROOM-32 and ESP32-WROOM-32E are often compatible
            String moduleType1 = extractModuleType(mpn1);
            String moduleType2 = extractModuleType(mpn2);
            return moduleType1.equals(moduleType2);
        }

        return false;
    }

    private String extractModuleType(String mpn) {
        // Extract base module type without revision
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();
        if (upperMpn.contains("WROOM")) {
            int index = upperMpn.indexOf("WROOM");
            if (index >= 0) {
                // Get base part before any revision letter
                String base = upperMpn.substring(0, index + "WROOM".length());
                // Add module number if present
                if (upperMpn.length() > index + "WROOM".length() + 1) {
                    char next = upperMpn.charAt(index + "WROOM".length() + 1);
                    if (Character.isDigit(next)) {
                        base += "-" + next;
                    }
                }
                return base;
            }
        }

        if (upperMpn.contains("WROVER")) {
            int index = upperMpn.indexOf("WROVER");
            if (index >= 0) {
                // Similar processing for WROVER modules
                String base = upperMpn.substring(0, index + "WROVER".length());
                if (upperMpn.length() > index + "WROVER".length() + 1) {
                    char next = upperMpn.charAt(index + "WROVER".length() + 1);
                    if (Character.isDigit(next)) {
                        base += "-" + next;
                    }
                }
                return base;
            }
        }

        return "";
    }
}