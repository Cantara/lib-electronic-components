# CLAUDE.md

This file provides guidance to Claude Code when working with this repository.

## Build Commands

```bash
mvn clean install          # Build the project
mvn test                   # Run all tests
mvn test -Dtest=ClassName  # Run single test class
mvn test -Dtest=Class#method  # Run single test method
mvn install -DskipTests    # Skip tests during build
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

**Before committing**: Run `/documentation-maintenance` to update skills and learnings.

## Cost-Effective Task Delegation

Use Haiku (12x cheaper) for straightforward work. See `/task-delegation` skill for details.

| Delegate to Haiku | Use Sonnet Directly |
|-------------------|---------------------|
| Test fixes, simple refactoring | Architectural decisions |
| Documentation updates | Complex debugging |
| Straightforward bug fixes | Ambiguous requirements |
| Pattern application | New handlers/calculators |

## Project Overview

Java 21 library for electronic components:
- **MPN operations**: Normalization, type detection, similarity, manufacturer ID
- **BOM management**: PCBA and mechanical assemblies
- **Component classification**: Passive/active categorization
- **Similarity analysis**: Finding equivalent components

## Architecture

### Core Domain (`no.cantara.electronic.component`)
- `ElectronicPart` - Base class with MPN, value, package, specs
- `BOMEntry` - Component entry in bill of materials
- `PCBABOM` / `MechanicalBOM` - Assembly containers

### Component Library (`no.cantara.electronic.component.lib`)
- `MPNUtils` - Static utilities for MPN operations
- `ComponentType` - ~200 component types with manufacturer variants
- `ComponentManufacturer` - Manufacturer enum with regex patterns
- `ManufacturerHandler` - Interface for manufacturer-specific logic

### Key Patterns
- **Adding manufacturers**: Create handler in `manufacturers/`, add to `ComponentManufacturer` enum
- **Type hierarchy**: Manufacturer types (e.g., `MOSFET_INFINEON`) map to base types via `getBaseType()`
- **MPN flow**: `ComponentManufacturer.fromMPN()` → handler pattern match → `MPNUtils.calculateSimilarity()`

---

## Skills Index

Skills provide detailed guidance for specific domains. Invoke with `/skill-name`.

### Component Skills
| Skill | Purpose |
|-------|---------|
| `/component` | Base skill for general component work |
| `/resistor`, `/capacitor`, `/inductor` | Passive component patterns |
| `/semiconductor` | Diodes, transistors, MOSFETs |
| `/ic` | Microcontrollers, op-amps, regulators |
| `/connector` | Headers, sockets, wire-to-board |
| `/memory` | Flash, EEPROM, SRAM |

### Architecture Skills
| Skill | Purpose |
|-------|---------|
| `/architecture` | Refactoring guidance, duplication hotspots |
| `/handler-pattern-design` | Handler patterns: Set.of(), dual registration, testing |
| `/similarity-calculator-architecture` | Calculator ordering, isApplicable() patterns |
| `/component-type-detection-hierarchy` | Type hierarchy, specificity, getBaseType() |

### Similarity Calculator Skills
| Skill | Purpose |
|-------|---------|
| `/similarity` | Main architecture, interfaces, calculator selection |
| `/similarity-metadata` | Metadata-driven framework (12/17 converted) |
| `/similarity-resistor`, `-capacitor`, `-transistor` | Type-specific calculators |
| `/similarity-mosfet`, `-diode`, `-opamp` | Active component calculators |

### Feature Skills
| Skill | Purpose |
|-------|---------|
| `/lifecycle` | Component obsolescence tracking |
| `/mpn-normalization` | Suffix handling, search variations |
| `/jackson` | Jackson 3.x migration guidance |
| `/task-delegation` | Cost-effective Haiku vs Sonnet usage |
| `/documentation-maintenance` | Pre-PR documentation checklist |

### Manufacturer Skills
Located in `.claude/skills/manufacturers/`:
`/manufacturers/ti`, `/manufacturers/st`, `/manufacturers/microchip`, `/manufacturers/nxp`, etc.

---

## Critical Gotchas

### Handler Implementation
1. **Use `Set.of()` not `HashSet`** in `getSupportedTypes()` - prevents duplicates, deterministic
2. **Register both types** - base type AND manufacturer-specific (e.g., `RESISTOR` and `RESISTOR_CHIP_YAGEO`)
3. **Suffix ordering** - longer suffixes before shorter (e.g., "DT" before "T")
4. **matchesForCurrentHandler()** - prevents cross-handler false positives

### Testing
1. **Package placement** - NEVER put handler tests in `manufacturers` package (classpath shadowing)
2. **Circular initialization** - Use direct instantiation (`new TIHandler()`) in `@BeforeAll`
3. **Flaky tests** - Check for `HashSet` iteration (should be `TreeSet` with comparator)
4. **Full suite vs single test** - Some tests fail alone but pass in suite due to class loading order

### Type Detection
1. **Specificity scoring** - `MPNUtils.getComponentType()` returns most specific type
2. **getBaseType() completeness** - All manufacturer-specific types MUST be in switch statement
3. **First handler wins** - `ManufacturerHandlerFactory` uses TreeSet for deterministic order

---

## Active Technical Debt

**Medium Priority**:
- Some handlers have commented-out patterns in `ComponentManufacturer.java` - unclear if deprecated
- `MPNUtils.getManufacturerHandler` relies on alphabetical handler order

**Low Priority**:
- 2 handlers missing test coverage: AbstractManufacturerHandler (base class), UnknownHandler (special case)

---

## Recording Learnings

When you discover patterns or quirks:
1. **General learnings** → Add to this file under appropriate section
2. **Component-specific** → Add to relevant skill in `.claude/skills/`
3. **Fixed bugs** → Archive in `.docs/history/FIXED_BUGS.md`

---

## Historical Documentation

- **HISTORY.md** - Chronological timeline of PRs and milestones
- **.docs/history/** - Detailed analysis of implementations
  - `FIXED_BUGS.md` - All fixed bugs (archived)
  - `BUG_FIX_ANALYSIS.md` - Critical bug analysis
  - `HANDLER_IMPLEMENTATION_PATTERNS.md` - Handler patterns
