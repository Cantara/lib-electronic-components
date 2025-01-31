package no.cantara.electronic.component.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Registry for component type detection.
 * Delegates to ComponentManufacturer for manufacturer-specific patterns.
 */
public class ComponentTypeRegistry {
    private final Map<ComponentType, Map<String, Pattern>> fallbackPatterns;

    public ComponentTypeRegistry() {
        this.fallbackPatterns = new HashMap<>();
        initializeFallbackPatterns();
    }

    /**
     * Initialize fallback patterns for cases where manufacturer cannot be determined
     */
    private void initializeFallbackPatterns() {
        // These patterns are only used when manufacturer detection fails
        addFallbackPattern(ComponentType.RESISTOR, "^R[0-9].*");
        addFallbackPattern(ComponentType.CAPACITOR, "^C[0-9].*");
        addFallbackPattern(ComponentType.INDUCTOR, "^L[0-9].*");
        addFallbackPattern(ComponentType.DIODE, "^D[0-9].*");
        addFallbackPattern(ComponentType.LED, "^LED.*");
        addFallbackPattern(ComponentType.CRYSTAL, "^X[0-9].*");
        addFallbackPattern(ComponentType.IC, "^IC[0-9].*");
    }

    private void addFallbackPattern(ComponentType type, String regex) {
        fallbackPatterns.computeIfAbsent(type, k -> new HashMap<>())
                .put(regex, Pattern.compile(regex));
    }

    /**
     * Determine component type from MPN
     */
    public ComponentType determineComponentType(String mpn1, String mpn2) {
        ComponentType type1 = determineType(mpn1);
        ComponentType type2 = determineType(mpn2);

        // If both types are from the same family, use that type
        if (type1.isSameFamily(type2)) {
            // Prefer the more specific type if available
            return type1 == ComponentType.GENERIC ? type2 : type1;
        }

        // If types are different, use GENERIC
        return ComponentType.GENERIC;
    }

    /**
     * Determine type for a single MPN
     */
    public ComponentType determineType(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return ComponentType.GENERIC;
        }

        String normalizedMpn = MPNUtils.normalize(mpn);

        // First try manufacturer detection
        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(normalizedMpn);
        if (manufacturer != ComponentManufacturer.UNKNOWN) {
            // Try all component types with this manufacturer
            for (ComponentType type : ComponentType.values()) {
                if (manufacturer.matchesPattern(normalizedMpn, type)) {
                    return type;
                }
            }
        }

        // If no manufacturer match, try fallback patterns
        return detectTypeFromFallbackPatterns(normalizedMpn);
    }

    private ComponentType detectTypeFromFallbackPatterns(String mpn) {
        for (Map.Entry<ComponentType, Map<String, Pattern>> entry : fallbackPatterns.entrySet()) {
            for (Pattern pattern : entry.getValue().values()) {
                if (pattern.matcher(mpn).matches()) {
                    return entry.getKey();
                }
            }
        }
        return ComponentType.GENERIC;
    }

    /**
     * Check if MPN matches a specific component type
     */
    public boolean matchesType(String mpn, ComponentType type) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return false;
        }

        String normalizedMpn = MPNUtils.normalize(mpn);

        // First check manufacturer-specific patterns
        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(normalizedMpn);
        if (manufacturer != ComponentManufacturer.UNKNOWN) {
            if (manufacturer.matchesPattern(normalizedMpn, type)) {
                return true;
            }
        }

        // Then check fallback patterns
        Map<String, Pattern> patterns = fallbackPatterns.get(type);
        if (patterns != null) {
            return patterns.values().stream()
                    .anyMatch(pattern -> pattern.matcher(normalizedMpn).matches());
        }

        return false;
    }

    /**
     * Get all possible types that match an MPN
     */
    public ComponentType[] getPossibleTypes(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return new ComponentType[]{ComponentType.GENERIC};
        }

        String normalizedMpn = MPNUtils.normalize(mpn);
        return ComponentType.getPossibleTypes(normalizedMpn);
    }
}