# Kemet Part Number Reference

## MPN Structure Overview

Kemet produces various capacitor types:
- **C**: MLCC ceramic capacitors
- **T**: Tantalum capacitors
- **F**: Film capacitors
- **A**: Aluminum electrolytic capacitors

---

## MLCC Format (C Series)

```
C [Size] [Temp] [Voltage] [Value] [Tolerance] [Packaging]
|   |      |       |         |         |           |
|   |      |       |         |         |           +-- Packaging
|   |      |       |         |         +-- Tolerance (K=10%, M=20%)
|   |      |       |         +-- Capacitance value
|   |      |       +-- Voltage rating
|   |      +-- Temperature characteristic
|   +-- Case size (0402, 0603, 0805)
+-- MLCC prefix
```

### Example

```
C0603C104K5RACTU
|  |  | | ||  ||
|  |  | | ||  |+-- Tube packaging
|  |  | | ||  +-- Reserved
|  |  | | |+-- Tolerance (K=10%)
|  |  | | +-- Reserved
|  |  | +-- Value (104=100nF)
|  |  +-- Voltage (5=50V)
|  +-- Temp (C=C0G)
+-- Size (0603)
```

---

## Tantalum Format (T Series)

```
T [Size] [Case] [Voltage] [Value] [Tolerance] [ESR]
```

---

## Size Codes

| Code | Imperial | Metric |
|------|----------|--------|
| 0402 | 0402 | 1005 |
| 0603 | 0603 | 1608 |
| 0805 | 0805 | 2012 |
| 1206 | 1206 | 3216 |

---

## Supported ComponentTypes

```java
ComponentType.CAPACITOR
ComponentType.CAPACITOR_CERAMIC_KEMET
ComponentType.CAPACITOR_TANTALUM_KEMET
ComponentType.CAPACITOR_FILM_KEMET
```

---

## Test Patterns

### Valid MLCC MPNs

```
C0603C104K5RACTU
C0402C103K4RACTU
C0805C106K8PACTU
```

### Valid Tantalum MPNs

```
T491A106K016AT
T491B476M010AT
```

---

## Related Files

- Handler: `manufacturers/KemetHandler.java`
- Test: `handlers/KemetHandlerTest.java`

<!-- Add new learnings above this line -->
