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
 * Handler for GigaDevice Semiconductor - Flash Memory and MCUs.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>GD25Qxx - Serial NOR Flash (standard SPI)</li>
 *   <li>GD25Bxx - Wide Voltage Serial Flash</li>
 *   <li>GD25LQxx - Low Power Serial Flash</li>
 *   <li>GD25WQxx - Wide Voltage Low Power Flash</li>
 *   <li>GD25Txx - High Performance Flash</li>
 *   <li>GD5Fxxxx - SLC NAND Flash</li>
 *   <li>GD32F1xx - ARM Cortex-M3 MCU (compatible with STM32F1)</li>
 *   <li>GD32F3xx - ARM Cortex-M4 MCU (compatible with STM32F3)</li>
 *   <li>GD32F4xx - High Performance Cortex-M4 MCU</li>
 *   <li>GD32E1xx/GD32E2xx - Enhanced MCUs</li>
 *   <li>GD32VF1xx - RISC-V MCU</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>GD25Q128CSIG - 128Mb Serial NOR Flash, SOP-8 package</li>
 *   <li>GD25Q64EWIGR - 64Mb Wide Voltage Flash, WSON-8 package, tape and reel</li>
 *   <li>GD32F103C8T6 - Cortex-M3 MCU, 64KB Flash, LQFP48 package</li>
 *   <li>GD32VF103CBT6 - RISC-V MCU, 128KB Flash, LQFP48 package</li>
 * </ul>
 */
public class GigaDeviceHandler implements ManufacturerHandler {

    // Pattern for extracting density from Flash MPNs
    private static final Pattern SERIAL_NOR_DENSITY_PATTERN =
            Pattern.compile("GD25[QBLWT]Q?(\\d+)");
    private static final Pattern NAND_DENSITY_PATTERN =
            Pattern.compile("GD5F(\\d+)G");

    // Pattern for extracting MCU details
    // GD32F103C8T6 -> family=F, series=1, line=03, pin=C, flash=8, pkg=T, temp=6
    // GD32VF103CBT6 -> family=VF, series=1, line=03, pin=C, flash=B, pkg=T, temp=6
    private static final Pattern MCU_PATTERN =
            Pattern.compile("GD32(VF|[VEFL])(\\d)(\\d{2})([A-Z])([0-9A-Z])([A-Z])(\\d)");

    // Alternative simpler pattern for extracting pin code (position 8-9 for ARM, 9-10 for RISC-V)
    // Uses (VF|[VEFL]) to handle RISC-V (VF) vs ARM (F/E/L) families correctly
    private static final Pattern MCU_PIN_PATTERN =
            Pattern.compile("GD32(VF|[VEFL])\\d{3}([A-Z])[0-9A-Z][A-Z]\\d");

    // Package code mappings for Flash
    private static final Map<String, String> FLASH_PACKAGE_CODES = Map.ofEntries(
            Map.entry("SIG", "SOP-8"),
            Map.entry("SIQ", "SOP-8"),
            Map.entry("SIP", "SOP-8"),
            Map.entry("CSIG", "SOP-8"),
            Map.entry("ESIG", "SOP-8"),
            Map.entry("EIG", "SOP-8"),
            Map.entry("WIG", "WSON-8"),
            Map.entry("EWIGR", "WSON-8"),
            Map.entry("EWIQ", "WSON-8"),
            Map.entry("WIGR", "WSON-8"),
            Map.entry("ZIG", "USON-8"),
            Map.entry("EZIQ", "USON-8"),
            Map.entry("FIG", "WLCSP"),
            Map.entry("LIG", "SOIC-16"),
            Map.entry("NIG", "DFN-8"),
            Map.entry("TIG", "TFBGA"),
            Map.entry("TIGR", "TFBGA"),
            Map.entry("UIG", "VSOP-8"),
            Map.entry("BIG", "BGA")
    );

    // Package code mappings for MCUs (position-based)
    private static final Map<Character, String> MCU_PACKAGE_CODES = Map.ofEntries(
            Map.entry('T', "LQFP"),
            Map.entry('C', "LQFP"),  // Alternate LQFP
            Map.entry('R', "LQFP"),  // LQFP (some variants)
            Map.entry('K', "UFBGA"),
            Map.entry('U', "VFQFPN"),
            Map.entry('H', "BGA"),
            Map.entry('V', "VFQFPN"),
            Map.entry('Y', "WLCSP"),
            Map.entry('G', "WLCSP")  // Alternate WLCSP
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

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Serial NOR Flash - GD25Qxx (standard)
        registry.addPattern(ComponentType.MEMORY, "^GD25Q\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^GD25Q\\d+.*");

        // Wide Voltage Serial Flash - GD25Bxx
        registry.addPattern(ComponentType.MEMORY, "^GD25B\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^GD25B\\d+.*");

        // Low Power Serial Flash - GD25LQxx
        registry.addPattern(ComponentType.MEMORY, "^GD25LQ\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^GD25LQ\\d+.*");

        // Wide Voltage Low Power Flash - GD25WQxx
        registry.addPattern(ComponentType.MEMORY, "^GD25WQ\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^GD25WQ\\d+.*");

        // High Performance Flash - GD25Txx
        registry.addPattern(ComponentType.MEMORY, "^GD25T\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^GD25T\\d+.*");

        // SLC NAND Flash - GD5Fxxxx
        registry.addPattern(ComponentType.MEMORY, "^GD5F\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^GD5F\\d+.*");

        // ARM Cortex-M3 MCU - GD32F1xx (STM32F1 compatible)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^GD32F1\\d{2}.*");

        // ARM Cortex-M4 MCU - GD32F3xx (STM32F3 compatible)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^GD32F3\\d{2}.*");

        // High Performance MCU - GD32F4xx
        registry.addPattern(ComponentType.MICROCONTROLLER, "^GD32F4\\d{2}.*");

        // Enhanced MCUs - GD32E1xx, GD32E2xx, GD32E5xx
        registry.addPattern(ComponentType.MICROCONTROLLER, "^GD32E[125]\\d{2}.*");

        // RISC-V MCU - GD32VF1xx
        registry.addPattern(ComponentType.MICROCONTROLLER, "^GD32VF1\\d{2}.*");

        // Cortex-M23 MCU - GD32W5xx (Wi-Fi)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^GD32W5\\d{2}.*");

        // Cortex-M33 MCU - GD32L2xx (low power)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^GD32L2\\d{2}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MEMORY,
                ComponentType.MEMORY_FLASH,
                ComponentType.MICROCONTROLLER
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check for Flash memory types
        boolean isFlash = upperMpn.matches("^GD25[QBLWT]Q?\\d+.*") ||
                upperMpn.matches("^GD5F\\d+.*");

        // Check for MCU types
        // GD32F1xx, GD32F3xx, GD32F4xx, GD32E1xx, GD32E2xx, GD32E5xx, GD32L2xx
        // GD32VF1xx (RISC-V), GD32W5xx (Wi-Fi)
        boolean isMCU = upperMpn.matches("^GD32[VEFL]\\d{3}.*") ||
                upperMpn.matches("^GD32VF\\d{3}.*") ||
                upperMpn.matches("^GD32W5\\d{2}.*");

        if (type == ComponentType.MEMORY || type == ComponentType.MEMORY_FLASH) {
            return isFlash;
        }

        if (type == ComponentType.MICROCONTROLLER) {
            return isMCU;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle Flash memory MPNs
        if (upperMpn.startsWith("GD25") || upperMpn.startsWith("GD5F")) {
            return extractFlashPackageCode(upperMpn);
        }

        // Handle MCU MPNs
        if (upperMpn.startsWith("GD32")) {
            return extractMCUPackageCode(upperMpn);
        }

        return "";
    }

    /**
     * Extract package code from Flash memory MPN.
     * Format: GD25Q128CSIG, GD25Q64EWIGR, etc.
     */
    private String extractFlashPackageCode(String mpn) {
        // Remove tape and reel suffix (R or TR at end)
        String normalized = mpn.replaceAll("R$", "").replaceAll("TR$", "");

        // Try matching known package code suffixes (longer first for specificity)
        String[] suffixesToCheck = {"EWIGR", "CSIG", "ESIG", "WIGR", "TIGR",
                "EWIQ", "EZIQ", "WIG", "SIG", "SIQ", "SIP", "EIG",
                "ZIG", "FIG", "LIG", "NIG", "TIG", "UIG", "BIG"};

        for (String suffix : suffixesToCheck) {
            if (normalized.endsWith(suffix)) {
                String pkg = FLASH_PACKAGE_CODES.get(suffix);
                return pkg != null ? pkg : suffix;
            }
        }

        // Try extracting last 2-4 letters as package code
        StringBuilder suffix = new StringBuilder();
        for (int i = normalized.length() - 1; i >= 0 && Character.isLetter(normalized.charAt(i)); i--) {
            suffix.insert(0, normalized.charAt(i));
            if (suffix.length() >= 4) break;
        }

        if (suffix.length() >= 2) {
            String pkg = FLASH_PACKAGE_CODES.get(suffix.toString());
            return pkg != null ? pkg : suffix.toString();
        }

        return "";
    }

    /**
     * Extract package code from MCU MPN.
     * Format: GD32F103C8T6 -> C=48-pin, 8=64KB, T=LQFP, 6=temp
     * The package type is the second-to-last character.
     */
    private String extractMCUPackageCode(String mpn) {
        if (mpn.length() < 2) return "";

        // For GD32, package is encoded in second-to-last character
        // e.g., GD32F103C8T6 -> T = LQFP
        char packageChar = mpn.charAt(mpn.length() - 2);
        String packageType = MCU_PACKAGE_CODES.get(packageChar);

        if (packageType != null) {
            // Try to get pin count from earlier in the MPN
            // GD32F103C8T6 -> C = 48 pins
            Matcher matcher = MCU_PATTERN.matcher(mpn);
            if (matcher.matches()) {
                char pinChar = matcher.group(4).charAt(0);
                Integer pinCount = MCU_PIN_COUNTS.get(pinChar);
                if (pinCount != null) {
                    return packageType + pinCount;
                }
            }
            return packageType;
        }

        return String.valueOf(packageChar);
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Serial NOR Flash series
        if (upperMpn.startsWith("GD25Q")) return "GD25Q";
        if (upperMpn.startsWith("GD25B")) return "GD25B";
        if (upperMpn.startsWith("GD25LQ")) return "GD25LQ";
        if (upperMpn.startsWith("GD25WQ")) return "GD25WQ";
        if (upperMpn.startsWith("GD25T")) return "GD25T";

        // NAND Flash series
        if (upperMpn.startsWith("GD5F")) return "GD5F";

        // MCU series - extract family designation
        if (upperMpn.startsWith("GD32VF1")) return "GD32VF1";
        if (upperMpn.startsWith("GD32F1")) return "GD32F1";
        if (upperMpn.startsWith("GD32F3")) return "GD32F3";
        if (upperMpn.startsWith("GD32F4")) return "GD32F4";
        if (upperMpn.startsWith("GD32E1")) return "GD32E1";
        if (upperMpn.startsWith("GD32E2")) return "GD32E2";
        if (upperMpn.startsWith("GD32E5")) return "GD32E5";
        if (upperMpn.startsWith("GD32W5")) return "GD32W5";
        if (upperMpn.startsWith("GD32L2")) return "GD32L2";

        return "";
    }

    /**
     * Extract memory density from Flash MPN.
     * <p>
     * Examples:
     * <ul>
     *   <li>GD25Q16 - returns "16" (16 Mbit)</li>
     *   <li>GD25Q128CSIG - returns "128" (128 Mbit)</li>
     *   <li>GD5F1GM7 - returns "1G" (1 Gbit)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the density string, or empty string if not extractable
     */
    public String extractDensity(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Serial NOR Flash (GD25Q, GD25B, GD25LQ, GD25WQ, GD25T)
        Matcher serialMatcher = SERIAL_NOR_DENSITY_PATTERN.matcher(upperMpn);
        if (serialMatcher.find()) {
            return serialMatcher.group(1);
        }

        // NAND Flash (GD5F1G, GD5F2G, etc.)
        Matcher nandMatcher = NAND_DENSITY_PATTERN.matcher(upperMpn);
        if (nandMatcher.find()) {
            return nandMatcher.group(1) + "G";
        }

        return "";
    }

    // Pattern for extracting flash size code
    // Uses (VF|[VEFL]) to handle RISC-V (VF) vs ARM (F/E/L) families correctly
    private static final Pattern MCU_FLASH_PATTERN =
            Pattern.compile("GD32(VF|[VEFL])\\d{3}[A-Z]([0-9A-Z])[A-Z]\\d");

    /**
     * Extract MCU flash size in KB from MPN.
     * <p>
     * Format: GD32F103C8T6 -> 8 = 64KB
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
     *   <li>I = 2048KB</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the flash size in KB, or -1 if not extractable
     */
    public int extractMCUFlashSizeKB(String mpn) {
        if (mpn == null || !mpn.toUpperCase().startsWith("GD32")) return -1;

        String upperMpn = mpn.toUpperCase();
        Matcher matcher = MCU_FLASH_PATTERN.matcher(upperMpn);
        if (matcher.matches()) {
            // group(1) is family (VF or F/E/L), group(2) is flash code
            char flashCode = matcher.group(2).charAt(0);
            return switch (flashCode) {
                case '4' -> 16;
                case '6' -> 32;
                case '8' -> 64;
                case 'B' -> 128;
                case 'C' -> 256;
                case 'D' -> 384;
                case 'E' -> 512;
                case 'G' -> 1024;
                case 'I' -> 2048;
                default -> -1;
            };
        }

        return -1;
    }

    /**
     * Extract MCU pin count from MPN.
     *
     * @param mpn the manufacturer part number
     * @return the pin count, or -1 if not extractable
     */
    public int extractMCUPinCount(String mpn) {
        if (mpn == null || !mpn.toUpperCase().startsWith("GD32")) return -1;

        String upperMpn = mpn.toUpperCase();
        Matcher matcher = MCU_PIN_PATTERN.matcher(upperMpn);
        if (matcher.matches()) {
            // group(1) is family (VF or F/E/L), group(2) is pin code
            char pinCode = matcher.group(2).charAt(0);
            Integer count = MCU_PIN_COUNTS.get(pinCode);
            return count != null ? count : -1;
        }

        return -1;
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

        // For Flash memory: must be same density
        if (series1.startsWith("GD25") || series1.startsWith("GD5F")) {
            String density1 = extractDensity(upper1);
            String density2 = extractDensity(upper2);
            if (!density1.equals(density2) || density1.isEmpty()) {
                return false;
            }
            // Same series and density = valid replacement (package differences acceptable)
            return true;
        }

        // For MCUs: must be same line (e.g., GD32F103) and same pin count
        if (series1.startsWith("GD32")) {
            // Extract full MCU line (e.g., GD32F103 from GD32F103C8T6)
            String line1 = extractMCULine(upper1);
            String line2 = extractMCULine(upper2);
            if (!line1.equals(line2) || line1.isEmpty()) {
                return false;
            }

            // Must have same pin count
            int pins1 = extractMCUPinCount(upper1);
            int pins2 = extractMCUPinCount(upper2);
            if (pins1 != pins2 || pins1 == -1) {
                return false;
            }

            // Same MCU line and pin count = valid replacement
            // Flash size and package differences are acceptable
            return true;
        }

        return false;
    }

    /**
     * Extract MCU line designation (e.g., GD32F103 from GD32F103C8T6).
     */
    private String extractMCULine(String mpn) {
        if (mpn == null || !mpn.startsWith("GD32")) return "";

        // Pattern: GD32 + family (V/E/F) + series number (1-5) + line (2 digits)
        // e.g., GD32F103, GD32VF103, GD32E230
        if (mpn.length() >= 9) {
            // Check for RISC-V (GD32VF) which has an extra 'F'
            if (mpn.startsWith("GD32VF")) {
                return mpn.substring(0, 9); // GD32VF103
            }
            // Standard ARM MCUs
            return mpn.substring(0, 8); // GD32F103, GD32E230
        }

        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
