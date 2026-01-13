package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for WIMA premium film capacitors.
 *
 * WIMA is a German manufacturer specializing in high-quality film capacitors
 * for audio, pulse, and general applications.
 *
 * Series naming convention (first 3 letters):
 * - First letter: Construction type
 *   - M = Metallized (thin metal layer on film)
 *   - F = Film/Foil (discrete metal foil electrodes)
 * - Second letter: K = Kunststoff (plastic film capacitor)
 * - Third letter: Dielectric material
 *   - S = Polyester (PET) - good general purpose
 *   - P = Polypropylene (PP) - lower losses, audio applications
 *   - C = Polycarbonate (discontinued)
 *
 * Common Series:
 * - MKS: Metallized Polyester - general purpose, compact
 * - MKP: Metallized Polypropylene - audio, snubber, pulse
 * - FKS: Foil Polyester - precision, low loss
 * - FKP: Foil Polypropylene - highest quality, audio
 * - FKC: Foil Polycarbonate (legacy)
 *
 * MPN Structure Examples:
 * - MKS4-100/63/10: MKS series, size 4, 100nF, 63V, 10% tolerance
 * - MKP10-0.01/1000/10: MKP10 series, 10nF, 1000V, 10% tolerance
 * - FKP2-330P/100/5: FKP2 series, 330pF, 100V, 5% tolerance
 *
 * Size codes (number after series):
 * - 2: 5mm lead spacing
 * - 4: 7.5mm lead spacing
 * - 6: 10mm lead spacing
 * - 10: 15mm lead spacing (MKP10 pulse capacitors)
 *
 * Sources:
 * - https://www.wima.de/en/our-product-range/pulse-capacitors/fkp-1/partnumber-system/
 * - https://www.wima.de/en/our-product-range/smd-capacitors/smd-pet/bestellnummer-systematik/
 */
public class WIMAHandler implements ManufacturerHandler {

    // Main series pattern: MKS, MKP, FKS, FKP, FKC followed by size code
    private static final Pattern WIMA_PATTERN = Pattern.compile(
            "^(MKS|MKP|FKS|FKP|FKC)(\\d+)[-_]?(.*)$", Pattern.CASE_INSENSITIVE);

    // SMD series pattern: SMDP, SMDM (SMD PET/PP)
    private static final Pattern SMD_PATTERN = Pattern.compile(
            "^SMD[PM]\\d+.*$", Pattern.CASE_INSENSITIVE);

    // Value extraction pattern: handles formats like 100, 0.01, 330P, 100N, 1U
    private static final Pattern VALUE_PATTERN = Pattern.compile(
            "([\\d.]+)([PNUu]?)[Ff]?");

    // Series to dielectric mapping
    private static final Map<String, String> SERIES_DIELECTRIC = Map.of(
            "MKS", "PET",    // Metallized Polyester
            "MKP", "PP",     // Metallized Polypropylene
            "FKS", "PET",    // Foil Polyester
            "FKP", "PP",     // Foil Polypropylene
            "FKC", "PC"      // Foil Polycarbonate (legacy)
    );

    // Size code to lead spacing mapping (in mm)
    private static final Map<String, String> SIZE_TO_PITCH = Map.of(
            "2", "5mm",
            "3", "7.5mm",
            "4", "7.5mm",
            "6", "10mm",
            "10", "15mm",
            "20", "22.5mm"
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Register patterns for film capacitors
        // MKS series - Metallized Polyester
        registry.addPattern(ComponentType.CAPACITOR, "^MKS\\d+.*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_WIMA, "^MKS\\d+.*");

        // MKP series - Metallized Polypropylene
        registry.addPattern(ComponentType.CAPACITOR, "^MKP\\d+.*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_WIMA, "^MKP\\d+.*");

        // FKS series - Foil Polyester
        registry.addPattern(ComponentType.CAPACITOR, "^FKS\\d+.*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_WIMA, "^FKS\\d+.*");

        // FKP series - Foil Polypropylene
        registry.addPattern(ComponentType.CAPACITOR, "^FKP\\d+.*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_WIMA, "^FKP\\d+.*");

        // FKC series - Foil Polycarbonate (legacy)
        registry.addPattern(ComponentType.CAPACITOR, "^FKC\\d+.*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_WIMA, "^FKC\\d+.*");

        // SMD series
        registry.addPattern(ComponentType.CAPACITOR, "^SMD[PM]\\d+.*");
        registry.addPattern(ComponentType.CAPACITOR_FILM_WIMA, "^SMD[PM]\\d+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.CAPACITOR,
                ComponentType.CAPACITOR_FILM_WIMA
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // Check for WIMA film capacitor patterns
        if (type == ComponentType.CAPACITOR || type == ComponentType.CAPACITOR_FILM_WIMA) {
            // Check main series: MKS, MKP, FKS, FKP, FKC
            if (upperMpn.matches("^(MKS|MKP|FKS|FKP|FKC)\\d+.*")) {
                return true;
            }
            // Check SMD series
            if (upperMpn.matches("^SMD[PM]\\d+.*")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();
        Matcher matcher = WIMA_PATTERN.matcher(upperMpn);

        if (matcher.matches()) {
            String sizeCode = matcher.group(2);
            // Return the lead spacing based on size code
            return SIZE_TO_PITCH.getOrDefault(sizeCode, sizeCode + "mm");
        }

        // SMD packages
        if (upperMpn.matches("^SMD[PM]\\d+.*")) {
            // Extract SMD package size (e.g., SMDP02 -> 0603)
            if (upperMpn.contains("02")) return "0603";
            if (upperMpn.contains("03")) return "0805";
            if (upperMpn.contains("04")) return "1206";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();
        Matcher matcher = WIMA_PATTERN.matcher(upperMpn);

        if (matcher.matches()) {
            // Return series with size code (e.g., MKS4, MKP10, FKP2)
            return matcher.group(1) + matcher.group(2);
        }

        // SMD series
        if (upperMpn.matches("^SMD[PM]\\d+.*")) {
            int endIdx = 5; // SMDP0 or SMDM0
            if (upperMpn.length() >= endIdx) {
                return upperMpn.substring(0, endIdx);
            }
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return false;
        }

        // Must be same series for replacement
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Must be same package/pitch
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        return pkg1.equals(pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get the dielectric type for a WIMA MPN.
     *
     * @param mpn the manufacturer part number
     * @return the dielectric type (PET, PP, PC) or empty string if not found
     */
    public String getDielectricType(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();
        Matcher matcher = WIMA_PATTERN.matcher(upperMpn);

        if (matcher.matches()) {
            String seriesPrefix = matcher.group(1);
            return SERIES_DIELECTRIC.getOrDefault(seriesPrefix, "");
        }

        // SMD series
        if (upperMpn.startsWith("SMDP")) {
            return "PET"; // SMD PET
        }
        if (upperMpn.startsWith("SMDM")) {
            return "PP"; // SMD PP (metallized polypropylene)
        }

        return "";
    }

    /**
     * Get the construction type for a WIMA MPN.
     *
     * @param mpn the manufacturer part number
     * @return "Metallized" or "Foil" or empty string if not found
     */
    public String getConstructionType(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("MK")) {
            return "Metallized";
        }
        if (upperMpn.startsWith("FK")) {
            return "Foil";
        }
        if (upperMpn.startsWith("SMD")) {
            return "SMD";
        }

        return "";
    }
}
