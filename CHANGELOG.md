# Changelog

All notable changes to lib-electronic-components will be documented in this file.

For detailed technical notes, implementation patterns, bug analyses, and learnings, see:
- **[HISTORY.md](HISTORY.md)** - Chronological timeline with technical details
- **[.docs/history/](.docs/history/)** - Deep dives into bugs, handler patterns, and architectural decisions

## [1.0.0] - 2026-01-11

First major release of lib-electronic-components library.

### Features

#### Manufacturer Handler Testing Framework
- Added comprehensive test suite for manufacturer handlers using parameterized tests
- Created manufacturer-agent skill for automated handler test generation
- Added handler tests for 20+ manufacturers:
  - **Batch 1**: TI, Atmel, ST
  - **Batch 2**: NXP, Infineon, Microchip, Maxim, ON Semiconductor
  - **Batch 3**: Murata, Vishay, Panasonic, Kemet, Yageo, Samsung, TDK, Rohm, Nordic, Espressif

#### New Skills and Documentation
- Added manufacturer skills in `.claude/skills/manufacturers/` for:
  - TI, Atmel, NXP, Infineon, Microchip, Maxim, ON Semiconductor
  - Murata, Vishay, Panasonic, Kemet, Yageo, Samsung, TDK, Rohm, Nordic, Espressif
- Added architecture skill with refactoring guidance
- Added Claude Code documentation and component skills
- Added Git workflow policy documentation
- Added handler test learnings and cleanup checklist

#### MPN Utilities
- Added `MPNUtils.findMPNInText()` for extracting MPNs from text strings
- Improved package code extraction across all handlers
- Enhanced component type detection

### Bug Fixes

- **Fixed circular dependency in EspressifHandler** - Removed `EspressifComponentType.values()` loop that caused initialization issues
- **Fixed deterministic handler ordering** - Resolved flaky tests caused by non-deterministic handler iteration
- **Fixed TIHandler suffix ordering bug** - Corrected package code extraction for TI components
- **Fixed ComponentType.getManufacturer()** - Added explicit mapping to avoid fragile string matching

### Refactoring

- Cleaned up TIHandler and introduced PackageCodeRegistry
- Reduced technical debt in package code extraction and type detection
- Improved handler initialization patterns

### Dependencies

Updated to latest versions:
- JUnit Jupiter 6.0.2
- Jackson 2.20.1
- Guava 33.5.0-jre
- Commons IO 2.21.0
- Commons Compress 1.28.0
- SLF4J 2.0.17
- ModelMapper 3.2.6
- Maven Compiler Plugin 3.14.1
- Maven Surefire Plugin 3.5.4

### Test Improvements

- 1414 tests total (up from ~500 in 0.8.x)
- All manufacturer handlers now have comprehensive test coverage
- Tests use documentation pattern (System.out.println) for behavior that varies
- Tests use assertions for stable methods (extractPackageCode, extractSeries, null handling)

---

## [0.8.1] - Previous Release

See git history for changes prior to 1.0.0.
