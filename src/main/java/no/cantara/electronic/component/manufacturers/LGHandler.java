
package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class LGHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // LED patterns for LG
        registry.addPattern(ComponentType.LED, "^LG\\s+R\\d+.*");  // Basic LED pattern
        registry.addPattern(ComponentType.LED, "^LG\\s+R\\d+(-[A-Z]+)?$");  // With optional suffix
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.LED);
        // Add any LG-specific LED or semiconductor types if they exist in ComponentType enum
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract package code from suffix (e.g., -KN)
        int dashIndex = mpn.indexOf('-');
        if (dashIndex > 0 && dashIndex < mpn.length() - 1) {
            return mpn.substring(dashIndex + 1);
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // For LG LEDs, return the base part before any suffix
        String[] parts = mpn.split("\\s+");
        if (parts.length > 1) {
            String basePart = parts[1];
            int dashIndex = basePart.indexOf('-');
            if (dashIndex > 0) {
                return "LG " + basePart.substring(0, dashIndex);
            }
            return "LG " + basePart;
        }
        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // LEDs with same base part but different or no suffix are replacements
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        return !series1.isEmpty() && series1.equals(series2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}