package no.cantara.electronic.component.lib;

import java.util.Map;
import java.util.Set;

/**
 * Centralized registry of IC package codes used across manufacturers.
 * <p>
 * This eliminates duplicate package code mappings across manufacturer handlers.
 * Handlers should use this registry instead of defining their own mappings.
 */
public final class PackageCodeRegistry {

    private PackageCodeRegistry() {
        // Utility class - no instantiation
    }

    // ========== DIP Packages (Through-Hole) ==========

    /**
     * Standard DIP (Dual In-line Package) suffix codes
     */
    public static final Map<String, String> DIP_CODES = Map.of(
            "N", "DIP",
            "P", "DIP"
    );

    // ========== SOIC Family ==========

    /**
     * SOIC (Small Outline IC) suffix codes
     */
    public static final Map<String, String> SOIC_CODES = Map.of(
            "D", "SOIC",
            "M", "SOIC",
            "R", "SOIC",
            "RT", "SOIC",
            "DW", "SOIC-Wide",
            "SO", "SOIC",
            "SM", "SOIC"
    );

    /**
     * TSSOP (Thin Shrink Small Outline Package) suffix codes
     */
    public static final Map<String, String> TSSOP_CODES = Map.of(
            "PW", "TSSOP",
            "DT", "TSSOP",
            "PT", "TSSOP",
            "RU", "TSSOP",
            "TS", "TSSOP",
            "ST", "TSSOP",
            "XU", "TSSOP",
            "V", "TSSOP"
    );

    /**
     * MSOP (Mini Small Outline Package) suffix codes
     */
    public static final Map<String, String> MSOP_CODES = Map.of(
            "DGK", "MSOP"
    );

    // ========== SOT Family ==========

    /**
     * SOT-23 and related suffix codes
     */
    public static final Map<String, String> SOT23_CODES = Map.of(
            "DBV", "SOT-23",
            "T1", "SOT-23",
            "3", "SOT-23"
    );

    /**
     * SOT-223 suffix codes
     */
    public static final Map<String, String> SOT223_CODES = Map.of(
            "MP", "SOT-223",
            "U", "SOT-223",
            "G", "SOT-223"
    );

    /**
     * Other SOT package suffix codes
     */
    public static final Map<String, String> SOT_OTHER_CODES = Map.of(
            "DRL", "SOT-553",
            "DRV", "SON"
    );

    // ========== Power Packages (Through-Hole) ==========

    /**
     * TO-220 family suffix codes
     */
    public static final Map<String, String> TO220_CODES = Map.of(
            "T", "TO-220",
            "T3", "TO-220",
            "CT", "TO-220",
            "T6", "TO-220",
            "TA", "TO-220F",
            "FP", "TO-220F",
            "DCB", "TO-220",
            "DCA", "TO-220",
            "L", "TO-220"
    );

    /**
     * Other TO package suffix codes
     */
    public static final Map<String, String> TO_OTHER_CODES = Map.of(
            "K", "TO-3",
            "H", "TO-39",
            "KC", "TO-252",
            "KV", "TO-252",
            "TU", "TO-251",
            "F", "TO-251"
    );

    // ========== Power Packages (Surface Mount) ==========

    /**
     * D2PAK/DPAK suffix codes
     */
    public static final Map<String, String> DPAK_CODES = Map.of(
            "S", "D2PAK",
            "DPAK", "DPAK"
    );

    // ========== Diode Packages ==========

    /**
     * Diode package suffix codes
     */
    public static final Map<String, String> DIODE_CODES = Map.of(
            "RL", "DO-41",
            "DO41", "DO-41",
            "DO35", "DO-35"
    );

    // ========== Generic/LED ==========

    /**
     * Generic mounting type codes
     */
    public static final Map<String, String> GENERIC_CODES = Map.of(
            "SMD", "SMD",
            "THT", "THT"
    );

    // ========== Combined Standard Codes ==========

    /**
     * All standard package codes combined.
     * Use this for general package code resolution.
     */
    public static final Map<String, String> STANDARD = Map.ofEntries(
            // DIP
            Map.entry("N", "DIP"),
            Map.entry("P", "DIP"),
            Map.entry("PU", "PDIP"),          // Atmel Plastic DIP

            // Atmel-specific package codes
            Map.entry("AU", "TQFP"),          // Atmel Thin QFP
            Map.entry("MU", "QFN"),           // Atmel QFN/MLF
            Map.entry("SU", "SOIC"),          // Atmel SOIC
            Map.entry("XU", "TSSOP"),         // Atmel TSSOP
            Map.entry("CU", "WLCSP"),         // Atmel Wafer Level CSP

            // SOIC
            Map.entry("D", "SOIC"),
            Map.entry("M", "SOIC"),
            Map.entry("R", "SOIC"),
            Map.entry("DW", "SOIC-Wide"),

            // TSSOP
            Map.entry("PW", "TSSOP"),
            Map.entry("DT", "TSSOP"),
            Map.entry("PT", "TSSOP"),

            // MSOP
            Map.entry("DGK", "MSOP"),

            // SOT-23
            Map.entry("DBV", "SOT-23"),

            // SOT-223
            Map.entry("MP", "SOT-223"),
            Map.entry("U", "SOT-223"),

            // SOT other
            Map.entry("DRL", "SOT-553"),
            Map.entry("DRV", "SON"),

            // TO-220
            Map.entry("T", "TO-220"),
            Map.entry("T3", "TO-220"),
            Map.entry("CT", "TO-220"),
            Map.entry("TA", "TO-220F"),
            Map.entry("FP", "TO-220F"),

            // TO other
            Map.entry("K", "TO-3"),
            Map.entry("H", "TO-39"),
            Map.entry("KC", "TO-252"),
            Map.entry("KV", "TO-252"),
            Map.entry("TU", "TO-251"),
            Map.entry("F", "TO-251"),

            // D2PAK/DPAK
            Map.entry("S", "D2PAK"),
            Map.entry("L", "DPAK"),

            // Diodes
            Map.entry("RL", "DO-41"),
            Map.entry("G", "DO-35"),

            // Generic
            Map.entry("SMD", "SMD"),
            Map.entry("THT", "THT")
    );

    /**
     * Power package names for grouping
     */
    public static final Set<String> POWER_PACKAGES = Set.of(
            "TO-220", "TO-220F", "TO-252", "TO-247", "TO-263",
            "D2PAK", "DPAK", "SOT-223"
    );

    /**
     * Through-hole package names for grouping
     */
    public static final Set<String> THROUGH_HOLE_PACKAGES = Set.of(
            "DIP", "TO-220", "TO-220F", "TO-3", "TO-39", "TO-247",
            "DO-41", "DO-35", "THT"
    );

    /**
     * Surface mount package names for grouping
     */
    public static final Set<String> SMD_PACKAGES = Set.of(
            "SOIC", "SOIC-Wide", "TSSOP", "MSOP", "QFP", "QFN", "BGA",
            "SOT-23", "SOT-223", "SOT-553", "SON",
            "D2PAK", "DPAK", "TO-252", "TO-263",
            "SMD"
    );

    // ========== Resolution Methods ==========

    /**
     * Resolve a package suffix code to its full package name.
     *
     * @param code the suffix code (e.g., "N", "D", "PW", "T")
     * @return the package name (e.g., "DIP", "SOIC", "TSSOP", "TO-220") or the original code if not found
     */
    public static String resolve(String code) {
        if (code == null || code.isEmpty()) {
            return "";
        }
        String upperCode = code.toUpperCase().trim();
        return STANDARD.getOrDefault(upperCode, upperCode);
    }

    /**
     * Check if a suffix code is a known package code.
     *
     * @param code the suffix code to check
     * @return true if it's a recognized package code
     */
    public static boolean isKnownCode(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        return STANDARD.containsKey(code.toUpperCase().trim());
    }

    /**
     * Check if a package is a power package (higher current capability).
     *
     * @param packageName the package name (e.g., "TO-220", "D2PAK")
     * @return true if it's a power package
     */
    public static boolean isPowerPackage(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }
        return POWER_PACKAGES.contains(packageName.toUpperCase().trim());
    }

    /**
     * Check if a package is through-hole.
     *
     * @param packageName the package name
     * @return true if it's a through-hole package
     */
    public static boolean isThroughHole(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }
        return THROUGH_HOLE_PACKAGES.contains(packageName.toUpperCase().trim());
    }

    /**
     * Check if a package is surface mount.
     *
     * @param packageName the package name
     * @return true if it's an SMD package
     */
    public static boolean isSurfaceMount(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }
        return SMD_PACKAGES.contains(packageName.toUpperCase().trim());
    }

    /**
     * Check if two packages are compatible for replacement.
     * Packages are compatible if they're in the same category (e.g., both power, both SMD small).
     *
     * @param pkg1 first package name
     * @param pkg2 second package name
     * @return true if packages are potentially compatible
     */
    public static boolean areCompatible(String pkg1, String pkg2) {
        if (pkg1 == null || pkg2 == null) {
            return false;
        }

        String p1 = pkg1.toUpperCase().trim();
        String p2 = pkg2.toUpperCase().trim();

        // Same package is always compatible
        if (p1.equals(p2)) {
            return true;
        }

        // Power packages are interchangeable with footprint consideration
        if (POWER_PACKAGES.contains(p1) && POWER_PACKAGES.contains(p2)) {
            return true;
        }

        // Small SMD packages (op-amps, etc.) are often pin-compatible
        Set<String> smallSmd = Set.of("DIP", "SOIC", "TSSOP", "MSOP");
        if (smallSmd.contains(p1) && smallSmd.contains(p2)) {
            return true;
        }

        return false;
    }
}
