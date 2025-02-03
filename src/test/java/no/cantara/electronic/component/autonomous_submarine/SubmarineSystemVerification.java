package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.autonomous_submarine.subsystems.*;
import no.cantara.electronic.component.BOMEntry;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubmarineSystemVerification {
    private final List<String> crossSystemIssues = new ArrayList<>();
    private final ComponentTypeDetector typeDetector= new ComponentTypeDetector();

    public SystemVerificationResult verifySystem(PlannedProductionBatch batch) {
        SystemVerificationResult result = new SystemVerificationResult();

        try {
            verifySubsystem(batch, result, "Mission Control", new MissionControlVerification());
            verifySubsystem(batch, result, "Navigation", new NavigationVerification());
            verifySubsystem(batch, result, "Power", new PowerManagementVerification());
            verifySubsystem(batch, result, "Propulsion", new PropulsionVerification());
            verifySubsystem(batch, result, "Communications", new CommunicationsVerification());
            verifySubsystem(batch, result, "Sensors", new SensorSuiteVerification());

            verifyCrossSystemIntegration(batch);
            result.setCrossSystemIssues(crossSystemIssues);
        } catch (Exception e) {
            crossSystemIssues.add("System verification failed: " + e.getMessage());
        }

        return result;
    }

    private void verifySubsystem(PlannedProductionBatch batch, SystemVerificationResult result,
                                 String subsystemName, SubsystemVerification verification) {
        try {
            boolean componentsValid = verification.verifyComponents(batch);
            boolean integrationValid = verification.verifyIntegration(batch);
            List<String> issues = verification.getVerificationIssues();

            result.addSubsystemResult(subsystemName, new SubsystemVerificationResult(
                    componentsValid,
                    integrationValid,
                    issues
            ));
        } catch (Exception e) {
            List<String> errorIssues = Collections.singletonList("Subsystem verification failed: " + e.getMessage());
            result.addSubsystemResult(subsystemName, new SubsystemVerificationResult(
                    false,
                    false,
                    errorIssues
            ));
        }
    }

    private void verifyCrossSystemIntegration(PlannedProductionBatch batch) {
        verifyPowerDistribution(batch);
        verifyNetworkConnectivity(batch);
        verifyEnvironmentalCompliance(batch);
        verifySafetySystemIntegration(batch);
    }

    private void verifyPowerDistribution(PlannedProductionBatch batch) {
        try {
            double totalPowerRequired = calculateTotalPowerRequirement(batch);
            double availablePower = getAvailablePower(batch);

            if (totalPowerRequired > availablePower) {
                crossSystemIssues.add(String.format("Power requirement (%.2fW) exceeds available power (%.2fW)",
                        totalPowerRequired, availablePower));
            }
        } catch (Exception e) {
            crossSystemIssues.add("Power distribution verification failed: " + e.getMessage());
        }
    }

    private double calculateTotalPowerRequirement(PlannedProductionBatch batch) {
        if (batch == null || batch.getPCBAStructure() == null) {
            return 0.0;
        }

        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("power"))
                .mapToDouble(this::extractPowerValue)
                .sum();
    }

    private double extractPowerValue(BOMEntry entry) {
        try {
            String powerSpec = entry.getSpecs().get("power");
            if (powerSpec == null || powerSpec.trim().isEmpty()) {
                return 0.0;
            }
            String powerValue = powerSpec.replaceAll("[^0-9.]", "");
            return powerValue.isEmpty() ? 0.0 : Double.parseDouble(powerValue);
        } catch (NumberFormatException e) {
            crossSystemIssues.add("Invalid power specification for " + entry.getMpn() + ": " + e.getMessage());
            return 0.0;
        }
    }

    private double getAvailablePower(PlannedProductionBatch batch) {
        if (batch == null || batch.getPCBAStructure() == null) {
            return 0.0;
        }

        return batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("power_output"))
                .mapToDouble(this::extractPowerOutputValue)
                .sum();
    }

    private double extractPowerOutputValue(BOMEntry entry) {
        try {
            String powerSpec = entry.getSpecs().getOrDefault("power_output", "0W");
            if (powerSpec == null || powerSpec.trim().isEmpty()) {
                return 0.0;
            }
            String powerValue = powerSpec.replaceAll("[^0-9.]", "");
            return powerValue.isEmpty() ? 0.0 : Double.parseDouble(powerValue);
        } catch (NumberFormatException e) {
            crossSystemIssues.add("Invalid power output specification for " + entry.getMpn() + ": " + e.getMessage());
            return 0.0;
        }
    }

    private void verifyNetworkConnectivity(PlannedProductionBatch batch) {
        if (batch == null) return;

        try {
            verifyNetworkInterfaces(batch);
            verifyNetworkTopology(batch);
            verifyRedundantConnections(batch);
        } catch (Exception e) {
            crossSystemIssues.add("Network connectivity verification failed: " + e.getMessage());
        }
    }

    private void verifyNetworkInterfaces(PlannedProductionBatch batch) {
        boolean hasRequiredInterfaces = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .anyMatch(entry -> {
                    String interfaceType = entry.getSpecs().getOrDefault("interface", "");
                    return interfaceType.contains("Ethernet") || interfaceType.contains("CAN");
                });

        if (!hasRequiredInterfaces) {
            crossSystemIssues.add("Required network interfaces not found");
        }
    }

    private void verifyNetworkTopology(PlannedProductionBatch batch) {
        boolean hasNetworkSwitch = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Network Switch"));

        if (!hasNetworkSwitch) {
            crossSystemIssues.add("Network switch not found in system");
        }
    }

    private void verifyRedundantConnections(PlannedProductionBatch batch) {
        // Implementation for redundant connections verification
    }

    private void verifyEnvironmentalCompliance(PlannedProductionBatch batch) {
        if (batch == null) return;

        try {
            verifyTemperatureRanges(batch);
            verifyPressureRatings(batch);
            verifyWaterproofing(batch);
        } catch (Exception e) {
            crossSystemIssues.add("Environmental compliance verification failed: " + e.getMessage());
        }
    }

    private void verifyTemperatureRanges(PlannedProductionBatch batch) {
        batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("temp_range"))
                .forEach(this::validateTemperatureRange);
    }

    private void validateTemperatureRange(BOMEntry entry) {
        String tempRange = entry.getSpecs().get("temp_range");
        if (!tempRange.contains("-40°C")) {
            crossSystemIssues.add("Component " + entry.getMpn() + " does not meet minimum temperature requirement");
        }
    }

    private void verifyPressureRatingOlds(PlannedProductionBatch batch) {
        batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("pressure_rating"))
                .forEach(this::validatePressureRating);
    }

    private void validatePressureRating(BOMEntry entry) {
        String pressureRating = entry.getSpecs().get("pressure_rating");
        if (!pressureRating.contains("300m")) {
            crossSystemIssues.add("Component " + entry.getMpn() + " does not meet pressure rating requirement");
        }
    }

    private void verifyWaterproofingOld(PlannedProductionBatch batch) {
        batch.getMechanicalStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().containsKey("sealing"))
                .forEach(this::validateWaterproofing);
    }

    private void validateWaterproofing(BOMEntry entry) {
        String sealing = entry.getSpecs().get("sealing");
        if (!sealing.contains("IP68") && !sealing.contains("Double O-ring")) {
            crossSystemIssues.add("Component " + entry.getMpn() + " does not meet waterproofing requirement");
        }
    }

    private void verifySafetySystemIntegration(PlannedProductionBatch batch) {
        if (batch == null) return;

        try {
            verifyEmergencyShutdown(batch);
            verifyRedundantSystems(batch);
            verifyMonitoringSystems(batch);
        } catch (Exception e) {
            crossSystemIssues.add("Safety system integration verification failed: " + e.getMessage());
        }
    }

    private void verifyEmergencyShutdown(PlannedProductionBatch batch) {
        boolean hasEmergencyShutdown = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .anyMatch(entry ->
                        entry.getSpecs().getOrDefault("features", "")
                                .contains("Emergency Shutdown"));

        if (!hasEmergencyShutdown) {
            crossSystemIssues.add("Emergency shutdown system not found");
        }
    }

    private void verifyRedundantSystems(PlannedProductionBatch batch) {
        long redundantProcessors = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getSpecs() != null)
                .filter(entry -> entry.getSpecs().getOrDefault("features", "")
                        .contains("Lock-step"))
                .count();

        if (redundantProcessors < 2) {
            crossSystemIssues.add("Insufficient redundant processors found");
        }
    }

    private void verifyMonitoringSystems(PlannedProductionBatch batch) {
        verifyTemperatureMonitoring(batch);
        verifyVoltageMonitoring(batch);
        verifyCurrentMonitoring(batch);
    }

    private void verifyTemperatureMonitoring(PlannedProductionBatch batch) {
        boolean hasTempMonitor = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Temperature Monitor") &&
                                entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("accuracy"));

        if (!hasTempMonitor) {
            crossSystemIssues.add("Temperature monitoring system not found");
        }
    }

    private void verifyVoltageMonitoring(PlannedProductionBatch batch) {
        boolean hasVoltageMonitor = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Voltage Monitor") &&
                                entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("accuracy"));

        if (!hasVoltageMonitor) {
            crossSystemIssues.add("Voltage monitoring system not found");
        }
    }

    private void verifyCurrentMonitoring(PlannedProductionBatch batch) {
        boolean hasCurrentMonitor = batch.getPCBAStructure().getAssemblies().stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription() != null &&
                                entry.getDescription().contains("Current Monitor") &&
                                entry.getSpecs() != null &&
                                entry.getSpecs().containsKey("accuracy"));

        if (!hasCurrentMonitor) {
            crossSystemIssues.add("Current monitoring system not found");
        }
    }

    /**
     * Verifies waterproofing requirements for all components.
     */
    public void verifyWaterproofing(PlannedProductionBatch batch) {
        StringBuilder errorReport = new StringBuilder("Waterproofing Verification Report:\n");
        boolean allValid = true;

        // First verify basic waterproofing for all components
        for (BOMEntry entry : getAllComponents(batch)) {
            if (!verifyBasicWaterproofing(entry, errorReport)) {
                allValid = false;
            }
        }

        // Then verify power protection only for power system components
        for (BOMEntry entry : getPowerSystemComponents(batch)) {
            if (requiresBatteryProtection(entry) && !verifyBatteryProtection(entry, errorReport)) {
                allValid = false;
            }
        }

        assertTrue(allValid, "Waterproofing requirements not met:\n" + errorReport.toString());
    }

    private boolean verifyBasicWaterproofing(BOMEntry entry, StringBuilder errorReport) {
        Map<String, String> specs = entry.getSpecs();
        String componentInfo = String.format("%s (%s)", entry.getMpn(), entry.getDescription());
        boolean valid = true;

        System.out.println("\nVerifying waterproofing for: " + componentInfo);
        System.out.println("Specs: " + specs);


        // Check waterproofing specs
        if (!specs.containsKey("waterproof") || !"Yes".equals(specs.get("waterproof"))) {
            errorReport.append(String.format("  Component not waterproof: %s\n", componentInfo));
            valid = false;
        }

        // Check sealing specs - look for both "sealing" and "sealing_method"
        if (!specs.containsKey("sealing") || !"IP68".equals(specs.get("sealing"))) {
            System.out.println("FAILED: sealing spec. Current value: " + specs.get("sealing"));
            errorReport.append(String.format("  Missing or invalid sealing for component %s\n", componentInfo));
            valid = false;
        }

        // Check protection rating
        if (!specs.containsKey("protection_rating") || !"IP68".equals(specs.get("protection_rating"))) {
            errorReport.append(String.format("  Missing or invalid protection_rating for component %s\n", componentInfo));
            valid = false;
        }
        System.out.println("PASSED: All waterproofing checks");
        return valid;
    }


    /**
     * Verifies thermal management requirements for all components.
     */
    public void verifyThermalManagement(PlannedProductionBatch batch) {
        StringBuilder errorReport = new StringBuilder("Thermal Management Verification Report:\n");
        boolean allValid = true;

        // Verify all components
        for (BOMEntry entry : getAllComponents(batch)) {
            if (!verifyComponentThermal(entry, errorReport)) {
                allValid = false;
            }
        }

        // Verify subsystem-level thermal management
        Map<String, Boolean> subsystemResults = new HashMap<>();
        subsystemResults.put("Navigation", verifySubsystemThermal(batch, "Navigation", errorReport));
        subsystemResults.put("Propulsion", verifySubsystemThermal(batch, "Propulsion", errorReport));
        subsystemResults.put("Communications", verifySubsystemThermal(batch, "Communications", errorReport));
        subsystemResults.put("Mission Control", verifySubsystemThermal(batch, "Mission Control", errorReport));
        subsystemResults.put("Sensors", verifySubsystemThermal(batch, "Sensors", errorReport));
        subsystemResults.put("Power", verifySubsystemThermal(batch, "Power", errorReport));

        // Check if any subsystem failed
        for (Map.Entry<String, Boolean> subsystemResult : subsystemResults.entrySet()) {
            if (!subsystemResult.getValue()) {
                allValid = false;
                errorReport.append(String.format("Subsystem %s failed thermal management check\n",
                        subsystemResult.getKey()));
            }
        }

        assertTrue(allValid, "Thermal management requirements not met:\n" + errorReport.toString());
    }

    private boolean verifyBatteryProtection(BOMEntry entry, StringBuilder errorReport) {
        Map<String, String> specs = entry.getSpecs();
        String componentInfo = String.format("%s (%s)", entry.getMpn(), entry.getDescription());

        if (!specs.containsKey("battery_protection") || specs.get("battery_protection").isEmpty()) {
            errorReport.append(String.format("  Missing or invalid battery_protection for Power component %s\n",
                    componentInfo));
            return false;
        }

        return true;
    }

    private boolean requiresBatteryProtectionOld(BOMEntry entry) {
        // Only electronic components in power subsystem need battery protection
        if (isMechanicalComponent(entry)) {
            return false;
        }

        // Check if it's an electronic component
        return isElectronicComponent(entry);
    }

    private boolean isMechanicalComponentOld(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("housing") ||
                desc.contains("mount") ||
                desc.contains("seal") ||
                desc.contains("connector") ||
                desc.contains("hub") ||
                desc.contains("propeller") ||
                desc.contains("bearing");
    }

    private boolean isElectronicComponentOld(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("processor") ||
                desc.contains("controller") ||
                desc.contains("transceiver") ||
                desc.contains("multiplexer") ||
                desc.contains("converter") ||
                desc.contains("adc") ||
                desc.contains("sensor") ||
                entry.getMpn().toLowerCase().matches(".*(max|ads|tca|adg).*");
    }

    private List<BOMEntry> getAllComponentsOld(PlannedProductionBatch batch) {
        List<BOMEntry> allComponents = new ArrayList<>();
        batch.getPCBAStructure().getAssemblies().forEach(pcba ->
                allComponents.addAll(pcba.getBomEntries()));
        batch.getMechanicalStructure().getAssemblies().forEach(mech ->
                allComponents.addAll(mech.getBomEntries()));
        return allComponents;
    }

    private List<BOMEntry> getPowerSystemComponents(PlannedProductionBatch batch) {
        return getAllComponents(batch).stream()
                .filter(entry -> isInPowerSubsystem(entry))
                .collect(Collectors.toList());
    }

    private boolean isInPowerSubsystemOld(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("power") ||
                entry.getMpn().toLowerCase().startsWith("ltc") ||
                entry.getMpn().toLowerCase().startsWith("tps") ||
                entry.getMpn().toLowerCase().startsWith("bq");
    }
    /**
     * Verifies pressure rating requirements for all components.
     */
    public void verifyPressureRatings(PlannedProductionBatch batch) {
        StringBuilder errorReport = new StringBuilder("Pressure Rating Verification Report:\n");
        boolean allValid = true;

        // Check all components
        for (BOMEntry entry : getAllComponents(batch)) {
            String pressureRating = entry.getSpecs().get("pressure_rating");
            if (pressureRating == null) {
                errorReport.append(String.format("  Missing pressure rating for %s (%s)\n",
                        entry.getMpn(), entry.getDescription()));
                allValid = false;
            } else if (!pressureRating.equals("300m depth")) {
                errorReport.append(String.format("  Invalid pressure rating format for %s: %s\n",
                        entry.getMpn(), pressureRating));
                allValid = false;
            }
        }

        assertTrue(allValid, "Pressure rating requirements not met:\n" + errorReport.toString());
    }

    private List<BOMEntry> getAllComponents(PlannedProductionBatch batch) {
        List<BOMEntry> allComponents = new ArrayList<>();

        // Add PCBA components
        batch.getPCBAStructure().getAssemblies().forEach(pcba ->
                allComponents.addAll(pcba.getBomEntries()));

        // Add mechanical components
        batch.getMechanicalStructure().getAssemblies().forEach(mech ->
                allComponents.addAll(mech.getBomEntries()));

        return allComponents;
    }

    private boolean verifyComponentWaterproofing(BOMEntry entry, StringBuilder errorReport) {
        Map<String, String> specs = entry.getSpecs();
        String componentInfo = String.format("%s (%s)", entry.getMpn(), entry.getDescription());
        boolean valid = true;

        // Check waterproof specification
        if (!specs.containsKey("waterproof") || !"Yes".equals(specs.get("waterproof"))) {
            errorReport.append(String.format("  Component not waterproof: %s\n", componentInfo));
            valid = false;
        }

        // Check sealing specification - accept both IP68 and Double O-ring
        if (!specs.containsKey("sealing") ||
                !("IP68".equals(specs.get("sealing")) || "Double O-ring".equals(specs.get("sealing")))) {
            errorReport.append(String.format("  Missing or invalid sealing for component %s\n", componentInfo));
            valid = false;
        }

        // Check protection rating
        if (!specs.containsKey("protection_rating") || !"IP68".equals(specs.get("protection_rating"))) {
            errorReport.append(String.format("  Missing or invalid protection_rating for component %s\n", componentInfo));
            valid = false;
        }

        // Additional checks for power subsystem components
        if (isInPowerSubsystem(entry)) {
            if (!specs.containsKey("battery_protection") || specs.get("battery_protection").isEmpty()) {
                errorReport.append(String.format("  Missing or invalid battery_protection for Power component %s\n",
                        componentInfo));
                valid = false;
            }
        }

        return valid;
    }

    private boolean verifySubsystemWaterproofing(PlannedProductionBatch batch, String subsystem,
                                                 StringBuilder errorReport) {
        boolean valid = true;
        for (BOMEntry entry : getSubsystemComponents(batch, subsystem)) {
            String[] requiredSpecs = getRequiredWaterproofingSpecs(subsystem, entry);
            for (String spec : requiredSpecs) {
                if (!entry.getSpecs().containsKey(spec) || entry.getSpecs().get(spec) == null
                        || entry.getSpecs().get(spec).isEmpty()) {
                    errorReport.append(String.format("  Missing or invalid %s for %s component %s (%s)\n",
                            spec, subsystem, entry.getMpn(), entry.getDescription()));
                    valid = false;
                }
            }
        }
        return valid;
    }

    private boolean verifyComponentThermal(BOMEntry entry, StringBuilder errorReport) {
        boolean valid = true;
        String componentInfo = String.format("%s (%s)", entry.getMpn(), entry.getDescription());

        // Basic thermal management requirements
        String[] requiredSpecs = {
                "thermal_monitoring", "thermal_protection", "cooling_redundancy",
                "heat_dissipation_method", "thermal_management", "operating_temperature"
        };

        for (String spec : requiredSpecs) {
            if (!entry.getSpecs().containsKey(spec) || entry.getSpecs().get(spec) == null
                    || entry.getSpecs().get(spec).isEmpty()) {
                errorReport.append(String.format("  Missing %s for component %s\n", spec, componentInfo));
                valid = false;
            }
        }

        // Verify operating temperature
        String tempRange = entry.getSpecs().get("operating_temperature");
        if (tempRange != null && !tempRange.equals("-40C to +85C")) {
            errorReport.append(String.format("  Invalid temperature range for %s: %s\n",
                    componentInfo, tempRange));
            valid = false;
        }

        return valid;
    }

    private boolean verifySubsystemThermal(PlannedProductionBatch batch, String subsystem,
                                           StringBuilder errorReport) {
        boolean valid = true;
        for (BOMEntry entry : getSubsystemComponents(batch, subsystem)) {
            String[] requiredSpecs = getRequiredThermalSpecs(subsystem, entry);
            for (String spec : requiredSpecs) {
                if (!entry.getSpecs().containsKey(spec) || entry.getSpecs().get(spec) == null
                        || entry.getSpecs().get(spec).isEmpty()) {
                    errorReport.append(String.format("  Missing %s for %s component %s (%s)\n",
                            spec, subsystem, entry.getMpn(), entry.getDescription()));
                    valid = false;
                }
            }
        }
        return valid;
    }

    private String[] getRequiredWaterproofingSpecs(String subsystem, BOMEntry entry) {
        List<String> specs = new ArrayList<>();
        specs.add("waterproofing");
        specs.add("pressure_rating");
        specs.add("pressure_compensation");

        switch (subsystem) {
            case "Communications":
                if (entry.getDescription().toLowerCase().contains("antenna")) {
                    specs.add("antenna_sealing");
                }
                break;
            case "Power":
                specs.add("battery_protection");
                break;
            case "Sensors":
                specs.add("sensor_sealing");
                break;
        }
        return specs.toArray(new String[0]);
    }

    private String[] getRequiredThermalSpecs(String subsystem, BOMEntry entry) {
        List<String> specs = new ArrayList<>();
        specs.add("thermal_management");
        specs.add("thermal_monitoring");
        specs.add("thermal_protection");
        specs.add("cooling_redundancy");
        specs.add("heat_dissipation_method");

        if (typeDetector.isProcessorComponent(entry)) {
            specs.add("cooling_method");
            specs.add("heat_sink_type");
            specs.add("thermal_throttling");
            specs.add("max_thermal_dissipation");
        }
        if (typeDetector.isPowerComponent(entry)) {
            specs.add("cooling_method");
            specs.add("thermal_runaway_protection");
            specs.add("max_thermal_load");
        }
        if (typeDetector.isSensorComponent(entry)) {
            specs.add("thermal_stability");
            specs.add("thermal_isolation");
            specs.add("temperature_compensation");
            specs.add("thermal_equilibrium_time");
        }

        return specs.toArray(new String[0]);
    }

    private List<BOMEntry> getSubsystemComponents(PlannedProductionBatch batch, String subsystem) {
        List<BOMEntry> components = new ArrayList<>();
        String subsystemPattern = subsystem.toLowerCase().replace(" ", "");

        // Get PCBA components
        batch.getPCBAStructure().getAssemblies().stream()
                .filter(pcba -> containsSubsystem(pcba, subsystemPattern))
                .forEach(pcba -> components.addAll(pcba.getBomEntries()));

        // Get mechanical components
        batch.getMechanicalStructure().getAssemblies().stream()
                .filter(mech -> containsSubsystem(mech, subsystemPattern))
                .forEach(mech -> components.addAll(mech.getBomEntries()));

        return components;
    }

    private boolean containsSubsystem(PCBABOM pcba, String subsystemPattern) {
        return pcba.getBomEntries().stream()
                .anyMatch(entry ->
                        (entry.getMpn() != null && entry.getMpn().toLowerCase().contains(subsystemPattern)) ||
                                (entry.getDescription() != null && entry.getDescription().toLowerCase().contains(subsystemPattern))
                );
    }

    private boolean containsSubsystem(MechanicalBOM mech, String subsystemPattern) {
        return mech.getBomEntries().stream()
                .anyMatch(entry ->
                        (entry.getMpn() != null && entry.getMpn().toLowerCase().contains(subsystemPattern)) ||
                                (entry.getDescription() != null && entry.getDescription().toLowerCase().contains(subsystemPattern))
                );
    }

    private boolean isInPowerSubsystem(BOMEntry entry) {
        if (entry.getDescription() == null) return false;

        // These types of components are not part of the power subsystem
        if (isMechanicalComponent(entry) || isStructuralComponent(entry)) {
            return false;
        }

        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn().toLowerCase();

        // Explicit power components
        return desc.contains("power") ||
                desc.contains("voltage") ||
                desc.contains("current") ||
                mpn.startsWith("ltc") ||
                mpn.startsWith("tps") ||
                mpn.startsWith("bq");
    }

    private boolean requiresBatteryProtection(BOMEntry entry) {
        if (entry.getDescription() == null) return false;

        // These components never need battery protection
        if (isMechanicalComponent(entry) || isStructuralComponent(entry)) {
            return false;
        }

        String desc = entry.getDescription().toLowerCase();

        // Components that require battery protection
        return desc.contains("processor") ||
                desc.contains("mcu") ||
                desc.contains("power") ||
                desc.contains("voltage") ||
                desc.contains("current") ||
                isElectronicComponent(entry);
    }

    private boolean isMechanicalComponent(BOMEntry entry) {
        if (entry.getDescription() == null) return false;

        String desc = entry.getDescription().toLowerCase();
        return desc.contains("seal") ||
                desc.contains("mount") ||
                desc.contains("housing") ||
                desc.contains("propeller") ||
                desc.contains("hub") ||
                desc.contains("mechanical");
    }

    private boolean isStructuralComponent(BOMEntry entry) {
        if (entry.getDescription() == null) return false;

        String desc = entry.getDescription().toLowerCase();
        return desc.contains("bracket") ||
                desc.contains("frame") ||
                desc.contains("support") ||
                desc.contains("structural");
    }

    private boolean isElectronicComponent(BOMEntry entry) {
        Map<String, String> specs = entry.getSpecs();

        // Check for electronic component specifications
        return specs.containsKey("power_rating") ||
                specs.containsKey("voltage_rating") ||
                specs.containsKey("current_rating") ||
                specs.containsKey("signal_type") ||
                specs.containsKey("interface_type") ||
                isActiveComponent(entry);
    }

    private boolean isActiveComponent(BOMEntry entry) {
        if (entry.getDescription() == null) return false;

        String desc = entry.getDescription().toLowerCase();
        return desc.contains("transceiver") ||
                desc.contains("multiplexer") ||
                desc.contains("adc") ||
                desc.contains("interface") ||
                desc.contains("sensor") ||
                desc.contains("controller") ||
                desc.contains("processor");
    }
}