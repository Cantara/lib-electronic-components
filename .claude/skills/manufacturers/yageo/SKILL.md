# Yageo Part Number Reference

## MPN Structure Overview

Yageo produces passive components:
- **RC**: Thick film chip resistors
- **CC**: Ceramic chip capacitors
- **AC**: Array resistors/capacitors

---

## RC Resistor Format

```
RC [Size] [Series] [Value] [Tolerance] [Packaging]
|    |       |        |         |           |
|    |       |        |         |           +-- Packaging code
|    |       |        |         +-- Tolerance (F=1%, J=5%)
|    |       |        +-- Resistance value (3-4 digit)
|    |       +-- Series (L, W, G, etc.)
|    +-- Case size (0402, 0603, 0805, etc.)
+-- Yageo resistor prefix
```

### Example Decoding

```
RC0603FR-07100KL
|  |  ||  | | ||
|  |  ||  | | |+-- Packaging (L=full reel)
|  |  ||  | | +-- Value (100K)
|  |  ||  | +-- Multiplier (07)
|  |  ||  +-- Reserved
|  |  |+-- Tolerance (F=1%)
|  |  +-- Series (R)
|  +-- Size (0603)
+-- Yageo chip resistor
```

---

## Size Codes

| Code | Imperial | Metric | Power Rating |
|------|----------|--------|--------------|
| 0201 | 0201 | 0603 | 1/20W |
| 0402 | 0402 | 1005 | 1/16W |
| 0603 | 0603 | 1608 | 1/10W |
| 0805 | 0805 | 2012 | 1/8W |
| 1206 | 1206 | 3216 | 1/4W |
| 1210 | 1210 | 3225 | 1/2W |
| 2010 | 2010 | 5025 | 3/4W |
| 2512 | 2512 | 6432 | 1W |

---

## Tolerance Codes

| Code | Tolerance |
|------|-----------|
| D | 0.5% |
| F | 1% |
| G | 2% |
| J | 5% |
| K | 10% |
| M | 20% |

---

## Supported ComponentTypes

```java
ComponentType.RESISTOR
ComponentType.RESISTOR_CHIP_YAGEO
ComponentType.CAPACITOR
ComponentType.CAPACITOR_CERAMIC_YAGEO
```

---

## Test Patterns

### Valid Resistor MPNs

```
RC0603FR-07100KL
RC0402JR-071KL
RC0805FR-0710KL
RC1206FR-07100RL
```

### Valid Capacitor MPNs

```
CC0603KRX7R8BB104
CC0402JRNPO9BN100
```

---

## Related Files

- Handler: `manufacturers/YageoHandler.java`
- Test: `handlers/YageoHandlerTest.java`

<!-- Add new learnings above this line -->
