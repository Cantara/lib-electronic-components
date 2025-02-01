package no.cantara.electronic.component.autonomous_submarine.verification;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.autonomous_submarine.subsystems.*;
import java.util.*;

/**
 * Coordinates system-wide verification of the autonomous submarine
 * Manages the verification of all subsystems and their integration
 */
public class SubmarineSystemVerification {
    private final NavigationVerification navigationVerification;
    private final PropulsionVerification propulsionVerification;
    private final PowerManagementVerification powerVerification;
    private final CommunicationsVerification communicationsVerification;
    private final SensorSuiteVerification sensorVerification;
    private final MissionControlVerification missionControlVerification;

    public SubmarineSystemVerification() {
        this.navigationVerification = new NavigationVerification();
        this.propulsionVerification = new PropulsionVerification();
        this.powerVerification = new PowerManagementVerification();
        this.communicationsVerification = new CommunicationsVerification();
        this.sensorVerification = new SensorSuiteVerification();
        this.missionControlVerification = new MissionControlVerification();
    }

    /**
     * Performs comprehensive system verification
     */
    public SystemVerificationResult verifySystem(PlannedProductionBatch batch) {
        SystemVerificationResult result = new SystemVerificationResult();

        // Verify individual subsystems
        result.addSubsystemResult("Navigation", verifySubsystem(navigationVerification, batch));
        result.addSubsystemResult("Propulsion", verifySubsystem(propulsionVerification, batch));
        result.addSubsystemResult("Power", verifySubsystem(powerVerification, batch));
        result.addSubsystemResult("Communications", verifySubsystem(communicationsVerification, batch));
        result.addSubsystemResult("Sensors", verifySubsystem(sensorVerification, batch));
        result.addSubsystemResult("Mission Control", verifySubsystem(missionControlVerification, batch));

        // Verify cross-system integration
        verifyCrossSystemIntegration(batch, result);

        return result;
    }

    private SubsystemVerificationResult verifySubsystem(
            SubsystemVerification verification,
            PlannedProductionBatch batch) {
        boolean componentsValid = verification.verifyComponents(batch);
        boolean integrationValid = verification.verifyIntegration(batch);
        List<String> issues = verification.getVerificationIssues();

        return new SubsystemVerificationResult(
                componentsValid,
                integrationValid,
                issues);
    }

    private void verifyCrossSystemIntegration(
            PlannedProductionBatch batch,
            SystemVerificationResult result) {

        // Verify power distribution
        if (!verifyPowerDistribution(batch)) {
            result.addCrossSystemIssue("Power distribution requirements not met");
        }

        // Verify communication backbone
        if (!verifyCommunicationBackbone(batch)) {
            result.addCrossSystemIssue("Communication backbone requirements not met");
        }

        // Verify mechanical integration
        if (!verifyMechanicalIntegration(batch)) {
            result.addCrossSystemIssue("Mechanical integration requirements not met");
        }

        // Verify environmental protection
        if (!verifyEnvironmentalProtection(batch)) {
            result.addCrossSystemIssue("System-wide environmental protection requirements not met");
        }

        // Verify safety systems
        if (!verifySafetySystems(batch)) {
            result.addCrossSystemIssue("System-wide safety requirements not met");
        }
    }

    private boolean verifyPowerDistribution(PlannedProductionBatch batch) {
        // Verify power budget
        boolean validPowerBudget = calculateTotalPowerRequirement(batch) <= getAvailablePower(batch);

        // Verify power delivery to all subsystems
        boolean powerDelivery = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Power"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("voltage") &&
                                entry.getSpecs().containsKey("current"));

        return validPowerBudget && powerDelivery;
    }

    private double calculateTotalPowerRequirement(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("power"))
                .mapToDouble(entry -> {
                    String power = entry.getSpecs().get("power").replaceAll("[^0-9.]", "");
                    return Double.parseDouble(power);
                })
                .sum();
    }

    private double getAvailablePower(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Power Supply"))
                .mapToDouble(entry -> {
                    String power = entry.getSpecs().get("power").replaceAll("[^0-9.]", "");
                    return Double.parseDouble(power);
                })
                .sum();
    }

    private boolean verifyCommunicationBackbone(PlannedProductionBatch batch) {
        // Verify network infrastructure
        boolean hasNetwork = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Ethernet Switch") &&
                                entry.getSpecs().getOrDefault("features", "").contains("VLAN"));

        // Verify all subsystems have network connectivity
        boolean allConnected = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> !entry.getDescription().contains("Power"))
                .allMatch(entry ->
                        entry.getSpecs().getOrDefault("interface", "").contains("Ethernet") ||
                                entry.getSpecs().getOrDefault("interface", "").contains("CAN"));

        return hasNetwork && allConnected;
    }

    private boolean verifyMechanicalIntegration(PlannedProductionBatch batch) {
        // Verify mounting compatibility
        boolean mountingCompatible = batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("mount_type"))
                .allMatch(entry ->
                        entry.getSpecs().get("mount_type").equals("DIN Rail") &&
                                entry.getSpecs().get("mount_standard").equals("EN 60715"));

        // Verify space constraints
        boolean spaceValid = verifySpaceConstraints(batch);

        // Verify thermal paths
        boolean thermalValid = verifyThermalPaths(batch);

        return mountingCompatible && spaceValid && thermalValid;
    }

    private boolean verifySpaceConstraints(PlannedProductionBatch batch) {
        // Implementation depends on specific space requirements
        return true; // Placeholder
    }

    private boolean verifyThermalPaths(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("heat_dissipation") ||
                                entry.getSpecs().containsKey("thermal_conductivity"));
    }

    private boolean verifyEnvironmentalProtection(PlannedProductionBatch batch) {
        // Verify pressure rating
        boolean pressureRated = batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("pressure_rating"))
                .allMatch(entry ->
                        entry.getSpecs().get("pressure_rating").contains("300m"));

        // Verify sealing
        boolean properlySealed = batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing"))
                .allMatch(entry ->
                        entry.getSpecs().getOrDefault("sealing", "").contains("IP68") ||
                                entry.getSpecs().getOrDefault("sealing", "").contains("Double O-ring"));

        return pressureRated && properlySealed;
    }

    private boolean verifySafetySystems(PlannedProductionBatch batch) {
        // Verify watchdog coverage
        boolean watchdogCoverage = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Watchdog"));

        // Verify emergency shutdown capability
        boolean emergencyShutdown = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Emergency Shutdown"));

        // Verify system monitoring
        boolean systemMonitoring = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Diagnostics"));

        return watchdogCoverage && emergencyShutdown && systemMonitoring;
    }
}