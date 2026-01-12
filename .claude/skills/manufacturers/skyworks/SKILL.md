# Skyworks Manufacturer Handler Skill

## Overview
SkyworksHandler manages Skyworks RF components including amplifiers, switches, front-end modules, filters, attenuators, power management, and Silicon Labs legacy MCUs.

## Supported Component Types
- RF_IC_SKYWORKS

## MPN Patterns

### RF Amplifiers
| Prefix | Description |
|--------|-------------|
| SKYxxxxx-xxx | RF Amplifiers |
| SExxxx | RF Power Amplifiers |

### RF Switches
| Prefix | Description |
|--------|-------------|
| ASxxxx | RF Switches |
| SKY13xxx | RF Switches |
| PExxxx | RF Switches |

### Front-End Modules
| Prefix | Description |
|--------|-------------|
| SKY7xxxx | Front-end Modules |
| SE47xx | 4G/5G FEMs |

### RF Filters
| Prefix | Description |
|--------|-------------|
| SKY6xxxx | RF Filters |
| SFxxxx | SAW Filters |

### Other RF Components
| Prefix | Description |
|--------|-------------|
| AATxxxx | Attenuators |
| SCxxxx | Voltage Regulators |

### Silicon Labs Legacy MCUs
| Prefix | Description |
|--------|-------------|
| EFM8xx | 8-bit MCUs |
| EFM32xx | 32-bit ARM MCUs |
| EFR32xx | Wireless MCUs |

### Wireless ICs
| Prefix | Description |
|--------|-------------|
| BGMxxx | Bluetooth Modules |
| EFR32BGxx | Blue Gecko |
| EFR32FGxx | Flex Gecko |
| EFR32MGxx | Mighty Gecko |

## Package Codes
| Suffix | Package |
|--------|---------|
| QFN | QFN |
| REEL, TR | Tape & Reel |
| WLCSP | Wafer Level CSP |
| CSP | Chip Scale Package |
| BGA | Ball Grid Array |
| 89 | QFN |
| 86 | CSP |
| 481 | QFN48 |
| 321 | QFN32 |

## Series Extraction
- SKY series: First 8 characters (e.g., "SKY13350")
- EFM/EFR series: First 7 alphanumeric characters
- Other: Prefix + first 5 characters

## Replacement Logic
- Same series compatibility
- Compatible series pairs: AS/PE switches, SKY65/SE65 amplifiers, EFM32GG/EFR32MG MCUs
- RF: Compatible frequency and power
- MCU: Compatible memory size

## Test Patterns
When testing SkyworksHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new SkyworksHandler()`

## Known Handler Issues
*All issues fixed in PR #88*

## Common Part Numbers
| Part Number | Description |
|-------------|-------------|
| SKY13350-385LF | SP3T RF Switch |
| SKY65017-70LF | LNA, 0.7-1.0GHz |
| SE2435L-R | 2.4GHz Front-End |
| AS179-92LF | GaAs SPDT Switch |
