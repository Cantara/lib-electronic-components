package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manufacturer handler for Alliance Memory products.
 *
 * Alliance Memory Part Number Structure:
 * - AS6C series: Async SRAM (AS6C1008, AS6C4008, AS6C62256)
 * - AS7C series: High-speed SRAM (AS7C1024, AS7C256)
 * - AS4C series: SDRAM and DDR (AS4C4M16SA, AS4C16M16SA, AS4C256M16D3)
 * - AS29LV series: NOR Flash
 *
 * Example formats:
 * - AS6C4008-55PCN (Async SRAM, 55ns, PDIP package, Commercial, Lead-free)
 * - AS7C1024A-12JCN (High-speed SRAM, 12ns, PLCC package, Commercial, Lead-free)
 * - AS4C4M16SA-6TIN (SDRAM, 6ns, TSOP package, Industrial, Lead-free)
 * - AS4C256M16D3A-12BCN (DDR3 SDRAM, 12ns, BGA package, Commercial, Lead-free)
 * - AS29LV160B-90TC (NOR Flash, 90ns, TSOP package, Commercial)
 *
 * Package codes: P=PDIP, T=TSOP, B=BGA, J=PLCC, S=SOIC, V=TSSOP
 * Temperature grades: C=Commercial (0-70), I=Industrial (-40 to +85), A=Automotive (-40 to +125)
 */
public class AllianceMemoryHandler implements ManufacturerHandler {

    // Patterns for different memory types
    private static final Pattern AS6C_PATTERN = Pattern.compile("^AS6C[0-9A-Z]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern AS7C_PATTERN = Pattern.compile("^AS7C[0-9A-Z]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern AS4C_PATTERN = Pattern.compile("^AS4C[0-9A-Z]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern AS29LV_PATTERN = Pattern.compile("^AS29LV[0-9A-Z]+.*", Pattern.CASE_INSENSITIVE);

    // Pattern to extract density from part number
    private static final Pattern DENSITY_PATTERN = Pattern.compile(
        "AS[0-9][A-Z]([0-9]+[MKG]?[0-9]*)",
        Pattern.CASE_INSENSITIVE
    );

    // Pattern to extract speed grade (digits after hyphen)
    private static final Pattern SPEED_PATTERN = Pattern.compile(
        "-([0-9]+)",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // AS6C series - Async SRAM (including low power variants)
        registry.addPattern(ComponentType.MEMORY, "^AS6C[0-9]+.*");
        registry.addPattern(ComponentType.MEMORY, "^AS6C[0-9]+[A-Z]*-.*");

        // AS7C series - High-speed SRAM
        registry.addPattern(ComponentType.MEMORY, "^AS7C[0-9]+.*");
        registry.addPattern(ComponentType.MEMORY, "^AS7C[0-9]+[A-Z]*-.*");

        // AS4C series - SDRAM (includes DDR1, DDR2, DDR3)
        registry.addPattern(ComponentType.MEMORY, "^AS4C[0-9]+M[0-9]+.*");
        registry.addPattern(ComponentType.MEMORY, "^AS4C[0-9]+M[0-9]+[A-Z]*-.*");

        // AS29LV series - NOR Flash
        registry.addPattern(ComponentType.MEMORY, "^AS29LV[0-9]+.*");
        registry.addPattern(ComponentType.MEMORY_FLASH, "^AS29LV[0-9]+.*");
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

        // Check for SRAM types
        if (type == ComponentType.MEMORY) {
            if (AS6C_PATTERN.matcher(upperMpn).matches() ||
                AS7C_PATTERN.matcher(upperMpn).matches() ||
                AS4C_PATTERN.matcher(upperMpn).matches() ||
                AS29LV_PATTERN.matcher(upperMpn).matches()) {
                return true;
            }
        }

        // Check for Flash types
        if (type == ComponentType.MEMORY_FLASH) {
            if (AS29LV_PATTERN.matcher(upperMpn).matches()) {
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

        // Package code is typically after the hyphen and speed grade
        // Format: AS6C4008-55PCN -> Package is 'P' (PDIP)
        // Format: AS4C256M16D3A-12BCN -> Package is 'B' (BGA)
        int hyphenIndex = upperMpn.indexOf('-');
        if (hyphenIndex < 0 || hyphenIndex >= upperMpn.length() - 1) {
            return "";
        }

        String suffix = upperMpn.substring(hyphenIndex + 1);

        // Skip the speed grade digits at the start
        int i = 0;
        while (i < suffix.length() && Character.isDigit(suffix.charAt(i))) {
            i++;
        }

        if (i >= suffix.length()) {
            return "";
        }

        // The first letter after the speed grade is the package code
        char packageChar = suffix.charAt(i);

        return switch (packageChar) {
            case 'P' -> "PDIP";
            case 'T' -> "TSOP";
            case 'B' -> "BGA";
            case 'J' -> "PLCC";
            case 'S' -> "SOIC";
            case 'V' -> "TSSOP";
            case 'Z' -> "FBGA";
            case 'W' -> "WSON";
            case 'Q' -> "QFN";
            default -> String.valueOf(packageChar);
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract the series prefix (AS6C, AS7C, AS4C, AS29LV)
        if (upperMpn.startsWith("AS29LV")) {
            return "AS29LV";
        }
        if (upperMpn.startsWith("AS6C")) {
            return "AS6C";
        }
        if (upperMpn.startsWith("AS7C")) {
            return "AS7C";
        }
        if (upperMpn.startsWith("AS4C")) {
            return "AS4C";
        }

        return "";
    }

    /**
     * Extracts memory density from the part number.
     *
     * Examples:
     * - AS6C4008 -> 4008 (4Mbit organized as 512K x 8)
     * - AS6C62256 -> 62256 (256Kbit organized as 32K x 8)
     * - AS7C1024 -> 1024 (1Mbit)
     * - AS4C4M16SA -> 4M16 (4M x 16 = 64Mbit)
     * - AS4C256M16D3 -> 256M16 (256M x 16 = 4Gbit)
     */
    public String extractDensity(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // For AS4C series (SDRAM/DDR), extract the density differently
        // Format: AS4C[density]M[width][type]
        // Example: AS4C4M16SA -> 4M16 (4Mbit x 16)
        // Example: AS4C256M16D3A -> 256M16 (256Mbit x 16)
        if (upperMpn.startsWith("AS4C")) {
            Pattern sdramPattern = Pattern.compile("AS4C([0-9]+M[0-9]+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = sdramPattern.matcher(upperMpn);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        // For AS6C and AS7C series (SRAM), extract numeric density after prefix
        if (upperMpn.startsWith("AS6C") || upperMpn.startsWith("AS7C")) {
            // Extract digits after the AS6C or AS7C prefix
            String remainder = upperMpn.startsWith("AS6C")
                ? upperMpn.substring(4)
                : upperMpn.substring(4);

            StringBuilder density = new StringBuilder();
            for (char c : remainder.toCharArray()) {
                if (Character.isDigit(c)) {
                    density.append(c);
                } else {
                    break;
                }
            }
            return density.toString();
        }

        // For AS29LV (Flash), extract density
        if (upperMpn.startsWith("AS29LV")) {
            String remainder = upperMpn.substring(6);
            StringBuilder density = new StringBuilder();
            for (char c : remainder.toCharArray()) {
                if (Character.isDigit(c)) {
                    density.append(c);
                } else {
                    break;
                }
            }
            return density.toString();
        }

        return "";
    }

    /**
     * Extracts speed grade from part number.
     * Speed grade is the number after the hyphen, representing access time in nanoseconds.
     *
     * Examples:
     * - AS6C4008-55PCN -> 55ns
     * - AS7C1024A-12JCN -> 12ns
     * - AS4C4M16SA-6TIN -> 6ns
     */
    public String extractSpeedGrade(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        Matcher matcher = SPEED_PATTERN.matcher(mpn.toUpperCase());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * Extracts temperature grade from part number.
     * Temperature grade is typically after the package code.
     *
     * Examples:
     * - AS6C4008-55PCN -> C (Commercial: 0 to +70C)
     * - AS4C4M16SA-6TIN -> I (Industrial: -40 to +85C)
     */
    public String extractTemperatureGrade(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        int hyphenIndex = upperMpn.indexOf('-');
        if (hyphenIndex < 0) return "";

        String suffix = upperMpn.substring(hyphenIndex + 1);

        // Temperature grade is typically the second-to-last or last letter
        // Format: -55PCN (P=package, C=temp, N=lead-free)
        // Format: -6TIN (T=package, I=temp, N=lead-free)
        if (suffix.length() >= 2) {
            // Look for temperature indicators
            for (int i = suffix.length() - 1; i >= 0; i--) {
                char c = suffix.charAt(i);
                if (c == 'C' || c == 'I' || c == 'A') {
                    // Check it's not the first character (package code) or last (lead-free indicator)
                    if (i > 0 && i < suffix.length() - 1) {
                        return String.valueOf(c);
                    }
                    // Could be at the end without lead-free indicator
                    if (i == suffix.length() - 1 && i > 1) {
                        char prev = suffix.charAt(i - 1);
                        if (!Character.isDigit(prev) && prev != 'N') {
                            return String.valueOf(c);
                        }
                    }
                }
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Must be same series for replacement
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Must be same density for replacement
        String density1 = extractDensity(mpn1);
        String density2 = extractDensity(mpn2);

        if (density1.isEmpty() || !density1.equals(density2)) {
            return false;
        }

        // Check speed grade compatibility
        // Faster parts (lower ns) can replace slower parts
        String speed1 = extractSpeedGrade(mpn1);
        String speed2 = extractSpeedGrade(mpn2);

        if (!speed1.isEmpty() && !speed2.isEmpty()) {
            try {
                int speed1Val = Integer.parseInt(speed1);
                int speed2Val = Integer.parseInt(speed2);
                // Lower number = faster, can replace slower
                if (speed1Val > speed2Val) {
                    return false;
                }
            } catch (NumberFormatException e) {
                // If parsing fails, still allow if density matches
            }
        }

        // Check temperature grade compatibility
        // Wider temp range can replace narrower
        String temp1 = extractTemperatureGrade(mpn1);
        String temp2 = extractTemperatureGrade(mpn2);

        if (!temp1.isEmpty() && !temp2.isEmpty()) {
            // Automotive (-40 to +125) > Industrial (-40 to +85) > Commercial (0 to +70)
            int tempRank1 = getTemperatureRank(temp1);
            int tempRank2 = getTemperatureRank(temp2);

            // Can replace if temp range is same or wider
            if (tempRank1 < tempRank2) {
                return false;
            }
        }

        return true;
    }

    private int getTemperatureRank(String tempGrade) {
        return switch (tempGrade.toUpperCase()) {
            case "C" -> 1;  // Commercial (narrowest)
            case "I" -> 2;  // Industrial
            case "A" -> 3;  // Automotive (widest)
            default -> 0;
        };
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
