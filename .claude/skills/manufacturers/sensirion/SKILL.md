---
name: sensirion
description: Sensirion environmental sensor MPN encoding patterns, suffix decoding, and handler guidance. Use when working with Sensirion humidity, temperature, gas, CO2, particulate, flow, or pressure sensors.
---

# Sensirion Manufacturer Skill

## Manufacturer Overview

Sensirion AG is a Swiss sensor manufacturer specializing in environmental sensors. Their product families include:

| Family | Products | Application |
|--------|----------|-------------|
| **SHTxx** | SHT30, SHT31, SHT35, SHT40, SHT41, SHT45, SHT85 | Humidity & Temperature |
| **STSxx** | STS30, STS31, STS40 | Temperature only |
| **SGPxx** | SGP30, SGP40, SGP41 | VOC/Gas (Air Quality) |
| **SCDxx** | SCD30, SCD40, SCD41 | CO2 concentration |
| **SFAxx** | SFA30 | Formaldehyde detection |
| **SPSxx** | SPS30 | Particulate matter (PM1.0/2.5/10) |
| **SLFxxxx** | SLF3S-1300F | Liquid flow measurement |
| **SDPxxx** | SDP31, SDP810 | Differential pressure |

All Sensirion sensors use **I2C** digital interface (some variants support alternate addresses).

---

## MPN Structure

Sensirion MPNs follow this general structure:

```
[PREFIX][SERIES]-[VARIANT][GRADE]-[REEL]
   │       │        │        │       │
   │       │        │        │       └── Reel option: R2, R4 (tape & reel qty)
   │       │        │        └── Accuracy grade: A, B, F
   │       │        └── Variant code: DIS, AD1B, D, P, etc.
   │       └── Series number (30, 31, 40, 41, 45, etc.)
   └── Family prefix (SHT, STS, SGP, SCD, SFA, SPS, SLF, SDP)
```

### Example Decoding

```
SHT31-DIS-B
│  │  │   │
│  │  │   └── B = Accuracy grade B
│  │  └── DIS = Digital sensor variant
│  └── 31 = Series 31 (mid-range accuracy)
└── SHT = Humidity & Temperature sensor

SHT40-AD1B-R2
│  │  │  │ │
│  │  │  │ └── R2 = Tape & reel (2000 pcs)
│  │  │  └── B = Accuracy grade B
│  │  └── AD1 = Alternate I2C address variant
│  └── 40 = Series 40 (4th generation)
└── SHT = Humidity & Temperature sensor

SGP40-D-R4
│  │  │ │
│  │  │ └── R4 = Tape & reel (4000 pcs)
│  │  └── D = Standard digital variant
│  └── 40 = Series 40
└── SGP = Gas sensor (VOC)

SLF3S-1300F
│  │  │   │
│  │  │   └── F = Variant identifier
│  │  └── 1300 = Flow range identifier
│  └── 3S = Series/size
└── SLF = Liquid Flow sensor
```

---

## Variant Codes

### Package/Variant Codes

| Code | Description | Package |
|------|-------------|---------|
| DIS | Digital sensor (standard) | DFN |
| DIS-B | Digital sensor, B grade | DFN |
| ARP | ARP variant | DFN |
| AD1B | Alternate I2C address, B grade | DFN |
| AD1F | Alternate I2C address, F grade | DFN |
| BD1B | B variant, alternate address | DFN |
| D | Standard digital | DFN |
| P | Pin header (development kit) | PIN |
| B | B accuracy grade | DFN |
| F | F accuracy grade | DFN |

### Reel Options

| Code | Description |
|------|-------------|
| R2 | Tape & reel, 2000 pieces |
| R4 | Tape & reel, 4000 pieces |

---

## Accuracy Grades

Sensirion sensors come in different accuracy grades. Higher accuracy = tighter tolerance.

| Grade | Accuracy Level | Typical RH Tolerance | Use Case |
|-------|---------------|---------------------|----------|
| A | Highest | +/- 1.5% RH | Precision applications |
| B | Mid-range | +/- 2.0% RH | General purpose |
| F | Standard | +/- 2.5% RH | Cost-sensitive |

**Grade Ordering (best to worst):** A > B > F

**Compatibility Rule:** Higher accuracy grades can replace lower grades (A can replace B, B can replace F).

---

## Series Families

### SHT Humidity & Temperature Sensors

| Series | Generation | Key Features |
|--------|------------|--------------|
| SHT3x | 3rd gen | SHT30/31/35 - established, wide use |
| SHT4x | 4th gen | SHT40/41/45 - smaller, lower power |
| SHT8x | Premium | SHT85 - high accuracy, PTFE filter |

**Compatibility:** SHT4x can replace SHT3x (next generation, same function).

**Within series:** SHT35 > SHT31 > SHT30 (accuracy ordering)

### STS Temperature-Only Sensors

| Series | Generation | Notes |
|--------|------------|-------|
| STS3x | 3rd gen | STS30/31 - temperature only |
| STS4x | 4th gen | STS40 - smaller footprint |

**Compatibility:** STS4x can replace STS3x.

### SGP Gas Sensors (VOC)

| Series | Generation | Key Features |
|--------|------------|--------------|
| SGP3x | 3rd gen | SGP30 - multi-pixel gas sensor |
| SGP4x | 4th gen | SGP40/41 - improved, lower power |

**Compatibility:** SGP4x can replace SGP3x (improved performance).

### SCD CO2 Sensors

| Series | Generation | Key Features |
|--------|------------|--------------|
| SCD3x | NDIR based | SCD30 - photoacoustic, larger size |
| SCD4x | Miniaturized | SCD40/41 - smaller, breakout board compatible |

**Compatibility:** SCD4x can replace SCD3x (miniaturized, same accuracy).

### SDP Differential Pressure Sensors

| Series | Pressure Range | Notes |
|--------|---------------|-------|
| SDP3x | +/- 500 Pa | SDP31 - low range |
| SDP8x | +/- 500/1000 Pa | SDP810 - extended range |

**Not interchangeable** - different pressure ranges require design consideration.

---

## Supported Component Types

From `SensirionHandler.getSupportedTypes()`:

```java
Set.of(
    ComponentType.SENSOR,              // Generic sensor
    ComponentType.HUMIDITY_SENSOR,     // SHTxx, humidity measurement
    ComponentType.TEMPERATURE_SENSOR,  // SHTxx, STSxx temperature
    ComponentType.PRESSURE_SENSOR,     // SDPxx differential pressure
    ComponentType.SENSOR_FLOW,         // SLFxx liquid flow
    ComponentType.IC                   // SGPxx, SCDxx (complex sensors)
)
```

### Pattern Registration

| Component Type | Pattern | Products |
|----------------|---------|----------|
| `HUMIDITY_SENSOR` | `^SHT[0-9]{2}.*` | SHT30, SHT31, SHT40, etc. |
| `TEMPERATURE_SENSOR` | `^SHT[0-9]{2}.*`, `^STS[0-9]{2}.*` | SHTxx, STSxx |
| `SENSOR` (gas) | `^SGP[0-9]{2}.*` | SGP30, SGP40, SGP41 |
| `SENSOR` (CO2) | `^SCD[0-9]{2}.*` | SCD30, SCD40, SCD41 |
| `SENSOR` (formaldehyde) | `^SFA[0-9]{2}.*` | SFA30 |
| `SENSOR` (particulate) | `^SPS[0-9]{2}.*` | SPS30 |
| `SENSOR_FLOW` | `^SLF[0-9A-Z]+.*` | SLF3S-1300F |
| `PRESSURE_SENSOR` | `^SDP[0-9]{2,3}.*` | SDP31, SDP810 |

---

## Package Code Extraction

The handler extracts package codes from the variant portion after the first hyphen:

```java
// Input: SHT31-DIS-B
// After first dash: "DIS-B"
// Package part: "DIS" (before second dash)
// Returns: "DFN" (mapped)

// Input: SHT40-AD1B-R2
// After first dash: "AD1B-R2"
// Package part: "AD1B"
// Returns: "DFN"
```

### Package Code Mappings

| Variant Code | Mapped Package |
|--------------|----------------|
| DIS | DFN |
| DIS-B | DFN |
| ARP | DFN |
| AD1B | DFN |
| AD1F | DFN |
| BD1B | DFN |
| D | DFN |
| P | PIN (development) |
| B | DFN |
| F | DFN |

**Note:** Most Sensirion sensors use DFN (Dual Flat No-lead) packaging.

---

## Series Extraction

The handler extracts series by grouping based on prefix patterns:

```java
// Input: SHT31-DIS-B
// Base part: "SHT31" (before first dash)
// Starts with "SHT3" → Returns: "SHT3x"

// Input: SCD40-D-R2
// Base part: "SCD40"
// Starts with "SCD4" → Returns: "SCD4x"
```

### Series Groupings

| Prefix | Series Group |
|--------|--------------|
| SHT3x | SHT30, SHT31, SHT35 |
| SHT4x | SHT40, SHT41, SHT45 |
| SHT8x | SHT85 |
| STS3x | STS30, STS31 |
| STS4x | STS40 |
| SGP3x | SGP30 |
| SGP4x | SGP40, SGP41 |
| SCD3x | SCD30 |
| SCD4x | SCD40, SCD41 |
| SFA3x | SFA30 |
| SPS3x | SPS30 |
| SLFxx | All SLF flow sensors |
| SDP3x | SDP31 |
| SDP8x | SDP810 |

---

## Replacement Compatibility

### Official Replacement Logic

The handler implements `isOfficialReplacement()` with these rules:

1. **Same series:** Compatible if accuracy grade allows
   - A can replace B or F
   - B can replace F
   - F cannot replace A or B

2. **Cross-series replacements:**
   - SHT4x can replace SHT3x (next generation)
   - STS4x can replace STS3x
   - SGP4x can replace SGP3x (improved gas sensor)
   - SCD4x can replace SCD3x (miniaturized CO2)

3. **Not interchangeable:**
   - SDP3x and SDP8x (different pressure ranges)
   - Different sensor families (SHT cannot replace SGP)

---

## Example MPNs

| MPN | Type | Description |
|-----|------|-------------|
| `SHT30-DIS-B` | Humidity/Temp | Series 30, digital, grade B |
| `SHT31-DIS-F` | Humidity/Temp | Series 31, digital, grade F |
| `SHT35-DIS-B` | Humidity/Temp | Series 35, digital, grade B (highest accuracy in 3x) |
| `SHT40-AD1B-R2` | Humidity/Temp | Series 40, alt address, grade B, 2k reel |
| `SHT41-AD1B-R2` | Humidity/Temp | Series 41, alt address, grade B |
| `SHT45-AD1B-R2` | Humidity/Temp | Series 45, highest accuracy in 4x |
| `SHT85` | Humidity/Temp | Premium, PTFE filter membrane |
| `STS30-DIS-B` | Temp only | Temperature-only sensor |
| `STS40-AD1B-R2` | Temp only | 4th gen temp-only |
| `SGP30` | Gas/VOC | Multi-pixel gas sensor |
| `SGP40-D-R4` | Gas/VOC | 4th gen VOC sensor |
| `SGP41-D-R4` | Gas/VOC | NOx + VOC dual sensor |
| `SCD30` | CO2 | NDIR CO2 sensor module |
| `SCD40-D-R2` | CO2 | Miniaturized CO2 sensor |
| `SCD41-D-R2` | CO2 | Extended range CO2 |
| `SFA30` | Formaldehyde | Formaldehyde sensor |
| `SPS30` | Particulate | PM1.0/2.5/4/10 sensor |
| `SLF3S-1300F` | Liquid Flow | 0-40 ml/min flow sensor |
| `SDP31` | Pressure | +/- 500 Pa differential |
| `SDP810-500PA` | Pressure | Configurable range |

---

## Handler Implementation Notes

### Interface Extraction

```java
// Most Sensirion sensors use I2C
// AD variants indicate alternate I2C address

if (upperMpn.contains("-AD")) return "I2C-ALT"; // Alternate address
return "I2C"; // Default
```

### Accuracy Grade Extraction

```java
// Grade embedded in variant code or as suffix
// SHT31-DIS-B → grade "B"
// SHT40-AD1B → grade "B" (from "1B")

if (upperMpn.contains("-B") || upperMpn.contains("1B")) return "B";
if (upperMpn.contains("-F") || upperMpn.contains("1F")) return "F";
if (upperMpn.contains("-A") || upperMpn.contains("1A")) return "A";
```

---

## Related Files

- Handler: `manufacturers/SensirionHandler.java`
- Component types: `SENSOR`, `HUMIDITY_SENSOR`, `TEMPERATURE_SENSOR`, `PRESSURE_SENSOR`, `SENSOR_FLOW`, `IC`

---

## Learnings & Quirks

- **All sensors use I2C**: Sensirion standardizes on I2C digital interface across all product families
- **DFN is dominant**: Most sensors use DFN (Dual Flat No-lead) packaging; "P" suffix indicates pin header for development
- **Generation upgrades**: 4x series generally replaces 3x series with improved specs and smaller footprint
- **Accuracy grade hierarchy**: A > B > F; higher grades can replace lower but not vice versa
- **Dual-function sensors**: SHTxx measures both humidity AND temperature; use appropriate ComponentType based on application
- **Complex sensors as IC**: Gas (SGP), CO2 (SCD), and particulate (SPS) sensors are registered as both SENSOR and IC due to their processing complexity
- **AD variants**: "AD" in variant code indicates alternate I2C address for multi-sensor configurations
- **Reel quantities**: R2 = 2000 pcs, R4 = 4000 pcs tape and reel
- **Pressure sensors not interchangeable**: SDP3x and SDP8x have different pressure ranges; check specs before substitution
- **No manufacturer-specific ComponentTypes**: Unlike TI/ST handlers, Sensirion uses generic types (HUMIDITY_SENSOR, not HUMIDITY_SENSOR_SENSIRION)

<!-- Add new learnings above this line -->
