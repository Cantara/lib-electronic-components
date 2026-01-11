# Samsung Electro-Mechanics Part Number Reference

## MPN Structure Overview

Samsung Electro-Mechanics produces various passive components:
- **CL**: MLCC ceramic capacitors
- **RC**: Chip resistors
- **CIL**: Chip inductors

---

## MLCC Format (CL Series)

```
CL [Size] [Temp] [Voltage] [Value] [Tolerance] [Packaging]
|    |      |       |         |         |           |
|    |      |       |         |         |           +-- Packaging code
|    |      |       |         |         +-- Tolerance (K=10%, M=20%)
|    |      |       |         +-- Capacitance value (3-digit)
|    |      |       +-- Voltage rating code
|    |      +-- Temperature characteristic
|    +-- Case size code
+-- Samsung MLCC prefix
```

### Example Decoding

```
CL10B104KB8NNNC
|  | | | ||    |
|  | | | ||    +-- Packaging
|  | | | |+-- Reserved
|  | | | +-- Tolerance (K=10%)
|  | | +-- Value (104=100nF)
|  | +-- Voltage (B=50V)
|  +-- Temp characteristic (B=X7R)
+-- Size (10=0603)
```

---

## Size Codes

| Code | Imperial | Metric |
|------|----------|--------|
| 03 | 0201 | 0603 |
| 05 | 0402 | 1005 |
| 10 | 0603 | 1608 |
| 21 | 0805 | 2012 |
| 31 | 1206 | 3216 |
| 32 | 1210 | 3225 |

---

## Temperature Characteristic Codes

| Code | Characteristic |
|------|----------------|
| A | C0G/NP0 |
| B | X7R |
| F | Y5V |
| R | X5R |

---

## Voltage Codes

| Code | Voltage |
|------|---------|
| 5 | 6.3V |
| 6 | 10V |
| 7 | 16V |
| 8 | 25V |
| 9 | 35V |
| A | 50V |
| B | 50V (X7R) |
| C | 100V |

---

## Supported ComponentTypes

```java
ComponentType.CAPACITOR
ComponentType.CAPACITOR_CERAMIC_SAMSUNG
ComponentType.INDUCTOR
ComponentType.INDUCTOR_CHIP_SAMSUNG
ComponentType.RESISTOR
ComponentType.RESISTOR_CHIP_SAMSUNG
```

---

## Test Patterns

### Valid MLCC MPNs

```
CL10B104KB8NNNC
CL05B104KO5NNNC
CL21B106KOQNNNE
CL31B106KPHNNNE
```

---

## Related Files

- Handler: `manufacturers/SamsungHandler.java`
- Test: `handlers/SamsungHandlerTest.java`

<!-- Add new learnings above this line -->
