package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Yangjie Technology (Chinese semiconductor manufacturer) components.
 *
 * Yangjie specializes in discrete semiconductors including:
 * - Rectifier diodes (YJ series, 1N series)
 * - Schottky rectifiers (MBR series)
 * - TVS diodes (SMBJ/SMAJ series)
 * - Schottky diodes (SS/SK series)
 * - Transistors (2N/MMBT series)
 * - Bridge rectifiers (YJB/YJDB series)
 */
public class YangjieHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Yangjie YJ series rectifier diodes (e.g., YJ1N4007, YJ1N5408)
        registry.addPattern(ComponentType.DIODE, "^YJ1N[0-9]{4}.*");

        // MBR Schottky rectifiers (e.g., MBR1045, MBR2045, MBR3045)
        registry.addPattern(ComponentType.DIODE, "^MBR[0-9]{4}.*");

        // SMBJ TVS diodes (e.g., SMBJ5.0A, SMBJ15A, SMBJ24A)
        registry.addPattern(ComponentType.DIODE, "^SMBJ[0-9]+\\.?[0-9]*[AC]?.*");

        // SMAJ TVS diodes (e.g., SMAJ5.0A, SMAJ15A, SMAJ24CA)
        registry.addPattern(ComponentType.DIODE, "^SMAJ[0-9]+\\.?[0-9]*[AC]?.*");

        // SS series Schottky diodes (e.g., SS14, SS24, SS34, SS54)
        registry.addPattern(ComponentType.DIODE, "^SS[0-9]{2}.*");

        // SK series Schottky diodes (e.g., SK34, SK54, SK56)
        registry.addPattern(ComponentType.DIODE, "^SK[0-9]{2}.*");

        // 2N series transistors (e.g., 2N7002, 2N3904, 2N3906)
        registry.addPattern(ComponentType.TRANSISTOR, "^2N[0-9]{4}.*");
        registry.addPattern(ComponentType.MOSFET, "^2N7002.*");  // 2N7002 is specifically a MOSFET

        // MMBT series transistors (e.g., MMBT2222, MMBT3904, MMBT3906)
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT[0-9]{4}.*");

        // YJB bridge rectifiers (e.g., YJB1010, YJB108)
        registry.addPattern(ComponentType.DIODE, "^YJB[0-9]+.*");

        // YJDB bridge rectifiers (e.g., YJDB107, YJDB157)
        registry.addPattern(ComponentType.DIODE, "^YJDB[0-9]+.*");

        // Standard 1N series diodes from Yangjie (e.g., 1N4007, 1N5408)
        registry.addPattern(ComponentType.DIODE, "^1N[0-9]{4}.*");
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

        // Direct matching for diodes
        if (type == ComponentType.DIODE) {
            if (upperMpn.matches("^YJ1N[0-9]{4}.*") ||           // YJ rectifier diodes
                    upperMpn.matches("^MBR[0-9]{4}.*") ||         // MBR Schottky rectifiers
                    upperMpn.matches("^SMBJ[0-9]+\\.?[0-9]*[AC]?.*") ||  // SMBJ TVS diodes
                    upperMpn.matches("^SMAJ[0-9]+\\.?[0-9]*[AC]?.*") ||  // SMAJ TVS diodes
                    upperMpn.matches("^SS[0-9]{2}.*") ||          // SS Schottky diodes
                    upperMpn.matches("^SK[0-9]{2}.*") ||          // SK Schottky diodes
                    upperMpn.matches("^YJB[0-9]+.*") ||           // YJB bridge rectifiers
                    upperMpn.matches("^YJDB[0-9]+.*") ||          // YJDB bridge rectifiers
                    upperMpn.matches("^1N[0-9]{4}.*")) {          // 1N series diodes
                return true;
            }
        }

        // Direct matching for transistors
        if (type == ComponentType.TRANSISTOR) {
            if (upperMpn.matches("^2N[0-9]{4}.*") ||             // 2N series transistors
                    upperMpn.matches("^MMBT[0-9]{4}.*")) {        // MMBT series transistors
                return true;
            }
        }

        // Direct matching for MOSFETs
        if (type == ComponentType.MOSFET) {
            if (upperMpn.matches("^2N7002.*")) {                  // 2N7002 N-channel MOSFET
                return true;
            }
        }

        // Fallback to pattern registry
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // YJ rectifier diodes - extract package suffix
        if (upperMpn.matches("^YJ1N[0-9]{4}.*")) {
            String suffix = upperMpn.replaceFirst("^YJ1N[0-9]{4}", "");
            return decodePackageSuffix(suffix);
        }

        // MBR Schottky rectifiers - extract package from suffix
        if (upperMpn.startsWith("MBR")) {
            // MBR1045CT -> CT = TO-220
            String suffix = extractTrailingSuffix(upperMpn);
            return decodeMBRPackage(suffix);
        }

        // SMBJ/SMAJ TVS diodes - SMB/SMA package based on prefix
        if (upperMpn.startsWith("SMBJ")) {
            return "SMB";
        }
        if (upperMpn.startsWith("SMAJ")) {
            return "SMA";
        }

        // SS/SK Schottky diodes - typically SMA or SMB based on suffix
        // Check longer suffixes first (FL before L)
        if (upperMpn.matches("^SS[0-9]{2}.*") || upperMpn.matches("^SK[0-9]{2}.*")) {
            if (upperMpn.endsWith("FL")) return "SOD-123FL";
            if (upperMpn.endsWith("L")) return "SOD-123";
            return "DO-214AC";  // Default SMA package
        }

        // 2N series transistors/MOSFETs
        if (upperMpn.startsWith("2N")) {
            if (upperMpn.contains("SOT")) return "SOT-23";
            return "TO-92";  // Default package for 2N series
        }

        // MMBT series - typically SOT-23
        if (upperMpn.startsWith("MMBT")) {
            return "SOT-23";
        }

        // YJB/YJDB bridge rectifiers - typically MBS/DIP package
        if (upperMpn.startsWith("YJB") || upperMpn.startsWith("YJDB")) {
            if (upperMpn.contains("S")) return "MBS";
            return "DIP-4";  // Default bridge package
        }

        // 1N series diodes
        if (upperMpn.startsWith("1N")) {
            if (upperMpn.endsWith("A")) return "DO-41";
            if (upperMpn.endsWith("SMD") || upperMpn.endsWith("S")) return "DO-214";
            return "DO-41";  // Default axial package
        }

        return "";
    }

    private String decodePackageSuffix(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "DO-41";

        return switch (suffix.toUpperCase()) {
            case "G" -> "DO-41";
            case "S" -> "SMD";
            case "FL" -> "SOD-123FL";
            case "M" -> "MELF";
            default -> suffix.isEmpty() ? "DO-41" : suffix;
        };
    }

    private String decodeMBRPackage(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "TO-220";

        return switch (suffix.toUpperCase()) {
            case "CT" -> "TO-220";
            case "PT" -> "TO-247";
            case "FCT" -> "TO-220F";
            case "S" -> "DO-214AB";  // SMC
            case "D" -> "DPAK";
            case "DP" -> "D2PAK";
            default -> suffix.isEmpty() ? "TO-220" : suffix;
        };
    }

    private String extractTrailingSuffix(String mpn) {
        // Extract letters after the last digit
        StringBuilder suffix = new StringBuilder();
        boolean foundDigit = false;
        for (int i = mpn.length() - 1; i >= 0; i--) {
            char c = mpn.charAt(i);
            if (Character.isDigit(c)) {
                foundDigit = true;
                break;
            }
            if (Character.isLetter(c)) {
                suffix.insert(0, c);
            }
        }
        return foundDigit ? suffix.toString() : "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // YJ rectifier diodes
        if (upperMpn.startsWith("YJ1N")) return "YJ";

        // MBR Schottky rectifiers - extract full series prefix
        if (upperMpn.startsWith("MBR")) return "MBR";

        // SMBJ TVS diodes
        if (upperMpn.startsWith("SMBJ")) return "SMBJ";

        // SMAJ TVS diodes
        if (upperMpn.startsWith("SMAJ")) return "SMAJ";

        // SS Schottky diodes
        if (upperMpn.matches("^SS[0-9]{2}.*")) return "SS";

        // SK Schottky diodes
        if (upperMpn.matches("^SK[0-9]{2}.*")) return "SK";

        // 2N series - return specific type
        if (upperMpn.startsWith("2N7002")) return "2N7002";  // MOSFET series
        if (upperMpn.startsWith("2N")) return "2N";

        // MMBT series transistors
        if (upperMpn.startsWith("MMBT")) return "MMBT";

        // YJB bridge rectifiers
        if (upperMpn.startsWith("YJB")) return "YJB";

        // YJDB bridge rectifiers
        if (upperMpn.startsWith("YJDB")) return "YJDB";

        // 1N series diodes - return specific series type
        if (upperMpn.matches("^1N4148.*")) return "1N4148";      // Signal diode
        if (upperMpn.matches("^1N914.*")) return "1N914";        // Signal diode
        if (upperMpn.matches("^1N47[0-9]{2}.*")) return "1N47xx"; // Zener series
        if (upperMpn.matches("^1N4[0-9]{3}.*")) return "1N4xxx";  // 1N4xxx rectifier series
        if (upperMpn.matches("^1N5[0-9]{3}.*")) return "1N5xxx";  // 1N5xxx rectifier series
        if (upperMpn.startsWith("1N")) return "1N";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // For rectifier diodes in same series, higher voltage rating can replace lower
        if (series1.equals("1N4xxx") || series1.equals("YJ")) {
            int voltage1 = extractRectifierVoltage(mpn1);
            int voltage2 = extractRectifierVoltage(mpn2);
            if (voltage1 > 0 && voltage2 > 0) {
                return voltage1 >= voltage2;  // Higher voltage can replace lower
            }
        }

        // For MBR Schottky rectifiers, same voltage/current rating with different package
        if (series1.equals("MBR")) {
            String base1 = extractMBRBase(mpn1);
            String base2 = extractMBRBase(mpn2);
            return base1.equals(base2);  // Same electrical specs
        }

        // For TVS diodes, same voltage rating (bidirectional CA can replace unidirectional A)
        if (series1.equals("SMBJ") || series1.equals("SMAJ")) {
            String voltage1 = extractTVSVoltage(mpn1);
            String voltage2 = extractTVSVoltage(mpn2);
            if (!voltage1.equals(voltage2)) return false;

            // CA (bidirectional) can replace A (unidirectional) in some applications
            boolean isBi1 = mpn1.toUpperCase().endsWith("CA");
            boolean isBi2 = mpn2.toUpperCase().endsWith("CA");
            return isBi1 || (!isBi1 && !isBi2);  // Same type or CA can replace A
        }

        // For SS/SK Schottky diodes - same current rating
        if (series1.equals("SS") || series1.equals("SK")) {
            String rating1 = extractSchottkyRating(mpn1);
            String rating2 = extractSchottkyRating(mpn2);
            return rating1.equals(rating2);
        }

        // For transistors - must be same part number (different manufacturer suffixes OK)
        if (series1.equals("MMBT") || series1.equals("2N")) {
            String base1 = extractTransistorBase(mpn1);
            String base2 = extractTransistorBase(mpn2);
            return base1.equals(base2);
        }

        return false;
    }

    private int extractRectifierVoltage(String mpn) {
        String upperMpn = mpn.toUpperCase();

        // YJ1N4001-1N4007 voltage ratings
        if (upperMpn.matches(".*1N400[1-7].*")) {
            char lastDigit = upperMpn.charAt(upperMpn.indexOf("1N400") + 5);
            return switch (lastDigit) {
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

        // 1N5401-1N5408 voltage ratings
        if (upperMpn.matches(".*1N540[1-8].*")) {
            char lastDigit = upperMpn.charAt(upperMpn.indexOf("1N540") + 5);
            return switch (lastDigit) {
                case '1' -> 100;
                case '2' -> 200;
                case '3' -> 200;
                case '4' -> 400;
                case '5' -> 500;
                case '6' -> 600;
                case '7' -> 800;
                case '8' -> 1000;
                default -> 0;
            };
        }

        return 0;
    }

    private String extractMBRBase(String mpn) {
        // Extract the numeric part (current/voltage rating) from MBR parts
        // e.g., MBR1045CT -> 1045 (10A, 45V)
        String upperMpn = mpn.toUpperCase();
        if (upperMpn.startsWith("MBR")) {
            StringBuilder base = new StringBuilder("MBR");
            for (int i = 3; i < upperMpn.length(); i++) {
                char c = upperMpn.charAt(i);
                if (Character.isDigit(c)) {
                    base.append(c);
                } else {
                    break;
                }
            }
            return base.toString();
        }
        return mpn;
    }

    private String extractTVSVoltage(String mpn) {
        // Extract voltage from TVS diode (e.g., SMBJ5.0A -> 5.0)
        String upperMpn = mpn.toUpperCase();
        String prefix = upperMpn.startsWith("SMBJ") ? "SMBJ" :
                       upperMpn.startsWith("SMAJ") ? "SMAJ" : "";
        if (!prefix.isEmpty()) {
            String remainder = upperMpn.substring(prefix.length());
            StringBuilder voltage = new StringBuilder();
            for (char c : remainder.toCharArray()) {
                if (Character.isDigit(c) || c == '.') {
                    voltage.append(c);
                } else {
                    break;
                }
            }
            return voltage.toString();
        }
        return "";
    }

    private String extractSchottkyRating(String mpn) {
        // Extract current rating from SS/SK Schottky (e.g., SS14 -> 1A, SS34 -> 3A)
        String upperMpn = mpn.toUpperCase();
        if (upperMpn.matches("^(SS|SK)[0-9]{2}.*")) {
            return upperMpn.substring(2, 4);  // Return the rating digits
        }
        return "";
    }

    private String extractTransistorBase(String mpn) {
        // Extract base transistor number without suffix
        // e.g., MMBT2222A -> MMBT2222, 2N3904 -> 2N3904
        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("MMBT")) {
            // MMBT followed by 4 digits is the base
            if (upperMpn.length() >= 8) {
                return upperMpn.substring(0, 8);
            }
            return upperMpn;
        }

        if (upperMpn.startsWith("2N")) {
            // 2N followed by 4 digits is the base
            if (upperMpn.length() >= 6) {
                return upperMpn.substring(0, 6);
            }
            return upperMpn;
        }

        return upperMpn;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
