---
name: documentation-maintenance
description: Use this skill BEFORE creating a PR to ensure all documentation, skills, and learnings are updated. Critical for preserving institutional knowledge and preventing documentation drift.
---

# Documentation Maintenance

**🚨 CRITICAL: Use this skill BEFORE every PR to ensure documentation is up-to-date!**

## When to Use This Skill

**MANDATORY before:**
- Creating a pull request
- Committing significant changes
- After discovering a bug or gotcha
- After implementing a new feature
- After refactoring complex code

**Proactive use:**
- After fixing a non-obvious bug
- After discovering edge cases
- After learning manufacturer-specific quirks
- After debugging a test failure

---

## Pre-PR Documentation Checklist

Use this checklist BEFORE running `/commit` or creating a PR:

### ✅ Step 1: Identify What Changed

**Questions to ask:**
- [ ] Did I add/modify a manufacturer handler? → Update `/handler-pattern-design`
- [ ] Did I work with MPN normalization? → Update `/mpn-normalization`
- [ ] Did I modify similarity calculators? → Update `/similarity-calculator-architecture`
- [ ] Did I add new component types? → Update `/component-type-detection-hierarchy`
- [ ] Did I add spec extraction logic? → Update `/component-spec-extraction`
- [ ] Did I convert a calculator to metadata? → Update `/metadata-driven-similarity-conversion`
- [ ] Did I modify manufacturer detection? → Update `/manufacturer-detection-from-mpn`
- [ ] Did I add equivalent groups? → Update `/equivalent-group-identification`
- [ ] Is this a cross-cutting change? → Update `CLAUDE.md`

---

### ✅ Step 2: Document Learnings & Quirks

**Where to add learnings:**

#### General/Cross-Cutting → `CLAUDE.md`

Add to `## Learnings & Quirks` section:

```markdown
### Pattern Matching
- Handler detection order matters in `ComponentManufacturer.fromMPN()` - more specific patterns checked before generic

### Handler Implementation
- Always register patterns for both base type AND manufacturer-specific type
```

**When to use CLAUDE.md:**
- Cross-cutting patterns affecting multiple areas
- Build system quirks (Maven, dependencies)
- Testing patterns (JUnit, parameterized tests)
- Git workflow discoveries
- CI/CD issues

---

#### Component-Specific → Relevant Skill File

**Example: Handler bug discovered**

Update `.claude/skills/handler-pattern-design/SKILL.md` under `## Learnings & Quirks`:

```markdown
### Package Extraction Edge Cases
- STM32F103C8**T6**: Package code is second-to-last char ('T'), not last ('6' is temp range)
- Position-based extraction requires normalization FIRST (hyphens break charAt())
```

**Example: MPN normalization quirk**

Update `.claude/skills/mpn-normalization/SKILL.md`:

```markdown
### Unicode Handling
- Micro sign µ (U+00B5) becomes Greek MU Μ (U+039C) when uppercased
- Impact: CapacitorSimilarityCalculator.parseCapacitanceValue() must replace BEFORE uppercase
```

**Which skill file to update:**

| Change Type | Skill File |
|-------------|------------|
| Handler patterns, anti-patterns | `/handler-pattern-design` |
| MPN suffix handling, normalize() | `/mpn-normalization` |
| Calculator ordering, isApplicable() | `/similarity-calculator-architecture` |
| ComponentType, getBaseType() | `/component-type-detection-hierarchy` |
| Spec extraction, SpecValue usage | `/component-spec-extraction` |
| Metadata conversion steps | `/metadata-driven-similarity-conversion` |
| Manufacturer regex, fromMPN() | `/manufacturer-detection-from-mpn` |
| Equivalent groups, cross-refs | `/equivalent-group-identification` |

---

### ✅ Step 3: Update Examples & Code Snippets

**If you fixed a bug, add the fix as an example:**

```markdown
### Common Anti-Patterns

| Anti-Pattern | Problem | Solution |
|-------------|---------|----------|
| **Using matches() instead of matchesForCurrentHandler()** | Cross-handler false matches | Use `patterns.matchesForCurrentHandler()` |
```

**If you discovered a new pattern, document it:**

```markdown
### Suffix Ordering (New Pattern)

**Pattern: Check longer suffixes BEFORE shorter suffixes.**

```java
// ✅ CORRECT: Check "DT" before "T"
if (upperMpn.endsWith("DT")) return "SOT-223";
if (upperMpn.endsWith("T")) return "TO-220";
```
```

---

### ✅ Step 4: Update Tables & Lists

**If you converted a calculator to metadata:**

Update `/metadata-driven-similarity-conversion/SKILL.md`:

```markdown
| PassiveComponentCalculator | ✅ | #XXX | 2026-01-24 | value, tolerance, package | value |
```

**If you added a manufacturer:**

Update `/manufacturer-detection-from-mpn/SKILL.md`:

```markdown
**Total manufacturers:** 121 (was 120+)
```

**If you fixed a handler bug:**

Update `/handler-pattern-design/SKILL.md`:

```markdown
**Fixed (PR #XX):**
- ~~PassiveComponentHandler duplicate patterns~~ - Removed duplicates
```

---

### ✅ Step 5: Add Cross-References

**If you created a NEW significant feature:**

1. Add to `CLAUDE.md` under appropriate section
2. Add cross-references in related skills
3. Update skill discovery (add to skill list in CLAUDE.md)

**Example: New PrefixRegistry feature**

Add to `CLAUDE.md`:
```markdown
### Component Classification
- `PrefixRegistry` - Cross-manufacturer prefix equivalence mapping
```

Add to `/equivalent-group-identification/SKILL.md`:
```markdown
## See Also
- `PrefixRegistry.java` - Prefix equivalence infrastructure
```

---

### ✅ Step 6: Update HISTORY.md (For Significant Changes)

**When to update HISTORY.md:**
- Major bug fixes (PR #114-115 OpAmp bug level)
- New features (MPN package suffix support)
- Architecture changes (metadata-driven similarity)
- Handler cleanup batches (PR #73-88)

**Format:**
```markdown
### PR #XXX: Brief Title (Date)
**What:** Brief description
**Why:** Problem solved
**Impact:** Who/what is affected
**Files:** Key files changed
```

---

## Quick Decision Tree

```
┌─ Did I change code? ───────────────────────────────────┐
│                                                         │
│  ┌─ Is it a bug fix?                                   │
│  │  └─ YES → Add to relevant skill "Learnings & Quirks"│
│  │                                                      │
│  ┌─ Is it a new feature?                               │
│  │  └─ YES → Update skill with examples & patterns     │
│  │                                                      │
│  ┌─ Is it a refactoring?                               │
│  │  └─ YES → Update anti-patterns or cleanup checklists│
│  │                                                      │
│  └─ Is it significant?                                 │
│     └─ YES → Update HISTORY.md                         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## Documentation Update Examples

### Example 1: Bug Fix (Handler Pattern)

**What:** Fixed NXPHandler to use matchesForCurrentHandler() instead of matches()

**Documentation updates:**

1. **`/handler-pattern-design/SKILL.md`** - Add to Learnings:
```markdown
### Cross-Handler Pattern Matching Bug (PR #89)
- Using `patterns.matches()` caused NXPHandler to match STM32 parts (ST manufacturer)
- Root cause: matches() searches ALL handlers, not just current
- Fix: Always use `patterns.matchesForCurrentHandler()` in handler matches() method
```

2. **`CLAUDE.md`** - Update Known Technical Debt:
```markdown
**Fixed (PR #89):**
- ~~NXPHandler cross-handler matching bug~~ - Uses matchesForCurrentHandler() now
```

---

### Example 2: New Feature (Equivalent Groups)

**What:** Added EquivalentPartRegistry centralization

**Documentation updates:**

1. **`/equivalent-group-identification/SKILL.md`** - Add section:
```markdown
## EquivalentPartRegistry Implementation

**Status:** ✅ Implemented in PR #XXX

**API:**
```java
EquivalentPartRegistry.getInstance().registerGroup(ComponentType.TRANSISTOR, Set.of("2N2222", "PN2222"));
```
```

2. **`CLAUDE.md`** - Add to Architecture section:
```markdown
### Equivalent Part Matching
- `EquivalentPartRegistry` - Centralized registry for cross-manufacturer equivalent groups
```

3. **`HISTORY.md`** - Add entry:
```markdown
### PR #XXX: Centralized Equivalent Part Registry (2026-01-24)
**What:** Created EquivalentPartRegistry to centralize hardcoded equivalent groups
**Why:** Eliminate duplication across 4 similarity calculators
**Impact:** Easier to add new equivalent groups, single source of truth
**Files:** EquivalentPartRegistry.java, TransistorSimilarityCalculator.java, etc.
```

---

### Example 3: Gotcha Discovery (Unicode Issue)

**What:** Discovered µ→Μ uppercasing breaks capacitor parsing

**Documentation updates:**

1. **`/mpn-normalization/SKILL.md`** - Add to Unicode Gotcha section:
```markdown
### Real-World Bug Example (PR #XXX)

**Bug:** CapacitorSimilarityCalculator failed to parse "10µF"
**Root Cause:** toUpperCase() converts µ (U+00B5) → Μ (U+039C) Greek MU
**Fix:** Replace µ with 'u' BEFORE calling toUpperCase()

```java
// ✅ CORRECT
String normalized = value.replace("µ", "u").replace("Μ", "u");
normalized = normalizeValue(normalized);
```
```

---

## Wiring This Into Workflow

### Option 1: Add to /commit Skill (RECOMMENDED)

Update `.claude/skills/commit/SKILL.md` to include:

```markdown
## BEFORE Creating Commit

**🚨 MANDATORY: Run documentation maintenance checklist first!**

```bash
/documentation-maintenance
```

Ask yourself:
1. Did I update relevant skill files with learnings?
2. Did I add examples for new patterns?
3. Did I update CLAUDE.md for cross-cutting changes?
4. Did I update HISTORY.md for significant features?
```

---

### Option 2: User Prompt Submit Hook

Add to `.claude/settings.json`:

```json
{
  "hooks": {
    "user-prompt-submit": {
      "command": "echo '⚠️ REMINDER: Update documentation before creating PR! Run: /documentation-maintenance'",
      "when": "commit|PR|pull request"
    }
  }
}
```

---

### Option 3: Git Pre-Commit Hook

Create `.git/hooks/pre-commit`:

```bash
#!/bin/bash

# Check if documentation was updated in this commit
if git diff --cached --name-only | grep -q "CLAUDE.md\|\.claude/skills"; then
  echo "✅ Documentation updated"
else
  echo "⚠️  WARNING: No documentation updates found!"
  echo "    Consider updating:"
  echo "    - CLAUDE.md (cross-cutting learnings)"
  echo "    - Relevant skill files (.claude/skills/)"
  echo ""
  read -p "Continue anyway? (y/n) " -n 1 -r
  echo
  if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    exit 1
  fi
fi
```

---

## Common Documentation Anti-Patterns

### ❌ Don't Do This

**1. Skipping documentation "because it's obvious"**
```
// Added fix for handler bug
// (No documentation update)
```
→ Future developers won't know the bug existed or why the fix is needed

**2. Adding TODO comments instead of documenting**
```java
// TODO: Document this pattern
```
→ TODOs rarely get addressed. Document NOW while knowledge is fresh.

**3. Documenting in code comments only**
```java
// IMPORTANT: Must use matchesForCurrentHandler() not matches()
```
→ Code comments aren't searchable in skills. Add to skill file too.

**4. Updating only CLAUDE.md for specific changes**
```markdown
# CLAUDE.md
- Fixed handler bug (specific to NXPHandler)
```
→ Should also update `/handler-pattern-design` skill for reusable pattern

**5. Not updating cross-references**
```
Added new skill but didn't update CLAUDE.md skill list
```
→ New skill is invisible to users

---

### ✅ Do This Instead

**1. Document immediately after fixing**
- Fix bug → Immediately update skill file
- Add pattern → Add example to skill
- Discover quirk → Add to Learnings section

**2. Document in multiple places**
- Specific pattern → Skill file
- Cross-cutting → CLAUDE.md
- Significant change → HISTORY.md
- Code comment → For implementation details only

**3. Make documentation searchable**
- Use keywords (bug, pattern, anti-pattern, gotcha)
- Add to tables for easy reference
- Include code examples

---

## Automation Opportunities

**Future improvements:**

1. **PR template with documentation checklist**
```markdown
## Documentation Updates
- [ ] Updated relevant skill files
- [ ] Added learnings to CLAUDE.md (if cross-cutting)
- [ ] Updated HISTORY.md (if significant)
- [ ] Verified cross-references
```

2. **CI check for documentation updates**
```bash
# Fail if code changed but no .md files updated
if [[ $CODE_CHANGES -eq 1 && $DOC_CHANGES -eq 0 ]]; then
  echo "ERROR: Code changed but no documentation updated"
  exit 1
fi
```

3. **Automated skill suggestion**
```bash
# Changed HandlerTest.java → Suggest updating /handler-pattern-design
git diff --name-only | grep Handler → echo "Consider updating /handler-pattern-design"
```

---

## Checklist Template (Copy-Paste for Each PR)

```markdown
## Documentation Maintenance Checklist

### Files Changed
- [ ] Handler code → Check `/handler-pattern-design`
- [ ] MPN normalization → Check `/mpn-normalization`
- [ ] Similarity calculator → Check `/similarity-calculator-architecture`
- [ ] Component types → Check `/component-type-detection-hierarchy`
- [ ] Spec extraction → Check `/component-spec-extraction`
- [ ] Metadata conversion → Check `/metadata-driven-similarity-conversion`
- [ ] Manufacturer detection → Check `/manufacturer-detection-from-mpn`
- [ ] Equivalent groups → Check `/equivalent-group-identification`

### Learnings Added
- [ ] Added quirks/gotchas to relevant skill file
- [ ] Added examples of new patterns
- [ ] Updated anti-pattern tables if applicable
- [ ] Added cross-references between related skills

### Cross-Cutting Updates
- [ ] Updated CLAUDE.md if cross-cutting change
- [ ] Updated HISTORY.md if significant feature
- [ ] Updated skill lists in CLAUDE.md if new skill
- [ ] Verified all cross-references resolve

### Verification
- [ ] Read through updated documentation
- [ ] Confirmed examples are accurate
- [ ] Checked markdown formatting
- [ ] Tested cross-reference links
```

---

## Quick Reference: File → Skill Mapping

| File Pattern | Skill to Update |
|-------------|-----------------|
| `*Handler.java` | `/handler-pattern-design` |
| `*HandlerTest.java` | `/handler-pattern-design` |
| `MPNUtils.java` (normalize, strip) | `/mpn-normalization` |
| `*SimilarityCalculator.java` | `/similarity-calculator-architecture` |
| `ComponentType.java` | `/component-type-detection-hierarchy` |
| `*Spec.java`, `SpecValue.java` | `/component-spec-extraction` |
| Calculator metadata conversion | `/metadata-driven-similarity-conversion` |
| `ComponentManufacturer.java` | `/manufacturer-detection-from-mpn` |
| Equivalent groups in calculators | `/equivalent-group-identification` |
| Cross-cutting patterns | `CLAUDE.md` |
| Significant features | `HISTORY.md` |

---

## Learnings & Quirks

### Documentation Maintenance Patterns
- **Update documentation BEFORE commit:** Knowledge is freshest immediately after implementation
- **Use copy-paste checklist:** Reduces mental load, ensures consistency
- **Search before adding:** Avoid duplicating existing documentation
- **Link between related concepts:** Cross-references improve discoverability

### Common Mistakes
- Assuming "obvious" changes don't need documentation
- Documenting in code comments instead of skill files
- Forgetting to update cross-references
- Skipping HISTORY.md for significant changes

---

## See Also

- **CLAUDE.md** - Main project documentation, skill listing
- **HISTORY.md** - Chronological project history
- `/architecture` - Refactoring and cleanup guidance
- All 8 advanced component skills - Specific domain documentation
