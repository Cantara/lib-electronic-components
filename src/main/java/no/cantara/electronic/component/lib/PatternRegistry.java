package no.cantara.electronic.component.lib;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Registry for component patterns
 */
public class PatternRegistry {
    // ComponentType -> (Class<?> handlerClass -> Set<Pattern>)
    private final Map<ComponentType, Map<Class<?>, Set<Pattern>>> patterns = new HashMap<>();
    private Class<?> currentHandlerClass;

    /**
     * Set the current handler class context
     */
    public void setCurrentHandlerClass(Class<?> handlerClass) {
        this.currentHandlerClass = handlerClass;
    }

    /**
     * Add a pattern for a component type
     */
    public void addPattern(ComponentType type, String regex) {
        if (currentHandlerClass == null) {
            // Fallback if no handler class is set
            currentHandlerClass = getCallingHandlerClass();
        }

        patterns.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(currentHandlerClass, k -> new HashSet<>())
                .add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
    }

    private Class<?> getCallingHandlerClass() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(element -> element.getClassName().contains("Handler"))
                .filter(element -> !element.getClassName().equals(getClass().getName()))
                .findFirst()
                .map(element -> {
                    try {
                        return Class.forName(element.getClassName());
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    /**
     * Check if any pattern matches for the given type (across all handlers)
     */
    public boolean matches(String mpn, ComponentType type) {
        return patterns.getOrDefault(type, Collections.emptyMap()).values().stream()
                .flatMap(Set::stream)
                .anyMatch(pattern -> pattern.matcher(mpn).matches());
    }

    /**
     * Check if any pattern matches for the given type using ONLY the current handler's patterns.
     * This should be used by the default matches() implementation to avoid cross-handler matching.
     */
    public boolean matchesForCurrentHandler(String mpn, ComponentType type) {
        if (currentHandlerClass == null) {
            return false;
        }
        Set<Pattern> handlerPatterns = getPatternsForHandler(type, currentHandlerClass);
        return handlerPatterns.stream()
                .anyMatch(pattern -> pattern.matcher(mpn).matches());
    }

    /**
     * Get patterns for a specific handler class
     */
    public Set<Pattern> getPatternsForHandler(ComponentType type, Class<?> handlerClass) {
        return patterns.getOrDefault(type, Collections.emptyMap())
                .getOrDefault(handlerClass, Collections.emptySet());
    }

    public boolean hasPattern(ComponentType type) {
        return patterns.containsKey(type) && !patterns.get(type).isEmpty();
    }

    public Set<ComponentType> getSupportedTypes() {
        return Collections.unmodifiableSet(patterns.keySet());
    }

    /**
     * Get a pattern for a component type (for backward compatibility)
     */
    public Pattern getPattern(ComponentType type) {
        // Get the first pattern found for this type
        return patterns.getOrDefault(type, Collections.emptyMap()).values().stream()
                .flatMap(Set::stream)
                .findFirst()
                .orElse(null);
    }


    /**
     * Gets the handler for a specific MPN.
     * Will try to match each handler with each component type.
     *
     * @param mpn The MPN to find a handler for
     * @return The first matching handler, or null if none found
     */
    public ManufacturerHandler getHandlerForMPN(String mpn) {
        for (Map.Entry<ComponentType, Map<Class<?>, Set<Pattern>>> typeEntry : patterns.entrySet()) {
            ComponentType type = typeEntry.getKey();
            for (Class<?> handlerClass : typeEntry.getValue().keySet()) {
                if (matches(mpn, type)) {
                    return createHandlerInstance(handlerClass);
                }
            }
        }
        return null;
    }

    /**
     * Gets all handlers registered for a specific component type.
     *
     * @param type The component type
     * @return Set of handlers for this type, empty if none found
     */
    public Set<ManufacturerHandler> getHandlersForType(ComponentType type) {
        Set<ManufacturerHandler> handlers = new HashSet<>();
        Map<Class<?>, Set<Pattern>> typeHandlers = patterns.get(type);

        if (typeHandlers != null) {
            for (Class<?> handlerClass : typeHandlers.keySet()) {
                ManufacturerHandler handler = createHandlerInstance(handlerClass);
                if (handler != null) {
                    handlers.add(handler);
                }
            }
        }

        return handlers;
    }

    /**
     * Gets all registered handlers.
     *
     * @return Set of all unique handlers
     */
    public Set<ManufacturerHandler> getAllHandlers() {
        Set<ManufacturerHandler> handlers = new HashSet<>();

        for (Map<Class<?>, Set<Pattern>> typeHandlers : patterns.values()) {
            for (Class<?> handlerClass : typeHandlers.keySet()) {
                ManufacturerHandler handler = createHandlerInstance(handlerClass);
                if (handler != null) {
                    handlers.add(handler);
                }
            }
        }

        return handlers;
    }

    private ManufacturerHandler createHandlerInstance(Class<?> handlerClass) {
        try {
            return (ManufacturerHandler) handlerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // Log error or handle appropriately
            return null;
        }
    }

    /**
     * Gets the patterns registered for a specific handler class and component type.
     *
     * @param handlerClass The handler class
     * @param type         The component type
     * @return Set of patterns, empty if none found
     */
    public Set<Pattern> getPatternsForHandler(Class<?> handlerClass, ComponentType type) {
        Map<Class<?>, Set<Pattern>> typeHandlers = patterns.get(type);
        if (typeHandlers != null) {
            return typeHandlers.getOrDefault(handlerClass, Collections.emptySet());
        }
        return Collections.emptySet();
    }

    /**
     * Gets the handler for a specific component type and MPN.
     */
    public ManufacturerHandler getHandlerForType(ComponentType type, String mpn) {
        Map<Class<?>, Set<Pattern>> typeHandlers = patterns.get(type);
        if (typeHandlers != null) {
            for (Map.Entry<Class<?>, Set<Pattern>> entry : typeHandlers.entrySet()) {
                // Check if any pattern matches
                for (Pattern pattern : entry.getValue()) {
                    if (pattern.matcher(mpn).matches()) {
                        try {
                            return (ManufacturerHandler) entry.getKey()
                                    .getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            System.err.println("Error creating handler instance: " +
                                    entry.getKey().getName());
                        }
                    }
                }
            }
        }
        return null;
    }

}