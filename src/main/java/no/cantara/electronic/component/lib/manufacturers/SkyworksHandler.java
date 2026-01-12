package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;


import java.util.Set;
import java.util.Collections;

public class SkyworksHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // RF Amplifiers
        registry.addPattern(ComponentType.IC, "^SKY[0-9]{5}-[0-9]{1,3}.*");  // RF Amplifiers
        registry.addPattern(ComponentType.IC, "^SE[0-9]{3,4}.*");            // RF Power Amplifiers

        // RF Switches
        registry.addPattern(ComponentType.IC, "^AS[0-9]{3,4}.*");           // RF Switches
        registry.addPattern(ComponentType.IC, "^SKY13[0-9]{3}.*");          // RF Switches
        registry.addPattern(ComponentType.IC, "^PE[0-9]{3,4}.*");           // RF Switches

        // RF Front-End Modules
        registry.addPattern(ComponentType.IC, "^SKY7[0-9]{4}.*");           // Front-end Modules
        registry.addPattern(ComponentType.IC, "^SE47[0-9]{2}.*");           // 4G/5G FEMs

        // RF Filters
        registry.addPattern(ComponentType.IC, "^SKY6[0-9]{4}.*");           // RF Filters
        registry.addPattern(ComponentType.IC, "^SF[0-9]{3,4}.*");           // SAW Filters

        // RF Mixers and Attenuators
        registry.addPattern(ComponentType.IC, "^SKY7[0-9]{4}.*");           // Mixers
        registry.addPattern(ComponentType.IC, "^AAT[0-9]{4}.*");            // Attenuators

        // Power Management
        registry.addPattern(ComponentType.IC, "^AAT[0-9]{4}.*");            // Power Management ICs
        registry.addPattern(ComponentType.IC, "^SC[0-9]{3,4}.*");           // Voltage Regulators

        // Silicon Labs Legacy Products
        // MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^EFM8[A-Z][A-Z][0-9]{2}.*");  // EFM8 8-bit MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^EFM32[A-Z][A-Z][0-9]{2}.*"); // EFM32 32-bit MCUs
        registry.addPattern(ComponentType.MICROCONTROLLER, "^EFR32[A-Z][A-Z][0-9]{2}.*"); // EFR32 Wireless MCUs

        // Wireless ICs
        registry.addPattern(ComponentType.IC, "^BGM[0-9]{3}.*");            // Bluetooth Modules
        registry.addPattern(ComponentType.IC, "^EFR32BG[0-9]{2}.*");        // Blue Gecko
        registry.addPattern(ComponentType.IC, "^EFR32FG[0-9]{2}.*");        // Flex Gecko
        registry.addPattern(ComponentType.IC, "^EFR32MG[0-9]{2}.*");        // Mighty Gecko
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];
            return switch (suffix) {
                case "QFN" -> "QFN";
                case "REEL" -> "REEL";
                case "TR" -> "REEL";
                case "89" -> "QFN";
                case "86" -> "CSP";
                case "481" -> "QFN48";
                case "321" -> "QFN32";
                case "WLCSP" -> "WLCSP";
                default -> {
                    // Check for embedded package codes
                    if (suffix.contains("QFN")) yield "QFN";
                    if (suffix.contains("CSP")) yield "CSP";
                    if (suffix.contains("BGA")) yield "BGA";
                    yield suffix;
                }
            };
        }
        return "";
    }
    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.RF_IC_SKYWORKS,
            ComponentType.IC,
            ComponentType.MICROCONTROLLER  // For EFM/EFR MCUs
        );
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series
        if (mpn.startsWith("SKY")) {
            // SKY series (e.g., SKY13350)
            return mpn.substring(0, Math.min(8, mpn.length()));
        } else if (mpn.startsWith("EFM8") || mpn.startsWith("EFM32") || mpn.startsWith("EFR32")) {
            // Silicon Labs MCU series
            StringBuilder series = new StringBuilder();
            boolean foundLetter = false;
            for (char c : mpn.toCharArray()) {
                if (Character.isLetter(c) || Character.isDigit(c)) {
                    if (Character.isLetter(c)) foundLetter = true;
                    if (!foundLetter || series.length() < 7) {
                        series.append(c);
                    } else {
                        break;
                    }
                }
            }
            return series.toString();
        } else {
            // Other series (AS, SE, PE, etc.)
            StringBuilder series = new StringBuilder();
            boolean foundDigit = false;
            for (char c : mpn.toCharArray()) {
                if (Character.isLetter(c) ||
                        (Character.isDigit(c) && (!foundDigit || series.length() < 5))) {
                    series.append(c);
                    if (Character.isDigit(c)) foundDigit = true;
                } else {
                    break;
                }
            }
            return series.toString();
        }
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series check
        if (!series1.equals(series2)) {
            return isCompatibleSeries(series1, series2);
        }

        // For RF components, check frequency and power specs
        if (mpn1.startsWith("SKY") || mpn1.startsWith("SE") ||
                mpn1.startsWith("AS") || mpn1.startsWith("PE")) {
            String freq1 = extractFrequencyBand(mpn1);
            String freq2 = extractFrequencyBand(mpn2);
            String power1 = extractPowerRating(mpn1);
            String power2 = extractPowerRating(mpn2);

            return isCompatibleFrequency(freq1, freq2) &&
                    isCompatiblePower(power1, power2);
        }

        // For MCUs, check flash/RAM size and features
        if (mpn1.startsWith("EFM") || mpn1.startsWith("EFR")) {
            String mem1 = extractMemorySize(mpn1);
            String mem2 = extractMemorySize(mpn2);
            return isCompatibleMemory(mem1, mem2);
        }

        return false;
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Define compatible series pairs
        return (
                // RF Amplifiers
                (series1.startsWith("SKY65") && series2.startsWith("SE65")) ||
                        (series1.startsWith("SE65") && series2.startsWith("SKY65")) ||

                        // RF Switches
                        (series1.startsWith("AS") && series2.startsWith("PE")) ||
                        (series1.startsWith("PE") && series2.startsWith("AS")) ||

                        // MCUs
                        (series1.startsWith("EFM32GG") && series2.startsWith("EFR32MG")) ||
                        (series1.startsWith("EFR32MG") && series2.startsWith("EFM32GG"))
        );
    }

    private String extractFrequencyBand(String mpn) {
        // Extract frequency specification
        if (mpn.contains("2G4")) return "2.4GHz";
        if (mpn.contains("5G")) return "5GHz";
        if (mpn.contains("6G")) return "6GHz";
        if (mpn.contains("SUB") || mpn.contains("LTE")) return "Sub-GHz";
        return "";
    }

    private String extractPowerRating(String mpn) {
        // Extract power rating
        StringBuilder power = new StringBuilder();
        boolean foundP = false;
        for (char c : mpn.toCharArray()) {
            if (c == 'P') {
                foundP = true;
            } else if (foundP && Character.isDigit(c)) {
                power.append(c);
            } else if (foundP && !power.isEmpty()) {
                break;
            }
        }
        return power.toString();
    }

    private boolean isCompatibleFrequency(String freq1, String freq2) {
        if (freq1.equals(freq2)) return true;
        if (freq1.isEmpty() || freq2.isEmpty()) return true;

        // Higher frequency parts can typically handle lower frequencies
        if (freq1.equals("6GHz")) return true;
        if (freq1.equals("5GHz") && !freq2.equals("6GHz")) return true;

        return false;
    }

    private boolean isCompatiblePower(String power1, String power2) {
        try {
            if (power1.isEmpty() || power2.isEmpty()) return true;

            int p1 = Integer.parseInt(power1);
            int p2 = Integer.parseInt(power2);

            // Higher power parts can typically handle lower power requirements
            return p1 >= p2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String extractMemorySize(String mpn) {
        // Extract memory size for MCUs
        StringBuilder size = new StringBuilder();
        boolean foundF = false;
        for (char c : mpn.toCharArray()) {
            if (c == 'F') {
                foundF = true;
            } else if (foundF && Character.isDigit(c)) {
                size.append(c);
            } else if (foundF && !size.isEmpty()) {
                break;
            }
        }
        return size.toString();
    }

    private boolean isCompatibleMemory(String size1, String size2) {
        try {
            if (size1.isEmpty() || size2.isEmpty()) return true;

            int s1 = Integer.parseInt(size1);
            int s2 = Integer.parseInt(size2);

            // Larger memory can typically replace smaller memory
            return s1 >= s2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}