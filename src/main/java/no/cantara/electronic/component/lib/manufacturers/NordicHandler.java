package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class NordicHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // nRF51 Series
        registry.addPattern(ComponentType.IC, "^nRF51[0-9]{3}.*");    // nRF51 Series SoCs
        registry.addPattern(ComponentType.IC, "^nRF51822.*");         // nRF51822 specific
        registry.addPattern(ComponentType.IC, "^nRF51824.*");         // nRF51824 specific

        // nRF52 Series
        registry.addPattern(ComponentType.IC, "^nRF52[0-9]{3}.*");    // nRF52 Series SoCs
        registry.addPattern(ComponentType.IC, "^nRF52810.*");         // nRF52810 specific
        registry.addPattern(ComponentType.IC, "^nRF52811.*");         // nRF52811 specific
        registry.addPattern(ComponentType.IC, "^nRF52832.*");         // nRF52832 specific
        registry.addPattern(ComponentType.IC, "^nRF52833.*");         // nRF52833 specific
        registry.addPattern(ComponentType.IC, "^nRF52840.*");         // nRF52840 specific

        // nRF53 Series
        registry.addPattern(ComponentType.IC, "^nRF53[0-9]{3}.*");    // nRF53 Series SoCs
        registry.addPattern(ComponentType.IC, "^nRF5340.*");          // nRF5340 specific

        // nRF91 Series
        registry.addPattern(ComponentType.IC, "^nRF91[0-9]{3}.*");    // nRF91 Series SoCs
        registry.addPattern(ComponentType.IC, "^nRF9160.*");          // nRF9160 specific

        // Development Kits
        registry.addPattern(ComponentType.IC, "^nRF.*DK$");           // Development Kits
        registry.addPattern(ComponentType.IC, "^nRF.*PDK$");          // Platform Development Kits

        // Modules
        registry.addPattern(ComponentType.IC, "^nRF.*Module.*");      // Modules
        registry.addPattern(ComponentType.IC, "^nRF.*Dongle.*");      // USB Dongles
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.BLUETOOTH_IC_NORDIC);
        // Add other Nordic Semiconductor wireless/RF types if they exist in ComponentType enum
        // Could include nRF series microcontrollers and other wireless ICs
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String[] parts = mpn.split("-");
        if (parts.length > 0) {
            String suffix = parts[parts.length - 1];

            return switch (suffix) {
                case "QFAA" -> "QFN48";
                case "QFAB" -> "QFN48";
                case "QFAC" -> "QFN48";
                case "CIAA" -> "WLCSP";
                case "CAAA" -> "WLCSP";
                case "CEAA" -> "WLCSP";
                case "CKAA" -> "QFN73";
                case "QIAA" -> "QFN48";
                default -> {
                    if (suffix.startsWith("Q")) yield "QFN";
                    if (suffix.startsWith("C")) yield "WLCSP";
                    yield suffix;
                }
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series (e.g., nRF52840, nRF5340)
        StringBuilder series = new StringBuilder();
        boolean foundDigit = false;

        for (char c : mpn.toCharArray()) {
            if (Character.isLetter(c) ||
                    Character.isDigit(c) && (!foundDigit || series.length() < 8)) {
                series.append(c);
                if (Character.isDigit(c)) foundDigit = true;
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

        // Check if parts are from compatible series
        if (!isCompatibleSeries(series1, series2)) {
            return false;
        }

        // Check memory and features
        String memory1 = extractMemorySize(mpn1);
        String memory2 = extractMemorySize(mpn2);

        if (!memory1.isEmpty() && !memory2.isEmpty()) {
            if (!isCompatibleMemory(memory1, memory2)) {
                return false;
            }
        }

        // Check radio capabilities
        String radio1 = extractRadioCapabilities(mpn1);
        String radio2 = extractRadioCapabilities(mpn2);

        return isCompatibleRadio(radio1, radio2);
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // Define compatible series
        if (series1.equals(series2)) return true;

        // nRF52 series compatibility
        if (series1.startsWith("nRF528") && series2.startsWith("nRF528")) {
            // nRF52840 can replace nRF52832
            if (series1.equals("nRF52840")) return true;
            if (series2.equals("nRF52840")) return false;
            // nRF52833 can replace nRF52832
            if (series1.equals("nRF52833")) return true;
            if (series2.equals("nRF52833")) return false;
        }

        return false;
    }

    private String extractMemorySize(String mpn) {
        // Extract memory size from variant code
        if (mpn.contains("256KB")) return "256";
        if (mpn.contains("512KB")) return "512";
        if (mpn.contains("1MB")) return "1024";
        return "";
    }

    private String extractRadioCapabilities(String mpn) {
        StringBuilder capabilities = new StringBuilder();
        if (mpn.contains("BLE")) capabilities.append("BLE");
        if (mpn.contains("ANT")) capabilities.append("ANT");
        if (mpn.contains("802.15.4")) capabilities.append("THREAD");
        if (mpn.contains("LTE")) capabilities.append("LTE");
        return capabilities.toString();
    }

    private boolean isCompatibleMemory(String mem1, String mem2) {
        try {
            int size1 = Integer.parseInt(mem1);
            int size2 = Integer.parseInt(mem2);
            // Larger memory can replace smaller memory
            return size1 >= size2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCompatibleRadio(String radio1, String radio2) {
        // Check if radio1 supports all capabilities of radio2
        if (radio2.contains("BLE") && !radio1.contains("BLE")) return false;
        if (radio2.contains("ANT") && !radio1.contains("ANT")) return false;
        if (radio2.contains("THREAD") && !radio1.contains("THREAD")) return false;
        if (radio2.contains("LTE") && !radio1.contains("LTE")) return false;
        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}