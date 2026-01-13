package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;

/**
 * Handler for Elna Company components (primarily audio-grade capacitors).
 *
 * Elna specializes in high-quality aluminum electrolytic capacitors,
 * particularly for audio applications.
 *
 * Major product series:
 * - Silmic II (RFS series): Premium audio-grade electrolytic capacitors
 * - TONEREX (ROA/ROB series): Audio-grade electrolytic capacitors
 * - RE3/RJ3/RJH series: Standard aluminum electrolytic capacitors
 * - Dynacap (DB/DX/DZ series): Electric double layer capacitors (EDLC/supercaps)
 *
 * MPN format examples:
 * - RFS-25V101MH3#P (Silmic II: 25V 100uF audio grade)
 * - ROA-50V4R7MF3# (TONEREX: 50V 4.7uF audio grade)
 * - RE3-25V102M (Standard: 25V 1000uF)
 * - RJH-35V470M (High temp: 35V 47uF)
 * - DB-5R5D105T (Dynacap: 5.5V 1F EDLC)
 */
public class ElnaHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Silmic II series (RFS) - Premium audio grade
        // Format: RFS-VoltageVCapacitancePackage#Suffix
        // Example: RFS-25V101MH3#P (25V, 100uF, H3 package)
        registry.addPattern(ComponentType.CAPACITOR, "^RFS-[0-9]+V[0-9A-Z]+.*");

        // TONEREX series (ROA, ROB) - Audio grade
        // Format: ROx-VoltageVCapacitancePackage#
        // Example: ROA-50V4R7MF3# (50V, 4.7uF, F3 package)
        registry.addPattern(ComponentType.CAPACITOR, "^RO[AB]-[0-9]+V[0-9A-Z]+.*");

        // General audio/Hi-Fi series with R prefix and series letters
        // RE3 series - Standard aluminum electrolytic
        registry.addPattern(ComponentType.CAPACITOR, "^RE3-[0-9]+V[0-9]+.*");

        // RJ3/RJH series - Standard and high temperature aluminum electrolytic
        registry.addPattern(ComponentType.CAPACITOR, "^RJ[3H]-[0-9]+V[0-9]+.*");

        // RBD/RBI series - Bi-polar (non-polar) electrolytic
        registry.addPattern(ComponentType.CAPACITOR, "^RB[DI]-[0-9]+V[0-9]+.*");

        // RSE series - Super low ESR
        registry.addPattern(ComponentType.CAPACITOR, "^RSE-[0-9]+V[0-9]+.*");

        // RVD/RVE series - Low leakage
        registry.addPattern(ComponentType.CAPACITOR, "^RV[DE]-[0-9]+V[0-9]+.*");

        // Generic R-series pattern for any letter combination followed by voltage
        // This catches other R-prefixed series not explicitly listed
        registry.addPattern(ComponentType.CAPACITOR, "^R[A-Z]{2}-[0-9]+V.*");

        // Dynacap series (Electric Double Layer Capacitors / Supercaps)
        // DB series - Standard EDLC
        // DX series - Low profile EDLC
        // DZ series - Ultra-low profile EDLC
        // Format: Dxy-VoltageVCapacitancePackage
        // Example: DB-5R5D105T (5.5V, 1F, T package)
        registry.addPattern(ComponentType.CAPACITOR, "^D[BXZ]-[0-9]+R[0-9]+[A-Z][0-9]+.*");

        // Alternative Dynacap format with standard voltage notation
        registry.addPattern(ComponentType.CAPACITOR, "^D[BXZ][0-9]+.*");

        // STARGET series for older Elna audio components
        registry.addPattern(ComponentType.CAPACITOR, "^LAO[0-9]+.*");  // For Audio
        registry.addPattern(ComponentType.CAPACITOR, "^LAS[0-9]+.*");  // Standard

        // CE-BP series (bi-polar for audio crossovers)
        registry.addPattern(ComponentType.CAPACITOR, "^CE-BP.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CAPACITOR,
            ComponentType.IC  // For potential driver ICs, though Elna primarily makes capacitors
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Silmic II and TONEREX series: package code after capacitance value
        // Example: RFS-25V101MH3#P -> H3
        // Example: ROA-50V4R7MF3# -> F3
        if (upperMpn.matches("^R[A-Z]{2}-[0-9]+V.*")) {
            // Find the pattern after capacitance code (3 digits + M for tolerance)
            int idx = upperMpn.indexOf('M');
            if (idx > 0 && idx + 2 < upperMpn.length()) {
                // Extract characters after M until # or end
                String suffix = upperMpn.substring(idx + 1);
                int hashIdx = suffix.indexOf('#');
                String packagePart = hashIdx > 0 ? suffix.substring(0, hashIdx) : suffix;

                // Package codes are typically 1-2 letters followed by a digit
                if (packagePart.length() >= 2) {
                    return mapPackageCode(packagePart.substring(0, 2));
                }
            }
        }

        // Dynacap series: last character often indicates package
        if (upperMpn.matches("^D[BXZ]-.*") || upperMpn.matches("^D[BXZ][0-9].*")) {
            char lastChar = upperMpn.charAt(upperMpn.length() - 1);
            return switch (lastChar) {
                case 'T' -> "Radial THT";
                case 'V' -> "Vertical SMD";
                case 'H' -> "Horizontal SMD";
                case 'C' -> "Coin Cell";
                default -> "";
            };
        }

        return "";
    }

    /**
     * Maps Elna package codes to human-readable package descriptions.
     * Elna uses dimension codes that indicate can diameter and height.
     */
    private String mapPackageCode(String code) {
        if (code == null || code.length() < 2) return "";

        // First letter often indicates diameter range
        // Second character (digit) indicates height range
        // Common Elna dimension codes:
        return switch (code.toUpperCase()) {
            case "H3" -> "5x11mm";      // Small radial
            case "H5" -> "6.3x11mm";    // Standard radial
            case "H7" -> "8x11.5mm";    // Medium radial
            case "F3" -> "5x7mm";       // Low profile
            case "F5" -> "6.3x7mm";     // Low profile
            case "L5" -> "10x12.5mm";   // Large radial
            case "L7" -> "10x16mm";     // Large radial tall
            case "M5" -> "12.5x15mm";   // Extra large
            case "M8" -> "12.5x20mm";   // Extra large tall
            case "P5" -> "16x25mm";     // Power
            case "P8" -> "16x31.5mm";   // Power tall
            case "Q5" -> "18x25mm";     // High power
            case "R5" -> "22x25mm";     // Very high power
            default -> code;            // Return raw code if unknown
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Silmic II series
        if (upperMpn.startsWith("RFS-")) return "Silmic II";

        // TONEREX series
        if (upperMpn.startsWith("ROA-")) return "TONEREX Type A";
        if (upperMpn.startsWith("ROB-")) return "TONEREX Type B";

        // Standard aluminum electrolytic series
        if (upperMpn.startsWith("RE3-")) return "RE3 Standard";
        if (upperMpn.startsWith("RJ3-")) return "RJ3 Standard";
        if (upperMpn.startsWith("RJH-")) return "RJH High Temp";

        // Bi-polar series
        if (upperMpn.startsWith("RBD-")) return "RBD Bi-Polar";
        if (upperMpn.startsWith("RBI-")) return "RBI Bi-Polar";

        // Low ESR series
        if (upperMpn.startsWith("RSE-")) return "RSE Super Low ESR";

        // Low leakage series
        if (upperMpn.startsWith("RVD-")) return "RVD Low Leakage";
        if (upperMpn.startsWith("RVE-")) return "RVE Low Leakage";

        // Dynacap (EDLC) series
        if (upperMpn.startsWith("DB-") || upperMpn.startsWith("DB")) return "Dynacap Standard";
        if (upperMpn.startsWith("DX-") || upperMpn.startsWith("DX")) return "Dynacap Low Profile";
        if (upperMpn.startsWith("DZ-") || upperMpn.startsWith("DZ")) return "Dynacap Ultra-Low Profile";

        // STARGET/Legacy audio series
        if (upperMpn.startsWith("LAO")) return "STARGET Audio";
        if (upperMpn.startsWith("LAS")) return "STARGET Standard";

        // CE-BP bi-polar for audio crossovers
        if (upperMpn.startsWith("CE-BP")) return "CE-BP Audio Crossover";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are generally not replacements
        if (!series1.equals(series2) && !series1.isEmpty() && !series2.isEmpty()) {
            // Exception: Silmic II can replace TONEREX (higher grade)
            if (series1.startsWith("Silmic") && series2.startsWith("TONEREX")) {
                return haveSameVoltageAndCapacitance(mpn1, mpn2);
            }
            // TONEREX Type A and Type B are interchangeable
            if (series1.startsWith("TONEREX") && series2.startsWith("TONEREX")) {
                return haveSameVoltageAndCapacitance(mpn1, mpn2);
            }
            return false;
        }

        // Same series - check voltage and capacitance
        return haveSameVoltageAndCapacitance(mpn1, mpn2);
    }

    /**
     * Checks if two MPNs have the same voltage rating and capacitance value.
     * Used for replacement compatibility checking.
     */
    private boolean haveSameVoltageAndCapacitance(String mpn1, String mpn2) {
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);
        String cap1 = extractCapacitance(mpn1);
        String cap2 = extractCapacitance(mpn2);

        return !voltage1.isEmpty() && voltage1.equals(voltage2) &&
               !cap1.isEmpty() && cap1.equals(cap2);
    }

    /**
     * Extracts voltage rating from Elna MPN.
     * Format: R??-XXV... where XX is voltage
     * Example: RFS-25V101MH3#P -> 25
     */
    private String extractVoltage(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // Pattern: -XXV where XX is 1-3 digits
        int dashIdx = upperMpn.indexOf('-');
        if (dashIdx >= 0 && dashIdx + 1 < upperMpn.length()) {
            String afterDash = upperMpn.substring(dashIdx + 1);
            int vIdx = afterDash.indexOf('V');
            if (vIdx > 0 && vIdx <= 3) {
                return afterDash.substring(0, vIdx);
            }
        }
        return "";
    }

    /**
     * Extracts capacitance code from Elna MPN.
     * Format: ...V[cap]M... where [cap] is 3-char EIA code or R notation
     * Examples:
     *   RFS-25V101MH3#P -> 101 (100uF)
     *   ROA-50V4R7MF3# -> 4R7 (4.7uF)
     */
    private String extractCapacitance(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        int vIdx = upperMpn.indexOf('V');
        if (vIdx >= 0 && vIdx + 1 < upperMpn.length()) {
            String afterV = upperMpn.substring(vIdx + 1);
            // Look for capacitance value (3-4 chars before M)
            int mIdx = afterV.indexOf('M');
            if (mIdx >= 3) {
                return afterV.substring(0, mIdx);
            }
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're using generic ComponentTypes
        return Collections.emptySet();
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty()) return false;
        if (type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle CAPACITOR type (Elna primarily makes capacitors)
        if (type == ComponentType.CAPACITOR) {
            // Silmic II series
            if (upperMpn.matches("^RFS-[0-9]+V[0-9A-Z]+.*")) return true;

            // TONEREX series
            if (upperMpn.matches("^RO[AB]-[0-9]+V[0-9A-Z]+.*")) return true;

            // Standard R-series (RE3, RJ3, RJH, RBD, RBI, RSE, RVD, RVE, etc.)
            if (upperMpn.matches("^R[A-Z]{2}-[0-9]+V.*")) return true;

            // Dynacap series
            if (upperMpn.matches("^D[BXZ]-[0-9]+R[0-9]+[A-Z][0-9]+.*")) return true;
            if (upperMpn.matches("^D[BXZ][0-9]+.*")) return true;

            // Legacy/STARGET series
            if (upperMpn.matches("^LA[OS][0-9]+.*")) return true;

            // CE-BP series
            if (upperMpn.startsWith("CE-BP")) return true;
        }

        // Fall back to pattern registry for other types
        return patterns.matches(upperMpn, type);
    }
}
