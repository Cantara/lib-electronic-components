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
 * Handler for Holtek Semiconductor - MCUs, Touch Controllers, LCD Drivers, and Voice ICs.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>HT66Fxxxx - Flash MCU (general purpose)</li>
 *   <li>HT67Fxxxx - Flash MCU (with LCD driver)</li>
 *   <li>HT68Fxxxx - Flash MCU (enhanced I/O)</li>
 *   <li>HT45Fxxxx - Touch Key MCU</li>
 *   <li>BS83xxxx - Capacitive Touch MCU (Body Sensor)</li>
 *   <li>HT82V739 - Voice/Audio IC</li>
 *   <li>HT42B534 - USB to UART Bridge IC</li>
 *   <li>HT1621 - LCD Controller Driver</li>
 *   <li>HT1628 - LCD Controller Driver (enhanced)</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>HT66F0185 - Flash MCU, general purpose</li>
 *   <li>HT67F5640 - Flash MCU with LCD driver</li>
 *   <li>HT68F002 - Flash MCU, enhanced I/O</li>
 *   <li>HT45F0072 - Touch Key MCU</li>
 *   <li>BS83B16A-3 - Capacitive Touch MCU, 16-key</li>
 *   <li>HT82V739 - Voice synthesis IC</li>
 *   <li>HT42B534-1 - USB to UART Bridge</li>
 *   <li>HT1621B - 32x4 LCD Controller</li>
 *   <li>HT1628 - LED/LCD Controller Driver</li>
 * </ul>
 * <p>
 * Package codes:
 * <ul>
 *   <li>SS - SSOP package</li>
 *   <li>SOP - SOP package</li>
 *   <li>NS - NSOP package</li>
 *   <li>QF - QFP package</li>
 *   <li>-3 suffix - typically indicates SSOP</li>
 *   <li>-1 suffix - typically indicates SOP</li>
 * </ul>
 */
public class HoltekHandler implements ManufacturerHandler {

    // Pattern for Flash MCU series (HT66F, HT67F, HT68F)
    private static final Pattern FLASH_MCU_PATTERN =
            Pattern.compile("^HT6[678]F\\d+.*", Pattern.CASE_INSENSITIVE);

    // Pattern for Touch MCU series (HT45F, BS83)
    private static final Pattern TOUCH_MCU_PATTERN =
            Pattern.compile("^(HT45F\\d+|BS83[A-Z]?\\d+).*", Pattern.CASE_INSENSITIVE);

    // Pattern for Voice/Audio IC (HT82V)
    private static final Pattern VOICE_IC_PATTERN =
            Pattern.compile("^HT82V\\d+.*", Pattern.CASE_INSENSITIVE);

    // Pattern for USB IC (HT42B)
    private static final Pattern USB_IC_PATTERN =
            Pattern.compile("^HT42B\\d+.*", Pattern.CASE_INSENSITIVE);

    // Pattern for LCD Driver (HT16xx)
    private static final Pattern LCD_DRIVER_PATTERN =
            Pattern.compile("^HT16\\d{2}[A-Z]?.*", Pattern.CASE_INSENSITIVE);

    // Pattern for extracting series from HT MCUs
    private static final Pattern HT_SERIES_PATTERN =
            Pattern.compile("^(HT\\d{2}[A-Z]?).*", Pattern.CASE_INSENSITIVE);

    // Pattern for extracting series from BS MCUs
    private static final Pattern BS_SERIES_PATTERN =
            Pattern.compile("^(BS\\d{2}[A-Z]?).*", Pattern.CASE_INSENSITIVE);

    // Package code mappings
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("SS", "SSOP"),
            Map.entry("SOP", "SOP"),
            Map.entry("NS", "NSOP"),
            Map.entry("QF", "QFP"),
            Map.entry("DIP", "DIP"),
            Map.entry("-3", "SSOP"),
            Map.entry("-1", "SOP"),
            Map.entry("-2", "SOP")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Flash MCUs - HT66F series (general purpose)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^HT66F\\d+.*");
        registry.addPattern(ComponentType.IC, "^HT66F\\d+.*");

        // Flash MCUs - HT67F series (with LCD driver)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^HT67F\\d+.*");
        registry.addPattern(ComponentType.IC, "^HT67F\\d+.*");

        // Flash MCUs - HT68F series (enhanced I/O)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^HT68F\\d+.*");
        registry.addPattern(ComponentType.IC, "^HT68F\\d+.*");

        // Touch Key MCUs - HT45F series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^HT45F\\d+.*");
        registry.addPattern(ComponentType.IC, "^HT45F\\d+.*");

        // Capacitive Touch MCUs - BS83 series (Body Sensor)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^BS83[A-Z]?\\d+.*");
        registry.addPattern(ComponentType.IC, "^BS83[A-Z]?\\d+.*");

        // Voice/Audio ICs - HT82V series
        registry.addPattern(ComponentType.IC, "^HT82V\\d+.*");

        // USB ICs - HT42B series (USB to UART bridge)
        registry.addPattern(ComponentType.IC, "^HT42B\\d+.*");

        // LCD Drivers - HT16xx series
        registry.addPattern(ComponentType.IC, "^HT16\\d{2}[A-Z]?.*");
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

        // Check for MCU types
        boolean isMCU = FLASH_MCU_PATTERN.matcher(upperMpn).matches() ||
                TOUCH_MCU_PATTERN.matcher(upperMpn).matches();

        // Check for IC types (includes MCUs, voice, USB, LCD drivers)
        boolean isIC = isMCU ||
                VOICE_IC_PATTERN.matcher(upperMpn).matches() ||
                USB_IC_PATTERN.matcher(upperMpn).matches() ||
                LCD_DRIVER_PATTERN.matcher(upperMpn).matches();

        if (type == ComponentType.MICROCONTROLLER) {
            return isMCU;
        }

        if (type == ComponentType.IC) {
            return isIC;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for suffix-based package codes (-1, -2, -3)
        if (upperMpn.endsWith("-3")) {
            return "SSOP";
        }
        if (upperMpn.endsWith("-1") || upperMpn.endsWith("-2")) {
            return "SOP";
        }

        // Check for package code prefixes in the suffix
        for (Map.Entry<String, String> entry : PACKAGE_CODES.entrySet()) {
            String code = entry.getKey();
            if (!code.startsWith("-") && upperMpn.contains(code)) {
                // Make sure it's a suffix, not part of the part number
                int idx = upperMpn.lastIndexOf(code);
                if (idx > 4) { // At least HT16 or BS83 prefix
                    return entry.getValue();
                }
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Flash MCU series - HT66F, HT67F, HT68F
        if (upperMpn.startsWith("HT66F")) return "HT66F";
        if (upperMpn.startsWith("HT67F")) return "HT67F";
        if (upperMpn.startsWith("HT68F")) return "HT68F";

        // Touch MCU series
        if (upperMpn.startsWith("HT45F")) return "HT45F";
        if (upperMpn.startsWith("BS83B")) return "BS83B";
        if (upperMpn.startsWith("BS83A")) return "BS83A";
        if (upperMpn.startsWith("BS83")) return "BS83";

        // Voice/Audio IC
        if (upperMpn.startsWith("HT82V")) return "HT82V";

        // USB IC
        if (upperMpn.startsWith("HT42B")) return "HT42B";

        // LCD Drivers
        if (upperMpn.startsWith("HT1621")) return "HT1621";
        if (upperMpn.startsWith("HT1628")) return "HT1628";
        if (upperMpn.matches("^HT16\\d{2}.*")) {
            // Extract HT16xx series
            return upperMpn.substring(0, 6);
        }

        return "";
    }

    /**
     * Extract the product subfamily from an MPN.
     * <p>
     * Examples:
     * <ul>
     *   <li>HT66F0185 - returns "HT66F0185"</li>
     *   <li>BS83B16A-3 - returns "BS83B16A"</li>
     *   <li>HT1621B - returns "HT1621B"</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the product subfamily, or empty string if not extractable
     */
    public String extractProductCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove package suffix (-1, -2, -3)
        String normalized = upperMpn.replaceAll("-\\d$", "");

        // For HT parts, extract up to first non-alphanumeric
        if (normalized.startsWith("HT") || normalized.startsWith("BS")) {
            StringBuilder result = new StringBuilder();
            for (char c : normalized.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    result.append(c);
                } else {
                    break;
                }
            }
            return result.toString();
        }

        return "";
    }

    /**
     * Determine if the MPN is a Touch MCU.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a touch MCU
     */
    public boolean isTouchMCU(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.startsWith("HT45F") || upperMpn.startsWith("BS83");
    }

    /**
     * Determine if the MPN is a Flash MCU.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a flash MCU
     */
    public boolean isFlashMCU(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.matches("^HT6[678]F\\d+.*");
    }

    /**
     * Determine if the MPN is an LCD Driver.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an LCD driver
     */
    public boolean isLCDDriver(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.matches("^HT16\\d{2}[A-Z]?.*");
    }

    /**
     * Determine if the MPN is a Voice/Audio IC.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a voice/audio IC
     */
    public boolean isVoiceIC(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.startsWith("HT82V");
    }

    /**
     * Determine if the MPN is a USB IC.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a USB IC
     */
    public boolean isUSBIC(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.startsWith("HT42B");
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

        // Must be same product code (ignoring package variant)
        String product1 = extractProductCode(upper1);
        String product2 = extractProductCode(upper2);

        // Same product in different packages are replacements
        return product1.equals(product2) && !product1.isEmpty();
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
