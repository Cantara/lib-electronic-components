package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Harting industrial connectors.
 *
 * Supported product families:
 * - Han series - Industrial connectors:
 *   - Han A/Han E - Standard industrial (09 20 xxx, 09 33 xxx)
 *   - Han B - High current (09 30 xxx)
 *   - Han D - Compact (09 21 xxx)
 *   - Han K - Power connectors
 *   - Han-Modular - Modular system
 *   - Han-Yellock - Push-pull circular
 * - har-flex - Flexible PCB connectors
 * - M12 - Circular connectors (21 03 xxx)
 * - M8 - Miniature circular (21 02 xxx)
 * - har-bus HM - High-speed backplane (02 00 xxx)
 * - har-link - High-speed data (09 01 xxx)
 *
 * Pattern examples:
 * - 09 20 010 2611 (Han A/E, 10-pin)
 * - 09 33 024 2701 (Han E, 24-pin)
 * - 09 30 016 0301 (Han B, 16-pin, high current)
 * - 09 21 006 3101 (Han D, 6-pin, compact)
 * - 21 03 311 1305 (M12, 3-pin, circular)
 * - 21 02 151 0405 (M8, miniature circular)
 * - 02 00 120 1101 (har-bus HM backplane)
 * - 09 01 000 6231 (har-link high-speed)
 *
 * Harting MPN structure:
 * - First 2 digits: Product family/series
 * - Next 2 digits: Sub-series or variant
 * - Next 3 digits: Pin count or position code
 * - Last 4 digits: Configuration code (plating, orientation, etc.)
 */
public class HartingHandler implements ManufacturerHandler {

    // Han A/Han E series: 09 20 xxx xxxx or 09 33 xxx xxxx
    private static final Pattern HAN_A_E_PATTERN = Pattern.compile(
            "^09[- ]?(20|33)[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // Han B series (high current): 09 30 xxx xxxx
    private static final Pattern HAN_B_PATTERN = Pattern.compile(
            "^09[- ]?30[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // Han D series (compact): 09 21 xxx xxxx
    private static final Pattern HAN_D_PATTERN = Pattern.compile(
            "^09[- ]?21[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // Han K series (power): 09 38 xxx xxxx or 09 16 xxx xxxx
    private static final Pattern HAN_K_PATTERN = Pattern.compile(
            "^09[- ]?(38|16)[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // Han-Modular series: 09 14 xxx xxxx
    private static final Pattern HAN_MODULAR_PATTERN = Pattern.compile(
            "^09[- ]?14[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // Han-Yellock (push-pull circular): 09 02 xxx xxxx
    private static final Pattern HAN_YELLOCK_PATTERN = Pattern.compile(
            "^09[- ]?02[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // har-flex (flexible PCB): 14 xxx xxxx or 15 xxx xxxx
    private static final Pattern HAR_FLEX_PATTERN = Pattern.compile(
            "^(14|15)[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // M12 circular connectors: 21 03 xxx xxxx
    private static final Pattern M12_PATTERN = Pattern.compile(
            "^21[- ]?03[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // M8 miniature circular: 21 02 xxx xxxx
    private static final Pattern M8_PATTERN = Pattern.compile(
            "^21[- ]?02[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // har-bus HM (high-speed backplane): 02 00 xxx xxxx or 02 01 xxx xxxx
    private static final Pattern HAR_BUS_HM_PATTERN = Pattern.compile(
            "^02[- ]?(00|01)[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // har-link (high-speed data): 09 01 xxx xxxx
    private static final Pattern HAR_LINK_PATTERN = Pattern.compile(
            "^09[- ]?01[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // Generic Harting pattern for any 11-digit format with spaces
    private static final Pattern GENERIC_HARTING_PATTERN = Pattern.compile(
            "^([0-9]{2})[- ]?([0-9]{2})[- ]?([0-9]{3})[- ]?([0-9]{4})$",
            Pattern.CASE_INSENSITIVE);

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            Map.entry("0920", "Han A/E"),
            Map.entry("0933", "Han E"),
            Map.entry("0930", "Han B"),
            Map.entry("0921", "Han D"),
            Map.entry("0938", "Han K"),
            Map.entry("0916", "Han K"),
            Map.entry("0914", "Han-Modular"),
            Map.entry("0902", "Han-Yellock"),
            Map.entry("0901", "har-link"),
            Map.entry("14", "har-flex"),
            Map.entry("15", "har-flex"),
            Map.entry("2103", "M12"),
            Map.entry("2102", "M8"),
            Map.entry("0200", "har-bus HM"),
            Map.entry("0201", "har-bus HM")
    );

    // Series to rated current mapping (in Amperes) - typical values
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            Map.entry("0920", 16.0),   // Han A/E standard
            Map.entry("0933", 16.0),   // Han E
            Map.entry("0930", 40.0),   // Han B high current
            Map.entry("0921", 10.0),   // Han D compact
            Map.entry("0938", 200.0),  // Han K power
            Map.entry("0916", 200.0),  // Han K power
            Map.entry("0914", 10.0),   // Han-Modular
            Map.entry("0902", 10.0),   // Han-Yellock
            Map.entry("0901", 3.0),    // har-link
            Map.entry("14", 1.5),      // har-flex
            Map.entry("15", 1.5),      // har-flex
            Map.entry("2103", 4.0),    // M12
            Map.entry("2102", 3.0),    // M8
            Map.entry("0200", 1.0),    // har-bus HM
            Map.entry("0201", 1.0)     // har-bus HM
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.CONNECTOR,
                ComponentType.IC
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Han A/E series (standard industrial)
        registry.addPattern(ComponentType.CONNECTOR, "^09[- ]?(20|33)[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // Han B series (high current)
        registry.addPattern(ComponentType.CONNECTOR, "^09[- ]?30[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // Han D series (compact)
        registry.addPattern(ComponentType.CONNECTOR, "^09[- ]?21[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // Han K series (power)
        registry.addPattern(ComponentType.CONNECTOR, "^09[- ]?(38|16)[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // Han-Modular series
        registry.addPattern(ComponentType.CONNECTOR, "^09[- ]?14[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // Han-Yellock (push-pull circular)
        registry.addPattern(ComponentType.CONNECTOR, "^09[- ]?02[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // har-flex (flexible PCB)
        registry.addPattern(ComponentType.CONNECTOR, "^(14|15)[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // M12 circular connectors
        registry.addPattern(ComponentType.CONNECTOR, "^21[- ]?03[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // M8 miniature circular
        registry.addPattern(ComponentType.CONNECTOR, "^21[- ]?02[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // har-bus HM (high-speed backplane)
        registry.addPattern(ComponentType.CONNECTOR, "^02[- ]?(00|01)[- ]?[0-9]{3}[- ]?[0-9]{4}$");

        // har-link (high-speed data)
        registry.addPattern(ComponentType.CONNECTOR, "^09[- ]?01[- ]?[0-9]{3}[- ]?[0-9]{4}$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        if (type == ComponentType.CONNECTOR || type == ComponentType.IC) {
            String normalizedMpn = normalizeMpn(mpn);

            // Check each series pattern directly for explicit matching
            if (HAN_A_E_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAN_B_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAN_D_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAN_K_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAN_MODULAR_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAN_YELLOCK_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAR_FLEX_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (M12_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (M8_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAR_BUS_HM_PATTERN.matcher(normalizedMpn).matches()) return true;
            if (HAR_LINK_PATTERN.matcher(normalizedMpn).matches()) return true;
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String normalizedMpn = normalizeMpn(mpn);

        // Extract the last 4 digits (configuration code)
        Matcher matcher = GENERIC_HARTING_PATTERN.matcher(normalizedMpn);
        if (matcher.matches()) {
            return matcher.group(4);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String normalizedMpn = normalizeMpn(mpn);

        // Check each pattern and return appropriate series name
        if (HAN_A_E_PATTERN.matcher(normalizedMpn).matches()) {
            Matcher m = HAN_A_E_PATTERN.matcher(normalizedMpn);
            if (m.matches()) {
                String subSeries = m.group(1);
                return "20".equals(subSeries) ? "Han A" : "Han E";
            }
        }
        if (HAN_B_PATTERN.matcher(normalizedMpn).matches()) return "Han B";
        if (HAN_D_PATTERN.matcher(normalizedMpn).matches()) return "Han D";
        if (HAN_K_PATTERN.matcher(normalizedMpn).matches()) return "Han K";
        if (HAN_MODULAR_PATTERN.matcher(normalizedMpn).matches()) return "Han-Modular";
        if (HAN_YELLOCK_PATTERN.matcher(normalizedMpn).matches()) return "Han-Yellock";
        if (HAR_FLEX_PATTERN.matcher(normalizedMpn).matches()) return "har-flex";
        if (M12_PATTERN.matcher(normalizedMpn).matches()) return "M12";
        if (M8_PATTERN.matcher(normalizedMpn).matches()) return "M8";
        if (HAR_BUS_HM_PATTERN.matcher(normalizedMpn).matches()) return "har-bus HM";
        if (HAR_LINK_PATTERN.matcher(normalizedMpn).matches()) return "har-link";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from the same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract pin counts
        int pins1 = extractPinCount(mpn1);
        int pins2 = extractPinCount(mpn2);

        // Must have same pin count (if extractable)
        if (pins1 != pins2) {
            // If we can't extract pin count from both, check if position codes match
            if (pins1 == 0 && pins2 == 0) {
                String pos1 = extractPositionCode(mpn1);
                String pos2 = extractPositionCode(mpn2);
                if (!pos1.isEmpty() && !pos2.isEmpty() && !pos1.equals(pos2)) {
                    return false;
                }
            } else {
                return false;
            }
        }

        // Same series, same pin count/position = compatible
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    /**
     * Normalize MPN by removing spaces and converting to uppercase.
     */
    private String normalizeMpn(String mpn) {
        return mpn.toUpperCase().trim();
    }

    /**
     * Extract pin count from MPN.
     * The third group of 3 digits often encodes the pin count.
     */
    public int extractPinCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String normalizedMpn = normalizeMpn(mpn);
        Matcher matcher = GENERIC_HARTING_PATTERN.matcher(normalizedMpn);
        if (matcher.matches()) {
            String positionCode = matcher.group(3);
            try {
                // First digit or first two digits often indicate pin count
                int code = Integer.parseInt(positionCode);
                // Common mappings: 010 = 10 pins, 024 = 24 pins, 003 = 3 pins
                if (code < 100) {
                    return code;
                }
                // For 3-digit codes, first two digits might be pin count
                return code / 10;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * Extract position/variant code (3rd group of 3 digits).
     */
    public String extractPositionCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String normalizedMpn = normalizeMpn(mpn);
        Matcher matcher = GENERIC_HARTING_PATTERN.matcher(normalizedMpn);
        if (matcher.matches()) {
            return matcher.group(3);
        }
        return "";
    }

    /**
     * Get the series code (first 4 digits without spaces).
     */
    public String getSeriesCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String normalizedMpn = normalizeMpn(mpn);
        Matcher matcher = GENERIC_HARTING_PATTERN.matcher(normalizedMpn);
        if (matcher.matches()) {
            return matcher.group(1) + matcher.group(2);
        }
        return "";
    }

    /**
     * Get the connector family/type description.
     */
    public String getFamily(String mpn) {
        String seriesCode = getSeriesCode(mpn);
        return SERIES_FAMILIES.getOrDefault(seriesCode, "Industrial Connector");
    }

    /**
     * Get rated current for the series (in Amperes).
     */
    public double getRatedCurrent(String mpn) {
        String seriesCode = getSeriesCode(mpn);
        // Check for 4-digit series code first
        Double current = SERIES_CURRENT.get(seriesCode);
        if (current != null) {
            return current;
        }
        // Check for 2-digit prefix (har-flex)
        if (seriesCode.length() >= 2) {
            current = SERIES_CURRENT.get(seriesCode.substring(0, 2));
            if (current != null) {
                return current;
            }
        }
        return 0.0;
    }

    /**
     * Check if this is a Han series connector.
     */
    public boolean isHanSeries(String mpn) {
        String series = extractSeries(mpn);
        return series.startsWith("Han");
    }

    /**
     * Check if this is a circular connector (M8 or M12).
     */
    public boolean isCircularConnector(String mpn) {
        String series = extractSeries(mpn);
        return "M8".equals(series) || "M12".equals(series);
    }

    /**
     * Check if this is a high-speed data connector.
     */
    public boolean isHighSpeedConnector(String mpn) {
        String series = extractSeries(mpn);
        return "har-bus HM".equals(series) || "har-link".equals(series);
    }

    /**
     * Check if this is a power connector (high current).
     */
    public boolean isPowerConnector(String mpn) {
        String series = extractSeries(mpn);
        return "Han B".equals(series) || "Han K".equals(series);
    }

    /**
     * Get connector application type.
     */
    public String getApplicationType(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "Han A", "Han E" -> "Industrial Signal";
            case "Han B" -> "High Current Industrial";
            case "Han D" -> "Compact Industrial";
            case "Han K" -> "Power Distribution";
            case "Han-Modular" -> "Modular Industrial";
            case "Han-Yellock" -> "Push-Pull Circular";
            case "har-flex" -> "Flexible PCB";
            case "M12" -> "Industrial Sensor/Ethernet";
            case "M8" -> "Miniature Sensor";
            case "har-bus HM" -> "High-Speed Backplane";
            case "har-link" -> "High-Speed Data Link";
            default -> "Industrial Connector";
        };
    }

    /**
     * Get IP rating for series (typical).
     */
    public String getIPRating(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "Han A", "Han E", "Han B", "Han D", "Han K", "Han-Modular" -> "IP65";
            case "Han-Yellock" -> "IP67";
            case "M12", "M8" -> "IP67";
            case "har-bus HM", "har-link", "har-flex" -> "IP20";
            default -> "";
        };
    }

    /**
     * Check if this is a modular system connector.
     */
    public boolean isModularSystem(String mpn) {
        String series = extractSeries(mpn);
        return "Han-Modular".equals(series);
    }
}
