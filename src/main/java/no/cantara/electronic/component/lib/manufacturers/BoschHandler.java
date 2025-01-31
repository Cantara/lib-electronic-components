package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class BoschHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Accelerometers
        registry.addPattern(ComponentType.ACCELEROMETER, "^BMA[0-9].*");     // Digital accelerometers
        registry.addPattern(ComponentType.ACCELEROMETER_BOSCH, "^BMA[0-9].*");
        registry.addPattern(ComponentType.ACCELEROMETER, "^SMB[0-9].*");     // Analog accelerometers
        registry.addPattern(ComponentType.ACCELEROMETER_BOSCH, "^SMB[0-9].*");

        // Gyroscopes
        registry.addPattern(ComponentType.GYROSCOPE, "^BMG[0-9].*");        // Digital gyroscopes
        registry.addPattern(ComponentType.GYROSCOPE_BOSCH, "^BMG[0-9].*");

        // IMUs (Inertial Measurement Units)
        registry.addPattern(ComponentType.SENSOR, "^BMI[0-9].*");           // IMU sensors
        registry.addPattern(ComponentType.ACCELEROMETER_BOSCH, "^BMI[0-9].*");
        registry.addPattern(ComponentType.GYROSCOPE_BOSCH, "^BMI[0-9].*");

        // Magnetometers
        registry.addPattern(ComponentType.MAGNETOMETER, "^BMM[0-9].*");     // Magnetometers
        registry.addPattern(ComponentType.MAGNETOMETER, "^BMM[0-9].*");

        // Pressure Sensors
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^BMP[0-9].*");  // Pressure sensors
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^BMP[0-9].*");
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^BMP[0-9].*[Px]"); // Pressure sensor variants

        // Environmental Sensors
        registry.addPattern(ComponentType.HUMIDITY_SENSOR, "^BME[0-9].*");  // Environmental sensors
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^BME[0-9].*");  // (Combined humidity, pressure, temp)
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^BME[0-9].*");

        // Gas Sensors
        registry.addPattern(ComponentType.SENSOR, "^BME68[0-9].*");        // Gas sensors
        registry.addPattern(ComponentType.SENSOR, "^BME69[0-9].*");        // Advanced gas sensors
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.SENSOR);
        types.add(ComponentType.ACCELEROMETER);
        types.add(ComponentType.ACCELEROMETER_BOSCH);
        types.add(ComponentType.GYROSCOPE);
        types.add(ComponentType.GYROSCOPE_BOSCH);
        types.add(ComponentType.IMU_BOSCH);
        types.add(ComponentType.MAGNETOMETER);
        types.add(ComponentType.MAGNETOMETER_BOSCH);
        types.add(ComponentType.PRESSURE_SENSOR);
        types.add(ComponentType.PRESSURE_SENSOR_BOSCH);
        types.add(ComponentType.HUMIDITY_SENSOR);
        types.add(ComponentType.HUMIDITY_SENSOR_BOSCH);
        types.add(ComponentType.TEMPERATURE_SENSOR);
        types.add(ComponentType.TEMPERATURE_SENSOR_BOSCH);
        types.add(ComponentType.GAS_SENSOR_BOSCH);
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
                case "FB" -> "LGA";          // LGA package
                case "FL" -> "LGA";          // Ultra-small LGA
                case "MI" -> "LGA-METAL";    // Metal lid LGA
                case "TR" -> "LGA";          // Tape & Reel LGA
                case "SG" -> "SMD";          // SMD package
                case "WB" -> "WLCSP";        // Wafer-level CSP
                case "CP" -> "CSP";          // Chip-scale package
                default -> suffix;
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series (e.g., BMA456, BMI270)
        StringBuilder series = new StringBuilder();
        boolean foundDigit = false;

        for (char c : mpn.toCharArray()) {
            if (Character.isLetter(c) ||
                    (Character.isDigit(c) && (!foundDigit || series.length() < 6))) {
                series.append(c);
                if (Character.isDigit(c)) {
                    foundDigit = true;
                }
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

        // Same series with different packages are typically compatible
        if (series1.equals(series2)) {
            // Check output interface compatibility
            String interface1 = extractInterface(mpn1);
            String interface2 = extractInterface(mpn2);

            if (!interface1.isEmpty() && !interface2.isEmpty() &&
                    !interface1.equals(interface2)) {
                return false;  // Different interfaces are not compatible
            }

            // Check temperature range compatibility
            String temp1 = extractTempRange(mpn1);
            String temp2 = extractTempRange(mpn2);

            return isCompatibleTempRange(temp1, temp2);
        }

        // Check for known compatible series
        return isCompatibleSeries(series1, series2);
    }

    private String extractInterface(String mpn) {
        // Extract interface type from part number
        if (mpn.contains("-I2C")) return "I2C";
        if (mpn.contains("-SPI")) return "SPI";
        if (mpn.contains("-ANA")) return "ANALOG";
        return "";
    }

    private String extractTempRange(String mpn) {
        // Extract temperature range indicator
        if (mpn.contains("-T")) return "T";    // Standard (-40 to +85°C)
        if (mpn.contains("-H")) return "H";    // High temp (-40 to +105°C)
        if (mpn.contains("-L")) return "L";    // Low temp (-40 to +65°C)
        if (mpn.contains("-A")) return "A";    // Automotive (-40 to +125°C)
        return "";
    }

    private boolean isCompatibleTempRange(String range1, String range2) {
        if (range1.equals(range2)) return true;
        if (range1.equals("A")) return true;  // Automotive can replace any
        if (range1.equals("H") && !range2.equals("A")) return true;
        if (range1.equals("T") && (range2.equals("L") || range2.isEmpty())) return true;
        if (range1.isEmpty() || range2.isEmpty()) return true;
        return false;
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Define known compatible series pairs
        return (
                // Compatible accelerometer series
                (series1.equals("BMA456") && series2.equals("BMA455")) ||
                        (series1.equals("BMA455") && series2.equals("BMA456")) ||

                        // Compatible IMU series
                        (series1.equals("BMI270") && series2.equals("BMI160")) ||
                        (series1.equals("BMI160") && series2.equals("BMI270")) ||

                        // Compatible pressure sensor series
                        (series1.equals("BMP390") && series2.equals("BMP388")) ||
                        (series1.equals("BMP388") && series2.equals("BMP390")) ||

                        // Compatible environmental sensor series
                        (series1.equals("BME680") && series2.equals("BME688")) ||
                        (series1.equals("BME688") && series2.equals("BME680"))
        );
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}