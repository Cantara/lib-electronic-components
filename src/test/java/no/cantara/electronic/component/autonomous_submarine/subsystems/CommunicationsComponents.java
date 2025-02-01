package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;

/**
 * Communications Components
 * Responsible for acoustic underwater communication, RF surface communication,
 * internal networking, and emergency beacons
 */
public class CommunicationsComponents {

    public static class PCBAComponents {
        public static PCBABOM createMainCommunicationsBoard() {
            PCBABOM bom = new PCBABOM();
            addMainProcessor(bom);
            addAcousticModem(bom);
            addRFComponents(bom);
            addNetworkComponents(bom);
            addEmergencyBeacon(bom);
            addPowerManagement(bom);
            return bom;
        }

        private static void addMainProcessor(PCBABOM bom) {
            BOMEntry processor = new BOMEntry()
                    .setMpn("STM32H735IGK6")
                    .setManufacturer("STMicroelectronics")
                    .setDescription("Communications Control MCU")
                    .setPkg("UFBGA-176")
                    .addSpec("core", "Cortex-M7")
                    .addSpec("speed", "550MHz")
                    .addSpec("flash", "1MB")
                    .addSpec("ram", "564KB")
                    .addSpec("features", "Crypto, USB")
                    .addSpec("interfaces", "Ethernet, CAN, SPI, UART")
                    .addSpec("quality", "Industrial")
                    .addSpec("temp_range", "-40°C to +85°C");  // Added temperature range for industrial grade MCU
            bom.getBomEntries().add(processor);
        }

        private static void addRFComponents(PCBABOM bom) {
            // RF Transceiver for surface communications
            BOMEntry rfTransceiver = new BOMEntry()
                    .setMpn("CC1200RHBR")
                    .setManufacturer("Texas Instruments")
                    .setDescription("High-Performance RF Transceiver")
                    .setPkg("QFN-32")
                    .addSpec("frequency", "169MHz")
                    .addSpec("modulation", "FSK, OOK, MSK")
                    .addSpec("sensitivity", "-123dBm")
                    .addSpec("output_power", "max +27dBm")
                    .addSpec("features", "Forward Error Correction")
                    .addSpec("temp_range", "-40°C to +85°C");  // Added temperature range for industrial RF component
            bom.getBomEntries().add(rfTransceiver);
        }

        private static void addEnclosure(MechanicalBOM bom) {
            BOMEntry housing = new BOMEntry()
                    .setMpn("COM-600-HSG")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Communications Housing")
                    .setPkg("Assembly")
                    .addSpec("material", "Grade 5 Titanium")  // Already correct
                    .addSpec("pressure_rating", "300m depth")
                    .addSpec("rf_windows", "Composite RF-transparent")
                    .addSpec("acoustic_window", "PEEK")
                    .addSpec("sealing", "Double O-ring")
                    .addSpec("mount_type", "DIN Rail")
                    .addSpec("mount", "Quick-Release")
                    .addSpec("mount_standard", "EN 60715")
                    .addSpec("temp_range", "-40°C to +60°C");
            bom.getBomEntries().add(housing);
        }


        private static void addAcousticModem(PCBABOM bom) {
            // DSP for acoustic processing
            BOMEntry dspProcessor = new BOMEntry()
                    .setMpn("TMS320C5535AZCHA")
                    .setManufacturer("Texas Instruments")
                    .setDescription("Digital Signal Processor")
                    .setPkg("BGA-100")
                    .addSpec("architecture", "C55x")
                    .addSpec("speed", "100MHz")
                    .addSpec("memory", "320KB RAM")
                    .addSpec("features", "FFT Accelerator")
                    .addSpec("power", "Low Power");
            bom.getBomEntries().add(dspProcessor);

            // Power amplifier for acoustic transmission
            BOMEntry powerAmp = new BOMEntry()
                    .setMpn("PD55003")
                    .setManufacturer("NXP")
                    .setDescription("RF Power Amplifier")
                    .setPkg("SOT-539")
                    .addSpec("frequency", "1-600MHz")
                    .addSpec("gain", "28.5dB")
                    .addSpec("power", "3W")
                    .addSpec("voltage", "12V");
            bom.getBomEntries().add(powerAmp);

            // ADC for acoustic reception
            BOMEntry adc = new BOMEntry()
                    .setMpn("ADS131E08IPAG")
                    .setManufacturer("Texas Instruments")
                    .setDescription("24-Bit ADC")
                    .setPkg("TQFP-64")
                    .addSpec("channels", "8")
                    .addSpec("sample_rate", "64kSPS")
                    .addSpec("resolution", "24-bit")
                    .addSpec("features", "Simultaneous Sampling");
            bom.getBomEntries().add(adc);
        }



        private static void addNetworkComponents(PCBABOM bom) {
            BOMEntry ethernetPhy = new BOMEntry()
                    .setMpn("DP83825IRHBR")
                    .setManufacturer("Texas Instruments")
                    .setDescription("Industrial Ethernet PHY")
                    .setPkg("QFN-32")
                    .addSpec("interface", "RMII")
                    .addSpec("speed", "10/100 Mbps")
                    .addSpec("features", "Auto-MDIX")
                    .addSpec("quality", "Industrial");
            bom.getBomEntries().add(ethernetPhy);

            BOMEntry switch_ic = new BOMEntry()
                    .setMpn("KSZ8895MQXCA")
                    .setManufacturer("Microchip")
                    .setDescription("5-Port Ethernet Switch")
                    .setPkg("QFN-128")
                    .addSpec("ports", "5")
                    .addSpec("speed", "10/100 Mbps")
                    .addSpec("features", "QoS, VLAN")
                    .addSpec("management", "SMI/MDIO");
            bom.getBomEntries().add(switch_ic);
        }

        private static void addEmergencyBeacon(PCBABOM bom) {
            BOMEntry beaconProcessor = new BOMEntry()
                    .setMpn("STM32L412K8U6")
                    .setManufacturer("STMicroelectronics")
                    .setDescription("Ultra-Low-Power MCU")
                    .setPkg("UFQFPN-32")
                    .addSpec("core", "Cortex-M4")
                    .addSpec("flash", "64KB")
                    .addSpec("features", "RTC, Low Power")
                    .addSpec("current", "100nA Standby");
            bom.getBomEntries().add(beaconProcessor);

            BOMEntry gps = new BOMEntry()
                    .setMpn("NEO-M9N")
                    .setManufacturer("u-blox")
                    .setDescription("GNSS Module")
                    .setPkg("LGA-39")
                    .addSpec("systems", "GPS, GLONASS, Galileo")
                    .addSpec("sensitivity", "-167dBm")
                    .addSpec("accuracy", "1.5m CEP")
                    .addSpec("features", "Low Power");
            bom.getBomEntries().add(gps);
        }

        private static void addPowerManagement(PCBABOM bom) {
            BOMEntry powerController = new BOMEntry()
                    .setMpn("TPS65281")
                    .setManufacturer("Texas Instruments")
                    .setDescription("Power Management IC")
                    .setPkg("HTQFP-80")
                    .addSpec("inputs", "4.5-18V")
                    .addSpec("outputs", "Multiple")
                    .addSpec("features", "Sequencing, Monitoring")
                    .addSpec("efficiency", "95%");
            bom.getBomEntries().add(powerController);
        }
    }

    public static class MechanicalComponents {
        public static MechanicalBOM createHousing() {
            MechanicalBOM bom = new MechanicalBOM();
            addEnclosure(bom);
            addTransducerAssembly(bom);
            addAntennaAssembly(bom);
            addMounting(bom);
            addConnectors(bom);
            return bom;
        }

        private static void addEnclosure(MechanicalBOM bom) {
            BOMEntry housing = new BOMEntry()
                    .setMpn("COM-600-HSG")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Communications Housing")
                    .setPkg("Assembly")
                    .addSpec("material", "Grade 5 Titanium")
                    .addSpec("pressure_rating", "300m depth")
                    .addSpec("rf_windows", "Composite RF-transparent")
                    .addSpec("acoustic_window", "PEEK")
                    .addSpec("sealing", "Double O-ring")
                    .addSpec("mount_type", "DIN Rail")
                    .addSpec("mount", "Quick-Release")
                    .addSpec("mount_standard", "EN 60715");
            bom.getBomEntries().add(housing);
        }

        private static void addTransducerAssembly(MechanicalBOM bom) {
            BOMEntry transducer = new BOMEntry()
                    .setMpn("AT12ET")
                    .setManufacturer("Teledyne")
                    .setDescription("Acoustic Transducer")
                    .setPkg("Assembly")
                    .addSpec("frequency", "20-50kHz")
                    .addSpec("beam_width", "120°")
                    .addSpec("sensitivity", "-190dB re 1V/μPa")
                    .addSpec("depth_rating", "300m")
                    .addSpec("material", "Composite")  // Changed to use approved material
                    .addSpec("temp_range", "-20°C to +50°C");
            bom.getBomEntries().add(transducer);
        }

        private static void addAntennaAssembly(MechanicalBOM bom) {
            BOMEntry antenna = new BOMEntry()
                    .setMpn("COM-600-ANT")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Surface Communications Antenna")
                    .setPkg("Assembly")
                    .addSpec("frequency", "169MHz")
                    .addSpec("gain", "3dBi")
                    .addSpec("pattern", "Omnidirectional")
                    .addSpec("depth_rating", "300m")
                    .addSpec("material", "Composite")  // Changed to use approved material
                    .addSpec("temp_range", "-40°C to +70°C");
            bom.getBomEntries().add(antenna);
        }

        private static void addMounting(MechanicalBOM bom) {
            BOMEntry mountKit = new BOMEntry()
                    .setMpn("COM-600-MNT")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Communications Mounting Kit")
                    .setPkg("Assembly")
                    .addSpec("material", "316L Stainless Steel")  // Changed to use approved material
                    .addSpec("type", "DIN Rail Mount")
                    .addSpec("load_rating", "20kg")
                    .addSpec("shock_isolation", "Vibration Dampening")
                    .addSpec("standard", "EN 60715");
            bom.getBomEntries().add(mountKit);
        }

        private static void addConnectors(MechanicalBOM bom) {
            BOMEntry dataConnector = new BOMEntry()
                    .setMpn("COM-600-CON-D")
                    .setManufacturer("SubConn")
                    .setDescription("Data Connector")
                    .setPkg("Assembly")
                    .addSpec("contacts", "16")
                    .addSpec("type", "Mixed Signal")
                    .addSpec("sealing", "IP68")
                    .addSpec("depth_rating", "300m")
                    .addSpec("material", "316L Stainless Steel")  // Added approved material
                    .addSpec("mating_cycles", "500");
            bom.getBomEntries().add(dataConnector);

            BOMEntry powerConnector = new BOMEntry()
                    .setMpn("COM-600-CON-P")
                    .setManufacturer("SubConn")
                    .setDescription("Power Connector")
                    .setPkg("Assembly")
                    .addSpec("contacts", "4")
                    .addSpec("current_rating", "10A")
                    .addSpec("voltage_rating", "300V")
                    .addSpec("sealing", "IP68")
                    .addSpec("depth_rating", "300m")
                    .addSpec("material", "316L Stainless Steel");  // Added approved material
            bom.getBomEntries().add(powerConnector);
        }

    }
}