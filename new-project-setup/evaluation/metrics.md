# Metrics: Measuring LLM-Assisted Development Effectiveness

Quantitative measures to track over time. These complement the rubric (qualitative) with numbers.

## Meta-Layer Metrics

### Size Metrics

| Metric | How to Measure | Target Range | Red Flags |
|--------|---------------|-------------|-----------|
| CLAUDE.md line count | `wc -l CLAUDE.md` | 100-300 | > 300 (too encyclopedic), < 50 (too sparse) |
| Skill count | `ls .claude/skills/*.md \| wc -l` | Tier-dependent | Below tier minimum, or > 50 without organization |
| Total skill lines | `wc -l .claude/skills/*.md` | 200-1000 per skill avg | < 30 per skill (skeleton-only) |
| HISTORY.md entries | Count `###` headers | 1+ per month | No entries in 3+ months |

### Content Metrics

| Metric | How to Measure | Good Sign | Bad Sign |
|--------|---------------|-----------|----------|
| Placeholder remnants | `grep -r '{{' .claude/ CLAUDE.md` | 0 matches | Any matches |
| Skills with real content | Manual: non-template content present | > 80% of skills | < 50% of skills |
| Gotchas count | Count gotcha items in CLAUDE.md | 3-10 | 0 or > 20 (move to skills) |
| Technical debt items | Count items in debt section | Tracked and prioritized | Empty section or unmanaged growth |

### Freshness Metrics

| Metric | How to Measure | Healthy | Stale |
|--------|---------------|---------|-------|
| Last CLAUDE.md update | `git log -1 CLAUDE.md` | Within last month | > 3 months ago |
| Last skill update | `git log -1 .claude/skills/` | Within last 2 weeks | > 2 months ago |
| Last HISTORY.md entry | Check last date in file | Within last month | > 3 months ago |
| Skill-to-commit ratio | Skills updated / total commits | > 1:20 | < 1:100 |

## Development Effectiveness Metrics

These are harder to measure but more meaningful:

### Session Quality

| Metric | How to Measure | Indicates |
|--------|---------------|-----------|
| Context questions per session | Count "what does X do?" questions to Claude | Lower = better context; high = gaps in CLAUDE.md/skills |
| Re-explanation rate | Times Claude explains something already in a skill | Should be near 0 if skills are discoverable |
| Wrong-approach starts | Times Claude starts, then corrects course | Lower = better gotchas documentation |

### Cost Efficiency

| Metric | How to Measure | Target |
|--------|---------------|--------|
| Haiku delegation rate | % of sub-tasks delegated to Haiku | 40-60% for pattern-heavy projects |
| Cost per session | Track API costs | Decreasing trend as delegation increases |
| Rework rate | Changes reverted or redone | Decreasing trend |

### Knowledge Retention

| Metric | How to Measure | Healthy |
|--------|---------------|---------|
| Gotchas rediscovered | Same bug hit twice without skill update | 0 (every gotcha documented after first encounter) |
| Pattern reinvention | Same pattern coded differently in different files | Decreasing (patterns documented in skills) |
| Onboarding time | Time for new contributor to make first PR | Decreasing (better CLAUDE.md and skills) |

## Tracking Over Time

### Recommended Cadence

| Activity | Frequency | What to Track |
|----------|-----------|---------------|
| Quick checklist | Every PR (via documentation-maintenance skill) | Pass/fail count |
| Full audit | Monthly or after major milestones | Rubric score (100-point) |
| Metrics snapshot | Quarterly | All size + freshness metrics |
| Cross-project comparison | When 3+ projects have been audited | Dimension averages |

### Storing Metrics

Audit scores are recorded in project reports (`feedback/project-reports/`).

For a quick snapshot, append to HISTORY.md:

```markdown
### YYYY-MM-DD - Metrics Snapshot
- CLAUDE.md: XXX lines
- Skills: XX files, XXXX total lines
- Audit score: XX/100 (Structure XX, Content XX, Integration XX, Evolution XX)
- Gotchas: XX documented
- Technical debt items: XX tracked
```

## Interpreting Trends

| Trend | What It Means | Action |
|-------|--------------|--------|
| Score increasing | Meta-layer is maturing | Continue current process |
| Score flat | Maintenance is keeping pace but not improving | Look for missing skills or stale content |
| Score decreasing | Code is evolving but meta-layer isn't | Enforce documentation-maintenance gate |
| CLAUDE.md growing past 300 | Knowledge not being moved to skills | Extract content to skills |
| Skills count growing rapidly | Possible fragmentation | Consider skill consolidation or organization |
| No HISTORY.md entries | Process not being followed | Review documentation-maintenance checklist |
