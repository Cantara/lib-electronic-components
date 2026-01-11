package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;

public class AtmelHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ATmega Series - register for both base type and manufacturer-specific type
        // Pattern: ATMEGA + digits + optional variant letter(s) + optional -[speed][package]
        // Examples: ATMEGA328P, ATMEGA328P-PU, ATMEGA2560-16AU, ATMEGA32U4-AU
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATMEGA[0-9]+[A-Z]{0,2}(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATMEGA[0-9]+[A-Z]{0,2}(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MCU_ATMEL, "^ATMEGA[0-9]+[A-Z]{0,2}(?:-[0-9]*[A-Z]{2,3})?$");

        // ATtiny Series
        // Examples: ATTINY85, ATTINY85-20PU, ATTINY13A, ATTINY85V-10PU
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATTINY[0-9]+[A-Z]{0,2}(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATTINY[0-9]+[A-Z]{0,2}(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MCU_ATMEL, "^ATTINY[0-9]+[A-Z]{0,2}(?:-[0-9]*[A-Z]{2,3})?$");

        // AT90 Series
        // Examples: AT90USB162, AT90CAN128
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT90[A-Z]{2,4}[0-9]+(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^AT90[A-Z]{2,4}[0-9]+(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MCU_ATMEL, "^AT90[A-Z]{2,4}[0-9]+(?:-[0-9]*[A-Z]{2,3})?$");

        // XMEGA Series
        // Examples: ATXMEGA128A1U
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATXMEGA[0-9]+[A-Z][0-9]?[A-Z]?(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATXMEGA[0-9]+[A-Z][0-9]?[A-Z]?(?:-[0-9]*[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MCU_ATMEL, "^ATXMEGA[0-9]+[A-Z][0-9]?[A-Z]?(?:-[0-9]*[A-Z]{2,3})?$");

        // SAM Series (ARM-based)
        // Examples: ATSAM3X8E, ATSAM3X8E-AU, ATSAMD21G18A, ATSAMD21G18A-AU
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATSAM[A-Z0-9]+(?:-[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATSAM[A-Z0-9]+(?:-[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MCU_ATMEL, "^ATSAM[A-Z0-9]+(?:-[A-Z]{2,3})?$");

        // Memory Products - I2C EEPROM
        // Examples: AT24C256, AT24C256-PU, AT24C32
        registry.addPattern(ComponentType.MEMORY_ATMEL, "^AT24[A-Z][0-9]+(?:-[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MEMORY, "^AT24[A-Z][0-9]+(?:-[A-Z]{2,3})?$");

        // Memory Products - SPI EEPROM/Flash
        // Examples: AT25SF041, AT25DF321, AT25080 (note: some have 0-2 letters like SF, DF, or none)
        registry.addPattern(ComponentType.MEMORY_ATMEL, "^AT25[A-Z]{0,2}[0-9]+(?:-[A-Z]{2,3})?$");
        registry.addPattern(ComponentType.MEMORY, "^AT25[A-Z]{0,2}[0-9]+(?:-[A-Z]{2,3})?$");

        // Touch Controllers - QTouch
        // Examples: AT42QT1010, AT42QT1011, AT42QT2120
        registry.addPattern(ComponentType.TOUCH_ATMEL, "^AT42QT[0-9]+[A-Z]?(?:-[A-Z]{2,3})?$");

        // Touch Controllers - maXTouch
        // Examples: ATMXT336S, ATMXT224
        registry.addPattern(ComponentType.TOUCH_ATMEL, "^ATMXT[0-9]+[A-Z]?(?:-[A-Z]{2,3})?$");

        // Crypto Products - CryptoAuthentication
        // Examples: ATECC608A, ATECC508A, ATECC108A
        registry.addPattern(ComponentType.CRYPTO_ATMEL, "^ATECC[0-9]+[A-Z]?(?:-[A-Z]{2,3})?$");

        // Crypto Products - SHA Authentication
        // Examples: ATSHA204A, ATSHA206A
        registry.addPattern(ComponentType.CRYPTO_ATMEL, "^ATSHA[0-9]+[A-Z]?(?:-[A-Z]{2,3})?$");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MICROCONTROLLER,
                ComponentType.MICROCONTROLLER_ATMEL,
                ComponentType.MCU_ATMEL,
                ComponentType.MEMORY,
                ComponentType.MEMORY_ATMEL,
                ComponentType.AVR_MCU,
                ComponentType.TOUCH_ATMEL,
                ComponentType.CRYPTO_ATMEL
        );
    }


    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check if this is an Atmel MCU by prefix
        boolean isAtmelMcu = upperMpn.startsWith("ATMEGA") || upperMpn.startsWith("ATTINY") ||
                upperMpn.startsWith("AT90") || upperMpn.startsWith("ATXMEGA") ||
                upperMpn.startsWith("ATSAM");

        // For microcontroller types, use prefix check only (don't fall through to pattern match
        // which would incorrectly match other manufacturers' MCUs like STM32)
        if (type == ComponentType.MICROCONTROLLER_ATMEL ||
                type == ComponentType.MCU_ATMEL ||
                type == ComponentType.MICROCONTROLLER) {
            return isAtmelMcu;
        }

        // For all other types, use the registry's matches() which checks ALL patterns
        // (The default ManufacturerHandler.matches() only checks ONE pattern via getPattern())
        return patterns.matches(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract suffix after the last hyphen
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1].toUpperCase();

            // Handle speed grade + package (e.g., "20PU", "16AU")
            // Extract just the letter portion (package code)
            String packageCode = suffix.replaceAll("^[0-9]+", "");
            if (packageCode.isEmpty()) {
                return suffix; // No letters found, return as-is
            }

            return switch (packageCode) {
                case "PU" -> "PDIP";             // Plastic DIP
                case "AU" -> "TQFP";             // Thin QFP
                case "MU" -> "QFN/MLF";          // QFN/Micro Lead Frame
                case "CU" -> "UCSP/WLCSP";       // Wafer Level Chip Scale Package
                case "SU" -> "SOIC";             // Small Outline IC
                case "TU" -> "QFP";              // Quad Flat Pack
                case "XU" -> "TSSOP";            // Thin Shrink Small Outline Package
                case "SS" -> "SSOP";             // Shrink Small Outline Package
                default -> packageCode;
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // ATmega series
        if (mpn.startsWith("ATMEGA")) {
            int end = "ATMEGA".length();
            while (end < mpn.length() && Character.isDigit(mpn.charAt(end))) {
                end++;
            }
            return mpn.substring(0, end);
        }

        // ATtiny series
        if (mpn.startsWith("ATTINY")) {
            int end = "ATTINY".length();
            while (end < mpn.length() && Character.isDigit(mpn.charAt(end))) {
                end++;
            }
            return mpn.substring(0, end);
        }

        // AT90 series
        if (mpn.startsWith("AT90")) {
            return mpn.substring(0, Math.min(mpn.length(), 6));  // Include series letter
        }

        // XMEGA series
        if (mpn.startsWith("ATX")) {
            int end = "ATXMEGA".length();
            while (end < mpn.length() && Character.isDigit(mpn.charAt(end))) {
                end++;
            }
            return mpn.substring(0, end);
        }

        // SAM series
        if (mpn.startsWith("ATSAM")) {
            return mpn.substring(0, Math.min(mpn.length(), 7));  // Include series identifier
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1.toUpperCase());
        String series2 = extractSeries(mpn2.toUpperCase());

        // Must be same series - different packages of the same series ARE replacements
        // (e.g., ATMEGA328P-PU and ATMEGA328P-AU are pin-compatible just different footprints)
        return !series1.isEmpty() && series1.equals(series2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}