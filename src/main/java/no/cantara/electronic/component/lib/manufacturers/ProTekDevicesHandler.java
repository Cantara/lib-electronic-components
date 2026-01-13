package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for ProTek Devices components.
 *
 * ProTek Devices specializes in circuit protection:
 * - TVS Diodes: TVS series (TVS03500, TVS05500, TVS15000)
 * - ESD Protection: GBLC series (GBLC03C, GBLC05C, GBLC15C)
 * - Automotive TVS: PSM series (PSM712, PSM712-LF)
 * - Ultra-Low Capacitance: ULC series (ULC0512, ULC0524)
 * - Surface Mount TVS: SMD series (SMD0512, SMD1512)
 *
 * MPN Structure:
 * TVS series: TVS[voltage][power] - e.g., TVS03500 = 3.5V standoff, TVS05500 = 5.5V standoff
 * GBLC series: GBLC[voltage]C - e.g., GBLC03C = 3.3V bidirectional ESD protection
 * PSM series: PSM[model][-LF] - e.g., PSM712 for RS-232 protection, -LF = lead-free
 * ULC series: ULC[voltage][current] - e.g., ULC0512 = 5V, 12 line protection
 * SMD series: SMD[power][voltage] - e.g., SMD0512 = 500W, 12V standoff
 *
 * Suffix Codes:
 * C = Bidirectional
 * -LF = Lead-free
 * -TR = Tape and Reel
 */
public class ProTekDevicesHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // TVS series - Standard TVS Diodes
        // Format: TVS[voltage][power] - TVS03500 (3.5V), TVS05500 (5.5V), TVS15000 (15V)
        registry.addPattern(ComponentType.DIODE, "^TVS[0-9]{5}.*");

        // GBLC series - ESD Protection
        // Format: GBLC[voltage]C - GBLC03C (3.3V), GBLC05C (5V), GBLC15C (15V)
        registry.addPattern(ComponentType.DIODE, "^GBLC[0-9]{2}C.*");

        // PSM series - Automotive/RS-232 Protection TVS
        // Format: PSM[model][-LF] - PSM712, PSM712-LF, PSM05, PSM24
        registry.addPattern(ComponentType.DIODE, "^PSM[0-9]+.*");

        // ULC series - Ultra-Low Capacitance ESD Protection
        // Format: ULC[voltage][lines] - ULC0512 (5V, 12-line), ULC0524 (5V, 24-line)
        registry.addPattern(ComponentType.DIODE, "^ULC[0-9]{4}.*");

        // SMD series - Surface Mount TVS Diodes
        // Format: SMD[power][voltage] - SMD0512 (500W, 12V), SMD1512 (1500W, 12V)
        registry.addPattern(ComponentType.DIODE, "^SMD[0-9]{4}.*");

        // SP series - Surge Protection
        // Format: SP[voltage] - SP05, SP12, SP24
        registry.addPattern(ComponentType.DIODE, "^SP[0-9]{2,3}.*");

        // LC series - Low Capacitance TVS
        // Format: LC[model] - LC03, LC05
        registry.addPattern(ComponentType.DIODE, "^LC[0-9]{2}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.DIODE
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        if (type == ComponentType.DIODE) {
            // TVS series - Standard TVS Diodes
            if (upperMpn.matches("^TVS[0-9]{5}.*")) {
                return true;
            }

            // GBLC series - ESD Protection
            if (upperMpn.matches("^GBLC[0-9]{2}C.*")) {
                return true;
            }

            // PSM series - Automotive/RS-232 Protection
            if (upperMpn.matches("^PSM[0-9]+.*")) {
                return true;
            }

            // ULC series - Ultra-Low Capacitance
            if (upperMpn.matches("^ULC[0-9]{4}.*")) {
                return true;
            }

            // SMD series - Surface Mount TVS
            if (upperMpn.matches("^SMD[0-9]{4}.*")) {
                return true;
            }

            // SP series - Surge Protection
            if (upperMpn.matches("^SP[0-9]{2,3}.*")) {
                return true;
            }

            // LC series - Low Capacitance TVS
            if (upperMpn.matches("^LC[0-9]{2}.*")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Check for lead-free suffix
        if (upperMpn.contains("-LF")) {
            // Return package code without -LF, or indicate lead-free
            // Most ProTek devices are SMD unless specified
            if (upperMpn.startsWith("PSM")) {
                return "SOIC-8";  // PSM series typically in SOIC-8
            }
        }

        // TVS series packages
        if (upperMpn.startsWith("TVS")) {
            // TVS series typically available in various packages
            // Default to common package
            return "SMB";  // DO-214AA
        }

        // GBLC series packages
        if (upperMpn.startsWith("GBLC")) {
            return "SOT-23";  // Common ESD protection package
        }

        // PSM series packages
        if (upperMpn.startsWith("PSM")) {
            return "SOIC-8";  // PSM712 and similar in SOIC-8
        }

        // ULC series packages
        if (upperMpn.startsWith("ULC")) {
            // Ultra-low capacitance ESD arrays
            if (upperMpn.matches("^ULC[0-9]{2}12.*")) {
                return "SSOP-16";  // 12-line protection
            }
            if (upperMpn.matches("^ULC[0-9]{2}24.*")) {
                return "SSOP-28";  // 24-line protection
            }
            return "SOT-23";  // Default for smaller arrays
        }

        // SMD series packages (named after package type)
        if (upperMpn.startsWith("SMD")) {
            // Extract power level to determine package
            String powerCode = upperMpn.substring(3, 5);
            return switch (powerCode) {
                case "05" -> "SMA";   // 500W -> SMA (DO-214AC)
                case "10" -> "SMB";   // 1000W -> SMB (DO-214AA)
                case "15" -> "SMC";   // 1500W -> SMC (DO-214AB)
                case "30" -> "SMC";   // 3000W -> SMC
                default -> "SMB";
            };
        }

        // SP series
        if (upperMpn.startsWith("SP")) {
            return "SOT-23";
        }

        // LC series
        if (upperMpn.startsWith("LC")) {
            return "SOT-23";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // TVS series
        if (upperMpn.startsWith("TVS")) {
            return "TVS";
        }

        // GBLC series
        if (upperMpn.startsWith("GBLC")) {
            return "GBLC";
        }

        // PSM series
        if (upperMpn.startsWith("PSM")) {
            return "PSM";
        }

        // ULC series
        if (upperMpn.startsWith("ULC")) {
            return "ULC";
        }

        // SMD series
        if (upperMpn.matches("^SMD[0-9].*")) {
            return "SMD";
        }

        // SP series
        if (upperMpn.startsWith("SP") && upperMpn.matches("^SP[0-9].*")) {
            return "SP";
        }

        // LC series
        if (upperMpn.startsWith("LC") && upperMpn.matches("^LC[0-9].*")) {
            return "LC";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return false;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series for official replacement
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Extract voltage ratings
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);

        // Same voltage required
        if (!voltage1.equals(voltage2) || voltage1.isEmpty()) {
            return false;
        }

        // Check directionality for ESD protection (C suffix = bidirectional)
        boolean bidir1 = isBidirectional(mpn1);
        boolean bidir2 = isBidirectional(mpn2);

        // Same directionality required
        if (bidir1 != bidir2) {
            return false;
        }

        // Lead-free variants are replacements for each other
        // PSM712 and PSM712-LF are interchangeable
        return true;
    }

    /**
     * Extract voltage rating from ProTek Devices MPN.
     *
     * @param mpn the manufacturer part number
     * @return voltage string or empty if not found
     */
    public String extractVoltage(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // TVS series: TVS03500 -> 03.5V (first 3 digits are voltage * 10)
        if (upperMpn.startsWith("TVS")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^TVS([0-9]{3}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String voltageCode = m.group(1);
                // Convert to actual voltage (e.g., 035 -> 3.5V, 055 -> 5.5V, 150 -> 15V)
                int voltageInt = Integer.parseInt(voltageCode);
                if (voltageInt < 100) {
                    return String.format("%.1f", voltageInt / 10.0);
                }
                return String.valueOf(voltageInt / 10);
            }
        }

        // GBLC series: GBLC03C -> 3.3V, GBLC05C -> 5V, GBLC15C -> 15V
        if (upperMpn.startsWith("GBLC")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^GBLC([0-9]{2}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String voltageCode = m.group(1);
                int voltage = Integer.parseInt(voltageCode);
                // Handle special cases
                if (voltage == 3) return "3.3";
                if (voltage == 5) return "5";
                return String.valueOf(voltage);
            }
        }

        // PSM series: PSM712 -> 7V (data line voltage), PSM05 -> 5V
        if (upperMpn.startsWith("PSM")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^PSM([0-9]+).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String modelNum = m.group(1);
                // PSM712 is a specific part for RS-232 with 7V standoff
                if (modelNum.equals("712")) return "7";
                if (modelNum.startsWith("05")) return "5";
                if (modelNum.startsWith("12")) return "12";
                if (modelNum.startsWith("24")) return "24";
                return modelNum;
            }
        }

        // ULC series: ULC0512 -> 5V (first 2 digits after ULC)
        if (upperMpn.startsWith("ULC")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^ULC([0-9]{2}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String voltageCode = m.group(1);
                int voltage = Integer.parseInt(voltageCode);
                if (voltage == 5) return "5";
                return String.valueOf(voltage);
            }
        }

        // SMD series: SMD0512 -> 12V (last 2 digits are voltage)
        if (upperMpn.matches("^SMD[0-9]{4}.*")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^SMD[0-9]{2}([0-9]{2}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String voltageCode = m.group(1);
                return String.valueOf(Integer.parseInt(voltageCode));
            }
        }

        // SP series: SP05 -> 5V, SP12 -> 12V
        if (upperMpn.matches("^SP[0-9].*")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^SP([0-9]{2,3}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                return m.group(1);
            }
        }

        // LC series: LC03 -> 3.3V, LC05 -> 5V
        if (upperMpn.matches("^LC[0-9].*")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^LC([0-9]{2}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String voltageCode = m.group(1);
                int voltage = Integer.parseInt(voltageCode);
                if (voltage == 3) return "3.3";
                if (voltage == 5) return "5";
                return String.valueOf(voltage);
            }
        }

        return "";
    }

    /**
     * Check if the device is bidirectional.
     *
     * @param mpn the manufacturer part number
     * @return true if bidirectional (C suffix for GBLC, or known bidirectional parts)
     */
    public boolean isBidirectional(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // GBLC series with C suffix is bidirectional
        if (upperMpn.matches("^GBLC[0-9]{2}C.*")) {
            return true;
        }

        // PSM series devices are typically bidirectional for data line protection
        if (upperMpn.startsWith("PSM")) {
            return true;
        }

        // ULC series are bidirectional ESD arrays
        if (upperMpn.startsWith("ULC")) {
            return true;
        }

        return false;
    }

    /**
     * Check if the device is lead-free.
     *
     * @param mpn the manufacturer part number
     * @return true if lead-free (-LF suffix)
     */
    public boolean isLeadFree(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return false;
        }
        return mpn.toUpperCase().contains("-LF");
    }

    /**
     * Get power rating for TVS diodes.
     *
     * @param mpn the manufacturer part number
     * @return power rating in watts, or 0 if not determinable
     */
    public int getPowerRating(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return 0;
        }

        String upperMpn = mpn.toUpperCase();

        // TVS series - extract power from last 2 digits
        if (upperMpn.startsWith("TVS")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^TVS[0-9]{3}([0-9]{2}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String powerCode = m.group(1);
                return Integer.parseInt(powerCode) * 10;  // e.g., 00 -> 0W, 50 -> 500W
            }
        }

        // SMD series - extract power from first 2 digits after SMD
        if (upperMpn.matches("^SMD[0-9]{4}.*")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^SMD([0-9]{2}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                String powerCode = m.group(1);
                return Integer.parseInt(powerCode) * 100;  // e.g., 05 -> 500W, 15 -> 1500W
            }
        }

        // GBLC series - typically low power ESD protection
        if (upperMpn.startsWith("GBLC")) {
            return 200;  // Typical ESD rating
        }

        // PSM series - medium power
        if (upperMpn.startsWith("PSM")) {
            return 400;
        }

        // ULC series - low power ESD
        if (upperMpn.startsWith("ULC")) {
            return 150;
        }

        return 0;
    }

    /**
     * Get the number of protection lines for array devices.
     *
     * @param mpn the manufacturer part number
     * @return number of lines, or 1 for single-channel devices
     */
    public int getLineCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return 1;
        }

        String upperMpn = mpn.toUpperCase();

        // ULC series: ULC0512 -> 12 lines, ULC0524 -> 24 lines
        if (upperMpn.startsWith("ULC")) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^ULC[0-9]{2}([0-9]{2}).*");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        }

        // GBLC series - typically single or dual channel
        if (upperMpn.startsWith("GBLC")) {
            return 1;  // Single channel
        }

        // PSM series - typically dual channel for data lines
        if (upperMpn.startsWith("PSM")) {
            return 2;
        }

        return 1;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
