---
name: fairchild
description: Fairchild Semiconductor (now ON Semiconductor) MPN encoding patterns, package codes, and handler guidance. Use when working with Fairchild MOSFETs, transistors, diodes, or FairchildHandler.
---

# Fairchild Semiconductor Manufacturer Skill

## Company Overview

Fairchild Semiconductor was a pioneering semiconductor company founded in 1957, known for inventing the planar process and helping create Silicon Valley. The company was acquired by ON Semiconductor in 2016. Fairchild was particularly renowned for:

- **Power MOSFETs**: FQP, FQD, FQU, FQL, FDB, FDA series
- **Small signal transistors**: 2N series, BS series
- **Power management**: Voltage regulators
- **Discrete semiconductors**: Diodes, transistors

**Note**: Many Fairchild part numbers are still in production under ON Semiconductor branding. The FairchildHandler in this library handles the legacy Fairchild nomenclature.

---

## MPN Structure by Product Family

### MOSFETs - FQP/FQD/FQU Series (Main Power MOSFETs)

```
FQ[PKG][CURRENT][CHANNEL][VOLTAGE][SUFFIX]
 |   |     |        |        |       |
 |   |     |        |        |       +-- Optional: L=Logic level
 |   |     |        |        +-- Voltage / 10 (e.g., 06=60V, 10=100V, 20=200V)
 |   |     |        +-- N=N-channel, P=P-channel
 |   |     +-- Current rating in Amps
 |   +-- Package: P=TO-220, D=DPAK, U=IPAK
 +-- FQ = Fairchild QFET series
```

**Package Codes in FQ Series**:
| Prefix | Package | Description |
|--------|---------|-------------|
| FQP | TO-220 | Standard through-hole power package |
| FQD | DPAK (TO-252) | Surface mount, medium power |
| FQU | IPAK (TO-251) | Surface mount, I-PAK |
| FQL | TO-220 | Logic-level series |

**Examples**:
- `FQP30N06` = TO-220, 30A, N-channel, 60V
- `FQP50N06L` = TO-220, 50A, N-channel, 60V, Logic Level
- `FQP27P06` = TO-220, 27A, P-channel, 60V
- `FQD13N10` = DPAK, 13A, N-channel, 100V
- `FQU11P06` = IPAK, 11A, P-channel, 60V

### MOSFETs - FDS Series (SO-8 Package)

```
FDS[CODE][CHANNEL]
 |    |      |
 |    |      +-- N implied for N-channel, P for P-channel
 |    +-- Device code (performance/voltage identifier)
 +-- FDS = Fairchild Discrete SO-8
```

**Examples**:
- `FDS6680A` = N-channel, SO-8, 30V, enhanced
- `FDS9926A` = Dual N-channel, SO-8
- `FDS4141P` = P-channel, SO-8

### MOSFETs - FDB/FDA Series (PowerTrench Technology)

```
FD[TYPE][CODE]
 |   |     |
 |   |     +-- Device code (identifies voltage/current class)
 |   +-- B=D2PAK, A=Complementary pair
 +-- FD = Fairchild PowerTrench
```

**Examples**:
- `FDB3632` = D2PAK, PowerTrench, 100V, 73A
- `FDB045AN06` = D2PAK, N-channel, 60V, 80A
- `FDA24N40F` = Complementary pair

### MOSFETs - FQL Series (Logic Level)

```
FQL[CODE]
 |    |
 |    +-- Device code
 +-- FQL = Fairchild QFET Logic Level
```

The FQL series features lower gate threshold voltage (~2.5V max Vgs) for direct logic-level drive.

**Examples**:
- `FQL40N50` = Logic level, 40A, 500V

### Small Signal MOSFETs - 2N7xxx Series

```
2N7[CODE][SUFFIX]
 |    |      |
 |    |      +-- Grade suffix (A, B, etc.)
 |    +-- JEDEC registration number
 +-- 2N = JEDEC transistor prefix
```

**Examples**:
- `2N7000` = N-channel, TO-92, 60V, 200mA
- `2N7002` = N-channel, SOT-23, 60V, 115mA

### Small Signal MOSFETs - BS Series

```
BS[CODE][SUFFIX]
 |   |      |
 |   |      +-- Package/variant suffix
 |   +-- Device code
 +-- BS = Small signal MOSFET
```

**Examples**:
- `BS170` = N-channel, TO-92, 60V, 500mA
- `BSS138` = N-channel, SOT-23, 50V, 200mA

---

## Supported Component Types

From `FairchildHandler.getSupportedTypes()`:

| ComponentType | Description |
|---------------|-------------|
| `MOSFET` | Power and small signal MOSFETs |
| `TRANSISTOR` | Bipolar transistors |
| `DIODE` | Rectifier and signal diodes |
| `VOLTAGE_REGULATOR` | Linear voltage regulators |

**Note**: The handler uses standard `ComponentType` enum values, not manufacturer-specific types (e.g., `MOSFET_FAIRCHILD`). This is because Fairchild is now part of ON Semiconductor.

---

## Package Codes

### Through-Hole Packages

| Code | Package | Description |
|------|---------|-------------|
| N | TO-220 | Standard TO-220 |
| TA | TO-220F | Fully isolated TO-220 |
| TU | TO-251 | I-PAK through-hole |

### Surface Mount Packages

| Code | Package | Description |
|------|---------|-------------|
| S | D2PAK (TO-263) | High power SMD |
| L | DPAK (TO-252) | Medium power SMD |
| F | IPAK (TO-251) | I-PAK surface mount |
| U | SOT-223 | Small outline power |
| G | SOT-223 | Alternate code |

### Package Extraction from Prefix

The handler extracts package type from the MPN prefix:

| Prefix | Returns | Package Type |
|--------|---------|--------------|
| FQP | TO-220 | Standard power through-hole |
| FQD | DPAK | Surface mount power |
| FQU | IPAK | I-PAK surface mount |
| FDS | SO-8 | Small outline 8-pin |

---

## Series Extraction Rules

The handler extracts series using regex patterns:

| MPN Pattern | Series Extraction | Example |
|-------------|-------------------|---------|
| `FQP[digits][N/P][digits]` | Full base part | FQP30N06 -> FQP30N06 |
| `FQD[digits][N/P][digits]` | Full base part | FQD13N10 -> FQD13N10 |
| `FQU[digits][N/P][digits]` | Full base part | FQU11P06 -> FQU11P06 |
| `FDS[digits]` | FDS + digits | FDS6680A -> FDS6680 |
| `FQL[digits]` | FQL + digits | FQL40N50 -> FQL40 |
| `2N7[digits]` | 2N7 + digits | 2N7000 -> 2N7000 |

**Series Extraction Code Logic**:
```java
// FQP, FQD, FQU series - extract up to N/P + voltage
"FQP30N06L" -> regex "([A-Z0-9]+[NP][0-9]+).*" -> "FQP30N06"

// FDS series - extract prefix + digits only
"FDS6680A" -> regex "(FDS[0-9]+).*" -> "FDS6680"

// FQL series - extract prefix + digits
"FQL40N50" -> regex "(FQL[0-9]+).*" -> "FQL40"

// 2N7 series - extract full JEDEC number
"2N7000" -> regex "(2N7[0-9]+).*" -> "2N7000"
```

---

## Pattern Matching

### Registered Patterns in `initializePatterns()`

**N-Channel MOSFETs**:
| Pattern | Description |
|---------|-------------|
| `^FQP[0-9].*N[0-9].*` | TO-220 N-channel |
| `^FQD[0-9].*N[0-9].*` | DPAK N-channel |
| `^FQU[0-9].*N[0-9].*` | IPAK N-channel |
| `^FDS[0-9].*` | SO-8 N-channel |

**P-Channel MOSFETs**:
| Pattern | Description |
|---------|-------------|
| `^FQP[0-9].*P[0-9].*` | TO-220 P-channel |
| `^FQD[0-9].*P[0-9].*` | DPAK P-channel |
| `^FQU[0-9].*P[0-9].*` | IPAK P-channel |
| `^FDS[0-9].*P.*` | SO-8 P-channel |

**Other MOSFET Families**:
| Pattern | Description |
|---------|-------------|
| `^FQL[0-9].*` | Logic Level series |
| `^2N7[0-9].*` | Small signal 2N7xxx |
| `^BS[0-9].*` | BS series small signal |
| `^FDB[0-9].*` | PowerTrench series |
| `^FDA[0-9].*` | Complementary pairs |

---

## Official Replacement Logic

The `isOfficialReplacement()` method determines if two parts are interchangeable:

### Same Series Check
```java
// Same series + same package = direct replacement
FQP30N06 <-> FQP30N06L (same series, same package)

// Same series + compatible package = replacement
FQP30N06 <-> FQD30N06 (same series, compatible packages)
```

### Compatible Packages
- TO-220 variants (TO-220, TO-220F) are compatible
- DPAK and D2PAK are often compatible
- SOT-223 and DPAK can be compatible

### Known Cross-References
```java
FQP30N06 <-> IRF530 (equivalent N-channel 60V MOSFETs)
```

---

## Example MPNs with Full Decoding

### FQP50N06L
```
FQP50N06L
│  │  │ │└── L = Logic Level (low Vgs threshold)
│  │  │ └── 06 = 60V (06 x 10)
│  │  └── N = N-channel
│  └── 50 = 50A drain current
└── FQP = TO-220 package

Specs: TO-220, N-channel, 60V, 50A, Logic Level
```

### FQD13N10
```
FQD13N10
│  │  │ │
│  │  │ └── 10 = 100V (10 x 10)
│  │  └── N = N-channel
│  └── 13 = 13A drain current
└── FQD = DPAK package

Specs: DPAK, N-channel, 100V, 13A
```

### FQP27P06
```
FQP27P06
│  │  │ │
│  │  │ └── 06 = 60V
│  │  └── P = P-channel
│  └── 27 = 27A drain current
└── FQP = TO-220 package

Specs: TO-220, P-channel, 60V, 27A
```

### FDS6680A
```
FDS6680A
│  │    │
│  │    └── A = Improved version/grade
│  └── 6680 = Device code (identifies 30V, 10.5A specs)
└── FDS = SO-8 package

Specs: SO-8, N-channel, 30V, 10.5A
```

### 2N7000
```
2N7000
│ │   │
│ │   └── 000 = JEDEC sequence number
│ └── 7 = Series identifier (70xx = small signal MOSFETs)
└── 2N = JEDEC transistor prefix

Specs: TO-92, N-channel enhancement MOSFET, 60V, 200mA
```

---

## Common Part Numbers Reference

### Popular N-Channel MOSFETs

| Part Number | Vds | Id | Rds(on) | Package |
|-------------|-----|-----|---------|---------|
| FQP30N06L | 60V | 30A | 35mOhm | TO-220 |
| FQP50N06L | 60V | 50A | 22mOhm | TO-220 |
| FQP30N06 | 60V | 30A | 45mOhm | TO-220 |
| FQD13N10 | 100V | 13A | 120mOhm | DPAK |
| FQD7N20L | 200V | 7A | 300mOhm | DPAK |
| FDS6680A | 30V | 10.5A | 12mOhm | SO-8 |
| 2N7000 | 60V | 200mA | 5Ohm | TO-92 |
| 2N7002 | 60V | 115mA | 7.5Ohm | SOT-23 |

### Popular P-Channel MOSFETs

| Part Number | Vds | Id | Rds(on) | Package |
|-------------|-----|-----|---------|---------|
| FQP27P06 | -60V | -27A | 70mOhm | TO-220 |
| FQP47P06 | -60V | -47A | 26mOhm | TO-220 |
| FQD11P06 | -60V | -11A | 100mOhm | DPAK |

### PowerTrench Series

| Part Number | Vds | Id | Package |
|-------------|-----|-----|---------|
| FDB3632 | 100V | 73A | D2PAK |
| FDB045AN06 | 60V | 80A | D2PAK |

---

## Handler Implementation Notes

### Quirks in Current Implementation

1. **STM32 code in extractPackageCode()**: Lines 115-126 contain STM32-specific extraction logic that appears to be copy-paste error from STHandler. This code path is never reached for Fairchild MPNs.

2. **HashSet in getSupportedTypes()**: Uses mutable `HashSet` instead of immutable `Set.of()`. Should be updated for consistency with other handlers.

3. **Limited transistor/diode patterns**: The handler only registers MOSFET patterns in `initializePatterns()`. No patterns for transistors or diodes despite declaring them in `getSupportedTypes()`.

4. **Cross-handler pattern matching**: Uses `patterns.matchesForCurrentHandler()` to avoid false matches from other handlers' patterns (good practice).

### Recommended Improvements

```java
// Change getSupportedTypes() from HashSet to Set.of()
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.MOSFET,
        ComponentType.TRANSISTOR,
        ComponentType.DIODE,
        ComponentType.VOLTAGE_REGULATOR
    );
}

// Add missing transistor patterns
registry.addPattern(ComponentType.TRANSISTOR, "^2N[2-6][0-9]{3}.*");
registry.addPattern(ComponentType.TRANSISTOR, "^BC[0-9]{3}.*");

// Add missing diode patterns
registry.addPattern(ComponentType.DIODE, "^1N[0-9]{4}.*");
```

---

## Related Files

- Handler: `manufacturers/FairchildHandler.java`
- ON Semiconductor handler: `manufacturers/OnSemiHandler.java` (parent company)
- Component types: Uses standard `MOSFET`, `TRANSISTOR`, `DIODE`, `VOLTAGE_REGULATOR`

---

## Cross-Reference to ON Semiconductor

Since Fairchild was acquired by ON Semiconductor in 2016, many part number series overlap:

| Fairchild Series | ON Semi Equivalent | Notes |
|-----------------|-------------------|-------|
| FQP/FQD/FQU | Still FQP/FQD/FQU | Retained branding |
| FDS | Still FDS | Retained branding |
| 2N7xxx | 2N7xxx | JEDEC standard |
| FDB/FDA | Still FDB/FDA | PowerTrench retained |

For newer designs, check ON Semiconductor's current product lineup as some Fairchild parts may be NRFND or obsolete.

---

## Learnings & Edge Cases

- **Logic Level suffix**: "L" at end of MPN indicates logic-level gate threshold (~2.5V Vgs max), critical for 3.3V/5V logic drive
- **Voltage encoding**: Voltage divided by 10 in FQ series (06=60V, 10=100V, 20=200V)
- **Current in part number**: Current rating directly encoded (FQP**30**N06 = 30A)
- **Channel polarity**: N/P letter in middle of MPN determines N-channel vs P-channel
- **SO-8 code differences**: FDS series numbers encode specs, not straightforward voltage/current decode
- **Package from prefix**: FQP=TO-220, FQD=DPAK, FQU=IPAK is reliable extraction method
- **Cross-reference with IRF**: FQP30N06 is often cross-referenced with IRF530 (both 60V N-channel)
- **STM32 code anomaly**: extractPackageCode() has unreachable STM32-specific code (copy-paste artifact)
- **PowerTrench branding**: FDB/FDA series use Fairchild's proprietary PowerTrench technology for lower Rds(on)

<!-- Add new learnings above this line -->
