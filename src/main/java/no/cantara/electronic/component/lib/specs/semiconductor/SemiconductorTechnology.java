package no.cantara.electronic.component.lib.specs.semiconductor;

/**
 * Common characteristics for semiconductor devices.
 */
public enum SemiconductorTechnology {
    SILICON("Si", "Silicon"),
    SILICON_CARBIDE("SiC", "Silicon Carbide"),
    GALLIUM_NITRIDE("GaN", "Gallium Nitride"),
    GALLIUM_ARSENIDE("GaAs", "Gallium Arsenide");

    private final String symbol;
    private final String description;

    SemiconductorTechnology(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    public String getSymbol() { return symbol; }
    public String getDescription() { return description; }
}

