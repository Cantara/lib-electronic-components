package no.cantara.electronic.component.lib.specs.semiconductor;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * MOSFET specifications.
 */
public class MOSFETSpecs extends SemiconductorSpecs {
    public enum ChannelType {
        N_CHANNEL("N-Channel"),
        P_CHANNEL("P-Channel");

        private final String description;

        ChannelType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public MOSFETSpecs() {
        super("MOSFET");
        initializeMOSFETSpecs();
    }

    private void initializeMOSFETSpecs() {
        specs.put("channelType", new SpecValue<>(ChannelType.N_CHANNEL, SpecUnit.NONE));
        specs.put("vdsMax", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("vgsMax", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("vgsThreshold", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("rdson", new SpecValue<>(0.0, SpecUnit.OHMS));
        specs.put("idMax", new SpecValue<>(0.0, SpecUnit.AMPS));
        specs.put("qg", new SpecValue<>(0.0, SpecUnit.NANOCOULOMBS));
        specs.put("qgd", new SpecValue<>(0.0, SpecUnit.NANOCOULOMBS));
        specs.put("qgs", new SpecValue<>(0.0, SpecUnit.NANOCOULOMBS));
        specs.put("trr", new SpecValue<>(0.0, SpecUnit.NANOSECONDS));
        specs.put("capacitanceInput", new SpecValue<>(0.0, SpecUnit.PICOFARADS));
        specs.put("capacitanceOutput", new SpecValue<>(0.0, SpecUnit.PICOFARADS));
        specs.put("capacitanceReverse", new SpecValue<>(0.0, SpecUnit.PICOFARADS));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "channelType", "Channel type");
        validateRequired(errors, "vdsMax", "Maximum drain-source voltage");
        validateRequired(errors, "idMax", "Maximum drain current");
        validateVoltages(errors);
        return errors;
    }

    private void validateVoltages(Map<String, String> errors) {
        SpecValue<?> vds = specs.get("vdsMax");
        SpecValue<?> vgs = specs.get("vgsMax");

        if (vds != null && vds.getValue() instanceof Number) {
            double vdsValue = ((Number) vds.getValue()).doubleValue();
            if (vdsValue <= 0) {
                errors.put("vdsMax", "VDS max must be greater than 0V");
            }
        }

        if (vgs != null && vgs.getValue() instanceof Number) {
            double vgsValue = ((Number) vgs.getValue()).doubleValue();
            if (vgsValue <= 0) {
                errors.put("vgsMax", "VGS max must be greater than 0V");
            }
        }
    }
}
