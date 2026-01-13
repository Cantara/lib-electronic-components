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
 * Handler for ABLIC (formerly Seiko Instruments) components.
 * <p>
 * ABLIC specializes in low-power analog semiconductors for battery-powered applications:
 * - S-1xxx: LDO voltage regulators (S-1167, S-1206, S-1312)
 * - S-80xxx: Voltage detectors/supervisors (S-80740, S-80945)
 * - S-35xxx: Real-Time Clocks (S-35390A, S-35198)
 * - S-82xx/S-8xxx: Battery management ICs (S-8261, S-8254)
 * - S-24C/S-93C: Serial EEPROM (I2C and Microwire)
 * <p>
 * Pattern examples: S-1167B33A-I6T1U, S-80740CNNB-G6T1U, S-35390A-T8T1G
 * <p>
 * Package code suffixes:
 * - A: SOT-23
 * - B: SOT-89
 * - U: USP (Ultra Small Package)
 * - N: SON
 * - C: Chip scale package
 */
public class ABLICHandler implements ManufacturerHandler {

    // Package code mappings for ABLIC components
    private static final Map<String, String> PACKAGE_CODES = Map.of(
            "A", "SOT-23",
            "B", "SOT-89",
            "U", "USP",
            "N", "SON",
            "C", "CSP",
            "T", "TSSOP",
            "S", "SOP",
            "F", "WLCSP"
    );

    // Series descriptions
    private static final Map<String, String> SERIES_INFO = Map.ofEntries(
            Map.entry("S-1167", "LDO Voltage Regulator"),
            Map.entry("S-1206", "LDO Voltage Regulator"),
            Map.entry("S-1312", "LDO Voltage Regulator"),
            Map.entry("S-1313", "LDO Voltage Regulator"),
            Map.entry("S-1318", "LDO Voltage Regulator"),
            Map.entry("S-80740", "Voltage Detector"),
            Map.entry("S-80945", "Voltage Detector"),
            Map.entry("S-807", "Voltage Detector Series"),
            Map.entry("S-809", "Voltage Detector Series"),
            Map.entry("S-35390", "Real-Time Clock with I2C"),
            Map.entry("S-35198", "Real-Time Clock"),
            Map.entry("S-8261", "Battery Protection IC"),
            Map.entry("S-8254", "Battery Fuel Gauge"),
            Map.entry("S-24C", "I2C EEPROM"),
            Map.entry("S-93C", "Microwire EEPROM")
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.MEMORY,
                ComponentType.MEMORY_EEPROM
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // LDO Voltage Regulators: S-1xxx series
        // Pattern: S-1[0-9]{3}[A-Z0-9-]*
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^S-1[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^S-1[0-9]{3}[A-Z0-9-]*$");

        // Voltage Detectors: S-80xxx, S-807xx, S-809xx
        // Pattern: S-80[0-9]{3}[A-Z0-9-]* or S-8[07]9[0-9]{2}[A-Z0-9-]*
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^S-80[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^S-8[07]9[0-9]{2}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^S-80[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^S-8[07]9[0-9]{2}[A-Z0-9-]*$");

        // Real-Time Clocks: S-35xxx series
        // Pattern: S-35[0-9]{3}[A-Z0-9-]*
        registry.addPattern(ComponentType.IC, "^S-35[0-9]{3}[A-Z0-9-]*$");

        // Battery Management ICs: S-82xx, S-82xxx
        // Pattern: S-82[0-9]{2,3}[A-Z0-9-]*
        registry.addPattern(ComponentType.IC, "^S-82[0-9]{2,3}[A-Z0-9-]*$");

        // I2C EEPROM: S-24Cxx
        // Pattern: S-24C[0-9]+[A-Z0-9-]*
        registry.addPattern(ComponentType.MEMORY, "^S-24C[0-9]+[A-Z0-9-]*$");
        registry.addPattern(ComponentType.MEMORY_EEPROM, "^S-24C[0-9]+[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^S-24C[0-9]+[A-Z0-9-]*$");

        // Microwire EEPROM: S-93Cxx
        // Pattern: S-93C[0-9]+[A-Z0-9-]*
        registry.addPattern(ComponentType.MEMORY, "^S-93C[0-9]+[A-Z0-9-]*$");
        registry.addPattern(ComponentType.MEMORY_EEPROM, "^S-93C[0-9]+[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^S-93C[0-9]+[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle ABLIC MPNs (start with S-)
        if (!upperMpn.startsWith("S-")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case VOLTAGE_REGULATOR:
                return isVoltageRegulator(upperMpn);
            case MEMORY:
            case MEMORY_EEPROM:
                return isMemory(upperMpn);
            case IC:
                return isABLICPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isABLICPart(String mpn) {
        // Match S-1xxx (LDO), S-80xxx (voltage detector), S-35xxx (RTC),
        // S-82xx (battery), S-24Cxx (I2C EEPROM), S-93Cxx (Microwire EEPROM)
        return mpn.matches("^S-1[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^S-80[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^S-8[07]9[0-9]{2}[A-Z0-9-]*$") ||
               mpn.matches("^S-35[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^S-82[0-9]{2,3}[A-Z0-9-]*$") ||
               mpn.matches("^S-24C[0-9]+[A-Z0-9-]*$") ||
               mpn.matches("^S-93C[0-9]+[A-Z0-9-]*$");
    }

    private boolean isVoltageRegulator(String mpn) {
        // LDO regulators: S-1xxx
        // Voltage detectors: S-80xxx, S-807xx, S-809xx
        return mpn.matches("^S-1[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^S-80[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^S-8[07]9[0-9]{2}[A-Z0-9-]*$");
    }

    private boolean isMemory(String mpn) {
        // I2C EEPROM: S-24Cxx
        // Microwire EEPROM: S-93Cxx
        return mpn.matches("^S-24C[0-9]+[A-Z0-9-]*$") ||
               mpn.matches("^S-93C[0-9]+[A-Z0-9-]*$");
    }

    /**
     * Check if the MPN is a Real-Time Clock.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an RTC part
     */
    public boolean isRTC(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.matches("^S-35[0-9]{3}[A-Z0-9-]*$");
    }

    /**
     * Check if the MPN is a Battery Management IC.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a battery management part
     */
    public boolean isBatteryManagement(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.matches("^S-82[0-9]{2,3}[A-Z0-9-]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // ABLIC package codes are typically single letters in the suffix
        // Pattern: S-xxxx[variant][voltage][package]-[suffix]
        // Example: S-1167B33A-I6T1U -> Package is 'A' (SOT-23) in middle, or U at end

        // Try to extract package code from near the end of the MPN
        // Look for common package letters: A, B, U, N, C, T, S, F

        // Check for suffix after hyphen (second hyphen if present)
        int lastHyphen = upperMpn.lastIndexOf('-');
        if (lastHyphen > 2) {
            // Check if there's a package code letter in the base part
            String basePart = upperMpn.substring(0, lastHyphen);

            // For LDOs: S-1167B33A -> extract 'A' before hyphen suffix
            // Package code is typically after voltage code (2-3 digits)
            Pattern packagePattern = Pattern.compile("^S-[0-9]+[A-Z]*[0-9]{2,3}([ABUNCTSF]).*$");
            java.util.regex.Matcher matcher = packagePattern.matcher(basePart);
            if (matcher.matches()) {
                String code = matcher.group(1);
                return PACKAGE_CODES.getOrDefault(code, code);
            }
        }

        // For EEPROM: S-24C02A -> Package is the suffix letter
        if (upperMpn.matches("^S-[0-9]+C[0-9]+[A-Z].*$")) {
            Pattern eepromPattern = Pattern.compile("^S-[0-9]+C[0-9]+([A-Z]).*$");
            java.util.regex.Matcher matcher = eepromPattern.matcher(upperMpn);
            if (matcher.matches()) {
                String code = matcher.group(1);
                return PACKAGE_CODES.getOrDefault(code, code);
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // LDO series: S-1xxx
        if (upperMpn.matches("^S-1[0-9]{3}.*")) {
            return upperMpn.substring(0, 6); // Returns S-1167, S-1206, etc.
        }

        // Voltage detector series: S-80xxx
        if (upperMpn.matches("^S-80[0-9]{3}.*")) {
            return upperMpn.substring(0, 7); // Returns S-80740, S-80945, etc.
        }

        // Voltage detector alternate series: S-807xx, S-809xx
        if (upperMpn.matches("^S-8[07]9[0-9]{2}.*")) {
            return upperMpn.substring(0, 5); // Returns S-807, S-809
        }

        // RTC series: S-35xxx
        if (upperMpn.matches("^S-35[0-9]{3}.*")) {
            return upperMpn.substring(0, 7); // Returns S-35390, S-35198, etc.
        }

        // Battery management: S-82xx or S-82xxx
        if (upperMpn.matches("^S-82[0-9]{2,3}.*")) {
            if (upperMpn.length() >= 7 && Character.isDigit(upperMpn.charAt(6))) {
                return upperMpn.substring(0, 7); // Returns S-8261x, S-8254x
            }
            return upperMpn.substring(0, 6); // Returns S-8261, S-8254
        }

        // I2C EEPROM: S-24Cxx
        if (upperMpn.matches("^S-24C[0-9]+.*")) {
            return "S-24C"; // Returns series prefix
        }

        // Microwire EEPROM: S-93Cxx
        if (upperMpn.matches("^S-93C[0-9]+.*")) {
            return "S-93C"; // Returns series prefix
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

        // Extract the base part number (removing package and suffix options)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        return base1.equals(base2);
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // For most ABLIC parts: S-xxxxx[variant] followed by optional suffixes
        // Remove trailing suffix after last hyphen
        int lastHyphen = upperMpn.lastIndexOf('-');
        if (lastHyphen > 2) {
            // Check if this is the main hyphen (S-xxxx) or a suffix hyphen
            String beforeHyphen = upperMpn.substring(0, lastHyphen);
            if (beforeHyphen.contains("-") && beforeHyphen.indexOf('-') == 1) {
                // This is a suffix hyphen, return base part
                return beforeHyphen;
            }
        }

        // For EEPROM and simple parts, extract up to numeric portion
        Pattern basePattern = Pattern.compile("^(S-[0-9]+C?[0-9]*)[A-Z].*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return upperMpn;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get a description for the given series.
     *
     * @param series the series code (e.g., "S-1167", "S-35390")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine if an MPN is a low-power LDO regulator.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an LDO regulator
     */
    public boolean isLDORegulator(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^S-1[0-9]{3}[A-Z0-9-]*$");
    }

    /**
     * Determine if an MPN is a voltage detector/supervisor.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a voltage detector
     */
    public boolean isVoltageDetector(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^S-80[0-9]{3}[A-Z0-9-]*$") ||
               upper.matches("^S-8[07]9[0-9]{2}[A-Z0-9-]*$");
    }

    /**
     * Determine if an MPN is an EEPROM.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an EEPROM part
     */
    public boolean isEEPROM(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^S-24C[0-9]+[A-Z0-9-]*$") ||
               upper.matches("^S-93C[0-9]+[A-Z0-9-]*$");
    }

    /**
     * Determine if an EEPROM uses I2C interface.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an I2C EEPROM
     */
    public boolean isI2CEEPROM(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^S-24C[0-9]+[A-Z0-9-]*$");
    }

    /**
     * Determine if an EEPROM uses Microwire interface.
     *
     * @param mpn the manufacturer part number
     * @return true if this is a Microwire EEPROM
     */
    public boolean isMicrowireEEPROM(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^S-93C[0-9]+[A-Z0-9-]*$");
    }
}
