# Littelfuse Manufacturer Skill

Use this skill when working with Littelfuse circuit protection components - TVS diodes, fuses, varistors, and MOVs.

## Overview

Littelfuse is a leading manufacturer of circuit protection components. The handler supports:
- **TVS Diodes**: Transient voltage suppressors for ESD and surge protection
- **Fuses**: SMD and through-hole fuses for overcurrent protection
- **Varistors**: Metal oxide varistors (MOVs) for voltage clamping

## MPN Structure

### TVS Diodes - SM Series (VR-Based Naming)

The SM series uses reverse standoff voltage (VR) in the part number:

```
SMXJ[Voltage][A/CA]
```

| Component | Description |
|-----------|-------------|
| SM | Surface Mount |
| X | Package: A=SMA, B=SMB, C=SMC, D=SMD |
| J | Series designator |
| Voltage | Reverse standoff voltage (e.g., 5.0, 15, 33) |
| A | Unidirectional |
| CA | Bidirectional |

**Power Ratings by Package:**
| Series | Package | Power (W) | DO Package |
|--------|---------|-----------|------------|
| SMAJ | SMA | 400 | DO-214AC |
| SMBJ | SMB | 600 | DO-214AA |
| SMCJ | SMC | 1500 | DO-214AB |
| SMDJ | SMD | 3000 | - |
| 5.0SMDJ | SMD | 5000 | - |

**Examples:**
- `SMAJ5.0A` - 400W, 5V standoff, unidirectional, SMA package
- `SMBJ33CA` - 600W, 33V standoff, bidirectional, SMB package

### TVS Diodes - P Series (VBR-Based Naming)

The P series uses breakdown voltage (VBR) in the part number:

```
P[Power][Package][Voltage][A/CA]
```

| Series | Package | Power (W) | Package Type |
|--------|---------|-----------|--------------|
| P4KE | DO-41 | 400 | Axial |
| P6KE | DO-15 | 600 | Axial |
| P4SMA | SMA | 400 | SMD |
| P6SMB | SMB | 600 | SMD |
| 1.5KE | DO-15 | 1500 | Axial |
| 1.5SMC | SMC | 1500 | SMD |
| 3KP | P600 | 3000 | Axial |

**Examples:**
- `P6KE6.8A` - 600W, 6.8V breakdown, unidirectional, axial DO-15
- `P6KE15CA` - 600W, 15V breakdown, bidirectional, axial DO-15
- `1.5KE33CA` - 1500W, 33V breakdown, bidirectional, axial

### Fuses - NANO2 Series

```
[Series][Current].[Suffix]
```

| Series | Type | Size |
|--------|------|------|
| 0451 | Very Fast Acting | 6.1 x 2.69mm |
| 0452 | Slow Blow (Slo-Blo) | 6.1 x 2.69mm |
| 0453 | Very Fast Acting | 10.1 x 3.12mm |
| 0454 | Slow Blow | 10.1 x 3.12mm |
| 0448 | Nano2 SMF | Square SMD |

**Suffixes:**
- `.MRL` - Mini Reel (1,000 pcs tape & reel)
- `.NRL` - Full Reel (5,000 pcs tape & reel)

**Examples:**
- `0452005.MRL` - Slow blow, 5A, mini reel
- `0451002.MRL` - Very fast acting, 2A, mini reel
- `0448.125` - Nano2 SMF, 125mA

### Varistors - V Series MOVs

**Radial Lead MOVs:**
```
V[Voltage][E/P][Size]
```

| Component | Description |
|-----------|-------------|
| V | Varistor prefix |
| Voltage | Operating voltage (2-3 digits) |
| E | Epoxy coating |
| P | Phenolic coating |
| Size | Disc diameter in mm |

**Multilayer SMD Varistors (MLE Series):**
```
V[Voltage]MLE[Package][Options]
```

**Examples:**
- `V07E130P` - 130V, 7mm disc, epoxy/phenolic
- `V18MLE0603N` - 18V, multilayer, 0603 package
- `V26MLE0805N` - 26V, multilayer, 0805 package

## Supported ComponentTypes

- `DIODE` - Base type for all TVS diodes
- `TVS_DIODE_LITTELFUSE` - Specific TVS diode type
- `FUSE_LITTELFUSE` - Fuse type
- `VARISTOR_LITTELFUSE` - Varistor type

## Handler Methods

### Core Methods

```java
// Pattern matching
boolean matches(String mpn, ComponentType type, PatternRegistry patterns)

// Extraction methods
String extractPackageCode(String mpn)  // Returns package (SMA, SMB, NANO2, etc.)
String extractSeries(String mpn)       // Returns series (SMAJ, P6KE, 0452, etc.)
```

### Littelfuse-Specific Methods

```java
// TVS Diode specific
String extractVoltage(String mpn)      // Returns voltage rating
boolean isBidirectional(String mpn)    // True for CA suffix parts
int getPowerRating(String mpn)         // Returns power in watts

// Fuse specific
String extractCurrentRating(String mpn) // Returns current rating
```

## Usage Examples

```java
LittelfuseHandler handler = new LittelfuseHandler();
PatternRegistry registry = new PatternRegistry();
handler.initializePatterns(registry);

// TVS Diode detection
handler.matches("SMAJ15A", ComponentType.TVS_DIODE_LITTELFUSE, registry); // true
handler.extractPackageCode("SMAJ15A");  // "SMA"
handler.extractVoltage("SMAJ15A");      // "15"
handler.isBidirectional("SMAJ15CA");    // true
handler.getPowerRating("SMAJ15A");      // 400

// Fuse detection
handler.matches("0452005.MRL", ComponentType.FUSE_LITTELFUSE, registry); // true
handler.extractPackageCode("0452005.MRL");  // "NANO2"
handler.extractCurrentRating("0452005.MRL"); // "5"

// Varistor detection
handler.matches("V18MLE0603N", ComponentType.VARISTOR_LITTELFUSE, registry); // true
handler.extractPackageCode("V18MLE0603N");  // "0603"
handler.extractVoltage("V18MLE0603N");      // "18"
```

## Common Selection Guide

### TVS Diode Selection by Application

| Application | Recommended Series | Power | Package |
|-------------|-------------------|-------|---------|
| Low-power signal | SMAJ | 400W | SMA |
| Medium-power | SMBJ | 600W | SMB |
| High-power DC | SMCJ | 1500W | SMC |
| Through-hole | P6KE | 600W | DO-15 |
| Low capacitance | SAC | 500W | DO-15 |

### Fuse Selection by Characteristics

| Characteristic | Recommended Series |
|----------------|-------------------|
| Fast acting, SMD | 0451 |
| Slow blow, SMD | 0452 |
| Fast acting, large | 0453 |
| Slow blow, large | 0454 |
| Ultra-small SMD | 0448 |

## Learnings & Quirks

### VR vs VBR Naming Systems
- **VR-based (SMAJ, SMBJ, etc.)**: Part number shows standoff voltage
- **VBR-based (P4KE, P6KE, etc.)**: Part number shows breakdown voltage
- **Important**: SMAJ10A and P4SMA10A have DIFFERENT voltage characteristics!
  - SMAJ10A has 10V standoff (VR), breakdown is higher
  - P4SMA10A has 10V breakdown (VBR), standoff is lower (~8.5V)

### Bidirectional vs Unidirectional
- `A` suffix = Unidirectional (cathode band marking)
- `CA` suffix = Bidirectional (no polarity)
- **Gotcha**: CA parts have ~2x the capacitance of A parts

### Fuse Current Ratings
- Current in 045x series is in the middle: `0452005` = 5A
- Leading zeros are significant for sub-1A ratings: `0452.250` = 250mA

### Package Cross-Reference
| Littelfuse | DO Standard | Dimensions |
|------------|-------------|------------|
| SMA | DO-214AC | 4.5 x 2.5 x 2.3mm |
| SMB | DO-214AA | 5.2 x 3.6 x 2.2mm |
| SMC | DO-214AB | 7.1 x 6.2 x 2.3mm |

---

## Related Skills

- `/semiconductor` - General semiconductor handling
- `/similarity-diode` - TVS diode similarity calculations
- `/component` - Base component operations

## Test Examples

```java
// Test file: LittelfuseHandlerTest.java
// Location: src/test/java/no/cantara/electronic/component/lib/handlers/

@Test
void shouldDetectSMAJVariants() {
    assertTrue(handler.matches("SMAJ15A", ComponentType.TVS_DIODE_LITTELFUSE, registry));
    assertTrue(handler.matches("SMAJ33CA", ComponentType.TVS_DIODE_LITTELFUSE, registry));
}

@Test
void shouldExtractTVSInfo() {
    assertEquals("SMA", handler.extractPackageCode("SMAJ15A"));
    assertEquals("15", handler.extractVoltage("SMAJ15A"));
    assertEquals(400, handler.getPowerRating("SMAJ15A"));
}
```
