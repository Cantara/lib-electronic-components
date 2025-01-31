package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ElectronicComponentManufacturer;
import no.cantara.electronic.component.lib.PatternRegistry;

public class MicrocontrollerSimilarityCalculator implements ComponentSimilarityCalculator {
    @Override
    public boolean isApplicable(ComponentType type) {
        return type == ComponentType.MICROCONTROLLER ||
                type == ComponentType.MICROCONTROLLER_ATMEL ||
                type == ComponentType.MICROCONTROLLER_INFINEON ||
                type == ComponentType.MICROCONTROLLER_ST ||
                type == ComponentType.MCU_ATMEL;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null || patternRegistry == null) {
            return 0.0;
        }

        ElectronicComponentManufacturer.Registry registry = ElectronicComponentManufacturer.Registry.getInstance();
        ElectronicComponentManufacturer mfr1 = registry.fromMPN(mpn1);
        ElectronicComponentManufacturer mfr2 = registry.fromMPN(mpn2);

        // Extract series from both MPNs
        String series1 = mfr1.extractSeries(mpn1);
        String series2 = mfr2.extractSeries(mpn2);

        // Same manufacturer and series
        if (mfr1 == mfr2 && series1 != null && series2 != null && series1.equals(series2)) {
            String pkg1 = mfr1.extractPackageCode(mpn1);
            String pkg2 = mfr2.extractPackageCode(mpn2);

            // Same series, different package
            if (!pkg1.equals(pkg2)) {
                return 0.9;  // Very high similarity for same part, different package
            }
            return 1.0;  // Identical parts (same package)
        }

        // Check for known equivalent parts across manufacturers
        if (mfr1.isOfficialReplacement(mpn1, mpn2) || mfr2.isOfficialReplacement(mpn2, mpn1)) {
            return 0.8;  // High similarity for known equivalent parts
        }

        // Same series across manufacturers
        if (series1 != null && series2 != null && series1.equals(series2)) {
            return 0.7;  // High similarity for same series
        }

        // Both are microcontrollers but different series
        return 0.5;  // Base similarity for microcontrollers
    }
}