# Fixed Bugs Archive

This document archives all bugs that have been identified and fixed in the lib-electronic-components project. These are kept for historical reference.

---

## Handler Bugs (Fixed in PRs #74-87)

### TIHandler (PR #77)
- ~~Duplicate COMPONENT_SERIES entries~~ - Consolidated, removed ~170 lines of duplicates
- ~~HashSet in getSupportedTypes()~~ - Changed to Set.of()
- ~~Suffix ordering bug (DT vs T)~~ - Longer suffixes checked first
- ~~Unused `TIHandlerPatterns.java`~~ - Deleted

### AtmelHandler (PR #78)
- ~~Multi-pattern matching bug~~ - Uses registry.matches() now
- ~~Speed grade in package extraction~~ - Strips leading digits
- ~~AT25 pattern too restrictive~~ - Now allows 0-2 letters
- ~~Cross-handler pattern matching~~ - Don't fall through for base MICROCONTROLLER type

### STHandler (PR #80)
- ~~HashSet in getSupportedTypes()~~ - Changed to Set.of()
- ~~Package extraction bugs~~ - Fixed STM32 (returns LQFP not T6), MOSFET, regulators
- ~~Multi-pattern matching bug~~ - Added explicit type checks

### InfineonHandler (PR #81)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Package extraction not working~~ - Fixed
- ~~Series extraction order issues~~ - Fixed
- ~~Missing XMC/OptiMOS patterns~~ - Added

### MicrochipHandler (PR #81)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Missing AVR32 patterns~~ - Added
- ~~Package extraction not decoding properly~~ - Fixed

### AnalogDevicesHandler (PR #81)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Missing ADC/DAC patterns~~ - Added
- ~~Package extraction incomplete~~ - Comprehensive now

### NXPHandler (115 tests passing)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Missing MX25 and S25FL memory patterns~~ - Added with MEMORY type
- ~~Missing MPX pressure sensor patterns~~ - Added
- ~~Package extraction incomplete~~ - Comprehensive (LPC, Kinetis, iMX, MOSFETs, transistors)
- ~~Series extraction incomplete~~ - Complete (audio codecs, sensors, all product lines)
- ~~Cross-handler matching issues~~ - Uses matchesForCurrentHandler()

### OnSemiHandler (119 tests passing)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Missing NTD/FQP/FDP MOSFET patterns~~ - Added
- ~~Missing 2N/MMBT/MPSA transistor patterns~~ - Added
- ~~Missing NCP regulator patterns~~ - Added
- ~~Package extraction incomplete~~ - Handles diodes, MOSFETs, and transistors
- ~~Series extraction incomplete~~ - Complete for all product lines

### MaximHandler (60 tests passing)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Missing interface IC patterns~~ - Comprehensive now
- ~~Missing thermocouple patterns~~ - Added
- ~~Missing power management patterns~~ - Added

### RenesasHandler (110 tests passing)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Package extraction version suffix issues~~ - Properly handles now
- ~~Pattern overlaps~~ - Resolved
- ~~Version suffix handling incorrect~~ - Fixed

### ToshibaHandler (116 tests passing)
- ~~HashSet in getSupportedTypes()~~ - Uses Set.of()
- ~~Missing TRANSISTOR type in getSupportedTypes()~~ - Added
- ~~Missing IC type in getSupportedTypes()~~ - Added
- ~~Missing 2SK patterns~~ - Added
- ~~Missing digital transistor patterns~~ - Added

---

## Batch Fixes (PR #85-87)

### Batch 5 Handlers
- **BroadcomHandler**: Fixed - uses Set.of(), includes IC type
- **SiliconLabsHandler**: Fixed - uses Set.of(), includes all MCU, IC, CRYSTAL, OSCILLATOR types
- **AVXHandler**: Fixed - uses Set.of(); Note: Ceramic package extraction reads positions 2-3 after 08/12 prefix (design choice, not bug)
- **TEHandler**: Fixed - uses Set.of()
- **AmphenolHandler**: Fixed - uses Set.of()
- **NichiconHandler**: Fixed - uses Set.of()
- **WurthHandler**: Fixed - uses Set.of(), HEADER_PATTERN now matches both 61xxx and 62xxx
- **MicronHandler**: Fixed - uses Set.of()

### Batch 6 Handlers (PR #87)
- **MolexHandler**: Fixed - uses Set.of()
- **HiroseHandler**: Fixed - uses Set.of()
- **JSTHandler**: Fixed - uses Set.of()
- **WinbondHandler**: Fixed - uses Set.of()
- **ISSIHandler**: Fixed - uses Set.of(), includes IC for LED drivers
- **QorvoHandler**: Fixed - uses Set.of(), includes IC
- **SkyworksHandler**: Fixed - uses Set.of(), includes IC and MICROCONTROLLER
- **BoschHandler**: Fixed - uses Set.of()
- **InvSenseHandler**: Fixed - uses Set.of(), includes IC for audio/motion processors
- **MelexisHandler**: Fixed - uses Set.of()

---

## Infrastructure Fixes (PR #74-76)

- ~~No AbstractManufacturerHandler base class~~ - Created with shared helper methods
- ~~Package code mappings duplicated across handlers~~ - Created `PackageCodeRegistry` with centralized mappings
- ~~Flaky tests due to handler iteration order~~ - Fixed with deterministic TreeSet ordering in ManufacturerHandlerFactory
- ~~`ComponentType.getManufacturer()` fragile string matching~~ - Fixed with explicit suffix→enum mapping

---

## Summary

**All Handler Bugs Fixed!**
- 8+ major handlers refactored
- 12,632+ tests passing
- 65/67 handlers have comprehensive tests (97.1% coverage)
- Missing test coverage only for: AbstractManufacturerHandler (base class), UnknownHandler (special case)

Last updated: January 2026
