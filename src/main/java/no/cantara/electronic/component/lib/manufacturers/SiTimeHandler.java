package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for SiTime MEMS Oscillators and timing devices.
 * <p>
 * SiTime product families:
 * <ul>
 *   <li>SiT15xx - MHz LVCMOS Oscillators (SiT1533, SiT1534, SiT1552)</li>
 *   <li>SiT16xx - Low Power Oscillators (SiT1602, SiT1618)</li>
 *   <li>SiT17xx - Ultra Low Power (SiT1701)</li>
 *   <li>SiT80xx - Precision MHz Oscillators (SiT8008, SiT8021)</li>
 *   <li>SiT81xx - Differential Oscillators (SiT8148, SiT8152)</li>
 *   <li>SiT85xx - Super-TCXO (SiT5155, SiT5156)</li>
 *   <li>SiT86xx - High Performance (SiT8621)</li>
 *   <li>SiT88xx - Network Sync (SiT8814)</li>
 *   <li>SiT90xx - High Temp Oscillators (SiT9002)</li>
 *   <li>SiT91xx - AEC-Q100 Automotive (SiT9120, SiT9121)</li>
 *   <li>SiT93xx - Clock Generators (SiT9367)</li>
 *   <li>SiT95xx - High Performance Clock (SiT9501)</li>
 * </ul>
 * <p>
 * MPN Format: SiTxxxxYY-ZZ-AA-FF.FFFX
 * <ul>
 *   <li>xxxx - Product family/model number</li>
 *   <li>YY - Temperature grade and options (e.g., AI, BI)</li>
 *   <li>ZZ - Voltage and configuration code (e.g., 71, H4)</li>
 *   <li>AA - Package size code (e.g., 18E, DCC)</li>
 *   <li>FF.FFF - Frequency (e.g., 24.000000, 32.768)</li>
 *   <li>X - Output type suffix (optional)</li>
 * </ul>
 */
public class SiTimeHandler implements ManufacturerHandler {

    // Pattern for extracting frequency from MPN
    private static final Pattern FREQUENCY_PATTERN = Pattern.compile(
            "([0-9]+(?:\\.[0-9]+)?)[MKX]?$", Pattern.CASE_INSENSITIVE);

    // Pattern for extracting series from MPN
    private static final Pattern SERIES_PATTERN = Pattern.compile(
            "^SIT([0-9]{4})", Pattern.CASE_INSENSITIVE);

    // Pattern for extracting package size code
    private static final Pattern PACKAGE_PATTERN = Pattern.compile(
            "-([0-9]{2}[A-Z]|[A-Z]{2,3})-", Pattern.CASE_INSENSITIVE);

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // SiT15xx - MHz LVCMOS Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT15[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT15[0-9]{2}.*");

        // SiT16xx - Low Power Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT16[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT16[0-9]{2}.*");

        // SiT17xx - Ultra Low Power
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT17[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT17[0-9]{2}.*");

        // SiT80xx - Precision MHz Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT80[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT80[0-9]{2}.*");

        // SiT81xx - Differential Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT81[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT81[0-9]{2}.*");

        // SiT85xx - Super-TCXO (also registered as SiT5xxx)
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT85[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT85[0-9]{2}.*");
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT5[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^SIT5[0-9]{3}.*");

        // SiT86xx - High Performance
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT86[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT86[0-9]{2}.*");

        // SiT88xx - Network Sync
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT88[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT88[0-9]{2}.*");

        // SiT90xx - High Temp Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT90[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT90[0-9]{2}.*");

        // SiT91xx - AEC-Q100 Automotive
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT91[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT91[0-9]{2}.*");

        // SiT93xx - Clock Generators
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT93[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT93[0-9]{2}.*");

        // SiT95xx - High Performance Clock
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT95[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SIT95[0-9]{2}.*");

        // Generic SiTime pattern for any SiTxxxx device
        registry.addPattern(ComponentType.OSCILLATOR, "^SIT[0-9]{4}.*");
        registry.addPattern(ComponentType.IC, "^SIT[0-9]{4}.*");

        // Some SiTime devices may be referred to as crystal replacements
        registry.addPattern(ComponentType.CRYSTAL, "^SIT15[0-9]{2}.*-32\\.768.*");
        registry.addPattern(ComponentType.CRYSTAL, "^SIT16[0-9]{2}.*-32\\.768.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.OSCILLATOR,
                ComponentType.CRYSTAL,
                ComponentType.IC
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Try to extract package code from format: SiTxxxxYY-ZZ-AA-freq
        // The package code is typically the third segment (AA)
        Matcher matcher = PACKAGE_PATTERN.matcher(upperMpn);
        if (matcher.find()) {
            String packageCode = matcher.group(1);
            return mapPackageCode(packageCode);
        }

        // Alternative: look for common SiTime size codes at specific positions
        String[] parts = upperMpn.split("-");
        if (parts.length >= 3) {
            String sizePart = parts[2];
            return mapPackageCode(sizePart);
        }

        return "";
    }

    /**
     * Map SiTime package codes to standard package descriptions.
     * SiTime uses numeric size codes with letter suffixes.
     */
    private String mapPackageCode(String code) {
        if (code == null || code.isEmpty()) return "";

        String upperCode = code.toUpperCase();

        // Size codes with letter suffix (e.g., 18E, 25E)
        return switch (upperCode) {
            case "18E", "18" -> "1.6x1.2mm";      // 1612 size
            case "21E", "21" -> "2.0x1.6mm";      // 2016 size
            case "25E", "25" -> "2.5x2.0mm";      // 2520 size
            case "32E", "32" -> "3.2x2.5mm";      // 3225 size
            case "50E", "50" -> "5.0x3.2mm";      // 5032 size
            case "70E", "70" -> "7.0x5.0mm";      // 7050 size
            // Chip-scale packages
            case "DCC" -> "1.5x0.8mm CSP";        // Chip-Scale Package
            case "DCS" -> "1.2x1.0mm CSP";        // Ultra-small CSP
            // SOT-style packages
            case "SOT" -> "SOT23-5";
            // QFN packages
            case "QFN" -> "QFN";
            default -> upperCode;
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Handle SiT5xxx series (Super-TCXO) first - single digit series (SiT5)
        // This must be checked before the general 4-digit pattern
        if (upperMpn.startsWith("SIT5") && upperMpn.length() > 4 && Character.isDigit(upperMpn.charAt(4))) {
            return "SiT5";
        }

        // Extract 4-digit series code for standard SiTxxxx parts
        Matcher matcher = SERIES_PATTERN.matcher(upperMpn);
        if (matcher.find()) {
            String seriesCode = matcher.group(1);
            return "SiT" + seriesCode.substring(0, 2);  // Return SiTxx (e.g., SiT15, SiT80)
        }

        return "";
    }

    /**
     * Get the descriptive series name for a SiTime product.
     */
    public String getSeriesDescription(String mpn) {
        String series = extractSeries(mpn);
        if (series.isEmpty()) return "";

        return switch (series) {
            case "SiT15" -> "MHz LVCMOS Oscillator";
            case "SiT16" -> "Low Power Oscillator";
            case "SiT17" -> "Ultra Low Power Oscillator";
            case "SiT5", "SiT85" -> "Super-TCXO";
            case "SiT80" -> "Precision MHz Oscillator";
            case "SiT81" -> "Differential Oscillator";
            case "SiT86" -> "High Performance Oscillator";
            case "SiT88" -> "Network Sync Oscillator";
            case "SiT90" -> "High Temperature Oscillator";
            case "SiT91" -> "AEC-Q100 Automotive Oscillator";
            case "SiT93" -> "Clock Generator";
            case "SiT95" -> "High Performance Clock";
            default -> "MEMS Oscillator";
        };
    }

    /**
     * Extract the frequency from the MPN.
     * Format examples: 24.000000X, 32.768, 25.000000
     */
    public String extractFrequency(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Frequency is typically at the end after the last hyphen
        int lastHyphen = mpn.lastIndexOf('-');
        if (lastHyphen >= 0 && lastHyphen < mpn.length() - 1) {
            String freqPart = mpn.substring(lastHyphen + 1);
            Matcher matcher = FREQUENCY_PATTERN.matcher(freqPart);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Must be same series
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Must have same frequency
        String freq1 = extractFrequency(mpn1);
        String freq2 = extractFrequency(mpn2);
        if (freq1.isEmpty() || !freq1.equals(freq2)) {
            return false;
        }

        // Same series and frequency can be replacements (may differ in temp grade, package)
        return true;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Check for SiTime prefix
        if (!upperMpn.startsWith("SIT")) {
            return false;
        }

        // Check supported types
        if (type == ComponentType.OSCILLATOR || type == ComponentType.IC) {
            return upperMpn.matches("^SIT[0-9]{4}.*");
        }

        if (type == ComponentType.CRYSTAL) {
            // 32.768kHz parts can be used as crystal replacements
            return upperMpn.matches("^SIT1[567][0-9]{2}.*-32\\.768.*");
        }

        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
