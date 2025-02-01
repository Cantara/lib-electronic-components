package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;

import java.time.LocalDate;

/**
 * Main factory class for creating complete autonomous submarine configurations.
 * Provides methods for assembling and validating complete submarine systems.
 */
public class SubmarineSystemFactory {

    public static class Configuration {
        private String batchId;
        private String productId;
        private String revision;
        private int quantity;
        private LocalDate plannedDate;
        private boolean includeNavigation = true;
        private boolean includePropulsion = true;
        private boolean includePower = true;
        private boolean includeCommunications = true;
        private boolean includeSensors = true;
        private boolean includeMissionControl = true;

        public Configuration(String batchId, String productId, String revision, int quantity) {
            this.batchId = batchId;
            this.productId = productId;
            this.revision = revision;
            this.quantity = quantity;
        }

        public Configuration setPlannedDate(LocalDate plannedDate) {
            this.plannedDate = plannedDate;
            return this;
        }

        public Configuration withoutNavigation() {
            this.includeNavigation = false;
            return this;
        }

        public Configuration withoutPropulsion() {
            this.includePropulsion = false;
            return this;
        }

        public Configuration withoutPower() {
            this.includePower = false;
            return this;
        }

        public Configuration withoutCommunications() {
            this.includeCommunications = false;
            return this;
        }

        public Configuration withoutSensors() {
            this.includeSensors = false;
            return this;
        }

        public Configuration withoutMissionControl() {
            this.includeMissionControl = false;
            return this;
        }
    }

    /**
     * Creates a complete submarine system with all subsystems included
     */
    public static PlannedProductionBatch createCompleteSystem(Configuration config) {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                config.batchId,
                config.productId,
                config.revision,
                config.quantity
        );

        if (config.plannedDate != null) {
            batch.setPlannedDate(config.plannedDate);
        }

        // Add subsystems based on configuration
        if (config.includeMissionControl) {
            addMissionControl(batch);
        }
        if (config.includeNavigation) {
            addNavigationControl(batch);
        }
        if (config.includePower) {
            addPowerSystem(batch);
        }
        if (config.includePropulsion) {
            addPropulsionSystem(batch);
        }
        if (config.includeCommunications) {
            addCommunicationsSystem(batch);
        }
        if (config.includeSensors) {
            addSensorSystem(batch);
        }

        validateSystem(batch);
        return batch;
    }

    /**
     * Creates a minimal viable submarine system with essential subsystems only
     */
    public static PlannedProductionBatch createMinimalSystem(String batchId, String productId, String revision, int quantity) {
        Configuration config = new Configuration(batchId, productId, revision, quantity)
                .withoutCommunications()
                .withoutSensors();
        return createCompleteSystem(config);
    }

    private static void addMissionControl(PlannedProductionBatch batch) {
        batch.addPCBA(MissionControlComponents.PCBAComponents.createMainControlBoard());
        batch.addMechanical(MissionControlComponents.MechanicalComponents.createHousing());
    }

    private static void addNavigationControl(PlannedProductionBatch batch) {
        batch.addPCBA(NavigationControlComponents.PCBAComponents.createMainControlBoard());
        batch.addMechanical(NavigationControlComponents.MechanicalComponents.createHousing());
    }

    private static void addPowerSystem(PlannedProductionBatch batch) {
        batch.addPCBA(PowerManagementComponents.PCBAComponents.createPowerControlBoard());
        batch.addMechanical(PowerManagementComponents.MechanicalComponents.createHousing());
    }

    private static void addPropulsionSystem(PlannedProductionBatch batch) {
        batch.addPCBA(PropulsionComponents.PCBAComponents.createMotorControlBoard());
        batch.addMechanical(PropulsionComponents.MechanicalComponents.createPropulsionUnit());
    }

    private static void addCommunicationsSystem(PlannedProductionBatch batch) {
        batch.addPCBA(CommunicationsComponents.PCBAComponents.createMainCommunicationsBoard());
        batch.addMechanical(CommunicationsComponents.MechanicalComponents.createHousing());
    }

    private static void addSensorSystem(PlannedProductionBatch batch) {
        batch.addPCBA(SensorSuiteComponents.PCBAComponents.createSensorInterfaceBoard());
        batch.addMechanical(SensorSuiteComponents.MechanicalComponents.createSensorPod());
    }

    /**
     * Validates the complete submarine system configuration
     */
    public static void validateSystem(PlannedProductionBatch batch) {
        SystemValidator.validateMechanicalCompatibility(batch);
        SystemValidator.validatePowerRequirements(batch);
        SystemValidator.validateCommunicationLinks(batch);
        SystemValidator.validateSafetyRequirements(batch);
        SystemValidator.validateEnvironmentalProtection(batch);
    }

    /**
     * Helper class for system validation
     */
    private static class SystemValidator {
        static void validateMechanicalCompatibility(PlannedProductionBatch batch) {
            // Verify DIN rail mounting compatibility
            boolean validMounting = batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .allMatch(entry -> {
                        var specs = entry.getSpecs();
                        return !specs.containsKey("mount_type") ||
                                specs.get("mount_type").equals("DIN Rail");
                    });
            if (!validMounting) {
                throw new IllegalStateException("Incompatible mounting systems detected");
            }
        }

        static void validatePowerRequirements(PlannedProductionBatch batch) {
            // Verify power distribution capability
            boolean hasPowerManagement = batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> entry.getDescription().contains("Power Management"));
            if (!hasPowerManagement) {
                throw new IllegalStateException("Missing power management system");
            }
        }

        static void validateCommunicationLinks(PlannedProductionBatch batch) {
            // Verify internal communication network
            boolean hasNetworking = batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> entry.getDescription().contains("Ethernet") ||
                            entry.getDescription().contains("Network"));
            if (!hasNetworking) {
                throw new IllegalStateException("Missing internal communication network");
            }
        }

        static void validateSafetyRequirements(PlannedProductionBatch batch) {
            // Verify presence of safety systems
            boolean hasSafetySystems = batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry -> entry.getDescription().contains("Safety") ||
                            entry.getDescription().contains("Watchdog"));
            if (!hasSafetySystems) {
                throw new IllegalStateException("Missing required safety systems");
            }
        }

        static void validateEnvironmentalProtection(PlannedProductionBatch batch) {
            // Verify water protection rating
            boolean hasWaterProtection = batch.getMechanicalStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .allMatch(entry -> {
                        var specs = entry.getSpecs();
                        return !specs.containsKey("pressure_rating") ||
                                specs.get("pressure_rating").contains("300m");
                    });
            if (!hasWaterProtection) {
                throw new IllegalStateException("Insufficient environmental protection");
            }
        }
    }

    /**
     * Example usage for creating different submarine configurations
     */
    public static class Examples {
        public static PlannedProductionBatch createStandardConfiguration() {
            Configuration config = new Configuration(
                    "AUV-2024-001",
                    "DEEPDIVE-1000",
                    "R1.0",
                    5
            ).setPlannedDate(LocalDate.now().plusMonths(1));

            return createCompleteSystem(config);
        }

        public static PlannedProductionBatch createLiteConfiguration() {
            Configuration config = new Configuration(
                    "AUV-2024-002",
                    "DEEPDIVE-500",
                    "R1.0",
                    10
            )
                    .setPlannedDate(LocalDate.now().plusMonths(1))
                    .withoutCommunications()
                    .withoutSensors();

            return createCompleteSystem(config);
        }

        public static PlannedProductionBatch createResearchConfiguration() {
            Configuration config = new Configuration(
                    "AUV-2024-003",
                    "DEEPDIVE-2000",
                    "R1.0",
                    2
            )
                    .setPlannedDate(LocalDate.now().plusMonths(2));

            return createCompleteSystem(config);
        }
    }
}