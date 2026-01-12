# Amphenol Manufacturer Handler Skill

## Overview
AmphenolHandler manages Amphenol components including Mini-PV, HD20, SFP+, USB 3.0, and various industrial connectors.

## Supported Component Types
- CONNECTOR
- CONNECTOR_AMPHENOL

## MPN Patterns

### Mini-PV Series (2.0mm pitch)
| Prefix | Description |
|--------|-------------|
| 504182-xxxx | Mini-PV THT |
| 505478-xxxx | Mini-PV SMT |

### HD20 Series (2.0mm pitch)
| Prefix | Description |
|--------|-------------|
| 10120843-xxxx | HD20 THT |
| 10120855-xxxx | HD20 SMT |

### SFP/SFP+ Cages
| Prefix | Description |
|--------|-------------|
| RJHSE-538x-xx | SFP+ cages |

### USB 3.0 Connectors
| Prefix | Description |
|--------|-------------|
| USB3-A-xx-xxxx | USB 3.0 Type A |

### Additional Series
| Prefix | Description |
|--------|-------------|
| 10051922-xxxx | MinitekPwr (3.0mm pitch) |
| 10151980-xxxx | BergStik headers (2.54mm pitch) |
| 10129378-xxxx | ICC headers (2.54mm pitch) |
| RJMG2310-xx | RJ45 connectors |

## Series Properties
| Series | Family | Pitch | Current Rating |
|--------|--------|-------|----------------|
| 504182 | Mini-PV | 2.00mm | 3.0A |
| 505478 | Mini-PV SMT | 2.00mm | 3.0A |
| 10120843 | HD20 | 2.00mm | 3.0A |
| 10120855 | HD20 SMT | 2.00mm | 3.0A |
| 10051922 | MinitekPwr | 3.00mm | 5.0A |
| 10151980 | BergStik | 2.54mm | 3.0A |

## Package Code Extraction
Extracted from last 4 digits (after dash):
- First two digits typically indicate position/pin count

## Series Extraction
Returns series prefix (e.g., "504182", "10120843", "RJHSE").

## Helper Methods
- `getPitch(mpn)` - Returns pitch in mm
- `getMountingType(mpn)` - Returns THT, SMT, or Other
- `getOrientation(mpn)` - Returns Right Angle or Vertical
- `getRatedCurrent(mpn)` - Returns rated current in Amps
- `isShielded(mpn)` - Returns true for SFP and USB connectors
- `getContactPlating(mpn)` - Returns Gold, Tin, or Standard
- `hasRetentionMechanism(mpn)` - Returns true for latched connectors
- `getApplicationType(mpn)` - Returns SFP/SFP+, USB 3.0, RJ45, Power, or Signal

## Replacement Logic
- Must be same series with same pin count
- Checks compatible mounting types (THT/SMT)

## Test Patterns
When testing AmphenolHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new AmphenolHandler()`

## Known Handler Issues
- Uses HashSet in getSupportedTypes() - should use Set.of()
