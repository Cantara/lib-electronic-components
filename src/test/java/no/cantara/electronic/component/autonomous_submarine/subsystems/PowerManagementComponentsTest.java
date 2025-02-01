package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PowerManagementComponentsTest {

    private PCBABOM powerPcba;
    private MechanicalBOM powerMechanical;

    @BeforeEach
    void setUp() {
        powerPcba = PowerManagementComponents.PCBAComponents.createPowerControlBoard();
        powerMechanical = PowerManagementComponents.MechanicalComponents.createHousing();
    }

    @Test
    void shouldCreatePowerControlBoard() {
        assertNotNull(powerPcba, "Power control PCBA should not be null");
        List<BOMEntry> components = powerPcba.getBomEntries();
        assertFalse(components.isEmpty(), "PCBA should have components");

        verifyBatteryManagement(components);
        verifyPowerDistribution(components);
        verifyProtectionCircuits(components);
        verifyMonitoringSystems(components);
    }

    @Test
    void shouldCreatePowerHousing() {
        assertNotNull(powerMechanical, "Power housing should not be null");
        List<BOMEntry> components = powerMechanical.getBomEntries();
        assertFalse(components.isEmpty(), "Mechanical BOM should have components");

        verifyHousingSpecs(components);
        verifyThermalManagement(components);
        verifyMountingSystem(components);
        verifyConnectors(components);
    }

    @Test
    void shouldMeetPowerRequirements() {
        List<BOMEntry> components = powerPcba.getBomEntries();
        verifyVoltageRatings(components);
        verifyCurrentCapabilities(components);
        verifyEfficiencyRequirements(components);
        verifyLoadDistribution(components);
    }

    @Test
    void shouldMeetEnvironmentalRequirements() {
        verifyPressureRating(powerMechanical.getBomEntries());
        verifyWaterproofing(powerMechanical.getBomEntries());
        verifyTemperatureRange(powerPcba.getBomEntries());
        verifyCorrosionResistance(powerMechanical.getBomEntries());
    }

    @Test
    void shouldHaveRequiredSafetySystems() {
        List<BOMEntry> components = powerPcba.getBomEntries();
        verifyOvervoltageProtection(components);
        verifyUndervoltageProtection(components);
        verifyOvercurrentProtection(components);
        verifyShortCircuitProtection(components);
        verifyThermalProtection(components);
    }

    private void verifyBatteryManagement(List<BOMEntry> components) {
        BOMEntry bms = findComponentByMpn(components, "BQ76952PFBR");
        assertNotNull(bms, "Battery management IC should be present");

        Map<String, String> specs = bms.getSpecs();
        assertTrue(specs.containsKey("cells"), "Should specify cell configuration");
        assertTrue(specs.getOrDefault("features", "").contains("Cell Balancing"),
                "Should have cell balancing");
        assertTrue(specs.getOrDefault("protection", "").matches(".*(OVP|UVP|OCP|SCP).*"),
                "Should have protection features");
    }

    private void verifyPowerDistribution(List<BOMEntry> components) {
        // Verify main power supply controller
        BOMEntry mainController = findComponentByMpn(components, "LTC4015");
        assertNotNull(mainController, "Main power controller should be present");
        assertTrue(mainController.getSpecs().containsKey("current"),
                "Should specify current rating");
        assertTrue(mainController.getSpecs().containsKey("efficiency"),
                "Should specify efficiency");

        // Verify DC-DC converter
        BOMEntry dcdc = findComponentByMpn(components, "LTC3891");
        assertNotNull(dcdc, "DC-DC converter should be present");
        assertTrue(dcdc.getSpecs().containsKey("voltage"),
                "Should specify voltage rating");
        assertTrue(dcdc.getSpecs().containsKey("current"),
                "Should specify current rating");
    }

    private void verifyProtectionCircuits(List<BOMEntry> components) {
        BOMEntry powerSwitch = findComponentByMpn(components, "TPS1H100-Q1");
        assertNotNull(powerSwitch, "Power distribution switch should be present");

        Map<String, String> specs = powerSwitch.getSpecs();
        assertTrue(specs.containsKey("voltage"), "Should specify voltage rating");
        assertTrue(specs.containsKey("current"), "Should specify current rating");
        assertTrue(specs.getOrDefault("protection", "").contains("OCP"),
                "Should have overcurrent protection");
        assertTrue(specs.getOrDefault("features", "").contains("Current Sensing"),
                "Should have current sensing");
    }

    private void verifyMonitoringSystems(List<BOMEntry> components) {
        // Verify current monitoring
        boolean hasCurrentMonitor = components.stream()
                .anyMatch(e -> e.getDescription().contains("Current Monitor") &&
                        e.getSpecs().containsKey("accuracy"));
        assertTrue(hasCurrentMonitor, "Should have current monitoring");

        // Verify temperature monitoring
        boolean hasTempMonitor = components.stream()
                .anyMatch(e -> e.getDescription().contains("Temperature") &&
                        e.getSpecs().containsKey("accuracy"));
        assertTrue(hasTempMonitor, "Should have temperature monitoring");
    }

    private void verifyHousingSpecs(List<BOMEntry> components) {
        BOMEntry housing = findComponentByMpn(components, "PWR-500-HSG");
        assertNotNull(housing, "Power housing should be present");

        Map<String, String> specs = housing.getSpecs();
        assertEquals("Grade 5 Titanium", specs.get("material"),
                "Should use correct material");
        assertTrue(specs.get("pressure_rating").contains("300m"),
                "Should meet depth rating");
        assertTrue(specs.get("sealing").contains("Double O-ring"),
                "Should have proper sealing");
    }

    private void verifyThermalManagement(List<BOMEntry> components) {
        boolean hasCooling = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("cooling") &&
                        e.getSpecs().get("cooling").contains("Liquid-Cooled"));
        assertTrue(hasCooling, "Should have cooling system");
    }

    private void verifyMountingSystem(List<BOMEntry> components) {
        boolean hasMount = components.stream()
                .anyMatch(e -> e.getDescription().contains("Mounting Kit") &&
                        e.getSpecs().get("type").equals("DIN Rail Mount") &&
                        e.getSpecs().get("standard").equals("EN 60715"));
        assertTrue(hasMount, "Should have standardized mounting system");
    }

    private void verifyConnectors(List<BOMEntry> components) {
        boolean hasPowerConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Power Connector") &&
                        e.getSpecs().containsKey("current_rating") &&
                        e.getSpecs().containsKey("voltage_rating"));
        assertTrue(hasPowerConnector, "Should have power connectors");

        boolean hasSignalConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Signal Connector") &&
                        e.getSpecs().containsKey("sealing"));
        assertTrue(hasSignalConnector, "Should have signal connectors");
    }

    private void verifyVoltageRatings(List<BOMEntry> components) {
        boolean hasHighVoltage = components.stream()
                .anyMatch(e -> {
                    Map<String, String> specs = e.getSpecs();
                    return specs.containsKey("voltage") &&
                            specs.get("voltage").contains("100V");
                });
        assertTrue(hasHighVoltage, "Should have high voltage components");
    }

    private void verifyCurrentCapabilities(List<BOMEntry> components) {
        boolean hasHighCurrent = components.stream()
                .anyMatch(e -> {
                    Map<String, String> specs = e.getSpecs();
                    return specs.containsKey("current") &&
                            specs.get("current").contains("30A");
                });
        assertTrue(hasHighCurrent, "Should have high current capability");
    }

    private void verifyEfficiencyRequirements(List<BOMEntry> components) {
        boolean hasHighEfficiency = components.stream()
                .anyMatch(e -> {
                    Map<String, String> specs = e.getSpecs();
                    return specs.containsKey("efficiency") &&
                            specs.get("efficiency").contains("95%");
                });
        assertTrue(hasHighEfficiency, "Should have high efficiency components");
    }

    private void verifyLoadDistribution(List<BOMEntry> components) {
        boolean hasLoadDistribution = components.stream()
                .anyMatch(e -> e.getDescription().contains("Distribution") &&
                        e.getSpecs().containsKey("features") &&
                        e.getSpecs().get("features").contains("Current Sensing"));
        assertTrue(hasLoadDistribution, "Should have load distribution capability");
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

    private void verifyTemperatureRange(List<BOMEntry> components) {
        boolean hasTemperatureSpec = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("temp_range"));
        assertTrue(hasTemperatureSpec, "Should specify temperature range");
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

    private void verifyOvervoltageProtection(List<BOMEntry> components) {
        boolean hasOVP = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("protection", "")
                        .contains("OVP"));
        assertTrue(hasOVP, "Should have overvoltage protection");
    }

    private void verifyUndervoltageProtection(List<BOMEntry> components) {
        boolean hasUVP = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("protection", "")
                        .contains("UVP"));
        assertTrue(hasUVP, "Should have undervoltage protection");
    }

    private void verifyOvercurrentProtection(List<BOMEntry> components) {
        boolean hasOCP = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("protection", "")
                        .contains("OCP"));
        assertTrue(hasOCP, "Should have overcurrent protection");
    }

    private void verifyShortCircuitProtection(List<BOMEntry> components) {
        boolean hasSCP = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("protection", "")
                        .contains("SCP"));
        assertTrue(hasSCP, "Should have short-circuit protection");
    }

    private void verifyThermalProtection(List<BOMEntry> components) {
        boolean hasOTP = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("protection", "")
                        .contains("OTP"));
        assertTrue(hasOTP, "Should have thermal protection");
    }

    private BOMEntry findComponentByMpn(List<BOMEntry> components, String mpn) {
        return components.stream()
                .filter(e -> e.getMpn().equals(mpn))
                .findFirst()
                .orElse(null);
    }
}