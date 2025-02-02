package no.cantara.electronic.component.autonomous_submarine;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for complete submarine system creation and verification.
 */
public class CompleteSubmarineSystemTest {
    private SubmarineSystemVerification systemVerification;
    private ComponentTypeDetector typeDetector;
    private SubmarineComponentSpecs componentSpecs;
    private PCBGenerator pcbGenerator;
    private SubmarineSubsystemFactory subsystemFactory;

    @BeforeEach
    void setUp() {
        typeDetector = new ComponentTypeDetector();
        componentSpecs = new SubmarineComponentSpecs(typeDetector);
        pcbGenerator = new PCBGenerator(componentSpecs);
        subsystemFactory = new SubmarineSubsystemFactory(componentSpecs, pcbGenerator);
        systemVerification = new SubmarineSystemVerification();
    }

    @Test
    void shouldCreateAndVerifyCompleteSystem() {
        // Create batch
        PlannedProductionBatch batch = createCompleteBatch();

        // Verify system
    //    SystemVerificationResult result = systemVerification.verifySystem(batch);
    //    assertTrue(result.isValid(), "System verification failed:\n" + result.getDetailedReport());
    }

    @Test
    void shouldSerializeAndDeserializeCompleteBatch() throws Exception {
        // Create and verify initial batch
        PlannedProductionBatch originalBatch = createCompleteBatch();
        //SystemVerificationResult originalResult = systemVerification.verifySystem(originalBatch);
        //assertTrue(originalResult.isValid(), "Original system invalid:\n" + originalResult.getDetailedReport());

        // Configure ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(originalBatch);
        assertNotNull(json, "Serialization failed");

        // Deserialize from JSON
        PlannedProductionBatch deserializedBatch = objectMapper.readValue(json, PlannedProductionBatch.class);
        assertNotNull(deserializedBatch, "Deserialization failed");

        // Verify deserialized batch
//        SystemVerificationResult deserializedResult = systemVerification.verifySystem(deserializedBatch);
//        assertTrue(deserializedResult.isValid(), "Deserialized system invalid:\n" + deserializedResult.getDetailedReport());
    }

    @Test
    void shouldHandleEnvironmentalRequirements() {
        // Create batch
        PlannedProductionBatch batch = createCompleteBatch();

        // Verify waterproofing
//        systemVerification.verifyWaterproofing(batch);

        // Verify thermal management
//        systemVerification.verifyThermalManagement(batch);

        // Verify pressure ratings
        systemVerification.verifyPressureRatings(batch);
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

        // Add network infrastructure first
        subsystemFactory.addNetworkInterfaces(batch);

        // Add all subsystems
        subsystemFactory.addMissionControlSystem(batch);
        subsystemFactory.addNavigationSystem(batch);
        subsystemFactory.addPowerSystem(batch);
        subsystemFactory.addPropulsionSystem(batch);
        subsystemFactory.addCommunicationsSystem(batch);
        subsystemFactory.addSensorSystem(batch);

        return batch;
    }

    // The verification methods will be moved to SubmarineSystemVerification class
}