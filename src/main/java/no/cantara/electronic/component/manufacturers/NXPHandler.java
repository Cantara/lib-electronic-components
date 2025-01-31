package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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

        // Power MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^PSMN[0-9]+.*");
        registry.addPattern(ComponentType.MOSFET_NXP, "^PSMN[0-9]+.*");
        registry.addPattern(ComponentType.MOSFET, "^BUK[0-9]+.*");
        registry.addPattern(ComponentType.MOSFET_NXP, "^BUK[0-9]+.*");

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
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.MICROCONTROLLER);
        types.add(ComponentType.MICROCONTROLLER_NXP);
        types.add(ComponentType.MCU_NXP);
        types.add(ComponentType.MEMORY);
        types.add(ComponentType.MEMORY_NXP);
        types.add(ComponentType.MOSFET);
        types.add(ComponentType.MOSFET_NXP);
        types.add(ComponentType.TRANSISTOR);
        types.add(ComponentType.TRANSISTOR_NXP);
        types.add(ComponentType.OPAMP);
        types.add(ComponentType.OPAMP_NXP);
        types.add(ComponentType.KINETIS_MCU);
        types.add(ComponentType.LPC_MCU);
        types.add(ComponentType.IMX_PROCESSOR);
        return types;
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

        // Direct matching for common op-amps
        if (type == ComponentType.OPAMP || type == ComponentType.OPAMP_NXP) {
            if (upperMpn.startsWith("LM358") ||
                    upperMpn.startsWith("LM324") ||
                    upperMpn.startsWith("LM741")) {
                return true;
            }
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
                    upperMpn.startsWith("SPI")) {    // SPI Flash
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

        // LPC Series package codes
        if (upperMpn.startsWith("LPC")) {
            // Example: LPC1768FBD100 -> FBD100 (LQFP100)
            int numStart = findFirstDigit(upperMpn, 3);
            if (numStart >= 0) {
                int numEnd = findLastDigit(upperMpn, numStart) + 1;
                if (numEnd < upperMpn.length()) {
                    return upperMpn.substring(numEnd);
                }
            }
        }

        // i.MX Series package codes
        if (upperMpn.startsWith("MCIMX")) {
            // Last characters typically represent package
            int lastNum = findLastDigit(upperMpn);
            if (lastNum >= 0 && lastNum < upperMpn.length() - 1) {
                return upperMpn.substring(lastNum + 1);
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

        if (upperMpn.startsWith("LPC")) return "LPC";
        if (upperMpn.startsWith("MK")) return "Kinetis";
        if (upperMpn.startsWith("MCIMX")) return "i.MX";
        if (upperMpn.startsWith("S32K")) return "S32K";
        if (upperMpn.startsWith("P") && Character.isDigit(getCharAt(upperMpn, 1))) return "QorIQ";
        if (upperMpn.startsWith("SE")) return "EEPROM";
        if (upperMpn.startsWith("SPI")) return "SPI Flash";
        if (upperMpn.startsWith("PSMN")) return "PSMN MOSFET";
        if (upperMpn.startsWith("BUK")) return "BUK MOSFET";

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