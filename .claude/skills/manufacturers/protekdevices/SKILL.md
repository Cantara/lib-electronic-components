---
name: protekdevices
description: ProTek Devices MPN encoding patterns, TVS diode and ESD protection decoding, and handler guidance. Use when working with ProTek circuit protection components (TVS, GBLC, PSM, ULC, SMD series).
---

# ProTek Devices Manufacturer Skill

## Overview

ProTek Devices specializes in circuit protection components:
- **TVS series**: Standard TVS diodes
- **GBLC series**: ESD protection devices
- **PSM series**: Automotive/RS-232 protection
- **ULC series**: Ultra-low capacitance ESD arrays
- **SMD series**: Surface mount TVS diodes
- **SP series**: Surge protection
- **LC series**: Low capacitance TVS

---

## MPN Structure

### TVS Series (Standard TVS Diodes)

```
TVS[VOLTAGE][POWER][SUFFIX]
|      |       |       |
|      |       |       +-- -LF = lead-free, -TR = tape/reel
|      |       +-- 2-digit power code (00, 50, etc.)
|      +-- 3-digit voltage code (035 = 3.5V, 055 = 5.5V)
+-- TVS = Standard TVS diode series

Example: TVS03500
         |  |  ||
         |  |  |+-- 00 = Power level
         |  |  +-- 035 = 3.5V standoff voltage
         +-- TVS series

Example: TVS15000-LF
         |  |  || |
         |  |  || +-- LF = Lead-free
         |  |  |+-- 00 = Power level
         |  |  +-- 150 = 15V standoff voltage
         +-- TVS series
```

### GBLC Series (ESD Protection)

```
GBLC[VOLTAGE]C[SUFFIX]
|       |    |    |
|       |    |    +-- -LF = lead-free, -TR = tape/reel
|       |    +-- C = Bidirectional
|       +-- 2-digit voltage code (03 = 3.3V, 05 = 5V)
+-- GBLC = ESD protection series

Example: GBLC03C
         |   | |
         |   | +-- C = Bidirectional
         |   +-- 03 = 3.3V
         +-- GBLC series

Example: GBLC15C-LF
         |   | | |
         |   | | +-- LF = Lead-free
         |   | +-- C = Bidirectional
         |   +-- 15 = 15V
         +-- GBLC series
```

### PSM Series (Automotive/RS-232 Protection)

```
PSM[MODEL][-LF]
|     |      |
|     |      +-- -LF = Lead-free
|     +-- Model number (712 = RS-232, 05/12/24 = voltage)
+-- PSM = Protection for signal/data lines

Example: PSM712
         |  |
         |  +-- 712 = RS-232 protection (7V standoff)
         +-- PSM series

Example: PSM712-LF
         |  |  |
         |  |  +-- LF = Lead-free
         |  +-- 712 = RS-232 protection
         +-- PSM series
```

### ULC Series (Ultra-Low Capacitance)

```
ULC[VOLTAGE][LINES][SUFFIX]
|      |       |       |
|      |       |       +-- -LF = lead-free, -TR = tape/reel
|      |       +-- 2-digit line count (12 = 12 lines, 24 = 24 lines)
|      +-- 2-digit voltage (05 = 5V)
+-- ULC = Ultra-low capacitance ESD array

Example: ULC0512
         |  | ||
         |  | |+-- 12 = 12 protection lines
         |  | +-- 05 = 5V
         +-- ULC series

Example: ULC0524-LF
         |  | || |
         |  | || +-- LF = Lead-free
         |  | |+-- 24 = 24 protection lines
         |  | +-- 05 = 5V
         +-- ULC series
```

### SMD Series (Surface Mount TVS)

```
SMD[POWER][VOLTAGE][SUFFIX]
|     |       |        |
|     |       |        +-- -LF = lead-free, -TR = tape/reel
|     |       +-- 2-digit voltage (12 = 12V, 24 = 24V)
|     +-- 2-digit power code (05 = 500W, 15 = 1500W)
+-- SMD = Surface mount TVS

Example: SMD0512
         |  | ||
         |  | |+-- 12 = 12V standoff
         |  | +-- 05 = 500W power
         +-- SMD series

Example: SMD1524-LF
         |  | || |
         |  | || +-- LF = Lead-free
         |  | |+-- 24 = 24V standoff
         |  | +-- 15 = 1500W power
         +-- SMD series
```

---

## Voltage Decoding

### TVS Series

| Code | Standoff Voltage | Clamping Voltage (typ) |
|------|------------------|------------------------|
| 035 | 3.5V | 8.5V |
| 055 | 5.5V | 12V |
| 100 | 10V | 19V |
| 150 | 15V | 27V |
| 200 | 20V | 36V |

### GBLC Series

| Code | Standoff Voltage | Application |
|------|------------------|-------------|
| 03 | 3.3V | 3.3V logic |
| 05 | 5V | 5V logic |
| 08 | 8V | Data lines |
| 15 | 15V | CAN/automotive |

### PSM Series

| Model | Standoff Voltage | Application |
|-------|------------------|-------------|
| 712 | 7V | RS-232 |
| 05 | 5V | General |
| 12 | 12V | Automotive |
| 24 | 24V | Industrial |

---

## Power Ratings

### TVS Series Power Codes

| Code | Power Rating |
|------|--------------|
| 00 | Standard |
| 50 | 500W |

### SMD Series Power Codes

| Code | Power Rating | Package |
|------|--------------|---------|
| 05 | 500W | SMA (DO-214AC) |
| 10 | 1000W | SMB (DO-214AA) |
| 15 | 1500W | SMC (DO-214AB) |
| 30 | 3000W | SMC |

---

## Package Types

| Series | Default Package | Notes |
|--------|-----------------|-------|
| TVS | SMB (DO-214AA) | Standard TVS |
| GBLC | SOT-23 | Small ESD protection |
| PSM | SOIC-8 | Multi-line protection |
| ULC (12-line) | SSOP-16 | ESD array |
| ULC (24-line) | SSOP-28 | ESD array |
| SMD05xx | SMA (DO-214AC) | 500W |
| SMD10xx | SMB (DO-214AA) | 1000W |
| SMD15xx | SMC (DO-214AB) | 1500W |
| SP | SOT-23 | Surge protection |
| LC | SOT-23 | Low capacitance |

---

## Directionality

### Bidirectional Devices

| Series | Indicator | Notes |
|--------|-----------|-------|
| GBLC | C suffix | Always bidirectional (GBLCxxC) |
| PSM | (inherent) | Data line protection is bidirectional |
| ULC | (inherent) | ESD arrays are bidirectional |

### Unidirectional Devices

| Series | Notes |
|--------|-------|
| TVS | Standard unidirectional unless specified |
| SMD | Standard unidirectional |
| SP | Typically unidirectional |

---

## Suffix Codes

| Suffix | Meaning |
|--------|---------|
| -LF | Lead-free (RoHS compliant) |
| -TR | Tape and reel packaging |
| C | Bidirectional (GBLC series) |
| (none) | Standard packaging |

---

## Replacement Compatibility

ProTek parts are compatible when:
1. **Same series** (TVS vs TVS, GBLC vs GBLC)
2. **Same voltage rating** (03 = 03, 15 = 15)
3. **Same directionality** (bidirectional cannot replace unidirectional)
4. **Same or higher power** (for TVS/SMD series)

### Lead-Free Compatibility

Lead-free (-LF) variants are interchangeable with standard variants:
- PSM712 and PSM712-LF are functionally identical
- -LF suffix only indicates RoHS compliance

---

## Common Part Numbers

### TVS Series

| Part Number | Voltage | Package | Notes |
|-------------|---------|---------|-------|
| TVS03500 | 3.5V | SMB | Standard |
| TVS05500 | 5.5V | SMB | Standard |
| TVS15000 | 15V | SMB | Standard |

### GBLC Series

| Part Number | Voltage | Package | Notes |
|-------------|---------|---------|-------|
| GBLC03C | 3.3V | SOT-23 | Bidirectional |
| GBLC05C | 5V | SOT-23 | Bidirectional |
| GBLC15C | 15V | SOT-23 | Bidirectional |

### PSM Series

| Part Number | Voltage | Package | Application |
|-------------|---------|---------|-------------|
| PSM712 | 7V | SOIC-8 | RS-232 |
| PSM712-LF | 7V | SOIC-8 | RS-232, RoHS |

### ULC Series

| Part Number | Voltage | Lines | Package |
|-------------|---------|-------|---------|
| ULC0512 | 5V | 12 | SSOP-16 |
| ULC0524 | 5V | 24 | SSOP-28 |

### SMD Series

| Part Number | Power | Voltage | Package |
|-------------|-------|---------|---------|
| SMD0512 | 500W | 12V | SMA |
| SMD1012 | 1000W | 12V | SMB |
| SMD1524 | 1500W | 24V | SMC |

---

## Handler Implementation Notes

### Pattern Matching

```java
// TVS series - Standard TVS Diodes
"^TVS[0-9]{5}.*"

// GBLC series - ESD Protection (with C suffix)
"^GBLC[0-9]{2}C.*"

// PSM series - Automotive/RS-232 Protection
"^PSM[0-9]+.*"

// ULC series - Ultra-Low Capacitance
"^ULC[0-9]{4}.*"

// SMD series - Surface Mount TVS
"^SMD[0-9]{4}.*"

// SP series - Surge Protection
"^SP[0-9]{2,3}.*"

// LC series - Low Capacitance
"^LC[0-9]{2}.*"
```

### Voltage Extraction

```java
String extractVoltage(String mpn) {
    String upperMpn = mpn.toUpperCase();

    // TVS series: TVS03500 -> 035 -> 3.5V
    if (upperMpn.startsWith("TVS")) {
        String voltageCode = upperMpn.substring(3, 6);  // e.g., "035"
        int voltageInt = Integer.parseInt(voltageCode);
        if (voltageInt < 100) {
            return String.format("%.1f", voltageInt / 10.0);  // 035 -> "3.5"
        }
        return String.valueOf(voltageInt / 10);  // 150 -> "15"
    }

    // GBLC series: GBLC03C -> 03 -> 3.3V
    if (upperMpn.startsWith("GBLC")) {
        String voltageCode = upperMpn.substring(4, 6);  // e.g., "03"
        int voltage = Integer.parseInt(voltageCode);
        if (voltage == 3) return "3.3";
        return String.valueOf(voltage);
    }

    // PSM series: PSM712 -> 7V (specific model)
    if (upperMpn.startsWith("PSM")) {
        if (upperMpn.contains("712")) return "7";
        // Extract numeric portion
    }

    // SMD series: SMD0512 -> 12V (last 2 digits)
    if (upperMpn.matches("^SMD[0-9]{4}.*")) {
        String voltageCode = upperMpn.substring(5, 7);  // e.g., "12"
        return String.valueOf(Integer.parseInt(voltageCode));
    }

    return "";
}
```

### Power Rating Extraction

```java
int getPowerRating(String mpn) {
    String upperMpn = mpn.toUpperCase();

    // SMD series: SMD0512 -> 05 -> 500W
    if (upperMpn.matches("^SMD[0-9]{4}.*")) {
        String powerCode = upperMpn.substring(3, 5);  // e.g., "05"
        return Integer.parseInt(powerCode) * 100;  // 05 -> 500W
    }

    // TVS series: last 2 digits encode power
    if (upperMpn.startsWith("TVS")) {
        String powerCode = upperMpn.substring(6, 8);  // e.g., "00"
        return Integer.parseInt(powerCode) * 10;  // 50 -> 500W
    }

    return 0;
}
```

### Line Count Extraction (ULC series)

```java
int getLineCount(String mpn) {
    String upperMpn = mpn.toUpperCase();

    // ULC series: ULC0512 -> 12 lines, ULC0524 -> 24 lines
    if (upperMpn.startsWith("ULC")) {
        String lineCode = upperMpn.substring(5, 7);  // e.g., "12"
        return Integer.parseInt(lineCode);
    }

    return 1;  // Default to single-channel
}
```

---

## Related Files

- Handler: `manufacturers/ProTekDevicesHandler.java`
- Component types: `DIODE` (all series registered as diodes)
- No manufacturer-specific types defined

---

## Application Selection Guide

| Application | Recommended Series | Notes |
|-------------|-------------------|-------|
| Power supply protection | TVS, SMD | Higher power handling |
| USB/HDMI ESD | GBLC | Low capacitance |
| RS-232/RS-485 | PSM712 | Multi-line protection |
| High-speed data | ULC | Ultra-low capacitance |
| Automotive | PSM, GBLC15C | Voltage tolerance |

---

## Learnings & Edge Cases

- **GBLC always bidirectional**: The "C" suffix indicates bidirectional, all GBLC parts have this
- **PSM712 specific**: PSM712 is specifically for RS-232, uses 7V standoff for +/-12V signals
- **SMD naming**: SMD series is named for package type, not to be confused with generic "SMD" term
- **ULC line count**: Second 2 digits indicate number of protection lines (12, 24)
- **Voltage encoding varies**: TVS uses 3 digits (divide by 10), GBLC uses 2 digits, SMD uses last 2 digits
- **Lead-free interchangeable**: -LF suffix only affects manufacturing process, electrically identical

<!-- Add new learnings above this line -->
