package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for Toshiba Semiconductor components
 */
public class ToshibaHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^TPC[0-9].*");          // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^TPH[0-9].*");          // High voltage MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^SSM[0-9].*");          // Small signal MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^TK[0-9].*");           // Digital power MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^2SK[0-9].*");          // JIS N-channel MOSFETs
        registry.addPattern(ComponentType.MOSFET_TOSHIBA, "^TPC[0-9].*");
        registry.addPattern(ComponentType.MOSFET_TOSHIBA, "^TPH[0-9].*");
        registry.addPattern(ComponentType.MOSFET_TOSHIBA, "^SSM[0-9].*");
        registry.addPattern(ComponentType.MOSFET_TOSHIBA, "^TK[0-9].*");
        registry.addPattern(ComponentType.MOSFET_TOSHIBA, "^2SK[0-9].*");

        // Transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^2SC[0-9].*");       // NPN transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^2SA[0-9].*");       // PNP transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^RN[0-9].*");        // Digital transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^RP[0-9].*");        // Digital transistors

        // IGBTs
        registry.addPattern(ComponentType.IC, "^GT[0-9].*");               // IGBT modules
        registry.addPattern(ComponentType.IC, "^MG[0-9].*");               // IGBT modules
        registry.addPattern(ComponentType.IGBT_TOSHIBA, "^GT[0-9].*");
        registry.addPattern(ComponentType.IGBT_TOSHIBA, "^MG[0-9].*");

        // Motor Drivers
        registry.addPattern(ComponentType.IC, "^TB[0-9].*");               // Motor drivers
        registry.addPattern(ComponentType.IC, "^TPD[0-9].*");              // Gate drivers
        registry.addPattern(ComponentType.MOTOR_DRIVER_TOSHIBA, "^TB[0-9].*");
        registry.addPattern(ComponentType.GATE_DRIVER_TOSHIBA, "^TPD[0-9].*");

        // Optocouplers
        registry.addPattern(ComponentType.IC, "^TLP[0-9].*");              // Optocouplers
        registry.addPattern(ComponentType.OPTOCOUPLER_TOSHIBA, "^TLP[0-9].*");

        // Voltage Regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR, "^TAR[0-9].*"); // Linear regulators
        registry.addPattern(ComponentType.VOLTAGE_REGULATOR_TOSHIBA, "^TAR[0-9].*");

        // Microcontrollers
        registry.addPattern(ComponentType.MICROCONTROLLER, "^TMPM[0-9].*"); // ARM-based MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^TMPV[0-9].*"); // RISC-V MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER_TOSHIBA, "^TMP[MV][0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MOSFET,
                ComponentType.MOSFET_TOSHIBA,
                ComponentType.TRANSISTOR,
                ComponentType.IC,
                ComponentType.IGBT_TOSHIBA,
                ComponentType.MOTOR_DRIVER_TOSHIBA,
                ComponentType.GATE_DRIVER_TOSHIBA,
                ComponentType.OPTOCOUPLER_TOSHIBA,
                ComponentType.VOLTAGE_REGULATOR,
                ComponentType.VOLTAGE_REGULATOR_TOSHIBA,
                ComponentType.MICROCONTROLLER,
                ComponentType.MICROCONTROLLER_TOSHIBA
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        if (upperMpn.startsWith("TPC") || upperMpn.startsWith("TPH")) {
            // Extract package code from suffix
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                return upperMpn.substring(lastDigitIndex + 1);
            }
        }

        if (upperMpn.startsWith("TMP")) {
            // Microcontroller package codes
            int dashIndex = upperMpn.lastIndexOf('-');
            if (dashIndex >= 0 && dashIndex < upperMpn.length() - 1) {
                return upperMpn.substring(dashIndex + 1);
            }
        }

        // TK series MOSFETs - format: TK[digits][letter][digits][package]
        // Example: TK024N60Z1 → Z1
        if (upperMpn.startsWith("TK")) {
            String suffix = upperMpn.substring(2); // Skip "TK"
            // Skip first set of digits
            int pos = 0;
            while (pos < suffix.length() && Character.isDigit(suffix.charAt(pos))) {
                pos++;
            }
            // Skip channel type letter (N/P)
            if (pos < suffix.length() && Character.isLetter(suffix.charAt(pos))) {
                pos++;
            }
            // Skip voltage digits
            while (pos < suffix.length() && Character.isDigit(suffix.charAt(pos))) {
                pos++;
            }
            // Remaining is package code
            if (pos < suffix.length()) {
                return suffix.substring(pos);
            }
        }

        // SSM series small signal MOSFETs - package code after K/J designation
        // Format: SSM3K102TU → TU
        if (upperMpn.startsWith("SSM")) {
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                return upperMpn.substring(lastDigitIndex + 1);
            }
        }

        // TLP optocouplers - package code after digits
        // Format: TLP127 → nothing, TLP291(GB-TP,SE) → (GB-TP,SE) but we strip that
        if (upperMpn.startsWith("TLP")) {
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                String suffix = upperMpn.substring(lastDigitIndex + 1);
                // Strip packaging options like (GB-TP,SE)
                int parenIndex = suffix.indexOf('(');
                if (parenIndex > 0) {
                    suffix = suffix.substring(0, parenIndex);
                }
                return suffix;
            }
        }

        // TB motor drivers - package code after digits
        // Format: TB6612FNG → FNG
        if (upperMpn.startsWith("TB")) {
            int lastDigitIndex = findLastDigit(upperMpn);
            if (lastDigitIndex >= 0 && lastDigitIndex < upperMpn.length() - 1) {
                return upperMpn.substring(lastDigitIndex + 1);
            }
        }

        return "";
    }

    private int findLastDigit(String str) {
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

        // MOSFETs
        if (upperMpn.startsWith("TPC")) return "TPC Series";
        if (upperMpn.startsWith("TPH")) return "TPH Series";
        if (upperMpn.startsWith("SSM")) return "SSM Series";
        if (upperMpn.startsWith("TK")) return "TK Series";
        if (upperMpn.startsWith("2SK")) return "2SK Series";

        // Transistors
        if (upperMpn.startsWith("2SC")) return "2SC Series";
        if (upperMpn.startsWith("2SA")) return "2SA Series";
        if (upperMpn.startsWith("RN")) return "RN Series";
        if (upperMpn.startsWith("RP")) return "RP Series";

        // IGBTs
        if (upperMpn.startsWith("GT")) return "GT Series";
        if (upperMpn.startsWith("MG")) return "MG Series";

        // Motor Drivers
        if (upperMpn.startsWith("TB")) return "TB Series";
        if (upperMpn.startsWith("TPD")) return "TPD Series";

        // Optocouplers
        if (upperMpn.startsWith("TLP")) return "TLP Series";

        // Voltage Regulators
        if (upperMpn.startsWith("TAR")) return "TAR Series";

        // Microcontrollers
        if (upperMpn.startsWith("TMPM")) return "ARM MCU";
        if (upperMpn.startsWith("TMPV")) return "RISC-V MCU";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // Check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        if (!pkg1.equals(pkg2)) return false;

        // For MOSFETs and IGBTs, check voltage and current ratings if available
        if (series1.endsWith(" Series") &&
                (series1.startsWith("TPC") || series1.startsWith("TPH") ||
                        series1.startsWith("GT") || series1.startsWith("MG"))) {
            // Could implement detailed rating comparison here
            return false;
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}