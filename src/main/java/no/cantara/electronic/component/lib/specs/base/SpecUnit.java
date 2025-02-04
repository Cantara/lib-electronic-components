package no.cantara.electronic.component.lib.specs.base;

/**
 * Enumeration of units used in component specifications.
 */
public enum SpecUnit {
    // Electrical units - Voltage
    VOLTS("V", "Voltage"),
    MILLIVOLTS("mV", "Millivolts"),
    MICROVOLTS("µV", "Microvolts"),
    NANOVOLTS("nV", "Nanovolts"),

    // Electrical units - Current
    AMPS("A", "Current"),
    MILLIAMPS("mA", "Milliamps"),
    MICROAMPS("µA", "Microamps"),
    NANOAMPS("nA", "Nanoamps"),

    // Electrical units - Power
    WATTS("W", "Power"),
    MILLIWATTS("mW", "Milliwatts"),
    MICROWATTS("µW", "Microwatts"),

    // Electrical units - Resistance
    OHMS("Ω", "Resistance"),
    KILOOHMS("kΩ", "Kiloohms"),
    MEGAOHMS("MΩ", "Megaohms"),

    // Electrical units - Capacitance
    FARADS("F", "Capacitance"),
    MICROFARADS("µF", "Microfarads"),
    NANOFARADS("nF", "Nanofarads"),
    PICOFARADS("pF", "Picofarads"),

    // Electrical units - Inductance
    HENRIES("H", "Inductance"),
    MILLIHENRIES("mH", "Millihenries"),
    MICROHENRIES("µH", "Microhenries"),

    // Frequency units
    HERTZ("Hz", "Frequency"),
    KILOHERTZ("kHz", "Kilohertz"),
    MEGAHERTZ("MHz", "Megahertz"),
    GIGAHERTZ("GHz", "Gigahertz"),

    // Time units
    HOURS("h", "Hours"),
    MINUTES("min", "Minutes"),
    SECONDS("s", "Seconds"),
    MILLISECONDS("ms", "Milliseconds"),
    MICROSECONDS("µs", "Microseconds"),
    NANOSECONDS("ns", "Nanoseconds"),
    PICOSECONDS("ps", "Picoseconds"),

    // Data/Memory units
    BITS("bit", "Bits"),
    BYTES("B", "Bytes"),
    KILOBYTES("KB", "Kilobytes"),
    MEGABYTES("MB", "Megabytes"),

    // Temperature units
    CELSIUS("°C", "Celsius"),
    KELVIN("K", "Kelvin"),

    // Physical dimensions
    METERS("m", "Meters"),
    MILLIMETERS("mm", "Millimeters"),
    MICROMETERS("µm", "Micrometers"),
    INCHES("in", "Inches"),

    // Charge units
    COULOMBS("C", "Coulombs"),
    MILLICOULOMBS("mC", "Millicoulombs"),
    MICROCOULOMBS("µC", "Microcoulombs"),
    NANOCOULOMBS("nC", "Nanocoulombs"),

    // Rate units
    VOLTS_PER_MICROSECOND("V/µs", "Volts per Microsecond"),
    VOLTS_PER_MILLISECOND("V/ms", "Volts per Millisecond"),
    AMPS_PER_SECOND("A/s", "Amps per Second"),

    // Other
    PERCENTAGE("%", "Percentage"),
    PPM("ppm", "Parts Per Million"),
    COUNT("pcs", "Count"),
    DEGREES("°", "Degrees"),
    RADIANS("rad", "Radians"),
    DECIBELS("dB", "Decibels"),
    NONE("", "No Unit");

    private final String symbol;
    private final String description;

    SpecUnit(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Formats a value with this unit.
     *
     * @param value The value to format
     * @return The formatted string with the unit symbol
     */
    public String format(Number value) {
        if (this == NONE) {
            return String.valueOf(value);
        }
        return value + getSymbol();
    }

    /**
     * Gets the base unit for a given unit (e.g., returns VOLTS for MILLIVOLTS)
     *
     * @return The base unit
     */
    public SpecUnit getBaseUnit() {
        return switch (this) {
            case MILLIVOLTS, MICROVOLTS, NANOVOLTS -> VOLTS;
            case MILLIAMPS, MICROAMPS, NANOAMPS -> AMPS;
            case MILLIWATTS, MICROWATTS -> WATTS;
            case KILOOHMS, MEGAOHMS -> OHMS;
            case MICROFARADS, NANOFARADS, PICOFARADS -> FARADS;
            case MILLIHENRIES, MICROHENRIES -> HENRIES;
            case KILOHERTZ, MEGAHERTZ, GIGAHERTZ -> HERTZ;
            case MILLISECONDS, MICROSECONDS, NANOSECONDS, PICOSECONDS -> SECONDS;
            case KILOBYTES, MEGABYTES -> BYTES;
            case MILLICOULOMBS, MICROCOULOMBS, NANOCOULOMBS -> COULOMBS;
            default -> this;
        };
    }

    /**
     * Attempts to find a SpecUnit from a string representation.
     *
     * @param text The text to parse
     * @return The matching SpecUnit, or NONE if no match
     */
    public static SpecUnit fromString(String text) {
        for (SpecUnit unit : values()) {
            if (unit.symbol.equalsIgnoreCase(text) ||
                    unit.name().equalsIgnoreCase(text) ||
                    unit.description.equalsIgnoreCase(text)) {
                return unit;
            }
        }
        return NONE;
    }
}