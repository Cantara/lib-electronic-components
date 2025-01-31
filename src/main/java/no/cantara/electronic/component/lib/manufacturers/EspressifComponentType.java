package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;

/**
 * Espressif-specific component types
 */
public enum EspressifComponentType implements ManufacturerComponentType {
    ESP8266_SOC(false, true, ComponentType.MICROCONTROLLER),
    ESP32_SOC(false, true, ComponentType.MICROCONTROLLER),
    ESP32_S2_SOC(false, true, ComponentType.MICROCONTROLLER),
    ESP32_S3_SOC(false, true, ComponentType.MICROCONTROLLER),
    ESP32_C3_SOC(false, true, ComponentType.MICROCONTROLLER),
    ESP32_WROOM_MODULE(false, true, ComponentType.MICROCONTROLLER),
    ESP32_WROVER_MODULE(false, true, ComponentType.MICROCONTROLLER);

    private final boolean passive;
    private final boolean semiconductor;
    private final ComponentType baseType;

    EspressifComponentType(boolean passive, boolean semiconductor, ComponentType baseType) {
        this.passive = passive;
        this.semiconductor = semiconductor;
        this.baseType = baseType;
    }

    @Override
    public boolean isPassive() {
        return passive;
    }

    @Override
    public boolean isSemiconductor() {
        return semiconductor;
    }

    @Override
    public ComponentType getBaseType() {
        return baseType;
    }
}