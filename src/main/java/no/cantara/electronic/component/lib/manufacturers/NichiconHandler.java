package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;

/**
 * Handler for Nichicon components (primarily capacitors)
 */
public class NichiconHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Aluminum Electrolytic Capacitors
        // Standard series
        registry.addPattern(ComponentType.CAPACITOR, "^UUD[0-9].*");          // Standard grade
        registry.addPattern(ComponentType.CAPACITOR, "^UUE[0-9].*");          // Standard grade, higher voltage
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^UUD[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^UUE[0-9].*");

        // High temperature series
        registry.addPattern(ComponentType.CAPACITOR, "^UHS[0-9].*");          // 125°C high temp
        registry.addPattern(ComponentType.CAPACITOR, "^UHE[0-9].*");          // 135°C high temp
        registry.addPattern(ComponentType.CAPACITOR, "^UHW[0-9].*");          // 105°C high temp
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^UH[SWE][0-9].*");

        // Long life series
        registry.addPattern(ComponentType.CAPACITOR, "^UES[0-9].*");          // Long life, standard grade
        registry.addPattern(ComponentType.CAPACITOR, "^UEW[0-9].*");          // Long life, high ripple
        registry.addPattern(ComponentType.CAPACITOR, "^UKL[0-9].*");          // Extra long life
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^UE[SW][0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^UKL[0-9].*");

        // Low impedance series
        registry.addPattern(ComponentType.CAPACITOR, "^UPW[0-9].*");          // Low impedance
        registry.addPattern(ComponentType.CAPACITOR, "^UPS[0-9].*");          // Ultra low impedance
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^UP[WS][0-9].*");

        // Polymer series
        registry.addPattern(ComponentType.CAPACITOR, "^PCJ[0-9].*");          // Polymer, standard
        registry.addPattern(ComponentType.CAPACITOR, "^PCS[0-9].*");          // Polymer, low profile
        registry.addPattern(ComponentType.CAPACITOR, "^PCR[0-9].*");          // Polymer, high reliability
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^PC[JSR][0-9].*");

        // Miniature series
        registry.addPattern(ComponentType.CAPACITOR, "^UMA[0-9].*");          // Miniature, general purpose
        registry.addPattern(ComponentType.CAPACITOR, "^UMD[0-9].*");          // Miniature, high temp
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^UM[AD][0-9].*");

        // Photo flash series
        registry.addPattern(ComponentType.CAPACITOR, "^PF[0-9].*");           // Photo flash
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^PF[0-9].*");

        // Super capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^JJD[0-9].*");          // Standard EDLC
        registry.addPattern(ComponentType.CAPACITOR, "^JJS[0-9].*");          // High power EDLC
        registry.addPattern(ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON, "^JJ[DS][0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CAPACITOR,
            ComponentType.CAPACITOR_ELECTROLYTIC_NICHICON,
            ComponentType.CAPACITOR_FILM_NICHICON,
            ComponentType.SUPERCAP_NICHICON
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Standard aluminum electrolytic cases
        if (upperMpn.startsWith("U") || upperMpn.startsWith("PC")) {
            // Look for case size after series code (typically 4th or 5th character)
            try {
                int startIndex = upperMpn.startsWith("PC") ? 3 : 4;
                String caseCode = upperMpn.substring(startIndex, startIndex + 2);

                // Common case codes for radial lead types
                return switch (caseCode) {
                    case "04" -> "4x7mm";
                    case "05" -> "5x7mm";
                    case "06" -> "6.3x7mm";
                    case "08" -> "8x10mm";
                    case "10" -> "10x12.5mm";
                    case "12" -> "12.5x15mm";
                    case "16" -> "16x15mm";
                    case "18" -> "18x20mm";
                    case "22" -> "22x25mm";
                    case "25" -> "25x25mm";
                    case "30" -> "30x30mm";
                    case "35" -> "35x40mm";
                    default -> caseCode + "mm";
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Super capacitor cases
        if (upperMpn.startsWith("JJ")) {
            try {
                String caseCode = upperMpn.substring(3, 5);
                return "ϕ" + caseCode + "mm";  // Using phi symbol for diameter
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

        // Standard series
        if (upperMpn.startsWith("UUD")) return "Standard Grade";
        if (upperMpn.startsWith("UUE")) return "Standard High Voltage";

        // High temperature series
        if (upperMpn.startsWith("UHS")) return "125°C High Temp";
        if (upperMpn.startsWith("UHE")) return "135°C High Temp";
        if (upperMpn.startsWith("UHW")) return "105°C High Temp";

        // Long life series
        if (upperMpn.startsWith("UES")) return "Long Life Standard";
        if (upperMpn.startsWith("UEW")) return "Long Life High Ripple";
        if (upperMpn.startsWith("UKL")) return "Extra Long Life";

        // Low impedance series
        if (upperMpn.startsWith("UPW")) return "Low Impedance";
        if (upperMpn.startsWith("UPS")) return "Ultra Low Impedance";

        // Polymer series
        if (upperMpn.startsWith("PCJ")) return "Polymer Standard";
        if (upperMpn.startsWith("PCS")) return "Polymer Low Profile";
        if (upperMpn.startsWith("PCR")) return "Polymer High Reliability";

        // Miniature series
        if (upperMpn.startsWith("UMA")) return "Miniature General";
        if (upperMpn.startsWith("UMD")) return "Miniature High Temp";

        // Photo flash series
        if (upperMpn.startsWith("PF")) return "Photo Flash";

        // Super capacitors
        if (upperMpn.startsWith("JJD")) return "Standard EDLC";
        if (upperMpn.startsWith("JJS")) return "High Power EDLC";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // Same series, case size, and voltage rating might be compatible
        String case1 = extractPackageCode(mpn1);
        String case2 = extractPackageCode(mpn2);
        if (!case1.equals(case2)) return false;

        // For some series, higher temperature rating can replace lower
        if (mpn1.startsWith("UH") && mpn2.startsWith("UH")) {
            // 135°C can replace 125°C and 105°C
            if (mpn1.startsWith("UHE")) return true;
            if (mpn2.startsWith("UHE")) return true;
            // 125°C can replace 105°C
            if (mpn1.startsWith("UHS") && mpn2.startsWith("UHW")) return true;
            if (mpn2.startsWith("UHS") && mpn1.startsWith("UHW")) return true;
        }

        // Long life series can typically replace standard series
        if ((mpn1.startsWith("UES") || mpn1.startsWith("UEW")) &&
                (mpn2.startsWith("UUD") || mpn2.startsWith("UUE"))) {
            return true;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}