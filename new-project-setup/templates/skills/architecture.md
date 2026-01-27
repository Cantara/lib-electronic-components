---
skill: architecture
version: 1.0.0
domain: project-structure
triggers:
  - "refactoring"
  - "architecture"
  - "code organization"
  - "design patterns"
---

# Architecture Skill

## When to Use This Skill

Use this skill when:
- Refactoring or reorganizing code
- Adding new modules or components
- Evaluating design patterns in the codebase
- Planning structural changes

## Project Architecture

### Module Structure

{{DESCRIBE_MODULE_STRUCTURE}}

### Key Design Patterns

{{PATTERN_1_NAME}}:
- **Where**: {{WHERE_USED}}
- **Why**: {{RATIONALE}}
- **Example**: {{FILE_REFERENCE}}

{{PATTERN_2_NAME}}:
- **Where**: {{WHERE_USED}}
- **Why**: {{RATIONALE}}
- **Example**: {{FILE_REFERENCE}}

### Dependency Flow

```
{{DEPENDENCY_DIAGRAM}}
```

## Known Duplication Hotspots

| Area | Files | Issue | Recommended Action |
|------|-------|-------|-------------------|
| {{AREA}} | {{FILES}} | {{ISSUE}} | {{ACTION}} |

## Refactoring Guidelines

### Safe Refactorings
- {{SAFE_REFACTORING_1}}
- {{SAFE_REFACTORING_2}}

### Risky Refactorings (Require Extra Testing)
- {{RISKY_REFACTORING_1}}
- {{RISKY_REFACTORING_2}}

## Architectural Decisions

| Decision | Chosen | Alternatives Considered | Rationale |
|----------|--------|------------------------|-----------|
| {{DECISION}} | {{CHOSEN}} | {{ALTERNATIVES}} | {{WHY}} |

## Checklist

Before completing architectural changes:
- [ ] No circular dependencies introduced
- [ ] Public API surface area hasn't grown unnecessarily
- [ ] Existing tests still pass
- [ ] New code follows existing patterns (or documents why it diverges)
- [ ] CLAUDE.md architecture section updated if structure changed

## Related Skills

- `/documentation-maintenance` - Update docs after structural changes
- `/task-delegation` - Delegate simple refactoring to Haiku
