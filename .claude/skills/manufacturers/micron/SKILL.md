# Micron Manufacturer Handler Skill

## Overview
MicronHandler manages Micron Technology components including DRAM, NAND Flash, NOR Flash, and SSDs.

## Supported Component Types
- MEMORY
- MEMORY_MICRON
- MEMORY_FLASH
- MEMORY_EEPROM

## MPN Patterns

### DRAM
| Prefix | Description |
|--------|-------------|
| MT40xxx | DDR4 SDRAM |
| MT41xxx | DDR3 SDRAM |
| MT47xxx | DDR2 SDRAM |
| MT46xxx | DDR SDRAM |
| MT48xxx | LPDDR4 |
| MT52xxx | LPDDR3 |

### NAND Flash
| Prefix | Description |
|--------|-------------|
| MT29F/E | NAND Flash |
| MT29C/B | SLC NAND |

### NOR Flash
| Prefix | Description |
|--------|-------------|
| MT28E/F | Parallel NOR Flash |
| MT25Q/T | Serial NOR Flash |

### Phase Change Memory
| Prefix | Description |
|--------|-------------|
| MT35X/Q | 3D XPoint |

### Managed NAND
| Prefix | Description |
|--------|-------------|
| MT29K/L | eMMC |
| MTFD | SSD |

## Package Code Extraction
Extracted from suffix after last dash:
- BGA, FBGA, TFBGA, VFBGA, WBGA
- TSOP, TSSOP, SOP
- LGA

## Series Extraction
Returns first 4-5 characters of base part number (e.g., "MT40A", "MT29F", "MTFDD").

## Temperature Range Codes
| Code | Range |
|------|-------|
| -I | Industrial (-40 to +85°C) |
| -C | Commercial (0 to +70°C) |
| -A | Automotive (-40 to +105°C) |
| -E | Extended (-40 to +95°C) |

## Replacement Logic
- Must be same series and density
- Speed grade: lower number = faster (faster can replace slower)
- Temperature range compatibility:
  - Automotive > Industrial > Extended > Commercial
  - Higher temp range can replace lower

## Test Patterns
When testing MicronHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new MicronHandler()`
