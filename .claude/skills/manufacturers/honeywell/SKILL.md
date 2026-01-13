---
name: honeywell
description: Honeywell Sensing MPN encoding patterns, suffix decoding, and handler guidance. Use when working with Honeywell sensors or HoneywellHandler.
---

# Honeywell Sensing Manufacturer Skill

## Overview

Honeywell Sensing is a major manufacturer of industrial and commercial sensors. The product portfolio includes:

- **Humidity Sensors** (HIH series) - Capacitive humidity sensors with analog/digital output
- **Pressure Sensors** (HSC, SSC, ABP, MPR series) - MEMS-based pressure sensors
- **Hall Effect Sensors** (SS4xx, SS5xx series) - Linear position and switch sensors
- **Magnetometers** (HMC series) - 3-axis digital compass ICs
- **Optical Sensors** (HOA, HLC series) - Reflective and interrupter sensors
- **Rotary Sensors** (HRS, HPX series) - Rotary position encoders

---

## MPN Structure

Honeywell uses different MPN structures for each product family:

### Humidity Sensors (HIH series)

```
HIH[SERIES]-[PACKAGE]-[VARIANT]
│   │          │         │
│   │          │         └── Options/Features code
│   │          └── Package/condensing type (021, 022, 031, 032)
│   └── Series number (4000, 5030, 6130, 8120, etc.)
└── Humidity Integrated Hybrid prefix
```

### Pressure Sensors (HSC/SSC/ABP/MPR series)

```
HSC[TYPE][OUTPUT][ADDR][RANGE][UNIT][PKG]
│   │      │      │      │      │     │
│   │      │      │      │      │     └── Package code (A3=DIP, A5=SMT)
│   │      │      │      │      └── Pressure unit (PG=psi, MD=mbar, KP=kPa)
│   │      │      │      └── Range (001, 060, 150, etc.)
│   │      │      └── I2C Address (N=default, A-F=alternate)
│   │      └── Output type (R=ratiometric, S=spi, A=analog, D=digital)
│   └── Transfer type (D=differential, M=MEMS, S=standard)
└── High accuracy Sensing prefix (HSC=TruStability, SSC=Standard, ABP=Basic, MPR=MicroPressure)
```

### Hall Effect Sensors (SS4xx/SS5xx series)

```
SS[FAMILY][TYPE][PKG][VARIANT]
│    │       │    │      │
│    │       │    │      └── Variant number (1, 2, etc.)
│    │       │    └── Package (A=TO-92, E=SOT-89, T=SOT-23, L=SIP)
│    │       └── Model in series (41, 49, 95, etc.)
│    └── Family (4=low-cost, 5=high performance)
└── Solid State prefix
```

### Magnetometers (HMC series)

```
HMC[SERIES][PKG][-SUFFIX]
│     │      │      │
│     │      │      └── -TR = Tape and Reel
│     │      └── L = LCC package
│     └── Series number (5843, 5883, etc.)
└── Honeywell Magnetic Compass prefix
```

---

## Package Codes

### Humidity Sensors (HIH series)

| Code | Package | Description |
|------|---------|-------------|
| -021 | SIP | SIP non-condensing |
| -022 | SMD | SMD non-condensing |
| -031 | SIP | SIP condensing |
| -032 | SMD | SMD condensing |

### Pressure Sensors

| Suffix | Package | Description |
|--------|---------|-------------|
| A3 | DIP | Through-hole DIP |
| A5 | SMT | Surface mount |
| AA | DIP | DIP variant |
| SA | SIP | Single inline |
| MD | SMD | Surface mount device |

### Hall Effect Sensors

| Letter | Package | Pins |
|--------|---------|------|
| A | TO-92 | 3 |
| E | SOT-89 | 3 |
| T | SOT-23 | 3 |
| L | SIP | 3-4 |

### Magnetometers

| Suffix | Package | Description |
|--------|---------|-------------|
| L | LCC | Leadless chip carrier |
| (none) | QFN | Bare die or QFN |

---

## Product Families

### Humidity Sensors (HIH series)

| Series | Type | Output | Interface |
|--------|------|--------|-----------|
| HIH4000 | Analog | Voltage | Analog |
| HIH4010 | Analog | Voltage | Analog (covered) |
| HIH5030 | Analog | Voltage | Analog |
| HIH6130 | Digital | I2C | I2C |
| HIH6131 | Digital | I2C | I2C (alarm output) |
| HIH8120 | Digital | I2C | I2C |
| HIH8121 | Digital | I2C | I2C (alarm output) |

### Pressure Sensors

| Family | Type | Accuracy | Output Options |
|--------|------|----------|----------------|
| HSC | TruStability | +-0.25% FSS | I2C, SPI, Analog |
| SSC | Standard | +-2% FSS | I2C, SPI, Analog |
| ABP | Basic | +-2.5% FSS | I2C, SPI, Analog |
| MPR | MicroPressure | +-0.25% FSS | I2C |

### Hall Effect Sensors

| Series | Type | Output | Application |
|--------|------|--------|-------------|
| SS49x | Linear | Analog | Position sensing |
| SS59x | Linear | Analog | High sensitivity |
| SS4xx | Switch | Digital | Proximity detection |
| SS5xx | Switch | Digital | High sensitivity |

---

## Supported Component Types

From `HoneywellHandler.getSupportedTypes()`:

| ComponentType | Products |
|---------------|----------|
| `SENSOR` | All sensor types |
| `HUMIDITY_SENSOR` | HIH series |
| `PRESSURE_SENSOR` | HSC, SSC, ABP, MPR series |
| `HALL_SENSOR` | SS4xx, SS5xx series |
| `MAGNETOMETER` | HMC series |
| `SENSOR_OPTICAL` | HOA, HLC series |

---

## Pattern Matching

### Registered Patterns (from initializePatterns)

```java
// Humidity Sensors
"^HIH[0-9].*"                 // HUMIDITY_SENSOR, SENSOR

// Pressure Sensors
"^HSC[A-Z].*"                 // PRESSURE_SENSOR, SENSOR
"^SSC[A-Z].*"                 // PRESSURE_SENSOR, SENSOR
"^ABP[A-Z].*"                 // PRESSURE_SENSOR, SENSOR
"^MPR[A-Z].*"                 // PRESSURE_SENSOR, SENSOR

// Hall Effect Linear Sensors
"^SS49[0-9].*"                // HALL_SENSOR, SENSOR (linear)
"^SS59[0-9].*"                // HALL_SENSOR, SENSOR (linear)

// Hall Effect Switch Sensors
"^SS4[0-8][0-9].*"            // HALL_SENSOR, SENSOR (switch, not SS49x)
"^SS5[0-8][0-9].*"            // HALL_SENSOR, SENSOR (switch, not SS59x)

// Magnetometers
"^HMC[0-9].*"                 // MAGNETOMETER, SENSOR

// Rotary Sensors
"^HRS[0-9].*"                 // SENSOR
"^HPX[0-9].*"                 // SENSOR

// Optical Sensors
"^HOA[0-9].*"                 // SENSOR_OPTICAL, SENSOR
"^HLC[0-9].*"                 // SENSOR_OPTICAL, SENSOR
```

---

## Series Extraction Rules

| Prefix | Extraction Rule | Example |
|--------|-----------------|---------|
| HIH | HIH + up to 4 digits | HIH6130-021-001 -> HIH6130 |
| HSC | Just "HSC" | HSCDANN001PG2A3 -> HSC |
| SSC | Just "SSC" | SSCMANV060PGAA5 -> SSC |
| ABP | Just "ABP" | ABPMANN060PG2A3 -> ABP |
| MPR | Just "MPR" | MPRLS0025PA00001A -> MPR |
| SS49 | "SS49" | SS495A1 -> SS49 |
| SS59 | "SS59" | SS59ET -> SS59 |
| SS4 | "SS4" (switch) | SS441A -> SS4 |
| SS5 | "SS5" (switch) | SS451A -> SS5 |
| HMC | HMC + up to 4 digits | HMC5883L -> HMC5883 |
| HRS | Just "HRS" | HRS100 -> HRS |
| HPX | Just "HPX" | HPX200 -> HPX |
| HOA | HOA + up to 4 digits | HOA1180-001 -> HOA1180 |
| HLC | HLC + up to 4 digits | HLC2705-001 -> HLC2705 |

---

## Package Code Extraction Rules

The handler uses different extraction logic per product family:

### Pressure Sensors (HSC/SSC/ABP/MPR)

```java
// Extract last 2 characters from the main part (before any hyphen)
// HSCDANN001PG2A3 -> A3 -> DIP
String suffix = mainPart.substring(mainPart.length() - 2);
switch (suffix) {
    case "A3" -> "DIP"
    case "A5" -> "SMT"
    case "AA" -> "DIP"
    case "SA" -> "SIP"
    case "MD" -> "SMD"
}
```

### Humidity Sensors (HIH)

```java
// Package code is in the second segment after hyphen
// HIH6130-021-001 -> 021 -> SIP
switch (segment) {
    case "021" -> "SIP"   // SIP non-condensing
    case "022" -> "SMD"   // SMD non-condensing
    case "031" -> "SIP"   // SIP condensing
    case "032" -> "SMD"   // SMD condensing
}
```

### Hall Effect Sensors (SS4xx/SS5xx)

```java
// Package code is the letter before final digit (if present)
// SS495A1 -> A -> TO-92
// SS441A  -> A -> TO-92
switch (pkgChar) {
    case 'A' -> "TO-92"
    case 'E' -> "SOT-89"
    case 'T' -> "SOT-23"
    case 'L' -> "SIP"
}
```

### Magnetometers (HMC)

```java
// Look for trailing L indicating LCC package
// HMC5883L -> L -> LCC
if (basePart.endsWith("L")) return "LCC"
```

---

## Example MPNs with Explanations

### Humidity Sensors

| MPN | Series | Package | Description |
|-----|--------|---------|-------------|
| HIH6130-021-001 | HIH6130 | SIP | Digital humidity sensor, SIP non-condensing |
| HIH6130-022-001 | HIH6130 | SMD | Digital humidity sensor, SMD non-condensing |
| HIH4000-001 | HIH4000 | (varies) | Analog humidity sensor |
| HIH8120-021-001 | HIH8120 | SIP | High accuracy digital humidity |

### Pressure Sensors

| MPN | Series | Package | Description |
|-----|--------|---------|-------------|
| HSCDANN001PG2A3 | HSC | DIP | TruStability, differential, 1 psi, DIP |
| HSCDRRN060MDSA | HSC | SIP | TruStability, 60 mbar differential, SIP |
| SSCMANV060PGAA5 | SSC | SMT | Standard pressure, 60 psi gauge, SMT |
| ABPMANN060PG2A3 | ABP | DIP | Basic pressure, 60 psi gauge, DIP |
| MPRLS0025PA00001A | MPR | SMT | MicroPressure, 25 psi, SMT |

### Hall Effect Sensors

| MPN | Series | Package | Description |
|-----|--------|---------|-------------|
| SS49E | SS49 | SOT-89 | Linear hall effect, SOT-89 |
| SS495A | SS49 | TO-92 | Linear hall effect, TO-92 |
| SS495A1 | SS49 | TO-92 | Linear hall effect, TO-92, variant 1 |
| SS441A | SS4 | TO-92 | Hall switch, bipolar, TO-92 |
| SS451A | SS4 | TO-92 | Hall switch, unipolar, TO-92 |
| SS59ET | SS59 | SOT-23 | Linear hall, high sensitivity, SOT-23 |

### Magnetometers

| MPN | Series | Package | Description |
|-----|--------|---------|-------------|
| HMC5883L | HMC5883 | LCC | 3-axis magnetometer, I2C, LCC |
| HMC5883L-TR | HMC5883 | LCC | Same as above, tape and reel |
| HMC5843 | HMC5843 | (QFN) | Older 3-axis magnetometer |

### Optical Sensors

| MPN | Series | Package | Description |
|-----|--------|---------|-------------|
| HOA1180-001 | HOA1180 | (varies) | Reflective optical sensor |
| HLC2705-001 | HLC2705 | (varies) | Optical interrupter |

---

## Official Replacement Logic

The `isOfficialReplacement()` method in HoneywellHandler checks:

1. **Same series**: Parts in the same series are typically compatible (different packages)
2. **Pressure range match**: For pressure sensors, range must match for compatibility
3. **Known compatible pairs**:
   - HIH6130 <-> HIH6131 (alarm output variant)
   - HIH8120 <-> HIH8121 (alarm output variant)
   - HIH4000 <-> HIH4010 (covered variant)
   - HMC5883 variants

---

## Handler Implementation Notes

### matches() Override

The handler provides explicit `matches()` implementation for better performance:

```java
@Override
public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
    switch (type) {
        case HUMIDITY_SENSOR:
            return upperMpn.matches("^HIH[0-9].*");
        case PRESSURE_SENSOR:
            return upperMpn.matches("^HSC[A-Z].*") ||
                   upperMpn.matches("^SSC[A-Z].*") ||
                   upperMpn.matches("^ABP[A-Z].*") ||
                   upperMpn.matches("^MPR[A-Z].*");
        // ... etc.
    }
}
```

### getSupportedTypes() Pattern

Uses `Set.of()` (modern, immutable) - compliant with codebase best practices:

```java
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.SENSOR,
        ComponentType.HUMIDITY_SENSOR,
        ComponentType.PRESSURE_SENSOR,
        ComponentType.HALL_SENSOR,
        ComponentType.MAGNETOMETER,
        ComponentType.SENSOR_OPTICAL
    );
}
```

---

## Related Files

- Handler: `manufacturers/HoneywellHandler.java`
- Component types: `SENSOR`, `HUMIDITY_SENSOR`, `PRESSURE_SENSOR`, `HALL_SENSOR`, `MAGNETOMETER`, `SENSOR_OPTICAL`
- Similarity: `SensorSimilarityCalculator` (general sensor similarity)

---

## Learnings & Quirks

- **SS49x vs SS4xx conflict**: The handler uses specific patterns to distinguish linear sensors (SS49x, SS59x) from switch sensors (SS4xx where xx != 9x). Patterns `SS4[0-8][0-9]` exclude SS49x.
- **Pressure sensor range extraction**: Range is embedded in the long MPN string at different positions. The handler searches for 3-digit or 4-digit numeric chunks.
- **Humidity sensor package in hyphenated segment**: Unlike most manufacturers where package is a suffix, HIH sensors encode package info in the second hyphen-separated segment (021, 022, 031, 032).
- **HMC5883 vs HMC5883L**: Both extract series "HMC5883" but "L" suffix indicates LCC package. They are the same IC in different packages.
- **No manufacturer-specific ComponentTypes**: Unlike TI or ST handlers, Honeywell uses generic types (HUMIDITY_SENSOR, PRESSURE_SENSOR) without _HONEYWELL suffixes.
- **getManufacturerTypes() returns empty set**: Honeywell does not define custom ManufacturerComponentType enums.

<!-- Add new learnings above this line -->
