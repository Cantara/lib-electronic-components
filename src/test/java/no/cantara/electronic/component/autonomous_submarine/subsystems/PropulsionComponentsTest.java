package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PropulsionComponentsTest {

    private PCBABOM motorControlPcba;
    private MechanicalBOM propulsionMechanical;

    @BeforeEach
    void setUp() {
        motorControlPcba = PropulsionComponents.PCBAComponents.createMotorControlBoard();
        propulsionMechanical = PropulsionComponents.MechanicalComponents.createPropulsionUnit();
    }

    @Test
    void shouldCreateMotorControlBoard() {
        assertNotNull(motorControlPcba, "Motor control PCBA should not be null");
        List<BOMEntry> components = motorControlPcba.getBomEntries();
        assertFalse(components.isEmpty(), "PCBA should have components");

        verifyMotorController(components);
        verifyGateDrivers(components);
        verifyPowerStage(components);
        verifyFeedbackSensors(components);
        verifyProtectionCircuits(components);
    }

    @Test
    void shouldCreatePropulsionUnit() {
        assertNotNull(propulsionMechanical, "Propulsion unit should not be null");
        List<BOMEntry> components = propulsionMechanical.getBomEntries();
        assertFalse(components.isEmpty(), "Mechanical BOM should have components");

        verifyMotorHousing(components);
        verifyPropellerAssembly(components);
        verifySealingSystem(components);
        verifyBearings(components);
        verifyMountingSystem(components);
    }

    @Test
    void shouldMeetPowerRequirements() {
        List<BOMEntry> components = motorControlPcba.getBomEntries();

        verifyVoltageRatings(components);
        verifyCurrentCapabilities(components);
        verifyPowerDissipation(components);
    }

    @Test
    void shouldMeetEnvironmentalRequirements() {
        verifyPressureRating(propulsionMechanical.getBomEntries());
        verifyWaterproofing(propulsionMechanical.getBomEntries());
        verifyTemperatureRange(motorControlPcba.getBomEntries());
        verifyCorrosionResistance(propulsionMechanical.getBomEntries());
    }

    private void verifyMotorController(List<BOMEntry> components) {
        BOMEntry controller = findComponentByMpn(components, "STM32G474VET6");
        assertNotNull(controller, "Motor controller should be present");

        Map<String, String> specs = controller.getSpecs();
        assertTrue(specs.getOrDefault("features", "").contains("HRTIM"),
                "Should have high-resolution timer");
        assertTrue(specs.getOrDefault("quality", "").contains("Automotive"),
                "Should be automotive grade");
        assertTrue(specs.containsKey("speed"), "Should specify speed");
    }

    private void verifyGateDrivers(List<BOMEntry> components) {
        BOMEntry driver = findComponentByMpn(components, "DRV8353RS");
        assertNotNull(driver, "Gate driver should be present");

        Map<String, String> specs = driver.getSpecs();
        assertTrue(specs.containsKey("voltage"), "Should specify voltage rating");
        assertTrue(specs.containsKey("current"), "Should specify current rating");
        assertTrue(specs.getOrDefault("protection", "").contains("OCP"),
                "Should have overcurrent protection");
        assertTrue(specs.getOrDefault("protection", "").contains("OTP"),
                "Should have thermal protection");
    }

    private void verifyPowerStage(List<BOMEntry> components) {
        BOMEntry mosfet = findComponentByMpn(components, "IPT015N10N5");
        assertNotNull(mosfet, "Power MOSFETs should be present");

        Map<String, String> specs = mosfet.getSpecs();
        assertTrue(specs.containsKey("vds"), "Should specify voltage rating");
        assertTrue(specs.containsKey("id"), "Should specify current rating");
        assertTrue(specs.containsKey("rds_on"), "Should specify on-resistance");
    }

    private void verifyFeedbackSensors(List<BOMEntry> components) {
        // Verify position sensor
        BOMEntry encoder = findComponentByMpn(components, "AS5047P");
        assertNotNull(encoder, "Position sensor should be present");
        assertTrue(encoder.getSpecs().containsKey("resolution"),
                "Should specify resolution");

        // Verify current sensor
        BOMEntry currentSensor = findComponentByMpn(components, "INA240A3");
        assertNotNull(currentSensor, "Current sensor should be present");
        assertTrue(currentSensor.getSpecs().containsKey("bandwidth"),
                "Should specify bandwidth");
    }

    private void verifyProtectionCircuits(List<BOMEntry> components) {
        boolean hasProtection = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("protection", "")
                        .matches(".*(OCP|OVP|UVP|SCP).*"));
        assertTrue(hasProtection, "Should have protection circuits");
    }

    private void verifyMotorHousing(List<BOMEntry> components) {
        BOMEntry housing = findComponentByMpn(components, "PROP-400-HSG");
        assertNotNull(housing, "Motor housing should be present");

        Map<String, String> specs = housing.getSpecs();
        assertEquals("Grade 5 Titanium", specs.get("material"),
                "Should use correct material");
        assertTrue(specs.get("pressure_rating").contains("300m"),
                "Should meet depth rating");
        assertTrue(specs.get("sealing").contains("Dual Shaft Seal"),
                "Should have shaft sealing");
    }

    private void verifyPropellerAssembly(List<BOMEntry> components) {
        BOMEntry propeller = findComponentByMpn(components, "PROP-400-PRO");
        assertNotNull(propeller, "Propeller should be present");

        Map<String, String> specs = propeller.getSpecs();
        assertTrue(specs.containsKey("material"), "Should specify material");
        assertTrue(specs.containsKey("diameter"), "Should specify diameter");
        assertTrue(specs.containsKey("pitch"), "Should specify pitch");
        assertTrue(specs.containsKey("blades"), "Should specify number of blades");
    }

    private void verifySealingSystem(List<BOMEntry> components) {
        BOMEntry seal = findComponentByMpn(components, "TC-25-40-10-CV");
        assertNotNull(seal, "Shaft seal should be present");

        Map<String, String> specs = seal.getSpecs();
        assertTrue(specs.getOrDefault("type", "").contains("Double Lip"),
                "Should have double lip seal");
        assertTrue(specs.containsKey("pressure"), "Should specify pressure rating");
        assertTrue(specs.containsKey("speed"), "Should specify speed rating");
    }

    private void verifyBearings(List<BOMEntry> components) {
        boolean hasCeramicBearings = components.stream()
                .anyMatch(e -> e.getDescription().contains("Bearing") &&
                        e.getSpecs().getOrDefault("type", "")
                                .contains("Ceramic Hybrid"));
        assertTrue(hasCeramicBearings, "Should have ceramic hybrid bearings");
    }

    private void verifyMountingSystem(List<BOMEntry> components) {
        boolean hasMount = components.stream()
                .anyMatch(e -> e.getDescription().contains("Mounting Kit") &&
                        e.getSpecs().get("type").equals("DIN Rail Mount") &&
                        e.getSpecs().get("standard").equals("EN 60715"));
        assertTrue(hasMount, "Should have standardized mounting system");
    }

    private void verifyVoltageRatings(List<BOMEntry> components) {
        boolean hasHighVoltage = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("voltage"));
        assertTrue(hasHighVoltage, "Should have voltage ratings");
    }

    private void verifyCurrentCapabilities(List<BOMEntry> components) {
        boolean hasCurrentRating = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("current"));
        assertTrue(hasCurrentRating, "Should have current ratings");
    }

    private void verifyPowerDissipation(List<BOMEntry> components) {
        boolean hasThermalManagement = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("thermal") ||
                        e.getSpecs().containsKey("cooling"));
        assertTrue(hasThermalManagement, "Should have thermal management");
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
                                    specs.get("sealing").contains("Double O-ring") ||
                                    specs.get("sealing").contains("Dual Shaft Seal"));
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
                            material.contains("316L") ||
                            material.contains("Ceramic") ||
                            material.contains("Composite");
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