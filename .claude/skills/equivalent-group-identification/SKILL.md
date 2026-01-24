# Equivalent Group Identification

Use this skill when working with component equivalent groups, cross-manufacturer part matching, or understanding hardcoded equivalencies.

## Hardcoded Groups in 4 Calculators

### 1. TransistorSimilarityCalculator

**Equivalent groups hardcoded in calculator:**

```java
private static final Set<Set<String>> EQUIVALENT_GROUPS = Set.of(
    // NPN Transistors
    Set.of("2N2222", "PN2222", "2N2222A", "PN2222A"),     // General purpose NPN
    Set.of("2N3904", "PN3904", "2N3904A", "PN3904A"),     // Low-power NPN
    Set.of("2N4401", "PN4401"),                            // Medium-power NPN

    // PNP Transistors
    Set.of("2N2907", "PN2907", "2N2907A", "PN2907A"),     // General purpose PNP
    Set.of("2N3906", "PN3906", "2N3906A", "PN3906A"),     // Low-power PNP
    Set.of("2N4403", "PN4403"),                            // Medium-power PNP

    // BC Series (European standard)
    Set.of("BC547", "BC547A", "BC547B", "BC547C"),        // Low noise NPN
    Set.of("BC548", "BC548A", "BC548B", "BC548C"),        // Low noise NPN
    Set.of("BC549", "BC549A", "BC549B", "BC549C"),        // Ultra-low noise NPN
    Set.of("BC557", "BC557A", "BC557B", "BC557C"),        // Low noise PNP
    Set.of("BC558", "BC558A", "BC558B", "BC558C")         // Low noise PNP
);

private boolean areInSameEquivalentGroup(String mpn1, String mpn2) {
    String base1 = getBaseMPN(mpn1);  // Strip package suffix
    String base2 = getBaseMPN(mpn2);

    for (Set<String> group : EQUIVALENT_GROUPS) {
        if (group.contains(base1) && group.contains(base2)) {
            return true;
        }
    }
    return false;
}
```

**Why these groups exist:**
- **2N2222 ≈ PN2222:** Same specs, different manufacturers (NXP vs various)
- **BC547 gain bins (A/B/C):** Same transistor, different hFE ranges
- **European vs American numbering:** BC series (Europe) vs 2N series (US)

---

### 2. DiodeSimilarityCalculator

**Equivalent groups hardcoded in calculator:**

```java
private static final Set<Set<String>> EQUIVALENT_GROUPS = Set.of(
    // Signal diodes
    Set.of("1N4148", "1N914", "1N914A", "1N914B"),        // Fast switching signal diode
    Set.of("1N4148W", "1N4148WS", "1N4148WT"),            // SMD variants of 1N4148

    // Rectifier diodes (1N400x series)
    Set.of("1N4001", "1N4002", "1N4003", "1N4004",        // General purpose rectifiers
           "1N4005", "1N4006", "1N4007"),                  // Different voltage ratings

    // Zener diodes
    Set.of("1N4728", "BZX55C3V3", "MMSZ4728"),            // 3.3V Zener variants
    Set.of("1N4733", "BZX55C5V1", "MMSZ4733"),            // 5.1V Zener variants

    // Schottky diodes
    Set.of("1N5817", "1N5818", "1N5819"),                 // Low-voltage Schottky
    Set.of("BAT54", "BAT54A", "BAT54C", "BAT54S")         // SMD Schottky variants
);
```

**Why these groups exist:**
- **1N4148 ≈ 1N914:** Identical specs, legacy part numbers
- **1N400x series:** Same diode, different reverse voltage ratings (50V-1000V)
- **Zener variants:** Same voltage, different packages or manufacturers

---

### 3. MosfetSimilarityCalculator

**Equivalent groups hardcoded in calculator:**

```java
private static final Set<Set<String>> EQUIVALENT_GROUPS = Set.of(
    // Infineon vs ST equivalents (N-channel)
    Set.of("IRF530", "STF530", "FQP30N06L"),              // 100V 14A N-channel
    Set.of("IRF540", "STF540", "FQP40N06L"),              // 100V 28A N-channel
    Set.of("IRF640", "STF640"),                            // 200V 18A N-channel
    Set.of("IRF740", "STF740"),                            // 400V 10A N-channel
    Set.of("IRF840", "STF840"),                            // 500V 8A N-channel

    // IRFZ family (low RDS(on))
    Set.of("IRFZ44", "IRFZ44N", "FQP50N06"),              // 55V 49A low RDS(on)
    Set.of("IRFZ46", "IRFZ46N"),                           // 55V 53A low RDS(on)

    // IRFP family (TO-247 package, high power)
    Set.of("IRFP250", "IRFP250N"),                         // 200V 30A
    Set.of("IRFP260", "IRFP260N")                          // 200V 50A
);
```

**Why these groups exist:**
- **IRF vs STF:** Infineon vs ST, pin-compatible with same specs
- **IRF vs FQP:** Infineon vs Fairchild, equivalent performance
- **N suffix:** RoHS/lead-free variant of same MOSFET

---

### 4. MemorySimilarityCalculator

**Equivalent groups hardcoded in calculator:**

```java
private static final Set<Set<String>> EQUIVALENT_GROUPS = Set.of(
    // I2C EEPROM (Microchip vs Atmel)
    Set.of("24LC256", "AT24C256", "24C256", "M24256"),    // 256Kbit I2C EEPROM
    Set.of("24LC128", "AT24C128", "24C128", "M24128"),    // 128Kbit I2C EEPROM
    Set.of("24LC64", "AT24C64", "24C64", "M24C64"),       // 64Kbit I2C EEPROM
    Set.of("24LC32", "AT24C32", "24C32", "M24C32"),       // 32Kbit I2C EEPROM

    // SPI Flash (Winbond vs Macronix vs Micron)
    Set.of("W25Q64", "MX25L6406E", "N25Q064"),            // 64Mbit SPI Flash
    Set.of("W25Q128", "MX25L12835F", "N25Q128"),          // 128Mbit SPI Flash

    // SPI EEPROM
    Set.of("25LC256", "AT25256", "M95256")                // 256Kbit SPI EEPROM
);
```

**Why these groups exist:**
- **24LC vs AT24C:** Microchip vs Atmel, pin-compatible I2C EEPROM
- **Capacity variants:** Same capacity, different manufacturers
- **Interface standards:** I2C and SPI memory interfaces are standardized

---

## PrefixRegistry Cross-Manufacturer Groups

**Existing infrastructure:** `PrefixRegistry.java`

**Purpose:** Maps component prefixes to equivalent cross-manufacturer groups.

**Example usage:**
```java
// Register equivalent prefix groups
PrefixRegistry registry = new PrefixRegistry();

// Op-amp prefixes
registry.registerEquivalentGroup("LM", "MC", "RC");  // LM358 ≈ MC1458 ≈ RC4558

// Voltage regulator prefixes
registry.registerEquivalentGroup("LM78", "MC78", "UA78");  // LM7805 ≈ MC7805 ≈ UA7805

// Check equivalence
boolean equiv = registry.areEquivalent("LM358", "MC1458");  // true
```

**Integration with similarity calculators:**
```java
// In OpAmpSimilarityCalculator
private boolean areEquivalentPrefixes(String mpn1, String mpn2) {
    String prefix1 = extractPrefix(mpn1);
    String prefix2 = extractPrefix(mpn2);
    return prefixRegistry.areEquivalent(prefix1, prefix2);
}
```

---

## Centralization Proposal: EquivalentPartRegistry

**Goal:** Unified registry for all equivalent groups across calculators.

**Proposed API design:**

```java
public class EquivalentPartRegistry {
    private final Map<ComponentType, Set<Set<String>>> equivalentGroups = new HashMap<>();

    // Register an equivalent group
    public void registerGroup(ComponentType type, Set<String> group) {
        equivalentGroups
            .computeIfAbsent(type, k -> new HashSet<>())
            .add(group);
    }

    // Check if two MPNs are equivalent
    public boolean isEquivalent(ComponentType type, String mpn1, String mpn2) {
        Set<Set<String>> groups = equivalentGroups.get(type);
        if (groups == null) return false;

        String base1 = stripSuffix(mpn1);
        String base2 = stripSuffix(mpn2);

        for (Set<String> group : groups) {
            if (group.contains(base1) && group.contains(base2)) {
                return true;
            }
        }
        return false;
    }

    // Get all equivalents for an MPN
    public Set<String> getEquivalents(ComponentType type, String mpn) {
        Set<Set<String>> groups = equivalentGroups.get(type);
        if (groups == null) return Set.of();

        String base = stripSuffix(mpn);

        for (Set<String> group : groups) {
            if (group.contains(base)) {
                Set<String> equivalents = new HashSet<>(group);
                equivalents.remove(base);  // Don't include self
                return equivalents;
            }
        }
        return Set.of();
    }
}
```

**Usage in similarity calculators:**

```java
// In TransistorSimilarityCalculator
private final EquivalentPartRegistry registry = EquivalentPartRegistry.getInstance();

// Register groups on initialization
static {
    registry.registerGroup(ComponentType.TRANSISTOR, Set.of("2N2222", "PN2222", "2N2222A"));
    registry.registerGroup(ComponentType.TRANSISTOR, Set.of("2N3904", "PN3904", "2N3904A"));
    // ... etc
}

// Check equivalence
private double calculateSimilarity(String mpn1, String mpn2) {
    if (registry.isEquivalent(ComponentType.TRANSISTOR, mpn1, mpn2)) {
        return HIGH_SIMILARITY;  // 0.9 or higher
    }
    // ... other similarity logic
}
```

---

## Migration Path from Hardcoded Groups

**Step 1: Create EquivalentPartRegistry class**
```java
public class EquivalentPartRegistry {
    private static final EquivalentPartRegistry INSTANCE = new EquivalentPartRegistry();
    // ... API methods
}
```

**Step 2: Migrate groups from calculators**
```java
// BEFORE (in TransistorSimilarityCalculator):
private static final Set<Set<String>> EQUIVALENT_GROUPS = Set.of(
    Set.of("2N2222", "PN2222"),
    Set.of("2N3904", "PN3904")
);

// AFTER (in EquivalentPartRegistry initialization):
static {
    getInstance().registerGroup(ComponentType.TRANSISTOR, Set.of("2N2222", "PN2222"));
    getInstance().registerGroup(ComponentType.TRANSISTOR, Set.of("2N3904", "PN3904"));
}
```

**Step 3: Update calculator to use registry**
```java
// BEFORE:
private boolean areInSameEquivalentGroup(String mpn1, String mpn2) {
    for (Set<String> group : EQUIVALENT_GROUPS) {
        if (group.contains(mpn1) && group.contains(mpn2)) return true;
    }
    return false;
}

// AFTER:
private boolean areInSameEquivalentGroup(String mpn1, String mpn2) {
    return EquivalentPartRegistry.getInstance()
        .isEquivalent(ComponentType.TRANSISTOR, mpn1, mpn2);
}
```

**Step 4: Test backward compatibility**
```java
@Test
void equivalentPartsShouldHaveHighSimilarity() {
    // Same tests as before, ensure registry produces same results
    double similarity = calculator.calculateSimilarity("2N2222", "PN2222", registry);
    assertTrue(similarity >= HIGH_SIMILARITY);
}
```

**Step 5: Repeat for all 4 calculators**
- TransistorSimilarityCalculator
- DiodeSimilarityCalculator
- MosfetSimilarityCalculator
- MemorySimilarityCalculator

---

## Adding New Equivalent Groups

### Research Process

**1. Check datasheets:**
- Compare electrical specs (voltage, current, power, etc.)
- Verify pin compatibility
- Check package dimensions

**2. Consult cross-reference guides:**
- Manufacturer cross-reference tables
- Distributor equivalence databases (Digi-Key, Mouser)
- Industry standard references

**3. Verify in practice:**
- Community forums (EEVblog, Electronics Stack Exchange)
- Design notes and application circuits
- Manufacturer migration guides

**Example: Adding new MOSFET equivalents**
```
Research:
- IRF3205 datasheet: 55V 110A, RDS(on) 8mΩ, TO-220
- FQP30N06L datasheet: 60V 32A, RDS(on) 35mΩ, TO-220
- Conclusion: NOT equivalent (different current/RDS(on))

- IRF530 datasheet: 100V 14A, RDS(on) 160mΩ, TO-220
- STF530 datasheet: 100V 14A, RDS(on) 160mΩ, TO-220
- Conclusion: EQUIVALENT (same specs, ST vs Infineon)

Add to registry:
registry.registerGroup(ComponentType.MOSFET, Set.of("IRF530", "STF530"));
```

---

## Testing Strategy

**Test equivalent pairs:**
```java
@ParameterizedTest
@CsvSource({
    "2N2222, PN2222",
    "2N3904, PN3904",
    "1N4148, 1N914",
    "IRF530, STF530",
    "24LC256, AT24C256"
})
void equivalentPartsShouldHaveHighSimilarity(String mpn1, String mpn2) {
    double similarity = MPNUtils.calculateSimilarity(mpn1, mpn2);
    assertTrue(similarity >= HIGH_SIMILARITY,
        String.format("%s and %s should be highly similar (>= 0.9), got: %.2f",
            mpn1, mpn2, similarity));
}
```

**Test non-equivalent pairs:**
```java
@ParameterizedTest
@CsvSource({
    "2N2222, 2N3904",   // Different transistor types
    "1N4148, 1N4001",   // Signal vs rectifier diode
    "IRF530, IRF540",   // Different current ratings
    "24LC256, 24LC128"  // Different memory capacities
})
void nonEquivalentPartsShouldNotHaveHighSimilarity(String mpn1, String mpn2) {
    double similarity = MPNUtils.calculateSimilarity(mpn1, mpn2);
    assertTrue(similarity < HIGH_SIMILARITY,
        String.format("%s and %s should NOT be highly similar (< 0.9), got: %.2f",
            mpn1, mpn2, similarity));
}
```

---

## Documentation Requirements

**For each new equivalent group, document:**

1. **Component specs:** Voltage, current, power, package, etc.
2. **Manufacturers:** Which manufacturers produce these parts
3. **Source:** Where equivalence was verified (datasheet, cross-ref guide, etc.)
4. **Known differences:** Any minor spec differences (e.g., RDS(on) tolerance)
5. **Package compatibility:** Confirm same footprint and pinout

**Example documentation:**
```java
// IRF530 ≈ STF530
// Specs: 100V, 14A, RDS(on) 160mΩ, TO-220
// Manufacturers: Infineon (IRF), STMicroelectronics (STF)
// Source: ST cross-reference guide AN3041
// Differences: None - pin-compatible drop-in replacement
// Package: Both TO-220 with same pinout (G-D-S)
Set.of("IRF530", "STF530")
```

---

## Learnings & Quirks

### Suffix Stripping is Critical
"2N2222A" and "2N2222" are the same base part with different gain bins. Strip suffixes before comparison.

### Package Variants Matter
"1N4148" (through-hole) vs "1N4148W" (SMD) are electrically equivalent but physically different. Document package differences.

### Voltage Ratings are NOT Equivalent
1N4001 (50V) and 1N4007 (1000V) are in the same family but NOT drop-in equivalents for all applications.

### Cross-Manufacturer Timing Matters
Acquisitions change part numbering. ATMEGA328P was Atmel, now it's Microchip after acquisition.

---

## See Also

- `/similarity` - How equivalent groups are used in similarity calculation
- `/similarity-transistor` - Transistor equivalent groups
- `/similarity-diode` - Diode equivalent groups
- `/similarity-mosfet` - MOSFET equivalent groups
- `/similarity-memory` - Memory IC equivalent groups
- `TransistorSimilarityCalculator.java` - Hardcoded transistor groups
- `DiodeSimilarityCalculator.java` - Hardcoded diode groups
- `MosfetSimilarityCalculator.java` - Hardcoded MOSFET groups
- `MemorySimilarityCalculator.java` - Hardcoded memory groups
