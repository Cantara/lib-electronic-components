package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.connectors.ConnectorHandler;
import no.cantara.electronic.component.lib.connectors.ConnectorHandlerRegistry;
import no.cantara.electronic.component.lib.connectors.TEConnectorHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectorSimilarityCalculator implements ComponentSimilarityCalculator {

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.5;
    private static final double LOW_SIMILARITY = 0.3;

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

        System.out.println("Comparing connectors: " + mpn1 + " and " + mpn2);
        System.out.println("Handlers: " + handler1.getClass().getSimpleName() +
                " and " + handler2.getClass().getSimpleName());

        // Different families are not compatible
        if (!handler1.getFamily().equals(handler2.getFamily())) {
            System.out.println("Different families - incompatible");
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
                    System.out.println("Same TE series - high similarity");
                    return HIGH_SIMILARITY;
                }
            }
        }

        // For Würth headers, check if they're compatible variants
        if (handler1.getFamily().equals("WR-PHD")) {
            int pinCount1 = handler1.getPinCount(mpn1);
            int pinCount2 = handler2.getPinCount(mpn2);

            if (pinCount1 != pinCount2) {
                System.out.println("Different pin counts for header - low similarity");
                return LOW_SIMILARITY;
            }

            if (handler1.areCompatible(mpn1, mpn2)) {
                System.out.println("Compatible header variants");
                return HIGH_SIMILARITY;
            }
        }

        // Check pin counts
        int pinCount1 = handler1.getPinCount(mpn1);
        int pinCount2 = handler2.getPinCount(mpn2);
        System.out.println("Pin counts: " + pinCount1 + " and " + pinCount2);

        // Different pin counts should have low similarity
        if (pinCount1 != pinCount2) {
            System.out.println("Different pin counts - low similarity");
            return LOW_SIMILARITY;
        }

        // Check if they're compatible variants
        if (handler1.areCompatible(mpn1, mpn2)) {
            String variant1 = handler1.getVariant(mpn1);
            String variant2 = handler2.getVariant(mpn2);

            // Same exact part
            if (variant1.equals(variant2)) {
                System.out.println("Exactly same part (1.0)");
                return 1.0;
            }

            // Different variants of same base part
            if (handler1.getFamily().equals("WR-PHD")) {
                System.out.println("Compatible header variants (0.9)");
                return HIGH_SIMILARITY;  // High similarity for header variants
            }

            // Terminal blocks from same series
            if (handler1.getFamily().equals("Terminal Block")) {
                System.out.println("Same terminal block series (0.8)");
                return 0.8;  // High similarity for terminal blocks in same series
            }

            // Default high similarity for compatible variants
            System.out.println("Compatible variants (0.8)");
            return 0.8;
        }

        // If not compatible variants, build up similarity from characteristics
        double similarity = 0.0;

        // Same pin count
        if (handler1.getPinCount(mpn1) == handler2.getPinCount(mpn2)) {
            similarity += 0.2;
            System.out.println("Same pin count (+0.2)");
        }

        // Same pitch
        String pitch1 = handler1.getPitch(mpn1);
        String pitch2 = handler2.getPitch(mpn2);
        if (!pitch1.isEmpty() && pitch1.equals(pitch2)) {
            similarity += 0.2;
            System.out.println("Same pitch (+0.2)");
        }

        // Compatible mounting types
        String mount1 = handler1.getMountingType(mpn1);
        String mount2 = handler2.getMountingType(mpn2);
        if (!mount1.isEmpty() && mount1.equals(mount2)) {
            similarity += 0.1;
            System.out.println("Compatible mounting types (+0.1)");
        }

        System.out.println("Final similarity: " + similarity);
        return similarity;
    }
}