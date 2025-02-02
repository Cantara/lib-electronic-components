package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.BOMEntry;
import java.util.Map;
import java.util.HashMap;

/**
 * Manages component specifications for the submarine system.
 * This class provides methods to add various types of specifications to components.
 */
public class SubmarineComponentSpecs {
    private final ComponentTypeDetector typeDetector;

    public SubmarineComponentSpecs(ComponentTypeDetector typeDetector) {
        this.typeDetector = typeDetector;
    }

    /**
     * Adds basic environmental specifications to a component.
     */
    public void addEnvironmentalSpecs(BOMEntry entry) {
        // Basic environmental specs
        // Basic environmental specifications for ALL components
        addBasicEnvironmentalSpecs(entry);

        // Add component-specific environmental specs
        if (typeDetector.isProcessorComponent(entry)) {
            addProcessorEnvironmentalSpecs(entry);
        } else if (typeDetector.isPowerComponent(entry)) {
            addPowerEnvironmentalSpecs(entry);
        } else if (typeDetector.isSensorComponent(entry)) {
            addSensorEnvironmentalSpecs(entry);
        } else if (typeDetector.isConnectorComponent(entry)) {
            addConnectorEnvironmentalSpecs(entry);
        } else if (typeDetector.isMechanicalComponent(entry)) {
            addMechanicalEnvironmentalSpecs(entry);
        }

        // Add comprehensive specifications
        addBaseThermalSpecs(entry);
        addInterfaceSpecs(entry);
    }

    private void addBasicEnvironmentalSpecs(BOMEntry entry) {
        // Waterproofing specs
        entry.addSpec("waterproof", "Yes");
        entry.addSpec("sealing", "IP68");
        entry.addSpec("protection_rating", "IP68");
        entry.addSpec("environmental_rating", "Submarine Grade");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("max_pressure", "31 bar");
        entry.addSpec("pressure_tested", "Yes");
        entry.addSpec("pressure_compensation", "Oil-filled");

        // Basic protection
        entry.addSpec("conformal_coating", "Type AR");
        entry.addSpec("potting_compound", "Epoxy");
        entry.addSpec("moisture_barrier", "Active");
        entry.addSpec("corrosion_protection", "Applied");

        // Temperature specs
        entry.addSpec("operating_temperature", "-40C to +85C");
        entry.addSpec("storage_temperature", "-55C to +125C");
        entry.addSpec("thermal_cycling", "Qualified");

        // Basic battery protection for all components
        entry.addSpec("battery_protection", "Integrated");
    }

    private void addProcessorEnvironmentalSpecs(BOMEntry entry) {
        entry.addSpec("encapsulation", "Hermetic");
        entry.addSpec("thermal_compound", "High Performance");
        entry.addSpec("heat_spreading", "Enhanced");
        entry.addSpec("junction_protection", "Active");
    }

    private void addPowerEnvironmentalSpecs(BOMEntry entry) {
        entry.addSpec("battery_protection", "Multi-level");
        entry.addSpec("power_isolation", "Galvanic");
        entry.addSpec("thermal_cutoff", "Active");
        entry.addSpec("current_limiting", "Dynamic");
    }

    private void addSensorEnvironmentalSpecs(BOMEntry entry) {
        entry.addSpec("sensor_sealing", "Double barrier");
        entry.addSpec("sensor_protection", "Environmental");
        entry.addSpec("calibration_stability", "Temperature compensated");
        entry.addSpec("moisture_protection", "Active barrier");
    }

    private void addConnectorEnvironmentalSpecs(BOMEntry entry) {
        entry.addSpec("connector_sealing", "Triple barrier");
        entry.addSpec("contact_protection", "Gold plated");
        entry.addSpec("strain_relief", "Integrated");
        entry.addSpec("mating_cycles", "500");
    }

    private void addMechanicalEnvironmentalSpecs(BOMEntry entry) {
        entry.addSpec("material_grade", "Marine");
        entry.addSpec("surface_treatment", "Anti-corrosion");
        entry.addSpec("gasket_system", "Double seal");
        entry.addSpec("assembly_rating", "IP68");
    }

    // Special handling for antenna components
    private void addAntennaEnvironmentalSpecs(BOMEntry entry) {
        entry.addSpec("antenna_sealing", "Molded encapsulation");
        entry.addSpec("radome_material", "Marine grade");
        entry.addSpec("pressure_barrier", "Integrated");
        entry.addSpec("feed_protection", "Waterproof");
    }

    /**
     * Adds basic thermal management specifications to a component.
     */
    public void addBaseThermalSpecs(BOMEntry entry) {
        // Basic thermal management specs
        entry.addSpec("thermal_monitoring", "Real-time temperature sensing");
        entry.addSpec("thermal_protection", "Automatic thermal shutdown");
        entry.addSpec("cooling_redundancy", "Dual cooling system");
        entry.addSpec("heat_dissipation_method", "Forced liquid convection");
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Add type-specific thermal specs
        if (typeDetector.isProcessorComponent(entry)) {
            addProcessorThermalSpecs(entry);
        }
        if (typeDetector.isPowerComponent(entry)) {
            addPowerThermalSpecs(entry);
        }
        if (typeDetector.isSensorComponent(entry)) {
            addSensorThermalSpecs(entry);
        }
        if (typeDetector.isCoolingComponent(entry)) {
            addCoolingThermalSpecs(entry);
        }
        if (typeDetector.isRFComponent(entry)) {
            addRFThermalSpecs(entry);
        }
        if (typeDetector.isMemoryComponent(entry)) {
            addMemoryThermalSpecs(entry);
        }
        if (typeDetector.isConnectorComponent(entry)) {
            addConnectorThermalSpecs(entry);
        }
        if (typeDetector.isMechanicalComponent(entry)) {
            addMechanicalThermalSpecs(entry);
        }
        if (typeDetector.isGateDriverComponent(entry)) {
            addGateDriverThermalSpecs(entry);
        }
        if (typeDetector.isPHYComponent(entry)) {
            addPHYThermalSpecs(entry);
        }
        if (typeDetector.isTransducerComponent(entry)) {
            addTransducerThermalSpecs(entry);
        }
    }

    /**
     * Adds interface specifications to a component.
     */
    public void addInterfaceSpecs(BOMEntry entry) {
        // Network interfaces
        entry.addSpec("network_interface", "Redundant 100BASE-T");
        entry.addSpec("network_speed", "100Mbps");
        entry.addSpec("network_redundancy", "Dual");

        // Power interfaces
        entry.addSpec("power_interface", "Redundant 24V DC");
        entry.addSpec("power_protection", "Short circuit protected");
        entry.addSpec("power_monitoring", "Real-time");

        // Control interfaces
        entry.addSpec("control_interface", "Redundant CAN bus");
        entry.addSpec("control_redundancy", "Dual channel");
        entry.addSpec("feedback_system", "Real-time monitoring");

        // Mechanical interfaces
        entry.addSpec("mechanical_interface", "Standardized rack mount");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");

        // Environmental interfaces
        entry.addSpec("thermal_interface", "Liquid cooling");
        entry.addSpec("pressure_interface", "Oil-filled");
        entry.addSpec("waterproof_interface", "IP68");
    }

    private void addProcessorThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Direct die cooling");
        entry.addSpec("thermal_interface", "Liquid metal");
        entry.addSpec("heat_sink_type", "Micro-channel liquid cooler");
        entry.addSpec("thermal_throttling", "Dynamic frequency scaling");
        entry.addSpec("max_thermal_dissipation", "35W");
        entry.addSpec("thermal_zones", "Multiple independent cooling zones");
        entry.addSpec("emergency_cooling", "Passive backup system");
    }

    private void addPowerThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Forced liquid convection");
        entry.addSpec("heat_dissipation", "Distributed cooling plates");
        entry.addSpec("thermal_runaway_protection", "Active prevention system");
        entry.addSpec("max_thermal_load", "50W");
        entry.addSpec("battery_thermal_control", "Cell-level cooling");
        entry.addSpec("thermal_zones", "Multiple independent zones");
        entry.addSpec("emergency_cooling", "Redundant cooling system");
    }

    private void addSensorThermalSpecs(BOMEntry entry) {
        entry.addSpec("thermal_stability", "±0.1°C");
        entry.addSpec("thermal_isolation", "Vacuum insulated");
        entry.addSpec("temperature_compensation", "Digital calibration");
        entry.addSpec("thermal_equilibrium_time", "<1 minute");
        entry.addSpec("cooling_method", "Precision temperature control");
        entry.addSpec("emergency_cooling", "Passive heat sinking");
    }

    private void addCoolingThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Active liquid cooling");
        entry.addSpec("heat_dissipation", "Micro-channel heat exchanger");
        entry.addSpec("thermal_runaway_protection", "Flow monitoring system");
        entry.addSpec("max_thermal_load", "500W");
        entry.addSpec("thermal_zones", "Multiple independent zones");
        entry.addSpec("emergency_cooling", "Passive backup system");
        entry.addSpec("thermal_stability", "±0.5°C");
        entry.addSpec("thermal_isolation", "Multi-layer isolation");
        entry.addSpec("temperature_compensation", "Real-time adjustment");
        entry.addSpec("thermal_equilibrium_time", "<2 minutes");
    }

    private void addRFThermalSpecs(BOMEntry entry) {
        entry.addSpec("rf_thermal_protection", "High-efficiency heat spreading");
        entry.addSpec("thermal_zones", "RF isolated sections");
        entry.addSpec("emergency_cooling", "Backup passive cooling");
        entry.addSpec("cooling_method", "Liquid cooling");
        entry.addSpec("heat_sink_type", "Copper heat spreader");
        entry.addSpec("thermal_throttling", "Dynamic power adjustment");
        entry.addSpec("max_thermal_dissipation", "25W");
    }

    private void addMemoryThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Indirect liquid cooling");
        entry.addSpec("heat_dissipation", "Thermal pad");
        entry.addSpec("thermal_protection", "Temperature throttling");
        entry.addSpec("max_thermal_dissipation", "10W");
        entry.addSpec("heat_sink_type", "Surface contact cooler");
        entry.addSpec("thermal_throttling", "Access rate control");
    }

    private void addConnectorThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Conduction cooling");
        entry.addSpec("heat_dissipation", "Metallic body conduction");
        entry.addSpec("thermal_runaway_protection", "Temperature monitoring");
        entry.addSpec("max_thermal_load", "10W");
        entry.addSpec("thermal_protection", "Auto-disconnect");
        entry.addSpec("thermal_monitoring", "Contact point sensing");
    }

    private void addMechanicalThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Passive conduction");
        entry.addSpec("heat_dissipation", "Thermal interface material");
        entry.addSpec("thermal_protection", "Material thermal limits");
        entry.addSpec("thermal_monitoring", "Surface temperature sensing");
        entry.addSpec("thermal_stability", "±1.0°C");
        entry.addSpec("thermal_isolation", "Thermal break design");
        entry.addSpec("temperature_compensation", "Material selection");
        entry.addSpec("thermal_equilibrium_time", "<5 minutes");
    }

    private void addGateDriverThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Direct die cooling");
        entry.addSpec("heat_sink_type", "Integrated copper spreader");
        entry.addSpec("thermal_throttling", "Current limiting");
        entry.addSpec("max_thermal_dissipation", "15W");
        entry.addSpec("thermal_protection", "Over-temperature shutdown");
        entry.addSpec("thermal_monitoring", "Junction temperature sensing");
        entry.addSpec("cooling_redundancy", "Dual thermal path");
        entry.addSpec("heat_dissipation_method", "Forced liquid convection");
        entry.addSpec("battery_thermal_control", "Active temperature management");
    }

    private void addPHYThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Direct contact cooling");
        entry.addSpec("heat_sink_type", "Low-profile heat spreader");
        entry.addSpec("thermal_throttling", "Link speed adaptation");
        entry.addSpec("max_thermal_dissipation", "2W");
        entry.addSpec("thermal_protection", "Adaptive power control");
        entry.addSpec("thermal_monitoring", "Internal temperature sensor");
        entry.addSpec("cooling_redundancy", "Dual cooling path");
        entry.addSpec("heat_dissipation_method", "Conduction to PCB");
        entry.addSpec("battery_thermal_control", "Power state control");
    }

    private void addTransducerThermalSpecs(BOMEntry entry) {
        entry.addSpec("thermal_stability", "±0.1°C");
        entry.addSpec("thermal_isolation", "Acoustic isolation barrier");
        entry.addSpec("temperature_compensation", "Digital compensation");
        entry.addSpec("thermal_equilibrium_time", "<2 minutes");
        entry.addSpec("cooling_method", "Active liquid cooling");
        entry.addSpec("thermal_protection", "Over-temperature protection");
        entry.addSpec("thermal_monitoring", "Distributed sensing");
        entry.addSpec("cooling_redundancy", "Dual cooling system");
        entry.addSpec("heat_dissipation_method", "Liquid convection");
        entry.addSpec("battery_thermal_control", "Temperature stabilization");
    }
}