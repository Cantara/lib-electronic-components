# Similarity Metadata System

**Use this skill when:**
- Working with component similarity calculations using metadata-driven architecture
- Configuring spec importance levels and tolerance rules
- Understanding context-aware similarity profiles
- Converting legacy similarity calculators to metadata-driven approach
- Troubleshooting similarity score calculations

---

## Overview

The library uses a **metadata-driven architecture** for component similarity calculations, replacing hardcoded logic with configurable, type-specific similarity rules.

### Motivation

**Problem**: Previous similarity calculators had hardcoded weights and thresholds, making them difficult to tune and inconsistent across component types.

**Solution**: Centralized metadata system that defines:
- Which specs are critical vs optional for each component type
- How to compare spec values (exact match, percentage tolerance, min/max requirements)
- Context-aware similarity profiles (design phase, replacement, cost optimization, etc.)

---

## Core Classes

| Class | Responsibility |
|-------|----------------|
| `ComponentTypeMetadata` | Defines specs, importance levels, tolerance rules for a component type |
| `ComponentTypeMetadataRegistry` | Singleton registry; maps ComponentType → metadata |
| `SpecImportance` | Enum: CRITICAL, HIGH, MEDIUM, LOW, OPTIONAL (with base weights) |
| `ToleranceRule` | Interface for comparing spec values with scoring logic |
| `SimilarityProfile` | Enum: 5 context-aware profiles that adjust importance multipliers |
| `SpecValue<T>` | Unit-aware value representation with min/max ranges |
| `SpecUnit` | Unit types: NONE, OHM, FARAD, HENRY, VOLT, AMPERE, WATT, HERTZ, PERCENTAGE |

---

## SpecImportance Levels

Defines how important a spec is for similarity matching:

| Level | Base Weight | Mandatory | Use Case |
|-------|-------------|-----------|----------|
| CRITICAL | 1.0 | Yes | Specs that affect core functionality (resistance value, capacitance, polarity) |
| HIGH | 0.7 | No | Specs that affect reliability (package type, tolerance, dielectric) |
| MEDIUM | 0.4 | No | Specs that affect performance (power rating, ESR, temp coefficient) |
| LOW | 0.2 | No | Secondary considerations (gate charge, viewing angle, certifications) |
| OPTIONAL | 0.0 | No | Informational only (lifecycle status, manufacturer notes) |

**Key Design**: Only CRITICAL specs are mandatory. Effective weight = baseWeight × profile multiplier.

---

## ToleranceRule Types

Defines how to compare candidate vs original spec values:

### 1. ExactMatchRule
```java
ToleranceRule.exactMatch()
```
- Scores 1.0 for exact match, 0.0 otherwise
- Case-insensitive for strings
- Use for: dielectric types (X7R, X5R), polarity (NPN, PNP), package codes

### 2. PercentageToleranceRule
```java
ToleranceRule.percentageTolerance(double maxPercent)
```
- Scores 1.0 if within tolerance, decays linearly beyond
- Example: `percentageTolerance(5.0)` allows ±5% deviation
- Use for: resistance, capacitance, inductance

### 3. MinimumRequiredRule
```java
ToleranceRule.minimumRequired()
```
- Scores 1.0 if candidate ≥ original (better or equal)
- Scores 0.0 if candidate < original (insufficient)
- Use for: voltage rating, current rating (candidate must meet or exceed)

### 4. MaximumAllowedRule
```java
ToleranceRule.maximumAllowed(double maxMultiplier)
```
- Scores 1.0 if candidate ≤ original (better or equal)
- Scores decay linearly up to maxMultiplier
- Example: `maximumAllowed(1.5)` allows up to 1.5× the original value
- Use for: Rds(on), ESR (lower is better, some increase acceptable)

### 5. RangeToleranceRule
```java
ToleranceRule.rangeTolerance(double lowerPercent, double upperPercent)
```
- Scores 1.0 within range, decays outside
- Example: `rangeTolerance(-10.0, 20.0)` allows -10% to +20%
- Use for: asymmetric tolerances, hFE ranges

---

## SimilarityProfile Contexts

Adjusts importance multipliers based on use case:

| Profile | Threshold | CRITICAL | HIGH | MEDIUM | LOW | Use Case |
|---------|-----------|----------|------|--------|-----|----------|
| DESIGN_PHASE | 0.85 | 1.0 | 0.9 | 0.7 | 0.4 | Exact match for new designs |
| REPLACEMENT | 0.75 | 1.0 | 0.7 | 0.4 | 0.2 | **Default**: Direct replacement |
| PERFORMANCE_UPGRADE | 0.70 | 1.0 | 0.8 | 0.5 | 0.2 | Better performance acceptable |
| COST_OPTIMIZATION | 0.60 | 1.0 | 0.4 | 0.2 | 0.0 | Maintain critical specs, relax others |
| EMERGENCY_SOURCING | 0.50 | 0.8 | 0.4 | 0.2 | 0.0 | Urgent replacement, relaxed requirements |

**Effective Weight Calculation**:
```
effectiveWeight = importance.baseWeight × profile.multiplier
```

Example: HIGH spec (0.7) in COST_OPTIMIZATION (0.4) → 0.7 × 0.4 = 0.28

---

## ComponentTypeMetadata Structure

Built using fluent builder pattern:

```java
ComponentTypeMetadata resistor = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
    .addSpec("tolerance", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
    .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
    .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
    .addSpec("temperatureCoefficient", SpecImportance.LOW, ToleranceRule.percentageTolerance(20.0))
    .defaultProfile(SimilarityProfile.REPLACEMENT)
    .build();
```

### Querying Metadata

```java
// Check if spec is configured
SpecConfig config = metadata.getSpecConfig("resistance");
if (config != null) {
    SpecImportance importance = config.getImportance();
    ToleranceRule rule = config.getToleranceRule();
}

// Check if spec is critical
boolean critical = metadata.isCritical("resistance"); // true

// Get all configured specs
Set<String> allSpecs = metadata.getAllSpecs();

// Get default profile
SimilarityProfile profile = metadata.getDefaultProfile();
```

---

## ComponentTypeMetadataRegistry

Singleton registry pre-configured for top 10 component types:

```java
ComponentTypeMetadataRegistry registry = ComponentTypeMetadataRegistry.getInstance();

// Lookup metadata
Optional<ComponentTypeMetadata> metadata = registry.getMetadata(ComponentType.RESISTOR);

// Fallback to base type for manufacturer-specific types
Optional<ComponentTypeMetadata> yageoMeta = registry.getMetadata(ComponentType.RESISTOR_CHIP_YAGEO);
// Returns RESISTOR metadata (base type fallback)

// Custom registration
ComponentTypeMetadata customMetadata = ComponentTypeMetadata.builder(ComponentType.INDUCTOR)
    .addSpec("inductance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(5.0))
    .build();
registry.register(customMetadata);
```

---

## Pre-Registered Component Types

The registry initializes with metadata for:

| ComponentType | Critical Specs | HIGH Specs | Notes |
|---------------|----------------|------------|-------|
| RESISTOR | resistance, tolerance | package | 2 critical, 5 total specs |
| CAPACITOR | capacitance, voltage, dielectric | package | 3 critical, 7 total specs |
| MOSFET | voltageRating, currentRating, channel | rdsOn | 3 critical, 6 total specs |
| TRANSISTOR | polarity, voltageRating | collectorCurrent | 2 critical, 6 total specs |
| DIODE | type, voltageRating | currentRating | 2 critical, 5 total specs |
| OPAMP | configuration, inputType | supplyVoltageMin | 2 critical, 6 total specs |
| MICROCONTROLLER | family, flashSize | ramSize | 2 critical, 5 total specs |
| MEMORY | type, capacity, interface | speed | 3 critical, 5 total specs |
| LED | color, brightness | wavelength | 2 critical, 4 total specs |
| CONNECTOR | pinCount, pitch, gender | mountingType | 3 critical, 6 total specs |

---

## Usage Examples

### Define Resistor Similarity

```java
ComponentTypeMetadata resistor = ComponentTypeMetadata.builder(ComponentType.RESISTOR)
    .addSpec("resistance", SpecImportance.CRITICAL, ToleranceRule.percentageTolerance(1.0))
    .addSpec("tolerance", SpecImportance.CRITICAL, ToleranceRule.exactMatch())
    .addSpec("package", SpecImportance.HIGH, ToleranceRule.exactMatch())
    .addSpec("powerRating", SpecImportance.MEDIUM, ToleranceRule.minimumRequired())
    .defaultProfile(SimilarityProfile.REPLACEMENT)
    .build();

// Check if 10.1kΩ is similar to 10kΩ (within 1%)
boolean similar = resistor.isCritical("resistance"); // true
SpecConfig config = resistor.getSpecConfig("resistance");
ToleranceRule rule = config.getToleranceRule();

SpecValue<Double> original = new SpecValue<>(10000.0, SpecUnit.OHM);
SpecValue<Double> candidate = new SpecValue<>(10100.0, SpecUnit.OHM);
double score = rule.calculateScore(original, candidate); // 0.0 (outside 1% tolerance)
```

### Context-Aware Similarity

```java
ComponentTypeMetadata capacitor = registry.getMetadata(ComponentType.CAPACITOR).orElseThrow();

// Design phase: strict matching (0.85 threshold)
SimilarityProfile designProfile = SimilarityProfile.DESIGN_PHASE;
double designMultiplier = designProfile.getMultiplier(SpecImportance.HIGH); // 0.9
boolean passesDesign = designProfile.meetsThreshold(0.82); // false

// Emergency sourcing: relaxed matching (0.50 threshold)
SimilarityProfile emergencyProfile = SimilarityProfile.EMERGENCY_SOURCING;
double emergencyMultiplier = emergencyProfile.getMultiplier(SpecImportance.HIGH); // 0.4
boolean passesEmergency = emergencyProfile.meetsThreshold(0.55); // true
```

---

## Calculator Integration Pattern

Each refactored calculator follows this pattern:

```java
public class XxxSimilarityCalculator implements ComponentSimilarityCalculator {
    private final ComponentTypeMetadataRegistry metadataRegistry;

    public XxxSimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        // Get metadata
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.XXX);
        if (metadataOpt.isEmpty()) {
            return calculateLegacySimilarity(mpn1, mpn2); // Fallback
        }

        ComponentTypeMetadata metadata = metadataOpt.get();
        SimilarityProfile profile = metadata.getDefaultProfile();

        // Extract specs from MPNs
        String spec1 = extractSpec(mpn1);
        String spec2 = extractSpec(mpn2);

        double totalScore = 0.0;
        double maxPossibleScore = 0.0;

        // For each spec: compare using ToleranceRule
        ComponentTypeMetadata.SpecConfig config = metadata.getSpecConfig("specName");
        if (config != null && spec1 != null && spec2 != null) {
            ToleranceRule rule = config.getToleranceRule();
            SpecValue<T> orig = new SpecValue<>(parseSpec(spec1), SpecUnit.XXX);
            SpecValue<T> cand = new SpecValue<>(parseSpec(spec2), SpecUnit.XXX);

            double specScore = rule.compare(orig, cand);
            double specWeight = profile.getEffectiveWeight(config.getImportance());

            totalScore += specScore * specWeight;
            maxPossibleScore += specWeight;
        }

        // Normalize to [0.0, 1.0]
        return maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;
    }

    private double calculateLegacySimilarity(String mpn1, String mpn2) {
        // Keep old hardcoded logic for backward compatibility
    }
}
```

### Key Implementation Details

**1. Legacy Fallback**
```java
if (metadataOpt.isEmpty()) {
    logger.warn("No metadata found for {} type, falling back to legacy scoring", type);
    return calculateLegacySimilarity(mpn1, mpn2);
}
```
Ensures backward compatibility if metadata unavailable.

**2. Value Parsing**
Add type-specific parsing methods:
- `parseResistanceValue(String)` → Double (in ohms): "10K" → 10000.0
- `parseCapacitanceValue(String)` → Double (in farads): "0.1µF" → 1.0e-7

**3. Normalization**
Critical change: all scores now normalized to [0.0, 1.0]:
```java
double normalizedSimilarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;
```

**4. Voltage Asymmetry is Correct**
MinimumRequiredRule creates intentional asymmetry:
- Downgrading voltage (50V → 5V): score ~0.6 (voltage fails)
- Upgrading voltage (5V → 50V): score ~1.0 (voltage passes)

This is **correct** for component replacement: you can replace a 5V part with a 50V part, but not vice versa.

---

## Effective Weight Calculation

```
effectiveWeight = baseWeight × profileMultiplier
totalScore = Σ(specScore × effectiveWeight)
normalized = totalScore / maxPossibleScore
```

**Example (resistor with REPLACEMENT profile)**:
- Package (HIGH): 1.0 × (0.7 × 0.7) = 0.49
- Resistance (CRITICAL): 1.0 × (1.0 × 1.0) = 1.0
- Total: 1.49
- Normalized: 1.49 / 1.49 = 1.0 (perfect match)

---

## Testing Strategy

165 comprehensive tests covering:

**ToleranceRuleTest** (35 tests):
- Each rule implementation (ExactMatch, Percentage, MinimumRequired, MaximumAllowed, Range)
- Scoring behavior, edge cases, null handling
- isAcceptable() threshold behavior

**SimilarityProfileTest** (36 tests):
- Multiplier values for all 5 profiles × 5 importance levels
- Effective weight calculations
- Threshold acceptance logic
- Real-world scenarios

**ComponentTypeMetadataTest** (29 tests):
- Builder pattern validation
- Spec config queries (getSpecConfig, getAllSpecs)
- Critical spec identification
- Real-world examples (resistor, capacitor, MOSFET)

**ComponentTypeMetadataRegistryTest** (35 tests):
- Singleton pattern
- Pre-registered types (10 component types)
- Base type fallback for manufacturer-specific types
- Custom registration and overwriting
- Critical spec configuration
- Tolerance rule configuration

**SpecImportanceTest** (30 tests):
- Base weight values
- Mandatory flag (only CRITICAL is mandatory)
- Weight distribution (CRITICAL+HIGH = 70% of total)
- Semantic meaning and real-world usage

---

## Gotchas and Learnings

### 1. SpecValue Instantiation
```java
// NO static factory method - use constructor
SpecValue<Double> value = new SpecValue<>(100.0, SpecUnit.FARAD); // ✓
SpecValue<Double> value = SpecValue.of(100.0); // ✗ Does not exist
```

### 2. ComponentTypeMetadata API
```java
// Methods return directly, not Optional (except registry lookup)
SpecConfig config = metadata.getSpecConfig("resistance"); // Can be null
Set<String> specs = metadata.getAllSpecs(); // Never null
boolean critical = metadata.isCritical("resistance"); // false if not found
```

### 3. Singleton Registry Side Effects
- Test isolation issue: Custom registration persists across tests
- Solution: Use unregistered types (CRYSTAL, FUSE) for tests, not pre-registered types (RESISTOR, CAPACITOR)

### 4. Builder Validation
```java
// Throws IllegalArgumentException (not NPE) for null component type
ComponentTypeMetadata.builder(null); // ✗ IllegalArgumentException

// Throws IllegalStateException if no specs added
ComponentTypeMetadata.builder(ComponentType.IC).build(); // ✗ IllegalStateException
```

### 5. Map.get(null) NPE
```java
// getSpecConfig(null) throws NullPointerException (expected Map behavior)
metadata.getSpecConfig(null); // ✗ NullPointerException
```

### 6. Profile Multiplier Values
- COST_OPTIMIZATION maintains CRITICAL=1.0 (not 0.9) - safety specs never compromised
- EMERGENCY_SOURCING relaxes CRITICAL to 0.8 - only for truly urgent scenarios
- PERFORMANCE_UPGRADE has CRITICAL=1.0, HIGH=0.8 (not inverted)

### 7. Tolerance Rule Acceptance Thresholds
- Default: 0.7 (from interface default method)
- MinimumRequiredRule: 0.8 (stricter because exact match required)
- Always check `isAcceptable()` in addition to score for filtering

### 8. Test Coverage Strategy
- Use nested @DisplayName test classes for organization
- Use @ParameterizedTest with @ValueSource/@CsvSource for data-driven tests
- Document expected behavior in test names (shouldXxxWhenYyy pattern)
- Test both positive and negative cases (shouldAccept vs shouldReject)

### 9. API Discovery Lessons
- `ToleranceRule.compare()` not `calculateScore()`
- `SpecUnit.OHMS` not `SpecUnit.OHM` (plural form)
- Always read interfaces before implementing

### 10. Test Expectations for Migration
- Old hardcoded ranges [0.0, 0.8] / [0.0, 0.9] → New normalized [0.0, 1.0]
- Update assertions with explanatory comments about metadata-driven scoring
- Document expected score changes in test comments

### 11. Voltage Asymmetry
- Don't try to enforce symmetry for MinimumRequiredRule comparisons
- Test the correctness of the asymmetry, not its absence
- Document the business logic behind the asymmetry

### 12. Unicode Gotcha: Micro Sign (µ) vs Greek Mu (Μ)

**Problem**: The micro sign µ (U+00B5) becomes Greek capital MU Μ (U+039C) when uppercased in Java:
```java
"0.1µF".toUpperCase() // Returns "0.1ΜF" (Greek MU, not micro!)
"0.1µF".toUpperCase().contains("µF") // false! Doesn't match
```

**Solution**: Replace micro variants before normalizing:
```java
String normalized = value.replace("µ", "u").replace("Μ", "u");
normalized = normalizeValue(normalized); // Now toUpperCase() works
if (normalized.contains("UF")) { // Matches both µF and plain UF
    // Parse value
}
```

**Why This Matters**: Component MPNs use µF for microfarads, and normalizeValue() calls toUpperCase(). Without the replacement, value parsing silently fails and comparisons return 0.0 similarity.

**Affected**: CapacitorSimilarityCalculator parseCapacitanceValue() method. Any future value parsing with Greek-origin SI prefixes (µ, Ω) must handle this.

---

## Metadata-Driven Benefits

1. **Consistent scoring** - All calculators use same weighting system
2. **Easy tuning** - Change weights without touching calculator code
3. **Context-aware profiles** - Different thresholds for different scenarios
4. **Self-documenting** - Metadata explains what specs matter and why
5. **Type-safe** - SpecValue provides unit awareness
6. **Testable** - 165 tests verify metadata behavior
7. **Extensible** - Easy to add new component types

---

## Conversion Status (January 2026)

**12 of 17 calculators converted (71% complete)**

| Calculator | Status | Specs | Critical Specs |
|-----------|--------|-------|----------------|
| ResistorSimilarityCalculator | ✅ | resistance, package, tolerance | resistance |
| CapacitorSimilarityCalculator | ✅ | capacitance, voltage, dielectric, package | capacitance, voltage |
| TransistorSimilarityCalculator | ✅ | polarity, voltageRating, currentRating, hfe, package | polarity, voltageRating, currentRating |
| DiodeSimilarityCalculator | ✅ | type, voltageRating, currentRating, package | type, voltageRating, currentRating |
| MosfetSimilarityCalculator | ✅ | channel, voltageRating, currentRating, rdsOn, package | channel, voltageRating, currentRating |
| VoltageRegulatorSimilarityCalculator | ✅ | regulatorType, outputVoltage, polarity, currentRating, package | regulatorType, outputVoltage, polarity |
| OpAmpSimilarityCalculator | ✅ | configuration, family, package | configuration |
| MemorySimilarityCalculator | ✅ | memoryType, capacity, interface, package | memoryType, capacity |
| LEDSimilarityCalculator | ✅ | color, family, brightness, package | color |
| ConnectorSimilarityCalculator | ✅ | pinCount, pitch, family, mountingType | pinCount, pitch |
| LogicICSimilarityCalculator | ✅ | function, series, technology, package | function |
| SensorSimilarityCalculator | ✅ | sensorType, family, interface, package | sensorType |
| MCUSimilarityCalculator | ⏳ | family, series, features | - |
| MicrocontrollerSimilarityCalculator | ⏳ | manufacturer, series, package | - |
| PassiveComponentCalculator | ⏳ | value, sizeCode, tolerance | - |
| DefaultSimilarityCalculator | ⏳ | - | - |
| LevenshteinCalculator | ⏳ | - | - |

---

## Related Skills

- `/similarity` - Main similarity architecture and calculator overview
- `/similarity-resistor` - Resistor-specific similarity patterns
- `/similarity-capacitor` - Capacitor-specific similarity patterns
- `/similarity-transistor` - Transistor-specific similarity patterns
- `/similarity-mosfet` - MOSFET-specific similarity patterns
- `/similarity-diode` - Diode-specific similarity patterns
- All other `/similarity-*` skills for component-specific patterns

---

## See Also

- `HISTORY.md` - Historical conversion progress and milestones
- `.docs/history/SIMILARITY_METADATA_EVOLUTION.md` - Detailed conversion journey (if created)
- `src/main/java/no/cantara/electronic/component/lib/metadata/` - Implementation classes
- `src/test/java/no/cantara/electronic/component/lib/metadata/` - Test suite
