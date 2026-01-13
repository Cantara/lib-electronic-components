package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for Prolific Technology components.
 * Implements specific pattern matching for USB interface ICs.
 *
 * Supported product families:
 * - PL23xx series - USB to serial (PL2303, PL2312)
 * - PL25xx series - USB bridge (PL2501, PL2571)
 * - PL27xx series - USB hub (PL2734, PL2773)
 * - PL38xx series - USB to parallel
 */
public class ProlificHandler implements ManufacturerHandler {

    // Package code mappings for Prolific devices
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("S", "SOP"),           // SOP package
            Map.entry("SOP", "SOP"),         // SOP package (explicit)
            Map.entry("SS", "SSOP"),         // SSOP package
            Map.entry("SSO", "SSOP"),        // SSOP variant
            Map.entry("SSOP", "SSOP"),       // SSOP package (explicit)
            Map.entry("Q", "QFN"),           // QFN package
            Map.entry("QFN", "QFN"),         // QFN package
            Map.entry("HX", "SSOP"),         // HX variant (SSOP)
            Map.entry("HXA", "SSOP"),        // HXA variant (SSOP)
            Map.entry("HXD", "SSOP"),        // HXD variant (SSOP)
            Map.entry("TA", "SSOP"),         // TA variant (SSOP-28)
            Map.entry("GT", "QFN"),          // GT variant (QFN)
            Map.entry("GC", "QFN"),          // GC variant (QFN)
            Map.entry("GL", "QFN"),          // GL variant (QFN)
            Map.entry("RA", "SOP"),          // RA variant (SOP)
            Map.entry("SA", "SOP"),          // SA variant (SOP)
            Map.entry("TSS", "TSSOP"),       // TSSOP package
            Map.entry("TSSOP", "TSSOP")      // TSSOP package
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // PL23xx series - USB to serial converters
        registry.addPattern(ComponentType.IC, "^PL23[0-9]{2}[A-Z]*.*");

        // PL25xx series - USB bridge ICs
        registry.addPattern(ComponentType.IC, "^PL25[0-9]{2}[A-Z]*.*");

        // PL27xx series - USB hub controllers
        registry.addPattern(ComponentType.IC, "^PL27[0-9]{2}[A-Z]*.*");

        // PL38xx series - USB to parallel converters
        registry.addPattern(ComponentType.IC, "^PL38[0-9]{2}[A-Z]*.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only support IC type for Prolific parts
        if (type != ComponentType.IC) {
            return false;
        }

        // PL23xx series - USB to serial (PL2303, PL2312, etc.)
        if (upperMpn.matches("^PL23[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // PL25xx series - USB bridge (PL2501, PL2571, etc.)
        if (upperMpn.matches("^PL25[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // PL27xx series - USB hub (PL2734, PL2773, etc.)
        if (upperMpn.matches("^PL27[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // PL38xx series - USB to parallel
        if (upperMpn.matches("^PL38[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove -REEL, -TUBE, -TRAY, -TR suffixes
        String cleanMpn = upperMpn.replaceAll("-?(REEL|TUBE|TRAY|TR)$", "");

        // Handle suffix after hyphen (e.g., PL2303HXA-SSOP becomes PL2303HXA)
        int hyphen = cleanMpn.indexOf('-');
        if (hyphen > 0) {
            // Check if part after hyphen is a package code
            String afterHyphen = cleanMpn.substring(hyphen + 1);
            if (PACKAGE_CODES.containsKey(afterHyphen)) {
                return PACKAGE_CODES.get(afterHyphen);
            }
            // Otherwise strip hyphen and continue
            cleanMpn = cleanMpn.substring(0, hyphen);
        }

        // PL23xx series (PL2303, PL2303HX, PL2303HXA, PL2303TA, etc.)
        if (cleanMpn.matches("^PL23[0-9]{2}[A-Z]*")) {
            String suffix = cleanMpn.substring(6); // After "PL2303"
            return resolvePackageCode(suffix);
        }

        // PL25xx series (PL2501, PL2571, etc.)
        if (cleanMpn.matches("^PL25[0-9]{2}[A-Z]*")) {
            String suffix = cleanMpn.substring(6); // After "PL25xx"
            return resolvePackageCode(suffix);
        }

        // PL27xx series (PL2734, PL2773, etc.)
        if (cleanMpn.matches("^PL27[0-9]{2}[A-Z]*")) {
            String suffix = cleanMpn.substring(6); // After "PL27xx"
            return resolvePackageCode(suffix);
        }

        // PL38xx series (PL3805, etc.)
        if (cleanMpn.matches("^PL38[0-9]{2}[A-Z]*")) {
            String suffix = cleanMpn.substring(6); // After "PL38xx"
            return resolvePackageCode(suffix);
        }

        return "";
    }

    private String resolvePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        // Try exact match first
        if (PACKAGE_CODES.containsKey(suffix)) {
            return PACKAGE_CODES.get(suffix);
        }

        // Try progressively shorter prefixes
        for (int len = Math.min(suffix.length(), 4); len >= 1; len--) {
            String prefix = suffix.substring(0, len);
            if (PACKAGE_CODES.containsKey(prefix)) {
                return PACKAGE_CODES.get(prefix);
            }
        }

        // Return suffix if no mapping found
        return suffix.isEmpty() ? "" : suffix;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // PL2303 series - USB to serial (most common)
        if (upperMpn.matches("^PL2303.*")) {
            return "PL2303";
        }

        // PL2312 series - USB to serial (enhanced)
        if (upperMpn.matches("^PL2312.*")) {
            return "PL2312";
        }

        // Other PL23xx series
        if (upperMpn.matches("^PL23[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        // PL2501 series - USB bridge
        if (upperMpn.matches("^PL2501.*")) {
            return "PL2501";
        }

        // PL2571 series - USB to IDE bridge
        if (upperMpn.matches("^PL2571.*")) {
            return "PL2571";
        }

        // Other PL25xx series
        if (upperMpn.matches("^PL25[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        // PL2734 series - USB 2.0 hub (4-port)
        if (upperMpn.matches("^PL2734.*")) {
            return "PL2734";
        }

        // PL2773 series - USB 3.0 hub
        if (upperMpn.matches("^PL2773.*")) {
            return "PL2773";
        }

        // Other PL27xx series
        if (upperMpn.matches("^PL27[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        // PL38xx series - USB to parallel
        if (upperMpn.matches("^PL38[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Same series with different package is typically a valid replacement
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || series2.isEmpty()) {
            return false;
        }

        // Same series is always compatible (just different package variants)
        if (series1.equals(series2)) {
            return true;
        }

        // PL2303 variants are generally compatible
        if (isPL2303Family(mpn1) && isPL2303Family(mpn2)) {
            return true;
        }

        // Different series are not interchangeable
        return false;
    }

    private boolean isPL2303Family(String mpn) {
        return mpn != null && mpn.toUpperCase().matches("^PL2303.*");
    }

    /**
     * Get the USB interface type for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the USB interface type (e.g., "Serial", "Bridge", "Hub", "Parallel")
     */
    public String getInterfaceType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        // PL23xx - USB to serial
        if (series.startsWith("PL23")) {
            return "Serial";
        }

        // PL25xx - USB bridge
        if (series.startsWith("PL25")) {
            if (series.equals("PL2571")) {
                return "IDE Bridge";
            }
            return "Bridge";
        }

        // PL27xx - USB hub
        if (series.startsWith("PL27")) {
            return "Hub";
        }

        // PL38xx - USB to parallel
        if (series.startsWith("PL38")) {
            return "Parallel";
        }

        return "";
    }

    /**
     * Get the USB version supported by the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the USB version (e.g., "USB 1.1", "USB 2.0", "USB 3.0")
     */
    public String getUsbVersion(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        // PL2773 is USB 3.0
        if (series.equals("PL2773")) {
            return "USB 3.0";
        }

        // Most PL23xx, PL25xx, PL27xx are USB 2.0
        if (series.startsWith("PL23") || series.startsWith("PL25") || series.startsWith("PL27")) {
            return "USB 2.0";
        }

        // PL38xx series
        if (series.startsWith("PL38")) {
            return "USB 2.0";
        }

        return "";
    }

    /**
     * Get the PL2303 variant type (for USB to serial chips).
     *
     * @param mpn the manufacturer part number
     * @return the variant (e.g., "Standard", "HX", "HXA", "HXD", "TA", "GT", "GL", "GC")
     */
    public String getPL2303Variant(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (!upperMpn.startsWith("PL2303")) {
            return "";
        }

        String suffix = upperMpn.substring(6);
        // Remove package and reel suffixes
        suffix = suffix.replaceAll("-?(REEL|TUBE|TRAY|TR|SSOP|QFN|SOP).*", "");

        if (suffix.isEmpty()) {
            return "Standard";
        }

        // Check for known variants (longer variants first)
        if (suffix.startsWith("HXD")) return "HXD";
        if (suffix.startsWith("HXA")) return "HXA";
        if (suffix.startsWith("HX")) return "HX";
        if (suffix.startsWith("GT")) return "GT";
        if (suffix.startsWith("GL")) return "GL";
        if (suffix.startsWith("GC")) return "GC";
        if (suffix.startsWith("TA")) return "TA";
        if (suffix.startsWith("RA")) return "RA";
        if (suffix.startsWith("SA")) return "SA";

        return suffix;
    }

    /**
     * Get the number of ports for USB hub controllers.
     *
     * @param mpn the manufacturer part number
     * @return the number of downstream ports (0 if not a hub)
     */
    public int getHubPortCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String series = extractSeries(mpn);

        // PL2734 - 4-port hub
        if (series.equals("PL2734")) {
            return 4;
        }

        // PL2773 - 4-port USB 3.0 hub
        if (series.equals("PL2773")) {
            return 4;
        }

        return 0;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
