---
name: nipponchemicon
description: Nippon Chemi-Con MPN encoding patterns, suffix decoding, and handler guidance. Use when working with Nippon Chemi-Con aluminum electrolytic and polymer capacitors.
---

# Nippon Chemi-Con Manufacturer Skill

## MPN Structure

Nippon Chemi-Con MPNs follow this general structure:

```
[PREFIX][SERIES][VOLTAGE][SUFFIX][CAP_CODE][CASE_SIZE]
   |       |        |       |        |         |
   |       |        |       |        |         +-- Case code (MJC5S, MHD, etc.)
   |       |        |       |        +-- 3-digit capacitance code (101=100uF)
   |       |        |       +-- Internal code (ELL, etc.)
   |       |        +-- 3-digit voltage (500=50V) or digit+letter (1C=16V)
   |       +-- Series code (YE, ZE, XG, etc.)
   +-- EK/E/M prefix (aluminum electrolytic prefix)
```

### Example Decoding

```
EKYE500ELL101MJC5S
|  | |  |  |  |
|  | |  |  |  +-- MJC5S = 5x5.3mm SMD case
|  | |  |  +-- 101 = 100uF (10 x 10^1)
|  | |  +-- ELL = Internal code
|  | +-- 500 = 50V (voltage code: 500/10 = 50V)
|  +-- YE = KY series internal code
+-- EK = Aluminum electrolytic prefix

KMG160ELL471M
|  | | |  |
|  | | |  +-- M suffix
|  | | +-- 471 = 470uF (47 x 10^1)
|  | +-- ELL = Internal code
|  +-- 160 = 16V (voltage code: 160/10 = 16V)
+-- KMG = KMG General Purpose Mini series

PSC1V471M
|  | |  |
|  | |  +-- M suffix
|  | +-- 471 = 470uF (47 x 10^1)
|  +-- 1V = 35V (EIA voltage code)
+-- PSC = Polymer Solid C series
```

---

## Series Reference

### Low Impedance Series

| Series | Full Name | Prefix Pattern | Description |
|--------|-----------|----------------|-------------|
| KY | KY Low Impedance | EKYE, EKY | High ripple current, low ESR |
| KXJ | KXJ High Temp Low Impedance | EKXJ | 105C, low impedance |

### Conductive Polymer Series

| Series | Full Name | Prefix Pattern | Description |
|--------|-----------|----------------|-------------|
| KZE | KZE Conductive Polymer | EKZE, EKZ | Polymer hybrid aluminum |
| PSC | PSC Polymer Solid | PSC | Solid polymer, low ESR |
| PSE | PSE Polymer Solid | PSE | Solid polymer, enhanced |

### High Temperature Series

| Series | Full Name | Prefix Pattern | Description |
|--------|-----------|----------------|-------------|
| KXG | KXG High Temp Long Life | EKXG | 105C, long life (5000-10000hr) |
| GXE | GXE Ultra Low ESR | GXE | Ultra-low ESR, high temp |

### Miniature Series

| Series | Full Name | Prefix Pattern | Description |
|--------|-----------|----------------|-------------|
| MVY | MVY Mini Low Impedance | MVY, EMVY | Miniature low impedance |
| MVZ | MVZ Mini Standard | MVZ, EMVZ | Miniature standard |

### General Purpose Series

| Series | Full Name | Prefix Pattern | Description |
|--------|-----------|----------------|-------------|
| KMG | KMG General Purpose Mini | EKMG, KMG | General purpose miniature |
| KMH | KMH General Purpose | EKMH, KMH | General purpose standard |

### Surface Mount Series

| Series | Full Name | Prefix Pattern | Description |
|--------|-----------|----------------|-------------|
| SMG | SMG Surface Mount | ESMG | SMD general purpose |
| SMH | SMH Surface Mount | ESMH | SMD standard |

---

## Voltage Codes

### 3-Digit Format (Primary)

Nippon Chemi-Con primarily uses 3-digit voltage codes where value = code / 10:

| Code | Voltage | Code | Voltage |
|------|---------|------|---------|
| 040 | 4V | 250 | 25V |
| 063 | 6.3V | 350 | 35V |
| 100 | 10V | 500 | 50V |
| 160 | 16V | 630 | 63V |
| 200 | 20V | | |

### EIA Letter Format (Alternate)

Some series use EIA voltage codes (digit + letter):

| Code | Voltage | Code | Voltage |
|------|---------|------|---------|
| 0G | 4V | 1V | 35V |
| 0J | 6.3V | 1H | 50V |
| 1A | 10V | 1J | 63V |
| 1C | 16V | 2A | 100V |
| 1E | 25V | | |

---

## Capacitance Codes

Uses standard EIA 3-digit code (value in uF):

| Code | Value | Calculation |
|------|-------|-------------|
| 100 | 10uF | 10 x 10^0 |
| 101 | 100uF | 10 x 10^1 |
| 221 | 220uF | 22 x 10^1 |
| 471 | 470uF | 47 x 10^1 |
| 102 | 1000uF | 10 x 10^2 |
| 103 | 10000uF | 10 x 10^3 |

---

## Case Size Codes

### SMD Case Codes

| Code | Dimensions | Notes |
|------|------------|-------|
| JC5 | 5x5.3mm | Small SMD |
| JC6 | 6.3x5.5mm | Standard SMD |
| JC8 | 8x6.2mm | Medium SMD |
| JC10 | 10x10.2mm | Large SMD |
| JH5 | 5x5.8mm | Tall SMD |
| JH6 | 6.3x5.8mm | Tall SMD |
| JH8 | 8x6.5mm | Tall SMD |

### Radial Lead Case Codes

| Code | Dimensions | Notes |
|------|------------|-------|
| MDD | 4x7mm | Mini |
| MHD | 5x11mm | Small |
| MLD | 6.3x11mm | Standard |
| MPD | 8x11.5mm | Medium |
| MNL | 10x12.5mm | Large |
| MQL | 12.5x15mm | Extra large |

---

## Handler Implementation Notes

### Pattern Recognition

```java
// KY series - Low impedance
"^EKY[A-Z][0-9]{3}.*"  // EKYE500...
"^EKYE[0-9]{3}.*"      // Alternate
"^EKY[0-9][A-Z].*"     // EIA voltage format

// KZE series - Polymer hybrid
"^EKZ[A-Z][0-9]{3}.*"  // EKZE250...
"^EKZE[0-9]{3}.*"      // Alternate
"^EKZ[0-9][A-Z].*"     // EIA voltage format

// KXG/KXJ series - High temperature
"^EKXG[0-9]{3}.*"      // EKXG500...
"^EKXJ[0-9]{3}.*"      // EKXJ350...

// MVY/MVZ series - Miniature
"^MVY[0-9]{3}.*"       // MVY160...
"^EMVY[0-9]{3}.*"      // EMVY160...
"^MVZ[0-9]{3}.*"       // MVZ100...

// KMG/KMH series - General purpose
"^EKMG[0-9]{3}.*"      // EKMG160...
"^KMG[0-9]{3}.*"       // KMG160...
"^EKMH[0-9]{3}.*"      // EKMH250...
"^KMH[0-9]{3}.*"       // KMH250...

// PSC/PSE series - Polymer solid
"^PSC[0-9]{3}.*"       // PSC100...
"^PSE[0-9]{3}.*"       // PSE160...
"^PSC[0-9][A-Z].*"     // PSC1V... (EIA voltage)

// GXE series - Ultra low ESR
"^GXE[0-9]{3}.*"       // GXE160...

// Generic patterns
"^EK[A-Z]{2}[0-9]{3}.*"    // Any EK + 2 letters + 3-digit voltage
"^M[A-Z]{2}[0-9]{3}.*"     // Any M + 2 letters + 3-digit voltage
```

### Voltage Extraction

```java
// Determine prefix length first
int startIdx;
if (mpn.startsWith("EKYE") || mpn.startsWith("EKZE") ||
    mpn.startsWith("EKXG") || mpn.startsWith("EKXJ") ||
    mpn.startsWith("EKMG") || mpn.startsWith("EKMH") ||
    mpn.startsWith("ESMG") || mpn.startsWith("ESMH") ||
    mpn.startsWith("EMVY") || mpn.startsWith("EMVZ")) {
    startIdx = 4;  // 4-letter prefix
} else if (mpn.startsWith("EKY") || mpn.startsWith("EKZ") ||
           mpn.startsWith("MVY") || mpn.startsWith("MVZ") ||
           mpn.startsWith("KMG") || mpn.startsWith("KMH") ||
           mpn.startsWith("PSC") || mpn.startsWith("PSE") ||
           mpn.startsWith("GXE")) {
    startIdx = 3;  // 3-letter prefix
}

// Extract 3-digit voltage code
String voltageCode = mpn.substring(startIdx, startIdx + 3);
int voltage = Integer.parseInt(voltageCode) / 10;  // 500 -> 50V
```

### Package Code Extraction

```java
// Look for known case code patterns
if (mpn.contains("JC5")) return "5x5.3mm";
if (mpn.contains("JC6")) return "6.3x5.5mm";
if (mpn.contains("MHD")) return "5x11mm";
// etc.

// Or extract M-suffix from end
int lastDigitIdx = findLastDigit(mpn);
String suffix = mpn.substring(lastDigitIdx + 1);
if (suffix.startsWith("M")) return suffix;  // MJC5S, MHD, etc.
```

---

## Replacement Rules

The handler supports replacement scenarios based on:

1. **Same series required**: Different series are not interchangeable
2. **Package must match**: Case size must be identical
3. **Higher voltage can replace lower**: Within same series, 50V can replace 25V

```java
// Higher voltage rating can replace lower in same series
int v1 = parseVoltageCode(voltage1);
int v2 = parseVoltageCode(voltage2);
if (v1 >= v2) return true;  // 50V can replace 25V
```

---

## Related Files

- Handler: `manufacturers/NipponChemiConHandler.java`
- Component types: `CAPACITOR`
- Supported types: CAPACITOR, IC

---

## Learnings & Edge Cases

- **Prefix variations**: Same series has multiple prefixes (KMG vs EKMG, MVY vs EMVY)
- **Voltage format duality**: 3-digit (500=50V) and EIA (1V=35V) formats both used
- **Case code in suffix**: Package info is embedded at end, not separate field
- **EK prefix meaning**: EK = Nippon Chemi-Con's aluminum electrolytic identifier
- **Series code extraction**: Need to strip EK/E prefix to get actual series (EKYE -> KY)
- **Voltage calculation**: 3-digit code divided by 10 gives actual voltage

<!-- Add new learnings above this line -->
