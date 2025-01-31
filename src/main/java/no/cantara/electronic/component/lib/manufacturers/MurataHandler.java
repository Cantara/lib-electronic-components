package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import java.util.*;
import java.util.regex.Pattern;

public class MurataHandler implements ManufacturerHandler {
    private static final Map<String, ComponentSeriesInfo> COMPONENT_SERIES = new LinkedHashMap<>();
    static {
        // === CAPACITORS ===
        COMPONENT_SERIES.put("GRM", new ComponentSeriesInfo(
                ComponentType.CAPACITOR_CERAMIC_MURATA,
                "^GRM[0-9][0-9A-Z]{2}[A-Z0-9]*$",
                "Multilayer Ceramic Chip Capacitor",
                Set.of(),
                Set.of(ComponentType.CAPACITOR),
                "^GRM"
        ));

        COMPONENT_SERIES.put("GCM", new ComponentSeriesInfo(
                ComponentType.CAPACITOR_CERAMIC_MURATA,
                "^GCM[0-9][0-9A-Z]{2}[A-Z0-9]*$",
                "Automotive Grade MLCC",
                Set.of(),
                Set.of(ComponentType.CAPACITOR),
                "^GCM"
        ));

        COMPONENT_SERIES.put("KC", new ComponentSeriesInfo(
                ComponentType.CAPACITOR_CERAMIC_MURATA,
                "^KC[ABMZ][0-9][0-9A-Z]{2}[A-Z0-9]*$",
                "High Voltage MLCC",
                Set.of(),
                Set.of(ComponentType.CAPACITOR),
                "^KC"
        ));

        // === INDUCTORS ===
        COMPONENT_SERIES.put("LQM", new ComponentSeriesInfo(
                ComponentType.INDUCTOR_CHIP_MURATA,
                "^LQM[0-9A-Z]{2}[A-Z]{2}[0-9R][0-9A-Z]*$",
                "Power Inductor",
                Set.of(),
                Set.of(ComponentType.INDUCTOR),
                "^LQM"
        ));

        COMPONENT_SERIES.put("LQW", new ComponentSeriesInfo(
                ComponentType.INDUCTOR_CHIP_MURATA,
                "^LQW[0-9A-Z]{2}[A-Z]{2}[0-9R][0-9A-Z]*$",
                "Wire Wound Inductor",
                Set.of(),
                Set.of(ComponentType.INDUCTOR),
                "^LQW"
        ));

        COMPONENT_SERIES.put("LQG", new ComponentSeriesInfo(
                ComponentType.INDUCTOR_CHIP_MURATA,
                "^LQG[0-9A-Z]{2}[A-Z]{2}[0-9R][0-9A-Z]*$",
                "RF Inductor",
                Set.of(),
                Set.of(ComponentType.INDUCTOR),
                "^LQG"
        ));

        COMPONENT_SERIES.put("DFE", new ComponentSeriesInfo(
                ComponentType.INDUCTOR_POWER_MURATA,
                "^DFE[0-9A-Z]+$",
                "Power Inductor",
                Set.of(),
                Set.of(ComponentType.INDUCTOR),
                "^DFE"
        ));

        // === EMI SUPPRESSION ===
        COMPONENT_SERIES.put("BLM", new ComponentSeriesInfo(
                ComponentType.INDUCTOR_CHIP_MURATA,
                "^BLM[0-9A-Z]+$",
                "Ferrite Bead",
                Set.of(),
                Set.of(ComponentType.INDUCTOR),
                "^BLM"
        ));

        COMPONENT_SERIES.put("NFM", new ComponentSeriesInfo(
                ComponentType.EMI_FILTER_MURATA,
                "^NFM[0-9A-Z]+$",
                "EMI Suppression Filter",
                Set.of(),
                Set.of(ComponentType.INDUCTOR),
                "^NFM"
        ));

        COMPONENT_SERIES.put("DLW", new ComponentSeriesInfo(
                ComponentType.COMMON_MODE_CHOKE_MURATA,
                "^DLW[0-9A-Z]+$",
                "Common Mode Choke Coil",
                Set.of(),
                Set.of(ComponentType.INDUCTOR),
                "^DLW"
        ));
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        for (ComponentSeriesInfo info : COMPONENT_SERIES.values()) {
            registry.addPattern(info.primaryType, info.pattern);
            registry.addPattern(info.primaryType.getBaseType(), info.pattern);
            for (ComponentType additionalType : info.additionalTypes) {
                registry.addPattern(additionalType, info.pattern);
            }
        }
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CAPACITOR);
        types.add(ComponentType.CAPACITOR_CERAMIC_MURATA);
        types.add(ComponentType.INDUCTOR);
        types.add(ComponentType.INDUCTOR_CHIP_MURATA);
        types.add(ComponentType.INDUCTOR_POWER_MURATA);
        types.add(ComponentType.EMI_FILTER_MURATA);
        types.add(ComponentType.COMMON_MODE_CHOKE_MURATA);
        return types;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();
        for (ComponentSeriesInfo info : COMPONENT_SERIES.values()) {
            if (upperMpn.matches(info.pattern)) {
                return type == info.primaryType ||
                        type == info.primaryType.getBaseType() ||
                        info.additionalTypes.contains(type);
            }
        }

        Pattern pattern = registry.getPattern(type);
        return pattern != null && pattern.matcher(upperMpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null) return "";

        // GRM/GCM series
        if (mpn.startsWith("GRM") || mpn.startsWith("GCM")) {
            try {
                return mpn.substring(3, 6);  // Size code in format "188", "155", etc
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // LQx series inductors
        if (mpn.matches("^LQ[MGW].*")) {
            try {
                return mpn.substring(3, 7);  // Size code like "2012"
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null) return "";

        // GRM/GCM/KC series capacitors
        if (mpn.matches("^(?:GRM|GCM|KC[ABMZ]).*")) {
            return mpn.substring(0, 6);  // Include size code
        }

        // LQx series inductors
        if (mpn.matches("^LQ[MGW].*")) {
            return mpn.substring(0, 7);  // Include size code
        }

        // Other series - base identifier
        for (ComponentSeriesInfo info : COMPONENT_SERIES.values()) {
            if (mpn.matches(info.pattern)) {
                return mpn.substring(0, 3);  // Base 3-letter identifier
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        return !series1.isEmpty() && series1.equals(series2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
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