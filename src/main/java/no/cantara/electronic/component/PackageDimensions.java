package no.cantara.electronic.component;

/**
 * Represents physical package dimensions and characteristics.
 */
public record PackageDimensions(
        double lengthMm,      // Length in millimeters
        double widthMm,       // Width in millimeters
        double heightMm,      // Height in millimeters
        double pitchMm,       // Pin pitch in millimeters
        int pinCount,         // Number of pins/leads
        String footprint      // Recommended PCB footprint name
) {
    @Override
    public String toString() {
        return String.format("%.2f × %.2f × %.2f mm (pitch: %.2f mm, pins: %d)",
                lengthMm, widthMm, heightMm, pitchMm, pinCount);
    }
}