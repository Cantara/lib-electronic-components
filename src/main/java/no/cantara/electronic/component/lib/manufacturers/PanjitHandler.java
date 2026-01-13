package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for Panjit International (Taiwan semiconductor manufacturer).
 *
 * Panjit manufactures discrete semiconductors including:
 * - Standard rectifier diodes (1N4xxx, 1N5xxx series)
 * - Fast recovery diodes (ES/RS series)
 * - Schottky diodes (SS/SK series)
 * - Signal diodes (BAV/BAS/BAT series)
 * - SMD transistors (MMBT series)
 * - Through-hole transistors (2N series)
 * - MOSFETs (PJ series)
 *
 * Package codes are typically suffixes:
 * - G = DO-41 (through-hole)
 * - W = SOD-123 (SMD)
 * - A = SOT-23 (SMD)
 * - TR = Tape and reel
 */
public class PanjitHandler implements ManufacturerHandler {

    // Package code mappings from suffix to package name
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("G", "DO-41"),
            Map.entry("GP", "DO-41"),
            Map.entry("W", "SOD-123"),
            Map.entry("WS", "SOD-323"),
            Map.entry("A", "SOT-23"),
            Map.entry("LT", "SOT-23"),
            Map.entry("WT", "SOT-23"),
            Map.entry("F", "SMAF"),
            Map.entry("S", "SMA"),
            Map.entry("B", "SMB"),
            Map.entry("C", "SMC"),
            Map.entry("FL", "SOD-123FL"),
            Map.entry("T", "TO-220"),
            Map.entry("TP", "TO-220F"),
            Map.entry("N", "TO-92"),
            Map.entry("NL", "TO-92")
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Standard rectifier diodes (1N4xxx series)
        // Examples: 1N4001G, 1N4007G, 1N4007W
        registry.addPattern(ComponentType.DIODE, "^1N4[0-9]{3}.*");

        // Signal diodes
        // Examples: 1N4148W, 1N4148WS
        registry.addPattern(ComponentType.DIODE, "^1N4148.*");
        registry.addPattern(ComponentType.DIODE, "^1N914.*");

        // Power rectifier diodes (1N5xxx series)
        // Examples: 1N5400G, 1N5408G, 1N5819
        registry.addPattern(ComponentType.DIODE, "^1N5[0-9]{3}.*");

        // Fast recovery diodes (ES series)
        // Examples: ES1J, ES2J, ES1D
        registry.addPattern(ComponentType.DIODE, "^ES[1-3][A-Z].*");

        // Fast recovery diodes (RS series)
        // Examples: RS1M, RS2M, RS1G
        registry.addPattern(ComponentType.DIODE, "^RS[1-3][A-Z].*");

        // Ultra-fast recovery (US/UF series)
        // Examples: US1M, UF4007
        registry.addPattern(ComponentType.DIODE, "^US[1-3][A-Z].*");
        registry.addPattern(ComponentType.DIODE, "^UF[0-9]{4}.*");

        // Schottky diodes (SS series - low voltage)
        // Examples: SS12, SS14, SS24, SS34
        registry.addPattern(ComponentType.DIODE, "^SS[1-3][0-9].*");

        // Schottky diodes (SK series - higher current)
        // Examples: SK52, SK54, SK56
        registry.addPattern(ComponentType.DIODE, "^SK[3-5][0-9].*");

        // Schottky barrier diodes (SB series)
        // Examples: SB140, SB360, SB560
        registry.addPattern(ComponentType.DIODE, "^SB[1-5][0-9]{2}.*");

        // Schottky barrier rectifier (MBR series)
        // Examples: MBR340, MBR360, MBR1045
        registry.addPattern(ComponentType.DIODE, "^MBR[0-9]{3,4}.*");

        // BAV signal diodes
        // Examples: BAV99, BAV70, BAV21
        registry.addPattern(ComponentType.DIODE, "^BAV[0-9]{2}.*");

        // BAS signal/switching diodes
        // Examples: BAS16, BAS21
        registry.addPattern(ComponentType.DIODE, "^BAS[0-9]{2}.*");

        // BAT Schottky diodes
        // Examples: BAT54, BAT54S, BAT54C
        registry.addPattern(ComponentType.DIODE, "^BAT[0-9]{2}.*");

        // Zener diodes (BZX series)
        // Examples: BZX84C5V1, BZX55C3V3
        registry.addPattern(ComponentType.DIODE, "^BZX[0-9]{2}.*");

        // TVS diodes (SMBJ/SMAJ series)
        // Examples: SMBJ5.0A, SMAJ5.0A
        registry.addPattern(ComponentType.DIODE, "^SMB[AJ][0-9].*");
        registry.addPattern(ComponentType.DIODE, "^SMA[J][0-9].*");

        // SMD transistors (MMBT series)
        // Examples: MMBT2222A, MMBT3904, MMBT3906
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT[0-9]{4}.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT[0-9]{3}.*");

        // MPSA series transistors
        // Examples: MPSA42, MPSA92, MPSA06
        registry.addPattern(ComponentType.TRANSISTOR, "^MPSA[0-9]{2}.*");

        // 2N series transistors
        // Examples: 2N2222A, 2N3904, 2N3906, 2N7002
        registry.addPattern(ComponentType.TRANSISTOR, "^2N[0-9]{4}.*");

        // BC series transistors
        // Examples: BC847, BC857
        registry.addPattern(ComponentType.TRANSISTOR, "^BC[0-9]{3}.*");

        // PN series transistors
        // Examples: PN2222A, PN2907A
        registry.addPattern(ComponentType.TRANSISTOR, "^PN[0-9]{4}.*");

        // 2N7002 is actually a MOSFET, not a transistor
        registry.addPattern(ComponentType.MOSFET, "^2N7002.*");

        // BSS MOSFETs
        // Examples: BSS138, BSS84
        registry.addPattern(ComponentType.MOSFET, "^BSS[0-9]{2,3}.*");

        // PJ series MOSFETs (Panjit proprietary series)
        // Examples: PJ2308, PJ3415, PJ4435
        registry.addPattern(ComponentType.MOSFET, "^PJ[0-9]{4}.*");

        // SI series MOSFETs
        // Examples: SI2301, SI2302
        registry.addPattern(ComponentType.MOSFET, "^SI[0-9]{4}.*");

        // AO series MOSFETs
        // Examples: AO3400, AO3401
        registry.addPattern(ComponentType.MOSFET, "^AO[0-9]{4}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.DIODE,
                ComponentType.TRANSISTOR,
                ComponentType.MOSFET
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Diode patterns
        if (type == ComponentType.DIODE) {
            // Standard rectifiers
            if (upperMpn.matches("^1N4[0-9]{3}.*")) return true;
            if (upperMpn.matches("^1N5[0-9]{3}.*")) return true;
            // Signal diodes
            if (upperMpn.matches("^1N4148.*")) return true;
            if (upperMpn.matches("^1N914.*")) return true;
            // Fast recovery
            if (upperMpn.matches("^ES[1-3][A-Z].*")) return true;
            if (upperMpn.matches("^RS[1-3][A-Z].*")) return true;
            if (upperMpn.matches("^US[1-3][A-Z].*")) return true;
            if (upperMpn.matches("^UF[0-9]{4}.*")) return true;
            // Schottky
            if (upperMpn.matches("^SS[1-3][0-9].*")) return true;
            if (upperMpn.matches("^SK[3-5][0-9].*")) return true;
            if (upperMpn.matches("^SB[1-5][0-9]{2}.*")) return true;
            if (upperMpn.matches("^MBR[0-9]{3,4}.*")) return true;
            // BAV/BAS/BAT signal
            if (upperMpn.matches("^BAV[0-9]{2}.*")) return true;
            if (upperMpn.matches("^BAS[0-9]{2}.*")) return true;
            if (upperMpn.matches("^BAT[0-9]{2}.*")) return true;
            // Zener
            if (upperMpn.matches("^BZX[0-9]{2}.*")) return true;
            // TVS
            if (upperMpn.matches("^SMB[AJ][0-9].*")) return true;
            if (upperMpn.matches("^SMA[J][0-9].*")) return true;
        }

        // Transistor patterns (but NOT 2N7002 which is a MOSFET)
        if (type == ComponentType.TRANSISTOR) {
            // Exclude 2N7002 which is a MOSFET
            if (upperMpn.startsWith("2N7002")) return false;
            if (upperMpn.matches("^MMBT[0-9]{3,4}.*")) return true;
            if (upperMpn.matches("^MPSA[0-9]{2}.*")) return true;
            if (upperMpn.matches("^2N[0-9]{4}.*")) return true;
            if (upperMpn.matches("^BC[0-9]{3}.*")) return true;
            if (upperMpn.matches("^PN[0-9]{4}.*")) return true;
        }

        // MOSFET patterns
        if (type == ComponentType.MOSFET) {
            if (upperMpn.matches("^2N7002.*")) return true;
            if (upperMpn.matches("^BSS[0-9]{2,3}.*")) return true;
            if (upperMpn.matches("^PJ[0-9]{4}.*")) return true;
            if (upperMpn.matches("^SI[0-9]{4}.*")) return true;
            if (upperMpn.matches("^AO[0-9]{4}.*")) return true;
        }

        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // For 1N series diodes, check common suffixes
        if (upperMpn.matches("^1N[0-9]{4}.*")) {
            return extractSuffixPackage(upperMpn);
        }

        // For signal diodes (1N4148, 1N914)
        if (upperMpn.matches("^1N4148.*") || upperMpn.matches("^1N914.*")) {
            return extractSuffixPackage(upperMpn);
        }

        // For ES/RS/US fast recovery diodes - typically SMD
        if (upperMpn.matches("^[EU]S[1-3][A-Z].*") || upperMpn.matches("^RS[1-3][A-Z].*")) {
            // These are typically DO-214 (SMA/SMB/SMC)
            if (upperMpn.contains("-F")) return "SMAF";
            if (upperMpn.contains("-S")) return "SMA";
            if (upperMpn.contains("-B")) return "SMB";
            return "SMA";  // Default for ES/RS series
        }

        // For SS/SK Schottky diodes
        if (upperMpn.matches("^SS[1-3][0-9].*") || upperMpn.matches("^SK[3-5][0-9].*")) {
            if (upperMpn.contains("-F")) return "SMAF";
            return "DO-214AC";  // SMA package typical for SS/SK series
        }

        // For BAV/BAS/BAT signal diodes
        if (upperMpn.matches("^BA[VTS][0-9]{2}.*")) {
            if (upperMpn.contains("LT") || upperMpn.contains("WT")) return "SOT-23";
            return "SOT-23";  // Most common package for these series
        }

        // For MMBT transistors
        if (upperMpn.startsWith("MMBT")) {
            if (upperMpn.contains("LT")) return "SOT-23";
            return "SOT-23";  // Standard package for MMBT series
        }

        // For 2N series transistors
        if (upperMpn.matches("^2N[0-9]{4}.*")) {
            // 2N7002 is a MOSFET in SOT-23
            if (upperMpn.startsWith("2N7002")) return "SOT-23";
            return extractSuffixPackage(upperMpn);
        }

        // For BC series transistors
        if (upperMpn.matches("^BC[0-9]{3}.*")) {
            return "SOT-23";  // Most common SMD package
        }

        // For PJ series MOSFETs
        if (upperMpn.matches("^PJ[0-9]{4}.*")) {
            // Package code is typically after the number
            return extractSuffixPackage(upperMpn);
        }

        // For BSS MOSFETs
        if (upperMpn.startsWith("BSS")) {
            return "SOT-23";  // Standard package for BSS series
        }

        return "";
    }

    /**
     * Extract package code from common suffix patterns
     */
    private String extractSuffixPackage(String mpn) {
        // Remove any trailing whitespace or tape/reel indicators
        String normalized = mpn.replaceAll("[-/]TR$", "").trim();

        // Check for known package suffixes (longer ones first to avoid partial matches)
        for (String suffix : new String[]{"GP", "WS", "FL", "NL", "TP", "LT", "WT"}) {
            if (normalized.endsWith(suffix)) {
                return PACKAGE_CODES.getOrDefault(suffix, suffix);
            }
        }

        // Check single-letter suffixes
        for (String suffix : new String[]{"G", "W", "A", "F", "S", "B", "C", "T", "N"}) {
            if (normalized.endsWith(suffix)) {
                return PACKAGE_CODES.getOrDefault(suffix, suffix);
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Signal diodes (check specific before generic)
        if (upperMpn.matches("^1N4148.*")) return "1N4148";
        if (upperMpn.matches("^1N914.*")) return "1N914";

        // Rectifier series
        if (upperMpn.matches("^1N4[0-9]{3}.*")) return "1N4000";
        if (upperMpn.matches("^1N5[0-9]{3}.*")) {
            // Check for Schottky (1N58xx) vs rectifier (1N54xx)
            if (upperMpn.matches("^1N58[0-9]{2}.*")) return "1N5800";  // Schottky
            return "1N5400";  // Standard power rectifier
        }

        // Fast recovery diodes
        if (upperMpn.matches("^ES[1-3].*")) return "ES";
        if (upperMpn.matches("^RS[1-3].*")) return "RS";
        if (upperMpn.matches("^US[1-3].*")) return "US";
        if (upperMpn.matches("^UF[0-9].*")) return "UF";

        // Schottky diodes
        if (upperMpn.matches("^SS[1-3][0-9].*")) return "SS";
        if (upperMpn.matches("^SK[3-5][0-9].*")) return "SK";
        if (upperMpn.matches("^SB[1-5][0-9]{2}.*")) return "SB";
        if (upperMpn.matches("^MBR[0-9].*")) return "MBR";

        // Signal diodes
        if (upperMpn.matches("^BAV[0-9]{2}.*")) return "BAV";
        if (upperMpn.matches("^BAS[0-9]{2}.*")) return "BAS";
        if (upperMpn.matches("^BAT[0-9]{2}.*")) return "BAT";

        // Zener diodes
        if (upperMpn.matches("^BZX[0-9]{2}.*")) return "BZX";

        // TVS diodes
        if (upperMpn.matches("^SMB[AJ].*")) return "SMBJ";
        if (upperMpn.matches("^SMA[J].*")) return "SMAJ";

        // Transistors
        if (upperMpn.matches("^MMBT[0-9].*")) return "MMBT";
        if (upperMpn.matches("^MPSA[0-9].*")) return "MPSA";
        if (upperMpn.matches("^2N[0-9].*")) return "2N";
        if (upperMpn.matches("^BC[0-9].*")) return "BC";
        if (upperMpn.matches("^PN[0-9].*")) return "PN";

        // MOSFETs
        if (upperMpn.matches("^BSS[0-9].*")) return "BSS";
        if (upperMpn.matches("^PJ[0-9].*")) return "PJ";
        if (upperMpn.matches("^SI[0-9].*")) return "SI";
        if (upperMpn.matches("^AO[0-9].*")) return "AO";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are generally not replaceable
        if (!series1.equals(series2)) {
            // Exception: 1N4148 and 1N914 are equivalent
            if ((series1.equals("1N4148") && series2.equals("1N914")) ||
                (series1.equals("1N914") && series2.equals("1N4148"))) {
                return true;
            }
            return false;
        }

        // 1N4000 series - higher voltage can replace lower
        if (series1.equals("1N4000")) {
            int voltage1 = extractDiodeVoltageRating(mpn1);
            int voltage2 = extractDiodeVoltageRating(mpn2);
            return voltage1 >= voltage2;
        }

        // 1N5400 series - higher voltage can replace lower
        if (series1.equals("1N5400")) {
            int voltage1 = extractDiodeVoltageRating(mpn1);
            int voltage2 = extractDiodeVoltageRating(mpn2);
            return voltage1 >= voltage2;
        }

        // SS series Schottky - higher current/voltage can replace lower
        if (series1.equals("SS")) {
            int rating1 = extractSchottkyRating(mpn1);
            int rating2 = extractSchottkyRating(mpn2);
            return rating1 >= rating2;
        }

        // MMBT transistors - check if same base type
        if (series1.equals("MMBT")) {
            String base1 = extractBaseNumber(mpn1);
            String base2 = extractBaseNumber(mpn2);
            return base1.equals(base2);
        }

        // 2N transistors
        if (series1.equals("2N")) {
            // MMBT variant can replace 2N and vice versa
            String base1 = extractBaseNumber(mpn1);
            String base2 = extractBaseNumber(mpn2);
            return base1.equals(base2);
        }

        // Same series with same package - generally replaceable
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        return !pkg1.isEmpty() && pkg1.equals(pkg2);
    }

    /**
     * Extract voltage rating from 1N4xxx or 1N5xxx diode
     */
    private int extractDiodeVoltageRating(String mpn) {
        String upperMpn = mpn.toUpperCase();

        // 1N400x series: last digit indicates voltage
        if (upperMpn.matches("^1N400[1-7].*")) {
            char lastDigit = upperMpn.charAt(5);
            return switch (lastDigit) {
                case '1' -> 50;    // 1N4001
                case '2' -> 100;   // 1N4002
                case '3' -> 200;   // 1N4003
                case '4' -> 400;   // 1N4004
                case '5' -> 600;   // 1N4005
                case '6' -> 800;   // 1N4006
                case '7' -> 1000;  // 1N4007
                default -> 0;
            };
        }

        // 1N540x series: last digit indicates voltage
        if (upperMpn.matches("^1N540[1-8].*")) {
            char lastDigit = upperMpn.charAt(5);
            return switch (lastDigit) {
                case '1' -> 100;   // 1N5401
                case '2' -> 200;   // 1N5402
                case '4' -> 400;   // 1N5404
                case '6' -> 600;   // 1N5406
                case '7' -> 800;   // 1N5407
                case '8' -> 1000;  // 1N5408
                default -> 0;
            };
        }

        return 0;
    }

    /**
     * Extract rating from SS series Schottky diodes
     * SS[current][voltage] e.g., SS14 = 1A 40V, SS34 = 3A 40V
     */
    private int extractSchottkyRating(String mpn) {
        String upperMpn = mpn.toUpperCase();
        if (upperMpn.matches("^SS[1-3][0-9].*")) {
            try {
                // First digit is current (1-3A), second is voltage code
                int current = Character.getNumericValue(upperMpn.charAt(2));
                int voltageCode = Character.getNumericValue(upperMpn.charAt(3));
                // Combine into a single rating for comparison
                return current * 100 + voltageCode * 10;
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * Extract base number from transistor MPN
     */
    private String extractBaseNumber(String mpn) {
        String upperMpn = mpn.toUpperCase();

        // MMBT2222A -> 2222
        if (upperMpn.startsWith("MMBT")) {
            return upperMpn.substring(4).replaceAll("[^0-9].*", "");
        }

        // 2N2222A -> 2222
        if (upperMpn.startsWith("2N")) {
            return upperMpn.substring(2).replaceAll("[^0-9].*", "");
        }

        // PN2222A -> 2222
        if (upperMpn.startsWith("PN")) {
            return upperMpn.substring(2).replaceAll("[^0-9].*", "");
        }

        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
