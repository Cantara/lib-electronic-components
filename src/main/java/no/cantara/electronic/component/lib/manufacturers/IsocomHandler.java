package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Isocom Components - manufacturer of optocouplers and solid state relays.
 * <p>
 * Key product lines:
 * <ul>
 *   <li>ISP series: Phototransistor output optocouplers (ISP817, ISP827, ISP847)</li>
 *   <li>ISQ series: High CTR optocouplers (ISQ817, ISQ827)</li>
 *   <li>ISD series: Darlington output optocouplers (ISD817, ISD827)</li>
 *   <li>4N series: Standard optocouplers (4N25, 4N26, 4N35, 4N36)</li>
 *   <li>6N series: Logic output optocouplers (6N135, 6N136, 6N137, 6N138)</li>
 *   <li>MOC series: Triac/SCR driver optocouplers (MOC3020, MOC3021, MOC3041, MOC3042)</li>
 *   <li>TLP series: Toshiba-compatible optocouplers (TLP521, TLP621)</li>
 * </ul>
 * <p>
 * Package codes:
 * <ul>
 *   <li>-1 = DIP-4</li>
 *   <li>-2 = DIP-6</li>
 *   <li>-4 = DIP-8</li>
 *   <li>X = Lead-free (RoHS)</li>
 * </ul>
 */
public class IsocomHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ISP series - Phototransistor output optocouplers
        registry.addPattern(ComponentType.IC, "^ISP[0-9]{3}.*");

        // ISQ series - High CTR optocouplers
        registry.addPattern(ComponentType.IC, "^ISQ[0-9]{3}.*");

        // ISD series - Darlington output optocouplers
        registry.addPattern(ComponentType.IC, "^ISD[0-9]{3}.*");

        // 4N series - Standard optocouplers (4N25, 4N26, 4N27, 4N28, 4N35, 4N36, 4N37)
        registry.addPattern(ComponentType.IC, "^4N[0-9]{2}.*");

        // 6N series - Logic output optocouplers (6N135, 6N136, 6N137, 6N138, 6N139)
        registry.addPattern(ComponentType.IC, "^6N1[0-9]{2}.*");

        // MOC series - Triac/SCR driver optocouplers
        // MOC30xx - Zero-crossing triac drivers
        registry.addPattern(ComponentType.IC, "^MOC30[0-9]{2}.*");
        // MOC31xx - Random phase triac drivers
        registry.addPattern(ComponentType.IC, "^MOC31[0-9]{2}.*");
        // MOC32xx - SCR output
        registry.addPattern(ComponentType.IC, "^MOC32[0-9]{2}.*");

        // TLP series - Toshiba-compatible optocouplers
        registry.addPattern(ComponentType.IC, "^TLP[0-9]{3}.*");

        // H11 series - Various output optocouplers
        registry.addPattern(ComponentType.IC, "^H11[A-Z][0-9].*");

        // CNY series - Phototransistor output
        registry.addPattern(ComponentType.IC, "^CNY[0-9]{2}.*");

        // PC series - Sharp-compatible optocouplers
        registry.addPattern(ComponentType.IC, "^PC[0-9]{3}.*");

        // IL series - Vishay-compatible optocouplers
        registry.addPattern(ComponentType.IC, "^IL[0-9]{3}.*");

        // SFH series - Osram-compatible
        registry.addPattern(ComponentType.IC, "^SFH[0-9]{4}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        if (type == ComponentType.IC) {
            // Isocom-specific series (ISP, ISQ, ISD)
            if (upperMpn.matches("^IS[PQD][0-9]{3}.*")) {
                return true;
            }

            // Standard optocoupler series (4N, 6N)
            if (upperMpn.matches("^4N[0-9]{2}.*") ||
                upperMpn.matches("^6N1[0-9]{2}.*")) {
                return true;
            }

            // MOC series triac/SCR drivers
            if (upperMpn.matches("^MOC3[0-2][0-9]{2}.*")) {
                return true;
            }

            // TLP series (Toshiba-compatible)
            if (upperMpn.matches("^TLP[0-9]{3}.*")) {
                return true;
            }

            // H11 series
            if (upperMpn.matches("^H11[A-Z][0-9].*")) {
                return true;
            }

            // CNY series
            if (upperMpn.matches("^CNY[0-9]{2}.*")) {
                return true;
            }

            // PC series (Sharp-compatible)
            if (upperMpn.matches("^PC[0-9]{3}.*")) {
                return true;
            }

            // IL series (Vishay-compatible)
            if (upperMpn.matches("^IL[0-9]{3}.*")) {
                return true;
            }

            // SFH series (Osram-compatible)
            if (upperMpn.matches("^SFH[0-9]{4}.*")) {
                return true;
            }
        }

        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for hyphen-based package code (e.g., ISP817-1, ISP817-2, ISP817-4)
        int hyphenIndex = upperMpn.indexOf('-');
        if (hyphenIndex >= 0 && hyphenIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(hyphenIndex + 1);
            // Remove X suffix (lead-free indicator) if present for package extraction
            suffix = suffix.replace("X", "");

            return switch (suffix) {
                case "1" -> "DIP-4";
                case "2" -> "DIP-6";
                case "4" -> "DIP-8";
                case "1S", "2S", "4S" -> "SMD-" + suffix.charAt(0);  // SMD variants
                default -> suffix.isEmpty() ? "" : suffix;
            };
        }

        // Default packages based on series (check BEFORE trailing digit logic)
        // These have known standard packages
        if (upperMpn.startsWith("4N")) return "DIP-6";
        if (upperMpn.startsWith("6N")) return "DIP-8";
        if (upperMpn.startsWith("MOC")) return "DIP-6";
        if (upperMpn.startsWith("H11")) return "DIP-6";

        // For ISP/ISQ/ISD/TLP without hyphen but with trailing package indicator
        // Only apply to series that commonly use this format (not 4N, 6N, MOC, H11)
        if ((upperMpn.startsWith("ISP") || upperMpn.startsWith("ISQ") ||
             upperMpn.startsWith("ISD") || upperMpn.startsWith("TLP")) &&
            upperMpn.matches(".*[0-9][124]$")) {
            char lastChar = upperMpn.charAt(upperMpn.length() - 1);
            return switch (lastChar) {
                case '1' -> "DIP-4";
                case '2' -> "DIP-6";
                case '4' -> "DIP-8";
                default -> "";
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Isocom ISP/ISQ/ISD series
        if (upperMpn.matches("^ISP[0-9]{3}.*")) return "ISP";
        if (upperMpn.matches("^ISQ[0-9]{3}.*")) return "ISQ";
        if (upperMpn.matches("^ISD[0-9]{3}.*")) return "ISD";

        // 4N series with specific part numbers
        if (upperMpn.matches("^4N25.*")) return "4N25";
        if (upperMpn.matches("^4N26.*")) return "4N26";
        if (upperMpn.matches("^4N27.*")) return "4N27";
        if (upperMpn.matches("^4N28.*")) return "4N28";
        if (upperMpn.matches("^4N35.*")) return "4N35";
        if (upperMpn.matches("^4N36.*")) return "4N36";
        if (upperMpn.matches("^4N37.*")) return "4N37";
        if (upperMpn.matches("^4N[0-9]{2}.*")) return "4N";

        // 6N series with specific part numbers
        if (upperMpn.matches("^6N135.*")) return "6N135";
        if (upperMpn.matches("^6N136.*")) return "6N136";
        if (upperMpn.matches("^6N137.*")) return "6N137";
        if (upperMpn.matches("^6N138.*")) return "6N138";
        if (upperMpn.matches("^6N139.*")) return "6N139";
        if (upperMpn.matches("^6N1[0-9]{2}.*")) return "6N";

        // MOC series
        if (upperMpn.matches("^MOC30[0-9]{2}.*")) return "MOC30xx";
        if (upperMpn.matches("^MOC31[0-9]{2}.*")) return "MOC31xx";
        if (upperMpn.matches("^MOC32[0-9]{2}.*")) return "MOC32xx";
        if (upperMpn.matches("^MOC3[0-9]{3}.*")) return "MOC3xxx";

        // TLP series
        if (upperMpn.matches("^TLP[0-9]{3}.*")) {
            // Extract TLPxxx series (e.g., TLP521, TLP621)
            return upperMpn.substring(0, Math.min(6, upperMpn.length())).replaceAll("[^A-Z0-9]", "");
        }

        // H11 series
        if (upperMpn.matches("^H11[A-Z][0-9].*")) {
            return upperMpn.substring(0, 4);  // e.g., H11A
        }

        // CNY series
        if (upperMpn.matches("^CNY[0-9]{2}.*")) return "CNY";

        // PC series
        if (upperMpn.matches("^PC[0-9]{3}.*")) return "PC";

        // IL series
        if (upperMpn.matches("^IL[0-9]{3}.*")) return "IL";

        // SFH series
        if (upperMpn.matches("^SFH[0-9]{4}.*")) return "SFH";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;
        if (mpn1.isEmpty() || mpn2.isEmpty()) return false;

        String upperMpn1 = mpn1.toUpperCase();
        String upperMpn2 = mpn2.toUpperCase();

        // Check package compatibility first
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If packages are specified, they must be compatible
        if (!pkg1.isEmpty() && !pkg2.isEmpty() && !pkg1.equals(pkg2)) {
            return false;
        }

        // Check CTR grade compatibility for ISP/ISQ/ISD series
        // Higher CTR grade can replace lower (A > B > C > D typically)
        if (upperMpn1.matches("^IS[PQD][0-9]{3}.*") && upperMpn2.matches("^IS[PQD][0-9]{3}.*")) {
            String baseSeries1 = upperMpn1.substring(0, 3);  // ISP, ISQ, or ISD
            String baseSeries2 = upperMpn2.substring(0, 3);

            if (!baseSeries1.equals(baseSeries2)) {
                return false;  // Different IS* series are not compatible
            }

            // Extract base part number (e.g., ISP817 from ISP817A)
            String baseNum1 = extractBasePartNumber(upperMpn1);
            String baseNum2 = extractBasePartNumber(upperMpn2);

            if (!baseNum1.equals(baseNum2)) {
                return false;  // Different part numbers
            }

            char grade1 = extractCtrGrade(mpn1);
            char grade2 = extractCtrGrade(mpn2);

            if (grade1 != '\0' && grade2 != '\0') {
                // Higher letter = lower CTR, so grade2 >= grade1 means mpn1 can replace mpn2
                return grade2 >= grade1;
            }

            return true;
        }

        // For 4N series, check phototransistor specs
        if (upperMpn1.matches("^4N[0-9]{2}.*") && upperMpn2.matches("^4N[0-9]{2}.*")) {
            return isCompatible4NSeries(mpn1, mpn2);
        }

        // For 6N series
        if (upperMpn1.matches("^6N1[0-9]{2}.*") && upperMpn2.matches("^6N1[0-9]{2}.*")) {
            // Same base part number required
            String series1 = extractSeries(mpn1);
            String series2 = extractSeries(mpn2);
            return series1.equals(series2);
        }

        // For MOC series, check driver type compatibility
        if (upperMpn1.matches("^MOC3[0-9]{3}.*") && upperMpn2.matches("^MOC3[0-9]{3}.*")) {
            return isCompatibleMOCSeries(mpn1, mpn2);
        }

        // For TLP series
        if (upperMpn1.matches("^TLP[0-9]{3}.*") && upperMpn2.matches("^TLP[0-9]{3}.*")) {
            String series1 = extractSeries(mpn1);
            String series2 = extractSeries(mpn2);
            return series1.equals(series2);
        }

        // For other series, require exact series match
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        return !series1.isEmpty() && series1.equals(series2);
    }

    /**
     * Extract the base part number (e.g., ISP817 from ISP817A-1X)
     */
    private String extractBasePartNumber(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        StringBuilder base = new StringBuilder();
        for (int i = 0; i < mpn.length(); i++) {
            char c = mpn.charAt(i);
            if (Character.isLetter(c) || Character.isDigit(c)) {
                if (Character.isDigit(c) || i < 3) {
                    base.append(c);
                } else {
                    // Stop at first letter after the numeric part (CTR grade)
                    break;
                }
            } else {
                break;  // Stop at hyphen or other char
            }
        }
        return base.toString();
    }

    /**
     * Extract CTR (Current Transfer Ratio) grade from MPN.
     * Common grades: A (highest CTR), B, C, D (lowest CTR)
     */
    private char extractCtrGrade(String mpn) {
        if (mpn == null || mpn.isEmpty()) return '\0';

        String upperMpn = mpn.toUpperCase();

        // Look for grade letter after the numeric part
        // e.g., ISP817A, ISP817B, ISP817C, ISP817D
        for (int i = 0; i < upperMpn.length(); i++) {
            char c = upperMpn.charAt(i);
            if (c >= 'A' && c <= 'D') {
                // Check if this is after the part number
                if (i > 3 && Character.isDigit(upperMpn.charAt(i - 1))) {
                    return c;
                }
            }
        }

        return '\0';
    }

    /**
     * Check if two 4N series parts are compatible replacements.
     */
    private boolean isCompatible4NSeries(String mpn1, String mpn2) {
        String num1 = extractPartNumber(mpn1, "4N");
        String num2 = extractPartNumber(mpn2, "4N");

        // 4N25/4N26/4N27/4N28 are phototransistor output with different specs
        // 4N35/4N36/4N37 are also phototransistor output
        // Within each group, higher number typically has better isolation

        if (num1.equals(num2)) return true;

        // Group 1: 4N25-4N28
        Set<String> group1 = Set.of("25", "26", "27", "28");
        // Group 2: 4N35-4N37
        Set<String> group2 = Set.of("35", "36", "37");

        // Allow replacements within the same group
        return (group1.contains(num1) && group1.contains(num2)) ||
               (group2.contains(num1) && group2.contains(num2));
    }

    /**
     * Check if two MOC series parts are compatible replacements.
     */
    private boolean isCompatibleMOCSeries(String mpn1, String mpn2) {
        // MOC30xx - Zero-crossing triac drivers
        // MOC31xx - Random phase triac drivers
        // MOC32xx - SCR output

        // Can only replace within same driver type
        String prefix1 = extractMOCPrefix(mpn1);
        String prefix2 = extractMOCPrefix(mpn2);

        return prefix1.equals(prefix2);
    }

    private String extractPartNumber(String mpn, String prefix) {
        if (mpn == null || !mpn.toUpperCase().startsWith(prefix.toUpperCase())) {
            return "";
        }

        StringBuilder num = new StringBuilder();
        for (int i = prefix.length(); i < mpn.length(); i++) {
            char c = mpn.charAt(i);
            if (Character.isDigit(c)) {
                num.append(c);
            } else {
                break;
            }
        }
        return num.toString();
    }

    private String extractMOCPrefix(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (upperMpn.matches("^MOC30[0-9]{2}.*")) return "MOC30";
        if (upperMpn.matches("^MOC31[0-9]{2}.*")) return "MOC31";
        if (upperMpn.matches("^MOC32[0-9]{2}.*")) return "MOC32";

        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
