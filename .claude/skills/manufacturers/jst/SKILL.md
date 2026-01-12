# JST Manufacturer Handler Skill

## Overview
JSTHandler manages JST connectors including PH, XH, SH, GH, ZH, and EH series.

## Supported Component Types
- CONNECTOR
- CONNECTOR_JST

## MPN Patterns

### PH Series (2.0mm pitch, 3A)
| Pattern | Description |
|---------|-------------|
| PH-x | Header (male) |
| PHR-x | Housing (female) |
| PHS-x | SMT variant |
| PHD-x | Double row |
| PHL-x | Locking variant |

### XH Series (2.5mm pitch, 3A)
| Pattern | Description |
|---------|-------------|
| XH-x | Header (male) |
| XHR-x | Housing (female) |
| XHS-x | SMT variant |
| XHD-x | Double row |
| XHL-x | Locking variant |

### SH Series (1.0mm pitch, 1A)
| Pattern | Description |
|---------|-------------|
| SH-x | Header (male) |
| SHR-x | Housing (female) |
| SHS-x | SMT variant |
| SHD-x | Double row |

### GH Series (1.25mm pitch, 1A)
| Pattern | Description |
|---------|-------------|
| GH-x | Header (male) |
| GHR-x | Housing (female) |
| GHS-x | SMT variant |
| GHD-x | Double row |

### Additional Series
| Series | Pitch | Current |
|--------|-------|---------|
| ZH | 1.5mm | 1A |
| EH | 2.5mm | 3A |

## Package Code Extraction
Returns suffix after pin count (e.g., "PH-4-V" returns "V").
- V = Vertical THT
- R = Right Angle THT
- VS/S = Vertical SMT
- RS = Right Angle SMT

## Series Extraction
Returns series prefix (e.g., "PH", "XH", "SH", "GH", "ZH", "EH").

## Helper Methods
- `getPitch(mpn)` - Returns pitch in mm
- `getMountingType(mpn)` - Returns THT or SMT
- `getOrientation(mpn)` - Returns Vertical or Right Angle
- `getRatedCurrent(mpn)` - Returns rated current in Amps
- `isKeyed(mpn)` - Always returns true (all JST connectors are keyed)
- `getGender(mpn)` - Returns Male (header) or Female (housing with R suffix)
- `getColor(mpn)` - Returns White, Black, Red, or Natural based on suffix

## Replacement Logic
- Must be from same series
- Must have same pin count
- Must have compatible mounting type (THT/THT or SMT/SMT)
- Must have compatible orientation (Vertical/Vertical or Right Angle/Right Angle)

## Test Patterns
When testing JSTHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new JSTHandler()`

## Known Handler Issues
*All issues fixed in PR #87*
