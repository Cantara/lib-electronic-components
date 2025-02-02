package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating submarine subsystems.
 * Manages the creation and configuration of all major subsystems including
 * mission control, navigation, power, propulsion, communications, and sensors.
 */
public class SubmarineSubsystemFactory {
    private final SubmarineComponentSpecs componentSpecs;
    private final PCBGenerator pcbGenerator;

    public SubmarineSubsystemFactory(SubmarineComponentSpecs componentSpecs, PCBGenerator pcbGenerator) {
        this.componentSpecs = componentSpecs;
        this.pcbGenerator = pcbGenerator;
    }

    /**
     * Adds the network infrastructure to the submarine system.
     */
    public void addNetworkInterfaces(PlannedProductionBatch batch) {
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
        MechanicalBOM networkMechanical = new MechanicalBOM();

        BOMEntry switchHousing = new BOMEntry();
        switchHousing.setMpn("NET-HSG-01");
        switchHousing.setDescription("Network Switch Housing");
        addNetworkHousingSpecs(switchHousing);

        List<BOMEntry> mechanicalEntries = new ArrayList<>();
        mechanicalEntries.add(switchHousing);
        networkMechanical.setBomEntries(mechanicalEntries);

        // Generate Gerber files for the network switch
        pcbGenerator.addGerberFilesToPCBA(networkSwitch, "NET-SW-01");

        // Add the components to the batch
        batch.addPCBA(networkSwitch);
        batch.addMechanical(networkMechanical);
    }

    private void addNetworkSwitchSpecs(BOMEntry entry) {
        // Basic environmental specs
        componentSpecs.addEnvironmentalSpecs(entry);

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

        // System interface
        entry.addSpec("system_interface", "Management console");
        entry.addSpec("monitoring_interface", "SNMP v3");
        entry.addSpec("configuration_interface", "Web/CLI");
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

        // Interface specs
        entry.addSpec("cable_penetrators", "Wet-mateable");
        entry.addSpec("mounting_system", "Quick Release");
        entry.addSpec("mechanical_interface", "Rack mount");
        entry.addSpec("mechanical_seal", "Double O-ring");
        entry.addSpec("mechanical_lock", "Quarter-turn locking");
    }
    /**
     * Adds the mission control system to the submarine.
     */
    public void addMissionControlSystem(PlannedProductionBatch batch) {
        // Create mission control PCBA components
        List<BOMEntry> pcbaEntries = new ArrayList<>();

        // Main processors
        addProcessorEntry(pcbaEntries, "i.MX8QuadXPlus", "Main Control Processor", true);
        addProcessorEntry(pcbaEntries, "TMS570LC4357BZWTQQ1", "Safety Co-Processor", true);
        addProcessorEntry(pcbaEntries, "MPC5744P", "Real-Time Co-Processor", true);

        // Memory systems
        addMemoryEntry(pcbaEntries, "MT41K512M16HA-125", "Memory", "512MB");
        addMemoryEntry(pcbaEntries, "SSDPE21K375GA01", "Storage", "375GB");

        // System monitoring
        addMonitoringEntry(pcbaEntries, "MAX6369KA+T", "Watchdog");
        addMonitoringEntry(pcbaEntries, "LTC2977", "Power Management");
        addMonitoringEntry(pcbaEntries, "MAX31730", "Temperature Monitor");
        addMonitoringEntry(pcbaEntries, "ADM1069", "Voltage Monitor");

        // Create the mission control PCBA
        PCBABOM missionControlBoard = new PCBABOM(
                "MC-MAIN-01",
                "Mission Control Board",
                "R1.0",
                pcbaEntries
        );

        // Add mechanical components
        List<BOMEntry> mechanicalEntries = new ArrayList<>();

        // Housing and thermal management
        addHousingEntry(mechanicalEntries, "MCS-800-HSG", "Mission Control Housing");
        addThermalEntry(mechanicalEntries, "MCS-800-THP", "Thermal Management Plate");
        addCoolingEntry(mechanicalEntries, "MCS-800-COOL", "Thermal Management System");

        // Mounting and connections
        addMountingEntry(mechanicalEntries, "MCS-800-MNT", "Mission Control Mounting Kit");
        addConnectorEntry(mechanicalEntries, "MCS-800-CON-M", "Main System Connector");
        addConnectorEntry(mechanicalEntries, "MCS-800-CON-N", "Network Connector");

        MechanicalBOM missionControlMech = new MechanicalBOM();
        missionControlMech.setBomEntries(mechanicalEntries);

        // Generate Gerber files
        pcbGenerator.addGerberFilesToPCBA(missionControlBoard, "MC-MAIN-01");

        // Add to batch
        batch.addPCBA(missionControlBoard);
        batch.addMechanical(missionControlMech);
    }

    /**
     * Adds the navigation system to the submarine.
     */
    public void addNavigationSystem(PlannedProductionBatch batch) {
        // Create navigation PCBA components
        List<BOMEntry> pcbaEntries = new ArrayList<>();

        // Main processor and sensors
        addProcessorEntry(pcbaEntries, "STM32H755ZIT6", "Dual-Core ARM Cortex-M7+M4 MCU", true);
        addSensorEntry(pcbaEntries, "BMX055", "9-axis IMU");
        addSensorEntry(pcbaEntries, "MS5837-30BA", "High Resolution Pressure Sensor");

        // Memory and storage
        addMemoryEntry(pcbaEntries, "FM24V10-G", "1Mb FRAM Non-volatile Memory", "1Mb");
        addMemoryEntry(pcbaEntries, "W25Q128JVSIQ", "128Mb Serial Flash", "128Mb");

        // Power and interfaces
        addPowerEntry(pcbaEntries, "TPS62912", "Buck Converter");
        addInterfaceEntry(pcbaEntries, "MAX3485EESA+", "RS-485 Transceiver");

        // Navigation specific interfaces
        addCustomEntry(pcbaEntries, "NAV-PWR-01", "Navigation Power Interface");
        addCustomEntry(pcbaEntries, "NAV-ETH-01", "Navigation Ethernet Interface");

        // Create the navigation PCBA
        PCBABOM navigationBoard = new PCBABOM(
                "NAV-MAIN-01",
                "Navigation Control Board",
                "R1.0",
                pcbaEntries
        );

        // Add mechanical components
        List<BOMEntry> mechanicalEntries = new ArrayList<>();

        // Housing and mounting
        addHousingEntry(mechanicalEntries, "NAV-300-HSG", "Navigation System Housing");
        addMountingEntry(mechanicalEntries, "NAV-300-MNT", "Mounting Kit");

        // Sealing and connections
        addSealingEntry(mechanicalEntries, "NAV-300-SEAL", "O-Ring Set");
        addConnectorEntry(mechanicalEntries, "NAV-300-CON", "Underwater Connector");

        // Mechanical interface
        addCustomEntry(mechanicalEntries, "NAV-MCH-01", "Navigation Mechanical Interface");

        MechanicalBOM navigationMech = new MechanicalBOM();
        navigationMech.setBomEntries(mechanicalEntries);

        // Generate Gerber files
        pcbGenerator.addGerberFilesToPCBA(navigationBoard, "NAV-MAIN-01");

        // Add to batch
        batch.addPCBA(navigationBoard);
        batch.addMechanical(navigationMech);
    }

    // Helper methods for component creation
    private void addProcessorEntry(List<BOMEntry> entries, String mpn, String description, boolean isRedundant) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);

        if (isRedundant) {
            entry.addSpec("redundancy_level", "Triple");
            entry.addSpec("fault_tolerance", "Active-Active-Active");
            entry.addSpec("failover_time", "<10ms");
        }

        entries.add(entry);
    }

    private void addMemoryEntry(List<BOMEntry> entries, String mpn, String description, String capacity) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("capacity", capacity);
        entry.addSpec("ecc", "Yes");
        entry.addSpec("error_correction", "Single-bit correction, Double-bit detection");
        entries.add(entry);
    }

    private void addMonitoringEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("monitoring_type", "Continuous");
        entry.addSpec("sampling_rate", "1kHz");
        entry.addSpec("alert_capability", "Yes");
        entries.add(entry);
    }

    private void addSensorEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("calibration", "Factory calibrated");
        entry.addSpec("accuracy", "High precision");
        entry.addSpec("sampling_rate", "100Hz");
        entries.add(entry);
    }
    /**
     * Adds the power management system to the submarine.
     */
    public void addPowerSystem(PlannedProductionBatch batch) {
        // Create power management PCBA components
        List<BOMEntry> pcbaEntries = new ArrayList<>();

        // Main control and monitoring
        addProcessorEntry(pcbaEntries, "STM32G474RET6", "Power Management MCU", true);

        // Power management ICs
        addPowerEntry(pcbaEntries, "LTC4015", "Battery Charging Power Supply Controller");
        addPowerEntry(pcbaEntries, "LTC3871", "High Voltage DC/DC Controller");
        addPowerEntry(pcbaEntries, "LTC3891", "Step-Down DC/DC Controller");
        addPowerEntry(pcbaEntries, "LTC7132", "Power Distribution Controller");

        // Power distribution and protection
        addPowerEntry(pcbaEntries, "TPS2595xx", "Smart Power Distribution Switch Array");
        addPowerEntry(pcbaEntries, "BQ76952PFBR", "Battery Management IC");
        addPowerEntry(pcbaEntries, "TPS1H100-Q1", "Smart High-Side Power Switch");
        addPowerEntry(pcbaEntries, "MAX17525AUP+", "System Protection IC");

        // Monitoring and safety
        addMonitoringEntry(pcbaEntries, "MAX6654", "Temperature Monitor with OTP");
        addMonitoringEntry(pcbaEntries, "BQ40Z80RSMR", "Battery Management IC");
        addMonitoringEntry(pcbaEntries, "MAX16135", "Overvoltage Protector");
        addMonitoringEntry(pcbaEntries, "INA238", "Current Monitor");
        addMonitoringEntry(pcbaEntries, "MAX31730", "Temperature Monitor");

        // Create the power management PCBA
        PCBABOM powerBoard = new PCBABOM(
                "PWR-MAIN-01",
                "Power Management Board",
                "R1.0",
                pcbaEntries
        );

        // Add mechanical components
        List<BOMEntry> mechanicalEntries = new ArrayList<>();

        // Housing and cooling
        addHousingEntry(mechanicalEntries, "PWR-500-HSG", "Power Management Housing");
        addCoolingEntry(mechanicalEntries, "PWR-500-COOL", "Liquid Cooling System");
        addThermalEntry(mechanicalEntries, "PWR-500-THP", "Thermal Interface Material");

        // Mounting and connections
        addMountingEntry(mechanicalEntries, "PWR-500-MNT", "Power Unit Mounting Kit");
        addPowerConnectorEntry(mechanicalEntries, "PWR-500-CON-P", "Power Connector");
        addSignalConnectorEntry(mechanicalEntries, "PWR-500-CON-S", "Signal Connector");

        MechanicalBOM powerMech = new MechanicalBOM();
        powerMech.setBomEntries(mechanicalEntries);

        // Generate Gerber files
        pcbGenerator.addGerberFilesToPCBA(powerBoard, "PWR-MAIN-01");

        // Add to batch
        batch.addPCBA(powerBoard);
        batch.addMechanical(powerMech);
    }

    /**
     * Adds the propulsion system to the submarine.
     */
    public void addPropulsionSystem(PlannedProductionBatch batch) {
        // Create propulsion PCBA components
        List<BOMEntry> pcbaEntries = new ArrayList<>();

        // Control and monitoring
        addProcessorEntry(pcbaEntries, "STM32G474VET6", "Motor Control MCU", true);
        addDriverEntry(pcbaEntries, "DRV8353RS", "Three-Phase Gate Driver");
        addPowerEntry(pcbaEntries, "IPT015N10N5", "100V N-Channel MOSFET");

        // Sensing and feedback
        addSensorEntry(pcbaEntries, "AS5047P", "Magnetic Rotary Encoder");
        addMonitoringEntry(pcbaEntries, "INA240A3", "Current Sense Amplifier");

        // Power and protection
        addPowerEntry(pcbaEntries, "TPS65261RHAR", "Triple Buck Converter");
        addPowerEntry(pcbaEntries, "BQ77PL900", "Motor Protection IC");
        addMonitoringEntry(pcbaEntries, "MAX31760", "Temperature Monitor and Fan Controller");

        // Create the propulsion control PCBA
        PCBABOM propulsionBoard = new PCBABOM(
                "PROP-MAIN-01",
                "Propulsion Control Board",
                "R1.0",
                pcbaEntries
        );

        // Add mechanical components
        List<BOMEntry> mechanicalEntries = new ArrayList<>();

        // Motor and propulsion
        addMotorEntry(mechanicalEntries, "U8-II", "Brushless DC Motor");
        addPropulsionEntry(mechanicalEntries, "PROP-400-PRO", "Propeller Assembly");
        addPropulsionEntry(mechanicalEntries, "PROP-400-HUB", "Propeller Hub");

        // Bearings and seals
        addBearingEntry(mechanicalEntries, "7208-BECBP", "Main Shaft Bearing");
        addSealingEntry(mechanicalEntries, "TC-25-40-10-CV", "Shaft Seal");
        addSealingEntry(mechanicalEntries, "PROP-400-SEAL", "O-Ring Set");

        // Housing and connections
        addHousingEntry(mechanicalEntries, "PROP-400-HSG", "Motor Housing");
        addMountingEntry(mechanicalEntries, "PROP-400-MNT", "Mounting Kit");
        addPowerConnectorEntry(mechanicalEntries, "PROP-400-CON-P", "Power Connector");
        addSignalConnectorEntry(mechanicalEntries, "PROP-400-CON-S", "Signal Connector");

        MechanicalBOM propulsionMech = new MechanicalBOM();
        propulsionMech.setBomEntries(mechanicalEntries);

        // Generate Gerber files
        pcbGenerator.addGerberFilesToPCBA(propulsionBoard, "PROP-MAIN-01");

        // Add to batch
        batch.addPCBA(propulsionBoard);
        batch.addMechanical(propulsionMech);
    }

    /**
     * Adds the communications system to the submarine.
     */
    public void addCommunicationsSystem(PlannedProductionBatch batch) {
        // Create communications PCBA components
        List<BOMEntry> pcbaEntries = new ArrayList<>();

        // Main processor and DSP
        addProcessorEntry(pcbaEntries, "STM32H735IGK6", "Communications Control MCU", true);
        addProcessorEntry(pcbaEntries, "TMS320C5535AZCHA", "Digital Signal Processor", true);

        // RF and communications
        addRFEntry(pcbaEntries, "PD55003", "RF Power Amplifier");
        addRFEntry(pcbaEntries, "ADS131E08IPAG", "24-Bit ADC");
        addRFEntry(pcbaEntries, "CC1200RHBR", "High-Performance RF Transceiver");

        // Network interfaces
        addNetworkEntry(pcbaEntries, "DP83825IRHBR", "Industrial Ethernet PHY");
        addNetworkEntry(pcbaEntries, "KSZ8895MQXCA", "5-Port Ethernet Switch");

        // Control and monitoring
        addProcessorEntry(pcbaEntries, "STM32L412K8U6", "Ultra-Low-Power MCU", false);
        addSensorEntry(pcbaEntries, "NEO-M9N", "GNSS Module");
        addPowerEntry(pcbaEntries, "TPS65281", "Power Management IC");

        // Create the communications PCBA
        PCBABOM communicationsBoard = new PCBABOM(
                "COM-MAIN-01",
                "Communications Board",
                "R1.0",
                pcbaEntries
        );

        // Add mechanical components
        List<BOMEntry> mechanicalEntries = new ArrayList<>();

        // Housing and transducers
        addHousingEntry(mechanicalEntries, "COM-600-HSG", "Communications Housing");
        addTransducerEntry(mechanicalEntries, "AT12ET", "Acoustic Transducer");
        addAntennaEntry(mechanicalEntries, "COM-600-ANT", "Surface Communications Antenna");

        // Mounting and connections
        addMountingEntry(mechanicalEntries, "COM-600-MNT", "Communications Mounting Kit");
        addConnectorEntry(mechanicalEntries, "COM-600-CON-D", "Data Connector");
        addPowerConnectorEntry(mechanicalEntries, "COM-600-CON-P", "Power Connector");

        MechanicalBOM communicationsMech = new MechanicalBOM();
        communicationsMech.setBomEntries(mechanicalEntries);

        // Generate Gerber files
        pcbGenerator.addGerberFilesToPCBA(communicationsBoard, "COM-MAIN-01");

        // Add to batch
        batch.addPCBA(communicationsBoard);
        batch.addMechanical(communicationsMech);
    }

    // More helper methods for component creation
    private void addPowerEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("power_rating", "Marine grade");
        entry.addSpec("protection", "Short circuit, overcurrent, thermal");
        entry.addSpec("monitoring", "Current, voltage, temperature");
        entries.add(entry);
    }

    private void addDriverEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("driver_type", "3-phase");
        entry.addSpec("current_sense", "Integrated");
        entry.addSpec("protection", "Overcurrent, thermal");
        entries.add(entry);
    }
    /**
     * Adds the sensor suite to the submarine.
     */
    public void addSensorSystem(PlannedProductionBatch batch) {
        // Create sensor interface PCBA components
        List<BOMEntry> pcbaEntries = new ArrayList<>();

        // Main processor and interfaces
        addProcessorEntry(pcbaEntries, "STM32H723ZGT6", "Sensor Interface MCU", true);

        // Water quality sensors
        addWaterQualityEntry(pcbaEntries, "WQ-COMBO-01", "Water Quality Multi-Parameter Sensor");
        addWaterQualityEntry(pcbaEntries, "WATER-PH-01", "pH Sensor Module");
        addWaterQualityEntry(pcbaEntries, "WATER-DO-01", "Dissolved Oxygen Sensor");
        addWaterQualityEntry(pcbaEntries, "WATER-COND-01", "Conductivity Sensor");

        // Interface and multiplexing
        addInterfaceEntry(pcbaEntries, "TCA9548A", "I2C Multiplexer");
        addInterfaceEntry(pcbaEntries, "MAX31855KASA", "Thermocouple Interface");

        // Signal processing
        addProcessorEntry(pcbaEntries, "TMS320C6748AGZK", "Digital Signal Processor", true);
        addADCEntry(pcbaEntries, "AD9269BBPZ-40", "Dual ADC for Sonar");
        addADCEntry(pcbaEntries, "ADS1278IPAG", "8-Channel Delta-Sigma ADC");
        addInterfaceEntry(pcbaEntries, "ADG1607BRUZ", "Analog Multiplexer");

        // Image processing
        addProcessorEntry(pcbaEntries, "TW8844-LA1-CR", "Image Signal Processor", false);
        addMemoryEntry(pcbaEntries, "IS42S16400J", "Video Frame Buffer", "64MB");

        // Power management
        addPowerEntry(pcbaEntries, "TPS65023", "Power Management IC");

        // Storage
        addMemoryEntry(pcbaEntries, "SFOM64G-2AEP1-I-DK-51-STD", "Industrial Grade Storage Module", "64GB");
        addMemoryEntry(pcbaEntries, "W25Q256JWEIQ", "Backup Storage Flash", "256Mb");

        // Create the sensor interface PCBA
        PCBABOM sensorBoard = new PCBABOM(
                "SENS-MAIN-01",
                "Sensor Interface Board",
                "R1.0",
                pcbaEntries
        );

        // Add mechanical components
        List<BOMEntry> mechanicalEntries = new ArrayList<>();

        // Housing and pods
        addHousingEntry(mechanicalEntries, "SENS-700-POD", "Multi-Sensor Pod Housing");
        addSensorEntry(mechanicalEntries, "SENS-700-SON", "Forward-Looking Sonar Array");

        // Optical systems
        addOpticalEntry(mechanicalEntries, "SENS-700-OPT", "Camera Viewport Assembly");
        addOpticalEntry(mechanicalEntries, "SENS-700-LED", "LED Illumination Array");

        // Sensor interfaces
        addSensorPortEntry(mechanicalEntries, "SENS-700-PORT", "Sensor Port Assembly");

        // Mounting and connections
        addMountingEntry(mechanicalEntries, "SENS-700-MNT", "Sensor Pod Mounting Kit");
        addConnectorEntry(mechanicalEntries, "SENS-700-CON-M", "Main System Connector");
        addConnectorEntry(mechanicalEntries, "SENS-700-CON-S", "Sensor Array Connector");

        MechanicalBOM sensorMech = new MechanicalBOM();
        sensorMech.setBomEntries(mechanicalEntries);

        // Generate Gerber files
        pcbGenerator.addGerberFilesToPCBA(sensorBoard, "SENS-MAIN-01");

        // Add to batch
        batch.addPCBA(sensorBoard);
        batch.addMechanical(sensorMech);
    }

    // Remaining helper methods for component creation
    private void addWaterQualityEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("measurement_type", "Continuous");
        entry.addSpec("calibration_interval", "6 months");
        entry.addSpec("accuracy_class", "Marine grade");
        entry.addSpec("response_time", "<1s");
        entries.add(entry);
    }

    private void addADCEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("resolution", "24-bit");
        entry.addSpec("sampling_rate", "100kHz");
        entry.addSpec("input_type", "Differential");
        entry.addSpec("noise_rejection", "High");
        entries.add(entry);
    }

    private void addOpticalEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("optical_material", "Sapphire glass");
        entry.addSpec("coating", "Anti-reflective");
        entry.addSpec("transmission", ">95%");
        entry.addSpec("pressure_rating", "300m depth");
        entries.add(entry);
    }

    private void addSensorPortEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("port_type", "Wet-mateable");
        entry.addSpec("contacts", "Gold-plated");
        entry.addSpec("sealing", "Double O-ring");
        entry.addSpec("alignment", "Keyed");
        entries.add(entry);
    }

    private void addHousingEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("material", "316L Stainless Steel");
        entry.addSpec("treatment", "Passivated");
        entry.addSpec("sealing", "Double O-ring");
        entry.addSpec("pressure_compensation", "Oil-filled");
        entries.add(entry);
    }

    private void addAntennaEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("antenna_type", "Pressure-compensated");
        entry.addSpec("frequency_range", "Multi-band");
        entry.addSpec("gain", "High-gain");
        entry.addSpec("pattern", "Omnidirectional");
        entries.add(entry);
    }

    private void addTransducerEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("frequency_range", "20-50kHz");
        entry.addSpec("beam_pattern", "Directional");
        entry.addSpec("sensitivity", "High");
        entry.addSpec("power_handling", "500W");
        entries.add(entry);
    }

    private void addCoolingEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("cooling_type", "Active liquid");
        entry.addSpec("flow_rate", "2L/min");
        entry.addSpec("heat_capacity", "500W");
        entry.addSpec("redundancy", "Dual pump");
        entries.add(entry);
    }

    private void addSealingEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("seal_type", "Double O-ring");
        entry.addSpec("material", "Fluorocarbon");
        entry.addSpec("durometer", "70A");
        entry.addSpec("chemical_resistance", "High");
        entries.add(entry);
    }

    private void addMountingEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("mount_type", "Quick release");
        entry.addSpec("material", "316L Stainless Steel");
        entry.addSpec("vibration_isolation", "Rubber dampers");
        entry.addSpec("alignment", "Keyed");
        entries.add(entry);
    }

    private void addThermalEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("thermal_conductivity", "High");
        entry.addSpec("heat_dissipation", "Active");
        entry.addSpec("cooling_type", "Liquid");
        entry.addSpec("flow_rate", "2L/min");
        entries.add(entry);
    }

    private void addConnectorEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("connector_type", "Wet-mateable");
        entry.addSpec("contacts", "Gold-plated");
        entry.addSpec("sealing", "Double O-ring");
        entry.addSpec("mating_cycles", "500");
        entries.add(entry);
    }

    private void addInterfaceEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("interface_type", "Digital");
        entry.addSpec("protocol", "Industrial standard");
        entry.addSpec("isolation", "Galvanic");
        entry.addSpec("protection", "ESD/EMI");
        entries.add(entry);
    }

    private void addCustomEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entries.add(entry);
    }

    private void addPowerConnectorEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("connector_type", "High-current wet-mateable");
        entry.addSpec("current_rating", "30A");
        entry.addSpec("voltage_rating", "100V DC");
        entry.addSpec("sealing", "Double O-ring");
        entries.add(entry);
    }

    private void addSignalConnectorEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("connector_type", "Signal wet-mateable");
        entry.addSpec("contacts", "Gold-plated");
        entry.addSpec("shielding", "EMI/RFI");
        entry.addSpec("sealing", "Double O-ring");
        entries.add(entry);
    }

    private void addMotorEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("motor_type", "Brushless DC");
        entry.addSpec("power_rating", "350W");
        entry.addSpec("speed_rating", "3000 RPM");
        entry.addSpec("sealing", "IP68");
        entries.add(entry);
    }

    private void addPropulsionEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("material", "Marine grade");
        entry.addSpec("thrust_rating", "High");
        entry.addSpec("efficiency", "85%");
        entry.addSpec("cavitation_resistant", "Yes");
        entries.add(entry);
    }

    private void addBearingEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("bearing_type", "Deep groove");
        entry.addSpec("material", "Ceramic hybrid");
        entry.addSpec("sealing", "Double-lip");
        entry.addSpec("lubrication", "Oil-filled");
        entries.add(entry);
    }

    private void addRFEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("frequency_range", "Marine band");
        entry.addSpec("power_output", "Adjustable");
        entry.addSpec("modulation", "Multiple");
        entry.addSpec("shielding", "EMI/RFI");
        entries.add(entry);
    }

    private void addNetworkEntry(List<BOMEntry> entries, String mpn, String description) {
        BOMEntry entry = new BOMEntry();
        entry.setMpn(mpn);
        entry.setDescription(description);
        componentSpecs.addEnvironmentalSpecs(entry);
        entry.addSpec("network_type", "Industrial Ethernet");
        entry.addSpec("speed", "100Mbps");
        entry.addSpec("redundancy", "Dual");
        entry.addSpec("protection", "ESD/Surge");
        entries.add(entry);
    }
}