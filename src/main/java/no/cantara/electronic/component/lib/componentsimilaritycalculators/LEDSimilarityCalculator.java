package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LEDSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(LEDSimilarityCalculator.class);
    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    // Known equivalent groups
    private static final Map<String, Set<String>> EQUIVALENT_GROUPS = new HashMap<>();
    static {
        // TI High-power LED series
        EQUIVALENT_GROUPS.put("TLHR5400", Set.of("TLHR5400", "TLHR5401", "TLHR5402", "TLHR5403"));
        EQUIVALENT_GROUPS.put("TLHG5800", Set.of("TLHG5800", "TLHG5801", "TLHG5802", "TLHG5803"));
        EQUIVALENT_GROUPS.put("TLHB5800", Set.of("TLHB5800", "TLHB5801", "TLHB5802", "TLHB5803"));

        // Osram/OSRAM OPTO Standard LED series
        EQUIVALENT_GROUPS.put("LW E67C", Set.of("LW E67C", "LW E6SF", "LCW E6SF"));
        EQUIVALENT_GROUPS.put("LR E67C", Set.of("LR E67C", "LR E6SF", "LCR E6SF"));
        EQUIVALENT_GROUPS.put("LS E67B", Set.of("LS E67B", "LS E6SF", "LCS E6SF"));
        EQUIVALENT_GROUPS.put("LY E67B", Set.of("LY E67B", "LY E6SF", "LCY E6SF"));

        // LG LED series
        EQUIVALENT_GROUPS.put("LG R971", Set.of("LG R971", "LG R971-KN", "LG R971-PK"));
        EQUIVALENT_GROUPS.put("LG B971", Set.of("LG B971", "LG B971-KN", "LG B971-PK"));
        EQUIVALENT_GROUPS.put("LG G971", Set.of("LG G971", "LG G971-KN", "LG G971-PK"));

        // Cree LED series
        EQUIVALENT_GROUPS.put("XPE2", Set.of("XPERED-L1", "XPERED-L1-0000", "XPERED-L1-R250"));
        EQUIVALENT_GROUPS.put("XPG3", Set.of("XPGDWT-L1", "XPGDWT-L1-0000", "XPGDWT-L1-R250"));

        // Lumileds LUXEON series
        EQUIVALENT_GROUPS.put("L130", Set.of("L130-5580", "L130-5580CT", "L130-5580XT"));
        EQUIVALENT_GROUPS.put("L135", Set.of("L135-5780", "L135-5780CT", "L135-5780XT"));

        // Samsung LED series
        EQUIVALENT_GROUPS.put("LM301B", Set.of("LM301B", "LM301B-K", "LM301B-V2"));
        EQUIVALENT_GROUPS.put("LM281B", Set.of("LM281B", "LM281B-K", "LM281B-V2"));

        // Nichia LED series
        EQUIVALENT_GROUPS.put("NCSW", Set.of("NCSW170", "NCSW170T", "NCSW170AT"));
        EQUIVALENT_GROUPS.put("NCSR", Set.of("NCSR170", "NCSR170T", "NCSR170AT"));
    }


    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) return false;
        return type == ComponentType.LED ||
                type.name().startsWith("LED_") ||
                type.getBaseType() == ComponentType.LED;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        logger.debug("Comparing LEDs: {} vs {}", mpn1, mpn2);

        // Get base parts without bin codes
        String base1 = getBasePart(mpn1);
        String base2 = getBasePart(mpn2);

        logger.trace("Base parts: {} and {}", base1, base2);

        // Same exact part
        if (mpn1.equals(mpn2)) {
            logger.debug("Exact match");
            return HIGH_SIMILARITY;
        }

        // Same base part (different bins or suffixes)
        if (base1.equals(base2)) {
            // For Cree LEDs, check color temperature compatibility
            if (isCreeColorBin(mpn1) && isCreeColorBin(mpn2)) {
                if (!haveSameColorTemperature(mpn1, mpn2)) {
                    logger.debug("Different color temperatures");
                    return LOW_SIMILARITY;
                }
            }

            // Check if the difference is just in bin codes
            String bin1 = getBinCode(mpn1);
            String bin2 = getBinCode(mpn2);
            if (!bin1.isEmpty() && !bin2.isEmpty()) {
                if (areSimilarBins(bin1, bin2)) {
                    logger.debug("Same LED different bins: {} vs {}", bin1, bin2);
                    return HIGH_SIMILARITY;
                }
                logger.debug("Different color/characteristic bins");
                return LOW_SIMILARITY;
            }

            logger.debug("Same LED base with different suffixes");
            return HIGH_SIMILARITY;
        }

        // Different parts but same LED family
        if (areSameLEDFamily(base1, base2)) {
            // Still check color temperature for same family
            if (isCreeColorBin(mpn1) && isCreeColorBin(mpn2) &&
                    !haveSameColorTemperature(mpn1, mpn2)) {
                logger.debug("Same family but different color temperatures");
                return LOW_SIMILARITY;
            }
            logger.debug("Same LED family with different base numbers");
            return HIGH_SIMILARITY;
        }

        return MEDIUM_SIMILARITY;
    }

    private String getBinCode(String mpn) {
        if (mpn == null) return "";
        // Extract bin code
        if (mpn.contains("-")) {
            String suffix = mpn.substring(mpn.lastIndexOf("-") + 1);
            if (suffix.matches("FK[A-Z]|[A-Z][0-9]|BIN[0-9]|[K-N]{1,2}")) {
                return suffix;
            }
        }
        return "";
    }

    private boolean areSameLEDFamily(String base1, String base2) {
        // Same prefix check (e.g., CLVBA, TLHR)
        if (base1.equals(base2)) return true;

        // Check for common LED family prefixes
        String prefix1 = getLEDPrefix(base1);
        String prefix2 = getLEDPrefix(base2);
        return !prefix1.isEmpty() && prefix1.equals(prefix2);
    }

    private String getLEDPrefix(String base) {
        if (base == null || base.isEmpty()) return "";

        // Extract common LED family prefixes
        if (base.startsWith("CLVB")) return "CLVB";  // Cree CLVB family
        if (base.startsWith("TLHR")) return "TLHR";  // TI TLHR family
        if (base.matches("LG R\\d+")) return "LG R"; // LG R series

        // Add more LED family prefix patterns as needed
        return "";
    }

    private boolean isSameBinType(String bin1, String bin2) {
        // Both are brightness bins (K-N)
        boolean isBrightnessBin = bin1.matches("[K-N]") && bin2.matches("[K-N]");
        // Both are forward voltage bins (typically numbers)
        boolean isVoltageBin = bin1.matches("\\d+") && bin2.matches("\\d+");
        // Both are color bins (typically letter combinations)
        boolean isColorBin = bin1.matches("FK[A-Z]") && bin2.matches("FK[A-Z]");

        return isBrightnessBin || isVoltageBin || isColorBin;
    }

    private boolean isCreeColorBin(String mpn) {
        return mpn != null && mpn.matches(".*-F[KC][A-Z].*");
    }

    private boolean haveSameColorTemperature(String mpn1, String mpn2) {
        String colorCode1 = extractColorTemperatureCode(mpn1);
        String colorCode2 = extractColorTemperatureCode(mpn2);
        return colorCode1 != null && colorCode2 != null && colorCode1.equals(colorCode2);
    }

    private String extractColorTemperatureCode(String mpn) {
        if (mpn == null) return null;
        // Extract the color temperature indicator (K or C) from Cree's bin code
        if (mpn.contains("-F")) {
            int idx = mpn.indexOf("-F");
            if (idx + 2 < mpn.length()) {
                return String.valueOf(mpn.charAt(idx + 2));
            }
        }
        return null;
    }


    private boolean areSimilarBins(String bin1, String bin2) {
        // For Cree LEDs with F[KC][A-Z] pattern
        if (bin1.matches("F[KC][A-Z]") && bin2.matches("F[KC][A-Z]")) {
            // Must have same color temperature indicator (K or C)
            return bin1.charAt(1) == bin2.charAt(1);
        }

        // Other bin comparisons (brightness, voltage, etc.)
        return isSameBinType(bin1, bin2);
    }

    private boolean isLED(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();

        // TI LED series
        if (upperMpn.matches("^TLH[RGB][0-9]{4}.*")) return true;  // RGB LEDs
        if (upperMpn.matches("^TLW[A-Z][0-9]{4}.*")) return true;  // White LEDs

        // LG LED series
        if (upperMpn.matches("^LG\\s*[RGB][0-9]{3}.*")) return true;

        // Osram LED series
        if (upperMpn.matches("^L[WRGBY][A-Z][0-9].*")) return true;   // Standard LEDs
        if (upperMpn.matches("^LC[WRGBY][A-Z][0-9].*")) return true;  // Chip LEDs
        if (upperMpn.matches("^LS[WRGBY][A-Z][0-9].*")) return true;  // Special LEDs

        // Cree LED series
        if (upperMpn.matches("^XP[ERGB][0-9].*")) return true;        // XP series
        if (upperMpn.matches("^XB[ERGB][0-9].*")) return true;        // XB series
        if (upperMpn.matches("^XQ[ERGB][0-9].*")) return true;        // XQ series
        if (upperMpn.matches("^XH[ERGB][0-9].*")) return true;        // XH series
        if (upperMpn.matches("^(?:MC|PC)LED.*")) return true;         // MC/PC series

        // Lumileds LUXEON series
        if (upperMpn.matches("^L[0-9]{3}-.*")) return true;           // LUXEON series
        if (upperMpn.matches("^LXML.*")) return true;                 // LUXEON XML
        if (upperMpn.matches("^LXHL.*")) return true;                 // LUXEON Star

        // Samsung LED series
        if (upperMpn.matches("^LM[0-9]{3}[A-Z].*")) return true;     // LM series
        if (upperMpn.matches("^LC[0-9]{3}[A-Z].*")) return true;     // LC series
        if (upperMpn.matches("^LH[0-9]{3}[A-Z].*")) return true;     // LH series

        // Nichia LED series
        if (upperMpn.matches("^NCS[WRGB][0-9]{3}.*")) return true;   // Standard series
        if (upperMpn.matches("^NVSU[0-9]{3}[A-Z].*")) return true;   // UV series
        if (upperMpn.matches("^NS[WPB][0-9]{3}.*")) return true;     // Surface mount series

        // Everlight LED series
        if (upperMpn.matches("^(?:67|19|12)-2[0-9]{2}.*")) return true; // Standard series
        if (upperMpn.matches("^EL[0-9]{3}.*")) return true;             // EL series

        // Kingbright LED series
        if (upperMpn.matches("^WP[0-9]{3}[A-Z].*")) return true;     // WP series
        if (upperMpn.matches("^AP[0-9]{3}[A-Z].*")) return true;     // AP series
        if (upperMpn.matches("^L-[0-9]{3}.*")) return true;          // L series

        // Generic LED patterns
        if (upperMpn.matches("^LED.*")) return true;
        if (upperMpn.matches(".*(RGB|RED|GRN|BLU|WHT).*LED.*")) return true;
        if (upperMpn.matches(".*LED.*(RGB|RED|GRN|BLU|WHT).*")) return true;

        // Common LED prefixes and patterns
        return upperMpn.startsWith("LED") ||
                upperMpn.matches("^(CL[A-Z]{2,4}|HL[A-Z]{2,4}).*") || // Cree
                upperMpn.matches("^L[RGBY][A-Z][0-9].*") ||          // OSRAM
                upperMpn.matches("^TL(HR|G)[0-9].*") ||              // TI
                upperMpn.matches("^LG R[0-9].*") ||                  // LG
                upperMpn.matches("^(XP|XT|XB|XQ)[A-Z][0-9].*") ||   // Cree XP/XT series
                upperMpn.startsWith("CLVBA");
    }


    private String getBasePart(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase().trim();

        // LG LED series
        if (upperMpn.startsWith("LG")) {
            return upperMpn.replaceAll("-.*$", "").trim();  // Remove everything after hyphen
        }

        // TI LED series
        if (upperMpn.matches("^TLH[RGB][0-9]{4}.*")) {
            return upperMpn.substring(0, 8);  // Keep base part including 4-digit number
        }
        if (upperMpn.matches("^TLW[A-Z][0-9]{4}.*")) {
            return upperMpn.substring(0, 9);  // Keep base part including letter and 4-digit number
        }

        // Generic base part extraction
        return mpn.replaceAll("[-_](FK[A-Z]|[A-Z][0-9]|BIN[0-9]|[0-9]+).*$", "");

    }

    private String normalizeMPN(String mpn) {
        if (mpn == null) return "";
        String normalized = mpn.toUpperCase()
                .replaceAll("\\s+", " ")
                .trim();

        // For LG LEDs, keep the full part number including suffix
        if (normalized.startsWith("LG")) {
            return normalized;
        }

        // For other LEDs, remove suffixes
        return normalized.replaceAll("-.*$", "");
    }

    private String getLEDFamily(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // TI LED families
        if (upperMpn.startsWith("TLHR")) return "TI_RED";
        if (upperMpn.startsWith("TLHG")) return "TI_GREEN";
        if (upperMpn.startsWith("TLHB")) return "TI_BLUE";
        if (upperMpn.startsWith("TLW")) return "TI_WHITE";

        // Osram LED families
        if (upperMpn.matches("^L[WRG].*")) return "OSRAM_" + upperMpn.charAt(1);
        if (upperMpn.matches("^LC[WRG].*")) return "OSRAM_" + upperMpn.charAt(2);

        // LG LED families
        if (upperMpn.startsWith("LG")) {
            return "LG_" + upperMpn.replaceAll("^LG\\s*([RGB]).*$", "$1");
        }

        // Cree LED families
        if (upperMpn.startsWith("XP")) {
            return "CREE_XP_" + (upperMpn.charAt(2) == 'E' ? "WHITE" : upperMpn.charAt(2));
        }

        // Lumileds families
        if (upperMpn.matches("^L[0-9]{3}.*")) return "LUXEON_STANDARD";
        if (upperMpn.startsWith("LXML")) return "LUXEON_XML";

        // Samsung families
        if (upperMpn.startsWith("LM")) return "SAMSUNG_LM";
        if (upperMpn.startsWith("LC")) return "SAMSUNG_LC";

        // Nichia families
        if (upperMpn.startsWith("NCS")) {
            return "NICHIA_" + upperMpn.charAt(3);
        }

        return "OTHER";
    }

    private String getPackageCode(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // For LG LEDs with suffix, consider the suffix as package code
        if (upperMpn.startsWith("LG") && upperMpn.contains("-")) {
            return upperMpn.substring(upperMpn.indexOf("-") + 1);
        }

        // TI LED packages
        if (upperMpn.matches("^TLH[RGB][0-9]{4}.*")) return "SMD";
        if (upperMpn.matches("^TLW[A-Z][0-9]{4}.*")) return "SMD";

        // Extract package code after hyphen for other LEDs
        if (upperMpn.contains("-")) {
            return upperMpn.substring(upperMpn.lastIndexOf("-") + 1);
        }

        return "";
    }

    private boolean arePackagesCompatible(String pkg1, String pkg2) {
        if (pkg1.equals(pkg2)) return true;

        // For LG LEDs, consider all suffix variations compatible
        if (pkg1.matches("(?:KN|PK).*") && pkg2.matches("(?:KN|PK).*")) {
            return true;
        }

        // SMD package variations
        Set<String> smdPackages = Set.of(
                "SMD", "PLCC", "3528", "5050", "2835", "3030", "5630",
                "0603", "0805", "1206"
        );
        if (smdPackages.contains(pkg1) && smdPackages.contains(pkg2)) {
            return true;
        }

        // Through-hole package variations
        Set<String> thPackages = Set.of(
                "TH", "DIP", "5MM", "3MM", "8MM", "10MM",
                "T-1", "T-1¾", "T-1¾BI"
        );
        if (thPackages.contains(pkg1) && thPackages.contains(pkg2)) {
            return true;
        }

        // High-power LED packages
        Set<String> hpPackages = Set.of(
                "STAR", "MCE", "XPE", "XPG", "XML",
                "LUXEON", "REBEL"
        );
        if (hpPackages.contains(pkg1) && hpPackages.contains(pkg2)) {
            return true;
        }

        return false;
    }
}