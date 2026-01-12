package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;


import java.util.Set;
import java.util.Collections;

public class MelexisHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Hall Effect Sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90242.*");     // Hall switches
        registry.addPattern(ComponentType.SENSOR, "^MLX90248.*");     // Hall latches
        registry.addPattern(ComponentType.SENSOR, "^MLX90251.*");     // Hall current sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90288.*");     // Programmable Hall sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90293.*");     // 3D Hall sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90363.*");     // Triaxis position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90371.*");     // 3D position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90372.*");     // 3D position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90377.*");     // Position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90378.*");     // Position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90381.*");     // Position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90382.*");     // Position sensors

        // Temperature Sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^MLX90614.*");   // IR temperature sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^MLX90632.*");   // Medical IR sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^MLX90640.*");   // IR array sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^MLX90641.*");   // IR array sensors
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^MLX90393.*");   // Temperature+magnetic

        // Current Sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX91204.*");     // Current sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX91205.*");     // Current sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX91206.*");     // Current sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX91207.*");     // Current sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX91208.*");     // Current sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX91216.*");     // Current sensors

        // Motor Position Sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90380.*");     // Motor position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90385.*");     // Motor position sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX90367.*");     // Motor position sensors

        // Optical Sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX75305.*");     // Light sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX75306.*");     // Light sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX75307.*");     // Light sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX75308.*");     // Light sensors
        registry.addPattern(ComponentType.SENSOR, "^MLX75309.*");     // Light sensors
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.SENSOR,
            ComponentType.MAGNETOMETER,
            ComponentType.TEMPERATURE_SENSOR
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String[] parts = mpn.split("-");
        if (parts.length > 0) {
            String mainPart = parts[0];
            String suffix = mainPart.replaceAll("^[A-Z0-9]+", "");

            return switch (suffix) {
                case "AAA" -> "TO-92UA";     // TO-92 package unibody
                case "BAA" -> "TO-92S";      // TO-92 package standard
                case "ESF" -> "SOIC-8";      // SOIC-8 package
                case "DCB" -> "TO-220";      // TO-220 package
                case "DCA" -> "TO-220";      // TO-220 package alternative
                case "LUA" -> "QFN-16";      // 4x4 QFN-16
                case "LXS" -> "QFN-24";      // 4x4 QFN-24
                case "LQV" -> "TQFP-44";     // TQFP-44 package
                case "TUA" -> "TO-18";       // TO-18 package
                case "TUB" -> "TO-18";       // TO-18 package with window
                default -> suffix;
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract series code (first 8 characters for most Melexis parts)
        StringBuilder series = new StringBuilder();
        int count = 0;

        for (char c : mpn.toCharArray()) {
            if (count < 8 && (Character.isLetterOrDigit(c))) {
                series.append(c);
                count++;
            } else if (c == '-' || !Character.isLetterOrDigit(c)) {
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

        // Must be same base series for compatibility
        if (!series1.equals(series2)) {
            return false;
        }

        // Extract specific features
        String interface1 = extractInterface(mpn1);
        String interface2 = extractInterface(mpn2);

        String resolution1 = extractResolution(mpn1);
        String resolution2 = extractResolution(mpn2);

        // For temperature sensors, check field of view
        if (mpn1.startsWith("MLX9061") || mpn1.startsWith("MLX9063")) {
            String fov1 = extractFieldOfView(mpn1);
            String fov2 = extractFieldOfView(mpn2);
            if (!fov1.equals(fov2)) {
                return false;
            }
        }

        // For Hall sensors, check magnetic characteristics
        if (mpn1.startsWith("MLX902") || mpn1.startsWith("MLX903")) {
            String sens1 = extractSensitivity(mpn1);
            String sens2 = extractSensitivity(mpn2);
            if (!sens1.equals(sens2)) {
                return false;
            }
        }

        // Check interface compatibility
        if (!interface1.isEmpty() && !interface2.isEmpty() &&
                !interface1.equals(interface2)) {
            return false;
        }

        // Check resolution compatibility (higher resolution can replace lower)
        if (!resolution1.isEmpty() && !resolution2.isEmpty()) {
            try {
                int res1 = Integer.parseInt(resolution1);
                int res2 = Integer.parseInt(resolution2);
                if (res1 < res2) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    private String extractInterface(String mpn) {
        // Extract interface type
        if (mpn.contains("-I") || mpn.endsWith("I")) return "I2C";
        if (mpn.contains("-P") || mpn.endsWith("P")) return "PWM";
        if (mpn.contains("-A") || mpn.endsWith("A")) return "ANALOG";
        if (mpn.contains("-S") || mpn.endsWith("S")) return "SPI";
        return "";
    }

    private String extractResolution(String mpn) {
        // Extract resolution (bits)
        if (mpn.contains("8B")) return "8";
        if (mpn.contains("10B")) return "10";
        if (mpn.contains("12B")) return "12";
        if (mpn.contains("14B")) return "14";
        if (mpn.contains("16B")) return "16";
        return "";
    }

    private String extractFieldOfView(String mpn) {
        // Extract field of view for IR sensors
        if (mpn.contains("5D")) return "5";     // 5 degrees
        if (mpn.contains("10D")) return "10";   // 10 degrees
        if (mpn.contains("35D")) return "35";   // 35 degrees
        if (mpn.contains("90D")) return "90";   // 90 degrees
        if (mpn.contains("100D")) return "100"; // 100 degrees
        return "";
    }

    private String extractSensitivity(String mpn) {
        // Extract magnetic sensitivity for Hall sensors
        if (mpn.contains("HC")) return "HIGH";    // High sensitivity
        if (mpn.contains("MC")) return "MEDIUM";  // Medium sensitivity
        if (mpn.contains("LC")) return "LOW";     // Low sensitivity
        if (mpn.contains("ULC")) return "ULTRA";  // Ultra-low sensitivity
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}