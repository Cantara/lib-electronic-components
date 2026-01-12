# Hirose Manufacturer Handler Skill

## Overview
HiroseHandler manages Hirose connectors including DF13, DF14, FH12, BM, DF52, DF63, FH19, FH28, and GT17 series.

## Supported Component Types
- CONNECTOR
- CONNECTOR_HIROSE

## MPN Patterns

### DF13 Series (1.25mm pitch, 1A)
| Pattern | Description |
|---------|-------------|
| DF13-xP-xxx | Through-hole vertical |
| DF13-xR-xxx | Through-hole right angle |
| DF13-xS-xxx | Surface mount |

### DF14 Series (1.25mm pitch, 2A, Automotive)
| Pattern | Description |
|---------|-------------|
| DF14-xP-xxx | Through-hole vertical |
| DF14-xR-xxx | Through-hole right angle |
| DF14-xS-xxx | Surface mount |

### FH12 Series (0.5mm pitch, FFC/FPC)
| Pattern | Description |
|---------|-------------|
| FH12-xS-xxx | Socket (bottom contact) |
| FH12-xP-xxx | Socket (top contact) |

### BM Series (USB Connectors)
| Pattern | Description |
|---------|-------------|
| BMxx-xxx | USB Type connectors |

### Additional Series
| Series | Pitch | Application |
|--------|-------|-------------|
| DF52 | 0.8mm | Board-to-board |
| DF63 | 0.5mm | Board-to-FPC |
| FH19 | 0.4mm | FFC/FPC |
| FH28 | 0.4mm | Dual row FFC/FPC |
| GT17 | 0.5mm | Board-to-board |

## Package Code Extraction
Returns suffix after last dash (e.g., "DF13-5P-1.25DS" returns "1.25DS").

## Series Extraction
Returns series prefix (e.g., "DF13", "FH12", "BM").

## Helper Methods
- `getPitch(mpn)` - Returns pitch in mm
- `getMountingType(mpn)` - Returns SMT, THT, or Other
- `getOrientation(mpn)` - Returns Right Angle or Vertical
- `getRatedCurrent(mpn)` - Returns rated current in Amps
- `isShielded(mpn)` - Returns true if DS/DSS in package code
- `getContactPlating(mpn)` - Returns Gold, Tin, or Standard
- `isLockingType(mpn)` - Returns true for DF series
- `getApplicationType(mpn)` - Returns FFC/FPC, USB, Board-to-Board, Automotive, or General Purpose

## Replacement Logic
- Must be from same series
- Must have same pin count
- Must have compatible mounting type

## Test Patterns
When testing HiroseHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new HiroseHandler()`

## Known Handler Issues
*All issues fixed in PR #88*
