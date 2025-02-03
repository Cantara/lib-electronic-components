package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.BOM;
import no.cantara.electronic.component.BOMEntry;import java.util.*;

/**
 * Validates submarine system configurations for safety, environmental protection,
 * and operational requirements.
 */
public class SubmarineSystemValidator {
    private final List<String> validationErrors = new ArrayList<>();
    private final List<String> validationWarnings = new ArrayList<>();

    public void validate(PlannedProductionBatch batch) {
        validationErrors.clear();
        validationWarnings.clear();

        // Always validate environmental protection
        validateEnvironmentalProtection(batch);

        // Always validate power systems (they're required)
        validatePowerSystems(batch);

        // Always validate safety systems (they're required)
        validateSafetySystems(batch);

        // Only validate communications if they're included
        if (hasCommunicationComponents(batch)) {
            validateCommunicationSystems(batch);
        }
    }

    private boolean hasCommunicationComponents(PlannedProductionBatch batch) {
        return batch.getPCBAStructure().getAssemblies().stream()
                .anyMatch(pcba ->
                        pcba.getProductionNo().toLowerCase().contains("com") ||
                                pcba.getBomEntries().stream().anyMatch(entry ->
                                        (entry.getMpn() != null &&
                                                (entry.getMpn().contains("CC1200") ||
                                                        entry.getMpn().contains("DP83825"))) ||
                                                entry.getSpecs().containsKey("communication_redundancy")
                                )
                );
    }


    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public List<String> getValidationWarnings() {
        return Collections.unmodifiableList(validationWarnings);
    }

    private void validateEnvironmentalProtection(PlannedProductionBatch batch) {
        validateWaterproofing(batch);
        validateThermalManagement(batch);
        validatePressureRating(batch);
    }

    private void validatePowerSystems(PlannedProductionBatch batch) {
        validatePowerDistribution(batch);
        validatePowerRedundancy(batch);
        validateEmergencyPower(batch);
    }

    private void validateSafetySystems(PlannedProductionBatch batch) {
        validateSafetyMonitoring(batch);
        validateEmergencyShutdown(batch);
        validateFailSafes(batch);
    }

    private void validateCommunicationSystems(PlannedProductionBatch batch) {
        validateCommunicationRedundancy(batch);
        validateDataEncryption(batch);
        validateNetworkIsolation(batch);
    }

    private void validateWaterproofing(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getMechanicalStructure().getAssemblies(), "waterproof")) {
            validationErrors.add("Required waterproofing components missing from mechanical assembly");
        }
    }

    private void validateThermalManagement(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "thermal")) {
            validationErrors.add("Required thermal management components missing from PCBA");
        }
    }

    private void validatePressureRating(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getMechanicalStructure().getAssemblies(), "pressure")) {
            validationErrors.add("Required pressure-rated components missing from mechanical assembly");
        }
    }

    private void validatePowerDistribution(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "power distribution")) {
            validationErrors.add("Required power distribution components missing from PCBA");
        }
    }

    private void validatePowerRedundancy(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "redundant power")) {
            validationErrors.add("Required redundant power system missing from PCBA");
        }
    }

    private void validateEmergencyPower(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "emergency power")) {
            validationErrors.add("Required emergency power system missing from PCBA");
        }
    }

    private void validateSafetyMonitoring(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "safety monitor")) {
            validationErrors.add("Required safety monitoring components missing from PCBA");
        }
    }

    private void validateEmergencyShutdown(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "emergency shutdown")) {
            validationErrors.add("Required emergency shutdown system missing from PCBA");
        }
    }

    private void validateFailSafes(PlannedProductionBatch batch) {
        boolean hasPCBAFailSafes = hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "fail-safe");
        boolean hasMechFailSafes = hasComponentsMatchingCriteria(batch.getMechanicalStructure().getAssemblies(), "fail-safe");

        if (!(hasPCBAFailSafes && hasMechFailSafes)) {
            validationErrors.add("Required fail-safe components missing from system");
        }
    }

    private void validateCommunicationRedundancy(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "redundant communication")) {
            validationErrors.add("Required communication redundancy missing from PCBA");
        }
    }

    private void validateDataEncryption(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "encryption")) {
            validationErrors.add("Required data encryption components missing from PCBA");
        }
    }

    private void validateNetworkIsolation(PlannedProductionBatch batch) {
        if (!hasComponentsMatchingCriteria(batch.getPCBAStructure().getAssemblies(), "network isolation")) {
            validationErrors.add("Required network isolation components missing from PCBA");
        }
    }

    private boolean hasComponentsMatchingCriteriaOld(Set<? extends BOM> assemblies, String criteria) {
        return assemblies.stream().anyMatch(assembly ->
                matchesCriteria(assembly, criteria)
        );
    }

    private boolean matchesCriteria(BOM assembly, String criteria) {
        String searchCriteria = criteria.toLowerCase();

        // Check BOM fields
        if (assembly.getProductionNo() != null &&
                assembly.getProductionNo().toLowerCase().contains(searchCriteria)) {
            return true;
        }

        // Check entries in the BOM
        return assembly.getBomEntries().stream().anyMatch(entry -> {
            // Check component details
            if ((entry.getMpn() != null && entry.getMpn().toLowerCase().contains(searchCriteria)) ||
                    (entry.getDescription() != null && entry.getDescription().toLowerCase().contains(searchCriteria))) {
                return true;
            }

            // Check specs
            Map<String, String> specs = entry.getSpecs();
            if (specs != null) {
                // Check for communication-specific specs
                if (criteria.equals("communication")) {
                    return specs.containsKey("communication_redundancy") ||
                            specs.containsKey("redundant_communication") ||
                            specs.containsKey("network_redundancy");
                }

                // Regular spec checking
                return specs.entrySet().stream().anyMatch(spec ->
                        spec.getKey().toLowerCase().contains(searchCriteria) ||
                                spec.getValue().toLowerCase().contains(searchCriteria)
                );
            }
            return false;
        });
    }


    private boolean hasComponentsMatchingCriteria(Set<? extends BOM> assemblies, String criteria) {
        return assemblies.stream().anyMatch(assembly ->
                assembly.getBomEntries().stream().anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    if (specs == null) return false;

                    // Special handling for safety-related criteria
                    if (criteria.equals("safety_monitoring") ||
                            criteria.equals("emergency_shutdown") ||
                            criteria.equals("fail-safe")) {

                        // Check specs with hyphens and underscores
                        String hyphenated = criteria.replace('_', '-');
                        String underscored = criteria.replace('-', '_');

                        if (specs.containsKey(hyphenated) || specs.containsKey(underscored)) {
                            return true;
                        }

                        // Check description
                        if (entry.getDescription() != null &&
                                entry.getDescription().toLowerCase().contains(criteria.toLowerCase())) {
                            return true;
                        }

                        // Check all specs for safety-related terms
                        return specs.entrySet().stream().anyMatch(spec -> {
                            String key = spec.getKey().toLowerCase();
                            String value = spec.getValue().toLowerCase();
                            return (key.contains("safety") || key.contains("emergency") || key.contains("fail")) &&
                                    (value.equals("yes") || value.equals("active"));
                        });
                    }

                    return matchesCriteria(assembly, criteria);
                })
        );
    }
}