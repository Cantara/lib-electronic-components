# Mean Well Part Number Reference

## MPN Structure Overview

Mean Well part numbers follow a consistent format:

```
SERIES-WATTAGE-VOLTAGE[SUFFIX]
```

Examples:
- `RS-25-5` = RS series, 25W, 5V output
- `LRS-350-24` = LRS series, 350W, 24V output
- `HLG-150H-24A` = HLG series, 150W (H=high efficiency), 24V, type A dimming

---

## Product Lines

### AC-DC Enclosed Power Supplies

| Series | Description | Power Range | Features |
|--------|-------------|-------------|----------|
| RS | Economy Enclosed | 15-150W | Basic single output |
| LRS | Slim Enclosed | 35-600W | Low profile, high efficiency |
| SE | High Power Enclosed | 200-1500W | PFC, high power |
| NES | Single Output | 15-350W | Classic design |
| SP | Single Output with PFC | 75-750W | Power factor correction |

### DC-DC Converters

| Series | Description | Power Range | Input Range |
|--------|-------------|-------------|-------------|
| SD | Enclosed DC-DC | 15-1000W | Various input ranges |
| DDR | DIN Rail DC-DC | 15-480W | 9.2-72V input options |

### LED Drivers

| Series | Description | Power Range | Features |
|--------|-------------|-------------|----------|
| LPV | Constant Voltage | 20-150W | IP67 plastic case |
| HLG | High Bay | 40-600W | 3-in-1 dimming, IP65/67 |
| ELG | Constant Current/Voltage | 75-240W | Timer dimming |
| PLN | Constant Current | 20-100W | IP64 plastic case |
| PWM | PWM Output | 40-200W | PWM dimming output |
| LCM | Modular | 25-60W | Multi-output configurable |

### DIN Rail Power Supplies

| Series | Description | Power Range | Width |
|--------|-------------|-------------|-------|
| HDR | Ultra Slim | 15-150W | 1-3 DIN units |
| EDR | Economy | 75-150W | 2-3 DIN units |
| MDR | Miniature | 10-100W | 1-2 DIN units |
| NDR | Slim | 75-480W | 2-4 DIN units |
| DR | Standard | 75-480W | 2-4 DIN units |

---

## MPN Decoding

### Enclosed AC-DC: RS-25-5

```
RS - 25 - 5
|    |    |
|    |    +-- Output voltage (5V)
|    +-- Power rating (25W)
+-- Series identifier
```

### LED Driver: HLG-150H-24A

```
HLG - 150 H - 24 A
|     |   |   |  |
|     |   |   |  +-- Dimming type (A=1-10V, B=3-in-1, blank=non-dim)
|     |   |   +-- Output voltage (24V)
|     |   +-- H = High efficiency
|     +-- Power rating (150W)
+-- Series identifier (High Bay LED driver)
```

### DC-DC Converter: SD-25C-12

```
SD - 25 C - 12
|    |  |   |
|    |  |   +-- Output voltage (12V)
|    |  +-- Input range (A=9.2-18V, B=19-36V, C=36-72V, D=72-144V)
|    +-- Power rating (25W)
+-- Series identifier
```

### DIN Rail: DDR-60G-24

```
DDR - 60 G - 24
|     |  |   |
|     |  |   +-- Output voltage (24V)
|     |  +-- Input range (G=9-18V wide range, L=18-75V wide range)
|     +-- Power rating (60W)
+-- Series identifier (DIN Rail DC-DC)
```

---

## Output Voltage Codes

Standard output voltages across most series:

| Code | Voltage | Common Applications |
|------|---------|---------------------|
| 3.3 | 3.3V | Logic circuits |
| 5 | 5V | TTL, USB devices |
| 12 | 12V | LEDs, motors, fans |
| 15 | 15V | Op-amp supplies |
| 24 | 24V | Industrial, PLCs |
| 36 | 36V | LED strips |
| 48 | 48V | Telecom, PoE |

---

## HLG Dimming Types

| Suffix | Dimming Method | Control Signal |
|--------|----------------|----------------|
| (blank) | Non-dimmable | N/A |
| A | Analog | 1-10V or 10V PWM |
| B | 3-in-1 | 1-10V, PWM, or resistance |
| AB | 3-in-1 + Aux | Same as B with aux output |
| D | DALI | DALI protocol |
| DA | DALI-2 | DALI-2 protocol |

---

## Handler Implementation Notes

### Pattern Matching

Mean Well patterns use the format:
```
^SERIES-[0-9]+(?:[A-Z])?(?:-[0-9]+)?(?:[A-Z]+)?$
```

The handler uses explicit series matching for reliability.

### Package Code Extraction

Returns wattage with "W" suffix:
```java
handler.extractPackageCode("RS-25-5")  // Returns "25W"
handler.extractPackageCode("LRS-350-24")  // Returns "350W"
```

### Output Voltage Extraction

Extracts voltage after second hyphen:
```java
handler.extractOutputVoltage("RS-25-5")  // Returns "5V"
handler.extractOutputVoltage("HLG-150H-24A")  // Returns "24V"
```

### Series Extraction

Returns the series prefix:
```java
handler.extractSeries("RS-25-5")  // Returns "RS"
handler.extractSeries("HLG-150H-24A")  // Returns "HLG"
```

### Supported ComponentTypes

```java
ComponentType.POWER_SUPPLY
ComponentType.POWER_SUPPLY_AC_DC
ComponentType.POWER_SUPPLY_DC_DC
ComponentType.POWER_SUPPLY_MEANWELL
ComponentType.POWER_SUPPLY_AC_DC_MEANWELL
ComponentType.POWER_SUPPLY_DC_DC_MEANWELL
ComponentType.LED_DRIVER
ComponentType.LED_DRIVER_MEANWELL
```

---

## Replacement Rules

Two Mean Well parts are considered replacements if:
1. Same series (e.g., both RS)
2. Same output voltage
3. Replacement has equal or greater wattage

Example: RS-50-5 can replace RS-25-5 (same series, same voltage, higher wattage)

---

## Helper Methods

The handler provides additional utility methods:

```java
// Check if LED driver
handler.isLEDDriver("HLG-150H-24A")  // true
handler.isLEDDriver("RS-25-5")  // false

// Check if DIN rail
handler.isDINRail("HDR-30-24")  // true
handler.isDINRail("RS-25-5")  // false

// Check if DC-DC converter
handler.isDCDC("SD-25C-12")  // true
handler.isDCDC("RS-25-5")  // false

// Get product description
handler.getProductType("RS-25-5")  // "Economy Enclosed Switching Power Supply"
handler.getProductType("HLG-150H-24A")  // "LED Driver with Dimming"
```

---

## Test Patterns

### Valid AC-DC MPNs

```
RS-25-5
RS-50-12
RS-100-24
LRS-50-5
LRS-100-12
LRS-200-24
LRS-350-24
LRS-600-48
SE-200-24
SE-450-24
SE-600-24
NES-25-5
NES-50-12
NES-350-48
```

### Valid LED Driver MPNs

```
LPV-20-12
LPV-35-24
LPV-60-12
LPV-100-24
HLG-40H-12
HLG-80H-24
HLG-150H-24A
HLG-240H-48B
HLG-320H-24
ELG-75-24
ELG-100-48
ELG-150-C500
```

### Valid DC-DC MPNs

```
SD-25A-12
SD-25B-24
SD-25C-12
SD-50A-24
SD-100B-12
DDR-15G-12
DDR-30L-24
DDR-60G-24
DDR-120D-24
```

### Valid DIN Rail MPNs

```
HDR-15-5
HDR-30-12
HDR-60-24
HDR-100-24
EDR-75-12
EDR-120-24
EDR-150-48
MDR-10-5
MDR-20-12
MDR-40-24
MDR-60-24
NDR-75-24
NDR-120-24
NDR-240-48
```

---

## Related Files

- Handler: `manufacturers/MeanWellHandler.java`
- Component types: POWER_SUPPLY_MEANWELL, POWER_SUPPLY_AC_DC_MEANWELL, POWER_SUPPLY_DC_DC_MEANWELL, LED_DRIVER_MEANWELL
- Test: `handlers/MeanWellHandlerTest.java`
- Manufacturer enum: MEAN_WELL in ComponentManufacturer.java

---

## Learnings & Edge Cases

- HLG series has optional "H" suffix for high efficiency models before voltage
- LED drivers with dimming have letter suffix (A, B, AB, D, DA)
- DC-DC converters use letter codes for input voltage range (A, B, C, D, G, L)
- Some series have identical wattage/voltage with different features (e.g., RS vs NES)
- DIN rail width is not encoded in MPN, must reference datasheet
- IP rating for LED drivers varies by series (LPV=IP67, HLG=IP65/67)

<!-- Add new learnings above this line -->
