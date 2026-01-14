package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.connectors.ConnectorHandler;
import no.cantara.electronic.component.lib.connectors.ConnectorHandlerRegistry;
import no.cantara.electronic.component.lib.connectors.TEConnectorHandler;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import no.cantara.electronic.component.lib.similarity.config.SimilarityProfile;
import no.cantara.electronic.component.lib.similarity.config.ToleranceRule;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metadata-driven similarity calculator for connector components.
 * Uses ComponentTypeMetadata for spec-based comparison (pinCount, pitch, mountingType)
 * while leveraging ConnectorHandler for manufacturer-specific extraction logic.
 */
public class ConnectorSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorSimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.5;
    private static final double LOW_SIMILARITY = 0.3;

    public ConnectorSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) {
            return false;
        }
        return type == ComponentType.CONNECTOR ||
                type == ComponentType.CONNECTOR_MOLEX ||
                type == ComponentType.CONNECTOR_TE ||
                type == ComponentType.CONNECTOR_JST ||
                type == ComponentType.CONNECTOR_HIROSE ||
                type == ComponentType.CONNECTOR_AMPHENOL ||
                type == ComponentType.CONNECTOR_HARWIN ||
                type.name().startsWith("CONNECTOR_");
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null || patternRegistry == null) {
            return 0.0;
        }

        // Get handlers for both MPNs
        ConnectorHandler handler1 = ConnectorHandlerRegistry.getInstance().getHandler(mpn1);
        ConnectorHandler handler2 = ConnectorHandlerRegistry.getInstance().getHandler(mpn2);

        if (handler1 == null || handler2 == null) {
            return 0.0;
        }

        logger.debug("Comparing connectors: {} and {}", mpn1, mpn2);
        logger.trace("Handlers: {} and {}", handler1.getClass().getSimpleName(),
                handler2.getClass().getSimpleName());

        // Check if metadata is available
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.CONNECTOR);
        if (metadataOpt.isPresent()) {
            logger.trace("Using metadata-driven similarity calculation");
            return calculateMetadataDrivenSimilarity(mpn1, mpn2, handler1, handler2, metadataOpt.get());
        }

        // Fall back to legacy logic if no metadata
        logger.trace("No metadata found, using legacy similarity calculation");
        return calculateLegacySimilarity(mpn1, mpn2, handler1, handler2);
    }

    /**
     * Metadata-driven similarity calculation using ComponentTypeMetadata specs.
     */
    private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2,
                                                      ConnectorHandler handler1, ConnectorHandler handler2,
                                                      ComponentTypeMetadata metadata) {
        SimilarityProfile profile = metadata.getDefaultProfile();

        // Different families are not compatible
        if (!handler1.getFamily().equals(handler2.getFamily())) {
            logger.debug("Different families - incompatible");
            return 0.0;
        }

        // Extract specs from both MPNs
        int pinCount1 = handler1.getPinCount(mpn1);
        int pinCount2 = handler2.getPinCount(mpn2);
        String pitch1 = handler1.getPitch(mpn1);
        String pitch2 = handler2.getPitch(mpn2);
        String mount1 = handler1.getMountingType(mpn1);
        String mount2 = handler2.getMountingType(mpn2);

        logger.trace("Specs - pinCount: {}vs{}, pitch: {}vs{}, mount: {}vs{}",
                pinCount1, pinCount2, pitch1, pitch2, mount1, mount2);

        // Special handling for terminal blocks and compatible variants
        if (handler1.areCompatible(mpn1, mpn2)) {
            logger.debug("Compatible variants detected by handler");
            // Terminal blocks from same series can have different "position" numbers
            if (handler1.getFamily().equals("Terminal Block")) {
                logger.debug("Same terminal block series - high similarity");
                return 0.8;
            }
            // Other compatible variants
            return 0.9;
        }

        // CRITICAL: Pin count must match for connectors to be compatible
        // Different pin counts means physical incompatibility, regardless of other specs
        if (pinCount1 > 0 && pinCount2 > 0 && pinCount1 != pinCount2) {
            logger.debug("Different pin counts ({} vs {}) - incompatible connectors", pinCount1, pinCount2);
            return LOW_SIMILARITY;
        }

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // 1. Pin count comparison (CRITICAL importance)
        ComponentTypeMetadata.SpecConfig pinCountConfig = metadata.getSpecConfig("pinCount");
        if (pinCountConfig != null && pinCount1 > 0 && pinCount2 > 0) {
            ToleranceRule pinCountRule = pinCountConfig.getToleranceRule();
            SpecValue<Integer> origPinCount = new SpecValue<>(pinCount1, SpecUnit.NONE);
            SpecValue<Integer> candPinCount = new SpecValue<>(pinCount2, SpecUnit.NONE);

            double pinCountScore = pinCountRule.compare(origPinCount, candPinCount);
            double pinCountWeight = profile.getEffectiveWeight(pinCountConfig.getImportance());

            logger.trace("Pin count score: {}, weight: {}", pinCountScore, pinCountWeight);
            totalScore += pinCountScore * pinCountWeight;
            maxPossibleScore += pinCountWeight;
        }

        // 2. Pitch comparison (CRITICAL importance)
        ComponentTypeMetadata.SpecConfig pitchConfig = metadata.getSpecConfig("pitch");
        if (pitchConfig != null && !pitch1.isEmpty() && !pitch2.isEmpty()) {
            ToleranceRule pitchRule = pitchConfig.getToleranceRule();
            SpecValue<String> origPitch = new SpecValue<>(pitch1, SpecUnit.NONE);
            SpecValue<String> candPitch = new SpecValue<>(pitch2, SpecUnit.NONE);

            double pitchScore = pitchRule.compare(origPitch, candPitch);
            double pitchWeight = profile.getEffectiveWeight(pitchConfig.getImportance());

            logger.trace("Pitch score: {}, weight: {}", pitchScore, pitchWeight);
            totalScore += pitchScore * pitchWeight;
            maxPossibleScore += pitchWeight;
        }

        // 3. Mounting type comparison (HIGH importance)
        ComponentTypeMetadata.SpecConfig mountConfig = metadata.getSpecConfig("mountingType");
        if (mountConfig != null && !mount1.isEmpty() && !mount2.isEmpty()) {
            ToleranceRule mountRule = mountConfig.getToleranceRule();
            SpecValue<String> origMount = new SpecValue<>(mount1, SpecUnit.NONE);
            SpecValue<String> candMount = new SpecValue<>(mount2, SpecUnit.NONE);

            double mountScore = mountRule.compare(origMount, candMount);
            double mountWeight = profile.getEffectiveWeight(mountConfig.getImportance());

            logger.trace("Mounting type score: {}, weight: {}", mountScore, mountWeight);
            totalScore += mountScore * mountWeight;
            maxPossibleScore += mountWeight;
        }

        // Normalize to [0.0, 1.0]
        double normalizedSimilarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

        logger.debug("Total score: {}, Max possible: {}, Normalized similarity: {}",
                totalScore, maxPossibleScore, normalizedSimilarity);

        return normalizedSimilarity;
    }

    /**
     * Legacy similarity calculation for backward compatibility.
     */
    private double calculateLegacySimilarity(String mpn1, String mpn2,
                                            ConnectorHandler handler1, ConnectorHandler handler2) {
        // Different families are not compatible
        if (!handler1.getFamily().equals(handler2.getFamily())) {
            logger.debug("Different families - incompatible");
            return 0.0;
        }

        // First check series match for TE connectors
        if (handler1 instanceof TEConnectorHandler) {
            TEConnectorHandler teHandler1 = (TEConnectorHandler) handler1;

            // Use the CONNECTOR_PATTERN from TEConnectorHandler
            Matcher m1 = Pattern.compile("(?:(\\d+)-)?(\\d+)-(\\d+)").matcher(mpn1);
            Matcher m2 = Pattern.compile("(?:(\\d+)-)?(\\d+)-(\\d+)").matcher(mpn2);

            if (m1.matches() && m2.matches()) {
                String series1 = m1.group(2);  // The series number
                String series2 = m2.group(2);

                if (series1 != null && series2 != null && series1.equals(series2)) {
                    logger.debug("Same TE series - high similarity");
                    return HIGH_SIMILARITY;
                }
            }
        }

        // For Würth headers, check if they're compatible variants
        if (handler1.getFamily().equals("WR-PHD")) {
            int pinCount1 = handler1.getPinCount(mpn1);
            int pinCount2 = handler2.getPinCount(mpn2);

            if (pinCount1 != pinCount2) {
                logger.debug("Different pin counts for header - low similarity");
                return LOW_SIMILARITY;
            }

            if (handler1.areCompatible(mpn1, mpn2)) {
                logger.debug("Compatible header variants");
                return HIGH_SIMILARITY;
            }
        }

        // Check pin counts
        int pinCount1 = handler1.getPinCount(mpn1);
        int pinCount2 = handler2.getPinCount(mpn2);
        logger.trace("Pin counts: {} and {}", pinCount1, pinCount2);

        // Different pin counts should have low similarity
        if (pinCount1 != pinCount2) {
            logger.debug("Different pin counts - low similarity");
            return LOW_SIMILARITY;
        }

        // Check if they're compatible variants
        if (handler1.areCompatible(mpn1, mpn2)) {
            String variant1 = handler1.getVariant(mpn1);
            String variant2 = handler2.getVariant(mpn2);

            // Same exact part
            if (variant1.equals(variant2)) {
                logger.debug("Exactly same part (1.0)");
                return 1.0;
            }

            // Different variants of same base part
            if (handler1.getFamily().equals("WR-PHD")) {
                logger.debug("Compatible header variants (0.9)");
                return HIGH_SIMILARITY;  // High similarity for header variants
            }

            // Terminal blocks from same series
            if (handler1.getFamily().equals("Terminal Block")) {
                logger.debug("Same terminal block series (0.8)");
                return 0.8;  // High similarity for terminal blocks in same series
            }

            // Default high similarity for compatible variants
            logger.debug("Compatible variants (0.8)");
            return 0.8;
        }

        // If not compatible variants, build up similarity from characteristics
        double similarity = 0.0;

        // Same pin count
        if (handler1.getPinCount(mpn1) == handler2.getPinCount(mpn2)) {
            similarity += 0.2;
            logger.trace("Same pin count (+0.2)");
        }

        // Same pitch
        String pitch1 = handler1.getPitch(mpn1);
        String pitch2 = handler2.getPitch(mpn2);
        if (!pitch1.isEmpty() && pitch1.equals(pitch2)) {
            similarity += 0.2;
            logger.trace("Same pitch (+0.2)");
        }

        // Compatible mounting types
        String mount1 = handler1.getMountingType(mpn1);
        String mount2 = handler2.getMountingType(mpn2);
        if (!mount1.isEmpty() && mount1.equals(mount2)) {
            similarity += 0.1;
            logger.trace("Compatible mounting types (+0.1)");
        }

        logger.debug("Final similarity: {}", similarity);
        return similarity;
    }
}