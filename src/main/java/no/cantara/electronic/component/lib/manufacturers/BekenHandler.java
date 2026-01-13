package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Beken Corporation components.
 * <p>
 * Beken makes WiFi and BLE SoCs:
 * <ul>
 *   <li>BK7231 series - WiFi + BLE combo</li>
 *   <li>BK7251 - Audio + WiFi</li>
 *   <li>BK3431 - BLE only</li>
 *   <li>BK3432 - BLE 5.0</li>
 *   <li>BK7256 - WiFi6 + BLE5.2</li>
 * </ul>
 * <p>
 * MPN patterns:
 * <ul>
 *   <li>BK7xxx - WiFi/WiFi+BLE combo chips</li>
 *   <li>BK3xxx - BLE only chips</li>
 * </ul>
 * <p>
 * Package codes: QFN-32, QFN-40, QFN-48
 */
public class BekenHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // WiFi + BLE combo chips (BK7xxx series)
        registry.addPattern(ComponentType.IC, "^BK7[0-9]{3}.*");

        // BLE only chips (BK3xxx series)
        registry.addPattern(ComponentType.IC, "^BK3[0-9]{3}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Check for QFN-xx pattern anywhere in the string (handles BK7231-QFN-32)
        int qfnIndex = upperMpn.indexOf("QFN");
        if (qfnIndex >= 0) {
            // Extract from QFN to end or until non-package character
            String fromQfn = upperMpn.substring(qfnIndex);
            // Handle QFN-32, QFN32, QFN-40, QFN40, etc.
            if (fromQfn.matches("QFN-?[0-9]+.*")) {
                // Extract QFN plus optional dash plus digits
                StringBuilder pkg = new StringBuilder("QFN");
                int i = 3;
                if (i < fromQfn.length() && fromQfn.charAt(i) == '-') {
                    pkg.append('-');
                    i++;
                }
                while (i < fromQfn.length() && Character.isDigit(fromQfn.charAt(i))) {
                    pkg.append(fromQfn.charAt(i));
                    i++;
                }
                return pkg.toString();
            }
            return "QFN";
        }

        // Extract package code from suffix letters
        // Common Beken suffixes: N (QFN), T (Tape & Reel), etc.
        String basePattern = extractBasePart(upperMpn);
        if (basePattern.length() < upperMpn.length()) {
            String suffix = upperMpn.substring(basePattern.length());
            // Map common suffix letters to package types
            if (suffix.startsWith("N")) return "QFN";
            if (suffix.startsWith("Q")) return "QFN";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // BK7xxx series - WiFi/WiFi+BLE combo
        if (upperMpn.matches("^BK7[0-9]{3}.*")) {
            // Extract the series number (e.g., BK7231, BK7251, BK7256)
            if (upperMpn.length() >= 6) {
                return upperMpn.substring(0, 6);
            }
        }

        // BK3xxx series - BLE only
        if (upperMpn.matches("^BK3[0-9]{3}.*")) {
            // Extract the series number (e.g., BK3431, BK3432)
            if (upperMpn.length() >= 6) {
                return upperMpn.substring(0, 6);
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
        if (series1.isEmpty() || series2.isEmpty()) return false;
        if (!series1.equals(series2)) return false;

        // Same series - now check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If same series and both have empty or same package, they're replacements
        if (pkg1.isEmpty() && pkg2.isEmpty()) return true;
        if (pkg1.equals(pkg2)) return true;

        // If one has a package and the other doesn't, check if they're compatible
        // (e.g., BK7231 and BK7231N where N indicates QFN - same chip, just package specified)
        if (pkg1.isEmpty() || pkg2.isEmpty()) {
            // Same series with one unspecified package is typically compatible
            return true;
        }

        // Both have package codes - check if they're compatible QFN packages
        if (pkg1.startsWith("QFN") && pkg2.startsWith("QFN")) {
            // Only compatible if pin count is the same or unspecified
            String pinCount1 = pkg1.replaceAll("[^0-9]", "");
            String pinCount2 = pkg2.replaceAll("[^0-9]", "");
            if (pinCount1.isEmpty() || pinCount2.isEmpty()) return true;
            return pinCount1.equals(pinCount2);
        }

        return false;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Direct pattern matching for Beken parts
        if (type == ComponentType.IC) {
            return upperMpn.matches("^BK7[0-9]{3}.*") || upperMpn.matches("^BK3[0-9]{3}.*");
        }

        // Fall back to registry patterns
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    /**
     * Extracts the base part number without suffixes.
     */
    private String extractBasePart(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Find where the base part number ends
        // Base format: BKxxxx (2 letters + 4 digits)
        StringBuilder base = new StringBuilder();
        int digitCount = 0;

        for (int i = 0; i < mpn.length(); i++) {
            char c = mpn.charAt(i);
            if (i < 2 && Character.isLetter(c)) {
                base.append(c);
            } else if (i >= 2 && Character.isDigit(c) && digitCount < 4) {
                base.append(c);
                digitCount++;
            } else if (digitCount >= 4) {
                break;
            }
        }
        return base.toString();
    }

    /**
     * Returns the wireless type for a given MPN.
     * @param mpn the manufacturer part number
     * @return "WIFI_BLE" for BK7xxx, "BLE" for BK3xxx, or empty string
     */
    public String getWirelessType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        if (upperMpn.matches("^BK7[0-9]{3}.*")) {
            // BK7xxx series includes WiFi
            if (upperMpn.startsWith("BK7256")) return "WIFI6_BLE5";
            return "WIFI_BLE";
        }

        if (upperMpn.matches("^BK3[0-9]{3}.*")) {
            // BK3xxx series is BLE only
            if (upperMpn.startsWith("BK3432")) return "BLE5";
            return "BLE";
        }

        return "";
    }

    /**
     * Checks if the MPN represents a WiFi-capable chip.
     */
    public boolean isWifiCapable(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;
        String upperMpn = mpn.toUpperCase();
        return upperMpn.matches("^BK7[0-9]{3}.*");
    }

    /**
     * Checks if the MPN represents a BLE-capable chip.
     */
    public boolean isBleCapable(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;
        String upperMpn = mpn.toUpperCase();
        // All Beken chips (BK7xxx and BK3xxx) support BLE
        return upperMpn.matches("^BK7[0-9]{3}.*") || upperMpn.matches("^BK3[0-9]{3}.*");
    }
}
