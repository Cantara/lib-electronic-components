package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;


import java.util.Set;
import java.util.Collections;

public class InvSenseHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // 6-Axis IMUs (Accelerometer + Gyroscope)
        registry.addPattern(ComponentType.ACCELEROMETER, "^ICM-20[0-9]{3}.*");  // ICM Series
        registry.addPattern(ComponentType.GYROSCOPE, "^ICM-20[0-9]{3}.*");
        registry.addPattern(ComponentType.ACCELEROMETER, "^MPU-6[0-9]{3}.*");   // MPU-6xxx Series
        registry.addPattern(ComponentType.GYROSCOPE, "^MPU-6[0-9]{3}.*");

        // 9-Axis IMUs (Accelerometer + Gyroscope + Magnetometer)
        registry.addPattern(ComponentType.ACCELEROMETER, "^ICM-209[0-9]{2}.*"); // ICM-209xx
        registry.addPattern(ComponentType.GYROSCOPE, "^ICM-209[0-9]{2}.*");
        registry.addPattern(ComponentType.MAGNETOMETER, "^ICM-209[0-9]{2}.*");
        registry.addPattern(ComponentType.ACCELEROMETER, "^MPU-9[0-9]{3}.*");   // MPU-9xxx
        registry.addPattern(ComponentType.GYROSCOPE, "^MPU-9[0-9]{3}.*");
        registry.addPattern(ComponentType.MAGNETOMETER, "^MPU-9[0-9]{3}.*");

        // Gyroscopes
        registry.addPattern(ComponentType.GYROSCOPE, "^ITG-3[0-9]{3}.*");      // ITG Series
        registry.addPattern(ComponentType.GYROSCOPE, "^IVS-4[0-9]{3}.*");      // IVS Series

        // Accelerometers
        registry.addPattern(ComponentType.ACCELEROMETER, "^IAM-2[0-9]{3}.*");   // IAM Series
        registry.addPattern(ComponentType.ACCELEROMETER, "^IIM-4[0-9]{3}.*");   // IIM Series

        // Audio/Motion Processors
        registry.addPattern(ComponentType.IC, "^ICS-4[0-9]{4}.*");             // ICS Series
        registry.addPattern(ComponentType.IC, "^IAC-5[0-9]{4}.*");             // IAC Series
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.SENSOR,
            ComponentType.ACCELEROMETER,
            ComponentType.GYROSCOPE,
            ComponentType.MAGNETOMETER,
            ComponentType.IC  // For audio/motion processors
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle package codes in suffix
        String[] parts = mpn.split("[\\-\\s]");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];

            return switch (suffix) {
                case "QFN" -> "QFN";
                case "LGA" -> "LGA";
                case "BGA" -> "BGA";
                case "WLCSP" -> "WLCSP";
                case "CSP" -> "CSP";
                default -> {
                    // Extract embedded package codes
                    if (suffix.endsWith("Q")) yield "QFN";
                    if (suffix.endsWith("L")) yield "LGA";
                    if (suffix.endsWith("B")) yield "BGA";
                    if (suffix.endsWith("C")) yield "CSP";
                    yield suffix;
                }
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series (e.g., ICM-20948, MPU-6050)
        String[] parts = mpn.split("[\\-\\s]");
        if (parts.length >= 2) {
            // Return family plus model number
            return parts[0] + "-" + parts[1].replaceAll("[^0-9].*$", "");
        }
        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series with different packages are typically compatible
        if (series1.equals(series2)) {
            // Check interface compatibility
            String interface1 = extractInterface(mpn1);
            String interface2 = extractInterface(mpn2);

            if (!interface1.isEmpty() && !interface2.isEmpty() &&
                    !interface1.equals(interface2)) {
                return false;  // Different interfaces are not compatible
            }

            // Check voltage compatibility
            String voltage1 = extractVoltage(mpn1);
            String voltage2 = extractVoltage(mpn2);

            return isCompatibleVoltage(voltage1, voltage2);
        }

        // Check for known compatible series
        return isCompatibleSeries(series1, series2);
    }

    private String extractInterface(String mpn) {
        // Extract interface type from part number
        if (mpn.contains("-I2C") || mpn.endsWith("I")) return "I2C";
        if (mpn.contains("-SPI") || mpn.endsWith("S")) return "SPI";
        if (mpn.contains("-COMBO") || mpn.endsWith("C")) return "COMBO";
        return "";
    }

    private String extractVoltage(String mpn) {
        // Extract voltage specification
        if (mpn.contains("1.8") || mpn.contains("-18")) return "1.8V";
        if (mpn.contains("2.5") || mpn.contains("-25")) return "2.5V";
        if (mpn.contains("3.3") || mpn.contains("-33")) return "3.3V";
        return "";
    }

    private boolean isCompatibleVoltage(String voltage1, String voltage2) {
        if (voltage1.equals(voltage2)) return true;
        if (voltage1.isEmpty() || voltage2.isEmpty()) return true;

        // Lower voltage parts can typically work at higher voltages
        if (voltage1.equals("1.8V")) return true;
        if (voltage1.equals("2.5V") && !voltage2.equals("1.8V")) return true;

        return false;
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Define known compatible series pairs
        return (
                // Compatible 6-axis IMU series
                (series1.startsWith("MPU-6050") && series2.startsWith("ICM-20600")) ||
                        (series1.startsWith("ICM-20600") && series2.startsWith("MPU-6050")) ||

                        // Compatible 9-axis IMU series
                        (series1.startsWith("MPU-9250") && series2.startsWith("ICM-20948")) ||
                        (series1.startsWith("ICM-20948") && series2.startsWith("MPU-9250")) ||

                        // Compatible gyroscope series
                        (series1.startsWith("ITG-3200") && series2.startsWith("IVS-4200")) ||
                        (series1.startsWith("IVS-4200") && series2.startsWith("ITG-3200")) ||

                        // Compatible accelerometer series
                        (series1.startsWith("IAM-2000") && series2.startsWith("IIM-4000")) ||
                        (series1.startsWith("IIM-4000") && series2.startsWith("IAM-2000"))
        );
    }

    private boolean isCompatibleGyroRange(String range1, String range2) {
        try {
            // Extract numeric values (e.g., "2000" from "±2000°/s")
            int value1 = Integer.parseInt(range1.replaceAll("[^0-9]", ""));
            int value2 = Integer.parseInt(range2.replaceAll("[^0-9]", ""));

            // Higher range can replace lower range
            return value1 >= value2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCompatibleAccelRange(String range1, String range2) {
        try {
            // Extract numeric values (e.g., "16" from "±16g")
            int value1 = Integer.parseInt(range1.replaceAll("[^0-9]", ""));
            int value2 = Integer.parseInt(range2.replaceAll("[^0-9]", ""));

            // Higher range can replace lower range
            return value1 >= value2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}