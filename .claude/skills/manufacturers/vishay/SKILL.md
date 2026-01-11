# Vishay Part Number Reference

## MPN Structure Overview

Vishay produces a wide range of passive and semiconductor components:
- **CRCW**: Thick film chip resistors
- **SI**: MOSFETs
- **1N**: Diodes (rectifiers, signal, zener)
- **2N/BC**: Transistors
- **BAT/BYV/BZX**: Specialty diodes
- **VLMU/VLMS**: LEDs

---

## CRCW Resistor Format

```
CRCW [Size] [Value] [Tolerance] [Packaging]
  |    |       |        |           |
  |    |       |        |           +-- Packaging code
  |    |       |        +-- Tolerance (F=1%, J=5%)
  |    |       +-- Resistance value (e.g., 100R, 10K)
  |    +-- Case size (0402, 0603, 0805, etc.)
  +-- Series prefix
```

### Example Decoding

```
CRCW0603100RFKEA
 |   |   |  ||+-- Packaging
 |   |   |  |+-- Reserved
 |   |   |  +-- Tolerance (F=1%)
 |   |   +-- Value (100R = 100 ohms)
 |   +-- Size (0603)
 +-- Series (CRCW = Thick Film Chip Resistor)
```

---

## Size Codes (Resistors)

| Code | Imperial | Metric | Power Rating |
|------|----------|--------|--------------|
| 0402 | 0402 | 1005 | 1/16W |
| 0603 | 0603 | 1608 | 1/10W |
| 0805 | 0805 | 2012 | 1/8W |
| 1206 | 1206 | 3216 | 1/4W |
| 1210 | 1210 | 3225 | 1/2W |
| 2010 | 2010 | 5025 | 3/4W |
| 2512 | 2512 | 6432 | 1W |

---

## MOSFET Series (SI)

| Prefix | Description |
|--------|-------------|
| SI | Standard power MOSFETs |
| SIS | Super low on-resistance |
| SIR | Standard TO package |
| SIB | Bottom drain |
| SIH | High voltage |
| SIHF | High voltage, fast switching |

---

## Diode Families

### Rectifiers

| Series | Voltage Range | Type |
|--------|---------------|------|
| 1N4001-1N4007 | 50V-1000V | Standard rectifier |
| 1N5400-1N5408 | 50V-1000V | High current rectifier |

### Signal Diodes

| Part | Description |
|------|-------------|
| 1N4148 | Fast switching |
| 1N914 | Fast switching |

### Schottky

| Series | Description |
|--------|-------------|
| BAT41-BAT85 | Low forward voltage |

### Zener

| Series | Description |
|--------|-------------|
| BZX series | Various voltages |
| 1N47xx | Standard zeners |

---

## Package Codes

### Diodes

| Code | Package |
|------|---------|
| DO-41 | Through-hole axial |
| DO-15 | Larger axial |
| SOD-123 | SMD small |
| SOD-323 | SMD mini |

### MOSFETs

| Code | Package |
|------|---------|
| DH | PowerPAK SO-8 |
| FH | PowerPAK SO-8 Dual |
| CH | PowerPAK 1212-8 |
| JH | PowerPAK SC-70 |

---

## Supported ComponentTypes

```java
ComponentType.RESISTOR
ComponentType.RESISTOR_CHIP_VISHAY
ComponentType.RESISTOR_THT_VISHAY
ComponentType.MOSFET
ComponentType.MOSFET_VISHAY
ComponentType.DIODE
ComponentType.DIODE_VISHAY
ComponentType.TRANSISTOR
ComponentType.TRANSISTOR_VISHAY
ComponentType.LED
ComponentType.LED_STANDARD_VISHAY
ComponentType.LED_RGB_VISHAY
ComponentType.LED_SMD_VISHAY
```

---

## Known Handler Issues

### 1. HashSet in getSupportedTypes()

Uses mutable `HashSet` instead of `Set.of()`.

### 2. Transistor/Diode Overlap

2N and 1N prefixes are also used by other manufacturers. Handler may match parts from TI, OnSemi, etc.

---

## Test Patterns

### Valid Resistor MPNs

```
CRCW0603100RFKEA
CRCW0805100KJNEA
CRCW12061K00FKEA
```

### Valid MOSFET MPNs

```
SI2302CDS
SI7336ADP
SIHF16N50D
```

### Valid Diode MPNs

```
1N4007
1N4148
BAT54S
BZX84C5V6
```

---

## Related Files

- Handler: `manufacturers/VishayHandler.java`
- Test: `handlers/VishayHandlerTest.java`

<!-- Add new learnings above this line -->
