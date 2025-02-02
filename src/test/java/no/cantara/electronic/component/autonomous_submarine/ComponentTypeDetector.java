package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.BOMEntry;

/**
 * Utility class for detecting component types in the submarine system.
 * This class analyzes BOMEntry descriptions and MPNs to determine their functional category.
 */
public class ComponentTypeDetector {

    public boolean isProcessorComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("processor") || desc.contains("mcu") || desc.contains("cpu") ||
                mpn.contains("stm32") || mpn.contains("tms") || mpn.contains("imx8") ||
                desc.contains("controller") || desc.contains("microcontroller") ||
                desc.contains("gate driver") || desc.contains("phy") || mpn.contains("dp83") ||
                mpn.contains("drv");
    }

    public boolean isPowerComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("power") || desc.contains("battery") || desc.contains("voltage") ||
                desc.contains("converter") || desc.contains("regulator") || mpn.contains("tps") ||
                mpn.contains("ltc") || mpn.contains("bq") || desc.contains("charger") ||
                mpn.contains("pwr-") || mpn.contains("batt") || desc.contains("dc/dc") ||
                desc.contains("power supply") || desc.contains("distribution");
    }

    public boolean isSensorComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("sensor") || desc.contains("detector") || mpn.startsWith("wq-") ||
                mpn.startsWith("water-") || desc.contains("temperature") || desc.contains("monitor") ||
                desc.contains("transducer") || desc.contains("sonar");
    }

    public boolean isMemoryComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("memory") || desc.contains("ram") || desc.contains("flash") ||
                desc.contains("storage") || mpn.contains("sram") || mpn.contains("dram") ||
                mpn.contains("fram") || mpn.contains("mt41") || mpn.contains("is42");
    }

    public boolean isConnectorComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("connector") || mpn.contains("con-") || mpn.endsWith("-con") ||
                desc.contains("socket") || desc.contains("plug") || desc.contains("receptacle");
    }

    public boolean isMechanicalComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        return desc.contains("mount") || desc.contains("seal") || desc.contains("bearing") ||
                desc.contains("shaft") || desc.contains("plate") || desc.contains("thermal interface") ||
                desc.contains("housing") || desc.contains("bracket") || desc.contains("support");
    }

    public boolean isRFComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("rf") || desc.contains("radio") || desc.contains("transceiver") ||
                mpn.contains("cc1200") || desc.contains("antenna");
    }

    public boolean isCoolingComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("cooling") || desc.contains("thermal") || desc.contains("heat") ||
                mpn.contains("cool") || mpn.contains("thp") || desc.contains("temperature");
    }

    public boolean isGateDriverComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("gate driver") || desc.contains("gate drv") ||
                mpn.contains("drv") || mpn.contains("ir21") || mpn.contains("irs2");
    }

    public boolean isPHYComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("phy") || desc.contains("ethernet phy") ||
                mpn.contains("dp83") || mpn.contains("ksz") || mpn.contains("lan87");
    }

    public boolean isTransducerComponent(BOMEntry entry) {
        String desc = entry.getDescription().toLowerCase();
        String mpn = entry.getMpn() != null ? entry.getMpn().toLowerCase() : "";
        return desc.contains("transducer") || desc.contains("acoustic") ||
                mpn.startsWith("at") || desc.contains("sonar");
    }
}