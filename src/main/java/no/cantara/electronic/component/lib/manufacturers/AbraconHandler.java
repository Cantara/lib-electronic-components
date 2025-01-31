package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler for Abracon timing devices, crystals, and RF components
 */
public class AbraconHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Crystal Units
        registry.addPattern(ComponentType.CRYSTAL, "^ABM[0-9].*");          // Standard crystals
        registry.addPattern(ComponentType.CRYSTAL, "^ABL[0-9].*");          // Low profile crystals
        registry.addPattern(ComponentType.CRYSTAL, "^ABT[0-9].*");          // Tuning fork crystals
        registry.addPattern(ComponentType.CRYSTAL, "^ABS[0-9].*");          // Automotive crystals
        registry.addPattern(ComponentType.CRYSTAL_ABRACON, "^ABM[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_ABRACON, "^ABL[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_ABRACON, "^ABT[0-9].*");
        registry.addPattern(ComponentType.CRYSTAL_ABRACON, "^ABS[0-9].*");

        // Oscillators
        // Standard Clock Oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^ASCO[0-9].*");      // Standard oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^ASFL[0-9].*");      // Low power oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^ASE[0-9].*");       // EMI reduced oscillators
        registry.addPattern(ComponentType.OSCILLATOR_ABRACON, "^ASCO[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_ABRACON, "^ASFL[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_ABRACON, "^ASE[0-9].*");

        // TCXO (Temperature Compensated Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^ASTX[0-9].*");      // TCXO
        registry.addPattern(ComponentType.OSCILLATOR, "^ASVTX[0-9].*");     // VCTCXO
        registry.addPattern(ComponentType.OSCILLATOR_TCXO_ABRACON, "^ASTX[0-9].*");
        registry.addPattern(ComponentType.OSCILLATOR_TCXO_ABRACON, "^ASVTX[0-9].*");

        // VCXO (Voltage Controlled Crystal Oscillators)
        registry.addPattern(ComponentType.OSCILLATOR, "^ASV[0-9].*");       // VCXO
        registry.addPattern(ComponentType.OSCILLATOR_VCXO_ABRACON, "^ASXV[0-9].*");

        // Real-Time Clock Modules
        registry.addPattern(ComponentType.IC, "^AB[0-9].*RTC.*");          // RTC modules
        registry.addPattern(ComponentType.IC, "^ABRTS[0-9].*");            // RTC with super capacitor

        // RF Products
        registry.addPattern(ComponentType.IC, "^ABA[0-9].*");              // RF antennas
        registry.addPattern(ComponentType.IC, "^ABF[0-9].*");              // RF filters
        registry.addPattern(ComponentType.IC, "^ABB[0-9].*");              // RF baluns
        registry.addPattern(ComponentType.IC, "^ABUN[0-9].*");             // RF diplexers

        // Resonators
        registry.addPattern(ComponentType.CRYSTAL, "^ABMM[0-9].*");        // Ceramic resonators
        registry.addPattern(ComponentType.CRYSTAL, "^ABRC[0-9].*");        // Crystal resonators

        // Inductors
        registry.addPattern(ComponentType.INDUCTOR, "^AIAL[0-9].*");       // Air core inductors
        registry.addPattern(ComponentType.INDUCTOR, "^AIML[0-9].*");       // Multilayer inductors
        registry.addPattern(ComponentType.INDUCTOR, "^AIRP[0-9].*");       // Power inductors
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CRYSTAL);
        types.add(ComponentType.CRYSTAL_ABRACON);
        types.add(ComponentType.OSCILLATOR);
        types.add(ComponentType.OSCILLATOR_ABRACON);
        types.add(ComponentType.OSCILLATOR_TCXO_ABRACON);
        types.add(ComponentType.OSCILLATOR_VCXO_ABRACON);
        types.add(ComponentType.OSCILLATOR_OCXO_ABRACON);
        types.add(ComponentType.CLOCK_ABRACON);
        types.add(ComponentType.ANTENNA_ABRACON);
        types.add(ComponentType.RF_FILTER_ABRACON);
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Crystal packages
        if (upperMpn.startsWith("ABM") || upperMpn.startsWith("ABL")) {
            try {
                // Extract size code from the model number
                String sizeCode = upperMpn.substring(3, 5);
                return switch (sizeCode) {
                    case "02" -> "2.0 x 1.6mm";
                    case "03" -> "3.2 x 2.5mm";
                    case "07" -> "7.0 x 5.0mm";
                    case "08" -> "8.0 x 4.5mm";
                    case "10" -> "10.0 x 4.5mm";
                    case "11" -> "11.4 x 4.7mm";
                    case "13" -> "13.0 x 4.9mm";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Oscillator packages
        if (upperMpn.startsWith("ASC") || upperMpn.startsWith("ASE") ||
                upperMpn.startsWith("AST") || upperMpn.startsWith("ASV")) {
            try {
                // Find package code after the base part number
                int dashIndex = upperMpn.indexOf('-');
                if (dashIndex > 0) {
                    String pkgCode = upperMpn.substring(dashIndex + 1, dashIndex + 3);
                    return switch (pkgCode) {
                        case "B" -> "2.0 x 1.6mm";
                        case "C" -> "2.5 x 2.0mm";
                        case "D" -> "3.2 x 2.5mm";
                        case "E" -> "5.0 x 3.2mm";
                        case "F" -> "7.0 x 5.0mm";
                        default -> pkgCode;
                    };
                }
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // Inductor packages
        if (upperMpn.startsWith("AIA") || upperMpn.startsWith("AIM")) {
            try {
                String sizeCode = upperMpn.substring(4, 6);
                return switch (sizeCode) {
                    case "02" -> "0201";
                    case "03" -> "0302";
                    case "05" -> "0503";
                    case "10" -> "1005";
                    case "15" -> "1508";
                    case "20" -> "2010";
                    default -> sizeCode;
                };
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // Crystal series
        if (upperMpn.startsWith("ABM")) return "Standard Crystal";
        if (upperMpn.startsWith("ABL")) return "Low Profile Crystal";
        if (upperMpn.startsWith("ABT")) return "Tuning Fork Crystal";
        if (upperMpn.startsWith("ABS")) return "Automotive Crystal";
        if (upperMpn.startsWith("ABMM")) return "Ceramic Resonator";
        if (upperMpn.startsWith("ABRC")) return "Crystal Resonator";

        // Oscillator series
        if (upperMpn.startsWith("ASCO")) return "Standard Oscillator";
        if (upperMpn.startsWith("ASFL")) return "Low Power Oscillator";
        if (upperMpn.startsWith("ASE")) return "EMI Reduced Oscillator";
        if (upperMpn.startsWith("ASTX")) return "TCXO";
        if (upperMpn.startsWith("ASVTX")) return "VCTCXO";
        if (upperMpn.startsWith("ASV")) return "VCXO";

        // RTC series
        if (upperMpn.contains("RTC")) return "RTC Module";
        if (upperMpn.startsWith("ABRTS")) return "RTC with SuperCap";

        // RF series
        if (upperMpn.startsWith("ABA")) return "RF Antenna";
        if (upperMpn.startsWith("ABF")) return "RF Filter";
        if (upperMpn.startsWith("ABB")) return "RF Balun";
        if (upperMpn.startsWith("ABUN")) return "RF Diplexer";

        // Inductor series
        if (upperMpn.startsWith("AIAL")) return "Air Core Inductor";
        if (upperMpn.startsWith("AIML")) return "Multilayer Inductor";
        if (upperMpn.startsWith("AIRP")) return "Power Inductor";

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different series are not replacements
        if (!series1.equals(series2)) return false;

        // For crystals and oscillators, check package and frequency
        if (mpn1.startsWith("AB") || mpn1.startsWith("AS")) {
            String pkg1 = extractPackageCode(mpn1);
            String pkg2 = extractPackageCode(mpn2);
            if (!pkg1.equals(pkg2)) return false;

            try {
                String freq1 = extractFrequencyCode(mpn1);
                String freq2 = extractFrequencyCode(mpn2);
                if (!freq1.equals(freq2)) return false;

                // Check stability grade if applicable
                String stability1 = extractStabilityCode(mpn1);
                String stability2 = extractStabilityCode(mpn2);

                // Higher stability can replace lower stability
                return isCompatibleStability(stability1, stability2);
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private String extractFrequencyCode(String mpn) {
        // Find frequency specification (typically after dash)
        int dashIndex = mpn.lastIndexOf('-');
        if (dashIndex >= 0 && dashIndex < mpn.length() - 1) {
            String freqPart = mpn.substring(dashIndex + 1);
            // Remove any suffix after frequency (like stability code)
            int suffixIndex = freqPart.indexOf('-');
            return suffixIndex > 0 ? freqPart.substring(0, suffixIndex) : freqPart;
        }
        return "";
    }

    private String extractStabilityCode(String mpn) {
        // Stability code typically follows frequency
        String[] parts = mpn.split("-");
        if (parts.length > 2) {
            return parts[parts.length - 1];
        }
        return "";
    }

    private boolean isCompatibleStability(String stability1, String stability2) {
        // Convert stability codes to PPM values and compare
        // This is a simplified example - actual implementation would need
        // to handle all Abracon stability codes
        try {
            int ppm1 = Integer.parseInt(stability1.replace("PPM", ""));
            int ppm2 = Integer.parseInt(stability2.replace("PPM", ""));
            // Lower PPM (higher stability) can replace higher PPM
            return ppm1 <= ppm2;
        } catch (NumberFormatException e) {
            return stability1.equals(stability2);
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        // Return empty set as we're not using manufacturer-specific types yet
        return Collections.emptySet();
    }
}