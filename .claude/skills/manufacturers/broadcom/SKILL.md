# Broadcom Manufacturer Handler Skill

## Overview
BroadcomHandler manages Broadcom components including Wi-Fi/Bluetooth combos, network switches, storage controllers, and PHYs.

## Supported Component Types
- IC

## MPN Patterns

### Wi-Fi/Bluetooth Combos
| Prefix | Description |
|--------|-------------|
| BCM2xxxx | Wi-Fi/BT combos |
| BCM3xxxx | Wi-Fi/BT combos |
| BCM4xxxx | Wi-Fi/BT combos |
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
| BCM1600x | SAS controllers |
| BCM2200x | RAID controllers |
| BCM8450x | NVMe controllers |

### Fiber Channel
| Prefix | Description |
|--------|-------------|
| BCM425xx | Fiber Channel |
| BCM578xx | Fiber Channel HBAs |

### PHYs
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
Extracted from suffix after dash:
- KF = FCBGA (Fine-pitch BGA)
- KU = UBGA (Ultra-fine BGA)
- B0 = BGA (Standard BGA)
- MC = MCM (Multi-chip module)
- FP = FCBGA-POD (Package-on-package)
- LP = LFBGA (Low-profile FBGA)
- HP = HSBGA (Heat-sink BGA)
- WH = WLCSP (Wafer-level CSP)
- FL = FLIP-CHIP (Flip-chip BGA)

## Series Extraction
Returns first 7-8 characters including prefix and up to 4 digits (e.g., "BCM4375", "BCM5781").

## Replacement Logic
- Must be same series for compatibility
- Wi-Fi capability: WiFi-6 can replace WiFi-5/4
- Bluetooth version: Higher versions can replace lower
- Network speed: Higher speed can replace lower
- Port count: More ports can replace fewer

## Test Patterns
When testing BroadcomHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new BroadcomHandler()`

## Known Handler Issues
*All issues fixed in PR #86*
