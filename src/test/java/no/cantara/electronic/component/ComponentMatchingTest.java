package no.cantara.electronic.component;

import no.cantara.electronic.component.lib.MPNUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComponentMatchingTest {

    @Test
    void testOpAmpAlternatives() {
        // Direct equivalents
        assertHighSimilarity("LM358", "MC1458", "Known equivalent op-amps");
        assertHighSimilarity("LM358N", "MC1458N", "Known equivalent op-amps with same package");

        // Package variations
        assertHighSimilarity("LM358N", "LM358D", "Same op-amp, different packages");
        assertHighSimilarity("MC1458N", "MC1458D", "Same op-amp, different packages");

        // Different manufacturers
        assertHighSimilarity("LM358", "TL072", "Known compatible op-amps");

        // Should not match
//        assertLowSimilarity("LM358", "LM317", "Op-amp vs voltage regulator");
    }

    @Test
    void testMosfetAlternatives() {
        // Direct equivalents across manufacturers
        assertHighSimilarity("IRF530", "STF530", "Equivalent MOSFETs across manufacturers");
        assertHighSimilarity("IRF530N", "FQP30N06", "Compatible N-channel MOSFETs");

        // Package variations
        assertHighSimilarity("IRF530N", "IRF530", "Same MOSFET with/without package suffix");

        // Should not match
        assertLowSimilarity("IRF530", "IRF9530", "N-channel vs P-channel");
    }

    @Test
    void testTransistorAlternatives() {
        // Known equivalent pairs
        assertHighSimilarity("2N2222", "PN2222", "Known equivalent transistors");
        assertHighSimilarity("2N2907", "PN2907", "Known equivalent transistors");

        // Package variations
        assertHighSimilarity("2N2222A", "2N2222", "Same transistor with/without suffix");

        // Should not match
        assertLowSimilarity("2N2222", "2N3904", "Different transistor types");
    }

    @Test
    void testICAlternatives() {
        // Logic IC equivalents
//        assertHighSimilarity("74LS00", "74HC00", "Compatible logic ICs");
        assertHighSimilarity("CD4001", "CD4001BE", "Same IC with package variation");

        // Should not match
        assertLowSimilarity("74LS00", "74LS04", "Different logic functions");
    }

    @Test
    void testPassiveComponentAlternatives() {
        // Resistors
        assertHighSimilarity("CRCW060310K0FKEA", "RC0603FR-0710KL",
                "Same value resistors from different manufacturers");

        // Capacitors
        assertHighSimilarity("GRM188R71H104KA93D", "C0603C104K5RACTU",
                "Same value capacitors from different manufacturers");

        // Should not match
        assertLowSimilarity("CRCW060310K0FKEA", "CRCW06031K00FKEA",
                "Different value resistors");
    }

    @Test
    void testVoltageRegulatorAlternatives() {
        // Fixed voltage regulators
        assertHighSimilarity("LM7805", "MC7805", "Same voltage rating across manufacturers");
        assertHighSimilarity("LM7805CT", "LM7805T", "Same regulator with different package codes");
        assertLowSimilarity("LM7805", "LM7812", "Different voltage ratings");

        // Adjustable regulators
        assertHighSimilarity("LM317T", "LM317K", "Same adjustable regulator different packages");
        assertHighSimilarity("LM317", "LM350", "Compatible adjustable regulators");
        assertLowSimilarity("LM317", "LM7805", "Adjustable vs fixed regulator");
    }

    @Test
    void testDiodeAlternatives() {
        // Rectifier diodes
        assertHighSimilarity("1N4001", "1N4002", "Compatible rectifier diodes");
        assertHighSimilarity("1N4007", "RL207", "Equivalent parts different manufacturers");

        // Small signal diodes
        assertHighSimilarity("1N4148", "1N914", "Known equivalent signal diodes");
        assertHighSimilarity("BAT54", "BAT54S", "Same diode different package");

        // Zener diodes
        assertHighSimilarity("1N4733A", "1N4733", "Same zener with/without suffix");
        assertLowSimilarity("1N4733", "1N4737", "Different zener voltages");
    }

    @Disabled
    @Test
    void testCrystalOscillatorAlternatives() {
        // Crystals
        assertHighSimilarity("HC49/US 16MHz", "HC49/S 16MHz", "Same frequency different packages");
        assertLowSimilarity("HC49/US 16MHz", "HC49/US 20MHz", "Different frequencies");

        // Oscillators
        assertHighSimilarity("SG-8002DC 25MHz", "SG-8002JC 25MHz", "Same oscillator different packages");
        assertLowSimilarity("SG-8002DC 25MHz", "SG-8002DC 24MHz", "Different frequencies");
    }

    @Disabled
    @Test
    void testInductorAlternatives() {
        // Power inductors
        assertHighSimilarity("IHLP2525CZER100M01", "IHLP2525CZER101M01", "Same value different codes");
        assertHighSimilarity("744771147", "744771147 47uH", "Same part with/without value in MPN");

        // Common mode chokes
        assertHighSimilarity("DLW21SN102SQ2L", "DLW21SN102SQ2", "Same choke different suffix");
        assertLowSimilarity("DLW21SN102SQ2L", "DLW21SN222SQ2L", "Different impedance values");
    }

    @Test
    void testConnectorAlternatives() {
        // Headers
        assertHighSimilarity("61300211121", "61300211021", "Compatible header variants");
        assertLowSimilarity("61300211121", "61300411121", "Different pin counts");

        // Terminal blocks
        assertHighSimilarity("282836-2", "282836-5", "Same series different positions");
        assertHighSimilarity("1-284392-0", "1-284392-1", "Same series different variants");
    }

    @Test
    void testLEDAlternatives() {
        // Standard LEDs
        assertHighSimilarity("TLHR5400", "TLHR5401", "Same LED different bin");
        assertHighSimilarity("LG R971", "LG R971-KN", "Same LED with/without suffix");

        // RGB LEDs
        assertHighSimilarity("CLVBA-FKA", "CLVBA-FKB", "Same RGB LED different bins");
        assertLowSimilarity("CLVBA-FKA", "CLVBA-FCA", "Different color temperatures");
    }

    @Test
    void testSensorAlternatives() {
        // Temperature sensors
        assertHighSimilarity("LM35DZ", "LM35DT", "Same sensor different packages");
        assertHighSimilarity("DS18B20", "DS18B20+", "Same sensor with/without plus");

        // Accelerometers
        assertHighSimilarity("ADXL345BCCZ", "ADXL345BCCZ-RL", "Same accelerometer different packaging");
        assertLowSimilarity("ADXL345", "ADXL346", "Different accelerometer models");
    }

    @Test
    void testMemoryAlternatives() {
        // EEPROM
        assertHighSimilarity("24LC256-I/P", "24LC256-I/SN", "Same EEPROM different packages");
        assertHighSimilarity("AT24C256C", "24LC256", "Compatible EEPROMs different manufacturers");

        // Flash
        assertHighSimilarity("W25Q32JVSSIQ", "W25Q32JVSIQ", "Same flash with/without suffix");
        assertLowSimilarity("W25Q32", "W25Q64", "Different capacities");
    }

    @Disabled
    @Test
    void testMixedComponentTypes() {
        // Should all have very low or zero similarity
        assertLowSimilarity("1N4148", "BC547", "Diode vs Transistor");
        assertLowSimilarity("LM358", "LM7805", "Op-amp vs Voltage Regulator");
        assertLowSimilarity("IRF530", "2N2222", "MOSFET vs BJT");
        assertLowSimilarity("100nF", "100Ω", "Capacitor vs Resistor");
    }

    private void assertHighSimilarity(String mpn1, String mpn2, String message) {
        double similarity = MPNUtils.calculateSimilarity(mpn1, mpn2);
        assertTrue(similarity > 0.7,
                message + " - Expected high similarity but got " + similarity +
                        " for " + mpn1 + " and " + mpn2);
    }

    private void assertLowSimilarity(String mpn1, String mpn2, String message) {
        double similarity = MPNUtils.calculateSimilarity(mpn1, mpn2);
        assertTrue(similarity < 0.5,
                message + " - Expected low similarity but got " + similarity +
                        " for " + mpn1 + " and " + mpn2);
    }

    private void assertExactSimilarity(String mpn1, String mpn2, double expectedSimilarity, String message) {
        double similarity = MPNUtils.calculateSimilarity(mpn1, mpn2);
        assertEquals(expectedSimilarity, similarity, 0.01,
                message + " - Expected similarity " + expectedSimilarity + " but got " + similarity +
                        " for " + mpn1 + " and " + mpn2);
    }
}