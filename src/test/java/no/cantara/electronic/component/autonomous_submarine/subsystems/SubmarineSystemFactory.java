package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class SubmarineSystemFactory {
    // Enum definition for subsystem types
    private enum SubsystemType {
        NETWORK("Network", true),
        MISSION_CONTROL("Mission Control", false),
        NAVIGATION("Navigation", false),
        POWER("Power", true),
        PROPULSION("Propulsion", false),
        COMMUNICATIONS("Communications", false),
        SENSOR("Sensor", false);

        private final String name;
        private final boolean required;

        SubsystemType(String name, boolean required) {
            this.name = name;
            this.required = required;
        }

        public String getName() {
            return name;
        }

        public boolean isRequired() {
            return required;
        }
    }

    /**
     * Abstract base builder with common functionality
     */
    private static abstract class AbstractSubmarineBuilder {
        protected final PlannedProductionBatch batch;
        protected final ComponentSpecifications specs;
        protected LocalDate plannedDate;
        protected final Set<SubsystemType> includedSubsystems;

        protected AbstractSubmarineBuilder(String batchId, String productId, String revision, int quantity) {
            this.batch = new PlannedProductionBatch(batchId, productId, revision, quantity);
            this.specs = new ComponentSpecifications();
            this.includedSubsystems = new HashSet<>();
            // Add required subsystems
            for (SubsystemType type : SubsystemType.values()) {
                if (type.isRequired()) {
                    includedSubsystems.add(type);
                }
            }
        }

        public AbstractSubmarineBuilder plannedDate(LocalDate date) {
            this.plannedDate = date;
            return this;
        }

        protected void addSubsystemComponents(SubsystemType type) {
            PCBABOM pcba = null;
            MechanicalBOM mechanical = null;

            switch (type) {
                case NETWORK:
                    pcba = specs.createNetworkBoard();
                    mechanical = specs.createNetworkMechanicals();
                    break;
                case MISSION_CONTROL:
                    pcba = specs.createMissionControlBoard();
                    mechanical = specs.createMissionControlMechanicals();
                    break;
                case NAVIGATION:
                    pcba = specs.createNavigationBoard();
                    mechanical = specs.createNavigationMechanicals();
                    break;
                case POWER:
                    pcba = specs.createPowerBoard();
                    mechanical = specs.createPowerMechanicals();
                    break;
                case PROPULSION:
                    pcba = specs.createPropulsionBoard();
                    mechanical = specs.createPropulsionMechanicals();
                    break;
                case COMMUNICATIONS:
                    pcba = specs.createCommunicationsBoard();
                    mechanical = specs.createCommunicationsMechanicals();
                    break;
                case SENSOR:
                    if (includedSubsystems.contains(SubsystemType.SENSOR)) {
                        pcba = specs.createMinimalSensorBoard();
                        mechanical = specs.createMinimalSensorMechanicals();
                    }
                    break;
            }

            if (pcba != null) batch.addPCBA(pcba);
            if (mechanical != null) batch.addMechanical(mechanical);
        }

        protected void validate() {
            validateRequiredSubsystems();
            validateSubsystemDependencies();
            validateRedundancyRequirements();
            validateSafetyRequirements();
        }

        protected void validateRequiredSubsystems() {
            for (SubsystemType type : SubsystemType.values()) {
                if (type.isRequired() && !includedSubsystems.contains(type)) {
                    throw new IllegalStateException(
                            String.format("%s is required for submarine operation", type.getName())
                    );
                }
            }
        }

        protected void validateSubsystemDependencies() {
            Map<SubsystemType, List<SubsystemType>> dependencies = new HashMap<>();
            dependencies.put(SubsystemType.NAVIGATION, Arrays.asList(SubsystemType.SENSOR));

            dependencies.forEach((subsystem, required) -> {
                if (includedSubsystems.contains(subsystem)) {
                    for (SubsystemType dependency : required) {
                        if (!includedSubsystems.contains(dependency)) {
                            throw new IllegalStateException(
                                    String.format("%s system requires %s",
                                            subsystem.getName(),
                                            dependency.getName())
                            );
                        }
                    }
                }
            });
        }

        protected void validateRedundancyRequirements() {
            Map<String, String> redundancyRequirements = new HashMap<>();
            redundancyRequirements.put("Network", "network_redundancy");

            redundancyRequirements.forEach((system, spec) -> {
                boolean hasRedundancy = batch.getPCBAStructure().getAssemblies().stream()
                        .flatMap(bom -> bom.getBomEntries().stream())
                        .anyMatch(entry ->
                                entry.getMpn().contains("-RED") ||
                                        entry.getDescription().toLowerCase().contains("redundant") ||
                                        (entry.getSpecs() != null &&
                                                entry.getSpecs().containsKey("network_redundancy") &&
                                                "yes".equalsIgnoreCase(entry.getSpecs().get("network_redundancy")))
                        );
                if (!hasRedundancy) {
                    throw new IllegalStateException(
                            String.format("%s system must have redundancy", system)
                    );
                }
            });
        }

        protected void validateSafetyRequirements() {
            // Check for watchdog more thoroughly
            boolean hasWatchdog = batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry ->
                            entry.getMpn().contains("MAX6369") ||
                                    entry.getDescription().toLowerCase().contains("watchdog") ||
                                    (entry.getSpecs() != null && (
                                            (entry.getSpecs().containsKey("protection") &&
                                                    "OTP".equals(entry.getSpecs().get("protection"))) ||
                                                    entry.getSpecs().values().stream()
                                                            .anyMatch(v -> v.toString().toLowerCase().contains("watchdog"))
                                    ))
                    );

            if (!hasWatchdog) {
                throw new IllegalStateException("System must include watchdog protection");
            }

            // Check thermal protection
            boolean hasThermalProtection = batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry ->
                            entry.getMpn().contains("MAX6654") ||
                                    entry.getDescription().toLowerCase().contains("thermal") ||
                                    (entry.getSpecs() != null &&
                                            entry.getSpecs().containsKey("thermal_management") &&
                                            "active".equals(entry.getSpecs().get("thermal_management")))
                    );

            if (!hasThermalProtection) {
                throw new IllegalStateException("System must include thermal protection");
            }

            // Check emergency shutdown
            boolean hasEmergencyShutdown = batch.getPCBAStructure().getAssemblies().stream()
                    .flatMap(bom -> bom.getBomEntries().stream())
                    .anyMatch(entry ->
                            entry.getMpn().contains("TMS570") ||
                                    entry.getDescription().toLowerCase().contains("emergency shutdown") ||
                                    (entry.getSpecs() != null &&
                                            entry.getSpecs().containsKey("emergency_shutdown") &&
                                            "yes".equals(entry.getSpecs().get("emergency_shutdown")))
                    );

            if (!hasEmergencyShutdown) {
                throw new IllegalStateException("System must include emergency shutdown capability");
            }
        }

        public PlannedProductionBatch build() {
            if (plannedDate != null) {
                batch.setPlannedDate(plannedDate);
            }
            addComponents();
            validate();
            return batch;
        }

        protected abstract void addComponents();
    }

    /**
     * Complete system builder
     */
    public static class CompleteSystemBuilder extends AbstractSubmarineBuilder {
        CompleteSystemBuilder(String batchId, String productId, String revision, int quantity) {
            super(batchId, productId, revision, quantity);
            includedSubsystems.addAll(Arrays.asList(SubsystemType.values()));
        }

        @Override
        public CompleteSystemBuilder plannedDate(LocalDate date) {
            super.plannedDate(date);
            return this;
        }

        @Override
        protected void addComponents() {
            for (SubsystemType type : SubsystemType.values()) {
                addSubsystemComponents(type);
            }
        }
    }

    /**
     * Minimal system builder
     */
    public static class MinimalSystemBuilder extends AbstractSubmarineBuilder {
        MinimalSystemBuilder(String batchId, String productId, String revision, int quantity) {
            super(batchId, productId, revision, quantity);
            // Add sensor system by default for minimal configuration
            includedSubsystems.add(SubsystemType.SENSOR);
        }

        @Override
        public MinimalSystemBuilder plannedDate(LocalDate date) {
            super.plannedDate(date);
            return this;
        }

        @Override
        protected void addComponents() {
            // Add required basic components
            for (SubsystemType type : includedSubsystems) {
                addSubsystemComponents(type);
            }

            // Always add safety components for minimal system
            addSafetyComponents();
        }

        private void addSafetyComponents() {
            // Add watchdog
            BOMEntry watchdog = new BOMEntry();
            watchdog.setMpn("MAX6369KA+T");
            watchdog.setDescription("Watchdog Timer with OTP");
            watchdog.addSpec("protection", "OTP");
            watchdog.addSpec("monitoring_type", "Continuous");
            watchdog.addSpec("safety_monitoring", "active");
            watchdog.addSpec("fail-safe", "yes");
            specs.addBasicSpecs(watchdog);

            // Add temperature monitor
            BOMEntry tempMonitor = new BOMEntry();
            tempMonitor.setMpn("MAX6654");
            tempMonitor.setDescription("Temperature Monitor with OTP");
            tempMonitor.addSpec("thermal_management", "active");
            tempMonitor.addSpec("monitoring_type", "Continuous");
            tempMonitor.addSpec("safety_monitoring", "active");
            tempMonitor.addSpec("fail-safe", "yes");
            specs.addBasicSpecs(tempMonitor);

            // Add safety processor
            BOMEntry safetyProcessor = new BOMEntry();
            safetyProcessor.setMpn("TMS570LC4357BZWTQQ1");
            safetyProcessor.setDescription("Safety Co-Processor with Emergency Shutdown");
            safetyProcessor.addSpec("emergency_shutdown", "yes");
            safetyProcessor.addSpec("monitoring_type", "Continuous");
            safetyProcessor.addSpec("safety_monitoring", "active");
            safetyProcessor.addSpec("fail-safe", "yes");
            specs.addBasicSpecs(safetyProcessor);

            // Create safety PCBA
            List<BOMEntry> safetyEntries = Arrays.asList(watchdog, tempMonitor, safetyProcessor);
            PCBABOM safetyBoard = new PCBABOM("SAFETY-MIN-01", "Minimal Safety Board", "R1.0", safetyEntries);
            batch.addPCBA(safetyBoard);
        }
    }

    /**
     * Custom system builder
     */
    public static class CustomSystemBuilder extends AbstractSubmarineBuilder {
        private boolean safetyAdded = false;
        public CustomSystemBuilder(String batchId, String productId, String revision, int quantity) {
            super(batchId, productId, revision, quantity);
        }

        @Override
        public CustomSystemBuilder plannedDate(LocalDate date) {
            super.plannedDate(date);
            return this;
        }

        public CustomSystemBuilder withMinimalConfiguration() {
            includedSubsystems.clear();
            for (SubsystemType type : SubsystemType.values()) {
                if (type.isRequired()) {
                    includedSubsystems.add(type);
                }
            }
            ensureSafetyComponents();
            return this;
        }

        private void ensureSafetyComponents() {
            if (!safetyAdded) {
                addSafetyComponents();
                safetyAdded = true;
            }
        }

        private void addSafetyComponents() {
            // Add watchdog timer
            BOMEntry watchdog = new BOMEntry();
            watchdog.setMpn("MAX6369KA+T");
            watchdog.setDescription("Watchdog Timer with OTP");
            watchdog.addSpec("protection", "OTP");
            watchdog.addSpec("monitoring_type", "Continuous");
            watchdog.addSpec("safety_monitoring", "active");
            watchdog.addSpec("fail-safe", "yes");
            specs.addBasicSpecs(watchdog);

            // Add temperature monitor
            BOMEntry tempMonitor = new BOMEntry();
            tempMonitor.setMpn("MAX6654");
            tempMonitor.setDescription("Temperature Monitor with OTP");
            tempMonitor.addSpec("thermal_management", "active");
            tempMonitor.addSpec("monitoring_type", "Continuous");
            tempMonitor.addSpec("safety_monitoring", "active");
            tempMonitor.addSpec("fail-safe", "yes");
            specs.addBasicSpecs(tempMonitor);

            // Add safety processor
            BOMEntry safetyProcessor = new BOMEntry();
            safetyProcessor.setMpn("TMS570LC4357BZWTQQ1");
            safetyProcessor.setDescription("Safety Co-Processor with Emergency Shutdown");
            safetyProcessor.addSpec("emergency_shutdown", "yes");
            safetyProcessor.addSpec("monitoring_type", "Continuous");
            safetyProcessor.addSpec("safety_monitoring", "active");
            safetyProcessor.addSpec("fail-safe", "yes");
            specs.addBasicSpecs(safetyProcessor);

            // Create and add safety board
            List<BOMEntry> safetyEntries = Arrays.asList(watchdog, tempMonitor, safetyProcessor);
            PCBABOM safetyBoard = new PCBABOM("SAFETY-MIN-01", "Safety Management Board", "R1.0", safetyEntries);
            batch.addPCBA(safetyBoard);
        }

        public CustomSystemBuilder withMinimalSensors() {
            includedSubsystems.add(SubsystemType.SENSOR);
            return this;
        }

        public CustomSystemBuilder withSensors() {
            includedSubsystems.add(SubsystemType.SENSOR);
            return this;
        }

        public CustomSystemBuilder withNavigation() {
            includedSubsystems.add(SubsystemType.NAVIGATION);
            includedSubsystems.add(SubsystemType.SENSOR);
            return this;
        }

        public CustomSystemBuilder withCommunications() {
            includedSubsystems.add(SubsystemType.COMMUNICATIONS);
            return this;
        }

        public CustomSystemBuilder withPropulsion() {
            includedSubsystems.add(SubsystemType.PROPULSION);
            return this;
        }

        public CustomSystemBuilder withMissionControl() {
            includedSubsystems.add(SubsystemType.MISSION_CONTROL);
            return this;
        }

        @Override
        protected void addComponents() {
            ensureSafetyComponents();
            // Add components in specific order
            // First add mission control if included (for thermal management)
            if (includedSubsystems.contains(SubsystemType.MISSION_CONTROL)) {
                addSubsystemComponents(SubsystemType.MISSION_CONTROL);
            }

            // Then add other components
            for (SubsystemType type : includedSubsystems) {
                if (type != SubsystemType.MISSION_CONTROL) {  // Skip mission control as it's already added
                    addSubsystemComponents(type);
                }
            }
        }
    }

    // Factory methods
    public static CompleteSystemBuilder createCompleteSystem(String batchId, String productId, String revision, int quantity) {
        return new CompleteSystemBuilder(batchId, productId, revision, quantity);
    }

    public static MinimalSystemBuilder createMinimalSystem(String batchId, String productId, String revision, int quantity) {
        return new MinimalSystemBuilder(batchId, productId, revision, quantity);
    }

    public static CustomSystemBuilder createCustomSystem(String batchId, String productId, String revision, int quantity) {
        return new CustomSystemBuilder(batchId, productId, revision, quantity);
    }
}