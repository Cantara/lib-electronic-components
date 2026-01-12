package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;


public class PanasonicHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Chip Resistors
        // ERJ series (General purpose) - with and without hyphen
        registry.addPattern(ComponentType.RESISTOR, "^ERJ-?[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERJ-?[0-9].*");

        // ERJP series (Precision)
        registry.addPattern(ComponentType.RESISTOR, "^ERJP-?[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERJP-?[0-9].*");

        // ERJG series (Current sense)
        registry.addPattern(ComponentType.RESISTOR, "^ERJG-?[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERJG-?[0-9].*");

        // ERA series (High precision)
        registry.addPattern(ComponentType.RESISTOR, "^ERA-?[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_PANASONIC, "^ERA-?[0-9].*");

        // Aluminum Electrolytic Capacitors
        // EEE series (SMD low impedance)
        registry.addPattern(ComponentType.CAPACITOR, "^EEE[A-Z]{2}[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_PANASONIC, "^EEE[A-Z]{2}[0-9].*");

        // EEU series (THT standard)
        registry.addPattern(ComponentType.CAPACITOR, "^EEU-?[A-Z]{2}[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_PANASONIC, "^EEU-?[A-Z]{2}[0-9].*");

        // EEV series (SMD standard)
        registry.addPattern(ComponentType.CAPACITOR, "^EEV[A-Z]{2}[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_PANASONIC, "^EEV[A-Z]{2}[0-9].*");

        // ECA series (THT general purpose)
        registry.addPattern(ComponentType.CAPACITOR, "^ECA[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_PANASONIC, "^ECA[0-9].*");

        // Ceramic Capacitors
        // ECJ series
        registry.addPattern(ComponentType.CAPACITOR, "^ECJ[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_CERAMIC_PANASONIC, "^ECJ[0-9].*");

        // Film Capacitors
        // ECQ series
        registry.addPattern(ComponentType.CAPACITOR, "^ECQ[A-Z][0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_PANASONIC, "^ECQ[A-Z][0-9].*");

        // Inductors
        // ELC series (SMD power inductors)
        registry.addPattern(ComponentType.INDUCTOR, "^ELC[A-Z0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_PANASONIC, "^ELC[A-Z0-9].*");

        // ELJ series (chip inductors)
        registry.addPattern(ComponentType.INDUCTOR, "^ELJ[A-Z0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_PANASONIC, "^ELJ[A-Z0-9].*");

        // EXC series (common mode chokes)
        registry.addPattern(ComponentType.INDUCTOR, "^EXC[A-Z0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_PANASONIC, "^EXC[A-Z0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.RESISTOR,
            ComponentType.RESISTOR_CHIP_PANASONIC,
            ComponentType.CAPACITOR,
            ComponentType.CAPACITOR_CERAMIC_PANASONIC,
            ComponentType.CAPACITOR_ELECTROLYTIC_PANASONIC,
            ComponentType.CAPACITOR_FILM_PANASONIC,
            ComponentType.INDUCTOR,
            ComponentType.INDUCTOR_PANASONIC
        );
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

        // Normalize by removing hyphens for resistor MPN parsing
        String normalizedMpn = mpn.replace("-", "");

        // ERJ series size codes (position 3 after removing hyphen)
        if (normalizedMpn.startsWith("ERJ") && normalizedMpn.length() > 3) {
            String sizeCode = normalizedMpn.substring(3, 4);
            return switch (sizeCode) {
                case "1" -> "0603";  // ERJ1
                case "2" -> "0402";  // ERJ2
                case "3" -> "0603";  // ERJ3
                case "6" -> "0805";  // ERJ6
                case "8" -> "1206";  // ERJ8
                case "M" -> "2512";  // ERJM
                case "P" -> extractERJPPackageCode(normalizedMpn);  // ERJP precision
                case "G" -> extractERJGPackageCode(normalizedMpn);  // ERJG current sense
                default -> sizeCode;
            };
        }

        // ERA series size codes
        if (normalizedMpn.startsWith("ERA") && normalizedMpn.length() > 3) {
            String sizeCode = normalizedMpn.substring(3, 4);
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

    private String extractERJPPackageCode(String mpn) {
        // ERJP uses 2-digit size codes at position 4-5
        if (mpn.length() > 5) {
            String sizeCode = mpn.substring(4, 6);
            return switch (sizeCode) {
                case "02" -> "0402";
                case "03" -> "0603";
                case "06" -> "0805";
                case "08" -> "1206";
                default -> sizeCode;
            };
        }
        return "";
    }

    private String extractERJGPackageCode(String mpn) {
        // ERJG uses 2-digit size codes at position 4-5
        if (mpn.length() > 5) {
            String sizeCode = mpn.substring(4, 6);
            return switch (sizeCode) {
                case "02" -> "0402";
                case "03" -> "0603";
                case "06" -> "0805";
                case "08" -> "1206";
                default -> sizeCode;
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Normalize by removing hyphens
        String normalizedMpn = mpn.replace("-", "");

        // ERJ series
        if (normalizedMpn.startsWith("ERJ")) {
            if (normalizedMpn.startsWith("ERJP")) return "ERJP";  // Precision
            if (normalizedMpn.startsWith("ERJG")) return "ERJG";  // Current sense
            return "ERJ";  // General purpose
        }

        // ERA series
        if (normalizedMpn.startsWith("ERA")) return "ERA";

        // Capacitor series
        if (normalizedMpn.startsWith("EEE")) return "EEE";  // SMD electrolytic
        if (normalizedMpn.startsWith("EEU")) return "EEU";  // THT electrolytic
        if (normalizedMpn.startsWith("EEV")) return "EEV";  // SMD electrolytic
        if (normalizedMpn.startsWith("ECA")) return "ECA";  // THT electrolytic
        if (normalizedMpn.startsWith("ECJ")) return "ECJ";  // Ceramic
        if (normalizedMpn.startsWith("ECQ")) return "ECQ";  // Film

        // Inductor series
        if (normalizedMpn.startsWith("ELC")) return "ELC";  // Power inductor
        if (normalizedMpn.startsWith("ELJ")) return "ELJ";  // Chip inductor
        if (normalizedMpn.startsWith("EXC")) return "EXC";  // Common mode choke

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