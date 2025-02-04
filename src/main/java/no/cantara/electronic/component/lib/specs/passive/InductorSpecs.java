package no.cantara.electronic.component.lib.specs.passive;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * Specifications for inductors.
 */
public class InductorSpecs extends PassiveComponentSpecs {
    public InductorSpecs() {
        super("Inductor");
        initializeInductorSpecs();
    }

    private void initializeInductorSpecs() {
        specs.put("inductance", new SpecValue<>(0.0, SpecUnit.HENRIES));
        specs.put("dcResistance", new SpecValue<>(0.0, SpecUnit.OHMS));
        specs.put("saturationCurrent", new SpecValue<>(0.0, SpecUnit.AMPS));
        specs.put("ratedCurrent", new SpecValue<>(0.0, SpecUnit.AMPS));
        specs.put("selfResonantFreq", new SpecValue<>(0.0, SpecUnit.HERTZ));
        specs.put("qFactor", new SpecValue<>(0.0, SpecUnit.NONE));
        specs.put("shieldingType", new SpecValue<>("Unshielded", SpecUnit.NONE));
        specs.put("core", new SpecValue<>("Ferrite", SpecUnit.NONE));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "inductance", "Inductance value");
        validateRequired(errors, "ratedCurrent", "Rated current");
        validateInductanceRange(errors);
        validateCurrentRatings(errors);
        return errors;
    }

    private void validateInductanceRange(Map<String, String> errors) {
        SpecValue<?> inductance = specs.get("inductance");
        if (inductance != null && inductance.getValue() instanceof Number) {
            double value = ((Number) inductance.getValue()).doubleValue();
            if (value <= 0) {
                errors.put("inductance", "Inductance must be greater than 0H");
            }
        }
    }

    private void validateCurrentRatings(Map<String, String> errors) {
        SpecValue<?> rated = specs.get("ratedCurrent");
        SpecValue<?> saturation = specs.get("saturationCurrent");

        if (rated != null && rated.getValue() instanceof Number) {
            double ratedValue = ((Number) rated.getValue()).doubleValue();
            if (ratedValue <= 0) {
                errors.put("ratedCurrent", "Rated current must be greater than 0A");
            }

            if (saturation != null && saturation.getValue() instanceof Number) {
                double satValue = ((Number) saturation.getValue()).doubleValue();
                if (satValue < ratedValue) {
                    errors.put("currentRatings",
                            "Saturation current must be greater than or equal to rated current");
                }
            }
        }
    }
}
