package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.ElectronicPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentTypeDetectorTest {

    @Test
    void testResistorDetection() {
        assertTrue(ComponentTypeDetector.isResistor("CRCW0603100RFKEA"), "Should detect Vishay resistor");
        assertTrue(ComponentTypeDetector.isResistor("RC0603FR-07100RL"), "Should detect Yageo resistor");
        assertFalse(ComponentTypeDetector.isResistor("GRM188R71H104KA93D"), "Should not detect capacitor as resistor");
    }

    @Test
    void testCapacitorDetection() {
        assertTrue(ComponentTypeDetector.isCapacitor("GRM188R71H104KA93D"), "Should detect Murata capacitor");
        assertTrue(ComponentTypeDetector.isCapacitor("C0603C104K5RACTU"), "Should detect Kemet capacitor");
        assertFalse(ComponentTypeDetector.isCapacitor("CRCW0603100RFKEA"), "Should not detect resistor as capacitor");
    }

    @Test
    void testInductorDetection() {
        assertTrue(ComponentTypeDetector.isInductor("MLF2012DR22KTD25"), "Should detect TDK inductor");
        assertTrue(ComponentTypeDetector.isInductor("LQM2MPN2R2NG0L"), "Should detect Murata inductor");
        assertFalse(ComponentTypeDetector.isInductor("GRM188R71H104KA93D"), "Should not detect capacitor as inductor");
    }

    @Test
    void testSemiconductorDetection() {
        // MOSFETs
        assertTrue(ComponentTypeDetector.isMosfet("IRF530"), "Should detect IRF MOSFET");
        assertTrue(ComponentTypeDetector.isMosfet("IRL2203N"), "Should detect IRL MOSFET");
        assertTrue(ComponentTypeDetector.isMosfet("STF5N52U"), "Should detect ST MOSFET");

        // Non-semiconductors
        ComponentType resistorType = ComponentTypeDetector.determineComponentType("CRCW0603100RFKEA");
        assertTrue(resistorType.isPassive(), "Should detect resistor as passive");
        assertFalse(resistorType.isSemiconductor(), "Resistor should not be semiconductor");

        ComponentType capacitorType = ComponentTypeDetector.determineComponentType("GRM188R71H104KA93D");
        assertTrue(capacitorType.isPassive(), "Should detect capacitor as passive");
        assertFalse(capacitorType.isSemiconductor(), "Capacitor should not be semiconductor");
    }

    @Test
    void testICDetection() {
        // Generic ICs
        assertTrue(ComponentTypeDetector.isIC("74HC00"), "Should detect 74-series IC");
        assertTrue(ComponentTypeDetector.isIC("CD4001"), "Should detect CD4000-series IC");
        assertTrue(ComponentTypeDetector.isIC("LM358"), "Should detect op-amp as IC");

        // Not ICs
        assertFalse(ComponentTypeDetector.isIC("2N2222"), "Should not detect transistor as IC");
        assertFalse(ComponentTypeDetector.isIC("1N4148"), "Should not detect diode as IC");
    }

    @Test
    void testAnalogICDetection() {
        assertTrue(ComponentTypeDetector.isAnalogIC("LM358"), "Should detect op-amp as analog IC");
        assertTrue(ComponentTypeDetector.isAnalogIC("LM7805"), "Should detect voltage regulator as analog IC");
        assertFalse(ComponentTypeDetector.isAnalogIC("74HC00"), "Should not detect digital IC as analog IC");
    }

    @Test
    void testDigitalICDetection() {
        assertTrue(ComponentTypeDetector.isDigitalIC("74HC00"), "Should detect 74-series as digital IC");
        assertTrue(ComponentTypeDetector.isDigitalIC("CD4001"), "Should detect CD4000-series as digital IC");
        assertFalse(ComponentTypeDetector.isDigitalIC("LM358"), "Should not detect op-amp as digital IC");
    }

    @Test
    void testComponentAnalysis() {
        // Passive components
        ComponentType resistorType = ComponentTypeDetector.determineComponentType("CRCW0603100RFKEA");
        assertTrue(resistorType.isPassive());
        assertFalse(resistorType.isSemiconductor());
        assertEquals(ComponentType.RESISTOR, resistorType.getBaseType());

        // MOSFET analysis
        ComponentType mosfetType = ComponentTypeDetector.determineComponentType("IRF530");
        assertFalse(mosfetType.isPassive());
        assertTrue(mosfetType.isSemiconductor());
        assertEquals(ComponentType.MOSFET, mosfetType.getBaseType());

        // Digital IC analysis
        ComponentType icType = ComponentTypeDetector.determineComponentType("74HC00");
        assertFalse(icType.isPassive());
        assertTrue(icType.isSemiconductor());
        assertEquals(ComponentType.IC, icType.getBaseType());
    }

    @Test
    void testNullAndEmptyInput() {
        assertFalse(ComponentTypeDetector.isResistor((String)null), "Should handle null string");
        assertFalse(ComponentTypeDetector.isResistor(""), "Should handle empty string");
        assertFalse(ComponentTypeDetector.isResistor(null), "Should handle null Bom");

        assertNull(ComponentTypeDetector.determineComponentType(null), "Should handle null in type determination");
        assertNull(ComponentTypeDetector.determineComponentType(""), "Should handle empty string in type determination");
    }

    private ElectronicPart createTestElectronicPart(String mpn) {
        ElectronicPart electronicPart = new ElectronicPart();
        electronicPart.setMpn(mpn);
        return electronicPart;
    }
}