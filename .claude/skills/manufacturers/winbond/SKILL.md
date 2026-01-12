# Winbond Manufacturer Handler Skill

## Overview
WinbondHandler manages Winbond memory components including SPI Flash, NOR Flash, and EEPROM.

## Supported Component Types
- MEMORY
- MEMORY_FLASH_WINBOND
- MEMORY_EEPROM_WINBOND
- MEMORY_FLASH
- MEMORY_EEPROM

## MPN Patterns

### SPI/QSPI Flash (W25 Series)
| Prefix | Description |
|--------|-------------|
| W25Qxx | Standard SPI/QSPI Flash |
| W25Nxx | SPI NAND Flash |
| W25Xxx | Legacy SPI Flash |

### NOR Flash (W29 Series)
| Prefix | Description |
|--------|-------------|
| W29Cxx | Parallel NOR Flash |
| W29Nxx | NOR Flash variant |
| W29Exx | NOR Flash variant |

### EEPROM (W24 Series)
| Prefix | Description |
|--------|-------------|
| W24xxx | Serial EEPROM |

## Package Codes
| Suffix | Package |
|--------|---------|
| S, SS | SOIC |
| F | QFN |
| W | WSON |
| U | USON |

## Series Extraction
Returns base series including density code (e.g., "W25Q32JV" returns "W25Q32JV").

## Package Code Extraction
Extracts suffix letters and maps to package type.

## Custom matches() Implementation
Handler overrides `matches()` for direct pattern matching:
- W25[QNX][0-9]+ for SPI/QSPI Flash
- W29[CNE][0-9]+ for NOR Flash
- W24\d+ for EEPROM

## Replacement Logic
- Same series with different package or variant are compatible
- Must have same density

## Test Patterns
When testing WinbondHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new WinbondHandler()`

## Known Handler Issues
*All issues fixed in PR #87*

## Common Part Numbers
| Part Number | Description |
|-------------|-------------|
| W25Q128JVSIQ | 128Mbit QSPI Flash, SOIC-8 |
| W25Q32JVSSIQ | 32Mbit QSPI Flash, SOIC-8 |
| W25Q64FVFIG | 64Mbit QSPI Flash, QFN |
| W25N01GVZEIG | 1Gbit SPI NAND Flash |
