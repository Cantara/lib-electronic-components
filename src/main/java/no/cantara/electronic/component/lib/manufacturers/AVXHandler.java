package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for AVX Corporation components
 */
public class AVXHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Tantalum Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^TAJ[A-Z][0-9].*");     // Standard MnO2
        registry.addPattern(ComponentType.CAPACITOR, "^TPS[A-Z][0-9].*");     // Polymer
        registry.addPattern(ComponentType.CAPACITOR, "^TPM[A-Z][0-9].*");     // Multianode polymer
        registry.addPattern(ComponentType.CAPACITOR, "^TRJ[A-Z][0-9].*");     // High reliability
        registry.addPattern(ComponentType.CAPACITOR, "^TCJ[A-Z][0-9].*");     // Commercial grade
        registry.addPattern(ComponentType.CAPACITOR_TANTALUM_AVX, "^TA[A-Z][0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_TANTALUM_AVX, "^TP[A-Z][0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_TANTALUM_AVX, "^TR[A-Z][0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_TANTALUM_AVX, "^TC[A-Z][0-9].*");

        // Ceramic Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^08[0-9].*");           // Standard MLCC
        registry.addPattern(ComponentType.CAPACITOR, "^12[0-9].*");           // High voltage MLCC
        registry.addPattern(ComponentType.CAPACITOR, "^21[0-9].*");           // RF/Microwave
        registry.addPattern(ComponentType.CAPACITOR, "^25[0-9].*");           // Medical grade
        registry.addPattern(ComponentType.CAPACITOR, "^ML[0-9].*");           // Low inductance

        // Film Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^FA[0-9].*");           // Stacked film
        registry.addPattern(ComponentType.CAPACITOR, "^FB[0-9].*");           // Box film
        registry.addPattern(ComponentType.CAPACITOR, "^FP[0-9].*");           // Pulse film

        // SuperCapacitors
        registry.addPattern(ComponentType.CAPACITOR, "^SCC[0-9].*");          // Cylindrical
        registry.addPattern(ComponentType.CAPACITOR, "^SCM[0-9].*");          // Module type

        // RF Components
        registry.addPattern(ComponentType.IC, "^BP[0-9].*");                  // Band pass filters
        registry.addPattern(ComponentType.IC, "^LP[0-9].*");                  // Low pass filters
        registry.addPattern(ComponentType.IC, "^HP[0-9].*");                  // High pass filters
        registry.addPattern(ComponentType.IC, "^MLO[0-9].*");                // RF inductors

        // Circuit Protection
        registry.addPattern(ComponentType.IC, "^VC[0-9].*");                 // Varistors
        registry.addPattern(ComponentType.IC, "^TC[0-9].*");                 // Transient suppressors

        // Connectors
        registry.addPattern(ComponentType.IC, "^00[0-9].*");                 // Board-to-board
        registry.addPattern(ComponentType.IC, "^70[0-9].*");                 // Card edge
        registry.addPattern(ComponentType.IC, "^90[0-9].*");                 // RF connectors
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CAPACITOR);
        types.add(ComponentType.CAPACITOR_TANTALUM_AVX);
        types.add(ComponentType.CAPACITOR_CERAMIC_AVX);
        types.add(ComponentType.CAPACITOR_FILM_AVX);
        types.add(ComponentType.CAPACITOR_POLYMER_AVX);
        types.add(ComponentType.SUPERCAP_AVX);
        types.add(ComponentType.FILTER_AVX);
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Tantalum case codes
        if (upperMpn.startsWith("T")) {
            // Look for standard tantalum case codes (A-E, V-Y)
            String[] caseCodes = {"A", "B", "C", "D", "E", "V", "W", "X", "Y"};
            for (String caseCode : caseCodes) {
                if (upperMpn.length() > 3 && caseCode.equals(String.valueOf(upperMpn.charAt(3)))) {
                    return switch (caseCode) {
                        case "A" -> "EIA-3216-12/Case A";
                        case "B" -> "EIA-3528-12/Case B";
                        case "C" -> "EIA-6032-15/Case C";
                        case "D" -> "EIA-7343-20/Case D";
                        case "E" -> "EIA-7343-31/Case E";
                        case "V" -> "EIA-7343-43/Case V";
                        case "W" -> "EIA-7360-38/Case W";
                        case "X" -> "EIA-7361-43/Case X";
                        case "Y" -> "EIA-7361-43/Case Y";
                        default -> "Case " + caseCode;
                    };
                }
            }
        }

        // Ceramic capacitor sizes
        if (upperMpn.startsWith("08") || upperMpn.startsWith("12")) {
            try {
                String sizeCode = upperMpn.substring(2, 4);
                return switch (sizeCode) {
                    case "05" -> "0402/1005M";
                    case "06" -> "0603/1608M";
                    case "08" -> "0805/2012M";
                    case "12" -> "1206/3216M";
                    case "21" -> "2220/5750M";
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

        // Tantalum series
        if (upperMpn.startsWith("TAJ")) return "Standard MnO2";
        if (upperMpn.startsWith("TPS")) return "Polymer";
        if (upperMpn.startsWith("TPM")) return "Multianode Polymer";
        if (upperMpn.startsWith("TRJ")) return "High Reliability";
        if (upperMpn.startsWith("TCJ")) return "Commercial Grade";

        // Ceramic series
        if (upperMpn.startsWith("08")) return "Standard MLCC";
        if (upperMpn.startsWith("12")) return "High Voltage MLCC";
        if (upperMpn.startsWith("21")) return "RF/Microwave";
        if (upperMpn.startsWith("25")) return "Medical Grade";
        if (upperMpn.startsWith("ML")) return "Low Inductance";

        // Film series
        if (upperMpn.startsWith("FA")) return "Stacked Film";
        if (upperMpn.startsWith("FB")) return "Box Film";
        if (upperMpn.startsWith("FP")) return "Pulse Film";

        // SuperCapacitor series
        if (upperMpn.startsWith("SCC")) return "Cylindrical SuperCap";
        if (upperMpn.startsWith("SCM")) return "Module SuperCap";

        // RF series
        if (upperMpn.startsWith("BP")) return "Band Pass Filter";
        if (upperMpn.startsWith("LP")) return "Low Pass Filter";
        if (upperMpn.startsWith("HP")) return "High Pass Filter";
        if (upperMpn.startsWith("MLO")) return "RF Inductor";

        // Protection series
        if (upperMpn.startsWith("VC")) return "Varistor";
        if (upperMpn.startsWith("TC")) return "Transient Suppressor";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // For tantalum capacitors, check case size and voltage rating
        if (mpn1.startsWith("T") && mpn2.startsWith("T")) {
            String case1 = extractPackageCode(mpn1);
            String case2 = extractPackageCode(mpn2);
            if (!case1.equals(case2)) return false;

            // TPS (polymer) can often replace TAJ (MnO2) in same case size
            if (mpn1.startsWith("TPS") && mpn2.startsWith("TAJ")) return true;
            if (mpn2.startsWith("TPS") && mpn1.startsWith("TAJ")) return true;

            // High reliability series can replace standard series
            if (mpn1.startsWith("TRJ") && mpn2.startsWith("TAJ")) return true;
        }

        // For ceramic capacitors, check size and voltage rating
        if ((mpn1.startsWith("08") || mpn1.startsWith("12")) &&
                (mpn2.startsWith("08") || mpn2.startsWith("12"))) {
            String size1 = extractPackageCode(mpn1);
            String size2 = extractPackageCode(mpn2);
            return size1.equals(size2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}