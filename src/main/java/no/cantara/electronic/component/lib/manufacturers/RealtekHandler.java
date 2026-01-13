package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;

/**
 * Handler for Realtek Semiconductor Corporation ICs.
 *
 * Realtek Product Categories:
 * - ALC2xx/ALC6xx/ALC8xx: Audio Codecs (ALC269, ALC272, ALC662, ALC892, ALC898)
 * - ALC1xxx: High Definition Audio (ALC1150, ALC1200, ALC1220)
 * - ALC5xxx: Mobile Audio Codecs (ALC5640, ALC5682)
 * - RTL81xx: Fast Ethernet Controllers (RTL8101, RTL8102)
 * - RTL81xxE: PCIe Ethernet Controllers (RTL8111E, RTL8168E)
 * - RTL821x: Gigabit PHY (RTL8211, RTL8212)
 * - RTL88xx: WiFi Controllers (RTL8188, RTL8192, RTL8812, RTL8814)
 * - RTL8xxxU: USB WiFi (RTL8188EU, RTL8192EU)
 * - RTD2xxx: Display Controllers
 *
 * Package Code Examples:
 * - GR: QFP (Quad Flat Package)
 * - VB: QFN (Quad Flat No-lead)
 * - CG: QFN (Quad Flat No-lead)
 * - VL: QFN-VL variant
 * - VS: QFN-VS variant
 * - LF: Lead-free variant
 *
 * MPN Format Examples:
 * - ALC892-GR (Audio codec in QFP package)
 * - ALC1220-VB (HD Audio codec in QFN package)
 * - RTL8111H-CG (PCIe Ethernet in QFN package)
 * - RTL8211E-VL-CG (Gigabit PHY with variant and package)
 */
public class RealtekHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ALC2xx - Entry-level Audio Codecs
        registry.addPattern(ComponentType.IC, "^ALC2[0-9]{2}.*");

        // ALC6xx - Mid-range Audio Codecs
        registry.addPattern(ComponentType.IC, "^ALC6[0-9]{2}.*");

        // ALC8xx - High-end Audio Codecs
        registry.addPattern(ComponentType.IC, "^ALC8[0-9]{2}.*");

        // ALC1xxx - High Definition Audio Codecs
        registry.addPattern(ComponentType.IC, "^ALC1[0-9]{3}.*");

        // ALC5xxx - Mobile Audio Codecs
        registry.addPattern(ComponentType.IC, "^ALC5[0-9]{3}.*");

        // RTL810x - Fast Ethernet Controllers
        registry.addPattern(ComponentType.IC, "^RTL810[0-9].*");
        // RTL8111/8168 - Gigabit Ethernet Controllers (PCIe)
        registry.addPattern(ComponentType.IC, "^RTL811[0-9].*");
        registry.addPattern(ComponentType.IC, "^RTL816[0-9].*");

        // RTL821x - Gigabit PHY
        registry.addPattern(ComponentType.IC, "^RTL821[0-9].*");

        // RTL8188/8192 - WiFi Controllers (802.11n, note: these start with 81 but are WiFi!)
        registry.addPattern(ComponentType.IC, "^RTL8188.*");
        registry.addPattern(ComponentType.IC, "^RTL8192.*");
        // RTL88xx - WiFi Controllers (802.11ac)
        registry.addPattern(ComponentType.IC, "^RTL881[0-9].*");
        registry.addPattern(ComponentType.IC, "^RTL882[0-9].*");

        // RTD2xxx - Display Controllers
        registry.addPattern(ComponentType.IC, "^RTD2[0-9]{3}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle hyphen-separated suffixes (most common format)
        // Examples: ALC892-GR, ALC1220-VB, RTL8111H-CG, RTL8211E-VL-CG
        int dashIndex = upperMpn.indexOf('-');
        if (dashIndex > 0 && dashIndex < upperMpn.length() - 1) {
            // Get the last segment after final hyphen (the package code)
            int lastDashIndex = upperMpn.lastIndexOf('-');
            String suffix = upperMpn.substring(lastDashIndex + 1);
            return decodePackageCode(suffix);
        }

        // Handle inline package codes (less common)
        // Try to extract trailing letters that could be package codes
        String suffix = extractTrailingSuffix(upperMpn);
        if (!suffix.isEmpty()) {
            return decodePackageCode(suffix);
        }

        return "";
    }

    private String extractTrailingSuffix(String mpn) {
        // Find where digits end and look for trailing letter suffix
        int i = mpn.length() - 1;
        while (i >= 0 && Character.isLetter(mpn.charAt(i))) {
            i--;
        }
        if (i < mpn.length() - 1 && i >= 3) {
            return mpn.substring(i + 1);
        }
        return "";
    }

    private String decodePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        // Realtek package code mappings
        return switch (suffix.toUpperCase()) {
            case "GR" -> "QFP";           // Quad Flat Package
            case "VB" -> "QFN";           // Quad Flat No-lead
            case "CG" -> "QFN";           // QFN variant (common for Ethernet ICs)
            case "VL" -> "QFN";           // QFN-VL variant
            case "VS" -> "QFN";           // QFN-VS variant
            case "VD" -> "QFN";           // QFN-VD variant
            case "VA" -> "QFN";           // QFN-VA variant
            case "VC" -> "QFN";           // QFN-VC variant
            case "VF" -> "QFN";           // QFN-VF variant
            case "LF" -> "QFN-LF";        // Lead-free QFN
            case "TR" -> "QFN-TR";        // Tape and reel
            case "BR" -> "BGA";           // Ball Grid Array
            case "BG" -> "BGA";           // Ball Grid Array variant
            default -> {
                // Check for partial matches
                if (suffix.startsWith("V")) yield "QFN";
                if (suffix.startsWith("BG") || suffix.startsWith("BR")) yield "BGA";
                if (suffix.startsWith("G")) yield "QFP";
                yield suffix;  // Return raw suffix if unknown
            }
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // ALC series - Audio Codecs
        if (upperMpn.startsWith("ALC")) {
            // ALC1xxx - HD Audio (4 characters: ALC1)
            if (upperMpn.length() >= 4 && upperMpn.charAt(3) == '1') {
                return "ALC1";
            }
            // ALC2xx, ALC6xx, ALC8xx (4 characters: ALC2, ALC6, ALC8)
            if (upperMpn.length() >= 4 && Character.isDigit(upperMpn.charAt(3))) {
                return "ALC" + upperMpn.charAt(3);
            }
            // ALC5xxx - Mobile Audio (4 characters: ALC5)
            if (upperMpn.length() >= 4 && upperMpn.charAt(3) == '5') {
                return "ALC5";
            }
        }

        // RTL series - Network and WiFi ICs
        if (upperMpn.startsWith("RTL")) {
            // RTL8188/8192 - WiFi Controllers (check BEFORE RTL81xx to handle special cases)
            // These start with RTL81 but are WiFi, not Ethernet
            if (upperMpn.startsWith("RTL8188") || upperMpn.startsWith("RTL8192")) {
                return "RTL88";  // Group with WiFi series
            }
            // RTL810x/811x/816x - Ethernet Controllers (5 characters: RTL81)
            if (upperMpn.length() >= 5 && upperMpn.startsWith("RTL81")) {
                return "RTL81";
            }
            // RTL821x - Gigabit PHY (5 characters: RTL82)
            if (upperMpn.length() >= 5 && upperMpn.startsWith("RTL82")) {
                return "RTL82";
            }
            // RTL88xx - WiFi Controllers (5 characters: RTL88)
            if (upperMpn.length() >= 5 && upperMpn.startsWith("RTL88")) {
                return "RTL88";
            }
        }

        // RTD series - Display Controllers
        if (upperMpn.startsWith("RTD")) {
            // RTD2xxx (4 characters: RTD2)
            if (upperMpn.length() >= 4 && upperMpn.charAt(3) == '2') {
                return "RTD2";
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || series2.isEmpty()) return false;

        // Parts must be from the same series family to be considered replacements
        if (!series1.equals(series2)) {
            return false;
        }

        // Extract base part numbers (without package codes)
        String base1 = extractBasePartNumber(mpn1);
        String base2 = extractBasePartNumber(mpn2);

        // Same base part with different packages are replacements
        if (base1.equals(base2)) {
            return true;
        }

        // Check for known compatible parts within series
        return isCompatibleWithinSeries(series1, base1, base2);
    }

    private String extractBasePartNumber(String mpn) {
        String upper = mpn.toUpperCase();

        // Remove package suffix (after hyphen)
        int dashIndex = upper.indexOf('-');
        if (dashIndex > 0) {
            return upper.substring(0, dashIndex);
        }

        // Remove trailing letter suffix if present
        int i = upper.length() - 1;
        while (i > 3 && Character.isLetter(upper.charAt(i))) {
            i--;
        }
        if (i < upper.length() - 1 && i > 3) {
            return upper.substring(0, i + 1);
        }

        return upper;
    }

    private boolean isCompatibleWithinSeries(String series, String base1, String base2) {
        // Audio codec compatibility - higher models can often replace lower ones
        // within the same generation
        if (series.equals("ALC8")) {
            // ALC892 and ALC898 are pin-compatible in many designs
            if ((base1.equals("ALC892") && base2.equals("ALC898")) ||
                (base1.equals("ALC898") && base2.equals("ALC892"))) {
                return true;
            }
        }

        if (series.equals("ALC1")) {
            // ALC1150 and ALC1200 have similar pinouts
            if ((base1.equals("ALC1150") && base2.equals("ALC1200")) ||
                (base1.equals("ALC1200") && base2.equals("ALC1150"))) {
                return true;
            }
            // ALC1200 and ALC1220 are often compatible
            if ((base1.equals("ALC1200") && base2.equals("ALC1220")) ||
                (base1.equals("ALC1220") && base2.equals("ALC1200"))) {
                return true;
            }
        }

        if (series.equals("RTL81")) {
            // RTL8111 variants (E, F, G, H) are typically backward compatible
            if (base1.startsWith("RTL8111") && base2.startsWith("RTL8111")) {
                return true;
            }
            // RTL8168 variants are similar
            if (base1.startsWith("RTL8168") && base2.startsWith("RTL8168")) {
                return true;
            }
        }

        if (series.equals("RTL82")) {
            // RTL8211 variants (E, F, etc.) are typically compatible
            if (base1.startsWith("RTL8211") && base2.startsWith("RTL8211")) {
                return true;
            }
        }

        if (series.equals("RTL88")) {
            // WiFi chips - same 802.11 standard are often compatible
            // RTL8188 variants
            if (base1.startsWith("RTL8188") && base2.startsWith("RTL8188")) {
                return true;
            }
            // RTL8192 variants
            if (base1.startsWith("RTL8192") && base2.startsWith("RTL8192")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct check for IC type - the main type for Realtek ICs
        if (type == ComponentType.IC) {
            // ALC Audio Codecs
            if (upperMpn.matches("^ALC[0-9]{3,4}.*")) {
                return true;
            }
            // RTL Ethernet Controllers
            if (upperMpn.matches("^RTL81[0-9]{2}.*")) {
                return true;
            }
            // RTL Gigabit PHY
            if (upperMpn.matches("^RTL82[0-9]{2}.*")) {
                return true;
            }
            // RTL WiFi Controllers
            if (upperMpn.matches("^RTL88[0-9]{2}.*")) {
                return true;
            }
            // RTD Display Controllers
            if (upperMpn.matches("^RTD2[0-9]{3}.*")) {
                return true;
            }
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods for product type identification

    /**
     * Check if MPN is an audio codec (ALC series)
     */
    public boolean isAudioCodec(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.startsWith("ALC");
    }

    /**
     * Check if MPN is a network controller (RTL81xx, RTL82xx, excluding WiFi chips)
     */
    public boolean isNetworkController(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        // Exclude RTL8188 and RTL8192 which are WiFi, not Ethernet
        if (upper.startsWith("RTL8188") || upper.startsWith("RTL8192")) {
            return false;
        }
        return upper.startsWith("RTL81") || upper.startsWith("RTL82");
    }

    /**
     * Check if MPN is a WiFi controller (RTL88xx, RTL8188, RTL8192)
     */
    public boolean isWiFiController(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        // RTL8188 and RTL8192 are WiFi, even though they start with RTL81
        return upper.startsWith("RTL88") ||
               upper.startsWith("RTL8188") ||
               upper.startsWith("RTL8192");
    }

    /**
     * Check if MPN is a display controller (RTD series)
     */
    public boolean isDisplayController(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.startsWith("RTD");
    }

    /**
     * Get the audio codec generation for ALC parts
     * @return generation string (e.g., "HD", "Mobile", "Standard")
     */
    public String getAudioCodecGeneration(String mpn) {
        if (mpn == null) return "";
        String upper = mpn.toUpperCase();

        if (upper.startsWith("ALC1")) {
            return "HD";  // High Definition Audio
        }
        if (upper.startsWith("ALC5")) {
            return "Mobile";  // Mobile Audio Codecs
        }
        if (upper.startsWith("ALC8") || upper.startsWith("ALC6") || upper.startsWith("ALC2")) {
            return "Standard";  // Standard Audio Codecs
        }
        return "";
    }

    /**
     * Get the network interface type for RTL parts
     * @return interface type (e.g., "Fast Ethernet", "Gigabit", "WiFi")
     */
    public String getNetworkInterfaceType(String mpn) {
        if (mpn == null) return "";
        String upper = mpn.toUpperCase();

        // Check WiFi FIRST (RTL8188 and RTL8192 start with RTL81 but are WiFi)
        if (upper.startsWith("RTL8188") || upper.startsWith("RTL8192") || upper.startsWith("RTL88")) {
            return "WiFi";  // Wireless LAN
        }
        if (upper.startsWith("RTL810")) {
            return "Fast Ethernet";  // 10/100 Mbps
        }
        if (upper.startsWith("RTL811") || upper.startsWith("RTL816")) {
            return "Gigabit Ethernet";  // 10/100/1000 Mbps
        }
        if (upper.startsWith("RTL821")) {
            return "Gigabit PHY";  // Physical layer transceiver
        }
        return "";
    }
}
