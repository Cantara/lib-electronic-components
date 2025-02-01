package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommunicationsComponentsTest {

    private PCBABOM communicationsPcba;
    private MechanicalBOM communicationsMechanical;

    @BeforeEach
    void setUp() {
        communicationsPcba = CommunicationsComponents.PCBAComponents.createMainCommunicationsBoard();
        communicationsMechanical = CommunicationsComponents.MechanicalComponents.createHousing();
    }

    @Test
    void shouldCreateMainCommunicationsBoard() {
        assertNotNull(communicationsPcba, "Communications PCBA should not be null");
        List<BOMEntry> components = communicationsPcba.getBomEntries();
        assertFalse(components.isEmpty(), "PCBA should have components");

        verifyMainProcessor(components);
        verifyAcousticModem(components);
        verifyRfComponents(components);
        verifyNetworkComponents(components);
        verifyEmergencyBeacon(components);
    }

    @Test
    void shouldCreateCommunicationsHousing() {
        assertNotNull(communicationsMechanical, "Communications housing should not be null");
        List<BOMEntry> components = communicationsMechanical.getBomEntries();
        assertFalse(components.isEmpty(), "Mechanical BOM should have components");

        verifyHousingSpecs(components);
        verifyTransducerAssembly(components);
        verifyAntennaAssembly(components);
        verifyMountingSystem(components);
        verifyConnectors(components);
    }

    @Test
    void shouldMeetPowerRequirements() {
        List<BOMEntry> components = communicationsPcba.getBomEntries();
        verifyPowerConsumption(components);
        verifyPowerDistribution(components);
        verifyPowerEfficiency(components);
    }

    @Test
    void shouldMeetEnvironmentalRequirements() {
        verifyPressureRating(communicationsMechanical.getBomEntries());
        verifyWaterproofing(communicationsMechanical.getBomEntries());
        verifyTemperatureRange(communicationsPcba.getBomEntries());
        verifyCorrosionResistance(communicationsMechanical.getBomEntries());
    }

    private void verifyMainProcessor(List<BOMEntry> components) {
        BOMEntry processor = findComponentByMpn(components, "STM32H735IGK6");
        assertNotNull(processor, "Main processor should be present");

        Map<String, String> specs = processor.getSpecs();
        assertTrue(specs.containsKey("core"), "Should specify core type");
        assertTrue(specs.containsKey("speed"), "Should specify speed");
        assertTrue(specs.containsKey("flash"), "Should specify flash size");
        assertTrue(specs.containsKey("ram"), "Should specify RAM size");
        assertTrue(specs.getOrDefault("features", "").contains("Crypto"),
                "Should have cryptographic features");
    }

    private void verifyAcousticModem(List<BOMEntry> components) {
        // Verify DSP
        BOMEntry dsp = findComponentByMpn(components, "TMS320C5535AZCHA");
        assertNotNull(dsp, "DSP should be present");
        assertTrue(dsp.getSpecs().getOrDefault("features", "").contains("FFT"),
                "Should have FFT capability");

        // Verify ADC
        BOMEntry adc = findComponentByMpn(components, "ADS131E08IPAG");
        assertNotNull(adc, "ADC should be present");
        assertTrue(adc.getSpecs().containsKey("channels"),
                "Should specify channel count");
        assertTrue(adc.getSpecs().containsKey("sample_rate"),
                "Should specify sample rate");

        // Verify power amplifier
        BOMEntry powerAmp = findComponentByMpn(components, "PD55003");
        assertNotNull(powerAmp, "Power amplifier should be present");
        assertTrue(powerAmp.getSpecs().containsKey("gain"),
                "Should specify gain");
    }

    private void verifyRfComponents(List<BOMEntry> components) {
        BOMEntry rfTransceiver = findComponentByMpn(components, "CC1200RHBR");
        assertNotNull(rfTransceiver, "RF transceiver should be present");

        Map<String, String> specs = rfTransceiver.getSpecs();
        assertTrue(specs.containsKey("frequency"), "Should specify frequency");
        assertTrue(specs.containsKey("modulation"), "Should specify modulation types");
        assertTrue(specs.containsKey("sensitivity"), "Should specify sensitivity");
        assertTrue(specs.containsKey("output_power"), "Should specify output power");
    }

    private void verifyNetworkComponents(List<BOMEntry> components) {
        // Verify Ethernet PHY
        BOMEntry ethernetPhy = findComponentByMpn(components, "DP83825IRHBR");
        assertNotNull(ethernetPhy, "Ethernet PHY should be present");
        assertTrue(ethernetPhy.getSpecs().containsKey("speed"),
                "Should specify speed");
        assertTrue(ethernetPhy.getSpecs().getOrDefault("quality", "")
                .contains("Industrial"), "Should be industrial grade");

        // Verify network switch
        boolean hasSwitch = components.stream()
                .anyMatch(e -> e.getDescription().contains("Ethernet Switch") &&
                        e.getSpecs().getOrDefault("features", "").contains("QoS"));
        assertTrue(hasSwitch, "Should have network switch with QoS");
    }

    private void verifyEmergencyBeacon(List<BOMEntry> components) {
        // Verify beacon processor
        BOMEntry beaconProcessor = findComponentByMpn(components, "STM32L412K8U6");
        assertNotNull(beaconProcessor, "Emergency beacon processor should be present");
        assertTrue(beaconProcessor.getSpecs().getOrDefault("features", "")
                .contains("Low Power"), "Should be low power");

        // Verify GPS module
        BOMEntry gps = findComponentByMpn(components, "NEO-M9N");
        assertNotNull(gps, "GPS module should be present");
        assertTrue(gps.getSpecs().containsKey("sensitivity"),
                "Should specify sensitivity");
        assertTrue(gps.getSpecs().containsKey("accuracy"),
                "Should specify accuracy");
    }

    private void verifyHousingSpecs(List<BOMEntry> components) {
        BOMEntry housing = findComponentByMpn(components, "COM-600-HSG");
        assertNotNull(housing, "Housing should be present");

        Map<String, String> specs = housing.getSpecs();
        assertEquals("Grade 5 Titanium", specs.get("material"),
                "Should use correct material");
        assertTrue(specs.get("pressure_rating").contains("300m"),
                "Should meet depth rating");
        assertTrue(specs.containsKey("rf_windows"),
                "Should have RF-transparent windows");
        assertTrue(specs.containsKey("acoustic_window"),
                "Should have acoustic window");
    }

    private void verifyTransducerAssembly(List<BOMEntry> components) {
        BOMEntry transducer = findComponentByMpn(components, "AT12ET");
        assertNotNull(transducer, "Acoustic transducer should be present");

        Map<String, String> specs = transducer.getSpecs();
        assertTrue(specs.containsKey("frequency"), "Should specify frequency range");
        assertTrue(specs.containsKey("beam_width"), "Should specify beam width");
        assertTrue(specs.containsKey("sensitivity"), "Should specify sensitivity");
        assertTrue(specs.containsKey("depth_rating"), "Should specify depth rating");
    }

    private void verifyAntennaAssembly(List<BOMEntry> components) {
        BOMEntry antenna = findComponentByMpn(components, "COM-600-ANT");
        assertNotNull(antenna, "Surface communications antenna should be present");

        Map<String, String> specs = antenna.getSpecs();
        assertTrue(specs.containsKey("frequency"), "Should specify frequency");
        assertTrue(specs.containsKey("gain"), "Should specify gain");
        assertTrue(specs.containsKey("pattern"), "Should specify pattern");
        assertTrue(specs.containsKey("depth_rating"), "Should specify depth rating");
    }

    private void verifyMountingSystem(List<BOMEntry> components) {
        boolean hasMount = components.stream()
                .anyMatch(e -> e.getDescription().contains("Mounting Kit") &&
                        e.getSpecs().get("type").equals("DIN Rail Mount") &&
                        e.getSpecs().get("standard").equals("EN 60715"));
        assertTrue(hasMount, "Should have standardized mounting system");
    }

    private void verifyConnectors(List<BOMEntry> components) {
        // Verify data connector
        boolean hasDataConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Data Connector") &&
                        e.getSpecs().containsKey("contacts") &&
                        e.getSpecs().get("sealing").contains("IP68"));
        assertTrue(hasDataConnector, "Should have waterproof data connector");

        // Verify power connector
        boolean hasPowerConnector = components.stream()
                .anyMatch(e -> e.getDescription().contains("Power Connector") &&
                        e.getSpecs().containsKey("current_rating") &&
                        e.getSpecs().get("sealing").contains("IP68"));
        assertTrue(hasPowerConnector, "Should have waterproof power connector");
    }

    private void verifyPowerConsumption(List<BOMEntry> components) {
        // Verify total power consumption
        boolean hasPowerSpecs = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("power") ||
                        e.getSpecs().containsKey("current"));
        assertTrue(hasPowerSpecs, "Should specify power requirements");
    }

    private void verifyPowerDistribution(List<BOMEntry> components) {
        boolean hasPowerControl = components.stream()
                .anyMatch(e -> e.getDescription().contains("Power Management") &&
                        e.getSpecs().containsKey("outputs"));
        assertTrue(hasPowerControl, "Should have power distribution control");
    }

    private void verifyPowerEfficiency(List<BOMEntry> components) {
        boolean hasEfficiencySpec = components.stream()
                .anyMatch(e -> e.getSpecs().containsKey("efficiency"));
        assertTrue(hasEfficiencySpec, "Should specify power efficiency");
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