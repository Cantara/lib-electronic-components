package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for NXP components
 */
public class NXPHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // LPC Series MCUs (ARM-based)
        registry.addPattern(ComponentType.MICROCONTROLLER, "^LPC[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_NXP, "^LPC[0-9]+.*");
        registry.addPattern(ComponentType.MCU_NXP, "^LPC[0-9]+.*");

        // Kinetis Series MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^MK[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_NXP, "^MK[0-9]+.*");
        registry.addPattern(ComponentType.MCU_NXP, "^MK[0-9]+.*");

        // i.MX Series Processors
        registry.addPattern(ComponentType.MICROCONTROLLER, "^MCIMX[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_NXP, "^MCIMX[0-9]+.*");
        registry.addPattern(ComponentType.MCU_NXP, "^MCIMX[0-9]+.*");

        // S32K Series MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^S32K[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_NXP, "^S32K[0-9]+.*");
        registry.addPattern(ComponentType.MCU_NXP, "^S32K[0-9]+.*");

        // QorIQ Processors
        registry.addPattern(ComponentType.MICROCONTROLLER, "^P[0-9]+.*");
        registry.addPattern(ComponentType.MICROCONTROLLER_NXP, "^P[0-9]+.*");

        // Memory products
        registry.addPattern(ComponentType.MEMORY, "^SE[0-9]+.*");     // EEPROM
        registry.addPattern(ComponentType.MEMORY_NXP, "^SE[0-9]+.*");
        registry.addPattern(ComponentType.MEMORY, "^SPI[0-9]+.*");    // SPI Flash
        registry.addPattern(ComponentType.MEMORY_NXP, "^SPI[0-9]+.*");
        registry.addPattern(ComponentType.MEMORY, "^MX25.*");         // MX25 SPI Flash
        registry.addPattern(ComponentType.MEMORY_NXP, "^MX25.*");
        registry.addPattern(ComponentType.MEMORY, "^S25FL.*");        // S25FL Flash (Spansion acquired by NXP)
        registry.addPattern(ComponentType.MEMORY_NXP, "^S25FL.*");

        // Pressure sensors
        registry.addPattern(ComponentType.SENSOR, "^MPX.*");          // MPX pressure sensors
        registry.addPattern(ComponentType.SENSOR, "^MPXV.*");         // MPXV voltage output sensors
        registry.addPattern(ComponentType.SENSOR, "^MPXA.*");         // MPXA analog sensors

        // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PSMN[0-9]+.*");
        registry.addPattern(ComponentType.MOSFET_NXP, "^PSMN[0-9]+.*");
        registry.addPattern(ComponentType.MOSFET, "^BUK[0-9]+.*");
        registry.addPattern(ComponentType.MOSFET_NXP, "^BUK[0-9]+.*");

        // Small signal MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PMV.*");
        registry.addPattern(ComponentType.MOSFET_NXP, "^PMV.*");
        registry.addPattern(ComponentType.MOSFET, "^BSS.*");
        registry.addPattern(ComponentType.MOSFET_NXP, "^BSS.*");

        // Transistors
        // NPN transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PN2222.*");     // PN2222 (2N2222 equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^PN2222.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^PN3904.*");     // PN3904 (2N3904 equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^PN3904.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^PN4401.*");     // PN4401 (2N4401 equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^PN4401.*");

        // PNP transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^PN2907.*");     // PN2907 (2N2907 equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^PN2907.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^PN3906.*");     // PN3906 (2N3906 equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^PN3906.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^PN4403.*");     // PN4403 (2N4403 equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^PN4403.*");

        // BC series transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^BC847.*");      // BC847 (BC547 SMD equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^BC847.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^BC857.*");      // BC857 (BC557 SMD equivalent)
        registry.addPattern(ComponentType.TRANSISTOR_NXP, "^BC857.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.MICROCONTROLLER,
                ComponentType.MICROCONTROLLER_NXP,
                ComponentType.MCU_NXP,
                ComponentType.MEMORY,
                ComponentType.MEMORY_NXP,
                ComponentType.MOSFET,
                ComponentType.MOSFET_NXP,
                ComponentType.TRANSISTOR,
                ComponentType.TRANSISTOR_NXP,
                ComponentType.SENSOR,
                ComponentType.OPAMP,
                ComponentType.OPAMP_NXP,
                ComponentType.KINETIS_MCU,
                ComponentType.LPC_MCU,
                ComponentType.IMX_PROCESSOR
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Direct matching for transistors
        if (type == ComponentType.TRANSISTOR || type == ComponentType.TRANSISTOR_NXP) {
            if (upperMpn.matches("^PN2222.*") ||     // PN2222 (2N2222 equivalent)
                    upperMpn.matches("^PN2907.*") ||     // PN2907 (2N2907 equivalent)
                    upperMpn.matches("^PN3904.*") ||     // PN3904 (2N3904 equivalent)
                    upperMpn.matches("^PN3906.*") ||     // PN3906 (2N3906 equivalent)
                    upperMpn.matches("^PN4401.*") ||     // PN4401 (2N4401 equivalent)
                    upperMpn.matches("^PN4403.*") ||     // PN4403 (2N4403 equivalent)
                    upperMpn.matches("^BC847.*") ||      // BC847 (BC547 SMD equivalent)
                    upperMpn.matches("^BC857.*")) {      // BC857 (BC557 SMD equivalent)
                return true;
            }
        }

        // NXP op-amps (e.g., PCA9xxx I2C interface ICs sometimes used for level shifting)
        // Note: LM358/LM324/LM741 are TI parts, not NXP - removed incorrect claims
        if (type == ComponentType.OPAMP || type == ComponentType.OPAMP_NXP) {
            // NXP doesn't have major op-amp product lines - they focus on MCUs, interfaces, and mixed-signal
            return false;
        }

        // Direct matching for microcontrollers
        if (type == ComponentType.MICROCONTROLLER || type == ComponentType.MICROCONTROLLER_NXP) {
            if (upperMpn.startsWith("LPC") ||    // LPC series
                    upperMpn.startsWith("MK") ||     // Kinetis series
                    upperMpn.startsWith("IMX") ||    // i.MX series
                    upperMpn.startsWith("S32K")) {   // S32K series
                return true;
            }
        }

        // Direct matching for MOSFETs
        if (type == ComponentType.MOSFET || type == ComponentType.MOSFET_NXP) {
            if (upperMpn.startsWith("PSMN") ||   // Power MOSFETs
                    upperMpn.startsWith("BUK") ||    // Standard MOSFETs
                    upperMpn.startsWith("PMV") ||    // Small signal MOSFETs
                    upperMpn.startsWith("BSS")) {    // Small signal MOSFETs
                return true;
            }
        }

        // Direct matching for memory
        if (type == ComponentType.MEMORY || type == ComponentType.MEMORY_NXP) {
            if (upperMpn.startsWith("SE") ||     // EEPROM
                    upperMpn.startsWith("SPI") ||    // SPI Flash
                    upperMpn.startsWith("MX25") ||   // MX25 Flash
                    upperMpn.startsWith("S25FL")) {  // S25FL Flash
                return true;
            }
        }

        // Direct matching for sensors
        if (type == ComponentType.SENSOR) {
            if (upperMpn.startsWith("MPX") ||    // Pressure sensors
                    upperMpn.startsWith("MPXV") ||   // Voltage output sensors
                    upperMpn.startsWith("MPXA")) {   // Analog sensors
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

        // LPC Series package codes: LPC[SERIES][VARIANT][PACKAGE][PINS]
        // Example: LPC1768FBD100 -> FBD (LQFP), LPC55S69JBD100 -> JBD (LQFP)
        if (upperMpn.startsWith("LPC")) {
            // Find the first letter sequence after the series digits
            // LPC1768FBD100: Skip LPC (3), find digits (1768), find letters (FBD)
            int pos = 3;  // Skip "LPC"

            // Skip series digits
            while (pos < upperMpn.length() && Character.isDigit(upperMpn.charAt(pos))) {
                pos++;
            }

            // Skip optional variant letter (like 'S' in LPC55S69)
            if (pos < upperMpn.length() && Character.isLetter(upperMpn.charAt(pos)) &&
                    pos + 1 < upperMpn.length() && Character.isDigit(upperMpn.charAt(pos + 1))) {
                pos++;
                // Skip more digits after variant letter
                while (pos < upperMpn.length() && Character.isDigit(upperMpn.charAt(pos))) {
                    pos++;
                }
            }

            // Extract package code letters (FBD, JBD, FET, FHN, UK)
            int pkgStart = pos;
            while (pos < upperMpn.length() && Character.isLetter(upperMpn.charAt(pos))) {
                pos++;
            }

            if (pos > pkgStart) {
                return upperMpn.substring(pkgStart, pos);
            }
        }

        // Kinetis Series: MK[SERIES][FLASH]V[PACKAGE][SPEED]
        // Example: MK64FN1M0VLL12 -> VLL (LQFP100)
        if (upperMpn.startsWith("MK")) {
            int vPos = upperMpn.indexOf('V');
            if (vPos > 0 && vPos + 3 < upperMpn.length()) {
                // Extract V + 2-3 letter package code
                int pkgEnd = vPos + 1;
                while (pkgEnd < upperMpn.length() && Character.isLetter(upperMpn.charAt(pkgEnd))) {
                    pkgEnd++;
                }
                if (pkgEnd > vPos + 1) {
                    return upperMpn.substring(vPos, pkgEnd);
                }
            }
        }

        // i.MX Series: MCIMX[SERIES][VARIANT][PACKAGE][TEMP][REV]
        // Example: MCIMX6Q5EYM10AC -> M (BGA)
        if (upperMpn.startsWith("MCIMX")) {
            // Package is typically a single letter before temperature/speed digits
            // Look for pattern: letter + digits + letter(s) at end
            for (int i = upperMpn.length() - 3; i >= 5; i--) {
                if (Character.isLetter(upperMpn.charAt(i)) &&
                        i + 1 < upperMpn.length() && Character.isDigit(upperMpn.charAt(i + 1))) {
                    return String.valueOf(upperMpn.charAt(i));
                }
            }
        }

        // PSMN MOSFETs: PSMN[SPEC]-[PACKAGE]
        // Example: PSMN4R3-30PL -> PL
        if (upperMpn.startsWith("PSMN")) {
            int dashPos = upperMpn.lastIndexOf('-');
            if (dashPos > 0 && upperMpn.length() > dashPos + 2) {
                // Extract letters after the last dash and digits
                String suffix = upperMpn.substring(dashPos + 1);
                int pos = 0;
                // Skip digits (voltage rating)
                while (pos < suffix.length() && Character.isDigit(suffix.charAt(pos))) {
                    pos++;
                }
                if (pos < suffix.length()) {
                    return suffix.substring(pos);
                }
            }
        }

        // BUK MOSFETs: BUK[LEVEL][PACKAGE][TECH][VOLTAGE][RESISTANCE]
        // Example: BUK9Y40-100B -> Y (LFPAK56)
        if (upperMpn.startsWith("BUK")) {
            // Package code is typically the 5th character
            if (upperMpn.length() > 4) {
                char pkgChar = upperMpn.charAt(4);
                if (Character.isLetter(pkgChar)) {
                    return String.valueOf(pkgChar);
                }
            }
        }

        // BC transistors: BC847[GAIN][PACKAGE_SUFFIX]
        // Example: BC847W -> W (SOT-323), BC847MB -> MB (DFN)
        if (upperMpn.startsWith("BC84") || upperMpn.startsWith("BC85")) {
            if (upperMpn.length() > 5) {
                // Skip BC847 or BC857, extract remaining letters
                String suffix = upperMpn.substring(5);
                // First char might be gain (A, B, C), then package
                if (suffix.length() > 1 && Character.isLetter(suffix.charAt(0)) && Character.isLetter(suffix.charAt(1))) {
                    return suffix.substring(1);  // Package after gain
                } else if (suffix.length() == 1 && !suffix.matches("[ABC]")) {
                    return suffix;  // Package without gain
                }
            }
        }

        return "";
    }

    private int findFirstDigit(String str, int startFrom) {
        for (int i = startFrom; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private int findLastDigit(String str) {
        return findLastDigit(str, 0);
    }

    private int findLastDigit(String str, int startFrom) {
        for (int i = str.length() - 1; i >= startFrom; i--) {
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

        // Microcontrollers
        if (upperMpn.startsWith("LPC")) return "LPC";
        if (upperMpn.startsWith("MK")) return "Kinetis";
        if (upperMpn.startsWith("MCIMX")) return "i.MX";
        if (upperMpn.startsWith("IMX")) return "i.MX";  // Plain IMX prefix (datasheets often use this)
        if (upperMpn.startsWith("S32K")) return "S32K";
        if (upperMpn.startsWith("P") && Character.isDigit(getCharAt(upperMpn, 1))) return "QorIQ";

        // Memory
        if (upperMpn.startsWith("SE")) return "EEPROM";
        if (upperMpn.startsWith("SPI")) return "SPI Flash";
        if (upperMpn.startsWith("MX25")) return "MX25 Flash";
        if (upperMpn.startsWith("S25FL")) return "S25FL Flash";

        // MOSFETs
        if (upperMpn.startsWith("PSMN")) return "PSMN MOSFET";
        if (upperMpn.startsWith("BUK")) return "BUK MOSFET";
        if (upperMpn.startsWith("PMV")) return "PMV MOSFET";
        if (upperMpn.startsWith("BSS")) return "BSS MOSFET";

        // Transistors
        if (upperMpn.startsWith("BC847")) return "BC847";
        if (upperMpn.startsWith("BC857")) return "BC857";
        if (upperMpn.startsWith("PN2222")) return "PN2222";
        if (upperMpn.startsWith("PN2907")) return "PN2907";
        if (upperMpn.startsWith("PN3904")) return "PN3904";
        if (upperMpn.startsWith("PN3906")) return "PN3906";
        if (upperMpn.startsWith("PN4401")) return "PN4401";
        if (upperMpn.startsWith("PN4403")) return "PN4403";

        // Sensors - CHECK SPECIFIC PREFIXES FIRST!
        if (upperMpn.startsWith("MPXV")) return "MPXV Pressure Sensor";
        if (upperMpn.startsWith("MPXA")) return "MPXA Pressure Sensor";
        if (upperMpn.startsWith("MPX")) return "MPX Pressure Sensor";  // Generic last

        // Audio codecs
        if (upperMpn.startsWith("UDA")) return "UDA Audio Codec";
        if (upperMpn.startsWith("TDA")) return "TDA Audio";
        if (upperMpn.startsWith("TFA")) return "TFA Audio Amplifier";

        // Interface ICs
        if (upperMpn.startsWith("PCA")) return "PCA Interface IC";
        if (upperMpn.startsWith("PCF")) return "PCF Interface IC";

        return "";
    }

    private char getCharAt(String str, int index) {
        return index < str.length() ? str.charAt(index) : '\0';
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        // Check for pin-compatible replacements within same series
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (!series1.equals(series2)) return false;

        // Add specific replacement rules here if needed
        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}