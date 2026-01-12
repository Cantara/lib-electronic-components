# ISSI Manufacturer Handler Skill

## Overview
ISSIHandler manages ISSI (Integrated Silicon Solution Inc.) memory components including SRAM, DRAM, Flash, LED drivers, and specialty memory.

## Supported Component Types
- MEMORY
- MEMORY_ISSI
- MEMORY_FLASH
- MEMORY_EEPROM

## MPN Patterns

### SRAM
| Prefix | Description |
|--------|-------------|
| IS61xx | Async SRAM |
| IS62xx | Low Power SRAM |
| IS64xx | Sync SRAM |

### DRAM
| Prefix | Description |
|--------|-------------|
| IS42xx | SDRAM |
| IS43xx | DDR SDRAM |
| IS45xx | DDR2 SDRAM |
| IS47xx | DDR3 SDRAM |

### Flash Memory
| Prefix | Description |
|--------|-------------|
| IS25xx | SPI Flash |
| IS29xx | NOR Flash |

### LED Drivers
| Prefix | Description |
|--------|-------------|
| IS31FL | LED Matrix Drivers |
| IS31AP | Audio Modulated LED Drivers |
| IS31LT | LED Lighting Drivers |

### Specialty Memory
| Prefix | Description |
|--------|-------------|
| IS65xx | FIFO |
| IS67xx | Dual-Port RAM |

## Package Codes
| Suffix | Package |
|--------|---------|
| T | TSOP |
| U | SOIC |
| L | PLCC |
| V | TSSOP |
| TL | TSOP-II |
| TR | TFBGA |
| B | BGA |
| BK | FBGA |
| WB | WBGA |
| K | VFBGA |
| M | QFN |
| Q | QFP |

## Series Extraction
Returns series code with first 2 characters after prefix (e.g., "IS61WV1288EDBLL" returns "IS61").

## Replacement Logic
- Must be same series
- Must have same density
- Faster speed grade can replace slower (lower number = faster)

## Test Patterns
When testing ISSIHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new ISSIHandler()`

## Known Handler Issues
*All issues fixed in PR #88*
- Package code extraction regex may not work correctly for all formats

## Common Part Numbers
| Part Number | Description |
|-------------|-------------|
| IS61WV1288EDBLL | 1Mbit Async SRAM, TSOP-44 |
| IS42S16160G-7TL | 256Mbit SDRAM, TSOP-II |
| IS25LP128F-JBLE | 128Mbit SPI Flash, BGA |
| IS31FL3731 | LED Matrix Driver |
