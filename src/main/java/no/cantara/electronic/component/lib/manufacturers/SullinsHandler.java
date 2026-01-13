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
 * Handler for Sullins Connector Solutions components.
 * Specializes in headers and sockets including:
 * - SBH series (box headers)
 * - PPPC series (female headers)
 * - PRPC series (male headers)
 * - NPPN series (pin headers)
 * - SFH series (FFC/FPC connectors)
 *
 * Example MPNs:
 * - SBH11-PBPC-D10-ST-BK (box header, 10 pin dual row)
 * - PPPC081LFBN-RC (female header, 8 pin single row)
 * - PRPC040SAAN-RC (male header, 40 pin single row)
 * - NPPN101BFCN-RC (pin header)
 * - SFH11-PBPC-D17-RA-BK (FFC/FPC connector, 17 pin)
 */
public class SullinsHandler implements ManufacturerHandler {

    // SBH series pattern: SBH11-PBPC-D10-ST-BK
    private static final Pattern SBH_PATTERN = Pattern.compile(
            "SBH([0-9]+)-([A-Z]+)-D([0-9]+)-([A-Z]+)(?:-([A-Z]+))?",
            Pattern.CASE_INSENSITIVE);

    // PPPC series pattern: PPPC081LFBN-RC
    private static final Pattern PPPC_PATTERN = Pattern.compile(
            "PPPC([0-9]+)([0-9])([A-Z]+)([A-Z]+)-([A-Z]+)",
            Pattern.CASE_INSENSITIVE);

    // PRPC series pattern: PRPC040SAAN-RC
    private static final Pattern PRPC_PATTERN = Pattern.compile(
            "PRPC([0-9]+)([0-9])([A-Z]+)([A-Z]+)-([A-Z]+)",
            Pattern.CASE_INSENSITIVE);

    // NPPN series pattern: NPPN101BFCN-RC
    private static final Pattern NPPN_PATTERN = Pattern.compile(
            "NPPN([0-9]+)([0-9])([A-Z]+)([A-Z]+)-([A-Z]+)",
            Pattern.CASE_INSENSITIVE);

    // SFH series pattern (similar to SBH): SFH11-PBPC-D17-RA-BK
    private static final Pattern SFH_PATTERN = Pattern.compile(
            "SFH([0-9]+)-([A-Z]+)-D([0-9]+)-([A-Z]+)(?:-([A-Z]+))?",
            Pattern.CASE_INSENSITIVE);

    // Generic Sullins pattern for matching
    private static final Pattern GENERIC_PATTERN = Pattern.compile(
            "^(S[A-Z]{2}[0-9]+|P[A-Z]{3}[0-9]+|N[A-Z]{3}[0-9]+).*",
            Pattern.CASE_INSENSITIVE);

    // Series to description mapping
    private static final Map<String, String> SERIES_DESCRIPTION = Map.ofEntries(
            new SimpleEntry<>("SBH", "Box Header"),
            new SimpleEntry<>("PPPC", "Female Header"),
            new SimpleEntry<>("PRPC", "Male Header"),
            new SimpleEntry<>("NPPN", "Pin Header"),
            new SimpleEntry<>("SFH", "FFC/FPC Connector")
    );

    // Pitch mapping (in mm) based on series
    private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
            new SimpleEntry<>("SBH", "2.54"),
            new SimpleEntry<>("PPPC", "2.54"),
            new SimpleEntry<>("PRPC", "2.54"),
            new SimpleEntry<>("NPPN", "2.54"),
            new SimpleEntry<>("SFH", "1.27")
    );

    // Rated current (in Amps) based on series
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            new SimpleEntry<>("SBH", 3.0),
            new SimpleEntry<>("PPPC", 3.0),
            new SimpleEntry<>("PRPC", 3.0),
            new SimpleEntry<>("NPPN", 3.0),
            new SimpleEntry<>("SFH", 1.0)
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
        // SBH series (box headers)
        registry.addPattern(ComponentType.CONNECTOR, "^SBH[0-9]+-[A-Z]+-D[0-9]+.*");

        // PPPC series (female headers)
        registry.addPattern(ComponentType.CONNECTOR, "^PPPC[0-9]+.*");

        // PRPC series (male headers)
        registry.addPattern(ComponentType.CONNECTOR, "^PRPC[0-9]+.*");

        // NPPN series (pin headers)
        registry.addPattern(ComponentType.CONNECTOR, "^NPPN[0-9]+.*");

        // SFH series (FFC/FPC connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^SFH[0-9]+-[A-Z]+-D[0-9]+.*");

        // Generic patterns for other Sullins products
        registry.addPattern(ComponentType.CONNECTOR, "^S[A-Z]{2}[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^P[A-Z]{3}[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^N[A-Z]{3}[0-9]+.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Try SBH/SFH pattern
        Matcher sbhMatcher = SBH_PATTERN.matcher(upperMpn);
        if (sbhMatcher.matches()) {
            // Package code is the mounting type (ST=straight, RA=right angle)
            String mountType = sbhMatcher.group(4);
            return decodeMountingCode(mountType);
        }

        Matcher sfhMatcher = SFH_PATTERN.matcher(upperMpn);
        if (sfhMatcher.matches()) {
            String mountType = sfhMatcher.group(4);
            return decodeMountingCode(mountType);
        }

        // Try PPPC pattern
        Matcher pppcMatcher = PPPC_PATTERN.matcher(upperMpn);
        if (pppcMatcher.matches()) {
            return decodeMountingFromSuffix(pppcMatcher.group(5));
        }

        // Try PRPC pattern
        Matcher prpcMatcher = PRPC_PATTERN.matcher(upperMpn);
        if (prpcMatcher.matches()) {
            return decodeMountingFromSuffix(prpcMatcher.group(5));
        }

        // Try NPPN pattern
        Matcher nppnMatcher = NPPN_PATTERN.matcher(upperMpn);
        if (nppnMatcher.matches()) {
            return decodeMountingFromSuffix(nppnMatcher.group(5));
        }

        // Try to find common suffix codes
        if (upperMpn.contains("-RC")) {
            return "Through-Hole";
        } else if (upperMpn.contains("-SC")) {
            return "SMT";
        }

        return "";
    }

    private String decodeMountingCode(String code) {
        if (code == null) return "";
        return switch (code.toUpperCase()) {
            case "ST" -> "Through-Hole Straight";
            case "RA" -> "Through-Hole Right Angle";
            case "SM" -> "SMT";
            default -> code;
        };
    }

    private String decodeMountingFromSuffix(String suffix) {
        if (suffix == null) return "";
        return switch (suffix.toUpperCase()) {
            case "RC" -> "Through-Hole";
            case "SC" -> "SMT";
            default -> suffix;
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for specific series prefixes
        if (upperMpn.startsWith("SBH")) return "SBH";
        if (upperMpn.startsWith("PPPC")) return "PPPC";
        if (upperMpn.startsWith("PRPC")) return "PRPC";
        if (upperMpn.startsWith("NPPN")) return "NPPN";
        if (upperMpn.startsWith("SFH")) return "SFH";

        // Extract generic series prefix (first 3-4 chars)
        Matcher matcher = Pattern.compile("^([A-Z]{3,4})[0-9]").matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from the same series
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Extract pin counts
        int pins1 = extractPinCount(mpn1);
        int pins2 = extractPinCount(mpn2);

        // Must have the same pin count
        if (pins1 != pins2 || pins1 == 0) {
            return false;
        }

        // Check if they're compatible mounting types
        return areCompatibleMountingTypes(mpn1, mpn2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    /**
     * Extract pin count from MPN.
     */
    public int extractPinCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String upperMpn = mpn.toUpperCase();

        // SBH and SFH patterns: D followed by pin count
        Matcher sbhMatcher = SBH_PATTERN.matcher(upperMpn);
        if (sbhMatcher.matches()) {
            try {
                // Group 3 is pin count (after D prefix)
                return Integer.parseInt(sbhMatcher.group(3)) * 2; // Dual row = double pins
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        Matcher sfhMatcher = SFH_PATTERN.matcher(upperMpn);
        if (sfhMatcher.matches()) {
            try {
                return Integer.parseInt(sfhMatcher.group(3)) * 2;
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // PPPC/PRPC/NPPN patterns: first 2-3 digits after series prefix
        Matcher pppcMatcher = PPPC_PATTERN.matcher(upperMpn);
        if (pppcMatcher.matches()) {
            try {
                // Group 1 is pin count (2-3 digits)
                return Integer.parseInt(pppcMatcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        Matcher prpcMatcher = PRPC_PATTERN.matcher(upperMpn);
        if (prpcMatcher.matches()) {
            try {
                return Integer.parseInt(prpcMatcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        Matcher nppnMatcher = NPPN_PATTERN.matcher(upperMpn);
        if (nppnMatcher.matches()) {
            try {
                return Integer.parseInt(nppnMatcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    /**
     * Get the row count (single or dual row).
     */
    public int getRowCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String upperMpn = mpn.toUpperCase();

        // SBH and SFH are always dual row (D prefix in MPN)
        if (upperMpn.startsWith("SBH") || upperMpn.startsWith("SFH")) {
            return 2;
        }

        // PPPC/PRPC/NPPN: check the digit after pin count
        // 1 = single row, 2 = dual row
        Matcher pppcMatcher = PPPC_PATTERN.matcher(upperMpn);
        if (pppcMatcher.matches()) {
            try {
                return Integer.parseInt(pppcMatcher.group(2));
            } catch (NumberFormatException e) {
                return 1;
            }
        }

        Matcher prpcMatcher = PRPC_PATTERN.matcher(upperMpn);
        if (prpcMatcher.matches()) {
            try {
                return Integer.parseInt(prpcMatcher.group(2));
            } catch (NumberFormatException e) {
                return 1;
            }
        }

        Matcher nppnMatcher = NPPN_PATTERN.matcher(upperMpn);
        if (nppnMatcher.matches()) {
            try {
                return Integer.parseInt(nppnMatcher.group(2));
            } catch (NumberFormatException e) {
                return 1;
            }
        }

        return 1;
    }

    private boolean areCompatibleMountingTypes(String mpn1, String mpn2) {
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Same mounting type is always compatible
        if (pkg1.equals(pkg2)) return true;

        // Check for through-hole compatibility
        if (isThroughHole(pkg1) && isThroughHole(pkg2)) return true;

        // Check for SMT compatibility
        if (isSurfaceMount(pkg1) && isSurfaceMount(pkg2)) return true;

        return false;
    }

    private boolean isThroughHole(String packageCode) {
        if (packageCode == null) return false;
        String upper = packageCode.toUpperCase();
        return upper.contains("THROUGH") || upper.contains("RC") || upper.contains("ST");
    }

    private boolean isSurfaceMount(String packageCode) {
        if (packageCode == null) return false;
        String upper = packageCode.toUpperCase();
        return upper.contains("SMT") || upper.contains("SC") || upper.contains("SM");
    }

    /**
     * Get the pitch for the connector series.
     */
    public String getPitch(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_PITCH.getOrDefault(series, "");
    }

    /**
     * Get the mounting type (THT or SMT).
     */
    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (isSurfaceMount(packageCode)) return "SMT";
        if (isThroughHole(packageCode)) return "THT";
        return "Unknown";
    }

    /**
     * Get the orientation (Straight or Right Angle).
     */
    public String getOrientation(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode == null) return "Unknown";
        String upper = packageCode.toUpperCase();
        // Check for explicit right angle markers
        // Note: Check for "RIGHT ANGLE" first, then check for standalone "-RA" or "RA-" or " RA"
        // to avoid false positive from "stRAight"
        if (upper.contains("RIGHT ANGLE")) {
            return "Right Angle";
        }
        // More precise RA detection - look for "RA" as a standalone code or at boundaries
        if (upper.equals("RA") || upper.startsWith("RA ") || upper.startsWith("RA-") ||
            upper.endsWith(" RA") || upper.endsWith("-RA") ||
            upper.contains(" RA ") || upper.contains("-RA-") ||
            upper.contains(" RA-") || upper.contains("-RA ")) {
            return "Right Angle";
        }
        return "Straight";
    }

    /**
     * Get the rated current for the connector series.
     */
    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    /**
     * Get the connector gender.
     */
    public String getGender(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "PPPC" -> "Female";
            case "PRPC", "NPPN" -> "Male";
            case "SBH", "SFH" -> "Male (Shrouded)";
            default -> "Unknown";
        };
    }

    /**
     * Get the application type for the connector.
     */
    public String getApplicationType(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "SBH" -> "Box Header";
            case "PPPC" -> "Female Header/Socket";
            case "PRPC" -> "Male Header";
            case "NPPN" -> "Pin Header";
            case "SFH" -> "FFC/FPC Connector";
            default -> "General Purpose Header";
        };
    }

    /**
     * Get the series description.
     */
    public String getSeriesDescription(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_DESCRIPTION.getOrDefault(series, "");
    }

    /**
     * Check if the connector is shrouded (has polarization key).
     */
    public boolean isShrouded(String mpn) {
        String series = extractSeries(mpn);
        // SBH and SFH series are shrouded/box style
        return "SBH".equals(series) || "SFH".equals(series);
    }

    /**
     * Get contact plating type from MPN.
     */
    public String getContactPlating(String mpn) {
        if (mpn == null) return "Unknown";
        String upperMpn = mpn.toUpperCase();

        // Look for plating codes in the MPN
        if (upperMpn.contains("GN") || upperMpn.contains("G/")) {
            return "Gold";
        } else if (upperMpn.contains("SN") || upperMpn.contains("T/")) {
            return "Tin";
        } else if (upperMpn.contains("LF")) {
            return "Lead-Free Tin";
        }
        return "Standard";
    }
}
