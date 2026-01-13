package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Viking Tech electronic components.
 * Viking Tech is a manufacturer of precision resistors including:
 * - CR series: Chip resistors (e.g., CR0603-FX-1001ELF)
 * - AR series: Anti-sulfur resistors (e.g., AR0603-FX-1002GLF)
 * - PA series: Power resistors
 * - CSR series: Current sense resistors (e.g., CSR0805-0R010F)
 *
 * MPN Structure:
 * - Series prefix (CR, AR, PA, CSR)
 * - Size code (0402, 0603, 0805, 1206, etc.)
 * - Tolerance code (F=1%, G=2%, J=5%)
 * - Value code (1001=1k, 1002=10k, 0R010=0.010 ohm)
 * - Suffix (ELF, GLF for lead-free)
 */
public class VikingTechHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // CR series - Standard chip resistors
        registry.addPattern(ComponentType.RESISTOR, "^CR[0-9]{4}.*");

        // AR series - Anti-sulfur resistors
        registry.addPattern(ComponentType.RESISTOR, "^AR[0-9]{4}.*");

        // PA series - Power resistors
        registry.addPattern(ComponentType.RESISTOR, "^PA[0-9]+.*");

        // CSR series - Current sense resistors
        registry.addPattern(ComponentType.RESISTOR, "^CSR[0-9]{4}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.RESISTOR,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        if (type == ComponentType.RESISTOR) {
            // CR series - Standard chip resistors
            if (upperMpn.matches("^CR[0-9]{4}.*")) {
                return true;
            }
            // AR series - Anti-sulfur resistors
            if (upperMpn.matches("^AR[0-9]{4}.*")) {
                return true;
            }
            // PA series - Power resistors
            if (upperMpn.matches("^PA[0-9]+.*")) {
                return true;
            }
            // CSR series - Current sense resistors
            if (upperMpn.matches("^CSR[0-9]{4}.*")) {
                return true;
            }
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // CR, AR series: CR0603-FX-1001ELF -> 0603
        if (upperMpn.matches("^(CR|AR)[0-9]{4}.*")) {
            try {
                String sizeCode = upperMpn.substring(2, 6);
                return mapSizeCode(sizeCode);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // CSR series: CSR0805-0R010F -> 0805
        if (upperMpn.matches("^CSR[0-9]{4}.*")) {
            try {
                String sizeCode = upperMpn.substring(3, 7);
                return mapSizeCode(sizeCode);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // PA series: PA0805... or PA1206...
        if (upperMpn.matches("^PA[0-9]{4}.*")) {
            try {
                String sizeCode = upperMpn.substring(2, 6);
                return mapSizeCode(sizeCode);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        return "";
    }

    private String mapSizeCode(String sizeCode) {
        return switch (sizeCode) {
            case "0201" -> "0201";
            case "0402" -> "0402";
            case "0603" -> "0603";
            case "0805" -> "0805";
            case "1206" -> "1206";
            case "1210" -> "1210";
            case "2010" -> "2010";
            case "2512" -> "2512";
            default -> sizeCode;
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // CR series
        if (upperMpn.startsWith("CR") && upperMpn.matches("^CR[0-9]{4}.*")) {
            String size = extractPackageCode(mpn);
            return "CR" + size;
        }

        // AR series
        if (upperMpn.startsWith("AR") && upperMpn.matches("^AR[0-9]{4}.*")) {
            String size = extractPackageCode(mpn);
            return "AR" + size;
        }

        // CSR series
        if (upperMpn.startsWith("CSR") && upperMpn.matches("^CSR[0-9]{4}.*")) {
            String size = extractPackageCode(mpn);
            return "CSR" + size;
        }

        // PA series
        if (upperMpn.startsWith("PA") && upperMpn.matches("^PA[0-9]+.*")) {
            String size = extractPackageCode(mpn);
            return size.isEmpty() ? "PA" : "PA" + size;
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series and size
        if (!series1.equals(series2) || series1.isEmpty()) return false;

        // For resistors, check tolerance class
        String tolerance1 = extractTolerance(mpn1);
        String tolerance2 = extractTolerance(mpn2);
        if (!tolerance1.isEmpty() && !tolerance2.isEmpty()) {
            return tolerance1.equals(tolerance2);
        }

        return true;
    }

    /**
     * Extract tolerance code from MPN.
     * F=1%, G=2%, J=5%
     */
    private String extractTolerance(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // CR/AR format: CR0603-FX-1001ELF - tolerance is after first hyphen
        if (upperMpn.matches("^(CR|AR)[0-9]{4}-[FGJ].*")) {
            int dashIndex = upperMpn.indexOf('-');
            if (dashIndex >= 0 && dashIndex + 1 < upperMpn.length()) {
                char toleranceChar = upperMpn.charAt(dashIndex + 1);
                if (toleranceChar == 'F' || toleranceChar == 'G' || toleranceChar == 'J') {
                    return String.valueOf(toleranceChar);
                }
            }
        }

        // CSR format: CSR0805-0R010F - tolerance is last char before suffix
        if (upperMpn.matches("^CSR[0-9]{4}-.*[FGJ].*")) {
            // Find the tolerance char - typically after the value
            for (int i = 8; i < upperMpn.length(); i++) {
                char c = upperMpn.charAt(i);
                if (c == 'F' || c == 'G' || c == 'J') {
                    return String.valueOf(c);
                }
            }
        }

        return "";
    }

    /**
     * Extract resistance value from MPN.
     * Returns value as a string (e.g., "1k", "10k", "0.010")
     */
    public String extractValue(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // CR/AR format: CR0603-FX-1001ELF - value is in third segment
        if (upperMpn.matches("^(CR|AR)[0-9]{4}-[A-Z]+-[0-9]+.*")) {
            String[] parts = upperMpn.split("-");
            if (parts.length >= 3) {
                String valuePart = parts[2].replaceAll("[A-Z]+$", ""); // Remove suffix like ELF
                return decodeResistorValue(valuePart);
            }
        }

        // CSR format: CSR0805-0R010F - value is after hyphen
        if (upperMpn.matches("^CSR[0-9]{4}-[0-9R]+.*")) {
            int dashIndex = upperMpn.indexOf('-');
            if (dashIndex >= 0) {
                String valuePart = upperMpn.substring(dashIndex + 1).replaceAll("[FGJ].*$", "");
                return decodeCurrentSenseValue(valuePart);
            }
        }

        return "";
    }

    private String decodeResistorValue(String code) {
        if (code == null || code.isEmpty()) return "";

        // Standard 4-digit code: 1001 = 100 * 10^1 = 1000 ohm = 1k
        // Format: ABC + D where ABC = significant digits, D = multiplier (10^D)
        if (code.matches("[0-9]{4}")) {
            int significantDigits = Integer.parseInt(code.substring(0, 3));
            int multiplier = Integer.parseInt(code.substring(3));
            double value = significantDigits * Math.pow(10, multiplier);

            if (value >= 1000000) {
                return String.format("%.0fM", value / 1000000);
            } else if (value >= 1000) {
                return String.format("%.0fk", value / 1000);
            } else {
                return String.format("%.0f", value);
            }
        }

        return code;
    }

    private String decodeCurrentSenseValue(String code) {
        if (code == null || code.isEmpty()) return "";

        // Current sense format: 0R010 = 0.010 ohm
        if (code.contains("R")) {
            return code.replace("R", ".");
        }

        return code;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
