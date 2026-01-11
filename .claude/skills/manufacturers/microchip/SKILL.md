# Microchip Part Number Reference

## MPN Structure Overview

Microchip part numbers follow a structured format that identifies the product family, features, temperature grade, and package type.

### General Format
```
[Family][Series][Features]-[TempGrade]/[Package]
```

Example: `PIC16F877A-I/P`
- **PIC16F**: Family (8-bit PIC with Flash)
- **877A**: Device number and revision
- **-I**: Industrial temperature grade
- **/P**: PDIP package

---

## PIC Microcontroller Families

### 8-bit PIC Families

| Prefix | Description | Architecture |
|--------|-------------|--------------|
| PIC10F | 6-8 pin baseline | 12-bit core |
| PIC12F | 8 pin mid-range | 14-bit core |
| PIC16F | Mid-range, most popular | 14-bit core |
| PIC16F1xxx | Enhanced mid-range | 14-bit enhanced |
| PIC18F | High-performance 8-bit | 16-bit core |

### Memory Type Indicator

| Letter | Meaning |
|--------|---------|
| F | Flash memory (electrically erasable) |
| C | OTP/EPROM (UV erasable, older parts) |
| LF | Low-voltage Flash (1.8V - 3.6V) |

### Common 8-bit Device Numbers

| Series | Pin Count | Features |
|--------|-----------|----------|
| PIC16F84A | 18 | Classic EEPROM device |
| PIC16F877A | 40 | Popular development MCU |
| PIC16F628A | 18 | USART, comparators |
| PIC18F452 | 40 | High-performance |
| PIC18F4550 | 40 | USB capable |

---

## 16-bit PIC Families

### PIC24 Series

| Prefix | Description |
|--------|-------------|
| PIC24F | General purpose, low power |
| PIC24FJ | Enhanced PIC24F with more peripherals |
| PIC24H/HJ | High performance (40 MIPS) |
| PIC24EP | Enhanced high performance |

### dsPIC Series (Digital Signal Controllers)

| Prefix | Description |
|--------|-------------|
| dsPIC30F | Original dsPIC, motor control |
| dsPIC33FJ | Higher performance, more memory |
| dsPIC33EP | Enhanced peripherals |
| dsPIC33EV | 5V tolerant |
| dsPIC33CH | Dual core |
| dsPIC33CK | Single core, cost-optimized |

Example: `dsPIC33FJ128GP706A-I/PT`
- **dsPIC33FJ**: dsPIC33F series, J subfamily
- **128**: 128KB Flash
- **GP**: General Purpose
- **706A**: 64-pin, revision A
- **-I**: Industrial temp
- **/PT**: TQFP package

---

## 32-bit PIC32 Families

| Series | Core | Description |
|--------|------|-------------|
| PIC32MX | MIPS32 M4K | Original 32-bit, general purpose |
| PIC32MZ | MIPS microAptiv | High performance, up to 252 MHz |
| PIC32MM | MIPS M4K | Low power, low cost |
| PIC32MK | MIPS microAptiv | Motor control focus |
| PIC32WK | - | Wireless (WiFi) integrated |

### PIC32MX Sub-series

| Sub-series | Focus |
|------------|-------|
| PIC32MX1xx | Cost-optimized |
| PIC32MX2xx | USB capable |
| PIC32MX3xx | General purpose |
| PIC32MX4xx | USB + more memory |
| PIC32MX5xx-7xx | Ethernet MAC |

### PIC32MZ Sub-series

| Sub-series | Description |
|------------|-------------|
| PIC32MZ EC | Basic MZ series |
| PIC32MZ EF | With FPU (floating point) |
| PIC32MZ DA | With graphics controller + DDR2 |

Example: `PIC32MX795F512H-80I/PT`
- **PIC32MX**: 32-bit family
- **795**: Series (Ethernet, USB)
- **F512**: 512KB Flash
- **H**: 64-pin variant
- **-80**: 80 MHz speed grade
- **I**: Industrial temp
- **/PT**: TQFP package

---

## Temperature Grade Codes

| Code | Range | Description |
|------|-------|-------------|
| (none) | 0 to +70C | Commercial |
| I | -40 to +85C | Industrial |
| E | -40 to +125C | Extended |
| H | -40 to +150C | High Temperature (select parts) |

---

## Package Codes

### Common Package Suffixes

| Code | Package Type | Description |
|------|--------------|-------------|
| P | PDIP | Plastic DIP (through-hole) |
| SP | SPDIP | Skinny PDIP (300 mil) |
| SO | SOIC | Small Outline IC |
| SS | SSOP | Shrink Small Outline Package |
| ST | TSSOP | Thin Shrink SOP |
| PT | TQFP | Thin Quad Flat Pack |
| PF | TQFP | TQFP (larger body) |
| ML | QFN | Quad Flat No-Lead |
| MV | QFN | QFN variant |
| MC | DFN | Dual Flat No-Lead |
| MF | DFN-S | DFN (small) |
| L | PLCC | Plastic Leaded Chip Carrier |
| OT | SOT-23 | Small Outline Transistor |

### Combined Temperature + Package Examples

| Suffix | Meaning |
|--------|---------|
| -I/P | Industrial, PDIP |
| -E/SO | Extended, SOIC |
| -I/PT | Industrial, TQFP |
| -I/ML | Industrial, QFN |
| -E/SS | Extended, SSOP |
| -I/SP | Industrial, Skinny PDIP |

---

## Memory Products

### I2C EEPROM (24xx Series)

Format: `24[Voltage][Size]-[Temp]/[Package]`

| Prefix | Voltage Range |
|--------|---------------|
| 24AA | 1.7V - 5.5V |
| 24LC | 2.5V - 5.5V |
| 24FC | 1.7V - 5.5V (High-speed 1MHz) |

| Size Code | Capacity |
|-----------|----------|
| 00 | 128 bits |
| 01 | 1 Kbit |
| 02 | 2 Kbit |
| 04 | 4 Kbit |
| 08 | 8 Kbit |
| 16 | 16 Kbit |
| 32 | 32 Kbit |
| 64 | 64 Kbit |
| 128 | 128 Kbit |
| 256 | 256 Kbit |
| 512 | 512 Kbit |
| 1025 | 1 Mbit |

Example: `24LC256-I/SN`
- **24LC**: 2.5V I2C EEPROM
- **256**: 256 Kbit (32KB)
- **-I**: Industrial temp
- **/SN**: SOIC-8 package

### SPI EEPROM (25xx Series)

| Prefix | Voltage Range |
|--------|---------------|
| 25AA | 1.8V - 5.5V |
| 25LC | 2.5V - 5.5V |

### Microwire EEPROM (93xx Series)

| Prefix | Description |
|--------|-------------|
| 93LC | Standard voltage |
| 93AA | Low voltage |

---

## Interface ICs (MCP Series)

### CAN Controllers and Transceivers

| Part | Description |
|------|-------------|
| MCP2515 | Stand-alone CAN Controller (SPI) |
| MCP2517FD | CAN FD Controller |
| MCP2518FD | CAN FD Controller (enhanced) |
| MCP2551 | High-speed CAN Transceiver |
| MCP2561 | CAN FD Transceiver |
| MCP2562 | CAN FD Transceiver (fault-tolerant) |

### USB Bridges

| Part | Description |
|------|-------------|
| MCP2200 | USB to UART Bridge |
| MCP2221 | USB to I2C/UART Bridge |
| MCP2221A | Enhanced USB Bridge |

### Real-Time Clocks

| Part | Description |
|------|-------------|
| MCP7940N | I2C RTC |
| MCP79410 | RTC with EEPROM |
| MCP79411 | RTC with unique ID |
| MCP7951x | SPI RTC with SRAM |

---

## Handler Implementation Notes

### Current MicrochipHandler Issues

1. **HashSet Usage**: Uses `new HashSet<>()` instead of `Set.of()` for immutable sets
2. **Pattern Duplication**: Many patterns registered for both base type and manufacturer-specific type
3. **No Debug Statements**: Clean implementation without println

### Pattern Registration

The handler registers patterns for:
- All dsPIC variants (30F, 33FJ, 33EP, 33EV, 33CH, 33CK)
- 8-bit PIC (10F, 10LF, 12F, 12LF, 16F, 16LF, 18F, 18LF)
- 16-bit PIC24 (24F, 24FJ, 24EP, 24HJ)
- 32-bit PIC32 (MX, MZ, MM, MK, WK)
- Memory (24AA, 24LC, 24FC, 25AA, 25LC, 93LC, 93AC)
- Interface ICs (MCP25xx, MCP22xx, MCP79xx)

### Package Extraction Logic

Current implementation handles:
- Hyphen-separated suffixes: `-P`, `-PT`, `-ML`, `-SO`, `-SS`, `-ST`
- Combined temp+package: `-I/P`, `-E/SO`, etc.
- Embedded codes: `/P`, `/PT`, `/ML`, `/SO`

### Series Extraction

- PIC: Extracts up to first non-alphanumeric character
- dsPIC33: Special handling to include sub-family (FJ, EP, etc.)
- Memory: Extracts first 4 characters (e.g., "24LC")
- MCP: Extracts "MCP" + numeric suffix

### Supported ComponentTypes

```java
ComponentType.MICROCONTROLLER
ComponentType.MICROCONTROLLER_MICROCHIP
ComponentType.MCU_MICROCHIP
ComponentType.MEMORY
ComponentType.MEMORY_MICROCHIP
ComponentType.PIC_MCU
ComponentType.AVR_MCU
ComponentType.MICROCONTROLLER_ATMEL
ComponentType.MCU_ATMEL
ComponentType.MEMORY_ATMEL
ComponentType.TOUCH_ATMEL
ComponentType.CRYPTO_ATMEL
```

Note: Handler also covers former Atmel products (AVR, etc.) since Microchip acquired Atmel.

---

## Test Patterns

### Valid PIC MPNs for Testing

```
# 8-bit
PIC10F200-I/P
PIC12F675-I/SN
PIC16F84A-04/P
PIC16F877A-I/P
PIC16F1459-I/SS
PIC18F4550-I/PT
PIC18LF46K22-I/PT

# 16-bit
PIC24FJ64GA002-I/SP
PIC24EP512GP806-I/PT
dsPIC30F4013-30I/P
dsPIC33FJ128GP706A-I/PT
dsPIC33EP512MU810-I/PT

# 32-bit
PIC32MX250F128B-I/SP
PIC32MX795F512H-80I/PT
PIC32MZ2048EFH144-I/PH

# Memory
24LC256-I/SN
24FC512-E/SM
25LC1024-I/P

# Interface
MCP2515-I/SO
MCP2221A-I/SL
```

---

## References

- [Microchip Packaging Specification](https://ww1.microchip.com/downloads/en/packagingspec/00049ad.pdf)
- [PIC Microcontrollers Wikipedia](https://en.wikipedia.org/wiki/PIC_microcontrollers)
- [Microchip Forum - PIC Naming Conventions](https://forum.microchip.com/s/topic/a5C3l000000M6J5EAK/t254303)
- [PIC32 Family Reference Manual](https://ww1.microchip.com/downloads/en/DeviceDoc/61132B_PIC32ReferenceManual.pdf)
