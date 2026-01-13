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
 * Handler for SG Micro Corp components.
 * <p>
 * SG Micro specializes in analog integrated circuits including:
 * - SGM2xxx: LDO regulators (SGM2019, SGM2036, SGM2040)
 * - SGM4xxx: ADCs/DACs and analog switches (SGM4567)
 * - SGM58xxx: ADCs (SGM58031)
 * - SGM6xxx: DC-DC converters (SGM6132, SGM6603)
 * - SGM8xxx: Op-amps and comparators (SGM8051, SGM8262, SGM8521, SGM8711, SGM8722)
 * <p>
 * Pattern examples: SGM2019-3.3YN5G/TR, SGM8051XN5G/TR, SGM6603AYTD6G/TR
 */
public class SGMicroHandler implements ManufacturerHandler {

    // Package code mappings for SG Micro components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Common SG Micro package suffixes
        PACKAGE_CODES.put("YN", "SOT-23");
        PACKAGE_CODES.put("YN5", "SOT-23-5");
        PACKAGE_CODES.put("YN6", "SOT-23-6");
        PACKAGE_CODES.put("YN8", "SOT-23-8");
        PACKAGE_CODES.put("XN", "SC70");
        PACKAGE_CODES.put("XN5", "SC70-5");
        PACKAGE_CODES.put("XN6", "SC70-6");
        PACKAGE_CODES.put("YS", "SOIC-8");
        PACKAGE_CODES.put("YS8", "SOIC-8");
        PACKAGE_CODES.put("YS14", "SOIC-14");
        PACKAGE_CODES.put("YS16", "SOIC-16");
        PACKAGE_CODES.put("XS", "MSOP-8");
        PACKAGE_CODES.put("XS8", "MSOP-8");
        PACKAGE_CODES.put("XS10", "MSOP-10");
        PACKAGE_CODES.put("YTD", "DFN");
        PACKAGE_CODES.put("YTD6", "DFN-6");
        PACKAGE_CODES.put("YTD8", "DFN-8");
        PACKAGE_CODES.put("XTD", "DFN");
        PACKAGE_CODES.put("YQN", "QFN");
        PACKAGE_CODES.put("YQN16", "QFN-16");
        PACKAGE_CODES.put("YQN20", "QFN-20");
        PACKAGE_CODES.put("UTD", "WLCSP");
        PACKAGE_CODES.put("UFC", "WLCSP");
    }

    // Series descriptions for documentation
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("SGM2", "LDO Regulators");
        SERIES_INFO.put("SGM4", "ADCs/DACs/Analog Switches");
        SERIES_INFO.put("SGM58", "High-Precision ADCs");
        SERIES_INFO.put("SGM6", "DC-DC Converters");
        SERIES_INFO.put("SGM8", "Op-Amps/Comparators");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.OPAMP
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // SGM2xxx - LDO regulators (SGM2019, SGM2036, SGM2040, etc.)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SGM2[0-9]{3}[A-Z0-9./-]*$");
        registry.addPattern(ComponentType.IC, "^SGM2[0-9]{3}[A-Z0-9./-]*$");

        // SGM4xxx - ADCs/DACs and analog switches
        registry.addPattern(ComponentType.IC, "^SGM4[0-9]{3,4}[A-Z0-9./-]*$");

        // SGM58xxx - High-precision ADCs (5 digits after SGM)
        registry.addPattern(ComponentType.IC, "^SGM58[0-9]{3}[A-Z0-9./-]*$");

        // SGM6xxx - DC-DC converters (SGM6132, SGM6603, etc.)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SGM6[0-9]{3}[A-Z0-9./-]*$");
        registry.addPattern(ComponentType.IC, "^SGM6[0-9]{3}[A-Z0-9./-]*$");

        // SGM8xxx - Op-amps (SGM8051, SGM8262, SGM8521)
        registry.addPattern(ComponentType.OPAMP, "^SGM8[0-5][0-9]{2}[A-Z0-9./-]*$");
        registry.addPattern(ComponentType.IC, "^SGM8[0-5][0-9]{2}[A-Z0-9./-]*$");

        // SGM8xxx - Comparators (SGM8711, SGM8722)
        // Comparators typically have part numbers SGM87xx or higher
        registry.addPattern(ComponentType.IC, "^SGM8[7-9][0-9]{2}[A-Z0-9./-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle SG Micro MPNs (start with SGM)
        if (!upperMpn.startsWith("SGM")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case VOLTAGE_REGULATOR:
                return isVoltageRegulator(upperMpn);
            case OPAMP:
                return isOpAmp(upperMpn);
            case IC:
                return isSGMicroPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isSGMicroPart(String mpn) {
        // Match SGM followed by 4 or 5 digits
        return mpn.matches("^SGM[0-9]{4,5}[A-Z0-9./-]*$");
    }

    private boolean isVoltageRegulator(String mpn) {
        // SGM2xxx - LDO regulators
        // SGM6xxx - DC-DC converters
        return mpn.matches("^SGM2[0-9]{3}[A-Z0-9./-]*$") ||
               mpn.matches("^SGM6[0-9]{3}[A-Z0-9./-]*$");
    }

    private boolean isOpAmp(String mpn) {
        // SGM8xxx - Op-amps (typically SGM80xx through SGM85xx)
        // SGM8051, SGM8262, SGM8521 are op-amps
        // SGM87xx+ are comparators, not op-amps
        return mpn.matches("^SGM8[0-5][0-9]{2}[A-Z0-9./-]*$");
    }

    private boolean isComparator(String mpn) {
        // SGM87xx and SGM88xx are comparators (SGM8711, SGM8722)
        return mpn.matches("^SGM8[7-9][0-9]{2}[A-Z0-9./-]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove common suffixes: G, /TR, G/TR (tape and reel), etc.
        String baseMpn = upperMpn.replaceAll("G?(/TR)?$", "");

        // Extract package code - typically after the part number
        // Pattern: SGM[0-9]{4,5}[-.]?[voltage]?[package-code]
        // Examples: SGM2019-3.3YN5, SGM8051XN5, SGM6603AYTD6

        // Try to find package code pattern (2-4 uppercase letters optionally followed by digits)
        Pattern packagePattern = Pattern.compile(".*?([XYUQ][A-Z]*[0-9]*)(?:G?(/TR)?)?$");
        java.util.regex.Matcher matcher = packagePattern.matcher(baseMpn);

        if (matcher.matches()) {
            String code = matcher.group(1);
            // Try full code first, then prefix without trailing numbers
            if (PACKAGE_CODES.containsKey(code)) {
                return PACKAGE_CODES.get(code);
            }
            // Try without trailing digits
            String codePrefix = code.replaceAll("[0-9]+$", "");
            if (PACKAGE_CODES.containsKey(codePrefix)) {
                return PACKAGE_CODES.get(codePrefix);
            }
            return code;
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract series - SGMxx where xx determines the product family
        // SGM2xxx, SGM4xxx, SGM58xxx, SGM6xxx, SGM8xxx

        if (upperMpn.startsWith("SGM58") && upperMpn.length() >= 8) {
            return "SGM58";  // High-precision ADC series
        }

        if (upperMpn.matches("^SGM[0-9][0-9]{3}.*")) {
            return upperMpn.substring(0, 4);  // Returns SGM2, SGM4, SGM6, SGM8
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

        // Extract the base part number (SGMxxxx portion without voltage/package)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2);
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match SGM[0-9]{4,5} - the core part number
        // Stop at hyphen/dot (voltage indicator) or letter (package code)
        Pattern basePattern = Pattern.compile("^(SGM[0-9]{4,5}).*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);

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
     * @param series the series code (e.g., "SGM2", "SGM8")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine if an MPN is an op-amp.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an op-amp (SGM80xx-SGM85xx)
     */
    public boolean isOpAmpPart(String mpn) {
        if (mpn == null) return false;
        return isOpAmp(mpn.toUpperCase());
    }

    /**
     * Determine if an MPN is a comparator.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a comparator (SGM87xx+)
     */
    public boolean isComparatorPart(String mpn) {
        if (mpn == null) return false;
        return isComparator(mpn.toUpperCase());
    }

    /**
     * Determine if an MPN is an LDO regulator.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an LDO (SGM2xxx)
     */
    public boolean isLDOPart(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^SGM2[0-9]{3}[A-Z0-9./-]*$");
    }

    /**
     * Determine if an MPN is a DC-DC converter.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a DC-DC converter (SGM6xxx)
     */
    public boolean isDCDCPart(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^SGM6[0-9]{3}[A-Z0-9./-]*$");
    }
}
