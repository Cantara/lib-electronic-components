package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;

/**
 * Manufacturer handler for Honeywell Sensing products.
 *
 * Product families supported:
 * - HIHxxxx - Humidity Sensors (HIH4000, HIH5030, HIH6130, HIH8120)
 * - HSCxxxx - TruStability Pressure Sensors (HSCDANN001PG2A3, HSCDRRN060MD)
 * - SSCxxxx - Standard Pressure Sensors (SSCMANV060PGAA5)
 * - ABPxxxx - Basic Pressure Sensors (ABPMANN060PG2A3)
 * - MPRxxxx - MicroPressure Sensors (MPRLS0025PA00001A)
 * - SS49x/SS59x - Hall Effect Linear Sensors (SS49E, SS495A, SS59ET)
 * - SS4xx/SS5xx - Hall Effect Switch Sensors (SS441A, SS451A, SS461A)
 * - HMCxxxx - Magnetometers (HMC5883L, HMC5883)
 * - HRS/HPX - Rotary Sensors
 * - HOA/HLC - Optical Sensors (HOA1180, HLC2705)
 */
public class HoneywellHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Humidity Sensors - HIH series
        registry.addPattern(ComponentType.HUMIDITY_SENSOR, "^HIH[0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^HIH[0-9].*");

        // TruStability Pressure Sensors - HSC series
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^HSC[A-Z].*");
        registry.addPattern(ComponentType.SENSOR, "^HSC[A-Z].*");

        // Standard Pressure Sensors - SSC series
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^SSC[A-Z].*");
        registry.addPattern(ComponentType.SENSOR, "^SSC[A-Z].*");

        // Basic Pressure Sensors - ABP series
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^ABP[A-Z].*");
        registry.addPattern(ComponentType.SENSOR, "^ABP[A-Z].*");

        // MicroPressure Sensors - MPR series
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^MPR[A-Z].*");
        registry.addPattern(ComponentType.SENSOR, "^MPR[A-Z].*");

        // Hall Effect Linear Sensors - SS49x and SS59x (specific patterns first)
        registry.addPattern(ComponentType.HALL_SENSOR, "^SS49[0-9].*");
        registry.addPattern(ComponentType.HALL_SENSOR, "^SS59[0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^SS49[0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^SS59[0-9].*");

        // Hall Effect Switch Sensors - SS4xx (not SS49x) and SS5xx (not SS59x)
        // These match SS4 followed by digit that's not 9
        registry.addPattern(ComponentType.HALL_SENSOR, "^SS4[0-8][0-9].*");
        registry.addPattern(ComponentType.HALL_SENSOR, "^SS5[0-8][0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^SS4[0-8][0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^SS5[0-8][0-9].*");

        // Magnetometers - HMC series
        registry.addPattern(ComponentType.MAGNETOMETER, "^HMC[0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^HMC[0-9].*");

        // Rotary Sensors - HRS and HPX series
        registry.addPattern(ComponentType.SENSOR, "^HRS[0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^HPX[0-9].*");

        // Optical Sensors - HOA and HLC series
        registry.addPattern(ComponentType.SENSOR_OPTICAL, "^HOA[0-9].*");
        registry.addPattern(ComponentType.SENSOR_OPTICAL, "^HLC[0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^HOA[0-9].*");
        registry.addPattern(ComponentType.SENSOR, "^HLC[0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.SENSOR,
            ComponentType.HUMIDITY_SENSOR,
            ComponentType.PRESSURE_SENSOR,
            ComponentType.HALL_SENSOR,
            ComponentType.MAGNETOMETER,
            ComponentType.SENSOR_OPTICAL
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle hyphenated MPNs (e.g., HIH6130-021-001)
        String[] parts = upperMpn.split("-");

        // For pressure sensors with long suffix codes
        if (upperMpn.startsWith("HSC") || upperMpn.startsWith("SSC") ||
            upperMpn.startsWith("ABP") || upperMpn.startsWith("MPR")) {
            // Package code is typically the last 2-3 characters
            // e.g., HSCDANN001PG2A3 -> A3 indicates package type
            if (parts.length > 0) {
                String mainPart = parts[0];
                if (mainPart.length() >= 2) {
                    String suffix = mainPart.substring(mainPart.length() - 2);
                    return switch (suffix) {
                        case "A3" -> "DIP";           // DIP package
                        case "A5" -> "SMT";           // SMT package
                        case "AA" -> "DIP";           // DIP variant
                        case "SA" -> "SIP";           // SIP package
                        case "MD" -> "SMD";           // SMD package
                        default -> suffix;
                    };
                }
            }
            return "";
        }

        // For humidity sensors with segment codes (e.g., HIH6130-021-001)
        if (upperMpn.startsWith("HIH")) {
            if (parts.length >= 2) {
                String segment = parts[1];
                return switch (segment) {
                    case "021" -> "SIP";    // SIP non-condensing
                    case "022" -> "SMD";    // SMD non-condensing
                    case "031" -> "SIP";    // SIP condensing
                    case "032" -> "SMD";    // SMD condensing
                    default -> segment;
                };
            }
            return "";
        }

        // For Hall effect sensors (e.g., SS495A1, SS441A)
        if (upperMpn.startsWith("SS4") || upperMpn.startsWith("SS5")) {
            // Package code is typically the letter near the end
            String basePart = parts[0];
            if (basePart.length() >= 5) {
                char pkgChar = basePart.charAt(basePart.length() - 1);
                if (Character.isDigit(pkgChar) && basePart.length() >= 6) {
                    pkgChar = basePart.charAt(basePart.length() - 2);
                }
                return switch (pkgChar) {
                    case 'A' -> "TO-92";    // TO-92 package
                    case 'E' -> "SOT-89";   // SOT-89 package
                    case 'T' -> "SOT-23";   // SOT-23 package
                    case 'L' -> "SIP";      // SIP package
                    default -> String.valueOf(pkgChar);
                };
            }
        }

        // For magnetometers (e.g., HMC5883L-TR)
        if (upperMpn.startsWith("HMC")) {
            String basePart = parts[0];
            if (basePart.endsWith("L")) {
                return "LCC";  // Leadless chip carrier
            }
            return "";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Humidity sensors - extract HIH + 4 digits
        if (upperMpn.startsWith("HIH")) {
            return extractNumericSeries(upperMpn, "HIH", 4);
        }

        // Pressure sensors - extract family prefix
        if (upperMpn.startsWith("HSC")) {
            return "HSC";
        }
        if (upperMpn.startsWith("SSC")) {
            return "SSC";
        }
        if (upperMpn.startsWith("ABP")) {
            return "ABP";
        }
        if (upperMpn.startsWith("MPR")) {
            return "MPR";
        }

        // Hall effect linear sensors - SS49x or SS59x
        if (upperMpn.startsWith("SS49")) {
            return "SS49";
        }
        if (upperMpn.startsWith("SS59")) {
            return "SS59";
        }

        // Hall effect switch sensors - SS4xx or SS5xx
        if (upperMpn.startsWith("SS4")) {
            return "SS4";
        }
        if (upperMpn.startsWith("SS5")) {
            return "SS5";
        }

        // Magnetometers - HMC + 4 digits
        if (upperMpn.startsWith("HMC")) {
            return extractNumericSeries(upperMpn, "HMC", 4);
        }

        // Rotary sensors
        if (upperMpn.startsWith("HRS")) {
            return "HRS";
        }
        if (upperMpn.startsWith("HPX")) {
            return "HPX";
        }

        // Optical sensors
        if (upperMpn.startsWith("HOA")) {
            return extractNumericSeries(upperMpn, "HOA", 4);
        }
        if (upperMpn.startsWith("HLC")) {
            return extractNumericSeries(upperMpn, "HLC", 4);
        }

        return "";
    }

    /**
     * Extract series code with prefix and up to maxDigits digits.
     */
    private String extractNumericSeries(String mpn, String prefix, int maxDigits) {
        StringBuilder series = new StringBuilder(prefix);
        int digitCount = 0;

        for (int i = prefix.length(); i < mpn.length() && digitCount < maxDigits; i++) {
            char c = mpn.charAt(i);
            if (Character.isDigit(c)) {
                series.append(c);
                digitCount++;
            } else {
                break;
            }
        }

        return series.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1.toUpperCase());
        String series2 = extractSeries(mpn2.toUpperCase());

        if (series1.isEmpty() || series2.isEmpty()) return false;

        // Same series parts are typically compatible (different packages)
        if (series1.equals(series2)) {
            // For pressure sensors, check if they have the same range
            if (isPressureSensor(mpn1) && isPressureSensor(mpn2)) {
                String range1 = extractPressureRange(mpn1);
                String range2 = extractPressureRange(mpn2);
                // If both have identifiable ranges, they must match
                if (!range1.isEmpty() && !range2.isEmpty()) {
                    return range1.equals(range2);
                }
            }
            return true;
        }

        // Check for known compatible series within same product family
        return isCompatibleSeries(series1, series2);
    }

    private boolean isPressureSensor(String mpn) {
        String upper = mpn.toUpperCase();
        return upper.startsWith("HSC") || upper.startsWith("SSC") ||
               upper.startsWith("ABP") || upper.startsWith("MPR");
    }

    /**
     * Extract pressure range from pressure sensor MPN.
     * E.g., HSCDRRN060MD -> "060" (60 mbar differential)
     */
    private String extractPressureRange(String mpn) {
        String upper = mpn.toUpperCase();

        // For HSC/SSC/ABP sensors, range is typically 3 digits after the configuration letters
        // Pattern: HSC + D/M/S (differential/mems/standard) + output type + address + range
        if (upper.startsWith("HSC") || upper.startsWith("SSC") || upper.startsWith("ABP")) {
            // Look for 3-digit range code
            for (int i = 3; i <= upper.length() - 3; i++) {
                String chunk = upper.substring(i, i + 3);
                if (chunk.matches("[0-9]{3}")) {
                    return chunk;
                }
            }
        }

        // For MPR sensors, format is different
        if (upper.startsWith("MPR")) {
            // MPRLS0025PA00001A -> 0025 is the range (25 PSI)
            for (int i = 4; i <= upper.length() - 4; i++) {
                String chunk = upper.substring(i, i + 4);
                if (chunk.matches("[0-9]{4}")) {
                    return chunk;
                }
            }
        }

        return "";
    }

    /**
     * Check for known compatible series pairs.
     */
    private boolean isCompatibleSeries(String series1, String series2) {
        // Humidity sensor compatibility
        // HIH6130 and HIH6131 are pin-compatible
        if (isHIHCompatible(series1, series2)) return true;

        // Hall effect sensor compatibility
        // SS49E and SS495A are the same sensor with different accuracy
        if (isHallEffectCompatible(series1, series2)) return true;

        // Magnetometer compatibility
        // HMC5883 and HMC5883L are same part with different package
        if (isMagnetometerCompatible(series1, series2)) return true;

        return false;
    }

    private boolean isHIHCompatible(String series1, String series2) {
        // HIH6130/6131, HIH8120/8121 pairs are compatible
        return (series1.equals("HIH6130") && series2.equals("HIH6131")) ||
               (series1.equals("HIH6131") && series2.equals("HIH6130")) ||
               (series1.equals("HIH8120") && series2.equals("HIH8121")) ||
               (series1.equals("HIH8121") && series2.equals("HIH8120")) ||
               // HIH4000/4010 series
               (series1.equals("HIH4000") && series2.equals("HIH4010")) ||
               (series1.equals("HIH4010") && series2.equals("HIH4000"));
    }

    private boolean isHallEffectCompatible(String series1, String series2) {
        // SS49 linear sensors are compatible within the family
        if (series1.equals("SS49") && series2.equals("SS49")) return true;
        // SS4 switch sensors are compatible within the family
        if (series1.equals("SS4") && series2.equals("SS4")) return true;
        return false;
    }

    private boolean isMagnetometerCompatible(String series1, String series2) {
        // HMC5883 variants are compatible
        return (series1.equals("HMC5883") && series2.equals("HMC5883"));
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Explicit checks for better performance and accuracy
        switch (type) {
            case HUMIDITY_SENSOR:
                return upperMpn.matches("^HIH[0-9].*");

            case PRESSURE_SENSOR:
                return upperMpn.matches("^HSC[A-Z].*") ||
                       upperMpn.matches("^SSC[A-Z].*") ||
                       upperMpn.matches("^ABP[A-Z].*") ||
                       upperMpn.matches("^MPR[A-Z].*");

            case HALL_SENSOR:
                return upperMpn.matches("^SS4[0-9].*") ||
                       upperMpn.matches("^SS5[0-9].*");

            case MAGNETOMETER:
                return upperMpn.matches("^HMC[0-9].*");

            case SENSOR_OPTICAL:
                return upperMpn.matches("^HOA[0-9].*") ||
                       upperMpn.matches("^HLC[0-9].*");

            case SENSOR:
                return upperMpn.matches("^HIH[0-9].*") ||
                       upperMpn.matches("^HSC[A-Z].*") ||
                       upperMpn.matches("^SSC[A-Z].*") ||
                       upperMpn.matches("^ABP[A-Z].*") ||
                       upperMpn.matches("^MPR[A-Z].*") ||
                       upperMpn.matches("^SS4[0-9].*") ||
                       upperMpn.matches("^SS5[0-9].*") ||
                       upperMpn.matches("^HMC[0-9].*") ||
                       upperMpn.matches("^HRS[0-9].*") ||
                       upperMpn.matches("^HPX[0-9].*") ||
                       upperMpn.matches("^HOA[0-9].*") ||
                       upperMpn.matches("^HLC[0-9].*");

            default:
                return patterns.matchesForCurrentHandler(upperMpn, type);
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
