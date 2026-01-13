---
name: pulseelectronics
description: Pulse Electronics MPN encoding patterns, series identification, and handler guidance. Use when working with Pulse Electronics transformers, inductors, or LAN magnetics.
---

# Pulse Electronics Manufacturer Skill

## Manufacturer Overview

Pulse Electronics is a leading manufacturer of electronic components specializing in:

- **LAN Transformers** - Ethernet isolation transformers (H series, T series)
- **Telecom Transformers** - Line interface transformers (T series)
- **Power Inductors** - SMD and through-hole inductors (P series)
- **Power Electronics** - Power transformers and inductors (PA, PE series)
- **LAN Magnetics** - RJ45 connectors with integrated magnetics (J, JK, JXD series)

Pulse Electronics is known for high-quality magnetic components used in networking, telecommunications, and power applications.

---

## MPN Structure

Pulse Electronics MPNs follow this general structure:

```
[SERIES][PART_NUMBER][SUFFIX]
   |         |          |
   |         |          +-- NL=Lead-free, NLT=Tape&Reel, S=Standard, G=Green
   |         +-- 4-digit part number (or dash-separated for J series)
   +-- H, T, P, PA, PE, JD0, JK, JXD, etc.
```

### Example Decodings

```
H1012NL
|  |  ||
|  |  |+-- NL = Lead-free / RoHS compliant
|  |  +--- (no additional variant)
|  +------ 1012 = Part number
+--------- H = LAN Transformer series

PE-53680NL
|   |    ||
|   |    |+-- NL = Lead-free
|   +-------- 53680 = Part number
+------------ PE = Power Electronics series

JD0-0001NL
|   |   ||
|   |   |+-- NL = Lead-free
|   +------- 0001 = Variant number
+----------- JD0 = J series LAN Magnetics (RJ45 with magnetics)

P0751SNL
| |  |||
| |  ||+-- NL = Lead-free
| |  |+--- S = Standard variant
| +------- 0751 = Part number
+--------- P = Power Inductor series
```

---

## Product Series

### Transformer Series

| Series | Type | Description | Pattern |
|--------|------|-------------|---------|
| H | LAN Transformer | Ethernet isolation transformers | `^H\d{4}[A-Z]*$` |
| T | Telecom Transformer | Line interface transformers | `^T\d{4}[A-Z]*$` |
| PA | Power Adapter | Power transformers, automotive | `^PA[-]?\d+[A-Z]*$` |
| PE | Power Electronics | High-power transformers | `^PE[-]\d+[A-Z]*$` |

### Inductor Series

| Series | Type | Description | Pattern |
|--------|------|-------------|---------|
| P | Power Inductor | General purpose power inductors | `^P\d{4}[A-Z]*$` |
| PA | Automotive Inductor | Automotive-grade inductors | `^PA[-]?\d+[A-Z]*$` |
| PE | SMD Inductor | Surface mount inductors | `^PE[-]\d+[A-Z]*$` |

### LAN Magnetics (Connectors) Series

| Series | Type | Description | Pattern |
|--------|------|-------------|---------|
| J (JD0, JD1) | LAN Magnetics | RJ45 with integrated magnetics | `^JD\d[-]\d{4}[A-Z]*$` |
| JK | RJ45 Jack | Standard RJ45 connector jacks | `^JK\d?[-]?\d+[A-Z]*$` |
| JXD | 10G LAN | High-speed 10 Gigabit connectors | `^JXD\d?[-]?\d+[A-Z]*$` |

---

## Suffix Codes

### Common Suffixes

| Suffix | Description |
|--------|-------------|
| NL | Lead-free / RoHS compliant |
| NLT | Lead-free, Tape and Reel packaging |
| S | Standard variant |
| SNL | Standard variant, Lead-free |
| G | Green / RoHS |
| GNL | Green, Lead-free |
| T | Tape and Reel |
| TL | Tape and Reel, Lead-free |

### Suffix Position

The suffix always appears at the end of the MPN, after the part number:

```
H1012NL     -> suffix = "NL"
PE-53680NLT -> suffix = "NLT"
P0751SNL    -> suffix = "SNL" (S + NL combined)
JD0-0001NL  -> suffix = "NL"
```

---

## Package Code Extraction

Package codes are derived from the suffix. The handler maps suffixes to package descriptions:

| Suffix | Extracted Package |
|--------|-------------------|
| NL | SMD Lead-Free |
| NLT | SMD Lead-Free T&R |
| S | Standard |
| SNL | Standard Lead-Free |
| G | Green/RoHS |
| GNL | Green Lead-Free |
| T | Tape & Reel |
| TL | Tape & Reel Lead-Free |

**Implementation Note**: If the suffix is not in the map, the raw suffix is returned.

```java
// From PulseElectronicsHandler.extractPackageCode()
String suffix = extractSuffix(mpn);
if (PACKAGE_CODES.containsKey(suffix)) {
    return PACKAGE_CODES.get(suffix);
}
return suffix;  // Return raw suffix if not mapped
```

---

## Series Extraction

Series extraction uses pattern matching with specific ordering to handle overlapping prefixes:

**Extraction Order (Important)**:
1. JXD series (most specific J variant)
2. JK series
3. J series (JD0, JD1, etc.)
4. PE series (before P - more specific)
5. PA series (before P - more specific)
6. P series
7. H series
8. T series

```java
// Example extraction results:
"H1012NL"     -> series = "H"
"T1029NL"     -> series = "T"
"P0751SNL"    -> series = "P"
"PA-1234NL"   -> series = "PA"
"PE-53680NL"  -> series = "PE"
"JD0-0001NL"  -> series = "J"   // Note: returns "J", not "JD0"
"JK0-1234NL"  -> series = "JK"
"JXD0-5678NL" -> series = "JXD"
```

---

## Supported ComponentTypes

The handler supports these component types:

| ComponentType | Description |
|---------------|-------------|
| `INDUCTOR` | Power inductors (P, PA, PE series) |
| `TRANSFORMER_BOURNS` | LAN/telecom/power transformers (H, T, PA, PE series) |
| `CONNECTOR` | LAN magnetics connectors (J, JK, JXD series) |
| `IC` | Generic fallback type (registered for all patterns) |

**Note**: Uses `TRANSFORMER_BOURNS` as there is no Pulse-specific transformer type.

---

## Pattern Matching Rules

### Series Pattern Details

```java
// H series - LAN transformers: H followed by 4 digits
"^(H)(\\d{4})([A-Z]*)$"
// Matches: H1012, H1102, H1188, H1012NL, H1188NLT

// T series - Telecom transformers: T followed by 4 digits
"^(T)(\\d{4})([A-Z]*)$"
// Matches: T1029, T3012, T1029NL

// P series - Power inductors: P followed by 4 digits
"^(P)(\\d{4})([A-Z]*)$"
// Matches: P0751, P1166, P0751SNL

// PA series - Power adapters: PA optionally followed by dash, then digits
"^(PA)[-]?(\\d+)([A-Z]*)$"
// Matches: PA1234, PA-1234, PA1234NL

// PE series - Power electronics: PE followed by dash, then digits
"^(PE)[-](\\d+)([A-Z]*)$"
// Matches: PE-53680, PE-53680NL (dash required)

// J series - RJ45 with magnetics: JDn followed by dash and 4 digits
"^(JD\\d)[-](\\d{4})([A-Z]*)$"
// Matches: JD0-0001, JD1-0005, JD0-0001NL

// JK series - RJ45 jacks: JK optionally followed by digit, dash, and digits
"^(JK\\d?)[-]?(\\d+)([A-Z]*)$"
// Matches: JK0-1234, JK-1234, JK1234

// JXD series - 10G LAN: JXD optionally followed by digit, dash, and digits
"^(JXD\\d?)[-]?(\\d+)([A-Z]*)$"
// Matches: JXD0-5678, JXD-1234, JXD1234NL
```

---

## Helper Methods

The handler provides several utility methods:

### Product Type Detection

```java
handler.getProductType("H1012NL");    // "LAN Transformer"
handler.getProductType("P0751SNL");   // "Power Inductor"
handler.getProductType("JD0-0001NL"); // "LAN Magnetics RJ45"
handler.getProductType("PE-53680NL"); // "Power Electronics"
```

### Lead-Free Detection

```java
handler.isLeadFree("H1012NL");   // true (contains NL)
handler.isLeadFree("P0751G");    // true (contains G)
handler.isLeadFree("T1029");     // false
```

### Tape and Reel Detection

```java
handler.isTapeAndReel("H1012NLT"); // true (ends with NLT)
handler.isTapeAndReel("P0751T");   // true (ends with T)
handler.isTapeAndReel("H1012NL");  // false
```

### Component Category Detection

```java
handler.isTransformer("H1012NL");   // true (H series)
handler.isTransformer("T1029NL");   // true (T series)
handler.isTransformer("PE-53680");  // true (PE series)
handler.isInductor("P0751SNL");     // true (P series)
handler.isInductor("PA-1234");      // true (PA series)
handler.isLanMagnetics("JD0-0001"); // true (J series)
handler.isLanMagnetics("JXD0-1234"); // true (JXD series)
```

---

## Official Replacement Logic

The `isOfficialReplacement()` method considers two parts as official replacements if:

1. Both are from the same series (e.g., both H series)
2. Both have the same base part number (without suffix)
3. Only the suffix differs (e.g., NL vs NLT)

```java
// These are considered official replacements:
handler.isOfficialReplacement("H1012NL", "H1012NLT");  // true (same base, different suffix)
handler.isOfficialReplacement("P0751S", "P0751SNL");   // true (same base, different suffix)

// These are NOT official replacements:
handler.isOfficialReplacement("H1012NL", "H1013NL");   // false (different part numbers)
handler.isOfficialReplacement("H1012NL", "T1012NL");   // false (different series)
```

---

## Example MPNs

### LAN Transformers (H Series)

| MPN | Series | Part# | Suffix | Description |
|-----|--------|-------|--------|-------------|
| H1012NL | H | 1012 | NL | 10/100Base-T LAN transformer, lead-free |
| H1102NL | H | 1102 | NL | Gigabit LAN transformer, lead-free |
| H1188NLT | H | 1188 | NLT | LAN transformer, lead-free tape & reel |

### Telecom Transformers (T Series)

| MPN | Series | Part# | Suffix | Description |
|-----|--------|-------|--------|-------------|
| T1029NL | T | 1029 | NL | Telecom line transformer, lead-free |
| T3012NL | T | 3012 | NL | T1/E1 transformer, lead-free |

### Power Inductors (P Series)

| MPN | Series | Part# | Suffix | Description |
|-----|--------|-------|--------|-------------|
| P0751SNL | P | 0751 | SNL | Power inductor, standard, lead-free |
| P1166NL | P | 1166 | NL | Power inductor, lead-free |
| P0751NLT | P | 0751 | NLT | Power inductor, lead-free T&R |

### Power Electronics (PE Series)

| MPN | Series | Part# | Suffix | Description |
|-----|--------|-------|--------|-------------|
| PE-53680NL | PE | 53680 | NL | Power transformer, lead-free |
| PE-53913NLT | PE | 53913 | NLT | Power transformer, lead-free T&R |

### LAN Magnetics (J Series)

| MPN | Series | Part# | Suffix | Description |
|-----|--------|-------|--------|-------------|
| JD0-0001NL | J | 0001 | NL | RJ45 with magnetics, single port, lead-free |
| JD0-0005NL | J | 0005 | NL | RJ45 with magnetics, quad port, lead-free |
| JXD0-1234NL | JXD | 1234 | NL | 10G RJ45 connector, lead-free |

---

## Related Files

- **Handler**: `manufacturers/PulseElectronicsHandler.java`
- **Component types**: `INDUCTOR`, `TRANSFORMER_BOURNS`, `CONNECTOR`, `IC`
- **Tests**: Should be in `handlers/PulseElectronicsHandlerTest.java` (not in manufacturers package)

---

## Learnings & Quirks

### Pattern Ordering

- **PE and PA before P**: Must check PE/PA patterns before P pattern since "PE-12345" starts with "P"
- **JXD and JK before J**: Must check more specific J-series variants first

### Series vs Base Part Number

- `extractSeries()` returns the series prefix (H, T, P, PA, PE, J, JK, JXD)
- `extractBasePartNumber()` returns series + part number without suffix (e.g., "H1012", "JD0-0001")

### J Series Naming

- J series uses "JD0", "JD1", etc. as the actual prefix in MPNs
- But `extractSeries()` returns just "J" (not "JD0")
- The `getProductType()` method handles this by mapping "J" to "JD" for description lookup

### Dash Handling

- **PE series**: Dash is required (PE-53680, not PE53680)
- **PA series**: Dash is optional (PA-1234 or PA1234)
- **J series**: Dash is required (JD0-0001, not JD00001)
- **JK/JXD series**: Dash is optional

### RoHS/Lead-Free Detection

- Both "NL" suffix and "G" suffix indicate RoHS compliance
- Most modern parts use "NL" (No Lead) suffix
- "G" (Green) is an older designation for RoHS compliance

### Component Type Overlap

- **PA and PE series** can be either transformers OR inductors
- The handler registers patterns for both `INDUCTOR` and `TRANSFORMER_BOURNS` for these series
- Use `isTransformer()` or `isInductor()` helper methods to determine specific type

### Missing Manufacturer-Specific Types

- Handler uses `TRANSFORMER_BOURNS` instead of a Pulse-specific transformer type
- Consider adding `TRANSFORMER_PULSE`, `INDUCTOR_PULSE`, `CONNECTOR_PULSE` if needed

<!-- Add new learnings above this line -->
