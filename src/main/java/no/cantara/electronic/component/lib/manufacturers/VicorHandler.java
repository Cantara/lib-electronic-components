package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Vicor Corporation high-performance power modules.
 *
 * Vicor products supported:
 * - DCM: DC-DC Converter Modules (e.g., DCM3623T50D40A4T)
 * - BCM: Bus Converter Modules (e.g., BCM48BT120T300A00)
 * - PRM: PRM Regulator Modules (e.g., PRM48BH480T200A00)
 * - VTM: Voltage Transformation Modules (e.g., VTM48EH040T025A00)
 * - NBM: NBM Converter Modules (e.g., NBM2317S54E1560T00)
 * - PI33xx: ZVS Buck Regulators (e.g., PI3301-00-LGIZ)
 * - PI35xx: Cool-Power ZVS Regulators (e.g., PI3523-00-LGIZ)
 *
 * MPN Structure for DCM/BCM/PRM/VTM:
 * - Prefix: Product family (DCM, BCM, PRM, VTM)
 * - Numbers: Input/output voltage and current specs
 * - Suffix: Package, options, and tape/reel indicators
 *
 * MPN Structure for PI series:
 * - PI33xx or PI35xx: Series and variant
 * - Suffix after hyphen: Package and options (LGIZ, QGIZ, etc.)
 */
public class VicorHandler implements ManufacturerHandler {

    // DCM DC-DC Converter pattern: DCM + 4 digits + suffix
    private static final Pattern DCM_PATTERN = Pattern.compile(
            "^DCM[0-9]{4}[A-Z0-9]+$", Pattern.CASE_INSENSITIVE);

    // BCM Bus Converter pattern: BCM + voltage (2-3 digits) + suffix
    private static final Pattern BCM_PATTERN = Pattern.compile(
            "^BCM[0-9]{2,3}[A-Z]{2}[0-9]+[A-Z0-9]*$", Pattern.CASE_INSENSITIVE);

    // PRM Regulator pattern: PRM + voltage + suffix
    private static final Pattern PRM_PATTERN = Pattern.compile(
            "^PRM[0-9]{2}[A-Z]{2}[0-9]+[A-Z0-9]*$", Pattern.CASE_INSENSITIVE);

    // VTM Voltage Transformation pattern: VTM + voltage + suffix
    private static final Pattern VTM_PATTERN = Pattern.compile(
            "^VTM[0-9]{2}[A-Z]{2}[0-9]+[A-Z0-9]*$", Pattern.CASE_INSENSITIVE);

    // NBM Converter pattern: NBM + 4 digits + suffix
    private static final Pattern NBM_PATTERN = Pattern.compile(
            "^NBM[0-9]{4}[A-Z][0-9]+[A-Z0-9]*$", Pattern.CASE_INSENSITIVE);

    // PI33xx ZVS Buck Regulator pattern (supports multiple hyphen-separated segments like PI3301-00-LGIZ)
    private static final Pattern PI33XX_PATTERN = Pattern.compile(
            "^PI33[0-9]{2}(-[A-Z0-9]+)*$", Pattern.CASE_INSENSITIVE);

    // PI35xx Cool-Power ZVS pattern (supports multiple hyphen-separated segments like PI3523-00-LGIZ)
    private static final Pattern PI35XX_PATTERN = Pattern.compile(
            "^PI35[0-9]{2}(-[A-Z0-9]+)*$", Pattern.CASE_INSENSITIVE);

    // General Vicor pattern for basic matching
    private static final Pattern VICOR_GENERAL_PATTERN = Pattern.compile(
            "^(DCM|BCM|PRM|VTM|NBM|PI3[35])[0-9].*$", Pattern.CASE_INSENSITIVE);

    // Package code extraction patterns
    private static final Pattern POWER_MODULE_PACKAGE_PATTERN = Pattern.compile(
            ".*([A-Z])([0-9]{2,3})([A-Z])([0-9]{2,3})([A-Z])([0-9]{2})$", Pattern.CASE_INSENSITIVE);

    private static final Pattern PI_PACKAGE_PATTERN = Pattern.compile(
            "-([A-Z]{2,4})([IZ])?$", Pattern.CASE_INSENSITIVE);

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.VOLTAGE_REGULATOR
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // DCM DC-DC Converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^DCM[0-9]{4}[A-Z0-9]+$");
        registry.addPattern(ComponentType.IC, "^DCM[0-9]{4}[A-Z0-9]+$");

        // BCM Bus Converters (voltage can be 2-3 digits, e.g., BCM48 or BCM384)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^BCM[0-9]{2,3}[A-Z]{2}[0-9]+[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^BCM[0-9]{2,3}[A-Z]{2}[0-9]+[A-Z0-9]*$");

        // PRM Regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^PRM[0-9]{2}[A-Z]{2}[0-9]+[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^PRM[0-9]{2}[A-Z]{2}[0-9]+[A-Z0-9]*$");

        // VTM Voltage Transformers
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^VTM[0-9]{2}[A-Z]{2}[0-9]+[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^VTM[0-9]{2}[A-Z]{2}[0-9]+[A-Z0-9]*$");

        // NBM Converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^NBM[0-9]{4}[A-Z][0-9]+[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^NBM[0-9]{4}[A-Z][0-9]+[A-Z0-9]*$");

        // PI33xx ZVS Buck Regulators (supports multiple hyphen-separated segments)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^PI33[0-9]{2}(-[A-Z0-9]+)*$");
        registry.addPattern(ComponentType.IC, "^PI33[0-9]{2}(-[A-Z0-9]+)*$");

        // PI35xx Cool-Power ZVS Regulators (supports multiple hyphen-separated segments)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^PI35[0-9]{2}(-[A-Z0-9]+)*$");
        registry.addPattern(ComponentType.IC, "^PI35[0-9]{2}(-[A-Z0-9]+)*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only match VOLTAGE_REGULATOR and IC types
        if (type != ComponentType.VOLTAGE_REGULATOR && type != ComponentType.IC) {
            return false;
        }

        // Check against all Vicor patterns
        return DCM_PATTERN.matcher(upperMpn).matches() ||
               BCM_PATTERN.matcher(upperMpn).matches() ||
               PRM_PATTERN.matcher(upperMpn).matches() ||
               VTM_PATTERN.matcher(upperMpn).matches() ||
               NBM_PATTERN.matcher(upperMpn).matches() ||
               PI33XX_PATTERN.matcher(upperMpn).matches() ||
               PI35XX_PATTERN.matcher(upperMpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle PI series with hyphenated package codes
        if (upperMpn.startsWith("PI3")) {
            Matcher piMatcher = PI_PACKAGE_PATTERN.matcher(upperMpn);
            if (piMatcher.find()) {
                String pkgCode = piMatcher.group(1);
                return switch (pkgCode) {
                    case "LG" -> "LGA";
                    case "LGIZ" -> "LGA";
                    case "QG" -> "QFN";
                    case "QGIZ" -> "QFN";
                    case "BG" -> "BGA";
                    default -> pkgCode;
                };
            }
            // PI series without hyphen has no package suffix
            return "";
        }

        // Handle power module families (DCM, BCM, PRM, VTM, NBM)
        // Package is typically indicated by suffix letter codes
        // T = Through-hole, S = SMD, etc.

        if (upperMpn.length() >= 3) {
            String prefix = upperMpn.substring(0, 3);
            if (prefix.equals("DCM") || prefix.equals("BCM") ||
                prefix.equals("PRM") || prefix.equals("VTM") ||
                prefix.equals("NBM")) {

                // Look for package indicator in suffix
                // Common Vicor package codes:
                // T at specific position = ChiP package
                // S = SiP (System in Package)
                // A = Advanced packaging option

                // Extract the suffix after the numeric spec
                String suffix = extractSuffixFromPowerModule(upperMpn);
                if (!suffix.isEmpty()) {
                    // First character often indicates package family
                    char pkgIndicator = suffix.charAt(0);
                    return switch (pkgIndicator) {
                        case 'T' -> "ChiP";
                        case 'S' -> "SiP";
                        case 'A' -> "ChiP-A";
                        case 'M' -> "MCM";
                        default -> "ChiP"; // Default for power modules
                    };
                }
                return "ChiP"; // Default package for Vicor power modules
            }
        }

        return "";
    }

    /**
     * Extract the suffix portion from a power module MPN.
     * Power modules have format: PREFIX + digits/letters + suffix
     */
    private String extractSuffixFromPowerModule(String mpn) {
        // Look for pattern like T300A00 at the end
        Pattern suffixPattern = Pattern.compile("[A-Z]([0-9]{2,3})[A-Z]([0-9]{2})$");
        Matcher matcher = suffixPattern.matcher(mpn);
        if (matcher.find()) {
            int start = matcher.start();
            if (start > 0) {
                return mpn.substring(start);
            }
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // DCM series
        if (upperMpn.startsWith("DCM")) {
            return "DCM";
        }

        // BCM series
        if (upperMpn.startsWith("BCM")) {
            return "BCM";
        }

        // PRM series
        if (upperMpn.startsWith("PRM")) {
            return "PRM";
        }

        // VTM series
        if (upperMpn.startsWith("VTM")) {
            return "VTM";
        }

        // NBM series
        if (upperMpn.startsWith("NBM")) {
            return "NBM";
        }

        // PI33xx series
        if (upperMpn.matches("^PI33[0-9]{2}.*")) {
            return "PI33";
        }

        // PI35xx series
        if (upperMpn.matches("^PI35[0-9]{2}.*")) {
            return "PI35";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series family for replacement
        if (series1.isEmpty() || series2.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // For power modules, compare the core specifications
        // Parts in the same family with same voltage/current specs are replaceable
        String spec1 = extractCoreSpec(mpn1);
        String spec2 = extractCoreSpec(mpn2);

        if (!spec1.isEmpty() && !spec2.isEmpty()) {
            // Same core spec means compatible (may differ in package/options)
            return spec1.equals(spec2);
        }

        // For PI series, same base part number is compatible
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        return !base1.isEmpty() && base1.equals(base2);
    }

    /**
     * Extract core specification from power module MPN.
     * For DCM3623T50D40A4T: core spec is 3623 (input/output config)
     * For BCM48BT120: core spec is 48BT120 (voltage and current)
     */
    private String extractCoreSpec(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String series = extractSeries(upperMpn);

        if (series.isEmpty()) return "";

        // Remove series prefix and extract spec portion
        String afterSeries = upperMpn.substring(series.length());

        // For DCM/NBM: first 4 digits are the core spec
        if (series.equals("DCM") || series.equals("NBM")) {
            if (afterSeries.length() >= 4) {
                return afterSeries.substring(0, 4);
            }
        }

        // For BCM/PRM/VTM: spec format is voltage + suffix + rating
        // e.g., 48BT120 or 48BH480
        if (series.equals("BCM") || series.equals("PRM") || series.equals("VTM")) {
            // Extract until we hit a 'T' followed by digits (package/options)
            Pattern specPattern = Pattern.compile("^([0-9]{2}[A-Z]{2}[0-9]{3})");
            Matcher matcher = specPattern.matcher(afterSeries);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        // For PI series: the full 4-digit part number
        if (series.equals("PI33") || series.equals("PI35")) {
            return series + afterSeries.substring(0, Math.min(2, afterSeries.length()));
        }

        return "";
    }

    /**
     * Extract base part number (without package suffix).
     */
    private String extractBasePart(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // For PI series, base is everything before the hyphen
        int hyphenIdx = upperMpn.indexOf('-');
        if (hyphenIdx > 0) {
            return upperMpn.substring(0, hyphenIdx);
        }

        // For power modules, extract the specification portion
        String series = extractSeries(upperMpn);
        if (!series.isEmpty()) {
            String spec = extractCoreSpec(upperMpn);
            if (!spec.isEmpty()) {
                return series + spec;
            }
        }

        return upperMpn;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
