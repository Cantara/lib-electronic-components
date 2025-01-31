package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class HiroseHandler implements ManufacturerHandler {
    // Common Hirose patterns
    private static final Pattern DF13_PATTERN = Pattern.compile("DF13-([0-9]+)([PRS])-([0-9A-Z]+)");
    private static final Pattern DF14_PATTERN = Pattern.compile("DF14-([0-9]+)([PRS])-([0-9A-Z]+)");
    private static final Pattern FH12_PATTERN = Pattern.compile("FH12-([0-9]+)([SP])-([0-9A-Z]+)");
    private static final Pattern BM_PATTERN = Pattern.compile("BM([0-9]+)-([0-9A-Z]+)");

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CONNECTOR);
        types.add(ComponentType.CONNECTOR_HIROSE);
        // Could add more specific connector types if they exist in ComponentType enum
        return types;
    }


    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("DF13", "DF13 Series"),    // 1.25mm pitch
            new SimpleEntry<>("DF14", "DF14 Series"),    // 1.25mm pitch, automotive
            new SimpleEntry<>("FH12", "FH12 Series"),    // 0.5mm pitch, FFC/FPC
            new SimpleEntry<>("BM", "BM Series"),        // USB connectors
            new SimpleEntry<>("DF52", "DF52 Series"),    // 0.8mm pitch
            new SimpleEntry<>("DF63", "DF63 Series"),    // 0.5mm pitch, board-to-FPC
            new SimpleEntry<>("FH19", "FH19 Series"),    // 0.4mm pitch, FFC/FPC
            new SimpleEntry<>("FH28", "FH28 Series"),    // 0.4mm pitch, dual row
            new SimpleEntry<>("GT17", "GT17 Series")     // Board-to-board, 0.5mm
    );

    // Series to pitch mapping (in mm)
    private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
            new SimpleEntry<>("DF13", "1.25"),
            new SimpleEntry<>("DF14", "1.25"),
            new SimpleEntry<>("FH12", "0.50"),
            new SimpleEntry<>("BM", "2.00"),     // Various, depends on USB type
            new SimpleEntry<>("DF52", "0.80"),
            new SimpleEntry<>("DF63", "0.50"),
            new SimpleEntry<>("FH19", "0.40"),
            new SimpleEntry<>("FH28", "0.40"),
            new SimpleEntry<>("GT17", "0.50")
    );

    // Series to rated current mapping (in Amperes)
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            new SimpleEntry<>("DF13", 1.0),
            new SimpleEntry<>("DF14", 2.0),
            new SimpleEntry<>("FH12", 0.5),
            new SimpleEntry<>("BM", 1.5),      // Varies by USB specification
            new SimpleEntry<>("DF52", 0.5),
            new SimpleEntry<>("DF63", 0.5),
            new SimpleEntry<>("FH19", 0.5),
            new SimpleEntry<>("FH28", 0.5),
            new SimpleEntry<>("GT17", 0.5)
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // DF13 Series (1.25mm pitch)
        registry.addPattern(ComponentType.CONNECTOR, "^DF13-[0-9]+[PRS]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^DF13-[0-9]+[PRS]-.*");

        // DF14 Series (1.25mm automotive)
        registry.addPattern(ComponentType.CONNECTOR, "^DF14-[0-9]+[PRS]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^DF14-[0-9]+[PRS]-.*");

        // FH12 Series (0.5mm FFC/FPC)
        registry.addPattern(ComponentType.CONNECTOR, "^FH12-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^FH12-[0-9]+[SP]-.*");

        // BM Series (USB)
        registry.addPattern(ComponentType.CONNECTOR, "^BM[0-9]+-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^BM[0-9]+-.*");

        // Additional series
        registry.addPattern(ComponentType.CONNECTOR, "^DF52-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^DF52-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR, "^DF63-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^DF63-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR, "^FH19-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^FH19-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR, "^FH28-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^FH28-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR, "^GT17-[0-9]+[SP]-.*");
        registry.addPattern(ComponentType.CONNECTOR_HIROSE, "^GT17-[0-9]+[SP]-.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Try each series pattern
        for (Pattern pattern : new Pattern[]{DF13_PATTERN, DF14_PATTERN, FH12_PATTERN, BM_PATTERN}) {
            Matcher matcher = pattern.matcher(mpn);
            if (matcher.matches()) {
                return matcher.group(matcher.groupCount()); // Last group contains package code
            }
        }

        // Generic pattern for other series
        Matcher matcher = Pattern.compile(".*-([0-9A-Z]+)$").matcher(mpn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract series prefix
        for (String series : SERIES_FAMILIES.keySet()) {
            if (mpn.startsWith(series)) {
                return series;
            }
        }

        // Special case for BM series which might have numbers immediately after prefix
        if (mpn.startsWith("BM")) {
            return "BM";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from same series
        if (!series1.equals(series2)) {
            return false;
        }

        // Extract pin counts
        int pins1 = extractPinCount(mpn1);
        int pins2 = extractPinCount(mpn2);

        // Must have same pin count
        if (pins1 != pins2 || pins1 == 0) {
            return false;
        }

        // Check if they're compatible mounting variants
        return areCompatibleMountingTypes(mpn1, mpn2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    private int extractPinCount(String mpn) {
        // Try each series pattern
        for (Pattern pattern : new Pattern[]{DF13_PATTERN, DF14_PATTERN, FH12_PATTERN}) {
            Matcher matcher = pattern.matcher(mpn);
            if (matcher.matches()) {
                try {
                    return Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }

        // Special case for BM series
        Matcher bmMatcher = BM_PATTERN.matcher(mpn);
        if (bmMatcher.matches()) {
            try {
                return Integer.parseInt(bmMatcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    private boolean areCompatibleMountingTypes(String mpn1, String mpn2) {
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Same mounting type is always compatible
        if (pkg1.equals(pkg2)) return true;

        // Check for through-hole compatibility
        if (isThroughHole(pkg1) && isThroughHole(pkg2)) return true;

        // Check for SMT compatibility
        if (isSurfaceMount(pkg1) && isSurfaceMount(pkg2)) return true;

        // Check for right-angle compatibility
        if (isRightAngle(pkg1) && isRightAngle(pkg2)) return true;

        return false;
    }

    private boolean isThroughHole(String packageCode) {
        return packageCode.contains("P") || packageCode.contains("PDT");
    }

    private boolean isSurfaceMount(String packageCode) {
        return packageCode.contains("S") || packageCode.contains("SDS");
    }

    private boolean isRightAngle(String packageCode) {
        return packageCode.contains("R") || packageCode.contains("RDS");
    }

    public String getPitch(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_PITCH.getOrDefault(series, "");
    }

    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (isSurfaceMount(packageCode)) return "SMT";
        if (isThroughHole(packageCode)) return "THT";
        return "Other";
    }

    public String getOrientation(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (isRightAngle(packageCode)) return "Right Angle";
        return "Vertical";
    }

    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    public boolean isShielded(String mpn) {
        String packageCode = extractPackageCode(mpn);
        return packageCode.contains("DS") || packageCode.contains("DSS");
    }

    public String getContactPlating(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.endsWith("G")) return "Gold";
        if (packageCode.endsWith("T")) return "Tin";
        return "Standard";
    }

    public boolean isLockingType(String mpn) {
        // Most DF series connectors have locking mechanism
        String series = extractSeries(mpn);
        return series.startsWith("DF");
    }

    public String getApplicationType(String mpn) {
        String series = extractSeries(mpn);
        return switch(series) {
            case "FH12", "FH19", "FH28" -> "FFC/FPC";
            case "BM" -> "USB";
            case "GT17" -> "Board-to-Board";
            case "DF14" -> "Automotive";
            default -> "General Purpose";
        };
    }
}