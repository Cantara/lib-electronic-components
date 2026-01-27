# Prompt: Audit Project Against LLM-Assisted Development Rubric

## Context

You are evaluating a project's LLM-assisted development meta-layer against the quality rubric. This produces a scored report that feeds back into the self-improvement loop.

The target project path is provided as an argument: `{{TARGET_PATH}}`

## Step 1: Read the Rubric

Load `new-project-setup/evaluation/rubric.md` for the full 100-point scoring criteria.
Load `new-project-setup/evaluation/checklist.md` for pass/fail prerequisites.

## Step 2: Pass/Fail Prerequisites

Before scoring, check the prerequisites from `checklist.md`. If any prerequisite fails, note it in the report but continue with scoring (a project can score points in other areas even without a CLAUDE.md, though it will score low).

## Step 3: Analyze the Project

Thoroughly examine the project's meta-layer:

### Structure Analysis
- Read CLAUDE.md (line count, section presence, organization)
- List all skills in `.claude/skills/` (count, naming, YAML frontmatter)
- Check for HISTORY.md, .docs/, commands, hooks
- Determine the effective tier (minimal/standard/comprehensive)

### Content Quality Analysis
- Read CLAUDE.md for accuracy and specificity
  - Are build commands correct? (Try running them mentally or actually)
  - Do architecture descriptions match actual code structure?
  - Are gotchas real and actionable?
- Sample 2-3 skills for depth and accuracy
  - Do patterns reflect actual codebase patterns?
  - Are common mistakes documented from real experience?
  - Is the checklist actionable?

### Operational Integration Analysis
- Check if skills are referenced in CLAUDE.md skills index
- Check if build commands work (if possible)
- Check if git workflow matches actual branching strategy
- Look for evidence of skill usage (meaningful, non-template content)
- Check for slash commands in `.claude/commands/`

### Evolution Readiness Analysis
- Check for HISTORY.md with real entries (not just template)
- Check for `.docs/` with real documentation
- Check for evidence of accumulated learnings (skills with non-template content)
- Check for feedback mechanisms (documentation-maintenance skill, pre-commit process)

## Step 4: Score Each Dimension

Score according to the rubric (25 points per dimension, 100 total).

For each dimension, assign a score and provide:
- The numeric score
- 2-3 sentences of justification
- Specific examples (file paths, line numbers)
- One concrete improvement recommendation

## Step 5: Generate Report

Create a report in the following format:

```markdown
# Audit Report: {{PROJECT_NAME}}

**Date**: {{AUDIT_DATE}}
**Auditor**: Claude (automated)
**Path**: {{TARGET_PATH}}
**Detected Tier**: {{DETECTED_TIER}}

## Pass/Fail Prerequisites

| Prerequisite | Status | Notes |
|-------------|--------|-------|
| CLAUDE.md exists | PASS/FAIL | ... |
| ... | ... | ... |

## Scores

| Dimension | Score | Max | Rating |
|-----------|-------|-----|--------|
| Structure | X | 25 | Excellent/Good/Fair/Poor |
| Content Quality | X | 25 | Excellent/Good/Fair/Poor |
| Operational Integration | X | 25 | Excellent/Good/Fair/Poor |
| Evolution Readiness | X | 25 | Excellent/Good/Fair/Poor |
| **Total** | **X** | **100** | **Rating** |

### Rating Scale
- 90-100: Excellent - exemplary LLM-assisted development structure
- 75-89: Good - solid structure with minor gaps
- 60-74: Fair - functional but missing key elements
- 40-59: Developing - basic structure in place, significant gaps
- 0-39: Initial - minimal or no LLM-development structure

## Dimension Details

### Structure (X/25)

**Score justification**: ...

**Strengths**:
- ...

**Gaps**:
- ...

**Top recommendation**: ...

### Content Quality (X/25)

**Score justification**: ...

**Strengths**:
- ...

**Gaps**:
- ...

**Top recommendation**: ...

### Operational Integration (X/25)

**Score justification**: ...

**Strengths**:
- ...

**Gaps**:
- ...

**Top recommendation**: ...

### Evolution Readiness (X/25)

**Score justification**: ...

**Strengths**:
- ...

**Gaps**:
- ...

**Top recommendation**: ...

## Top 3 Improvements (Priority Order)

1. **...**: ... (estimated impact: +X points)
2. **...**: ... (estimated impact: +X points)
3. **...**: ... (estimated impact: +X points)

## Comparison Notes

{{If this project was previously audited, compare scores.}}
{{If other projects have been audited, note where this project stands relative to them.}}
```

## Step 6: Save Report

Save the report to `new-project-setup/feedback/project-reports/{{PROJECT_NAME}}-{{DATE}}.md`.

If this is a re-audit, keep the old report (append-only) and note the score change.

## Step 7: Present Results

Show the user:
1. The total score and rating
2. The dimension breakdown
3. The top 3 improvements
4. Suggest running `/self-improve` if there are 3+ project reports

## Notes

- Be honest in scoring. Inflated scores undermine the feedback loop.
- Score based on what exists, not what's planned.
- A project that just completed init should score in the 60-75 range (structure is there, but content hasn't accumulated yet).
- A mature project with accumulated learnings should score 80+.
- Perfect scores (100) should be rare -- there's always room for improvement.
