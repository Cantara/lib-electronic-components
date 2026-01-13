package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Handler for JAE Electronics connectors.
 *
 * JAE (Japan Aviation Electronics Industry) is a leading connector manufacturer
 * known for high-quality FPC/FFC, board-to-board, circular, and automotive connectors.
 *
 * Supported series:
 * - FI: FPC/FFC connectors (FI-RE, FI-X, FI-W, FI-E, FI-S, FI-J series)
 * - DX: Board-to-board connectors (DX07 USB-C, DX40 high-speed)
 * - MX: Various connectors including automotive (MX34, MX44, MX77)
 * - IL: Circular connectors
 *
 * MPN format examples:
 * - FI-RE51S-HF-R1500: FI (FPC), RE (series), 51 (pin count), S (type), HF (option), R1500 (packaging)
 * - FI-X30HL-T: FI (FPC), X (series), 30 (pin count), HL (type), T (packaging)
 * - DX07S024JA1R1500: DX07 (USB-C), S (SMT), 024 (24 pins), JA (variant), 1 (option), R1500 (reel)
 * - MX34036NF1: MX34 (series), 036 (36 pins), NF (variant), 1 (option)
 */
public class JAEHandler implements ManufacturerHandler {

    // Common JAE patterns
    private static final Pattern FI_RE_PATTERN = Pattern.compile("FI-RE([0-9]+)([A-Z]+)-([A-Z0-9]+)(-R[0-9]+)?");
    private static final Pattern FI_X_PATTERN = Pattern.compile("FI-X([0-9]+)([A-Z]+)-([A-Z0-9]*)");
    private static final Pattern FI_W_PATTERN = Pattern.compile("FI-W([0-9]+)([A-Z]+)-([A-Z0-9]+)?");
    private static final Pattern FI_GENERIC_PATTERN = Pattern.compile("FI-([A-Z])([0-9]+)([A-Z0-9]*)(-.*)?");
    private static final Pattern DX07_PATTERN = Pattern.compile("DX07([A-Z])([0-9]{3})([A-Z0-9]+)(-R[0-9]+)?");
    private static final Pattern DX40_PATTERN = Pattern.compile("DX40-([0-9]+)([A-Z]+)-([A-Z0-9]+)?");
    private static final Pattern MX34_PATTERN = Pattern.compile("MX34([0-9]{3})([A-Z0-9]+)");
    private static final Pattern MX44_PATTERN = Pattern.compile("MX44([0-9]{3})([A-Z0-9]+)");
    private static final Pattern MX77_PATTERN = Pattern.compile("MX77([0-9]+)([A-Z0-9]+)");
    private static final Pattern IL_PATTERN = Pattern.compile("IL-([A-Z0-9]+)-([0-9]+)([A-Z0-9]+)?");

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("FI-RE", "FI-RE Series"),      // 0.5mm pitch, low profile FPC
            new SimpleEntry<>("FI-X", "FI-X Series"),        // 0.5mm pitch, FPC
            new SimpleEntry<>("FI-W", "FI-W Series"),        // 0.5mm pitch, high-current FPC
            new SimpleEntry<>("FI-E", "FI-E Series"),        // 0.5mm pitch, slim FPC
            new SimpleEntry<>("FI-S", "FI-S Series"),        // 1.0mm pitch, FPC
            new SimpleEntry<>("FI-J", "FI-J Series"),        // 1.0mm pitch, wire-to-board
            new SimpleEntry<>("DX07", "DX07 Series"),        // USB Type-C
            new SimpleEntry<>("DX40", "DX40 Series"),        // Board-to-board, high-speed
            new SimpleEntry<>("DX30", "DX30 Series"),        // Board-to-board
            new SimpleEntry<>("MX34", "MX34 Series"),        // Automotive waterproof
            new SimpleEntry<>("MX44", "MX44 Series"),        // Automotive miniature
            new SimpleEntry<>("MX77", "MX77 Series"),        // Automotive
            new SimpleEntry<>("IL", "IL Series")             // Circular
    );

    // Series to pitch mapping (in mm)
    private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
            new SimpleEntry<>("FI-RE", "0.50"),
            new SimpleEntry<>("FI-X", "0.50"),
            new SimpleEntry<>("FI-W", "0.50"),
            new SimpleEntry<>("FI-E", "0.50"),
            new SimpleEntry<>("FI-S", "1.00"),
            new SimpleEntry<>("FI-J", "1.00"),
            new SimpleEntry<>("DX07", "0.50"),  // USB Type-C has 0.5mm pitch
            new SimpleEntry<>("DX40", "0.40"),
            new SimpleEntry<>("DX30", "0.40"),
            new SimpleEntry<>("MX34", "2.20"),
            new SimpleEntry<>("MX44", "1.00"),
            new SimpleEntry<>("MX77", "2.20")
    );

    // Series to rated current mapping (in Amperes)
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            new SimpleEntry<>("FI-RE", 0.5),
            new SimpleEntry<>("FI-X", 0.5),
            new SimpleEntry<>("FI-W", 1.0),     // High-current version
            new SimpleEntry<>("FI-E", 0.5),
            new SimpleEntry<>("FI-S", 1.0),
            new SimpleEntry<>("FI-J", 1.0),
            new SimpleEntry<>("DX07", 5.0),     // USB-C can carry up to 5A for power
            new SimpleEntry<>("DX40", 0.5),
            new SimpleEntry<>("DX30", 0.5),
            new SimpleEntry<>("MX34", 5.0),     // Automotive high-current
            new SimpleEntry<>("MX44", 3.0),
            new SimpleEntry<>("MX77", 5.0)
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CONNECTOR,
            ComponentType.CONNECTOR_JAE
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // FI series (FPC/FFC connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^FI-RE[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-RE[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^FI-X[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-X[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^FI-W[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-W[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^FI-E[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-E[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^FI-S[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-S[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^FI-J[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-J[0-9]+.*");

        // DX series (board-to-board and USB)
        registry.addPattern(ComponentType.CONNECTOR, "^DX07[A-Z][0-9]{3}.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^DX07[A-Z][0-9]{3}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^DX40-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^DX40-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^DX30-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^DX30-[0-9]+.*");

        // MX series (automotive connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^MX34[0-9]{3}.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^MX34[0-9]{3}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^MX44[0-9]{3}.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^MX44[0-9]{3}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^MX77[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^MX77[0-9]+.*");

        // IL series (circular connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^IL-[A-Z0-9]+-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JAE, "^IL-[A-Z0-9]+-[0-9]+.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // FI-RE series: FI-RE51S-HF-R1500 -> HF (option code before reel)
        Matcher fiReMatcher = FI_RE_PATTERN.matcher(upperMpn);
        if (fiReMatcher.matches()) {
            return fiReMatcher.group(3);  // Return HF part
        }

        // FI-X series: FI-X30HL-T -> HL (type code)
        Matcher fiXMatcher = FI_X_PATTERN.matcher(upperMpn);
        if (fiXMatcher.matches()) {
            return fiXMatcher.group(2);  // Return HL part
        }

        // DX07 series: DX07S024JA1R1500 -> JA1 (variant and option)
        Matcher dx07Matcher = DX07_PATTERN.matcher(upperMpn);
        if (dx07Matcher.matches()) {
            String fullCode = dx07Matcher.group(3);
            // Remove trailing reel info
            if (fullCode.length() > 3) {
                return fullCode.substring(0, 3);
            }
            return fullCode;
        }

        // MX34 series: MX34036NF1 -> NF1 (variant code)
        Matcher mx34Matcher = MX34_PATTERN.matcher(upperMpn);
        if (mx34Matcher.matches()) {
            return mx34Matcher.group(2);
        }

        // MX44 series
        Matcher mx44Matcher = MX44_PATTERN.matcher(upperMpn);
        if (mx44Matcher.matches()) {
            return mx44Matcher.group(2);
        }

        // Generic: try to extract suffix after hyphen
        int lastHyphen = upperMpn.lastIndexOf('-');
        if (lastHyphen > 0 && lastHyphen < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(lastHyphen + 1);
            // Remove reel packaging codes (Rxxxx)
            if (suffix.startsWith("R") && suffix.length() > 1 && Character.isDigit(suffix.charAt(1))) {
                // This is a reel code, not a package code
                // Look for previous hyphen
                String beforeReel = upperMpn.substring(0, lastHyphen);
                int prevHyphen = beforeReel.lastIndexOf('-');
                if (prevHyphen > 0) {
                    return beforeReel.substring(prevHyphen + 1);
                }
            }
            return suffix;
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check FI series variants
        if (upperMpn.startsWith("FI-RE")) return "FI-RE";
        if (upperMpn.startsWith("FI-X")) return "FI-X";
        if (upperMpn.startsWith("FI-W")) return "FI-W";
        if (upperMpn.startsWith("FI-E")) return "FI-E";
        if (upperMpn.startsWith("FI-S")) return "FI-S";
        if (upperMpn.startsWith("FI-J")) return "FI-J";

        // Check DX series
        if (upperMpn.startsWith("DX07")) return "DX07";
        if (upperMpn.startsWith("DX40")) return "DX40";
        if (upperMpn.startsWith("DX30")) return "DX30";

        // Check MX series
        if (upperMpn.startsWith("MX34")) return "MX34";
        if (upperMpn.startsWith("MX44")) return "MX44";
        if (upperMpn.startsWith("MX77")) return "MX77";

        // Check IL series
        if (upperMpn.startsWith("IL-")) return "IL";

        // Check generic patterns
        for (String series : SERIES_FAMILIES.keySet()) {
            if (upperMpn.startsWith(series)) {
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
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Extract pin counts
        int pins1 = extractPinCount(mpn1);
        int pins2 = extractPinCount(mpn2);

        // Must have same pin count
        if (pins1 != pins2 || pins1 == 0) {
            return false;
        }

        // Check if they're compatible mounting variants
        return areCompatibleMountingTypes(mpn1, mpn2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    /**
     * Extract pin count from JAE MPN.
     */
    public int extractPinCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String upperMpn = mpn.toUpperCase();

        // FI-RE series: FI-RE51S-HF -> 51 pins
        Matcher fiReMatcher = FI_RE_PATTERN.matcher(upperMpn);
        if (fiReMatcher.matches()) {
            try {
                return Integer.parseInt(fiReMatcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // FI-X series: FI-X30HL -> 30 pins
        Matcher fiXMatcher = FI_X_PATTERN.matcher(upperMpn);
        if (fiXMatcher.matches()) {
            try {
                return Integer.parseInt(fiXMatcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // Generic FI pattern
        Matcher fiGenericMatcher = FI_GENERIC_PATTERN.matcher(upperMpn);
        if (fiGenericMatcher.matches()) {
            try {
                return Integer.parseInt(fiGenericMatcher.group(2));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // DX07 series: DX07S024JA1 -> 24 pins
        Matcher dx07Matcher = DX07_PATTERN.matcher(upperMpn);
        if (dx07Matcher.matches()) {
            try {
                return Integer.parseInt(dx07Matcher.group(2));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // DX40 series: DX40-36S -> 36 pins
        Matcher dx40Matcher = DX40_PATTERN.matcher(upperMpn);
        if (dx40Matcher.matches()) {
            try {
                return Integer.parseInt(dx40Matcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // MX34 series: MX34036NF1 -> 36 pins
        Matcher mx34Matcher = MX34_PATTERN.matcher(upperMpn);
        if (mx34Matcher.matches()) {
            try {
                return Integer.parseInt(mx34Matcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // MX44 series
        Matcher mx44Matcher = MX44_PATTERN.matcher(upperMpn);
        if (mx44Matcher.matches()) {
            try {
                return Integer.parseInt(mx44Matcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // MX77 series
        Matcher mx77Matcher = MX77_PATTERN.matcher(upperMpn);
        if (mx77Matcher.matches()) {
            try {
                return Integer.parseInt(mx77Matcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // IL series: IL-xxx-16xxx -> 16 pins
        Matcher ilMatcher = IL_PATTERN.matcher(upperMpn);
        if (ilMatcher.matches()) {
            try {
                return Integer.parseInt(ilMatcher.group(2));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    private boolean areCompatibleMountingTypes(String mpn1, String mpn2) {
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Same package is always compatible
        if (pkg1.equals(pkg2)) return true;

        // FI series (FPC connectors) are all SMT, so consider option variants compatible
        // Option codes like HF (halogen-free) and VF don't affect mounting
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        if (series1.startsWith("FI-") && series2.startsWith("FI-")) {
            // Same FI series connectors with different option codes are compatible
            return true;
        }

        // Check for SMT compatibility
        if (isSurfaceMount(pkg1) && isSurfaceMount(pkg2)) return true;

        // Check for through-hole compatibility
        if (isThroughHole(pkg1) && isThroughHole(pkg2)) return true;

        return false;
    }

    private boolean isSurfaceMount(String pkg) {
        if (pkg == null || pkg.isEmpty()) return true;  // Default to SMT for JAE
        return pkg.contains("S") || pkg.startsWith("S") || pkg.contains("SM");
    }

    private boolean isThroughHole(String pkg) {
        if (pkg == null || pkg.isEmpty()) return false;
        return pkg.contains("P") || pkg.contains("DIP");
    }

    /**
     * Get the pitch for a given JAE MPN.
     * @param mpn The manufacturer part number
     * @return Pitch in mm as a string, or empty string if unknown
     */
    public String getPitch(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_PITCH.getOrDefault(series, "");
    }

    /**
     * Get the mounting type for a given JAE MPN.
     * @param mpn The manufacturer part number
     * @return "SMT" for surface mount, "THT" for through-hole, or "Other"
     */
    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (isSurfaceMount(packageCode)) return "SMT";
        if (isThroughHole(packageCode)) return "THT";
        return "SMT";  // Default to SMT for JAE
    }

    /**
     * Get the rated current for a given JAE MPN.
     * @param mpn The manufacturer part number
     * @return Rated current in Amperes, or 0.0 if unknown
     */
    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    /**
     * Get the application type for a given JAE MPN.
     * @param mpn The manufacturer part number
     * @return Application type description
     */
    public String getApplicationType(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "FI-RE", "FI-X", "FI-W", "FI-E" -> "FPC/FFC";
            case "FI-S", "FI-J" -> "Wire-to-Board";
            case "DX07" -> "USB Type-C";
            case "DX40", "DX30" -> "Board-to-Board";
            case "MX34", "MX44", "MX77" -> "Automotive";
            case "IL" -> "Circular";
            default -> "General Purpose";
        };
    }

    /**
     * Check if the connector is waterproof (automotive rated).
     * @param mpn The manufacturer part number
     * @return true if waterproof
     */
    public boolean isWaterproof(String mpn) {
        String series = extractSeries(mpn);
        // MX34 and MX77 series are waterproof automotive connectors
        return "MX34".equals(series) || "MX77".equals(series);
    }

    /**
     * Get the connector family name.
     * @param mpn The manufacturer part number
     * @return Family name or "Unknown"
     */
    public String getFamily(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_FAMILIES.getOrDefault(series, "Unknown");
    }

    /**
     * Check if the connector is a USB Type-C connector.
     * @param mpn The manufacturer part number
     * @return true if USB Type-C
     */
    public boolean isUSBTypeC(String mpn) {
        return "DX07".equals(extractSeries(mpn));
    }

    /**
     * Check if the connector is automotive grade.
     * @param mpn The manufacturer part number
     * @return true if automotive grade
     */
    public boolean isAutomotiveGrade(String mpn) {
        String series = extractSeries(mpn);
        return series.startsWith("MX");
    }

    /**
     * Check if the connector is an FPC/FFC type.
     * @param mpn The manufacturer part number
     * @return true if FPC/FFC connector
     */
    public boolean isFPCConnector(String mpn) {
        String series = extractSeries(mpn);
        return series.startsWith("FI-");
    }
}
