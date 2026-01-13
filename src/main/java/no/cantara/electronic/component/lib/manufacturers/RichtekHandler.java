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
 * Handler for Richtek Technology components.
 * <p>
 * Richtek specializes in power management ICs including:
 * - RT8xxx: DC-DC converters (RT8059, RT8070, RT8485 LED drivers)
 * - RT9xxx: LDOs and linear regulators (RT9013, RT9193, RT9058)
 * - RT6xxx: High-efficiency DC-DC converters (RT6150, RT6160)
 * - RT4xxx: LED drivers (RT4831, RT4801)
 * - RT5xxx: DC-DC converters (RT5785, RT5749)
 * - RTQ series: Automotive grade versions (RTQ2106, RTQ6360)
 * <p>
 * Pattern examples: RT8059GQW, RT9013-33GB, RTQ2106GQW
 * Package codes: GQW (QFN), GSP (WLCSP), PQW (QFN), GB (SOT-23-5)
 */
public class RichtekHandler implements ManufacturerHandler {

    // Package code mappings for Richtek components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // QFN packages
        PACKAGE_CODES.put("GQW", "QFN");
        PACKAGE_CODES.put("PQW", "QFN");
        PACKAGE_CODES.put("GQ", "QFN");
        PACKAGE_CODES.put("GW", "QFN");

        // WLCSP packages
        PACKAGE_CODES.put("GSP", "WLCSP");
        PACKAGE_CODES.put("GS", "WLCSP");

        // SOT-23 packages
        PACKAGE_CODES.put("GB", "SOT-23-5");
        PACKAGE_CODES.put("GE", "SOT-23-6");
        PACKAGE_CODES.put("GT", "SOT-23-3");

        // DFN packages
        PACKAGE_CODES.put("GD", "DFN");
        PACKAGE_CODES.put("GF", "DFN");

        // SOP/SOIC packages
        PACKAGE_CODES.put("SP", "SOP-8");
        PACKAGE_CODES.put("SA", "SOIC-8");
        PACKAGE_CODES.put("SE", "SOIC-8");

        // TSSOP packages
        PACKAGE_CODES.put("TP", "TSSOP");
        PACKAGE_CODES.put("TS", "TSSOP");

        // MSOP packages
        PACKAGE_CODES.put("MS", "MSOP");
        PACKAGE_CODES.put("MF", "MSOP-8");

        // BGA packages
        PACKAGE_CODES.put("BGA", "BGA");
    }

    // Series descriptions for extractSeries documentation
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("RT4", "LED Drivers/Backlight Controllers");
        SERIES_INFO.put("RT5", "DC-DC Converters");
        SERIES_INFO.put("RT6", "High-Efficiency DC-DC Converters");
        SERIES_INFO.put("RT8", "DC-DC Converters/LED Drivers");
        SERIES_INFO.put("RT9", "LDO/Linear Regulators");
        SERIES_INFO.put("RTQ", "Automotive Grade");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.LED_DRIVER
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // RT4xxx - LED drivers / backlight controllers
        registry.addPattern(ComponentType.LED_DRIVER, "^RT4[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^RT4[0-9]{3}[A-Z0-9-]*$");

        // RT5xxx - DC-DC converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^RT5[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^RT5[0-9]{3}[A-Z0-9-]*$");

        // RT6xxx - High-efficiency DC-DC converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^RT6[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^RT6[0-9]{3}[A-Z0-9-]*$");

        // RT8xxx - DC-DC converters and LED drivers (mixed series)
        // RT84xx and RT85xx are typically LED drivers
        registry.addPattern(ComponentType.LED_DRIVER, "^RT8[45][0-9]{2}[A-Z0-9-]*$");
        // Other RT8xxx are typically DC-DC converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^RT8[0-367-9][0-9]{2}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^RT8[0-9]{3}[A-Z0-9-]*$");

        // RT9xxx - LDO and linear regulators
        // RT9455, RT9466 are battery chargers (still voltage regulators)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^RT9[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^RT9[0-9]{3}[A-Z0-9-]*$");

        // RTQ series - Automotive grade versions
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^RTQ[0-9]{4}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^RTQ[0-9]{4}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.LED_DRIVER, "^RTQ4[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.LED_DRIVER, "^RTQ8[45][0-9]{2}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Richtek MPNs (start with RT)
        if (!upperMpn.startsWith("RT")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case VOLTAGE_REGULATOR:
                return isVoltageRegulator(upperMpn);
            case LED_DRIVER:
                return isLEDDriver(upperMpn);
            case IC:
                return isRichtekPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isRichtekPart(String mpn) {
        // Match RT[4-9]xxx, RTQxxxx
        return mpn.matches("^RT[4-9][0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^RTQ[0-9]{4}[A-Z0-9-]*$");
    }

    private boolean isVoltageRegulator(String mpn) {
        // RT5xxx, RT6xxx are voltage regulators
        if (mpn.matches("^RT[56][0-9]{3}[A-Z0-9-]*$")) {
            return true;
        }

        // RT8xxx except RT84xx and RT85xx (which are LED drivers)
        if (mpn.matches("^RT8[0-367-9][0-9]{2}[A-Z0-9-]*$")) {
            return true;
        }

        // RT9xxx - LDOs and linear regulators
        if (mpn.matches("^RT9[0-9]{3}[A-Z0-9-]*$")) {
            return true;
        }

        // RTQ automotive but not LED drivers (RTQ4xxx and RTQ84xx/RTQ85xx)
        if (mpn.matches("^RTQ[0-9]{4}[A-Z0-9-]*$")) {
            return !mpn.matches("^RTQ4[0-9]{3}.*") &&
                   !mpn.matches("^RTQ8[45][0-9]{2}.*");
        }

        return false;
    }

    private boolean isLEDDriver(String mpn) {
        // RT4xxx are LED drivers/backlight controllers
        if (mpn.matches("^RT4[0-9]{3}[A-Z0-9-]*$")) {
            return true;
        }

        // RT84xx and RT85xx are LED drivers
        if (mpn.matches("^RT8[45][0-9]{2}[A-Z0-9-]*$")) {
            return true;
        }

        // RTQ4xxx and RTQ84xx/RTQ85xx automotive LED drivers
        if (mpn.matches("^RTQ4[0-9]{3}[A-Z0-9-]*$") ||
            mpn.matches("^RTQ8[45][0-9]{2}[A-Z0-9-]*$")) {
            return true;
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove common suffixes: -TR, -E, etc.
        String baseMpn = upperMpn.replaceAll("-(TR|E|RL|REEL)+$", "");

        // Handle pattern like RT9013-33GB (with voltage in middle)
        // Pattern: RT[0-9]{4}-[0-9]{2}[package]
        Pattern voltagePattern = Pattern.compile("^RT[0-9]{4}-[0-9]{2}([A-Z]{2,3})$");
        java.util.regex.Matcher voltageMatcher = voltagePattern.matcher(baseMpn);
        if (voltageMatcher.matches()) {
            String code = voltageMatcher.group(1);
            return PACKAGE_CODES.getOrDefault(code, code);
        }

        // Extract package code - typically 2-3 letters after the part number
        // Pattern: RT[Q]?[0-9]{3,4}[package-letters]...
        Pattern packagePattern = Pattern.compile("^RTQ?[0-9]{3,4}([A-Z]{2,3}).*$");
        java.util.regex.Matcher matcher = packagePattern.matcher(baseMpn);

        if (matcher.matches()) {
            String code = matcher.group(1);
            return PACKAGE_CODES.getOrDefault(code, code);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // RTQ series: RTQxxxx
        if (upperMpn.startsWith("RTQ") && upperMpn.length() >= 7) {
            return "RTQ";
        }

        // Standard RT[4-9]xxx series
        if (upperMpn.matches("^RT[4-9][0-9]{3}.*")) {
            return upperMpn.substring(0, 3);  // Returns RT4, RT5, RT6, RT8, RT9
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Check for automotive equivalents first (RTQ vs RT)
        if (areEquivalentParts(mpn1, mpn2)) {
            return true;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be in the same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract the base part number (RTxxxx portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        if (base1.equals(base2)) {
            return true;
        }

        return false;
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match RT[Q]?[0-9]{3,4} - the core part number
        Pattern basePattern = Pattern.compile("^(RTQ?[0-9]{4}).*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    private boolean areEquivalentParts(String mpn1, String mpn2) {
        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Check if one is automotive (RTQ) version of the other (RT)
        // e.g., RT6150 <-> RTQ6150
        if (upper1.startsWith("RTQ") && upper2.startsWith("RT") && !upper2.startsWith("RTQ")) {
            String num1 = upper1.substring(3, 7);  // Get 4-digit number from RTQxxxx
            if (upper2.length() >= 6) {
                String num2 = upper2.substring(2, 6);  // Get 4-digit number from RTxxxx
                return num1.equals(num2);
            }
        }

        if (upper2.startsWith("RTQ") && upper1.startsWith("RT") && !upper1.startsWith("RTQ")) {
            String num2 = upper2.substring(3, 7);
            if (upper1.length() >= 6) {
                String num1 = upper1.substring(2, 6);
                return num1.equals(num2);
            }
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get a description for the given series.
     *
     * @param series the series code (e.g., "RT8", "RTQ")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine if an MPN is an automotive-grade part.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an automotive (AEC-Q100) qualified part
     */
    public boolean isAutomotiveGrade(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.startsWith("RTQ") || upper.contains("-AEC");
    }

    /**
     * Determine if an MPN is a battery charger IC.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a battery charger IC
     */
    public boolean isBatteryCharger(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        // RT9455, RT9466, RT9467 are battery chargers
        return upper.matches("^RT945[0-9].*") ||
               upper.matches("^RT946[0-9].*") ||
               upper.matches("^RTQ945[0-9].*") ||
               upper.matches("^RTQ946[0-9].*");
    }
}
