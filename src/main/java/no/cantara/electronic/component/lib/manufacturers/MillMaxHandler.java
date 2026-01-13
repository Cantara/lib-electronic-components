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
 * Handler for Mill-Max Manufacturing precision pins, sockets, and connectors.
 *
 * Mill-Max is known for high-precision interconnects including:
 * - Spring-loaded pins/contacts (pogo pins): 0906, 0907, 0908 series
 * - IC sockets: 110, 510, 610 series
 * - Board-to-board connectors: 850, 851, 852 series
 * - Headers and receptacles: 300, 310, 800 series
 *
 * MPN structure typically: SERIES-SPEC1-SPEC2-SPEC3-...
 * Example: 0906-0-15-20-75-14-11-0 (spring-loaded pin)
 *          310-93-108-41-001000 (header)
 *
 * Package types: Through-hole (THT), Surface mount (SMT)
 */
public class MillMaxHandler implements ManufacturerHandler {

    // Spring-loaded pin patterns (pogo pins)
    // Format: 0906-0-15-20-75-14-11-0, 0907-x-xx-xx-xx-xx-xx-x, 0908-x-xx-xx-xx-xx-xx-x
    private static final Pattern SPRING_PIN_PATTERN = Pattern.compile(
            "^(090[6-8])-([0-9])-([0-9]{2})-([0-9]{2})-([0-9]{2})-([0-9]{2})-([0-9]{2})-([0-9])$");

    // IC socket patterns
    // Format: 110-xx-xxx-xx-xxxxxx, 510-xx-xxx-xx-xxxxxx, 610-xx-xxx-xx-xxxxxx
    private static final Pattern IC_SOCKET_PATTERN = Pattern.compile(
            "^([156]10)-([0-9]{2})-([0-9]{3})-([0-9]{2})-([0-9]+)$");

    // Board-to-board patterns
    // Format: 850-xx-xxx-xx-xxxxxx, 851-xx-xxx-xx-xxxxxx, 852-xx-xxx-xx-xxxxxx
    private static final Pattern BOARD_TO_BOARD_PATTERN = Pattern.compile(
            "^(85[0-2])-([0-9]{2})-([0-9]{3})-([0-9]{2})-([0-9]+)$");

    // Header and receptacle patterns
    // Format: 300-xx-xxx-xx-xxxxxx, 310-xx-xxx-xx-xxxxxx, 800-xx-xxx-xx-xxxxxx
    private static final Pattern HEADER_PATTERN = Pattern.compile(
            "^([38][01]0)-([0-9]{2})-([0-9]{3})-([0-9]{2})-([0-9]+)$");

    // Generic Mill-Max pattern (3-4 digit series followed by dash-separated numeric fields)
    private static final Pattern GENERIC_PATTERN = Pattern.compile(
            "^([0-9]{3,4})-[0-9]+-[0-9]+-[0-9]+-[0-9]+.*$");

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            Map.entry("0906", "Spring-Loaded Contact"),
            Map.entry("0907", "Spring-Loaded Contact"),
            Map.entry("0908", "Spring-Loaded Contact"),
            Map.entry("0965", "Spring-Loaded Contact"),
            Map.entry("0985", "Spring-Loaded Contact"),
            Map.entry("110", "IC Socket"),
            Map.entry("510", "IC Socket"),
            Map.entry("610", "IC Socket"),
            Map.entry("850", "Board-to-Board"),
            Map.entry("851", "Board-to-Board"),
            Map.entry("852", "Board-to-Board"),
            Map.entry("300", "Header"),
            Map.entry("310", "Header"),
            Map.entry("311", "Header"),
            Map.entry("800", "Receptacle"),
            Map.entry("801", "Receptacle"),
            Map.entry("350", "Single Row Socket"),
            Map.entry("315", "SMT Header"),
            Map.entry("316", "SMT Header")
    );

    // Series to mounting type mapping
    private static final Map<String, String> SERIES_MOUNTING = Map.ofEntries(
            Map.entry("0906", "THT"),
            Map.entry("0907", "SMT"),
            Map.entry("0908", "THT"),
            Map.entry("0965", "SMT"),
            Map.entry("0985", "SMT"),
            Map.entry("110", "THT"),
            Map.entry("510", "SMT"),
            Map.entry("610", "THT"),
            Map.entry("850", "THT"),
            Map.entry("851", "SMT"),
            Map.entry("852", "THT"),
            Map.entry("300", "THT"),
            Map.entry("310", "THT"),
            Map.entry("311", "SMT"),
            Map.entry("315", "SMT"),
            Map.entry("316", "SMT"),
            Map.entry("800", "THT"),
            Map.entry("801", "SMT")
    );

    // Series to application type mapping
    private static final Map<String, String> SERIES_APPLICATION = Map.ofEntries(
            Map.entry("0906", "Test/Programming"),
            Map.entry("0907", "Test/Programming"),
            Map.entry("0908", "Test/Programming"),
            Map.entry("0965", "Battery Contact"),
            Map.entry("0985", "Board-to-Board Spring"),
            Map.entry("110", "DIP IC Socket"),
            Map.entry("510", "PLCC Socket"),
            Map.entry("610", "PGA Socket"),
            Map.entry("850", "High Density B2B"),
            Map.entry("851", "High Density B2B SMT"),
            Map.entry("852", "High Density B2B"),
            Map.entry("300", "Pin Header"),
            Map.entry("310", "Pin Header"),
            Map.entry("800", "Pin Receptacle"),
            Map.entry("801", "Pin Receptacle SMT")
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
        // Spring-loaded pins (pogo pins) - 0906, 0907, 0908, 0965, 0985 series
        registry.addPattern(ComponentType.CONNECTOR, "^090[6-8]-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]$");
        registry.addPattern(ComponentType.CONNECTOR, "^096[0-9]-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]$");
        registry.addPattern(ComponentType.CONNECTOR, "^098[0-9]-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]$");

        // IC sockets - 110, 510, 610 series
        registry.addPattern(ComponentType.IC, "^110-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");
        registry.addPattern(ComponentType.IC, "^510-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");
        registry.addPattern(ComponentType.IC, "^610-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");
        registry.addPattern(ComponentType.CONNECTOR, "^110-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");
        registry.addPattern(ComponentType.CONNECTOR, "^510-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");
        registry.addPattern(ComponentType.CONNECTOR, "^610-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");

        // Board-to-board - 850, 851, 852 series
        registry.addPattern(ComponentType.CONNECTOR, "^85[0-2]-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");

        // Headers - 300, 310, 311, 315, 316 series
        registry.addPattern(ComponentType.CONNECTOR, "^3[01][0156]-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");

        // Receptacles - 800, 801 series
        registry.addPattern(ComponentType.CONNECTOR, "^80[01]-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");

        // Single row sockets - 350 series
        registry.addPattern(ComponentType.CONNECTOR, "^350-[0-9]+-[0-9]+-[0-9]+-[0-9]+$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Check IC socket types for IC
        if (type == ComponentType.IC) {
            if (upperMpn.matches("^110-[0-9]+-[0-9]+-[0-9]+-[0-9]+$") ||
                upperMpn.matches("^510-[0-9]+-[0-9]+-[0-9]+-[0-9]+$") ||
                upperMpn.matches("^610-[0-9]+-[0-9]+-[0-9]+-[0-9]+$")) {
                return true;
            }
        }

        // Check all connector patterns
        if (type == ComponentType.CONNECTOR) {
            // Spring pins
            if (upperMpn.matches("^090[6-8]-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]$") ||
                upperMpn.matches("^096[0-9]-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]$") ||
                upperMpn.matches("^098[0-9]-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]+-[0-9]$")) {
                return true;
            }
            // IC sockets (also connectors)
            if (upperMpn.matches("^[156]10-[0-9]+-[0-9]+-[0-9]+-[0-9]+$")) {
                return true;
            }
            // Board-to-board
            if (upperMpn.matches("^85[0-2]-[0-9]+-[0-9]+-[0-9]+-[0-9]+$")) {
                return true;
            }
            // Headers
            if (upperMpn.matches("^3[01][0156]-[0-9]+-[0-9]+-[0-9]+-[0-9]+$")) {
                return true;
            }
            // Receptacles
            if (upperMpn.matches("^80[01]-[0-9]+-[0-9]+-[0-9]+-[0-9]+$")) {
                return true;
            }
            // Single row sockets
            if (upperMpn.matches("^350-[0-9]+-[0-9]+-[0-9]+-[0-9]+$")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        // For Mill-Max, package code typically determined by series (THT vs SMT)
        String mounting = SERIES_MOUNTING.get(series);
        if (mounting != null) {
            return mounting;
        }

        // Try to extract from the MPN structure
        // The last field often indicates packaging/plating option
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract series from the beginning of the MPN
        int firstDash = mpn.indexOf('-');
        if (firstDash > 0) {
            String series = mpn.substring(0, firstDash);
            // Validate that it's a known series
            if (SERIES_FAMILIES.containsKey(series)) {
                return series;
            }
            // Return it anyway if it looks like a valid Mill-Max series (3-4 digits)
            if (series.matches("^[0-9]{3,4}$")) {
                return series;
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from the exact same series (not just same family)
        // Different series have different mounting types and mechanical properties
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Must be from same series family
        String family1 = SERIES_FAMILIES.get(series1);
        if (family1 == null) {
            return false;
        }

        // For spring pins, check if they have similar specifications
        if (family1.equals("Spring-Loaded Contact")) {
            return areCompatibleSpringPins(mpn1, mpn2);
        }

        // For headers/receptacles, check pin compatibility
        if (family1.equals("Header") || family1.equals("Receptacle")) {
            return areCompatibleHeaders(mpn1, mpn2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    /**
     * Check if two spring pins are compatible replacements.
     * Spring pins are compatible if they have the same travel and force characteristics.
     */
    private boolean areCompatibleSpringPins(String mpn1, String mpn2) {
        Matcher m1 = SPRING_PIN_PATTERN.matcher(mpn1);
        Matcher m2 = SPRING_PIN_PATTERN.matcher(mpn2);

        if (!m1.matches() || !m2.matches()) {
            return false;
        }

        // Compare key characteristics (groups 3-6 typically represent travel/force specs)
        // They should be identical for replacement compatibility
        return m1.group(3).equals(m2.group(3)) &&  // Travel specification
               m1.group(4).equals(m2.group(4)) &&  // Force specification
               m1.group(5).equals(m2.group(5));    // Stroke specification
    }

    /**
     * Check if two headers are compatible replacements.
     * Headers are compatible if they have the same pin count and pitch.
     */
    private boolean areCompatibleHeaders(String mpn1, String mpn2) {
        Matcher m1 = HEADER_PATTERN.matcher(mpn1);
        Matcher m2 = HEADER_PATTERN.matcher(mpn2);

        if (!m1.matches() || !m2.matches()) {
            return false;
        }

        // Pin count is typically in group 3 (the 3-digit field)
        return m1.group(3).equals(m2.group(3));
    }

    /**
     * Get the product family for a given MPN.
     */
    public String getFamily(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_FAMILIES.getOrDefault(series, "Unknown");
    }

    /**
     * Get the mounting type (THT or SMT) for a given MPN.
     */
    public String getMountingType(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_MOUNTING.getOrDefault(series, "Unknown");
    }

    /**
     * Get the application type for a given MPN.
     */
    public String getApplicationType(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_APPLICATION.getOrDefault(series, "General Purpose");
    }

    /**
     * Check if the component is a spring-loaded contact (pogo pin).
     */
    public boolean isSpringLoaded(String mpn) {
        String series = extractSeries(mpn);
        String family = SERIES_FAMILIES.get(series);
        return "Spring-Loaded Contact".equals(family);
    }

    /**
     * Check if the component is an IC socket.
     */
    public boolean isICSocket(String mpn) {
        String series = extractSeries(mpn);
        String family = SERIES_FAMILIES.get(series);
        return "IC Socket".equals(family);
    }

    /**
     * Check if the component is a board-to-board connector.
     */
    public boolean isBoardToBoard(String mpn) {
        String series = extractSeries(mpn);
        String family = SERIES_FAMILIES.get(series);
        return "Board-to-Board".equals(family);
    }

    /**
     * Check if the component is surface mount.
     */
    public boolean isSurfaceMount(String mpn) {
        return "SMT".equals(getMountingType(mpn));
    }

    /**
     * Check if the component is through-hole.
     */
    public boolean isThroughHole(String mpn) {
        return "THT".equals(getMountingType(mpn));
    }

    /**
     * Extract pin count from header/receptacle MPNs.
     * The pin count is typically encoded in the third field (3-digit number).
     */
    public int extractPinCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String[] parts = mpn.split("-");
        if (parts.length >= 3) {
            try {
                // For headers and receptacles, the third field often indicates pin count
                return Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
