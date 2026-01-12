package no.cantara.electronic.component.lib;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ComponentQualityAnalyzer.
 */
class ComponentQualityAnalyzerTest {

    @Test
    @DisplayName("Should return HIGH tier for resistor with full specs and datasheet")
    void testHighQualityResistor() {
        String mpn = "CRCW060310K0FKEA";  // Vishay resistor
        Map<String, Object> specs = new HashMap<>();
        specs.put("resistance", "10000");
        specs.put("voltageRating", "75");
        specs.put("tolerance", "1");
        specs.put("powerRating", "0.1");
        specs.put("temperatureCoefficient", "100");
        specs.put("noiseIndex", "-40");

        QualityResult result = ComponentQualityAnalyzer.calculateQuality(mpn, specs, true);

        System.out.println("Resistor quality result: " + result);
        assertEquals(QualityTier.HIGH, result.tier());
        assertTrue(result.overallScore() >= 0.8);
        assertTrue(result.validMpn());
        assertTrue(result.missingCriticalSpecs().isEmpty());
    }

    @Test
    @DisplayName("Should return INCOMPLETE tier for resistor missing critical resistance spec")
    void testIncompleteResistor() {
        String mpn = "CRCW060310K0FKEA";
        Map<String, Object> specs = new HashMap<>();
        specs.put("voltageRating", "75");
        specs.put("tolerance", "1");
        // Missing critical spec: resistance

        QualityResult result = ComponentQualityAnalyzer.calculateQuality(mpn, specs, false);

        System.out.println("Incomplete resistor result: " + result);
        assertEquals(QualityTier.INCOMPLETE, result.tier());
        assertTrue(result.hasMissingCriticalSpecs());
        assertTrue(result.missingCriticalSpecs().contains("resistance"));
    }

    @Test
    @DisplayName("Should return MEDIUM tier for MOSFET with partial specs")
    void testMediumQualityMosfet() {
        String mpn = "IRF540N";  // Infineon MOSFET
        Map<String, Object> specs = new HashMap<>();
        specs.put("vdsMax", "100");
        specs.put("idMax", "33");
        specs.put("rdson", "0.044");
        // Missing: vgsMax, vgsThreshold, qg, pd, rthJC

        QualityResult result = ComponentQualityAnalyzer.calculateQuality(mpn, specs, false);

        System.out.println("MOSFET quality result: " + result);
        assertTrue(result.tier() == QualityTier.MEDIUM || result.tier() == QualityTier.LOW);
        assertTrue(result.specCompleteness() < 1.0);
        assertTrue(result.missingCriticalSpecs().isEmpty()); // Has vdsMax and idMax
    }

    @Test
    @DisplayName("Should return INCOMPLETE for empty specs map")
    void testEmptySpecs() {
        String mpn = "STM32F103C8T6";
        Map<String, Object> specs = new HashMap<>();

        QualityResult result = ComponentQualityAnalyzer.calculateQuality(mpn, specs, false);

        System.out.println("Empty specs result: " + result);
        assertEquals(QualityTier.INCOMPLETE, result.tier());
        assertEquals(0, result.presentSpecs());
        assertTrue(result.expectedSpecs() > 0);
    }

    @Test
    @DisplayName("Should return unknown for null MPN")
    void testNullMpn() {
        QualityResult result = ComponentQualityAnalyzer.calculateQuality(null, Map.of(), false);

        assertEquals(QualityTier.INCOMPLETE, result.tier());
        assertEquals("UNKNOWN", result.componentType());
    }

    @Test
    @DisplayName("Should normalize source-prefixed spec names")
    void testSourcePrefixNormalization() {
        String mpn = "LM7805CT";
        Map<String, Object> specs = new HashMap<>();
        specs.put("OpenAI_output_voltage", "5.0");
        specs.put("Digikey_input_voltage_min", "7.0");
        specs.put("Mouser_input_voltage_max", "35");
        specs.put("AI_output_current", "1.5");

        QualityResult result = ComponentQualityAnalyzer.calculateQuality(mpn, specs, false);

        System.out.println("Source-prefixed specs result: " + result);
        assertTrue(result.presentSpecs() >= 4);
        assertFalse(result.missingSpecs().contains("outputVoltage"));
    }

    @Test
    @DisplayName("Should validate MPN against known patterns")
    void testMpnValidation() {
        // Known manufacturer MPNs
        assertTrue(ComponentQualityAnalyzer.isValidMpn("STM32F103C8T6"));
        assertTrue(ComponentQualityAnalyzer.isValidMpn("LM7805CT"));
        assertTrue(ComponentQualityAnalyzer.isValidMpn("IRF540N"));
        assertTrue(ComponentQualityAnalyzer.isValidMpn("GRM188R71H104KA93D"));

        // Invalid/unknown
        assertFalse(ComponentQualityAnalyzer.isValidMpn(null));
        assertFalse(ComponentQualityAnalyzer.isValidMpn(""));
        assertFalse(ComponentQualityAnalyzer.isValidMpn("   "));
    }

    @Test
    @DisplayName("Should return validation errors for missing critical specs")
    void testValidationErrors() {
        String mpn = "IRF540N";
        Map<String, Object> specs = new HashMap<>();
        // Missing critical specs: vdsMax, idMax

        ValidationResult result = ComponentQualityAnalyzer.validateSpecs(mpn, specs);

        System.out.println("Validation errors: " + result.errors());
        assertFalse(result.valid());
        assertTrue(result.hasErrors());
        assertTrue(result.errors().containsKey("vdsMax") || result.errors().containsKey("idMax"));
    }

    @Test
    @DisplayName("Should return validation success for complete specs")
    void testValidationSuccess() {
        String mpn = "CRCW060310K0FKEA";
        Map<String, Object> specs = new HashMap<>();
        specs.put("resistance", "10000");

        ValidationResult result = ComponentQualityAnalyzer.validateSpecs(mpn, specs);

        System.out.println("Validation result: " + result);
        assertTrue(result.valid());
        assertFalse(result.hasErrors());
    }

    @Test
    @DisplayName("Should get expected specs for component type")
    void testGetExpectedSpecs() {
        List<String> mosfetSpecs = ComponentQualityAnalyzer.getExpectedSpecs(ComponentType.MOSFET);

        System.out.println("MOSFET expected specs: " + mosfetSpecs);
        assertFalse(mosfetSpecs.isEmpty());
        assertTrue(mosfetSpecs.contains("vdsMax"));
        assertTrue(mosfetSpecs.contains("rdson"));
    }

    @Test
    @DisplayName("Should get critical specs for component type")
    void testGetCriticalSpecs() {
        var capacitorCritical = ComponentQualityAnalyzer.getCriticalSpecs(ComponentType.CAPACITOR);

        System.out.println("Capacitor critical specs: " + capacitorCritical);
        assertTrue(capacitorCritical.contains("capacitance"));
        assertTrue(capacitorCritical.contains("voltageRating"));
    }

    @Test
    @DisplayName("Should handle MCU with comprehensive specs")
    void testMcuQuality() {
        String mpn = "STM32F103C8T6";
        Map<String, Object> specs = new HashMap<>();
        specs.put("flashSize", "64KB");
        specs.put("clockFrequency", "72MHz");
        specs.put("ramSize", "20KB");
        specs.put("gpioCount", "37");
        specs.put("adcChannels", "10");

        QualityResult result = ComponentQualityAnalyzer.calculateQuality(mpn, specs, true);

        System.out.println("MCU quality result: " + result);
        assertEquals("MICROCONTROLLER_ST", result.componentType());
        assertTrue(result.missingCriticalSpecs().isEmpty());
        assertTrue(result.tier() == QualityTier.HIGH || result.tier() == QualityTier.MEDIUM);
    }

    @Test
    @DisplayName("Should handle capacitor quality assessment")
    void testCapacitorQuality() {
        String mpn = "GRM188R71H104KA93D";  // Murata capacitor
        Map<String, Object> specs = new HashMap<>();
        specs.put("capacitance", "100nF");
        specs.put("voltageRating", "50V");
        specs.put("tolerance", "10%");
        specs.put("esr", "0.01");

        QualityResult result = ComponentQualityAnalyzer.calculateQuality(mpn, specs, false);

        System.out.println("Capacitor quality result: " + result);
        assertTrue(result.missingCriticalSpecs().isEmpty());
        assertTrue(result.specCompleteness() > 0.3);
    }

    @Test
    @DisplayName("QualityResult helper methods should work correctly")
    void testQualityResultHelpers() {
        QualityResult highQuality = new QualityResult(
                0.9, QualityTier.HIGH, 0.85, 6, 7,
                List.of("missingSpec"), List.of(), true, "RESISTOR"
        );

        assertTrue(highQuality.isHighQuality());
        assertFalse(highQuality.needsEnrichment());
        assertFalse(highQuality.hasMissingCriticalSpecs());

        QualityResult lowQuality = new QualityResult(
                0.3, QualityTier.LOW, 0.2, 2, 10,
                List.of("spec1", "spec2"), List.of("critical1"), false, "MOSFET"
        );

        assertFalse(lowQuality.isHighQuality());
        assertTrue(lowQuality.needsEnrichment());
        assertTrue(lowQuality.hasMissingCriticalSpecs());
    }

    @Test
    @DisplayName("ValidationResult helper methods should work correctly")
    void testValidationResultHelpers() {
        ValidationResult success = ValidationResult.success();
        assertTrue(success.valid());
        assertFalse(success.hasErrors());
        assertFalse(success.hasWarnings());
        assertEquals(0, success.issueCount());

        ValidationResult withErrors = ValidationResult.withErrors(Map.of("field1", "error1"));
        assertFalse(withErrors.valid());
        assertTrue(withErrors.hasErrors());
        assertEquals(1, withErrors.issueCount());
    }
}
