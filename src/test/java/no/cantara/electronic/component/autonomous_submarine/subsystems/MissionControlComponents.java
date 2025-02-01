package no.cantara.electronic.component.autonomous_submarine.subsystems;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;

public class MissionControlComponents {
    public static class PCBAComponents {
        public static PCBABOM createMainControlBoard() {
            PCBABOM bom = new PCBABOM();
            addMainProcessor(bom);
            addSafetyProcessor(bom);
            addRealTimeProcessor(bom);
            addMemorySystems(bom);
            addStorage(bom);
            addWatchdog(bom);
            return bom;
        }

        private static void addMainProcessor(PCBABOM bom) {
            BOMEntry processor = new BOMEntry()
                    .setMpn("i.MX8QuadXPlus")
                    .setManufacturer("NXP")
                    .setDescription("Main Control Processor")
                    .setPkg("FCBGA-236")
                    .addSpec("core", "Cortex-A35")
                    .addSpec("features", "ECC Memory, Lockstep")
                    .addSpec("security", "TrustZone")
                    .addSpec("speed", "1200MHz")  // High performance >1GHz
                    .addSpec("temp_range", "-40°C to +85°C");
            bom.getBomEntries().add(processor);
        }

        private static void addStorage(PCBABOM bom) {
            BOMEntry storage = new BOMEntry()
                    .setMpn("SSDPE21K375GA01")
                    .setManufacturer("Intel")
                    .setDescription("Storage")
                    .setPkg("M.2")
                    .addSpec("capacity", "375GB")
                    .addSpec("type", "NVMe SSD")
                    .addSpec("features", "Power Loss Protection")
                    .addSpec("temp_range", "-40°C to +85°C");
            bom.getBomEntries().add(storage);
        }

        private static void addMemorySystems(PCBABOM bom) {
            BOMEntry systemRam = new BOMEntry()
                    .setMpn("MT41K512M16HA-125")
                    .setManufacturer("Micron")
                    .setDescription("Memory")
                    .setPkg("BGA-96")
                    .addSpec("capacity", "8GB")
                    .addSpec("features", "ECC")  // Required ECC feature
                    .addSpec("temp_range", "-40°C to +85°C");
            bom.getBomEntries().add(systemRam);
        }

        private static void addRealTimeProcessor(PCBABOM bom) {
            BOMEntry rtProcessor = new BOMEntry()
                    .setMpn("MPC5744P")
                    .setManufacturer("NXP")
                    .setDescription("Real-Time Co-Processor")
                    .setPkg("LQFP-144")
                    .addSpec("features", "Real-Time OS, AUTOSAR")
                    .addSpec("quality", "Automotive")
                    .addSpec("temp_range", "-40°C to +125°C");
            bom.getBomEntries().add(rtProcessor);
        }

        private static void addSafetyProcessor(PCBABOM bom) {
            BOMEntry safetyProcessor = new BOMEntry()
                    .setMpn("TMS570LC4357BZWTQQ1")
                    .setManufacturer("Texas Instruments")
                    .setDescription("Safety Co-Processor")
                    .setPkg("NFBGA-337")
                    .addSpec("core", "Dual ARM Cortex-R5F")
                    .addSpec("features", "Lock-step")
                    .addSpec("certification", "SIL 3")
                    .addSpec("temp_range", "-40°C to +125°C");
            bom.getBomEntries().add(safetyProcessor);
        }

        private static void addWatchdog(PCBABOM bom) {
            BOMEntry watchdog = new BOMEntry()
                    .setMpn("MAX6369KA+T")
                    .setManufacturer("Maxim")
                    .setDescription("Watchdog")  // Contains "Watchdog"
                    .setPkg("SOT-23")
                    .addSpec("timeout", "60s")
                    .addSpec("features", "Manual Reset, WDI Input")
                    .addSpec("temp_range", "-40°C to +85°C");
            bom.getBomEntries().add(watchdog);
        }
    }

    public static class MechanicalComponents {
        public static MechanicalBOM createHousing() {
            MechanicalBOM bom = new MechanicalBOM();
            addHousing(bom);
            addThermalManagement(bom);
            return bom;
        }

        private static void addHousing(MechanicalBOM bom) {
            BOMEntry housing = new BOMEntry()
                    .setMpn("MCS-800-HSG")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Mission Control Housing")
                    .setPkg("Assembly")
                    .addSpec("material", "Grade 5 Titanium")
                    .addSpec("pressure_rating", "300m depth")
                    .addSpec("heat_dissipation", "Active")
                    .addSpec("thermal_management", "Liquid Cooling")
                    .addSpec("sealing", "IP68")  // Required waterproofing
                    .addSpec("temp_range", "-40°C to +85°C");
            bom.getBomEntries().add(housing);
        }

        private static void addThermalManagement(MechanicalBOM bom) {
            BOMEntry thermalPlate = new BOMEntry()
                    .setMpn("MCS-800-THM")
                    .setManufacturer("DeepSea Manufacturing")
                    .setDescription("Thermal Management System")
                    .setPkg("Assembly")
                    .addSpec("conductivity", "380 W/m·K")
                    .addSpec("cooling", "Liquid-Cooled")
                    .addSpec("capacity", "500W")
                    .addSpec("sealing", "IP68")  // Required waterproofing
                    .addSpec("temp_range", "-40°C to +85°C");
            bom.getBomEntries().add(thermalPlate);
        }
    }
}