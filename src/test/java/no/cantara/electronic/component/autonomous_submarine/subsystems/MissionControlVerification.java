package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Verification for Mission Control subsystem
 * Responsible for verifying main control processor, safety systems,
 * real-time processing, and system monitoring
 */
public class MissionControlVerification implements SubsystemVerification {
    private final List<String> issues = new ArrayList<>();

    private static final int MIN_PROCESSOR_SPEED_MHZ = 1000;  // 1 GHz minimum
    private static final int MIN_MEMORY_SIZE_MB = 512;        // 512 MB minimum
    private static final int MIN_STORAGE_SIZE_GB = 128;       // 128 GB minimum

    @Override
    public boolean verifyComponents(PlannedProductionBatch batch) {
        issues.clear();
        boolean valid = true;

        if (!verifyProcessingSystems(batch)) {
            issues.add("Missing or invalid processing systems");
            valid = false;
        }

        if (!verifySafetySystems(batch)) {
            issues.add("Missing or invalid safety systems");
            valid = false;
        }

        if (!verifyMemorySystems(batch)) {
            issues.add("Missing or invalid memory systems");
            valid = false;
        }

        if (!verifyMonitoringSystems(batch)) {
            issues.add("Missing or invalid monitoring systems");
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean verifyIntegration(PlannedProductionBatch batch) {
        boolean valid = true;

        if (!verifyRedundancy(batch)) {
            issues.add("Redundancy requirements not met");
            valid = false;
        }

        if (!verifyThermalManagement(batch)) {
            issues.add("Thermal management requirements not met");
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

    private boolean verifyProcessingSystems(PlannedProductionBatch batch) {
        return verifyMainProcessor(batch) &&
                verifySafetyProcessor(batch) &&
                verifyRealTimeProcessor(batch);
    }

    private boolean verifyMainProcessor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("i.MX8QuadXPlus") &&
                                verifyMainProcessorSpecs(entry.getSpecs()));
    }

    private boolean verifyMainProcessorSpecs(Map<String, String> specs) {
        try {
            int speed = Integer.parseInt(specs.getOrDefault("speed", "0MHz")
                    .replaceAll("[^0-9]", ""));
            return speed >= MIN_PROCESSOR_SPEED_MHZ &&
                    specs.getOrDefault("features", "").contains("ECC Memory") &&
                    specs.getOrDefault("features", "").contains("Lockstep") &&
                    specs.getOrDefault("quality", "").contains("Automotive");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifySafetyProcessor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("TMS570LC4357BZWTQQ1") &&
                                verifySafetyProcessorSpecs(entry.getSpecs()));
    }

    private boolean verifySafetyProcessorSpecs(Map<String, String> specs) {
        return specs.getOrDefault("features", "").contains("Lock-step") &&
                specs.getOrDefault("certification", "").contains("SIL 3") &&
                specs.getOrDefault("core", "").contains("Dual");
    }

    private boolean verifyRealTimeProcessor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("MPC5744P") &&
                                verifyRealTimeProcessorSpecs(entry.getSpecs()));
    }

    private boolean verifyRealTimeProcessorSpecs(Map<String, String> specs) {
        return specs.getOrDefault("features", "").contains("Real-Time OS") &&
                specs.getOrDefault("features", "").contains("AUTOSAR") &&
                specs.getOrDefault("quality", "").contains("Automotive");
    }

    private boolean verifySafetySystems(PlannedProductionBatch batch) {
        return verifyWatchdogTimer(batch) &&
                verifyVoltageMonitor(batch) &&
                verifyEmergencyShutdown(batch);
    }

    private boolean verifyWatchdogTimer(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("MAX6369KA+T") &&
                                verifyWatchdogSpecs(entry.getSpecs()));
    }

    private boolean verifyWatchdogSpecs(Map<String, String> specs) {
        return specs.containsKey("timeout") &&
                specs.getOrDefault("window", "").contains("Windowed") &&
                specs.getOrDefault("reset", "").contains("Push-Pull");
    }

    private boolean verifyVoltageMonitor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Voltage Monitor") &&
                                entry.getSpecs().containsKey("accuracy"));
    }

    private boolean verifyEmergencyShutdown(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Emergency Shutdown") ||
                                entry.getSpecs().getOrDefault("features", "")
                                        .contains("Emergency Shutdown"));
    }

    private boolean verifyMemorySystems(PlannedProductionBatch batch) {
        return verifySystemMemory(batch) &&
                verifyNonVolatileStorage(batch);
    }

    private boolean verifySystemMemory(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("MT41K512M16HA-125") &&
                                verifyMemorySpecs(entry.getSpecs()));
    }

    private boolean verifyMemorySpecs(Map<String, String> specs) {
        try {
            String sizeStr = specs.getOrDefault("size", "0")
                    .replaceAll("[^0-9]", "");
            int size = Integer.parseInt(sizeStr);
            return size >= MIN_MEMORY_SIZE_MB &&
                    specs.getOrDefault("features", "").contains("ECC");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifyNonVolatileStorage(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("SSDPE21K375GA01") &&
                                verifyStorageSpecs(entry.getSpecs()));
    }

    private boolean verifyStorageSpecs(Map<String, String> specs) {
        try {
            String capacityStr = specs.getOrDefault("capacity", "0")
                    .replaceAll("[^0-9]", "");
            int capacity = Integer.parseInt(capacityStr);
            return capacity >= MIN_STORAGE_SIZE_GB &&
                    specs.getOrDefault("features", "").contains("Power Loss Protection");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifyMonitoringSystems(PlannedProductionBatch batch) {
        return verifyTemperatureMonitoring(batch) &&
                verifyPowerMonitoring(batch) &&
                verifySystemHealth(batch);
    }

    private boolean verifyTemperatureMonitoring(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Temperature"))
                .count() >= 3; // At least 3 temperature monitoring points
    }

    private boolean verifyPowerMonitoring(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Power Monitor") &&
                                entry.getSpecs().containsKey("accuracy"));
    }

    private boolean verifySystemHealth(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Diagnostics"));
    }

    private boolean verifyRedundancy(PlannedProductionBatch batch) {
        return verifyProcessorRedundancy(batch) &&
                verifyMemoryRedundancy(batch) &&
                verifyStorageRedundancy(batch);
    }

    private boolean verifyProcessorRedundancy(PlannedProductionBatch batch) {
        long redundantProcessors = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Lock-step"))
                .count();
        return redundantProcessors >= 2;
    }

    private boolean verifyMemoryRedundancy(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Memory"))
                .allMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("ECC"));
    }

    private boolean verifyStorageRedundancy(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Storage"))
                .allMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Power Loss Protection"));
    }

    private boolean verifyThermalManagement(PlannedProductionBatch batch) {
        return verifyThermalSolution(batch) &&
                verifyThermalMonitoring(batch) &&
                verifyThermalProtection(batch);
    }

    private boolean verifyThermalSolution(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("heat_dissipation") ||
                                entry.getSpecs().containsKey("thermal_conductivity"));
    }

    private boolean verifyThermalMonitoring(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Temperature"))
                .count() >= 2;
    }

    private boolean verifyThermalProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("protection", "")
                                .contains("OTP"));
    }

    private boolean verifySystemInterfaces(PlannedProductionBatch batch) {
        return verifyNetworkInterfaces(batch) &&
                verifyControlInterfaces(batch) &&
                verifyDebugInterfaces(batch);
    }

    private boolean verifyNetworkInterfaces(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                entry.getSpecs().get("interface").contains("Ethernet"));
    }

    private boolean verifyControlInterfaces(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                entry.getSpecs().get("interface").contains("CAN"));
    }

    private boolean verifyDebugInterfaces(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                (entry.getSpecs().get("interface").contains("JTAG") ||
                                        entry.getSpecs().get("interface").contains("SWD")));
    }

    private boolean verifyEnvironmentalProtection(PlannedProductionBatch batch) {
        return verifyEnclosureProtection(batch) &&
                verifyConnectorProtection(batch) &&
                verifyMaterialCompatibility(batch);
    }

    private boolean verifyEnclosureProtection(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("pressure_rating") &&
                                entry.getSpecs().get("pressure_rating").contains("300m"));
    }

    private boolean verifyConnectorProtection(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Connector"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("sealing") &&
                                entry.getSpecs().get("sealing").contains("IP68"));
    }

    private boolean verifyMaterialCompatibility(PlannedProductionBatch batch) {
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