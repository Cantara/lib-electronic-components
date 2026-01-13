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
 * Handler for ESMT (Elite Semiconductor Memory Technology) - DRAM and Flash memory ICs.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>M12Lxxxx - SDRAM (synchronous DRAM)</li>
 *   <li>M14Dxxxx - DDR SDRAM</li>
 *   <li>F25Lxxx - SPI Flash</li>
 *   <li>F49Lxxx - Parallel Flash</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>M12L128168A-6TG - 128Mb SDRAM, TSOP-II package</li>
 *   <li>M12L64164A-5TG - 64Mb SDRAM, TSOP-II package</li>
 *   <li>M14D5121632A-2.5BG - 512Mb DDR SDRAM, BGA package</li>
 *   <li>F25L004A-100PAIG - 4Mb SPI Flash, SOP package</li>
 *   <li>F25L008A-100PAIG - 8Mb SPI Flash</li>
 *   <li>F25L016A-100PAIG - 16Mb SPI Flash</li>
 *   <li>F49L160UA-70TG - 16Mb Parallel Flash, TSOP package</li>
 * </ul>
 * <p>
 * Package codes:
 * <ul>
 *   <li>A - TSOP (Thin Small Outline Package)</li>
 *   <li>B - BGA (Ball Grid Array)</li>
 *   <li>TG - TSOP-II</li>
 *   <li>WG - WBGA (Window BGA)</li>
 *   <li>BG - BGA</li>
 *   <li>PA - SOP (Small Outline Package)</li>
 * </ul>
 */
public class ESMTHandler implements ManufacturerHandler {

    // Pattern for extracting density from MPN
    private static final Pattern SDRAM_DENSITY_PATTERN =
            Pattern.compile("M12L(\\d+)");
    private static final Pattern DDR_DENSITY_PATTERN =
            Pattern.compile("M14D(\\d+)");
    private static final Pattern SPI_FLASH_DENSITY_PATTERN =
            Pattern.compile("F25L(\\d+)");
    private static final Pattern PARALLEL_FLASH_DENSITY_PATTERN =
            Pattern.compile("F49L(\\d+)");

    // Package code mappings
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("A", "TSOP"),
            Map.entry("B", "BGA"),
            Map.entry("TG", "TSOP-II"),
            Map.entry("WG", "WBGA"),
            Map.entry("BG", "BGA"),
            Map.entry("PA", "SOP"),
            Map.entry("PAIG", "SOP"),
            Map.entry("PG", "SOP-8")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // SDRAM - M12Lxxxx series
        registry.addPattern(ComponentType.MEMORY, "^M12L\\d+.*");
        registry.addPattern(ComponentType.IC, "^M12L\\d+.*");

        // DDR SDRAM - M14Dxxxx series
        registry.addPattern(ComponentType.MEMORY, "^M14D\\d+.*");
        registry.addPattern(ComponentType.IC, "^M14D\\d+.*");

        // SPI Flash - F25Lxxx series
        registry.addPattern(ComponentType.MEMORY, "^F25L\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^F25L\\d+.*");
        registry.addPattern(ComponentType.IC, "^F25L\\d+.*");

        // Parallel Flash - F49Lxxx series
        registry.addPattern(ComponentType.MEMORY, "^F49L\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^F49L\\d+.*");
        registry.addPattern(ComponentType.IC, "^F49L\\d+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MEMORY,
                ComponentType.MEMORY_FLASH,
                ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct checks for memory and IC types
        if (type == ComponentType.MEMORY || type == ComponentType.IC) {
            // SDRAM - M12L series
            if (upperMpn.matches("^M12L\\d+.*")) {
                return true;
            }
            // DDR SDRAM - M14D series
            if (upperMpn.matches("^M14D\\d+.*")) {
                return true;
            }
            // SPI Flash - F25L series
            if (upperMpn.matches("^F25L\\d+.*")) {
                return true;
            }
            // Parallel Flash - F49L series
            if (upperMpn.matches("^F49L\\d+.*")) {
                return true;
            }
        }

        // Direct checks for flash types
        if (type == ComponentType.MEMORY_FLASH) {
            // SPI Flash - F25L series
            if (upperMpn.matches("^F25L\\d+.*")) {
                return true;
            }
            // Parallel Flash - F49L series
            if (upperMpn.matches("^F49L\\d+.*")) {
                return true;
            }
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove any leading/trailing whitespace
        upperMpn = upperMpn.trim();

        // Handle hyphenated MPNs - get the part after the last hyphen
        int lastHyphen = upperMpn.lastIndexOf('-');
        if (lastHyphen > 0 && lastHyphen < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(lastHyphen + 1);
            // For patterns like "6TG", "2.5BG", extract the package code after digits/dots
            String pkgCode = suffix.replaceAll("^[0-9.]+", "");
            if (!pkgCode.isEmpty()) {
                // Check longer codes first (TG before T, BG before B, etc.)
                if (PACKAGE_CODES.containsKey(pkgCode)) {
                    return PACKAGE_CODES.get(pkgCode);
                }
                // Try first 2 chars if full suffix not found
                if (pkgCode.length() >= 2) {
                    String twoChar = pkgCode.substring(0, 2);
                    if (PACKAGE_CODES.containsKey(twoChar)) {
                        return PACKAGE_CODES.get(twoChar);
                    }
                }
                // Try first char
                String oneChar = pkgCode.substring(0, 1);
                if (PACKAGE_CODES.containsKey(oneChar)) {
                    return PACKAGE_CODES.get(oneChar);
                }
                return pkgCode; // Return raw suffix if not mapped
            }
        }

        // For SPI Flash like F25L004A-100PAIG, extract package from suffix
        // Look for package code pattern at the end
        Pattern pkgPattern = Pattern.compile(".*?(PAIG|PA|PG|TG|WG|BG|A|B)(?:I[GC]?)?$");
        Matcher matcher = pkgPattern.matcher(upperMpn);
        if (matcher.matches()) {
            String pkgCode = matcher.group(1);
            if (PACKAGE_CODES.containsKey(pkgCode)) {
                return PACKAGE_CODES.get(pkgCode);
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // SDRAM - M12L series
        if (upperMpn.startsWith("M12L")) return "M12L";

        // DDR SDRAM - M14D series
        if (upperMpn.startsWith("M14D")) return "M14D";

        // SPI Flash - F25L series
        if (upperMpn.startsWith("F25L")) return "F25L";

        // Parallel Flash - F49L series
        if (upperMpn.startsWith("F49L")) return "F49L";

        return "";
    }

    /**
     * Extract memory density from MPN.
     * <p>
     * Examples:
     * <ul>
     *   <li>M12L128168A - returns "128" (128 Mbit)</li>
     *   <li>M12L64164A - returns "64" (64 Mbit)</li>
     *   <li>M14D5121632A - returns "512" (512 Mbit)</li>
     *   <li>F25L004A - returns "4" (4 Mbit)</li>
     *   <li>F25L008A - returns "8" (8 Mbit)</li>
     *   <li>F25L016A - returns "16" (16 Mbit)</li>
     *   <li>F49L160UA - returns "16" (16 Mbit)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the density string in Mbits, or empty string if not extractable
     */
    public String extractDensity(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // SDRAM (M12L series)
        // Format: M12L128168A = 128Mb (128 x 16 bits x 8 banks)
        // Format: M12L64164A = 64Mb (64 x 16 bits x 4 banks)
        Matcher sdramMatcher = SDRAM_DENSITY_PATTERN.matcher(upperMpn);
        if (sdramMatcher.find()) {
            String densityDigits = sdramMatcher.group(1);
            // First 2-3 digits encode the density
            if (densityDigits.startsWith("128")) return "128";
            if (densityDigits.startsWith("64")) return "64";
            if (densityDigits.startsWith("32")) return "32";
            if (densityDigits.startsWith("16")) return "16";
            // Try extracting leading digits
            return extractLeadingDensity(densityDigits);
        }

        // DDR SDRAM (M14D series)
        // Format: M14D5121632A = 512Mb DDR SDRAM
        Matcher ddrMatcher = DDR_DENSITY_PATTERN.matcher(upperMpn);
        if (ddrMatcher.find()) {
            String densityDigits = ddrMatcher.group(1);
            if (densityDigits.startsWith("512")) return "512";
            if (densityDigits.startsWith("256")) return "256";
            if (densityDigits.startsWith("128")) return "128";
            if (densityDigits.startsWith("64")) return "64";
            return extractLeadingDensity(densityDigits);
        }

        // SPI Flash (F25L series)
        // Format: F25L004A = 4Mb, F25L008A = 8Mb, F25L016A = 16Mb
        Matcher spiFlashMatcher = SPI_FLASH_DENSITY_PATTERN.matcher(upperMpn);
        if (spiFlashMatcher.find()) {
            String densityCode = spiFlashMatcher.group(1);
            // Extract numeric part and interpret
            // 004 = 4Mb, 008 = 8Mb, 016 = 16Mb, 032 = 32Mb, etc.
            return switch (densityCode) {
                case "004" -> "4";
                case "008" -> "8";
                case "016" -> "16";
                case "032" -> "32";
                case "064" -> "64";
                case "128" -> "128";
                case "4" -> "4";
                case "8" -> "8";
                case "16" -> "16";
                case "32" -> "32";
                case "64" -> "64";
                default -> {
                    // Try parsing as number
                    String numStr = densityCode.replaceAll("[^0-9]", "");
                    if (!numStr.isEmpty()) {
                        int num = Integer.parseInt(numStr);
                        yield String.valueOf(num);
                    }
                    yield "";
                }
            };
        }

        // Parallel Flash (F49L series)
        // Format: F49L160UA = 16Mb parallel flash
        Matcher parallelFlashMatcher = PARALLEL_FLASH_DENSITY_PATTERN.matcher(upperMpn);
        if (parallelFlashMatcher.find()) {
            String densityCode = parallelFlashMatcher.group(1);
            // 160 = 16Mb, 320 = 32Mb, 640 = 64Mb (scaled encoding like some others)
            return switch (densityCode) {
                case "160" -> "16";
                case "320" -> "32";
                case "640" -> "64";
                case "800" -> "8";
                case "400" -> "4";
                default -> {
                    // Try interpreting first digits
                    if (densityCode.length() >= 2) {
                        String prefix = densityCode.substring(0, 2);
                        if (prefix.equals("16")) yield "16";
                        if (prefix.equals("32")) yield "32";
                        if (prefix.equals("64")) yield "64";
                    }
                    yield densityCode;
                }
            };
        }

        return "";
    }

    /**
     * Extract leading density value from digit string.
     */
    private String extractLeadingDensity(String digits) {
        if (digits == null || digits.isEmpty()) return "";

        // Common density values: 16, 32, 64, 128, 256, 512, 1024
        if (digits.startsWith("1024")) return "1024";
        if (digits.startsWith("512")) return "512";
        if (digits.startsWith("256")) return "256";
        if (digits.startsWith("128")) return "128";
        if (digits.startsWith("64")) return "64";
        if (digits.startsWith("32")) return "32";
        if (digits.startsWith("16")) return "16";
        if (digits.startsWith("8")) return "8";
        if (digits.startsWith("4")) return "4";

        return "";
    }

    /**
     * Extract memory configuration from MPN.
     * <p>
     * Examples:
     * <ul>
     *   <li>M12L128168A - returns "128x16x8" (128Mb = 128 x 16 bits x 8 banks)</li>
     *   <li>M12L64164A - returns "64x16x4" (64Mb = 64 x 16 bits x 4 banks)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return configuration string, or empty string if not extractable
     */
    public String extractConfiguration(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // SDRAM configuration encoded in part number
        // M12L128168A = 128Mb (8M x 16 bits x 8 banks)
        // M12L64164A = 64Mb (4M x 16 bits x 4 banks)
        Matcher sdramMatcher = SDRAM_DENSITY_PATTERN.matcher(upperMpn);
        if (sdramMatcher.find()) {
            String digits = sdramMatcher.group(1);
            if (digits.startsWith("128168")) return "8Mx16x8";
            if (digits.startsWith("64164")) return "4Mx16x4";
            if (digits.startsWith("32164")) return "2Mx16x4";
            if (digits.startsWith("16164")) return "1Mx16x4";
        }

        // DDR SDRAM configuration
        Matcher ddrMatcher = DDR_DENSITY_PATTERN.matcher(upperMpn);
        if (ddrMatcher.find()) {
            String digits = ddrMatcher.group(1);
            if (digits.startsWith("5121632")) return "32Mx16x2";
            if (digits.startsWith("2561616")) return "16Mx16x1";
            if (digits.startsWith("1281616")) return "8Mx16x1";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Must be same series
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Must be same density for replacement
        String density1 = extractDensity(mpn1);
        String density2 = extractDensity(mpn2);
        if (!density1.equals(density2) || density1.isEmpty()) {
            return false;
        }

        // Same series and same density = valid replacement
        // Package and speed differences are acceptable
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
