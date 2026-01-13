package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler for Kyocera Corporation electronic components.
 * <p>
 * Kyocera is a major Japanese manufacturer of electronic components including:
 * <ul>
 *   <li>CX series - Ceramic resonators</li>
 *   <li>KC series - Crystal oscillators</li>
 *   <li>CT series - Ceramic capacitors</li>
 *   <li>5600/5800 series - Connectors</li>
 *   <li>AVX acquisition products - Various capacitor lines</li>
 * </ul>
 * <p>
 * Note: Kyocera acquired AVX Corporation in 2019. Some AVX product lines are
 * now branded under Kyocera AVX.
 */
public class KyoceraHandler extends AbstractManufacturerHandler {

    // Ceramic resonator patterns
    private static final Pattern CX_RESONATOR_PATTERN = Pattern.compile("^CX[-]?[0-9]{2,4}[A-Z]*.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CXO_RESONATOR_PATTERN = Pattern.compile("^CXO[-]?[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSTS_RESONATOR_PATTERN = Pattern.compile("^CSTS[A-Z]*[-]?[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern PBRC_RESONATOR_PATTERN = Pattern.compile("^PBRC[-]?[0-9]+.*", Pattern.CASE_INSENSITIVE);

    // Crystal oscillator patterns
    private static final Pattern KC_OSCILLATOR_PATTERN = Pattern.compile("^KC[0-9]{4}[A-Z]*[-]?.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern KT_OSCILLATOR_PATTERN = Pattern.compile("^KT[0-9]{4}[A-Z]*[-]?.*", Pattern.CASE_INSENSITIVE);

    // Ceramic capacitor patterns
    private static final Pattern CT_CAPACITOR_PATTERN = Pattern.compile("^CT[0-9]{2,4}[A-Z0-9]*.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CM_CAPACITOR_PATTERN = Pattern.compile("^CM[0-9]{2,4}[A-Z0-9]*.*", Pattern.CASE_INSENSITIVE);

    // Connector patterns
    private static final Pattern CONNECTOR_5600_PATTERN = Pattern.compile("^5[6-9][0-9]{2}[-]?[0-9]+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONNECTOR_SERIES_PATTERN = Pattern.compile("^[0-9]{4,5}[-][0-9]{3,4}[-]?[0-9]*.*", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Ceramic Resonators (CX, CXO, CSTS, PBRC series)
        registry.addPattern(ComponentType.CRYSTAL, "^CX[-]?[0-9]{2,4}[A-Z]*.*");
        registry.addPattern(ComponentType.CRYSTAL, "^CXO[-]?[0-9]+.*");
        registry.addPattern(ComponentType.CRYSTAL, "^CSTS[A-Z]*[-]?[0-9]+.*");
        registry.addPattern(ComponentType.CRYSTAL, "^PBRC[-]?[0-9]+.*");

        // Crystal Oscillators (KC, KT series)
        registry.addPattern(ComponentType.OSCILLATOR, "^KC[0-9]{4}[A-Z]*[-]?.*");
        registry.addPattern(ComponentType.OSCILLATOR, "^KT[0-9]{4}[A-Z]*[-]?.*");

        // Ceramic Capacitors (CT, CM series)
        registry.addPattern(ComponentType.CAPACITOR, "^CT[0-9]{2,4}[A-Z0-9]*.*");
        registry.addPattern(ComponentType.CAPACITOR, "^CM[0-9]{2,4}[A-Z0-9]*.*");

        // Connectors (5600/5800 series and numeric series)
        registry.addPattern(ComponentType.CONNECTOR, "^5[6-9][0-9]{2}[-]?[0-9]+.*");
        registry.addPattern(ComponentType.CONNECTOR, "^[0-9]{4,5}[-][0-9]{3,4}[-]?[0-9]*.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CRYSTAL,
            ComponentType.OSCILLATOR,
            ComponentType.CAPACITOR,
            ComponentType.CONNECTOR
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check crystal/resonator patterns
        if (type == ComponentType.CRYSTAL) {
            return CX_RESONATOR_PATTERN.matcher(upperMpn).matches() ||
                   CXO_RESONATOR_PATTERN.matcher(upperMpn).matches() ||
                   CSTS_RESONATOR_PATTERN.matcher(upperMpn).matches() ||
                   PBRC_RESONATOR_PATTERN.matcher(upperMpn).matches();
        }

        // Check oscillator patterns
        if (type == ComponentType.OSCILLATOR) {
            return KC_OSCILLATOR_PATTERN.matcher(upperMpn).matches() ||
                   KT_OSCILLATOR_PATTERN.matcher(upperMpn).matches();
        }

        // Check capacitor patterns
        if (type == ComponentType.CAPACITOR) {
            return CT_CAPACITOR_PATTERN.matcher(upperMpn).matches() ||
                   CM_CAPACITOR_PATTERN.matcher(upperMpn).matches();
        }

        // Check connector patterns
        if (type == ComponentType.CONNECTOR) {
            return CONNECTOR_5600_PATTERN.matcher(upperMpn).matches() ||
                   CONNECTOR_SERIES_PATTERN.matcher(upperMpn).matches();
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // CX resonator package codes
        // Format: CX-XXFFA where XX = dimensions (32 = 3.2x1.5mm, 49 = 4.5x2.0mm)
        if (upperMpn.startsWith("CX")) {
            String normalized = upperMpn.replace("-", "");
            if (normalized.length() >= 4) {
                String sizeCode = normalized.substring(2, 4);
                return mapResonatorPackage(sizeCode);
            }
        }

        // KC oscillator package codes
        // Format: KC1612X-C3 where 1612 = 1.6x1.2mm
        if (upperMpn.startsWith("KC") || upperMpn.startsWith("KT")) {
            String normalized = upperMpn.replace("-", "");
            if (normalized.length() >= 6) {
                String sizeCode = normalized.substring(2, 6);
                return mapOscillatorPackage(sizeCode);
            }
        }

        // CT/CM capacitor package codes
        // Format: CT31 where 31 = size code
        if (upperMpn.startsWith("CT") || upperMpn.startsWith("CM")) {
            if (upperMpn.length() >= 4) {
                String sizeCode = upperMpn.substring(2, 4);
                return mapCapacitorPackage(sizeCode);
            }
        }

        // Connector package codes
        // Format: 5600-050-141 where 050 can indicate pin count
        if (CONNECTOR_5600_PATTERN.matcher(upperMpn).matches() ||
            CONNECTOR_SERIES_PATTERN.matcher(upperMpn).matches()) {
            return extractConnectorPackage(upperMpn);
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Ceramic Resonators
        if (upperMpn.startsWith("CX-") || upperMpn.matches("^CX[0-9].*")) {
            return "CX Ceramic Resonator";
        }
        if (upperMpn.startsWith("CXO")) {
            return "CXO Ceramic Resonator";
        }
        if (upperMpn.startsWith("CSTS")) {
            return "CSTS Ceramic Resonator";
        }
        if (upperMpn.startsWith("PBRC")) {
            return "PBRC Ceramic Resonator";
        }

        // Crystal Oscillators
        if (upperMpn.startsWith("KC")) {
            if (upperMpn.contains("D")) {
                return "KC-D Crystal Oscillator";
            }
            if (upperMpn.contains("B")) {
                return "KC-B Crystal Oscillator";
            }
            return "KC Crystal Oscillator";
        }
        if (upperMpn.startsWith("KT")) {
            return "KT Crystal Oscillator";
        }

        // Ceramic Capacitors
        if (upperMpn.startsWith("CT")) {
            if (upperMpn.length() >= 4) {
                String series = upperMpn.substring(0, 4);
                return series + " Ceramic Capacitor";
            }
            return "CT Ceramic Capacitor";
        }
        if (upperMpn.startsWith("CM")) {
            if (upperMpn.length() >= 4) {
                String series = upperMpn.substring(0, 4);
                return series + " Ceramic Capacitor";
            }
            return "CM Ceramic Capacitor";
        }

        // Connectors
        if (CONNECTOR_5600_PATTERN.matcher(upperMpn).matches()) {
            int dashIndex = upperMpn.indexOf('-');
            String seriesNum = dashIndex > 0 ? upperMpn.substring(0, dashIndex) : upperMpn.substring(0, 4);
            return seriesNum + " Connector";
        }
        if (CONNECTOR_SERIES_PATTERN.matcher(upperMpn).matches()) {
            int dashIndex = upperMpn.indexOf('-');
            if (dashIndex > 0) {
                return upperMpn.substring(0, dashIndex) + " Connector";
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // For resonators and oscillators, check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Same series and same package are typically compatible
        if (!pkg1.isEmpty() && !pkg2.isEmpty()) {
            return pkg1.equals(pkg2);
        }

        // For resonators, check frequency compatibility
        if (series1.contains("Resonator") || series1.contains("Oscillator")) {
            String freq1 = extractFrequency(mpn1);
            String freq2 = extractFrequency(mpn2);
            if (!freq1.isEmpty() && !freq2.isEmpty()) {
                return freq1.equals(freq2);
            }
        }

        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // ========== Helper Methods ==========

    private String mapResonatorPackage(String sizeCode) {
        return switch (sizeCode) {
            case "32" -> "3.2x1.5mm";
            case "49" -> "4.5x2.0mm";
            case "20" -> "2.0x1.2mm";
            case "25" -> "2.5x2.0mm";
            case "16" -> "1.6x1.0mm";
            default -> sizeCode + "mm";
        };
    }

    private String mapOscillatorPackage(String sizeCode) {
        return switch (sizeCode) {
            case "1612" -> "1.6x1.2mm";
            case "2016" -> "2.0x1.6mm";
            case "2520" -> "2.5x2.0mm";
            case "3215" -> "3.2x1.5mm";
            case "3225" -> "3.2x2.5mm";
            case "5032" -> "5.0x3.2mm";
            case "7050" -> "7.0x5.0mm";
            default -> sizeCode.substring(0, 2) + "x" + sizeCode.substring(2) + "mm";
        };
    }

    private String mapCapacitorPackage(String sizeCode) {
        return switch (sizeCode) {
            case "01" -> "0201/0603M";
            case "02" -> "0402/1005M";
            case "03" -> "0603/1608M";
            case "05" -> "0805/2012M";
            case "06" -> "1206/3216M";
            case "31" -> "0603/1608M";
            case "41" -> "0805/2012M";
            case "42" -> "1206/3216M";
            case "43" -> "1210/3225M";
            case "45" -> "1812/4532M";
            default -> sizeCode;
        };
    }

    private String extractConnectorPackage(String mpn) {
        // Extract pin count from connector MPN
        // Format: 5600-050-141 where 050 might indicate 50 pins
        String[] parts = mpn.split("-");
        if (parts.length >= 2) {
            try {
                int pinCount = Integer.parseInt(parts[1]);
                if (pinCount > 0 && pinCount < 200) {
                    return pinCount + "-pin";
                }
            } catch (NumberFormatException e) {
                // Not a numeric pin count
            }
        }
        return "";
    }

    private String extractFrequency(String mpn) {
        // Try to extract frequency from MPN
        // Common formats: xxx-4.000M, xxx-16.000MHz
        String upperMpn = mpn.toUpperCase();

        // Look for frequency specification after last dash
        int lastDash = upperMpn.lastIndexOf('-');
        if (lastDash >= 0 && lastDash < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(lastDash + 1);
            // Check if it looks like a frequency (contains digits and M/K/Hz)
            if (suffix.matches(".*[0-9]+.*[MK].*") || suffix.matches(".*[0-9]+.*HZ.*")) {
                return suffix;
            }
        }

        // Look for embedded frequency in the MPN
        // Format like CX-3215GA (no explicit frequency in MPN)
        return "";
    }
}
