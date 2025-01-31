package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class YageoHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Chip Resistors
        registry.addPattern(ComponentType.RESISTOR, "^RC[0-9]{4}[A-Z0-9-]+");  // RC series
        registry.addPattern(ComponentType.RESISTOR_CHIP_YAGEO, "^RC[0-9]{4}[A-Z0-9-]+");

        // Add other Yageo resistor series
        registry.addPattern(ComponentType.RESISTOR, "^RT[0-9]{4}[A-Z0-9-]+");  // RT series
        registry.addPattern(ComponentType.RESISTOR_CHIP_YAGEO, "^RT[0-9]{4}[A-Z0-9-]+");

        registry.addPattern(ComponentType.RESISTOR, "^RL[0-9]{4}[A-Z0-9-]+");  // RL series
        registry.addPattern(ComponentType.RESISTOR_CHIP_YAGEO, "^RL[0-9]{4}[A-Z0-9-]+");
        // Chip Resistors
        registry.addPattern(ComponentType.RESISTOR, "^RC[0-9]{4}.*");         // Standard thick film
        registry.addPattern(ComponentType.RESISTOR_CHIP_YAGEO, "^RC[0-9]{4}.*");
        registry.addPattern(ComponentType.RESISTOR, "^RT[0-9]{4}.*");         // Thin film precision
        registry.addPattern(ComponentType.RESISTOR_CHIP_YAGEO, "^RT[0-9]{4}.*");
        registry.addPattern(ComponentType.RESISTOR, "^RL[0-9]{4}.*");         // Low resistance
        registry.addPattern(ComponentType.RESISTOR_CHIP_YAGEO, "^RL[0-9]{4}.*");

        // Ceramic Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^CC[0-9]{4}.*");        // Standard MLCC
        registry.addPattern(ComponentType.CAPACITOR_CERAMIC_YAGEO, "^CC[0-9]{4}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.RESISTOR);
        types.add(ComponentType.RESISTOR_CHIP_YAGEO);
        types.add(ComponentType.RESISTOR_THT_YAGEO);
        types.add(ComponentType.CAPACITOR);
        types.add(ComponentType.CAPACITOR_CERAMIC_YAGEO);
        types.add(ComponentType.INDUCTOR);
        types.add(ComponentType.INDUCTOR_CHIP_YAGEO);
        types.add(ComponentType.FERRITE_BEAD_YAGEO);
        return types;
    }


    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;
        // Direct check for resistor series
        if (type == ComponentType.RESISTOR ||
                type == ComponentType.RESISTOR_CHIP_YAGEO ||
                type == ComponentType.RESISTOR_THT_YAGEO) {

            String upperMpn = mpn.toUpperCase();
            if (upperMpn.matches("^RC[0-9]{4}[A-Z0-9-]+") ||  // RC series
                    upperMpn.matches("^RT[0-9]{4}[A-Z0-9-]+") ||  // RT series
                    upperMpn.matches("^RL[0-9]{4}[A-Z0-9-]+")) {  // RL series
                return true;
            }
        }

        String upperMpn = mpn.toUpperCase();

        // Special case for RC resistors
        if (type == ComponentType.RESISTOR_CHIP_YAGEO && upperMpn.startsWith("RC")) {
            // Check if it follows the size code pattern (RC0603, etc.)
            if (upperMpn.matches("^RC[0-9]{4}.*")) {
                return true;
            }
        }

        // Special case for RT resistors
        if (type == ComponentType.RESISTOR_CHIP_YAGEO && upperMpn.startsWith("RT")) {
            if (upperMpn.matches("^RT[0-9]{4}.*")) {
                return true;
            }
        }

        // Special case for RL resistors
        if (type == ComponentType.RESISTOR_CHIP_YAGEO && upperMpn.startsWith("RL")) {
            if (upperMpn.matches("^RL[0-9]{4}.*")) {
                return true;
            }
        }

        // Special case for ceramic capacitors
        if (type == ComponentType.CAPACITOR_CERAMIC_YAGEO && upperMpn.startsWith("CC")) {
            if (upperMpn.matches("^CC[0-9]{4}.*")) {
                return true;
            }
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract package size code for resistors and capacitors
        if (mpn.matches("^(?:RC|RT|RL|CC)[0-9]{4}.*")) {
            try {
                String sizeCode = mpn.substring(2, 6);
                return switch (sizeCode) {
                    case "0402" -> "0402";
                    case "0603" -> "0603";
                    case "0805" -> "0805";
                    case "1206" -> "1206";
                    case "1210" -> "1210";
                    case "2010" -> "2010";
                    case "2512" -> "2512";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Resistors
        if (mpn.startsWith("RC")) {
            String size = extractPackageCode(mpn);
            return "RC" + size;  // Include size in series identification
        }
        if (mpn.startsWith("RT")) {
            String size = extractPackageCode(mpn);
            return "RT" + size;  // Include size in series identification
        }
        if (mpn.startsWith("RL")) {
            String size = extractPackageCode(mpn);
            return "RL" + size;  // Include size in series identification
        }

        // Capacitors
        if (mpn.startsWith("CC")) {
            String size = extractPackageCode(mpn);
            return "CC" + size;  // Include size in series identification
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series and size
        if (!series1.equals(series2)) return false;

        // For resistors, check tolerance class if present
        if (mpn1.startsWith("RC") && mpn2.startsWith("RC")) {
            String tolerance1 = extractToleranceClass(mpn1);
            String tolerance2 = extractToleranceClass(mpn2);
            if (!tolerance1.isEmpty() && !tolerance2.isEmpty()) {
                return tolerance1.equals(tolerance2);
            }
        }

        return true;
    }

    private String extractToleranceClass(String mpn) {
        // Example: RC0603FR-07100RL -> F (1%)
        try {
            if (mpn.contains("-")) {
                String[] parts = mpn.split("-");
                if (parts[0].length() >= 7) {
                    return parts[0].substring(6, 7);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore and return empty
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}