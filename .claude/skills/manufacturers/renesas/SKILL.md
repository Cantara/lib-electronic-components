# Renesas Part Number Reference

## MPN Structure Overview

Renesas part numbers follow structured formats that vary by product family. The main MCU families are:
- **RL78**: 8/16-bit low-power MCUs (R5F1xxxx)
- **RX**: 32-bit general purpose MCUs (R5Fxxxxx)
- **RA**: 32-bit Arm Cortex-M MCUs (R7FAxxxx)
- **RH850**: 32-bit automotive MCUs (R7F7xxxx)
- **R8C**: 16-bit compact MCUs (R5Fxxxx)

---

## General Part Number Format

```
R[Core][Family][Series][Features][Package][Grade]
```

### Core Designator

| Prefix | Description |
|--------|-------------|
| R5F | Flash MCU (RL78, RX, R8C families) |
| R7F | Flash MCU (RA, RH850 families) |
| R1EX | Flash Memory |
| R1LV | Low Voltage Memory |

---

## RL78 Family (8/16-bit Low-Power)

### Part Number Format

Format: `R5F1[Group][Pin/ROM][Grade][Package]#[Version]`

Example: `R5F100LEAFB#30`
- **R5F1**: RL78 Flash MCU
- **00**: RL78/G13 group (00-01 = G13)
- **L**: 64-pin variant
- **E**: 64KB Flash
- **A**: Consumer temp grade
- **FB**: LFQFP package
- **#30**: Production version

### RL78 Series Groups

| Part Number Pattern | Series | Description |
|---------------------|--------|-------------|
| R5F100xx, R5F101xx | RL78/G13 | General purpose, low power |
| R5F102xx | RL78/G14 | Motor control, industrial |
| R5F104xx | RL78/G1C | Ultra-low power |
| R5F10Axx | RL78/G10 | Small pin count |
| R5F10Pxx | RL78/G12 | Touch key |
| R5F10Yxx | RL78/F13 | Automotive |
| R5F11xxx | RL78/I1x | LCD driver |

### RL78 Pin Count Codes (G13 Series)

| Code | Pin Count |
|------|-----------|
| 6, 7 | 20-pin |
| 7 | 24-pin |
| 8 | 25-pin |
| A | 30-pin |
| B | 32-pin |
| C | 36-pin |
| E | 40-pin |
| F | 44-pin |
| G | 48-pin |
| J | 52-pin |
| L | 64-pin |
| M | 80-pin |
| P | 100-pin |

### RL78 Memory Size Codes

| Code | Flash Size | RAM Size |
|------|------------|----------|
| A | 2KB | 256B |
| C | 4KB | 512B |
| D | 8KB | 768B |
| E | 16KB | 1.5KB |
| F | 32KB | 2KB |
| G | 48KB | 4KB |
| H | 64KB | 4KB |
| J | 96KB | 8KB |
| K | 128KB | 12KB |
| L | 256KB | 20KB |

---

## RX Family (32-bit General Purpose)

### Part Number Format

Format: `R5F[Series][Group][ROM/RAM][Package]#[Version]`

Example: `R5F5631EDDFP#V1`
- **R5F**: Flash MCU
- **56**: RX600 series
- **31**: RX631 group
- **E**: ROM/RAM size code
- **DD**: 100-pin variant
- **FP**: LQFP package
- **#V1**: Production version

### RX Series Groups

| Part Number Pattern | Series | Core Speed | Focus |
|---------------------|--------|------------|-------|
| R5F521xx | RX210 | 50MHz | Low power |
| R5F523xx | RX230/231 | 54MHz | Low power, USB |
| R5F524xx | RX24x | 80MHz | Motor control |
| R5F51xxx | RX100 | 32MHz | Entry-level |
| R5F563xx | RX63x | 100MHz | General purpose |
| R5F564xx | RX64M | 120MHz | High performance |
| R5F565xx | RX65x | 120MHz | Ethernet, USB |
| R5F566xx | RX66x | 120MHz | Security |
| R5F571xx | RX71x | 240MHz | High performance |
| R5F572xx | RX72x | 240MHz | Motor control |

### RX Memory Size Codes

| Code | Flash | RAM | Data Flash |
|------|-------|-----|------------|
| J | 16KB | 8KB | 8KB |
| 1 | 32KB | 10KB | 8KB |
| 3 | 64KB | 10KB | 8KB |
| 4 | 96KB | 16KB | 8KB |
| 5 | 128KB | 16KB | 8KB |
| 6 | 256KB | 32KB | 8KB |
| 7 | 384KB | 64KB | 8KB |
| 8 | 512KB | 64KB | 8KB |
| D | 1MB | 128KB | 32KB |
| E | 2MB | 256KB | 64KB |

---

## RA Family (32-bit Arm Cortex-M)

### Part Number Format

Format: `R7FA[Series][Features][Package][Grade]#[AA0]`

Example: `R7FA4M1AB3CFP#AA0`
- **R7F**: Flash MCU
- **A**: RA family
- **4M1**: RA4M1 series
- **AB**: Feature set
- **3**: Temperature grade (105C)
- **C**: Quality grade
- **FP**: LQFP package
- **#AA0**: Tin terminals, tray packing

### RA Series Groups

| Part Number Pattern | Series | Core | Speed | Focus |
|---------------------|--------|------|-------|-------|
| R7FA2xxx | RA2 | Cortex-M23 | 48MHz | Entry-level |
| R7FA4xxx | RA4 | Cortex-M33 | 100MHz | General purpose |
| R7FA6xxx | RA6 | Cortex-M33 | 200MHz | High performance |

### RA Sub-series

| Series | Description |
|--------|-------------|
| RA2A1 | Entry-level, analog |
| RA2E1 | Entry-level, low power |
| RA2L1 | Entry-level, capacitive touch |
| RA4M1 | Mid-range general purpose |
| RA4M2 | Mid-range with security |
| RA4M3 | Mid-range enhanced |
| RA6M1 | High performance |
| RA6M2 | High performance with Ethernet |
| RA6M3 | High performance with TFT |
| RA6M4 | High performance with security |
| RA6T1 | Motor control |
| RA6T2 | Motor control enhanced |

---

## RH850 Family (32-bit Automotive)

### Part Number Format

Format: `R7F70[Group][Variant][Package]`

Example: `R7F7016023AFP-C`
- **R7F70**: RH850 Flash MCU
- **160**: F1K series
- **23**: Variant
- **A**: Automotive grade
- **FP**: QFP package
- **-C**: Industrial quality

### RH850 Series Groups

| Part Number Pattern | Series | Focus |
|---------------------|--------|-------|
| R7F7010xx | RH850/C1x | Chassis |
| R7F7012xx | RH850/C1M | Chassis |
| R7F7014xx | RH850/D1x | Display |
| R7F7016xx | RH850/F1K | General automotive |
| R7F7017xx | RH850/F1KM | General automotive |
| R7F7020xx | RH850/E2x | Engine control |
| R7F7023xx | RH850/U2A | High-end automotive |

---

## R8C Family (16-bit Compact)

### Part Number Format

Format: `R5F2[Series][ROM][Features][Package]#[Version]`

Example: `R5F21358SNFP#V0`
- **R5F2**: R8C Flash MCU
- **13**: R8C/13 group
- **58**: ROM/Feature variant
- **S**: Grade
- **N**: Feature
- **FP**: LQFP package

---

## Package Codes

### Common Package Suffixes

| Code | Package Type | Pitch |
|------|--------------|-------|
| FP | LQFP | 0.80mm |
| FM | LFQFP | 0.50mm |
| FB | LFQFP | 0.50mm |
| FK | LQFP | 0.80mm |
| FL | LFQFP | 0.50mm (48-pin) |
| FA | LQFP | 0.65mm |
| SP | LSSOP | 0.65mm |
| NP | HWQFN | 0.50mm |
| NE | HWQFN | 0.50mm |
| LA | WFLGA | 0.50mm |
| BG | VFBGA | 0.40mm |
| LF | LGA | Various |

### Package Pin Count Examples

| Pattern | Description |
|---------|-------------|
| xxFP | LQFP 100-pin |
| xxFM | LFQFP 64-pin |
| xxFB | LFQFP 64-pin |
| xxFL | LFQFP 48-pin |
| xxBG | BGA various |

---

## Temperature Grade Codes

### RL78/RX Temperature Grades

| Code | Temperature Range | Application |
|------|-------------------|-------------|
| A | -40C to +85C | Consumer |
| C | -40C to +85C | Industrial |
| D | -40C to +105C | Industrial extended |
| G | -40C to +85C | Industrial |

### RA Temperature Grades

| Code | Temperature Range |
|------|-------------------|
| 2 | -40C to +85C |
| 3 | -40C to +105C |
| 4 | -40C to +125C |
| 5 | -40C to +150C |

### RH850 Temperature Grades

| Code | Temperature Range | Application |
|------|-------------------|-------------|
| A | -40C to +150C (Tj) | Automotive |

---

## Quality Grade Codes

| Code | Application |
|------|-------------|
| A | Automotive |
| C | Industrial |
| D | Consumer |

---

## Production/Packing Codes

### Version Suffix (after #)

| Code | Meaning |
|------|---------|
| V0, V1 | Version/revision |
| U0, U2, U3 | Tray packing |
| 30 | Standard production |

### Packing Type (in suffix)

| Code | Packing |
|------|---------|
| A | Tray |
| B | Tray (full carton) |
| H | Tape and reel |

---

## Handler Implementation Notes

### Current RenesasHandler Issues

1. **HashSet Usage**: Uses `new HashSet<>()` instead of `Set.of()` - mutable set returned
2. **Package Extraction Logic**: Simple algorithm finds last digit and takes everything after it
   - Works for patterns like `R5F51303ADFN` -> `DFN`
   - May fail for complex suffixes with version numbers

### Pattern Registration

The handler registers patterns for:
- RX Series: `^R5F[0-9]+.*`
- RL78 Series: `^R5F1[0-9]+.*`
- RH850 Series: `^R7F[0-9]+.*`
- RA Series: `^R7FA[0-9]+.*`
- R8C/RE Series: `^R8C[0-9]+.*`
- Memory: `^R1EX[0-9]+.*`, `^R1LV[0-9]+.*`

### Overlapping Patterns Issue

Note: RL78 pattern `^R5F1[0-9]+.*` is a subset of RX pattern `^R5F[0-9]+.*`
This means RL78 parts will match both patterns.

### Supported ComponentTypes

```java
ComponentType.MICROCONTROLLER
ComponentType.MICROCONTROLLER_RENESAS
ComponentType.MCU_RENESAS
ComponentType.MEMORY
ComponentType.MEMORY_RENESAS
```

### Series Extraction Logic

Current implementation:
- `R5F1xxx` -> "RL78"
- `R5Fxxx` (not R5F1) -> "RX"
- `R7FA` -> "RA"
- `R7F` (not R7FA) -> "RH850"
- `R8C` -> "R8C"
- `R1EX` -> "Flash"
- `R1LV` -> "Low Voltage"

---

## Test Patterns

### Valid RL78 MPNs

```
R5F100LEAFA#30
R5F100LEAFB#30
R5F100GCAFB#30
R5F100PJDFB#30
R5F101LCAFB#30
R5F10268ASP#V0
R5F10YPLAGFB#V0
```

### Valid RX MPNs

```
R5F51303ADFM
R5F51303ADFN
R5F5631EDDFP#V1
R5F562N8BDFB#V0
R5F564MLDDFP#V1
R5F52108ADFM
R5F52318AXFM
R5F565NEDDFB
R5F572MDDDFB#30
```

### Valid RA MPNs

```
R7FA2A1AB3CFM#AA0
R7FA2E1A92DFM
R7FA4M1AB3CFP#AA0
R7FA4M2AD3CFP
R7FA6M1AD3CFP
R7FA6T1AD3CFP
```

### Valid RH850 MPNs

```
R7F7016023AFP
R7F7016024AFP-C
R7F701695AFP
R7F701278ABG
R7F702011EABG
```

### Valid Memory MPNs

```
R1EX25064ASA
R1LV0416CSB
```

---

## References

- [Renesas Orderable Part Number Guide](https://www.renesas.com/en/support/product-naming-guide)
- [RL78 Family Product Part Number Guide](https://www.renesas.com/en/document/gde/rl78-family-product-part-number-guide)
- [RX Family Product Part Number Guide](https://www.renesas.com/en/document/gde/rx-family-product-part-number-guide)
- [RA Family Nomenclature](https://www.renesas.com/en/document/gde/ra-family-nomenclature)
- [RH850 Family Part Number Guide](https://www.renesas.com/en/document/gde/renesas-mcu-product-part-number-guide-rh850-family)
- [Renesas Package Code System](https://www.renesas.com/en/support/technical-resources/packaging/packing-outline/renesas-code)
