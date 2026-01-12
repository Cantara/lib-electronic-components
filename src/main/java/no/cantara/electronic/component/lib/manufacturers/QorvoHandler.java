package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;


import java.util.Set;
import java.util.Collections;

public class QorvoHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // RF Power Amplifiers
        registry.addPattern(ComponentType.IC, "^QPA[0-9]{4}.*");     // Power amplifiers
        registry.addPattern(ComponentType.IC, "^TQP[0-9]{4}.*");     // Power amplifiers
        registry.addPattern(ComponentType.IC, "^RF[0-9]{4}.*");      // RF amplifiers

        // RF Switches
        registry.addPattern(ComponentType.IC, "^QPC[0-9]{4}.*");     // RF switches
        registry.addPattern(ComponentType.IC, "^RFSW[0-9]{4}.*");    // RF switches
        registry.addPattern(ComponentType.IC, "^PE4[0-9]{4}.*");     // RF switches (TriQuint)

        // RF Filters
        registry.addPattern(ComponentType.IC, "^885[0-9]{3}.*");     // RF filters
        registry.addPattern(ComponentType.IC, "^854[0-9]{3}.*");     // RF filters
        registry.addPattern(ComponentType.IC, "^TQM[0-9]{3}.*");     // RF filters

        // Front-End Modules
        registry.addPattern(ComponentType.IC, "^RF5[0-9]{3}.*");     // Front-end modules
        registry.addPattern(ComponentType.IC, "^RFF[0-9]{4}.*");     // Front-end modules
        registry.addPattern(ComponentType.IC, "^QPF[0-9]{4}.*");     // Front-end modules

        // LNAs (Low Noise Amplifiers)
        registry.addPattern(ComponentType.IC, "^QPL[0-9]{4}.*");     // LNAs
        registry.addPattern(ComponentType.IC, "^TQP3[0-9]{3}.*");    // LNAs
        registry.addPattern(ComponentType.IC, "^SPF[0-9]{4}.*");     // LNAs

        // Phase Shifters
        registry.addPattern(ComponentType.IC, "^QPS[0-9]{4}.*");     // Phase shifters
        registry.addPattern(ComponentType.IC, "^RFPS[0-9]{4}.*");    // Phase shifters

        // RF Mixers
        registry.addPattern(ComponentType.IC, "^QPM[0-9]{4}.*");     // Mixers
        registry.addPattern(ComponentType.IC, "^TQM[0-9]{4}.*");     // Mixers

        // Wi-Fi/Bluetooth Modules
        registry.addPattern(ComponentType.IC, "^CX2[0-9]{4}.*");     // Wi-Fi modules
        registry.addPattern(ComponentType.IC, "^RFX[0-9]{4}.*");     // RF transceivers

        // Power Management
        registry.addPattern(ComponentType.IC, "^ACT[0-9]{4}.*");     // Power management ICs
        registry.addPattern(ComponentType.IC, "^PAC[0-9]{4}.*");     // Power amplifier controllers
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.RF_IC_QORVO,
            ComponentType.IC
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Handle package codes in suffix
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];

            return switch (suffix) {
                case "TR1" -> "QFN-TR";      // QFN Tape & Reel
                case "BU" -> "WLCSP";        // Wafer Level CSP
                case "EN" -> "DFN";          // Dual Flat No-Lead
                case "GM" -> "MCM";          // Multi-Chip Module
                case "ML" -> "QFN";          // QFN Standard
                case "CS" -> "CSP";          // Chip Scale Package
                case "LAM" -> "LAMINATE";    // Laminate Package
                default -> {
                    // Check for embedded package codes
                    if (suffix.contains("QFN")) yield "QFN";
                    if (suffix.contains("DFN")) yield "DFN";
                    if (suffix.contains("LGA")) yield "LGA";
                    yield suffix;
                }
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series
        StringBuilder series = new StringBuilder();
        boolean foundDigit = false;
        int count = 0;

        for (char c : mpn.toCharArray()) {
            if (Character.isLetter(c)) {
                if (!foundDigit) series.append(c);
            } else if (Character.isDigit(c)) {
                foundDigit = true;
                if (count < 4) {  // Take first 4 digits
                    series.append(c);
                    count++;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return series.toString();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Check if series are directly compatible
        if (series1.equals(series2)) {
            return checkCompatibility(mpn1, mpn2);
        }

        // Check for known cross-series compatibility
        return isCompatibleSeries(series1, series2) &&
                checkCompatibility(mpn1, mpn2);
    }

    private boolean checkCompatibility(String mpn1, String mpn2) {
        // Extract specifications
        String freq1 = extractFrequencyBand(mpn1);
        String freq2 = extractFrequencyBand(mpn2);
        String power1 = extractPowerRating(mpn1);
        String power2 = extractPowerRating(mpn2);
        String gain1 = extractGain(mpn1);
        String gain2 = extractGain(mpn2);

        // Check specification compatibility
        return isCompatibleFrequency(freq1, freq2) &&
                isCompatiblePower(power1, power2) &&
                isCompatibleGain(gain1, gain2);
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Define compatible series pairs
        return (
                // Power Amplifiers
                (series1.startsWith("QPA") && series2.startsWith("TQP")) ||
                        (series1.startsWith("TQP") && series2.startsWith("QPA")) ||

                        // RF Switches
                        (series1.startsWith("QPC") && series2.startsWith("RFSW")) ||
                        (series1.startsWith("RFSW") && series2.startsWith("QPC")) ||
                        (series1.startsWith("PE4") && series2.startsWith("QPC")) ||

                        // Front-End Modules
                        (series1.startsWith("RF5") && series2.startsWith("RFF")) ||
                        (series1.startsWith("RFF") && series2.startsWith("QPF"))
        );
    }

    private String extractFrequencyBand(String mpn) {
        // Extract frequency band information
        if (mpn.contains("24G") || mpn.contains("2G4")) return "2.4GHz";
        if (mpn.contains("5G")) return "5GHz";
        if (mpn.contains("6G")) return "6GHz";
        if (mpn.contains("SUB") || mpn.contains("LTE")) return "Sub-GHz";
        if (mpn.contains("UWB")) return "UWB";

        // Check frequency codes in the part number
        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String freqPart = parts[1];
            if (freqPart.matches("\\d{3,4}")) {
                int freq = Integer.parseInt(freqPart);
                if (freq < 1000) return freq + "MHz";
                return (freq / 1000.0) + "GHz";
            }
        }

        return "";
    }

    private String extractPowerRating(String mpn) {
        // Extract power rating
        if (mpn.contains("P0")) return "0dBm";
        if (mpn.contains("P10")) return "10dBm";
        if (mpn.contains("P20")) return "20dBm";
        if (mpn.contains("P30")) return "30dBm";

        // Look for power specification in format Pxx
        int pIndex = mpn.indexOf('P');
        if (pIndex >= 0 && pIndex + 2 < mpn.length()) {
            String power = mpn.substring(pIndex + 1, pIndex + 3);
            try {
                Integer.parseInt(power);
                return power + "dBm";
            } catch (NumberFormatException e) {
                // Not a valid power specification
            }
        }

        return "";
    }

    private String extractGain(String mpn) {
        // Extract gain specification
        if (mpn.contains("G10")) return "10dB";
        if (mpn.contains("G20")) return "20dB";
        if (mpn.contains("G30")) return "30dB";

        // Look for gain specification in format Gxx
        int gIndex = mpn.indexOf('G');
        if (gIndex >= 0 && gIndex + 2 < mpn.length()) {
            String gain = mpn.substring(gIndex + 1, gIndex + 3);
            try {
                Integer.parseInt(gain);
                return gain + "dB";
            } catch (NumberFormatException e) {
                // Not a valid gain specification
            }
        }

        return "";
    }

    private boolean isCompatibleFrequency(String freq1, String freq2) {
        if (freq1.equals(freq2)) return true;
        if (freq1.isEmpty() || freq2.isEmpty()) return true;

        // Convert to MHz for comparison
        double f1 = parseFrequency(freq1);
        double f2 = parseFrequency(freq2);

        if (f1 <= 0 || f2 <= 0) return false;

        // Higher frequency parts can typically handle lower frequencies
        // within a reasonable range (20% overlap)
        return f1 >= f2 && (f1 - f2) / f2 <= 0.2;
    }

    private double parseFrequency(String freq) {
        try {
            double value = Double.parseDouble(freq.replaceAll("[^0-9.]", ""));
            if (freq.endsWith("GHz")) value *= 1000;
            return value;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isCompatiblePower(String power1, String power2) {
        if (power1.equals(power2)) return true;
        if (power1.isEmpty() || power2.isEmpty()) return true;

        try {
            int p1 = Integer.parseInt(power1.replaceAll("\\D", ""));
            int p2 = Integer.parseInt(power2.replaceAll("\\D", ""));

            // Higher power parts can handle lower power requirements
            return p1 >= p2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCompatibleGain(String gain1, String gain2) {
        if (gain1.equals(gain2)) return true;
        if (gain1.isEmpty() || gain2.isEmpty()) return true;

        try {
            int g1 = Integer.parseInt(gain1.replaceAll("\\D", ""));
            int g2 = Integer.parseInt(gain2.replaceAll("\\D", ""));

            // Allow ±3dB variation
            return Math.abs(g1 - g2) <= 3;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}