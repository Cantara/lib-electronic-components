package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;

/**
 * Navigation Control Components
 * Responsible for vessel orientation, position, and motion control
 */
public class NavigationControlComponents {

    public static class PCBAComponents {
        public static PCBABOM createMainControlBoard() {
            PCBABOM bom = new PCBABOM();
            addProcessors(bom);
            addSensors(bom);
            addMemory(bom);
            addPowerRegulation(bom);
            addInterfaces(bom);
            return bom;
        }

        private static void addProcessors(PCBABOM bom) {
            BOMEntry processor = new BOMEntry()
                    .setMpn("STM32H755ZIT6")
                    .setManufacturer("STMicroelectronics")
                    .setDescription("Dual-Core ARM Cortex-M7+M4 MCU")
                    .setPkg("LQFP144")
                    .addSpec("core", "Dual Cortex-M7/M4")
                    .addSpec("flash", "2MB")
                    .addSpec("ram", "1MB")
                    .addSpec("speed", "480MHz")
                    .addSpec("temp_range", "-40C to +85C")
                    .addSpec("quality", "Automotive Grade");
            bom.getBomEntries().add(processor);
        }

        private static void addSensors(PCBABOM bom) {
            BOMEntry imu = new BOMEntry()
                    .setMpn("BMX055")
                    .setManufacturer("Bosch")
                    .setDescription("9-axis IMU")
                    .setPkg("LGA-20")
                    .addSpec("sensors", "Accelerometer, Gyroscope, Magnetometer")
                    .addSpec("interface", "SPI/I2C")
                    .addSpec("accel_range", "±16g")
                    .addSpec("gyro_range", "±2000°/s")
                    .addSpec("mag_range", "±1300µT");
            bom.getBomEntries().add(imu);

            BOMEntry pressureSensor = new BOMEntry()
                    .setMpn("MS5837-30BA")
                    .setManufacturer("TE Connectivity")
                    .setDescription("High Resolution Pressure Sensor")
                    .setPkg("QFN-8")
                    .addSpec("pressure_range", "0-30 bar")
                    .addSpec("resolution", "0.2 mbar")
                    .addSpec("interface", "I2C")
                    .addSpec("temp_range", "-20C to +85C");
            bom.getBomEntries().add(pressureSensor);
        }

        private static void addMemory(PCBABOM bom) {
            BOMEntry fram = new BOMEntry()
                    .setMpn("FM24V10-G")
                    .setManufacturer("Infineon")
                    .setDescription("1Mb FRAM Non-volatile Memory")
                    .setPkg("SOIC-8")
                    .addSpec("size", "1Mb")
                    .addSpec("interface", "SPI")
                    .addSpec("endurance", "10^14 cycles")
                    .addSpec("retention", "151 years at 85°C");
            bom.getBomEntries().add(fram);

            BOMEntry flash = new BOMEntry()
                    .setMpn("W25Q128JVSIQ")
                    .setManufacturer("Winbond")
                    .setDescription("128Mb Serial Flash")
                    .setPkg("SOIC-8")
                    .addSpec("size", "128Mb")
                    .addSpec("interface", "SPI")
                    .addSpec("speed", "133MHz")
                    .addSpec("voltage", "1.8V");
            bom.getBomEntries().add(flash);
        }

        private static void addPowerRegulation(PCBABOM bom) {
            BOMEntry mainRegulator = new BOMEntry()
                    .setMpn("TPS62912")
                    .setManufacturer("Texas Instruments")
                    .setDescription("Buck Converter")
                    .setPkg("VQFN-14")
                    .addSpec("input", "4-12V")
                    .addSpec("output", "3.3V")
                    .addSpec("current", "2A")
                    .addSpec("efficiency", "95%");
            bom.getBomEntries().add(mainRegulator);
        }

        private static void addInterfaces(PCBABOM bom) {
            BOMEntry rs485 = new BOMEntry()
                    .setMpn("MAX3485EESA+")
                    .setManufacturer("Maxim")
                    .setDescription("RS-485 Transceiver")
                    .setPkg("SOIC-8")
                    .addSpec("speed", "12Mbps")
                    .addSpec("features", "Short-circuit Protection")
                    .addSpec("voltage", "3.3V");
            bom.getBomEntries().add(rs485);
        }
    }

    public static class MechanicalComponents {
        public static MechanicalBOM createHousing() {
            MechanicalBOM bom = new MechanicalBOM();
            addHousing(bom);
            addMounting(bom);
            addSealing(bom);
            addConnectors(bom);
            return bom;
        }

        private static void addHousing(MechanicalBOM bom) {
            BOMEntry housing = new BOMEntry()
                    .setMpn("NAV-300-HSG")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Navigation System Housing")
                    .setPkg("Assembly")
                    .addSpec("material", "316L Stainless Steel")
                    .addSpec("pressure_rating", "300m depth")
                    .addSpec("treatment", "Electropolished")
                    .addSpec("volume", "2.5L")
                    .addSpec("sealing", "Double O-ring")
                    .addSpec("mount_type", "DIN Rail")
                    .addSpec("mount", "Quick-Release")
                    .addSpec("mount_standard", "EN 60715");
            bom.getBomEntries().add(housing);
        }

        private static void addMounting(MechanicalBOM bom) {
            BOMEntry mountKit = new BOMEntry()
                    .setMpn("NAV-300-MNT")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Mounting Kit")
                    .setPkg("Assembly")
                    .addSpec("material", "316L Stainless Steel")
                    .addSpec("type", "DIN Rail Mount")
                    .addSpec("load_rating", "15kg")
                    .addSpec("shock_isolation", "Vibration Dampening");
            bom.getBomEntries().add(mountKit);
        }

        private static void addSealing(MechanicalBOM bom) {
            BOMEntry orings = new BOMEntry()
                    .setMpn("NAV-300-SEAL")
                    .setManufacturer("Parker")
                    .setDescription("O-Ring Set")
                    .setPkg("Kit")
                    .addSpec("material", "FKM (Viton)")
                    .addSpec("size", "Custom")
                    .addSpec("quantity", "2")
                    .addSpec("temp_range", "-15C to +200C");
            bom.getBomEntries().add(orings);
        }

        private static void addConnectors(MechanicalBOM bom) {
            BOMEntry connector = new BOMEntry()
                    .setMpn("NAV-300-CON")
                    .setManufacturer("SubConn")
                    .setDescription("Underwater Connector")
                    .setPkg("Assembly")
                    .addSpec("pins", "12")
                    .addSpec("rating", "IP68")
                    .addSpec("depth_rating", "300m")
                    .addSpec("mating_cycles", "500");
            bom.getBomEntries().add(connector);
        }
    }
}