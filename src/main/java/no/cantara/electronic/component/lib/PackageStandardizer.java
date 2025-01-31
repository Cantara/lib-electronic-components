package no.cantara.electronic.component.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Utility class for standardizing electronic component package names and formats.
 * Provides comprehensive package information including dimensions and pad layouts.
 */
public class PackageStandardizer {
    private static final Logger LOG = LoggerFactory.getLogger(PackageStandardizer.class);

    private PackageStandardizer() {
        // Private constructor to prevent instantiation
    }

    public enum PackageType {
        // Through-hole Packages
        DIP,        // Dual In-line Package
        SIP,        // Single In-line Package
        ZIP,        // Zig-zag In-line Package
        TO92,       // Transistor Outline Package
        TO220,      // Power Package
        TO247,      // High-Power Package
        TO263,      // Surface Mount Power Package

        // Surface Mount Packages - ICs
        SOIC,       // Small Outline Integrated Circuit
        SSOP,       // Shrink Small Outline Package
        TSSOP,      // Thin Shrink Small Outline Package
        MSOP,       // Mini Small Outline Package
        HSOP,       // Heat-sink Small Outline Package
        QFP,        // Quad Flat Package
        LQFP,       // Low-profile Quad Flat Package
        TQFP,       // Thin Quad Flat Package
        PQFP,       // Plastic Quad Flat Package
        QFN,        // Quad Flat No-leads
        DFN,        // Dual Flat No-leads
        SON,        // Small Outline No-leads
        WSON,       // Very Very Thin Small Outline No-leads
        BGA,        // Ball Grid Array
        FBGA,       // Fine-pitch Ball Grid Array
        TFBGA,      // Thin Fine-pitch Ball Grid Array
        LGA,        // Land Grid Array
        PLCC,       // Plastic Leaded Chip Carrier

        // Surface Mount Packages - Discrete
        SOT23,      // Small Outline Transistor
        SOT223,     // Small Outline Transistor (4-lead)
        SOT323,     // Small Outline Transistor (SC-70)
        SOT363,     // Small Outline Transistor (6-lead)
        SOT663,     // Small Outline Transistor (8-lead)
        SOD123,     // Small Outline Diode
        SOD323,     // Small Outline Diode (SC-76)
        SOD523,     // Small Outline Diode (SC-79)
        DPAK,       // Decawatt Package
        D2PAK,      // TO-263
        D3PAK,      // TO-263 variant

        // Surface Mount Packages - Passive
        SMD_01005,  // 0.4mm × 0.2mm
        SMD_0201,   // 0.6mm × 0.3mm
        SMD_0402,   // 1.0mm × 0.5mm
        SMD_0603,   // 1.6mm × 0.8mm
        SMD_0805,   // 2.0mm × 1.25mm
        SMD_1206,   // 3.2mm × 1.6mm
        SMD_1210,   // 3.2mm × 2.5mm
        SMD_1812,   // 4.5mm × 3.2mm
        SMD_2010,   // 5.0mm × 2.5mm
        SMD_2512,   // 6.3mm × 3.2mm

        // Special Packages
        MELF,       // Metal Electrode Leadless Face
        MINIMELF,   // Mini MELF
        MICROMELF,  // Micro MELF
        DO214,      // Diode Outline Package
        DO214AC,    // SMA Package
        DO214AA,    // SMB Package
        DO214AB,    // SMC Package

        // Tantalum Capacitors
        CASE_A,     // EIA 3216-18
        CASE_B,     // EIA 3528-21
        CASE_C,     // EIA 6032-28
        CASE_D,     // EIA 7343-31
        CASE_E,     // EIA 7343-43

        // Electrolytic Capacitors
        RADIAL_2MM,    // 2mm lead spacing
        RADIAL_2_5MM,  // 2.5mm lead spacing
        RADIAL_5MM,    // 5mm lead spacing
        RADIAL_7_5MM,  // 7.5mm lead spacing

        OTHER;

        public static PackageType fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return OTHER;
            }
        }
    }

    private static final Map<PackageType, PackageDimensions> PACKAGE_DIMENSIONS = new HashMap<>();
    private static final Map<PackageType, PadLayout> PAD_LAYOUTS = new HashMap<>();

    static {
        // Initialize package dimensions
        initializePackageDimensions();
        // Initialize pad layouts
        initializePadLayouts();
    }

    private static void initializePackageDimensions() {
        // SMD Packages
        PACKAGE_DIMENSIONS.put(PackageType.SMD_01005, new PackageDimensions(0.4, 0.2, 0.13, 0, 2, "01005"));
        PACKAGE_DIMENSIONS.put(PackageType.SMD_0201, new PackageDimensions(0.6, 0.3, 0.23, 0, 2, "0201"));
        PACKAGE_DIMENSIONS.put(PackageType.SMD_0402, new PackageDimensions(1.0, 0.5, 0.35, 0, 2, "0402"));
        PACKAGE_DIMENSIONS.put(PackageType.SMD_0603, new PackageDimensions(1.6, 0.8, 0.45, 0, 2, "0603"));
        PACKAGE_DIMENSIONS.put(PackageType.SMD_0805, new PackageDimensions(2.0, 1.25, 0.45, 0, 2, "0805"));
        PACKAGE_DIMENSIONS.put(PackageType.SMD_1206, new PackageDimensions(3.2, 1.6, 0.55, 0, 2, "1206"));
        PACKAGE_DIMENSIONS.put(PackageType.SMD_1210, new PackageDimensions(3.2, 2.5, 0.55, 0, 2, "1210"));

        // SOT Packages
        PACKAGE_DIMENSIONS.put(PackageType.SOT23, new PackageDimensions(2.9, 1.3, 1.1, 0.95, 3, "SOT-23"));
        PACKAGE_DIMENSIONS.put(PackageType.SOT223, new PackageDimensions(6.5, 3.5, 1.6, 2.3, 4, "SOT-223"));
        PACKAGE_DIMENSIONS.put(PackageType.SOT323, new PackageDimensions(2.0, 1.25, 0.95, 0.65, 3, "SOT-323"));

        // SOIC Packages
        PACKAGE_DIMENSIONS.put(PackageType.SOIC, new PackageDimensions(4.9, 3.9, 1.75, 1.27, 8, "SOIC-8"));
        PACKAGE_DIMENSIONS.put(PackageType.TSSOP, new PackageDimensions(4.4, 3.0, 1.2, 0.65, 14, "TSSOP-14"));

        // QFP Packages
        PACKAGE_DIMENSIONS.put(PackageType.TQFP, new PackageDimensions(7.0, 7.0, 1.0, 0.5, 32, "TQFP-32"));
        PACKAGE_DIMENSIONS.put(PackageType.LQFP, new PackageDimensions(10.0, 10.0, 1.4, 0.5, 48, "LQFP-48"));

        // Power Packages
        PACKAGE_DIMENSIONS.put(PackageType.TO220, new PackageDimensions(10.0, 4.5, 4.1, 2.54, 3, "TO-220"));
        PACKAGE_DIMENSIONS.put(PackageType.DPAK, new PackageDimensions(6.5, 6.1, 2.3, 2.28, 3, "TO-252"));
    }

    private static void initializePadLayouts() {
        // SMD Packages
        PAD_LAYOUTS.put(PackageType.SMD_0201, new PadLayout(
                0.3, 0.3, 0.95, 1.0, "+0.05mm", "1:1"
        ));
        PAD_LAYOUTS.put(PackageType.SMD_0402, new PadLayout(
                0.5, 0.5, 1.3, 1.5, "+0.075mm", "1:1"
        ));
        PAD_LAYOUTS.put(PackageType.SMD_0603, new PadLayout(
                0.8, 0.8, 1.8, 2.0, "+0.075mm", "1:1"
        ));

        // SOT23
        PAD_LAYOUTS.put(PackageType.SOT23, new PadLayout(
                0.9, 1.2, 2.3, 3.0, "+0.075mm", "1:1"
        ));

        // SOIC
        PAD_LAYOUTS.put(PackageType.SOIC, new PadLayout(
                0.6, 1.55, 1.27, 6.0, "+0.075mm", "1:1"
        ));

        // TQFP
        PAD_LAYOUTS.put(PackageType.TQFP, new PadLayout(
                0.25, 1.5, 0.5, 9.0, "+0.075mm", "1:1"
        ));
    }

    /**
     * Gets the standardized dimensions for a package type.
     *
     * @param packageType The package type
     * @return PackageDimensions object or null if not defined
     */
    public static PackageDimensions getPackageDimensions(PackageType packageType) {
        return PACKAGE_DIMENSIONS.get(packageType);
    }

    /**
     * Gets the recommended pad layout for a package type.
     *
     * @param packageType The package type
     * @return PadLayout object or null if not defined
     */
    public static PadLayout getPadLayout(PackageType packageType) {
        return PAD_LAYOUTS.get(packageType);
    }

    /**
     * Gets a complete package specification including dimensions and pad layout.
     *
     * @param packageType The package type
     * @return Map containing package specifications
     */
    public static Map<String, Object> getPackageSpecification(PackageType packageType) {
        Map<String, Object> specs = new HashMap<>();

        PackageDimensions dimensions = getPackageDimensions(packageType);
        if (dimensions != null) {
            specs.put("dimensions", dimensions);
        }

        PadLayout padLayout = getPadLayout(packageType);
        if (padLayout != null) {
            specs.put("padLayout", padLayout);
        }

        specs.put("type", packageType.name());
        specs.put("mountingType", getMountingType(packageType));

        return specs;
    }

    private static String getMountingType(PackageType packageType) {
        return switch (packageType) {
            case DIP, SIP, ZIP, TO92, TO220, TO247 -> "Through-hole";
            case RADIAL_2MM, RADIAL_2_5MM, RADIAL_5MM, RADIAL_7_5MM -> "Through-hole";
            default -> "Surface Mount";
        };
    }

    /**
     * Standardizes a package name to a consistent format.
     *
     * @param packageName The package name to standardize
     * @return Standardized package name
     */
    public static String standardizePackageName(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return packageName;
        }

        // Remove spaces and convert to uppercase
        String stdPackage = packageName.trim().toUpperCase().replaceAll("\\s+", "");

        // Check aliases first
        String aliased = PACKAGE_ALIASES.get(stdPackage);
        if (aliased != null) {
            return aliased;
        }

        // Apply specific standardization rules
        stdPackage = standardizeSOIC(stdPackage);
        stdPackage = standardizeSMD(stdPackage);
        stdPackage = standardizeSOT(stdPackage);
        stdPackage = standardizeTO(stdPackage);
        stdPackage = standardizeQFP(stdPackage);

        return stdPackage;
    }

    private static String standardizeSOIC(String packageName) {
        // Handle variations of SOIC packages
        if (packageName.matches("SO[0-9]+")) {
            return "SOIC-" + packageName.substring(2);
        }
        if (packageName.matches("SOIC[0-9]+")) {
            return "SOIC-" + packageName.substring(4);
        }
        if (packageName.equals("SO") || packageName.equals("SOP")) {
            return "SOIC-8"; // Default to 8 pins if not specified
        }
        return packageName;
    }

    private static String standardizeSMD(String packageName) {
        // Convert metric and imperial SMD package names
        if (packageName.matches("[0-9]{4}M?")) {
            return "SMD_" + packageName.replaceAll("M$", "");
        }
        // Convert direct size specifications
        if (packageName.matches("(0201|0402|0603|0805|1206|1210|1812|2010|2512)")) {
            return "SMD_" + packageName;
        }
        return packageName;
    }

    private static String standardizeSOT(String packageName) {
        // Handle SOT package variations
        if (packageName.matches("SOT[0-9]+")) {
            return packageName.replace("SOT", "SOT-");
        }
        // Handle SC package variants (equivalent to some SOT packages)
        if (packageName.equals("SC70")) {
            return "SOT-323";
        }
        if (packageName.equals("SC88")) {
            return "SOT-363";
        }
        return packageName;
    }

    private static String standardizeTO(String packageName) {
        // Handle TO package variations
        if (packageName.matches("TO[0-9]+")) {
            return packageName.replace("TO", "TO-");
        }
        return packageName;
    }

    private static String standardizeQFP(String packageName) {
        // Handle QFP package variations
        if (packageName.matches("(L?T?QFP)[0-9]+")) {
            String prefix = packageName.replaceAll("[0-9]+.*", "");
            String pins = packageName.substring(prefix.length());
            return prefix + "-" + pins;
        }
        return packageName;
    }

    /**
     * Checks if a package name is valid according to standard patterns.
     *
     * @param packageName The package name to validate
     * @return true if the package name is valid
     */
    public static boolean isValidPackageName(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return false;
        }

        String stdPackage = standardizePackageName(packageName);
        PackageType type = determinePackageType(stdPackage);

        // Check if it's a known package type
        if (type != PackageType.OTHER) {
            // Check if dimensions exist for this package type
            if (PACKAGE_DIMENSIONS.containsKey(type)) {
                return true;
            }
        }

        // Check common patterns
        return stdPackage.matches("SOIC-[0-9]+") ||
                stdPackage.matches("SMD_[0-9]{4}") ||
                stdPackage.matches("SOT-[0-9]+") ||
                stdPackage.matches("TO-[0-9]+") ||
                stdPackage.matches("(L?T?QFP)-[0-9]+") ||
                stdPackage.matches("DIP-[0-9]+") ||
                PACKAGE_ALIASES.containsKey(stdPackage);
    }

    // Static initialization of package aliases
    private static final Map<String, String> PACKAGE_ALIASES = new HashMap<>();
    static {
        // SOIC aliases
        PACKAGE_ALIASES.put("SO8", "SOIC-8");
        PACKAGE_ALIASES.put("SO14", "SOIC-14");
        PACKAGE_ALIASES.put("SO16", "SOIC-16");
        PACKAGE_ALIASES.put("SO20", "SOIC-20");

        // SOT aliases
        PACKAGE_ALIASES.put("SOT23", "SOT-23");
        PACKAGE_ALIASES.put("SOT223", "SOT-223");
        PACKAGE_ALIASES.put("SC70", "SOT-323");
        PACKAGE_ALIASES.put("SC88", "SOT-363");

        // TO package aliases
        PACKAGE_ALIASES.put("TO220", "TO-220");
        PACKAGE_ALIASES.put("TO247", "TO-247");
        PACKAGE_ALIASES.put("TO252", "DPAK");
        PACKAGE_ALIASES.put("TO263", "D2PAK");

        // QFP aliases
        PACKAGE_ALIASES.put("TQFP32", "TQFP-32");
        PACKAGE_ALIASES.put("TQFP44", "TQFP-44");
        PACKAGE_ALIASES.put("TQFP64", "TQFP-64");
        PACKAGE_ALIASES.put("LQFP48", "LQFP-48");
        PACKAGE_ALIASES.put("LQFP64", "LQFP-64");

        // SMD aliases
        PACKAGE_ALIASES.put("0201", "SMD_0201");
        PACKAGE_ALIASES.put("0402", "SMD_0402");
        PACKAGE_ALIASES.put("0603", "SMD_0603");
        PACKAGE_ALIASES.put("0805", "SMD_0805");
        PACKAGE_ALIASES.put("1206", "SMD_1206");
        PACKAGE_ALIASES.put("1210", "SMD_1210");
        PACKAGE_ALIASES.put("1812", "SMD_1812");
        PACKAGE_ALIASES.put("2010", "SMD_2010");
        PACKAGE_ALIASES.put("2512", "SMD_2512");
    }

    /**
     * Determines the package type from a package name.
     *
     * @param packageName The package name to analyze
     * @return Detected PackageType
     */
    public static PackageType determinePackageType(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return PackageType.OTHER;
        }

        String stdPackage = standardizePackageName(packageName).toUpperCase();

        // Through-hole packages
        if (stdPackage.startsWith("DIP-")) return PackageType.DIP;
        if (stdPackage.startsWith("SIP-")) return PackageType.SIP;
        if (stdPackage.startsWith("ZIP-")) return PackageType.ZIP;
        if (stdPackage.startsWith("TO-92")) return PackageType.TO92;
        if (stdPackage.startsWith("TO-220")) return PackageType.TO220;
        if (stdPackage.startsWith("TO-247")) return PackageType.TO247;
        if (stdPackage.startsWith("TO-263")) return PackageType.TO263;

        // Surface Mount IC Packages
        if (stdPackage.startsWith("SOIC-")) return PackageType.SOIC;
        if (stdPackage.startsWith("SSOP-")) return PackageType.SSOP;
        if (stdPackage.startsWith("TSSOP-")) return PackageType.TSSOP;
        if (stdPackage.startsWith("MSOP-")) return PackageType.MSOP;
        if (stdPackage.startsWith("HSOP-")) return PackageType.HSOP;
        if (stdPackage.startsWith("QFP-")) return PackageType.QFP;
        if (stdPackage.startsWith("LQFP-")) return PackageType.LQFP;
        if (stdPackage.startsWith("TQFP-")) return PackageType.TQFP;
        if (stdPackage.startsWith("PQFP-")) return PackageType.PQFP;
        if (stdPackage.startsWith("QFN-")) return PackageType.QFN;
        if (stdPackage.startsWith("DFN-")) return PackageType.DFN;
        if (stdPackage.startsWith("SON-")) return PackageType.SON;
        if (stdPackage.startsWith("WSON-")) return PackageType.WSON;
        if (stdPackage.startsWith("BGA")) return PackageType.BGA;
        if (stdPackage.startsWith("FBGA")) return PackageType.FBGA;
        if (stdPackage.startsWith("TFBGA")) return PackageType.TFBGA;
        if (stdPackage.startsWith("LGA-")) return PackageType.LGA;
        if (stdPackage.startsWith("PLCC-")) return PackageType.PLCC;

        // Surface Mount Discrete Packages
        if (stdPackage.equals("SOT-23")) return PackageType.SOT23;
        if (stdPackage.equals("SOT-223")) return PackageType.SOT223;
        if (stdPackage.equals("SOT-323")) return PackageType.SOT323;
        if (stdPackage.equals("SOT-363")) return PackageType.SOT363;
        if (stdPackage.equals("SOT-663")) return PackageType.SOT663;
        if (stdPackage.equals("SOD-123")) return PackageType.SOD123;
        if (stdPackage.equals("SOD-323")) return PackageType.SOD323;
        if (stdPackage.equals("SOD-523")) return PackageType.SOD523;
        if (stdPackage.startsWith("DPAK")) return PackageType.DPAK;
        if (stdPackage.startsWith("D2PAK")) return PackageType.D2PAK;
        if (stdPackage.startsWith("D3PAK")) return PackageType.D3PAK;

        // Surface Mount Passive Packages
        if (stdPackage.equals("SMD_01005")) return PackageType.SMD_01005;
        if (stdPackage.equals("SMD_0201")) return PackageType.SMD_0201;
        if (stdPackage.equals("SMD_0402")) return PackageType.SMD_0402;
        if (stdPackage.equals("SMD_0603")) return PackageType.SMD_0603;
        if (stdPackage.equals("SMD_0805")) return PackageType.SMD_0805;
        if (stdPackage.equals("SMD_1206")) return PackageType.SMD_1206;
        if (stdPackage.equals("SMD_1210")) return PackageType.SMD_1210;
        if (stdPackage.equals("SMD_1812")) return PackageType.SMD_1812;
        if (stdPackage.equals("SMD_2010")) return PackageType.SMD_2010;
        if (stdPackage.equals("SMD_2512")) return PackageType.SMD_2512;

        // Special Packages
        if (stdPackage.equals("MELF")) return PackageType.MELF;
        if (stdPackage.startsWith("MINIMELF")) return PackageType.MINIMELF;
        if (stdPackage.startsWith("MICROMELF")) return PackageType.MICROMELF;
        if (stdPackage.startsWith("DO-214")) return PackageType.DO214;
        if (stdPackage.equals("DO-214AC")) return PackageType.DO214AC;
        if (stdPackage.equals("DO-214AA")) return PackageType.DO214AA;
        if (stdPackage.equals("DO-214AB")) return PackageType.DO214AB;

        // Tantalum Capacitor Cases
        if (stdPackage.equals("CASE-A") || stdPackage.equals("EIA-3216-18")) return PackageType.CASE_A;
        if (stdPackage.equals("CASE-B") || stdPackage.equals("EIA-3528-21")) return PackageType.CASE_B;
        if (stdPackage.equals("CASE-C") || stdPackage.equals("EIA-6032-28")) return PackageType.CASE_C;
        if (stdPackage.equals("CASE-D") || stdPackage.equals("EIA-7343-31")) return PackageType.CASE_D;
        if (stdPackage.equals("CASE-E") || stdPackage.equals("EIA-7343-43")) return PackageType.CASE_E;

        // Electrolytic Capacitor Packages
        if (stdPackage.contains("RADIAL")) {
            if (stdPackage.contains("2MM")) return PackageType.RADIAL_2MM;
            if (stdPackage.contains("2.5MM")) return PackageType.RADIAL_2_5MM;
            if (stdPackage.contains("5MM")) return PackageType.RADIAL_5MM;
            if (stdPackage.contains("7.5MM")) return PackageType.RADIAL_7_5MM;
        }

        // Check aliases
        String aliased = PACKAGE_ALIASES.get(stdPackage);
        if (aliased != null) {
            return determinePackageType(aliased);
        }

        return PackageType.OTHER;
    }
}