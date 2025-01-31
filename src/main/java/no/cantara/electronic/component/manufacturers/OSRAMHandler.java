package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class OSRAMHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Standard LEDs
        registry.addPattern(ComponentType.LED, "^LS[A-Z][0-9].*");       // Standard LEDs
        registry.addPattern(ComponentType.LED_STANDARD_OSRAM, "^LS[A-Z][0-9].*");

        // SMD LEDs
        registry.addPattern(ComponentType.LED, "^LW[A-Z][0-9].*");       // White SMD LEDs
        registry.addPattern(ComponentType.LED_SMD_OSRAM, "^LW[A-Z][0-9].*");
        registry.addPattern(ComponentType.LED, "^LA[A-Z][0-9].*");       // Amber SMD LEDs
        registry.addPattern(ComponentType.LED_SMD_OSRAM, "^LA[A-Z][0-9].*");
        registry.addPattern(ComponentType.LED, "^LY[A-Z][0-9].*");       // Yellow SMD LEDs
        registry.addPattern(ComponentType.LED_SMD_OSRAM, "^LY[A-Z][0-9].*");
        registry.addPattern(ComponentType.LED, "^LB[A-Z][0-9].*");       // Blue SMD LEDs
        registry.addPattern(ComponentType.LED_SMD_OSRAM, "^LB[A-Z][0-9].*");
        registry.addPattern(ComponentType.LED, "^LR[A-Z][0-9].*");       // Red SMD LEDs
        registry.addPattern(ComponentType.LED_SMD_OSRAM, "^LR[A-Z][0-9].*");
        registry.addPattern(ComponentType.LED, "^LG[A-Z][0-9].*");       // Green SMD LEDs
        registry.addPattern(ComponentType.LED_SMD_OSRAM, "^LG[A-Z][0-9].*");

        // High Power LEDs
        registry.addPattern(ComponentType.LED, "^OSLON.*");              // OSLON series
        registry.addPattern(ComponentType.LED_HIGHPOWER_OSRAM, "^OSLON.*");
        registry.addPattern(ComponentType.LED, "^OSRAM.*");              // OSRAM series
        registry.addPattern(ComponentType.LED_HIGHPOWER_OSRAM, "^OSRAM.*");
        registry.addPattern(ComponentType.LED, "^DURIS.*");              // DURIS series
        registry.addPattern(ComponentType.LED_HIGHPOWER_OSRAM, "^DURIS.*");

        // RGB LEDs
        registry.addPattern(ComponentType.LED, "^LRTB.*");               // RGB LEDs
        registry.addPattern(ComponentType.LED_RGB_OSRAM, "^LRTB.*");
        registry.addPattern(ComponentType.LED, "^LBCW.*");               // RGBW LEDs
        registry.addPattern(ComponentType.LED_RGB_OSRAM, "^LBCW.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.LED);
        types.add(ComponentType.LED_STANDARD_OSRAM);
        types.add(ComponentType.LED_RGB_OSRAM);
        types.add(ComponentType.LED_SMD_OSRAM);
        types.add(ComponentType.LED_HIGHPOWER_OSRAM);
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle different package types
        if (mpn.startsWith("LS")) {
            return "THT";  // Through-hole package
        }

        if (mpn.startsWith("LW") || mpn.startsWith("LA") ||
                mpn.startsWith("LY") || mpn.startsWith("LB") ||
                mpn.startsWith("LR") || mpn.startsWith("LG")) {
            String suffix = mpn.replaceAll("^[A-Z0-9]+", "");
            return switch (suffix) {
                case "G6" -> "PLCC-6";
                case "G4" -> "PLCC-4";
                case "T2" -> "TOP-LED";
                case "CH" -> "Chip-LED";
                case "MS" -> "Mini-SIDELED";
                case "P2" -> "Power-TOPLED";
                case "CR" -> "Ceramic";
                default -> suffix;
            };
        }

        if (mpn.startsWith("OSLON") || mpn.startsWith("DURIS")) {
            String suffix = mpn.replaceAll("^[A-Z0-9]+", "");
            return switch (suffix) {
                case "SX" -> "Square";
                case "SSL" -> "Square Lite";
                case "S5" -> "Square 5mm";
                case "P3" -> "PowerStar";
                default -> suffix;
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle standard and SMD LEDs
        if (mpn.startsWith("LS") || mpn.startsWith("LW") ||
                mpn.startsWith("LA") || mpn.startsWith("LY") ||
                mpn.startsWith("LB") || mpn.startsWith("LR") ||
                mpn.startsWith("LG")) {
            // Extract series code (first 3-4 characters)
            StringBuilder series = new StringBuilder();
            for (int i = 0; i < mpn.length() && i < 4; i++) {
                if (Character.isLetterOrDigit(mpn.charAt(i))) {
                    series.append(mpn.charAt(i));
                }
            }
            return series.toString();
        }

        // Handle high power LEDs
        if (mpn.startsWith("OSLON") || mpn.startsWith("DURIS")) {
            int spaceIndex = mpn.indexOf(' ');
            return spaceIndex > 0 ? mpn.substring(0, spaceIndex) : mpn;
        }

        // Handle RGB LEDs
        if (mpn.startsWith("LRTB") || mpn.startsWith("LBCW")) {
            return mpn.substring(0, 4);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series LEDs with different packages
        if (series1.equals(series2)) {
            // For color LEDs, check color code matches
            if ((mpn1.startsWith("L") && mpn2.startsWith("L")) &&
                    (mpn1.charAt(1) == mpn2.charAt(1))) {  // Compare color code (R,G,B,W,A,Y)
                return true;
            }

            // For OSLON/DURIS series, check base part
            if ((mpn1.startsWith("OSLON") && mpn2.startsWith("OSLON")) ||
                    (mpn1.startsWith("DURIS") && mpn2.startsWith("DURIS"))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}