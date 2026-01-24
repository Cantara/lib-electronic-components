# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build the project
mvn clean install

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=MPNUtilsTest

# Run a single test method
mvn test -Dtest=MPNUtilsTest#testNormalize

# Skip tests during build
mvn install -DskipTests

# Package without running tests
mvn package -DskipTests
```

## Git Workflow

**Always use Pull Requests** - never commit directly to main.

**🚨 CRITICAL: Update documentation BEFORE committing!**

```bash
# 0. Update documentation FIRST (use checklist)
# Run: /documentation-maintenance
# - Update relevant skill files with learnings
# - Add examples for new patterns
# - Update CLAUDE.md for cross-cutting changes
# - Update HISTORY.md for significant features

# 1. Create feature branch
git checkout -b feature/short-description

# 2. Make changes and commit
git add <files>
git commit -m "feat: description"

# 3. Push and create PR
git push -u origin feature/short-description
gh pr create --title "feat: description" --body "## Summary\n..."
```

## Cost-Effective Task Delegation

**IMPORTANT:** Use the Task tool with cheaper models (Haiku) for straightforward work to minimize costs and reserve Sonnet for complex reasoning.

### Model Cost Comparison (as of January 2026)

| Model | Input ($/MTok) | Output ($/MTok) | Speed | Best For |
|-------|----------------|-----------------|-------|----------|
| Haiku | $0.25 | $1.25 | Fast | Simple tasks, test fixes, documentation |
| Sonnet 4.5 | $3.00 | $15.00 | Thorough | Complex reasoning, architecture, ambiguous problems |

**Cost difference: Haiku is 12x cheaper than Sonnet 4.5**

### When to Delegate to Haiku

**Always delegate to Haiku for:**
- Test fixes (updating assertions, adding test cases)
- Simple refactoring (renaming, extracting methods)
- Documentation updates (README, CLAUDE.md, skills)
- Straightforward bug fixes with clear root cause
- Adding logging or error messages
- Code formatting and style fixes
- Simple pattern application (e.g., converting getSupportedTypes() from HashSet to Set.of())

**Example delegation:**
```javascript
Task(
  subagent_type="general-purpose",
  model="haiku",
  prompt="Fix the 2 failing tests in ResistorSimilarityCalculatorTest.java.
  Update assertions to use >= HIGH_SIMILARITY (0.9) instead of assertEquals(0.9, ...).
  Run the tests to verify they pass."
)
```

### When NOT to Delegate (Use Sonnet Directly)

**Use Sonnet directly for:**
- Architectural decisions (new handler design, metadata system changes)
- Complex debugging (circular initialization, flaky tests, cross-handler issues)
- Ambiguous requirements (need to explore codebase first)
- Multi-step planning requiring coordination
- Performance optimization requiring deep analysis
- Security-sensitive changes (input validation, injection prevention)
- **Creating new handlers or similarity calculators** (requires understanding patterns)

### Successful Haiku Delegations in This Project

**PR #125: Test Coverage Expansion** ✅
- 114 new tests added across 3 files
- 840 lines of code written autonomously
- All tests passing on first run
- Cost: ~$0.07 vs ~$0.85 with Sonnet (92% savings)

**Tasks completed:**
- DefaultSimilarityCalculatorTest: +11 edge case tests
- LevenshteinCalculatorTest: +10 advanced substitution tests
- MetadataIntegrationTest: Created new file with 43 integration tests

### Cost Savings Impact

**Single task example (PR #125):**
- Haiku cost: ~$0.07 (840 lines)
- Sonnet cost: ~$0.85 (same work)
- **Savings: $0.78 per task (92% reduction)**

**Project-wide potential:**
- 100 test expansion tasks: **$78 savings**
- 50 simple refactorings: **$39 savings**
- Total annual savings: **$200-400**

### Best Practices

1. **Default to delegation** - When in doubt about complexity, try Haiku first
2. **Clear success criteria** - Specify what "done" looks like (e.g., "all tests pass")
3. **Single responsibility** - One focused task per delegation
4. **Include verification** - Always add "run tests and verify" to the prompt
5. **Escalate if stuck** - If Haiku can't solve it, that signals it needs Sonnet

### Red Flags for Delegation

Don't delegate if the task involves:
- "Investigate why..." (requires exploration)
- "Design an approach for..." (requires architectural thinking)
- Multiple handlers or calculators (cross-cutting changes)
- Unclear requirements ("make it better")
- First-time patterns (no existing examples to follow)

### Known Limitations

**Resource limits:**
- Haiku may hit rate limits during high concurrency
- Fallback: Complete work directly with Sonnet if delegation fails
- Document attempts in PR description for tracking


## Project Overview

This is a Java 21 library for working with electronic components in software systems. It provides functionality for:

- **MPN (Manufacturer Part Number) operations**: Normalization, type detection, similarity calculation, manufacturer identification
- **BOM (Bill of Materials) management**: PCBA and mechanical assemblies, production batch planning
- **Component classification**: Passive/active categorization, manufacturer-specific type detection
- **Similarity analysis**: Finding equivalent components across manufacturers

## Architecture

### Core Domain Model (`no.cantara.electronic.component`)

- `ElectronicPart` - Base class for electronic components with MPN, value, package, and specs
- `BOMEntry` - Component entry in a bill of materials (extends ElectronicPart)
- `PCBABOM` / `MechanicalBOM` - Assemblies for PCB and mechanical components
- `PlannedProductionBatch` (in `advanced/`) - Container for complete production systems

### Component Library (`no.cantara.electronic.component.lib`)

- `MPNUtils` - Static utility methods for MPN normalization, similarity, and type matching
- `ComponentType` - Enum with ~200 component types including manufacturer-specific variants (e.g., `MOSFET_INFINEON`, `CAPACITOR_CERAMIC_MURATA`)
- `ComponentManufacturer` - Enum of manufacturers with regex patterns and handler references
- `ComponentTypeDetector` - Determines component type from MPN string
- `ManufacturerHandler` - Interface for manufacturer-specific pattern matching and extraction

### Manufacturer Handlers (`no.cantara.electronic.component.lib.manufacturers`)

Each major manufacturer (TI, ST, Microchip, Murata, etc.) has a handler implementing `ManufacturerHandler`:
- Initializes regex patterns for component types via `PatternRegistry`
- Extracts package codes and series information from MPNs
- Defines supported component types for that manufacturer

### Similarity Calculators (`no.cantara.electronic.component.lib.componentsimilaritycalculators`)

Component-type-specific calculators (e.g., `ResistorSimilarityCalculator`, `MosfetSimilarityCalculator`) that compare MPNs based on electrical characteristics rather than just string matching.

## Key Patterns

### Adding a New Manufacturer
1. Create handler class in `manufacturers/` implementing `ManufacturerHandler`
2. Add enum entry to `ComponentManufacturer` with regex pattern and handler instance
3. Define component type patterns in `initializePatterns()`

### Component Type Hierarchy
`ComponentType` enum uses a base type pattern. Manufacturer-specific types like `MOSFET_INFINEON` map to base type `MOSFET` via `getBaseType()`.

### MPN Processing Flow
1. `ComponentManufacturer.fromMPN()` identifies manufacturer
2. Manufacturer's handler matches patterns to determine `ComponentType`
3. `MPNUtils.calculateSimilarity()` uses type-specific calculators for comparison

## Component Skills

Specialized skills are available in `.claude/skills/` for working with specific component types:
- `/component` - Base skill for general component work
- `/resistor`, `/capacitor`, `/inductor` - Passive components
- `/semiconductor` - Diodes, transistors, MOSFETs
- `/ic` - Microcontrollers, op-amps, voltage regulators
- `/connector` - Headers, sockets, wire-to-board
- `/memory` - Flash, EEPROM, SRAM
- `/lifecycle` - **Component lifecycle tracking** (obsolescence, NRFND, LTB, replacements)
- `/architecture` - **Refactoring and cleanup guidance** (critical issues, duplication hotspots)
- `/task-delegation` - **Cost-effective task delegation** (Haiku vs Sonnet, when to delegate, proven patterns)
- `/documentation-maintenance` - **🚨 Pre-PR documentation checklist** (MANDATORY before commits, ensures skills/learnings updated)

### Advanced Component Skills

Deep-dive skills for complex subsystems and architectural patterns:
- `/handler-pattern-design` - **Handler patterns and anti-patterns** (Set.of() vs HashSet, dual type registration, matches() override, testing strategies)
- `/mpn-normalization` - **MPN suffix handling and normalization** (stripPackageSuffix, getSearchVariations, Unicode µ→Μ gotcha)
- `/similarity-calculator-architecture` - **Calculator ordering and bugs** (OpAmp IC interception, first-applicable-wins, isApplicable() patterns)
- `/component-type-detection-hierarchy` - **Type hierarchy and detection** (~450 types, specificity levels, getBaseType() mapping)
- `/component-spec-extraction` - **Spec extraction patterns** (SpecValue wrapper, camelCase naming, type-specific extraction)
- `/metadata-driven-similarity-conversion` - **Metadata migration guide** (5-step conversion, dual-path implementation, weighted scoring)
- `/manufacturer-detection-from-mpn` - **Manufacturer regex patterns** (120+ manufacturers, detection ordering, shared prefixes)
- `/equivalent-group-identification` - **Equivalent part groups** (2N2222≈PN2222, IRF530≈STF530, hardcoded groups in 4 calculators)

## Similarity Calculator Skills

Skills for working with component similarity calculations in `.claude/skills/similarity*/`:
- `/similarity` - **Main skill**: Architecture, interfaces, calculator selection
- `/similarity-resistor` - Value, package, tolerance matching
- `/similarity-capacitor` - Capacitance, voltage, dielectric matching
- `/similarity-transistor` - NPN/PNP, equivalent groups (2N2222≈PN2222)
- `/similarity-mosfet` - N/P channel, IRF/IRFP families
- `/similarity-diode` - Signal/rectifier/zener, 1N400x equivalents
- `/similarity-opamp` - Single/dual/quad, LM358≈MC1458 families
- `/similarity-mcu` - Family/series/feature weighted comparison
- `/similarity-memory` - I2C/SPI EEPROM, Flash equivalents
- `/similarity-sensor` - Temperature, accelerometer, humidity families
- `/similarity-led` - Color bin, brightness bin, family matching
- `/similarity-connector` - Pin count, pitch, mounting type
- `/similarity-regulator` - 78xx/79xx fixed, LM317 adjustable
- `/similarity-logic` - 74xx/CD4000 series, LS/HC/HCT technologies

---

## Metadata-Driven Similarity Framework (January 2026)

The similarity system has been significantly enhanced with a **metadata-driven architecture** for configurable, type-specific similarity rules.

### Conversion Status

**12 of 17 calculators converted (71% complete)**

| Calculator | Status | PR | Specs | Critical Specs |
|-----------|--------|-----|-------|----------------|
| ResistorSimilarityCalculator | ✅ | - | resistance, package, tolerance | resistance |
| CapacitorSimilarityCalculator | ✅ | - | capacitance, voltage, dielectric, package | capacitance, voltage |
| TransistorSimilarityCalculator | ✅ | - | polarity, voltageRating, currentRating, hfe, package | polarity, voltageRating, currentRating |
| DiodeSimilarityCalculator | ✅ | - | type, voltageRating, currentRating, package | type, voltageRating, currentRating |
| MosfetSimilarityCalculator | ✅ | - | channel, voltageRating, currentRating, rdsOn, package | channel, voltageRating, currentRating |
| VoltageRegulatorSimilarityCalculator | ✅ | - | regulatorType, outputVoltage, polarity, currentRating, package | regulatorType, outputVoltage, polarity |
| OpAmpSimilarityCalculator | ✅ | #116 | configuration, family, package | configuration |
| MemorySimilarityCalculator | ✅ | #117 | memoryType, capacity, interface, package | memoryType, capacity |
| LEDSimilarityCalculator | ✅ | #118 | color, family, brightness, package | color |
| ConnectorSimilarityCalculator | ✅ | pre-existing | pinCount, pitch, family, mountingType | pinCount, pitch |
| LogicICSimilarityCalculator | ✅ | #119 | function, series, technology, package | function |
| SensorSimilarityCalculator | ✅ | #120 | sensorType, family, interface, package | sensorType |
| MCUSimilarityCalculator | ⏳ | - | family, series, features | - |
| MicrocontrollerSimilarityCalculator | ⏳ | - | manufacturer, series, package | - |
| PassiveComponentCalculator | ⏳ | - | value, sizeCode, tolerance | - |
| DefaultSimilarityCalculator | ⏳ | - | - | - |
| LevenshteinCalculator | ⏳ | - | - | - |

### Core Architecture Classes

```
ComponentTypeMetadataRegistry (Singleton)
├── ComponentTypeMetadata (per component type)
│   ├── SpecConfig (per spec: importance + tolerance rule)
│   │   ├── SpecImportance (enum: CRITICAL, HIGH, MEDIUM, LOW, OPTIONAL)
│   │   └── ToleranceRule (interface: exactMatch, percentage, range, etc.)
│   └── SimilarityProfile (context-aware: REPLACEMENT, DESIGN_PHASE, etc.)
└── SpecValue<T> (type-safe wrapper: value + unit)
```

### Implementation Pattern

Converted calculators follow this dual-path approach:

```java
@Override
public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
    if (mpn1 == null || mpn2 == null) return 0.0;

    // Try metadata-driven approach first
    Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.OPAMP);
    if (metadataOpt.isPresent()) {
        logger.trace("Using metadata-driven similarity calculation");
        return calculateMetadataDrivenSimilarity(mpn1, mpn2, metadataOpt.get());
    }

    // Fallback to legacy pattern-based approach
    logger.trace("No metadata found, using legacy approach");
    return calculateLegacySimilarity(mpn1, mpn2);
}

private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
    SimilarityProfile profile = metadata.getDefaultProfile();

    // Extract specs from MPNs
    String config1 = extractConfiguration(mpn1);
    String config2 = extractConfiguration(mpn2);
    // ... extract other specs

    // Short-circuit check for CRITICAL incompatibility
    if (!config1.isEmpty() && !config2.isEmpty() && !config1.equals(config2)) {
        logger.debug("CRITICAL spec mismatch - returning LOW_SIMILARITY");
        return LOW_SIMILARITY;
    }

    double totalScore = 0.0;
    double maxPossibleScore = 0.0;

    // Compare each spec with weighted scoring
    ComponentTypeMetadata.SpecConfig configSpec = metadata.getSpecConfig("configuration");
    if (configSpec != null && !config1.isEmpty() && !config2.isEmpty()) {
        ToleranceRule rule = configSpec.getToleranceRule();
        SpecValue<String> orig = new SpecValue<>(config1, SpecUnit.NONE);
        SpecValue<String> cand = new SpecValue<>(config2, SpecUnit.NONE);

        double specScore = rule.compare(orig, cand);
        double specWeight = profile.getEffectiveWeight(configSpec.getImportance());

        totalScore += specScore * specWeight;
        maxPossibleScore += specWeight;
    }

    // Repeat for other specs (family, package, etc.)
    // ...

    double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

    // Apply boosts for known equivalent groups
    if (areEquivalentParts(mpn1, mpn2)) {
        similarity = Math.max(similarity, HIGH_SIMILARITY);
    }

    return similarity;
}
```

### Key Features

**1. Spec Importance Levels**
- **CRITICAL** (1.0): Must match, short-circuit on mismatch
- **HIGH** (0.7): Primary characteristics
- **MEDIUM** (0.4): Secondary characteristics
- **LOW** (0.2): Minor details (package, suffix)
- **OPTIONAL** (0.0): Informational only

**2. Tolerance Rules**
- `exactMatch()` - Strings must be identical
- `percentageTolerance(double)` - Within percentage range
- `minimumRequired(double)` - Must meet minimum value
- `maximumAllowed(double)` - Must not exceed maximum
- `rangeTolerance(double, double)` - Within min/max range

**3. Context-Aware Profiles**
```java
| Profile | Threshold | CRITICAL | HIGH | MEDIUM | LOW | Use Case |
|---------|-----------|----------|------|--------|-----|----------|
| DESIGN_PHASE | 0.85 | 1.0 | 0.9 | 0.7 | 0.4 | Exact match for new designs |
| REPLACEMENT | 0.75 | 1.0 | 0.7 | 0.4 | 0.2 | Direct replacement (default) |
| COST_OPTIMIZATION | 0.60 | 1.0 | 0.4 | 0.2 | 0.0 | Maintain critical specs only |
| EMERGENCY_SOURCING | 0.50 | 0.8 | 0.4 | 0.2 | 0.0 | Urgent, relaxed requirements |
```

**4. Weighted Scoring Formula**
```java
similarity = totalScore / maxPossibleScore

where:
  totalScore = Σ(specScore × effectiveWeight)
  maxPossibleScore = Σ(effectiveWeight)
  effectiveWeight = profile.getEffectiveWeight(specImportance)
```

### Behavior Improvements

The metadata-driven approach provides more accurate similarity scores:

**OpAmp Example**:
- LM358 vs MC1458: 0.9 → **1.0** (exact configuration + family match)
- LM358 vs LM324: 0.7 → **0.3** (short-circuit on configuration: dual vs quad)

**Memory Example**:
- 24LC256 vs AT24C256: 0.7+ → **0.85** (equivalent I2C EEPROM)
- 24LC256 vs 24LC512: 0.3 → **0.3** (short-circuit on capacity)

**Sensor Example**:
- ADXL345 vs ADXL346: 0.3 → **0.703** (same type + interface = MEDIUM)
- DS18B20 vs DS18B20+: 0.9 → **1.0** (equivalent variants boost)

### Migration Guidelines

**For converting existing calculators:**
1. Add imports: `SimilarityProfile`, `ToleranceRule`, `SpecUnit`, `SpecValue`
2. Modify `calculateSimilarity()` to check for metadata first
3. Implement `calculateMetadataDrivenSimilarity()` method
4. Add spec extraction helper methods
5. Update tests to use threshold assertions (`>= HIGH_SIMILARITY`)
6. Verify backward compatibility with full test suite

**Benefits of conversion:**
- More precise similarity scores
- Configurable importance weights
- Context-aware thresholds
- Better documentation of comparison logic
- Easier to maintain and extend

**See `/similarity` skill for complete documentation.**

---

## Manufacturer Skills

Manufacturer-specific skills for complex MPN encoding patterns:
- `/manufacturers/ti` - **Texas Instruments** MPN decoding (LM, TL, TPS prefixes; package/temp suffixes)
- `/manufacturers/atmel` - **Atmel/Microchip AVR** MPN decoding (ATmega, ATtiny, SAM; -PU/-AU package codes)
- `/manufacturers/st` - **STMicroelectronics** MPN decoding (STM32, STM8, MOSFETs, regulators)
- `/manufacturers/nxp` - **NXP Semiconductors** MPN decoding (LPC, S32K, audio codecs, sensors)
- `/manufacturers/infineon` - **Infineon Technologies** MPN decoding (IRF, BSC, IFX MOSFETs; Cypress PSoC)
- `/manufacturers/microchip` - **Microchip Technology** MPN decoding (PIC, dsPIC, AVR, SAM; package suffixes)
- `/manufacturers/onsemi` - **ON Semiconductor** MPN decoding (FQP, NTD MOSFETs; 2N transistors; MC78xx regulators)
- `/manufacturers/analogdevices` - **Analog Devices** MPN decoding (AD8xxx op-amps, ADCs, DACs, ADXL accelerometers)
- `/manufacturers/maxim` - **Maxim Integrated** MPN decoding (MAX232, DS18B20, DS1307 RTC; temp+package suffixes)
- `/manufacturers/renesas` - **Renesas Electronics** MPN decoding (RL78, RX, RA, RH850 MCUs; R5F/R7F prefixes)
- `/manufacturers/toshiba` - **Toshiba Semiconductor** MPN decoding (TK MOSFETs, TLP optocouplers, TB motor drivers, 2SC transistors)

## Recording Learnings

When you discover quirks, edge cases, or important patterns while working on this codebase, record them:
1. **General learnings** → Add to this file under "Learnings & Quirks" section below
2. **Component-specific learnings** → Add to the relevant skill file in `.claude/skills/`

This ensures institutional knowledge is preserved for future sessions.

---

## Learnings & Quirks

### Pattern Matching
- Manufacturer detection order matters in `ComponentManufacturer.fromMPN()` - more specific patterns (like `DSPIC`, `STM32`) are checked before generic prefix matching
- Some manufacturers share part number prefixes (e.g., `LM` series used by TI, ST, ON Semi) - the code handles this with explicit manufacturer indicators in the MPN

### Handler Implementation
- Always register patterns for both the base type AND the manufacturer-specific type (e.g., both `RESISTOR` and `RESISTOR_CHIP_YAGEO`)
- The `matches()` method in handlers should include direct pattern checks for performance, falling back to `PatternRegistry` lookup

### Testing
- `MPNUtilsTest` and `MPNExtractionTest` are the primary tests for MPN parsing logic
- Many test MPNs are real-world part numbers from datasheets
- **Test coverage**: 65 handlers have tests (4960+ tests), 17 similarity calculators have tests (302 tests)
- **Handler test coverage**: 65/67 handlers (97.1%), missing: AbstractManufacturerHandler (base class), UnknownHandler (special case)
- **Flaky tests**: If tests pass individually but fail in suite, check for non-deterministic iteration (HashSet → TreeSet)

### Handler Test Gotchas (IMPORTANT)

**Classpath Shadowing**:
- NEVER put handler tests in the `manufacturers` package (e.g., `no.cantara.electronic.component.lib.manufacturers`)
- Test-classes directory shadows main-classes directory, causing `ManufacturerHandlerFactory` to find 0 handlers
- Solution: Put handler tests in a different package (e.g., `no.cantara.electronic.component.lib.handlers`)

**Circular Initialization (UPDATED)**:
- Direct instantiation (`new TIHandler()`) works in `@BeforeAll` when the test class doesn't trigger ComponentManufacturer loading first
- If you get `ExceptionInInitializerError`, it's due to circular deps: ComponentType ↔ ComponentManufacturer ↔ Handler
- **Recommended approach**: Use direct instantiation in `@BeforeAll` - simpler and avoids MPN lookup dependency
- **Alternative**: Use `MPNUtils.getManufacturerHandler("LM358")` if direct instantiation fails

**Test Isolation vs Full Suite**:
- Running a single handler test may fail while full suite passes
- This is due to class loading order - other tests initialize classes first
- If single test fails with initialization errors, run full suite to verify

**EspressifComponentType Latent Bug**:
- `EnumSet.allOf(EspressifComponentType.class)` can throw "class not an enum" in certain loading orders
- This is a circular initialization issue that only manifests in specific test scenarios

### Critical Implementation Details

**Handler Ordering (IMPORTANT)**:
- `ManufacturerHandlerFactory` MUST use `TreeSet` with deterministic comparator
- `HashSet` causes flaky tests because iteration order varies between runs
- First matching handler wins in `getManufacturerHandler()` - order matters!

**Type Detection Specificity**:
- `MPNUtils.getComponentType()` uses specificity scoring to return most specific type
- Manufacturer-specific types (OPAMP_TI) score higher than generic (IC, ANALOG_IC)
- Without scoring, HashSet iteration could return IC instead of OPAMP_TI

**ComponentType.getBaseType() Completeness**:
- All manufacturer-specific types MUST be in the switch statement
- Missing types fall through to `default -> this` (returns self, not base type)
- Check when adding new types: TRANSISTOR_*, OPAMP_*, MOSFET_*, etc.

### Handler Cleanup Checklist

When cleaning up a manufacturer handler, follow this pattern (established in PR #77):

1. **Replace `HashSet` with `Set.of()` in `getSupportedTypes()`** - Prevents duplicates, immutable
2. **Migrate local package code maps to `PackageCodeRegistry`** - Centralized, consistent
3. **Remove unused enums and methods** - Dead code cleanup
4. **Check suffix ordering** - Longer suffixes must be checked before shorter ones (e.g., "DT" before "T")
5. **Add comprehensive tests** - Use nested test classes by category (see TIHandlerTest)

### Known Technical Debt

**Fixed (PR #74, #75, #76, #77, #78, #80, #81, Pre-2026)**:
- ~~TIHandler duplicate COMPONENT_SERIES entries~~ - Consolidated, removed ~170 lines of duplicates
- ~~No AbstractManufacturerHandler base class~~ - Created with shared helper methods
- ~~Package code mappings duplicated~~ - Created `PackageCodeRegistry` with centralized mappings
- ~~Flaky tests due to handler order~~ - Fixed with deterministic TreeSet ordering
- ~~`ComponentType.getManufacturer()` fragile string matching~~ - Fixed with explicit suffix→enum mapping
- ~~Unused `TIHandlerPatterns.java`~~ - Deleted
- ~~TIHandler: HashSet in getSupportedTypes()~~ - Changed to Set.of()
- ~~TIHandler: suffix ordering bug (DT vs T)~~ - Longer suffixes checked first
- ~~AtmelHandler: multi-pattern matching bug~~ - Uses registry.matches() now
- ~~AtmelHandler: speed grade in package extraction~~ - Strips leading digits
- ~~AtmelHandler: AT25 pattern too restrictive~~ - Now allows 0-2 letters
- ~~STHandler: HashSet in getSupportedTypes()~~ - Changed to Set.of()
- ~~STHandler: package extraction bugs~~ - Fixed STM32 (returns LQFP not T6), MOSFET, regulators
- ~~STHandler: multi-pattern matching bug~~ - Added explicit type checks
- ~~AtmelHandler: cross-handler pattern matching~~ - Don't fall through for base MICROCONTROLLER type
- ~~InfineonHandler: All 5 bugs fixed~~ - Uses Set.of(), package extraction works, series extraction order fixed, has XMC/OptiMOS patterns
- ~~MicrochipHandler: All 5 bugs fixed~~ - Uses Set.of(), has AVR32 patterns, package extraction decodes properly
- ~~AnalogDevicesHandler: All 5 bugs fixed~~ - Uses Set.of(), has ADC/DAC patterns, package extraction comprehensive

**All Handler Bugs Fixed! (8 handlers, 12,632 tests passing)** ✅

All handlers documented with bugs have been fixed in previous work:

*NXPHandler* ✅ (115 tests passing):
- Uses Set.of()
- Has MX25 and S25FL memory patterns with MEMORY type
- Has MPX pressure sensor patterns
- Package extraction comprehensive (LPC, Kinetis, iMX, MOSFETs, transistors)
- Series extraction complete (audio codecs, sensors, all product lines)
- Uses matchesForCurrentHandler() to prevent cross-handler matching

*OnSemiHandler* ✅ (119 tests passing):
- Uses Set.of()
- Has NTD/FQP/FDP MOSFET patterns
- Has 2N/MMBT/MPSA transistor patterns
- Has NCP regulator patterns
- Package extraction handles diodes, MOSFETs, and transistors
- Series extraction complete for all product lines

*MaximHandler* ✅ (60 tests passing):
- Uses Set.of()
- Has comprehensive patterns for interface ICs, thermocouples, power management
- Package extraction comprehensive
- Series extraction complete

*RenesasHandler* ✅ (110 tests passing):
- Uses Set.of()
- Package extraction properly handles version suffixes
- Pattern overlaps resolved
- Version suffix handling correct

*ToshibaHandler* ✅ (116 tests passing):
- Uses Set.of()
- Has TRANSISTOR and IC types in getSupportedTypes()
- Package extraction complete for all series
- Series extraction complete
- Has 2SK and digital transistor patterns

**Medium**:
- Some handlers have commented-out patterns in `ComponentManufacturer.java` - unclear if deprecated
- `MPNUtils.getManufacturerHandler` relies on alphabetical handler order - could be fragile

**Low**:
- Test coverage: 61 handlers now have comprehensive tests (4497 total tests)
- Use existing handler tests as templates: TIHandlerTest, STHandlerTest, NXPHandlerTest, etc.

*BroadcomHandler*: Fixed - uses Set.of(), includes IC type
*SiliconLabsHandler*: Fixed - uses Set.of(), includes all MCU, IC, CRYSTAL, OSCILLATOR types
*AVXHandler*: Fixed - uses Set.of(); Note: Ceramic package extraction reads positions 2-3 after 08/12 prefix (design choice, not bug)
*TEHandler*: Fixed - uses Set.of()
*AmphenolHandler*: Fixed - uses Set.of()
*NichiconHandler*: Fixed - uses Set.of()
*WurthHandler*: Fixed - uses Set.of(), HEADER_PATTERN now matches both 61xxx and 62xxx
*MicronHandler*: Fixed - uses Set.of()

**Batch 6 Handlers (PR #87):**
*MolexHandler*: Fixed - uses Set.of()
*HiroseHandler*: Fixed - uses Set.of()
*JSTHandler*: Fixed - uses Set.of()
*WinbondHandler*: Fixed - uses Set.of()
*ISSIHandler*: Fixed - uses Set.of(), includes IC for LED drivers
*QorvoHandler*: Fixed - uses Set.of(), includes IC
*SkyworksHandler*: Fixed - uses Set.of(), includes IC and MICROCONTROLLER
*BoschHandler*: Fixed - uses Set.of()
*InvSenseHandler*: Fixed - uses Set.of(), includes IC for audio/motion processors
*MelexisHandler*: Fixed - uses Set.of()


### Architecture Notes
- `PatternRegistry` supports multi-handler per ComponentType but this is largely unused
- Similarity calculators registered in `MPNUtils` static initializer (lines 34-48)
- `ManufacturerHandlerFactory` uses reflection-based classpath scanning with TreeSet for deterministic order

### MPN Package Suffix Support (January 2026)

**Feature**: MPNUtils now supports stripping manufacturer-specific package suffixes from MPNs for better component matching and searching.

**Supported Patterns**:
| Pattern | Delimiter | Example | Manufacturers |
|---------|-----------|---------|---------------|
| Plus suffix | `+` | MAX3483EESA**+** → MAX3483EESA | Maxim, Analog Devices (lead-free) |
| Hash suffix | `#` | LTC2053HMS8**#PBF** → LTC2053HMS8 | Linear Technology (RoHS, Tape & Reel) |
| Slash suffix | `/` | TJA1050T**/CM,118** → TJA1050T | NXP (ordering codes) |
| Comma suffix | `,` | NC7WZ04**,315** → NC7WZ04 | Various (ordering codes) |

**API Methods**:
```java
// Strip package suffix to get base part
String base = MPNUtils.stripPackageSuffix("MAX3483EESA+");  // → "MAX3483EESA"

// Generate search variations for datasheet/component searches
List<String> vars = MPNUtils.getSearchVariations("LTC2053HMS8#PBF");  // → ["LTC2053HMS8#PBF", "LTC2053HMS8"]

// Check component equivalence (ignoring packaging)
boolean equiv = MPNUtils.isEquivalentMPN("LTC2053HMS8#PBF", "LTC2053HMS8#TR");  // → true

// Extract the package suffix
Optional<String> suffix = MPNUtils.getPackageSuffix("MAX3483EESA+");  // → Optional.of("+")
```

**Use Cases**:
- **Datasheet searches**: Try both "MAX3483EESA+" and "MAX3483EESA" for better search results
- **Component deduplication**: Recognize "LTC2053HMS8#PBF" and "LTC2053HMS8#TR" as same component
- **BOM validation**: Match supplier parts to design MPNs despite different packaging codes
- **Inventory matching**: Improve supplier part number matching accuracy

**Implementation Details**:
- **Conservative approach**: Only strips well-known patterns (+, #, /, ,) to avoid false positives
- **Single-letter suffixes NOT stripped**: "NC7WZ04G" remains unchanged (ambiguous - could be part of base MPN)
- **Pattern priority**: + → # → / → , (first matching delimiter wins)
- **Performance**: O(1) string operations, no regex overhead
- **Case-insensitive**: Equivalence checks ignore case differences

**Test Coverage**:
- 32 comprehensive tests in `MPNPackageSuffixTest.java`
- Real-world production MPNs tested (Maxim, Linear Tech, NXP, ON Semi)
- Integration tests for datasheet search, BOM validation, deduplication scenarios
- All 32 tests passing ✅

**Gotchas**:
1. **No manufacturer-specific logic**: Uses generic patterns, not ManufacturerHandler integration
2. **First delimiter wins**: "PART#ABC/XYZ" → "PART" (stops at #, not /)
3. **Conservative by design**: Ambiguous cases preserved to avoid breaking existing MPNs
4. **No multiple suffix support**: Designed for single suffix per MPN

**Future Enhancements** (if needed):
- Manufacturer-aware suffix detection via ManufacturerHandler
- Package code registry for more accurate detection
- Support for manufacturer-specific single-letter suffixes (T, G, etc.)

---


## Similarity Metadata System

The library uses a **metadata-driven architecture** for component similarity calculations with configurable, type-specific rules.

**Key Features:**
- Spec importance levels (CRITICAL, HIGH, MEDIUM, LOW, OPTIONAL)
- Tolerance rules (exactMatch, percentageTolerance, minimumRequired, etc.)
- Context-aware profiles (DESIGN_PHASE, REPLACEMENT, COST_OPTIMIZATION, etc.)
- 12 of 17 calculators converted (71% complete)

**For complete documentation**, see `.claude/skills/similarity-metadata/SKILL.md`:
- ComponentTypeMetadata architecture
- SpecImportance levels and weights
- ToleranceRule types and scoring
- SimilarityProfile contexts
- Calculator integration patterns
- 165 comprehensive tests
- Gotchas and learnings

---

## Jackson 3 Migration

This project uses **Jackson 3.0.3** for JSON serialization.

**Key changes from 2.x:**
- GroupId: `com.fasterxml.jackson.core` → `tools.jackson.core`
- Main class: `ObjectMapper` → `JsonMapper` (builder pattern)
- Java 8 date/time: Built-in (no JavaTimeModule needed)
- Annotations: Stay on 2.x (`com.fasterxml.jackson.annotation`)

**For complete migration guidance**, see `.claude/skills/jackson/SKILL.md`.

---

## Key Changes from Jackson 2.x

| Aspect | Jackson 2.x | Jackson 3.x |
|--------|-------------|-------------|
| GroupId | `com.fasterxml.jackson.core` | `tools.jackson.core` |
| Main class | `ObjectMapper` | `JsonMapper` |
| Construction | `new ObjectMapper()` | `JsonMapper.builder().build()` |
| Package | `com.fasterxml.jackson.databind` | `tools.jackson.databind.json` |
| Java 8 date/time | Requires `JavaTimeModule` | Built-in |
| Annotations | `com.fasterxml.jackson.annotation` | Stays on 2.x (`2.20`) |

### Maven Dependencies

```xml
<!-- Jackson 3.x databind -->
<dependency>
    <groupId>tools.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>3.0.3</version>
</dependency>
<!-- Annotations stay on 2.x for compatibility -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.20</version>
</dependency>
```

### Module System (module-info.java)

```java
// Jackson 3.x module names
requires transitive tools.jackson.core;
requires transitive tools.jackson.databind;
requires transitive com.fasterxml.jackson.annotation; // Still 2.x

// Opens for reflection
opens my.package to tools.jackson.databind;
```

### Code Migration Examples

```java
// BEFORE (Jackson 2.x)
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new JavaTimeModule());

// AFTER (Jackson 3.x)
import tools.jackson.databind.json.JsonMapper;

JsonMapper mapper = JsonMapper.builder().build();
// Java 8 date/time support is built-in, no module needed
```

### Gotchas

1. **Annotations package unchanged** - `@JsonProperty`, `@JsonCreator` etc. stay at `com.fasterxml.jackson.annotation`
2. **ObjectMapper still exists** but `JsonMapper` is preferred and provides the builder pattern
3. **JavaTimeModule not needed** - LocalDate, LocalDateTime, Instant work out of the box
4. **SerializationFeature** - Use builder: `JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build()`
5. **Maven Central API lag** - Search API may not show latest versions, but they download fine with Maven

### Jackson 3 Skills

See `.claude/skills/jackson/SKILL.md` for detailed migration guidance.

---

## Component Lifecycle Tracking

The library supports tracking component lifecycle status for obsolescence management.

**Lifecycle statuses:** ACTIVE → NRFND → LAST_TIME_BUY → OBSOLETE → EOL

**Key features:**
- Replacement part tracking with compatibility levels
- Status change history
- LTB deadline tracking
- JSON serialization support

**For complete documentation**, see `.claude/skills/lifecycle/SKILL.md`.

---

## Core Classes

| Class | Purpose |
|-------|---------|
| `ComponentLifecycleStatus` | Enum: ACTIVE, NRFND, LAST_TIME_BUY, OBSOLETE, EOL, UNKNOWN |
| `ComponentLifecycle` | Full lifecycle data: status, dates, replacements, history |
| `ReplacementPart` | Suggested replacement with compatibility level |
| `StatusChange` | Audit trail for status transitions |

### Lifecycle Status Flow

```
ACTIVE → NRFND → LAST_TIME_BUY → OBSOLETE → EOL
```

- **ACTIVE**: Component in production, available
- **NRFND**: Not Recommended For New Designs (still available)
- **LAST_TIME_BUY**: Final order window before discontinuation
- **OBSOLETE**: No longer manufactured (may have remaining stock)
- **EOL**: Complete end of life, no longer available

### Usage Examples

```java
// Create part with active lifecycle
ElectronicPart part = new ElectronicPart()
    .setMpn("STM32F407VGT6")
    .setManufacturer("STMicroelectronics")
    .setLifecycle(ComponentLifecycle.active());

// Create obsolete part with replacement
ElectronicPart obsolete = new ElectronicPart()
    .setMpn("LM7805CT")
    .setLifecycle(ComponentLifecycle.obsoleteWithReplacement(
        "LM7805CT/NOPB", "Texas Instruments"));

// Create Last Time Buy with deadline
ComponentLifecycle ltb = ComponentLifecycle.lastTimeBuy(LocalDate.of(2024, 9, 30));
ltb.setSource("Manufacturer PCN");
ltb.addReplacementPart("NEW-PART-001", "Same Corp",
    ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION);

// Check lifecycle risk
if (part.isLifecycleAtRisk()) {
    // Alert: component needs replacement planning
    ComponentLifecycle lifecycle = part.getLifecycle();
    if (lifecycle.isLtbApproaching(90)) {
        // LTB deadline within 90 days
    }
}
```

### Replacement Compatibility Levels

| Level | Description |
|-------|-------------|
| `FORM_FIT_FUNCTION` | Drop-in replacement, same footprint and pinout |
| `FUNCTIONAL_EQUIVALENT` | Same function, may have different form factor |
| `SIMILAR` | Similar specs, may require design changes |
| `CROSS_REFERENCE` | Equivalent from different manufacturer |
| `UPGRADE` | Better specifications than original |

### Convenience Methods on ElectronicPart

| Method | Returns | Purpose |
|--------|---------|---------|
| `getLifecycle()` | `ComponentLifecycle` | Full lifecycle data |
| `getLifecycleStatus()` | `ComponentLifecycleStatus` | Current status (or UNKNOWN) |
| `hasLifecycleInfo()` | `boolean` | True if lifecycle data exists and is not UNKNOWN |
| `isLifecycleAtRisk()` | `boolean` | True if LTB, OBSOLETE, or EOL |

### JSON Serialization

Lifecycle data serializes cleanly with Jackson 3:

```json
{
  "mpn": "MC34063ADR",
  "manufacturer": "Texas Instruments",
  "lifecycle": {
    "status": "LAST_TIME_BUY",
    "statusDate": "2026-01-12",
    "lastTimeBuyDate": "2024-09-30",
    "replacementParts": [
      {
        "mpn": "MC34063ADR-NEW",
        "manufacturer": "Texas Instruments",
        "compatibility": "FORM_FIT_FUNCTION"
      }
    ],
    "source": "Manufacturer PCN"
  }
}
```

### Status History Tracking

Status changes are automatically recorded:

```java
ComponentLifecycle lifecycle = ComponentLifecycle.active();
lifecycle.setStatus(ComponentLifecycleStatus.NRFND);
lifecycle.setStatus(ComponentLifecycleStatus.LAST_TIME_BUY);

// History now contains 2 StatusChange entries
List<StatusChange> history = lifecycle.getStatusHistory();
// [ACTIVE→NRFND, NRFND→LAST_TIME_BUY]
```

### Gotchas and Best Practices

1. **Always check for null lifecycle**: Use `hasLifecycleInfo()` or `getLifecycleStatus()` which safely returns UNKNOWN
2. **LTB deadline checks**: Use `isLtbApproaching(days)` with appropriate threshold (e.g., 90 days)
3. **Multiple replacements**: Add replacements in priority order - `getPrimaryReplacement()` returns first
4. **Source attribution**: Always set `setSource()` to track where lifecycle info came from
5. **Status changes**: Use `setStatus()` to trigger automatic history recording

### Lifecycle Skills

See `.claude/skills/lifecycle/SKILL.md` for detailed guidance.

---

## Parametric Search

Unit-aware parametric search for filtering components by specifications.

**Query syntax:** `>= 10nF`, `<= 50V`, `10nF..1uF`, `IN(X7R, X5R)`
**Supported units:** pF/nF/µF, Ω/kΩ/MΩ, mV/V/kV, mA/A, %

**For complete documentation**, see applicable skill or codebase.

---

## Query Syntax

| Syntax | Example | Description |
|--------|---------|-------------|
| `>= value` | `>= 10nF` | Greater than or equal |
| `<= value` | `<= 50V` | Less than or equal |
| `> value` | `> 1k` | Greater than |
| `< value` | `< 1uF` | Less than |
| `= value` | `= X7R` | Exact match |
| `!= value` | `!= X5R` | Not equal |
| `min..max` | `10nF..1uF` | Range (inclusive) |
| `IN(a, b, c)` | `IN(X7R, X5R, C0G)` | Set membership |

### Supported Units

Values are automatically parsed with unit awareness:
- **Capacitance**: pF, nF, uF/µF, mF, F
- **Resistance**: Ω, kΩ/k, MΩ/M, GΩ
- **Voltage**: mV, V, kV
- **Current**: nA, µA, mA, A
- **Percentage**: %

### Usage Examples

```java
// Static filter method
List<ElectronicPart> results = ParametricSearch.filter(capacitors, Map.of(
    "capacitance", ">= 10nF",
    "voltage", ">= 25V",
    "dielectric", "IN(X7R, X5R)"
));

// Fluent builder
List<ElectronicPart> results = ParametricSearch.search(capacitors)
    .min("capacitance", "10nF")
    .max("voltage", "50V")
    .in("dielectric", "X7R", "X5R")
    .find();

// Range query
List<ElectronicPart> results = ParametricSearch.search(resistors)
    .range("resistance", "1k", "100k")
    .where("tolerance", "<= 1%")
    .find();

// Check single part
boolean matches = ParametricSearch.meets(part, "capacitance", ">= 10nF");

// Utility methods
long count = ParametricSearch.search(parts).min("voltage", "25V").count();
boolean any = ParametricSearch.search(parts).equals("dielectric", "X7R").anyMatch();
Optional<ElectronicPart> first = ParametricSearch.search(parts).min("capacitance", "1uF").findFirst();
```

### Built-in Fields

The search also checks common ElectronicPart fields:
- `value` - Component value
- `package` / `pkg` - Package type
- `manufacturer` / `mfr` - Manufacturer name
- `description` / `desc` - Description
- `mpn` - Manufacturer part number

### Gotchas

1. **Unit prefixes are case-sensitive for some**: `k` and `K` both work for kilo, but `m` is milli, `M` is mega
2. **Missing specs return no match**: Parts without the requested spec are filtered out
3. **String comparisons are case-insensitive**: `= X7R` matches `x7r`
4. **IN clause is case-insensitive**: `IN(x7r, X5R)` works

---

## Historical Documentation

For historical information about implemented features, bug fixes, and decisions:

- **HISTORY.md** - Chronological timeline of all PRs and milestones
- **.docs/history/** - Detailed analysis of significant implementations
  - **BUG_FIX_ANALYSIS.md** - Critical bugs discovered and fixed (PR #89, PR #114-115)
  - **HANDLER_IMPLEMENTATION_PATTERNS.md** - Handler development patterns and best practices
- **Skills** - Component-specific and feature-specific documentation in `.claude/skills/`

