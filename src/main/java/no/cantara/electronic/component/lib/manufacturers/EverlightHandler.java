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
 * Handler for Everlight Electronics components.
 *
 * Everlight is a major LED and optocoupler manufacturer producing:
 * - Standard SMD LEDs (17-21, 19-21, 26-21 series based on package size)
 * - High-power SMD LEDs (ELSH series)
 * - SMD LEDs (EL-SSD series)
 * - Through-hole LEDs (333-2 series)
 * - RGB LEDs (19-337 series)
 * - High-power LEDs (Shwo series, EASP series)
 * - Infrared LEDs (IR333, IR908)
 * - Phototransistors (PT333, PT908, PT19-21)
 * - Optocouplers (EL817, EL357, EL3063)
 * - Light Sensors (ALS-PT19-315)
 * - Seven-segment displays
 *
 * MPN Structure:
 *
 * Standard SMD LEDs:
 * [size]-[color][brightness][bin]/[lens]/[packing]
 * e.g., 17-21SURC/S530-A3/TR8
 * - 17-21 = 0603 package size
 * - 19-21 = 0805 package size
 * - 26-21 = 1206 package size
 * - SURC = Super Red Clear lens
 * - S530 = brightness bin
 * - A3 = color bin
 * - TR8 = Tape & Reel packing
 *
 * Optocouplers:
 * EL[series][variant]-[suffix]
 * e.g., EL817S1-C-TU
 * - EL817 = series
 * - S1 = variant (single channel)
 * - C = CTR bin
 * - TU = tape/packaging option
 *
 * Infrared LEDs:
 * IR[size]-[variant]
 * e.g., IR333-A, IR908-7C-F
 * - IR333 = 5mm IR LED
 * - IR908 = 3mm IR LED
 *
 * Phototransistors:
 * PT[size]-[variant]
 * e.g., PT333-3C, PT908-7C-F, PT19-21B/TR8
 * - PT333 = 5mm phototransistor
 * - PT908 = 3mm phototransistor
 * - PT19-21 = SMD phototransistor
 *
 * Light Sensors:
 * ALS-PT[series]-[specs]/[packing]
 * e.g., ALS-PT19-315C/L177/TR8
 */
public class EverlightHandler implements ManufacturerHandler {

    // Package size mapping from Everlight naming to standard sizes
    private static final Map<String, String> SIZE_CODE_MAP = Map.ofEntries(
            Map.entry("17-21", "0603"),
            Map.entry("19-21", "0805"),
            Map.entry("26-21", "1206"),
            Map.entry("12-21", "0402"),
            Map.entry("15-21", "0603"),
            Map.entry("23-21", "0805"),
            Map.entry("23-22", "0805"),
            Map.entry("24-21", "1206"),
            Map.entry("67-21", "2835"),
            Map.entry("19-337", "PLCC-6"),  // RGB LED package
            Map.entry("333", "T-1"),        // 3mm through-hole
            Map.entry("333-2", "T-1"),      // 3mm through-hole
            Map.entry("908", "T-1-3/4")     // 5mm through-hole
    );

    // Standard SMD LED pattern: 17-21SURC/S530-A3/TR8
    private static final Pattern SMD_LED_PATTERN =
            Pattern.compile("^(\\d{2}-\\d{2})([A-Z]+).*", Pattern.CASE_INSENSITIVE);

    // Through-hole LED pattern: 333-2SURC/S400-A3
    private static final Pattern THROUGH_HOLE_LED_PATTERN =
            Pattern.compile("^(333-?\\d?)([A-Z]+).*", Pattern.CASE_INSENSITIVE);

    // High-power ELSH series pattern: ELSH-Q61K1-0LPGS
    private static final Pattern ELSH_PATTERN =
            Pattern.compile("^ELSH-([A-Z][0-9]+[A-Z0-9]*)-.*", Pattern.CASE_INSENSITIVE);

    // EL-SSD series pattern
    private static final Pattern EL_SSD_PATTERN =
            Pattern.compile("^EL-SSD([A-Z0-9]+).*", Pattern.CASE_INSENSITIVE);

    // RGB LED pattern: 19-337/RGB
    private static final Pattern RGB_LED_PATTERN =
            Pattern.compile("^(\\d{2}-\\d{3})[/-]?RGB.*", Pattern.CASE_INSENSITIVE);

    // Shwo/EASP high-power series
    private static final Pattern HIGH_POWER_PATTERN =
            Pattern.compile("^(SHWO|EASP)-?([A-Z0-9]+).*", Pattern.CASE_INSENSITIVE);

    // Infrared LED pattern: IR333-A, IR908-7C-F
    private static final Pattern IR_LED_PATTERN =
            Pattern.compile("^IR(\\d{3})(-[A-Z0-9]+)?.*", Pattern.CASE_INSENSITIVE);

    // Phototransistor patterns:
    // PT333-3C, PT908-7C-F (3-digit through-hole series)
    // PT19-21B/TR8 (SMD series with dash in size code)
    private static final Pattern PHOTOTRANSISTOR_TH_PATTERN =
            Pattern.compile("^PT(\\d{3})-?([0-9A-Z]+)?.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHOTOTRANSISTOR_SMD_PATTERN =
            Pattern.compile("^PT(\\d{2}-\\d{2})([A-Z0-9/]+)?.*", Pattern.CASE_INSENSITIVE);

    // Optocoupler pattern: EL817S1-C-TU, EL357N-C, EL3063
    private static final Pattern OPTOCOUPLER_PATTERN =
            Pattern.compile("^EL(\\d{3,4})([A-Z0-9]*)?(-[A-Z0-9-]+)?.*", Pattern.CASE_INSENSITIVE);

    // Light sensor pattern: ALS-PT19-315C/L177/TR8
    private static final Pattern LIGHT_SENSOR_PATTERN =
            Pattern.compile("^ALS-PT(\\d{2}-\\d{3})([A-Z0-9/]+)?.*", Pattern.CASE_INSENSITIVE);

    // Seven-segment display pattern
    private static final Pattern DISPLAY_PATTERN =
            Pattern.compile("^ELS-([0-9]+)([A-Z0-9-]+)?.*", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Standard SMD LEDs (17-21, 19-21, 26-21 series)
        registry.addPattern(ComponentType.LED, "^\\d{2}-\\d{2}[A-Z].*");

        // Through-hole LEDs (333 series)
        registry.addPattern(ComponentType.LED, "^333-?\\d?[A-Z].*");

        // High-power SMD LEDs (ELSH series)
        registry.addPattern(ComponentType.LED, "^ELSH-.*");

        // SMD LEDs (EL-SSD series)
        registry.addPattern(ComponentType.LED, "^EL-SSD.*");

        // RGB LEDs (19-337 series and variants)
        registry.addPattern(ComponentType.LED, "^\\d{2}-\\d{3}[/-]?RGB.*");

        // High-power LEDs (Shwo/EASP series)
        registry.addPattern(ComponentType.LED, "^SHWO-?.*");
        registry.addPattern(ComponentType.LED, "^EASP-?.*");

        // Infrared LEDs (IR333, IR908)
        registry.addPattern(ComponentType.LED, "^IR\\d{3}.*");

        // Phototransistors (PT333, PT908, PT19-21)
        registry.addPattern(ComponentType.LED, "^PT\\d{2,3}.*");
        registry.addPattern(ComponentType.IC, "^PT\\d{2,3}.*");

        // Optocouplers (EL817, EL357, EL3063)
        registry.addPattern(ComponentType.IC, "^EL\\d{3,4}.*");
        registry.addPattern(ComponentType.OPTOCOUPLER_TOSHIBA, "^EL\\d{3,4}.*");  // Use existing optocoupler type

        // Light Sensors (ALS-PT19-315)
        registry.addPattern(ComponentType.IC, "^ALS-PT.*");

        // Seven-segment displays
        registry.addPattern(ComponentType.LED, "^ELS-\\d+.*");
        registry.addPattern(ComponentType.IC, "^ELS-\\d+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.LED,
                ComponentType.IC,
                ComponentType.OPTOCOUPLER_TOSHIBA  // Reuse existing optocoupler type
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // LED types
        if (type == ComponentType.LED) {
            // Standard SMD LEDs (17-21, 19-21, 26-21, etc.)
            if (upperMpn.matches("^\\d{2}-\\d{2}[A-Z].*")) {
                return true;
            }
            // Through-hole LEDs (333 series)
            if (upperMpn.matches("^333-?\\d?[A-Z].*")) {
                return true;
            }
            // High-power ELSH series
            if (upperMpn.matches("^ELSH-.*")) {
                return true;
            }
            // EL-SSD series
            if (upperMpn.matches("^EL-SSD.*")) {
                return true;
            }
            // RGB LEDs
            if (upperMpn.matches("^\\d{2}-\\d{3}[/-]?RGB.*")) {
                return true;
            }
            // High-power Shwo/EASP series
            if (upperMpn.matches("^SHWO-?.*") || upperMpn.matches("^EASP-?.*")) {
                return true;
            }
            // Infrared LEDs
            if (upperMpn.matches("^IR\\d{3}.*")) {
                return true;
            }
            // Phototransistors (also categorized as LED for some purposes)
            if (upperMpn.matches("^PT\\d{2,3}.*")) {
                return true;
            }
            // Seven-segment displays
            if (upperMpn.matches("^ELS-\\d+.*")) {
                return true;
            }
        }

        // IC types (optocouplers, light sensors, phototransistors)
        if (type == ComponentType.IC) {
            // Optocouplers (EL817, EL357, EL3063)
            if (upperMpn.matches("^EL\\d{3,4}.*")) {
                return true;
            }
            // Light sensors
            if (upperMpn.matches("^ALS-PT.*")) {
                return true;
            }
            // Phototransistors
            if (upperMpn.matches("^PT\\d{2,3}.*")) {
                return true;
            }
            // Seven-segment displays with controller
            if (upperMpn.matches("^ELS-\\d+.*")) {
                return true;
            }
        }

        // Optocouplers
        if (type == ComponentType.OPTOCOUPLER_TOSHIBA) {
            if (upperMpn.matches("^EL\\d{3,4}.*")) {
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

        // Standard SMD LEDs (17-21, 19-21, 26-21)
        Matcher smdMatcher = SMD_LED_PATTERN.matcher(upperMpn);
        if (smdMatcher.matches()) {
            String sizeCode = smdMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(sizeCode, sizeCode);
        }

        // RGB LEDs (19-337)
        Matcher rgbMatcher = RGB_LED_PATTERN.matcher(upperMpn);
        if (rgbMatcher.matches()) {
            String sizeCode = rgbMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(sizeCode, sizeCode);
        }

        // Through-hole LEDs (333 series)
        Matcher thMatcher = THROUGH_HOLE_LED_PATTERN.matcher(upperMpn);
        if (thMatcher.matches()) {
            String sizeCode = thMatcher.group(1).replace("-", "");
            if (sizeCode.startsWith("333")) {
                return "T-1"; // 3mm
            }
        }

        // Infrared LEDs
        Matcher irMatcher = IR_LED_PATTERN.matcher(upperMpn);
        if (irMatcher.matches()) {
            String size = irMatcher.group(1);
            if (size.equals("333")) {
                return "T-1"; // 3mm
            } else if (size.equals("908")) {
                return "T-1-3/4"; // 5mm (actually closer to this standard size)
            }
        }

        // Phototransistors - Through-hole (PT333, PT908)
        Matcher ptThMatcher = PHOTOTRANSISTOR_TH_PATTERN.matcher(upperMpn);
        if (ptThMatcher.matches()) {
            String size = ptThMatcher.group(1);
            if (size.equals("333")) {
                return "T-1"; // 3mm
            } else if (size.equals("908")) {
                return "T-1-3/4"; // 5mm
            }
        }

        // Phototransistors - SMD (PT19-21)
        Matcher ptSmdMatcher = PHOTOTRANSISTOR_SMD_PATTERN.matcher(upperMpn);
        if (ptSmdMatcher.matches()) {
            String sizeCode = ptSmdMatcher.group(1);
            return SIZE_CODE_MAP.getOrDefault(sizeCode, "0805");
        }

        // Optocouplers - extract package from suffix
        Matcher optMatcher = OPTOCOUPLER_PATTERN.matcher(upperMpn);
        if (optMatcher.matches()) {
            String variant = optMatcher.group(2);
            if (variant != null) {
                if (variant.contains("S1") || variant.contains("S2")) {
                    return "SMD";
                }
            }
            return "DIP-4";  // Default for most optocouplers
        }

        // High-power ELSH series
        if (upperMpn.startsWith("ELSH-")) {
            // Extract package from first part: ELSH-Q61K1 -> Q61
            Matcher elshMatcher = ELSH_PATTERN.matcher(upperMpn);
            if (elshMatcher.matches()) {
                String code = elshMatcher.group(1);
                // Q61 indicates a specific package type
                if (code.startsWith("Q")) {
                    return "QFN";
                }
            }
            return "SMD";
        }

        // Light sensors
        if (upperMpn.startsWith("ALS-PT")) {
            Matcher alsMatcher = LIGHT_SENSOR_PATTERN.matcher(upperMpn);
            if (alsMatcher.matches()) {
                String sizeCode = alsMatcher.group(1);
                // ALS-PT19-315 -> 19-21 style SMD package
                return "SMD";
            }
        }

        // High-power Shwo/EASP series
        if (upperMpn.startsWith("SHWO") || upperMpn.startsWith("EASP")) {
            return "SMD-HIGHPOWER";
        }

        // EL-SSD series
        if (upperMpn.startsWith("EL-SSD")) {
            return "SMD";
        }

        // Seven-segment displays
        if (upperMpn.startsWith("ELS-")) {
            return "Display";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Standard SMD LEDs - return size code as series (e.g., 17-21)
        Matcher smdMatcher = SMD_LED_PATTERN.matcher(upperMpn);
        if (smdMatcher.matches()) {
            return smdMatcher.group(1);
        }

        // RGB LEDs - return size code as series (e.g., 19-337)
        Matcher rgbMatcher = RGB_LED_PATTERN.matcher(upperMpn);
        if (rgbMatcher.matches()) {
            return rgbMatcher.group(1);
        }

        // Through-hole LEDs - return 333 series
        Matcher thMatcher = THROUGH_HOLE_LED_PATTERN.matcher(upperMpn);
        if (thMatcher.matches()) {
            return thMatcher.group(1);
        }

        // High-power ELSH series
        if (upperMpn.startsWith("ELSH-")) {
            return "ELSH";
        }

        // EL-SSD series
        if (upperMpn.startsWith("EL-SSD")) {
            return "EL-SSD";
        }

        // High-power Shwo series
        if (upperMpn.startsWith("SHWO")) {
            return "SHWO";
        }

        // High-power EASP series
        if (upperMpn.startsWith("EASP")) {
            return "EASP";
        }

        // Infrared LEDs - return IR series
        Matcher irMatcher = IR_LED_PATTERN.matcher(upperMpn);
        if (irMatcher.matches()) {
            return "IR" + irMatcher.group(1);
        }

        // Phototransistors - Through-hole (PT333, PT908)
        Matcher ptThMatcher = PHOTOTRANSISTOR_TH_PATTERN.matcher(upperMpn);
        if (ptThMatcher.matches()) {
            return "PT" + ptThMatcher.group(1);
        }

        // Phototransistors - SMD (PT19-21)
        Matcher ptSmdMatcher = PHOTOTRANSISTOR_SMD_PATTERN.matcher(upperMpn);
        if (ptSmdMatcher.matches()) {
            return "PT" + ptSmdMatcher.group(1).replace("-", "");
        }

        // Optocouplers - return EL series number
        Matcher optMatcher = OPTOCOUPLER_PATTERN.matcher(upperMpn);
        if (optMatcher.matches()) {
            return "EL" + optMatcher.group(1);
        }

        // Light sensors - return ALS-PT series
        Matcher alsMatcher = LIGHT_SENSOR_PATTERN.matcher(upperMpn);
        if (alsMatcher.matches()) {
            return "ALS-PT" + alsMatcher.group(1);
        }

        // Seven-segment displays
        Matcher displayMatcher = DISPLAY_PATTERN.matcher(upperMpn);
        if (displayMatcher.matches()) {
            return "ELS-" + displayMatcher.group(1);
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

        // For LEDs, check if same color (for same-series parts)
        String color1 = extractColor(mpn1);
        String color2 = extractColor(mpn2);

        // If both have identifiable colors, they must match
        if (!color1.isEmpty() && !color2.isEmpty()) {
            return color1.equals(color2);
        }

        // Same series is sufficient for other components (optocouplers, sensors)
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Extracts the color code from an Everlight LED MPN.
     *
     * Common color codes:
     * - SURC = Super Red Clear lens
     * - SUGC = Super Green Clear lens
     * - SUBC = Super Blue Clear lens
     * - SUYC = Super Yellow Clear lens
     * - SUOC = Super Orange Clear lens
     * - SUWC = Super White Clear lens
     * - RGB = RGB multicolor
     *
     * @param mpn the manufacturer part number
     * @return the color description or empty string
     */
    public String extractColor(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Check for RGB first
        if (upperMpn.contains("RGB")) {
            return "RGB";
        }

        // Common Everlight color codes
        if (upperMpn.contains("SURC") || upperMpn.contains("SRC") ||
                upperMpn.contains("URC") || upperMpn.contains("-R")) {
            return "Red";
        }
        if (upperMpn.contains("SUGC") || upperMpn.contains("SGC") ||
                upperMpn.contains("UGC") || upperMpn.contains("-G")) {
            return "Green";
        }
        if (upperMpn.contains("SUBC") || upperMpn.contains("SBC") ||
                upperMpn.contains("UBC") || upperMpn.contains("-B")) {
            return "Blue";
        }
        if (upperMpn.contains("SUYC") || upperMpn.contains("SYC") ||
                upperMpn.contains("UYC") || upperMpn.contains("-Y")) {
            return "Yellow";
        }
        if (upperMpn.contains("SUOC") || upperMpn.contains("SOC") ||
                upperMpn.contains("UOC") || upperMpn.contains("-O")) {
            return "Orange";
        }
        if (upperMpn.contains("SUWC") || upperMpn.contains("SWC") ||
                upperMpn.contains("UWC") || upperMpn.contains("-W")) {
            return "White";
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

        String upperMpn = mpn.toUpperCase();

        // Through-hole types
        if (upperMpn.matches("^333-?\\d?[A-Z].*") ||
                upperMpn.matches("^IR333.*") ||
                upperMpn.matches("^IR908.*") ||
                upperMpn.matches("^PT333.*") ||
                upperMpn.matches("^PT908.*")) {
            return "Through-hole";
        }

        // SMD types
        if (upperMpn.matches("^\\d{2}-\\d{2,3}.*") ||
                upperMpn.startsWith("ELSH-") ||
                upperMpn.startsWith("EL-SSD") ||
                upperMpn.startsWith("SHWO") ||
                upperMpn.startsWith("EASP") ||
                upperMpn.startsWith("ALS-PT") ||
                upperMpn.matches("^PT19-21.*")) {
            return "SMD";
        }

        // DIP for optocouplers
        if (upperMpn.matches("^EL\\d{3,4}.*")) {
            // Check for SMD variants
            if (upperMpn.contains("S1") || upperMpn.contains("S2") || upperMpn.contains("-G")) {
                return "SMD";
            }
            return "DIP";
        }

        return "";
    }

    /**
     * Determines the product category of an Everlight component.
     *
     * @param mpn the manufacturer part number
     * @return product category description
     */
    public String getProductCategory(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        if (upperMpn.matches("^\\d{2}-\\d{3}.*RGB.*")) {
            return "RGB LED";
        }
        if (upperMpn.matches("^\\d{2}-\\d{2}[A-Z].*")) {
            return "SMD LED";
        }
        if (upperMpn.matches("^333-?\\d?[A-Z].*")) {
            return "Through-hole LED";
        }
        if (upperMpn.startsWith("ELSH-")) {
            return "High-power SMD LED";
        }
        if (upperMpn.startsWith("EL-SSD")) {
            return "SMD LED";
        }
        if (upperMpn.startsWith("SHWO") || upperMpn.startsWith("EASP")) {
            return "High-power LED";
        }
        if (upperMpn.matches("^IR\\d{3}.*")) {
            return "Infrared LED";
        }
        if (upperMpn.matches("^PT\\d{2,3}.*")) {
            return "Phototransistor";
        }
        if (upperMpn.matches("^EL\\d{3,4}.*")) {
            return "Optocoupler";
        }
        if (upperMpn.startsWith("ALS-PT")) {
            return "Light Sensor";
        }
        if (upperMpn.startsWith("ELS-")) {
            return "Seven-segment Display";
        }

        return "";
    }
}
