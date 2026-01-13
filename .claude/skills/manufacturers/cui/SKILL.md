# CUI Devices Manufacturer Handler Skill

## Overview
CUIHandler manages CUI Devices components including audio connectors, power jacks, buzzers, speakers, and encoders.

## Supported Component Types
- CONNECTOR
- AUDIO_JACK
- SPEAKER
- BUZZER
- ENCODER

## MPN Patterns

### SJ Series (3.5mm Audio Jacks)
| Pattern | Description |
|---------|-------------|
| SJ1-3523N | 3.5mm audio jack, vertical, SPST |
| SJ1-3525N | 3.5mm audio jack, with switch |
| SJ2-xxxx | Alternative audio jack series |

### PJ Series (DC Power Jacks)
| Pattern | Description |
|---------|-------------|
| PJ-002A | DC power jack, 2.0mm |
| PJ-102A | DC power jack, 2.5mm |
| PJ-xxxAH | Horizontal mount variant |

### CMI Series (Magnetic Buzzers)
| Pattern | Description |
|---------|-------------|
| CMI-SIZE-IMPEDANCE | SIZE = diameter in mm (first 2 digits), IMPEDANCE = ohms |
| CMI-1295-85T | 12mm, 85 ohms, terminal type |
| CMI-0905-45T | 9mm, 45 ohms, terminal type |

### CMS Series (Speakers)
| Pattern | Description |
|---------|-------------|
| CMS-SIZE-IMPEDANCE | SIZE = diameter (first 2 digits), IMPEDANCE = ohms |
| CMS-15118-78P | 15mm, 78 ohms, PCB mount |
| CMS-2011-45P | 20mm, 45 ohms, PCB mount |

### CPE/CPT Series (Piezo Buzzers/Transducers)
| Pattern | Description |
|---------|-------------|
| CPE-825P | Piezo buzzer |
| CPT-2014-65 | Piezo transducer |

### AMT Series (Modular Encoders)
| Pattern | Description |
|---------|-------------|
| AMT102-V | Incremental encoder, 102 PPR, vertical |
| AMT103-V | Incremental encoder, 103 series |
| AMT10E-V | Encoder variant |
| AMT112Q-V | Encoder with quadrature output |

### ACZ Series (Rotary Encoders)
| Pattern | Description |
|---------|-------------|
| ACZ11BR1E-20FA1-24C | Rotary encoder with switch |
| ACZ16NBRD-15ETG | 16mm rotary encoder |

## Package Code Extraction
- SJ series: Returns suffix letter(s) (N, NR, NC, etc.)
- PJ series: Returns suffix letter(s) (A, AH, B, etc.)
- CMI/CMS: Returns last letter (T for terminal, P for PCB)
- AMT series: Returns suffix after dash (V for vertical)

## Series Extraction
| MPN | Extracted Series |
|-----|-----------------|
| SJ1-3523N | SJ1 |
| PJ-002A | PJ |
| CMI-1295-85T | CMI |
| CMS-15118-78P | CMS |
| AMT102-V | AMT102 |
| ACZ11BR1E | ACZ11 |

## Helper Methods
- `isAudioConnector(mpn)` - Returns true for SJ series
- `isPowerConnector(mpn)` - Returns true for PJ series
- `isBuzzer(mpn)` - Returns true for CMI, CPE, CPT series
- `isSpeaker(mpn)` - Returns true for CMS series
- `isEncoder(mpn)` - Returns true for AMT, ACZ series
- `getProductFamily(mpn)` - Returns family description
- `getMountingType(mpn)` - Returns mounting type (Vertical, SMT, etc.)
- `getTransducerSizeMm(mpn)` - Returns diameter in mm for buzzers/speakers
- `getImpedanceOhms(mpn)` - Returns impedance in ohms for buzzers/speakers

## Replacement Logic
- Audio jacks: Same connector number (middle section) required
- Encoders: Same series including resolution number required
- Transducers: Same size AND impedance required
- Cross-series replacement not allowed

## Test Patterns
When testing CUIHandler:
1. Use assertions for `matches()` with CONNECTOR, AUDIO_JACK, SPEAKER, BUZZER, ENCODER types
2. Use assertions for `extractPackageCode()`, `extractSeries()`
3. Test helper methods for classification
4. Instantiate directly: `new CUIHandler()`

## Real-World MPN Examples
| MPN | Type | Description |
|-----|------|-------------|
| SJ1-3523N | Audio Jack | 3.5mm stereo jack, vertical PCB |
| SJ1-3525N | Audio Jack | 3.5mm jack with switch |
| PJ-002A | Power Jack | DC barrel jack, 2.0mm ID |
| CMS-15118-78P | Speaker | 15mm speaker, 8 ohm |
| CMI-1295-85T | Buzzer | 12mm magnetic buzzer |
| AMT102-V | Encoder | Modular incremental encoder |

## Known Handler Issues
*No known issues - handler created fresh*

## Learnings & Quirks

### MPN Format Patterns
- SJ series uses format: SJx-xxxxY where x=series, xxxx=type code, Y=mount suffix
- CMI/CMS series encode size in first 2 digits of first number group
- AMT series embeds resolution in series number (AMT102 = 102 PPR base)
- ACZ series numbers indicate mechanical size

### Package Code Meanings
| Code | Meaning |
|------|---------|
| N | Vertical/PCB mount |
| NR | Vertical with nut/retention |
| A | Surface mount |
| AH | Surface mount horizontal |
| T | Terminal type |
| P | PCB mount |
| V | Vertical |
| H | Horizontal |

<!-- Add new learnings above this line -->
