package no.cantara.electronic.component.lib.specs.semiconductor;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Map;

/**
 * Diode specifications including various diode types (rectifier, switching, zener, etc.)
 */
public class DiodeSpecs extends SemiconductorSpecs {
    public enum DiodeType {
        RECTIFIER("General Purpose Rectifier"),
        FAST_RECOVERY("Fast Recovery Rectifier"),
        SCHOTTKY("Schottky"),
        ZENER("Zener"),
        TVS("Transient Voltage Suppressor"),
        PIN("PIN Diode"),
        VARACTOR("Varactor/Varicap"),
        LED("Light Emitting Diode");

        private final String description;
        DiodeType(String description) {
            this.description = description;
        }
        public String getDescription() { return description; }
    }

    public DiodeSpecs() {
        super("Diode");
        initializeDiodeSpecs();
    }

    private void initializeDiodeSpecs() {
        specs.put("diodeType", new SpecValue<>(DiodeType.RECTIFIER, SpecUnit.NONE));
        specs.put("vrrm", new SpecValue<>(0.0, SpecUnit.VOLTS));  // Maximum Repetitive Reverse Voltage
        specs.put("vf", new SpecValue<>(0.0, SpecUnit.VOLTS));    // Forward Voltage
        specs.put("ifm", new SpecValue<>(0.0, SpecUnit.AMPS));    // Maximum Forward Current
        specs.put("ifsm", new SpecValue<>(0.0, SpecUnit.AMPS));   // Non-repetitive Forward Surge Current
        specs.put("ir", new SpecValue<>(0.0, SpecUnit.MICROAMPS)); // Reverse Current
        specs.put("trr", new SpecValue<>(0.0, SpecUnit.NANOSECONDS)); // Reverse Recovery Time
        specs.put("cj", new SpecValue<>(0.0, SpecUnit.PICOFARADS)); // Junction Capacitance

        // Additional specs for specific diode types
        specs.put("vznom", new SpecValue<>(0.0, SpecUnit.VOLTS));     // Nominal Zener Voltage
        specs.put("vclamp", new SpecValue<>(0.0, SpecUnit.VOLTS));    // Clamping Voltage (TVS)
        specs.put("pppm", new SpecValue<>(0.0, SpecUnit.WATTS));      // Peak Pulse Power
    }

    @Override
    public Map<String, String> validate() {
        Map<String, String> errors = super.validate();
        validateRequired(errors, "diodeType", "Diode type");
        validateRequired(errors, "vrrm", "Maximum reverse voltage");
        validateRequired(errors, "vf", "Forward voltage");
        validateRequired(errors, "ifm", "Maximum forward current");

        DiodeType type = (DiodeType) specs.get("diodeType").getValue();
        switch(type) {
            case ZENER -> validateZenerSpecs(errors);
            case TVS -> validateTVSSpecs(errors);
            case LED -> validateLEDSpecs(errors);
        }

        return errors;
    }

    private void validateZenerSpecs(Map<String, String> errors) {
        validateRequired(errors, "vznom", "Nominal Zener voltage");
    }

    private void validateTVSSpecs(Map<String, String> errors) {
        validateRequired(errors, "vclamp", "Clamping voltage");
        validateRequired(errors, "pppm", "Peak pulse power");
    }

    private void validateLEDSpecs(Map<String, String> errors) {
        validateRequired(errors, "wavelength", "Wavelength");
        validateRequired(errors, "luminousIntensity", "Luminous intensity");
    }
}

