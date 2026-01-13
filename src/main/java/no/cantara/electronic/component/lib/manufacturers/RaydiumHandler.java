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
 * Handler for Raydium Semiconductor components.
 * <p>
 * Raydium specializes in touch and display controller ICs:
 * - RM68xxx: TFT LCD drivers (RM68120, RM68140, RM68172)
 * - RM69xxx: AMOLED display drivers (RM69032, RM69080, RM69091)
 * - RM31xxx: Touch controllers (RM31100, RM31080)
 * - RM35xxx: Touch + display integrated controllers
 * <p>
 * Package types:
 * - COG: Chip-On-Glass (direct bonding to glass substrate)
 * - COF: Chip-On-Film (flexible film bonding)
 * - BGA: Ball Grid Array
 * - QFN: Quad Flat No-leads
 * <p>
 * Pattern examples: RM68120, RM69091-COG, RM31100BGA
 */
public class RaydiumHandler implements ManufacturerHandler {

    // Package code mappings for Raydium components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        PACKAGE_CODES.put("COG", "COG");    // Chip-On-Glass
        PACKAGE_CODES.put("COF", "COF");    // Chip-On-Film
        PACKAGE_CODES.put("BGA", "BGA");    // Ball Grid Array
        PACKAGE_CODES.put("QFN", "QFN");    // Quad Flat No-leads
        PACKAGE_CODES.put("WLCSP", "WLCSP"); // Wafer Level Chip Scale Package
    }

    // Series descriptions
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("RM68", "TFT LCD Drivers");
        SERIES_INFO.put("RM69", "AMOLED Display Drivers");
        SERIES_INFO.put("RM31", "Touch Controllers");
        SERIES_INFO.put("RM35", "Touch + Display Controllers");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // RM68xxx - TFT LCD drivers
        registry.addPattern(ComponentType.IC, "^RM68[0-9]{3}[A-Z0-9-]*$");

        // RM69xxx - AMOLED display drivers
        registry.addPattern(ComponentType.IC, "^RM69[0-9]{3}[A-Z0-9-]*$");

        // RM31xxx - Touch controllers
        registry.addPattern(ComponentType.IC, "^RM31[0-9]{3}[A-Z0-9-]*$");

        // RM35xxx - Touch + display integrated controllers
        registry.addPattern(ComponentType.IC, "^RM35[0-9]{3}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Raydium MPNs (start with RM)
        if (!upperMpn.startsWith("RM")) {
            return false;
        }

        // Direct pattern check for IC type
        if (type == ComponentType.IC) {
            return isRaydiumPart(upperMpn);
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isRaydiumPart(String mpn) {
        // Match RM68xxx (TFT), RM69xxx (AMOLED), RM31xxx (Touch), RM35xxx (Touch+Display)
        return mpn.matches("^RM68[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^RM69[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^RM31[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^RM35[0-9]{3}[A-Z0-9-]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for hyphenated package code: RM69091-COG
        int hyphenIndex = upperMpn.indexOf('-');
        if (hyphenIndex > 0 && hyphenIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(hyphenIndex + 1);
            if (PACKAGE_CODES.containsKey(suffix)) {
                return PACKAGE_CODES.get(suffix);
            }
        }

        // Check for concatenated package code: RM31100BGA
        // Base part is RMxxyyy (7 chars), package follows
        if (upperMpn.length() > 7 && upperMpn.matches("^RM[0-9]{5}[A-Z]+.*$")) {
            String suffix = upperMpn.substring(7);
            // Check for known package codes at the start of suffix
            for (String pkg : PACKAGE_CODES.keySet()) {
                if (suffix.startsWith(pkg)) {
                    return PACKAGE_CODES.get(pkg);
                }
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract series: RM68, RM69, RM31, or RM35
        if (upperMpn.matches("^RM(68|69|31|35)[0-9]{3}.*")) {
            return upperMpn.substring(0, 4);  // Returns RM68, RM69, RM31, RM35
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be in the same series (same product family)
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract the base part number (RMxyyyy portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages are replacements
        if (base1.equals(base2)) {
            return true;
        }

        // Check for known compatible parts within the same series
        return areCompatibleParts(base1, base2, series1);
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match RMxyyyy - the core part number (2 letters + 5 digits)
        Pattern basePattern = Pattern.compile("^(RM[0-9]{5}).*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    private boolean areCompatibleParts(String base1, String base2, String series) {
        // Within the same series, parts with similar numbering may be compatible
        // For example, RM69080 and RM69091 are both AMOLED drivers but with different resolutions
        // We don't mark them as compatible since they support different panel sizes

        // Only same base parts are considered direct replacements
        return base1.equals(base2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get a description for the given series.
     *
     * @param series the series code (e.g., "RM68", "RM69")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine the product type for a given MPN.
     *
     * @param mpn the manufacturer part number
     * @return product type description (TFT Driver, AMOLED Driver, Touch Controller, or Touch+Display)
     */
    public String getProductType(String mpn) {
        if (mpn == null) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "RM68" -> "TFT LCD Driver";
            case "RM69" -> "AMOLED Display Driver";
            case "RM31" -> "Touch Controller";
            case "RM35" -> "Touch + Display Controller";
            default -> "";
        };
    }

    /**
     * Check if the MPN is a display driver (TFT or AMOLED).
     *
     * @param mpn the manufacturer part number
     * @return true if this is a display driver
     */
    public boolean isDisplayDriver(String mpn) {
        if (mpn == null) return false;
        String series = extractSeries(mpn);
        return "RM68".equals(series) || "RM69".equals(series) || "RM35".equals(series);
    }

    /**
     * Check if the MPN is a touch controller.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a touch controller
     */
    public boolean isTouchController(String mpn) {
        if (mpn == null) return false;
        String series = extractSeries(mpn);
        return "RM31".equals(series) || "RM35".equals(series);
    }
}
