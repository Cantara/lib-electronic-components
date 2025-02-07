package no.cantara.electronic.component.lib;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MPNUtilsTest {
    private MPNUtils mpnUtils;

    @BeforeEach
    void setUp() {
        mpnUtils = new MPNUtils();
    }

    @Test
    void testMicrocontrollerPatterns() {
        // Microchip MCUs
        assertTrue(MPNUtils.matchesType("PIC16F877A", ComponentType.MICROCONTROLLER_MICROCHIP),
                "Should match Microchip PIC pattern");
        assertTrue(MPNUtils.matchesType("dsPIC33FJ128GP802", ComponentType.MICROCONTROLLER_MICROCHIP),
                "Should match Microchip dsPIC pattern");
        assertFalse(MPNUtils.matchesType("ABC123", ComponentType.MICROCONTROLLER_MICROCHIP),
                "Should not match invalid Microchip pattern");

        // ST MCUs
        assertTrue(MPNUtils.matchesType("STM32F103C8T6", ComponentType.MICROCONTROLLER_ST),
                "Should match ST STM32 pattern");
        assertTrue(MPNUtils.matchesType("STM8S003F3P6", ComponentType.MICROCONTROLLER_ST),
                "Should match ST STM8 pattern");
        assertFalse(MPNUtils.matchesType("ABC123", ComponentType.MICROCONTROLLER_ST),
                "Should not match invalid ST pattern");

        // Atmel MCUs
        assertTrue(MPNUtils.matchesType("ATMEGA328P", ComponentType.MICROCONTROLLER_ATMEL),
                "Should match Atmel ATMega pattern");
        assertTrue(MPNUtils.matchesType("ATTINY85", ComponentType.MICROCONTROLLER_ATMEL),
                "Should match Atmel ATTiny pattern");
        assertFalse(MPNUtils.matchesType("ABC123", ComponentType.MICROCONTROLLER_ATMEL),
                "Should not match invalid Atmel pattern");
    }

    @Test
    void testMosfetPatterns() {
        // Infineon MOSFETs
        assertTrue(MPNUtils.matchesType("IRF530", ComponentType.MOSFET_INFINEON),
                "Should match Infineon IRF pattern");
        assertTrue(MPNUtils.matchesType("IRL2203N", ComponentType.MOSFET_INFINEON),
                "Should match Infineon IRL pattern");
        assertFalse(MPNUtils.matchesType("ABC123", ComponentType.MOSFET_INFINEON),
                "Should not match invalid Infineon pattern");

        // ST MOSFETs
        assertTrue(MPNUtils.matchesType("STF5N52U", ComponentType.MOSFET_ST),
                "Should match ST MOSFET pattern");
        assertTrue(MPNUtils.matchesType("STD4N52K3", ComponentType.MOSFET_ST),
                "Should match ST MOSFET pattern with suffix");
        assertFalse(MPNUtils.matchesType("ABC123", ComponentType.MOSFET_ST),
                "Should not match invalid ST MOSFET pattern");
    }

    @Test
    void testPassiveComponentPatterns() {
        // Resistors
        assertTrue(MPNUtils.matchesType("CRCW0603100RFKEA", ComponentType.RESISTOR_CHIP_VISHAY),
                "Should match Vishay chip resistor pattern");
        assertTrue(MPNUtils.matchesType("RC0603FR-07100RL", ComponentType.RESISTOR_CHIP_YAGEO),
                "Should match Yageo chip resistor pattern");
        assertTrue(MPNUtils.matchesType("ERJ3GEYJ102V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "Should match Panasonic chip resistor pattern");
        // Panasonic resistors - various formats
        assertTrue(MPNUtils.matchesType("ERJ3GEYJ102V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "Should match Panasonic ERJ3 resistor pattern");
        assertTrue(MPNUtils.matchesType("ERJ6GEY0R00V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "Should match Panasonic ERJ6 resistor pattern");
        assertTrue(MPNUtils.matchesType("ERA3AEB103V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "Should match Panasonic ERA3 resistor pattern");
        assertTrue(MPNUtils.matchesType("ERA6AEB104V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "Should match Panasonic ERA6 resistor pattern");

        // Capacitors
        assertTrue(MPNUtils.matchesType("C0603C104K5RACTU", ComponentType.CAPACITOR_CERAMIC_KEMET),
                "Should match Kemet ceramic capacitor pattern");
        assertTrue(MPNUtils.matchesType("GRM188R71H104KA93D", ComponentType.CAPACITOR_CERAMIC_MURATA),
                "Should match Murata ceramic capacitor pattern");
        assertTrue(MPNUtils.matchesType("CL10B104KB8NNNC", ComponentType.CAPACITOR_CERAMIC_SAMSUNG),
                "Should match Samsung ceramic capacitor pattern");
    }

    @Test
    void testPanasonicResistorFormats() {
        // ERJ series
        assertTrue(MPNUtils.matchesType("ERJ3GEYJ102V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "ERJ3 series");
        assertTrue(MPNUtils.matchesType("ERJ6GEYJ103V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "ERJ6 series");
        assertTrue(MPNUtils.matchesType("ERJ8GEYJ104V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "ERJ8 series");
        assertTrue(MPNUtils.matchesType("ERJ14NF1000U", ComponentType.RESISTOR_CHIP_PANASONIC),
                "ERJ14 series");

        // ERA series
        assertTrue(MPNUtils.matchesType("ERA3AEB103V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "ERA3 series");
        assertTrue(MPNUtils.matchesType("ERA6AEB104V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "ERA6 series");
        assertTrue(MPNUtils.matchesType("ERA8AEB105V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "ERA8 series");

        // Special values
        assertTrue(MPNUtils.matchesType("ERJ3GEY0R00V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "Zero ohm resistor");
        assertTrue(MPNUtils.matchesType("ERJ3GEYJ000V", ComponentType.RESISTOR_CHIP_PANASONIC),
                "Zero ohm alternate format");
    }

    @Test
    void testNormalization() {
        assertEquals("IRF530N", MPNUtils.normalize("IRF530N"),
                "Should maintain original correct format");
        assertEquals("IRF530N", MPNUtils.normalize("irf530n"),
                "Should convert to uppercase");
        assertEquals("IRF530N", MPNUtils.normalize(" IRF530N "),
                "Should trim whitespace");
        assertEquals("", MPNUtils.normalize(null),
                "Should handle null input");
        assertEquals("", MPNUtils.normalize(""),
                "Should handle empty string");
        assertEquals("", MPNUtils.normalize("  "),
                "Should handle whitespace only");
    }

    @Test
    void testSimilarityCalculation() {
        String mpn1 = "LM358";
        String mpn2 = "MC1458";
        double similarity = MPNUtils.calculateSimilarity(mpn1, mpn2);
        System.out.println("Similarity between " + mpn1 + " and " + mpn2 + ": " + similarity);
        assertTrue(similarity > 0.6,
                "Op-amp cross-manufacturer similarity should be high");

        // Same component different manufacturers
        assertTrue(MPNUtils.calculateSimilarity("IRF530", "STF530") > 0.7,
                "MOSFET cross-manufacturer similarity should be high");
        assertTrue(MPNUtils.calculateSimilarity("LM358", "MC1458") > 0.6,
                "Op-amp cross-manufacturer similarity should be high");
        assertTrue(MPNUtils.calculateSimilarity("2N2222", "PN2222") > 0.7,
                "Transistor cross-manufacturer similarity should be high");

        // Different components
        assertTrue(MPNUtils.calculateSimilarity("IRF530", "LM358") < 0.5,
                "Different component types should have low similarity");
        assertTrue(MPNUtils.calculateSimilarity("2N2222", "BC547") < 0.5,
                "Different transistor families should have low similarity");

        // Edge cases
        assertEquals(0.0, MPNUtils.calculateSimilarity("", "IRF530"),
                "Empty string should have zero similarity");
        assertEquals(0.0, MPNUtils.calculateSimilarity(null, "IRF530"),
                "Null should have zero similarity");
    }

    @Test
    void testPackageVariations() {
        assertTrue(MPNUtils.calculateSimilarity("LM358N", "LM358D") > 0.8,
                "Same component with different packages should have high similarity");
        assertTrue(MPNUtils.calculateSimilarity("IRF530N", "IRF530") > 0.8,
                "Same component with/without suffix should have high similarity");
        assertTrue(MPNUtils.calculateSimilarity("ATMEGA328P-PU", "ATMEGA328P-AU") > 0.8,
                "Same component with different package codes should have high similarity");
    }

    @Test
    public void shouldFindHandlersForMPNs() {
        // Test finding handlers for specific MPNs
        ManufacturerHandler handler;

        // Test TI components
        handler = MPNUtils.getManufacturerHandler("LM358");
        assertNotNull(handler, "Should find handler for LM358");
        assertTrue(handler.getClass().getSimpleName().contains("TI"),
                "Should be TI handler");

        // Test Atmel components
        handler = MPNUtils.getManufacturerHandler("ATMEGA328P");
        assertNotNull(handler, "Should find handler for ATMEGA328P");

    }

    @Test
    public void shouldGetHandlersForComponentTypes() {
        // Check handlers for specific component types
        Set<ManufacturerHandler> opampHandlers = MPNUtils.getHandlersForType(ComponentType.OPAMP);
        assertFalse(opampHandlers.isEmpty(), "Should find handlers for op-amps");

        Set<ManufacturerHandler> mcuHandlers = MPNUtils.getHandlersForType(ComponentType.MICROCONTROLLER);
        assertFalse(mcuHandlers.isEmpty(), "Should find handlers for microcontrollers");

        // Print all handlers for debugging
        System.out.println("\nHandlers for OPAMP:");
        opampHandlers.forEach(h -> System.out.println("- " + h.getClass().getSimpleName()));

        System.out.println("\nHandlers for MICROCONTROLLER:");
        mcuHandlers.forEach(h -> System.out.println("- " + h.getClass().getSimpleName()));
    }

    @Test
    public void shouldExtractPackageCodes() {
        // Now we can properly test package code extraction
        assertEquals("SOIC", MPNUtils.getPackageCode("LM358D"));
        assertEquals("PU", MPNUtils.getPackageCode("ATMEGA328P-PU"));
        assertEquals("T6", MPNUtils.getPackageCode("STM32F103C8T6"));

        // Print handler information for debugging
        String mpn = "LM358D";
        ManufacturerHandler handler = MPNUtils.getManufacturerHandler(mpn);
        System.out.println("\nHandler for " + mpn + ":");
        System.out.println("- Class: " + (handler != null ? handler.getClass().getSimpleName() : "null"));
        System.out.println("- Package code: " + MPNUtils.getPackageCode(mpn));
    }
}