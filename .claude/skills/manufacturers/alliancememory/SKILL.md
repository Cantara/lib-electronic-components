---
name: alliancememory
description: Alliance Memory MPN encoding patterns, suffix decoding, and handler guidance. Use when working with Alliance Memory SRAM, DRAM, or Flash components.
---

# Alliance Memory Manufacturer Skill

## Company Overview

Alliance Memory is a leading supplier of commodity memory devices including SRAM (Static RAM), SDRAM, DDR SDRAM (DDR1/DDR2/DDR3), and NOR Flash. They specialize in providing drop-in replacements for legacy memory devices from other manufacturers.

**Key Product Lines:**
- **AS6C series**: Asynchronous SRAM (async SRAM)
- **AS7C series**: High-speed synchronous SRAM
- **AS4C series**: SDRAM and DDR SDRAM (DDR1, DDR2, DDR3)
- **AS29LV series**: NOR Flash memory

---

## MPN Structure

Alliance Memory MPNs follow this general structure:

```
[PREFIX][DENSITY][VARIANT]-[SPEED][PACKAGE][TEMP][QUALIFIER]
   |       |        |         |      |       |       |
   |       |        |         |      |       |       +-- N = Lead-free (RoHS)
   |       |        |         |      |       +---------- C/I/A = Temperature grade
   |       |        |         |      +------------------ P/T/B/J/S/V = Package
   |       |        |         +------------------------- Access time in nanoseconds
   |       |        +----------------------------------- A/B = Variant/revision
   |       +-------------------------------------------- Memory density
   +---------------------------------------------------- AS6C/AS7C/AS4C/AS29LV = Series prefix
```

### Example Decoding

```
AS6C4008-55PCN
|  |   |  ||||
|  |   |  |||+-- N = Lead-free (RoHS compliant)
|  |   |  ||+--- C = Commercial temperature (0 to +70C)
|  |   |  |+---- P = PDIP package
|  |   |  +----- 55 = 55ns access time
|  |   +-------- 4008 = 4Mbit density (512K x 8)
|  +------------ 6C = Async SRAM series
+--------------- AS = Alliance Memory prefix

AS4C256M16D3A-12BCN
|  |     | |||  ||||
|  |     | |||  |||+-- N = Lead-free
|  |     | |||  ||+--- C = Commercial temperature
|  |     | |||  |+---- B = BGA package
|  |     | |||  +----- 12 = 12ns (CL=11 DDR3)
|  |     | ||+-------- A = Revision A
|  |     | |+--------- D3 = DDR3 generation
|  |     | +---------- 16 = 16-bit data bus width
|  |     +------------ M = Mega (x1,000,000)
|  +------------------ 256 = 256 Megabits depth
+---------------------- AS4C = SDRAM/DDR series
```

---

## Package Codes

### Memory Package Mapping

| Code | Package | Description | Typical Pin Count |
|------|---------|-------------|-------------------|
| P | PDIP | Plastic Dual In-line Package | 28-40 |
| T | TSOP | Thin Small Outline Package | 28-56 |
| B | BGA | Ball Grid Array | 54-84 |
| J | PLCC | Plastic Leaded Chip Carrier | 32-44 |
| S | SOIC | Small Outline IC | 28-32 |
| V | TSSOP | Thin Shrink Small Outline Package | 28-44 |
| Z | FBGA | Fine-pitch Ball Grid Array | 48-96 |
| W | WSON | Very Very Thin Small Outline No-lead | 8-24 |
| Q | QFN | Quad Flat No-lead | 24-48 |

### Package Selection by Memory Type

| Memory Type | Common Packages |
|-------------|-----------------|
| AS6C (Async SRAM) | PDIP, TSOP, SOIC, PLCC |
| AS7C (High-speed SRAM) | PLCC, TSOP |
| AS4C SDRAM | TSOP |
| AS4C DDR1/DDR2 | TSOP |
| AS4C DDR3 | BGA, FBGA |
| AS29LV Flash | TSOP |

---

## Series Reference

### AS6C - Asynchronous SRAM

Low-power async SRAM for embedded systems, battery-backed applications.

| Part Number | Density | Organization | Typical Speed |
|-------------|---------|--------------|---------------|
| AS6C62256 | 256Kbit | 32K x 8 | 55ns, 70ns |
| AS6C6264 | 64Kbit | 8K x 8 | 55ns, 70ns |
| AS6C1008 | 1Mbit | 128K x 8 | 45ns, 55ns |
| AS6C2016 | 16Kbit | 2K x 8 | 55ns |
| AS6C4008 | 4Mbit | 512K x 8 | 45ns, 55ns |

### AS7C - High-Speed SRAM

Synchronous and high-speed async SRAM for performance applications.

| Part Number | Density | Organization | Typical Speed |
|-------------|---------|--------------|---------------|
| AS7C256 | 256Kbit | 32K x 8 | 10ns, 12ns, 15ns |
| AS7C1024 | 1Mbit | 128K x 8 | 10ns, 12ns |
| AS7C4096 | 4Mbit | 512K x 8 | 12ns |

### AS4C - SDRAM and DDR SDRAM

| Suffix | Type | Description |
|--------|------|-------------|
| SA | SDRAM | Synchronous DRAM (PC100/PC133) |
| D1 | DDR1 | DDR SDRAM (DDR-266/333/400) |
| D2 | DDR2 | DDR2 SDRAM (DDR2-400/533/667) |
| D3 | DDR3 | DDR3 SDRAM (DDR3-1066/1333/1600) |

| Part Number | Density | Organization | Type |
|-------------|---------|--------------|------|
| AS4C4M16SA | 64Mbit | 4M x 16 | SDRAM |
| AS4C8M16SA | 128Mbit | 8M x 16 | SDRAM |
| AS4C16M16SA | 256Mbit | 16M x 16 | SDRAM |
| AS4C32M16SA | 512Mbit | 32M x 16 | SDRAM |
| AS4C32M16D1A | 512Mbit | 32M x 16 | DDR1 |
| AS4C64M16D2A | 1Gbit | 64M x 16 | DDR2 |
| AS4C128M16D3A | 2Gbit | 128M x 16 | DDR3 |
| AS4C256M16D3A | 4Gbit | 256M x 16 | DDR3 |
| AS4C512M16D3A | 8Gbit | 512M x 16 | DDR3 |

### AS29LV - NOR Flash

Legacy-compatible NOR Flash memory.

| Part Number | Density | Notes |
|-------------|---------|-------|
| AS29LV160B | 16Mbit | Boot block flash |
| AS29LV320B | 32Mbit | Boot block flash |
| AS29LV640B | 64Mbit | Large capacity flash |
| AS29LV800B | 8Mbit | Boot block flash |
| AS29LV400B | 4Mbit | Boot block flash |

---

## Temperature Grades

| Code | Grade | Range | Application |
|------|-------|-------|-------------|
| C | Commercial | 0C to +70C | Consumer/office equipment |
| I | Industrial | -40C to +85C | Industrial systems |
| A | Automotive | -40C to +125C | Automotive/harsh environments |

---

## Speed Grades

Speed grade represents the access time in nanoseconds. Lower = faster.

### SRAM Speed Grades

| Speed (ns) | Series | Notes |
|------------|--------|-------|
| 10 | AS7C | Ultra-high speed |
| 12 | AS7C | High-speed |
| 15 | AS7C | High-speed |
| 20 | AS6C, AS7C | Standard |
| 25 | AS6C | Standard |
| 45 | AS6C | Standard |
| 55 | AS6C | Standard (most common) |
| 70 | AS6C | Low-power optimized |

### DDR Speed Grades

| Speed (ns) | Type | DDR Standard |
|------------|------|--------------|
| 5 | DDR1 | DDR-400 |
| 6 | SDRAM | PC133 |
| 7 | SDRAM | PC133 |
| 10 | SDRAM | PC100 |
| 12 | DDR3 | DDR3-1600 (CL=11) |
| 15 | DDR3 | DDR3-1333 (CL=9) |
| 25 | DDR2 | DDR2-667 |

### Flash Speed Grades

| Speed (ns) | Notes |
|------------|-------|
| 70 | Fast access |
| 90 | Standard |
| 120 | Low-power |

---

## Handler Implementation Notes

### Package Code Extraction

```java
// Package code is after the speed grade digits
// AS6C4008-55PCN -> skip "55" (digits), first letter 'P' = PDIP

int hyphenIndex = mpn.indexOf('-');
String suffix = mpn.substring(hyphenIndex + 1);

// Skip speed grade digits
int i = 0;
while (i < suffix.length() && Character.isDigit(suffix.charAt(i))) {
    i++;
}
// suffix.charAt(i) is the package code
```

### Series Extraction

```java
// Extract the series prefix (before density digits)
if (mpn.startsWith("AS29LV")) return "AS29LV";  // Check longest first!
if (mpn.startsWith("AS6C")) return "AS6C";
if (mpn.startsWith("AS7C")) return "AS7C";
if (mpn.startsWith("AS4C")) return "AS4C";
```

**IMPORTANT**: Check "AS29LV" before the shorter prefixes - pattern order matters!

### Density Extraction

```java
// Different format for different series:
// AS6C/AS7C: numeric density after 4-char prefix
//   AS6C4008 -> "4008"
//   AS6C62256 -> "62256"

// AS4C: depth + "M" + width format
//   AS4C4M16SA -> "4M16" (4M x 16)
//   AS4C256M16D3A -> "256M16" (256M x 16)

// AS29LV: numeric density in Mbit (divide by 10 for actual)
//   AS29LV160B -> "160" (16Mbit)
```

### Replacement Logic

The handler implements `isOfficialReplacement()` with these rules:

1. **Same series required**: AS6C cannot replace AS7C
2. **Same density required**: Different densities are not compatible
3. **Faster can replace slower**: 45ns can replace 55ns (not vice versa)
4. **Wider temp can replace narrower**: Industrial can replace Commercial
5. **Package is flexible**: Different packages are replaceable (design consideration)

---

## Component Types

The handler supports:

| ComponentType | Description |
|---------------|-------------|
| `MEMORY` | All memory types (SRAM, SDRAM, DDR) |
| `MEMORY_FLASH` | AS29LV NOR Flash only |

**Note**: AS29LV matches BOTH `MEMORY` and `MEMORY_FLASH` types.

---

## Cross-Reference with Other Manufacturers

Alliance Memory specializes in drop-in replacements:

| Alliance Part | Compatible With | Notes |
|---------------|-----------------|-------|
| AS6C1008 | Cypress CY7C1019 | Pin-compatible async SRAM |
| AS6C4008 | Cypress CY7C1041 | Pin-compatible async SRAM |
| AS6C62256 | Cypress CY7C199 | Classic 32K x 8 SRAM |
| AS4C16M16SA | Micron MT48LC16M16 | SDRAM replacement |
| AS29LV160B | Spansion AM29LV160 | NOR Flash replacement |

---

## Related Files

- Handler: `manufacturers/AllianceMemoryHandler.java`
- Tests: `handlers/AllianceMemoryHandlerTest.java`
- Component types: `MEMORY`, `MEMORY_FLASH`
- Similar handlers: `MicronHandler`, `ISSIHandler`, `WinbondHandler`

---

## Learnings & Edge Cases

- **Series prefix order**: When matching, always check "AS29LV" (6 chars) before "AS6C" (4 chars) - otherwise AS29LV won't match correctly
- **Density format varies by series**: AS6C/AS7C use simple numbers (4008), AS4C uses "depth M width" format (256M16)
- **DDR generation from suffix**: D1=DDR1, D2=DDR2, D3=DDR3, SA=SDRAM
- **Flash dual-type matching**: AS29LV parts match both `MEMORY` and `MEMORY_FLASH` - this is intentional
- **Low power variants**: Some parts have 'L' in the part number (AS6C4008L) for low-power versions
- **Revision letters**: A/B after the base number indicates silicon revision (AS7C1024A, AS29LV160B)
- **Lead-free indicator**: 'N' suffix indicates RoHS-compliant lead-free packaging
- **Temperature in suffix position**: Temperature grade comes after package code, before lead-free indicator (55PCN = P-package, C-temp, N-lead-free)
- **Handler uses Set.of()**: Correctly uses immutable Set.of() in getSupportedTypes()
- **Case-insensitive matching**: Handler converts MPNs to uppercase before pattern matching

<!-- Add new learnings above this line -->
