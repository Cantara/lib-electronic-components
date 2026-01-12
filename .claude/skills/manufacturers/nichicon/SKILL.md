# Nichicon Manufacturer Handler Skill

## Overview
NichiconHandler manages Nichicon components including aluminum electrolytic capacitors, polymer capacitors, and supercapacitors (EDLC).

## Supported Component Types
- CAPACITOR
- CAPACITOR_ELECTROLYTIC_NICHICON
- CAPACITOR_FILM_NICHICON
- SUPERCAP_NICHICON

## MPN Patterns

### Standard Grade
| Prefix | Description |
|--------|-------------|
| UUDx | Standard grade |
| UUEx | Standard grade, higher voltage |

### High Temperature
| Prefix | Description |
|--------|-------------|
| UHSx | 125C high temp |
| UHEx | 135C high temp |
| UHWx | 105C high temp |

### Long Life
| Prefix | Description |
|--------|-------------|
| UESx | Long life, standard grade |
| UEWx | Long life, high ripple |
| UKLx | Extra long life |

### Low Impedance
| Prefix | Description |
|--------|-------------|
| UPWx | Low impedance |
| UPSx | Ultra low impedance |

### Polymer
| Prefix | Description |
|--------|-------------|
| PCJx | Polymer, standard |
| PCSx | Polymer, low profile |
| PCRx | Polymer, high reliability |

### Miniature
| Prefix | Description |
|--------|-------------|
| UMAx | Miniature, general purpose |
| UMDx | Miniature, high temp |

### Photo Flash
| Prefix | Description |
|--------|-------------|
| PFx | Photo flash capacitors |

### Super Capacitors (EDLC)
| Prefix | Description |
|--------|-------------|
| JJDx | Standard EDLC |
| JJSx | High power EDLC |

## Package Code Extraction
Extracted from case code in MPN:
- For U/PC series: 4th or 5th character indicates diameter
  - 04 = 4x7mm
  - 05 = 5x7mm
  - 06 = 6.3x7mm
  - 08 = 8x10mm
  - 10 = 10x12.5mm
  - 12 = 12.5x15mm
  - 16 = 16x15mm

## Series Extraction
Returns human-readable series names:
- "Standard Grade", "Standard High Voltage"
- "125C High Temp", "135C High Temp", "105C High Temp"
- "Long Life Standard", "Long Life High Ripple", "Extra Long Life"
- "Low Impedance", "Ultra Low Impedance"
- "Polymer Standard", "Polymer Low Profile", "Polymer High Reliability"
- "Miniature General", "Miniature High Temp"
- "Photo Flash"
- "Standard EDLC", "High Power EDLC"

## Replacement Logic
- Must be same series and case size
- Higher temperature rating can replace lower:
  - 135C -> 125C -> 105C
- Long life series can replace standard series
- Same voltage rating required

## Test Patterns
When testing NichiconHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new NichiconHandler()`

## Known Handler Issues
*All issues fixed in PR #86*
