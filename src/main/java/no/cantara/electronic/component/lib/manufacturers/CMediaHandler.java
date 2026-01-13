package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;

/**
 * Handler for C-Media Electronics USB audio controller ICs.
 *
 * C-Media Product Categories:
 * - CM108 series: USB Audio Codec (CM108, CM108AH, CM108B)
 * - CM119 series: USB 7.1 Channel Audio (CM119, CM119A, CM119B)
 * - CM6xxx series: Professional USB Audio (CM6206, CM6631, CM6632)
 * - CMI87xx series: HD Audio Codec (CMI8738, CMI8768, CMI8788)
 *
 * Package Code Examples:
 * - QFP: Quad Flat Package (various pin counts)
 * - LQFP: Low-profile Quad Flat Package
 * - QFN: Quad Flat No-lead
 * - SSOP: Shrink Small Outline Package
 */
public class CMediaHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // CM108 series - USB Audio Codec (basic stereo)
        // CM108, CM108AH, CM108B, etc.
        registry.addPattern(ComponentType.IC, "^CM108[A-Z]*.*");

        // CM109 series - USB Audio with HID keyboard support
        registry.addPattern(ComponentType.IC, "^CM109[A-Z]*.*");

        // CM119 series - USB 7.1 Channel Audio
        // CM119, CM119A, CM119B, etc.
        registry.addPattern(ComponentType.IC, "^CM119[A-Z]*.*");

        // CM6xxx series - Professional USB Audio
        // CM6206: USB 2.0 audio, CM6631: USB 2.0 audio processor
        // CM6632: USB audio DAC, CM6400: USB audio
        registry.addPattern(ComponentType.IC, "^CM6[0-9]{3}[A-Z]*.*");

        // CMI87xx series - HD Audio Codec (PCI/PCIe)
        // CMI8738, CMI8768, CMI8788 (Oxygen HD Audio)
        registry.addPattern(ComponentType.IC, "^CMI87[0-9]{2}[A-Z]*.*");

        // CMI83xx series - Legacy PCI audio
        registry.addPattern(ComponentType.IC, "^CMI83[0-9]{2}[A-Z]*.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle hyphenated suffixes (e.g., CM108AH-QFP48)
        int dashIndex = upperMpn.indexOf('-');
        if (dashIndex > 0 && dashIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(dashIndex + 1);
            return decodePackageCode(suffix);
        }

        // Handle embedded package codes without hyphen
        // Look for common package indicators at the end
        String suffix = extractTrailingSuffix(upperMpn);
        if (!suffix.isEmpty()) {
            return decodePackageCode(suffix);
        }

        return "";
    }

    private String extractTrailingSuffix(String mpn) {
        // Find where the main part number ends and package suffix begins
        // C-Media parts typically have format: CMxxx[variant][package]
        // Examples: CM108AH, CM6206-LQ, CM6631A-S

        // For CM108/CM109/CM119 series
        if (mpn.matches("^CM10[89][A-Z]*.*") || mpn.matches("^CM119[A-Z]*.*")) {
            // Extract letters after the number that indicate package
            int i = 5; // After "CM108" or "CM119"
            while (i < mpn.length() && Character.isLetter(mpn.charAt(i))) {
                i++;
            }
            // If there are more characters, they could be package info
            if (i < mpn.length()) {
                return mpn.substring(i);
            }
        }

        // For CM6xxx series
        if (mpn.matches("^CM6[0-9]{3}.*")) {
            int i = 6; // After "CM6xxx"
            while (i < mpn.length() && Character.isLetter(mpn.charAt(i))) {
                i++;
            }
            if (i < mpn.length()) {
                return mpn.substring(i);
            }
            // Check for single letter suffix variants
            if (mpn.length() > 6 && Character.isLetter(mpn.charAt(6))) {
                // Variant letter like CM6631A - not package
                return "";
            }
        }

        // For CMI87xx series
        if (mpn.matches("^CMI87[0-9]{2}.*")) {
            int i = 7; // After "CMI87xx"
            while (i < mpn.length() && Character.isLetter(mpn.charAt(i))) {
                i++;
            }
            if (i > 7) {
                return mpn.substring(7, i);
            }
        }

        return "";
    }

    private String decodePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        String cleanSuffix = suffix.toUpperCase().replaceAll("[-/].*$", "");

        // Remove pin count digits to get pure package type
        String packageType = cleanSuffix.replaceAll("[0-9]+$", "");

        return switch (packageType) {
            case "QFP" -> "QFP";
            case "LQFP", "LQ" -> "LQFP";
            case "QFN" -> "QFN";
            case "SSOP", "S" -> "SSOP";
            case "TQFP" -> "TQFP";
            case "PLCC" -> "PLCC";
            case "BGA" -> "BGA";
            default -> {
                // Check for partial matches
                if (cleanSuffix.startsWith("LQ")) yield "LQFP";
                if (cleanSuffix.startsWith("QF")) yield "QFP";
                if (cleanSuffix.startsWith("SS")) yield "SSOP";
                yield cleanSuffix;
            }
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // CM108 series
        if (upperMpn.matches("^CM108.*")) {
            return "CM108";
        }

        // CM109 series
        if (upperMpn.matches("^CM109.*")) {
            return "CM109";
        }

        // CM119 series
        if (upperMpn.matches("^CM119.*")) {
            return "CM119";
        }

        // CM6xxx series - extract full 4-digit series
        if (upperMpn.matches("^CM6[0-9]{3}.*")) {
            return upperMpn.substring(0, 6);
        }

        // CMI87xx series
        if (upperMpn.matches("^CMI87[0-9]{2}.*")) {
            return upperMpn.substring(0, 7);
        }

        // CMI83xx series
        if (upperMpn.matches("^CMI83[0-9]{2}.*")) {
            return upperMpn.substring(0, 7);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || series2.isEmpty()) return false;

        // Same series parts are typically compatible (different packages or variants)
        if (series1.equals(series2)) {
            return isSameBasePartNumber(mpn1, mpn2);
        }

        // Check for known compatible series
        return isCompatibleSeries(series1, series2, mpn1, mpn2);
    }

    private boolean isSameBasePartNumber(String mpn1, String mpn2) {
        String base1 = extractBasePartNumber(mpn1);
        String base2 = extractBasePartNumber(mpn2);
        return base1.equals(base2);
    }

    private String extractBasePartNumber(String mpn) {
        String upper = mpn.toUpperCase();

        // Remove package suffix after hyphen
        int dashIndex = upper.indexOf('-');
        if (dashIndex > 0) {
            return upper.substring(0, dashIndex);
        }

        // For CM108/CM109/CM119 series, include variant letter
        if (upper.matches("^CM10[89][A-Z]*.*") || upper.matches("^CM119[A-Z]*.*")) {
            // Extract up to variant letter (e.g., CM108AH -> CM108AH)
            StringBuilder base = new StringBuilder();
            for (int i = 0; i < upper.length(); i++) {
                char c = upper.charAt(i);
                if (Character.isDigit(c) || Character.isLetter(c)) {
                    // Stop at package indicators
                    if (i > 4 && upper.substring(i).matches("^(QFP|LQFP|QFN|SSOP|TQFP).*")) {
                        break;
                    }
                    base.append(c);
                } else {
                    break;
                }
            }
            return base.toString();
        }

        // For CM6xxx series
        if (upper.matches("^CM6[0-9]{3}[A-Z]*.*")) {
            // CM6631A -> CM6631A (include variant)
            int i = 6;
            while (i < upper.length() && Character.isLetter(upper.charAt(i))) {
                i++;
            }
            return upper.substring(0, i);
        }

        // For CMI87xx series
        if (upper.matches("^CMI87[0-9]{2}[A-Z]*.*")) {
            int i = 7;
            while (i < upper.length() && Character.isLetter(upper.charAt(i))) {
                i++;
            }
            return upper.substring(0, i);
        }

        return upper;
    }

    private boolean isCompatibleSeries(String series1, String series2, String mpn1, String mpn2) {
        // CM108 and CM108 variants (AH, B, etc.) are same series, handled by isSameBasePartNumber
        // CM119A and CM119B are same series, handled above

        // Different series are generally not compatible
        // CM6206 and CM6631 are different products (basic vs professional audio)
        return false;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct check for IC type - the main type for C-Media audio ICs
        if (type == ComponentType.IC) {
            // CM108/CM109 series - USB audio codec
            if (upperMpn.matches("^CM10[89][A-Z]*.*")) {
                return true;
            }
            // CM119 series - USB 7.1 audio
            if (upperMpn.matches("^CM119[A-Z]*.*")) {
                return true;
            }
            // CM6xxx series - professional USB audio
            if (upperMpn.matches("^CM6[0-9]{3}[A-Z]*.*")) {
                return true;
            }
            // CMI87xx series - HD audio codec
            if (upperMpn.matches("^CMI87[0-9]{2}[A-Z]*.*")) {
                return true;
            }
            // CMI83xx series - legacy PCI audio
            if (upperMpn.matches("^CMI83[0-9]{2}[A-Z]*.*")) {
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
