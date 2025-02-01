package no.cantara.electronic.component.advanced;

import no.cantara.electronic.component.BOM;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Cable Bill of Materials, describing cable assemblies,
 * wire harnesses, and interconnects.
 */
public class CableBOM extends BOM {
    private String id;
    private String revision;
    private String description;
    private CableType type;
    private Map<String, String> specifications = new HashMap<>();

    public enum CableType {
        POWER_CABLE,
        SIGNAL_CABLE,
        CONTROL_CABLE,
        DATA_CABLE,
        WIRE_HARNESS
    }

    // Builder-style setters for fluent API
    public CableBOM setId(String id) {
        this.id = id;
        return this;
    }

    public CableBOM setRevision(String revision) {
        this.revision = revision;
        return this;
    }

    public CableBOM setDescription(String description) {
        this.description = description;
        return this;
    }

    public CableBOM setType(CableType type) {
        this.type = type;
        return this;
    }

    public CableBOM addSpecification(String key, String value) {
        specifications.put(key, value);
        return this;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getRevision() {
        return revision;
    }

    public String getDescription() {
        return description;
    }

    public CableType getType() {
        return type;
    }

    public Map<String, String> getSpecifications() {
        return Map.copyOf(specifications);  // Return immutable copy
    }

    // Example usage in test:
    public static CableBOM createPowerCable() {
        return new CableBOM()
                .setId("CBL-PWR-001")
                .setRevision("1.0")
                .setDescription("Main Power Cable Assembly")
                .setType(CableType.POWER_CABLE)
                .addSpecification("wireGauge", "2.5mm²")
                .addSpecification("length", "500mm")
                .addSpecification("rating", "600V")
                .addSpecification("conductor", "Copper")
                .addSpecification("insulation", "PVC")
                .addSpecification("color", "Black");
    }

    public static CableBOM createSignalCable() {
        return new CableBOM()
                .setId("CBL-SIG-001")
                .setRevision("1.0")
                .setDescription("Sensor Signal Cable")
                .setType(CableType.SIGNAL_CABLE)
                .addSpecification("wireCount", "4")
                .addSpecification("shielded", "Yes")
                .addSpecification("wireGauge", "0.25mm²")
                .addSpecification("length", "300mm")
                .addSpecification("connector1", "M8 4-pin")
                .addSpecification("connector2", "JST-XH");
    }

    public static CableBOM createHarness() {
        return new CableBOM()
                .setId("CBL-HAR-001")
                .setRevision("1.0")
                .setDescription("Internal Wire Harness")
                .setType(CableType.WIRE_HARNESS)
                .addSpecification("branchCount", "3")
                .addSpecification("totalLength", "800mm")
                .addSpecification("wireTypes", "Power, Signal")
                .addSpecification("mainConnector", "Molex Mini-Fit")
                .addSpecification("branchConnectors", "JST-XH, JST-PH")
                .addSpecification("bindingType", "Spiral Wrap");
    }
}