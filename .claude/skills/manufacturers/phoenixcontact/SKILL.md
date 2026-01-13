---
name: phoenixcontact
description: Phoenix Contact MPN encoding patterns, connector series decoding, and handler guidance. Use when working with Phoenix Contact terminal blocks, COMBICON connectors, or PhoenixContactHandler.
---

# Phoenix Contact Manufacturer Skill

## Manufacturer Overview

Phoenix Contact is a German industrial connector manufacturer specializing in:

- **Terminal Blocks**: Screw, push-in, and spring-cage types (UK, UT, PT series)
- **PCB Connectors**: COMBICON pluggable and fixed connectors (MC, MCV, MSTB series)
- **Industrial Automation**: DIN rail components, industrial Ethernet, I/O systems
- **Power Distribution**: High-current connectors, power terminal blocks

Phoenix Contact is known for industrial-grade connectors with comprehensive application support.

---

## MPN Structure

Phoenix Contact MPNs follow this general structure:

```
[SERIES] [GAUGE]/[PINS]-[TYPE]-[PITCH] [-SUFFIX]
    |       |       |      |      |        |
    |       |       |      |      |        +-- Optional: SMD, color, etc.
    |       |       |      |      +-- Pitch in mm (e.g., 3,81 or 5,08)
    |       |       |      +-- Connector type (ST, G, GF, PH, etc.)
    |       |       +-- Number of pins/positions
    |       +-- Wire gauge in mm2 (e.g., 1,5 or 2,5)
    +-- Product series (MC, MCV, MSTB, PT, UK, etc.)
```

**Note**: Phoenix Contact uses European decimal notation (comma instead of period): `3,81` means 3.81mm.

### Example Decoding

```
MC 1,5/3-ST-3,81
|   |  |  |   |
|   |  |  |   +-- 3.81mm pitch
|   |  |  +-- ST = Plug (female connector)
|   |  +-- 3 pins/positions
|   +-- 1.5mm2 wire gauge
+-- MC = COMBICON Pluggable series

MCV 1,5/4-G-3,81
|    |  |  |  |
|    |  |  |  +-- 3.81mm pitch
|    |  |  +-- G = Header (male connector)
|    |  +-- 4 pins/positions
|    +-- 1.5mm2 wire gauge
+-- MCV = COMBICON Header series

PTSM 0,5/3-HH-2,5-SMD
|     |  |  |   |   |
|     |  |  |   |   +-- Surface mount
|     |  |  |   +-- 2.5mm pitch
|     |  |  +-- HH = Double-row horizontal
|     |  +-- 3 pins
|     +-- 0.5mm2 wire gauge
+-- PTSM = SMD Terminal Block series
```

---

## Product Series

### COMBICON PCB Connectors (Pluggable)

| Series | Family | Typical Pitch | Current | Description |
|--------|--------|---------------|---------|-------------|
| MC | COMBICON Pluggable | 3.81mm | 8A | Plug (female) connectors |
| MCV | COMBICON Header | 3.81mm | 8A | Header (male) connectors |
| MSTB | COMBICON Standard Pluggable | 5.08mm | 12A | Standard pitch plug |
| MSTBA | COMBICON Standard Header | 5.08mm | 12A | Standard pitch header |
| FK-MCP | High Current Connector | 3.50mm | 12A | High current PCB |
| FRONT-MC | Front Connector | 3.81mm | 8A | Front panel mount |

**Mating Pairs**: MC plugs mate with MCV headers; MSTB plugs mate with MSTBA headers.

### Terminal Blocks (Non-Separable)

| Series | Family | Typical Pitch | Current | Description |
|--------|--------|---------------|---------|-------------|
| PT | Push-In Terminal Block | 3.50mm | 17.5A | Push-in connection |
| PTSM | SMD Terminal Block | 2.50mm | 6A | Surface mount terminal |
| SPT | Spring-Cage PCB Connector | 5.00mm | 24A | Spring-cage connection |
| PC | PCB Terminal Block | 7.62mm | 24A | Fixed PCB terminal |
| UK | Screw Terminal Block | 5.08mm | 32A | DIN rail screw terminal |
| UT | Through-Wall Terminal | 6.20mm | 32A | Through-wall feedthrough |

---

## Connector Type Codes

| Code | Type | Description |
|------|------|-------------|
| ST | Plug | Female connector (mates with header) |
| G | Header | Male connector (solders to PCB) |
| GF | Header + Flange | Header with mounting flange |
| STF | Plug + Flange | Plug with mounting flange |
| V | Vertical | Vertical mounting orientation |
| H | Horizontal | Horizontal/right-angle mounting |
| HH | Double-row Horizontal | Two-row horizontal configuration |
| PH | Push-In Header | Header with push-in termination |

### Mating Connector Selection

```
To find mating connector for a plug:
- MC 1,5/X-ST-3,81 mates with MCV 1,5/X-G-3,81
- MSTB 2,5/X-ST-5,08 mates with MSTBA 2,5/X-G-5,08

Key: Same series base, same pins, same pitch
     ST (plug) pairs with G (header)
```

---

## Package Code Extraction

The `extractPackageCode()` method returns a formatted string:

| Series Pattern | Package Code Format | Example |
|----------------|---------------------|---------|
| MC/MCV/MSTB | `{pins}P-{pitch}mm` | "3P-3.81mm" |
| PT/SPT/PC | `{pins}P-{pitch}mm` | "2P-3.5mm" |
| PTSM (SMD) | `{pins}P-{pitch}mm-SMD` | "3P-2.5mm-SMD" |
| UK/UT | `{gauge}mm2` | "2.5mm2" |

**Note**: Comma notation is converted to periods (3,81 -> 3.81).

---

## Series Extraction

Series extraction returns the product family prefix:

| MPN Pattern | Extracted Series |
|-------------|------------------|
| `MC 1,5/3-ST-3,81` | "MC" |
| `MCV 1,5/4-G-3,81` | "MCV" |
| `MSTB 2,5/4-ST-5,08` | "MSTB" |
| `MSTBA 2,5/6-G-5,08` | "MSTBA" |
| `PT 1,5/2-PH-3,5` | "PT" |
| `PTSM 0,5/3-HH-2,5-SMD` | "PTSM" |
| `UK 5-TWIN` | "UK" |
| `UT 2,5-MTD` | "UT" |
| `FK-MCP 1,5/4-ST-3,5` | "FK-MCP" |
| `FRONT-MC 1,5/6-ST-3,81` | "FRONT-MC" |
| `PC 5/3-STF-7,62` | "PC" |
| `SPT 2,5/3-V-5,0` | "SPT" |

**Critical**: Longer prefixes are checked before shorter ones (e.g., "FRONT-MC" before "MC").

---

## Helper Methods

The PhoenixContactHandler provides additional helper methods:

| Method | Returns | Description |
|--------|---------|-------------|
| `extractPinCount(mpn)` | int | Number of pins/positions |
| `extractPitch(mpn)` | String | Pitch in mm (e.g., "3.81") |
| `getFamily(mpn)` | String | Family description (e.g., "COMBICON Pluggable") |
| `getRatedCurrent(mpn)` | double | Rated current in Amperes |
| `isSMD(mpn)` | boolean | True if surface mount |
| `getMountingType(mpn)` | String | "SMD" or "THT" |
| `getConnectorType(mpn)` | String | Type code (ST, G, etc.) |
| `getConnectorTypeDescription(mpn)` | String | Human-readable type |
| `getWireGauge(mpn)` | String | Wire gauge in mm2 |
| `isPluggable(mpn)` | boolean | True if separable connector |
| `isTerminalBlock(mpn)` | boolean | True if non-separable |

### Usage Examples

```java
PhoenixContactHandler handler = new PhoenixContactHandler();

// Extract specifications
String mpn = "MC 1,5/3-ST-3,81";
int pins = handler.extractPinCount(mpn);           // 3
String pitch = handler.extractPitch(mpn);          // "3.81"
String family = handler.getFamily(mpn);            // "COMBICON Pluggable"
double current = handler.getRatedCurrent(mpn);     // 8.0
String type = handler.getConnectorTypeDescription(mpn);  // "Plug (female)"
boolean pluggable = handler.isPluggable(mpn);      // true

// SMD check
String smtMpn = "PTSM 0,5/3-HH-2,5-SMD";
boolean isSmd = handler.isSMD(smtMpn);             // true
String mounting = handler.getMountingType(smtMpn); // "SMD"
```

---

## Replacement Compatibility

The `isOfficialReplacement()` method determines if two connectors are compatible:

**Requirements for compatibility**:
1. Same series (e.g., both MC or both MCV)
2. Same pin count
3. Same pitch

**Example**:
```java
// Compatible - same series, pins, pitch (different variants)
handler.isOfficialReplacement("MC 1,5/3-ST-3,81", "MC 1,5/3-STF-3,81");  // true

// Not compatible - different pitch
handler.isOfficialReplacement("MC 1,5/3-ST-3,81", "MC 1,5/3-ST-5,08");  // false

// Not compatible - different pin count
handler.isOfficialReplacement("MC 1,5/3-ST-3,81", "MC 1,5/4-ST-3,81");  // false
```

---

## Wire Gauge Reference

| Gauge (mm2) | AWG Approx | Common Current |
|-------------|------------|----------------|
| 0.5 | 20 | 6A |
| 1.5 | 16 | 8-12A |
| 2.5 | 14 | 12-17.5A |
| 4.0 | 12 | 20-24A |
| 5.0 | 10 | 24-32A |
| 6.0 | 10 | 32A |

---

## Common Applications

### Low-Current Signal (0.5-1.5mm2)
- MC/MCV series at 3.81mm pitch
- PTSM for SMD applications
- Control signal wiring

### Medium-Current (2.5mm2)
- MSTB/MSTBA series at 5.08mm pitch
- PT push-in terminal blocks
- Motor control, sensors

### High-Current (4-6mm2)
- SPT spring-cage connectors
- UK/UT screw terminal blocks
- Power distribution, DIN rail

---

## Related Files

- Handler: `manufacturers/PhoenixContactHandler.java`
- Component types: `CONNECTOR`, `IC`
- Pattern registry patterns for series: MC, MCV, MSTB, MSTBA, PT, PTSM, SPT, UK, UT, FK-MCP, FRONT-MC, PC

---

## Learnings & Edge Cases

- **Decimal notation**: Phoenix Contact uses European notation (comma as decimal separator). Always convert to period for numeric operations: `"3,81".replace(",", ".")`.
- **Space vs hyphen**: MPNs can use either space or hyphen as separator after series prefix (e.g., "MC 1,5" or "MC-1,5"). The handler patterns accept both.
- **Series ordering**: When extracting series, check longer prefixes first (FRONT-MC before MC, MSTBA before MSTB, PTSM before PT).
- **UK/UT series**: These DIN rail terminal blocks don't have pin count in the MPN - the number indicates wire gauge (UK 5 = 5mm2 wire gauge).
- **Mating pairs**: To select mating connectors, match the series base (MC with MCV, MSTB with MSTBA), keep same pin count and pitch, change connector type (ST to G).
- **SMD identification**: PTSM series is always SMD; other series may have "-SMD" suffix for surface mount variants.
- **Package code format**: The handler returns `{pins}P-{pitch}mm` format (e.g., "3P-3.81mm") which differs from semiconductor package codes - this is intentional for connector identification.
- **getSupportedTypes() includes IC**: The handler returns `Set.of(CONNECTOR, IC)` - the IC type is included for completeness but primary use is CONNECTOR.

<!-- Add new learnings above this line -->
