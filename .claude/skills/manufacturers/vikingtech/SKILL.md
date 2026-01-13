---
name: vikingtech
description: Viking Tech MPN encoding patterns, suffix decoding, and handler guidance. Use when working with Viking Tech precision resistors including chip, anti-sulfur, and current sense resistors.
---

# Viking Tech Manufacturer Skill

## MPN Structure

Viking Tech MPNs follow this general structure:

```
[SERIES][SIZE]-[TOLERANCE][TYPE]-[VALUE][SUFFIX]
   |      |        |        |       |       |
   |      |        |        |       |       +-- Lead-free suffix (ELF, GLF)
   |      |        |        |       +-- 4-digit value code (1001=1k)
   |      |        |        +-- Type code (X, etc.)
   |      |        +-- Tolerance (F=1%, G=2%, J=5%)
   |      +-- Package size (0402, 0603, 0805, 1206, etc.)
   +-- Series prefix (CR, AR, CSR, PA)
```

### Example Decoding

```
CR0603-FX-1001ELF
|  |   | |  |   |
|  |   | |  |   +-- ELF = Lead-free suffix
|  |   | |  +-- 1001 = 1k (100 x 10^1)
|  |   | +-- X = Type code
|  |   +-- F = 1% tolerance
|  +-- 0603 = 0603 package (1.6x0.8mm)
+-- CR = Chip Resistor series

AR0603-FX-1002GLF
|  |   | |  |   |
|  |   | |  |   +-- GLF = Lead-free suffix (alternate)
|  |   | |  +-- 1002 = 10k (100 x 10^2)
|  |   | +-- X = Type code
|  |   +-- F = 1% tolerance
|  +-- 0603 = 0603 package
+-- AR = Anti-sulfur Resistor series

CSR0805-0R010F
|   |   |    |
|   |   |    +-- F = 1% tolerance
|   |   +-- 0R010 = 0.010 ohm (R notation)
|   +-- 0805 = 0805 package
+-- CSR = Current Sense Resistor series
```

---

## Series Reference

| Series | Name | Description |
|--------|------|-------------|
| CR | Chip Resistor | Standard thick film chip resistors |
| AR | Anti-Sulfur Resistor | Sulfur-resistant for harsh environments |
| PA | Power Resistor | High power chip resistors |
| CSR | Current Sense Resistor | Low ohm precision current sensing |

---

## Package Sizes

Viking Tech uses standard EIA package codes:

| Code | Dimensions (mm) | Dimensions (inch) | Power Rating |
|------|-----------------|-------------------|--------------|
| 0201 | 0.6 x 0.3 | 0.02 x 0.01 | 1/20W |
| 0402 | 1.0 x 0.5 | 0.04 x 0.02 | 1/16W |
| 0603 | 1.6 x 0.8 | 0.06 x 0.03 | 1/10W |
| 0805 | 2.0 x 1.25 | 0.08 x 0.05 | 1/8W |
| 1206 | 3.2 x 1.6 | 0.12 x 0.06 | 1/4W |
| 1210 | 3.2 x 2.5 | 0.12 x 0.10 | 1/2W |
| 2010 | 5.0 x 2.5 | 0.20 x 0.10 | 3/4W |
| 2512 | 6.4 x 3.2 | 0.25 x 0.12 | 1W |

---

## Tolerance Codes

| Code | Tolerance | Description |
|------|-----------|-------------|
| F | +/-1% | Precision |
| G | +/-2% | Standard precision |
| J | +/-5% | General purpose |

---

## Value Codes

### Standard 4-Digit Code (CR, AR series)

Format: `ABCD` where ABC = significant digits, D = multiplier (10^D)

| Code | Value | Calculation |
|------|-------|-------------|
| 1000 | 100 ohm | 100 x 10^0 |
| 1001 | 1k | 100 x 10^1 |
| 1002 | 10k | 100 x 10^2 |
| 1003 | 100k | 100 x 10^3 |
| 4700 | 470 ohm | 470 x 10^0 |
| 4701 | 4.7k | 470 x 10^1 |
| 4702 | 47k | 470 x 10^2 |

### R Notation (CSR series)

For low-ohm current sense resistors, R indicates decimal point:

| Code | Value |
|------|-------|
| 0R010 | 0.010 ohm |
| 0R020 | 0.020 ohm |
| 0R100 | 0.100 ohm |
| 1R00 | 1.00 ohm |
| R100 | 0.100 ohm |

---

## Handler Implementation Notes

### Pattern Recognition

```java
// CR series - Standard chip resistors
"^CR[0-9]{4}.*"  // CR0603...

// AR series - Anti-sulfur resistors
"^AR[0-9]{4}.*"  // AR0603...

// PA series - Power resistors
"^PA[0-9]+.*"    // PA0805..., PA1206...

// CSR series - Current sense resistors
"^CSR[0-9]{4}.*" // CSR0805...
```

### Package Code Extraction

```java
// CR, AR series: size code starts at position 2
if (mpn.matches("^(CR|AR)[0-9]{4}.*")) {
    String sizeCode = mpn.substring(2, 6);  // "0603"
    return sizeCode;
}

// CSR series: size code starts at position 3
if (mpn.matches("^CSR[0-9]{4}.*")) {
    String sizeCode = mpn.substring(3, 7);  // "0805"
    return sizeCode;
}

// PA series: size code starts at position 2
if (mpn.matches("^PA[0-9]{4}.*")) {
    String sizeCode = mpn.substring(2, 6);  // "0805"
    return sizeCode;
}
```

### Tolerance Extraction

```java
// CR/AR format: CR0603-FX-1001ELF - tolerance after first hyphen
if (mpn.matches("^(CR|AR)[0-9]{4}-[FGJ].*")) {
    int dashIndex = mpn.indexOf('-');
    char toleranceChar = mpn.charAt(dashIndex + 1);
    // F=1%, G=2%, J=5%
}

// CSR format: CSR0805-0R010F - tolerance at end before suffix
// Find F, G, or J after the value
```

### Value Extraction

```java
// CR/AR format: value is in third segment
// CR0603-FX-1001ELF -> 1001
String[] parts = mpn.split("-");
if (parts.length >= 3) {
    String valuePart = parts[2].replaceAll("[A-Z]+$", "");  // Remove ELF
    return decodeResistorValue(valuePart);  // 1001 -> 1k
}

// CSR format: value after hyphen with R notation
// CSR0805-0R010F -> 0R010 -> 0.010 ohm
int dashIndex = mpn.indexOf('-');
String valuePart = mpn.substring(dashIndex + 1).replaceAll("[FGJ].*$", "");
return valuePart.replace("R", ".");  // 0R010 -> 0.010
```

### Value Decoding (4-digit code)

```java
private String decodeResistorValue(String code) {
    if (code.matches("[0-9]{4}")) {
        int significantDigits = Integer.parseInt(code.substring(0, 3));
        int multiplier = Integer.parseInt(code.substring(3));
        double value = significantDigits * Math.pow(10, multiplier);

        if (value >= 1000000) {
            return String.format("%.0fM", value / 1000000);
        } else if (value >= 1000) {
            return String.format("%.0fk", value / 1000);
        } else {
            return String.format("%.0f", value);
        }
    }
    return code;
}
```

---

## Replacement Rules

The handler supports replacement based on:

1. **Same series and size required**: CR0603 can only replace CR0603
2. **Tolerance must match**: F (1%) cannot be replaced by J (5%)
3. **Same value required**: Resistance value must be identical

```java
// Same series and size
String series1 = extractSeries(mpn1);  // "CR0603"
String series2 = extractSeries(mpn2);  // "CR0603"
if (!series1.equals(series2)) return false;

// Same tolerance
String tolerance1 = extractTolerance(mpn1);  // "F"
String tolerance2 = extractTolerance(mpn2);  // "F"
if (!tolerance1.equals(tolerance2)) return false;
```

---

## Related Files

- Handler: `manufacturers/VikingTechHandler.java`
- Component types: `RESISTOR`
- Supported types: RESISTOR, IC

---

## Application Notes

### Anti-Sulfur Resistors (AR series)

AR series resistors use special electrode materials to resist sulfur corrosion. Use in:
- Industrial environments with sulfur gases
- Automotive applications
- LED lighting (near rubber components)
- Any application with potential sulfur exposure

### Current Sense Resistors (CSR series)

CSR series are optimized for current measurement:
- Low inductance design
- 4-terminal (Kelvin) sensing available on larger sizes
- Low TCR (Temperature Coefficient of Resistance)
- Typical values: 0.001 ohm to 1 ohm

---

## Learnings & Edge Cases

- **Value code position varies**: CR/AR use segment-based (third hyphen segment), CSR uses position-based
- **R notation for CSR**: 0R010 means 0.010 ohm (R is decimal point)
- **Suffix meanings**: ELF and GLF both indicate lead-free, different internal codes
- **Series in extraction**: extractSeries() returns series+size (CR0603) not just series (CR)
- **4-digit value code**: First 3 digits are significant, 4th is multiplier (1001 = 100 x 10^1 = 1k)
- **CSR tolerance position**: Tolerance letter comes after value, not in separate segment

<!-- Add new learnings above this line -->
