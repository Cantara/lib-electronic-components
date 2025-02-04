package no.cantara.electronic.component.lib.specs.base;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.cantara.electronic.component.lib.specs.base.ComponentSpecification;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import  no.cantara.electronic.component.lib.specs.base.SpecUnit;

/**
 * Base implementation of component specifications.
 * Provides common functionality for all component types.
 */
public abstract class BaseComponentSpecs implements ComponentSpecification {
    protected final Map<String, SpecValue<?>> specs;
    protected final String componentType;

    private static final Pattern REQUIREMENT_PATTERN =
            Pattern.compile("^([<>]=?|=|!=)\\s*([\\d.]+)\\s*([a-zA-Z%°Ω]+)?$");

    protected BaseComponentSpecs(String componentType) {
        this.specs = new HashMap<>();
        this.componentType = componentType;
        initializeCommonSpecs();
    }

    private void initializeCommonSpecs() {
        // Initialize common specifications all components should have
        specs.put("operatingTempMin", new SpecValue<>(-40.0, SpecUnit.CELSIUS));
        specs.put("operatingTempMax", new SpecValue<>(85.0, SpecUnit.CELSIUS));
        specs.put("package", new SpecValue<>("", SpecUnit.NONE));
        specs.put("mounting", new SpecValue<>("", SpecUnit.NONE));
        specs.put("rohs", new SpecValue<>("Yes", SpecUnit.NONE));
    }

    @Override
    public Map<String, SpecValue<?>> getSpecs() {
        return new TreeMap<>(specs);  // Return sorted copy
    }

    @Override
    public SpecValue<?> getSpec(String key) {
        return specs.get(key);
    }

    @Override
    public void setSpec(String key, SpecValue<?> value) {
        specs.put(key, value);
    }

    @Override
    public boolean meetsRequirements(Map<String, String> requirements) {
        for (Map.Entry<String, String> requirement : requirements.entrySet()) {
            String key = requirement.getKey();
            String requirementValue = requirement.getValue();

            SpecValue<?> spec = specs.get(key);
            if (spec == null) {
                return false;
            }

            if (!meetsRequirement(spec, requirementValue)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean meetsRequirement(SpecValue<?> spec, String requirementStr) {
        Matcher matcher = REQUIREMENT_PATTERN.matcher(requirementStr.trim());
        if (!matcher.matches()) {
            return false;
        }

        String operator = matcher.group(1);
        String valueStr = matcher.group(2);
        String unitStr = matcher.group(3);

        // Verify units match if specified
        if (unitStr != null) {
            SpecUnit requiredUnit = SpecUnit.fromString(unitStr);
            if (requiredUnit != spec.getUnit()) {
                return false;
            }
        }

        // Handle numeric comparisons
        if (spec.getValue() instanceof Number) {
            double specValue = ((Number) spec.getValue()).doubleValue();
            double reqValue = Double.parseDouble(valueStr);

            return switch (operator) {
                case ">" -> specValue > reqValue;
                case ">=" -> specValue >= reqValue;
                case "<" -> specValue < reqValue;
                case "<=" -> specValue <= reqValue;
                case "=" -> specValue == reqValue;
                case "!=" -> specValue != reqValue;
                default -> false;
            };
        }

        // Handle string comparisons
        String specStr = spec.getValue().toString();
        return switch (operator) {
            case "=" -> specStr.equals(valueStr);
            case "!=" -> !specStr.equals(valueStr);
            default -> false;
        };
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = new HashMap<>();

        // Validate common required fields
        validateRequired(errors, "operatingTempMin", "Operating temperature minimum");
        validateRequired(errors, "operatingTempMax", "Operating temperature maximum");
        validateRequired(errors, "package", "Package");
        validateRequired(errors, "mounting", "Mounting type");

        // Validate temperature range
        SpecValue<?> tempMin = specs.get("operatingTempMin");
        SpecValue<?> tempMax = specs.get("operatingTempMax");
        if (tempMin != null && tempMax != null) {
            double min = ((Number) tempMin.getValue()).doubleValue();
            double max = ((Number) tempMax.getValue()).doubleValue();
            if (min >= max) {
                errors.put("temperatureRange",
                        "Minimum temperature must be less than maximum temperature");
            }
        }

        return errors;
    }

    public void validateRequired(Map<String, String> errors, String key, String displayName) {
        SpecValue<?> value = specs.get(key);
        if (value == null || value.getValue() == null) {
            errors.put(key, displayName + " is required");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(componentType).append(" Specifications:\n");

        getSpecs().forEach((key, value) ->
                sb.append(String.format("%-20s: %s%n", key, value)));

        return sb.toString();
    }
}