# Silicon Labs Manufacturer Handler Skill

## Overview
SiliconLabsHandler manages Silicon Labs components including EFM8/EFM32/EFR32 MCUs, timing ICs, sensors, and USB bridges.

## Supported Component Types
- MICROCONTROLLER, MICROCONTROLLER_SILICON_LABS, MCU_SILICON_LABS
- MICROCONTROLLER_SILABS, MCU_SILABS
- EFM8_MCU, EFM32_MCU, EFR32_MCU

## MPN Patterns

### 8-bit MCUs
| Prefix | Description |
|--------|-------------|
| C8051Fxxx | C8051 MCUs (8051-based) |
| EFM8BBxxx | EFM8 Busy Bee |
| EFM8SBxxx | EFM8 Sleepy Bee |
| EFM8LBxxx | EFM8 Laser Bee |

### 32-bit ARM MCUs (EFM32)
| Prefix | Description |
|--------|-------------|
| EFM32GGxxx | Giant Gecko (Cortex-M3) |
| EFM32WGxxx | Wonder Gecko (Cortex-M4) |
| EFM32HGxxx | Happy Gecko (Cortex-M0+) |
| EFM32PGxxx | Pearl Gecko (Cortex-M4) |

### Wireless MCUs (EFR32)
| Prefix | Description |
|--------|-------------|
| EFR32BGxxx | Blue Gecko (Bluetooth) |
| EFR32FGxxx | Flex Gecko (Proprietary) |
| EFR32MGxxx | Mighty Gecko (Multi-protocol) |

### Timing Solutions
| Prefix | Description |
|--------|-------------|
| SI5xxx | Clock ICs |
| 501xxx | Crystal products |
| 598xxx | Oscillator products |

### Sensors
| Prefix | Description |
|--------|-------------|
| SI7xxx | Temperature/Humidity sensors |
| SI1xxx | Proximity/Ambient light sensors |

### Interface ICs
| Prefix | Description |
|--------|-------------|
| CP2xxx | USB-UART bridges |
| SI321x | USB controllers |

### Isolators
| Prefix | Description |
|--------|-------------|
| SI84xx | Digital isolators |
| SI86xx | Isolated gate drivers |

## Package Code Extraction
- C8051: Extracted after last dash (e.g., C8051F380-GQ → GQ)
- EFM32/EFR32: Look for common package names (QFP, BGA, QFN, CSP) or single letter before dash

## Series Extraction
Returns series names:
- "C8051"
- "EFM8 Busy Bee", "EFM8 Sleepy Bee", "EFM8 Laser Bee"
- "EFM32 Giant Gecko", "EFM32 Wonder Gecko", "EFM32 Happy Gecko", "EFM32 Pearl Gecko"
- "Blue Gecko", "Flex Gecko", "Mighty Gecko"
- "Timing", "Environmental Sensor", "Proximity Sensor"
- "USB Bridge", "Digital Isolator", "Isolated Gate Driver"

## Replacement Logic
- Parts must be from same series
- Same base part number with different package/temperature grade may be pin-compatible

## Test Patterns
When testing SiliconLabsHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new SiliconLabsHandler()`
