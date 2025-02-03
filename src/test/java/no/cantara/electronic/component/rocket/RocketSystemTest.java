package no.cantara.electronic.component.rocket;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RocketSystemTest {
    private RocketSystemValidator systemValidator;

    @BeforeEach
    void setUp() {
        systemValidator = new RocketSystemValidator();
    }

    @Test
    void shouldCreateCompleteRocketSystem() {
        PlannedProductionBatch batch = RocketSystemFactory.createCompleteSystem(
                        "RKT-2024-001",
                        "ORION-X",
                        "R1.0",
                        1
                )
                .plannedDate(LocalDate.now())
                .build();

        assertNotNull(batch);
        assertTrue(batch.getPCBAStructure().getAssemblies().size() > 0);
        assertTrue(batch.getMechanicalStructure().getAssemblies().size() > 0);

        // Validate the complete system
        systemValidator.validate(batch);
        assertTrue(systemValidator.getValidationErrors().isEmpty(),
                "Complete rocket system should have no validation errors");
    }

    @Test
    void shouldHandleFlightControlRequirements() {
        PlannedProductionBatch batch = RocketSystemFactory.createCustomSystem(
                        "RKT-2024-002",
                        "ORION-X",
                        "R1.0",
                        1
                )
                .withFlightControl()
                .withRedundancy()
                .build();

        // Verify flight control requirements
        verifyFlightControlRequirements(batch);
    }

    @Test
    void shouldHandlePropulsionRequirements() {
        PlannedProductionBatch batch = RocketSystemFactory.createCustomSystem(
                        "RKT-2024-003",
                        "ORION-X",
                        "R1.0",
                        1
                )
                .withPropulsion()
                .withRedundancy()
                .build();

        // Verify propulsion requirements
        verifyPropulsionRequirements(batch);
    }

    @Test
    void shouldHandleTelemetryRequirements() {
        PlannedProductionBatch batch = RocketSystemFactory.createCustomSystem(
                        "RKT-2024-004",
                        "ORION-X",
                        "R1.0",
                        1
                )
                .withTelemetry()
                .withRedundancy()
                .build();

        // Verify telemetry requirements
        verifyTelemetryRequirements(batch);
    }
    private void verifyFlightControlRequirements(PlannedProductionBatch batch) {
        // Verify essential flight control components
        assertTrue(systemValidator.hasComponent(batch, "STM32H755", "Dual-Core Flight Controller"));
        assertTrue(systemValidator.hasComponent(batch, "BMI088", "High-G IMU"));
        assertTrue(systemValidator.hasComponent(batch, "MS5611", "High-Precision Barometer"));
        assertTrue(systemValidator.hasComponent(batch, "MAX31856", "Thermocouple Interface"));

        // Verify redundancy
        assertTrue(systemValidator.hasRedundantComponents(batch, "IMU"));
        assertTrue(systemValidator.hasRedundantComponents(batch, "Barometer"));

        // Verify thermal management
        assertTrue(systemValidator.hasThermalManagement(batch));

        // Verify safety systems
        assertTrue(systemValidator.hasWatchdogProtection(batch));
        assertTrue(systemValidator.hasEmergencyAbortSystem(batch));
    }

    private void verifyPropulsionRequirements(PlannedProductionBatch batch) {
        // Verify essential propulsion components
        assertTrue(systemValidator.hasComponent(batch, "DRV8701", "Motor Driver"));
        assertTrue(systemValidator.hasComponent(batch, "INA240", "Current Sense Amplifier"));
        assertTrue(systemValidator.hasComponent(batch, "MAX31855", "Thermocouple Interface"));

        // Verify redundancy
        assertTrue(systemValidator.hasRedundantComponents(batch, "Motor Driver"));
        assertTrue(systemValidator.hasRedundantComponents(batch, "Current Sensor"));

        // Verify protection systems
        assertTrue(systemValidator.hasOvercurrentProtection(batch));
        assertTrue(systemValidator.hasThermalProtection(batch));
    }

    private void verifyTelemetryRequirements(PlannedProductionBatch batch) {
        // Verify essential telemetry components
        assertTrue(systemValidator.hasComponent(batch, "CC1200", "High-Power RF Transceiver"));
        assertTrue(systemValidator.hasComponent(batch, "NEO-M9N", "GPS Module"));
        assertTrue(systemValidator.hasComponent(batch, "SE2435L", "RF Power Amplifier"));

        // Verify redundancy
        assertTrue(systemValidator.hasRedundantComponents(batch, "RF Transceiver"));
        assertTrue(systemValidator.hasRedundantComponents(batch, "GPS"));

        // Verify security features
        assertTrue(systemValidator.hasEncryption(batch));
        assertTrue(systemValidator.hasSecureBootloader(batch));
    }

}