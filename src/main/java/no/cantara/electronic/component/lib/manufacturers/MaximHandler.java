package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

public class MaximHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Temperature Sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR_MAXIM, "^DS18[A-Z0-9]+\\+?$");
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^DS18[A-Z0-9]+\\+?$");
        registry.addPattern(ComponentType.SENSOR, "^DS18[A-Z0-9]+\\+?$");

        // MAX6xxx temperature sensors (allow optional + for RoHS compliance indicator)
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR_MAXIM, "^MAX6[0-9]{3}[A-Z0-9-]*\\+?$");
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^MAX6[0-9]{3}[A-Z0-9-]*\\+?$");
        registry.addPattern(ComponentType.SENSOR, "^MAX6[0-9]{3}[A-Z0-9-]*\\+?$");

        // Real-Time Clocks (DS12xx, DS13xx, DS32xx)
        registry.addPattern(ComponentType.RTC_MAXIM, "^DS12[0-9]{2}.*");
        registry.addPattern(ComponentType.RTC_MAXIM, "^DS13[0-9]{2}.*");
        registry.addPattern(ComponentType.RTC_MAXIM, "^DS32[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^DS12[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^DS13[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^DS32[0-9]{2}.*");

        // Memory
        registry.addPattern(ComponentType.MEMORY, "^DS28.*");       // EEPROM
        registry.addPattern(ComponentType.MEMORY_MAXIM, "^DS28.*");

        // Interface ICs (RS-232, RS-485, Level translators)
        registry.addPattern(ComponentType.INTERFACE_IC_MAXIM, "^MAX232.*");  // RS-232
        registry.addPattern(ComponentType.INTERFACE_IC_MAXIM, "^MAX485.*");  // RS-485
        registry.addPattern(ComponentType.INTERFACE_IC_MAXIM, "^MAX3[0-9]{3}.*");  // RS-232/RS-485 family
        registry.addPattern(ComponentType.INTERFACE_IC_MAXIM, "^MAX2[0-9]{3}.*");  // Level translators
        registry.addPattern(ComponentType.IC, "^MAX232.*");
        registry.addPattern(ComponentType.IC, "^MAX485.*");
        registry.addPattern(ComponentType.IC, "^MAX3[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^MAX2[0-9]{3}.*");

        // Voltage Regulators (MAX17xx LDO, MAX8xxx switching)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_MAXIM, "^MAX17[0-9]{2}.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_MAXIM, "^MAX8[0-9]{3}.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MAX17[0-9]{2}.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MAX8[0-9]{3}.*");

        // Battery Management (MAX17xxx fuel gauges, chargers)
        registry.addPattern(ComponentType.BATTERY_MANAGEMENT_MAXIM, "^MAX17[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^MAX17[0-9]{3}.*");

        // ADCs and DACs
        registry.addPattern(ComponentType.IC, "^MAX11[0-9]{3}.*");  // ADC
        registry.addPattern(ComponentType.IC, "^MAX12[0-9]{3}.*");  // DAC
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.IC,
            ComponentType.SENSOR,
            ComponentType.INTERFACE_IC_MAXIM,
            ComponentType.VOLTAGE_REGULATOR,
            ComponentType.VOLTAGE_REGULATOR_MAXIM,
            ComponentType.RTC_MAXIM,
            ComponentType.TEMPERATURE_SENSOR,
            ComponentType.TEMPERATURE_SENSOR_MAXIM,
            ComponentType.BATTERY_MANAGEMENT_MAXIM,
            ComponentType.MEMORY,
            ComponentType.MEMORY_MAXIM
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct sensor check first
        if (type == ComponentType.SENSOR ||
                type == ComponentType.TEMPERATURE_SENSOR ||
                type == ComponentType.TEMPERATURE_SENSOR_MAXIM) {

            if (upperMpn.matches("^DS18[A-Z0-9]+\\+?$") ||    // DS18xxx series
                    upperMpn.matches("^MAX6[0-9]{3}[A-Z0-9-]*\\+?$")) { // MAX6xxx series (allow + suffix)
                return true;
            }
        }

        // Use handler-specific patterns for other matches (avoid cross-handler false matches)
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle special cases for DS18B20
        if (mpn.startsWith("DS18")) {
            if (mpn.endsWith("Z")) return "TO-92";
            if (mpn.endsWith("PAR")) return "TO-92 (Parasitic)";
            if (mpn.endsWith("SMD")) return "SOIC";
            return "";  // Default package for DS18B20
        }

        // Extract general package suffix
        return mpn.replaceAll("^[A-Z0-9]+([A-Z][A-Z0-9-]*)$", "$1");
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Strip RoHS + suffix for series extraction
        String baseMpn = mpn.replace("+", "");

        // Handle DS18B20 series
        if (baseMpn.matches("(?i)^DS18B20.*")) {
            return "DS18B20";  // Return base series without variants
        }

        // Handle MAX temperature sensors
        if (baseMpn.matches("(?i)^MAX6[0-9]{3}.*")) {
            return baseMpn.substring(0, Math.min(7, baseMpn.length()));  // Return base MAX6xxx number
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // DS18B20 variants are all compatible
        if (series1.equals("DS18B20") && series2.equals("DS18B20")) {
            return true;
        }

        // For other series, check exact match
        return !series1.isEmpty() && series1.equals(series2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }




}