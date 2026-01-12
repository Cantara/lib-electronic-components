# Component Lifecycle Tracking Skill

This skill provides guidance for working with component lifecycle tracking in the lib-electronic-components library.

## Overview

Component lifecycle tracking helps manage obsolescence risk by tracking:
- Current manufacturing status (Active, NRFND, Last Time Buy, Obsolete, EOL)
- Important dates (Last Time Buy deadline, End of Life date)
- Replacement part suggestions with compatibility levels
- Status change history for audit trail

## Core Classes

### ComponentLifecycleStatus (Enum)

```java
ACTIVE          // In production, available
NRFND           // Not Recommended For New Designs
LAST_TIME_BUY   // Final order window
OBSOLETE        // No longer manufactured
EOL             // Complete end of life
UNKNOWN         // Status not determined
```

**Key Methods:**
- `isAvailable()` - Returns true for ACTIVE, NRFND, LAST_TIME_BUY
- `shouldAvoidForNewDesigns()` - Returns true for all except ACTIVE and UNKNOWN
- `requiresAttention()` - Returns true for LAST_TIME_BUY, OBSOLETE, EOL

### ComponentLifecycle (Class)

**Fields:**
- `status` - Current lifecycle status
- `statusDate` - When status was set/verified
- `lastTimeBuyDate` - LTB deadline (if applicable)
- `endOfLifeDate` - EOL date
- `replacementParts` - List of suggested replacements
- `source` - Where lifecycle info came from
- `notes` - Additional notes
- `statusHistory` - Audit trail of status changes

**Factory Methods:**
```java
ComponentLifecycle.active()                              // Creates ACTIVE status
ComponentLifecycle.lastTimeBuy(LocalDate deadline)       // Creates LTB with deadline
ComponentLifecycle.obsoleteWithReplacement(mpn, mfr)     // Creates OBSOLETE with replacement
```

**Key Methods:**
```java
isAvailable()                    // Status allows purchasing
shouldAvoidForNewDesigns()       // Status indicates avoid for new designs
requiresAttention()              // Status needs action (LTB/OBSOLETE/EOL)
isLtbApproaching(int days)       // LTB deadline within threshold
isLtbExpired()                   // LTB deadline has passed
getPrimaryReplacement()          // First replacement in list
getReplacementsByCompatibility() // Filter by compatibility level
```

### ReplacementPart (Inner Class)

**Fields:**
- `mpn` - Replacement part number
- `manufacturer` - Replacement manufacturer
- `compatibility` - Compatibility level enum
- `notes` - Additional notes

**CompatibilityLevel Enum:**
```java
FORM_FIT_FUNCTION      // Drop-in replacement
FUNCTIONAL_EQUIVALENT  // Same function, different form
SIMILAR                // Similar specs, may need changes
CROSS_REFERENCE        // Equivalent from different manufacturer
UPGRADE                // Better specs than original
```

### StatusChange (Inner Class)

Records status transitions for audit:
- `fromStatus` - Previous status
- `toStatus` - New status
- `changeDate` - When change occurred
- `reason` - Optional reason for change

## Usage Patterns

### Adding Lifecycle to ElectronicPart

```java
ElectronicPart part = new ElectronicPart()
    .setMpn("STM32F407VGT6")
    .setManufacturer("STMicroelectronics")
    .setLifecycle(ComponentLifecycle.active());
```

### Adding Lifecycle to BOMEntry

```java
BOMEntry entry = new BOMEntry()
    .setMpn("MC34063ADR")
    .setManufacturer("Texas Instruments")
    .setQty("10")
    .setLifecycle(ComponentLifecycle.lastTimeBuy(LocalDate.of(2024, 9, 30)));
```

### Checking Lifecycle Risk

```java
// Simple check
if (part.isLifecycleAtRisk()) {
    System.out.println("Component needs replacement planning");
}

// Detailed check
ComponentLifecycleStatus status = part.getLifecycleStatus();
switch (status) {
    case ACTIVE -> System.out.println("OK");
    case NRFND -> System.out.println("Consider alternatives for new designs");
    case LAST_TIME_BUY -> {
        if (part.getLifecycle().isLtbApproaching(90)) {
            System.out.println("URGENT: LTB deadline within 90 days");
        }
    }
    case OBSOLETE, EOL -> System.out.println("CRITICAL: Find replacement");
    case UNKNOWN -> System.out.println("Update lifecycle info");
}
```

### Managing Replacements

```java
ComponentLifecycle lifecycle = new ComponentLifecycle(ComponentLifecycleStatus.OBSOLETE);

// Add drop-in replacement
lifecycle.addReplacementPart("NEW-001", "Same Corp",
    ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION);

// Add cross-reference
lifecycle.addReplacementPart("ALT-001", "Other Corp",
    ReplacementPart.CompatibilityLevel.CROSS_REFERENCE);

// Add upgrade
ReplacementPart upgrade = new ReplacementPart()
    .setMpn("BETTER-001")
    .setManufacturer("Premium Corp")
    .setCompatibility(ReplacementPart.CompatibilityLevel.UPGRADE)
    .setNotes("2x the current rating");
lifecycle.addReplacementPart(upgrade);

// Get primary (first) replacement
ReplacementPart primary = lifecycle.getPrimaryReplacement();

// Get only drop-in replacements
List<ReplacementPart> dropIns = lifecycle.getReplacementsByCompatibility(
    ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION);
```

### Tracking Status History

```java
ComponentLifecycle lifecycle = ComponentLifecycle.active();

// Status changes are automatically recorded
lifecycle.setStatus(ComponentLifecycleStatus.NRFND);
lifecycle.setStatus(ComponentLifecycleStatus.LAST_TIME_BUY);

// Review history
for (StatusChange change : lifecycle.getStatusHistory()) {
    System.out.printf("%s: %s → %s%n",
        change.getChangeDate(),
        change.getFromStatus(),
        change.getToStatus());
}
```

## JSON Serialization

Lifecycle data serializes cleanly with Jackson 3:

```java
JsonMapper mapper = JsonMapper.builder().build();
String json = mapper.writeValueAsString(part);
ElectronicPart deserialized = mapper.readValue(json, ElectronicPart.class);
```

Output format:
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

## BOM Analysis Example

```java
public class BOMLifecycleAnalyzer {

    public void analyzeRisk(BOM bom) {
        List<BOMEntry> atRisk = bom.getBomEntries().stream()
            .filter(ElectronicPart::isLifecycleAtRisk)
            .toList();

        if (!atRisk.isEmpty()) {
            System.out.println("Components requiring attention:");
            for (BOMEntry entry : atRisk) {
                ComponentLifecycle lc = entry.getLifecycle();
                System.out.printf("  %s (%s): %s%n",
                    entry.getMpn(),
                    entry.getManufacturer(),
                    lc.getStatus().getDisplayName());

                if (lc.getLastTimeBuyDate() != null) {
                    System.out.printf("    LTB Deadline: %s%n", lc.getLastTimeBuyDate());
                }

                ReplacementPart primary = lc.getPrimaryReplacement();
                if (primary != null) {
                    System.out.printf("    Suggested: %s (%s)%n",
                        primary.getMpn(), primary.getCompatibility().getDisplayName());
                }
            }
        }
    }
}
```

## Gotchas and Best Practices

### 1. Always Check for Null Lifecycle

```java
// BAD - NPE if lifecycle is null
if (part.getLifecycle().getStatus() == ComponentLifecycleStatus.OBSOLETE) { ... }

// GOOD - Use convenience method
if (part.getLifecycleStatus() == ComponentLifecycleStatus.OBSOLETE) { ... }

// GOOD - Check first
if (part.hasLifecycleInfo()) {
    ComponentLifecycle lc = part.getLifecycle();
    // ...
}
```

### 2. Use Appropriate LTB Thresholds

```java
// Common thresholds
lifecycle.isLtbApproaching(30);   // Urgent - 1 month
lifecycle.isLtbApproaching(90);   // Warning - 3 months
lifecycle.isLtbApproaching(180);  // Planning - 6 months
```

### 3. Add Replacements in Priority Order

```java
// First added = primary replacement
lifecycle.addReplacementPart("BEST-001", ...);   // getPrimaryReplacement() returns this
lifecycle.addReplacementPart("ALT-001", ...);
lifecycle.addReplacementPart("ALT-002", ...);
```

### 4. Always Set Source Attribution

```java
lifecycle.setSource("Manufacturer PCN #12345");
lifecycle.setSource("DigiKey obsolescence notice");
lifecycle.setSource("Manual review 2026-01-12");
```

### 5. Status Changes Auto-Record History

```java
// Each setStatus() call automatically adds to statusHistory
// No need to manually track changes
lifecycle.setStatus(ComponentLifecycleStatus.NRFND);  // Recorded
lifecycle.setStatus(ComponentLifecycleStatus.LTB);    // Recorded
```

### 6. UNKNOWN vs Null

```java
// UNKNOWN status means "we have lifecycle data but status is unknown"
new ComponentLifecycle()  // status defaults to UNKNOWN

// Null lifecycle means "no lifecycle data at all"
part.getLifecycle()  // may return null

// getLifecycleStatus() safely handles both
part.getLifecycleStatus()  // returns UNKNOWN if lifecycle is null
```

## Test Examples

See `ComponentLifecycleTest.java` for comprehensive test coverage including:
- Status enum behavior tests
- Factory method tests
- LTB deadline detection tests
- Replacement management tests
- Status history tracking tests
- JSON serialization round-trip tests

## Related Files

- `ComponentLifecycleStatus.java` - Status enum
- `ComponentLifecycle.java` - Main lifecycle class with inner classes
- `ElectronicPart.java` - Base class with lifecycle field
- `BOMEntry.java` - BOM entry with lifecycle inheritance
- `ComponentLifecycleTest.java` - Unit tests

## Learnings & Quirks

### January 2026

- **CompatibilityLevel enum requires constructor**: When defining enum values with parameters, must include constructor and fields
- **Status history auto-recording**: `setStatus()` automatically adds to history, no manual tracking needed
- **Factory methods set statusDate**: `active()`, `lastTimeBuy()`, `obsoleteWithReplacement()` all set current date
- **getLifecycleStatus() is null-safe**: Returns UNKNOWN if lifecycle is null, prevents NPE

<!-- Add new learnings above this line -->
