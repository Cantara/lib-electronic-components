# Silicon Labs Manufacturer Handler Skill

## Overview
SiliconLabsHandler manages Silicon Labs components including 8-bit MCUs (C8051, EFM8), 32-bit ARM MCUs (EFM32), wireless MCUs (EFR32), timing ICs, sensors, and USB bridges.

## Supported Component Types
- MICROCONTROLLER
- MICROCONTROLLER_SILABS
- MCU_SILABS
- MICROCONTROLLER_SILICON_LABS
- MCU_SILICON_LABS
- EFM8_MCU
- EFM32_MCU
- EFR32_MCU
- IC
- CRYSTAL
- OSCILLATOR

## MPN Patterns

### 8-bit MCUs (8051-based)
| Prefix | Description |
|--------|-------------|
| C8051Fxxx | C8051 MCUs |

### EFM8 Series (8-bit MCUs)
| Prefix | Description |
|--------|-------------|
| EFM8BBxxx | Busy Bee |
| EFM8SBxxx | Sleepy Bee |
| EFM8LBxxx | Laser Bee |

### EFM32 Series (32-bit ARM MCUs)
| Prefix | Description |
|--------|-------------|
| EFM32GGxxx | Giant Gecko |
| EFM32WGxxx | Wonder Gecko |
| EFM32HGxxx | Happy Gecko |
| EFM32PGxxx | Pearl Gecko |

### EFR32 Series (Wireless MCUs)
| Prefix | Description |
|--------|-------------|
| EFR32BGxxx | Blue Gecko (Bluetooth) |
| EFR32FGxxx | Flex Gecko (Proprietary) |
| EFR32MGxxx | Mighty Gecko (Multi-protocol) |

### Wireless ICs
| Prefix | Description |
|--------|-------------|
| SIxxxx | General wireless ICs |
| BGMxxxx | Blue Gecko Modules |
| EZR32xxx | EZR32 Wireless MCUs |

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
- EFM32/EFR32: Single letter before dash (G=BGA) or full package name after dash (QFP100)
- C8051: Suffix after dash (e.g., GQ)
- SI parts: Suffix after last dash

## Series Extraction
Returns human-readable series names:
- "C8051", "EFM8 Busy Bee", "EFM8 Sleepy Bee", "EFM8 Laser Bee"
- "EFM32 Giant Gecko", "EFM32 Wonder Gecko", "EFM32 Happy Gecko", "EFM32 Pearl Gecko"
- "Blue Gecko", "Flex Gecko", "Mighty Gecko"
- "Timing", "Environmental Sensor", "Proximity Sensor", "USB Bridge"
- "Digital Isolator", "Isolated Gate Driver", "Bluetooth Module"

## Replacement Logic
- Must be same series for compatibility
- Pin-compatible parts within same subfamily may be replaceable

## Test Patterns
When testing SiliconLabsHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new SiliconLabsHandler()`

## Known Handler Issues
- Uses HashSet in getSupportedTypes() - should use Set.of()
