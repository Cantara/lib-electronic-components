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
 * Handler for Chipone Technology components.
 * <p>
 * Chipone Technology specializes in LED display driver ICs:
 * - ICN2xxx: Constant current LED drivers (ICN2024, ICN2038, ICN2053, ICN2065)
 * - ICN20xx: LED display drivers (ICN2012, ICN2018, ICN2026)
 * - ICND2xxx: Improved series LED drivers (ICND2025, ICND2053, ICND2110)
 * <p>
 * Pattern examples: ICN2024, ICN2053SS, ICND2110-S, ICN2065B
 */
public class ChiponeHandler implements ManufacturerHandler {

    // Package code mappings for Chipone components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Common Chipone package suffixes
        PACKAGE_CODES.put("SOP", "SOP");
        PACKAGE_CODES.put("SS", "SSOP");
        PACKAGE_CODES.put("SSOP", "SSOP");
        PACKAGE_CODES.put("QFN", "QFN");
        PACKAGE_CODES.put("S", "SOP");
        PACKAGE_CODES.put("Q", "QFN");
        PACKAGE_CODES.put("T", "TSSOP");
        PACKAGE_CODES.put("TSSOP", "TSSOP");
    }

    // Series descriptions
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("ICN2", "Constant Current LED Drivers");
        SERIES_INFO.put("ICN20", "LED Display Drivers");
        SERIES_INFO.put("ICND2", "Improved LED Drivers");
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
        // ICN2xxx - Constant current LED drivers (most common series)
        // Examples: ICN2024, ICN2038, ICN2053, ICN2065
        registry.addPattern(ComponentType.LED_DRIVER, "^ICN2[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^ICN2[0-9]{3}[A-Z0-9-]*$");

        // ICN20xx - LED display drivers subset
        // Examples: ICN2012, ICN2018, ICN2026
        registry.addPattern(ComponentType.LED_DRIVER, "^ICN20[0-9]{2}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^ICN20[0-9]{2}[A-Z0-9-]*$");

        // ICND2xxx - Improved series LED drivers
        // Examples: ICND2025, ICND2053, ICND2110
        registry.addPattern(ComponentType.LED_DRIVER, "^ICND2[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^ICND2[0-9]{3}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Chipone MPNs (start with ICN or ICND)
        if (!upperMpn.startsWith("ICN") && !upperMpn.startsWith("ICND")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case LED_DRIVER:
                return isLEDDriver(upperMpn);
            case IC:
                return isChiponePart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isChiponePart(String mpn) {
        // Match ICN2xxx and ICND2xxx patterns
        return mpn.matches("^ICN2[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^ICND2[0-9]{3}[A-Z0-9-]*$");
    }

    private boolean isLEDDriver(String mpn) {
        // All Chipone ICN2xxx and ICND2xxx are LED drivers
        return isChiponePart(mpn);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove trailing suffixes like -S, -TR (tape/reel)
        String baseMpn = upperMpn.replaceAll("-TR$", "");

        // Check for package suffix after hyphen (e.g., ICND2110-S)
        int hyphenIdx = baseMpn.lastIndexOf('-');
        if (hyphenIdx > 0 && hyphenIdx < baseMpn.length() - 1) {
            String suffix = baseMpn.substring(hyphenIdx + 1);
            String mapped = PACKAGE_CODES.get(suffix);
            if (mapped != null) {
                return mapped;
            }
            // Return the suffix as-is if not in mapping
            return suffix;
        }

        // Check for inline package code at the end (e.g., ICN2053SS)
        // Pattern: ICN[D]?2xxx followed by package letters
        Pattern packagePattern = Pattern.compile("^ICND?2[0-9]{3}([A-Z]+)$");
        java.util.regex.Matcher matcher = packagePattern.matcher(baseMpn);

        if (matcher.matches()) {
            String suffix = matcher.group(1);
            // Skip single letter variants like 'B' (version indicator)
            if (suffix.length() == 1 && suffix.matches("[A-EG-Z]")) {
                return ""; // Single letters like B, C are variants, not packages
            }
            return PACKAGE_CODES.getOrDefault(suffix, suffix);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // ICND2xxx series
        if (upperMpn.startsWith("ICND2") && upperMpn.length() >= 8) {
            return "ICND2";
        }

        // ICN2xxx series
        if (upperMpn.startsWith("ICN2") && upperMpn.length() >= 7) {
            return "ICN2";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Check for known replacements first: ICND2xxx are improved versions of ICN2xxx
        // e.g., ICND2053 is a drop-in replacement for ICN2053
        if (upper1.startsWith("ICND2") && upper2.startsWith("ICN2") &&
            !upper2.startsWith("ICND")) {
            String num1 = extractPartNumber(upper1);
            String num2 = extractPartNumber(upper2);
            if (num1.equals(num2)) {
                return true;
            }
        }
        if (upper2.startsWith("ICND2") && upper1.startsWith("ICN2") &&
            !upper1.startsWith("ICND")) {
            String num1 = extractPartNumber(upper1);
            String num2 = extractPartNumber(upper2);
            if (num1.equals(num2)) {
                return true;
            }
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be in the same series for same-part replacements
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract the base part number
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2);
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match ICN[D]?2xxx - the core part number
        Pattern basePattern = Pattern.compile("^(ICND?2[0-9]{3}).*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    private String extractPartNumber(String mpn) {
        // Extract just the numeric part (2xxx) from ICN2xxx or ICND2xxx
        if (mpn == null) return "";
        java.util.regex.Matcher matcher = Pattern.compile("ICND?(2[0-9]{3})").matcher(mpn);
        if (matcher.find()) {
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
     * @param series the series code (e.g., "ICN2", "ICND2")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Get the number of output channels for a given part number.
     * Common Chipone LED drivers: ICN2024 = 24ch, ICN2053 = 16ch + S-PWM
     *
     * @param mpn the manufacturer part number
     * @return number of channels, or -1 if unknown
     */
    public int getChannelCount(String mpn) {
        if (mpn == null) return -1;
        String upper = mpn.toUpperCase();

        // ICN2024, ICND2024 = 24 channels
        if (upper.contains("2024")) return 24;
        // ICN2012 = 12 channels
        if (upper.contains("2012")) return 12;
        // ICN2018 = 18 channels
        if (upper.contains("2018")) return 18;
        // ICN2026 = 26 channels
        if (upper.contains("2026")) return 26;
        // ICN2038 = 38 channels
        if (upper.contains("2038")) return 38;
        // ICN2053, ICND2053 = 16 channels (with S-PWM)
        if (upper.contains("2053")) return 16;
        // ICN2065 = 16 channels (high refresh)
        if (upper.contains("2065")) return 16;
        // ICND2110 = 16 channels (improved)
        if (upper.contains("2110")) return 16;

        return -1;
    }
}
