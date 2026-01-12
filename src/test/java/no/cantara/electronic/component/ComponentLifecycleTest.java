package no.cantara.electronic.component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Component Lifecycle tracking functionality.
 */
class ComponentLifecycleTest {

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Test
    @DisplayName("ComponentLifecycleStatus enum has correct properties")
    void lifecycleStatusEnumProperties() {
        // Test ACTIVE status
        assertEquals("Active", ComponentLifecycleStatus.ACTIVE.getDisplayName());
        assertTrue(ComponentLifecycleStatus.ACTIVE.isAvailable());
        assertFalse(ComponentLifecycleStatus.ACTIVE.shouldAvoidForNewDesigns());
        assertFalse(ComponentLifecycleStatus.ACTIVE.requiresAttention());

        // Test NRFND status
        assertEquals("NRFND", ComponentLifecycleStatus.NRFND.getDisplayName());
        assertTrue(ComponentLifecycleStatus.NRFND.isAvailable());
        assertTrue(ComponentLifecycleStatus.NRFND.shouldAvoidForNewDesigns());
        assertFalse(ComponentLifecycleStatus.NRFND.requiresAttention());

        // Test LAST_TIME_BUY status
        assertEquals("Last Time Buy", ComponentLifecycleStatus.LAST_TIME_BUY.getDisplayName());
        assertTrue(ComponentLifecycleStatus.LAST_TIME_BUY.isAvailable());
        assertTrue(ComponentLifecycleStatus.LAST_TIME_BUY.shouldAvoidForNewDesigns());
        assertTrue(ComponentLifecycleStatus.LAST_TIME_BUY.requiresAttention());

        // Test OBSOLETE status
        assertEquals("Obsolete", ComponentLifecycleStatus.OBSOLETE.getDisplayName());
        assertFalse(ComponentLifecycleStatus.OBSOLETE.isAvailable());
        assertTrue(ComponentLifecycleStatus.OBSOLETE.shouldAvoidForNewDesigns());
        assertTrue(ComponentLifecycleStatus.OBSOLETE.requiresAttention());

        // Test EOL status
        assertEquals("End of Life", ComponentLifecycleStatus.EOL.getDisplayName());
        assertFalse(ComponentLifecycleStatus.EOL.isAvailable());
        assertTrue(ComponentLifecycleStatus.EOL.shouldAvoidForNewDesigns());
        assertTrue(ComponentLifecycleStatus.EOL.requiresAttention());

        // Test UNKNOWN status
        assertEquals("Unknown", ComponentLifecycleStatus.UNKNOWN.getDisplayName());
        assertFalse(ComponentLifecycleStatus.UNKNOWN.isAvailable());
        assertFalse(ComponentLifecycleStatus.UNKNOWN.shouldAvoidForNewDesigns());
        assertFalse(ComponentLifecycleStatus.UNKNOWN.requiresAttention());
    }

    @Test
    @DisplayName("Create active component lifecycle")
    void createActiveLifecycle() {
        ComponentLifecycle lifecycle = ComponentLifecycle.active();

        assertEquals(ComponentLifecycleStatus.ACTIVE, lifecycle.getStatus());
        assertNotNull(lifecycle.getStatusDate());
        assertTrue(lifecycle.isAvailable());
        assertFalse(lifecycle.shouldAvoidForNewDesigns());
        assertFalse(lifecycle.requiresAttention());
    }

    @Test
    @DisplayName("Create obsolete lifecycle with replacement")
    void createObsoleteWithReplacement() {
        ComponentLifecycle lifecycle = ComponentLifecycle.obsoleteWithReplacement(
                "NEW-MPN-001",
                "Texas Instruments"
        );

        assertEquals(ComponentLifecycleStatus.OBSOLETE, lifecycle.getStatus());
        assertFalse(lifecycle.isAvailable());
        assertTrue(lifecycle.requiresAttention());

        List<ComponentLifecycle.ReplacementPart> replacements = lifecycle.getReplacementParts();
        assertEquals(1, replacements.size());

        ComponentLifecycle.ReplacementPart replacement = replacements.get(0);
        assertEquals("NEW-MPN-001", replacement.getMpn());
        assertEquals("Texas Instruments", replacement.getManufacturer());
        assertEquals(ComponentLifecycle.ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION,
                     replacement.getCompatibility());
    }

    @Test
    @DisplayName("Create Last Time Buy lifecycle with deadline")
    void createLastTimeBuyLifecycle() {
        LocalDate ltbDate = LocalDate.now().plusMonths(3);
        ComponentLifecycle lifecycle = ComponentLifecycle.lastTimeBuy(ltbDate);

        assertEquals(ComponentLifecycleStatus.LAST_TIME_BUY, lifecycle.getStatus());
        assertEquals(ltbDate, lifecycle.getLastTimeBuyDate());
        assertTrue(lifecycle.isAvailable());
        assertTrue(lifecycle.requiresAttention());
    }

    @Test
    @DisplayName("LTB deadline detection")
    void ltbDeadlineDetection() {
        // LTB in 30 days
        ComponentLifecycle lifecycle = ComponentLifecycle.lastTimeBuy(
                LocalDate.now().plusDays(30)
        );

        assertTrue(lifecycle.isLtbApproaching(60));  // Within 60 days threshold
        assertTrue(lifecycle.isLtbApproaching(30));  // Exactly at threshold
        assertFalse(lifecycle.isLtbApproaching(15)); // Outside threshold
        assertFalse(lifecycle.isLtbExpired());

        // Expired LTB
        ComponentLifecycle expiredLifecycle = new ComponentLifecycle(ComponentLifecycleStatus.LAST_TIME_BUY);
        expiredLifecycle.setLastTimeBuyDate(LocalDate.now().minusDays(10));
        assertTrue(expiredLifecycle.isLtbExpired());
    }

    @Test
    @DisplayName("Add multiple replacement parts")
    void addMultipleReplacementParts() {
        ComponentLifecycle lifecycle = new ComponentLifecycle(ComponentLifecycleStatus.OBSOLETE);

        lifecycle.addReplacementPart("DROP-IN-001", "Same Corp",
                ComponentLifecycle.ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION);
        lifecycle.addReplacementPart("FUNC-EQ-002", "Other Corp",
                ComponentLifecycle.ReplacementPart.CompatibilityLevel.FUNCTIONAL_EQUIVALENT);
        lifecycle.addReplacementPart("UPGRADE-003", "Premium Corp",
                ComponentLifecycle.ReplacementPart.CompatibilityLevel.UPGRADE);

        assertEquals(3, lifecycle.getReplacementParts().size());

        // Test primary replacement
        ComponentLifecycle.ReplacementPart primary = lifecycle.getPrimaryReplacement();
        assertNotNull(primary);
        assertEquals("DROP-IN-001", primary.getMpn());

        // Test filter by compatibility
        List<ComponentLifecycle.ReplacementPart> dropIns = lifecycle.getReplacementsByCompatibility(
                ComponentLifecycle.ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION);
        assertEquals(1, dropIns.size());
        assertEquals("DROP-IN-001", dropIns.get(0).getMpn());
    }

    @Test
    @DisplayName("Status change is recorded in history")
    void statusChangeRecordedInHistory() {
        ComponentLifecycle lifecycle = ComponentLifecycle.active();
        assertTrue(lifecycle.getStatusHistory().isEmpty());

        // Change to NRFND
        lifecycle.setStatus(ComponentLifecycleStatus.NRFND);
        assertEquals(1, lifecycle.getStatusHistory().size());

        ComponentLifecycle.StatusChange change = lifecycle.getStatusHistory().get(0);
        assertEquals(ComponentLifecycleStatus.ACTIVE, change.getFromStatus());
        assertEquals(ComponentLifecycleStatus.NRFND, change.getToStatus());
        assertEquals(LocalDate.now(), change.getChangeDate());

        // Change to LTB
        lifecycle.setStatus(ComponentLifecycleStatus.LAST_TIME_BUY);
        assertEquals(2, lifecycle.getStatusHistory().size());
    }

    @Test
    @DisplayName("ElectronicPart with lifecycle")
    void electronicPartWithLifecycle() {
        ElectronicPart part = new ElectronicPart()
                .setMpn("TPS62130RGTR")
                .setManufacturer("Texas Instruments")
                .setDescription("3A Step-Down Converter")
                .setPkg("QFN-16")
                .setLifecycle(ComponentLifecycle.active());

        assertTrue(part.hasLifecycleInfo());
        assertFalse(part.isLifecycleAtRisk());
        assertEquals(ComponentLifecycleStatus.ACTIVE, part.getLifecycleStatus());
    }

    @Test
    @DisplayName("ElectronicPart without lifecycle returns UNKNOWN status")
    void electronicPartWithoutLifecycle() {
        ElectronicPart part = new ElectronicPart()
                .setMpn("TEST-001")
                .setManufacturer("Test");

        assertFalse(part.hasLifecycleInfo());
        assertFalse(part.isLifecycleAtRisk());
        assertEquals(ComponentLifecycleStatus.UNKNOWN, part.getLifecycleStatus());
    }

    @Test
    @DisplayName("ElectronicPart with obsolete lifecycle is at risk")
    void electronicPartWithObsoleteLifecycle() {
        ElectronicPart part = new ElectronicPart()
                .setMpn("OLD-PART-001")
                .setManufacturer("Legacy Corp")
                .setLifecycle(ComponentLifecycle.obsoleteWithReplacement(
                        "NEW-PART-001", "Modern Corp"));

        assertTrue(part.hasLifecycleInfo());
        assertTrue(part.isLifecycleAtRisk());
        assertEquals(ComponentLifecycleStatus.OBSOLETE, part.getLifecycleStatus());
    }

    @Test
    @DisplayName("BOMEntry inherits lifecycle functionality")
    void bomEntryWithLifecycle() {
        BOMEntry entry = new BOMEntry()
                .setMpn("LM7805CT")
                .setManufacturer("Texas Instruments")
                .setDescription("5V Linear Regulator")
                .setPkg("TO-220")
                .setQty("5")
                .setLifecycle(ComponentLifecycle.lastTimeBuy(LocalDate.now().plusMonths(6)));

        assertTrue(entry.hasLifecycleInfo());
        assertTrue(entry.isLifecycleAtRisk());
        assertEquals(ComponentLifecycleStatus.LAST_TIME_BUY, entry.getLifecycleStatus());
    }

    @Test
    @DisplayName("Serialize and deserialize lifecycle")
    void serializeAndDeserializeLifecycle() throws Exception {
        ComponentLifecycle original = new ComponentLifecycle(ComponentLifecycleStatus.NRFND);
        original.setLastTimeBuyDate(LocalDate.of(2024, 12, 31));
        original.setEndOfLifeDate(LocalDate.of(2025, 6, 30));
        original.setSource("Manufacturer PCN");
        original.setNotes("Replaced by next generation product");
        original.addReplacementPart("NEXT-GEN-001", "Same Corp",
                ComponentLifecycle.ReplacementPart.CompatibilityLevel.UPGRADE);

        String json = mapper.writeValueAsString(original);
        System.out.println("Serialized Lifecycle:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(original));

        ComponentLifecycle deserialized = mapper.readValue(json, ComponentLifecycle.class);

        assertEquals(original.getStatus(), deserialized.getStatus());
        assertEquals(original.getLastTimeBuyDate(), deserialized.getLastTimeBuyDate());
        assertEquals(original.getEndOfLifeDate(), deserialized.getEndOfLifeDate());
        assertEquals(original.getSource(), deserialized.getSource());
        assertEquals(original.getNotes(), deserialized.getNotes());
        assertEquals(1, deserialized.getReplacementParts().size());

        ComponentLifecycle.ReplacementPart replacement = deserialized.getReplacementParts().get(0);
        assertEquals("NEXT-GEN-001", replacement.getMpn());
        assertEquals("Same Corp", replacement.getManufacturer());
        assertEquals(ComponentLifecycle.ReplacementPart.CompatibilityLevel.UPGRADE,
                     replacement.getCompatibility());
    }

    @Test
    @DisplayName("Serialize and deserialize ElectronicPart with lifecycle")
    void serializeElectronicPartWithLifecycle() throws Exception {
        ElectronicPart original = new ElectronicPart()
                .setMpn("STM32F407VGT6")
                .setManufacturer("STMicroelectronics")
                .setDescription("ARM Cortex-M4 MCU")
                .setPkg("LQFP-100")
                .setLifecycle(ComponentLifecycle.active());

        String json = mapper.writeValueAsString(original);
        System.out.println("Serialized ElectronicPart with Lifecycle:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(original));

        ElectronicPart deserialized = mapper.readValue(json, ElectronicPart.class);

        assertEquals(original.getMpn(), deserialized.getMpn());
        assertEquals(original.getManufacturer(), deserialized.getManufacturer());
        assertNotNull(deserialized.getLifecycle());
        assertEquals(ComponentLifecycleStatus.ACTIVE, deserialized.getLifecycleStatus());
    }

    @Test
    @DisplayName("Serialize and deserialize BOMEntry with lifecycle")
    void serializeBOMEntryWithLifecycle() throws Exception {
        LocalDate ltbDate = LocalDate.of(2024, 9, 30);

        BOMEntry original = new BOMEntry()
                .setMpn("MC34063ADR")
                .setManufacturer("Texas Instruments")
                .setDescription("DC-DC Converter")
                .setPkg("SOIC-8")
                .setQty("10")
                .setLifecycle(ComponentLifecycle.lastTimeBuy(ltbDate));

        original.getDesignators().add("U1");
        original.getDesignators().add("U2");

        String json = mapper.writeValueAsString(original);
        System.out.println("Serialized BOMEntry with Lifecycle:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(original));

        BOMEntry deserialized = mapper.readValue(json, BOMEntry.class);

        assertEquals(original.getMpn(), deserialized.getMpn());
        assertEquals(original.getQty(), deserialized.getQty());
        assertEquals(2, deserialized.getDesignators().size());
        assertNotNull(deserialized.getLifecycle());
        assertEquals(ComponentLifecycleStatus.LAST_TIME_BUY, deserialized.getLifecycleStatus());
        assertEquals(ltbDate, deserialized.getLifecycle().getLastTimeBuyDate());
    }

    @Test
    @DisplayName("Replacement part with notes")
    void replacementPartWithNotes() {
        ComponentLifecycle.ReplacementPart replacement = new ComponentLifecycle.ReplacementPart()
                .setMpn("NEW-001")
                .setManufacturer("Better Corp")
                .setCompatibility(ComponentLifecycle.ReplacementPart.CompatibilityLevel.FUNCTIONAL_EQUIVALENT)
                .setNotes("Requires minor layout change for different pinout");

        assertEquals("NEW-001", replacement.getMpn());
        assertEquals("Better Corp", replacement.getManufacturer());
        assertEquals(ComponentLifecycle.ReplacementPart.CompatibilityLevel.FUNCTIONAL_EQUIVALENT,
                     replacement.getCompatibility());
        assertTrue(replacement.getNotes().contains("pinout"));
    }

    @Test
    @DisplayName("All compatibility levels are defined")
    void allCompatibilityLevelsExist() {
        ComponentLifecycle.ReplacementPart.CompatibilityLevel[] levels =
                ComponentLifecycle.ReplacementPart.CompatibilityLevel.values();

        assertEquals(5, levels.length);
        assertNotNull(ComponentLifecycle.ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION);
        assertNotNull(ComponentLifecycle.ReplacementPart.CompatibilityLevel.FUNCTIONAL_EQUIVALENT);
        assertNotNull(ComponentLifecycle.ReplacementPart.CompatibilityLevel.SIMILAR);
        assertNotNull(ComponentLifecycle.ReplacementPart.CompatibilityLevel.CROSS_REFERENCE);
        assertNotNull(ComponentLifecycle.ReplacementPart.CompatibilityLevel.UPGRADE);

        // Each should have descriptions
        for (ComponentLifecycle.ReplacementPart.CompatibilityLevel level : levels) {
            assertNotNull(level.name());
        }
    }

    @Test
    @DisplayName("Empty lifecycle has no primary replacement")
    void emptyLifecycleNoPrimaryReplacement() {
        ComponentLifecycle lifecycle = ComponentLifecycle.active();
        assertNull(lifecycle.getPrimaryReplacement());
        assertTrue(lifecycle.getReplacementParts().isEmpty());
    }

    @Test
    @DisplayName("Lifecycle with null LTB date does not report as approaching")
    void nullLtbDateNotApproaching() {
        ComponentLifecycle lifecycle = ComponentLifecycle.active();
        assertFalse(lifecycle.isLtbApproaching(90));
        assertFalse(lifecycle.isLtbExpired());
    }
}
