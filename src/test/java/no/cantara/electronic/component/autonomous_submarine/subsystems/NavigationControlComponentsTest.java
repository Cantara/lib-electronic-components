package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NavigationControlComponentsTest {

    private PCBABOM navigationPcba;
    private MechanicalBOM navigationMechanical;

    @BeforeEach
    void setUp() {
        navigationPcba = NavigationControlComponents.PCBAComponents.createMainControlBoard();
        navigationMechanical = NavigationControlComponents.MechanicalComponents.createHousing();
    }

    @Test
    void shouldCreateMainControlBoard() {
        assertNotNull(navigationPcba, "PCBA should not be null");
        List<BOMEntry> components = navigationPcba.getBomEntries();
        assertFalse(components.isEmpty(), "PCBA should have components");

        verifyMainProcessor(components);
        verifyImu(components);
        verifyMemoryComponents(components);
        verifyPowerRegulation(components);
        verifyInterfaces(components);
    }

    @Test
    void shouldCreateNavigationHousing() {
        assertNotNull(navigationMechanical, "Mechanical BOM should not be null");
        List<BOMEntry> components = navigationMechanical.getBomEntries();
        assertFalse(components.isEmpty(), "Mechanical BOM should have components");

        verifyHousingSpecs(components);
        verifyMountingSystem(components);
        verifySealingSystem(components);
        verifyConnectors(components);
    }

    @Test
    void shouldMeetEnvironmentalRequirements() {
        verifyPressureRating(navigationMechanical.getBomEntries());
        verifyWaterproofing(navigationMechanical.getBomEntries());
        verifyTemperatureRange(navigationPcba.getBomEntries());
    }

    @Test
    void shouldHaveCorrectPowerRequirements() {
        List<BOMEntry> components = navigationPcba.getBomEntries();
        verifyPowerSpecs(components);
    }

    private void verifyMainProcessor(List<BOMEntry> components) {
        BOMEntry processor = findComponentByMpn(components, "STM32H755ZIT6");
        assertNotNull(processor, "Main processor should be present");

        Map<String, String> specs = processor.getSpecs();
        assertEquals("Dual Cortex-M7/M4", specs.get("core"),
                "Should have correct processor core");
        assertTrue(specs.containsKey("flash"), "Should specify flash memory");
        assertTrue(specs.containsKey("ram"), "Should specify RAM");
        assertTrue(specs.containsKey("speed"), "Should specify clock speed");
        assertTrue(specs.get("quality").contains("Automotive"),
                "Should be automotive grade");
    }

    private void verifyImu(List<BOMEntry> components) {
        BOMEntry imu = findComponentByMpn(components, "BMX055");
        assertNotNull(imu, "IMU should be present");

        Map<String, String> specs = imu.getSpecs();
        assertTrue(specs.get("sensors").contains("Accelerometer"),
                "Should have accelerometer");
        assertTrue(specs.get("sensors").contains("Gyroscope"),
                "Should have gyroscope");
        assertTrue(specs.get("sensors").contains("Magnetometer"),
                "Should have magnetometer");
    }

    private void verifyMemoryComponents(List<BOMEntry> components) {
        BOMEntry fram = findComponentByMpn(components, "FM24V10-G");
        assertNotNull(fram, "FRAM should be present");

        Map<String, String> specs = fram.getSpecs();
        assertTrue(specs.containsKey("size"), "Should specify memory size");
        assertEquals("SPI", specs.get("interface"), "Should use SPI interface");
    }

    private void verifyPowerRegulation(List<BOMEntry> components) {
        boolean hasRegulator = components.stream()
                .anyMatch(e -> e.getDescription().contains("Buck Converter") &&
                        e.getSpecs().containsKey("efficiency") &&
                        e.getSpecs().containsKey("current"));
        assertTrue(hasRegulator, "Power regulator should be present");
    }

    private void verifyInterfaces(List<BOMEntry> components) {
        boolean hasRs485 = components.stream()
                .anyMatch(e -> e.getMpn().equals("MAX3485EESA+"));
        assertTrue(hasRs485, "Should have RS-485 interface");
    }

    private void verifyHousingSpecs(List<BOMEntry> components) {
        BOMEntry housing = findComponentByMpn(components, "NAV-300-HSG");
        assertNotNull(housing, "Housing should be present");

        Map<String, String> specs = housing.getSpecs();
        assertEquals("316L Stainless Steel", specs.get("material"),
                "Should use correct material");
        assertTrue(specs.get("pressure_rating").contains("300m"),
                "Should meet depth rating");
        assertTrue(specs.get("sealing").contains("Double O-ring"),
                "Should have proper sealing");
    }

    private void verifyMountingSystem(List<BOMEntry> components) {
        boolean hasMount = components.stream()
                .anyMatch(e -> e.getDescription().contains("Mounting Kit") &&
                        e.getSpecs().containsKey("load_rating") &&
                        e.getSpecs().get("type").equals("DIN Rail Mount"));
        assertTrue(hasMount, "Should have proper mounting system");
    }

    private void verifySealingSystem(List<BOMEntry> components) {
        boolean hasSeals = components.stream()
                .anyMatch(e -> e.getDescription().contains("O-Ring Set"));
        assertTrue(hasSeals, "Should have sealing system");
    }

    private void verifyConnectors(List<BOMEntry> components) {
        boolean hasConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Connector") &&
                        e.getSpecs().containsKey("rating") &&
                        e.getSpecs().containsKey("mating_cycles"));
        assertTrue(hasConnector, "Should have proper connectors");
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

    private void verifyPowerSpecs(List<BOMEntry> components) {
        boolean hasCorrectVoltages = components.stream()
                .filter(e -> e.getSpecs().containsKey("voltage"))
                .anyMatch(e -> {
                    String voltage = e.getSpecs().get("voltage");
                    return voltage.contains("3.3V") || voltage.contains("5V");
                });
        assertTrue(hasCorrectVoltages, "Should have standard voltage requirements");
    }

    private BOMEntry findComponentByMpn(List<BOMEntry> components, String mpn) {
        return components.stream()
                .filter(e -> e.getMpn().equals(mpn))
                .findFirst()
                .orElse(null);
    }
}