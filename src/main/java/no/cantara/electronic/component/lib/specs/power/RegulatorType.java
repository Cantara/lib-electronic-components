package no.cantara.electronic.component.lib.specs.power;

/**
 * Types of voltage regulators
 */
public enum RegulatorType {
    LINEAR("Linear"),
    SWITCHING_BUCK("Buck"),
    SWITCHING_BOOST("Boost"),
    SWITCHING_BUCK_BOOST("Buck-Boost"),
    SWITCHING_FLYBACK("Flyback"),
    SWITCHING_FORWARD("Forward"),
    LDO("Low Dropout"),
    SWITCHING_SEPIC("SEPIC");

    private final String description;

    RegulatorType(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}