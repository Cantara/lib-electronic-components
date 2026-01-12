package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;


import java.util.Set;
import java.util.Collections;

public class ISSIHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // SRAM
        registry.addPattern(ComponentType.MEMORY, "^IS61[A-Z][0-9].*");    // Async SRAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS61[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^IS62[A-Z][0-9].*");    // Low Power SRAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS62[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^IS64[A-Z][0-9].*");    // Sync SRAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS64[A-Z][0-9].*");

        // DRAM
        registry.addPattern(ComponentType.MEMORY, "^IS42[A-Z][0-9].*");    // SDRAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS42[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^IS43[A-Z][0-9].*");    // DDR SDRAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS43[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^IS45[A-Z][0-9].*");    // DDR2 SDRAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS45[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^IS47[A-Z][0-9].*");    // DDR3 SDRAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS47[A-Z][0-9].*");

        // Flash Memory
        registry.addPattern(ComponentType.MEMORY, "^IS25[A-Z][0-9].*");    // SPI Flash
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS25[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^IS29[A-Z][0-9].*");    // NOR Flash
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS29[A-Z][0-9].*");

        // LED Drivers
        registry.addPattern(ComponentType.IC, "^IS31FL.*");               // LED Matrix Drivers
        registry.addPattern(ComponentType.IC, "^IS31AP.*");               // Audio Modulated LED Drivers
        registry.addPattern(ComponentType.IC, "^IS31LT.*");               // LED Lighting Drivers

        // Specialty Memory
        registry.addPattern(ComponentType.MEMORY, "^IS65[A-Z][0-9].*");    // First-In-First-Out (FIFO)
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS65[A-Z][0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^IS67[A-Z][0-9].*");    // Dual-Port RAM
        registry.addPattern(ComponentType.MEMORY_ISSI, "^IS67[A-Z][0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MEMORY,
            ComponentType.MEMORY_ISSI,
            ComponentType.MEMORY_FLASH,
            ComponentType.MEMORY_EEPROM,
            ComponentType.IC  // For LED drivers
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Find the last letter sequence in the part number
        String[] parts = mpn.split("-");
        if (parts.length > 0) {
            String mainPart = parts[0];
            String suffix = mainPart.replaceAll("^[A-Z0-9]+", "");

            return switch (suffix) {
                case "T" -> "TSOP";
                case "U" -> "SOIC";
                case "L" -> "PLCC";
                case "V" -> "TSSOP";
                case "TL" -> "TSOP-II";
                case "TR" -> "TFBGA";
                case "B" -> "BGA";
                case "BK" -> "FBGA";
                case "WB" -> "WBGA";
                case "K" -> "VFBGA";
                case "M" -> "QFN";
                case "Q" -> "QFP";
                default -> suffix;
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract series code (first 4-5 characters before density code)
        StringBuilder series = new StringBuilder();
        boolean pastPrefix = false;
        int count = 0;

        for (char c : mpn.toCharArray()) {
            if (Character.isDigit(c) && !pastPrefix) {
                pastPrefix = true;
            }
            if (pastPrefix) {
                count++;
            }
            if (count <= 2) {  // Take first two characters after prefix
                series.append(c);
            } else {
                break;
            }
        }

        return series.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (!series1.equals(series2)) {
            return false;
        }

        // Extract density and organization
        String density1 = extractDensity(mpn1);
        String density2 = extractDensity(mpn2);

        // Same density and series are typically replaceable
        if (density1.equals(density2)) {
            return true;
        }

        // Check for speed grade compatibility
        String speed1 = extractSpeedGrade(mpn1);
        String speed2 = extractSpeedGrade(mpn2);

        // Faster parts can replace slower ones in same series
        if (!speed1.isEmpty() && !speed2.isEmpty()) {
            try {
                int speed1Val = Integer.parseInt(speed1);
                int speed2Val = Integer.parseInt(speed2);
                return speed1Val <= speed2Val;  // Lower number means faster
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    private String extractDensity(String mpn) {
        // Extract density code from part number
        if (mpn.length() >= 6) {
            // Density is typically digits after series code
            StringBuilder density = new StringBuilder();
            boolean foundDigit = false;
            for (char c : mpn.toCharArray()) {
                if (Character.isDigit(c)) {
                    foundDigit = true;
                    density.append(c);
                } else if (foundDigit) {
                    break;
                }
            }
            return density.toString();
        }
        return "";
    }

    private String extractSpeedGrade(String mpn) {
        // Extract speed grade from part number (typically after dash)
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