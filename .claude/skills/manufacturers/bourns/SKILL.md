# Bourns Manufacturer Handler Skill

## Overview
BournsHandler manages Bourns electronic components including resistors, inductors, circuit protection, and sensors.

## Supported Component Types
- RESISTOR, RESISTOR_CHIP_BOURNS
- INDUCTOR, INDUCTOR_CHIP_BOURNS, INDUCTOR_THT_BOURNS
- POTENTIOMETER_BOURNS
- TRANSFORMER_BOURNS
- CIRCUIT_PROTECTION_BOURNS, TVS_DIODE_BOURNS, PPTC_FUSE_BOURNS

## MPN Patterns

### Resistors
| Prefix | Description |
|--------|-------------|
| CR | Standard chip resistors |
| CRA | Anti-sulfur resistors |
| CRF | Fusible resistors |
| CRM | MELF resistors |
| PWR | Power resistors |

### Inductors
| Prefix | Description |
|--------|-------------|
| SRN | Power inductors (non-shielded) |
| SRP | Power inductors (high current) |
| SRR | Power inductors (shielded) |
| SDR | Power inductors (unshielded) |
| RLB | Through-hole inductors |

### Circuit Protection
| Prefix | Description |
|--------|-------------|
| CDSOT | TVS diode arrays |
| CDDFN | Ultra-low cap TVS arrays |
| MOV- | Metal oxide varistors |
| MF-R | PTC resettable fuses |
| MF-S | Surface mount fuses |

### Sensors & Controls
| Prefix | Description |
|--------|-------------|
| PTA | Position sensors |
| PTB | Motion sensors |
| PTV | Potentiometers |
| PEC | Panel encoders |
| 3005 | Cermet trimpots |
| 3006 | Wirewound trimpots |

## Package Code Extraction

### Resistor Size Codes
- 01 = 0201/0603M
- 02 = 0402/1005M
- 03 = 0603/1608M
- 06 = 0805/2012M
- 12 = 1206/3216M
- 20 = 2010/5025M
- 25 = 2512/6432M

## Series Extraction
Returns human-readable series names:
- "Chip Resistor", "Anti-Sulfur Resistor", "Fusible Resistor"
- "Shielded Inductor", "Non-Shielded Inductor", "High Current Inductor"
- "TVS Diode Array", "Metal Oxide Varistor", "Resettable Fuse"

## Replacement Logic
- Anti-sulfur resistors can replace standard chip resistors
- Shielded inductors can replace non-shielded of same size
- Checks package, power rating, and tolerance compatibility

## Test Patterns
When testing BournsHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new BournsHandler()`
