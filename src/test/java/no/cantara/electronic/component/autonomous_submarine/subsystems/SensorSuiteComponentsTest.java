package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SensorSuiteComponentsTest {

    private PCBABOM sensorPcba;
    private MechanicalBOM sensorMechanical;

    @BeforeEach
    void setUp() {
        sensorPcba = SensorSuiteComponents.PCBAComponents.createSensorInterfaceBoard();
        sensorMechanical = SensorSuiteComponents.MechanicalComponents.createSensorPod();
    }

    @Test
    void shouldCreateSensorInterfaceBoard() {
        assertNotNull(sensorPcba, "Sensor interface PCBA should not be null");
        List<BOMEntry> components = sensorPcba.getBomEntries();
        assertFalse(components.isEmpty(), "PCBA should have components");

        verifyMainProcessor(components);
        verifySonarProcessing(components);
        verifyAnalogInterface(components);
        verifyCameraInterface(components);
        verifyPowerManagement(components);
    }

    @Test
    void shouldCreateSensorPod() {
        assertNotNull(sensorMechanical, "Sensor pod should not be null");
        List<BOMEntry> components = sensorMechanical.getBomEntries();
        assertFalse(components.isEmpty(), "Mechanical BOM should have components");

        verifyMainHousing(components);
        verifySonarArray(components);
        verifyOpticalPorts(components);
        verifySensorPorts(components);
        verifyMountingSystem(components);
        verifyConnectors(components);
    }

    @Test
    void shouldMeetSensorRequirements() {
        verifyWaterQualitySensors(sensorMechanical.getBomEntries());
        verifyImagingSystem(sensorMechanical.getBomEntries());
        verifyEnvironmentalSensors(sensorMechanical.getBomEntries());
    }

    @Test
    void shouldMeetDataAcquisitionRequirements() {
        List<BOMEntry> components = sensorPcba.getBomEntries();
        verifyAnalogAcquisition(components);
        verifyDigitalAcquisition(components);
        verifyDataStorage(components);
        verifyDataBandwidth(components);
    }

    @Test
    void shouldMeetEnvironmentalRequirements() {
        verifyPressureRating(sensorMechanical.getBomEntries());
        verifyWaterproofing(sensorMechanical.getBomEntries());
        verifyTemperatureRange(sensorPcba.getBomEntries());
        verifyCorrosionResistance(sensorMechanical.getBomEntries());
    }

    private void verifyMainProcessor(List<BOMEntry> components) {
        BOMEntry processor = findComponentByMpn(components, "STM32H723ZGT6");
        assertNotNull(processor, "Main processor should be present");

        Map<String, String> specs = processor.getSpecs();
        assertTrue(specs.getOrDefault("features", "").contains("FPU"),
                "Should have floating-point unit");
        assertTrue(specs.getOrDefault("features", "").contains("DSP"),
                "Should have DSP capabilities");
        assertTrue(specs.containsKey("interfaces"), "Should specify interfaces");
    }

    private void verifySonarProcessing(List<BOMEntry> components) {
        // Verify sonar processor
        BOMEntry sonarProcessor = findComponentByMpn(components, "TMS320C6748AGZK");
        assertNotNull(sonarProcessor, "Sonar DSP should be present");
        assertTrue(sonarProcessor.getSpecs().getOrDefault("features", "")
                .contains("FFT Hardware"), "Should have FFT capability");

        // Verify sonar frontend
        BOMEntry sonarFrontend = findComponentByMpn(components, "AD9269BBPZ-40");
        assertNotNull(sonarFrontend, "Sonar frontend should be present");
        assertTrue(sonarFrontend.getSpecs().containsKey("channels"),
                "Should specify channel count");
        assertTrue(sonarFrontend.getSpecs().containsKey("sample_rate"),
                "Should specify sample rate");
    }

    private void verifyAnalogInterface(List<BOMEntry> components) {
        BOMEntry adc = findComponentByMpn(components, "ADS1278IPAG");
        assertNotNull(adc, "Precision ADC should be present");

        Map<String, String> specs = adc.getSpecs();
        assertTrue(specs.containsKey("channels"), "Should specify channel count");
        assertTrue(specs.containsKey("resolution"), "Should specify resolution");
        assertTrue(specs.containsKey("sample_rate"), "Should specify sample rate");
        assertTrue(specs.getOrDefault("features", "").contains("Simultaneous"),
                "Should have simultaneous sampling");
    }

    private void verifyCameraInterface(List<BOMEntry> components) {
        BOMEntry imageProcessor = findComponentByMpn(components, "TW8844-LA1-CR");
        assertNotNull(imageProcessor, "Image processor should be present");

        Map<String, String> specs = imageProcessor.getSpecs();
        assertTrue(specs.containsKey("resolution"), "Should specify resolution");
        assertTrue(specs.containsKey("interface"), "Should specify interface type");
        assertTrue(specs.getOrDefault("features", "").contains("Image Enhancement"),
                "Should have image enhancement");
    }

    private void verifyPowerManagement(List<BOMEntry> components) {
        boolean hasPowerManagement = components.stream()
                .anyMatch(e -> e.getDescription().contains("Power Management") &&
                        e.getSpecs().containsKey("outputs") &&
                        e.getSpecs().containsKey("protection"));
        assertTrue(hasPowerManagement, "Should have power management system");
    }

    private void verifyMainHousing(List<BOMEntry> components) {
        BOMEntry housing = findComponentByMpn(components, "SENS-700-POD");
        assertNotNull(housing, "Sensor pod housing should be present");

        Map<String, String> specs = housing.getSpecs();
        assertEquals("Grade 5 Titanium", specs.get("material"),
                "Should use correct material");
        assertTrue(specs.get("pressure_rating").contains("300m"),
                "Should meet depth rating");
        assertTrue(specs.containsKey("viewport"), "Should have viewport");
        assertTrue(specs.containsKey("coating"), "Should have anti-fouling coating");
    }

    private void verifySonarArray(List<BOMEntry> components) {
        BOMEntry sonarArray = findComponentByMpn(components, "SENS-700-SON");
        assertNotNull(sonarArray, "Sonar array should be present");

        Map<String, String> specs = sonarArray.getSpecs();
        assertTrue(specs.containsKey("elements"), "Should specify element count");
        assertTrue(specs.containsKey("frequency"), "Should specify frequency");
        assertTrue(specs.containsKey("beam_width"), "Should specify beam width");
        assertTrue(specs.containsKey("range"), "Should specify range");
    }

    private void verifyOpticalPorts(List<BOMEntry> components) {
        // Verify viewport
        boolean hasViewport = components.stream()
                .anyMatch(e -> e.getSpecs().getOrDefault("material", "")
                        .contains("Sapphire"));
        assertTrue(hasViewport, "Should have sapphire viewport");

        // Verify LED array
        boolean hasIllumination = components.stream()
                .anyMatch(e -> e.getDescription().contains("LED") &&
                        e.getSpecs().containsKey("power") &&
                        e.getSpecs().containsKey("beam_angle"));
        assertTrue(hasIllumination, "Should have LED illumination");
    }

    private void verifySensorPorts(List<BOMEntry> components) {
        BOMEntry sensorPorts = findComponentByMpn(components, "SENS-700-PORT");
        assertNotNull(sensorPorts, "Sensor ports should be present");

        Map<String, String> specs = sensorPorts.getSpecs();
        assertTrue(specs.containsKey("ports"), "Should specify port count");
        assertTrue(specs.getOrDefault("type", "").contains("Wet-Mateable"),
                "Should be wet-mateable");
        assertTrue(specs.containsKey("sealing"), "Should specify sealing");
    }

    private void verifyMountingSystem(List<BOMEntry> components) {
        boolean hasMount = components.stream()
                .anyMatch(e -> e.getDescription().contains("Mounting Kit") &&
                        e.getSpecs().get("type").equals("DIN Rail Mount") &&
                        e.getSpecs().get("standard").equals("EN 60715"));
        assertTrue(hasMount, "Should have standardized mounting system");
    }

    private void verifyConnectors(List<BOMEntry> components) {
        // Verify main system connector
        boolean hasMainConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Main System Connector") &&
                        e.getSpecs().containsKey("contacts") &&
                        e.getSpecs().get("sealing").contains("IP68"));
        assertTrue(hasMainConnector, "Should have main system connector");

        // Verify sensor array connector
        boolean hasSensorConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Sensor Array Connector") &&
                        e.getSpecs().containsKey("contacts") &&
                        e.getSpecs().get("sealing").contains("IP68"));
        assertTrue(hasSensorConnector, "Should have sensor array connector");
    }

    private void verifyWaterQualitySensors(List<BOMEntry> components) {
        boolean hasWaterQualitySensors = components.stream()
                .anyMatch(e -> e.getDescription().contains("Water Quality") &&
                        e.getSpecs().getOrDefault("parameters", "")
                                .matches(".*(pH|DO|Conductivity).*"));
        assertTrue(hasWaterQualitySensors, "Should have water quality sensors");
    }

    private void verifyImagingSystem(List<BOMEntry> components) {
        // Verify camera
        boolean hasCamera = components.stream()
                .anyMatch(e -> e.getMpn().equals("UI-5261SE-C-HQ"));
        assertTrue(hasCamera, "Should have imaging camera");

        // Verify lighting
        boolean hasLighting = components.stream()
                .anyMatch(e -> e.getDescription().contains("LED Array"));
        assertTrue(hasLighting, "Should have lighting system");
    }

    private void verifyEnvironmentalSensors(List<BOMEntry> components) {
        // Check for required environmental sensors
        boolean hasDepthSensor = components.stream()
                .anyMatch(e -> e.getMpn().equals("MS5837-30BA"));
        assertTrue(hasDepthSensor, "Should have depth sensor");

        boolean hasTemperatureSensor = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("temp_range"));
        assertTrue(hasTemperatureSensor, "Should have temperature sensor");
    }

    private void verifyAnalogAcquisition(List<BOMEntry> components) {
        boolean hasAnalogAcquisition = components.stream()
                .anyMatch(e -> e.getDescription().contains("ADC") &&
                        e.getSpecs().containsKey("resolution") &&
                        e.getSpecs().containsKey("channels"));
        assertTrue(hasAnalogAcquisition, "Should have analog acquisition system");
    }

    private void verifyDigitalAcquisition(List<BOMEntry> components) {
        boolean hasDigitalInterfaces = components.stream()
                .anyMatch(e -> {
                    String interfaces = e.getSpecs().getOrDefault("interface", "");
                    return interfaces.contains("SPI") || interfaces.contains("I2C");
                });
        assertTrue(hasDigitalInterfaces, "Should have digital interfaces");
    }

    private void verifyDataStorage(List<BOMEntry> components) {
        boolean hasStorage = components.stream()
                .anyMatch(e -> e.getDescription().contains("Storage") &&
                        e.getSpecs().containsKey("capacity"));
        assertTrue(hasStorage, "Should have data storage capability");
    }

    private void verifyDataBandwidth(List<BOMEntry> components) {
        boolean hasSufficientBandwidth = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("speed") ||
                        e.getSpecs().containsKey("bandwidth"));
        assertTrue(hasSufficientBandwidth, "Should have sufficient data bandwidth");
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
                            material.contains("316L") ||
                            material.contains("Sapphire") ||
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