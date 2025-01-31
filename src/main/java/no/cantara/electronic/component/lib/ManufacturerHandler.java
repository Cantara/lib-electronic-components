package no.cantara.electronic.component.lib;

import java.util.Set;
import java.util.regex.Pattern;

public interface ManufacturerHandler {
    void initializePatterns(PatternRegistry registry);
    String extractPackageCode(String mpn);
    String extractSeries(String mpn);
    boolean isOfficialReplacement(String mpn1, String mpn2);
    Set<ManufacturerComponentType> getManufacturerTypes();
    Set<ComponentType> getSupportedTypes();


    /**
     * Check if an MPN matches a specific component type.
     * This allows handlers to implement custom matching logic beyond simple pattern matching.
     */
    default boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;
        Pattern pattern = patterns.getPattern(type);
        return pattern != null && pattern.matcher(mpn.toUpperCase()).matches();
    }
}