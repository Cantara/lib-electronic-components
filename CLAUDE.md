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

### Known Technical Debt

**Fixed (PR #74, #75)**:
- ~~TIHandler duplicate COMPONENT_SERIES entries~~ - Consolidated, removed ~170 lines of duplicates
- ~~No AbstractManufacturerHandler base class~~ - Created with shared helper methods
- ~~Package code mappings duplicated~~ - Created `PackageCodeRegistry` with centralized mappings
- ~~Flaky tests due to handler order~~ - Fixed with deterministic TreeSet ordering

**Medium**:
- `ComponentType.getManufacturer()` method (lines 497-507) returns incorrect results for some types
- Some handlers have commented-out patterns in `ComponentManufacturer.java` - unclear if deprecated
- `TIHandlerPatterns.java` exists but is unused - can be deleted (patterns now in TIHandler)

**Low**:
- Test coverage gaps: 50+ handlers and 20+ similarity calculators have no dedicated tests

### Architecture Notes
- `PatternRegistry` supports multi-handler per ComponentType but this is largely unused
- Similarity calculators registered in `MPNUtils` static initializer (lines 34-48)
- `ManufacturerHandlerFactory` uses reflection-based classpath scanning with TreeSet for deterministic order

### Recent Infrastructure (PR #74)
- **`PackageCodeRegistry`** - Centralized package code mappings (PU→PDIP, AU→TQFP, etc.)
- **`AbstractManufacturerHandler`** - Base class with `extractSuffixAfterHyphen()`, `extractTrailingSuffix()`, `findFirstDigitIndex()`, `findLastDigitIndex()` helpers

<!-- Add new learnings above this line -->
