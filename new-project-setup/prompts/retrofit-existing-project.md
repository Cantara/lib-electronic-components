# Prompt: Retrofit Existing Project with LLM-Assisted Development Structure

## Context

You are adding LLM-assisted development structure to an existing project that was not initialized with this bootstrapper. This is a gap analysis and incremental application process -- not a from-scratch init.

The target project path is provided as an argument: `{{TARGET_PATH}}`

## Step 1: Analyze Existing State

Read the project to understand what already exists:

1. **Check for existing meta-layer**:
   - `CLAUDE.md` or similar instructions file
   - `.claude/` directory (skills, commands, hooks)
   - `HISTORY.md` or changelog
   - `.docs/` or documentation directory
   - `CONTRIBUTING.md` or development guides

2. **Analyze the codebase**:
   - Primary language and build system (check for pom.xml, package.json, Cargo.toml, etc.)
   - Project structure and architecture patterns
   - Test framework and test organization
   - Git workflow (check branch naming, PR templates, CI config)

3. **Check for institutional knowledge**:
   - README.md content
   - Code comments documenting patterns or gotchas
   - CI/CD configuration
   - Existing documentation

## Step 2: Gap Analysis

Compare the existing state against the three tiers. Present findings to the user:

```
## Gap Analysis for {{PROJECT_NAME}}

### Already Present
- [x] Item that exists
- [x] Item that exists

### Missing (Recommended)
- [ ] Item to add (rationale)
- [ ] Item to add (rationale)

### Missing (Optional)
- [ ] Nice-to-have item
```

Ask the user:
1. **Which tier** should this project target? (Present recommendation with rationale)
2. **Which missing items** should be created? (Default: all recommended items)
3. **Should existing content be preserved or replaced?** (Default: preserve and augment)

## Step 3: Preserve Existing Content

If the project has an existing CLAUDE.md or instructions file:
- Read it completely
- Identify content that maps to template sections
- Identify content that doesn't map (preserve as-is or move to a skill)
- **Never delete existing content** without user confirmation

If the project has existing skills or documentation:
- Catalog them
- Check if they follow the `_skill-skeleton.md` structure
- Propose restructuring only if the user agrees

## Step 4: Load Templates

Same as init prompt (Step 2), but with awareness of what already exists.

Read templates from `new-project-setup/templates/`:
- `core/CLAUDE.md.template`
- `core/HISTORY.md.template` (if needed)
- `skills/_skill-skeleton.md`
- Appropriate tier spec from `templates/directory-structures/`
- Language module from `templates/language-specific/` (if available)

## Step 5: Create Missing Structure

For each missing item identified in the gap analysis:

### CLAUDE.md (if missing or incomplete)
- If missing: Generate from template, populated with information from existing README/docs
- If incomplete: Merge missing sections into existing file, preserving current content
- **Hard constraint**: Result must be under 300 lines

### Skills (if missing or insufficient)
- Create skills appropriate for the tier using `_skill-skeleton.md`
- Populate with patterns discovered during codebase analysis (Step 1)
- If existing skills need restructuring, create new versions alongside old ones

### HISTORY.md (if missing and standard+ tier)
- Generate from template
- Back-fill significant milestones from git log if available:
  ```
  git log --oneline --since="6 months ago" | head -20
  ```
- Record the retrofit as a timeline entry

### Directory structure
- Create any missing directories (`.claude/skills/`, `.docs/`, etc.)
- Never move or rename existing directories

## Step 6: Integrate With Existing Workflow

Adapt the structure to the project's existing conventions:

- **Git workflow**: Match the project's existing branch naming and PR process
- **Build commands**: Extract from existing CI config, Makefile, or build files
- **Code style**: Note any existing linters, formatters, or style guides in CLAUDE.md

## Step 7: Verify

After retrofitting, verify:
- [ ] CLAUDE.md exists and is under 300 lines
- [ ] No existing content was lost (diff check)
- [ ] Skills directory has appropriate number of skills for the tier
- [ ] Each new skill has valid YAML frontmatter
- [ ] No `{{PLACEHOLDER}}` strings remain in generated files
- [ ] Build commands are accurate
- [ ] Existing workflows are documented, not replaced

## Step 8: Report

Tell the user what was done:

```
## Retrofit Summary for {{PROJECT_NAME}}

### Created
- List of new files with descriptions

### Modified
- List of modified files with description of changes

### Preserved (unchanged)
- List of existing files left intact

### Recommendations
- Suggested next steps
- Skills that should be added as project patterns emerge
- Areas where the existing documentation could be improved
```

Suggest running `/audit-project` to establish a baseline score.

## Notes

- Retrofitting is inherently conservative: add, don't replace
- When in doubt, ask the user rather than guessing
- If the project has a very different structure philosophy, adapt rather than force compliance
- Always create a git commit boundary before and after retrofitting so changes can be reviewed or reverted
