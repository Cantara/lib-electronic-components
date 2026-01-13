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
 * Handler for Sumida Corporation power inductors.
 * <p>
 * Supports the following product series:
 * <ul>
 *   <li>CDRH series - SMD power inductors (CDRH4D28, CDRH6D28, CDRH8D43)</li>
 *   <li>CDR series - Drum core inductors (CDR74, CDR104, CDR125)</li>
 *   <li>CDEP series - SMD shielded power inductors (CDEP104, CDEP105)</li>
 *   <li>CDEF series - SMD high efficiency inductors (CDEF38)</li>
 *   <li>CR series - Chip inductors (CR16, CR21, CR32)</li>
 *   <li>RCH series - High current inductors (RCH110, RCH895)</li>
 *   <li>CEP series - Edge-wound inductors</li>
 *   <li>CDC series - Common mode chokes (CDC2G2)</li>
 *   <li>CLF series - Low profile power inductors (CLF10040)</li>
 * </ul>
 * <p>
 * MPN Pattern examples:
 * <ul>
 *   <li>CDRH6D28NP-4R7NC - 6x6x2.8mm SMD power inductor, 4.7uH</li>
 *   <li>CDR125NP-100MC - Drum core, 10uH</li>
 *   <li>CDEP105NP-1R5MC - Shielded power, 1.5uH</li>
 *   <li>CR21-100JM - Chip inductor, 10uH</li>
 * </ul>
 */
public class SumidaHandler implements ManufacturerHandler {

    // Pattern for CDRH series (SMD power inductors)
    // Format: CDRH<size><suffix>-<value><tolerance><termination>
    private static final Pattern CDRH_PATTERN = Pattern.compile(
            "^CDRH[0-9]+[A-Z]?[0-9]*[A-Z]*-?[0-9R]+[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for CDR series (Drum core inductors)
    // Format: CDR<size><suffix>-<value><tolerance><termination>
    private static final Pattern CDR_PATTERN = Pattern.compile(
            "^CDR[0-9]+[A-Z]*-?[0-9R]+[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for CDEP series (SMD shielded power inductors)
    private static final Pattern CDEP_PATTERN = Pattern.compile(
            "^CDEP[0-9]+[A-Z]*-?[0-9R]+[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for CDEF series (SMD high efficiency inductors)
    private static final Pattern CDEF_PATTERN = Pattern.compile(
            "^CDEF[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for CR series (Chip inductors)
    private static final Pattern CR_PATTERN = Pattern.compile(
            "^CR[0-9]+[A-Z]*-?[0-9R]+[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for RCH series (High current inductors)
    private static final Pattern RCH_PATTERN = Pattern.compile(
            "^RCH[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for CEP series (Edge-wound inductors)
    private static final Pattern CEP_PATTERN = Pattern.compile(
            "^CEP[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for CDC series (Common mode chokes)
    private static final Pattern CDC_PATTERN = Pattern.compile(
            "^CDC[0-9A-Z]+[A-Z]*-?[0-9R]*[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern for CLF series (Low profile power inductors)
    private static final Pattern CLF_PATTERN = Pattern.compile(
            "^CLF[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Combined pattern for all Sumida inductors
    private static final Pattern SUMIDA_INDUCTOR_PATTERN = Pattern.compile(
            "^(CDRH|CDR|CDEP|CDEF|CR|RCH|CEP|CDC|CLF)[0-9A-Z]+-?[0-9R]*[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to extract inductance value from MPN
    // Formats: 4R7 = 4.7uH, 100 = 10uH, 1R5 = 1.5uH, R47 = 0.47uH
    private static final Pattern VALUE_PATTERN = Pattern.compile(
            "-([0-9R]+)[A-Z]*$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to extract size code from CDRH series (e.g., 6D28 from CDRH6D28)
    private static final Pattern CDRH_SIZE_PATTERN = Pattern.compile(
            "^CDRH([0-9]+[A-Z]?[0-9]+)",
            Pattern.CASE_INSENSITIVE);

    // Pattern to extract size code from other series (e.g., 125 from CDR125)
    private static final Pattern SIZE_PATTERN = Pattern.compile(
            "^(?:CDR|CDEP|CDEF|CLF|CR|RCH|CEP|CDC)([0-9A-Z]+)",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Register patterns for base INDUCTOR type
        registry.addPattern(ComponentType.INDUCTOR, "^CDRH[0-9]+[A-Z]?[0-9]*[A-Z]*-?[0-9R]+[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^CDR[0-9]+[A-Z]*-?[0-9R]+[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^CDEP[0-9]+[A-Z]*-?[0-9R]+[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^CDEF[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^CR[0-9]+[A-Z]*-?[0-9R]+[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^RCH[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^CEP[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^CDC[0-9A-Z]+[A-Z]*-?[0-9R]*[A-Z]*$");
        registry.addPattern(ComponentType.INDUCTOR, "^CLF[0-9]+[A-Z]*-?[0-9R]*[A-Z]*$");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.INDUCTOR);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only match INDUCTOR type
        if (type != ComponentType.INDUCTOR) {
            return false;
        }

        // Check against all Sumida patterns
        return CDRH_PATTERN.matcher(upperMpn).matches() ||
                CDR_PATTERN.matcher(upperMpn).matches() ||
                CDEP_PATTERN.matcher(upperMpn).matches() ||
                CDEF_PATTERN.matcher(upperMpn).matches() ||
                CR_PATTERN.matcher(upperMpn).matches() ||
                RCH_PATTERN.matcher(upperMpn).matches() ||
                CEP_PATTERN.matcher(upperMpn).matches() ||
                CDC_PATTERN.matcher(upperMpn).matches() ||
                CLF_PATTERN.matcher(upperMpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // CDRH series: extract size code like 6D28 (6x6x2.8mm), 4D28 (4x4x2.8mm), 8D43 (8x8x4.3mm)
        if (upperMpn.startsWith("CDRH")) {
            Matcher matcher = CDRH_SIZE_PATTERN.matcher(upperMpn);
            if (matcher.find()) {
                String sizeCode = matcher.group(1);
                return decodeCDRHSize(sizeCode);
            }
        }

        // CDR series: extract size from number (e.g., CDR74 = 7.4mm, CDR125 = 12.5mm diameter)
        if (upperMpn.startsWith("CDR") && !upperMpn.startsWith("CDRH")) {
            Matcher matcher = SIZE_PATTERN.matcher(upperMpn);
            if (matcher.find()) {
                String sizeCode = matcher.group(1);
                return decodeCDRSize(sizeCode);
            }
        }

        // CDEP series: extract size code (e.g., CDEP104 = 10x10x4mm, CDEP105 = 10x10x5mm)
        if (upperMpn.startsWith("CDEP")) {
            Matcher matcher = SIZE_PATTERN.matcher(upperMpn);
            if (matcher.find()) {
                String sizeCode = matcher.group(1);
                return decodeCDEPSize(sizeCode);
            }
        }

        // CR series: chip inductor sizes (CR16, CR21, CR32)
        if (upperMpn.startsWith("CR")) {
            Matcher matcher = SIZE_PATTERN.matcher(upperMpn);
            if (matcher.find()) {
                String sizeCode = matcher.group(1);
                return decodeCRSize(sizeCode);
            }
        }

        // CLF series: low profile (e.g., CLF10040 = 10x10x4.0mm)
        if (upperMpn.startsWith("CLF")) {
            Matcher matcher = SIZE_PATTERN.matcher(upperMpn);
            if (matcher.find()) {
                String sizeCode = matcher.group(1);
                return decodeCLFSize(sizeCode);
            }
        }

        // For other series, return the numeric portion as the size code
        Matcher matcher = SIZE_PATTERN.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    /**
     * Decode CDRH size code to dimensions.
     * Format: LDH where L=length/width, D=delimiter, H=height
     * Example: 6D28 = 6x6x2.8mm, 4D28 = 4x4x2.8mm
     */
    private String decodeCDRHSize(String sizeCode) {
        if (sizeCode == null || sizeCode.isEmpty()) return "";

        // Pattern like "6D28" -> "6x6x2.8mm"
        if (sizeCode.matches("[0-9]+[A-Z][0-9]+")) {
            int delimIdx = -1;
            for (int i = 0; i < sizeCode.length(); i++) {
                if (Character.isLetter(sizeCode.charAt(i))) {
                    delimIdx = i;
                    break;
                }
            }
            if (delimIdx > 0 && delimIdx < sizeCode.length() - 1) {
                String width = sizeCode.substring(0, delimIdx);
                String height = sizeCode.substring(delimIdx + 1);
                // Convert height: 28 -> 2.8, 43 -> 4.3
                String heightMm = height.length() >= 2 ?
                        height.charAt(0) + "." + height.substring(1) : height;
                return width + "x" + width + "x" + heightMm + "mm";
            }
        }

        return sizeCode;
    }

    /**
     * Decode CDR drum core size code.
     * Example: 74 = 7.4mm diameter, 125 = 12.5mm diameter
     */
    private String decodeCDRSize(String sizeCode) {
        if (sizeCode == null || sizeCode.isEmpty()) return "";

        // Remove any trailing letters (like NP)
        String numericPart = sizeCode.replaceAll("[A-Z]+$", "");

        if (numericPart.length() == 2) {
            // 74 -> 7.4mm
            return numericPart.charAt(0) + "." + numericPart.charAt(1) + "mm";
        } else if (numericPart.length() == 3) {
            // 125 -> 12.5mm
            return numericPart.substring(0, 2) + "." + numericPart.charAt(2) + "mm";
        }

        return sizeCode;
    }

    /**
     * Decode CDEP shielded power inductor size.
     * Example: 104 = 10x10x4mm, 105 = 10x10x5mm
     */
    private String decodeCDEPSize(String sizeCode) {
        if (sizeCode == null || sizeCode.isEmpty()) return "";

        // Remove trailing letters
        String numericPart = sizeCode.replaceAll("[A-Z]+$", "");

        if (numericPart.length() == 3) {
            // 104 -> 10x10x4mm
            String width = numericPart.substring(0, 2);
            String height = numericPart.substring(2);
            return width + "x" + width + "x" + height + "mm";
        }

        return sizeCode;
    }

    /**
     * Decode CR chip inductor size.
     * Example: 16 = 1.6mm, 21 = 2.1mm, 32 = 3.2mm
     */
    private String decodeCRSize(String sizeCode) {
        if (sizeCode == null || sizeCode.isEmpty()) return "";

        // Remove trailing letters
        String numericPart = sizeCode.replaceAll("[A-Z]+$", "");

        if (numericPart.length() == 2) {
            // 16 -> 1.6x0.8mm (approximate chip size)
            return numericPart.charAt(0) + "." + numericPart.charAt(1) + "mm";
        }

        return sizeCode;
    }

    /**
     * Decode CLF low profile inductor size.
     * Example: 10040 = 10x10x4.0mm (format: LLWWH where LL=length, WW=width, H=height)
     */
    private String decodeCLFSize(String sizeCode) {
        if (sizeCode == null || sizeCode.isEmpty()) return "";

        // Remove trailing letters
        String numericPart = sizeCode.replaceAll("[A-Z]+$", "");

        if (numericPart.length() == 5) {
            // 10040 -> 10x10x4.0mm (LLWWH format)
            String length = numericPart.substring(0, 2);
            String width = numericPart.substring(2, 4);
            // For 5-digit codes: first 2 = length, next 2 = width (same as length for square), last = height
            // Actually CLF10040 means: 10 = 10mm length, 04 could be part of encoding
            // Looking at real datasheets: CLF10040 = 10.0x10.0x4.0mm
            // The format is LLWHH where LL=10, W=0, HH=40 meaning 4.0mm height
            // Let's re-interpret: 100 40 -> 10.0mm square, 4.0mm height
            String height = numericPart.substring(3, 4) + "." + numericPart.substring(4);
            return length + "x" + length + "x" + height + "mm";
        }

        return sizeCode;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check each series prefix
        if (upperMpn.startsWith("CDRH")) return "CDRH";
        if (upperMpn.startsWith("CDEP")) return "CDEP";
        if (upperMpn.startsWith("CDEF")) return "CDEF";
        if (upperMpn.startsWith("CDC")) return "CDC";
        if (upperMpn.startsWith("CDR")) return "CDR";
        if (upperMpn.startsWith("CLF")) return "CLF";
        if (upperMpn.startsWith("CR")) return "CR";
        if (upperMpn.startsWith("RCH")) return "RCH";
        if (upperMpn.startsWith("CEP")) return "CEP";

        return "";
    }

    /**
     * Extract inductance value from MPN.
     * <p>
     * Value encoding formats:
     * <ul>
     *   <li>4R7 = 4.7uH (R is decimal point)</li>
     *   <li>R47 = 0.47uH (R as leading decimal)</li>
     *   <li>100 = 10uH (first two digits * 10^third digit, but typically simplified)</li>
     *   <li>1R5 = 1.5uH</li>
     *   <li>10R = 10uH</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the inductance value string (e.g., "4.7uH") or empty string if not found
     */
    public String extractInductanceValue(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Find the value portion after the hyphen
        Matcher matcher = VALUE_PATTERN.matcher(upperMpn);
        if (matcher.find()) {
            String valueCode = matcher.group(1);
            return decodeInductanceValue(valueCode);
        }

        return "";
    }

    /**
     * Decode inductance value from code.
     * <p>
     * Examples:
     * <ul>
     *   <li>4R7 -> 4.7uH</li>
     *   <li>R47 -> 0.47uH</li>
     *   <li>100 -> 10uH (10 * 10^0)</li>
     *   <li>220 -> 22uH (22 * 10^0)</li>
     *   <li>101 -> 100uH (10 * 10^1)</li>
     *   <li>1R5 -> 1.5uH</li>
     *   <li>10R -> 10uH</li>
     * </ul>
     * <p>
     * Standard EIA inductor value code: first two digits are significant,
     * third digit is multiplier (10^n).
     */
    private String decodeInductanceValue(String valueCode) {
        if (valueCode == null || valueCode.isEmpty()) return "";

        // Handle R notation (R = decimal point)
        if (valueCode.contains("R")) {
            if (valueCode.startsWith("R")) {
                // R47 -> 0.47uH
                return "0." + valueCode.substring(1) + "uH";
            } else if (valueCode.endsWith("R")) {
                // 10R -> 10uH
                return valueCode.substring(0, valueCode.length() - 1) + "uH";
            } else {
                // 4R7 -> 4.7uH
                return valueCode.replace("R", ".") + "uH";
            }
        }

        // Pure numeric value - interpret as EIA multiplier code
        // Standard: first two digits * 10^(third digit)
        // 100 = 10 * 10^0 = 10uH
        // 220 = 22 * 10^0 = 22uH
        // 101 = 10 * 10^1 = 100uH
        // 102 = 10 * 10^2 = 1000uH = 1mH
        if (valueCode.matches("[0-9]+")) {
            if (valueCode.length() == 3) {
                // Standard EIA code: first two digits * 10^third digit
                int significand = Integer.parseInt(valueCode.substring(0, 2));
                int exponent = Integer.parseInt(valueCode.substring(2));
                double value = significand * Math.pow(10, exponent);
                if (value >= 1000) {
                    return String.format("%.0fmH", value / 1000);
                } else {
                    return String.format("%.0fuH", value);
                }
            } else if (valueCode.length() == 2) {
                // Two digit: direct value in uH
                return valueCode + "uH";
            }
        }

        return valueCode + "uH";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Must have same inductance value
        String value1 = extractInductanceValue(mpn1);
        String value2 = extractInductanceValue(mpn2);

        if (value1.isEmpty() || value2.isEmpty()) {
            return false;
        }

        return value1.equals(value2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Check if the MPN is a Sumida inductor.
     *
     * @param mpn the manufacturer part number
     * @return true if it matches any Sumida inductor pattern
     */
    public boolean isSumidaInductor(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;
        return SUMIDA_INDUCTOR_PATTERN.matcher(mpn.toUpperCase()).matches();
    }

    /**
     * Get a description of the inductor type based on series.
     *
     * @param mpn the manufacturer part number
     * @return description of the inductor type
     */
    public String getInductorTypeDescription(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);

        return switch (series) {
            case "CDRH" -> "SMD Power Inductor";
            case "CDR" -> "Drum Core Inductor";
            case "CDEP" -> "SMD Shielded Power Inductor";
            case "CDEF" -> "SMD High Efficiency Inductor";
            case "CR" -> "Chip Inductor";
            case "RCH" -> "High Current Inductor";
            case "CEP" -> "Edge-Wound Inductor";
            case "CDC" -> "Common Mode Choke";
            case "CLF" -> "Low Profile Power Inductor";
            default -> "";
        };
    }
}
