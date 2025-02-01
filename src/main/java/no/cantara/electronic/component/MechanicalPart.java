package no.cantara.electronic.component;

import java.util.Map;

/**
 * Represents a mechanical part in a BOM, extending the base ElectronicPart class
 * to maintain consistency with other component types while adding mechanical-specific
 * functionality.
 */
public class MechanicalPart extends BOMEntry {

    /**
     * Common categories for mechanical parts
     */
    public enum MechanicalCategory {
        ENCLOSURE,           // Boxes, cases, housings
        HEATSINK,           // Thermal management components
        MOUNTING,           // Brackets, standoffs, clips
        HARDWARE,           // Screws, nuts, washers
        GASKET,            // Seals, O-rings
        STRUCTURAL,        // Frames, supports
        CUSTOM             // Special mechanical components
    }

    private MechanicalCategory category;

    public MechanicalPart setCategory(MechanicalCategory category) {
        this.category = category;
        return this;
    }

    public MechanicalCategory getCategory() {
        return category;
    }

    @Override
    public MechanicalPart setMpn(String mpn) {
        super.setMpn(mpn);
        return this;
    }

    @Override
    public MechanicalPart setManufacturer(String manufacturer) {
        super.setManufacturer(manufacturer);
        return this;
    }

    @Override
    public MechanicalPart setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public MechanicalPart setPkg(String pkg) {
        super.setPkg(pkg);
        return this;
    }

    @Override
    public MechanicalPart addSpec(String key, String value) {
        super.addSpec(key, value);
        return this;
    }

    @Override
    public MechanicalPart addSpecs(Map<String, String> specs) {
        super.addSpecs(specs);
        return this;
    }

    /**
     * Helper method to add multiple mechanical specifications in one call
     */
    public MechanicalPart addMechanicalSpecs(String... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Must provide key-value pairs");
        }
        for (int i = 0; i < keyValues.length; i += 2) {
            addSpec(keyValues[i], keyValues[i + 1]);
        }
        return this;
    }

    /**
     * Factory method for creating an enclosure part
     */
    public static MechanicalPart createEnclosure(String mpn, String description) {
        return new MechanicalPart()
                .setMpn(mpn)
                .setDescription(description)
                .setCategory(MechanicalCategory.ENCLOSURE);
    }

    /**
     * Factory method for creating a mounting part
     */
    public static MechanicalPart createMounting(String mpn, String description) {
        return new MechanicalPart()
                .setMpn(mpn)
                .setDescription(description)
                .setCategory(MechanicalCategory.MOUNTING);
    }

    /**
     * Factory method for creating a heatsink
     */
    public static MechanicalPart createHeatsink(String mpn, String description) {
        return new MechanicalPart()
                .setMpn(mpn)
                .setDescription(description)
                .setCategory(MechanicalCategory.HEATSINK);
    }
}