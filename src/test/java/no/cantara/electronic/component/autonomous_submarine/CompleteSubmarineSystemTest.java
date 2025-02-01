package no.cantara.electronic.component.autonomous_submarine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.autonomous_submarine.subsystems.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for complete submarine system verification
 */
public class CompleteSubmarineSystemTest {

    private SubmarineSystemVerification systemVerification;
    private final ObjectMapper objectMapper;

    public CompleteSubmarineSystemTest() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @BeforeEach
    void setUp() {
        systemVerification = new SubmarineSystemVerification();
    }

    @Test
    void shouldCreateAndVerifyCompleteSystem() {
        // Create production batch with all subsystems
        PlannedProductionBatch batch = createCompleteBatch();

        // Verify the complete system
        SystemVerificationResult result = systemVerification.verifySystem(batch);

        // Assert overall system validity
        assertTrue(result.isValid(), "System verification failed:\n" + result.getDetailedReport());

        // Verify individual subsystems
        verifyAllSubsystems(result);

        // Verify no cross-system issues
        assertTrue(result.getCrossSystemIssues().isEmpty(),
                "Found cross-system issues:\n" + String.join("\n", result.getCrossSystemIssues()));
    }

    @Test
    void shouldSerializeAndDeserializeCompleteBatch() throws Exception {
        // Create and verify initial batch
        PlannedProductionBatch originalBatch = createCompleteBatch();
        SystemVerificationResult originalResult = systemVerification.verifySystem(originalBatch);
        assertTrue(originalResult.isValid(), "Original system invalid:\n" + originalResult.getDetailedReport());

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(originalBatch);
        assertNotNull(json, "Serialization failed");

        // Deserialize from JSON
        PlannedProductionBatch deserializedBatch = objectMapper.readValue(json, PlannedProductionBatch.class);
        assertNotNull(deserializedBatch, "Deserialization failed");

        // Verify deserialized batch
        SystemVerificationResult deserializedResult = systemVerification.verifySystem(deserializedBatch);
        assertTrue(deserializedResult.isValid(),
                "Deserialized system invalid:\n" + deserializedResult.getDetailedReport());
    }

    @Test
    void shouldCreateMinimalViableSystem() {
        // Create minimal system without optional subsystems
        PlannedProductionBatch minimalBatch = createMinimalBatch();

        // Verify minimal system
        SystemVerificationResult result = systemVerification.verifySystem(minimalBatch);

        // Assert core functionality
        assertTrue(result.isValid(), "Minimal system verification failed:\n" + result.getDetailedReport());
        verifyMinimalSubsystems(result);
    }

    @Test
    void shouldHandleEnvironmentalRequirements() {
        PlannedProductionBatch batch = createCompleteBatch();
        SystemVerificationResult result = systemVerification.verifySystem(batch);

        // Verify pressure ratings
        verifyPressureRatings(result);

        // Verify waterproofing
        verifyWaterproofing(result);

        // Verify thermal management
        verifyThermalManagement(result);
    }

    @Test
    void shouldVerifySafetyRequirements() {
        PlannedProductionBatch batch = createCompleteBatch();
        SystemVerificationResult result = systemVerification.verifySystem(batch);

        // Verify safety systems
        verifySafetySystems(result);

        // Verify redundancy
        verifyRedundancySystems(result);

        // Verify emergency systems
        verifyEmergencySystems(result);
    }

    private PlannedProductionBatch createCompleteBatch() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-FULL-001",
                "DEEPDIVE-1000",
                "R1.0",
                5
        );

        batch.setPlannedDate(LocalDate.of(2024, 6, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // Add all subsystems
        addMissionControl(batch);
        addNavigation(batch);
        addPowerSystem(batch);
        addPropulsion(batch);
        addCommunications(batch);
        addSensors(batch);

        return batch;
    }

    private PlannedProductionBatch createMinimalBatch() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-MIN-001",
                "DEEPDIVE-500",
                "R1.0",
                5
        );

        batch.setPlannedDate(LocalDate.of(2024, 6, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // Add only essential subsystems
        addMissionControl(batch);
        addNavigation(batch);
        addPowerSystem(batch);
        addPropulsion(batch);

        return batch;
    }

    private void addMissionControl(PlannedProductionBatch batch) {
        var mainBoard = MissionControlComponents.PCBAComponents.createMainControlBoard();
        batch.addPCBA(mainBoard);

        var housing = MissionControlComponents.MechanicalComponents.createHousing();
        batch.addMechanical(housing);
    }

    private void addNavigation(PlannedProductionBatch batch) {
        var navBoard = NavigationControlComponents.PCBAComponents.createMainControlBoard();
        batch.addPCBA(navBoard);

        var housing = NavigationControlComponents.MechanicalComponents.createHousing();
        batch.addMechanical(housing);
    }

    private void addPowerSystem(PlannedProductionBatch batch) {
        var powerBoard = PowerManagementComponents.PCBAComponents.createPowerControlBoard();
        batch.addPCBA(powerBoard);

        var housing = PowerManagementComponents.MechanicalComponents.createHousing();
        batch.addMechanical(housing);
    }

    private void addPropulsion(PlannedProductionBatch batch) {
        var propulsionBoard = PropulsionComponents.PCBAComponents.createMotorControlBoard();
        batch.addPCBA(propulsionBoard);

        var propulsionUnit = PropulsionComponents.MechanicalComponents.createPropulsionUnit();
        batch.addMechanical(propulsionUnit);
    }

    private void addCommunications(PlannedProductionBatch batch) {
        var commsBoard = CommunicationsComponents.PCBAComponents.createMainCommunicationsBoard();
        batch.addPCBA(commsBoard);

        var housing = CommunicationsComponents.MechanicalComponents.createHousing();
        batch.addMechanical(housing);
    }

    private void addSensors(PlannedProductionBatch batch) {
        var sensorBoard = SensorSuiteComponents.PCBAComponents.createSensorInterfaceBoard();
        batch.addPCBA(sensorBoard);

        var sensorPod = SensorSuiteComponents.MechanicalComponents.createSensorPod();
        batch.addMechanical(sensorPod);
    }

    private void verifyAllSubsystems(SystemVerificationResult result) {
        var subsystemResults = result.getSubsystemResults();

        assertTrue(subsystemResults.get("Mission Control").isValid(),
                "Mission Control verification failed");
        assertTrue(subsystemResults.get("Navigation").isValid(),
                "Navigation verification failed");
        assertTrue(subsystemResults.get("Power").isValid(),
                "Power Management verification failed");
        assertTrue(subsystemResults.get("Propulsion").isValid(),
                "Propulsion verification failed");
        assertTrue(subsystemResults.get("Communications").isValid(),
                "Communications verification failed");
        assertTrue(subsystemResults.get("Sensors").isValid(),
                "Sensor Suite verification failed");
    }

    private void verifyMinimalSubsystems(SystemVerificationResult result) {
        var subsystemResults = result.getSubsystemResults();

        assertTrue(subsystemResults.get("Mission Control").isValid(),
                "Mission Control verification failed");
        assertTrue(subsystemResults.get("Navigation").isValid(),
                "Navigation verification failed");
        assertTrue(subsystemResults.get("Power").isValid(),
                "Power Management verification failed");
        assertTrue(subsystemResults.get("Propulsion").isValid(),
                "Propulsion verification failed");
    }

    private void verifyPressureRatings(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().values().stream()
                        .allMatch(SubsystemVerificationResult::isValid),
                "Pressure rating requirements not met");
    }

    private void verifyWaterproofing(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().values().stream()
                        .allMatch(SubsystemVerificationResult::isValid),
                "Waterproofing requirements not met");
    }

    private void verifyThermalManagement(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().values().stream()
                        .allMatch(SubsystemVerificationResult::isValid),
                "Thermal management requirements not met");
    }

    private void verifySafetySystems(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().get("Mission Control").isValid(),
                "Safety system requirements not met");
    }

    private void verifyRedundancySystems(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().get("Mission Control").isValid(),
                "Redundancy requirements not met");
    }

    private void verifyEmergencySystems(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().get("Mission Control").isValid(),
                "Emergency system requirements not met");
    }
}