package no.cantara.electronic.component.connectors;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class WurthHeaderHandler implements ConnectorHandler {
    // Constants for similarity calculation
    private static final double SAME_SERIES_SCORE = 0.2;  // Base score for same series
    private static final double SAME_PINS_SCORE = 0.2;    // Same pin count
    private static final double SAME_MOUNTING_SCORE = 0.1; // Same mounting type
    private static final double SAME_PITCH_SCORE = 0.2;   // Same pitch
    private static final double SAME_VARIANT_SCORE = 0.3;  // Same variant

    // Changed pattern to correctly group all parts of the part number
    private static final Pattern HEADER_PATTERN = Pattern.compile("(61[0-9]{2})(\\d{2})(\\d{2})(\\d{2})(\\d)");

    // Pitch mapping (series to pitch in mm)
    private static final Map<String, String> SERIES_PITCH = Map.ofEntries(
            new SimpleEntry<>("61300", "2.54"),  // Standard 0.1" header
            new SimpleEntry<>("61301", "2.00"),  // 2mm header
            new SimpleEntry<>("61302", "1.27"),  // 1.27mm header
            new SimpleEntry<>("61303", "1.00")   // 1mm header
    );

    // Known series families
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("61300", "WR-PHD 2.54mm"),
            new SimpleEntry<>("61301", "WR-PHD 2.00mm"),
            new SimpleEntry<>("61302", "WR-PHD 1.27mm"),
            new SimpleEntry<>("61303", "WR-PHD 1.00mm")
    );


    @Override
    public String getFamily() {
        return "WR-PHD";
    }

    @Override
    public int getPinCount(String mpn) {
        if (mpn == null) return 0;

        Matcher m = HEADER_PATTERN.matcher(mpn);
        if (m.matches()) {
            try {
                return Integer.parseInt(m.group(2));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public String getPitch(String mpn) {
        if (mpn == null) return "";

        Matcher m = HEADER_PATTERN.matcher(mpn);
        if (m.matches()) {
            String series = m.group(1);  // Now correctly gets "61300"
            return SERIES_PITCH.getOrDefault(series, "");
        }
        return "";
    }

    @Override
    public String getMountingType(String mpn) {
        if (mpn == null) return "";

        Matcher m = HEADER_PATTERN.matcher(mpn);
        if (m.matches()) {
            String mountCode = m.group(4);
            return switch(mountCode) {
                case "1" -> "THT";
                case "2" -> "SMD";
                case "3" -> "Press-Fit";
                case "4" -> "SMD/THT Combo";
                default -> "";
            };
        }
        return "";
    }

    @Override
    public String getVariant(String mpn) {
        if (mpn == null) return "";

        Matcher m = HEADER_PATTERN.matcher(mpn);
        if (m.matches()) {
            return m.group(4);  // Last digit is variant code
        }
        return "";
    }

    @Override
    public boolean areCompatible(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        Matcher m1 = HEADER_PATTERN.matcher(mpn1);
        Matcher m2 = HEADER_PATTERN.matcher(mpn2);

        if (m1.matches() && m2.matches()) {
            String series1 = m1.group(1);
            String pins1 = m1.group(2);

            String series2 = m2.group(1);
            String pins2 = m2.group(2);

            boolean sameSeries = series1.equals(series2);
            boolean samePins = pins1.equals(pins2);

            return sameSeries && samePins;
        }
        return false;
    }

    @Override
    public String getSeriesName(String mpn) {
        if (mpn == null) return "";

        Matcher m = HEADER_PATTERN.matcher(mpn);
        if (m.matches()) {
            String series = m.group(1);
            return SERIES_FAMILIES.getOrDefault(series, "Unknown");
        }
        return "";
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        Matcher m1 = HEADER_PATTERN.matcher(mpn1);
        Matcher m2 = HEADER_PATTERN.matcher(mpn2);

        if (!m1.matches() || !m2.matches()) return 0.0;

        double similarity = 0.0;

        // Check series match (base similarity)
        String series1 = m1.group(1);
        String series2 = m2.group(1);
        if (series1.equals(series2)) {
            similarity += SAME_SERIES_SCORE;

            // Check pin count
            String pins1 = m1.group(2);
            String pins2 = m2.group(2);
            if (pins1.equals(pins2)) {
                similarity += SAME_PINS_SCORE;
            }

            // Check mounting type
            String mount1 = m1.group(4);
            String mount2 = m2.group(4);
            if (mount1.equals(mount2)) {
                similarity += SAME_MOUNTING_SCORE;
            }

            // Same pitch (implicit in same series)
            similarity += SAME_PITCH_SCORE;

            // Check variant (row configuration)
            String variant1 = m1.group(3);
            String variant2 = m2.group(3);
            if (variant1.equals(variant2)) {
                similarity += SAME_VARIANT_SCORE;
            }
        }

        return similarity;
    }
}