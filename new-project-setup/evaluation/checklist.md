# Quick Evaluation Checklist

Pass/fail prerequisites and quick quality check. Use this for a fast assessment before running the full rubric audit.

## Prerequisites (Pass/Fail)

These must all pass for the project to be considered "set up" for LLM-assisted development:

- [ ] **CLAUDE.md exists** at project root
- [ ] **CLAUDE.md has Build Commands section** with at least one working command
- [ ] **CLAUDE.md is under 300 lines**
- [ ] **.claude/skills/ directory exists** with at least 2 skills
- [ ] **Skills have YAML frontmatter** (check first skill file)
- [ ] **No unfilled `{{PLACEHOLDER}}` strings** in CLAUDE.md or skills

## Quick Quality Checks

Rate each item: Pass / Partial / Fail

### Content
- [ ] Build commands are accurate (try running one)
- [ ] Architecture section describes THIS project (not generic)
- [ ] Gotchas section has at least one project-specific entry
- [ ] Skills index in CLAUDE.md matches actual skill files

### Structure
- [ ] CLAUDE.md acts as index (links to skills, not encyclopedic)
- [ ] Skills follow consistent structure (check 2 random skills)
- [ ] Tier-appropriate structure (minimal projects aren't over-structured)

### Workflow
- [ ] Git workflow section matches actual project practice
- [ ] documentation-maintenance skill exists
- [ ] HISTORY.md exists with at least initialization entry (standard+ tier)

## Result

| Outcome | Criteria |
|---------|----------|
| **Ready** | All prerequisites pass + 8+ quality checks pass |
| **Almost** | All prerequisites pass + 5-7 quality checks pass |
| **Needs Work** | 1-2 prerequisites fail OR fewer than 5 quality checks pass |
| **Not Set Up** | 3+ prerequisites fail |

## Next Steps by Result

- **Ready**: Run full audit with `/audit-project` for scored assessment
- **Almost**: Address failing quality checks, then re-evaluate
- **Needs Work**: Run `/retrofit-project` to fill gaps
- **Not Set Up**: Run `/init-project` or `/retrofit-project` to establish structure
