package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class CreeHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // LED patterns for Cree
        registry.addPattern(ComponentType.LED, "^CLV.*");           // CLV series
        registry.addPattern(ComponentType.LED_HIGHPOWER_CREE, "^CLV.*");
        registry.addPattern(ComponentType.LED, "^CLVBA-[A-Z]{3}$"); // CLVBA series with bin codes
        registry.addPattern(ComponentType.LED_HIGHPOWER_CREE, "^CLVBA-[A-Z]{3}$");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.LED,
            ComponentType.LED_HIGHPOWER_CREE,
            ComponentType.LED_STANDARD_KINGBRIGHT,
            ComponentType.LED_RGB_KINGBRIGHT,
            ComponentType.LED_SMD_KINGBRIGHT
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // For CLVBA series, no separate package code
        if (mpn.startsWith("CLVBA-")) {
            return "SMD";
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // For CLVBA-FKA style LEDs, return everything before the last letter
        if (mpn.startsWith("CLVBA-")) {
            return mpn.substring(0, mpn.length() - 1);
        }

        // For other series, return the base part before any dash
        int dashIndex = mpn.indexOf('-');
        return dashIndex > 0 ? mpn.substring(0, dashIndex) : mpn;
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // LEDs with same base part but different bin codes are replacements
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        return !series1.isEmpty() && series1.equals(series2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}