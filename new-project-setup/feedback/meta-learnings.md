# Meta-Learnings

Cross-project patterns and insights about the bootstrapper itself. This file is append-only -- add new entries at the bottom.

## Format

Each entry follows this structure:

```markdown
## Learning: {Title} ({Date})

**Source**: {Project report, direct observation, or user feedback}
**Confidence**: {High / Medium / Low}

**Observation**: What was noticed

**Implication**: What this means for the bootstrapper

**Action taken**: What changed (or "None yet - pending review")
```

---

## Learning: Initial Calibration from lib-electronic-components (2026-01-27)

**Source**: Direct observation during bootstrapper creation
**Confidence**: High

**Observation**: The lib-electronic-components project evolved its LLM-development structure organically over several months. Key findings:
- CLAUDE.md shrank from 1010 to 166 lines (84% reduction) through extracting to skills
- 39 skills accumulated totaling 10,643 lines of domain knowledge
- The documentation-maintenance gate prevented knowledge loss during rapid development
- Haiku delegation saved ~55% on pattern-following tasks

**Implication**: The bootstrapper should encourage early skill extraction rather than cramming content into CLAUDE.md. The 300-line limit is validated. Documentation gates should be a standard-tier feature, not optional.

**Action taken**: 300-line hard limit set in all templates. Documentation-maintenance skill included in all tiers (even minimal).
