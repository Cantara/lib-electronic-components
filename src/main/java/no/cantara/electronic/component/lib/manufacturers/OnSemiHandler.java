package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class OnSemiHandler implements ManufacturerHandler {
    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // DIP packages
        PACKAGE_CODES.put("N", "DIP");
        PACKAGE_CODES.put("P", "DIP");

        // Surface mount packages
        PACKAGE_CODES.put("D", "SOIC");
        PACKAGE_CODES.put("M", "SOIC");
        PACKAGE_CODES.put("DW", "SOIC-Wide");
        PACKAGE_CODES.put("PW", "TSSOP");
        PACKAGE_CODES.put("DGK", "MSOP");
        PACKAGE_CODES.put("DBV", "SOT-23");

        // Diode packages
        PACKAGE_CODES.put("RL", "DO-41");    // Standard rectifier package
        PACKAGE_CODES.put("G", "DO-35");     // Small signal diode package
        PACKAGE_CODES.put("T", "TO-220");    // Power package
        PACKAGE_CODES.put("FP", "TO-220F");  // Fully isolated TO-220
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MOSFET,
                ComponentType.MOSFET_ONSEMI,
                ComponentType.IGBT_ONSEMI,
                ComponentType.TRANSISTOR,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.VOLTAGE_REGULATOR_LINEAR_ON,
                ComponentType.VOLTAGE_REGULATOR_SWITCHING_ON,
                ComponentType.LED_DRIVER,
                ComponentType.LED_DRIVER_ONSEMI,
                ComponentType.MOTOR_DRIVER,
                ComponentType.MOTOR_DRIVER_ONSEMI,
                ComponentType.OPAMP,
                ComponentType.OPAMP_ON,
                ComponentType.DIODE,
                ComponentType.DIODE_ON
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Op-amps
        registry.addPattern(ComponentType.OPAMP, "^MC1458[A-Z]?.*");      // Dual Op-amp
        registry.addPattern(ComponentType.OPAMP_ON, "^MC1458[A-Z]?.*");
        registry.addPattern(ComponentType.OPAMP, "^MC324[A-Z]?.*");       // Quad Op-amp
        registry.addPattern(ComponentType.OPAMP_ON, "^MC324[A-Z]?.*");
        registry.addPattern(ComponentType.OPAMP, "^MC741[A-Z]?.*");       // Single Op-amp
        registry.addPattern(ComponentType.OPAMP_ON, "^MC741[A-Z]?.*");

        // Voltage Regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MC78[0-9]{2}[A-Z]?.*");     // Positive fixed
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, "^MC78[0-9]{2}[A-Z]?.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MC79[0-9]{2}[A-Z]?.*");     // Negative fixed
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, "^MC79[0-9]{2}[A-Z]?.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^MC33[0-9]{2}[A-Z]?.*");     // Switching
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_SWITCHING_ON, "^MC33[0-9]{2}[A-Z]?.*");
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^NCP[0-9]{3,4}.*");          // NCP regulator family
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_LINEAR_ON, "^NCP[0-9]{3,4}.*");

        // MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^NTD[0-9]+[A-Z]?.*");                   // N-channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_ONSEMI, "^NTD[0-9]+[A-Z]?.*");
        registry.addPattern(ComponentType.MOSFET, "^FQP[0-9]+[A-Z]?.*");                   // QFET MOSFETs
        registry.addPattern(ComponentType.MOSFET_ONSEMI, "^FQP[0-9]+[A-Z]?.*");
        registry.addPattern(ComponentType.MOSFET, "^FDP[0-9]+[A-Z]?.*");                   // PowerTrench MOSFETs
        registry.addPattern(ComponentType.MOSFET_ONSEMI, "^FDP[0-9]+[A-Z]?.*");
        registry.addPattern(ComponentType.MOSFET, "^NTP[0-9]+[A-Z]?.*");                   // P-channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_ONSEMI, "^NTP[0-9]+[A-Z]?.*");

        // Transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^2N[0-9]{4}[A-Z]?.*");              // 2N series BJTs
        registry.addPattern(ComponentType.TRANSISTOR, "^MMBT[0-9]{4}[A-Z]?.*");            // SOT-23 BJTs
        registry.addPattern(ComponentType.TRANSISTOR, "^MPSA[0-9]{2}[A-Z]?.*");            // MPSA series (NPN)
        registry.addPattern(ComponentType.TRANSISTOR, "^MPSH[0-9]{2}[A-Z]?.*");            // MPSH series (PNP)

        // Diodes
        // Standard rectifier diodes
        registry.addPattern(ComponentType.DIODE, "^RL207[A-Z]?.*");       // 1000V/2A rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RL207[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^RL206[A-Z]?.*");       // 800V/2A rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RL206[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^RL205[A-Z]?.*");       // 600V/2A rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RL205[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^RL204[A-Z]?.*");       // 400V/2A rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RL204[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^RL203[A-Z]?.*");       // 200V/2A rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RL203[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^RL202[A-Z]?.*");       // 100V/2A rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RL202[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^RL201[A-Z]?.*");       // 50V/2A rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RL201[A-Z]?.*");

        // Fast recovery rectifiers
        registry.addPattern(ComponentType.DIODE, "^MUR[0-9]+[A-Z]?.*");   // Ultra-fast recovery
        registry.addPattern(ComponentType.DIODE_ON, "^MUR[0-9]+[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^RHRP[0-9]+[A-Z]?.*");  // High voltage rectifier
        registry.addPattern(ComponentType.DIODE_ON, "^RHRP[0-9]+[A-Z]?.*");

        // Schottky diodes
        registry.addPattern(ComponentType.DIODE, "^MBRS[0-9]+[A-Z]?.*");  // Surface mount Schottky
        registry.addPattern(ComponentType.DIODE_ON, "^MBRS[0-9]+[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^MBR[0-9]+[A-Z]?.*");   // Standard Schottky
        registry.addPattern(ComponentType.DIODE_ON, "^MBR[0-9]+[A-Z]?.*");

        // Zener diodes
        registry.addPattern(ComponentType.DIODE, "^1N47[0-9]{2}[A-Z]?.*"); // 1N47xx Zener series
        registry.addPattern(ComponentType.DIODE_ON, "^1N47[0-9]{2}[A-Z]?.*");
        registry.addPattern(ComponentType.DIODE, "^1N52[0-9]{2}[A-Z]?.*"); // 1N52xx Zener series
        registry.addPattern(ComponentType.DIODE_ON, "^1N52[0-9]{2}[A-Z]?.*");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct matching for diodes
        if (type == ComponentType.DIODE || type == ComponentType.DIODE_ON) {
            if (upperMpn.matches("^RL20[1-7][A-Z]?.*") ||      // RL series rectifiers
                    upperMpn.matches("^MUR[0-9]+[A-Z]?.*") ||      // Ultra-fast recovery
                    upperMpn.matches("^RHRP[0-9]+[A-Z]?.*") ||     // High voltage rectifier
                    upperMpn.matches("^MBR[S]?[0-9]+[A-Z]?.*") ||  // Schottky diodes
                    upperMpn.matches("^1N47[0-9]{2}[A-Z]?.*") ||   // 1N47xx Zener series
                    upperMpn.matches("^1N52[0-9]{2}[A-Z]?.*")) {   // 1N52xx Zener series
                return true;
            }
        }

        // Direct matching for common op-amps
        if (type == ComponentType.OPAMP || type == ComponentType.OPAMP_ON) {
            if (upperMpn.startsWith("MC1458") ||
                    upperMpn.startsWith("MC324") ||
                    upperMpn.startsWith("MC741")) {
                return true;
            }
        }

        // Direct matching for voltage regulators
        if (type == ComponentType.VOLTAGE_REGULATOR ||
                type == ComponentType.VOLTAGE_REGULATOR_LINEAR_ON ||
                type == ComponentType.VOLTAGE_REGULATOR_SWITCHING_ON) {
            if (upperMpn.matches("^MC78[0-9]{2}.*") ||     // Positive fixed
                    upperMpn.matches("^MC79[0-9]{2}.*") ||     // Negative fixed
                    upperMpn.matches("^MC33[0-9]{2}.*") ||     // Switching
                    upperMpn.startsWith("NCP")) {              // NCP regulator family
                return true;
            }
        }

        // Direct matching for MOSFETs
        if (type == ComponentType.MOSFET || type == ComponentType.MOSFET_ONSEMI) {
            if (upperMpn.startsWith("NTD") ||     // N-channel
                    upperMpn.startsWith("FQP") ||     // QFET
                    upperMpn.startsWith("FDP") ||     // PowerTrench
                    upperMpn.startsWith("NTP")) {     // P-channel
                return true;
            }
        }

        // Direct matching for transistors
        if (type == ComponentType.TRANSISTOR) {
            if (upperMpn.matches("^2N[0-9]{4}[A-Z]?.*") ||      // 2N series BJTs
                    upperMpn.startsWith("MMBT") ||                 // SOT-23 BJTs
                    upperMpn.startsWith("MPSA") ||                 // MPSA series (NPN)
                    upperMpn.startsWith("MPSH")) {                 // MPSH series (PNP)
                return true;
            }
        }

        // Use handler-specific patterns for other matches (avoid cross-handler false matches)
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Diode packages
        if (upperMpn.startsWith("RL")) {
            return "DO-41";  // Standard package for RL series
        }
        if (upperMpn.startsWith("MUR")) {
            if (upperMpn.endsWith("T")) return "TO-220";
            return "DO-41";  // Default for MUR series
        }
        if (upperMpn.startsWith("MBRS")) {
            return "SMB";    // Surface mount package
        }
        if (upperMpn.startsWith("MBR")) {
            if (upperMpn.endsWith("T")) return "TO-220";
            return "DO-41";  // Default for MBR series
        }

        // MOSFET packages - extract suffix after digits
        if (upperMpn.startsWith("NTD") || upperMpn.startsWith("NTP")) {
            // NTD4808N, NTP30N06 - Extract suffix after last digit
            int lastDigit = findLastDigitIndex(upperMpn);
            if (lastDigit >= 0 && lastDigit < upperMpn.length() - 1) {
                return upperMpn.substring(lastDigit + 1);
            }
        }
        if (upperMpn.startsWith("FQP") || upperMpn.startsWith("FDP")) {
            // FQP30N06L, FDP8896 - Extract suffix after last digit
            int lastDigit = findLastDigitIndex(upperMpn);
            if (lastDigit >= 0 && lastDigit < upperMpn.length() - 1) {
                return upperMpn.substring(lastDigit + 1);
            }
            // Common MOSFET packages if no suffix
            return "TO-220";  // Default for FQP/FDP series
        }

        // Transistor packages
        if (upperMpn.matches("^2N[0-9]{4}[A-Z]?.*")) {
            // 2N2222A, 2N3904 - Standard through-hole
            return "TO-92";  // Default for 2N series
        }
        if (upperMpn.startsWith("MMBT")) {
            // MMBT2222, MMBT3904 - SOT-23 package
            return "SOT-23";
        }
        if (upperMpn.startsWith("MPSA") || upperMpn.startsWith("MPSH")) {
            // MPSA42, MPSH10 - Usually TO-92 or SOT-23
            return "TO-92";  // Default
        }

        // Voltage regulators and op-amps - extract suffix
        if (upperMpn.startsWith("MC") || upperMpn.startsWith("NCP")) {
            String suffix = upperMpn.replaceAll("^[A-Z0-9]+(?=[A-Z])", "");

            // Check for known package codes
            for (Map.Entry<String, String> entry : PACKAGE_CODES.entrySet()) {
                if (suffix.startsWith(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return suffix;
        }

        return "";
    }

    private int findLastDigitIndex(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // MOSFETs - CHECK SPECIFIC PREFIXES FIRST
        if (upperMpn.startsWith("NTD")) return "NTD";        // N-channel MOSFETs
        if (upperMpn.startsWith("NTP")) return "NTP";        // P-channel MOSFETs
        if (upperMpn.startsWith("FQP")) return "FQP";        // QFET MOSFETs
        if (upperMpn.startsWith("FDP")) return "FDP";        // PowerTrench MOSFETs

        // Transistors
        if (upperMpn.matches("^2N[0-9]{4}.*")) return "2N";  // 2N series BJTs
        if (upperMpn.startsWith("MMBT")) return "MMBT";      // SOT-23 BJTs
        if (upperMpn.startsWith("MPSA")) return "MPSA";      // MPSA series (NPN)
        if (upperMpn.startsWith("MPSH")) return "MPSH";      // MPSH series (PNP)

        // Voltage regulators - CHECK SPECIFIC PREFIXES FIRST
        if (upperMpn.startsWith("NCP")) return "NCP";        // NCP regulator family
        if (upperMpn.startsWith("MC78")) return "MC78";      // Positive fixed
        if (upperMpn.startsWith("MC79")) return "MC79";      // Negative fixed
        if (upperMpn.startsWith("MC33")) return "MC33";      // Switching regulators

        // Op-amp series - CHECK SPECIFIC PREFIXES FIRST
        if (upperMpn.startsWith("MC1458")) return "MC1458";  // Dual op-amp
        if (upperMpn.startsWith("MC324")) return "MC324";    // Quad op-amp
        if (upperMpn.startsWith("MC741")) return "MC741";    // Single op-amp

        // Diode series - CHECK SPECIFIC PREFIXES FIRST
        if (upperMpn.startsWith("MBRS")) return "MBRS";      // Surface mount Schottky (before MBR!)
        if (upperMpn.startsWith("MBR")) return "MBR";        // Standard Schottky
        if (upperMpn.matches("^RL20[1-7].*")) return "RL207"; // RL series rectifiers
        if (upperMpn.startsWith("MUR")) return "MUR";        // Ultra-fast recovery
        if (upperMpn.matches("^1N47[0-9]{2}.*")) return "1N47"; // 1N47xx Zener
        if (upperMpn.matches("^1N52[0-9]{2}.*")) return "1N52"; // 1N52xx Zener

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Check diode replacements
        if (mpn1.matches("(?i)^RL20[1-7].*") && mpn2.matches("(?i)^RL20[1-7].*")) {
            int voltage1 = extractDiodeVoltage(mpn1);
            int voltage2 = extractDiodeVoltage(mpn2);
            // Higher voltage rating can replace lower
            return voltage1 >= voltage2;
        }

        // Same series check
        if (!series1.equals(series2)) return false;

        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Same package is always compatible
        if (pkg1.equals(pkg2)) return true;

        // For diodes, some packages are interchangeable
        if ((pkg1.equals("DO-41") || pkg1.equals("DO-201")) &&
                (pkg2.equals("DO-41") || pkg2.equals("DO-201"))) {
            return true;
        }

        return false;
    }

    private int extractDiodeVoltage(String mpn) {
        if (mpn.matches("(?i)RL20[1-7]")) {
            return switch (mpn.charAt(mpn.length() - 1)) {
                case '1' -> 50;    // RL201
                case '2' -> 100;   // RL202
                case '3' -> 200;   // RL203
                case '4' -> 400;   // RL204
                case '5' -> 600;   // RL205
                case '6' -> 800;   // RL206
                case '7' -> 1000;  // RL207
                default -> 0;
            };
        }
        return 0;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}