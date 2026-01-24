# Handler Pattern Design

Use this skill when creating new manufacturer handlers or refactoring existing ones. This skill codifies proven patterns, anti-patterns, and best practices from 67 production handlers.

## Core Patterns

### 1. Set.of() vs HashSet in getSupportedTypes()

**Pattern: Always use immutable Set.of() instead of mutable HashSet.**

**Why:** Immutable collections prevent accidental modification and are more efficient.

```java
// ✅ CORRECT: Use Set.of()
@Override
public Set<ComponentType> getSupportedTypes() {
    return Set.of(
        ComponentType.MICROCONTROLLER,
        ComponentType.MICROCONTROLLER_ST,
        ComponentType.MOSFET,
        ComponentType.MOSFET_ST,
        ComponentType.VOLTAGE_REGULATOR
    );
}

// ❌ WRONG: Don't use HashSet
@Override
public Set<ComponentType> getSupportedTypes() {
    Set<ComponentType> types = new HashSet<>();
    types.add(ComponentType.MICROCONTROLLER);
    types.add(ComponentType.MOSFET);
    return types;  // Mutable set can be modified!
}
```

**Technical debt:** 34 handlers still use HashSet and need migration to Set.of().

---

### 2. Dual Type Registration (Base + Manufacturer-Specific)

**Pattern: Register patterns for BOTH the base type AND the manufacturer-specific type.**

**Why:** Users may search for either generic "MOSFET" or specific "MOSFET_INFINEON". Both must work.

```java
@Override
public void initializePatterns(PatternRegistry registry) {
    // ✅ CORRECT: Register both base and specific types
    registry.addPattern(ComponentType.MOSFET, "^IRF[0-9]+.*");        // Base type
    registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRF[0-9]+.*"); // Specific type

    registry.addPattern(ComponentType.OPAMP, "^LM358.*");
    registry.addPattern(ComponentType.OPAMP_TI, "^LM358.*");
}

// ❌ WRONG: Only registering manufacturer-specific type
registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRF[0-9]+.*");
// Users searching for ComponentType.MOSFET will get 0 results!
```

**TIHandler example (lines 366-376):**
```java
for (Map.Entry<String, ComponentSeriesInfo> entry : COMPONENT_SERIES.entrySet()) {
    ComponentSeriesInfo info = entry.getValue();
    // Register primary type
    registry.addPattern(info.primaryType, info.pattern);
    // Register base component type
    registry.addPattern(info.primaryType.getBaseType(), info.pattern);
    // Register additional types if any
    for (ComponentType additionalType : info.additionalTypes) {
        registry.addPattern(additionalType, info.pattern);
    }
}
```

---

### 3. matches() Override Strategy: matchesForCurrentHandler() NOT getPattern()

**Pattern: Use `matchesForCurrentHandler()` to prevent cross-handler false matches.**

**Why:** Using `patterns.matches()` or `patterns.getPattern()` searches ALL handlers' patterns, causing false positives when another manufacturer shares a prefix.

```java
@Override
public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
    if (mpn == null || type == null) return false;

    String upperMpn = mpn.toUpperCase();

    // 1. Do specific checks for your manufacturer first
    if (upperMpn.startsWith("STM32")) {
        return type == ComponentType.MICROCONTROLLER || type == ComponentType.MICROCONTROLLER_ST;
    }

    // 2. ✅ CORRECT: Use matchesForCurrentHandler() for fallback
    return patterns.matchesForCurrentHandler(upperMpn, type);

    // 3. ❌ WRONG: Don't use patterns.matches() - searches ALL handlers!
    // return patterns.matches(upperMpn, type);  // Cross-handler contamination!
}
```

**NXPHandler example (line 180):**
```java
// Use handler-specific patterns for other matches (avoid cross-handler false matches)
return patterns.matchesForCurrentHandler(upperMpn, type);
```

**Critical bug example (PR #89):** AtmelHandler used `patterns.matches()` and incorrectly matched PIC microcontrollers (Microchip), STM32 (ST), and NXP parts as Atmel because it returned true for base `MICROCONTROLLER` type from other handlers' patterns.

---

### 4. extractPackageCode() with Normalization

**Pattern: Always normalize MPN to uppercase BEFORE position-based extraction.**

**Why:** Position-based extraction with charAt() breaks if hyphens or special chars are present. MPN normalization (via `MPNUtils.normalize()`) handles this, but at minimum always uppercase.

```java
@Override
public String extractPackageCode(String mpn) {
    if (mpn == null || mpn.isEmpty()) return "";

    // ✅ CORRECT: Normalize to uppercase first
    String upperMpn = mpn.toUpperCase();

    // STM32F103C8T6 → package is second-to-last char
    if (upperMpn.startsWith("STM32") && upperMpn.length() >= 2) {
        char packageChar = upperMpn.charAt(upperMpn.length() - 2);  // 'T'
        return switch (packageChar) {
            case 'T' -> "LQFP";
            case 'H' -> "BGA";
            case 'U' -> "VFQFPN";
            default -> String.valueOf(packageChar);
        };
    }

    // ❌ WRONG: Using mpn directly without normalization
    // char packageChar = mpn.charAt(mpn.length() - 2);  // Fails if mpn is lowercase!

    return "";
}
```

**AbstractManufacturerHandler helpers (lines 66-94):**
```java
// Use centralized PackageCodeRegistry
protected String extractPackageCode(String mpn) {
    if (mpn == null || mpn.isEmpty()) return "";

    String upperMpn = mpn.toUpperCase();

    // Try hyphen-separated suffix first (e.g., "ATMEGA328P-PU" -> "PU")
    String suffix = extractSuffixAfterHyphen(upperMpn);
    if (!suffix.isEmpty() && PackageCodeRegistry.isKnownCode(suffix)) {
        return PackageCodeRegistry.resolve(suffix);
    }

    // Try common suffix patterns at end of MPN
    suffix = extractTrailingSuffix(upperMpn);
    if (!suffix.isEmpty() && PackageCodeRegistry.isKnownCode(suffix)) {
        return PackageCodeRegistry.resolve(suffix);
    }

    return "";
}
```

**TIHandler override (lines 436-467):** Overrides for manufacturer-specific package extraction but still calls helper methods.

---

### 5. extractSeries() Pattern Ordering: Specific Before Generic

**Pattern: Check longer/more specific patterns BEFORE shorter/generic patterns.**

**Why:** Prevents false matches. "MPXV5010" should match "MPXV" series, not "MPX" series.

```java
@Override
public String extractSeries(String mpn) {
    if (mpn == null || mpn.isEmpty()) return "";
    String upperMpn = mpn.toUpperCase();

    // ✅ CORRECT: Check specific prefixes first, then generic
    if (upperMpn.startsWith("MPXV")) return "MPXV Pressure Sensor";  // Specific
    if (upperMpn.startsWith("MPXA")) return "MPXA Pressure Sensor";  // Specific
    if (upperMpn.startsWith("MPX")) return "MPX Pressure Sensor";    // Generic last

    // ❌ WRONG: Generic first - "MPXV5010" would incorrectly return "MPX"
    // if (upperMpn.startsWith("MPX")) return "MPX Pressure Sensor";
    // if (upperMpn.startsWith("MPXV")) return "MPXV Pressure Sensor";

    return "";
}
```

**NXPHandler example (lines 355-358):**
```java
// Sensors - CHECK SPECIFIC PREFIXES FIRST!
if (upperMpn.startsWith("MPXV")) return "MPXV Pressure Sensor";
if (upperMpn.startsWith("MPXA")) return "MPXA Pressure Sensor";
if (upperMpn.startsWith("MPX")) return "MPX Pressure Sensor";  // Generic last
```

**AbstractManufacturerHandler helper (lines 135-153):**
```java
@Override
public String extractSeries(String mpn) {
    if (mpn == null || mpn.isEmpty()) return "";

    String upperMpn = mpn.toUpperCase();

    // Default: extract prefix + first numeric sequence
    int firstDigit = findFirstDigitIndex(upperMpn, 0);
    if (firstDigit < 0) {
        return upperMpn; // No digits, return whole thing
    }

    int endOfDigits = firstDigit;
    while (endOfDigits < upperMpn.length() && Character.isDigit(upperMpn.charAt(endOfDigits))) {
        endOfDigits++;
    }

    return upperMpn.substring(0, endOfDigits);
}
```

---

## Handler Testing Strategy

### Test File Naming and Package Placement

**CRITICAL: Test files MUST NOT be in the `manufacturers` package.**

**Why:** Test-classes directory shadows main-classes directory. If handler tests are in `no.cantara.electronic.component.lib.manufacturers`, the test-classes directory will be found first during classpath scanning, causing `ManufacturerHandlerFactory` to find 0 handlers (catastrophic failure).

```
✅ CORRECT Package:
no.cantara.electronic.component.lib.handlers
  └── TIHandlerTest.java
  └── STHandlerTest.java
  └── NXPHandlerTest.java

❌ WRONG Package (NEVER DO THIS):
no.cantara.electronic.component.lib.manufacturers
  └── TIHandlerTest.java  // CLASSPATH SHADOWING - breaks ManufacturerHandlerFactory!
```

**Test file structure:**
```java
package no.cantara.electronic.component.lib.handlers;  // ✅ handlers, not manufacturers

import no.cantara.electronic.component.lib.manufacturers.TIHandler;  // Import handler from manufacturers package
import org.junit.jupiter.api.*;

class TIHandlerTest {
    private static TIHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new TIHandler();  // Direct instantiation
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }
}
```

---

### Circular Initialization Prevention

**Problem:** ComponentType ↔ ComponentManufacturer ↔ Handler circular dependency can cause `ExceptionInInitializerError`.

**Solution: Use direct instantiation in @BeforeAll.**

```java
@BeforeAll
static void setUp() {
    // ✅ CORRECT: Direct instantiation - simple and avoids circular dependency
    handler = new TIHandler();
    registry = new PatternRegistry();
    handler.initializePatterns(registry);
}

// ❌ WRONG: Using MPNUtils lookup triggers ComponentManufacturer loading
// handler = (TIHandler) MPNUtils.getManufacturerHandler("LM358");
// This can cause ExceptionInInitializerError if ComponentManufacturer enum isn't loaded yet
```

**Alternative (if direct instantiation fails):** Use `MPNUtils.getManufacturerHandler("MPN")` if direct instantiation causes issues.

**Test isolation gotcha:** Running a single handler test may fail while full suite passes. This is due to class loading order - other tests initialize classes first. If single test fails with initialization errors, run full suite to verify.

---

### Nested Test Classes with @DisplayName

**Pattern: Group related tests with nested classes for better organization.**

```java
class TIHandlerTest {

    @Nested
    @DisplayName("Op-Amp Detection")
    class OpAmpTests {

        @ParameterizedTest
        @DisplayName("Should detect LM358 variants as OPAMP_TI")
        @ValueSource(strings = {"LM358", "LM358N", "LM358D", "LM358PW"})
        void shouldDetectLM358Variants(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.OPAMP_TI, registry));
            assertTrue(handler.matches(mpn, ComponentType.OPAMP, registry));
        }
    }

    @Nested
    @DisplayName("Voltage Regulator Detection")
    class VoltageRegulatorTests {

        @ParameterizedTest
        @CsvSource({
            "LM7805, VOLTAGE_REGULATOR_LINEAR_TI",
            "LM7812, VOLTAGE_REGULATOR_LINEAR_TI",
            "UA7805, VOLTAGE_REGULATOR_LINEAR_TI"
        })
        void shouldDetect78xxRegulators(String mpn, String expectedType) {
            ComponentType type = ComponentType.valueOf(expectedType);
            assertTrue(handler.matches(mpn, type, registry));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        // ...
    }
}
```

**Benefits:**
- Clear test organization matching handler functionality
- Easy to navigate test results
- Logical grouping of related test cases

---

### ParameterizedTest Examples

**Pattern: Use @ParameterizedTest for testing multiple MPNs with same logic.**

```java
// Simple list of MPNs
@ParameterizedTest
@ValueSource(strings = {"LM358", "LM358N", "LM358D", "LM358PW"})
void shouldDetectLM358Variants(String mpn) {
    assertTrue(handler.matches(mpn, ComponentType.OPAMP_TI, registry));
}

// MPN + expected result pairs
@ParameterizedTest
@CsvSource({
    "LM7805CT, TO-220",
    "LM7805DT, SOT-223",
    "LM7805KC, TO-252",
    "LM7805T, TO-220"
})
void shouldExtractVoltageRegulatorPackages(String mpn, String expectedPackage) {
    assertEquals(expectedPackage, handler.extractPackageCode(mpn));
}

// MPN + multiple expected values
@ParameterizedTest
@CsvSource({
    "LM7805, VOLTAGE_REGULATOR_LINEAR_TI",
    "LM7812, VOLTAGE_REGULATOR_LINEAR_TI",
    "UA7805, VOLTAGE_REGULATOR_LINEAR_TI"
})
void shouldDetect78xxRegulators(String mpn, String expectedType) {
    ComponentType type = ComponentType.valueOf(expectedType);
    assertTrue(handler.matches(mpn, type, registry));
}
```

---

## Handler Cleanup Checklist

When refactoring an existing handler, follow this 9-step checklist (established in PR #77):

### 1. Replace HashSet with Set.of() in getSupportedTypes()
**Before:**
```java
Set<ComponentType> types = new HashSet<>();
types.add(ComponentType.MOSFET);
return types;
```

**After:**
```java
return Set.of(ComponentType.MOSFET, ComponentType.MOSFET_INFINEON);
```

### 2. Migrate local package code maps to PackageCodeRegistry
**Before:**
```java
private static final Map<String, String> PACKAGE_CODES = Map.of(
    "CT", "TO-220",
    "DT", "SOT-223"
);
```

**After:**
```java
// Use centralized PackageCodeRegistry.resolve("CT") → "TO-220"
// Add new codes to PackageCodeRegistry if missing
```

### 3. Remove unused enums and methods
- Delete `ComponentType` enum duplicates (use main ComponentType enum)
- Delete unused helper methods
- Delete commented-out code

### 4. Check suffix ordering (longer before shorter)
```java
// ✅ CORRECT: Check "DT" before "T"
if (upperMpn.endsWith("DT")) return "SOT-223";
if (upperMpn.endsWith("CT")) return "TO-220";
if (upperMpn.endsWith("T")) return "TO-220";

// ❌ WRONG: "T" matches first, never checks "DT" or "CT"
if (upperMpn.endsWith("T")) return "TO-220";
if (upperMpn.endsWith("DT")) return "SOT-223";  // Never reached!
```

### 5. Use matchesForCurrentHandler() instead of matches()
```java
// ✅ CORRECT
return patterns.matchesForCurrentHandler(upperMpn, type);

// ❌ WRONG: Searches all handlers' patterns
return patterns.matches(upperMpn, type);
```

### 6. Add comprehensive tests with nested classes
- Use TIHandlerTest, STHandlerTest, NXPHandlerTest as templates
- Organize with @Nested classes
- Use @ParameterizedTest for multiple MPNs

### 7. Verify dual type registration (base + specific)
```java
registry.addPattern(ComponentType.MOSFET, "^IRF.*");
registry.addPattern(ComponentType.MOSFET_INFINEON, "^IRF.*");
```

### 8. Update getSupportedTypes() to include ALL registered types
If you register patterns for `VOLTAGE_REGULATOR_LINEAR_TI`, it must be in `getSupportedTypes()`.

### 9. Run full test suite to verify no regressions
```bash
mvn test -Dtest=TIHandlerTest        # Single handler test
mvn test -Dtest=*HandlerTest         # All handler tests
mvn test                             # Full suite
```

---

## Common Anti-Patterns

| Anti-Pattern | Problem | Solution |
|-------------|---------|----------|
| **Using HashSet instead of Set.of()** | Mutable set, less efficient | Use `Set.of(...)` for immutable sets |
| **Only registering manufacturer-specific types** | Users searching for base type get 0 results | Register BOTH base and specific types |
| **Using patterns.matches() in matches()** | Cross-handler false matches | Use `patterns.matchesForCurrentHandler()` |
| **Suffix ordering: short before long** | "T" matches before "DT", never checks "DT" | Check longer suffixes first (DT → CT → T) |
| **Not uppercasing MPN before charAt()** | Fails on lowercase MPNs | Always `String upperMpn = mpn.toUpperCase()` |
| **Test in manufacturers package** | Classpath shadowing breaks handler loading | Put tests in `handlers` package, not `manufacturers` |
| **Complex regex when simple startsWith() works** | Slower, harder to maintain | Use `startsWith()` for simple prefix checks |

---

## Handler Examples

### Simple Handlers (~100 lines)

**AVXHandler:** Ceramic capacitor manufacturer, simple prefix patterns.
- Pattern: `^08[0-9]{4}.*` (0805 ceramic), `^12[0-9]{4}.*` (1206 ceramic)
- Package extraction: Read positions 2-3 after prefix for size code
- Use as template for: Simple component manufacturers with predictable MPN structure

**TEHandler:** TE Connectivity connectors.
- Pattern: Connector part numbers with simple prefix matching
- Use as template for: Connector manufacturers

### Medium Complexity Handlers (~200-300 lines)

**TIHandler:** Texas Instruments (628 lines, but well-structured).
- Handles: Op-amps, voltage regulators, temperature sensors, LEDs
- Complex series mapping with ComponentSeriesInfo class
- Suffix ordering critical: LM7805CT → "DT" before "T"
- Distinguishes LM35 (temp sensor) from LM358 (op-amp)
- Use as template for: Multi-product-line manufacturers

**STHandler:** STMicroelectronics (240 lines).
- Handles: STM32/STM8 microcontrollers, MOSFETs, voltage regulators
- Package extraction from position (STM32F103C8T6 → 'T' = LQFP)
- Prefix-based series detection
- Use as template for: Microcontroller manufacturers

**NXPHandler:** NXP Semiconductors (393 lines).
- Handles: LPC/Kinetis/i.MX MCUs, memory, MOSFETs, transistors, sensors
- Complex package extraction with position tracking
- Specific prefix ordering (MPXV → MPXA → MPX)
- Uses `matchesForCurrentHandler()` correctly
- Use as template for: Diversified semiconductor manufacturers

### Complex Handlers (~400+ lines)

**InfineonHandler:** Infineon Technologies.
- Handles: IRF/IRFP/BSC MOSFETs, XMC microcontrollers, OptiMOS
- Complex series extraction with subfamily detection
- Multiple product lines with different MPN structures
- Use as template for: Power semiconductor manufacturers with multiple families

**MicrochipHandler:** Microchip Technology.
- Handles: PIC/dsPIC/AVR/SAM microcontrollers
- Merged Atmel product lines (AVR, ATmega, ATtiny)
- Complex package suffix decoding (-PU, -AU, /SS, /ML)
- Use as template for: Manufacturers with merged product portfolios

---

## Learnings & Quirks

### Handler Ordering Matters
`ManufacturerHandlerFactory` uses `TreeSet` with deterministic ordering. First matching handler wins in `getManufacturerHandler()`.

### ComponentType.getBaseType() Completeness
All manufacturer-specific types MUST be in the switch statement. Missing types fall through to `default -> this` (returns self, not base type).

### Pattern Specificity Scoring
`MPNUtils.getComponentType()` uses specificity scoring:
- Manufacturer-specific (OPAMP_TI): 150 points
- Base type (OPAMP): 100 points
- Category (ANALOG_IC): 50 points
- Generic (IC): -50 points

Without scoring, HashSet iteration could return IC instead of OPAMP_TI.

### AbstractManufacturerHandler Benefits
- Centralized package code resolution via `PackageCodeRegistry`
- Common helper methods: `extractSuffixAfterHyphen()`, `findFirstDigitIndex()`, etc.
- Default `isOfficialReplacement()` implementation
- Reduces code duplication across handlers

---

## See Also

- `/component` - Base skill for general component work
- `/architecture` - Refactoring and cleanup guidance
- `/mpn-normalization` - MPN suffix handling and normalization
- `AbstractManufacturerHandler.java` - Base class with helper methods
- `TIHandlerTest.java`, `STHandlerTest.java`, `NXPHandlerTest.java` - Test templates
