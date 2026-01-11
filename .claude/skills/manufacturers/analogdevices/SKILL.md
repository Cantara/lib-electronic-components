---
name: analogdevices
description: Analog Devices MPN encoding patterns, suffix decoding, and handler guidance. Use when working with AD components or AnalogDevicesHandler.
---

# Analog Devices Manufacturer Skill

## MPN Structure

Analog Devices MPNs follow this general structure:

```
[PREFIX][SERIES][GRADE][PACKAGE][ROHS][REEL]
   |       |       |       |       |      |
   |       |       |       |       |      +-- Optional: -R7=7" tape, -RL=large reel
   |       |       |       |       +-- Z = RoHS compliant
   |       |       |       +-- Package code (R, RU, RZ, etc.)
   |       |       +-- Temperature/performance grade (A-G, J, K, etc.)
   |       +-- Series number (e.g., 8065, 7606, 590)
   +-- Family prefix (AD, OP, REF, AMP, ADXL, etc.)
```

### Example Decoding

```
AD8065ARTZ-R7
|   |   ||||  |
|   |   ||||  +-- R7 = 7" tape and reel
|   |   |||+-- Z = RoHS compliant
|   |   ||+-- T = SOT-23 package (5-pin)
|   |   |+-- R = Surface mount
|   |   +-- A = -40C to +85C industrial grade
|   +-- 8065 = High speed FET input op-amp series
+-- AD = Analog Devices prefix

ADXL345BCCZ-RL
|   |   |  ||  |
|   |   |  ||  +-- RL = Large tape and reel
|   |   |  |+-- Z = RoHS compliant
|   |   |  +-- CC = LGA package
|   |   +-- B = Variant (improved specs)
|   +-- 345 = 3-axis digital accelerometer
+-- ADXL = Accelerometer family prefix

AD7606BSTZ
|   |   |||
|   |   ||+-- Z = RoHS compliant
|   |   |+-- T = LQFP package
|   |   +-- S = industrial temp, LQFP
|   +-- 7606 = 8-channel simultaneous sampling ADC
+-- AD = Analog Devices prefix
```

---

## Family Prefixes

### Analog/Precision (AD, OP, AMP)

| Prefix | Category | Examples |
|--------|----------|----------|
| AD8xxx | High-speed op-amps | AD8065 (FET input), AD8599 (low noise) |
| AD5xxx | DACs | AD5628 (octal 12-bit), AD5543 (16-bit) |
| AD7xxx | ADCs | AD7606 (8-ch SAR), AD7124 (24-bit sigma-delta) |
| AD9xxx | High-speed ADCs | AD9697 (14-bit 1.3GSPS) |
| OP | Precision op-amps | OP07, OP27, OP37, OP177 |
| AMP | Amplifiers | AMP01, AMP02 |

### Voltage References (REF, ADR)

| Prefix | Category | Examples |
|--------|----------|----------|
| REF | Voltage references | REF195, REF02 |
| ADR | Precision references | ADR421, ADR4540 |

### Sensors & MEMS (ADXL, ADXRS, ADT)

| Prefix | Category | Examples |
|--------|----------|----------|
| ADXL | Accelerometers | ADXL345, ADXL355, ADXL375 |
| ADXRS | Gyroscopes | ADXRS290, ADXRS450 |
| ADT | Digital temp sensors | ADT7410, ADT7420 |
| AD590 | Analog temp sensor | AD590 (current output) |
| TMP | Temperature sensors | TMP35, TMP36 |

### Audio/Video (SSM, ADV)

| Prefix | Category | Examples |
|--------|----------|----------|
| SSM | Audio products | SSM2164, SSM2166 |
| ADV | Video products | ADV7611, ADV7842 |

---

## Package Codes

### Surface Mount Packages

| Code | Package | Description |
|------|---------|-------------|
| R | SOIC | Small Outline IC (narrow body) |
| RW | SOIC-Wide | Wide body SOIC |
| RM | MSOP | Mini Small Outline Package |
| RU | TSSOP | Thin Shrink SOP |
| RZ | QFN/LFCSP | Quad Flat No-lead |
| RT | SOT-23-5 | 5-pin SOT-23 |
| T | LQFP | Low-profile QFP |
| BCP | WLCSP | Wafer Level CSP |
| CP | LGA | Land Grid Array |
| CCZ | LGA | LGA variant (accelerometers) |
| BCCZ | LGA | LGA variant (3x5mm) |

### Through-Hole Packages

| Code | Package | Description |
|------|---------|-------------|
| N | PDIP | Plastic DIP |
| P | PDIP | Plastic DIP (alternate) |
| H | TO-39/TO-52 | Metal can (hermetic) |
| JH | CDIP | Ceramic DIP (hermetic) |
| K | TO-3 | Metal power package |

### Package Suffix Examples

| MPN | Package |
|-----|---------|
| AD8065ARZ | SOIC-8 |
| AD8065ARTZ | SOT-23-5 |
| AD8065ARMZ | MSOP-8 |
| AD8065ACPZ | LFCSP-8 (QFN) |
| ADXL345BCCZ | LGA-14 |
| AD7606BSTZ | LQFP-64 |

---

## Temperature Grades

### Standard Grades

| Letter | Range | Application |
|--------|-------|-------------|
| J, K, L | 0C to +70C | Commercial |
| A, B, C | -40C to +85C | Industrial |
| S, T, U | -55C to +125C | Military/Hi-Rel |

### Grade Examples in Part Numbers

| MPN | Grade | Temp Range |
|-----|-------|------------|
| AD8065ARZ | A = Industrial | -40C to +85C |
| OP27EPZ | E = Commercial | 0C to +70C |
| OP27GPZ | G = Industrial | -40C to +85C |
| AD590KH | K = Commercial | 0C to +70C |
| AD590LH | L = Commercial (tighter spec) | 0C to +70C |
| AD590MH | M = Military | -55C to +125C |

### Automotive Suffix

| Suffix | Meaning |
|--------|---------|
| WARTZ | Automotive qualified (-40C to +105C) |
| Q1 | AEC-Q100 qualified |

---

## Common Series Reference

### Op-Amps

| Series | Type | Key Features |
|--------|------|--------------|
| AD8065/AD8066 | Single/Dual FET | High speed (145MHz), low distortion |
| AD8605/AD8606/AD8608 | Single/Dual/Quad | Rail-to-rail, low noise |
| AD8597/AD8599 | Single/Dual | Ultra low noise, low distortion |
| AD8613/AD8617/AD8619 | Single/Dual/Quad | Micropower rail-to-rail |
| OP07 | Single | Ultralow offset (75uV max) |
| OP27 | Single | Low noise + low offset |
| OP37 | Single | Decompensated OP27 (gain > 5) |
| OP177 | Single | Improved OP07 |

### ADCs (AD7xxx = Precision, AD9xxx = High-Speed)

| Series | Type | Resolution |
|--------|------|------------|
| AD7606 | 8-ch simultaneous SAR | 16-bit |
| AD7124 | Sigma-delta | 24-bit |
| AD7190 | Sigma-delta | 24-bit |
| AD7689 | 8-ch SAR | 16-bit |
| AD9697 | High-speed | 14-bit, 1.3GSPS |
| AD9628 | Dual high-speed | 12-bit, 125MSPS |

### DACs (AD5xxx)

| Series | Type | Resolution |
|--------|------|------------|
| AD5543 | Current output | 16-bit |
| AD5628/5648/5668 | Octal voltage | 12/14/16-bit |
| AD5754 | Quad voltage | 16-bit |

### Accelerometers (ADXL)

| Series | Type | Range |
|--------|------|-------|
| ADXL345 | 3-axis digital | +/-16g |
| ADXL355 | 3-axis low noise | +/-8g |
| ADXL375 | 3-axis high-g | +/-200g |
| ADXL1002 | Single-axis MEMS | +/-50g |

### Temperature Sensors

| Series | Type | Output |
|--------|------|--------|
| AD590 | Analog current | 1uA/K |
| ADT7410 | Digital I2C | 16-bit, 0.0078C |
| TMP36 | Analog voltage | 10mV/C |

---

## Tape and Reel Suffixes

| Suffix | Meaning |
|--------|---------|
| -R7 | 7" tape and reel |
| -RL | 13" large tape and reel |
| -REEL | Large reel (legacy) |
| -REEL7 | 7" reel (legacy) |
| (none) | Tube or tray packaging |

### LTC/Linear Technology Products

| Suffix | Meaning |
|--------|---------|
| #TRPBF | 2500 piece tape and reel, lead-free |
| #TRMPBF | 500 piece mini reel, lead-free |
| #PBF | Lead-free (bulk) |

---

## Handler Implementation Notes

### Package Code Extraction

```java
// AD package codes appear AFTER grade letter
// Example: AD8065ARZ -> base="AD8065", grade="A", package="R", rohs="Z"

// Common package code mappings
switch (packageCode) {
    case "R" -> "SOIC";
    case "RM" -> "MSOP";
    case "RU" -> "TSSOP";
    case "RZ" -> "QFN";
    case "RT" -> "SOT-23";
    case "T" -> "LQFP";
    case "CP" -> "LGA";
    case "BCCZ", "CCZ" -> "LGA";  // Accelerometer packages
    case "N", "P" -> "PDIP";
    case "H" -> "TO-52";
}

// IMPORTANT: Strip trailing "Z" (RoHS indicator) before package lookup
// AD8065ARZ -> package is "R" (SOIC), not "RZ" (QFN)
String pkg = mpn.replaceAll("Z$", "").replaceAll(".*[A-Z](R[A-Z]*)$", "$1");
```

### Series Extraction

```java
// Most AD parts: prefix + numeric series
// AD8065ARZ -> series = "AD8065"
// ADXL345BCCZ -> series = "ADXL345"

// OP series: "OP" + 2 digits
// OP27GPZ -> series = "OP27"

// Strip grade, package, and RoHS suffixes
String series = mpn.replaceAll("[A-Z]{1,4}Z?(-R[L7])?$", "");
```

### Critical Pattern Issues

1. **AD8xxx vs AD5xxx vs AD7xxx confusion**:
   - AD8xxx = Op-amps/amplifiers
   - AD5xxx = DACs
   - AD7xxx = ADCs (precision)
   - AD9xxx = ADCs (high-speed)

2. **ADT vs AD590 temperature sensors**:
   - ADT = Digital temperature sensors
   - AD590 = Analog current-output sensor

3. **Single/Dual/Quad variants**:
   - Often differ by last digit: AD8065 (single), AD8066 (dual)
   - Or by middle digits: AD8605/AD8606/AD8608

---

## Related Files

- Handler: `manufacturers/AnalogDevicesHandler.java`
- Component types: `OPAMP_AD`, `ADC_AD`, `DAC_AD`, `AMPLIFIER_AD`, `VOLTAGE_REFERENCE_AD`, `TEMPERATURE_SENSOR_AD`, `ACCELEROMETER_AD`, `GYROSCOPE_AD`
- Package registry: `PackageCodeRegistry.java` (standard codes)

---

## Known Handler Issues

1. **HashSet in getSupportedTypes()**: Should use `Set.of()` or `EnumSet` for immutability
2. **Missing ADC/DAC patterns**: No patterns registered for AD7xxx (ADCs) or AD5xxx (DACs) with manufacturer-specific types
3. **Generic IC fallback**: ADCs and DACs fall through to generic `ComponentType.IC` instead of `ADC_AD`/`DAC_AD`
4. **Package extraction bug**: Current logic doesn't properly handle the grade+package+rohs structure

---

## Learnings & Edge Cases

- **RoHS suffix "Z"**: Almost all modern AD parts end with "Z" for RoHS compliance - don't confuse with package code
- **Single/Dual naming**: AD8065=single, AD8066=dual (differ by last digit)
- **OP series grades**: OP27E (commercial), OP27G (industrial) - letter comes right after series number
- **ADXL accelerometers**: Use "BCCZ" and "CCZ" for LGA packages, unique to MEMS products
- **AD590 is special**: Very old design, uses TO-52 metal can ("H" suffix), current output (1uA/K)
- **Linear Technology merger**: LTC parts now under AD umbrella, use different suffix convention (#TRPBF)

<!-- Add new learnings above this line -->
