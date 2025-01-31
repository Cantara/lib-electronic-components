package no.cantara.electronic.component.manufacturers;

import no.cantara.electronic.component.ComponentType;
import no.cantara.electronic.component.ManufacturerHandler;
import no.cantara.electronic.component.ManufacturerComponentType;
import no.cantara.electronic.component.PatternRegistry;

import java.util.*;

public class VishayHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Vishay Dale Chip Resistors
        // CRCW0603100RFKEA format
        registry.addPattern(ComponentType.RESISTOR, "^CRCW[0-9]{4}.*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^CRCW[0-9]{4}.*");

        // Resistors
        registry.addPattern(ComponentType.RESISTOR, "^CRCW[0-9]{4}[0-9R][0-9A-Z]*");  // CRCW series
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^CRCW[0-9]{4}[0-9R][0-9A-Z]*");

        // Add other common Vishay resistor series
        registry.addPattern(ComponentType.RESISTOR, "^(RCG|RCWP|RCA|RCL)[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^(RCG|RCWP|RCA|RCL)[0-9].*");

        registry.addPattern(ComponentType.RESISTOR, "^(PAT|PTN|PNM)[0-9].*");  // Precision resistors
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^(PAT|PTN|PNM)[0-9].*");

        // High precision thin film
        registry.addPattern(ComponentType.RESISTOR, "^CRMA[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^CRMA[0-9].*");

        // High voltage
        registry.addPattern(ComponentType.RESISTOR, "^CRHV[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^CRHV[0-9].*");

        // Current sense
        registry.addPattern(ComponentType.RESISTOR, "^RCS[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^RCS[0-9].*");

        // Power resistors
        registry.addPattern(ComponentType.RESISTOR, "^WSL[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_VISHAY, "^WSL[0-9].*");

        // MOSFETs
        registry.addPattern(ComponentType.MOSFET, "^SI[0-9].*");        // SI series MOSFETs
        registry.addPattern(ComponentType.MOSFET_VISHAY, "^SI[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^SIS[0-9].*");       // SIS series
        registry.addPattern(ComponentType.MOSFET_VISHAY, "^SIS[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^SIR[0-9].*");       // SIR series
        registry.addPattern(ComponentType.MOSFET_VISHAY, "^SIR[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^SIB[0-9].*");       // SIB series
        registry.addPattern(ComponentType.MOSFET_VISHAY, "^SIB[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^SIH[0-9].*");       // SIH series
        registry.addPattern(ComponentType.MOSFET_VISHAY, "^SIH[0-9].*");
        registry.addPattern(ComponentType.MOSFET, "^SIHF[0-9].*");      // SIHF series
        registry.addPattern(ComponentType.MOSFET_VISHAY, "^SIHF[0-9].*");

        // Diodes
        // Rectifier diodes
        registry.addPattern(ComponentType.DIODE, "^1N4[0-9]{3}.*");     // 1N4000 series
        registry.addPattern(ComponentType.DIODE_VISHAY, "^1N4[0-9]{3}.*");
        registry.addPattern(ComponentType.DIODE, "^1N5[0-9]{3}.*");     // 1N5000 series
        registry.addPattern(ComponentType.DIODE_VISHAY, "^1N5[0-9]{3}.*");

        // Signal diodes
        registry.addPattern(ComponentType.DIODE, "^1N4148.*");          // 1N4148 signal diode
        registry.addPattern(ComponentType.DIODE_VISHAY, "^1N4148.*");
        registry.addPattern(ComponentType.DIODE, "^1N914.*");           // 1N914 signal diode
        registry.addPattern(ComponentType.DIODE_VISHAY, "^1N914.*");

        // Schottky diodes
        registry.addPattern(ComponentType.DIODE, "^BAT[0-9].*");        // BAT series Schottkys
        registry.addPattern(ComponentType.DIODE_VISHAY, "^BAT[0-9].*");

        // Fast/Ultra-fast recovery
        registry.addPattern(ComponentType.DIODE, "^BYV[0-9].*");        // BYV series
        registry.addPattern(ComponentType.DIODE_VISHAY, "^BYV[0-9].*");
        registry.addPattern(ComponentType.DIODE, "^UF[0-9].*");         // UF series
        registry.addPattern(ComponentType.DIODE_VISHAY, "^UF[0-9].*");

        // Zener diodes
        registry.addPattern(ComponentType.DIODE, "^BZX[0-9].*");        // BZX series Zeners
        registry.addPattern(ComponentType.DIODE_VISHAY, "^BZX[0-9].*");
        registry.addPattern(ComponentType.DIODE, "^1N47[0-9]{2}.*");    // 1N47xx Zeners
        registry.addPattern(ComponentType.DIODE_VISHAY, "^1N47[0-9]{2}.*");

        // LEDs
        registry.addPattern(ComponentType.LED, "^VLMU[0-9].*");         // Standard LEDs
        registry.addPattern(ComponentType.LED_STANDARD_VISHAY, "^VLMU[0-9].*");
        registry.addPattern(ComponentType.LED, "^VLMRGB[0-9].*");       // RGB LEDs
        registry.addPattern(ComponentType.LED_RGB_VISHAY, "^VLMRGB[0-9].*");
        registry.addPattern(ComponentType.LED, "^VLMS[0-9].*");         // SMD LEDs
        registry.addPattern(ComponentType.LED_SMD_VISHAY, "^VLMS[0-9].*");

        // Transistors
        // BJT NPN transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^2N2222.*");     // 2N2222
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^2N2222.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^2N3904.*");     // 2N3904
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^2N3904.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^2N4401.*");     // 2N4401
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^2N4401.*");

        // BJT PNP transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^2N2907.*");     // 2N2907
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^2N2907.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^2N3906.*");     // 2N3906
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^2N3906.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^2N4403.*");     // 2N4403
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^2N4403.*");

        // BC series transistors
        registry.addPattern(ComponentType.TRANSISTOR, "^BC547.*");      // BC547
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^BC547.*");
        registry.addPattern(ComponentType.TRANSISTOR, "^BC557.*");      // BC557
        registry.addPattern(ComponentType.TRANSISTOR_VISHAY, "^BC557.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.RESISTOR);
        types.add(ComponentType.RESISTOR_CHIP_VISHAY);
        types.add(ComponentType.RESISTOR_THT_VISHAY);
        types.add(ComponentType.MOSFET);
        types.add(ComponentType.MOSFET_VISHAY);
        types.add(ComponentType.DIODE);
        types.add(ComponentType.DIODE_VISHAY);
    //    types.add(ComponentType.OPTOCOUPLER);
        types.add(ComponentType.OPTOCOUPLER_VISHAY);
        types.add(ComponentType.TRANSISTOR);
        types.add(ComponentType.TRANSISTOR_VISHAY);
        types.add(ComponentType.LED);
        types.add(ComponentType.LED_STANDARD_VISHAY);
        types.add(ComponentType.LED_RGB_VISHAY);
        types.add(ComponentType.LED_SMD_VISHAY);
        return types;
    }
    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        // Direct check for common resistor series
        if (type == ComponentType.RESISTOR ||
                type == ComponentType.RESISTOR_CHIP_VISHAY ||
                type == ComponentType.RESISTOR_THT_VISHAY) {

            String upperMpn = mpn.toUpperCase();
            if (upperMpn.matches("^CRCW[0-9]{4}[0-9R][0-9A-Z]*") ||  // CRCW series
                    upperMpn.matches("^(RCG|RCWP|RCA|RCL)[0-9].*") ||    // Other chip series
                    upperMpn.matches("^(PAT|PTN|PNM)[0-9].*")) {         // Precision series
                return true;
            }
        }

        String upperMpn = mpn.toUpperCase();

        // Direct matching for transistors
        if (type == ComponentType.TRANSISTOR || type == ComponentType.TRANSISTOR_VISHAY) {
            if (upperMpn.matches("^2N[0-9]{4}.*") ||     // 2N series
                    upperMpn.matches("^BC[0-9]{3}.*")) {      // BC series
                return true;
            }
        }

        // Special case for diodes
        if (type == ComponentType.DIODE || type == ComponentType.DIODE_VISHAY) {
            if (upperMpn.matches("^1N4[0-9]{3}.*") ||    // 1N4000 series
                    upperMpn.matches("^1N5[0-9]{3}.*") ||    // 1N5000 series
                    upperMpn.matches("^1N4148.*") ||         // Signal diode
                    upperMpn.matches("^1N914.*") ||          // Signal diode
                    upperMpn.matches("^BAT[0-9].*") ||       // Schottky
                    upperMpn.matches("^BYV[0-9].*") ||       // Fast recovery
                    upperMpn.matches("^UF[0-9].*") ||        // Ultra-fast recovery
                    upperMpn.matches("^BZX[0-9].*") ||       // Zener
                    upperMpn.matches("^1N47[0-9]{2}.*")) {   // Zener
                return true;
            }
        }

        // Special case for CRCW resistors
        if (type == ComponentType.RESISTOR_CHIP_VISHAY && upperMpn.startsWith("CRCW")) {
            return true;
        }

        // Fallback to default pattern matching
        return ManufacturerHandler.super.matches(mpn, type, patterns);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Transistor packages
        if (mpn.matches("(?i)^2N.*")) {
            return "TO-18";  // Most 2N transistors use TO-18
        }

        // Diode packages
        if (mpn.matches("(?i)^1N.*")) {
            // Common 1N series packages
            if (mpn.endsWith("A")) return "DO-41";
            if (mpn.endsWith("B")) return "DO-15";
            return "DO-41";  // Default for 1N series
        }

        // CRCW resistors use 4-digit size codes
        if (mpn.startsWith("CRCW")) {
            try {
                String sizeCode = mpn.substring(4, 8);
                return switch (sizeCode) {
                    case "0402" -> "0402";
                    case "0603" -> "0603";
                    case "0805" -> "0805";
                    case "1206" -> "1206";
                    case "1210" -> "1210";
                    case "2010" -> "2010";
                    case "2512" -> "2512";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // MOSFET packages
        if (mpn.startsWith("SI")) {
            // Extract package code after the numeric part
            String[] parts = mpn.split("[0-9]+");
            if (parts.length > 1) {
                return switch (parts[1]) {
                    case "DH" -> "PowerPAK SO-8";
                    case "FH" -> "PowerPAK SO-8 Dual";
                    case "CH" -> "PowerPAK 1212-8";
                    case "JH" -> "PowerPAK SC-70";
                    default -> parts[1];
                };
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Diode series
        if (mpn.matches("(?i)^1N4[0-9]{3}.*")) return "1N4000";  // 1N4000 series
        if (mpn.matches("(?i)^1N5[0-9]{3}.*")) return "1N5000";  // 1N5000 series
        if (mpn.matches("(?i)^1N4148.*")) return "1N4148";       // Signal diode
        if (mpn.matches("(?i)^1N914.*")) return "1N914";         // Signal diode
        if (mpn.matches("(?i)^BAT[0-9].*")) return mpn.substring(0, 5);  // BAT series
        if (mpn.matches("(?i)^BYV[0-9].*")) return mpn.substring(0, 5);  // BYV series
        if (mpn.matches("(?i)^UF[0-9].*")) return mpn.substring(0, 4);   // UF series
        if (mpn.matches("(?i)^BZX[0-9].*")) return mpn.substring(0, 5);  // BZX series

        // Resistors
        if (mpn.startsWith("CRCW")) return "CRCW";
        if (mpn.startsWith("CRMA")) return "CRMA";
        if (mpn.startsWith("CRHV")) return "CRHV";
        if (mpn.startsWith("RCS")) return "RCS";
        if (mpn.startsWith("WSL")) {
            if (mpn.startsWith("WSLP")) return "WSLP";
            if (mpn.startsWith("WSLT")) return "WSLT";
            return "WSL";
        }

        // MOSFETs
        if (mpn.startsWith("SI")) {
            if (mpn.startsWith("SIHF")) return "SIHF";
            if (mpn.startsWith("SIH")) return "SIH";
            if (mpn.startsWith("SIS")) return "SIS";
            if (mpn.startsWith("SIR")) return "SIR";
            if (mpn.startsWith("SIB")) return "SIB";
            return "SI";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Diode replacements
        if (series1.equals("1N4000") && series2.equals("1N4000")) {
            int voltage1 = extractDiodeVoltage(mpn1);
            int voltage2 = extractDiodeVoltage(mpn2);
            // Higher voltage rating can replace lower
            return voltage1 >= voltage2;
        }

        // Different series are not replaceable
        if (!series1.equals(series2)) return false;

        // MOSFET replacements
        if (mpn1.startsWith("SI") && mpn2.startsWith("SI")) {
            // Extract the base part number
            String base1 = mpn1.replaceAll("[A-Z]+$", "");
            String base2 = mpn2.replaceAll("[A-Z]+$", "");
            return base1.equals(base2);
        }

        // Resistor replacements
        if (mpn1.startsWith("CR") && mpn2.startsWith("CR")) {
            String size1 = extractPackageCode(mpn1);
            String size2 = extractPackageCode(mpn2);
            return size1.equals(size2);
        }

        return false;
    }

    private int extractDiodeVoltage(String mpn) {
        if (mpn.matches("(?i)1N400[1-7]")) {
            return switch (mpn.charAt(mpn.length() - 1)) {
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
        return 0;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}