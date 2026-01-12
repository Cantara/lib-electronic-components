# Molex Manufacturer Handler Skill

## Overview
MolexHandler manages Molex connectors including Micro-Fit, Mini-Fit Jr., PicoBlade, KK 254, PicoClasp, Nano-Fit, and Micro-Lock Plus series.

## Supported Component Types
- CONNECTOR
- CONNECTOR_MOLEX

## MPN Patterns

### Micro-Fit 3.0 Series (3.0mm pitch, 5A)
| Prefix | Description |
|--------|-------------|
| 43045-xxxx | Micro-Fit 3.0 headers |
| 43046-xxxx | Micro-Fit 3.0 receptacles |

### Mini-Fit Jr. Series (4.2mm pitch, 9A)
| Prefix | Description |
|--------|-------------|
| 39281-xxxx | Mini-Fit Jr. headers |
| 39282-xxxx | Mini-Fit Jr. receptacles |

### PicoBlade Series (1.25mm pitch, 1A)
| Prefix | Description |
|--------|-------------|
| 53261-xxxx | PicoBlade headers |
| 53262-xxxx | PicoBlade receptacles |

### KK 254 Series (2.54mm pitch, 3A)
| Prefix | Description |
|--------|-------------|
| 22057-xxxx | KK 254 headers |
| 22058-xxxx | KK 254 receptacles |

### Additional Series
| Prefix | Description | Pitch | Current |
|--------|-------------|-------|---------|
| 51021-xxxx | PicoClasp | 1.0mm | 1A |
| 51047-xxxx | Nano-Fit | 2.5mm | 3.5A |
| 87832-xxxx | Micro-Lock Plus | 2.0mm | 3A |

## MPN Structure
Format: `XXXXX-YYYY`
- XXXXX = 5-digit series code
- YYYY = 4-digit package code (first 2 digits = pin count, last 2 = mounting)

## Package Code Extraction
Returns 4-digit code after dash (e.g., "43045-0802" returns "0802").

## Series Extraction
Returns 5-digit series code (e.g., "43045-0802" returns "43045").

## Helper Methods
- `getPitch(mpn)` - Returns pitch in mm
- `getMountingType(mpn)` - Returns THT, SMT, or Other
- `getOrientation(mpn)` - Returns Vertical, Right Angle, or Right Angle Reversed
- `getRatedCurrent(mpn)` - Returns rated current in Amps
- `isKeyed(mpn)` - Returns true for most series (false for KK series)
- `getGender(mpn)` - Returns Male (odd series) or Female (even series)
- `hasLatchingMechanism(mpn)` - Returns true for most series

## Mounting Type Codes
- Ends with 01, 02 = Through-Hole
- Ends with 10, 11 = Surface Mount

## Replacement Logic
- Must be from same connector family
- Must have same pin count
- Must have compatible mounting type (THT/THT or SMT/SMT)

## Test Patterns
When testing MolexHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new MolexHandler()`

## Known Handler Issues
*All issues fixed in PR #88*
