package no.cantara.electronic.component;

/**
 * Enum containing common component type patterns
 */
public enum ComponentTypePattern {
    // Memory patterns
    EEPROM("^(24|25|93|95)[0-9]+.*", ComponentType.MEMORY, "EEPROM devices"),
    FLASH("^(SST|MX|W)[0-9]+.*", ComponentType.MEMORY, "Flash memory devices"),

    // Microcontroller patterns
    AVR("^(AT)(MEGA|TINY)[0-9]+.*", ComponentType.MICROCONTROLLER, "AVR microcontrollers"),
    PIC("^PIC[0-9]+.*", ComponentType.MICROCONTROLLER, "PIC microcontrollers"),

    // Interface IC patterns
    UART_BRIDGE("^(FT|CP)[0-9]+.*", ComponentType.IC, "UART bridge controllers"),
    USB_CONTROLLER("^CH[0-9]+.*", ComponentType.IC, "USB controllers"),

    // Analog IC patterns
    OPAMP("^(LM|TL|NE|UA|AD)[0-9]+.*", ComponentType.OPAMP, "Operational amplifiers"),
    ADC("^(AD|MCP)[0-9]+.*", ComponentType.IC, "Analog-to-Digital converters"),

    // Voltage regulator patterns
    LINEAR_REGULATOR("^(LM|MC|L|LD)[0-9]+.*", ComponentType.VOLTAGE_REGULATOR, "Linear voltage regulators"),
    SWITCHING_REGULATOR("^(LT|MC|IR)[0-9]+.*", ComponentType.VOLTAGE_REGULATOR, "Switching voltage regulators");

    private final String regex;
    private final ComponentType type;
    private final String description;

    ComponentTypePattern(String regex, ComponentType type, String description) {
        this.regex = regex;
        this.type = type;
        this.description = description;
    }

    public String getRegex() {
        return regex;
    }

    public ComponentType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean matches(String mpn) {
        return mpn != null && mpn.toUpperCase().matches(regex);
    }
}