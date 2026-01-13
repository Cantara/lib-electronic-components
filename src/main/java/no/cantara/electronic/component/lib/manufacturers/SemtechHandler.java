package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;

/**
 * Handler for Semtech Corporation components.
 *
 * Semtech specializes in:
 * - LoRa transceivers (SX1276, SX1278, SX1262, SX1268)
 * - LoRa SoCs/Gateway chips (SX1302, SX1303)
 * - ESD protection devices (RClamp, TVS: RCLAMP0524P, SLVU2.8-4)
 * - Signal integrity (ClearEdge: GN2033)
 * - Power management (SC series)
 */
public class SemtechHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // LoRa Transceivers - SX127x series (legacy)
        registry.addPattern(ComponentType.IC, "^SX127[0-9].*");           // SX1276, SX1278, SX1279

        // LoRa Transceivers - SX126x series (newer)
        registry.addPattern(ComponentType.IC, "^SX126[0-9].*");           // SX1261, SX1262, SX1268

        // LoRa Gateway Chips - SX130x series
        registry.addPattern(ComponentType.IC, "^SX130[0-9].*");           // SX1301, SX1302, SX1303

        // LoRa Transceivers - LR11xx series (newest)
        registry.addPattern(ComponentType.IC, "^LR11[0-9]{2}.*");         // LR1110, LR1120, LR1121

        // LoRa Edge - LLCC68
        registry.addPattern(ComponentType.IC, "^LLCC68.*");               // LLCC68 LoRa transceiver

        // LoRa SiP Modules - SX1250x series
        registry.addPattern(ComponentType.IC, "^SX125[0-9].*");           // SX1250, SX1255, SX1257

        // ESD Protection - RClamp series
        registry.addPattern(ComponentType.IC, "^RCLAMP[0-9].*");          // RCLAMP0524P, etc.

        // ESD Protection - SLVU series (TVS diodes)
        registry.addPattern(ComponentType.IC, "^SLVU[0-9].*");            // SLVU2.8-4, etc.

        // ESD Protection - SM series (SM712, SM15T33A, SM200)
        registry.addPattern(ComponentType.IC, "^SM[0-9]{2,4}[A-Z0-9]*.*");  // SM712, SM15T33A, SM200, etc.

        // ESD Protection - SMCJ series (TVS diodes)
        registry.addPattern(ComponentType.IC, "^SMCJ[0-9].*");            // SMCJ5.0A, etc.

        // ESD Protection - TVS2xxx series
        registry.addPattern(ComponentType.IC, "^TVS[0-9].*");             // TVS2200, etc.

        // Signal Integrity - ClearEdge GN series
        registry.addPattern(ComponentType.IC, "^GN[0-9]{4}.*");           // GN2033, GN2005, etc.

        // Signal Integrity - Retimers/Redrivers
        registry.addPattern(ComponentType.IC, "^GS[0-9]{4}.*");           // GS12090, etc.

        // Power Management - SC series
        registry.addPattern(ComponentType.IC, "^SC[0-9]{3,4}.*");         // SC4238, SC5501, etc.

        // Power Management - SY series (formerly Micrel)
        registry.addPattern(ComponentType.IC, "^SY[0-9]{4}.*");           // SY8089, SY8113, etc.

        // Power Management - TC series (formerly Micropower Direct)
        registry.addPattern(ComponentType.IC, "^TC[0-9]{4}.*");           // TC7660, etc.

        // Timing - Clock Generators/Buffers
        registry.addPattern(ComponentType.IC, "^NB[0-9].*");              // NB3N series clock buffers
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.IC,
            ComponentType.RF_IC_SKYWORKS,  // LoRa is RF technology
            ComponentType.ESD_PROTECTION_NEXPERIA  // Reuse ESD protection type
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract package from suffix after hyphen
        int dashIndex = upperMpn.lastIndexOf('-');
        if (dashIndex > 0 && dashIndex < upperMpn.length() - 1) {
            String suffix = upperMpn.substring(dashIndex + 1);

            // Known package codes
            return switch (suffix) {
                // QFN variants
                case "QFN" -> "QFN";
                case "QFN24" -> "QFN-24";
                case "QFN32" -> "QFN-32";
                case "QFN48" -> "QFN-48";
                case "TQFN" -> "TQFN";
                // TDFN variants
                case "TDFN" -> "TDFN";
                case "TDFN8" -> "TDFN-8";
                // SOT variants
                case "SOT23" -> "SOT-23";
                case "SOT23L" -> "SOT-23-6";
                case "SOT89" -> "SOT-89";
                case "SOT363" -> "SOT-363";
                case "SOT563" -> "SOT-563";
                // SC70 variants
                case "SC70" -> "SC70";
                case "SC70-3" -> "SC70-3";
                case "SC70-5" -> "SC70-5";
                case "SC70-6" -> "SC70-6";
                // Other packages
                case "SOIC" -> "SOIC";
                case "SOIC8" -> "SOIC-8";
                case "DFN" -> "DFN";
                case "DFN8" -> "DFN-8";
                case "MLF" -> "MLF";
                case "WLCSP" -> "WLCSP";
                case "LGA" -> "LGA";
                case "BGA" -> "BGA";
                // Tape & Reel variants
                case "TR" -> "Tape & Reel";
                case "TRG" -> "Tape & Reel (Green)";
                case "IMLT" -> "IMLT";
                case "IMLTR" -> "IMLT-TR";
                default -> {
                    // Check for embedded package codes
                    if (suffix.contains("QFN")) yield "QFN";
                    if (suffix.contains("SOT")) yield "SOT";
                    if (suffix.contains("DFN")) yield "DFN";
                    if (suffix.contains("SC70")) yield "SC70";
                    yield suffix;
                }
            };
        }

        // Some ESD parts encode package in the MPN itself
        if (upperMpn.startsWith("RCLAMP")) {
            // RCLAMP0524P - the P often indicates a specific package
            if (upperMpn.endsWith("P")) return "SOT-23";
            if (upperMpn.endsWith("T")) return "SC70";
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // LoRa transceivers - extract full series
        if (upperMpn.startsWith("SX127")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("SX126")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("SX130")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("SX125")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("LR11")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("LLCC68")) {
            return "LLCC68";
        }

        // ESD protection - extract series name
        if (upperMpn.startsWith("RCLAMP")) {
            // RCLAMP0524P -> RCLAMP
            return "RCLAMP";
        }
        if (upperMpn.startsWith("SLVU")) {
            // SLVU2.8-4 -> SLVU
            return "SLVU";
        }
        if (upperMpn.startsWith("SM") && Character.isDigit(upperMpn.charAt(2))) {
            // SM712 -> SM712
            return extractUpToDigitsOrDash(upperMpn, 5);
        }
        if (upperMpn.startsWith("SMCJ")) {
            return "SMCJ";
        }
        if (upperMpn.startsWith("TVS")) {
            return extractUpToDigitsOrDash(upperMpn, 7);
        }

        // Signal integrity - ClearEdge
        if (upperMpn.startsWith("GN")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("GS")) {
            return extractUpToDigitsOrDash(upperMpn, 7);
        }

        // Power management
        if (upperMpn.startsWith("SC")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("SY")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }
        if (upperMpn.startsWith("TC")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }

        // Clock buffers
        if (upperMpn.startsWith("NB")) {
            return extractUpToDigitsOrDash(upperMpn, 6);
        }

        // Default: take first 6 characters or up to first dash
        return extractUpToDigitsOrDash(upperMpn, 6);
    }

    /**
     * Extract series up to maxLength or first dash, including letters and digits.
     */
    private String extractUpToDigitsOrDash(String mpn, int maxLength) {
        StringBuilder series = new StringBuilder();
        for (int i = 0; i < mpn.length() && series.length() < maxLength; i++) {
            char c = mpn.charAt(i);
            if (c == '-') break;
            if (Character.isLetterOrDigit(c) || c == '.') {
                series.append(c);
            }
        }
        return series.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series with different packages are compatible
        if (series1.equals(series2)) {
            // Check for frequency/power compatibility for LoRa parts
            if (isLoRaPart(mpn1) && isLoRaPart(mpn2)) {
                return areLoRaPartsCompatible(mpn1, mpn2);
            }
            return true;
        }

        // Check for known compatible series
        return isCompatibleSeries(series1, series2);
    }

    private boolean isLoRaPart(String mpn) {
        String upper = mpn.toUpperCase();
        return upper.startsWith("SX127") || upper.startsWith("SX126") ||
               upper.startsWith("SX130") || upper.startsWith("LR11") ||
               upper.startsWith("LLCC");
    }

    private boolean areLoRaPartsCompatible(String mpn1, String mpn2) {
        // Extract frequency bands if encoded in MPN
        String freq1 = extractFrequencyBand(mpn1);
        String freq2 = extractFrequencyBand(mpn2);

        // If both have frequency info, they should match or be compatible
        if (!freq1.isEmpty() && !freq2.isEmpty()) {
            return isCompatibleFrequency(freq1, freq2);
        }

        return true;  // Same series without frequency differences
    }

    private String extractFrequencyBand(String mpn) {
        String upper = mpn.toUpperCase();
        // SX1278 is 433MHz/868MHz, SX1276 is 868MHz/915MHz
        // Extract numeric suffix for frequency indication
        if (upper.contains("433")) return "433";
        if (upper.contains("868")) return "868";
        if (upper.contains("915")) return "915";
        if (upper.contains("923")) return "923";
        return "";
    }

    private boolean isCompatibleFrequency(String freq1, String freq2) {
        return freq1.equals(freq2);
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // LoRa transceiver compatibility
        // SX127x and SX126x are NOT direct replacements (different APIs)
        // but may be upgrades
        if ((series1.startsWith("SX127") && series2.startsWith("SX127")) ||
            (series1.startsWith("SX126") && series2.startsWith("SX126"))) {
            return true;
        }

        // SX1261 and SX1262 are compatible (different power output)
        if (series1.startsWith("SX1261") && series2.startsWith("SX1262")) return true;
        if (series1.startsWith("SX1262") && series2.startsWith("SX1261")) return true;

        // SX1268 is compatible with SX1262 for same frequency bands
        if (series1.startsWith("SX1268") && series2.startsWith("SX1262")) return true;
        if (series1.startsWith("SX1262") && series2.startsWith("SX1268")) return true;

        // Gateway chips - SX1302 and SX1303 are compatible
        if (series1.startsWith("SX1302") && series2.startsWith("SX1303")) return true;
        if (series1.startsWith("SX1303") && series2.startsWith("SX1302")) return true;

        // LR11xx series are compatible within the family
        if (series1.startsWith("LR11") && series2.startsWith("LR11")) return true;

        // ESD protection - parts with same clamping voltage are compatible
        if (series1.equals("RCLAMP") && series2.equals("RCLAMP")) return true;
        if (series1.equals("SLVU") && series2.equals("SLVU")) return true;

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Override matches to handle Semtech-specific component type detection.
     */
    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // LoRa and RF parts
        if (type == ComponentType.IC || type == ComponentType.RF_IC_SKYWORKS) {
            if (upperMpn.matches("^SX12[0-9]{2}.*") ||
                upperMpn.matches("^SX13[0-9]{2}.*") ||
                upperMpn.matches("^SX125[0-9].*") ||
                upperMpn.matches("^LR11[0-9]{2}.*") ||
                upperMpn.matches("^LLCC68.*") ||
                upperMpn.matches("^GN[0-9]{4}.*") ||
                upperMpn.matches("^GS[0-9]{4}.*") ||
                upperMpn.matches("^SC[0-9]{3,4}.*") ||
                upperMpn.matches("^SY[0-9]{4}.*") ||
                upperMpn.matches("^TC[0-9]{4}.*") ||
                upperMpn.matches("^NB[0-9].*")) {
                return true;
            }
        }

        // ESD protection
        if (type == ComponentType.IC || type == ComponentType.ESD_PROTECTION_NEXPERIA) {
            if (upperMpn.matches("^RCLAMP[0-9].*") ||
                upperMpn.matches("^SLVU[0-9].*") ||
                upperMpn.matches("^SM[0-9]{2,4}[A-Z0-9]*.*") ||
                upperMpn.matches("^SMCJ[0-9].*") ||
                upperMpn.matches("^TVS[0-9].*")) {
                return true;
            }
        }

        // Fall back to pattern registry
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }
}
