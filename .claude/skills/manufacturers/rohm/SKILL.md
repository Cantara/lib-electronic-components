# Rohm Part Number Reference

## MPN Structure Overview

Rohm produces various semiconductor and passive components:
- **MCR/KTR**: Chip resistors
- **BH**: Power management ICs
- **BD**: Audio/motor driver ICs
- **RQ**: MOSFETs
- **SML**: LEDs

---

## Chip Resistor Format (MCR/KTR Series)

```
MCR [Size] [Series] [Value] [Tolerance] [Packaging]
 |    |       |        |         |           |
 |    |       |        |         |           +-- Packaging code
 |    |       |        |         +-- Tolerance (F=1%, J=5%)
 |    |       |        +-- Resistance value
 |    |       +-- Series code
 |    +-- Case size
 +-- Rohm resistor prefix
```

---

## Power Management (BH Series)

| Prefix | Type |
|--------|------|
| BH | Voltage regulators, battery management |
| BD | Audio amps, motor drivers |
| BU | LED drivers |

---

## MOSFET (RQ Series)

| Prefix | Type |
|--------|------|
| RQ | Standard MOSFETs |
| RQK | Low-on-resistance |
| RQP | Power MOSFETs |

---

## LED Series

| Series | Type |
|--------|------|
| SML | Standard SMD LEDs |
| SMLP | High power |
| SMLD | Side-emitting |

---

## Size Codes (Resistors)

| Code | Imperial | Metric |
|------|----------|--------|
| 01 | 0201 | 0603 |
| 03 | 0402 | 1005 |
| 10 | 0603 | 1608 |
| 18 | 0805 | 2012 |
| 25 | 1206 | 3216 |

---

## Supported ComponentTypes

```java
ComponentType.RESISTOR
ComponentType.RESISTOR_CHIP_ROHM
ComponentType.MOSFET
ComponentType.MOSFET_ROHM
ComponentType.LED
ComponentType.LED_STANDARD_ROHM
ComponentType.VOLTAGE_REGULATOR
ComponentType.VOLTAGE_REGULATOR_ROHM
```

---

## Test Patterns

### Valid Resistor MPNs

```
MCR10EZPF1001
MCR03EZPFX1000
KTR03EZPF1001
```

### Valid IC MPNs

```
BH1750FVI-TR
BD9215AFV
BU9480F
```

### Valid MOSFET MPNs

```
RQ3E050BNTB
RQK0305CBDTB
```

### Valid LED MPNs

```
SML-LX0603IW
SMLP12BC8W1
```

---

## Related Files

- Handler: `manufacturers/RohmHandler.java`
- Test: `handlers/RohmHandlerTest.java`

<!-- Add new learnings above this line -->
