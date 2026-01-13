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
 * Handler for Macroblock Inc. components.
 * <p>
 * Macroblock specializes in LED driver ICs for displays, including:
 * - MBI5xxx series: Constant current LED drivers (MBI5024, MBI5039, MBI5153)
 * - MBI6xxx series: DC-DC LED drivers
 * - MBI1xxx series: Scan drivers for LED displays
 * <p>
 * Pattern examples: MBI5024, MBI5039GP, MBI5153, MBI6651, MBI1801
 */
public class MacroblockHandler implements ManufacturerHandler {

    // Package code mappings for Macroblock components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Common Macroblock package suffixes
        PACKAGE_CODES.put("GP", "SOP-24");
        PACKAGE_CODES.put("GF", "SSOP-24");
        PACKAGE_CODES.put("GH", "TSSOP-24");
        PACKAGE_CODES.put("GS", "SSOP-24");
        PACKAGE_CODES.put("GT", "TSSOP-24");
        PACKAGE_CODES.put("GN", "QFN");
        PACKAGE_CODES.put("GQ", "QFN");
        PACKAGE_CODES.put("TE", "TQFP");
        PACKAGE_CODES.put("TF", "TQFP-48");
        PACKAGE_CODES.put("TP", "TQFP-64");
        PACKAGE_CODES.put("S", "SOP");
        PACKAGE_CODES.put("T", "TSSOP");
        PACKAGE_CODES.put("N", "QFN");
        PACKAGE_CODES.put("P", "PDIP");
    }

    // Series descriptions for extractSeries documentation
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("MBI5", "Constant Current LED Drivers");
        SERIES_INFO.put("MBI6", "DC-DC LED Drivers");
        SERIES_INFO.put("MBI1", "Scan Drivers");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.LED_DRIVER
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MBI5xxx - Constant current LED drivers (16-bit shift registers, PWM drivers)
        // MBI5024: 16-channel constant current LED driver
        // MBI5039: 16-channel constant current with error detection
        // MBI5153: 48-channel constant current LED driver
        registry.addPattern(ComponentType.LED_DRIVER, "^MBI5[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MBI5[0-9]{3}[A-Z0-9-]*$");

        // MBI6xxx - DC-DC LED drivers for backlighting and LED lighting
        registry.addPattern(ComponentType.LED_DRIVER, "^MBI6[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MBI6[0-9]{3}[A-Z0-9-]*$");

        // MBI1xxx - Scan drivers for LED matrix displays
        registry.addPattern(ComponentType.LED_DRIVER, "^MBI1[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MBI1[0-9]{3}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Macroblock MPNs (start with MBI)
        if (!upperMpn.startsWith("MBI")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case LED_DRIVER:
                return isMacroblockLEDDriver(upperMpn);
            case IC:
                return isMacroblockPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isMacroblockPart(String mpn) {
        // Match MBI[156]xxx series
        return mpn.matches("^MBI[156][0-9]{3}[A-Z0-9-]*$");
    }

    private boolean isMacroblockLEDDriver(String mpn) {
        // All MBI series are LED drivers
        return mpn.matches("^MBI5[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^MBI6[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^MBI1[0-9]{3}[A-Z0-9-]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove common suffixes: -TR (tape and reel), etc.
        String baseMpn = upperMpn.replaceAll("-TR$", "");

        // Extract package code - typically 1-2 letters after the part number
        // Pattern: MBI[156]xxx[package-code]
        Pattern packagePattern = Pattern.compile("^MBI[156][0-9]{3}([A-Z]{1,2}).*$");
        java.util.regex.Matcher matcher = packagePattern.matcher(baseMpn);

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

        // MBI5xxx series
        if (upperMpn.matches("^MBI5[0-9]{3}.*")) {
            return "MBI5";
        }

        // MBI6xxx series
        if (upperMpn.matches("^MBI6[0-9]{3}.*")) {
            return "MBI6";
        }

        // MBI1xxx series
        if (upperMpn.matches("^MBI1[0-9]{3}.*")) {
            return "MBI1";
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

        // Extract the base part number (MBIxxxx portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2);
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match MBI[156]xxx - the core part number
        Pattern basePattern = Pattern.compile("^(MBI[156][0-9]{3}).*$");
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
     * @param series the series code (e.g., "MBI5", "MBI6")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Get the channel count for known LED drivers.
     *
     * @param mpn the manufacturer part number
     * @return number of channels or -1 if unknown
     */
    public int getChannelCount(String mpn) {
        if (mpn == null) return -1;

        String upperMpn = mpn.toUpperCase();

        // Well-known parts with specific channel counts
        if (upperMpn.startsWith("MBI5024")) return 16;
        if (upperMpn.startsWith("MBI5039")) return 16;
        if (upperMpn.startsWith("MBI5040")) return 16;
        if (upperMpn.startsWith("MBI5041")) return 16;
        if (upperMpn.startsWith("MBI5042")) return 16;
        if (upperMpn.startsWith("MBI5050")) return 16;
        if (upperMpn.startsWith("MBI5124")) return 16;
        if (upperMpn.startsWith("MBI5153")) return 48;
        if (upperMpn.startsWith("MBI5252")) return 48;
        if (upperMpn.startsWith("MBI5353")) return 48;

        return -1;
    }

    /**
     * Determine if an MPN is a constant current LED driver.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a constant current driver (MBI5xxx series)
     */
    public boolean isConstantCurrentDriver(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^MBI5[0-9]{3}.*");
    }

    /**
     * Determine if an MPN is a DC-DC LED driver.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a DC-DC driver (MBI6xxx series)
     */
    public boolean isDCDCDriver(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^MBI6[0-9]{3}.*");
    }

    /**
     * Determine if an MPN is a scan driver.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a scan driver (MBI1xxx series)
     */
    public boolean isScanDriver(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^MBI1[0-9]{3}.*");
    }
}
