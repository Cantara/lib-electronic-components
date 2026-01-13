package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for Genesys Logic components.
 * Implements specific pattern matching for USB hubs, card readers, and USB PD controllers.
 *
 * Supported product families:
 * - GL85xx series - USB 2.0 hubs (GL850G, GL852G)
 * - GL35xx series - USB 3.0 hubs (GL3510, GL3520, GL3523)
 * - GL32xx series - Card readers (GL3220, GL3224, GL3227)
 * - GL98xx series - USB PD controllers (GL9801, GL9802)
 */
public class GenesysLogicHandler implements ManufacturerHandler {

    // Package code mappings for Genesys Logic devices
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("Q", "QFN"),        // QFN package
            Map.entry("G", "LQFP"),       // LQFP package (GL850G)
            Map.entry("S", "SSOP"),       // SSOP package
            Map.entry("T", "TQFP"),       // TQFP package
            Map.entry("QFN", "QFN"),      // Explicit QFN
            Map.entry("LQFP", "LQFP"),    // Explicit LQFP
            Map.entry("SSOP", "SSOP")     // Explicit SSOP
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // GL850/GL852 series - USB 2.0 hubs (GL850G, GL852G)
        registry.addPattern(ComponentType.IC, "^GL85[0-9][A-Z].*");

        // GL35xx series - USB 3.0/3.1 hubs (GL3510, GL3520, GL3523, GL3590)
        registry.addPattern(ComponentType.IC, "^GL35[0-9]{2}.*");

        // GL32xx series - Card readers (GL3220, GL3224, GL3227)
        registry.addPattern(ComponentType.IC, "^GL32[0-9]{2}.*");

        // GL98xx series - USB PD controllers (GL9801, GL9802)
        registry.addPattern(ComponentType.IC, "^GL98[0-9]{2}.*");

        // GL36xx series - USB 3.1 Gen 2 hubs
        registry.addPattern(ComponentType.IC, "^GL36[0-9]{2}.*");

        // GL33xx series - USB 3.0 card readers
        registry.addPattern(ComponentType.IC, "^GL33[0-9]{2}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only support IC type for Genesys Logic parts
        if (type != ComponentType.IC) {
            return false;
        }

        // GL850/GL852 series - USB 2.0 hubs (GL850G, GL852G)
        if (upperMpn.matches("^GL85[0-9][A-Z].*")) {
            return true;
        }

        // GL35xx series - USB 3.0/3.1 hubs (GL3510, GL3520, GL3523, GL3590)
        if (upperMpn.matches("^GL35[0-9]{2}.*")) {
            return true;
        }

        // GL32xx series - Card readers (GL3220, GL3224, GL3227)
        if (upperMpn.matches("^GL32[0-9]{2}.*")) {
            return true;
        }

        // GL98xx series - USB PD controllers (GL9801, GL9802)
        if (upperMpn.matches("^GL98[0-9]{2}.*")) {
            return true;
        }

        // GL36xx series - USB 3.1 Gen 2 hubs
        if (upperMpn.matches("^GL36[0-9]{2}.*")) {
            return true;
        }

        // GL33xx series - USB 3.0 card readers
        if (upperMpn.matches("^GL33[0-9]{2}.*")) {
            return true;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Must start with GL to be a valid Genesys Logic part
        if (!upperMpn.startsWith("GL")) {
            return "";
        }

        // Remove -REEL, -TR suffixes
        String cleanMpn = upperMpn.replaceAll("-?(REEL|TR)$", "");

        // Handle hyphenated package codes like GL3523-QFN88 or GL850G-HHY48
        int hyphen = cleanMpn.indexOf('-');
        if (hyphen > 0) {
            // First check if there's a package letter before the hyphen (GL850G-xxx)
            String beforeHyphen = cleanMpn.substring(0, hyphen);
            if (beforeHyphen.matches("^GL[0-9]{3}[A-Z]$")) {
                String suffix = beforeHyphen.substring(beforeHyphen.length() - 1);
                return resolvePackageCode(suffix);
            }

            // Check for explicit package after hyphen (GL3523-QFN88)
            String afterHyphen = cleanMpn.substring(hyphen + 1);
            // Extract package type from suffix (e.g., QFN88 -> QFN)
            String packageType = afterHyphen.replaceAll("[0-9]+$", "");
            if (PACKAGE_CODES.containsKey(packageType)) {
                return PACKAGE_CODES.get(packageType);
            }
            // Only return package type if it looks like a valid package name
            if (packageType.length() <= 4 && packageType.matches("^[A-Z]+$")) {
                return packageType;
            }
            return "";
        }

        // Handle GL850G style (GL + 3 digits + package letter)
        if (cleanMpn.matches("^GL[0-9]{3}[A-Z]$")) {
            String suffix = cleanMpn.substring(cleanMpn.length() - 1);
            return resolvePackageCode(suffix);
        }

        // Handle GL3510-QFN style without numbers (GL + 4 digits + hyphen + package)
        if (cleanMpn.matches("^GL[0-9]{4}$")) {
            // No package suffix on 4-digit parts without hyphen
            return "";
        }

        return "";
    }

    private String resolvePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        if (PACKAGE_CODES.containsKey(suffix)) {
            return PACKAGE_CODES.get(suffix);
        }

        return suffix;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // GL850 series - USB 2.0 4-port hub (e.g., GL850G)
        if (upperMpn.matches("^GL850[A-Z].*")) {
            return "GL850";
        }

        // GL852 series - USB 2.0 4-port hub improved (e.g., GL852G)
        if (upperMpn.matches("^GL852[A-Z].*")) {
            return "GL852";
        }

        // GL3510 series - USB 3.0 4-port hub
        if (upperMpn.matches("^GL3510.*")) {
            return "GL3510";
        }

        // GL3520 series - USB 3.0 4-port hub with MTT
        if (upperMpn.matches("^GL3520.*")) {
            return "GL3520";
        }

        // GL3523 series - USB 3.1 Gen 1 7-port hub
        if (upperMpn.matches("^GL3523.*")) {
            return "GL3523";
        }

        // GL3590 series - USB 3.0 hub
        if (upperMpn.matches("^GL3590.*")) {
            return "GL3590";
        }

        // GL32xx series - Card readers
        if (upperMpn.matches("^GL3220.*")) {
            return "GL3220";
        }
        if (upperMpn.matches("^GL3224.*")) {
            return "GL3224";
        }
        if (upperMpn.matches("^GL3227.*")) {
            return "GL3227";
        }

        // GL33xx series - USB 3.0 card readers
        if (upperMpn.matches("^GL33[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        // GL36xx series - USB 3.1 Gen 2 hubs
        if (upperMpn.matches("^GL36[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        // GL98xx series - USB PD controllers
        if (upperMpn.matches("^GL9801.*")) {
            return "GL9801";
        }
        if (upperMpn.matches("^GL9802.*")) {
            return "GL9802";
        }
        if (upperMpn.matches("^GL98[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        // Generic GL prefix extraction for other series (4-digit)
        if (upperMpn.matches("^GL[0-9]{4}.*")) {
            return upperMpn.substring(0, 6);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || series2.isEmpty()) {
            return false;
        }

        // Same series is always compatible (just different package variants)
        if (series1.equals(series2)) {
            return true;
        }

        // GL852 can replace GL850 (same port count, improved features)
        if (series1.equals("GL852") && series2.equals("GL850")) {
            return true;
        }
        if (series1.equals("GL850") && series2.equals("GL852")) {
            return true;
        }

        // GL3520 can replace GL3510 (same port count, MTT support)
        if (series1.equals("GL3520") && series2.equals("GL3510")) {
            return true;
        }
        if (series1.equals("GL3510") && series2.equals("GL3520")) {
            return true;
        }

        return false;
    }

    /**
     * Get the product type for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the product type (e.g., "USB Hub", "Card Reader", "USB PD Controller")
     */
    public String getProductType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        // USB 2.0 hubs
        if (series.startsWith("GL85")) {
            return "USB 2.0 Hub";
        }

        // USB 3.0/3.1 hubs
        if (series.startsWith("GL35") || series.startsWith("GL36")) {
            return "USB 3.x Hub";
        }

        // Card readers
        if (series.startsWith("GL32") || series.startsWith("GL33")) {
            return "Card Reader";
        }

        // USB PD controllers
        if (series.startsWith("GL98")) {
            return "USB PD Controller";
        }

        return "";
    }

    /**
     * Get the USB version supported by the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the USB version (e.g., "USB 2.0", "USB 3.0", "USB 3.1 Gen 1", "USB 3.1 Gen 2")
     */
    public String getUsbVersion(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        // USB 2.0 hubs
        if (series.startsWith("GL85")) {
            return "USB 2.0";
        }

        // GL35xx series - USB 3.0/3.1 Gen 1
        if (series.equals("GL3510") || series.equals("GL3520")) {
            return "USB 3.0";
        }
        if (series.equals("GL3523") || series.equals("GL3590")) {
            return "USB 3.1 Gen 1";
        }

        // GL36xx series - USB 3.1 Gen 2
        if (series.startsWith("GL36")) {
            return "USB 3.1 Gen 2";
        }

        // Card readers
        if (series.startsWith("GL32")) {
            return "USB 2.0";
        }
        if (series.startsWith("GL33")) {
            return "USB 3.0";
        }

        return "";
    }

    /**
     * Get the number of downstream ports for hub devices.
     *
     * @param mpn the manufacturer part number
     * @return the number of downstream ports (0 if not a hub or unknown)
     */
    public int getPortCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String series = extractSeries(mpn);
        if (series.isEmpty()) return 0;

        return switch (series) {
            case "GL850", "GL852" -> 4;        // 4-port USB 2.0 hub
            case "GL3510", "GL3520" -> 4;      // 4-port USB 3.0 hub
            case "GL3523" -> 7;                // 7-port USB 3.1 hub
            case "GL3590" -> 4;                // 4-port USB 3.0 hub
            default -> 0;
        };
    }

    /**
     * Check if the device supports Multiple Transaction Translator (MTT).
     * MTT allows better handling of multiple USB 2.0 devices behind a USB 3.0 hub.
     *
     * @param mpn the manufacturer part number
     * @return true if MTT is supported
     */
    public boolean supportsMTT(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        String series = extractSeries(mpn);

        // GL3520 and GL3523 support MTT
        return series.equals("GL3520") || series.equals("GL3523");
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
