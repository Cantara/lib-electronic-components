# Similarity Calculator Architecture

Use this skill when working with similarity calculators, understanding calculator registration order, debugging calculator selection issues, or adding new similarity calculators.

## Calculator Registration Order

### Registration List (MPNUtils.java lines 37-51)

Calculators are registered in specific order. **First applicable calculator wins and returns immediately** - no fallthrough.

```java
private static final List<ComponentSimilarityCalculator> calculators = Arrays.asList(
    new VoltageRegulatorSimilarityCalculator(),  // 1st priority
    new LEDSimilarityCalculator(),
    new OpAmpSimilarityCalculator(),
    new LogicICSimilarityCalculator(),
    new MemorySimilarityCalculator(),
    new DiodeSimilarityCalculator(),
    new SensorSimilarityCalculator(),
    new MosfetSimilarityCalculator(),
    new TransistorSimilarityCalculator(),
    new MicrocontrollerSimilarityCalculator(),
    new ResistorSimilarityCalculator(),
    new CapacitorSimilarityCalculator(),
    new ConnectorSimilarityCalculator()          // Last priority
);
```

### Execution Flow

```java
for (ComponentSimilarityCalculator calculator : calculators) {
    if (calculator.isApplicable(type1) || calculator.isApplicable(type2)) {
        double similarity = calculator.calculateSimilarity(mpn1, mpn2, registry);
        logger.debug("  Returning similarity: {}", similarity);
        return similarity;  // ← FIRST match wins, NO FALLTHROUGH!
    }
}
// Only reaches here if NO calculator claimed the types
return calculateDefaultSimilarity(mpn1, mpn2, type1, type2);
```

**Critical properties:**
1. **First-applicable wins:** No fallthrough to other calculators
2. **Trusts calculator result:** Even 0.0 is returned immediately (fixed in PR #114)
3. **OR logic:** If EITHER type1 OR type2 is applicable, calculator is used
4. **No second chances:** Once a calculator claims a type, its result is final

---

## The OpAmp IC Interception Bug (PR #115)

### Overview
- **Discovered:** January 14, 2026
- **Impact:** Logic ICs (CD4001, 74HC00) returned 0.0 similarity instead of 0.9
- **Root cause:** OpAmpSimilarityCalculator claimed ALL ICs but only handled op-amps
- **Severity:** CRITICAL - LogicICSimilarityCalculator never executed for logic ICs

### The Bug

**BEFORE (OpAmpSimilarityCalculator.java):**
```java
@Override
public boolean isApplicable(ComponentType type) {
    // ❌ BUG: This intercepted ALL ICs
    if (type == ComponentType.IC || type == ComponentType.ANALOG_IC) {
        return true;
    }
    return type == ComponentType.OPAMP || type.name().startsWith("OPAMP_");
}

@Override
public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
    // Check if both are actually op-amps
    if (!isOpAmp(mpn1) || !isOpAmp(mpn2)) {
        return 0.0;  // ← Returns 0.0 for logic ICs like CD4001!
    }
    // ... op-amp comparison logic (never executes for logic ICs)
}
```

**Calculator registration order:**
```java
new OpAmpSimilarityCalculator(),   // ← Claims IC type first
new LogicICSimilarityCalculator(), // ← Never executes for ICs!
```

**What happened:**
1. CD4001 (logic IC) → ComponentType.IC
2. OpAmpSimilarityCalculator.isApplicable(IC) → **true** (claimed it!)
3. calculateSimilarity() → checks `isOpAmp(CD4001)` → **false**
4. Returns 0.0
5. LogicICSimilarityCalculator **never executes** (first-applicable-wins rule)

### Impact Table

| MPN1 | MPN2 | Expected | Actual (Bug) | Actual (Fixed) |
|------|------|----------|--------------|----------------|
| CD4001 | CD4001BE | 0.9 (same chip) | **0.0** ❌ | 0.9 ✅ |
| 74HC00 | 74HCT00 | 0.9 (equivalent) | **0.0** ❌ | 0.9 ✅ |
| LM358 | LM358N | 0.9 (same op-amp) | 0.9 ✅ | 0.9 ✅ |
| LM358 | LM324 | 0.3 (different config) | 0.3 ✅ | 0.3 ✅ |

### The Fix

**AFTER (Fixed - PR #115):**
```java
@Override
public boolean isApplicable(ComponentType type) {
    if (type == null) {
        return false;
    }

    // ✅ FIX: Only handle specific op-amp types
    // Don't intercept generic IC, ANALOG_IC, or other IC subtypes
    return type == ComponentType.OPAMP ||
           type == ComponentType.OPAMP_TI ||
           type.name().startsWith("OPAMP_");
}
```

**Now logic ICs work correctly:**
1. CD4001 → ComponentType.IC
2. OpAmpSimilarityCalculator.isApplicable(IC) → **false** (doesn't claim it)
3. LogicICSimilarityCalculator.isApplicable(IC) → **true** (claims it!)
4. LogicICSimilarityCalculator.calculateSimilarity() → 0.9 ✅

### Test Updates

**BEFORE (Incorrect test - validated the bug!):**
```java
@Test
void shouldBeApplicableForICTypes() {
    assertTrue(calculator.isApplicable(ComponentType.IC));
    assertTrue(calculator.isApplicable(ComponentType.ANALOG_IC));
}
```

**AFTER (Fixed test):**
```java
@Test
void shouldNotBeApplicableForGenericICTypes() {
    // OpAmpSimilarityCalculator should only handle specific OPAMP types
    assertFalse(calculator.isApplicable(ComponentType.IC));
    assertFalse(calculator.isApplicable(ComponentType.ANALOG_IC));
}

@Test
void shouldBeApplicableForOpAmpTypes() {
    assertTrue(calculator.isApplicable(ComponentType.OPAMP));
    assertTrue(calculator.isApplicable(ComponentType.OPAMP_TI));
}
```

### Lessons Learned

#### 1. Calculator Order Matters
First applicable calculator wins. OpAmpSimilarityCalculator was before LogicICSimilarityCalculator, so it intercepted all ICs.

#### 2. Be Specific in isApplicable()
**Rule:** Only claim types you can FULLY handle.

```java
// ❌ WRONG: Claiming generic type but only handling subset
public boolean isApplicable(ComponentType type) {
    return type == ComponentType.IC;  // Claims ALL ICs!
}
public double calculateSimilarity(...) {
    if (!isOpAmp(mpn1)) return 0.0;  // But only handles op-amps
}

// ✅ CORRECT: Only claim what you handle
public boolean isApplicable(ComponentType type) {
    return type == ComponentType.OPAMP || type.name().startsWith("OPAMP_");
}
```

#### 3. Test Ordering Matters
Bug only appeared in full test suite, not individual calculator tests. This is because:
- Individual test: Only one calculator exists in memory
- Full suite: All calculators compete, registration order matters

#### 4. Domain Ownership Principle
Each calculator should own its specific domain:
- **OpAmpSimilarityCalculator:** OPAMP, OPAMP_* only
- **LogicICSimilarityCalculator:** IC (but checks if logic IC), LOGIC_IC, LOGIC_IC_*
- **MemorySimilarityCalculator:** MEMORY, MEMORY_* only

---

## Calculator Ordering Strategy

### Current Order Rationale

```
1. VoltageRegulatorSimilarityCalculator  ← Specific component type
2. LEDSimilarityCalculator               ← Specific component type
3. OpAmpSimilarityCalculator             ← Specific IC subtype (FIXED: no longer claims IC)
4. LogicICSimilarityCalculator           ← IC subtype (checks isLogicIC)
5. MemorySimilarityCalculator            ← Specific IC subtype
6. DiodeSimilarityCalculator             ← Specific semiconductor
7. SensorSimilarityCalculator            ← Specific component type
8. MosfetSimilarityCalculator            ← Specific semiconductor
9. TransistorSimilarityCalculator        ← Specific semiconductor
10. MicrocontrollerSimilarityCalculator  ← Specific IC subtype
11. ResistorSimilarityCalculator         ← Passive component
12. CapacitorSimilarityCalculator        ← Passive component
13. ConnectorSimilarityCalculator        ← Mechanical component
```

**Ordering principles:**
- **Specific before generic:** Voltage regulators before general ICs
- **IC subtypes together:** OpAmp, Logic, Memory, MCU grouped
- **Semiconductors together:** Diode, MOSFET, Transistor grouped
- **Passives together:** Resistor, Capacitor
- **Connectors last:** Most mechanical, least electrical

### Ordering Bugs to Watch For

| Bug Pattern | Example | Solution |
|-------------|---------|----------|
| **Generic before specific** | DefaultCalculator before OpAmpCalculator | Move specific calculators first |
| **Claiming types you don't handle** | OpAmp claims IC but only handles op-amps | Only claim types you fully handle |
| **Overlapping domains** | Two calculators claim MOSFET | Define clear ownership |
| **No calculator for type** | New GYROSCOPE type but no calculator | Add specific calculator or update default |

---

## Two Interface Types

### 1. SimilarityCalculator (Simple Interface)

**Package:** `no.cantara.electronic.component.lib`

**Purpose:** Simple similarity calculation without type awareness.

```java
public interface SimilarityCalculator {
    /**
     * Calculate similarity between two MPNs.
     *
     * @param mpn1 First MPN
     * @param mpn2 Second MPN
     * @return Similarity score [0.0, 1.0]
     */
    double calculateSimilarity(String mpn1, String mpn2);
}
```

**Implementations:**
- `DefaultSimilarityCalculator` - String-based similarity (Levenshtein, prefix matching)
- `LevenshteinCalculator` - Pure edit distance

**Use when:**
- Don't need component type information
- Comparing MPNs as strings
- Fallback/default comparison

---

### 2. ComponentSimilarityCalculator (Type-Aware Interface)

**Package:** `no.cantara.electronic.component.lib`

**Purpose:** Type-aware similarity calculation with component-specific logic.

```java
public interface ComponentSimilarityCalculator {
    /**
     * Check if this calculator is applicable for the given component type.
     *
     * @param type Component type to check
     * @return true if this calculator can handle this type
     */
    boolean isApplicable(ComponentType type);

    /**
     * Calculate similarity between two MPNs.
     *
     * @param mpn1 First MPN
     * @param mpn2 Second MPN
     * @param registry Pattern registry for component detection
     * @return Similarity score [0.0, 1.0]
     */
    double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry);
}
```

**Implementations (17 total):**
- ResistorSimilarityCalculator
- CapacitorSimilarityCalculator
- DiodeSimilarityCalculator
- MosfetSimilarityCalculator
- TransistorSimilarityCalculator
- OpAmpSimilarityCalculator
- VoltageRegulatorSimilarityCalculator
- LEDSimilarityCalculator
- MemorySimilarityCalculator
- SensorSimilarityCalculator
- LogicICSimilarityCalculator
- ConnectorSimilarityCalculator
- MicrocontrollerSimilarityCalculator
- MCUSimilarityCalculator
- PassiveComponentCalculator
- DefaultSimilarityCalculator (implements both)
- LevenshteinCalculator (implements both)

**Use when:**
- Need component-specific comparison logic
- Electrical specs matter (voltage, capacitance, resistance, etc.)
- Type-specific equivalent groups (e.g., 2N2222 ≈ PN2222)

---

## Metadata-Driven Migration Status

**12 of 17 calculators converted (71% complete)**

| Calculator | Status | PR | Conversion Date | Specs | Critical Specs |
|-----------|--------|-----|-----------------|-------|----------------|
| ResistorSimilarityCalculator | ✅ | Pre-existing | - | resistance, package, tolerance | resistance |
| CapacitorSimilarityCalculator | ✅ | Pre-existing | - | capacitance, voltage, dielectric, package | capacitance, voltage |
| TransistorSimilarityCalculator | ✅ | Pre-existing | - | polarity, voltageRating, currentRating, hfe, package | polarity, voltageRating, currentRating |
| DiodeSimilarityCalculator | ✅ | Pre-existing | - | type, voltageRating, currentRating, package | type, voltageRating, currentRating |
| MosfetSimilarityCalculator | ✅ | Pre-existing | - | channel, voltageRating, currentRating, rdsOn, package | channel, voltageRating, currentRating |
| VoltageRegulatorSimilarityCalculator | ✅ | Pre-existing | - | regulatorType, outputVoltage, polarity, currentRating, package | regulatorType, outputVoltage, polarity |
| OpAmpSimilarityCalculator | ✅ | #116 | 2026-01-08 | configuration, family, package | configuration |
| MemorySimilarityCalculator | ✅ | #117 | 2026-01-09 | memoryType, capacity, interface, package | memoryType, capacity |
| LEDSimilarityCalculator | ✅ | #118 | 2026-01-10 | color, family, brightness, package | color |
| ConnectorSimilarityCalculator | ✅ | Pre-existing | - | pinCount, pitch, family, mountingType | pinCount, pitch |
| LogicICSimilarityCalculator | ✅ | #119 | 2026-01-11 | function, series, technology, package | function |
| SensorSimilarityCalculator | ✅ | #120 | 2026-01-12 | sensorType, family, interface, package | sensorType |
| MCUSimilarityCalculator | ⏳ | - | - | family, series, features | - |
| MicrocontrollerSimilarityCalculator | ⏳ | - | - | manufacturer, series, package | - |
| PassiveComponentCalculator | ⏳ | - | - | value, sizeCode, tolerance | - |
| DefaultSimilarityCalculator | ⏳ | - | - | - | - |
| LevenshteinCalculator | ⏳ | - | - | - | - |

**For metadata-driven migration details, see:** `/similarity-metadata` and `/metadata-driven-similarity-conversion` skills.

---

## Testing Strategy

### isApplicable() Positive Tests

```java
@Test
void shouldBeApplicableForOpAmpTypes() {
    assertTrue(calculator.isApplicable(ComponentType.OPAMP));
    assertTrue(calculator.isApplicable(ComponentType.OPAMP_TI));
    assertTrue(calculator.isApplicable(ComponentType.OPAMP_ANALOG_DEVICES));
}
```

### isApplicable() Negative Tests (Critical!)

```java
@Test
void shouldNotBeApplicableForGenericICTypes() {
    // OpAmpSimilarityCalculator should NOT claim generic IC types
    assertFalse(calculator.isApplicable(ComponentType.IC));
    assertFalse(calculator.isApplicable(ComponentType.ANALOG_IC));
}

@Test
void shouldNotBeApplicableForOtherComponentTypes() {
    assertFalse(calculator.isApplicable(ComponentType.RESISTOR));
    assertFalse(calculator.isApplicable(ComponentType.CAPACITOR));
    assertFalse(calculator.isApplicable(ComponentType.MOSFET));
}
```

**Why negative tests are critical:** They prevent the OpAmp IC interception bug from recurring. Explicitly test that calculators DON'T claim types they can't handle.

### Integration Tests for Ordering

```java
@Test
void logicICsShouldUseLogicCalculatorNotOpAmpCalculator() {
    // CD4001 is a logic IC, not an op-amp
    double similarity = MPNUtils.calculateSimilarity("CD4001", "CD4001BE");
    assertTrue(similarity >= 0.9, "Logic ICs should be highly similar");
    // If this fails, check if OpAmpSimilarityCalculator is claiming IC type
}

@Test
void opAmpsShouldUseOpAmpCalculator() {
    double similarity = MPNUtils.calculateSimilarity("LM358", "LM358N");
    assertTrue(similarity >= 0.9, "Same op-amp variants should be highly similar");
}
```

---

## Learnings & Quirks

### Calculator Selection is Deterministic
Given the same MPNs, the same calculator always wins (no randomness or concurrency issues).

### 0.0 is a Valid Result
PR #114 fixed MPNUtils to trust 0.0 results. Previously, 0.0 caused fallthrough to default calculator.

### OR Logic in Selection
```java
if (calculator.isApplicable(type1) || calculator.isApplicable(type2))
```
If EITHER component type is applicable, calculator is used. This allows cross-type comparisons (e.g., comparing OPAMP to OPAMP_TI).

### No Fallback Chain
Unlike traditional strategy pattern with fallback chain, this implementation returns immediately on first match. There is no "try calculator A, then B, then C" logic.

### isApplicable() vs calculateSimilarity() Consistency
**Rule:** If isApplicable() returns true, calculateSimilarity() MUST be able to handle those types.

**OpAmp bug violated this rule:**
- isApplicable(IC) → true
- calculateSimilarity(CD4001, ...) → 0.0 (can't handle logic ICs)

**Prevention:** Add validation in tests or runtime:
```java
if (calculator.isApplicable(type) && similarity == 0.0) {
    logger.warn("Calculator {} claimed type {} but returned 0.0",
                calculator.getClass().getSimpleName(), type);
}
```

---

## See Also

- `/similarity` - Main similarity system documentation
- `/similarity-metadata` - Metadata-driven similarity framework
- `/metadata-driven-similarity-conversion` - Migration guide for converting calculators
- `.docs/history/BUG_FIX_ANALYSIS.md` - Detailed bug analysis
- `MPNUtils.java` (lines 37-51) - Calculator registration
- `OpAmpSimilarityCalculator.java` - Example of fixed isApplicable()
- `LogicICSimilarityCalculator.java` - Handles logic ICs correctly
