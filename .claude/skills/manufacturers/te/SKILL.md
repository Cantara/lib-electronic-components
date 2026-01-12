# TE Connectivity Manufacturer Handler Skill

## Overview
TEHandler manages TE Connectivity components including terminal blocks, PCB headers, IDC connectors, and MATE-N-LOK connectors.

## Supported Component Types
- CONNECTOR
- CONNECTOR_TE

## MPN Patterns

### Terminal Blocks
| Prefix | Description |
|--------|-------------|
| 282837-x | Terminal blocks (5.08mm pitch) |
| 282836-x | Terminal blocks (5.00mm pitch) |

### PCB Headers
| Prefix | Description |
|--------|-------------|
| 5-826xxx | PCB headers (2.54mm pitch) |
| 5-103xxx | PCB headers (2.54mm pitch) |

### IDC Connectors
| Prefix | Description |
|--------|-------------|
| 640456-xx | IDC connectors (2.54mm pitch) |
| 640457-xx | IDC connectors (2.54mm pitch) |

### MATE-N-LOK
| Prefix | Description |
|--------|-------------|
| 350211-x | MATE-N-LOK (4.14mm pitch) |

### Headers with Prefix
| Prefix | Description |
|--------|-------------|
| 1-xxxxxx-x | Rectangular connectors |
| 2-xxxxxx-x | Rectangular connectors |

## Series Families
| Series | Family | Pitch |
|--------|--------|-------|
| 282837 | Terminal Block | 5.08mm |
| 282836 | Terminal Block | 5.00mm |
| 5-826 | PCB Header | 2.54mm |
| 5-103 | PCB Header | 2.54mm |
| 640456 | IDC Connector | 2.54mm |
| 640457 | IDC Connector | 2.54mm |
| 350211 | MATE-N-LOK | 4.14mm |

## Package Code Extraction
Extracted from last digit of MPN, indicates variant:
- 1 = THT (Male)
- 2 = SMD (Female)
- 3 = Press-fit
- 4 = Wire-wrap
- 5 = Screw

## Series Extraction
Returns series prefix (e.g., "282837", "5-826").

## Helper Methods
- `getFamily(mpn)` - Returns family name (Terminal Block, PCB Header, etc.)
- `getPitch(mpn)` - Returns pitch in mm
- `getMountingType(mpn)` - Returns THT, SMD, Press-fit, etc.
- `isPolarized(mpn)` - Returns true for polarized connectors
- `getGender(mpn)` - Returns Male, Female, or Unspecified
- `getOrientation(mpn)` - Returns Right Angle, Vertical, or Standard

## Replacement Logic
- Must be same series and pin count
- Must have same pitch
- Different package codes may be compatible within same series

## Test Patterns
When testing TEHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new TEHandler()`
