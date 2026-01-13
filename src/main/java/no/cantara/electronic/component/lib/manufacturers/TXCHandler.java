package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for TXC Corporation (Taiwan) timing devices: crystals and oscillators.
 *
 * <p>TXC product lines:
 * <ul>
 *   <li><b>7M series</b>: Standard crystals (e.g., 7M-12.000MAAJ-T)</li>
 *   <li><b>8Y series</b>: SMD crystals (e.g., 8Y-12.000MAAE-T)</li>
 *   <li><b>9C series</b>: Clock oscillators (e.g., 9C-12.000MBD-T)</li>
 *   <li><b>7V series</b>: VCXO - Voltage Controlled Crystal Oscillators (e.g., 7V-12.000MBA-T)</li>
 *   <li><b>7X series</b>: TCXO - Temperature Compensated Crystal Oscillators (e.g., 7X-16.000MBA-T)</li>
 *   <li><b>AX series</b>: Automotive grade crystals (e.g., AX-12.000MAAJ-T)</li>
 * </ul>
 *
 * <p>MPN structure: SERIES-FREQ+SUFFIX
 * <ul>
 *   <li>Series: First 2 characters (7M, 8Y, 9C, 7V, 7X, AX)</li>
 *   <li>Frequency: After hyphen, e.g., 12.000M = 12 MHz</li>
 *   <li>Temperature grade: A = Commercial (-20 to +70C), E = Extended (-40 to +85C)</li>
 *   <li>Packaging: -T = Tape and Reel</li>
 * </ul>
 */
public class TXCHandler implements ManufacturerHandler {

    // Crystal series patterns
    private static final String CRYSTAL_7M_PATTERN = "^7M-?[0-9].*";    // Standard crystals
    private static final String CRYSTAL_8Y_PATTERN = "^8Y-?[0-9].*";    // SMD crystals
    private static final String CRYSTAL_AX_PATTERN = "^AX-?[0-9].*";    // Automotive crystals

    // Oscillator series patterns
    private static final String OSCILLATOR_9C_PATTERN = "^9C-?[0-9].*"; // Clock oscillators
    private static final String VCXO_7V_PATTERN = "^7V-?[0-9].*";       // VCXO
    private static final String TCXO_7X_PATTERN = "^7X-?[0-9].*";       // TCXO

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Crystal patterns - register for both base type and manufacturer-specific
        registry.addPattern(ComponentType.CRYSTAL, CRYSTAL_7M_PATTERN);
        registry.addPattern(ComponentType.CRYSTAL, CRYSTAL_8Y_PATTERN);
        registry.addPattern(ComponentType.CRYSTAL, CRYSTAL_AX_PATTERN);

        // Clock oscillator patterns
        registry.addPattern(ComponentType.OSCILLATOR, OSCILLATOR_9C_PATTERN);

        // VCXO patterns
        registry.addPattern(ComponentType.OSCILLATOR, VCXO_7V_PATTERN);

        // TCXO patterns
        registry.addPattern(ComponentType.OSCILLATOR, TCXO_7X_PATTERN);
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.CRYSTAL,
                ComponentType.OSCILLATOR
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Crystal type matching
        if (type == ComponentType.CRYSTAL) {
            return upperMpn.matches(CRYSTAL_7M_PATTERN) ||
                   upperMpn.matches(CRYSTAL_8Y_PATTERN) ||
                   upperMpn.matches(CRYSTAL_AX_PATTERN);
        }

        // Oscillator type matching (includes clock, VCXO, TCXO)
        if (type == ComponentType.OSCILLATOR) {
            return upperMpn.matches(OSCILLATOR_9C_PATTERN) ||
                   upperMpn.matches(VCXO_7V_PATTERN) ||
                   upperMpn.matches(TCXO_7X_PATTERN);
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // TXC package info is encoded in the series prefix and suffix
        // Series prefix indicates approximate package size
        String series = extractSeriesCode(upperMpn);
        if (series.isEmpty()) return "";

        // Must have frequency info to return package details
        String freq = extractFrequency(upperMpn);
        if (freq.isEmpty()) return "";

        // Extract temperature/packaging suffix after frequency
        // Format: SERIES-FREQ[SUFFIX]-T where suffix contains temp grade
        String tempGrade = extractTemperatureGrade(upperMpn);
        String packaging = upperMpn.endsWith("-T") ? "Tape & Reel" : "";

        // Build package description based on series
        String packageSize = switch (series) {
            case "7M" -> "HC49";           // Standard through-hole crystal package
            case "8Y" -> "SMD 3.2x2.5mm";  // SMD crystal
            case "9C" -> "SMD Oscillator"; // Clock oscillator
            case "7V" -> "SMD VCXO";       // VCXO package
            case "7X" -> "SMD TCXO";       // TCXO package
            case "AX" -> "Automotive SMD"; // Automotive grade
            default -> "";
        };

        // Combine package info
        StringBuilder pkg = new StringBuilder();
        if (!packageSize.isEmpty()) {
            pkg.append(packageSize);
        }
        if (!tempGrade.isEmpty()) {
            if (pkg.length() > 0) pkg.append(" ");
            pkg.append(tempGrade);
        }
        if (!packaging.isEmpty()) {
            if (pkg.length() > 0) pkg.append(" ");
            pkg.append(packaging);
        }

        return pkg.toString();
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        String seriesCode = extractSeriesCode(upperMpn);
        return switch (seriesCode) {
            case "7M" -> "7M Standard Crystal";
            case "8Y" -> "8Y SMD Crystal";
            case "9C" -> "9C Clock Oscillator";
            case "7V" -> "7V VCXO";
            case "7X" -> "7X TCXO";
            case "AX" -> "AX Automotive Crystal";
            default -> "";
        };
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Must be same series
        String series1 = extractSeriesCode(upper1);
        String series2 = extractSeriesCode(upper2);
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Must have same frequency
        String freq1 = extractFrequency(upper1);
        String freq2 = extractFrequency(upper2);
        if (freq1.isEmpty() || !freq1.equals(freq2)) {
            return false;
        }

        // Check temperature grade compatibility
        // Extended temp (E) can replace Commercial (A), but not vice versa
        String temp1 = extractTemperatureGrade(upper1);
        String temp2 = extractTemperatureGrade(upper2);

        // Same temp grade is always compatible
        if (temp1.equals(temp2)) {
            return true;
        }

        // Extended can replace Commercial
        if ("Extended".equals(temp1) && "Commercial".equals(temp2)) {
            return true;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // ========== Helper Methods ==========

    /**
     * Extract the 2-character series code from an MPN.
     * @param mpn the manufacturer part number (uppercase)
     * @return the series code (7M, 8Y, 9C, 7V, 7X, AX) or empty string
     */
    private String extractSeriesCode(String mpn) {
        if (mpn == null || mpn.length() < 2) return "";

        String prefix = mpn.substring(0, 2);
        return switch (prefix) {
            case "7M", "8Y", "9C", "7V", "7X", "AX" -> prefix;
            default -> "";
        };
    }

    /**
     * Extract the frequency from an MPN.
     * Format: SERIES-FREQUENCY... (e.g., 7M-12.000MAAJ-T -> 12.000M)
     * @param mpn the manufacturer part number (uppercase)
     * @return the frequency string or empty string
     */
    private String extractFrequency(String mpn) {
        if (mpn == null) return "";

        int dashIndex = mpn.indexOf('-');
        if (dashIndex < 0 || dashIndex >= mpn.length() - 1) {
            return "";
        }

        String afterDash = mpn.substring(dashIndex + 1);

        // Find the end of the frequency (ends with M for MHz or K for kHz)
        int freqEnd = -1;
        for (int i = 0; i < afterDash.length(); i++) {
            char c = afterDash.charAt(i);
            if (c == 'M' || c == 'K') {
                freqEnd = i + 1;
                break;
            }
        }

        if (freqEnd > 0) {
            return afterDash.substring(0, freqEnd);
        }

        return "";
    }

    /**
     * Extract the temperature grade from an MPN.
     * A = Commercial (-20 to +70C), E = Extended (-40 to +85C)
     * @param mpn the manufacturer part number (uppercase)
     * @return "Commercial", "Extended", or empty string
     */
    private String extractTemperatureGrade(String mpn) {
        if (mpn == null) return "";

        // The temperature grade is typically in the suffix after the frequency
        // TXC MPN format: SERIES-FREQ+OPTIONS where temperature is embedded in options
        // e.g., 7M-12.000MAAJ-T -> frequency is 12.000M, options suffix is AAJ
        // The second letter of the options indicates temp: A=Commercial, E=Extended

        // Remove trailing -T if present
        String workMpn = mpn.endsWith("-T") ? mpn.substring(0, mpn.length() - 2) : mpn;

        if (workMpn.length() < 2) return "";

        int dashIndex = workMpn.indexOf('-');
        if (dashIndex < 0) return "";

        String suffix = workMpn.substring(dashIndex + 1);
        // Find where frequency ends (after M or K)
        int freqEnd = -1;
        for (int i = 0; i < suffix.length(); i++) {
            char c = suffix.charAt(i);
            if (c == 'M' || c == 'K') {
                freqEnd = i + 1;
                break;
            }
        }

        if (freqEnd < 0 || freqEnd >= suffix.length()) return "";

        String optionsSuffix = suffix.substring(freqEnd);
        if (optionsSuffix.length() < 2) return "";

        // Temperature grade is the second character of options suffix
        // e.g., AAJ -> A=tolerance, A=temp(Commercial), J=other
        // e.g., AAE -> A=tolerance, A=other, E=temp(Extended)
        // After analyzing TXC datasheets: the 2nd character in options is temp grade
        // MAAJ = M(MHz) + AA(options) + J(package) where second 'A' = commercial temp
        // MAAE = M(MHz) + AAE where 'E' at end = extended temp

        // Look for 'E' anywhere in the options suffix for Extended temp
        // Otherwise assume Commercial if 'A' is present
        if (optionsSuffix.contains("E")) {
            return "Extended";
        }
        if (optionsSuffix.contains("A")) {
            return "Commercial";
        }

        return "";
    }
}
