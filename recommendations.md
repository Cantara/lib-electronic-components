# CLAUDE.md Organization Recommendations

Lessons learned from refactoring the `lib-electronic-components` project's CLAUDE.md from 1010 lines to 166 lines (84% reduction).

---

## Core Principle

**CLAUDE.md should be an index, not an encyclopedia.**

The file is loaded into every conversation context. Keep it lean so Claude has room for the actual work. Detailed knowledge belongs in skills that are loaded on-demand.

---

## Recommended CLAUDE.md Structure (~150-300 lines)

```markdown
# CLAUDE.md

## Build Commands          # ~10 lines - Essential commands only
## Git Workflow            # ~15 lines - Branch strategy, PR process
## Cost Optimization       # ~10 lines - Summary table, link to skill
## Project Overview        # ~10 lines - What the project does
## Architecture            # ~30 lines - Key classes and patterns
## Skills Index            # ~40 lines - Table of available skills
## Critical Gotchas        # ~30 lines - Top 10-15 must-know issues
## Active Technical Debt   # ~10 lines - Only UNFIXED items
## Recording Learnings     # ~10 lines - Where to document discoveries
## Historical Docs         # ~5 lines  - Links to archives
```

---

## What to Extract to Skills

### Move to Skills (load on-demand)
| Content Type | Skill Location | Rationale |
|--------------|----------------|-----------|
| Feature documentation | `/feature-name/SKILL.md` | Only needed when working on that feature |
| Migration guides | `/migration-name/SKILL.md` | One-time or rare usage |
| Detailed API examples | `/api-name/SKILL.md` | Reference when needed |
| Component-specific patterns | `/component/SKILL.md` | Domain expertise |
| Manufacturer guides | `/manufacturers/name/SKILL.md` | Very specific knowledge |

### Keep in CLAUDE.md (always needed)
| Content Type | Why |
|--------------|-----|
| Build commands | Used every session |
| Git workflow | Used every session |
| Architecture overview | Context for any work |
| Skills index | Navigation aid |
| Top gotchas | Prevents common mistakes |
| Active debt | Current awareness |

---

## What to Archive (move to `.docs/history/`)

| Content Type | Archive Location |
|--------------|------------------|
| Fixed bugs | `.docs/history/FIXED_BUGS.md` |
| Completed migrations | `.docs/history/MIGRATIONS.md` |
| Old implementation decisions | `.docs/history/DECISIONS.md` |
| PR summaries | `HISTORY.md` or `.docs/history/` |

**Rule**: If it starts with ~~strikethrough~~, archive it.

---

## Strengths to Replicate

### 1. Rich Skill Ecosystem
Create skills for every significant domain. This project has 175 skills covering:
- Component types (resistor, capacitor, etc.)
- Architecture patterns (handler design, similarity calculators)
- Features (lifecycle tracking, parametric search)
- Manufacturers (TI, ST, Microchip, etc.)
- Similarity calculators (14 type-specific skills)

### 2. Explicit Cost Optimization
Document when to use cheaper models (Haiku) vs. expensive ones (Sonnet/Opus):
```markdown
| Delegate to Haiku | Use Sonnet Directly |
|-------------------|---------------------|
| Test fixes | Architectural decisions |
| Documentation | Complex debugging |
| Simple refactoring | Ambiguous requirements |
```

### 3. Learnings & Gotchas Sections
Capture institutional knowledge explicitly:
- **Gotchas**: Things that will bite you if you don't know them
- **Learnings**: Patterns discovered through experience
- **Active debt**: Known issues to be aware of

### 4. Status Tables for Ongoing Work
Track progress clearly:
```markdown
| Feature | Status | Notes |
|---------|--------|-------|
| ResistorCalculator | Done | PR #115 |
| MCUCalculator | Pending | Needs metadata |
```

### 5. Skill Invocation Pattern
Use `/skill-name` convention for easy discovery and invocation.

### 6. Documentation Maintenance Reminder
Include a pre-commit checklist skill that reminds to update docs:
```markdown
**Before committing**: Run `/documentation-maintenance`
```

---

## Anti-Patterns to Avoid

### 1. Duplication Between CLAUDE.md and Skills
**Problem**: Same content in both places gets out of sync.
**Solution**: CLAUDE.md has summary + link, skill has details.

### 2. Historical Clutter
**Problem**: Fixed bugs taking up space with ~~strikethrough~~.
**Solution**: Archive to `.docs/history/FIXED_BUGS.md`.

### 3. Mixing Index with Content
**Problem**: CLAUDE.md tries to be both navigation and documentation.
**Solution**: Be a clear index with minimal inline content.

### 4. Undifferentiated Skill Lists
**Problem**: Long lists of skills with no categorization.
**Solution**: Group skills by purpose (component, architecture, feature).

### 5. No Skill at All
**Problem**: Everything in CLAUDE.md, nothing loadable on-demand.
**Solution**: Extract detailed knowledge to `.claude/skills/`.

---

## Migration Checklist

When refactoring an oversized CLAUDE.md:

1. **Measure**: Count lines, identify sections
2. **Categorize**: Mark each section as KEEP, EXTRACT, or ARCHIVE
3. **Create archives**: `.docs/history/FIXED_BUGS.md`, etc.
4. **Create skills**: One per major feature/domain
5. **Rewrite CLAUDE.md**: Index format with tables
6. **Verify skills exist**: Check links point to real files
7. **Test**: Ensure Claude can find information via skills

---

## Directory Structure Example

```
project/
├── CLAUDE.md                 # 150-300 lines, index only
├── HISTORY.md                # Chronological PR timeline
├── .claude/
│   └── skills/
│       ├── feature-a/
│       │   └── SKILL.md      # Detailed feature docs
│       ├── feature-b/
│       │   └── SKILL.md
│       ├── architecture/
│       │   └── SKILL.md      # Patterns and decisions
│       └── manufacturers/
│           ├── ti/SKILL.md
│           └── st/SKILL.md
└── .docs/
    └── history/
        ├── FIXED_BUGS.md     # Archived fixed issues
        ├── MIGRATIONS.md     # Completed migrations
        └── DECISIONS.md      # Historical decisions
```

---

## Metrics

| Metric | Before | After | Change |
|--------|--------|-------|--------|
| CLAUDE.md lines | 1010 | 166 | -84% |
| Skills files | 175 | 175 | (already extracted) |
| Fixed bugs in CLAUDE.md | ~100 lines | 0 | Archived |
| Duplicate content | Significant | Minimal | Eliminated |

---

## Summary

1. **CLAUDE.md = Index** - Quick reference, links to details
2. **Skills = Deep knowledge** - Loaded when needed
3. **Archives = Historical** - Out of active context
4. **Gotchas = Critical** - Always visible
5. **Cost guidance = Explicit** - When to use which model
