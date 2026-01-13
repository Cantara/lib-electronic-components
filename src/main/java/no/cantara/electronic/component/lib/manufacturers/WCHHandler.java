package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for WCH (Nanjing Qinheng Microelectronics) components.
 * <p>
 * WCH is a Chinese semiconductor company known for low-cost USB interface chips
 * and RISC-V microcontrollers.
 * <p>
 * Supported product families:
 * <ul>
 *   <li>CH32V003 - Entry-level RISC-V MCU (QingKe V2A core, 48MHz, 2KB SRAM)</li>
 *   <li>CH32V103 - RISC-V MCU (QingKe V3A core, 80MHz, 20KB SRAM)</li>
 *   <li>CH32V203 - RISC-V MCU (QingKe V4B core, 144MHz, 64KB SRAM)</li>
 *   <li>CH32V208 - RISC-V MCU with BLE (QingKe V4C core)</li>
 *   <li>CH32V303 - High-performance RISC-V MCU (QingKe V4F core, 144MHz)</li>
 *   <li>CH32V305/307 - RISC-V MCU with USB HS and Ethernet</li>
 *   <li>CH32F103 - ARM Cortex-M3 MCU (STM32F103 compatible)</li>
 *   <li>CH32F203 - ARM Cortex-M3 MCU with enhanced peripherals</li>
 *   <li>CH340 - USB to serial UART (most popular)</li>
 *   <li>CH341 - USB to serial/parallel/I2C/SPI</li>
 *   <li>CH9340 - USB to serial (enhanced CH340)</li>
 *   <li>CH395 - Ethernet controller</li>
 *   <li>CH9121 - Ethernet to serial converter</li>
 *   <li>CH9141/CH9143 - Bluetooth modules</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>CH32V003F4U6 - RISC-V MCU, 16KB Flash, QFN-20, industrial temp</li>
 *   <li>CH32V103C8T6 - RISC-V MCU, 64KB Flash, LQFP-48</li>
 *   <li>CH32V203RBT6 - RISC-V MCU, 128KB Flash, LQFP-64</li>
 *   <li>CH32V307VCT6 - RISC-V MCU, 256KB Flash, LQFP-100</li>
 *   <li>CH32F103C8T6 - ARM MCU, 64KB Flash, LQFP-48</li>
 *   <li>CH340G - USB to serial, SOP-16</li>
 *   <li>CH340C - USB to serial with integrated crystal, SOP-16</li>
 *   <li>CH341A - USB to serial/parallel/I2C, SOP-28</li>
 * </ul>
 * <p>
 * Package codes (MCUs):
 * <ul>
 *   <li>C - LQFP-48</li>
 *   <li>R - LQFP-64</li>
 *   <li>V - LQFP-100</li>
 *   <li>G/U - QFN</li>
 *   <li>K - ESSOP</li>
 *   <li>N/J - SOP</li>
 *   <li>F - TSSOP-20 (for CH32V003)</li>
 * </ul>
 */
public class WCHHandler implements ManufacturerHandler {

    // Package code mappings for MCUs (pin code position)
    private static final Map<Character, String> MCU_PACKAGE_CODES = Map.ofEntries(
            Map.entry('C', "LQFP-48"),
            Map.entry('R', "LQFP-64"),
            Map.entry('V', "LQFP-100"),
            Map.entry('G', "QFN"),
            Map.entry('U', "QFN"),
            Map.entry('K', "ESSOP"),
            Map.entry('N', "SOP"),
            Map.entry('J', "SOP"),
            Map.entry('F', "TSSOP-20"),
            Map.entry('A', "QFN-28"),
            Map.entry('W', "QFN-28")
    );

    // Pin count mappings for MCUs
    private static final Map<Character, Integer> MCU_PIN_COUNTS = Map.ofEntries(
            Map.entry('A', 28),
            Map.entry('C', 48),
            Map.entry('F', 20),
            Map.entry('G', 20),
            Map.entry('J', 20),
            Map.entry('K', 20),
            Map.entry('N', 8),
            Map.entry('R', 64),
            Map.entry('U', 20),
            Map.entry('V', 100),
            Map.entry('W', 28)
    );

    // Package code mappings for USB interface chips
    private static final Map<String, String> USB_PACKAGE_CODES = Map.ofEntries(
            Map.entry("G", "SOP-16"),
            Map.entry("C", "SOP-16"),
            Map.entry("K", "ESSOP-10"),
            Map.entry("N", "SOP-8"),
            Map.entry("E", "MSOP-10"),
            Map.entry("X", "ESSOP-10"),
            Map.entry("T", "SSOP-20"),
            Map.entry("A", "SOP-28"),
            Map.entry("B", "SSOP-28"),
            Map.entry("S", "SOP-28"),
            Map.entry("H", "SOP-28"),
            Map.entry("L", "LQFP-48")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // RISC-V MCUs - CH32Vxxx series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32V003[A-Z][0-9A-Z][A-Z][0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32V103[A-Z][0-9A-Z][A-Z][0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32V203[A-Z][0-9A-Z][A-Z][0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32V208[A-Z][0-9A-Z][A-Z][0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32V303[A-Z][0-9A-Z][A-Z][0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32V305[A-Z][0-9A-Z][A-Z][0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32V307[A-Z][0-9A-Z][A-Z][0-9].*");

        // ARM Cortex-M3 MCUs - CH32Fxxx series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32F103[A-Z][0-9A-Z][A-Z][0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^CH32F203[A-Z][0-9A-Z][A-Z][0-9].*");

        // USB interface chips - CH340/CH341/CH9340 series
        registry.addPattern(ComponentType.IC, "^CH340[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH341[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH9340[A-Z]?$");

        // Ethernet controllers - CH395/CH9121
        registry.addPattern(ComponentType.IC, "^CH395[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH9121.*");

        // Bluetooth modules - CH9141/CH9143
        registry.addPattern(ComponentType.IC, "^CH9141.*");
        registry.addPattern(ComponentType.IC, "^CH9143.*");

        // USB hub controllers - CH334
        registry.addPattern(ComponentType.IC, "^CH334[A-Z]?.*");

        // USB PD controllers - CH224K
        registry.addPattern(ComponentType.IC, "^CH224[A-Z]?$");

        // Other USB interface chips
        registry.addPattern(ComponentType.IC, "^CH342[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH343[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH344[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH345[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH346[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH347[A-Z]?$");
        registry.addPattern(ComponentType.IC, "^CH348[A-Z]?.*");
        registry.addPattern(ComponentType.IC, "^CH552[A-Z]?.*");
        registry.addPattern(ComponentType.IC, "^CH554[A-Z]?.*");
        registry.addPattern(ComponentType.IC, "^CH559[A-Z]?.*");
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

        // Check for MCU types (CH32Vxxx RISC-V and CH32Fxxx ARM)
        boolean isMCU = upperMpn.matches("^CH32[VF]\\d{3}[A-Z].*");

        // Check for USB interface chips
        boolean isUSBInterface = upperMpn.matches("^CH34[0-9][A-Z]?$") ||
                upperMpn.matches("^CH9340[A-Z]?$");

        // Check for Ethernet chips
        boolean isEthernet = upperMpn.matches("^CH395[A-Z]?$") ||
                upperMpn.matches("^CH9121.*");

        // Check for Bluetooth modules
        boolean isBluetooth = upperMpn.matches("^CH914[13].*");

        // Check for USB hub, PD, and other USB chips
        boolean isOtherUSB = upperMpn.matches("^CH334[A-Z]?.*") ||
                upperMpn.matches("^CH224[A-Z]?$") ||
                upperMpn.matches("^CH55[249][A-Z]?.*") ||
                upperMpn.matches("^CH34[2-8][A-Z]?.*");

        if (type == ComponentType.MICROCONTROLLER) {
            return isMCU;
        }

        if (type == ComponentType.IC) {
            return isUSBInterface || isEthernet || isBluetooth || isOtherUSB;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle MCU MPNs (CH32Vxxx, CH32Fxxx)
        if (upperMpn.matches("^CH32[VF]\\d{3}[A-Z][0-9A-Z][A-Z]\\d.*")) {
            return extractMCUPackageCode(upperMpn);
        }

        // Handle USB interface chips (CH340, CH341, CH9340, etc.)
        if (upperMpn.matches("^CH34[0-9][A-Z]?$") ||
                upperMpn.matches("^CH9340[A-Z]?$") ||
                upperMpn.matches("^CH34[2-8][A-Z]?$")) {
            return extractUSBPackageCode(upperMpn);
        }

        // Handle CH224K (USB PD)
        if (upperMpn.matches("^CH224[A-Z]?$")) {
            return extractUSBPackageCode(upperMpn);
        }

        // Handle CH341 variants
        if (upperMpn.matches("^CH341[A-Z]?$")) {
            return extractUSBPackageCode(upperMpn);
        }

        return "";
    }

    /**
     * Extract package code from MCU MPN.
     * Format: CH32V103C8T6 -> C=48-pin, 8=64KB, T=package type, 6=temp
     * Position: 01234567890123
     *           CH32V103C8T6
     *                   ^-- position 8 is pin code 'C'
     */
    private String extractMCUPackageCode(String mpn) {
        if (mpn.length() < 12) return "";

        // Pin code is at position 8 (0-indexed) for standard CH32Vxxx/CH32Fxxx format
        // CH32V103C8T6
        //         ^-- position 8 is pin code 'C'
        char pinCode = mpn.charAt(8);
        String packageType = MCU_PACKAGE_CODES.get(pinCode);

        if (packageType != null) {
            Integer pinCount = MCU_PIN_COUNTS.get(pinCode);
            if (pinCount != null) {
                // Return package with pin count (e.g., "LQFP-48")
                if (packageType.contains("-")) {
                    // Already has pin count (like "LQFP-48" or "TSSOP-20")
                    return packageType;
                }
                return packageType + "-" + pinCount;
            }
            return packageType;
        }

        return String.valueOf(pinCode);
    }

    /**
     * Extract package code from USB interface chip MPN.
     * Format: CH340G -> G = SOP-16
     */
    private String extractUSBPackageCode(String mpn) {
        // Get the last character as the package code
        String suffix = "";

        // CH340, CH341, CH9340, CH342-CH348, CH224
        if (mpn.matches("^CH(340|341|9340|34[2-8]|224)[A-Z]$")) {
            suffix = mpn.substring(mpn.length() - 1);
        } else if (mpn.matches("^CH(340|341|9340|34[2-8]|224)$")) {
            // No suffix - default package
            return "SOP-16";
        }

        if (!suffix.isEmpty()) {
            String pkg = USB_PACKAGE_CODES.get(suffix);
            return pkg != null ? pkg : suffix;
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // RISC-V MCU series
        if (upperMpn.startsWith("CH32V003")) return "CH32V003";
        if (upperMpn.startsWith("CH32V103")) return "CH32V103";
        if (upperMpn.startsWith("CH32V203")) return "CH32V203";
        if (upperMpn.startsWith("CH32V208")) return "CH32V208";
        if (upperMpn.startsWith("CH32V303")) return "CH32V303";
        if (upperMpn.startsWith("CH32V305")) return "CH32V305";
        if (upperMpn.startsWith("CH32V307")) return "CH32V307";

        // ARM MCU series
        if (upperMpn.startsWith("CH32F103")) return "CH32F103";
        if (upperMpn.startsWith("CH32F203")) return "CH32F203";

        // USB interface series
        if (upperMpn.startsWith("CH340")) return "CH340";
        if (upperMpn.startsWith("CH341")) return "CH341";
        if (upperMpn.startsWith("CH9340")) return "CH9340";

        // Other CH34x variants
        if (upperMpn.matches("^CH34[2-8].*")) return upperMpn.substring(0, 5);

        // Ethernet
        if (upperMpn.startsWith("CH395")) return "CH395";
        if (upperMpn.startsWith("CH9121")) return "CH9121";

        // Bluetooth
        if (upperMpn.startsWith("CH9141")) return "CH9141";
        if (upperMpn.startsWith("CH9143")) return "CH9143";

        // USB hub
        if (upperMpn.startsWith("CH334")) return "CH334";

        // USB PD
        if (upperMpn.startsWith("CH224")) return "CH224";

        // 8051-based USB MCUs
        if (upperMpn.startsWith("CH552")) return "CH552";
        if (upperMpn.startsWith("CH554")) return "CH554";
        if (upperMpn.startsWith("CH559")) return "CH559";

        return "";
    }

    /**
     * Extract MCU flash size in KB from MPN.
     * <p>
     * Format: CH32V103C8T6 -> 8 = 64KB
     * <p>
     * Position: 01234567890123
     *           CH32V103C8T6
     *                    ^-- position 9 is flash code '8'
     * <p>
     * Flash size encoding (similar to STM32):
     * <ul>
     *   <li>4 = 16KB</li>
     *   <li>6 = 32KB</li>
     *   <li>8 = 64KB</li>
     *   <li>B = 128KB</li>
     *   <li>C = 256KB</li>
     *   <li>D = 384KB</li>
     *   <li>E = 512KB</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the flash size in KB, or -1 if not extractable
     */
    public int extractMCUFlashSizeKB(String mpn) {
        if (mpn == null || !mpn.toUpperCase().startsWith("CH32")) return -1;

        String upperMpn = mpn.toUpperCase();
        if (!upperMpn.matches("^CH32[VF]\\d{3}[A-Z][0-9A-Z][A-Z]\\d.*")) return -1;

        // Flash size code is at position 9 (0-indexed)
        // CH32V103C8T6
        //          ^-- position 9 is flash code '8'
        char flashCode = upperMpn.charAt(9);
        return switch (flashCode) {
            case '4' -> 16;
            case '6' -> 32;
            case '8' -> 64;
            case 'B' -> 128;
            case 'C' -> 256;
            case 'D' -> 384;
            case 'E' -> 512;
            case 'G' -> 1024;
            default -> -1;
        };
    }

    /**
     * Extract MCU pin count from MPN.
     * <p>
     * Position: 01234567890123
     *           CH32V103C8T6
     *                   ^-- position 8 is pin code 'C'
     *
     * @param mpn the manufacturer part number
     * @return the pin count, or -1 if not extractable
     */
    public int extractMCUPinCount(String mpn) {
        if (mpn == null || !mpn.toUpperCase().startsWith("CH32")) return -1;

        String upperMpn = mpn.toUpperCase();
        if (!upperMpn.matches("^CH32[VF]\\d{3}[A-Z][0-9A-Z][A-Z]\\d.*")) return -1;

        // Pin code is at position 8 (0-indexed)
        // CH32V103C8T6
        //         ^-- position 8 is pin code 'C'
        char pinCode = upperMpn.charAt(8);
        Integer count = MCU_PIN_COUNTS.get(pinCode);
        return count != null ? count : -1;
    }

    /**
     * Get the core type for the given MCU MPN.
     *
     * @param mpn the manufacturer part number
     * @return the core type (e.g., "RISC-V QingKe V2A", "ARM Cortex-M3")
     */
    public String getCoreType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "CH32V003" -> "RISC-V QingKe V2A";
            case "CH32V103" -> "RISC-V QingKe V3A";
            case "CH32V203", "CH32V208" -> "RISC-V QingKe V4B";
            case "CH32V303" -> "RISC-V QingKe V4F";
            case "CH32V305", "CH32V307" -> "RISC-V QingKe V4F";
            case "CH32F103", "CH32F203" -> "ARM Cortex-M3";
            case "CH552", "CH554", "CH559" -> "8051 Enhanced";
            default -> "";
        };
    }

    /**
     * Get the interface type for USB chips.
     *
     * @param mpn the manufacturer part number
     * @return the interface type (e.g., "USB to UART", "USB to I2C/SPI")
     */
    public String getInterfaceType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "CH340", "CH9340" -> "USB to UART";
            case "CH341" -> "USB to UART/I2C/SPI/Parallel";
            case "CH342", "CH343", "CH344", "CH347" -> "USB to UART";
            case "CH345", "CH346" -> "USB to Parallel";
            case "CH348" -> "USB to 8x UART";
            case "CH395" -> "Ethernet Controller";
            case "CH9121" -> "Ethernet to UART";
            case "CH9141" -> "BLE to UART";
            case "CH9143" -> "BLE to UART";
            case "CH334" -> "USB Hub Controller";
            case "CH224" -> "USB PD Controller";
            default -> "";
        };
    }

    /**
     * Check if the MPN is a RISC-V MCU.
     *
     * @param mpn the manufacturer part number
     * @return true if RISC-V MCU
     */
    public boolean isRiscV(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;
        return mpn.toUpperCase().matches("^CH32V\\d{3}.*");
    }

    /**
     * Check if the MPN is an ARM MCU.
     *
     * @param mpn the manufacturer part number
     * @return true if ARM MCU
     */
    public boolean isArm(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;
        return mpn.toUpperCase().matches("^CH32F\\d{3}.*");
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

        // For USB interface chips: same series = compatible (package variants)
        if (series1.startsWith("CH34") || series1.equals("CH9340")) {
            return true;
        }

        // For MCUs: must be same line and pin count
        if (series1.startsWith("CH32")) {
            // Must have same pin count
            int pins1 = extractMCUPinCount(upper1);
            int pins2 = extractMCUPinCount(upper2);
            if (pins1 != pins2 || pins1 == -1) {
                return false;
            }

            // Same series and pin count = valid replacement
            // Flash size and package differences are acceptable
            return true;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
