package no.cantara.electronic.component.lib.specs.passive;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * Specifications for capacitors.
 */
public class CapacitorSpecs extends PassiveComponentSpecs {
    public CapacitorSpecs() {
        super("Capacitor");
        initializeCapacitorSpecs();
    }

    private void initializeCapacitorSpecs() {
        specs.put("capacitance", new SpecValue<>(0.0, SpecUnit.FARADS));
        specs.put("voltageRating", new SpecValue<>(50.0, SpecUnit.VOLTS));
        specs.put("dielectric", new SpecValue<>("X7R", SpecUnit.NONE));
        specs.put("esr", new SpecValue<>(0.0, SpecUnit.OHMS));
        specs.put("rippleCurrent", new SpecValue<>(0.0, SpecUnit.AMPS));
        specs.put("leakageCurrent", new SpecValue<>(0.0, SpecUnit.MICROAMPS));
        specs.put("isPolarized", new SpecValue<>(false, SpecUnit.NONE));
        specs.put("lifetimeHours", new SpecValue<>(2000, SpecUnit.NONE));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "capacitance", "Capacitance value");
        validateRequired(errors, "voltageRating", "Voltage rating");
        validateCapacitanceRange(errors);
        validateVoltageRating(errors);
        return errors;
    }

    private void validateCapacitanceRange(Map<String, String> errors) {
        SpecValue<?> capacitance = specs.get("capacitance");
        if (capacitance != null && capacitance.getValue() instanceof Number) {
            double value = ((Number) capacitance.getValue()).doubleValue();
            if (value <= 0) {
                errors.put("capacitance", "Capacitance must be greater than 0F");
            }
        }
    }

    private void validateVoltageRating(Map<String, String> errors) {
        SpecValue<?> voltage = specs.get("voltageRating");
        if (voltage != null && voltage.getValue() instanceof Number) {
            double value = ((Number) voltage.getValue()).doubleValue();
            if (value <= 0) {
                errors.put("voltageRating", "Voltage rating must be greater than 0V");
            }
        }
    }
}
