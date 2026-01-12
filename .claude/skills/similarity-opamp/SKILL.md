---
name: similarity-opamp
description: Use when working with op-amp similarity calculations - comparing op-amp MPNs, understanding single/dual/quad configurations, equivalent families like LM358/MC1458, or op-amp-specific similarity logic.
---

# Op-Amp Similarity Calculator Skill

Guidance for working with `OpAmpSimilarityCalculator` in the lib-electronic-components library.

## Overview

The `OpAmpSimilarityCalculator` compares operational amplifiers based on:
- **Equivalent families** - Known interchangeable parts
- **Channel count** - Single, dual, quad configurations
- **Characteristics** - Bandwidth, slew rate, input type

## Applicable Types

```java
ComponentType.OPAMP
ComponentType.OPAMP_TI
ComponentType.OPAMP_ANALOG_DEVICES
ComponentType.OPAMP_ST
// Any type starting with "OPAMP_"
```

## Similarity Thresholds

```java
HIGH_SIMILARITY = 0.9;   // Equivalent parts
MEDIUM_SIMILARITY = 0.7; // Same family, compatible
LOW_SIMILARITY = 0.3;    // Different family
```

## Equivalent Families

### Dual Op-Amps (2 channels)
| Family | Members |
|--------|---------|
| LM358 family | LM358, LM358A, LM358B, MC1458, RC1458 |
| TL072 family | TL072, TL072A, TL072B, TL072C |
| NE5532 family | NE5532, NE5532A, SA5532, SE5532 |

### Quad Op-Amps (4 channels)
| Family | Members |
|--------|---------|
| LM324 family | LM324, LM324A, LM324N, MC3403, RC3403 |
| TL074 family | TL074, TL074A, TL074B, TL074C |

### Single Op-Amps
| Family | Members |
|--------|---------|
| LM741 family | LM741, UA741, MC741, RC741 |

## Channel Count Rules

The calculator considers channel count when comparing:

| Comparison | Result |
|------------|--------|
| LM358 (dual) vs MC1458 (dual) | 0.9 (equivalent) |
| LM358 (dual) vs LM324 (quad) | 0.7 (same family, different count) |
| LM358 (dual) vs LM741 (single) | 0.7 (same technology, different count) |

## Package Suffix Handling

Common suffixes are normalized:
- `N`, `P` - DIP packages
- `D`, `DR` - SOIC packages
- `PW`, `PWR` - TSSOP packages
- `A`, `B` - Grade variants

```java
calculator.calculateSimilarity("LM358N", "LM358D", registry);
// Returns 0.9 (same op-amp, different package)
```

## Test Examples

```java
// Equivalent dual op-amps
calculator.calculateSimilarity("LM358", "MC1458", registry);
// Returns 0.9

// Same family, different grades
calculator.calculateSimilarity("LM324", "LM324A", registry);
// Returns 0.9

// Dual vs quad (different channel count)
calculator.calculateSimilarity("TL072", "TL074", registry);
// Returns 0.7

// Non-op-amp parts
calculator.calculateSimilarity("IRF530", "2N2222", registry);
// Returns 0.0
```

## Key Characteristics

The calculator may also consider:
- **GBW** (Gain-Bandwidth Product)
- **Slew Rate**
- **Input Type** (JFET vs BJT)
- **Rail-to-Rail** capability

---

## Learnings & Quirks

### Naming Conventions
- `LM` prefix: National Semiconductor (now TI)
- `MC` prefix: Motorola (now ON Semi)
- `TL` prefix: TI JFET-input
- `NE`/`SA`/`SE` prefix: Signetics (now TI)
- `UA`/`RC` prefix: Various second sources

### Family Relationships
- LM358/LM324: Same die, 358 is dual, 324 is quad
- TL072/TL074: JFET-input, 072 is dual, 074 is quad
- NE5532/NE5534: 5532 is dual, 5534 is single (lower noise)

### Cross-Manufacturer Equivalence
- LM358 ≈ MC1458 ≈ RC1458 (all dual, general purpose)
- LM324 ≈ MC3403 ≈ RC3403 (all quad, general purpose)

<!-- Add new learnings above this line -->
