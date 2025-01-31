package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler for TDK Corporation components
 */
public class TDKHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Inductors - MLF series needs to be matched first
        registry.addPattern(ComponentType.INDUCTOR, "^MLF[0-9]{4}[A-Z0-9]*");  // More specific MLF pattern
        registry.addPattern(ComponentType.INDUCTOR_CHIP_TDK, "^MLF[0-9]{4}[A-Z0-9]*");
        // Ceramic Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^C[0-9].*");             // Standard MLCC
        registry.addPattern(ComponentType.CAPACITOR, "^CGA[0-9].*");           // High-reliability MLCC
        registry.addPattern(ComponentType.CAPACITOR, "^CGJ[0-9].*");           // Low ESL MLCC
        registry.addPattern(ComponentType.CAPACITOR, "^CGK[0-9].*");           // High voltage MLCC
        registry.addPattern(ComponentType.CAPACITOR_CERAMIC_TDK, "^C[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_CERAMIC_TDK, "^CGA[0-9].*");
        registry.addPattern(ComponentType.CAPACITOR_CERAMIC_TDK, "^CGJ[0-9].*");

        // Inductors
        registry.addPattern(ComponentType.INDUCTOR, "^MLF[0-9].*");           // Multilayer ferrite inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SLF[0-9].*");           // Shielded power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^VLS[0-9].*");           // Power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^NLV[0-9].*");           // High current inductors
        registry.addPattern(ComponentType.INDUCTOR, "^CLF[0-9].*");           // Thin-film power inductors
        registry.addPattern(ComponentType.INDUCTOR_CHIP_TDK, "^MLF[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_TDK, "^SLF[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_TDK, "^VLS[0-9].*");
        // Inductors
        registry.addPattern(ComponentType.INDUCTOR, "^MLF[0-9].*");  // MLF series inductors
        registry.addPattern(ComponentType.INDUCTOR_CHIP_TDK, "^MLF[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR, "^SLF[0-9].*");  // SLF series inductors
        registry.addPattern(ComponentType.INDUCTOR_CHIP_TDK, "^SLF[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR, "^NLC[0-9].*");  // NLC series inductors
        registry.addPattern(ComponentType.INDUCTOR_CHIP_TDK, "^NLC[0-9].*");


        // Power inductors
        registry.addPattern(ComponentType.INDUCTOR_POWER_TDK, "^CLF[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR, "^CLF[0-9].*");

        // EMI filters and beads
        registry.addPattern(ComponentType.EMI_FILTER_TDK, "^MMZ[0-9].*");
        registry.addPattern(ComponentType.FERRITE_BEAD_TDK, "^MPZ[0-9].*");
        registry.addPattern(ComponentType.COMMON_MODE_CHOKE_TDK, "^ACM[0-9].*");

        // EMI Components
        registry.addPattern(ComponentType.INDUCTOR, "^MMZ[0-9].*");           // Ferrite beads
        registry.addPattern(ComponentType.INDUCTOR, "^MPZ[0-9].*");           // EMI suppression filters
        registry.addPattern(ComponentType.INDUCTOR, "^ACM[0-9].*");           // Common mode chokes
        registry.addPattern(ComponentType.INDUCTOR, "^ACT[0-9].*");           // Common mode transformers

        // Transformers and Pulse Transformers
        registry.addPattern(ComponentType.INDUCTOR, "^ALT[0-9].*");           // Pulse transformers
        registry.addPattern(ComponentType.INDUCTOR, "^ATB[0-9].*");           // SMD transformers

        // Sensors and NTC Thermistors
        registry.addPattern(ComponentType.IC, "^B57[0-9].*");                // NTC thermistors
        registry.addPattern(ComponentType.IC, "^HAL[0-9].*");                // Hall sensors
        registry.addPattern(ComponentType.IC, "^TMR[0-9].*");                // Temperature sensors

        // RF Components
        registry.addPattern(ComponentType.IC, "^DEA[0-9].*");                // RF diplexers
        registry.addPattern(ComponentType.IC, "^MEA[0-9].*");                // RF multiplexers
        registry.addPattern(ComponentType.IC, "^FBA[0-9].*");                // RF baluns

        // Magnetics (Ferrite Cores and Magnets)
        registry.addPattern(ComponentType.INDUCTOR, "^PC[0-9].*");           // Ferrite cores
        registry.addPattern(ComponentType.INDUCTOR, "^PM[0-9].*");           // Permanent magnets

        // Varistors and Arresters
        registry.addPattern(ComponentType.IC, "^B72[0-9].*");                // Varistors
        registry.addPattern(ComponentType.IC, "^EC[0-9].*");                 // Surge arresters

        // Film Capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^B32[0-9].*");         // Film capacitors
        registry.addPattern(ComponentType.CAPACITOR, "^B33[0-9].*");         // Metallized film capacitors
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CAPACITOR);
        types.add(ComponentType.CAPACITOR_CERAMIC_TDK);
        types.add(ComponentType.INDUCTOR);
        types.add(ComponentType.INDUCTOR_CHIP_TDK);
        types.add(ComponentType.INDUCTOR_POWER_TDK);
        types.add(ComponentType.EMI_FILTER_TDK);
        types.add(ComponentType.FERRITE_BEAD_TDK);
        types.add(ComponentType.COMMON_MODE_CHOKE_TDK);
        return types;
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;
        // Direct check for MLF series inductors
        if ((type == ComponentType.INDUCTOR || type == ComponentType.INDUCTOR_CHIP_TDK)
                && mpn.toUpperCase().matches("^MLF[0-9]{4}[A-Z0-9]*")) {
            return true;
        }
        Pattern pattern = registry.getPattern(type);
        if (pattern == null) return false;

        return pattern.matcher(mpn.toUpperCase()).matches();
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // MLCC size codes
        if (upperMpn.startsWith("C") || upperMpn.startsWith("CGA")) {
            // Extract metric size code
            try {
                String sizeCode = upperMpn.substring(upperMpn.startsWith("CGA") ? 3 : 1, 5);
                return switch (sizeCode) {
                    case "02" -> "0402/1005M";
                    case "03" -> "0603/1608M";
                    case "05" -> "0805/2012M";
                    case "12" -> "1206/3216M";
                    case "21" -> "0201/0603M";
                    case "31" -> "1210/3225M";
                    case "32" -> "1812/4532M";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Inductor size codes
        if (upperMpn.startsWith("MLF") || upperMpn.startsWith("SLF")) {
            if (upperMpn.length() >= 6) {
                String sizeCode = upperMpn.substring(3, 6);
                return switch (sizeCode) {
                    case "151" -> "0402";
                    case "201" -> "0603";
                    case "251" -> "0805";
                    case "261" -> "1206";
                    default -> sizeCode;
                };
            }
        }

        // EMI component size codes
        if (upperMpn.startsWith("MMZ") || upperMpn.startsWith("MPZ")) {
            if (upperMpn.length() >= 6) {
                String sizeCode = upperMpn.substring(3, 6);
                return switch (sizeCode) {
                    case "160" -> "0402";
                    case "180" -> "0603";
                    case "210" -> "0805";
                    case "310" -> "1206";
                    default -> sizeCode;
                };
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Capacitors
        if (upperMpn.startsWith("CGA")) return "CGA Series";
        if (upperMpn.startsWith("CGJ")) return "CGJ Series";
        if (upperMpn.startsWith("CGK")) return "CGK Series";
        if (upperMpn.startsWith("C") && Character.isDigit(getCharAt(upperMpn, 1)))
            return "C Series";

        // Inductors
        if (upperMpn.startsWith("MLF")) return "MLF Series";
        if (upperMpn.startsWith("SLF")) return "SLF Series";
        if (upperMpn.startsWith("VLS")) return "VLS Series";
        if (upperMpn.startsWith("NLV")) return "NLV Series";
        if (upperMpn.startsWith("CLF")) return "CLF Series";

        // EMI Components
        if (upperMpn.startsWith("MMZ")) return "MMZ Series";
        if (upperMpn.startsWith("MPZ")) return "MPZ Series";
        if (upperMpn.startsWith("ACM")) return "ACM Series";
        if (upperMpn.startsWith("ACT")) return "ACT Series";

        // Transformers
        if (upperMpn.startsWith("ALT")) return "ALT Series";
        if (upperMpn.startsWith("ATB")) return "ATB Series";

        // Sensors
        if (upperMpn.startsWith("B57")) return "NTC Thermistor";
        if (upperMpn.startsWith("HAL")) return "Hall Sensor";
        if (upperMpn.startsWith("TMR")) return "Temperature Sensor";

        // RF Components
        if (upperMpn.startsWith("DEA")) return "RF Diplexer";
        if (upperMpn.startsWith("MEA")) return "RF Multiplexer";
        if (upperMpn.startsWith("FBA")) return "RF Balun";

        // Varistors and Arresters
        if (upperMpn.startsWith("B72")) return "Varistor";
        if (upperMpn.startsWith("EC")) return "Surge Arrester";

        return "";
    }

    private char getCharAt(String str, int index) {
        return index < str.length() ? str.charAt(index) : '\0';
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // For MLCCs, same size, voltage, and temperature characteristic
        if ((mpn1.startsWith("C") && Character.isDigit(getCharAt(mpn1, 1))) ||
                mpn1.startsWith("CGA")) {
            String size1 = extractPackageCode(mpn1);
            String size2 = extractPackageCode(mpn2);
            if (!size1.equals(size2)) return false;

            // Extract and compare temperature characteristic
            try {
                // Temperature characteristic is typically after size code
                String temp1 = mpn1.substring(5, 7);
                String temp2 = mpn2.substring(5, 7);
                return temp1.equals(temp2);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }

        // For inductors, same size and current rating
        if (mpn1.startsWith("MLF") || mpn1.startsWith("SLF")) {
            String size1 = extractPackageCode(mpn1);
            String size2 = extractPackageCode(mpn2);
            return size1.equals(size2);
        }

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}