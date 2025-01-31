package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class BroadcomHandler implements ManufacturerHandler {
    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Wi-Fi/Bluetooth Combo Chips
        registry.addPattern(ComponentType.IC, "^BCM[2-4][0-9]{4}.*");   // Wi-Fi/BT combos
        registry.addPattern(ComponentType.IC, "^BCM43[0-9]{3}.*");      // Wi-Fi 5/6/6E
        registry.addPattern(ComponentType.IC, "^BCM89[0-9]{3}.*");      // Wi-Fi 6/6E
        registry.addPattern(ComponentType.IC, "^BCM4774[0-9].*");       // GNSS combos

        // Network Switches
        registry.addPattern(ComponentType.IC, "^BCM53[0-9]{3}.*");      // Ethernet switches
        registry.addPattern(ComponentType.IC, "^BCM56[0-9]{3}.*");      // StrataXGS switches
        registry.addPattern(ComponentType.IC, "^BCM58[0-9]{3}.*");      // Trident switches
        registry.addPattern(ComponentType.IC, "^BCM88[0-9]{3}.*");      // StrataDNX switches

        // Network Controllers
        registry.addPattern(ComponentType.IC, "^BCM57[0-9]{3}.*");      // NetXtreme controllers
        registry.addPattern(ComponentType.IC, "^BCM5719.*");            // NetXtreme GbE
        registry.addPattern(ComponentType.IC, "^BCM5720.*");            // NetXtreme GbE
        registry.addPattern(ComponentType.IC, "^BCM5762.*");            // NetXtreme GbE

        // Storage Controllers
        registry.addPattern(ComponentType.IC, "^BCM1600.*");            // SAS controllers
        registry.addPattern(ComponentType.IC, "^BCM2200.*");            // RAID controllers
        registry.addPattern(ComponentType.IC, "^BCM8450.*");            // NVMe controllers

        // Fiber Channel
        registry.addPattern(ComponentType.IC, "^BCM425[0-9]{2}.*");     // Fiber Channel
        registry.addPattern(ComponentType.IC, "^BCM578[0-9]{2}.*");     // Fiber Channel HBAs

        // PHYs and MACs
        registry.addPattern(ComponentType.IC, "^BCM5241[0-9].*");       // Gigabit PHYs
        registry.addPattern(ComponentType.IC, "^BCM5461[0-9].*");       // Gigabit PHYs
        registry.addPattern(ComponentType.IC, "^BCM5482[0-9].*");       // Dual PHYs

        // Optical Components
        registry.addPattern(ComponentType.IC, "^BCM876[0-9]{2}.*");     // Optical transceivers
        registry.addPattern(ComponentType.IC, "^AFCT-[0-9]{4}.*");      // Fiber transceivers

        // PCIe Switches
        registry.addPattern(ComponentType.IC, "^PEX[0-9]{4}.*");        // PCIe switches
        registry.addPattern(ComponentType.IC, "^PLX[0-9]{4}.*");        // PLX PCIe switches
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
      //  types.add(ComponentType.OPTOCOUPLER);
      //  types.add(ComponentType.RF_IC);
      //  types.add(ComponentType.WIFI_IC);
        // Include former Avago technologies products
        // Add other Broadcom-specific types if they exist in ComponentType enum
        // Could include networking ICs, fiber optic components, etc.
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String[] parts = mpn.split("-");
        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];

            return switch (suffix) {
                case "KF" -> "FCBGA";           // Fine-pitch BGA
                case "KU" -> "UBGA";            // Ultra-fine BGA
                case "B0" -> "BGA";             // Standard BGA
                case "MC" -> "MCM";             // Multi-chip module
                case "FP" -> "FCBGA-POD";       // Package-on-package
                case "LP" -> "LFBGA";           // Low-profile FBGA
                case "HP" -> "HSBGA";           // Heat-sink BGA
                case "WH" -> "WLCSP";           // Wafer-level CSP
                case "FL" -> "FLIP-CHIP";       // Flip-chip BGA
                default -> {
                    if (suffix.startsWith("BG")) yield "BGA";
                    if (suffix.startsWith("FC")) yield "FCBGA";
                    if (suffix.startsWith("WL")) yield "WLCSP";
                    yield suffix;
                }
            };
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        // Extract base series (first 7-8 characters for BCM parts)
        StringBuilder series = new StringBuilder();
        boolean foundDigit = false;
        int digitCount = 0;

        for (char c : mpn.toCharArray()) {
            if (Character.isLetter(c) ||
                    (Character.isDigit(c) && (!foundDigit || digitCount < 4))) {
                series.append(c);
                if (Character.isDigit(c)) {
                    foundDigit = true;
                    digitCount++;
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

        // Check if parts are from compatible series
        if (!isCompatibleSeries(series1, series2)) {
            return false;
        }

        // Check specific features based on product type
        if (isWiFiBluetoothChip(mpn1) && isWiFiBluetoothChip(mpn2)) {
            return checkWiFiBluetoothCompatibility(mpn1, mpn2);
        }

        if (isNetworkSwitch(mpn1) && isNetworkSwitch(mpn2)) {
            return checkNetworkSwitchCompatibility(mpn1, mpn2);
        }

        if (isStorageController(mpn1) && isStorageController(mpn2)) {
            return checkStorageControllerCompatibility(mpn1, mpn2);
        }

        return false;
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        if (series1.equals(series2)) return true;

        // Define compatible series pairs
        return (
                // Wi-Fi/Bluetooth compatibility
                (series1.startsWith("BCM4375") && series2.startsWith("BCM4373")) ||
                        (series1.startsWith("BCM4389") && series2.startsWith("BCM4375")) ||

                        // Network switch compatibility
                        (series1.startsWith("BCM5675") && series2.startsWith("BCM5673")) ||
                        (series1.startsWith("BCM5682") && series2.startsWith("BCM5681")) ||

                        // Storage controller compatibility
                        (series1.startsWith("BCM2210") && series2.startsWith("BCM2209")) ||
                        (series1.startsWith("BCM2220") && series2.startsWith("BCM2210"))
        );
    }

    private boolean isWiFiBluetoothChip(String mpn) {
        return mpn.startsWith("BCM4") || mpn.startsWith("BCM89");
    }

    private boolean isNetworkSwitch(String mpn) {
        return mpn.startsWith("BCM53") || mpn.startsWith("BCM56") ||
                mpn.startsWith("BCM58") || mpn.startsWith("BCM88");
    }

    private boolean isStorageController(String mpn) {
        return mpn.startsWith("BCM16") || mpn.startsWith("BCM22") ||
                mpn.startsWith("BCM84");
    }

    private boolean checkWiFiBluetoothCompatibility(String mpn1, String mpn2) {
        // Extract Wi-Fi/Bluetooth capabilities
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

    private boolean checkNetworkSwitchCompatibility(String mpn1, String mpn2) {
        // Extract switch capabilities
        String speed1 = extractNetworkSpeed(mpn1);
        String speed2 = extractNetworkSpeed(mpn2);
        String ports1 = extractPortCount(mpn1);
        String ports2 = extractPortCount(mpn2);

        // Check speed compatibility
        if (!speed1.isEmpty() && !speed2.isEmpty()) {
            if (!isCompatibleSpeed(speed1, speed2)) {
                return false;
            }
        }

        // Check port count compatibility
        if (!ports1.isEmpty() && !ports2.isEmpty()) {
            if (!isCompatiblePorts(ports1, ports2)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkStorageControllerCompatibility(String mpn1, String mpn2) {
        // Extract storage capabilities
        String interface1 = extractStorageInterface(mpn1);
        String interface2 = extractStorageInterface(mpn2);
        String ports1 = extractPortCount(mpn1);
        String ports2 = extractPortCount(mpn2);

        // Check interface compatibility
        if (!interface1.isEmpty() && !interface2.isEmpty()) {
            if (!interface1.equals(interface2)) {
                return false;
            }
        }

        // Check port count compatibility
        if (!ports1.isEmpty() && !ports2.isEmpty()) {
            if (!isCompatiblePorts(ports1, ports2)) {
                return false;
            }
        }

        return true;
    }

    private String extractWiFiCapability(String mpn) {
        if (mpn.contains("AX")) return "WiFi-6";
        if (mpn.contains("AC")) return "WiFi-5";
        if (mpn.contains("N")) return "WiFi-4";
        return "";
    }

    private String extractBluetoothVersion(String mpn) {
        if (mpn.contains("BT5.2")) return "5.2";
        if (mpn.contains("BT5.1")) return "5.1";
        if (mpn.contains("BT5.0")) return "5.0";
        if (mpn.contains("BT4.2")) return "4.2";
        return "";
    }

    private String extractNetworkSpeed(String mpn) {
        if (mpn.contains("100G")) return "100G";
        if (mpn.contains("40G")) return "40G";
        if (mpn.contains("10G")) return "10G";
        if (mpn.contains("1G")) return "1G";
        return "";
    }

    private String extractPortCount(String mpn) {
        // Extract port count from part number
        StringBuilder ports = new StringBuilder();
        boolean foundP = false;
        for (char c : mpn.toCharArray()) {
            if (c == 'P') {
                foundP = true;
            } else if (foundP && Character.isDigit(c)) {
                ports.append(c);
            } else if (foundP && !ports.isEmpty()) {
                break;
            }
        }
        return ports.toString();
    }

    private String extractStorageInterface(String mpn) {
        if (mpn.contains("SAS")) return "SAS";
        if (mpn.contains("SATA")) return "SATA";
        if (mpn.contains("NVME")) return "NVMe";
        if (mpn.contains("PCIE")) return "PCIe";
        return "";
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

    private boolean isCompatibleSpeed(String speed1, String speed2) {
        // Convert to Gbps for comparison
        int s1 = parseSpeed(speed1);
        int s2 = parseSpeed(speed2);
        // Higher speed can typically handle lower speed requirements
        return s1 >= s2;
    }

    private int parseSpeed(String speed) {
        try {
            return Integer.parseInt(speed.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isCompatiblePorts(String ports1, String ports2) {
        try {
            int p1 = Integer.parseInt(ports1);
            int p2 = Integer.parseInt(ports2);
            // More ports can typically handle requirements for fewer ports
            return p1 >= p2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}