# Anti-Patterns: What NOT to Do

Patterns that seem reasonable but lead to poor outcomes in LLM-assisted development. Each anti-pattern includes why it fails and what to do instead.

## Anti-Pattern 1: The Encyclopedic CLAUDE.md

**What it looks like**: A 500-1000+ line CLAUDE.md that tries to document everything -- every pattern, every gotcha, every architectural decision, every build variant.

**Why it fails**:
- Wastes context window on irrelevant information
- Claude can't distinguish what's important from what's noise
- Maintenance becomes impossible (nobody updates a 1000-line file)
- Signal-to-noise ratio degrades as the file grows

**Evidence**: lib-electronic-components had a 1010-line CLAUDE.md. After restructuring to 166 lines + 39 skills, Claude's context utilization improved and maintenance became tractable.

**Do this instead**: CLAUDE.md under 300 lines acting as an index. Domain knowledge in on-demand skills. If a CLAUDE.md section exceeds 20 lines of domain detail, extract it to a skill.

## Anti-Pattern 2: Template-Only Skills

**What it looks like**: Skills created during initialization that are never updated. They still contain placeholder text or generic content that could apply to any project.

**Why it fails**:
- Skills without project-specific content provide no value over Claude's general knowledge
- Claude learns to ignore skills that don't contain useful information
- Creates a false sense of documentation ("we have 10 skills!")
- Maintenance overhead for files that add nothing

**Evidence**: Projects that initialize with comprehensive tier but never populate skills beyond templates score well on Structure (dimension 1) but poorly on Content Quality (dimension 2) in audits.

**Do this instead**: Start with fewer skills and populate them with real content. A minimal tier with 2 well-written skills beats a comprehensive tier with 10 template-only skills. Add skills when you have real patterns to document.

## Anti-Pattern 3: Documentation as Separate Task

**What it looks like**: "We'll update the docs later" or "documentation sprint next month." Documentation is treated as a separate activity from development.

**Why it fails**:
- "Later" never comes; documentation falls permanently behind
- Context is lost between development and documentation
- Batch documentation is lower quality (you forget the details)
- Creates a growing documentation debt

**Evidence**: Projects without a documentation-maintenance gate consistently have stale CLAUDE.md files and empty HISTORY.md. Projects with the gate maintain accurate documentation throughout.

**Do this instead**: Documentation-maintenance skill as a pre-PR checklist. Every PR is an opportunity to capture what you learned. Small, incremental updates maintain accuracy. The best time to document a gotcha is when you hit it.

## Anti-Pattern 4: Over-Structuring Simple Projects

**What it looks like**: A weekend script with 10 skills, custom commands, hooks, HISTORY.md, .docs/ directory, and a comprehensive evaluation pipeline.

**Why it fails**:
- Overhead exceeds value for simple projects
- Maintenance of unused structure creates guilt and noise
- New contributors are overwhelmed by meta-layer complexity
- Structure exists "just in case" rather than because it's needed

**Evidence**: Tier system was introduced specifically to prevent this. Minimal tier (CLAUDE.md + 2 skills) is sufficient for most scripts and small tools.

**Do this instead**: Start with minimal tier. Upgrade when you see the signals:
- 3+ architectural modules? Consider standard tier
- Multiple contributors? Consider standard tier
- Complex CI/CD pipeline? Consider comprehensive tier

The upgrade signals in `templates/directory-structures/` are explicit about when to level up.

## Anti-Pattern 5: Generic Documentation

**What it looks like**: Gotchas like "be careful with tests." Architecture descriptions like "follows MVC pattern." Build commands like "run the build tool."

**Why it fails**:
- Claude already knows generic patterns -- it doesn't need you to repeat them
- Generic documentation is indistinguishable from no documentation
- Developers ignore generic advice (it doesn't help them)
- Takes up space that could be used for specific, actionable content

**Evidence**: Audit scoring (Content Quality dimension) specifically penalizes generic content. Projects with specific gotchas (file paths, exact scenarios, concrete workarounds) score significantly higher and report fewer recurring issues.

**Do this instead**: Every documentation entry should answer: "What would a developer need to know that they can't figure out from the code alone?"

Good:
- "Never put handler tests in the `manufacturers` package -- classpath shadowing causes test failures that pass in full suite but fail individually"
- "Use `Set.of()` not `HashSet` in `getSupportedTypes()` -- HashSet iteration order is non-deterministic and causes flaky tests"

Bad:
- "Be careful with test package placement"
- "Use immutable collections when possible"

## Summary Matrix

| Anti-Pattern | Root Cause | Fix |
|-------------|-----------|-----|
| Encyclopedic CLAUDE.md | "More is better" thinking | Index + on-demand skills |
| Template-only skills | Structure without content | Fewer, well-populated skills |
| Documentation as separate task | "We'll do it later" | Pre-PR gate (documentation-maintenance skill) |
| Over-structuring simple projects | "Just in case" | Tiered structure with upgrade signals |
| Generic documentation | Not knowing what's useful | Project-specific content with file paths and examples |
