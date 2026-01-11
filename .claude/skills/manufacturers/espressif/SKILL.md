# Espressif Part Number Reference

## MPN Structure Overview

Espressif produces WiFi/Bluetooth SoCs and modules:
- **ESP8266**: Legacy WiFi SoC
- **ESP32**: WiFi + Bluetooth SoC
- **ESP32-S2/S3**: Enhanced ESP32 variants
- **ESP32-C3/C6**: RISC-V based variants
- **WROOM/WROVER**: Module families

---

## SoC Part Number Format

```
ESP[Series]-[Variant][Package]
  |    |       |        |
  |    |       |        +-- Package suffix (optional)
  |    |       +-- Variant (D0WD, S2, S3, C3, etc.)
  |    +-- Series (8266, 32)
  +-- Espressif prefix
```

### Module Part Number Format

```
ESP32-[FormFactor]-[Revision][-U][-Memory]
  |        |            |      |      |
  |        |            |      |      +-- Memory config (N4, N8, R2, R8)
  |        |            |      +-- External antenna (U suffix)
  |        |            +-- Revision (E, B, etc.)
  |        +-- Form factor (WROOM, WROVER, MINI, PICO)
  +-- SoC type
```

### Example Decoding

```
ESP32-S3-WROOM-1-N16R8
  |   |    |   | |  |
  |   |    |   | |  +-- R8 = 8MB PSRAM
  |   |    |   | +-- N16 = 16MB Flash
  |   |    |   +-- Revision 1
  |   |    +-- WROOM form factor
  |   +-- S3 variant
  +-- ESP32 series
```

---

## SoC Variants

### ESP8266 Series

| SoC | Core | WiFi | BT | Notes |
|-----|------|------|----|-------|
| ESP8266EX | Tensilica | 802.11 b/g/n | No | Original |
| ESP8285 | Tensilica | 802.11 b/g/n | No | With 1MB flash |

### ESP32 Original

| SoC | Core | WiFi | BT |
|-----|------|------|----|
| ESP32-D0WD | Dual LX6 | 802.11 b/g/n | BT 4.2 |
| ESP32-S0WD | Single LX6 | 802.11 b/g/n | BT 4.2 |
| ESP32-PICO-D4 | Dual LX6 | 802.11 b/g/n | BT 4.2 |

### ESP32-S Series

| SoC | Core | WiFi | BT |
|-----|------|------|----|
| ESP32-S2 | Single LX7 | 802.11 b/g/n | No |
| ESP32-S3 | Dual LX7 | 802.11 b/g/n | BT 5.0 LE |

### ESP32-C Series (RISC-V)

| SoC | Core | WiFi | BT |
|-----|------|------|----|
| ESP32-C2 | RISC-V | 802.11 b/g/n | BT 5.0 LE |
| ESP32-C3 | RISC-V | 802.11 b/g/n | BT 5.0 LE |
| ESP32-C6 | RISC-V | WiFi 6 | BT 5.0 LE |

---

## Memory Configuration Suffixes

### Flash (N/H prefix)

| Suffix | Size | Temperature |
|--------|------|-------------|
| N4 | 4 MB | -40 to +85C |
| N8 | 8 MB | -40 to +85C |
| N16 | 16 MB | -40 to +85C |
| H4 | 4 MB | -40 to +105C |

### PSRAM (R suffix)

| Suffix | Size | Interface |
|--------|------|-----------|
| R2 | 2 MB | Quad SPI |
| R8 | 8 MB | Octal SPI |

---

## Module Form Factors

| Form Factor | Description | Size |
|-------------|-------------|------|
| WROOM | Standard, PCB antenna | 18x25mm |
| WROVER | WROOM + PSRAM (legacy) | 18x31mm |
| MINI | Compact, PCB antenna | 13x16mm |
| PICO | Ultra-compact SiP | 7x7mm |

---

## Supported ComponentTypes

```java
ComponentType.MICROCONTROLLER
ComponentType.MICROCONTROLLER_ESPRESSIF
ComponentType.MCU_ESPRESSIF
ComponentType.ESP8266_SOC
ComponentType.ESP32_SOC
ComponentType.ESP32_S2_SOC
ComponentType.ESP32_S3_SOC
ComponentType.ESP32_C3_SOC
ComponentType.ESP32_WROOM_MODULE
ComponentType.ESP32_WROVER_MODULE
```

---

## Known Handler Issues

### 1. Pattern Order Issues

Pattern `^ESP32.*` may match before more specific patterns like `^ESP32-S3.*`.

### 2. extractSeries WROOM Conflict

```java
if (upperMpn.contains("WROOM")) {
    if (upperMpn.startsWith("ESP32")) return "ESP32-WROOM";
    return "ESP-WROOM";
}
```

This means `ESP32-S3-WROOM-1` returns `ESP32-WROOM` instead of `ESP32-S3`.

### 3. Missing C6/H2 Types

ComponentType enum doesn't include ESP32_C6_SOC or ESP32_H2_SOC.

---

## Test Patterns

### Valid SoC MPNs

```
ESP8266EX
ESP32-D0WDQ6
ESP32-S2
ESP32-S3
ESP32-C3
```

### Valid Module MPNs

```
ESP32-WROOM-32E
ESP32-WROVER-E
ESP32-S3-WROOM-1-N16R8
ESP32-C3-MINI-1-N4
ESP32-C3-MINI-1U-N4
```

---

## Related Files

- Handler: `manufacturers/EspressifHandler.java`
- Component types: `EspressifComponentType.java`
- Test: `handlers/EspressifHandlerTest.java`

<!-- Add new learnings above this line -->
