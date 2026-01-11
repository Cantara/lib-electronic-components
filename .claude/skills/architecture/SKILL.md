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

### RESOLVED ISSUES (PR #74, #75)

The following issues have been fixed:

1. **TIHandler Pattern Duplication** - FIXED
   - Removed ~170 lines of duplicate COMPONENT_SERIES entries
   - Fixed LM35/LM358 pattern conflict (LM35 sensors use letter suffix A-D)

2. **No Base Handler Class** - FIXED
   - Created `AbstractManufacturerHandler` with shared helper methods
   - Methods: `extractSuffixAfterHyphen()`, `extractTrailingSuffix()`, `findFirstDigitIndex()`, `findLastDigitIndex()`

3. **Package Code Duplication** - FIXED
   - Created `PackageCodeRegistry` with centralized mappings
   - Includes standard codes (N→DIP, D→SOIC) and Atmel-specific (PU→PDIP, AU→TQFP)

4. **Flaky Tests** - FIXED
   - Changed `ManufacturerHandlerFactory` from `HashSet` to `TreeSet` with deterministic ordering
   - Handler iteration order is now consistent across all test runs

---

## Remaining Issues

### 1. ComponentType.getManufacturer() Broken (MEDIUM)

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

## Package Code Registry (IMPLEMENTED)

The `PackageCodeRegistry` class has been created to centralize package code mappings:

```java
// Usage in handlers:
String resolvedCode = PackageCodeRegistry.resolve("PU");  // Returns "PDIP"
boolean isKnown = PackageCodeRegistry.isKnownCode("N");   // Returns true
boolean isPower = PackageCodeRegistry.isPowerPackage("TO-220"); // Returns true
```

**Supported codes include**:
- Standard: N→DIP, D→SOIC, PW→TSSOP, DGK→MSOP, DBV→SOT-23
- Power: T→TO-220, KC→TO-252, MP→SOT-223
- Atmel-specific: PU→PDIP, AU→TQFP, MU→QFN, SU→SOIC, XU→TSSOP

**Next step**: Migrate existing handlers to use the registry instead of inline maps.

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

### COMPLETED (PR #74, #75)

- ~~Deduplicate TIHandler COMPONENT_SERIES~~ - Done, removed ~170 lines
- ~~Create AbstractManufacturerHandler~~ - Done
- ~~Create PackageCodeRegistry~~ - Done
- ~~Fix flaky tests (handler ordering)~~ - Done, uses TreeSet now

### Priority 1: High (Structural Improvements)

1. **Migrate handlers to use AbstractManufacturerHandler**
   - Many handlers still use duplicate helper methods
   - Files: `*Handler.java` in `manufacturers/`

2. **Migrate handlers to use PackageCodeRegistry**
   - Replace inline PACKAGE_CODES maps with registry calls
   - Files: `*Handler.java` in `manufacturers/`

3. **Delete TIHandlerPatterns.java**
   - Now unused after TIHandler consolidation
   - File: `manufacturers/TIHandlerPatterns.java`

### Priority 2: Medium (Quality Improvements)

4. **Add Handler Unit Tests**
   - Test each handler's pattern matching
   - New files: `*HandlerTest.java`

5. **Fix ComponentType.getManufacturer()**
   - Add explicit mapping or deprecate
   - File: `ComponentType.java`

6. **Add Similarity Calculator Tests**
   - Test each calculator
   - New files: `*SimilarityCalculatorTest.java`

### Priority 3: Low (Enhancements)

7. **Standardize Pattern Approaches**
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

### Critical Implementation Details

**Handler Ordering (PR #75)**:
- `ManufacturerHandlerFactory` MUST use `TreeSet` with deterministic comparator
- `HashSet` caused flaky tests because iteration order varied between runs
- First matching handler wins in `getManufacturerHandler()` - order is critical!

**Type Detection Specificity (PR #74)**:
- `MPNUtils.getComponentType()` uses specificity scoring via `getTypeSpecificityScore()`
- Manufacturer-specific types (OPAMP_TI) score +150, generic types (IC) score -50
- Without scoring, iteration order could return IC instead of OPAMP_TI

**ComponentType.getBaseType() Completeness**:
- All manufacturer-specific types MUST be in the switch statement
- Missing types fall through to `default -> this` (returns self, not base type)
- Fixed in PR #74: Added TRANSISTOR_VISHAY, TRANSISTOR_NXP, OPAMP_ON, OPAMP_NXP, OPAMP_ROHM

### Known Gotchas
- Handler order in `ComponentManufacturer` affects detection priority for ambiguous MPNs
- Some MPNs legitimately match multiple manufacturers (second-source parts)
- `TIHandlerPatterns.java` exists but is unused - can be safely deleted

### Historical Context
- CI test failures (pre-PR #75) were caused by non-deterministic HashSet iteration
- Test stability now achieved via TreeSet with class name comparator
- Some handlers have commented-out patterns (e.g., `ComponentManufacturer.java` lines 45-53) - unclear if deprecated or WIP

<!-- Add new learnings above this line -->
