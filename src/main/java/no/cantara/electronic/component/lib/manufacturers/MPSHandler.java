package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler for Monolithic Power Systems (MPS) components.
 * <p>
 * MPS specializes in high-performance power management solutions including:
 * - MP1xxx: Step-down converters (MP1584, MP1593)
 * - MP2xxx: Step-down/LDO converters (MP2307, MP2359, MP2161)
 * - MP3xxx: LED drivers (MP3302, MP3394)
 * - MP4xxx: High-current step-down converters (MP4560, MP4569)
 * - MP5xxx: Step-up/SEPIC converters (MP5010, MP5032)
 * - MP6xxx: Motor drivers (MP6500, MP6513, MP6543)
 * - MP8xxx: Multi-channel PMIC (MP8756, MP8859)
 * - MP9xxx: High-voltage converters
 * - MPQ series: Automotive grade (MPQ4560, MPQ8875)
 * - MPM series: Power modules (MPM3610, MPM3833)
 * <p>
 * Pattern examples: MP1584EN-LF-Z, MP2307DN-LF-Z, MPQ4560GQ-AEC1-LF-Z
 */
public class MPSHandler implements ManufacturerHandler {

    // Package code mappings for MPS components
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Common MPS package suffixes
        PACKAGE_CODES.put("EN", "SOIC-8E");
        PACKAGE_CODES.put("DN", "SO-8");
        PACKAGE_CODES.put("GQ", "QFN");
        PACKAGE_CODES.put("GL", "QFN");
        PACKAGE_CODES.put("GR", "QFN");
        PACKAGE_CODES.put("GS", "QFN-EP");
        PACKAGE_CODES.put("GT", "QFN");
        PACKAGE_CODES.put("GU", "QFN");
        PACKAGE_CODES.put("GV", "QFN");
        PACKAGE_CODES.put("GN", "QFN");
        PACKAGE_CODES.put("GF", "WLCSP");
        PACKAGE_CODES.put("GW", "WLCSP");
        PACKAGE_CODES.put("DF", "TSOT-23");
        PACKAGE_CODES.put("DG", "TSOT-23-5");
        PACKAGE_CODES.put("DH", "SOT-23-8");
        PACKAGE_CODES.put("DJ", "DFN");
        PACKAGE_CODES.put("DK", "DFN-10");
        PACKAGE_CODES.put("DL", "DFN-12");
        PACKAGE_CODES.put("EC", "SOIC-8");
        PACKAGE_CODES.put("EF", "SOIC-8");
        PACKAGE_CODES.put("EG", "TSSOP-8");
        PACKAGE_CODES.put("EH", "TSSOP-16");
        PACKAGE_CODES.put("EJ", "SOP-8");
        PACKAGE_CODES.put("EK", "MSOP-8");
        PACKAGE_CODES.put("EL", "MSOP-10");
        PACKAGE_CODES.put("MN", "QFN-Module");
        PACKAGE_CODES.put("MF", "Module-BGA");
        PACKAGE_CODES.put("HF", "TO-263");
        PACKAGE_CODES.put("HN", "TO-220");
    }

    // Series descriptions for extractSeries documentation
    private static final Map<String, String> SERIES_INFO = new HashMap<>();
    static {
        SERIES_INFO.put("MP1", "Step-Down Converters");
        SERIES_INFO.put("MP2", "Step-Down/LDO Converters");
        SERIES_INFO.put("MP3", "LED Drivers");
        SERIES_INFO.put("MP4", "High-Current Step-Down");
        SERIES_INFO.put("MP5", "Step-Up/SEPIC Converters");
        SERIES_INFO.put("MP6", "Motor Drivers");
        SERIES_INFO.put("MP7", "Power Switches");
        SERIES_INFO.put("MP8", "Multi-Channel PMIC");
        SERIES_INFO.put("MP9", "High-Voltage Converters");
        SERIES_INFO.put("MPQ", "Automotive Grade");
        SERIES_INFO.put("MPM", "Power Modules");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.LED_DRIVER,
                ComponentType.MOTOR_DRIVER
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MP1xxx - Step-down converters (DC-DC)
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MP1[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP1[0-9]{3}[A-Z0-9-]*$");

        // MP2xxx - Step-down/LDO converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MP2[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP2[0-9]{3}[A-Z0-9-]*$");

        // MP3xxx - LED drivers
        registry.addPattern(ComponentType.LED_DRIVER, "^MP3[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP3[0-9]{3}[A-Z0-9-]*$");

        // MP4xxx - High-current step-down converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MP4[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP4[0-9]{3}[A-Z0-9-]*$");

        // MP5xxx - Step-up/SEPIC converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MP5[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP5[0-9]{3}[A-Z0-9-]*$");

        // MP6xxx - Motor drivers
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^MP6[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP6[0-9]{3}[A-Z0-9-]*$");

        // MP7xxx - Power switches/load switches
        registry.addPattern(ComponentType.IC, "^MP7[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MP7[0-9]{3}[A-Z0-9-]*$");

        // MP8xxx - Multi-channel PMIC
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MP8[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP8[0-9]{3}[A-Z0-9-]*$");

        // MP9xxx - High-voltage converters
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MP9[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MP9[0-9]{3}[A-Z0-9-]*$");

        // MPQ series - Automotive grade versions
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MPQ[0-9]{4}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MPQ[0-9]{4}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^MPQ6[0-9]{3}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.LED_DRIVER, "^MPQ3[0-9]{3}[A-Z0-9-]*$");

        // MPM series - Power modules
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MPM[0-9]{4}[A-Z0-9-]*$");
        registry.addPattern(ComponentType.IC, "^MPM[0-9]{4}[A-Z0-9-]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only handle MPS MPNs (start with MP)
        if (!upperMpn.startsWith("MP")) {
            return false;
        }

        // Direct pattern checks for efficiency
        switch (type) {
            case VOLTAGE_REGULATOR:
                return isVoltageRegulator(upperMpn);
            case LED_DRIVER:
                return isLEDDriver(upperMpn);
            case MOTOR_DRIVER:
                return isMotorDriver(upperMpn);
            case IC:
                return isMPSPart(upperMpn);
            default:
                break;
        }

        // Fall back to pattern registry for other types
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    private boolean isMPSPart(String mpn) {
        // Match MP[1-9]xxx, MPQxxxx, MPMxxxx
        return mpn.matches("^MP[1-9][0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^MPQ[0-9]{4}[A-Z0-9-]*$") ||
               mpn.matches("^MPM[0-9]{4}[A-Z0-9-]*$");
    }

    private boolean isVoltageRegulator(String mpn) {
        // MP1xxx, MP2xxx, MP4xxx, MP5xxx, MP7xxx, MP8xxx, MP9xxx are voltage regulators
        // MPQ series (except MPQ6xxx motor drivers, MPQ3xxx LED drivers)
        // MPM power modules
        if (mpn.matches("^MP[124578][0-9]{3}[A-Z0-9-]*$") ||
            mpn.matches("^MP9[0-9]{3}[A-Z0-9-]*$") ||
            mpn.matches("^MPM[0-9]{4}[A-Z0-9-]*$")) {
            return true;
        }
        // MPQ automotive but not motor drivers or LED drivers
        if (mpn.matches("^MPQ[0-9]{4}[A-Z0-9-]*$")) {
            return !mpn.matches("^MPQ6[0-9]{3}.*") && !mpn.matches("^MPQ3[0-9]{3}.*");
        }
        return false;
    }

    private boolean isLEDDriver(String mpn) {
        // MP3xxx and MPQ3xxx are LED drivers
        return mpn.matches("^MP3[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^MPQ3[0-9]{3}[A-Z0-9-]*$");
    }

    private boolean isMotorDriver(String mpn) {
        // MP6xxx and MPQ6xxx are motor drivers
        return mpn.matches("^MP6[0-9]{3}[A-Z0-9-]*$") ||
               mpn.matches("^MPQ6[0-9]{3}[A-Z0-9-]*$");
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove common suffixes: -LF-Z, -Z, -LF, -AEC1, etc.
        String baseMpn = upperMpn.replaceAll("-(LF|Z|AEC1|TR|RT)+(-.*)?$", "");

        // Extract package code - typically 2 letters after the part number
        // Pattern: MP[1-9Q]xxx[2-letter-package]...
        Pattern packagePattern = Pattern.compile("^MP[A-Z]?[0-9]{3,4}([A-Z]{2}).*$");
        java.util.regex.Matcher matcher = packagePattern.matcher(baseMpn);

        if (matcher.matches()) {
            String code = matcher.group(1);
            return PACKAGE_CODES.getOrDefault(code, code);
        }

        // For MPM modules, check for module-specific packages
        if (upperMpn.startsWith("MPM")) {
            Pattern modulePattern = Pattern.compile("^MPM[0-9]{4}([A-Z]{2}).*$");
            matcher = modulePattern.matcher(baseMpn);
            if (matcher.matches()) {
                String code = matcher.group(1);
                return PACKAGE_CODES.getOrDefault(code, code);
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // MPM series: MPMxxxx
        if (upperMpn.startsWith("MPM") && upperMpn.length() >= 7) {
            return "MPM";
        }

        // MPQ series: MPQxxxx
        if (upperMpn.startsWith("MPQ") && upperMpn.length() >= 7) {
            return "MPQ";
        }

        // Standard MP[1-9]xxx series
        if (upperMpn.matches("^MP[1-9][0-9]{3}.*")) {
            return upperMpn.substring(0, 3);  // Returns MP1, MP2, etc.
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Check for automotive equivalents first (MPQ vs MP)
        // This needs to happen before series check because MPQ has different series code
        if (areEquivalentParts(mpn1, mpn2)) {
            return true;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be in the same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Extract the base part number (MPxxxx portion)
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        // Same base part with different packages/options are replacements
        if (base1.equals(base2)) {
            return true;
        }

        return false;
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Match MP[Q|M]?[0-9]{3,4} - the core part number
        Pattern basePattern = Pattern.compile("^(MP[QM]?[0-9]{3,4}).*$");
        java.util.regex.Matcher matcher = basePattern.matcher(upperMpn);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

    private boolean areEquivalentParts(String mpn1, String mpn2) {
        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Check if one is automotive (MPQ) version of the other (MP)
        // e.g., MP4560 <-> MPQ4560
        if (upper1.startsWith("MPQ") && upper2.startsWith("MP") && !upper2.startsWith("MPQ") && !upper2.startsWith("MPM")) {
            String num1 = upper1.substring(3, 7);  // Get 4-digit number from MPQxxxx
            if (upper2.length() >= 6) {
                String num2 = upper2.substring(2, 6);  // Get 4-digit number from MPxxxx
                return num1.equals(num2);
            }
        }

        if (upper2.startsWith("MPQ") && upper1.startsWith("MP") && !upper1.startsWith("MPQ") && !upper1.startsWith("MPM")) {
            String num2 = upper2.substring(3, 7);
            if (upper1.length() >= 6) {
                String num1 = upper1.substring(2, 6);
                return num1.equals(num2);
            }
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get a description for the given series.
     *
     * @param series the series code (e.g., "MP1", "MPQ")
     * @return description of the series or empty string if unknown
     */
    public String getSeriesDescription(String series) {
        return SERIES_INFO.getOrDefault(series, "");
    }

    /**
     * Determine if an MPN is an automotive-grade part.
     *
     * @param mpn the manufacturer part number
     * @return true if this is an automotive (AEC-Q100) qualified part
     */
    public boolean isAutomotiveGrade(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.startsWith("MPQ") || upper.contains("-AEC");
    }
}
