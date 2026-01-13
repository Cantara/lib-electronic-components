---
name: everlight
description: Everlight Electronics MPN encoding patterns, LED/optocoupler suffix decoding, and handler guidance. Use when working with Everlight LEDs, optocouplers, phototransistors, or EverlightHandler.
---

# Everlight Electronics Manufacturer Skill

## Manufacturer Overview

Everlight Electronics is a major LED and optoelectronics manufacturer producing:

- **Standard SMD LEDs** - 17-21, 19-21, 26-21 series based on package size
- **High-power SMD LEDs** - ELSH series
- **SMD LEDs** - EL-SSD series
- **Through-hole LEDs** - 333-2 series
- **RGB LEDs** - 19-337 series
- **High-power LEDs** - SHWO series, EASP series
- **Infrared LEDs** - IR333, IR908
- **Phototransistors** - PT333, PT908, PT19-21
- **Optocouplers** - EL817, EL357, EL3063
- **Light Sensors** - ALS-PT19-315
- **Seven-segment Displays** - ELS series

---

## MPN Structure

### Standard SMD LEDs

```
[SIZE]-[COLOR][BRIGHTNESS][BIN]/[LENS]/[PACKING]
  |       |        |        |     |       |
  |       |        |        |     |       +-- Packing: TR8=Tape&Reel
  |       |        |        |     +-- Lens spec: S530
  |       |        |        +-- Color bin: A3
  |       |        +-- Brightness bin
  |       +-- Color code: SURC=Super Red Clear
  +-- Package size code: 17-21=0603
```

**Example Decoding:**
```
17-21SURC/S530-A3/TR8
|    |     |   |   |
|    |     |   |   +-- TR8 = Tape & Reel packing
|    |     |   +-- A3 = Color bin
|    |     +-- S530 = Brightness bin
|    +-- SURC = Super Red Clear lens
+-- 17-21 = 0603 package size
```

### Optocouplers

```
EL[SERIES][VARIANT]-[CTR_BIN]-[PACKING]
   |        |          |         |
   |        |          |         +-- Packing: TU=Tube, TR=Tape
   |        |          +-- CTR bin: C, D, E
   |        +-- Variant: S1=SMD single, S2=SMD dual
   +-- Series number: 817, 357, 3063
```

**Example Decoding:**
```
EL817S1-C-TU
|  | | | | |
|  | | | | +-- TU = Tube packing
|  | | | +-- C = CTR (Current Transfer Ratio) bin
|  | | +-- S1 = SMD single channel
|  | +-- 817 = Series number
+-- EL = Everlight optocoupler prefix
```

### Infrared LEDs

```
IR[SIZE]-[VARIANT]
   |        |
   |        +-- Variant: A, 7C, 7C-F
   +-- Size: 333=5mm, 908=3mm
```

**Example:**
```
IR333-A     -> 5mm IR LED, A variant
IR908-7C-F  -> 3mm IR LED, 7C variant, filtered
```

### Phototransistors

```
PT[SIZE]-[VARIANT]/[PACKING]
   |        |         |
   |        |         +-- Packing: TR8=Tape&Reel
   |        +-- Variant: 3C, 7C-F, B
   +-- Size: 333=5mm, 908=3mm, 19-21=SMD
```

**Example:**
```
PT333-3C        -> 5mm through-hole phototransistor
PT908-7C-F      -> 3mm through-hole phototransistor, filtered
PT19-21B/TR8    -> SMD phototransistor (0805), Tape&Reel
```

### Light Sensors

```
ALS-PT[SIZE]-[SPECS]/[LENS]/[PACKING]
       |       |       |        |
       |       |       |        +-- TR8=Tape&Reel
       |       |       +-- Lens spec: L177
       |       +-- Specs: 315C
       +-- Size: 19-315
```

**Example:**
```
ALS-PT19-315C/L177/TR8
|      |   | |    |
|      |   | |    +-- TR8 = Tape & Reel
|      |   | +-- L177 = Lens spec
|      |   +-- 315C = Specification code
|      +-- 19-315 = Series
+-- ALS-PT = Ambient Light Sensor prefix
```

---

## Package Size Codes

### SMD LED Size Codes

| Everlight Code | Standard Size | Notes |
|----------------|---------------|-------|
| 12-21 | 0402 | Ultra-small |
| 15-21 | 0603 | Small |
| 17-21 | 0603 | Common small |
| 19-21 | 0805 | Common medium |
| 23-21 | 0805 | Medium |
| 23-22 | 0805 | Medium variant |
| 24-21 | 1206 | Large |
| 26-21 | 1206 | Common large |
| 67-21 | 2835 | High-power SMD |
| 19-337 | PLCC-6 | RGB LED |

### Through-Hole Sizes

| Code | Package | Description |
|------|---------|-------------|
| 333 | T-1 | 3mm through-hole |
| 333-2 | T-1 | 3mm through-hole |
| 908 | T-1-3/4 | 5mm through-hole |

---

## Color Codes

| Code | Full Name | Color |
|------|-----------|-------|
| SURC | Super Red Clear | Red |
| SUGC | Super Green Clear | Green |
| SUBC | Super Blue Clear | Blue |
| SUYC | Super Yellow Clear | Yellow |
| SUOC | Super Orange Clear | Orange |
| SUWC | Super White Clear | White |
| SRC | (Short) Red Clear | Red |
| SGC | (Short) Green Clear | Green |
| SBC | (Short) Blue Clear | Blue |
| RGB | Multi-color | RGB |

**Short Forms:** URC, UGC, UBC, UYC, UOC, UWC

---

## Supported Component Types

From `EverlightHandler.getSupportedTypes()`:

| ComponentType | Description |
|---------------|-------------|
| `LED` | All LED types (SMD, TH, IR, RGB) |
| `IC` | Optocouplers, light sensors, phototransistors |
| `OPTOCOUPLER_TOSHIBA` | Optocouplers (uses existing type) |

---

## Package Code Extraction Rules

The handler extracts package codes based on product category:

### SMD LEDs
- Extract size code prefix (e.g., "17-21")
- Map to standard size (e.g., "0603")

### Through-Hole LEDs
- 333 series -> "T-1" (3mm)
- 908 series -> "T-1-3/4" (5mm)

### Optocouplers
- S1/S2 suffix -> "SMD"
- Default -> "DIP-4"

### High-Power LEDs
- ELSH series with Q prefix -> "QFN"
- SHWO/EASP series -> "SMD-HIGHPOWER"

### Light Sensors
- ALS-PT series -> "SMD"

### Seven-Segment Displays
- ELS series -> "Display"

---

## Series Extraction Rules

| Pattern | Series Extracted | Example |
|---------|------------------|---------|
| SMD LED (17-21...) | Size code | "17-21" |
| RGB LED (19-337...) | Size code | "19-337" |
| Through-hole LED | Base number | "333" or "333-2" |
| High-power ELSH | "ELSH" | "ELSH" |
| High-power SHWO | "SHWO" | "SHWO" |
| High-power EASP | "EASP" | "EASP" |
| Infrared LED | IR + size | "IR333", "IR908" |
| Phototransistor TH | PT + size | "PT333", "PT908" |
| Phototransistor SMD | PT + normalized size | "PT1921" |
| Optocoupler | EL + series | "EL817", "EL357" |
| Light Sensor | Full prefix | "ALS-PT19-315" |
| Display | ELS + number | "ELS-123" |

---

## Example MPNs with Explanations

### Standard SMD LEDs

```
17-21SURC/S530-A3/TR8
  - Package: 0603 (from 17-21)
  - Color: Red (SURC = Super Red Clear)
  - Series: 17-21
  - Packing: Tape & Reel

19-21SUGC/C530/TR8
  - Package: 0805 (from 19-21)
  - Color: Green (SUGC)
  - Series: 19-21

26-21SUBC/S530-A4/TR8
  - Package: 1206 (from 26-21)
  - Color: Blue (SUBC)
  - Series: 26-21
```

### Optocouplers

```
EL817S1-C-TU
  - Package: SMD (S1 = SMD single)
  - Series: EL817
  - CTR Bin: C
  - Packing: Tube

EL357N-C
  - Package: DIP-4 (N = DIP, no SMD indicator)
  - Series: EL357
  - CTR Bin: C

EL3063S1-TU
  - Package: SMD (S1)
  - Series: EL3063
  - Zero-crossing triac optocoupler
```

### Infrared Components

```
IR333-A
  - Package: T-1 (3mm)
  - Series: IR333
  - Category: Infrared LED

IR908-7C-F
  - Package: T-1-3/4 (5mm)
  - Series: IR908
  - Variant: 7C, Filtered

PT333-3C
  - Package: T-1 (3mm)
  - Series: PT333
  - Category: Phototransistor

PT19-21B/TR8
  - Package: 0805 (from 19-21)
  - Series: PT1921
  - Category: SMD Phototransistor
  - Mounting: SMD
```

### Light Sensors

```
ALS-PT19-315C/L177/TR8
  - Package: SMD
  - Series: ALS-PT19-315
  - Category: Light Sensor
  - Lens: L177
  - Packing: Tape & Reel
```

### High-Power LEDs

```
ELSH-Q61K1-0LPGS
  - Package: QFN (Q prefix)
  - Series: ELSH
  - Category: High-power SMD LED

SHWO-8LPRS
  - Package: SMD-HIGHPOWER
  - Series: SHWO
  - Category: High-power LED
```

---

## Handler Implementation Notes

### Mounting Type Detection

The handler includes a `getMountingType()` helper method:

```java
// Through-hole components
"333-?" prefix  -> "Through-hole"
"IR333", "IR908" -> "Through-hole"
"PT333", "PT908" -> "Through-hole"

// SMD components
"XX-XX" pattern -> "SMD"
"ELSH-", "EL-SSD", "SHWO", "EASP" -> "SMD"
"ALS-PT", "PT19-21" -> "SMD"

// DIP for optocouplers
"EL817", etc. without S1/S2/G -> "DIP"
"EL817S1" with S1/S2/G -> "SMD"
```

### Product Category Detection

The handler includes a `getProductCategory()` helper:

| Pattern | Category |
|---------|----------|
| `XX-XXX.*RGB.*` | RGB LED |
| `XX-XX[A-Z].*` | SMD LED |
| `333-?[A-Z].*` | Through-hole LED |
| `ELSH-.*` | High-power SMD LED |
| `SHWO.*`, `EASP.*` | High-power LED |
| `IR[0-9]{3}.*` | Infrared LED |
| `PT[0-9]{2,3}.*` | Phototransistor |
| `EL[0-9]{3,4}.*` | Optocoupler |
| `ALS-PT.*` | Light Sensor |
| `ELS-.*` | Seven-segment Display |

### Color Extraction

The handler extracts LED colors from MPNs:

```java
"RGB" in MPN       -> "RGB"
"SURC", "SRC", "URC", "-R" -> "Red"
"SUGC", "SGC", "UGC", "-G" -> "Green"
"SUBC", "SBC", "UBC", "-B" -> "Blue"
"SUYC", "SYC", "UYC", "-Y" -> "Yellow"
"SUOC", "SOC", "UOC", "-O" -> "Orange"
"SUWC", "SWC", "UWC", "-W" -> "White"
```

---

## Related Files

- Handler: `manufacturers/EverlightHandler.java`
- Component types used: `LED`, `IC`, `OPTOCOUPLER_TOSHIBA`
- Test file: `handlers/EverlightHandlerTest.java` (if exists)

---

## Learnings & Quirks

### General Notes
- **No manufacturer-specific ComponentTypes**: Unlike TI/ST, Everlight uses generic `LED` and `IC` types
- **OPTOCOUPLER_TOSHIBA reuse**: Everlight optocouplers use the Toshiba optocoupler type since a generic optocoupler type doesn't exist
- **Size code pattern**: Everlight uses XX-XX format (e.g., 17-21) for SMD packages which maps to standard sizes
- **Phototransistors dual-typed**: Registered under both `LED` and `IC` component types

### MPN Parsing
- **Slash separators**: Everlight MPNs use `/` to separate sections (size/color/brightness/packing)
- **Color in MPN**: Color code appears directly after size code, not as a suffix
- **Brightness bins**: The S530, C530 codes after color indicate brightness binning
- **Color bins**: A3, A4 codes after brightness indicate color temperature binning

### Package Extraction
- **SMD size code mapping**: Must handle the Everlight-specific size codes (17-21 -> 0603)
- **Through-hole sizes**: IR/PT 333 = 3mm, 908 = 5mm (note: 908 is larger despite number)
- **Optocoupler default**: Without SMD indicator (S1/S2), assume DIP-4

### Series Extraction
- **Phototransistor SMD normalization**: PT19-21 extracts as "PT1921" (dash removed)
- **RGB LED series**: Uses the 3-digit format (19-337) not 2-digit

### Replacement Logic
- **Same series required**: `isOfficialReplacement()` requires matching series
- **Color matching for LEDs**: If both parts have identifiable colors, they must match
- **Optocouplers/sensors**: Same series alone is sufficient for replacement

<!-- Add new learnings above this line -->
