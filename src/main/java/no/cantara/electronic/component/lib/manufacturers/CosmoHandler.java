package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Cosmo Electronics (Taiwan) components.
 *
 * Cosmo Electronics is a Taiwanese manufacturer specializing in optocouplers and photo interrupters.
 *
 * Key product lines:
 * - KP series: Phototransistor optocouplers (KP1010, KP1020, KP2010, KP4010)
 * - KPC series: High isolation optocouplers (KPC817, KPC357)
 * - KPH series: High speed optocouplers (KPH121, KPH141)
 * - KPS series: SMD optocouplers (KPS1010, KPS2010)
 * - KPTR series: Reflective sensors (KPTR1200, KPTR1201)
 *
 * MPN Structure:
 *
 * Phototransistor Optocouplers (KP series):
 * KP[series][variant][-suffix]
 * e.g., KP1010-1, KP2010C
 * - KP1010 = single channel phototransistor coupler
 * - KP2010 = dual channel
 * - KP4010 = quad channel
 * - -1 = standard DIP package
 * - C, S = SMD variants
 *
 * High Isolation Optocouplers (KPC series):
 * KPC[series][CTR grade][-suffix]
 * e.g., KPC817C, KPC357NT
 * - KPC817 = general purpose high isolation
 * - KPC357 = Darlington output
 * - A/B/C/D = CTR (Current Transfer Ratio) grades
 * - -1 = standard DIP
 *
 * High Speed Optocouplers (KPH series):
 * KPH[series][variant][-suffix]
 * e.g., KPH121-1, KPH141S
 * - KPH121 = high speed single channel
 * - KPH141 = high speed dual channel
 *
 * SMD Optocouplers (KPS series):
 * KPS[series][variant]
 * e.g., KPS1010S, KPS2010C
 * - Surface mount versions of KP series
 *
 * Reflective Sensors (KPTR series):
 * KPTR[series][variant]
 * e.g., KPTR1200, KPTR1201
 * - Reflective photo interrupters
 *
 * Package codes from suffix:
 * - -1 = Standard DIP-4 package
 * - C = SMD (compact)
 * - S = Surface mount
 * - -F = Lead-free variant
 * - -TR = Tape and reel packaging
 */
public class CosmoHandler implements ManufacturerHandler {

    // KP series: Phototransistor optocouplers
    // KP1010, KP1020, KP2010, KP4010
    private static final Pattern KP_PATTERN =
            Pattern.compile("^KP(\\d{4})([A-Z])?(-[A-Z0-9]+)?$", Pattern.CASE_INSENSITIVE);

    // KPC series: High isolation optocouplers
    // KPC817, KPC357
    private static final Pattern KPC_PATTERN =
            Pattern.compile("^KPC(\\d{3})([A-Z])?([A-Z]+)?(-[A-Z0-9]+)?$", Pattern.CASE_INSENSITIVE);

    // KPH series: High speed optocouplers
    // KPH121, KPH141
    private static final Pattern KPH_PATTERN =
            Pattern.compile("^KPH(\\d{3})([A-Z])?(-[A-Z0-9]+)?$", Pattern.CASE_INSENSITIVE);

    // KPS series: SMD optocouplers
    // KPS1010, KPS2010
    private static final Pattern KPS_PATTERN =
            Pattern.compile("^KPS(\\d{4})([A-Z])?(-[A-Z0-9]+)?$", Pattern.CASE_INSENSITIVE);

    // KPTR series: Reflective sensors
    // KPTR1200, KPTR1201
    private static final Pattern KPTR_PATTERN =
            Pattern.compile("^KPTR(\\d{4})([A-Z0-9-]+)?$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // KP series: Phototransistor optocouplers
        registry.addPattern(ComponentType.IC, "^KP\\d{4}.*");
        registry.addPattern(ComponentType.OPTOCOUPLER_TOSHIBA, "^KP\\d{4}.*");

        // KPC series: High isolation optocouplers
        registry.addPattern(ComponentType.IC, "^KPC\\d{3}.*");
        registry.addPattern(ComponentType.OPTOCOUPLER_TOSHIBA, "^KPC\\d{3}.*");

        // KPH series: High speed optocouplers
        registry.addPattern(ComponentType.IC, "^KPH\\d{3}.*");
        registry.addPattern(ComponentType.OPTOCOUPLER_TOSHIBA, "^KPH\\d{3}.*");

        // KPS series: SMD optocouplers
        registry.addPattern(ComponentType.IC, "^KPS\\d{4}.*");
        registry.addPattern(ComponentType.OPTOCOUPLER_TOSHIBA, "^KPS\\d{4}.*");

        // KPTR series: Reflective sensors
        registry.addPattern(ComponentType.IC, "^KPTR\\d{4}.*");
        registry.addPattern(ComponentType.SENSOR, "^KPTR\\d{4}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.OPTOCOUPLER_TOSHIBA,
                ComponentType.SENSOR
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // IC type - matches all Cosmo products
        if (type == ComponentType.IC) {
            if (upperMpn.matches("^KP\\d{4}.*") ||
                    upperMpn.matches("^KPC\\d{3}.*") ||
                    upperMpn.matches("^KPH\\d{3}.*") ||
                    upperMpn.matches("^KPS\\d{4}.*") ||
                    upperMpn.matches("^KPTR\\d{4}.*")) {
                return true;
            }
        }

        // OPTOCOUPLER type - matches optocoupler products (KP, KPC, KPH, KPS)
        if (type == ComponentType.OPTOCOUPLER_TOSHIBA) {
            if (upperMpn.matches("^KP\\d{4}.*") ||
                    upperMpn.matches("^KPC\\d{3}.*") ||
                    upperMpn.matches("^KPH\\d{3}.*") ||
                    upperMpn.matches("^KPS\\d{4}.*")) {
                return true;
            }
        }

        // SENSOR type - matches reflective sensors (KPTR)
        if (type == ComponentType.SENSOR) {
            if (upperMpn.matches("^KPTR\\d{4}.*")) {
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

        // Check for suffix-based package codes
        // -1 = Standard DIP-4
        if (upperMpn.contains("-1")) {
            return "DIP-4";
        }

        // KPS series is always SMD
        if (upperMpn.startsWith("KPS")) {
            return "SMD";
        }

        // KPTR series reflective sensors - typically SMD
        if (upperMpn.startsWith("KPTR")) {
            return "SMD";
        }

        // KPC series - check for CTR grade letters (A, B, C, D) and package indicators
        Matcher kpcMatcher = KPC_PATTERN.matcher(upperMpn);
        if (kpcMatcher.matches()) {
            String suffix = kpcMatcher.group(4);  // -suffix group
            if (suffix != null && suffix.contains("-1")) {
                return "DIP-4";
            }
            // Check if there's a package indicator letter after CTR grade
            String pkgIndicator = kpcMatcher.group(3);  // Additional letters after CTR grade
            if (pkgIndicator != null) {
                // S = SMD, G = SMD variant, T = SOP thin variant
                if (pkgIndicator.contains("S") || pkgIndicator.contains("G")) {
                    return "SMD";
                }
                if (pkgIndicator.contains("T")) {
                    return "SOP";  // Thin variant
                }
            }
            return "DIP-4";  // Default for KPC (CTR grades A,B,C,D are not package codes)
        }

        // KPH series - check for SMD indicator
        Matcher kphMatcher = KPH_PATTERN.matcher(upperMpn);
        if (kphMatcher.matches()) {
            String variant = kphMatcher.group(2);
            if (variant != null && (variant.equals("S") || variant.equals("C"))) {
                return "SMD";
            }
            return "DIP";  // Default for KPH
        }

        // KP series - check for SMD indicators (S or C suffix after numbers)
        Matcher kpMatcher = KP_PATTERN.matcher(upperMpn);
        if (kpMatcher.matches()) {
            String variant = kpMatcher.group(2);
            if (variant != null && (variant.equals("S") || variant.equals("C"))) {
                return "SMD";
            }
            return "DIP-4";  // Default for KP
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // KPTR series (check first as it's longest prefix)
        Matcher kptrMatcher = KPTR_PATTERN.matcher(upperMpn);
        if (kptrMatcher.matches()) {
            return "KPTR" + kptrMatcher.group(1);
        }

        // KPS series (SMD optocouplers)
        Matcher kpsMatcher = KPS_PATTERN.matcher(upperMpn);
        if (kpsMatcher.matches()) {
            return "KPS" + kpsMatcher.group(1);
        }

        // KPC series (high isolation)
        Matcher kpcMatcher = KPC_PATTERN.matcher(upperMpn);
        if (kpcMatcher.matches()) {
            return "KPC" + kpcMatcher.group(1);
        }

        // KPH series (high speed)
        Matcher kphMatcher = KPH_PATTERN.matcher(upperMpn);
        if (kphMatcher.matches()) {
            return "KPH" + kphMatcher.group(1);
        }

        // KP series (phototransistor)
        Matcher kpMatcher = KP_PATTERN.matcher(upperMpn);
        if (kpMatcher.matches()) {
            return "KP" + kpMatcher.group(1);
        }

        // Fallback: extract prefix and first numeric sequence
        if (upperMpn.startsWith("KPTR")) {
            int end = 4;
            while (end < upperMpn.length() && Character.isDigit(upperMpn.charAt(end))) {
                end++;
            }
            return upperMpn.substring(0, end);
        }
        if (upperMpn.startsWith("KPS")) {
            int end = 3;
            while (end < upperMpn.length() && Character.isDigit(upperMpn.charAt(end))) {
                end++;
            }
            return upperMpn.substring(0, end);
        }
        if (upperMpn.startsWith("KPC")) {
            int end = 3;
            while (end < upperMpn.length() && Character.isDigit(upperMpn.charAt(end))) {
                end++;
            }
            return upperMpn.substring(0, end);
        }
        if (upperMpn.startsWith("KPH")) {
            int end = 3;
            while (end < upperMpn.length() && Character.isDigit(upperMpn.charAt(end))) {
                end++;
            }
            return upperMpn.substring(0, end);
        }
        if (upperMpn.startsWith("KP")) {
            int end = 2;
            while (end < upperMpn.length() && Character.isDigit(upperMpn.charAt(end))) {
                end++;
            }
            return upperMpn.substring(0, end);
        }

        return "";
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

        // For optocouplers, same series with different CTR grades are not direct replacements
        // but same series with different packages may be
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If both have package codes, check if they're compatible
        if (!pkg1.isEmpty() && !pkg2.isEmpty()) {
            // DIP and SMD are not compatible
            if ((pkg1.contains("DIP") && pkg2.contains("SMD")) ||
                    (pkg1.contains("SMD") && pkg2.contains("DIP"))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Extracts the CTR (Current Transfer Ratio) grade from a KPC series MPN.
     *
     * CTR grades for KPC817:
     * - A: 80-160%
     * - B: 130-260%
     * - C: 200-400%
     * - D: 300-600%
     *
     * @param mpn the manufacturer part number
     * @return the CTR grade letter (A, B, C, D) or empty string if not found
     */
    public String extractCTRGrade(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // KPC series CTR grades
        Matcher kpcMatcher = KPC_PATTERN.matcher(upperMpn);
        if (kpcMatcher.matches()) {
            String ctrGrade = kpcMatcher.group(2);
            if (ctrGrade != null && ctrGrade.matches("[A-D]")) {
                return ctrGrade;
            }
        }

        return "";
    }

    /**
     * Determines the product category of a Cosmo component.
     *
     * @param mpn the manufacturer part number
     * @return product category description
     */
    public String getProductCategory(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("KPTR")) {
            return "Reflective Sensor";
        }
        if (upperMpn.startsWith("KPS")) {
            return "SMD Optocoupler";
        }
        if (upperMpn.startsWith("KPC")) {
            return "High Isolation Optocoupler";
        }
        if (upperMpn.startsWith("KPH")) {
            return "High Speed Optocoupler";
        }
        if (upperMpn.startsWith("KP")) {
            return "Phototransistor Optocoupler";
        }

        return "";
    }

    /**
     * Determines if a component is through-hole or SMD.
     *
     * @param mpn the manufacturer part number
     * @return "Through-hole" or "SMD" or empty string
     */
    public String getMountingType(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String pkg = extractPackageCode(mpn);
        if (pkg.isEmpty()) {
            return "";
        }

        if (pkg.contains("DIP")) {
            return "Through-hole";
        }
        if (pkg.equals("SMD") || pkg.equals("SOP")) {
            return "SMD";
        }

        return "";
    }

    /**
     * Gets the number of channels in an optocoupler.
     *
     * @param mpn the manufacturer part number
     * @return number of channels (1, 2, 4) or 0 if unknown
     */
    public int getChannelCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return 0;
        }

        String series = extractSeries(mpn);
        if (series.isEmpty()) {
            return 0;
        }

        // KP series channel count from series number
        // 1010 = single channel, 2010 = dual channel, 4010 = quad channel
        // KPC817, KPC357 = single channel
        // KPH121 = single channel, KPH141 = dual channel
        if (series.contains("1010") || series.contains("1020") ||
                series.contains("817") || series.contains("357") ||
                series.contains("121") || series.contains("1200") || series.contains("1201")) {
            return 1;
        }
        if (series.contains("2010") || series.contains("141")) {
            return 2;
        }
        if (series.contains("4010")) {
            return 4;
        }

        return 1;  // Default to single channel
    }
}
