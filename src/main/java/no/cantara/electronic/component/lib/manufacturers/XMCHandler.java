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
 * Handler for XMC (Wuhan Xinxin Semiconductor) - SPI NOR Flash Memory ICs.
 * <p>
 * Supports the following product families:
 * <ul>
 *   <li>XM25QH series - Standard SPI NOR Flash (3.3V)</li>
 *   <li>XM25QU series - Low Voltage SPI NOR Flash (1.8V)</li>
 *   <li>XM25LU series - Ultra Low Voltage SPI NOR Flash</li>
 * </ul>
 * <p>
 * MPN Format examples:
 * <ul>
 *   <li>XM25QH64A - 64Mbit SPI NOR Flash, SOIC-8 package</li>
 *   <li>XM25QH128A - 128Mbit SPI NOR Flash, SOIC-8 package</li>
 *   <li>XM25QH256B - 256Mbit SPI NOR Flash, SOIC-16 package</li>
 *   <li>XM25QU64A - 64Mbit 1.8V SPI NOR Flash</li>
 *   <li>XM25LU128 - 128Mbit Ultra Low Voltage Flash</li>
 * </ul>
 * <p>
 * Density encoding:
 * <ul>
 *   <li>32 = 32 Mbit (4 MB)</li>
 *   <li>64 = 64 Mbit (8 MB)</li>
 *   <li>128 = 128 Mbit (16 MB)</li>
 *   <li>256 = 256 Mbit (32 MB)</li>
 *   <li>512 = 512 Mbit (64 MB)</li>
 * </ul>
 * <p>
 * Package codes:
 * <ul>
 *   <li>A = SOIC-8</li>
 *   <li>B = SOIC-16</li>
 *   <li>C = WSON-8</li>
 *   <li>D = USON-8</li>
 * </ul>
 */
public class XMCHandler implements ManufacturerHandler {

    // Pattern for extracting density from MPN
    // XM25QH64A -> captures 64
    // XM25QU128B -> captures 128
    // XM25LU256 -> captures 256
    private static final Pattern DENSITY_PATTERN =
            Pattern.compile("XM25(?:QH|QU|LU)(\\d+)");

    // Pattern for XM25QH series (3.3V standard)
    private static final Pattern XM25QH_PATTERN =
            Pattern.compile("^XM25QH\\d+[A-Z]*.*", Pattern.CASE_INSENSITIVE);

    // Pattern for XM25QU series (1.8V low voltage)
    private static final Pattern XM25QU_PATTERN =
            Pattern.compile("^XM25QU\\d+[A-Z]*.*", Pattern.CASE_INSENSITIVE);

    // Pattern for XM25LU series (ultra low voltage)
    private static final Pattern XM25LU_PATTERN =
            Pattern.compile("^XM25LU\\d+[A-Z]*.*", Pattern.CASE_INSENSITIVE);

    // Package code mappings
    private static final Map<String, String> PACKAGE_CODES = Map.of(
            "A", "SOIC-8",
            "B", "SOIC-16",
            "C", "WSON-8",
            "D", "USON-8"
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // XM25QH series - Standard 3.3V SPI NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^XM25QH\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^XM25QH\\d+.*");
        registry.addPattern(ComponentType.IC, "^XM25QH\\d+.*");

        // XM25QU series - 1.8V Low Voltage SPI NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^XM25QU\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^XM25QU\\d+.*");
        registry.addPattern(ComponentType.IC, "^XM25QU\\d+.*");

        // XM25LU series - Ultra Low Voltage SPI NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^XM25LU\\d+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^XM25LU\\d+.*");
        registry.addPattern(ComponentType.IC, "^XM25LU\\d+.*");
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
        if (type == ComponentType.MEMORY || type == ComponentType.MEMORY_FLASH || type == ComponentType.IC) {
            // XM25QH series - Standard 3.3V SPI NOR Flash
            if (XM25QH_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // XM25QU series - 1.8V Low Voltage SPI NOR Flash
            if (XM25QU_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
            // XM25LU series - Ultra Low Voltage SPI NOR Flash
            if (XM25LU_PATTERN.matcher(upperMpn).matches()) {
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

        // XMC flash format: XM25QHxxxY where Y is the package code
        // Extract the letter suffix after the density digits
        // XM25QH64A -> A (SOIC-8)
        // XM25QH128B -> B (SOIC-16)
        // XM25QU256C -> C (WSON-8)

        // Find the density portion and extract the suffix after it
        Matcher matcher = DENSITY_PATTERN.matcher(upperMpn);
        if (matcher.find()) {
            int densityEnd = matcher.end();
            if (densityEnd < upperMpn.length()) {
                // Get the character(s) after the density
                String suffix = upperMpn.substring(densityEnd);
                if (!suffix.isEmpty()) {
                    String pkgCode = suffix.substring(0, 1);
                    // Check if it's a known package code
                    if (PACKAGE_CODES.containsKey(pkgCode)) {
                        return PACKAGE_CODES.get(pkgCode);
                    }
                    // Return the raw suffix if not a known package code
                    return pkgCode;
                }
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // XM25QH series - Standard 3.3V SPI NOR Flash
        if (upperMpn.startsWith("XM25QH")) return "XM25QH";

        // XM25QU series - 1.8V Low Voltage SPI NOR Flash
        if (upperMpn.startsWith("XM25QU")) return "XM25QU";

        // XM25LU series - Ultra Low Voltage SPI NOR Flash
        if (upperMpn.startsWith("XM25LU")) return "XM25LU";

        return "";
    }

    /**
     * Extract memory density from MPN.
     * <p>
     * Examples:
     * <ul>
     *   <li>XM25QH64A - returns "64" (64 Mbit)</li>
     *   <li>XM25QH128B - returns "128" (128 Mbit)</li>
     *   <li>XM25QU256C - returns "256" (256 Mbit)</li>
     *   <li>XM25LU512 - returns "512" (512 Mbit)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the density string in Mbits, or empty string if not extractable
     */
    public String extractDensity(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        Matcher matcher = DENSITY_PATTERN.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    /**
     * Get the voltage range for a given MPN.
     * <p>
     * Determines the operating voltage based on the series:
     * <ul>
     *   <li>XM25QH = 2.7V - 3.6V (standard 3.3V)</li>
     *   <li>XM25QU = 1.65V - 2.0V (1.8V low voltage)</li>
     *   <li>XM25LU = 1.65V - 1.95V (ultra low voltage)</li>
     * </ul>
     *
     * @param mpn the manufacturer part number
     * @return the voltage range string, or empty string if not determinable
     */
    public String getVoltageRange(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "XM25QH" -> "2.7V-3.6V";
            case "XM25QU" -> "1.65V-2.0V";
            case "XM25LU" -> "1.65V-1.95V";
            default -> "";
        };
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Must be same series for replacement
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
        // Package differences are acceptable for replacement
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
