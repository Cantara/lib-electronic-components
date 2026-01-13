---
name: cirruslogic
description: Cirrus Logic MPN encoding patterns, suffix decoding, and handler guidance. Use when working with Cirrus Logic audio ICs, Wolfson audio products, or CirrusLogicHandler.
---

# Cirrus Logic Manufacturer Skill

## Overview

Cirrus Logic is a leading supplier of high-precision analog and digital signal processing components for audio and voice signal processing applications. Key product areas include:

- **Audio ADCs** (Analog-to-Digital Converters)
- **Audio DACs** (Digital-to-Analog Converters)
- **Audio CODECs** (Combined ADC + DAC)
- **Digital Audio Interfaces**
- **DSP Audio Processors**
- **Amplifiers**

**Note**: Cirrus Logic acquired **Wolfson Microelectronics** in 2014. Wolfson WM8xxx series products are now part of the Cirrus Logic portfolio.

---

## MPN Structure

### Cirrus Logic CS Series

```
CS [SERIES] [PART] [-PACKAGE] [QUALIFIER]
│    │        │        │          │
│    │        │        │          └── Optional: R=Tape/Reel
│    │        │        └── Package code (CZZ, CNZ, DZZ, etc.)
│    │        └── Part number within series (digits/letters)
│    └── Series number (42=ADC, 43=DAC/CODEC, 47=DSP, etc.)
└── Cirrus prefix
```

### Example Decoding (CS Series)

```
CS4344-CZZ
│  │   │
│  │   └── CZZ = TSSOP package
│  └── 4344 = Stereo DAC
└── CS = Cirrus Logic prefix

CS43L22-CNZ
│  │    │
│  │    └── CNZ = QFN package
│  └── 43L22 = Low-power stereo DAC/headphone amp
└── CS = Cirrus Logic prefix

CS8416-DZZ
│  │   │
│  │   └── DZZ = SSOP package
│  └── 8416 = Digital Audio Interface Receiver
└── CS = Cirrus Logic prefix
```

### Wolfson WM8xxx Series

```
WM8 [SERIES] [PACKAGE]
│     │         │
│     │         └── Package suffix (SEDS, CGEFL, etc.)
│     └── Series number (731=basic codec, 960=advanced, 994=flagship)
└── Wolfson prefix (now Cirrus Logic)
```

### Example Decoding (Wolfson Series)

```
WM8731SEDS
│   │   │
│   │   └── SEDS = SOIC package
│   └── 8731 = Low-power stereo codec
└── WM8 = Wolfson prefix

WM8960CGEFL
│   │    │
│   │    └── CGEFL = QFN with exposed pad
│   └── 8960 = Low-power stereo codec (advanced)
└── WM8 = Wolfson prefix
```

---

## Product Series Reference

### CS42xx - Audio ADCs

| Part | Description | Resolution | Channels |
|------|-------------|------------|----------|
| CS4270 | 24-bit Audio Codec | 24-bit | 2 |
| CS4271 | 24-bit Audio Codec | 24-bit | 2 |
| CS4272 | 24-bit Audio Codec | 24-bit | 2 |

### CS43xx - Audio DACs/CODECs

| Part | Description | Resolution | Notes |
|------|-------------|------------|-------|
| CS4334 | Stereo DAC | 24-bit | Low cost |
| CS4340 | Stereo DAC | 24-bit | Automotive |
| CS4344 | Stereo DAC | 24-bit | Popular choice |
| CS43L22 | Stereo DAC + HP Amp | 24-bit | Low-power, headphone amplifier |

### CS47xx - DSP Audio

| Part | Description | Notes |
|------|-------------|-------|
| CS47L24 | Smart Codec | Low-power always-on |
| CS47L35 | Smart Codec | Voice wake, noise suppression |

### CS48xx - SoundClear DSP

| Part | Description | Notes |
|------|-------------|-------|
| CS4860x | SoundClear DSP | Audio processing algorithms |

### CS53xx - LED Drivers

| Part | Description | Notes |
|------|-------------|-------|
| CS5361 | LED Driver | 8-channel |

### CS84xx - Digital Audio Interface

| Part | Description | Notes |
|------|-------------|-------|
| CS8416 | S/PDIF Receiver | 192kHz, 24-bit |
| CS8422 | S/PDIF Receiver | Sample rate converter |

### WM8xxx - Wolfson Audio (Acquired)

| Part | Description | Notes |
|------|-------------|-------|
| WM8731 | Stereo Codec | Low-power, portable audio |
| WM8960 | Stereo Codec | Integrated headphone amp |
| WM8994 | Multi-channel Codec | Flagship smartphone codec |

---

## Package Codes

### CS Series Package Codes

| Code | Package | Notes |
|------|---------|-------|
| CZZ | TSSOP | Common for DACs |
| CZZR | TSSOP | Tape and reel |
| CNZ | QFN | Compact, good thermal |
| CNZR | QFN | Tape and reel |
| DZZ | SSOP | Wider body than TSSOP |
| DZZR | SSOP | Tape and reel |

### Package Code Patterns (CS Series)

| First Letter | Meaning |
|--------------|---------|
| C | QFN or TSSOP family |
| D | SSOP family |

| Second Letter | Meaning |
|---------------|---------|
| N (after C) | QFN |
| Z (after C) | TSSOP |
| Z (after D) | SSOP |

### Wolfson WM8xxx Package Codes

| Code | Package | Notes |
|------|---------|-------|
| SEDS | SOIC | Standard SMD |
| SEDS/V | SOIC | Lead-free variant |
| CGEFL | QFN | Exposed pad, good thermal |
| CGEFL/V | QFN | Lead-free variant |
| CLQVP | LQFP | Larger pin count |
| CLQVP/V | LQFP | Lead-free variant |
| CSBVP | WLCSP | Wafer-level CSP |
| CSBVP/V | WLCSP | Lead-free variant |
| EFL | QFN | QFN variant |
| EDS | SOIC | SOIC variant |
| GEFL | QFN | QFN variant |

### Wolfson Package Code Patterns

| Contains | Decoded Package |
|----------|-----------------|
| FL | QFN |
| DS | SOIC |
| QV, QFP | LQFP |
| CSB, CSP | WLCSP |

**Note**: The `/V` suffix indicates a lead-free (RoHS compliant) variant.

---

## Supported Component Types

The `CirrusLogicHandler` supports:

| ComponentType | Description |
|---------------|-------------|
| `IC` | All Cirrus Logic audio ICs |

**Note**: The handler registers all patterns under `ComponentType.IC` since Cirrus Logic's product portfolio is exclusively audio/analog ICs.

---

## Handler Implementation Notes

### Package Code Extraction

```java
// CS series: Extract suffix after hyphen
// CS4344-CZZ → suffix="CZZ" → TSSOP
int dashIndex = mpn.indexOf('-');
if (dashIndex > 0) {
    String suffix = mpn.substring(dashIndex + 1);
    return decodePackageCode(suffix);
}

// CS series without hyphen: Find trailing letter suffix
// CS43L22CNZ → extract "CNZ" → QFN

// WM8xxx series: No hyphen, suffix follows part number
// WM8731SEDS → extract "SEDS" after "8731" → SOIC
// WM8960CGEFL → extract "CGEFL" after "8960" → QFN
```

### Series Extraction

```java
// CS series: First 4 characters
// CS4344-CZZ → "CS43"
// CS8416-DZZ → "CS84"

// WM8xxx series: First 4 characters
// WM8731SEDS → "WM87"
// WM8960CGEFL → "WM89"
```

### Pattern Matching

```java
// CS series patterns (all register under IC)
"^CS42[0-9]{2}.*"        // Audio ADCs (CS4270, CS4272)
"^CS43[0-9A-Z]{2,4}.*"   // Audio DACs/CODECs (CS4344, CS43L22)
"^CS47[0-9A-Z]{2,4}.*"   // DSP Audio (CS47L24, CS47L35)
"^CS48[0-9]{2,3}.*"      // SoundClear DSP
"^CS53[0-9]{2}.*"        // LED Drivers
"^CS84[0-9]{2}.*"        // Digital Audio Interface

// Wolfson patterns
"^WM8[0-9]{3}.*"         // WM8xxx series (WM8731, WM8960)
"^WM89[0-9]{2}.*"        // WM89xx series (more specific)
```

---

## Replacement Compatibility

The handler implements `isOfficialReplacement()` with these rules:

### Same Base Part Number
Parts with the same base part number but different packages are compatible:
- `CS4344-CZZ` and `CS4344-CNZ` (same part, TSSOP vs QFN)
- `WM8731SEDS` and `WM8731CGEFL` (same part, SOIC vs QFN)

### Series Compatibility
Parts must have the same base part number to be considered compatible. Different part numbers within the same series (e.g., CS4334 vs CS4344) are NOT automatically marked as compatible because they may have different specifications.

---

## Example MPNs

### CS Series Examples

| MPN | Series | Package | Description |
|-----|--------|---------|-------------|
| CS4344-CZZ | CS43 | TSSOP | Stereo DAC |
| CS4344-CZZR | CS43 | TSSOP | Stereo DAC, tape and reel |
| CS4344-CNZ | CS43 | QFN | Stereo DAC |
| CS43L22-CNZ | CS43 | QFN | Low-power stereo DAC with headphone amp |
| CS8416-DZZ | CS84 | SSOP | S/PDIF receiver |
| CS4270-CZZ | CS42 | TSSOP | Audio codec |
| CS47L24-CWZF | CS47 | - | Smart codec (suffix decoded from pattern) |

### Wolfson WM8xxx Examples

| MPN | Series | Package | Description |
|-----|--------|---------|-------------|
| WM8731SEDS | WM87 | SOIC | Low-power stereo codec |
| WM8731SEDS/V | WM87 | SOIC | Lead-free variant |
| WM8731CGEFL | WM87 | QFN | QFN package variant |
| WM8960CGEFL | WM89 | QFN | Advanced stereo codec |
| WM8960CGEFL/V | WM89 | QFN | Lead-free variant |
| WM8994CLQVP | WM89 | LQFP | Multi-channel smartphone codec |

---

## Related Files

- Handler: `manufacturers/CirrusLogicHandler.java`
- Component types: `IC`
- Manufacturer enum: `ComponentManufacturer.CIRRUS_LOGIC`

---

## Learnings & Quirks

### Wolfson Acquisition
- Cirrus Logic acquired Wolfson Microelectronics in 2014
- WM8xxx series products are still manufactured under the original part numbers
- Both CS and WM prefixes are valid Cirrus Logic parts

### Package Code Extraction
- CS series uses hyphen-separated suffixes (CS4344-CZZ)
- Some CS parts may have package code embedded without hyphen
- WM8xxx series never uses hyphen - package immediately follows part number
- Wolfson `/V` suffix indicates lead-free (RoHS) variant

### Series Numbering
- CS42xx = Audio ADCs
- CS43xx = Audio DACs and CODECs (largest product family)
- CS47xx = DSP Audio (Smart Codecs)
- CS48xx = SoundClear DSP
- CS53xx = LED Drivers (not audio, but same prefix convention)
- CS84xx = Digital Audio Interface (S/PDIF, I2S)

### Pattern Overlap Prevention
- WM8xxx pattern `^WM8[0-9]{3}.*` is specific enough to avoid conflicts
- CS patterns use two-digit series identifiers (42, 43, 47, etc.)
- The CS43 pattern `^CS43[0-9A-Z]{2,4}.*` handles both CS4344 and CS43L22

### Part Number Variants
- CS43Lxx parts (like CS43L22) include "L" for low-power variants
- These are handled by the pattern `[0-9A-Z]{2,4}` which allows letters

### Handler Only Uses IC Type
- Unlike handlers for manufacturers with diverse products, CirrusLogicHandler only supports `ComponentType.IC`
- This is appropriate since Cirrus Logic exclusively manufactures audio/analog ICs

<!-- Add new learnings above this line -->
