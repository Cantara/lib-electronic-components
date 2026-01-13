---
name: genesyslogic
description: Genesys Logic MPN encoding patterns, suffix decoding, and handler guidance. Use when working with Genesys Logic USB controller components or GenesysLogicHandler.
---

# Genesys Logic Manufacturer Skill

## MPN Structure

Genesys Logic MPNs follow this general structure:

```
[PREFIX][SERIES][MODEL][PACKAGE][-OPTIONS]
   |       |       |       |        |
   |       |       |       |        +-- Optional: -QFN88, -REEL, -TR
   |       |       |       +-- Package letter: G=LQFP, Q=QFN, S=SSOP
   |       |       +-- Model digits (varies by series)
   |       +-- Series: 85, 35, 32, 33, 36, 98
   +-- GL prefix (all Genesys Logic products)
```

### Example Decoding

```
GL850G
|  | ||
|  | |+-- G = LQFP package
|  | +-- 0 = base model
|  +-- 85 = USB 2.0 hub series
+-- GL = Genesys Logic prefix

GL3523-QFN88
|  |   |
|  |   +-- QFN88 = 88-pin QFN package
|  +-- 3523 = USB 3.1 Gen 1 7-port hub
+-- GL = Genesys Logic prefix
```

---

## Product Families

### GL85x Series - USB 2.0 Hubs

| Part Number | Ports | Features |
|-------------|-------|----------|
| GL850G | 4 | USB 2.0 4-port hub, LQFP |
| GL852G | 4 | USB 2.0 4-port hub (improved) |

### GL35xx Series - USB 3.0/3.1 Hubs

| Part Number | Ports | USB Version | Features |
|-------------|-------|-------------|----------|
| GL3510 | 4 | USB 3.0 | Basic 4-port hub |
| GL3520 | 4 | USB 3.0 | 4-port hub with MTT |
| GL3523 | 7 | USB 3.1 Gen 1 | 7-port hub with MTT |
| GL3590 | 4 | USB 3.0 | 4-port hub |

### GL36xx Series - USB 3.1 Gen 2 Hubs

| Part Number | Ports | USB Version | Features |
|-------------|-------|-------------|----------|
| GL36xx | varies | USB 3.1 Gen 2 | 10 Gbps support |

### GL32xx Series - USB 2.0 Card Readers

| Part Number | Description | Features |
|-------------|-------------|----------|
| GL3220 | Card reader | USB 2.0 |
| GL3224 | Card reader | USB 2.0 |
| GL3227 | Card reader | USB 2.0 |

### GL33xx Series - USB 3.0 Card Readers

| Part Number | Description | Features |
|-------------|-------------|----------|
| GL33xx | Card reader | USB 3.0 |

### GL98xx Series - USB Power Delivery Controllers

| Part Number | Description | Features |
|-------------|-------------|----------|
| GL9801 | USB PD controller | Power delivery |
| GL9802 | USB PD controller | Power delivery |

---

## Package Codes

### Single-Letter Suffixes (3-digit models)

| Code | Package | Notes |
|------|---------|-------|
| G | LQFP | Low-profile QFP (GL850G, GL852G) |
| Q | QFN | Quad flat no-lead |
| S | SSOP | Shrink SOP |
| T | TQFP | Thin QFP |

### Hyphenated Package Codes (4-digit models)

| Code | Package | Notes |
|------|---------|-------|
| -QFN | QFN | Generic QFN |
| -QFN88 | QFN-88 | 88-pin QFN |
| -LQFP | LQFP | Generic LQFP |
| -SSOP | SSOP | Generic SSOP |

### Tape and Reel Suffixes

| Suffix | Meaning |
|--------|---------|
| -REEL | Tape and reel packaging |
| -TR | Tape and reel (alternate) |

---

## USB Version Reference

| Series | USB Version | Speed |
|--------|-------------|-------|
| GL85x | USB 2.0 | 480 Mbps |
| GL3510, GL3520 | USB 3.0 | 5 Gbps |
| GL3523, GL3590 | USB 3.1 Gen 1 | 5 Gbps |
| GL36xx | USB 3.1 Gen 2 | 10 Gbps |
| GL32xx | USB 2.0 | 480 Mbps |
| GL33xx | USB 3.0 | 5 Gbps |

---

## Handler Implementation Notes

### Pattern Matching

```java
// GL850/GL852 series - USB 2.0 hubs (GL850G, GL852G)
"^GL85[0-9][A-Z].*"

// GL35xx series - USB 3.0/3.1 hubs (GL3510, GL3520, GL3523, GL3590)
"^GL35[0-9]{2}.*"

// GL36xx series - USB 3.1 Gen 2 hubs
"^GL36[0-9]{2}.*"

// GL32xx series - Card readers (GL3220, GL3224, GL3227)
"^GL32[0-9]{2}.*"

// GL33xx series - USB 3.0 card readers
"^GL33[0-9]{2}.*"

// GL98xx series - USB PD controllers (GL9801, GL9802)
"^GL98[0-9]{2}.*"
```

### Package Code Extraction

```java
// Step 1: Remove -REEL, -TR suffixes
String cleanMpn = mpn.replaceAll("-?(REEL|TR)$", "");

// Step 2: Check for hyphenated package (GL3523-QFN88)
int hyphen = cleanMpn.indexOf('-');
if (hyphen > 0) {
    String afterHyphen = cleanMpn.substring(hyphen + 1);
    String packageType = afterHyphen.replaceAll("[0-9]+$", ""); // Remove pin count
    return packageType;  // e.g., "QFN"
}

// Step 3: Check for 3-digit + letter format (GL850G)
if (cleanMpn.matches("^GL[0-9]{3}[A-Z]$")) {
    String suffix = cleanMpn.substring(cleanMpn.length() - 1);
    return resolvePackageCode(suffix);  // G -> LQFP
}
```

### Series Extraction

Returns specific series identifiers:
- GL850G -> "GL850"
- GL852G -> "GL852"
- GL3510 -> "GL3510"
- GL3523 -> "GL3523"

---

## Port Count Reference

| Series | Downstream Ports |
|--------|-----------------|
| GL850, GL852 | 4 |
| GL3510, GL3520 | 4 |
| GL3523 | 7 |
| GL3590 | 4 |

---

## MTT Support (Multiple Transaction Translator)

MTT allows better performance when multiple USB 2.0 devices are connected behind a USB 3.0 hub.

| Part | MTT Support |
|------|-------------|
| GL850G, GL852G | No |
| GL3510 | No |
| GL3520 | Yes |
| GL3523 | Yes |
| GL3590 | No |

---

## Related Files

- Handler: `manufacturers/GenesysLogicHandler.java`
- Component types: `IC`

---

## Official Replacements

### Upgrade Paths

| Original | Replacement | Notes |
|----------|-------------|-------|
| GL850G | GL852G | Same port count, improved features |
| GL3510 | GL3520 | Same port count, adds MTT |

### Package Variants

Parts with same series but different packages are interchangeable functionally:
- GL850G (LQFP) can be replaced by GL850Q (QFN) with PCB redesign
- GL3523-QFN88 is the standard package for GL3523

---

## Common Use Cases

### USB Hub Designs

| Application | Recommended Part | Notes |
|-------------|-----------------|-------|
| Basic USB 2.0 hub | GL852G | 4-port, improved GL850 |
| USB 3.0 hub | GL3520 | 4-port with MTT |
| Multi-port USB 3.0 | GL3523 | 7-port with MTT |
| High-speed USB 3.1 | GL36xx | USB 3.1 Gen 2 |

### Card Reader Designs

| Application | Recommended Part | Notes |
|-------------|-----------------|-------|
| USB 2.0 card reader | GL3220 | Basic USB 2.0 |
| USB 3.0 card reader | GL33xx | Higher speed |

---

## Learnings & Edge Cases

- **3-digit vs 4-digit**: GL85x uses 3-digit model + package letter (GL850G), while GL35xx uses 4-digit model + hyphenated package (GL3523-QFN88).
- **Package suffix position**: For 3-digit models, package letter is concatenated; for 4-digit models, package comes after hyphen.
- **Pin count in package**: QFN88 means 88-pin QFN - the number is pin count, not part of package type.
- **Tape and reel**: -REEL and -TR suffixes should be stripped before extracting package code.
- **MTT importance**: For USB 3.0 hubs with many USB 2.0 devices, MTT support (GL3520, GL3523) provides better performance.
- **GL852 vs GL850**: GL852G is an improved version of GL850G with better compatibility - use GL852G for new designs.

<!-- Add new learnings above this line -->
