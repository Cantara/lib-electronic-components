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
 * Handler for Macronix International - Flash Memory ICs.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>MX25Lxxxx - Serial NOR Flash (standard)</li>
 *   <li>MX25Uxxxx - Ultra Low Power Serial Flash</li>
 *   <li>MX25Rxxxx - Wide Voltage Range Serial Flash</li>
 *   <li>MX25Vxxxx - Very Low Voltage Serial Flash</li>
 *   <li>MX66Lxxxx - High Performance Serial Flash</li>
 *   <li>MX29GLxxx - Parallel NOR Flash (high density)</li>
 *   <li>MX29LVxxx - Low Voltage Parallel Flash</li>
 *   <li>MX30LFxxxx - SLC NAND Flash</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>MX25L6433FMI-08G - 64Mb Serial NOR Flash, SOP-8 package</li>
 *   <li>MX25L12835FMI-10G - 128Mb Serial NOR Flash</li>
 *   <li>MX29GL256FHT2I-90Q - 256Mb Parallel NOR Flash</li>
 *   <li>MX30LF1G18AC - 1Gbit SLC NAND Flash</li>
 * </ul>
 */
public class MacronixHandler implements ManufacturerHandler {

    // Pattern for extracting density from MPN
    private static final Pattern SERIAL_NOR_DENSITY_PATTERN =
            Pattern.compile("MX(?:25[LURV]|66L)(\\d+)");
    private static final Pattern PARALLEL_NOR_DENSITY_PATTERN =
            Pattern.compile("MX29(?:GL|LV)(\\d+)");
    private static final Pattern NAND_DENSITY_PATTERN =
            Pattern.compile("MX30LF(\\d+)G");

    // Package code mappings
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("MI", "SOP-8"),
            Map.entry("MN", "DFN-8"),
            Map.entry("ZI", "WSON-8"),
            Map.entry("SI", "SOIC-8"),
            Map.entry("SS", "SOIC-8"),
            Map.entry("DI", "SOIC-16"),
            Map.entry("TI", "TSOP-8"),
            Map.entry("BH", "BGA"),
            Map.entry("FH", "FBGA"),
            Map.entry("HT", "TSOP-48"),
            Map.entry("AC", "TSOP-48"),
            Map.entry("ZB", "WSON-8"),
            Map.entry("EB", "BGA"),
            Map.entry("LI", "WLCSP")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Serial NOR Flash - MX25Lxxxx (standard)
        registry.addPattern(ComponentType.MEMORY, "^MX25L\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX25L\\d+.*");

        // Ultra Low Power Serial Flash - MX25Uxxxx
        registry.addPattern(ComponentType.MEMORY, "^MX25U\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX25U\\d+.*");

        // Wide Voltage Serial Flash - MX25Rxxxx
        registry.addPattern(ComponentType.MEMORY, "^MX25R\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX25R\\d+.*");

        // Very Low Voltage Serial Flash - MX25Vxxxx
        registry.addPattern(ComponentType.MEMORY, "^MX25V\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX25V\\d+.*");

        // High Performance Serial Flash - MX66Lxxxx
        registry.addPattern(ComponentType.MEMORY, "^MX66L\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX66L\\d+.*");

        // Parallel NOR Flash - MX29GLxxx (high density)
        registry.addPattern(ComponentType.MEMORY, "^MX29GL\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX29GL\\d+.*");

        // Low Voltage Parallel Flash - MX29LVxxx
        registry.addPattern(ComponentType.MEMORY, "^MX29LV\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX29LV\\d+.*");

        // SLC NAND Flash - MX30LFxxxx
        registry.addPattern(ComponentType.MEMORY, "^MX30LF\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^MX30LF\\d+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MEMORY,
                ComponentType.MEMORY_FLASH
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct checks for flash memory types
        if (type == ComponentType.MEMORY || type == ComponentType.MEMORY_FLASH) {
            // Serial NOR Flash families
            if (upperMpn.matches("^MX25[LURV]\\d+.*")) {
                return true;
            }
            // High Performance Serial NOR Flash
            if (upperMpn.matches("^MX66L\\d+.*")) {
                return true;
            }
            // Parallel NOR Flash
            if (upperMpn.matches("^MX29GL\\d+.*") || upperMpn.matches("^MX29LV\\d+.*")) {
                return true;
            }
            // SLC NAND Flash
            if (upperMpn.matches("^MX30LF\\d+.*")) {
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

        // For serial NOR flash (MX25L, MX25U, MX25R, MX25V, MX66L)
        // Package code typically appears after the density, e.g., MX25L6433FMI-08G
        // Format: MX25L<density><F/E><package>-<speed><grade>
        if (upperMpn.startsWith("MX25") || upperMpn.startsWith("MX66L")) {
            // Look for 2-letter package code after F or E
            int fIndex = upperMpn.indexOf('F');
            int eIndex = upperMpn.indexOf('E');
            int startIdx = Math.max(fIndex, eIndex);

            if (startIdx > 0 && startIdx + 3 <= upperMpn.length()) {
                String pkgCode = upperMpn.substring(startIdx + 1, startIdx + 3);
                // Check if it's a known package code
                if (PACKAGE_CODES.containsKey(pkgCode)) {
                    return PACKAGE_CODES.get(pkgCode);
                }
            }
        }

        // For parallel NOR flash (MX29GL, MX29LV)
        // Format: MX29GL256FHT2I-90Q where HT is TSOP-48
        if (upperMpn.startsWith("MX29")) {
            // Look for package code after F/E and before temperature grade
            Pattern pkgPattern = Pattern.compile("MX29(?:GL|LV)\\d+[FE]?(\\w{2})");
            Matcher matcher = pkgPattern.matcher(upperMpn);
            if (matcher.find()) {
                String pkgCode = matcher.group(1);
                if (PACKAGE_CODES.containsKey(pkgCode)) {
                    return PACKAGE_CODES.get(pkgCode);
                }
            }
        }

        // For NAND flash (MX30LF)
        // Format: MX30LF1G18AC where AC is package
        if (upperMpn.startsWith("MX30LF")) {
            // Extract last 2 chars before any hyphen
            int hyphenIdx = upperMpn.indexOf('-');
            String base = hyphenIdx > 0 ? upperMpn.substring(0, hyphenIdx) : upperMpn;
            if (base.length() >= 2) {
                String suffix = base.substring(base.length() - 2);
                if (PACKAGE_CODES.containsKey(suffix)) {
                    return PACKAGE_CODES.get(suffix);
                }
                return suffix; // Return raw suffix if not mapped
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Serial NOR Flash - MX25L, MX25U, MX25R, MX25V
        if (upperMpn.startsWith("MX25L")) return "MX25L";
        if (upperMpn.startsWith("MX25U")) return "MX25U";
        if (upperMpn.startsWith("MX25R")) return "MX25R";
        if (upperMpn.startsWith("MX25V")) return "MX25V";

        // High Performance Serial NOR Flash - MX66L
        if (upperMpn.startsWith("MX66L")) return "MX66L";

        // Parallel NOR Flash - MX29GL, MX29LV
        if (upperMpn.startsWith("MX29GL")) return "MX29GL";
        if (upperMpn.startsWith("MX29LV")) return "MX29LV";

        // SLC NAND Flash - MX30LF
        if (upperMpn.startsWith("MX30LF")) return "MX30LF";

        return "";
    }

    /**
     * Extract memory density from MPN.
     * <p>
     * Examples:
     * <ul>
     *   <li>MX25L6433F - returns "64" (64 Mbit)</li>
     *   <li>MX25L12833F - returns "128" (128 Mbit)</li>
     *   <li>MX29GL256 - returns "256" (256 Mbit)</li>
     *   <li>MX30LF1G18AC - returns "1G" (1 Gbit)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the density string, or empty string if not extractable
     */
    public String extractDensity(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Serial NOR Flash (MX25L, MX25U, MX25R, MX25V, MX66L)
        // Format: MX25L6433F means 64Mb (first 2-3 digits encode density)
        Matcher serialMatcher = SERIAL_NOR_DENSITY_PATTERN.matcher(upperMpn);
        if (serialMatcher.find()) {
            String densityDigits = serialMatcher.group(1);
            return extractDensityFromDigits(densityDigits);
        }

        // Parallel NOR Flash (MX29GL, MX29LV)
        // Format: MX29GL256 means 256Mb, MX29LV640 means 64Mb
        Matcher parallelMatcher = PARALLEL_NOR_DENSITY_PATTERN.matcher(upperMpn);
        if (parallelMatcher.find()) {
            String rawDensity = parallelMatcher.group(1);
            // MX29LV series uses scaled encoding: 640=64Mb, 160=16Mb, 320=32Mb
            return switch (rawDensity) {
                case "640" -> "64";
                case "160" -> "16";
                case "320" -> "32";
                case "800" -> "8";
                default -> rawDensity; // MX29GL uses direct encoding
            };
        }

        // NAND Flash (MX30LF)
        // Format: MX30LF1G means 1Gbit
        Matcher nandMatcher = NAND_DENSITY_PATTERN.matcher(upperMpn);
        if (nandMatcher.find()) {
            return nandMatcher.group(1) + "G";
        }

        return "";
    }

    /**
     * Convert Macronix serial flash density code to actual density.
     * <p>
     * Macronix uses a specific encoding for serial flash:
     * <ul>
     *   <li>6433 = 64 Mbit</li>
     *   <li>12833 = 128 Mbit</li>
     *   <li>25635 = 256 Mbit</li>
     *   <li>51235 = 512 Mbit</li>
     *   <li>1G = 1 Gbit</li>
     * </ul>
     */
    private String extractDensityFromDigits(String digits) {
        if (digits == null || digits.isEmpty()) return "";

        // Common Macronix density mappings
        // First 2-3 digits typically represent density
        if (digits.startsWith("6433") || digits.startsWith("6435")) return "64";
        if (digits.startsWith("12833") || digits.startsWith("12835")) return "128";
        if (digits.startsWith("25635") || digits.startsWith("25645")) return "256";
        if (digits.startsWith("51235") || digits.startsWith("51245")) return "512";
        if (digits.startsWith("1G")) return "1G";
        if (digits.startsWith("2G")) return "2G";

        // For simpler formats, try to extract leading digits
        StringBuilder density = new StringBuilder();
        for (char c : digits.toCharArray()) {
            if (Character.isDigit(c)) {
                density.append(c);
            } else if (c == 'G') {
                density.append(c);
                break;
            } else if (density.length() > 0) {
                break;
            }
        }

        // Interpret common density codes
        String result = density.toString();
        switch (result) {
            case "32": return "32";
            case "64": return "64";
            case "128": return "128";
            case "256": return "256";
            case "512": return "512";
            case "640": return "64"; // MX29LV640
            case "160": return "16"; // MX29LV160
            case "320": return "32"; // MX29LV320
            default:
                // Try first 2-3 chars as density
                if (result.length() >= 3) {
                    String prefix = result.substring(0, 3);
                    if (prefix.equals("128") || prefix.equals("256") || prefix.equals("512")) {
                        return prefix;
                    }
                }
                if (result.length() >= 2) {
                    String prefix = result.substring(0, 2);
                    if (prefix.equals("64") || prefix.equals("32") || prefix.equals("16")) {
                        return prefix;
                    }
                }
                return result;
        }
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
