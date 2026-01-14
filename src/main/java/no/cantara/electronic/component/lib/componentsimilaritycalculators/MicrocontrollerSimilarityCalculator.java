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

        // Normalize MPNs (uppercase, trim)
        String normalizedMpn1 = mpn1.toUpperCase().trim();
        String normalizedMpn2 = mpn2.toUpperCase().trim();

        // Get metadata for spec-based comparison
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.MICROCONTROLLER);
        if (metadataOpt.isPresent()) {
            logger.trace("Using metadata-driven comparison for microcontrollers");
            return calculateMetadataDrivenSimilarity(normalizedMpn1, normalizedMpn2, metadataOpt.get());
        }

        // Fallback to legacy manufacturer-based approach if metadata not available
        logger.trace("No metadata found for MICROCONTROLLER, using manufacturer registry approach");
        return calculateManufacturerBasedSimilarity(normalizedMpn1, normalizedMpn2);
    }

    /**
     * Calculate similarity using metadata-driven weighted comparison.
     * Extracts specs using manufacturer handlers and uses tolerance rules for comparison.
     */
    private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
        no.cantara.electronic.component.lib.similarity.config.SimilarityProfile profile = metadata.getDefaultProfile();

        logger.trace("Using profile: {}", profile);

        ElectronicComponentManufacturer.Registry registry = ElectronicComponentManufacturer.Registry.getInstance();
        ElectronicComponentManufacturer mfr1 = registry.fromMPN(mpn1);
        ElectronicComponentManufacturer mfr2 = registry.fromMPN(mpn2);

        // Extract specs from both MPNs using manufacturer handlers
        String series1 = mfr1.extractSeries(mpn1);
        String series2 = mfr2.extractSeries(mpn2);
        String pkg1 = mfr1.extractPackageCode(mpn1);
        String pkg2 = mfr2.extractPackageCode(mpn2);

        logger.trace("MPN1 specs: series={}, package={}", series1, pkg1);
        logger.trace("MPN2 specs: series={}, package={}", series2, pkg2);

        // Check for official replacements (CRITICAL compatibility)
        boolean isOfficialReplacement = mfr1.isOfficialReplacement(mpn1, mpn2) ||
                                        mfr2.isOfficialReplacement(mpn2, mpn1);
        if (isOfficialReplacement) {
            logger.debug("Official replacement found - HIGH similarity");
            return 0.9;  // Official replacements are highly compatible
        }

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // Compare series (HIGH importance)
        ComponentTypeMetadata.SpecConfig seriesConfig = metadata.getSpecConfig("series");
        if (seriesConfig != null && series1 != null && series2 != null) {
            double seriesSim = series1.equals(series2) ? 1.0 :
                             (mfr1 == mfr2 ? 0.7 : 0.5);  // Same series = 1.0, same mfr = 0.7, cross-mfr = 0.5
            double specWeight = profile.getEffectiveWeight(seriesConfig.getImportance());

            totalScore += seriesSim * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Series comparison: score={}, weight={}, contribution={}", seriesSim, specWeight, seriesSim * specWeight);
        }

        // Compare package (MEDIUM importance)
        ComponentTypeMetadata.SpecConfig packageConfig = metadata.getSpecConfig("package");
        if (packageConfig != null && pkg1 != null && pkg2 != null && !pkg1.isEmpty() && !pkg2.isEmpty()) {
            double pkgSim = pkg1.equals(pkg2) ? 1.0 : 0.3;  // Same package = 1.0, different = low
            double specWeight = profile.getEffectiveWeight(packageConfig.getImportance());

            totalScore += pkgSim * specWeight;
            maxPossibleScore += specWeight;
            logger.trace("Package comparison: score={}, weight={}, contribution={}", pkgSim, specWeight, pkgSim * specWeight);
        }

        // Note: flashSize, ramSize, ioCount, frequency specs are defined in metadata but not extracted yet.
        // Future enhancement: add extraction methods to manufacturer handlers for these specs.

        // Normalize to [0.0, 1.0]
        double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.5;  // Default to 0.5 if no specs compared

        logger.debug("Final similarity: {}", similarity);
        return similarity;
    }

    /**
     * Calculate similarity based on manufacturer registry, series matching, and official replacements.
     * Legacy approach used when metadata is not available.
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