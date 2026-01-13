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
 * Handler for Seoul Semiconductor LED components.
 *
 * Seoul Semiconductor is a major LED manufacturer producing:
 * - Acrich - AC-driven LED modules (SMJHA series)
 * - Z-Power - High-power LEDs (Z5-M, P4, P7 series)
 * - Z5-Mx - Mid-power LEDs (Z5-M0, Z5-M1, Z5-M2)
 * - X-series - High-power LEDs
 * - N-series - Niche series
 * - MJT - Multi-Junction Technology LEDs
 * - WICOP - Wafer-Level Integrated Chip on PCB
 * - SunLike - Human-centric lighting LEDs
 * - STW/STN - Standard white/neutral LEDs
 * - SFH - Infrared LEDs
 * - UVA/UVC - UV LEDs (CUD series)
 *
 * MPN Structure examples:
 * - Z5-M0-W0-00: Z-Power Z5 series, variant M0, white, options
 * - STW9Q14C-W9: Standard white LED, series 9Q14C, white 9
 * - SPHWHTL3D50YE3KPH: SunLike series LED with encoded specifications
 * - CUD6GF1B: UV LED, CUD series
 * - SMJHA3V1W1P0S0 / SMJHA-3V1W1P0S0: Acrich AC LED module
 */
public class SeoulSemiHandler implements ManufacturerHandler {

    // Z-Power high-power LED pattern (Z5-M0, Z5-M1, P4, P7, etc.)
    private static final Pattern Z_POWER_PATTERN =
            Pattern.compile("^Z[0-9]+-?[A-Z][0-9]?-?[A-Z0-9-]*", Pattern.CASE_INSENSITIVE);

    // Acrich AC LED pattern (SMJHA series)
    private static final Pattern ACRICH_PATTERN =
            Pattern.compile("^SMJHA-?[0-9A-Z]+", Pattern.CASE_INSENSITIVE);

    // Standard white/neutral LED pattern (STW, STN series)
    private static final Pattern STW_STN_PATTERN =
            Pattern.compile("^ST[WN][0-9A-Z]+-?[A-Z0-9]*", Pattern.CASE_INSENSITIVE);

    // SunLike LED pattern (SPHWHTL, etc.)
    private static final Pattern SUNLIKE_PATTERN =
            Pattern.compile("^SPH[A-Z]{3,}[0-9A-Z]+", Pattern.CASE_INSENSITIVE);

    // UV LED pattern (CUD series)
    private static final Pattern UV_LED_PATTERN =
            Pattern.compile("^CUD[0-9A-Z]+", Pattern.CASE_INSENSITIVE);

    // Infrared LED pattern (SFH series)
    private static final Pattern IR_LED_PATTERN =
            Pattern.compile("^SFH[0-9A-Z]+", Pattern.CASE_INSENSITIVE);

    // WICOP LED pattern
    private static final Pattern WICOP_PATTERN =
            Pattern.compile("^W[0-9]{3,}[A-Z0-9-]*", Pattern.CASE_INSENSITIVE);

    // MJT LED pattern
    private static final Pattern MJT_PATTERN =
            Pattern.compile("^MJT[0-9A-Z-]+", Pattern.CASE_INSENSITIVE);

    // N-series pattern
    private static final Pattern N_SERIES_PATTERN =
            Pattern.compile("^N[0-9]{2,}[A-Z0-9-]*", Pattern.CASE_INSENSITIVE);

    // P-series high-power pattern (P4, P7)
    private static final Pattern P_SERIES_PATTERN =
            Pattern.compile("^P[47][A-Z0-9-]*", Pattern.CASE_INSENSITIVE);

    // X-series high-power pattern
    private static final Pattern X_SERIES_PATTERN =
            Pattern.compile("^X[0-9]+[A-Z0-9-]*", Pattern.CASE_INSENSITIVE);

    // Package code mappings
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("3030", "3030"),
            Map.entry("3535", "3535"),
            Map.entry("5050", "5050"),
            Map.entry("5630", "5630"),
            Map.entry("2835", "2835"),
            Map.entry("3014", "3014"),
            Map.entry("3528", "3528")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Z-Power high-power LEDs (Z5-M0, Z5-M1, Z5-M2, etc.)
        registry.addPattern(ComponentType.LED, "^Z[0-9]+-?[A-Z][0-9]?-?[A-Z0-9-]*");
        registry.addPattern(ComponentType.IC, "^Z[0-9]+-?[A-Z][0-9]?-?[A-Z0-9-]*");

        // Acrich AC-driven LED modules (SMJHA series)
        registry.addPattern(ComponentType.LED, "^SMJHA-?[0-9A-Z]+");
        registry.addPattern(ComponentType.IC, "^SMJHA-?[0-9A-Z]+");

        // Standard white/neutral LEDs (STW, STN series)
        registry.addPattern(ComponentType.LED, "^ST[WN][0-9A-Z]+-?[A-Z0-9]*");
        registry.addPattern(ComponentType.IC, "^ST[WN][0-9A-Z]+-?[A-Z0-9]*");

        // SunLike LEDs (SPHWHTL, etc.)
        registry.addPattern(ComponentType.LED, "^SPH[A-Z]{3,}[0-9A-Z]+");
        registry.addPattern(ComponentType.IC, "^SPH[A-Z]{3,}[0-9A-Z]+");

        // UV LEDs (CUD series)
        registry.addPattern(ComponentType.LED, "^CUD[0-9A-Z]+");
        registry.addPattern(ComponentType.IC, "^CUD[0-9A-Z]+");

        // Infrared LEDs (SFH series)
        registry.addPattern(ComponentType.LED, "^SFH[0-9A-Z]+");
        registry.addPattern(ComponentType.IC, "^SFH[0-9A-Z]+");

        // WICOP LEDs
        registry.addPattern(ComponentType.LED, "^W[0-9]{3,}[A-Z0-9-]*");
        registry.addPattern(ComponentType.IC, "^W[0-9]{3,}[A-Z0-9-]*");

        // MJT Multi-Junction Technology LEDs
        registry.addPattern(ComponentType.LED, "^MJT[0-9A-Z-]+");
        registry.addPattern(ComponentType.IC, "^MJT[0-9A-Z-]+");

        // N-series niche LEDs
        registry.addPattern(ComponentType.LED, "^N[0-9]{2,}[A-Z0-9-]*");
        registry.addPattern(ComponentType.IC, "^N[0-9]{2,}[A-Z0-9-]*");

        // P-series high-power LEDs (P4, P7)
        registry.addPattern(ComponentType.LED, "^P[47][A-Z0-9-]*");
        registry.addPattern(ComponentType.IC, "^P[47][A-Z0-9-]*");

        // X-series high-power LEDs
        registry.addPattern(ComponentType.LED, "^X[0-9]+[A-Z0-9-]*");
        registry.addPattern(ComponentType.IC, "^X[0-9]+[A-Z0-9-]*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.LED,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        if (type == ComponentType.LED || type == ComponentType.IC) {
            // Z-Power series
            if (Z_POWER_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // Acrich AC LEDs
            if (ACRICH_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // Standard white/neutral LEDs
            if (STW_STN_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // SunLike LEDs
            if (SUNLIKE_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // UV LEDs
            if (UV_LED_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // Infrared LEDs
            if (IR_LED_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // WICOP LEDs
            if (WICOP_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // MJT LEDs
            if (MJT_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // N-series
            if (N_SERIES_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // P-series (P4, P7)
            if (P_SERIES_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // X-series
            if (X_SERIES_PATTERN.matcher(upperMpn).matches()) {
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

        // Z-Power series - typically high-power with custom packages
        // Check this BEFORE embedded size codes
        if (Z_POWER_PATTERN.matcher(upperMpn).matches()) {
            // Z5-M0, Z5-M1, etc. have specific package designations
            if (upperMpn.contains("Z5-M0") || upperMpn.contains("Z5M0")) {
                return "Ceramic";
            }
            if (upperMpn.contains("Z5-M1") || upperMpn.contains("Z5M1")) {
                return "Ceramic";
            }
            if (upperMpn.contains("Z5-M2") || upperMpn.contains("Z5M2")) {
                return "Ceramic";
            }
            return "High-Power";
        }

        // Acrich modules - check before size codes
        if (ACRICH_PATTERN.matcher(upperMpn).matches()) {
            return "Module";
        }

        // WICOP - wafer-level package, check before size codes
        if (WICOP_PATTERN.matcher(upperMpn).matches()) {
            return "CSP"; // Chip Scale Package
        }

        // MJT - multi-junction, check before size codes
        if (MJT_PATTERN.matcher(upperMpn).matches()) {
            return "MJT";
        }

        // UV LEDs - check before size codes
        if (UV_LED_PATTERN.matcher(upperMpn).matches()) {
            return "UV";
        }

        // Infrared LEDs - check before size codes
        if (IR_LED_PATTERN.matcher(upperMpn).matches()) {
            return "IR";
        }

        // P-series high-power - check before size codes
        if (P_SERIES_PATTERN.matcher(upperMpn).matches()) {
            return "High-Power";
        }

        // X-series high-power - check before size codes
        if (X_SERIES_PATTERN.matcher(upperMpn).matches()) {
            return "High-Power";
        }

        // SunLike LEDs - try to extract package size from MPN
        if (SUNLIKE_PATTERN.matcher(upperMpn).matches()) {
            Matcher sizeMatcher = Pattern.compile("([0-9]{4})").matcher(upperMpn);
            if (sizeMatcher.find()) {
                String size = sizeMatcher.group(1);
                return PACKAGE_CODES.getOrDefault(size, "SMD");
            }
            return "SMD";
        }

        // STW/STN standard LEDs - try to extract size
        if (STW_STN_PATTERN.matcher(upperMpn).matches()) {
            Matcher sizeMatcher = Pattern.compile("([0-9]{4})").matcher(upperMpn);
            if (sizeMatcher.find()) {
                String size = sizeMatcher.group(1);
                return PACKAGE_CODES.getOrDefault(size, "SMD");
            }
            return "SMD";
        }

        // Check for embedded package size codes (fallback)
        for (String sizeCode : PACKAGE_CODES.keySet()) {
            if (upperMpn.contains(sizeCode)) {
                return PACKAGE_CODES.get(sizeCode);
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Z-Power series - extract Z5, etc.
        if (Z_POWER_PATTERN.matcher(upperMpn).matches()) {
            // Match Z followed by digits and optional variant (Z5-M0, Z5M0)
            Matcher matcher = Pattern.compile("^(Z[0-9]+)[-]?([A-Z][0-9])?").matcher(upperMpn);
            if (matcher.find()) {
                String base = matcher.group(1);
                String variant = matcher.group(2);
                if (variant != null) {
                    return base + "-" + variant;
                }
                return base;
            }
        }

        // Acrich series
        if (ACRICH_PATTERN.matcher(upperMpn).matches()) {
            return "Acrich";
        }

        // SunLike series
        if (SUNLIKE_PATTERN.matcher(upperMpn).matches()) {
            return "SunLike";
        }

        // UV LED series
        if (UV_LED_PATTERN.matcher(upperMpn).matches()) {
            // Extract CUD series (CUD6, CUD8, etc.)
            Matcher matcher = Pattern.compile("^(CUD[0-9]+)").matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return "CUD";
        }

        // Infrared LED series
        if (IR_LED_PATTERN.matcher(upperMpn).matches()) {
            // Extract SFH series (SFH4, SFH5, etc.)
            Matcher matcher = Pattern.compile("^(SFH[0-9]+)").matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return "SFH";
        }

        // WICOP series
        if (WICOP_PATTERN.matcher(upperMpn).matches()) {
            return "WICOP";
        }

        // MJT series
        if (MJT_PATTERN.matcher(upperMpn).matches()) {
            return "MJT";
        }

        // STW/STN series - extract base series code
        if (STW_STN_PATTERN.matcher(upperMpn).matches()) {
            // Match STW or STN followed by alphanumerics until dash
            Matcher matcher = Pattern.compile("^(ST[WN][0-9A-Z]+)").matcher(upperMpn);
            if (matcher.find()) {
                String series = matcher.group(1);
                int dashIndex = series.indexOf('-');
                return dashIndex > 0 ? series.substring(0, dashIndex) : series;
            }
        }

        // N-series
        if (N_SERIES_PATTERN.matcher(upperMpn).matches()) {
            Matcher matcher = Pattern.compile("^(N[0-9]+)").matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return "N";
        }

        // P-series (P4, P7)
        if (P_SERIES_PATTERN.matcher(upperMpn).matches()) {
            Matcher matcher = Pattern.compile("^(P[47])").matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        // X-series
        if (X_SERIES_PATTERN.matcher(upperMpn).matches()) {
            Matcher matcher = Pattern.compile("^(X[0-9]+)").matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return "X";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null || mpn1.isEmpty() || mpn2.isEmpty()) {
            return false;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // For LEDs, same series and color can be replacements
        String color1 = extractColorCode(mpn1);
        String color2 = extractColorCode(mpn2);

        // If colors match (or one is unknown), consider them replacements
        if (!color1.isEmpty() && !color2.isEmpty()) {
            if (!color1.equals(color2)) {
                return false;
            }
        }

        // Same package required for LED replacement
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If packages are known and different, not a replacement
        if (!pkg1.isEmpty() && !pkg2.isEmpty() && !pkg1.equals(pkg2)) {
            return false;
        }

        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Extracts the color code from a Seoul Semiconductor LED MPN.
     *
     * Common color indicators:
     * - W, WH, WHT = White
     * - N, NW = Neutral White
     * - CW = Cool White
     * - WW = Warm White
     * - R, RD = Red
     * - G, GR = Green
     * - B, BL = Blue
     * - Y, YL = Yellow
     * - A, AM = Amber
     * - UV = Ultraviolet
     * - IR = Infrared
     *
     * @param mpn the manufacturer part number
     * @return the color code or empty string
     */
    public String extractColorCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // UV - check first as CUD pattern is specific
        if (upperMpn.startsWith("CUD") || upperMpn.contains("UV")) {
            return "UV";
        }

        // IR - check first as SFH pattern is specific
        if (upperMpn.startsWith("SFH") || upperMpn.contains("IR")) {
            return "IR";
        }

        // Check white variants BEFORE generic white
        // Cool White - check before generic white
        if (upperMpn.contains("-CW") || upperMpn.contains("CW-") ||
            upperMpn.matches(".*-CW[0-9-].*")) {
            return "CW";
        }

        // Warm White - check before generic white
        if (upperMpn.contains("-WW") || upperMpn.contains("WW-") ||
            upperMpn.matches(".*-WW[0-9-].*")) {
            return "WW";
        }

        // Neutral White - check before generic white
        if (upperMpn.contains("-NW") || upperMpn.contains("NW-") ||
            upperMpn.matches(".*-NW[0-9-].*")) {
            return "NW";
        }

        // Generic White - check after variants
        if (upperMpn.contains("-W0") || upperMpn.contains("-W-") ||
            upperMpn.contains("WHT") || upperMpn.contains("WH-")) {
            return "W";
        }

        // Red
        if (upperMpn.contains("-R0") || upperMpn.contains("-R-") ||
            upperMpn.contains("RED") || upperMpn.contains("-RD")) {
            return "R";
        }

        // Green
        if (upperMpn.contains("-G0") || upperMpn.contains("-G-") ||
            upperMpn.contains("GRN") || upperMpn.contains("-GR")) {
            return "G";
        }

        // Blue
        if (upperMpn.contains("-B0") || upperMpn.contains("-B-") ||
            upperMpn.contains("BLU") || upperMpn.contains("-BL")) {
            return "B";
        }

        // Yellow
        if (upperMpn.contains("-Y0") || upperMpn.contains("-Y-") ||
            upperMpn.contains("YEL") || upperMpn.contains("-YL")) {
            return "Y";
        }

        // Amber
        if (upperMpn.contains("-A0") || upperMpn.contains("-A-") ||
            upperMpn.contains("AMB") || upperMpn.contains("-AM")) {
            return "A";
        }

        return "";
    }

    /**
     * Gets the LED technology type.
     *
     * @param mpn the manufacturer part number
     * @return the technology description
     */
    public String getTechnology(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        if (ACRICH_PATTERN.matcher(upperMpn).matches()) {
            return "AC-Driven";
        }

        if (WICOP_PATTERN.matcher(upperMpn).matches()) {
            return "WICOP (Wafer-Level CSP)";
        }

        if (MJT_PATTERN.matcher(upperMpn).matches()) {
            return "MJT (Multi-Junction)";
        }

        if (SUNLIKE_PATTERN.matcher(upperMpn).matches()) {
            return "SunLike (Human-Centric)";
        }

        if (UV_LED_PATTERN.matcher(upperMpn).matches()) {
            return "UV LED";
        }

        if (IR_LED_PATTERN.matcher(upperMpn).matches()) {
            return "Infrared LED";
        }

        if (Z_POWER_PATTERN.matcher(upperMpn).matches() ||
            P_SERIES_PATTERN.matcher(upperMpn).matches() ||
            X_SERIES_PATTERN.matcher(upperMpn).matches()) {
            return "High-Power LED";
        }

        if (STW_STN_PATTERN.matcher(upperMpn).matches()) {
            if (upperMpn.startsWith("STW")) {
                return "Standard White";
            }
            if (upperMpn.startsWith("STN")) {
                return "Standard Neutral";
            }
        }

        return "";
    }
}
