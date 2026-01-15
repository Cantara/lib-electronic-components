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

```bash
# 1. Create feature branch
git checkout -b feature/short-description

# 2. Make changes and commit
git add <files>
git commit -m "feat: description"

# 3. Push and create PR
git push -u origin feature/short-description
gh pr create --title "feat: description" --body "## Summary\n..."
```

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
- **Test coverage**: 61 handlers have tests (4497 tests), 17 similarity calculators have tests (302 tests)
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

### Batch 6 Learnings (PR #88)

**Connector Handlers (Molex, Hirose, JST)**:
- Connector MPNs typically have format: `SERIES-PACKAGE` (e.g., "43045-0802")
- Package code often encodes: pin count (first 2 digits) + mounting type (last 2 digits)
- Series numbers often indicate gender: odd = male/header, even = female/receptacle
- Helper methods like `getPitch()`, `getMountingType()`, `getRatedCurrent()` are useful additions

**Memory Handlers (Winbond, ISSI)**:
- Memory handlers should support multiple component types (MEMORY, MEMORY_FLASH, MEMORY_EEPROM, etc.)
- ISSI also makes LED drivers (IS31FL series) - add `IC` type to getSupportedTypes()
- Package codes are typically suffix letters mapped to package types (S=SOIC, F=QFN, etc.)
- Winbond overrides `matches()` for direct pattern matching - faster than registry lookup

**RF Handlers (Qorvo, Skyworks)**:
- RF handlers register patterns under `ComponentType.IC` - must include IC in getSupportedTypes()
- Series extraction often takes prefix letters + first 4 digits (not all digits)
- Skyworks includes Silicon Labs legacy MCUs (EFM8, EFM32, EFR32) - add MICROCONTROLLER type
- Package codes often indicate tape & reel (-TR1, -REEL) or package type (-QFN, -WLCSP)

**Sensor Handlers (Bosch, InvenSense, Melexis)**:
- Sensor handlers need many component types (SENSOR, ACCELEROMETER, GYROSCOPE, MAGNETOMETER, etc.)
- InvenSense also makes audio/motion processors (ICS, IAC series) - add IC type
- Melexis package code extraction has a bug: regex `^[A-Z0-9]+` removes all alphanumerics
- Series extraction typically returns first 6-8 characters of the MPN

**General Handler Bug Pattern**:
- When handler registers patterns for `ComponentType.IC`, must include `IC` in getSupportedTypes()
- Otherwise `matches()` may return true but `getSupportedTypes()` won't contain the type

### Architecture Notes
- `PatternRegistry` supports multi-handler per ComponentType but this is largely unused
- Similarity calculators registered in `MPNUtils` static initializer (lines 34-48)
- `ManufacturerHandlerFactory` uses reflection-based classpath scanning with TreeSet for deterministic order

---

## Jinling Handler Implementation (PR #102 - January 2026)

### Position-Based MPN Encoding Learnings

**Critical Discovery**: Jinling uses a strict 15-character position-based elprint format where EVERY position has meaning:

```
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

**Key Gotchas**:
1. **Exact Length Required**: Handler pattern MUST validate exactly 15 characters, not 13-17 variable length
2. **Numeric Positions**: Positions 2-7 (6 characters) MUST be numeric digits - letters will fail validation
3. **No Placeholders**: Test MPNs cannot use "X" or other placeholder characters in numeric positions
4. **Position Alignment Matters**: Moving a character one position left/right completely changes the meaning
   - Example: Connector type at position 8 instead of 12 breaks everything

### Test MPN Format Pitfalls

**Problem Pattern**: Initial tests had 13-17 character MPNs with encoding in wrong positions:
- `273102NSNSUXT` (13 chars) - Missing positions
- `27310202ASNSUTT` (17 chars) - Extra characters
- `27310202MNSNSUT` - "M" at position 8 (material) instead of position 12 (connector type)
- `17310110AG3SUT` (14 chars) - Missing "N" for post at position 9

**Solution**: Systematically corrected all test MPNs to proper 15-character format with correct position encoding

**Pattern Matching Fix**:
```java
// WRONG (14 chars total)
"^(?:13|16|17|26|27)[0-9]{4}[0-9]{2}[A-Z][A-Z]{2}[A-Z]{3}"

// CORRECT (15 chars: 2+6+1+1+2+3)
"^(?:13|16|17|26|27)[0-9]{6}[A-Z][A-Z][A-Z0-9]{2}[A-Z]{3}$"
```

### Distributor Format Complexity

Jinling uses TWO completely different MPN systems:

1. **Elprint Format** (15 chars): `27310202ANG3SUT` - Position-based internal encoding
2. **Distributor Format** (15-18 chars): `22850102ANG1SYA02` - LCSC/JLCPCB public MPNs

**Handler Challenge**: Must support BOTH formats with different extraction logic:
- Elprint: Extract from fixed positions
- Distributor: Pattern-based extraction varies by family (12xxx, 22xxx, 32xxx)

**Pin Count Extraction Example**:
```java
// Elprint: positions 5-7 (rows × pins/row)
"27310202" → charAt(5)='2' × substring(6,8)='02' = 4 pins

// Distributor IDC (32xxx): positions 4-5
"321010MG0CBK00A02" → substring(4,6)='10' = 10 pins

// Distributor Headers (12xxx, 22xxx): positions 6-7
"12251140CNG0S115001" → substring(6,8)='40' = 40 pins
```

### Test Suite Journey

| Stage | Tests Passing | Failures | Issue |
|-------|--------------|----------|-------|
| Initial | 91/131 (69%) | 40 | MPN format violations |
| After pattern fix | 106/131 (81%) | 25 | Test MPNs still wrong |
| After MPN corrections | 122/131 (93%) | 9 | Orientation, distributor patterns |
| After distributor fix | 127/131 (97%) | 4 | Pin count, replacement logic |
| Final | 131/131 (100%) | 0 | All tests passing ✓ |

### Validation Strategy

**isElprintFormat() Design**:
```java
// CRITICAL: Check exact length AND numeric positions
if (mpn == null || mpn.length() != 15) return false;  // Exact 15

// Positions 2-7 must be ALL numeric (not just positions 2-5)
boolean hasNumericPrefix = mpn.substring(2, 8).matches("[0-9]{6}");
```

**Why This Matters**: Position-based extraction uses `substring(6,8)` for pins per row - if this contains letters, `Integer.parseInt()` throws NumberFormatException and pin count returns 0.

### Best Practices from This Implementation

1. **Pattern Design**: For position-based MPNs, validate EXACT length upfront, not ranges
2. **Test Data**: Use REAL distributor MPNs from LCSC/JLCPCB, not invented examples
3. **Incremental Testing**: Fix patterns first, then test MPNs, then extraction logic
4. **Documentation**: ASCII diagrams showing position meanings are invaluable
5. **Dual Format Support**: When manufacturer uses multiple formats, document both clearly

### Files Created/Modified

- **JinlingHandler.java** (~560 lines): Dual-format support, 15+ helper methods
- **JinlingHandlerTest.java** (~550 lines, 131 tests): Comprehensive test coverage
- **SKILL.md** (~400 lines): Position-by-position encoding reference
- **ComponentManufacturer.java**: Added JINLING enum entry
- **ComponentType.java**: Added CONNECTOR_JINLING type

---

## Codebase Analysis Findings (January 2026)

### Test Coverage Status

| Category | Total | With Tests | Without Tests | Coverage |
|----------|-------|------------|---------------|----------|
| Handlers | 67 | 61 | 6 | 91.0% |
| Similarity Calculators | 17 | 17 | 0 | 100% |

**Handlers Without Tests (6)**: AbstractManufacturerHandler (base class), FairchildHandler, LogicICHandler, QualcommHandler, SpansionHandler, UnknownHandler (special case)

**Similarity Calculators (ALL tested)**: ResistorSimilarityCalculator, CapacitorSimilarityCalculator, TransistorSimilarityCalculator, DiodeSimilarityCalculator, MosfetSimilarityCalculator, OpAmpSimilarityCalculator, MCUSimilarityCalculator, MicrocontrollerSimilarityCalculator, MemorySimilarityCalculator, SensorSimilarityCalculator, ConnectorSimilarityCalculator, LEDSimilarityCalculator, VoltageRegulatorSimilarityCalculator, LogicICSimilarityCalculator, PassiveComponentCalculator, LevenshteinCalculator, DefaultSimilarityCalculator (302 tests total)

### Technical Debt Inventory

| Issue | Count | Severity | Location |
|-------|-------|----------|----------|
| System.out.println debug statements | 181 | HIGH | 19 files, primarily MPNUtils (35), ManufacturerHandlerFactory (17), similarity calculators (97) |
| printStackTrace() calls | 9 | HIGH | ManufacturerHandlerFactory (8), MPNUtils (1) |
| Magic numbers in scoring | 108+ | MEDIUM | All similarity calculators - hardcoded weights |
| Inconsistent getSupportedTypes() | 59 | MEDIUM | 34 use HashSet, 23 use Set.of() |

### Pattern Inconsistencies Found

**getSupportedTypes() Pattern Split**:
- **Set.of() (modern, preferred)**: 23 handlers - AtmelHandler, STHandler, TIHandler, BoschHandler, HiroseHandler, etc.
- **HashSet (legacy, mutable)**: 34 handlers - CreeHandler, AbraconHandler, LGHandler, PanasonicHandler, VishayHandler, etc.

**Type Declaration vs Pattern Registration Mismatches**:
| Handler | Issue |
|---------|-------|
| MaximHandler | Declares INTERFACE_IC_MAXIM, RTC_MAXIM, BATTERY_MANAGEMENT_MAXIM but doesn't register patterns |
| EspressifHandler | Declares ESP8266_SOC, ESP32_SOC, all module types but only registers MICROCONTROLLER patterns |
| PanasonicHandler | Declares capacitor/inductor types but registers NO patterns for them |

### Code Quality Issues Found

| File | Issue | Line(s) |
|------|-------|---------|
| LogicICHandler.java | Debug System.out.println in production | 70, 75, 84, 85 |
| EspressifHandler.java | NPE risk - substring without checking indexOf result | 153-154 |
| InfineonHandler.java | Commented-out code | 44, 49, 51 |

### Priority Action Items

1. **IMMEDIATE**: Replace 181 debug statements with SLF4J logging
2. **IMMEDIATE**: Replace 9 printStackTrace() calls with logger.error()
3. **HIGH**: Add tests for remaining 15 handlers
4. **HIGH**: Standardize all getSupportedTypes() to Set.of()
5. **HIGH**: Fix type/pattern mismatches in MaximHandler, EspressifHandler, PanasonicHandler
6. ~~**MEDIUM**: Add similarity calculator test suite (0% coverage)~~ ✓ DONE (302 tests)
7. **MEDIUM**: Extract magic numbers to configurable constants

### Recent Infrastructure (PR #74)
- **`PackageCodeRegistry`** - Centralized package code mappings (PU→PDIP, AU→TQFP, etc.)
- **`AbstractManufacturerHandler`** - Base class with `extractSuffixAfterHyphen()`, `extractTrailingSuffix()`, `findFirstDigitIndex()`, `findLastDigitIndex()` helpers

---

## Bug Fix Learnings (PR #89 - January 2026)

### Bug Patterns & Fixes

**1. NPE Prevention in substring() Operations**
```java
// BAD - throws StringIndexOutOfBoundsException if no dash
String base = mpn.substring(0, mpn.indexOf('-'));

// GOOD - guard against missing delimiter
int dash = mpn.indexOf('-');
String base = dash >= 0 ? mpn.substring(0, dash) : mpn;
```
*Affected*: EspressifHandler line 153-154

**2. Pattern Ordering for Series Extraction**
```java
// BAD - generic pattern matches first, returns wrong series
if (mpn.matches("1N4[0-9]{3}.*")) return "1N4000";  // Matches 1N4148!
if (mpn.matches("1N4148.*")) return "1N4148";       // Never reached

// GOOD - specific patterns before generic
if (mpn.matches("1N4148.*")) return "1N4148";       // Check FIRST
if (mpn.matches("1N914.*")) return "1N914";
if (mpn.matches("1N47[0-9]{2}.*")) return "1N47xx";
if (mpn.matches("1N4[0-9]{3}.*")) return "1N4000";  // Generic LAST
```
*Affected*: VishayHandler extractSeries()

**3. Type/Pattern Registration Mismatch**
```java
// BAD - declares type but doesn't register patterns
getSupportedTypes() → includes ESP32_SOC
initializePatterns() → only registers MICROCONTROLLER

// GOOD - every declared type must have patterns
initializePatterns() {
    registry.addPattern(ComponentType.ESP32_SOC, "^ESP32[^-].*");
    registry.addPattern(ComponentType.MICROCONTROLLER, "^ESP32[^-].*");
}
```
*Affected*: EspressifHandler, MaximHandler, PanasonicHandler

**4. Hyphenated MPN Normalization**
```java
// BAD - hyphen breaks position-based extraction
"ERJ-3GEYJ103V" → position 3 returns '-' not '3'

// GOOD - normalize first
String normalized = mpn.replace("-", "");
"ERJ3GEYJ103V" → position 3 returns '3' (size code)
```
*Affected*: PanasonicHandler extractPackageCode()

**5. Missing matches() Override for Manufacturer-Specific Types**
```java
// BAD - falls through to registry which may not find MOSFET_VISHAY
// Because pattern was only registered for MOSFET base type

// GOOD - explicit check in matches()
if (type == ComponentType.MOSFET || type == ComponentType.MOSFET_VISHAY) {
    if (upperMpn.matches("^SI[0-9]+.*") || upperMpn.matches("^SIH.*")) {
        return true;
    }
}
```
*Affected*: VishayHandler

**6. Cross-Handler Pattern Matching (CRITICAL)**
```java
// BAD - default matches() used getPattern(type) which returns
// the FIRST pattern from ANY handler. Caused false matches!
default boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
    Pattern pattern = patterns.getPattern(type);  // Gets ANY handler's pattern!
    return pattern != null && pattern.matcher(mpn).matches();
}

// GOOD - use handler-specific patterns only
default boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
    // Only checks patterns registered by THIS handler
    return patterns.matchesForCurrentHandler(mpn.toUpperCase(), type);
}
```
*Root cause*: CypressHandler (alphabetically before STHandler) didn't override matches().
When testing STM32F103C8T6, it used STHandler's pattern and incorrectly matched.
*Symptom*: Tests pass locally but fail in CI (different HashMap iteration order).
*Fix*: Added `matchesForCurrentHandler()` to PatternRegistry.

### Quick Reference: Handler Bug Checklist

When reviewing/fixing a handler, check for:

| Check | Issue | Fix |
|-------|-------|-----|
| `indexOf()` without guard | NPE risk | Add `>= 0` check before substring |
| Generic pattern before specific | Wrong match | Reorder: specific first |
| Type in getSupportedTypes() | Missing pattern | Add to initializePatterns() |
| Position-based extraction | Hyphen breaks it | Normalize: `mpn.replace("-", "")` |
| Base type matches, specific doesn't | Missing override | Add explicit check in matches() |
| System.out.println | Debug leak | Remove or replace with logger |
| HashSet in getSupportedTypes() | Mutable | Change to Set.of() |
| Handler doesn't override matches() | Cross-handler false match | Override matches() or patterns fix |

### CI vs Local Test Differences

**If tests pass locally but fail in CI:**
1. Check for HashMap/HashSet iteration order dependencies
2. Check for cross-handler pattern matching (see #6 above)
3. Check handler alphabetical order - first matching handler wins
4. Verify TreeSet comparators are truly deterministic

---

## Similarity Calculator Learnings (January 2026)

### Calculator Architecture

**Two Interface Types**:
1. **`SimilarityCalculator`** - Simple interface: `calculateSimilarity(String mpn1, String mpn2)` → double
   - Used by: `MCUSimilarityCalculator`, `LevenshteinCalculator`, `PassiveComponentCalculator`
   - No registry required, pattern-based matching

2. **`ComponentSimilarityCalculator`** - Type-aware: `isApplicable(ComponentType)` + `calculateSimilarity(String, String, PatternRegistry)`
   - Used by: `ResistorSimilarityCalculator`, `CapacitorSimilarityCalculator`, etc.
   - Requires PatternRegistry for manufacturer handler integration

### Similarity Thresholds (Common Pattern)

```java
HIGH_SIMILARITY = 0.9;    // Equivalent/interchangeable parts
MEDIUM_SIMILARITY = 0.7;  // Compatible but different (same family)
LOW_SIMILARITY = 0.3;     // Same type, different specs
```

### Test Writing Gotchas

**1. Avoid Exact Equality Assertions**
```java
// BAD - breaks when calculator tuning changes
assertEquals(0.9, similarity);

// GOOD - uses threshold ranges
assertTrue(similarity >= MEDIUM_SIMILARITY);
assertTrue(similarity > 0.5 && similarity < 1.0);
```

**2. Equivalent Groups Don't Always Score 0.9**
- Some calculators return MEDIUM_SIMILARITY (0.7) for variants
- Example: `LM35DZ` vs `LM35CZ` may return 0.7, not 0.9
- Test with `>= MEDIUM_SIMILARITY` instead of `== HIGH_SIMILARITY`

**3. Cross-Manufacturer Equivalents**
- 1N4148 ≈ 1N914 (signal diodes) → 0.9
- 2N2222 ≈ PN2222 ≈ MMBT2222 (transistors) → 0.9
- LM358 ≈ MC1458 (op-amps) → 0.9

**4. Package Variants Have High Similarity**
- Same part, different package = 0.9 (e.g., ATMEGA328P-AU vs ATMEGA328P-PU)
- Package doesn't affect core part equivalence

**5. Unknown Parts Default to Levenshtein**
- When type can't be determined, falls back to string similarity
- Ensures non-zero scores for similar-looking MPNs

### Calculator-Specific Notes

| Calculator | Key Behavior |
|------------|--------------|
| ResistorSimilarityCalculator | Compares value (10K), package (0603), tolerance (1%) |
| CapacitorSimilarityCalculator | Compares capacitance (104=100nF), voltage, dielectric (X7R) |
| TransistorSimilarityCalculator | Uses equivalent groups (2N2222≈PN2222), polarity matching |
| DiodeSimilarityCalculator | Signal/rectifier/zener grouping, 1N400x family matching |
| MosfetSimilarityCalculator | N/P channel, voltage/current ratings, IRF/IRFP families |
| OpAmpSimilarityCalculator | Single/dual/quad variants, manufacturer families |
| MCUSimilarityCalculator | Family (50%), series (30%), features (20%) weighted |
| MemorySimilarityCalculator | Interface (I2C/SPI), density, temperature grade |
| SensorSimilarityCalculator | Sensor type (temp/accel/humidity), interface, accuracy |
| LEDSimilarityCalculator | Color bin, brightness bin, package |
| ConnectorSimilarityCalculator | Pin count (MUST match), pitch, mounting type |
| VoltageRegulatorSimilarityCalculator | Fixed (78xx) vs adjustable (LM317), voltage rating |
| LogicICSimilarityCalculator | Function (00=NAND), technology (LS/HC/HCT), family |

### Skills Documentation

Similarity calculator skills are in `.claude/skills/similarity*/`:
- Main skill: `similarity/SKILL.md` - Overview and architecture
- Specialized skills: `similarity-resistor/`, `similarity-mcu/`, etc.

Each skill documents:
- Applicable ComponentTypes
- Similarity thresholds and weights
- Equivalent groups
- MPN pattern examples
- Test examples
- Learnings & quirks section (append new discoveries here)

---

## Similarity Metadata System (January 2026)

The library now uses a **metadata-driven architecture** for component similarity calculations, replacing hardcoded logic with configurable, type-specific similarity rules.

### Motivation

**Problem**: Previous similarity calculators had hardcoded weights and thresholds, making them difficult to tune and inconsistent across component types.

**Solution**: Centralized metadata system that defines:
- Which specs are critical vs optional for each component type
- How to compare spec values (exact match, percentage tolerance, min/max requirements)
- Context-aware similarity profiles (design phase, replacement, cost optimization, etc.)

### Core Classes

| Class | Responsibility |
|-------|----------------|
| `ComponentTypeMetadata` | Defines specs, importance levels, tolerance rules for a component type |
| `ComponentTypeMetadataRegistry` | Singleton registry; maps ComponentType → metadata |
| `SpecImportance` | Enum: CRITICAL, HIGH, MEDIUM, LOW, OPTIONAL (with base weights) |
| `ToleranceRule` | Interface for comparing spec values with scoring logic |
| `SimilarityProfile` | Enum: 5 context-aware profiles that adjust importance multipliers |
| `SpecValue<T>` | Unit-aware value representation with min/max ranges |
| `SpecUnit` | Unit types: NONE, OHM, FARAD, HENRY, VOLT, AMPERE, WATT, HERTZ, PERCENTAGE |

### SpecImportance Levels

Defines how important a spec is for similarity matching:

| Level | Base Weight | Mandatory | Use Case |
|-------|-------------|-----------|----------|
| CRITICAL | 1.0 | Yes | Specs that affect core functionality (resistance value, capacitance, polarity) |
| HIGH | 0.7 | No | Specs that affect reliability (package type, tolerance, dielectric) |
| MEDIUM | 0.4 | No | Specs that affect performance (power rating, ESR, temp coefficient) |
| LOW | 0.2 | No | Secondary considerations (gate charge, viewing angle, certifications) |
| OPTIONAL | 0.0 | No | Informational only (lifecycle status, manufacturer notes) |

**Key Design**: Only CRITICAL specs are mandatory. Effective weight = baseWeight × profile multiplier.

### ToleranceRule Types

Defines how to compare candidate vs original spec values:

#### 1. ExactMatchRule
```java
ToleranceRule.exactMatch()
```
- Scores 1.0 for exact match, 0.0 otherwise
- Case-insensitive for strings
- Use for: dielectric types (X7R, X5R), polarity (NPN, PNP), package codes

#### 2. PercentageToleranceRule
```java
ToleranceRule.percentageTolerance(double maxPercent)
```
- Scores 1.0 if within tolerance, decays linearly beyond
- Example: `percentageTolerance(5.0)` allows ±5% deviation
- Use for: resistance, capacitance, inductance

#### 3. MinimumRequiredRule
```java
ToleranceRule.minimumRequired()
```
- Scores 1.0 if candidate ≥ original (better or equal)
- Scores 0.0 if candidate < original (insufficient)
- Use for: voltage rating, current rating (candidate must meet or exceed)

#### 4. MaximumAllowedRule
```java
ToleranceRule.maximumAllowed(double maxMultiplier)
```
- Scores 1.0 if candidate ≤ original (better or equal)
- Scores decay linearly up to maxMultiplier
- Example: `maximumAllowed(1.5)` allows up to 1.5× the original value
- Use for: Rds(on), ESR (lower is better, some increase acceptable)

#### 5. RangeToleranceRule
```java
ToleranceRule.rangeTolerance(double lowerPercent, double upperPercent)
```
- Scores 1.0 within range, decays outside
- Example: `rangeTolerance(-10.0, 20.0)` allows -10% to +20%
- Use for: asymmetric tolerances, hFE ranges

### SimilarityProfile Contexts

Adjusts importance multipliers based on use case:

| Profile | Threshold | CRITICAL | HIGH | MEDIUM | LOW | Use Case |
|---------|-----------|----------|------|--------|-----|----------|
| DESIGN_PHASE | 0.85 | 1.0 | 0.9 | 0.7 | 0.4 | Exact match for new designs |
| REPLACEMENT | 0.75 | 1.0 | 0.7 | 0.4 | 0.2 | **Default**: Direct replacement |
| PERFORMANCE_UPGRADE | 0.70 | 1.0 | 0.8 | 0.5 | 0.2 | Better performance acceptable |
| COST_OPTIMIZATION | 0.60 | 1.0 | 0.4 | 0.2 | 0.0 | Maintain critical specs, relax others |
| EMERGENCY_SOURCING | 0.50 | 0.8 | 0.4 | 0.2 | 0.0 | Urgent replacement, relaxed requirements |

**Effective Weight Calculation**:
```
effectiveWeight = importance.baseWeight × profile.multiplier
```

Example: HIGH spec (0.7) in COST_OPTIMIZATION (0.4) → 0.7 × 0.4 = 0.28

### ComponentTypeMetadata Structure

Built using fluent builder pattern:

```java
ComponentTypeMetadata resistor = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
    .addSpec("tolerance", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
    .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
    .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
    .addSpec("temperatureCoefficient", SpecImportance.LOW, ToleranceRule.percentageTolerance(20.0))
    .defaultProfile(SimilarityProfile.REPLACEMENT)
    .build();
```

**Querying Metadata**:
```java
// Check if spec is configured
SpecConfig config = metadata.getSpecConfig("resistance");
if (config != null) {
    SpecImportance importance = config.getImportance();
    ToleranceRule rule = config.getToleranceRule();
}

// Check if spec is critical
boolean critical = metadata.isCritical("resistance"); // true

// Get all configured specs
Set<String> allSpecs = metadata.getAllSpecs();

// Get default profile
SimilarityProfile profile = metadata.getDefaultProfile();
```

### ComponentTypeMetadataRegistry

Singleton registry pre-configured for top 10 component types:

```java
ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

// Lookup metadata
Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.RESISTOR);

// Fallback to base type for manufacturer-specific types
Optional<ComponentTypeMetadata> yageoMeta = registry.getMetadata(ComponentType.RESISTOR_CHIP_YAGEO);
// Returns RESISTOR metadata (base type fallback)

// Custom registration
ComponentTypeMetadata customMetadata = ComponentTypeMetadata.builder(ComponentType.INDUCTOR)
    .addSpec("inductance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
    .build();
registry.register(customMetadata);
```

### Pre-Registered Component Types

The registry initializes with metadata for:

| ComponentType | Critical Specs | HIGH Specs | Notes |
|---------------|----------------|------------|-------|
| RESISTOR | resistance, tolerance | package | 2 critical, 5 total specs |
| CAPACITOR | capacitance, voltage, dielectric | package | 3 critical, 7 total specs |
| MOSFET | voltageRating, currentRating, channel | rdsOn | 3 critical, 6 total specs |
| TRANSISTOR | polarity, voltageRating | collectorCurrent | 2 critical, 6 total specs |
| DIODE | type, voltageRating | currentRating | 2 critical, 5 total specs |
| OPAMP | configuration, inputType | supplyVoltageMin | 2 critical, 6 total specs |
| MICROCONTROLLER | family, flashSize | ramSize | 2 critical, 5 total specs |
| MEMORY | type, capacity, interface | speed | 3 critical, 5 total specs |
| LED | color, brightness | wavelength | 2 critical, 4 total specs |
| CONNECTOR | pinCount, pitch, gender | mountingType | 3 critical, 6 total specs |

### Usage Examples

#### Define Resistor Similarity
```java
ComponentTypeMetadata resistor = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
    .addSpec("tolerance", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
    .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
    .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
    .defaultProfile(SimilarityProfile.REPLACEMENT)
    .build();

// Check if 10.1kΩ is similar to 10kΩ (within 1%)
boolean similar = resistor.isCritical("resistance"); // true
SpecConfig config = resistor.getSpecConfig("resistance");
ToleranceRule rule = config.getToleranceRule();

SpecValue<Double> original = new SpecValue<>(10000.0, SpecUnit.OHM);
SpecValue<Double> candidate = new SpecValue<>(10100.0, SpecUnit.OHM);
double score = rule.calculateScore(original, candidate); // 0.0 (outside 1% tolerance)
```

#### Context-Aware Similarity
```java
ComponentTypeMetadata capacitor = registry.getMetadata(ComponentType.CAPACITOR).orElseThrow();

// Design phase: strict matching (0.85 threshold)
SimilarityProfile designProfile = SimilarityProfile.DESIGN_PHASE;
double designMultiplier = designProfile.getMultiplier(SpecImportance.HIGH); // 0.9
boolean passesDesign = designProfile.meetsThreshold(0.82); // false

// Emergency sourcing: relaxed matching (0.50 threshold)
SimilarityProfile emergencyProfile = SimilarityProfile.EMERGENCY_SOURCING;
double emergencyMultiplier = emergencyProfile.getMultiplier(SpecImportance.HIGH); // 0.4
boolean passesEmergency = emergencyProfile.meetsThreshold(0.55); // true
```

### Testing Strategy

165 comprehensive tests covering:

**ToleranceRuleTest** (35 tests):
- Each rule implementation (ExactMatch, Percentage, MinimumRequired, MaximumAllowed, Range)
- Scoring behavior, edge cases, null handling
- isAcceptable() threshold behavior

**SimilarityProfileTest** (36 tests):
- Multiplier values for all 5 profiles × 5 importance levels
- Effective weight calculations
- Threshold acceptance logic
- Real-world scenarios

**ComponentTypeMetadataTest** (29 tests):
- Builder pattern validation
- Spec config queries (getSpecConfig, getAllSpecs)
- Critical spec identification
- Real-world examples (resistor, capacitor, MOSFET)

**ComponentTypeMetadataRegistryTest** (35 tests):
- Singleton pattern
- Pre-registered types (10 component types)
- Base type fallback for manufacturer-specific types
- Custom registration and overwriting
- Critical spec configuration
- Tolerance rule configuration

**SpecImportanceTest** (30 tests):
- Base weight values
- Mandatory flag (only CRITICAL is mandatory)
- Weight distribution (CRITICAL+HIGH = 70% of total)
- Semantic meaning and real-world usage

### Gotchas and Learnings

**1. SpecValue Instantiation**
```java
// NO static factory method - use constructor
SpecValue<Double> value = new SpecValue<>(100.0, SpecUnit.FARAD); // ✓
SpecValue<Double> value = SpecValue.of(100.0); // ✗ Does not exist
```

**2. ComponentTypeMetadata API**
```java
// Methods return directly, not Optional (except registry lookup)
SpecConfig config = metadata.getSpecConfig("resistance"); // Can be null
Set<String> specs = metadata.getAllSpecs(); // Never null
boolean critical = metadata.isCritical("resistance"); // false if not found
```

**3. Singleton Registry Side Effects**
- Test isolation issue: Custom registration persists across tests
- Solution: Use unregistered types (CRYSTAL, FUSE) for tests, not pre-registered types (RESISTOR, CAPACITOR)

**4. Builder Validation**
```java
// Throws IllegalArgumentException (not NPE) for null component type
ComponentTypeMetadata.builder(null); // ✗ IllegalArgumentException

// Throws IllegalStateException if no specs added
ComponentTypeMetadata.builder(ComponentType.IC).build(); // ✗ IllegalStateException
```

**5. Map.get(null) NPE**
```java
// getSpecConfig(null) throws NullPointerException (expected Map behavior)
metadata.getSpecConfig(null); // ✗ NullPointerException
```

**6. Profile Multiplier Values**
- COST_OPTIMIZATION maintains CRITICAL=1.0 (not 0.9) - safety specs never compromised
- EMERGENCY_SOURCING relaxes CRITICAL to 0.8 - only for truly urgent scenarios
- PERFORMANCE_UPGRADE has CRITICAL=1.0, HIGH=0.8 (not inverted)

**7. Tolerance Rule Acceptance Thresholds**
- Default: 0.7 (from interface default method)
- MinimumRequiredRule: 0.8 (stricter because exact match required)
- Always check `isAcceptable()` in addition to score for filtering

**8. Test Coverage Strategy**
- Use nested @DisplayName test classes for organization
- Use @ParameterizedTest with @ValueSource/@CsvSource for data-driven tests
- Document expected behavior in test names (shouldXxxWhenYyy pattern)
- Test both positive and negative cases (shouldAccept vs shouldReject)

### Milestone 2: Calculator Integration (January 2026)

**Milestone Status**: ✅ COMPLETED

Integrated the metadata-driven architecture into existing similarity calculators, starting with ResistorSimilarityCalculator and CapacitorSimilarityCalculator.

#### Integration Pattern

Each refactored calculator follows this pattern:

```java
public class XxxSimilarityCalculator implements ComponentSimilarityCalculator {
    private final ComponentTypeMetadataRegistry metadataRegistry;

    public XxxSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        // Get metadata
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.XXX);
        if (metadataOpt.isEmpty()) {
            return calculateLegacySimilarity(mpn1, mpn2); // Fallback
        }

        ComponentTypeMetadata metadata = metadataOpt.get();
        SimilarityProfile profile = metadata.getDefaultProfile();

        // Extract specs from MPNs
        String spec1 = extractSpec(mpn1);
        String spec2 = extractSpec(mpn2);

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // For each spec: compare using ToleranceRule
        ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("specName");
        if (config != null && spec1 != null && spec2 != null) {
            ToleranceRule rule = config.getToleranceRule();
            SpecValue<T> orig = new SpecValue<>(parseSpec(spec1), SpecUnit.XXX);
            SpecValue<T> cand = new SpecValue<>(parseSpec(spec2), SpecUnit.XXX);

            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(config.getImportance());

            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
        }

        // Normalize to [0.0, 1.0]
        return maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;
    }

    private double calculateLegacySimilarity(String mpn1, String mpn2) {
        // Keep old hardcoded logic for backward compatibility
    }
}
```

#### Refactored Calculators

**1. ResistorSimilarityCalculator** (PR #XXX)

*Before*:
- Hardcoded weights: 0.3 (package), 0.5 (value)
- Max similarity: 0.8
- String-based value comparison

*After*:
- Metadata-driven: package (HIGH), resistance (CRITICAL)
- Max similarity: 1.0 (normalized)
- Numeric comparison with PercentageToleranceRule(1.0%)
- Effective weights: package=0.49, resistance=1.0 → total=1.49, normalized to [0.0, 1.0]

*Score Changes*:
- Perfect match: 0.8 → 1.0
- Value match only: 0.5 → 0.67 (resistance dominates)
- Package match only: 0.3 → 0.33 (normalized)

*Test Updates*:
- Updated 4 assertions to expect normalized [0.0, 1.0] range
- Added comments explaining metadata-driven scoring
- All 20 tests passing

**2. CapacitorSimilarityCalculator** (PR #XXX)

*Before*:
- Hardcoded weights: 0.3 (package), 0.4 (value), 0.2 (voltage)
- Max similarity: 0.9
- Voltage comparison: simple `v1 >= v2` check

*After*:
- Metadata-driven: package (HIGH), capacitance (CRITICAL), voltage (CRITICAL)
- Max similarity: 1.0 (normalized)
- MinimumRequiredRule for voltage (asymmetric by design)
- Effective weights: package=0.49, capacitance=1.0, voltage=1.0 → total=2.49, normalized to [0.0, 1.0]

*Score Changes*:
- Perfect match: 0.9 → 1.0
- Asymmetry increased due to voltage weight (0.2 → 1.0)

*Test Updates*:
- Renamed symmetry test to "similarityRespectsVoltageRatingAsymmetry"
- Documented that voltage asymmetry is **correct behavior** for replacement scenarios
- All 20 tests passing

#### Key Implementation Details

**1. Legacy Fallback**
```java
if (metadataOpt.isEmpty()) {
    logger.warn("No metadata found for {} type, falling back to legacy scoring", type);
    return calculateLegacySimilarity(mpn1, mpn2);
}
```
Ensures backward compatibility if metadata unavailable.

**2. Value Parsing**
Added type-specific parsing methods:
- `parseResistanceValue(String)` → Double (in ohms): "10K" → 10000.0
- `parseCapacitanceValue(String)` → Double (in farads): "0.1µF" → 1.0e-7

**3. Normalization**
Critical change: all scores now normalized to [0.0, 1.0]:
```java
double normalizedSimilarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;
```

**4. Voltage Asymmetry is Correct**
MinimumRequiredRule creates intentional asymmetry:
- Downgrading voltage (50V → 5V): score ~0.6 (voltage fails)
- Upgrading voltage (5V → 50V): score ~1.0 (voltage passes)

This is **correct** for component replacement: you can replace a 5V part with a 50V part, but not vice versa.

#### Lessons Learned

**1. API Discovery**
- `ToleranceRule.compare()` not `calculateScore()`
- `SpecUnit.OHMS` not `SpecUnit.OHM` (plural form)
- Always read interfaces before implementing

**2. Test Expectations**
- Old hardcoded ranges [0.0, 0.8] / [0.0, 0.9] → New normalized [0.0, 1.0]
- Update assertions with explanatory comments about metadata-driven scoring
- Document expected score changes in test comments

**3. Voltage Asymmetry**
- Don't try to enforce symmetry for MinimumRequiredRule comparisons
- Test the correctness of the asymmetry, not its absence
- Document the business logic behind the asymmetry

**4. Effective Weight Calculation**
```
effectiveWeight = baseWeight × profileMultiplier
totalScore = Σ(specScore × effectiveWeight)
normalized = totalScore / maxPossibleScore
```

Example (resistor with REPLACEMENT profile):
- Package (HIGH): 1.0 × (0.7 × 0.7) = 0.49
- Resistance (CRITICAL): 1.0 × (1.0 × 1.0) = 1.0
- Total: 1.49
- Normalized: 1.49 / 1.49 = 1.0 (perfect match)

**5. Metadata-Driven Benefits**
- Consistent scoring across all calculators
- Easy to tune weights without touching calculator code
- Context-aware profiles (DESIGN_PHASE, REPLACEMENT, COST_OPTIMIZATION)
- Self-documenting: metadata explains what specs matter and why

**6. Unicode Gotcha: Micro Sign (µ) vs Greek Mu (Μ)**

**Problem**: The micro sign µ (U+00B5) becomes Greek capital MU Μ (U+039C) when uppercased in Java:
```java
"0.1µF".toUpperCase() // Returns "0.1ΜF" (Greek MU, not micro!)
"0.1µF".toUpperCase().contains("µF") // false! Doesn't match
```

**Solution**: Replace micro variants before normalizing:
```java
String normalized = value.replace("µ", "u").replace("Μ", "u");
normalized = normalizeValue(normalized); // Now toUpperCase() works
if (normalized.contains("UF")) { // Matches both µF and plain UF
    // Parse value
}
```

**Why This Matters**: Component MPNs use µF for microfarads, and normalizeValue() calls toUpperCase(). Without the replacement, value parsing silently fails and comparisons return 0.0 similarity.

**Affected**: CapacitorSimilarityCalculator parseCapacitanceValue() method. Any future value parsing with Greek-origin SI prefixes (µ, Ω) must handle this.

#### Next Steps (Milestone 3)

Remaining 15 calculators to refactor:
- TransistorSimilarityCalculator
- MosfetSimilarityCalculator
- DiodeSimilarityCalculator
- OpAmpSimilarityCalculator
- And 11 more...

Same pattern, faster implementation with established helpers and test templates.

---

## Jackson 3 Migration (January 2026)

This project uses **Jackson 3.0.3** for JSON serialization.

### Key Changes from Jackson 2.x

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

## Component Lifecycle Tracking (January 2026)

This library now supports tracking component lifecycle status for obsolescence management.

### Core Classes

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

## Parametric Search (January 2026)

The library provides unit-aware parametric search for filtering components by specifications.

### Query Syntax

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

## Critical Bug Fixes (January 2026 - PR #114)

### Bug 1: MPNUtils Ignoring 0.0 Calculator Results

**Discovered**: January 14, 2026
**Severity**: CRITICAL - Caused incompatible parts to receive high similarity scores

**Problem**: `MPNUtils.calculateSimilarity()` had a check `if (similarity > 0)` at line 135 that **ignored calculator results of 0.0**. When similarity calculators correctly returned 0.0 for incompatible parts (e.g., N-channel vs P-channel MOSFETs), the code fell through to `calculateDefaultSimilarity()` which gave high scores based only on manufacturer/type matching.

**Impact Examples**:
- IRF530 (N-channel) vs IRF9530 (P-channel): returned **0.9** instead of **0.0** ❌
- 2N2222 vs 2N3904 (different current ratings): returned **1.0** instead of **0.547** ❌

**Root Cause**: The assumption that 0.0 meant "calculator couldn't determine similarity" instead of "parts are incompatible".

**Fix** (MPNUtils.java line 132-138):
```java
// BEFORE:
if (applicable1 || applicable2) {
    double similarity = calculator.calculateSimilarity(mpn1, mpn2, patternRegistry);
    if (similarity > 0) {  // ❌ BUG: ignores 0.0
        return similarity;
    }
}

// AFTER:
if (applicable1 || applicable2) {
    double similarity = calculator.calculateSimilarity(mpn1, mpn2, patternRegistry);
    // Trust the calculator's result, even if it's 0.0 (incompatible parts)
    return similarity;  // ✅ FIX: trust all results
}
```

**Lesson Learned**: Always trust calculator results. If a calculator returns 0.0, it means "these parts are incompatible," not "I don't know."

### Bug 2: TransistorSimilarityCalculator getBasePart() Regex Failure

**Discovered**: January 14, 2026
**Severity**: HIGH - Caused all transistors to use default characteristics

**Problem**: The `getBasePart()` method had a broken regex `([A-Z0-9]+)[A-Z].*$` that extracted only the first character. For "2N2222", it matched "2" + "N" + "2222" and captured just **"2"**, causing all `KNOWN_CHARACTERISTICS` lookups to fail.

**Impact**:
- `getBasePart("2N2222")` returned `"2"` instead of `"2N2222"` ❌
- `getBasePart("2N3904")` returned `"2"` instead of `"2N3904"` ❌
- All transistors used default values (600mA, TO-18) instead of actual specs
- Similarity scores were based on defaults, not real characteristics

**Example Impact**:
```
2N2222 specs: 800mA, TO-18 (actual)
2N3904 specs: 200mA, TO-92 (actual)

With bug: Both got 600mA, TO-18 (defaults) → similarity = 1.0 ❌
After fix: Used actual specs → similarity = 0.547 ✅
```

**Fix** (TransistorSimilarityCalculator.java):
```java
// BEFORE:
private String getBasePart(String mpn) {
    if (mpn == null) return "";
    return mpn.replaceAll("([A-Z0-9]+)[A-Z].*$", "$1").toUpperCase();  // ❌ BROKEN
}

// AFTER:
private String getBasePart(String mpn) {
    if (mpn == null) return "";
    String upperMpn = mpn.toUpperCase();
    // Remove known suffixes (package codes, qualifiers)
    return upperMpn.replaceAll("[-](T|TR|TA|TF|G|L|R|Q|X)$", "")
                   .replaceAll("(T|TR|TA|TF|G|L|R|Q|X)$", "")
                   .replaceAll("A$", "");  // ✅ FIXED: preserves base part
}
```

**Lesson Learned**:
1. Regex for part number extraction must preserve the full base part, not just extract pieces
2. Simple suffix stripping is more reliable than complex capture groups
3. Always test helper methods with actual part numbers to verify correct extraction

### Bug 3: OpAmpSimilarityCalculator Intercepting Generic IC Types

**Discovered**: January 14, 2026 (Session 2)
**Severity**: HIGH - Caused logic ICs to receive 0.0 similarity instead of correct scores
**Fixed**: PR #115

**Problem**: `OpAmpSimilarityCalculator.isApplicable()` returned `true` for `ComponentType.IC`, causing it to intercept **ALL ICs** before more specific calculators like `LogicICSimilarityCalculator` could handle them. When it received a non-op-amp IC, it would check `isOpAmp()`, find it wasn't an op-amp, and return 0.0.

**Impact**:
- CD4001 vs CD4001BE: 0.0 instead of 0.9 ❌
- All logic ICs intercepted by OpAmpSimilarityCalculator
- LogicICSimilarityCalculator never reached for logic ICs

**Root Cause - Calculator Registration Order**:
```java
// MPNUtils.java line 37-51
private static final List<ComponentSimilarityCalculator> calculators = Arrays.asList(
        new VoltageRegulatorSimilarityCalculator(),
        new LEDSimilarityCalculator(),
        new OpAmpSimilarityCalculator(),         // ← Claimed ALL ICs here (line 40)
        new LogicICSimilarityCalculator(),        // ← Never reached (line 41)
        new MemorySimilarityCalculator(),
        // ...
);
```

The first applicable calculator wins. OpAmpSimilarityCalculator said "yes, I handle IC type" and returned 0.0 for non-op-amps.

**Execution Flow**:
```java
// MPNUtils.calculateSimilarity()
for (ComponentSimilarityCalculator calculator : calculators) {
    if (calculator.isApplicable(type1) || calculator.isApplicable(type2)) {
        double similarity = calculator.calculateSimilarity(mpn1, mpn2, patternRegistry);
        return similarity;  // FIRST applicable calculator wins!
    }
}
```

**OpAmpSimilarityCalculator Behavior (BEFORE)**:
```java
public boolean isApplicable(ComponentType type) {
    // This intercepted ALL ICs ❌
    if (type == ComponentType.IC || type == ComponentType.ANALOG_IC) {
        return true;
    }
    return type == ComponentType.OPAMP || type.name().startsWith("OPAMP_");
}

public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
    // Check if both are actually op-amps
    if (!isOpAmp(mpn1) || !isOpAmp(mpn2)) {
        return 0.0;  // Returns 0.0 for logic ICs like CD4001
    }
    // ...
}
```

**Fix** (OpAmpSimilarityCalculator.java):
```java
@Override
public boolean isApplicable(ComponentType type) {
    if (type == null) {
        return false;
    }

    // Only handle specific op-amp types to avoid intercepting other IC types
    // (e.g., logic ICs, memory ICs, etc.)
    return type == ComponentType.OPAMP ||
            type == ComponentType.OPAMP_TI ||
            type.name().startsWith("OPAMP_");
}
```

**Test Updates** (OpAmpSimilarityCalculatorTest.java):
```java
// BEFORE:
@Test
void shouldBeApplicableForICTypes() {
    assertTrue(calculator.isApplicable(ComponentType.IC));
    assertTrue(calculator.isApplicable(ComponentType.ANALOG_IC));
}

// AFTER:
@Test
void shouldNotBeApplicableForGenericICTypes() {
    // OpAmpSimilarityCalculator should only handle specific OPAMP types
    assertFalse(calculator.isApplicable(ComponentType.IC));
    assertFalse(calculator.isApplicable(ComponentType.ANALOG_IC));
}
```

**Results**:
- CD4001 vs CD4001BE: 0.0 → 0.9 ✅
- Logic ICs now properly handled by LogicICSimilarityCalculator
- All 13,314 tests passing ✅

**Lesson Learned**:
1. **Calculator order matters** - First applicable calculator wins and returns immediately
2. **Be specific in isApplicable()** - Don't claim generic types unless you handle ALL subtypes
3. **Test ordering matters** - Bug only appeared when running full test suite, not individual tests
4. **Each calculator should own its domain** - Op-amp calculator should only handle op-amps, not all ICs

**Related Issues**:
- This pattern may exist in other calculators (check LEDSimilarityCalculator, MemorySimilarityCalculator, etc.)
- Consider adding validation: if calculator returns 0.0 for parts of claimed type, log warning

### Metadata-Driven Conversion Progress (January 2026)

**Completed Conversions**:
1. ✅ DiodeSimilarityCalculator (PR #110)
2. ✅ MosfetSimilarityCalculator (PR #111)
3. ✅ TransistorSimilarityCalculator (PR #112)
4. ✅ VoltageRegulatorSimilarityCalculator (PR #113)

**Key Benefits Observed**:
1. **More accurate scoring** - Metadata approach produces scores based on actual specs (0.95-0.99 for very similar, 1.0 for identical)
2. **Better differentiation** - Compatible but non-identical parts get appropriate intermediate scores
3. **Proper incompatibility detection** - Critical spec mismatches return 0.0
4. **Threshold-based testing** - Tests use `>= HIGH_SIMILARITY` instead of `assertEquals(0.9, similarity)`

**Conversion Pattern**:
```java
// 1. Extract specs from both MPNs
String spec1 = extractSpec(mpn1);
String spec2 = extractSpec(mpn2);

// 2. Short-circuit check for critical mismatches
if (spec1 != null && spec2 != null && !spec1.equals(spec2)) {
    return 0.0;  // Incompatible
}

// 3. Weighted spec comparison
for each spec {
    specScore = rule.compare(origValue, candValue);
    specWeight = profile.getEffectiveWeight(importance);
    totalScore += specScore * specWeight;
    maxPossibleScore += specWeight;
}
similarity = totalScore / maxPossibleScore;

// 4. Optional boost for known equivalent groups
if (areKnownEquivalents(mpn1, mpn2)) {
    similarity = Math.max(similarity, HIGH_SIMILARITY);
}
```

**Test Expectation Updates**:
- Changed from `assertEquals(0.9, similarity)` to `assertTrue(similarity >= 0.9)`
- Adjusted borderline thresholds from `< 0.5` to `< 0.55` for parts with moderate similarity
- Updated tests to expect moderate scores (0.5-0.7) for parts that share some but not all critical specs

**Next Candidates**: OpAmpSimilarityCalculator, MemorySimilarityCalculator, LEDSimilarityCalculator

<!-- Add new learnings above this line -->
