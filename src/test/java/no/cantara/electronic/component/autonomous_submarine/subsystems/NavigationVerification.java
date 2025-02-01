package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Verification for Navigation Control subsystem
 */
public class NavigationVerification implements SubsystemVerification {
    private final List<String> issues = new ArrayList<>();

    @Override
    public boolean verifyComponents(PlannedProductionBatch batch) {
        issues.clear();
        boolean valid = true;

        if (!verifyProcessors(batch)) {
            issues.add("Missing or invalid navigation processors");
            valid = false;
        }

        if (!verifySensors(batch)) {
            issues.add("Missing or invalid navigation sensors");
            valid = false;
        }

        if (!verifyNonVolatileStorage(batch)) {
            issues.add("Missing or invalid non-volatile storage");
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean verifyIntegration(PlannedProductionBatch batch) {
        boolean valid = true;

        if (!verifyPowerInterface(batch)) {
            issues.add("Power interface requirements not met");
            valid = false;
        }

        if (!verifyDataInterface(batch)) {
            issues.add("Data interface requirements not met");
            valid = false;
        }

        if (!verifyMechanicalInterface(batch)) {
            issues.add("Mechanical interface requirements not met");
            valid = false;
        }

        return valid;
    }

    @Override
    public List<String> getVerificationIssues() {
        return Collections.unmodifiableList(issues);
    }

    private boolean verifyProcessors(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("STM32H755ZIT6") &&
                                verifyProcessorSpecs(entry.getSpecs()));
    }

    private boolean verifyProcessorSpecs(Map<String, String> specs) {
        return specs.getOrDefault("core", "").contains("Cortex-M7") &&
                specs.getOrDefault("speed", "0").compareTo("400MHz") > 0 &&
                specs.getOrDefault("quality", "").contains("Automotive");
    }

    private boolean verifySensors(PlannedProductionBatch batch) {
        boolean hasImu = verifyImu(batch);
        boolean hasDepthSensor = verifyDepthSensorSpecs(batch);
        boolean hasPositionSensor = verifyPositionSensorSpecs(batch);

        if (!hasImu) issues.add("Missing or invalid IMU");
        if (!hasDepthSensor) issues.add("Missing or invalid depth sensor");
        if (!hasPositionSensor) issues.add("Missing or invalid position sensor");

        return hasImu && hasDepthSensor && hasPositionSensor;
    }

    private boolean verifyImu(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("BMX055") &&
                                verifyImuSpecs(entry.getSpecs()));
    }

    private boolean verifyImuSpecs(Map<String, String> specs) {
        String sensors = specs.getOrDefault("sensors", "");
        return sensors.contains("Accelerometer") &&
                sensors.contains("Gyroscope") &&
                sensors.contains("Magnetometer");
    }

    private boolean verifyDepthSensorSpecs(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("MS5837-30BA") &&
                                entry.getSpecs().getOrDefault("pressure_range", "")
                                        .contains("30 bar"));
    }

    private boolean verifyPositionSensorSpecs(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Position") ||
                                entry.getDescription().contains("GNSS") ||
                                entry.getDescription().contains("GPS"));
    }

    private boolean verifyNonVolatileStorage(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        (entry.getDescription().contains("FRAM") ||
                                entry.getDescription().contains("Flash")) &&
                                verifyStorageSpecs(entry.getSpecs()));
    }

    private boolean verifyStorageSpecs(Map<String, String> specs) {
        return specs.containsKey("size") &&
                specs.containsKey("interface") &&
                (specs.get("interface").contains("SPI") ||
                        specs.get("interface").contains("I2C"));
    }

    private boolean verifyPowerInterface(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Power"))
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("voltage") &&
                            (specs.get("voltage").contains("3.3V") ||
                                    specs.get("voltage").contains("5V"));
                });
    }

    private boolean verifyDataInterface(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("interface") &&
                            (specs.get("interface").contains("CAN") ||
                                    specs.get("interface").contains("Ethernet"));
                });
    }

    private boolean verifyMechanicalInterface(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing"))
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("mount_type") &&
                            specs.get("mount_type").equals("DIN Rail") &&
                            specs.containsKey("mount_standard") &&
                            specs.get("mount_standard").equals("EN 60715");
                });
    }
}