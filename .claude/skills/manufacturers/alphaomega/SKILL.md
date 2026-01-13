# Alpha and Omega Semiconductor (AOS) Handler Skill

Use this skill when working with Alpha and Omega Semiconductor (AOS) components - primarily MOSFETs.

## Company Overview

Alpha and Omega Semiconductor (AOSMD) specializes in power semiconductors, particularly MOSFETs. They are known for:
- High-performance power MOSFETs for computing, consumer, industrial, and automotive applications
- Innovative packaging technologies (TOLL, GTPAK, GLPAK)
- Shield Gate Technology (SGT) for low Rds(on)
- Both N-channel and P-channel devices

## MPN Structure

AOS part numbers follow this pattern:

```
AO[Package Prefix][Series Number][Variant Letter]
```

### Package Prefixes

| Prefix | Package Type | Description |
|--------|-------------|-------------|
| `AO3xxx` | SOT-23 | Small outline transistor, 3-pin |
| `AO4xxx` | SO-8 | Small outline, 8-pin |
| `AOD` | TO-252 (DPAK) | Decawatt package |
| `AON` | DFN | Dual flat no-lead (various sizes) |
| `AOI` | TO-251 (IPAK) | I-pak package |
| `AOT` | TO-220 | Through-hole power package |
| `AOB` | TO-263 (D2PAK) | Double DPAK |
| `AOC` | SO-8 | Common drain dual MOSFETs |
| `AOP` | PDFN | Power DFN |
| `AOTL` | TOLL | TO-Leadless (30% smaller than D2PAK) |
| `AOGT` | GTPAK | Topside cooling package |
| `AOGL` | GLPAK | Gull-wing TOLL |
| `AONS` | DFN | DFN source-down |
| `AONR` | DFN | DFN source-down |
| `AONK` | DFN3.3x3.3 | Compact DFN source-down |

### Variant Letters

- `A` - Revision or improved version
- `L` - Low Rds(on) variant

### Examples

| MPN | Package | Type | Notes |
|-----|---------|------|-------|
| `AO3401A` | SOT-23 | P-channel | 30V, 4A |
| `AO3400A` | SOT-23 | N-channel | 30V, 5.7A |
| `AO4407A` | SO-8 | P-channel | 30V, 12A |
| `AO4988` | SO-8 | N-channel | 40V, 10A |
| `AOD4184` | TO-252 | N-channel | 40V, 50A |
| `AON6758` | DFN 5x6 | N-channel | 30V, 85A |
| `AOT240L` | TO-220 | N-channel | 40V, 79A |
| `AOGT66909` | GTPAK | N-channel | Topside cooling |

## Handler Implementation

### File Location
```
src/main/java/no/cantara/electronic/component/lib/manufacturers/AlphaOmegaHandler.java
```

### Supported Component Types
- `MOSFET`

### Key Methods

#### `matches(String mpn, ComponentType type, PatternRegistry patterns)`
Checks if MPN matches AOS MOSFET patterns.

#### `extractPackageCode(String mpn)`
Returns package type based on prefix:
- `AO3xxx` -> `SOT-23`
- `AO4xxx` -> `SO-8`
- `AOD` -> `TO-252`
- `AON` -> `DFN`
- etc.

#### `extractSeries(String mpn)`
Returns base series without variant letters:
- `AO3401A` -> `AO3401`
- `AOD4184A` -> `AOD4184`

## Test Coverage

### Test File Location
```
src/test/java/no/cantara/electronic/component/lib/handlers/AlphaOmegaHandlerTest.java
```

### Test Categories
- SOT-23 Package MOSFETs (AO3xxx)
- SO-8 Package MOSFETs (AO4xxx)
- TO-252/DPAK Package MOSFETs (AOD)
- DFN Package MOSFETs (AON)
- TO-251/IPAK Package MOSFETs (AOI)
- TO-220 Package MOSFETs (AOT)
- TO-263/D2PAK Package MOSFETs (AOB)
- Advanced Packages (AOTL, AOGT, AOGL, AONS, AONR, AONK)
- Common Drain Dual MOSFETs (AOC)
- PDFN Package MOSFETs (AOP)
- Package Code Extraction
- Series Extraction
- Edge Cases
- Manufacturer Detection

## Common Tasks

### Adding New Package Types
1. Add pattern to `initializePatterns()`
2. Add explicit check in `matches()`
3. Add package mapping in `extractPackageCode()`
4. Add series extraction logic in `extractSeries()`
5. Add tests for new patterns

### Run Tests
```bash
mvn test -Dtest=AlphaOmegaHandlerTest
```

## Learnings & Quirks

### Package Encoding
- Position 3-4 after "AO" indicates package type for basic series
- Extended prefixes (AOTL, AOGT, etc.) are 4 characters
- Series numbers typically 3-5 digits

### N-channel vs P-channel
- Part number doesn't directly encode polarity
- Must reference datasheet or use external lookup
- Even numbers often N-channel, odd numbers often P-channel (not a strict rule)

### Variant Letters
- `A` suffix common for improved versions
- `L` suffix for low Rds(on) variants
- No suffix for original versions

### References
- [AOS MOSFET Product Page](https://www.aosmd.com/products/mosfets)
- [DigiKey Forum: Identifying AOS MOSFETs](https://forum.digikey.com/t/identifying-alpha-and-omega-semiconductor-mosfets/19458)

---
*Last Updated: January 2026*
