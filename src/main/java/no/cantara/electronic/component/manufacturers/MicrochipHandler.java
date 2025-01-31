package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class MicrochipHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Add all dsPIC patterns first to ensure higher priority
        registry.addPattern(ComponentType.MICROCONTROLLER, "^dsPIC.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^dsPIC.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^dsPIC30F[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^dsPIC30F[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^dsPIC33FJ[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^dsPIC33FJ[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^dsPIC33EP[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^dsPIC33EP[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^dsPIC33EV[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^dsPIC33EV[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^dsPIC33CH[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^dsPIC33CH[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^dsPIC33CK[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^dsPIC33CK[0-9].*");

        // 8-bit PIC Microcontrollers - Basic Series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC10F[0-9].*");      // PIC10F series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC10F[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC10LF[0-9].*");     // PIC10LF series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC10LF[0-9].*");

        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC12F[0-9].*");      // PIC12F series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC12F[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC12LF[0-9].*");     // PIC12LF series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC12LF[0-9].*");

        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC16F[0-9].*");      // PIC16F series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC16F[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC16LF[0-9].*");     // PIC16LF series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC16LF[0-9].*");

        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC18F[0-9].*");      // PIC18F series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC18F[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC18LF[0-9].*");     // PIC18LF series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC18LF[0-9].*");

        // 16-bit PIC24 Microcontrollers
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC24F[0-9].*");      // PIC24F series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC24F[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC24FJ.*");          // PIC24FJ series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC24FJ.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC24EP.*");          // PIC24EP series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC24EP.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC24HJ.*");          // PIC24HJ series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC24HJ.*");

        // 32-bit PIC32 Microcontrollers
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC32MX.*");          // PIC32MX series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC32MX.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC32MZ.*");          // PIC32MZ series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC32MZ.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC32MM.*");          // PIC32MM series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC32MM.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC32MK.*");          // PIC32MK series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC32MK.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^PIC32WK.*");          // PIC32WK series
        registry.addPattern(ComponentType.MICROCONTROLLER_MICROCHIP, "^PIC32WK.*");

        // Memory Products
        registry.addPattern(ComponentType.MEMORY, "^24AA.*");                      // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^24AA.*");
        registry.addPattern(ComponentType.MEMORY, "^24LC.*");                      // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^24LC.*");
        registry.addPattern(ComponentType.MEMORY, "^24FC.*");                      // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^24FC.*");
        registry.addPattern(ComponentType.MEMORY, "^25AA.*");                      // SPI EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^25AA.*");
        registry.addPattern(ComponentType.MEMORY, "^25LC.*");                      // SPI EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^25LC.*");
        registry.addPattern(ComponentType.MEMORY, "^93[A|L]C.*");                  // Microwire EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^93[A|L]C.*");

        // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY, "^24AA[0-9].*");                      // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^24AA[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^24LC[0-9].*");                      // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^24LC[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^24FC[0-9].*");                      // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^24FC[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^AT24C[0-9].*");                     // I²C EEPROM (Atmel/Microchip)

        // SPI EEPROM
        registry.addPattern(ComponentType.MEMORY, "^25AA[0-9].*");                      // SPI EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^25AA[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^25LC[0-9].*");                      // SPI EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^25LC[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^AT25[A-Z][0-9].*");                // SPI EEPROM (Atmel/Microchip)
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^AT25[A-Z][0-9].*");

        // Microwire EEPROM
        registry.addPattern(ComponentType.MEMORY, "^93[A|L]C[0-9].*");                  // Microwire EEPROM
        registry.addPattern(ComponentType.MEMORY_MICROCHIP, "^93[A|L]C[0-9].*");

        // Interface ICs
        registry.addPattern(ComponentType.IC, "^MCP2515.*");                      // CAN Controller
        registry.addPattern(ComponentType.IC, "^MCP2517.*");                      // CAN FD Controller
        registry.addPattern(ComponentType.IC, "^MCP2518.*");                      // CAN FD Controller
        registry.addPattern(ComponentType.IC, "^MCP2551.*");                      // CAN Transceiver
        registry.addPattern(ComponentType.IC, "^MCP2561.*");                      // CAN FD Transceiver
        registry.addPattern(ComponentType.IC, "^MCP2562.*");                      // CAN FD Transceiver

        // USB Interface
        registry.addPattern(ComponentType.IC, "^MCP2200.*");                      // USB-UART Bridge
        registry.addPattern(ComponentType.IC, "^MCP2221.*");                      // USB-I2C/UART Bridge

        // Real-Time Clocks
        registry.addPattern(ComponentType.IC, "^MCP794[0-9].*");                  // RTCs
        registry.addPattern(ComponentType.IC, "^MCP795[0-9].*");                  // RTCs with SRAM
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MICROCONTROLLER);
        types.add(ComponentType.MICROCONTROLLER_MICROCHIP);
        types.add(ComponentType.MCU_MICROCHIP);
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_MICROCHIP);
        types.add(ComponentType.PIC_MCU);
        types.add(ComponentType.AVR_MCU);
        // Add for former Atmel products now under Microchip
        types.add(ComponentType.MICROCONTROLLER_ATMEL);
        types.add(ComponentType.MCU_ATMEL);
        types.add(ComponentType.MEMORY_ATMEL);
        types.add(ComponentType.TOUCH_ATMEL);
        types.add(ComponentType.CRYPTO_ATMEL);
        return types;
    }


    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Special case for memory devices
        if (type == ComponentType.MEMORY || type == ComponentType.MEMORY_MICROCHIP) {
            if (upperMpn.matches("^(24AA|24LC|24FC|AT24C)[0-9].*") ||    // I²C EEPROM
                    upperMpn.matches("^(25AA|25LC|AT25)[0-9].*") ||          // SPI EEPROM
                    upperMpn.matches("^93[A|L]C[0-9].*")) {                  // Microwire EEPROM
                return true;
            }
        }
        // Special case for dsPIC parts
        if (type == ComponentType.MICROCONTROLLER_MICROCHIP &&
                (upperMpn.startsWith("DSPIC") || upperMpn.startsWith("dsPIC"))) {
            return true;
        }

        // Special case for PIC microcontrollers
        if (type == ComponentType.MICROCONTROLLER_MICROCHIP && upperMpn.startsWith("PIC")) {
            return true;
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle package codes in suffix
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];
            return switch (suffix) {
                case "P", "PDI" -> "PDIP";           // Plastic DIP
                case "PT", "TQ" -> "TQFP";           // Thin QFP
                case "PF" -> "PLCC";                 // Plastic Leaded Chip Carrier
                case "ML", "MV" -> "QFN";            // Quad Flat No-Lead
                case "SO", "SM" -> "SOIC";           // Small Outline IC
                case "SP" -> "SPDIP";                // Skinny PDIP
                case "SS" -> "SSOP";                 // Shrink Small Outline Package
                case "ST" -> "TSSOP";                // Thin Shrink Small Outline Package
                case "IPT" -> "TQFP";                // Industrial TQFP
                default -> suffix;
            };
        }

        // Handle embedded package codes
        if (mpn.contains("/P")) return "PDIP";
        if (mpn.contains("/PT")) return "TQFP";
        if (mpn.contains("/ML")) return "QFN";
        if (mpn.contains("/SO")) return "SOIC";

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle PIC series
        if (mpn.startsWith("PIC")) {
            // Extract up to the first non-alphanumeric character
            StringBuilder series = new StringBuilder();
            for (char c : mpn.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    series.append(c);
                } else {
                    break;
                }
            }
            return series.toString();
        }

        // Handle dsPIC series
        if (mpn.startsWith("dsPIC")) {
            // Extract series including the FJ, EP, etc. suffix
            if (mpn.startsWith("dsPIC33")) {
                int end = "dsPIC33".length();
                while (end < mpn.length() && Character.isLetter(mpn.charAt(end))) {
                    end++;
                }
                return mpn.substring(0, end);
            }
            // For other dsPIC series
            return mpn.substring(0, Math.min(mpn.length(), 8));
        }

        // Handle memory series (24xx, 25xx, 93xx)
        if (mpn.matches("^(24|25|93)[A-Z]{1,2}[0-9].*")) {
            return mpn.substring(0, 4);
        }

        // Handle interface ICs (MCP series)
        if (mpn.startsWith("MCP")) {
            int end = "MCP".length();
            while (end < mpn.length() && Character.isDigit(mpn.charAt(end))) {
                end++;
            }
            return mpn.substring(0, end);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series for basic compatibility
        if (!series1.equals(series2)) {
            return false;
        }

        // Extract package types
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If packages are specified and different, they're not compatible
        if (!pkg1.isEmpty() && !pkg2.isEmpty() && !pkg1.equals(pkg2)) {
            return false;
        }

        // For dsPIC33 series, check sub-family compatibility
        if (series1.startsWith("dsPIC33")) {
            return isCompatibleDsPIC33(mpn1, mpn2);
        }

        // For memory devices, check density and speed grade
        if (mpn1.matches("^(24|25|93)[A-Z]{1,2}[0-9].*")) {
            return isCompatibleMemory(mpn1, mpn2);
        }

        // Same series PICs with same package are typically replaceable
        return true;
    }

    private boolean isCompatibleDsPIC33(String mpn1, String mpn2) {
        // Check if both are from the same sub-family (FJ, EP, etc.)
        String subFamily1 = mpn1.substring(7, 9);  // Get FJ, EP, etc.
        String subFamily2 = mpn2.substring(7, 9);

        return subFamily1.equals(subFamily2);
    }

    private boolean isCompatibleMemory(String mpn1, String mpn2) {
        // Extract memory size (typically indicated by numbers after series)
        String size1 = extractMemorySize(mpn1);
        String size2 = extractMemorySize(mpn2);

        // Must have same memory size
        if (!size1.equals(size2)) {
            return false;
        }

        // Check speed grade if present
        String speed1 = extractSpeedGrade(mpn1);
        String speed2 = extractSpeedGrade(mpn2);

        if (!speed1.isEmpty() && !speed2.isEmpty()) {
            try {
                int speed1Val = Integer.parseInt(speed1);
                int speed2Val = Integer.parseInt(speed2);
                // Lower number means faster speed
                return speed1Val <= speed2Val;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    private String extractMemorySize(String mpn) {
        // Extract numeric part after series identifier
        StringBuilder size = new StringBuilder();
        boolean foundDigit = false;
        for (char c : mpn.toCharArray()) {
            if (Character.isDigit(c)) {
                foundDigit = true;
                size.append(c);
            } else if (foundDigit) {
                break;
            }
        }
        return size.toString();
    }

    private String extractSpeedGrade(String mpn) {
        // Look for speed grade after hyphen
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            StringBuilder speed = new StringBuilder();
            for (char c : parts[1].toCharArray()) {
                if (Character.isDigit(c)) {
                    speed.append(c);
                } else {
                    break;
                }
            }
            return speed.toString();
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}