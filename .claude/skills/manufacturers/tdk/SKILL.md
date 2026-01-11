# TDK Part Number Reference

## MPN Structure Overview

TDK produces various passive and magnetic components:
- **C**: MLCC ceramic capacitors
- **MLZ**: Inductors
- **ACM**: Common mode filters
- **MPZ**: Ferrite beads

---

## MLCC Format (C Series)

```
C [Size] [Series] [Voltage] [Value] [Tolerance] [Packaging]
|   |       |         |        |         |           |
|   |       |         |        |         |           +-- Packaging
|   |       |         |        |         +-- Tolerance
|   |       |         |        +-- Capacitance value
|   |       |         +-- Voltage rating
|   |       +-- Series code (G, K, etc.)
|   +-- Case size
+-- TDK capacitor prefix
```

### Example Decoding

```
C1005X5R1C104K050BC
|  | | ||  | | |  |
|  | | ||  | | |  +-- Packaging
|  | | ||  | | +-- Size variant
|  | | ||  | +-- Tolerance (K=10%)
|  | | ||  +-- Value (104=100nF)
|  | | |+-- Voltage (1C=16V)
|  | | +-- Reserved
|  | +-- Temp (X5R)
|  +-- Series
+-- Size (1005=0402 metric)
```

---

## Size Codes

| Code | Metric | Imperial |
|------|--------|----------|
| 0603 | 0603 | 0201 |
| 1005 | 1005 | 0402 |
| 1608 | 1608 | 0603 |
| 2012 | 2012 | 0805 |
| 3216 | 3216 | 1206 |
| 3225 | 3225 | 1210 |

---

## Inductor Series

| Series | Type |
|--------|------|
| MLZ | Power inductors |
| MLF | RF inductors |
| MCL | Multilayer inductors |

---

## EMI Components

| Series | Type |
|--------|------|
| ACM | Common mode filters |
| MPZ | Ferrite beads |
| MLP | EMI filters |

---

## Supported ComponentTypes

```java
ComponentType.CAPACITOR
ComponentType.CAPACITOR_CERAMIC_TDK
ComponentType.INDUCTOR
ComponentType.INDUCTOR_CHIP_TDK
ComponentType.EMI_FILTER_TDK
ComponentType.COMMON_MODE_CHOKE_TDK
```

---

## Test Patterns

### Valid MLCC MPNs

```
C1005X5R1C104K050BC
C1608X7R1E104K080AA
C2012X5R1C106K125AB
```

### Valid Inductor MPNs

```
MLZ2012N100LT000
MLF2012A1R0KT000
```

---

## Related Files

- Handler: `manufacturers/TDKHandler.java`
- Test: `handlers/TDKHandlerTest.java`

<!-- Add new learnings above this line -->
