package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.ElectronicPart;
import no.cantara.electronic.component.lib.componentsimilaritycalculators.*;
import no.cantara.electronic.component.lib.manufacturers.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class for MPN (Manufacturer Part Number) operations
 */
public class MPNUtils {
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

        System.out.println("\nChecking MPN: " + mpn + " against type: " + type);

        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
        System.out.println("Found manufacturer: " + manufacturer.getName());

        // If it's a manufacturer-specific type
        if (type.name().contains("_")) {
            boolean matches = manufacturer.matchesPattern(mpn, type);
            System.out.println("Pattern match result: " + matches);
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

        System.out.println("Calculating similarity between " + mpn1 + " and " + mpn2);

        // Exactly the same MPN
        if (mpn1.equals(mpn2)) {
            return 1.0;
        }

        ComponentType type1 = ComponentTypeDetector.determineComponentType(mpn1);
        ComponentType type2 = ComponentTypeDetector.determineComponentType(mpn2);

        System.out.println("Types: " + type1 + " and " + type2);

        // If either type is null, return 0 similarity
        if (type1 == null || type2 == null) {
            return 0.0;
        }

        // Create pattern registry
        PatternRegistry patternRegistry = new PatternRegistry();

        // Try specialized calculators
        for (ComponentSimilarityCalculator calculator : calculators) {
            System.out.println("Trying calculator: " + calculator.getClass().getSimpleName());
            boolean applicable1 = calculator.isApplicable(type1);
            boolean applicable2 = calculator.isApplicable(type2);
            System.out.println("  isApplicable for type1: " + applicable1);
            System.out.println("  isApplicable for type2: " + applicable2);

            if (applicable1 || applicable2) {
                double similarity = calculator.calculateSimilarity(mpn1, mpn2, patternRegistry);
                System.out.println("Similarity: " + similarity);
                if (similarity > 0) {
                    System.out.println("  Returning similarity: " + similarity);
                    return similarity;
                }
            }
        }
        System.out.println("  Returning calculateDefaultSimilarity: ");

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

        System.out.println("\nLooking for handler for MPN: " + mpn);

        // Try each component type
        for (ComponentType type : ComponentType.values()) {
            if (registry.matches(mpn, type)) {
                System.out.println("Found match for type: " + type);
                // Get all handlers for this type
                Set<ManufacturerHandler> handlers = ManufacturerHandlerFactory.getHandlers();
                for (ManufacturerHandler handler : handlers) {
                    if (handler.getSupportedTypes().contains(type)) {
                        System.out.println("Testing handler: " + handler.getClass().getSimpleName());
                        // Initialize patterns for this handler
                        registry.setCurrentHandlerClass(handler.getClass());
                        handler.initializePatterns(registry);
                        if (handler.matches(mpn, type, registry)) {
                            System.out.println("Found matching handler: " + handler.getClass().getSimpleName());
                            return handler;
                        }
                    }
                }
            }
        }

        System.out.println("No handler found for MPN: " + mpn);
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

        System.out.println("\nExtracting package code for MPN: " + mpn);
        ManufacturerHandler handler = getManufacturerHandler(mpn);

        if (handler != null) {
            System.out.println("Using handler: " + handler.getClass().getSimpleName());
            try {
                String packageCode = handler.extractPackageCode(mpn);
                System.out.println("Extracted package code: '" + packageCode + "'");
                return packageCode;
            } catch (Exception e) {
                System.err.println("Error extracting package code: " + e.getMessage());
                e.printStackTrace();
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

        System.out.println("\nSearching for MPN in text: " + text);
        String[] words = text.trim().split("\\s+|[;,|]");

        // Remove common prefixes and suffixes
        words = cleanWords(words);

        // Try each word with each handler
        Set<ManufacturerHandler> handlers = ManufacturerHandlerFactory.getHandlers();
        for (String word : words) {
            // First try manufacturer-specific types
            for (ManufacturerHandler handler : handlers) {
                for (ComponentType type : handler.getSupportedTypes()) {
                    if (handler.matches(word, type, registry)) {
                        System.out.println("Found match using " + handler.getClass().getSimpleName() +
                                " for type " + type + ": " + word);
                        return word;
                    }
                }
            }

            // Then try generic component types
            for (ComponentType type : ComponentType.values()) {
                if (registry.matches(word, type)) {
                    System.out.println("Found match for type " + type + ": " + word);
                    return word;
                }
            }
        }

        System.out.println("No MPN found in text");
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
     *
     * @param mpn The manufacturer part number
     * @return The component type, or null if not determined
     */
    public static ComponentType getComponentType(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return null;
        }

        System.out.println("\nDetermining component type for MPN: " + mpn);

        // First try manufacturer-specific types
        Set<ManufacturerHandler> handlers = ManufacturerHandlerFactory.getHandlers();
        for (ManufacturerHandler handler : handlers) {
            for (ComponentType type : handler.getSupportedTypes()) {
                if (handler.matches(mpn, type, registry)) {
                    System.out.println("Found match using " + handler.getClass().getSimpleName() +
                            " for type: " + type);
                    return type;
                }
            }
        }

        // Then try generic component types
        for (ComponentType type : ComponentType.values()) {
            if (!type.name().contains("_") && registry.matches(mpn, type)) {
                System.out.println("Found match for generic type: " + type);
                return type;
            }
        }

        System.out.println("No component type found for MPN: " + mpn);
        return null;
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