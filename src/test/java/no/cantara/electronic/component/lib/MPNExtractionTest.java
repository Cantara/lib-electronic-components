package no.cantara.electronic.component.lib;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import no.cantara.electronic.component.lib.ManufacturerHandler;

/**
 * Tests for extracting MPNs from various text formats.
 * Shows different ways to use the MPNUtils library for finding and analyzing MPNs in text.
 */
public class MPNExtractionTest {

    @Test
    public void shouldExtractMPNFromSimpleDescriptions() {
        // Basic component descriptions
        assertMPNExtraction("Op Amp LM358", "LM358");
        assertMPNExtraction("ATMEGA328P Microcontroller", "ATMEGA328P");
        assertMPNExtraction("STM32F103C8T6 ARM MCU", "STM32F103C8T6");
        assertMPNExtraction("2N2222A Transistor", "2N2222A");
    }

    @Test
    public void shouldExtractMPNFromBOMLines() {
        // Common BOM formats
        assertMPNExtraction(
                "IC Op Amp Dual LM358 8-SOIC",
                "LM358",
                ComponentType.OPAMP
        );

        assertMPNExtraction(
                "MCU 8-Bit 20MHz 32KB ATMEGA328P-PU DIP-28",
                "ATMEGA328P-PU",
                ComponentType.MICROCONTROLLER
        );

        assertMPNExtraction(
                "STM32F103C8T6 32-Bit MCU Cortex-M3",
                "STM32F103C8T6",
                ComponentType.MICROCONTROLLER
        );
    }

    @Test
    public void shouldExtractMPNFromStructuredText() {
        // Key-value and structured formats
        assertMPNExtraction("Part Number: LM358", "LM358");
        assertMPNExtraction("MPN:ATMEGA328P-PU;", "ATMEGA328P-PU");
        assertMPNExtraction("Component: STM32F103C8T6, Package: LQFP48", "STM32F103C8T6");
        assertMPNExtraction("IC=LM358N|Package=DIP-8", "LM358N");
    }

    @Test
    public void shouldExtractMPNWithPackageInfo() {
        // Debug: Print handler info before assertions
        String[] mpns = {"LM358N", "ATMEGA328P-PU", "STM32F103C8T6", "LM7805CT"};
        for (String mpn : mpns) {
            ManufacturerHandler handler = MPNUtils.getManufacturerHandler(mpn);
            String pkg = MPNUtils.getPackageCode(mpn);
            System.out.println("DEBUG: " + mpn + " -> Handler: " +
                (handler != null ? handler.getClass().getSimpleName() : "null") +
                ", Package: '" + pkg + "'");
        }

        // Descriptions with package information
        assertMPNAndPackage("LM358N DIP-8", "LM358N", "DIP");
        assertMPNAndPackage("ATMEGA328P-PU DIP-28", "ATMEGA328P-PU", "PDIP");
        assertMPNAndPackage("STM32F103C8T6 LQFP48", "STM32F103C8T6", "LQFP");
        assertMPNAndPackage("LM7805CT TO-220", "LM7805CT", "TO-220");
    }

    @Test
    public void shouldIdentifyComponentType() {
        // Extract MPN and verify component type
        assertMPNAndType("Op Amp LM358", "LM358", ComponentType.OPAMP);
        assertMPNAndType("MCU ATMEGA328P", "ATMEGA328P", ComponentType.MICROCONTROLLER);
        assertMPNAndType("2N2222A NPN", "2N2222A", ComponentType.TRANSISTOR);
        assertMPNAndType("STM32F103", "STM32F103", ComponentType.MICROCONTROLLER);
    }

    @Test
    public void shouldHandleMultipleComponents() {
        // Text with multiple component references
        String text = "Circuit uses LM358 op-amp and ATMEGA328P MCU";
        String mpn = MPNUtils.findMPNInText(text);
        assertNotNull(mpn, "Should find at least one MPN");
        assertTrue(mpn.equals("LM358") || mpn.equals("ATMEGA328P"),
                "Should find one of the valid MPNs");
    }

    @Test
    public void shouldHandleInvalidInput() {
        assertNull(MPNUtils.findMPNInText(null), "Null input should return null");
        assertNull(MPNUtils.findMPNInText(""), "Empty string should return null");
        assertNull(MPNUtils.findMPNInText("   "), "Whitespace should return null");
        assertNull(MPNUtils.findMPNInText("No MPN here"), "Text without MPN should return null");
        //assertNull(MPNUtils.findMPNInText("12345"), "Just numbers should return null");
    }

    /**
     * Helper method to test MPN extraction with detailed output
     */
    private void assertMPNExtraction(String text, String expectedMPN) {
        System.out.println("\nTesting MPN extraction:");
        System.out.println("Input text: " + text);
        System.out.println("Expected MPN: " + expectedMPN);

        String actualMPN = MPNUtils.findMPNInText(text);
        System.out.println("Extracted MPN: " + actualMPN);

        assertEquals(expectedMPN, actualMPN,
                String.format("Failed to extract MPN from: '%s'", text));
    }

    /**
     * Helper method to test MPN extraction with component type verification
     */
    private void assertMPNExtraction(String text, String expectedMPN, ComponentType expectedType) {
        System.out.println("\nTesting MPN extraction with type:");
        System.out.println("Input text: " + text);
        System.out.println("Expected MPN: " + expectedMPN);
        System.out.println("Expected type: " + expectedType);

        String actualMPN = MPNUtils.findMPNInText(text);
        System.out.println("Extracted MPN: " + actualMPN);

        assertEquals(expectedMPN, actualMPN,
                String.format("Failed to extract MPN from: '%s'", text));

        ComponentType actualType = MPNUtils.getComponentType(actualMPN);
        System.out.println("Actual type: " + actualType);

        assertTrue(actualType.toString().contains(expectedType.toString()),
                String.format("Wrong component type for MPN: '%s'", actualMPN));
    }

    /**
     * Helper method to test MPN extraction with package code verification
     */
    private void assertMPNAndPackage(String text, String expectedMPN, String expectedPackage) {
        System.out.println("\nTesting MPN and package extraction:");
        System.out.println("Input text: " + text);
        System.out.println("Expected MPN: " + expectedMPN);
        System.out.println("Expected package: " + expectedPackage);

        String actualMPN = MPNUtils.findMPNInText(text);
        System.out.println("Extracted MPN: " + actualMPN);

        assertEquals(expectedMPN, actualMPN,
                String.format("Failed to extract MPN from: '%s'", text));

        String actualPackage = MPNUtils.getPackageCode(actualMPN);
        System.out.println("Actual package: " + actualPackage);

        assertEquals(expectedPackage.toString(),actualPackage.toString(),
                String.format("Wrong package code for MPN: '%s'", actualMPN));
    }

    /**
     * Helper method to test MPN extraction with type verification
     */
    private void assertMPNAndType(String text, String expectedMPN, ComponentType expectedType) {
        System.out.println("\nTesting MPN and type extraction:");
        System.out.println("Input text: " + text);
        System.out.println("Expected MPN: " + expectedMPN);
        System.out.println("Expected type: " + expectedType);

        String actualMPN = MPNUtils.findMPNInText(text);
        System.out.println("Extracted MPN: " + actualMPN);

        assertEquals(expectedMPN, actualMPN,
                String.format("Failed to extract MPN from: '%s'", text));

        ComponentType actualType = MPNUtils.getComponentType(actualMPN);
        System.out.println("Actual type: " + actualType);

        assertTrue(actualType == expectedType || actualType.getBaseType() == expectedType,
                String.format("Wrong component type for MPN: '%s', expected %s but was %s",
                        actualMPN, expectedType, actualType));
    }
}