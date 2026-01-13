---
name: gigadevice
description: GigaDevice Semiconductor MPN encoding patterns, suffix decoding, and handler guidance. Use when working with GigaDevice Flash memory, MCUs, or GigaDeviceHandler.
---

# GigaDevice Semiconductor Manufacturer Skill

## Manufacturer Overview

GigaDevice Semiconductor is a Chinese fabless semiconductor company founded in 2005. They are a major supplier of:

- **Serial NOR Flash Memory** - GD25 series (SPI Flash, compatible with Winbond W25)
- **SLC NAND Flash Memory** - GD5F series
- **ARM Cortex MCUs** - GD32 series (pin-compatible with STM32)
- **RISC-V MCUs** - GD32VF series (open ISA alternative)

GigaDevice is known for providing cost-effective alternatives to Western manufacturers, particularly:
- GD25Qxx Flash as an alternative to Winbond W25Qxx
- GD32Fxxx MCUs as alternatives to STMicroelectronics STM32Fxxx

---

## Supported Component Types

The `GigaDeviceHandler` supports these component types:

| ComponentType | Description |
|---------------|-------------|
| `MEMORY` | Generic memory category |
| `MEMORY_FLASH` | Flash memory (NOR and NAND) |
| `MICROCONTROLLER` | ARM Cortex and RISC-V MCUs |

---

## Flash Memory MPN Structure

### Serial NOR Flash (GD25 Series)

```
GD25 [VARIANT] [DENSITY] [GRADE] [PACKAGE] [QUALIFIER]
  │      │         │        │        │          │
  │      │         │        │        │          └── R/TR = Tape and Reel
  │      │         │        │        └── Package code (see table below)
  │      │         │        └── Optional grade letter (C/E = voltage/temp grades)
  │      │         └── Density in Megabits (16, 32, 64, 128, 256, 512)
  │      └── Variant: Q=Standard, B=Wide VCC, LQ=Low Power, WQ=Wide VCC Low Power, T=High Performance
  └── GD25 = Serial NOR Flash prefix
```

### Flash Variant Types

| Prefix | Description | Voltage Range | Notes |
|--------|-------------|---------------|-------|
| GD25Q | Standard Serial NOR | 2.7V - 3.6V | Most common, Winbond W25Q compatible |
| GD25B | Wide Voltage | 1.65V - 3.6V | Extended voltage range |
| GD25LQ | Low Power | 2.7V - 3.6V | Lower active/standby current |
| GD25WQ | Wide Voltage Low Power | 1.65V - 3.6V | Combines B and LQ features |
| GD25T | High Performance | 2.7V - 3.6V | Higher speed grades |
| GD5F | SLC NAND Flash | 2.7V - 3.6V | NAND technology, higher density |

### Flash Density Codes

The density in Megabits follows the variant prefix:

| Code | Density | Bytes |
|------|---------|-------|
| 16 | 16 Mbit | 2 MB |
| 32 | 32 Mbit | 4 MB |
| 64 | 64 Mbit | 8 MB |
| 128 | 128 Mbit | 16 MB |
| 256 | 256 Mbit | 32 MB |
| 512 | 512 Mbit | 64 MB |

For NAND (GD5F series), density is in Gigabits:

| Code | Density | Bytes |
|------|---------|-------|
| 1G | 1 Gbit | 128 MB |
| 2G | 2 Gbit | 256 MB |
| 4G | 4 Gbit | 512 MB |

### Flash Package Codes

| Suffix | Package | Description |
|--------|---------|-------------|
| SIG | SOP-8 | Standard 8-pin SOIC |
| SIQ | SOP-8 | SOP-8 variant |
| SIP | SOP-8 | SOP-8 variant |
| CSIG | SOP-8 | SOP-8 with extended features |
| ESIG | SOP-8 | SOP-8 extended voltage |
| EIG | SOP-8 | SOP-8 extended |
| WIG | WSON-8 | 6x5mm QFN-style |
| EWIGR | WSON-8 | WSON-8 extended, tape & reel |
| EWIQ | WSON-8 | WSON-8 extended variant |
| WIGR | WSON-8 | WSON-8 tape & reel |
| ZIG | USON-8 | Ultra-small 4x3mm |
| EZIQ | USON-8 | USON-8 extended |
| FIG | WLCSP | Wafer-level chip-scale |
| LIG | SOIC-16 | 16-pin SOIC (dual I/O) |
| NIG | DFN-8 | Small DFN package |
| TIG | TFBGA | Thin fine-pitch BGA |
| TIGR | TFBGA | TFBGA tape & reel |
| UIG | VSOP-8 | Very small SOP |
| BIG | BGA | Ball grid array |

### Flash MPN Examples

```
GD25Q128CSIG
│    │  │  │
│    │  │  └── SIG = SOP-8 package
│    │  └── C = Grade (voltage/temp variant)
│    └── 128 = 128 Mbit (16 MB) density
└── GD25Q = Standard serial NOR flash

GD25Q64EWIGR
│    │ │   │
│    │ │   └── R = Tape and Reel packaging
│    │ └── EWIG = Extended WSON-8 package
│    └── 64 = 64 Mbit (8 MB) density
└── GD25Q = Standard serial NOR flash

GD25LQ16CZIG
│     │  │  │
│     │  │  └── ZIG = USON-8 package
│     │  └── C = Grade variant
│     └── 16 = 16 Mbit (2 MB) density
└── GD25LQ = Low Power serial NOR flash

GD5F1GM7XEYIGR
│   │ │ │   │
│   │ │ │   └── R = Tape and Reel
│   │ │ └── EYIG = Package code
│   │ └── M7 = Variant/generation
│   └── 1G = 1 Gbit (128 MB) density
└── GD5F = SLC NAND flash
```

---

## MCU MPN Structure

### GD32 ARM Cortex MCUs

```
GD32 [FAMILY] [SERIES] [LINE] [PIN] [FLASH] [PKG] [TEMP]
  │      │        │       │     │      │      │      │
  │      │        │       │     │      │      │      └── Temperature grade (6=0-70°C, 7=-40-85°C)
  │      │        │       │     │      │      └── Package type (T=LQFP, K=BGA, etc.)
  │      │        │       │     │      └── Flash size code (8=64KB, B=128KB, etc.)
  │      │        │       │     └── Pin count code (C=48, R=64, V=100, etc.)
  │      │        │       └── Line number within series (01-30)
  │      │        └── Series number (1, 2, 3, 4, 5)
  │      └── Family: F=Cortex-M3, F3=M4, F4=M4F, E=Enhanced, VF=RISC-V, L=Low Power, W=Wi-Fi
  └── GD32 = MCU prefix
```

### MCU Family Types

| Prefix | Core | Compatible With | Notes |
|--------|------|-----------------|-------|
| GD32F1xx | Cortex-M3 | STM32F1xx | Entry-level, most common |
| GD32F3xx | Cortex-M4 | STM32F3xx | DSP instructions, no FPU |
| GD32F4xx | Cortex-M4F | STM32F4xx | FPU, high performance |
| GD32E1xx | Cortex-M3 | - | Enhanced peripherals |
| GD32E2xx | Cortex-M23 | - | TrustZone-M |
| GD32E5xx | Cortex-M33 | - | Enhanced security |
| GD32VF1xx | RISC-V | - | Open ISA, RV32IMAC |
| GD32W5xx | Cortex-M33 | - | Wi-Fi integrated |
| GD32L2xx | Cortex-M23 | - | Ultra-low power |

### MCU Pin Count Codes

| Code | Pin Count | Notes |
|------|-----------|-------|
| T | 36 | Small package |
| K | 32 | Compact |
| C | 48 | Common for prototyping |
| R | 64 | Medium complexity |
| V | 100 | High pin count |
| Z | 144 | Large package |
| I | 176 | Maximum pins |

### MCU Flash Size Codes

| Code | Flash Size | RAM (typical) |
|------|------------|---------------|
| 4 | 16 KB | 4-6 KB |
| 6 | 32 KB | 6-10 KB |
| 8 | 64 KB | 10-20 KB |
| B | 128 KB | 20-32 KB |
| C | 256 KB | 32-48 KB |
| D | 384 KB | 48-64 KB |
| E | 512 KB | 64-96 KB |
| G | 1024 KB | 96-128 KB |
| I | 2048 KB | 128-256 KB |

### MCU Package Codes

| Code | Package | Notes |
|------|---------|-------|
| T | LQFP | Standard QFP, most common |
| C | LQFP | Alternate LQFP code |
| R | LQFP | LQFP variant |
| K | UFBGA | Ultra-fine pitch BGA |
| U | VFQFPN | Very fine pitch QFN |
| H | BGA | Ball grid array |
| V | VFQFPN | QFN variant |
| Y | WLCSP | Wafer-level CSP |
| G | WLCSP | WLCSP variant |

### MCU Temperature Grades

| Code | Range | Application |
|------|-------|-------------|
| 6 | 0°C to +70°C | Commercial |
| 7 | -40°C to +85°C | Industrial |
| 5 | -40°C to +105°C | Extended |

### MCU MPN Examples

```
GD32F103C8T6
│   │  │││││
│   │  ││││└── 6 = Commercial temp (0 to +70°C)
│   │  │││└── T = LQFP package
│   │  ││└── 8 = 64KB Flash
│   │  │└── C = 48 pins
│   │  └── 03 = Line designation (compatible with STM32F103)
│   └── F1 = Cortex-M3, series 1
└── GD32 = MCU prefix

GD32VF103CBT6
│     │  │││││
│     │  ││││└── 6 = Commercial temp
│     │  │││└── T = LQFP package
│     │  ││└── B = 128KB Flash
│     │  │└── C = 48 pins
│     │  └── 03 = Line designation
│     └── VF1 = RISC-V, series 1
└── GD32 = MCU prefix

GD32F407VET7
│   │  ││││││
│   │  │││││└── 7 = Industrial temp (-40 to +85°C)
│   │  ││││└── T = LQFP package
│   │  │││└── E = 512KB Flash
│   │  ││└── V = 100 pins
│   │  │└── 07 = High-performance line
│   │  └── 4 = Series 4
│   └── F = Cortex-M4F family
└── GD32 = MCU prefix
```

---

## Handler Implementation Notes

### Package Code Extraction

```java
// For Flash memory: check suffixes in order (longer first)
String[] suffixesToCheck = {"EWIGR", "CSIG", "ESIG", "WIGR", "TIGR",
        "EWIQ", "EZIQ", "WIG", "SIG", "SIQ", "SIP", "EIG",
        "ZIG", "FIG", "LIG", "NIG", "TIG", "UIG", "BIG"};

// For MCUs: package is second-to-last character
// GD32F103C8T6 -> T = LQFP
char packageChar = mpn.charAt(mpn.length() - 2);
```

### Series Extraction

The handler extracts series by matching specific prefixes:

| MPN Prefix | Series Returned |
|------------|-----------------|
| GD25Q... | GD25Q |
| GD25B... | GD25B |
| GD25LQ... | GD25LQ |
| GD25WQ... | GD25WQ |
| GD25T... | GD25T |
| GD5F... | GD5F |
| GD32VF1... | GD32VF1 |
| GD32F1... | GD32F1 |
| GD32F3... | GD32F3 |
| GD32F4... | GD32F4 |
| GD32E1... | GD32E1 |
| GD32E2... | GD32E2 |
| GD32E5... | GD32E5 |
| GD32W5... | GD32W5 |
| GD32L2... | GD32L2 |

### MCU Line Extraction

```java
// GD32F103C8T6 -> GD32F103 (line)
// GD32VF103CBT6 -> GD32VF103 (line)
// RISC-V has extra 'F' so needs special handling

if (mpn.startsWith("GD32VF")) {
    return mpn.substring(0, 9); // GD32VF103
}
return mpn.substring(0, 8); // GD32F103, GD32E230
```

### Replacement Logic

The handler's `isOfficialReplacement()` method considers parts as valid replacements when:

**For Flash Memory:**
- Same series (e.g., both GD25Q)
- Same density (e.g., both 128 Mbit)
- Package differences are acceptable

**For MCUs:**
- Same MCU line (e.g., both GD32F103)
- Same pin count (e.g., both 48-pin)
- Flash size and package differences are acceptable

---

## STM32 Compatibility

GD32F series MCUs are designed as drop-in replacements for STM32F:

| GD32 | STM32 Equivalent | Compatibility Notes |
|------|------------------|---------------------|
| GD32F103 | STM32F103 | High compatibility, same peripherals |
| GD32F303 | STM32F303 | Good compatibility, minor timing differences |
| GD32F407 | STM32F407 | Compatible with some peripheral differences |

**Important compatibility notes:**
- Clock speeds may differ slightly (GD32 often faster)
- Flash write timing may vary
- Some peripheral register bits may have differences
- USB and CAN peripherals are highly compatible
- Always verify critical timing-sensitive code

---

## Common Patterns

### Registered Pattern Types

```java
// Serial NOR Flash patterns
"^GD25Q\\d+.*"    // Standard
"^GD25B\\d+.*"    // Wide voltage
"^GD25LQ\\d+.*"   // Low power
"^GD25WQ\\d+.*"   // Wide voltage low power
"^GD25T\\d+.*"    // High performance

// NAND Flash pattern
"^GD5F\\d+.*"

// MCU patterns
"^GD32F1\\d{2}.*"   // Cortex-M3
"^GD32F3\\d{2}.*"   // Cortex-M4
"^GD32F4\\d{2}.*"   // High performance M4
"^GD32E[125]\\d{2}.*"  // Enhanced
"^GD32VF1\\d{2}.*"  // RISC-V
"^GD32W5\\d{2}.*"   // Wi-Fi
"^GD32L2\\d{2}.*"   // Low power
```

---

## Related Files

- Handler: `manufacturers/GigaDeviceHandler.java`
- Component types: `MEMORY`, `MEMORY_FLASH`, `MICROCONTROLLER`
- Similar handlers: `WinbondHandler.java` (competing Flash), `STHandler.java` (compatible MCUs)

---

## Learnings & Edge Cases

- **RISC-V MPN parsing**: GD32VF1xx has "VF" as the family code (2 characters), while ARM variants use single letter (F, E, L). The regex patterns handle this with `(VF|[VEFL])` alternation.
- **Flash suffix order**: When extracting package codes, check longer suffixes first (EWIGR before WIG before IG) to avoid partial matches.
- **STM32 compatibility**: While GD32 is advertised as STM32-compatible, real-world projects may need timing adjustments. The Flash programming speed and erase time can differ.
- **Density extraction**: For NAND Flash (GD5F series), density is in Gigabits (e.g., GD5F1GM7 = 1 Gbit), while NOR Flash (GD25) uses Megabits.
- **Package includes pin count**: The `extractPackageCode()` method for MCUs returns package type with pin count (e.g., "LQFP48") when the pin code can be extracted.
- **Tape and reel suffix**: The "R" or "TR" suffix indicates tape and reel packaging and should be stripped before package code extraction.
- **Handler uses Set.of()**: The `getSupportedTypes()` method correctly uses `Set.of()` (immutable) rather than `HashSet`.

<!-- Add new learnings above this line -->
