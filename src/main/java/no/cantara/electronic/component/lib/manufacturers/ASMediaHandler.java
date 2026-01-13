package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for ASMedia Technology components.
 * Implements specific pattern matching for USB and SATA controller ICs.
 *
 * Supported product families:
 * - ASM1xxx series - USB 3.x hub controllers (ASM1042, ASM1142, ASM1153, etc.)
 * - ASM2xxx series - SATA controllers (ASM2362, ASM2364, etc.)
 * - ASM3xxx series - USB4/Thunderbolt controllers (ASM3242, etc.)
 * - ASM107x series - USB to SATA bridge controllers
 *
 * MPN Pattern: ASM[0-9]{4}[A-Z]*
 * Common suffixes indicate package type and revision.
 */
public class ASMediaHandler implements ManufacturerHandler {

    // Package code mappings for ASMedia devices
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("QFN", "QFN"),
            Map.entry("BGA", "BGA"),
            Map.entry("LQFP", "LQFP"),
            Map.entry("Q", "QFN"),
            Map.entry("B", "BGA"),
            Map.entry("L", "LQFP")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ASM1xxx series - USB 3.x hub controllers
        // ASM1042 - PCIe to USB 3.0 host controller
        // ASM1142 - PCIe to USB 3.1 Gen2 host controller
        // ASM1153 - USB 3.0 to SATA bridge (single port)
        // ASM1156 - USB 3.0 to SATA bridge
        registry.addPattern(ComponentType.IC, "^ASM1[0-9]{3}[A-Z]*.*");

        // ASM2xxx series - SATA controllers
        // ASM2362 - PCIe to NVMe/SATA bridge
        // ASM2364 - USB 3.2 Gen2x2 to NVMe bridge
        registry.addPattern(ComponentType.IC, "^ASM2[0-9]{3}[A-Z]*.*");

        // ASM3xxx series - USB4/Thunderbolt controllers
        // ASM3242 - USB4 controller
        registry.addPattern(ComponentType.IC, "^ASM3[0-9]{3}[A-Z]*.*");

        // Generic pattern for all ASM parts
        registry.addPattern(ComponentType.IC, "^ASM[0-9]{4}[A-Z]*.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only support IC type for ASMedia parts
        if (type != ComponentType.IC) {
            return false;
        }

        // ASM1xxx series - USB 3.x controllers and bridges
        if (upperMpn.matches("^ASM1[0-9]{3}[A-Z]*.*")) {
            return true;
        }

        // ASM2xxx series - SATA controllers
        if (upperMpn.matches("^ASM2[0-9]{3}[A-Z]*.*")) {
            return true;
        }

        // ASM3xxx series - USB4/Thunderbolt controllers
        if (upperMpn.matches("^ASM3[0-9]{3}[A-Z]*.*")) {
            return true;
        }

        // Generic ASM pattern
        if (upperMpn.matches("^ASM[0-9]{4}[A-Z]*.*")) {
            return true;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove common suffixes like -REEL, -TRAY, -TR
        String cleanMpn = upperMpn.replaceAll("-?(REEL|TRAY|TR)$", "");

        // Check for explicit package suffix after hyphen (e.g., ASM1042-QFN)
        int hyphen = cleanMpn.indexOf('-');
        if (hyphen > 0 && hyphen < cleanMpn.length() - 1) {
            String suffix = cleanMpn.substring(hyphen + 1);
            if (PACKAGE_CODES.containsKey(suffix)) {
                return PACKAGE_CODES.get(suffix);
            }
            // Try matching suffix to known package types
            for (Map.Entry<String, String> entry : PACKAGE_CODES.entrySet()) {
                if (suffix.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }

        // Extract trailing letters after the 4-digit part number
        // Pattern: ASM1042A -> A suffix
        if (cleanMpn.matches("^ASM[0-9]{4}[A-Z]+.*")) {
            int digitEnd = 7; // Position after "ASM1042"
            if (cleanMpn.length() > digitEnd) {
                String suffix = cleanMpn.substring(digitEnd);
                // Remove any trailing non-package characters
                suffix = suffix.replaceAll("[0-9].*", "");
                if (!suffix.isEmpty() && PACKAGE_CODES.containsKey(suffix.substring(0, 1))) {
                    return PACKAGE_CODES.get(suffix.substring(0, 1));
                }
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // ASM1xxx series - USB 3.x controllers
        if (upperMpn.matches("^ASM10[0-9]{2}.*")) {
            // ASM1042, ASM1074, etc.
            return extractSeriesNumber(upperMpn, "ASM10");
        }

        if (upperMpn.matches("^ASM11[0-9]{2}.*")) {
            // ASM1142, ASM1153, ASM1156, etc.
            return extractSeriesNumber(upperMpn, "ASM11");
        }

        // ASM2xxx series - SATA/NVMe controllers
        if (upperMpn.matches("^ASM2[0-9]{3}.*")) {
            return extractSeriesNumber(upperMpn, "ASM2");
        }

        // ASM3xxx series - USB4/Thunderbolt
        if (upperMpn.matches("^ASM3[0-9]{3}.*")) {
            return extractSeriesNumber(upperMpn, "ASM3");
        }

        // Generic ASM part
        if (upperMpn.matches("^ASM[0-9]{4}.*")) {
            return upperMpn.substring(0, 7); // Return ASM + 4 digits
        }

        return "";
    }

    private String extractSeriesNumber(String mpn, String prefix) {
        // Extract the base series (e.g., "ASM1042" from "ASM1042A-QFN")
        if (mpn.length() >= 7) {
            return mpn.substring(0, 7);
        }
        return mpn.replaceAll("[^A-Z0-9].*", "");
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

        // Check for known upgrade paths
        // ASM1042 -> ASM1142 (USB 3.0 -> USB 3.1 Gen2)
        if (canUpgrade(series1, series2)) {
            return true;
        }

        return false;
    }

    private boolean canUpgrade(String replacement, String original) {
        // USB 3.1 Gen2 can replace USB 3.0 (backward compatible)
        if ("ASM1142".equals(replacement) && "ASM1042".equals(original)) {
            return true;
        }

        // Same generation bridges are compatible
        if (isSameBridgeFamily(replacement, original)) {
            return true;
        }

        return false;
    }

    private boolean isSameBridgeFamily(String series1, String series2) {
        // USB to SATA bridge family: ASM1153, ASM1156
        if (series1.matches("ASM115[0-9]") && series2.matches("ASM115[0-9]")) {
            return true;
        }
        return false;
    }

    /**
     * Get the controller type for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the controller type (e.g., "USB Host", "USB to SATA", "NVMe Bridge")
     */
    public String getControllerType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "ASM1042" -> "USB 3.0 Host Controller";
            case "ASM1074" -> "USB 3.0 Hub Controller";
            case "ASM1142" -> "USB 3.1 Gen2 Host Controller";
            case "ASM1153" -> "USB 3.0 to SATA Bridge";
            case "ASM1156" -> "USB 3.0 to SATA Bridge";
            case "ASM2362" -> "PCIe to NVMe/SATA Bridge";
            case "ASM2364" -> "USB 3.2 Gen2x2 to NVMe Bridge";
            case "ASM3242" -> "USB4 Controller";
            default -> {
                if (series.startsWith("ASM1")) yield "USB Controller";
                if (series.startsWith("ASM2")) yield "Storage Controller";
                if (series.startsWith("ASM3")) yield "USB4/TB Controller";
                yield "";
            }
        };
    }

    /**
     * Get the USB version supported by the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the USB version (e.g., "USB 3.0", "USB 3.1 Gen2", "USB4")
     */
    public String getUsbVersion(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "ASM1042", "ASM1074", "ASM1153", "ASM1156" -> "USB 3.0";
            case "ASM1142", "ASM1143" -> "USB 3.1 Gen2";
            case "ASM2362" -> "N/A (PCIe)";
            case "ASM2364" -> "USB 3.2 Gen2x2";
            case "ASM3242" -> "USB4";
            default -> {
                if (series.startsWith("ASM10")) yield "USB 3.0";
                if (series.startsWith("ASM11")) yield "USB 3.1";
                if (series.startsWith("ASM3")) yield "USB4";
                yield "";
            }
        };
    }

    /**
     * Get the interface type for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the interface type (e.g., "PCIe to USB", "USB to SATA")
     */
    public String getInterfaceType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "ASM1042", "ASM1142" -> "PCIe to USB";
            case "ASM1074" -> "USB Hub";
            case "ASM1153", "ASM1156" -> "USB to SATA";
            case "ASM2362" -> "PCIe to NVMe/SATA";
            case "ASM2364" -> "USB to NVMe";
            case "ASM3242" -> "USB4/Thunderbolt";
            default -> {
                if (series.startsWith("ASM10")) yield "USB Controller";
                if (series.startsWith("ASM11")) yield "USB Bridge";
                if (series.startsWith("ASM2")) yield "Storage Bridge";
                if (series.startsWith("ASM3")) yield "USB4";
                yield "";
            }
        };
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
