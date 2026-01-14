package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for Bourns components
 */
public class BournsHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Resistors
        registry.addPattern(ComponentType.RESISTOR, "^CR[0-9].*");          // Standard chip resistors
        registry.addPattern(ComponentType.RESISTOR, "^CRA[0-9].*");         // Anti-sulfur resistors
        registry.addPattern(ComponentType.RESISTOR, "^CRF[0-9].*");         // Fusible resistors
        registry.addPattern(ComponentType.RESISTOR, "^CRM[0-9].*");         // MELF resistors
        registry.addPattern(ComponentType.RESISTOR, "^PWR[0-9].*");         // Power resistors
        registry.addPattern(ComponentType.RESISTOR_CHIP_BOURNS, "^CR[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_BOURNS, "^CRA[0-9].*");
        registry.addPattern(ComponentType.RESISTOR_CHIP_BOURNS, "^CRF[0-9].*");

        // Inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SRN[0-9].*");         // Power inductors
        registry.addPattern(ComponentType.INDUCTOR, "^SRP[0-9].*");         // Power inductors (high current)
        registry.addPattern(ComponentType.INDUCTOR, "^SRR[0-9].*");         // Power inductors (shielded)
        registry.addPattern(ComponentType.INDUCTOR, "^SDR[0-9].*");         // Power inductors (unshielded)
        registry.addPattern(ComponentType.INDUCTOR_CHIP_BOURNS, "^SRN[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_BOURNS, "^SRP[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_CHIP_BOURNS, "^SRR[0-9].*");
        registry.addPattern(ComponentType.INDUCTOR_THT_BOURNS, "^RLB[0-9].*");

        // Magnetics (Transformers & Common Mode Chokes)
        registry.addPattern(ComponentType.INDUCTOR, "^PM[0-9].*");          // Power transformers
        registry.addPattern(ComponentType.INDUCTOR, "^PT[0-9].*");          // Pulse transformers
        registry.addPattern(ComponentType.INDUCTOR, "^SRF[0-9].*");         // Common mode chokes

        // Circuit Protection
        registry.addPattern(ComponentType.IC, "^CDSOT[0-9].*");            // TVS diode arrays
        registry.addPattern(ComponentType.IC, "^CDDFN[0-9].*");            // TVS diode arrays (ultra-low cap)
        registry.addPattern(ComponentType.IC, "^MOV-[0-9].*");             // Metal oxide varistors
        registry.addPattern(ComponentType.IC, "^MF-R[0-9].*");             // PTC resettable fuses
        registry.addPattern(ComponentType.IC, "^MF-S[0-9].*");             // Surface mount fuses
        registry.addPattern(ComponentType.IC, "^SF-[0-9].*");              // Fuse holders

        // Sensors and Controls
        registry.addPattern(ComponentType.IC, "^PTA[0-9].*");              // Position sensors
        registry.addPattern(ComponentType.IC, "^PTB[0-9].*");              // Motion sensors
        registry.addPattern(ComponentType.IC, "^PTV[0-9].*");              // Potentiometers
        registry.addPattern(ComponentType.IC, "^3310[0-9].*");             // Encoders
        registry.addPattern(ComponentType.IC, "^PEC[0-9].*");              // Encoders (panel mount)

        // Trimmer Potentiometers
        registry.addPattern(ComponentType.IC, "^3005[0-9].*");             // Trimpot® (cermet)
        registry.addPattern(ComponentType.IC, "^3006[0-9].*");             // Trimpot® (wirewound)
        registry.addPattern(ComponentType.IC, "^3310[0-9].*");             // Trimpot® (SMD)

        // EMI/RFI Filters
        registry.addPattern(ComponentType.IC, "^BLM[0-9].*");              // Ferrite beads
        registry.addPattern(ComponentType.IC, "^BE[0-9].*");               // EMI filters
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.RESISTOR,
            ComponentType.RESISTOR_CHIP_BOURNS,
            ComponentType.INDUCTOR,
            ComponentType.INDUCTOR_CHIP_BOURNS,
            ComponentType.INDUCTOR_THT_BOURNS,
            ComponentType.POTENTIOMETER_BOURNS,
            ComponentType.TRANSFORMER_BOURNS,
            ComponentType.CIRCUIT_PROTECTION_BOURNS,
            ComponentType.TVS_DIODE_BOURNS,
            ComponentType.PPTC_FUSE_BOURNS
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Resistor packages
        if (upperMpn.startsWith("CR")) {
            try {
                String sizeCode = upperMpn.substring(2, 4);
                return switch (sizeCode) {
                    case "01" -> "0201/0603M";
                    case "02" -> "0402/1005M";
                    case "03" -> "0603/1608M";
                    case "06" -> "0805/2012M";
                    case "12" -> "1206/3216M";
                    case "20" -> "2010/5025M";
                    case "25" -> "2512/6432M";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Inductor packages
        if (upperMpn.startsWith("SR")) {
            try {
                String series = upperMpn.substring(0, 3);
                String size = extractInductorSize(upperMpn);
                return switch (series) {
                    case "SRN" -> "Non-shielded " + size;
                    case "SRP" -> "Power " + size;
                    case "SRR" -> "Shielded " + size;
                    default -> size;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Circuit protection packages
        if (upperMpn.startsWith("CDSOT") || upperMpn.startsWith("CDDFN")) {
            try {
                String pkgCode = upperMpn.substring(upperMpn.lastIndexOf('-') + 1);
                return switch (pkgCode) {
                    case "3" -> "SOT-23";
                    case "5" -> "SOT-353";
                    case "6" -> "SOT-363";
                    case "8" -> "DFN-8";
                    default -> pkgCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        return "";
    }

    private String extractInductorSize(String mpn) {
        try {
            String sizePart = mpn.substring(3, 7);
            return switch (sizePart) {
                case "2515" -> "2.5 x 1.5mm";
                case "3015" -> "3.0 x 1.5mm";
                case "4018" -> "4.0 x 1.8mm";
                case "5020" -> "5.0 x 2.0mm";
                case "6028" -> "6.0 x 2.8mm";
                case "7030" -> "7.0 x 3.0mm";
                case "1206" -> "12.0 x 6.0mm";
                default -> sizePart;
            };
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Resistors
        if (upperMpn.startsWith("CR")) {
            if (upperMpn.startsWith("CRA")) return "Anti-Sulfur Resistor";
            if (upperMpn.startsWith("CRF")) return "Fusible Resistor";
            if (upperMpn.startsWith("CRM")) return "MELF Resistor";
            return "Chip Resistor";
        }
        if (upperMpn.startsWith("PWR")) return "Power Resistor";

        // Inductors
        if (upperMpn.startsWith("SRN")) return "Non-Shielded Inductor";
        if (upperMpn.startsWith("SRP")) return "High Current Inductor";
        if (upperMpn.startsWith("SRR")) return "Shielded Inductor";
        if (upperMpn.startsWith("SDR")) return "Unshielded Inductor";
        if (upperMpn.startsWith("RLB")) return "Through-Hole Inductor";

        // Magnetics
        if (upperMpn.startsWith("PM")) return "Power Transformer";
        if (upperMpn.startsWith("PT")) return "Pulse Transformer";
        if (upperMpn.startsWith("SRF")) return "Common Mode Choke";

        // Circuit Protection
        if (upperMpn.startsWith("CDSOT")) return "TVS Diode Array";
        if (upperMpn.startsWith("CDDFN")) return "Low Cap TVS Array";
        if (upperMpn.startsWith("MOV")) return "Metal Oxide Varistor";
        if (upperMpn.startsWith("MF-R")) return "Resettable Fuse";
        if (upperMpn.startsWith("MF-S")) return "SMD Fuse";
        if (upperMpn.startsWith("SF-")) return "Fuse Holder";

        // Sensors and Controls
        if (upperMpn.startsWith("PTA")) return "Position Sensor";
        if (upperMpn.startsWith("PTB")) return "Motion Sensor";
        if (upperMpn.startsWith("PTV")) return "Potentiometer";
        if (upperMpn.startsWith("3310")) return "Encoder";
        if (upperMpn.startsWith("PEC")) return "Panel Encoder";

        // Trimmer Potentiometers
        if (upperMpn.startsWith("3005")) return "Cermet Trimpot";
        if (upperMpn.startsWith("3006")) return "Wirewound Trimpot";
        if (upperMpn.startsWith("3310")) return "SMD Trimpot";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) {
            // Check for compatible series
            if (isCompatibleSeries(series1, series2)) {
                return checkCompatibility(mpn1, mpn2);
            }
            return false;
        }

        return checkCompatibility(mpn1, mpn2);
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Anti-sulfur can replace standard resistors
        if (series1.equals("Anti-Sulfur Resistor") && series2.equals("Chip Resistor")) {
            return true;
        }

        // Shielded can replace unshielded inductors of same size
        if (series1.equals("Shielded Inductor") && series2.equals("Non-Shielded Inductor")) {
            return true;
        }

        return false;
    }

    private boolean checkCompatibility(String mpn1, String mpn2) {
        // Check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);
        if (!pkg1.equals(pkg2)) return false;

        // For resistors, check power rating and tolerance
        if (mpn1.startsWith("CR") && mpn2.startsWith("CR")) {
            try {
                return checkResistorCompatibility(mpn1, mpn2);
            } catch (Exception e) {
                return false;
            }
        }

        // For inductors, check current rating and DCR
        if (mpn1.startsWith("SR") && mpn2.startsWith("SR")) {
            try {
                return checkInductorCompatibility(mpn1, mpn2);
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private boolean checkResistorCompatibility(String mpn1, String mpn2) {
        // Extract and compare power ratings
        String power1 = extractPowerRating(mpn1);
        String power2 = extractPowerRating(mpn2);
        if (comparePowerRatings(power1, power2) < 0) return false;

        // Extract and compare tolerances
        String tol1 = extractTolerance(mpn1);
        String tol2 = extractTolerance(mpn2);
        return compareTolerances(tol1, tol2) <= 0;
    }

    private String extractPowerRating(String mpn) {
        // Example implementation - would need to be adjusted based on actual part numbering
        if (mpn.contains("-")) {
            String[] parts = mpn.split("-");
            for (String part : parts) {
                if (part.endsWith("W")) {
                    return part;
                }
            }
        }
        return "";
    }

    private int comparePowerRatings(String power1, String power2) {
        try {
            double p1 = Double.parseDouble(power1.replace("W", ""));
            double p2 = Double.parseDouble(power2.replace("W", ""));
            return Double.compare(p1, p2);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String extractTolerance(String mpn) {
        // Example: CR0603-F-1K0 -> F is tolerance code
        if (mpn.contains("-")) {
            String[] parts = mpn.split("-");
            for (String part : parts) {
                if (part.length() == 1 && "FGJMK".contains(part)) {
                    return part;
                }
            }
        }
        return "";
    }

    private int compareTolerances(String tol1, String tol2) {
        // F(±1%) > G(±2%) > J(±5%) > M(±10%) > K(±20%)
        String toleranceOrder = "FGJMK";
        int pos1 = toleranceOrder.indexOf(tol1);
        int pos2 = toleranceOrder.indexOf(tol2);
        if (pos1 < 0 || pos2 < 0) return 0;
        return Integer.compare(pos1, pos2);
    }

    private boolean checkInductorCompatibility(String mpn1, String mpn2) {
        // Extract and compare current ratings
        String current1 = extractCurrentRating(mpn1);
        String current2 = extractCurrentRating(mpn2);
        if (compareCurrentRatings(current1, current2) < 0) return false;

        // Check DCR if available
        String dcr1 = extractDCR(mpn1);
        String dcr2 = extractDCR(mpn2);
        return compareDCR(dcr1, dcr2) <= 0;
    }

    private String extractCurrentRating(String mpn) {
        // Implementation would depend on Bourns' specific part numbering
        return "";
    }

    private int compareCurrentRatings(String current1, String current2) {
        try {
            double i1 = Double.parseDouble(current1.replace("A", ""));
            double i2 = Double.parseDouble(current2.replace("A", ""));
            return Double.compare(i1, i2);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String extractDCR(String mpn) {
        // Implementation would depend on Bourns' specific part numbering
        return "";
    }

    private int compareDCR(String dcr1, String dcr2) {
        try {
            double d1 = Double.parseDouble(dcr1.replace("Ω", ""));
            double d2 = Double.parseDouble(dcr2.replace("Ω", ""));
            // Lower DCR is better
            return Double.compare(d1, d2);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}