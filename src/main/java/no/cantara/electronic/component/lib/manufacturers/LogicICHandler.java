package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicICHandler implements ManufacturerHandler {
    private static final Map<String, ComponentSeriesInfo> COMPONENT_SERIES = new HashMap<>();
    static {
        // 74xx series
        COMPONENT_SERIES.put("74xx", new ComponentSeriesInfo(
                ComponentType.LOGIC_IC,  // Primary type
                "^(?:74|54)[LSAHCTTF]{0,4}[0-9]{2,4}.*",
                "74xx Logic Series",
                Set.of(),
                Set.of(ComponentType.IC),  // Base type as additional
                "^(?:74|54)"
        ));

        // CD4xxx series
        COMPONENT_SERIES.put("CD4xxx", new ComponentSeriesInfo(
                ComponentType.LOGIC_IC,  // Primary type
                "^CD4[0-9]{3}.*",
                "CMOS 4000 Series",
                Set.of(),
                Set.of(ComponentType.IC),  // Base type as additional
                "^CD4"
        ));
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Register patterns in order: base type first, then specific types
        for (ComponentSeriesInfo info : COMPONENT_SERIES.values()) {
            // Register for base IC type first
            registry.addPattern(info.primaryType, info.pattern);

            // Register for additional types
            for (ComponentType additionalType : info.additionalTypes) {
                registry.addPattern(additionalType, info.pattern);
            }
        }
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.IC);  // Base type first
        types.add(ComponentType.LOGIC_IC);
        return types;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        // Direct check for logic ICs
        String upperMpn = mpn.toUpperCase();
        boolean isLogicIC = upperMpn.matches("^(?:74|54)[LSAHCTTF]{0,4}[0-9]{2,4}.*") ||
                upperMpn.matches("^CD4[0-9]{3}.*");

        if (isLogicIC) {
            // For IC type or when checking base type, return true
            if (type == ComponentType.IC) {
                System.out.println("Direct match for base type IC with MPN " + mpn);
                return true;
            }
            // For LOGIC_IC, only return true if specifically asked for LOGIC_IC
            if (type == ComponentType.LOGIC_IC) {
                System.out.println("Direct match for type LOGIC_IC with MPN " + mpn);
                return true;
            }
        }

        // Fallback to registry pattern
        Pattern pattern = registry.getPattern(type);
        if (pattern != null) {
            boolean matches = pattern.matcher(upperMpn).matches();
            System.out.println("Registry pattern match for type " + type + " with MPN " + mpn + ": " + matches);
            return matches;
        }

        return false;
    }



    @Override
    public String extractSeries(String mpn) {
        if (mpn == null) return "";

        // Extract series without package codes
        if (mpn.matches("^(?:74|54).*")) {
            return mpn.replaceAll("([0-9]{2,4}).*$", "$1");
        }
        if (mpn.matches("^CD4.*")) {
            return mpn.replaceAll("(CD4[0-9]{3}).*$", "$1");
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null) return "";

        // CD4xxx package codes
        if (mpn.startsWith("CD4")) {
            if (mpn.endsWith("BE")) return "BE";  // Plastic DIP
            if (mpn.endsWith("BC")) return "BC";  // CMOS
            if (mpn.endsWith("UBE")) return "UBE"; // Ultra-High Reliability
            if (mpn.endsWith("B")) return "B";    // Standard
            if (mpn.endsWith("E")) return "E";    // Economy
        }

        // 74xx package codes
        Matcher m = Pattern.compile(".*?([NDPMT])$").matcher(mpn);
        return m.find() ? m.group(1) : "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        return extractSeries(mpn1).equals(extractSeries(mpn2));
    }

    private boolean isCompatiblePackage(String pkg1, String pkg2) {
        // Common compatible CD4xxx packages
        Set<String> compatibleGroup1 = Set.of("BE", "B", "E");
        Set<String> compatibleGroup2 = Set.of("BC", "B");

        return (compatibleGroup1.contains(pkg1) && compatibleGroup1.contains(pkg2)) ||
                (compatibleGroup2.contains(pkg1) && compatibleGroup2.contains(pkg2));
    }

    /**
     * Class to hold information about a specific component series
     */
    private static class ComponentSeriesInfo {
        final ComponentType primaryType;
        final String pattern;
        final String description;
        final Set<String> equivalentSeries;
        final Set<ComponentType> additionalTypes;
        final String baseSeriesPattern; // Pattern to extract base series

        ComponentSeriesInfo(ComponentType primaryType, String pattern, String description,
                            Set<String> equivalentSeries) {
            this(primaryType, pattern, description, equivalentSeries, Set.of(), null);
        }

        ComponentSeriesInfo(ComponentType primaryType, String pattern, String description,
                            Set<String> equivalentSeries, Set<ComponentType> additionalTypes,
                            String baseSeriesPattern) {
            this.primaryType = primaryType;
            this.pattern = pattern;
            this.description = description;
            this.equivalentSeries = equivalentSeries;
            this.additionalTypes = additionalTypes != null ? additionalTypes : Set.of();
            this.baseSeriesPattern = baseSeriesPattern != null ? baseSeriesPattern : pattern;
        }
    }

}