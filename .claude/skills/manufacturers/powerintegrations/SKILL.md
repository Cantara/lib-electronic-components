---
name: powerintegrations
description: Power Integrations MPN encoding patterns, package decoding, and handler guidance. Use when working with Power Integrations SMPS controllers, LED drivers, or gate drivers.
---

# Power Integrations Manufacturer Skill

## Company Overview

Power Integrations is a leading supplier of high-voltage analog integrated circuits for energy-efficient power conversion. Their products include:

- **AC-DC Converters**: Offline switching power supply ICs
- **LED Drivers**: High-efficiency LED driver ICs
- **Gate Drivers**: High-voltage gate driver ICs for motor control
- **Power Factor Correction**: Active PFC controller ICs

Key differentiators:
- Integrated high-voltage MOSFETs in many products
- Patented EcoSmart energy-saving technology
- FluxLink magnetically-coupled feedback (InnoSwitch)

---

## MPN Structure

Power Integrations MPNs follow this general structure:

```
[PREFIX][GENERATION][NUMBER][PACKAGE]
   |         |         |        |
   |         |         |        +-- Package code (Y, G, K, C, etc.)
   |         |         +-- Part number within family (2-3 digits)
   |         +-- Generation indicator (single digit)
   +-- Family prefix (TOP, TNY, LNK, INN, PFS, LCS, CAP, SEN)
```

### Example Decoding

```
TOP249YN
|  |  ||
|  |  |+-- N = Package variant (YN = TO-220 variant)
|  |  +-- Y = TO-220 package
|  +-- 249 = Part number (49 in generation 2)
+-- TOP = TOPSwitch family

INN3673C
|  |   |
|  |   +-- C = InSOP-24 package
|  +-- 3673 = InnoSwitch3 part number
+-- INN = InnoSwitch family

TNY268GN
|  |  ||
|  |  |+-- N = Package variant
|  |  +-- G = SMD-8 (SOIC-8) package
|  +-- 268 = Part number (68 in generation 2)
+-- TNY = TinySwitch family
```

---

## Product Families

### TOPSwitch (TOP)

High-power offline switchers with integrated MOSFET.

| Generation | Pattern | Power Range | Features |
|------------|---------|-------------|----------|
| TOP2xx | TOP2[0-9]{2} | 10-250W | Original TOPSwitch |
| TOP3xx | TOP3[0-9]{2} | 10-250W | Enhanced efficiency |

**Examples**: TOP223Y, TOP249YN, TOP269KG

### TinySwitch (TNY)

Low-power offline switchers for chargers and adapters.

| Generation | Pattern | Power Range | Features |
|------------|---------|-------------|----------|
| TNY2xx | TNY2[0-9]{2} | 2-23W | Original TinySwitch |
| TNY3xx | TNY3[0-9]{2} | 2-28W | TinySwitch-III |

**Examples**: TNY263G, TNY264GN, TNY268PN

### LinkSwitch (LNK)

Cost-optimized low-power switchers.

| Generation | Pattern | Power Range | Features |
|------------|---------|-------------|----------|
| LNK3xx | LNK3[0-9]{2} | 0.5-4W | CV/CC, LED driver |
| LNK4xx | LNK4[0-9]{2} | 2-12W | LinkSwitch-II |
| LNK6xx | LNK6[0-9]{2} | 3-15W | LinkSwitch-LP |

**Examples**: LNK302GN, LNK304, LNK364DN

### InnoSwitch (INN)

High-integration switchers with FluxLink isolated feedback.

| Generation | Pattern | Power Range | Features |
|------------|---------|-------------|----------|
| INN20xx | INN20[0-9]{2} | 15-45W | InnoSwitch original |
| INN30xx | INN30[0-9]{2} | 27-65W | InnoSwitch-CE |
| INN40xx | INN40[0-9]{2} | 45-100W | InnoSwitch-CP |
| INN3xxx | INN3[0-9]{3} | Various | InnoSwitch3 family |

**Examples**: INN2023K, INN3166C, INN3673CJ

### HiperPFS (PFS)

Power Factor Correction controllers.

| Pattern | Power Range | Features |
|---------|-------------|----------|
| PFS7xx | 75-400W | Interleaved PFC |

**Examples**: PFS714EG, PFS7624H

### HiperLCS (LCS)

LLC resonant converter controllers.

| Pattern | Power Range | Features |
|---------|-------------|----------|
| LCS7xx | 100-400W | High-efficiency LLC |

**Examples**: LCS708HG

### CAPZero (CAP)

X-capacitor discharge ICs for no-load power reduction.

| Pattern | Function |
|---------|----------|
| CAP0xx | Safety-compliant X-cap discharge |

**Examples**: CAP002DG, CAP004DG

### SENZero (SEN)

Lossless current sensing ICs.

| Pattern | Function |
|---------|----------|
| SEN0xx | Accurate current sensing |

**Examples**: SEN013DG

---

## Package Codes

### Through-Hole Packages

| Code | Package | Pin Count | Thermal | Notes |
|------|---------|-----------|---------|-------|
| Y | TO-220 | 3-7 | Good | Standard power |
| YN | TO-220 | 3-7 | Good | TO-220 variant |
| D | DIP-8 | 8 | Low | Standard DIP |
| DN | DIP-8 | 8 | Low | DIP variant |
| P | PDIP-8 | 8 | Low | Plastic DIP |
| PN | PDIP-8 | 8 | Low | PDIP variant |

### Surface Mount Packages

| Code | Package | Pin Count | Thermal | Notes |
|------|---------|-----------|---------|-------|
| G | SMD-8 (SOIC-8) | 8 | Medium | Standard SMD |
| GN | SMD-8 (SOIC-8) | 8 | Medium | SMD variant |
| K | eSOP-12 | 12 | Good | InnoSwitch |
| KG | eSOP-12 | 12 | Excellent | With heatsink |
| C | InSOP-24 | 24 | Excellent | InnoSwitch3 |
| CJ | InSOP-24 | 24 | Excellent | InSOP variant |
| H | eSIP-7 | 7 | Good | HiperPFS/LCS |
| HG | eSIP-7 | 7 | Excellent | With heatsink |
| E | eSIP | 7 | Good | eSIP package |
| EG | eSIP-7 | 7 | Excellent | eSIP variant |
| DG | SMD-8 | 8 | Medium | CAPZero/SENZero |

### Package Selection Guide

```
Low Power (< 10W):     G, GN, D, DN, P, PN
Medium Power (10-50W): Y, YN, K, KG
High Power (50-100W+): C, CJ, H, HG, E, EG
```

---

## Supported Component Types

The handler supports these `ComponentType` values:

| Type | Description | Families |
|------|-------------|----------|
| `IC` | Generic IC classification | All families |
| `VOLTAGE_REGULATOR` | Power conversion IC | All families |

**Note**: Power Integrations parts are classified as `VOLTAGE_REGULATOR` since they perform AC-DC conversion and voltage regulation functions, even though they are technically more complex SMPS controllers.

---

## Handler Implementation Notes

### Package Code Extraction

```java
// Extract suffix after the last digit
// TOP249YN → YN
// INN3673C → C

// Check longer suffixes FIRST (e.g., "KG" before "K", "YN" before "Y")
for (int len = Math.min(suffix.length(), 2); len >= 1; len--) {
    String candidate = suffix.substring(0, len);
    if (PACKAGE_CODES.containsKey(candidate)) {
        return PACKAGE_CODES.get(candidate);  // Returns decoded name, not code
    }
}
```

**Key Point**: The handler returns the decoded package name (e.g., "TO-220") not the raw code (e.g., "Y").

### Series Extraction

```java
// Extract 3-letter prefix
if (upperMpn.startsWith("TOP")) return "TOP";
if (upperMpn.startsWith("TNY")) return "TNY";
if (upperMpn.startsWith("LNK")) return "LNK";
if (upperMpn.startsWith("INN")) return "INN";
if (upperMpn.startsWith("PFS")) return "PFS";
if (upperMpn.startsWith("LCS")) return "LCS";
if (upperMpn.startsWith("CAP")) return "CAP";
if (upperMpn.startsWith("SEN")) return "SEN";
```

### Product Family Helper

The handler provides `getProductFamily(String mpn)` to get the marketing name:

| Series | Family Name |
|--------|-------------|
| TOP | TOPSwitch |
| TNY | TinySwitch |
| LNK | LinkSwitch |
| INN | InnoSwitch |
| PFS | HiperPFS |
| LCS | HiperLCS |
| CAP | CAPZero |
| SEN | SENZero |

### Base Part Number Extraction

```java
// extractBasePartNumber() returns series + number without package suffix
// TOP249YN → TOP249
// INN3673CJ → INN3673
// TNY268GN → TNY268
```

### Replacement Compatibility

`isOfficialReplacement()` checks:
1. Same product family (series prefix)
2. Same base part = same part, different package (always compatible)
3. Same sub-family (first 4 chars) with numeric difference <= 10

```java
// Same base part, different package = compatible
TOP249Y and TOP249YN → true (same part)

// Same sub-family, close numbers = potentially compatible
TOP223Y and TOP224Y → true (within 10)
TOP223Y and TOP249Y → false (26 apart)
```

---

## Pattern Matching Details

### Registered Patterns

```
TOPSwitch: ^TOP[23][0-9]{2}[A-Z0-9]*$
TinySwitch: ^TNY[23][0-9]{2}[A-Z0-9]*$
LinkSwitch: ^LNK[346][0-9]{2}[A-Z0-9]*$
InnoSwitch: ^INN[234]0[0-9]{2}[A-Z0-9]*$ and ^INN3[0-9]{3}[A-Z0-9]*$
HiperPFS: ^PFS7[0-9]{2}[A-Z0-9]*$
HiperLCS: ^LCS7[0-9]{2}[A-Z0-9]*$
CAPZero: ^CAP0[0-9]{2}[A-Z0-9]*$
SENZero: ^SEN0[0-9]{2}[A-Z0-9]*$
```

### Explicit matches() Implementation

The handler implements explicit pattern matching in `matches()` rather than relying solely on registry lookup. This provides:
- Better performance (direct regex)
- More predictable behavior
- Explicit fallback to registry

---

## Example MPN Reference

| MPN | Family | Base Part | Package | Notes |
|-----|--------|-----------|---------|-------|
| TOP223Y | TOPSwitch | TOP223 | TO-220 | Entry-level TOPSwitch |
| TOP249YN | TOPSwitch | TOP249 | TO-220 | High-power TOPSwitch |
| TOP269KG | TOPSwitch | TOP269 | eSOP-12 | SMD with heatsink |
| TNY263G | TinySwitch | TNY263 | SMD-8 | Low-power adapter |
| TNY268PN | TinySwitch | TNY268 | PDIP-8 | Higher power variant |
| LNK302GN | LinkSwitch | LNK302 | SMD-8 | LED driver application |
| LNK364DN | LinkSwitch | LNK364 | DIP-8 | General purpose |
| INN2023K | InnoSwitch | INN2023 | eSOP-12 | Original InnoSwitch |
| INN3166C | InnoSwitch | INN3166 | InSOP-24 | InnoSwitch-CE |
| INN3673CJ | InnoSwitch3 | INN3673 | InSOP-24 | High-power InnoSwitch3 |
| PFS714EG | HiperPFS | PFS714 | eSIP-7 | PFC controller |
| PFS7624H | HiperPFS | PFS7624 | eSIP-7 | High-power PFC |
| LCS708HG | HiperLCS | LCS708 | eSIP-7 | LLC controller |
| CAP002DG | CAPZero | CAP002 | SMD-8 | X-cap discharge |
| SEN013DG | SENZero | SEN013 | SMD-8 | Current sensing |

---

## Related Files

- Handler: `manufacturers/PowerIntegrationsHandler.java`
- Component types: `ComponentType.IC`, `ComponentType.VOLTAGE_REGULATOR`

---

## Learnings & Quirks

- **Integrated MOSFET**: Many Power Integrations parts (TOPSwitch, TinySwitch, LinkSwitch) have an integrated high-voltage MOSFET, making them single-chip solutions
- **FluxLink Technology**: InnoSwitch family uses magnetic coupling for isolated feedback, eliminating optocouplers
- **Package suffix ordering**: Handler correctly checks 2-character suffixes (KG, YN, GN) before 1-character (K, Y, G)
- **Not true voltage regulators**: While classified as VOLTAGE_REGULATOR, these are SMPS controllers that perform AC-DC conversion, not linear regulators
- **Generation numbering**: The digit after the prefix (e.g., TOP**2**49, TNY**3**68) indicates product generation, not always strictly numeric progression
- **InnoSwitch3 vs InnoSwitch**: INN3xxx (4 digits after INN) is InnoSwitch3, while INN20xx/30xx/40xx (with leading 0) are earlier InnoSwitch generations
- **Package heatsink variants**: Suffix "G" after package letter (KG, HG, EG) typically indicates enhanced thermal performance with integrated heatsink
- **CAPZero/SENZero special**: These are auxiliary ICs (not SMPS) for power supply optimization - CAPZero discharges safety X-caps, SENZero provides lossless current sensing

<!-- Add new learnings above this line -->
