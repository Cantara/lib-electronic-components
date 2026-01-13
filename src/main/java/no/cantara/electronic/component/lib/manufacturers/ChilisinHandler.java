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
 * Handler for Chilisin Electronics inductors.
 *
 * Supported product series:
 * - SQC series - Chip inductors (SQC453226T-100M-N)
 * - MHCI series - Power inductors (MHCI0504-1R0M-R8)
 * - MHCC series - Coupled inductors
 * - CS series - Ferrite chip inductors
 *
 * MPN structure example: SQC453226T-100M-N
 * - SQC = Series (chip inductor)
 * - 4532 = Size code (4.5mm x 3.2mm)
 * - 26 = Height code
 * - T = Type/Shield code
 * - 100 = Inductance (10 x 10^0 uH = 10uH)
 * - M = Tolerance (M=+/-20%, K=+/-10%, J=+/-5%)
 * - N = Packaging code
 *
 * Inductance encoding:
 * - 1R0 = 1.0 uH (R indicates decimal point)
 * - 100 = 10 uH (first 2 digits = mantissa, 3rd = multiplier in 10^n uH)
 * - 101 = 100 uH
 * - R10 = 0.10 uH
 *
 * Package size codes:
 * - 0504 = 5mm x 4mm
 * - 4532 = 4.5mm x 3.2mm
 * - 2016 = 2.0mm x 1.6mm
 */
public class ChilisinHandler implements ManufacturerHandler {

    // Pattern to match SQC chip inductors
    // Groups: (1)series (2)size (3)height (4)type (5)value (6)tolerance (7)packaging
    private static final Pattern SQC_PATTERN = Pattern.compile(
            "^(SQC)(\\d{4})(\\d{2})([A-Z])?[-]?([0-9R]+)([A-Z])[-]?([A-Z0-9]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match MHCI power inductors
    // Groups: (1)series (2)size (3)value (4)tolerance (5)packaging
    private static final Pattern MHCI_PATTERN = Pattern.compile(
            "^(MHCI)(\\d{4})[-]?([0-9R]+)([A-Z])[-]?([A-Z0-9]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match MHCC coupled inductors
    // Groups: (1)series (2)size (3)value (4)tolerance (5)packaging
    private static final Pattern MHCC_PATTERN = Pattern.compile(
            "^(MHCC)(\\d{4})[-]?([0-9R]+)?([A-Z])?[-]?([A-Z0-9]*)$",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match CS ferrite chip inductors
    // Groups: (1)series (2)size (3)value (4)tolerance (5)packaging
    private static final Pattern CS_PATTERN = Pattern.compile(
            "^(CS)(\\d{4})[-]?([0-9R]+)?([A-Z])?[-]?([A-Z0-9]*)$",
            Pattern.CASE_INSENSITIVE);

    // Series to package type mapping
    private static final Map<String, String> SERIES_PACKAGE_MAP = Map.ofEntries(
            Map.entry("SQC", "Chip Inductor"),
            Map.entry("MHCI", "Power Inductor"),
            Map.entry("MHCC", "Coupled Inductor"),
            Map.entry("CS", "Ferrite Chip")
    );

    // Size code to package dimensions mapping
    private static final Map<String, String> SIZE_CODE_MAP = Map.ofEntries(
            Map.entry("0504", "5.0x4.0mm"),
            Map.entry("0403", "4.0x3.0mm"),
            Map.entry("0302", "3.0x2.0mm"),
            Map.entry("4532", "4.5x3.2mm"),
            Map.entry("3225", "3.2x2.5mm"),
            Map.entry("2520", "2.5x2.0mm"),
            Map.entry("2016", "2.0x1.6mm"),
            Map.entry("1608", "1.6x0.8mm"),
            Map.entry("1005", "1.0x0.5mm"),
            Map.entry("0603", "0603"),
            Map.entry("0402", "0402"),
            Map.entry("0201", "0201")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // SQC series - Chip inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SQC\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^SQC\\d{4}.*");

        // MHCI series - Power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^MHCI\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^MHCI\\d{4}.*");

        // MHCC series - Coupled inductors
        registry.addPattern(ComponentType.INDUCTOR, "^MHCC\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^MHCC\\d{4}.*");

        // CS series - Ferrite chip inductors
        registry.addPattern(ComponentType.INDUCTOR, "^CS\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^CS\\d{4}.*");
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

        // Check if this is a Chilisin inductor
        if (type == ComponentType.INDUCTOR || type == ComponentType.IC) {
            if (matchesAnyPattern(upperMpn)) {
                return true;
            }
        }

        // Fall back to registry patterns
        return registry.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Checks if the MPN matches any Chilisin inductor pattern.
     */
    private boolean matchesAnyPattern(String mpn) {
        return SQC_PATTERN.matcher(mpn).matches() ||
               MHCI_PATTERN.matcher(mpn).matches() ||
               MHCC_PATTERN.matcher(mpn).matches() ||
               CS_PATTERN.matcher(mpn).matches() ||
               mpn.matches("^SQC\\d{4}.*") ||
               mpn.matches("^MHCI\\d{4}.*") ||
               mpn.matches("^MHCC\\d{4}.*") ||
               mpn.matches("^CS\\d{4}.*");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract size code and map to dimensions
        String sizeCode = extractSizeCode(upperMpn);
        if (sizeCode != null && SIZE_CODE_MAP.containsKey(sizeCode)) {
            return SIZE_CODE_MAP.get(sizeCode);
        }

        // Return the raw size code if no mapping found
        if (sizeCode != null) {
            return sizeCode;
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Try to match various patterns and extract series + size
        Matcher m;

        m = SQC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "SQC4532"
        }

        m = MHCI_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "MHCI0504"
        }

        m = MHCC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "MHCC0504"
        }

        m = CS_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2); // e.g., "CS0402"
        }

        // Fallback - extract series prefix + size code
        if (upperMpn.matches("^SQC\\d{4}.*")) {
            return "SQC" + upperMpn.substring(3, 7);
        }
        if (upperMpn.matches("^MHCI\\d{4}.*")) {
            return "MHCI" + upperMpn.substring(4, 8);
        }
        if (upperMpn.matches("^MHCC\\d{4}.*")) {
            return "MHCC" + upperMpn.substring(4, 8);
        }
        if (upperMpn.matches("^CS\\d{4}.*")) {
            return "CS" + upperMpn.substring(2, 6);
        }

        return "";
    }

    /**
     * Extracts the inductance value from the MPN.
     *
     * Chilisin uses the following encoding:
     * - 1R0 = 1.0 uH (R indicates decimal point)
     * - R10 = 0.10 uH
     * - 100 = 10 uH (first 2 digits mantissa, 3rd digit is 10^n in uH)
     * - 101 = 100 uH
     * - 102 = 1000 uH = 1 mH
     */
    public String extractInductanceValue(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String valueCode = null;

        // Extract value code from various patterns
        Matcher m;

        m = SQC_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            valueCode = m.group(5);
        }

        m = MHCI_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            valueCode = m.group(3);
        }

        m = MHCC_PATTERN.matcher(upperMpn);
        if (m.matches() && m.group(3) != null) {
            valueCode = m.group(3);
        }

        m = CS_PATTERN.matcher(upperMpn);
        if (m.matches() && m.group(3) != null) {
            valueCode = m.group(3);
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

        // Handle R-notation at start (e.g., R10 = 0.10 uH = 100nH, R22 = 0.22 uH = 220nH)
        if (code.startsWith("R") && !code.substring(1).contains("R")) {
            try {
                String digits = code.substring(1);
                if (digits.length() >= 2) {
                    // R followed by 2 digits means 0.XX uH
                    double value = Double.parseDouble("0." + digits);
                    return formatInductance(value);
                }
            } catch (NumberFormatException e) {
                return "";
            }
        }

        // Handle R-notation in middle (e.g., 1R0 = 1.0 uH, 2R2 = 2.2 uH)
        if (code.contains("R")) {
            try {
                String value = code.replace("R", ".");
                double microhenries = Double.parseDouble(value);
                return formatInductance(microhenries);
            } catch (NumberFormatException e) {
                return "";
            }
        }

        // Standard 3-digit code (e.g., 100 = 10 uH, 101 = 100 uH)
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

        if (mpn.matches("^SQC\\d+.*")) return "SQC";
        if (mpn.matches("^MHCI\\d+.*")) return "MHCI";
        if (mpn.matches("^MHCC\\d+.*")) return "MHCC";
        if (mpn.matches("^CS\\d+.*")) return "CS";

        return null;
    }

    /**
     * Extracts the size code from the MPN.
     */
    private String extractSizeCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return null;

        Matcher m;

        m = SQC_PATTERN.matcher(mpn);
        if (m.matches()) {
            return m.group(2); // Size code for SQC
        }

        m = MHCI_PATTERN.matcher(mpn);
        if (m.matches()) {
            return m.group(2); // Size code for MHCI
        }

        m = MHCC_PATTERN.matcher(mpn);
        if (m.matches()) {
            return m.group(2); // Size code for MHCC
        }

        m = CS_PATTERN.matcher(mpn);
        if (m.matches()) {
            return m.group(2); // Size code for CS
        }

        // Fallback extraction
        if (mpn.matches("^SQC\\d{4}.*")) {
            return mpn.substring(3, 7);
        }
        if (mpn.matches("^MHCI\\d{4}.*")) {
            return mpn.substring(4, 8);
        }
        if (mpn.matches("^MHCC\\d{4}.*")) {
            return mpn.substring(4, 8);
        }
        if (mpn.matches("^CS\\d{4}.*")) {
            return mpn.substring(2, 6);
        }

        return null;
    }
}
