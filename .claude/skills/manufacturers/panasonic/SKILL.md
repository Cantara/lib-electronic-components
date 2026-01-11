# Panasonic Part Number Reference

## MPN Structure Overview

Panasonic produces various passive components:
- **EEE/EEH/EEU**: Aluminum electrolytic capacitors
- **ERJ**: Thick film chip resistors
- **EVQ**: Switches/tactile buttons
- **ELP**: Inductors

---

## Electrolytic Capacitor Format (EEE Series)

```
EEE [Temp] [Size] [Voltage] [Value] [Series]
 |    |      |       |         |        |
 |    |      |       |         |        +-- Series code
 |    |      |       |         +-- Capacitance value
 |    |      |       +-- Voltage rating
 |    |      +-- Case size code
 |    +-- Temperature rating
 +-- Panasonic electrolytic prefix
```

---

## ERJ Resistor Format

```
ERJ [Size] [Series] [Value] [Tolerance]
 |    |       |        |         |
 |    |       |        |         +-- Tolerance code
 |    |       |        +-- Resistance value
 |    |       +-- Series code
 |    +-- Case size (3, 6, 8, etc.)
 +-- Panasonic resistor prefix
```

---

## Size Codes

### Capacitors

| Code | Size | Diameter |
|------|------|----------|
| 1H | 6.3x5.8mm | 6.3mm |
| 1V | 6.3x7.7mm | 6.3mm |
| 2A | 8x10mm | 8mm |

### Resistors (ERJ)

| Code | Imperial | Metric |
|------|----------|--------|
| 3 | 0603 | 1608 |
| 6 | 0805 | 2012 |
| 8 | 1206 | 3216 |

---

## Supported ComponentTypes

```java
ComponentType.CAPACITOR
ComponentType.CAPACITOR_ELECTROLYTIC_PANASONIC
ComponentType.RESISTOR
ComponentType.RESISTOR_CHIP_PANASONIC
ComponentType.INDUCTOR
ComponentType.INDUCTOR_CHIP_PANASONIC
```

---

## Test Patterns

### Valid Capacitor MPNs

```
EEEHA1V470P
EEEHD1H100P
EEU-FC1E101
```

### Valid Resistor MPNs

```
ERJ-3EKF1001V
ERJ-6ENF1000V
ERJ-8ENF1002V
```

---

## Related Files

- Handler: `manufacturers/PanasonicHandler.java`
- Test: `handlers/PanasonicHandlerTest.java`

<!-- Add new learnings above this line -->
