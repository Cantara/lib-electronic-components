# Nexperia Manufacturer Skill

Use this skill when working with Nexperia components - discrete semiconductors including transistors, MOSFETs, diodes, ESD protection devices, and logic ICs.

## Handler Location

- **Handler**: `src/main/java/no/cantara/electronic/component/lib/manufacturers/NexteriaHandler.java`
- **Tests**: `src/test/java/no/cantara/electronic/component/lib/handlers/NexteriaHandlerTest.java`

**NOTE**: The filename uses "Nexteria" which is a typo - the manufacturer is "Nexperia".

## Company Background

Nexperia was spun off from NXP Semiconductors in 2017. The company inherited:
- NXP Standard Products division
- Philips Semiconductors discrete components (acquired by NXP in 2006)
- Wide portfolio of transistors, diodes, MOSFETs, ESD protection, and logic ICs

## Product Lines

### Transistors (BJT)

| Series | Type | Description | Package |
|--------|------|-------------|---------|
| PMBT | Small signal | SOT23 transistors (PMBT2222A, PMBT3904, PMBT3906) | SOT23 |
| PBSS | Small signal | High performance transistors | SOT23, SOT363 |
| BC | Classic | BC547, BC557, BC846 - Philips legacy | Various |
| BF | RF | High frequency transistors | SOT23, SOT143 |
| MMBT | SMD | Surface mount versions of 2N series | SOT23 |
| 2N/PN | Standard | 2N2222, 2N3904, PN2222 | Various |

### MOSFETs

| Series | Type | Description | Typical Use |
|--------|------|-------------|-------------|
| PSMN | N-channel power | High current switching | Motor drive, power supply |
| PSMP | P-channel power | P-channel complement | High-side switching |
| PMV | Small signal | Low power MOSFETs | Signal switching |
| BSS | Small signal | BSS138 series | Level shifting |
| 2N7002 | N-channel | Popular switching MOSFET | General purpose |
| BUK | Legacy power | Philips heritage | Automotive, industrial |

### Diodes

| Series | Type | Description | Package |
|--------|------|-------------|---------|
| PMEG | Schottky rectifier | Low Vf Schottky | SOD123, SOD323 |
| BAV | Signal/Switching | BAV99 dual, BAV70 | SOT23 |
| BAS | Signal | General purpose signal | SOT23 |
| BAT | Schottky | Small signal Schottky | SOT23 |
| BZX | Zener | Voltage reference | SOT23, DO-35 |
| PZU | Zener | Nexperia Zener line | SOD323 |

### ESD Protection

| Series | Type | Description | Protection Level |
|--------|------|-------------|-----------------|
| PESD | TVS diode | Single/dual line ESD | 5V, 3.3V, etc. |
| PRTR | Array | Multi-channel protection | USB, data lines |
| PTVS | High power | Automotive TVS | Higher current |
| IP4 | Interface | USB, HDMI protection | Interface specific |

### Logic ICs

| Family | Voltage | Speed | Use Case |
|--------|---------|-------|----------|
| 74LVC | 1.65-3.6V | Fast | Low voltage systems |
| 74AHC | 2-5.5V | Very fast | High speed CMOS |
| 74AHCT | 4.5-5.5V | Very fast | TTL compatible |
| 74AUP | 0.8-3.6V | Medium | Ultra-low power |
| 74AUC | 0.8-2.7V | Fast | Ultra-low voltage |
| 74HC | 2-6V | Medium | General purpose |
| 74HCT | 4.5-5.5V | Medium | TTL compatible |

## MPN Format

### Tape & Reel Suffixes

Nexperia uses comma-separated suffixes for tape & reel packaging:

| Suffix | Package | Reel Size |
|--------|---------|-----------|
| ,215 | SOT23 | 7" reel |
| ,235 | SOT23 | 13" reel |
| ,315 | SOD882 | 7" reel |
| ,115 | SOT223 | 7" reel |

**Example**: `PMBT2222A,215` = PMBT2222A in SOT23 package on 7" tape & reel

### Power MOSFET Suffixes

| Suffix | Package |
|--------|---------|
| T | LFPAK56 |
| U | LFPAK88 |
| V | LFPAK33 |
| B | SOT754 |
| L | TO-220 |
| F, FI | TO-220F |
| PE | LFPAK56E |

### Zener Diode Format

Format: `BZX{series}-C{voltage}`

| Series | Package | Example |
|--------|---------|---------|
| BZX84 | SOT23 | BZX84-C5V1 (5.1V Zener) |
| BZX55 | DO-35 | BZX55-C4V7 (4.7V Zener) |
| BZX79 | DO-35 | BZX79-C6V2 (6.2V Zener) |
| BZX85 | DO-41 | BZX85-C12 (12V Zener) |
| BZX384 | SOD323 | BZX384-C3V3 (3.3V Zener) |

### ESD Protection Format

Format: `PESD{voltage}S{channels}{package}`

- `PESD5V0S1BL` = 5.0V, 1 channel, SOD882 package
- `PESD3V3L1BA` = 3.3V low cap, 1 channel, SOD323 package
- `PESD5V0S2UT` = 5.0V, 2 channels, SOT23 package

## Pattern Matching

The handler supports these ComponentTypes:
- `MOSFET`, `MOSFET_NEXPERIA`
- `TRANSISTOR`, `BIPOLAR_TRANSISTOR_NEXPERIA`
- `DIODE`
- `IC`, `ESD_PROTECTION_NEXPERIA`, `LOGIC_IC_NEXPERIA`

### Registered Patterns

```java
// MOSFETs
"^PSMN[0-9].*"   // N-channel power
"^PSMP[0-9].*"   // P-channel power
"^PMV[0-9].*"    // Small signal
"^BSS[0-9].*"    // BSS series
"^2N7002.*"      // 2N7002 variants
"^BUK[0-9].*"    // Legacy power

// Transistors
"^PMBT[0-9].*"   // SOT23 transistors
"^PBSS[0-9].*"   // Small signal
"^BC[0-9].*"     // Classic BC series
"^BF[0-9].*"     // RF transistors
"^MMBT[0-9].*"   // SMD transistors
"^2N2222.*"      // Standard NPN
"^2N3904.*"      // General purpose NPN
"^2N3906.*"      // General purpose PNP
"^PN2222.*"      // PN2222 variant

// Diodes
"^PMEG[0-9].*"   // Schottky rectifier
"^BAV[0-9].*"    // Signal diodes
"^BAS[0-9].*"    // Signal diodes
"^BAT[0-9].*"    // Schottky
"^BZX[0-9].*"    // Zener
"^PZU[0-9].*"    // Nexperia Zener

// ESD Protection
"^PESD[0-9].*"   // TVS diodes
"^PRTR[0-9].*"   // Protection arrays
"^PTVS[0-9].*"   // High power TVS
"^IP4[0-9].*"    // Interface protection

// Logic ICs
"^74AHC.*"       // Advanced HC
"^74AHCT.*"      // AHC TTL compatible
"^74AUC.*"       // Ultra-low voltage
"^74AUP.*"       // Ultra-low power
"^74HC[0-9].*"   // High-speed CMOS
"^74HCT[0-9].*"  // HC TTL compatible
"^74LVC.*"       // Low voltage CMOS
"^74LVCH.*"      // LVC with bus hold
"^74LVT.*"       // Low voltage BiCMOS
```

## Example Usage

```java
NexteriaHandler handler = new NexteriaHandler();
PatternRegistry registry = new PatternRegistry();
handler.initializePatterns(registry);

// Detect component type
handler.matches("PMBT2222A,215", ComponentType.TRANSISTOR, registry);  // true
handler.matches("PMBT2222A,215", ComponentType.BIPOLAR_TRANSISTOR_NEXPERIA, registry);  // true

// Extract package code
handler.extractPackageCode("PMBT2222A,215");  // "SOT23"
handler.extractPackageCode("BZX84-C5V1");     // "SOT23"
handler.extractPackageCode("PSMN3R5-30YLT");  // "LFPAK56"

// Extract series
handler.extractSeries("PMBT2222A,215");  // "PMBT"
handler.extractSeries("74LVC08D");       // "74LVC"
handler.extractSeries("PESD5V0S1BL");    // "PESD"
```

## Cross-Manufacturer Equivalents

| Nexperia | Equivalent | Notes |
|----------|------------|-------|
| PMBT2222A | 2N2222, MMBT2222 | SOT23 version |
| PMBT3904 | 2N3904, MMBT3904 | General purpose NPN |
| PMBT3906 | 2N3906, MMBT3906 | General purpose PNP |
| BC547 | BC107, BC171 | General purpose NPN |
| 2N7002 | BSS138, Si2302 | Small signal N-ch MOSFET |
| BAV99 | BAS19 pair | Dual series diode |
| 74HC00 | SN74HC00, TC74HC00 | Quad NAND gate |

## Replacement Rules

1. **Logic ICs**: 74HCT is backward compatible with 74HC (accepts TTL levels)
2. **Transistors**: PMBT, MMBT, and 2N series with same number are equivalent
3. **MOSFETs**: Same base part number with different package suffix are interchangeable (e.g., PSMN3R5-30YLT vs PSMN3R5-30YLU)
4. **Zeners**: Same voltage rating across BZX84/BZX55/BZX79 are functionally equivalent (different packages)

## Learnings & Quirks

### Comma Suffix Handling
- Nexperia uses `,215`, `,315`, etc. as tape & reel packaging codes
- The handler normalizes these before pattern matching
- Package extraction uses the comma suffix to determine the actual package

### BC Series Numbering
- BC5xx (BC546-BC550) are low noise NPN
- BC5x7 (BC547, BC557) are complementary pairs
- BC8xx are SMD versions (BC846 = BC546 in SOT23)

### Logic IC Family Hierarchy
When checking for 74-series families, check longer prefixes first:
1. AHCT before AHC
2. LVCH before LVC
3. HCT before HC

### 2N7002 vs BSS138
Both are popular small signal N-channel MOSFETs from Nexperia:
- 2N7002: 60V, 115mA, lower Rds(on)
- BSS138: 50V, 220mA, higher current capability

---

## Adding New Patterns

When adding support for new Nexperia component lines:

1. Add patterns to `initializePatterns()` for both base type and Nexperia-specific type
2. Add explicit checks to `matches()` method to prevent cross-handler false matches
3. Add package code mappings for any new suffix formats
4. Add series extraction logic
5. Add comprehensive tests for new patterns

Example:
```java
// In initializePatterns()
registry.addPattern(ComponentType.DIODE, "^NEW_SERIES[0-9].*");

// In matches()
boolean isNewSeries = normalizedMpn.matches("^NEW_SERIES[0-9].*");
if (type == ComponentType.DIODE && isNewSeries) {
    return true;
}
```
