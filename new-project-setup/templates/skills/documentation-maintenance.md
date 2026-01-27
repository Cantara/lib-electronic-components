---
skill: documentation-maintenance
version: 1.0.0
domain: workflow
triggers:
  - "before committing"
  - "before PR"
  - "documentation update"
  - "pre-commit checklist"
---

# Documentation Maintenance Skill

## When to Use This Skill

Use this skill:
- **Before every PR** (mandatory gate)
- When significant patterns or gotchas are discovered
- When architecture changes affect the meta-layer
- When skills need updating based on new learnings

## Pre-PR Checklist

Run through this checklist before creating any PR:

### CLAUDE.md Updates
- [ ] Build commands still accurate?
- [ ] Architecture section reflects current structure?
- [ ] Gotchas section includes any new discoveries?
- [ ] Skills index table lists all current skills?
- [ ] Technical debt section updated (added or resolved items)?
- [ ] Line count still under 300?

### Skills Updates
- [ ] Any new patterns discovered that belong in a skill?
- [ ] Any existing skill content that's now outdated?
- [ ] Any new skill needed for a domain touched by this PR?
- [ ] Skills referenced in CLAUDE.md match actual skill files?

### HISTORY.md Updates (Standard+ Tier)
- [ ] Significant changes recorded with date?
- [ ] PR reference included in entry?
- [ ] Entry categorized correctly (Feature/Fix/Refactor/Docs/Milestone)?

### .docs/ Updates (Standard+ Tier)
- [ ] Fixed bugs documented in `.docs/history/FIXED_BUGS.md`?
- [ ] Architectural decisions documented if applicable?

## What to Document Where

| Discovery Type | Where to Record |
|---------------|----------------|
| Build quirk | CLAUDE.md > Gotchas |
| Architecture pattern | Relevant domain skill |
| Bug fix | `.docs/history/FIXED_BUGS.md` |
| New tool/dependency | CLAUDE.md > Build Commands or Overview |
| Performance insight | Relevant domain skill |
| Process improvement | This skill or CLAUDE.md > Git Workflow |
| Common mistake | Relevant domain skill > Common Mistakes table |

## Documentation Quality Standards

Good documentation entries are:
- **Specific**: Include file paths, line numbers, exact commands
- **Actionable**: Reader knows what to do after reading
- **Contextual**: Explains why, not just what
- **Current**: Reflects the code as it is now, not as it was

Bad documentation:
- "Be careful with X" (no specifics)
- Outdated references to removed code
- Duplicate of information already in another skill
- Generic advice not specific to this project

## When to Create a New Skill

Create a new skill when:
- A domain has 3+ patterns worth documenting
- You find yourself explaining the same thing in multiple PRs
- A new module or subsystem is added to the project
- A common mistake keeps recurring

Use `new-project-setup/templates/skills/_skill-skeleton.md` as the template.

## Checklist

Before completing documentation maintenance:
- [ ] All checklist items above addressed
- [ ] No stale references in CLAUDE.md
- [ ] Skills index matches actual skills on disk
- [ ] HISTORY.md has entry for this session's work (if significant)

## Related Skills

- `/architecture` - Architecture-specific documentation
- `/task-delegation` - Delegate doc updates to Haiku when straightforward
