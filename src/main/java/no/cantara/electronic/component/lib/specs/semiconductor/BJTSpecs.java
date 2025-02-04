package no.cantara.electronic.component.lib.specs.semiconductor;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * BJT (Bipolar Junction Transistor) specifications
 */
public class BJTSpecs extends SemiconductorSpecs {
    public enum BJTType {
        NPN("NPN"),
        PNP("PNP");

        private final String description;

        BJTType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public BJTSpecs() {
        super("BJT");
        initializeBJTSpecs();
    }

    private void initializeBJTSpecs() {
        specs.put("transistorType", new SpecValue<>(BJTType.NPN, SpecUnit.NONE));
        specs.put("vceo", new SpecValue<>(0.0, SpecUnit.VOLTS));     // Collector-Emitter Voltage
        specs.put("vebo", new SpecValue<>(0.0, SpecUnit.VOLTS));     // Emitter-Base Voltage
        specs.put("ic", new SpecValue<>(0.0, SpecUnit.AMPS));        // Collector Current
        specs.put("icMax", new SpecValue<>(0.0, SpecUnit.AMPS));     // Maximum Collector Current
        specs.put("hfe", new SpecValue<>(0.0, SpecUnit.NONE));       // DC Current Gain
        specs.put("hfeMin", new SpecValue<>(0.0, SpecUnit.NONE));    // Minimum DC Current Gain
        specs.put("hfeMax", new SpecValue<>(0.0, SpecUnit.NONE));    // Maximum DC Current Gain
        specs.put("ft", new SpecValue<>(0.0, SpecUnit.MEGAHERTZ));   // Transit Frequency
        specs.put("ccb", new SpecValue<>(0.0, SpecUnit.PICOFARADS)); // Collector-Base Capacitance
        specs.put("ceb", new SpecValue<>(0.0, SpecUnit.PICOFARADS)); // Emitter-Base Capacitance
        specs.put("pd", new SpecValue<>(0.0, SpecUnit.WATTS));       // Power Dissipation
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "transistorType", "Transistor type");
        validateRequired(errors, "vceo", "Collector-emitter voltage");
        validateRequired(errors, "ic", "Collector current");
        validateRequired(errors, "hfe", "Current gain");
        validateVoltagesAndCurrents(errors);
        return errors;
    }

    private void validateVoltagesAndCurrents(Map<String, String> errors) {
        SpecValue<?> vceo = specs.get("vceo");
        SpecValue<?> ic = specs.get("ic");
        SpecValue<?> pd = specs.get("pd");

        if (vceo != null && vceo.getValue() instanceof Number) {
            double vceoValue = ((Number) vceo.getValue()).doubleValue();
            if (vceoValue <= 0) {
                errors.put("vceo", "VCEO must be greater than 0V");
            }
        }

        if (ic != null && ic.getValue() instanceof Number) {
            double icValue = ((Number) ic.getValue()).doubleValue();
            if (icValue <= 0) {
                errors.put("ic", "IC must be greater than 0A");
            }
        }

        if (pd != null && pd.getValue() instanceof Number) {
            double pdValue = ((Number) pd.getValue()).doubleValue();
            if (pdValue <= 0) {
                errors.put("pd", "Power dissipation must be greater than 0W");
            }
        }
    }
}
