# Prompt: Initialize New Project with LLM-Assisted Development Structure

## Context

You are setting up a new software project with a meta-layer optimized for LLM-assisted development. This prompt guides you through analyzing the project and creating the appropriate structure.

The target project path is provided as an argument: `{{TARGET_PATH}}`

## Step 1: Gather Project Information

Ask the user the following questions (skip any already answered):

1. **What does this project do?** (One paragraph description)
2. **What is the primary language and build system?** (e.g., Java/Maven, TypeScript/npm, Python/pip)
3. **What tier of structure do you want?**
   - **Minimal**: CLAUDE.md + 2 skills (scripts, small tools)
   - **Standard**: + HISTORY.md + .docs/ + 5 skills (production libraries, services)
   - **Comprehensive**: + hooks, commands, 10+ skills (complex systems, team projects)
4. **What are the 2-3 most important architectural concepts?** (e.g., "handler pattern", "event-driven", "REST API with middleware")
5. **Any critical gotchas a new contributor should know?** (Build quirks, environment setup, common mistakes)

## Step 2: Load Templates

Read the following template files from `new-project-setup/templates/`:

- `core/CLAUDE.md.template` - Main instructions file
- `core/HISTORY.md.template` - Project timeline (standard+ tier)
- `skills/_skill-skeleton.md` - For creating skills

Check for a language-specific module:
- Look in `templates/language-specific/` for a matching language directory
- If found, read the `CLAUDE.md.fragment` and any skill templates
- If not found, note that build commands will need manual specification

Load the appropriate tier specification:
- `templates/directory-structures/minimal.yaml`
- `templates/directory-structures/standard.yaml`
- `templates/directory-structures/comprehensive.yaml`

## Step 3: Create Directory Structure

Based on the selected tier, create the directory structure at the target path.

**All tiers**:
```
{{TARGET_PATH}}/
├── CLAUDE.md
└── .claude/
    └── skills/
```

**Standard tier adds**:
```
├── HISTORY.md
└── .docs/
    └── history/
```

**Comprehensive tier adds**:
```
├── .claude/
│   ├── commands/
│   └── hooks/
└── .docs/
    ├── history/
    └── decisions/
```

## Step 4: Generate CLAUDE.md

Fill in the `CLAUDE.md.template` with project-specific content:

1. **Build Commands**: From language module fragment, or ask user
2. **Git Workflow**: Use standard PR-based workflow (customize if user specifies different)
3. **Project Overview**: From user's description (Step 1)
4. **Architecture**: From user's architectural concepts (Step 1)
5. **Skills Index**: Table listing created skills with descriptions
6. **Critical Gotchas**: From user's gotchas (Step 1)
7. **Technical Debt**: Start empty with placeholder structure
8. **Historical Documentation**: Link to HISTORY.md if standard+ tier

**Hard constraint**: The generated CLAUDE.md must be under 300 lines. If it would exceed this, move content to skills.

## Step 5: Create Skills

Based on the tier, create skills using `_skill-skeleton.md` as the template:

**Minimal tier (2 skills)**:
1. `architecture.md` - Project architecture patterns
2. `documentation-maintenance.md` - Pre-commit checklist

**Standard tier (5 skills)** - adds:
3. `task-delegation.md` - Cost optimization (Haiku vs Sonnet)
4. Domain skill 1 (based on project type)
5. Domain skill 2 (based on project type)

**Comprehensive tier (10+ skills)** - adds:
6-10+. Domain-specific skills based on project complexity

For each skill:
- Use the `_skill-skeleton.md` template structure
- Fill in with project-specific content
- Ensure YAML frontmatter includes triggers

Load starter skill templates from `templates/skills/` and adapt them:
- `architecture.md` - Adapt to project's actual architecture
- `documentation-maintenance.md` - Adapt to project's workflow
- `task-delegation.md` - Include for standard+ tier

## Step 6: Generate HISTORY.md (Standard+ Tier)

Fill in the `HISTORY.md.template`:
- Set project name
- Set current date
- Record initialization as the first entry
- List created skills

## Step 7: Verify

After creation, verify:
- [ ] CLAUDE.md exists and is under 300 lines
- [ ] CLAUDE.md has all required sections (Build, Git, Overview, Architecture, Skills, Gotchas)
- [ ] Skills directory has the correct number of skills for the tier
- [ ] Each skill has valid YAML frontmatter
- [ ] No `{{PLACEHOLDER}}` strings remain in any generated file
- [ ] HISTORY.md exists (if standard+ tier)
- [ ] Build commands are accurate for the language/build system

## Step 8: Report

Tell the user what was created:
- List all files created with one-line descriptions
- Report the CLAUDE.md line count
- Suggest next steps:
  - "Review CLAUDE.md and adjust any sections"
  - "Run `/audit-project` after a few sessions to evaluate effectiveness"
  - "Add domain-specific skills as you discover patterns"

## Notes

- If the target path already has a CLAUDE.md, stop and suggest `/retrofit-project` instead
- If the target path doesn't exist, create it
- Always ask before overwriting any existing files
- When unsure about a section, create it with a `TODO:` marker rather than guessing
