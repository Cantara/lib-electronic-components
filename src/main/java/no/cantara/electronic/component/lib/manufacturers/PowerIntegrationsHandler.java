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
 * Handler for Power Integrations SMPS (Switched-Mode Power Supply) controllers.
 *
 * Supports the following product families:
 * - TOPSwitch: TOP2xx, TOP3xx (e.g., TOP223Y, TOP249Y, TOP269KG)
 * - TinySwitch: TNY2xx, TNY3xx (e.g., TNY263, TNY264, TNY268)
 * - LinkSwitch: LNK3xx, LNK4xx, LNK6xx (e.g., LNK302, LNK304, LNK364)
 * - InnoSwitch: INN20xx, INN30xx, INN40xx (e.g., INN2023K, INN3166C)
 * - HiperPFS: PFS7xx (e.g., PFS714EG, PFS7624H)
 * - HiperLCS: LCS7xx (e.g., LCS708HG)
 * - CAPZero: CAP0xx (e.g., CAP002DG)
 * - SENZero: SEN0xx (e.g., SEN013DG)
 * - InnoSwitch3: INN3xxx (e.g., INN3673C)
 */
public class PowerIntegrationsHandler implements ManufacturerHandler {

    // Package code mappings for Power Integrations parts
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Standard package codes
        PACKAGE_CODES.put("Y", "TO-220");       // TO-220 (7-pin or less)
        PACKAGE_CODES.put("YN", "TO-220");      // TO-220 with specific variant
        PACKAGE_CODES.put("G", "SMD-8");        // SMD package (8-pin SOIC)
        PACKAGE_CODES.put("GN", "SMD-8");       // SMD package variant
        PACKAGE_CODES.put("D", "DIP-8");        // 8-pin DIP
        PACKAGE_CODES.put("DN", "DIP-8");       // 8-pin DIP variant
        PACKAGE_CODES.put("P", "PDIP-8");       // 8-pin PDIP
        PACKAGE_CODES.put("PN", "PDIP-8");      // 8-pin PDIP variant
        PACKAGE_CODES.put("K", "eSOP-12");      // eSOP-12 package (InnoSwitch)
        PACKAGE_CODES.put("KG", "eSOP-12");     // eSOP-12 with heatsink
        PACKAGE_CODES.put("C", "InSOP-24");     // InSOP-24 package (InnoSwitch)
        PACKAGE_CODES.put("CJ", "InSOP-24");    // InSOP-24 variant
        PACKAGE_CODES.put("H", "eSIP-7");       // eSIP-7 package (HiperPFS/HiperLCS)
        PACKAGE_CODES.put("HG", "eSIP-7");      // eSIP-7 with heatsink
        PACKAGE_CODES.put("EG", "eSIP-7");      // eSIP-7 variant
        PACKAGE_CODES.put("E", "eSIP");         // eSIP package
        PACKAGE_CODES.put("DG", "SMD-8");       // SMD-8 (used by CAPZero, SENZero)
    }

    // Series to family mapping
    private static final Map<String, String> SERIES_FAMILIES = new HashMap<>();
    static {
        SERIES_FAMILIES.put("TOP", "TOPSwitch");
        SERIES_FAMILIES.put("TNY", "TinySwitch");
        SERIES_FAMILIES.put("LNK", "LinkSwitch");
        SERIES_FAMILIES.put("INN", "InnoSwitch");
        SERIES_FAMILIES.put("PFS", "HiperPFS");
        SERIES_FAMILIES.put("LCS", "HiperLCS");
        SERIES_FAMILIES.put("CAP", "CAPZero");
        SERIES_FAMILIES.put("SEN", "SENZero");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.VOLTAGE_REGULATOR
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // TOPSwitch patterns: TOP2xx, TOP3xx
        registry.addPattern(ComponentType.IC, "^TOP2[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^TOP3[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^TOP2[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^TOP3[0-9]{2}[A-Z0-9]*$");

        // TinySwitch patterns: TNY2xx, TNY3xx
        registry.addPattern(ComponentType.IC, "^TNY2[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^TNY3[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^TNY2[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^TNY3[0-9]{2}[A-Z0-9]*$");

        // LinkSwitch patterns: LNK3xx, LNK4xx, LNK6xx
        registry.addPattern(ComponentType.IC, "^LNK3[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^LNK4[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^LNK6[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^LNK3[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^LNK4[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^LNK6[0-9]{2}[A-Z0-9]*$");

        // InnoSwitch patterns: INN20xx, INN30xx, INN40xx, INN3xxx
        registry.addPattern(ComponentType.IC, "^INN20[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^INN30[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^INN40[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.IC, "^INN3[0-9]{3}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^INN20[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^INN30[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^INN40[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^INN3[0-9]{3}[A-Z0-9]*$");

        // HiperPFS patterns: PFS7xx
        registry.addPattern(ComponentType.IC, "^PFS7[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^PFS7[0-9]{2}[A-Z0-9]*$");

        // HiperLCS patterns: LCS7xx
        registry.addPattern(ComponentType.IC, "^LCS7[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^LCS7[0-9]{2}[A-Z0-9]*$");

        // CAPZero patterns: CAP0xx
        registry.addPattern(ComponentType.IC, "^CAP0[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^CAP0[0-9]{2}[A-Z0-9]*$");

        // SENZero patterns: SEN0xx
        registry.addPattern(ComponentType.IC, "^SEN0[0-9]{2}[A-Z0-9]*$");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^SEN0[0-9]{2}[A-Z0-9]*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct matching for IC and VOLTAGE_REGULATOR types
        if (type == ComponentType.IC || type == ComponentType.VOLTAGE_REGULATOR) {
            // TOPSwitch: TOP2xx, TOP3xx
            if (upperMpn.matches("^TOP[23][0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
            // TinySwitch: TNY2xx, TNY3xx
            if (upperMpn.matches("^TNY[23][0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
            // LinkSwitch: LNK3xx, LNK4xx, LNK6xx
            if (upperMpn.matches("^LNK[346][0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
            // InnoSwitch: INN20xx, INN30xx, INN40xx
            if (upperMpn.matches("^INN[234]0[0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
            // InnoSwitch3: INN3xxx
            if (upperMpn.matches("^INN3[0-9]{3}[A-Z0-9]*$")) {
                return true;
            }
            // HiperPFS: PFS7xx
            if (upperMpn.matches("^PFS7[0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
            // HiperLCS: LCS7xx
            if (upperMpn.matches("^LCS7[0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
            // CAPZero: CAP0xx
            if (upperMpn.matches("^CAP0[0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
            // SENZero: SEN0xx
            if (upperMpn.matches("^SEN0[0-9]{2}[A-Z0-9]*$")) {
                return true;
            }
        }

        // Fall back to handler-specific patterns
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract the suffix after the numeric part
        String suffix = extractPackageSuffix(upperMpn);
        if (suffix.isEmpty()) return "";

        // Check for longer suffixes first (e.g., "KG" before "K", "YN" before "Y")
        for (int len = Math.min(suffix.length(), 2); len >= 1; len--) {
            String candidate = suffix.substring(0, len);
            if (PACKAGE_CODES.containsKey(candidate)) {
                return PACKAGE_CODES.get(candidate);
            }
        }

        return suffix;
    }

    /**
     * Extract the package suffix from the MPN (letters after the numeric part)
     */
    private String extractPackageSuffix(String mpn) {
        // Find where the numeric part ends
        int lastDigitIndex = -1;
        for (int i = 0; i < mpn.length(); i++) {
            if (Character.isDigit(mpn.charAt(i))) {
                lastDigitIndex = i;
            }
        }

        if (lastDigitIndex >= 0 && lastDigitIndex < mpn.length() - 1) {
            return mpn.substring(lastDigitIndex + 1);
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract the series prefix (first 3 letters)
        if (upperMpn.startsWith("TOP")) return "TOP";
        if (upperMpn.startsWith("TNY")) return "TNY";
        if (upperMpn.startsWith("LNK")) return "LNK";
        if (upperMpn.startsWith("INN")) return "INN";
        if (upperMpn.startsWith("PFS")) return "PFS";
        if (upperMpn.startsWith("LCS")) return "LCS";
        if (upperMpn.startsWith("CAP")) return "CAP";
        if (upperMpn.startsWith("SEN")) return "SEN";

        return "";
    }

    /**
     * Get the product family name for a given MPN
     *
     * @param mpn the manufacturer part number
     * @return the family name (e.g., "TOPSwitch", "TinySwitch")
     */
    public String getProductFamily(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_FAMILIES.getOrDefault(series, "");
    }

    /**
     * Extract the base part number (series + number, without package suffix)
     *
     * @param mpn the manufacturer part number
     * @return the base part number (e.g., "TOP249" from "TOP249YN")
     */
    public String extractBasePartNumber(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Find the boundary between number and letters
        StringBuilder basePart = new StringBuilder();
        for (int i = 0; i < upperMpn.length(); i++) {
            char c = upperMpn.charAt(i);
            if (Character.isLetter(c) && i > 0 && Character.isDigit(upperMpn.charAt(i - 1))) {
                break;
            }
            basePart.append(c);
        }
        return basePart.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from the same product family
        if (!series1.equals(series2)) return false;
        if (series1.isEmpty()) return false;

        // Extract base part numbers
        String base1 = extractBasePartNumber(mpn1);
        String base2 = extractBasePartNumber(mpn2);

        // Same base part number = same part, different package
        if (base1.equals(base2)) {
            return true;
        }

        // Check for related parts within the same sub-family
        // Parts with the same first 4 characters are typically compatible
        // (e.g., TOP223Y and TOP224Y are related but not identical)
        if (base1.length() >= 4 && base2.length() >= 4) {
            String family1 = base1.substring(0, 4);
            String family2 = base2.substring(0, 4);

            // Same sub-family parts may be replacements depending on power rating
            // Higher power parts can typically replace lower power parts
            if (family1.equals(family2)) {
                return areCompatiblePowerRatings(base1, base2);
            }
        }

        return false;
    }

    /**
     * Check if two parts have compatible power ratings for replacement.
     * Higher power parts can typically replace lower power parts.
     */
    private boolean areCompatiblePowerRatings(String base1, String base2) {
        // Extract the numeric part after the series prefix
        try {
            int num1 = extractNumericPart(base1);
            int num2 = extractNumericPart(base2);

            // Higher number typically means higher power rating
            // Either part can replace the other if they're in the same family
            // (user should verify electrical compatibility)
            return Math.abs(num1 - num2) <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Extract the numeric part from a base part number.
     */
    private int extractNumericPart(String basePart) {
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < basePart.length(); i++) {
            char c = basePart.charAt(i);
            if (Character.isDigit(c)) {
                num.append(c);
            }
        }
        return Integer.parseInt(num.toString());
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
