package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Similarity calculator for sensor components.
 *
 * Handles temperature, accelerometer, gyroscope, humidity, pressure, and combined sensors.
 * Uses family matching and equivalent sensor groups for comparison.
 * Metadata infrastructure available for spec-based comparison when characteristics are known.
 */
public class SensorSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(SensorSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    public SensorSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    /**
     * Indicates whether this calculator is applicable for the given component type.
     *
     * @param type The component type to check
     * @return true if this calculator can handle the given type, false otherwise
     */
    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) return false;
        return type == ComponentType.SENSOR ||
                type == ComponentType.TEMPERATURE_SENSOR ||
                type.name().startsWith("TEMPERATURE_SENSOR_") ||
                type == ComponentType.ACCELEROMETER ||
                type.name().startsWith("ACCELEROMETER_") ||
                type.name().startsWith("SENSOR_");
    }


    /**
     * Calculates similarity between two sensor components.
     *
     * @param mpn1 First sensor's MPN
     * @param mpn2 Second sensor's MPN
     * @param registry Pattern registry for component matching
     * @return Similarity score between 0.0 and 1.0
     */
    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        logger.debug("Comparing sensors: {} vs {}", mpn1, mpn2);

        // First check if they're even sensors
        if (!isSensor(mpn1) || !isSensor(mpn2)) {
            logger.debug("One or both parts are not sensors");
            return 0.0;
        }

        // Check if metadata is available (for future spec-based enhancement)
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.SENSOR);
        if (metadataOpt.isEmpty()) {
            logger.trace("No metadata found for SENSOR, using family matching approach");
        }

        // Sensor comparison uses family matching approach
        return calculateFamilyBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity based on sensor family, type, and equivalent groups.
     * This is the primary sensor comparison method.
     */
    private double calculateFamilyBasedSimilarity(String mpn1, String mpn2) {

        // Get base parts without package codes
        String base1 = getBasePart(mpn1);
        String base2 = getBasePart(mpn2);

        logger.debug("Base parts: {} vs {}", base1, base2);

        // Determine sensor families
        SensorFamily family1 = determineSensorFamily(mpn1);
        SensorFamily family2 = determineSensorFamily(mpn2);

        // Different sensor families should have low similarity
        if (family1 != family2) {
            logger.debug("Different sensor families");
            return LOW_SIMILARITY;
        }

        // For accelerometers, keep the complete part number including grade
        if (family1 == SensorFamily.ACCELEROMETER) {
            if (base1.equals(base2)) {
                return HIGH_SIMILARITY; // Same accelerometer with different packaging
            }
            return LOW_SIMILARITY; // Different accelerometer models
        }

        // Same exact base part (ignoring package/suffix)
        if (base1.equals(base2)) {
            String pkg1 = getPackageCode(mpn1);
            String pkg2 = getPackageCode(mpn2);

            // Check package compatibility
            if (arePackagesCompatible(pkg1, pkg2)) {
                logger.debug("Same sensor with compatible packages");
                return HIGH_SIMILARITY;
            }
            logger.debug("Same sensor with incompatible packages");
            return MEDIUM_SIMILARITY;
        }

        // Handle different sensor types
        switch (family1) {
            case TEMPERATURE:
                if (areEquivalentTemperatureSensors(mpn1, mpn2)) {
                    String pkg1 = getPackageCode(mpn1);
                    String pkg2 = getPackageCode(mpn2);
                    return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
                }
                // Different temperature sensor models
                return LOW_SIMILARITY;

            case ACCELEROMETER:
                // Different accelerometer models should have low similarity
                // (ADXL345 vs ADXL346 are different)
                if (!base1.equals(base2)) {
                    return LOW_SIMILARITY;
                }
                // Same accelerometer model, check packages
                String pkg1 = getPackageCode(mpn1);
                String pkg2 = getPackageCode(mpn2);
                return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;

            case GYROSCOPE:
                // Gyroscopes follow similar rules to accelerometers
                if (!base1.equals(base2)) {
                    return LOW_SIMILARITY;
                }
                pkg1 = getPackageCode(mpn1);
                pkg2 = getPackageCode(mpn2);
                return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;

            case HUMIDITY:
                // Humidity sensors (e.g., SHT3x series)
                if (areEquivalentHumiditySensors(mpn1, mpn2)) {
                    pkg1 = getPackageCode(mpn1);
                    pkg2 = getPackageCode(mpn2);
                    return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
                }
                return LOW_SIMILARITY;

            case PRESSURE:
                // Pressure sensors (e.g., BMP280 vs BMP380)
                if (areEquivalentPressureSensors(mpn1, mpn2)) {
                    pkg1 = getPackageCode(mpn1);
                    pkg2 = getPackageCode(mpn2);
                    return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
                }
                return LOW_SIMILARITY;

            case COMBINED:
                // Combined sensors (e.g., BME280 - temp, humidity, pressure)
                if (areEquivalentCombinedSensors(mpn1, mpn2)) {
                    pkg1 = getPackageCode(mpn1);
                    pkg2 = getPackageCode(mpn2);
                    return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
                }
                return LOW_SIMILARITY;

            default:
                // Generic sensors or unknown types
                // Only consider same base part with compatible packages
                if (base1.equals(base2)) {
                    pkg1 = getPackageCode(mpn1);
                    pkg2 = getPackageCode(mpn2);
                    return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
                }
                return LOW_SIMILARITY;
        }
    }

    private SensorFamily determineSensorFamily(String mpn) {
        if (mpn == null) return SensorFamily.UNKNOWN;
        String upperMpn = mpn.toUpperCase();

        // Temperature sensors
        if (upperMpn.startsWith("LM35") ||
                upperMpn.startsWith("DS18") ||
                upperMpn.startsWith("TMP")) {
            return SensorFamily.TEMPERATURE;
        }

        // Accelerometers
        if (upperMpn.startsWith("ADXL") ||
                upperMpn.startsWith("MMA") ||
                upperMpn.startsWith("LIS")) {
            return SensorFamily.ACCELEROMETER;
        }

        // Add more families as needed...
        return SensorFamily.UNKNOWN;
    }

    private double compareTemperatureSensors(String mpn1, String mpn2,
                                             String base1, String base2) {
        // Same base part indicates same sensor family
        if (base1.equals(base2)) {
            String pkg1 = getPackageCode(mpn1);
            String pkg2 = getPackageCode(mpn2);
            return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
        }

        // Check equivalent groups for temperature sensors
        if (areEquivalentTemperatureSensors(mpn1, mpn2)) {
            return HIGH_SIMILARITY;
        }

        return LOW_SIMILARITY;
    }

    private double compareAccelerometers(String mpn1, String mpn2,
                                         String base1, String base2) {
        // Exact same accelerometer with different package/suffix
        if (base1.equals(base2)) {
            String pkg1 = getPackageCode(mpn1);
            String pkg2 = getPackageCode(mpn2);
            return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
        }

        // Different accelerometer models should have low similarity
        // ADXL345 vs ADXL346 are different models
        return LOW_SIMILARITY;
    }

    /**
     * Checks if two temperature sensors are considered equivalent.
     */
    private boolean areEquivalentTemperatureSensors(String mpn1, String mpn2) {
        String norm1 = normalizeMPN(mpn1);
        String norm2 = normalizeMPN(mpn2);

        // DS18B20 variants (including +, Z suffixes)
        if (norm1.startsWith("DS18B20") && norm2.startsWith("DS18B20")) {
            return true;  // All DS18B20 variants are equivalent
        }

        // LM35 variants (D/C/A grades are equivalent within their series)
        if (norm1.startsWith("LM35") && norm2.startsWith("LM35")) {
            String suffix1 = norm1.substring(4);
            String suffix2 = norm2.substring(4);
            // Consider D/C/A variants equivalent within their grade
            if (suffix1.matches("[DCA]?") && suffix2.matches("[DCA]?")) {
                return true;
            }
        }

        // TMP36 variants
        if (norm1.startsWith("TMP36") && norm2.startsWith("TMP36")) {
            return true;  // All TMP36 variants are equivalent
        }

        // MAX31820 variants (compatible with DS18B20)
        if ((norm1.startsWith("DS18B20") && norm2.startsWith("MAX31820")) ||
                (norm1.startsWith("MAX31820") && norm2.startsWith("DS18B20"))) {
            return true;  // MAX31820 is compatible with DS18B20
        }

        return false;
    }

    /**
     * Checks if two humidity sensors are considered equivalent.
     */
    private boolean areEquivalentHumiditySensors(String mpn1, String mpn2) {
        String norm1 = normalizeMPN(mpn1);
        String norm2 = normalizeMPN(mpn2);

        // SHT3x series (SHT30/31/35 are compatible within accuracy grades)
        if (norm1.startsWith("SHT3") && norm2.startsWith("SHT3")) {
            char grade1 = norm1.charAt(4);
            char grade2 = norm2.charAt(4);
            // Same series number or adjacent grades
            return grade1 == grade2 || Math.abs(grade1 - grade2) == 1;
        }

        // HIH6130/6131 series
        if (norm1.startsWith("HIH613") && norm2.startsWith("HIH613")) {
            return true;  // Compatible series
        }

        // HDC1080/2080 series
        if ((norm1.startsWith("HDC1080") && norm2.startsWith("HDC2080")) ||
                (norm1.startsWith("HDC2080") && norm2.startsWith("HDC1080"))) {
            return true;  // Compatible series
        }

        return false;
    }

    /**
     * Checks if two pressure sensors are considered equivalent.
     */
    private boolean areEquivalentPressureSensors(String mpn1, String mpn2) {
        String norm1 = normalizeMPN(mpn1);
        String norm2 = normalizeMPN(mpn2);

        // BMP280/BME280 pressure compatibility
        if ((norm1.startsWith("BMP280") && norm2.startsWith("BME280")) ||
                (norm1.startsWith("BME280") && norm2.startsWith("BMP280"))) {
            return true;  // BMP280 pressure compatible with BME280
        }

        // MS5611/5607 series
        if (norm1.startsWith("MS56") && norm2.startsWith("MS56")) {
            // Check if they're in the same sub-series
            String num1 = norm1.substring(4, 6);
            String num2 = norm2.substring(4, 6);
            return num1.equals(num2) ||
                    (num1.equals("11") && num2.equals("07")) ||
                    (num1.equals("07") && num2.equals("11"));
        }

        return false;
    }

    /**
     * Checks if two combined sensors are considered equivalent.
     */
    private boolean areEquivalentCombinedSensors(String mpn1, String mpn2) {
        String norm1 = normalizeMPN(mpn1);
        String norm2 = normalizeMPN(mpn2);

        // BME280/680 series (temperature, humidity, pressure)
        if (norm1.startsWith("BME") && norm2.startsWith("BME")) {
            String num1 = norm1.substring(3, 6);
            String num2 = norm2.substring(3, 6);
            // 280 and 680 are different generations
            return num1.equals(num2);
        }

        // MPU6050/9250 series (accelerometer + gyroscope)
        if (norm1.startsWith("MPU") && norm2.startsWith("MPU")) {
            String num1 = norm1.substring(3, 7);
            String num2 = norm2.substring(3, 7);
            // Different models are not equivalent
            return num1.equals(num2);
        }

        return false;
    }




    private double compareGyroscopes(String mpn1, String mpn2,
                                     String base1, String base2) {
        // Same logic as accelerometers
        if (base1.equals(base2)) {
            String pkg1 = getPackageCode(mpn1);
            String pkg2 = getPackageCode(mpn2);
            return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
        }
        return LOW_SIMILARITY;
    }

    private double compareGenericSensors(String mpn1, String mpn2,
                                         String base1, String base2) {
        if (base1.equals(base2)) {
            String pkg1 = getPackageCode(mpn1);
            String pkg2 = getPackageCode(mpn2);
            return arePackagesCompatible(pkg1, pkg2) ? HIGH_SIMILARITY : MEDIUM_SIMILARITY;
        }
        return LOW_SIMILARITY;
    }

    /**
     * Checks if the given MPN represents a sensor.
     */
    private boolean isSensor(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Temperature sensors
        if (upperMpn.matches("^LM35[A-Z0-9]*")) return true;  // LM35 series
        if (upperMpn.matches("^DS18[A-Z0-9]*\\+?")) return true;  // DS18xxx series
        if (upperMpn.matches("^TMP[0-9]+.*")) return true;    // TMP series
        if (upperMpn.matches("^MAX318[0-9]+.*")) return true; // MAX318xx series

        // Accelerometers
        if (upperMpn.matches("^ADXL[0-9]+.*")) return true;   // ADXL series
        if (upperMpn.matches("^MMA[0-9]+.*")) return true;    // MMA series
        if (upperMpn.matches("^LIS[23][A-Z0-9]+.*")) return true; // LIS2/3 series
        if (upperMpn.matches("^BMI[0-9]+.*")) return true;    // BMI series
        if (upperMpn.matches("^ICM[0-9]+.*")) return true;    // ICM series

        // Gyroscopes
        if (upperMpn.matches("^L3GD[0-9]+.*")) return true;   // L3GD series
        if (upperMpn.matches("^ITG[0-9]+.*")) return true;    // ITG series
        if (upperMpn.matches("^MPU[0-9]+.*")) return true;    // MPU series

        // Humidity sensors
        if (upperMpn.matches("^SHT[0-9]+.*")) return true;    // SHT series
        if (upperMpn.matches("^HIH[0-9]+.*")) return true;    // HIH series
        if (upperMpn.matches("^AM[0-9]+.*")) return true;     // AM series

        // Multi-sensors
        if (upperMpn.matches("^BME[0-9]+.*")) return true;    // BME series
        if (upperMpn.matches("^BMP[0-9]+.*")) return true;    // BMP series
        if (upperMpn.matches("^LSM[0-9]+.*")) return true;    // LSM series

        // Pressure sensors
        if (upperMpn.matches("^MS[0-9]+.*")) return true;     // MS series
        if (upperMpn.matches("^LPS[0-9]+.*")) return true;    // LPS series

        return false;
    }


    /**
     * Gets the base part of the sensor MPN (without package codes).
     */
    private String getBasePart(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // ADXL series accelerometers (keep grade and interface type)
        if (upperMpn.startsWith("ADXL")) {
            // Remove reel and packaging suffixes but keep part number and grade
            return upperMpn.replaceAll("(?:-R[LT]|-REEL|-TUBE)$", "");
        }

        // DS18B20 series
        if (upperMpn.startsWith("DS18B20")) {
            return "DS18B20";  // Always return base part for DS18B20 variants
        }

        // LM35 series
        if (upperMpn.matches("^LM35.*")) {
            return upperMpn.replaceAll("^(LM35[A-Z]?).*$", "$1");
        }

        // TMP series
        if (upperMpn.matches("^TMP[0-9]+.*")) {
            return upperMpn.replaceAll("^(TMP[0-9]+).*$", "$1");
        }

        // Generic extraction
        return upperMpn.replaceAll("([A-Z0-9]+?)(?:[A-Z]\\d*)?(?:-.*|\\+.*|/.*)?$", "$1");
    }

    /**
     * Normalizes an MPN by removing common suffixes and variations.
     */
    private String normalizeMPN(String mpn) {
        if (mpn == null) return "";
        String normalized = mpn.toUpperCase()
                .replaceAll("\\+$", "")     // Remove trailing +
                .replaceAll("-.*$", "")     // Remove everything after -
                .replaceAll("/.*$", "")     // Remove everything after /
                .replaceAll("Z$", "");      // Remove trailing Z

        // Special handling for DS18B20
        if (normalized.startsWith("DS18B20")) {
            return "DS18B20";
        }

        return normalized;
    }

    /**
     * Checks if two package codes are compatible.
     */
    private boolean arePackagesCompatible(String pkg1, String pkg2) {
        if (pkg1 == null || pkg2 == null) return false;

        // Clean packages (remove reel/tube suffixes)
        String clean1 = cleanPackageCode(pkg1);
        String clean2 = cleanPackageCode(pkg2);

        // Exact same package or same package with reel variants
        if (clean1.equals(clean2)) return true;
        if (pkg1.replaceAll("-R[LT]$", "").equals(pkg2.replaceAll("-R[LT]$", ""))) return true;

        // Temperature sensor packages
        Set<String> throughHoleTemp = Set.of("TO-92", "TO-226", "TO-220");
        Set<String> smtTemp = Set.of("SOT-23", "SOIC", "MSOP");

        // MEMS packages (accelerometers, gyroscopes)
        Set<String> lccLga = Set.of("LCC", "LGA", "LFCSP", "QFN", "BCC");
        Set<String> qfpSoic = Set.of("QFP", "LQFP", "TQFP", "SOIC");

        // Package group compatibility checks
        if (throughHoleTemp.contains(clean1) && throughHoleTemp.contains(clean2)) return true;
        if (smtTemp.contains(clean1) && smtTemp.contains(clean2)) return true;
        if (lccLga.contains(clean1) && lccLga.contains(clean2)) return true;
        if (qfpSoic.contains(clean1) && qfpSoic.contains(clean2)) return true;

        return false;
    }


    /**
     * Clean package code by removing reel/tube suffixes
     */
    private String cleanPackageCode(String pkg) {
        if (pkg == null) return "";
        // For ADXL packages, keep the base package code
        if (pkg.contains("BCC") || pkg.contains("LGA") || pkg.contains("LFCSP")) {
            return pkg.replaceAll("(?:-R[LT]|-REEL|-TUBE)$", "");
        }
        return pkg.replaceAll("(?:-R[LT]|-REEL|-TUBE)$", "")  // Remove reel/tube suffix
                .replaceAll("(?:REEL|TUBE)$", "");           // Remove without hyphen
    }


    /**
     * Extracts the package code from an MPN.
     */
    private String getPackageCode(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // Handle MEMS packages first (most specific)
        if (upperMpn.contains("LFCSP")) return "LFCSP";
        if (upperMpn.contains("LGA")) return "LGA";
        if (upperMpn.contains("BGA")) return "BGA";
        if (upperMpn.contains("QFN")) return "QFN";
        if (upperMpn.contains("SOIC")) return "SOIC";
        if (upperMpn.contains("TQFP")) return "TQFP";
        if (upperMpn.contains("LQFP")) return "LQFP";
        if (upperMpn.contains("QFP")) return "QFP";

        // Special handling for DS18B20 series
        if (upperMpn.startsWith("DS18B20")) {
            if (upperMpn.endsWith("+")) return "TO-92";  // Standard package
            if (upperMpn.endsWith("Z")) return "TO-92";  // TO-92 package
            if (upperMpn.endsWith("/T")) return "TO-92"; // TO-92 variant
            if (upperMpn.endsWith("U")) return "MSOP";   // MSOP package
            if (upperMpn.matches(".*SMD.*")) return "SOIC"; // Surface mount package
            return "TO-92";  // Default package for DS18B20
        }

        // Handle reel/tube/tray variations
        if (upperMpn.endsWith("-RL") || upperMpn.endsWith("-RT")) {
            String basePkg = extractBasePackage(upperMpn.substring(0, upperMpn.length() - 3));
            return basePkg + upperMpn.substring(upperMpn.length() - 3);
        }

        // Standard temperature sensor packages
        if (upperMpn.endsWith("T")) return "TO-92";
        if (upperMpn.endsWith("Z")) return "TO-92";
        if (upperMpn.endsWith("DT")) return "SOT-23";
        if (upperMpn.endsWith("RT")) return "SOT-23";

        // Extract base package without reel/tube suffix
        return extractBasePackage(upperMpn);
    }

    /**
     * Extracts the base package code without reel/tube suffix
     */
    private String extractBasePackage(String mpn) {
        // Match package code pattern
        java.util.regex.Matcher matcher =
                Pattern.compile(".*?([A-Z]+(?:-[A-Z0-9]+)?)(?:-R[LT])?$").matcher(mpn);
        if (matcher.find()) {
            String pkg = matcher.group(1);
            // Don't return the entire component name as a package code
            if (!pkg.startsWith("DS18B20") &&
                    !pkg.startsWith("ADXL") &&
                    !pkg.startsWith("BME")) {
                return pkg;
            }
        }
        return "";
    }
    /**
     * Checks if two sensors are considered equivalent.
     */
    private boolean areEquivalentSensors(String mpn1, String mpn2) {
        String norm1 = normalizeMPN(mpn1);
        String norm2 = normalizeMPN(mpn2);

        // DS18B20 variants (including +)
        if (norm1.startsWith("DS18B20") && norm2.startsWith("DS18B20")) {
            // All DS18B20 variants are considered equivalent
            return true;
        }

        // LM35 variants
        if (norm1.startsWith("LM35") && norm2.startsWith("LM35")) {
            String suffix1 = norm1.substring(4);
            String suffix2 = norm2.substring(4);
            // Consider D/C/A variants equivalent
            return suffix1.matches("[DCA]?") && suffix2.matches("[DCA]?");
        }

        return false;
    }
    /**
     * Compares two instances of the same sensor with different packages/variants.
     */
    private double compareSameSensor(String mpn1, String mpn2) {
        // Extract package codes
        String pkg1 = getPackageCode(mpn1);
        String pkg2 = getPackageCode(mpn2);

        logger.debug("Same sensor, comparing packages: {} vs {}", pkg1, pkg2);

        // Same package or compatible packages
        if (areCompatiblePackages(pkg1, pkg2)) {
            return HIGH_SIMILARITY;
        }

        return MEDIUM_SIMILARITY;
    }





    /**
     * Compares two different sensor families.
     */
    private double compareDifferentSensors(String mpn1, String mpn2) {
        // If both are temperature sensors
        if (isTemperatureSensor(mpn1) && isTemperatureSensor(mpn2)) {
            return MEDIUM_SIMILARITY;
        }

        // If both are humidity sensors
        if (isHumiditySensor(mpn1) && isHumiditySensor(mpn2)) {
            return MEDIUM_SIMILARITY;
        }

        // Different sensor types
        return LOW_SIMILARITY;
    }

    /**
     * Checks if the MPN represents a temperature sensor.
     */
    private boolean isTemperatureSensor(String mpn) {
        String upperMpn = mpn.toUpperCase();
        return upperMpn.startsWith("LM35") ||
                upperMpn.startsWith("DS18") ||
                upperMpn.startsWith("TMP") ||
                upperMpn.matches("^AD59[0-9].*");
    }

    /**
     * Checks if the MPN represents a humidity sensor.
     */
    private boolean isHumiditySensor(String mpn) {
        String upperMpn = mpn.toUpperCase();
        return upperMpn.startsWith("SHT") ||
                upperMpn.startsWith("HIH") ||
                upperMpn.startsWith("AM");
    }



    /**
     * Extract package suffix from MPN
     */
    private String extractPackageSuffix(String mpn) {
        // Match package suffix pattern
        java.util.regex.Matcher matcher =
                Pattern.compile(".*?([A-Z]+(?:-[A-Z0-9]+)?)$").matcher(mpn);
        if (matcher.find()) {
            String suffix = matcher.group(1);
            // Don't return the entire DS18B20 part as a package code
            if (!suffix.startsWith("DS18B20")) {
                return suffix;
            }
        }
        return "";
    }


    /**
     * Checks if two package codes are compatible.
     */
    private boolean areCompatiblePackages(String pkg1, String pkg2) {
        if (pkg1.equals(pkg2)) return true;

        // DS18B20 variants are all considered compatible
        if (pkg1.equals("TO-92") && pkg2.equals("TO-92")) return true;
        if (pkg1.equals("TO-92") && pkg2.startsWith("DS18B20")) return true;
        if (pkg2.equals("TO-92") && pkg1.startsWith("DS18B20")) return true;

        // Common compatible pairs
        if ((pkg1.equals("TO-92") && pkg2.equals("TO-226")) ||
                (pkg1.equals("TO-226") && pkg2.equals("TO-92"))) {
            return true;
        }

        if ((pkg1.equals("SOT-23") && pkg2.equals("TO-236")) ||
                (pkg1.equals("TO-236") && pkg2.equals("SOT-23"))) {
            return true;
        }

        return false;
    }

    private enum SensorFamily {
        TEMPERATURE,
        ACCELEROMETER,
        GYROSCOPE,
        HUMIDITY,
        PRESSURE,
        COMBINED,
        UNKNOWN
    }

}