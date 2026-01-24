# Component Type Detection Hierarchy

Use this skill when working with ComponentType enum, type detection from MPNs, specificity scoring, or getBaseType() mapping.

## Specificity Levels

The ~450 component types are organized into 4 specificity levels:

### Level 4: Manufacturer-Specific Types (Most Specific)

**Pattern:** `{BASE_TYPE}_{MANUFACTURER}`

**Examples:**
- `MOSFET_INFINEON` → IRF530, IRFZ44
- `OPAMP_TI` → LM358, LM324, TL072
- `MICROCONTROLLER_ST` → STM32F103, STM8S103
- `VOLTAGE_REGULATOR_LINEAR_TI` → LM7805, LM317
- `CAPACITOR_CERAMIC_MURATA` → GRM188, GRM31

**Specificity score:** 150 points

**Use when:**
- Need manufacturer-specific characteristics
- Comparing parts from same manufacturer
- Manufacturer has unique MPN patterns

---

### Level 3: Base Types (Moderately Specific)

**Examples:**
- `MOSFET` (base for all MOSFET variants)
- `OPAMP` (base for all op-amp variants)
- `MICROCONTROLLER` (base for all MCU variants)
- `RESISTOR` (base for all resistor variants)
- `CAPACITOR` (base for all capacitor variants)

**Specificity score:** 100 points

**Use when:**
- Generic component search
- Cross-manufacturer comparison
- Handler registration (always register both base + specific)

---

### Level 2: Category Types (Generic)

**Examples:**
- `IC` (generic integrated circuit)
- `ANALOG_IC` (analog integrated circuits)
- `SEMICONDUCTOR` (generic semiconductor)
- `PASSIVE` (passive components)
- `SENSOR` (generic sensors)

**Specificity score:** 50 points

**Use when:**
- High-level classification
- Broad searches
- Inventory categorization

---

### Level 1: Unknown/Generic (Least Specific)

**Examples:**
- `UNKNOWN` (could not determine type)
- `COMPONENT` (generic component)

**Specificity score:** -50 points

**Use when:**
- Type detection failed
- Generic placeholder

---

## getBaseType() Mapping

**Purpose:** Map manufacturer-specific types back to base types for cross-manufacturer comparison.

**Implementation pattern (ComponentType.java):**
```java
public ComponentType getBaseType() {
    return switch (this) {
        // Resistors
        case RESISTOR_CHIP_YAGEO,
             RESISTOR_CHIP_KOA,
             RESISTOR_CHIP_VISHAY,
             RESISTOR_CHIP_PANASONIC -> RESISTOR;

        // MOSFETs
        case MOSFET_INFINEON,
             MOSFET_ST,
             MOSFET_NXP,
             MOSFET_ON_SEMI -> MOSFET;

        // Op-Amps
        case OPAMP_TI,
             OPAMP_ANALOG_DEVICES,
             OPAMP_ST,
             OPAMP_NXP -> OPAMP;

        // Microcontrollers
        case MICROCONTROLLER_ATMEL,
             MICROCONTROLLER_ST,
             MICROCONTROLLER_TI,
             MICROCONTROLLER_NXP,
             MICROCONTROLLER_MICROCHIP -> MICROCONTROLLER;

        // Voltage Regulators
        case VOLTAGE_REGULATOR_LINEAR_TI,
             VOLTAGE_REGULATOR_LINEAR_ST,
             VOLTAGE_REGULATOR_SWITCHING_TI -> VOLTAGE_REGULATOR;

        // If not manufacturer-specific, return self
        default -> this;
    };
}
```

**CRITICAL:** All manufacturer-specific types MUST be in the switch statement. Missing types fall through to `default -> this`, returning the specific type instead of the base type.

### Verification Test Pattern

```java
@Test
void allManufacturerSpecificTypesShouldHaveBaseType() {
    for (ComponentType type : ComponentType.values()) {
        if (type.name().contains("_")) {
            // Manufacturer-specific type
            ComponentType base = type.getBaseType();
            assertNotEquals(type, base,
                type + " should map to a base type, not itself");
            assertFalse(base.name().contains("_"),
                base + " should be a base type without manufacturer suffix");
        }
    }
}
```

---

## Type Detection from MPN

### Detection Flow

```
1. MPN Input
   ↓
2. ComponentManufacturer.fromMPN(mpn)
   ↓
3. manufacturer.getHandler()
   ↓
4. handler.matches(mpn, type, patterns)
   ↓
5. Return matched type with highest specificity
```

**Example:**
```java
String mpn = "LM358N";

// Step 1: Detect manufacturer
ComponentManufacturer mfr = ComponentManufacturer.fromMPN("LM358N");
// → TEXAS_INSTRUMENTS

// Step 2: Get handler
ManufacturerHandler handler = mfr.getHandler();
// → TIHandler

// Step 3: Check all types, score by specificity
Map<ComponentType, Integer> matches = new HashMap<>();
for (ComponentType type : ComponentType.values()) {
    if (handler.matches(mpn, type, registry)) {
        matches.put(type, getSpecificityScore(type));
    }
}

// Matches:
// IC                → 50 points
// ANALOG_IC        → 50 points
// OPAMP            → 100 points
// OPAMP_TI         → 150 points (WINNER!)

// Step 4: Return highest specificity
return OPAMP_TI;
```

### Specificity Scoring (ComponentTypeDetector.java)

```java
private int getSpecificityScore(ComponentType type) {
    String name = type.name();

    // Level 4: Manufacturer-specific (contains underscore)
    if (name.contains("_")) {
        // But not generic types like ANALOG_IC, DIGITAL_IC
        if (!name.equals("ANALOG_IC") &&
            !name.equals("DIGITAL_IC") &&
            !name.equals("LOGIC_IC")) {
            return 150;
        }
    }

    // Level 3: Base types
    if (type == ComponentType.RESISTOR ||
        type == ComponentType.CAPACITOR ||
        type == ComponentType.MOSFET ||
        type == ComponentType.OPAMP) {
        return 100;
    }

    // Level 2: Category types
    if (type == ComponentType.IC ||
        type == ComponentType.ANALOG_IC ||
        type == ComponentType.SEMICONDUCTOR) {
        return 50;
    }

    // Level 1: Unknown/generic
    if (type == ComponentType.UNKNOWN) {
        return -50;
    }

    return 0;
}
```

---

## Why Specificity Scoring Matters

**Without scoring (using HashSet iteration):**
```java
// LM358N could match any of these:
Set<ComponentType> matches = Set.of(
    ComponentType.IC,         // Generic
    ComponentType.ANALOG_IC,  // Generic
    ComponentType.OPAMP,      // Base
    ComponentType.OPAMP_TI    // Specific
);

// HashSet iteration is UNPREDICTABLE!
// Could return IC, ANALOG_IC, OPAMP, or OPAMP_TI randomly!
```

**With scoring (returns most specific):**
```java
// LM358N matches:
// IC         →  50 points
// ANALOG_IC  →  50 points
// OPAMP      → 100 points
// OPAMP_TI   → 150 points ← WINNER!

// Always returns OPAMP_TI (most specific)
```

---

## Type Flags

**Purpose:** Quick classification without pattern matching.

**Implementation (ComponentType enum):**
```java
public enum ComponentType {
    // Passive components
    RESISTOR(true, false),
    CAPACITOR(true, false),
    INDUCTOR(true, false),

    // Semiconductors
    MOSFET(false, true),
    TRANSISTOR(false, true),
    DIODE(false, true),

    // ICs (not semiconductors for our classification)
    IC(false, false),
    OPAMP(false, false),
    MICROCONTROLLER(false, false);

    private final boolean isPassive;
    private final boolean isSemiconductor;

    ComponentType(boolean isPassive, boolean isSemiconductor) {
        this.isPassive = isPassive;
        this.isSemiconductor = isSemiconductor;
    }

    public boolean isPassive() {
        return isPassive;
    }

    public boolean isSemiconductor() {
        return isSemiconductor;
    }
}
```

**Usage:**
```java
if (type.isPassive()) {
    // Handle resistors, capacitors, inductors
}

if (type.isSemiconductor()) {
    // Handle diodes, transistors, MOSFETs
}
```

---

## Component Type Statistics

**Total types:** ~450

**By specificity:**
- Manufacturer-specific (Level 4): ~250 types
- Base types (Level 3): ~80 types
- Category types (Level 2): ~15 types
- Generic (Level 1): ~5 types

**Top 10 types by handler registration:**
1. RESISTOR (65+ handlers register patterns)
2. CAPACITOR (60+ handlers)
3. IC (50+ handlers)
4. MOSFET (30+ handlers)
5. MICROCONTROLLER (25+ handlers)
6. DIODE (20+ handlers)
7. TRANSISTOR (18+ handlers)
8. LED (15+ handlers)
9. OPAMP (12+ handlers)
10. VOLTAGE_REGULATOR (10+ handlers)

---

## Learnings & Quirks

### getBaseType() Completeness is Critical
Missing manufacturer-specific types in the switch statement cause subtle bugs. The type returns itself instead of the base type, breaking cross-manufacturer comparison.

### Specificity Scoring Prevents Non-Determinism
Without scoring, HashSet iteration could return any matching type randomly. Scoring ensures the most specific type always wins.

### Type Flags Are Fast
`isPassive()` and `isSemiconductor()` are O(1) boolean checks, much faster than pattern matching or instanceof checks.

### Category Types Are Ambiguous
`IC` can mean many things (op-amp, MCU, memory, logic IC). Prefer base types (OPAMP, MICROCONTROLLER) or specific types (OPAMP_TI) for clarity.

---

## See Also

- `/component` - Base skill for general component work
- `/similarity` - How types are used in similarity calculation
- `/handler-pattern-design` - How handlers register patterns for types
- `ComponentType.java` (873 lines) - Full enum definition
- `ComponentTypeDetector.java` - Type detection logic
