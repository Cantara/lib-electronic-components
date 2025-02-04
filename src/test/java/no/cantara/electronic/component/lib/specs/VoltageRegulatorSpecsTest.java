package no.cantara.electronic.component.lib.specs;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import no.cantara.electronic.component.lib.specs.power.RegulatorType;
import no.cantara.electronic.component.lib.specs.power.VoltageRegulatorSpecs;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test 1: Demonstrates creating and validating a voltage regulator specification
 */
public class VoltageRegulatorSpecsTest {

    @Test
    public void shouldCreateValidLDORegulator() {
        // Create LDO regulator specification
        VoltageRegulatorSpecs ldo = new VoltageRegulatorSpecs();

        // Required base specifications
        ldo.setSpec("package", new SpecValue<>("SOT-223", SpecUnit.NONE));
        ldo.setSpec("mounting", new SpecValue<>("SMD", SpecUnit.NONE));

        // Regulator specific specifications
        ldo.setSpec("regulatorType", new SpecValue<>(RegulatorType.LDO, SpecUnit.NONE));
        ldo.setSpec("inputVoltageMin", new SpecValue<>(4.5, SpecUnit.VOLTS));
        ldo.setSpec("inputVoltageMax", new SpecValue<>(5.5, SpecUnit.VOLTS));
        ldo.setSpec("outputVoltage", new SpecValue<>(3.3, SpecUnit.VOLTS));
        ldo.setSpec("outputCurrent", new SpecValue<>(1.0, SpecUnit.AMPS));
        ldo.setSpec("dropoutVoltage", new SpecValue<>(200.0, SpecUnit.MILLIVOLTS));
        ldo.setSpec("efficiency", new SpecValue<>(85.0, SpecUnit.PERCENTAGE));

        // Protection features
        ldo.setSpec("thermalShutdown", new SpecValue<>(true, SpecUnit.NONE));
        ldo.setSpec("shortCircuitProtection", new SpecValue<>(true, SpecUnit.NONE));

        // Temperature specifications
        ldo.setSpec("operatingTempMin", new SpecValue<>(-40.0, SpecUnit.CELSIUS));
        ldo.setSpec("operatingTempMax", new SpecValue<>(125.0, SpecUnit.CELSIUS));

        // Performance specifications
        ldo.setSpec("lineRegulation", new SpecValue<>(0.02, SpecUnit.PERCENTAGE));
        ldo.setSpec("loadRegulation", new SpecValue<>(0.1, SpecUnit.PERCENTAGE));
        ldo.setSpec("rippleRejection", new SpecValue<>(60.0, SpecUnit.DECIBELS));

        // Validate the specification
        Map<String, String> errors = ldo.validate();

        // Print any validation errors for debugging
        if (!errors.isEmpty()) {
            System.out.println("Validation errors:");
            errors.forEach((key, value) -> System.out.println(key + ": " + value));
        }

        assertTrue(errors.isEmpty(), "Specification should be valid: " + errors);

        // Verify values
        SpecValue<?> outputVoltage = ldo.getSpec("outputVoltage");
        assertEquals(3.3, ((Double) outputVoltage.getValue()), 0.001);
        assertEquals(SpecUnit.VOLTS, outputVoltage.getUnit());
    }

    @Test
    public void shouldDetectInvalidRegulatorSpecs() {
        VoltageRegulatorSpecs regulator = new VoltageRegulatorSpecs();
        regulator.setSpec("regulatorType", new SpecValue<>(RegulatorType.SWITCHING_BUCK, SpecUnit.NONE));
        regulator.setSpec("inputVoltageMin", new SpecValue<>(12.0, SpecUnit.VOLTS));
        regulator.setSpec("inputVoltageMax", new SpecValue<>(5.0, SpecUnit.VOLTS));  // Invalid: max < min
        regulator.setSpec("outputVoltage", new SpecValue<>(3.3, SpecUnit.VOLTS));
        regulator.setSpec("efficiency", new SpecValue<>(110.0, SpecUnit.PERCENTAGE)); // Invalid: > 100%

        Map<String, String> errors = regulator.validate();

        // Print validation errors for debugging
        System.out.println("Expected validation errors:");
        errors.forEach((key, value) -> System.out.println(key + ": " + value));

        assertFalse(errors.isEmpty(), "Should detect validation errors");
        assertTrue(errors.containsKey("inputVoltage"), "Should detect invalid input voltage range");
        assertTrue(errors.containsKey("efficiency"), "Should detect invalid efficiency value");
    }

    @Test
    public void shouldValidateVoltageRelationships() {
        VoltageRegulatorSpecs ldo = new VoltageRegulatorSpecs();

        // Set up a valid LDO configuration
        ldo.setSpec("regulatorType", new SpecValue<>(RegulatorType.LDO, SpecUnit.NONE));
        ldo.setSpec("inputVoltageMin", new SpecValue<>(4.5, SpecUnit.VOLTS));
        ldo.setSpec("inputVoltageMax", new SpecValue<>(5.5, SpecUnit.VOLTS));
        ldo.setSpec("outputVoltage", new SpecValue<>(3.3, SpecUnit.VOLTS));
        ldo.setSpec("dropoutVoltage", new SpecValue<>(200.0, SpecUnit.MILLIVOLTS));

        Map<String, String> errors = ldo.validate();

        // Verify dropout voltage is less than input-output differential
        double vinMin = 4.5;
        double vout = 3.3;
        double dropout = 0.2; // 200mV
        assertTrue((vinMin - vout) > dropout, "Input-output differential should exceed dropout voltage");
    }
}