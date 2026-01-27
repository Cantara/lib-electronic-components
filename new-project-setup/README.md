# New Project Setup - Self-Improving LLM Development Bootstrapper

A framework for bootstrapping, auditing, and continuously improving LLM-assisted development workflows. Claude reads these prompts and templates to initialize projects with best-practice structure for AI-assisted software engineering.

## Quick Start

### Initialize a new project
```
/init-project /path/to/new-project
```

### Add structure to an existing project
```
/retrofit-project /path/to/existing-project
```

### Evaluate a project against the rubric
```
/audit-project /path/to/project
```

### Propose improvements to these templates
```
/self-improve
```

## What This Does

This bootstrapper creates and manages the **meta-layer** that makes LLM-assisted development effective:

1. **CLAUDE.md** - Project instructions file (< 300 lines) that acts as an index
2. **Skills** - On-demand domain knowledge loaded via `/skill-name`
3. **History** - Chronological project timeline
4. **Evaluation** - Rubric-based quality scoring
5. **Feedback loops** - Accumulated learnings that improve future projects

## Why Prompts, Not Scripts

Claude interprets Markdown prompts contextually. This is more powerful than shell scripts because Claude can:
- Adapt to project specifics and ask clarifying questions
- Handle edge cases through reasoning rather than branching logic
- Reason about the prompts themselves and propose improvements
- Apply language-agnostic patterns to any tech stack

Templates use `{{PLACEHOLDER}}` syntax that Claude fills in based on project context.

## Directory Structure

```
new-project-setup/
├── README.md                    # This file
├── PHILOSOPHY.md                # Design principles and core thesis
├── EVOLUTION.md                 # Change governance and version history
│
├── prompts/                     # Operational prompts Claude executes
│   ├── init-new-project.md      # Initialize from scratch
│   ├── retrofit-existing-project.md  # Apply to existing project
│   ├── audit-project.md         # Evaluate against rubric
│   └── self-improve.md          # Propose template improvements
│
├── templates/                   # Files adapted into target projects
│   ├── core/                    # Language-agnostic templates
│   ├── skills/                  # Starter skill templates
│   ├── directory-structures/    # Tier specs (minimal/standard/comprehensive)
│   └── language-specific/       # Language modules (java-maven, etc.)
│
├── evaluation/                  # Quality criteria
│   ├── rubric.md                # 100-point scoring
│   ├── checklist.md             # Quick pass/fail
│   └── metrics.md               # Quantitative measures
│
├── feedback/                    # Grows over time (append-only)
│   ├── project-reports/         # One report per audited project
│   └── meta-learnings.md        # Cross-project patterns
│
└── reference/                   # Background material
    ├── proven-patterns.md       # Patterns from real projects
    ├── industry-practices.md    # State of the art
    └── anti-patterns.md         # What to avoid
```

## Three Tiers

| Tier | When to Use | What You Get |
|------|-------------|-------------|
| **Minimal** | Quick scripts, small tools | CLAUDE.md + 2 skills |
| **Standard** | Production libraries, services | + HISTORY.md + .docs/ + 5 skills |
| **Comprehensive** | Complex systems, team projects | + hooks, commands, 10+ skills |

See `templates/directory-structures/` for exact specifications.

## The Self-Improvement Loop

```
Templates ──> Init/Retrofit Project ──> Project Accumulates Learnings
                                              │
                                              v
Human Review <── Self-Improve Analysis <── Audit Project
     │                                        │
     v                                        v
Update Templates <──────────────────── Project Reports
```

1. **Use**: Initialize or retrofit creates structure in a target project
2. **Accumulate**: Project develops, Claude adds learnings to skills
3. **Audit**: Score project against rubric (100 points, 4 dimensions)
4. **Analyze**: Self-improve reads all project reports, finds patterns
5. **Propose**: Claude suggests template/prompt changes with rationale
6. **Review**: Human accepts or rejects proposed changes
7. **Apply**: Updated templates improve the next initialization

## Language Support

The core is language-agnostic. Language-specific content lives in `templates/language-specific/{lang}/` as mergeable fragments.

**Currently supported**: Java/Maven

**Adding a new language**: Create a subdirectory in `templates/language-specific/` with a `CLAUDE.md.fragment` (build commands section) and a `skills/build.md` (build skill).

## Further Reading

- [PHILOSOPHY.md](PHILOSOPHY.md) - Design principles and core thesis
- [EVOLUTION.md](EVOLUTION.md) - How this bootstrapper evolves
- [evaluation/rubric.md](evaluation/rubric.md) - Full scoring criteria
- [reference/proven-patterns.md](reference/proven-patterns.md) - Battle-tested patterns
