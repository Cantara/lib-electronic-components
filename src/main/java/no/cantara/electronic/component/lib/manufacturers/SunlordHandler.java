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
 * Handler for Sunlord Electronics inductors and ferrite beads.
 *
 * Supported product series:
 * - SDCL series - Power inductors (SDCL1005C1R0MTDF)
 * - SWPA series - Power inductors (SWPA4020S100MT)
 * - SDFL series - Ferrite chip inductors (SDFL2012T1R0M3B)
 * - GZ series - Ferrite beads (GZ2012D601TF)
 *
 * MPN structure examples:
 *
 * SDCL1005C1R0MTDF:
 * - SDCL = Series (power inductor)
 * - 1005 = Package size (1.0mm x 0.5mm = 0402 imperial)
 * - C = Type/characteristics code
 * - 1R0 = Inductance (1.0uH - R indicates decimal point)
 * - M = Tolerance (M=+/-20%, K=+/-10%, J=+/-5%)
 * - T = Taping specification
 * - DF = Additional options
 *
 * SWPA4020S100MT:
 * - SWPA = Series (power inductor)
 * - 4020 = Package size (4.0mm x 2.0mm)
 * - S = Shielded
 * - 100 = Inductance (10uH - 2 digits + multiplier)
 * - M = Tolerance
 * - T = Taping
 *
 * GZ2012D601TF:
 * - GZ = Series (ferrite bead)
 * - 2012 = Package size (2.0mm x 1.2mm = 0805 imperial)
 * - D = Type code
 * - 601 = Impedance at 100MHz (600 ohm)
 * - T = Tolerance
 * - F = Packaging
 *
 * Inductance encoding:
 * - 1R0 = 1.0uH (R indicates decimal point position)
 * - 100 = 10uH (10 x 10^0)
 * - 101 = 100uH (10 x 10^1)
 * - R47 = 0.47uH
 */
public class SunlordHandler implements ManufacturerHandler {

    // Pattern to match SDCL power inductors
    // Groups: (1)series (2)size (3)type (4)value (5)tolerance+options
    private static final Pattern SDCL_PATTERN = Pattern.compile(
            "^(SDCL)(\\d{4})([A-Z]?)([0-9R]+)([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match SWPA power inductors
    // Groups: (1)series (2)size (3)type (4)value (5)tolerance+options
    private static final Pattern SWPA_PATTERN = Pattern.compile(
            "^(SWPA)(\\d{4})([A-Z]?)([0-9R]+)([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match SDFL ferrite chip inductors
    // Groups: (1)series (2)size (3)type (4)value (5)tolerance+options
    private static final Pattern SDFL_PATTERN = Pattern.compile(
            "^(SDFL)(\\d{4})([A-Z]?)([0-9R]+)([A-Z0-9]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match GZ ferrite beads
    // Groups: (1)series (2)size (3)type (4)impedance (5)tolerance+options
    private static final Pattern GZ_PATTERN = Pattern.compile(
            "^(GZ)(\\d{4})([A-Z]?)([0-9]{3})([A-Z]*)$",
            Pattern.CASE_INSENSITIVE);

    // Package size to imperial code mapping
    private static final Map<String, String> SIZE_TO_IMPERIAL_MAP = Map.ofEntries(
            Map.entry("1005", "0402"),
            Map.entry("1608", "0603"),
            Map.entry("2012", "0805"),
            Map.entry("3216", "1206"),
            Map.entry("3225", "1210"),
            Map.entry("4516", "1806"),
            Map.entry("4520", "1808"),
            Map.entry("4532", "1812"),
            Map.entry("5025", "2010"),
            Map.entry("6332", "2512"),
            // Power inductor sizes (metric)
            Map.entry("2520", "2520"),
            Map.entry("3015", "3015"),
            Map.entry("4020", "4020"),
            Map.entry("4030", "4030"),
            Map.entry("5020", "5020"),
            Map.entry("5030", "5030"),
            Map.entry("6020", "6020"),
            Map.entry("6030", "6030")
    );

    // Series to package type mapping
    private static final Map<String, String> SERIES_PACKAGE_MAP = Map.ofEntries(
            Map.entry("SDCL", "Power Inductor"),
            Map.entry("SWPA", "Power Inductor Shielded"),
            Map.entry("SDFL", "Ferrite Chip Inductor"),
            Map.entry("GZ", "Ferrite Bead")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // SDCL series - Power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SDCL\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^SDCL\\d{4}.*");

        // SWPA series - Power inductors (shielded)
        registry.addPattern(ComponentType.INDUCTOR, "^SWPA\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^SWPA\\d{4}.*");

        // SDFL series - Ferrite chip inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SDFL\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^SDFL\\d{4}.*");

        // GZ series - Ferrite beads
        registry.addPattern(ComponentType.INDUCTOR, "^GZ\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^GZ\\d{4}.*");
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

        // Check if this is a Sunlord inductor or ferrite bead
        if (type == ComponentType.INDUCTOR || type == ComponentType.IC) {
            if (matchesAnyPattern(upperMpn)) {
                return true;
            }
        }

        // Fall back to registry patterns
        return registry.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Checks if the MPN matches any Sunlord inductor/ferrite bead pattern.
     */
    private boolean matchesAnyPattern(String mpn) {
        return SDCL_PATTERN.matcher(mpn).matches() ||
               SWPA_PATTERN.matcher(mpn).matches() ||
               SDFL_PATTERN.matcher(mpn).matches() ||
               GZ_PATTERN.matcher(mpn).matches() ||
               mpn.matches("^SDCL\\d{4}.*") ||
               mpn.matches("^SWPA\\d{4}.*") ||
               mpn.matches("^SDFL\\d{4}.*") ||
               mpn.matches("^GZ\\d{4}.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String sizeCode = extractSizeCode(upperMpn);

        if (sizeCode != null && !sizeCode.isEmpty()) {
            // Return imperial code if available, otherwise return metric size
            return SIZE_TO_IMPERIAL_MAP.getOrDefault(sizeCode, sizeCode);
        }

        return "";
    }

    /**
     * Extracts the 4-digit metric size code from the MPN.
     */
    private String extractSizeCode(String mpn) {
        Matcher m;

        m = SDCL_PATTERN.matcher(mpn);
        if (m.matches()) return m.group(2);

        m = SWPA_PATTERN.matcher(mpn);
        if (m.matches()) return m.group(2);

        m = SDFL_PATTERN.matcher(mpn);
        if (m.matches()) return m.group(2);

        m = GZ_PATTERN.matcher(mpn);
        if (m.matches()) return m.group(2);

        // Fallback: try to extract 4 digits after series prefix
        if (mpn.matches("^(SDCL|SWPA|SDFL|GZ)\\d{4}.*")) {
            int start = mpn.startsWith("GZ") ? 2 : 4;
            return mpn.substring(start, start + 4);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Try to match various patterns and extract series + size
        Matcher m;

        m = SDCL_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "SDCL1005"
        }

        m = SWPA_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "SWPA4020"
        }

        m = SDFL_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "SDFL2012"
        }

        m = GZ_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "GZ2012"
        }

        // Fallback - extract series prefix + size
        if (upperMpn.matches("^SDCL\\d{4}.*")) {
            return "SDCL" + upperMpn.substring(4, 8);
        }
        if (upperMpn.matches("^SWPA\\d{4}.*")) {
            return "SWPA" + upperMpn.substring(4, 8);
        }
        if (upperMpn.matches("^SDFL\\d{4}.*")) {
            return "SDFL" + upperMpn.substring(4, 8);
        }
        if (upperMpn.matches("^GZ\\d{4}.*")) {
            return "GZ" + upperMpn.substring(2, 6);
        }

        return "";
    }

    /**
     * Extracts the inductance value from the MPN in microhenries.
     *
     * Sunlord uses a 3-character code where:
     * - 1R0 = 1.0uH (R indicates decimal point)
     * - R47 = 0.47uH (R at start indicates sub-1uH value)
     * - 100 = 10uH (10 x 10^0)
     * - 101 = 100uH (10 x 10^1)
     * - 102 = 1000uH = 1mH (10 x 10^2)
     */
    public String extractInductanceValue(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String valueCode = null;

        // Extract value code from various patterns
        Matcher m;

        m = SDCL_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            valueCode = m.group(4);
        }

        if (valueCode == null) {
            m = SWPA_PATTERN.matcher(upperMpn);
            if (m.matches()) {
                valueCode = m.group(4);
            }
        }

        if (valueCode == null) {
            m = SDFL_PATTERN.matcher(upperMpn);
            if (m.matches()) {
                valueCode = m.group(4);
            }
        }

        if (valueCode == null || valueCode.isEmpty()) {
            return "";
        }

        // Parse the value code
        return parseInductanceCode(valueCode);
    }

    /**
     * Extracts the impedance value from a ferrite bead MPN in ohms.
     *
     * The impedance code is a 3-digit value representing impedance at 100MHz.
     * - 601 = 600 ohm (60 x 10^1)
     * - 121 = 120 ohm (12 x 10^1)
     * - 102 = 1000 ohm (10 x 10^2)
     */
    public String extractImpedanceValue(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        Matcher m = GZ_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            String impedanceCode = m.group(4);
            return parseImpedanceCode(impedanceCode);
        }

        return "";
    }

    /**
     * Parses the inductance code to a human-readable value.
     */
    private String parseInductanceCode(String code) {
        if (code == null || code.isEmpty()) return "";

        code = code.toUpperCase();

        // Handle R-notation at start (e.g., R47 = 0.47uH)
        if (code.startsWith("R")) {
            try {
                double value = Double.parseDouble("0." + code.substring(1));
                return formatInductance(value);
            } catch (NumberFormatException e) {
                return "";
            }
        }

        // Handle R-notation in middle (e.g., 1R0 = 1.0uH, 2R2 = 2.2uH)
        if (code.contains("R")) {
            try {
                String normalized = code.replace("R", ".");
                double value = Double.parseDouble(normalized);
                return formatInductance(value);
            } catch (NumberFormatException e) {
                return "";
            }
        }

        // Standard 3-digit code (first 2 digits = value, 3rd digit = multiplier)
        // Example: 100 = 10 x 10^0 = 10uH, 101 = 10 x 10^1 = 100uH, 102 = 10 x 10^2 = 1000uH
        if (code.length() >= 3 && code.substring(0, 3).matches("\\d{3}")) {
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
     * Parses the 3-digit impedance code to ohms.
     */
    private String parseImpedanceCode(String code) {
        if (code == null || code.length() < 3) return "";

        try {
            int mantissa = Integer.parseInt(code.substring(0, 2));
            int exponent = Integer.parseInt(code.substring(2, 3));
            int ohms = (int) (mantissa * Math.pow(10, exponent));
            return ohms + " ohm";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    /**
     * Formats inductance value with appropriate unit.
     */
    private String formatInductance(double microhenries) {
        if (microhenries >= 1000) {
            return String.format("%.1fmH", microhenries / 1000);
        } else if (microhenries >= 1) {
            return String.format("%.1fuH", microhenries);
        } else if (microhenries >= 0.001) {
            return String.format("%.0fnH", microhenries * 1000);
        } else {
            return String.format("%.2fuH", microhenries);
        }
    }

    /**
     * Returns the type description based on series.
     */
    public String getSeriesType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("SDCL")) return "Power Inductor";
        if (upperMpn.startsWith("SWPA")) return "Power Inductor (Shielded)";
        if (upperMpn.startsWith("SDFL")) return "Ferrite Chip Inductor";
        if (upperMpn.startsWith("GZ")) return "Ferrite Bead";

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

        // For inductors, must have same inductance value
        String value1 = extractInductanceValue(mpn1);
        String value2 = extractInductanceValue(mpn2);

        if (!value1.isEmpty() && !value2.isEmpty()) {
            return value1.equals(value2);
        }

        // For ferrite beads, must have same impedance value
        String impedance1 = extractImpedanceValue(mpn1);
        String impedance2 = extractImpedanceValue(mpn2);

        if (!impedance1.isEmpty() && !impedance2.isEmpty()) {
            return impedance1.equals(impedance2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
