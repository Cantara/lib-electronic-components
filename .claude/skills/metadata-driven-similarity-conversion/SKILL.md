# Metadata-Driven Similarity Conversion

Use this skill when converting existing similarity calculators to the metadata-driven approach with configurable, type-specific rules.

## 5-Step Conversion Process

### Step 1: Add Imports

Add these imports to your calculator class:

```java
import no.cantara.electronic.component.lib.metadata.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.metadata.ComponentTypeMetadataRegistry;
import no.cantara.electronic.component.lib.metadata.SimilarityProfile;
import no.cantara.electronic.component.lib.metadata.ToleranceRule;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

import java.util.Optional;
```

---

### Step 2: Modify calculateSimilarity() to Check for Metadata First

```java
// Add field
private final ComponentTypeMetadataRegistry metadataRegistry =
    ComponentTypeMetadataRegistry.getInstance();

@Override
public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
    if (mpn1 == null || mpn2 == null) return 0.0;

    // ✅ NEW: Try metadata-driven approach first
    Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.RESISTOR);
    if (metadataOpt.isPresent()) {
        logger.trace("Using metadata-driven similarity calculation");
        return calculateMetadataDrivenSimilarity(mpn1, mpn2, metadataOpt.get());
    }

    // Fallback to legacy pattern-based approach
    logger.trace("No metadata found, using legacy approach");
    return calculateLegacySimilarity(mpn1, mpn2);
}
```

---

### Step 3: Implement calculateMetadataDrivenSimilarity()

```java
private double calculateMetadataDrivenSimilarity(String mpn1, String mpn2, ComponentTypeMetadata metadata) {
    SimilarityProfile profile = metadata.getDefaultProfile();

    // Extract specs from MPNs
    String resistance1 = extractResistance(mpn1);
    String resistance2 = extractResistance(mpn2);
    String package1 = extractPackage(mpn1);
    String package2 = extractPackage(mpn2);
    String tolerance1 = extractTolerance(mpn1);
    String tolerance2 = extractTolerance(mpn2);

    // Short-circuit check for CRITICAL incompatibility
    ComponentTypeMetadata.SpecConfig resistanceConfig = metadata.getSpecConfig("resistance");
    if (resistanceConfig != null &&
        resistanceConfig.getImportance() == SpecImportance.CRITICAL &&
        !resistance1.isEmpty() && !resistance2.isEmpty() &&
        !resistance1.equals(resistance2)) {
        logger.debug("CRITICAL spec mismatch - returning LOW_SIMILARITY");
        return LOW_SIMILARITY;
    }

    double totalScore = 0.0;
    double maxPossibleScore = 0.0;

    // Compare each spec with weighted scoring
    if (resistanceConfig != null && !resistance1.isEmpty() && !resistance2.isEmpty()) {
        ToleranceRule rule = resistanceConfig.getToleranceRule();
        SpecValue<String> orig = new SpecValue<>(resistance1, SpecUnit.OHM);
        SpecValue<String> cand = new SpecValue<>(resistance2, SpecUnit.OHM);

        double specScore = rule.compare(orig, cand);
        double specWeight = profile.getEffectiveWeight(resistanceConfig.getImportance());

        totalScore += specScore * specWeight;
        maxPossibleScore += specWeight;
    }

    // Repeat for other specs (package, tolerance, etc.)
    // ...

    double similarity = maxPossibleScore > 0 ? totalScore / maxPossibleScore : 0.0;

    // Apply boosts for known equivalent groups
    if (areEquivalentParts(mpn1, mpn2)) {
        similarity = Math.max(similarity, HIGH_SIMILARITY);
    }

    return similarity;
}
```

---

### Step 4: Add Spec Extraction Helper Methods

```java
private String extractResistance(String mpn) {
    if (mpn == null || mpn.isEmpty()) return "";
    // Manufacturer-specific extraction logic
    // e.g., Yageo RC0805JR-0710KL → "10K"
    return "";
}

private String extractPackage(String mpn) {
    if (mpn == null || mpn.isEmpty()) return "";
    // e.g., RC0805 → "0805"
    return "";
}

private String extractTolerance(String mpn) {
    if (mpn == null || mpn.isEmpty()) return "";
    // e.g., "JR" → "5%"
    return "";
}
```

---

### Step 5: Update Tests to Use Threshold Assertions

**BEFORE (exact equality):**
```java
@Test
void shouldCalculateHighSimilarityForSameResistance() {
    double similarity = calculator.calculateSimilarity("RC0805JR-0710KL", "RC0805FR-0710KL", registry);
    assertEquals(0.9, similarity);  // ❌ Exact match - fragile!
}
```

**AFTER (threshold assertion):**
```java
@Test
void shouldCalculateHighSimilarityForSameResistance() {
    double similarity = calculator.calculateSimilarity("RC0805JR-0710KL", "RC0805FR-0710KL", registry);
    assertTrue(similarity >= HIGH_SIMILARITY,  // ✅ Threshold - robust!
        "Same resistance should have HIGH_SIMILARITY (>= 0.9), got: " + similarity);
}
```

**Why:** Metadata-driven approach produces more precise scores (0.95-0.99 for very similar parts) instead of fixed thresholds (0.9).

---

## Dual-Path Implementation

**Pattern:** Metadata first, legacy fallback.

```java
@Override
public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
    if (mpn1 == null || mpn2 == null) return 0.0;

    // Try metadata-driven approach first
    Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.OPAMP);
    if (metadataOpt.isPresent()) {
        logger.trace("Using metadata-driven similarity calculation");
        return calculateMetadataDrivenSimilarity(mpn1, mpn2, metadataOpt.get());
    }

    // Fallback to legacy pattern-based approach
    logger.trace("No metadata found, using legacy approach");
    return calculateLegacySimilarity(mpn1, mpn2);
}
```

**Example from ResistorSimilarityCalculator:**
- Has metadata → use calculateMetadataDrivenSimilarity()
- No metadata → use calculateLegacySimilarity()

**Benefits:**
- Backward compatibility (no metadata = legacy behavior)
- Gradual migration (convert one calculator at a time)
- Easy rollback (remove metadata to revert to legacy)

---

## Short-Circuit Critical Spec Checks

**Pattern:** Check CRITICAL specs early, return 0.0 on mismatch.

```java
// Short-circuit check for CRITICAL incompatibility
ComponentTypeMetadata.SpecConfig capacitanceConfig = metadata.getSpecConfig("capacitance");
ComponentTypeMetadata.SpecConfig voltageConfig = metadata.getSpecConfig("voltage");

// Capacitance mismatch
if (capacitanceConfig != null &&
    capacitanceConfig.getImportance() == SpecImportance.CRITICAL &&
    !capacitance1.isEmpty() && !capacitance2.isEmpty() &&
    !capacitance1.equals(capacitance2)) {
    logger.debug("CRITICAL capacitance mismatch - returning 0.0");
    return 0.0;
}

// Voltage downgrade (50V → 5V is incompatible)
if (voltageConfig != null &&
    voltageConfig.getImportance() == SpecImportance.CRITICAL &&
    voltage1 > voltage2) {
    logger.debug("CRITICAL voltage downgrade - returning 0.0");
    return 0.0;
}
```

**Example (Capacitor):**
- 100nF 50V vs 100nF 5V → return 0.0 (voltage downgrade)
- 100nF 50V vs 1µF 50V → return 0.0 (capacitance mismatch)

---

## Weighted Scoring Formula

```
similarity = totalScore / maxPossibleScore

where:
  totalScore = Σ(specScore × effectiveWeight)
  maxPossibleScore = Σ(effectiveWeight)
  effectiveWeight = profile.getEffectiveWeight(specImportance)
```

**Example calculation (Resistor):**

| Spec | Value1 | Value2 | Score | Importance | Weight | Contribution |
|------|--------|--------|-------|------------|--------|--------------|
| Resistance | 10kΩ | 10kΩ | 1.0 | CRITICAL | 1.0 | 1.0 × 1.0 = 1.0 |
| Package | 0805 | 0805 | 1.0 | MEDIUM | 0.4 | 1.0 × 0.4 = 0.4 |
| Tolerance | 5% | 1% | 0.8 | LOW | 0.2 | 0.8 × 0.2 = 0.16 |

```
totalScore = 1.0 + 0.4 + 0.16 = 1.56
maxPossibleScore = 1.0 + 0.4 + 0.2 = 1.6
similarity = 1.56 / 1.6 = 0.975 ≈ 0.98
```

---

## Test Expectation Updates

### Old Pattern (Exact Equality)

```java
assertEquals(0.9, similarity);
assertEquals(0.0, similarity);
assertEquals(1.0, similarity);
```

**Problem:** Metadata produces more precise scores (0.95, 0.99, 0.703) that don't match exact thresholds.

### New Pattern (Threshold Assertions)

```java
// HIGH similarity (≥ 0.9)
assertTrue(similarity >= HIGH_SIMILARITY,
    "Expected HIGH_SIMILARITY (>= 0.9), got: " + similarity);

// MEDIUM similarity (≥ 0.5)
assertTrue(similarity >= MEDIUM_SIMILARITY,
    "Expected MEDIUM_SIMILARITY (>= 0.5), got: " + similarity);

// LOW similarity (< 0.5)
assertTrue(similarity < MEDIUM_SIMILARITY,
    "Expected LOW_SIMILARITY (< 0.5), got: " + similarity);

// ZERO similarity
assertEquals(0.0, similarity, 0.001,
    "Expected ZERO similarity for incompatible parts");
```

**Constants:**
```java
private static final double HIGH_SIMILARITY = 0.9;
private static final double MEDIUM_SIMILARITY = 0.5;
private static final double LOW_SIMILARITY = 0.3;
```

---

## 5 Remaining Calculators

| Calculator | Complexity | Priority | Notes |
|-----------|------------|----------|-------|
| **PassiveComponentCalculator** | Medium | HIGH | Generic passive handler, needs multi-type metadata |
| **MicrocontrollerSimilarityCalculator** | High | HIGH | Complex specs (flash, RAM, peripherals) |
| **MCUSimilarityCalculator** | High | HIGH | Similar to Microcontroller, may merge |
| **LevenshteinCalculator** | Low | LOW | String-based, may not need metadata |
| **DefaultSimilarityCalculator** | Low | LOW | Fallback, intentionally simple |

**Recommendation:** Start with PassiveComponentCalculator (straightforward, high impact).

---

## Behavior Improvements

### OpAmp Example

**BEFORE (pattern-based):**
- LM358 vs MC1458: 0.9 (equivalent group boost)
- LM358 vs LM324: 0.7 (same manufacturer, different config)

**AFTER (metadata-driven):**
- LM358 vs MC1458: **1.0** (exact configuration + family match)
- LM358 vs LM324: **0.3** (short-circuit on configuration: dual vs quad)

### Memory Example

**BEFORE:**
- 24LC256 vs AT24C256: 0.7+ (equivalent EEPROM)
- 24LC256 vs 24LC512: 0.3 (different capacity)

**AFTER:**
- 24LC256 vs AT24C256: **0.85** (exact match on type, capacity, interface)
- 24LC256 vs 24LC512: **0.3** (short-circuit on capacity)

### Sensor Example

**BEFORE:**
- ADXL345 vs ADXL346: 0.3 (different suffix)
- DS18B20 vs DS18B20+: 0.9 (known equivalent boost)

**AFTER:**
- ADXL345 vs ADXL346: **0.703** (same type + interface = MEDIUM)
- DS18B20 vs DS18B20+: **1.0** (equivalent variants boost)

---

## Migration Checklist

- [ ] Add imports (Step 1)
- [ ] Get metadata in calculateSimilarity() (Step 2)
- [ ] Implement calculateMetadataDrivenSimilarity() (Step 3)
- [ ] Add spec extraction helpers (Step 4)
- [ ] Implement short-circuit CRITICAL checks
- [ ] Implement weighted scoring loop
- [ ] Apply equivalent group boosts
- [ ] Update tests to threshold assertions (Step 5)
- [ ] Run full test suite (verify backward compatibility)
- [ ] Check metadata configuration (SpecImportance, ToleranceRule)

---

## Learnings & Quirks

### Metadata is Optional
If no metadata exists for a type, calculator falls back to legacy behavior. This enables gradual migration.

### Spec Extraction is Manufacturer-Specific
Each manufacturer encodes specs differently in MPNs. Extraction logic must handle multiple formats.

### Threshold Assertions are More Robust
Exact equality (assertEquals(0.9, ...)) breaks when metadata produces 0.95 or 0.99. Use threshold assertions (>= HIGH_SIMILARITY).

### Short-Circuit Saves Computation
Checking CRITICAL specs early avoids unnecessary extraction and comparison of other specs.

---

## See Also

- `/similarity-metadata` - Complete metadata framework documentation
- `/component-spec-extraction` - Spec extraction patterns
- 12 converted calculators - Use as migration templates
- `ComponentTypeMetadata.java` - Metadata configuration
- `SimilarityProfile.java` - Context-aware profiles
