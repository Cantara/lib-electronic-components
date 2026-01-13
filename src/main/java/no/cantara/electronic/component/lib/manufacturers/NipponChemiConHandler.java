package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.Set;

/**
 * Handler for Nippon Chemi-Con components (primarily aluminum electrolytic capacitors).
 *
 * Nippon Chemi-Con is a leading manufacturer of aluminum electrolytic capacitors,
 * conductive polymer capacitors, and supercapacitors (EDLC).
 *
 * MPN Structure:
 * - Prefix: EK = Aluminum electrolytic, M = Miniature series
 * - Series code: 2-3 letters (KY, KZE, KXG, KXJ, MVY, MVZ, etc.)
 * - Voltage code: 3 digits (500=50V, 250=25V, 160=16V, 100=10V, 063=6.3V)
 * - Capacitance code: 3 digits + multiplier (101=100uF, 471=470uF, 102=1000uF)
 *
 * Example: EKYE500ELL101MJC5S
 * - EK = Aluminum electrolytic
 * - YE = KY series (using Y as internal code)
 * - 500 = 50V
 * - ELL = Internal code
 * - 101 = 100uF
 * - MJC5S = Case size and termination
 */
public class NipponChemiConHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // KY series - Low impedance, high ripple current
        // Format 1: EKYE500... (EK + YE + 3-digit voltage)
        // Format 2: EKY1C... (EK + Y + digit + letter voltage code)
        registry.addPattern(ComponentType.CAPACITOR, "^EKY[A-Z][0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EKYE[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EKY[0-9][A-Z].*");   // Alternate voltage format

        // KZE series - Conductive polymer hybrid
        registry.addPattern(ComponentType.CAPACITOR, "^EKZ[A-Z][0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EKZE[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EKZ[0-9][A-Z].*");   // Alternate voltage format

        // KXG series - High temperature (105C), long life
        registry.addPattern(ComponentType.CAPACITOR, "^EKXG[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EKXG[0-9][A-Z].*"); // Alternate voltage format

        // KXJ series - High temperature (105C), low impedance
        registry.addPattern(ComponentType.CAPACITOR, "^EKXJ[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EKXJ[0-9][A-Z].*"); // Alternate voltage format

        // MVY series - Miniature, low impedance
        registry.addPattern(ComponentType.CAPACITOR, "^MVY[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EMVY[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^MVY[0-9][A-Z].*");  // Alternate voltage format

        // MVZ series - Miniature, standard
        registry.addPattern(ComponentType.CAPACITOR, "^MVZ[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EMVZ[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^MVZ[0-9][A-Z].*");  // Alternate voltage format

        // KMG series - General purpose, miniature
        registry.addPattern(ComponentType.CAPACITOR, "^EKMG[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^KMG[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^KMG[0-9][A-Z].*");  // Alternate voltage format

        // KMH series - General purpose
        registry.addPattern(ComponentType.CAPACITOR, "^EKMH[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^KMH[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^KMH[0-9][A-Z].*");  // Alternate voltage format

        // SMG/SMH series - Surface mount
        registry.addPattern(ComponentType.CAPACITOR, "^ESMG[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^ESMH[0-9]{3}.*");

        // PSC/PSE series - Conductive polymer solid
        registry.addPattern(ComponentType.CAPACITOR, "^PSC[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^PSE[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^PSC[0-9][A-Z].*");  // Alternate voltage format
        registry.addPattern(ComponentType.CAPACITOR, "^PSE[0-9][A-Z].*");  // Alternate voltage format

        // GXE series - Ultra low ESR
        registry.addPattern(ComponentType.CAPACITOR, "^GXE[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^GXE[0-9][A-Z].*");  // Alternate voltage format

        // Generic patterns for aluminum electrolytic series
        // Format 1: EK + 2 letters + 3 digits (e.g., EKAB500...)
        // Format 2: EK + 2 letters + digit + letter (e.g., EKAB1C...)
        registry.addPattern(ComponentType.CAPACITOR, "^EK[A-Z]{2}[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^EK[A-Z]{2}[0-9][A-Z].*");
        registry.addPattern(ComponentType.CAPACITOR, "^M[A-Z]{2}[0-9]{3}.*");
        registry.addPattern(ComponentType.CAPACITOR, "^M[A-Z]{2}[0-9][A-Z].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CAPACITOR,
            ComponentType.IC
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Look for case size code in the MPN
        // Nippon Chemi-Con uses suffixes like MJC5S, MHD, etc.
        // Case sizes are often encoded in the suffix

        // SMD case codes (common suffixes)
        if (upperMpn.contains("JC5")) return "5x5.3mm";
        if (upperMpn.contains("JC6")) return "6.3x5.5mm";
        if (upperMpn.contains("JC8")) return "8x6.2mm";
        if (upperMpn.contains("JC10")) return "10x10.2mm";
        if (upperMpn.contains("JH5")) return "5x5.8mm";
        if (upperMpn.contains("JH6")) return "6.3x5.8mm";
        if (upperMpn.contains("JH8")) return "8x6.5mm";

        // Radial lead case codes
        if (upperMpn.contains("MHD")) return "5x11mm";
        if (upperMpn.contains("MDD")) return "4x7mm";
        if (upperMpn.contains("MLD")) return "6.3x11mm";
        if (upperMpn.contains("MPD")) return "8x11.5mm";
        if (upperMpn.contains("MNL")) return "10x12.5mm";
        if (upperMpn.contains("MQL")) return "12.5x15mm";

        // Try to extract from position-based encoding
        // For patterns like EKYE500ELL101MJC5S
        int lastDigitIdx = findLastDigitBeforeSuffix(upperMpn);
        if (lastDigitIdx > 0 && lastDigitIdx < upperMpn.length() - 2) {
            String suffix = upperMpn.substring(lastDigitIdx + 1);
            if (suffix.startsWith("M")) {
                return suffix;
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // KY series - Low impedance
        if (upperMpn.startsWith("EKYE") || upperMpn.startsWith("EKY")) {
            return "KY Low Impedance";
        }

        // KZE series - Conductive polymer hybrid
        if (upperMpn.startsWith("EKZE") || upperMpn.startsWith("EKZ")) {
            return "KZE Conductive Polymer";
        }

        // KXG series - High temperature long life
        if (upperMpn.startsWith("EKXG")) {
            return "KXG High Temp Long Life";
        }

        // KXJ series - High temperature low impedance
        if (upperMpn.startsWith("EKXJ")) {
            return "KXJ High Temp Low Impedance";
        }

        // MVY series - Miniature low impedance
        if (upperMpn.startsWith("MVY") || upperMpn.startsWith("EMVY")) {
            return "MVY Miniature Low Impedance";
        }

        // MVZ series - Miniature standard
        if (upperMpn.startsWith("MVZ") || upperMpn.startsWith("EMVZ")) {
            return "MVZ Miniature Standard";
        }

        // KMG series - General purpose miniature
        if (upperMpn.startsWith("EKMG") || upperMpn.startsWith("KMG")) {
            return "KMG General Purpose Mini";
        }

        // KMH series - General purpose
        if (upperMpn.startsWith("EKMH") || upperMpn.startsWith("KMH")) {
            return "KMH General Purpose";
        }

        // SMG/SMH series - Surface mount
        if (upperMpn.startsWith("ESMG")) {
            return "SMG Surface Mount";
        }
        if (upperMpn.startsWith("ESMH")) {
            return "SMH Surface Mount";
        }

        // PSC/PSE series - Conductive polymer solid
        if (upperMpn.startsWith("PSC")) {
            return "PSC Polymer Solid";
        }
        if (upperMpn.startsWith("PSE")) {
            return "PSE Polymer Solid";
        }

        // GXE series - Ultra low ESR
        if (upperMpn.startsWith("GXE")) {
            return "GXE Ultra Low ESR";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are generally not replacements
        if (!series1.equals(series2) || series1.isEmpty()) return false;

        // Same series with matching package could be compatible
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If packages don't match, not a drop-in replacement
        if (!pkg1.equals(pkg2) || pkg1.isEmpty()) return false;

        // Extract voltage from MPN (3 digits after series code)
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);

        // Higher voltage rating can replace lower (within same series)
        if (!voltage1.isEmpty() && !voltage2.isEmpty()) {
            try {
                int v1 = parseVoltageCode(voltage1);
                int v2 = parseVoltageCode(voltage2);
                // Higher voltage can replace lower
                if (v1 >= v2) return true;
            } catch (NumberFormatException e) {
                // Fall through to false
            }
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Extracts the voltage code from an MPN.
     * Nippon Chemi-Con uses 3-digit voltage codes: 500=50V, 250=25V, 160=16V, etc.
     */
    private String extractVoltage(String mpn) {
        if (mpn == null || mpn.length() < 7) return "";
        String upperMpn = mpn.toUpperCase();

        // Find the voltage code position (after series prefix)
        int startIdx = -1;
        if (upperMpn.startsWith("EKYE") || upperMpn.startsWith("EKZE") ||
            upperMpn.startsWith("EKXG") || upperMpn.startsWith("EKXJ")) {
            startIdx = 4;
        } else if (upperMpn.startsWith("EKY") || upperMpn.startsWith("EKZ")) {
            startIdx = 3;
        } else if (upperMpn.startsWith("EKMG") || upperMpn.startsWith("EKMH") ||
                   upperMpn.startsWith("ESMG") || upperMpn.startsWith("ESMH") ||
                   upperMpn.startsWith("EMVY") || upperMpn.startsWith("EMVZ")) {
            startIdx = 4;
        } else if (upperMpn.startsWith("MVY") || upperMpn.startsWith("MVZ") ||
                   upperMpn.startsWith("KMG") || upperMpn.startsWith("KMH") ||
                   upperMpn.startsWith("PSC") || upperMpn.startsWith("PSE") ||
                   upperMpn.startsWith("GXE")) {
            startIdx = 3;
        }

        if (startIdx > 0 && startIdx + 3 <= upperMpn.length()) {
            String voltageCode = upperMpn.substring(startIdx, startIdx + 3);
            if (voltageCode.matches("[0-9]{3}")) {
                return voltageCode;
            }
        }

        return "";
    }

    /**
     * Parses a 3-digit voltage code to actual voltage value.
     * Example: 500 -> 50, 250 -> 25, 160 -> 16
     */
    private int parseVoltageCode(String code) {
        if (code == null || code.length() != 3) {
            throw new NumberFormatException("Invalid voltage code: " + code);
        }
        // The code is typically voltage * 10 (e.g., 500 = 50V, 250 = 25V)
        int rawValue = Integer.parseInt(code);
        return rawValue / 10;
    }

    /**
     * Finds the position of the last digit before the case size suffix.
     */
    private int findLastDigitBeforeSuffix(String mpn) {
        for (int i = mpn.length() - 1; i >= 0; i--) {
            char c = mpn.charAt(i);
            if (Character.isDigit(c)) {
                return i;
            }
        }
        return -1;
    }
}
