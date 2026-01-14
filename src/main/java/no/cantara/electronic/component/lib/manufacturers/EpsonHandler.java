package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for Epson timing devices and crystals
 */
public class EpsonHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Crystal Units
        registry.addPattern(ComponentType.CRYSTAL, "^FA-?[0-9].*");          // AT-cut crystals
        registry.addPattern(ComponentType.CRYSTAL, "^FC-?[0-9].*");          // Tuning fork crystals
        registry.addPattern(ComponentType.CRYSTAL, "^MA-?[0-9].*");          // High frequency crystals
        registry.addPattern(ComponentType.CRYSTAL, "^MC-?[0-9].*");          // Ceramic package crystals
        registry.addPattern(ComponentType.CRYSTAL_EPSON, "^FA-?[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_EPSON, "^FC-?[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_EPSON, "^M[AC]-?[0-9].*");

        // Oscillators
        // Standard Clock Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^SG-?[0-9].*");       // Standard oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^SG-?210[0-9].*");    // Programmable oscillators
        registry.addPattern(ComponentType.OSCILLATOR_EPSON, "^SG-?[0-9].*");

        // TCXO (Temperature Compensated Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^TG-?[0-9].*");       // TCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^TG-?3541[0-9].*");   // High stability TCXO
        registry.addPattern(ComponentType.OSCILLATOR_TCXO_EPSON, "^TG-?[0-9].*");

        // VCXO (Voltage Controlled Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^VG-?[0-9].*");       // VCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^VG-?4513[0-9].*");   // High stability VCXO
        registry.addPattern(ComponentType.OSCILLATOR_VCXO_EPSON, "^VG-?[0-9].*");

        // OCXO (Oven Controlled Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^HG-?[0-9].*");       // OCXO
        registry.addPattern(ComponentType.OSCILLATOR_OCXO_EPSON, "^HG-?[0-9].*");

        // Real-Time Clock Modules
        registry.addPattern(ComponentType.IC, "^RX-?[0-9].*");              // RTC modules
        registry.addPattern(ComponentType.IC, "^RX-?4571[0-9].*");          // Low power RTC
        registry.addPattern(ComponentType.IC, "^RX-?8900[0-9].*");          // High accuracy RTC

        // Timing Devices
        registry.addPattern(ComponentType.IC, "^RA-?[0-9].*");              // Programmable timers
        registry.addPattern(ComponentType.IC, "^RR-?[0-9].*");              // Real-time clock ICs
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CRYSTAL,
            ComponentType.CRYSTAL_EPSON,
            ComponentType.OSCILLATOR,
            ComponentType.OSCILLATOR_EPSON,
            ComponentType.OSCILLATOR_TCXO_EPSON,
            ComponentType.OSCILLATOR_VCXO_EPSON,
            ComponentType.OSCILLATOR_OCXO_EPSON,
            ComponentType.RTC_EPSON,
            ComponentType.TIMER_EPSON,
            ComponentType.GYRO_SENSOR_EPSON
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Crystal packages
        if (upperMpn.startsWith("FA") || upperMpn.startsWith("FC")) {
            try {
                // Extract size code from the model number
                String sizeCode = upperMpn.substring(upperMpn.indexOf('-') + 1, upperMpn.indexOf('-') + 4);
                return switch (sizeCode) {
                    case "128" -> "1.2 x 1.0mm";
                    case "135" -> "1.6 x 1.2mm";
                    case "238" -> "2.0 x 1.6mm";
                    case "328" -> "3.2 x 2.5mm";
                    case "405" -> "4.0 x 2.5mm";
                    case "506" -> "5.0 x 3.2mm";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Oscillator packages
        if (upperMpn.startsWith("SG") || upperMpn.startsWith("TG") ||
                upperMpn.startsWith("VG") || upperMpn.startsWith("HG")) {
            try {
                String modelNum = upperMpn.substring(upperMpn.indexOf('-') + 1, upperMpn.indexOf('-') + 5);
                return switch (modelNum) {
                    case "210" -> "2.0 x 1.6mm";
                    case "310" -> "2.5 x 2.0mm";
                    case "510" -> "3.2 x 2.5mm";
                    case "531" -> "5.0 x 3.2mm";
                    case "7050" -> "7.0 x 5.0mm";
                    case "8002" -> "8.0 x 4.5mm";
                    default -> modelNum;
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
        if (upperMpn.startsWith("FA")) return "AT-Cut Crystal";
        if (upperMpn.startsWith("FC")) return "Tuning Fork Crystal";
        if (upperMpn.startsWith("MA")) return "High Frequency Crystal";
        if (upperMpn.startsWith("MC")) return "Ceramic Package Crystal";

        // Oscillator series
        if (upperMpn.startsWith("SG")) {
            if (upperMpn.contains("210")) return "Programmable Oscillator";
            return "Standard Oscillator";
        }
        if (upperMpn.startsWith("TG")) {
            if (upperMpn.contains("3541")) return "High Stability TCXO";
            return "TCXO";
        }
        if (upperMpn.startsWith("VG")) {
            if (upperMpn.contains("4513")) return "High Stability VCXO";
            return "VCXO";
        }
        if (upperMpn.startsWith("HG")) return "OCXO";

        // RTC series
        if (upperMpn.startsWith("RX")) {
            if (upperMpn.contains("4571")) return "Low Power RTC";
            if (upperMpn.contains("8900")) return "High Accuracy RTC";
            return "RTC Module";
        }

        // Timing series
        if (upperMpn.startsWith("RA")) return "Programmable Timer";
        if (upperMpn.startsWith("RR")) return "RTC IC";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // For crystals and oscillators, same package and frequency might be compatible
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        if (!pkg1.equals(pkg2)) return false;

        // High stability versions can typically replace standard versions
        if (series1.contains("High Stability") &&
                series2.replace("High Stability ", "").equals(series1.replace("High Stability ", ""))) {
            return true;
        }

        // Within same basic series, check frequency compatibility
        if (mpn1.startsWith("SG") || mpn1.startsWith("TG") ||
                mpn1.startsWith("VG") || mpn1.startsWith("HG")) {
            try {
                // Extract frequency code (if present)
                String freq1 = extractFrequencyCode(mpn1);
                String freq2 = extractFrequencyCode(mpn2);
                return freq1.equals(freq2);
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private String extractFrequencyCode(String mpn) {
        // Example implementation - would need to be adjusted based on actual part numbering
        int freqStart = mpn.lastIndexOf('-');
        if (freqStart >= 0 && freqStart < mpn.length() - 1) {
            return mpn.substring(freqStart + 1);
        }
        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}