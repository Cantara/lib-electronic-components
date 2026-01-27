# Proven Patterns

Patterns distilled from real projects that have demonstrated effectiveness in LLM-assisted development. Each pattern includes evidence from actual use.

## Pattern 1: Index, Don't Enumerate

**What**: CLAUDE.md acts as a table of contents pointing to skills, not as an encyclopedia containing all knowledge.

**Evidence**: lib-electronic-components reduced CLAUDE.md from 1010 to 166 lines. The 39 skills (10,643 lines) are loaded on demand. No information was lost. Claude finds relevant skills faster through the index than by scanning a long document.

**How to apply**:
- Keep CLAUDE.md under 300 lines
- Use a Skills Index table with skill name, trigger, and one-line description
- Move any section exceeding ~20 lines of domain detail into its own skill
- Reference skills with `/skill-name` syntax

**Anti-pattern**: A 500+ line CLAUDE.md that tries to document every pattern, gotcha, and architectural decision inline.

## Pattern 2: On-Demand Skill Loading

**What**: Domain knowledge is organized into skill files loaded by Claude when relevant to the current task, rather than all at once.

**Evidence**: With 39 skills totaling 10,643 lines, loading everything into context would consume significant token budget and dilute relevance. On-demand loading means Claude only sees the 50-200 lines relevant to the current task.

**How to apply**:
- One skill per domain or subsystem
- YAML frontmatter with trigger phrases for discoverability
- "When to Use" section at the top of each skill
- Consistent structure following the skill skeleton template

**Anti-pattern**: Loading all skills at session start, or having one mega-skill that covers everything.

## Pattern 3: Documentation Gate Before PR

**What**: A documentation-maintenance skill that serves as a mandatory pre-PR checklist, ensuring the meta-layer stays current with code changes.

**Evidence**: Without this gate, meta-layer documentation drifts as code evolves. With it, every PR is an opportunity to capture new patterns, gotchas, and learnings. The lib-electronic-components project maintained accurate documentation through 130+ PRs using this pattern.

**How to apply**:
- Create a documentation-maintenance skill with explicit checklist
- Reference it in CLAUDE.md git workflow section
- Include checks for: CLAUDE.md accuracy, skill updates, HISTORY.md entries, gotcha additions
- Run the checklist before every PR

**Anti-pattern**: Treating documentation as a separate task to do "later" (later never comes).

## Pattern 4: Tiered Structure

**What**: Three levels of meta-layer complexity (minimal/standard/comprehensive) chosen based on project size and team.

**Evidence**: Applying comprehensive structure to a simple script creates overhead. Applying minimal structure to a complex system leaves gaps. The tier system provides appropriate structure for each project size.

**How to apply**:
- **Minimal**: CLAUDE.md + 2 skills (scripts, small tools)
- **Standard**: + HISTORY.md + .docs/ + 5 skills (production projects)
- **Comprehensive**: + hooks, commands, 10+ skills (complex systems)
- Include upgrade signals in tier definitions so projects know when to level up

**Anti-pattern**: One-size-fits-all structure, or no structure at all.

## Pattern 5: Cost-Effective Delegation

**What**: Routing straightforward tasks to cheaper models (Haiku) while reserving expensive models (Opus/Sonnet) for tasks requiring judgment.

**Evidence**: In lib-electronic-components, approximately 60% of sub-tasks were pattern-following work (test fixes, simple refactoring, documentation updates) suitable for Haiku at 1/12th the cost. Estimated 55% cost savings per session.

**How to apply**:
- Create a task-delegation skill with a clear delegation matrix
- Provide explicit patterns: what to delegate vs. what to keep
- Include guidance on writing good delegation prompts (context, examples, file paths, definition of done)
- Never delegate security, architecture, or ambiguous tasks

**Anti-pattern**: Using the most expensive model for everything, or delegating tasks that require judgment.

## Pattern 6: Append-Only Feedback

**What**: Project reports and meta-learnings are never deleted or modified, creating a growing corpus of evidence for template improvement.

**Evidence**: This pattern comes from the self-improvement loop design. Append-only ensures:
- No loss of historical signal
- Trend analysis across all reports
- Evidence for template changes is preserved
- Mistakes are visible and learnable

**How to apply**:
- Audit reports saved to `feedback/project-reports/` with date suffix
- Meta-learnings appended to a single file
- Re-audits produce new reports (old ones kept)
- Self-improve reads the full corpus

**Anti-pattern**: Overwriting reports, deleting "outdated" feedback, or not recording learnings at all.

## Pattern 7: Concrete Over Abstract

**What**: Documentation should include specific file paths, line numbers, exact commands, and real examples rather than abstract advice.

**Evidence**: Generic gotchas like "be careful with tests" provide no value. Specific gotchas like "Never put handler tests in the `manufacturers` package due to classpath shadowing" are immediately actionable. In lib-electronic-components, specific gotchas prevented recurring bugs.

**How to apply**:
- Gotchas include file paths and exact scenarios
- Skills include code examples from the actual codebase
- Build commands include project-specific flags, not just generic ones
- Architecture descriptions reference real modules and classes

**Anti-pattern**: Abstract documentation that could apply to any project ("follow best practices", "write clean code", "test your changes").
