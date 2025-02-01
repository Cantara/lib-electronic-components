package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Verification for Propulsion subsystem
 * Responsible for verifying motor control, propeller systems, and associated components
 */
public class PropulsionVerification implements SubsystemVerification {
    private final List<String> issues = new ArrayList<>();

    @Override
    public boolean verifyComponents(PlannedProductionBatch batch) {
        issues.clear();
        boolean valid = true;

        // Verify electronic components
        if (!verifyMotorController(batch)) {
            issues.add("Missing or invalid motor controller");
            valid = false;
        }

        if (!verifyPowerElectronics(batch)) {
            issues.add("Missing or invalid power electronics");
            valid = false;
        }

        if (!verifyFeedbackSensors(batch)) {
            issues.add("Missing or invalid feedback sensors");
            valid = false;
        }

        // Verify mechanical components
        if (!verifyPropulsionMechanics(batch)) {
            issues.add("Missing or invalid propulsion mechanics");
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

        if (!verifyControlInterface(batch)) {
            issues.add("Control interface requirements not met");
            valid = false;
        }

        if (!verifyMechanicalInterface(batch)) {
            issues.add("Mechanical interface requirements not met");
            valid = false;
        }

        if (!verifyWaterproofing(batch)) {
            issues.add("Waterproofing requirements not met");
            valid = false;
        }

        return valid;
    }

    @Override
    public List<String> getVerificationIssues() {
        return Collections.unmodifiableList(issues);
    }

    private boolean verifyMotorController(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("STM32G474VET6") &&
                                verifyControllerSpecs(entry.getSpecs()));
    }

    private boolean verifyControllerSpecs(Map<String, String> specs) {
        return specs.getOrDefault("features", "").contains("HRTIM") &&
                specs.getOrDefault("quality", "").contains("Automotive") &&
                specs.getOrDefault("temp_range", "").contains("-40C");
    }

    private boolean verifyPowerElectronics(PlannedProductionBatch batch) {
        boolean hasGateDriver = verifyGateDriver(batch);
        boolean hasPowerMosfets = verifyPowerMosfets(batch);
        boolean hasProtection = verifyProtectionCircuits(batch);

        if (!hasGateDriver) issues.add("Missing or invalid gate driver");
        if (!hasPowerMosfets) issues.add("Missing or invalid power MOSFETs");
        if (!hasProtection) issues.add("Missing or invalid protection circuits");

        return hasGateDriver && hasPowerMosfets && hasProtection;
    }

    private boolean verifyGateDriver(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("DRV8353RS") &&
                                verifyGateDriverSpecs(entry.getSpecs()));
    }

    private boolean verifyGateDriverSpecs(Map<String, String> specs) {
        return specs.containsKey("voltage") &&
                Integer.parseInt(specs.get("voltage").replaceAll("[^0-9]", "")) >= 100 &&
                specs.getOrDefault("protection", "").contains("OCP") &&
                specs.getOrDefault("protection", "").contains("OTP");
    }

    private boolean verifyPowerMosfets(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("IPT015N10N5") &&
                                verifyMosfetSpecs(entry.getSpecs()));
    }

    private boolean verifyMosfetSpecs(Map<String, String> specs) {
        return specs.containsKey("vds") &&
                Integer.parseInt(specs.get("vds").replaceAll("[^0-9]", "")) >= 100 &&
                specs.containsKey("id") &&
                Integer.parseInt(specs.get("id").replaceAll("[^0-9]", "")) >= 100;
    }

    private boolean verifyProtectionCircuits(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("protection", "")
                                .matches(".*(OCP|OVP|UVP|SCP).*"));
    }

    private boolean verifyFeedbackSensors(PlannedProductionBatch batch) {
        boolean hasPositionSensor = verifyPositionSensor(batch);
        boolean hasCurrentSensor = verifyCurrentSensor(batch);
        boolean hasTemperatureSensor = verifyTemperatureSensor(batch);

        if (!hasPositionSensor) issues.add("Missing or invalid position sensor");
        if (!hasCurrentSensor) issues.add("Missing or invalid current sensor");
        if (!hasTemperatureSensor) issues.add("Missing or invalid temperature sensor");

        return hasPositionSensor && hasCurrentSensor && hasTemperatureSensor;
    }

    private boolean verifyPositionSensor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("AS5047P") &&
                                Integer.parseInt(entry.getSpecs()
                                        .getOrDefault("resolution", "0")
                                        .replaceAll("[^0-9]", "")) >= 14);
    }

    private boolean verifyCurrentSensor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("INA240A3") &&
                                entry.getSpecs().containsKey("bandwidth") &&
                                Integer.parseInt(entry.getSpecs()
                                        .get("bandwidth")
                                        .replaceAll("[^0-9]", "")) >= 400);
    }

    private boolean verifyTemperatureSensor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Temperature") &&
                                entry.getSpecs().containsKey("temp_range"));
    }

    private boolean verifyPropulsionMechanics(PlannedProductionBatch batch) {
        boolean hasPropeller = verifyPropeller(batch);
        boolean hasSeals = verifySeals(batch);
        boolean hasBearings = verifyBearings(batch);

        if (!hasPropeller) issues.add("Missing or invalid propeller assembly");
        if (!hasSeals) issues.add("Missing or invalid shaft seals");
        if (!hasBearings) issues.add("Missing or invalid bearings");

        return hasPropeller && hasSeals && hasBearings;
    }

    private boolean verifyPropeller(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("PROP-400-PRO") &&
                                verifyPropellerSpecs(entry.getSpecs()));
    }

    private boolean verifyPropellerSpecs(Map<String, String> specs) {
        return specs.containsKey("material") &&
                specs.containsKey("diameter") &&
                specs.containsKey("pitch") &&
                specs.containsKey("blades") &&
                Integer.parseInt(specs.getOrDefault("blades", "0")) >= 5;
    }

    private boolean verifySeals(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().contains("TC-25-40-10-CV") &&
                                entry.getSpecs().getOrDefault("type", "").contains("Double Lip"));
    }

    private boolean verifyBearings(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("type", "").contains("Ceramic Hybrid") ||
                                entry.getDescription().contains("Ceramic Hybrid Bearing"));
    }

    private boolean verifyPowerInterface(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("voltage_rating"))
                .anyMatch(entry ->
                        Integer.parseInt(entry.getSpecs()
                                .get("voltage_rating")
                                .replaceAll("[^0-9]", "")) >= 100);
    }

    private boolean verifyControlInterface(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                (entry.getSpecs().get("interface").contains("CAN") ||
                                        entry.getSpecs().get("interface").contains("RS-485")));
    }

    private boolean verifyMechanicalInterface(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("mount_type") &&
                                entry.getSpecs().get("mount_type").equals("DIN Rail") &&
                                entry.getSpecs().containsKey("mount_standard") &&
                                entry.getSpecs().get("mount_standard").equals("EN 60715"));
    }

    private boolean verifyWaterproofing(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing") ||
                        entry.getDescription().contains("Connector"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("sealing") &&
                                (entry.getSpecs().get("sealing").contains("IP68") ||
                                        entry.getSpecs().get("sealing").contains("Double O-ring")));
    }
}