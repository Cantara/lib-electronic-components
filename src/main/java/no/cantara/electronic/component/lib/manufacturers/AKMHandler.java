package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class AKMHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Magnetic Sensors
        registry.addPattern(ComponentType.MAGNETOMETER, "^AK89[0-9].*");  // 3-axis magnetic sensors
        registry.addPattern(ComponentType.MAGNETOMETER, "^AK099.*");      // High precision magnetic sensors

        // IMUs and Combined Sensors
        registry.addPattern(ComponentType.SENSOR, "^AK09[0-9].*");       // 9-axis sensors
        registry.addPattern(ComponentType.SENSOR, "^AK09911.*");         // 9-axis with accelerometer
        registry.addPattern(ComponentType.SENSOR, "^AK09912.*");         // 9-axis with gyroscope
        registry.addPattern(ComponentType.SENSOR, "^AK09913.*");         // 9-axis combined

        // Digital Compasses
        registry.addPattern(ComponentType.MAGNETOMETER, "^AK8963.*");    // 3-axis electronic compass
        registry.addPattern(ComponentType.MAGNETOMETER, "^AK8975.*");    // 3-axis electronic compass
        registry.addPattern(ComponentType.MAGNETOMETER, "^AK09918.*");   // 3-axis electronic compass

        // Hall Effect Sensors
        registry.addPattern(ComponentType.SENSOR, "^AK0991[0-9].*");    // Hall sensors

        // Audio ICs
        registry.addPattern(ComponentType.IC, "^AK449[0-9].*");         // Audio ADCs
        registry.addPattern(ComponentType.IC, "^AK479[0-9].*");         // Audio DACs
        registry.addPattern(ComponentType.IC, "^AK4490.*");             // Premium audio DAC
        registry.addPattern(ComponentType.IC, "^AK5386.*");             // Audio ADC
    }
    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.SENSOR);
        types.add(ComponentType.MAGNETOMETER);
    //    types.add(ComponentType.HALL_SENSOR);
    //    types.add(ComponentType.ADC);
    //    types.add(ComponentType.DAC);
        // Add AKM specific types if they exist in ComponentType enum
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String[] parts = mpn.split("-");
        if (parts.length > 0) {
            String mainPart = parts[0];
            String suffix = mainPart.replaceAll("^[A-Z0-9]+", "");

            return switch (suffix) {
                case "TR" -> "LGA";           // LGA package
                case "CS" -> "CSP";           // Chip-scale package
                case "WL" -> "WLCSP";         // Wafer-level CSP
                case "TS" -> "TSSOP";         // TSSOP package
                case "QFN" -> "QFN";          // QFN package
                case "BGA" -> "BGA";          // BGA package
                default -> suffix;
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract series code (first 6 characters for sensors, 5 for audio ICs)
        StringBuilder series = new StringBuilder();
        int count = 0;
        boolean isAudioIC = mpn.startsWith("AK4") || mpn.startsWith("AK5");
        int maxLength = isAudioIC ? 5 : 6;

        for (char c : mpn.toCharArray()) {
            if (count < maxLength && Character.isLetterOrDigit(c)) {
                series.append(c);
                count++;
            } else {
                break;
            }
        }
        return series.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (!series1.equals(series2)) {
            return false;
        }

        // Extract resolution and interface type
        String resolution1 = extractResolution(mpn1);
        String resolution2 = extractResolution(mpn2);

        String interface1 = extractInterface(mpn1);
        String interface2 = extractInterface(mpn2);

        // For sensors, check resolution and interface compatibility
        if (mpn1.startsWith("AK8") || mpn1.startsWith("AK09")) {
            return resolution1.equals(resolution2) &&
                    (interface1.equals(interface2) || interface1.isEmpty() || interface2.isEmpty());
        }

        // For audio ICs, check sampling rate and bit depth
        if (mpn1.startsWith("AK4") || mpn1.startsWith("AK5")) {
            String sampleRate1 = extractSampleRate(mpn1);
            String sampleRate2 = extractSampleRate(mpn2);
            return sampleRate1.equals(sampleRate2);
        }

        return false;
    }

    private String extractResolution(String mpn) {
        // Extract resolution from sensor part number
        if (mpn.contains("14BIT")) return "14";
        if (mpn.contains("16BIT")) return "16";
        return "";
    }

    private String extractInterface(String mpn) {
        // Extract interface type from part number
        if (mpn.contains("I2C") || mpn.endsWith("I")) return "I2C";
        if (mpn.contains("SPI") || mpn.endsWith("S")) return "SPI";
        return "";
    }

    private String extractSampleRate(String mpn) {
        // Extract sample rate for audio ICs
        if (mpn.contains("192")) return "192";
        if (mpn.contains("96")) return "96";
        if (mpn.contains("48")) return "48";
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}