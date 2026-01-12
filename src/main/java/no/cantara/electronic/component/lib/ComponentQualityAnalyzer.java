package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.lib.ComponentSpecificationDefinitions.SpecDefinition;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for analyzing the quality of electronic component data.
 *
 * Provides methods for calculating quality scores, spec completeness,
 * and validation of component specifications.
 */
public class ComponentQualityAnalyzer {

    // Score weights
    private static final double SPEC_COMPLETENESS_WEIGHT = 0.6;
    private static final double VALID_MPN_WEIGHT = 0.2;
    private static final double DATASHEET_WEIGHT = 0.2;

    // Pattern to strip source prefixes from spec names
    private static final Pattern SOURCE_PREFIX = Pattern.compile("^(OpenAI|Digikey|Mouser|AI|Nexar)_", Pattern.CASE_INSENSITIVE);

    // Spec name mapping table (source variations -> standard name)
    private static final Map<String, String> SPEC_NAME_MAPPINGS = new HashMap<>();

    // Critical specs per component type (must be present for valid data)
    private static final Map<ComponentType, Set<String>> CRITICAL_SPECS = new HashMap<>();

    static {
        initializeSpecMappings();
        initializeCriticalSpecs();
    }

    private static void initializeSpecMappings() {
        // Resistance/Capacitance/Inductance
        SPEC_NAME_MAPPINGS.put("resistance_value", "resistance");
        SPEC_NAME_MAPPINGS.put("capacitance_value", "capacitance");
        SPEC_NAME_MAPPINGS.put("inductance_value", "inductance");

        // Voltage specs
        SPEC_NAME_MAPPINGS.put("vds_max", "vdsMax");
        SPEC_NAME_MAPPINGS.put("vgs_max", "vgsMax");
        SPEC_NAME_MAPPINGS.put("vgs_threshold", "vgsThreshold");
        SPEC_NAME_MAPPINGS.put("voltage_rating", "voltageRating");
        SPEC_NAME_MAPPINGS.put("input_voltage_min", "inputVoltageMin");
        SPEC_NAME_MAPPINGS.put("input_voltage_max", "inputVoltageMax");
        SPEC_NAME_MAPPINGS.put("output_voltage", "outputVoltage");

        // Current specs
        SPEC_NAME_MAPPINGS.put("id_max", "idMax");
        SPEC_NAME_MAPPINGS.put("output_current", "outputCurrent");
        SPEC_NAME_MAPPINGS.put("ripple_current", "rippleCurrent");
        SPEC_NAME_MAPPINGS.put("leakage_current", "leakageCurrent");

        // Resistance specs
        SPEC_NAME_MAPPINGS.put("rds_on", "rdson");
        SPEC_NAME_MAPPINGS.put("rdson_typ", "rdson");
        SPEC_NAME_MAPPINGS.put("esr_value", "esr");

        // Power specs
        SPEC_NAME_MAPPINGS.put("power_dissipation", "pd");
        SPEC_NAME_MAPPINGS.put("power_rating", "powerRating");

        // Gate charge
        SPEC_NAME_MAPPINGS.put("gate_charge", "qg");
        SPEC_NAME_MAPPINGS.put("qg_total", "qg");

        // MCU specs
        SPEC_NAME_MAPPINGS.put("flash_size", "flashSize");
        SPEC_NAME_MAPPINGS.put("ram_size", "ramSize");
        SPEC_NAME_MAPPINGS.put("clock_frequency", "clockFrequency");
        SPEC_NAME_MAPPINGS.put("cpu_frequency", "clockFrequency");
        SPEC_NAME_MAPPINGS.put("gpio_count", "gpioCount");
        SPEC_NAME_MAPPINGS.put("adc_channels", "adcChannels");
        SPEC_NAME_MAPPINGS.put("adc_resolution", "adcResolution");

        // Op-amp specs
        SPEC_NAME_MAPPINGS.put("gain_bandwidth", "gainBandwidth");
        SPEC_NAME_MAPPINGS.put("gbw", "gainBandwidth");
        SPEC_NAME_MAPPINGS.put("slew_rate", "slewRate");
        SPEC_NAME_MAPPINGS.put("input_offset_voltage", "inputOffsetVoltage");
        SPEC_NAME_MAPPINGS.put("offset_voltage", "inputOffsetVoltage");

        // Regulator specs
        SPEC_NAME_MAPPINGS.put("dropout_voltage", "dropoutVoltage");
        SPEC_NAME_MAPPINGS.put("line_regulation", "lineRegulation");
        SPEC_NAME_MAPPINGS.put("load_regulation", "loadRegulation");

        // Thermal
        SPEC_NAME_MAPPINGS.put("rth_jc", "rthJC");
        SPEC_NAME_MAPPINGS.put("thermal_resistance", "rthJC");

        // LED specs
        SPEC_NAME_MAPPINGS.put("forward_voltage", "forwardVoltage");
        SPEC_NAME_MAPPINGS.put("forward_current", "forwardCurrent");
        SPEC_NAME_MAPPINGS.put("luminous_intensity", "luminousIntensity");
        SPEC_NAME_MAPPINGS.put("viewing_angle", "viewingAngle");
        SPEC_NAME_MAPPINGS.put("wavelength_dominant", "wavelengthDominant");

        // Connector specs
        SPEC_NAME_MAPPINGS.put("pin_count", "pinCount");
        SPEC_NAME_MAPPINGS.put("current_rating", "currentRating");
        SPEC_NAME_MAPPINGS.put("contact_resistance", "contactResistance");
        SPEC_NAME_MAPPINGS.put("mating_cycles", "matingCycles");
    }

    private static void initializeCriticalSpecs() {
        CRITICAL_SPECS.put(ComponentType.RESISTOR, Set.of("resistance"));
        CRITICAL_SPECS.put(ComponentType.CAPACITOR, Set.of("capacitance", "voltageRating"));
        CRITICAL_SPECS.put(ComponentType.INDUCTOR, Set.of("inductance"));
        CRITICAL_SPECS.put(ComponentType.MOSFET, Set.of("vdsMax", "idMax"));
        CRITICAL_SPECS.put(ComponentType.MICROCONTROLLER, Set.of("flashSize", "clockFrequency"));
        CRITICAL_SPECS.put(ComponentType.OPAMP, Set.of("gainBandwidth"));
        CRITICAL_SPECS.put(ComponentType.VOLTAGE_REGULATOR, Set.of("outputVoltage"));
        CRITICAL_SPECS.put(ComponentType.LED, Set.of("forwardVoltage", "forwardCurrent"));
        CRITICAL_SPECS.put(ComponentType.CONNECTOR, Set.of("pinCount"));
        CRITICAL_SPECS.put(ComponentType.DIODE, Set.of("forwardVoltage"));
    }

    /**
     * Calculate the quality of component data.
     *
     * @param mpn The manufacturer part number
     * @param specs Map of specification names to values
     * @return QualityResult with score, tier, and details
     */
    public static QualityResult calculateQuality(String mpn, Map<String, ?> specs) {
        return calculateQuality(mpn, specs, false);
    }

    /**
     * Calculate the quality of component data.
     *
     * @param mpn The manufacturer part number
     * @param specs Map of specification names to values
     * @param hasDatasheet Whether a datasheet URL is available
     * @return QualityResult with score, tier, and details
     */
    public static QualityResult calculateQuality(String mpn, Map<String, ?> specs, boolean hasDatasheet) {
        if (mpn == null || mpn.isBlank()) {
            return QualityResult.unknown(mpn);
        }

        // Detect component type
        ComponentType type = ComponentTypeDetector.determineComponentType(mpn);
        if (type == null) {
            return QualityResult.unknown(mpn);
        }

        // Get base type for spec lookup
        ComponentType baseType = type.getBaseType();

        // Get expected specs for this type
        List<SpecDefinition> expectedSpecDefs = ComponentSpecificationDefinitions.getKeySpecs(baseType);
        if (expectedSpecDefs.isEmpty()) {
            // No spec definitions for this type - can't assess quality
            return new QualityResult(
                    0.5, // Middle score for unknown types
                    QualityTier.MEDIUM,
                    0.0,
                    0,
                    0,
                    List.of(),
                    List.of(),
                    isValidMpn(mpn),
                    type.name()
            );
        }

        Set<String> expectedSpecNames = expectedSpecDefs.stream()
                .map(SpecDefinition::name)
                .collect(Collectors.toSet());

        // Normalize provided specs
        Map<String, Object> normalizedSpecs = normalizeSpecNames(specs);

        // Calculate completeness
        Set<String> presentSpecs = new HashSet<>();
        for (String expected : expectedSpecNames) {
            if (hasSpec(normalizedSpecs, expected)) {
                presentSpecs.add(expected);
            }
        }

        List<String> missingSpecs = expectedSpecNames.stream()
                .filter(spec -> !presentSpecs.contains(spec))
                .sorted()
                .collect(Collectors.toList());

        // Check critical specs
        Set<String> criticalForType = CRITICAL_SPECS.getOrDefault(baseType, Set.of());
        List<String> missingCritical = criticalForType.stream()
                .filter(spec -> !hasSpec(normalizedSpecs, spec))
                .sorted()
                .collect(Collectors.toList());

        boolean hasCriticalSpecs = missingCritical.isEmpty();

        // Calculate completeness score
        double specCompleteness = expectedSpecNames.isEmpty() ? 0.0 :
                (double) presentSpecs.size() / expectedSpecNames.size();

        // Validate MPN
        boolean validMpn = isValidMpn(mpn);

        // Calculate overall score
        double overallScore = (specCompleteness * SPEC_COMPLETENESS_WEIGHT)
                + (validMpn ? VALID_MPN_WEIGHT : 0.0)
                + (hasDatasheet ? DATASHEET_WEIGHT : 0.0);

        // Determine tier
        QualityTier tier = QualityTier.fromScore(overallScore, hasDatasheet, validMpn, hasCriticalSpecs);

        return new QualityResult(
                overallScore,
                tier,
                specCompleteness,
                presentSpecs.size(),
                expectedSpecNames.size(),
                missingSpecs,
                missingCritical,
                validMpn,
                type.name()
        );
    }

    /**
     * Get the list of missing specs for a component.
     *
     * @param mpn The manufacturer part number
     * @param specs Map of specification names to values
     * @return List of missing spec names
     */
    public static List<String> getMissingSpecs(String mpn, Map<String, ?> specs) {
        QualityResult result = calculateQuality(mpn, specs);
        return result.missingSpecs();
    }

    /**
     * Assign a quality tier to a component.
     *
     * @param mpn The manufacturer part number
     * @param specs Map of specification names to values
     * @param hasDatasheet Whether a datasheet URL is available
     * @return The quality tier
     */
    public static QualityTier assignQualityTier(String mpn, Map<String, ?> specs, boolean hasDatasheet) {
        return calculateQuality(mpn, specs, hasDatasheet).tier();
    }

    /**
     * Validate component specs against expected definitions.
     *
     * @param mpn The manufacturer part number
     * @param specs Map of specification names to values
     * @return ValidationResult with errors and warnings
     */
    public static ValidationResult validateSpecs(String mpn, Map<String, ?> specs) {
        Map<String, String> errors = new LinkedHashMap<>();
        Map<String, String> warnings = new LinkedHashMap<>();

        if (mpn == null || mpn.isBlank()) {
            errors.put("mpn", "MPN is required");
            return ValidationResult.withErrorsAndWarnings(errors, warnings);
        }

        ComponentType type = ComponentTypeDetector.determineComponentType(mpn);
        if (type == null) {
            warnings.put("mpn", "Unknown component type for MPN: " + mpn);
            return ValidationResult.withErrorsAndWarnings(errors, warnings);
        }

        ComponentType baseType = type.getBaseType();

        // Check critical specs
        Set<String> criticalForType = CRITICAL_SPECS.getOrDefault(baseType, Set.of());
        Map<String, Object> normalizedSpecs = normalizeSpecNames(specs);

        for (String critical : criticalForType) {
            if (!hasSpec(normalizedSpecs, critical)) {
                errors.put(critical, "Critical spec '" + critical + "' is missing for " + baseType);
            }
        }

        // Check for empty/null values in provided specs
        for (Map.Entry<String, Object> entry : normalizedSpecs.entrySet()) {
            if (entry.getValue() == null) {
                warnings.put(entry.getKey(), "Spec '" + entry.getKey() + "' has null value");
            } else if (entry.getValue().toString().isBlank()) {
                warnings.put(entry.getKey(), "Spec '" + entry.getKey() + "' has empty value");
            }
        }

        // Validate MPN
        if (!isValidMpn(mpn)) {
            warnings.put("mpn", "MPN does not match known manufacturer patterns");
        }

        return ValidationResult.withErrorsAndWarnings(errors, warnings);
    }

    /**
     * Get spec completeness breakdown by category.
     *
     * @param mpn The manufacturer part number
     * @param specs Map of specification names to values
     * @return Map of category to completeness percentage
     */
    public static Map<String, Double> getCompletenessBreakdown(String mpn, Map<String, ?> specs) {
        Map<String, Double> breakdown = new LinkedHashMap<>();

        QualityResult result = calculateQuality(mpn, specs);

        breakdown.put("overall", result.specCompleteness());
        breakdown.put("criticalSpecs", result.hasMissingCriticalSpecs() ? 0.0 : 1.0);

        return breakdown;
    }

    /**
     * Check if an MPN matches known manufacturer patterns.
     */
    public static boolean isValidMpn(String mpn) {
        if (mpn == null || mpn.isBlank()) {
            return false;
        }

        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
        return manufacturer != ComponentManufacturer.UNKNOWN;
    }

    /**
     * Get the expected specs for a component type.
     */
    public static List<String> getExpectedSpecs(ComponentType type) {
        if (type == null) {
            return List.of();
        }
        ComponentType baseType = type.getBaseType();
        return ComponentSpecificationDefinitions.getKeySpecs(baseType).stream()
                .map(SpecDefinition::name)
                .collect(Collectors.toList());
    }

    /**
     * Get the critical specs for a component type.
     */
    public static Set<String> getCriticalSpecs(ComponentType type) {
        if (type == null) {
            return Set.of();
        }
        return CRITICAL_SPECS.getOrDefault(type.getBaseType(), Set.of());
    }

    // --- Private helper methods ---

    private static Map<String, Object> normalizeSpecNames(Map<String, ?> specs) {
        if (specs == null || specs.isEmpty()) {
            return Map.of();
        }

        Map<String, Object> normalized = new HashMap<>();

        for (Map.Entry<String, ?> entry : specs.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Normalize the key
            String normalizedKey = normalizeSpecName(key);
            normalized.put(normalizedKey, value);
        }

        return normalized;
    }

    private static String normalizeSpecName(String specName) {
        if (specName == null || specName.isBlank()) {
            return "";
        }

        // Strip source prefix (OpenAI_, Digikey_, etc.)
        String name = SOURCE_PREFIX.matcher(specName).replaceFirst("");

        // Convert to lowercase for lookup
        String lowerName = name.toLowerCase();

        // Check mapping table first
        String mapped = SPEC_NAME_MAPPINGS.get(lowerName);
        if (mapped != null) {
            return mapped;
        }

        // Fall back to camelCase conversion
        return toCamelCase(lowerName);
    }

    private static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (c == '_' || c == '-' || c == ' ') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    private static boolean hasSpec(Map<String, Object> specs, String specName) {
        if (specs == null || specs.isEmpty()) {
            return false;
        }

        // Direct match
        if (specs.containsKey(specName)) {
            Object value = specs.get(specName);
            return value != null && !value.toString().isBlank();
        }

        // Try lowercase match
        String lowerSpec = specName.toLowerCase();
        for (Map.Entry<String, Object> entry : specs.entrySet()) {
            if (entry.getKey().toLowerCase().equals(lowerSpec)) {
                Object value = entry.getValue();
                return value != null && !value.toString().isBlank();
            }
        }

        return false;
    }
}
