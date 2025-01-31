package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for NDK (Nihon Dempa Kogyo) timing devices and crystals
 */
public class NDKHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Crystal Units
        registry.addPattern(ComponentType.CRYSTAL, "^NX[0-9].*");           // Standard crystals
        registry.addPattern(ComponentType.CRYSTAL, "^NT[0-9].*");           // Tuning fork crystals
        registry.addPattern(ComponentType.CRYSTAL, "^NH[0-9].*");           // High frequency crystals
        registry.addPattern(ComponentType.CRYSTAL, "^NAT[0-9].*");          // Automotive grade crystals
        registry.addPattern(ComponentType.CRYSTAL_NDK, "^NX[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_NDK, "^NT[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_NDK, "^NH[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_NDK, "^NAT[0-9].*");

        // Clock Oscillators
        // Standard Clock Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^NZ[0-9].*");        // Standard oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^NP[0-9].*");        // Programmable oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^NV[0-9].*");        // Low jitter oscillators
        registry.addPattern(ComponentType.OSCILLATOR_NDK, "^NZ[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_NDK, "^NP[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_NDK, "^NV[0-9].*");

        // TCXO (Temperature Compensated Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^NT[0-9].*");        // TCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^NTW[0-9].*");       // Wide temp range TCXO
        registry.addPattern(ComponentType.OSCILLATOR_TCXO_NDK, "^NT[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_TCXO_NDK, "^NTW[0-9].*");

        // VCXO (Voltage Controlled Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^NV[0-9].*");        // VCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^NVW[0-9].*");       // Wide pull range VCXO
        registry.addPattern(ComponentType.OSCILLATOR_VCXO_NDK, "^NV[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_VCXO_NDK, "^NVW[0-9].*");

        // OCXO (Oven Controlled Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^NO[0-9].*");        // OCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^NH[0-9].*");        // High stability OCXO
        registry.addPattern(ComponentType.OSCILLATOR_OCXO_NDK, "^NO[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_OCXO_NDK, "^NH[0-9].*");

        // VCSO (Voltage Controlled SAW Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^VS[0-9].*");        // VCSO
        registry.addPattern(ComponentType.OSCILLATOR, "^VSW[0-9].*");       // Wide pull range VCSO

        // SAW Devices
        registry.addPattern(ComponentType.IC, "^SF[0-9].*");               // SAW filters
        registry.addPattern(ComponentType.IC, "^SR[0-9].*");               // SAW resonators
        registry.addPattern(ComponentType.IC, "^SD[0-9].*");               // SAW duplexers
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CRYSTAL);
        types.add(ComponentType.CRYSTAL_NDK);
        types.add(ComponentType.OSCILLATOR);
        types.add(ComponentType.OSCILLATOR_NDK);
        types.add(ComponentType.OSCILLATOR_TCXO_NDK);
        types.add(ComponentType.OSCILLATOR_VCXO_NDK);
        types.add(ComponentType.OSCILLATOR_OCXO_NDK);
        types.add(ComponentType.SAW_FILTER_NDK);
        types.add(ComponentType.SAW_RESONATOR_NDK);
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Crystal packages
        if (upperMpn.startsWith("NX") || upperMpn.startsWith("NT") ||
                upperMpn.startsWith("NH") || upperMpn.startsWith("NAT")) {
            try {
                // Extract size code from the model number
                String sizeCode = upperMpn.substring(3, 5);
                return switch (sizeCode) {
                    case "12" -> "1.2 x 1.0mm";
                    case "16" -> "1.6 x 1.2mm";
                    case "20" -> "2.0 x 1.6mm";
                    case "25" -> "2.5 x 2.0mm";
                    case "32" -> "3.2 x 2.5mm";
                    case "50" -> "5.0 x 3.2mm";
                    case "80" -> "8.0 x 4.5mm";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Oscillator packages
        if (upperMpn.startsWith("NZ") || upperMpn.startsWith("NP") ||
                upperMpn.startsWith("NV") || upperMpn.startsWith("NO")) {
            try {
                String sizeCode = upperMpn.substring(3, 5);
                return switch (sizeCode) {
                    case "21" -> "2.0 x 1.6mm";
                    case "25" -> "2.5 x 2.0mm";
                    case "32" -> "3.2 x 2.5mm";
                    case "50" -> "5.0 x 3.2mm";
                    case "70" -> "7.0 x 5.0mm";
                    case "98" -> "9.8 x 7.5mm";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Crystal series
        if (upperMpn.startsWith("NX")) return "Standard Crystal";
        if (upperMpn.startsWith("NT")) return "Tuning Fork Crystal";
        if (upperMpn.startsWith("NH")) return "High Frequency Crystal";
        if (upperMpn.startsWith("NAT")) return "Automotive Crystal";

        // Oscillator series
        if (upperMpn.startsWith("NZ")) return "Standard Oscillator";
        if (upperMpn.startsWith("NP")) return "Programmable Oscillator";
        if (upperMpn.startsWith("NV") && upperMpn.length() > 3) {
            char type = upperMpn.charAt(2);
            if (type == 'W') return "Wide Pull VCXO";
            return "VCXO";
        }

        // TCXO series
        if (upperMpn.startsWith("NT") && upperMpn.length() > 3) {
            char type = upperMpn.charAt(2);
            if (type == 'W') return "Wide Temp TCXO";
            return "TCXO";
        }

        // OCXO series
        if (upperMpn.startsWith("NO")) return "OCXO";
        if (upperMpn.startsWith("NH") && !upperMpn.startsWith("NH[0-9]"))
            return "High Stability OCXO";

        // VCSO series
        if (upperMpn.startsWith("VS")) {
            if (upperMpn.startsWith("VSW")) return "Wide Pull VCSO";
            return "VCSO";
        }

        // SAW devices
        if (upperMpn.startsWith("SF")) return "SAW Filter";
        if (upperMpn.startsWith("SR")) return "SAW Resonator";
        if (upperMpn.startsWith("SD")) return "SAW Duplexer";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) {
            // Some cross-series compatibility exists
            if (isCompatibleSeries(series1, series2)) {
                String pkg1 = extractPackageCode(mpn1);
                String pkg2 = extractPackageCode(mpn2);
                return pkg1.equals(pkg2);
            }
            return false;
        }

        // For crystals and oscillators, same package and frequency required
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        if (!pkg1.equals(pkg2)) return false;

        // Check frequency compatibility
        try {
            String freq1 = extractFrequencyCode(mpn1);
            String freq2 = extractFrequencyCode(mpn2);
            return freq1.equals(freq2);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Wide temperature range can replace standard
        if (series1.startsWith("Wide Temp") &&
                series2.equals(series1.replace("Wide Temp ", ""))) return true;
        if (series2.startsWith("Wide Temp") &&
                series1.equals(series2.replace("Wide Temp ", ""))) return true;

        // High stability can replace standard
        if (series1.startsWith("High Stability") &&
                series2.equals(series1.replace("High Stability ", ""))) return true;
        if (series2.startsWith("High Stability") &&
                series1.equals(series2.replace("High Stability ", ""))) return true;

        return false;
    }

    private String extractFrequencyCode(String mpn) {
        // Example: for parts like NX3225SA-24.000M
        int lastDash = mpn.lastIndexOf('-');
        if (lastDash >= 0 && lastDash < mpn.length() - 1) {
            return mpn.substring(lastDash + 1);
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}