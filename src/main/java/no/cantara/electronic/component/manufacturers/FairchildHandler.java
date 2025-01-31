package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Handler for Fairchild Semiconductor (now part of ON Semiconductor) components
 */
public class FairchildHandler implements ManufacturerHandler {
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Through-hole packages
        PACKAGE_CODES.put("N", "TO-220");      // Standard TO-220
        PACKAGE_CODES.put("TA", "TO-220F");    // Fully isolated TO-220
        PACKAGE_CODES.put("TU", "TO-251");     // I-PAK

        // Surface mount packages
        PACKAGE_CODES.put("S", "D2PAK");       // TO-263
        PACKAGE_CODES.put("L", "DPAK");        // TO-252
        PACKAGE_CODES.put("F", "IPAK");        // TO-251
        PACKAGE_CODES.put("U", "SOT-223");
        PACKAGE_CODES.put("G", "SOT-223");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        // Note: Fairchild was acquired by ON Semiconductor, so types might overlap
        types.add(ComponentType.MOSFET);
        types.add(ComponentType.TRANSISTOR);
        types.add(ComponentType.DIODE);
        types.add(ComponentType.VOLTAGE_REGULATOR);
        // Could add specific Fairchild types if they exist in ComponentType enum
        return types;
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MOSFETs
        // N-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^FQP[0-9].*N[0-9].*");  // N-Channel TO-220
        registry.addPattern(ComponentType.MOSFET, "^FQD[0-9].*N[0-9].*");  // N-Channel DPAK
        registry.addPattern(ComponentType.MOSFET, "^FQU[0-9].*N[0-9].*");  // N-Channel IPAK
        registry.addPattern(ComponentType.MOSFET, "^FDS[0-9].*");          // N-Channel SO-8

        // P-Channel MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^FQP[0-9].*P[0-9].*");  // P-Channel TO-220
        registry.addPattern(ComponentType.MOSFET, "^FQD[0-9].*P[0-9].*");  // P-Channel DPAK
        registry.addPattern(ComponentType.MOSFET, "^FQU[0-9].*P[0-9].*");  // P-Channel IPAK
        registry.addPattern(ComponentType.MOSFET, "^FDS[0-9].*P.*");       // P-Channel SO-8

        // Logic Level MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^FQL[0-9].*");          // Logic Level series

        // Small Signal MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^2N7[0-9].*");          // 2N7xxx series
        registry.addPattern(ComponentType.MOSFET, "^BS[0-9].*");           // BS series

        // PowerTrench MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^FDB[0-9].*");          // PowerTrench series

        // Complementary Pairs
        registry.addPattern(ComponentType.MOSFET, "^FDA[0-9].*");          // Complementary pairs
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct matching for MOSFETs
        if (type == ComponentType.MOSFET) {
            // N-Channel patterns
            if (upperMpn.matches("^FQP[0-9]+N[0-9]+.*") ||    // TO-220 N-Channel
                    upperMpn.matches("^FQD[0-9]+N[0-9]+.*") ||    // DPAK N-Channel
                    upperMpn.matches("^FQU[0-9]+N[0-9]+.*") ||    // IPAK N-Channel
                    upperMpn.matches("^FDS[0-9]+.*")) {           // SO-8 N-Channel
                return true;
            }

            // P-Channel patterns
            if (upperMpn.matches("^FQP[0-9]+P[0-9]+.*") ||    // TO-220 P-Channel
                    upperMpn.matches("^FQD[0-9]+P[0-9]+.*") ||    // DPAK P-Channel
                    upperMpn.matches("^FQU[0-9]+P[0-9]+.*") ||    // IPAK P-Channel
                    upperMpn.matches("^FDS[0-9]+P.*")) {          // SO-8 P-Channel
                return true;
            }

            // Other MOSFET families
            if (upperMpn.matches("^FQL[0-9]+.*") ||           // Logic Level
                    upperMpn.matches("^2N7[0-9]+.*") ||           // Small Signal
                    upperMpn.matches("^BS[0-9]+.*") ||            // Small Signal
                    upperMpn.matches("^FDB[0-9]+.*") ||           // PowerTrench
                    upperMpn.matches("^FDA[0-9]+.*")) {           // Complementary
                return true;
            }
        }

        // Use pattern registry for other matches
        Pattern pattern = patterns.getPattern(type);
        return pattern != null && pattern.matcher(upperMpn).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for known package codes
        for (Map.Entry<String, String> entry : PACKAGE_CODES.entrySet()) {
            if (upperMpn.endsWith(entry.getKey()) || upperMpn.contains(entry.getKey() + "-")) {
                return entry.getValue();
            }
        }

        // Package type from prefix
        if (upperMpn.startsWith("FQP")) return "TO-220";
        if (upperMpn.startsWith("FQD")) return "DPAK";
        if (upperMpn.startsWith("FQU")) return "IPAK";
        if (upperMpn.startsWith("FDS")) return "SO-8";

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract series based on prefix and type
        if (upperMpn.startsWith("FQP")) {
            return upperMpn.replaceAll("([A-Z0-9]+[NP][0-9]+).*", "$1");  // FQPxxNxx or FQPxxPxx
        }
        if (upperMpn.startsWith("FQD") || upperMpn.startsWith("FQU")) {
            return upperMpn.replaceAll("([A-Z0-9]+[NP][0-9]+).*", "$1");  // FQDxxNxx or FQUxxPxx
        }
        if (upperMpn.startsWith("FDS")) {
            return upperMpn.replaceAll("(FDS[0-9]+).*", "$1");
        }
        if (upperMpn.startsWith("FQL")) {
            return upperMpn.replaceAll("(FQL[0-9]+).*", "$1");
        }
        if (upperMpn.startsWith("2N7")) {
            return upperMpn.replaceAll("(2N7[0-9]+).*", "$1");
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series check
        if (!series1.isEmpty() && series1.equals(series2)) {
            String pkg1 = extractPackageCode(mpn1);
            String pkg2 = extractPackageCode(mpn2);

            // Same package is direct replacement
            if (pkg1.equals(pkg2)) return true;

            // Check compatible packages
            if (arePackagesCompatible(pkg1, pkg2)) return true;
        }

        // Check for known cross-reference replacements
        if (isKnownReplacement(mpn1, mpn2)) return true;

        return false;
    }

    private boolean arePackagesCompatible(String pkg1, String pkg2) {
        // TO-220 variants are usually compatible
        if (pkg1.startsWith("TO-220") && pkg2.startsWith("TO-220")) return true;

        // DPAK and D2PAK are often compatible
        if ((pkg1.equals("DPAK") || pkg1.equals("D2PAK")) &&
                (pkg2.equals("DPAK") || pkg2.equals("D2PAK"))) return true;

        // SOT-223 and DPAK can be compatible in some cases
        if ((pkg1.equals("SOT-223") || pkg1.equals("DPAK")) &&
                (pkg2.equals("SOT-223") || pkg2.equals("DPAK"))) return true;

        return false;
    }

    private boolean isKnownReplacement(String mpn1, String mpn2) {
        // Known replacement pairs
        if (mpn1.startsWith("FQP30N06") && mpn2.startsWith("IRF530")) return true;
        if (mpn1.startsWith("IRF530") && mpn2.startsWith("FQP30N06")) return true;

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();  // Using standard ComponentType enum
    }
}