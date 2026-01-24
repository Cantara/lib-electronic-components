package no.cantara.electronic.component.lib;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MPN package suffix handling methods in MPNUtils.
 *
 * These tests verify that package suffixes are correctly stripped,
 * search variations are generated, and component equivalence is properly detected.
 */
class MPNPackageSuffixTest {

    // ========== stripPackageSuffix() tests ==========

    @Test
    void testStripPackageSuffix_MaximPlus() {
        assertEquals("MAX3483EESA", MPNUtils.stripPackageSuffix("MAX3483EESA+"));
        assertEquals("MAX485ESA", MPNUtils.stripPackageSuffix("MAX485ESA+"));
    }

    @Test
    void testStripPackageSuffix_LinearTechPBF() {
        assertEquals("LTC2053HMS8", MPNUtils.stripPackageSuffix("LTC2053HMS8#PBF"));
        assertEquals("LT1117CST", MPNUtils.stripPackageSuffix("LT1117CST#PBF"));
    }

    @Test
    void testStripPackageSuffix_LinearTechTR() {
        assertEquals("LT1117CST", MPNUtils.stripPackageSuffix("LT1117CST#TR"));
        assertEquals("LTC2053HMS8", MPNUtils.stripPackageSuffix("LTC2053HMS8#TRPBF"));
    }

    @Test
    void testStripPackageSuffix_NXPOrdering() {
        assertEquals("TJA1050T", MPNUtils.stripPackageSuffix("TJA1050T/CM,118"));
        assertEquals("MCP2551-I", MPNUtils.stripPackageSuffix("MCP2551-I/SN"));
    }

    @Test
    void testStripPackageSuffix_CommaDelimited() {
        assertEquals("NC7WZ04", MPNUtils.stripPackageSuffix("NC7WZ04,315"));
    }

    @Test
    void testStripPackageSuffix_NoSuffix() {
        assertEquals("ADS1115IDGSR", MPNUtils.stripPackageSuffix("ADS1115IDGSR"));
        assertEquals("STM32F103C8T6", MPNUtils.stripPackageSuffix("STM32F103C8T6"));
        assertEquals("LM358N", MPNUtils.stripPackageSuffix("LM358N"));
    }

    @Test
    void testStripPackageSuffix_EdgeCases() {
        assertEquals("", MPNUtils.stripPackageSuffix(null));
        assertEquals("", MPNUtils.stripPackageSuffix(""));
        assertEquals("", MPNUtils.stripPackageSuffix("   "));
        assertEquals("TEST", MPNUtils.stripPackageSuffix("  TEST  "));
    }

    @Test
    void testStripPackageSuffix_SingleCharacter() {
        // Single character after base - should not strip (could be part of MPN)
        assertEquals("NC7WZ04G", MPNUtils.stripPackageSuffix("NC7WZ04G"));
    }

    // ========== getSearchVariations() tests ==========

    @Test
    void testGetSearchVariations_WithSuffix() {
        List<String> variations = MPNUtils.getSearchVariations("MAX3483EESA+");
        assertEquals(2, variations.size());
        assertTrue(variations.contains("MAX3483EESA+"));
        assertTrue(variations.contains("MAX3483EESA"));
    }

    @Test
    void testGetSearchVariations_WithHashSuffix() {
        List<String> variations = MPNUtils.getSearchVariations("LTC2053HMS8#PBF");
        assertEquals(2, variations.size());
        assertTrue(variations.contains("LTC2053HMS8#PBF"));
        assertTrue(variations.contains("LTC2053HMS8"));
    }

    @Test
    void testGetSearchVariations_WithSlashSuffix() {
        List<String> variations = MPNUtils.getSearchVariations("TJA1050T/CM,118");
        assertEquals(2, variations.size());
        assertTrue(variations.contains("TJA1050T/CM,118"));
        assertTrue(variations.contains("TJA1050T"));
    }

    @Test
    void testGetSearchVariations_NoSuffix() {
        List<String> variations = MPNUtils.getSearchVariations("ADS1115");
        assertEquals(1, variations.size());
        assertTrue(variations.contains("ADS1115"));
    }

    @Test
    void testGetSearchVariations_EdgeCases() {
        assertTrue(MPNUtils.getSearchVariations(null).isEmpty());
        assertTrue(MPNUtils.getSearchVariations("").isEmpty());
        assertTrue(MPNUtils.getSearchVariations("   ").isEmpty());
    }

    @Test
    void testGetSearchVariations_OrderPreservation() {
        // Original should always be first
        List<String> variations = MPNUtils.getSearchVariations("MAX3483EESA+");
        assertEquals("MAX3483EESA+", variations.get(0));
        assertEquals("MAX3483EESA", variations.get(1));
    }

    // ========== isEquivalentMPN() tests ==========

    @Test
    void testIsEquivalentMPN_SamePartDifferentPackage() {
        assertTrue(MPNUtils.isEquivalentMPN("MAX3483EESA+", "MAX3483EESA"));
        assertTrue(MPNUtils.isEquivalentMPN("MAX3483EESA", "MAX3483EESA+"));
    }

    @Test
    void testIsEquivalentMPN_SameBaseDifferentSuffixes() {
        assertTrue(MPNUtils.isEquivalentMPN("LTC2053HMS8#PBF", "LTC2053HMS8#TR"));
        assertTrue(MPNUtils.isEquivalentMPN("LTC2053HMS8#PBF", "LTC2053HMS8#TRPBF"));
    }

    @Test
    void testIsEquivalentMPN_SamePartNoSuffix() {
        assertTrue(MPNUtils.isEquivalentMPN("ADS1115", "ADS1115"));
        assertTrue(MPNUtils.isEquivalentMPN("STM32F103C8T6", "STM32F103C8T6"));
    }

    @Test
    void testIsEquivalentMPN_DifferentParts() {
        assertFalse(MPNUtils.isEquivalentMPN("NC7WZ485M8X", "NC7WZ240"));
        assertFalse(MPNUtils.isEquivalentMPN("MAX3483EESA", "MAX3485EESA"));
        assertFalse(MPNUtils.isEquivalentMPN("LM358", "LM324"));
    }

    @Test
    void testIsEquivalentMPN_CaseInsensitive() {
        assertTrue(MPNUtils.isEquivalentMPN("max3483eesa+", "MAX3483EESA"));
        assertTrue(MPNUtils.isEquivalentMPN("MAX3483EESA+", "max3483eesa"));
    }

    @Test
    void testIsEquivalentMPN_EdgeCases() {
        assertFalse(MPNUtils.isEquivalentMPN(null, "MAX3483EESA"));
        assertFalse(MPNUtils.isEquivalentMPN("MAX3483EESA", null));
        assertFalse(MPNUtils.isEquivalentMPN(null, null));
        assertFalse(MPNUtils.isEquivalentMPN("", ""));
        assertFalse(MPNUtils.isEquivalentMPN("  ", "  "));
    }

    @Test
    void testIsEquivalentMPN_ProductionScenarios() {
        // Real-world examples from production logs
        assertTrue(MPNUtils.isEquivalentMPN("TJA1050T/CM,118", "TJA1050T"));
        assertTrue(MPNUtils.isEquivalentMPN("LT1117CST#TR", "LT1117CST#PBF"));
        assertFalse(MPNUtils.isEquivalentMPN("TXT315AT", "TPS51125A"));
    }

    // ========== getPackageSuffix() tests ==========

    @Test
    void testGetPackageSuffix_Plus() {
        assertEquals(Optional.of("+"), MPNUtils.getPackageSuffix("MAX3483EESA+"));
    }

    @Test
    void testGetPackageSuffix_Hash() {
        assertEquals(Optional.of("#PBF"), MPNUtils.getPackageSuffix("LTC2053HMS8#PBF"));
        assertEquals(Optional.of("#TR"), MPNUtils.getPackageSuffix("LT1117CST#TR"));
        assertEquals(Optional.of("#TRPBF"), MPNUtils.getPackageSuffix("LTC2053HMS8#TRPBF"));
    }

    @Test
    void testGetPackageSuffix_Slash() {
        assertEquals(Optional.of("/CM,118"), MPNUtils.getPackageSuffix("TJA1050T/CM,118"));
        assertEquals(Optional.of("/SN"), MPNUtils.getPackageSuffix("MCP2551-I/SN"));
    }

    @Test
    void testGetPackageSuffix_Comma() {
        assertEquals(Optional.of(",315"), MPNUtils.getPackageSuffix("NC7WZ04,315"));
    }

    @Test
    void testGetPackageSuffix_NoSuffix() {
        assertEquals(Optional.empty(), MPNUtils.getPackageSuffix("ADS1115"));
        assertEquals(Optional.empty(), MPNUtils.getPackageSuffix("STM32F103C8T6"));
    }

    @Test
    void testGetPackageSuffix_EdgeCases() {
        assertEquals(Optional.empty(), MPNUtils.getPackageSuffix(null));
        assertEquals(Optional.empty(), MPNUtils.getPackageSuffix(""));
        assertEquals(Optional.empty(), MPNUtils.getPackageSuffix("   "));
    }

    @Test
    void testGetPackageSuffix_WithWhitespace() {
        assertEquals(Optional.of("+"), MPNUtils.getPackageSuffix("  MAX3483EESA+  "));
    }

    // ========== Integration tests ==========

    @Test
    void testIntegration_RealWorldDatasheetSearch() {
        // Simulates datasheet search scenario from AI service
        String mpn = "MAX3483EESA+";
        List<String> variations = MPNUtils.getSearchVariations(mpn);

        // Should try both variations for datasheet search
        assertEquals(2, variations.size());

        // Each variation should be valid for search
        for (String variation : variations) {
            assertNotNull(variation);
            assertFalse(variation.isEmpty());
        }
    }

    @Test
    void testIntegration_BOMValidation() {
        // Simulates BOM validation scenario
        String bomMPN = "LTC2053HMS8#PBF";  // From design
        String supplierMPN = "LTC2053HMS8#TR";  // From supplier

        // Should recognize as equivalent despite different packaging
        assertTrue(MPNUtils.isEquivalentMPN(bomMPN, supplierMPN));
    }

    @Test
    void testIntegration_ComponentDeduplication() {
        // Simulates preventing duplicate components in database
        String existing = "TJA1050T/CM,118";
        String incoming = "TJA1050T";

        // Should recognize as duplicate
        assertTrue(MPNUtils.isEquivalentMPN(existing, incoming));
    }

    @Test
    void testIntegration_PreservesNonSuffixedMPNs() {
        // Important: Don't break existing MPNs without suffixes
        String mpn = "STM32F103C8T6";

        assertEquals(mpn, MPNUtils.stripPackageSuffix(mpn));
        assertEquals(1, MPNUtils.getSearchVariations(mpn).size());
        assertTrue(MPNUtils.isEquivalentMPN(mpn, mpn));
        assertEquals(Optional.empty(), MPNUtils.getPackageSuffix(mpn));
    }
}
