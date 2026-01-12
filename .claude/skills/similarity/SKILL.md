---
name: similarity
description: Use when working with component similarity calculations - comparing MPNs, finding equivalent parts, implementing new similarity calculators, or understanding how component matching works.
---

# Component Similarity Calculator Skill

This skill provides guidance for working with component similarity calculators in the lib-electronic-components library.

## Overview

Similarity calculators determine how similar two electronic components are based on their MPNs (Manufacturer Part Numbers). They return a value between 0.0 (completely different) and 1.0 (identical or equivalent).

## Core Interfaces

### SimilarityCalculator (Simple)
```java
public interface SimilarityCalculator {
    double calculateSimilarity(String normalizedMpn1, String normalizedMpn2);
}
```
Used for generic calculators that don't need component type context.

### ComponentSimilarityCalculator (Type-Aware)
```java
public interface ComponentSimilarityCalculator {
    boolean isApplicable(ComponentType type);
    double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry);
}
```
Used for component-specific calculators that need to check applicability.

## Standard Similarity Thresholds

```java
private static final double HIGH_SIMILARITY = 0.9;    // Equivalent/interchangeable
private static final double MEDIUM_SIMILARITY = 0.7;  // Similar, may work as substitute
private static final double LOW_SIMILARITY = 0.3;     // Same category but different specs
```

## Available Calculators

| Calculator | Interface | Component Types | Key Features |
|------------|-----------|-----------------|--------------|
| `ResistorSimilarityCalculator` | Component | RESISTOR, RESISTOR_* | Value, package, tolerance |
| `CapacitorSimilarityCalculator` | Component | CAPACITOR, CAPACITOR_* | Value, voltage, dielectric |
| `TransistorSimilarityCalculator` | Component | TRANSISTOR, TRANSISTOR_* | NPN/PNP polarity, equivalent groups |
| `DiodeSimilarityCalculator` | Component | DIODE, DIODE_* | Signal/rectifier/zener types |
| `MosfetSimilarityCalculator` | Component | MOSFET, MOSFET_* | N/P channel, equivalent groups |
| `OpAmpSimilarityCalculator` | Component | OPAMP, OPAMP_* | Single/dual/quad, equivalent families |
| `VoltageRegulatorSimilarityCalculator` | Component | VOLTAGE_REGULATOR* | Fixed (78xx) vs adjustable (LM317) |
| `LogicICSimilarityCalculator` | Component | LOGIC_IC, IC | 74xx/CD4000 series, function groups |
| `LEDSimilarityCalculator` | Component | LED, LED_* | Color, bins, families |
| `MemorySimilarityCalculator` | Component | MEMORY, MEMORY_* | I2C/SPI EEPROM, Flash equivalents |
| `SensorSimilarityCalculator` | Component | SENSOR, TEMPERATURE_SENSOR, ACCELEROMETER | Sensor families, package variants |
| `ConnectorSimilarityCalculator` | Component | CONNECTOR, CONNECTOR_* | Pin count, pitch, family |
| `MicrocontrollerSimilarityCalculator` | Component | MICROCONTROLLER* | Series, package, manufacturer |
| `MCUSimilarityCalculator` | Simple | (generic) | Family, series, features |
| `PassiveComponentCalculator` | Simple | (generic) | Value, size code, tolerance |
| `LevenshteinCalculator` | Simple | (generic) | String edit distance |
| `DefaultSimilarityCalculator` | Simple | (generic) | Prefix, numeric, suffix weights |

## Creating a New Similarity Calculator

### 1. Implement the Interface

```java
public class NewComponentSimilarityCalculator implements ComponentSimilarityCalculator {
    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) return false;
        return type == ComponentType.NEW_COMPONENT ||
               type.name().startsWith("NEW_COMPONENT_");
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry registry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        // Check if both are the component type we handle
        if (!isComponentType(mpn1) || !isComponentType(mpn2)) {
            return 0.0;
        }

        // Compare components
        // ...
        return similarity;
    }
}
```

### 2. Key Design Principles

1. **Return 0.0 for null inputs** - Always check for null MPNs and registry
2. **Return 0.0 for non-matching types** - If the MPN isn't your component type
3. **Use equivalent groups** - Define known equivalent parts (e.g., 2N2222 ≈ PN2222)
4. **Consider package variants** - Same part in different package should be high similarity
5. **Ensure symmetry** - `sim(A,B) == sim(B,A)`
6. **Keep in [0.0, 1.0]** - Never return values outside this range

### 3. Common Patterns

#### Equivalent Groups
```java
private static final Map<String, Set<String>> EQUIVALENT_GROUPS = new HashMap<>();
static {
    EQUIVALENT_GROUPS.put("2N2222", Set.of("2N2222", "2N2222A", "PN2222", "PN2222A"));
}

private boolean areEquivalent(String mpn1, String mpn2) {
    for (Set<String> group : EQUIVALENT_GROUPS.values()) {
        if (group.contains(mpn1) && group.contains(mpn2)) {
            return true;
        }
    }
    return false;
}
```

#### Package Code Extraction
```java
private String extractBasePart(String mpn) {
    // Remove common package suffixes
    return mpn.replaceAll("(?:CT|T|N|P|DG|PW|DR)$", "");
}
```

#### Polarity/Type Checking
```java
private boolean areSamePolarity(String mpn1, String mpn2) {
    boolean isNPN1 = NPN_PATTERNS.stream().anyMatch(mpn1::matches);
    boolean isNPN2 = NPN_PATTERNS.stream().anyMatch(mpn2::matches);
    return isNPN1 == isNPN2;
}
```

## Testing

### Test Structure
```java
@Nested
@DisplayName("isApplicable tests")
class IsApplicableTests { /* ... */ }

@Nested
@DisplayName("Equivalent groups tests")
class EquivalentGroupTests { /* ... */ }

@Nested
@DisplayName("Edge cases and null handling")
class EdgeCaseTests { /* ... */ }

@Nested
@DisplayName("Symmetry and property tests")
class PropertyTests { /* ... */ }
```

### Run Tests
```bash
# All similarity calculator tests
mvn test -Dtest="*SimilarityCalculatorTest,PassiveComponentCalculatorTest"

# Specific calculator
mvn test -Dtest=TransistorSimilarityCalculatorTest
```

## Related Skills

- `/similarity-resistor` - Resistor similarity details
- `/similarity-transistor` - Transistor equivalent groups and polarity
- `/similarity-mosfet` - MOSFET N/P channel comparison
- `/similarity-opamp` - Op-amp families and equivalents
- `/similarity-memory` - Memory IC equivalents (I2C/SPI EEPROM, Flash)
- `/similarity-sensor` - Sensor family comparison
- `/similarity-led` - LED bins and color temperature
- `/similarity-regulator` - Voltage regulator comparison (78xx, LM317)
- `/similarity-logic` - Logic IC function groups (74xx, CD4000)

---

## Learnings & Quirks

### General Patterns
- Always normalize MPNs to uppercase before comparison
- Package suffixes vary by manufacturer - don't assume consistency
- Some calculators return HIGH_SIMILARITY (0.9) for identical parts, not 1.0

### Edge Cases
- Parts starting with digits (e.g., "2N2222") need special regex handling - `^[A-Za-z]+` won't match
- Some MPNs have significant hyphens (Molex) vs decorative hyphens (TI)
- Reel/tape suffixes (-RL, -T, -TR) should generally be ignored

<!-- Add new learnings above this line -->
