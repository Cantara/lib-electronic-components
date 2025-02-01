package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;

import java.util.List;

/**
 * Base interface for subsystem verification
 */
public interface SubsystemVerification {
    boolean verifyComponents(PlannedProductionBatch batch);
    boolean verifyIntegration(PlannedProductionBatch batch);
    List<String> getVerificationIssues();
}