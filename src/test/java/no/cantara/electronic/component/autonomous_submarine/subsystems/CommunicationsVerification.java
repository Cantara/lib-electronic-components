package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommunicationsVerification implements SubsystemVerification {
    private final List<String> issues = new ArrayList<>();

    @Override
    public boolean verifyComponents(PlannedProductionBatch batch) {
        issues.clear();
        boolean valid = true;

        try {
            if (!verifyAcousticSystem(batch)) {
                issues.add("Missing or invalid acoustic communication system");
                valid = false;
            }
            if (!verifyRfSystem(batch)) {
                issues.add("Missing or invalid RF communication system");
                valid = false;
            }
            if (!verifyNetworking(batch)) {
                issues.add("Missing or invalid networking system");
                valid = false;
            }
            if (!verifyEmergencyBeacon(batch)) {
                issues.add("Missing or invalid emergency beacon system");
                valid = false;
            }
        } catch (Exception e) {
            issues.add("Component verification failed: " + e.getMessage());
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean verifyIntegration(PlannedProductionBatch batch) {
        boolean valid = true;

        try {
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
        } catch (Exception e) {
            issues.add("Integration verification failed: " + e.getMessage());
            valid = false;
        }

        return valid;
    }

    @Override
    public List<String> getVerificationIssues() {
        return Collections.unmodifiableList(issues);
    }

    private boolean verifyAcousticSystem(PlannedProductionBatch batch) {
        try {
            return verifyAcousticProcessor(batch) &&
                    verifyAcousticTransducer(batch) &&
                    verifySignalConditioning(batch);
        } catch (Exception e) {
            issues.add("Acoustic system verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyAcousticProcessor(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getMpn().equals("TMS320C5535AZCHA") &&
                                verifyDspSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Acoustic processor verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyDspSpecs(Map<String, String> specs) {
        if (specs == null) return false;
        try {
            return specs.getOrDefault("architecture", "").contains("C55x") &&
                    specs.getOrDefault("features", "").contains("FFT") &&
                    specs.containsKey("memory");
        } catch (Exception e) {
            issues.add("DSP specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyAcousticTransducer(PlannedProductionBatch batch) {
        try {
            return batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getMpn().equals("AT12ET") &&
                                verifyTransducerSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Acoustic transducer verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyTransducerSpecs(Map<String, String> specs) {
        if (specs == null) return false;
        try {
            return specs.containsKey("frequency") &&
                    specs.containsKey("beam_width") &&
                    specs.containsKey("depth_rating") &&
                    specs.getOrDefault("depth_rating", "").contains("300m");
        } catch (Exception e) {
            issues.add("Transducer specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifySignalConditioning(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getMpn().equals("ADS131E08IPAG") &&
                                verifyAdcSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Signal conditioning verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyAdcSpecs(Map<String, String> specs) {
        if (specs == null) return false;
        try {
            return specs.containsKey("channels") &&
                    specs.containsKey("resolution") &&
                    specs.getOrDefault("resolution", "").contains("24-bit");
        } catch (Exception e) {
            issues.add("ADC specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyRfSystem(PlannedProductionBatch batch) {
        try {
            return verifyRfTransceiver(batch) && verifyRfAmplifier(batch);
        } catch (Exception e) {
            issues.add("RF system verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyRfTransceiver(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getMpn().equals("CC1200RHBR") &&
                                verifyRfTransceiverSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("RF transceiver verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyRfTransceiverSpecs(Map<String, String> specs) {
        if (specs == null) return false;
        try {
            String sensitivityStr = specs.getOrDefault("sensitivity", "0")
                    .replaceAll("[^0-9.-]", "");
            if (sensitivityStr.isEmpty()) return false;
            float sensitivity = Float.parseFloat(sensitivityStr);
            return sensitivity <= -120 &&
                    specs.containsKey("modulation") &&
                    specs.containsKey("output_power");
        } catch (NumberFormatException e) {
            issues.add("Invalid RF sensitivity format: " + e.getMessage());
            return false;
        } catch (Exception e) {
            issues.add("RF transceiver specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyPowerRequirements(PlannedProductionBatch batch) {
        try {
            double totalPower = batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .filter(entry -> entry.getSpecs() != null)
                    .filter(entry -> entry.getSpecs().containsKey("power"))
                    .mapToDouble(entry -> {
                        try {
                            String powerSpec = entry.getSpecs().get("power");
                            if (powerSpec == null || powerSpec.trim().isEmpty()) {
                                return 0.0;
                            }
                            String powerValue = powerSpec.replaceAll("[^0-9.]", "");
                            return powerValue.isEmpty() ? 0.0 : Double.parseDouble(powerValue);
                        } catch (NumberFormatException e) {
                            issues.add("Invalid power specification for " + entry.getMpn() + ": " + e.getMessage());
                            return 0.0;
                        }
                    })
                    .sum();

            return totalPower <= 50.0;
        } catch (Exception e) {
            issues.add("Power requirements verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyEnvironmentalProtection(PlannedProductionBatch batch) {
        try {
            return verifyHousingProtection(batch) && verifyWindowMaterials(batch);
        } catch (Exception e) {
            issues.add("Environmental protection verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyHousingProtection(PlannedProductionBatch batch) {
        try {
            return batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .filter(entry -> entry.getDescription() != null &&
                            entry.getDescription().contains("Housing"))
                    .allMatch(entry -> {
                        Map<String, String> specs = entry.getSpecs();
                        return specs != null &&
                                specs.containsKey("pressure_rating") &&
                                specs.get("pressure_rating").contains("300m");
                    });
        } catch (Exception e) {
            issues.add("Housing protection verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyWindowMaterials(PlannedProductionBatch batch) {
        try {
            return batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        Map<String, String> specs = entry.getSpecs();
                        return specs != null &&
                                specs.containsKey("rf_windows") &&
                                specs.get("rf_windows").contains("Composite");
                    });
        } catch (Exception e) {
            issues.add("Window materials verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifySystemInterfaces(PlannedProductionBatch batch) {
        try {
            return verifyDataInterfaces(batch) && verifyPowerInterfaces(batch);
        } catch (Exception e) {
            issues.add("System interfaces verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyDataInterfaces(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .filter(entry -> entry.getSpecs() != null &&
                            entry.getSpecs().containsKey("interface"))
                    .anyMatch(entry -> {
                        String interface_ = entry.getSpecs().get("interface");
                        return interface_ != null && (
                                interface_.contains("Ethernet") ||
                                        interface_.contains("CAN") ||
                                        interface_.contains("RS-485"));
                    });
        } catch (Exception e) {
            issues.add("Data interfaces verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyPowerInterfaces(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .filter(entry -> entry.getSpecs() != null &&
                            entry.getSpecs().containsKey("voltage"))
                    .allMatch(entry -> {
                        String voltage = entry.getSpecs().get("voltage");
                        return voltage != null && (
                                voltage.contains("3.3V") ||
                                        voltage.contains("5V") ||
                                        voltage.contains("12V"));
                    });
        } catch (Exception e) {
            issues.add("Power interfaces verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyAntennaSystems(PlannedProductionBatch batch) {
        try {
            return verifyRfAntenna(batch) && verifyGpsAntenna(batch);
        } catch (Exception e) {
            issues.add("Antenna systems verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyRfAntenna(PlannedProductionBatch batch) {
        try {
            return batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || !entry.getMpn().contains("COM-600-ANT")) {
                            return false;
                        }
                        Map<String, String> specs = entry.getSpecs();
                        return specs != null &&
                                verifyRfAntennaSpecs(specs);
                    });
        } catch (Exception e) {
            issues.add("RF antenna verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyRfAntennaSpecs(Map<String, String> specs) {
        try {
            return specs.containsKey("frequency") &&
                    specs.containsKey("gain") &&
                    specs.containsKey("pattern") &&
                    specs.containsKey("depth_rating");
        } catch (Exception e) {
            issues.add("RF antenna specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyRfAmplifier(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || !entry.getMpn().equals("PD55003")) {
                            return false;
                        }
                        Map<String, String> specs = entry.getSpecs();
                        return specs != null &&
                                verifyAmplifierSpecs(specs);
                    });
        } catch (Exception e) {
            issues.add("RF amplifier verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyAmplifierSpecs(Map<String, String> specs) {
        try {
            return specs.containsKey("gain") &&
                    specs.containsKey("frequency") &&
                    specs.containsKey("power");
        } catch (Exception e) {
            issues.add("Amplifier specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyGpsAntenna(PlannedProductionBatch batch) {
        try {
            return batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getDescription() == null ||
                                !entry.getDescription().contains("GPS Antenna")) {
                            return false;
                        }
                        Map<String, String> specs = entry.getSpecs();
                        return specs != null &&
                                verifyGpsAntennaSpecs(specs);
                    });
        } catch (Exception e) {
            issues.add("GPS antenna verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyGpsAntennaSpecs(Map<String, String> specs) {
        try {
            return specs.containsKey("frequency") &&
                    specs.containsKey("gain") &&
                    specs.containsKey("depth_rating");
        } catch (Exception e) {
            issues.add("GPS antenna specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyNetworking(PlannedProductionBatch batch) {
        try {
            return verifyNetworkSwitch(batch) && verifyNetworkInterfaces(batch);
        } catch (Exception e) {
            issues.add("Networking verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyNetworkSwitch(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getDescription() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getDescription().contains("Ethernet Switch") &&
                                verifyNetworkSwitchSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Network switch verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyNetworkSwitchSpecs(Map<String, String> specs) {
        try {
            return specs.containsKey("ports") &&
                    specs.getOrDefault("features", "").contains("QoS") &&
                    specs.containsKey("speed");
        } catch (Exception e) {
            issues.add("Network switch specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyNetworkInterfaces(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getDescription() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        String description = entry.getDescription().toLowerCase();
                        return (description.contains("ethernet") || description.contains("network")) &&
                                verifyNetworkInterfaceSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Network interfaces verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyNetworkInterfaceSpecs(Map<String, String> specs) {
        try {
            return specs.containsKey("speed") &&
                    specs.containsKey("interface") &&
                    specs.containsKey("features");
        } catch (Exception e) {
            issues.add("Network interface specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyEmergencyBeacon(PlannedProductionBatch batch) {
        try {
            return verifyBeaconProcessor(batch) && verifyBeaconGPS(batch) && verifyBeaconAntenna(batch);
        } catch (Exception e) {
            issues.add("Emergency beacon verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyBeaconProcessor(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getMpn().equals("STM32L412K8U6") &&
                                verifyBeaconProcessorSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Beacon processor verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyBeaconProcessorSpecs(Map<String, String> specs) {
        try {
            return specs.getOrDefault("features", "").contains("Low Power") &&
                    specs.containsKey("core") &&
                    specs.containsKey("flash");
        } catch (Exception e) {
            issues.add("Beacon processor specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyBeaconGPS(PlannedProductionBatch batch) {
        try {
            return batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getMpn() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getMpn().equals("NEO-M9N") &&
                                verifyBeaconGPSSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Beacon GPS verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyBeaconGPSSpecs(Map<String, String> specs) {
        try {
            return specs.containsKey("sensitivity") &&
                    specs.containsKey("accuracy") &&
                    specs.containsKey("features");
        } catch (Exception e) {
            issues.add("Beacon GPS specs verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyBeaconAntenna(PlannedProductionBatch batch) {
        try {
            return batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> {
                        if (entry.getDescription() == null || entry.getSpecs() == null) {
                            return false;
                        }
                        return entry.getDescription().contains("Emergency Beacon Antenna") &&
                                verifyBeaconAntennaSpecs(entry.getSpecs());
                    });
        } catch (Exception e) {
            issues.add("Beacon antenna verification error: " + e.getMessage());
            return false;
        }
    }

    private boolean verifyBeaconAntennaSpecs(Map<String, String> specs) {
        try {
            return specs.containsKey("frequency") &&
                    specs.containsKey("gain") &&
                    specs.containsKey("pattern") &&
                    specs.containsKey("waterproof");
        } catch (Exception e) {
            issues.add("Beacon antenna specs verification error: " + e.getMessage());
            return false;
        }
    }
}
