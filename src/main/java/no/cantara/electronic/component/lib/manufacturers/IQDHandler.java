package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for IQD (International Quartz Devices) frequency products
 */
public class IQDHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Crystal Units
        registry.addPattern(ComponentType.CRYSTAL, "^LFXTAL[0-9].*");      // Low frequency crystals
        registry.addPattern(ComponentType.CRYSTAL, "^CFPX[0-9].*");        // Standard crystals
        registry.addPattern(ComponentType.CRYSTAL, "^XTALS[0-9].*");       // Surface mount crystals
        registry.addPattern(ComponentType.CRYSTAL, "^XTAL[0-9].*");        // Through-hole crystals
        registry.addPattern(ComponentType.CRYSTAL_IQD, "^LFXTAL[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_IQD, "^CFPX[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_IQD, "^XTALS[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_IQD, "^XTAL[0-9].*");

        // Clock Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^IQXO-[0-9].*");    // Standard oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^IQXO-H[0-9].*");   // High frequency oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^IQXO-L[0-9].*");   // Low power oscillators
        registry.addPattern(ComponentType.OSCILLATOR_IQD, "^IQXO-[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_IQD, "^IQXO-[HL][0-9].*");

        // TCXO (Temperature Compensated Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^IQTX-[0-9].*");    // TCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^IQTX-H[0-9].*");   // High stability TCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^IQTX-L[0-9].*");   // Low power TCXO
        registry.addPattern(ComponentType.OSCILLATOR_TCXO_IQD, "^IQTX-[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_TCXO_IQD, "^IQTX-[HL][0-9].*");

        // VCXO (Voltage Controlled Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^IQVCXO-[0-9].*");  // VCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^IQVCXO-H[0-9].*"); // High stability VCXO
        registry.addPattern(ComponentType.OSCILLATOR_VCXO_IQD, "^IQVCXO-[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_VCXO_IQD, "^IQVCXO-H[0-9].*");

        // OCXO (Oven Controlled Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^IQOCXO-[0-9].*");  // OCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^IQOCXO-H[0-9].*"); // High stability OCXO
        registry.addPattern(ComponentType.OSCILLATOR_OCXO_IQD, "^IQOCXO-[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_OCXO_IQD, "^IQOCXO-H[0-9].*");

        // Clock Modules and Timing Solutions
        registry.addPattern(ComponentType.IC, "^IQCM-[0-9].*");           // Clock modules
        registry.addPattern(ComponentType.IC, "^IQRTC-[0-9].*");          // RTC modules

        // Filters and Signal Conditioning
        registry.addPattern(ComponentType.IC, "^IQXF-[0-9].*");           // Crystal filters
        registry.addPattern(ComponentType.IC, "^IQSPF-[0-9].*");          // SAW filters
        registry.addPattern(ComponentType.IC, "^IQRB-[0-9].*");           // Resonator bandpass
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CRYSTAL,
            ComponentType.CRYSTAL_IQD,
            ComponentType.OSCILLATOR,
            ComponentType.OSCILLATOR_IQD,
            ComponentType.OSCILLATOR_TCXO_IQD,
            ComponentType.OSCILLATOR_VCXO_IQD,
            ComponentType.OSCILLATOR_OCXO_IQD,
            ComponentType.CRYSTAL_FILTER_IQD,
            ComponentType.RTC_MODULE_IQD
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Crystal packages
        if (upperMpn.startsWith("CFPX") || upperMpn.startsWith("XTALS")) {
            try {
                // Extract size code after the dash
                int dashIndex = upperMpn.indexOf('-');
                if (dashIndex > 0 && dashIndex + 4 <= upperMpn.length()) {
                    String sizeCode = upperMpn.substring(dashIndex + 1, dashIndex + 4);
                    return switch (sizeCode) {
                        case "016" -> "1.6 x 1.2mm";
                        case "020" -> "2.0 x 1.6mm";
                        case "025" -> "2.5 x 2.0mm";
                        case "032" -> "3.2 x 2.5mm";
                        case "050" -> "5.0 x 3.2mm";
                        case "070" -> "7.0 x 5.0mm";
                        default -> sizeCode;
                    };
                }
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Oscillator packages
        if (upperMpn.startsWith("IQXO") || upperMpn.startsWith("IQTX") ||
                upperMpn.startsWith("IQVCXO") || upperMpn.startsWith("IQOCXO")) {
            try {
                // Find package code in the part number
                String[] parts = upperMpn.split("-");
                if (parts.length >= 2) {
                    String pkgPart = parts[1];
                    // Package code is typically the first two characters
                    String pkgCode = pkgPart.substring(0, 2);
                    return switch (pkgCode) {
                        case "21" -> "2.0 x 1.6mm";
                        case "25" -> "2.5 x 2.0mm";
                        case "32" -> "3.2 x 2.5mm";
                        case "50" -> "5.0 x 3.2mm";
                        case "70" -> "7.0 x 5.0mm";
                        case "90" -> "9.0 x 7.0mm";
                        default -> pkgCode;
                    };
                }
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
        if (upperMpn.startsWith("LFXTAL")) return "Low Frequency Crystal";
        if (upperMpn.startsWith("CFPX")) return "Standard Crystal";
        if (upperMpn.startsWith("XTALS")) return "SMD Crystal";
        if (upperMpn.startsWith("XTAL")) return "Through-hole Crystal";

        // Oscillator series
        if (upperMpn.startsWith("IQXO")) {
            if (upperMpn.startsWith("IQXO-H")) return "High Frequency Oscillator";
            if (upperMpn.startsWith("IQXO-L")) return "Low Power Oscillator";
            return "Standard Oscillator";
        }

        // TCXO series
        if (upperMpn.startsWith("IQTX")) {
            if (upperMpn.startsWith("IQTX-H")) return "High Stability TCXO";
            if (upperMpn.startsWith("IQTX-L")) return "Low Power TCXO";
            return "Standard TCXO";
        }

        // VCXO series
        if (upperMpn.startsWith("IQVCXO")) {
            if (upperMpn.startsWith("IQVCXO-H")) return "High Stability VCXO";
            return "Standard VCXO";
        }

        // OCXO series
        if (upperMpn.startsWith("IQOCXO")) {
            if (upperMpn.startsWith("IQOCXO-H")) return "High Stability OCXO";
            return "Standard OCXO";
        }

        // Other series
        if (upperMpn.startsWith("IQCM")) return "Clock Module";
        if (upperMpn.startsWith("IQRTC")) return "RTC Module";
        if (upperMpn.startsWith("IQXF")) return "Crystal Filter";
        if (upperMpn.startsWith("IQSPF")) return "SAW Filter";
        if (upperMpn.startsWith("IQRB")) return "Resonator Bandpass";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are generally not replacements
        if (!series1.equals(series2)) {
            // Check for compatible series upgrades
            if (isCompatibleSeriesUpgrade(series1, series2)) {
                return checkCompatibility(mpn1, mpn2);
            }
            return false;
        }

        return checkCompatibility(mpn1, mpn2);
    }

    private boolean isCompatibleSeriesUpgrade(String series1, String series2) {
        // High stability versions can replace standard versions
        if (series1.startsWith("High Stability") &&
                series2.equals(series1.replace("High Stability ", ""))) {
            return true;
        }

        // Low power versions might be compatible with standard versions
        // (need to check specifications)
        if (series1.startsWith("Low Power") &&
                series2.equals(series1.replace("Low Power ", ""))) {
            return true;
        }

        return false;
    }

    private boolean checkCompatibility(String mpn1, String mpn2) {
        // Check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        if (!pkg1.equals(pkg2)) return false;

        try {
            // Check frequency compatibility
            String freq1 = extractFrequencyCode(mpn1);
            String freq2 = extractFrequencyCode(mpn2);
            if (!freq1.equals(freq2)) return false;

            // Check stability grade if applicable
            return checkStabilityCompatibility(mpn1, mpn2);
        } catch (Exception e) {
            return false;
        }
    }

    private String extractFrequencyCode(String mpn) {
        // Extract frequency from part number
        // Format varies by product type
        String[] parts = mpn.split("-");
        if (parts.length >= 3) {
            // Frequency is typically in the last or second-to-last part
            for (int i = parts.length - 1; i >= 0; i--) {
                if (parts[i].matches(".*\\d.*")) {
                    return parts[i].split("[A-Za-z]")[0]; // Get numeric part
                }
            }
        }
        return "";
    }

    private boolean checkStabilityCompatibility(String mpn1, String mpn2) {
        // Extract stability grades from part numbers
        String stability1 = extractStabilityGrade(mpn1);
        String stability2 = extractStabilityGrade(mpn2);

        // If stability grades are the same, they're compatible
        if (stability1.equals(stability2)) return true;

        // Better stability can replace worse stability
        try {
            double stab1 = parseStabilityValue(stability1);
            double stab2 = parseStabilityValue(stability2);
            return stab1 <= stab2; // Lower PPM is better stability
        } catch (NumberFormatException e) {
            // If we can't parse the stability values, require exact match
            return false;
        }
    }

    private String extractStabilityGrade(String mpn) {
        // Extract stability grade from part number
        // Format varies by product type
        String[] parts = mpn.split("-");
        for (String part : parts) {
            if (part.endsWith("PPM") || part.matches(".*\\d+\\.?\\d*.*")) {
                return part;
            }
        }
        return "";
    }

    private double parseStabilityValue(String stability) {
        // Parse stability value from string (e.g., "20PPM" -> 20.0)
        return Double.parseDouble(stability.replace("PPM", "").trim());
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}