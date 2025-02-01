package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Verification for Power Management subsystem
 * Responsible for verifying battery management, power distribution, protection systems,
 * and monitoring capabilities
 */
public class PowerManagementVerification implements SubsystemVerification {
    private final List<String> issues = new ArrayList<>();

    @Override
    public boolean verifyComponents(PlannedProductionBatch batch) {
        issues.clear();
        boolean valid = true;

        // Verify battery management
        if (!verifyBatteryManagement(batch)) {
            issues.add("Missing or invalid battery management system");
            valid = false;
        }

        // Verify power distribution
        if (!verifyPowerDistribution(batch)) {
            issues.add("Missing or invalid power distribution system");
            valid = false;
        }

        // Verify protection systems
        if (!verifyProtectionSystems(batch)) {
            issues.add("Missing or invalid protection systems");
            valid = false;
        }

        // Verify monitoring systems
        if (!verifyMonitoringSystems(batch)) {
            issues.add("Missing or invalid monitoring systems");
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean verifyIntegration(PlannedProductionBatch batch) {
        boolean valid = true;

        if (!verifyThermalManagement(batch)) {
            issues.add("Thermal management requirements not met");
            valid = false;
        }

        if (!verifyLoadDistribution(batch)) {
            issues.add("Load distribution requirements not met");
            valid = false;
        }

        if (!verifySystemInterfaces(batch)) {
            issues.add("System interface requirements not met");
            valid = false;
        }

        if (!verifyEnvironmentalProtection(batch)) {
            issues.add("Environmental protection requirements not met");
            valid = false;
        }

        return valid;
    }

    @Override
    public List<String> getVerificationIssues() {
        return Collections.unmodifiableList(issues);
    }

    private boolean verifyBatteryManagement(PlannedProductionBatch batch) {
        return verifyBmsController(batch) &&
                verifyBmsMonitoring(batch) &&
                verifyBalancing(batch);
    }

    private boolean verifyBmsController(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("BQ76952PFBR") &&
                                verifyBmsSpecs(entry.getSpecs()));
    }

    private boolean verifyBmsSpecs(Map<String, String> specs) {
        return specs.getOrDefault("cells", "0").contains("16S") &&
                specs.getOrDefault("features", "").contains("Cell Balancing") &&
                specs.getOrDefault("protection", "").matches(".*(OVP|UVP|OCP|SCP).*");
    }

    private boolean verifyBmsMonitoring(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Current Monitor") &&
                                verifyCurrrentMonitorSpecs(entry.getSpecs()));
    }

    private boolean verifyCurrrentMonitorSpecs(Map<String, String> specs) {
        try {
            int current = Integer.parseInt(specs.getOrDefault("current", "0")
                    .replaceAll("[^0-9]", ""));
            return current >= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifyBalancing(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Cell Balancing"));
    }

    private boolean verifyPowerDistribution(PlannedProductionBatch batch) {
        return verifyMainConverter(batch) &&
                verifySecondaryConverters(batch) &&
                verifyDistributionSwitches(batch);
    }

    private boolean verifyMainConverter(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("LTC4015") &&
                                verifyMainConverterSpecs(entry.getSpecs()));
    }

    private boolean verifyMainConverterSpecs(Map<String, String> specs) {
        try {
            int current = Integer.parseInt(specs.getOrDefault("current", "0")
                    .replaceAll("[^0-9]", ""));
            int efficiency = Integer.parseInt(specs.getOrDefault("efficiency", "0%")
                    .replaceAll("[^0-9]", ""));
            return current >= 20 && efficiency >= 95;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifySecondaryConverters(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("DC/DC"))
                .count() >= 2;
    }

    private boolean verifyDistributionSwitches(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("TPS1H100-Q1") &&
                                verifyDistributionSwitchSpecs(entry.getSpecs()));
    }

    private boolean verifyDistributionSwitchSpecs(Map<String, String> specs) {
        try {
            int current = Integer.parseInt(specs.getOrDefault("current", "0")
                    .replaceAll("[^0-9]", ""));
            return current >= 30 &&
                    specs.getOrDefault("protection", "").contains("OCP") &&
                    specs.getOrDefault("features", "").contains("Current Sensing");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifyProtectionSystems(PlannedProductionBatch batch) {
        boolean hasOvervoltage = verifyOvervoltageProtection(batch);
        boolean hasUndervoltage = verifyUndervoltageProtection(batch);
        boolean hasOvercurrent = verifyOvercurrentProtection(batch);
        boolean hasShortCircuit = verifyShortCircuitProtection(batch);

        if (!hasOvervoltage) issues.add("Missing overvoltage protection");
        if (!hasUndervoltage) issues.add("Missing undervoltage protection");
        if (!hasOvercurrent) issues.add("Missing overcurrent protection");
        if (!hasShortCircuit) issues.add("Missing short-circuit protection");

        return hasOvervoltage && hasUndervoltage && hasOvercurrent && hasShortCircuit;
    }

    private boolean verifyOvervoltageProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("protection", "").contains("OVP"));
    }

    private boolean verifyUndervoltageProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("protection", "").contains("UVP"));
    }

    private boolean verifyOvercurrentProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("protection", "").contains("OCP"));
    }

    private boolean verifyShortCircuitProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("protection", "").contains("SCP"));
    }

    private boolean verifyMonitoringSystems(PlannedProductionBatch batch) {
        boolean hasVoltageMonitoring = verifyVoltageMonitoring(batch);
        boolean hasCurrentMonitoring = verifyCurrentMonitoring(batch);
        boolean hasTemperatureMonitoring = verifyTemperatureMonitoring(batch);
        boolean hasTelemetry = verifyTelemetry(batch);

        if (!hasVoltageMonitoring) issues.add("Missing voltage monitoring");
        if (!hasCurrentMonitoring) issues.add("Missing current monitoring");
        if (!hasTemperatureMonitoring) issues.add("Missing temperature monitoring");
        if (!hasTelemetry) issues.add("Missing telemetry capabilities");

        return hasVoltageMonitoring && hasCurrentMonitoring &&
                hasTemperatureMonitoring && hasTelemetry;
    }

    private boolean verifyVoltageMonitoring(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Voltage Monitor") ||
                                entry.getSpecs().getOrDefault("features", "")
                                        .contains("Voltage Monitoring"));
    }

    private boolean verifyCurrentMonitoring(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Current Monitor") ||
                                entry.getSpecs().getOrDefault("features", "")
                                        .contains("Current Monitoring"));
    }

    private boolean verifyTemperatureMonitoring(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Temperature") &&
                                entry.getSpecs().containsKey("temp_range"));
    }

    private boolean verifyTelemetry(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                (entry.getSpecs().get("interface").contains("I2C") ||
                                        entry.getSpecs().get("interface").contains("SPI")));
    }

    private boolean verifyThermalManagement(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("cooling") &&
                                entry.getSpecs().get("cooling").contains("Liquid-Cooled"));
    }

    private boolean verifyLoadDistribution(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Power") &&
                        entry.getSpecs().containsKey("current"))
                .mapToInt(entry -> Integer.parseInt(entry.getSpecs()
                        .get("current").replaceAll("[^0-9]", "")))
                .sum() >= 50; // Minimum total current capacity
    }

    private boolean verifySystemInterfaces(PlannedProductionBatch batch) {
        return verifyPowerInterfaces(batch) && verifyControlInterfaces(batch);
    }

    private boolean verifyPowerInterfaces(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Power Connector"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("current_rating") &&
                                entry.getSpecs().containsKey("voltage_rating") &&
                                entry.getSpecs().containsKey("sealing"));
    }

    private boolean verifyControlInterfaces(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                (entry.getSpecs().get("interface").contains("CAN") ||
                                        entry.getSpecs().get("interface").contains("I2C")));
    }

    private boolean verifyEnvironmentalProtection(PlannedProductionBatch batch) {
        return verifyWaterproofing(batch) && verifyCorrosionProtection(batch);
    }

    private boolean verifyWaterproofing(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing") ||
                        entry.getDescription().contains("Connector"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("sealing") &&
                                entry.getSpecs().get("sealing").contains("IP68"));
    }

    private boolean verifyCorrosionProtection(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("material"))
                .allMatch(entry -> {
                    String material = entry.getSpecs().get("material");
                    return material.contains("Titanium") ||
                            material.contains("316L");
                });
    }
}