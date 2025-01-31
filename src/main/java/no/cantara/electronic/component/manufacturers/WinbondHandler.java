package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.regex.Pattern;

public class WinbondHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Flash Memory
        registry.addPattern(ComponentType.MEMORY, "^W25[QNX][0-9]+.*");  // SPI/QSPI Flash
        registry.addPattern(ComponentType.MEMORY_FLASH, "^W25[QNX][0-9]+.*");  // SPI/QSPI Flash
        registry.addPattern(ComponentType.MEMORY_FLASH_WINBOND, "^W25[QNX][0-9]+.*");  // SPI/QSPI Flash

        // NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^W29[CNE][0-9]+.*");  // NOR Flash
        registry.addPattern(ComponentType.MEMORY_FLASH, "^W29[CNE][0-9]+.*");  // NOR Flash
        registry.addPattern(ComponentType.MEMORY_FLASH_WINBOND, "^W29[CNE][0-9]+.*");  // NOR Flash

        // EEPROM
        registry.addPattern(ComponentType.MEMORY, "^W24\\d+.*");       // EEPROM
        registry.addPattern(ComponentType.MEMORY_EEPROM, "^W24\\d+.*");       // EEPROM
        registry.addPattern(ComponentType.MEMORY_EEPROM_WINBOND, "^W24\\d+.*");       // EEPROM
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_FLASH_WINBOND);
        types.add(ComponentType.MEMORY_EEPROM_WINBOND);
        types.add(ComponentType.MEMORY_FLASH);
        types.add(ComponentType.MEMORY_EEPROM);
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        if (mpn.startsWith("W25") || mpn.startsWith("W29")) {
            // Extract package suffix
            int lastLetter = mpn.length() - 1;
            while (lastLetter >= 0 && Character.isLetter(mpn.charAt(lastLetter))) {
                lastLetter--;
            }
            String suffix = mpn.substring(lastLetter + 1);
            return switch (suffix.toUpperCase()) {
                case "S", "SS" -> "SOIC";
                case "F" -> "QFN";
                case "W" -> "WSON";
                case "U" -> "USON";
                default -> suffix;
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series (W25Q32, W29C020, etc.)
        if (mpn.startsWith("W25") || mpn.startsWith("W29")) {
            int end = 4;  // Start after W25 or W29
            while (end < mpn.length() &&
                    (Character.isLetterOrDigit(mpn.charAt(end)) || mpn.charAt(end) == '_')) {
                end++;
            }
            return mpn.substring(0, end);
        }

        return "";
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct flash memory check first
        if (type == ComponentType.MEMORY ||
                type == ComponentType.MEMORY_FLASH ||
                type == ComponentType.MEMORY_FLASH_WINBOND) {

            if (upperMpn.matches("^W25[QNX][0-9]+.*") ||  // SPI/QSPI Flash
                    upperMpn.matches("^W29[CNE][0-9]+.*")) {  // NOR Flash
                return true;
            }
        }

        // EEPROM check
        if (type == ComponentType.MEMORY ||
                type == ComponentType.MEMORY_EEPROM ||
                type == ComponentType.MEMORY_EEPROM_WINBOND) {

            if (upperMpn.matches("^W24\\d+.*")) {
                return true;
            }
        }

        // Use pattern registry for other matches
        Pattern pattern = patterns.getPattern(type);
        return pattern != null && pattern.matcher(upperMpn).matches();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Same series with different package or variant
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        return series1.equals(series2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}