package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler for Lelon Electronics components (primarily electrolytic capacitors).
 *
 * Lelon is a Taiwanese manufacturer specializing in aluminum electrolytic capacitors.
 *
 * Series supported:
 * - VE series: Low impedance (VE-101M1CTR-0605)
 * - VR series: Standard (VR-100M1ETR-0811)
 * - VZ series: High temperature (VZ-470M1HTR-0810)
 * - REA/REB/REC series: Radial lead (REA221M1CTR-0810)
 * - OVZ/OVR series: Conductive polymer (OVZ-100M1ETR-0605)
 *
 * MPN Structure:
 * - Series prefix (VE, VR, VZ, REA, REB, REC, OVZ, OVR)
 * - Hyphen
 * - Capacitance code (101=100uF, 470=47uF - uses 3-digit code where first two are significant)
 * - Tolerance (M=20%, K=10%, J=5%)
 * - Voltage code (1C=16V, 1E=25V, 1H=50V, etc.)
 * - Suffix (TR=tape and reel)
 * - Package code (0605=6.3x5mm, 0810=8x10mm, 0811=8x11mm)
 */
public class LelonHandler implements ManufacturerHandler {

    // Pre-compiled patterns for performance
    private static final Pattern VE_PATTERN = Pattern.compile("^VE-[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern VR_PATTERN = Pattern.compile("^VR-[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern VZ_PATTERN = Pattern.compile("^VZ-[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern REA_PATTERN = Pattern.compile("^REA[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern REB_PATTERN = Pattern.compile("^REB[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern REC_PATTERN = Pattern.compile("^REC[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern OVZ_PATTERN = Pattern.compile("^OVZ-[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern OVR_PATTERN = Pattern.compile("^OVR-[0-9]+.*", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // VE series - Low impedance electrolytic capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^VE-[0-9]+.*");

        // VR series - Standard electrolytic capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^VR-[0-9]+.*");

        // VZ series - High temperature electrolytic capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^VZ-[0-9]+.*");

        // REA/REB/REC series - Radial lead electrolytic capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^REA[0-9]+.*");
        registry.addPattern(ComponentType.CAPACITOR, "^REB[0-9]+.*");
        registry.addPattern(ComponentType.CAPACITOR, "^REC[0-9]+.*");

        // OVZ/OVR series - Conductive polymer capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^OVZ-[0-9]+.*");
        registry.addPattern(ComponentType.CAPACITOR, "^OVR-[0-9]+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CAPACITOR,
            ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        if (type == ComponentType.CAPACITOR) {
            return isLelonCapacitorMPN(upperMpn);
        }

        // Fall back to registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Check if the MPN is a Lelon capacitor.
     */
    private boolean isLelonCapacitorMPN(String upperMpn) {
        return VE_PATTERN.matcher(upperMpn).matches() ||
               VR_PATTERN.matcher(upperMpn).matches() ||
               VZ_PATTERN.matcher(upperMpn).matches() ||
               REA_PATTERN.matcher(upperMpn).matches() ||
               REB_PATTERN.matcher(upperMpn).matches() ||
               REC_PATTERN.matcher(upperMpn).matches() ||
               OVZ_PATTERN.matcher(upperMpn).matches() ||
               OVR_PATTERN.matcher(upperMpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Package code is typically the last 4 digits indicating diameter x height in mm
        // Format: DDLL where DD is diameter and LL is length
        // Examples: 0605=6.3x5mm, 0810=8x10mm, 0811=8x11mm, 1012=10x12mm

        // Look for 4-digit package code at the end
        if (upperMpn.length() >= 4) {
            String lastFour = upperMpn.substring(upperMpn.length() - 4);
            if (lastFour.matches("[0-9]{4}")) {
                return decodePackageCode(lastFour);
            }
        }

        // Try to find package code after hyphen and other parts
        int lastHyphen = upperMpn.lastIndexOf('-');
        if (lastHyphen >= 0 && upperMpn.length() > lastHyphen + 4) {
            String afterHyphen = upperMpn.substring(lastHyphen + 1);
            if (afterHyphen.length() >= 4) {
                String lastFour = afterHyphen.substring(afterHyphen.length() - 4);
                if (lastFour.matches("[0-9]{4}")) {
                    return decodePackageCode(lastFour);
                }
            }
        }

        return "";
    }

    /**
     * Decode the 4-digit package code to human-readable format.
     * Format: DDLL where DD is diameter (in 0.1mm units for values < 10) and LL is length
     */
    private String decodePackageCode(String code) {
        if (code.length() != 4) {
            return code;
        }

        String diameterCode = code.substring(0, 2);
        String lengthCode = code.substring(2, 4);

        String diameter = switch (diameterCode) {
            case "04" -> "4";
            case "05" -> "5";
            case "06" -> "6.3";
            case "08" -> "8";
            case "10" -> "10";
            case "12" -> "12.5";
            case "13" -> "13";
            case "16" -> "16";
            case "18" -> "18";
            case "22" -> "22";
            case "25" -> "25";
            case "30" -> "30";
            case "35" -> "35";
            default -> diameterCode;
        };

        String length = switch (lengthCode) {
            case "05" -> "5";
            case "07" -> "7";
            case "08" -> "8";
            case "10" -> "10";
            case "11" -> "11";
            case "12" -> "12";
            case "15" -> "15";
            case "16" -> "16";
            case "20" -> "20";
            case "25" -> "25";
            case "30" -> "30";
            case "35" -> "35";
            case "40" -> "40";
            case "45" -> "45";
            case "50" -> "50";
            default -> lengthCode;
        };

        return diameter + "x" + length + "mm";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // V-series (with hyphen)
        if (upperMpn.startsWith("VE-")) return "VE Low Impedance";
        if (upperMpn.startsWith("VR-")) return "VR Standard";
        if (upperMpn.startsWith("VZ-")) return "VZ High Temperature";

        // RE-series (radial lead, no hyphen)
        if (upperMpn.startsWith("REA")) return "REA Radial Lead";
        if (upperMpn.startsWith("REB")) return "REB Radial Lead";
        if (upperMpn.startsWith("REC")) return "REC Radial Lead";

        // OV-series (polymer, with hyphen)
        if (upperMpn.startsWith("OVZ-")) return "OVZ Conductive Polymer";
        if (upperMpn.startsWith("OVR-")) return "OVR Conductive Polymer";

        return "";
    }

    /**
     * Extract voltage rating from MPN.
     * Voltage codes appear after the capacitance and tolerance codes.
     * Examples: 1C=16V, 1E=25V, 1H=50V, 1V=35V, 2A=100V
     */
    public String extractVoltageRating(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Look for voltage code pattern: digit + letter
        // Common codes: 0J=6.3V, 1A=10V, 1C=16V, 1E=25V, 1V=35V, 1H=50V, 2A=100V
        java.util.regex.Matcher matcher = Pattern.compile("[0-9][A-Z]").matcher(upperMpn);

        // Skip the first match if it's part of the series prefix
        int startPos = 0;
        if (upperMpn.startsWith("VE-") || upperMpn.startsWith("VR-") ||
            upperMpn.startsWith("VZ-") || upperMpn.startsWith("OVZ-") ||
            upperMpn.startsWith("OVR-")) {
            startPos = upperMpn.indexOf('-') + 1;
        } else if (upperMpn.startsWith("REA") || upperMpn.startsWith("REB") ||
                   upperMpn.startsWith("REC")) {
            startPos = 3;
        }

        // Find voltage code after capacitance (3 digits) and tolerance (1 letter)
        if (upperMpn.length() > startPos + 4) {
            String afterCap = upperMpn.substring(startPos);
            // Match: 3 digits (capacitance) + 1 letter (tolerance) + voltage code
            java.util.regex.Matcher voltageMatcher =
                Pattern.compile("[0-9]{3}[A-Z]([0-9][A-Z])").matcher(afterCap);
            if (voltageMatcher.find()) {
                String voltageCode = voltageMatcher.group(1);
                return decodeVoltage(voltageCode);
            }
        }

        return "";
    }

    /**
     * Decode voltage code to actual voltage value.
     */
    private String decodeVoltage(String code) {
        if (code == null || code.length() != 2) {
            return "";
        }

        return switch (code) {
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
     * Extract capacitance value from MPN.
     * Uses 3-digit code where first two digits are significant and third is multiplier.
     * Examples: 101=100uF, 470=47uF, 102=1000uF
     */
    public String extractCapacitance(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Find start position after series prefix
        int startPos = 0;
        if (upperMpn.startsWith("VE-") || upperMpn.startsWith("VR-") ||
            upperMpn.startsWith("VZ-") || upperMpn.startsWith("OVZ-") ||
            upperMpn.startsWith("OVR-")) {
            startPos = upperMpn.indexOf('-') + 1;
        } else if (upperMpn.startsWith("REA") || upperMpn.startsWith("REB") ||
                   upperMpn.startsWith("REC")) {
            startPos = 3;
        }

        // Extract 3-digit capacitance code
        if (upperMpn.length() > startPos + 2) {
            String afterPrefix = upperMpn.substring(startPos);
            java.util.regex.Matcher capMatcher =
                Pattern.compile("^([0-9]{3})").matcher(afterPrefix);
            if (capMatcher.find()) {
                String capCode = capMatcher.group(1);
                return decodeCapacitance(capCode);
            }
        }

        return "";
    }

    /**
     * Decode 3-digit capacitance code to actual value in uF.
     * For electrolytic capacitors, the code represents uF directly:
     * First two digits are significant, third is multiplier (power of 10).
     * Examples: 101 = 10 x 10^1 = 100uF, 102 = 10 x 10^2 = 1000uF
     */
    private String decodeCapacitance(String code) {
        if (code == null || code.length() != 3) {
            return "";
        }

        try {
            int significantDigits = Integer.parseInt(code.substring(0, 2));
            int multiplier = Integer.parseInt(code.substring(2, 3));

            // For electrolytic capacitors, the value is directly in uF
            double valueInUf = significantDigits * Math.pow(10, multiplier);

            // Format based on size
            if (valueInUf >= 1000000) {
                return String.format("%.0fmF", valueInUf / 1000);
            } else if (valueInUf >= 1) {
                return String.format("%.0fuF", valueInUf);
            } else {
                return String.format("%.0fnF", valueInUf * 1000);
            }
        } catch (NumberFormatException e) {
            return code;
        }
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return false;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Package must match for any replacement
        if (!pkg1.equals(pkg2) || pkg1.isEmpty()) {
            return false;
        }

        // Same series - generally not auto-replacements (specs may differ)
        if (series1.equals(series2)) {
            return false;
        }

        // VE (low impedance) can replace VR (standard) - better performance
        if ((series1.equals("VE Low Impedance") && series2.equals("VR Standard")) ||
            (series2.equals("VE Low Impedance") && series1.equals("VR Standard"))) {
            return true;
        }

        // VZ (high temp) can replace VR/VE - wider temperature range
        if (series1.equals("VZ High Temperature") &&
            (series2.equals("VR Standard") || series2.equals("VE Low Impedance"))) {
            return true;
        }
        if (series2.equals("VZ High Temperature") &&
            (series1.equals("VR Standard") || series1.equals("VE Low Impedance"))) {
            return true;
        }

        // OVZ/OVR polymer series can replace standard VR series - longer life
        if ((series1.contains("Polymer") && series2.equals("VR Standard")) ||
            (series2.contains("Polymer") && series1.equals("VR Standard"))) {
            return true;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
