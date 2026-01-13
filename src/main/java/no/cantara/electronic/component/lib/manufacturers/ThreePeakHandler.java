package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler for 3PEAK (Suzhou 3PEAK Electronic Inc.) components.
 * <p>
 * 3PEAK specializes in analog ICs including:
 * - TP1xxx: Op-amps (TP1541, TP1561 comparator)
 * - TP2xxx: Precision op-amps (TP2111, TP2304, TP2345 comparator)
 * - TP5xxx: ADCs (TP5551, TP5854)
 * - TP7xxx: LDO voltage regulators (TP7140, TP7150)
 * - TP1xx: Current sense amplifiers (TP181, TP182)
 * <p>
 * Pattern examples: TP1541-TR, TP2111-QR, TP7150-MR, TP181-DR
 * <p>
 * Package codes:
 * - TR: SOT-23
 * - QR: QFN
 * - MR: MSOP
 * - DR: SOIC
 */
public class ThreePeakHandler implements ManufacturerHandler {

    // Package code mappings for 3PEAK components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        PACKAGE_CODES.put("TR", "SOT-23");
        PACKAGE_CODES.put("QR", "QFN");
        PACKAGE_CODES.put("MR", "MSOP");
        PACKAGE_CODES.put("DR", "SOIC");
        PACKAGE_CODES.put("SR", "SOP");
        PACKAGE_CODES.put("PR", "TSSOP");
    }

    // Series descriptions for extractSeries documentation
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("TP1", "Op-Amps/Comparators");
        SERIES_INFO.put("TP2", "Precision Op-Amps/Comparators");
        SERIES_INFO.put("TP5", "ADCs");
        SERIES_INFO.put("TP7", "LDO Regulators");
        SERIES_INFO.put("TP18", "Current Sense Amplifiers");
    }

    // Op-amp part numbers (4-digit, TP1xxx and TP2xxx, excluding comparators)
    private static final Pattern OPAMP_PATTERN = Pattern.compile(
            "^TP(1541|2111|2304|2071|2072|2082|2092|2231|2232)[A-Z0-9-]*$",
            Pattern.CASE_INSENSITIVE);

    // Comparator part numbers
    private static final Pattern COMPARATOR_PATTERN = Pattern.compile(
            "^TP(1561|2345|1393|2393)[A-Z0-9-]*$",
            Pattern.CASE_INSENSITIVE);

    // LDO part numbers (TP7xxx)
    private static final Pattern LDO_PATTERN = Pattern.compile(
            "^TP7[0-9]{3}[A-Z0-9-]*$",
            Pattern.CASE_INSENSITIVE);

    // ADC part numbers (TP5xxx)
    private static final Pattern ADC_PATTERN = Pattern.compile(
            "^TP5[0-9]{3}[A-Z0-9-]*$",
            Pattern.CASE_INSENSITIVE);

    // Current sense amplifier part numbers (TP1xx, 3 digits)
    private static final Pattern CURRENT_SENSE_PATTERN = Pattern.compile(
            "^TP1[0-9]{2}[A-Z0-9-]*$",
            Pattern.CASE_INSENSITIVE);

    // Generic 3PEAK pattern (TP followed by digits)
    private static final Pattern GENERIC_3PEAK_PATTERN = Pattern.compile(
            "^TP[0-9]{3,4}[A-Z0-9-]*$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.OPAMP,
                ComponentType.VOLTAGE_REGULATOR
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Op-amps (TP1541, TP2111, TP2304, etc.)
        registry.addPattern(ComponentType.OPAMP, "^TP(1541|2111|2304|2071|2072|2082|2092|2231|2232)[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^TP(1541|2111|2304|2071|2072|2082|2092|2231|2232)[A-Z0-9-]*$");

        // Comparators (TP1561, TP2345, etc.) - registered under IC
        registry.addPattern(ComponentType.IC, "^TP(1561|2345|1393|2393)[A-Z0-9-]*$");

        // LDO regulators (TP7xxx)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^TP7[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^TP7[0-9]{3}[A-Z0-9-]*$");

        // ADCs (TP5xxx)
        registry.addPattern(ComponentType.IC, "^TP5[0-9]{3}[A-Z0-9-]*$");

        // Current sense amplifiers (TP1xx, 3 digits)
        registry.addPattern(ComponentType.OPAMP, "^TP1[0-9]{2}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^TP1[0-9]{2}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle 3PEAK MPNs (start with TP)
        if (!upperMpn.startsWith("TP")) {
            return false;
        }

        // Verify this is a 3PEAK part (TP + digits)
        if (!GENERIC_3PEAK_PATTERN.matcher(upperMpn).matches()) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case OPAMP:
                return isOpAmp(upperMpn);
            case VOLTAGE_REGULATOR:
                return isLDO(upperMpn);
            case IC:
                return is3PeakPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean is3PeakPart(String mpn) {
        return GENERIC_3PEAK_PATTERN.matcher(mpn).matches();
    }

    private boolean isOpAmp(String mpn) {
        // Exclude comparators first (they also match current sense pattern prefix)
        if (COMPARATOR_PATTERN.matcher(mpn).matches()) {
            return false;
        }
        // Op-amps: TP1541, TP2111, TP2304, etc.
        // Current sense amps: TP181, TP182 (also analog amplifiers)
        return OPAMP_PATTERN.matcher(mpn).matches() ||
               CURRENT_SENSE_PATTERN.matcher(mpn).matches();
    }

    private boolean isComparator(String mpn) {
        return COMPARATOR_PATTERN.matcher(mpn).matches();
    }

    private boolean isLDO(String mpn) {
        return LDO_PATTERN.matcher(mpn).matches();
    }

    private boolean isADC(String mpn) {
        return ADC_PATTERN.matcher(mpn).matches();
    }

    private boolean isCurrentSenseAmp(String mpn) {
        return CURRENT_SENSE_PATTERN.matcher(mpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove trailing suffixes like -5, -33 (voltage options)
        String baseMpn = upperMpn.replaceAll("-[0-9]+$", "");

        // Extract the 2-letter package code suffix
        // Pattern: TPxxxx-XX or TPxxx-XX where XX is package code
        java.util.regex.Matcher matcher = Pattern.compile("^TP[0-9]{3,4}-?([A-Z]{2})$").matcher(baseMpn);
        if (matcher.matches()) {
            String code = matcher.group(1);
            return PACKAGE_CODES.getOrDefault(code, code);
        }

        // Try matching package code at the end (no hyphen)
        // e.g., TP1541TR, TP7150MR
        matcher = Pattern.compile("^TP[0-9]{3,4}([A-Z]{2})$").matcher(baseMpn);
        if (matcher.matches()) {
            String code = matcher.group(1);
            return PACKAGE_CODES.getOrDefault(code, code);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Current sense amps: TP18x series
        if (upperMpn.matches("^TP18[0-9].*")) {
            return "TP18";
        }

        // Standard series: TP1xxx, TP2xxx, TP5xxx, TP7xxx
        if (upperMpn.matches("^TP[1257][0-9]{3}.*")) {
            return upperMpn.substring(0, 3);  // Returns TP1, TP2, TP5, TP7
        }

        // 3-digit parts (current sense amps like TP181)
        if (upperMpn.matches("^TP[0-9]{3}.*")) {
            return upperMpn.substring(0, 3);  // Returns full 3-char prefix
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be in the same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract the base part number (TPxxxx portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2);
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match TP[0-9]{3,4} - the core part number
        java.util.regex.Matcher matcher = Pattern.compile("^(TP[0-9]{3,4}).*$").matcher(upperMpn);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get a description for the given series.
     *
     * @param series the series code (e.g., "TP1", "TP7")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine the component category for a given MPN.
     *
     * @param mpn the manufacturer part number
     * @return category string: "Op-Amp", "Comparator", "LDO", "ADC", "Current Sense", or "Unknown"
     */
    public String getComponentCategory(String mpn) {
        if (mpn == null) return "Unknown";
        String upper = mpn.toUpperCase();

        if (OPAMP_PATTERN.matcher(upper).matches()) return "Op-Amp";
        if (COMPARATOR_PATTERN.matcher(upper).matches()) return "Comparator";
        if (LDO_PATTERN.matcher(upper).matches()) return "LDO";
        if (ADC_PATTERN.matcher(upper).matches()) return "ADC";
        if (CURRENT_SENSE_PATTERN.matcher(upper).matches()) return "Current Sense";

        return "Unknown";
    }
}
