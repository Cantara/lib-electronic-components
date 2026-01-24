# Historical Documentation

This directory contains detailed technical analyses of significant implementations, bug fixes, and architectural decisions.

## Navigation

### Bug Fix Analyses
- **[BUG_FIX_ANALYSIS.md](BUG_FIX_ANALYSIS.md)** - Comprehensive analysis of critical bugs
  - PR #89: Handler bugs (NPE, pattern ordering, cross-handler matching)
  - PR #114-115: Similarity calculation bugs (0.0 ignoring, regex failures, IC interception)
  - Before/after code examples, prevention strategies, testing approaches

### Implementation Patterns
- **[HANDLER_IMPLEMENTATION_PATTERNS.md](HANDLER_IMPLEMENTATION_PATTERNS.md)** - Handler development best practices
  - Jinling handler case study (position-based encoding, dual format support)
  - Handler cleanup checklist from PRs #73-88
  - Testing strategies, common pitfalls, debugging techniques

## When to Read These Documents

### For Bug Investigation
→ Read **BUG_FIX_ANALYSIS.md** when:
- Investigating similarity calculation issues
- Debugging handler pattern matching problems
- Understanding why tests pass locally but fail in CI
- Preventing similar bugs in new code

### For Handler Development
→ Read **HANDLER_IMPLEMENTATION_PATTERNS.md** when:
- Adding a new manufacturer handler
- Debugging MPN extraction issues
- Understanding position-based vs pattern-based encoding
- Writing comprehensive handler tests

### For Quick Reference
→ Check **[HISTORY.md](../../HISTORY.md)** for:
- Chronological timeline of all PRs and milestones
- Quick summaries of completed work
- Project statistics and metrics

### For Active Development
→ Check **[CLAUDE.md](../../CLAUDE.md)** for:
- Current build commands and workflows
- Active learnings and patterns
- Project architecture overview
- Component and similarity calculator skills index

## Document Organization Principles

**HISTORY.md** - Chronological timeline
- What was done and when
- Summary-level descriptions
- Links to deep dives

**.docs/history/** - Detailed analyses
- How and why decisions were made
- Comprehensive code examples
- Lessons learned and prevention strategies

**CLAUDE.md** - Active guidance
- Current patterns and best practices
- Evergreen knowledge for ongoing work
- References to historical documentation

**.claude/skills/** - Domain-specific expertise
- Component-type specific patterns
- Similarity calculator details
- Manufacturer MPN encoding rules

---

**Last Updated:** January 24, 2026
