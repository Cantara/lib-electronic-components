package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Rubycon Corporation components (primarily aluminum electrolytic capacitors).
 *
 * Rubycon is a Japanese manufacturer specializing in aluminum electrolytic capacitors
 * with various series for different applications:
 *
 * - ZLH series: Low impedance, high ripple current
 * - YXF/YXG series: Miniature aluminum electrolytic
 * - MCZ series: Conductive polymer hybrid aluminum electrolytic
 * - PK/PL series: Small size aluminum electrolytic
 *
 * MPN Format Examples:
 * - ZLH35VB221M08X12: ZLH series, 35V, 220uF (221M), 8x12mm
 * - YXF25VB100M05X11: YXF series, 25V, 10uF (100M), 5x11mm
 * - MCZ1V471MNN08F12: MCZ series, 35V, 470uF, 8x12mm
 * - 16PK1000MEFC10X20: 16V PK series, 1000uF, 10x20mm
 *
 * Value Encoding (EIA standard):
 * - 221M = 220uF (22 * 10^1 = 220, M = +/-20%)
 * - 100M = 10uF (10 * 10^0 = 10, M = +/-20%)
 * - 471M = 470uF (47 * 10^1 = 470, M = +/-20%)
 *
 * Voltage Encoding:
 * - 35VB = 35V, 25VB = 25V (voltage + VB suffix)
 * - 1V = 35V (Rubycon voltage code)
 * - Prefix number (16PK) = 16V
 */
public class RubyconHandler implements ManufacturerHandler {

    // Patterns for different Rubycon series
    private static final Pattern ZLH_PATTERN = Pattern.compile("^ZLH([0-9]+)V[A-Z]?([0-9]{3})[A-Z].*", Pattern.CASE_INSENSITIVE);
    private static final Pattern YXF_PATTERN = Pattern.compile("^YXF([0-9]+)V[A-Z]?([0-9]{3})[A-Z].*", Pattern.CASE_INSENSITIVE);
    private static final Pattern YXG_PATTERN = Pattern.compile("^YXG([0-9]+)V[A-Z]?([0-9]{3})[A-Z].*", Pattern.CASE_INSENSITIVE);
    private static final Pattern MCZ_PATTERN = Pattern.compile("^MCZ([0-9][A-Z])([0-9]{3})[A-Z].*", Pattern.CASE_INSENSITIVE);
    private static final Pattern PK_PATTERN = Pattern.compile("^([0-9]+)PK([0-9]+)[A-Z].*", Pattern.CASE_INSENSITIVE);
    private static final Pattern PL_PATTERN = Pattern.compile("^([0-9]+)PL([0-9]+)[A-Z].*", Pattern.CASE_INSENSITIVE);

    // Pattern for extracting dimensions (e.g., 08X12 = 8mm x 12mm)
    private static final Pattern DIMENSION_PATTERN = Pattern.compile("([0-9]{2})X([0-9]{2})$", Pattern.CASE_INSENSITIVE);
    private static final Pattern DIMENSION_PATTERN_F = Pattern.compile("([0-9]{2})F([0-9]{2})$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ZLH series - Low impedance, high ripple current
        registry.addPattern(ComponentType.CAPACITOR, "^ZLH[0-9]+.*");

        // YXF series - Miniature aluminum electrolytic
        registry.addPattern(ComponentType.CAPACITOR, "^YXF[0-9]+.*");

        // YXG series - Miniature aluminum electrolytic (wider temp range)
        registry.addPattern(ComponentType.CAPACITOR, "^YXG[0-9]+.*");

        // MCZ series - Conductive polymer hybrid
        registry.addPattern(ComponentType.CAPACITOR, "^MCZ[0-9][A-Z].*");

        // PK series - Small size (voltage prefix format)
        registry.addPattern(ComponentType.CAPACITOR, "^[0-9]+PK[0-9]+.*");

        // PL series - Small size long life (voltage prefix format)
        registry.addPattern(ComponentType.CAPACITOR, "^[0-9]+PL[0-9]+.*");

        // ZL series - Standard low impedance
        registry.addPattern(ComponentType.CAPACITOR, "^ZL[0-9]+.*");

        // YXA/YXH/YXJ series - Various miniature types
        registry.addPattern(ComponentType.CAPACITOR, "^YX[AHJL][0-9]+.*");

        // MBZ series - Conductive polymer solid
        registry.addPattern(ComponentType.CAPACITOR, "^MBZ[0-9][A-Z].*");

        // USR/UST/USP series - Ultra-small SMD
        registry.addPattern(ComponentType.CAPACITOR, "^US[RTP][0-9]+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.CAPACITOR);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || mpn.isEmpty()) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        if (type == ComponentType.CAPACITOR) {
            // ZLH series
            if (upperMpn.matches("^ZLH[0-9]+.*")) return true;
            // YXF/YXG series
            if (upperMpn.matches("^YX[FG][0-9]+.*")) return true;
            // MCZ series
            if (upperMpn.matches("^MCZ[0-9][A-Z].*")) return true;
            // PK/PL series (voltage prefix)
            if (upperMpn.matches("^[0-9]+P[KL][0-9]+.*")) return true;
            // ZL series
            if (upperMpn.matches("^ZL[0-9]+.*")) return true;
            // YXA/YXH/YXJ/YXL series
            if (upperMpn.matches("^YX[AHJL][0-9]+.*")) return true;
            // MBZ series
            if (upperMpn.matches("^MBZ[0-9][A-Z].*")) return true;
            // USR/UST/USP series
            if (upperMpn.matches("^US[RTP][0-9]+.*")) return true;
        }

        // Only match CAPACITOR type - Rubycon only makes capacitors
        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Try to extract dimensions from end of MPN (e.g., 08X12)
        Matcher dimMatcher = DIMENSION_PATTERN.matcher(upperMpn);
        if (dimMatcher.find()) {
            int diameter = Integer.parseInt(dimMatcher.group(1));
            int height = Integer.parseInt(dimMatcher.group(2));
            return diameter + "x" + height + "mm";
        }

        // Try alternate format with F separator (e.g., 08F12 for some polymer types)
        Matcher dimFMatcher = DIMENSION_PATTERN_F.matcher(upperMpn);
        if (dimFMatcher.find()) {
            int diameter = Integer.parseInt(dimFMatcher.group(1));
            int height = Integer.parseInt(dimFMatcher.group(2));
            return diameter + "x" + height + "mm";
        }

        // For PK/PL series, try to extract from different position
        if (upperMpn.matches("^[0-9]+P[KL].*")) {
            // Look for pattern like 10X20 anywhere in the string
            Pattern pkDimPattern = Pattern.compile("([0-9]{1,2})X([0-9]{1,2})", Pattern.CASE_INSENSITIVE);
            Matcher pkMatcher = pkDimPattern.matcher(upperMpn);
            if (pkMatcher.find()) {
                return pkMatcher.group(1) + "x" + pkMatcher.group(2) + "mm";
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // ZLH series - Low impedance
        if (upperMpn.startsWith("ZLH")) return "ZLH (Low Impedance)";

        // ZL series - Standard low impedance
        if (upperMpn.startsWith("ZL") && !upperMpn.startsWith("ZLH")) return "ZL (Standard)";

        // YXF series - Miniature
        if (upperMpn.startsWith("YXF")) return "YXF (Miniature)";

        // YXG series - Miniature wide temp
        if (upperMpn.startsWith("YXG")) return "YXG (Miniature Wide Temp)";

        // YXA series - High temperature
        if (upperMpn.startsWith("YXA")) return "YXA (High Temperature)";

        // YXH series - Low ESR miniature
        if (upperMpn.startsWith("YXH")) return "YXH (Low ESR Miniature)";

        // YXJ series - Standard miniature
        if (upperMpn.startsWith("YXJ")) return "YXJ (Standard Miniature)";

        // YXL series - Long life miniature
        if (upperMpn.startsWith("YXL")) return "YXL (Long Life Miniature)";

        // MCZ series - Conductive polymer hybrid
        if (upperMpn.startsWith("MCZ")) return "MCZ (Polymer Hybrid)";

        // MBZ series - Conductive polymer solid
        if (upperMpn.startsWith("MBZ")) return "MBZ (Polymer Solid)";

        // PK series - Small size
        if (upperMpn.matches("^[0-9]+PK.*")) return "PK (Small Size)";

        // PL series - Small size long life
        if (upperMpn.matches("^[0-9]+PL.*")) return "PL (Small Size Long Life)";

        // USR series - Ultra-small SMD
        if (upperMpn.startsWith("USR")) return "USR (Ultra-Small SMD)";

        // UST series - Ultra-small SMD high temp
        if (upperMpn.startsWith("UST")) return "UST (Ultra-Small SMD High Temp)";

        // USP series - Ultra-small SMD polymer
        if (upperMpn.startsWith("USP")) return "USP (Ultra-Small SMD Polymer)";

        return "";
    }

    /**
     * Extracts the voltage rating from a Rubycon capacitor MPN.
     *
     * @param mpn the manufacturer part number
     * @return the voltage rating as a string (e.g., "35V"), or empty string if not found
     */
    public String extractVoltage(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // ZLH/ZL/YXx series: voltage follows series code (e.g., ZLH35VB, YXF25VB)
        if (upperMpn.matches("^(ZLH|ZL|YX[FGAHJL])[0-9]+.*")) {
            Pattern voltagePattern = Pattern.compile("^(?:ZLH|ZL|YX[FGAHJL])([0-9]+)V", Pattern.CASE_INSENSITIVE);
            Matcher matcher = voltagePattern.matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1) + "V";
            }
        }

        // MCZ series: uses voltage code (e.g., MCZ1V = 35V, MCZ0J = 6.3V)
        if (upperMpn.startsWith("MCZ")) {
            Pattern voltagePattern = Pattern.compile("^MCZ([0-9])([A-Z])", Pattern.CASE_INSENSITIVE);
            Matcher matcher = voltagePattern.matcher(upperMpn);
            if (matcher.find()) {
                String code = matcher.group(1) + matcher.group(2);
                return decodeVoltageCode(code);
            }
        }

        // PK/PL series: voltage is the prefix number (e.g., 16PK = 16V)
        if (upperMpn.matches("^[0-9]+P[KL].*")) {
            Pattern voltagePattern = Pattern.compile("^([0-9]+)P[KL]", Pattern.CASE_INSENSITIVE);
            Matcher matcher = voltagePattern.matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1) + "V";
            }
        }

        // MBZ series: uses voltage code
        if (upperMpn.startsWith("MBZ")) {
            Pattern voltagePattern = Pattern.compile("^MBZ([0-9])([A-Z])", Pattern.CASE_INSENSITIVE);
            Matcher matcher = voltagePattern.matcher(upperMpn);
            if (matcher.find()) {
                String code = matcher.group(1) + matcher.group(2);
                return decodeVoltageCode(code);
            }
        }

        return "";
    }

    /**
     * Decodes Rubycon voltage codes to actual voltage values.
     * Uses standard EIA voltage coding.
     *
     * @param code the voltage code (e.g., "1V", "0J")
     * @return the decoded voltage (e.g., "35V", "6.3V")
     */
    private String decodeVoltageCode(String code) {
        if (code == null || code.length() < 2) return "";

        return switch (code.toUpperCase()) {
            case "0E" -> "2.5V";
            case "0G" -> "4V";
            case "0J" -> "6.3V";
            case "1A" -> "10V";
            case "1C" -> "16V";
            case "1E" -> "25V";
            case "1V" -> "35V";
            case "1H" -> "50V";
            case "1J" -> "63V";
            case "2A" -> "100V";
            case "2C" -> "160V";
            case "2D" -> "200V";
            case "2E" -> "250V";
            case "2G" -> "400V";
            case "2W" -> "450V";
            default -> code;
        };
    }

    /**
     * Extracts the capacitance value from a Rubycon capacitor MPN.
     * Rubycon uses standard EIA 3-digit code with tolerance suffix.
     *
     * @param mpn the manufacturer part number
     * @return the capacitance value (e.g., "220uF"), or empty string if not found
     */
    public String extractCapacitance(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Look for 3-digit code followed by tolerance (M = +/-20%, K = +/-10%)
        Pattern capPattern = Pattern.compile("([0-9]{3})[MKJ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = capPattern.matcher(upperMpn);
        if (matcher.find()) {
            String code = matcher.group(1);
            return decodeCapacitanceCode(code);
        }

        // PK/PL series may have direct value (e.g., 1000MEFC = 1000uF)
        if (upperMpn.matches("^[0-9]+P[KL].*")) {
            Pattern directCapPattern = Pattern.compile("P[KL]([0-9]+)[MKJ]", Pattern.CASE_INSENSITIVE);
            Matcher directMatcher = directCapPattern.matcher(upperMpn);
            if (directMatcher.find()) {
                String value = directMatcher.group(1);
                // If it's more than 3 digits, it's likely a direct uF value
                if (value.length() > 3) {
                    return value + "uF";
                }
                return decodeCapacitanceCode(value);
            }
        }

        return "";
    }

    /**
     * Decodes EIA 3-digit capacitance code to actual value.
     *
     * @param code the 3-digit code (e.g., "221", "100", "471")
     * @return the decoded capacitance (e.g., "220uF", "10uF", "470uF")
     */
    private String decodeCapacitanceCode(String code) {
        if (code == null || code.length() < 2) return "";

        try {
            // Handle 2-digit codes (like "10" from PK series)
            if (code.length() == 2) {
                int value = Integer.parseInt(code);
                return value + "uF";
            }

            // Standard 3-digit EIA code
            int significantDigits = Integer.parseInt(code.substring(0, 2));
            int multiplier = Integer.parseInt(code.substring(2, 3));

            double value = significantDigits * Math.pow(10, multiplier);

            // Convert to appropriate unit
            if (value >= 1000000) {
                return String.format("%.0fmF", value / 1000000);
            } else if (value >= 1000) {
                return String.format("%.0fuF", value);
            } else if (value >= 1) {
                return String.format("%.0fuF", value);
            } else if (value >= 0.001) {
                return String.format("%.0fnF", value * 1000);
            } else {
                return String.format("%.0fpF", value * 1000000);
            }
        } catch (NumberFormatException e) {
            return "";
        }
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are generally not replacements
        if (!series1.equals(series2)) {
            // However, some series can replace others with same specs
            // YXG (wide temp) can replace YXF (standard temp)
            if (series1.contains("YXG") && series2.contains("YXF")) {
                return haveSameSpecifications(mpn1, mpn2);
            }
            // PL (long life) can replace PK (standard)
            if (series1.contains("PL") && series2.contains("PK")) {
                return haveSameSpecifications(mpn1, mpn2);
            }
            // ZLH can replace ZL with same specs
            if (series1.contains("ZLH") && series2.contains("ZL (Standard)")) {
                return haveSameSpecifications(mpn1, mpn2);
            }
            return false;
        }

        // Same series - check specifications match
        return haveSameSpecifications(mpn1, mpn2);
    }

    /**
     * Checks if two capacitors have the same electrical specifications.
     *
     * @param mpn1 first MPN
     * @param mpn2 second MPN
     * @return true if voltage, capacitance, and package match
     */
    private boolean haveSameSpecifications(String mpn1, String mpn2) {
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);
        if (!voltage1.equals(voltage2)) return false;

        String cap1 = extractCapacitance(mpn1);
        String cap2 = extractCapacitance(mpn2);
        if (!cap1.equals(cap2)) return false;

        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        return pkg1.equals(pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
