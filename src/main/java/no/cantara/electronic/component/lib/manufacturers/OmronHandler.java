package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler for Omron Electronic Components.
 *
 * Omron product families:
 * - Relays: G2R, G3M, G5V, G6K series (signal and power relays, SSR)
 * - Switches: B3F (tact), D2F (micro), SS (slide)
 * - Sensors: EE-S (optical), D6F (flow), E2E (proximity)
 *
 * MPN Structure Examples:
 * - G5V-1-DC5: G=Relay, 5V=Series, 1=Contact config, DC5=Coil voltage
 * - G3MC-201P-DC5: G3=SSR, MC=Series, 201P=Model, DC5=Control voltage
 * - B3F-1000: B3F=Tact switch series, 1000=Force/travel variant
 * - D2FC-F-7N: D2F=Microswitch series, C=Compact, F=Force, 7N=Model
 * - EE-SX1041: EE=Optical sensor, S=Slot type, X=Variant, 1041=Model
 * - D6F-01N1-110: D6F=Flow sensor, 01N1=Flow range, 110=Interface
 * - E2E-X1R5E1: E2E=Proximity sensor, X=Unshielded, 1R5=1.5mm range, E1=Connector
 *
 * Contact configurations (relays):
 * - 1: SPDT (Single Pole Double Throw)
 * - 2: DPDT (Double Pole Double Throw)
 *
 * Package codes:
 * - Relays: PCB mount, socket mount, plug-in
 * - Switches: Through-hole, SMD variants
 */
public class OmronHandler implements ManufacturerHandler {

    // Relay series patterns
    private static final Pattern RELAY_G2R_PATTERN = Pattern.compile("^G2R[L]?-[0-9A]+.*");
    private static final Pattern RELAY_G3M_PATTERN = Pattern.compile("^G3M[A-Z]*-[0-9]+.*");
    private static final Pattern RELAY_G5V_PATTERN = Pattern.compile("^G5V-[0-9]+.*");
    private static final Pattern RELAY_G6K_PATTERN = Pattern.compile("^G6K[U]?-[0-9]+.*");
    private static final Pattern RELAY_G5RL_PATTERN = Pattern.compile("^G5RL-[0-9A-Z]+.*");
    private static final Pattern RELAY_GENERIC_PATTERN = Pattern.compile("^G[0-9][A-Z0-9]*-.*");

    // SSR patterns
    private static final Pattern SSR_G3MC_PATTERN = Pattern.compile("^G3MC-[0-9]+.*");
    private static final Pattern SSR_G3NA_PATTERN = Pattern.compile("^G3NA-[0-9]+.*");
    private static final Pattern SSR_G3NE_PATTERN = Pattern.compile("^G3NE-[0-9]+.*");
    private static final Pattern SSR_GENERIC_PATTERN = Pattern.compile("^G3[A-Z][A-Z]?-.*");

    // Switch patterns
    private static final Pattern SWITCH_B3F_PATTERN = Pattern.compile("^B3F-[0-9]+.*");
    private static final Pattern SWITCH_B3FS_PATTERN = Pattern.compile("^B3FS-[0-9]+.*");
    private static final Pattern SWITCH_B3U_PATTERN = Pattern.compile("^B3U-[0-9]+.*");
    private static final Pattern SWITCH_D2F_PATTERN = Pattern.compile("^D2F[C]?-[A-Z0-9]+.*");
    private static final Pattern SWITCH_SS_PATTERN = Pattern.compile("^SS-[0-9]+.*");
    private static final Pattern SWITCH_GENERIC_PATTERN = Pattern.compile("^(B3[A-Z]|D2F|SS)-.*");

    // Sensor patterns
    private static final Pattern SENSOR_EES_PATTERN = Pattern.compile("^EE-S[A-Z]?[0-9]+.*");
    private static final Pattern SENSOR_EEX_PATTERN = Pattern.compile("^EE-[A-Z][A-Z]?[0-9]+.*");
    private static final Pattern SENSOR_D6F_PATTERN = Pattern.compile("^D6F-[0-9A-Z]+.*");
    private static final Pattern SENSOR_E2E_PATTERN = Pattern.compile("^E2E[A-Z]?-[A-Z0-9]+.*");
    private static final Pattern SENSOR_E2K_PATTERN = Pattern.compile("^E2K-[A-Z0-9]+.*");

    // Series to family mapping
    private static final Map<String, String> RELAY_FAMILIES = Map.ofEntries(
            Map.entry("G2R", "General Purpose Relay"),
            Map.entry("G2RL", "Slim Power Relay"),
            Map.entry("G3M", "Solid State Relay"),
            Map.entry("G3MC", "Solid State Relay"),
            Map.entry("G3NA", "Panel Mount SSR"),
            Map.entry("G3NE", "Heat Sink SSR"),
            Map.entry("G5V", "Signal Relay"),
            Map.entry("G5RL", "Slim Power Relay"),
            Map.entry("G6K", "Ultra Miniature Signal Relay"),
            Map.entry("G6KU", "Ultra Small Signal Relay")
    );

    private static final Map<String, String> SWITCH_FAMILIES = Map.of(
            "B3F", "Tactile Switch",
            "B3FS", "Tactile Switch SMD",
            "B3U", "Ultra Compact Tactile Switch",
            "D2F", "Microswitch",
            "D2FC", "Microswitch Compact",
            "SS", "Slide Switch"
    );

    // Sensor families ordered from most specific to least specific for matching
    private static final String[][] SENSOR_FAMILIES_ORDERED = {
            {"EE-SX", "Optical Sensor SMD"},
            {"EE-SY", "Optical Sensor Reflective"},
            {"EE-S", "Optical Sensor Slot"},
            {"D6F", "MEMS Flow Sensor"},
            {"E2E", "Proximity Sensor Inductive"},
            {"E2K", "Proximity Sensor Capacitive"}
    };

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Relay patterns - both base and Omron-specific types
        registry.addPattern(ComponentType.RELAY, "^G[0-9][A-Z0-9]*-.*");
        registry.addPattern(ComponentType.RELAY_OMRON, "^G[0-9][A-Z0-9]*-.*");
        registry.addPattern(ComponentType.RELAY_SIGNAL, "^G(5V|6K)[U]?-.*");
        registry.addPattern(ComponentType.RELAY_POWER, "^G(2R|2RL|5RL)-.*");

        // SSR patterns
        registry.addPattern(ComponentType.RELAY, "^G3[A-Z][A-Z]?-.*");
        registry.addPattern(ComponentType.RELAY_OMRON, "^G3[A-Z][A-Z]?-.*");
        registry.addPattern(ComponentType.RELAY_SSR, "^G3[A-Z][A-Z]?-.*");

        // Switch patterns - both base and Omron-specific types
        registry.addPattern(ComponentType.SWITCH, "^B3[A-Z]-.*");
        registry.addPattern(ComponentType.SWITCH_OMRON, "^B3[A-Z]-.*");
        registry.addPattern(ComponentType.SWITCH_TACT, "^B3[FUS]-.*");

        registry.addPattern(ComponentType.SWITCH, "^D2F[C]?-.*");
        registry.addPattern(ComponentType.SWITCH_OMRON, "^D2F[C]?-.*");
        registry.addPattern(ComponentType.SWITCH_MICRO, "^D2F[C]?-.*");

        registry.addPattern(ComponentType.SWITCH, "^SS-.*");
        registry.addPattern(ComponentType.SWITCH_OMRON, "^SS-.*");
        registry.addPattern(ComponentType.SWITCH_SLIDE, "^SS-.*");

        // Sensor patterns
        registry.addPattern(ComponentType.SENSOR, "^EE-[A-Z].*");
        registry.addPattern(ComponentType.SENSOR_OMRON, "^EE-[A-Z].*");
        registry.addPattern(ComponentType.SENSOR_OPTICAL, "^EE-[A-Z].*");
        registry.addPattern(ComponentType.SENSOR_OPTICAL_OMRON, "^EE-[A-Z].*");

        registry.addPattern(ComponentType.SENSOR, "^D6F-.*");
        registry.addPattern(ComponentType.SENSOR_OMRON, "^D6F-.*");
        registry.addPattern(ComponentType.SENSOR_FLOW, "^D6F-.*");
        registry.addPattern(ComponentType.SENSOR_FLOW_OMRON, "^D6F-.*");

        registry.addPattern(ComponentType.SENSOR, "^E2[EK]-.*");
        registry.addPattern(ComponentType.SENSOR_OMRON, "^E2[EK]-.*");
        registry.addPattern(ComponentType.SENSOR_PROXIMITY, "^E2[EK]-.*");
        registry.addPattern(ComponentType.SENSOR_PROXIMITY_OMRON, "^E2[EK]-.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                // Relay types
                ComponentType.RELAY,
                ComponentType.RELAY_OMRON,
                ComponentType.RELAY_SIGNAL,
                ComponentType.RELAY_POWER,
                ComponentType.RELAY_SSR,
                // Switch types
                ComponentType.SWITCH,
                ComponentType.SWITCH_OMRON,
                ComponentType.SWITCH_TACT,
                ComponentType.SWITCH_MICRO,
                ComponentType.SWITCH_SLIDE,
                // Sensor types
                ComponentType.SENSOR,
                ComponentType.SENSOR_OMRON,
                ComponentType.SENSOR_OPTICAL,
                ComponentType.SENSOR_OPTICAL_OMRON,
                ComponentType.SENSOR_FLOW,
                ComponentType.SENSOR_FLOW_OMRON,
                ComponentType.SENSOR_PROXIMITY,
                ComponentType.SENSOR_PROXIMITY_OMRON
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Relay matching
        if (type == ComponentType.RELAY || type == ComponentType.RELAY_OMRON) {
            return isRelay(upperMpn);
        }
        if (type == ComponentType.RELAY_SIGNAL) {
            return RELAY_G5V_PATTERN.matcher(upperMpn).matches() ||
                   RELAY_G6K_PATTERN.matcher(upperMpn).matches();
        }
        if (type == ComponentType.RELAY_POWER) {
            return RELAY_G2R_PATTERN.matcher(upperMpn).matches() ||
                   RELAY_G5RL_PATTERN.matcher(upperMpn).matches();
        }
        if (type == ComponentType.RELAY_SSR) {
            return isSSR(upperMpn);
        }

        // Switch matching
        if (type == ComponentType.SWITCH || type == ComponentType.SWITCH_OMRON) {
            return isSwitch(upperMpn);
        }
        if (type == ComponentType.SWITCH_TACT) {
            return SWITCH_B3F_PATTERN.matcher(upperMpn).matches() ||
                   SWITCH_B3FS_PATTERN.matcher(upperMpn).matches() ||
                   SWITCH_B3U_PATTERN.matcher(upperMpn).matches();
        }
        if (type == ComponentType.SWITCH_MICRO) {
            return SWITCH_D2F_PATTERN.matcher(upperMpn).matches();
        }
        if (type == ComponentType.SWITCH_SLIDE) {
            return SWITCH_SS_PATTERN.matcher(upperMpn).matches();
        }

        // Sensor matching
        if (type == ComponentType.SENSOR || type == ComponentType.SENSOR_OMRON) {
            return isSensor(upperMpn);
        }
        if (type == ComponentType.SENSOR_OPTICAL || type == ComponentType.SENSOR_OPTICAL_OMRON) {
            return SENSOR_EES_PATTERN.matcher(upperMpn).matches() ||
                   SENSOR_EEX_PATTERN.matcher(upperMpn).matches();
        }
        if (type == ComponentType.SENSOR_FLOW || type == ComponentType.SENSOR_FLOW_OMRON) {
            return SENSOR_D6F_PATTERN.matcher(upperMpn).matches();
        }
        if (type == ComponentType.SENSOR_PROXIMITY || type == ComponentType.SENSOR_PROXIMITY_OMRON) {
            return SENSOR_E2E_PATTERN.matcher(upperMpn).matches() ||
                   SENSOR_E2K_PATTERN.matcher(upperMpn).matches();
        }

        // Fallback to registry
        return registry.matches(upperMpn, type);
    }

    private boolean isRelay(String mpn) {
        return RELAY_GENERIC_PATTERN.matcher(mpn).matches() ||
               SSR_GENERIC_PATTERN.matcher(mpn).matches();
    }

    private boolean isSSR(String mpn) {
        return SSR_G3MC_PATTERN.matcher(mpn).matches() ||
               SSR_G3NA_PATTERN.matcher(mpn).matches() ||
               SSR_G3NE_PATTERN.matcher(mpn).matches() ||
               SSR_GENERIC_PATTERN.matcher(mpn).matches();
    }

    private boolean isSwitch(String mpn) {
        return SWITCH_B3F_PATTERN.matcher(mpn).matches() ||
               SWITCH_B3FS_PATTERN.matcher(mpn).matches() ||
               SWITCH_B3U_PATTERN.matcher(mpn).matches() ||
               SWITCH_D2F_PATTERN.matcher(mpn).matches() ||
               SWITCH_SS_PATTERN.matcher(mpn).matches();
    }

    private boolean isSensor(String mpn) {
        return SENSOR_EES_PATTERN.matcher(mpn).matches() ||
               SENSOR_EEX_PATTERN.matcher(mpn).matches() ||
               SENSOR_D6F_PATTERN.matcher(mpn).matches() ||
               SENSOR_E2E_PATTERN.matcher(mpn).matches() ||
               SENSOR_E2K_PATTERN.matcher(mpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // For relays, extract contact configuration
        if (isRelay(upperMpn)) {
            return extractRelayContactConfig(upperMpn);
        }

        // For switches, extract variant/mount info
        if (isSwitch(upperMpn)) {
            return extractSwitchPackage(upperMpn);
        }

        // For sensors, extract interface/output type
        if (isSensor(upperMpn)) {
            return extractSensorOutput(upperMpn);
        }

        return "";
    }

    private String extractRelayContactConfig(String mpn) {
        // Extract contact configuration from relay MPN
        // G5V-1-DC5 -> "1" (SPDT)
        // G2R-2-DC12 -> "2" (DPDT)
        String[] parts = mpn.split("-");
        if (parts.length >= 2) {
            String config = parts[1];
            // Check for standard configurations
            if (config.equals("1") || config.equals("1A")) return "SPDT";
            if (config.equals("2") || config.equals("2A")) return "DPDT";
            if (config.matches("[0-9]+[A-Z]*")) {
                int poles = Integer.parseInt(config.replaceAll("[^0-9]", ""));
                if (poles == 1) return "SPDT";
                if (poles == 2) return "DPDT";
                return poles + "P";
            }
        }
        return "";
    }

    private String extractSwitchPackage(String mpn) {
        // B3F-1000 -> THT
        // B3FS-1002P -> SMD
        // B3U-1000P -> SMD Ultra-compact
        if (mpn.startsWith("B3FS") || mpn.startsWith("B3U")) {
            return "SMD";
        }
        if (mpn.startsWith("B3F")) {
            return "THT";
        }
        if (mpn.startsWith("D2F") || mpn.startsWith("D2FC")) {
            // D2F microswitches with suffix
            // D2F-01L -> Long lever (L suffix after model)
            // D2F-01F -> Low force variant (F suffix)
            // D2FC-F-7N -> Compact low force (F is separate segment)
            String[] parts = mpn.split("-");
            for (String part : parts) {
                if (part.equals("L") || part.endsWith("L")) {
                    return "Long lever";
                }
                if (part.equals("F") || part.endsWith("F")) {
                    return "Low force";
                }
            }
            return "Standard";
        }
        return "";
    }

    private String extractSensorOutput(String mpn) {
        // EE-SX sensors have different output types
        // D6F flow sensors have different interfaces
        // E2E proximity sensors have different connector types
        if (mpn.contains("-NPN")) return "NPN";
        if (mpn.contains("-PNP")) return "PNP";
        if (mpn.contains("-NO")) return "NO";
        if (mpn.contains("-NC")) return "NC";

        // For E2E sensors, check connector suffix
        if (mpn.startsWith("E2E")) {
            if (mpn.endsWith("E1")) return "M12 Connector";
            if (mpn.endsWith("E2")) return "2m Cable";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Extract series up to first hyphen
        int hyphenIdx = upperMpn.indexOf('-');
        if (hyphenIdx > 0) {
            return upperMpn.substring(0, hyphenIdx);
        }

        // If no hyphen, try to extract the series prefix
        StringBuilder series = new StringBuilder();
        for (char c : upperMpn.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                series.append(c);
            } else {
                break;
            }
        }

        return series.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1.toUpperCase());
        String series2 = extractSeries(mpn2.toUpperCase());

        // Different series are not replacements
        if (!series1.equals(series2)) {
            // Check for known compatible series
            return isCompatibleSeries(series1, series2);
        }

        // Same series - check contact config or other key parameters
        String config1 = extractPackageCode(mpn1);
        String config2 = extractPackageCode(mpn2);

        // For relays, contact configuration must match
        if (isRelay(mpn1.toUpperCase()) && isRelay(mpn2.toUpperCase())) {
            if (!config1.isEmpty() && !config2.isEmpty() && !config1.equals(config2)) {
                return false;
            }
            // Check coil voltage compatibility
            return isCompatibleVoltage(mpn1, mpn2);
        }

        // For switches, different variants can be replacements
        if (isSwitch(mpn1.toUpperCase()) && isSwitch(mpn2.toUpperCase())) {
            // Different packages are not direct replacements
            if (!config1.isEmpty() && !config2.isEmpty() && !config1.equals(config2)) {
                return false;
            }
        }

        return true;
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Known compatible series pairs
        return (
            // G5V and G6K are both signal relays (different sizes)
            (series1.equals("G5V") && series2.equals("G6K")) ||
            (series1.equals("G6K") && series2.equals("G5V")) ||
            // G3MC and G3NA are both SSRs (different mounting)
            (series1.equals("G3MC") && series2.equals("G3NA")) ||
            (series1.equals("G3NA") && series2.equals("G3MC")) ||
            // B3F and B3FS are similar tact switches (THT vs SMD)
            (series1.equals("B3F") && series2.equals("B3FS")) ||
            (series1.equals("B3FS") && series2.equals("B3F"))
        );
    }

    private boolean isCompatibleVoltage(String mpn1, String mpn2) {
        // Extract coil voltage from relay MPNs
        // G5V-1-DC5 -> DC5 (5V DC)
        // G2R-1-DC12 -> DC12 (12V DC)
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);

        // Voltages must match
        return voltage1.equals(voltage2) || voltage1.isEmpty() || voltage2.isEmpty();
    }

    private String extractVoltage(String mpn) {
        // Look for voltage pattern at the end
        // DC5, DC12, DC24, AC120, etc.
        String upperMpn = mpn.toUpperCase();
        if (upperMpn.contains("-DC")) {
            int idx = upperMpn.lastIndexOf("-DC");
            return upperMpn.substring(idx + 1).replaceAll("[^A-Z0-9]", "");
        }
        if (upperMpn.contains("-AC")) {
            int idx = upperMpn.lastIndexOf("-AC");
            return upperMpn.substring(idx + 1).replaceAll("[^A-Z0-9]", "");
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods for additional information extraction

    /**
     * Get the product family description for a given MPN.
     */
    public String getFamily(String mpn) {
        String series = extractSeries(mpn);
        if (series == null || series.isEmpty()) return "Unknown";

        // Check relay families
        String relayFamily = RELAY_FAMILIES.get(series);
        if (relayFamily != null) return relayFamily;

        // Check for partial matches in relay families
        for (Map.Entry<String, String> entry : RELAY_FAMILIES.entrySet()) {
            if (series.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }

        // Check switch families
        String switchFamily = SWITCH_FAMILIES.get(series);
        if (switchFamily != null) return switchFamily;

        // Check sensor families (ordered from most specific to least)
        String upperMpn = mpn.toUpperCase();
        for (String[] entry : SENSOR_FAMILIES_ORDERED) {
            if (upperMpn.startsWith(entry[0])) {
                return entry[1];
            }
        }

        return "Unknown";
    }

    /**
     * Get the coil/control voltage for relays and SSRs.
     */
    public String getCoilVoltage(String mpn) {
        String voltage = extractVoltage(mpn);
        if (voltage.isEmpty()) return "";

        // Parse and format voltage
        if (voltage.startsWith("DC")) {
            return voltage.substring(2) + "V DC";
        }
        if (voltage.startsWith("AC")) {
            return voltage.substring(2) + "V AC";
        }
        return voltage;
    }

    /**
     * Get contact rating for relays.
     */
    public String getContactRating(String mpn) {
        String series = extractSeries(mpn);
        if (series == null) return "";

        // Known ratings by series
        return switch (series) {
            case "G5V" -> "1A 24VDC";
            case "G6K" -> "1A 30VDC";
            case "G2R" -> "5A 250VAC";
            case "G2RL" -> "8A 250VAC";
            case "G5RL" -> "16A 250VAC";
            default -> "";
        };
    }

    /**
     * Get actuation force for switches.
     */
    public String getActuationForce(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // B3F series uses the last 3-4 digits to indicate force/travel
        if (upperMpn.startsWith("B3F-")) {
            String[] parts = upperMpn.split("-");
            if (parts.length >= 2) {
                String variant = parts[1].replaceAll("[^0-9]", "");
                if (variant.length() >= 3) {
                    char forceCode = variant.charAt(0);
                    return switch (forceCode) {
                        case '1' -> "1.47N";
                        case '2' -> "2.55N";
                        case '3' -> "3.43N";
                        case '4' -> "4.12N";
                        case '5' -> "6.37N";
                        default -> "";
                    };
                }
            }
        }

        // D2F series
        if (upperMpn.startsWith("D2F") || upperMpn.startsWith("D2FC")) {
            if (upperMpn.contains("-F")) return "0.74N"; // Low force
            if (upperMpn.contains("-L")) return "1.47N"; // Standard with lever
            return "1.47N"; // Standard
        }

        return "";
    }
}
