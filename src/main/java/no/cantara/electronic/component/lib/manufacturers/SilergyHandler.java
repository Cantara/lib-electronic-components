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
 * Handler for Silergy Corp components.
 * <p>
 * Silergy specializes in power management solutions including:
 * - SY808x/809x: DC-DC buck converters (SY8088, SY8089, SY8113)
 * - SY720x: DC-DC boost converters (SY7208, SY7201)
 * - SY800x/628x: LDOs (SY8009, SY6288)
 * - SY698x: Battery chargers (SY6981, SY6982)
 * - SY7200: LED drivers
 * - SYXxxx: Extended product line
 * <p>
 * Pattern examples: SY8088AAC, SY7208QNC, SY6288DFN, SYX196
 */
public class SilergyHandler implements ManufacturerHandler {

    // Package code mappings for Silergy components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Common Silergy package suffixes
        PACKAGE_CODES.put("AAC", "QFN");
        PACKAGE_CODES.put("QNC", "QFN");
        PACKAGE_CODES.put("DFN", "DFN");
        PACKAGE_CODES.put("SOT", "SOT-23");
        PACKAGE_CODES.put("TSOT", "TSOT-23");
        PACKAGE_CODES.put("QFN", "QFN");
        PACKAGE_CODES.put("WLCSP", "WLCSP");
        PACKAGE_CODES.put("CSP", "CSP");
        PACKAGE_CODES.put("SOIC", "SOIC-8");
        PACKAGE_CODES.put("SOP", "SOP-8");
    }

    // Series descriptions for extractSeries documentation
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("SY808", "DC-DC Buck Converters (Low Voltage)");
        SERIES_INFO.put("SY811", "DC-DC Buck Converters");
        SERIES_INFO.put("SY720", "DC-DC Boost Converters");
        SERIES_INFO.put("SY800", "LDO Regulators");
        SERIES_INFO.put("SY628", "LDO Regulators (High Current)");
        SERIES_INFO.put("SY698", "Battery Chargers");
        SERIES_INFO.put("SY720", "LED Drivers");
        SERIES_INFO.put("SYX", "Extended Product Line");
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
        // SY808x - DC-DC buck converters (low voltage input)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY808[0-9][A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY808[0-9][A-Z0-9]*$");

        // SY809x - DC-DC buck converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY809[0-9][A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY809[0-9][A-Z0-9]*$");

        // SY811x - DC-DC buck converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY811[0-9][A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY811[0-9][A-Z0-9]*$");

        // SY720x - DC-DC boost converters and LED drivers
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY720[1-9][A-Z0-9]*$");
        registry.addPattern(ComponentType.LED_DRIVER, "^SY7200[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY720[0-9][A-Z0-9]*$");

        // SY800x - LDO regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY800[0-9][A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY800[0-9][A-Z0-9]*$");

        // SY628x - LDO regulators (high current)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY628[0-9][A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY628[0-9][A-Z0-9]*$");

        // SY698x - Battery chargers
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY698[0-9][A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY698[0-9][A-Z0-9]*$");

        // SYXxxx - Extended product line
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SYX[0-9]{3}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SYX[0-9]{3}[A-Z0-9]*$");

        // Generic SY[0-9]{4} pattern for other Silergy parts
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SY[0-9]{4}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^SY[0-9]{4}[A-Z0-9]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle Silergy MPNs (start with SY)
        if (!upperMpn.startsWith("SY")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case VOLTAGE_REGULATOR:
                return isVoltageRegulator(upperMpn);
            case LED_DRIVER:
                return isLEDDriver(upperMpn);
            case IC:
                return isSilergyPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isSilergyPart(String mpn) {
        // Match SY[0-9]{4} or SYX[0-9]{3}
        return mpn.matches("^SY[0-9]{4}[A-Z0-9]*$") ||
               mpn.matches("^SYX[0-9]{3}[A-Z0-9]*$");
    }

    private boolean isVoltageRegulator(String mpn) {
        // Buck converters: SY808x, SY809x, SY811x
        if (mpn.matches("^SY808[0-9][A-Z0-9]*$") ||
            mpn.matches("^SY809[0-9][A-Z0-9]*$") ||
            mpn.matches("^SY811[0-9][A-Z0-9]*$")) {
            return true;
        }
        // Boost converters: SY720[1-9] (SY7200 is LED driver)
        if (mpn.matches("^SY720[1-9][A-Z0-9]*$")) {
            return true;
        }
        // LDOs: SY800x, SY628x
        if (mpn.matches("^SY800[0-9][A-Z0-9]*$") ||
            mpn.matches("^SY628[0-9][A-Z0-9]*$")) {
            return true;
        }
        // Battery chargers: SY698x
        if (mpn.matches("^SY698[0-9][A-Z0-9]*$")) {
            return true;
        }
        // Extended line: SYXxxx
        if (mpn.matches("^SYX[0-9]{3}[A-Z0-9]*$")) {
            return true;
        }
        // Generic SY[0-9]{4} parts (excluding LED drivers)
        if (mpn.matches("^SY[0-9]{4}[A-Z0-9]*$") && !isLEDDriver(mpn)) {
            return true;
        }
        return false;
    }

    private boolean isLEDDriver(String mpn) {
        // SY7200 is an LED driver
        return mpn.matches("^SY7200[A-Z0-9]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract package code - typically letters after the part number
        // Pattern: SY[0-9]{4}[PACKAGE] or SYX[0-9]{3}[PACKAGE]
        Pattern packagePattern = Pattern.compile("^SY[X]?[0-9]{3,4}([A-Z]{2,5}).*$");
        java.util.regex.Matcher matcher = packagePattern.matcher(upperMpn);

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

        // SYXxxx extended series
        if (upperMpn.matches("^SYX[0-9]{3}.*")) {
            return "SYX";
        }

        // Standard SY[0-9]{4} series - extract first 5 chars (SYxxxx prefix)
        if (upperMpn.matches("^SY[0-9]{4}.*")) {
            // Return SY + first 3 digits (e.g., SY808, SY720)
            return upperMpn.substring(0, 5);
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

        // Extract the base part number (SYxxxx or SYXxxx portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2);
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match SY[0-9]{4} or SYX[0-9]{3} - the core part number
        Pattern basePattern = Pattern.compile("^(SY[X]?[0-9]{3,4}).*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);

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
     * @param series the series code (e.g., "SY808", "SY720")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine the product category for an MPN.
     *
     * @param mpn the manufacturer part number
     * @return product category description
     */
    public String getProductCategory(String mpn) {
        if (mpn == null) return "";
        String upper = mpn.toUpperCase();

        if (upper.matches("^SY808[0-9].*") || upper.matches("^SY809[0-9].*") || upper.matches("^SY811[0-9].*")) {
            return "DC-DC Buck Converter";
        }
        if (upper.matches("^SY720[1-9].*")) {
            return "DC-DC Boost Converter";
        }
        if (upper.matches("^SY7200.*")) {
            return "LED Driver";
        }
        if (upper.matches("^SY800[0-9].*") || upper.matches("^SY628[0-9].*")) {
            return "LDO Regulator";
        }
        if (upper.matches("^SY698[0-9].*")) {
            return "Battery Charger";
        }
        if (upper.matches("^SYX[0-9]{3}.*")) {
            return "Extended Product";
        }
        return "";
    }
}
