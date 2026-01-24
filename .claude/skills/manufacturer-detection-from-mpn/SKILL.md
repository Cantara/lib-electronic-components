# Manufacturer Detection from MPN

Use this skill when working with manufacturer detection regex patterns, understanding detection ordering, or debugging manufacturer identification issues.

## ComponentManufacturer Enum Structure

**Location:** `ComponentManufacturer.java` (627 lines)

**Format:**
```java
public enum ComponentManufacturer {
    TEXAS_INSTRUMENTS(
        "^(?:LM|TL|TPS|TMP|SN|TLC|TPA|MSP|CC)[0-9]+.*",
        "Texas Instruments",
        () -> new TIHandler()
    ),

    STMICROELECTRONICS(
        "^(?:STM32|STM8|ST|L78|L79|MC78|MC79|VN|VP|STF|STP|STD|STB).*",
        "STMicroelectronics",
        () -> new STHandler()
    ),

    // ... 120+ manufacturers

    UNKNOWN(
        ".*",  // Matches anything
        "Unknown",
        () -> new UnknownHandler()
    );

    private final String pattern;
    private final String name;
    private final Supplier<ManufacturerHandler> handlerSupplier;

    ComponentManufacturer(String pattern, String name, Supplier<ManufacturerHandler> handlerSupplier) {
        this.pattern = pattern;
        this.name = name;
        this.handlerSupplier = handlerSupplier;
    }

    public ManufacturerHandler getHandler() {
        return handlerSupplier.get();
    }
}
```

---

## Regex Pattern Design

### Prefix Specificity Examples

**Simple prefix:**
```java
MURATA("^GRM.*", "Murata Manufacturing", () -> new MurataHandler())
// Matches: GRM188, GRM31, GRM21
```

**Multiple prefixes with alternation:**
```java
TEXAS_INSTRUMENTS(
    "^(?:LM|TL|TPS|TMP|SN|TLC|TPA|MSP|CC)[0-9]+.*",
    "Texas Instruments",
    () -> new TIHandler()
)
// Matches: LM358, TL072, TPS7350, TMP36, SN74HC00, MSP430
```

**Lookahead patterns for disambiguation:**
```java
MICROCHIP(
    "^(?:PIC|DSPIC|AT(?!MEL)|MCP|24[A-Z]{2})[0-9A-Z]+.*",
    "Microchip Technology",
    () -> new MicrochipHandler()
)
// AT(?!MEL) → Matches AT but NOT ATMEL (negative lookahead)
// Matches: PIC16F877, DSPIC33, ATMEGA328 (legacy Atmel), MCP2551, 24LC256
// Doesn't match: ATMEL (that's Atmel, not Microchip)
```

**Character class usage:**
```java
YAGEO(
    "^(?:RC|CC|AC|PT|MF)[0-9]{4}.*",
    "Yageo",
    () -> new YageoHandler()
)
// Character class: [0-9]{4} = exactly 4 digits
// Matches: RC0805, CC0603, AC0402
```

---

## Detection Ordering

**Rule:** Specific patterns before generic patterns. **First match wins.**

### Example: DSPIC before PIC

```java
// ✅ CORRECT ORDER
MICROCHIP("^(?:DSPIC|PIC|AT(?!MEL)).*", ...),  // Checks DSPIC first

// ❌ WRONG ORDER (if they were separate)
MICROCHIP_PIC("^PIC.*", ...),     // Would match DSPIC33FJ128
MICROCHIP_DSPIC("^DSPIC.*", ...); // Never reached!
```

**Why:** "DSPIC33FJ128" starts with both "DSPIC" and "PIC" (substring). Without ordering, PIC would match first.

### Example: Shared LM Prefix

**Multiple manufacturers use "LM" prefix:**

```java
// Order in enum:
TEXAS_INSTRUMENTS("^(?:LM|TL|...).*", ...)      // 1st - most LM parts
STMICROELECTRONICS("^(?:STM32|...|LM).*", ...)  // 2nd - some LM parts
ON_SEMICONDUCTOR("^(?:LM|MC|...).*", ...)        // 3rd - some LM parts
```

**Result:**
- LM358 → TEXAS_INSTRUMENTS (wins first)
- LM7805 → TEXAS_INSTRUMENTS (wins first)
- MC78L05 → STMICROELECTRONICS (ST prefix checked before ON_SEMI)

**This is intentional!** TI produces the majority of LM-series parts (LM358, LM324, LM7805, etc.).

### fromMPN() Detection Flow

```java
public static ComponentManufacturer fromMPN(String mpn) {
    if (mpn == null || mpn.isEmpty()) {
        return UNKNOWN;
    }

    String normalized = MPNUtils.normalize(mpn);  // Uppercase, remove special chars

    // Iterate enum values in declaration order
    for (ComponentManufacturer manufacturer : values()) {
        if (normalized.matches(manufacturer.pattern)) {
            return manufacturer;  // First match wins!
        }
    }

    return UNKNOWN;  // No match (should never reach here - UNKNOWN pattern is .*)
}
```

**Critical properties:**
1. **Enum declaration order matters** - First match wins
2. **Normalization first** - MPNs are uppercased, special chars removed
3. **No fallback chain** - Returns immediately on first match
4. **UNKNOWN always matches** - Catch-all pattern `.*` at end

---

## Handler Supplier Pattern

**Purpose:** Lazy initialization of handlers on first use.

```java
private final Supplier<ManufacturerHandler> handlerSupplier;

TEXAS_INSTRUMENTS(
    "^(?:LM|TL|...).*",
    "Texas Instruments",
    () -> new TIHandler()  // Lambda supplier
);

public ManufacturerHandler getHandler() {
    return handlerSupplier.get();  // Calls lambda, creates new handler
}
```

**Benefits:**
- **Lazy:** Handler only created when needed
- **Stateless:** Each call creates new handler (no shared state issues)
- **Simple:** Lambda syntax is concise

**Alternative (singleton pattern):**
```java
private ManufacturerHandler handler;

public ManufacturerHandler getHandler() {
    if (handler == null) {
        handler = handlerSupplier.get();
    }
    return handler;
}
```

**Trade-off:** Singleton saves memory but adds complexity. Current pattern prefers simplicity.

---

## Common Pattern Gotchas

### 1. Overly Broad Patterns

```java
// ❌ WRONG: Too broad, matches non-manufacturer parts
GENERIC_MFR("^[A-Z]+.*", "Generic", ...)
// Matches EVERYTHING starting with a letter!
```

### 2. Missing Digit Requirements

```java
// ❌ WRONG: Matches "LM" alone
TEXAS_INSTRUMENTS("^LM.*", ...)

// ✅ CORRECT: Requires digit after prefix
TEXAS_INSTRUMENTS("^LM[0-9]+.*", ...)
```

### 3. Case Sensitivity

```java
// ❌ WRONG: Won't match lowercase MPNs
MURATA("^grm.*", ...)

// ✅ CORRECT: Normalize to uppercase first
MURATA("^GRM.*", ...)

// fromMPN() normalizes automatically:
String normalized = MPNUtils.normalize(mpn);  // Uppercases
```

### 4. Special Character Handling

```java
// User input: "LM358-N"
// Normalized: "LM358N" (hyphen removed)
// Pattern: "^LM[0-9]+.*" → matches ✅

// If pattern expected hyphen:
// Pattern: "^LM[0-9]+-[A-Z]$" → doesn't match ❌
```

---

## Manufacturer Count and Categories

**Total manufacturers:** 120+

**By category:**
- **Semiconductors:** 40+ (TI, ST, NXP, Infineon, ON Semi, Analog Devices, etc.)
- **Passives:** 25+ (Murata, Yageo, Vishay, Kemet, TDK, Panasonic, etc.)
- **Connectors:** 15+ (TE Connectivity, Molex, Amphenol, JAE, Hirose, etc.)
- **Memory:** 10+ (Micron, Winbond, ISSI, Macronix, etc.)
- **Sensors:** 10+ (Bosch, InvenSense, Honeywell, Sensirion, etc.)
- **Other:** 20+ (Power supplies, LEDs, crystals, etc.)

**Top 10 manufacturers by handler complexity:**
1. Texas Instruments (TIHandler - 628 lines)
2. Infineon (InfineonHandler - complex MOSFET families)
3. NXP (NXPHandler - 393 lines, diverse product lines)
4. STMicroelectronics (STHandler - STM32/STM8 MCUs)
5. Microchip (MicrochipHandler - PIC/AVR/SAM families)
6. Analog Devices (ADHandler - ADCs, DACs, op-amps)
7. ON Semiconductor (OnSemiHandler - MOSFETs, transistors)
8. Renesas (RenesasHandler - RL78, RX, RA MCUs)
9. Murata (MurataHandler - ceramic capacitors)
10. Vishay (VishayHandler - resistors, MOSFETs, diodes)

---

## Testing Patterns

### Positive Tests (Matches Expected Manufacturer)

```java
@ParameterizedTest
@CsvSource({
    "LM358, TEXAS_INSTRUMENTS",
    "TL072, TEXAS_INSTRUMENTS",
    "STM32F103, STMICROELECTRONICS",
    "ATMEGA328P, MICROCHIP",
    "PIC16F877, MICROCHIP",
    "GRM188, MURATA"
})
void shouldDetectManufacturer(String mpn, String expectedManufacturer) {
    ComponentManufacturer detected = ComponentManufacturer.fromMPN(mpn);
    assertEquals(ComponentManufacturer.valueOf(expectedManufacturer), detected);
}
```

### Negative Tests (Doesn't Match Wrong Manufacturer)

```java
@Test
void atmega328pShouldNotBeMisidentifiedAsAtmel() {
    // ATMEGA328P is now Microchip (Atmel was acquired)
    ComponentManufacturer detected = ComponentManufacturer.fromMPN("ATMEGA328P");
    assertEquals(ComponentManufacturer.MICROCHIP, detected);
    assertNotEquals(ComponentManufacturer.ATMEL, detected);
}

@Test
void dspic33ShouldNotMatchGenericPICPattern() {
    // DSPIC33 should match DSPIC pattern first, not generic PIC
    ComponentManufacturer detected = ComponentManufacturer.fromMPN("DSPIC33FJ128");
    assertEquals(ComponentManufacturer.MICROCHIP, detected);
    // Verify it's detected as DSPIC-specific, not just PIC
}
```

### Edge Cases

```java
@Test
void shouldHandleNullAndEmptyMPNs() {
    assertEquals(ComponentManufacturer.UNKNOWN, ComponentManufacturer.fromMPN(null));
    assertEquals(ComponentManufacturer.UNKNOWN, ComponentManufacturer.fromMPN(""));
    assertEquals(ComponentManufacturer.UNKNOWN, ComponentManufacturer.fromMPN("   "));
}

@Test
void shouldHandleCaseInsensitivity() {
    assertEquals(ComponentManufacturer.TEXAS_INSTRUMENTS,
        ComponentManufacturer.fromMPN("lm358"));  // Lowercase
    assertEquals(ComponentManufacturer.TEXAS_INSTRUMENTS,
        ComponentManufacturer.fromMPN("LM358"));  // Uppercase
}

@Test
void shouldHandleSpecialCharacters() {
    assertEquals(ComponentManufacturer.TEXAS_INSTRUMENTS,
        ComponentManufacturer.fromMPN("LM358-N"));  // Hyphen
    assertEquals(ComponentManufacturer.MICROCHIP,
        ComponentManufacturer.fromMPN("ATMEGA328P-PU"));  // Hyphen + suffix
}
```

---

## Learnings & Quirks

### Enum Declaration Order is Immutable
Once defined, enum order cannot be changed without recompilation. This is why ordering is critical.

### UNKNOWN Always Matches
The UNKNOWN enum entry has pattern `.*` (matches anything), ensuring fromMPN() always returns a value.

### Normalization Removes Package Suffixes
`MPNUtils.normalize()` removes special characters, which strips package suffixes like "+", "#PBF", etc. This is intentional - manufacturer detection should work on base MPN.

### Shared Prefixes are Common
Many manufacturers share prefixes (LM, MC, BC). Ordering and specificity matter.

### Handler Supplier is Stateless
Each call to `getHandler()` creates a new handler instance. This prevents shared state issues but uses more memory.

---

## See Also

- `/handler-pattern-design` - How handlers are implemented
- `/component-type-detection-hierarchy` - How types are detected after manufacturer
- `ComponentManufacturer.java` (627 lines) - Full enum definition
- `MPNUtils.java` - normalize() method used in detection
- `ManufacturerHandler.java` - Interface for handler implementations
