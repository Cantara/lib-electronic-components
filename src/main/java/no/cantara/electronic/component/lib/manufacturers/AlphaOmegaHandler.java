package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Alpha and Omega Semiconductor (AOS) components.
 *
 * AOS specializes in power semiconductors, particularly MOSFETs.
 *
 * MPN Structure:
 * [AO][Package Code][Series Number][Variant]
 *
 * Package Code Prefixes:
 * - AO3xxx = SOT-23 packages (3 = SOT-23)
 * - AO4xxx = SO-8 packages (4 = SO-8)
 * - AOD = TO-252 (DPAK) packages
 * - AON = DFN packages (various sizes)
 * - AOI = TO-251 (IPAK) packages
 * - AOT = TO-220/TO-247 packages
 * - AOB = D2PAK (TO-263) packages
 * - AOC = Common drain dual MOSFETs
 * - AOP = PDFN packages
 * - AOTL = TOLL (TO-Leadless) packages
 * - AOGT = GTPAK (topside cooling)
 * - AOGL = GLPAK (gull-wing TOLL)
 *
 * Examples:
 * - AO3401A: P-channel MOSFET, SOT-23, 30V, 4A
 * - AO3400A: N-channel MOSFET, SOT-23, 30V, 5.7A
 * - AOD4184: N-channel MOSFET, TO-252 (DPAK), 40V, 50A
 * - AON6758: N-channel MOSFET, DFN 5x6, 30V, 85A
 * - AOT240L: N-channel MOSFET, TO-220, 40V, 79A
 * - AO4407A: P-channel MOSFET, SO-8, 30V, 12A
 */
public class AlphaOmegaHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MOSFETs - primary product line for AOS
        // SOT-23 series (AO3xxx)
        registry.addPattern(ComponentType.MOSFET, "^AO3[0-9]{3}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AO3[0-9]{3}[A-Z]?-.*");

        // SO-8 series (AO4xxx)
        registry.addPattern(ComponentType.MOSFET, "^AO4[0-9]{3}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AO4[0-9]{3}[A-Z]?-.*");

        // TO-252/DPAK series (AOD)
        registry.addPattern(ComponentType.MOSFET, "^AOD[0-9]{3,4}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOD[0-9]{3,4}[A-Z]?-.*");

        // DFN series (AON)
        registry.addPattern(ComponentType.MOSFET, "^AON[0-9]{3,4}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AON[0-9]{3,4}[A-Z]?-.*");

        // TO-251/IPAK series (AOI)
        registry.addPattern(ComponentType.MOSFET, "^AOI[0-9]{3,4}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOI[0-9]{3,4}[A-Z]?-.*");

        // TO-220/TO-247 series (AOT)
        registry.addPattern(ComponentType.MOSFET, "^AOT[0-9]{3,4}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOT[0-9]{3,4}[A-Z]?-.*");

        // D2PAK/TO-263 series (AOB)
        registry.addPattern(ComponentType.MOSFET, "^AOB[0-9]{3,4}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOB[0-9]{3,4}[A-Z]?-.*");

        // Common drain dual MOSFETs (AOC)
        registry.addPattern(ComponentType.MOSFET, "^AOC[0-9]{3,4}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOC[0-9]{3,4}[A-Z]?-.*");

        // PDFN series (AOP)
        registry.addPattern(ComponentType.MOSFET, "^AOP[0-9]{3,4}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOP[0-9]{3,4}[A-Z]?-.*");

        // TOLL package series (AOTL)
        registry.addPattern(ComponentType.MOSFET, "^AOTL[0-9]{3,5}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOTL[0-9]{3,5}[A-Z]?-.*");

        // GTPAK topside cooling (AOGT)
        registry.addPattern(ComponentType.MOSFET, "^AOGT[0-9]{3,5}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOGT[0-9]{3,5}[A-Z]?-.*");

        // GLPAK gull-wing TOLL (AOGL)
        registry.addPattern(ComponentType.MOSFET, "^AOGL[0-9]{3,5}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AOGL[0-9]{3,5}[A-Z]?-.*");

        // AONS/AONR DFN source-down series
        registry.addPattern(ComponentType.MOSFET, "^AONS[0-9]{3,5}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AONS[0-9]{3,5}[A-Z]?-.*");
        registry.addPattern(ComponentType.MOSFET, "^AONR[0-9]{3,5}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AONR[0-9]{3,5}[A-Z]?-.*");

        // AONK DFN source-down (DFN3.3x3.3)
        registry.addPattern(ComponentType.MOSFET, "^AONK[0-9]{3,5}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AONK[0-9]{3,5}[A-Z]?-.*");

        // Generic AO pattern fallback
        registry.addPattern(ComponentType.MOSFET, "^AO[0-9]{4,5}[A-Z]?$");
        registry.addPattern(ComponentType.MOSFET, "^AO[0-9]{4,5}[A-Z]?-.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MOSFET
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // MOSFET patterns - AOS specializes in MOSFETs
        if (type == ComponentType.MOSFET) {
            // Check longer prefixes FIRST (before shorter ones that are substrings)

            // TOLL package series (AOTL) - check before AOT
            if (upperMpn.matches("^AOTL[0-9]{3,5}[A-Z]?(-.*)?$")) {
                return true;
            }
            // GTPAK topside cooling (AOGT)
            if (upperMpn.matches("^AOGT[0-9]{3,5}[A-Z]?(-.*)?$")) {
                return true;
            }
            // GLPAK gull-wing (AOGL)
            if (upperMpn.matches("^AOGL[0-9]{3,5}[A-Z]?(-.*)?$")) {
                return true;
            }
            // AONK DFN source-down - check before AON
            if (upperMpn.matches("^AONK[0-9]{3,5}[A-Z]?(-.*)?$")) {
                return true;
            }
            // AONS/AONR DFN source-down - check before AON
            if (upperMpn.matches("^AONS[0-9]{3,5}[A-Z]?(-.*)?$")) {
                return true;
            }
            if (upperMpn.matches("^AONR[0-9]{3,5}[A-Z]?(-.*)?$")) {
                return true;
            }

            // SOT-23 series (AO3xxx)
            if (upperMpn.matches("^AO3[0-9]{3}[A-Z]?(-.*)?$")) {
                return true;
            }
            // SO-8 series (AO4xxx)
            if (upperMpn.matches("^AO4[0-9]{3}[A-Z]?(-.*)?$")) {
                return true;
            }
            // TO-252/DPAK series (AOD) - supports digits only OR alphanumeric (e.g., AOD4184, AOD10N60)
            if (upperMpn.matches("^AOD[0-9A-Z]{3,6}[A-Z]?(-.*)?$")) {
                return true;
            }
            // DFN series (AON)
            if (upperMpn.matches("^AON[0-9]{3,4}[A-Z]?(-.*)?$")) {
                return true;
            }
            // TO-251/IPAK series (AOI)
            if (upperMpn.matches("^AOI[0-9]{3,4}[A-Z]?(-.*)?$")) {
                return true;
            }
            // TO-220/TO-247 series (AOT) - supports digits only OR alphanumeric (e.g., AOT240L, AOT10N60)
            if (upperMpn.matches("^AOT[0-9A-Z]{3,6}[A-Z]?(-.*)?$")) {
                return true;
            }
            // D2PAK/TO-263 series (AOB) - supports digits only OR alphanumeric (e.g., AOB409L, AOB10N60)
            if (upperMpn.matches("^AOB[0-9A-Z]{3,6}[A-Z]?(-.*)?$")) {
                return true;
            }
            // Common drain dual MOSFETs (AOC)
            if (upperMpn.matches("^AOC[0-9]{3,4}[A-Z]?(-.*)?$")) {
                return true;
            }
            // PDFN series (AOP)
            if (upperMpn.matches("^AOP[0-9]{3,4}[A-Z]?(-.*)?$")) {
                return true;
            }
            // Generic AO + 4-5 digits
            if (upperMpn.matches("^AO[0-9]{4,5}[A-Z]?(-.*)?$")) {
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

        // Check longer prefixes FIRST (before shorter ones that are substrings)

        // TOLL package series (AOTL) - check before AOT
        if (upperMpn.startsWith("AOTL")) {
            return "TOLL";
        }

        // GTPAK topside cooling (AOGT) - check before AOT (if it existed)
        if (upperMpn.startsWith("AOGT")) {
            return "GTPAK";
        }

        // GLPAK gull-wing (AOGL)
        if (upperMpn.startsWith("AOGL")) {
            return "GLPAK";
        }

        // DFN3.3x3.3 source-down (AONK) - check before AON
        if (upperMpn.startsWith("AONK")) {
            return "DFN3.3x3.3";
        }

        // DFN source-down series (AONS, AONR) - check before AON
        if (upperMpn.startsWith("AONS") || upperMpn.startsWith("AONR")) {
            return "DFN";
        }

        // SOT-23 series (AO3xxx)
        if (upperMpn.matches("^AO3[0-9]{3}.*")) {
            return "SOT-23";
        }

        // SO-8 series (AO4xxx)
        if (upperMpn.matches("^AO4[0-9]{3}.*")) {
            return "SO-8";
        }

        // TO-252/DPAK series (AOD)
        if (upperMpn.startsWith("AOD")) {
            return "TO-252";
        }

        // DFN series (AON) - various sizes
        if (upperMpn.startsWith("AON")) {
            return "DFN";
        }

        // TO-251/IPAK series (AOI)
        if (upperMpn.startsWith("AOI")) {
            return "TO-251";
        }

        // TO-220 series (AOT)
        if (upperMpn.startsWith("AOT")) {
            return "TO-220";
        }

        // D2PAK/TO-263 series (AOB)
        if (upperMpn.startsWith("AOB")) {
            return "TO-263";
        }

        // Common drain dual MOSFETs (AOC)
        if (upperMpn.startsWith("AOC")) {
            return "SO-8";  // Typically SO-8 for common drain pairs
        }

        // PDFN series (AOP)
        if (upperMpn.startsWith("AOP")) {
            return "PDFN";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Remove any suffix after hyphen for series extraction
        int hyphenIndex = upperMpn.indexOf('-');
        String baseMpn = hyphenIndex >= 0 ? upperMpn.substring(0, hyphenIndex) : upperMpn;

        // Extended prefixes first (longer patterns checked before shorter)
        // AOTL, AOGT, AOGL, AONS, AONR, AONK series
        if (baseMpn.matches("^AOTL[0-9]{3,5}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOTL");
        }
        if (baseMpn.matches("^AOGT[0-9]{3,5}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOGT");
        }
        if (baseMpn.matches("^AOGL[0-9]{3,5}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOGL");
        }
        if (baseMpn.matches("^AONS[0-9]{3,5}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AONS");
        }
        if (baseMpn.matches("^AONR[0-9]{3,5}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AONR");
        }
        if (baseMpn.matches("^AONK[0-9]{3,5}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AONK");
        }

        // Standard 3-letter prefixes (AOD, AON, AOI, AOT, AOB, AOC, AOP)
        if (baseMpn.matches("^AOD[0-9]{3,4}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOD");
        }
        if (baseMpn.matches("^AON[0-9]{3,4}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AON");
        }
        if (baseMpn.matches("^AOI[0-9]{3,4}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOI");
        }
        if (baseMpn.matches("^AOT[0-9]{3,4}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOT");
        }
        if (baseMpn.matches("^AOB[0-9]{3,4}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOB");
        }
        if (baseMpn.matches("^AOC[0-9]{3,4}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOC");
        }
        if (baseMpn.matches("^AOP[0-9]{3,4}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AOP");
        }

        // SOT-23 series (AO3xxx)
        if (baseMpn.matches("^AO3[0-9]{3}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AO");
        }

        // SO-8 series (AO4xxx)
        if (baseMpn.matches("^AO4[0-9]{3}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AO");
        }

        // Generic AO + digits
        if (baseMpn.matches("^AO[0-9]{4,5}[A-Z]?$")) {
            return extractSeriesNumber(baseMpn, "AO");
        }

        return "";
    }

    /**
     * Extract series number from MPN after prefix.
     * Returns prefix + digits (without trailing letter variants).
     */
    private String extractSeriesNumber(String mpn, String prefix) {
        String afterPrefix = mpn.substring(prefix.length());
        StringBuilder series = new StringBuilder(prefix);

        for (int i = 0; i < afterPrefix.length(); i++) {
            char c = afterPrefix.charAt(i);
            if (Character.isDigit(c)) {
                series.append(c);
            } else {
                break;
            }
        }

        return series.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return false;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series for official replacement
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Same package required
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        return pkg1.equals(pkg2) && !pkg1.isEmpty();
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
