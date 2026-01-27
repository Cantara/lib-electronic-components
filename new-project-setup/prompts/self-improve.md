# Prompt: Self-Improve Templates and Prompts

## Context

You are analyzing feedback from projects that used this bootstrapper and proposing improvements to the templates, prompts, and evaluation criteria. This is the meta-improvement step of the feedback loop.

## Step 1: Gather Data

Read all available feedback:

1. **Project reports**: Read all files in `new-project-setup/feedback/project-reports/`
2. **Meta-learnings**: Read `new-project-setup/feedback/meta-learnings.md`
3. **Evolution history**: Read `new-project-setup/EVOLUTION.md` for past changes

Also read the current state of templates:
4. **Current templates**: Read key template files in `new-project-setup/templates/`
5. **Current prompts**: Read all files in `new-project-setup/prompts/`
6. **Current rubric**: Read `new-project-setup/evaluation/rubric.md`

## Step 2: Analyze Patterns

Look for these signals across project reports:

### Template Improvement Signals
- **Consistent modifications**: 3+ projects change the same section after initialization
- **Low-scoring dimensions**: A dimension that scores below 15/25 across multiple projects
- **Missing content**: Content that retrofitting always adds but init doesn't create
- **Unused structure**: Template sections that remain empty across projects

### Prompt Improvement Signals
- **Repeated questions**: Topics where users consistently need follow-up clarification
- **Skipped steps**: Steps that are always skipped or never produce useful results
- **Wrong assumptions**: Assumptions in prompts that don't hold for certain project types
- **Missing guidance**: Areas where the prompt doesn't provide enough direction

### Rubric Improvement Signals
- **Score ceiling**: Dimensions where even mature projects can't score above 20/25
- **Score inflation**: Dimensions where new projects easily score 20+/25
- **Missing criteria**: Quality gaps not captured by any rubric dimension
- **Unclear criteria**: Scoring criteria that are ambiguous or inconsistently applied

## Step 3: Cross-Reference with Industry Practices

Read `new-project-setup/reference/industry-practices.md` and consider:
- Are there new best practices not yet reflected in templates?
- Have any referenced practices become outdated?
- Are there tools or approaches gaining adoption that should be supported?

## Step 4: Generate Proposals

For each identified improvement, create a proposal:

```markdown
### Proposal: {{TITLE}}

**Signal**: What evidence triggered this proposal
**Change type**: Template / Prompt / Rubric / New language module / New skill template
**Files affected**: List of files to modify
**Current state**: What the file says now (quote relevant section)
**Proposed state**: What the file should say (full replacement text)
**Rationale**: Why this change improves outcomes
**Risk**: What could go wrong with this change
**Evidence strength**: Strong (5+ projects) / Moderate (3-4 projects) / Weak (1-2 projects)
```

### Proposal Constraints

- Never remove existing rubric criteria (only add or refine)
- Never reduce tier requirements (only increase or add alternatives)
- Template changes must maintain the `{{PLACEHOLDER}}` convention
- Philosophy changes require extra justification (these are foundational)
- New language modules are always additive (never changes to core)

## Step 5: Prioritize

Rank proposals by:
1. **Evidence strength** (strong > moderate > weak)
2. **Impact** (affects many projects > affects few)
3. **Risk** (low risk > high risk)
4. **Effort** (small change > large change)

Present the top 5 proposals (or fewer if there aren't enough).

## Step 6: Update Meta-Learnings

Append to `new-project-setup/feedback/meta-learnings.md`:

```markdown
## Analysis: {{DATE}}

### Data Sources
- {{N}} project reports analyzed
- Date range: {{EARLIEST}} to {{LATEST}}

### Key Patterns Found
1. ...
2. ...

### Proposals Generated
1. {{PROPOSAL_TITLE}} ({{EVIDENCE_STRENGTH}})
2. ...

### Proposals Accepted by Human
{{TO BE FILLED AFTER REVIEW}}
```

## Step 7: Present to Human

Show the proposals to the user in priority order. For each:
1. State the problem (with evidence)
2. Show the proposed change (diff format if possible)
3. Explain the expected impact
4. Ask for accept/reject/modify

After human review:
- Apply accepted proposals
- Record decisions in meta-learnings
- Update EVOLUTION.md with version bump if changes were made

## Notes

- If there are fewer than 3 project reports, note that evidence is limited and proposals are preliminary
- If no project reports exist, analyze the templates against the rubric to identify self-referential improvements
- The self-improve prompt itself can be the subject of improvement proposals (meta-meta-improvement)
- Avoid proposing changes for the sake of change; every proposal needs evidence
- Consider the bootstrapper's own PHILOSOPHY.md when evaluating proposals -- changes should align with the stated principles
