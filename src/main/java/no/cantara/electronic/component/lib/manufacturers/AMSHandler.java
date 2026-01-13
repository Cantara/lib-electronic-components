package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * Handler for ams-OSRAM (Austria Microsystems merged with OSRAM Opto) sensor products.
 *
 * Note: OSRAM LEDs are handled by the existing OSRAMHandler. This handler covers
 * ams sensor products including spectral sensors, color sensors, light sensors,
 * proximity sensors, position sensors, and environmental sensors.
 *
 * Product Families:
 * - AS72xx - Spectral Sensors (AS7261, AS7262, AS7263, AS7265)
 * - AS73xx - Color Sensors (AS7341, AS7343)
 * - TSL2xxx - Light-to-Digital Sensors (TSL2561, TSL2591)
 * - TMD2xxx - Proximity/ALS Sensors (TMD2671, TMD2772, TMD26721)
 * - TCS3xxx - Color Sensors (TCS3400, TCS3472, TCS34725)
 * - APDS-9xxx - Proximity/Gesture Sensors (APDS-9960, APDS-9930)
 * - AS3xxx - Driver ICs
 * - AS5xxx - Position Sensors (AS5600, AS5047, AS5048)
 * - AS6xxx - Spectral Sensors (AS6500)
 * - ENSxxx - Environmental Sensors (ENS160, ENS210, ENS220)
 *
 * Package Code Examples:
 * - BLGT = BGA Lead-free Green/Tape
 * - TSL = Special suffix for TSL parts
 * - FN = QFN package
 * - ASIL = Automotive Safety Integrity Level variant
 * - ASOM = Module variant
 */
public class AMSHandler implements ManufacturerHandler {

    // Pre-compiled patterns for better performance
    private static final Pattern AS72XX_PATTERN = Pattern.compile("^AS72[0-9]{2}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern AS73XX_PATTERN = Pattern.compile("^AS73[0-9]{2}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern TSL2XXX_PATTERN = Pattern.compile("^TSL2[0-9]{3}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern TMD2XXX_PATTERN = Pattern.compile("^TMD2[0-9]{3,4}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern TCS3XXX_PATTERN = Pattern.compile("^TCS3[0-9]{3,4}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern APDS9XXX_PATTERN = Pattern.compile("^APDS-?9[0-9]{3}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern AS3XXX_PATTERN = Pattern.compile("^AS3[0-9]{3}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern AS5XXX_PATTERN = Pattern.compile("^AS5[0-9]{3}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern AS6XXX_PATTERN = Pattern.compile("^AS6[0-9]{3}.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern ENSXXX_PATTERN = Pattern.compile("^ENS[12][0-9]{2}.*", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // AS72xx - Spectral Sensors
        registry.addPattern(ComponentType.SENSOR, "^AS72[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^AS72[0-9]{2}.*");

        // AS73xx - Color Sensors (AS7341, AS7343, etc.)
        registry.addPattern(ComponentType.SENSOR, "^AS73[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^AS73[0-9]{2}.*");

        // TSL2xxx - Light-to-Digital Sensors
        registry.addPattern(ComponentType.SENSOR, "^TSL2[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^TSL2[0-9]{3}.*");

        // TMD2xxx - Proximity/ALS Sensors
        registry.addPattern(ComponentType.SENSOR, "^TMD2[0-9]{3,4}.*");
        registry.addPattern(ComponentType.SENSOR_PROXIMITY, "^TMD2[0-9]{3,4}.*");
        registry.addPattern(ComponentType.IC, "^TMD2[0-9]{3,4}.*");

        // TCS3xxx - Color Sensors
        registry.addPattern(ComponentType.SENSOR, "^TCS3[0-9]{3,4}.*");
        registry.addPattern(ComponentType.IC, "^TCS3[0-9]{3,4}.*");

        // APDS-9xxx - Proximity/Gesture Sensors (with or without hyphen)
        registry.addPattern(ComponentType.SENSOR, "^APDS-?9[0-9]{3}.*");
        registry.addPattern(ComponentType.SENSOR_PROXIMITY, "^APDS-?9[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^APDS-?9[0-9]{3}.*");

        // AS3xxx - Driver ICs
        registry.addPattern(ComponentType.IC, "^AS3[0-9]{3}.*");
        registry.addPattern(ComponentType.LED_DRIVER, "^AS3[0-9]{3}.*");

        // AS5xxx - Position Sensors (magnetic rotary encoders)
        registry.addPattern(ComponentType.SENSOR, "^AS5[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^AS5[0-9]{3}.*");

        // AS6xxx - Spectral Sensors
        registry.addPattern(ComponentType.SENSOR, "^AS6[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^AS6[0-9]{3}.*");

        // ENSxxx - Environmental Sensors (ENS160, ENS210, ENS220)
        registry.addPattern(ComponentType.SENSOR, "^ENS[12][0-9]{2}.*");
        registry.addPattern(ComponentType.HUMIDITY_SENSOR, "^ENS[12][0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^ENS[12][0-9]{2}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.SENSOR,
            ComponentType.SENSOR_PROXIMITY,
            ComponentType.HUMIDITY_SENSOR,
            ComponentType.LED_DRIVER,
            ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // AS72xx - Spectral Sensors
        if (AS72XX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR || type == ComponentType.IC;
        }

        // AS73xx - Color Sensors
        if (AS73XX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR || type == ComponentType.IC;
        }

        // TSL2xxx - Light-to-Digital Sensors
        if (TSL2XXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR || type == ComponentType.IC;
        }

        // TMD2xxx - Proximity/ALS Sensors
        if (TMD2XXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR ||
                   type == ComponentType.SENSOR_PROXIMITY ||
                   type == ComponentType.IC;
        }

        // TCS3xxx - Color Sensors
        if (TCS3XXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR || type == ComponentType.IC;
        }

        // APDS-9xxx - Proximity/Gesture Sensors
        if (APDS9XXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR ||
                   type == ComponentType.SENSOR_PROXIMITY ||
                   type == ComponentType.IC;
        }

        // AS3xxx - Driver ICs
        if (AS3XXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.IC || type == ComponentType.LED_DRIVER;
        }

        // AS5xxx - Position Sensors
        if (AS5XXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR || type == ComponentType.IC;
        }

        // AS6xxx - Spectral Sensors
        if (AS6XXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR || type == ComponentType.IC;
        }

        // ENSxxx - Environmental Sensors (ENS160, ENS210, ENS220)
        if (ENSXXX_PATTERN.matcher(upperMpn).matches()) {
            return type == ComponentType.SENSOR ||
                   type == ComponentType.HUMIDITY_SENSOR ||
                   type == ComponentType.IC;
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle hyphenated suffixes (e.g., AS7262-BLGT, APDS-9960-ASIL)
        int lastHyphen = upperMpn.lastIndexOf('-');
        if (lastHyphen >= 0 && lastHyphen < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(lastHyphen + 1);

            // Skip numeric-only suffixes (model numbers like in APDS-9960)
            if (!suffix.matches("^[0-9]+$")) {
                // Map known package codes
                return switch (suffix) {
                    case "BLGT" -> "BGA";           // BGA Lead-free Green/Tape
                    case "BGA" -> "BGA";
                    case "FN", "QFN" -> "QFN";      // QFN package
                    case "LGA" -> "LGA";
                    case "TSL" -> "DFN";            // TSL special suffix (typically DFN)
                    case "ASIL" -> extractBasePackage(upperMpn); // Automotive variant
                    case "ASOM" -> "MODULE";        // Module variant
                    case "TR" -> "TAPE_REEL";       // Tape and Reel
                    default -> {
                        // Check for embedded package indicators
                        if (suffix.endsWith("FN")) yield "QFN";
                        if (suffix.endsWith("LGA")) yield "LGA";
                        if (suffix.endsWith("BGA")) yield "BGA";
                        yield suffix;
                    }
                };
            }
        }

        // Check for suffix directly on main part (e.g., TMD26721FN, TCS34725FN)
        if (upperMpn.endsWith("FN")) return "QFN";
        if (upperMpn.endsWith("LGA")) return "LGA";

        return "";
    }

    /**
     * Extract base package code from automotive/variant parts.
     */
    private String extractBasePackage(String mpn) {
        // For ASIL variants, the package is typically in the base part
        // e.g., APDS-9960-ASIL would inherit the base APDS-9960 package
        return "QFN";  // Default for most APDS parts
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle APDS series (with hyphen in family name)
        if (upperMpn.startsWith("APDS")) {
            // Extract APDS-9xxx pattern
            if (upperMpn.matches("^APDS-?9[0-9]{3}.*")) {
                return "APDS";
            }
        }

        // Handle TSL series
        if (upperMpn.startsWith("TSL")) {
            return "TSL";
        }

        // Handle TMD series
        if (upperMpn.startsWith("TMD")) {
            return "TMD";
        }

        // Handle TCS series
        if (upperMpn.startsWith("TCS")) {
            return "TCS";
        }

        // Handle ENS series
        if (upperMpn.startsWith("ENS")) {
            return "ENS";
        }

        // Handle AS series (AS72, AS71, AS3, AS5, AS6)
        if (upperMpn.startsWith("AS")) {
            // Extract ASnn pattern (first 4 characters)
            if (upperMpn.length() >= 4) {
                String prefix = upperMpn.substring(0, 4);
                if (prefix.matches("AS[0-9]{2}")) {
                    return prefix.substring(0, 4);  // Return AS72, AS71, AS30, AS50, AS60
                }
            }
            // Fallback for shorter patterns
            if (upperMpn.length() >= 3) {
                return upperMpn.substring(0, 3);
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series family
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Extract base model numbers for comparison
        String base1 = extractBaseModel(mpn1);
        String base2 = extractBaseModel(mpn2);

        // Same base model with different suffixes are compatible
        if (base1.equals(base2)) {
            // Check interface compatibility
            String interface1 = extractInterface(mpn1);
            String interface2 = extractInterface(mpn2);

            if (!interface1.isEmpty() && !interface2.isEmpty() &&
                !interface1.equals(interface2)) {
                return false;  // Different interfaces are not compatible
            }

            return true;
        }

        // Check for known compatible models within the same series
        return isCompatibleModel(base1, base2);
    }

    /**
     * Extract the base model number (e.g., AS7262 from AS7262-BLGT).
     */
    private String extractBaseModel(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle hyphenated MPNs
        int hyphen = upperMpn.indexOf('-');
        if (hyphen > 0) {
            // For APDS, include the hyphen and model number
            if (upperMpn.startsWith("APDS")) {
                // Find the second hyphen or end
                int secondHyphen = upperMpn.indexOf('-', 5);
                if (secondHyphen > 0) {
                    return upperMpn.substring(0, secondHyphen);
                }
            }
            return upperMpn.substring(0, hyphen);
        }

        // Extract base model by removing trailing letters
        StringBuilder base = new StringBuilder();
        boolean foundDigit = false;
        for (char c : upperMpn.toCharArray()) {
            if (Character.isLetter(c)) {
                if (!foundDigit) {
                    base.append(c);
                } else {
                    // Stop at first letter after digits (usually package code)
                    break;
                }
            } else if (Character.isDigit(c)) {
                base.append(c);
                foundDigit = true;
            }
        }

        return base.toString();
    }

    /**
     * Extract interface type from MPN.
     */
    private String extractInterface(String mpn) {
        String upperMpn = mpn.toUpperCase();

        if (upperMpn.contains("-I2C") || upperMpn.endsWith("I")) return "I2C";
        if (upperMpn.contains("-SPI") || upperMpn.endsWith("S")) return "SPI";
        if (upperMpn.contains("-ANA") || upperMpn.endsWith("A")) return "ANALOG";

        return "";
    }

    /**
     * Check if two base models are compatible replacements.
     */
    private boolean isCompatibleModel(String base1, String base2) {
        // AS72xx spectral sensors - different channels but same interface
        if (isAS72xxCompatible(base1, base2)) return true;

        // TSL light sensors
        if (isTSLCompatible(base1, base2)) return true;

        // TCS color sensors
        if (isTCSCompatible(base1, base2)) return true;

        // APDS proximity/gesture sensors
        if (isAPDSCompatible(base1, base2)) return true;

        // AS5xxx position sensors
        if (isAS5xxxCompatible(base1, base2)) return true;

        // ENS environmental sensors
        if (isENSCompatible(base1, base2)) return true;

        return false;
    }

    private boolean isAS72xxCompatible(String base1, String base2) {
        // AS7261, AS7262, AS7263 are different spectral channel versions
        // They have same interface but different wavelength responses
        // AS7265x is the multi-chip set
        return (base1.matches("AS726[123]") && base2.matches("AS726[123]")) ||
               (base1.matches("AS7265[A-Z]?") && base2.matches("AS7265[A-Z]?"));
    }

    private boolean isTSLCompatible(String base1, String base2) {
        // TSL2561 and TSL2591 are both ambient light sensors with different specs
        // TSL2591 is newer with better specs
        return (base1.startsWith("TSL25") && base2.startsWith("TSL25"));
    }

    private boolean isTCSCompatible(String base1, String base2) {
        // TCS3472 and TCS34725 are different versions of same sensor
        return (base1.startsWith("TCS347") && base2.startsWith("TCS347")) ||
               (base1.startsWith("TCS34") && base2.startsWith("TCS34"));
    }

    private boolean isAPDSCompatible(String base1, String base2) {
        // APDS-9930 (proximity/ALS) and APDS-9960 (gesture/proximity/ALS)
        // are in the same family but have different features
        return (base1.startsWith("APDS-99") && base2.startsWith("APDS-99")) ||
               (base1.startsWith("APDS99") && base2.startsWith("APDS99"));
    }

    private boolean isAS5xxxCompatible(String base1, String base2) {
        // AS5600, AS5047, AS5048 are magnetic position sensors with different resolutions
        // Same interface but different resolution/features
        return (base1.matches("AS50[0-9]{2}") && base2.matches("AS50[0-9]{2}")) ||
               (base1.matches("AS56[0-9]{2}") && base2.matches("AS56[0-9]{2}"));
    }

    private boolean isENSCompatible(String base1, String base2) {
        // ENS160 (air quality) and ENS210 (humidity/temp) are different sensor types
        // Not directly compatible
        return base1.equals(base2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
