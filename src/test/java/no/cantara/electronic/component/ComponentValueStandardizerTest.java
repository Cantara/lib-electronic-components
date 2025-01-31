package no.cantara.electronic.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComponentValueStandardizerTest {
    private ComponentValueStandardizer standardizer;

    @BeforeEach
    void setUp() {
        standardizer = new ComponentValueStandardizer();
    }

    @Test
    void testResistorValueStandardization() {
        // Basic values
        assertEquals("4.7kΩ", standardizer.standardizeResistorValue("4.7k"));
        assertEquals("4.7kΩ", standardizer.standardizeResistorValue("4K7"));
        assertEquals("100Ω", standardizer.standardizeResistorValue("100R"));
        assertEquals("1MΩ", standardizer.standardizeResistorValue("1M"));
        assertEquals("0Ω", standardizer.standardizeResistorValue("0R"));

        // Alternative notations
        assertEquals("4.7kΩ", standardizer.standardizeResistorValue("4.7 k"));
        assertEquals("4.7kΩ", standardizer.standardizeResistorValue("4k7"));
        assertEquals("100Ω", standardizer.standardizeResistorValue("100"));
        assertEquals("1.5MΩ", standardizer.standardizeResistorValue("1M5"));

        // Case insensitivity
        assertEquals("4.7kΩ", standardizer.standardizeResistorValue("4.7K"));
        assertEquals("4.7kΩ", standardizer.standardizeResistorValue("4k7"));
        assertEquals("100Ω", standardizer.standardizeResistorValue("100r"));

        // Edge cases
        assertEquals("", standardizer.standardizeResistorValue(""));
        assertEquals("", standardizer.standardizeResistorValue(null));
        assertEquals("100Ω", standardizer.standardizeResistorValue("100ohm"));
    }

    @Test
    void testCapacitorValueStandardization() {
        // Basic values
        assertEquals("100nF", standardizer.standardizeCapacitorValue("100n"));
        assertEquals("10µF", standardizer.standardizeCapacitorValue("10u"));
        assertEquals("4.7µF", standardizer.standardizeCapacitorValue("4.7uF"));
        assertEquals("100pF", standardizer.standardizeCapacitorValue("100p"));
        assertEquals("1mF", standardizer.standardizeCapacitorValue("1m"));

        // Alternative notations
        assertEquals("10µF", standardizer.standardizeCapacitorValue("10uf"));
        assertEquals("10µF", standardizer.standardizeCapacitorValue("10 uF"));
        assertEquals("4.7µF", standardizer.standardizeCapacitorValue("4u7"));
        assertEquals("100µF", standardizer.standardizeCapacitorValue("100"));

        // Case insensitivity
        assertEquals("100nF", standardizer.standardizeCapacitorValue("100N"));
        assertEquals("10µF", standardizer.standardizeCapacitorValue("10U"));
        assertEquals("100pF", standardizer.standardizeCapacitorValue("100P"));

        // Edge cases
        assertEquals("", standardizer.standardizeCapacitorValue(""));
        assertEquals("", standardizer.standardizeCapacitorValue(null));
    }

    @Test
    void testInductorValueStandardization() {
        // Basic values
        assertEquals("10µH", standardizer.standardizeInductorValue("10u"));
        assertEquals("100nH", standardizer.standardizeInductorValue("100n"));
        assertEquals("1mH", standardizer.standardizeInductorValue("1m"));
        assertEquals("4.7µH", standardizer.standardizeInductorValue("4.7uH"));

        // Alternative notations
        assertEquals("10µH", standardizer.standardizeInductorValue("10uh"));
        assertEquals("10µH", standardizer.standardizeInductorValue("10 uH"));
        assertEquals("4.7µH", standardizer.standardizeInductorValue("4u7"));
        assertEquals("100µH", standardizer.standardizeInductorValue("100"));

        // Case insensitivity
        assertEquals("100nH", standardizer.standardizeInductorValue("100N"));
        assertEquals("10µH", standardizer.standardizeInductorValue("10U"));
        assertEquals("1mH", standardizer.standardizeInductorValue("1M"));

        // Edge cases
        assertEquals("", standardizer.standardizeInductorValue(""));
        assertEquals("", standardizer.standardizeInductorValue(null));
    }

    @Test
    void testComponentValueStandardization() {
        // Test with different component types
        assertEquals("4.7kΩ", standardizer.standardizeValue("4.7k", ComponentType.RESISTOR));
        assertEquals("100nF", standardizer.standardizeValue("100n", ComponentType.CAPACITOR));
        assertEquals("10µH", standardizer.standardizeValue("10u", ComponentType.INDUCTOR));

        // Test with non-value components
        assertEquals("2N2222", standardizer.standardizeValue("2N2222", ComponentType.TRANSISTOR));
        assertEquals("LM358", standardizer.standardizeValue("LM358", ComponentType.OPAMP));
        assertEquals("ATmega328P", standardizer.standardizeValue("ATmega328P", ComponentType.MICROCONTROLLER));

        // Edge cases
        assertEquals("", standardizer.standardizeValue("", ComponentType.RESISTOR));
        assertEquals("", standardizer.standardizeValue(null, ComponentType.CAPACITOR));
    }

    @Test
    void testValueValidation() {
        // Valid values
        assertTrue(standardizer.isValidValue("4.7k", ComponentType.RESISTOR));
        assertTrue(standardizer.isValidValue("100n", ComponentType.CAPACITOR));
        assertTrue(standardizer.isValidValue("10uH", ComponentType.INDUCTOR));

        // Invalid values
        assertFalse(standardizer.isValidValue("abc", ComponentType.RESISTOR));
        assertFalse(standardizer.isValidValue("xyz", ComponentType.CAPACITOR));
        assertFalse(standardizer.isValidValue("123xyz", ComponentType.INDUCTOR));

        // Edge cases
        assertFalse(standardizer.isValidValue("", ComponentType.RESISTOR));
        assertFalse(standardizer.isValidValue(null, ComponentType.CAPACITOR));
    }
}