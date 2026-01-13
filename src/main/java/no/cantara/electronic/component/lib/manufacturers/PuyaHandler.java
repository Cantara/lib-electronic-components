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
 * Handler for Puya Semiconductor - SPI NOR Flash Memory ICs.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>P25Q series - Standard SPI NOR Flash (P25Q80H, P25Q16H, P25Q32H, P25Q64H, P25Q128H)</li>
 *   <li>P25D series - Low Power SPI NOR Flash</li>
 *   <li>PY25Q series - Automotive Grade SPI NOR Flash</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>P25Q80H - 8Mbit SPI NOR Flash, SOIC-8 package</li>
 *   <li>P25Q16H-SSH - 16Mbit SPI NOR Flash, SOIC-8 package</li>
 *   <li>P25Q32H-SUH - 32Mbit SPI NOR Flash, WSON-8 package</li>
 *   <li>P25Q64H - 64Mbit SPI NOR Flash</li>
 *   <li>P25Q128H - 128Mbit SPI NOR Flash</li>
 *   <li>P25D80H - 8Mbit Low Power Flash</li>
 *   <li>PY25Q128HA - 128Mbit Automotive Grade Flash</li>
 * </ul>
 * <p>
 * Density codes:
 * <ul>
 *   <li>80 = 8 Mbit</li>
 *   <li>16 = 16 Mbit</li>
 *   <li>32 = 32 Mbit</li>
 *   <li>64 = 64 Mbit</li>
 *   <li>128 = 128 Mbit</li>
 * </ul>
 * <p>
 * Package codes:
 * <ul>
 *   <li>H = SOIC-8 (standard)</li>
 *   <li>U = USON-8</li>
 *   <li>SH = SOIC-8 wide</li>
 *   <li>SU = WSON-8</li>
 * </ul>
 */
public class PuyaHandler implements ManufacturerHandler {

    // Pattern for extracting density from MPN
    private static final Pattern DENSITY_PATTERN = Pattern.compile("P(?:Y)?25[QD](\\d+)");

    // Package code mappings
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("H", "SOIC-8"),
            Map.entry("U", "USON-8"),
            Map.entry("SH", "SOIC-8-WIDE"),
            Map.entry("SU", "WSON-8"),
            Map.entry("SSH", "SOIC-8"),
            Map.entry("SUH", "WSON-8")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // P25Q series - Standard SPI NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^P25Q\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^P25Q\\d+.*");
        registry.addPattern(ComponentType.IC, "^P25Q\\d+.*");

        // P25D series - Low Power SPI NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^P25D\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^P25D\\d+.*");
        registry.addPattern(ComponentType.IC, "^P25D\\d+.*");

        // PY25Q series - Automotive Grade SPI NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^PY25Q\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^PY25Q\\d+.*");
        registry.addPattern(ComponentType.IC, "^PY25Q\\d+.*");
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

        // Direct checks for flash memory types
        if (type == ComponentType.MEMORY ||
                type == ComponentType.MEMORY_FLASH ||
                type == ComponentType.IC) {

            // P25Q series - Standard SPI NOR Flash
            if (upperMpn.matches("^P25Q\\d+.*")) {
                return true;
            }
            // P25D series - Low Power SPI NOR Flash
            if (upperMpn.matches("^P25D\\d+.*")) {
                return true;
            }
            // PY25Q series - Automotive Grade SPI NOR Flash
            if (upperMpn.matches("^PY25Q\\d+.*")) {
                return true;
            }
        }

        // Use handler-specific patterns for other matches (avoid cross-handler false matches)
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check if it's a Puya flash part
        if (!upperMpn.matches("^P(?:Y)?25[QD]\\d+.*")) {
            return "";
        }

        // Handle hyphenated suffixes first (e.g., P25Q16H-SSH, P25Q32H-SUH)
        int hyphenIndex = upperMpn.indexOf('-');
        if (hyphenIndex > 0 && hyphenIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(hyphenIndex + 1);
            if (PACKAGE_CODES.containsKey(suffix)) {
                return PACKAGE_CODES.get(suffix);
            }
        }

        // Extract trailing package code (e.g., P25Q80H -> H)
        // Find where the density ends (after digits following P25Q/P25D/PY25Q)
        Matcher densityMatcher = DENSITY_PATTERN.matcher(upperMpn);
        if (densityMatcher.find()) {
            int densityEnd = densityMatcher.end();
            if (densityEnd < upperMpn.length()) {
                String suffix = upperMpn.substring(densityEnd);
                // Remove any trailing temperature/grade codes
                if (suffix.contains("-")) {
                    suffix = suffix.substring(0, suffix.indexOf('-'));
                }
                // Check longer suffixes first
                if (suffix.length() >= 2) {
                    String twoChar = suffix.substring(0, 2);
                    if (PACKAGE_CODES.containsKey(twoChar)) {
                        return PACKAGE_CODES.get(twoChar);
                    }
                }
                if (suffix.length() >= 1) {
                    String oneChar = suffix.substring(0, 1);
                    if (PACKAGE_CODES.containsKey(oneChar)) {
                        return PACKAGE_CODES.get(oneChar);
                    }
                }
                // Return raw suffix if not mapped
                if (!suffix.isEmpty()) {
                    return suffix;
                }
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // PY25Q series - Automotive Grade (check first as it's more specific)
        if (upperMpn.startsWith("PY25Q")) return "PY25Q";

        // P25Q series - Standard SPI NOR Flash
        if (upperMpn.startsWith("P25Q")) return "P25Q";

        // P25D series - Low Power SPI NOR Flash
        if (upperMpn.startsWith("P25D")) return "P25D";

        return "";
    }

    /**
     * Extract memory density from MPN.
     * <p>
     * Examples:
     * <ul>
     *   <li>P25Q80H - returns "8" (8 Mbit)</li>
     *   <li>P25Q16H - returns "16" (16 Mbit)</li>
     *   <li>P25Q32H - returns "32" (32 Mbit)</li>
     *   <li>P25Q64H - returns "64" (64 Mbit)</li>
     *   <li>P25Q128H - returns "128" (128 Mbit)</li>
     *   <li>PY25Q128HA - returns "128" (128 Mbit)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the density in Mbit, or empty string if not extractable
     */
    public String extractDensity(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        Matcher matcher = DENSITY_PATTERN.matcher(upperMpn);
        if (matcher.find()) {
            String densityCode = matcher.group(1);
            // Puya uses direct encoding for most parts
            // Special case: 80 = 8Mbit (not 80Mbit)
            return switch (densityCode) {
                case "80" -> "8";
                default -> densityCode;
            };
        }

        return "";
    }

    /**
     * Extract the grade from MPN (standard vs automotive).
     * <p>
     * Examples:
     * <ul>
     *   <li>P25Q80H - returns "Standard"</li>
     *   <li>P25D80H - returns "Low Power"</li>
     *   <li>PY25Q128HA - returns "Automotive"</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the grade description, or empty string if not extractable
     */
    public String extractGrade(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("PY25Q")) return "Automotive";
        if (upperMpn.startsWith("P25D")) return "Low Power";
        if (upperMpn.startsWith("P25Q")) return "Standard";

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
        // Package differences are acceptable
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
