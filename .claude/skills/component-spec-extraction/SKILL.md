# Component Spec Extraction

Use this skill when working with component specification extraction, SpecValue wrappers, or type-specific extraction patterns.

## Spec Class Locations

**24 spec classes** organized in 4 packages:

### base/ Package (Core Specs)
- `ComponentSpecs.java` - Base interface for all specs
- `Spec.java` - Generic spec value holder
- `SpecUnit.java` - Enum of measurement units
- `SpecValue.java` - Type-safe unit-aware wrapper

### passive/ Package (Passive Components)
- `ResistorSpecs.java` - Resistance, tolerance, power rating
- `CapacitorSpecs.java` - Capacitance, voltage, dielectric, ESR
- `InductorSpecs.java` - Inductance, current rating, DCR

### semiconductor/ Package (Semiconductors)
- `DiodeSpecs.java` - Forward voltage, reverse voltage, current
- `TransistorSpecs.java` - VCE, IC, hFE, polarity
- `MosfetSpecs.java` - VDS, ID, RDS(on), channel type
- `OpAmpSpecs.java` - Configuration, GBW, input offset
- `VoltageRegulatorSpecs.java` - Output voltage, current, dropout

### power/ Package (Power Components)
- `PowerSupplySpecs.java` - Input/output voltage, efficiency
- `BatterySpecs.java` - Capacity, chemistry, voltage

**Additional specs:**
- MemorySpecs, SensorSpecs, ConnectorSpecs, LEDSpecs, LogicICSpecs, MCUSpecs, etc.

---

## SpecValue<T> Wrapper Pattern

**Purpose:** Type-safe, unit-aware value representation.

**Constructor (NO static factory method):**
```java
// ✅ CORRECT: Use constructor directly
SpecValue<Double> resistance = new SpecValue<>(1000.0, SpecUnit.OHM);
SpecValue<String> dielectric = new SpecValue<>("X7R", SpecUnit.NONE);

// ❌ WRONG: No factory method exists
// SpecValue<Double> resistance = SpecValue.of(1000.0, SpecUnit.OHM);  // DOESN'T EXIST!
```

**Implementation:**
```java
public class SpecValue<T> {
    private final T value;
    private final SpecUnit unit;
    private final T minValue;  // Optional range
    private final T maxValue;  // Optional range

    // Constructor
    public SpecValue(T value, SpecUnit unit) {
        this.value = value;
        this.unit = unit;
        this.minValue = null;
        this.maxValue = null;
    }

    // Constructor with range
    public SpecValue(T value, SpecUnit unit, T minValue, T maxValue) {
        this.value = value;
        this.unit = unit;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public T getValue() { return value; }
    public SpecUnit getUnit() { return unit; }
    public T getMinValue() { return minValue; }
    public T getMaxValue() { return maxValue; }
}
```

**Examples:**
```java
// Resistance with tolerance
SpecValue<Double> resistance = new SpecValue<>(10000.0, SpecUnit.OHM, 9500.0, 10500.0);

// Capacitance
SpecValue<Double> capacitance = new SpecValue<>(100e-9, SpecUnit.FARAD);  // 100nF

// Voltage rating
SpecValue<Double> voltage = new SpecValue<>(50.0, SpecUnit.VOLT);

// Dielectric (no unit)
SpecValue<String> dielectric = new SpecValue<>("X7R", SpecUnit.NONE);
```

---

## Extraction Method Naming

**Convention:** Consistent camelCase method names across all files.

**Patterns:**
```java
// ✅ CORRECT: extractXxx() pattern
public String extractResistance(String mpn);
public String extractCapacitance(String mpn);
public String extractVoltage(String mpn);
public String extractPackage(String mpn);
public String extractTolerance(String mpn);
public String extractDielectric(String mpn);

// ❌ WRONG: Inconsistent naming
public String getResistance(String mpn);       // Don't mix get/extract
public String parseCapacitance(String mpn);   // Don't use parse
public String resistance(String mpn);          // Too terse
```

**Rationale:**
- `extract` implies parsing from MPN string
- `get` implies retrieving existing value
- Consistency aids navigation and maintenance

---

## Unit Awareness

**SpecUnit Enum Values:**
```java
public enum SpecUnit {
    // Electrical
    OHM("Ω"),           // Resistance
    FARAD("F"),         // Capacitance
    HENRY("H"),         // Inductance
    VOLT("V"),          // Voltage
    AMPERE("A"),        // Current
    WATT("W"),          // Power
    HERTZ("Hz"),        // Frequency

    // Dimensionless
    PERCENTAGE("%"),
    NONE("");

    private final String symbol;

    SpecUnit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
```

**Unit Conversion Patterns:**
```java
// Resistor values: k, M multipliers
"1k"  → 1000.0 Ω
"10k" → 10000.0 Ω
"1M"  → 1000000.0 Ω

// Capacitor values: p, n, u, m multipliers
"10pF" → 10e-12 F
"100nF" → 100e-9 F
"1uF" → 1e-6 F
"10mF" → 10e-3 F

// Voltage: m, k multipliers
"3.3V" → 3.3 V
"5V" → 5.0 V
"400kV" → 400000.0 V
```

---

## Type-Specific Extraction Patterns

### Resistor: Value from MPN Position

**Chip resistors (e.g., Yageo RC0805JR-0710KL):**
```java
public String extractResistance(String mpn) {
    // RC0805JR-0710KL
    // Positions 9-12: "10K" → 10kΩ

    if (mpn.length() < 13) return "";

    String valueCode = mpn.substring(9, 13);  // "10KL"
    // Parse "10K" → 10000Ω
}
```

### Capacitor: Value from Code (104 = 100nF)

**Ceramic capacitors use 3-digit codes:**
```java
public String extractCapacitance(String mpn) {
    // GRM188R71H104KA01D
    // "104" in MPN → 10 × 10^4 pF = 100,000pF = 100nF

    Pattern codePattern = Pattern.compile("(\\d{3})");
    Matcher matcher = codePattern.matcher(mpn);
    if (matcher.find()) {
        String code = matcher.group(1);
        int digit1 = code.charAt(0) - '0';
        int digit2 = code.charAt(1) - '0';
        int multiplier = code.charAt(2) - '0';

        double picofarads = (digit1 * 10 + digit2) * Math.pow(10, multiplier);
        return picofarads + "pF";
    }
}
```

**Examples:**
- 104 → 10 × 10^4 pF = 100nF
- 105 → 10 × 10^5 pF = 1µF
- 106 → 10 × 10^6 pF = 10µF

### MOSFET: Voltage/Current from Suffix

**Infineon IRFxxx series:**
```java
public String extractVoltage(String mpn) {
    // IRF530 → 100V
    // IRF540 → 100V
    // IRFZ44 → 55V

    // Known mappings for Infineon MOSFETs
    if (mpn.startsWith("IRF530")) return "100V";
    if (mpn.startsWith("IRF540")) return "100V";
    if (mpn.startsWith("IRFZ44")) return "55V";

    // Or parse from datasheet mapping table
}
```

---

## Null Handling Strategies

### Return null vs empty string vs Optional

**Pattern 1: Return empty string (most common)**
```java
public String extractPackage(String mpn) {
    if (mpn == null || mpn.isEmpty()) {
        return "";  // Empty string indicates "not found"
    }

    // Extraction logic...
    if (packageNotFound) {
        return "";
    }

    return packageCode;
}
```

**Pattern 2: Return null (for numeric values)**
```java
public Double extractCapacitance(String mpn) {
    if (mpn == null) {
        return null;  // null indicates "not found"
    }

    // Extraction logic...
    if (cannotParse) {
        return null;
    }

    return capacitanceValue;
}
```

**Pattern 3: Return Optional (modern approach)**
```java
public Optional<String> extractDielectric(String mpn) {
    if (mpn == null) {
        return Optional.empty();
    }

    // Extraction logic...
    if (dielectricNotFound) {
        return Optional.empty();
    }

    return Optional.of(dielectricType);
}
```

**Recommendation:** Use empty string for String returns, null for numeric returns.

### Validation Before Extraction

```java
public String extractResistance(String mpn) {
    // ✅ CORRECT: Validate first
    if (mpn == null || mpn.isEmpty()) {
        return "";
    }

    if (mpn.length() < MINIMUM_LENGTH) {
        return "";  // MPN too short to contain resistance code
    }

    // Now safe to extract
    String valueCode = mpn.substring(startIndex, endIndex);
}
```

---

## Integration with Similarity Calculators

**Metadata-driven similarity uses spec extraction:**

```java
// In ResistorSimilarityCalculator.calculateMetadataDrivenSimilarity():

// Extract resistance spec
String resistance1 = extractResistance(mpn1);
String resistance2 = extractResistance(mpn2);

if (!resistance1.isEmpty() && !resistance2.isEmpty()) {
    // Create SpecValue
    SpecValue<Double> r1 = new SpecValue<>(parseResistance(resistance1), SpecUnit.OHM);
    SpecValue<Double> r2 = new SpecValue<>(parseResistance(resistance2), SpecUnit.OHM);

    // Get tolerance rule from metadata
    ComponentTypeMetadata.SpecConfig resistanceConfig = metadata.getSpecConfig("resistance");
    ToleranceRule rule = resistanceConfig.getToleranceRule();

    // Compare
    double specScore = rule.compare(r1, r2);
    double specWeight = profile.getEffectiveWeight(resistanceConfig.getImportance());

    totalScore += specScore * specWeight;
}
```

---

## Learnings & Quirks

### No SpecValue Factory Method
Use `new SpecValue<>(value, unit)` constructor, not a factory method (doesn't exist).

### camelCase Naming is Mandatory
All extraction methods use extractXxx() pattern. Don't mix with getXxx() or parseXxx().

### Null Handling Varies by Type
- String returns: empty string ""
- Numeric returns: null
- Modern APIs: Optional<T>

### Units Matter for Comparison
10kΩ and 10000Ω are the same value but different strings. Always convert to base unit (Ω) before comparison.

### Manufacturer-Specific Extraction
Each manufacturer has unique MPN encoding. Resistor extraction for Yageo differs from Vishay, KOA, etc.

---

## See Also

- `/similarity-metadata` - How specs are used in metadata-driven similarity
- `/metadata-driven-similarity-conversion` - Converting calculators to use spec extraction
- 24 spec classes in 4 packages (base/, passive/, semiconductor/, power/)
- `SpecValue.java` - Type-safe wrapper implementation
- `SpecUnit.java` - Measurement units enum
