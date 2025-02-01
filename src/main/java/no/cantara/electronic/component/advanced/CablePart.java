package no.cantara.electronic.component.advanced;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.ElectronicPart;

import java.util.Map;

/**
 * Represents a cable or wire harness component in a BOM, extending the base ElectronicPart
 * class to maintain consistency while adding cable-specific functionality.
 */
public class CablePart extends BOMEntry {

    /**
     * Common categories for cable parts
     */
    public enum CableCategory {
        POWER_CABLE,        // Main power distribution
        SIGNAL_CABLE,       // Signal/data transmission
        CONTROL_CABLE,      // Control signals
        WIRE_HARNESS,       // Multiple wire assemblies
        RIBBON_CABLE,       // Flat ribbon cables
        COAXIAL_CABLE,      // RF/high-frequency
        FIBER_OPTIC        // Optical cables
    }

    private CableCategory category;

    public CablePart setCategory(CableCategory category) {
        this.category = category;
        return this;
    }

    public CableCategory getCategory() {
        return category;
    }

    @Override
    public CablePart setMpn(String mpn) {
        super.setMpn(mpn);
        return this;
    }

    @Override
    public CablePart setManufacturer(String manufacturer) {
        super.setManufacturer(manufacturer);
        return this;
    }

    @Override
    public CablePart setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public CablePart setPkg(String pkg) {
        super.setPkg(pkg);
        return this;
    }

    @Override
    public CablePart addSpec(String key, String value) {
        super.addSpec(key, value);
        return this;
    }

    @Override
    public CablePart addSpecs(Map<String, String> specs) {
        super.addSpecs(specs);
        return this;
    }

    /**
     * Helper method to add multiple cable specifications in one call
     */
    public CablePart addCableSpecs(String... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Must provide key-value pairs");
        }
        for (int i = 0; i < keyValues.length; i += 2) {
            addSpec(keyValues[i], keyValues[i + 1]);
        }
        return this;
    }

    /**
     * Factory method for creating a power cable
     */
    public static CablePart createPowerCable(String mpn, String description) {
        return new CablePart()
                .setMpn(mpn)
                .setDescription(description)
                .setCategory(CableCategory.POWER_CABLE);
    }

    /**
     * Factory method for creating a signal cable
     */
    public static CablePart createSignalCable(String mpn, String description) {
        return new CablePart()
                .setMpn(mpn)
                .setDescription(description)
                .setCategory(CableCategory.SIGNAL_CABLE);
    }

    /**
     * Factory method for creating a wire harness
     */
    public static CablePart createWireHarness(String mpn, String description) {
        return new CablePart()
                .setMpn(mpn)
                .setDescription(description)
                .setCategory(CableCategory.WIRE_HARNESS);
    }

    /**
     * Factory method for creating a control cable
     */
    public static CablePart createControlCable(String mpn, String description) {
        return new CablePart()
                .setMpn(mpn)
                .setDescription(description)
                .setCategory(CableCategory.CONTROL_CABLE);
    }
}