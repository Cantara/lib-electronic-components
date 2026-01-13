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
 * Handler for Sitronix Technology Corporation components.
 * <p>
 * Sitronix specializes in display controllers and LED drivers including:
 * - ST75xx: TFT LCD controllers (ST7565, ST7567, ST7571, ST7580)
 * - ST77xx: TFT display drivers (ST7735, ST7789, ST7796)
 * - ST7920: Graphic LCD controller
 * - ST16xx: LED drivers (ST1628, ST1633)
 * <p>
 * Pattern examples: ST7735S, ST7789V, ST7920-0B, ST1628
 */
public class SitronixHandler implements ManufacturerHandler {

    // Package code mappings for Sitronix components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Common Sitronix package suffixes
        PACKAGE_CODES.put("QFP", "QFP");
        PACKAGE_CODES.put("LQFP", "LQFP");
        PACKAGE_CODES.put("COG", "COG");      // Chip-on-Glass
        PACKAGE_CODES.put("COF", "COF");      // Chip-on-Film
        PACKAGE_CODES.put("QFN", "QFN");
        PACKAGE_CODES.put("SOP", "SOP");
        PACKAGE_CODES.put("SSOP", "SSOP");
        PACKAGE_CODES.put("TSSOP", "TSSOP");
        PACKAGE_CODES.put("TQFP", "TQFP");
        PACKAGE_CODES.put("BGA", "BGA");
    }

    // Series descriptions
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("ST75", "TFT LCD Controllers");
        SERIES_INFO.put("ST77", "TFT Display Drivers");
        SERIES_INFO.put("ST79", "Graphic LCD Controllers");
        SERIES_INFO.put("ST16", "LED Drivers");
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
        // ST75xx - TFT LCD controllers (ST7565, ST7567, ST7571, ST7580)
        registry.addPattern(ComponentType.IC, "^ST75[0-9]{2}[A-Z0-9-]*$");

        // ST77xx - TFT display drivers (ST7735, ST7789, ST7796)
        registry.addPattern(ComponentType.IC, "^ST77[0-9]{2}[A-Z0-9-]*$");

        // ST7920 - Graphic LCD controller
        registry.addPattern(ComponentType.IC, "^ST79[0-9]{2}[A-Z0-9-]*$");

        // ST16xx - LED drivers (ST1628, ST1633)
        registry.addPattern(ComponentType.LED_DRIVER, "^ST16[0-9]{2}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^ST16[0-9]{2}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Sitronix MPNs (start with ST followed by digits)
        if (!isSitronixPart(upperMpn)) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case LED_DRIVER:
                return isLEDDriver(upperMpn);
            case IC:
                return isSitronixDisplayController(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Check if MPN is a Sitronix part (ST followed by 2 digits for display/LED products).
     */
    private boolean isSitronixPart(String mpn) {
        // Sitronix display controllers: ST75xx, ST77xx, ST79xx
        // Sitronix LED drivers: ST16xx
        return mpn.matches("^ST(75|77|79|16)[0-9]{2}[A-Z0-9-]*$");
    }

    /**
     * Check if MPN is a Sitronix display controller or LED driver IC.
     */
    private boolean isSitronixDisplayController(String mpn) {
        return mpn.matches("^ST(75|77|79|16)[0-9]{2}[A-Z0-9-]*$");
    }

    /**
     * Check if MPN is an LED driver (ST16xx series).
     */
    private boolean isLEDDriver(String mpn) {
        return mpn.matches("^ST16[0-9]{2}[A-Z0-9-]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove hyphenated suffixes first
        int hyphenIdx = upperMpn.indexOf('-');
        String basePart = hyphenIdx >= 0 ? upperMpn.substring(0, hyphenIdx) : upperMpn;

        // Extract package code - typically letter(s) after the part number
        // Pattern: ST[0-9]{4}[letter(s)]
        Pattern packagePattern = Pattern.compile("^ST[0-9]{4}([A-Z]+)$");
        java.util.regex.Matcher matcher = packagePattern.matcher(basePart);

        if (matcher.matches()) {
            String code = matcher.group(1);
            // Check for known package codes
            if (PACKAGE_CODES.containsKey(code)) {
                return PACKAGE_CODES.get(code);
            }
            // Map common single-letter suffixes
            switch (code) {
                case "S":
                    return "SOP";
                case "V":
                    return "LQFP";
                case "R":
                    return "QFN";
                case "B":
                    return "COG";
                default:
                    return code;
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // ST75xx series
        if (upperMpn.matches("^ST75[0-9]{2}.*")) {
            return "ST75";
        }

        // ST77xx series
        if (upperMpn.matches("^ST77[0-9]{2}.*")) {
            return "ST77";
        }

        // ST79xx series (includes ST7920)
        if (upperMpn.matches("^ST79[0-9]{2}.*")) {
            return "ST79";
        }

        // ST16xx series (LED drivers)
        if (upperMpn.matches("^ST16[0-9]{2}.*")) {
            return "ST16";
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

        // Extract the base part number (ST[0-9]{4} portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2);
    }

    /**
     * Extract the base part number (ST[0-9]{4}).
     */
    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match ST[0-9]{4} - the core part number
        Pattern basePattern = Pattern.compile("^(ST[0-9]{4}).*$");
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
     * @param series the series code (e.g., "ST75", "ST77")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Check if an MPN is a TFT LCD controller (ST75xx series).
     *
     * @param mpn the manufacturer part number
     * @return true if this is a TFT LCD controller
     */
    public boolean isTFTLCDController(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^ST75[0-9]{2}[A-Z0-9-]*$");
    }

    /**
     * Check if an MPN is a TFT display driver (ST77xx series).
     *
     * @param mpn the manufacturer part number
     * @return true if this is a TFT display driver
     */
    public boolean isTFTDisplayDriver(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^ST77[0-9]{2}[A-Z0-9-]*$");
    }

    /**
     * Check if an MPN is a graphic LCD controller (ST79xx series like ST7920).
     *
     * @param mpn the manufacturer part number
     * @return true if this is a graphic LCD controller
     */
    public boolean isGraphicLCDController(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^ST79[0-9]{2}[A-Z0-9-]*$");
    }
}
