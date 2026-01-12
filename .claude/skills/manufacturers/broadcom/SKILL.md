# Broadcom Manufacturer Handler Skill

## Overview
BroadcomHandler manages Broadcom components including Wi-Fi/Bluetooth combos, network switches, storage controllers, and PHYs.

**Note**: Broadcom acquired Avago Technologies in 2015 and merged with CA Technologies in 2018.

## Supported Component Types
- IC (generic)
- Wi-Fi/Bluetooth combo chips
- Network switches
- Storage controllers
- PHYs

## MPN Patterns

### Wi-Fi/Bluetooth Combo Chips
| Prefix | Description |
|--------|-------------|
| BCM2xxx-4xxx | Wi-Fi/BT combos |
| BCM43xxx | Wi-Fi 5/6/6E |
| BCM89xxx | Wi-Fi 6/6E |
| BCM4774x | GNSS combos |

### Network Switches
| Prefix | Description |
|--------|-------------|
| BCM53xxx | Ethernet switches |
| BCM56xxx | StrataXGS switches |
| BCM58xxx | Trident switches |
| BCM88xxx | StrataDNX switches |

### Network Controllers
| Prefix | Description |
|--------|-------------|
| BCM57xxx | NetXtreme controllers |
| BCM5719 | NetXtreme GbE |
| BCM5720 | NetXtreme GbE |
| BCM5762 | NetXtreme GbE |

### Storage Controllers
| Prefix | Description |
|--------|-------------|
| BCM1600 | SAS controllers |
| BCM2200 | RAID controllers |
| BCM8450 | NVMe controllers |

### PHYs and MACs
| Prefix | Description |
|--------|-------------|
| BCM5241x | Gigabit PHYs |
| BCM5461x | Gigabit PHYs |
| BCM5482x | Dual PHYs |

### PCIe Switches
| Prefix | Description |
|--------|-------------|
| PEXxxxx | PCIe switches |
| PLXxxxx | PLX PCIe switches |

## Package Code Extraction
Extracted from suffix after last dash:
- KF = FCBGA (Fine-pitch BGA)
- KU = UBGA (Ultra-fine BGA)
- B0 = BGA
- MC = MCM (Multi-chip module)
- WH = WLCSP
- LP = LFBGA

## Series Extraction
Returns base series name (first 7-8 characters for BCM parts).

## Replacement Logic
- Checks if parts are from compatible series
- For Wi-Fi/BT chips: compares Wi-Fi and Bluetooth versions
- For network switches: compares speed and port counts
- For storage controllers: compares interface type

## Test Patterns
When testing BroadcomHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new BroadcomHandler()`
