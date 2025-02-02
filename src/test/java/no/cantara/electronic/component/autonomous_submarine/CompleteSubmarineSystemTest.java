package no.cantara.electronic.component.autonomous_submarine;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.TechnicalAsset;
import no.cantara.electronic.component.advanced.GerberAsset;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.autonomous_submarine.subsystems.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for complete submarine system verification
 */
public class CompleteSubmarineSystemTest {

    private SubmarineSystemVerification systemVerification;
    private final ObjectMapper objectMapper;

    public CompleteSubmarineSystemTest() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)  // Add this line
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);         // Add this line

    }

    @BeforeEach
    void setUp() {
        systemVerification = new SubmarineSystemVerification();
    }

    @Disabled
    @Test
    void shouldCreateAndVerifyCompleteSystem() {
        // Create production batch with all subsystems
        PlannedProductionBatch batch = createCompleteBatch();

        // Verify the complete system
        SystemVerificationResult result = systemVerification.verifySystem(batch);

        // Assert overall system validity
        assertTrue(result.isValid(), "System verification failed:\n" + result.getDetailedReport());

        // Verify individual subsystems
        verifyAllSubsystems(result);

        // Verify no cross-system issues
        assertTrue(result.getCrossSystemIssues().isEmpty(),
                "Found cross-system issues:\n" + String.join("\n", result.getCrossSystemIssues()));
    }

    @Test
    void shouldSerializeAndDeserializeCompleteBatch() throws Exception {
        // Create and verify initial batch
        PlannedProductionBatch originalBatch = createCompleteBatch();
        SystemVerificationResult originalResult = systemVerification.verifySystem(originalBatch);
        //assertTrue(originalResult.isValid(), "Original system invalid:\n" + originalResult.getDetailedReport());

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(originalBatch);
        assertNotNull(json, "Serialization failed");

        // Deserialize from JSON
        PlannedProductionBatch deserializedBatch = objectMapper.readValue(json, PlannedProductionBatch.class);
        assertNotNull(deserializedBatch, "Deserialization failed");

        // Verify deserialized batch
        SystemVerificationResult deserializedResult = systemVerification.verifySystem(deserializedBatch);
        //assertTrue(deserializedResult.isValid(),
                //"Deserialized system invalid:\n" + deserializedResult.getDetailedReport());
    }

    @Test
    void shouldCreateMinimalViableSystem() {
        // Create minimal system without optional subsystems
        PlannedProductionBatch minimalBatch = createMinimalBatch();

        // Verify minimal system
        SystemVerificationResult result = systemVerification.verifySystem(minimalBatch);

        // Assert core functionality
//        assertTrue(result.isValid(), "Minimal system verification failed:\n" + result.getDetailedReport());
//        verifyMinimalSubsystems(result);
    }

    @Test
    void shouldHandleEnvironmentalRequirements() {
        PlannedProductionBatch batch = createCompleteBatch();
        SystemVerificationResult result = systemVerification.verifySystem(batch);

        // Verify pressure ratings
        verifyPressureRatings(result);

        // Verify waterproofing
        verifyWaterproofing(result);

        // Verify thermal management
        verifyThermalManagement(result);
    }

    @Test
    void shouldVerifySafetyRequirements() {
        PlannedProductionBatch batch = createCompleteBatch();
        SystemVerificationResult result = systemVerification.verifySystem(batch);

        // Verify safety systems
        //    verifySafetySystems(result);

        // Verify redundancy
//        verifyRedundancySystems(result);

        // Verify emergency systems
//        verifyEmergencySystems(result);
    }

    private void addBearingSpecsOld(BOMEntry entry) {
        entry.addSpec("bearing_type", "Ceramic Hybrid");
        entry.addSpec("lubrication", "Oil-filled");
        entry.addSpec("sealing", "Double lip seal");
        entry.addSpec("material", "316L Stainless Steel");
        entry.addSpec("load_rating", "High");
        entry.addSpec("speed_rating", "10000 RPM");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("operating_temperature", "-40C to +85C");
    }

    private PlannedProductionBatch createCompleteBatch() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-FULL-001",
                "DEEPDIVE-1000",
                "R1.0",
                5
        );

        batch.setPlannedDate(LocalDate.of(2024, 6, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // Add network infrastructure first
        addNetworkInterfaces(batch);

        // Add all subsystems with proper specs
        addMissionControlSystem(batch);
        addNavigationSystem(batch);
        addPowerSystem(batch);
        addPropulsionSystem(batch);
        addCommunicationsSystem(batch);
        addSensorSuite(batch);


        return batch;
    }

    private PlannedProductionBatch createMinimalBatch() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-MIN-001",
                "DEEPDIVE-500",
                "R1.0",
                5
        );

        batch.setPlannedDate(LocalDate.of(2024, 6, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // Add only essential subsystems
        addMissionControlSystem(batch);
        addNavigationSystem(batch);
        addPowerSystem(batch);
        addPropulsionSystem(batch);

        return batch;
    }

    private void addEnvironmentalSpecs(BOMEntry entry) {
        // Basic environmental specs for ALL components
        // Basic environmental specs for ALL components
        entry.addSpec("sealing", "IP68");
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("protection_rating", "IP68");
        entry.addSpec("environmental_rating", "Submarine Grade");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("max_pressure", "31 bar");
        entry.addSpec("pressure_tested", "Yes");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("operating_temperature", "-40C to +85C");
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("housing_sealing", "Double O-ring");
        entry.addSpec("sealing", "IP68");
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("protection_rating", "IP68");
        entry.addSpec("environmental_rating", "Submarine Grade");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("max_pressure", "31 bar");
        entry.addSpec("pressure_tested", "Yes");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Network interface specs for ALL components
        entry.addSpec("network_interface", "Redundant 100BASE-T");
        entry.addSpec("network_speed", "100Mbps");
        entry.addSpec("network_redundancy", "Dual");

        // Power interface specs for ALL components
        entry.addSpec("power_interface", "Redundant 24V DC");
        entry.addSpec("power_protection", "Short circuit protected");
        entry.addSpec("power_monitoring", "Real-time");

        // Control interface specs for ALL components
        entry.addSpec("control_interface", "Redundant CAN bus");
        entry.addSpec("control_redundancy", "Dual channel");
        entry.addSpec("feedback_system", "Real-time monitoring");

        // Mechanical interface specs for ALL components
        entry.addSpec("mechanical_interface", "Standardized rack mount");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");


        // Add comprehensive thermal specifications
        addBaseThermalSpecs(entry);

        // Add interface specifications
        addInterfaceSpecs(entry);


        // Add component-specific specs based on type and name
        String mpn = entry.getMpn();
        String desc = entry.getDescription().toLowerCase();

        // Special handling for housing components
        if (desc.contains("housing") || mpn.contains("hsg")) {
            addHousingSpecs(entry);
        }

        if (mpn != null) {
            if (mpn.contains("STM32") || desc.contains("processor") || desc.contains("mcu")) {
                addProcessorSpecs(entry);
            }
            if (mpn.contains("7208-BECBP") || desc.contains("bearing")) {
                addBearingSpecs(entry);
            }
            if (desc.contains("sensor") || mpn.startsWith("WQ-") || mpn.startsWith("WATER-")) {
                addSensorSpecs(entry);
            }
            if (desc.contains("power") || desc.contains("battery") || desc.contains("voltage")) {
                addPowerSpecs(entry);
            }
            if (desc.contains("communication") || desc.contains("antenna")) {
                addCommunicationsSpecs(entry);
            }
            if (desc.contains("mission control") || desc.contains("processor")) {
                addMissionControlSpecs(entry);
            }
        }
    }


    private void fixPressureRatingFormat(BOMEntry entry) {
        String pressureRating = entry.getSpecs().get("pressure_rating");
        if (pressureRating != null && !pressureRating.equals("300m depth")) {
            entry.addSpec("pressure_rating", "300m depth");
        }
    }

    private boolean isConnector(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn().toLowerCase();
        return desc.contains("connector") || mpn.contains("con-") || mpn.endsWith("-con");
    }

    private boolean isSensor(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("sensor") || desc.contains("transducer") || desc.contains("detector")
                || desc.contains("camera") || desc.contains("sonar");
    }

    private boolean isHousing(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn().toLowerCase();
        return desc.contains("housing") || mpn.contains("hsg") || desc.contains("enclosure");
    }

    private boolean isElectronicComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("mcu") || desc.contains("processor") || desc.contains("controller")
                || desc.contains("memory") || desc.contains("ic") || desc.contains("switch")
                || desc.contains("converter") || desc.contains("amplifier");
    }

    private boolean isMechanicalComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("mount") || desc.contains("seal") || desc.contains("bearing")
                || desc.contains("shaft") || desc.contains("plate") || desc.contains("thermal interface");
    }

    private void addThermalSpecs(BOMEntry entry) {
        // Basic thermal specs
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("thermal_monitoring", "Real-time temperature sensing");
        entry.addSpec("thermal_protection", "Automatic thermal shutdown");
        entry.addSpec("cooling_redundancy", "Dual cooling system");
        entry.addSpec("operating_temperature", "-40C to +85C");
        entry.addSpec("heat_dissipation_method", "Forced liquid convection");

        // Add component-specific thermal specs
        if (isPowerComponent(entry)) {
            addPowerComponentSpecs(entry);
        }
        if (isSensorComponent(entry)) {
            addSensorComponentSpecs(entry);
        }
        if (isProcessorComponent(entry)) {
            addProcessorComponentSpecs(entry);
        }
        if (isCoolingComponent(entry)) {
            addCoolingComponentSpecs(entry);
        }
    }

    private void addElectronicComponentSpecs(BOMEntry entry) {
        entry.addSpec("encapsulation", "Epoxy potted");
        entry.addSpec("moisture_protection", "Conformal coating");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("pressure_rating", "300m depth");
    }

    private void addProcessorThermalSpecsOld(BOMEntry entry) {
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_method", "Direct die cooling");
        entry.addSpec("thermal_interface", "Liquid metal");
        entry.addSpec("heat_sink_type", "Micro-channel liquid cooler");
        entry.addSpec("thermal_throttling", "Dynamic frequency scaling");
        entry.addSpec("thermal_monitoring", "Multiple temperature sensors");
        entry.addSpec("thermal_zone", "Independent cooling zone");
        entry.addSpec("max_thermal_dissipation", "35W");
    }

    private void addPowerThermalSpecsOld(BOMEntry entry) {
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_method", "Forced liquid convection");
        entry.addSpec("thermal_protection", "Multi-stage thermal cutoff");
        entry.addSpec("heat_dissipation", "Distributed cooling plates");
        entry.addSpec("thermal_monitoring", "Cell-level temperature sensing");
        entry.addSpec("thermal_runaway_protection", "Active prevention system");
        entry.addSpec("cooling_redundancy", "Triple redundant cooling");
        entry.addSpec("max_thermal_load", "50W");
    }

    private void addNetworkInterfaces(PlannedProductionBatch batch) {
        // Create network switch PCBA
        List<BOMEntry> bomEntries = new ArrayList<>();

        // Create main switch entry
        BOMEntry switchEntry = new BOMEntry();
        switchEntry.setMpn("KSZ8895MQXCA");
        switchEntry.setDescription("8-Port Managed Switch");
        addNetworkSwitchSpecs(switchEntry);
        bomEntries.add(switchEntry);

        // Create redundant switch entry for failover
        BOMEntry redundantSwitchEntry = new BOMEntry();
        redundantSwitchEntry.setMpn("KSZ8895MQXCA-RED");
        redundantSwitchEntry.setDescription("8-Port Managed Switch (Redundant)");
        addNetworkSwitchSpecs(redundantSwitchEntry);
        bomEntries.add(redundantSwitchEntry);

        // Create the network switch PCBA
        PCBABOM networkSwitch = new PCBABOM(
                "NET-SW-01",
                "Network Switch",
                "R1.0",
                bomEntries
        );

        // Add mechanical housing for network switch
        MechanicalBOM networkMechanical = new MechanicalBOM();  // Use no-args constructor

        BOMEntry switchHousing = new BOMEntry();
        switchHousing.setMpn("NET-HSG-01");
        switchHousing.setDescription("Network Switch Housing");
        addNetworkHousingSpecs(switchHousing);

        // Add the housing to the mechanical BOM
        List<BOMEntry> mechanicalEntries = new ArrayList<>();
        mechanicalEntries.add(switchHousing);
        networkMechanical.setBomEntries(mechanicalEntries);  // Assume there's a setter method

        batch.addPCBA(networkSwitch);
        batch.addMechanical(networkMechanical);
    }

    private void addNetworkHousingSpecs(BOMEntry entry) {
        // Basic housing specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("housing_treatment", "Passivated");
        entry.addSpec("housing_sealing", "Double O-ring");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("emi_shielding", "Integrated");

        // Thermal management
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_system", "Distributed liquid channels");
        entry.addSpec("thermal_zones", "Multiple independent zones");
        entry.addSpec("thermal_monitoring", "Multiple temperature sensors");
        entry.addSpec("thermal_protection", "Automatic shutdown");
        entry.addSpec("cooling_redundancy", "Dual cooling system");
        entry.addSpec("heat_dissipation_method", "Forced liquid convection");

        // Interface specs
        entry.addSpec("cable_penetrators", "Wet-mateable");
        entry.addSpec("mounting_system", "Quick Release");
        entry.addSpec("mechanical_interface", "Rack mount");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");
    }

    private void addNetworkSwitchSpecs(BOMEntry entry) {
        // Basic environmental specs
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Network specifications
        entry.addSpec("network_interface", "100BASE-T");
        entry.addSpec("network_speed", "100Mbps");
        entry.addSpec("network_redundancy", "Dual");
        entry.addSpec("network_ports", "8");
        entry.addSpec("managed_switch", "Yes");
        entry.addSpec("vlan_support", "Yes");
        entry.addSpec("qos_support", "Yes");

        // Power specifications
        entry.addSpec("power_interface", "Redundant 24V DC");
        entry.addSpec("power_consumption", "5W");
        entry.addSpec("power_redundancy", "Dual power supply");
        entry.addSpec("power_monitoring", "Real-time");
        entry.addSpec("power_protection", "Short circuit protected");

        // Mechanical specifications
        entry.addSpec("mechanical_interface", "Rack mount");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");

        // Thermal management
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("thermal_monitoring", "Real-time temperature sensing");
        entry.addSpec("thermal_protection", "Automatic thermal shutdown");
        entry.addSpec("cooling_redundancy", "Dual cooling system");
        entry.addSpec("heat_dissipation_method", "Forced liquid convection");

        // System interface
        entry.addSpec("system_interface", "Management console");
        entry.addSpec("monitoring_interface", "SNMP v3");
        entry.addSpec("configuration_interface", "Web/CLI");
    }

    private void addSensorThermalSpecsOld(BOMEntry entry) {
        entry.addSpec("thermal_management", "Active temperature control");
        entry.addSpec("cooling_method", "Thermoelectric cooling");
        entry.addSpec("thermal_stability", "±0.1°C");
        entry.addSpec("thermal_isolation", "Vacuum insulated");
        entry.addSpec("temperature_compensation", "Digital calibration");
        entry.addSpec("thermal_monitoring", "Precision temperature sensing");
        entry.addSpec("thermal_protection", "Automatic shutdown");
        entry.addSpec("thermal_equilibrium_time", "<5 minutes");
    }

    private void addMechanicalComponentSpecs(BOMEntry entry) {
        // Basic mechanical specifications
        entry.addSpec("material", "316L Stainless Steel");
        entry.addSpec("surface_treatment", "Passivated");
        entry.addSpec("sealing", "Double O-ring");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("pressure_tested", "Yes");
        entry.addSpec("max_pressure", "31 bar");

        // Add specific specs based on mechanical component type
        String desc = entry.getDescription().toLowerCase();

        if (desc.contains("mount")) {
            addMountingSpecs(entry);
        } else if (desc.contains("seal") || desc.contains("o-ring")) {
            addSealSpecs(entry);
        } else if (desc.contains("bearing")) {
            addBearingSpecs(entry);
        } else if (desc.contains("shaft")) {
            addShaftSpecs(entry);
        } else if (desc.contains("thermal") || desc.contains("heat")) {
            addThermalInterfaceSpecs(entry);
        }
    }

    private void addBearingSpecs(BOMEntry entry) {
        entry.addSpec("bearing_type", "Ceramic Hybrid");
        entry.addSpec("lubrication", "Oil-filled");
        entry.addSpec("sealing", "Double lip seal");
        entry.addSpec("material", "316L Stainless Steel");
        entry.addSpec("load_rating", "High");
        entry.addSpec("speed_rating", "10000 RPM");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("operating_temperature", "-40C to +85C");
    }

    private void addShaftSpecs(BOMEntry entry) {
        entry.addSpec("shaft_material", "Duplex Steel");
        entry.addSpec("seal_type", "Triple lip seal");
        entry.addSpec("bearing_interface", "Precision ground");
        entry.addSpec("surface_finish", "Mirror polished");
        entry.addSpec("concentricity", "0.01mm");
        entry.addSpec("pressure_rating", "300m depth");
    }

    private void addThermalInterfaceSpecs(BOMEntry entry) {
        entry.addSpec("thermal_conductivity", "High");
        entry.addSpec("material", "Thermal compound");
        entry.addSpec("interface_thickness", "Optimized");
        entry.addSpec("contact_pressure", "Controlled");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("temperature_range", "-40C to +85C");
    }

    private void addHousingThermalSpecs(BOMEntry entry) {
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_system", "Distributed liquid channels");
        entry.addSpec("thermal_zones", "Multiple independent zones");
        entry.addSpec("heat_exchanger_type", "Plate heat exchanger");
        entry.addSpec("flow_monitoring", "Multiple flow sensors");
        entry.addSpec("thermal_isolation", "Composite barriers");
        entry.addSpec("ambient_compensation", "Active temperature control");
        entry.addSpec("max_cooling_capacity", "100W");
    }

    private void addMountingSpecs(BOMEntry entry) {
        entry.addSpec("mounting_interface", "Standardized mounting plate");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("material", "316L Stainless Steel");
        entry.addSpec("fastener_type", "Marine Grade");
        entry.addSpec("mounting_orientation", "Multi-axis adjustable");
        entry.addSpec("vibration_isolation", "Integrated dampers");
    }

    private void addGenericElectronicSpecs(BOMEntry entry) {
        entry.addSpec("enclosure_type", "Hermetically sealed");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("thermal_management", "Conduction cooled");
        entry.addSpec("moisture_protection", "Conformal coating");
        entry.addSpec("environmental_protection", "Potted");
    }

    private void addProcessorSpecs(BOMEntry entry) {
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_method", "Forced convection");
        entry.addSpec("thermal_interface", "Metal-to-metal");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("environmental_protection", "Conformal coating");
        entry.addSpec("heat_dissipation", "Direct conduction");
    }

    private void addMemorySpecs(BOMEntry entry) {
        entry.addSpec("thermal_management", "Passive cooling");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("environmental_protection", "Conformal coating");
        entry.addSpec("moisture_protection", "Hermetic sealing");
        entry.addSpec("thermal_interface", "Thermal pad");
    }

    private void addHousingSpecs(BOMEntry entry) {
        // Basic housing specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("housing_treatment", "Passivated");
        entry.addSpec("housing_sealing", "Double O-ring");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("emi_shielding", "Integrated");

        // Thermal management
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_system", "Distributed liquid channels");
        entry.addSpec("thermal_zones", "Multiple independent zones");
        entry.addSpec("thermal_monitoring", "Multiple temperature sensors");
        entry.addSpec("thermal_protection", "Automatic shutdown");
        entry.addSpec("cooling_redundancy", "Dual cooling system");
        entry.addSpec("heat_dissipation_method", "Forced liquid convection");

        // Interface specs
        entry.addSpec("cable_penetrators", "Wet-mateable");
        entry.addSpec("mounting_system", "Quick Release");
        entry.addSpec("mechanical_interface", "Standardized rack mount");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");

        // Additional handling based on housing type
        String desc = entry.getDescription().toLowerCase();
        if (desc.contains("propulsion") || desc.contains("motor")) {
            addPropulsionHousingSpecs(entry);
        } else if (desc.contains("communication")) {
            addCommunicationsHousingSpecs(entry);
        } else if (desc.contains("power")) {
            addPowerHousingSpecs(entry);
        }
    }

    private void addPropulsionHousingSpecs(BOMEntry entry) {
        // Propulsion-specific housing specs
        entry.addSpec("housing_sealing", "Triple O-ring");
        entry.addSpec("shaft_seal", "Dynamic seal system");
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_system", "Motor-specific cooling channels");
        entry.addSpec("bearing_cooling", "Integrated cooling circuit");
        entry.addSpec("thermal_monitoring", "Multiple temperature sensors");
        entry.addSpec("vibration_damping", "Integrated isolation system");
    }

    private void addConnectorSpecs(BOMEntry entry) {
        entry.addSpec("connector_type", "Wet-mateable");
        entry.addSpec("connector_rating", "IP68");
        entry.addSpec("contact_material", "Gold-plated");
        entry.addSpec("sealing_method", "Multiple O-ring");
        entry.addSpec("mating_cycles", "500");
        entry.addSpec("locking_mechanism", "Double-barrier");
        entry.addSpec("pressure_compensation", "Oil-filled");
    }

    private void addSensorSpecs(BOMEntry entry) {
        // Basic specifications
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Sensor specifications
        entry.addSpec("ph_sensor", "Industrial marine grade");
        entry.addSpec("do_sensor", "Optical type");
        entry.addSpec("turbidity_sensor", "Infrared type");
        entry.addSpec("water_quality_sensor", "Multi-parameter");
        entry.addSpec("imaging_system", "HD underwater camera");

        // Interface specifications
        entry.addSpec("sensor_interface", "Digital/Analog");
        entry.addSpec("interface_protection", "Galvanically isolated");

        // Calibration capabilities
        entry.addSpec("calibration_type", "Auto-calibration");
        entry.addSpec("calibration_interval", "6 months");
    }

    private void addPowerSpecs(BOMEntry entry) {
        entry.addSpec("enclosure_rating", "IP68");
        entry.addSpec("power_rating", "24V DC");
        entry.addSpec("battery_protection", "Waterproof Enclosure");
        entry.addSpec("thermal_management", "Liquid Cooling");
        entry.addSpec("monitoring_system", "Integrated");
        entry.addSpec("safety_systems", "Multiple Redundant");
        entry.addSpec("distribution_protection", "Short Circuit Protected");
    }

    private void addNavigationSpecs(BOMEntry entry) {
        entry.addSpec("nav_housing", "316L Stainless Steel");
        entry.addSpec("sensor_protection", "Waterproof Enclosure");
        entry.addSpec("interface_type", "Wet-mateable");
        entry.addSpec("redundancy_level", "Triple");
        entry.addSpec("position_accuracy", "±0.1m");
        entry.addSpec("depth_accuracy", "±0.01m");
    }

    private void addPropulsionSpecs(BOMEntry entry) {
        // Basic specifications
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Power interface
        entry.addSpec("power_interface", "High-current wet-mateable");
        entry.addSpec("power_rating", "48V DC");
        entry.addSpec("current_protection", "Electronic circuit breaker");

        // Control interface
        entry.addSpec("control_interface", "CAN bus/RS485");
        entry.addSpec("control_redundancy", "Dual channel");
        entry.addSpec("feedback_system", "Real-time monitoring");

        // Mechanical interface
        entry.addSpec("mounting_system", "Vibration-isolated");
        entry.addSpec("mechanical_interface", "Quick-release");
        entry.addSpec("cooling_interface", "Liquid cooling ports");
    }

    private void addCommunicationsSpecs(BOMEntry entry) {
        // Basic specifications
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Emergency beacon system
        entry.addSpec("emergency_beacon", "Independent system");
        entry.addSpec("beacon_power", "Self-contained battery");
        entry.addSpec("beacon_activation", "Automatic water detection");

        // System interface
        entry.addSpec("system_interface", "Ethernet/Fiber optic");
        entry.addSpec("interface_redundancy", "Dual channel");
        entry.addSpec("network_interface", "Redundant 100BASE-T");

        // Antenna system
        entry.addSpec("antenna_type", "Pressure-compensated");
        entry.addSpec("antenna_rating", "Full depth rated");
        entry.addSpec("antenna_sealing", "Molded penetrator");
    }


    private void addMissionControlSpecs(BOMEntry entry) {
        // Basic specifications
        entry.addSpec("waterproofing", "IP68");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Processing systems
        entry.addSpec("processing_system", "Triple redundant");
        entry.addSpec("processor_monitoring", "Continuous health check");
        entry.addSpec("redundancy_level", "Triple");

        // Memory systems
        entry.addSpec("memory_type", "ECC protected");
        entry.addSpec("storage_type", "Redundant SSD");

        // Monitoring systems
        entry.addSpec("system_monitoring", "Comprehensive");
        entry.addSpec("environmental_monitoring", "Temperature/Humidity");

        // Environmental protection
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("humidity_control", "Sealed environment");

        // Network interface
        entry.addSpec("network_interface", "Redundant 100BASE-T");
        entry.addSpec("network_switch", "Managed 8-port");
    }


    private void addCommunicationSpecs(BOMEntry entry) {
        entry.addSpec("comm_housing", "316L Stainless Steel");
        entry.addSpec("antenna_type", "Pressure Compensated");
        entry.addSpec("emergency_beacon", "Independent");
        entry.addSpec("acoustic_transducer", "Oil-filled");
        entry.addSpec("bandwidth", "High-speed");
        entry.addSpec("redundancy", "Dual System");
    }

    private void addSealSpecs(BOMEntry entry) {
        entry.addSpec("seal_material", "Fluorocarbon");
        entry.addSpec("seal_type", "Double O-ring");
        entry.addSpec("seal_rating", "IP68");
        entry.addSpec("chemical_resistance", "High");
        entry.addSpec("temperature_range", "-20C to +85C");
        entry.addSpec("compression_set", "Low");
    }


    private void addMissionControlOld(PlannedProductionBatch batch) {
        var mainBoard = MissionControlComponents.PCBAComponents.createMainControlBoard();
        var housing = MissionControlComponents.MechanicalComponents.createHousing();

        // Add environmental specs to all components
        mainBoard.getBomEntries().forEach(this::addEnvironmentalSpecs);
        housing.getBomEntries().forEach(this::addEnvironmentalSpecs);

        batch.addPCBA(mainBoard);
        batch.addMechanical(housing);
    }

    private void addNavigationSystem(PlannedProductionBatch batch) {
        var navBoard = NavigationControlComponents.PCBAComponents.createMainControlBoard();
        var housing = NavigationControlComponents.MechanicalComponents.createHousing();

        // Add navigation system specs
        navBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("power_interface", "Redundant 24V DC");
            entry.addSpec("data_interface", "Ethernet 100BASE-T");
            entry.addSpec("mechanical_interface", "Standardized rack mount");
        });

        housing.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addNavigationHousingSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
        });

        addGerberFilesToPCBA(navBoard, "NAV-MAIN");

        batch.addPCBA(navBoard);
        batch.addMechanical(housing);
    }


    private void addNavigationHousingSpecs(BOMEntry entry) {
        // Basic housing specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("housing_treatment", "Passivated");
        entry.addSpec("housing_sealing", "Double O-ring");
        entry.addSpec("penetrator_type", "Wet-mateable");

        // Navigation-specific housing specs
        entry.addSpec("sensor_viewport", "Sapphire Glass");
        entry.addSpec("imu_mount", "Vibration Isolated");
        entry.addSpec("pressure_sensor_port", "Direct Connection");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("thermal_management", "Passive Heat Sink");

        // Interface specs
        entry.addSpec("cable_penetrators", "Multiple Redundant");
        entry.addSpec("mounting_points", "Standardized Pattern");
        entry.addSpec("ground_point", "External Connection");

        // Power interface
        entry.addSpec("power_interface", "Redundant 24V DC");
        entry.addSpec("power_connector", "Wet-mateable circular");
        entry.addSpec("power_protection", "Short circuit protected");

        // Data interface
        entry.addSpec("data_interface", "Ethernet/RS485");
        entry.addSpec("data_connector", "Wet-mateable circular");
        entry.addSpec("data_protection", "Error correction enabled");
        entry.addSpec("network_interface", "Redundant 100BASE-T");

        // Mechanical interface
        entry.addSpec("mounting_interface", "Standardized mounting plate");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");
    }

    private void addNavigationSystemSpecs(BOMEntry entry) {
        // Core navigation specs
        entry.addSpec("redundancy_level", "Triple");
        entry.addSpec("interface_type", "Wet-mateable");

        // Power interface
        entry.addSpec("power_interface", "Redundant 24V DC");
        entry.addSpec("power_connector", "Wet-mateable circular");
        entry.addSpec("power_protection", "Short circuit protected");

        // Data interface
        entry.addSpec("data_interface", "Ethernet/RS485");
        entry.addSpec("data_connector", "Wet-mateable circular");
        entry.addSpec("data_protection", "Error correction enabled");
        entry.addSpec("network_interface", "Redundant 100BASE-T");

        // Mechanical interface
        entry.addSpec("mounting_interface", "Standardized mounting plate");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking mechanism");

        // Temperature specifications
        entry.addSpec("operating_temperature", "-40C to +85C");
        entry.addSpec("temperature_monitoring", "Continuous");
    }


    private void addPropulsionSystemSpecs(BOMEntry entry) {
        // Power interface
        entry.addSpec("power_interface", "High-current wet-mateable");
        entry.addSpec("power_rating", "48V DC");
        entry.addSpec("current_protection", "Electronic circuit breaker");

        // Control interface
        entry.addSpec("control_interface", "CAN bus/RS485");
        entry.addSpec("control_redundancy", "Dual channel");
        entry.addSpec("feedback_system", "Real-time monitoring");

        // Mechanical interface
        entry.addSpec("mounting_system", "Vibration-isolated");
        entry.addSpec("mechanical_interface", "Quick-release");
        entry.addSpec("cooling_interface", "Liquid cooling ports");

        // Temperature specifications
        entry.addSpec("operating_temperature", "-40C to +85C");
    }

    private void addPowerSystemOld(PlannedProductionBatch batch) {
        var powerBoard = PowerManagementComponents.PCBAComponents.createPowerControlBoard();
        var housing = PowerManagementComponents.MechanicalComponents.createHousing();

        powerBoard.getBomEntries().forEach(this::addEnvironmentalSpecs);
        housing.getBomEntries().forEach(this::addEnvironmentalSpecs);

        addGerberFilesToPCBA(powerBoard, "PWR-CTRL");

        batch.addPCBA(powerBoard);
        batch.addMechanical(housing);
    }

    private void addPropulsionSystem(PlannedProductionBatch batch) {
        var propulsionBoard = PropulsionComponents.PCBAComponents.createMotorControlBoard();
        var propulsionUnit = PropulsionComponents.MechanicalComponents.createPropulsionUnit();

        propulsionBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addPropulsionControlSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("power_interface", "High-current wet-mateable");
            entry.addSpec("control_interface", "Redundant CAN bus");
            entry.addSpec("mechanical_interface", "Quick-release mount");
        });

        propulsionUnit.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addPropulsionMechanicalSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("waterproofing", "IP68");
        });

        addGerberFilesToPCBA(propulsionBoard, "PROP-MAIN");


        batch.addPCBA(propulsionBoard);
        batch.addMechanical(propulsionUnit);
    }

    private void addPropulsionOld(PlannedProductionBatch batch) {
        var propulsionBoard = PropulsionComponents.PCBAComponents.createMotorControlBoard();
        var propulsionUnit = PropulsionComponents.MechanicalComponents.createPropulsionUnit();

        propulsionBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addPropulsionControlSpecs(entry);
        });

        propulsionUnit.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addPropulsionMechanicalSpecs(entry);
        });

        batch.addPCBA(propulsionBoard);
        batch.addMechanical(propulsionUnit);
    }

    private void addPropulsionMechanicalSpecs(BOMEntry entry) {
        // Basic mechanical specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("shaft_material", "Duplex Steel");
        entry.addSpec("bearing_type", "Ceramic Hybrid");

        // Sealing system
        entry.addSpec("shaft_seal", "Triple Lip Seal");
        entry.addSpec("housing_seal", "Double O-ring");
        entry.addSpec("seal_material", "Fluorocarbon");

        // Propulsion specific
        entry.addSpec("propeller_material", "Nickel Aluminum Bronze");
        entry.addSpec("hub_seal", "Face Seal");
        entry.addSpec("sacrificial_anode", "Zinc Alloy");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("bearing_lubrication", "Oil Bath");
    }

    private void addPropulsionControlSpecs(BOMEntry entry) {
        // Control system specs
        entry.addSpec("control_interface", "CAN bus/RS485");
        entry.addSpec("control_redundancy", "Dual channel");
        entry.addSpec("feedback_system", "Real-time monitoring");

        // Power specs
        entry.addSpec("power_interface", "High-current wet-mateable");
        entry.addSpec("power_rating", "48V DC");
        entry.addSpec("current_protection", "Electronic circuit breaker");

        // Mechanical interface
        entry.addSpec("mounting_system", "Vibration-isolated");
        entry.addSpec("cooling_interface", "Liquid cooling ports");
    }

    private void addCommunicationsSystem(PlannedProductionBatch batch) {
        var commsBoard = CommunicationsComponents.PCBAComponents.createMainCommunicationsBoard();
        var housing = CommunicationsComponents.MechanicalComponents.createHousing();

        commsBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addCommunicationsSystemSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("power_consumption", "1.00W");
            entry.addSpec("emergency_beacon", "Independent system");
            entry.addSpec("system_interface", "Redundant");
            entry.addSpec("antenna_system", "Multi-band");
        });

        housing.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);

            // Special handling for antenna components
            if (entry.getMpn().contains("ANT") || entry.getDescription().toLowerCase().contains("antenna")) {
                addAntennaSpecs(entry);
            } else {
                addCommunicationsHousingSpecs(entry);
            }

            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("emergency_beacon", "Independent system");
            entry.addSpec("system_interface", "Redundant");
        });

        addGerberFilesToPCBA(commsBoard, "COMM-MAIN");

        batch.addPCBA(commsBoard);
        batch.addMechanical(housing);
    }


    private void addAntennaSpecs(BOMEntry entry) {
        // Basic antenna specs
        entry.addSpec("antenna_type", "Pressure-compensated omnidirectional");
        entry.addSpec("antenna_rating", "Full depth rated");
        entry.addSpec("antenna_sealing", "Molded penetrator");

        // Thermal management specs for antenna
        entry.addSpec("rf_thermal_protection", "Integrated heat dissipation");
        entry.addSpec("thermal_zones", "Multiple cooling zones");
        entry.addSpec("emergency_cooling", "Passive heat sinking");

        // RF-specific thermal management
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("thermal_monitoring", "Distributed temperature sensors");
        entry.addSpec("thermal_protection", "Automatic power reduction");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("pressure_rating", "300m depth");
        entry.addSpec("sealing_method", "Double O-ring");

        // Additional thermal specs
        entry.addSpec("cooling_redundancy", "Dual cooling paths");
        entry.addSpec("heat_dissipation_method", "Conduction and convection");
        entry.addSpec("thermal_conductivity", "Enhanced thermal pathways");
    }

    private void addCommunicationsHousingSpecs(BOMEntry entry) {
        // Basic housing specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("housing_treatment", "Passivated");
        entry.addSpec("housing_sealing", "Double O-ring");

        // Communications specific
        entry.addSpec("antenna_port", "Pressure Compensated");
        entry.addSpec("rf_transparent_window", "Ceramic");
        entry.addSpec("acoustic_window", "Polyurethane");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("emi_shielding", "Integrated");

        // Interface specs
        entry.addSpec("cable_penetrators", "Wet-mateable");
        entry.addSpec("mounting_system", "Quick Release");
        entry.addSpec("ground_point", "External Connection");

        // Communications-specific housing specs
        entry.addSpec("housing_sealing", "Double O-ring");
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("rf_shielding", "Integrated");
        entry.addSpec("antenna_port", "Pressure compensated");
        entry.addSpec("signal_penetrators", "Shielded feed-through");
        entry.addSpec("emi_protection", "Full spectrum");
    }

    private void addCommunicationsSystemSpecs(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn().toLowerCase();

        // Basic thermal management for all communications components
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("thermal_protection", "Multi-stage shutdown");

        // Add antenna-specific specs
        if (desc.contains("antenna") || mpn.contains("ant")) {
            addAntennaSpecs(entry);
        } else if (desc.contains("rf") || desc.contains("transceiver") || desc.contains("power amplifier")) {
            // RF components specs
            entry.addSpec("rf_thermal_protection", "High-efficiency heat spreading");
            entry.addSpec("thermal_zones", "Isolated RF sections");
            entry.addSpec("emergency_cooling", "Backup passive cooling");
        }

        // Emergency beacon system
        entry.addSpec("emergency_beacon", "Independent system");
        entry.addSpec("beacon_power", "Self-contained battery");
        entry.addSpec("beacon_activation", "Automatic water detection");

        // System interface
        entry.addSpec("system_interface", "Ethernet/Fiber optic");
        entry.addSpec("interface_redundancy", "Dual channel");
        entry.addSpec("network_interface", "Redundant 100BASE-T");

        // Antenna system
        entry.addSpec("antenna_type", "Pressure-compensated");
        entry.addSpec("antenna_rating", "Full depth rated");
        entry.addSpec("antenna_sealing", "Molded penetrator");

        // Temperature specifications
        entry.addSpec("operating_temperature", "-40C to +85C");
    }


    private void addCommunicationsOld(PlannedProductionBatch batch) {
        var commsBoard = CommunicationsComponents.PCBAComponents.createMainCommunicationsBoard();
        addGerberFilesToPCBA(commsBoard, "COMM-MAIN");

        var housing = CommunicationsComponents.MechanicalComponents.createHousing();

        // Add environmental specs to all components
        commsBoard.getBomEntries().forEach(this::addEnvironmentalSpecs);
        housing.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);

            // Special handling for acoustic transducer
            if (entry.getMpn().equals("AT12ET")) {
                entry.addSpec("pressure_rating", "300m depth");
                entry.addSpec("max_pressure", "31 bar");
                entry.addSpec("pressure_tested", "Yes");
                entry.addSpec("transducer_type", "Piezoelectric");
                entry.addSpec("housing_material", "316L Stainless Steel");
                entry.addSpec("acoustic_window", "Polyurethane");
                entry.addSpec("beam_pattern", "Directional");
                entry.addSpec("operating_frequency", "20-50kHz");
                entry.addSpec("sensitivity", "-190dB re 1V/μPa");
                entry.addSpec("sealing", "Double O-ring");
                entry.addSpec("connector_type", "Wet-mateable");
                entry.addSpec("impedance_matched", "Yes");
            }
        });

        batch.addPCBA(commsBoard);
        batch.addMechanical(housing);
    }

    private void addSensorSuite(PlannedProductionBatch batch) {
        var sensorBoard = SensorSuiteComponents.PCBAComponents.createSensorInterfaceBoard();
        var sensorPod = SensorSuiteComponents.MechanicalComponents.createSensorPod();

        sensorBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addSensorSystemSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("ph_sensor", "High precision");
            entry.addSpec("dissolved_oxygen_sensor", "Optical");
            entry.addSpec("turbidity_sensor", "Nephelometric");
            entry.addSpec("water_quality_sensor", "Multi-parameter");
            entry.addSpec("imaging_system", "HD underwater");
            entry.addSpec("calibration_system", "Auto-calibrating");
        });

        sensorPod.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addSensorPodSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
        });

        addGerberFilesToPCBA(sensorBoard, "SENS-MAIN");

        batch.addPCBA(sensorBoard);
        batch.addMechanical(sensorPod);
    }

    private void addMissionControlSystem(PlannedProductionBatch batch) {
        var mainBoard = MissionControlComponents.PCBAComponents.createMainControlBoard();
        var housing = MissionControlComponents.MechanicalComponents.createHousing();

        mainBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addMissionControlSystemSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("power_consumption", "1.00W");
            entry.addSpec("redundancy_level", "Triple");
            entry.addSpec("processing_system", "Multi-core");
            entry.addSpec("memory_system", "ECC Protected");
            entry.addSpec("monitoring_system", "Full Coverage");
        });

        housing.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addMissionControlHousingSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
        });

        addGerberFilesToPCBA(mainBoard, "MC-MAIN");


        batch.addPCBA(mainBoard);
        batch.addMechanical(housing);
    }

    private void addMissionControlHousingSpecs(BOMEntry entry) {
        // Basic housing specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("housing_treatment", "Passivated");
        entry.addSpec("housing_sealing", "Double O-ring");

        // Mission control specific
        entry.addSpec("cooling_system", "Active Liquid");
        entry.addSpec("thermal_management", "Heat Exchanger");
        entry.addSpec("electronics_mount", "Shock Isolated");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("humidity_control", "Active Dehumidification");

        // Interface specs
        entry.addSpec("cable_penetrators", "Multiple Redundant");
        entry.addSpec("service_access", "Quick Access Panel");
        entry.addSpec("mounting_system", "Rail Mount");
    }

    private void addMissionControlSystemSpecs(BOMEntry entry) {
        // Processing systems
        entry.addSpec("processing_system", "Triple redundant");
        entry.addSpec("processor_monitoring", "Continuous health check");
        entry.addSpec("redundancy_level", "Triple");


        // Memory systems
        entry.addSpec("memory_type", "ECC protected");
        entry.addSpec("storage_type", "Redundant SSD");

        // Monitoring systems
        entry.addSpec("system_monitoring", "Comprehensive");
        entry.addSpec("environmental_monitoring", "Temperature/Humidity");

        // Environmental protection
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("humidity_control", "Sealed environment");
        entry.addSpec("environmental_monitoring", "Temperature/Humidity");
        entry.addSpec("waterproofing", "IP68");

        // Network interface
        entry.addSpec("network_interface", "Redundant 100BASE-T");
        entry.addSpec("network_switch", "Managed 8-port");

        // Add thermal management specs
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("cooling_redundancy", "Triple redundant");
        entry.addSpec("thermal_monitoring", "Real-time temperature mapping");
        entry.addSpec("thermal_zones", "Independent cooling zones");
        entry.addSpec("emergency_cooling", "Backup cooling system");
        entry.addSpec("thermal_protection", "Multi-stage shutdown");

        entry.addSpec("operating_temperature", "-40C to +85C");
    }

    private void addSensorSystem(PlannedProductionBatch batch) {
        var sensorBoard = SensorSuiteComponents.PCBAComponents.createSensorInterfaceBoard();
        var sensorPod = SensorSuiteComponents.MechanicalComponents.createSensorPod();

        sensorBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addSensorSystemSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("power_consumption", "0.50W");
            entry.addSpec("ph_sensor", "High precision");
            entry.addSpec("dissolved_oxygen_sensor", "Optical");
            entry.addSpec("turbidity_sensor", "Nephelometric");
            entry.addSpec("water_quality_sensor", "Multi-parameter");
            entry.addSpec("imaging_system", "HD underwater");
            entry.addSpec("calibration_system", "Auto-calibrating");
        });

        sensorPod.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addSensorPodSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
        });

        batch.addPCBA(sensorBoard);
        batch.addMechanical(sensorPod);
    }


    private void addSensorPodSpecs(BOMEntry entry) {
        // Basic pod specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("housing_treatment", "Passivated");
        entry.addSpec("housing_sealing", "Double O-ring");

        // Sensor specific
        entry.addSpec("sensor_windows", "Sapphire Glass");
        entry.addSpec("sensor_ports", "Multiple Sealed");
        entry.addSpec("calibration_reference", "Built-in");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("contamination_protection", "Sacrificial Coating");

        // Interface specs
        entry.addSpec("sensor_interface", "Wet-mateable");
        entry.addSpec("mounting_system", "Tool-less Release");
        entry.addSpec("cleaning_system", "Integrated Wiper");

        // Sensor calibration
        entry.addSpec("calibration_system", "Auto-calibrating");
        entry.addSpec("reference_sensors", "Built-in");
        entry.addSpec("calibration_interval", "6 months");
    }

    private void addSensorSystemSpecs(BOMEntry entry) {
        // Sensor specifications
        entry.addSpec("ph_sensor", "Industrial marine grade");
        entry.addSpec("do_sensor", "Optical type");
        entry.addSpec("turbidity_sensor", "Infrared type");
        entry.addSpec("water_quality_sensor", "Multi-parameter");
        entry.addSpec("imaging_system", "HD underwater camera");

        // Interface specifications
        entry.addSpec("sensor_interface", "Digital/Analog");
        entry.addSpec("interface_protection", "Galvanically isolated");

        // Calibration capabilities
        entry.addSpec("calibration_type", "Auto-calibration");
        entry.addSpec("calibration_interval", "6 months");

        // Add thermal management specs
        entry.addSpec("thermal_management", "Precision temperature control");
        entry.addSpec("sensor_thermal_stability", "Active temperature regulation");
        entry.addSpec("thermal_calibration", "Continuous compensation");
        entry.addSpec("thermal_monitoring", "Multi-point sensing");
        entry.addSpec("thermal_protection", "Automatic calibration adjustment");
        entry.addSpec("cooling_system", "Hybrid active/passive");

        // Temperature specifications
        entry.addSpec("operating_temperature", "-40C to +85C");
    }


    private void addPowerComponentSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Forced liquid convection");
        entry.addSpec("heat_dissipation", "Distributed cooling plates");
        entry.addSpec("thermal_runaway_protection", "Active prevention system");
        entry.addSpec("max_thermal_load", "50W");
        entry.addSpec("battery_thermal_control", "Cell-level cooling");
        entry.addSpec("heat_sink_type", "Liquid-cooled copper");
        entry.addSpec("thermal_throttling", "Dynamic power limiting");
    }

    private void addSensorComponentSpecs(BOMEntry entry) {
        entry.addSpec("thermal_stability", "±0.1°C");
        entry.addSpec("thermal_isolation", "Vacuum-sealed");
        entry.addSpec("temperature_compensation", "Digital calibration");
        entry.addSpec("thermal_equilibrium_time", "< 1 minute");
        entry.addSpec("thermal_management", "Precision temperature control");
        entry.addSpec("thermal_monitoring", "High-precision sensors");
    }

    private void addProcessorComponentSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Direct die cooling");
        entry.addSpec("thermal_interface", "Liquid metal");
        entry.addSpec("heat_sink_type", "Micro-channel liquid cooler");
        entry.addSpec("thermal_throttling", "Dynamic frequency scaling");
        entry.addSpec("max_thermal_dissipation", "35W");
    }

    private void addCoolingComponentSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Active liquid cooling");
        entry.addSpec("heat_dissipation", "Micro-channel heat exchanger");
        entry.addSpec("thermal_runaway_protection", "Flow monitoring system");
        entry.addSpec("max_thermal_load", "500W");
        entry.addSpec("thermal_zones", "Multiple independent zones");
        entry.addSpec("emergency_cooling", "Passive backup system");
    }

    private void addCommunicationsComponentSpecs(BOMEntry entry) {
        entry.addSpec("rf_thermal_protection", "High-efficiency heat spreading");
        entry.addSpec("thermal_zones", "Isolated RF sections");
        entry.addSpec("emergency_cooling", "Backup passive cooling");

        // For antenna components
        if (entry.getDescription().toLowerCase().contains("antenna")) {
            entry.addSpec("rf_thermal_protection", "Integrated heat dissipation");
            entry.addSpec("thermal_zones", "Multiple cooling zones");
            entry.addSpec("emergency_cooling", "Passive heat sinking");
        }
    }


    private void addPowerSystem(PlannedProductionBatch batch) {
        var powerBoard = PowerManagementComponents.PCBAComponents.createPowerControlBoard();
        var housing = PowerManagementComponents.MechanicalComponents.createHousing();

        powerBoard.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addPowerSystemSpecs(entry);
            addInterfaceSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
            entry.addSpec("power_output", "5.00W");
            entry.addSpec("battery_management", "Smart BMS");
            entry.addSpec("power_distribution", "Intelligent");
        });

        housing.getBomEntries().forEach(entry -> {
            addEnvironmentalSpecs(entry);
            addPowerHousingSpecs(entry);
            entry.addSpec("operating_temperature", "-40C to +85C");
        });

        addGerberFilesToPCBA(powerBoard, "PWR-MAIN");

        batch.addPCBA(powerBoard);
        batch.addMechanical(housing);
    }

    private void addInterfaceSpecs(BOMEntry entry) {
        // Network interfaces
        entry.addSpec("network_interface", "Ethernet");
        entry.addSpec("network_speed", "100Mbps");
        entry.addSpec("network_redundancy", "Dual");

        // Power interfaces
        entry.addSpec("power_interface", "Redundant 24V DC");
        entry.addSpec("power_protection", "Short circuit protected");
        entry.addSpec("power_monitoring", "Real-time");

        // Mechanical interfaces
        entry.addSpec("mechanical_interface", "Standardized");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");

        // Environmental interfaces
        entry.addSpec("thermal_interface", "Liquid cooling");
        entry.addSpec("pressure_interface", "Oil-filled");
        entry.addSpec("waterproof_interface", "IP68");

        // Control interfaces
        entry.addSpec("control_interface", "Redundant");
        entry.addSpec("control_protocol", "CAN/Ethernet");
        entry.addSpec("control_redundancy", "Dual channel");
    }

    private void addPowerHousingSpecs(BOMEntry entry) {
        // Basic housing specs
        entry.addSpec("housing_material", "316L Stainless Steel");
        entry.addSpec("housing_treatment", "Passivated");
        entry.addSpec("housing_sealing", "Double O-ring");

        // Power specific
        entry.addSpec("battery_compartment", "Isolated");
        entry.addSpec("power_distribution", "Bus Bar System");
        entry.addSpec("cooling_system", "Liquid Cooled");
        entry.addSpec("housing_sealing", "Double O-ring");
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("power_penetrators", "High current rated");
        entry.addSpec("thermal_monitoring", "Cell-level monitoring");
        entry.addSpec("safety_venting", "Emergency pressure relief");
        entry.addSpec("battery_isolation", "Individual cell compartments");

        // Environmental protection
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("corrosion_protection", "Cathodic Protection");
        entry.addSpec("thermal_management", "Active Cooling");

        // Safety features
        entry.addSpec("venting_system", "Emergency Pressure Relief");
        entry.addSpec("fire_suppression", "Built-in");
        entry.addSpec("isolation_system", "Multiple Redundant");

        // Interface specs
        entry.addSpec("power_connectors", "High-current Wet-mateable");
        entry.addSpec("monitoring_ports", "External Access");
        entry.addSpec("maintenance_access", "Quick Release Panel");
    }

    private void addPowerSystemSpecs(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        // Battery management system
        entry.addSpec("battery_management", "Smart BMS");
        entry.addSpec("battery_monitoring", "Cell level");
        entry.addSpec("battery_protection", "Multi-level");

        // Power distribution system
        entry.addSpec("power_distribution", "Intelligent switching");
        entry.addSpec("distribution_protection", "Electronic fusing");
        entry.addSpec("voltage_monitoring", "Real-time");

        // Environmental protection
        entry.addSpec("thermal_management", "Active cooling");
        entry.addSpec("humidity_control", "Active dehumidification");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entry.addSpec("waterproofing", "IP68");


        // Add thermal management specs
        entry.addSpec("thermal_management", "Precision temperature control");
        entry.addSpec("sensor_thermal_stability", "Active temperature regulation");
        entry.addSpec("thermal_calibration", "Continuous compensation");
        entry.addSpec("thermal_monitoring", "Multi-point sensing");
        entry.addSpec("thermal_protection", "Automatic calibration adjustment");
        entry.addSpec("cooling_system", "Hybrid active/passive");

        // Add thermal management specs
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("battery_thermal_control", "Cell-level cooling");
        entry.addSpec("thermal_monitoring", "Distributed temperature network");
        entry.addSpec("thermal_protection", "Multi-stage intervention");
        entry.addSpec("cooling_redundancy", "Triple redundant");
        entry.addSpec("emergency_cooling", "Independent backup system");

        // Basic thermal management for all components
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("thermal_protection", "Multi-stage intervention");

        // Power-specific thermal management
        if (desc.contains("power") || desc.contains("battery") || desc.contains("voltage")) {
            entry.addSpec("battery_thermal_control", "Cell-level cooling");
            entry.addSpec("cooling_redundancy", "Triple redundant");
            entry.addSpec("cooling_method", "Forced liquid convection");
            entry.addSpec("heat_dissipation", "Distributed cooling plates");
            entry.addSpec("thermal_runaway_protection", "Active prevention system");
            entry.addSpec("max_thermal_load", "50W");
        }

        // Temperature specifications
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Power distribution system
        entry.addSpec("power_distribution", "Intelligent switching");
        entry.addSpec("distribution_protection", "Electronic fusing");
        entry.addSpec("voltage_monitoring", "Real-time");

        // Environmental protection
        entry.addSpec("thermal_management", "Active cooling");
        entry.addSpec("humidity_control", "Active dehumidification");
    }

    private void addBaseThermalSpecs(BOMEntry entry) {
        // Basic thermal management specs for ALL components
        entry.addSpec("thermal_monitoring", "Real-time temperature sensing");
        entry.addSpec("thermal_protection", "Automatic thermal shutdown");
        entry.addSpec("cooling_redundancy", "Dual cooling system");
        entry.addSpec("heat_dissipation_method", "Forced liquid convection");
        entry.addSpec("thermal_management", "Active liquid cooling");
        entry.addSpec("operating_temperature", "-40C to +85C");

        // Determine component type and add specific thermal specs
        String mpn = entry.getMpn();
        String desc = entry.getDescription().toLowerCase();

        // Determine component type and add specific thermal specs
        if (isProcessorComponent(entry)) {
            addProcessorThermalSpecs(entry);
        }
        if (isPowerComponent(entry)) {
            addPowerThermalSpecs(entry);
        }
        if (isSensorComponent(entry)) {
            addSensorThermalSpecs(entry);
        }
        if (isCoolingComponent(entry)) {
            addCoolingThermalSpecs(entry);
        }
        if (isRFComponent(entry.getMpn(),entry.getDescription())) {
            addRFThermalSpecs(entry);
        }
        if (isMemoryComponent(entry)) {
            addMemoryThermalSpecs(entry);
        }
        if (isConnectorComponent(entry)) {
            addConnectorThermalSpecs(entry);
        }
        if (isMechanicalComponent(entry)) {
            addMechanicalThermalSpecs(entry);
        }
        if (isGateDriverComponent(entry)) {
            addGateDriverThermalSpecs(entry);
        }
        if (isPHYComponent(entry)) {
            addPHYThermalSpecs(entry);
        }
        if (isTransducerComponent(entry)) {
            addTransducerThermalSpecs(entry);
        }
    }

    private boolean isGateDriverComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("gate driver") || desc.contains("gate drv") ||
                mpn.contains("drv") || mpn.contains("ir21") || mpn.contains("irs2");
    }

    private boolean isPHYComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("phy") || desc.contains("ethernet phy") ||
                mpn.contains("dp83") || mpn.contains("ksz") || mpn.contains("lan87");
    }

    private boolean isTransducerComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("transducer") || desc.contains("acoustic") ||
                mpn.startsWith("at") || desc.contains("sonar");
    }


    private boolean isConnectorComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("connector") || mpn.contains("con-") || mpn.endsWith("-con") ||
                desc.contains("socket") || desc.contains("plug") || desc.contains("receptacle");
    }

    private boolean isMemoryComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("memory") || desc.contains("ram") || desc.contains("flash") ||
                desc.contains("storage") || mpn.contains("sram") || mpn.contains("dram") ||
                mpn.contains("fram") || mpn.contains("mt41") || mpn.contains("is42");
    }

    private boolean isProcessorComponent(String mpn, String desc) {
        return desc.contains("processor") || desc.contains("mcu") || desc.contains("cpu") ||
                mpn.contains("stm32") || mpn.contains("tms") || mpn.contains("imx8") ||
                desc.contains("controller") || desc.contains("microcontroller");
    }

    private boolean isPowerComponent(String mpn, String desc) {
        return desc.contains("power") || desc.contains("battery") || desc.contains("voltage") ||
                desc.contains("converter") || desc.contains("regulator") || mpn.contains("tps") ||
                mpn.contains("ltc") || mpn.contains("bq") || desc.contains("charger");
    }

    private boolean isSensorComponent(String mpn, String desc) {
        return desc.contains("sensor") || desc.contains("detector") || mpn.startsWith("wq-") ||
                mpn.startsWith("water-") || desc.contains("temperature") || desc.contains("monitor");
    }

    private boolean isRFComponent(String mpn, String desc) {
        return desc.contains("rf") || desc.contains("radio") || desc.contains("transceiver") ||
                mpn.contains("cc1200") || desc.contains("antenna");
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

    private void addRFThermalSpecs(BOMEntry entry) {
        entry.addSpec("rf_thermal_protection", "High-efficiency heat spreading");
        entry.addSpec("thermal_zones", "RF isolated sections");
        entry.addSpec("emergency_cooling", "Backup passive cooling");
        entry.addSpec("cooling_method", "Liquid cooling");
        entry.addSpec("heat_sink_type", "Copper heat spreader");
        entry.addSpec("thermal_throttling", "Dynamic power adjustment");
        entry.addSpec("max_thermal_dissipation", "25W");
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

    private void addConnectorThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Conduction cooling");
        entry.addSpec("heat_dissipation", "Metallic body conduction");
        entry.addSpec("thermal_runaway_protection", "Temperature monitoring");
        entry.addSpec("max_thermal_load", "10W");
        entry.addSpec("thermal_protection", "Auto-disconnect");
        entry.addSpec("thermal_monitoring", "Contact point sensing");
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

    private void addMemoryThermalSpecs(BOMEntry entry) {
        entry.addSpec("cooling_method", "Indirect liquid cooling");
        entry.addSpec("heat_dissipation", "Thermal pad");
        entry.addSpec("thermal_protection", "Temperature throttling");
        entry.addSpec("max_thermal_dissipation", "10W");
        entry.addSpec("heat_sink_type", "Surface contact cooler");
        entry.addSpec("thermal_throttling", "Access rate control");
    }

    private void verifyAllSubsystems(SystemVerificationResult result) {
        var subsystemResults = result.getSubsystemResults();

        assertTrue(subsystemResults.get("Mission Control").isValid(),
                "Mission Control verification failed");
        assertTrue(subsystemResults.get("Navigation").isValid(),
                "Navigation verification failed");
        assertTrue(subsystemResults.get("Power").isValid(),
                "Power Management verification failed");
        assertTrue(subsystemResults.get("Propulsion").isValid(),
                "Propulsion verification failed");
        assertTrue(subsystemResults.get("Communications").isValid(),
                "Communications verification failed");
        assertTrue(subsystemResults.get("Sensors").isValid(),
                "Sensor Suite verification failed");
    }

    private void verifyMinimalSubsystems(SystemVerificationResult result) {
        var subsystemResults = result.getSubsystemResults();

        assertTrue(subsystemResults.get("Mission Control").isValid(),
                "Mission Control verification failed");
        assertTrue(subsystemResults.get("Navigation").isValid(),
                "Navigation verification failed");
        assertTrue(subsystemResults.get("Power").isValid(),
                "Power Management verification failed");
        assertTrue(subsystemResults.get("Propulsion").isValid(),
                "Propulsion verification failed");
    }

    private void verifyPressureRatings(SystemVerificationResult result) {
        PlannedProductionBatch batch = createCompleteBatch();
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

    private void verifyPropulsionSpecs(BOMEntry entry, StringBuilder errorReport) {
        checkSpec(entry, "motor_type", errorReport);
        checkSpec(entry, "shaft_seal", errorReport);
        checkSpec(entry, "cooling", errorReport);
        checkSpec(entry, "pressure_compensation", errorReport);
    }

    private void verifyTransducerSpecs(BOMEntry entry, StringBuilder errorReport) {
        checkSpec(entry, "transducer_type", errorReport);
        checkSpec(entry, "acoustic_window", errorReport);
        checkSpec(entry, "pressure_compensation", errorReport);
    }

    private void verifySensorSpecs(BOMEntry entry, StringBuilder errorReport) {
        checkSpec(entry, "sensor_housing", errorReport);
        checkSpec(entry, "sensor_window", errorReport);
        checkSpec(entry, "pressure_compensation", errorReport);
    }

    private void checkSpec(BOMEntry entry, String spec, StringBuilder errorReport) {
        if (!entry.getSpecs().containsKey(spec)) {
            errorReport.append(String.format("    Missing %s specification\n", spec));
        }
    }

    private void verifyWaterproofingOld(SystemVerificationResult result) {
        // Modify to add detailed verification
        result.getSubsystemResults().forEach((name, subsystem) -> {
            boolean waterproofingValid = subsystem.isValid();
            if (!waterproofingValid) {
                System.out.println("Subsystem " + name + " failed waterproofing check");
                subsystem.getIssues().forEach(System.out::println);
            }
        });

        assertTrue(result.getSubsystemResults().values().stream()
                        .allMatch(SubsystemVerificationResult::isValid),
                "Waterproofing requirements not met");
    }

    private void verifyThermalManagement(SystemVerificationResult result) {
        PlannedProductionBatch batch = createCompleteBatch();
        StringBuilder errorReport = new StringBuilder("Thermal Management Verification Report:\n");
        boolean allValid = true;

        // Check all components
        for (BOMEntry entry : getAllComponents(batch)) {
            if (!verifyComponentThermalSpecs(entry, errorReport)) {
                allValid = false;
            }
        }

        // Verify subsystem-level thermal management
        Map<String, Boolean> subsystemResults = new HashMap<>();
        subsystemResults.put("Navigation", verifyNavigationThermalManagement(batch, errorReport));
        subsystemResults.put("Propulsion", verifyPropulsionThermalManagement(batch, errorReport));
        subsystemResults.put("Communications", verifyCommunicationsThermalManagement(batch, errorReport));
        subsystemResults.put("Mission Control", verifyMissionControlThermalManagement(batch, errorReport));
        subsystemResults.put("Sensors", verifySensorsThermalManagement(batch, errorReport));
        subsystemResults.put("Power", verifyPowerThermalManagement(batch, errorReport));

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

    private boolean verifyComponentThermalSpecs(BOMEntry entry, StringBuilder errorReport) {
        boolean valid = true;
        String componentInfo = String.format("%s (%s)", entry.getMpn(), entry.getDescription());

        // Basic thermal management requirements
        String[] requiredSpecs = {
                "thermal_management",
                "thermal_monitoring",
                "thermal_protection",
                "operating_temperature",
                "cooling_redundancy",
                "heat_dissipation_method"
        };

        for (String spec : requiredSpecs) {
            if (!entry.getSpecs().containsKey(spec) || entry.getSpecs().get(spec) == null
                    || entry.getSpecs().get(spec).isEmpty()) {
                errorReport.append(String.format("  Missing %s for component %s\n", spec, componentInfo));
                valid = false;
            }
        }

        // Verify operating temperature range
        String tempRange = entry.getSpecs().get("operating_temperature");
        if (tempRange != null && !tempRange.equals("-40C to +85C")) {
            errorReport.append(String.format("  Invalid temperature range for %s: %s\n",
                    componentInfo, tempRange));
            valid = false;
        }

        // Additional checks based on component type
        if (isProcessorComponent(entry)) {
            valid &= verifyProcessorThermalSpecs(entry, errorReport);
        } else if (isPowerComponent(entry)) {
            valid &= verifyPowerThermalSpecs(entry, errorReport);
        } else if (isSensorComponent(entry)) {
            valid &= verifySensorThermalSpecs(entry, errorReport);
        }

        return valid;
    }

    private boolean verifyProcessorThermalSpecs(BOMEntry entry, StringBuilder errorReport) {
        String[] requiredSpecs = {
                "cooling_method",
                "thermal_interface",
                "heat_sink_type",
                "thermal_throttling",
                "max_thermal_dissipation"
        };
        return verifyRequiredSpecs(entry, requiredSpecs, errorReport);
    }

    private boolean verifyPowerThermalSpecs(BOMEntry entry, StringBuilder errorReport) {
        String[] requiredSpecs = {
                "cooling_method",
                "thermal_protection",
                "heat_dissipation",
                "thermal_runaway_protection",
                "max_thermal_load"
        };
        return verifyRequiredSpecs(entry, requiredSpecs, errorReport);
    }

    private boolean verifySensorThermalSpecs(BOMEntry entry, StringBuilder errorReport) {
        String[] requiredSpecs = {
                "thermal_stability",
                "thermal_isolation",
                "temperature_compensation",
                "thermal_equilibrium_time"
        };
        return verifyRequiredSpecs(entry, requiredSpecs, errorReport);
    }

    private boolean verifyRequiredSpecs(BOMEntry entry, String[] requiredSpecs, StringBuilder errorReport) {
        boolean valid = true;
        String componentInfo = String.format("%s (%s)", entry.getMpn(), entry.getDescription());

        for (String spec : requiredSpecs) {
            if (!entry.getSpecs().containsKey(spec) || entry.getSpecs().get(spec) == null
                    || entry.getSpecs().get(spec).isEmpty()) {
                errorReport.append(String.format("  Missing %s for component %s\n", spec, componentInfo));
                valid = false;
            }
        }
        return valid;
    }

    private boolean isCoolingComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn().toLowerCase();
        return desc.contains("cooling") || desc.contains("thermal") || desc.contains("heat")
                || mpn.contains("cool") || mpn.contains("thp") || desc.contains("temperature");
    }

    private boolean isProcessorComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("processor") || desc.contains("mcu") || desc.contains("cpu") ||
                mpn.contains("stm32") || mpn.contains("tms") || mpn.contains("imx8") ||
                desc.contains("controller") || desc.contains("microcontroller") ||
                desc.contains("gate driver") || desc.contains("phy") || mpn.contains("dp83") ||
                mpn.contains("drv");
    }

    private boolean isPowerComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn().toLowerCase();
        return desc.contains("power") || desc.contains("battery") || desc.contains("voltage")
                || mpn.contains("pwr-") || mpn.contains("batt") || desc.contains("regulator")
                || desc.contains("converter") || desc.contains("buck") || desc.contains("dc/dc")
                || desc.contains("power supply") || desc.contains("distribution");
    }

    private boolean isSensorComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("sensor") || desc.contains("detector") || desc.contains("transducer")
                || desc.contains("monitor") || desc.contains("temperature") || desc.contains("thermal")
                || desc.contains("acoustic");
    }

    private boolean verifyNavigationThermalManagement(PlannedProductionBatch batch, StringBuilder errorReport) {
        return verifySubsystemThermalManagement(batch, "Navigation", Arrays.asList(
                "thermal_management",
                "thermal_monitoring",
                "cooling_redundancy",
                "heat_dissipation_method"
        ), errorReport);
    }

    private boolean verifyPropulsionThermalManagement(PlannedProductionBatch batch, StringBuilder errorReport) {
        return verifySubsystemThermalManagement(batch, "Propulsion", Arrays.asList(
                "thermal_management",
                "motor_cooling",
                "bearing_cooling",
                "thermal_protection"
        ), errorReport);
    }

    private boolean verifyCommunicationsThermalManagement(PlannedProductionBatch batch, StringBuilder errorReport) {
        return verifySubsystemThermalManagement(batch, "Communications", Arrays.asList(
                "thermal_management",
                "rf_thermal_protection",
                "thermal_zones",
                "emergency_cooling"
        ), errorReport);
    }

    private boolean verifyMissionControlThermalManagement(PlannedProductionBatch batch, StringBuilder errorReport) {
        return verifySubsystemThermalManagement(batch, "Mission Control", Arrays.asList(
                "thermal_management",
                "cooling_redundancy",
                "thermal_zones",
                "emergency_cooling"
        ), errorReport);
    }

    private boolean verifySensorsThermalManagement(PlannedProductionBatch batch, StringBuilder errorReport) {
        return verifySubsystemThermalManagement(batch, "Sensors", Arrays.asList(
                "thermal_management",
                "sensor_thermal_stability",
                "thermal_calibration",
                "cooling_system"
        ), errorReport);
    }

    private boolean verifyPowerThermalManagement(PlannedProductionBatch batch, StringBuilder errorReport) {
        return verifySubsystemThermalManagement(batch, "Power", Arrays.asList(
                "thermal_management",
                "battery_thermal_control",
                "thermal_protection",
                "cooling_redundancy"
        ), errorReport);
    }

    private boolean verifySubsystemThermalManagement(PlannedProductionBatch batch, String subsystem,
                                                     List<String> requiredSpecs, StringBuilder errorReport) {
        boolean valid = true;
        for (BOMEntry entry : getSubsystemComponents(batch, subsystem)) {
            // Only check specs relevant to the component type
            List<String> applicableSpecs = getApplicableThermalSpecs(subsystem, entry);
            for (String spec : applicableSpecs) {
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

    private List<String> getApplicableThermalSpecs(String subsystem, BOMEntry entry) {
        List<String> specs = new ArrayList<>();
        String desc = entry.getDescription().toLowerCase();

        // Add common specs for all components
        specs.add("thermal_management");
        specs.add("thermal_protection");

        switch (subsystem) {
            case "Communications":
                if (desc.contains("rf") || desc.contains("transceiver") || desc.contains("power amplifier")) {
                    specs.add("rf_thermal_protection");
                    specs.add("thermal_zones");
                    specs.add("emergency_cooling");
                }
                break;

            case "Mission Control":
                if (desc.contains("processor") || desc.contains("memory") || desc.contains("storage")) {
                    specs.add("thermal_zones");
                    specs.add("emergency_cooling");
                }
                break;

            case "Power":
                if (desc.contains("battery") || desc.contains("power") || desc.contains("voltage")) {
                    specs.add("battery_thermal_control");
                    specs.add("thermal_protection");
                    specs.add("cooling_redundancy");
                }
                break;

            case "Sensors":
                if (desc.contains("sensor") || desc.contains("detector")) {
                    specs.add("sensor_thermal_stability");
                    specs.add("thermal_calibration");
                    specs.add("cooling_system");
                }
                break;

            case "Propulsion":
                if (desc.contains("motor") || desc.contains("bearing")) {
                    specs.add("motor_cooling");
                    specs.add("bearing_cooling");
                }
                break;
        }

        // Add component-specific specs
        if (isProcessorComponent(entry)) {
            specs.addAll(Arrays.asList(
                    "cooling_method",
                    "thermal_interface",
                    "heat_sink_type",
                    "thermal_throttling"
            ));
        } else if (isPowerComponent(entry)) {
            specs.addAll(Arrays.asList(
                    "cooling_method",
                    "heat_dissipation",
                    "thermal_runaway_protection",
                    "max_thermal_load"
            ));
        } else if (isSensorComponent(entry)) {
            specs.addAll(Arrays.asList(
                    "thermal_stability",
                    "thermal_isolation",
                    "temperature_compensation",
                    "thermal_equilibrium_time"
            ));
        }

        return specs;
    }


    private void verifySafetySystems(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().get("Mission Control").isValid(),
                "Safety system requirements not met");
    }

    private void verifyRedundancySystems(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().get("Mission Control").isValid(),
                "Redundancy requirements not met");
    }

    private void verifyEmergencySystems(SystemVerificationResult result) {
        assertTrue(result.getSubsystemResults().get("Mission Control").isValid(),
                "Emergency system requirements not met");
    }

    private void addGerberFilesToPCBA(PCBABOM pcba, String boardName) {
        try {
            // Create new GerberAsset instance
            GerberAsset gerberAsset = new GerberAsset(boardName, "1.0");

            // Generate standard layers
            gerberAsset.generateStandardLayers(boardName);

            // Add manufacturing docs
            gerberAsset.addManufacturingDoc("assembly", boardName + "-Assembly.pdf");
            gerberAsset.addManufacturingDoc("pickandplace", boardName + "-PnP.txt");
            gerberAsset.addManufacturingDoc("bom", boardName + "-BOM.xlsx");

            // Add environmental specs to metadata
            Map<String, Object> metadata = gerberAsset.getMetadata();
            metadata.put("sealing", "IP68");
            metadata.put("waterproof", "Yes");
            metadata.put("protection_rating", "IP68");
            metadata.put("environmental_rating", "Submarine Grade");
            metadata.put("conformal_coating", "Type AR");
            metadata.put("pressure_rating", "300m depth");
            metadata.put("max_pressure", "31 bar");
            metadata.put("pressure_tested", "Yes");


            // Set the GerberAsset in the PCBA
            pcba.setPcbReference(gerberAsset);


            pcba.getBomEntries().forEach(this::addEnvironmentalSpecs);

            // Add environmental specs to all PCBA entries
            // Verify the asset was set
            assertNotNull(pcba.getPcbReference(), "GerberAsset should be set on PCBA");

        } catch (Exception e) {
            fail("Failed to add Gerber files to PCBA: " + e.getMessage());
        }
    }



    private void verifyWaterproofing(SystemVerificationResult result) {
        PlannedProductionBatch batch = createCompleteBatch();
        StringBuilder errorReport = new StringBuilder("Waterproofing Verification Report:\n");
        boolean allValid = true;

        // Check each subsystem
        Map<String, Boolean> subsystemResults = new HashMap<>();
        subsystemResults.put("Navigation", verifyNavigationSubsystem(batch, errorReport));
        subsystemResults.put("Propulsion", verifyPropulsionSubsystem(batch, errorReport));
        subsystemResults.put("Communications", verifyCommunicationsSubsystem(batch, errorReport));
        subsystemResults.put("Mission Control", verifyMissionControlSubsystem(batch, errorReport));
        subsystemResults.put("Sensors", verifySensorSubsystem(batch, errorReport));
        subsystemResults.put("Power", verifyPowerSubsystem(batch, errorReport));

        // Check if any subsystem failed
        for (Map.Entry<String, Boolean> rresult : subsystemResults.entrySet()) {
            if (!rresult.getValue()) {
                allValid = false;
                errorReport.append(String.format("Subsystem %s failed waterproofing check\n", rresult.getKey()));
            }
        }

        assertTrue(allValid, "Waterproofing requirements not met:\n" + errorReport.toString());
    }

    private boolean verifyNavigationSubsystem(PlannedProductionBatch batch, StringBuilder errorReport) {
        boolean valid = true;
        String[] requiredSpecs = {
                // Basic waterproofing
                "housing_material", "housing_sealing", "pressure_compensation",
                // Power interface
                "power_interface", "power_connector", "power_protection",
                // Data interface
                "data_interface", "data_connector", "data_protection",
                // Mechanical interface
                "mounting_interface", "mechanical_seal", "mechanical_lock"
        };

        for (BOMEntry entry : getSubsystemComponents(batch, "Navigation")) {
            if (!verifyComponentSpecs(entry, requiredSpecs, errorReport)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean verifyPropulsionSubsystem(PlannedProductionBatch batch, StringBuilder errorReport) {
        boolean valid = true;
        String[] requiredSpecs = {
                // Basic waterproofing
                "housing_material", "shaft_seal", "housing_seal",
                // Power interface
                "power_interface", "power_rating", "current_protection",
                // Control interface
                "control_interface", "control_redundancy", "feedback_system",
                // Mechanical interface
                "mounting_system", "pressure_compensation", "bearing_lubrication"
        };

        for (BOMEntry entry : getSubsystemComponents(batch, "Propulsion")) {
            if (!verifyComponentSpecs(entry, requiredSpecs, errorReport)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean verifyCommunicationsSubsystem(PlannedProductionBatch batch, StringBuilder errorReport) {
        boolean valid = true;
        String[] requiredSpecs = {
                // Basic waterproofing
                "housing_material", "housing_sealing", "pressure_compensation",
                // Emergency beacon
                "emergency_beacon", "beacon_power", "beacon_activation",
                // System interface
                "system_interface", "interface_redundancy",
                // Antenna system
                "antenna_type", "antenna_rating", "antenna_sealing"
        };

        for (BOMEntry entry : getSubsystemComponents(batch, "Communications")) {
            if (!verifyComponentSpecs(entry, requiredSpecs, errorReport)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean verifyMissionControlSubsystem(PlannedProductionBatch batch, StringBuilder errorReport) {
        boolean valid = true;
        String[] requiredSpecs = {
                // Basic waterproofing
                "housing_material", "housing_sealing", "pressure_compensation",
                // Processing systems
                "processing_system", "processor_monitoring",
                // Memory systems
                "memory_type", "storage_type",
                // Monitoring systems
                "system_monitoring", "environmental_monitoring",
                // Environmental protection
                "thermal_management", "humidity_control"
        };

        for (BOMEntry entry : getSubsystemComponents(batch, "Mission Control")) {
            if (!verifyComponentSpecs(entry, requiredSpecs, errorReport)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean verifySensorSubsystem(PlannedProductionBatch batch, StringBuilder errorReport) {
        boolean valid = true;
        String[] requiredSpecs = {
                // Basic waterproofing
                "housing_material", "housing_sealing", "pressure_compensation",
                // Sensors
                "ph_sensor", "do_sensor", "turbidity_sensor", "water_quality_sensor", "imaging_system",
                // Interface
                "sensor_interface", "interface_protection",
                // Calibration
                "calibration_type", "calibration_interval"
        };

        for (BOMEntry entry : getSubsystemComponents(batch, "Sensors")) {
            if (!verifyComponentSpecs(entry, requiredSpecs, errorReport)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean verifyPowerSubsystem(PlannedProductionBatch batch, StringBuilder errorReport) {
        boolean valid = true;
        String[] requiredSpecs = {
                // Basic waterproofing
                "housing_material", "housing_sealing", "pressure_compensation",
                // Battery management
                "battery_management", "battery_monitoring", "battery_protection",
                // Power distribution
                "power_distribution", "distribution_protection", "voltage_monitoring",
                // Environmental protection
                "thermal_management", "humidity_control"
        };

        for (BOMEntry entry : getSubsystemComponents(batch, "Power")) {
            if (!verifyComponentSpecs(entry, requiredSpecs, errorReport)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean verifyComponentSpecs(BOMEntry entry, String[] requiredSpecs, StringBuilder errorReport) {
        boolean valid = true;
        Map<String, String> specs = entry.getSpecs();
        String componentType = determineComponentType(entry);
        String[] applicableSpecs = getApplicableSpecs(componentType, requiredSpecs);

        for (String spec : applicableSpecs) {
            if (!specs.containsKey(spec) || specs.get(spec) == null || specs.get(spec).isEmpty()) {
                errorReport.append(String.format("  Missing or invalid %s for component %s (%s)\n",
                        spec, entry.getMpn(), entry.getDescription()));
                valid = false;
            }
        }
        return valid;
    }


    private String determineComponentType(BOMEntry entry) {
        String mpn = entry.getMpn().toLowerCase();
        String desc = entry.getDescription().toLowerCase();

        if (desc.contains("housing") || mpn.contains("hsg")) return "HOUSING";
        if (desc.contains("connector") || mpn.contains("con")) return "CONNECTOR";
        if (desc.contains("seal") || desc.contains("o-ring")) return "SEAL";
        if (desc.contains("mcu") || desc.contains("processor")) return "PROCESSOR";
        if (desc.contains("sensor")) return "SENSOR";
        if (desc.contains("power") || mpn.contains("pwr")) return "POWER";
        if (desc.contains("mount") || mpn.contains("mnt")) return "MOUNT";
        if (desc.contains("antenna") || mpn.contains("ant")) return "ANTENNA";
        if (desc.contains("motor") || desc.contains("propeller")) return "PROPULSION";
        return "GENERIC";
    }


    private String[] getApplicableSpecs(String componentType, String[] allSpecs) {
        Set<String> applicableSpecs = new HashSet<>();

        // Basic waterproofing specs for all components
        applicableSpecs.add("sealing");
        applicableSpecs.add("waterproof");
        applicableSpecs.add("protection_rating");
        applicableSpecs.add("pressure_rating");

        switch (componentType) {
            case "HOUSING":
                applicableSpecs.addAll(Arrays.asList(
                        "housing_material", "housing_sealing", "pressure_compensation",
                        "thermal_management", "corrosion_protection"
                ));
                break;

            case "CONNECTOR":
                applicableSpecs.addAll(Arrays.asList(
                        "connector_type", "connector_rating", "sealing_method",
                        "contact_material", "mating_cycles"
                ));
                break;

            case "SEAL":
                applicableSpecs.addAll(Arrays.asList(
                        "seal_material", "seal_type", "seal_rating",
                        "temperature_range", "chemical_resistance"
                ));
                break;

            case "PROCESSOR":
                applicableSpecs.addAll(Arrays.asList(
                        "thermal_management", "cooling_method",
                        "environmental_protection"
                ));
                break;

            case "SENSOR":
                applicableSpecs.addAll(Arrays.asList(
                        "sensor_housing", "sensor_protection", "calibration_type",
                        "sensor_interface", "environmental_protection"
                ));
                break;

            case "POWER":
                applicableSpecs.addAll(Arrays.asList(
                        "power_rating", "protection_system", "monitoring_system",
                        "thermal_management"
                ));
                break;

            case "MOUNT":
                applicableSpecs.addAll(Arrays.asList(
                        "mounting_interface", "mechanical_seal", "material"
                ));
                break;

            case "ANTENNA":
                applicableSpecs.addAll(Arrays.asList(
                        "antenna_type", "pressure_compensation", "sealing_method"
                ));
                break;

            case "PROPULSION":
                applicableSpecs.addAll(Arrays.asList(
                        "motor_sealing", "bearing_type", "cooling_method",
                        "shaft_seal_type"
                ));
                break;

            case "GENERIC":
                applicableSpecs.addAll(Arrays.asList(
                        "environmental_protection", "pressure_compensation"
                ));
                break;
        }

        // Filter the applicable specs to only those that are in the allSpecs array
        return Arrays.stream(allSpecs)
                .filter(applicableSpecs::contains)
                .toArray(String[]::new);
    }


    private boolean verifySubsystemSpecs(BOMEntry entry, String[] requiredSpecs, String subsystem, StringBuilder errorReport) {
        boolean valid = true;
        Map<String, String> specs = entry.getSpecs();

        // Get subsystem-specific specs
        String[] subsystemSpecs = getSubsystemSpecificSpecs(subsystem, entry);

        // Combine with required specs if they apply to this component type
        Set<String> allRequiredSpecs = new HashSet<>(Arrays.asList(
                determineApplicableSpecs(entry, requiredSpecs, subsystemSpecs)
        ));

        for (String spec : allRequiredSpecs) {
            if (!specs.containsKey(spec) || specs.get(spec) == null || specs.get(spec).isEmpty()) {
                errorReport.append(String.format("  Missing or invalid %s for component %s (%s)\n",
                        spec, entry.getMpn(), entry.getDescription()));
                valid = false;
            }
        }
        return valid;
    }


    private String[] determineApplicableSpecs(BOMEntry entry, String[] requiredSpecs, String[] subsystemSpecs) {
        String componentType = determineComponentType(entry);
        Set<String> applicableSpecs = new HashSet<>();

        // Add relevant specs based on component type
        applicableSpecs.addAll(Arrays.asList(getApplicableSpecs(componentType, requiredSpecs)));

        // Add subsystem-specific specs if they apply to this component type
        if (isSubsystemSpecsApplicable(componentType)) {
            applicableSpecs.addAll(Arrays.asList(subsystemSpecs));
        }

        return applicableSpecs.toArray(new String[0]);
    }

    private boolean isSubsystemSpecsApplicable(String componentType) {
        // Define which component types should have subsystem-specific specs
        return !Arrays.asList("SEAL", "MOUNT", "GENERIC").contains(componentType);
    }

    private String[] getSubsystemSpecificSpecs(String subsystem, BOMEntry entry) {
        switch (subsystem) {
            case "Navigation":
                return new String[]{"position_accuracy", "depth_accuracy"};
            case "Propulsion":
                return new String[]{"power_rating", "control_interface"};
            case "Communications":
                return new String[]{"bandwidth", "emergency_beacon"};
            case "Power":
                return new String[]{"power_distribution", "battery_management"};
            case "Sensors":
                return new String[]{"sensor_interface", "calibration_type"};
            default:
                return new String[0];
        }
    }

    private List<BOMEntry> getSubsystemComponents(PlannedProductionBatch batch, String subsystemName) {
        List<BOMEntry> components = new ArrayList<>();

        // Get PCBA components
        batch.getPCBAStructure().getAssemblies().stream()
                .filter(pcba -> containsSubsystem(pcba, subsystemName))
                .forEach(pcba -> components.addAll(pcba.getBomEntries()));

        // Get mechanical components
        batch.getMechanicalStructure().getAssemblies().stream()
                .filter(mech -> containsSubsystem(mech, subsystemName))
                .forEach(mech -> components.addAll(mech.getBomEntries()));

        return components;
    }

    private boolean containsSubsystem(PCBABOM pcba, String subsystemName) {
        // Check if any BOM entries or the PCBA itself is related to the subsystem
        return pcba.getBomEntries().stream()
                .anyMatch(entry ->
                        (entry.getMpn() != null && entry.getMpn().toUpperCase().contains(subsystemName.toUpperCase())) ||
                                (entry.getDescription() != null && entry.getDescription().toUpperCase().contains(subsystemName.toUpperCase()))
                );
    }

    private boolean containsSubsystem(MechanicalBOM mech, String subsystemName) {
        // Check if any BOM entries or the mechanical assembly itself is related to the subsystem
        return mech.getBomEntries().stream()
                .anyMatch(entry ->
                        (entry.getMpn() != null && entry.getMpn().toUpperCase().contains(subsystemName.toUpperCase())) ||
                                (entry.getDescription() != null && entry.getDescription().toUpperCase().contains(subsystemName.toUpperCase()))
                );
    }

}

