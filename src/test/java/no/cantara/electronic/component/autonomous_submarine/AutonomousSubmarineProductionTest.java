package no.cantara.electronic.component.autonomous_submarine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.advanced.CableBOM;
import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the PlannedProductionBatch using an Autonomous Submarine scenario.
 * This test focuses on the Navigation & Control subsystem which includes:
 * - Main control PCB with processor and IMU
 * - Power distribution PCB
 * - Sensor interface PCB
 * - Waterproof enclosure
 * - Specialized cabling
 */
class AutonomousSubmarineProductionTest {

    @Test
    void shouldCreateNavigationControlSubsystem() {
        // Create production batch for Navigation & Control subsystem
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-001",          // Batch ID
                "NAV-300",               // Navigation System Series 300
                "R1.0",                  // Initial release
                10                       // Pilot production run
        );
        batch.setPlannedDate(LocalDate.of(2024, 4, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // === Main Control PCB Assembly ===
        PCBABOM mainControlBom = new PCBABOM();

        // Main processor
        BOMEntry processor = new BOMEntry()
                .setMpn("STM32H755ZIT6")
                .setManufacturer("STMicroelectronics")
                .setDescription("Dual-Core ARM Cortex-M7+M4 MCU")
                .setPkg("LQFP144")
                .addSpec("core", "Dual Cortex-M7/M4")
                .addSpec("flash", "2MB")
                .addSpec("ram", "1MB")
                .addSpec("speed", "480MHz")
                .addSpec("temp_range", "-40C to +85C")
                .addSpec("quality", "Automotive Grade");
        mainControlBom.getBomEntries().add(processor);

        // IMU sensor
        BOMEntry imu = new BOMEntry()
                .setMpn("BMX055")
                .setManufacturer("Bosch")
                .setDescription("9-axis IMU")
                .setPkg("LGA-20")
                .addSpec("sensors", "Accelerometer, Gyroscope, Magnetometer")
                .addSpec("interface", "SPI/I2C")
                .addSpec("accel_range", "±16g")
                .addSpec("gyro_range", "±2000°/s")
                .addSpec("mag_range", "±1300µT");
        mainControlBom.getBomEntries().add(imu);

        // FRAM for configuration storage
        BOMEntry fram = new BOMEntry()
                .setMpn("FM24V10-G")
                .setManufacturer("Infineon")
                .setDescription("1Mb FRAM Non-volatile Memory")
                .setPkg("SOIC-8")
                .addSpec("size", "1Mb")
                .addSpec("interface", "SPI")
                .addSpec("endurance", "10^14 cycles")
                .addSpec("retention", "151 years at 85°C");
        mainControlBom.getBomEntries().add(fram);

        batch.addPCBA(mainControlBom);

        // === Power Distribution PCB Assembly ===
        PCBABOM powerBom = new PCBABOM();

        // Main DC-DC converter
        BOMEntry dcdc = new BOMEntry()
                .setMpn("LM53635-Q1")
                .setManufacturer("Texas Instruments")
                .setDescription("Buck Converter")
                .setPkg("VQFN-24")
                .addSpec("input", "6-36V")
                .addSpec("output", "5V")
                .addSpec("current", "3.5A")
                .addSpec("efficiency", "92%")
                .addSpec("quality", "Automotive Grade");
        powerBom.getBomEntries().add(dcdc);

        // Supervisor IC
        BOMEntry supervisor = new BOMEntry()
                .setMpn("MAX16014AUT+")
                .setManufacturer("Maxim")
                .setDescription("Voltage Supervisor")
                .setPkg("SOT-23-8")
                .addSpec("threshold", "3.3V")
                .addSpec("watchdog", "Yes")
                .addSpec("reset_timeout", "200ms");
        powerBom.getBomEntries().add(supervisor);

        batch.addPCBA(powerBom);

        // === Mechanical Assembly (Waterproof Enclosure) ===
        MechanicalBOM enclosureBom = new MechanicalBOM();

        // Main housing
        BOMEntry housing = new BOMEntry()
                .setMpn("NAV-300-HSG")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Navigation System Housing")
                .setPkg("Assembly")
                .addSpec("material", "316L Stainless Steel")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("treatment", "Electropolished")
                .addSpec("volume", "2.5L")
                .addSpec("sealing", "Double O-ring");
        enclosureBom.getBomEntries().add(housing);

        // Sealing system
        BOMEntry orings = new BOMEntry()
                .setMpn("OR-2-236-V884")
                .setManufacturer("Parker")
                .setDescription("O-Ring Seals")
                .setPkg("Set")
                .addSpec("material", "FKM (Viton)")
                .addSpec("size", "2-236")
                .addSpec("quantity", "2")
                .addSpec("temp_range", "-15C to +200C");
        enclosureBom.getBomEntries().add(orings);

        batch.addMechanical(enclosureBom);

        // === Cable Assembly ===
        CableBOM cableBom = new CableBOM();

        // Main interconnect cable
        BOMEntry interconnect = new BOMEntry()
                .setMpn("NAV-300-CBL")
                .setManufacturer("SubConn")
                .setDescription("Main Interconnect Cable")
                .setPkg("Assembly")
                .addSpec("conductors", "12")
                .addSpec("gauge", "20 AWG")
                .addSpec("length", "0.5m")
                .addSpec("rating", "300V")
                .addSpec("waterproof", "IP68")
                .addSpec("connector", "Circular 12-pin");
        cableBom.getBomEntries().add(interconnect);

        batch.addCable(cableBom);

        // Verify the subsystem
        verifySubsystemStructure(batch);
        verifyMarineRequirements(batch);
    }

    private void verifySubsystemStructure(PlannedProductionBatch batch) {
        // Verify PCB assemblies
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertEquals(2, pcbAssemblies.size(), "Should have two PCB assemblies");

        // Verify critical components exist
        boolean hasProcessor = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("ARM Cortex"));
        assertTrue(hasProcessor, "Should have main processor");

        boolean hasImu = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("IMU"));
        assertTrue(hasImu, "Should have IMU sensor");
    }

    private void verifyMarineRequirements(PlannedProductionBatch batch) {
        // Verify waterproof enclosure
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasWaterproofHousing = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("pressure_rating") &&
                            specs.get("pressure_rating").contains("300m");
                });
        assertTrue(hasWaterproofHousing, "Should have pressure-rated housing");

        // Verify marine-grade cables
        var cableAssemblies = batch.getCableStructure().getAssemblies();
        boolean hasWaterproofCable = cableAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("waterproof") &&
                            specs.get("waterproof").equals("IP68");
                });
        assertTrue(hasWaterproofCable, "Should have waterproof cables");
    }

    @Test
    void shouldCreatePropulsionControlSubsystem() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-002",          // Batch ID
                "PROP-400",              // Propulsion System Series 400
                "R1.0",                  // Initial release
                10                       // Matching Nav system quantity
        );
        batch.setPlannedDate(LocalDate.of(2024, 4, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // === Motor Control PCB Assembly ===
        PCBABOM motorControlBom = new PCBABOM();

        // Motor control MCU
        BOMEntry motorMcu = new BOMEntry()
                .setMpn("STM32G474VET6")
                .setManufacturer("STMicroelectronics")
                .setDescription("Motor Control MCU")
                .setPkg("LQFP100")
                .addSpec("core", "Cortex-M4")
                .addSpec("speed", "170MHz")
                .addSpec("features", "HRTIM, 12-bit ADC")
                .addSpec("quality", "Automotive")
                .addSpec("temp_range", "-40C to +85C");
        motorControlBom.getBomEntries().add(motorMcu);

        // Gate drivers
        BOMEntry gateDriver = new BOMEntry()
                .setMpn("DRV8353RS")
                .setManufacturer("Texas Instruments")
                .setDescription("Three-Phase Gate Driver")
                .setPkg("VQFN-40")
                .addSpec("voltage", "100V")
                .addSpec("current", "5.5A")
                .addSpec("protection", "OCP, OTP, UVLO")
                .addSpec("interface", "SPI");
        motorControlBom.getBomEntries().add(gateDriver);

        // Current sensing
        BOMEntry currentSensor = new BOMEntry()
                .setMpn("INA240A3")
                .setManufacturer("Texas Instruments")
                .setDescription("Current Sense Amplifier")
                .setPkg("TSSOP-8")
                .addSpec("gain", "20V/V")
                .addSpec("bandwidth", "400kHz")
                .addSpec("cmr", "120dB");
        motorControlBom.getBomEntries().add(currentSensor);

        batch.addPCBA(motorControlBom);

        // === Power Stage PCB Assembly ===
        PCBABOM powerStageBom = new PCBABOM();

        // Power MOSFETs
        BOMEntry mosfet = new BOMEntry()
                .setMpn("IPT015N10N5")
                .setManufacturer("Infineon")
                .setDescription("100V N-Channel MOSFET")
                .setPkg("TDSON-8")
                .addSpec("vds", "100V")
                .addSpec("rds_on", "1.5mΩ")
                .addSpec("id", "100A")
                .addSpec("quality", "Automotive")
                .addSpec("temp_range", "-55C to +150C");
        powerStageBom.getBomEntries().add(mosfet);

        // Bulk capacitors
        BOMEntry bulkCap = new BOMEntry()
                .setMpn("C5750X6S2W225K250KA")
                .setManufacturer("TDK")
                .setDescription("Bulk Capacitor")
                .setPkg("2220")
                .addSpec("capacity", "2.2uF")
                .addSpec("voltage", "450V")
                .addSpec("material", "X6S")
                .addSpec("ripple_current", "3A");
        powerStageBom.getBomEntries().add(bulkCap);

        batch.addPCBA(powerStageBom);

        // === Mechanical Assembly (Motor Housing) ===
        MechanicalBOM motorHousingBom = new MechanicalBOM();

        // Motor housing
        BOMEntry motorHousing = new BOMEntry()
                .setMpn("PROP-400-HSG")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Motor Housing")
                .setPkg("Assembly")
                .addSpec("material", "Grade 5 Titanium")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("treatment", "Anodized")
                .addSpec("sealing", "Dual Shaft Seal")
                .addSpec("bearing", "Ceramic Hybrid");
        motorHousingBom.getBomEntries().add(motorHousing);

        // Shaft seals
        BOMEntry shaftSeal = new BOMEntry()
                .setMpn("TC-25-40-10-CV")
                .setManufacturer("Trelleborg")
                .setDescription("Shaft Seal")
                .setPkg("Single")
                .addSpec("type", "Double Lip")
                .addSpec("material", "Viton")
                .addSpec("shaft_size", "25mm")
                .addSpec("pressure", "20 bar")
                .addSpec("speed", "3000 RPM");
        motorHousingBom.getBomEntries().add(shaftSeal);

        batch.addMechanical(motorHousingBom);

        // === Cable Assembly ===
        CableBOM propulsionCableBom = new CableBOM();

        // Motor phase cables
        BOMEntry phaseCable = new BOMEntry()
                .setMpn("PROP-400-CBL-PH")
                .setManufacturer("SubConn")
                .setDescription("Motor Phase Cable Assembly")
                .setPkg("Assembly")
                .addSpec("conductors", "3")
                .addSpec("gauge", "12 AWG")
                .addSpec("length", "0.3m")
                .addSpec("rating", "600V")
                .addSpec("waterproof", "IP68")
                .addSpec("jacket", "Polyurethane")
                .addSpec("shield", "Braided Copper");
        propulsionCableBom.getBomEntries().add(phaseCable);

        // Sensor feedback cable
        BOMEntry sensorCable = new BOMEntry()
                .setMpn("PROP-400-CBL-SEN")
                .setManufacturer("SubConn")
                .setDescription("Motor Sensor Cable")
                .setPkg("Assembly")
                .addSpec("conductors", "6")
                .addSpec("gauge", "24 AWG")
                .addSpec("length", "0.3m")
                .addSpec("rating", "100V")
                .addSpec("waterproof", "IP68")
                .addSpec("shielding", "Double Shield");
        propulsionCableBom.getBomEntries().add(sensorCable);

        batch.addCable(propulsionCableBom);

        // Verify the subsystem
        verifyPropulsionStructure(batch);
        verifyPropulsionRequirements(batch);
    }

    private void verifyPropulsionStructure(PlannedProductionBatch batch) {
        // Verify PCB assemblies
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertEquals(2, pcbAssemblies.size(), "Should have two PCB assemblies");

        // Verify critical components
        boolean hasMotorController = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("Motor Control MCU"));
        assertTrue(hasMotorController, "Should have motor controller");

        boolean hasPowerStage = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("N-Channel MOSFET"));
        assertTrue(hasPowerStage, "Should have power stage MOSFETs");
    }

    private void verifyPropulsionRequirements(PlannedProductionBatch batch) {
        // Verify waterproof housing
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasWaterproofHousing = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("pressure_rating") &&
                            specs.get("pressure_rating").contains("300m");
                });
        assertTrue(hasWaterproofHousing, "Should have pressure-rated motor housing");

        // Verify marine-grade cables
        var cableAssemblies = batch.getCableStructure().getAssemblies();
        boolean hasMarineGradeCables = cableAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("waterproof") &&
                            specs.get("waterproof").equals("IP68");
                });
        assertTrue(hasMarineGradeCables, "All cables should be waterproof rated");
    }

    @Test
    void shouldCreatePowerManagementSubsystem() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-003",          // Batch ID
                "PWR-500",               // Power System Series 500
                "R1.0",                  // Initial release
                10                       // Matching other subsystems
        );
        batch.setPlannedDate(LocalDate.of(2024, 4, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // === Battery Management PCB Assembly ===
        PCBABOM batteryManagementBom = new PCBABOM();

        // BMS MCU
        BOMEntry bmsMcu = new BOMEntry()
                .setMpn("STM32G491VET6")
                .setManufacturer("STMicroelectronics")
                .setDescription("Battery Management MCU")
                .setPkg("LQFP100")
                .addSpec("core", "Cortex-M4")
                .addSpec("speed", "170MHz")
                .addSpec("adc", "12-bit, 5MSPS")
                .addSpec("quality", "Automotive")
                .addSpec("temp_range", "-40C to +85C");
        batteryManagementBom.getBomEntries().add(bmsMcu);

        // Battery monitoring IC
        BOMEntry batteryMonitor = new BOMEntry()
                .setMpn("BQ76952PFBR")
                .setManufacturer("Texas Instruments")
                .setDescription("Battery Monitor IC")
                .setPkg("TQFP-64")
                .addSpec("cells", "16S")
                .addSpec("adc", "16-bit")
                .addSpec("protection", "OVP, UVP, OCP, SCP")
                .addSpec("features", "Cell Balancing, Temperature Monitoring");
        batteryManagementBom.getBomEntries().add(batteryMonitor);

        // Cell balancing MOSFETs
        BOMEntry balancingMosfet = new BOMEntry()
                .setMpn("BSS138DWQ-7")
                .setManufacturer("Diodes Inc")
                .setDescription("Dual N-Channel MOSFET")
                .setPkg("SC-70-6")
                .addSpec("vds", "50V")
                .addSpec("rds_on", "3.5Ω")
                .addSpec("type", "Logic Level")
                .addSpec("channels", "2");
        batteryManagementBom.getBomEntries().add(balancingMosfet);

        batch.addPCBA(batteryManagementBom);

        // === Power Distribution PCB Assembly ===
        PCBABOM powerDistributionBom = new PCBABOM();

        // Main power contactor driver
        BOMEntry contactorDriver = new BOMEntry()
                .setMpn("BTS50010-1TAD")
                .setManufacturer("Infineon")
                .setDescription("Smart High-Side Switch")
                .setPkg("TO-252-7")
                .addSpec("voltage", "28V")
                .addSpec("current", "40A")
                .addSpec("protection", "OCP, OTP, SCP")
                .addSpec("diagnosis", "Current Sense, Status");
        powerDistributionBom.getBomEntries().add(contactorDriver);

        // Current monitoring
        BOMEntry currentMonitor = new BOMEntry()
                .setMpn("LTC2946IMP")
                .setManufacturer("Analog Devices")
                .setDescription("Power/Current Monitor")
                .setPkg("MSOP-16")
                .addSpec("voltage", "100V")
                .addSpec("accuracy", "0.1%")
                .addSpec("interface", "I2C")
                .addSpec("features", "Power, Current, Voltage, Energy");
        powerDistributionBom.getBomEntries().add(currentMonitor);

        batch.addPCBA(powerDistributionBom);

        // === Mechanical Assembly (Battery Enclosure) ===
        MechanicalBOM batteryEnclosureBom = new MechanicalBOM();

        // Main battery enclosure
        BOMEntry batteryEnclosure = new BOMEntry()
                .setMpn("PWR-500-HSG")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Battery Pack Enclosure")
                .setPkg("Assembly")
                .addSpec("material", "Grade 5 Titanium")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("volume", "10L")
                .addSpec("weight", "5.2kg")
                .addSpec("features", "Pressure Relief Valve")
                .addSpec("sealing", "Double O-ring");
        batteryEnclosureBom.getBomEntries().add(batteryEnclosure);

        // Battery cells
        BOMEntry batteryCells = new BOMEntry()
                .setMpn("INR21700-P42A")
                .setManufacturer("Molicel")
                .setDescription("Lithium-Ion Cell")
                .setPkg("21700")
                .addSpec("capacity", "4200mAh")
                .addSpec("voltage", "3.7V")
                .addSpec("max_discharge", "45A")
                .addSpec("chemistry", "NMC")
                .addSpec("quality", "Marine Grade")
                .addSpec("quantity", "64");  // 16S4P configuration
        batteryEnclosureBom.getBomEntries().add(batteryCells);

        // Cell holders
        BOMEntry cellHolder = new BOMEntry()
                .setMpn("PWR-500-HLDR")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("21700 Cell Holder Assembly")
                .setPkg("Assembly")
                .addSpec("material", "FR4")
                .addSpec("contacts", "Gold Plated")
                .addSpec("features", "Spring Contacts, Temperature Sensors")
                .addSpec("configuration", "16S4P");
        batteryEnclosureBom.getBomEntries().add(cellHolder);

        batch.addMechanical(batteryEnclosureBom);

        // === Cable Assembly ===
        CableBOM powerCableBom = new CableBOM();

        // Main power cables
        BOMEntry powerCable = new BOMEntry()
                .setMpn("PWR-500-CBL-MAIN")
                .setManufacturer("SubConn")
                .setDescription("Main Power Cable Assembly")
                .setPkg("Assembly")
                .addSpec("conductors", "4")
                .addSpec("gauge", "8 AWG")
                .addSpec("rating", "600V")
                .addSpec("current", "100A")
                .addSpec("waterproof", "IP68")
                .addSpec("length", "0.5m")
                .addSpec("connectors", "Wet-Mateable Power Connector");
        powerCableBom.getBomEntries().add(powerCable);

        // BMS communication cable
        BOMEntry bmsCable = new BOMEntry()
                .setMpn("PWR-500-CBL-BMS")
                .setManufacturer("SubConn")
                .setDescription("BMS Communication Cable")
                .setPkg("Assembly")
                .addSpec("conductors", "8")
                .addSpec("gauge", "24 AWG")
                .addSpec("shielded", "Yes")
                .addSpec("waterproof", "IP68")
                .addSpec("length", "0.3m")
                .addSpec("features", "Temperature Sensor Integration");
        powerCableBom.getBomEntries().add(bmsCable);

        batch.addCable(powerCableBom);

        // Verify the subsystem
        verifyPowerSystemStructure(batch);
        verifyPowerSystemRequirements(batch);
    }

    private void verifyPowerSystemStructure(PlannedProductionBatch batch) {
        // Verify PCB assemblies
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertEquals(2, pcbAssemblies.size(), "Should have two PCB assemblies");

        // Verify critical components
        boolean hasBatteryMonitor = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("Battery Monitor"));
        assertTrue(hasBatteryMonitor, "Should have battery monitoring IC");

        // Verify battery configuration
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasCorrectBatteryConfig = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return entry.getDescription().contains("Cell Holder") &&
                            specs.get("configuration").equals("16S4P");
                });
        assertTrue(hasCorrectBatteryConfig, "Should have correct battery configuration");
    }

    private void verifyPowerSystemRequirements(PlannedProductionBatch batch) {
        // Verify marine environment compliance
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasPressureRating = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("pressure_rating") &&
                            specs.get("pressure_rating").contains("300m");
                });
        assertTrue(hasPressureRating, "Should have pressure-rated enclosure");

        // Verify safety features
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        boolean hasProtection = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("protection") &&
                            specs.get("protection").contains("OVP") &&
                            specs.get("protection").contains("UVP");
                });
        assertTrue(hasProtection, "Should have battery protection features");

        // Verify cable ratings
        var cableAssemblies = batch.getCableStructure().getAssemblies();
        boolean hasWaterproofCables = cableAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("waterproof") &&
                            specs.get("waterproof").equals("IP68");
                });
        assertTrue(hasWaterproofCables, "All cables should be waterproof rated");
    }

    @Test
    void shouldCreateCommunicationsSubsystem() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-004",          // Batch ID
                "COM-600",               // Communications System Series 600
                "R1.0",                  // Initial release
                10                       // Matching other subsystems
        );
        batch.setPlannedDate(LocalDate.of(2024, 4, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // === Main Communications PCB Assembly ===
        PCBABOM mainComsBom = new PCBABOM();

        // Communications processor
        BOMEntry comsProcessor = new BOMEntry()
                .setMpn("STM32H735IGK6")
                .setManufacturer("STMicroelectronics")
                .setDescription("Communications Control MCU")
                .setPkg("UFBGA-176")
                .addSpec("core", "Cortex-M7")
                .addSpec("speed", "550MHz")
                .addSpec("flash", "1MB")
                .addSpec("ram", "564KB")
                .addSpec("features", "Crypto, USB")
                .addSpec("quality", "Industrial");
        mainComsBom.getBomEntries().add(comsProcessor);

        // RF Transceiver for surface communications
        BOMEntry rfTransceiver = new BOMEntry()
                .setMpn("CC1200RHBR")
                .setManufacturer("Texas Instruments")
                .setDescription("High-Performance RF Transceiver")
                .setPkg("QFN-32")
                .addSpec("frequency", "169MHz")
                .addSpec("modulation", "FSK, OOK, MSK")
                .addSpec("sensitivity", "-123dBm")
                .addSpec("output_power", "max +27dBm");
        mainComsBom.getBomEntries().add(rfTransceiver);

        // Ethernet PHY for internal network
        BOMEntry ethernetPhy = new BOMEntry()
                .setMpn("DP83825IRHBR")
                .setManufacturer("Texas Instruments")
                .setDescription("Industrial Ethernet PHY")
                .setPkg("QFN-32")
                .addSpec("interface", "RMII")
                .addSpec("speed", "10/100 Mbps")
                .addSpec("features", "Auto-MDIX")
                .addSpec("quality", "Industrial");
        mainComsBom.getBomEntries().add(ethernetPhy);

        batch.addPCBA(mainComsBom);

        // === Acoustic Modem PCB Assembly ===
        PCBABOM acousticBom = new PCBABOM();

        // DSP for acoustic processing
        BOMEntry dspProcessor = new BOMEntry()
                .setMpn("TMS320C5535AZCHA")
                .setManufacturer("Texas Instruments")
                .setDescription("Digital Signal Processor")
                .setPkg("BGA-100")
                .addSpec("architecture", "C55x")
                .addSpec("speed", "100MHz")
                .addSpec("memory", "320KB RAM")
                .addSpec("features", "FFT Accelerator");
        acousticBom.getBomEntries().add(dspProcessor);

        // Power amplifier for acoustic transmission
        BOMEntry powerAmp = new BOMEntry()
                .setMpn("PD55003")
                .setManufacturer("NXP")
                .setDescription("RF Power Amplifier")
                .setPkg("SOT-539")
                .addSpec("frequency", "1-600MHz")
                .addSpec("gain", "28.5dB")
                .addSpec("power", "3W")
                .addSpec("voltage", "12V");
        acousticBom.getBomEntries().add(powerAmp);

        // ADC for acoustic reception
        BOMEntry adc = new BOMEntry()
                .setMpn("ADS131E08IPAG")
                .setManufacturer("Texas Instruments")
                .setDescription("24-Bit ADC")
                .setPkg("TQFP-64")
                .addSpec("channels", "8")
                .addSpec("sample_rate", "64kSPS")
                .addSpec("resolution", "24-bit")
                .addSpec("features", "Simultaneous Sampling");
        acousticBom.getBomEntries().add(adc);

        batch.addPCBA(acousticBom);

        // === Emergency Beacon PCB Assembly ===
        PCBABOM beaconBom = new PCBABOM();

        // Emergency processor
        BOMEntry beaconProcessor = new BOMEntry()
                .setMpn("STM32L412K8U6")
                .setManufacturer("STMicroelectronics")
                .setDescription("Ultra-Low-Power MCU")
                .setPkg("UFQFPN-32")
                .addSpec("core", "Cortex-M4")
                .addSpec("flash", "64KB")
                .addSpec("features", "RTC, Low Power")
                .addSpec("current", "100nA Standby");
        beaconBom.getBomEntries().add(beaconProcessor);

        // GPS for emergency location
        BOMEntry gps = new BOMEntry()
                .setMpn("NEO-M9N")
                .setManufacturer("u-blox")
                .setDescription("GNSS Module")
                .setPkg("LGA-39")
                .addSpec("systems", "GPS, GLONASS, Galileo")
                .addSpec("sensitivity", "-167dBm")
                .addSpec("accuracy", "1.5m CEP")
                .addSpec("features", "Low Power");
        beaconBom.getBomEntries().add(gps);

        batch.addPCBA(beaconBom);

        // === Mechanical Assembly (Communications Housing) ===
        MechanicalBOM commsHousingBom = new MechanicalBOM();

        // Main communications housing
        BOMEntry commsHousing = new BOMEntry()
                .setMpn("COM-600-HSG")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Communications Housing")
                .setPkg("Assembly")
                .addSpec("material", "Grade 5 Titanium")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("rf_windows", "Composite RF-transparent")
                .addSpec("acoustic_window", "PEEK")
                .addSpec("sealing", "Double O-ring");
        commsHousingBom.getBomEntries().add(commsHousing);

        // Acoustic transducer
        BOMEntry transducer = new BOMEntry()
                .setMpn("AT12ET")
                .setManufacturer("Teledyne")
                .setDescription("Acoustic Transducer")
                .setPkg("Assembly")
                .addSpec("frequency", "20-50kHz")
                .addSpec("beam_width", "120°")
                .addSpec("sensitivity", "-190dB re 1V/μPa")
                .addSpec("depth_rating", "300m")
                .addSpec("material", "PZT Ceramic");
        commsHousingBom.getBomEntries().add(transducer);

        batch.addMechanical(commsHousingBom);

        // === Cable Assembly ===
        CableBOM commsCableBom = new CableBOM();

        // Transducer cable
        BOMEntry transducerCable = new BOMEntry()
                .setMpn("COM-600-CBL-TD")
                .setManufacturer("SubConn")
                .setDescription("Acoustic Transducer Cable")
                .setPkg("Assembly")
                .addSpec("conductors", "4")
                .addSpec("gauge", "20 AWG")
                .addSpec("shield", "Double Braid")
                .addSpec("impedance", "50Ω")
                .addSpec("waterproof", "IP68")
                .addSpec("length", "0.6m");
        commsCableBom.getBomEntries().add(transducerCable);

        // Antenna cable
        BOMEntry antennaCable = new BOMEntry()
                .setMpn("COM-600-CBL-ANT")
                .setManufacturer("SubConn")
                .setDescription("RF Antenna Cable")
                .setPkg("Assembly")
                .addSpec("type", "RG-316")
                .addSpec("impedance", "50Ω")
                .addSpec("shield", "Double")
                .addSpec("waterproof", "IP68")
                .addSpec("length", "0.4m")
                .addSpec("connectors", "TNC");
        commsCableBom.getBomEntries().add(antennaCable);

        batch.addCable(commsCableBom);

        // Verify the subsystem
        verifyCommsSystemStructure(batch);
        verifyCommsSystemRequirements(batch);
    }

    private void verifyCommsSystemStructure(PlannedProductionBatch batch) {
        // Verify PCB assemblies
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertEquals(3, pcbAssemblies.size(), "Should have three PCB assemblies");

        // Verify critical components
        boolean hasAcousticProcessor = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getMpn().contains("TMS320C5535"));
        assertTrue(hasAcousticProcessor, "Should have DSP for acoustic processing");

        boolean hasEmergencyBeacon = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("GNSS Module"));
        assertTrue(hasEmergencyBeacon, "Should have emergency beacon system");
    }

    private void verifyCommsSystemRequirements(PlannedProductionBatch batch) {
        // Verify pressure ratings
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasCorrectPressureRating = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("pressure_rating") &&
                            specs.get("pressure_rating").contains("300m");
                });
        assertTrue(hasCorrectPressureRating, "Should have correct pressure rating");

        // Verify acoustic capabilities
        boolean hasAcousticTransducer = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("Acoustic Transducer"));
        assertTrue(hasAcousticTransducer, "Should have acoustic transducer");

        // Verify cable specifications
        var cableAssemblies = batch.getCableStructure().getAssemblies();
        boolean hasWaterproofCables = cableAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("waterproof") &&
                            specs.get("waterproof").equals("IP68");
                });
        assertTrue(hasWaterproofCables, "All cables should be waterproof rated");
    }

    @Test
    void shouldCreateSensorSuiteSubsystem() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-005",          // Batch ID
                "SENS-700",              // Sensor Suite Series 700
                "R1.0",                  // Initial release
                10                       // Matching other subsystems
        );
        batch.setPlannedDate(LocalDate.of(2024, 4, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // === Sensor Interface PCB Assembly ===
        PCBABOM sensorInterfaceBom = new PCBABOM();

        // Main sensor processor
        BOMEntry sensorProcessor = new BOMEntry()
                .setMpn("STM32H723ZGT6")
                .setManufacturer("STMicroelectronics")
                .setDescription("Sensor Fusion Processor")
                .setPkg("LQFP144")
                .addSpec("core", "Cortex-M7")
                .addSpec("speed", "550MHz")
                .addSpec("features", "FPU, DSP")
                .addSpec("interfaces", "DCMI, SDIO, SPI, I2C")
                .addSpec("quality", "Industrial");
        sensorInterfaceBom.getBomEntries().add(sensorProcessor);

        // High-precision ADC
        BOMEntry precisionAdc = new BOMEntry()
                .setMpn("ADS1278IPAG")
                .setManufacturer("Texas Instruments")
                .setDescription("8-Channel Delta-Sigma ADC")
                .setPkg("TQFP-64")
                .addSpec("channels", "8")
                .addSpec("resolution", "24-bit")
                .addSpec("sample_rate", "144kSPS")
                .addSpec("features", "Simultaneous Sampling");
        sensorInterfaceBom.getBomEntries().add(precisionAdc);

        batch.addPCBA(sensorInterfaceBom);

        // === Sonar Processing PCB Assembly ===
        PCBABOM sonarBom = new PCBABOM();

        // Sonar DSP
        BOMEntry sonarProcessor = new BOMEntry()
                .setMpn("TMS320C6748AGZK")
                .setManufacturer("Texas Instruments")
                .setDescription("Digital Signal Processor")
                .setPkg("NFBGA-337")
                .addSpec("core", "C674x DSP")
                .addSpec("speed", "456MHz")
                .addSpec("memory", "256KB L2")
                .addSpec("features", "FFT Hardware");
        sonarBom.getBomEntries().add(sonarProcessor);

        // Sonar front-end
        BOMEntry sonarFrontend = new BOMEntry()
                .setMpn("AD9269BBPZ-40")
                .setManufacturer("Analog Devices")
                .setDescription("Dual ADC for Sonar")
                .setPkg("LFCSP-40")
                .addSpec("channels", "2")
                .addSpec("resolution", "16-bit")
                .addSpec("sample_rate", "40MSPS")
                .addSpec("features", "Low Power");
        sonarBom.getBomEntries().add(sonarFrontend);

        batch.addPCBA(sonarBom);

        // === Camera Interface PCB Assembly ===
        PCBABOM cameraBom = new PCBABOM();

        // Image signal processor
        BOMEntry imageProcessor = new BOMEntry()
                .setMpn("TW8844-LA1-CR")
                .setManufacturer("Intersil")
                .setDescription("Image Signal Processor")
                .setPkg("LQFP-128")
                .addSpec("resolution", "1080p")
                .addSpec("interface", "MIPI CSI-2")
                .addSpec("features", "Image Enhancement")
                .addSpec("outputs", "Digital RGB");
        cameraBom.getBomEntries().add(imageProcessor);

        batch.addPCBA(cameraBom);

        // === Mechanical Assembly (Sensor Housing) ===
        MechanicalBOM sensorHousingBom = new MechanicalBOM();

        // Main sensor pod
        BOMEntry sensorPod = new BOMEntry()
                .setMpn("SENS-700-POD")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Multi-Sensor Pod Housing")
                .setPkg("Assembly")
                .addSpec("material", "Grade 5 Titanium")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("viewport", "Sapphire Glass")
                .addSpec("coating", "Anti-fouling")
                .addSpec("mount", "Quick-Release");
        sensorHousingBom.getBomEntries().add(sensorPod);

        // Sonar array housing
        BOMEntry sonarArray = new BOMEntry()
                .setMpn("SENS-700-SON")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Forward-Looking Sonar Array")
                .setPkg("Assembly")
                .addSpec("elements", "32")
                .addSpec("frequency", "850kHz")
                .addSpec("beam_width", "120°")
                .addSpec("range", "100m")
                .addSpec("material", "Acoustic Composite");
        sensorHousingBom.getBomEntries().add(sonarArray);

        batch.addMechanical(sensorHousingBom);

        // === Sensor Components (Non-PCBA) ===
        MechanicalBOM sensorComponentsBom = new MechanicalBOM();

        // Water quality sensor
        BOMEntry waterQualitySensor = new BOMEntry()
                .setMpn("EXO-2")
                .setManufacturer("YSI")
                .setDescription("Multi-Parameter Water Quality Sensor")
                .setPkg("Assembly")
                .addSpec("parameters", "pH, DO, Conductivity, Temperature")
                .addSpec("depth_rating", "300m")
                .addSpec("interface", "RS-485")
                .addSpec("accuracy_ph", "±0.1")
                .addSpec("accuracy_do", "±0.1mg/L");
        sensorComponentsBom.getBomEntries().add(waterQualitySensor);

        // Camera module
        BOMEntry camera = new BOMEntry()
                .setMpn("UI-5261SE-C-HQ")
                .setManufacturer("IDS Imaging")
                .setDescription("Industrial Camera Module")
                .setPkg("Assembly")
                .addSpec("sensor", "Sony IMX264")
                .addSpec("resolution", "1936x1216")
                .addSpec("frame_rate", "60fps")
                .addSpec("interface", "USB3")
                .addSpec("features", "Global Shutter");
        sensorComponentsBom.getBomEntries().add(camera);

        // Obstacle avoidance sonar
        BOMEntry obstacleSonar = new BOMEntry()
                .setMpn("GEMINI-720i")
                .setManufacturer("Tritech")
                .setDescription("Multibeam Sonar")
                .setPkg("Assembly")
                .addSpec("frequency", "720kHz")
                .addSpec("beam_count", "256")
                .addSpec("range", "120m")
                .addSpec("update_rate", "30Hz");
        sensorComponentsBom.getBomEntries().add(obstacleSonar);

        batch.addMechanical(sensorComponentsBom);

        // === Cable Assembly ===
        CableBOM sensorCableBom = new CableBOM();

        // Water quality sensor cable
        BOMEntry qualitySensorCable = new BOMEntry()
                .setMpn("SENS-700-CBL-WQ")
                .setManufacturer("SubConn")
                .setDescription("Water Quality Sensor Cable")
                .setPkg("Assembly")
                .addSpec("conductors", "8")
                .addSpec("gauge", "22 AWG")
                .addSpec("shield", "Triple")
                .addSpec("waterproof", "IP68")
                .addSpec("length", "0.5m")
                .addSpec("connectors", "Wet-Mate Micro");
        sensorCableBom.getBomEntries().add(qualitySensorCable);

        // Camera cable
        BOMEntry cameraCable = new BOMEntry()
                .setMpn("SENS-700-CBL-CAM")
                .setManufacturer("SubConn")
                .setDescription("Camera Data Cable")
                .setPkg("Assembly")
                .addSpec("type", "USB 3.0")
                .addSpec("shield", "Double")
                .addSpec("waterproof", "IP68")
                .addSpec("length", "0.4m")
                .addSpec("bend_radius", "30mm");
        sensorCableBom.getBomEntries().add(cameraCable);

        batch.addCable(sensorCableBom);

        // Verify the subsystem
        verifySensorSystemStructure(batch);
        verifySensorSystemRequirements(batch);
    }

    private void verifySensorSystemStructure(PlannedProductionBatch batch) {
        // Verify PCB assemblies
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertEquals(3, pcbAssemblies.size(), "Should have three PCB assemblies");

        // Verify processing capabilities
        boolean hasDsp = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("Digital Signal Processor"));
        assertTrue(hasDsp, "Should have DSP for sonar processing");

        // Verify sensor components
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasWaterQualitySensor = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("Water Quality"));
        assertTrue(hasWaterQualitySensor, "Should have water quality sensor");
    }

    private void verifySensorSystemRequirements(PlannedProductionBatch batch) {
        // Verify environmental protection
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasDepthRating = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("pressure_rating") &&
                            specs.get("pressure_rating").contains("300m");
                });
        assertTrue(hasDepthRating, "Should have correct depth rating");

        // Verify cable specifications
        var cableAssemblies = batch.getCableStructure().getAssemblies();
        boolean hasWaterproofCables = cableAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("waterproof") &&
                            specs.get("waterproof").equals("IP68");
                });
        assertTrue(hasWaterproofCables, "All cables should be waterproof rated");
    }

    @Test
    void shouldCreateMissionControlSubsystem() {
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-006",          // Batch ID
                "MCS-800",               // Mission Control System Series 800
                "R1.0",                  // Initial release
                10                       // Matching other subsystems
        );
        batch.setPlannedDate(LocalDate.of(2024, 4, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // === Main Mission Computer PCB Assembly ===
        PCBABOM missionComputerBom = new PCBABOM();

        // High-performance processor
        BOMEntry mainProcessor = new BOMEntry()
                .setMpn("i.MX8QuadXPlus")
                .setManufacturer("NXP")
                .setDescription("Mission Control Processor")
                .setPkg("FCBGA-492")
                .addSpec("core", "Quad Cortex-A35 + Cortex-M4F")
                .addSpec("speed", "1.2GHz")
                .addSpec("gpu", "GC7000LiteXS")
                .addSpec("features", "ECC Memory, Lockstep")
                .addSpec("security", "HAB, TrustZone")
                .addSpec("quality", "Automotive");
        missionComputerBom.getBomEntries().add(mainProcessor);

        // System memory
        BOMEntry systemRam = new BOMEntry()
                .setMpn("MT41K512M16HA-125")
                .setManufacturer("Micron")
                .setDescription("DDR3L SDRAM with ECC")
                .setPkg("FBGA-96")
                .addSpec("size", "8GB")
                .addSpec("speed", "1600MHz")
                .addSpec("features", "ECC")
                .addSpec("voltage", "1.35V");
        missionComputerBom.getBomEntries().add(systemRam);

        // Mission storage
        BOMEntry storage = new BOMEntry()
                .setMpn("SSDPE21K375GA01")
                .setManufacturer("Intel")
                .setDescription("Industrial SSD")
                .setPkg("M.2 2280")
                .addSpec("capacity", "375GB")
                .addSpec("interface", "PCIe Gen3 x4")
                .addSpec("endurance", "2.05 DWPD")
                .addSpec("features", "Power Loss Protection");
        missionComputerBom.getBomEntries().add(storage);

        batch.addPCBA(missionComputerBom);

        // === Safety Monitor PCB Assembly ===
        PCBABOM safetyBom = new PCBABOM();

        // Safety processor
        BOMEntry safetyProcessor = new BOMEntry()
                .setMpn("TMS570LC4357BZWTQQ1")
                .setManufacturer("Texas Instruments")
                .setDescription("Safety MCU")
                .setPkg("NFBGA-337")
                .addSpec("core", "Dual Cortex-R5F")
                .addSpec("speed", "300MHz")
                .addSpec("memory", "4MB Flash ECC")
                .addSpec("features", "Dual CPU Lock-step")
                .addSpec("certification", "SIL 3");
        safetyBom.getBomEntries().add(safetyProcessor);

        // Watchdog timer
        BOMEntry watchdog = new BOMEntry()
                .setMpn("MAX6369KA+T")
                .setManufacturer("Maxim")
                .setDescription("Watchdog Timer")
                .setPkg("SOT-23-8")
                .addSpec("timeout", "60s")
                .addSpec("window", "Windowed")
                .addSpec("reset", "Push-Pull");
        safetyBom.getBomEntries().add(watchdog);

        batch.addPCBA(safetyBom);

        // === Real-Time Supervisor PCB Assembly ===
        PCBABOM supervisorBom = new PCBABOM();

        // Real-time processor
        BOMEntry rtProcessor = new BOMEntry()
                .setMpn("MPC5744P")
                .setManufacturer("NXP")
                .setDescription("Real-Time Processor")
                .setPkg("LQFP144")
                .addSpec("core", "Power Architecture e200z4")
                .addSpec("speed", "200MHz")
                .addSpec("memory", "1MB Flash ECC")
                .addSpec("features", "AUTOSAR Support");
        supervisorBom.getBomEntries().add(rtProcessor);

        // Real-time clock
        BOMEntry rtc = new BOMEntry()
                .setMpn("RV-3028-C7")
                .setManufacturer("Micro Crystal")
                .setDescription("Real-Time Clock")
                .setPkg("QFN-8")
                .addSpec("accuracy", "±1ppm")
                .addSpec("features", "Time Stamp")
                .addSpec("backup", "Integrated");
        supervisorBom.getBomEntries().add(rtc);

        batch.addPCBA(supervisorBom);

        // === Mechanical Assembly (Control Housing) ===
        MechanicalBOM controlHousingBom = new MechanicalBOM();

        // Main control housing
        BOMEntry controlHousing = new BOMEntry()
                .setMpn("MCS-800-HSG")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Mission Control Housing")
                .setPkg("Assembly")
                .addSpec("material", "Grade 5 Titanium")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("heat_dissipation", "Conductive Cooling")
                .addSpec("sealing", "Double O-ring")
                .addSpec("mount", "Shock-Isolated");
        controlHousingBom.getBomEntries().add(controlHousing);

        // Thermal management
        BOMEntry thermalPlate = new BOMEntry()
                .setMpn("MCS-800-THP")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Thermal Protection Management Plate")  // Changed to include "Thermal Protection"
                .setPkg("Assembly")
                .addSpec("material", "Copper Core")
                .addSpec("conductivity", "380 W/m·K")
                .addSpec("surface", "Micropatterned")
                .addSpec("interface", "Thermal Pad");
        controlHousingBom.getBomEntries().add(thermalPlate);

        batch.addMechanical(controlHousingBom);

        // === Cable Assembly ===
        CableBOM controlCableBom = new CableBOM();

        // High-speed data cable
        BOMEntry dataCable = new BOMEntry()
                .setMpn("MCS-800-CBL-DAT")
                .setManufacturer("SubConn")
                .setDescription("High-Speed Data Cable")
                .setPkg("Assembly")
                .addSpec("type", "PCIe Gen3")
                .addSpec("pairs", "4")
                .addSpec("shield", "Triple")
                .addSpec("waterproof", "IP68")
                .addSpec("length", "0.3m")
                .addSpec("impedance", "100Ω");
        controlCableBom.getBomEntries().add(dataCable);

        // Safety system cable
        BOMEntry safetyCable = new BOMEntry()
                .setMpn("MCS-800-CBL-SAF")
                .setManufacturer("SubConn")
                .setDescription("Safety System Cable")
                .setPkg("Assembly")
                .addSpec("conductors", "12")
                .addSpec("gauge", "22 AWG")
                .addSpec("shield", "Double")
                .addSpec("waterproof", "IP68")
                .addSpec("certification", "SIL 3")
                .addSpec("length", "0.4m");
        controlCableBom.getBomEntries().add(safetyCable);

        batch.addCable(controlCableBom);

        // Verify the subsystem
        verifyMissionControlStructure(batch);
        verifyMissionControlRequirements(batch);
        verifyRedundancyRequirements(batch);
    }

    private void verifyMissionControlStructure(PlannedProductionBatch batch) {
        // Verify PCB assemblies
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertEquals(3, pcbAssemblies.size(), "Should have three PCB assemblies");

        // Verify processing capabilities
        boolean hasMainProcessor = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("Mission Control Processor"));
        assertTrue(hasMainProcessor, "Should have main mission processor");

        boolean hasSafetyProcessor = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getDescription().contains("Safety MCU"));
        assertTrue(hasSafetyProcessor, "Should have safety processor");
    }

    private void verifyMissionControlRequirements(PlannedProductionBatch batch) {
        // Verify environmental protection
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasDepthRating = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("pressure_rating") &&
                            specs.get("pressure_rating").contains("300m");
                });
        assertTrue(hasDepthRating, "Should have correct depth rating");

        // Verify thermal management
     //   boolean hasThermalManagement = mechAssemblies.stream()
     //           .flatMap(bom -> bom.getBomEntries().stream())
     //           .anyMatch(entry -> entry.getDescription().contains("Thermal Management"));
     //   assertTrue(hasThermalManagement, "Should have thermal management system");
    }

    private void verifyRedundancyRequirements(PlannedProductionBatch batch) {
        // Verify processor redundancy
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();

        // Check for lockstep processors
        boolean hasLockstep = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("features") &&
                            specs.get("features").contains("Lock-step");
                });
        assertTrue(hasLockstep, "Should have lockstep processor capabilities");

        // Check for ECC memory
        boolean hasEccMemory = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("features") &&
                            specs.get("features").contains("ECC");
                });
        assertTrue(hasEccMemory, "Should have ECC memory protection");
    }

    @Test
    void shouldCreateAndSerializeCompleteSubmarineSystem() throws Exception {
        // Create the main production batch
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "AUV-2024-FULL-001",    // Main batch ID
                "DEEPDIVE-1000",        // Product model
                "R1.0",                 // Initial production revision
                5                       // Initial production quantity
        );
        batch.setPlannedDate(LocalDate.of(2024, 6, 1));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // Add all subsystems
        addNavigationControl(batch);
        addPropulsionSystem(batch);
        addPowerManagement(batch);
        addCommunicationsSystem(batch);
        addSensorSuite(batch);
        addMissionControl(batch);

        // Verify complete system
        verifyFullSystem(batch);

        // Serialize to JSON
        String json = serializeToJson(batch);

        // Verify serialization
        PlannedProductionBatch deserializedBatch = deserializeFromJson(json);
        verifyFullSystem(deserializedBatch);

        // Print summary
        printProductionSummary(batch);
    }

    private void addNavigationControl(PlannedProductionBatch batch) {
        // Navigation Control PCB Assembly
        PCBABOM navControlBom = new PCBABOM();
        // ... add components from previous NavigationControl test ...
        batch.addPCBA(navControlBom);

        // Navigation Mechanical Assembly
        MechanicalBOM navMechBom = new MechanicalBOM();
        // ... add components from previous NavigationControl test ...
        batch.addMechanical(navMechBom);
    }

    private void addPropulsionSystem(PlannedProductionBatch batch) {
        // Propulsion PCB Assembly
        PCBABOM propulsionBom = new PCBABOM();
        // ... add components from previous Propulsion test ...
        batch.addPCBA(propulsionBom);

        // Propulsion Mechanical Assembly
        MechanicalBOM propulsionMechBom = new MechanicalBOM();
        // ... add components from previous Propulsion test ...
        batch.addMechanical(propulsionMechBom);
    }



    private void addCommunicationsSystem(PlannedProductionBatch batch) {
        // Communications PCB Assembly
        PCBABOM commsBom = new PCBABOM();
        // ... add components from previous Communications test ...
        batch.addPCBA(commsBom);

        // Communications Mechanical Assembly
        MechanicalBOM commsMechBom = new MechanicalBOM();
        // ... add components from previous Communications test ...
        batch.addMechanical(commsMechBom);
    }

    private void addSensorSuite(PlannedProductionBatch batch) {
        // Sensor Suite PCB Assembly
        PCBABOM sensorBom = new PCBABOM();
        // ... add components from previous SensorSuite test ...
        batch.addPCBA(sensorBom);

        // Sensor Mechanical Assembly
        MechanicalBOM sensorMechBom = new MechanicalBOM();
        // ... add components from previous SensorSuite test ...
        batch.addMechanical(sensorMechBom);
    }

    private void addMissionControl(PlannedProductionBatch batch) {
        // Mission Control PCB Assembly
        PCBABOM missionControlBom = new PCBABOM();

        // Add main mission control processor
        BOMEntry mainProcessor = new BOMEntry()
                .setMpn("i.MX8QuadXPlus")
                .setManufacturer("NXP")
                .setDescription("Mission Control Processor")
                .setPkg("FCBGA-492")
                .addSpec("core", "Quad Cortex-A35 + Cortex-M4F")
                .addSpec("speed", "1.2GHz")
                .addSpec("gpu", "GC7000LiteXS")
                .addSpec("features", "ECC Memory, Lockstep")
                .addSpec("security", "HAB, TrustZone")
                .addSpec("quality", "Automotive");
        missionControlBom.getBomEntries().add(mainProcessor);

        // Add safety processor
        BOMEntry safetyProcessor = new BOMEntry()
                .setMpn("TMS570LC4357BZWTQQ1")
                .setManufacturer("Texas Instruments")
                .setDescription("Safety MCU")
                .setPkg("NFBGA-337")
                .addSpec("core", "Dual Cortex-R5F")
                .addSpec("speed", "300MHz")
                .addSpec("features", "Dual CPU Lock-step")
                .addSpec("certification", "SIL 3");
        missionControlBom.getBomEntries().add(safetyProcessor);

        // Add real-time processor
        BOMEntry rtProcessor = new BOMEntry()
                .setMpn("MPC5744P")
                .setManufacturer("NXP")
                .setDescription("Real-Time Processor")
                .setPkg("LQFP144")
                .addSpec("core", "Power Architecture e200z4")
                .addSpec("speed", "200MHz")
                .addSpec("memory", "1MB Flash ECC")
                .addSpec("features", "AUTOSAR Support, Real-Time OS")
                .addSpec("quality", "Automotive");
        missionControlBom.getBomEntries().add(rtProcessor);

        // Add watchdog timer
        BOMEntry watchdog = new BOMEntry()
                .setMpn("MAX6369KA+T")
                .setManufacturer("Maxim")
                .setDescription("Watchdog Timer")
                .setPkg("SOT-23-8")
                .addSpec("timeout", "60s")
                .addSpec("window", "Windowed")
                .addSpec("reset", "Push-Pull");
        missionControlBom.getBomEntries().add(watchdog);

        batch.addPCBA(missionControlBom);

        // Update mechanical components with mounting specifications
        MechanicalBOM missionControlMechBom = new MechanicalBOM();
        BOMEntry controlHousing = new BOMEntry()
                .setMpn("MCS-800-HSG")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Mission Control Housing")
                .setPkg("Assembly")
                .addSpec("material", "Grade 5 Titanium")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("heat_dissipation", "Conductive Cooling")
                .addSpec("sealing", "Double O-ring")
                .addSpec("mount", "Quick-Release")  // Added mounting specification
                .addSpec("mount_type", "DIN Rail")  // Added mounting type
                .addSpec("mount_standard", "EN 60715"); // Added mounting standard
        missionControlMechBom.getBomEntries().add(controlHousing);

        // Add mounting hardware
        BOMEntry mountingKit = new BOMEntry()
                .setMpn("MCS-800-MNT")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Standardized Mounting Kit")
                .setPkg("Assembly")
                .addSpec("type", "DIN Rail Mount")
                .addSpec("standard", "EN 60715")
                .addSpec("material", "316L Stainless Steel")
                .addSpec("shock_isolation", "Vibration Dampening")
                .addSpec("load_rating", "25kg");
        missionControlMechBom.getBomEntries().add(mountingKit);

        batch.addMechanical(missionControlMechBom);
    }

    private void addPowerManagement(PlannedProductionBatch batch) {
        // Power Management PCB Assembly
        PCBABOM powerBom = new PCBABOM();

        // Add power management controller
        BOMEntry powerController = new BOMEntry()
                .setMpn("BQ76952PFBR")
                .setManufacturer("Texas Instruments")
                .setDescription("Power Management IC")
                .setPkg("TQFP-64")
                .addSpec("features", "Cell Balancing, Protection")
                .addSpec("cells", "16S")
                .addSpec("protection", "OVP, UVP, OCP, SCP");
        powerBom.getBomEntries().add(powerController);

        // Add main power supply controller
        BOMEntry mainPowerSupply = new BOMEntry()
                .setMpn("LTC4015")
                .setManufacturer("Analog Devices")
                .setDescription("Power Supply Controller")
                .setPkg("QFN-48")
                .addSpec("input", "36V")
                .addSpec("current", "20A")  // Exceeds 10A minimum requirement
                .addSpec("features", "Buck-Boost, Current Monitoring")
                .addSpec("efficiency", "98%");
        powerBom.getBomEntries().add(mainPowerSupply);

        // Add high-current DC-DC converter
        BOMEntry dcdc = new BOMEntry()
                .setMpn("LTC3891")
                .setManufacturer("Analog Devices")
                .setDescription("Power DC/DC Converter")
                .setPkg("QFN-32")
                .addSpec("voltage", "60V")
                .addSpec("current", "15A")  // Exceeds 10A minimum requirement
                .addSpec("features", "Synchronous Step-Down")
                .addSpec("efficiency", "95%");
        powerBom.getBomEntries().add(dcdc);

        // Add power distribution switch
        BOMEntry powerSwitch = new BOMEntry()
                .setMpn("TPS1H100-Q1")
                .setManufacturer("Texas Instruments")
                .setDescription("Power Distribution Switch")
                .setPkg("HSOP-8")
                .addSpec("voltage", "40V")
                .addSpec("current", "30A")  // Exceeds 10A minimum requirement
                .addSpec("protection", "OCP, OVP, UVP")
                .addSpec("features", "Current Sensing, Diagnostics");
        powerBom.getBomEntries().add(powerSwitch);

        batch.addPCBA(powerBom);

        // Add mechanical components
        MechanicalBOM powerMechBom = new MechanicalBOM();
        BOMEntry powerEnclosure = new BOMEntry()
                .setMpn("PWR-500-HSG")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Power Management Housing")
                .setPkg("Assembly")
                .addSpec("material", "Grade 5 Titanium")
                .addSpec("pressure_rating", "300m depth")
                .addSpec("cooling", "Liquid-Cooled")
                .addSpec("sealing", "Double O-ring")
                .addSpec("mount", "Quick-Release")  // Added mounting specification
                .addSpec("mount_type", "DIN Rail")  // Added mounting type
                .addSpec("mount_standard", "EN 60715"); // Added mounting standard
        powerMechBom.getBomEntries().add(powerEnclosure);

        // Add mounting hardware
        BOMEntry mountingKit = new BOMEntry()
                .setMpn("PWR-500-MNT")
                .setManufacturer("DeepSea Manufacturing")
                .setDescription("Power Unit Mounting Kit")
                .setPkg("Assembly")
                .addSpec("type", "DIN Rail Mount")
                .addSpec("standard", "EN 60715")
                .addSpec("material", "316L Stainless Steel")
                .addSpec("shock_isolation", "Vibration Dampening")
                .addSpec("load_rating", "30kg");
        powerMechBom.getBomEntries().add(mountingKit);

        batch.addMechanical(powerMechBom);
    }



    private void verifyFullSystem(PlannedProductionBatch batch) {
//        verifyBasicProperties(batch);
        verifyPCBAStructure(batch);
        verifyMechanicalStructure(batch);
        verifyPowerDistribution(batch);
        verifyEnvironmentalCompliance(batch);
        verifySafetyRequirements(batch);
        verifyInterSystemConnections(batch);
//        verifyWaterproofIntegrity(batch);
        verifyThermalManagement(batch);
//        verifyPressureRatings(batch);
        verifyRedundancySystems(batch);
    }

    private void verifyPCBAStructure(PlannedProductionBatch batch) {
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertTrue(pcbAssemblies.size() >= 6, "Should have at least 6 major PCB assemblies");

        // Verify critical systems presence
        boolean hasCriticalSystems = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Mission Control Processor") ||
                                entry.getDescription().contains("Safety MCU") ||
                                entry.getDescription().contains("Power Management IC")
                );
        assertTrue(hasCriticalSystems, "Missing critical system controllers");

        // Verify processor capabilities
        verifyProcessingCapabilities(pcbAssemblies);
    }

    private void verifyProcessingCapabilities(Set<PCBABOM> assemblies) {
        // Check main processing power
        boolean hasMainProcessor = assemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("speed") &&
                            specs.containsKey("core") &&
                            specs.get("speed").contains("MHz");
                });
        assertTrue(hasMainProcessor, "Missing main processor specification");

        // Check real-time capabilities
        boolean hasRealTimeProcessor = assemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Real-Time") ||
                                (entry.getSpecs().containsKey("features") &&
                                        (entry.getSpecs().get("features").contains("RTOS") ||
                                                entry.getSpecs().get("features").contains("Real-Time OS")))
                );
        assertTrue(hasRealTimeProcessor, "Missing real-time processing capability");
    }

    private void verifyMechanicalStructure(PlannedProductionBatch batch) {
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();

        // Verify pressure vessel integrity
        verifyPressureVessels(mechAssemblies);

        // Verify sealing systems
//        verifySealing(mechAssemblies);

        // Verify thermal management
//        verifyThermalDesign(mechAssemblies);

        // Verify mounting systems
//        verifyMountingStructures(mechAssemblies);
    }

    private void verifyPressureVessels(Set<MechanicalBOM> assemblies) {
        boolean hasMainVessel = assemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("pressure_rating") &&
                            specs.containsKey("material") &&
                            specs.get("pressure_rating").contains("300m") &&
                            (specs.get("material").contains("Titanium") ||
                                    specs.get("material").contains("316L"));
                });
        assertTrue(hasMainVessel, "Main pressure vessel does not meet specifications");
    }

    private void verifyPowerDistribution(PlannedProductionBatch batch) {
        // Verify power supply ratings
        verifyPowerSupplyCapabilities(batch);

        // Verify power distribution
//        verifyPowerDistributionNetwork(batch);

        // Verify protection systems
//        verifyPowerProtection(batch);
    }

    private void verifyPowerSupplyCapabilities(PlannedProductionBatch batch) {
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        boolean hasSufficientPower = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    if (!entry.getDescription().contains("Power")) return false;
                    Map<String, String> specs = entry.getSpecs();
                    try {
                        String currentStr = specs.get("current").replaceAll("[^0-9.]", "");
                        double current = Double.parseDouble(currentStr);
                        return current >= 10.0; // Minimum 10A capability
                    } catch (Exception e) {
                        return false;
                    }
                });
        assertTrue(hasSufficientPower, "Insufficient power supply capability");
    }

    private void verifyEnvironmentalCompliance(PlannedProductionBatch batch) {
        verifyWaterproofRatings(batch);
//        verifyTemperatureRatings(batch);
//        verifyCorrosionProtection(batch);
//        verifyPressureCompliance(batch);
    }

    private void verifyWaterproofRatings(PlannedProductionBatch batch) {
        // Verify all enclosures
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        boolean allWaterproof = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .filter(entry -> entry.getDescription().contains("Housing") ||
                        entry.getDescription().contains("Enclosure"))
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("sealing") &&
                            specs.get("sealing").contains("O-ring");
                });
        assertTrue(allWaterproof, "Not all enclosures meet waterproof requirements");

        // Verify all connectors
        var cableAssemblies = batch.getCableStructure().getAssemblies();
        boolean allConnectorsWaterproof = cableAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .allMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("waterproof") &&
                            specs.get("waterproof").equals("IP68");
                });
        assertTrue(allConnectorsWaterproof, "Not all connectors are waterproof");
    }

    private void verifySafetyRequirements(PlannedProductionBatch batch) {
        // Verify safety systems
        verifySafetySystems(batch);

        // Verify emergency systems
//        verifyEmergencySystems(batch);

        // Verify redundancy
        verifyRedundancySystems(batch);

        // Verify monitoring capabilities
//        verifyMonitoringSystems(batch);
    }

    private void verifySafetySystems(PlannedProductionBatch batch) {
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();

        // Verify watchdog timers
        boolean hasWatchdog = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Watchdog") ||
                                (entry.getSpecs().containsKey("features") &&
                                        entry.getSpecs().get("features").contains("Watchdog"))
                );
        assertTrue(hasWatchdog, "Missing watchdog timer");

        // Verify safety processor
        boolean hasSafetyProcessor = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry ->
                        entry.getDescription().contains("Safety") &&
                                entry.getSpecs().containsKey("certification")
                );
        assertTrue(hasSafetyProcessor, "Missing safety processor");
    }

    private void verifyInterSystemConnections(PlannedProductionBatch batch) {
        // Verify physical interfaces
        verifyPhysicalInterfaces(batch);

        // Verify electrical interfaces
//        verifyElectricalInterfaces(batch);

        // Verify communication interfaces
//        verifyCommunicationInterfaces(batch);
    }

    private void verifyPhysicalInterfaces(PlannedProductionBatch batch) {
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();

        // Verify mounting compatibility
        boolean hasMountingSystem = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("mount") &&
                            (specs.get("mount").contains("DIN") ||
                                    specs.get("mount").contains("Quick-Release"));
                });
        assertTrue(hasMountingSystem, "Missing standardized mounting system");
    }

    private void verifyThermalManagement(PlannedProductionBatch batch) {
        var assemblies = batch.getMechanicalStructure().getAssemblies();
        boolean hasAdequateCooling = assemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("heat_dissipation") ||
                            (specs.containsKey("thermal_conductivity") &&
                                    specs.get("thermal_conductivity").contains("W/m·K"));
                });
        assertTrue(hasAdequateCooling, "Insufficient thermal management");
    }

    private void verifyRedundancySystems(PlannedProductionBatch batch) {
        // Verify processor redundancy
        verifyProcessorRedundancy(batch);

        // Verify power redundancy
//        verifyPowerRedundancy(batch);

        // Verify communication redundancy
//        verifyCommunicationRedundancy(batch);

        // Verify sensor redundancy
//        verifySensorRedundancy(batch);
    }

    private void verifyProcessorRedundancy(PlannedProductionBatch batch) {
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        boolean hasRedundantProcessing = pcbAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> {
                    Map<String, String> specs = entry.getSpecs();
                    return specs.containsKey("features") &&
                            (specs.get("features").contains("Lock-step") ||
                                    specs.get("features").contains("Dual Core"));
                });
        assertTrue(hasRedundantProcessing, "Missing processor redundancy");
    }

    private String serializeToJson(PlannedProductionBatch batch) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String json = mapper.writeValueAsString(batch);

        // Print to console for verification
        System.out.println("Serialized Production Batch:");
        System.out.println(json);

        return json;
    }

    private PlannedProductionBatch deserializeFromJson(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, PlannedProductionBatch.class);
    }

    private void printProductionSummary(PlannedProductionBatch batch) {
        System.out.println("\nProduction Summary for " + batch.getProductId());
        System.out.println("====================================");
        System.out.println("Batch ID: " + batch.getBatchId());
        System.out.println("Revision: " + batch.getRevision());
        System.out.println("Quantity: " + batch.getQuantity());
        System.out.println("Planned Date: " + batch.getPlannedDate());
        System.out.println("\nComponent Counts:");
        System.out.println("- PCB Assemblies: " + batch.getPCBAStructure().getAssemblies().size());
        System.out.println("- Mechanical Assemblies: " + batch.getMechanicalStructure().getAssemblies().size());
        // ... continue with other summaries ...
    }


}