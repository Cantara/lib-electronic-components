package no.cantara.electronic.component.lib.specs.semiconductor;

import no.cantara.electronic.component.lib.specs.base.BaseComponentSpecs;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import java.util.Map;

/**
 * Base class for semiconductor specifications.
 */
public abstract class SemiconductorSpecs extends BaseComponentSpecs {
    protected SemiconductorSpecs(String componentType) {
        super(componentType);
        initializeSemiconductorSpecs();
    }

    private void initializeSemiconductorSpecs() {
        // Explicitly specify the type parameters for each SpecValue
        specs.put("technology", new SpecValue<SemiconductorTechnology>(SemiconductorTechnology.SILICON, SpecUnit.NONE));
        specs.put("junctionTempMax", new SpecValue<Double>(150.0, SpecUnit.CELSIUS));
        specs.put("thermalResistance", new SpecValue<Double>(0.0, SpecUnit.NONE));
        specs.put("esd", new SpecValue<Double>(2000.0, SpecUnit.VOLTS));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "technology", "Semiconductor technology");
        validateRequired(errors, "junctionTempMax", "Maximum junction temperature");
        return errors;
    }

    public void validateRequired(Map<String, String> errors, String key, String displayName) {
        SpecValue<?> value = specs.get(key);
        if (value == null || value.getValue() == null) {
            errors.put(key, displayName + " is required");
        }
    }
}