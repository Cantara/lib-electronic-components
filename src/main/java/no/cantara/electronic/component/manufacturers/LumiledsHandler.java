package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class LumiledsHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // LUXEON High Power LEDs
        registry.addPattern(ComponentType.LED, "^L1T2.*");              // LUXEON TX
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^L1T2.*");
        registry.addPattern(ComponentType.LED, "^L1M1.*");              // LUXEON M
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^L1M1.*");
        registry.addPattern(ComponentType.LED, "^L1V0.*");              // LUXEON V
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^L1V0.*");
        registry.addPattern(ComponentType.LED, "^LXML.*");              // LUXEON Rebel
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXML.*");
        registry.addPattern(ComponentType.LED, "^LXHL.*");              // LUXEON III
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXHL.*");

        // LUXEON Color LEDs
        registry.addPattern(ComponentType.LED, "^LXZ1.*");              // LUXEON Z
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXZ1.*");
        registry.addPattern(ComponentType.LED, "^LXCL.*");              // LUXEON C
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXCL.*");

        // RGB and Multi-die LEDs
        registry.addPattern(ComponentType.LED, "^LXI8.*");              // LUXEON Altilon
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXI8.*");
        registry.addPattern(ComponentType.LED, "^LXA7.*");              // LUXEON RGB
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXA7.*");

        // Mid-Power LEDs
        registry.addPattern(ComponentType.LED, "^L130.*");              // LUXEON 3030
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^L130.*");
        registry.addPattern(ComponentType.LED, "^L135.*");              // LUXEON 3535
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^L135.*");
        registry.addPattern(ComponentType.LED, "^L150.*");              // LUXEON 5050
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^L150.*");

        // Low-Power LEDs
        registry.addPattern(ComponentType.LED, "^LW Q18.*");            // LUXEON Low-Power
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LW Q18.*");

        // Automotive LEDs
        registry.addPattern(ComponentType.LED, "^LXA2.*");              // LUXEON Altilon
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXA2.*");
        registry.addPattern(ComponentType.LED, "^LXA3.*");              // LUXEON F
        registry.addPattern(ComponentType.LED_HIGHPOWER_LUMILEDS, "^LXA3.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.LED);
        types.add(ComponentType.LED_HIGHPOWER_LUMILEDS);
        // Include LUXEON series and other Lumileds specific LEDs
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle different package types
        if (mpn.startsWith("LXZ1")) {
            return "Z2";  // LUXEON Z package
        }

        if (mpn.startsWith("LXML")) {
            return "Rebel";  // LUXEON Rebel package
        }

        if (mpn.startsWith("L130")) {
            return "3030";  // 3.0x3.0mm package
        }

        if (mpn.startsWith("L135")) {
            return "3535";  // 3.5x3.5mm package
        }

        if (mpn.startsWith("L150")) {
            return "5050";  // 5.0x5.0mm package
        }

        // Extract package code from suffix
        String suffix = mpn.replaceAll("^[A-Z0-9]+", "");
        return switch (suffix) {
            case "PW" -> "Power";
            case "ES" -> "Emitter";
            case "DS" -> "DomeLED";
            case "UV" -> "UV";
            case "IR" -> "IR";
            case "RGB" -> "RGB";
            case "RGBW" -> "RGBW";
            default -> suffix;
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract series code
        if (mpn.startsWith("LX")) {
            // For LUXEON series (LXML, LXHL, etc.)
            StringBuilder series = new StringBuilder();
            for (int i = 0; i < mpn.length() && i < 4; i++) {
                if (Character.isLetterOrDigit(mpn.charAt(i))) {
                    series.append(mpn.charAt(i));
                }
            }
            return series.toString();
        }

        if (mpn.startsWith("L1") || mpn.startsWith("L2")) {
            // For newer series (L1T2, L1M1, etc.)
            return mpn.substring(0, 4);
        }

        // For numbered series (L130, L135, L150)
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

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series LEDs
        if (series1.equals(series2)) {
            // Check color code/bin if available
            String color1 = extractColorCode(mpn1);
            String color2 = extractColorCode(mpn2);

            // If color codes exist and match, or if one doesn't have a color code
            if (color1.equals(color2) || color1.isEmpty() || color2.isEmpty()) {
                return true;
            }
        }

        // Special case for cross-series compatibility
        if (isCompatibleSeries(series1, series2)) {
            return true;
        }

        return false;
    }

    private String extractColorCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract color code based on series
        if (mpn.startsWith("LXZ1") || mpn.startsWith("LXML")) {
            // Look for color code after dash
            int dashIndex = mpn.indexOf('-');
            if (dashIndex > 0 && dashIndex + 2 <= mpn.length()) {
                return mpn.substring(dashIndex + 1, dashIndex + 2);
            }
        }

        if (mpn.startsWith("L130") || mpn.startsWith("L135")) {
            // Color code is typically after series number
            if (mpn.length() >= 6) {
                return mpn.substring(4, 6);
            }
        }

        return "";
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Define compatible series pairs
        return (series1.equals("LXML") && series2.equals("LXZ1")) ||
                (series1.equals("LXZ1") && series2.equals("LXML")) ||
                (series1.equals("L130") && series2.equals("L135")) ||
                (series1.equals("L135") && series2.equals("L130"));
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}