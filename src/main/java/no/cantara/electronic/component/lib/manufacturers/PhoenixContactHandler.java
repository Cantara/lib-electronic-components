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
 * Handler for Phoenix Contact industrial connectors.
 *
 * Supported product families:
 * - COMBICON PCB connectors:
 *   - MC/MCV series - Pluggable connectors (MC 1,5/, MCV 1,5/)
 *   - MSTB/MSTBA - Standard pitch pluggable
 *   - FK-MCP - High current connectors
 *   - PC series - PCB terminal blocks
 *   - FRONT-MC - Front connectors
 * - PT series - Push-in terminal blocks
 * - UT series - Through-wall terminal blocks
 * - UK series - Screw terminal blocks
 * - PTSM - SMD terminal blocks
 * - SPT series - Spring-cage PCB connectors
 *
 * Pattern examples:
 * - MC 1,5/3-ST-3,81 (COMBICON pluggable, 3-pin, 3.81mm pitch)
 * - MCV 1,5/4-G-3,81 (COMBICON header, 4-pin, 3.81mm pitch)
 * - MSTB 2,5/4-ST-5,08 (COMBICON pluggable, 4-pin, 5.08mm pitch)
 * - PT 1,5/2-PH-3,5 (Push-in terminal, 2-pin, 3.5mm pitch)
 * - PTSM 0,5/3-HH-2,5-SMD (SMD terminal, 3-pin, 2.5mm pitch)
 * - UK 5-TWIN (Screw terminal block)
 * - UT 2,5-MTD (Through-wall terminal)
 * - SPT 2,5/3-V-5,0 (Spring-cage, 3-pin, 5.0mm pitch)
 */
public class PhoenixContactHandler implements ManufacturerHandler {

    // Pattern for MC/MCV series: MC 1,5/3-ST-3,81 or MCV 1,5/4-G-3,81
    private static final Pattern MC_MCV_PATTERN = Pattern.compile(
            "^(MCV?)[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*).*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for MSTB/MSTBA series: MSTB 2,5/4-ST-5,08 or MSTBA 2,5/6-G-5,08
    private static final Pattern MSTB_PATTERN = Pattern.compile(
            "^(MSTBA?)[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*).*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for PT series: PT 1,5/2-PH-3,5
    private static final Pattern PT_PATTERN = Pattern.compile(
            "^PT[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*).*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for PTSM series: PTSM 0,5/3-HH-2,5-SMD
    private static final Pattern PTSM_PATTERN = Pattern.compile(
            "^PTSM[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*)(-SMD)?.*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for SPT series: SPT 2,5/3-V-5,0
    private static final Pattern SPT_PATTERN = Pattern.compile(
            "^SPT[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*).*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for UK series: UK 5-TWIN, UK 2,5 N, UK 3-TWIN
    private static final Pattern UK_PATTERN = Pattern.compile(
            "^UK[- ]([0-9]+[,.]?[0-9]*)(-?[A-Z0-9]+)?.*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for UT series: UT 2,5-MTD, UT 4-TWIN
    private static final Pattern UT_PATTERN = Pattern.compile(
            "^UT[- ]([0-9]+[,.]?[0-9]*)(-?[A-Z0-9]+)?.*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for FK-MCP series: FK-MCP 1,5/4-ST-3,5
    private static final Pattern FK_MCP_PATTERN = Pattern.compile(
            "^FK-?MCP[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*).*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for FRONT-MC series: FRONT-MC 1,5/6-ST-3,81
    private static final Pattern FRONT_MC_PATTERN = Pattern.compile(
            "^FRONT-?MC[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*).*",
            Pattern.CASE_INSENSITIVE);

    // Pattern for PC series: PC 5/3-STF-7,62
    private static final Pattern PC_PATTERN = Pattern.compile(
            "^PC[- ]([0-9]+[,.]?[0-9]*)/([0-9]+)-([A-Z]+)-([0-9]+[,.]?[0-9]*).*",
            Pattern.CASE_INSENSITIVE);

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            Map.entry("MC", "COMBICON Pluggable"),
            Map.entry("MCV", "COMBICON Header"),
            Map.entry("MSTB", "COMBICON Standard Pluggable"),
            Map.entry("MSTBA", "COMBICON Standard Header"),
            Map.entry("PT", "Push-In Terminal Block"),
            Map.entry("PTSM", "SMD Terminal Block"),
            Map.entry("SPT", "Spring-Cage PCB Connector"),
            Map.entry("UK", "Screw Terminal Block"),
            Map.entry("UT", "Through-Wall Terminal"),
            Map.entry("FK-MCP", "High Current Connector"),
            Map.entry("FRONT-MC", "Front Connector"),
            Map.entry("PC", "PCB Terminal Block")
    );

    // Series to typical pitch mapping (in mm)
    private static final Map<String, String> DEFAULT_PITCH = Map.ofEntries(
            Map.entry("MC", "3.81"),
            Map.entry("MCV", "3.81"),
            Map.entry("MSTB", "5.08"),
            Map.entry("MSTBA", "5.08"),
            Map.entry("PT", "3.50"),
            Map.entry("PTSM", "2.50"),
            Map.entry("SPT", "5.00"),
            Map.entry("UK", "5.08"),
            Map.entry("UT", "6.20"),
            Map.entry("FK-MCP", "3.50"),
            Map.entry("FRONT-MC", "3.81"),
            Map.entry("PC", "7.62")
    );

    // Series to rated current mapping (in Amperes)
    private static final Map<String, Double> SERIES_CURRENT = Map.ofEntries(
            Map.entry("MC", 8.0),
            Map.entry("MCV", 8.0),
            Map.entry("MSTB", 12.0),
            Map.entry("MSTBA", 12.0),
            Map.entry("PT", 17.5),
            Map.entry("PTSM", 6.0),
            Map.entry("SPT", 24.0),
            Map.entry("UK", 32.0),
            Map.entry("UT", 32.0),
            Map.entry("FK-MCP", 12.0),
            Map.entry("FRONT-MC", 8.0),
            Map.entry("PC", 24.0)
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.CONNECTOR,
                ComponentType.IC
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MC/MCV series (COMBICON pluggable/header)
        registry.addPattern(ComponentType.CONNECTOR, "^MCV?[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");

        // MSTB/MSTBA series (standard pitch pluggable)
        registry.addPattern(ComponentType.CONNECTOR, "^MSTBA?[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");

        // PT series (push-in terminal blocks)
        registry.addPattern(ComponentType.CONNECTOR, "^PT[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");

        // PTSM series (SMD terminal blocks)
        registry.addPattern(ComponentType.CONNECTOR, "^PTSM[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");

        // SPT series (spring-cage PCB connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^SPT[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");

        // UK series (screw terminal blocks)
        registry.addPattern(ComponentType.CONNECTOR, "^UK[- ][0-9]+[,.]?[0-9]*.*");

        // UT series (through-wall terminal blocks)
        registry.addPattern(ComponentType.CONNECTOR, "^UT[- ][0-9]+[,.]?[0-9]*.*");

        // FK-MCP series (high current connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^FK-?MCP[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");

        // FRONT-MC series (front connectors)
        registry.addPattern(ComponentType.CONNECTOR, "^FRONT-?MC[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");

        // PC series (PCB terminal blocks)
        registry.addPattern(ComponentType.CONNECTOR, "^PC[- ][0-9]+[,.]?[0-9]*/[0-9]+-[A-Z]+-[0-9]+[,.]?[0-9]*.*");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        if (type == ComponentType.CONNECTOR || type == ComponentType.IC) {
            String upperMpn = mpn.toUpperCase();

            // Check each series pattern directly for explicit matching
            if (MC_MCV_PATTERN.matcher(upperMpn).matches()) return true;
            if (MSTB_PATTERN.matcher(upperMpn).matches()) return true;
            if (PT_PATTERN.matcher(upperMpn).matches()) return true;
            if (PTSM_PATTERN.matcher(upperMpn).matches()) return true;
            if (SPT_PATTERN.matcher(upperMpn).matches()) return true;
            if (UK_PATTERN.matcher(upperMpn).matches()) return true;
            if (UT_PATTERN.matcher(upperMpn).matches()) return true;
            if (FK_MCP_PATTERN.matcher(upperMpn).matches()) return true;
            if (FRONT_MC_PATTERN.matcher(upperMpn).matches()) return true;
            if (PC_PATTERN.matcher(upperMpn).matches()) return true;
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // For COMBICON-style MPNs, extract pitch and pin count
        // Format: SERIES CURRENT/PINS-TYPE-PITCH
        Matcher mcMatcher = MC_MCV_PATTERN.matcher(upperMpn);
        if (mcMatcher.matches()) {
            String pins = mcMatcher.group(3);
            String pitch = mcMatcher.group(5).replace(",", ".");
            return pins + "P-" + pitch + "mm";
        }

        Matcher mstbMatcher = MSTB_PATTERN.matcher(upperMpn);
        if (mstbMatcher.matches()) {
            String pins = mstbMatcher.group(3);
            String pitch = mstbMatcher.group(5).replace(",", ".");
            return pins + "P-" + pitch + "mm";
        }

        Matcher ptMatcher = PT_PATTERN.matcher(upperMpn);
        if (ptMatcher.matches()) {
            String pins = ptMatcher.group(2);
            String pitch = ptMatcher.group(4).replace(",", ".");
            return pins + "P-" + pitch + "mm";
        }

        Matcher ptsmMatcher = PTSM_PATTERN.matcher(upperMpn);
        if (ptsmMatcher.matches()) {
            String pins = ptsmMatcher.group(2);
            String pitch = ptsmMatcher.group(4).replace(",", ".");
            String smd = ptsmMatcher.group(5) != null ? "-SMD" : "";
            return pins + "P-" + pitch + "mm" + smd;
        }

        Matcher sptMatcher = SPT_PATTERN.matcher(upperMpn);
        if (sptMatcher.matches()) {
            String pins = sptMatcher.group(2);
            String pitch = sptMatcher.group(4).replace(",", ".");
            return pins + "P-" + pitch + "mm";
        }

        Matcher fkMcpMatcher = FK_MCP_PATTERN.matcher(upperMpn);
        if (fkMcpMatcher.matches()) {
            String pins = fkMcpMatcher.group(2);
            String pitch = fkMcpMatcher.group(4).replace(",", ".");
            return pins + "P-" + pitch + "mm";
        }

        Matcher frontMcMatcher = FRONT_MC_PATTERN.matcher(upperMpn);
        if (frontMcMatcher.matches()) {
            String pins = frontMcMatcher.group(2);
            String pitch = frontMcMatcher.group(4).replace(",", ".");
            return pins + "P-" + pitch + "mm";
        }

        Matcher pcMatcher = PC_PATTERN.matcher(upperMpn);
        if (pcMatcher.matches()) {
            String pins = pcMatcher.group(2);
            String pitch = pcMatcher.group(4).replace(",", ".");
            return pins + "P-" + pitch + "mm";
        }

        // For UK/UT series, extract the current rating
        Matcher ukMatcher = UK_PATTERN.matcher(upperMpn);
        if (ukMatcher.matches()) {
            String current = ukMatcher.group(1).replace(",", ".");
            return current + "mm2";
        }

        Matcher utMatcher = UT_PATTERN.matcher(upperMpn);
        if (utMatcher.matches()) {
            String current = utMatcher.group(1).replace(",", ".");
            return current + "mm2";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check patterns in order of specificity (longer prefixes first)
        if (upperMpn.startsWith("FRONT-MC") || upperMpn.startsWith("FRONT MC")) {
            return "FRONT-MC";
        }
        if (upperMpn.startsWith("FK-MCP") || upperMpn.startsWith("FK MCP") || upperMpn.startsWith("FKMCP")) {
            return "FK-MCP";
        }
        if (upperMpn.startsWith("MSTBA")) {
            return "MSTBA";
        }
        if (upperMpn.startsWith("MSTB")) {
            return "MSTB";
        }
        if (upperMpn.startsWith("MCV")) {
            return "MCV";
        }
        if (upperMpn.startsWith("MC ") || upperMpn.startsWith("MC-")) {
            return "MC";
        }
        if (upperMpn.startsWith("PTSM")) {
            return "PTSM";
        }
        if (upperMpn.startsWith("PT ") || upperMpn.startsWith("PT-")) {
            return "PT";
        }
        if (upperMpn.startsWith("SPT")) {
            return "SPT";
        }
        if (upperMpn.startsWith("UK ") || upperMpn.startsWith("UK-")) {
            return "UK";
        }
        if (upperMpn.startsWith("UT ") || upperMpn.startsWith("UT-")) {
            return "UT";
        }
        if (upperMpn.startsWith("PC ") || upperMpn.startsWith("PC-")) {
            return "PC";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from the same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract pin counts
        int pins1 = extractPinCount(mpn1);
        int pins2 = extractPinCount(mpn2);

        // Must have same pin count
        if (pins1 != pins2 || pins1 == 0) {
            return false;
        }

        // Extract pitch
        String pitch1 = extractPitch(mpn1);
        String pitch2 = extractPitch(mpn2);

        // Must have same pitch
        if (!pitch1.isEmpty() && !pitch2.isEmpty() && !pitch1.equals(pitch2)) {
            return false;
        }

        // Same series, same pin count, same pitch = compatible
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    /**
     * Extract pin count from MPN.
     */
    public int extractPinCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String upperMpn = mpn.toUpperCase();

        // Try each pattern that includes pin count
        Matcher mcMatcher = MC_MCV_PATTERN.matcher(upperMpn);
        if (mcMatcher.matches()) {
            return parseIntSafe(mcMatcher.group(3));
        }

        Matcher mstbMatcher = MSTB_PATTERN.matcher(upperMpn);
        if (mstbMatcher.matches()) {
            return parseIntSafe(mstbMatcher.group(3));
        }

        Matcher ptMatcher = PT_PATTERN.matcher(upperMpn);
        if (ptMatcher.matches()) {
            return parseIntSafe(ptMatcher.group(2));
        }

        Matcher ptsmMatcher = PTSM_PATTERN.matcher(upperMpn);
        if (ptsmMatcher.matches()) {
            return parseIntSafe(ptsmMatcher.group(2));
        }

        Matcher sptMatcher = SPT_PATTERN.matcher(upperMpn);
        if (sptMatcher.matches()) {
            return parseIntSafe(sptMatcher.group(2));
        }

        Matcher fkMcpMatcher = FK_MCP_PATTERN.matcher(upperMpn);
        if (fkMcpMatcher.matches()) {
            return parseIntSafe(fkMcpMatcher.group(2));
        }

        Matcher frontMcMatcher = FRONT_MC_PATTERN.matcher(upperMpn);
        if (frontMcMatcher.matches()) {
            return parseIntSafe(frontMcMatcher.group(2));
        }

        Matcher pcMatcher = PC_PATTERN.matcher(upperMpn);
        if (pcMatcher.matches()) {
            return parseIntSafe(pcMatcher.group(2));
        }

        return 0;
    }

    /**
     * Extract pitch from MPN (in mm).
     */
    public String extractPitch(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Try patterns that include pitch
        Matcher mcMatcher = MC_MCV_PATTERN.matcher(upperMpn);
        if (mcMatcher.matches()) {
            return mcMatcher.group(5).replace(",", ".");
        }

        Matcher mstbMatcher = MSTB_PATTERN.matcher(upperMpn);
        if (mstbMatcher.matches()) {
            return mstbMatcher.group(5).replace(",", ".");
        }

        Matcher ptMatcher = PT_PATTERN.matcher(upperMpn);
        if (ptMatcher.matches()) {
            return ptMatcher.group(4).replace(",", ".");
        }

        Matcher ptsmMatcher = PTSM_PATTERN.matcher(upperMpn);
        if (ptsmMatcher.matches()) {
            return ptsmMatcher.group(4).replace(",", ".");
        }

        Matcher sptMatcher = SPT_PATTERN.matcher(upperMpn);
        if (sptMatcher.matches()) {
            return sptMatcher.group(4).replace(",", ".");
        }

        Matcher fkMcpMatcher = FK_MCP_PATTERN.matcher(upperMpn);
        if (fkMcpMatcher.matches()) {
            return fkMcpMatcher.group(4).replace(",", ".");
        }

        Matcher frontMcMatcher = FRONT_MC_PATTERN.matcher(upperMpn);
        if (frontMcMatcher.matches()) {
            return frontMcMatcher.group(4).replace(",", ".");
        }

        Matcher pcMatcher = PC_PATTERN.matcher(upperMpn);
        if (pcMatcher.matches()) {
            return pcMatcher.group(4).replace(",", ".");
        }

        // Fall back to default pitch for series
        String series = extractSeries(mpn);
        return DEFAULT_PITCH.getOrDefault(series, "");
    }

    /**
     * Get the connector family/type description.
     */
    public String getFamily(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_FAMILIES.getOrDefault(series, "Connector");
    }

    /**
     * Get rated current for the series (in Amperes).
     */
    public double getRatedCurrent(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_CURRENT.getOrDefault(series, 0.0);
    }

    /**
     * Check if this is an SMD (surface mount) connector.
     */
    public boolean isSMD(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.contains("-SMD") || upperMpn.startsWith("PTSM");
    }

    /**
     * Get mounting type (THT or SMD).
     */
    public String getMountingType(String mpn) {
        return isSMD(mpn) ? "SMD" : "THT";
    }

    /**
     * Get connector type code (ST = plug, G = header, etc.).
     */
    public String getConnectorType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract the type code between the two dashes
        Matcher mcMatcher = MC_MCV_PATTERN.matcher(upperMpn);
        if (mcMatcher.matches()) {
            return mcMatcher.group(4);
        }

        Matcher mstbMatcher = MSTB_PATTERN.matcher(upperMpn);
        if (mstbMatcher.matches()) {
            return mstbMatcher.group(4);
        }

        Matcher ptMatcher = PT_PATTERN.matcher(upperMpn);
        if (ptMatcher.matches()) {
            return ptMatcher.group(3);
        }

        Matcher ptsmMatcher = PTSM_PATTERN.matcher(upperMpn);
        if (ptsmMatcher.matches()) {
            return ptsmMatcher.group(3);
        }

        Matcher sptMatcher = SPT_PATTERN.matcher(upperMpn);
        if (sptMatcher.matches()) {
            return sptMatcher.group(3);
        }

        return "";
    }

    /**
     * Decode connector type code to human-readable description.
     */
    public String getConnectorTypeDescription(String mpn) {
        String typeCode = getConnectorType(mpn);
        return switch (typeCode) {
            case "ST" -> "Plug (female)";
            case "G" -> "Header (male)";
            case "GF" -> "Header with flange";
            case "STF" -> "Plug with flange";
            case "V" -> "Vertical";
            case "H" -> "Horizontal";
            case "HH" -> "Double-row horizontal";
            case "PH" -> "Push-in header";
            default -> typeCode.isEmpty() ? "Unknown" : typeCode;
        };
    }

    /**
     * Get wire gauge rating (in mm2).
     */
    public String getWireGauge(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract the gauge from the MPN (first number after series prefix)
        Matcher mcMatcher = MC_MCV_PATTERN.matcher(upperMpn);
        if (mcMatcher.matches()) {
            return mcMatcher.group(2).replace(",", ".");
        }

        Matcher mstbMatcher = MSTB_PATTERN.matcher(upperMpn);
        if (mstbMatcher.matches()) {
            return mstbMatcher.group(2).replace(",", ".");
        }

        Matcher ptMatcher = PT_PATTERN.matcher(upperMpn);
        if (ptMatcher.matches()) {
            return ptMatcher.group(1).replace(",", ".");
        }

        Matcher ptsmMatcher = PTSM_PATTERN.matcher(upperMpn);
        if (ptsmMatcher.matches()) {
            return ptsmMatcher.group(1).replace(",", ".");
        }

        Matcher sptMatcher = SPT_PATTERN.matcher(upperMpn);
        if (sptMatcher.matches()) {
            return sptMatcher.group(1).replace(",", ".");
        }

        return "";
    }

    /**
     * Check if this is a pluggable (separable) connector.
     */
    public boolean isPluggable(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "MC", "MCV", "MSTB", "MSTBA", "FK-MCP", "FRONT-MC" -> true;
            default -> false;
        };
    }

    /**
     * Check if this is a terminal block (non-separable).
     */
    public boolean isTerminalBlock(String mpn) {
        String series = extractSeries(mpn);
        return switch (series) {
            case "PT", "PTSM", "SPT", "UK", "UT", "PC" -> true;
            default -> false;
        };
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
