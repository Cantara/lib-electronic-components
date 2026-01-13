package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

/**
 * Handler for Shenzhen Jinling Electronics Co., Ltd. (JILN) components.
 * Specializes in pin headers, female headers, and connector products.
 *
 * Company: Shenzhen Jinling Electronics Co., Ltd.
 * Founded: 2004
 * Website: szjiln.en.made-in-china.com, jilnconnector.com
 *
 * Product Lines:
 * - Pin Headers (families: 13=1.27mm, 16=2.00mm, 17=2.54mm)
 * - Female Headers (families: 26=2.00mm, 27=2.54mm)
 * - IDC Connectors
 * - Board-to-Board Connectors
 * - Terminal Blocks
 *
 * MPN Systems:
 * 1. Elprint position-based encoding: 15-17 characters (273102NSNSUXT)
 * 2. JILN distributor format: 15-18 characters (321010MG0CBK00A02)
 *
 * Elprint Format (position-based):
 * [0-1]   Family (13/16/17=pin, 26/27=female)
 * [2-3]   Plastics height × 10 (31 = 3.1mm)
 * [4]     House count (usually 0)
 * [5]     Rows (1/2/3)
 * [6-7]   Pins per row (02-99)
 * [8]     Insulator material (A-F)
 * [9]     Post (W/N)
 * [10-11] Contact plating (SN, G0-G8, S0)
 * [12]    Connector type (M/S/R/W/Z)
 * [13]    Contact type (U/S/R)
 * [14]    Packing (T/R/O/A/P)
 * [15-16] Optional layout (01/02)
 */
public class JinlingHandler extends AbstractManufacturerHandler {

    // Family to category mapping
    private static final Map<String, String> FAMILY_CATEGORIES = Map.ofEntries(
        new SimpleEntry<>("13", "Pin Header"),
        new SimpleEntry<>("16", "Pin Header"),
        new SimpleEntry<>("17", "Pin Header"),
        new SimpleEntry<>("26", "Female Header"),
        new SimpleEntry<>("27", "Female Header"),
        new SimpleEntry<>("32", "IDC Connector")
    );

    // Family to pitch mapping (mm)
    private static final Map<String, String> FAMILY_PITCH = Map.ofEntries(
        new SimpleEntry<>("13", "1.27"),
        new SimpleEntry<>("16", "2.00"),
        new SimpleEntry<>("17", "2.54"),
        new SimpleEntry<>("26", "2.00"),
        new SimpleEntry<>("27", "2.54"),
        new SimpleEntry<>("32", "2.54")
    );

    // Insulator material decode map (position 8)
    private static final Map<String, String> INSULATOR_MATERIALS = Map.ofEntries(
        new SimpleEntry<>("A", "PBT"),
        new SimpleEntry<>("B", "PA66"),
        new SimpleEntry<>("C", "PA6T"),
        new SimpleEntry<>("D", "PA46"),
        new SimpleEntry<>("E", "PA9T"),
        new SimpleEntry<>("F", "LCP")
    );

    // Contact plating decode map (positions 10-11)
    private static final Map<String, String> PLATING_CODES = Map.ofEntries(
        new SimpleEntry<>("SN", "Tin"),
        new SimpleEntry<>("G0", "Gold Flash"),
        new SimpleEntry<>("G1", "3µin Gold"),
        new SimpleEntry<>("G2", "5µin Gold"),
        new SimpleEntry<>("G3", "10µin Gold"),
        new SimpleEntry<>("G4", "15µin Gold"),
        new SimpleEntry<>("G5", "30µin Gold"),
        new SimpleEntry<>("S0", "Gold Flash/Tin")
    );

    // Connector type decode map (position 12)
    private static final Map<String, String> CONNECTOR_TYPES = Map.ofEntries(
        new SimpleEntry<>("M", "SMT"),
        new SimpleEntry<>("S", "Straight THT"),
        new SimpleEntry<>("R", "Right Angle THT"),
        new SimpleEntry<>("W", "Straddle THT"),
        new SimpleEntry<>("Z", "Right Angle SMT")
    );

    // Contact type decode map (position 13)
    private static final Map<String, String> CONTACT_TYPES = Map.ofEntries(
        new SimpleEntry<>("U", "U-Type"),
        new SimpleEntry<>("S", "Straight"),
        new SimpleEntry<>("R", "Right Angle")
    );

    // Packing decode map (position 14)
    private static final Map<String, String> PACKING_TYPES = Map.ofEntries(
        new SimpleEntry<>("T", "Tube"),
        new SimpleEntry<>("P", "Tube + Cap"),
        new SimpleEntry<>("R", "Reel"),
        new SimpleEntry<>("O", "PE Bag"),
        new SimpleEntry<>("A", "Tray")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Elprint position-based patterns (15 chars exactly)
        // Pattern: Family(2) + Numeric(6) + Letter + Letter + Alphanumeric(2) + Letters(3)
        // Pin Headers (male) - families 13, 16, 17
        registry.addPattern(ComponentType.CONNECTOR, "^13[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^13[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");

        registry.addPattern(ComponentType.CONNECTOR, "^16[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^16[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");

        registry.addPattern(ComponentType.CONNECTOR, "^17[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^17[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");

        // Female Headers - families 26, 27
        registry.addPattern(ComponentType.CONNECTOR, "^26[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^26[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");

        registry.addPattern(ComponentType.CONNECTOR, "^27[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^27[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$");

        // JILN distributor format patterns (actual MPNs from LCSC/JLCPCB)
        // IDC Connectors (32xxxx pattern)
        registry.addPattern(ComponentType.CONNECTOR, "^32[0-9]{4}[A-Z]{2}[0-9][A-Z]{3}[0-9]{2}[A-Z][0-9]{2}");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^32[0-9]{4}[A-Z]{2}[0-9][A-Z]{3}[0-9]{2}[A-Z][0-9]{2}");

        // Pin/Female Headers (12xxx, 22xxx patterns)
        registry.addPattern(ComponentType.CONNECTOR, "^1[0-9]{4}[0-9]{3,4}[A-Z]{3}[0-9][A-Z][0-9]{6,7}");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^1[0-9]{4}[0-9]{3,4}[A-Z]{3}[0-9][A-Z][0-9]{6,7}");

        registry.addPattern(ComponentType.CONNECTOR, "^22[0-9]{3}[0-9]{3,4}[A-Z]{3}[0-9][A-Z]{3}[0-9]{2}");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^22[0-9]{3}[0-9]{3,4}[A-Z]{3}[0-9][A-Z]{3}[0-9]{2}");

        // Board-to-board connectors (35xxxx pattern)
        registry.addPattern(ComponentType.CONNECTOR, "^35[0-9]{4}[0-9]{2}[A-Z]{4}[0-9][A-Z]{3}[0-9]");
        registry.addPattern(ComponentType.CONNECTOR_JINLING, "^35[0-9]{4}[0-9]{2}[A-Z]{4}[0-9][A-Z]{3}[0-9]");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CONNECTOR,
            ComponentType.CONNECTOR_JINLING
        );
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract first 2 digits as family/series code
        if (upperMpn.length() >= 2) {
            return upperMpn.substring(0, 2);
        }

        return "";
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // For elprint format (position 12 is connector type)
        if (isElprintFormat(upperMpn) && upperMpn.length() >= 13) {
            String typeCode = upperMpn.substring(12, 13);
            return CONNECTOR_TYPES.getOrDefault(typeCode, typeCode);
        }

        // For JILN distributor format, check for M (SMT) or G (gold/THT indicators)
        if (upperMpn.contains("MG") || upperMpn.contains("M")) {
            return "SMT";
        }
        if (upperMpn.contains("CNG") || upperMpn.contains("ANG")) {
            return "Through-Hole";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Must be same family/series
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Must have same pin count
        int pins1 = extractPinCount(mpn1);
        int pins2 = extractPinCount(mpn2);
        if (pins1 != pins2 || pins1 == 0) {
            return false;
        }

        // Package codes must be compatible (both SMT or both THT)
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        return areCompatibleMountingTypes(pkg1, pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Explicit matching for supported types
        if (type == ComponentType.CONNECTOR || type == ComponentType.CONNECTOR_JINLING) {
            // Elprint format - families 13, 16, 17, 26, 27 (exactly 15 chars)
            // Pattern: Family(2) + Numeric(6) + Letter + Letter + Alphanumeric(2) + Letters(3)
            if (upperMpn.matches("^(?:13|16|17|26|27)[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$")) {
                return true;
            }

            // JILN distributor format patterns
            // IDC Connectors (32xxxx)
            if (upperMpn.matches("^32[0-9]{4}[A-Z]{2}[0-9][A-Z]{3}[0-9]{2}[A-Z][0-9]{2}")) {
                return true;
            }

            // Pin/Female Headers (12xxx, 22xxx)
            if (upperMpn.matches("^1[0-9]{4}[0-9]{3,4}[A-Z]{3}[0-9][A-Z][0-9]{6,7}")) {
                return true;
            }
            if (upperMpn.matches("^22[0-9]{3}[0-9]{3,4}[A-Z]{3}[0-9][A-Z]{3}[0-9]{2}")) {
                return true;
            }

            // Board-to-board (35xxxx)
            if (upperMpn.matches("^35[0-9]{4}[0-9]{2}[A-Z]{4}[0-9][A-Z]{3}[0-9]")) {
                return true;
            }
        }

        // Fall back to registry matching for current handler
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Extract pin count from MPN.
     * For elprint format: rows (pos 5) × pins/row (pos 6-7)
     * For distributor format: embedded in part number
     */
    public int extractPinCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String upperMpn = mpn.toUpperCase();

        // Elprint format: position 5 is rows, 6-7 is pins per row
        if (isElprintFormat(upperMpn) && upperMpn.length() >= 8) {
            try {
                int rows = Character.getNumericValue(upperMpn.charAt(5));
                int pinsPerRow = Integer.parseInt(upperMpn.substring(6, 8));
                return rows * pinsPerRow;
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // JILN distributor format: extract based on family
        if (upperMpn.startsWith("32") && upperMpn.length() >= 6) {
            // IDC format: positions 4-5 contain pin count (e.g., 321010 = 10 pins)
            try {
                String pinStr = upperMpn.substring(4, 6);
                return Integer.parseInt(pinStr);
            } catch (NumberFormatException e) {
                // Fall through
            }
        } else if ((upperMpn.startsWith("12") || upperMpn.startsWith("22")) && upperMpn.length() >= 8) {
            // Pin/Female header format: positions 6-7 contain pin count (e.g., 12251140 = 40 pins)
            try {
                String pinStr = upperMpn.substring(6, 8);
                return Integer.parseInt(pinStr);
            } catch (NumberFormatException e) {
                // Fall through
            }
        }

        return 0;
    }

    /**
     * Get row count (1=single, 2=dual, 3=triple).
     * Only applicable for elprint format.
     */
    public int getRowCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String upperMpn = mpn.toUpperCase();

        if (isElprintFormat(upperMpn) && upperMpn.length() >= 6) {
            return Character.getNumericValue(upperMpn.charAt(5));
        }

        // Try to infer from family and pin count for distributor format
        String family = extractSeries(upperMpn);
        if (upperMpn.contains("2X") || upperMpn.contains("2*")) {
            return 2;
        }
        if (upperMpn.contains("1X") || upperMpn.contains("1*")) {
            return 1;
        }

        return 1; // Default to single row
    }

    /**
     * Get connector pitch in millimeters based on family code.
     */
    public String getPitch(String mpn) {
        String family = extractSeries(mpn);
        return FAMILY_PITCH.getOrDefault(family, "");
    }

    /**
     * Get mounting type (SMT or THT).
     */
    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.isEmpty()) return "";

        if (packageCode.contains("SMT")) return "SMT";
        if (packageCode.contains("THT") || packageCode.contains("Through-Hole")) return "THT";

        return "THT"; // Default for connectors
    }

    /**
     * Get orientation (Straight or Right Angle).
     */
    public String getOrientation(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "Unknown";

        String upperMpn = mpn.toUpperCase();

        // Check position 12 for elprint format
        if (isElprintFormat(upperMpn) && upperMpn.length() >= 13) {
            String typeCode = upperMpn.substring(12, 13);
            if ("R".equals(typeCode) || "Z".equals(typeCode)) {
                return "Right Angle";
            }
            return "Straight";
        }

        return "Straight"; // Default
    }

    /**
     * Get gender (Male or Female) based on family code.
     */
    public String getGender(String mpn) {
        String family = extractSeries(mpn);

        // Families 13, 16, 17 are pin headers (male)
        if ("13".equals(family) || "16".equals(family) || "17".equals(family)) {
            return "Male";
        }

        // Families 26, 27 are female headers
        if ("26".equals(family) || "27".equals(family)) {
            return "Female";
        }

        // Check for female/male indicators in distributor format
        String upperMpn = mpn.toUpperCase();
        if (upperMpn.contains("FEMALE") || upperMpn.startsWith("22")) {
            return "Female";
        }

        return "Unknown";
    }

    /**
     * Get plastics height in millimeters.
     * Elprint format: positions 2-3 × 0.1
     */
    public double getPlasticsHeight(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0.0;

        String upperMpn = mpn.toUpperCase();

        if (isElprintFormat(upperMpn) && upperMpn.length() >= 4) {
            try {
                int heightCode = Integer.parseInt(upperMpn.substring(2, 4));
                return heightCode * 0.1;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }

        return 0.0;
    }

    /**
     * Get insulator material (e.g., "PBT", "PA66").
     * Elprint format: position 8
     */
    public String getInsulatorMaterial(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (isElprintFormat(upperMpn) && upperMpn.length() >= 9) {
            String materialCode = upperMpn.substring(8, 9);
            return INSULATOR_MATERIALS.getOrDefault(materialCode, materialCode);
        }

        return "";
    }

    /**
     * Get contact plating material.
     * Elprint format: positions 10-11
     */
    public String getContactPlating(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (isElprintFormat(upperMpn) && upperMpn.length() >= 12) {
            String platingCode = upperMpn.substring(10, 12);
            return PLATING_CODES.getOrDefault(platingCode, platingCode);
        }

        // For distributor format, check for G (gold) indicators
        if (upperMpn.contains("MG")) return "Gold";
        if (upperMpn.contains("SN")) return "Tin";

        return "";
    }

    /**
     * Check if connector has post/standoff.
     * Elprint format: position 9 (W=with, N=without)
     */
    public boolean hasPost(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        String upperMpn = mpn.toUpperCase();

        if (isElprintFormat(upperMpn) && upperMpn.length() >= 10) {
            return "W".equals(upperMpn.substring(9, 10));
        }

        return false;
    }

    /**
     * Get contact type (U-Type, Straight, Right Angle).
     * Elprint format: position 13
     */
    public String getContactType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (isElprintFormat(upperMpn) && upperMpn.length() >= 14) {
            String contactCode = upperMpn.substring(13, 14);
            return CONTACT_TYPES.getOrDefault(contactCode, contactCode);
        }

        return "";
    }

    /**
     * Get packing type (Tube, Reel, etc.).
     * Elprint format: position 14
     */
    public String getPacking(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (isElprintFormat(upperMpn) && upperMpn.length() >= 15) {
            String packingCode = upperMpn.substring(14, 15);
            return PACKING_TYPES.getOrDefault(packingCode, packingCode);
        }

        return "";
    }

    /**
     * Get connector category based on family code.
     */
    public String getCategory(String mpn) {
        String family = extractSeries(mpn);
        return FAMILY_CATEGORIES.getOrDefault(family, "Connector");
    }

    // Helper methods

    /**
     * Check if MPN is in elprint position-based format.
     * Elprint format: starts with family code (13/16/17/26/27) + 6 numeric + 7 alpha (15 total)
     */
    private boolean isElprintFormat(String mpn) {
        if (mpn == null || mpn.length() != 15) return false;

        String family = mpn.substring(0, 2);
        boolean isKnownFamily = "13".equals(family) || "16".equals(family) ||
                                "17".equals(family) || "26".equals(family) ||
                                "27".equals(family);

        // Elprint format has numeric positions 2-7 (height, house, rows, pins per row)
        boolean hasNumericPrefix = mpn.substring(2, 8).matches("[0-9]{6}");

        return isKnownFamily && hasNumericPrefix;
    }

    /**
     * Check if two mounting types are compatible for replacement.
     */
    private boolean areCompatibleMountingTypes(String pkg1, String pkg2) {
        if (pkg1.isEmpty() || pkg2.isEmpty()) return false;

        // Same type is always compatible
        if (pkg1.equals(pkg2)) return true;

        // Both SMT = compatible
        if (pkg1.contains("SMT") && pkg2.contains("SMT")) return true;

        // Both THT = compatible
        if ((pkg1.contains("THT") || pkg1.contains("Through-Hole")) &&
            (pkg2.contains("THT") || pkg2.contains("Through-Hole"))) {
            return true;
        }

        return false;
    }
}
