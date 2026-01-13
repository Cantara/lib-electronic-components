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
 * Handler for Nuvoton Technology - MCUs, Audio Codecs, and TPM chips.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>NUC1xx - ARM Cortex-M0 MCU (NuMicro M051 series)</li>
 *   <li>NUC2xx - ARM Cortex-M0 MCU (NuMicro M0516 series)</li>
 *   <li>M031 - ARM Cortex-M0 MCU (entry-level)</li>
 *   <li>M451 - ARM Cortex-M4 MCU (motor control)</li>
 *   <li>M480 - ARM Cortex-M4 MCU (high performance)</li>
 *   <li>N76E003 - 8051-based MCU (low-cost, small footprint)</li>
 *   <li>MS51 - Enhanced 8051 MCU</li>
 *   <li>NAU8810 - Audio Codec (mono input, mono output)</li>
 *   <li>NAU8822 - Audio Codec (stereo input, stereo output)</li>
 *   <li>NAU88L25 - Audio Codec (low power)</li>
 *   <li>NPCT6xx - TPM (Trusted Platform Module) chips</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>NUC123LD4AN0 - ARM Cortex-M0 MCU, LQFP-48 package</li>
 *   <li>NUC240LD2AE - ARM Cortex-M0 MCU, LQFP-48 package</li>
 *   <li>M031LD2AE - ARM Cortex-M0 MCU, LQFP-48 package</li>
 *   <li>M451LG6AE - ARM Cortex-M4 MCU, LQFP-100 package</li>
 *   <li>M480KIDAE - ARM Cortex-M4 MCU, LQFP-128 package</li>
 *   <li>N76E003AT20 - 8051 MCU, TSSOP-20 package</li>
 *   <li>MS51FB9AE - Enhanced 8051 MCU, LQFP-48 package</li>
 *   <li>NAU8810YG - Audio Codec, QFN-32 package</li>
 *   <li>NAU8822YG - Audio Codec, QFN-32 package</li>
 *   <li>NAU88L25YGB - Audio Codec, QFN-32 package</li>
 *   <li>NPCT650JAAYX - TPM chip</li>
 * </ul>
 */
public class NuvotonHandler implements ManufacturerHandler {

    // Pattern for ARM Cortex-M MCUs (NUC1xx, NUC2xx)
    // NUC123LD4AN0 -> family=NUC, series=1, line=23, pin=LD, flash=4, pkg=A, temp=N, ver=0
    private static final Pattern NUC_MCU_PATTERN =
            Pattern.compile("NUC(\\d)(\\d{2})([A-Z]{2})(\\d)([A-Z])([A-Z])(\\d)?");

    // Pattern for M-series MCUs (M031, M451, M480)
    // M451LG6AE -> family=M, line=451, pin=LG, flash=6, pkg=AE
    // M480KIDAE -> family=M, line=480, pin=KI, flash=D, pkg=AE (flash can be letter for M480)
    private static final Pattern M_SERIES_PATTERN =
            Pattern.compile("M(\\d{3})([A-Z]{2})([0-9A-Z])([A-Z]{2})");

    // Pattern for 8051 MCUs - N76E003
    // N76E003AT20 -> family=N76E003, pkg=AT, pins=20
    private static final Pattern N76E_PATTERN =
            Pattern.compile("N76E(\\d{3})([A-Z]{2})(\\d+)");

    // Pattern for MS51 MCUs
    // MS51FB9AE -> family=MS51, line=FB, flash=9, pkg=AE
    private static final Pattern MS51_PATTERN =
            Pattern.compile("MS51([A-Z]{2})(\\d)([A-Z]{2})");

    // Pattern for Audio Codecs - NAU series
    // NAU8810YG, NAU8822YG, NAU88L25YGB
    private static final Pattern NAU_CODEC_PATTERN =
            Pattern.compile("NAU(\\d{4,5}|88L\\d{2})([A-Z]+)");

    // Pattern for TPM chips - NPCT6xx
    // NPCT650JAAYX -> family=NPCT, series=650, variant=JAAYX
    private static final Pattern NPCT_PATTERN =
            Pattern.compile("NPCT(\\d{3})([A-Z]+)");

    // Pin code to package name mapping for NUC series MCUs
    // Format: First letter = pin count, Second letter = D for standard variants
    // L = 48-pin, S = 64-pin, V = 100-pin, Z = 144-pin
    private static final Map<String, String> NUC_PIN_TO_PACKAGE = Map.ofEntries(
            Map.entry("LD", "LQFP-48"),
            Map.entry("LE", "LQFP-48"),
            Map.entry("SD", "LQFP-64"),
            Map.entry("SE", "LQFP-64"),
            Map.entry("VD", "LQFP-100"),
            Map.entry("VE", "LQFP-100"),
            Map.entry("ZD", "LQFP-144"),
            Map.entry("AN", "QFN-33")
    );

    // Pin code to package name mapping for M-series MCUs
    private static final Map<String, String> M_SERIES_PIN_TO_PACKAGE = Map.ofEntries(
            Map.entry("LD", "LQFP-48"),
            Map.entry("LC", "LQFP-48"),
            Map.entry("SD", "LQFP-64"),
            Map.entry("SC", "LQFP-64"),
            Map.entry("LG", "LQFP-100"),
            Map.entry("VG", "LQFP-100"),
            Map.entry("KI", "LQFP-128"),
            Map.entry("ZG", "LQFP-144"),
            Map.entry("ZI", "LQFP-144")
    );

    // Package code mappings for 8051 MCUs (N76E, MS51)
    private static final Map<String, String> MCU_8051_PACKAGE_CODES = Map.ofEntries(
            Map.entry("AT", "TSSOP-20"),
            Map.entry("AS", "SOP-20"),
            Map.entry("AQ", "QFN-20"),
            Map.entry("FB", "TSSOP-20"),
            Map.entry("DA", "TSSOP-16"),
            Map.entry("BA", "TSSOP-8"),
            Map.entry("AE", "LQFP-48")
    );

    // Pin count codes for NUC series MCUs
    private static final Map<String, Integer> NUC_PIN_CODES = Map.ofEntries(
            Map.entry("LD", 48),
            Map.entry("LE", 48),
            Map.entry("SD", 64),
            Map.entry("SE", 64),
            Map.entry("VD", 100),
            Map.entry("VE", 100),
            Map.entry("ZD", 144),
            Map.entry("AN", 33)
    );

    // Pin count codes for M-series MCUs
    private static final Map<String, Integer> M_SERIES_PIN_CODES = Map.ofEntries(
            Map.entry("LD", 48),
            Map.entry("LC", 48),
            Map.entry("SD", 64),
            Map.entry("SC", 64),
            Map.entry("LG", 100),
            Map.entry("VG", 100),
            Map.entry("KI", 128),
            Map.entry("ZG", 144),
            Map.entry("ZI", 144)
    );

    // Package codes for Audio Codecs
    private static final Map<String, String> CODEC_PACKAGE_CODES = Map.ofEntries(
            Map.entry("YG", "QFN-32"),
            Map.entry("YGB", "QFN-32"),
            Map.entry("LG", "WLCSP"),
            Map.entry("G", "QFN-48")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ARM Cortex-M MCUs - NUC1xx series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^NUC1\\d{2}[A-Z]+.*");

        // ARM Cortex-M MCUs - NUC2xx series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^NUC2\\d{2}[A-Z]+.*");

        // ARM Cortex-M MCUs - M0xx series (M031, etc.)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^M0\\d{2}[A-Z]+.*");

        // ARM Cortex-M4 MCUs - M4xx series (M451, M480, etc.)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^M4\\d{2}[A-Z]+.*");

        // 8051 MCUs - N76E003 series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^N76E\\d{3}.*");

        // Enhanced 8051 MCUs - MS51 series
        registry.addPattern(ComponentType.MICROCONTROLLER, "^MS51[A-Z]+.*");

        // Audio Codecs - NAU series
        registry.addPattern(ComponentType.IC, "^NAU\\d{4,5}.*");
        registry.addPattern(ComponentType.IC, "^NAU88L\\d{2}.*");

        // TPM chips - NPCT6xx series
        registry.addPattern(ComponentType.IC, "^NPCT\\d{3}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MICROCONTROLLER,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check for MCU types
        boolean isMCU = upperMpn.matches("^NUC[12]\\d{2}[A-Z]+.*") ||
                upperMpn.matches("^M[04]\\d{2}[A-Z]+.*") ||
                upperMpn.matches("^N76E\\d{3}.*") ||
                upperMpn.matches("^MS51[A-Z]+.*");

        // Check for IC types (Audio Codecs, TPM)
        boolean isIC = upperMpn.matches("^NAU\\d{4,5}.*") ||
                upperMpn.matches("^NAU88L\\d{2}.*") ||
                upperMpn.matches("^NPCT\\d{3}.*");

        if (type == ComponentType.MICROCONTROLLER) {
            return isMCU;
        }

        if (type == ComponentType.IC) {
            return isIC;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle NUC series MCUs
        if (upperMpn.startsWith("NUC")) {
            return extractNUCPackageCode(upperMpn);
        }

        // Handle M-series MCUs
        if (upperMpn.matches("^M[04]\\d{2}.*")) {
            return extractMSeriesPackageCode(upperMpn);
        }

        // Handle N76E series
        if (upperMpn.startsWith("N76E")) {
            return extractN76EPackageCode(upperMpn);
        }

        // Handle MS51 series
        if (upperMpn.startsWith("MS51")) {
            return extractMS51PackageCode(upperMpn);
        }

        // Handle NAU Audio Codecs
        if (upperMpn.startsWith("NAU")) {
            return extractNAUPackageCode(upperMpn);
        }

        return "";
    }

    /**
     * Extract package code from NUC series MCU MPN.
     * Format: NUC123LD4AN0 -> LD (first 2 letters after line number)
     * LD = LQFP-48, SD = LQFP-64, VD = LQFP-100, ZD = LQFP-144
     */
    private String extractNUCPackageCode(String mpn) {
        Matcher matcher = NUC_MCU_PATTERN.matcher(mpn);
        if (matcher.matches()) {
            String pinCode = matcher.group(3);  // e.g., "LD"
            String packageCode = NUC_PIN_TO_PACKAGE.get(pinCode);
            if (packageCode != null) {
                return packageCode;
            }
            return pinCode;
        }
        return "";
    }

    /**
     * Extract package code from M-series MCU MPN.
     * Format: M451LG6AE -> LG determines pin count (LQFP-100)
     * LD = LQFP-48, SD = LQFP-64, LG = LQFP-100, KI = LQFP-128
     */
    private String extractMSeriesPackageCode(String mpn) {
        Matcher matcher = M_SERIES_PATTERN.matcher(mpn);
        if (matcher.matches()) {
            String pinCode = matcher.group(2);  // e.g., "LG"
            String packageName = M_SERIES_PIN_TO_PACKAGE.get(pinCode);
            if (packageName != null) {
                return packageName;
            }
            return pinCode;
        }
        return "";
    }

    /**
     * Extract package code from N76E series MPN.
     * Format: N76E003AT20 -> AT = TSSOP, 20 = pin count
     */
    private String extractN76EPackageCode(String mpn) {
        Matcher matcher = N76E_PATTERN.matcher(mpn);
        if (matcher.matches()) {
            String pkgCode = matcher.group(2);  // e.g., "AT"
            String pins = matcher.group(3);     // e.g., "20"
            String packageName = MCU_8051_PACKAGE_CODES.get(pkgCode);
            if (packageName != null) {
                return packageName;
            }
            return pkgCode + pins;
        }
        return "";
    }

    /**
     * Extract package code from MS51 series MPN.
     * Format: MS51FB9AE -> FB is line, AE is package
     */
    private String extractMS51PackageCode(String mpn) {
        Matcher matcher = MS51_PATTERN.matcher(mpn);
        if (matcher.matches()) {
            String pkgCode = matcher.group(3);  // e.g., "AE"
            String packageName = MCU_8051_PACKAGE_CODES.get(pkgCode);
            if (packageName != null) {
                return packageName;
            }
            return pkgCode;
        }
        return "";
    }

    /**
     * Extract package code from NAU Audio Codec MPN.
     * Format: NAU8810YG -> YG = QFN-32
     */
    private String extractNAUPackageCode(String mpn) {
        Matcher matcher = NAU_CODEC_PATTERN.matcher(mpn);
        if (matcher.matches()) {
            String pkgCode = matcher.group(2);  // e.g., "YG"
            String packageName = CODEC_PACKAGE_CODES.get(pkgCode);
            if (packageName != null) {
                return packageName;
            }
            return pkgCode;
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // NUC1xx series
        if (upperMpn.matches("^NUC1\\d{2}.*")) return "NUC1xx";

        // NUC2xx series
        if (upperMpn.matches("^NUC2\\d{2}.*")) return "NUC2xx";

        // M031 series
        if (upperMpn.startsWith("M031")) return "M031";

        // M451 series
        if (upperMpn.startsWith("M451")) return "M451";

        // M480 series
        if (upperMpn.startsWith("M480")) return "M480";

        // M-series generic (catch-all for M0xx, M4xx patterns)
        if (upperMpn.matches("^M0\\d{2}.*")) {
            return "M0" + upperMpn.substring(2, 4);
        }
        if (upperMpn.matches("^M4\\d{2}.*")) {
            return "M4" + upperMpn.substring(2, 4);
        }

        // N76E003 series
        if (upperMpn.startsWith("N76E003")) return "N76E003";
        if (upperMpn.startsWith("N76E")) return "N76E";

        // MS51 series
        if (upperMpn.startsWith("MS51")) return "MS51";

        // NAU88L series (check before NAU88 to be more specific)
        if (upperMpn.matches("^NAU88L\\d{2}.*")) return "NAU88L";

        // NAU8810
        if (upperMpn.startsWith("NAU8810")) return "NAU8810";

        // NAU8822
        if (upperMpn.startsWith("NAU8822")) return "NAU8822";

        // NAU generic
        if (upperMpn.matches("^NAU\\d{4,5}.*")) {
            // Extract 4 or 5 digit series number
            Matcher m = Pattern.compile("^NAU(\\d{4,5})").matcher(upperMpn);
            if (m.find()) {
                return "NAU" + m.group(1);
            }
        }

        // NPCT6xx TPM series
        if (upperMpn.matches("^NPCT6\\d{2}.*")) return "NPCT6xx";
        if (upperMpn.startsWith("NPCT")) return "NPCT";

        return "";
    }

    /**
     * Extract MCU flash size in KB from MPN (for supported MCU types).
     * <p>
     * For NUC and M-series, the flash size is encoded in the MPN.
     *
     * @param mpn the manufacturer part number
     * @return the flash size in KB, or -1 if not extractable
     */
    public int extractMCUFlashSizeKB(String mpn) {
        if (mpn == null) return -1;

        String upperMpn = mpn.toUpperCase();

        // NUC series: flash code is position after pin code
        // NUC123LD4AN0 -> 4 = 64KB (varies by series)
        Matcher nucMatcher = NUC_MCU_PATTERN.matcher(upperMpn);
        if (nucMatcher.matches()) {
            int flashCode = Character.getNumericValue(nucMatcher.group(4).charAt(0));
            // NUC flash encoding (approximate - varies by exact series)
            return switch (flashCode) {
                case 2 -> 16;
                case 3 -> 32;
                case 4 -> 64;
                case 5 -> 128;
                case 6 -> 256;
                default -> -1;
            };
        }

        // M-series: flash code is the digit/letter before package suffix
        // M451LG6AE -> 6 = 512KB
        // M480KIDAE -> D is flash code (letter encoding)
        Matcher mMatcher = M_SERIES_PATTERN.matcher(upperMpn);
        if (mMatcher.matches()) {
            char flashCode = mMatcher.group(3).charAt(0);
            if (Character.isDigit(flashCode)) {
                int code = Character.getNumericValue(flashCode);
                return switch (code) {
                    case 2 -> 32;
                    case 3 -> 64;
                    case 4 -> 128;
                    case 5 -> 256;
                    case 6 -> 512;
                    default -> -1;
                };
            } else {
                // Letter-based flash encoding for M480 series
                return switch (flashCode) {
                    case 'A' -> 128;
                    case 'B' -> 256;
                    case 'C' -> 384;
                    case 'D' -> 512;
                    default -> -1;
                };
            }
        }

        return -1;
    }

    /**
     * Extract MCU pin count from MPN.
     *
     * @param mpn the manufacturer part number
     * @return the pin count, or -1 if not extractable
     */
    public int extractMCUPinCount(String mpn) {
        if (mpn == null) return -1;

        String upperMpn = mpn.toUpperCase();

        // NUC series
        Matcher nucMatcher = NUC_MCU_PATTERN.matcher(upperMpn);
        if (nucMatcher.matches()) {
            String pinCode = nucMatcher.group(3);
            Integer count = NUC_PIN_CODES.get(pinCode);
            return count != null ? count : -1;
        }

        // M-series
        Matcher mMatcher = M_SERIES_PATTERN.matcher(upperMpn);
        if (mMatcher.matches()) {
            String pinCode = mMatcher.group(2);
            Integer count = M_SERIES_PIN_CODES.get(pinCode);
            return count != null ? count : -1;
        }

        // N76E series - extract from suffix
        Matcher n76Matcher = N76E_PATTERN.matcher(upperMpn);
        if (n76Matcher.matches()) {
            try {
                return Integer.parseInt(n76Matcher.group(3));
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return -1;
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Must be same series
        String series1 = extractSeries(upper1);
        String series2 = extractSeries(upper2);
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // For MCUs: must be same line and pin count
        if (isMCU(upper1) && isMCU(upper2)) {
            int pins1 = extractMCUPinCount(upper1);
            int pins2 = extractMCUPinCount(upper2);
            if (pins1 != pins2 || pins1 == -1) {
                return false;
            }
            // Same series and pin count = valid replacement
            // Flash size and package differences are acceptable
            return true;
        }

        // For Audio Codecs: NAU8810 variants are replacements for each other
        if (series1.startsWith("NAU") && series1.equals(series2)) {
            // Same codec series = valid replacement (package differences acceptable)
            return true;
        }

        return false;
    }

    /**
     * Check if the MPN is a microcontroller.
     */
    private boolean isMCU(String mpn) {
        return mpn.matches("^NUC[12]\\d{2}.*") ||
                mpn.matches("^M[04]\\d{2}.*") ||
                mpn.matches("^N76E\\d{3}.*") ||
                mpn.matches("^MS51.*");
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
