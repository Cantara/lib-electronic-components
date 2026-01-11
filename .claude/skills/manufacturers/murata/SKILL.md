# Murata Part Number Reference

## MPN Structure Overview

Murata part numbers follow a structured format that varies by product family:
- **GRM/GCM**: Multilayer Ceramic Chip Capacitors (MLCC)
- **LQM/LQW/LQG**: Chip Inductors
- **BLM**: Ferrite Beads
- **NFM**: EMI Suppression Filters
- **DFE**: Power Inductors
- **DLW**: Common Mode Choke Coils

---

## MLCC Capacitor Format (GRM/GCM Series)

```
GRM [Size] [Temp] [Voltage] [Value] [Tolerance] [Packaging]
 |    |      |       |        |         |           |
 |    |      |       |        |         |           +-- Packaging code
 |    |      |       |        |         +-- Tolerance (J=5%, K=10%, M=20%)
 |    |      |       |        +-- Capacitance value
 |    |      |       +-- Voltage rating code
 |    |      +-- Temperature characteristic
 |    +-- Case size (188=0603, 155=0402, 21=0805)
 +-- Series (GRM=Standard, GCM=Automotive)
```

### Example Decoding

```
GRM188R71H104KA93D
 |   |  | |  |  ||+-- Packaging (D=7" reel)
 |   |  | |  |  |+-- Reserved
 |   |  | |  |  +-- Tolerance (K=10%)
 |   |  | |  +-- Capacitance (104=100nF)
 |   |  | +-- Voltage (1H=50V)
 |   |  +-- Temperature characteristic (R7=X7R)
 |   +-- Case size (188=0603)
 +-- Series (GRM=Standard MLCC)
```

---

## Size Codes (Case Size)

| Code | Metric | Imperial | Notes |
|------|--------|----------|-------|
| 033 | 0201 | 01005 | Ultra-small |
| 055 | 0402 | 0201 | Very small |
| 155 | 0402 | 0201 | Standard |
| 188 | 0603 | 0402 | Common |
| 21 | 0805 | 0402 | Standard |
| 31 | 1206 | 0603 | Common |
| 32 | 1210 | 0805 | Large |
| 55 | 2220 | 1210 | High capacity |

---

## Temperature Characteristics

| Code | Characteristic | Temp Range | Tolerance |
|------|----------------|------------|-----------|
| C0 | C0G/NPO | -55C to +125C | 0 ppm/C |
| R7 | X7R | -55C to +125C | +/-15% |
| R6 | X5R | -55C to +85C | +/-15% |
| F1 | Y5V | -30C to +85C | +22%/-82% |

---

## Voltage Rating Codes

| Code | Voltage |
|------|---------|
| 0J | 6.3V |
| 1C | 16V |
| 1E | 25V |
| 1H | 50V |
| 2A | 100V |
| 2E | 250V |
| 2J | 630V |

---

## Inductor Series (LQx)

### LQM - Metal Power Inductors

```
LQM [Size] [Type] [Value] [Tolerance] [Packaging]
```

### LQW - Wire Wound Inductors

```
LQW [Size] [Type] [Value] [Tolerance] [Packaging]
```

### LQG - RF Chip Inductors

```
LQG [Size] [Type] [Value] [Tolerance] [Packaging]
```

---

## EMI Components

### BLM - Ferrite Beads

```
BLM [Size] [Series] [Impedance] [Current] [Packaging]
```

### NFM - EMI Filters

```
NFM [Size] [Type] [Cutoff] [Rating] [Packaging]
```

---

## Handler Implementation Notes

### Package Code Extraction

The handler extracts size codes from positions 3-6 for GRM/GCM series:

```java
// GRM/GCM series
if (mpn.startsWith("GRM") || mpn.startsWith("GCM")) {
    return mpn.substring(3, 6);  // Size code like "188", "155"
}

// LQx series inductors
if (mpn.matches("^LQ[MGW].*")) {
    return mpn.substring(3, 7);  // Size code like "2012"
}
```

### Series Extraction

```java
// GRM/GCM/KC series capacitors
if (mpn.matches("^(?:GRM|GCM|KC[ABMZ]).*")) {
    return mpn.substring(0, 6);  // Include size code
}

// LQx series inductors
if (mpn.matches("^LQ[MGW].*")) {
    return mpn.substring(0, 7);  // Include size code
}
```

### Supported ComponentTypes

```java
ComponentType.CAPACITOR
ComponentType.CAPACITOR_CERAMIC_MURATA
ComponentType.INDUCTOR
ComponentType.INDUCTOR_CHIP_MURATA
ComponentType.INDUCTOR_POWER_MURATA
ComponentType.EMI_FILTER_MURATA
ComponentType.COMMON_MODE_CHOKE_MURATA
```

---

## Known Handler Issues

### 1. HashSet in getSupportedTypes()

Uses mutable `HashSet` instead of `Set.of()`:
```java
Set<ComponentType> types = new HashSet<>();  // Should be Set.of()
```

### 2. Pattern Anchoring

Some patterns use `$` anchor which may miss parts with additional suffixes.

### 3. Limited Series Coverage

Only covers GRM, GCM, KC, LQM, LQW, LQG, DFE, BLM, NFM, DLW series.
Missing resonators, sensors, and other Murata products.

---

## Test Patterns

### Valid MLCC MPNs

```
GRM188R71H104KA93D
GRM155R71C104KA88D
GCM188R71H104KA93D
GRM21BR71A105KA73L
GRM31CR71H475KA88L
```

### Valid Inductor MPNs

```
LQM2MPN2R2MG0L
LQW18AN2N2D00D
LQG15HS2N2S02D
DFE201612P-1R0M
```

### Valid EMI MPNs

```
BLM15AG121SN1D
NFM41PC104R1E3L
DLW5BTM102SQ2L
```

---

## Related Files

- Handler: `manufacturers/MurataHandler.java`
- Component types: CAPACITOR_CERAMIC_MURATA, INDUCTOR_CHIP_MURATA, INDUCTOR_POWER_MURATA, EMI_FILTER_MURATA, COMMON_MODE_CHOKE_MURATA
- Test: `handlers/MurataHandlerTest.java`

---

## Learnings & Edge Cases

- GCM series is automotive grade, same structure as GRM
- KC series is high voltage MLCC with slightly different prefix pattern
- Inductor size codes are 4 digits (e.g., 2012) vs capacitor 3 digits (e.g., 188)
- Packaging codes vary: D=7" reel, J=13" reel, B=bulk, etc.
- Temperature characteristics determine voltage derating behavior

<!-- Add new learnings above this line -->
