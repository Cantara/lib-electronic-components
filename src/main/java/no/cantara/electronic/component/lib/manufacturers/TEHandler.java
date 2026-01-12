package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TEHandler implements ManufacturerHandler {
    // Common TE Connectivity patterns
    private static final Pattern TERMINAL_PATTERN = Pattern.compile("(\\d+)-(\\d+)(-\\d+)?");
    private static final Pattern HEADER_PATTERN = Pattern.compile("(\\d+)-?(\\d+)-(\\d+)");
    private static final Pattern MATE_N_LOK_PATTERN = Pattern.compile("([0-9]+)-([0-9]+)-([0-9]+)");

    // Series family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.of(
            "282837", "Terminal Block",
            "282836", "Terminal Block",
            "5-826", "PCB Header",
            "5-103", "PCB Header",
            "640456", "IDC Connector",
            "640457", "IDC Connector",
            "1-770966", "Rectangular Connector",
            "1-770967", "Rectangular Connector",
            "350211", "MATE-N-LOK"
    );

    // Pitch mapping (series to pitch in mm)
    private static final Map<String, String> SERIES_PITCH = Map.of(
            "282837", "5.08",
            "282836", "5.00",
            "5-826", "2.54",
            "5-103", "2.54",
            "640456", "2.54",
            "640457", "2.54",
            "350211", "4.14"  // MATE-N-LOK
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CONNECTOR,
            ComponentType.CONNECTOR_TE
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {

        // Headers and connectors
        registry.addPattern(ComponentType.CONNECTOR, "^(?:(?:1-|2-)[0-9]{6}|(?:282[0-9]{3}))-[0-9]+");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^(?:(?:1-|2-)[0-9]{6}|(?:282[0-9]{3}))-[0-9]+");

// Terminal Blocks
        registry.addPattern(ComponentType.CONNECTOR, "^282[0-9]{3}-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^282[0-9]{3}-[0-9]+.*");

        // PCB Headers
        registry.addPattern(ComponentType.CONNECTOR, "^5-[0-9]{3}-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^5-[0-9]{3}-[0-9]+.*");

        // IDC Connectors
        registry.addPattern(ComponentType.CONNECTOR, "^64[0-9]{4}-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^64[0-9]{4}-[0-9]+.*");

        // MATE-N-LOK
        registry.addPattern(ComponentType.CONNECTOR, "^350[0-9]{3}-[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^350[0-9]{3}-[0-9]+.*");

        // General pattern for other TE connectors
        registry.addPattern(ComponentType.CONNECTOR, "^[0-9]+-[0-9]+(-[0-9]+)?$");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^[0-9]+-[0-9]+(-[0-9]+)?$");

        // Edge headers
        registry.addPattern(ComponentType.CONNECTOR, "^5-[0-9]{3}-[0-9]+");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^5-[0-9]{3}-[0-9]+");

        // Terminal blocks (e.g., 282836-2)
        registry.addPattern(ComponentType.CONNECTOR, "^282[0-9]{3}-[0-9]+$");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^282[0-9]{3}-[0-9]+$");

        // Headers with prefix (e.g., 1-284392-0)
        registry.addPattern(ComponentType.CONNECTOR, "^[12]-[0-9]{6}-[0-9]+$");
        registry.addPattern(ComponentType.CONNECTOR_TE, "^[12]-[0-9]{6}-[0-9]+$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        // Direct pattern check for common formats
        if (type == ComponentType.CONNECTOR || type == ComponentType.CONNECTOR_TE) {
            return mpn.matches("^282[0-9]{3}-[0-9]+$") || // Terminal blocks
                    mpn.matches("^[12]-[0-9]{6}-[0-9]+$"); // Headers with prefix
        }

        // Use registry for other patterns
        Pattern pattern = registry.getPattern(type);
        return pattern != null && pattern.matcher(mpn).matches();
    }


    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // For terminal blocks and headers with variant suffix
        Matcher m = Pattern.compile(".*-([0-9])$").matcher(mpn);
        if (m.matches()) {
            return m.group(1);
        }

        // For MATE-N-LOK with package code in middle
        Matcher mateLokMatcher = MATE_N_LOK_PATTERN.matcher(mpn);
        if (mateLokMatcher.matches()) {
            return mateLokMatcher.group(2);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Try to match different series patterns
        for (String seriesPrefix : SERIES_FAMILIES.keySet()) {
            if (mpn.startsWith(seriesPrefix)) {
                return seriesPrefix;
            }
        }

        // For complex part numbers (e.g., 1-770966-x)
        Matcher m = Pattern.compile("(\\d+-\\d+).*").matcher(mpn);
        if (m.matches()) {
            return m.group(1);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
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

        // Get pitch
        String pitch1 = SERIES_PITCH.get(series1);
        String pitch2 = SERIES_PITCH.get(series2);

        // Must have same pitch
        if (pitch1 == null || !pitch1.equals(pitch2)) {
            return false;
        }

        // Check if they're just packaging variants
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Different package codes are replaceable within same series
        return !pkg1.equals(pkg2);
    }

    private int extractPinCount(String mpn) {
        // Terminal blocks and headers
        Matcher terminalMatcher = TERMINAL_PATTERN.matcher(mpn);
        if (terminalMatcher.matches()) {
            try {
                return Integer.parseInt(terminalMatcher.group(2));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // MATE-N-LOK
        Matcher mateLokMatcher = MATE_N_LOK_PATTERN.matcher(mpn);
        if (mateLokMatcher.matches()) {
            try {
                return Integer.parseInt(mateLokMatcher.group(3));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    public String getFamily(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_FAMILIES.getOrDefault(series, "Connector");
    }

    public String getPitch(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_PITCH.getOrDefault(series, "");
    }

    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        return switch (packageCode) {
            case "1" -> "THT";
            case "2" -> "SMD";
            case "3" -> "Press-fit";
            case "4" -> "Wire-wrap";
            case "5" -> "Screw";
            default -> "THT";  // Default to THT if not specified
        };
    }

    public boolean isPolarized(String mpn) {
        // Most TE connectors are polarized, but some specific series are not
        String series = extractSeries(mpn);
        return !series.startsWith("282");  // Terminal blocks are typically not polarized
    }

    public String getGender(String mpn) {
        // Determine connector gender if applicable
        if (mpn.endsWith("-1")) {
            return "Male";
        } else if (mpn.endsWith("-2")) {
            return "Female";
        }
        return "Unspecified";
    }

    public String getOrientation(String mpn) {
        if (mpn.contains("-R")) {
            return "Right Angle";
        } else if (mpn.contains("-V")) {
            return "Vertical";
        }
        return "Standard";
    }
}