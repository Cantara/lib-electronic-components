package no.cantara.electronic.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;

class BOMTest {

    // Helper methods
    private boolean hasComponentWithMpn(BOM bom, String targetMpn) {
        if (targetMpn == null) return false;

        boolean found = bom.getBomEntries().stream()
                .filter(Objects::nonNull)
                .map(BOMEntry::getMpn)
                .filter(Objects::nonNull)
                .anyMatch(mpn -> {
                    boolean matches = targetMpn.equals(mpn);
                    if (!matches) {
                        System.out.println("MPN comparison: " + targetMpn + " vs " + mpn);
                    }
                    return matches;
                });

        if (!found) {
            System.out.println("Failed to find MPN: " + targetMpn);
        }
        return found;
    }

    private boolean hasComponentWithDescription(BOM bom, String descriptionPart) {
        if (descriptionPart == null) return false;
        return bom.getBomEntries().stream()
                .filter(Objects::nonNull)
                .map(BOMEntry::getDescription)
                .filter(Objects::nonNull)
                .anyMatch(desc -> desc.contains(descriptionPart));
    }

    private List<BOMEntry> findComponentsByType(BOM bom, String type) {
        if (type == null) return Collections.emptyList();
        return bom.getBomEntries().stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getDescription() != null &&
                        entry.getDescription().contains(type))
                .collect(Collectors.toList());
    }

    private boolean hasInductorWithCurrentRating(BOM bom, double minCurrent) {
        return bom.getBomEntries().stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getDescription() != null &&
                        entry.getDescription().contains("Power Inductor"))
                .filter(entry -> entry.getSpecs() != null)
                .anyMatch(entry -> {
                    String currentStr = entry.getSpecs().get("Current").toString();
                    if (currentStr == null) return false;
                    try {
                        double current = Double.parseDouble(
                                currentStr.replaceAll("[^0-9.]", ""));
                        return current >= minCurrent;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
    }

    private boolean hasCrystalWithFrequency(BOM bom, String frequency) {
        if (frequency == null) return false;
        return bom.getBomEntries().stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getDescription() != null &&
                        entry.getDescription().contains("Crystal"))
                .filter(entry -> entry.getSpecs() != null)
                .anyMatch(entry -> frequency.equals(entry.getSpecs().get("Frequency")));
    }

    private void verifyComponent(BOM bom, String mpn, String componentType, String expectedDesc) {
        boolean found = hasComponentWithMpn(bom, mpn);
        if (!found) {
            System.out.println("\nFailed to find " + componentType + ":");
            System.out.println("Looking for MPN: " + mpn);
            System.out.println("Expected description: " + expectedDesc);
            System.out.println("Actual BOM entries:");
            bom.getBomEntries().forEach(entry ->
                    System.out.println(String.format("- MPN: %s, Description: %s",
                            entry.getMpn(), entry.getDescription())));
        }
        assertTrue(found, componentType + " should be present");
    }

    private void addComponentsToBOM(BOM bom, BOMEntry... entries) {
        for (BOMEntry entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Null BOM entry detected");
            }
            if (entry.getMpn() == null || entry.getMpn().trim().isEmpty()) {
                throw new IllegalArgumentException("BOM entry without MPN detected");
            }
            if (entry.getDescription() == null || entry.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("BOM entry without description detected");
            }
        }
        bom.getBomEntries().addAll(Arrays.asList(entries));
    }

    @Test
    void shouldCreateTemperatureSensorModuleBOM() {
        // Create a BOM for a basic temperature sensor module
        BOM bom = new BOM(
                "TSM-2024-001",           // Production number
                "Environmental Systems",   // Customer name
                "ORD-2024-Q1-123",        // Order number
                new ArrayList<>()          // We'll add entries
        );
        bom.setBomType(BOMType.PCBA);

        // 1. Microcontroller
        BOMEntry mcuEntry = new BOMEntry()
                .setMpn("ATTINY84A-SSU")
                .setManufacturer("Microchip")
                .setDescription("MCU 8-bit ATtiny AVR RISC 8KB Flash")
                .setPkg("SOIC-14")
                .setQty("1")
                .addSpec("Core", "AVR")
                .addSpec("Flash", "8KB")
                .addSpec("Voltage", "1.8-5.5V");
        mcuEntry.getDesignators().add("U1");

        // 2. Temperature Sensor
        BOMEntry sensorEntry = new BOMEntry()
                .setMpn("SHT31-DIS-B")
                .setManufacturer("Sensirion")
                .setDescription("Temperature/Humidity Sensor Digital")
                .setPkg("DFN-8")
                .setQty("1")
                .addSpec("Interface", "I2C")
                .addSpec("Accuracy", "±0.2°C")
                .addSpec("Supply", "2.4-5.5V");
        sensorEntry.getDesignators().add("U2");

        // 3. Voltage Regulator
        BOMEntry regulatorEntry = new BOMEntry()
                .setMpn("AP2112K-3.3TRG1")
                .setManufacturer("Diodes Inc.")
                .setDescription("LDO Voltage Regulator 3.3V")
                .setPkg("SOT-23-5")
                .setQty("1")
                .addSpec("Output", "3.3V")
                .addSpec("Current", "600mA");
        regulatorEntry.getDesignators().add("U3");

        // 4. Bypass Capacitors
        BOMEntry bypassCapsEntry = new BOMEntry()
                .setMpn("CL10B104KB8NNNC")
                .setManufacturer("Samsung")
                .setDescription("Ceramic Capacitor 100nF 50V X7R")
                .setPkg("0603")
                .setQty("3")
                .addSpec("Value", "100nF")
                .addSpec("Voltage", "50V")
                .addSpec("Dielectric", "X7R");
        bypassCapsEntry.getDesignators().addAll(List.of("C1", "C2", "C3"));

        // 5. I2C Pull-up Resistors
        BOMEntry pullupResistorsEntry = new BOMEntry()
                .setMpn("RC0603FR-074K7L")
                .setManufacturer("Yageo")
                .setDescription("Chip Resistor 4.7kΩ 1%")
                .setPkg("0603")
                .setQty("2")
                .addSpec("Value", "4.7kΩ")
                .addSpec("Tolerance", "1%")
                .addSpec("Power", "0.1W");
        pullupResistorsEntry.getDesignators().addAll(List.of("R1", "R2"));

        // Add all entries to BOM with verification
        addComponentsToBOM(bom,
                mcuEntry, sensorEntry, regulatorEntry,
                bypassCapsEntry, pullupResistorsEntry
        );

        // Debug output
        System.out.println("\nTemperature Sensor BOM Entries:");
        bom.getBomEntries().forEach(entry ->
                System.out.println(String.format("MPN: %s, Description: %s",
                        entry.getMpn(), entry.getDescription())));

        // Verify BOM structure
        assertEquals("TSM-2024-001", bom.getProductionNo());
        assertEquals(BOMType.PCBA, bom.getBomType());
        assertEquals(5, bom.getBomEntries().size());

        // Verify components with debug output
        verifyComponent(bom, "SHT31-DIS-B", "Temperature sensor",
                sensorEntry.getDescription());

        // Verify I2C pull-up resistors
        List<BOMEntry> pullupResistors = findComponentsByType(bom, "Resistor");
        assertFalse(pullupResistors.isEmpty(), "Should have pull-up resistors");

        pullupResistors.forEach(resistor -> {
            String value = resistor.getSpecs().get("Value");
            assertNotNull(value, "Resistor should have a value specification");
            assertTrue(value.contains("4.7k"),
                    "I2C pull-up resistors should be 4.7kΩ");
        });

        // Verify bypass capacitors
        long bypassCapCount = bom.getBomEntries().stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getDescription() != null &&
                        entry.getDescription().contains("100nF"))
                .mapToInt(entry -> {
                    try {
                        return Integer.parseInt(entry.getQty());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .sum();
        assertEquals(3, bypassCapCount, "Should have 3 bypass capacitors");
    }

    @Test
    void shouldCreateBatteryPoweredIoTDeviceBOM() {
        // Create a BOM for an IoT environmental monitor
        BOM bom = new BOM(
                "IOT-2024-001",           // Production number
                "Smart Solutions Inc",     // Customer name
                "ORD-2024-Q1-456",        // Order number
                new ArrayList<>()          // We'll add entries
        );
        bom.setBomType(BOMType.PCBA);

        // 1. Main MCU with integrated BLE
        BOMEntry mcuEntry = new BOMEntry()
                .setMpn("nRF52832-QFAA-R")
                .setManufacturer("Nordic Semiconductor")
                .setDescription("BLE MCU ARM Cortex-M4 64MHz 512KB Flash")
                .setPkg("QFN-48")
                .setQty("1")
                .addSpec("Core", "ARM Cortex-M4")
                .addSpec("Flash", "512KB")
                .addSpec("RAM", "64KB")
                .addSpec("Frequency", "64MHz");
        mcuEntry.getDesignators().add("U1");

        // 2. Battery Management IC
        BOMEntry batteryMgmtEntry = new BOMEntry()
                .setMpn("BQ24075RGTR")
                .setManufacturer("Texas Instruments")
                .setDescription("LiPo Battery Charger with Power Path")
                .setPkg("QFN-16")
                .setQty("1")
                .addSpec("Charge Current", "1.5A")
                .addSpec("Input Voltage", "4.2-10V");
        batteryMgmtEntry.getDesignators().add("U2");

        // 3. Buck-Boost Converter
        BOMEntry dcdcEntry = new BOMEntry()
                .setMpn("TPS63020DSJR")
                .setManufacturer("Texas Instruments")
                .setDescription("Buck-Boost Converter 3.3V 2A")
                .setPkg("SON-12")
                .setQty("1")
                .addSpec("Output", "3.3V")
                .addSpec("Current", "2A")
                .addSpec("Efficiency", "96%");
        dcdcEntry.getDesignators().add("U3");

        // 4. Power Inductor
        BOMEntry inductorEntry = new BOMEntry()
                .setMpn("744043002")
                .setManufacturer("Würth Elektronik")
                .setDescription("Power Inductor 2.2µH 2.8A")
                .setPkg("3.0x3.0mm")
                .setQty("1")
                .addSpec("Value", "2.2µH")
                .addSpec("Current", "2.8A")
                .addSpec("DCR", "59mΩ");
        inductorEntry.getDesignators().add("L1");

        // 5. Battery Protection IC
        BOMEntry protectionEntry = new BOMEntry()
                .setMpn("DW01A-G")
                .setManufacturer("Fortune Semiconductor")
                .setDescription("Battery Protection IC")
                .setPkg("SOT-23-6")
                .setQty("1")
                .addSpec("Overcurrent", "3A")
                .addSpec("Overdischarge", "2.4V");
        protectionEntry.getDesignators().add("U5");

        // 6. USB-C Connector
        BOMEntry usbConnEntry = new BOMEntry()
                .setMpn("USB4085-GF-A")
                .setManufacturer("GCT")
                .setDescription("USB Type-C Receptacle")
                .setPkg("SMT")
                .setQty("1")
                .addSpec("Current", "5A");
        usbConnEntry.getDesignators().add("J1");

        // Add all entries to BOM with verification
        addComponentsToBOM(bom,
                mcuEntry, batteryMgmtEntry, dcdcEntry,
                inductorEntry, protectionEntry, usbConnEntry
        );

        // Debug output
        System.out.println("\nBattery-Powered IoT Device BOM Entries:");
        bom.getBomEntries().forEach(entry ->
                System.out.println(String.format("MPN: %s, Description: %s",
                        entry.getMpn(), entry.getDescription())));

        // Verify BOM structure
        assertEquals("IOT-2024-001", bom.getProductionNo());
        assertEquals(BOMType.PCBA, bom.getBomType());
        assertEquals(6, bom.getBomEntries().size());

        // Enhanced component verification with debug output
        verifyComponent(bom, "BQ24075RGTR", "Battery management IC",
                batteryMgmtEntry.getDescription());
        verifyComponent(bom, "TPS63020DSJR", "Buck-Boost converter",
                dcdcEntry.getDescription());
        verifyComponent(bom, "DW01A-G", "Battery protection",
                protectionEntry.getDescription());
        verifyComponent(bom, "USB4085-GF-A", "USB-C connector",
                usbConnEntry.getDescription());

        // Verify power inductor
        assertTrue(hasInductorWithCurrentRating(bom, 2.8),
                "Power inductor should have sufficient current rating");
    }
}