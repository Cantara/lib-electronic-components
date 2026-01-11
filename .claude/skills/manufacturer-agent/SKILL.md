---
name: manufacturer-agent
description: Web-augmented agent for creating manufacturer skills and comprehensive tests. Use when adding or improving support for a manufacturer handler.
---

# Manufacturer Agent

Automated agent for creating detailed manufacturer skills and comprehensive test coverage.

## Usage

```
/manufacturer-agent <manufacturer-name>
```

Examples:
- `/manufacturer-agent ST` - STMicroelectronics
- `/manufacturer-agent NXP` - NXP Semiconductors
- `/manufacturer-agent Infineon` - Infineon Technologies
- `/manufacturer-agent Microchip` - Microchip Technology

## Agent Workflow

Execute these phases in order:

### Phase 1: Analyze Existing Handler

1. Find the handler file:
   ```
   Glob: src/main/java/**/manufacturers/*Handler.java
   ```

2. Read the handler and extract:
   - All patterns registered in `initializePatterns()`
   - Supported ComponentTypes from `getSupportedTypes()`
   - Package code mappings in `extractPackageCode()`
   - Series extraction logic in `extractSeries()`
   - Any special matching logic in `matches()`

3. Identify issues:
   - Uses `HashSet` instead of `Set.of()`?
   - Uses default `matches()` with multi-pattern types?
   - Missing case-insensitive handling?

### Phase 2: Web Research

Search for manufacturer documentation using these queries:

```
WebSearch: "{manufacturer} part number naming convention"
WebSearch: "{manufacturer} ordering information guide"
WebSearch: "{manufacturer} package marking code"
WebSearch: "{manufacturer} product selector guide filetype:pdf"
```

For each relevant result, fetch and analyze:
```
WebFetch: <url>
Prompt: "Extract the MPN structure, package codes, temperature grades, and family prefixes from this page"
```

**Key information to gather:**
- MPN structure breakdown (prefix, series, variant, package, temp, qualifier)
- Package code table (e.g., N=DIP, D=SOIC, PW=TSSOP)
- Temperature grade suffixes (I=Industrial, Q=Automotive, M=Military)
- Family prefixes and what they indicate
- Common product series with examples
- Any naming quirks or conflicts (like TI's LM35 vs LM358)

### Phase 3: Gather Real MPN Examples

Search for real part numbers to use in tests:

```
WebSearch: "{manufacturer} {product-type} datasheet"
```

Product types to search based on handler's supported types:
- Microcontroller → "STM32 datasheet", "PIC microcontroller"
- MOSFET → "IRFZ44N datasheet", "STF5N52U"
- Op-amp → "LM358 datasheet", "TL072"
- Voltage regulator → "LM7805 datasheet"

Collect 5-10 real MPNs for each supported ComponentType.

### Phase 4: Generate Skill File

Create `.claude/skills/manufacturers/{manufacturer}/SKILL.md` following this template:

```markdown
---
name: {manufacturer-lowercase}
description: {Manufacturer} MPN encoding patterns, suffix decoding, and handler guidance.
---

# {Manufacturer} Manufacturer Skill

## MPN Structure

{Diagram showing MPN breakdown}

### Example Decoding

{2-3 annotated examples}

---

## Package Codes

### Through-Hole Packages
| Code | Package | Notes |
|------|---------|-------|

### Surface Mount Packages
| Code | Package | Notes |
|------|---------|-------|

---

## Family Prefixes

| Prefix | Category | Examples |
|--------|----------|----------|

---

## Temperature Grades

| Suffix | Range | Application |
|--------|-------|-------------|

---

## Common Series Reference

{Tables of common product series}

---

## Handler Implementation Notes

### Package Code Extraction
{Code examples and gotchas}

### Series Extraction
{Code examples}

### Known Issues
{Any bugs or limitations found}

---

## Related Files

- Handler: `manufacturers/{Handler}.java`
- Component types: {list types}
- Test: `handlers/{Handler}Test.java`

---

## Learnings & Edge Cases

{Bullet points of discoveries}

<!-- Add new learnings above this line -->
```

### Phase 5: Generate Test File

Create `src/test/java/no/cantara/electronic/component/lib/handlers/{Handler}Test.java`:

**Structure (use TIHandlerTest/AtmelHandlerTest as template):**

```java
package no.cantara.electronic.component.lib.handlers;

import ...;

class {Handler}Test {

    private static {Handler} handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        // MUST use MPNUtils to avoid circular initialization
        ManufacturerHandler h = MPNUtils.getManufacturerHandler("{known-mpn}");
        assertNotNull(h, "Should find handler");
        assertTrue(h instanceof {Handler}, "Handler should be {Handler}");
        handler = ({Handler}) h;

        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("{ProductType} Detection")
    class {ProductType}Tests {
        @ParameterizedTest
        @ValueSource(strings = {"{real-mpn-1}", "{real-mpn-2}", ...})
        void shouldDetect{ProductType}(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.{TYPE}, registry));
        }
    }

    // Nested classes for each product type...

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "{mpn1}, {expected-package1}",
            "{mpn2}, {expected-package2}"
        })
        void shouldExtractPackageCodes(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests { ... }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() { ... }

        @Test
        void shouldBeCaseInsensitive() { ... }
    }
}
```

### Phase 6: Run Tests and Report

1. Run the new tests:
   ```bash
   mvn test -Dtest={Handler}Test
   ```

2. If single-test fails with initialization error, run full suite:
   ```bash
   mvn test
   ```

3. Analyze failures:
   - Pattern not matching? → Fix pattern in handler
   - Package extraction wrong? → Check suffix ordering, speed grades
   - Multi-pattern type issue? → Use `registry.matches()` instead of default

4. Create summary report:
   - Tests created: X
   - Tests passing: Y
   - Handler bugs found: Z
   - Recommended fixes: [list]

### Phase 7: Update Documentation

1. Update CLAUDE.md with any new learnings
2. Add handler to "Known Technical Debt" if issues found
3. Note the new test as a template for similar manufacturers

---

## Common Handler Issues to Check

From previous work on TI and Atmel handlers:

1. **HashSet in getSupportedTypes()** → Change to `Set.of()`
2. **Multi-pattern types** → Override `matches()` to use `registry.matches()`
3. **Suffix ordering** → Check longer suffixes before shorter (DT before T)
4. **Speed grades** → Strip digits before package lookup
5. **Case sensitivity** → Ensure `toUpperCase()` is called
6. **Local package maps** → Migrate to `PackageCodeRegistry`

---

## Output Checklist

After running this agent, you should have:

- [ ] `.claude/skills/manufacturers/{manufacturer}/SKILL.md`
- [ ] `src/test/java/.../handlers/{Handler}Test.java`
- [ ] Handler fixes (if needed)
- [ ] Updated CLAUDE.md with learnings
- [ ] PR ready for review
