package no.cantara.electronic.component.lib.specs.passive;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Utility class for working with passive component specifications.
 */
public class PassiveComponentUtils {

    /**
     * Calculates power rating requirement based on voltage and resistance.
     */
    public static double calculateRequiredPowerRating(double voltage, double resistance) {
        return (voltage * voltage) / resistance;
    }

    /**
     * Calculates the maximum voltage across a resistor based on its power rating.
     */
    public static double calculateMaxVoltage(double powerRating, double resistance) {
        return Math.sqrt(powerRating * resistance);
    }

    /**
     * Converts a capacitance value to a standard unit (Farads).
     */
    public static SpecValue<Double> normalizeCapacitance(SpecValue<?> value) {
        if (!(value.getValue() instanceof Number)) {
            throw new IllegalArgumentException("Value must be numeric");
        }

        double normalizedValue = ((Number) value.getValue()).doubleValue();

        switch (value.getUnit()) {
            case PICOFARADS -> normalizedValue *= 1e-12;
            case NANOFARADS -> normalizedValue *= 1e-9;
            case MICROFARADS -> normalizedValue *= 1e-6;
            case FARADS -> {}
            default -> throw new IllegalArgumentException("Invalid capacitance unit");
        }

        return new SpecValue<>(normalizedValue, SpecUnit.FARADS);
    }

    /**
     * Converts an inductance value to a standard unit (Henries).
     */
    public static SpecValue<Double> normalizeInductance(SpecValue<?> value) {
        if (!(value.getValue() instanceof Number)) {
            throw new IllegalArgumentException("Value must be numeric");
        }

        double normalizedValue = ((Number) value.getValue()).doubleValue();

        switch (value.getUnit()) {
            case MICROHENRIES -> normalizedValue *= 1e-6;
            case MILLIHENRIES -> normalizedValue *= 1e-3;
            case HENRIES -> {}
            default -> throw new IllegalArgumentException("Invalid inductance unit");
        }

        return new SpecValue<>(normalizedValue, SpecUnit.HENRIES);
    }

    /**
     * Calculates the resonant frequency of an LC circuit.
     */
    public static double calculateResonantFrequency(double inductance, double capacitance) {
        return 1 / (2 * Math.PI * Math.sqrt(inductance * capacitance));
    }
}