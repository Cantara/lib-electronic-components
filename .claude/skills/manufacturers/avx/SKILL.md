# AVX Manufacturer Handler Skill

## Overview
AVXHandler manages AVX Corporation components including tantalum capacitors, ceramic capacitors, film capacitors, supercapacitors, and RF components.

## Supported Component Types
- CAPACITOR
- CAPACITOR_TANTALUM_AVX
- CAPACITOR_CERAMIC_AVX
- CAPACITOR_FILM_AVX
- CAPACITOR_POLYMER_AVX
- SUPERCAP_AVX
- FILTER_AVX
- IC

## MPN Patterns

### Tantalum Capacitors
| Prefix | Description |
|--------|-------------|
| TAJx | Standard MnO2 |
| TPSx | Polymer |
| TPMx | Multianode polymer |
| TRJx | High reliability |
| TCJx | Commercial grade |

### Ceramic Capacitors
| Prefix | Description |
|--------|-------------|
| 08xx | Standard MLCC |
| 12xx | High voltage MLCC |
| 21xx | RF/Microwave |
| 25xx | Medical grade |
| MLxx | Low inductance |

### Film Capacitors
| Prefix | Description |
|--------|-------------|
| FAxx | Stacked film |
| FBxx | Box film |
| FPxx | Pulse film |

### SuperCapacitors
| Prefix | Description |
|--------|-------------|
| SCCxx | Cylindrical |
| SCMxx | Module type |

### RF Components
| Prefix | Description |
|--------|-------------|
| BPxx | Band pass filters |
| LPxx | Low pass filters |
| HPxx | High pass filters |
| MLOxx | RF inductors |

## Package Code Extraction

### Tantalum Case Codes
| Code | EIA Size | Case |
|------|----------|------|
| A | 3216-12 | Case A |
| B | 3528-12 | Case B |
| C | 6032-15 | Case C |
| D | 7343-20 | Case D |
| E | 7343-31 | Case E |
| V | 7343-43 | Case V |
| W | 7360-38 | Case W |
| X | 7361-43 | Case X |
| Y | 7361-43 | Case Y |

### Ceramic Sizes (from position 2-3)
| Code | Imperial/Metric |
|------|----------------|
| 05 | 0402/1005M |
| 06 | 0603/1608M |
| 08 | 0805/2012M |
| 12 | 1206/3216M |
| 21 | 2220/5750M |

## Series Extraction
Returns human-readable series names:
- "Standard MnO2", "Polymer", "Multianode Polymer", "High Reliability", "Commercial Grade"
- "Standard MLCC", "High Voltage MLCC", "RF/Microwave", "Medical Grade", "Low Inductance"
- "Stacked Film", "Box Film", "Pulse Film"
- "Cylindrical SuperCap", "Module SuperCap"
- "Band Pass Filter", "Low Pass Filter", "High Pass Filter", "RF Inductor"

## Replacement Logic
- Same series and case size required
- TPS (polymer) can replace TAJ (MnO2) in same case size
- TRJ (high reliability) can replace TAJ (standard)
- Ceramic: Same size and voltage rating required

## Test Patterns
When testing AVXHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new AVXHandler()`

## Known Handler Issues
- Uses HashSet in getSupportedTypes() - should use Set.of()
- Ceramic package extraction reads positions 2-3 after 08/12 prefix (e.g., "0805YC104KAT2A" extracts "05" which maps to "0402/1005M", not "0805")
