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
 * Handler for Cyntec power inductors and modules.
 *
 * Supported product series:
 * - PCMC series - Power inductors (PCMC063T-1R0MN)
 * - VCMD series - Molded power inductors (VCMD063T-2R2MN)
 * - MCPA series - Automotive power inductors (MCPA0504-1R0MN)
 * - CMC series - Common mode chokes (CMC0503-471M)
 *
 * MPN structure example: PCMC063T-1R0MN
 * - PCMC = Series (power inductor)
 * - 063 = Size code (6.3mm)
 * - T = Type/variant
 * - 1R0 = Inductance (1.0uH, R indicates decimal point)
 * - M = Tolerance (M=+/-20%, K=+/-10%)
 * - N = Packaging (N=tape & reel)
 *
 * Inductance encoding:
 * - 1R0 = 1.0uH (R indicates decimal point)
 * - 2R2 = 2.2uH
 * - 100 = 10uH (3-digit code: 10 x 10^0)
 * - 101 = 100uH (10 x 10^1)
 *
 * Size codes:
 * - 063 = 6.3mm
 * - 0504 = 5.0mm x 4.0mm
 * - 0403 = 4.0mm x 3.0mm
 */
public class CyntecHandler implements ManufacturerHandler {

    // Pattern to match PCMC power inductors
    // Groups: (1)series (2)size (3)type (4)value (5)tolerance+options
    private static final Pattern PCMC_PATTERN = Pattern.compile(
            "^(PCMC)(\\d{3,4})([A-Z]?)[-]?([0-9R]+)([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match VCMD molded power inductors
    private static final Pattern VCMD_PATTERN = Pattern.compile(
            "^(VCMD)(\\d{3,4})([A-Z]?)[-]?([0-9R]+)([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match MCPA automotive inductors
    private static final Pattern MCPA_PATTERN = Pattern.compile(
            "^(MCPA)(\\d{4})[-]?([0-9R]+)([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match CMC common mode chokes
    private static final Pattern CMC_PATTERN = Pattern.compile(
            "^(CMC)(\\d{4})[-]?([0-9]+)([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Series to package type mapping
    private static final Map<String, String> SERIES_PACKAGE_MAP = Map.of(
            "PCMC", "Power Inductor",
            "VCMD", "Molded Power Inductor",
            "MCPA", "Automotive Power Inductor",
            "CMC", "Common Mode Choke"
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // PCMC series - Power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^PCMC\\d{3,4}.*");
        registry.addPattern(ComponentType.IC, "^PCMC\\d{3,4}.*");

        // VCMD series - Molded power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^VCMD\\d{3,4}.*");
        registry.addPattern(ComponentType.IC, "^VCMD\\d{3,4}.*");

        // MCPA series - Automotive power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^MCPA\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^MCPA\\d{4}.*");

        // CMC series - Common mode chokes
        registry.addPattern(ComponentType.INDUCTOR, "^CMC\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^CMC\\d{4}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.INDUCTOR,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check if this is a Cyntec inductor
        if (type == ComponentType.INDUCTOR || type == ComponentType.IC) {
            if (matchesAnyPattern(upperMpn)) {
                return true;
            }
        }

        // Fall back to registry patterns
        return registry.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Checks if the MPN matches any Cyntec product pattern.
     */
    private boolean matchesAnyPattern(String mpn) {
        return PCMC_PATTERN.matcher(mpn).matches() ||
               VCMD_PATTERN.matcher(mpn).matches() ||
               MCPA_PATTERN.matcher(mpn).matches() ||
               CMC_PATTERN.matcher(mpn).matches() ||
               mpn.matches("^PCMC\\d{3,4}.*") ||
               mpn.matches("^VCMD\\d{3,4}.*") ||
               mpn.matches("^MCPA\\d{4}.*") ||
               mpn.matches("^CMC\\d{4}.*");
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

        m = PCMC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            String type = m.group(3);
            return m.group(1) + m.group(2) + (type != null && !type.isEmpty() ? type : "");
        }

        m = VCMD_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            String type = m.group(3);
            return m.group(1) + m.group(2) + (type != null && !type.isEmpty() ? type : "");
        }

        m = MCPA_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2);
        }

        m = CMC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2);
        }

        return "";
    }

    /**
     * Extracts the inductance value from the MPN in microhenries.
     *
     * Cyntec uses R-notation for decimal values:
     * - 1R0 = 1.0 uH (R indicates decimal point)
     * - 2R2 = 2.2 uH
     * - R47 = 0.47 uH
     *
     * Or 3-digit code for larger values:
     * - 100 = 10 uH (10 x 10^0)
     * - 101 = 100 uH (10 x 10^1)
     * - 471 = 470 uH (47 x 10^1)
     */
    public String extractInductanceValue(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String valueCode = null;

        // Extract value code from various patterns
        Matcher m;

        m = PCMC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            valueCode = m.group(4);
        }

        if (valueCode == null) {
            m = VCMD_PATTERN.matcher(upperMpn);
            if (m.matches()) {
                valueCode = m.group(4);
            }
        }

        if (valueCode == null) {
            m = MCPA_PATTERN.matcher(upperMpn);
            if (m.matches()) {
                valueCode = m.group(3);
            }
        }

        if (valueCode == null) {
            m = CMC_PATTERN.matcher(upperMpn);
            if (m.matches()) {
                valueCode = m.group(3);
            }
        }

        if (valueCode == null || valueCode.isEmpty()) {
            return "";
        }

        // Parse the value code
        return parseInductanceCode(valueCode);
    }

    /**
     * Parses the inductance code to a human-readable value.
     */
    private String parseInductanceCode(String code) {
        if (code == null || code.isEmpty()) return "";

        code = code.toUpperCase();

        // Handle R-notation for values < 10 uH (e.g., 1R0 = 1.0 uH, R47 = 0.47 uH)
        if (code.contains("R")) {
            try {
                if (code.startsWith("R")) {
                    // R47 = 0.47 uH
                    double value = Double.parseDouble("0." + code.substring(1));
                    return formatInductance(value);
                } else {
                    // 1R0 = 1.0 uH, 2R2 = 2.2 uH
                    String[] parts = code.split("R");
                    if (parts.length == 2) {
                        double value = Double.parseDouble(parts[0] + "." + parts[1]);
                        return formatInductance(value);
                    }
                }
            } catch (NumberFormatException e) {
                return "";
            }
        }

        // Standard 3-digit code (for larger values)
        if (code.length() >= 3 && code.matches("\\d{3}")) {
            try {
                int mantissa = Integer.parseInt(code.substring(0, 2));
                int exponent = Integer.parseInt(code.substring(2, 3));
                double microhenries = mantissa * Math.pow(10, exponent);
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

    /**
     * Extracts the size code from the MPN.
     *
     * Size codes:
     * - 063 = 6.3mm
     * - 0504 = 5.0mm x 4.0mm
     * - 0403 = 4.0mm x 3.0mm
     */
    public String extractSizeCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        Matcher m;

        m = PCMC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(2);
        }

        m = VCMD_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(2);
        }

        m = MCPA_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(2);
        }

        m = CMC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(2);
        }

        return "";
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

        if (mpn.matches("^PCMC\\d+.*")) return "PCMC";
        if (mpn.matches("^VCMD\\d+.*")) return "VCMD";
        if (mpn.matches("^MCPA\\d+.*")) return "MCPA";
        if (mpn.matches("^CMC\\d+.*")) return "CMC";

        return null;
    }
}
