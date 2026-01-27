# Evolution: How This Bootstrapper Changes

## Version History

### v0.1.0 - Initial Release (2026-01-27)
- Core templates: CLAUDE.md, HISTORY.md, skill skeleton
- Four operational prompts: init, retrofit, audit, self-improve
- Three tier specifications: minimal, standard, comprehensive
- Evaluation rubric (100-point, 4 dimensions)
- First language module: Java/Maven
- Four slash commands for Claude Code integration
- Reference material distilled from lib-electronic-components

## Change Governance

### Who Can Change What

| Change Type | Who Decides | Process |
|------------|-------------|---------|
| Template wording | Human reviewer | Self-improve proposes, human approves |
| New language module | Any contributor | PR with fragment + build skill |
| Rubric criteria | Original author + feedback | Evidence from 3+ project reports |
| Tier thresholds | Original author | Evidence of misclassification |
| New skill template | Any contributor | PR following `_skill-skeleton.md` |
| Prompt restructuring | Original author | Major version bump |
| Philosophy changes | Original author | Requires written rationale |

### Change Process

1. **Identify signal**: An audit reveals a gap, a project report highlights a pattern, or a user reports friction
2. **Propose change**: Create a PR with the change and a rationale section explaining:
   - What problem does this solve?
   - What evidence supports this change?
   - What could go wrong?
3. **Review**: Human reviews the change against the philosophy principles
4. **Apply**: Merge PR, update version in this file
5. **Validate**: Next audit or init should demonstrate improvement

### Signals That Trigger Changes

**Template needs updating when**:
- 3+ projects modify the same section after initialization
- Audit scores consistently low in one dimension across projects
- A new language/framework becomes common in initialized projects
- Industry practices shift (new tooling, new conventions)

**Prompt needs updating when**:
- Users consistently need to ask Claude follow-up questions for the same topic
- Initialization misses something that retrofitting always catches
- The prompt makes assumptions that don't hold for certain project types

**Rubric needs updating when**:
- Projects score perfectly but still have quality gaps
- A rubric dimension doesn't differentiate between good and great
- New best practices emerge that the rubric doesn't capture

## Design Constraints

These constraints should survive across all versions:

1. **CLAUDE.md < 300 lines** - This is a hard limit, not a guideline
2. **Prompts are Markdown** - No executable code in prompt files
3. **Templates use `{{PLACEHOLDER}}`** - Consistent substitution syntax
4. **Three tiers** - Minimal/standard/comprehensive (not two, not four)
5. **Human review required** - Self-improve proposes; humans decide
6. **Append-only feedback** - Project reports are never deleted or modified
7. **Language modules are additive** - New languages don't change core

## Migration Notes

When this bootstrapper is extracted to its own repository:

1. Copy the entire `new-project-setup/` directory as the repo root
2. Move `.claude/commands/{init,retrofit,audit,self-improve}-project.md` to `.claude/commands/`
3. Update path references in prompts from `new-project-setup/` to repo root
4. Create a git tag for the version at extraction time
5. Projects initialized before extraction continue to work (no breaking changes)
