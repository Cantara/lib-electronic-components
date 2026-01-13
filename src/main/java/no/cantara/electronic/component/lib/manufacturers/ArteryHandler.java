package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Artery Technology - STM32-compatible ARM Cortex-M4 MCUs.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>AT32F403 series - Cortex-M4, 240MHz, high performance</li>
 *   <li>AT32F413 series - Cortex-M4, 200MHz, mainstream</li>
 *   <li>AT32F415 series - Cortex-M4, USB OTG support</li>
 *   <li>AT32F421 series - Cortex-M4, value line</li>
 *   <li>AT32F423 series - Cortex-M4, enhanced value line</li>
 *   <li>AT32F425 series - Cortex-M4, USB device</li>
 *   <li>AT32F435/437 series - Cortex-M4, high-performance with advanced peripherals</li>
 *   <li>AT32F407 series - Cortex-M4, Ethernet and CAN</li>
 *   <li>AT32WB415 series - Cortex-M4, Bluetooth LE</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>AT32F403AVCT7 - F403 series, 100-pin LQFP, 256KB Flash, industrial temp</li>
 *   <li>AT32F413RCT7 - F413 series, 64-pin LQFP, 256KB Flash, industrial temp</li>
 *   <li>AT32F415CBT7 - F415 series, 48-pin LQFP, 128KB Flash, industrial temp</li>
 *   <li>AT32F421C8T7 - F421 series, 48-pin LQFP, 64KB Flash, industrial temp</li>
 *   <li>AT32F435ZMT7 - F435 series, 144-pin LQFP, 4096KB Flash, industrial temp</li>
 * </ul>
 * <p>
 * MPN Structure: AT32F[series][pin][flash][package][temp]
 * <ul>
 *   <li>Pin codes: T=36-pin, C=48-pin, R=64-pin, V=100-pin, Z=144-pin</li>
 *   <li>Flash codes: 4=16KB, 6=32KB, 8=64KB, B=128KB, C=256KB, D=384KB, E=512KB, G=1024KB, M=4096KB</li>
 *   <li>Package codes: T=LQFP, U=QFN, H=BGA</li>
 *   <li>Temperature: 6=industrial (-40 to 85C), 7=industrial extended (-40 to 105C)</li>
 * </ul>
 */
public class ArteryHandler implements ManufacturerHandler {

    // Pattern for extracting MCU details
    // AT32F403AVCT7 -> family=F, series=403, variant=A, pin=V, flash=C, pkg=T, temp=7
    private static final Pattern MCU_PATTERN =
            Pattern.compile("AT32(F|WB)(\\d{3})([A-Z]?)([A-Z])([0-9A-Z])([A-Z])(\\d)");

    // Simplified pattern for pin code extraction
    private static final Pattern MCU_PIN_PATTERN =
            Pattern.compile("AT32[A-Z]{1,2}\\d{3}[A-Z]?([A-Z])[0-9A-Z][A-Z]\\d");

    // Flash size pattern
    private static final Pattern MCU_FLASH_PATTERN =
            Pattern.compile("AT32[A-Z]{1,2}\\d{3}[A-Z]?[A-Z]([0-9A-Z])[A-Z]\\d");

    // Package code mappings for MCUs
    private static final Map<Character, String> MCU_PACKAGE_CODES = Map.ofEntries(
            Map.entry('T', "LQFP"),
            Map.entry('U', "QFN"),
            Map.entry('H', "BGA")
    );

    // Pin count codes for MCUs
    private static final Map<Character, Integer> MCU_PIN_COUNTS = Map.ofEntries(
            Map.entry('T', 36),
            Map.entry('K', 32),
            Map.entry('C', 48),
            Map.entry('R', 64),
            Map.entry('V', 100),
            Map.entry('Z', 144),
            Map.entry('I', 176)
    );

    // Flash size codes (in KB)
    private static final Map<Character, Integer> MCU_FLASH_SIZES = Map.ofEntries(
            Map.entry('4', 16),
            Map.entry('6', 32),
            Map.entry('8', 64),
            Map.entry('B', 128),
            Map.entry('C', 256),
            Map.entry('D', 384),
            Map.entry('E', 512),
            Map.entry('G', 1024),
            Map.entry('M', 4096)
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // AT32F403 series - Cortex-M4, 240MHz high performance
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F403[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F403[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F407 series - Cortex-M4, Ethernet and CAN
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F407[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F407[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F413 series - Cortex-M4, 200MHz mainstream
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F413[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F413[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F415 series - Cortex-M4, USB OTG
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F415[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F415[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F421 series - Cortex-M4, value line
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F421[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F421[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F423 series - Cortex-M4, enhanced value line
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F423[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F423[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F425 series - Cortex-M4, USB device
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F425[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F425[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F435 series - Cortex-M4, high-performance
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F435[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F435[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32F437 series - Cortex-M4, high-performance with Ethernet
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F437[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F437[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // AT32WB415 series - Cortex-M4, Bluetooth LE
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32WB415[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32WB415[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        // Generic AT32F pattern for any future series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT32F\\d{3}[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
        registry.addPattern(ComponentType.IC, "^AT32F\\d{3}[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MICROCONTROLLER,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check for Artery MCU types
        // AT32F4xx, AT32F421, AT32WB415
        boolean isMCU = upperMpn.matches("^AT32F\\d{3}[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$") ||
                upperMpn.matches("^AT32WB\\d{3}[A-Z]?[A-Z][0-9A-Z][A-Z]\\d$");

        if (type == ComponentType.MICROCONTROLLER || type == ComponentType.IC) {
            return isMCU;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // For AT32, package is encoded in second-to-last character
        // e.g., AT32F403AVCT7 -> T = LQFP
        if (upperMpn.length() < 2) return "";

        char packageChar = upperMpn.charAt(upperMpn.length() - 2);
        String packageType = MCU_PACKAGE_CODES.get(packageChar);

        if (packageType != null) {
            // Try to get pin count from earlier in the MPN
            int pinCount = extractMCUPinCount(upperMpn);
            if (pinCount > 0) {
                return packageType + pinCount;
            }
            return packageType;
        }

        return String.valueOf(packageChar);
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // AT32WB415 series (Bluetooth)
        if (upperMpn.startsWith("AT32WB415")) return "AT32WB415";

        // AT32F4xx series - extract series number
        if (upperMpn.startsWith("AT32F403")) return "AT32F403";
        if (upperMpn.startsWith("AT32F407")) return "AT32F407";
        if (upperMpn.startsWith("AT32F413")) return "AT32F413";
        if (upperMpn.startsWith("AT32F415")) return "AT32F415";
        if (upperMpn.startsWith("AT32F421")) return "AT32F421";
        if (upperMpn.startsWith("AT32F423")) return "AT32F423";
        if (upperMpn.startsWith("AT32F425")) return "AT32F425";
        if (upperMpn.startsWith("AT32F435")) return "AT32F435";
        if (upperMpn.startsWith("AT32F437")) return "AT32F437";

        // Generic fallback for AT32F series
        if (upperMpn.startsWith("AT32F") && upperMpn.length() >= 8) {
            return upperMpn.substring(0, 8);
        }

        return "";
    }

    /**
     * Extract MCU flash size in KB from MPN.
     * <p>
     * Format: AT32F403AVCT7 -> C = 256KB
     * <p>
     * Flash size encoding:
     * <ul>
     *   <li>4 = 16KB</li>
     *   <li>6 = 32KB</li>
     *   <li>8 = 64KB</li>
     *   <li>B = 128KB</li>
     *   <li>C = 256KB</li>
     *   <li>D = 384KB</li>
     *   <li>E = 512KB</li>
     *   <li>G = 1024KB</li>
     *   <li>M = 4096KB</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the flash size in KB, or -1 if not extractable
     */
    public int extractMCUFlashSizeKB(String mpn) {
        if (mpn == null || !mpn.toUpperCase().startsWith("AT32")) return -1;

        String upperMpn = mpn.toUpperCase();

        // Flash code is 3rd character from end (e.g., AT32F403AVCT7 -> C)
        if (upperMpn.length() < 3) return -1;

        char flashCode = upperMpn.charAt(upperMpn.length() - 3);
        Integer flashSize = MCU_FLASH_SIZES.get(flashCode);
        return flashSize != null ? flashSize : -1;
    }

    /**
     * Extract MCU pin count from MPN.
     * <p>
     * Format: AT32F403AVCT7 -> V = 100 pins
     *
     * @param mpn the manufacturer part number
     * @return the pin count, or -1 if not extractable
     */
    public int extractMCUPinCount(String mpn) {
        if (mpn == null || !mpn.toUpperCase().startsWith("AT32")) return -1;

        String upperMpn = mpn.toUpperCase();

        // Pin code is 4th character from end (e.g., AT32F403AVCT7 -> V)
        if (upperMpn.length() < 4) return -1;

        char pinCode = upperMpn.charAt(upperMpn.length() - 4);
        Integer pinCount = MCU_PIN_COUNTS.get(pinCode);
        return pinCount != null ? pinCount : -1;
    }

    /**
     * Extract temperature grade from MPN.
     * <p>
     * Temperature encoding:
     * <ul>
     *   <li>6 = Industrial (-40 to +85C)</li>
     *   <li>7 = Industrial extended (-40 to +105C)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return temperature grade description, or empty string if not extractable
     */
    public String extractTemperatureGrade(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        if (upperMpn.length() < 1) return "";

        char tempCode = upperMpn.charAt(upperMpn.length() - 1);
        return switch (tempCode) {
            case '6' -> "Industrial (-40 to +85C)";
            case '7' -> "Industrial Extended (-40 to +105C)";
            default -> "";
        };
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Must be same series
        String series1 = extractSeries(upper1);
        String series2 = extractSeries(upper2);
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Must have same pin count
        int pins1 = extractMCUPinCount(upper1);
        int pins2 = extractMCUPinCount(upper2);
        if (pins1 != pins2 || pins1 == -1) {
            return false;
        }

        // Same series and pin count = valid replacement
        // Flash size, package type, and temperature differences are acceptable
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
