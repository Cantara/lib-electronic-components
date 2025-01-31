package no.cantara.electronic.component.connectors;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class TEConnectorHandler implements ConnectorHandler {
    // Updated pattern to handle all formats:
    // - XXXXXX-Y (e.g., 282836-2)
    // - X-XXXXXX-Y (e.g., 1-284392-0)
    private static final Pattern CONNECTOR_PATTERN = Pattern.compile("(?:(\\d+)-)?(\\d+)-(\\d+)");

    // Constants for similarity calculation
    private static final double SAME_SERIES_SCORE = 0.8;  // Base score for same series and prefix
    private static final double SAME_VARIANT_SCORE = 0.2;  // Additional score for same variant

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("282836", "Terminal Block 5.0mm"),
            new SimpleEntry<>("282837", "Terminal Block 5.08mm"),
            new SimpleEntry<>("282838", "Terminal Block 7.5mm"),
            new SimpleEntry<>("282842", "Terminal Block 10.0mm"),
            new SimpleEntry<>("284392", "Standard Edge Header")
    );

    // Series to pitch mapping (in mm)
    private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
            new SimpleEntry<>("282836", "5.00"),
            new SimpleEntry<>("282837", "5.08"),
            new SimpleEntry<>("282838", "7.50"),
            new SimpleEntry<>("282842", "10.00"),
            new SimpleEntry<>("284392", "2.54")
    );

    private record ParsedMPN(String prefix, String series, String variant) {
        @Override
        public String toString() {
            return "ParsedMPN[prefix='" + prefix + "', series='" + series + "', variant='" + variant + "']";
        }
    }

    private ParsedMPN parseMPN(String mpn) {
        if (mpn == null) return null;

        Matcher m = CONNECTOR_PATTERN.matcher(mpn);
        if (m.matches()) {
            String prefix = m.group(1) != null ? m.group(1) : "";
            String series = m.group(2);
            String variant = m.group(3);
            System.out.println("Parsed MPN " + mpn + " into: prefix='" + prefix + "', series='" + series + "', variant='" + variant + "'");
            return new ParsedMPN(prefix, series, variant);
        }
        System.out.println("Failed to parse MPN: " + mpn);
        return null;
    }

    @Override
    public String getFamily() {
        return "Terminal Block";
    }

    @Override
    public int getPinCount(String mpn) {
        ParsedMPN parsed = parseMPN(mpn);
        if (parsed == null) return 0;

        try {
            return Integer.parseInt(parsed.variant());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String getPitch(String mpn) {
        ParsedMPN parsed = parseMPN(mpn);
        if (parsed == null) return "";

        return SERIES_PITCH.getOrDefault(parsed.series(), "");
    }

    @Override
    public String getMountingType(String mpn) {
        return "THT";  // Terminal blocks are typically through-hole
    }

    @Override
    public String getVariant(String mpn) {
        ParsedMPN parsed = parseMPN(mpn);
        if (parsed == null) return "";

        return parsed.variant();
    }

    @Override
    public boolean areCompatible(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        ParsedMPN parsed1 = parseMPN(mpn1);
        ParsedMPN parsed2 = parseMPN(mpn2);

        if (parsed1 == null || parsed2 == null) return false;

        // Components are compatible if they're in the same series
        // For series with prefixes, they must match exactly
        boolean sameSeries = parsed1.series().equals(parsed2.series());
        boolean prefixMatch = parsed1.prefix().equals(parsed2.prefix());

        return sameSeries && prefixMatch;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            System.out.println("Null MPN provided");
            return 0.0;
        }

        ParsedMPN parsed1 = parseMPN(mpn1);
        ParsedMPN parsed2 = parseMPN(mpn2);

        if (parsed1 == null || parsed2 == null) {
            System.out.println("Failed to parse one or both MPNs");
            return 0.0;
        }

        System.out.println("Comparing: " + parsed1 + " with " + parsed2);

        double similarity = 0.0;

        // Check series and prefix match
        boolean sameSeries = parsed1.series().equals(parsed2.series());
        boolean samePrefix = parsed1.prefix().equals(parsed2.prefix());

        System.out.println("Same series: " + sameSeries + " ('" + parsed1.series() + "' vs '" + parsed2.series() + "')");
        System.out.println("Same prefix: " + samePrefix + " ('" + parsed1.prefix() + "' vs '" + parsed2.prefix() + "')");

        if (sameSeries && samePrefix) {
            // Base similarity for same series and prefix
            similarity = SAME_SERIES_SCORE;
            System.out.println("Adding base series score: " + SAME_SERIES_SCORE);

            // Additional score if variants match
            if (parsed1.variant().equals(parsed2.variant())) {
                similarity += SAME_VARIANT_SCORE;
                System.out.println("Adding variant match score: " + SAME_VARIANT_SCORE);
            }
        }

        System.out.println("Final similarity: " + similarity);
        return similarity;
    }

    @Override
    public String getSeriesName(String mpn) {
        ParsedMPN parsed = parseMPN(mpn);
        if (parsed == null) return "";

        return SERIES_FAMILIES.getOrDefault(parsed.series(), "Unknown");
    }

    public boolean isStackable(String mpn) {
        ParsedMPN parsed = parseMPN(mpn);
        if (parsed == null) return false;

        return SERIES_FAMILIES.containsKey(parsed.series());
    }

    public String getWireRange(String mpn) {
        ParsedMPN parsed = parseMPN(mpn);
        if (parsed == null) return "";

        return switch(parsed.series()) {
            case "282836", "282837" -> "26-14 AWG";
            case "282838" -> "24-12 AWG";
            case "282842" -> "22-10 AWG";
            case "284392" -> "N/A";  // Not applicable for edge headers
            default -> "";
        };
    }

    public double getRatedCurrent(String mpn) {
        ParsedMPN parsed = parseMPN(mpn);
        if (parsed == null) return 0.0;

        return switch(parsed.series()) {
            case "282836", "282837" -> 10.0;  // 10A
            case "282838" -> 15.0;            // 15A
            case "282842" -> 20.0;            // 20A
            case "284392" -> 3.0;             // 3A for edge headers
            default -> 0.0;
        };
    }
}