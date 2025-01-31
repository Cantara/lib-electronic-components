package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.PatternRegistry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AmphenolHandler implements ManufacturerHandler {
    // Common Amphenol patterns
    private static final Pattern MINITEK_PATTERN = Pattern.compile("(504182|505478)-([0-9]{4})");
    private static final Pattern HD20_PATTERN = Pattern.compile("(10120843|10120855)-([0-9]{4})");
    private static final Pattern SFP_PATTERN = Pattern.compile("(RJHSE-538[0-9])-([0-9]{2})");
    private static final Pattern USB_PATTERN = Pattern.compile("(USB3-A-[0-9]{2})-([0-9A-Z]{4})");

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CONNECTOR);
        types.add(ComponentType.CONNECTOR_AMPHENOL);
        // Add other Amphenol-specific connector types if they exist in ComponentType enum
        return types;
    }

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("504182", "Mini-PV"),          // Mini-PV series 2.0mm
            new SimpleEntry<>("505478", "Mini-PV SMT"),      // Mini-PV SMT series 2.0mm
            new SimpleEntry<>("10120843", "HD20"),           // HD20 series 2.0mm
            new SimpleEntry<>("10120855", "HD20 SMT"),       // HD20 SMT series 2.0mm
            new SimpleEntry<>("RJHSE", "SFP/SFP+"),         // SFP+ cages
            new SimpleEntry<>("USB3-A", "USB 3.0"),         // USB 3.0 Type A
            new SimpleEntry<>("10051922", "MinitekPwr"),    // Minitek Pwr 3.0mm
            new SimpleEntry<>("10151980", "BergStik"),      // BergStik headers
            new SimpleEntry<>("10129378", "ICC"),           // ICC headers
            new SimpleEntry<>("RJMG2310", "RJ45")          // RJ45 connectors
    );

    // Series to pitch mapping (in mm)
    private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
            new SimpleEntry<>("504182", "2.00"),
            new SimpleEntry<>("505478", "2.00"),
            new SimpleEntry<>("10120843", "2.00"),
            new SimpleEntry<>("10120855", "2.00"),
            new SimpleEntry<>("10051922", "3.00"),
            new SimpleEntry<>("10151980", "2.54"),
            new SimpleEntry<>("10129378", "2.54"),
            new SimpleEntry<>("RJHSE", "0.80"),     // SFP+ standard pitch
            new SimpleEntry<>("USB3-A", "2.50")     // USB standard pitch
    );

    // Series to rated current mapping (in Amperes)
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            new SimpleEntry<>("504182", 3.0),
            new SimpleEntry<>("505478", 3.0),
            new SimpleEntry<>("10120843", 3.0),
            new SimpleEntry<>("10120855", 3.0),
            new SimpleEntry<>("10051922", 5.0),
            new SimpleEntry<>("10151980", 3.0),
            new SimpleEntry<>("10129378", 2.5),
            new SimpleEntry<>("RJHSE", 0.5),      // Per pin for SFP+
            new SimpleEntry<>("USB3-A", 0.9)      // USB 3.0 standard
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Mini-PV series
        registry.addPattern(ComponentType.CONNECTOR, "^504182-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^504182-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^505478-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^505478-[0-9]{4}.*");

        // HD20 series
        registry.addPattern(ComponentType.CONNECTOR, "^10120843-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^10120843-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^10120855-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^10120855-[0-9]{4}.*");

        // SFP+ cages and connectors
        registry.addPattern(ComponentType.CONNECTOR, "^RJHSE-538[0-9]-[0-9]{2}.*");
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^RJHSE-538[0-9]-[0-9]{2}.*");

        // USB 3.0 connectors
        registry.addPattern(ComponentType.CONNECTOR, "^USB3-A-[0-9]{2}-[0-9A-Z]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^USB3-A-[0-9]{2}-[0-9A-Z]{4}.*");

        // Additional connector series
        registry.addPattern(ComponentType.CONNECTOR, "^10051922-[0-9]{4}.*");  // Minitek Pwr
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^10051922-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^10151980-[0-9]{4}.*");  // BergStik
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^10151980-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^10129378-[0-9]{4}.*");  // ICC
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^10129378-[0-9]{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^RJMG2310-[0-9]{2}.*");  // RJ45
        registry.addPattern(ComponentType.CONNECTOR_AMPHENOL, "^RJMG2310-[0-9]{2}.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Try each series pattern
        for (Pattern pattern : new Pattern[]{MINITEK_PATTERN, HD20_PATTERN, SFP_PATTERN, USB_PATTERN}) {
            Matcher matcher = pattern.matcher(mpn);
            if (matcher.matches()) {
                return matcher.group(2);  // Package code is the second group
            }
        }

        // Generic pattern for other series
        Matcher matcher = Pattern.compile(".*-([0-9]{4})$").matcher(mpn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Try to match series from the mapping
        for (String series : SERIES_FAMILIES.keySet()) {
            if (mpn.startsWith(series)) {
                return series;
            }
        }

        // Special case for series with variable numbers
        if (mpn.startsWith("RJHSE-538")) {
            return "RJHSE";
        }

        // Extract numeric series code
        Matcher matcher = Pattern.compile("^([0-9]{8})").matcher(mpn);
        if (matcher.find()) {
            return matcher.group(1);
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
        String packageCode = extractPackageCode(mpn);
        if (packageCode.isEmpty()) return 0;

        try {
            // First two digits typically indicate position/pin count
            return Integer.parseInt(packageCode.substring(0, 2));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private boolean areCompatibleMountingTypes(String mpn1, String mpn2) {
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Same mounting type is always compatible
        if (pkg1.equals(pkg2)) return true;

        // Check for through-hole compatibility
        if (isThroughHole(series1, pkg1) && isThroughHole(series2, pkg2)) return true;

        // Check for SMT compatibility
        if (isSurfaceMount(series1, pkg1) && isSurfaceMount(series2, pkg2)) return true;

        return false;
    }

    private boolean isThroughHole(String series, String packageCode) {
        // Check series-specific through-hole indicators
        if (series.equals("504182") || series.equals("10120843")) return true;
        if (series.equals("10151980")) return true;  // BergStik is THT

        // Check package code indicators
        return packageCode.endsWith("01") || packageCode.endsWith("TH");
    }

    private boolean isSurfaceMount(String series, String packageCode) {
        // Check series-specific SMT indicators
        if (series.equals("505478") || series.equals("10120855")) return true;

        // Check package code indicators
        return packageCode.endsWith("02") || packageCode.endsWith("SMT");
    }

    public String getPitch(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_PITCH.getOrDefault(series, "");
    }

    public String getMountingType(String mpn) {
        String series = extractSeries(mpn);
        String packageCode = extractPackageCode(mpn);

        if (isSurfaceMount(series, packageCode)) return "SMT";
        if (isThroughHole(series, packageCode)) return "THT";
        return "Other";
    }

    public String getOrientation(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.contains("RA") || packageCode.contains("90")) {
            return "Right Angle";
        }
        return "Vertical";
    }

    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    public boolean isShielded(String mpn) {
        // SFP and USB connectors are typically shielded
        String series = extractSeries(mpn);
        return series.startsWith("RJHSE") || series.startsWith("USB3-A");
    }

    public String getContactPlating(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.endsWith("G")) return "Gold";
        if (packageCode.endsWith("T")) return "Tin";
        if (packageCode.contains("AU")) return "Gold";
        return "Standard";
    }

    public boolean hasRetentionMechanism(String mpn) {
        String series = extractSeries(mpn);
        String packageCode = extractPackageCode(mpn);

        // Mini-PV and HD20 typically have latches
        if (series.startsWith("50418") || series.startsWith("50547")) return true;
        if (series.startsWith("101208")) return true;

        // Check for retention features in package code
        return packageCode.contains("LK") || packageCode.contains("LAT");
    }

    public String getApplicationType(String mpn) {
        String series = extractSeries(mpn);
        return switch(series) {
            case "RJHSE" -> "SFP/SFP+";
            case "USB3-A" -> "USB 3.0";
            case "RJMG2310" -> "RJ45";
            case "10051922" -> "Power";
            default -> "Signal";
        };
    }
}