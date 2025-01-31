package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class MicronHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // DRAM
        registry.addPattern(ComponentType.MEMORY, "^MT4[0-9].*");         // DDR4 SDRAM
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT4[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^MT41[0-9].*");        // DDR3 SDRAM
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT41[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^MT47[0-9].*");        // DDR2 SDRAM
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT47[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^MT46[0-9].*");        // DDR SDRAM
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT46[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^MT48[0-9].*");        // LPDDR4
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT48[0-9].*");
        registry.addPattern(ComponentType.MEMORY, "^MT52[0-9].*");        // LPDDR3
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT52[0-9].*");

        // NAND Flash
        registry.addPattern(ComponentType.MEMORY, "^MT29[F|E].*");        // NAND Flash
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT29[F|E].*");
        registry.addPattern(ComponentType.MEMORY, "^MT29[C|B].*");        // SLC NAND
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT29[C|B].*");

        // NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^MT28[E|F].*");        // Parallel NOR Flash
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT28[E|F].*");
        registry.addPattern(ComponentType.MEMORY, "^MT25[Q|T].*");        // Serial NOR Flash
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT25[Q|T].*");

        // Phase Change Memory
        registry.addPattern(ComponentType.MEMORY, "^MT35[X|Q].*");        // 3D XPoint
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT35[X|Q].*");

        // Managed NAND
        registry.addPattern(ComponentType.MEMORY, "^MT29[K|L].*");        // eMMC
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MT29[K|L].*");
        registry.addPattern(ComponentType.MEMORY, "^MTFD.*");             // SSD
        registry.addPattern(ComponentType.MEMORY_MICRON, "^MTFD.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract package code from last part of MPN
        String[] parts = mpn.split("-");
        if (parts.length > 0) {
            String suffix = parts[parts.length - 1];
            return switch (suffix) {
                case "BGA" -> "BGA";
                case "FBGA" -> "FBGA";
                case "TFBGA" -> "TFBGA";
                case "VFBGA" -> "VFBGA";
                case "WBGA" -> "WBGA";
                case "TSOP" -> "TSOP";
                case "TSSOP" -> "TSSOP";
                case "SOP" -> "SOP";
                case "LGA" -> "LGA";
                default -> {
                    // Check for embedded package codes
                    if (suffix.contains("BGA")) yield "BGA";
                    if (suffix.contains("FBG")) yield "FBGA";
                    if (suffix.contains("TSO")) yield "TSOP";
                    yield suffix;
                }
            };
        }
        return "";
    }


    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_MICRON);
        types.add(ComponentType.MEMORY_FLASH);
        types.add(ComponentType.MEMORY_EEPROM);
        // Add any other specific Micron memory types from ComponentType enum
        return types;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base part number (first segment before dash)
        String[] parts = mpn.split("-");
        if (parts.length > 0) {
            String basePart = parts[0];

            // Extract series identifier (first 4-5 characters)
            StringBuilder series = new StringBuilder();
            int count = 0;
            for (char c : basePart.toCharArray()) {
                if (count < 5 && (Character.isLetterOrDigit(c) || c == '/')) {
                    series.append(c);
                    count++;
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

        // Must be same series for compatibility
        if (!series1.equals(series2)) {
            return false;
        }

        // Extract density and configuration
        String density1 = extractDensity(mpn1);
        String density2 = extractDensity(mpn2);

        // Extract speed grade
        String speed1 = extractSpeedGrade(mpn1);
        String speed2 = extractSpeedGrade(mpn2);

        // Same density is required for replacement
        if (!density1.equals(density2)) {
            return false;
        }

        // Compare speed grades if available
        if (!speed1.isEmpty() && !speed2.isEmpty()) {
            try {
                int speed1Val = Integer.parseInt(speed1);
                int speed2Val = Integer.parseInt(speed2);
                // Lower number means faster speed
                if (speed1Val > speed2Val) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Check temperature range compatibility
        String temp1 = extractTempRange(mpn1);
        String temp2 = extractTempRange(mpn2);

        return isCompatibleTempRange(temp1, temp2);
    }

    private String extractDensity(String mpn) {
        // Extract density information from part number
        if (mpn.length() >= 7) {
            // Look for density code after series identifier
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
        // Extract speed grade from part number
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String speedPart = parts[1];
            StringBuilder speed = new StringBuilder();
            for (char c : speedPart.toCharArray()) {
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

    private String extractTempRange(String mpn) {
        // Look for temperature range indicator
        if (mpn.contains("-I")) return "I";  // Industrial (-40 to +85)
        if (mpn.contains("-C")) return "C";  // Commercial (0 to +70)
        if (mpn.contains("-A")) return "A";  // Automotive (-40 to +105)
        if (mpn.contains("-E")) return "E";  // Extended (-40 to +95)
        return "";
    }

    private boolean isCompatibleTempRange(String range1, String range2) {
        // Temperature range compatibility rules
        // Automotive > Industrial > Extended > Commercial
        if (range1.equals(range2)) return true;
        if (range1.equals("A")) return true;  // Automotive can replace any
        if (range1.equals("I") && !range2.equals("A")) return true;
        if (range1.equals("E") && (range2.equals("C") || range2.isEmpty())) return true;
        if (range1.isEmpty() || range2.isEmpty()) return true;  // Allow if range not specified
        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}