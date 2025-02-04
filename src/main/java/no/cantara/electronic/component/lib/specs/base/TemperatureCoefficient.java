package no.cantara.electronic.component.lib.specs.base;

/**
 * Temperature coefficient categories for passive components.
 */
public enum TemperatureCoefficient {
    // For Resistors
    TCR_25("±25 ppm/°C", 25),
    TCR_50("±50 ppm/°C", 50),
    TCR_100("±100 ppm/°C", 100),
    TCR_200("±200 ppm/°C", 200),

    // For Capacitors (Common X7R, X5R, etc.)
    X7R("X7R", -55, 125, 0.15),  // ±15% over temp range
    X5R("X5R", -55, 85, 0.15),
    X8R("X8R", -55, 150, 0.15),
    C0G_NP0("C0G/NP0", -55, 125, 0.003);  // ±0.3% over temp range

    private final String code;
    private final int minTemp;
    private final int maxTemp;
    private final double maxDeviation;

    TemperatureCoefficient(String code, double ppm) {
        this(code, -55, 125, ppm / 1000000.0);
    }

    TemperatureCoefficient(String code, int minTemp, int maxTemp, double maxDeviation) {
        this.code = code;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.maxDeviation = maxDeviation;
    }

    public String getCode() { return code; }
    public int getMinTemp() { return minTemp; }
    public int getMaxTemp() { return maxTemp; }
    public double getMaxDeviation() { return maxDeviation; }
}