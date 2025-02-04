package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import java.util.*;

/**
 * Utility class defining key technical specifications for different component types.
 * This helps standardize which specifications should be defined for each component type.
 */
public class ComponentSpecificationDefinitions {

    /**
     * Represents a specification definition with its unit and description
     */
    public record SpecDefinition(String name, SpecUnit unit, String description) {}

    /**
     * Gets the list of key specifications for a given component type.
     *
     * @param componentType The type of component
     * @return List of specification definitions
     */
    public static List<SpecDefinition> getKeySpecs(ComponentType componentType) {
        return switch (componentType) {
            case MOSFET -> getMOSFETSpecs();
            case VOLTAGE_REGULATOR -> getVoltageRegulatorSpecs();
            case CAPACITOR -> getCapacitorSpecs();
            case RESISTOR -> getResistorSpecs();
            case LED -> getLEDSpecs();
            case MICROCONTROLLER -> getMCUSpecs();
            case OPAMP -> getOpAmpSpecs();
            case CONNECTOR -> getConnectorSpecs();
            default -> Collections.emptyList();
        };
    }

    private static List<SpecDefinition> getMOSFETSpecs() {
        return List.of(
                new SpecDefinition("vdsMax", SpecUnit.VOLTS, "Maximum drain-source voltage"),
                new SpecDefinition("vgsMax", SpecUnit.VOLTS, "Maximum gate-source voltage"),
                new SpecDefinition("vgsThreshold", SpecUnit.VOLTS, "Gate threshold voltage"),
                new SpecDefinition("idMax", SpecUnit.AMPS, "Maximum continuous drain current"),
                new SpecDefinition("rdson", SpecUnit.OHMS, "Drain-source on-state resistance"),
                new SpecDefinition("qg", SpecUnit.NANOCOULOMBS, "Total gate charge"),
                new SpecDefinition("pd", SpecUnit.WATTS, "Power dissipation"),
                new SpecDefinition("rthJC", SpecUnit.NONE, "Thermal resistance junction-to-case")
        );
    }

    private static List<SpecDefinition> getVoltageRegulatorSpecs() {
        return List.of(
                new SpecDefinition("inputVoltageMin", SpecUnit.VOLTS, "Minimum input voltage"),
                new SpecDefinition("inputVoltageMax", SpecUnit.VOLTS, "Maximum input voltage"),
                new SpecDefinition("outputVoltage", SpecUnit.VOLTS, "Output voltage"),
                new SpecDefinition("outputCurrent", SpecUnit.AMPS, "Maximum output current"),
                new SpecDefinition("dropoutVoltage", SpecUnit.MILLIVOLTS, "Dropout voltage"),
                new SpecDefinition("efficiency", SpecUnit.PERCENTAGE, "Power efficiency"),
                new SpecDefinition("lineRegulation", SpecUnit.PERCENTAGE, "Line regulation"),
                new SpecDefinition("loadRegulation", SpecUnit.PERCENTAGE, "Load regulation"),
                new SpecDefinition("rippleRejection", SpecUnit.DECIBELS, "Ripple rejection ratio"),
                new SpecDefinition("outputNoise", SpecUnit.MICROVOLTS, "Output noise voltage")
        );
    }

    private static List<SpecDefinition> getCapacitorSpecs() {
        return List.of(
                new SpecDefinition("capacitance", SpecUnit.FARADS, "Capacitance value"),
                new SpecDefinition("voltageRating", SpecUnit.VOLTS, "Rated voltage"),
                new SpecDefinition("tolerance", SpecUnit.PERCENTAGE, "Capacitance tolerance"),
                new SpecDefinition("esr", SpecUnit.OHMS, "Equivalent series resistance"),
                new SpecDefinition("rippleCurrent", SpecUnit.AMPS, "Maximum ripple current"),
                new SpecDefinition("leakageCurrent", SpecUnit.MICROAMPS, "Leakage current"),
                new SpecDefinition("temperatureCoefficient", SpecUnit.PPM, "Temperature coefficient"),
                new SpecDefinition("lifetimeHours", SpecUnit.HOURS, "Expected lifetime")
        );
    }

    private static List<SpecDefinition> getResistorSpecs() {
        return List.of(
                new SpecDefinition("resistance", SpecUnit.OHMS, "Resistance value"),
                new SpecDefinition("tolerance", SpecUnit.PERCENTAGE, "Resistance tolerance"),
                new SpecDefinition("powerRating", SpecUnit.WATTS, "Power rating"),
                new SpecDefinition("temperatureCoefficient", SpecUnit.PPM, "Temperature coefficient"),
                new SpecDefinition("voltageRating", SpecUnit.VOLTS, "Maximum working voltage"),
                new SpecDefinition("noiseIndex", SpecUnit.DECIBELS, "Current noise index")
        );
    }

    private static List<SpecDefinition> getLEDSpecs() {
        return List.of(
                new SpecDefinition("forwardVoltage", SpecUnit.VOLTS, "Forward voltage"),
                new SpecDefinition("forwardCurrent", SpecUnit.MILLIAMPS, "Forward current"),
                new SpecDefinition("luminousIntensity", SpecUnit.MILLICANDELA, "Luminous intensity"),
                new SpecDefinition("wavelength", SpecUnit.NANOMETERS, "Peak wavelength"),
                new SpecDefinition("viewingAngle", SpecUnit.DEGREES, "Viewing angle"),
                new SpecDefinition("colorTemperature", SpecUnit.KELVIN, "Color temperature")
        );
    }

    private static List<SpecDefinition> getMCUSpecs() {
        return List.of(
                new SpecDefinition("cpuFrequency", SpecUnit.MEGAHERTZ, "CPU frequency"),
                new SpecDefinition("flashSize", SpecUnit.BYTES, "Flash memory size"),
                new SpecDefinition("ramSize", SpecUnit.BYTES, "RAM size"),
                new SpecDefinition("vddMin", SpecUnit.VOLTS, "Minimum supply voltage"),
                new SpecDefinition("vddMax", SpecUnit.VOLTS, "Maximum supply voltage"),
                new SpecDefinition("gpioCount", SpecUnit.COUNT, "Number of GPIO pins"),
                new SpecDefinition("adcResolution", SpecUnit.BITS, "ADC resolution"),
                new SpecDefinition("powerConsumption", SpecUnit.MILLIWATTS, "Active power consumption")
        );
    }

    private static List<SpecDefinition> getOpAmpSpecs() {
        return List.of(
                new SpecDefinition("gainBandwidth", SpecUnit.MEGAHERTZ, "Gain bandwidth product"),
                new SpecDefinition("slewRate", SpecUnit.VOLTS_PER_MICROSECOND, "Slew rate"),
                new SpecDefinition("inputOffsetVoltage", SpecUnit.MICROVOLTS, "Input offset voltage"),
                new SpecDefinition("inputBiasCurrentMax", SpecUnit.NANOAMPS, "Maximum input bias current"),
                new SpecDefinition("cmrr", SpecUnit.DECIBELS, "Common-mode rejection ratio"),
                new SpecDefinition("psrr", SpecUnit.DECIBELS, "Power supply rejection ratio"),
                new SpecDefinition("outputVoltageSwing", SpecUnit.VOLTS, "Output voltage swing")
        );
    }

    private static List<SpecDefinition> getConnectorSpecs() {
        return List.of(
                new SpecDefinition("pinCount", SpecUnit.COUNT, "Number of pins/contacts"),
                new SpecDefinition("currentRating", SpecUnit.AMPS, "Current rating per pin"),
                new SpecDefinition("voltageRating", SpecUnit.VOLTS, "Voltage rating"),
                new SpecDefinition("contactResistance", SpecUnit.MILLIOHMS, "Contact resistance"),
                new SpecDefinition("matingCycles", SpecUnit.COUNT, "Mating cycles"),
                new SpecDefinition("insulationResistance", SpecUnit.MEGAOHMS, "Insulation resistance"),
                new SpecDefinition("operatingTemperature", SpecUnit.CELSIUS, "Operating temperature")
        );
    }

    /**
     * Gets marine-specific additional specifications for a component type.
     */
    public static List<SpecDefinition> getMarineSpecs(ComponentType componentType) {
        List<SpecDefinition> marineSpecs = new ArrayList<>(List.of(
                new SpecDefinition("ipRating", SpecUnit.NONE, "IP protection rating"),
                new SpecDefinition("corrosionResistance", SpecUnit.NONE, "Corrosion resistance rating"),
                new SpecDefinition("saltSprayHours", SpecUnit.HOURS, "Salt spray test duration"),
                new SpecDefinition("waterproofing", SpecUnit.NONE, "Waterproofing method"),
                new SpecDefinition("pressureRating", SpecUnit.BARS, "Maximum pressure rating")
        ));

        // Add component-specific marine specs
        switch (componentType) {
            case CONNECTOR -> marineSpecs.addAll(List.of(
                    new SpecDefinition("underwaterMating", SpecUnit.NONE, "Underwater mating capable"),
                    new SpecDefinition("seaWaterResistant", SpecUnit.NONE, "Sea water resistance rating")
            ));
            case MICROCONTROLLER, VOLTAGE_REGULATOR -> marineSpecs.addAll(List.of(
                    new SpecDefinition("conformalCoating", SpecUnit.NONE, "Conformal coating type"),
                    new SpecDefinition("moistureResistance", SpecUnit.NONE, "Moisture resistance level")
            ));
        }

        return marineSpecs;
    }
}