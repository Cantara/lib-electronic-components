package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Verification for Sensor Suite subsystem
 * Responsible for verifying sonar systems, water quality sensors,
 * imaging systems, and environmental sensors
 */
public class SensorSuiteVerification implements SubsystemVerification {
    private final List<String> issues = new ArrayList<>();

    @Override
    public boolean verifyComponents(PlannedProductionBatch batch) {
        issues.clear();
        boolean valid = true;

        if (!verifySonarSystem(batch)) {
            issues.add("Missing or invalid sonar system");
            valid = false;
        }

        if (!verifyWaterQualitySensors(batch)) {
            issues.add("Missing or invalid water quality sensors");
            valid = false;
        }

        if (!verifyImagingSystem(batch)) {
            issues.add("Missing or invalid imaging system");
            valid = false;
        }

        if (!verifyEnvironmentalSensors(batch)) {
            issues.add("Missing or invalid environmental sensors");
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean verifyIntegration(PlannedProductionBatch batch) {
        boolean valid = true;

        if (!verifyDataAcquisition(batch)) {
            issues.add("Data acquisition requirements not met");
            valid = false;
        }

        if (!verifySensorInterfaces(batch)) {
            issues.add("Sensor interface requirements not met");
            valid = false;
        }

        if (!verifyEnvironmentalProtection(batch)) {
            issues.add("Environmental protection requirements not met");
            valid = false;
        }

        if (!verifyCalibrationCapabilities(batch)) {
            issues.add("Calibration capabilities requirements not met");
            valid = false;
        }

        return valid;
    }

    @Override
    public List<String> getVerificationIssues() {
        return Collections.unmodifiableList(issues);
    }

    private boolean verifySonarSystem(PlannedProductionBatch batch) {
        boolean hasSonarProcessor = verifySonarProcessor(batch);
        boolean hasSonarArray = verifySonarArray(batch);
        boolean hasSonarInterface = verifySonarInterface(batch);

        if (!hasSonarProcessor) issues.add("Missing or invalid sonar processor");
        if (!hasSonarArray) issues.add("Missing or invalid sonar array");
        if (!hasSonarInterface) issues.add("Missing or invalid sonar interface");

        return hasSonarProcessor && hasSonarArray && hasSonarInterface;
    }

    private boolean verifySonarProcessor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("TMS320C6748AGZK") &&
                                verifySonarProcessorSpecs(entry.getSpecs()));
    }

    private boolean verifySonarProcessorSpecs(Map<String, String> specs) {
        return specs.getOrDefault("core", "").contains("C674x") &&
                specs.getOrDefault("features", "").contains("FFT") &&
                specs.containsKey("memory");
    }

    private boolean verifySonarArray(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("SENS-700-SON") &&
                                verifySonarArraySpecs(entry.getSpecs()));
    }

    private boolean verifySonarArraySpecs(Map<String, String> specs) {
        try {
            int elements = Integer.parseInt(specs.getOrDefault("elements", "0"));
            return elements >= 32 &&
                    specs.containsKey("frequency") &&
                    specs.containsKey("beam_width") &&
                    specs.containsKey("range");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifySonarInterface(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("AD9269BBPZ-40") &&
                                verifySonarInterfaceSpecs(entry.getSpecs()));
    }

    private boolean verifySonarInterfaceSpecs(Map<String, String> specs) {
        return specs.containsKey("channels") &&
                specs.containsKey("resolution") &&
                specs.containsKey("sample_rate");
    }

    private boolean verifyWaterQualitySensors(PlannedProductionBatch batch) {
        boolean hasPhSensor = verifyPhSensor(batch);
        boolean hasDoSensor = verifyDissolvedOxygenSensor(batch);
        boolean hasConductivitySensor = verifyConductivitySensor(batch);
        boolean hasTurbidity = verifyTurbiditySensor(batch);

        if (!hasPhSensor) issues.add("Missing or invalid pH sensor");
        if (!hasDoSensor) issues.add("Missing or invalid dissolved oxygen sensor");
        if (!hasConductivitySensor) issues.add("Missing or invalid conductivity sensor");
        if (!hasTurbidity) issues.add("Missing or invalid turbidity sensor");

        return hasPhSensor && hasDoSensor && hasConductivitySensor && hasTurbidity;
    }

    private boolean verifyPhSensor(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getSpecs().getOrDefault("parameters", "").contains("pH") &&
                        entry.getSpecs().containsKey("accuracy_ph"));
    }

    private boolean verifyDissolvedOxygenSensor(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getSpecs().getOrDefault("parameters", "").contains("DO") &&
                        entry.getSpecs().containsKey("accuracy_do"));
    }

    private boolean verifyConductivitySensor(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getSpecs().getOrDefault("parameters", "")
                        .contains("Conductivity"));
    }

    private boolean verifyTurbiditySensor(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getSpecs().getOrDefault("parameters", "")
                        .contains("Turbidity"));
    }

    private boolean verifyImagingSystem(PlannedProductionBatch batch) {
        return verifyCamera(batch) &&
                verifyLighting(batch) &&
                verifyImageProcessor(batch);
    }

    private boolean verifyCamera(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("UI-5261SE-C-HQ") &&
                                verifyCameraSpecs(entry.getSpecs()));
    }

    private boolean verifyCameraSpecs(Map<String, String> specs) {
        return specs.containsKey("sensor") &&
                specs.containsKey("resolution") &&
                specs.containsKey("frame_rate") &&
                specs.getOrDefault("features", "").contains("Global Shutter");
    }

    private boolean verifyLighting(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("LED") &&
                                verifyLightingSpecs(entry.getSpecs()));
    }

    private boolean verifyLightingSpecs(Map<String, String> specs) {
        try {
            int power = Integer.parseInt(specs.getOrDefault("power", "0W")
                    .replaceAll("[^0-9]", ""));
            return power >= 50 &&
                    specs.containsKey("beam_angle") &&
                    specs.containsKey("color_temp");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifyImageProcessor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("TW8844-LA1-CR") &&
                                verifyImageProcessorSpecs(entry.getSpecs()));
    }

    private boolean verifyImageProcessorSpecs(Map<String, String> specs) {
        return specs.getOrDefault("resolution", "").contains("1080p") &&
                specs.containsKey("interface") &&
                specs.getOrDefault("features", "").contains("Image Enhancement");
    }

    private boolean verifyEnvironmentalSensors(PlannedProductionBatch batch) {
        boolean hasTemperature = verifyTemperatureSensor(batch);
        boolean hasPressure = verifyPressureSensor(batch);
        boolean hasDepth = verifyDepthSensor(batch);

        if (!hasTemperature) issues.add("Missing or invalid temperature sensor");
        if (!hasPressure) issues.add("Missing or invalid pressure sensor");
        if (!hasDepth) issues.add("Missing or invalid depth sensor");

        return hasTemperature && hasPressure && hasDepth;
    }

    private boolean verifyTemperatureSensor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Temperature") &&
                                entry.getSpecs().containsKey("accuracy"));
    }

    private boolean verifyPressureSensor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Pressure") &&
                                entry.getSpecs().containsKey("pressure_range"));
    }

    private boolean verifyDepthSensor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("MS5837-30BA") &&
                                entry.getSpecs().getOrDefault("pressure_range", "")
                                        .contains("30 bar"));
    }

    private boolean verifyDataAcquisition(PlannedProductionBatch batch) {
        return verifyAnalogAcquisition(batch) &&
                verifyDigitalAcquisition(batch) &&
                verifyDataStorage(batch);
    }

    private boolean verifyAnalogAcquisition(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("ADS1278IPAG") &&
                                verifyAdcSpecs(entry.getSpecs()));
    }

    private boolean verifyAdcSpecs(Map<String, String> specs) {
        return specs.containsKey("channels") &&
                specs.containsKey("resolution") &&
                specs.get("resolution").contains("24-bit");
    }

    private boolean verifyDigitalAcquisition(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                (entry.getSpecs().get("interface").contains("SPI") ||
                                        entry.getSpecs().get("interface").contains("I2C")));
    }

    private boolean verifyDataStorage(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Storage") &&
                                entry.getSpecs().containsKey("capacity"));
    }

    private boolean verifySensorInterfaces(PlannedProductionBatch batch) {
        return verifyAnalogInterfaces(batch) &&
                verifyDigitalInterfaces(batch) &&
                verifyPowerInterfaces(batch);
    }

    private boolean verifyAnalogInterfaces(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Sensor Port"))
                .anyMatch(entry -> entry.getSpecs().containsKey("analog_channels"));
    }

    private boolean verifyDigitalInterfaces(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Sensor Port"))
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("interface") &&
                                (entry.getSpecs().get("interface").contains("RS-485") ||
                                        entry.getSpecs().get("interface").contains("CAN")));
    }

    private boolean verifyPowerInterfaces(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Sensor Port"))
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("power") &&
                                entry.getSpecs().containsKey("voltage"));
    }

    private boolean verifyEnvironmentalProtection(PlannedProductionBatch batch) {
        return verifyHousingProtection(batch) &&
                verifyConnectorProtection(batch) &&
                verifyViewportProtection(batch);
    }

    private boolean verifyHousingProtection(PlannedProductionBatch batch) {
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

    private boolean verifyViewportProtection(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Viewport"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("material") &&
                                entry.getSpecs().get("material").contains("Sapphire"));
    }

    private boolean verifyCalibrationCapabilities(PlannedProductionBatch batch) {
        return verifyCalibrationReference(batch) &&
                verifyCalibrationAccess(batch);
    }

    private boolean verifyCalibrationReference(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Reference") &&
                                entry.getSpecs().containsKey("accuracy"));
    }

    private boolean verifyCalibrationAccess(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Sensor"))
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("calibration") ||
                                entry.getSpecs().containsKey("adjustment"));
    }
}