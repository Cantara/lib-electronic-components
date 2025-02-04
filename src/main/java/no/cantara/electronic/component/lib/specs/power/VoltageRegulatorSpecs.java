package no.cantara.electronic.component.lib.specs.power;

import no.cantara.electronic.component.lib.specs.base.BaseComponentSpecs;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import java.util.Map;

public class VoltageRegulatorSpecs extends BaseComponentSpecs {

    public VoltageRegulatorSpecs() {
        super("Voltage Regulator");
        initializeRegulatorSpecs();
    }

    private void initializeRegulatorSpecs() {
        // Basic specifications remain the same...
        specs.put("regulatorType", new SpecValue<RegulatorType>(RegulatorType.LINEAR, SpecUnit.NONE));
        specs.put("inputVoltageMin", new SpecValue<Double>(0.0, SpecUnit.VOLTS));
        specs.put("inputVoltageMax", new SpecValue<Double>(0.0, SpecUnit.VOLTS));
        specs.put("outputVoltage", new SpecValue<Double>(0.0, SpecUnit.VOLTS));
        specs.put("outputCurrent", new SpecValue<Double>(0.0, SpecUnit.AMPS));
        specs.put("dropoutVoltage", new SpecValue<Double>(0.0, SpecUnit.VOLTS));
        specs.put("efficiency", new SpecValue<Double>(0.0, SpecUnit.PERCENTAGE));
        specs.put("lineRegulation", new SpecValue<Double>(0.0, SpecUnit.PERCENTAGE));
        specs.put("loadRegulation", new SpecValue<Double>(0.0, SpecUnit.PERCENTAGE));
        specs.put("rippleRejection", new SpecValue<Double>(0.0, SpecUnit.DECIBELS));
        specs.put("outputNoise", new SpecValue<Double>(0.0, SpecUnit.MICROVOLTS));

        // Protection features
        specs.put("thermalShutdown", new SpecValue<Boolean>(false, SpecUnit.NONE));
        specs.put("shortCircuitProtection", new SpecValue<Boolean>(false, SpecUnit.NONE));

        // Switching regulator specific
        specs.put("switchingFrequency", new SpecValue<Double>(0.0, SpecUnit.KILOHERTZ));
        specs.put("dutyCycleMax", new SpecValue<Double>(0.0, SpecUnit.PERCENTAGE));
        specs.put("softStart", new SpecValue<Boolean>(false, SpecUnit.NONE));
        specs.put("synchronizable", new SpecValue<Boolean>(false, SpecUnit.NONE));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();

        // Get regulator type
        SpecValue<?> typeSpec = specs.get("regulatorType");
        if (typeSpec == null || typeSpec.getValue() == null) {
            errors.put("regulatorType", "Regulator type must be specified");
            return errors;
        }

        RegulatorType type = (RegulatorType) typeSpec.getValue();

        // Validate common parameters
        validateRequired(errors, "inputVoltageMin", "Minimum input voltage must be specified");
        validateRequired(errors, "inputVoltageMax", "Maximum input voltage must be specified");
        validateRequired(errors, "outputVoltage", "Output voltage must be specified");
        validateRequired(errors, "outputCurrent", "Output current must be specified");

        validateVoltages(errors);
        validateEfficiency(errors);

        // Validate switching-specific parameters only for switching regulators
        if (isSwichingRegulator(type)) {
            validateSwitchingParameters(errors);
        }

        return errors;
    }

    private boolean isSwichingRegulator(RegulatorType type) {
        return switch(type) {
            case SWITCHING_BUCK, SWITCHING_BOOST, SWITCHING_BUCK_BOOST,
                 SWITCHING_FLYBACK, SWITCHING_FORWARD, SWITCHING_SEPIC -> true;
            case LINEAR, LDO -> false;
        };
    }

    private void validateVoltages(Map<String, String> errors) {
        SpecValue<?> vinMin = specs.get("inputVoltageMin");
        SpecValue<?> vinMax = specs.get("inputVoltageMax");
        SpecValue<?> vout = specs.get("outputVoltage");

        if (vinMin != null && vinMax != null &&
                vinMin.getValue() instanceof Number && vinMax.getValue() instanceof Number) {
            double min = (Double) vinMin.getValue();
            double max = (Double) vinMax.getValue();
            if (min >= max) {
                errors.put("inputVoltage", "Minimum input voltage must be less than maximum");
            }
        }

        RegulatorType type = (RegulatorType) specs.get("regulatorType").getValue();
        if (type == RegulatorType.LDO) {
            validateLDOVoltages(errors);
        }
    }

    private void validateLDOVoltages(Map<String, String> errors) {
        SpecValue<?> vinMin = specs.get("inputVoltageMin");
        SpecValue<?> vout = specs.get("outputVoltage");
        SpecValue<?> dropout = specs.get("dropoutVoltage");

        if (vinMin != null && vout != null && dropout != null &&
                vinMin.getValue() instanceof Number &&
                vout.getValue() instanceof Number &&
                dropout.getValue() instanceof Number) {

            double vin = (Double) vinMin.getValue();
            double voutVal = (Double) vout.getValue();
            double vdrop = (Double) dropout.getValue();

            // Convert dropout voltage from millivolts to volts if necessary
            if (dropout.getUnit() == SpecUnit.MILLIVOLTS) {
                vdrop = vdrop / 1000.0;
            }

            if ((vin - voutVal) <= vdrop) {
                errors.put("voltageDropout",
                        String.format("Input-output differential (%.2fV) must be greater than dropout voltage (%.2fV)",
                                (vin - voutVal), vdrop));
            }
        }
    }

    private void validateEfficiency(Map<String, String> errors) {
        SpecValue<?> efficiency = specs.get("efficiency");
        if (efficiency != null && efficiency.getValue() instanceof Number) {
            double eff = (Double) efficiency.getValue();
            if (eff <= 0 || eff > 100) {
                errors.put("efficiency", "Efficiency must be between 0% and 100%");
            }
        }
    }

    private void validateSwitchingParameters(Map<String, String> errors) {
        SpecValue<?> freq = specs.get("switchingFrequency");
        if (freq != null && freq.getValue() instanceof Number) {
            double frequency = (Double) freq.getValue();
            if (frequency <= 0) {
                errors.put("switchingFrequency", "Switching frequency must be greater than 0");
            }
        }

        SpecValue<?> duty = specs.get("dutyCycleMax");
        if (duty != null && duty.getValue() instanceof Number) {
            double dutyCycle = (Double) duty.getValue();
            if (dutyCycle <= 0 || dutyCycle > 100) {
                errors.put("dutyCycleMax", "Maximum duty cycle must be between 0% and 100%");
            }
        }
    }
}