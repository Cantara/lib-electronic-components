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
 * Handler for Pulse Electronics - Magnetics and Power Components.
 *
 * Supported product series:
 *
 * Transformers:
 * - H series - LAN transformers (H1012, H1102, H1188)
 * - T series - Telecom transformers (T1029, T3012)
 * - PA series - Power adapters
 * - PE series - Power electronics transformers
 *
 * Inductors:
 * - P series - Power inductors (P0751, P1166)
 * - PA series - Automotive inductors
 * - PE series - SMD inductors
 *
 * LAN Magnetics (Connectors with integrated magnetics):
 * - J series - RJ45 with magnetics (JD0-0001, JD0-0005)
 * - JK series - RJ45 jacks
 * - JXD series - 10G LAN connectors
 *
 * MPN structure examples:
 * - H1012NL: H=LAN transformer, 1012=part number, NL=RoHS compliant/lead-free
 * - PE-53680NL: PE=power electronics, 53680=part number, NL=lead-free
 * - JD0-0001NL: JD0=J series LAN magnetics, 0001=variant, NL=lead-free
 * - P0751SNL: P=power inductor, 0751=part number, S=suffix, NL=lead-free
 * - T1029NL: T=telecom transformer, 1029=part number, NL=lead-free
 *
 * Common suffix meanings:
 * - NL: Lead-free / RoHS compliant
 * - NLT: Lead-free, tape and reel
 * - S: Standard variant
 * - G: Green/RoHS
 */
public class PulseElectronicsHandler implements ManufacturerHandler {

    // H series - LAN transformers (H followed by 4 digits)
    private static final Pattern H_SERIES_PATTERN = Pattern.compile(
            "^(H)(\\d{4})([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // T series - Telecom transformers (T followed by 4 digits)
    private static final Pattern T_SERIES_PATTERN = Pattern.compile(
            "^(T)(\\d{4})([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // PA series - Power adapters / automotive (PA- followed by digits)
    private static final Pattern PA_SERIES_PATTERN = Pattern.compile(
            "^(PA)[-]?(\\d+)([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // PE series - Power electronics (PE- followed by digits)
    private static final Pattern PE_SERIES_PATTERN = Pattern.compile(
            "^(PE)[-](\\d+)([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // P series - Power inductors (P followed by 4 digits)
    private static final Pattern P_SERIES_PATTERN = Pattern.compile(
            "^(P)(\\d{4})([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // J series - RJ45 with magnetics (JD0, JD1, etc. with dashes)
    private static final Pattern J_SERIES_PATTERN = Pattern.compile(
            "^(JD\\d)[-](\\d{4})([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // JK series - RJ45 jacks
    private static final Pattern JK_SERIES_PATTERN = Pattern.compile(
            "^(JK\\d?)[-]?(\\d+)([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // JXD series - 10G LAN connectors
    private static final Pattern JXD_SERIES_PATTERN = Pattern.compile(
            "^(JXD\\d?)[-]?(\\d+)([A-Z]*)$", Pattern.CASE_INSENSITIVE);

    // Series to product type description mapping
    private static final Map<String, String> SERIES_DESCRIPTION = Map.ofEntries(
            Map.entry("H", "LAN Transformer"),
            Map.entry("T", "Telecom Transformer"),
            Map.entry("PA", "Power Adapter"),
            Map.entry("PE", "Power Electronics"),
            Map.entry("P", "Power Inductor"),
            Map.entry("JD", "LAN Magnetics RJ45"),
            Map.entry("JK", "RJ45 Jack"),
            Map.entry("JXD", "10G LAN Connector")
    );

    // Package code mapping based on suffix
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("NL", "SMD Lead-Free"),
            Map.entry("NLT", "SMD Lead-Free T&R"),
            Map.entry("S", "Standard"),
            Map.entry("SNL", "Standard Lead-Free"),
            Map.entry("G", "Green/RoHS"),
            Map.entry("GNL", "Green Lead-Free"),
            Map.entry("T", "Tape & Reel"),
            Map.entry("TL", "Tape & Reel Lead-Free")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // H series - LAN transformers
        registry.addPattern(ComponentType.TRANSFORMER_BOURNS, "^H\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^H\\d{4}.*");

        // T series - Telecom transformers
        registry.addPattern(ComponentType.TRANSFORMER_BOURNS, "^T\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^T\\d{4}.*");

        // PA series - Power adapters / Automotive inductors
        registry.addPattern(ComponentType.INDUCTOR, "^PA[-]?\\d+.*");
        registry.addPattern(ComponentType.TRANSFORMER_BOURNS, "^PA[-]?\\d+.*");
        registry.addPattern(ComponentType.IC, "^PA[-]?\\d+.*");

        // PE series - Power electronics transformers / SMD inductors
        registry.addPattern(ComponentType.INDUCTOR, "^PE[-]\\d+.*");
        registry.addPattern(ComponentType.TRANSFORMER_BOURNS, "^PE[-]\\d+.*");
        registry.addPattern(ComponentType.IC, "^PE[-]\\d+.*");

        // P series - Power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^P\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^P\\d{4}.*");

        // J series - RJ45 with magnetics (LAN connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^JD\\d[-]\\d{4}.*");
        registry.addPattern(ComponentType.IC, "^JD\\d[-]\\d{4}.*");

        // JK series - RJ45 jacks
        registry.addPattern(ComponentType.CONNECTOR, "^JK\\d?[-]?\\d+.*");
        registry.addPattern(ComponentType.IC, "^JK\\d?[-]?\\d+.*");

        // JXD series - 10G LAN connectors
        registry.addPattern(ComponentType.CONNECTOR, "^JXD\\d?[-]?\\d+.*");
        registry.addPattern(ComponentType.IC, "^JXD\\d?[-]?\\d+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.INDUCTOR,
                ComponentType.TRANSFORMER_BOURNS,
                ComponentType.CONNECTOR,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check for transformer types (H, T, PA, PE series)
        if (type == ComponentType.TRANSFORMER_BOURNS || type == ComponentType.IC) {
            if (H_SERIES_PATTERN.matcher(upperMpn).matches() ||
                T_SERIES_PATTERN.matcher(upperMpn).matches() ||
                PA_SERIES_PATTERN.matcher(upperMpn).matches() ||
                PE_SERIES_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
        }

        // Check for inductor types (P, PA, PE series)
        if (type == ComponentType.INDUCTOR || type == ComponentType.IC) {
            if (P_SERIES_PATTERN.matcher(upperMpn).matches() ||
                PA_SERIES_PATTERN.matcher(upperMpn).matches() ||
                PE_SERIES_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
        }

        // Check for connector types (J, JK, JXD series - LAN magnetics)
        if (type == ComponentType.CONNECTOR || type == ComponentType.IC) {
            if (J_SERIES_PATTERN.matcher(upperMpn).matches() ||
                JK_SERIES_PATTERN.matcher(upperMpn).matches() ||
                JXD_SERIES_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
        }

        // Fall back to registry patterns for this handler only
        return registry.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String suffix = extractSuffix(upperMpn);

        // Map the suffix to a package description
        if (suffix != null && !suffix.isEmpty()) {
            // Check for known package codes
            for (Map.Entry<String, String> entry : PACKAGE_CODES.entrySet()) {
                if (suffix.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
            // Return the raw suffix if not in map
            return suffix;
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        Matcher m;

        // JXD series (check first - more specific)
        m = JXD_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "JXD";
        }

        // JK series
        m = JK_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "JK";
        }

        // J series (JD0, JD1, etc.)
        m = J_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "J";
        }

        // PE series (check before P)
        m = PE_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "PE";
        }

        // PA series (check before P)
        m = PA_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "PA";
        }

        // P series - Power inductors
        m = P_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "P";
        }

        // H series - LAN transformers
        m = H_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "H";
        }

        // T series - Telecom transformers
        m = T_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return "T";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract base part numbers (without suffix)
        String base1 = extractBasePartNumber(mpn1);
        String base2 = extractBasePartNumber(mpn2);

        // Must have same base part number
        if (base1.isEmpty() || !base1.equals(base2)) {
            return false;
        }

        // Different suffixes (like NL vs NLT) of the same part are replacements
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    /**
     * Extracts the suffix (package/options code) from the MPN.
     */
    private String extractSuffix(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        Matcher m;

        // Try each pattern and extract the suffix group
        Pattern[] patterns = {
                H_SERIES_PATTERN, T_SERIES_PATTERN, PA_SERIES_PATTERN,
                PE_SERIES_PATTERN, P_SERIES_PATTERN, J_SERIES_PATTERN,
                JK_SERIES_PATTERN, JXD_SERIES_PATTERN
        };

        for (Pattern pattern : patterns) {
            m = pattern.matcher(upperMpn);
            if (m.matches()) {
                // The suffix is always the last group
                String suffix = m.group(m.groupCount());
                return suffix != null ? suffix : "";
            }
        }

        return "";
    }

    /**
     * Extracts the base part number without suffix.
     * For example: H1012NL -> H1012, JD0-0001NL -> JD0-0001
     */
    private String extractBasePartNumber(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        Matcher m;

        // H series
        m = H_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2);
        }

        // T series
        m = T_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2);
        }

        // PA series
        m = PA_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + "-" + m.group(2);
        }

        // PE series
        m = PE_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + "-" + m.group(2);
        }

        // P series
        m = P_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + m.group(2);
        }

        // J series
        m = J_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + "-" + m.group(2);
        }

        // JK series
        m = JK_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + "-" + m.group(2);
        }

        // JXD series
        m = JXD_SERIES_PATTERN.matcher(upperMpn);
        if (m.matches()) {
            return m.group(1) + "-" + m.group(2);
        }

        return "";
    }

    /**
     * Gets the product type description for a given series.
     */
    public String getProductType(String mpn) {
        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        // Handle J series sub-types
        if (series.equals("J")) {
            return SERIES_DESCRIPTION.getOrDefault("JD", "LAN Magnetics");
        }

        return SERIES_DESCRIPTION.getOrDefault(series, "");
    }

    /**
     * Checks if the MPN represents a lead-free (RoHS compliant) part.
     */
    public boolean isLeadFree(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.contains("NL") || upperMpn.contains("G");
    }

    /**
     * Checks if the MPN represents a tape and reel variant.
     */
    public boolean isTapeAndReel(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.endsWith("NLT") || upperMpn.endsWith("T") || upperMpn.endsWith("TL");
    }

    /**
     * Determines if the MPN is a transformer (H, T, PA, PE series).
     */
    public boolean isTransformer(String mpn) {
        String series = extractSeries(mpn);
        return series.equals("H") || series.equals("T") ||
               series.equals("PA") || series.equals("PE");
    }

    /**
     * Determines if the MPN is an inductor (P, PA, PE series).
     */
    public boolean isInductor(String mpn) {
        String series = extractSeries(mpn);
        return series.equals("P") || series.equals("PA") || series.equals("PE");
    }

    /**
     * Determines if the MPN is a LAN magnetics/connector (J, JK, JXD series).
     */
    public boolean isLanMagnetics(String mpn) {
        String series = extractSeries(mpn);
        return series.equals("J") || series.equals("JK") || series.equals("JXD");
    }
}
