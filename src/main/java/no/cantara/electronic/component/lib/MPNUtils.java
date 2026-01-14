package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.ElectronicPart;
import no.cantara.electronic.component.lib.componentsimilaritycalculators.*;
import no.cantara.electronic.component.lib.manufacturers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class for MPN (Manufacturer Part Number) operations
 */
public class MPNUtils {
    private static final Logger logger = LoggerFactory.getLogger(MPNUtils.class);
    private static final Pattern SPECIAL_CHARS = Pattern.compile("[^a-zA-Z0-9]");
    private static final PatternRegistry registry;

    static {
        // Initialize registry with handlers in a deterministic order
        registry = new PatternRegistry();
        List<ManufacturerHandler> handlers = new ArrayList<>(ManufacturerHandlerFactory.initializeHandlers());
        handlers.sort(Comparator.comparing(h -> h.getClass().getName()));
        for (ManufacturerHandler handler : handlers) {
            registry.setCurrentHandlerClass(handler.getClass());
            handler.initializePatterns(registry);
        }
    }







    private static final List<ComponentSimilarityCalculator> calculators = Arrays.asList(
            new VoltageRegulatorSimilarityCalculator(),
            new LEDSimilarityCalculator(),
            new OpAmpSimilarityCalculator(),
            new LogicICSimilarityCalculator(),
            new MemorySimilarityCalculator(),
            new DiodeSimilarityCalculator(),
            new SensorSimilarityCalculator(),
            new MosfetSimilarityCalculator(),
            new TransistorSimilarityCalculator(),
            new MicrocontrollerSimilarityCalculator(),
            new ResistorSimilarityCalculator(),
            new CapacitorSimilarityCalculator(),
            new ConnectorSimilarityCalculator()
    );

    /**
     * Normalize an MPN by removing special characters and converting to uppercase
     */
    public static String normalize(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return "";
        }
        return SPECIAL_CHARS.matcher(mpn.trim().toUpperCase()).replaceAll("");
    }

    /**
     * Check if an MPN matches a specific component type.
     */
    public static boolean matchesType(String mpn, ComponentType type) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        logger.trace("Checking MPN: {} against type: {}", mpn, type);

        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
        logger.trace("Found manufacturer: {}", manufacturer.getName());

        // If it's a manufacturer-specific type
        if (type.name().contains("_")) {
            boolean matches = manufacturer.matchesPattern(mpn, type);
            logger.trace("Pattern match result: {}", matches);
            return matches;
        }

        // For base types, check if any manufacturer matches
        ComponentManufacturer[] manufacturers = ComponentManufacturer.getManufacturersForType(type);
        for (ComponentManufacturer mfr : manufacturers) {
            if (mfr.matchesPattern(mpn, type)) {
                return true;
            }
        }

        // If no specific matches, check if it matches the base type
        ComponentType detectedType = ComponentTypeDetector.determineComponentType(mpn);
        return detectedType != null && type == detectedType.getBaseType();
    }

    /**
     * Calculate similarity between two MPNs
     */
    public static double calculateSimilarity(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return 0.0;
        }

        logger.debug("Calculating similarity between {} and {}", mpn1, mpn2);

        // Exactly the same MPN
        if (mpn1.equals(mpn2)) {
            return 1.0;
        }

        ComponentType type1 = ComponentTypeDetector.determineComponentType(mpn1);
        ComponentType type2 = ComponentTypeDetector.determineComponentType(mpn2);

        logger.debug("Types: {} and {}", type1, type2);

        // If either type is null, return 0 similarity
        if (type1 == null || type2 == null) {
            return 0.0;
        }

        // Create pattern registry
        PatternRegistry patternRegistry = new PatternRegistry();

        // Try specialized calculators
        for (ComponentSimilarityCalculator calculator : calculators) {
            logger.trace("Trying calculator: {}", calculator.getClass().getSimpleName());
            boolean applicable1 = calculator.isApplicable(type1);
            boolean applicable2 = calculator.isApplicable(type2);
            logger.trace("  isApplicable for type1: {}", applicable1);
            logger.trace("  isApplicable for type2: {}", applicable2);

            if (applicable1 || applicable2) {
                double similarity = calculator.calculateSimilarity(mpn1, mpn2, patternRegistry);
                logger.trace("Similarity: {}", similarity);
                // Trust the calculator's result, even if it's 0.0 (incompatible parts)
                logger.debug("  Returning similarity: {}", similarity);
                return similarity;
            }
        }
        logger.trace("  Returning calculateDefaultSimilarity");

        // Default similarity calculation
        return calculateDefaultSimilarity(mpn1, mpn2, type1, type2);
    }

    private static double calculateDefaultSimilarity(String mpn1, String mpn2,
                                                     ComponentType type1, ComponentType type2) {
        double similarity = 0.0;

        // Same base type
        if (type1 != null && type2 != null && type1.getBaseType() == type2.getBaseType()) {
            similarity += 0.4;
        }

        // Get manufacturers
        ComponentManufacturer mfr1 = ComponentManufacturer.fromMPN(mpn1);
        ComponentManufacturer mfr2 = ComponentManufacturer.fromMPN(mpn2);

        // Same manufacturer
        if (mfr1 == mfr2 && mfr1 != ComponentManufacturer.UNKNOWN) {
            similarity += 0.3;

            // Compare series
            String series1 = mfr1.extractSeries(mpn1);
            String series2 = mfr2.extractSeries(mpn2);
            if (series1 != null && series2 != null && series1.equals(series2)) {
                similarity += 0.2;
            }
        }

        return Math.min(similarity, 1.0);
    }



    /**
     * Check if an MPN is from a specific manufacturer
     */
    public static boolean isFromManufacturer(String mpn, ComponentManufacturer expectedMfr) {
        if (mpn == null || expectedMfr == null) {
            return false;
        }
        return ComponentManufacturer.fromMPN(normalize(mpn)) == expectedMfr;
    }

    /**
     * Get the manufacturer for an MPN
     */
    public static ComponentManufacturer getManufacturer(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return ComponentManufacturer.UNKNOWN;
        }
        return ComponentManufacturer.fromMPN(normalize(mpn));
    }

    /**
     * Checks if an MPN represents a MOSFET.
     */
    public static boolean isMosfet(String mpn) {
        ComponentType type = ComponentTypeDetector.determineComponentType(mpn);
        return type != null && type.getBaseType() == ComponentType.MOSFET;
    }

    /**
     * Checks if a BOM object represents a MOSFET.
     */
    public static boolean isMosfet(ElectronicPart electronicPart) {
        if (electronicPart == null || electronicPart.getMpn()==null) {
            return false;
        }
        return isMosfet(electronicPart.getMpn());
    }

    /**
     * Checks if an MPN represents an analog IC.
     */
    public static boolean isAnalogIC(String mpn) {
        ComponentType type = ComponentTypeDetector.determineComponentType(mpn);
        if (type == null) return false;

        // Check for analog-specific types
        return type == ComponentType.OPAMP ||
                type == ComponentType.VOLTAGE_REGULATOR ||
                type.getBaseType() == ComponentType.OPAMP ||
                type.getBaseType() == ComponentType.VOLTAGE_REGULATOR;
    }

    /**
     * Checks if an MPN represents a digital IC.
     */
    public static boolean isDigitalIC(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        // Check for common digital IC patterns
        if (mpn.startsWith("74")) return true;
        if (mpn.startsWith("CD4")) return true;

        ComponentType type = ComponentTypeDetector.determineComponentType(mpn);
        return type != null && type == ComponentType.IC && !isAnalogIC(mpn);
    }

    /**
     * Gets the manufacturer handler that matches this MPN.
     */
    public static ManufacturerHandler getManufacturerHandler(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return null;
        }

        logger.debug("Looking for handler for MPN: {}", mpn);

        // Try each component type
        for (ComponentType type : ComponentType.values()) {
            if (registry.matches(mpn, type)) {
                logger.trace("Found match for type: {}", type);
                // Get all handlers for this type
                Set<ManufacturerHandler> handlers = ManufacturerHandlerFactory.getHandlers();
                for (ManufacturerHandler handler : handlers) {
                    if (handler.getSupportedTypes().contains(type)) {
                        logger.trace("Testing handler: {}", handler.getClass().getSimpleName());
                        // Initialize patterns for this handler
                        registry.setCurrentHandlerClass(handler.getClass());
                        if (handler.matches(mpn, type, registry)) {
                            logger.debug("Found matching handler: {}", handler.getClass().getSimpleName());
                            return handler;
                        }
                    }
                }
            }
        }

        logger.debug("No handler found for MPN: {}", mpn);
        return null;
    }



    /**
     * Attempts to extract the package code from an MPN.
     *
     * @param mpn The manufacturer part number
     * @return The package code if found, null otherwise
     */
    public static String getPackageCode(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return "";
        }

        logger.debug("Extracting package code for MPN: {}", mpn);
        ManufacturerHandler handler = getManufacturerHandler(mpn);

        if (handler != null) {
            logger.debug("Using handler: {}", handler.getClass().getSimpleName());
            try {
                String packageCode = handler.extractPackageCode(mpn);
                logger.debug("Extracted package code: '{}'", packageCode);
                return packageCode;
            } catch (Exception e) {
                logger.error("Error extracting package code from MPN: {}", mpn, e);
            }
        }

        return "";
    }


    /**
     * Gets all handlers that support a specific component type.
     *
     * @param type The component type
     * @return Set of handlers supporting this type
     */
    public static Set<ManufacturerHandler> getHandlersForType(ComponentType type) {
        return registry.getHandlersForType(type);
    }

    /**
     * Gets all registered handlers.
     *
     * @return Set of all handlers
     */
    public static Set<ManufacturerHandler> getAllHandlers() {
        return registry.getAllHandlers();
    }


    /**
     * Attempts to find an MPN in the given text.
     *
     * @param text The text to search for an MPN
     * @return The found MPN, or null if none found
     */
    public static String findMPNInText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        logger.debug("Searching for MPN in text: {}", text);
        String[] words = text.trim().split("\\s+|[;,|]");

        // Remove common prefixes and suffixes
        words = cleanWords(words);

        logger.trace("Cleaned words: {}", Arrays.toString(words));

        // Try each word with each handler
        Set<ManufacturerHandler> handlers = ManufacturerHandlerFactory.getHandlers();
        logger.trace("Number of handlers: {}", handlers.size());

        for (String word : words) {
            logger.trace("Trying word: {}", word);

            // First try manufacturer-specific types
            for (ManufacturerHandler handler : handlers) {
                logger.trace("Checking handler: {}", handler.getClass().getSimpleName());
                for (ComponentType type : handler.getSupportedTypes()) {
                    logger.trace("  Checking type: {}", type);
                    if (handler.matches(word, type, registry)) {
                        logger.debug("Found match using {} for type {}: {}",
                                handler.getClass().getSimpleName(), type, word);
                        return word;
                    }
                }
            }

            // Then try generic component types
            logger.trace("Trying generic types for word: {}", word);
            for (ComponentType type : ComponentType.values()) {
                if (!type.name().contains("_") && registry.matches(word, type)) {
                    logger.debug("Found match for generic type {}: {}", type, word);
                    return word;
                }
            }
        }

        logger.debug("No MPN found in text");
        return null;
    }


    /**
     * Clean words by removing common prefixes and suffixes.
     */
    private static String[] cleanWords(String[] words) {
        String[] result = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim().toUpperCase();

            // Handle key-value pairs with equals sign
            if (word.contains("=")) {
                word = word.split("=")[1];
            }

            // Remove common prefixes
            String[] prefixes = {
                    "IC-", "IC ",
                    "PART-", "PART ",
                    "MPN-", "MPN:",
                    "PN:", "P/N:",
                    "REF:", "REF-",
                    "ITEM:", "ITEM-"
            };
            for (String prefix : prefixes) {
                if (word.startsWith(prefix)) {
                    word = word.substring(prefix.length());
                }
            }

            // Remove common suffixes that aren't part of the MPN
            String[] suffixes = {
                    "-SMD", "-THT",
                    " SMD", " THT",
                    "-ROHS", " ROHS"
            };
            for (String suffix : suffixes) {
                if (word.endsWith(suffix)) {
                    word = word.substring(0, word.length() - suffix.length());
                }
            }

            result[i] = word;
        }
        return result;
    }

    /**
     * Gets the component type for an MPN by checking all handlers.
     * Returns the most specific matching type (manufacturer-specific types before base types).
     *
     * @param mpn The manufacturer part number
     * @return The component type, or null if not determined
     */
    public static ComponentType getComponentType(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return null;
        }

        logger.debug("Determining component type for MPN: {}", mpn);

        // Collect all matching types, then return the most specific one
        ComponentType mostSpecificType = null;
        int mostSpecificScore = -1;

        Set<ManufacturerHandler> handlers = ManufacturerHandlerFactory.getHandlers();
        for (ManufacturerHandler handler : handlers) {
            for (ComponentType type : handler.getSupportedTypes()) {
                if (handler.matches(mpn, type, registry)) {
                    int score = getTypeSpecificityScore(type);
                    logger.trace("Found match using {} for type: {} (score: {})",
                            handler.getClass().getSimpleName(), type, score);
                    if (score > mostSpecificScore) {
                        mostSpecificScore = score;
                        mostSpecificType = type;
                    }
                }
            }
        }

        if (mostSpecificType != null) {
            logger.debug("Returning most specific type: {}", mostSpecificType);
            return mostSpecificType;
        }

        // Then try generic component types
        for (ComponentType type : ComponentType.values()) {
            if (!type.name().contains("_") && registry.matches(mpn, type)) {
                logger.debug("Found match for generic type: {}", type);
                return type;
            }
        }

        logger.debug("No component type found for MPN: {}", mpn);
        return null;
    }

    /**
     * Calculate a specificity score for a component type.
     * Higher scores are more specific types.
     * Manufacturer-specific types (containing "_") are more specific than base types.
     * Types like OPAMP are more specific than IC or ANALOG_IC.
     */
    private static int getTypeSpecificityScore(ComponentType type) {
        String name = type.name();
        int score = 0;

        // Manufacturer-specific types get highest base score
        if (name.contains("_")) {
            score += 100;
        }

        // Penalize very generic types
        if (name.equals("IC")) {
            score -= 50;
        } else if (name.equals("ANALOG_IC") || name.equals("DIGITAL_IC")) {
            score -= 40;
        }

        // Bonus for specific functional types
        if (name.contains("OPAMP") || name.contains("VOLTAGE_REGULATOR") ||
                name.contains("TEMPERATURE_SENSOR") || name.contains("LED") ||
                name.contains("MICROCONTROLLER") || name.contains("TRANSISTOR") ||
                name.contains("MOSFET") || name.contains("DIODE")) {
            score += 50;
        }

        return score;
    }

    /**
     * Gets all matching component types for an MPN.
     *
     * @param mpn The manufacturer part number
     * @return Set of matching component types, empty if none found
     */
    public static Set<ComponentType> getMatchingTypes(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return Collections.emptySet();
        }

        Set<ComponentType> matchingTypes = new HashSet<>();

        // Check manufacturer-specific types
        Set<ManufacturerHandler> handlers = ManufacturerHandlerFactory.getHandlers();
        for (ManufacturerHandler handler : handlers) {
            for (ComponentType type : handler.getSupportedTypes()) {
                if (handler.matches(mpn, type, registry)) {
                    matchingTypes.add(type);
                }
            }
        }

        // Check generic types
        for (ComponentType type : ComponentType.values()) {
            if (!type.name().contains("_") && registry.matches(mpn, type)) {
                matchingTypes.add(type);
            }
        }

        return matchingTypes;
    }

}