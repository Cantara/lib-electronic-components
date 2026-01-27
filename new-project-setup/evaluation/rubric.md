# Evaluation Rubric: LLM-Assisted Development Quality

100-point scoring across four dimensions, 25 points each.

## Dimension 1: Structure (25 points)

Does the meta-layer exist, follow conventions, and have the right organization?

### CLAUDE.md (10 points)

| Points | Criteria |
|--------|----------|
| 0 | No CLAUDE.md exists |
| 2 | CLAUDE.md exists but is minimal (< 20 lines, missing key sections) |
| 5 | CLAUDE.md has most sections but exceeds 300 lines or mixes index with content |
| 8 | CLAUDE.md under 300 lines with all required sections, acts as index |
| 10 | CLAUDE.md is concise, well-organized, under 300 lines, and every section is populated with project-specific content |

**Required sections**: Build Commands, Git Workflow, Project Overview, Architecture, Skills Index, Critical Gotchas

### Skills Directory (8 points)

| Points | Criteria |
|--------|----------|
| 0 | No `.claude/skills/` directory |
| 2 | Directory exists with 1-2 skills |
| 4 | Minimum skills for the detected tier |
| 6 | Skills have valid YAML frontmatter and follow skeleton structure |
| 8 | Skills are well-organized, named consistently, and cover key domains |

### Supporting Structure (7 points)

| Points | Criteria |
|--------|----------|
| 0 | No supporting structure beyond CLAUDE.md |
| 2 | HISTORY.md or .docs/ exists |
| 4 | Both HISTORY.md and .docs/ exist (for standard+ tier) |
| 5 | Commands or hooks directory exists (for comprehensive tier) |
| 7 | Full structure appropriate for the project's tier, including .docs/history/ |

## Dimension 2: Content Quality (25 points)

Is the content accurate, specific, actionable, and project-specific?

### Build Commands Accuracy (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No build commands or completely wrong |
| 2 | Build commands exist but are incomplete or partially wrong |
| 4 | Build commands are correct and cover common operations |
| 5 | Build commands are correct, cover edge cases, and include project-specific flags |

### Architecture Description (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No architecture section |
| 2 | Generic description that could apply to any project |
| 4 | Describes actual modules, patterns, and key classes |
| 5 | Describes architecture with file references, dependency flow, and rationale |

### Gotchas and Patterns (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No gotchas documented |
| 2 | Generic gotchas (e.g., "be careful with tests") |
| 4 | Project-specific gotchas with actionable detail |
| 5 | Gotchas include specific file paths, commands, and workarounds from real experience |

### Skill Content Depth (5 points)

Sample 2-3 skills and evaluate:

| Points | Criteria |
|--------|----------|
| 0 | Skills are empty or template-only (unfilled placeholders) |
| 2 | Skills have basic content but lack specificity |
| 4 | Skills have project-specific patterns, code examples, and common mistakes |
| 5 | Skills are comprehensive with real examples, tested patterns, and learned gotchas |

### Technical Debt Documentation (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No technical debt section |
| 2 | Technical debt section exists but is empty or generic |
| 4 | Lists real technical debt items with priority |
| 5 | Items include rationale, impact, and suggested remediation |

## Dimension 3: Operational Integration (25 points)

Does the meta-layer connect to actual development workflows?

### Skills Index Consistency (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No skills index in CLAUDE.md |
| 2 | Skills index exists but doesn't match actual skills on disk |
| 4 | Skills index matches disk, with descriptions |
| 5 | Skills index is organized by category, matches disk, all descriptions accurate |

### Git Workflow Integration (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No git workflow documented |
| 2 | Generic git workflow (doesn't match actual project practice) |
| 4 | Workflow matches actual branching strategy and PR process |
| 5 | Workflow includes project-specific conventions (branch naming, commit format, CI checks) |

### Skill Usability (5 points)

| Points | Criteria |
|--------|----------|
| 0 | Skills not referenced from CLAUDE.md |
| 2 | Skills referenced but triggers don't match actual use cases |
| 4 | Skills have accurate triggers and are loadable via `/skill-name` |
| 5 | Skills cover the domains most frequently worked on, with clear "When to Use" sections |

### Commands and Automation (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No custom commands |
| 2 | Commands exist but are broken or outdated |
| 3 | Commands exist and work (minimal for standard tier) |
| 4 | Commands cover common workflows |
| 5 | Commands are well-documented and referenced in CLAUDE.md |

*Note: For minimal tier, max 3 points here (commands not expected).*

### Documentation Maintenance Process (5 points)

| Points | Criteria |
|--------|----------|
| 0 | No documentation maintenance process |
| 2 | documentation-maintenance skill exists but shows no evidence of use |
| 4 | Evidence that docs are updated during development (non-template HISTORY.md entries) |
| 5 | Active maintenance: HISTORY.md has recent entries, skills reflect accumulated learnings, FIXED_BUGS.md has entries |

## Dimension 4: Evolution Readiness (25 points)

Can the meta-layer grow and improve over time?

### History Tracking (6 points)

| Points | Criteria |
|--------|----------|
| 0 | No HISTORY.md |
| 2 | HISTORY.md exists with only the init entry |
| 4 | HISTORY.md has 3+ real entries from development sessions |
| 6 | HISTORY.md is actively maintained with entries spanning multiple sessions/PRs |

### Learning Accumulation (7 points)

| Points | Criteria |
|--------|----------|
| 0 | No evidence of accumulated learnings |
| 2 | Some skills have non-template content |
| 4 | Multiple skills contain project-specific patterns and gotchas from experience |
| 6 | Skills, gotchas, and technical debt sections all show evidence of updates beyond initial setup |
| 7 | Clear evolution visible: skills have been refined, gotchas added, patterns discovered over time |

### Bug and Decision Documentation (6 points)

| Points | Criteria |
|--------|----------|
| 0 | No bug or decision documentation |
| 2 | `.docs/` directory exists but is mostly empty |
| 4 | FIXED_BUGS.md has real entries OR decision records exist |
| 6 | Both bug documentation and decision records exist with real, detailed entries |

### Feedback Loop Readiness (6 points)

| Points | Criteria |
|--------|----------|
| 0 | No mechanism for feedback or improvement |
| 2 | documentation-maintenance skill exists as a gate |
| 4 | Maintenance process is documented AND has evidence of being followed |
| 6 | Project has been audited, scores tracked, and improvements made based on audit results |

## Scoring Summary

| Score Range | Rating | Description |
|-------------|--------|-------------|
| 90-100 | Excellent | Exemplary LLM-assisted development structure |
| 75-89 | Good | Solid structure with minor gaps |
| 60-74 | Fair | Functional but missing key elements |
| 40-59 | Developing | Basic structure in place, significant gaps |
| 0-39 | Initial | Minimal or no LLM-development structure |

## Expected Scores by Stage

| Project Stage | Expected Score |
|--------------|---------------|
| Just initialized (any tier) | 55-70 |
| After 1 month of active development | 65-80 |
| After 3 months of active development | 75-90 |
| Mature project (6+ months) | 80-100 |

These are guidelines, not targets. A focused project may reach 85 in one month; a rarely-touched project may stay at 65 for a year.
