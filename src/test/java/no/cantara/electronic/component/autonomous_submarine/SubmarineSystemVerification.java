package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.autonomous_submarine.subsystems.*;
import no.cantara.electronic.component.BOMEntry;
import java.util.*;

public class SubmarineSystemVerification {
    private final List<String> crossSystemIssues = new ArrayList<>();

    public SystemVerificationResult verifySystem(PlannedProductionBatch batch) {
        SystemVerificationResult result = new SystemVerificationResult();

        try {
            verifySubsystem(batch, result, "Mission Control", new MissionControlVerification());
            verifySubsystem(batch, result, "Navigation", new NavigationVerification());
            verifySubsystem(batch, result, "Power", new PowerManagementVerification());
            verifySubsystem(batch, result, "Propulsion", new PropulsionVerification());
            verifySubsystem(batch, result, "Communications", new CommunicationsVerification());
            verifySubsystem(batch, result, "Sensors", new SensorSuiteVerification());

            verifyCrossSystemIntegration(batch);
            result.setCrossSystemIssues(crossSystemIssues);
        } catch (Exception e) {
            crossSystemIssues.add("System verification failed: " + e.getMessage());
        }

        return result;
    }

    private void verifySubsystem(PlannedProductionBatch batch, SystemVerificationResult result,
                                 String subsystemName, SubsystemVerification verification) {
        try {
            boolean componentsValid = verification.verifyComponents(batch);
            boolean integrationValid = verification.verifyIntegration(batch);
            List<String> issues = verification.getVerificationIssues();

            result.addSubsystemResult(subsystemName, new SubsystemVerificationResult(
                    componentsValid,
                    integrationValid,
                    issues
            ));
        } catch (Exception e) {
            List<String> errorIssues = Collections.singletonList("Subsystem verification failed: " + e.getMessage());
            result.addSubsystemResult(subsystemName, new SubsystemVerificationResult(
                    false,
                    false,
                    errorIssues
            ));
        }
    }

    private void verifyCrossSystemIntegration(PlannedProductionBatch batch) {
        verifyPowerDistribution(batch);
        verifyNetworkConnectivity(batch);
        verifyEnvironmentalCompliance(batch);
        verifySafetySystemIntegration(batch);
    }

    private void verifyPowerDistribution(PlannedProductionBatch batch) {
        try {
            double totalPowerRequired = calculateTotalPowerRequirement(batch);
            double availablePower = getAvailablePower(batch);

            if (totalPowerRequired > availablePower) {
                crossSystemIssues.add(String.format("Power requirement (%.2fW) exceeds available power (%.2fW)",
                        totalPowerRequired, availablePower));
            }
        } catch (Exception e) {
            crossSystemIssues.add("Power distribution verification failed: " + e.getMessage());
        }
    }

    private double calculateTotalPowerRequirement(PlannedProductionBatch batch) {
        if (batch == null || batch.getPCBAStructure() == null) {
            return 0.0;
        }

        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("power"))
                .mapToDouble(this::extractPowerValue)
                .sum();
    }

    private double extractPowerValue(BOMEntry entry) {
        try {
            String powerSpec = entry.getSpecs().get("power");
            if (powerSpec == null || powerSpec.trim().isEmpty()) {
                return 0.0;
            }
            String powerValue = powerSpec.replaceAll("[^0-9.]", "");
            return powerValue.isEmpty() ? 0.0 : Double.parseDouble(powerValue);
        } catch (NumberFormatException e) {
            crossSystemIssues.add("Invalid power specification for " + entry.getMpn() + ": " + e.getMessage());
            return 0.0;
        }
    }

    private double getAvailablePower(PlannedProductionBatch batch) {
        if (batch == null || batch.getPCBAStructure() == null) {
            return 0.0;
        }

        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("power_output"))
                .mapToDouble(this::extractPowerOutputValue)
                .sum();
    }

    private double extractPowerOutputValue(BOMEntry entry) {
        try {
            String powerSpec = entry.getSpecs().getOrDefault("power_output", "0W");
            if (powerSpec == null || powerSpec.trim().isEmpty()) {
                return 0.0;
            }
            String powerValue = powerSpec.replaceAll("[^0-9.]", "");
            return powerValue.isEmpty() ? 0.0 : Double.parseDouble(powerValue);
        } catch (NumberFormatException e) {
            crossSystemIssues.add("Invalid power output specification for " + entry.getMpn() + ": " + e.getMessage());
            return 0.0;
        }
    }

    private void verifyNetworkConnectivity(PlannedProductionBatch batch) {
        if (batch == null) return;

        try {
            verifyNetworkInterfaces(batch);
            verifyNetworkTopology(batch);
            verifyRedundantConnections(batch);
        } catch (Exception e) {
            crossSystemIssues.add("Network connectivity verification failed: " + e.getMessage());
        }
    }

    private void verifyNetworkInterfaces(PlannedProductionBatch batch) {
        boolean hasRequiredInterfaces = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .anyMatch(entry -> {
                    String interfaceType = entry.getSpecs().getOrDefault("interface", "");
                    return interfaceType.contains("Ethernet") || interfaceType.contains("CAN");
                });

        if (!hasRequiredInterfaces) {
            crossSystemIssues.add("Required network interfaces not found");
        }
    }

    private void verifyNetworkTopology(PlannedProductionBatch batch) {
        boolean hasNetworkSwitch = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Network Switch"));

        if (!hasNetworkSwitch) {
            crossSystemIssues.add("Network switch not found in system");
        }
    }

    private void verifyRedundantConnections(PlannedProductionBatch batch) {
        // Implementation for redundant connections verification
    }

    private void verifyEnvironmentalCompliance(PlannedProductionBatch batch) {
        if (batch == null) return;

        try {
            verifyTemperatureRanges(batch);
            verifyPressureRatings(batch);
            verifyWaterproofing(batch);
        } catch (Exception e) {
            crossSystemIssues.add("Environmental compliance verification failed: " + e.getMessage());
        }
    }

    private void verifyTemperatureRanges(PlannedProductionBatch batch) {
        batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("temp_range"))
                .forEach(this::validateTemperatureRange);
    }

    private void validateTemperatureRange(BOMEntry entry) {
        String tempRange = entry.getSpecs().get("temp_range");
        if (!tempRange.contains("-40°C")) {
            crossSystemIssues.add("Component " + entry.getMpn() + " does not meet minimum temperature requirement");
        }
    }

    private void verifyPressureRatings(PlannedProductionBatch batch) {
        batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("pressure_rating"))
                .forEach(this::validatePressureRating);
    }

    private void validatePressureRating(BOMEntry entry) {
        String pressureRating = entry.getSpecs().get("pressure_rating");
        if (!pressureRating.contains("300m")) {
            crossSystemIssues.add("Component " + entry.getMpn() + " does not meet pressure rating requirement");
        }
    }

    private void verifyWaterproofing(PlannedProductionBatch batch) {
        batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("sealing"))
                .forEach(this::validateWaterproofing);
    }

    private void validateWaterproofing(BOMEntry entry) {
        String sealing = entry.getSpecs().get("sealing");
        if (!sealing.contains("IP68") && !sealing.contains("Double O-ring")) {
            crossSystemIssues.add("Component " + entry.getMpn() + " does not meet waterproofing requirement");
        }
    }

    private void verifySafetySystemIntegration(PlannedProductionBatch batch) {
        if (batch == null) return;

        try {
            verifyEmergencyShutdown(batch);
            verifyRedundantSystems(batch);
            verifyMonitoringSystems(batch);
        } catch (Exception e) {
            crossSystemIssues.add("Safety system integration verification failed: " + e.getMessage());
        }
    }

    private void verifyEmergencyShutdown(PlannedProductionBatch batch) {
        boolean hasEmergencyShutdown = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Emergency Shutdown"));

        if (!hasEmergencyShutdown) {
            crossSystemIssues.add("Emergency shutdown system not found");
        }
    }

    private void verifyRedundantSystems(PlannedProductionBatch batch) {
        long redundantProcessors = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().getOrDefault("features", "")
                        .contains("Lock-step"))
                .count();

        if (redundantProcessors < 2) {
            crossSystemIssues.add("Insufficient redundant processors found");
        }
    }

    private void verifyMonitoringSystems(PlannedProductionBatch batch) {
        verifyTemperatureMonitoring(batch);
        verifyVoltageMonitoring(batch);
        verifyCurrentMonitoring(batch);
    }

    private void verifyTemperatureMonitoring(PlannedProductionBatch batch) {
        boolean hasTempMonitor = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Temperature Monitor") &&
                                entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("accuracy"));

        if (!hasTempMonitor) {
            crossSystemIssues.add("Temperature monitoring system not found");
        }
    }

    private void verifyVoltageMonitoring(PlannedProductionBatch batch) {
        boolean hasVoltageMonitor = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Voltage Monitor") &&
                                entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("accuracy"));

        if (!hasVoltageMonitor) {
            crossSystemIssues.add("Voltage monitoring system not found");
        }
    }

    private void verifyCurrentMonitoring(PlannedProductionBatch batch) {
        boolean hasCurrentMonitor = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Current Monitor") &&
                                entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("accuracy"));

        if (!hasCurrentMonitor) {
            crossSystemIssues.add("Current monitoring system not found");
        }
    }
}