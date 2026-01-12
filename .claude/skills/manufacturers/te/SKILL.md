# TE Connectivity Manufacturer Handler Skill

## Overview
TEHandler manages TE Connectivity components including terminal blocks, PCB headers, IDC connectors, and MATE-N-LOK connectors.

## Supported Component Types
- CONNECTOR
- CONNECTOR_TE

## MPN Patterns

### Terminal Blocks
| Pattern | Description |
|---------|-------------|
| 282xxx-x | Terminal blocks (5.08mm/5.00mm pitch) |

### PCB Headers
| Pattern | Description |
|---------|-------------|
| 5-xxx-x | PCB headers (2.54mm pitch) |
| 1-xxxxxx-x | Headers with prefix |
| 2-xxxxxx-x | Headers with prefix |

### IDC Connectors
| Pattern | Description |
|---------|-------------|
| 64xxxx-x | IDC connectors (2.54mm pitch) |

### MATE-N-LOK
| Pattern | Description |
|---------|-------------|
| 350xxx-x | MATE-N-LOK (4.14mm pitch) |

### Rectangular Connectors
| Pattern | Description |
|---------|-------------|
| 1-770966-x | Rectangular connector |
| 1-770967-x | Rectangular connector |

## Series Properties
| Series | Family | Pitch | Notes |
|--------|--------|-------|-------|
| 282837 | Terminal Block | 5.08mm | |
| 282836 | Terminal Block | 5.00mm | |
| 5-826 | PCB Header | 2.54mm | |
| 5-103 | PCB Header | 2.54mm | |
| 640456 | IDC Connector | 2.54mm | |
| 640457 | IDC Connector | 2.54mm | |
| 350211 | MATE-N-LOK | 4.14mm | |

## Package Code Extraction
Returns last digit (variant code) from terminal blocks and headers.

## Series Extraction
- For mapped series: Returns series prefix (e.g., "282836", "282837")
- For complex part numbers: Returns first digits and dash pattern (e.g., "1-770966")

## Helper Methods
- `getFamily(mpn)` - Returns family name (Terminal Block, PCB Header, etc.)
- `getPitch(mpn)` - Returns pitch in mm
- `getMountingType(mpn)` - Returns THT, SMD, Press-fit, Wire-wrap, or Screw
- `isPolarized(mpn)` - Returns false for terminal blocks
- `getGender(mpn)` - Returns Male, Female, or Unspecified based on suffix
- `getOrientation(mpn)` - Returns Right Angle, Vertical, or Standard

## Replacement Logic
- Must be same series
- Must have same pin count
- Must have same pitch
- Different package variants may be compatible within same series

## Test Patterns
When testing TEHandler:
1. Use assertions for terminal blocks and headers (stable behavior)
2. Use documentation tests for IDC and MATE-N-LOK
3. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
4. Instantiate directly: `new TEHandler()`

## Known Handler Issues
- Uses HashSet in getSupportedTypes() - should use Set.of()
