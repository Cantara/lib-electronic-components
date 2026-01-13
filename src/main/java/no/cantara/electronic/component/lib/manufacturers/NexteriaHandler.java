package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Nexperia components (formerly NXP Standard Products/Philips Semiconductors).
 * <p>
 * Nexperia specializes in discrete components and logic ICs:
 * <ul>
 *   <li><b>Transistors</b>: PMBT, PBSS, BC, BF, PN, 2N series (NPN/PNP)</li>
 *   <li><b>MOSFETs</b>: PSMN (N-ch power), PSMP (P-ch power), PMV (small signal), 2N7002</li>
 *   <li><b>Diodes</b>: PMEG (Schottky), BAV/BAS (signal), BAT (Schottky), BZX/PZU (Zener)</li>
 *   <li><b>ESD Protection</b>: PESD, PRTR, PTVS, IP4 series</li>
 *   <li><b>Logic ICs</b>: 74LVC, 74AHC, 74AUP, 74HC, 74HCT families</li>
 * </ul>
 * <p>
 * NOTE: The filename uses "Nexteria" which is a typo - the manufacturer is "Nexperia".
 * <p>
 * Package suffix format: Most Nexperia parts use ",215" or ",315" suffix for tape & reel:
 * <ul>
 *   <li>PMBT2222A,215 - SOT23 package, 7" reel</li>
 *   <li>PESD5V0S1BL,315 - SOD882 package, 7" reel</li>
 * </ul>
 */
public class NexteriaHandler extends AbstractManufacturerHandler {

    // Package code mappings for Nexperia-specific suffixes
    private static final java.util.Map<String, String> NEXPERIA_PACKAGES = java.util.Map.ofEntries(
            // SOT packages (transistors, small MOSFETs)
            java.util.Map.entry("215", "SOT23"),           // Standard 7" reel for SOT23
            java.util.Map.entry("235", "SOT23"),           // Alternate SOT23
            java.util.Map.entry("315", "SOD882"),          // Leadless package
            java.util.Map.entry("115", "SOT223"),          // SOT223 package
            java.util.Map.entry("T", "SOT23"),
            java.util.Map.entry("S", "SOT363"),            // 6-pin SOT
            java.util.Map.entry("U", "SOT323"),            // SC-70
            java.util.Map.entry("W", "SC70"),
            java.util.Map.entry("F", "SOT89"),

            // Power MOSFET packages
            java.util.Map.entry("LFPAK56", "LFPAK56"),     // Power MOSFET standard
            java.util.Map.entry("LFPAK88", "LFPAK88"),     // Larger power MOSFET
            java.util.Map.entry("LFPAK33", "LFPAK33"),     // Smaller power MOSFET
            java.util.Map.entry("SOT754", "SOT754"),       // Power-SO8
            java.util.Map.entry("L", "TO-220"),
            java.util.Map.entry("FI", "TO-220F"),          // Isolated TO-220

            // Logic IC packages
            java.util.Map.entry("D", "SO14"),
            java.util.Map.entry("PW", "TSSOP"),
            java.util.Map.entry("BQ", "DHVQFN"),
            java.util.Map.entry("GD", "XSON8"),
            java.util.Map.entry("GS", "XSON6")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ========== MOSFETs ==========
        // Power MOSFETs - PSMN (N-channel), PSMP (P-channel)
        registry.addPattern(ComponentType.MOSFET, "^PSMN[0-9].*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^PSMN[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^PSMP[0-9].*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^PSMP[0-9].*");

        // Small signal MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PMV[0-9].*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^PMV[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^BSS[0-9].*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^BSS[0-9].*");

        // 2N7002 series (very popular N-channel MOSFET)
        registry.addPattern(ComponentType.MOSFET, "^2N7002.*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^2N7002.*");

        // Legacy MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^BUK[0-9].*");
        registry.addPattern(ComponentType.MOSFET_NEXPERIA, "^BUK[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^PJD[0-9].*");         // JFET series

        // ========== Transistors (BJT) ==========
        // PMBT series - small signal transistors (SOT23)
        registry.addPattern(ComponentType.TRANSISTOR, "^PMBT[0-9].*");
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^PMBT[0-9].*");

        // PBSS series - small signal transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PBSS[0-9].*");
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^PBSS[0-9].*");

        // PMP series - medium power transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PMP[0-9].*");
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^PMP[0-9].*");

        // PXN series - high power transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PXN[0-9].*");
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^PXN[0-9].*");

        // Classic transistor series (inherited from Philips)
        registry.addPattern(ComponentType.TRANSISTOR, "^BC[0-9].*");      // BC546, BC547, BC548, BC549, BC550
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^BC[0-9].*");
        registry.addPattern(ComponentType.TRANSISTOR, "^BF[0-9].*");      // BF series
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^BF[0-9].*");
        registry.addPattern(ComponentType.TRANSISTOR, "^PN2222.*");       // PN2222A
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^PN2222.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^2N2222.*");       // 2N2222A
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^2N2222.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^2N3904.*");
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^2N3904.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^2N3906.*");
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^2N3906.*");

        // MMBT series - surface mount transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT[0-9].*");
        registry.addPattern(ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, "^MMBT[0-9].*");

        // ========== Diodes ==========
        // PMEG series - Schottky rectifiers
        registry.addPattern(ComponentType.DIODE, "^PMEG[0-9].*");

        // BAV series - signal/switching diodes
        registry.addPattern(ComponentType.DIODE, "^BAV[0-9].*");          // BAV99, BAV70, etc.

        // BAS series - signal diodes
        registry.addPattern(ComponentType.DIODE, "^BAS[0-9].*");          // BAS16, BAS21, etc.

        // BAT series - Schottky diodes
        registry.addPattern(ComponentType.DIODE, "^BAT[0-9].*");          // BAT54, BAT46, etc.

        // BZX series - Zener diodes
        registry.addPattern(ComponentType.DIODE, "^BZX[0-9].*");          // BZX84, BZX55, etc.

        // PZU series - Zener diodes
        registry.addPattern(ComponentType.DIODE, "^PZU[0-9].*");

        // 1N series diodes (standard)
        registry.addPattern(ComponentType.DIODE, "^1N4148.*");            // Signal diode
        registry.addPattern(ComponentType.DIODE, "^1N914.*");             // Signal diode

        // ========== ESD Protection ==========
        // PESD series - single/dual line ESD protection
        registry.addPattern(ComponentType.IC, "^PESD[0-9].*");
        registry.addPattern(ComponentType.ESD_PROTECTION_NEXPERIA, "^PESD[0-9].*");

        // PRTR series - ESD protection arrays
        registry.addPattern(ComponentType.IC, "^PRTR[0-9].*");
        registry.addPattern(ComponentType.ESD_PROTECTION_NEXPERIA, "^PRTR[0-9].*");

        // PTVS series - TVS diodes
        registry.addPattern(ComponentType.IC, "^PTVS[0-9].*");
        registry.addPattern(ComponentType.ESD_PROTECTION_NEXPERIA, "^PTVS[0-9].*");

        // IP4 series - ESD protection for interfaces (USB, HDMI)
        registry.addPattern(ComponentType.IC, "^IP4[0-9].*");
        registry.addPattern(ComponentType.ESD_PROTECTION_NEXPERIA, "^IP4[0-9].*");

        // ========== Logic ICs ==========
        // 74-series logic families
        registry.addPattern(ComponentType.IC, "^74AHC.*");               // Advanced High-speed CMOS
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74AHC.*");
        registry.addPattern(ComponentType.IC, "^74AHCT.*");              // AHC with TTL inputs
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74AHCT.*");
        registry.addPattern(ComponentType.IC, "^74AUC.*");               // Advanced Ultra-low voltage CMOS
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74AUC.*");
        registry.addPattern(ComponentType.IC, "^74AUP.*");               // Advanced Ultra-low Power
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74AUP.*");
        registry.addPattern(ComponentType.IC, "^74HC[0-9].*");           // High-speed CMOS
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74HC[0-9].*");
        registry.addPattern(ComponentType.IC, "^74HCT[0-9].*");          // HC with TTL inputs
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74HCT[0-9].*");
        registry.addPattern(ComponentType.IC, "^74LVC.*");               // Low-voltage CMOS
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74LVC.*");
        registry.addPattern(ComponentType.IC, "^74LVCH.*");              // LVC with bus hold
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74LVCH.*");
        registry.addPattern(ComponentType.IC, "^74LVT.*");               // Low-voltage BiCMOS
        registry.addPattern(ComponentType.LOGIC_IC_NEXPERIA, "^74LVT.*");

        // ========== Interface ICs ==========
        // PCA series - I2C devices
        registry.addPattern(ComponentType.IC, "^PCA[0-9].*");            // I2C bus controllers, switches
        // PCF series - Interface and control
        registry.addPattern(ComponentType.IC, "^PCF[0-9].*");            // Legacy interface ICs
        // PTN series - Level translators
        registry.addPattern(ComponentType.IC, "^PTN[0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                // Base types
                ComponentType.MOSFET,
                ComponentType.TRANSISTOR,
                ComponentType.DIODE,
                ComponentType.IC,

                // Nexperia-specific types
                ComponentType.MOSFET_NEXPERIA,
                ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA,
                ComponentType.ESD_PROTECTION_NEXPERIA,
                ComponentType.LOGIC_IC_NEXPERIA
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();
        // Remove trailing ,215 or ,315 etc for pattern matching
        String normalizedMpn = normalizeForMatching(upperMpn);

        // MOSFET checks
        boolean isMOSFET = normalizedMpn.matches("^PSMN[0-9].*") ||
                normalizedMpn.matches("^PSMP[0-9].*") ||
                normalizedMpn.matches("^PMV[0-9].*") ||
                normalizedMpn.matches("^BSS[0-9].*") ||
                normalizedMpn.matches("^BUK[0-9].*") ||
                normalizedMpn.startsWith("2N7002");

        if ((type == ComponentType.MOSFET || type == ComponentType.MOSFET_NEXPERIA) && isMOSFET) {
            return true;
        }

        // Transistor checks
        boolean isTransistor = normalizedMpn.matches("^PMBT[0-9].*") ||
                normalizedMpn.matches("^PBSS[0-9].*") ||
                normalizedMpn.matches("^PMP[0-9].*") ||
                normalizedMpn.matches("^PXN[0-9].*") ||
                normalizedMpn.matches("^BC[0-9].*") ||
                normalizedMpn.matches("^BF[0-9].*") ||
                normalizedMpn.matches("^MMBT[0-9].*") ||
                normalizedMpn.startsWith("PN2222") ||
                normalizedMpn.startsWith("2N2222") ||
                normalizedMpn.startsWith("2N3904") ||
                normalizedMpn.startsWith("2N3906");

        if ((type == ComponentType.TRANSISTOR || type == ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA) && isTransistor) {
            return true;
        }

        // Diode checks
        boolean isDiode = normalizedMpn.matches("^PMEG[0-9].*") ||
                normalizedMpn.matches("^BAV[0-9].*") ||
                normalizedMpn.matches("^BAS[0-9].*") ||
                normalizedMpn.matches("^BAT[0-9].*") ||
                normalizedMpn.matches("^BZX[0-9].*") ||
                normalizedMpn.matches("^PZU[0-9].*") ||
                normalizedMpn.startsWith("1N4148") ||
                normalizedMpn.startsWith("1N914");

        if (type == ComponentType.DIODE && isDiode) {
            return true;
        }

        // ESD Protection checks
        boolean isESD = normalizedMpn.matches("^PESD[0-9].*") ||
                normalizedMpn.matches("^PRTR[0-9].*") ||
                normalizedMpn.matches("^PTVS[0-9].*") ||
                normalizedMpn.matches("^IP4[0-9].*");

        if ((type == ComponentType.IC || type == ComponentType.ESD_PROTECTION_NEXPERIA) && isESD) {
            return true;
        }

        // Logic IC checks
        boolean isLogic = normalizedMpn.matches("^74AHC.*") ||
                normalizedMpn.matches("^74AHCT.*") ||
                normalizedMpn.matches("^74AUC.*") ||
                normalizedMpn.matches("^74AUP.*") ||
                normalizedMpn.matches("^74HC[0-9].*") ||
                normalizedMpn.matches("^74HCT[0-9].*") ||
                normalizedMpn.matches("^74LVC.*") ||
                normalizedMpn.matches("^74LVCH.*") ||
                normalizedMpn.matches("^74LVT.*");

        if ((type == ComponentType.IC || type == ComponentType.LOGIC_IC_NEXPERIA) && isLogic) {
            return true;
        }

        // Interface IC checks
        boolean isInterface = normalizedMpn.matches("^PCA[0-9].*") ||
                normalizedMpn.matches("^PCF[0-9].*") ||
                normalizedMpn.matches("^PTN[0-9].*");

        if (type == ComponentType.IC && isInterface) {
            return true;
        }

        // Do NOT fall back to registry - this causes cross-handler false matches
        // Only return true for patterns this handler explicitly supports
        return false;
    }

    /**
     * Normalize MPN for pattern matching by removing tape & reel suffixes.
     * Nexperia uses ",215", ",315", ",235" etc. as packaging codes.
     */
    private String normalizeForMatching(String mpn) {
        // Remove common Nexperia tape & reel suffixes
        if (mpn.contains(",")) {
            return mpn.substring(0, mpn.indexOf(','));
        }
        return mpn;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // First check for Nexperia-specific comma-separated packaging codes
        if (upperMpn.contains(",")) {
            String packagingCode = upperMpn.substring(upperMpn.indexOf(',') + 1);
            String resolved = NEXPERIA_PACKAGES.get(packagingCode);
            if (resolved != null) {
                return resolved;
            }
        }

        String normalizedMpn = normalizeForMatching(upperMpn);

        // Power MOSFET packages (PSMN, PSMP)
        if (normalizedMpn.startsWith("PSMN") || normalizedMpn.startsWith("PSMP")) {
            int lastDigitIndex = findLastDigitIndex(normalizedMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < normalizedMpn.length() - 1) {
                String suffix = normalizedMpn.substring(lastDigitIndex + 1);
                return switch (suffix) {
                    case "T" -> "LFPAK56";
                    case "U" -> "LFPAK88";
                    case "V" -> "LFPAK33";
                    case "B" -> "SOT754";
                    case "PE" -> "LFPAK56E";
                    case "L" -> "TO-220";
                    case "F" -> "TO-220F";
                    case "FI" -> "TO-220F";
                    default -> suffix;
                };
            }
        }

        // Small signal MOSFETs (PMV, BSS, 2N7002)
        if (normalizedMpn.startsWith("PMV") || normalizedMpn.startsWith("BSS") ||
                normalizedMpn.startsWith("2N7002")) {
            int lastDigitIndex = findLastDigitIndex(normalizedMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < normalizedMpn.length() - 1) {
                String suffix = normalizedMpn.substring(lastDigitIndex + 1);
                String resolved = NEXPERIA_PACKAGES.get(suffix);
                return resolved != null ? resolved : suffix;
            }
            // 2N7002 with no suffix is typically SOT23
            if (normalizedMpn.equals("2N7002")) {
                return "SOT23";
            }
        }

        // Small signal transistors (PMBT, PBSS)
        if (normalizedMpn.startsWith("PMBT") || normalizedMpn.startsWith("PBSS")) {
            // PMBT2222A - extract suffix after the "A" or number
            int lastDigitIndex = findLastDigitIndex(normalizedMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < normalizedMpn.length() - 1) {
                String suffix = normalizedMpn.substring(lastDigitIndex + 1);
                // Skip grade letters like A, B, C
                if (suffix.length() == 1 && Character.isLetter(suffix.charAt(0))) {
                    return "SOT23"; // Default for PMBT series
                }
                String resolved = NEXPERIA_PACKAGES.get(suffix);
                return resolved != null ? resolved : suffix;
            }
            return "SOT23"; // Default for PMBT series
        }

        // BC/BF transistors
        if (normalizedMpn.matches("^BC[0-9].*") || normalizedMpn.matches("^BF[0-9].*")) {
            // Check for package suffix after the grade letter
            if (normalizedMpn.length() > 5) {
                String suffix = normalizedMpn.substring(normalizedMpn.length() - 1);
                return switch (suffix) {
                    case "W" -> "SOT323";
                    case "S" -> "SOT363";
                    default -> "";
                };
            }
        }

        // Zener diodes (BZX84-Cxxx)
        if (normalizedMpn.startsWith("BZX")) {
            if (normalizedMpn.contains("-")) {
                // BZX84-C5V1 format
                String prefix = normalizedMpn.substring(0, normalizedMpn.indexOf('-'));
                return switch (prefix) {
                    case "BZX84" -> "SOT23";
                    case "BZX55" -> "DO-35";
                    case "BZX79" -> "DO-35";
                    case "BZX85" -> "DO-41";
                    case "BZX384" -> "SOD323";
                    default -> "";
                };
            }
            // Without hyphen: BZX84xxx, BZX384xxx
            if (normalizedMpn.startsWith("BZX84")) return "SOT23";
            if (normalizedMpn.startsWith("BZX384")) return "SOD323";
        }

        // Signal diodes (BAV, BAS, BAT)
        if (normalizedMpn.matches("^BAV[0-9].*")) {
            if (normalizedMpn.startsWith("BAV99")) return "SOT23";
            if (normalizedMpn.startsWith("BAV70")) return "SOT23";
            return "SOT23"; // Most BAV are SOT23
        }
        if (normalizedMpn.matches("^BAS[0-9].*")) {
            return "SOT23"; // Most BAS are SOT23
        }
        if (normalizedMpn.matches("^BAT[0-9].*")) {
            if (normalizedMpn.startsWith("BAT54")) return "SOT23";
            return "SOT23"; // Most BAT are SOT23
        }

        // Schottky rectifiers (PMEG)
        if (normalizedMpn.startsWith("PMEG")) {
            // PMEG2010AEH - last letters indicate package
            int lastDigitIndex = findLastDigitIndex(normalizedMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < normalizedMpn.length() - 1) {
                String suffix = normalizedMpn.substring(lastDigitIndex + 1);
                return switch (suffix) {
                    case "AEH" -> "SOD123";
                    case "AED" -> "SOD323F";
                    case "BEA" -> "SOD128";
                    case "BEB" -> "CFP3";
                    case "AET" -> "SOD523";
                    default -> suffix;
                };
            }
        }

        // ESD protection (PESD, PRTR)
        if (normalizedMpn.startsWith("PESD")) {
            // PESD5V0S1BL
            if (normalizedMpn.endsWith("BL")) return "SOD882";
            if (normalizedMpn.endsWith("BA")) return "SOD323";
            if (normalizedMpn.endsWith("UB")) return "SOT23";
            if (normalizedMpn.endsWith("UD")) return "SOT323";
        }

        // Logic IC packages
        if (normalizedMpn.startsWith("74")) {
            // 74LVC1G04GW - package at end
            if (normalizedMpn.contains("GW")) return "SOT353";
            if (normalizedMpn.contains("GM")) return "XSON6";
            if (normalizedMpn.contains("BQ")) return "DHVQFN";
            if (normalizedMpn.contains("PW")) return "TSSOP";
            if (normalizedMpn.contains("D")) return "SO14";

            // Try underscore-separated format (74HC00_D)
            if (normalizedMpn.contains("_")) {
                String suffix = normalizedMpn.substring(normalizedMpn.indexOf('_') + 1);
                return switch (suffix) {
                    case "D" -> "SO14";
                    case "DB" -> "SSOP14";
                    case "PW" -> "TSSOP14";
                    case "BQ" -> "DHVQFN14";
                    default -> suffix;
                };
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();
        String normalizedMpn = normalizeForMatching(upperMpn);

        // MOSFETs
        if (normalizedMpn.startsWith("PSMN")) return "PSMN";
        if (normalizedMpn.startsWith("PSMP")) return "PSMP";
        if (normalizedMpn.startsWith("PMV")) return "PMV";
        if (normalizedMpn.startsWith("BSS")) return "BSS";
        if (normalizedMpn.startsWith("PJD")) return "PJD";
        if (normalizedMpn.startsWith("BUK")) return "BUK";
        if (normalizedMpn.startsWith("2N7002")) return "2N7002";

        // Transistors
        if (normalizedMpn.startsWith("PMBT")) return "PMBT";
        if (normalizedMpn.startsWith("PBSS")) return "PBSS";
        if (normalizedMpn.startsWith("PMP")) return "PMP";
        if (normalizedMpn.startsWith("PXN")) return "PXN";
        if (normalizedMpn.startsWith("MMBT")) return "MMBT";
        if (normalizedMpn.matches("^BC5[0-9]{2}.*")) return "BC5xx";
        if (normalizedMpn.matches("^BC8[0-9]{2}.*")) return "BC8xx";
        if (normalizedMpn.matches("^BC[0-9].*")) return "BC";
        if (normalizedMpn.matches("^BF[0-9].*")) return "BF";
        if (normalizedMpn.startsWith("PN2222")) return "PN2222";
        if (normalizedMpn.startsWith("2N2222")) return "2N2222";
        if (normalizedMpn.startsWith("2N3904")) return "2N3904";
        if (normalizedMpn.startsWith("2N3906")) return "2N3906";

        // Diodes
        if (normalizedMpn.startsWith("PMEG")) return "PMEG";
        if (normalizedMpn.matches("^BAV[0-9]{2}.*")) {
            // Extract specific BAV series (BAV99, BAV70)
            return normalizedMpn.substring(0, 5);
        }
        if (normalizedMpn.matches("^BAS[0-9].*")) return "BAS";
        if (normalizedMpn.matches("^BAT[0-9]{2}.*")) {
            // Extract specific BAT series (BAT54, BAT46)
            return normalizedMpn.substring(0, 5);
        }
        if (normalizedMpn.matches("^BZX[0-9]{2}.*")) {
            // Extract BZX series (BZX84, BZX55)
            return normalizedMpn.substring(0, 5);
        }
        if (normalizedMpn.startsWith("PZU")) return "PZU";
        if (normalizedMpn.startsWith("1N4148")) return "1N4148";
        if (normalizedMpn.startsWith("1N914")) return "1N914";

        // ESD Protection
        if (normalizedMpn.startsWith("PESD")) return "PESD";
        if (normalizedMpn.startsWith("PRTR")) return "PRTR";
        if (normalizedMpn.startsWith("PTVS")) return "PTVS";
        if (normalizedMpn.startsWith("IP4")) return "IP4";

        // Logic ICs
        if (normalizedMpn.startsWith("74")) {
            // Extract logic family (AHC, AUC, HC, HCT, LVC, etc.)
            // Check longer prefixes first
            for (String family : new String[]{"AHCT", "AHC", "AUCT", "AUC", "AUP",
                    "LVCH", "LVT", "LVC", "HCT", "HC"}) {
                if (normalizedMpn.startsWith("74" + family)) {
                    return "74" + family;
                }
            }
            return "74";
        }

        // Interface ICs
        if (normalizedMpn.startsWith("PCA")) return "PCA";
        if (normalizedMpn.startsWith("PCF")) return "PCF";
        if (normalizedMpn.startsWith("PTN")) return "PTN";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        String normalized1 = normalizeForMatching(mpn1.toUpperCase());
        String normalized2 = normalizeForMatching(mpn2.toUpperCase());

        // Logic IC cross-family compatibility
        if (series1.startsWith("74")) {
            // Different package variants of same logic function are usually compatible
            String func1 = extractLogicFunction(normalized1);
            String func2 = extractLogicFunction(normalized2);
            if (func1.equals(func2) && !func1.isEmpty()) {
                // HCT is backward compatible with HC
                String family1 = extractLogicFamily(normalized1);
                String family2 = extractLogicFamily(normalized2);
                if ((family1.equals("HC") && family2.equals("HCT")) ||
                        (family1.equals("HCT") && family2.equals("HC"))) {
                    return true;
                }
                // Same family, different packages
                if (family1.equals(family2)) {
                    return true;
                }
            }
        }

        // Transistor equivalents
        if (series1.equals("PMBT") || series1.equals("2N2222") || series1.equals("PN2222")) {
            // PMBT2222A is equivalent to 2N2222 and PN2222
            String base1 = extractTransistorBase(normalized1);
            String base2 = extractTransistorBase(normalized2);
            if (base1.equals(base2)) {
                return true;
            }
        }

        // MOSFET replacements within same voltage/current class
        if (series1.equals("PSMN") || series1.equals("PSMP") || series1.equals("PMV")) {
            // Same base part, different package
            String base1 = extractMosfetBase(normalized1);
            String base2 = extractMosfetBase(normalized2);
            return base1.equals(base2);
        }

        return false;
    }

    private String extractLogicFunction(String mpn) {
        // Extract the actual logic function number from 74xxx parts
        // Example: 74HC00 -> "00", 74HCT04 -> "04", 74LVC1G04 -> "04"
        int startIndex = mpn.indexOf("74") + 2;
        // Skip family letters
        while (startIndex < mpn.length() && Character.isLetter(mpn.charAt(startIndex))) {
            startIndex++;
        }
        // Handle 1Gxx format (single gate)
        if (startIndex < mpn.length() && mpn.charAt(startIndex) == '1' &&
                startIndex + 1 < mpn.length() && mpn.charAt(startIndex + 1) == 'G') {
            startIndex += 2;
        }
        int endIndex = startIndex;
        while (endIndex < mpn.length() && Character.isDigit(mpn.charAt(endIndex))) {
            endIndex++;
        }
        return startIndex < endIndex ? mpn.substring(startIndex, endIndex) : "";
    }

    private String extractLogicFamily(String mpn) {
        // Extract family from 74xxx (AHC, HCT, HC, LVC, etc.)
        if (!mpn.startsWith("74")) return "";
        String rest = mpn.substring(2);
        for (String family : new String[]{"AHCT", "AHC", "AUCT", "AUC", "AUP",
                "LVCH", "LVT", "LVC", "HCT", "HC"}) {
            if (rest.startsWith(family)) {
                return family;
            }
        }
        return "";
    }

    private String extractTransistorBase(String mpn) {
        // Normalize transistor to base type (2222, 3904, 3906)
        if (mpn.contains("2222")) return "2222";
        if (mpn.contains("3904")) return "3904";
        if (mpn.contains("3906")) return "3906";
        // For PMBT series, extract the number
        if (mpn.startsWith("PMBT")) {
            return mpn.substring(4).replaceAll("[A-Z]$", "");
        }
        return mpn;
    }

    private String extractMosfetBase(String mpn) {
        // Extract base MOSFET identifier (before package suffix)
        int lastDigitIndex = findLastDigitIndex(mpn);
        if (lastDigitIndex >= 0) {
            return mpn.substring(0, lastDigitIndex + 1);
        }
        return mpn;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
