package no.cantara.electronic.component;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BOMTest {

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
        BOMEntry mcuEntry = new BOMEntry();
        mcuEntry.setMpn("ATTINY84A-SSU");
        mcuEntry.setManufacturer("Microchip");
        mcuEntry.setDescription("MCU 8-bit ATtiny AVR RISC 8KB Flash");
        mcuEntry.setPkg("SOIC-14");
        mcuEntry.setQty("1");
        mcuEntry.getDesignators().add("U1");
        mcuEntry.getSpecs().put("Core", "AVR");
        mcuEntry.getSpecs().put("Flash", "8KB");
        mcuEntry.getSpecs().put("Voltage", "1.8-5.5V");

        // 2. Temperature Sensor
        BOMEntry sensorEntry = new BOMEntry();
        sensorEntry.setMpn("SHT31-DIS-B");
        sensorEntry.setManufacturer("Sensirion");
        sensorEntry.setDescription("Temperature/Humidity Sensor Digital");
        sensorEntry.setPkg("DFN-8");
        sensorEntry.setQty("1");
        sensorEntry.getDesignators().add("U2");
        sensorEntry.getSpecs().put("Interface", "I2C");
        sensorEntry.getSpecs().put("Accuracy", "±0.2°C");
        sensorEntry.getSpecs().put("Supply", "2.4-5.5V");

        // 3. Voltage Regulator
        BOMEntry regulatorEntry = new BOMEntry();
        regulatorEntry.setMpn("AP2112K-3.3TRG1");
        regulatorEntry.setManufacturer("Diodes Inc.");
        regulatorEntry.setDescription("LDO Voltage Regulator 3.3V");
        regulatorEntry.setPkg("SOT-23-5");
        regulatorEntry.setQty("1");
        regulatorEntry.getDesignators().add("U3");
        regulatorEntry.getSpecs().put("Output", "3.3V");
        regulatorEntry.getSpecs().put("Current", "600mA");

        // 4. Bypass Capacitors
        BOMEntry bypassCapsEntry = new BOMEntry();
        bypassCapsEntry.setMpn("CL10B104KB8NNNC");
        bypassCapsEntry.setManufacturer("Samsung");
        bypassCapsEntry.setDescription("Ceramic Capacitor 100nF 50V X7R");
        bypassCapsEntry.setPkg("0603");
        bypassCapsEntry.setQty("3");
        bypassCapsEntry.getDesignators().addAll(List.of("C1", "C2", "C3"));
        bypassCapsEntry.getSpecs().put("Value", "100nF");
        bypassCapsEntry.getSpecs().put("Voltage", "50V");
        bypassCapsEntry.getSpecs().put("Dielectric", "X7R");

        // 5. Bulk Capacitor
        BOMEntry bulkCapEntry = new BOMEntry();
        bulkCapEntry.setMpn("CL31A106KAHNNNE");
        bulkCapEntry.setManufacturer("Samsung");
        bulkCapEntry.setDescription("Ceramic Capacitor 10µF 25V X5R");
        bulkCapEntry.setPkg("1206");
        bulkCapEntry.setQty("1");
        bulkCapEntry.getDesignators().add("C4");
        bulkCapEntry.getSpecs().put("Value", "10µF");
        bulkCapEntry.getSpecs().put("Voltage", "25V");
        bulkCapEntry.getSpecs().put("Dielectric", "X5R");

        // 6. I2C Pull-up Resistors
        BOMEntry pullupResistorsEntry = new BOMEntry();
        pullupResistorsEntry.setMpn("RC0603FR-074K7L");
        pullupResistorsEntry.setManufacturer("Yageo");
        pullupResistorsEntry.setDescription("Chip Resistor 4.7kΩ 1%");
        pullupResistorsEntry.setPkg("0603");
        pullupResistorsEntry.setQty("2");
        pullupResistorsEntry.getDesignators().addAll(List.of("R1", "R2"));
        pullupResistorsEntry.getSpecs().put("Value", "4.7kΩ");
        pullupResistorsEntry.getSpecs().put("Tolerance", "1%");
        pullupResistorsEntry.getSpecs().put("Power", "0.1W");

        // 7. Power LED and Resistor
        BOMEntry ledEntry = new BOMEntry();
        ledEntry.setMpn("LTST-C191KGKT");
        ledEntry.setManufacturer("Lite-On");
        ledEntry.setDescription("LED Green 2V 20mA");
        ledEntry.setPkg("0603");
        ledEntry.setQty("1");
        ledEntry.getDesignators().add("D1");
        ledEntry.getSpecs().put("Color", "Green");
        ledEntry.getSpecs().put("Current", "20mA");

        BOMEntry ledResistorEntry = new BOMEntry();
        ledResistorEntry.setMpn("RC0603FR-07150RL");
        ledResistorEntry.setManufacturer("Yageo");
        ledResistorEntry.setDescription("Chip Resistor 150Ω 1%");
        ledResistorEntry.setPkg("0603");
        ledResistorEntry.setQty("1");
        ledResistorEntry.getDesignators().add("R3");
        ledResistorEntry.getSpecs().put("Value", "150Ω");
        ledResistorEntry.getSpecs().put("Tolerance", "1%");

        // 8. Programming Header
        BOMEntry headerEntry = new BOMEntry();
        headerEntry.setMpn("20021111-00006T4LF");
        headerEntry.setManufacturer("Amphenol");
        headerEntry.setDescription("Header 2x3 pins");
        headerEntry.setPkg("THT");
        headerEntry.setQty("1");
        headerEntry.getDesignators().add("J1");
        headerEntry.getSpecs().put("Pins", "6");
        headerEntry.getSpecs().put("Rows", "2");

        // Add all entries to BOM
        bom.getBomEntries().addAll(List.of(
                mcuEntry, sensorEntry, regulatorEntry, bypassCapsEntry,
                bulkCapEntry, pullupResistorsEntry, ledEntry,
                ledResistorEntry, headerEntry
        ));

        // Verify BOM structure
        assertEquals("TSM-2024-001", bom.getProductionNo());
        assertEquals(BOMType.PCBA, bom.getBomType());
        assertEquals(9, bom.getBomEntries().size());

        // Verify component relationships
        assertTrue(bom.getBomEntries().stream()
                        .anyMatch(entry -> entry.getMpn().equals("SHT31-DIS-B")),
                "Temperature sensor should be present");

        // Verify I2C pull-up resistors are present for the sensor
        assertTrue(bom.getBomEntries().stream()
                        .filter(entry -> entry.getDesignators().contains("R1") ||
                                entry.getDesignators().contains("R2"))
                        .allMatch(entry -> entry.getSpecs().get("Value").equals("4.7kΩ")),
                "I2C pull-up resistors should be 4.7kΩ");

        // Verify bypass capacitors
        long bypassCapCount = bom.getBomEntries().stream()
                .filter(entry -> entry.getDescription().contains("100nF"))
                .mapToInt(entry -> Integer.parseInt(entry.getQty()))
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
        BOMEntry mcuEntry = new BOMEntry();
        mcuEntry.setMpn("nRF52832-QFAA-R");
        mcuEntry.setManufacturer("Nordic Semiconductor");
        mcuEntry.setDescription("BLE MCU ARM Cortex-M4 64MHz 512KB Flash");
        mcuEntry.setPkg("QFN-48");
        mcuEntry.setQty("1");
        mcuEntry.getDesignators().add("U1");
        mcuEntry.getSpecs().put("Core", "ARM Cortex-M4");
        mcuEntry.getSpecs().put("Flash", "512KB");
        mcuEntry.getSpecs().put("RAM", "64KB");
        mcuEntry.getSpecs().put("Frequency", "64MHz");

        // 2. Battery Management IC
        BOMEntry batteryMgmtEntry = new BOMEntry();
        batteryMgmtEntry.setMpn("BQ24075RGTR");
        batteryMgmtEntry.setManufacturer("Texas Instruments");
        batteryMgmtEntry.setDescription("LiPo Battery Charger with Power Path");
        batteryMgmtEntry.setPkg("QFN-16");
        batteryMgmtEntry.setQty("1");
        batteryMgmtEntry.getDesignators().add("U2");
        batteryMgmtEntry.getSpecs().put("Charge Current", "1.5A");
        batteryMgmtEntry.getSpecs().put("Input Voltage", "4.2-10V");

        // 3. Buck-Boost Converter
        BOMEntry dcdcEntry = new BOMEntry();
        dcdcEntry.setMpn("TPS63020DSJR");
        dcdcEntry.setManufacturer("Texas Instruments");
        dcdcEntry.setDescription("Buck-Boost Converter 3.3V 2A");
        dcdcEntry.setPkg("SON-12");
        dcdcEntry.setQty("1");
        dcdcEntry.getDesignators().add("U3");
        dcdcEntry.getSpecs().put("Output", "3.3V");
        dcdcEntry.getSpecs().put("Current", "2A");
        dcdcEntry.getSpecs().put("Efficiency", "96%");

        // 4. Environmental Sensor
        BOMEntry envSensorEntry = new BOMEntry();
        envSensorEntry.setMpn("BME280");
        envSensorEntry.setManufacturer("Bosch");
        envSensorEntry.setDescription("Environmental Sensor (T, H, P)");
        envSensorEntry.setPkg("LGA-8");
        envSensorEntry.setQty("1");
        envSensorEntry.getDesignators().add("U4");
        envSensorEntry.getSpecs().put("Interface", "I2C/SPI");
        envSensorEntry.getSpecs().put("Supply", "1.8-3.6V");

        // 5. Power Inductors
        BOMEntry inductorEntry = new BOMEntry();
        inductorEntry.setMpn("744043002");
        inductorEntry.setManufacturer("Würth Elektronik");
        inductorEntry.setDescription("Power Inductor 2.2µH 2.8A");
        inductorEntry.setPkg("3.0x3.0mm");
        inductorEntry.setQty("1");
        inductorEntry.getDesignators().add("L1");
        inductorEntry.getSpecs().put("Value", "2.2µH");
        inductorEntry.getSpecs().put("Current", "2.8A");
        inductorEntry.getSpecs().put("DCR", "59mΩ");

        // 6. Power Path Capacitors
        BOMEntry powerCapsEntry = new BOMEntry();
        powerCapsEntry.setMpn("GRM188R61C106KAALD");
        powerCapsEntry.setManufacturer("Murata");
        powerCapsEntry.setDescription("Ceramic Capacitor 10µF 16V X5R");
        powerCapsEntry.setPkg("0603");
        powerCapsEntry.setQty("4");
        powerCapsEntry.getDesignators().addAll(List.of("C1", "C2", "C3", "C4"));
        powerCapsEntry.getSpecs().put("Value", "10µF");
        powerCapsEntry.getSpecs().put("Voltage", "16V");
        powerCapsEntry.getSpecs().put("Dielectric", "X5R");

        // 7. Battery Connector
        BOMEntry battConnEntry = new BOMEntry();
        battConnEntry.setMpn("S2B-PH-K-S(LF)(SN)");
        battConnEntry.setManufacturer("JST");
        battConnEntry.setDescription("Battery Connector 2-Pin");
        battConnEntry.setPkg("THT");
        battConnEntry.setQty("1");
        battConnEntry.getDesignators().add("J1");
        battConnEntry.getSpecs().put("Pins", "2");
        battConnEntry.getSpecs().put("Pitch", "2mm");

        // 8. USB-C Connector
        BOMEntry usbConnEntry = new BOMEntry();
        usbConnEntry.setMpn("USB4085-GF-A");
        usbConnEntry.setManufacturer("GCT");
        usbConnEntry.setDescription("USB Type-C Receptacle");
        usbConnEntry.setPkg("SMT");
        usbConnEntry.setQty("1");
        usbConnEntry.getDesignators().add("J2");
        usbConnEntry.getSpecs().put("Type", "USB-C");
        usbConnEntry.getSpecs().put("Current", "5A");

        // 9. Battery Protection
        BOMEntry protectionEntry = new BOMEntry();
        protectionEntry.setMpn("DW01A-G");
        protectionEntry.setManufacturer("Fortune Semiconductor");
        protectionEntry.setDescription("Battery Protection IC");
        protectionEntry.setPkg("SOT-23-6");
        protectionEntry.setQty("1");
        protectionEntry.getDesignators().add("U5");
        protectionEntry.getSpecs().put("Overcurrent", "3A");
        protectionEntry.getSpecs().put("Overdischarge", "2.4V");

        // 10. 32.768kHz Crystal
        BOMEntry crystalEntry = new BOMEntry();
        crystalEntry.setMpn("ABS07-32.768KHZ-T");
        crystalEntry.setManufacturer("Abracon");
        crystalEntry.setDescription("32.768kHz Crystal");
        crystalEntry.setPkg("SMD-3215");
        crystalEntry.setQty("1");
        crystalEntry.getDesignators().add("Y1");
        crystalEntry.getSpecs().put("Frequency", "32.768kHz");
        crystalEntry.getSpecs().put("Load", "12.5pF");
        crystalEntry.getSpecs().put("Stability", "20ppm");

        // Add all entries to BOM
        bom.getBomEntries().addAll(List.of(
                mcuEntry, batteryMgmtEntry, dcdcEntry, envSensorEntry,
                inductorEntry, powerCapsEntry, battConnEntry, usbConnEntry,
                protectionEntry, crystalEntry
        ));

        // Verify BOM structure
        assertEquals("IOT-2024-001", bom.getProductionNo());
        assertEquals(BOMType.PCBA, bom.getBomType());
        assertEquals(10, bom.getBomEntries().size());

        // Verify power management components
        assertTrue(bom.getBomEntries().stream()
                        .anyMatch(entry -> entry.getMpn().equals("BQ24075RGTR")),
                "Battery management IC should be present");

        assertTrue(bom.getBomEntries().stream()
                        .anyMatch(entry -> entry.getMpn().equals("TPS63020DSJR")),
                "Buck-Boost converter should be present");

        // Verify battery protection
        assertTrue(bom.getBomEntries().stream()
                        .anyMatch(entry -> entry.getDescription().contains("Battery Protection")),
                "Battery protection IC should be present");

        // Verify connectors
        assertTrue(bom.getBomEntries().stream()
                        .anyMatch(entry -> entry.getDescription().contains("USB Type-C")),
                "USB-C connector should be present");

        assertTrue(bom.getBomEntries().stream()
                        .anyMatch(entry -> entry.getDescription().contains("Battery Connector")),
                "Battery connector should be present");

        // Verify critical power components
        assertTrue(bom.getBomEntries().stream()
                        .filter(entry -> entry.getDescription().contains("Power Inductor"))
                        .anyMatch(entry -> entry.getSpecs().get("Current").toString().contains("2.8A")),
                "Power inductor should have sufficient current rating");

        // Verify MCU crystal
        assertTrue(bom.getBomEntries().stream()
                        .filter(entry -> entry.getDescription().contains("Crystal"))
                        .anyMatch(entry -> entry.getSpecs().get("Frequency").equals("32.768kHz")),
                "32.768kHz crystal should be present for RTC");
    }


}