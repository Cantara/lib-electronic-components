package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handler for KEMET Electronics components.
 * Supports MLCC (Ceramic), Tantalum, and Aluminum Electrolytic capacitors.
 */
public class KemetHandler implements ManufacturerHandler {

    // Common size codes for ceramic capacitors
    private static final Map<String, String> SIZE_CODES = new HashMap<>();
    static {
        SIZE_CODES.put("0402", "01005");
        SIZE_CODES.put("0603", "0201");
        SIZE_CODES.put("0805", "0202");
        SIZE_CODES.put("1206", "0503");
        SIZE_CODES.put("1210", "0504");
        SIZE_CODES.put("1812", "0505");
        SIZE_CODES.put("2220", "0506");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.CAPACITOR,
            ComponentType.CAPACITOR_CERAMIC_KEMET,
            ComponentType.CAPACITOR_TANTALUM_KEMET,
            ComponentType.CAPACITOR_ALUMINUM_KEMET,
            ComponentType.CAPACITOR_FILM_KEMET,
            ComponentType.EMI_FILTER_KEMET
        );
    }

    // Compatible package groups for replacements
    private static final Set<String> MLCC_SIZES = Set.of(
            "0402", "0603", "0805", "1206", "1210", "1812", "2220"
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Ceramic Capacitors (MLCC)
        // Base patterns for all ceramic capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^C[0-9]{4}[A-Z0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_CERAMIC_KEMET, "^C[0-9]{4}[A-Z0-9].*");

        // Size-specific patterns
        for (String size : MLCC_SIZES) {
            registry.addPattern(ComponentType.CAPACITOR_CERAMIC_KEMET, "^C" + size + "[A-Z0-9].*");
        }

        // Tantalum Capacitors
        // Standard tantalum series
        registry.addPattern(ComponentType.CAPACITOR, "^T[0-9]{3}[A-Z0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_TANTALUM_KEMET, "^T[0-9]{3}[A-Z0-9].*");
        // Special tantalum series
        registry.addPattern(ComponentType.CAPACITOR, "^T[A-Z][0-9]{2}[A-Z0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_TANTALUM_KEMET, "^T[A-Z][0-9]{2}[A-Z0-9].*");

        // Aluminum Electrolytic Capacitors
        // Standard aluminum series
        registry.addPattern(ComponentType.CAPACITOR, "^A[0-9]{3}[A-Z0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ALUMINUM_KEMET, "^A[0-9]{3}[A-Z0-9].*");
        // Special aluminum series
        registry.addPattern(ComponentType.CAPACITOR, "^A[A-Z][0-9]{2}[A-Z0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_ALUMINUM_KEMET, "^A[A-Z][0-9]{2}[A-Z0-9].*");

        // Film Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^PHE[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_KEMET, "^PHE[0-9].*");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct matching for ceramic capacitors
        if ((type == ComponentType.CAPACITOR || type == ComponentType.CAPACITOR_CERAMIC_KEMET)
                && upperMpn.startsWith("C")) {
            if (upperMpn.matches("^C[0-9]{4}[A-Z0-9].*")) {
                String sizeCode = upperMpn.substring(1, 5);
                return MLCC_SIZES.contains(sizeCode);
            }
        }

        // Direct matching for tantalum capacitors
        if ((type == ComponentType.CAPACITOR || type == ComponentType.CAPACITOR_TANTALUM_KEMET)
                && upperMpn.startsWith("T")) {
            return upperMpn.matches("^T(?:[0-9]{3}|[A-Z][0-9]{2})[A-Z0-9].*");
        }

        // Direct matching for aluminum capacitors
        if ((type == ComponentType.CAPACITOR || type == ComponentType.CAPACITOR_ALUMINUM_KEMET)
                && upperMpn.startsWith("A")) {
            return upperMpn.matches("^A(?:[0-9]{3}|[A-Z][0-9]{2})[A-Z0-9].*");
        }

        // Direct matching for film capacitors
        if ((type == ComponentType.CAPACITOR || type == ComponentType.CAPACITOR_FILM_KEMET)
                && upperMpn.startsWith("PHE")) {
            return upperMpn.matches("^PHE[0-9].*");
        }

        // Use handler-specific patterns for other matches (avoid cross-handler false matches)
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Ceramic capacitor size codes
        if (mpn.startsWith("C")) {
            try {
                String sizeCode = mpn.substring(1, 5);
                // Return mapped standard size code if available
                return SIZE_CODES.getOrDefault(sizeCode, sizeCode);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Tantalum and Aluminum case codes
        if (mpn.startsWith("T") || mpn.startsWith("A")) {
            try {
                String caseCode = mpn.substring(0, 3);
                // Add logic for specific case code mappings if needed
                return caseCode;
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Film capacitor packages
        if (mpn.startsWith("PHE")) {
            try {
                return mpn.substring(0, 4); // Include series identifier
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Ceramic capacitors
        if (mpn.startsWith("C")) {
            try {
                // Series includes the size code for ceramics
                return mpn.substring(0, 5);
            } catch (IndexOutOfBoundsException e) {
                return "C";
            }
        }

        // Tantalum capacitors
        if (mpn.startsWith("T")) {
            try {
                // Extract the complete series code (includes case size)
                return mpn.substring(0, 3);
            } catch (IndexOutOfBoundsException e) {
                return "T";
            }
        }

        // Aluminum capacitors
        if (mpn.startsWith("A")) {
            try {
                // Extract the complete series code (includes case size)
                return mpn.substring(0, 3);
            } catch (IndexOutOfBoundsException e) {
                return "A";
            }
        }

        // Film capacitors
        if (mpn.startsWith("PHE")) {
            try {
                return mpn.substring(0, 4);
            } catch (IndexOutOfBoundsException e) {
                return "PHE";
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series for base compatibility
        if (!series1.equals(series2)) return false;

        // Ceramic capacitors
        if (mpn1.startsWith("C") && mpn2.startsWith("C")) {
            String size1 = extractPackageCode(mpn1);
            String size2 = extractPackageCode(mpn2);

            // Must be same size for ceramics
            if (!size1.equals(size2)) return false;

            // Additional validation could be added here
            // (voltage rating, temperature coefficient, etc.)
            return true;
        }

        // Tantalum and Aluminum - series includes case size
        if ((mpn1.startsWith("T") && mpn2.startsWith("T")) ||
                (mpn1.startsWith("A") && mpn2.startsWith("A"))) {
            // Series match is sufficient as it includes case size
            return true;
        }

        // Film capacitors
        if (mpn1.startsWith("PHE") && mpn2.startsWith("PHE")) {
            // Series match is sufficient for film capacitors
            return true;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as KEMET types are handled through standard ComponentType
        return Collections.emptySet();
    }
}