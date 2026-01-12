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
- `/architecture` - **Refactoring and cleanup guidance** (critical issues, duplication hotspots)

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
- **Test coverage gap**: 50+ handlers and 20+ similarity calculators have no dedicated tests
- **Flaky tests**: If tests pass individually but fail in suite, check for non-deterministic iteration (HashSet → TreeSet)

### Handler Test Gotchas (IMPORTANT)

**Classpath Shadowing**:
- NEVER put handler tests in the `manufacturers` package (e.g., `no.cantara.electronic.component.lib.manufacturers`)
- Test-classes directory shadows main-classes directory, causing `ManufacturerHandlerFactory` to find 0 handlers
- Solution: Put handler tests in a different package (e.g., `no.cantara.electronic.component.lib.handlers`)

**Circular Initialization**:
- NEVER use `new TIHandler()` or similar direct instantiation in tests
- Causes `ExceptionInInitializerError` due to circular deps: ComponentType ↔ ComponentManufacturer ↔ Handler
- Solution: Use `@BeforeAll` with `MPNUtils.getManufacturerHandler("LM358")` to get handler instance

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
- Test coverage: 31 handlers now have comprehensive tests (1821+ total tests)
- Use existing handler tests as templates: TIHandlerTest, STHandlerTest, NXPHandlerTest, etc.

*BroadcomHandler (2 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- getSupportedTypes() returns empty set despite having IC patterns

*SiliconLabsHandler (1 bug)*:
- HashSet in getSupportedTypes() - should use Set.of()

*AVXHandler (2 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- Ceramic package extraction reads positions 2-3 after 08/12 prefix (e.g., "0805" extracts "05" not "0805")

*TEHandler (1 bug)*:
- HashSet in getSupportedTypes() - should use Set.of()

*AmphenolHandler (1 bug)*:
- HashSet in getSupportedTypes() - should use Set.of()

*NichiconHandler (1 bug)*:
- HashSet in getSupportedTypes() - should use Set.of()

*WurthHandler (2 bugs)*:
- HashSet in getSupportedTypes() - should use Set.of()
- HEADER_PATTERN only matches 61xxx (pin headers), not 62xxx (socket headers) - series extraction returns empty for socket headers

*MicronHandler (1 bug)*:
- HashSet in getSupportedTypes() - should use Set.of()

### Architecture Notes
- `PatternRegistry` supports multi-handler per ComponentType but this is largely unused
- Similarity calculators registered in `MPNUtils` static initializer (lines 34-48)
- `ManufacturerHandlerFactory` uses reflection-based classpath scanning with TreeSet for deterministic order

### Recent Infrastructure (PR #74)
- **`PackageCodeRegistry`** - Centralized package code mappings (PU→PDIP, AU→TQFP, etc.)
- **`AbstractManufacturerHandler`** - Base class with `extractSuffixAfterHyphen()`, `extractTrailingSuffix()`, `findFirstDigitIndex()`, `findLastDigitIndex()` helpers

<!-- Add new learnings above this line -->
