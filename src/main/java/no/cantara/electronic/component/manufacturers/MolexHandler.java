package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.AbstractMap.SimpleEntry;


public class MolexHandler implements ManufacturerHandler {
    private static final Pattern MICRO_FIT_PATTERN = Pattern.compile("(43045)-(\\d{4})");
    private static final Pattern MINI_FIT_PATTERN = Pattern.compile("(39281)-(\\d{4})");
    private static final Pattern PICOBLADE_PATTERN = Pattern.compile("(53261)-(\\d{4})");
    private static final Pattern KK_PATTERN = Pattern.compile("(22057)-(\\d{4})");

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CONNECTOR);
        types.add(ComponentType.CONNECTOR_MOLEX);
        // Could add more specific connector types if they exist in ComponentType enum
        return types;
    }

        // Series to family mapping
        private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
                new SimpleEntry<>("43045", "Micro-Fit 3.0"),
                new SimpleEntry<>("43046", "Micro-Fit 3.0"),
                new SimpleEntry<>("39281", "Mini-Fit Jr."),
                new SimpleEntry<>("39282", "Mini-Fit Jr."),
                new SimpleEntry<>("53261", "PicoBlade"),
                new SimpleEntry<>("53262", "PicoBlade"),
                new SimpleEntry<>("22057", "KK 254"),
                new SimpleEntry<>("22058", "KK 254"),
                new SimpleEntry<>("51021", "PicoClasp"),
                new SimpleEntry<>("51047", "Nano-Fit"),
                new SimpleEntry<>("87832", "Micro-Lock Plus")
        );

        // Series to pitch mapping (in mm)
        private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
                new SimpleEntry<>("43045", "3.00"),  // Micro-Fit 3.0
                new SimpleEntry<>("43046", "3.00"),
                new SimpleEntry<>("39281", "4.20"),  // Mini-Fit Jr.
                new SimpleEntry<>("39282", "4.20"),
                new SimpleEntry<>("53261", "1.25"),  // PicoBlade
                new SimpleEntry<>("53262", "1.25"),
                new SimpleEntry<>("22057", "2.54"),  // KK 254
                new SimpleEntry<>("22058", "2.54"),
                new SimpleEntry<>("51021", "1.00"),  // PicoClasp
                new SimpleEntry<>("51047", "2.50"),  // Nano-Fit
                new SimpleEntry<>("87832", "2.00")   // Micro-Lock Plus
        );

        // Series to rated current mapping (in Amperes)
        private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
                new SimpleEntry<>("43045", 5.0),    // Micro-Fit 3.0
                new SimpleEntry<>("43046", 5.0),
                new SimpleEntry<>("39281", 9.0),    // Mini-Fit Jr.
                new SimpleEntry<>("39282", 9.0),
                new SimpleEntry<>("53261", 1.0),    // PicoBlade
                new SimpleEntry<>("53262", 1.0),
                new SimpleEntry<>("22057", 3.0),    // KK 254
                new SimpleEntry<>("22058", 3.0),
                new SimpleEntry<>("51021", 1.0),    // PicoClasp
                new SimpleEntry<>("51047", 3.5),    // Nano-Fit
                new SimpleEntry<>("87832", 3.0)     // Micro-Lock Plus
        );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Micro-Fit 3.0 series
        registry.addPattern(ComponentType.CONNECTOR, "^43045-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^43045-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^43046-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^43046-\\d{4}.*");

        // Mini-Fit Jr. series
        registry.addPattern(ComponentType.CONNECTOR, "^39281-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^39281-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^39282-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^39282-\\d{4}.*");

        // PicoBlade series
        registry.addPattern(ComponentType.CONNECTOR, "^53261-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^53261-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^53262-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^53262-\\d{4}.*");

        // KK 254 series
        registry.addPattern(ComponentType.CONNECTOR, "^22057-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^22057-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^22058-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^22058-\\d{4}.*");

        // Additional series
        registry.addPattern(ComponentType.CONNECTOR, "^51021-\\d{4}.*");  // PicoClasp
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^51021-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^51047-\\d{4}.*");  // Nano-Fit
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^51047-\\d{4}.*");
        registry.addPattern(ComponentType.CONNECTOR, "^87832-\\d{4}.*");  // Micro-Lock Plus
        registry.addPattern(ComponentType.CONNECTOR_MOLEX, "^87832-\\d{4}.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract the 4-digit code after the dash
        Matcher matcher = Pattern.compile("\\d{5}-(\\d{4})").matcher(mpn);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract the 5-digit series code
        Matcher matcher = Pattern.compile("(\\d{5})-\\d{4}").matcher(mpn);
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

        // Must be from same connector family
        if (!getFamily(series1).equals(getFamily(series2))) {
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

        return areCompatibleMountingTypes(pkg1, pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    private String getFamily(String series) {
        return SERIES_FAMILIES.getOrDefault(series, "Unknown");
    }

    private int extractPinCount(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.isEmpty()) return 0;

        try {
            // First two digits of package code typically represent pin count
            return Integer.parseInt(packageCode.substring(0, 2));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private boolean areCompatibleMountingTypes(String pkg1, String pkg2) {
        if (pkg1.isEmpty() || pkg2.isEmpty()) return false;

        // Last two digits typically indicate mounting type and options
        String mount1 = pkg1.substring(2);
        String mount2 = pkg2.substring(2);

        // Same mounting type is always compatible
        if (mount1.equals(mount2)) return true;

        // Check for compatible through-hole variants
        if (isThroughHole(mount1) && isThroughHole(mount2)) return true;

        // Check for compatible surface mount variants
        if (isSurfaceMount(mount1) && isSurfaceMount(mount2)) return true;

        return false;
    }

    private boolean isThroughHole(String mountingCode) {
        return mountingCode.endsWith("01") || mountingCode.endsWith("02");
    }

    private boolean isSurfaceMount(String mountingCode) {
        return mountingCode.endsWith("10") || mountingCode.endsWith("11");
    }

    public String getPitch(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_PITCH.getOrDefault(series, "");
    }

    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.length() < 4) return "";

        String mountCode = packageCode.substring(2);
        if (isThroughHole(mountCode)) return "THT";
        if (isSurfaceMount(mountCode)) return "SMT";
        return "Other";
    }

    public String getOrientation(String mpn) {
        String packageCode = extractPackageCode(mpn);
        if (packageCode.length() < 4) return "";

        char orientationChar = packageCode.charAt(2);
        return switch (orientationChar) {
            case '0' -> "Vertical";
            case '1' -> "Right Angle";
            case '2' -> "Right Angle Reversed";
            default -> "Other";
        };
    }

    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    public boolean isKeyed(String mpn) {
        // Most Molex connectors are keyed for proper orientation
        String series = extractSeries(mpn);
        // Only some vintage or basic headers might not be keyed
        return !series.startsWith("220");  // KK series might have unkeyed variants
    }

    public String getGender(String mpn) {
        String series = extractSeries(mpn);
        // Even series numbers typically indicate receptacle (female)
        // Odd series numbers typically indicate header (male)
        return Integer.parseInt(series) % 2 == 0 ? "Female" : "Male";
    }

    public boolean hasLatchingMechanism(String mpn) {
        String series = extractSeries(mpn);
        // Most Molex connectors have latching mechanisms except some basic headers
        return !series.startsWith("220");  // KK series might not have latching
    }
}