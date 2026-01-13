# WIMA Handler Skill

Use this skill when working with WIMA premium film capacitors - adding patterns, parsing WIMA MPNs, extracting capacitance values, voltage ratings, dielectric types, or package codes from WIMA part numbers.

## Overview

WIMA is a German manufacturer specializing in high-quality film capacitors for audio, pulse, and general applications. They are particularly renowned for their audio-grade capacitors.

## Series Naming Convention

WIMA uses a 3-letter prefix system followed by a size code:

### First Letter - Construction Type
- **M** = Metallized (thin metal layer on film) - self-healing
- **F** = Film/Foil (discrete metal foil electrodes) - lowest losses

### Second Letter
- **K** = Kunststoff (German for "plastic film capacitor")

### Third Letter - Dielectric Material
- **S** = Polyester (PET) - good general purpose, higher capacitance per volume
- **P** = Polypropylene (PP) - lower losses, better for audio/pulse
- **C** = Polycarbonate (legacy, discontinued)

### Common Series

| Series | Construction | Dielectric | Typical Use |
|--------|--------------|------------|-------------|
| **MKS** | Metallized | Polyester (PET) | General purpose, compact, coupling |
| **MKP** | Metallized | Polypropylene (PP) | Audio, snubber, pulse |
| **FKS** | Foil | Polyester (PET) | Precision, low loss |
| **FKP** | Foil | Polypropylene (PP) | Highest quality, premium audio |
| **FKC** | Foil | Polycarbonate | Legacy (discontinued) |

### Size Codes (Lead Spacing/Pitch)

| Code | Pitch |
|------|-------|
| 2 | 5mm |
| 3 | 7.5mm |
| 4 | 7.5mm |
| 6 | 10mm |
| 10 | 15mm (MKP10 pulse) |
| 20 | 22.5mm |

## MPN Structure

Format: `[Series][Size]-[Value]/[Voltage]/[Tolerance]`

### Examples

| MPN | Series | Size | Value | Voltage | Tolerance |
|-----|--------|------|-------|---------|-----------|
| MKS4-100/63/10 | MKS | 4 (7.5mm) | 100nF | 63V | 10% |
| MKP10-0.01/1000/10 | MKP | 10 (15mm) | 10nF | 1000V | 10% |
| FKP2-330/100/5 | FKP | 2 (5mm) | 330pF | 100V | 5% |
| MKS2-100/63/10 | MKS | 2 (5mm) | 100nF | 63V | 10% |

### Value Formats
- `100` = 100nF (default unit for larger values)
- `0.01` = 10nF (decimal format)
- `330P` = 330pF (with unit suffix)
- `100N` = 100nF (with unit suffix)
- `1U` = 1uF (with unit suffix)

## Handler Implementation

### Key Files
- Handler: `src/main/java/.../manufacturers/WIMAHandler.java`
- Tests: `src/test/java/.../handlers/WIMAHandlerTest.java`

### Supported ComponentTypes
- `ComponentType.CAPACITOR` (base type)
- `ComponentType.CAPACITOR_FILM_WIMA` (manufacturer-specific)

### Key Methods

```java
// Check if MPN is a WIMA capacitor
handler.matches(mpn, ComponentType.CAPACITOR_FILM_WIMA, registry)

// Extract series (e.g., "MKS4", "MKP10", "FKP2")
handler.extractSeries(mpn)

// Extract package/pitch (e.g., "5mm", "7.5mm", "15mm")
handler.extractPackageCode(mpn)

// Get dielectric type (e.g., "PET", "PP", "PC")
handler.getDielectricType(mpn)

// Get construction type (e.g., "Metallized", "Foil")
handler.getConstructionType(mpn)
```

### Pattern Matching

The handler uses regex patterns to identify WIMA parts:
```java
// Main series pattern
"^(MKS|MKP|FKS|FKP|FKC)\\d+.*"

// SMD series pattern
"^SMD[PM]\\d+.*"
```

## Audio Applications

WIMA capacitors are highly regarded in audio applications:

### Recommended Series for Audio
1. **FKP** - Foil Polypropylene: Best for critical audio paths (signal coupling)
2. **MKP** - Metallized Polypropylene: Good balance of performance/cost
3. **MKS** - Metallized Polyester: Budget option for less critical applications

### Audio Application Examples
- Input/output coupling: FKP2 or MKP series
- Power supply decoupling: MKP4 or MKP10
- Tone control: FKP2 for precision
- Crossover networks: MKP series

## Replacement Guidelines

### Compatible Replacements
- Same series prefix (MKS, MKP, etc.)
- Same size code (same lead spacing)
- Same or higher voltage rating
- Same or tighter tolerance

### Upgrade Paths
| Original | Upgrade | Notes |
|----------|---------|-------|
| MKS | MKP | Lower loss, better audio |
| MKS | FKS | Lower loss |
| MKP | FKP | Lowest loss, premium |
| FKS | FKP | Better dielectric |

## Testing Examples

```java
// Test MPN detection
assertTrue(handler.matches("MKS4-100/63/10", ComponentType.CAPACITOR_FILM_WIMA, registry));

// Test series extraction
assertEquals("MKS4", handler.extractSeries("MKS4-100/63/10"));

// Test package extraction
assertEquals("7.5mm", handler.extractPackageCode("MKS4-100/63/10"));

// Test dielectric type
assertEquals("PET", handler.getDielectricType("MKS4-100/63/10"));
assertEquals("PP", handler.getDielectricType("MKP10-100/1000/10"));

// Test construction type
assertEquals("Metallized", handler.getConstructionType("MKS4-100/63/10"));
assertEquals("Foil", handler.getConstructionType("FKP2-330/100/5"));
```

## Learnings & Quirks

### Pattern Matching Notes
- WIMA MPNs can use hyphens or underscores as separators
- Value can be in various formats: decimal (0.01), integer (100), or with suffix (330P)
- Size code immediately follows the series prefix without separator

### Color Coding (Physical Identification)
Historical WIMA color coding:
- **MKS (PET Metallized)**: Red case, white/silver text
- **MKP (PP Metallized)**: Red case, black text
- **FKS (PET Foil)**: Red case with yellow band, white/silver text
- **FKP (PP Foil)**: Red case with yellow band, black text

Note: Since 2024, WIMA has standardized to black marking on all capacitors.

### Known Limitations
- SMD patterns (SMDP, SMDM) are less common and may have different formats
- Legacy FKC (polycarbonate) series is discontinued but still in some legacy designs
- Some alternative MPN formats exist (e.g., MKS4C024703C00KSSD)

---

## Sources
- [WIMA Part Number System](https://www.wima.de/en/our-product-range/pulse-capacitors/fkp-1/partnumber-system/)
- [WIMA SMD Part Numbers](https://www.wima.de/en/our-product-range/smd-capacitors/smd-pet/bestellnummer-systematik/)
- [WIMA Audio Capacitors](https://www.wima.de/wp-content/uploads/media/WIMA-Audio.pdf)
