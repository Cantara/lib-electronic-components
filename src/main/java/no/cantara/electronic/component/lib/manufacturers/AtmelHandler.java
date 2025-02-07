package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AtmelHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ATmega Series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATMEGA[0-9].*");     // ATmega series
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATMEGA[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATMEGA[0-9].*P.*");  // With program memory
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATMEGA[0-9].*P.*");

        // ATtiny Series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATTINY[0-9].*");     // ATtiny series
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATTINY[0-9].*");

        // AT90 Series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^AT90[A-Z][0-9].*");  // AT90 series
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^AT90[A-Z][0-9].*");

        // XMEGA Series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATX(MEGA)[0-9].*");  // XMEGA series
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATX(MEGA)[0-9].*");

        // SAM Series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^ATSAM[0-9].*");      // SAM series
        registry.addPattern(ComponentType.MICROCONTROLLER_ATMEL, "^ATSAM[0-9].*");

        // Memory Products
        registry.addPattern(ComponentType.MEMORY, "^AT24[A-Z][0-9].*");          // I²C EEPROM
        registry.addPattern(ComponentType.MEMORY_ATMEL, "^AT24[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^AT25[A-Z][0-9].*");          // SPI EEPROM
        registry.addPattern(ComponentType.MEMORY_ATMEL, "^AT25[A-Z][0-9].*");

        // Touch Controllers
        registry.addPattern(ComponentType.TOUCH_ATMEL, "^AT42QT[0-9].*");        // QTouch controllers
        registry.addPattern(ComponentType.TOUCH_ATMEL, "^ATMXT[0-9].*");         // maXTouch controllers

        // Crypto Products
        registry.addPattern(ComponentType.CRYPTO_ATMEL, "^ATECC[0-9].*");        // CryptoAuthentication
        registry.addPattern(ComponentType.CRYPTO_ATMEL, "^ATSHA[0-9].*");        // SHA Authentication
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MICROCONTROLLER);
        types.add(ComponentType.MICROCONTROLLER_ATMEL);
        types.add(ComponentType.MCU_ATMEL);
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_ATMEL);
        types.add(ComponentType.AVR_MCU);
        types.add(ComponentType.TOUCH_ATMEL);
        types.add(ComponentType.CRYPTO_ATMEL);
        return types;
    }


    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Special case for microcontrollers
        if (type == ComponentType.MICROCONTROLLER_ATMEL &&
                (upperMpn.startsWith("ATMEGA") || upperMpn.startsWith("ATTINY") ||
                        upperMpn.startsWith("AT90") || upperMpn.startsWith("ATXMEGA") ||
                        upperMpn.startsWith("ATSAM"))) {
            return true;
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract suffix after the last hyphen
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];
            return switch (suffix) {
                case "PU" -> "PDIP";             // Plastic DIP
                case "AU" -> "TQFP";             // Thin QFP
                case "MU" -> "QFN/MLF";          // QFN/Micro Lead Frame
                case "CU" -> "UCSP/WLCSP";       // Wafer Level Chip Scale Package
                case "SU" -> "SOIC";             // Small Outline IC
                case "TU" -> "QFP";              // Quad Flat Pack
                case "XU" -> "TSSOP";            // Thin Shrink Small Outline Package
                default -> suffix;
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

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (!series1.equals(series2)) return false;

        // Check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If packages are specified and different, they're not compatible
        if (!pkg1.isEmpty() && !pkg2.isEmpty() && !pkg1.equals(pkg2)) {
            return false;
        }

        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}