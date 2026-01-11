---
name: architecture
description: Use when refactoring, cleaning up, or enhancing the lib-electronic-components codebase. Provides guidance on architecture patterns, known issues, duplication hotspots, and recommended improvements.
---

# Software Architecture Skill

This skill provides architectural guidance for enhancing and cleaning up the lib-electronic-components library.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    ComponentType (Enum)                         │
│                    ~200 component types                         │
│         Base types (RESISTOR) + Specific (RESISTOR_CHIP_VISHAY) │
└────────────────────────┬────────────────────────────────────────┘
                         │ matched by
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ComponentManufacturer (Enum)                       │
│                    50+ manufacturers                            │
│         Each holds: regex pattern + ManufacturerHandler         │
└────────────────────────┬────────────────────────────────────────┘
                         │ delegates to
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ManufacturerHandler (Interface)                    │
│                    50+ implementations                          │
│     initializePatterns(), extractPackageCode(), extractSeries() │
└────────────────────────┬────────────────────────────────────────┘
                         │ populates
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                PatternRegistry (Class)                          │
│     Map<ComponentType, Map<Class<?>, Set<Pattern>>>            │
│              Multi-handler pattern storage                      │
└────────────────────────┬────────────────────────────────────────┘
                         │ used by
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│          ComponentSimilarityCalculator (Interface)              │
│                    20+ implementations                          │
│     Per-component-type similarity strategies                    │
└─────────────────────────────────────────────────────────────────┘
```

## Design Patterns In Use

| Pattern | Implementation | Location |
|---------|---------------|----------|
| Registry | `PatternRegistry` | Stores patterns by type and handler |
| Factory | `ManufacturerHandlerFactory` | Dynamic handler discovery |
| Strategy | `ComponentSimilarityCalculator` | Different algorithms per type |
| Enum Dispatch | `ComponentManufacturer` | Enum + handler association |
| Template Method | `ManufacturerHandler.matches()` | Default with override |

---

## Critical Issues to Fix

### 1. TIHandler Pattern Duplication (CRITICAL)

**File**: `manufacturers/TIHandler.java`

The `COMPONENT_SERIES` map contains duplicate entries with conflicting patterns:

```java
// Line 172-179: First LM358 definition
COMPONENT_SERIES.put("LM358", new ComponentSeriesInfo(
    ComponentType.OPAMP_TI,
    "^LM358(?:[AMDP])?(?:N|D|P|DG|PW|DR|DGK|DBV|DRG4)?$", ...

// Line 240-248: SECOND LM358 definition (overwrites first!)
COMPONENT_SERIES.put("LM358", new ComponentSeriesInfo(
    ComponentType.OPAMP_TI,
    "^LM358(?:[A-Z0-9]*(?:N|D|P|DG|PW))?$", ...  // DIFFERENT PATTERN
```

**Duplicated components**:
- LM358: lines 172, 240
- LM7805: lines 153, 182, 519
- LM7905: lines 162, 202, 282, 381, 538
- TL072: lines 269, 306, 395, 451
- LM7812: lines 192, 347, 528
- LM317: lines 122, 212, 557
- NE5532: lines 411 + in TIHandlerPatterns

**Fix**: Consolidate to single definitions, remove duplicates.

### 2. TIHandlerPatterns Unused (HIGH)

**File**: `manufacturers/TIHandlerPatterns.java` was created to extract patterns but TIHandler was never refactored to use it.

**Fix**: Refactor TIHandler to delegate to TIHandlerPatterns, remove inline definitions.

### 3. No Base Handler Class (HIGH)

All 50 handlers duplicate:
- Package code extraction logic
- Series extraction patterns
- Helper methods (findFirstDigit, findLastDigit)

**Fix**: Create `AbstractManufacturerHandler`:

```java
public abstract class AbstractManufacturerHandler implements ManufacturerHandler {
    // Shared package code mappings
    protected static final Map<String, String> COMMON_PACKAGES = Map.of(
        "N", "DIP", "D", "SOIC", "PW", "TSSOP", ...
    );

    // Shared helpers
    protected int findFirstDigit(String str, int start) { ... }
    protected int findLastDigit(String str) { ... }
    protected String extractStandardPackage(String mpn) { ... }
}
```

### 4. ComponentSeriesInfo Duplication (MEDIUM)

Same class defined in two places:
- `TIHandler.java` lines 73-96
- `TIHandlerPatterns.java` lines 265-326

**Fix**: Extract to shared `ComponentSeriesDefinition.java`

### 5. ComponentType.getManufacturer() Broken (MEDIUM)

**File**: `ComponentType.java` lines 497-507

```java
public ComponentManufacturer getManufacturer() {
    String[] parts = name().split("_");
    String mfrName = parts[parts.length - 1];  // "TI"
    return Arrays.stream(ComponentManufacturer.values())
        .filter(m -> m.getName().toUpperCase().contains(mfrName))
        // ComponentManufacturer.TI.getName() = "Texas Instruments"
        // "TEXAS INSTRUMENTS".contains("TI") = true BUT FRAGILE
        .findFirst().orElse(UNKNOWN);
}
```

**Problem**: Relies on manufacturer full name containing the enum suffix.

**Fix**: Add explicit mapping or remove method.

---

## Package Code Duplication

Same mappings repeated across handlers:

**TIHandler** (lines 17-44):
```java
PACKAGE_CODES.put("N", "DIP");
PACKAGE_CODES.put("D", "SOIC");
PACKAGE_CODES.put("PW", "TSSOP");
// ... 15 more
```

**Also in**: AtmelHandler, NXPHandler, STHandler, InfineonHandler...

**Fix**: Create `PackageCodeRegistry`:

```java
public final class PackageCodeRegistry {
    public static final Map<String, String> STANDARD = Map.ofEntries(
        entry("N", "DIP"), entry("P", "DIP"),
        entry("D", "SOIC"), entry("PW", "TSSOP"),
        entry("DGK", "MSOP"), entry("DBV", "SOT-23"),
        entry("T", "TO-220"), entry("KC", "TO-252"),
        entry("MP", "SOT-223")
    );

    public static String resolve(String code) {
        return STANDARD.getOrDefault(code.toUpperCase(), code);
    }
}
```

---

## Test Coverage Gaps

| Area | Files | Tests | Gap |
|------|-------|-------|-----|
| Handlers | 50+ | 0 | No handler unit tests |
| Similarity Calculators | 20+ | 0 | No calculator tests |
| PatternRegistry | 1 | 0 | No registry tests |
| ManufacturerHandlerFactory | 1 | 0 | No factory tests |

**Priority tests to add**:

1. **Handler tests** - For each handler:
   ```java
   @Test void shouldDetectComponentType()
   @Test void shouldExtractPackageCode()
   @Test void shouldExtractSeries()
   @Test void shouldIdentifyReplacements()
   ```

2. **Pattern conflict tests**:
   ```java
   @Test void noTwoHandlersShouldClaimSameMPN()
   ```

3. **Similarity calculator tests**:
   ```java
   @Test void resistorsSameValueShouldBeHighSimilarity()
   @Test void differentComponentTypesShouldBeLowSimilarity()
   ```

---

## Refactoring Priorities

### Priority 1: Critical (Do First)

1. **Deduplicate TIHandler COMPONENT_SERIES**
   - Remove duplicate entries for LM358, LM7805, TL072, etc.
   - Keep only the most specific pattern
   - Files: `TIHandler.java` lines 99-611

2. **Integrate TIHandlerPatterns**
   - Refactor TIHandler to use TIHandlerPatterns
   - Remove inline COMPONENT_SERIES definitions
   - Files: `TIHandler.java`, `TIHandlerPatterns.java`

### Priority 2: High (Structural Improvements)

3. **Create AbstractManufacturerHandler**
   - Extract common package/series extraction
   - Move helper methods
   - New file: `AbstractManufacturerHandler.java`

4. **Create PackageCodeRegistry**
   - Centralize package code mappings
   - New file: `PackageCodeRegistry.java`

5. **Extract ComponentSeriesDefinition**
   - Shared series info container
   - New file: `ComponentSeriesDefinition.java`

### Priority 3: Medium (Quality Improvements)

6. **Add Handler Unit Tests**
   - Test each handler's pattern matching
   - New files: `*HandlerTest.java`

7. **Fix ComponentType.getManufacturer()**
   - Add explicit mapping or deprecate
   - File: `ComponentType.java`

8. **Add Similarity Calculator Tests**
   - Test each calculator
   - New files: `*SimilarityCalculatorTest.java`

### Priority 4: Low (Enhancements)

9. **Refactor ManufacturerHandlerFactory**
   - Replace reflection with explicit registration
   - File: `ManufacturerHandlerFactory.java`

10. **Standardize Pattern Approaches**
    - Consistent regex style across handlers
    - All handlers in `manufacturers/`

---

## Code Smell Indicators

When reviewing code, watch for:

| Smell | Example | Fix |
|-------|---------|-----|
| Duplicate Map.put() | Same key added twice | Remove duplicate |
| Hardcoded package codes | `return "TO-220"` | Use PackageCodeRegistry |
| Copy-pasted regex | Same pattern in 3 handlers | Extract to constant |
| Giant switch statement | 50+ cases | Use Map lookup |
| Unused ComponentSeriesInfo | Defined but never queried | Remove or use |

---

## File Reference Quick Guide

| Task | Primary Files |
|------|---------------|
| Add manufacturer | `ComponentManufacturer.java`, new `*Handler.java` |
| Add component type | `ComponentType.java`, handler `initializePatterns()` |
| Fix pattern matching | Handler's `matches()` method |
| Add similarity logic | New `*SimilarityCalculator.java`, register in `MPNUtils` |
| Debug MPN detection | `ComponentManufacturer.fromMPN()`, handler patterns |

---

## Learnings & Quirks

### Architecture Decisions
- `ComponentManufacturer` enum tightly couples manufacturer identity with handler - this is intentional for performance (single lookup)
- `PatternRegistry` supports multi-handler per type but this feature is largely unused
- Similarity calculators are registered in `MPNUtils` static initializer (lines 34-48)

### Known Gotchas
- Handler order in `ComponentManufacturer` affects detection priority for ambiguous MPNs
- Some MPNs legitimately match multiple manufacturers (second-source parts)
- `TIHandlerPatterns.java` exists but isn't integrated - don't add more patterns there until refactored

### Historical Context
- Recent commits show "debugging CI test failures" - test stability may need attention
- Some handlers have commented-out patterns (e.g., `ComponentManufacturer.java` lines 45-53) - unclear if deprecated or WIP

<!-- Add new learnings above this line -->
