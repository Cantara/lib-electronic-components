# Handler Implementation Patterns

Best practices and patterns for developing manufacturer handlers in the lib-electronic-components library.

## Table of Contents
- [Position-Based MPN Encoding (Jinling Case Study)](#position-based-mpn-encoding-jinling-case-study)
- [Handler Cleanup Checklist](#handler-cleanup-checklist)
- [Component-Type Specific Patterns](#component-type-specific-patterns)
- [Testing Strategies](#testing-strategies)
- [Common Pitfalls and Solutions](#common-pitfalls-and-solutions)

---

## Position-Based MPN Encoding (Jinling Case Study)

### Overview
Based on PR #102 (January 2026) - Jinling connector handler implementation demonstrating position-based MPN encoding patterns.

### Critical Discovery
Jinling uses a strict 15-character position-based elprint format where **EVERY position has meaning**:

```
Position-by-position breakdown of "27310202ANG3SUT":

27 31 0 2 02 A N G3 S U T
│  │  │ │ │  │ │ │  │ │ └─ [14] Packing (1 char)
│  │  │ │ │  │ │ │  │ └─── [13] ContactType (1 char)
│  │  │ │ │  │ │ │  └───── [12] ConnectorType (1 char)
│  │  │ │ │  │ │ └─────── [10-11] ContactPlating (2 chars)
│  │  │ │ │  │ └────────── [9] Post (1 char)
│  │  │ │ │  └─────────── [8] InsulatorMaterial (1 char)
│  │  │ │ └──────────── [6-7] PinsPerRow (2 digits)
│  │  │ └────────────── [5] Rows (1 digit)
│  │  └──────────────── [4] HouseCount (1 digit)
│  └─────────────────── [2-3] PlasticsHeight (2 digits)
└────────────────────── [0-1] Family (2 digits)
```

### Key Gotchas

#### 1. Exact Length Required
```java
// ❌ BAD: Variable length (13-17 chars)
if (mpn.length() >= 13 && mpn.length() <= 17) {
    // Won't work - position extraction fails
}

// ✅ GOOD: Exact length validation
if (mpn.length() != 15) {
    return false;  // Not an elprint format MPN
}
```

**Why:** Position-based extraction like `mpn.charAt(5)` fails if length varies. Off-by-one position errors completely change the meaning.

#### 2. Numeric Positions Must Be Validated
```java
// ❌ BAD: Assume positions are numeric
int pinsPerRow = Integer.parseInt(mpn.substring(6, 8));
// Throws NumberFormatException if position 6-7 contains letters

// ✅ GOOD: Validate numeric positions first
boolean hasNumericPrefix = mpn.substring(2, 8).matches("[0-9]{6}");
if (!hasNumericPrefix) {
    return false;  // Not a valid elprint MPN
}
```

**Why:** `Integer.parseInt()` throws exceptions on non-numeric input. Validation prevents runtime errors.

#### 3. No Placeholders in Test Data
```java
// ❌ BAD: Using placeholders
String testMpn = "27310X02ANG3SUT";  // X at position 5
// Position validation fails - X is not a digit

// ✅ GOOD: Real encoding
String testMpn = "27310202ANG3SUT";  // 2 rows at position 5
```

**Why:** Test MPNs must use actual encoding values, not placeholders.

#### 4. Position Alignment Matters
```java
// ❌ BAD: 13-character MPN (missing 2 characters)
"273102NSNSUXT"
// All positions shifted - extraction returns wrong values

// ❌ BAD: 17-character MPN (extra characters)
"27310202ASNSUTT"
// Positions 15-16 don't exist in spec

// ✅ GOOD: Exactly 15 characters
"27310202ANG3SUT"
```

**Example of position shift impact:**
```
Correct:  "27310202ANG3SUT"
          Position 12 = 'G' (connector type)

Wrong:    "27310202MNSNSUT"
          Position 8 = 'M' (should be insulator material)
          Position 12 = 'S' (wrong value for connector type)
```

### Pattern Matching for Position-Based MPNs

```java
// ❌ WRONG: Variable length
"^(?:13|16|17|26|27)[0-9]{4}[0-9]{2}[A-Z][A-Z]{2}[A-Z]{3}"
// Total: 2 + 4 + 2 + 1 + 2 + 3 = 14 chars (missing 1 char)

// ✅ CORRECT: Exact 15 characters
"^(?:13|16|17|26|27)[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$"
// Total: 2 + 6 + 1 + 1 + 2 + 3 = 15 chars
// Breakdown:
//   [0-1]:  Family (13|16|17|26|27)
//   [2-7]:  Numeric positions (6 digits)
//   [8]:    Insulator material (1 letter)
//   [9]:    Post (1 letter)
//   [10-11]: Contact plating (2 alphanumeric)
//   [12-14]: Connector/contact/packing (3 letters)
```

### Dual Format Support

Jinling uses TWO completely different MPN systems:

#### 1. Elprint Format (15 chars)
```
Example: "27310202ANG3SUT"
Type: Position-based internal encoding
Extraction: Fixed positions
```

#### 2. Distributor Format (15-18 chars)
```
Example: "22850102ANG1SYA02"
Type: LCSC/JLCPCB public MPNs
Extraction: Pattern-based, varies by family
```

#### Extraction Logic Differences

**Pin Count Extraction Example:**

```java
public int getPinCount(String mpn) {
    if (isElprintFormat(mpn)) {
        // Elprint: positions 5-7 (rows × pins/row)
        // "27310202" → charAt(5)='2' × substring(6,8)='02' = 4 pins
        int rows = Character.getNumericValue(mpn.charAt(5));
        int pinsPerRow = Integer.parseInt(mpn.substring(6, 8));
        return rows * pinsPerRow;

    } else if (isDistributorIDC(mpn)) {
        // Distributor IDC (32xxx): positions 4-5
        // "321010MG0CBK00A02" → substring(4,6)='10' = 10 pins
        return Integer.parseInt(mpn.substring(4, 6));

    } else if (isDistributorHeader(mpn)) {
        // Distributor Headers (12xxx, 22xxx): positions 6-7
        // "12251140CNG0S115001" → substring(6,8)='40' = 40 pins
        return Integer.parseInt(mpn.substring(6, 8));
    }

    return 0;  // Unknown format
}
```

### Test Suite Journey

Progressive test fixing approach:

| Stage | Tests Passing | Failures | Issue Addressed |
|-------|--------------|----------|-----------------|
| Initial | 91/131 (69%) | 40 | MPN format violations |
| After pattern fix | 106/131 (81%) | 25 | Test MPNs still wrong |
| After MPN corrections | 122/131 (93%) | 9 | Orientation, distributor patterns |
| After distributor fix | 127/131 (97%) | 4 | Pin count, replacement logic |
| Final | 131/131 (100%) | 0 | All tests passing ✓ |

**Lesson:** Incremental fixes - patterns first, test data second, extraction logic third.

### Validation Strategy

**isElprintFormat() Implementation:**
```java
private boolean isElprintFormat(String mpn) {
    if (mpn == null || mpn.length() != 15) {
        return false;  // CRITICAL: Exact 15 characters
    }

    // CRITICAL: Positions 2-7 must be ALL numeric (not just 2-5)
    boolean hasNumericPrefix = mpn.substring(2, 8).matches("[0-9]{6}");
    if (!hasNumericPrefix) {
        return false;
    }

    // Family code must be in known set
    String family = mpn.substring(0, 2);
    return family.equals("13") || family.equals("16") ||
           family.equals("17") || family.equals("26") ||
           family.equals("27");
}
```

**Why This Matters:**
- Position-based extraction uses `substring(6,8)` for pins per row
- If this contains letters, `Integer.parseInt()` throws `NumberFormatException`
- Pin count returns 0 instead of actual value
- Tests fail with "Expected 4, got 0"

### Best Practices from Jinling Implementation

1. **Pattern Design:** For position-based MPNs, validate EXACT length upfront, not ranges

2. **Test Data:** Use REAL distributor MPNs from datasheets/distributors, not invented examples

3. **Incremental Testing:**
   - Step 1: Fix regex patterns (length, character classes)
   - Step 2: Fix test MPN encoding (positions, values)
   - Step 3: Fix extraction logic (parsing, calculation)

4. **Documentation:** ASCII diagrams showing position meanings are invaluable
   ```
   Use visual breakdowns in JavaDoc:
   /**
    * Jinling elprint format (15 chars):
    *
    * Position: 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14
    *           ├──┤ ├────────┤ ├──┤ ├──┤ ├───┤ ├───────┤
    *           Family  Height  Row Pin Material  Type
    *           (2)     (4)     (2) (2)  (4)      (3)
    */
   ```

5. **Dual Format Support:** When manufacturer uses multiple formats, document both clearly
   - Create separate validation methods: `isElprintFormat()`, `isDistributorFormat()`
   - Create separate extraction methods: `extractElprint()`, `extractDistributor()`
   - Document which format is used where (internal vs LCSC/JLCPCB)

### Files Created for Jinling Handler

- **JinlingHandler.java** (~560 lines)
  - Dual-format support
  - 15+ helper methods
  - Position-based and pattern-based extraction

- **JinlingHandlerTest.java** (~550 lines, 131 tests)
  - Comprehensive coverage of both formats
  - Test categories: pattern matching, extraction, pin count, pitch, series

- **.claude/skills/connector/SKILL.md** updated
  - Position-by-position encoding reference
  - Dual format documentation
  - Test MPN examples

---

## Handler Cleanup Checklist

Established pattern from PR #77 (TI Handler cleanup), followed by all subsequent handler PRs.

### 1. Replace HashSet with Set.of() in getSupportedTypes()

```java
// ❌ BAD: Mutable HashSet
@Override
public Set<ComponentType> getSupportedTypes() {
    Set<ComponentType> types = new HashSet<>();
    types.add(ComponentType.MICROCONTROLLER);
    types.add(ComponentType.OPAMP);
    return types;
}

// ✅ GOOD: Immutable Set.of()
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.MICROCONTROLLER,
        ComponentType.OPAMP
    );
}
```

**Benefits:**
- Immutable (thread-safe, prevents accidental modification)
- Prevents duplicates at compile time
- More concise
- Better performance (specialized Set implementation)

### 2. Migrate Local Package Code Maps to PackageCodeRegistry

```java
// ❌ BAD: Local map duplicates mappings
private static final Map<String, String> PACKAGE_CODES = Map.of(
    "PU", "PDIP",
    "AU", "TQFP",
    "D", "SOIC"
);

public String extractPackageCode(String mpn) {
    String suffix = extractSuffix(mpn);
    return PACKAGE_CODES.getOrDefault(suffix, "UNKNOWN");
}

// ✅ GOOD: Use centralized registry
public String extractPackageCode(String mpn) {
    String suffix = extractSuffix(mpn);
    return PackageCodeRegistry.getPackageType(suffix);
}
```

**Benefits:**
- Single source of truth for package code mappings
- Consistency across all handlers
- Easier to maintain and update

### 3. Remove Unused Enums and Methods

```java
// ❌ BAD: Dead code
private enum ObsoleteEnum {
    // Not used anywhere
}

private String helperMethodNeverCalled(String mpn) {
    // Not used anywhere
    return "";
}

// ✅ GOOD: Clean codebase, remove unused code
```

**How to Find:**
- IDE unused code warnings
- Grep for usage across codebase
- Code coverage reports

### 4. Check Suffix Ordering

```java
// ❌ BAD: Shorter suffix checked first
if (mpn.endsWith("T")) {
    return "T_PACKAGE";  // Matches "DT" MPNs incorrectly!
}
if (mpn.endsWith("DT")) {
    return "DT_PACKAGE";  // Never reached
}

// ✅ GOOD: Longer suffixes checked first
if (mpn.endsWith("DT")) {
    return "DT_PACKAGE";  // Check longer suffix first
}
if (mpn.endsWith("T")) {
    return "T_PACKAGE";  // Then check shorter suffix
}
```

**Why:** `"LM358DT".endsWith("T")` returns true, so "T" check must come after "DT" check.

### 5. Add Comprehensive Tests

Use nested test classes by category (see TIHandlerTest pattern):

```java
@DisplayName("TI Handler Tests")
public class TIHandlerTest {

    @Nested
    @DisplayName("Pattern Matching")
    class PatternMatchingTests {
        @Test
        void shouldMatchOpAmpMPNs() { ... }

        @Test
        void shouldMatchVoltageRegulatorMPNs() { ... }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeExtractionTests {
        @Test
        void shouldExtractPDIPPackage() { ... }

        @Test
        void shouldExtractTQFPPackage() { ... }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @Test
        void shouldExtractLM358Series() { ... }

        @Test
        void shouldExtractTL071Series() { ... }
    }
}
```

**Benefits:**
- Clear organization
- Easy to find related tests
- Better test reports
- Encourages comprehensive coverage

---

## Component-Type Specific Patterns

Based on Batch 6 learnings (PR #88) and handler development experience.

### Connector Handlers (Molex, Hirose, JST)

**MPN Format:**
```
SERIES-PACKAGE
Example: "43045-0802"
         ├────┤ ├──┤
         Series Package
```

**Package Code Encoding:**
- First 2 digits: Pin count
- Last 2 digits: Mounting type
- Example: "0802" = 08 pins, 02 mounting type

**Series Number Patterns:**
- Odd numbers: Male/Header (e.g., 43045)
- Even numbers: Female/Receptacle (e.g., 43046)

**Useful Helper Methods:**
```java
public double getPitch(String mpn);           // Pin-to-pin spacing
public String getMountingType(String mpn);     // SMD, TH, etc.
public double getRatedCurrent(String mpn);     // Amperage per contact
```

### Memory Handlers (Winbond, ISSI)

**Multiple Component Types:**
```java
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.MEMORY,
        ComponentType.MEMORY_FLASH,
        ComponentType.MEMORY_EEPROM,
        ComponentType.MEMORY_SRAM,
        ComponentType.IC  // ISSI also makes LED drivers
    );
}
```

**Package Code Patterns:**
- Suffix letters: S=SOIC, F=QFN, R=SOP, etc.
- Example: "W25Q128JVSIQ" → Q suffix = QFN

**Performance Optimization:**
```java
// Winbond pattern: Override matches() for direct pattern matching
@Override
public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
    String upperMpn = mpn.toUpperCase();

    // Direct regex check is faster than registry lookup
    if (upperMpn.matches("^W25[QM].*")) {
        return true;  // Flash memory
    }

    return super.matches(mpn, type, patterns);
}
```

### RF Handlers (Qorvo, Skyworks)

**Component Type Pattern:**
- RF components typically register under `ComponentType.IC`
- **MUST** include `IC` in `getSupportedTypes()` or matches() will succeed but type check fails

```java
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.IC,  // ← CRITICAL: Must include this
        ComponentType.MICROCONTROLLER  // Skyworks acquired Silicon Labs MCUs
    );
}
```

**Series Extraction:**
- Take prefix letters + first 4 digits (not all digits)
- Example: "QPQ1907" → "QPQ1907" (not "QPQ19071234")

**Package Code Suffixes:**
- `-TR1`, `-REEL`: Tape and reel packaging
- `-QFN`, `-WLCSP`: Package type indicators

### Sensor Handlers (Bosch, InvenSense, Melexis)

**Multiple Sensor Types:**
```java
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.SENSOR,
        ComponentType.ACCELEROMETER,
        ComponentType.GYROSCOPE,
        ComponentType.MAGNETOMETER,
        ComponentType.PRESSURE_SENSOR,
        ComponentType.IC  // InvenSense makes audio/motion processors
    );
}
```

**Series Extraction:**
- Typically first 6-8 characters of MPN
- Example: "ADXL345BCCZ" → "ADXL345"

**Known Bug (Melexis):**
```java
// ❌ BAD: Removes ALL alphanumerics
return mpn.replaceAll("^[A-Z0-9]+", "");  // Returns empty string!

// ✅ GOOD: Extract package suffix properly
return mpn.replaceAll("^.*([A-Z]{2})$", "$1");  // Last 2 letters
```

---

## Testing Strategies

### 1. Nested Test Classes by Feature

```java
@DisplayName("Jinling Handler Tests")
public class JinlingHandlerTest {

    @Nested
    @DisplayName("Pattern Matching")
    class PatternMatchingTests {
        // Tests for matches() method
    }

    @Nested
    @DisplayName("Elprint Format")
    class ElprintFormatTests {
        // Tests for 15-character position-based format
    }

    @Nested
    @DisplayName("Distributor Format")
    class DistributorFormatTests {
        // Tests for LCSC/JLCPCB format
    }

    @Nested
    @DisplayName("Pin Count Extraction")
    class PinCountExtractionTests {
        // Tests for both formats
    }
}
```

### 2. Real-World Test Data

```java
// ❌ BAD: Invented test MPNs
"TEST-1234-ABCD"
"FAKE-MPN-001"

// ✅ GOOD: Real MPNs from datasheets/distributors
"27310202ANG3SUT"    // From Jinling elprint spec
"22850102ANG1SYA02"  // From LCSC
```

### 3. Edge Case Coverage

```java
@Test
void shouldHandleNullMPN() {
    assertFalse(handler.matches(null, ComponentType.CONNECTOR));
}

@Test
void shouldHandleEmptyMPN() {
    assertFalse(handler.matches("", ComponentType.CONNECTOR));
}

@Test
void shouldHandleWrongLength() {
    assertFalse(handler.matches("1234567890123456", ComponentType.CONNECTOR));
    // Expected 15 chars, got 16
}

@Test
void shouldHandleNonNumericPositions() {
    assertFalse(handler.matches("27ABCD02ANG3SUT", ComponentType.CONNECTOR));
    // Positions 2-7 must be numeric
}
```

### 4. Incremental Test Fixing

**Approach:**
1. Run full test suite, note failure count
2. Fix one category of issues (e.g., pattern regex)
3. Run tests again, verify improvement
4. Move to next category (e.g., test MPN format)
5. Repeat until 100% passing

**Tracking Progress:**
```
Initial:          91/131 (69%) ← Start
After patterns:  106/131 (81%) ← +15 tests fixed
After test MPNs: 122/131 (93%) ← +16 tests fixed
After extraction: 127/131 (97%) ← +5 tests fixed
Final:           131/131 (100%) ← +4 tests fixed
```

---

## Common Pitfalls and Solutions

### 1. Cross-Handler Pattern Matching

**Problem:** Handler matches MPNs from other manufacturers.

**Solution:**
```java
@Override
public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
    // Use matchesForCurrentHandler() to only check THIS handler's patterns
    return patterns.matchesForCurrentHandler(mpn.toUpperCase(), type);
}
```

**See:** `.docs/history/BUG_FIX_ANALYSIS.md` for detailed analysis.

### 2. Type/Pattern Registration Mismatch

**Problem:** Type in getSupportedTypes() but no patterns registered.

**Solution:**
```java
// Every type MUST have patterns
@Override
protected void initializePatterns(PatternRegistry registry) {
    for (ComponentType type : getSupportedTypes()) {
        // Register at least one pattern for each type
        registry.addPattern(type, getPatternForType(type));
    }
}
```

### 3. Position-Based Extraction with Hyphens

**Problem:** Hyphens shift positions.

**Solution:**
```java
// Normalize first
String normalized = mpn.replace("-", "");
String sizeCode = normalized.charAt(3);
```

### 4. Suffix Ordering

**Problem:** Shorter suffix matches first.

**Solution:**
```java
// Sort by length, longest first
List<String> suffixes = Arrays.asList("DT", "TR", "TA", "T", "R", "A");
suffixes.sort((a, b) -> Integer.compare(b.length(), a.length()));

for (String suffix : suffixes) {
    if (mpn.endsWith(suffix)) {
        return extractWithSuffix(mpn, suffix);
    }
}
```

### 5. IC Type in RF/Sensor Handlers

**Problem:** matches() returns true but getSupportedTypes() doesn't contain IC.

**Solution:**
```java
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.SENSOR,
        ComponentType.IC  // ← Don't forget this!
    );
}
```

---

## Summary

### Key Takeaways

1. **Position-Based MPNs:**
   - Validate EXACT length
   - Validate numeric positions
   - Use real test data
   - Document with ASCII diagrams

2. **Handler Cleanup:**
   - Use Set.of() (immutable)
   - Use PackageCodeRegistry (centralized)
   - Remove dead code
   - Check suffix ordering (longest first)
   - Add comprehensive tests (nested classes)

3. **Component-Type Patterns:**
   - Connectors: Series-Package format, gender encoding
   - Memory: Multiple types, suffix package codes
   - RF: Register under IC, include IC in getSupportedTypes()
   - Sensors: Many sensor types, IC for processors

4. **Testing:**
   - Nested test classes by feature
   - Real-world test data from datasheets
   - Edge case coverage (null, empty, wrong length)
   - Incremental fixing, track progress

5. **Common Pitfalls:**
   - Cross-handler matching → Use matchesForCurrentHandler()
   - Type/pattern mismatch → Register patterns for all types
   - Hyphens in position-based → Normalize first
   - Suffix ordering → Longest first
   - IC type missing → Add to getSupportedTypes()

---

**Last Updated:** January 24, 2026

For bug fix analysis, see [BUG_FIX_ANALYSIS.md](BUG_FIX_ANALYSIS.md).
For chronological history, see [HISTORY.md](../../HISTORY.md).
For active guidance, see [CLAUDE.md](../../CLAUDE.md).
