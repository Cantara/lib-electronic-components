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
- **Test coverage**: 41+ handlers have tests (2613+ tests), 17 similarity calculators have tests (302 tests)
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

**Fixed (PR #74, #75, #76, #77, #78, #80, #81)**:
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

**Documented Handler Bugs (11 handlers audited, 880+ tests)**:

*NXPHandler (8 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Missing MEMORY type despite memory patterns (MX25, S25FL)
- Missing pressure sensor patterns (MPX series)
- Package extraction incomplete (only handles LPC)
- Series extraction incomplete for audio codecs, sensors
- Cross-handler pattern matching possible (falls through to registry)

*InfineonHandler (5 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Missing Cypress PSoC specific patterns (acquired company)
- Package extraction only handles IRF MOSFETs, not BSC/IFX
- Speed grade suffix handling incomplete

*MicrochipHandler (5 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Missing AVR32 patterns (legacy acquired from Atmel)
- Package extraction returns raw suffix, not decoded package name
- Some dsPIC patterns overlap with PIC patterns

*OnSemiHandler (9 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Missing NTD/FQP/FDP MOSFET patterns
- Missing 2N/MMBT/MPSA transistor patterns
- Missing NCP regulator patterns
- Package extraction only handles diodes, not MOSFETs/transistors
- Series extraction incomplete for MOSFETs/transistors

*AnalogDevicesHandler (5 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Missing AD7xxx ADC and AD5xxx DAC patterns with manufacturer-specific types
- ADCs/DACs fall through to generic IC instead of ADC_AD/DAC_AD
- Package extraction doesn't handle grade+package+rohs structure

*MaximHandler (6 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Missing MAX232/MAX485 interface IC patterns
- Missing MAX6675 thermocouple patterns
- Missing MAX17xxx power management patterns
- Package extraction only handles DS18B20
- Series extraction limited to DS18B20 and MAX6xxx

*RenesasHandler (5 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Package extraction returns too much (includes version suffix)
- RL78 pattern subset of RX pattern (overlapping)
- Version suffix (#30, #V1) breaks package extraction

*ToshibaHandler (7 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Missing TRANSISTOR type in getSupportedTypes() despite 2SC/2SA patterns
- Missing base IC type in getSupportedTypes()
- Package extraction missing TK/TLP/SSM/TB/2SC series
- Series extraction missing TLP/2SC/2SA/TB series
- Missing 2SK JIS N-channel MOSFET patterns
- Missing RN/RP digital transistor patterns

**Medium**:
- Some handlers have commented-out patterns in `ComponentManufacturer.java` - unclear if deprecated
- `MPNUtils.getManufacturerHandler` relies on alphabetical handler order - could be fragile

**Low**:
- Test coverage: 41 handlers now have comprehensive tests (2613+ total tests)
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

## Codebase Analysis Findings (January 2026)

### Test Coverage Status

| Category | Total | With Tests | Without Tests | Coverage |
|----------|-------|------------|---------------|----------|
| Handlers | 56 | 41 | 15 | 73.2% |
| Similarity Calculators | 17 | 17 | 0 | 100% |

**Handlers Without Tests (15)**: Abracon, AKM, Cree, DiodesInc, Epson, Fairchild, IQD, LG, Lumileds, NDK, Nexteria, OSRAM, Qualcomm, Spansion, Unknown

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

<!-- Add new learnings above this line -->
