package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;


public class PanasonicHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Chip Resistors
        // ERJ series (General purpose)
        registry.addPattern(ComponentType.RESISTOR, "^ERJ[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERJ[0-9].*");

        // ERJP series (Precision)
        registry.addPattern(ComponentType.RESISTOR, "^ERJP[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERJP[0-9].*");

        // ERJG series (Current sense)
        registry.addPattern(ComponentType.RESISTOR, "^ERJG[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERJG[0-9].*");

        // ERA series (High precision)
        registry.addPattern(ComponentType.RESISTOR, "^ERA[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERA[0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.RESISTOR);
        types.add(ComponentType.RESISTOR_CHIP_PANASONIC);
        types.add(ComponentType.CAPACITOR);
        types.add(ComponentType.CAPACITOR_CERAMIC_PANASONIC);
        types.add(ComponentType.CAPACITOR_ELECTROLYTIC_PANASONIC);
        types.add(ComponentType.CAPACITOR_FILM_PANASONIC);
        types.add(ComponentType.INDUCTOR);
        types.add(ComponentType.INDUCTOR_PANASONIC);
        return types;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Special case for ERJ resistors
        if (type == ComponentType.RESISTOR_CHIP_PANASONIC &&
                (upperMpn.startsWith("ERJ") || upperMpn.startsWith("ERA"))) {
            return true;
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // ERJ series size codes
        if (mpn.startsWith("ERJ")) {
            String sizeCode = mpn.substring(3, 4);
            return switch (sizeCode) {
                case "1" -> "0603";  // ERJ1
                case "2" -> "0402";  // ERJ2
                case "3" -> "0603";  // ERJ3
                case "6" -> "0805";  // ERJ6
                case "8" -> "1206";  // ERJ8
                case "M" -> "2512";  // ERJM
                default -> sizeCode;
            };
        }

        // ERA series size codes
        if (mpn.startsWith("ERA")) {
            String sizeCode = mpn.substring(3, 4);
            return switch (sizeCode) {
                case "1" -> "0402";  // ERA1
                case "2" -> "0603";  // ERA2
                case "3" -> "0603";  // ERA3
                case "6" -> "0805";  // ERA6
                case "8" -> "1206";  // ERA8
                default -> sizeCode;
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // ERJ series
        if (mpn.startsWith("ERJ")) {
            if (mpn.startsWith("ERJP")) return "ERJP";  // Precision
            if (mpn.startsWith("ERJG")) return "ERJG";  // Current sense
            return "ERJ";  // General purpose
        }

        // ERA series
        if (mpn.startsWith("ERA")) return "ERA";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (!series1.equals(series2)) return false;

        // Check package size
        String size1 = extractPackageCode(mpn1);
        String size2 = extractPackageCode(mpn2);

        return size1.equals(size2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}