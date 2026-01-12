# Parametric Search Skill

This skill provides guidance for using the ParametricSearch utility to filter electronic components by their specifications.

## Overview

`ParametricSearch` enables filtering collections of `ElectronicPart` objects using unit-aware parametric queries. It supports:
- Comparison operators (>=, <=, >, <, =, !=)
- Range queries (min..max)
- Set membership (IN)
- Automatic unit parsing (nF, uF, kΩ, etc.)

## Quick Reference

### Query Syntax

| Syntax | Example | Matches |
|--------|---------|---------|
| `>= value` | `>= 10nF` | Capacitors 10nF and above |
| `<= value` | `<= 50V` | Voltage 50V and below |
| `> value` | `> 1k` | Resistance above 1kΩ |
| `< value` | `< 1uF` | Capacitance below 1µF |
| `= value` | `= X7R` | Exact string match |
| `!= value` | `!= X5R` | Not equal to string |
| `min..max` | `10nF..1uF` | Range (inclusive) |
| `IN(a,b,c)` | `IN(X7R, X5R)` | Any of listed values |

### Unit Prefixes

| Prefix | Multiplier | Example |
|--------|------------|---------|
| p | 10⁻¹² | 100pF |
| n | 10⁻⁹ | 100nF |
| u/µ | 10⁻⁶ | 10uF, 10µF |
| m | 10⁻³ | 100mV |
| k/K | 10³ | 10k, 10K |
| M | 10⁶ | 1M |
| G | 10⁹ | 1GHz |

## Usage Patterns

### Static Filter Method

```java
Map<String, String> requirements = Map.of(
    "capacitance", ">= 10nF",
    "voltage", ">= 25V",
    "dielectric", "IN(X7R, X5R)"
);

List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);
```

### Fluent Builder

```java
List<ElectronicPart> results = ParametricSearch.search(capacitors)
    .min("capacitance", "10nF")
    .max("voltage", "50V")
    .in("dielectric", "X7R", "X5R", "C0G")
    .find();
```

### Range Queries

```java
// Find resistors between 1kΩ and 100kΩ
List<ElectronicPart> results = ParametricSearch.search(resistors)
    .range("resistance", "1k", "100k")
    .find();

// Equivalent using where()
List<ElectronicPart> results = ParametricSearch.search(resistors)
    .where("resistance", "1k..100k")
    .find();
```

### Single Part Check

```java
ElectronicPart capacitor = ...;

if (ParametricSearch.meets(capacitor, "capacitance", ">= 10nF")) {
    // Part meets requirement
}
```

### Utility Methods

```java
// Count matching parts
long count = ParametricSearch.search(parts)
    .min("voltage", "25V")
    .count();

// Check if any match
boolean hasHighVoltage = ParametricSearch.search(parts)
    .min("voltage", "100V")
    .anyMatch();

// Get first match
Optional<ElectronicPart> first = ParametricSearch.search(parts)
    .equals("dielectric", "C0G")
    .findFirst();
```

## Builder Methods

| Method | Description |
|--------|-------------|
| `where(key, requirement)` | Add any requirement string |
| `min(key, value)` | Add `>= value` requirement |
| `max(key, value)` | Add `<= value` requirement |
| `range(key, min, max)` | Add `min..max` requirement |
| `equals(key, value)` | Add `= value` requirement |
| `notEquals(key, value)` | Add `!= value` requirement |
| `in(key, values...)` | Add `IN(values)` requirement |
| `find()` | Execute and return List |
| `findFirst()` | Execute and return Optional |
| `count()` | Execute and return count |
| `anyMatch()` | Execute and return boolean |

## Searchable Fields

### From specs Map

Any key in `ElectronicPart.getSpecs()`:
- `capacitance`, `resistance`, `inductance`
- `voltage`, `current`, `power`
- `tolerance`, `tempco`
- `dielectric`, `material`
- Custom spec keys

### Built-in Fields

| Key | Field |
|-----|-------|
| `value` | `getValue()` |
| `package`, `pkg` | `getPkg()` |
| `manufacturer`, `mfr` | `getManufacturer()` |
| `description`, `desc` | `getDescription()` |
| `mpn` | `getMpn()` |

## Common Use Cases

### Find High-Voltage X7R Capacitors

```java
List<ElectronicPart> results = ParametricSearch.search(capacitors)
    .min("voltage", "50V")
    .equals("dielectric", "X7R")
    .find();
```

### Find Precision Resistors

```java
List<ElectronicPart> results = ParametricSearch.search(resistors)
    .where("tolerance", "<= 0.1%")
    .in("package", "0402", "0603", "0805")
    .find();
```

### Filter BOM by Manufacturer

```java
List<ElectronicPart> murataOnly = ParametricSearch.search(bomEntries)
    .equals("manufacturer", "Murata")
    .find();
```

### Find Capacitors in Value Range

```java
List<ElectronicPart> decouplingCaps = ParametricSearch.search(capacitors)
    .range("capacitance", "100nF", "10uF")
    .min("voltage", "16V")
    .in("dielectric", "X7R", "X5R")
    .find();
```

## Integration with BOM

```java
public List<BOMEntry> findObsoleteHighValueParts(BOM bom) {
    return ParametricSearch.search(bom.getBomEntries())
        .min("capacitance", "10uF")  // High value
        .find()
        .stream()
        .filter(ElectronicPart::isLifecycleAtRisk)  // Obsolete
        .map(p -> (BOMEntry) p)
        .toList();
}
```

## Gotchas

### 1. Case Sensitivity

- **Unit prefixes**: `m` = milli, `M` = mega (case matters!)
- **k/K**: Both work for kilo
- **String values**: Case-insensitive (`X7R` matches `x7r`)

### 2. Missing Specs

Parts without the requested spec are **excluded** from results:

```java
// Part without "voltage" spec will NOT be in results
ParametricSearch.filter(parts, Map.of("voltage", ">= 25V"));
```

### 3. Null Handling

```java
// Empty/null collection returns empty list
ParametricSearch.filter(null, requirements);  // []
ParametricSearch.filter(List.of(), requirements);  // []

// Null/empty requirements returns all parts
ParametricSearch.filter(parts, null);  // all parts
ParametricSearch.filter(parts, Map.of());  // all parts
```

### 4. Unit Stripping

Units are stripped for comparison:
- `"50V"` and `"50"` compare as equal
- `"10kΩ"` and `"10k"` compare as equal

### 5. Works with Subclasses

`BOMEntry` extends `ElectronicPart`, so filtering works:

```java
List<BOMEntry> entries = ...;
List<ElectronicPart> results = ParametricSearch.filter(entries, requirements);
// Results can be cast back to BOMEntry if needed
```

## Performance Notes

- Predicates are built once per filter() call
- Each part is evaluated against all predicates
- For large collections, consider pre-filtering by type first
- No indexing - O(n) per filter operation

## Related Classes

- `ComponentValueStandardizer` - Value parsing (used internally)
- `SpecValue` / `SpecUnit` - Typed spec system
- `BaseComponentSpecs.meetsRequirements()` - Similar pattern for typed specs

## Learnings & Quirks

### January 2026

- **BigDecimal comparison**: Use `compareTo()`, not `equals()` - `1E+3` equals `1000` by value
- **Micro symbol**: Both `u` and `µ` are supported for micro prefix
- **Percentage handling**: `1%` parses to `1`, not `0.01` (matches how tolerances are typically specified)

<!-- Add new learnings above this line -->
