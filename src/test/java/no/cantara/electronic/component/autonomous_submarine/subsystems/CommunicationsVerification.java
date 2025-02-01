package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Verification for Communications subsystem
 * Responsible for verifying acoustic modem, RF communications, internal networking,
 * and emergency beacon systems
 */
public class CommunicationsVerification implements SubsystemVerification {
    private final List<String> issues = new ArrayList<>();

    @Override
    public boolean verifyComponents(PlannedProductionBatch batch) {
        issues.clear();
        boolean valid = true;

        // Verify acoustic communications
        if (!verifyAcousticSystem(batch)) {
            issues.add("Missing or invalid acoustic communication system");
            valid = false;
        }

        // Verify RF communications
        if (!verifyRfSystem(batch)) {
            issues.add("Missing or invalid RF communication system");
            valid = false;
        }

        // Verify internal networking
        if (!verifyNetworking(batch)) {
            issues.add("Missing or invalid networking system");
            valid = false;
        }

        // Verify emergency beacon
        if (!verifyEmergencyBeacon(batch)) {
            issues.add("Missing or invalid emergency beacon system");
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean verifyIntegration(PlannedProductionBatch batch) {
        boolean valid = true;

        if (!verifyPowerRequirements(batch)) {
            issues.add("Power requirements not met");
            valid = false;
        }

        if (!verifyEnvironmentalProtection(batch)) {
            issues.add("Environmental protection requirements not met");
            valid = false;
        }

        if (!verifySystemInterfaces(batch)) {
            issues.add("System interface requirements not met");
            valid = false;
        }

        if (!verifyAntennaSystems(batch)) {
            issues.add("Antenna system requirements not met");
            valid = false;
        }

        return valid;
    }

    @Override
    public List<String> getVerificationIssues() {
        return Collections.unmodifiableList(issues);
    }

    private boolean verifyAcousticSystem(PlannedProductionBatch batch) {
        return verifyAcousticProcessor(batch) &&
                verifyAcousticTransducer(batch) &&
                verifySignalConditioning(batch);
    }

    private boolean verifyAcousticProcessor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("TMS320C5535AZCHA") &&
                                verifyDspSpecs(entry.getSpecs()));
    }

    private boolean verifyDspSpecs(Map<String, String> specs) {
        return specs.getOrDefault("architecture", "").contains("C55x") &&
                specs.getOrDefault("features", "").contains("FFT") &&
                specs.containsKey("memory");
    }

    private boolean verifyAcousticTransducer(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("AT12ET") &&
                                verifyTransducerSpecs(entry.getSpecs()));
    }

    private boolean verifyTransducerSpecs(Map<String, String> specs) {
        return specs.containsKey("frequency") &&
                specs.containsKey("beam_width") &&
                specs.containsKey("depth_rating") &&
                specs.get("depth_rating").contains("300m");
    }

    private boolean verifySignalConditioning(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("ADS131E08IPAG") &&
                                verifyAdcSpecs(entry.getSpecs()));
    }

    private boolean verifyAdcSpecs(Map<String, String> specs) {
        return specs.containsKey("channels") &&
                specs.containsKey("resolution") &&
                specs.get("resolution").contains("24-bit");
    }

    private boolean verifyRfSystem(PlannedProductionBatch batch) {
        return verifyRfTransceiver(batch) && verifyRfAmplifier(batch);
    }

    private boolean verifyRfTransceiver(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("CC1200RHBR") &&
                                verifyRfTransceiverSpecs(entry.getSpecs()));
    }

    private boolean verifyRfTransceiverSpecs(Map<String, String> specs) {
        try {
            float sensitivity = Float.parseFloat(specs.getOrDefault("sensitivity", "0")
                    .replaceAll("[^0-9.-]", ""));
            return sensitivity <= -120 &&
                    specs.containsKey("modulation") &&
                    specs.containsKey("output_power");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verifyRfAmplifier(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("PD55003") &&
                                verifyAmplifierSpecs(entry.getSpecs()));
    }

    private boolean verifyAmplifierSpecs(Map<String, String> specs) {
        return specs.containsKey("gain") &&
                specs.containsKey("frequency") &&
                specs.containsKey("power");
    }

    private boolean verifyNetworking(PlannedProductionBatch batch) {
        return verifyEthernetPhy(batch) && verifyNetworkSwitch(batch);
    }

    private boolean verifyEthernetPhy(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("DP83825IRHBR") &&
                                verifyPhySpecs(entry.getSpecs()));
    }

    private boolean verifyPhySpecs(Map<String, String> specs) {
        return specs.containsKey("speed") &&
                specs.get("speed").contains("100") &&
                specs.getOrDefault("quality", "").contains("Industrial");
    }

    private boolean verifyNetworkSwitch(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("KSZ8895MQXCA") &&
                                verifySwichSpecs(entry.getSpecs()));
    }

    private boolean verifySwichSpecs(Map<String, String> specs) {
        return specs.containsKey("ports") &&
                specs.getOrDefault("features", "").contains("QoS") &&
                specs.getOrDefault("features", "").contains("VLAN");
    }

    private boolean verifyEmergencyBeacon(PlannedProductionBatch batch) {
        return verifyBeaconProcessor(batch) && verifyGpsModule(batch);
    }

    private boolean verifyBeaconProcessor(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("STM32L412K8U6") &&
                                verifyBeaconProcessorSpecs(entry.getSpecs()));
    }

    private boolean verifyBeaconProcessorSpecs(Map<String, String> specs) {
        return specs.getOrDefault("features", "").contains("Low Power") &&
                specs.containsKey("flash");
    }

    private boolean verifyGpsModule(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().equals("NEO-M9N") &&
                                verifyGpsSpecs(entry.getSpecs()));
    }

    private boolean verifyGpsSpecs(Map<String, String> specs) {
        return specs.getOrDefault("systems", "").contains("GPS") &&
                specs.containsKey("sensitivity") &&
                specs.containsKey("accuracy");
    }

    private boolean verifyPowerRequirements(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("power"))
                .mapToDouble(entry -> {
                    String power = entry.getSpecs().get("power")
                            .replaceAll("[^0-9.]", "");
                    return Double.parseDouble(power);
                })
                .sum() <= 50; // Maximum power budget
    }

    private boolean verifyEnvironmentalProtection(PlannedProductionBatch batch) {
        return verifyHousingProtection(batch) && verifyWindowMaterials(batch);
    }

    private boolean verifyHousingProtection(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing"))
                .allMatch(entry ->
                        entry.getSpecs().containsKey("pressure_rating") &&
                                entry.getSpecs().get("pressure_rating").contains("300m"));
    }

    private boolean verifyWindowMaterials(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs().containsKey("rf_windows") &&
                                entry.getSpecs().get("rf_windows").contains("Composite"));
    }

    private boolean verifySystemInterfaces(PlannedProductionBatch batch) {
        return verifyDataInterfaces(batch) && verifyPowerInterfaces(batch);
    }

    private boolean verifyDataInterfaces(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("interface"))
                .anyMatch(entry -> {
                    String interface_ = entry.getSpecs().get("interface");
                    return interface_.contains("Ethernet") ||
                            interface_.contains("CAN") ||
                            interface_.contains("RS-485");
                });
    }

    private boolean verifyPowerInterfaces(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs().containsKey("voltage"))
                .allMatch(entry -> {
                    String voltage = entry.getSpecs().get("voltage");
                    return voltage.contains("3.3V") ||
                            voltage.contains("5V") ||
                            voltage.contains("12V");
                });
    }

    private boolean verifyAntennaSystems(PlannedProductionBatch batch) {
        return verifyRfAntenna(batch) && verifyGpsAntenna(batch);
    }

    private boolean verifyRfAntenna(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getMpn().contains("COM-600-ANT") &&
                                verifyRfAntennaSpecs(entry.getSpecs()));
    }

    private boolean verifyRfAntennaSpecs(Map<String, String> specs) {
        return specs.containsKey("frequency") &&
                specs.containsKey("gain") &&
                specs.containsKey("pattern") &&
                specs.containsKey("depth_rating");
    }

    private boolean verifyGpsAntenna(PlannedProductionBatch batch) {
        return batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("GPS Antenna") &&
                                verifyGpsAntennaSpecs(entry.getSpecs()));
    }

    private boolean verifyGpsAntennaSpecs(Map<String, String> specs) {
        return specs.containsKey("frequency") &&
                specs.containsKey("gain") &&
                specs.containsKey("depth_rating");
    }
}