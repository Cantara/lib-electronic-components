package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Kingbright LED components.
 *
 * Kingbright is a major LED manufacturer producing:
 * - Through-hole LEDs (WP prefix - lead-free)
 * - SMD top-emitting LEDs (KP prefix)
 * - SMD right-angle LEDs (AP, APA prefix)
 * - SMD LEDs PLCC package (AA prefix)
 * - LED displays (KP prefix with display patterns)
 *
 * MPN Structure varies by series:
 *
 * Through-hole (WP series):
 * WP[size][color][variant] e.g., WP7113ID (5mm red LED)
 * - Size: 7113 = 5mm T-1 3/4, 3114 = 3mm T-1, 44 = 3mm flat
 * - Color: ID = red (InGaAlP), SGC = green (GaP), SBC = blue (InGaN)
 *
 * SMD Top-emitting (KP series):
 * KP-[size][color] e.g., KP-2012CGCK (0805 green)
 * - Size: 2012 = 0805, 1608 = 0603, 3216 = 1206
 * - Color: SRC = red, CGC = green, SBC = blue
 *
 * SMD Right-angle (AP series):
 * APT[size][color] or AP-[size][color] e.g., APTD3216SRCPRV
 * - Size: 3216 = 1206, 2012 = 0805
 * - Color: SRC = red, CGC = green, SBC = blue
 *
 * SMD PLCC (AA series):
 * AA[size][color] e.g., AA3528SURSK
 * - Size: 3528 = PLCC-4, 5060 = PLCC-6
 */
public class KingbrightHandler implements ManufacturerHandler {

    // Metric to Imperial package size mapping
    private static final Map<String, String> SIZE_CODE_MAP = Map.ofEntries(
            Map.entry("3216", "1206"),
            Map.entry("3528", "1411"),
            Map.entry("5060", "2024"),
            Map.entry("2012", "0805"),
            Map.entry("1608", "0603"),
            Map.entry("0603", "0201"),
            Map.entry("0805", "0302"),
            Map.entry("1005", "0402"),
            Map.entry("7113", "T-1-3/4"),  // 5mm through-hole
            Map.entry("3114", "T-1"),       // 3mm through-hole
            Map.entry("934", "T-1-3/4"),    // 5mm flat top
            Map.entry("44", "T-1"),         // 3mm flat top
            Map.entry("34", "T-1")          // 3mm
    );

    // Through-hole pattern: WP followed by size code and color
    private static final Pattern THROUGH_HOLE_PATTERN =
            Pattern.compile("^WP([0-9]{2,4})([A-Z]{2,4}).*", Pattern.CASE_INSENSITIVE);

    // SMD KP series pattern: KP- or KP followed by size and color
    private static final Pattern KP_SMD_PATTERN =
            Pattern.compile("^KP-?([0-9]{4})([A-Z]{2,4}).*", Pattern.CASE_INSENSITIVE);

    // SMD APT/APTD series pattern
    private static final Pattern APT_SMD_PATTERN =
            Pattern.compile("^APT[D]?([0-9]{4})([A-Z]{2,4}).*", Pattern.CASE_INSENSITIVE);

    // SMD AP- series pattern (with dash)
    private static final Pattern AP_SMD_PATTERN =
            Pattern.compile("^AP-?([0-9]{4})([A-Z]{2,4}).*", Pattern.CASE_INSENSITIVE);

    // SMD AA series pattern
    private static final Pattern AA_SMD_PATTERN =
            Pattern.compile("^AA([0-9]{4})([A-Z]{2,6}).*", Pattern.CASE_INSENSITIVE);

    // SMD KA series pattern
    private static final Pattern KA_SMD_PATTERN =
            Pattern.compile("^KA[A-Z]?-?([0-9]{4})([A-Z]{2,6}).*", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Through-hole LEDs (WP series - lead-free)
        registry.addPattern(ComponentType.LED, "^WP[0-9]{2,4}[A-Z].*");
        registry.addPattern(ComponentType.LED_STANDARD_KINGBRIGHT, "^WP[0-9]{2,4}[A-Z].*");

        // SMD top-emitting LEDs (KP series)
        registry.addPattern(ComponentType.LED, "^KP-?[0-9]{4}[A-Z].*");
        registry.addPattern(ComponentType.LED_SMD_KINGBRIGHT, "^KP-?[0-9]{4}[A-Z].*");

        // SMD right-angle LEDs (APT/APTD/AP series)
        registry.addPattern(ComponentType.LED, "^APT[D]?[0-9]{4}[A-Z].*");
        registry.addPattern(ComponentType.LED_SMD_KINGBRIGHT, "^APT[D]?[0-9]{4}[A-Z].*");
        registry.addPattern(ComponentType.LED, "^AP[0-9]{4}[A-Z].*");
        registry.addPattern(ComponentType.LED_SMD_KINGBRIGHT, "^AP[0-9]{4}[A-Z].*");

        // SMD PLCC LEDs (AA series)
        registry.addPattern(ComponentType.LED, "^AA[0-9]{4}[A-Z].*");
        registry.addPattern(ComponentType.LED_SMD_KINGBRIGHT, "^AA[0-9]{4}[A-Z].*");

        // SMD LEDs (KA series)
        registry.addPattern(ComponentType.LED, "^KA[A-Z]?-?[0-9]{4}[A-Z].*");
        registry.addPattern(ComponentType.LED_SMD_KINGBRIGHT, "^KA[A-Z]?-?[0-9]{4}[A-Z].*");

        // RGB LEDs (typically contain RGB or have special suffixes)
        registry.addPattern(ComponentType.LED_RGB_KINGBRIGHT, "^(?:WP|KP|AA|AP).*RGB.*");
        registry.addPattern(ComponentType.LED_RGB_KINGBRIGHT, "^(?:WP|KP|AA|AP).*RGBW.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.LED,
                ComponentType.LED_STANDARD_KINGBRIGHT,
                ComponentType.LED_SMD_KINGBRIGHT,
                ComponentType.LED_RGB_KINGBRIGHT
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // Through-hole LEDs (WP series)
        if (type == ComponentType.LED || type == ComponentType.LED_STANDARD_KINGBRIGHT) {
            if (upperMpn.matches("^WP[0-9]{2,4}[A-Z].*")) {
                return true;
            }
        }

        // SMD LEDs (KP, APT, AP, AA, KA series)
        if (type == ComponentType.LED || type == ComponentType.LED_SMD_KINGBRIGHT) {
            if (upperMpn.matches("^KP-?[0-9]{4}[A-Z].*") ||
                upperMpn.matches("^APT[D]?[0-9]{4}[A-Z].*") ||
                upperMpn.matches("^AP[0-9]{4}[A-Z].*") ||
                upperMpn.matches("^AA[0-9]{4}[A-Z].*") ||
                upperMpn.matches("^KA[A-Z]?-?[0-9]{4}[A-Z].*")) {
                return true;
            }
        }

        // RGB LEDs
        if (type == ComponentType.LED_RGB_KINGBRIGHT) {
            if (upperMpn.contains("RGB")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Through-hole LEDs (WP series)
        Matcher thMatcher = THROUGH_HOLE_PATTERN.matcher(upperMpn);
        if (thMatcher.matches()) {
            String sizeCode = thMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(sizeCode, "T-1-3/4"); // Default 5mm
        }

        // SMD KP series
        Matcher kpMatcher = KP_SMD_PATTERN.matcher(upperMpn);
        if (kpMatcher.matches()) {
            String metricSize = kpMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(metricSize, metricSize);
        }

        // SMD APT series
        Matcher aptMatcher = APT_SMD_PATTERN.matcher(upperMpn);
        if (aptMatcher.matches()) {
            String metricSize = aptMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(metricSize, metricSize);
        }

        // SMD AP series
        Matcher apMatcher = AP_SMD_PATTERN.matcher(upperMpn);
        if (apMatcher.matches()) {
            String metricSize = apMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(metricSize, metricSize);
        }

        // SMD AA series
        Matcher aaMatcher = AA_SMD_PATTERN.matcher(upperMpn);
        if (aaMatcher.matches()) {
            String metricSize = aaMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(metricSize, "PLCC-" + metricSize);
        }

        // SMD KA series
        Matcher kaMatcher = KA_SMD_PATTERN.matcher(upperMpn);
        if (kaMatcher.matches()) {
            String metricSize = kaMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(metricSize, metricSize);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Through-hole LEDs (WP series) - return WP + size code
        Matcher thMatcher = THROUGH_HOLE_PATTERN.matcher(upperMpn);
        if (thMatcher.matches()) {
            return "WP" + thMatcher.group(1);
        }

        // SMD KP series - return KP-size
        Matcher kpMatcher = KP_SMD_PATTERN.matcher(upperMpn);
        if (kpMatcher.matches()) {
            return "KP-" + kpMatcher.group(1);
        }

        // SMD APT series - return APT/APTD + size
        Matcher aptMatcher = APT_SMD_PATTERN.matcher(upperMpn);
        if (aptMatcher.matches()) {
            // Preserve APTD vs APT distinction
            String prefix = upperMpn.startsWith("APTD") ? "APTD" : "APT";
            return prefix + aptMatcher.group(1);
        }

        // SMD AP series
        Matcher apMatcher = AP_SMD_PATTERN.matcher(upperMpn);
        if (apMatcher.matches()) {
            return "AP" + apMatcher.group(1);
        }

        // SMD AA series
        Matcher aaMatcher = AA_SMD_PATTERN.matcher(upperMpn);
        if (aaMatcher.matches()) {
            return "AA" + aaMatcher.group(1);
        }

        // SMD KA series
        Matcher kaMatcher = KA_SMD_PATTERN.matcher(upperMpn);
        if (kaMatcher.matches()) {
            return "KA" + kaMatcher.group(1);
        }

        // Fallback: return prefix up to first non-alphanumeric
        int idx = 0;
        while (idx < upperMpn.length() && (Character.isLetterOrDigit(upperMpn.charAt(idx)) || upperMpn.charAt(idx) == '-')) {
            idx++;
        }
        return upperMpn.substring(0, Math.min(idx, upperMpn.length()));
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return false;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Same package required for LED replacement
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        return pkg1.equals(pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Extracts the color code from a Kingbright LED MPN.
     *
     * Common color codes:
     * - ID, SRC, SRCPRV = Red (InGaAlP)
     * - SGC, CGC = Green (GaP or InGaN)
     * - SBC, SBCPRV = Blue (InGaN)
     * - SEC, YGC = Yellow/Amber
     * - SURSK, SURSCK = Super bright red
     *
     * @param mpn the manufacturer part number
     * @return the color description or empty string
     */
    public String extractColor(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Common color code patterns
        if (upperMpn.contains("SRC") || upperMpn.contains("SURSK") ||
            upperMpn.contains("ID") || upperMpn.contains("SURSCK")) {
            return "Red";
        }
        if (upperMpn.contains("SGC") || upperMpn.contains("CGC") ||
            upperMpn.contains("GC")) {
            return "Green";
        }
        if (upperMpn.contains("SBC") || upperMpn.contains("BC")) {
            return "Blue";
        }
        if (upperMpn.contains("SEC") || upperMpn.contains("YGC") ||
            upperMpn.contains("YC")) {
            return "Yellow";
        }
        if (upperMpn.contains("RGB")) {
            return "RGB";
        }
        if (upperMpn.contains("WC") || upperMpn.contains("SWC")) {
            return "White";
        }

        return "";
    }

    /**
     * Determines if an LED is through-hole (leaded) or SMD.
     *
     * @param mpn the manufacturer part number
     * @return "Through-hole" or "SMD" or empty string
     */
    public String getMountingType(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("WP")) {
            return "Through-hole";
        }
        if (upperMpn.startsWith("KP") || upperMpn.startsWith("AP") ||
            upperMpn.startsWith("AA") || upperMpn.startsWith("KA")) {
            return "SMD";
        }

        return "";
    }
}
