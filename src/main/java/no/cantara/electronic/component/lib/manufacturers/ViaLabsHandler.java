package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for VIA Labs components.
 * Implements specific pattern matching for USB controllers.
 *
 * Supported product families:
 * - VL7xx series - USB 2.0 hubs (VL750, VL751, VL752)
 * - VL8xx series - USB 3.0 hubs (VL812, VL813, VL817)
 * - VL82x series - USB 3.2 hubs (VL822, VL823)
 * - VL10x series - USB Power Delivery controllers (VL100, VL103)
 */
public class ViaLabsHandler implements ManufacturerHandler {

    // Package code mappings for VIA Labs devices
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("Q", "QFN"),        // QFN package
            Map.entry("QFN", "QFN"),      // QFN explicit
            Map.entry("L", "LQFP"),       // LQFP package
            Map.entry("LQFP", "LQFP"),    // LQFP explicit
            Map.entry("B", "BGA"),        // BGA package
            Map.entry("T", "TQFP")        // TQFP package
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // VL7xx series - USB 2.0 hub controllers
        registry.addPattern(ComponentType.IC, "^VL7[0-9]{2}[A-Z]*.*");

        // VL8xx series - USB 3.0 hub controllers
        registry.addPattern(ComponentType.IC, "^VL8[0-9]{2}[A-Z]*.*");

        // VL82x series - USB 3.2 hub controllers
        registry.addPattern(ComponentType.IC, "^VL82[0-9][A-Z]*.*");

        // VL10x series - USB Power Delivery controllers
        registry.addPattern(ComponentType.IC, "^VL10[0-9][A-Z]*.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only support IC type for VIA Labs parts
        if (type != ComponentType.IC) {
            return false;
        }

        // VL7xx series - USB 2.0 hub controllers
        if (upperMpn.matches("^VL7[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // VL8xx series - USB 3.0 hub controllers
        if (upperMpn.matches("^VL8[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // VL82x series - USB 3.2 hub controllers (also matches VL8xx but more specific)
        if (upperMpn.matches("^VL82[0-9][A-Z]*.*")) {
            return true;
        }

        // VL10x series - USB Power Delivery controllers
        if (upperMpn.matches("^VL10[0-9][A-Z]*.*")) {
            return true;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove -REEL, -TR, -TUBE, -TRAY suffixes
        String cleanMpn = upperMpn.replaceAll("-?(REEL|TR|TUBE|TRAY)$", "");

        // Handle suffix after hyphen (e.g., VL817-Q7 -> Q7 -> QFN)
        int hyphen = cleanMpn.indexOf('-');
        if (hyphen > 0 && hyphen < cleanMpn.length() - 1) {
            String suffix = cleanMpn.substring(hyphen + 1);
            // Try first character of suffix as package code
            if (!suffix.isEmpty()) {
                String firstChar = suffix.substring(0, 1);
                if (PACKAGE_CODES.containsKey(firstChar)) {
                    return PACKAGE_CODES.get(firstChar);
                }
            }
        }

        // Handle trailing suffix without hyphen (e.g., VL817Q)
        if (cleanMpn.matches("^VL[0-9]{3}[A-Z]+.*")) {
            // Extract letters after the 3-digit number
            String suffix = cleanMpn.replaceAll("^VL[0-9]{3}", "");
            if (!suffix.isEmpty()) {
                String firstChar = suffix.substring(0, 1);
                if (PACKAGE_CODES.containsKey(firstChar)) {
                    return PACKAGE_CODES.get(firstChar);
                }
            }
        }

        return "";
    }

    private String resolvePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        // Try exact match first
        if (PACKAGE_CODES.containsKey(suffix)) {
            return PACKAGE_CODES.get(suffix);
        }

        // Try first character
        if (PACKAGE_CODES.containsKey(suffix.substring(0, 1))) {
            return PACKAGE_CODES.get(suffix.substring(0, 1));
        }

        return suffix;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // VL7xx series - USB 2.0 (VL750, VL751, VL752)
        if (upperMpn.matches("^VL750.*")) {
            return "VL750";
        }
        if (upperMpn.matches("^VL751.*")) {
            return "VL751";
        }
        if (upperMpn.matches("^VL752.*")) {
            return "VL752";
        }

        // VL8xx series - USB 3.0 (VL812, VL813, VL815, VL817)
        if (upperMpn.matches("^VL812.*")) {
            return "VL812";
        }
        if (upperMpn.matches("^VL813.*")) {
            return "VL813";
        }
        if (upperMpn.matches("^VL815.*")) {
            return "VL815";
        }
        if (upperMpn.matches("^VL817.*")) {
            return "VL817";
        }

        // VL82x series - USB 3.2 (VL820, VL822, VL823)
        if (upperMpn.matches("^VL820.*")) {
            return "VL820";
        }
        if (upperMpn.matches("^VL822.*")) {
            return "VL822";
        }
        if (upperMpn.matches("^VL823.*")) {
            return "VL823";
        }

        // VL10x series - USB PD (VL100, VL102, VL103)
        if (upperMpn.matches("^VL100.*")) {
            return "VL100";
        }
        if (upperMpn.matches("^VL102.*")) {
            return "VL102";
        }
        if (upperMpn.matches("^VL103.*")) {
            return "VL103";
        }

        // Generic VL7xx, VL8xx, VL10x series fallback
        if (upperMpn.matches("^VL7[0-9]{2}.*")) {
            return upperMpn.substring(0, 5);
        }
        if (upperMpn.matches("^VL8[0-9]{2}.*")) {
            return upperMpn.substring(0, 5);
        }
        if (upperMpn.matches("^VL10[0-9].*")) {
            return upperMpn.substring(0, 5);
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

        // USB 3.0 hub upgrades within VL8xx series
        // VL817 can replace VL812/VL813 (newer, same USB 3.0 spec)
        if (isUsb30HubSeries(series1) && isUsb30HubSeries(series2)) {
            return canReplaceUsb30Hub(series1, series2);
        }

        // USB 3.2 hub upgrades within VL82x series
        if (isUsb32HubSeries(series1) && isUsb32HubSeries(series2)) {
            return canReplaceUsb32Hub(series1, series2);
        }

        // USB 2.0 hubs within VL7xx series
        if (isUsb20HubSeries(series1) && isUsb20HubSeries(series2)) {
            return true; // All USB 2.0 hubs are generally interchangeable
        }

        // Different USB versions are not interchangeable
        return false;
    }

    private boolean isUsb20HubSeries(String series) {
        return "VL750".equals(series) || "VL751".equals(series) || "VL752".equals(series);
    }

    private boolean isUsb30HubSeries(String series) {
        return "VL812".equals(series) || "VL813".equals(series) ||
               "VL815".equals(series) || "VL817".equals(series);
    }

    private boolean isUsb32HubSeries(String series) {
        return "VL820".equals(series) || "VL822".equals(series) || "VL823".equals(series);
    }

    private boolean isUsbPdSeries(String series) {
        return "VL100".equals(series) || "VL102".equals(series) || "VL103".equals(series);
    }

    private boolean canReplaceUsb30Hub(String replacement, String original) {
        // VL817 is the latest USB 3.0 hub and can replace earlier versions
        // VL815 can replace VL812/VL813
        int replacementGen = getUsb30HubGeneration(replacement);
        int originalGen = getUsb30HubGeneration(original);
        return replacementGen >= originalGen;
    }

    private int getUsb30HubGeneration(String series) {
        return switch (series) {
            case "VL812" -> 1;  // First gen USB 3.0 hub
            case "VL813" -> 2;  // Second gen
            case "VL815" -> 3;  // Third gen
            case "VL817" -> 4;  // Latest gen USB 3.0 hub
            default -> 0;
        };
    }

    private boolean canReplaceUsb32Hub(String replacement, String original) {
        // VL823 is the latest USB 3.2 hub
        int replacementGen = getUsb32HubGeneration(replacement);
        int originalGen = getUsb32HubGeneration(original);
        return replacementGen >= originalGen;
    }

    private int getUsb32HubGeneration(String series) {
        return switch (series) {
            case "VL820" -> 1;
            case "VL822" -> 2;
            case "VL823" -> 3;
            default -> 0;
        };
    }

    /**
     * Get the USB version supported by the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the USB version (e.g., "USB 2.0", "USB 3.0", "USB 3.2", "USB PD")
     */
    public String getUsbVersion(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        if (isUsb20HubSeries(series)) {
            return "USB 2.0";
        }
        if (isUsb30HubSeries(series)) {
            return "USB 3.0";
        }
        if (isUsb32HubSeries(series)) {
            return "USB 3.2";
        }
        if (isUsbPdSeries(series)) {
            return "USB PD";
        }

        return "";
    }

    /**
     * Get the product type for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the product type (e.g., "Hub Controller", "PD Controller")
     */
    public String getProductType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        if (isUsb20HubSeries(series) || isUsb30HubSeries(series) || isUsb32HubSeries(series)) {
            return "Hub Controller";
        }
        if (isUsbPdSeries(series)) {
            return "PD Controller";
        }

        return "";
    }

    /**
     * Get the number of downstream ports for hub controllers.
     *
     * @param mpn the manufacturer part number
     * @return the number of downstream ports (typically 4 or 7), or 0 if unknown
     */
    public int getPortCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String series = extractSeries(mpn);
        return switch (series) {
            case "VL750", "VL812", "VL820", "VL822" -> 4;  // 4-port hubs
            case "VL751", "VL813", "VL815", "VL817", "VL823" -> 4;  // Varies, but typically 4
            case "VL752" -> 7;  // 7-port hub
            default -> 0;
        };
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
