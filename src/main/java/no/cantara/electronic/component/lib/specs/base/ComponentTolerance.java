package no.cantara.electronic.component.lib.specs.base;

/**
 * Represents the tolerance ranges for passive components.
 */
public enum ComponentTolerance {
    ULTRA_PRECISION("0.1%", 0.001),
    PRECISION("1%", 0.01),
    GENERAL_PURPOSE("5%", 0.05),
    LOW_PRECISION("10%", 0.1),
    VERY_LOW_PRECISION("20%", 0.2);

    private final String display;
    private final double value;

    ComponentTolerance(String display, double value) {
        this.display = display;
        this.value = value;
    }

    public String getDisplay() { return display; }
    public double getValue() { return value; }
}