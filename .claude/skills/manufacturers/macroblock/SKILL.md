---
name: macroblock
description: Macroblock Inc. MPN encoding patterns, LED driver decoding, and handler guidance. Use when working with Macroblock LED drivers or MacroblockHandler.
---

# Macroblock Inc. Manufacturer Skill

## MPN Structure

Macroblock specializes in LED driver ICs for displays.

```
MBI[SERIES][MODEL][PACKAGE][-OPTIONS]
    |   |     |       |        |
    |   |     |       |        +-- Options (TR = Tape and Reel)
    |   |     |       +-- Package code (GP, GF, GN, etc.)
    |   |     +-- Model number (024, 039, 153)
    |   +-- Series (5=constant current, 6=DC-DC, 1=scan)
    +-- MBI = Macroblock Inc.
```

### Example Decoding

```
MBI5024GP
|   | | ||
|   | | |+-- P = PDIP or SOP-24 variant
|   | | +-- G = package type indicator
|   | +-- 024 = 16-channel model
|   +-- 5 = Constant current series
+-- MBI = Macroblock Inc.

MBI5153-TR
|   |   ||
|   |   |+-- TR = Tape and Reel
|   |   +-- (no package code, bare part number)
|   +-- 5153 = 48-channel model
+-- MBI = Macroblock Inc.
```

---

## Product Families

### MBI5xxx Series - Constant Current LED Drivers

16-bit shift register based constant current drivers. The workhorses of LED display drivers.

| Part | Channels | Features |
|------|----------|----------|
| MBI5024 | 16 | Basic constant current |
| MBI5039 | 16 | Error detection |
| MBI5040 | 16 | Enhanced features |
| MBI5041 | 16 | Variant |
| MBI5042 | 16 | Variant |
| MBI5050 | 16 | High-end 16-ch |
| MBI5124 | 16 | Enhanced variant |
| MBI5153 | 48 | High channel count |
| MBI5252 | 48 | Enhanced 48-ch |
| MBI5353 | 48 | Premium 48-ch |

### MBI6xxx Series - DC-DC LED Drivers

Switching LED drivers for backlighting and general LED lighting.

| Part | Description |
|------|-------------|
| MBI6651 | DC-DC LED driver |
| MBI6652 | Enhanced variant |
| MBI6661 | High power variant |

### MBI1xxx Series - Scan Drivers

Row/scan drivers for LED matrix displays, work in conjunction with MBI5xxx column drivers.

| Part | Description |
|------|-------------|
| MBI1801 | P-channel scan driver |
| MBI1802 | Enhanced scan driver |

---

## Package Codes

| Code | Package | Notes |
|------|---------|-------|
| GP | SOP-24 | Standard SOP package |
| GF | SSOP-24 | Shrink SOP |
| GH | TSSOP-24 | Thin SOP |
| GS | SSOP-24 | SSOP variant |
| GT | TSSOP-24 | TSSOP variant |
| GN | QFN | Quad flat no-lead |
| GQ | QFN | QFN variant |
| TE | TQFP | Thin QFP |
| TF | TQFP-48 | 48-pin TQFP |
| TP | TQFP-64 | 64-pin TQFP |
| S | SOP | SOP generic |
| T | TSSOP | TSSOP generic |
| N | QFN | QFN generic |
| P | PDIP | Through-hole DIP |

### Options

| Suffix | Meaning |
|--------|---------|
| -TR | Tape and Reel packaging |

---

## Channel Count Reference

| Part | Channels | Application |
|------|----------|-------------|
| MBI5024 | 16 | Standard displays |
| MBI5039 | 16 | With fault detection |
| MBI5050 | 16 | Premium 16-ch |
| MBI5153 | 48 | High-density displays |
| MBI5252 | 48 | Enhanced 48-ch |
| MBI5353 | 48 | Premium 48-ch |

---

## Handler Implementation Notes

### Series Extraction

```java
// Extract series prefix (MBI + first digit)
// MBI5024GP -> MBI5
// MBI6651 -> MBI6
// MBI1801 -> MBI1

if (upperMpn.matches("^MBI5[0-9]{3}.*")) return "MBI5";
if (upperMpn.matches("^MBI6[0-9]{3}.*")) return "MBI6";
if (upperMpn.matches("^MBI1[0-9]{3}.*")) return "MBI1";
```

### Package Code Extraction

```java
// Pattern: MBI[156]xxx[package-code]
Pattern packagePattern = Pattern.compile("^MBI[156][0-9]{3}([A-Z]{1,2}).*$");

// Decode package using lookup table
PACKAGE_CODES.put("GP", "SOP-24");
PACKAGE_CODES.put("GF", "SSOP-24");
PACKAGE_CODES.put("GN", "QFN");
```

### Base Part Extraction

```java
// Extract MBI[156]xxx - the core 7-character part number
// MBI5024GP -> MBI5024
// MBI5153-TR -> MBI5153

Pattern basePattern = Pattern.compile("^(MBI[156][0-9]{3}).*$");
```

### Matching Patterns

```java
// MBI5xxx - Constant current LED drivers
"^MBI5[0-9]{3}[A-Z0-9-]*$"

// MBI6xxx - DC-DC LED drivers
"^MBI6[0-9]{3}[A-Z0-9-]*$"

// MBI1xxx - Scan drivers
"^MBI1[0-9]{3}[A-Z0-9-]*$"
```

---

## Helper Methods

The handler provides useful helper methods:

```java
// Get channel count for known parts
int channels = handler.getChannelCount("MBI5024");  // Returns 16

// Check driver type
handler.isConstantCurrentDriver("MBI5024");  // true
handler.isDCDCDriver("MBI6651");             // true
handler.isScanDriver("MBI1801");             // true

// Get series description
handler.getSeriesDescription("MBI5");  // "Constant Current LED Drivers"
```

---

## Replacement Rules

### Same Base Part, Different Package

Parts with the same base number but different packages are replacements:
- MBI5024GP = MBI5024GF = MBI5024GN (same part, different package)
- MBI5024 = MBI5024-TR (same part with/without tape and reel)

### Same Series, Different Model

Parts in the same series with different model numbers are NOT replacements:
- MBI5024 (16-ch) != MBI5153 (48-ch) - different channel count
- MBI5024 != MBI5039 - different features (error detection)

---

## Related Files

- Handler: `manufacturers/MacroblockHandler.java`
- Component types: `ComponentType.IC`, `ComponentType.LED_DRIVER`

---

## Common MPNs

| MPN | Description | Channels |
|-----|-------------|----------|
| MBI5024GP | Constant current, SOP-24 | 16 |
| MBI5039GP | With error detection, SOP-24 | 16 |
| MBI5153 | High channel count | 48 |
| MBI5252 | Enhanced 48-ch | 48 |
| MBI6651 | DC-DC driver | N/A |
| MBI1801 | Scan driver | N/A |

---

## LED Display Architecture

Typical LED display driver setup:

```
MBI5xxx (Column Drivers) - Constant current sink
    |
    v
LED Matrix
    ^
    |
MBI1xxx (Row/Scan Drivers) - P-channel switch
```

- **MBI5xxx**: Sinks current through LEDs, controls brightness via PWM
- **MBI1xxx**: Selects which row of LEDs is active (scan multiplexing)

---

## Applications

- LED video walls
- LED signage displays
- Indoor/outdoor LED screens
- LED matrix displays
- Full-color LED panels
- RGB LED modules
- LED backlighting

---

## Learnings & Edge Cases

- **MBI prefix**: All Macroblock parts start with MBI.
- **Series digit significance**: 5=constant current sink, 6=DC-DC driver, 1=scan driver.
- **Channel count varies**: 16-channel parts (MBI50xx) vs 48-channel (MBI51xx/52xx/53xx).
- **Package suffix position**: 1-2 letters immediately after the 4-digit model number.
- **No manufacturer-specific ComponentTypes**: Handler uses generic IC and LED_DRIVER types.
- **-TR suffix**: Tape and Reel is packaging option, not a different part.
- **Column vs Row drivers**: MBI5xxx drives columns (current sink), MBI1xxx drives rows (switch).
- **Error detection feature**: MBI5039 has built-in LED open/short detection.

<!-- Add new learnings above this line -->
