package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

public class QualcommHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Mobile Platforms (Snapdragon)
        registry.addPattern(ComponentType.IC, "^SM[0-9]{3}.*");         // Mobile SoCs
        registry.addPattern(ComponentType.IC, "^SD[0-9]{3}.*");         // Snapdragon series
        registry.addPattern(ComponentType.IC, "^QSD[0-9]{4}.*");        // Snapdragon series
        registry.addPattern(ComponentType.IC, "^MSM[0-9]{4}.*");        // Mobile Station Modem

        // IoT/Embedded Platforms
        registry.addPattern(ComponentType.IC, "^QCS[0-9]{3}.*");        // IoT platforms
        registry.addPattern(ComponentType.IC, "^QCM[0-9]{3}.*");        // IoT modules
        registry.addPattern(ComponentType.IC, "^APQ[0-9]{4}.*");        // Application processors
        registry.addPattern(ComponentType.IC, "^IPQ[0-9]{4}.*");        // Network processors

        // RF Front-End
        registry.addPattern(ComponentType.IC, "^QM[0-9]{4}.*");         // RF modules
        registry.addPattern(ComponentType.IC, "^QPM[0-9]{4}.*");        // Power modules
        registry.addPattern(ComponentType.IC, "^QAT[0-9]{4}.*");        // Antenna tuners
        registry.addPattern(ComponentType.IC, "^QET[0-9]{4}.*");        // Envelope trackers

        // Wireless Connectivity
        registry.addPattern(ComponentType.IC, "^QCA[0-9]{4}.*");        // Wi-Fi/Bluetooth
        registry.addPattern(ComponentType.IC, "^WCN[0-9]{4}.*");        // Wireless connectivity
        registry.addPattern(ComponentType.IC, "^MDM[0-9]{4}.*");        // Modems
        registry.addPattern(ComponentType.IC, "^FSM[0-9]{4}.*");        // Small cell

        // Power Management
        registry.addPattern(ComponentType.IC, "^PM[0-9]{4}.*");         // PMICs
        registry.addPattern(ComponentType.IC, "^PMI[0-9]{4}.*");        // Power management
        registry.addPattern(ComponentType.IC, "^SMB[0-9]{3}.*");        // Battery charging

        // Audio Codecs
        registry.addPattern(ComponentType.IC, "^WCD[0-9]{4}.*");        // Audio codecs
        registry.addPattern(ComponentType.IC, "^WSA[0-9]{4}.*");        // Smart amplifiers
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.WIFI_IC_QUALCOMM);
       // types.add(ComponentType.WIFI_IC);
        // Could add other Qualcomm wireless/RF/mobile types if they exist in ComponentType enum
        // Such as cellular modems, SoCs, etc.
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];

            return switch (suffix) {
                case "BGA" -> "BGA";
                case "CSP" -> "CSP";
                case "POP" -> "Package-on-Package";
                case "PoP" -> "Package-on-Package";
                case "QFN" -> "QFN";
                case "PQFN" -> "Power QFN";
                case "LFCSP" -> "Lead Frame CSP";
                case "WLCSP" -> "Wafer Level CSP";
                case "FCBGA" -> "Flip-Chip BGA";
                default -> {
                    if (suffix.contains("BGA")) yield "BGA";
                    if (suffix.contains("CSP")) yield "CSP";
                    if (suffix.contains("QFN")) yield "QFN";
                    yield suffix;
                }
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series (first part before any dash)
        StringBuilder series = new StringBuilder();
        boolean foundLetter = false;
        boolean foundNumber = false;

        for (char c : mpn.toCharArray()) {
            if (c == '-') break;

            if (Character.isLetter(c)) {
                foundLetter = true;
                series.append(c);
            } else if (Character.isDigit(c)) {
                foundNumber = true;
                series.append(c);
            } else if (foundLetter && foundNumber) {
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

        // Check specific features based on product type
        if (isSnapdragonSoC(mpn1) && isSnapdragonSoC(mpn2)) {
            return checkSnapdragonCompatibility(mpn1, mpn2);
        }

        if (isWirelessModule(mpn1) && isWirelessModule(mpn2)) {
            return checkWirelessCompatibility(mpn1, mpn2);
        }

        if (isPowerManagement(mpn1) && isPowerManagement(mpn2)) {
            return checkPowerCompatibility(mpn1, mpn2);
        }

        return false;
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        if (series1.equals(series2)) return true;

        // Define compatible series pairs
        return (
                // Snapdragon SoC compatibility
                (series1.startsWith("SD8") && series2.startsWith("SD8")) ||
                        (series1.startsWith("SM8") && series2.startsWith("SM8")) ||

                        // IoT platform compatibility
                        (series1.startsWith("QCS4") && series2.startsWith("QCS4")) ||
                        (series1.startsWith("QCM2") && series2.startsWith("QCM2")) ||

                        // RF module compatibility
                        (series1.startsWith("QM4") && series2.startsWith("QM4")) ||

                        // Wi-Fi/Bluetooth compatibility
                        (series1.startsWith("QCA6") && series2.startsWith("QCA6")) ||
                        (series1.startsWith("WCN3") && series2.startsWith("WCN3"))
        );
    }

    private boolean isSnapdragonSoC(String mpn) {
        return mpn.startsWith("SM") || mpn.startsWith("SD") ||
                mpn.startsWith("QSD") || mpn.startsWith("MSM");
    }

    private boolean isWirelessModule(String mpn) {
        return mpn.startsWith("QCA") || mpn.startsWith("WCN") ||
                mpn.startsWith("MDM") || mpn.startsWith("FSM");
    }

    private boolean isPowerManagement(String mpn) {
        return mpn.startsWith("PM") || mpn.startsWith("PMI") ||
                mpn.startsWith("SMB");
    }

    private boolean checkSnapdragonCompatibility(String mpn1, String mpn2) {
        // Extract capabilities
        String gen1 = extractGeneration(mpn1);
        String gen2 = extractGeneration(mpn2);
        String tier1 = extractPerformanceTier(mpn1);
        String tier2 = extractPerformanceTier(mpn2);

        // Check generation compatibility
        if (!gen1.isEmpty() && !gen2.isEmpty()) {
            if (!isCompatibleGeneration(gen1, gen2)) {
                return false;
            }
        }

        // Check performance tier compatibility
        if (!tier1.isEmpty() && !tier2.isEmpty()) {
            if (!isCompatibleTier(tier1, tier2)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkWirelessCompatibility(String mpn1, String mpn2) {
        // Extract wireless capabilities
        String wifi1 = extractWiFiCapability(mpn1);
        String wifi2 = extractWiFiCapability(mpn2);
        String bt1 = extractBluetoothVersion(mpn1);
        String bt2 = extractBluetoothVersion(mpn2);

        // Check Wi-Fi compatibility
        if (!wifi1.isEmpty() && !wifi2.isEmpty()) {
            if (!isCompatibleWiFi(wifi1, wifi2)) {
                return false;
            }
        }

        // Check Bluetooth compatibility
        if (!bt1.isEmpty() && !bt2.isEmpty()) {
            if (!isCompatibleBluetooth(bt1, bt2)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkPowerCompatibility(String mpn1, String mpn2) {
        // Extract power capabilities
        String voltage1 = extractVoltageRating(mpn1);
        String voltage2 = extractVoltageRating(mpn2);
        String current1 = extractCurrentRating(mpn1);
        String current2 = extractCurrentRating(mpn2);

        // Check voltage compatibility
        if (!voltage1.isEmpty() && !voltage2.isEmpty()) {
            if (!isCompatibleVoltage(voltage1, voltage2)) {
                return false;
            }
        }

        // Check current compatibility
        if (!current1.isEmpty() && !current2.isEmpty()) {
            if (!isCompatibleCurrent(current1, current2)) {
                return false;
            }
        }

        return true;
    }

    private String extractGeneration(String mpn) {
        // Extract Snapdragon generation
        if (mpn.contains("Gen1")) return "1";
        if (mpn.contains("Gen2")) return "2";
        if (mpn.contains("Gen3")) return "3";

        // Extract from numeric part
        String[] parts = mpn.split("[^0-9]");
        for (String part : parts) {
            if (part.length() == 3) {
                return part.substring(0, 1);
            }
        }
        return "";
    }

    private String extractPerformanceTier(String mpn) {
        // Extract performance tier
        if (mpn.contains("Elite")) return "Elite";
        if (mpn.contains("Plus")) return "Plus";
        if (mpn.contains("Pro")) return "Pro";

        // Extract from numeric part
        String[] parts = mpn.split("[^0-9]");
        for (String part : parts) {
            if (part.length() == 3) {
                return part.substring(1, 2);
            }
        }
        return "";
    }

    private String extractWiFiCapability(String mpn) {
        if (mpn.contains("ax")) return "WiFi-6";
        if (mpn.contains("ac")) return "WiFi-5";
        if (mpn.contains("n")) return "WiFi-4";
        return "";
    }

    private String extractBluetoothVersion(String mpn) {
        if (mpn.contains("BT5.2")) return "5.2";
        if (mpn.contains("BT5.1")) return "5.1";
        if (mpn.contains("BT5.0")) return "5.0";
        if (mpn.contains("BT4.2")) return "4.2";
        return "";
    }

    private String extractVoltageRating(String mpn) {
        // Extract voltage rating from part number
        StringBuilder voltage = new StringBuilder();
        boolean foundV = false;
        for (char c : mpn.toCharArray()) {
            if (c == 'V') {
                foundV = true;
            } else if (foundV && Character.isDigit(c)) {
                voltage.append(c);
            } else if (foundV && !voltage.isEmpty()) {
                break;
            }
        }
        return voltage.toString();
    }

    private String extractCurrentRating(String mpn) {
        // Extract current rating from part number
        StringBuilder current = new StringBuilder();
        boolean foundA = false;
        for (char c : mpn.toCharArray()) {
            if (c == 'A') {
                foundA = true;
            } else if (foundA && Character.isDigit(c)) {
                current.append(c);
            } else if (foundA && !current.isEmpty()) {
                break;
            }
        }
        return current.toString();
    }

    private boolean isCompatibleGeneration(String gen1, String gen2) {
        try {
            int g1 = Integer.parseInt(gen1);
            int g2 = Integer.parseInt(gen2);
            // Higher generation can typically replace lower generation
            return g1 >= g2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCompatibleTier(String tier1, String tier2) {
        // Define tier hierarchy
        Map<String, Integer> tierRanking = Map.of(
                "Elite", 4,
                "Plus", 3,
                "Pro", 2,
                "Standard", 1
        );

        Integer rank1 = tierRanking.get(tier1);
        Integer rank2 = tierRanking.get(tier2);

        if (rank1 == null || rank2 == null) return false;

        // Higher tier can typically replace lower tier
        return rank1 >= rank2;
    }

    private boolean isCompatibleWiFi(String wifi1, String wifi2) {
        // Higher Wi-Fi versions can typically replace lower versions
        if (wifi1.equals("WiFi-6")) return true;
        if (wifi1.equals("WiFi-5") && !wifi2.equals("WiFi-6")) return true;
        if (wifi1.equals("WiFi-4") && wifi2.equals("WiFi-4")) return true;
        return false;
    }

    private boolean isCompatibleBluetooth(String bt1, String bt2) {
        try {
            double v1 = Double.parseDouble(bt1);
            double v2 = Double.parseDouble(bt2);
            // Higher BT versions can typically replace lower versions
            return v1 >= v2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCompatibleVoltage(String voltage1, String voltage2) {
        try {
            double v1 = Double.parseDouble(voltage1);
            double v2 = Double.parseDouble(voltage2);
            // Allow ±5% voltage tolerance
            return Math.abs(v1 - v2) / v2 <= 0.05;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCompatibleCurrent(String current1, String current2) {
        try {
            double c1 = Double.parseDouble(current1);
            double c2 = Double.parseDouble(current2);
            // Higher current rating can typically handle lower current requirements
            return c1 >= c2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}