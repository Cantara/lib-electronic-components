# Qorvo Manufacturer Handler Skill

## Overview
QorvoHandler manages Qorvo RF components including power amplifiers, switches, filters, front-end modules, LNAs, phase shifters, and mixers.

## Supported Component Types
- RF_IC_QORVO

## MPN Patterns

### RF Power Amplifiers
| Prefix | Description |
|--------|-------------|
| QPAxxxx | Power amplifiers |
| TQPxxxx | Power amplifiers (TriQuint legacy) |
| RFxxxx | RF amplifiers |

### RF Switches
| Prefix | Description |
|--------|-------------|
| QPCxxxx | RF switches |
| RFSWxxxx | RF switches |
| PE4xxxx | RF switches (TriQuint legacy) |

### RF Filters
| Prefix | Description |
|--------|-------------|
| 885xxx | RF filters |
| 854xxx | RF filters |
| TQMxxx | RF filters |

### Front-End Modules
| Prefix | Description |
|--------|-------------|
| RF5xxx | Front-end modules |
| RFFxxxx | Front-end modules |
| QPFxxxx | Front-end modules |

### LNAs (Low Noise Amplifiers)
| Prefix | Description |
|--------|-------------|
| QPLxxxx | LNAs |
| TQP3xxx | LNAs |
| SPFxxxx | LNAs |

### Phase Shifters & Mixers
| Prefix | Description |
|--------|-------------|
| QPSxxxx | Phase shifters |
| RFPSxxxx | Phase shifters |
| QPMxxxx | Mixers |
| TQMxxxx | Mixers (4-digit) |

## Package Codes
| Suffix | Package |
|--------|---------|
| TR1 | QFN Tape & Reel |
| BU | WLCSP |
| EN | DFN |
| GM | MCM (Multi-Chip Module) |
| ML | QFN Standard |
| CS | CSP |
| LAM | Laminate Package |

## Series Extraction
Returns prefix letters + first 4 digits (e.g., "PE41234-GM" returns "PE4123").

## Replacement Logic
- Same series or compatible series (QPA/TQP, QPC/RFSW, RF5/RFF/QPF)
- Compatible frequency band
- Compatible power rating (higher can replace lower)
- Compatible gain (±3dB tolerance)

## Test Patterns
When testing QorvoHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new QorvoHandler()`

## Known Handler Issues
*All issues fixed in PR #88*
- Series extraction takes only 4 digits after prefix

## Common Part Numbers
| Part Number | Description |
|-------------|-------------|
| QPA2305-TR1 | 2W Power Amplifier, QFN |
| QPF4528-TR1 | Wi-Fi 6 Front-End Module |
| TQP3M9040 | LNA, 0.5-6GHz |
| PE42724-GM | SPDT RF Switch |
