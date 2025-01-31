package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class AnalogDevicesHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Motion Sensors
        registry.addPattern(ComponentType.ACCELEROMETER, "^ADXL[0-9].*");    // Accelerometers
        registry.addPattern(ComponentType.ACCELEROMETER_AD, "^ADXL[0-9].*");
        registry.addPattern(ComponentType.GYROSCOPE, "^ADXRS[0-9].*");       // Gyroscopes
        registry.addPattern(ComponentType.GYROSCOPE_AD, "^ADXRS[0-9].*");

        // Op-amps
        registry.addPattern(ComponentType.OPAMP, "^AD8[0-9].*");            // General purpose
        registry.addPattern(ComponentType.OPAMP_AD, "^AD8[0-9].*");
        registry.addPattern(ComponentType.OPAMP, "^OP[0-9].*");             // Precision
        registry.addPattern(ComponentType.OPAMP_AD, "^OP[0-9].*");

        // Temperature Sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^AD590.*");   // Temperature
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR_AD, "^AD590.*");
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^ADT[0-9].*"); // Digital temp
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR_AD, "^ADT[0-9].*");

        // ADCs and DACs
        registry.addPattern(ComponentType.IC, "^AD7[0-9].*");               // ADCs
        registry.addPattern(ComponentType.IC, "^AD5[0-9].*");               // DACs
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle package codes
        String[] parts = mpn.split("-");
        if (parts.length > 0) {
            String mainPart = parts[0];
            String suffix = mainPart.replaceAll("^[A-Z0-9]+", "");
            return switch (suffix) {
                case "Z" -> "QFN";
                case "CP" -> "LGA";
                case "BCCZ" -> "LGA";
                case "CCZ" -> "LGA";
                case "R" -> "SOIC";
                case "RT" -> "SOIC";
                case "RM" -> "MSOP";
                case "RU" -> "TSSOP";
                default -> suffix;
            };
        }
        return "";
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.OPAMP);
        types.add(ComponentType.OPAMP_AD);
        types.add(ComponentType.ADC_AD);
        types.add(ComponentType.DAC_AD);
        types.add(ComponentType.AMPLIFIER_AD);
        types.add(ComponentType.VOLTAGE_REFERENCE_AD);
        types.add(ComponentType.TEMPERATURE_SENSOR);
        types.add(ComponentType.TEMPERATURE_SENSOR_AD);
        types.add(ComponentType.ACCELEROMETER);
        types.add(ComponentType.ACCELEROMETER_AD);
        types.add(ComponentType.GYROSCOPE);
        types.add(ComponentType.GYROSCOPE_AD);
        return types;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle ADXL series
        if (mpn.startsWith("ADXL")) {
            int end = "ADXL".length();
            while (end < mpn.length() && Character.isDigit(mpn.charAt(end))) {
                end++;
            }
            return mpn.substring(0, end);
        }

        // Handle other series
        String[] parts = mpn.split("[^A-Z0-9]"); // Split on non-alphanumeric
        if (parts.length > 0) {
            return parts[0];
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Same part, different packaging (e.g., ADXL345BCCZ vs ADXL345BCCZ-RL)
        String base1 = mpn1.split("-")[0];
        String base2 = mpn2.split("-")[0];

        return base1.equals(base2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}