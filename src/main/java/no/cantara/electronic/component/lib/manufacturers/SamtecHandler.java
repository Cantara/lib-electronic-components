package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Samtec high-speed connectors.
 * Samtec specializes in high-performance interconnect solutions including:
 * - LSHM series (high-speed micro headers)
 * - SEAM series (card edge connectors)
 * - HSEC8 series (high-speed edge card)
 * - QSH/QTH series (high-speed terminal strips)
 * - TFM/TSM series (terminal strips)
 * - SSW/TSW series (through-hole headers)
 *
 * MPN format examples:
 * - LSHM-110-02.5-L-DV-A-S-K-TR
 * - SEAM-50-03.0-L-10-2-A-K
 * - HSEC8-120-01-L-DV-A
 * - QSH-030-01-L-D-A
 * - QTH-030-01-L-D-A
 * - TFM-110-01-L-D
 * - TSM-110-01-L-DV
 * - SSW-110-01-L-S
 * - TSW-110-01-L-S
 */
public class SamtecHandler implements ManufacturerHandler {

    // Series patterns
    private static final Pattern LSHM_PATTERN = Pattern.compile("LSHM-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern SEAM_PATTERN = Pattern.compile("SEAM-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern HSEC8_PATTERN = Pattern.compile("HSEC8-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern QSH_PATTERN = Pattern.compile("QSH-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern QTH_PATTERN = Pattern.compile("QTH-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern TFM_PATTERN = Pattern.compile("TFM-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern TSM_PATTERN = Pattern.compile("TSM-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern SSW_PATTERN = Pattern.compile("SSW-([0-9]+)-([0-9.]+)-([A-Z])-?.*");
    private static final Pattern TSW_PATTERN = Pattern.compile("TSW-([0-9]+)-([0-9.]+)-([A-Z])-?.*");

    // Series families for documentation
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("LSHM", "High-Speed Micro Headers"),
            new SimpleEntry<>("SEAM", "Card Edge Connectors"),
            new SimpleEntry<>("HSEC8", "High-Speed Edge Card"),
            new SimpleEntry<>("QSH", "High-Speed Terminal Strip (Socket)"),
            new SimpleEntry<>("QTH", "High-Speed Terminal Strip (Header)"),
            new SimpleEntry<>("TFM", "Terminal Strip (Female)"),
            new SimpleEntry<>("TSM", "Tiger Eye Terminal Strip"),
            new SimpleEntry<>("SSW", "Through-Hole Socket Strip"),
            new SimpleEntry<>("TSW", "Through-Hole Terminal Strip")
    );

    // Series to pitch mapping (in mm)
    private static final Map<String, String> SERIES_DEFAULT_PITCH = Map.ofEntries(
            new SimpleEntry<>("LSHM", "0.50"),
            new SimpleEntry<>("SEAM", "1.27"),
            new SimpleEntry<>("HSEC8", "0.80"),
            new SimpleEntry<>("QSH", "0.635"),
            new SimpleEntry<>("QTH", "0.635"),
            new SimpleEntry<>("TFM", "1.27"),
            new SimpleEntry<>("TSM", "1.27"),
            new SimpleEntry<>("SSW", "2.54"),
            new SimpleEntry<>("TSW", "2.54")
    );

    // Series to rated current mapping (in Amperes)
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            new SimpleEntry<>("LSHM", 1.7),
            new SimpleEntry<>("SEAM", 1.5),
            new SimpleEntry<>("HSEC8", 1.8),
            new SimpleEntry<>("QSH", 2.3),
            new SimpleEntry<>("QTH", 2.3),
            new SimpleEntry<>("TFM", 2.1),
            new SimpleEntry<>("TSM", 2.3),
            new SimpleEntry<>("SSW", 3.0),
            new SimpleEntry<>("TSW", 3.0)
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
        // LSHM Series (High-Speed Micro Headers)
        registry.addPattern(ComponentType.CONNECTOR, "^LSHM-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^LSHM-[0-9]+-.*");

        // SEAM Series (Card Edge)
        registry.addPattern(ComponentType.CONNECTOR, "^SEAM-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^SEAM-[0-9]+-.*");

        // HSEC8 Series (High-Speed Edge Card)
        registry.addPattern(ComponentType.CONNECTOR, "^HSEC8-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^HSEC8-[0-9]+-.*");

        // QSH Series (High-Speed Terminal Strip Socket)
        registry.addPattern(ComponentType.CONNECTOR, "^QSH-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^QSH-[0-9]+-.*");

        // QTH Series (High-Speed Terminal Strip Header)
        registry.addPattern(ComponentType.CONNECTOR, "^QTH-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^QTH-[0-9]+-.*");

        // TFM Series (Terminal Strip Female)
        registry.addPattern(ComponentType.CONNECTOR, "^TFM-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^TFM-[0-9]+-.*");

        // TSM Series (Tiger Eye Terminal Strip)
        registry.addPattern(ComponentType.CONNECTOR, "^TSM-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^TSM-[0-9]+-.*");

        // SSW Series (Through-Hole Socket Strip)
        registry.addPattern(ComponentType.CONNECTOR, "^SSW-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^SSW-[0-9]+-.*");

        // TSW Series (Through-Hole Terminal Strip)
        registry.addPattern(ComponentType.CONNECTOR, "^TSW-[0-9]+-.*");
        registry.addPattern(ComponentType.IC, "^TSW-[0-9]+-.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Samtec uses mounting type indicators in the MPN
        // Common patterns: -L- (low profile), -S (single row), -D (dual row), -DV (dual vertical)
        String upperMpn = mpn.toUpperCase();

        // Extract the portion after pin count and pitch
        // Format: SERIES-PINS-PITCH-MOUNT-OPTIONS
        String[] parts = upperMpn.split("-");
        if (parts.length >= 4) {
            // Return mounting type and subsequent options
            StringBuilder pkgCode = new StringBuilder();
            for (int i = 3; i < parts.length; i++) {
                if (pkgCode.length() > 0) pkgCode.append("-");
                pkgCode.append(parts[i]);
            }
            return pkgCode.toString();
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check each known series
        for (String series : SERIES_FAMILIES.keySet()) {
            if (upperMpn.startsWith(series + "-") || upperMpn.startsWith(series)) {
                return series;
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract pin counts
        int pins1 = extractPinCount(mpn1);
        int pins2 = extractPinCount(mpn2);

        // Must have same pin count
        if (pins1 != pins2 || pins1 == 0) {
            return false;
        }

        // Check if they have compatible mounting types
        return areCompatibleMountingTypes(mpn1, mpn2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    /**
     * Extract pin count from MPN.
     * Samtec format: SERIES-PINS-PITCH-OPTIONS
     * e.g., LSHM-110-02.5-L-DV-A = 110 pins
     */
    public int extractPinCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        Pattern[] patterns = {
            LSHM_PATTERN, SEAM_PATTERN, HSEC8_PATTERN,
            QSH_PATTERN, QTH_PATTERN,
            TFM_PATTERN, TSM_PATTERN,
            SSW_PATTERN, TSW_PATTERN
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(mpn.toUpperCase());
            if (matcher.matches()) {
                try {
                    return Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }

        // Generic extraction for series-pins-pitch format
        String[] parts = mpn.toUpperCase().split("-");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    /**
     * Extract pitch from MPN (in mm).
     * Samtec format: SERIES-PINS-PITCH-OPTIONS
     */
    public String getPitch(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        Pattern[] patterns = {
            LSHM_PATTERN, SEAM_PATTERN, HSEC8_PATTERN,
            QSH_PATTERN, QTH_PATTERN,
            TFM_PATTERN, TSM_PATTERN,
            SSW_PATTERN, TSW_PATTERN
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(mpn.toUpperCase());
            if (matcher.matches()) {
                return matcher.group(2);
            }
        }

        // Fall back to series default pitch
        String series = extractSeries(mpn);
        return SERIES_DEFAULT_PITCH.getOrDefault(series, "");
    }

    /**
     * Get the mounting type (SMT or THT).
     */
    public String getMountingType(String mpn) {
        String series = extractSeries(mpn);

        // Through-hole series
        if ("SSW".equals(series) || "TSW".equals(series)) {
            return "THT";
        }

        // High-speed series are typically SMT
        if ("LSHM".equals(series) || "HSEC8".equals(series) ||
            "QSH".equals(series) || "QTH".equals(series)) {
            return "SMT";
        }

        // Card edge
        if ("SEAM".equals(series)) {
            return "Card Edge";
        }

        // TFM/TSM can be either, check package code
        String pkgCode = extractPackageCode(mpn);
        if (pkgCode.contains("S") && !pkgCode.contains("SMT")) {
            return "THT";
        }

        return "SMT";
    }

    /**
     * Get the connector orientation.
     */
    public String getOrientation(String mpn) {
        String pkgCode = extractPackageCode(mpn);

        if (pkgCode.contains("RA") || pkgCode.contains("R-")) {
            return "Right Angle";
        }

        return "Vertical";
    }

    /**
     * Get rated current per pin in Amperes.
     */
    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    /**
     * Get the number of rows.
     */
    public String getRowConfiguration(String mpn) {
        String pkgCode = extractPackageCode(mpn);

        if (pkgCode.contains("DV") || pkgCode.contains("D-") || pkgCode.startsWith("D")) {
            return "Dual Row";
        }
        if (pkgCode.contains("S-") || pkgCode.startsWith("S")) {
            return "Single Row";
        }

        // Default based on series
        String series = extractSeries(mpn);
        if ("QSH".equals(series) || "QTH".equals(series)) {
            return "Dual Row";
        }

        return "Single Row";
    }

    /**
     * Get the series description.
     */
    public String getSeriesDescription(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_FAMILIES.getOrDefault(series, "Unknown Series");
    }

    /**
     * Check if the connector is high-speed rated.
     */
    public boolean isHighSpeed(String mpn) {
        String series = extractSeries(mpn);
        return "LSHM".equals(series) || "HSEC8".equals(series) ||
               "QSH".equals(series) || "QTH".equals(series);
    }

    /**
     * Get application type based on series.
     */
    public String getApplicationType(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "LSHM" -> "High-Speed Board-to-Board";
            case "SEAM" -> "Card Edge";
            case "HSEC8" -> "High-Speed Edge Card";
            case "QSH", "QTH" -> "High-Speed Terminal";
            case "TFM", "TSM" -> "General Purpose Terminal Strip";
            case "SSW", "TSW" -> "Through-Hole Header/Socket";
            default -> "General Purpose";
        };
    }

    private boolean areCompatibleMountingTypes(String mpn1, String mpn2) {
        String mount1 = getMountingType(mpn1);
        String mount2 = getMountingType(mpn2);

        // Same mounting type is always compatible
        return mount1.equals(mount2);
    }
}
