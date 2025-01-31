package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.regex.Pattern;

public class MaximHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Temperature Sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR_MAXIM, "^DS18[A-Z0-9]+\\+?$");
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^DS18[A-Z0-9]+\\+?$");
        registry.addPattern(ComponentType.SENSOR, "^DS18[A-Z0-9]+\\+?$");

        registry.addPattern(ComponentType.TEMPERATURE_SENSOR_MAXIM, "^MAX6[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^MAX6[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.SENSOR, "^MAX6[0-9]{3}[A-Z0-9-]*$");

        // Real-Time Clocks
        registry.addPattern(ComponentType.IC, "^DS12.*");           // RTC
        registry.addPattern(ComponentType.IC, "^DS13.*");           // RTC

        // Memory
        registry.addPattern(ComponentType.MEMORY, "^DS28.*");       // EEPROM
        registry.addPattern(ComponentType.MEMORY_MAXIM, "^DS28.*");

        // Interface ICs
        registry.addPattern(ComponentType.IC, "^MAX3.*");           // RS-232/RS-485
        registry.addPattern(ComponentType.IC, "^MAX2.*");           // Level translators

        // ADCs and DACs
        registry.addPattern(ComponentType.IC, "^MAX11.*");          // ADC
        registry.addPattern(ComponentType.IC, "^MAX12.*");          // DAC
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.INTERFACE_IC_MAXIM);
        types.add(ComponentType.VOLTAGE_REGULATOR);
        types.add(ComponentType.VOLTAGE_REGULATOR_MAXIM);
        types.add(ComponentType.RTC_MAXIM);
        types.add(ComponentType.TEMPERATURE_SENSOR);
        types.add(ComponentType.TEMPERATURE_SENSOR_MAXIM);
        types.add(ComponentType.BATTERY_MANAGEMENT_MAXIM);
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_MAXIM);
        return types;
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
                    upperMpn.matches("^MAX6[0-9]{3}[A-Z0-9-]*$")) { // MAX6xxx series
                return true;
            }
        }

        // Use pattern registry for other matches
        Pattern pattern = patterns.getPattern(type);
        return pattern != null && pattern.matcher(upperMpn).matches();
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

        // Handle DS18B20 series
        if (mpn.matches("(?i)^DS18B20.*")) {
            return "DS18B20";  // Return base series without variants
        }

        // Handle MAX temperature sensors
        if (mpn.matches("(?i)^MAX6[0-9]{3}.*")) {
            return mpn.substring(0, 7);  // Return base MAX6xxx number
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