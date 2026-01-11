package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Handler for Texas Instruments components.
 * Implements specific pattern matching and component identification for TI parts.
 */
public class TIHandler implements ManufacturerHandler {
    // Package code mapping for different package types
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // DIP packages
        PACKAGE_CODES.put("N", "DIP");
        PACKAGE_CODES.put("P", "DIP");

        // Surface mount packages
        PACKAGE_CODES.put("D", "SOIC");
        PACKAGE_CODES.put("PW", "TSSOP");
        PACKAGE_CODES.put("DGK", "MSOP");
        PACKAGE_CODES.put("DBV", "SOT-23");
        PACKAGE_CODES.put("DRL", "SOT-553");
        PACKAGE_CODES.put("DRV", "SON");

        // Power packages
        PACKAGE_CODES.put("T", "TO-220");
        PACKAGE_CODES.put("T3", "TO-220");
        PACKAGE_CODES.put("K", "TO-3");
        PACKAGE_CODES.put("H", "TO-39");
        PACKAGE_CODES.put("KC", "TO-252");
        PACKAGE_CODES.put("KV", "TO-252");
        PACKAGE_CODES.put("MP", "SOT-223");
        PACKAGE_CODES.put("DT", "SOT-223");

        // LED packages
        PACKAGE_CODES.put("SMD", "SMD");  // Surface Mount LED
        PACKAGE_CODES.put("THT", "THT");  // Through Hole LED
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.IC);
        types.add(ComponentType.ANALOG_IC);
        types.add(ComponentType.OPAMP);
        types.add(ComponentType.OPAMP_TI);
        types.add(ComponentType.OPAMP);
        types.add(ComponentType.OPAMP_TI);
        types.add(ComponentType.VOLTAGE_REGULATOR);
        types.add(ComponentType.VOLTAGE_REGULATOR_LINEAR_TI);
        types.add(ComponentType.VOLTAGE_REGULATOR_SWITCHING_TI);
        types.add(ComponentType.MICROCONTROLLER);
        types.add(ComponentType.MICROCONTROLLER_TI);
        types.add(ComponentType.MCU_TI);
        types.add(ComponentType.MSP430_MCU);
        types.add(ComponentType.C2000_MCU);
        types.add(ComponentType.LED);
        types.add(ComponentType.LED_TI);
        types.add(ComponentType.TEMPERATURE_SENSOR);
        types.add(ComponentType.TEMPERATURE_SENSOR_TI);
        return types;
    }

    /**
     * Class to hold information about a specific component series
     */
    private static class ComponentSeriesInfo {
        final ComponentType primaryType;
        final String pattern;
        final String description;
        final Set<String> equivalentSeries;
        final Set<ComponentType> additionalTypes;
        final String baseSeriesPattern; // Pattern to extract base series

        ComponentSeriesInfo(ComponentType primaryType, String pattern, String description,
                            Set<String> equivalentSeries) {
            this(primaryType, pattern, description, equivalentSeries, Set.of(), null);
        }

        ComponentSeriesInfo(ComponentType primaryType, String pattern, String description,
                            Set<String> equivalentSeries, Set<ComponentType> additionalTypes,
                            String baseSeriesPattern) {
            this.primaryType = primaryType;
            this.pattern = pattern;
            this.description = description;
            this.equivalentSeries = equivalentSeries;
            this.additionalTypes = additionalTypes != null ? additionalTypes : Set.of();
            this.baseSeriesPattern = baseSeriesPattern != null ? baseSeriesPattern : pattern;
        }
    }

    // Specific component series patterns
    private static final Map<String, ComponentSeriesInfo> COMPONENT_SERIES = new HashMap<>();
    static {
        // === VOLTAGE REGULATORS (78xx/79xx) === (must be first)
        COMPONENT_SERIES.put("78xx", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)78[0-9]{2}(?:[A-Z]*(?:CT|T|KC|KV|MP|DT))?$",
                "78xx Fixed Positive Voltage Regulator",
                Set.of("MC78xx", "UA78xx"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.ANALOG_IC, ComponentType.IC),
                "^(?:LM|UA)78"
        ));

        // === VOLTAGE REGULATORS (79xx) ===
        COMPONENT_SERIES.put("79xx", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)79[0-9]{2}(?:[A-Z]*(?:CT|T|KC|KV|MP|DT))?$",
                "79xx Fixed Negative Voltage Regulator",
                Set.of("MC79xx", "UA79xx"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.ANALOG_IC, ComponentType.IC),
                "^(?:LM|UA)79"
        ));
        // Make sure the pattern for op-amps is more specific to avoid matching voltage regulators
        // LM317 family (must be before op-amps)
        COMPONENT_SERIES.put("LM317_ADJ", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^LM317(?:T|K|H|MP|S|AT|AH|AEMP|AIDCT|AMDCYR)?$",  // Super specific pattern with all known packages
                "1.5A Adjustable Positive Voltage Regulator",
                Set.of("MC317", "LM350", "LM338"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.IC),
                "^LM317"
        ));

        // LM350 family
        COMPONENT_SERIES.put("LM350_ADJ", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^LM350(?:T|K|AT|AK|MP|S)?$",  // Super specific pattern with all known packages
                "3A Adjustable Positive Voltage Regulator",
                Set.of("LM317", "LM338"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.IC),
                "^LM350"
        ));

        // LM338 family
        COMPONENT_SERIES.put("LM338_ADJ", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^LM338(?:T|K|S|MP)?$",  // Super specific pattern with all known packages
                "5A Adjustable Positive Voltage Regulator",
                Set.of("LM317", "LM350"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.IC),
                "^LM338"
        ));

        // === FIXED VOLTAGE REGULATORS ===
        // 78xx series with specific voltages
        COMPONENT_SERIES.put("LM7805", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7805(?:CT|T|KC|KV|MP|DT|AC|ACP|AACT)?$",  // All known 7805 packages
                "5V Fixed Positive Voltage Regulator",
                Set.of("MC7805", "UA7805"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.IC),
                "^(?:LM|UA)7805"
        ));

        // 79xx series negative regulators
        COMPONENT_SERIES.put("LM7905", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7905(?:CT|T|KC|KV|MP|DT|AC|ACP|AACT)?$",  // All known 7905 packages
                "-5V Fixed Negative Voltage Regulator",
                Set.of("MC7905", "UA7905"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.IC),
                "^(?:LM|UA)7905"
        ));

        // LM358 series - Dual Op-Amp (single consolidated entry)
        COMPONENT_SERIES.put("LM358", new ComponentSeriesInfo(
                ComponentType.OPAMP_TI,
                "^LM358(?:[AMDP])?(?:N|D|P|DG|PW|DR|DGK|DBV|DRG4)?$",
                "Dual Op-Amp",
                Set.of("MC1458", "LM1458", "RC4558"),
                Set.of(ComponentType.OPAMP, ComponentType.ANALOG_IC, ComponentType.IC),
                "^LM358"
        ));

        // Fixed 12V Positive
        COMPONENT_SERIES.put("LM7812", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7812[A-Z]*(?:CT|T|KC|KV|MP|DT)?$",
                "12V Fixed Positive Voltage Regulator",
                Set.of("MC7812", "UA7812"),
                Set.of(ComponentType.VOLTAGE_REGULATOR, ComponentType.IC),
                "^(?:LM|UA)7812"
        ));

        // Note: LM7905, LM317, LM350, LM337 defined below in consolidated section
        // Note: LM358 already defined above

        COMPONENT_SERIES.put("LM324", new ComponentSeriesInfo(
                ComponentType.OPAMP_TI,
                "^LM324(?:[A-Z0-9]*(?:N|D|P|DG|PW))?$",  // Very specific pattern
                "Quad Op-Amp",
                Set.of("MC3403", "RC4136"),
                Set.of(ComponentType.OPAMP, ComponentType.ANALOG_IC, ComponentType.IC),
                "^LM324(?![0-9])"  // Make sure no digits follow 324
        ));

        // === TEMPERATURE SENSORS === (after op-amps)
        // LM35 sensors have LETTER after 35 (e.g., LM35DZ, LM35CZ), NOT digit (LM358 is op-amp!)
        COMPONENT_SERIES.put("LM35", new ComponentSeriesInfo(
                ComponentType.TEMPERATURE_SENSOR_TI,
                "^LM35[A-D][A-Z0-9-]*$",  // Letter A-D after 35, NOT digit (avoids LM358)
                "Precision Temperature Sensor",
                Set.of(),
                Set.of(ComponentType.TEMPERATURE_SENSOR, ComponentType.SENSOR, ComponentType.IC),
                "^LM35[A-D]"  // Letter after 35
        ));

        // TL072 - consolidated entry (single definition)
        COMPONENT_SERIES.put("TL072", new ComponentSeriesInfo(
                ComponentType.OPAMP_TI,
                "^TL072[A-Z0-9]*(?:N|D|P|DG|PW)?$",
                "Dual JFET Op-Amp",
                Set.of(),
                Set.of(ComponentType.OPAMP, ComponentType.IC),
                "^TL072"
        ));

        // Note: LM7905 already defined above





        // === COMPARATORS ===
        COMPONENT_SERIES.put("LM311", new ComponentSeriesInfo(
                ComponentType.OPAMP_TI,
                "^LM311[A-Z0-9]*(?:N|D|P)?$",
                "Voltage Comparator",
                Set.of(),
                Set.of(ComponentType.OPAMP, ComponentType.IC),  // Add IC as additional type
                "^LM311"
        ));

        // Note: TL072 already defined above

        COMPONENT_SERIES.put("LM7806", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7806[A-Z]*(CT|T|KC|KV|MP|DT)?$",
                "6V Fixed Positive Voltage Regulator",
                Set.of("MC7806", "UA7806", "KA7806"),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^(?:LM|UA)7806"
        ));
        COMPONENT_SERIES.put("LM7808", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7808[A-Z]*(CT|T|KC|KV|MP|DT)?$",
                "8V Fixed Positive Voltage Regulator",
                Set.of("MC7808", "UA7808", "KA7808"),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^(?:LM|UA)7808"
        ));
        COMPONENT_SERIES.put("LM7809", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7809[A-Z]*(CT|T|KC|KV|MP|DT)?$",
                "9V Fixed Positive Voltage Regulator",
                Set.of("MC7809", "UA7809", "KA7809"),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^(?:LM|UA)7809"
        ));
        COMPONENT_SERIES.put("LM7810", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7810[A-Z]*(CT|T|KC|KV|MP|DT)?$",
                "10V Fixed Positive Voltage Regulator",
                Set.of("MC7810", "UA7810", "KA7810"),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^(?:LM|UA)7810"
        ));
        // Note: LM7812 already defined above

        COMPONENT_SERIES.put("LM7815", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7815[A-Z]*(CT|T|KC|KV|MP|DT)?$",
                "15V Fixed Positive Voltage Regulator",
                Set.of("MC7815", "UA7815", "KA7815"),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^(?:LM|UA)7815"
        ));
        COMPONENT_SERIES.put("LM7818", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7818[A-Z]*(CT|T|KC|KV|MP|DT)?$",
                "18V Fixed Positive Voltage Regulator",
                Set.of("MC7818", "UA7818", "KA7818"),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^(?:LM|UA)7818"
        ));
        COMPONENT_SERIES.put("LM7824", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7824[A-Z]*(CT|T|KC|KV|MP|DT)?$",
                "24V Fixed Positive Voltage Regulator",
                Set.of("MC7824", "UA7824", "KA7824"),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^(?:LM|UA)7824"
        ));

        // Note: LM7905 already defined above



        // Note: TL072 already defined above

        // TL074 - consolidated entry
        COMPONENT_SERIES.put("TL074", new ComponentSeriesInfo(
                ComponentType.OPAMP_TI,
                "^TL074[A-Z0-9]*(?:N|D|P|DG|PW)?$",
                "Quad JFET Op-Amp",
                Set.of(),
                Set.of(ComponentType.OPAMP, ComponentType.IC),
                "^TL074"
        ));

        // NE5532 - consolidated entry
        COMPONENT_SERIES.put("NE5532", new ComponentSeriesInfo(
                ComponentType.OPAMP_TI,
                "^NE5532[A-Z0-9]*(?:N|D|P|DG|PW)?$",
                "Dual Low-Noise Op-Amp",
                Set.of(),
                Set.of(ComponentType.OPAMP, ComponentType.IC),
                "^NE5532"
        ));

        // Note: LM35 already defined above

        // === LOW DROPOUT REGULATORS (LDO) ===
        COMPONENT_SERIES.put("TPS7350", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^TPS7350[A-Z0-9]*$",
                "Low Dropout Regulator",
                Set.of(),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^TPS7350"
        ));

        // === REFERENCES ===
        COMPONENT_SERIES.put("TL431", new ComponentSeriesInfo(
                ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^TL431[A-Z0-9]*(?:A|B|C)?(?:LP|P|D)?$",
                "Programmable Voltage Reference",
                Set.of(),
                Set.of(ComponentType.VOLTAGE_REGULATOR),
                "^TL431"
        ));

        // Note: TL072, TL074, LM35 already defined above


        // LEDs - RGB Series
        COMPONENT_SERIES.put("TLHR5400", new ComponentSeriesInfo(
                ComponentType.LED_TI,
                "^TLHR5400[0-9]*$",
                "Red High-Power LED",
                Set.of("TLHR5401", "TLHR5402", "TLHR5403"),
                Set.of(ComponentType.LED),
                "^TLHR5400"
        ));
        COMPONENT_SERIES.put("TLHG5800", new ComponentSeriesInfo(
                ComponentType.LED_TI,
                "^TLHG5800[0-9]*$",
                "Green High-Power LED",
                Set.of("TLHG5801", "TLHG5802", "TLHG5803"),
                Set.of(ComponentType.LED),
                "^TLHG5800"
        ));
        COMPONENT_SERIES.put("TLHB5800", new ComponentSeriesInfo(
                ComponentType.LED_TI,
                "^TLHB5800[0-9]*$",
                "Blue High-Power LED",
                Set.of("TLHB5801", "TLHB5802", "TLHB5803"),
                Set.of(ComponentType.LED),
                "^TLHB5800"
        ));

        // LEDs - White Series
        COMPONENT_SERIES.put("TLW", new ComponentSeriesInfo(
                ComponentType.LED_TI,
                "^TLW[A-Z][0-9]{4}[A-Z0-9-]*$",
                "White LED Series",
                Set.of(),
                Set.of(ComponentType.LED),
                "^TLW[A-Z][0-9]{4}"
        ));


        // Note: LM7808 already defined above

        // Note: LM7812, LM7905, LM7912, LM317, LM350, LM337, LM338 already defined above


        // Note: LM35 already defined above
    }

    // Fallback patterns for broad component types
    private static final Map<ComponentType, String> FALLBACK_PATTERNS = new HashMap<>();
    static {
        // Op-Amps
        FALLBACK_PATTERNS.put(ComponentType.OPAMP_TI, "^(?:LM|TL|NE|SE|SA)[0-9]{3}[A-Z0-9-]*$");
        FALLBACK_PATTERNS.put(ComponentType.OPAMP, "^(?:LM|TL|NE|SE|SA)[0-9]{3}[A-Z0-9-]*$");

        // Voltage Regulators
        FALLBACK_PATTERNS.put(ComponentType.VOLTAGE_REGULATOR_LINEAR_TI,
                "^(?:LM|UA)7[89][0-9]{2}[A-Z0-9-]*$");
        FALLBACK_PATTERNS.put(ComponentType.VOLTAGE_REGULATOR,
                "^(?:LM|UA)7[89][0-9]{2}[A-Z0-9-]*$");

        // Temperature Sensors - LM35 has LETTER after 35 (LM35D, LM35C), not digit (LM358 is op-amp!)
        FALLBACK_PATTERNS.put(ComponentType.TEMPERATURE_SENSOR_TI,
                "^(?:LM35[A-D]|TMP[0-9]+)[A-Z0-9-]*$");
        FALLBACK_PATTERNS.put(ComponentType.TEMPERATURE_SENSOR,
                "^(?:LM35[A-D]|TMP[0-9]+)[A-Z0-9-]*$");
        FALLBACK_PATTERNS.put(ComponentType.SENSOR,
                "^(?:LM35[A-D]|TMP[0-9]+)[A-Z0-9-]*$");

        // Add LED patterns to fallback
        FALLBACK_PATTERNS.put(ComponentType.LED_TI,
                "^(?:TLH[RGB][0-9]{4}|TLW[A-Z][0-9]{4})[A-Z0-9-]*$");
        FALLBACK_PATTERNS.put(ComponentType.LED,
                "^(?:TLH[RGB][0-9]{4}|TLW[A-Z][0-9]{4})[A-Z0-9-]*$");

    }

    // Component family enum for internal use
    private enum TIComponentFamily {
        OPAMP_GENERAL,
        OPAMP_TL_SERIES,
        VOLTAGE_REGULATOR_LINEAR_FIXED,
        VOLTAGE_REGULATOR_LINEAR_ADJUST,
        TEMPERATURE_SENSOR,
        LED_RGB,             // TLHx5xxx series
        LED_WHITE,          // TLWx series
        UNKNOWN
    }
    @Override
    public void initializePatterns(PatternRegistry registry) {
        for (Map.Entry<String, ComponentSeriesInfo> entry : COMPONENT_SERIES.entrySet()) {
            ComponentSeriesInfo info = entry.getValue();
            // Register primary type
            registry.addPattern(info.primaryType, info.pattern);
            // Register base component type
            registry.addPattern(info.primaryType.getBaseType(), info.pattern);
            // Register additional types if any
            for (ComponentType additionalType : info.additionalTypes) {
                registry.addPattern(additionalType, info.pattern);
            }
        }

        // Register fallback patterns
        for (Map.Entry<ComponentType, String> entry : FALLBACK_PATTERNS.entrySet()) {
            registry.addPattern(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct check for voltage regulators first
        if (upperMpn.matches("^(?:LM|UA)7[89][0-9]{2}.*")) {
            if (type == ComponentType.VOLTAGE_REGULATOR_LINEAR_TI ||
                    type == ComponentType.VOLTAGE_REGULATOR ||
                    type == ComponentType.ANALOG_IC ||
                    type == ComponentType.IC) {
                return true;
            }
            return false;  // If it's a voltage regulator, it can't be any other type
        }

        // Check for op-amps
        boolean isOpAmp = upperMpn.matches("^LM358[A-Z0-9]*(?:N|D|P|DG|PW)?$") ||
                upperMpn.matches("^LM324[A-Z0-9]*(?:N|D|P|DG|PW)?$");

        if (isOpAmp) {
            // Check hierarchy: IC -> ANALOG_IC -> OPAMP -> OPAMP_TI
            if (type == ComponentType.IC ||
                    type == ComponentType.ANALOG_IC ||
                    type == ComponentType.OPAMP ||
                    type == ComponentType.OPAMP_TI) {
                return true;
            }
        }
        // Try specific series first
        ComponentSeriesInfo matchingSeries = findMatchingSeries(mpn);
        if (matchingSeries != null) {
            // Check if the requested type matches the series
            if (type == matchingSeries.primaryType ||
                    type == matchingSeries.primaryType.getBaseType() ||
                    matchingSeries.additionalTypes.contains(type)) {
                return true;
            }
        }

        // Try fallback patterns
        String fallbackPattern = FALLBACK_PATTERNS.get(type);
        if (fallbackPattern != null && upperMpn.matches(fallbackPattern)) {
            return true;
        }

        // Finally, try pattern registry
        Pattern pattern = patterns.getPattern(type);
        return pattern != null && pattern.matcher(upperMpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // First check if it's a known series
        ComponentSeriesInfo series = findMatchingSeries(mpn);
        if (series != null) {
            // Extract package code based on series-specific rules
            return extractPackageCodeForSeries(mpn, series);
        }

        // Generic package code extraction
        String upperMpn = mpn.toUpperCase();

        // Extract the suffix after the base part number
        String suffix = extractSuffix(upperMpn);
        if (suffix.isEmpty()) return "";

        // Check for known package codes
        for (Map.Entry<String, String> entry : PACKAGE_CODES.entrySet()) {
            if (suffix.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }

        return suffix;
    }


    private TIComponentFamily determineComponentFamily(String mpn) {
        if (mpn == null) return TIComponentFamily.UNKNOWN;
        String upperMpn = mpn.toUpperCase();

        // LED families (check first)
        if (upperMpn.matches("^TLH[RGB][0-9]{4}.*")) {
            return TIComponentFamily.LED_RGB;
        }
        if (upperMpn.matches("^TLW[A-Z][0-9]{4}.*")) {
            return TIComponentFamily.LED_WHITE;
        }

        // [Rest of family determination logic]
        return TIComponentFamily.UNKNOWN;
    }

    private String extractSuffix(String mpn) {
        // Match the part after the base part number
        Pattern suffixPattern = Pattern.compile("^[A-Z0-9]+(?=[A-Z])([A-Z0-9-]+)$");
        java.util.regex.Matcher matcher = suffixPattern.matcher(mpn);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return matcher.group(1);
        }
        return "";
    }

    private String extractPackageCodeForSeries(String mpn, ComponentSeriesInfo series) {
        String upperMpn = mpn.toUpperCase();

        // Handle voltage regulators
        if (series.primaryType == ComponentType.VOLTAGE_REGULATOR_LINEAR_TI ||
                series.primaryType == ComponentType.VOLTAGE_REGULATOR_SWITCHING_TI) {
            if (upperMpn.endsWith("T")) return "TO-220";
            if (upperMpn.endsWith("CT")) return "TO-220";
            if (upperMpn.endsWith("MP")) return "SOT-223";
            if (upperMpn.endsWith("DT")) return "SOT-223";
            if (upperMpn.endsWith("KC") || upperMpn.endsWith("KV")) return "TO-252";
        }

        // Handle op-amps
        if (series.primaryType == ComponentType.OPAMP_TI) {
            if (upperMpn.endsWith("N")) return "DIP";
            if (upperMpn.endsWith("D")) return "SOIC";
            if (upperMpn.endsWith("PW")) return "TSSOP";
            if (upperMpn.endsWith("DGK")) return "MSOP";
        }

        // Handle temperature sensors
        if (series.primaryType == ComponentType.TEMPERATURE_SENSOR_TI) {
            if (upperMpn.endsWith("T")) return "TO-220";
            if (upperMpn.endsWith("Z")) return "TO-92";
        }

        // LED package codes
        if (mpn.matches("(?i)^TLH[RGB][0-9]{4}.*")) {
            return "SMD";  // Surface mount LED package
        }
        if (mpn.matches("(?i)^TLW[A-Z][0-9]{4}.*")) {
            return "SMD";  // Surface mount LED package
        }
        // Fall back to generic suffix extraction
        return extractSuffix(upperMpn);
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // First check if it's a known series
        ComponentSeriesInfo matchingSeries = findMatchingSeries(mpn);
        if (matchingSeries != null) {
            // Extract series based on the series-specific base pattern
            Pattern seriesPattern = Pattern.compile(matchingSeries.baseSeriesPattern);
            java.util.regex.Matcher matcher = seriesPattern.matcher(mpn.toUpperCase());
            if (matcher.find()) {
                return matcher.group(0);
            }
        }

        // Generic series extraction
        String upperMpn = mpn.toUpperCase();

        // LED series
        if (mpn.matches("(?i)^TLH[RGB][0-9]{4}.*")) {
            return mpn.substring(0, 8);  // Return base part without bin number
        }
        if (mpn.matches("(?i)^TLW[A-Z][0-9]{4}.*")) {
            return mpn.substring(0, 9);  // Return base part without bin number
        }

        // Extract series based on common patterns
        if (upperMpn.startsWith("LM") || upperMpn.startsWith("TL")) {
            return upperMpn.replaceAll("([A-Z0-9]+)(?=[A-Z].*|$)", "$1");
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Check if both MPNs belong to known series
        ComponentSeriesInfo series1 = findMatchingSeries(mpn1);
        ComponentSeriesInfo series2 = findMatchingSeries(mpn2);

        // If both are from known series
        if (series1 != null && series2 != null) {
            // Same series
            if (series1 == series2) {
                return arePackagesCompatible(mpn1, mpn2, series1);
            }

            // Check equivalent series
            String baseMpn1 = getBasePart(mpn1);
            String baseMpn2 = getBasePart(mpn2);
            if (series1.equivalentSeries.contains(baseMpn2) ||
                    series2.equivalentSeries.contains(baseMpn1)) {
                return arePackagesCompatible(mpn1, mpn2, series1);
            }
        }

        // Fall back to generic series comparison
        String series1Str = extractSeries(mpn1);
        String series2Str = extractSeries(mpn2);
        if (!series1Str.isEmpty() && series1Str.equals(series2Str)) {
            return arePackagesCompatible(mpn1, mpn2, null);
        }

        return false;
    }

    private boolean arePackagesCompatible(String mpn1, String mpn2, ComponentSeriesInfo series) {
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Same package is always compatible
        if (pkg1.equals(pkg2)) return true;

        // Handle voltage regulators
        if (series != null && series.primaryType == ComponentType.VOLTAGE_REGULATOR_LINEAR_TI) {
            Set<String> powerPkgs = Set.of("TO-220", "TO-252", "SOT-223");
            return powerPkgs.contains(pkg1) && powerPkgs.contains(pkg2);
        }

        // Handle op-amps
        if (series != null && series.primaryType == ComponentType.OPAMP_TI) {
            // DIP and SOIC are usually interchangeable for op-amps
            return (pkg1.equals("DIP") || pkg1.equals("SOIC")) &&
                    (pkg2.equals("DIP") || pkg2.equals("SOIC"));
        }

        return false;
    }

    private ComponentSeriesInfo findMatchingSeries(String mpn) {
        if (mpn == null) return null;
        String upperMpn = mpn.toUpperCase();

        return COMPONENT_SERIES.values().stream()
                .filter(info -> upperMpn.matches(info.pattern))
                .findFirst()
                .orElse(null);
    }

    private String getBasePart(String mpn) {
        if (mpn == null) return "";
        return mpn.toUpperCase().replaceAll("([A-Z0-9]+)[A-Z].*$", "$1");
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}