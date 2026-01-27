# Philosophy: LLM-Assisted Development Structure

## Core Thesis

**The quality of LLM-assisted development is determined by the quality of the meta-layer surrounding the code, not by the code itself.**

An LLM with excellent project context produces better code than an expert LLM with no context. The meta-layer -- instructions, skills, history, patterns -- is the multiplier. This bootstrapper creates and evolves that meta-layer.

## First Principles

### 1. Context is the Product

Code is the output. Context is the product. When an LLM reads a well-structured CLAUDE.md with on-demand skills, it operates with the accumulated wisdom of every previous session. Without this, each session starts from zero.

**Implication**: Invest in the meta-layer (CLAUDE.md, skills, history) as seriously as the code. It compounds over time.

### 2. Index, Don't Enumerate

A 1000-line CLAUDE.md is worse than a 150-line CLAUDE.md that indexes 40 skills. The index tells Claude *where* to find knowledge. Loading everything upfront wastes context window and dilutes signal.

**Evidence**: The lib-electronic-components project reduced CLAUDE.md from 1010 to 166 lines (84% reduction) with zero information loss. Knowledge moved to 39 on-demand skills (10,643 lines total).

**Implication**: CLAUDE.md should be under 300 lines. Domain knowledge belongs in skills loaded via `/skill-name`.

### 3. Prompts Over Scripts

Shell scripts encode fixed logic. Prompts encode intent that Claude interprets contextually. A prompt saying "analyze the codebase and create appropriate skills" adapts to any project. A script that runs `find . -name "*.java"` breaks on Python projects.

More importantly, prompts are self-improvable. A future Claude instance can read a prompt, evaluate whether it achieves its goals, and propose better phrasing. Scripts require a programmer to improve.

**Implication**: Use Markdown prompts with `{{PLACEHOLDER}}` syntax. Reserve scripts only for mechanical operations (file copying, git commands).

### 4. Tiers Prevent Over-Engineering

Not every project needs 40 skills and a comprehensive evaluation pipeline. A weekend script needs a CLAUDE.md and maybe two skills. Forcing comprehensive structure on simple projects creates noise.

**Implication**: Three tiers (minimal/standard/comprehensive) with clear criteria for which to use. Default to minimal; escalate when needed.

### 5. Feedback Loops Create Compound Returns

A template used once is static. A template that incorporates feedback from every project it initializes improves continuously. The self-improvement loop (use → audit → analyze → propose → review → update) is the mechanism.

**Implication**: Every audit produces a project report. The self-improve prompt reads all reports and proposes template changes. Humans review changes. The loop must be easy to execute -- one slash command each.

### 6. Language-Agnostic Core, Language-Specific Modules

Build commands differ between Maven and npm. Architecture patterns differ between Java and Python. But the *structure* of LLM-assisted development (instructions file, skills, history, evaluation) is universal.

**Implication**: Core templates work for any language. Language-specific content lives in mergeable fragments. Adding a new language means adding one directory, not rewriting the framework.

### 7. Human Review is Non-Negotiable

The self-improvement loop proposes changes; it does not apply them. Every template modification goes through human review. This prevents prompt drift, catches LLM blind spots, and maintains intentionality.

**Implication**: Self-improve generates a diff with rationale. Human accepts, modifies, or rejects. The review itself becomes part of the evolution history.

## Design Decisions and Their Rationale

### Why Markdown?

- Claude reads Markdown natively and with high fidelity
- Version-controllable in git alongside code
- Human-readable without special tooling
- Supports structured content (tables, code blocks, YAML frontmatter)
- Editable by both humans and LLMs

### Why `{{PLACEHOLDER}}` Syntax?

- Visually distinct from code and Markdown
- Easy for Claude to find-and-replace during initialization
- Self-documenting (placeholder names describe what goes there)
- Does not conflict with any programming language syntax
- Grep-friendly for auditing unfilled placeholders

### Why Not a CLI Tool?

A CLI tool (npm package, pip install, etc.) adds dependencies, versioning complexity, and a maintenance burden. Markdown files in a git repo are:
- Zero-dependency
- Self-contained
- Forkable and customizable
- Readable by any LLM, any time

The "tool" is Claude itself. The files are instructions.

### Why Rubric-Based Evaluation?

Checklists give binary pass/fail but miss nuance. A rubric with dimensions (Structure, Content Quality, Operational Integration, Evolution Readiness) scores on a gradient. This reveals *where* a project is strong and where it needs work.

The four dimensions (25 points each, 100 total) were chosen to cover the full lifecycle:
1. **Structure** - Does the meta-layer exist and follow conventions?
2. **Content Quality** - Is the content accurate, specific, and actionable?
3. **Operational Integration** - Does the meta-layer connect to actual workflows?
4. **Evolution Readiness** - Can the meta-layer grow and improve over time?

## What This Is NOT

- **Not a code generator**: This creates the *meta-layer*, not application code
- **Not an AI wrapper**: No API calls, no inference, just documents Claude reads
- **Not prescriptive about code architecture**: Your code can follow any pattern; this structures the AI-assistance layer
- **Not a one-time setup**: The value comes from continuous use of the feedback loop
- **Not a replacement for thinking**: Claude is the tool; the developer makes decisions

## Measuring Success

This bootstrapper succeeds when:

1. **New projects** reach productive LLM-assisted development faster
2. **Existing projects** measurably improve their LLM collaboration quality
3. **Templates evolve** based on real project feedback (not theoretical improvements)
4. **Audit scores** trend upward across projects over time
5. **The bootstrapper itself** improves through its own self-improvement loop

The ultimate test: does a developer using this produce better outcomes with less friction than one who starts from scratch each time?
