package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;

import java.util.Set;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class JSTHandler implements ManufacturerHandler {
    // Common JST patterns
    private static final Pattern PH_PATTERN = Pattern.compile("(PHR?|PH[RSDL]?)-([0-9]+)(-[0-9A-Z]+)?");
    private static final Pattern XH_PATTERN = Pattern.compile("(XHR?|XH[RSDL]?)-([0-9]+)(-[0-9A-Z]+)?");
    private static final Pattern SH_PATTERN = Pattern.compile("(SHR?|SH[RSDL]?)-([0-9]+)(-[0-9A-Z]+)?");
    private static final Pattern GH_PATTERN = Pattern.compile("(GHR?|GH[RSDL]?)-([0-9]+)(-[0-9A-Z]+)?");

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("PH", "PH Series"),   // 2.0mm pitch
            new SimpleEntry<>("PHR", "PH Series"),
            new SimpleEntry<>("XH", "XH Series"),   // 2.5mm pitch
            new SimpleEntry<>("XHR", "XH Series"),
            new SimpleEntry<>("SH", "SH Series"),   // 1.0mm pitch
            new SimpleEntry<>("SHR", "SH Series"),
            new SimpleEntry<>("GH", "GH Series"),   // 1.25mm pitch
            new SimpleEntry<>("GHR", "GH Series"),
            new SimpleEntry<>("ZH", "ZH Series"),   // 1.5mm pitch
            new SimpleEntry<>("ZHR", "ZH Series"),
            new SimpleEntry<>("EH", "EH Series"),   // 2.5mm pitch
            new SimpleEntry<>("EHR", "EH Series")
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CONNECTOR,
            ComponentType.CONNECTOR_JST
        );
    }


    // Series to pitch mapping (in mm)
    private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
            new SimpleEntry<>("PH", "2.00"),
            new SimpleEntry<>("PHR", "2.00"),
            new SimpleEntry<>("XH", "2.50"),
            new SimpleEntry<>("XHR", "2.50"),
            new SimpleEntry<>("SH", "1.00"),
            new SimpleEntry<>("SHR", "1.00"),
            new SimpleEntry<>("GH", "1.25"),
            new SimpleEntry<>("GHR", "1.25"),
            new SimpleEntry<>("ZH", "1.50"),
            new SimpleEntry<>("ZHR", "1.50"),
            new SimpleEntry<>("EH", "2.50"),
            new SimpleEntry<>("EHR", "2.50")
    );

    // Series to current rating mapping (in Amperes)
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            new SimpleEntry<>("PH", 3.0),
            new SimpleEntry<>("PHR", 3.0),
            new SimpleEntry<>("XH", 3.0),
            new SimpleEntry<>("XHR", 3.0),
            new SimpleEntry<>("SH", 1.0),
            new SimpleEntry<>("SHR", 1.0),
            new SimpleEntry<>("GH", 1.0),
            new SimpleEntry<>("GHR", 1.0),
            new SimpleEntry<>("ZH", 1.0),
            new SimpleEntry<>("ZHR", 1.0),
            new SimpleEntry<>("EH", 3.0),
            new SimpleEntry<>("EHR", 3.0)
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // PH Series (2.0mm)
        registry.addPattern(ComponentType.CONNECTOR, "^PHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^PHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^PH[RSDL]?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^PH[RSDL]?-[0-9]+.*");

        // XH Series (2.5mm)
        registry.addPattern(ComponentType.CONNECTOR, "^XHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^XHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^XH[RSDL]?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^XH[RSDL]?-[0-9]+.*");

        // SH Series (1.0mm)
        registry.addPattern(ComponentType.CONNECTOR, "^SHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^SHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^SH[RSDL]?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^SH[RSDL]?-[0-9]+.*");

        // GH Series (1.25mm)
        registry.addPattern(ComponentType.CONNECTOR, "^GHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^GHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^GH[RSDL]?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_JST, "^GH[RSDL]?-[0-9]+.*");

        // Additional series
        registry.addPattern(ComponentType.CONNECTOR, "^ZHR?-[0-9]+.*");  // ZH Series (1.5mm)
        registry.addPattern(ComponentType.CONNECTOR_JST, "^ZHR?-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^EHR?-[0-9]+.*");  // EH Series (2.5mm)
        registry.addPattern(ComponentType.CONNECTOR_JST, "^EHR?-[0-9]+.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract the suffix after the pin count
        Matcher matcher = Pattern.compile("[A-Z]+-[0-9]+(-[0-9A-Z]+)").matcher(mpn);
        if (matcher.find()) {
            return matcher.group(1).substring(1);  // Remove the leading dash
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract the series prefix (PH, XH, SH, etc.)
        for (String series : SERIES_FAMILIES.keySet()) {
            if (mpn.startsWith(series)) {
                return series;
            }
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
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Check mounting type compatibility
        return areCompatibleMountingTypes(pkg1, pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    private int extractPinCount(String mpn) {
        // Try each series pattern
        for (Pattern pattern : new Pattern[]{PH_PATTERN, XH_PATTERN, SH_PATTERN, GH_PATTERN}) {
            Matcher matcher = pattern.matcher(mpn);
            if (matcher.matches()) {
                try {
                    return Integer.parseInt(matcher.group(2));
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    private boolean areCompatibleMountingTypes(String pkg1, String pkg2) {
        if (pkg1.isEmpty() || pkg2.isEmpty()) return false;

        // Same mounting type is always compatible
        if (pkg1.equals(pkg2)) return true;

        // Check for through-hole compatibility
        if (isVerticalTHT(pkg1) && isVerticalTHT(pkg2)) return true;
        if (isRightAngleTHT(pkg1) && isRightAngleTHT(pkg2)) return true;

        // Check for SMT compatibility
        if (isVerticalSMT(pkg1) && isVerticalSMT(pkg2)) return true;
        if (isRightAngleSMT(pkg1) && isRightAngleSMT(pkg2)) return true;

        return false;
    }

    private boolean isVerticalTHT(String pkg) {
        return pkg.endsWith("V") || pkg.isEmpty();  // Empty means standard vertical
    }

    private boolean isRightAngleTHT(String pkg) {
        return pkg.endsWith("R");
    }

    private boolean isVerticalSMT(String pkg) {
        return pkg.endsWith("VS") || pkg.endsWith("S");
    }

    private boolean isRightAngleSMT(String pkg) {
        return pkg.endsWith("RS");
    }

    public String getPitch(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_PITCH.getOrDefault(series, "");
    }

    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.contains("S")) return "SMT";
        return "THT";  // Default to through-hole
    }

    public String getOrientation(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.contains("R")) return "Right Angle";
        return "Vertical";  // Default to vertical
    }

    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    public boolean isKeyed(String mpn) {
        // All JST connectors are keyed for proper orientation
        return true;
    }

    public String getGender(String mpn) {
        // For JST, housing (female) parts typically end with R
        // Header (male) parts typically don't have the R
        String series = extractSeries(mpn);
        return series.endsWith("R") ? "Female" : "Male";
    }

    public String getColor(String mpn) {
        String packageCode = extractPackageCode(mpn);
        return switch (packageCode.charAt(packageCode.length() - 1)) {
            case 'W' -> "White";
            case 'B' -> "Black";
            case 'R' -> "Red";
            default -> "Natural";
        };
    }
}