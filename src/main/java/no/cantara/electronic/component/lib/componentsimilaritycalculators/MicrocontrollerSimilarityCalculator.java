package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ElectronicComponentManufacturer;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Similarity calculator for microcontroller components.
 *
 * Uses manufacturer registry for series/package extraction and replacement tracking.
 * Metadata infrastructure available for spec-based comparison when characteristics are known.
 */
public class MicrocontrollerSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MicrocontrollerSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    public MicrocontrollerSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

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

        logger.debug("Comparing microcontrollers: {} vs {}", mpn1, mpn2);

        // Check if metadata is available (for future spec-based enhancement)
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.MICROCONTROLLER);
        if (metadataOpt.isEmpty()) {
            logger.trace("No metadata found for MICROCONTROLLER, using manufacturer registry approach");
        }

        // Microcontroller comparison primarily uses manufacturer registry approach
        return calculateManufacturerBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity based on manufacturer registry, series matching, and official replacements.
     * This is the primary microcontroller comparison method.
     */
    private double calculateManufacturerBasedSimilarity(String mpn1, String mpn2) {
        ElectronicComponentManufacturer.Registry registry = ElectronicComponentManufacturer.Registry.getInstance();
        ElectronicComponentManufacturer mfr1 = registry.fromMPN(mpn1);
        ElectronicComponentManufacturer mfr2 = registry.fromMPN(mpn2);

        // Extract series from both MPNs
        String series1 = mfr1.extractSeries(mpn1);
        String series2 = mfr2.extractSeries(mpn2);

        logger.trace("Manufacturers: {} and {}", mfr1, mfr2);
        logger.trace("Series: {} and {}", series1, series2);

        // Same manufacturer and series
        if (mfr1 == mfr2 && series1 != null && series2 != null && series1.equals(series2)) {
            String pkg1 = mfr1.extractPackageCode(mpn1);
            String pkg2 = mfr2.extractPackageCode(mpn2);

            // Same series, different package
            if (!pkg1.equals(pkg2)) {
                logger.debug("Same series, different package - similarity: 0.9");
                return 0.9;  // Very high similarity for same part, different package
            }
            logger.debug("Identical parts - similarity: 1.0");
            return 1.0;  // Identical parts (same package)
        }

        // Check for known equivalent parts across manufacturers
        if (mfr1.isOfficialReplacement(mpn1, mpn2) || mfr2.isOfficialReplacement(mpn2, mpn1)) {
            logger.debug("Official replacement - similarity: 0.8");
            return 0.8;  // High similarity for known equivalent parts
        }

        // Same series across manufacturers
        if (series1 != null && series2 != null && series1.equals(series2)) {
            logger.debug("Same series across manufacturers - similarity: 0.7");
            return 0.7;  // High similarity for same series
        }

        // Both are microcontrollers but different series
        logger.debug("Different microcontrollers - similarity: 0.5");
        return 0.5;  // Base similarity for microcontrollers
    }
}