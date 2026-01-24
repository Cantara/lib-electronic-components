# MPN Normalization

Use this skill when working with MPN (Manufacturer Part Number) normalization, package suffix handling, and component equivalence checking.

## Core Methods

### 1. stripPackageSuffix() - Remove Packaging Codes

**Purpose:** Strip manufacturer-specific package suffixes to get the base component part number.

**Supported patterns:**

| Pattern | Delimiter | Example | Manufacturers | Use Case |
|---------|-----------|---------|---------------|----------|
| Plus suffix | `+` | MAX3483EESA**+** → MAX3483EESA | Maxim, Analog Devices | Lead-free indicator |
| Hash suffix | `#` | LTC2053HMS8**#PBF** → LTC2053HMS8 | Linear Technology | RoHS, Tape & Reel (#PBF, #TR, #TRPBF) |
| Slash suffix | `/` | TJA1050T**/CM,118** → TJA1050T | NXP | Ordering codes (/CM,118, /SN) |
| Comma suffix | `,` | NC7WZ04**,315** → NC7WZ04 | Various | Generic ordering codes |

**API:**
```java
String base = MPNUtils.stripPackageSuffix("MAX3483EESA+");
// Returns: "MAX3483EESA"
```

**Implementation (MPNUtils.java lines 565-597):**
```java
public static String stripPackageSuffix(String mpn) {
    if (mpn == null || mpn.trim().isEmpty()) {
        return "";
    }

    String trimmed = mpn.trim();

    // Pattern 1: + suffix (Maxim/Analog Devices lead-free)
    if (trimmed.endsWith("+")) {
        return trimmed.substring(0, trimmed.length() - 1);
    }

    // Pattern 2: # suffix (Linear Tech: #PBF, #TR, #TRPBF)
    int hashIndex = trimmed.indexOf('#');
    if (hashIndex > 0) {
        return trimmed.substring(0, hashIndex);
    }

    // Pattern 3: / suffix (NXP ordering codes: /CM,118)
    int slashIndex = trimmed.indexOf('/');
    if (slashIndex > 0) {
        return trimmed.substring(0, slashIndex);
    }

    // Pattern 4: , suffix (some ordering codes)
    int commaIndex = trimmed.indexOf(',');
    if (commaIndex > 0) {
        return trimmed.substring(0, commaIndex);
    }

    // No recognized suffix
    return trimmed;
}
```

**Gotcha:** Single-letter suffixes are NOT stripped to avoid false positives:
```java
MPNUtils.stripPackageSuffix("NC7WZ04G");  // Returns: "NC7WZ04G" (not "NC7WZ04")
// "G" could be part of base MPN, not a packaging suffix
```

---

### 2. getSearchVariations() - Generate Search Variants

**Purpose:** Generate MPN variations for datasheet searches, component lookups, and supplier matching.

**Use cases:**
- **Datasheet searches:** Try both "MAX3483EESA+" and "MAX3483EESA" for better results
- **Component deduplication:** Recognize variants as same component
- **Supplier matching:** Match parts despite different packaging codes

**API:**
```java
List<String> variations = MPNUtils.getSearchVariations("LTC2053HMS8#PBF");
// Returns: ["LTC2053HMS8#PBF", "LTC2053HMS8"]
```

**Implementation (MPNUtils.java lines 619-636):**
```java
public static List<String> getSearchVariations(String mpn) {
    List<String> variations = new ArrayList<>();

    if (mpn == null || mpn.trim().isEmpty()) {
        return variations;
    }

    // Always include original
    variations.add(mpn);

    // Add base part if different
    String base = stripPackageSuffix(mpn);
    if (!base.isEmpty() && !base.equals(mpn)) {
        variations.add(base);
    }

    return variations;
}
```

**Examples:**
```java
MPNUtils.getSearchVariations("MAX3483EESA+");
// → ["MAX3483EESA+", "MAX3483EESA"]

MPNUtils.getSearchVariations("LTC2053HMS8#PBF");
// → ["LTC2053HMS8#PBF", "LTC2053HMS8"]

MPNUtils.getSearchVariations("ADS1115");
// → ["ADS1115"]  // No variations if no suffix
```

---

### 3. isEquivalentMPN() - Component Equivalence Check

**Purpose:** Check if two MPNs refer to the same base component, ignoring packaging differences.

**Use cases:**
- **BOM validation:** Verify supplier parts match design MPNs
- **Component deduplication:** Merge duplicate entries in inventory
- **Cross-referencing:** Identify same component across different suppliers

**API:**
```java
boolean equiv = MPNUtils.isEquivalentMPN("LTC2053HMS8#PBF", "LTC2053HMS8#TR");
// Returns: true (same base component, different tape/reel)
```

**Implementation (MPNUtils.java lines 656-669):**
```java
public static boolean isEquivalentMPN(String mpn1, String mpn2) {
    if (mpn1 == null || mpn2 == null) {
        return false;
    }

    String base1 = stripPackageSuffix(mpn1);
    String base2 = stripPackageSuffix(mpn2);

    if (base1.isEmpty() || base2.isEmpty()) {
        return false;
    }

    return base1.equalsIgnoreCase(base2);  // Case-insensitive comparison
}
```

**Examples:**
```java
// ✅ Equivalent (same base, different packaging)
MPNUtils.isEquivalentMPN("MAX3483EESA+", "MAX3483EESA");
// → true

MPNUtils.isEquivalentMPN("LTC2053HMS8#PBF", "LTC2053HMS8#TR");
// → true (same chip, different tape/reel)

MPNUtils.isEquivalentMPN("TJA1050T/CM,118", "TJA1050T/SN");
// → true (same part, different ordering codes)

// ❌ Not equivalent (different base parts)
MPNUtils.isEquivalentMPN("NC7WZ485M8X", "NC7WZ240");
// → false (different chips)

MPNUtils.isEquivalentMPN("LM358", "LM324");
// → false (different op-amp configurations: dual vs quad)
```

---

### 4. getPackageSuffix() - Extract Package Suffix

**Purpose:** Extract the package suffix from an MPN, if present.

**API:**
```java
Optional<String> suffix = MPNUtils.getPackageSuffix("MAX3483EESA+");
// Returns: Optional.of("+")

Optional<String> suffix = MPNUtils.getPackageSuffix("ADS1115");
// Returns: Optional.empty()
```

**Implementation (MPNUtils.java lines 685-696):**
```java
public static Optional<String> getPackageSuffix(String mpn) {
    if (mpn == null || mpn.trim().isEmpty()) {
        return Optional.empty();
    }

    String base = stripPackageSuffix(mpn);
    if (base.equals(mpn.trim())) {
        return Optional.empty();  // No suffix found
    }

    return Optional.of(mpn.trim().substring(base.length()));
}
```

**Examples:**
```java
MPNUtils.getPackageSuffix("MAX3483EESA+");
// → Optional.of("+")

MPNUtils.getPackageSuffix("LTC2053HMS8#PBF");
// → Optional.of("#PBF")

MPNUtils.getPackageSuffix("TJA1050T/CM,118");
// → Optional.of("/CM,118")

MPNUtils.getPackageSuffix("ADS1115IDGSR");
// → Optional.empty()
```

---

### 5. normalize() - Case and Special Character Removal

**Purpose:** Normalize MPNs for consistent matching by removing special characters and converting to uppercase.

**API:**
```java
String normalized = MPNUtils.normalize("LM358-N");
// Returns: "LM358N"
```

**Implementation (MPNUtils.java lines 56-61):**
```java
private static final Pattern SPECIAL_CHARS = Pattern.compile("[^A-Z0-9]");

public static String normalize(String mpn) {
    if (mpn == null || mpn.trim().isEmpty()) {
        return "";
    }
    return SPECIAL_CHARS.matcher(mpn.trim().toUpperCase()).replaceAll("");
}
```

**Critical for position-based extraction:**
```java
// ❌ WRONG: Using MPN directly with hyphens
String mpn = "ATMEGA328P-PU";
char packageChar = mpn.charAt(mpn.length() - 2);  // Returns 'P' (from "-PU"), not package!

// ✅ CORRECT: Normalize first
String normalized = MPNUtils.normalize("ATMEGA328P-PU");  // → "ATMEGA328PPU"
// Now position-based extraction works correctly
```

**Examples:**
```java
MPNUtils.normalize("LM358-N");
// → "LM358N"

MPNUtils.normalize("stm32f103c8t6");
// → "STM32F103C8T6"

MPNUtils.normalize("  LM7805/CT  ");
// → "LM7805CT"

MPNUtils.normalize("MAX3483+");
// → "MAX3483+"  // Plus is removed by normalization (not A-Z0-9)
// Wait, that's wrong - let me check...
```

**IMPORTANT NOTE:** `normalize()` removes ALL non-alphanumeric characters, including package suffix delimiters (+, #, /, ,). This makes it INCOMPATIBLE with `stripPackageSuffix()` which relies on delimiters.

**Use normalize() for:**
- MPN comparison (case-insensitive, whitespace-agnostic)
- Manufacturer detection
- Type detection

**DO NOT use normalize() before stripPackageSuffix():**
```java
// ❌ WRONG: normalize() removes delimiters
String base = MPNUtils.stripPackageSuffix(MPNUtils.normalize("MAX3483+"));
// → "MAX3483" (normalize removed +, stripPackageSuffix does nothing)

// ✅ CORRECT: stripPackageSuffix first, then normalize if needed
String base = MPNUtils.stripPackageSuffix("MAX3483+");  // → "MAX3483"
String normalized = MPNUtils.normalize(base);           // → "MAX3483"
```

---

## Unicode Gotcha: µ → Μ Uppercasing Issue

**Problem:** The micro sign µ (U+00B5) becomes Greek MU Μ (U+039C) when uppercased, breaking parsing.

### The Issue

```java
String mpn = "10µF";
String upper = mpn.toUpperCase();
// upper = "10ΜF"  (Greek MU Μ, NOT Latin M!)

// Parsing logic looking for "u" or "µ" fails
if (upper.contains("UF")) {  // ❌ FALSE - contains "ΜF" (Greek MU)
    // Parse microfarads
}
```

**Character codes:**
- Micro sign: µ (U+00B5)
- Greek MU (uppercase): Μ (U+039C)
- Latin M (uppercase): M (U+004D)

These are DIFFERENT Unicode characters!

### Impact on CapacitorSimilarityCalculator

**parseCapacitanceValue() (lines 189-192):**
```java
// ✅ CORRECT: Replace µ/Μ with 'u' BEFORE normalizing
String normalized = value.replace("µ", "u").replace("Μ", "u");
normalized = normalizeValue(normalized);
if (normalized == null) {
    return null;
```

**Why this matters:**
```java
// Input: "10µF"
// Step 1: Replace µ → u:  "10uF"
// Step 2: toUpperCase():   "10UF"
// Step 3: Parse successfully

// ❌ WRONG: toUpperCase() first
// Input: "10µF"
// Step 1: toUpperCase():   "10ΜF"  (Greek MU!)
// Step 2: Try to parse:    FAILS - looking for "UF", finds "ΜF"
```

### Solution Pattern

**Always replace µ/Μ with 'u' BEFORE calling toUpperCase() or normalize():**

```java
// ✅ CORRECT Pattern
public static String parseValue(String value) {
    // 1. Replace micro variants with 'u' FIRST
    String cleaned = value.replace("µ", "u").replace("Μ", "u");

    // 2. NOW safe to uppercase
    String upper = cleaned.toUpperCase();

    // 3. Parse normally
    if (upper.endsWith("UF")) {
        // Parse microfarads
    }
}

// ❌ WRONG Pattern
public static String parseValue(String value) {
    String upper = value.toUpperCase();  // µ → Μ (Greek MU)
    // Parsing will fail!
}
```

### Real-World Examples

```java
// Capacitor MPNs with micro sign
"GRM188R71H103KA01D"  // 10nF (no micro sign)
"C1206C105K4RAC"      // 1µF (might be written as "1uF" or "1µF")

// If user input contains µ:
"1µF" → replace µ with u → "1uF" → uppercase → "1UF" → parse ✅

// If uppercased first:
"1µF" → uppercase → "1ΜF" → parse fails ❌ (looking for "UF", finds "ΜF")
```

---

## Testing Strategy

### ParameterizedTest Examples

**Test all 4 suffix patterns:**
```java
@ParameterizedTest
@CsvSource({
    "MAX3483EESA+, MAX3483EESA",           // Plus suffix
    "LTC2053HMS8#PBF, LTC2053HMS8",        // Hash suffix
    "TJA1050T/CM,118, TJA1050T",           // Slash suffix
    "NC7WZ04,315, NC7WZ04"                 // Comma suffix
})
void testStripPackageSuffix(String input, String expected) {
    assertEquals(expected, MPNUtils.stripPackageSuffix(input));
}
```

**Test edge cases:**
```java
@Test
void testStripPackageSuffix_EdgeCases() {
    assertEquals("", MPNUtils.stripPackageSuffix(null));
    assertEquals("", MPNUtils.stripPackageSuffix(""));
    assertEquals("", MPNUtils.stripPackageSuffix("   "));
    assertEquals("TEST", MPNUtils.stripPackageSuffix("  TEST  "));
}

@Test
void testStripPackageSuffix_SingleCharacter() {
    // Single character - should NOT strip (ambiguous)
    assertEquals("NC7WZ04G", MPNUtils.stripPackageSuffix("NC7WZ04G"));
}
```

**Test search variations:**
```java
@Test
void testGetSearchVariations_WithSuffix() {
    List<String> variations = MPNUtils.getSearchVariations("MAX3483EESA+");
    assertEquals(2, variations.size());
    assertTrue(variations.contains("MAX3483EESA+"));
    assertTrue(variations.contains("MAX3483EESA"));
}

@Test
void testGetSearchVariations_NoSuffix() {
    List<String> variations = MPNUtils.getSearchVariations("ADS1115");
    assertEquals(1, variations.size());
    assertTrue(variations.contains("ADS1115"));
}
```

**Test equivalence:**
```java
@ParameterizedTest
@CsvSource({
    "MAX3483EESA+, MAX3483EESA, true",
    "LTC2053HMS8#PBF, LTC2053HMS8#TR, true",
    "TJA1050T/CM,118, TJA1050T/SN, true",
    "NC7WZ485M8X, NC7WZ240, false",
    "LM358, LM324, false"
})
void testIsEquivalentMPN(String mpn1, String mpn2, boolean expected) {
    assertEquals(expected, MPNUtils.isEquivalentMPN(mpn1, mpn2));
}
```

---

## Manufacturer-Specific Notes

### Maxim Integrated / Analog Devices
**Suffix:** `+` (plus sign)
- **Meaning:** Lead-free, RoHS compliant
- **Examples:**
  - MAX3483EESA+ → MAX3483EESA
  - MAX485ESA+ → MAX485ESA
  - ADS1115IDGSR+ → ADS1115IDGSR

### Linear Technology (now Analog Devices)
**Suffix:** `#PBF`, `#TR`, `#TRPBF`
- **Meaning:**
  - PBF = Lead-free (Pb-Free)
  - TR = Tape and Reel
  - TRPBF = Tape and Reel, Lead-free
- **Examples:**
  - LTC2053HMS8#PBF → LTC2053HMS8
  - LT1117CST#TR → LT1117CST
  - LTC2053HMS8#TRPBF → LTC2053HMS8

### NXP Semiconductors
**Suffix:** `/CM,118`, `/SN`
- **Meaning:** Ordering codes, package variants
- **Examples:**
  - TJA1050T/CM,118 → TJA1050T
  - MCP2551-I/SN → MCP2551-I

### Various Manufacturers
**Suffix:** `,315`, `,118`
- **Meaning:** Generic ordering codes
- **Examples:**
  - NC7WZ04,315 → NC7WZ04

---

## Gotchas

### 1. First Delimiter Wins
If multiple delimiters exist, the first match is used:
```java
MPNUtils.stripPackageSuffix("PART#ABC/XYZ");
// Returns: "PART" (stops at #, ignores /)
```

### 2. Conservative Design
Single-letter suffixes are NOT stripped to avoid false positives:
```java
MPNUtils.stripPackageSuffix("NC7WZ04G");
// Returns: "NC7WZ04G" (not "NC7WZ04")
// "G" could be part of base MPN
```

### 3. No Manufacturer-Specific Logic
The stripPackageSuffix() method uses generic patterns, not ManufacturerHandler integration:
```java
// Works for known patterns
MPNUtils.stripPackageSuffix("MAX3483+");  // → "MAX3483"

// Doesn't understand manufacturer-specific suffixes
MPNUtils.stripPackageSuffix("LM7805CT");  // → "LM7805CT" (CT not recognized)
// "CT" is a TI voltage regulator package suffix, but not a recognized delimiter
```

For manufacturer-specific package extraction, use `ManufacturerHandler.extractPackageCode()` instead.

### 4. Case-Insensitive Equivalence
`isEquivalentMPN()` uses case-insensitive comparison:
```java
MPNUtils.isEquivalentMPN("lm358n", "LM358N");
// → true
```

---

## See Also

- `/component` - Base skill for general component work
- `/handler-pattern-design` - Handler patterns (uses normalization for package extraction)
- `MPNUtils.java` (lines 54-697) - Implementation details
- `MPNPackageSuffixTest.java` - 32 comprehensive tests
- `CLAUDE.md` - MPN Package Suffix Support section
- `HISTORY.md` - PR #128 (MPN package suffix support)
