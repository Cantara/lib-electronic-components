# Industry Practices: LLM-Assisted Development (2025-2026)

State of the art in structuring projects for LLM-assisted development, based on research and community practices.

## Claude Code Ecosystem

### Built-in `/init` Command

Claude Code includes a `/init` command that generates a starter CLAUDE.md by analyzing the codebase. It produces a basic file with:
- Build commands (detected from build files)
- Project description (from README)
- File structure overview

**Our extension**: This bootstrapper goes further with skills ecosystem, evaluation rubrics, tiered structure, and self-improvement loops. The built-in `/init` is a good starting point; this framework is the full lifecycle.

### CLAUDE.md Conventions

Community consensus as of early 2026:
- **Under 300 lines**: CLAUDE.md should be an index, not a manual
- **Sections**: Build, Git workflow, Architecture, Skills, Gotchas are standard
- **Skills over inline**: Domain knowledge in `.claude/skills/`, not CLAUDE.md body
- **Specificity**: Project-specific content only; no generic programming advice

### Skills Ecosystem

The `.claude/skills/` directory pattern has become standard for non-trivial projects:
- YAML frontmatter for metadata and triggers
- Loaded on demand via `/skill-name`
- One skill per domain (not per file or per class)
- 50-200 lines per skill is the sweet spot

### Custom Commands

`.claude/commands/` houses project-specific slash commands:
- Markdown files with prompts that Claude executes
- Can reference project files and templates
- Discoverable through Claude Code's command system

## Meta-Prompting Patterns

### Self-Referential Improvement

The practice of LLMs improving their own instructions:
- Generate instructions → evaluate effectiveness → refine instructions
- Applied to prompt engineering, documentation, and evaluation criteria
- Key insight: the LLM can reason about prompt quality in ways scripts cannot

### Reflective Loops

Generate → evaluate → refine cycles applied to development:
- Code generation with self-review
- Documentation that evaluates its own completeness
- Templates that track their own effectiveness

### Prompt Versioning

Treating prompts as versioned artifacts:
- Git-versioned alongside code
- Change history with rationale
- Rollback capability
- A/B testing between prompt versions (advanced)

## Documentation Patterns

### Living Documentation

Documentation that stays current through process, not heroic effort:
- Pre-commit/pre-PR gates that check documentation
- Skills updated as part of feature development, not as a separate task
- HISTORY.md entries as a natural part of PR creation
- Automated staleness detection (metrics on last-updated dates)

### Knowledge Extraction

Moving knowledge from people's heads to structured documents:
- Bug fixes → FIXED_BUGS.md entries
- Architecture discussions → decision records
- Repeated explanations → skill entries
- Environment setup issues → gotchas

### Hierarchical Organization

For large documentation sets:
- Top-level index (CLAUDE.md) < 300 lines
- Category indexes for large skill sets
- Individual skills as leaf nodes
- Cross-references between related skills

## Cost Optimization

### Model Tiering

Using different models for different task types:
- **Tier 1 (cheapest)**: Haiku for pattern-following, boilerplate, simple fixes
- **Tier 2 (balanced)**: Sonnet for standard development, moderate complexity
- **Tier 3 (premium)**: Opus for architecture, complex debugging, ambiguous tasks
- Savings of 50-70% reported by teams using tiered delegation

### Context Window Management

Strategies for effective context use:
- On-demand skill loading (not pre-loading everything)
- CLAUDE.md as index (small upfront load, skills loaded as needed)
- Focused task descriptions (one task per agent, not "fix everything")
- Summary-based history (HISTORY.md with highlights, not full logs)

## Evaluation Approaches

### Rubric-Based Assessment

Structured scoring over checklists:
- Multiple dimensions capture different quality aspects
- Gradient scoring reveals nuance (not just pass/fail)
- Reproducible across projects and over time
- Feeds into improvement loops

### Continuous Audit

Regular evaluation rather than one-time review:
- Monthly audits with score tracking
- Trend analysis across audits
- Automated metric collection where possible
- Cross-project comparison when multiple projects are managed

## Emerging Practices

### Multi-Agent Workflows

Projects structured for multiple AI agents working in parallel:
- Clear task boundaries in documentation
- Shared context through CLAUDE.md and skills
- Agent-specific commands for specialized workflows
- Coordination through git branches and PR reviews

### Template Ecosystems

Shareable, reusable project templates:
- Language-specific modules (build commands, framework patterns)
- Domain-specific skill libraries (web apps, data pipelines, libraries)
- Evaluation rubrics tailored to project types
- Community-maintained template registries (emerging)

### Feedback-Driven Evolution

Projects that systematically improve their own meta-layer:
- Audit → report → analyze → propose → review → apply cycle
- Evidence-based template changes (not theoretical)
- Cross-project learning (patterns from project A improve project B's templates)
- Version-controlled prompt evolution with change rationale

## Sources and Further Reading

These practices are synthesized from:
- Claude Code documentation and community discussions
- Anthropic's prompt engineering guides
- Open-source project CLAUDE.md files on GitHub
- LLM-assisted development blog posts and conference talks (2025-2026)
- Direct experience with the lib-electronic-components project
