package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class SpansionHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^S29[A-Z][A-Z].*");   // Parallel NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^S25FL.*");           // Serial NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^S25FS.*");           // Serial NOR Flash (High Performance)

        // NAND Flash
        registry.addPattern(ComponentType.MEMORY, "^S34ML.*");           // SLC NAND Flash
        registry.addPattern(ComponentType.MEMORY, "^S35ML.*");           // MLC NAND Flash

        // Microcontrollers
        registry.addPattern(ComponentType.MICROCONTROLLER, "^MB9[A-Z].*"); // FM3 Family
        registry.addPattern(ComponentType.MICROCONTROLLER, "^MB9B.*");     // FM4 Family

        // Interface ICs
        registry.addPattern(ComponentType.IC, "^GL850.*");                // USB Hub Controller
        registry.addPattern(ComponentType.IC, "^FL1K.*");                 // USB Flash Controller
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String suffix = mpn.replaceAll("^[A-Z0-9]+", "");
        return switch (suffix) {
            case "F" -> "FBGA";
            case "T" -> "TSOP";
            case "V" -> "VFBGA";
            case "S" -> "SO";
            case "W" -> "WSON";
            case "N" -> "PBGA";
            case "L" -> "PLCC";
            case "P" -> "PDIP";
            default -> suffix;
        };
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_FLASH);
        types.add(ComponentType.MICROCONTROLLER);
        // Note: Spansion was acquired by Cypress, which was then acquired by Infineon
        // Could add specific Spansion types if they exist in ComponentType enum
        return types;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract series code for different product families
        if (mpn.startsWith("S29") || mpn.startsWith("S25")) {
            // Flash memory series
            return mpn.substring(0, 4);
        }

        if (mpn.startsWith("S34") || mpn.startsWith("S35")) {
            // NAND Flash series
            return mpn.substring(0, 5);
        }

        if (mpn.startsWith("MB9")) {
            // Microcontroller series
            StringBuilder series = new StringBuilder("MB9");
            for (int i = 3; i < mpn.length(); i++) {
                char c = mpn.charAt(i);
                if (Character.isLetter(c)) {
                    series.append(c);
                } else {
                    break;
                }
            }
            return series.toString();
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series but different packages
        if (series1.equals(series2)) {
            // For Flash memories, check density and speed grade
            if (series1.startsWith("S2") || series1.startsWith("S3")) {
                String density1 = extractDensity(mpn1);
                String density2 = extractDensity(mpn2);
                String speed1 = extractSpeedGrade(mpn1);
                String speed2 = extractSpeedGrade(mpn2);

                return density1.equals(density2) &&
                        (speed1.equals(speed2) || speed1.isEmpty() || speed2.isEmpty());
            }

            // For MCUs, check variant compatibility
            if (series1.startsWith("MB9")) {
                return extractMCUVariant(mpn1).equals(extractMCUVariant(mpn2));
            }
        }

        return false;
    }

    private String extractDensity(String mpn) {
        // Extract memory density (e.g., "128" from S29GL128)
        if (mpn.matches(".*\\d+[MK].*")) {
            StringBuilder density = new StringBuilder();
            boolean foundDigit = false;
            for (char c : mpn.toCharArray()) {
                if (Character.isDigit(c)) {
                    density.append(c);
                    foundDigit = true;
                } else if (foundDigit && (c == 'M' || c == 'K')) {
                    density.append(c);
                    break;
                }
            }
            return density.toString();
        }
        return "";
    }

    private String extractSpeedGrade(String mpn) {
        // Extract speed grade (typically a number after density)
        int digitIndex = -1;
        for (int i = 0; i < mpn.length(); i++) {
            if (Character.isDigit(mpn.charAt(i))) {
                digitIndex = i;
                break;
            }
        }
        if (digitIndex >= 0 && digitIndex + 2 < mpn.length()) {
            char speedChar = mpn.charAt(digitIndex + 2);
            if (Character.isDigit(speedChar)) {
                return String.valueOf(speedChar);
            }
        }
        return "";
    }

    private String extractMCUVariant(String mpn) {
        // Extract MCU variant code (after series letter)
        if (mpn.startsWith("MB9")) {
            for (int i = 4; i < mpn.length(); i++) {
                if (Character.isDigit(mpn.charAt(i))) {
                    return mpn.substring(4, i);
                }
            }
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}