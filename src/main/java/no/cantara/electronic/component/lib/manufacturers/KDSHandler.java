package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for KDS (Daishinku Corporation) crystals and oscillators.
 *
 * KDS is a major Japanese manufacturer of frequency control products including:
 * - DSX series: SMD crystals (DSX321G, DSX530GA, DSX840GA)
 * - DST series: Tuning fork crystals (DST310S, DST410S)
 * - DSO series: Clock oscillators (DSO321SR, DSO531SDH)
 * - DSB series: SAW filters/resonators (DSB321SDA)
 * - 1N series: Crystal units (1N-26.000)
 *
 * Package codes: G=SMD ceramic, S=surface mount specific, R=tape reel
 * Series is first 3-4 characters (DSX, DST, DSO, DSB, 1N)
 */
public class KDSHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // DSX series - SMD crystals
        registry.addPattern(ComponentType.CRYSTAL, "^DSX[0-9].*");           // Standard SMD crystals
        registry.addPattern(ComponentType.CRYSTAL, "^DSX[0-9]{3}G.*");       // SMD ceramic package crystals

        // DST series - Tuning fork crystals (32.768kHz typical)
        registry.addPattern(ComponentType.CRYSTAL, "^DST[0-9].*");           // Tuning fork crystals

        // DSO series - Clock oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^DSO[0-9].*");        // Standard clock oscillators
        registry.addPattern(ComponentType.OSCILLATOR, "^DSO[0-9]{3}S.*");    // SMD oscillators

        // DSB series - SAW filters and resonators
        registry.addPattern(ComponentType.IC, "^DSB[0-9].*");                // SAW filters/resonators

        // 1N series - Through-hole crystal units
        registry.addPattern(ComponentType.CRYSTAL, "^1N-[0-9].*");           // Crystal units (1N-26.000)
        registry.addPattern(ComponentType.CRYSTAL, "^1N[0-9].*");            // Crystal units without dash

        // DX series - Standard crystals
        registry.addPattern(ComponentType.CRYSTAL, "^DX[0-9].*");            // DX series crystals

        // SM series - Surface mount crystals
        registry.addPattern(ComponentType.CRYSTAL, "^SM[0-9].*");            // SM series crystals
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.CRYSTAL,
                ComponentType.OSCILLATOR,
                ComponentType.IC  // For SAW filters/resonators
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Explicit check for KDS patterns
        if (type == ComponentType.CRYSTAL) {
            if (upperMpn.matches("^DSX[0-9].*") ||
                upperMpn.matches("^DST[0-9].*") ||
                upperMpn.matches("^1N-?[0-9].*") ||
                upperMpn.matches("^DX[0-9].*") ||
                upperMpn.matches("^SM[0-9].*")) {
                return true;
            }
        }

        if (type == ComponentType.OSCILLATOR) {
            if (upperMpn.matches("^DSO[0-9].*")) {
                return true;
            }
        }

        if (type == ComponentType.IC) {
            if (upperMpn.matches("^DSB[0-9].*")) {
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

        // DSX series - extract size code and package suffix
        if (upperMpn.startsWith("DSX")) {
            try {
                // DSX321G - 321 is size code (3.2 x 1.3mm), G is package type
                String sizeCode = upperMpn.substring(3, 6);
                String packageSuffix = "";
                if (upperMpn.length() > 6) {
                    // Find the package suffix letter (G, GA, etc.)
                    int idx = 6;
                    while (idx < upperMpn.length() && Character.isLetter(upperMpn.charAt(idx))) {
                        idx++;
                    }
                    packageSuffix = upperMpn.substring(6, idx);
                }
                return mapSizeCodeToPackage(sizeCode, packageSuffix);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // DST series - tuning fork crystals
        if (upperMpn.startsWith("DST")) {
            try {
                // DST310S - 310 is size code, S is package type
                String sizeCode = upperMpn.substring(3, 6);
                return mapDSTSizeCode(sizeCode);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // DSO series - oscillators
        if (upperMpn.startsWith("DSO")) {
            try {
                // DSO321SR - 321 is size code, S is SMD, R is tape reel
                String sizeCode = upperMpn.substring(3, 6);
                return mapOscillatorSizeCode(sizeCode);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // DSB series - SAW devices
        if (upperMpn.startsWith("DSB")) {
            try {
                String sizeCode = upperMpn.substring(3, 6);
                return mapSAWSizeCode(sizeCode);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        // 1N series - extract from format like 1N-26.000
        if (upperMpn.startsWith("1N")) {
            return "HC-49U";  // Standard through-hole crystal package
        }

        return "";
    }

    private String mapSizeCodeToPackage(String sizeCode, String packageSuffix) {
        // Map KDS size codes to actual package dimensions
        String basePackage = switch (sizeCode) {
            case "211" -> "2.0 x 1.2mm";
            case "221" -> "2.0 x 1.2mm";
            case "321" -> "3.2 x 1.3mm";
            case "320" -> "3.2 x 2.0mm";
            case "530", "531" -> "5.0 x 3.2mm";
            case "750" -> "7.0 x 5.0mm";
            case "840" -> "8.0 x 4.5mm";
            case "860" -> "8.6 x 3.7mm";
            default -> sizeCode;
        };

        // Add package type indicator if present
        if (!packageSuffix.isEmpty()) {
            return basePackage + " " + decodePackageSuffix(packageSuffix);
        }
        return basePackage;
    }

    private String decodePackageSuffix(String suffix) {
        return switch (suffix.toUpperCase()) {
            case "G" -> "(SMD ceramic)";
            case "GA" -> "(SMD ceramic AEC-Q200)";
            case "S" -> "(SMD)";
            case "SR" -> "(SMD tape reel)";
            case "R" -> "(tape reel)";
            case "SDH" -> "(SMD high stability)";
            case "SDA" -> "(SMD automotive)";
            default -> "(" + suffix + ")";
        };
    }

    private String mapDSTSizeCode(String sizeCode) {
        return switch (sizeCode) {
            case "210" -> "2.0 x 1.2mm";
            case "310" -> "3.1 x 1.5mm";
            case "410" -> "4.1 x 1.5mm";
            case "520" -> "5.0 x 2.0mm";
            default -> sizeCode;
        };
    }

    private String mapOscillatorSizeCode(String sizeCode) {
        return switch (sizeCode) {
            case "211" -> "2.0 x 1.6mm";
            case "221" -> "2.0 x 1.6mm";
            case "321" -> "3.2 x 2.5mm";
            case "531" -> "5.0 x 3.2mm";
            case "750" -> "7.0 x 5.0mm";
            default -> sizeCode;
        };
    }

    private String mapSAWSizeCode(String sizeCode) {
        return switch (sizeCode) {
            case "211" -> "2.0 x 1.2mm";
            case "321" -> "3.2 x 1.3mm";
            case "531" -> "5.0 x 3.0mm";
            default -> sizeCode;
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";
        String upperMpn = mpn.toUpperCase();

        // DSX series - SMD crystals
        if (upperMpn.startsWith("DSX")) {
            if (upperMpn.contains("GA")) {
                return "DSX (SMD Crystal AEC-Q200)";
            }
            return "DSX (SMD Crystal)";
        }

        // DST series - Tuning fork crystals
        if (upperMpn.startsWith("DST")) {
            return "DST (Tuning Fork Crystal)";
        }

        // DSO series - Clock oscillators
        if (upperMpn.startsWith("DSO")) {
            if (upperMpn.contains("SDH")) {
                return "DSO (High Stability Oscillator)";
            }
            return "DSO (Clock Oscillator)";
        }

        // DSB series - SAW devices
        if (upperMpn.startsWith("DSB")) {
            if (upperMpn.contains("SDA")) {
                return "DSB (SAW Filter/Resonator AEC-Q200)";
            }
            return "DSB (SAW Filter/Resonator)";
        }

        // 1N series - Through-hole crystals
        if (upperMpn.startsWith("1N")) {
            return "1N (Crystal Unit)";
        }

        // DX series - Standard crystals
        if (upperMpn.startsWith("DX")) {
            return "DX (Standard Crystal)";
        }

        // SM series - Surface mount crystals
        if (upperMpn.startsWith("SM")) {
            return "SM (Surface Mount Crystal)";
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Different base series are not replacements
        String baseSeries1 = series1.split(" ")[0];
        String baseSeries2 = series2.split(" ")[0];
        if (!baseSeries1.equals(baseSeries2)) {
            return false;
        }

        // Same package required for replacement
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Extract just the dimension part for comparison
        String dim1 = pkg1.split(" ")[0];
        String dim2 = pkg2.split(" ")[0];
        if (!dim1.equals(dim2)) {
            return false;
        }

        // For crystals and oscillators, check frequency compatibility
        String freq1 = extractFrequencyCode(mpn1);
        String freq2 = extractFrequencyCode(mpn2);
        if (!freq1.isEmpty() && !freq2.isEmpty() && !freq1.equals(freq2)) {
            return false;
        }

        // AEC-Q200 grade can replace standard (upgrade path)
        if (series2.contains("AEC-Q200") && !series1.contains("AEC-Q200")) {
            return true;
        }

        // High stability can replace standard
        if (series2.contains("High Stability") && !series1.contains("High Stability")) {
            return true;
        }

        // Same series, same package, same frequency = compatible
        return series1.equals(series2);
    }

    private String extractFrequencyCode(String mpn) {
        if (mpn == null) return "";
        String upperMpn = mpn.toUpperCase();

        // 1N series has explicit frequency: 1N-26.000 = 26 MHz
        if (upperMpn.startsWith("1N-")) {
            int dashIdx = upperMpn.indexOf('-');
            if (dashIdx >= 0 && dashIdx < upperMpn.length() - 1) {
                return upperMpn.substring(dashIdx + 1);
            }
        }

        // For DSX/DST/DSO series, frequency may be encoded in suffix
        // Example: DSX321G-26.000M = 26 MHz
        int lastDash = upperMpn.lastIndexOf('-');
        if (lastDash >= 0 && lastDash < upperMpn.length() - 1) {
            String freqPart = upperMpn.substring(lastDash + 1);
            // Check if it looks like a frequency (contains numbers and possibly M/K suffix)
            if (freqPart.matches(".*\\d.*")) {
                return freqPart;
            }
        }

        return "";
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
