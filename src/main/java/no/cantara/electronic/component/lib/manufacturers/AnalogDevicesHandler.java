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

        // ADCs - register both IC and ADC_AD types
        registry.addPattern(ComponentType.IC, "^AD7[0-9].*");               // ADCs
        registry.addPattern(ComponentType.ADC_AD, "^AD7[0-9].*");

        // DACs - register both IC and DAC_AD types
        registry.addPattern(ComponentType.IC, "^AD5[0-9].*");               // DACs
        registry.addPattern(ComponentType.DAC_AD, "^AD5[0-9].*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Analog Devices format examples:
        // AD8065ARZ = AD8065 + A (grade) + RZ (package)
        // ADXL345BCCZ = ADXL345 + B (grade) + CCZ (package)
        // AD7606BSTZ = AD7606 + BSTZ (grade + package)
        // AD7606-4BSTZ = AD7606-4 + BSTZ (with variant number)

        // Split on hyphen first
        String[] parts = upperMpn.split("-");
        String mainPart = parts[0];

        // Extract package suffix (typically 1-4 characters after the numeric part)
        // Look for letter patterns after digits
        int lastDigitIndex = -1;
        for (int i = mainPart.length() - 1; i >= 0; i--) {
            if (Character.isDigit(mainPart.charAt(i))) {
                lastDigitIndex = i;
                break;
            }
        }

        if (lastDigitIndex >= 0 && lastDigitIndex < mainPart.length() - 1) {
            String suffix = mainPart.substring(lastDigitIndex + 1);

            // Map common Analog Devices package codes
            return switch (suffix) {
                // QFN variants
                case "Z", "BZ", "CZ" -> "QFN";
                case "CCZ", "BCCZ" -> "LGA";  // Ceramic Leadless Chip Carrier
                case "CPZ", "BCPZ" -> "LFCSP";  // Lead Frame Chip Scale Package

                // SOIC variants
                case "R", "RZ", "ARZ" -> "SOIC";
                case "RT" -> "SOIC";
                case "RZV7" -> "SOIC";
                case "R7" -> "SOIC";

                // MSOP variants
                case "RM", "RMZ" -> "MSOP";

                // TSSOP variants
                case "RU", "RUZ" -> "TSSOP";

                // TSOT variants
                case "TRZ" -> "TSOT-23";
                case "TRM" -> "TSOT-23";

                // Special packages
                case "BSTZ" -> "LQFP";  // Low profile QFP
                case "BCQZ" -> "CQFP";  // Ceramic QFP
                case "BEYZ" -> "TQFP";  // Thin QFP

                default -> suffix;  // Return raw suffix if not recognized
            };
        }

        // Check second part after hyphen (e.g., AD7606-4BSTZ)
        if (parts.length > 1) {
            String suffix = parts[1];
            // Remove leading digits/letters for variant code
            int suffixDigitEnd = 0;
            while (suffixDigitEnd < suffix.length() && Character.isDigit(suffix.charAt(suffixDigitEnd))) {
                suffixDigitEnd++;
            }
            if (suffixDigitEnd < suffix.length()) {
                String pkgCode = suffix.substring(suffixDigitEnd);
                return switch (pkgCode) {
                    case "BSTZ" -> "LQFP";
                    case "BCQZ" -> "CQFP";
                    case "BRUZ", "BRU" -> "TSSOP";
                    default -> pkgCode;
                };
            }
        }

        return "";
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.OPAMP,
                ComponentType.OPAMP_AD,
                ComponentType.ADC_AD,
                ComponentType.DAC_AD,
                ComponentType.AMPLIFIER_AD,
                ComponentType.VOLTAGE_REFERENCE_AD,
                ComponentType.TEMPERATURE_SENSOR,
                ComponentType.TEMPERATURE_SENSOR_AD,
                ComponentType.ACCELEROMETER,
                ComponentType.ACCELEROMETER_AD,
                ComponentType.GYROSCOPE,
                ComponentType.GYROSCOPE_AD,
                ComponentType.IC
        );
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