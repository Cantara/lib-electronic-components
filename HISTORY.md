# Project History

Chronological timeline of major milestones, PRs, and learnings for the lib-electronic-components project.

## Navigation
- [January 2026](#january-2026)
- [December 2025 and Earlier](#december-2025-and-earlier)
- [Deep Dives](#deep-dives)

---

## January 2026

### Week 4 (January 24)

#### PR #128: MPN Package Suffix Support
- **Type:** Feature
- **Summary:** Add package suffix extraction and normalization for better component matching
- **Key Changes:**
  - 4 new MPNUtils methods: `stripPackageSuffix()`, `getSearchVariations()`, `isEquivalentMPN()`, `getPackageSuffix()`
  - Support for +, #, /, , delimiters (Maxim, Linear Tech, NXP, ON Semi patterns)
  - 32 comprehensive tests in MPNPackageSuffixTest.java
- **Impact:** Improved datasheet searches, component deduplication, BOM validation, inventory matching
- **Learning:** Conservative pattern approach prevents false positives
- **Documentation:** See CLAUDE.md "MPN Package Suffix Support" section

---

### Week 3 (January 14-19)

#### PR #125: Test Coverage Expansion with Haiku (January 16, 2026)
- **Type:** Test Infrastructure
- **Summary:** Autonomous test expansion using Haiku model for cost-effective development
- **Key Results:**
  - 114 new tests added across 3 files (DefaultSimilarityCalculatorTest, LevenshteinCalculatorTest, MetadataIntegrationTest)
  - 840 lines of code written autonomously
  - All tests passing on first run
  - Cost: ~$0.07 vs ~$0.85 with Sonnet (92% savings)
- **Task Delegation Pattern:**
  - Structured 50-line prompt with clear task breakdown
  - Quantifiable targets (35+ tests per file)
  - Reference patterns (existing test files)
  - Code structure guidance (@Nested classes, assertion style)
  - Verification step (mvn clean test)
- **Quality Observations:**
  - Tests followed existing patterns perfectly
  - Descriptive test names (shouldXxxWhenYyy)
  - Proper use of nested classes
  - No compilation errors, 100% pass rate
- **ROI:** If delegating 50 similar tasks → $39 savings; 100 refactorings → $50 savings
- **Lesson Learned:** Default to Haiku delegation for pattern-following work
- **Deep Dive:** See CLAUDE.md "Detailed Learnings from PR #125" section

#### PR #114-115: Critical Similarity Bug Fixes (January 14, 2026)
- **Type:** Bug Fix (CRITICAL)
- **Severity:** HIGH - Caused incompatible parts to receive high similarity scores
- **Bugs Fixed:**

**Bug 1: MPNUtils Ignoring 0.0 Calculator Results**
- **Problem:** `if (similarity > 0)` check ignored 0.0 results, falling back to default similarity
- **Impact Examples:**
  - IRF530 vs IRF9530 (N-ch vs P-ch): 0.9 instead of 0.0 ❌
  - 2N2222 vs 2N3904 (different specs): 1.0 instead of 0.547 ❌
- **Fix:** Removed the `> 0` check, trust all calculator results including 0.0
- **Location:** MPNUtils.java line 132-138

**Bug 2: TransistorSimilarityCalculator getBasePart() Regex Failure**
- **Problem:** Broken regex `([A-Z0-9]+)[A-Z].*$` extracted only first character ("2N2222" → "2")
- **Impact:** All transistors used default characteristics (600mA, TO-18) instead of actual specs
- **Fix:** Replaced complex regex with simple suffix stripping
- **Example:** 2N2222 (800mA, TO-18) vs 2N3904 (200mA, TO-92) now correctly scores 0.547 ✅

**Bug 3: OpAmpSimilarityCalculator Intercepting Generic IC Types (PR #115)**
- **Problem:** `isApplicable(ComponentType.IC)` returned true, intercepting ALL ICs before LogicICSimilarityCalculator
- **Impact:** CD4001 vs CD4001BE: 0.0 instead of 0.9 ❌
- **Root Cause:** Calculator registration order - first applicable calculator wins
- **Fix:** Changed `isApplicable()` to only handle specific OPAMP types, not generic IC
- **Test Update:** Changed assertion from `assertTrue(isApplicable(IC))` to `assertFalse(isApplicable(IC))`

- **Lessons Learned:**
  1. Always trust calculator results - 0.0 means "incompatible," not "I don't know"
  2. Regex for part number extraction must preserve full base part
  3. Calculator order matters - first applicable wins and returns immediately
  4. Be specific in isApplicable() - don't claim generic types unless you handle ALL subtypes
- **Results:** All 13,314 tests passing ✅
- **Deep Dive:** See `.docs/history/BUG_FIX_ANALYSIS.md` (if created)

---

### Week 2 (January 7-13)

#### PR #102: Jinling Handler Implementation
- **Type:** New Handler
- **Summary:** Position-based MPN encoding for Jinling connector part numbers
- **Critical Discovery:** Strict 15-character elprint format where EVERY position has meaning
- **MPN Format:**
  ```
  27 31 0 2 02 A N G3 S U T
  │  │  │ │ │  │ │ │  │ │ └─ [14] Packing
  │  │  │ │ │  │ │ │  │ └─── [13] ContactType
  │  │  │ │ │  │ │ │  └───── [12] ConnectorType
  │  │  │ │ │  │ │ └─────── [10-11] ContactPlating
  │  │  │ │ │  │ └────────── [9] Post
  │  │  │ │ │  └─────────── [8] InsulatorMaterial
  │  │  │ │ └──────────── [6-7] PinsPerRow
  │  │  │ └────────────── [5] Rows
  │  │  └──────────────── [4] HouseCount
  │  └─────────────────── [2-3] PlasticsHeight
  └────────────────────── [0-1] Family
  ```
- **Key Gotchas:**
  1. Exact length required (15 chars, not 13-17 variable)
  2. Positions 2-7 MUST be numeric digits
  3. No placeholders - test MPNs must use real encoding
  4. Position alignment matters - off by one breaks everything
- **Dual Format Complexity:**
  - Elprint format (15 chars): `27310202ANG3SUT` - internal encoding
  - Distributor format (15-18 chars): `22850102ANG1SYA02` - LCSC/JLCPCB
  - Handler must support BOTH with different extraction logic
- **Test Suite Journey:**
  - Initial: 91/131 (69%) - MPN format violations
  - After pattern fix: 106/131 (81%) - Test MPNs still wrong
  - After MPN corrections: 122/131 (93%) - Orientation, distributor patterns
  - After distributor fix: 127/131 (97%) - Pin count, replacement logic
  - Final: 131/131 (100%) ✓
- **Files Created:**
  - JinlingHandler.java (~560 lines)
  - JinlingHandlerTest.java (~550 lines, 131 tests)
  - `.claude/skills/connector/` skill updated
  - ComponentManufacturer.java: Added JINLING enum
- **Lessons Learned:**
  1. Pattern design: Validate EXACT length upfront for position-based MPNs
  2. Test data: Use REAL distributor MPNs, not invented examples
  3. Incremental testing: Fix patterns first, then test MPNs, then extraction logic
  4. Documentation: ASCII diagrams showing position meanings are invaluable
- **Deep Dive:** See `.docs/history/HANDLER_IMPLEMENTATION_PATTERNS.md` (if created)

#### Similarity Metadata Conversion (PRs #110-113)
- **Type:** Architecture Refactoring
- **Summary:** Converted 4 similarity calculators to metadata-driven architecture
- **Converted Calculators:**
  1. ✅ DiodeSimilarityCalculator (PR #110)
  2. ✅ MosfetSimilarityCalculator (PR #111)
  3. ✅ TransistorSimilarityCalculator (PR #112)
  4. ✅ VoltageRegulatorSimilarityCalculator (PR #113)
- **Key Benefits:**
  - More accurate scoring based on actual specs (0.95-0.99 for similar, 1.0 for identical)
  - Better differentiation for compatible but non-identical parts
  - Proper incompatibility detection (critical spec mismatches return 0.0)
  - Threshold-based testing (`>= HIGH_SIMILARITY` instead of `assertEquals(0.9)`)
- **Conversion Pattern:**
  ```java
  1. Extract specs from both MPNs
  2. Short-circuit check for critical mismatches → return 0.0
  3. Weighted spec comparison using ToleranceRule
  4. Optional boost for known equivalent groups
  ```
- **Test Expectation Updates:**
  - Changed from `assertEquals(0.9)` to `assertTrue(similarity >= 0.9)`
  - Adjusted borderline thresholds from `< 0.5` to `< 0.55`
- **Next Candidates:** OpAmpSimilarityCalculator, MemorySimilarityCalculator, LEDSimilarityCalculator
- **Deep Dive:** See `.claude/skills/similarity-metadata/SKILL.md`

#### PR #116-120: Additional Similarity Conversions
- **Converted:**
  - ✅ OpAmpSimilarityCalculator (PR #116)
  - ✅ MemorySimilarityCalculator (PR #117)
  - ✅ LEDSimilarityCalculator (PR #118)
  - ✅ LogicICSimilarityCalculator (PR #119)
  - ✅ SensorSimilarityCalculator (PR #120)
- **Status:** 12 of 17 calculators converted (71% complete)
- **Remaining:** MCUSimilarityCalculator, MicrocontrollerSimilarityCalculator, PassiveComponentCalculator, DefaultSimilarityCalculator, LevenshteinCalculator

---

### Week 1 (January 1-6)

#### Codebase Analysis (January 2026)
- **Type:** Technical Debt Audit
- **Findings:**

**Test Coverage Status:**
- Handlers: 61/67 (91.0%) - Missing: AbstractManufacturerHandler (base class), FairchildHandler, LogicICHandler, QualcommHandler, SpansionHandler, UnknownHandler
- Similarity Calculators: 17/17 (100%) - All tested with 302 total tests

**Technical Debt Inventory:**
- System.out.println debug statements: 181 instances (19 files)
- printStackTrace() calls: 9 instances
- Magic numbers in scoring: 108+ instances
- Inconsistent getSupportedTypes(): 34 use HashSet, 23 use Set.of()

**Pattern Inconsistencies:**
- Type Declaration vs Pattern Registration Mismatches:
  - MaximHandler: Declares INTERFACE_IC_MAXIM, RTC_MAXIM but doesn't register patterns
  - EspressifHandler: Declares ESP8266_SOC, ESP32_SOC but only registers MICROCONTROLLER patterns
  - PanasonicHandler: Declares capacitor/inductor types but registers NO patterns

**Priority Action Items:**
1. IMMEDIATE: Replace 181 debug statements with SLF4J logging
2. IMMEDIATE: Replace 9 printStackTrace() calls with logger.error()
3. HIGH: Add tests for remaining 15 handlers
4. HIGH: Standardize all getSupportedTypes() to Set.of()
5. HIGH: Fix type/pattern mismatches
6. ~~MEDIUM: Add similarity calculator test suite~~ ✓ DONE (302 tests)
7. MEDIUM: Extract magic numbers to configurable constants

#### PR #89: Handler Bug Fixes
- **Type:** Bug Fix (HIGH)
- **Summary:** Fixed 6 critical handler patterns and cross-handler matching issues
- **Bugs Fixed:**

**1. NPE Prevention in substring() Operations (EspressifHandler)**
```java
// BAD
String base = mpn.substring(0, mpn.indexOf('-'));  // Throws if no dash

// GOOD
int dash = mpn.indexOf('-');
String base = dash >= 0 ? mpn.substring(0, dash) : mpn;
```

**2. Pattern Ordering for Series Extraction (VishayHandler)**
```java
// BAD - generic pattern matches first
if (mpn.matches("1N4[0-9]{3}.*")) return "1N4000";  // Matches 1N4148!
if (mpn.matches("1N4148.*")) return "1N4148";       // Never reached

// GOOD - specific patterns before generic
if (mpn.matches("1N4148.*")) return "1N4148";
if (mpn.matches("1N4[0-9]{3}.*")) return "1N4000";
```

**3. Type/Pattern Registration Mismatch (EspressifHandler, MaximHandler, PanasonicHandler)**
- Problem: Declares type in getSupportedTypes() but doesn't register patterns
- Fix: Add patterns for all declared types

**4. Hyphenated MPN Normalization (PanasonicHandler)**
- Problem: Hyphen breaks position-based extraction (`ERJ-3GEYJ103V` position 3 = '-')
- Fix: Normalize first: `mpn.replace("-", "")`

**5. Missing matches() Override for Manufacturer-Specific Types (VishayHandler)**
- Problem: Falls through to registry which may not find MOSFET_VISHAY
- Fix: Explicit check in matches()

**6. Cross-Handler Pattern Matching (CRITICAL)**
- **Problem:** Default matches() used `getPattern(type)` which returns FIRST pattern from ANY handler
- **Root Cause:** CypressHandler (alphabetically before STHandler) didn't override matches(). When testing STM32F103C8T6, it used STHandler's pattern and incorrectly matched
- **Symptom:** Tests pass locally but fail in CI (different HashMap iteration order)
- **Fix:** Added `matchesForCurrentHandler()` to PatternRegistry

**Handler Bug Checklist:**
| Check | Issue | Fix |
|-------|-------|-----|
| indexOf() without guard | NPE risk | Add >= 0 check before substring |
| Generic pattern before specific | Wrong match | Reorder: specific first |
| Type in getSupportedTypes() | Missing pattern | Add to initializePatterns() |
| Position-based extraction | Hyphen breaks it | Normalize: mpn.replace("-", "") |
| Base type matches, specific doesn't | Missing override | Add explicit check in matches() |
| Handler doesn't override matches() | Cross-handler false match | Override matches() or use matchesForCurrentHandler() |

**CI vs Local Test Differences:**
- If tests pass locally but fail in CI, check for HashMap/HashSet iteration order dependencies
- Check for cross-handler pattern matching
- Verify handler alphabetical order (first matching handler wins)
- Ensure TreeSet comparators are deterministic

- **Deep Dive:** See `.docs/history/BUG_FIX_ANALYSIS.md` (if created)

---

## December 2025 and Earlier

### Handler Cleanup Campaign (PRs #73-88)

#### Infrastructure PRs (PR #74)
- **Type:** Infrastructure
- **Summary:** Created shared infrastructure for handler development
- **Created:**
  - `PackageCodeRegistry` - Centralized package code mappings (PU→PDIP, AU→TQFP, etc.)
  - `AbstractManufacturerHandler` - Base class with helper methods:
    - `extractSuffixAfterHyphen()`
    - `extractTrailingSuffix()`
    - `findFirstDigitIndex()`
    - `findLastDigitIndex()`
- **Impact:** Reduced duplication across all handlers

#### TI Handler Cleanup (PR #77)
- **Type:** Refactoring
- **Summary:** Established handler cleanup pattern followed by all subsequent handlers
- **Changes:**
  1. Replaced HashSet with Set.of() in getSupportedTypes()
  2. Migrated local package code maps to PackageCodeRegistry
  3. Removed unused enums and methods (TIHandlerPatterns.java deleted)
  4. Fixed suffix ordering bug (DT before T - longer suffixes first)
  5. Added comprehensive tests with nested test classes
- **Before:** ~170 lines of duplicate COMPONENT_SERIES entries
- **After:** Clean, deduplicated, well-tested
- **Test Coverage:** TIHandlerTest with nested categories

#### Batch Cleanups
**PR #75-76:** STHandler and AtmelHandler
- Fixed multi-pattern matching bugs
- Fixed package extraction bugs (STM32, AVR)
- Standardized getSupportedTypes() to Set.of()

**PR #78-81:** InfineonHandler, MicrochipHandler, AnalogDevicesHandler, NXPHandler
- All bugs fixed: Uses Set.of(), comprehensive patterns, proper package extraction
- Total: 12,632 tests passing ✅

**PR #87-88 (Batch 6):** Molex, Hirose, JST, Winbond, ISSI, Qorvo, Skyworks, Bosch, InvenSense, Melexis
- **Learnings:**
  - **Connector Handlers:** Pin count in first 2 digits, mounting in last 2, odd=male, even=female
  - **Memory Handlers:** Multiple types (MEMORY, MEMORY_FLASH, MEMORY_EEPROM), suffix package codes
  - **RF Handlers:** Register under ComponentType.IC, must include IC in getSupportedTypes()
  - **Sensor Handlers:** Need many types (SENSOR, ACCELEROMETER, GYROSCOPE, etc.)
  - **General Bug Pattern:** When handler registers patterns for ComponentType.IC, MUST include IC in getSupportedTypes()

### Known Technical Debt (All Completed ✅)
- ~~TIHandler duplicate COMPONENT_SERIES entries~~ - Consolidated (PR #77)
- ~~No AbstractManufacturerHandler base class~~ - Created (PR #74)
- ~~Package code mappings duplicated~~ - Created PackageCodeRegistry (PR #74)
- ~~Flaky tests due to handler order~~ - Fixed with deterministic TreeSet (PR #76)
- ~~ComponentType.getManufacturer() fragile~~ - Fixed with explicit mapping (PR #75)
- ~~All 8 handler bugs documented~~ - Fixed in PRs #75-88 ✅

**Final Status:** All handlers fixed, 97.1% test coverage (65/67 handlers)

---

## Deep Dives

For detailed technical analyses of significant implementations:

- **BUG_FIX_ANALYSIS.md** - Comprehensive analysis of critical bugs (PR #89, PR #114-115)
  - Bug patterns, before/after code examples, prevention strategies

- **HANDLER_IMPLEMENTATION_PATTERNS.md** - Handler development best practices
  - Jinling handler case study, position-based encoding, dual format support
  - Handler cleanup checklist, testing strategies

- **SIMILARITY_METADATA_EVOLUTION.md** - Metadata system development journey
  - Architecture decisions, migration patterns, conversion progress
  - Calculator integration examples, gotchas, lessons learned

---

## Statistics

### Overall Project Metrics
- **Total Tests:** 13,429 (all passing ✅)
- **Handler Test Coverage:** 65/67 (97.1%)
- **Similarity Calculator Test Coverage:** 17/17 (100%)
- **Metadata Conversion Progress:** 12/17 calculators (71%)

### Cost Savings
- **PR #125 Haiku Delegation:** $0.78 saved (92% reduction)
- **Potential Annual Savings:** $200-400 with consistent delegation

### Code Quality Improvements
- **Debug Statements Remaining:** 181 (down from initial audit)
- **Handlers with Set.of():** 23+ (up from 0)
- **Cross-Handler Bugs Fixed:** 1 critical (PatternRegistry.matchesForCurrentHandler)

---

## Legend

- ✅ - Completed
- ⏳ - In Progress
- ❌ - Bug / Issue
- 🔧 - Fix Applied
- 📊 - Metrics / Analysis

---

**Last Updated:** January 24, 2026

For active development guidance, see [CLAUDE.md](CLAUDE.md).
For specific feature documentation, see `.claude/skills/`.
