package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;

/**
 * Handler for ESS Technology high-end audio DACs.
 *
 * ESS Technology Product Categories:
 * - ES9038 Series: Flagship Sabre DACs
 *   - ES9038PRO: 8-channel flagship DAC (32-bit, 8 channels, -140dB THD+N)
 *   - ES9038Q2M: 2-channel mobile DAC (optimized for portable)
 * - ES9028 Series: High-end Sabre DACs
 *   - ES9028PRO: 8-channel high-performance DAC
 *   - ES9028Q2M: 2-channel version
 * - ES9018 Series: Classic Sabre DACs
 *   - ES9018S: Reference stereo DAC
 *   - ES9018K2M: Mobile 2-channel DAC
 * - ES92xx Series: Portable DACs with integrated headphone amps
 *   - ES9218P: Portable with headphone amp, HiFi on mobile
 *   - ES9219: Newest portable DAC/amp
 *   - ES9281: USB DAC
 *
 * Package Code Examples:
 * - QFN: Quad Flat No-Lead (most common for audio DACs)
 * - TQFP: Thin Quad Flat Pack (larger pin count parts)
 * - WLCSP: Wafer Level Chip Scale Package (compact mobile parts)
 *
 * MPN Format: ES[90|92][0-9]{2}[A-Z0-9]*
 * Examples:
 * - ES9038PRO: Flagship 8-channel
 * - ES9038Q2M: 2-channel mobile version
 * - ES9218P-QFNR-48: Portable with package suffix
 * - ES9219C: Newest portable with grade suffix
 */
public class ESSHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ES9038 Series - Flagship Sabre DACs (8-channel and 2-channel variants)
        registry.addPattern(ComponentType.IC, "^ES9038.*");

        // ES9028 Series - High-end Sabre DACs
        registry.addPattern(ComponentType.IC, "^ES9028.*");

        // ES9018 Series - Classic Sabre DACs
        registry.addPattern(ComponentType.IC, "^ES9018.*");

        // ES9010/ES9016/ES9023 Series - Mid-range DACs
        registry.addPattern(ComponentType.IC, "^ES901[0-6].*");
        registry.addPattern(ComponentType.IC, "^ES902[0-3].*");

        // ES92xx Series - Portable DACs with headphone amps
        registry.addPattern(ComponentType.IC, "^ES92[0-9]{2}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for hyphenated suffix with package info
        // Format: ESxxxx-PACKAGE or ESxxxx-PACKAGE-pincount
        int dashIndex = upperMpn.indexOf('-');
        if (dashIndex > 0 && dashIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(dashIndex + 1);
            return decodePackageCode(suffix);
        }

        // Check for embedded package code without hyphen
        // Common patterns: ESxxxxQFN, ESxxxxTQFP
        String suffix = extractTrailingSuffix(upperMpn);
        if (!suffix.isEmpty()) {
            return decodePackageCode(suffix);
        }

        return "";
    }

    private String extractTrailingSuffix(String mpn) {
        if (mpn == null || mpn.length() < 7) return "";

        // Find where the model number ends
        // ES9038PRO, ES9218P, ES9028Q2M
        // Look for known suffixes after the base part number
        int i = 2; // Skip "ES"
        while (i < mpn.length() && Character.isDigit(mpn.charAt(i))) {
            i++;
        }

        // Now we might have PRO, Q2M, P, K2M, S, etc.
        // Skip the grade/variant suffix to find package
        int packageStart = i;
        while (packageStart < mpn.length()) {
            String remaining = mpn.substring(packageStart);
            // Check for package codes
            if (remaining.startsWith("QFN") || remaining.startsWith("TQFP") ||
                    remaining.startsWith("WLCSP") || remaining.startsWith("LQFP")) {
                return remaining;
            }
            packageStart++;
        }

        return "";
    }

    private String decodePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        String upperSuffix = suffix.toUpperCase();

        // Remove trailing reel/packaging indicators
        String cleanSuffix = upperSuffix.replaceAll("[-/].*$", "");
        // Also remove trailing -XX pin count indicators
        cleanSuffix = cleanSuffix.replaceAll("-[0-9]+$", "");

        // Direct package matches
        if (cleanSuffix.startsWith("QFN")) return "QFN";
        if (cleanSuffix.startsWith("TQFP")) return "TQFP";
        if (cleanSuffix.startsWith("WLCSP")) return "WLCSP";
        if (cleanSuffix.startsWith("LQFP")) return "LQFP";

        // Handle compound suffixes like QFNR (QFN tape and reel)
        if (cleanSuffix.contains("QFN")) return "QFN";
        if (cleanSuffix.contains("TQFP")) return "TQFP";
        if (cleanSuffix.contains("WLCSP")) return "WLCSP";
        if (cleanSuffix.contains("LQFP")) return "LQFP";

        return cleanSuffix;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Must start with ES
        if (!upperMpn.startsWith("ES")) return "";

        // Extract ES + 4 digits for the series
        // ES9038, ES9028, ES9018, ES9218, ES9219
        if (upperMpn.length() >= 6) {
            // Check that characters 2-5 are digits
            String digits = upperMpn.substring(2, 6);
            if (digits.chars().allMatch(Character::isDigit)) {
                return "ES" + digits;
            }
        }

        // Fallback: extract ES + available digits
        int i = 2;
        while (i < upperMpn.length() && Character.isDigit(upperMpn.charAt(i))) {
            i++;
        }
        if (i > 2) {
            return upperMpn.substring(0, i);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || series2.isEmpty()) return false;

        // Same series parts with different packages are replacements
        if (series1.equals(series2)) {
            return isSameBasePartNumber(mpn1, mpn2);
        }

        // Check for known compatible series within product families
        return isCompatibleSeries(series1, series2, mpn1, mpn2);
    }

    private boolean isSameBasePartNumber(String mpn1, String mpn2) {
        String base1 = extractBasePartNumber(mpn1);
        String base2 = extractBasePartNumber(mpn2);
        return base1.equals(base2);
    }

    private String extractBasePartNumber(String mpn) {
        String upper = mpn.toUpperCase();

        // Remove hyphenated suffix
        int dashIndex = upper.indexOf('-');
        if (dashIndex > 0) {
            return upper.substring(0, dashIndex);
        }

        // Remove trailing package codes
        String result = upper
                .replaceAll("QFN.*$", "")
                .replaceAll("TQFP.*$", "")
                .replaceAll("WLCSP.*$", "")
                .replaceAll("LQFP.*$", "");

        return result;
    }

    private boolean isCompatibleSeries(String series1, String series2, String mpn1, String mpn2) {
        // ES9038Q2M is a 2-channel version of ES9038PRO - NOT directly compatible
        // ES9028PRO vs ES9028Q2M - same base but different channel count
        // Generally, ESS DACs in same series but different channel counts are NOT replacements

        // Only same base number different packages are replacements
        String base1 = extractBasePartNumber(mpn1);
        String base2 = extractBasePartNumber(mpn2);

        return base1.equals(base2);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct check for IC type - the main type for ESS audio DACs
        if (type == ComponentType.IC) {
            // ES9038 Series - Flagship
            if (upperMpn.matches("^ES9038.*")) return true;

            // ES9028 Series - High-end
            if (upperMpn.matches("^ES9028.*")) return true;

            // ES9018 Series - Classic
            if (upperMpn.matches("^ES9018.*")) return true;

            // ES901x Series - Mid-range
            if (upperMpn.matches("^ES901[0-6].*")) return true;

            // ES902x Series - Mid-range
            if (upperMpn.matches("^ES902[0-3].*")) return true;

            // ES92xx Series - Portable
            if (upperMpn.matches("^ES92[0-9]{2}.*")) return true;
        }

        // Fall back to handler-specific patterns
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
