package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for JMicron Technology components.
 * Implements specific pattern matching for storage controller ICs.
 *
 * Supported product families:
 * - JMS5xx series - USB to SATA bridge controllers (JMS539, JMS567, JMS578, JMS583)
 * - JMB5xx series - PCIe SATA/NVMe controllers (JMB575, JMB585)
 * - JMB3xx series - SATA port multiplier/host controllers (JMB363, JMB368)
 * - JMF series - Flash memory controllers
 *
 * Package types: QFN, LQFP, BGA
 */
public class JMicronHandler implements ManufacturerHandler {

    // Package code mappings for JMicron devices
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("QFN", "QFN"),
            Map.entry("LQFP", "LQFP"),
            Map.entry("BGA", "BGA"),
            Map.entry("Q", "QFN"),
            Map.entry("L", "LQFP"),
            Map.entry("B", "BGA"),
            Map.entry("T", "TQFP")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // JMS5xx series - USB to SATA bridge controllers
        // JMS539 - USB 3.0 to SATA III
        // JMS567 - USB 3.0 to SATA III with UASP
        // JMS578 - USB 3.1 Gen 1 to SATA III
        // JMS583 - USB 3.1 Gen 2 to SATA III/NVMe
        registry.addPattern(ComponentType.IC, "^JMS5[0-9]{2}[A-Z]*.*");

        // JMB5xx series - PCIe SATA/NVMe controllers
        // JMB575 - 5-port SATA 6Gb/s controller
        // JMB585 - 5-port SATA 6Gb/s PCIe Gen 3 controller
        registry.addPattern(ComponentType.IC, "^JMB5[0-9]{2}[A-Z]*.*");

        // JMB3xx series - SATA port multiplier/host controllers
        // JMB363 - SATA/PATA combo controller
        // JMB368 - PATA controller
        registry.addPattern(ComponentType.IC, "^JMB3[0-9]{2}[A-Z]*.*");

        // JMF series - Flash memory controllers
        // JMF60x, JMF61x, JMF66x series
        registry.addPattern(ComponentType.IC, "^JMF[0-9]+[A-Z]*.*");

        // JMB4xx series - USB bridge controllers (legacy)
        registry.addPattern(ComponentType.IC, "^JMB4[0-9]{2}[A-Z]*.*");

        // JM20xxx series - IDE/SATA controllers (legacy)
        registry.addPattern(ComponentType.IC, "^JM20[0-9]{3}[A-Z]*.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only support IC type for JMicron parts
        if (type != ComponentType.IC) {
            return false;
        }

        // JMS5xx series - USB to SATA bridge
        if (upperMpn.matches("^JMS5[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // JMB5xx series - PCIe SATA/NVMe controllers
        if (upperMpn.matches("^JMB5[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // JMB3xx series - SATA port multiplier/host
        if (upperMpn.matches("^JMB3[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // JMF series - Flash controllers
        if (upperMpn.matches("^JMF[0-9]+[A-Z]*.*")) {
            return true;
        }

        // JMB4xx series - USB bridge controllers
        if (upperMpn.matches("^JMB4[0-9]{2}[A-Z]*.*")) {
            return true;
        }

        // JM20xxx series - IDE/SATA controllers
        if (upperMpn.matches("^JM20[0-9]{3}[A-Z]*.*")) {
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

        // Handle hyphenated package codes (e.g., JMS583-QFN, JMB585-LQFP)
        int hyphen = cleanMpn.indexOf('-');
        if (hyphen > 0 && hyphen < cleanMpn.length() - 1) {
            String suffix = cleanMpn.substring(hyphen + 1);
            if (PACKAGE_CODES.containsKey(suffix)) {
                return PACKAGE_CODES.get(suffix);
            }
            // Check for partial match at start
            for (String key : PACKAGE_CODES.keySet()) {
                if (suffix.startsWith(key)) {
                    return PACKAGE_CODES.get(key);
                }
            }
        }

        // Check for trailing package letter suffix (e.g., JMS583Q for QFN)
        // Look for common package indicators in the suffix
        if (cleanMpn.matches("^JM[SBF][0-9]+[A-Z].*")) {
            // Extract the letter(s) after the numeric part
            int digitEnd = findLastDigitIndex(cleanMpn);
            if (digitEnd >= 0 && digitEnd < cleanMpn.length() - 1) {
                String suffix = cleanMpn.substring(digitEnd + 1);
                // Check longer suffixes first
                if (suffix.length() >= 3) {
                    String threeChar = suffix.substring(0, 3);
                    if (PACKAGE_CODES.containsKey(threeChar)) {
                        return PACKAGE_CODES.get(threeChar);
                    }
                }
                if (suffix.length() >= 2) {
                    String twoChar = suffix.substring(0, 2);
                    if (PACKAGE_CODES.containsKey(twoChar)) {
                        return PACKAGE_CODES.get(twoChar);
                    }
                }
                if (suffix.length() >= 1) {
                    String oneChar = suffix.substring(0, 1);
                    if (PACKAGE_CODES.containsKey(oneChar)) {
                        return PACKAGE_CODES.get(oneChar);
                    }
                }
            }
        }

        return "";
    }

    private int findLastDigitIndex(String s) {
        for (int i = s.length() - 1; i >= 0; i--) {
            if (Character.isDigit(s.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // JMS5xx series - USB to SATA
        if (upperMpn.matches("^JMS5[0-9]{2}.*")) {
            // Extract JMS5xx (e.g., JMS539, JMS567, JMS578, JMS583)
            return upperMpn.substring(0, 6);
        }

        // JMB5xx series - PCIe SATA/NVMe
        if (upperMpn.matches("^JMB5[0-9]{2}.*")) {
            // Extract JMB5xx (e.g., JMB575, JMB585)
            return upperMpn.substring(0, 6);
        }

        // JMB3xx series - SATA port multiplier/host
        if (upperMpn.matches("^JMB3[0-9]{2}.*")) {
            // Extract JMB3xx (e.g., JMB363, JMB368)
            return upperMpn.substring(0, 6);
        }

        // JMB4xx series - USB bridge
        if (upperMpn.matches("^JMB4[0-9]{2}.*")) {
            return upperMpn.substring(0, 6);
        }

        // JMF series - Flash controllers (variable length numbers)
        if (upperMpn.matches("^JMF[0-9]+.*")) {
            // Find end of numeric portion
            int i = 3;
            while (i < upperMpn.length() && Character.isDigit(upperMpn.charAt(i))) {
                i++;
            }
            return upperMpn.substring(0, i);
        }

        // JM20xxx series - IDE/SATA (legacy)
        if (upperMpn.matches("^JM20[0-9]{3}.*")) {
            return upperMpn.substring(0, 7);
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

        // Same series is always compatible (different package variants)
        if (series1.equals(series2)) {
            return true;
        }

        // USB to SATA bridge upgrade paths
        // JMS578/JMS583 can replace JMS567 (USB 3.1 replaces USB 3.0)
        // JMS567 can replace JMS539
        if (isUsbSataBridge(series1) && isUsbSataBridge(series2)) {
            return canReplaceUsbSata(series1, series2);
        }

        // PCIe SATA controllers - JMB585 can replace JMB575
        if ("JMB585".equals(series1) && "JMB575".equals(series2)) {
            return true;  // JMB585 is newer/faster
        }

        return false;
    }

    private boolean isUsbSataBridge(String series) {
        return "JMS539".equals(series) || "JMS567".equals(series) ||
               "JMS578".equals(series) || "JMS583".equals(series);
    }

    private boolean canReplaceUsbSata(String replacement, String original) {
        // Replacement level based on USB generation and features
        int replacementLevel = getUsbSataLevel(replacement);
        int originalLevel = getUsbSataLevel(original);
        return replacementLevel >= originalLevel;
    }

    private int getUsbSataLevel(String series) {
        return switch (series) {
            case "JMS539" -> 1;  // USB 3.0 to SATA III
            case "JMS567" -> 2;  // USB 3.0 to SATA III with UASP
            case "JMS578" -> 3;  // USB 3.1 Gen 1 to SATA III
            case "JMS583" -> 4;  // USB 3.1 Gen 2 to SATA III/NVMe
            default -> 0;
        };
    }

    /**
     * Get the interface type for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the primary interface type (e.g., "USB-SATA", "PCIe-SATA", "SATA", "Flash")
     */
    public String getInterfaceType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        // JMS5xx - USB to SATA bridges
        if (series.startsWith("JMS5")) {
            if ("JMS583".equals(series)) {
                return "USB-SATA/NVMe";  // JMS583 supports NVMe
            }
            return "USB-SATA";
        }

        // JMB5xx - PCIe SATA controllers
        if (series.startsWith("JMB5")) {
            return "PCIe-SATA";
        }

        // JMB3xx - SATA/PATA controllers
        if (series.startsWith("JMB3")) {
            if ("JMB363".equals(series)) {
                return "SATA/PATA";  // Combo controller
            }
            if ("JMB368".equals(series)) {
                return "PATA";  // PATA only
            }
            return "SATA";
        }

        // JMB4xx - USB bridges
        if (series.startsWith("JMB4")) {
            return "USB";
        }

        // JMF - Flash controllers
        if (series.startsWith("JMF")) {
            return "Flash";
        }

        // JM20xxx - IDE/SATA
        if (series.startsWith("JM20")) {
            return "IDE/SATA";
        }

        return "";
    }

    /**
     * Get the USB generation for USB-SATA bridge controllers.
     *
     * @param mpn the manufacturer part number
     * @return the USB generation (e.g., "USB 3.0", "USB 3.1 Gen 1", "USB 3.1 Gen 2")
     *         or empty string if not a USB controller
     */
    public String getUsbGeneration(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "JMS539", "JMS567" -> "USB 3.0";
            case "JMS578" -> "USB 3.1 Gen 1";
            case "JMS583" -> "USB 3.1 Gen 2";
            default -> "";
        };
    }

    /**
     * Get the number of SATA ports supported by the controller.
     *
     * @param mpn the manufacturer part number
     * @return the number of SATA ports (1 for bridges, 5 for JMB5xx, etc.)
     */
    public int getPortCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String series = extractSeries(mpn);

        // USB to SATA bridges - single port
        if (series.startsWith("JMS5")) {
            return 1;
        }

        // JMB575/JMB585 - 5 port SATA controllers
        if ("JMB575".equals(series) || "JMB585".equals(series)) {
            return 5;
        }

        // JMB3xx series - typically 2 ports
        if (series.startsWith("JMB3")) {
            return 2;
        }

        return 0;
    }

    /**
     * Check if the controller supports UASP (USB Attached SCSI Protocol).
     *
     * @param mpn the manufacturer part number
     * @return true if the controller supports UASP
     */
    public boolean supportsUASP(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        String series = extractSeries(mpn);
        // JMS567 and later support UASP
        return "JMS567".equals(series) || "JMS578".equals(series) || "JMS583".equals(series);
    }

    /**
     * Check if the controller supports NVMe.
     *
     * @param mpn the manufacturer part number
     * @return true if the controller supports NVMe
     */
    public boolean supportsNVMe(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        String series = extractSeries(mpn);
        // Only JMS583 supports NVMe
        return "JMS583".equals(series);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
