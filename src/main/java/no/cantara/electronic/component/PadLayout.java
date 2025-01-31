package no.cantara.electronic.component;

/**
 * Represents PCB pad layout recommendations for a package.
 */
public record PadLayout(
        double padWidthMm,     // Individual pad width
        double padLengthMm,    // Individual pad length
        double padPitchMm,     // Distance between pad centers
        double landPatternMm,  // Total land pattern length
        String soldermask,     // Recommended solder mask extension
        String stencil        // Recommended stencil aperture
) {
    @Override
    public String toString() {
        return String.format("Pad: %.2f × %.2f mm, Pitch: %.2f mm, Pattern: %.2f mm",
                padWidthMm, padLengthMm, padPitchMm, landPatternMm);
    }
}