package no.cantara.electronic.component.lib.specs.passive;

import no.cantara.electronic.component.lib.specs.base.*;
import java.util.Map;

/**
 * Base class for passive component specifications.
 */
public abstract class PassiveComponentSpecs extends BaseComponentSpecs {
    protected PassiveComponentSpecs(String componentType) {
        super(componentType);
        initializePassiveSpecs();
    }

    private void initializePassiveSpecs() {
        // Common specifications for all passive components
        specs.put("tolerance", new SpecValue<>(ComponentTolerance.GENERAL_PURPOSE, SpecUnit.NONE));
        specs.put("ratedPower", new SpecValue<>(0.125, SpecUnit.WATTS));
        specs.put("temperatureCoefficient", new SpecValue<>(TemperatureCoefficient.TCR_100, SpecUnit.NONE));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "tolerance", "Tolerance");
        validateRequired(errors, "ratedPower", "Rated power");
        return errors;
    }
}

