package no.cantara.electronic.component.rocket;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.BOMEntry;

import java.util.ArrayList;
import java.util.List;

public class RocketSystemValidator {
    private List<String> validationErrors;

    public RocketSystemValidator() {
        this.validationErrors = new ArrayList<>();
    }

    public void validate(PlannedProductionBatch batch) {
        validationErrors.clear();

        if (batch == null) {
            validationErrors.add("Planned production batch cannot be null");
            return;
        }

        validateBasicStructure(batch);
        validateSafetyAndProtection(batch);
    }

    public List<String> getValidationErrors() {
        return new ArrayList<>(validationErrors);
    }

    private void validateBasicStructure(PlannedProductionBatch batch) {
        if (batch.getPCBAStructure() == null) {
            validationErrors.add("PCBA structure is missing");
            return;
        }

        if (batch.getPCBAStructure().getAssemblies().isEmpty()) {
            validationErrors.add("No assemblies found in PCBA structure");
        }

        if (batch.getMechanicalStructure() == null) {
            validationErrors.add("Mechanical structure is missing");
            return;
        }

        if (batch.getMechanicalStructure().getAssemblies().isEmpty()) {
            validationErrors.add("No assemblies found in mechanical structure");
        }
    }

    private void validateSafetyAndProtection(PlannedProductionBatch batch) {
        // Safety systems
        if (!hasWatchdogProtection(batch)) {
            validationErrors.add("Watchdog protection is missing");
        }
        if (!hasOvercurrentProtection(batch)) {
            validationErrors.add("Overcurrent protection is missing");
        }
        if (!hasEmergencyAbortSystem(batch)) {
            validationErrors.add("Emergency abort system is missing");
        }
        if (!hasThermalProtection(batch)) {
            validationErrors.add("Thermal protection is missing");
        }

        // Security features
        if (!hasEncryption(batch)) {
            validationErrors.add("Encryption is missing");
        }
        if (!hasSecureBootloader(batch)) {
            validationErrors.add("Secure bootloader is missing");
        }
    }

    public boolean hasComponent(PlannedProductionBatch batch, String mpn, String description) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        (entry.getMpn() != null && entry.getMpn().contains(mpn)) ||
                                (entry.getDescription() != null && entry.getDescription().contains(description)));
    }

    public boolean hasRedundantComponents(PlannedProductionBatch batch, String componentType) {
        long count = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry ->
                        (entry.getDescription() != null && entry.getDescription().contains(componentType)) ||
                                (entry.getMpn() != null && entry.getMpn().contains("-RED")))
                .count();
        return count >= 2;
    }

    public boolean hasThermalManagement(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("thermal_management") &&
                                "active".equals(entry.getSpecs().get("thermal_management")));
    }

    public boolean hasWatchdogProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("watchdog") &&
                                "enabled".equals(entry.getSpecs().get("watchdog")));
    }

    public boolean hasOvercurrentProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs() != null &&
                                (entry.getSpecs().containsKey("overcurrent_protection") &&
                                        "enabled".equals(entry.getSpecs().get("overcurrent_protection"))) ||
                                (entry.getSpecs().containsKey("protection") &&
                                        "OCP".equals(entry.getSpecs().get("protection"))));
    }

    public boolean hasEmergencyAbortSystem(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("emergency_abort") &&
                                "yes".equals(entry.getSpecs().get("emergency_abort")));
    }

    public boolean hasThermalProtection(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("thermal_protection") &&
                                "enabled".equals(entry.getSpecs().get("thermal_protection")));
    }

    public boolean hasEncryption(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("encryption") &&
                                "enabled".equals(entry.getSpecs().get("encryption")));
    }

    public boolean hasSecureBootloader(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("secure_bootloader") &&
                                "enabled".equals(entry.getSpecs().get("secure_bootloader")));
    }
}