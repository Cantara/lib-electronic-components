package no.cantara.electronic.component;

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
     * Check if any pattern matches for the given type
     */
    public boolean matches(String mpn, ComponentType type) {
        return patterns.getOrDefault(type, Collections.emptyMap()).values().stream()
                .flatMap(Set::stream)
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
}