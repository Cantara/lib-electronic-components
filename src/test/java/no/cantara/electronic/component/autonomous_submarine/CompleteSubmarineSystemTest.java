package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.autonomous_submarine.subsystems.SubmarineSystemFactory;
import no.cantara.electronic.component.autonomous_submarine.subsystems.SubmarineSystemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CompleteSubmarineSystemTest {

    private SubmarineSystemValidator systemValidator;

    @BeforeEach
    void setUp() {
        systemValidator = new SubmarineSystemValidator();
    }

    @Test
    void shouldCreateCompleteSystem() {
        PlannedProductionBatch batch = SubmarineSystemFactory.createCompleteSystem(
                        "SUB-2024-001",
                        "DEEPDIVE-1000",
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
                "Complete system should have no validation errors");
    }

    @Test
    void shouldCreateMinimalSystem() {
        PlannedProductionBatch batch = SubmarineSystemFactory.createMinimalSystem(
                        "SUB-2024-002",
                        "DEEPDIVE-500",
                        "R1.0",
                        1
                )
                .build();

        assertNotNull(batch);
        assertTrue(batch.getPCBAStructure().getAssemblies().size() > 0);
        assertTrue(batch.getMechanicalStructure().getAssemblies().size() > 0);

        // Validate minimal system
        systemValidator.validate(batch);
        assertTrue(systemValidator.getValidationErrors().isEmpty(),
                "Minimal system should have no validation errors");
    }

    @Test
    void shouldHandleEnvironmentalRequirements() {
        PlannedProductionBatch batch = SubmarineSystemFactory.createCustomSystem(
                        "SUB-2024-003",
                        "DEEPDIVE-1000",
                        "R1.0",
                        1
                )
                .plannedDate(LocalDate.now())
                .withMinimalSensors()
                .build();

        systemValidator.validate(batch);

        // Verify no environmental-related validation errors
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("waterproof")));
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("thermal")));
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("pressure")));
    }

    @Test
    void shouldHandlePowerRequirements() {
        PlannedProductionBatch batch = SubmarineSystemFactory.createCustomSystem(
                        "SUB-2024-004",
                        "DEEPDIVE-1000",
                        "R1.0",
                        1
                )
                .withMinimalConfiguration()  // Only include required systems
                .build();

        systemValidator.validate(batch);

        // Check power-related validation
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("power")));
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("battery")));
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("emergency power")));
    }

    @Test
    void shouldHandleSafetyRequirements() {
        PlannedProductionBatch batch = SubmarineSystemFactory.createCustomSystem(
                        "SUB-2024-005",
                        "DEEPDIVE-1000",
                        "R1.0",
                        1
                )
                .plannedDate(LocalDate.now())
                .withMinimalConfiguration()
                .withMinimalSensors()
                .build();

        systemValidator.validate(batch);

        // Check safety-related validation
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("safety")));
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("emergency")));
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("fail-safe")));
    }

    @Test
    void shouldHandleCommunicationRequirements() {
        PlannedProductionBatch batch = SubmarineSystemFactory.createCompleteSystem(
                        "SUB-2024-006",
                        "DEEPDIVE-1000",
                        "R1.0",
                        1
                )
                .plannedDate(LocalDate.now())
                .build();

        systemValidator.validate(batch);

        // Check communication-related validation
        assertTrue(systemValidator.getValidationErrors().stream()
                .noneMatch(error -> error.toLowerCase().contains("communication")));
    }
}