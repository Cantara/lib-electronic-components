---
name: kingbright
description: Kingbright LED MPN encoding patterns, suffix decoding, and handler guidance. Use when working with through-hole or SMD LEDs, LED displays, and optoelectronic components.
---

# Kingbright Manufacturer Skill

## MPN Structure

Kingbright MPNs vary by product type but follow these general structures:

### Through-Hole LEDs (WP series)

```
WP[SIZE][COLOR][VARIANT]
│  │     │      │
│  │     │      └── Optional: /D=diffused, /C=clear, etc.
│  │     └── Color code (ID=red, SGC=green, SBC=blue)
│  └── Size code (7113=5mm, 3114=3mm, 34=3mm flat)
└── WP = Lead-free through-hole (replaced older "L" prefix)
```

### SMD LEDs (KP, APT, AA series)

```
KP-[SIZE][COLOR][SUFFIX]
│  │      │      │
│  │      │      └── Optional: PRV=lead-free, K=kelvin, etc.
│  │      └── Color code (SRC=red, CGC=green, SBC=blue)
│  └── Metric size (2012=0805, 3216=1206, 1608=0603)
└── KP = SMD top-emitting series

APTD[SIZE][COLOR][SUFFIX]
│  │ │     │      │
│  │ │     │      └── Optional suffix
│  │ │     └── Color code
│  │ └── Metric size
│  └── D = indicates specific variant
└── APT = Right-angle/side-emitting SMD

AA[SIZE][COLOR][SUFFIX]
│  │     │      │
│  │     │      └── Optional: SK, K suffix
│  │     └── Color code (SURSK=super bright red)
│  └── Metric size (3528=PLCC-4, 5060=PLCC-6)
└── AA = PLCC package SMD
```

---

## Example Decoding

```
WP7113ID
│  │   │
│  │   └── ID = Red (InGaAlP die)
│  └── 7113 = T-1 3/4 (5mm) package
└── WP = Lead-free through-hole

APTD3216SRCPRV
│   │    │    │
│   │    │    └── PRV = Pb-free/RoHS
│   │    └── SRC = Super Red Clear
│   └── 3216 = 1206 imperial size (3.2mm x 1.6mm)
└── APTD = Right-angle SMD LED

KP-2012CGCK
│  │    │
│  │    └── CGC = Clear Green, K variant
│  └── 2012 = 0805 imperial size (2.0mm x 1.2mm)
└── KP = SMD top-emitting

AA3528SURSK
│  │    │
│  │    └── SURSK = Super Ultra Red, SK bin code
│  └── 3528 = 1411 imperial (3.5mm x 2.8mm PLCC-4)
└── AA = PLCC SMD package
```

---

## Series Prefixes

| Prefix | Type | Description |
|--------|------|-------------|
| WP | Through-hole | Lead-free through-hole LEDs (replaced "L" prefix) |
| KP | SMD top-emitting | Low-profile chip LEDs |
| AP/APT | SMD right-angle | Side-emitting/side-view LEDs |
| APTD | SMD right-angle | Right-angle LED variant |
| AA | SMD PLCC | PLCC package (3528, 5060) |
| KA | SMD | Alternative SMD series |

---

## Size Code Reference

### Metric to Imperial Conversion

| Metric (mm) | Imperial | Common Name |
|-------------|----------|-------------|
| 0603 | 0201 | Micro LED |
| 1005 | 0402 | Ultra-small SMD |
| 1608 | 0603 | Small SMD |
| 2012 | 0805 | Standard SMD |
| 3216 | 1206 | Large SMD |
| 3528 | 1411 | PLCC-4 |
| 5060 | 2024 | PLCC-6 |

### Through-Hole Size Codes

| Code | Size | Common Name |
|------|------|-------------|
| 7113 | 5mm | T-1 3/4 |
| 3114 | 3mm | T-1 |
| 934 | 5mm | T-1 3/4 flat top |
| 44 | 3mm | T-1 flat top |
| 34 | 3mm | T-1 |

---

## Color Codes

### Common Color Designators

| Code | Color | Die Material |
|------|-------|--------------|
| ID | Red | InGaAlP (Indium Gallium Aluminum Phosphide) |
| SRC | Red | Super Red Clear |
| SURSK | Red | Super Ultra Red (bright) |
| SURSCK | Red | Super Ultra Red Clear (brightest) |
| SGC | Green | GaP (Gallium Phosphide) |
| CGC | Green | Clear Green |
| SBC | Blue | InGaN (Indium Gallium Nitride) |
| BC | Blue | Blue Clear |
| SEC | Yellow | Standard yellow/amber |
| YGC | Yellow | Yellow-Green Clear |
| SWC | White | White LED |
| WC | White | White Clear |
| RGB | RGB | Multi-color (Red/Green/Blue) |

---

## Common Part Numbers

### Through-Hole (5mm, T-1 3/4)

| MPN | Color | Description |
|-----|-------|-------------|
| WP7113ID | Red | 5mm red, diffused |
| WP7113SGC | Green | 5mm green |
| WP7113SBC | Blue | 5mm blue |
| WP7113SEC | Yellow | 5mm yellow/amber |
| WP7113SWC | White | 5mm white |

### Through-Hole (3mm, T-1)

| MPN | Color | Description |
|-----|-------|-------------|
| WP3114ID | Red | 3mm red |
| WP3114SGC | Green | 3mm green |
| WP3114SBC | Blue | 3mm blue |

### SMD (0805 / 2012)

| MPN | Color | Description |
|-----|-------|-------------|
| KP-2012SRCPRV | Red | 0805 red SMD |
| KP-2012CGCK | Green | 0805 green SMD |
| APT2012SRCPRV | Red | 0805 right-angle red |
| APT2012CGCK | Green | 0805 right-angle green |

### SMD (1206 / 3216)

| MPN | Color | Description |
|-----|-------|-------------|
| APTD3216SRCPRV | Red | 1206 right-angle red |
| APTD3216CGCK | Green | 1206 right-angle green |
| KP-3216SRCPRV | Red | 1206 top-emitting red |

### SMD PLCC (3528)

| MPN | Color | Description |
|-----|-------|-------------|
| AA3528SURSK | Red | PLCC-4 super bright red |
| AA3528CGSK | Green | PLCC-4 green |
| AA3528ESGW | Green | PLCC-4 green-white |

---

## Handler Implementation Notes

### Package Code Extraction

```java
// Through-hole: Map size code to package name
// WP7113ID -> 7113 -> T-1-3/4 (5mm)
// WP3114SGC -> 3114 -> T-1 (3mm)

// SMD: Convert metric to imperial
// KP-2012CGCK -> 2012 -> 0805
// APTD3216SRCPRV -> 3216 -> 1206
// AA3528SURSK -> 3528 -> 1411 (PLCC)
```

### Series Extraction

```java
// Through-hole: WP + size code
// WP7113ID -> WP7113
// WP3114SGC -> WP3114

// SMD: Prefix + size code
// KP-2012CGCK -> KP-2012
// APTD3216SRCPRV -> APTD3216
// AA3528SURSK -> AA3528
```

### Color Extraction

```java
// Look for standard color code patterns
// *SRC*, *SURSK* = Red
// *SGC*, *CGC*, *GC* = Green
// *SBC*, *BC* = Blue
// *SEC*, *YGC* = Yellow
// *WC*, *SWC* = White
// *RGB* = RGB multi-color
```

---

## Related Files

- Handler: `manufacturers/KingbrightHandler.java`
- Component types: `LED`, `LED_STANDARD_KINGBRIGHT`, `LED_SMD_KINGBRIGHT`, `LED_RGB_KINGBRIGHT`
- Test: `handlers/KingbrightHandlerTest.java`

---

## Learnings & Edge Cases

- **WP replaced L prefix** - Old Kingbright LEDs used "L" prefix (L7113ID), new lead-free versions use "WP" (WP7113ID)
- **KP with/without dash** - Both KP-2012 and KP2012 formats exist; handler should match both
- **APTD vs APT** - Both are right-angle LEDs; APTD is a newer/different variant designation
- **Metric sizes in MPN** - Unlike most manufacturers, Kingbright uses metric sizes (2012 not 0805) in part numbers
- **Color codes are substring-matched** - Position varies; "SRCPRV" contains "SRC" for red
- **PRV suffix** - Indicates Pb-free/RoHS compliant (most modern parts have this)
- **K suffix** - Often indicates Kelvin color temperature variant for white LEDs
- **SK suffix** - Often appears on AA series as bin code identifier
- **Same series = replaceable** - LEDs from same series (e.g., WP7113) but different colors are considered replaceable (same footprint)

<!-- Add new learnings above this line -->
