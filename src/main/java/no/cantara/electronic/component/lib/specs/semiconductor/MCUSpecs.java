package no.cantara.electronic.component.lib.specs.semiconductor;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * Microcontroller specifications.
 */
public class MCUSpecs extends SemiconductorSpecs {
    public MCUSpecs() {
        super("Microcontroller");
        initializeMCUSpecs();
    }

    private void initializeMCUSpecs() {
        specs.put("architecture", new SpecValue<>("", SpecUnit.NONE));
        specs.put("coreType", new SpecValue<>("", SpecUnit.NONE));
        specs.put("clockFrequency", new SpecValue<>(0.0, SpecUnit.HERTZ));
        specs.put("flashSize", new SpecValue<>(0, SpecUnit.BYTES));
        specs.put("ramSize", new SpecValue<>(0, SpecUnit.BYTES));
        specs.put("eepromSize", new SpecValue<>(0, SpecUnit.BYTES));
        specs.put("vddMin", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("vddMax", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("gpioCount", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("adcChannels", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("adcResolution", new SpecValue<>(0, SpecUnit.BITS));
        specs.put("dacChannels", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("dacResolution", new SpecValue<>(0, SpecUnit.BITS));
        specs.put("uartCount", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("spiCount", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("i2cCount", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("pwmChannels", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("timers", new SpecValue<>(0, SpecUnit.COUNT));
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "architecture", "Architecture");
        validateRequired(errors, "clockFrequency", "Clock frequency");
        validateRequired(errors, "flashSize", "Flash size");
        validateRequired(errors, "ramSize", "RAM size");
        validateMemorySizes(errors);
        validateVoltages(errors);
        return errors;
    }

    private void validateMemorySizes(Map<String, String> errors) {
        SpecValue<?> flash = specs.get("flashSize");
        SpecValue<?> ram = specs.get("ramSize");

        if (flash != null && flash.getValue() instanceof Number) {
            long flashSize = ((Number) flash.getValue()).longValue();
            if (flashSize <= 0) {
                errors.put("flashSize", "Flash size must be greater than 0");
            }
        }

        if (ram != null && ram.getValue() instanceof Number) {
            long ramSize = ((Number) ram.getValue()).longValue();
            if (ramSize <= 0) {
                errors.put("ramSize", "RAM size must be greater than 0");
            }
        }
    }

    private void validateVoltages(Map<String, String> errors) {
        SpecValue<?> vddMin = specs.get("vddMin");
        SpecValue<?> vddMax = specs.get("vddMax");

        if (vddMin != null && vddMax != null) {
            double min = ((Number) vddMin.getValue()).doubleValue();
            double max = ((Number) vddMax.getValue()).doubleValue();
            if (min >= max) {
                errors.put("voltageRange", "VDD min must be less than VDD max");
            }
        }
    }
}
