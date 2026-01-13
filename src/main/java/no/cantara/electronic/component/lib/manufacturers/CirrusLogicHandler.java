package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;

/**
 * Handler for Cirrus Logic audio ICs.
 *
 * Cirrus Logic Product Categories:
 * - CS42xx: Audio ADCs (CS4270, CS4272)
 * - CS43xx: Audio DACs/CODECs (CS4334, CS4344, CS43L22)
 * - CS47xx: DSP Audio (CS47L24, CS47L35)
 * - CS48xx: SoundClear DSP (CS4860x)
 * - CS53xx: LED Drivers (CS5361)
 * - CS84xx: Digital Audio Interface (CS8416, CS8422)
 * - WM8xxx: Wolfson Audio (acquired by Cirrus) (WM8731, WM8960, WM8994)
 *
 * Package Code Examples:
 * - CZZ: TSSOP
 * - CNZ: QFN
 * - DZZ: SSOP
 * - SEDS: SOIC
 * - CGEFL: QFN with exposed pad
 */
public class CirrusLogicHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Audio ADCs (CS42xx)
        registry.addPattern(ComponentType.IC, "^CS42[0-9]{2}.*");

        // Audio DACs/CODECs (CS43xx)
        registry.addPattern(ComponentType.IC, "^CS43[0-9A-Z]{2,4}.*");

        // DSP Audio (CS47xx)
        registry.addPattern(ComponentType.IC, "^CS47[0-9A-Z]{2,4}.*");

        // SoundClear DSP (CS48xx)
        registry.addPattern(ComponentType.IC, "^CS48[0-9]{2,3}.*");

        // LED Drivers (CS53xx)
        registry.addPattern(ComponentType.IC, "^CS53[0-9]{2}.*");

        // Digital Audio Interface (CS84xx)
        registry.addPattern(ComponentType.IC, "^CS84[0-9]{2}.*");

        // Wolfson Audio (WM8xxx) - acquired by Cirrus Logic
        registry.addPattern(ComponentType.IC, "^WM8[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^WM89[0-9]{2}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle Cirrus Logic CS series package codes
        // Format: CSxxxx-suffix where suffix contains package info
        if (upperMpn.startsWith("CS")) {
            int dashIndex = upperMpn.indexOf('-');
            if (dashIndex > 0 && dashIndex < upperMpn.length() - 1) {
                String suffix = upperMpn.substring(dashIndex + 1);
                return decodePackageCode(suffix);
            }
            // Check for embedded package codes without hyphen
            String suffix = extractTrailingSuffix(upperMpn);
            if (!suffix.isEmpty()) {
                return decodePackageCode(suffix);
            }
        }

        // Handle Wolfson WM8xxx package codes
        // Format: WM8xxxSUFFIX (no hyphen, package follows part number)
        if (upperMpn.startsWith("WM8")) {
            String suffix = extractWolfsonSuffix(upperMpn);
            if (!suffix.isEmpty()) {
                return decodeWolfsonPackageCode(suffix);
            }
        }

        return "";
    }

    private String extractTrailingSuffix(String mpn) {
        // Find where digits end and letters begin for package suffix
        int i = 0;
        while (i < mpn.length() && (Character.isLetter(mpn.charAt(i)) || Character.isDigit(mpn.charAt(i)))) {
            i++;
        }
        // Look for letter suffix after the main part number
        int letterStart = -1;
        for (int j = 4; j < mpn.length(); j++) { // Skip "CSxx" prefix
            char c = mpn.charAt(j);
            if (Character.isLetter(c) && !Character.isDigit(mpn.charAt(j > 0 ? j - 1 : 0))) {
                letterStart = j;
                break;
            }
        }
        if (letterStart > 0) {
            return mpn.substring(letterStart);
        }
        return "";
    }

    private String extractWolfsonSuffix(String mpn) {
        // WM8xxx followed by letters/numbers for package
        // Example: WM8731SEDS -> SEDS, WM8960CGEFL -> CGEFL
        if (mpn.length() <= 6) return "";

        // Find where the numeric part ends
        int numEnd = 6; // After "WM8xxx"
        while (numEnd < mpn.length() && Character.isDigit(mpn.charAt(numEnd))) {
            numEnd++;
        }

        if (numEnd < mpn.length()) {
            return mpn.substring(numEnd);
        }
        return "";
    }

    private String decodePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        // Remove any trailing reel/packaging indicators
        String cleanSuffix = suffix.replaceAll("[-/].*$", "");

        return switch (cleanSuffix.toUpperCase()) {
            case "CZZ" -> "TSSOP";
            case "CNZ" -> "QFN";
            case "DZZ" -> "SSOP";
            case "DZZR" -> "SSOP";
            case "CZZR" -> "TSSOP";
            case "CNZR" -> "QFN";
            default -> {
                // Check for partial matches
                if (cleanSuffix.startsWith("C") && cleanSuffix.length() >= 2) {
                    char second = cleanSuffix.charAt(1);
                    if (second == 'N') yield "QFN";
                    if (second == 'Z') yield "TSSOP";
                }
                if (cleanSuffix.startsWith("D") && cleanSuffix.length() >= 2) {
                    if (cleanSuffix.charAt(1) == 'Z') yield "SSOP";
                }
                yield cleanSuffix;
            }
        };
    }

    private String decodeWolfsonPackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        String cleanSuffix = suffix.toUpperCase();

        return switch (cleanSuffix) {
            case "SEDS" -> "SOIC";
            case "SEDS/V" -> "SOIC";
            case "CGEFL" -> "QFN";
            case "CGEFL/V" -> "QFN";
            case "CLQVP" -> "LQFP";
            case "CLQVP/V" -> "LQFP";
            case "CSBVP" -> "WLCSP";
            case "CSBVP/V" -> "WLCSP";
            case "EFL" -> "QFN";
            case "EFL/V" -> "QFN";
            case "EDS" -> "SOIC";
            case "EDS/V" -> "SOIC";
            case "GEFL" -> "QFN";
            case "GEFL/V" -> "QFN";
            default -> {
                // Check for common patterns
                if (cleanSuffix.contains("FL")) yield "QFN";
                if (cleanSuffix.contains("DS")) yield "SOIC";
                if (cleanSuffix.contains("QV") || cleanSuffix.contains("QFP")) yield "LQFP";
                if (cleanSuffix.contains("CSB") || cleanSuffix.contains("CSP")) yield "WLCSP";
                yield cleanSuffix;
            }
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle Cirrus Logic CS series
        // CS42xx, CS43xx, CS47xx, CS48xx, CS53xx, CS84xx
        if (upperMpn.startsWith("CS")) {
            // Extract CSxx (first 4 characters give the series)
            if (upperMpn.length() >= 4) {
                return upperMpn.substring(0, 4);
            }
            return upperMpn;
        }

        // Handle Wolfson WM8xxx series
        if (upperMpn.startsWith("WM8")) {
            // WM87xx, WM89xx, WM89xx
            if (upperMpn.length() >= 4) {
                return upperMpn.substring(0, 4);
            }
            return upperMpn;
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || series2.isEmpty()) return false;

        // Same series parts are typically compatible
        // (e.g., CS4344-CZZ and CS4344-CNZ are same part, different package)
        if (series1.equals(series2)) {
            return isSameBasePartNumber(mpn1, mpn2);
        }

        // Check for known compatible series within product families
        return isCompatibleSeries(series1, series2, mpn1, mpn2);
    }

    private boolean isSameBasePartNumber(String mpn1, String mpn2) {
        // Extract base part number (everything before package suffix)
        String base1 = extractBasePartNumber(mpn1);
        String base2 = extractBasePartNumber(mpn2);

        return base1.equals(base2);
    }

    private String extractBasePartNumber(String mpn) {
        String upper = mpn.toUpperCase();

        // For CS parts: CSxxxx or CSxxLxx
        if (upper.startsWith("CS")) {
            int dashIndex = upper.indexOf('-');
            if (dashIndex > 0) {
                return upper.substring(0, dashIndex);
            }
            // Find where package suffix starts (consecutive letters at end)
            int i = upper.length() - 1;
            while (i > 2 && Character.isLetter(upper.charAt(i))) {
                i--;
            }
            // Include the last letter if it's part of the part number (e.g., CS43L22)
            if (i > 2 && i < upper.length() - 2) {
                return upper.substring(0, i + 1);
            }
            return upper;
        }

        // For WM parts: WM8xxx
        if (upper.startsWith("WM8")) {
            // Extract WM8 + digits
            int i = 3;
            while (i < upper.length() && Character.isDigit(upper.charAt(i))) {
                i++;
            }
            return upper.substring(0, i);
        }

        return upper;
    }

    private boolean isCompatibleSeries(String series1, String series2, String mpn1, String mpn2) {
        // Within CS43xx DAC/CODEC family - some parts are pin-compatible
        // CS4334, CS4340, CS4344 are in the same basic DAC family
        if (series1.equals("CS43") && series2.equals("CS43")) {
            String base1 = extractBasePartNumber(mpn1);
            String base2 = extractBasePartNumber(mpn2);
            // Only consider same base part numbers as compatible
            return base1.equals(base2);
        }

        // Within CS42xx ADC family
        if (series1.equals("CS42") && series2.equals("CS42")) {
            String base1 = extractBasePartNumber(mpn1);
            String base2 = extractBasePartNumber(mpn2);
            return base1.equals(base2);
        }

        // Within WM8xxx Wolfson family
        if (series1.startsWith("WM8") && series2.startsWith("WM8")) {
            String base1 = extractBasePartNumber(mpn1);
            String base2 = extractBasePartNumber(mpn2);
            return base1.equals(base2);
        }

        return false;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct check for IC type - the main type for Cirrus Logic audio ICs
        if (type == ComponentType.IC) {
            // CS series
            if (upperMpn.matches("^CS42[0-9]{2}.*") ||    // Audio ADCs
                upperMpn.matches("^CS43[0-9A-Z]{2,4}.*") || // Audio DACs/CODECs
                upperMpn.matches("^CS47[0-9A-Z]{2,4}.*") || // DSP Audio
                upperMpn.matches("^CS48[0-9]{2,3}.*") ||   // SoundClear DSP
                upperMpn.matches("^CS53[0-9]{2}.*") ||    // LED Drivers
                upperMpn.matches("^CS84[0-9]{2}.*")) {    // Digital Audio Interface
                return true;
            }
            // Wolfson WM8xxx series
            if (upperMpn.matches("^WM8[0-9]{3}.*") ||
                upperMpn.matches("^WM89[0-9]{2}.*")) {
                return true;
            }
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
