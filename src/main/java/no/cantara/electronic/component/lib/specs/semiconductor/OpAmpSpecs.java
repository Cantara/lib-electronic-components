package no.cantara.electronic.component.lib.specs.semiconductor;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * Operational Amplifier specifications
 */
public class OpAmpSpecs extends ICSpecs {
    public OpAmpSpecs() {
        super("Operational Amplifier");
        initializeOpAmpSpecs();
    }

    private void initializeOpAmpSpecs() {
        specs.put("gainBandwidth", new SpecValue<>(0.0, SpecUnit.MEGAHERTZ));
        specs.put("slewRate", new SpecValue<>(0.0, SpecUnit.VOLTS_PER_MICROSECOND));
        specs.put("inputOffsetVoltage", new SpecValue<>(0.0, SpecUnit.MILLIVOLTS));
        specs.put("inputBiasCurrentMax", new SpecValue<>(0.0, SpecUnit.NANOAMPS));
        specs.put("cmrr", new SpecValue<>(0.0, SpecUnit.DECIBELS));
        specs.put("psrr", new SpecValue<>(0.0, SpecUnit.DECIBELS));
        specs.put("outputVoltageSwing", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("railToRail", new SpecValue<>(false, SpecUnit.NONE));
        specs.put("numberChannels", new SpecValue<>(1, SpecUnit.COUNT));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "gainBandwidth", "Gain bandwidth product");
        validateRequired(errors, "slewRate", "Slew rate");
        validateRequired(errors, "outputVoltageSwing", "Output voltage swing");
        validateOpAmpParameters(errors);
        return errors;
    }

    private void validateOpAmpParameters(Map<String, String> errors) {
        SpecValue<?> gbw = specs.get("gainBandwidth");
        if (gbw != null && gbw.getValue() instanceof Number) {
            double gbwValue = ((Number) gbw.getValue()).doubleValue();
            if (gbwValue <= 0) {
                errors.put("gainBandwidth", "Gain bandwidth must be greater than 0 MHz");
            }
        }

        SpecValue<?> slewRate = specs.get("slewRate");
        if (slewRate != null && slewRate.getValue() instanceof Number) {
            double srValue = ((Number) slewRate.getValue()).doubleValue();
            if (srValue <= 0) {
                errors.put("slewRate", "Slew rate must be greater than 0 V/µs");
            }
        }
    }
}
