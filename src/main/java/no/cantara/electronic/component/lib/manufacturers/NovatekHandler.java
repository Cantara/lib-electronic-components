package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Novatek Microelectronics components.
 * <p>
 * Novatek specializes in display driver ICs for LCD and AMOLED panels:
 * - NT35xxx: TFT LCD drivers (NT35510, NT35516, NT35596)
 * - NT36xxx: AMOLED drivers (NT36672, NT36675)
 * - NT39xxx: Advanced display controllers
 * - NTxxxxx: Touch controllers
 * <p>
 * Package types: COG (Chip-on-Glass), COF (Chip-on-Film), BGA, QFP
 * <p>
 * Pattern examples: NT35510H, NT36672A-DP, NT39016D-C02
 */
public class NovatekHandler implements ManufacturerHandler {

    // Package code mappings for Novatek components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Common Novatek package suffixes
        PACKAGE_CODES.put("COG", "Chip-on-Glass");
        PACKAGE_CODES.put("COF", "Chip-on-Film");
        PACKAGE_CODES.put("BGA", "BGA");
        PACKAGE_CODES.put("QFP", "QFP");
        PACKAGE_CODES.put("WLCSP", "WLCSP");
        PACKAGE_CODES.put("TFBGA", "Thin Fine-pitch BGA");
        PACKAGE_CODES.put("FBGA", "Fine-pitch BGA");
        // Single-letter package suffixes often found in Novatek MPNs
        PACKAGE_CODES.put("H", "COG");
        PACKAGE_CODES.put("C", "COF");
        PACKAGE_CODES.put("A", "AMOLED");
        PACKAGE_CODES.put("D", "Display Driver");
    }

    // Series descriptions for extractSeries documentation
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("NT35", "TFT LCD Drivers");
        SERIES_INFO.put("NT36", "AMOLED Drivers");
        SERIES_INFO.put("NT39", "Advanced Display Controllers");
        SERIES_INFO.put("NT50", "Timing Controllers");
        SERIES_INFO.put("NT51", "LED Backlight Drivers");
        SERIES_INFO.put("NT66", "Touch Controllers");
        SERIES_INFO.put("NT67", "Touch Controllers");
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
        // NT35xxx - TFT LCD drivers (e.g., NT35510, NT35516, NT35596)
        registry.addPattern(ComponentType.IC, "^NT35[0-9]{3}[A-Z0-9-]*$");

        // NT36xxx - AMOLED drivers (e.g., NT36672, NT36675)
        registry.addPattern(ComponentType.IC, "^NT36[0-9]{3}[A-Z0-9-]*$");

        // NT37xxx - Display drivers
        registry.addPattern(ComponentType.IC, "^NT37[0-9]{3}[A-Z0-9-]*$");

        // NT38xxx - Display drivers
        registry.addPattern(ComponentType.IC, "^NT38[0-9]{3}[A-Z0-9-]*$");

        // NT39xxx - Advanced display controllers
        registry.addPattern(ComponentType.IC, "^NT39[0-9]{3}[A-Z0-9-]*$");

        // NT50xxx - Timing controllers (TCON)
        registry.addPattern(ComponentType.IC, "^NT50[0-9]{3}[A-Z0-9-]*$");

        // NT51xxx - LED backlight drivers
        registry.addPattern(ComponentType.IC, "^NT51[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.LED_DRIVER, "^NT51[0-9]{3}[A-Z0-9-]*$");

        // NT66xxx - Touch controllers
        registry.addPattern(ComponentType.IC, "^NT66[0-9]{3}[A-Z0-9-]*$");

        // NT67xxx - Touch controllers
        registry.addPattern(ComponentType.IC, "^NT67[0-9]{3}[A-Z0-9-]*$");

        // General Novatek pattern for any NT followed by digits
        registry.addPattern(ComponentType.IC, "^NT[3-9][0-9]{4}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Novatek MPNs (start with NT and followed by digits)
        if (!upperMpn.matches("^NT[0-9].*")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case IC:
                return isNovatekPart(upperMpn);
            case LED_DRIVER:
                return isLEDDriver(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isNovatekPart(String mpn) {
        // Match NT3[5-9]xxx, NT50xxx, NT51xxx, NT66xxx, NT67xxx
        return mpn.matches("^NT3[5-9][0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^NT50[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^NT51[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^NT66[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^NT67[0-9]{3}[A-Z0-9-]*$");
    }

    private boolean isLEDDriver(String mpn) {
        // NT51xxx are LED backlight drivers
        return mpn.matches("^NT51[0-9]{3}[A-Z0-9-]*$");
    }

    /**
     * Determine if an MPN is a TFT LCD driver.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a TFT LCD driver (NT35xxx series)
     */
    public boolean isTFTLCDDriver(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^NT35[0-9]{3}[A-Z0-9-]*$");
    }

    /**
     * Determine if an MPN is an AMOLED driver.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an AMOLED driver (NT36xxx series)
     */
    public boolean isAMOLEDDriver(String mpn) {
        if (mpn == null) return false;
        return mpn.toUpperCase().matches("^NT36[0-9]{3}[A-Z0-9-]*$");
    }

    /**
     * Determine if an MPN is a touch controller.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a touch controller (NT66xxx/NT67xxx series)
     */
    public boolean isTouchController(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^NT66[0-9]{3}[A-Z0-9-]*$") ||
               upper.matches("^NT67[0-9]{3}[A-Z0-9-]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for explicit package code after hyphen (e.g., NT35510-COG)
        int hyphenIndex = upperMpn.indexOf('-');
        if (hyphenIndex > 0 && hyphenIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(hyphenIndex + 1);
            // Extract first part if there are multiple hyphen-separated parts
            int nextHyphen = suffix.indexOf('-');
            if (nextHyphen > 0) {
                suffix = suffix.substring(0, nextHyphen);
            }
            String mappedPackage = PACKAGE_CODES.get(suffix);
            if (mappedPackage != null) {
                return mappedPackage;
            }
        }

        // Extract package code from suffix letters after part number
        // Pattern: NT[0-9]{5}[A-Z]...
        Pattern packagePattern = Pattern.compile("^NT[0-9]{5}([A-Z]).*$");
        Matcher matcher = packagePattern.matcher(upperMpn);

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

        // Extract the first 4 characters (NT + 2 digits) as series identifier
        if (upperMpn.matches("^NT[0-9]{2}.*")) {
            String series = upperMpn.substring(0, 4);
            return series;
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

        // Extract the base part number (NTxxxxx portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        if (base1.equals(base2)) {
            return true;
        }

        // Check for revision equivalents (same series, sequential numbers)
        // e.g., NT35510 and NT35511 might be compatible within same family
        if (areInSameFamily(mpn1, mpn2)) {
            return true;
        }

        return false;
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match NTxxxxx - the core part number (5 digits after NT)
        Pattern basePattern = Pattern.compile("^(NT[0-9]{5}).*$");
        Matcher matcher = basePattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    private boolean areInSameFamily(String mpn1, String mpn2) {
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        if (base1.isEmpty() || base2.isEmpty()) {
            return false;
        }

        // Check if they differ only in the last digit (revision variants)
        if (base1.length() == 7 && base2.length() == 7) {
            String prefix1 = base1.substring(0, 6);
            String prefix2 = base2.substring(0, 6);
            return prefix1.equals(prefix2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get a description for the given series.
     *
     * @param series the series code (e.g., "NT35", "NT36")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine the display technology type for this driver.
     *
     * @param mpn the manufacturer part number
     * @return the display technology type (TFT, AMOLED, OLED, etc.) or empty string
     */
    public String getDisplayTechnology(String mpn) {
        if (mpn == null) return "";
        String upper = mpn.toUpperCase();

        if (upper.matches("^NT35[0-9]{3}.*")) {
            return "TFT LCD";
        } else if (upper.matches("^NT36[0-9]{3}.*")) {
            return "AMOLED";
        } else if (upper.matches("^NT37[0-9]{3}.*") || upper.matches("^NT38[0-9]{3}.*")) {
            return "OLED";
        } else if (upper.matches("^NT39[0-9]{3}.*")) {
            return "Advanced Display";
        } else if (upper.matches("^NT50[0-9]{3}.*")) {
            return "Timing Controller";
        } else if (upper.matches("^NT51[0-9]{3}.*")) {
            return "LED Backlight";
        } else if (upper.matches("^NT66[0-9]{3}.*") || upper.matches("^NT67[0-9]{3}.*")) {
            return "Touch Controller";
        }

        return "";
    }
}
