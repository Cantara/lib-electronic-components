package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler for Torex Semiconductor components.
 * <p>
 * Torex specializes in power management solutions including:
 * - XC62xx: LDOs (XC6206, XC6210, XC6220) - Ultra-low quiescent current LDOs
 * - XC92xx: DC-DC converters (XC9265, XC9142, XC9235) - Step-down/step-up converters
 * - XC61xx: Voltage detectors (XC6119, XC61CN) - Reset ICs and supervisors
 * - XC68xx: Battery chargers (XC6802, XC6808) - Li-ion/Li-polymer chargers
 * - XC81xx: Load switches (XC8107, XC8109) - High-side load switches
 * <p>
 * MPN patterns:
 * - XC[0-9]{4}[A-Z0-9]* - Standard format (e.g., XC6206P332MR)
 * - XC6[0-9]{2}[A-Z]* - Short format (e.g., XC61CN)
 * <p>
 * Package codes:
 * - A: SOT-23
 * - B: SOT-89
 * - C: SOT-25
 * - D: DFN
 * - MR: USP (Ultra Small Package)
 * - N: SOT-89-5
 * - P: SOT-23-5
 * - R: SOT-25
 * - S: SOT-353
 * - T: TSOT-5
 */
public class TorexHandler implements ManufacturerHandler {

    // Package code mappings for Torex components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        PACKAGE_CODES.put("A", "SOT-23");
        PACKAGE_CODES.put("B", "SOT-89");
        PACKAGE_CODES.put("C", "SOT-25");
        PACKAGE_CODES.put("D", "DFN");
        PACKAGE_CODES.put("MR", "USP");
        PACKAGE_CODES.put("N", "SOT-89-5");
        PACKAGE_CODES.put("P", "SOT-23-5");
        PACKAGE_CODES.put("R", "SOT-25");
        PACKAGE_CODES.put("S", "SOT-353");
        PACKAGE_CODES.put("T", "TSOT-5");
        PACKAGE_CODES.put("TR", "SOT-23-TR");
        PACKAGE_CODES.put("NR", "SOT-89-5");
        PACKAGE_CODES.put("DR", "DFN");
    }

    // Series descriptions
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("XC62", "LDO Regulators");
        SERIES_INFO.put("XC91", "DC-DC Converters");
        SERIES_INFO.put("XC92", "DC-DC Converters");
        SERIES_INFO.put("XC61", "Voltage Detectors");
        SERIES_INFO.put("XC68", "Battery Chargers");
        SERIES_INFO.put("XC81", "Load Switches");
        SERIES_INFO.put("XC93", "DC-DC Converters (High Efficiency)");
        SERIES_INFO.put("XC63", "LDO Regulators (High Current)");
        SERIES_INFO.put("XC64", "LDO Regulators (Low Noise)");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.VOLTAGE_REGULATOR
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // XC62xx - LDO Regulators (XC6206, XC6210, XC6220, etc.)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC62[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC62[0-9]{2}[A-Z0-9]*$");

        // XC63xx - High Current LDOs
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC63[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC63[0-9]{2}[A-Z0-9]*$");

        // XC64xx - Low Noise LDOs
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC64[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC64[0-9]{2}[A-Z0-9]*$");

        // XC91xx - DC-DC Converters (XC9142, etc.)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC91[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC91[0-9]{2}[A-Z0-9]*$");

        // XC92xx - DC-DC Converters (step-down/step-up)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC92[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC92[0-9]{2}[A-Z0-9]*$");

        // XC93xx - High Efficiency DC-DC Converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC93[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC93[0-9]{2}[A-Z0-9]*$");

        // XC61xx - Voltage Detectors / Reset ICs (also power management ICs)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC61[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC61[0-9]{2}[A-Z0-9]*$");
        // Short form like XC61CN
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC61[A-Z]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC61[A-Z]{2}[A-Z0-9]*$");

        // XC68xx - Battery Chargers
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC68[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC68[0-9]{2}[A-Z0-9]*$");

        // XC81xx - Load Switches
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^XC81[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^XC81[0-9]{2}[A-Z0-9]*$");

        // General XC pattern for other power management ICs
        registry.addPattern(ComponentType.IC, "^XC[0-9]{4}[A-Z0-9]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Torex MPNs (start with XC)
        if (!upperMpn.startsWith("XC")) {
            return false;
        }

        // Verify it's a valid Torex pattern
        if (!isTorexPart(upperMpn)) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case VOLTAGE_REGULATOR:
                return isVoltageRegulator(upperMpn);
            case IC:
                return isTorexPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isTorexPart(String mpn) {
        // Match XC[0-9]{4} or XC6[0-9]{1}[A-Z]{2} formats
        return mpn.matches("^XC[0-9]{4}[A-Z0-9]*$") ||
               mpn.matches("^XC6[0-9][A-Z]{2}[A-Z0-9]*$");
    }

    private boolean isVoltageRegulator(String mpn) {
        // LDOs: XC62xx, XC63xx, XC64xx
        if (mpn.matches("^XC6[234][0-9]{2}[A-Z0-9]*$")) {
            return true;
        }
        // DC-DC converters: XC91xx, XC92xx, XC93xx
        if (mpn.matches("^XC9[123][0-9]{2}[A-Z0-9]*$")) {
            return true;
        }
        // Voltage detectors: XC61xx, XC61CN etc.
        if (mpn.matches("^XC61[0-9]{2}[A-Z0-9]*$") ||
            mpn.matches("^XC61[A-Z]{2}[A-Z0-9]*$")) {
            return true;
        }
        // Battery chargers: XC68xx
        if (mpn.matches("^XC68[0-9]{2}[A-Z0-9]*$")) {
            return true;
        }
        // Load switches: XC81xx
        if (mpn.matches("^XC81[0-9]{2}[A-Z0-9]*$")) {
            return true;
        }
        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Torex MPN format: XC6206P332MR
        // XC6206 = series, P = package, 332 = voltage code, MR = tape reel suffix
        // The package letter is the single letter immediately after the 4-digit part number
        // MR/TR at the end are tape and reel indicators, not package codes

        // Pattern for standard parts like XC6206P332MR or XC6206A33AMR
        // Extract the single letter after XC + 4 digits
        Pattern standardPattern = Pattern.compile("^XC[0-9]{4}([A-Z]).*$");
        java.util.regex.Matcher matcher = standardPattern.matcher(upperMpn);

        if (matcher.matches()) {
            String code = matcher.group(1);
            return PACKAGE_CODES.getOrDefault(code, code);
        }

        // Pattern for short form like XC61CN
        Pattern shortPattern = Pattern.compile("^XC6[0-9]([A-Z]{2}).*$");
        matcher = shortPattern.matcher(upperMpn);

        if (matcher.matches()) {
            String code = matcher.group(1);
            // The second letter might be the package
            if (code.length() >= 1) {
                String lastLetter = code.substring(code.length() - 1);
                return PACKAGE_CODES.getOrDefault(lastLetter, "");
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Standard format: XC6206, XC9265, etc.
        if (upperMpn.matches("^XC[0-9]{4}.*")) {
            // Return first 4 characters of the number part (e.g., XC62 from XC6206)
            return upperMpn.substring(0, 4);
        }

        // Short format: XC61CN
        if (upperMpn.matches("^XC6[0-9][A-Z]{2}.*")) {
            return upperMpn.substring(0, 4);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be in the same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract the base part number
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2) && !base1.isEmpty();
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match XC[0-9]{4} - the core part number
        Pattern basePattern = Pattern.compile("^(XC[0-9]{4}).*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        // Short format XC61CN -> XC61
        Pattern shortPattern = Pattern.compile("^(XC6[0-9])[A-Z]{2}.*$");
        matcher = shortPattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get a description for the given series.
     *
     * @param series the series code (e.g., "XC62", "XC92")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Extract the voltage code from an MPN.
     * For example, XC6206P332MR returns "332" which represents 3.3V.
     *
     * @param mpn the manufacturer part number
     * @return the voltage code or empty string if not found
     */
    public String extractVoltageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Pattern: XC6206P332MR - voltage code is 3 digits after package letter
        Pattern voltagePattern = Pattern.compile("^XC[0-9]{4}[A-Z]([0-9]{2,3})[A-Z]?.*$");
        java.util.regex.Matcher matcher = voltagePattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    /**
     * Determine if an MPN is an LDO regulator.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an LDO regulator
     */
    public boolean isLDO(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^XC6[234][0-9]{2}.*");
    }

    /**
     * Determine if an MPN is a DC-DC converter.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a DC-DC converter
     */
    public boolean isDCDCConverter(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^XC9[123][0-9]{2}.*");
    }

    /**
     * Determine if an MPN is a voltage detector/reset IC.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a voltage detector
     */
    public boolean isVoltageDetector(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^XC61[0-9]{2}.*") || upper.matches("^XC61[A-Z]{2}.*");
    }

    /**
     * Determine if an MPN is a battery charger.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a battery charger
     */
    public boolean isBatteryCharger(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^XC68[0-9]{2}.*");
    }

    /**
     * Determine if an MPN is a load switch.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a load switch
     */
    public boolean isLoadSwitch(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^XC81[0-9]{2}.*");
    }
}
