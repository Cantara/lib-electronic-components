package no.cantara.electronic.component.lib.specs.passive;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * Specifications for resistors.
 */
public class ResistorSpecs extends PassiveComponentSpecs {
    public ResistorSpecs() {
        super("Resistor");
        initializeResistorSpecs();
    }

    private void initializeResistorSpecs() {
        specs.put("resistance", new SpecValue<>(0.0, SpecUnit.OHMS));
        specs.put("voltageRating", new SpecValue<>(50.0, SpecUnit.VOLTS));
        specs.put("composition", new SpecValue<>("Thick Film", SpecUnit.NONE));
        specs.put("tcr", new SpecValue<>(100.0, SpecUnit.PPM));
        specs.put("noiseIndex", new SpecValue<>(-40.0, SpecUnit.DECIBELS));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "resistance", "Resistance value");
        validateResistanceRange(errors);
        return errors;
    }

    private void validateResistanceRange(Map<String, String> errors) {
        SpecValue<?> resistance = specs.get("resistance");
        if (resistance != null && resistance.getValue() instanceof Number) {
            double value = ((Number) resistance.getValue()).doubleValue();
            if (value <= 0) {
                errors.put("resistance", "Resistance must be greater than 0Ω");
            }
        }
    }
}
