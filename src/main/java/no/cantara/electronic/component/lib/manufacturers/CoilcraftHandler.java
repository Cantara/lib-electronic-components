package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Coilcraft power inductors.
 *
 * Supported product series:
 * - XAL/XEL series - High current power inductors (XAL4020, XAL5030, XEL4030)
 * - XFL series - Low profile power inductors (XFL2006, XFL3012, XFL4020)
 * - SER series - High efficiency power inductors (SER1360, SER2010)
 * - LPS series - Low profile shielded (LPS3015, LPS4018)
 * - MSS series - Magnetically shielded (MSS1038, MSS1048)
 * - DO series - Unshielded drum core (DO1608C, DO3316P)
 * - MSD series - Mid-size drum (MSD1260)
 * - SLC/SLR series - High Q chip inductors
 * - 0402HP/0603HP - High performance chip inductors
 *
 * MPN structure example: XAL4020-222ME
 * - XAL = Series (shielded power inductor)
 * - 4020 = Size code (4.0mm x 2.0mm)
 * - 222 = Inductance (2.2uH, 3-digit code: mantissa + multiplier)
 * - M = Tolerance (M=+/-20%, L=+/-15%, K=+/-10%)
 * - E = Packaging (E=embossed tape, B=bulk)
 */
public class CoilcraftHandler implements ManufacturerHandler {

    // Pattern to match Coilcraft inductor MPNs
    // Groups: (1)series (2)size (3)value (4)tolerance+options
    // More permissive suffix matching to handle variants like MRB, MEB, etc.
    private static final Pattern XAL_XEL_PATTERN = Pattern.compile(
            "^(XA[LT]|XEL)(\\d{4})[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern XFL_PATTERN = Pattern.compile(
            "^(XFL)(\\d{4})[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern SER_PATTERN = Pattern.compile(
            "^(SER)(\\d{4})[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern LPS_PATTERN = Pattern.compile(
            "^(LPS)(\\d{4})[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern MSS_PATTERN = Pattern.compile(
            "^(MSS)(\\d{4})[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern DO_PATTERN = Pattern.compile(
            "^(DO)(\\d{4})([CPT]?)[-]?(\\d{3}|R\\d{2})?([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern MSD_PATTERN = Pattern.compile(
            "^(MSD)(\\d{4})[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern SLC_SLR_PATTERN = Pattern.compile(
            "^(SL[CR])(\\d{4})[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern HP_PATTERN = Pattern.compile(
            "^(0[46]0[23]HP)[-]?(\\d{3}|R\\d{2})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Series to package type mapping
    private static final Map<String, String> SERIES_PACKAGE_MAP = Map.ofEntries(
            Map.entry("XAL", "Shielded Power"),
            Map.entry("XAT", "Shielded Power"),
            Map.entry("XEL", "Shielded Low DCR"),
            Map.entry("XFL", "Low Profile"),
            Map.entry("SER", "High Efficiency"),
            Map.entry("LPS", "Low Profile Shielded"),
            Map.entry("MSS", "Magnetically Shielded"),
            Map.entry("DO", "Drum Core"),
            Map.entry("MSD", "Mid-Size Drum"),
            Map.entry("SLC", "High Q Chip"),
            Map.entry("SLR", "High Q Chip RF"),
            Map.entry("0402HP", "0402 High Performance"),
            Map.entry("0603HP", "0603 High Performance")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // XAL/XEL series - High current power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^XA[LT]\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^XA[LT]\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR, "^XEL\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^XEL\\d{4}.*");

        // XFL series - Low profile power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^XFL\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^XFL\\d{4}.*");

        // SER series - High efficiency power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SER\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^SER\\d{4}.*");

        // LPS series - Low profile shielded
        registry.addPattern(ComponentType.INDUCTOR, "^LPS\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^LPS\\d{4}.*");

        // MSS series - Magnetically shielded
        registry.addPattern(ComponentType.INDUCTOR, "^MSS\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^MSS\\d{4}.*");

        // DO series - Unshielded drum core
        registry.addPattern(ComponentType.INDUCTOR, "^DO\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^DO\\d{4}.*");

        // MSD series - Mid-size drum
        registry.addPattern(ComponentType.INDUCTOR, "^MSD\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^MSD\\d{4}.*");

        // SLC/SLR series - High Q chip inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SL[CR]\\d{4}.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^SL[CR]\\d{4}.*");

        // 0402HP/0603HP - High performance chip inductors
        registry.addPattern(ComponentType.INDUCTOR, "^0[46]0[23]HP.*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_COILCRAFT, "^0[46]0[23]HP.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.INDUCTOR,
                ComponentType.INDUCTOR_CHIP_COILCRAFT
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check if this is a Coilcraft inductor
        if (type == ComponentType.INDUCTOR || type == ComponentType.INDUCTOR_CHIP_COILCRAFT) {
            if (matchesAnyPattern(upperMpn)) {
                return true;
            }
        }

        // Fall back to registry patterns
        return registry.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Checks if the MPN matches any Coilcraft inductor pattern.
     */
    private boolean matchesAnyPattern(String mpn) {
        return XAL_XEL_PATTERN.matcher(mpn).matches() ||
               XFL_PATTERN.matcher(mpn).matches() ||
               SER_PATTERN.matcher(mpn).matches() ||
               LPS_PATTERN.matcher(mpn).matches() ||
               MSS_PATTERN.matcher(mpn).matches() ||
               DO_PATTERN.matcher(mpn).matches() ||
               MSD_PATTERN.matcher(mpn).matches() ||
               SLC_SLR_PATTERN.matcher(mpn).matches() ||
               HP_PATTERN.matcher(mpn).matches() ||
               mpn.matches("^XA[LT]\\d{4}.*") ||
               mpn.matches("^XEL\\d{4}.*") ||
               mpn.matches("^XFL\\d{4}.*") ||
               mpn.matches("^SER\\d{4}.*") ||
               mpn.matches("^LPS\\d{4}.*") ||
               mpn.matches("^MSS\\d{4}.*") ||
               mpn.matches("^DO\\d{4}.*") ||
               mpn.matches("^MSD\\d{4}.*") ||
               mpn.matches("^SL[CR]\\d{4}.*") ||
               mpn.matches("^0[46]0[23]HP.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract series prefix and map to package type
        String series = extractSeriesPrefix(upperMpn);
        if (series != null && SERIES_PACKAGE_MAP.containsKey(series)) {
            return SERIES_PACKAGE_MAP.get(series);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Try to match various patterns and extract series + size
        Matcher m;

        m = XAL_XEL_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "XAL4020"
        }

        m = XFL_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "XFL4020"
        }

        m = SER_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "SER2010"
        }

        m = LPS_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "LPS4018"
        }

        m = MSS_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "MSS1048"
        }

        m = DO_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2) + (m.group(3) != null ? m.group(3) : ""); // e.g., "DO3316P"
        }

        m = MSD_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "MSD1260"
        }

        m = SLC_SLR_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "SLC0806"
        }

        m = HP_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1); // e.g., "0402HP"
        }

        // Fallback - extract first part before hyphen or value code
        if (upperMpn.matches("^[A-Z]{2,3}\\d{4}.*")) {
            return upperMpn.substring(0, upperMpn.indexOf('4') + 4).replaceAll("-.*", "");
        }

        return "";
    }

    /**
     * Extracts the inductance value from the MPN in microhenries.
     *
     * Coilcraft uses a 3-digit code where:
     * - First two digits are significant figures
     * - Third digit is the multiplier (power of 10 in nH)
     *
     * Examples:
     * - 222 = 22 x 10^2 nH = 2200 nH = 2.2 uH
     * - 472 = 47 x 10^2 nH = 4700 nH = 4.7 uH
     * - R47 = 0.47 uH (R indicates decimal point)
     * - 100 = 10 x 10^0 nH = 10 nH = 0.01 uH
     */
    public String extractInductanceValue(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String valueCode = null;

        // Extract value code from various patterns
        Matcher[] matchers = {
                XAL_XEL_PATTERN.matcher(upperMpn),
                XFL_PATTERN.matcher(upperMpn),
                SER_PATTERN.matcher(upperMpn),
                LPS_PATTERN.matcher(upperMpn),
                MSS_PATTERN.matcher(upperMpn),
                MSD_PATTERN.matcher(upperMpn),
                SLC_SLR_PATTERN.matcher(upperMpn)
        };

        for (Matcher m : matchers) {
            if (m.matches()) {
                valueCode = m.group(3);
                break;
            }
        }

        // DO pattern has different group structure
        Matcher doMatcher = DO_PATTERN.matcher(upperMpn);
        if (doMatcher.matches() && doMatcher.group(4) != null) {
            valueCode = doMatcher.group(4);
        }

        // HP pattern
        Matcher hpMatcher = HP_PATTERN.matcher(upperMpn);
        if (hpMatcher.matches()) {
            valueCode = hpMatcher.group(2);
        }

        if (valueCode == null || valueCode.isEmpty()) {
            return "";
        }

        // Parse the value code
        return parseInductanceCode(valueCode);
    }

    /**
     * Parses the 3-digit inductance code to a human-readable value.
     */
    private String parseInductanceCode(String code) {
        if (code == null || code.isEmpty()) return "";

        code = code.toUpperCase();

        // Handle R-notation (e.g., R47 = 0.47 uH)
        if (code.startsWith("R")) {
            try {
                double value = Double.parseDouble("0." + code.substring(1));
                return formatInductance(value);
            } catch (NumberFormatException e) {
                return "";
            }
        }

        // Standard 3-digit code
        if (code.length() >= 3 && code.matches("\\d{3}")) {
            try {
                int mantissa = Integer.parseInt(code.substring(0, 2));
                int exponent = Integer.parseInt(code.substring(2, 3));
                double nanohenries = mantissa * Math.pow(10, exponent);
                double microhenries = nanohenries / 1000.0;
                return formatInductance(microhenries);
            } catch (NumberFormatException e) {
                return "";
            }
        }

        return "";
    }

    /**
     * Formats inductance value with appropriate unit.
     */
    private String formatInductance(double microhenries) {
        if (microhenries >= 1000) {
            return String.format("%.1fmH", microhenries / 1000);
        } else if (microhenries >= 1) {
            return String.format("%.1fuH", microhenries);
        } else {
            return String.format("%.0fnH", microhenries * 1000);
        }
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series (including size)
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Must have same inductance value
        String value1 = extractInductanceValue(mpn1);
        String value2 = extractInductanceValue(mpn2);

        return !value1.isEmpty() && value1.equals(value2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Extracts just the series prefix (without size code) from an MPN.
     */
    private String extractSeriesPrefix(String mpn) {
        if (mpn == null || mpn.isEmpty()) return null;

        // HP series pattern
        if (mpn.matches("^0[46]0[23]HP.*")) {
            return mpn.substring(0, 6);
        }

        // Standard series prefixes (2-3 letters)
        if (mpn.matches("^XA[LT]\\d+.*")) return mpn.substring(0, 3);
        if (mpn.matches("^XEL\\d+.*")) return "XEL";
        if (mpn.matches("^XFL\\d+.*")) return "XFL";
        if (mpn.matches("^SER\\d+.*")) return "SER";
        if (mpn.matches("^LPS\\d+.*")) return "LPS";
        if (mpn.matches("^MSS\\d+.*")) return "MSS";
        if (mpn.matches("^DO\\d+.*")) return "DO";
        if (mpn.matches("^MSD\\d+.*")) return "MSD";
        if (mpn.matches("^SL[CR]\\d+.*")) return mpn.substring(0, 3);

        return null;
    }
}
