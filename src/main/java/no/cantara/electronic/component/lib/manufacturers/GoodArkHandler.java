package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for Good-Ark Semiconductor components.
 * Good-Ark is a Chinese manufacturer specializing in diodes, rectifiers, and transistors.
 *
 * Product lines:
 * - 1N4xxx: Standard rectifiers (1N4001-1N4007, 1N4148, 1N4448)
 * - 1N5xxx: Power rectifiers (1N5400-1N5408, 1N5817-1N5819)
 * - ES/US series: Fast recovery diodes (ES1J, ES2D, US1M, US2G)
 * - SS/SK series: Schottky diodes (SS14, SS34, SK34, SK56)
 * - MMBT series: SMD transistors (MMBT2222, MMBT3904, MMBT3906)
 * - 2N series: Standard transistors (2N2222, 2N3904, 2N3906)
 * - BAV/BAT series: Signal diodes (BAV21, BAT54, BAT85)
 */
public class GoodArkHandler implements ManufacturerHandler {

    // Package code mappings for common suffixes
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            // Through-hole diode packages
            Map.entry("A", "DO-41"),           // Standard axial lead
            Map.entry("B", "DO-15"),           // Medium current axial
            Map.entry("G", "DO-35"),           // Small signal diode
            Map.entry("RL", "DO-201AD"),       // Power rectifier
            Map.entry("TAP", "DO-41"),         // Tape and ammo pack

            // Surface mount diode packages
            Map.entry("S", "DO-214AA"),        // SMB package
            Map.entry("F", "DO-214AB"),        // SMC package
            Map.entry("FA", "DO-214AC"),       // SMA package
            Map.entry("FL", "SOD-123FL"),      // Flat lead SOD-123
            Map.entry("W", "SOD-123"),         // SOD-123 package

            // Through-hole transistor packages
            Map.entry("TO", "TO-92"),          // TO-92 plastic
            Map.entry("TA", "TO-92"),          // TO-92 alternate suffix
            Map.entry("TF", "TO-92F"),         // TO-92 flat package
            Map.entry("E", "TO-92"),           // EBC pinout variant

            // Surface mount transistor packages
            Map.entry("LT1", "SOT-23"),        // SOT-23 3-pin
            Map.entry("LT", "SOT-23"),         // SOT-23 3-pin
            Map.entry("SMD", "SOT-23"),        // Generic SMD marking
            Map.entry("K", "SOT-23"),          // SOT-23 marking
            Map.entry("G3", "SOT-323"),        // SOT-323 package
            Map.entry("F5", "SOT-23-5"),       // SOT-23-5 5-pin
            Map.entry("T1", "SOT-416"),        // SOT-416 package
            Map.entry("T", "SOT-523"),         // SOT-523 mini package

            // Power packages
            Map.entry("CT", "TO-220"),         // TO-220 package
            Map.entry("TU", "TO-220F"),        // TO-220F isolated
            Map.entry("D", "TO-252"),          // DPAK/TO-252
            Map.entry("D2", "TO-263"),         // D2PAK/TO-263
            Map.entry("D3", "TO-268")          // D3PAK/TO-268
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.DIODE,
                ComponentType.TRANSISTOR
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // ===== DIODE PATTERNS =====

        // Standard rectifier diodes (1N400x series)
        registry.addPattern(ComponentType.DIODE, "^1N400[1-7].*");        // 1N4001-1N4007

        // Signal diodes
        registry.addPattern(ComponentType.DIODE, "^1N4148.*");            // Small signal diode
        registry.addPattern(ComponentType.DIODE, "^1N4448.*");            // High-speed signal diode
        registry.addPattern(ComponentType.DIODE, "^1N914.*");             // Signal diode equivalent

        // Zener diodes
        registry.addPattern(ComponentType.DIODE, "^1N47[0-9]{2}.*");      // 1N47xx Zener series

        // Power rectifiers (1N54xx series)
        registry.addPattern(ComponentType.DIODE, "^1N540[0-8].*");        // 1N5400-1N5408 (3A rectifiers)
        registry.addPattern(ComponentType.DIODE, "^1N58[1][7-9].*");      // 1N5817-1N5819 (1A Schottky)
        registry.addPattern(ComponentType.DIODE, "^1N58[2][0-5].*");      // 1N5820-1N5825 (3A Schottky)

        // ES/EF series: Ultra-fast recovery diodes
        registry.addPattern(ComponentType.DIODE, "^ES[12][A-Z].*");       // ES1J, ES1D, ES2D, etc.
        registry.addPattern(ComponentType.DIODE, "^EF[12][A-Z].*");       // EF series fast recovery

        // US series: Ultra-fast recovery diodes
        registry.addPattern(ComponentType.DIODE, "^US[12][A-Z].*");       // US1M, US1G, US2G, etc.
        registry.addPattern(ComponentType.DIODE, "^UF[0-9]+.*");          // UF series ultra-fast

        // SS series: Small Schottky diodes (various current ratings)
        registry.addPattern(ComponentType.DIODE, "^SS[1-5][0-9].*");      // SS14, SS24, SS34, SS54, SS110, SS120

        // SK series: Schottky diodes (various current ratings)
        registry.addPattern(ComponentType.DIODE, "^SK[1-5][0-9].*");      // SK34, SK36, SK54, SK56, etc.

        // SB series: Schottky barrier diodes
        registry.addPattern(ComponentType.DIODE, "^SB[1-5][0-9]{2}.*");   // SB140, SB160, SB360, etc.

        // BAV series: Small signal diodes
        registry.addPattern(ComponentType.DIODE, "^BAV[0-9]+.*");         // BAV21, BAV70, BAV99, etc.

        // BAT series: Schottky barrier diodes
        registry.addPattern(ComponentType.DIODE, "^BAT[0-9]+.*");         // BAT54, BAT85, BAT46, etc.

        // TVS diodes
        registry.addPattern(ComponentType.DIODE, "^SMBJ[0-9]+.*");        // SMB TVS diodes
        registry.addPattern(ComponentType.DIODE, "^SMAJ[0-9]+.*");        // SMA TVS diodes
        registry.addPattern(ComponentType.DIODE, "^P[46]KE[0-9]+.*");     // P4KE/P6KE TVS diodes

        // Bridge rectifiers
        registry.addPattern(ComponentType.DIODE, "^MB[1-6]S.*");          // Mini bridge rectifiers
        registry.addPattern(ComponentType.DIODE, "^DB[1-6]S.*");          // Dual mini bridges

        // ===== TRANSISTOR PATTERNS =====

        // 2N series: Standard through-hole transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^2N2222.*");       // NPN general purpose
        registry.addPattern(ComponentType.TRANSISTOR, "^2N2907.*");       // PNP general purpose
        registry.addPattern(ComponentType.TRANSISTOR, "^2N3904.*");       // NPN low power
        registry.addPattern(ComponentType.TRANSISTOR, "^2N3906.*");       // PNP low power
        registry.addPattern(ComponentType.TRANSISTOR, "^2N4401.*");       // NPN high gain
        registry.addPattern(ComponentType.TRANSISTOR, "^2N4403.*");       // PNP high gain
        registry.addPattern(ComponentType.TRANSISTOR, "^2N5401.*");       // PNP high voltage
        registry.addPattern(ComponentType.TRANSISTOR, "^2N5551.*");       // NPN high voltage
        registry.addPattern(ComponentType.TRANSISTOR, "^2N706.*");        // NPN switching
        registry.addPattern(ComponentType.TRANSISTOR, "^2N[0-9]{4}.*");   // Generic 2N series

        // MMBT series: SMD transistors (SOT-23)
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT2222.*");     // SMD 2N2222 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT2907.*");     // SMD 2N2907 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT3904.*");     // SMD 2N3904 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT3906.*");     // SMD 2N3906 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT4401.*");     // SMD 2N4401 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT4403.*");     // SMD 2N4403 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT5401.*");     // SMD 2N5401 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT5551.*");     // SMD 2N5551 equivalent
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT[0-9]+.*");   // Generic MMBT series

        // MMBTA series: SMD transistors with A suffix
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBTA[0-9]+.*"); // MMBTA42, MMBTA92, etc.

        // PN series: Plastic package transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PN2222.*");       // Plastic 2N2222
        registry.addPattern(ComponentType.TRANSISTOR, "^PN2907.*");       // Plastic 2N2907

        // MPSA series: Low power transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^MPSA[0-9]+.*");   // MPSA42, MPSA92, etc.

        // BC series: European standard transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^BC[0-9]{3}.*");   // BC547, BC557, BC337, etc.

        // BF series: High frequency transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^BF[0-9]+.*");     // BF245, BF199, etc.

        // S8050/S8550 series: Common Asia-market transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^S8050.*");        // NPN general purpose
        registry.addPattern(ComponentType.TRANSISTOR, "^S8550.*");        // PNP general purpose
        registry.addPattern(ComponentType.TRANSISTOR, "^S9012.*");        // PNP low noise
        registry.addPattern(ComponentType.TRANSISTOR, "^S9013.*");        // NPN low noise
        registry.addPattern(ComponentType.TRANSISTOR, "^S9014.*");        // NPN low noise
        registry.addPattern(ComponentType.TRANSISTOR, "^S9015.*");        // PNP low noise
        registry.addPattern(ComponentType.TRANSISTOR, "^S9018.*");        // NPN high frequency
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct matching for diodes
        if (type == ComponentType.DIODE) {
            // 1N series diodes
            if (upperMpn.matches("^1N400[1-7].*") ||             // 1N400x rectifiers
                    upperMpn.matches("^1N4148.*") ||              // Signal diode
                    upperMpn.matches("^1N4448.*") ||              // High-speed signal
                    upperMpn.matches("^1N914.*") ||               // Signal diode
                    upperMpn.matches("^1N47[0-9]{2}.*") ||        // Zener diodes
                    upperMpn.matches("^1N540[0-8].*") ||          // 1N54xx power rectifiers
                    upperMpn.matches("^1N58[0-9]{2}.*")) {        // 1N58xx Schottky
                return true;
            }

            // Fast recovery diodes
            if (upperMpn.matches("^ES[12][A-Z].*") ||             // ES series
                    upperMpn.matches("^EF[12][A-Z].*") ||         // EF series
                    upperMpn.matches("^US[12][A-Z].*") ||         // US series
                    upperMpn.matches("^UF[0-9]+.*")) {            // UF series
                return true;
            }

            // Schottky diodes
            if (upperMpn.matches("^SS[1-5][0-9].*") ||            // SS series
                    upperMpn.matches("^SK[1-5][0-9].*") ||        // SK series
                    upperMpn.matches("^SB[1-5][0-9]{2}.*")) {     // SB series
                return true;
            }

            // BAV/BAT series
            if (upperMpn.matches("^BAV[0-9]+.*") ||               // BAV signal diodes
                    upperMpn.matches("^BAT[0-9]+.*")) {           // BAT Schottky
                return true;
            }

            // TVS diodes
            if (upperMpn.matches("^SMBJ[0-9]+.*") ||              // SMBJ TVS
                    upperMpn.matches("^SMAJ[0-9]+.*") ||          // SMAJ TVS
                    upperMpn.matches("^P[46]KE[0-9]+.*")) {       // P4KE/P6KE TVS
                return true;
            }

            // Bridge rectifiers
            if (upperMpn.matches("^MB[1-6]S.*") ||                // MB bridges
                    upperMpn.matches("^DB[1-6]S.*")) {            // DB bridges
                return true;
            }
        }

        // Direct matching for transistors
        if (type == ComponentType.TRANSISTOR) {
            // 2N series
            if (upperMpn.matches("^2N[0-9]{3,4}.*")) {
                return true;
            }

            // MMBT/MMBTA series (SMD)
            if (upperMpn.matches("^MMBT[A]?[0-9]+.*")) {
                return true;
            }

            // PN series
            if (upperMpn.matches("^PN2[0-9]{3}.*")) {
                return true;
            }

            // MPSA series
            if (upperMpn.matches("^MPSA[0-9]+.*")) {
                return true;
            }

            // BC/BF series
            if (upperMpn.matches("^BC[0-9]{3}.*") ||
                    upperMpn.matches("^BF[0-9]+.*")) {
                return true;
            }

            // S80xx/S9xxx series
            if (upperMpn.matches("^S8[05][0-9]{2}.*") ||
                    upperMpn.matches("^S90[0-9]{2}.*")) {
                return true;
            }
        }

        // Fall back to pattern registry
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // MMBT series - typically SOT-23
        if (upperMpn.startsWith("MMBT")) {
            // Check for specific package suffix
            if (upperMpn.endsWith("LT1") || upperMpn.endsWith("LT")) return "SOT-23";
            if (upperMpn.endsWith("G3")) return "SOT-323";
            if (upperMpn.endsWith("F5")) return "SOT-23-5";
            return "SOT-23";  // Default for MMBT
        }

        // MMBTA series - typically SOT-23
        if (upperMpn.startsWith("MMBTA")) {
            return "SOT-23";
        }

        // 2N series - typically TO-92
        if (upperMpn.matches("^2N[0-9]{3,4}.*")) {
            if (upperMpn.endsWith("A") || upperMpn.endsWith("TA")) return "TO-92";
            return "TO-92";  // Default for 2N series
        }

        // PN series - typically TO-92
        if (upperMpn.matches("^PN[0-9]{4}.*")) {
            return "TO-92";
        }

        // BC series - check suffix
        if (upperMpn.matches("^BC[0-9]{3}.*")) {
            if (upperMpn.contains("SMD") || upperMpn.endsWith("LT1")) return "SOT-23";
            return "TO-92";
        }

        // S80xx/S90xx series
        if (upperMpn.matches("^S[89][0-9]{3}.*")) {
            if (upperMpn.contains("SMD")) return "SOT-23";
            return "TO-92";
        }

        // 1N series diodes
        if (upperMpn.matches("^1N400[1-7].*")) {
            // Check suffix for package type
            if (upperMpn.endsWith("RL") || upperMpn.endsWith("RLG")) return "DO-201AD";
            if (upperMpn.endsWith("G")) return "DO-41";
            return "DO-41";  // Default
        }

        // 1N4148/1N4448/1N914 - signal diodes
        if (upperMpn.matches("^1N4148.*") ||
                upperMpn.matches("^1N4448.*") ||
                upperMpn.matches("^1N914.*")) {
            if (upperMpn.endsWith("W") || upperMpn.contains("W-")) return "SOD-123";
            if (upperMpn.endsWith("WT")) return "SOD-523";
            if (upperMpn.endsWith("TR")) return "SOD-323";
            return "DO-35";  // Default
        }

        // 1N54xx power rectifiers
        if (upperMpn.matches("^1N540[0-8].*")) {
            return "DO-201AD";
        }

        // 1N58xx Schottky diodes
        if (upperMpn.matches("^1N58[0-9]{2}.*")) {
            return "DO-41";
        }

        // ES/US fast recovery diodes
        if (upperMpn.matches("^[EU]S[12][A-Z].*")) {
            // Typically SMA package (DO-214AC)
            return "SMA";
        }

        // SS Schottky series
        if (upperMpn.matches("^SS[12][0-9].*")) {
            return "SMA";  // Typically SMA package
        }

        // SK Schottky series
        if (upperMpn.matches("^SK[1-5][0-9].*")) {
            return "SMB";  // Typically SMB package
        }

        // SB Schottky barrier
        if (upperMpn.matches("^SB[1-5][0-9]{2}.*")) {
            return "DO-41";
        }

        // BAV series signal diodes
        if (upperMpn.matches("^BAV[0-9]+.*")) {
            if (upperMpn.contains("70") || upperMpn.contains("99")) return "SOT-23";
            return "SOD-323";
        }

        // BAT series Schottky
        if (upperMpn.matches("^BAT[0-9]+.*")) {
            return "SOT-23";  // Most common for BAT54
        }

        // TVS diodes
        if (upperMpn.startsWith("SMBJ")) return "SMB";
        if (upperMpn.startsWith("SMAJ")) return "SMA";
        if (upperMpn.matches("^P[46]KE.*")) return "DO-41";

        // Check suffix-based package codes
        for (Map.Entry<String, String> entry : PACKAGE_CODES.entrySet()) {
            if (upperMpn.endsWith(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Signal diodes - check specific types before generic 1Nxxxx
        if (upperMpn.matches("^1N4148.*")) return "1N4148";
        if (upperMpn.matches("^1N4448.*")) return "1N4448";
        if (upperMpn.matches("^1N914.*")) return "1N914";

        // Zener diodes - before generic 1N4xxx
        if (upperMpn.matches("^1N47[0-9]{2}.*")) return "1N47xx";

        // Standard rectifiers
        if (upperMpn.matches("^1N400[1-7].*")) return "1N400x";
        if (upperMpn.matches("^1N540[0-8].*")) return "1N540x";
        if (upperMpn.matches("^1N58[1][7-9].*")) return "1N5817";
        if (upperMpn.matches("^1N58[2][0-5].*")) return "1N5820";

        // Fast recovery
        if (upperMpn.matches("^ES[12][A-Z].*")) {
            return upperMpn.substring(0, 3);  // ES1x, ES2x
        }
        if (upperMpn.matches("^US[12][A-Z].*")) {
            return upperMpn.substring(0, 3);  // US1x, US2x
        }
        if (upperMpn.matches("^UF[0-9]+.*")) return "UF";

        // Schottky diodes
        if (upperMpn.matches("^SS[1-5][0-9].*")) {
            return upperMpn.substring(0, 4);  // SS14, SS34, etc.
        }
        if (upperMpn.matches("^SK[1-5][0-9].*")) {
            return upperMpn.substring(0, 4);  // SK34, SK56, etc.
        }
        if (upperMpn.matches("^SB[1-5][0-9]{2}.*")) {
            return upperMpn.substring(0, 5);  // SB140, SB360, etc.
        }

        // BAV/BAT series
        if (upperMpn.matches("^BAV[0-9]+.*")) {
            int endIdx = findFirstNonDigitAfterPrefix(upperMpn, 3);
            return upperMpn.substring(0, endIdx);
        }
        if (upperMpn.matches("^BAT[0-9]+.*")) {
            int endIdx = findFirstNonDigitAfterPrefix(upperMpn, 3);
            return upperMpn.substring(0, endIdx);
        }

        // TVS diodes
        if (upperMpn.startsWith("SMBJ")) return "SMBJ";
        if (upperMpn.startsWith("SMAJ")) return "SMAJ";
        if (upperMpn.matches("^P4KE.*")) return "P4KE";
        if (upperMpn.matches("^P6KE.*")) return "P6KE";

        // Bridge rectifiers
        if (upperMpn.matches("^MB[1-6]S.*")) return "MBS";
        if (upperMpn.matches("^DB[1-6]S.*")) return "DBS";

        // MMBT transistors
        if (upperMpn.matches("^MMBT[0-9]{4}.*")) {
            return upperMpn.substring(0, 8);  // MMBT2222, MMBT3904, etc.
        }

        // MMBTA transistors
        if (upperMpn.matches("^MMBTA[0-9]+.*")) {
            int endIdx = findFirstNonDigitAfterPrefix(upperMpn, 5);
            return upperMpn.substring(0, endIdx);
        }

        // 2N transistors
        if (upperMpn.matches("^2N[0-9]{4}.*")) {
            return upperMpn.substring(0, 6);  // 2N2222, 2N3904, etc.
        }
        if (upperMpn.matches("^2N[0-9]{3}.*")) {
            return upperMpn.substring(0, 5);  // 2N706, etc.
        }

        // PN transistors
        if (upperMpn.matches("^PN[0-9]{4}.*")) {
            return upperMpn.substring(0, 6);  // PN2222, PN2907
        }

        // MPSA transistors
        if (upperMpn.matches("^MPSA[0-9]+.*")) {
            int endIdx = findFirstNonDigitAfterPrefix(upperMpn, 4);
            return upperMpn.substring(0, endIdx);
        }

        // BC transistors
        if (upperMpn.matches("^BC[0-9]{3}.*")) {
            return upperMpn.substring(0, 5);  // BC547, BC557, etc.
        }

        // BF transistors
        if (upperMpn.matches("^BF[0-9]+.*")) {
            int endIdx = findFirstNonDigitAfterPrefix(upperMpn, 2);
            return upperMpn.substring(0, endIdx);
        }

        // S80xx/S90xx series
        if (upperMpn.matches("^S8050.*")) return "S8050";
        if (upperMpn.matches("^S8550.*")) return "S8550";
        if (upperMpn.matches("^S90[0-9]{2}.*")) {
            return upperMpn.substring(0, 5);  // S9012, S9013, etc.
        }

        return "";
    }

    /**
     * Helper to find the end of the numeric portion after a prefix.
     */
    private int findFirstNonDigitAfterPrefix(String mpn, int prefixLength) {
        int i = prefixLength;
        while (i < mpn.length() && Character.isDigit(mpn.charAt(i))) {
            i++;
        }
        return i;
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // Same series requirement
        String series1 = extractSeries(upper1);
        String series2 = extractSeries(upper2);

        // 1N400x series - higher voltage can replace lower
        if (series1.equals("1N400x") && series2.equals("1N400x")) {
            int voltage1 = extract1N400xVoltage(upper1);
            int voltage2 = extract1N400xVoltage(upper2);
            // Higher voltage rating can replace lower
            return voltage1 >= voltage2;
        }

        // 1N540x series - same logic
        if (series1.equals("1N540x") && series2.equals("1N540x")) {
            int voltage1 = extract1N540xVoltage(upper1);
            int voltage2 = extract1N540xVoltage(upper2);
            return voltage1 >= voltage2;
        }

        // MMBT can replace 2N equivalent
        if ((series1.startsWith("MMBT") && series2.startsWith("2N")) ||
                (series1.startsWith("2N") && series2.startsWith("MMBT"))) {
            // Extract the base transistor number
            String base1 = series1.replaceAll("^(MMBT|2N)", "");
            String base2 = series2.replaceAll("^(MMBT|2N)", "");
            // Same base number means compatible (e.g., MMBT2222 ≈ 2N2222)
            return base1.equals(base2);
        }

        // PN can replace 2N equivalent
        if ((series1.startsWith("PN") && series2.startsWith("2N")) ||
                (series1.startsWith("2N") && series2.startsWith("PN"))) {
            String base1 = series1.replaceAll("^(PN|2N)", "");
            String base2 = series2.replaceAll("^(PN|2N)", "");
            return base1.equals(base2);
        }

        // Signal diode equivalents: 1N4148 ≈ 1N914
        if ((series1.equals("1N4148") || series1.equals("1N914")) &&
                (series2.equals("1N4148") || series2.equals("1N914"))) {
            return true;
        }

        // SS series Schottky - higher current can replace lower
        if (series1.startsWith("SS") && series2.startsWith("SS")) {
            // Compare current ratings (SS14 < SS16 < SS110 < SS120)
            int current1 = extractSSCurrent(upper1);
            int current2 = extractSSCurrent(upper2);
            if (current1 > 0 && current2 > 0) {
                return current1 >= current2;
            }
        }

        // Same series is generally compatible (different package variants)
        if (!series1.isEmpty() && series1.equals(series2)) {
            return true;
        }

        return false;
    }

    /**
     * Extract voltage rating from 1N400x series (1=50V, 2=100V, ..., 7=1000V)
     */
    private int extract1N400xVoltage(String mpn) {
        if (mpn.length() >= 6 && mpn.startsWith("1N400")) {
            char digit = mpn.charAt(5);
            return switch (digit) {
                case '1' -> 50;
                case '2' -> 100;
                case '3' -> 200;
                case '4' -> 400;
                case '5' -> 600;
                case '6' -> 800;
                case '7' -> 1000;
                default -> 0;
            };
        }
        return 0;
    }

    /**
     * Extract voltage rating from 1N540x series (0=50V, 1=100V, ..., 8=800V)
     * Format: 1N540X where X is the voltage code (0-8)
     *         Index: 01234 5
     */
    private int extract1N540xVoltage(String mpn) {
        if (mpn.length() >= 6 && mpn.startsWith("1N540")) {
            char digit = mpn.charAt(5);  // Index 5 is the voltage digit
            return switch (digit) {
                case '0' -> 50;
                case '1' -> 100;
                case '2' -> 200;
                case '3' -> 300;
                case '4' -> 400;
                case '5' -> 500;
                case '6' -> 600;
                case '7' -> 700;
                case '8' -> 800;
                default -> 0;
            };
        }
        return 0;
    }

    /**
     * Extract current rating from SS series Schottky (SS14=1A, SS34=3A, SS110=1A, etc.)
     */
    private int extractSSCurrent(String mpn) {
        // SS14 = 1A, SS34 = 3A, SS54 = 5A
        // SS110 = 1A, SS120 = 1A (different format)
        if (mpn.length() >= 4 && mpn.startsWith("SS")) {
            try {
                // Get the first digit after SS
                char firstDigit = mpn.charAt(2);
                return Character.getNumericValue(firstDigit);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
