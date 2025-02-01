package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MissionControlComponentsTest {

    private PCBABOM missionControlPcba;
    private MechanicalBOM missionControlMechanical;

    @BeforeEach
    void setUp() {
        missionControlPcba = MissionControlComponents.PCBAComponents.createMainControlBoard();
        missionControlMechanical = MissionControlComponents.MechanicalComponents.createHousing();
    }

    @Test
    void shouldCreateMainControlBoard() {
        assertNotNull(missionControlPcba, "Mission control PCBA should not be null");
        List<BOMEntry> components = missionControlPcba.getBomEntries();
        assertFalse(components.isEmpty(), "PCBA should have components");

        verifyMainProcessor(components);
        verifySafetyProcessor(components);
        verifyRealTimeProcessor(components);
        verifyMemorySystems(components);
        verifyWatchdog(components);
        verifyPowerManagement(components);
    }

    @Test
    void shouldCreateControlHousing() {
        assertNotNull(missionControlMechanical, "Mission control housing should not be null");
        List<BOMEntry> components = missionControlMechanical.getBomEntries();
        assertFalse(components.isEmpty(), "Mechanical BOM should have components");

        verifyHousingSpecs(components);
        verifyThermalManagement(components);
        verifyMountingSystem(components);
        verifyConnectors(components);
    }

    @Test
    void shouldMeetProcessingRequirements() {
        List<BOMEntry> components = missionControlPcba.getBomEntries();
        verifyProcessingPower(components);
        verifyRealTimeCapabilities(components);
        verifySafetyFeatures(components);
        verifyRedundancy(components);
    }

    @Test
    void shouldMeetMemoryRequirements() {
        List<BOMEntry> components = missionControlPcba.getBomEntries();
        verifySystemMemory(components);
        verifyNonVolatileStorage(components);
        verifyMemoryProtection(components);
    }

    @Test
    void shouldMeetSafetyRequirements() {
        List<BOMEntry> components = missionControlPcba.getBomEntries();
        verifyWatchdogSystems(components);
        verifyVoltageMonitoring(components);
        verifyTemperatureMonitoring(components);
        verifyEmergencyShutdown(components);
    }

    @Test
    void shouldMeetEnvironmentalRequirements() {
        verifyPressureRating(missionControlMechanical.getBomEntries());
        verifyWaterproofing(missionControlMechanical.getBomEntries());
        verifyThermalDesign(missionControlMechanical.getBomEntries());
        verifyCorrosionResistance(missionControlMechanical.getBomEntries());
    }

    private void verifyMainProcessor(List<BOMEntry> components) {
        BOMEntry processor = findComponentByMpn(components, "i.MX8QuadXPlus");
        assertNotNull(processor, "Main processor should be present");

        Map<String, String> specs = processor.getSpecs();
        assertTrue(specs.getOrDefault("core", "").contains("Cortex-A35"),
                "Should have correct processor core");
        assertTrue(specs.getOrDefault("features", "").contains("ECC Memory"),
                "Should have ECC memory support");
        assertTrue(specs.getOrDefault("features", "").contains("Lockstep"),
                "Should have lockstep capability");
        assertTrue(specs.getOrDefault("security", "").contains("TrustZone"),
                "Should have security features");
    }

    private void verifySafetyProcessor(List<BOMEntry> components) {
        BOMEntry safetyProcessor = findComponentByMpn(components, "TMS570LC4357BZWTQQ1");
        assertNotNull(safetyProcessor, "Safety processor should be present");

        Map<String, String> specs = safetyProcessor.getSpecs();
        assertTrue(specs.getOrDefault("core", "").contains("Dual"),
                "Should have dual core");
        assertTrue(specs.getOrDefault("features", "").contains("Lock-step"),
                "Should have lock-step capability");
        assertEquals("SIL 3", specs.get("certification"),
                "Should have correct safety certification");
    }

    private void verifyRealTimeProcessor(List<BOMEntry> components) {
        BOMEntry rtProcessor = findComponentByMpn(components, "MPC5744P");
        assertNotNull(rtProcessor, "Real-time processor should be present");

        Map<String, String> specs = rtProcessor.getSpecs();
        assertTrue(specs.getOrDefault("features", "").contains("Real-Time OS"),
                "Should support RTOS");
        assertTrue(specs.getOrDefault("features", "").contains("AUTOSAR"),
                "Should have AUTOSAR support");
        assertTrue(specs.getOrDefault("quality", "").contains("Automotive"),
                "Should be automotive grade");
    }

    private void verifyMemorySystems(List<BOMEntry> components) {
        // Verify system RAM
        BOMEntry systemRam = findComponentByMpn(components, "MT41K512M16HA-125");
        assertNotNull(systemRam, "System RAM should be present");
        assertTrue(systemRam.getSpecs().getOrDefault("features", "").contains("ECC"),
                "RAM should have ECC");

        // Verify storage
        BOMEntry storage = findComponentByMpn(components, "SSDPE21K375GA01");
        assertNotNull(storage, "Storage should be present");
        assertTrue(storage.getSpecs().getOrDefault("features", "")
                        .contains("Power Loss Protection"),
                "Storage should have power loss protection");
    }

    private void verifyWatchdog(List<BOMEntry> components) {
        BOMEntry watchdog = findComponentByMpn(components, "MAX6369KA+T");
        assertNotNull(watchdog, "Watchdog timer should be present");

        Map<String, String> specs = watchdog.getSpecs();
        assertTrue(specs.containsKey("timeout"), "Should specify timeout period");
        assertTrue(specs.getOrDefault("window", "").contains("Windowed"),
                "Should have windowed operation");
        assertTrue(specs.getOrDefault("reset", "").contains("Push-Pull"),
                "Should have push-pull reset");
    }

    private void verifyPowerManagement(List<BOMEntry> components) {
        boolean hasPowerManagement = components.stream()
                .anyMatch(e -> e.getDescription().contains("Power Management") &&
                        e.getSpecs().containsKey("outputs") &&
                        e.getSpecs().containsKey("protection"));
        assertTrue(hasPowerManagement, "Should have power management system");
    }

    private void verifyHousingSpecs(List<BOMEntry> components) {
        BOMEntry housing = findComponentByMpn(components, "MCS-800-HSG");
        assertNotNull(housing, "Housing should be present");

        Map<String, String> specs = housing.getSpecs();
        assertEquals("Grade 5 Titanium", specs.get("material"),
                "Should use correct material");
        assertTrue(specs.get("pressure_rating").contains("300m"),
                "Should meet depth rating");
        assertTrue(specs.get("sealing").contains("Double O-ring"),
                "Should have proper sealing");
        assertTrue(specs.containsKey("heat_dissipation"),
                "Should have thermal management");
    }

    private void verifyThermalManagement(List<BOMEntry> components) {
        BOMEntry thermalPlate = findComponentByMpn(components, "MCS-800-THP");
        assertNotNull(thermalPlate, "Thermal management plate should be present");

        Map<String, String> specs = thermalPlate.getSpecs();
        assertTrue(specs.containsKey("conductivity"),
                "Should specify thermal conductivity");
        assertTrue(specs.containsKey("interface"),
                "Should specify thermal interface");
    }

    private void verifyMountingSystem(List<BOMEntry> components) {
        boolean hasMount = components.stream()
                .anyMatch(e -> e.getDescription().contains("Mounting Kit") &&
                        e.getSpecs().get("type").equals("DIN Rail Mount") &&
                        e.getSpecs().get("standard").equals("EN 60715"));
        assertTrue(hasMount, "Should have standardized mounting system");
    }

    private void verifyConnectors(List<BOMEntry> components) {
        boolean hasMainConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Main System Connector") &&
                        e.getSpecs().containsKey("contacts") &&
                        e.getSpecs().get("sealing").contains("IP68"));
        assertTrue(hasMainConnector, "Should have main system connector");

        boolean hasNetworkConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Network Connector") &&
                        e.getSpecs().containsKey("speed") &&
                        e.getSpecs().get("sealing").contains("IP68"));
        assertTrue(hasNetworkConnector, "Should have network connector");
    }

    private void verifyProcessingPower(List<BOMEntry> components) {
        boolean hasHighPerformance = components.stream()
                .anyMatch(e -> {
                    Map<String, String> specs = e.getSpecs();
                    String speed = specs.getOrDefault("speed", "0")
                            .replaceAll("[^0-9.]", "");
                    try {
                        return Double.parseDouble(speed) >= 1000; // At least 1GHz
                    } catch (NumberFormatException ex) {
                        return false;
                    }
                });
        assertTrue(hasHighPerformance, "Should have high-performance processing");
    }

    private void verifyRealTimeCapabilities(List<BOMEntry> components) {
        boolean hasRealTime = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("features", "")
                        .contains("Real-Time OS"));
        assertTrue(hasRealTime, "Should have real-time processing capability");
    }

    private void verifySafetyFeatures(List<BOMEntry> components) {
        boolean hasSafetyCert = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("certification", "")
                        .contains("SIL"));
        assertTrue(hasSafetyCert, "Should have safety certification");
    }

    private void verifyRedundancy(List<BOMEntry> components) {
        long redundantProcessors = components.stream()
                .filter(e -> {
                    String features = e.getSpecs().getOrDefault("features", "");
                    return features.contains("Lockstep") || features.contains("Lock-step");
                })
                .count();
        assertTrue(redundantProcessors >= 2, "Should have redundant processors");
    }

    private void verifySystemMemory(List<BOMEntry> components) {
        boolean hasEccMemory = components.stream()
                .filter(e -> e.getDescription().contains("Memory"))
                .anyMatch(e -> e.getSpecs().getOrDefault("features", "")
                        .contains("ECC"));
        assertTrue(hasEccMemory, "Should have ECC memory");
    }

    private void verifyNonVolatileStorage(List<BOMEntry> components) {
        boolean hasStorage = components.stream()
                .anyMatch(e -> e.getDescription().contains("Storage") &&
                        e.getSpecs().containsKey("capacity"));
        assertTrue(hasStorage, "Should have non-volatile storage");
    }

    private void verifyMemoryProtection(List<BOMEntry> components) {
        boolean hasProtection = components.stream()
                .filter(e -> e.getDescription().contains("Memory"))
                .anyMatch(e -> e.getSpecs().getOrDefault("features", "")
                        .contains("ECC"));
        assertTrue(hasProtection, "Should have memory protection");
    }

    private void verifyWatchdogSystems(List<BOMEntry> components) {
        boolean hasWatchdog = components.stream()
                .anyMatch(e -> e.getDescription().contains("Watchdog"));
        assertTrue(hasWatchdog, "Should have watchdog system");
    }

    private void verifyVoltageMonitoring(List<BOMEntry> components) {
        boolean hasVoltageMonitor = components.stream()
                .anyMatch(e -> e.getDescription().contains("Voltage") &&
                        e.getSpecs().containsKey("accuracy"));
        assertTrue(hasVoltageMonitor, "Should have voltage monitoring");
    }

    private void verifyTemperatureMonitoring(List<BOMEntry> components) {
        boolean hasTempMonitor = components.stream()
                .anyMatch(e -> e.getDescription().contains("Temperature") &&
                        e.getSpecs().containsKey("accuracy"));
        assertTrue(hasTempMonitor, "Should have temperature monitoring");
    }

    private void verifyEmergencyShutdown(List<BOMEntry> components) {
        boolean hasEmergencyShutdown = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("features", "")
                        .contains("Emergency Shutdown"));
        assertTrue(hasEmergencyShutdown, "Should have emergency shutdown capability");
    }

    private void verifyPressureRating(List<BOMEntry> components) {
        boolean hasCorrectPressureRating = components.stream()
                .filter(e -> e.getSpecs().containsKey("pressure_rating"))
                .allMatch(e -> e.getSpecs().get("pressure_rating").contains("300m"));
        assertTrue(hasCorrectPressureRating,
                "All components should meet pressure rating requirements");
    }

    private void verifyWaterproofing(List<BOMEntry> components) {
        boolean hasWaterproofing = components.stream()
                .anyMatch(e -> {
                    Map<String, String> specs = e.getSpecs();
                    return specs.containsKey("sealing") &&
                            (specs.get("sealing").contains("IP68") ||
                                    specs.get("sealing").contains("Double O-ring"));
                });
        assertTrue(hasWaterproofing, "Should have waterproof protection");
    }

    private void verifyThermalDesign(List<BOMEntry> components) {
        boolean hasThermalManagement = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("heat_dissipation") ||
                        e.getSpecs().containsKey("thermal_conductivity"));
        assertTrue(hasThermalManagement, "Should have thermal management system");
    }

    private void verifyCorrosionResistance(List<BOMEntry> components) {
        boolean hasCorrosionResistance = components.stream()
                .filter(e -> e.getSpecs().containsKey("material"))
                .allMatch(e -> {
                    String material = e.getSpecs().get("material");
                    return material.contains("Titanium") ||
                            material.contains("316L");
                });
        assertTrue(hasCorrosionResistance, "Should use corrosion-resistant materials");
    }

    private BOMEntry findComponentByMpn(List<BOMEntry> components, String mpn) {
        return components.stream()
                .filter(e -> e.getMpn().equals(mpn))
                .findFirst()
                .orElse(null);
    }
}